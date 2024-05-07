package weblogic.cluster.migration;

import java.util.List;
import weblogic.cluster.singleton.AbstractServiceLocationSelector;
import weblogic.cluster.singleton.BasicServiceLocationSelector;
import weblogic.cluster.singleton.ServiceLocationSelector;
import weblogic.cluster.singleton.SingletonServicesDebugLogger;
import weblogic.cluster.singleton.SingletonServicesState;
import weblogic.cluster.singleton.SingletonServicesStateManager;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;

public class FailureRecoveryServiceLocationSelector extends AbstractServiceLocationSelector {
   private MigratableTargetMBean mtBean;
   private ServerMBean upsServer;
   private SingletonServicesStateManager stateManager;
   private List serverList;
   private ServerMBean lastHost;
   private boolean triedOnUPS = false;
   private ServiceLocationSelector sls;

   public FailureRecoveryServiceLocationSelector(MigratableTargetMBean var1, SingletonServicesStateManager var2) {
      this.mtBean = var1;
      this.upsServer = var1.getUserPreferredServer();
      this.stateManager = var2;
      this.sls = new BasicServiceLocationSelector(var1.getName(), var2);
   }

   public ServerMBean chooseServer() {
      ServerMBean var1 = null;
      boolean var2 = this.stateManager.checkServiceState(this.mtBean.getName(), 1);
      boolean var3 = this.stateManager.checkServiceState(this.mtBean.getName(), 2);
      boolean var4 = this.stateManager.checkServiceState(this.mtBean.getName(), 3);
      boolean var5 = this.stateManager.checkServiceState(this.mtBean.getName(), 0);
      if (var4) {
         if (DEBUG) {
            p(this.mtBean.getName() + " - is marked as shutdown. Trying to" + " start it on its UPS Server");
         }

         return this.startOnUPServer(false);
      } else {
         if (var5) {
            if (this.lastHost == null) {
               if (DEBUG) {
                  p(this.mtBean.getName() + " - is marked as failed and its last host " + "is null. First trying to start it on its UPS Server");
               }

               var1 = this.startOnUPServer(false);
            } else {
               if (DEBUG) {
                  p(this.mtBean.getName() + " - is marked as failed and its last host " + "is not null. First trying to start it on its UPS Server");
               }

               if (!this.lastHost.getName().equals(this.upsServer.getName())) {
                  var1 = this.startOnUPServer(false);
               }
            }

            if (var1 == null) {
               if (DEBUG) {
                  p(this.mtBean.getName() + " -  Now trying to start it on its Non-UPS Server");
               }

               var1 = this.startOnNonUPServer(false);
            }
         } else {
            if (!var3 && !var2) {
               if (DEBUG) {
                  p(this.mtBean.getName() + " - is neither marked as NonMigratable nor " + "migratable. Trying to start it on its UPS Server");
               }

               return this.startOnUPServer(false);
            }

            if (var3) {
               if (this.lastHost == null) {
                  if (DEBUG) {
                     p(this.mtBean.getName() + " is marked as NonMigratable and its " + "lasthost is null. Trying to start it on its UPS Server");
                  }

                  return this.startOnUPServer(false);
               }

               if (DEBUG) {
                  p(this.mtBean.getName() + " is marked as NonMigratable and its " + "lasthost is not null. First trying to start it on its UPS Server");
               }

               if (!this.triedOnUPS) {
                  var1 = this.startOnUPServer(true);
               }

               if (var1 == null) {
                  if (DEBUG) {
                     p(this.mtBean.getName() + " -  Now trying to start it on its Non-UPS Server");
                  }

                  var1 = this.startOnNonUPServer(true);
               }
            } else if (var2) {
               if (this.lastHost == null) {
                  if (DEBUG) {
                     p(this.mtBean.getName() + " is marked as Migratable and its " + "lasthost is null. First trying to start it on its UPS Server.");
                  }

                  if (!this.triedOnUPS) {
                     var1 = this.startOnUPServer(false);
                  }

                  if (var1 == null) {
                     if (DEBUG) {
                        p(this.mtBean.getName() + " -  Now trying to start it on its Non-UPS Server");
                     }

                     var1 = this.startOnNonUPServer(false);
                  }
               } else {
                  if (DEBUG) {
                     p(this.mtBean.getName() + " is marked as Migratable and its " + "lasthost is not null. First trying to start it on its UPS Server.");
                  }

                  if (!this.triedOnUPS) {
                     var1 = this.startOnUPServer(true);
                  }

                  if (var1 == null) {
                     if (DEBUG) {
                        p(this.mtBean.getName() + " -  Now trying to start it on its Non-UPS Server");
                     }

                     var1 = this.startOnNonUPServer(true);
                  }
               }
            }
         }

         return var1;
      }
   }

   private ServerMBean startOnUPServer(boolean var1) {
      ServerMBean var2 = null;
      if (this.isServerRunning(this.upsServer) && !this.triedOnUPS) {
         var2 = this.chooseUPSServer();
      }

      if (var2 != null) {
         if (DEBUG) {
            p(this.mtBean.getName() + " - Going to select (UPS) " + var2);
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
      SingletonServicesState var3 = null;
      if (var2) {
         if (var1.getName().equals(this.upsServer.getName())) {
            var3 = new SingletonServicesState(2);
         } else {
            var3 = new SingletonServicesState(1);
         }
      } else {
         this.upsServer = var1;
         var3 = new SingletonServicesState(2);
      }

      this.stateManager.storeServiceState(this.mtBean.getName(), var3);
   }

   private ServerMBean chooseUPSServer() {
      this.sls.setLastHost(this.upsServer);
      this.triedOnUPS = true;
      return this.upsServer;
   }

   protected static void p(Object var0) {
      SingletonServicesDebugLogger.debug("FailureRecoveryServiceLocationSelector: " + var0);
   }
}
