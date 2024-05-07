package weblogic.application.internal.flow;

import java.util.Iterator;
import weblogic.application.AppDeploymentExtension;
import weblogic.application.internal.FlowContext;
import weblogic.management.DeploymentException;

public class AppDeploymentExtensionPostInitFlow extends BaseFlow {
   public AppDeploymentExtensionPostInitFlow(FlowContext var1) {
      super(var1);
   }

   public void prepare() throws DeploymentException {
      Iterator var1 = this.appCtx.getAppDeploymentExtensions(FlowContext.ExtensionType.POST).iterator();

      while(var1.hasNext()) {
         AppDeploymentExtension var2 = (AppDeploymentExtension)var1.next();
         var2.init(this.appCtx);
      }

   }
}
