package weblogic.application.compiler.flow;

import weblogic.application.compiler.AppcUtils;
import weblogic.application.compiler.CompilerCtx;
import weblogic.application.compiler.EARModule;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.compiler.ToolFailureException;

public class SingleModuleCompileFlow extends SingleModuleFlow {
   public SingleModuleCompileFlow(CompilerCtx var1) {
      super(var1);
   }

   protected void proecessModule(EARModule var1) throws ToolFailureException {
      GenericClassLoader var2 = AppcUtils.getClassLoaderForModule(var1.getClassFinder(), this.ctx, this.ctx.getApplicationContext().getApplicationId(), var1.getURI());
      var1.compile(var2, this.ctx);
   }
}
