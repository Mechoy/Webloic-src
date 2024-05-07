package weblogic.application.compiler.flow;

import java.io.File;
import java.io.IOException;
import weblogic.application.compiler.AppcUtils;
import weblogic.application.compiler.CompilerCtx;
import weblogic.j2ee.J2EEApplicationService;
import weblogic.j2ee.J2EELogger;
import weblogic.kernel.Kernel;
import weblogic.logging.Loggable;
import weblogic.utils.Getopt2;
import weblogic.utils.application.WarDetector;
import weblogic.utils.compiler.ToolFailureException;

public final class PrepareInputFlow extends CompilerFlow {
   private File sourceFile;
   private boolean verbose;
   private Getopt2 opts;

   public PrepareInputFlow(CompilerCtx var1) {
      super(var1);
   }

   public void compile() throws ToolFailureException {
      this.opts = this.ctx.getOpts();
      this.prepareInput(this.ctx);
   }

   public void cleanup() {
      this.ctx.getApplicationContext().getAppClassLoader().close();
   }

   private void prepareInput(CompilerCtx var1) throws ToolFailureException {
      if (this.opts.args().length > 1) {
         this.opts.usageError("weblogic.appc");
         Loggable var7 = J2EELogger.logTooManyArgsForAppcLoggable();
         throw new ToolFailureException(var7.getMessage());
      } else {
         this.verbose = this.opts.hasOption("verbose");
         var1.setVerbose(this.verbose);
         if (this.opts.hasOption("readonly")) {
            var1.setReadOnlyInvocation();
         }

         String var2 = this.getSourceFileName(this.opts);

         try {
            this.sourceFile = (new File(var2)).getCanonicalFile();
         } catch (IOException var6) {
            throw new ToolFailureException("Error processing source " + var2, var6);
         }

         var1.setSourceFile(this.sourceFile);
         if (this.opts.hasOption("lightweight")) {
            var1.setLightWeightAppName(this.opts.getOption("lightweight"));
         }

         var1.createApplicationContext();
         if (!this.sourceFile.exists()) {
            Loggable var8 = J2EELogger.logAppcSourceArgDoesNotExistLoggable(var2);
            throw new ToolFailureException(var8.getMessage());
         } else if (this.opts.hasOption("jsps") && this.opts.getBooleanOption("compileAllTagFiles")) {
            throw new ToolFailureException("jsps & compileAllTagFiles flags cannot be used together with Appc");
         } else if (this.opts.hasOption("jsps") && this.opts.getOption("jsps").indexOf(42) >= 0 && !"*".equals(this.opts.getOption("jsps")) && !"\"*\"".equals(this.opts.getOption("jsps")) && !"'*'".equals(this.opts.getOption("jsps"))) {
            throw new ToolFailureException("Unsupported wildcard pattern was passed to -jsps");
         } else if (this.opts.hasOption("compiler") && !"javac".equals(this.opts.getOption("compiler")) && !"jdt".equals(this.opts.getOption("compiler")) && !"none".equals(this.opts.getOption("compiler"))) {
            throw new ToolFailureException("-compiler only supports javac, jdt and none");
         } else {
            this.prepareOutputFiles(this.opts, var1);
            String var3;
            if (this.opts.hasOption("classpath")) {
               var3 = this.opts.getOption("classpath");
               var1.setClasspathArg(var3);
            }

            if (this.opts.hasOption("plan")) {
               var3 = this.opts.getOption("plan");
               var1.setPlanName(var3);
               File var4 = new File(var3);
               var1.setPlanFile(var4);
               Loggable var5;
               if (!var4.exists() || !var4.isFile()) {
                  if (!this.opts.hasOption("ignorePlanValidation")) {
                     var5 = J2EELogger.logAppcPlanArgDoesNotExistLoggable(var3);
                     throw new ToolFailureException(var5.getMessage());
                  }

                  var1.setPlanName((String)null);
                  var1.setPlanFile((File)null);
                  System.out.println("Warning: The plan file " + var3 + " doesn't exist, ignore it");
               }

               if (!var3.endsWith(".xml") && var1.getPlanName() != null) {
                  var5 = J2EELogger.logAppcPlanArgWrongTypeLoggable();
                  throw new ToolFailureException(var5.getMessage());
               }
            }

            if (this.opts.hasOption("readonly") && this.opts.hasOption("writeInferredDescriptors")) {
               throw new ToolFailureException("readonly & writeInferredDescriptors flags cannot be used together with AppMerge");
            } else {
               if (this.opts.hasOption("ignoreMissingLibRefs")) {
                  var1.setVerifyLibraryReferences(false);
               }

               if (this.opts.hasOption("writeInferredDescriptors")) {
                  var1.setWriteInferredDescriptors();
               }

               if (this.opts.hasOption("manifest")) {
                  var1.setManifestFile(new File(this.opts.getOption("manifest")));
               }

            }
         }
      }
   }

   private String getSourceFileName(Getopt2 var1) {
      String[] var2 = var1.args();
      return var2[0];
   }

   private void prepareOutputFiles(Getopt2 var1, CompilerCtx var2) throws ToolFailureException {
      String var3 = this.sourceFile.getPath();
      boolean var4 = this.sourceFile.isFile();
      boolean var5 = var1.hasOption("output");
      boolean var6 = var1.hasOption("d");
      String var7;
      if (var5 || var6) {
         if (var5) {
            var3 = var1.getOption("output");
         } else {
            var3 = var1.getOption("d");
         }

         if ((new File(var3)).exists()) {
            J2EELogger.logOutputLocationExists(var3);
         }

         var7 = var3.toLowerCase();
         if (!var7.endsWith(".jar") && !WarDetector.instance.suffixed(var7) && !var7.endsWith(".rar") && !var7.endsWith(".ear")) {
            var4 = false;
         } else {
            var4 = true;
         }

         if (var2.getOpts().hasOption("nopackage")) {
            var4 = false;
         }

         if ((var1.hasOption("jsps") || var1.hasOption("moduleUri")) && !var4) {
            var2.setPartialOutputTarget(var3);
         }
      }

      var7 = null;
      File var9;
      if (var4) {
         File var8 = null;
         if (Kernel.isServer()) {
            var8 = J2EEApplicationService.getTempDir();
         } else {
            var8 = new File(System.getProperty("java.io.tmpdir"));
         }

         var9 = AppcUtils.makeOutputDir(var2.getTempDir().getName() + "_" + this.sourceFile.getName(), var8, true);
         if (this.verbose) {
            this.say("Created working directory: " + var9);
         }
      } else if (var2.getPartialOutputTarget() != null && this.sourceFile.isDirectory()) {
         var9 = this.sourceFile;
      } else if (var2.isReadOnlyInvocation()) {
         var9 = new File(var3);
      } else {
         var9 = AppcUtils.makeOutputDir(var3, (File)null, false);
      }

      var2.setOutputDir(var9);
      var2.setTargetArchive(var4 ? var3 : null);
   }
}
