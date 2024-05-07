package weblogic.application.compiler.flow;

import java.util.Map;
import weblogic.application.compiler.CompilerCtx;
import weblogic.application.internal.flow.CustomModuleHelper;
import weblogic.j2ee.descriptor.wl.WeblogicExtensionBean;
import weblogic.management.DeploymentException;
import weblogic.utils.compiler.ToolFailureException;

public final class CustomModuleProviderFlow extends CompilerFlow {
   public CustomModuleProviderFlow(CompilerCtx var1) {
      super(var1);
   }

   public void compile() throws ToolFailureException {
      try {
         WeblogicExtensionBean var1 = this.ctx.getWLExtensionDD();
         Map var2 = CustomModuleHelper.initFactories(var1, this.ctx.getApplicationContext().getAppClassLoader());
         if (var2 != null) {
            this.ctx.setCustomModuleFactories(var2);
         }
      } catch (DeploymentException var3) {
         throw new ToolFailureException("Encountered exception in initing custom factories", var3);
      }
   }

   public void cleanup() {
   }
}
