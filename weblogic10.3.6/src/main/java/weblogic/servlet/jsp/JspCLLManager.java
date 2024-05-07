package weblogic.servlet.jsp;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import oracle.jsp.provider.JspResourceProvider;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.JspConfigBean;
import weblogic.j2ee.descriptor.JspPropertyGroupBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.wl.VirtualDirectoryMappingBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.jsp.compiler.Diagnostic;
import weblogic.jsp.compiler.DiagnosticList;
import weblogic.jsp.compiler.IApplication;
import weblogic.jsp.compiler.ICPL;
import weblogic.jsp.compiler.IClientContext;
import weblogic.jsp.compiler.IJavelin;
import weblogic.jsp.compiler.IJavelinFile;
import weblogic.jsp.compiler.ISourceFile;
import weblogic.jsp.compiler.Diagnostic.Severity;
import weblogic.jsp.compiler.client.ClientUtils;
import weblogic.jsp.compiler.jsp.IJspCompilerOptions;
import weblogic.jsp.compiler.jsp.IJspConfigFeature;
import weblogic.jsp.compiler.jsp.IWebAppProjectFeature;
import weblogic.jsp.internal.css.CSSLanguage;
import weblogic.jsp.internal.html.HtmlLanguageX;
import weblogic.jsp.internal.jsp.JspLanguageX;
import weblogic.jsp.internal.jsp.config.JspConfigFeatureX;
import weblogic.jsp.internal.jsp.config.JspPropertyGroup;
import weblogic.jsp.internal.jsp.config.JspPropertySet;
import weblogic.jsp.internal.jsp.config.URLPattern;
import weblogic.jsp.internal.jsp.options.JspCompilerOptionsX;
import weblogic.jsp.internal.jsp.utils.JavaTransformUtils;
import weblogic.jsp.languages.java.IJavaLanguage;
import weblogic.jsp.wlw.filesystem.IFile;
import weblogic.jsp.wlw.filesystem.IFileFilter;
import weblogic.jsp.wlw.util.filesystem.FS;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.servlet.security.internal.WebAppSecurity;
import weblogic.utils.StringUtils;
import weblogic.utils.classloaders.Source;

public class JspCLLManager {
   static final IJavelin JAVELIN = ClientUtils.createCommandLineJavelin(false);
   private static boolean EXTRA_SAVE_USE_BEAN;
   private static boolean DISABLE_SMAP;
   private static final int MAX_DIAGNOSTIC_ERRORS = 100;
   private IJSPCompilerContext compilerContext;
   private IApplication application;
   private ICPL cll;
   private IFile workingDir;
   private boolean addTemporaryCPL;
   private boolean isClosed;
   private boolean keepGenerated;
   private Set _tagFiles;
   private Map _tagFilesModified;

   public static boolean compileJsps(List var0, IJSPCompilerContext var1) throws CompilationException {
      JspCLLManager var2 = null;
      boolean var3 = true;

      try {
         var2 = new JspCLLManager(var1);
         JspConfig var4 = var1.getJspConfig();
         HashSet var5 = new HashSet();
         HashSet var6 = new HashSet();
         StringWriter var7 = null;
         Iterator var8 = var0.iterator();

         while(var8.hasNext()) {
            String var9 = (String)var8.next();
            Source var10 = var1.getSource(var9);
            if (var10 == null) {
               var1.sayError(var9, " Cannot find " + var9 + " in the given docroot");
            } else {
               URI var11 = JavelinxJSPStub.getAbsoluteJspURI(var10, var9);
               if (var11 != null) {
                  IFile var12 = FS.getIFile(var11);
                  if (var12 != null) {
                     var1.say(var9);
                     var5.add(var12);
                     var2.getCLL().addSourceFile(var12);
                  }
               }
            }
         }

         Set var18 = var2.getCLL().getSourceFiles();
         boolean var19 = false;

         try {
            var19 = !var2.build(var18, (IFile)null, var6);
         } catch (InterruptedException var16) {
         }

         if (var19 || var2.getCLL().getFilesWithDiagnostics().size() > 0) {
            if (var7 == null) {
               var7 = new StringWriter();
            }

            boolean var20 = var2.addDiagnostics(true, var7, var6);
            if (var20) {
               var3 = false;
               String var21 = var7.getBuffer().toString();
               if (!var4.isPrecompileContinue()) {
                  throw new CompilationException(var21, (String)null, "", (String)null, (Throwable)null);
               }

               var1.sayError("jspURI", var21);
               if (var7 != null) {
                  var7.getBuffer().setLength(0);
               }

               var6.clear();
               var18.clear();
            }
         }
      } finally {
         if (var2 != null) {
            var2.close();
         }

      }

      return var3;
   }

   public JspCLLManager(IJSPCompilerContext var1) {
      this(var1, false);
   }

   public JspCLLManager(IJSPCompilerContext var1, boolean var2) {
      this._tagFilesModified = new HashMap();
      this.addTemporaryCPL = var2;
      this.compilerContext = var1;
      this.initialize();
   }

   private void initialize() {
      this.initCPL();
      this.initCompilerOptions();
      this.initConfigOptions();
      this.initPaths();
   }

   public ICPL getCLL() {
      return this.cll;
   }

   public boolean build(Set var1, IFile var2, Set var3) throws InterruptedException {
      Object var4 = new HashMap();
      HashMap var5 = new HashMap();
      if (this.keepGenerated) {
         var5.put("-keepGenerated", "true");
         var5.put("-s", this.workingDir.getFile().getAbsolutePath());
      }

      while(var1.size() > 0) {
         var3.addAll(var1);
         var4 = null;
         var4 = ClientUtils.get(this.application).build(this.cll, var1, this.workingDir, var5);
         var1.addAll(this.cll.getSourceFiles());
         var1.removeAll(var3);
      }

      ((Map)var4).putAll(ClientUtils.get(this.application).buildPrototypes(this.cll, this.workingDir, var5));
      return !ClientUtils.hasErrors((Map)var4);
   }

   public boolean addDiagnostics(boolean var1, Writer var2, Set var3) {
      boolean var4 = false;
      HashSet var5 = new HashSet();
      Iterator var6 = var3.iterator();

      while(var6.hasNext()) {
         Object var7 = var6.next();
         if (var7 instanceof ISourceFile) {
            ISourceFile var8 = (ISourceFile)var7;
            if (var8.getCreator() != null) {
               var5.add(var8.getCreator().getIFile());
            } else {
               var5.add(var8.getIFile());
            }
         } else {
            var5.add(var7);
         }
      }

      Map var16 = this.cll.getFilesWithDiagnostics();
      DiagnosticMessageLimitFilter var17 = null;
      PrintWriter var9 = null;
      Iterator var10 = var16.entrySet().iterator();

      while(var10.hasNext()) {
         Map.Entry var11 = (Map.Entry)var10.next();
         IJavelinFile var12 = (IJavelinFile)var11.getKey();
         if (!var12.isBinary() && var5.contains(var12.getIFile())) {
            DiagnosticList var13 = (DiagnosticList)var11.getValue();
            if (var13 != null) {
               List var14 = var13.get(true, Severity.ERROR);
               int var15 = var14.size();
               if (var15 > 0) {
                  var4 = true;
                  if (var9 == null) {
                     var9 = new PrintWriter(var2);
                  }

                  if (var17 == null) {
                     var17 = new DiagnosticMessageLimitFilter(100);
                  }

                  var13.print(true, JavaTransformUtils.isDiagnosticWithAbsolutePath ? var12.getIFile().getDisplayPath() : var12.getName(), var9, var1, Severity.ERROR, var17);
                  if (var17.isMax()) {
                     break;
                  }
               }
            }
         }
      }

      return var4;
   }

   public synchronized void close() {
      try {
         if (!this.isClosed) {
            ClientUtils.get(this.application).close();
         }

         this.cll = null;
         this.application = null;
         this.isClosed = true;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public boolean isClosed() {
      return this.isClosed;
   }

   private void initCPL() {
      String var1 = this.compilerContext.getName();
      if (this.addTemporaryCPL) {
         var1 = var1 + System.currentTimeMillis();
      }

      try {
         this.application = JAVELIN.createApplication(var1);
      } catch (IllegalArgumentException var3) {
         var3.printStackTrace();
         this.application = JAVELIN.createApplication(var1 + System.currentTimeMillis());
      }

      this.cll = this.application.createCPL("CPL");
   }

   private void initCompilerOptions() {
      IJspCompilerOptions var1 = JspCompilerOptionsX.createFeature(this.cll);
      JspConfig var2 = this.compilerContext.getJspConfig();
      String var3 = var2.getWorkingDir();
      IJavaLanguage var4 = JAVELIN.getJavaLanguage();
      if (DISABLE_SMAP) {
         var4.setEmitDebug(0);
      } else if (var2.isDebugEnabled()) {
         var4.setEmitDebug(7);
      }

      this.workingDir = FS.getIFile(new File(var3));
      this.keepGenerated = var2.isKeepGenerated();
      setUpCompilerOptions(var1, var2);
      this.cll.setClassLoader(this.compilerContext.getClassLoader());
   }

   static void setUpCompilerOptions(IJspCompilerOptions var0, JspConfig var1) {
      String var2 = var1.getSuperClassName();
      if (var2 == null || "".equals(var2)) {
         var2 = "weblogic.servlet.jsp.JspBase";
      }

      String var3 = var1.getPackagePrefix();
      if (var3 == null) {
         var3 = "jsp_servlet";
      }

      var0.setCompilerOption("disableEnsureAppDeployment", "true");
      var0.setCompilerOption("packagePrefix", var3);
      var0.setCompilerOption("superclass", var2);
      var0.setCompilerOption("keepgenerated", Boolean.toString(var1.isKeepGenerated()));
      var0.setCompilerOption("printNulls", Boolean.toString(var1.isPrintNulls()));
      var0.setCompilerOption("compilerSupportsEncoding", Boolean.toString(var1.isCompilerSupportsEncoding()));
      var0.setCompilerOption("backwardCompatible", Boolean.toString(var1.isBackwardCompatible()));
      var0.setCompilerOption("rtexprvalueJspParamName", Boolean.toString(var1.isRtexprvalueJspParamName()));
      var0.setCompilerOption("compressHtmlTemplate", Boolean.toString(var1.isCompressHtmlTemplate()));
      var0.setCompilerOption("optimizeJavaExpression", Boolean.toString(var1.isOptimizeJavaExpression()));
      var0.setCompilerOption("useByteBuffer", Boolean.toString(var1.useByteBuffer()));
      if (var1.getCompiler() != null) {
         var0.setCompilerOption("compiler", var1.getCompiler());
      }

      if (var1.getEncoding() != null) {
         var0.setCompilerOption("jspEncoding", var1.getEncoding());
      }

      if (EXTRA_SAVE_USE_BEAN) {
         var0.setCompilerOption("extraSaveBeanToContext", "true");
      }

   }

   private void initConfigOptions() {
      WebAppBean var1 = this.compilerContext.getWebAppBean();
      makeConfigFeature(var1, this.cll, this.compilerContext.getAdditionalExtensions());
   }

   private void initPaths() {
      ArrayList var1 = new ArrayList();
      this.addPaths(var1, this.compilerContext.getClasspath());
      this.cll.initializeBinaryPaths(var1);
      this._tagFiles = new HashSet();
      String[] var2 = this.compilerContext.getSourcePaths();
      ArrayList var3 = new ArrayList();
      if (var2 != null && var2.length > 0) {
         IFile[] var4 = new IFile[var2.length];

         for(int var5 = 0; var5 < var2.length; ++var5) {
            var4[var5] = FS.getIFile(new File(var2[var5]));
            if (var4[var5] != null) {
               var3.add(var4[var5].getCanonicalIFile().getURI());
            }
         }
      }

      this.cll.setFeature(IWebAppProjectFeature.class, new WebAppProjectFeatureImpl(this.cll, this.compilerContext.getContextPath(), (URI[])((URI[])var3.toArray(new URI[var3.size()])), getWebAppVersion(this.compilerContext.getWebAppBean()), getVirtualRootsInfo(this.compilerContext.getWlWebAppBean())));
   }

   private static boolean isWebAppVersionGE2dot4(WebAppBean var0) {
      boolean var1 = false;
      String var2 = getWebAppVersion(var0);

      try {
         var1 = Double.parseDouble(var2) >= 2.4;
      } catch (Exception var4) {
      }

      return var1;
   }

   private static String getWebAppVersion(WebAppBean var0) {
      return var0 == null ? "2.5" : ((DescriptorBean)var0).getDescriptor().getOriginalVersionInfo();
   }

   private boolean isTagFileStale(IFile var1) {
      Object var2 = this._tagFilesModified.get(var1);
      if (var2 != null && (Long)var2 >= var1.lastModified()) {
         return false;
      } else {
         this._tagFilesModified.put(var1, new Long(var1.lastModified()));
         return true;
      }
   }

   static IJspConfigFeature makeConfigFeature(WebAppBean var0, ICPL var1, Set var2) {
      ArrayList var3 = new ArrayList();
      if (var0 != null) {
         JspConfigBean[] var4 = var0.getJspConfigs();
         if (var4 != null && var4.length > 0) {
            JspConfigBean var5 = var4[0];
            if (var5 != null) {
               JspPropertyGroupBean[] var6 = var5.getJspPropertyGroups();
               if (var6 != null) {
                  for(int var7 = 0; var7 < var6.length; ++var7) {
                     fillPropertyGroups(var6[var7], var3);
                  }
               }
            }
         }
      }

      List var8 = fillDefaultGroups(var2);
      if (var8 != null) {
         var3.addAll(var8);
      }

      return JspConfigFeatureX.createFeature(var1, var3, isWebAppVersionGE2dot4(var0));
   }

   private static void fillPropertyGroups(JspPropertyGroupBean var0, List var1) {
      if (var0 != null && var0.getUrlPatterns() != null) {
         String[] var2 = var0.getUrlPatterns();
         Boolean var3 = var0.isElIgnored() ? Boolean.TRUE : Boolean.FALSE;
         Boolean var4 = var0.isScriptingInvalid() ? Boolean.TRUE : Boolean.FALSE;
         String var5 = var0.getPageEncoding();
         List var6 = null;
         if (var0.getIncludePreludes() != null) {
            var6 = Arrays.asList(var0.getIncludePreludes());
         }

         List var7 = null;
         if (var0.getIncludeCodas() != null) {
            var7 = Arrays.asList(var0.getIncludeCodas());
         }

         Boolean var8 = var0.isIsXmlSet() ? (var0.isIsXml() ? Boolean.TRUE : Boolean.FALSE) : null;
         Boolean var9 = var0.isDeferredSyntaxAllowedAsLiteral() ? Boolean.TRUE : Boolean.FALSE;
         Boolean var10 = var0.isTrimDirectiveWhitespaces() ? Boolean.TRUE : Boolean.FALSE;
         JspPropertySet var11 = new JspPropertySet(var3, var4, var5, var6, var7, var8, var9, var10);

         for(int var12 = 0; var12 < var2.length; ++var12) {
            try {
               URLPattern var13 = new URLPattern(var2[var12], true);
               var1.add(new JspPropertyGroup(var13, var11));
            } catch (IllegalArgumentException var14) {
            }
         }

      }
   }

   private static List fillDefaultGroups(Set var0) {
      if (var0 == null) {
         return null;
      } else {
         ArrayList var1 = null;
         Boolean var2 = Boolean.FALSE;
         Boolean var3 = Boolean.FALSE;
         Boolean var4 = Boolean.FALSE;
         Boolean var5 = Boolean.FALSE;
         JspPropertySet var6 = new JspPropertySet(var2, var3, (String)null, (List)null, (List)null, (Boolean)null, var4, var5);
         Iterator var7 = var0.iterator();

         while(var7.hasNext()) {
            String var8 = (String)var7.next();
            if (var1 == null) {
               var1 = new ArrayList(var0.size());
            }

            URLPattern var9 = new URLPattern(var8, true);
            var1.add(new JspPropertyGroup(var9, var6));
         }

         return var1;
      }
   }

   private void addPaths(List var1, String var2) {
      String[] var3 = StringUtils.splitCompletely(var2, File.pathSeparator);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         File var5 = new File(var3[var4]);
         if (var5.exists()) {
            var1.add(FS.getIFile(var5));
         }
      }

   }

   private void addLooseClassFiles(Set var1, IFile var2) {
      if (var2 != null && var2.exists()) {
         IFile[] var3 = var2.listIFiles(new IFileFilter() {
            public boolean accept(IFile var1) {
               if ("class".equals(var1.getNameExtension(false))) {
                  return true;
               } else {
                  return var1.isDirectory();
               }
            }
         });
         if (var3 != null) {
            for(int var4 = 0; var4 < var3.length; ++var4) {
               if (var3[var4].isDirectory()) {
                  this.addLooseClassFiles(var1, var3[var4]);
               } else {
                  var1.add(var3[var4]);
               }
            }
         }
      }

   }

   private static void getTagFiles(IFile var0, Set var1) {
      if (var0 != null && var0.exists()) {
         IFile[] var2 = var0.listIFiles();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (!var2[var3].getNameExtension(false).equals("tag") && !var2[var3].getNameExtension(false).equals("tagx")) {
               if (var2[var3].isDirectory()) {
                  getTagFiles(var2[var3], var1);
               }
            } else {
               var1.add(var2[var3]);
            }
         }
      }

   }

   public static void setWebAppProjectFeature(ICPL var0, String var1, URI[] var2, String var3, VirtualRootInfo[] var4, JspResourceProvider var5) {
      var0.setFeature(IWebAppProjectFeature.class, new WebAppProjectFeatureImpl(var0, var1, var2, var3, var4, var5));
   }

   static VirtualRootInfo[] getVirtualRootsInfo(WeblogicWebAppBean var0) {
      if (var0 == null) {
         return null;
      } else {
         VirtualDirectoryMappingBean[] var1 = var0.getVirtualDirectoryMappings();
         if (var1 != null && var1.length != 0) {
            VirtualRootInfo[] var2 = new VirtualRootInfo[var1.length];

            for(int var3 = 0; var3 < var1.length; ++var3) {
               String[] var4 = var1[var3].getUrlPatterns();
               if (var4 == null || var4.length == 0) {
                  var4 = new String[]{WebAppSecurity.fixupURLPattern("*")};
               }

               try {
                  File var5 = (new File(var1[var3].getLocalPath())).getCanonicalFile();
                  IFile var6 = FS.getIFile(var5);
                  if (var6 != null) {
                     var2[var3] = new VirtualRootInfo(var6.getURI(), var4);
                  } else {
                     var2[var3] = null;
                  }
               } catch (IOException var7) {
                  var2[var3] = null;
               }
            }

            return var2;
         } else {
            return null;
         }
      }
   }

   static {
      FS.initialize();
      JAVELIN.addLanguage(new JspLanguageX(JAVELIN));
      JAVELIN.addLanguage(new HtmlLanguageX(JAVELIN));
      JAVELIN.addLanguage(new CSSLanguage(JAVELIN));
      EXTRA_SAVE_USE_BEAN = Boolean.getBoolean("weblogic.jsp.extraSaveUseBean");
      DISABLE_SMAP = Boolean.getBoolean("weblogic.jsp.disableSMAP");
   }

   private static class DiagnosticMessageLimitFilter implements DiagnosticList.Filter {
      private final int max;
      private int count;

      DiagnosticMessageLimitFilter(int var1) {
         this.max = var1;
         this.count = 0;
      }

      public boolean accept(Diagnostic var1) {
         if (!this.isMax() && var1 != null) {
            ++this.count;
            return true;
         } else {
            return false;
         }
      }

      boolean isMax() {
         return this.count >= this.max;
      }
   }

   public static class JspClientContext implements IClientContext {
      private boolean hasException = false;
      private Throwable exception;
      private String path;

      JspClientContext() {
      }

      public void addFileListener(IFile var1) {
      }

      public void exceptionEncountered(Throwable var1, IFile var2) {
         this.path = var2.getDisplayPath();
         this.exception = var1;
         this.hasException = true;
      }

      public void removeFileListener(IFile var1) {
      }

      public void synchronizeFileListeners() {
      }

      public boolean hasException() {
         return this.hasException;
      }

      public Throwable getException() {
         return this.exception;
      }

      public String getPath() {
         return this.path;
      }
   }

   static class VirtualRootInfo {
      private URI virtualRoot;
      private String[] urlPatterns;

      public VirtualRootInfo(URI var1, String[] var2) {
         this.virtualRoot = var1;
         this.urlPatterns = var2;
      }
   }

   private static class WebAppProjectFeatureImpl implements IWebAppProjectFeature {
      private ICPL _cpl;
      private String _contextPath;
      private URI[] _roots;
      private VirtualRootInfo[] _vroots;
      private String _version;
      private JspResourceProvider _resourceProvider;

      public WebAppProjectFeatureImpl(ICPL var1, String var2, URI[] var3, String var4, VirtualRootInfo[] var5) {
         this._resourceProvider = null;
         this._cpl = var1;
         this._contextPath = var2;
         this._roots = var3;
         this._vroots = var5;
         this._version = var4;
      }

      public WebAppProjectFeatureImpl(ICPL var1, String var2, URI[] var3, String var4, VirtualRootInfo[] var5, JspResourceProvider var6) {
         this(var1, var2, var3, var4, var5);
         this._resourceProvider = var6;
      }

      public String getContextPath() {
         return this._contextPath;
      }

      public ICPL getCPL() {
         return this._cpl;
      }

      public URI[] getRoots() {
         return this._roots;
      }

      public URI findRoot(String var1) {
         try {
            URI var2 = new URI(var1);

            for(int var3 = 0; var3 < this._roots.length; ++var3) {
               URI var4 = this._roots[var3].relativize(var2);
               if (!var4.equals(var2)) {
                  return this._roots[var3];
               }
            }
         } catch (URISyntaxException var5) {
         }

         for(int var6 = 0; var6 < this._roots.length; ++var6) {
            IFile var7 = FS.getIFile(FS.getIFile(this._roots[var6]), var1);
            if (var7 != null && var7.exists()) {
               return this._roots[var6];
            }
         }

         return null;
      }

      public URI findResource(String var1) {
         if (var1 == null) {
            return null;
         } else {
            for(int var2 = 0; var2 < this._roots.length; ++var2) {
               IFile var3 = FS.getIFile(FS.getIFile(this._roots[var2]), var1);
               if (var3 != null && var3.exists()) {
                  String var4 = var3.getURI().normalize().toString();
                  String var5 = this._roots[var2].normalize().toString();
                  if (var4.startsWith(var5)) {
                     return var3.getURI();
                  }
               }
            }

            if (this._vroots != null && this._vroots.length != 0) {
               URLPattern var8 = new URLPattern(var1, false);

               for(int var9 = 0; var9 < this._vroots.length; ++var9) {
                  if (this._vroots[var9] != null) {
                     for(int var10 = 0; var10 < this._vroots[var9].urlPatterns.length; ++var10) {
                        if (this.matchPattern(this._vroots[var9].urlPatterns[var10], var8)) {
                           IFile var11 = FS.getIFile(FS.getIFile(this._vroots[var9].virtualRoot), var1);
                           if (var11 != null) {
                              String var6 = var11.getURI().normalize().getSchemeSpecificPart();
                              String var7 = this._vroots[var9].virtualRoot.normalize().toString();
                              if (var6.startsWith(var7)) {
                                 return var11.getURI();
                              }
                           }
                        }
                     }
                  }
               }

               return null;
            } else {
               return null;
            }
         }
      }

      private boolean matchPattern(String var1, URLPattern var2) {
         URLPattern var3 = new URLPattern(var1, true);
         if (var3.getExtension() == null) {
            if (var3.getPath() == null || var2.getPath() == null) {
               return false;
            }

            if (var2.getPath().startsWith(var3.getPath())) {
               return true;
            }
         } else {
            if (var3.getExtension().equals("*") || var3.getPath() != null && var3.getPath().equals("/")) {
               return true;
            }

            if (var2.getExtension() != null) {
               if (var3.getExtension().equals(var2.getExtension())) {
                  return true;
               }
            } else {
               if (var3.getPath() == null || var2.getPath() == null) {
                  return false;
               }

               if (var2.getPath().startsWith(var3.getPath())) {
                  return true;
               }
            }
         }

         return false;
      }

      public URI[] getVirtualRoots() {
         if (this._vroots != null && this._vroots.length != 0) {
            URI[] var1 = new URI[this._vroots.length];

            for(int var2 = 0; var2 < this._vroots.length; ++var2) {
               var1[var2] = this._vroots[var2].virtualRoot;
            }

            return var1;
         } else {
            return null;
         }
      }

      public String getVersion() {
         return this._version;
      }

      public IWebAppProjectFeature[] getWebAppProjects() {
         return new IWebAppProjectFeature[]{this};
      }

      public JspResourceProvider getResourceProvider() {
         return this._resourceProvider;
      }
   }

   public interface IJSPCompilerContext {
      ClassLoader getClassLoader();

      Source getSource(String var1);

      String getClasspath();

      JspConfig getJspConfig();

      String[] getSourcePaths();

      WebAppBean getWebAppBean();

      WeblogicWebAppBean getWlWebAppBean();

      String getName();

      WebAppServletContext getServletContext();

      String getContextPath();

      void say(String var1);

      void sayError(String var1, String var2);

      Set getAdditionalExtensions();
   }
}
