package weblogic.servlet.internal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import weblogic.application.utils.SecPermSpecUtils;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.utils.DescriptorUtils;
import weblogic.j2ee.descriptor.JspConfigBean;
import weblogic.j2ee.descriptor.JspPropertyGroupBean;
import weblogic.j2ee.descriptor.ServletBean;
import weblogic.j2ee.descriptor.ServletMappingBean;
import weblogic.j2ee.descriptor.TagLibBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.wl.JspDescriptorBean;
import weblogic.j2ee.descriptor.wl.SecurityPermissionBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.management.ManagementException;
import weblogic.security.service.SupplementalPolicyObject;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.JSPServlet;
import weblogic.servlet.jsp.JSPPrecompiler;
import weblogic.servlet.jsp.JspConfig;
import weblogic.servlet.jsp.JspFactoryImpl;
import weblogic.servlet.jsp.JspStub;
import weblogic.utils.http.HttpParsing;
import weblogic.utils.io.FilenameEncoder;

public final class JSPManager {
   private static final char FSC;
   public static final String PAGE_CHECK_SECS = "PageCheckSeconds";
   public static final String JSP_VERBOSE = "Verbose";
   public static final String JSP_KEEP_GENERATED = "Keepgenerated";
   private final WebAppServletContext context;
   private final HashMap jspcArgs = new HashMap();
   private TagLibBean[] taglibs;
   private String userJSPWorkingDir;
   private String jspcPkgPrefix;
   private boolean jspExactMapping = false;
   private String jspServletClazz;
   private final ArrayList jspConfigs;
   private String jspSecuritySpec;
   private boolean jspPrecompileEnabled;
   private Set jspExtensions;
   private final BeanUpdateListener jspBeanListener;
   private String resourceProviderClass;
   private boolean isStrictStaleCheck;
   private boolean pageCheckSecondsSet;
   private int pageCheckSeconds;
   static Map JSP_DESC_ELEMENTS_MAP;
   static final String DEPRECATED_JSP_ELEMENT = "warning";

   JSPManager(WebAppServletContext var1) {
      this.jspServletClazz = JspConfig.DEFAULT_JSP_SERVLET;
      this.jspConfigs = new ArrayList();
      this.jspPrecompileEnabled = false;
      this.isStrictStaleCheck = true;
      this.pageCheckSecondsSet = false;
      this.pageCheckSeconds = HttpServer.isProductionModeEnabled() ? -1 : 1;
      this.context = var1;
      this.jspBeanListener = this.createBeanUpdateListener();
   }

   public boolean isJspExactMapping() {
      return this.jspExactMapping;
   }

   public TagLibBean[] getTagLibs() {
      return this.taglibs;
   }

   public Map getJspConfigArgs() {
      return this.jspcArgs;
   }

   String getJspcPkgPrefix() {
      return this.jspcPkgPrefix;
   }

   public String getResourceProviderClass() {
      return this.resourceProviderClass;
   }

   public String getJSPWorkingDir() {
      if (this.userJSPWorkingDir != null) {
         return this.userJSPWorkingDir;
      } else {
         File var1 = this.context.getRootTempDir();
         String var2 = var1.getAbsolutePath();
         if (var2 == null) {
            var2 = ".";
         }

         return var2;
      }
   }

   public synchronized JspConfig createJspConfig() {
      JspConfig var1 = new JspConfig(this.jspcArgs);
      this.jspConfigs.add(var1);
      return var1;
   }

   boolean isPageCheckSecondsSet() {
      return this.pageCheckSecondsSet;
   }

   int getPageCheckSeconds() {
      return this.pageCheckSeconds;
   }

   void destroy() {
      if (this.jspSecuritySpec != null) {
         String[] var1 = new String[2];
         if (this.context.getDocroot() != null) {
            var1[0] = this.context.getDocroot().replace(FSC, '/');
         }

         String var2 = this.getJSPWorkingDir();
         if (var2 != null) {
            var1[1] = FilenameEncoder.resolveRelativeURIPath(var2.replace(FSC, '/'));
         }

         SupplementalPolicyObject.removePolicies(WebAppConfigManager.KERNEL_ID, var1);
      }

   }

   public void registerTagLibs(JspConfigBean[] var1) {
      if (var1 != null && var1.length != 0) {
         this.taglibs = var1[0].getTagLibs();
      }
   }

   void registerSecurityPermissionSpec(WeblogicWebAppBean var1) {
      SecurityPermissionBean var2 = null;
      if (var1 != null) {
         var2 = (SecurityPermissionBean)DescriptorUtils.getFirstChildOrDefaultBean(var1, var1.getSecurityPermissions(), "SecurityPermission");
      }

      if (var2 != null) {
         this.jspSecuritySpec = var2.getSecurityPermissionSpec();
      }

      String[] var3 = new String[2];
      String var4 = getCanonicalPath(this.context.getDocroot()).replace(FSC, '/');
      var3[0] = var4;
      String var5 = getCanonicalPath(this.getJSPWorkingDir()).replace(FSC, '/');
      var3[1] = var5;
      SupplementalPolicyObject.setPoliciesFromGrantStatement(WebAppConfigManager.KERNEL_ID, var3, this.context.getApplicationContext() == null ? this.jspSecuritySpec : SecPermSpecUtils.getNewSecurityPermissionSpec(this.context.getApplicationContext(), this.jspSecuritySpec), "WEB");
   }

   private static String getCanonicalPath(String var0) {
      try {
         return (new File(var0)).getCanonicalPath();
      } catch (IOException var2) {
         return null;
      }
   }

   void precompileJSPs() throws DeploymentException {
      Thread var1 = Thread.currentThread();
      ClassLoader var2 = var1.getContextClassLoader();

      try {
         Loggable var4;
         try {
            JspConfig var3 = new JspConfig(this.jspcArgs);
            if (HTTPDebugLogger.isEnabled()) {
               var4 = HTTPLogger.logPrecompilingJSPsLoggable(this.context.getAppDisplayName(), this.context.getModuleName(), var3.toString());
               HTTPDebugLogger.debug(var4.getMessage());
            }

            var4 = null;
            Class var5 = Class.forName(JspConfig.COMMON_UTILS.getJspPrecompilerClass());
            JSPPrecompiler var15 = (JSPPrecompiler)var5.newInstance();
            File var6 = new File(this.context.getDocroot());
            var1.setContextClassLoader(this.context.getServletClassLoader());
            var15.compile(this.context, var6);
         } catch (RuntimeException var12) {
            throw var12;
         } catch (Exception var13) {
            var4 = HTTPLogger.logFailureCompilingJSPsLoggable(this.context.getAppDisplayName(), this.context.getModuleName(), var13);
            var4.log();
            throw new DeploymentException(var13);
         }
      } finally {
         var1.setContextClassLoader(var2);
      }

   }

   private void setArg(HashMap var1, String var2, String var3) {
      if (var3 != null) {
         var1.put(var2, var3);
      }

   }

   void setJspParam(String var1, String var2) throws DeploymentException {
      Iterator var3 = this.jspConfigs.iterator();

      while(var3.hasNext()) {
         JspConfig var4 = (JspConfig)var3.next();
         if (var1.equals("workingDir")) {
            if (var2 == null) {
               throw new DeploymentException("JSP 'workingDir' cannot be null");
            }

            this.userJSPWorkingDir = var2;
            if (this.userJSPWorkingDir != null) {
               this.userJSPWorkingDir = this.userJSPWorkingDir.replace('/', File.separatorChar);
               this.ensureWorkingDir(this.userJSPWorkingDir);
            }

            var4.setWorkingDir(var2);
         } else if (var1.equals("pageCheckSeconds")) {
            var4.setPageCheckSecs(Long.parseLong(var2));
         } else if (var1.equals("compileCommand")) {
            var4.setCompileCommand(var2);
         } else if (var1.equals("compilerclass")) {
            var4.setCompilerClass(var2);
         } else if (var1.equals("compileFlags")) {
            var4.setCompileFlagsString(var2);
         } else if (var1.equals("verbose")) {
            var4.setVerbose("true".equalsIgnoreCase(var2));
         } else if (var1.equals("keepgenerated")) {
            var4.setKeepGenerated("true".equalsIgnoreCase(var2));
         } else if (var1.equals("precompileContinue")) {
            var4.setPrecompileContinue("true".equalsIgnoreCase(var2));
         } else if (var1.equals("encoding")) {
            var4.setEncoding(var2);
         } else if (var1.equals("compilerSupportsEncoding")) {
            var4.setCompilerSupportsEncoding("true".equalsIgnoreCase(var2));
         } else if (var1.equals("noTryBlocks")) {
            var4.setNoTryBlocks("true".equalsIgnoreCase(var2));
         } else if (var1.equals("exactMapping")) {
            var4.setExactMapping("true".equalsIgnoreCase(var2));
         } else if (var1.equals("debug")) {
            var4.setDebugEnabled("true".equalsIgnoreCase(var2));
         } else if (var1.equals("compressHtmlTemplate")) {
            var4.setCompressHtmlTemplate("true".equalsIgnoreCase(var2));
         } else {
            if (!var1.equals("optimizeJavaExpression")) {
               throw new DeploymentException("Unrecognized JSP param: " + var1);
            }

            var4.setOptimizeJavaExpression("true".equalsIgnoreCase(var2));
         }
      }

      this.setArg(this.jspcArgs, var1, var2);
   }

   ServletStubImpl registerJSPServletDefinition(ServletBean var1, HashMap var2) throws DeploymentException {
      String var3 = var1.getJspFile();
      if (var3 == null) {
         Loggable var13 = HTTPLogger.logNeedServletClassOrJspFileLoggable(var1.getServletName());
         var13.log();
         throw new DeploymentException(var13.getMessage());
      } else {
         var3 = HttpParsing.ensureStartingSlash(var3);
         String var4 = JSPServlet.uri2classname(this.jspcPkgPrefix, var3);

         try {
            Servlet var6 = (Servlet)Class.forName(this.jspServletClazz).newInstance();
            if (var6 instanceof JSPServlet) {
               JSPServlet var7 = (JSPServlet)var6;
               JspStub var5 = JSPServlet.getNewJspStub(var1.getServletName(), var4, var2, this.context, new JspConfig(this.jspcArgs));
               if (this.context.getRuntimeMBean() != null) {
                  var5.initRuntime();
               }

               var5.setFilePath(var3);
               this.context.registerServletStub(var1.getServletName(), var5);
               this.context.registerServletMap(var1.getServletName(), var3, var5);
               return var5;
            } else {
               throw new DeploymentException("Unrecognized JSP Servlet '" + this.jspServletClazz);
            }
         } catch (ClassNotFoundException var8) {
            throw new DeploymentException("Error initializing JSP Servlet", var8);
         } catch (InstantiationException var9) {
            throw new DeploymentException("Error initializing JSP Servlet", var9);
         } catch (IllegalAccessException var10) {
            throw new DeploymentException("Error initializing JSP Servlet", var10);
         } catch (ServletException var11) {
            throw new DeploymentException("Error initializing JSP Servlet", var11);
         } catch (ManagementException var12) {
            throw new DeploymentException("Error initializing JSP Servlet", var12);
         }
      }
   }

   void registerJspDescriptor(WebAppBean var1, WeblogicWebAppBean var2) throws DeploymentException {
      this.addJspExtensions(var1);
      this.jspcArgs.put("workingDir", this.getJSPWorkingDir());
      this.jspcArgs.put("verbose", "true");
      this.jspExactMapping = true;
      Enumeration var3 = this.context.getInitParameterNames();

      String var6;
      while(var3.hasMoreElements()) {
         String var4 = (String)var3.nextElement();
         int var5 = "weblogic.jsp.".length();
         if (var4.startsWith("weblogic.jsp.") && var4.length() >= var5 + 1) {
            var6 = this.context.getInitParameter(var4);
            var4 = var4.substring(var5);
            this.jspcArgs.put(var4, var6);
         }
      }

      this.jspcArgs.put("rtexprvalueJspParamName", "" + this.context.getConfigManager().isRtexprvalueJspParamName());
      this.jspcArgs.put("backwardCompatible", "" + this.context.getConfigManager().isJSPCompilerBackwardsCompatible());
      if (var2 != null) {
         this.initialize((JspDescriptorBean)DescriptorUtils.getFirstChildOrDefaultBean(var2, var2.getJspDescriptors(), "JspDescriptor"));
      }

      if (!HttpServer.isProductionModeEnabled() && JspConfig.checkCompilerClass() && this.jspcArgs != null && this.jspcArgs.get("compilerClass") == null) {
         String var7 = (String)this.jspcArgs.get("compileCommand");
         if (var7 == null) {
            this.setArg(this.jspcArgs, "compilerClass", "com.sun.tools.javac.Main");
         }
      }

      if (this.jspcPkgPrefix == null) {
         this.jspcPkgPrefix = "jsp_servlet";
      }

      HashMap var8 = new HashMap();
      if (!this.context.getConfigManager().isImplicitServletMappingDisabled()) {
         this.context.registerServlet("JspServlet", "*.jsp", this.jspServletClazz, var8);
         this.context.registerServlet("JspServlet", "*.jspx", this.jspServletClazz, var8);
      }

      Set var9 = getJspConfigPatterns(var1.getJspConfigs());
      if (var9 != null) {
         Iterator var10 = var9.iterator();

         while(var10.hasNext()) {
            var6 = (String)var10.next();
            if (null == this.context.getServletStub(var6)) {
               this.context.registerServlet("JspServlet", var6, this.jspServletClazz, var8);
            }
         }
      }

   }

   private void initialize(JspDescriptorBean var1) throws DeploymentException {
      if (var1 != null) {
         this.userJSPWorkingDir = var1.getWorkingDir();
         if (this.userJSPWorkingDir != null) {
            this.userJSPWorkingDir = this.userJSPWorkingDir.replace('/', File.separatorChar);
            this.ensureWorkingDir(this.userJSPWorkingDir);
         }

         this.jspPrecompileEnabled = var1.isPrecompile();
         this.jspcPkgPrefix = var1.getPackagePrefix();
         this.jspExactMapping = var1.isExactMapping();
         this.resourceProviderClass = var1.getResourceProviderClass();
         this.isStrictStaleCheck = var1.isStrictStaleCheck();
         if (var1.getEncoding() != null) {
            HTTPLogger.logDeprecatedEncodingPropertyUsed(this.context.getLogContext());
         }

         DescriptorBean var2 = (DescriptorBean)var1;
         if (var2.isSet("RtexprvalueJspParamName")) {
            this.setArg(this.jspcArgs, "rtexprvalueJspParamName", "" + var1.isRtexprvalueJspParamName());
         }

         if (var2.isSet("BackwardCompatible")) {
            this.setArg(this.jspcArgs, "backwardCompatible", "" + var1.isBackwardCompatible());
         }

         if (var1.isPageCheckSecondsSet()) {
            this.pageCheckSecondsSet = true;
            this.pageCheckSeconds = var1.getPageCheckSeconds();
         }

         this.setArg(this.jspcArgs, "encoding", var1.getEncoding());
         this.setArg(this.jspcArgs, "keepgenerated", "" + var1.isKeepgenerated());
         this.setArg(this.jspcArgs, "superclass", var1.getSuperClass());
         this.setArg(this.jspcArgs, "pageCheckSeconds", "" + var1.getPageCheckSeconds());
         this.setArg(this.jspcArgs, "index.jsp", var1.getDefaultFileName());
         this.setArg(this.jspcArgs, "workingDir", var1.getWorkingDir());
         this.setArg(this.jspcArgs, "packagePrefix", var1.getPackagePrefix());
         this.setArg(this.jspcArgs, "printNulls", "" + var1.isPrintNulls());
         this.setArg(this.jspcArgs, "exactMapping", "" + var1.isExactMapping());
         this.setArg(this.jspcArgs, "verbose", "" + var1.isVerbose());
         this.setArg(this.jspcArgs, "debug", "" + var1.isDebug());
         this.setArg(this.jspcArgs, "compressHtmlTemplate", "" + var1.isCompressHtmlTemplate());
         this.setArg(this.jspcArgs, "optimizeJavaExpression", "" + var1.isOptimizeJavaExpression());
         if (this.jspPrecompileEnabled) {
            this.setArg(this.jspcArgs, "precompileContinue", "" + var1.isPrecompileContinue());
         }

         if (var1.getResourceProviderClass() != null) {
            this.setArg(this.jspcArgs, "resourceProviderClass", var1.getResourceProviderClass());
         }

      }
   }

   private void ensureWorkingDir(String var1) throws DeploymentException {
      File var2 = new File(var1);
      if (!var2.exists()) {
         if (!var2.mkdirs()) {
            if (!var2.exists()) {
               System.out.println("*** Debug: WORKINGDIR:" + var2.getAbsolutePath());
               throw new DeploymentException("Working directory: " + var1 + " was not found and could not be created");
            }
         }
      }
   }

   boolean isJspPrecompileEnabled() {
      return this.jspPrecompileEnabled;
   }

   boolean isStrictStaleCheck() {
      return this.isStrictStaleCheck;
   }

   public static Set getJspConfigPatterns(JspConfigBean[] var0) {
      if (var0 != null && var0.length != 0) {
         JspConfigBean var1 = var0[0];
         JspPropertyGroupBean[] var2 = var1.getJspPropertyGroups();
         if (var2 == null) {
            return null;
         } else {
            HashSet var3 = null;

            for(int var4 = 0; var4 < var2.length; ++var4) {
               String[] var5 = var2[var4].getUrlPatterns();

               for(int var6 = 0; var6 < var5.length; ++var6) {
                  String var7 = var5[var6];
                  if (!"*.jspx".equals(var7)) {
                     if (var3 == null) {
                        var3 = new HashSet();
                     }

                     if (!var7.startsWith("*.")) {
                        var7 = HttpParsing.ensureStartingSlash(var7);
                     }

                     var3.add(var7);
                  }
               }
            }

            return var3;
         }
      } else {
         return null;
      }
   }

   void addJspExtensions(WebAppBean var1) {
      ServletBean[] var2 = var1.getServlets();
      ArrayList var3 = null;
      if (var2 != null) {
         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (JspConfig.DEFAULT_JSP_SERVLET.equals(var2[var4].getServletClass())) {
               if (var3 == null) {
                  var3 = new ArrayList();
               }

               var3.add(var2[var4].getServletName());
            }
         }

         if (var3 != null && var3.size() > 0) {
            ServletMappingBean[] var7 = var1.getServletMappings();
            if (var7 == null) {
               return;
            }

            for(int var5 = 0; var5 < var7.length; ++var5) {
               String var6 = var7[var5].getServletName();
               if (var3.contains(var6)) {
                  if (this.jspExtensions == null) {
                     this.jspExtensions = new HashSet();
                  }

                  if (var7[var5].getUrlPatterns() != null && var7[var5].getUrlPatterns().length > 0) {
                     this.jspExtensions.addAll(Arrays.asList(var7[var5].getUrlPatterns()));
                  }
               }
            }
         }
      }

   }

   public Set getJspExtensions() {
      return this.jspExtensions;
   }

   BeanUpdateListener getBeanUpdateListener() {
      return this.jspBeanListener;
   }

   private BeanUpdateListener createBeanUpdateListener() {
      return new WebComponentBeanUpdateListener() {
         protected void handlePropertyRemove(BeanUpdateEvent.PropertyUpdate var1) {
            String var2 = var1.getPropertyName();
            if ("PageCheckSeconds".equals(var2)) {
               JSPManager.this.pageCheckSecondsSet = false;
               JSPManager.this.pageCheckSeconds = HttpServer.isProductionModeEnabled() ? -1 : 1;
            }

            Iterator var3 = JSPManager.this.jspConfigs.iterator();

            while(var3.hasNext()) {
               JspConfig var4 = (JspConfig)var3.next();
               if ("Verbose".equals(var2)) {
                  var4.setVerbose(true);
                  JSPManager.this.setArg(JSPManager.this.jspcArgs, "verbose", "ture");
               }

               if ("Keepgenerated".equals(var2)) {
                  var4.setKeepGenerated(false);
                  JSPManager.this.setArg(JSPManager.this.jspcArgs, "keepgenerated", "false");
               }

               if ("PageCheckSeconds".equals(var2)) {
                  var4.setPageCheckSecs(HttpServer.isProductionModeEnabled() ? -1L : 1L);
                  JSPManager.this.setArg(JSPManager.this.jspcArgs, "pageCheckSeconds", "" + var4.getPageCheckSecs());
               }
            }

         }

         protected void handlePropertyChange(BeanUpdateEvent.PropertyUpdate var1, DescriptorBean var2) {
            JspDescriptorBean var3 = (JspDescriptorBean)var2;
            String var4 = var1.getPropertyName();
            if (var4.equals("PageCheckSeconds") && var3 != null) {
               JSPManager.this.pageCheckSecondsSet = true;
               JSPManager.this.pageCheckSeconds = var3.getPageCheckSeconds();
            }

            Iterator var5 = JSPManager.this.jspConfigs.iterator();

            while(var5.hasNext()) {
               JspConfig var6 = (JspConfig)var5.next();
               if (var4.equals("Verbose")) {
                  var6.setVerbose(var3 != null && var3.isVerbose());
                  JSPManager.this.setArg(JSPManager.this.jspcArgs, "verbose", "" + var6.isVerbose());
               }

               if (var4.equals("Keepgenerated")) {
                  var6.setKeepGenerated(var3 != null && var3.isKeepgenerated());
                  JSPManager.this.setArg(JSPManager.this.jspcArgs, "keepgenerated", "" + var6.isKeepGenerated());
               }

               if (var4.equals("PageCheckSeconds")) {
                  var6.setPageCheckSecs(var3 != null ? (long)var3.getPageCheckSeconds() : (long)(HttpServer.isProductionModeEnabled() ? -1 : 1));
                  JSPManager.this.setArg(JSPManager.this.jspcArgs, "pageCheckSeconds", "" + var6.getPageCheckSecs());
               }
            }

         }

         protected void prepareBeanAdd(BeanUpdateEvent.PropertyUpdate var1, DescriptorBean var2) throws BeanUpdateRejectedException {
            if (var2 instanceof JspDescriptorBean) {
               WeblogicWebAppBean var3 = JSPManager.this.context.getWebAppModule().getWlWebAppBean();
               JspDescriptorBean var4 = (JspDescriptorBean)var2;
               JspDescriptorBean var5 = (JspDescriptorBean)DescriptorUtils.getFirstChildOrDefaultBean(var3, var3.getContainerDescriptors(), "JspDescriptor");
               ArrayList var6 = new ArrayList();
               computeChange("package-prefix", var4.getPackagePrefix(), var5.getPackagePrefix(), var6);
               computeChange("super-class", var4.getSuperClass(), var5.getSuperClass(), var6);
               computeChange("precompile", var4.isPrecompile(), var5.isPrecompile(), var6);
               computeChange("precompile-continue", var4.isPrecompileContinue(), var5.isPrecompileContinue(), var6);
               computeChange("working-dir", var4.getWorkingDir(), var5.getWorkingDir(), var6);
               computeChange("print-nulls", var4.isPrintNulls(), var5.isPrintNulls(), var6);
               computeChange("backward-compatible", var4.isBackwardCompatible(), var5.isBackwardCompatible(), var6);
               computeChange("encoding", var4.getEncoding(), var5.getEncoding(), var6);
               computeChange("exact-mapping", var4.isExactMapping(), var5.isExactMapping(), var6);
               computeChange("default-file-name", var4.getDefaultFileName(), var5.getDefaultFileName(), var6);
               computeChange("rtexprvalue-jsp-param-name", var4.isRtexprvalueJspParamName(), var5.isRtexprvalueJspParamName(), var6);
               computeChange("debug", var4.isDebug(), var5.isDebug(), var6);
               computeChange("compress-html-template", var4.isCompressHtmlTemplate(), var5.isCompressHtmlTemplate(), var6);
               computeChange("optimize-java-expression", var4.isOptimizeJavaExpression(), var5.isOptimizeJavaExpression(), var6);
               computeChange("resource-provider-class", var4.getResourceProviderClass(), var5.getResourceProviderClass(), var6);
               if (!var6.isEmpty()) {
                  throw new BeanUpdateRejectedException("Non-Dynamic property in \"jsp-descriptor\" is/are specified in deployment plan: '" + getChangedPropertyNames(var6) + "'");
               }
            }
         }

         protected void handleBeanAdd(BeanUpdateEvent.PropertyUpdate var1, DescriptorBean var2) {
            if (var2 instanceof JspDescriptorBean && var1 != null && "JspDescriptors".equals(var1.getPropertyName())) {
               JspDescriptorBean var3 = (JspDescriptorBean)var2;
               ((DescriptorBean)var3).addBeanUpdateListener(this);
               if (var3.isPageCheckSecondsSet()) {
                  JSPManager.this.pageCheckSecondsSet = true;
                  JSPManager.this.pageCheckSeconds = var3.getPageCheckSeconds();
               }

               Iterator var4 = JSPManager.this.jspConfigs.iterator();

               while(var4.hasNext()) {
                  JspConfig var5 = (JspConfig)var4.next();
                  var5.setVerbose(var3 != null && var3.isVerbose());
                  JSPManager.this.setArg(JSPManager.this.jspcArgs, "verbose", "" + var5.isVerbose());
                  var5.setKeepGenerated(var3 != null && var3.isKeepgenerated());
                  JSPManager.this.setArg(JSPManager.this.jspcArgs, "keepgenerated", "" + var5.isKeepGenerated());
                  var5.setPageCheckSecs(var3 != null ? (long)var3.getPageCheckSeconds() : (long)(HttpServer.isProductionModeEnabled() ? -1 : 1));
                  JSPManager.this.setArg(JSPManager.this.jspcArgs, "pageCheckSeconds", "" + var5.getPageCheckSecs());
               }

            }
         }

         protected void handleBeanRemove(BeanUpdateEvent.PropertyUpdate var1) {
            if ("JspDescriptors".equals(var1.getPropertyName()) && var1 != null && var1.getRemovedObject() instanceof JspDescriptorBean) {
               JspDescriptorBean var2 = (JspDescriptorBean)var1.getRemovedObject();
               ((DescriptorBean)var2).removeBeanUpdateListener(this);
               JSPManager.this.pageCheckSecondsSet = false;
               JSPManager.this.pageCheckSeconds = HttpServer.isProductionModeEnabled() ? -1 : 1;
               Iterator var3 = JSPManager.this.jspConfigs.iterator();

               while(var3.hasNext()) {
                  JspConfig var4 = (JspConfig)var3.next();
                  var4.setVerbose(true);
                  JSPManager.this.setArg(JSPManager.this.jspcArgs, "verbose", "true");
                  var4.setKeepGenerated(false);
                  JSPManager.this.setArg(JSPManager.this.jspcArgs, "keepgenerated", "false");
                  var4.setPageCheckSecs(HttpServer.isProductionModeEnabled() ? -1L : 1L);
                  JSPManager.this.setArg(JSPManager.this.jspcArgs, "pageCheckSeconds", "" + var4.getPageCheckSecs());
               }

            }
         }
      };
   }

   static {
      FSC = File.separatorChar;
      JSP_DESC_ELEMENTS_MAP = new HashMap();
      JspFactoryImpl.init();
      JSP_DESC_ELEMENTS_MAP.put("backwardCompatible".toLowerCase(), "backward-compatible");
      JSP_DESC_ELEMENTS_MAP.put("encoding".toLowerCase(), "encoding");
      JSP_DESC_ELEMENTS_MAP.put("exactMapping".toLowerCase(), "exact-mapping");
      JSP_DESC_ELEMENTS_MAP.put("keepgenerated".toLowerCase(), "keepgenerated");
      JSP_DESC_ELEMENTS_MAP.put("packagePrefix".toLowerCase(), "package-prefix");
      JSP_DESC_ELEMENTS_MAP.put("pageCheckSeconds".toLowerCase(), "page-check-seconds");
      JSP_DESC_ELEMENTS_MAP.put("precompile".toLowerCase(), "precompile");
      JSP_DESC_ELEMENTS_MAP.put("precompileContinue".toLowerCase(), "precompile-continue");
      JSP_DESC_ELEMENTS_MAP.put("printNulls".toLowerCase(), "print-nulls");
      JSP_DESC_ELEMENTS_MAP.put("superclass".toLowerCase(), "super-class");
      JSP_DESC_ELEMENTS_MAP.put("verbose".toLowerCase(), "verbose");
      JSP_DESC_ELEMENTS_MAP.put("workingDir".toLowerCase(), "working-dir");
      JSP_DESC_ELEMENTS_MAP.put("compileCommand".toLowerCase(), "warning");
      JSP_DESC_ELEMENTS_MAP.put("compilerClass".toLowerCase(), "warning");
      JSP_DESC_ELEMENTS_MAP.put("compileFlags".toLowerCase(), "warning");
      JSP_DESC_ELEMENTS_MAP.put("compilerSupportsEncoding".toLowerCase(), "warning");
      JSP_DESC_ELEMENTS_MAP.put("defaultFileName".toLowerCase(), "default-file-name");
      JSP_DESC_ELEMENTS_MAP.put("noTryBlocks".toLowerCase(), "warning");
      JSP_DESC_ELEMENTS_MAP.put("jspServlet".toLowerCase(), "warning");
      JSP_DESC_ELEMENTS_MAP.put("jspPrecompiler".toLowerCase(), "warning");
   }
}
