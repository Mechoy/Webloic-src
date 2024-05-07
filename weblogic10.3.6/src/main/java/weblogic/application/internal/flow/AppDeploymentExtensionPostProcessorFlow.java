package weblogic.application.internal.flow;

import java.util.Set;
import weblogic.application.AppDeploymentExtension;
import weblogic.application.internal.FlowContext;

public class AppDeploymentExtensionPostProcessorFlow extends AppDeploymentExtensionFlow {
   public AppDeploymentExtensionPostProcessorFlow(FlowContext var1) {
      super(var1);
   }

   protected Set<AppDeploymentExtension> getExtensions() {
      return this.appCtx.getAppDeploymentExtensions(FlowContext.ExtensionType.POST);
   }
}
