package weblogic.cluster.singleton;

import java.util.List;
import weblogic.management.configuration.ServerMBean;

public class BasicServiceLocationSelector extends AbstractServiceLocationSelector {
   private String serviceName;
   private SingletonServicesStateManager stateManager;
   private ServerMBean upsServer = null;
   private int serverIndex = -1;
   private List serverList = null;
   private ServerMBean lastHost = null;
   private boolean indexSet = false;
   private boolean triedOnUPS = false;

   public BasicServiceLocationSelector(String var1, SingletonServicesStateManager var2) {
      this.serviceName = var1;
      this.stateManager = var2;
   }

   public void setUPS(ServerMBean var1) {
      this.upsServer = var1;
   }

   public ServerMBean chooseServer() {
      if (this.serverList != null && this.serverList.size() != 0) {
         if (this.upsServer != null && !this.triedOnUPS && this.isServerRunning(this.upsServer)) {
            this.triedOnUPS = true;
            return this.upsServer;
         } else {
            if (!this.indexSet) {
               this.setIndex();
            }

            ServerMBean var1 = null;
            int var2 = (this.serverIndex + 1) % this.serverList.size();
            int var3 = var2;

            do {
               var1 = (ServerMBean)this.serverList.get(var2);
               if (var1 != null) {
                  if (this.isServerRunning(var1)) {
                     this.serverIndex = var2;
                     break;
                  }

                  var1 = null;
               }

               var2 = (var2 + 1) % this.serverList.size();
            } while(var2 != var3);

            return var1;
         }
      } else {
         return null;
      }
   }

   public void setLastHost(ServerMBean var1) {
      this.lastHost = var1;
      this.indexSet = false;
   }

   public void setServerList(List var1) {
      this.serverList = this.getAcceptableServers(var1);
      if (this.serverList.size() > 1 && this.upsServer != null) {
         this.serverList.remove(this.upsServer);
      }

   }

   private void setIndex() {
      if (this.lastHost != null && this.serverList != null) {
         for(int var1 = 0; var1 < this.serverList.size(); ++var1) {
            ServerMBean var2 = (ServerMBean)this.serverList.get(var1);
            if (var2.getName().equals(this.lastHost.getName())) {
               this.serverIndex = var1;
               break;
            }
         }
      }

      this.indexSet = true;
   }

   public void migrationSuccessful(ServerMBean var1, boolean var2) {
      this.stateManager.storeServiceState(this.serviceName, new SingletonServicesState(1));
   }
}
