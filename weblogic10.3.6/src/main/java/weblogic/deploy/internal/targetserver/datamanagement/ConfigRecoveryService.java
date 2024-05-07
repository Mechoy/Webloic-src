package weblogic.deploy.internal.targetserver.datamanagement;

import java.security.AccessController;
import weblogic.deploy.common.Debug;
import weblogic.logging.Loggable;
import weblogic.management.deploy.internal.DeploymentManagerLogger;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public final class ConfigRecoveryService extends AbstractServerService {
   private static AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void start() throws ServiceFailureException {
      if (ManagementService.getPropertyService(KERNEL_ID).isAdminServer()) {
         try {
            ConfigBackupRecoveryManager.getInstance().restoreFromBackup();
         } catch (Throwable var3) {
            Loggable var2 = DeploymentManagerLogger.logFailureOnConfigRecoveryLoggable(var3);
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug(var2.getMessage());
            }

            var2.log();
         }
      }

   }
}
