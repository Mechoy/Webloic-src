package weblogic.application.compiler.flow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.jar.Manifest;
import weblogic.application.compiler.CompilerCtx;
import weblogic.j2ee.J2EELogger;
import weblogic.logging.Loggable;
import weblogic.utils.FileUtils;
import weblogic.utils.compiler.ToolFailureException;

public class ManifestFlow extends CompilerFlow {
   public ManifestFlow(CompilerCtx var1) {
      super(var1);
   }

   public void compile() throws ToolFailureException {
      File var1 = this.ctx.getManifestFile();
      if (var1 != null) {
         try {
            if (!var1.exists() || !var1.isFile()) {
               throw new FileNotFoundException();
            }

            Manifest var2 = new Manifest();
            var2.read(new FileInputStream(var1));
            File var5 = new File(new File(this.ctx.getOutputDir(), "META-INF"), "MANIFEST.MF");
            FileUtils.copy(var1, var5);
         } catch (Exception var4) {
            Loggable var3 = J2EELogger.logAppcErrorsEncounteredCompilingModuleLoggable(this.ctx.getSourceFile().getAbsolutePath(), var4.toString());
            throw new ToolFailureException(var3.getMessage(), var4);
         }
      }

   }

   public void cleanup() throws ToolFailureException {
   }
}
