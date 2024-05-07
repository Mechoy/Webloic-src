package weblogic.messaging.saf.internal;

import java.security.AccessController;
import javax.naming.NamingException;
import weblogic.jms.saf.SAFAgentDeployer;
import weblogic.jms.saf.SAFService;
import weblogic.management.DeploymentException;
import weblogic.management.ManagementException;
import weblogic.management.UndeploymentException;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.SAFAgentMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.utils.GenericAdminHandler;
import weblogic.messaging.saf.SAFException;
import weblogic.messaging.saf.SAFLogger;
import weblogic.messaging.saf.common.SAFDebug;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.ServiceFailureException;

public final class SAFServiceAdmin implements GenericAdminHandler {
   private SAFAgentDeployer jmsSAFAgentDeployer = SAFService.getSAFService().getDeployer();
   private SAFServerService safService;
   private String serverName;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public SAFServiceAdmin() {
      this.serverName = ManagementService.getRuntimeAccess(kernelId).getServerName();
      this.safService = SAFServerService.getService();
   }

   private void addAgent(SAFAgentAdmin var1) {
      this.safService.addAgent(var1);
   }

   private SAFAgentAdmin getAgent(String var1) {
      return this.safService.getAgent(var1);
   }

   private void removeAgent(String var1) {
      this.safService.removeAgent(var1);
   }

   public void prepare(DeploymentMBean var1) throws DeploymentException {
      if (var1 instanceof SAFAgentMBean) {
         this.jmsSAFAgentDeployer.prepare(var1);

         try {
            this.safService.checkShutdown();
         } catch (ServiceFailureException var3) {
            throw new DeploymentException(var3);
         }

         try {
            this.addAgent(new SAFAgentAdmin(this, (SAFAgentMBean)var1));
         } catch (ManagementException var4) {
            if (SAFDebug.SAFAdmin.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
               SAFDebug.SAFAdmin.debug("Error preparing SAF agent " + this.getAgentName(var1.getName()), var4);
            }

            SAFLogger.logErrorPrepareSAFAgent(this.getAgentName(var1.getName()), var4);
            throw new DeploymentException("Error preparing SAF agent " + this.getAgentName(var1.getName()), var4);
         }

         SAFLogger.logSAFAgentPrepared(this.getAgentName(var1.getName()));
      }
   }

   public void activate(DeploymentMBean var1) throws DeploymentException {
      if (var1 instanceof SAFAgentMBean) {
         this.jmsSAFAgentDeployer.activate(var1);

         try {
            this.safService.checkShutdown();
         } catch (ServiceFailureException var4) {
            throw new DeploymentException(var4);
         }

         SAFAgentAdmin var2 = this.getAgent(var1.getName());
         if (var2 == null) {
            throw new DeploymentException("Error activating SAF agent " + this.getAgentName(var1.getName()) + ": it was not successfully prepared");
         } else {
            try {
               var2.start();
            } catch (SAFException var5) {
               if (SAFDebug.SAFAdmin.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
                  SAFDebug.SAFAdmin.debug("Error activating SAF agent " + this.getAgentName(var1.getName()), var5);
               }

               SAFLogger.logErrorStartSAFAgent(this.getAgentName(var1.getName()), var5);
               throw new DeploymentException("Error activating SAF agent " + this.getAgentName(var1.getName()), var5);
            } catch (NamingException var6) {
               if (SAFDebug.SAFAdmin.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
                  SAFDebug.SAFAdmin.debug("Error activating SAF agent " + this.getAgentName(var1.getName()), var6);
               }

               SAFLogger.logErrorStartSAFAgent(this.getAgentName(var1.getName()), var6);
               throw new DeploymentException("Error activating SAF agent " + this.getAgentName(var1.getName()), var6);
            }

            SAFLogger.logSAFAgentActivated(this.getAgentName(var1.getName()));
         }
      }
   }

   public void deactivate(DeploymentMBean var1) throws UndeploymentException {
      if (var1 instanceof SAFAgentMBean) {
         if (SAFDebug.SAFAdmin.isDebugEnabled() && SAFDebug.SAFVerbose.isDebugEnabled()) {
            SAFDebug.SAFAdmin.debug("Undeploying " + var1.getName());
         }

         SAFAgentAdmin var2 = this.getAgent(var1.getName());
         if (var2 != null) {
            var2.close();
            SAFLogger.logSAFAgentDeactivated(this.getAgentName(var1.getName()));
            this.jmsSAFAgentDeployer.deactivate(var1);
         }
      }
   }

   public void unprepare(DeploymentMBean var1) throws UndeploymentException {
      if (var1 instanceof SAFAgentMBean) {
         this.removeAgent(var1.getName());
         SAFLogger.logSAFAgentUnprepared(this.getAgentName(var1.getName()));
         this.jmsSAFAgentDeployer.unprepare(var1);
      }
   }

   private String getAgentName(String var1) {
      return var1 + "@" + this.serverName;
   }
}
