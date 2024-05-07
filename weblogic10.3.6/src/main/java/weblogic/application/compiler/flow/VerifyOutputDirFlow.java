package weblogic.application.compiler.flow;

import java.io.File;
import weblogic.application.compiler.CompilerCtx;
import weblogic.utils.compiler.ToolFailureException;

public final class VerifyOutputDirFlow extends CompilerFlow {
   public VerifyOutputDirFlow(CompilerCtx var1) {
      super(var1);
   }

   public void compile() throws ToolFailureException {
      if (this.ctx.isSplitDir()) {
         File var1 = this.ctx.getSourceFile();
         File var2 = this.ctx.getOutputDir();
         if (var1.equals(var2)) {
            throw new ToolFailureException("Cannot merge into splitdir please set -output to a different dir/ear");
         }
      }
   }

   public void cleanup() {
   }
}
