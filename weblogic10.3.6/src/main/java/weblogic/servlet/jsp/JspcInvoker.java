package weblogic.servlet.jsp;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import weblogic.application.library.LibraryManager;
import weblogic.descriptor.utils.DescriptorUtils;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.wl.CharsetMappingBean;
import weblogic.j2ee.descriptor.wl.CharsetParamsBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.JspDescriptorBean;
import weblogic.j2ee.descriptor.wl.ModuleOverrideBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.j2ee.validation.ModuleValidationInfo;
import weblogic.servlet.internal.CharsetMap;
import weblogic.servlet.internal.War;
import weblogic.servlet.internal.WebAppDescriptor;
import weblogic.servlet.internal.dd.compliance.ComplianceUtils;
import weblogic.servlet.internal.dd.compliance.DeploymentInfo;
import weblogic.servlet.utils.WarUtils;
import weblogic.servlet.utils.WebAppLibraryUtils;
import weblogic.tools.ui.progress.ProgressListener;
import weblogic.utils.ErrorCollectionException;
import weblogic.utils.FileUtils;
import weblogic.utils.Getopt2;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.compiler.ToolFailureException;
import weblogic.utils.jars.JarFileUtils;
import weblogic.utils.jars.VirtualJarFile;
import weblogic.webservice.tools.cchecker.ComplianceChecker;

public class JspcInvoker {
   private static final boolean debug = false;
   private static final String WEB_XML = "WEB-INF/web.xml";
   private static final String JSPC_TEMP_DIR = ".jspcgen_" + System.currentTimeMillis();
   private static final String tmpDirRoot = System.getProperty("java.io.tmpdir");
   final Getopt2 opts;
   private boolean isExtracted;
   private String warName;
   private ProgressListener listener;
   private WebAppBean bean;
   private WeblogicWebAppBean wlBean;

   public JspcInvoker(Getopt2 var1) {
      this.opts = var1;
   }

   public JspcInvoker(Getopt2 var1, ProgressListener var2) {
      this.opts = var1;
      this.listener = var2;
   }

   public void checkCompliance(GenericClassLoader var1, VirtualJarFile var2, File var3, ModuleValidationInfo var4, boolean var5) throws ErrorCollectionException {
      this.checkCompliance(var1, var2, var3, (File)null, (DeploymentPlanBean)null, var4, var5);
   }

   public void checkCompliance(GenericClassLoader var1, VirtualJarFile var2, File var3, File var4, DeploymentPlanBean var5, ModuleValidationInfo var6, boolean var7) throws ErrorCollectionException {
      try {
         String var8 = var6 == null ? this.getUriFromPlan(var5) : var6.getURI();
         this.parseDescriptors(var3, var2, var4, var5, var8);
      } catch (IOException var9) {
         throw new ErrorCollectionException(var9);
      } catch (XMLStreamException var10) {
         throw new ErrorCollectionException(var10);
      }

      this.checkCompliance(var1, var6, var7, var2);
   }

   private String getUriFromPlan(DeploymentPlanBean var1) {
      if (var1 == null) {
         return null;
      } else {
         ModuleOverrideBean[] var2 = var1.getModuleOverrides();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            ModuleOverrideBean var4 = var2[var3];
            if (var1.rootModule(var4.getModuleName())) {
               return var4.getModuleName();
            }
         }

         return null;
      }
   }

   private void checkCompliance(GenericClassLoader var1, ModuleValidationInfo var2, boolean var3, VirtualJarFile var4) throws ErrorCollectionException {
      DeploymentInfo var5 = new DeploymentInfo();
      var5.setWebAppBean(this.bean);
      var5.setWeblogicWebAppBean(this.wlBean);
      var5.setClassLoader(var1);
      var5.setModuleValidationInfo(var2);
      var5.setVerbose(var3);
      var5.setIsWebServiceModule(var4.getEntry("WEB-INF/webservices.xml") != null);
      if (var3) {
         p("Checking web app for compliance.");
      }

      ComplianceUtils.checkCompliance(var5, this.listener);
   }

   public void compile(GenericClassLoader var1, VirtualJarFile var2, File var3, ModuleValidationInfo var4, LibraryManager var5) throws ErrorCollectionException {
      this.compile(var1, var2, var3, (File)null, (DeploymentPlanBean)null, var4, var5);
   }

   public void compile(GenericClassLoader var1, VirtualJarFile var2, File var3, File var4, DeploymentPlanBean var5, ModuleValidationInfo var6, LibraryManager var7) throws ErrorCollectionException {
      this.ensureClean();
      File var8 = getJspcTempDir();
      ClassFinder var9 = null;
      ErrorCollectionException var10 = null;

      try {
         War var11 = new War("jspc", var8, var2);
         File var12 = this.determineDocroot(var2);
         String var13 = var6 == null ? this.getUriFromPlan(var5) : var6.getURI();
         this.parseDescriptors(var3, var2, var4, var5, var13);
         boolean var14 = this.opts.getBooleanOption("verbose");
         if (this.wlBean != null) {
            processLibraries(this.wlBean, var11, var8, var7, var6 == null ? null : var6.getURI());
         }

         var9 = var11.getClassFinder();
         var1.addClassFinder(var9);

         try {
            this.checkCompliance(var1, var6, var14, var2);
         } catch (ErrorCollectionException var27) {
            var10 = var27;
         }

         if (var2.getEntry("WEB-INF/web-services.xml") != null) {
            try {
               if (var14) {
                  p("Checking web services for compliance.");
               }

               ComplianceChecker var15 = new ComplianceChecker((String[])null);
               var15.runChecker(var2, var1, var14);
            } catch (Exception var28) {
               if (var10 == null) {
                  var10 = new ErrorCollectionException(var28);
               } else {
                  var10.add(var28);
               }
            }
         }

         if (var10 == null || var10.isEmpty()) {
            String[] var31 = this.validateToolInputs(var12);
            Object var16 = null;

            try {
               Class var17 = Class.forName("weblogic.servlet.jsp.jspc20");
               Constructor var18 = var17.getConstructor(String[].class);
               var16 = var18.newInstance(var31);
            } catch (Exception var26) {
               var26.printStackTrace();
               throw var26;
            }

            if (var16 != null) {
               IJspc var32 = (IJspc)var16;
               var32.setWebBean(this.bean);
               var32.setWlWebBean(this.wlBean);
               ClassFinder var33 = var11.getResourceFinder("/");
               var32.runJspc(var1, var33, var2);
            }

            if (this.isExtracted) {
               if (this.warName != null) {
                  JarFileUtils.createJarFileFromDirectory(this.warName, var12);
               }

               FileUtils.remove(var12);
            }

            if (var9 != null) {
               var9.close();
               var9 = null;
            }

            if (var11 != null) {
               var11.remove();
               FileUtils.remove(var8);
            }
         }
      } catch (Exception var29) {
         if (var10 == null) {
            var10 = new ErrorCollectionException();
         }

         var10.add(var29);
         throw var10;
      } finally {
         if (var9 != null) {
            var9.close();
         }

      }

      if (var10 != null && !var10.isEmpty()) {
         throw var10;
      }
   }

   private void parseDescriptors(File var1, VirtualJarFile var2, File var3, DeploymentPlanBean var4, String var5) throws IOException, XMLStreamException {
      try {
         WebAppDescriptor var6 = WarUtils.getWebAppDescriptor(var1, var2, var3, var4, var5);
         this.bean = var6.getWebAppBean();
         this.wlBean = var6.getWeblogicWebAppBean();
      } catch (FileNotFoundException var7) {
      }

   }

   private String[] validateToolInputs(File var1) {
      String var2 = this.determineTargetDir(var1);
      ArrayList var3 = new ArrayList();
      if (this.opts.getBooleanOption("verbose")) {
         var3.add("-verboseJspc");
      }

      if (this.opts.getBooleanOption("keepgenerated")) {
         var3.add("-keepgenerated");
      }

      if (this.opts.getBooleanOption("forceGeneration")) {
         var3.add("-forceGeneration");
      }

      if (this.opts.getBooleanOption("k")) {
         var3.add("-k");
      }

      if (this.opts.getBooleanOption("useByteBuffer")) {
         var3.add("-useByteBuffer");
      }

      var3.add("-d");
      var3.add(this.opts.getOption("d", var2));
      var3.add("-noexit");
      if (this.opts.hasOption("jsps")) {
         var3.add("-jsps");
         var3.add(this.opts.getOption("jsps"));
      }

      if (this.opts.getBooleanOption("compileAllTagFiles")) {
         var3.add("-compileAllTagFiles");
      }

      if (this.opts.hasOption("compiler")) {
         var3.add("-compiler");
         var3.add(this.opts.getOption("compiler"));
      }

      return (String[])((String[])var3.toArray(new String[0]));
   }

   private String determineTargetDir(File var1) {
      File var2 = new File(var1, "WEB-INF" + File.separatorChar + "classes");
      return var2.getAbsolutePath();
   }

   private File determineDocroot(VirtualJarFile var1) throws IOException {
      if (var1.isDirectory()) {
         return var1.getDirectory();
      } else {
         this.isExtracted = true;
         this.warName = var1.getName();
         if (this.warName.lastIndexOf(File.separator) != -1) {
            this.warName = this.warName.substring(this.warName.lastIndexOf(File.separator) + 1);
         }

         File var2 = getJspcTempDir();
         JarFileUtils.extract(var1, var2);
         return var2;
      }
   }

   public static File getJspcTempDir() {
      File var0 = new File(tmpDirRoot, JSPC_TEMP_DIR);
      var0.mkdirs();
      return var0;
   }

   private void ensureClean() {
      File var1 = new File(tmpDirRoot, JSPC_TEMP_DIR);
      FileUtils.remove(var1);
   }

   public static String determineWebAppClasspath(VirtualJarFile var0, Map var1) {
      StringBuffer var2 = new StringBuffer();
      File[] var3 = var0.getRootFiles();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         buildWebAppClasspath(var3[var4], var2, var1);
      }

      return var2.length() > 0 ? var2.toString() : null;
   }

   private static void addFileToClasspath(StringBuffer var0, File var1) {
      if (var0.length() > 0) {
         var0.append(File.pathSeparator);
      }

      var0.append(var1.getAbsolutePath());
   }

   private static void buildWebAppClasspath(File var0, StringBuffer var1, Map var2) {
      File var3 = new File(var0, "WEB-INF");
      if (var3.exists() && var3.isDirectory()) {
         File var4 = new File(var3, "classes");
         if (var4.exists() && var4.isDirectory()) {
            addFileToClasspath(var1, var4);
         }

         File var5 = new File(var3, "lib");
         if (var5.exists() && var5.isDirectory()) {
            File[] var6 = var5.listFiles(new JarFileFilter());
            if (var6 != null) {
               for(int var7 = 0; var7 < var6.length; ++var7) {
                  addFileToClasspath(var1, var6[var7]);
                  var2.put(var6[var7].getName(), var6[var7].getAbsolutePath());
               }
            }
         }

      }
   }

   public static GenericClassLoader getJspClassLoader(ClassLoader var0, ClassFinder var1, String var2) {
      return new JspClassLoader(var1, var0, var2);
   }

   public static Map makeDescriptorMap(WeblogicWebAppBean var0, CharsetMap var1) {
      if (var0 == null) {
         return null;
      } else {
         Map var2 = makeDefaultDescriptorMap();
         JspDescriptorBean var3 = (JspDescriptorBean)DescriptorUtils.getFirstChildOrDefaultBean(var0, var0.getJspDescriptors(), "JspDescriptor");
         if (var3 != null) {
            setArg(var2, "keepgenerated", "" + var3.isKeepgenerated());
            setArg(var2, "backwardCompatible", "" + var3.isBackwardCompatible());
            setArg(var2, "rtexprvalueJspParamName", "" + var3.isRtexprvalueJspParamName());
            setArg(var2, "superclass", var3.getSuperClass());
            setArg(var2, "packagePrefix", var3.getPackagePrefix());
            setArg(var2, "pageCheckSeconds", "" + var3.getPageCheckSeconds());
            setArg(var2, "encoding", var3.getEncoding());
            setArg(var2, "printNulls", "" + var3.isPrintNulls());
            setArg(var2, "debug", "" + var3.isDebug());
            setArg(var2, "compressHtmlTemplate", "" + var3.isCompressHtmlTemplate());
            setArg(var2, "optimizeJavaExpression", "" + var3.isOptimizeJavaExpression());
            setArg(var2, "strictStaleCheck", "" + var3.isStrictStaleCheck());
         }

         if (var1 != null) {
            CharsetParamsBean var4 = (CharsetParamsBean)DescriptorUtils.getFirstChildOrDefaultBean(var0, var0.getCharsetParams(), "CharsetParams");
            if (var4 != null) {
               CharsetMappingBean[] var5 = var4.getCharsetMappings();

               for(int var6 = 0; var6 < var5.length; ++var6) {
                  var1.addMapping(var5[var6]);
               }
            }
         }

         return var2;
      }
   }

   public static Map makeDefaultDescriptorMap() {
      HashMap var0 = new HashMap();
      setArg(var0, "defaultFileName", "index.jsp");
      setArg(var0, "verbose", "true");
      setArg(var0, "keepgenerated", "false");
      setArg(var0, "precompileContinue", "false");
      setArg(var0, "pageCheckSeconds", "1");
      setArg(var0, "packagePrefix", "jsp_servlet");
      setArg(var0, "superclass", "weblogic.servlet.jsp.JspBase");
      setArg(var0, "exactMapping", "true");
      setArg(var0, "backwardCompatible", "false");
      setArg(var0, "printNulls", "true");
      setArg(var0, "compressHtmlTemplate", "false");
      setArg(var0, "optimizeJavaExpression", "false");
      setArg(var0, "strictStaleCheck", "true");
      return var0;
   }

   static void setArg(Map var0, String var1, String var2) {
      if (var2 != null) {
         var0.put(var1, var2);
      }

   }

   static void say(String var0) {
      System.out.println("[jspc] " + var0);
   }

   static void p(String var0) {
      System.out.println("[JspcInvoker]" + var0);
   }

   static void printAll(Getopt2 var0) {
      p("-d :" + var0.getOption("d"));
      p("-lineNumbers :" + var0.getOption("lineNumbers"));
      p("-k :" + var0.getOption("k"));
      p("-verbose :" + var0.getOption("verbose"));
      p("-keepgenerated :" + var0.getOption("keepgenerated"));
      p("-compiler :" + var0.getOption("compiler"));
      p("-forceGeneration :" + var0.getOption("forceGeneration"));
   }

   public static boolean canFindJavelinClasses() {
      try {
         URL var0 = (new Object()).getClass().getResource("/weblogic/servlet/jsp/.build.txt");
         if (var0 == null) {
            return true;
         } else {
            Class.forName("weblogic.jsp.wlw.filesystem.IFileFilter");
            return true;
         }
      } catch (Exception var1) {
         return false;
      }
   }

   private static void processLibraries(WeblogicWebAppBean var0, War var1, File var2, LibraryManager var3, String var4) throws ToolFailureException, IOException {
      WebAppLibraryUtils.initWebAppLibraryManager(var3, var0, var4);
      WebAppLibraryUtils.extractWebAppLibraries(var3, var1, var2);
   }

   public interface IJspc {
      void setWebBean(WebAppBean var1);

      void setWlWebBean(WeblogicWebAppBean var1);

      void runJspc(GenericClassLoader var1, ClassFinder var2, VirtualJarFile var3) throws Exception;
   }

   private static class JarFileFilter implements FileFilter {
      private JarFileFilter() {
      }

      public boolean accept(File var1) {
         return !var1.isDirectory() && (var1.getName().endsWith(".jar") || var1.getName().endsWith(".JAR"));
      }

      // $FF: synthetic method
      JarFileFilter(Object var1) {
         this();
      }
   }
}
