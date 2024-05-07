package weblogic.application.compiler.flow;

import weblogic.application.compiler.CompilerCtx;
import weblogic.application.compiler.EARModule;
import weblogic.utils.compiler.ToolFailureException;

public final class MergeModuleFlow extends CompilerFlow {
   public MergeModuleFlow(CompilerCtx var1) {
      super(var1);
   }

   public void compile() throws ToolFailureException {
      EARModule[] var1 = this.ctx.getModules();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2].merge(this.ctx);
      }

   }

   public void cleanup() {
   }
}
