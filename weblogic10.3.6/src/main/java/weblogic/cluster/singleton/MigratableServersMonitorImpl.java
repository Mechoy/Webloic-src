package weblogic.cluster.singleton;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import weblogic.cluster.ClusterLogger;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.MachineMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.nodemanager.mbean.NodeManagerRuntime;
import weblogic.protocol.LocalServerIdentity;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.timers.NakedTimerListener;
import weblogic.timers.Timer;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.collections.ConcurrentHashMap;

class MigratableServersMonitorImpl implements NakedTimerListener, MigratableServiceConstants {
   public static final String LEASE_TYPE = "wlsserver";
   private final boolean DEBUG = MigrationDebugLogger.isDebugEnabled();
   private final int leaseRenewInterval;
   private Timer timer;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private HashMap serverMachineTable;
   private final ConcurrentHashMap migratableServerMap = new ConcurrentHashMap();
   private ConcurrentHashMap previouslyUnresponsiveServers;
   private TimerManager timerManager = null;
   private final LeaseManager manager;

   MigratableServersMonitorImpl(LeaseManager var1, int var2) {
      this.manager = var1;
      this.leaseRenewInterval = var2;
      this.previouslyUnresponsiveServers = new ConcurrentHashMap();
      this.serverMachineTable = new HashMap();
   }

   void start() {
      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      MachineMBean[] var2 = var1.getMachines();
      ClusterMBean var3 = var1.lookupServer(LocalServerIdentity.getIdentity().getServerName()).getCluster();
      if (this.DEBUG) {
         this.p("Processing Cluster " + var3.getName());
      }

      ServerMBean[] var4 = var3.getServers();

      for(int var5 = 0; var5 < var2.length; ++var5) {
         if (this.DEBUG) {
            this.p("Processing Machine " + var2[var5].getName());
         }

         NodeManagerRuntime var6 = NodeManagerRuntime.getInstance(var2[var5]);

         for(int var8 = 0; var8 < var4.length; ++var8) {
            if (this.DEBUG) {
               this.p("Processing Server[" + var8 + "] " + var4[var8].getName());
            }

            if (!var4[var8].isAutoMigrationEnabled()) {
               if (this.DEBUG) {
                  this.p("Skipping server with Auto Migration Diabled " + var4[var8].toString());
               }
            } else {
               String var7;
               try {
                  var7 = var6.getState(var4[var8]);
               } catch (IOException var10) {
                  if (this.DEBUG) {
                     this.p("IOException from machine " + var2[var5].getName());
                  }
                  break;
               }

               if (var7.indexOf("FAILED") == -1 && var7.indexOf("SHUTDOWN") == -1 && var7.indexOf("UNKNOWN") == -1) {
                  if (this.serverMachineTable.get(var4[var8].getName()) == null) {
                     ClusterLogger.logMonitoringMigratableServer(var4[var8].getName());
                  }

                  this.serverMachineTable.put(var4[var8].getName(), var2[var5].getName());
                  if (this.DEBUG) {
                     this.p("Found " + var4[var8] + " living on " + var2[var5]);
                  }
               } else if (this.DEBUG) {
                  this.p(var4[var8] + " is not active on " + var2[var5]);
               }
            }
         }
      }

      this.timerManager = TimerManagerFactory.getTimerManagerFactory().getTimerManager("SingletonServiceTimerManager", "weblogic.kernel.System");
      this.timer = this.timerManager.schedule(this, 0L, (long)this.leaseRenewInterval);
   }

   void stop() {
      if (this.timer != null) {
         this.timer.cancel();
      }

   }

   public void timerExpired(Timer var1) {
      String[] var2 = this.manager.findExpiredLeases();
      ConcurrentHashMap var3 = new ConcurrentHashMap(var2.length * 2);

      for(int var4 = 0; var4 < var2.length; ++var4) {
         if (!"CLUSTER_MASTER".equals(var2[var4]) && !"SINGLETON_MASTER".equals(var2[var4])) {
            var3.put(var2[var4], var2[var4]);
            this.previouslyUnresponsiveServers.remove(var2[var4]);
            MigratableServerState var5 = this.findOrCreateMigratableServerStateInfo(var2[var4]);
            if (var5 != null) {
               try {
                  if (this.DEBUG) {
                     this.p("Sending server-unresponsive to " + var5);
                  }

                  var5.serverUnresponsive();
               } catch (ServerMigrationException var10) {
                  ClusterLogger.logFailedToAutomaticallyMigrateServers2(var5.getServer().getName(), var10);
               }
            }
         }
      }

      Set var11 = this.previouslyUnresponsiveServers.keySet();
      Iterator var12 = var11.iterator();

      while(var12.hasNext()) {
         String var6 = (String)var12.next();
         if (!"CLUSTER_MASTER".equals(var6)) {
            MigratableServerState var7 = this.findOrCreateMigratableServerStateInfo(var6);
            if (var7 != null) {
               var7.markAsMigratableAgain();

               try {
                  var7.serverUnresponsive();
               } catch (ServerMigrationException var9) {
                  ClusterLogger.logFailedToAutomaticallyMigrateServers2(var7.getServer().getName(), var9);
               }
            }
         }
      }

      this.previouslyUnresponsiveServers = var3;
   }

   private MigratableServerState findOrCreateMigratableServerStateInfo(String var1) {
      MigratableServerState var2 = (MigratableServerState)this.migratableServerMap.get(var1);
      if (var2 == null) {
         var2 = this.createMigratableServerStateInfo(var1);
         if (var2 == null) {
            return null;
         }

         this.migratableServerMap.put(var1, var2);
         if (MigrationDebugLogger.isDebugEnabled()) {
            MigrationDebugLogger.debug(var1 + " failed to renew its lease");
         }
      }

      return var2;
   }

   private MigratableServerState createMigratableServerStateInfo(String var1) {
      String var2 = this.getCurrentMachine(var1);
      if (var2 == null) {
         return null;
      } else {
         DomainMBean var3 = ManagementService.getRuntimeAccess(kernelId).getDomain();
         MachineMBean var4 = this.getMachine(var3, var2);
         ServerMBean var5 = var3.lookupServer(var1);
         return var5 != null && var4 != null ? new MigratableServerState(var5, var4, this) : null;
      }
   }

   public synchronized void setServerLocation(String var1, String var2) throws RemoteException {
      if (this.serverMachineTable.get(var1) == null) {
         ClusterLogger.logMonitoringMigratableServer(var1);
      }

      this.serverMachineTable.put(var1, var2);
      if (this.DEBUG) {
         this.p("Got a report: " + var1 + " is now on " + var2);
      }

   }

   public synchronized String getCurrentMachine(String var1) {
      String var2 = (String)this.serverMachineTable.get(var1);
      return var2 != null ? var2 : this.getConfiguredMachine(var1);
   }

   private String getConfiguredMachine(String var1) {
      ServerMBean var2 = ManagementService.getRuntimeAccess(kernelId).getDomain().lookupServer(var1);
      if (var2 == null) {
         return null;
      } else {
         return var2.getMachine() == null ? null : var2.getMachine().getName();
      }
   }

   private void p(Object var1) {
      System.out.println("<MigratableServersMonitorImpl> " + var1);
   }

   MachineMBean getMachine(String var1) {
      DomainMBean var2 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      return this.getMachine(var2, var1);
   }

   private MachineMBean getMachine(DomainMBean var1, String var2) {
      return var1.lookupMachine(var2);
   }
}
