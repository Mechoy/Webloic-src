package weblogic.deploy.internal.targetserver.operations;

import weblogic.deploy.internal.InternalDeploymentData;
import weblogic.deploy.internal.targetserver.DeployHelper;
import weblogic.management.DeploymentException;
import weblogic.management.ManagementException;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;

public class DeactivateOperation extends AbstractOperation {
   protected int cbTag;

   public DeactivateOperation(long var1, String var3, InternalDeploymentData var4, BasicDeploymentMBean var5, DomainMBean var6, AuthenticatedSubject var7, boolean var8) throws DeploymentException {
      super(var1, var3, var4, var5, var6, var7, var8);
      this.operation = 3;
      this.controlOperation = true;
   }

   protected void doCommit() throws DeploymentException {
      try {
         this.deactivate();
         this.complete(3, (Exception)null);
      } catch (ManagementException var2) {
         throw DeployHelper.convertThrowable(var2);
      }
   }

   protected void deactivate() throws DeploymentException {
      if (this.isDebugEnabled()) {
         this.debug(" Deactivating application = " + this.getApplication().getName());
      }

      this.appcontainer = this.getApplication().findDeployment();
      if (this.appcontainer != null) {
      }

      if (this.appcontainer != null) {
         int var1 = this.getState(this.appcontainer);
         if (this.isGracefulProductionToAdmin()) {
            if (var1 == 3) {
               this.gracefulProductionToAdmin(this.appcontainer);
            }
         } else if (var1 == 3 || var1 == 2 && this.getApplication().hasPendingGraceful()) {
            this.forceProductionToAdmin(this.appcontainer);
         }

         if (this.getState(this.appcontainer) == 2) {
            this.appcontainer.deactivate(this.deploymentContext);
         }
      }

   }

   protected boolean isDeploymentRequestValidForCurrentServer() {
      return this.isTargetListContainsCurrentServer();
   }
}
