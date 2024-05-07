package weblogic.wsee.deploy;

import javax.naming.Context;
import weblogic.application.ApplicationContext;
import weblogic.ejb.spi.DeploymentInfo;
import weblogic.ejb.spi.EJBDeployListener;
import weblogic.ejb.spi.EJBDeploymentException;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.EJBComponentMBean;

public class WsEJBDeployListener implements EJBDeployListener {
   private WSEEEjbModule wseeEjbModule;
   private boolean prepared = false;
   private ApplicationContext appCtx;

   public void init(ApplicationContext var1, EJBComponentMBean var2) throws EJBDeploymentException {
      String var3 = null;
      if (var2 != null) {
         var3 = var2.getURI();
      }

      this.appCtx = var1;
      this.wseeEjbModule = new WSEEEjbModule(var3, var1);
   }

   public void prepare(DeploymentInfo var1, EjbDescriptorBean var2, ApplicationContext var3) throws EJBDeploymentException {
      this.wseeEjbModule.setEjbDeploymentInfo(var1);
      this.wseeEjbModule.setEjbDescriptorBean(var2);

      try {
         AppDeploymentExtensionFactory.INSTANCE.addModule(var3, this.wseeEjbModule);
      } catch (DeploymentException var5) {
         throw new RuntimeException(var5);
      }

      this.prepared = true;
   }

   public void activate(EjbDescriptorBean var1, ClassLoader var2, Context var3) throws EJBDeploymentException {
   }

   public void deactivate() {
   }

   public void unprepare() {
      if (this.prepared) {
         try {
            AppDeploymentExtensionFactory.INSTANCE.removeModule(this.appCtx, this.wseeEjbModule);
         } catch (DeploymentException var2) {
            throw new RuntimeException(var2);
         }

         this.wseeEjbModule = null;
         this.appCtx = null;
         this.prepared = false;
      }

   }
}
