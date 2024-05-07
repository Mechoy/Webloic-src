package weblogic.application.compiler.flow;

import weblogic.application.compiler.CompilerCtx;
import weblogic.application.compiler.EARModule;
import weblogic.utils.compiler.ToolFailureException;

public class ModuleClassLoaderFlow extends CompilerFlow {
   public ModuleClassLoaderFlow(CompilerCtx var1) {
      super(var1);
   }

   public void compile() throws ToolFailureException {
      EARModule[] var1 = this.ctx.getModules();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2].initModuleClassLoader(this.ctx, this.ctx.getApplicationContext().getAppClassLoader());
      }

   }

   public void cleanup() throws ToolFailureException {
   }
}
