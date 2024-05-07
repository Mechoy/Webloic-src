package weblogic.application.compiler.flow;

import weblogic.application.compiler.CompilerCtx;
import weblogic.application.compiler.EARModule;
import weblogic.utils.compiler.ToolFailureException;

public class WriteModulesFlow extends CompilerFlow {
   public WriteModulesFlow(CompilerCtx var1) {
      super(var1);
   }

   private void writeModules(EARModule[] var1) throws ToolFailureException {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2].write(this.ctx);
      }

   }

   public void compile() throws ToolFailureException {
      this.writeModules(this.ctx.getModules());
      EARModule[] var1 = this.ctx.getCustomModules();
      if (var1 != null) {
         this.writeModules(var1);
      }

   }

   public void cleanup() throws ToolFailureException {
   }
}
