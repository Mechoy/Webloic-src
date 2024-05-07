package weblogic.application.compiler.flow;

import weblogic.application.compiler.CompilerCtx;
import weblogic.application.compiler.EARModule;
import weblogic.utils.compiler.ToolFailureException;

public final class SingleModuleMergeFlow extends SingleModuleFlow {
   public SingleModuleMergeFlow(CompilerCtx var1) {
      super(var1);
   }

   protected void proecessModule(EARModule var1) throws ToolFailureException {
      var1.merge(this.ctx);
   }
}
