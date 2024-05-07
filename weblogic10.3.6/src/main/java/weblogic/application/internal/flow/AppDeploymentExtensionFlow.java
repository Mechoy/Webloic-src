package weblogic.application.internal.flow;

import java.util.Iterator;
import java.util.Set;
import weblogic.application.AppDeploymentExtension;
import weblogic.application.internal.FlowContext;
import weblogic.management.DeploymentException;

public abstract class AppDeploymentExtensionFlow extends BaseFlow {
   public AppDeploymentExtensionFlow(FlowContext var1) {
      super(var1);
   }

   protected abstract Set<AppDeploymentExtension> getExtensions();

   public void prepare() throws DeploymentException {
      Iterator var1 = this.getExtensions().iterator();

      while(var1.hasNext()) {
         AppDeploymentExtension var2 = (AppDeploymentExtension)var1.next();
         var2.prepare(this.appCtx);
      }

   }

   public void unprepare() throws DeploymentException {
      Iterator var1 = this.getExtensions().iterator();

      while(var1.hasNext()) {
         AppDeploymentExtension var2 = (AppDeploymentExtension)var1.next();
         var2.unprepare(this.appCtx);
      }

   }

   public void activate() throws DeploymentException {
      Iterator var1 = this.getExtensions().iterator();

      while(var1.hasNext()) {
         AppDeploymentExtension var2 = (AppDeploymentExtension)var1.next();
         var2.activate(this.appCtx);
      }

   }

   public void deactivate() throws DeploymentException {
      Iterator var1 = this.getExtensions().iterator();

      while(var1.hasNext()) {
         AppDeploymentExtension var2 = (AppDeploymentExtension)var1.next();
         var2.deactivate(this.appCtx);
      }

   }
}
