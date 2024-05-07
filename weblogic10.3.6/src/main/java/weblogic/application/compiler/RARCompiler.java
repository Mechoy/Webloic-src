package weblogic.application.compiler;

import java.io.File;
import weblogic.application.utils.IOUtils;
import weblogic.connector.external.RAComplianceException;
import weblogic.j2ee.J2EELogger;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.validation.ModuleValidationInfo;
import weblogic.logging.Loggable;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.compiler.ToolFailureException;
import weblogic.utils.jars.VirtualJarFile;

final class RARCompiler implements Compiler {
   private final CompilerCtx ctx;
   private File outputDir = null;
   private File sourceFile = null;
   private File config = null;
   private DeploymentPlanBean plan = null;
   private VirtualJarFile vSource = null;

   RARCompiler(CompilerCtx var1) {
      this.ctx = var1;
   }

   private void setup() throws ToolFailureException {
      this.outputDir = this.ctx.getOutputDir();
      this.sourceFile = this.ctx.getSourceFile();
      this.config = this.ctx.getConfigDir();
      this.plan = this.ctx.getPlanBean();
      if (!this.sourceFile.equals(this.outputDir)) {
         AppcUtils.expandJarFileIntoDirectory(this.sourceFile, this.outputDir);
      }

      try {
         this.vSource = AppcUtils.getVirtualJarFile(this.ctx.getSourceFile());
      } catch (Exception var2) {
         IOUtils.forceClose(this.vSource);
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         if (var2 instanceof ToolFailureException) {
            throw (ToolFailureException)var2;
         }
      }

   }

   public void compile() throws ToolFailureException {
      this.setup();
      String var1 = this.outputDir.getPath();
      Object var2 = null;
      Object var3 = null;
      this.ctx.setTargetArchive((String)null);
      GenericClassLoader var4 = AppcUtils.getClassLoaderForApplication(new ClasspathClassFinder2(var1), this.ctx, this.ctx.getApplicationContext().getApplicationId());

      try {
         if (this.vSource.isDirectory()) {
            AppcUtils.compileRAR(var4, this.vSource, (File)var2, this.config, this.plan, (ModuleValidationInfo)var3, this.ctx);
         } else {
            AppcUtils.expandJarFileIntoDirectory(this.vSource, this.outputDir);
            VirtualJarFile var5 = AppcUtils.getVirtualJarFile(this.outputDir);
            AppcUtils.compileRAR(var4, var5, (File)var2, this.config, this.plan, (ModuleValidationInfo)var3, this.ctx);
         }
      } catch (RAComplianceException var11) {
         Loggable var6 = J2EELogger.logAppcErrorsEncounteredCompilingModuleLoggable(this.ctx.getSourceFile().getAbsolutePath(), var11.toString());
         throw new ToolFailureException(var6.getMessage(), var11);
      } finally {
         var4.close();
         IOUtils.forceClose(this.vSource);
      }

   }
}
