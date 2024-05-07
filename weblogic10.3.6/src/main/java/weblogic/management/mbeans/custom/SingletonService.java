package weblogic.management.mbeans.custom;

import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.cluster.ClusterService;
import weblogic.cluster.singleton.LeasingException;
import weblogic.cluster.singleton.MigratableServerService;
import weblogic.cluster.singleton.SingletonMonitorRemote;
import weblogic.jndi.Environment;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.SingletonServiceMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;
import weblogic.protocol.URLManager;
import weblogic.utils.Debug;

public class SingletonService extends ConfigurationMBeanCustomizer {
   private transient ServerMBean userPreferredServer;
   private String _userPreferredServerName;
   private static final ServerMBean[] NO_SERVERS = new ServerMBean[0];

   public SingletonService(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   private SingletonMonitorRemote getSingletonMonitor(ClusterMBean var1) {
      if (var1 == null) {
         return null;
      } else {
         SingletonMonitorRemote var2 = null;

         try {
            if (ClusterService.getServices() == null) {
               ServerMBean[] var3 = var1.getServers();

               for(int var4 = 0; var4 < var3.length; ++var4) {
                  String var5 = URLManager.findAdministrationURL(var3[var4].getName());
                  var2 = this.getSingletonMonitorRemote(var5);
                  if (var2 != null) {
                     return var2;
                  }
               }
            } else {
               String var8 = MigratableServerService.theOne().findSingletonMaster();
               if (var8 != null) {
                  MigratableServerService.theOne();
                  String var9 = MigratableServerService.findURLOfUnconnectedServer(var8);
                  var2 = this.getSingletonMonitorRemote(var9);
               }
            }
         } catch (LeasingException var6) {
         } catch (UnknownHostException var7) {
         }

         return var2;
      }
   }

   private SingletonMonitorRemote getSingletonMonitorRemote(String var1) {
      Environment var2 = new Environment();
      Context var3 = null;

      SingletonMonitorRemote var4;
      try {
         if (var1 != null) {
            var2.setProviderUrl(var1);
            var3 = var2.getInitialContext();
            var4 = (SingletonMonitorRemote)var3.lookup("weblogic/cluster/singleton/SingletonMonitorRemote");
            return var4;
         }

         var4 = null;
      } catch (NamingException var16) {
         Object var5 = null;
         return (SingletonMonitorRemote)var5;
      } finally {
         if (var3 != null) {
            try {
               var3.close();
            } catch (NamingException var15) {
            }
         }

      }

      return var4;
   }

   public ServerMBean getHostingServer() {
      SingletonServiceMBean var1 = (SingletonServiceMBean)this.getMbean();
      String var2 = null;
      ConfigurationMBean var3 = (ConfigurationMBean)this.getMbean().getParent();
      if (var3 instanceof ServerMBean) {
         return (ServerMBean)var3;
      } else {
         try {
            SingletonMonitorRemote var4 = this.getSingletonMonitor(var1.getCluster());
            if (var4 == null) {
               return this.getUserPreferredServer();
            }

            var2 = var4.findServiceLocation(var1.getName());
         } catch (RemoteException var5) {
            return this.getUserPreferredServer();
         }

         if (var3 instanceof DomainMBean && var2 != null) {
            DomainMBean var6 = (DomainMBean)var3;
            return var6.lookupServer(var2);
         } else {
            return this.getUserPreferredServer();
         }
      }
   }

   public ServerMBean[] getAllCandidateServers() {
      SingletonServiceMBean var1 = (SingletonServiceMBean)this.getMbean();
      if (var1 == null) {
         return NO_SERVERS;
      } else if (var1.getConstrainedCandidateServers() != null && var1.getConstrainedCandidateServers().length > 0) {
         return this.moveUserPreferredServerToHead(var1.getConstrainedCandidateServers());
      } else {
         return var1.getCluster() != null && var1.getCluster().getServers().length > 0 ? this.moveUserPreferredServerToHead(var1.getCluster().getServers()) : NO_SERVERS;
      }
   }

   public boolean isManualActiveOn(ServerMBean var1) {
      return this.getUserPreferredServer().getName().equals(var1.getName());
   }

   public boolean isCandidate(ServerMBean var1) {
      ServerMBean[] var2 = ((SingletonServiceMBean)this.getMbean()).getAllCandidateServers();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var1.getName().equals(var2[var3].getName())) {
            return true;
         }
      }

      return false;
   }

   public ServerMBean getUserPreferredServer() {
      if (this.userPreferredServer == null && this._userPreferredServerName != null) {
         ConfigurationMBean var1 = (ConfigurationMBean)this.getMbean().getParent();
         if (var1 instanceof ServerMBean) {
            var1 = (ConfigurationMBean)var1.getParent();
         }

         if (var1 instanceof DomainMBean) {
            DomainMBean var2 = (DomainMBean)var1;
            this.userPreferredServer = var2.lookupServer(this._userPreferredServerName);
         }
      }

      return this.userPreferredServer;
   }

   public void setUserPreferredServer(ServerMBean var1) {
      this.userPreferredServer = var1;
      if (var1 != null) {
         this._userPreferredServerName = var1.getName();
         ClusterMBean var2 = var1.getCluster();
         SingletonServiceMBean var3 = (SingletonServiceMBean)this.getMbean();
         if (var3.getCluster() == null && var2 != null) {
            var3.setCluster(var2);
            if (var2 == null) {
               Debug.assertion(var3.getCluster() == null);
            } else {
               ClusterMBean var4 = var3.getCluster();
               Debug.assertion(var4 != null, "Migratable Target Cluster is null");
               Debug.assertion(var2.getName() != null, "Cluster name is null");
               Debug.assertion(var2.getName().equals(var4.getName()));
            }
         }
      }

   }

   public Set getServerNames() {
      ServerMBean[] var1 = this.getAllCandidateServers();
      HashSet var2 = new HashSet(var1.length);

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2.add(var1[var3].getName());
      }

      return var2;
   }

   private ServerMBean[] moveUserPreferredServerToHead(ServerMBean[] var1) {
      if (this.getUserPreferredServer() == null) {
         return var1;
      } else {
         ServerMBean var2 = this.getUserPreferredServer();
         String var3 = var2.getName();
         if (var1[0].getName().equals(var3)) {
            return var1;
         } else {
            for(int var4 = 1; var4 < var1.length; ++var4) {
               ServerMBean var5 = var1[var4];
               if (var3.equals(var5.getName())) {
                  ServerMBean var6 = var1[0];
                  var1[0] = var1[var4];
                  var1[var4] = var6;
                  return var1;
               }
            }

            ServerMBean[] var7 = new ServerMBean[var1.length + 1];
            System.arraycopy(var1, 0, var7, 1, var1.length);
            var7[0] = var2;
            return var7;
         }
      }
   }
}
