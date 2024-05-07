package weblogic.deploy.internal.targetserver.operations;

import weblogic.deploy.internal.InternalDeploymentData;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.deploy.DeployerRuntimeTextTextFormatter;
import weblogic.management.deploy.internal.SlaveDeployerLogger;
import weblogic.security.acl.internal.AuthenticatedSubject;

public final class RetireOperation extends AbstractOperation {
   public RetireOperation(long var1, String var3, InternalDeploymentData var4, BasicDeploymentMBean var5, DomainMBean var6, AuthenticatedSubject var7, boolean var8) throws DeploymentException {
      super(var1, var3, var4, var5, var6, var7, var8);
      this.operation = 13;
      this.controlOperation = true;
   }

   protected void doCommit() throws DeploymentException {
      if (this.isDebugEnabled()) {
         this.debug("Retiring application " + this.getApplication().getName());
      }

      this.appcontainer = this.getApplication().findDeployment();
      if (this.appcontainer != null && this.getApplication().needRetirement()) {
         try {
            if (this.getState(this.appcontainer) == 3) {
               this.forceProductionToAdmin(this.appcontainer);
            }

            if (this.getState(this.appcontainer) == 2) {
               this.appcontainer.deactivate(this.deploymentContext);
            }

            if (this.getState(this.appcontainer) == 1) {
               this.silentUnprepare(this.appcontainer);
            }
         } finally {
            if (this.getState() != null) {
               this.getState().setCurrentState("STATE_RETIRED", true);
            }

         }
      } else {
         SlaveDeployerLogger.logNoDeployment(DeployerRuntimeTextTextFormatter.getInstance().messageRetire(), this.mbean.getName());
      }

      this.complete(3, (Exception)null);
   }
}
