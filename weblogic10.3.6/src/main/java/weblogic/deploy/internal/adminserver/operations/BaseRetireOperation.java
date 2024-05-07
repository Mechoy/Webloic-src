package weblogic.deploy.internal.adminserver.operations;

import java.security.AccessController;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.service.ConfigurationContext;
import weblogic.deploy.service.DeploymentRequest;
import weblogic.deploy.service.internal.DeploymentService;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.deploy.DeploymentData;
import weblogic.management.deploy.internal.DeployerRuntimeImpl;
import weblogic.management.deploy.internal.DeploymentServerService;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.DeploymentTaskRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public abstract class BaseRetireOperation extends AbstractOperation {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public BaseRetireOperation() {
      this.controlOperation = true;
   }

   public final DeploymentTaskRuntimeMBean executeControlOperation(String var1, String var2, String var3, DeploymentData var4, String var5, boolean var6, AuthenticatedSubject var7) throws ManagementException {
      try {
         String var8 = OperationHelper.ensureAppName(var2);
         String var9 = OperationHelper.getTaskString(this.taskType);
         String var10 = OperationHelper.getVersionIdFromData(var4, var2);
         DomainMBean var11 = ManagementService.getRuntimeAccess(kernelId).getDomain();
         OperationHelper.assertNameIsNonNull(var8, var9);
         if (isDebugEnabled()) {
            this.printDebugStartMessage(var1, var8, var10, var4, var5, var9, var3);
         }

         AppDeploymentMBean var12 = ApplicationVersionUtils.getAppDeployment(var11, var8, var10);
         OperationHelper.assertAppIsNonNull(var12, var8, var10, var9);
         this.createRuntimeObjects(var1, var5, var12, var4, this.getCreateTaskType(), var11, var7, true);
         DeployerRuntimeImpl var13 = (DeployerRuntimeImpl)DeploymentServerService.getDeployerRuntime();
         var13.registerTaskRuntime(var5, this.deploymentTask);
         createAndInitDeploymentRequest();
         if (var6) {
            this.deploymentTask.start();
         }

         return this.deploymentTask;
      } catch (Throwable var14) {
         deploymentManager.deploymentFailedBeforeStart(this.deployment, var14);
         OperationHelper.logTaskFailed(var2, this.taskType, var14);
         if (var14 instanceof ManagementException) {
            throw (ManagementException)var14;
         } else {
            throw new ManagementException(var14.getMessage(), var14);
         }
      }
   }

   protected final AppDeploymentMBean updateConfiguration(String var1, String var2, String var3, DeploymentData var4, String var5, boolean var6) throws ManagementException {
      return null;
   }

   private static void createAndInitDeploymentRequest() throws ManagementException {
      DeploymentRequest var0 = DeploymentService.getDeploymentService().createDeploymentRequest();
      deploymentManager.addDeploymentsTo(var0, (ConfigurationContext)null);
   }

   protected boolean isRemote(DeploymentData var1) {
      return false;
   }
}
