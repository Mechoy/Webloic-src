package weblogic.cluster.migration;

import java.security.AccessController;
import java.util.List;
import weblogic.cluster.singleton.AbstractServiceLocationSelector;
import weblogic.cluster.singleton.BasicServiceLocationSelector;
import weblogic.cluster.singleton.ServiceLocationSelector;
import weblogic.cluster.singleton.SingletonServicesDebugLogger;
import weblogic.cluster.singleton.SingletonServicesState;
import weblogic.cluster.singleton.SingletonServicesStateManager;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class ExactlyOnceServiceLocationSelector extends AbstractServiceLocationSelector {
   private MigratableTargetMBean mtBean;
   private ServerMBean upsServer;
   private SingletonServicesStateManager stateManager;
   private List serverList;
   private ServerMBean lastHost;
   private boolean triedOnUPS = false;
   private ServiceLocationSelector sls;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public ExactlyOnceServiceLocationSelector(MigratableTargetMBean var1, SingletonServicesStateManager var2) {
      this.mtBean = var1;
      this.upsServer = var1.getUserPreferredServer();
      this.stateManager = var2;
      this.sls = new BasicServiceLocationSelector(var1.getName(), var2);
   }

   public ServerMBean chooseServer() {
      ServerMBean var1 = null;
      boolean var2 = false;
      if (this.lastHost == null) {
         var1 = this.startOnUPServer(var2);
      } else {
         if (!this.stateManager.checkServiceState(this.mtBean.getName(), 0)) {
            var2 = true;
         }

         boolean var3 = false;
         if (this.lastHost == ManagementService.getRuntimeAccess(kernelId).getServer() && this.lastHost.getName().equals(this.upsServer.getName()) && this.stateManager.getServiceState(this.mtBean.getName()) == null) {
            var3 = true;
         }

         if (!this.lastHost.getName().equals(this.upsServer.getName()) || var3) {
            var1 = this.startOnUPServer(var2);
         }
      }

      if (var1 == null) {
         var1 = this.startOnNonUPServer(var2);
      }

      return var1;
   }

   public void setLastHost(ServerMBean var1) {
      this.lastHost = var1;
      this.sls.setLastHost(var1);
   }

   public void setServerList(List var1) {
      this.serverList = this.getAcceptableServers(var1);
      if (this.serverList.size() > 1) {
         this.serverList.remove(this.upsServer);
      }

      this.sls.setServerList(this.serverList);
   }

   public void migrationSuccessful(ServerMBean var1, boolean var2) {
      if (!var2) {
         this.upsServer = var1;
      }

      this.stateManager.storeServiceState(this.mtBean.getName(), new SingletonServicesState(1));
   }

   private ServerMBean startOnUPServer(boolean var1) {
      ServerMBean var2 = null;
      if (this.isServerRunning(this.upsServer) && !this.triedOnUPS) {
         var2 = this.chooseUPSServer();
      }

      if (var2 != null) {
         if (DEBUG) {
            p(this.mtBean.getName() + "- Going to select (UPS) " + var2);
         }

         if (var1 && !this.executePostScript(this.mtBean, var2, this.lastHost)) {
            this.stateManager.storeServiceState(this.mtBean.getName(), new SingletonServicesState(4));
            var2 = null;
         }
      } else if (DEBUG) {
         p(this.mtBean.getName() + " - UPS Server " + this.upsServer + " not available");
      }

      return var2;
   }

   private ServerMBean startOnNonUPServer(boolean var1) {
      if (this.serverList.size() == 0) {
         return null;
      } else {
         ServerMBean var2 = this.sls.chooseServer();
         if (var2 != null) {
            if (DEBUG) {
               p(this.mtBean.getName() + " - Going to select (Non-UPS) " + var2);
            }

            if (var1 && !this.executePostScript(this.mtBean, var2, this.lastHost)) {
               this.stateManager.storeServiceState(this.mtBean.getName(), new SingletonServicesState(4));
               var2 = null;
            }
         }

         return var2;
      }
   }

   private ServerMBean chooseUPSServer() {
      this.sls.setLastHost(this.upsServer);
      this.triedOnUPS = true;
      return this.upsServer;
   }

   protected static void p(Object var0) {
      SingletonServicesDebugLogger.debug("ExactlyOnceServiceLocationSelector: " + var0);
   }
}
