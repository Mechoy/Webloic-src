package weblogic.application.compiler;

import java.io.File;
import weblogic.application.utils.EarUtils;

public final class EARCompilerFactory implements CompilerFactory, MergerFactory {
   public Compiler createCompiler(CompilerCtx var1, File var2) {
      return EarUtils.isEar(var2) ? new EARCompiler(var1) : null;
   }

   public Merger createMerger(CompilerCtx var1, File var2) {
      if (EarUtils.isEar(var2)) {
         if (var1.isReadOnlyInvocation()) {
            if (var1.isMergeDisabled()) {
               return (Merger)(var1.isBasicView() ? new EarWithoutCMReader(var1) : new EarReader(var1));
            } else {
               return new ReadOnlyEarMerger(var1);
            }
         } else {
            return new EARMerger(var1);
         }
      } else {
         return null;
      }
   }
}
