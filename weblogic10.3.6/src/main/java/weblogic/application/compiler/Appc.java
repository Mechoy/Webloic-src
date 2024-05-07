package weblogic.application.compiler;

import java.util.ArrayList;
import java.util.List;
import weblogic.application.compiler.flow.AppCompilerFlow;
import weblogic.application.compiler.flow.CheckUnusedLibrariesFlow;
import weblogic.application.compiler.flow.CompilerFlow;
import weblogic.application.compiler.flow.InitLibrariesFlow;
import weblogic.application.compiler.flow.InitPlanFlow;
import weblogic.application.compiler.flow.OptionalPackageReferencerFlow;
import weblogic.application.compiler.flow.PrepareInputFlow;
import weblogic.application.compiler.flow.SetupFlow;
import weblogic.application.utils.LibraryUtils;
import weblogic.j2ee.J2EELogger;
import weblogic.utils.BadOptionException;
import weblogic.utils.compiler.ArgfileParser;
import weblogic.utils.compiler.CompilerInvoker;
import weblogic.utils.compiler.Tool;
import weblogic.utils.compiler.ToolFailureException;

public class Appc extends Tool {
   protected Appc(String[] var1) {
      super(var1);
   }

   public void prepare() {
      this.setRequireExtraArgs(true);
      this.setUsageName("weblogic.appc");
      this.opts.setUsageArgs("<ear, jar, war or rar file or directory>");
      this.opts.addOption("output", "file", "Specifies an alternate output archive or directory.  If not set, output will be placed in the source archive or directory.");
      this.opts.addOption("plan", "file", "Specifies an optional deployment plan.");
      this.opts.addFlag("forceGeneration", "Force generation of EJB and JSP classes.  Without this flag the classes may not be regenerated if it is determined to be unnecessary.");
      this.opts.addFlag("quiet", "Turns off output except for errors");
      this.opts.addFlag("lineNumbers", "Add JSP line numbers to generated class files to aid in debugging.");
      this.opts.addAdvancedFlag("k", "continue compiling jsp files, even when some fail");
      this.opts.addOption("moduleUri", "uri", "Specify a module in an ear file for compilation.");
      this.opts.addOption("jsps", "jsps", "Comma-separated list of jsp files, specifies jsps that need to be compiled. All jsps of the app will be compiled if the option is not passed into.");
      this.opts.addAdvancedFlag("useByteBuffer", "Generate source codes of jsp files to use NIO ByteBuffer for static contents.");
      this.opts.addFlag("compileAllTagFiles", "Compile all JSP tag files");
      LibraryUtils.addLibraryUsage(this.opts);
      this.opts.addAdvancedFlag("basicClientJar", "Do not include deployment descriptors in client jars generated for EJBs.");
      this.opts.addFlag("disableHotCodeGen", "Generate ejb stub and skel as part of ejbc. Avoid HotCodeGen to have better performance.");
      this.opts.addFlag("enableHotCodeGen", "Do not generate ejb stub and skel as part of ejbc. Stubs and skels will be dynamically generated.");
      this.opts.addFlag("writeInferredDescriptors", "Write out the descriptors with inferred information including annotations.");
      this.opts.addOption("manifest", "file", "Include manifest information from specified manifest file.");
      this.opts.addOption("clientJarOutputDir", "dir", "Specifies a directory to put generated client jars.");
      this.opts.addAdvancedFlag("idl", "Generate idl for EJB remote interfaces");
      this.opts.addAdvancedFlag("idlOverwrite", "Always overwrite existing IDL files");
      this.opts.addAdvancedFlag("idlVerbose", "Display verbose information for IDL generation");
      this.opts.addAdvancedFlag("idlNoValueTypes", "Do not generate valuetypes and methods/attributes that contain them.");
      this.opts.addAdvancedFlag("idlNoAbstractInterfaces", "Do not generate abstract interfaces and methods/attributes that contain them.");
      this.opts.addAdvancedFlag("idlFactories", "Generate factory methods for valuetypes.");
      this.opts.addAdvancedFlag("idlVisibroker", "Generate IDL somewhat compatible with Visibroker 4.5 C++.");
      this.opts.addAdvancedFlag("idlOrbix", "Generate IDL somewhat compatible with Orbix 2000 2.0 C++.");
      this.opts.addAdvancedOption("idlDirectory", "dir", "Specify the directory where IDL files will be created (default : target directory or jar)");
      this.opts.addAdvancedOption("idlMethodSignatures", "signature", "Specify the method signatures used to trigger idl code generation.");
      this.opts.addAdvancedFlag("iiop", "Generate CORBA stubs for EJBs");
      this.opts.addAdvancedFlag("ignorePlanValidation", "Ignore the plan file if it doesn't exist");
      this.opts.addAdvancedOption("iiopDirectory", "dir", "Specify the directory where IIOP stub files will be written (default : target directory or jar)");
      this.opts.addAdvancedOption("altappdd", "file", "Location of the alternate application deployment descriptor.");
      this.opts.addAdvancedOption("altwlsappdd", "file", "Location of the alternate WebLogic application deployment descriptor.");
      this.opts.addAdvancedOption("maxfiles", "int", "Maximum number of generated java files to be compiled at one time.");
      new CompilerInvoker(this.opts);
      this.opts.markAdvanced("verboseJavac");
      this.opts.markAdvanced("normi");
      this.opts.markAdvanced("nowarn");
      this.opts.markAdvanced("deprecation");
      this.opts.markAdvanced("O");
      this.opts.markAdvanced("J");
      this.opts.markAdvanced("g");
      this.opts.markAdvanced("compilerclass");
      this.opts.markAdvanced("compiler");
      this.opts.markPrivate("nowrite");
      this.opts.markPrivate("commentary");
      this.opts.markPrivate("d");
      this.opts.markPrivate("disableHotCodeGen");
      this.opts.markPrivate("enableHotCodeGen");
      this.opts.markPrivate("moduleUri");
      this.opts.markPrivate("jsps");
      this.opts.markPrivate("compileAllTagFiles");
      this.opts.markPrivate("useByteBuffer");
   }

   public void runBody() throws ToolFailureException {
      try {
         if (this.opts.hasOption("enableHotCodeGen")) {
            this.opts.setFlag("disableHotCodeGen", false);
         } else {
            this.opts.setFlag("disableHotCodeGen", true);
         }
      } catch (BadOptionException var5) {
         throw new AssertionError(var5);
      }

      this.opts.removeOption("enableHotCodeGen");
      CompilerCtx var1 = new CompilerCtx();
      var1.setOpts(this.opts);
      String var2 = "appcgen_" + System.currentTimeMillis();
      CompilerFlow[] var3 = new CompilerFlow[]{new SetupFlow(var1, var2), new PrepareInputFlow(var1), new OptionalPackageReferencerFlow(var1), new InitPlanFlow(var1), new InitLibrariesFlow(var1, false), new AppCompilerFlow(var1), new CheckUnusedLibrariesFlow(var1)};

      try {
         (new FlowDriver()).run(var3);
      } catch (ToolFailureException var6) {
         if (!var1.isVerbose()) {
            J2EELogger.logAppcFailedWithError();
         }

         if (var1.isVerbose() && var6.getCause() != null) {
            var6.getCause().printStackTrace();
         }

         throw var6;
      }
   }

   private List<String> conjoinJspsOption(List<String> var1) {
      ArrayList var2 = new ArrayList();
      boolean var3 = false;
      ArrayList var4 = new ArrayList();

      for(int var5 = 0; var5 < var1.size(); ++var5) {
         String var6 = (String)var1.get(var5);
         if (var5 == var1.size() - 1) {
            var3 = false;
         }

         if (var6.startsWith("-")) {
            if ("-jsps".equals(var6)) {
               var3 = true;
            } else {
               var3 = false;
            }
         }

         if (var3 && !var6.startsWith("-")) {
            var4.add(var6);
         } else {
            var2.add(var6);
         }
      }

      StringBuilder var7 = new StringBuilder();

      for(int var8 = 0; var8 < var4.size(); ++var8) {
         var7.append((String)var4.get(var8));
         if (var8 < var4.size() - 1) {
            var7.append(",");
         }
      }

      if (var2.indexOf("-jsps") >= 0) {
         var2.add(var2.indexOf("-jsps") + 1, var7.toString());
      }

      return var2;
   }

   protected String[] transformArgs(String[] var1) throws ToolFailureException {
      ArrayList var2 = new ArrayList();
      String[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         if (var6.startsWith("@")) {
            var2.addAll((new ArgfileParser(var6.substring(1))).parse());
         } else {
            var2.add(var6);
         }
      }

      List var7 = this.conjoinJspsOption(var2);
      return (String[])var7.toArray(new String[var7.size()]);
   }

   public static void main(String[] var0) throws Exception {
      (new Appc(var0)).run();
   }
}
