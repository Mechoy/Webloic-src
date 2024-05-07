package weblogic.application.compiler;

import java.io.File;
import weblogic.j2ee.J2EELogger;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.logging.Loggable;
import weblogic.utils.FileUtils;
import weblogic.utils.compiler.ToolFailureException;

final class JMSCompiler implements Compiler {
   private final CompilerCtx ctx;
   private BuildtimeApplicationContext libCtx = null;

   JMSCompiler(CompilerCtx var1) {
      this.ctx = var1;
      this.libCtx = (BuildtimeApplicationContext)var1.getApplicationContext();
   }

   public void compile() throws ToolFailureException {
      File var1 = this.ctx.getOutputDir();
      File var2 = this.ctx.getSourceFile();
      File var3 = var2;
      if (!var2.equals(var1)) {
         var3 = this.getModuleFile(var2.getName(), var1);
      }

      try {
         File var4 = this.ctx.getConfigDir();
         DeploymentPlanBean var7 = this.ctx.getPlanBean();
         FileUtils.copy(var2, var3);
         AppcUtils.compileJMS(var4, var7, var1, var3.getPath(), this.ctx.getOpts());
      } catch (Exception var6) {
         Loggable var5 = J2EELogger.logAppcErrorsEncounteredCompilingModuleLoggable(this.ctx.getSourceFile().getAbsolutePath(), var6.toString());
         throw new ToolFailureException(var5.getMessage(), var6);
      }
   }

   private File getModuleFile(String var1, File var2) {
      return this.libCtx.getURILink(var1) != null ? this.libCtx.getURILink(var1) : new File(var2, var1);
   }
}
