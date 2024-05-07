package weblogic.deploy.internal.targetserver;

import java.io.IOException;
import weblogic.application.Deployment;
import weblogic.deploy.internal.targetserver.datamanagement.AppData;
import weblogic.deploy.internal.targetserver.datamanagement.Data;
import weblogic.deploy.internal.targetserver.operations.AbstractOperation;
import weblogic.deploy.internal.targetserver.state.DeploymentState;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.management.deploy.internal.SlaveDeployerLogger;

public class SystemResourceDeployment extends BasicDeployment {
   public SystemResourceDeployment(SystemResourceMBean var1) {
      super(var1);
   }

   public void verifyAppVersionSecurity(AbstractOperation var1) throws DeploymentException {
   }

   public void prepare() throws IOException, DeploymentException {
      if (isDebugEnabled()) {
         debug("Preparing " + this.name);
      }

      try {
         this.fireVetoableDeploymentEvent();
         this.stageFilesForStatic();
         Deployment var1 = this.createDeployment(this.deploymentMBean, (DeploymentState)null);
         DeploymentContextImpl var2;
         if (this.task != null && this.task.getDeploymentContext() != null) {
            var2 = this.task.getDeploymentContext();
         } else {
            var2 = DeployHelper.createDeploymentContext(this.deploymentMBean);
            var2.setStaticDeploymentOperation(true);
         }

         var1.prepare(var2);
      } catch (Throwable var8) {
         this.removeDeployment();
         DeploymentException var3 = DeployHelper.convertThrowable(var8);
         SlaveDeployerLogger.logIntialPrepareApplicationFailedLoggable(this.name, var3).log();
         throw var3;
      } finally {
         ;
      }
   }

   public void updateDescriptorsPathInfo() {
   }

   public void removeDeployment() {
      this.deploymentManager.removeDeployment(this.deploymentMBean.getName());
   }

   protected final Data createLocalData() {
      BasicDeploymentMBean var1 = this.getDeploymentMBean();
      return new AppData(var1, this, "staged", DeployHelper.getSourcePath(var1), (String)null);
   }
}
