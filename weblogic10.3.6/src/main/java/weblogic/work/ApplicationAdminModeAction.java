package weblogic.work;

import java.security.AccessController;
import weblogic.application.Deployment;
import weblogic.application.DeploymentManager;
import weblogic.application.utils.EarUtils;
import weblogic.deploy.internal.targetserver.DeployHelper;
import weblogic.deploy.internal.targetserver.DeploymentContextImpl;
import weblogic.j2ee.descriptor.wl.ApplicationAdminModeTriggerBean;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

final class ApplicationAdminModeAction extends AbstractStuckThreadAction {
   private final String applicationName;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   ApplicationAdminModeAction(ApplicationAdminModeTriggerBean var1, String var2) {
      super((long)var1.getMaxStuckThreadTime(), var1.getStuckThreadCount());
      this.applicationName = var2;
      if (this.isDebugEnabled()) {
         this.debug("MaxStuckThreadTime=" + var1.getMaxStuckThreadTime() + ", StuckThreadCount=" + var1.getStuckThreadCount());
      }

   }

   public void execute() {
      final Deployment var1 = DeploymentManager.getDeploymentManager().findDeployment(this.applicationName);
      if (this.isDebugEnabled()) {
         this.debug("executing action for deployment " + var1);
      }

      assert var1 != null;

      WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
         public void run() {
            try {
               if (ApplicationAdminModeAction.this.isDebugEnabled()) {
                  ApplicationAdminModeAction.this.debug("invoking forceProductionToAdmin");
               }

               DeploymentContextImpl var1x = DeployHelper.createDeploymentContext((BasicDeploymentMBean)null);
               var1x.setAdminModeTransition(true);
               var1x.setAdminModeCallback(EarUtils.noopAdminModeCallback);
               var1.forceProductionToAdmin(var1x);
               if (ApplicationAdminModeAction.this.isDebugEnabled()) {
                  ApplicationAdminModeAction.this.debug("forceProductionToAdmin invoked");
               }
            } catch (DeploymentException var2) {
               var2.printStackTrace();
            }

         }
      });
   }

   public void withdraw() {
      final Deployment var1 = DeploymentManager.getDeploymentManager().findDeployment(this.applicationName);
      if (this.isDebugEnabled()) {
         this.debug("withdraw action for deployment " + var1);
      }

      assert var1 != null;

      WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
         public void run() {
            try {
               if (ApplicationAdminModeAction.this.isDebugEnabled()) {
                  ApplicationAdminModeAction.this.debug("invoking adminToProduction");
               }

               DeploymentContextImpl var1x = DeployHelper.createDeploymentContext((BasicDeploymentMBean)null);
               var1x.setAdminModeTransition(false);
               var1.adminToProduction(var1x);
               if (ApplicationAdminModeAction.this.isDebugEnabled()) {
                  ApplicationAdminModeAction.this.debug("adminToProduction invoked");
               }
            } catch (DeploymentException var2) {
               var2.printStackTrace();
            }

         }
      });
   }

   private void debug(String var1) {
      WorkManagerLogger.logDebug("[ApplicationAdminModeAction][" + this.applicationName + "]" + var1);
   }
}
