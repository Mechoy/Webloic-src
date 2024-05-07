package weblogic.application.compiler.flow;

import weblogic.application.compiler.CompilerCtx;
import weblogic.application.compiler.EARModule;
import weblogic.utils.compiler.ToolFailureException;

public class CleanupModulesFlow extends CompilerFlow {
   public CleanupModulesFlow(CompilerCtx var1) {
      super(var1);
   }

   public void compile() throws ToolFailureException {
   }

   public void cleanup() throws ToolFailureException {
      EARModule[] var1 = this.ctx.getModules();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2].cleanup();
      }

   }
}
