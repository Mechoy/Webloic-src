package weblogic.deploy.internal.targetserver.operations;

import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.deploy.internal.InternalDeploymentData;
import weblogic.deploy.internal.TargetHelper;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.deploy.DeployerRuntimeTextTextFormatter;
import weblogic.management.deploy.internal.SlaveDeployerLogger;
import weblogic.security.acl.internal.AuthenticatedSubject;

public final class StopOperation extends AbstractOperation {
   protected int cbTag;
   private String[] moduleIds = null;

   public StopOperation(long var1, String var3, InternalDeploymentData var4, BasicDeploymentMBean var5, DomainMBean var6, AuthenticatedSubject var7, boolean var8) throws DeploymentException {
      super(var1, var3, var4, var5, var6, var7, var8);
      this.operation = 8;
      DeploymentOptions var9 = this.deploymentData.getDeploymentOptions();
      if (var9 == null || !var9.isDisableModuleLevelStartStop()) {
         this.moduleIds = TargetHelper.getModulesForTarget(this.deploymentData, var6);
      }

      this.controlOperation = true;
   }

   protected void doPrepare() throws DeploymentException {
      this.ensureAppContainerSet();
   }

   protected void doCommit() throws DeploymentException {
      if (this.appcontainer != null) {
         if (this.getState(this.appcontainer) == 3 || this.getState(this.appcontainer) == 2) {
            if (this.moduleIds != null) {
               this.stop(this.appcontainer, this.moduleIds);
            } else {
               if (this.getState(this.appcontainer) == 3) {
                  if (this.isGracefulProductionToAdmin()) {
                     this.gracefulProductionToAdmin(this.appcontainer);
                  } else {
                     this.forceProductionToAdmin(this.appcontainer);
                  }
               }

               if (!this.isAdminMode() && !this.getApplication().isGracefulInterrupted() && this.getState(this.appcontainer) == 2) {
                  this.appcontainer.deactivate(this.deploymentContext);
               }
            }
         }
      } else {
         SlaveDeployerLogger.logNoDeployment(DeployerRuntimeTextTextFormatter.getInstance().messageStop(), this.mbean.getName());
      }

      this.complete(3, (Exception)null);
   }

   protected final boolean isDeploymentRequestValidForCurrentServer() {
      return this.isTargetListContainsCurrentServer();
   }
}
