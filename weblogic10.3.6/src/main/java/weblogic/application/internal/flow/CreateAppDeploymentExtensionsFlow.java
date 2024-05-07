package weblogic.application.internal.flow;

import java.util.Iterator;
import weblogic.application.AppDeploymentExtension;
import weblogic.application.AppDeploymentExtensionFactory;
import weblogic.application.ApplicationFactoryManager;
import weblogic.application.internal.FlowContext;
import weblogic.management.DeploymentException;

public class CreateAppDeploymentExtensionsFlow extends BaseFlow {
   public CreateAppDeploymentExtensionsFlow(FlowContext var1) {
      super(var1);
   }

   public void prepare() throws DeploymentException {
      ApplicationFactoryManager var1 = ApplicationFactoryManager.getApplicationFactoryManager();
      Iterator var2 = var1.getAppExtensionFactories();

      AppDeploymentExtension var4;
      while(var2.hasNext()) {
         AppDeploymentExtensionFactory var3 = (AppDeploymentExtensionFactory)var2.next();
         var4 = var3.createPreProcessorExtension();
         if (var4 != null) {
            this.appCtx.addAppDeploymentExtension(var4, FlowContext.ExtensionType.PRE);
         }

         var4 = var3.createPostProcessorExtension();
         if (var4 != null) {
            this.appCtx.addAppDeploymentExtension(var4, FlowContext.ExtensionType.POST);
         }
      }

      Iterator var5 = this.appCtx.getAppDeploymentExtensions(FlowContext.ExtensionType.PRE).iterator();

      while(var5.hasNext()) {
         var4 = (AppDeploymentExtension)var5.next();
         var4.init(this.appCtx);
      }

   }
}
