package weblogic.application.compiler.flow;

import java.io.File;
import weblogic.application.compiler.CompilerCtx;
import weblogic.j2ee.J2EEApplicationService;
import weblogic.kernel.Kernel;
import weblogic.utils.FileUtils;
import weblogic.utils.compiler.ToolFailureException;

public class SetupFlow extends CompilerFlow {
   private static final boolean KEEP_TEMP_FILES = Boolean.getBoolean("weblogic.application.compiler.KeepTempFilesOnExit");
   private String tempDirName;

   public SetupFlow(CompilerCtx var1, String var2) {
      super(var1);
      this.tempDirName = var2;
   }

   public void cleanup() throws ToolFailureException {
      if (!KEEP_TEMP_FILES) {
         if (this.ctx.getOutputDir() != null && this.ctx.getTempDir() != null && this.ctx.getOutputDir().getName().startsWith(this.ctx.getTempDir().getName() + "_")) {
            FileUtils.remove(this.ctx.getOutputDir());
            this.ctx.setOutputDir((File)null);
         }

         FileUtils.remove(this.ctx.getTempDir());
         this.ctx.setTempDir((File)null);
      }
   }

   public void compile() throws ToolFailureException {
      File var1 = null;
      if (Kernel.isServer()) {
         var1 = new File(J2EEApplicationService.getTempDir(), this.tempDirName);
      } else {
         var1 = new File(System.getProperty("java.io.tmpdir"), this.tempDirName);
      }

      if (var1.exists() && !var1.isDirectory()) {
         var1.delete();
      }

      var1.mkdirs();
      this.ctx.setTempDir(var1);
   }
}
