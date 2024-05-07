package weblogic.servlet.jsp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import weblogic.version;
import weblogic.descriptor.DescriptorException;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.servlet.JSPServlet;
import weblogic.servlet.internal.CharsetMap;
import weblogic.servlet.internal.JSPManager;
import weblogic.servlet.internal.WebAppDescriptor;
import weblogic.utils.BadOptionException;
import weblogic.utils.FileUtils;
import weblogic.utils.Getopt2;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.Source;
import weblogic.utils.compiler.CompilerInvoker;
import weblogic.utils.compiler.Tool;
import weblogic.utils.compiler.ToolFailureException;
import weblogic.utils.enumerations.ResourceEnumerator;
import weblogic.utils.jars.JarFileUtils;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public final class jspc20 extends Tool implements StaleChecker, JspcInvoker.IJspc {
   private static final String WEBLOGIC_EXTENSION_DIRS = "weblogic.ext.dirs";
   private static final boolean debug = false;
   private boolean verbose;
   private static final int DEFAULT_MAX_FILES = 2000;
   private static String DEFAULT_TARGET_DIR;
   private ClassFinder resourceFinder;
   private ClassFinder classFinder;
   private GenericClassLoader classLoader;
   private File outputDir;
   private WebAppBean webBean;
   private WeblogicWebAppBean wlWebBean;
   private String[] args;
   private boolean isStrictStaleCheck;
   private static final boolean useUniqueJSPCL;

   public jspc20(String[] var1) {
      super(var1);
      this.opts.setUsageArgs("<jsp files>...");
      this.opts.addOption("webapp", "dir", "Directory to be considered as the document root for resolving relative files.");
      this.opts.addAlias("docroot", "webapp");
      this.opts.addFlag("verboseJspc", "whether JSP Compiler runs in verbose mode (default is false)");
      this.opts.addAdvancedOption("compileFlags", "flags", "Use this to specify non-standard options to the java compiler (those other than -g -nowarn, etc).  E.g, -compileFlags \"+E -otherOpt +P\"");
      this.opts.addAdvancedFlag("verboseJavac", "whether to invoke java compiler with -verbose (default false, use -verbose also)");
      this.opts.addAdvancedFlag("linenumbers", "add jsp line numbers to generated class files to aid debugging");
      this.opts.addAlias("debug", "linenumbers");
      this.opts.addAdvancedFlag("k", "continue compiling files, even when some fail");
      this.opts.addAdvancedFlag("noTryBlocks", "disable use of try/finally blocks for BodyTag extensions. Improves code for deeply-nested tags.");
      this.opts.addAdvancedFlag("noImplicitTlds", "disable search and registration of implicit Tlds. ");
      this.opts.addAdvancedFlag("noPrintNulls", "show \"null\" in jsp expressions as \"\" ");
      this.opts.addAlias("docRoot", "webapp");
      this.opts.addAdvancedFlag("backwardcompatible", "Backward compatibility option.");
      this.opts.addAdvancedOption("charsetMap", "charsetMapString", "specify mapping of IANA or unofficial charset names used in JSP contentType directives to java charset names.  E.g., '-charsetMap x-sjis=Shift_JIS,x-big5=Big5'  The most common mappings are built-in to jspc.  Use this option only if a desired charset mapping isn't recognized.");
      this.opts.addAdvancedOption("maxfiles", "int", "Maximum number of generated java files to be compiled at one time.");
      this.opts.addAdvancedFlag("skipJavac", "Skip compilation of generated servlet code.");
      this.opts.addAdvancedFlag("forceGeneration", "Force generation of JSP classes. Without this flag, the classes may not be generated if it is determined to be unnecessary.");
      this.opts.addAdvancedFlag("compressHtmlTemplate", "Remove additional white spaces in html template. Without this flag, html template will be output as is.");
      this.opts.addAdvancedFlag("optimizeJavaExpression", "Optimize string concatenation in Java expression. Without this flag, Java expression will be output as is.");
      this.opts.addAdvancedFlag("useByteBuffer", "Generate source codes of jsp files to use NIO ByteBuffer for static contents.");
      this.opts.addFlag("moreVerbose", "yet some more verbose");
      this.opts.markPrivate("moreVerbose");
      this.opts.addFlag("compileAll", "compile all .jsp files below the directory specified by -webapp");
      this.opts.markPrivate("compileAll");
      this.opts.addFlag("depend", "only compile files that are out of date (source .jsp file(s) newer than generated .class file)");
      this.opts.markPrivate("depend");
      new CompilerInvoker(this.opts);
      this.opts.addAdvancedOption("encoding", "options", "Valid args are \"default\" to use the default character encoding of JDK, or named character encoding, like \"8859_1\". If the -encoding flag is not present,  an array of bytes is used.");
      this.opts.addAdvancedOption("compilerSupportsEncoding", "options", "Set to \"true\" if the java compiler supports the -encoding flag, or \"false\" if it does not. ");
      this.opts.addAdvancedOption("package", "packageName", "The package into which the .jsp files should be placed");
      this.opts.addAdvancedOption("superclass", "superclass", "The class name of the superclass which this servlet should extend.");
      this.opts.markPrivate("nowrite");
      this.opts.markPrivate("nowarn");
      this.opts.markPrivate("verbose");
      this.opts.markPrivate("deprecation");
      this.opts.markPrivate("commentary");
      this.opts.markPrivate("O");
      this.opts.markPrivate("J");
      this.opts.markPrivate("normi");
      this.opts.markPrivate("g");
      this.opts.markPrivate("compiler");
      this.opts.markPrivate("compilerclass");
      this.opts.markPrivate("compilerSupportsEncoding");
      this.opts.markPrivate("verboseJavac");
      this.opts.markPrivate("compileFlags");
      this.opts.markPrivate("skipJavac");
      this.opts.markPrivate("source");
      this.opts.markPrivate("linenumbers");
      this.opts.markPrivate("noImplicitTlds");
      this.opts.markPrivate("noTryBlocks");
      this.opts.addOption("jsps", "jsps", "Comma-separated list of jsp files, specifies jsps that need to be compiled. All jsps of the app will be compiled if the option is not passed into.");
      this.opts.markPrivate("jsps");
      this.opts.addFlag("compileAllTagFiles", "Compile all JSP tag files");
      this.opts.markPrivate("compileAllTagFiles");
      this.opts.markPrivate("useByteBuffer");
      this.args = var1;
   }

   public void prepare() {
      this.setRequireExtraArgs(false);
   }

   public void runJspc(GenericClassLoader var1, ClassFinder var2, VirtualJarFile var3) throws Exception {
      this.prepare();
      this.opts.grok(this.args);
      this.resourceFinder = var2;
      this.init(var1, var3);

      assert var3.isDirectory();

      try {
         boolean var4 = true;
         String[] var5 = new String[0];
         if (this.opts.hasOption("jsps") && !"*".equals(this.opts.getOption("jsps")) && !"\"*\"".equals(this.opts.getOption("jsps")) && !"'*'".equals(this.opts.getOption("jsps"))) {
            var5 = this.opts.getOption("jsps").split(",");
            var4 = false;
         }

         this.runBodyInternal(var3, var4, var5);
      } finally {
         try {
            if (var3 != null) {
               var3.close();
            }

            if (var2 != null) {
               var2.close();
            }

            if (this.classFinder != null) {
               this.classFinder.close();
            }
         } catch (IOException var11) {
         }

      }

   }

   public void runBody() throws Exception {
      this.validateToolInput(this.opts);
      long var1 = 0L;
      boolean var3 = this.opts.getBooleanOption("moreVerbose");
      if (var3) {
         var1 = System.currentTimeMillis();
      }

      String var4 = this.opts.getOption("webapp", ".").replace('/', File.separatorChar);
      File var5 = new File(var4);
      VirtualJarFile var6 = VirtualJarFactory.createVirtualJar(var5);
      boolean var7 = !var6.isDirectory();
      boolean var8 = var7 && this.opts.getOption("d") == null;
      var6 = this.getVirtualJarFile(var6);
      this.init((GenericClassLoader)null, var6);
      this.initDescriptors(var6);

      try {
         this.runBodyInternal(var6, false, this.opts.args());
      } finally {
         try {
            if (var6 != null) {
               var6.close();
            }

            if (this.resourceFinder != null) {
               this.resourceFinder.close();
            }

            if (this.classFinder != null) {
               this.classFinder.close();
            }
         } catch (IOException var15) {
         }

         if (var3) {
            say("Total time elapsed : " + (System.currentTimeMillis() - var1));
         }

      }

      if (var7) {
         String var9 = this.opts.getOption("webapp");
         File var10 = new File(var9);
         if (var8) {
            say(" Creating jar file " + var4 + " w/ compiled jsps");
            JarFileUtils.createJarFileFromDirectory(var4, var10);
         }

         FileUtils.remove(var10);
      }

   }

   public void setWebBean(WebAppBean var1) {
      this.webBean = var1;
   }

   public void setWlWebBean(WeblogicWebAppBean var1) {
      this.wlWebBean = var1;
   }

   private void runBodyInternal(VirtualJarFile var1, boolean var2, String[] var3) throws Exception {
      JspConfig var4 = this.mergeOptions(this.opts);
      Object var5 = null;
      if (this.webBean != null) {
         var5 = JSPManager.getJspConfigPatterns(this.webBean.getJspConfigs());
      }

      this.verbose = this.opts.getBooleanOption("verboseJspc", false);
      this.isStrictStaleCheck = var4.isStrictStaleCheck();
      boolean var6 = this.opts.getBooleanOption("keepgenerated", false);
      boolean var7 = this.opts.getBooleanOption("k", false);
      boolean var8 = var2;
      if (!var2) {
         var8 = this.opts.getOption("webapp") != null && var3.length == 0 || this.opts.getBooleanOption("compileAll");
      }

      boolean var9 = this.opts.getBooleanOption("compileAllTagFiles", false);
      File[] var10 = var1.getRootFiles();

      assert var10 != null;

      ArrayList var11 = null;
      String var16;
      if (var8) {
         if (this.verbose) {
            String var12 = this.opts.getBooleanOption("compileAll") ? "-compileAll specified, " : " -webapp specified, ";
            say(var12 + "searching " + this.opts.getOption("webapp", ".") + " for JSPs");
         }

         ArrayList var32 = new ArrayList();
         if (var5 == null) {
            var5 = new HashSet();
         }

         if (var9) {
            ((Set)var5).add("/WEB-INF/tags/*.tag");
            ((Set)var5).add("/WEB-INF/tags/*.tagx");
         }

         ((Set)var5).add("*.jsp");
         ((Set)var5).add("*.jspx");
         String[] var13 = (String[])((String[])((Set)var5).toArray(new String[0]));

         for(int var14 = 0; var14 < var10.length; ++var14) {
            if (!var10[var14].exists()) {
               if (this.verbose) {
                  say("Ignoring non-existent web-root " + var10[var14].getAbsolutePath());
               }
            } else {
               ResourceEnumerator var15 = ResourceEnumerator.makeInstance(var10[var14], new String[0], var13);
               var16 = null;

               while((var16 = var15.getNextURI()) != null) {
                  var32.add(var16);
               }

               var15.close();
            }
         }

         var11 = var32;
      } else if (var3 != null && var3.length > 0) {
         var11 = new ArrayList(var3.length);

         for(int var33 = 0; var33 < var3.length; ++var33) {
            File var35 = new File(var3[var33].replace('/', File.separatorChar));
            var11.add(determineURI(var10[0], var35, var3[var33]));
         }
      }

      if (var11 != null && var11.size() != 0) {
         boolean var34 = this.opts.getBooleanOption("forceGeneration", false);
         boolean var36 = this.opts.getBooleanOption("depend", false);
         ClassFinder var37 = this.classLoader.getClassFinder();
         GenericClassLoader var38 = this.classLoader;
         if (!var34 || var36) {
            var16 = this.opts.getOption("package", "jsp_servlet");
            Iterator var17 = var11.iterator();

            while(var17.hasNext()) {
               String var18 = (String)var17.next();
               if (useUniqueJSPCL) {
                  String var19 = JSPServlet.uri2classname(var16, var18);
                  var38 = JspcInvoker.getJspClassLoader(this.classLoader, var37, var19);
               }

               if (!this.classIsStale(var16, var18, var38)) {
                  var17.remove();
                  if (this.verbose) {
                     say("skipping " + var18 + ", it is up to date.");
                  }
               }
            }
         }

         JspCompilerContext var39 = new JspCompilerContext("[jspc]");
         var39.setVerbose(this.verbose);
         var39.setClassLoader(var38);
         var39.setResourceFinder(this.resourceFinder);
         var39.setJspConfig(var4);
         String var40 = this.resourceFinder.getClassPath();
         String[] var41 = var40.split(File.pathSeparator);
         int var20;
         if (var41 != null) {
            ArrayList var42 = new ArrayList(var41.length);

            for(var20 = 0; var20 < var41.length; ++var20) {
               File var21 = new File(var41[var20]);
               if (var21.exists()) {
                  var42.add(var41[var20]);
               }
            }

            var39.setSourcePaths((String[])((String[])var42.toArray(new String[0])));
         }

         var39.setWebAppBean(this.webBean);
         var39.setWlWebAppBean(this.wlWebBean);
         int var43 = this.opts.getIntegerOption("maxfiles", 2000);
         var20 = var11.size();
         if (var20 < var43) {
            var43 = var20;
         }

         boolean var44 = false;
         int var22 = 0;

         for(int var23 = var43; var22 < var20; var22 += var23) {
            int var24 = Math.min(var23, var20 - var22);
            int var26 = var22 + var24;
            List var27 = var11.subList(var22, var26);
            boolean var28 = false;

            try {
               var28 = JspCLLManager.compileJsps(var27, var39);
            } catch (CompilationException var30) {
               if (!var7) {
                  throw new ToolFailureException("jspc failed with errors :" + var30);
               }
            } catch (Exception var31) {
               var31.printStackTrace();
               throw new ToolFailureException("Unexpected exception while compiling jsps :" + var31);
            }

            var44 |= !var28;
         }

         if (!var7 && var44) {
            throw new ToolFailureException("[jspc] compiler failed with errors");
         }
      } else {
         say("No jsp files found, nothing to do");
      }
   }

   private void init(GenericClassLoader var1, VirtualJarFile var2) {
      if (this.resourceFinder == null) {
         this.resourceFinder = buildResourceFinder(var2);
      }

      this.classLoader = var1;
      this.outputDir = new File(this.opts.getOption("d", "."));
      this.outputDir.mkdirs();
      if (this.classLoader == null) {
         this.classFinder = buildClassFinder(this.opts, this.outputDir, var2);
         this.classLoader = new GenericClassLoader(this.classFinder);
      } else {
         String var3 = buildExtClassPath();
         StringBuilder var4 = new StringBuilder();
         if (var3 != null) {
            var4.append(var3);
            var4.append(File.pathSeparator);
         }

         var4.append(this.outputDir.getAbsolutePath());
         this.classLoader.addClassFinder(new ClasspathClassFinder2(var4.toString()));
      }

   }

   private void initDescriptors(VirtualJarFile var1) throws ToolFailureException {
      boolean var2 = var1.getEntry("WEB-INF/web.xml") != null;
      if (!var2) {
         say("warning: expected file /WEB-INF/web.xml  not found, tag libraries cannot be resolved.");
      }

      boolean var3 = var1.getEntry("WEB-INF/weblogic.xml") != null;
      WebAppDescriptor var4 = new WebAppDescriptor(var1);
      if (var2 && this.webBean == null) {
         try {
            this.webBean = var4.getWebAppBean();
         } catch (XMLStreamException var9) {
         } catch (DescriptorException var10) {
            String var6 = null;
            if (var10.getCause() != null) {
               var6 = var10.getCause().getMessage();
            }

            say("Warning: Error occured while parsing WEB-INF/web.xml, tag libraries will not be resolved " + (var6 == null ? "" : var6));
         } catch (IOException var11) {
         }
      }

      if (var3 && this.wlWebBean == null) {
         try {
            this.wlWebBean = var4.getWeblogicWebAppBean();
         } catch (XMLStreamException var7) {
            throw new ToolFailureException("Error occured while parsing /WEB-INF/weblogic.xml : " + var7);
         } catch (IOException var8) {
         }
      }

   }

   public boolean isResourceStale(String var1, long var2, String var4, String var5) {
      boolean var6 = Boolean.getBoolean("weblogic.jspc.skipVersionCheck");
      if (!var6 && !version.getReleaseBuildVersion().equals(var4)) {
         if (this.verbose) {
            say("Resource '" + var1 + "' is being considered new, its server version '" + var4 + "' does not match the current server version '" + version.getReleaseBuildVersion() + "'");
         }

         return true;
      } else {
         if (var6 && this.verbose) {
            say("found System property 'weblogic.jspc.skipVersionCheck=true', skipping server version check ");
         }

         Source var7 = this.resourceFinder.getSource(var1);
         if (var7 == null) {
            return true;
         } else {
            long var8 = var7.lastModified();
            if (!this.isStrictStaleCheck) {
               say("using none strick stale check");
               return var2 != var8 + 2000L;
            } else {
               return var2 < var8;
            }
         }
      }
   }

   private VirtualJarFile getVirtualJarFile(VirtualJarFile var1) throws IOException {
      if (var1.isDirectory()) {
         return var1;
      } else {
         File var2 = JspcInvoker.getJspcTempDir();
         JarFileUtils.extract(var1, var2);

         try {
            this.opts.setOption("webapp", var2.getAbsolutePath());
         } catch (BadOptionException var6) {
            assert false;
         }

         File var3 = new File(var2, DEFAULT_TARGET_DIR);
         if (this.opts.getOption("d") == null) {
            try {
               this.opts.setOption("d", var3.getAbsolutePath());
            } catch (BadOptionException var5) {
               assert false;
            }
         }

         var1.close();
         return VirtualJarFactory.createVirtualJar(var2);
      }
   }

   private JspConfig mergeOptions(Getopt2 var1) {
      Map var2 = null;
      if (this.wlWebBean == null) {
         var2 = JspcInvoker.makeDefaultDescriptorMap();
      } else {
         var2 = JspcInvoker.makeDescriptorMap(this.wlWebBean, (CharsetMap)null);
      }

      var2.put("workingDir", this.outputDir.getAbsolutePath());
      var2.put("compiler", var1.getOption("compiler"));
      Iterator var3 = var2.keySet().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         String var5 = this.getToolOptionName(var4);
         String var6 = null;
         if (var5 != null && (var6 = var1.getOption(var5)) != null) {
            if ("printNulls".equals(var4)) {
               var6 = "false".equals(var6) ? "true" : "false";
            } else {
               say("Overriding " + (this.wlWebBean == null ? " default " : "") + "descriptor option '" + var4 + "'" + (var5.equals(var4) ? "" : " (alias '" + var5 + "')") + " with value specified on command-line '" + var6 + "' ");
            }

            var2.put(var4, var6);
         }
      }

      var2.put("useByteBuffer", Boolean.toString(var1.getBooleanOption("useByteBuffer")));
      return new JspConfig(var2);
   }

   private boolean classIsStale(String var1, String var2, ClassLoader var3) {
      String var4 = JSPServlet.uri2classname(var1, var2);

      try {
         Class var5 = var3.loadClass(var4);
         return JspStub.isJSPClassStale(var5, this);
      } catch (ThreadDeath var6) {
         throw var6;
      } catch (NoClassDefFoundError var7) {
         if (this.verbose) {
            say("WARN: precompiled servlet class " + var4 + "'s dependent class " + var7.getMessage().replace('/', '.') + " cannot be found!, so jsp check stale fails and " + var2 + " will be recompiled!");
         }
      } catch (Throwable var8) {
      }

      return true;
   }

   private static String buildExtClassPath() {
      return System.getProperty("weblogic.ext.dirs");
   }

   private static ClassFinder buildClassFinder(Getopt2 var0, File var1, VirtualJarFile var2) {
      String var3 = var0.getOption("classpath", (String)null);
      String var4 = System.getProperty("java.class.path");
      if (var3 != null) {
         var4 = var4 + File.pathSeparator + var3;
      }

      String var5 = buildExtClassPath();
      if (var5 != null) {
         var4 = var5 + File.pathSeparator + var4;
      }

      String var6 = JspcInvoker.determineWebAppClasspath(var2, new HashMap());
      if (var6 != null) {
         var4 = var6 + File.pathSeparator + var4;
      }

      if (var1 != null) {
         var4 = var1.getAbsolutePath() + File.pathSeparator + var4;
      }

      return new ClasspathClassFinder2(var4);
   }

   private static String determineURI(File var0, File var1, String var2) throws FileNotFoundException {
      String var3 = var0.getAbsolutePath();
      int var4 = var3.length();
      if (var3.endsWith(".")) {
         --var4;
         var3 = var3.substring(0, var4);
      }

      if (var3.endsWith(File.separator)) {
         --var4;
         var3 = var3.substring(0, var4);
      }

      String var5 = var1.getAbsolutePath();
      if (var5.startsWith(var3.substring(1), 1) && var5.substring(0, 1).equalsIgnoreCase(var3.substring(0, 1))) {
         String var7 = var5.substring(var4).replace(File.separatorChar, '/');
         return var7;
      } else {
         File var6 = new File(var3, var2.replace('/', File.separatorChar));
         if (!var6.exists()) {
            throw new FileNotFoundException("ERROR: Source file '" + var2 + "' can not be found in the docroot '" + var3 + "'. Put the source in the docroot or specify the correct docroot" + " with jspc option \"-webapp\".");
         } else {
            return var2.replace(File.separatorChar, '/');
         }
      }
   }

   private static ClassFinder buildResourceFinder(VirtualJarFile var0) {
      if (var0 == null) {
         return null;
      } else {
         String var1 = "";
         File[] var2 = var0.getRootFiles();
         if (var2 == null) {
            return null;
         } else {
            for(int var3 = 0; var3 < var2.length; ++var3) {
               var1 = var2[var3].getAbsolutePath() + File.pathSeparator + var1;
            }

            return new ClasspathClassFinder2(var1);
         }
      }
   }

   protected String getToolOptionName(String var1) {
      if (var1.equals("printNulls")) {
         return "noPrintNulls";
      } else if (var1.equals("precompileContinue")) {
         return "k";
      } else if (var1.equals("compileCommand")) {
         return "compiler";
      } else if (var1.equals("packagePrefix")) {
         return "package";
      } else if (var1.equals("debug")) {
         return "linenumbers";
      } else {
         return !var1.equals("encoding") && !var1.equals("compilerclass") && !var1.equals("compileFlags") && !var1.equals("keepgenerated") && !var1.equals("compilerSupportsEncoding") && !var1.equals("superclass") && !var1.equals("compressHtmlTemplate") && !var1.equals("optimizeJavaExpression") && !var1.equals("noTryBlocks") ? null : var1;
      }
   }

   private void validateToolInput(Getopt2 var1) throws ToolFailureException {
      boolean var2 = var1.getBooleanOption("compileAll");
      boolean var3 = var1.getOption("webapp") != null;
      String var4 = var1.getOption("package", "jsp_servlet");
      if (!var2 && !var3 && var1.args().length == 0) {
         if (var1.hasOptions()) {
            var1.usageError("weblogic.jspc");
            throw new ToolFailureException("Only options were given, additional arguments are required ");
         } else {
            var1.usageError("weblogic.jspc");
            throw new ToolFailureException("No arguments were given, there is nothing to do.");
         }
      } else if ("".equals(var4.trim())) {
         throw new IllegalArgumentException("Bad -package option value, please specify a valid value for this option");
      }
   }

   private static void say(String var0) {
      System.out.println("[jspc] " + var0);
   }

   private static void sayError(String var0) {
      System.err.println("Error: " + var0);
   }

   static void p(String var0) {
      System.out.println("[jspc20]" + var0);
   }

   public static void main(String[] var0) throws Exception {
      (new jspc20(var0)).run();
   }

   static {
      DEFAULT_TARGET_DIR = File.separator + "WEB-INF" + File.separator + "classes";
      useUniqueJSPCL = Boolean.getBoolean("weblogic.jspc.useUniqueJspClassLoader");
   }
}
