package weblogic.application.internal.flow;

import java.util.Set;
import weblogic.application.AppDeploymentExtension;
import weblogic.application.internal.FlowContext;
import weblogic.management.DeploymentException;

public class AppDeploymentExtensionPreProcessorFlow extends AppDeploymentExtensionFlow {
   public AppDeploymentExtensionPreProcessorFlow(FlowContext var1) {
      super(var1);
   }

   protected Set<AppDeploymentExtension> getExtensions() {
      return this.appCtx.getAppDeploymentExtensions(FlowContext.ExtensionType.PRE);
   }

   public void unprepare() throws DeploymentException {
      super.unprepare();
      this.appCtx.clearAppDeploymentExtensions();
   }
}
