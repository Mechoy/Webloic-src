package weblogic.application.compiler;

import java.io.File;
import weblogic.application.ApplicationAccess;
import weblogic.kernel.Kernel;

public final class LightWeightCompilerFactory implements CompilerFactory, MergerFactory {
   public Compiler createCompiler(CompilerCtx var1, File var2) {
      return null;
   }

   public Merger createMerger(CompilerCtx var1, File var2) {
      String var3 = var1.getLightWeightAppName();
      if (var3 == null) {
         return null;
      } else if (!Kernel.isServer()) {
         return null;
      } else {
         return null == ApplicationAccess.getApplicationAccess().getApplicationContext(var1.getLightWeightAppName()) ? null : new LightWeightMerger(var1);
      }
   }
}
