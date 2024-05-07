package weblogic.cluster.singleton;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.security.AccessController;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.cluster.ClusterExtensionLogger;
import weblogic.cluster.ClusterLogger;
import weblogic.cluster.ClusterService;
import weblogic.cluster.migration.ExactlyOnceServiceLocationSelector;
import weblogic.cluster.migration.FailureRecoveryServiceLocationSelector;
import weblogic.cluster.migration.MigrationException;
import weblogic.cluster.migration.RemoteMigratableServiceCoordinator;
import weblogic.deploy.event.DeploymentEventManager;
import weblogic.jndi.Environment;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JTAMigratableTargetMBean;
import weblogic.management.configuration.MachineMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.SingletonServiceMBean;
import weblogic.management.provider.ManagementService;
import weblogic.nodemanager.mbean.NodeManagerRuntime;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.URLManager;
import weblogic.rjvm.PeerGoneException;
import weblogic.rmi.extensions.PortableRemoteObject;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.ServiceFailureException;
import weblogic.timers.NakedTimerListener;
import weblogic.timers.Timer;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.transaction.internal.TxDebug;
import weblogic.work.WorkManagerFactory;

public class SingletonMonitor implements NakedTimerListener, MigratableServiceConstants, SingletonMonitorRemote, ConsensusServiceGroupViewListener {
   private static final boolean DEBUG = SingletonServicesDebugLogger.isDebugEnabled();
   public static final String LEASE_TYPE = "service";
   private final int leaseRenewInterval;
   private Timer timer;
   private boolean active = false;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private SingletonMonitorServiceTracker serviceTracker;
   private TimerManager timerManager = null;
   private LeaseManager manager;
   private ClusterMBean cluster;
   private DomainMBean domain;
   private SingletonServicesStateManager stateManager;
   ServiceMigrationRuntimeMBeanImpl runtimeMBean;
   private MemberDeathDetector memberDeathDetector;
   HashSet currentlyRunningMigrations = new HashSet();

   SingletonMonitor(LeaseManager var1, int var2) {
      this.manager = var1;
      this.leaseRenewInterval = var2;
      this.serviceTracker = new SingletonMonitorServiceTracker(var1);
      this.stateManager = new ReplicatedSingletonServicesStateManager("default-singleton-statemanager", var1);
   }

   private ServiceMigrationRuntimeMBeanImpl getRuntimeMBean() {
      if (this.runtimeMBean != null) {
         return this.runtimeMBean;
      } else {
         this.runtimeMBean = ServiceMigrationRuntimeMBeanImpl.getInstance();
         return this.runtimeMBean;
      }
   }

   void start() {
      if (DEBUG) {
         this.p("Starting singleton monitor");
      }

      this.domain = ManagementService.getRuntimeAccess(kernelId).getDomain();
      this.cluster = this.domain.lookupServer(LocalServerIdentity.getIdentity().getServerName()).getCluster();
      this.getRuntimeMBean();
      this.serviceTracker.initialize(this.cluster, this.domain);
      DomainMBean var1 = this.domain;
      var1.addBeanUpdateListener(this.serviceTracker);
      DeploymentEventManager.addDeploymentEventListener(this.serviceTracker, false);
      ClusterService.getServices().addClusterMembersListener(this.serviceTracker);
      this.stateManager.leaseAcquired();
      synchronized(this) {
         this.active = true;
      }

      if (DEBUG) {
         this.p("Scheduling monitoring service to check lease status every " + this.leaseRenewInterval + " millis.");
      }

      this.timerManager = TimerManagerFactory.getTimerManagerFactory().getTimerManager("SingletonServiceTimerManager", "weblogic.kernel.System");
      this.timer = this.timerManager.schedule(this, 0L, (long)this.leaseRenewInterval);
      if (ClusterService.getClusterService().isMemberDeathDetectorEnabled() && !"consensus".equals(MigratableServerService.theOne().getLeasingType())) {
         ClusterExtensionLogger.logStartingMemberDeathDetector();
         this.memberDeathDetector = this.initializeMemberDeathDetector();
      }

   }

   void stop() {
      synchronized(this) {
         this.active = false;
      }

      if (this.timer != null) {
         this.timer.cancel();
      }

      this.stateManager.lostLease();
      if (this.memberDeathDetector != null) {
         this.memberDeathDetector.stop();
      }

   }

   public void register(String var1) {
      this.serviceTracker.register(var1);
   }

   public void registerJTA(String var1) {
      this.serviceTracker.registerJTA(var1);
   }

   public void unregister(String var1) {
      this.serviceTracker.unregister(var1);
   }

   public String findServiceLocation(String var1) {
      try {
         String var2 = this.manager.findOwner(var1);
         ServerMBean var3 = null;
         if (var2 != null && this.isServiceActive(var1)) {
            var3 = this.domain.lookupServer(LeaseManager.getServerNameFromOwnerIdentity(var2));
         }

         return var3 == null ? null : var3.getName();
      } catch (LeasingException var4) {
         return null;
      }
   }

   private boolean isServiceActive(String var1) {
      MigratableTargetMBean var2 = this.getMigratableTarget(var1);
      if (var2 != null && var2 instanceof JTAMigratableTargetMBean) {
         return true;
      } else {
         return this.stateManager.checkServiceState(var1, 1) || this.stateManager.checkServiceState(var1, 2);
      }
   }

   private void checkFailedLeases(Collection var1) {
      if (DEBUG) {
         this.p("Checking Failed Leases");
      }

      Map var2 = this.stateManager.getAllServicesState();
      Iterator var3 = var2.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry var4 = (Map.Entry)var3.next();
         Serializable var5 = (Serializable)var4.getKey();
         SingletonServicesState var6 = (SingletonServicesState)var4.getValue();
         if (var6.getState() == 0) {
            if (DEBUG) {
               this.p(var5 + " - Detected that it is in Failed State.");
            }

            var1.add(var5);
         }
      }

   }

   private void checkExpiredLeases(Collection var1) {
      if (DEBUG) {
         this.p("Checking existant, but expired leases");
      }

      String[] var2 = this.manager.findExpiredLeases();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (DEBUG) {
            this.p(var2[var3] + " - Its an expired lease.");
         }

         SingletonDataObject var4 = (SingletonDataObject)this.serviceTracker.get(var2[var3]);
         String var5 = null;
         if (var4 == null) {
            SingletonService var6 = SingletonServicesManager.getInstance().getService(var2[var3]);
            if (var6 != null) {
               var5 = var2[var3];
            }
         } else {
            var5 = var4.getName();
         }

         if (var5 == null) {
            if (DEBUG) {
               this.p(var2[var3] + " - But the expired lease corresponds to no registered " + "singleton service, ignoring it");
            }
         } else if (!var1.contains(var5)) {
            if (!this.startMigration(var5, -1L)) {
               if (DEBUG) {
                  this.p(var5 + " - In the middle of migration. Not starting new one.");
               }
            } else {
               var1.add(var5);
            }
         }
      }

   }

   private void checkRegisteredSingletons(Collection var1) {
      if (DEBUG) {
         this.p("Checking for registered Migratable Targets and Singleton Services without a lease");
      }

      Iterator var2 = this.serviceTracker.values().iterator();

      while(var2.hasNext()) {
         SingletonDataObject var3 = (SingletonDataObject)var2.next();
         String var4 = var3.getName();

         try {
            if (!this.startMigration(var4, -1L)) {
               if (DEBUG) {
                  this.p(var4 + " - In the middle of migration. Not starting new one.");
               }
            } else {
               String var5 = this.manager.findOwner(var4);
               if (var5 != null) {
                  if (DEBUG) {
                     this.p(var4 + " - Found an owner - " + var5);
                  }

                  if (var3.isJTA() && this.stateManager.getServiceState(var4) == null) {
                     ServerMBean var6 = this.domain.lookupServer(LeaseManager.getServerNameFromOwnerIdentity(var5));
                     ServiceLocationSelector var7 = this.getServiceLocationSelector(var3.getName());
                     var7.migrationSuccessful(var6, true);
                  }

                  this.endMigration(var4);
               } else {
                  if (DEBUG) {
                     this.p(var4 + " - Detected that it has no lease.");
                  }

                  var1.add(var4);
               }
            }
         } catch (LeasingException var8) {
            this.endMigration(var4);
            ClusterLogger.logExceptionWhileMigratingService(var4 != null ? var4 : "an unknown service", var8);
         }
      }

   }

   private boolean restartInPlace(String var1, ServerMBean var2, int var3, ServiceLocationSelector var4) {
      MigratableTargetMBean var5 = this.getMigratableTarget(var1);
      if (var5 == null) {
         return false;
      } else if (!var5.getRestartOnFailure()) {
         return false;
      } else if (var3 == -1 || var3 != 0 && var3 != 1 && var3 != 2) {
         return false;
      } else if (var2 == null) {
         return false;
      } else {
         for(int var6 = 0; var6 < var5.getNumberOfRestartAttempts(); ++var6) {
            RemoteSingletonServicesControl var7 = this.getRemoteSingletonServicesControl(var2.getName());
            if (var7 == null) {
               return false;
            }

            if (this.getRuntimeMBean() != null) {
               this.getRuntimeMBean().migrationStarted(var1, var2.getName(), var2.getName());
            }

            try {
               if (DEBUG) {
                  this.p("Trying to re-start service " + var1 + " on " + var2);
               }

               var7.restartService(var1);
               var4.migrationSuccessful(var2, true);
               boolean var8 = true;
               return var8;
            } catch (MigrationException var16) {
               if (DEBUG) {
                  var16.printStackTrace();
               }
            } catch (RemoteException var17) {
               if (DEBUG) {
                  var17.printStackTrace();
               }
            } finally {
               if (this.getRuntimeMBean() != null) {
                  this.getRuntimeMBean().migrationCompleted(var1, var2.getName(), var2.getName());
               }

            }

            try {
               Thread.sleep((long)(var5.getSecondsBetweenRestarts() * 1000));
            } catch (InterruptedException var15) {
            }
         }

         return false;
      }
   }

   public void timerExpired(Timer var1) {
      if (this.isActive()) {
         if (DEBUG) {
            this.p("Now checking lease statuses.");
         }

         HashSet var2 = new HashSet();
         this.checkRegisteredSingletons(var2);
         this.checkExpiredLeases(var2);
         this.checkFailedLeases(var2);
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            final String var4 = (String)var3.next();
            if (this.stateManager.checkServiceState(var4, 4)) {
               if (DEBUG) {
                  this.p(var4 + " - needs to be manually migrated. Cannot auto-migrate");
               }

               this.endMigration(var4);
            } else {
               MigrationDebugLogger.debug(var4 + " - Will attempt to auto-migrate.");
               WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
                  public void run() {
                     try {
                        if (!SingletonMonitor.this.isActive()) {
                           return;
                        }

                        Object var1 = SingletonMonitor.this.serviceTracker.get(var4);
                        Iterator var3;
                        ServerMBean var5;
                        if (var1 != null) {
                           SingletonDataObject var2 = (SingletonDataObject)var1;
                           if (var2.isAppScopedSingleton()) {
                              var3 = var2.getTargets().iterator();
                              boolean var4x = true;

                              while(true) {
                                 if (var3.hasNext()) {
                                    var5 = (ServerMBean)var3.next();

                                    try {
                                       RemoteSingletonServicesControl var6 = SingletonMonitor.this.getRemoteSingletonServicesControl(var5.getName());
                                       if (var6 == null) {
                                          continue;
                                       }

                                       if (var6.isServiceRegistered(var4)) {
                                          var4x = false;
                                       }
                                    } catch (Exception var25) {
                                       continue;
                                    }
                                 }

                                 if (var4x) {
                                    if (SingletonMonitor.DEBUG) {
                                       SingletonMonitor.this.p(var4 + " - belongs to app " + var2.getAppName() + " which is not active. Skipping its migration");
                                    }

                                    return;
                                 }
                                 break;
                              }
                           }
                        }

                        int var27 = 0;
                        var3 = null;
                        ServiceLocationSelector var29 = SingletonMonitor.this.getServiceLocationSelector(var4);
                        var5 = null;
                        String var30 = SingletonMonitor.this.findServiceLocation(var4);
                        if (var30 != null) {
                           var5 = SingletonMonitor.this.domain.lookupServer(var30);
                        }

                        ServerMBean var7 = null;
                        int var8 = -1;
                        SingletonServicesState var9 = SingletonMonitor.this.stateManager.getServiceState(var4);
                        if (var9 != null) {
                           var8 = var9.getState();
                           if (var8 == 0) {
                              var7 = SingletonMonitor.this.domain.lookupServer((String)var9.getStateData());
                           }
                        }

                        if (var7 == null) {
                           try {
                              var7 = SingletonMonitor.this.findPreviousServer(var4);
                           } catch (LeasingException var22) {
                           }
                        }

                        List var10 = null;
                        var10 = SingletonMonitor.this.getAllCandidateServers(var4);
                        if (var10 == null) {
                           var10 = SingletonMonitor.this.serviceTracker.getServerList(var4);
                        }

                        var29.setServerList(var10);
                        if (var7 != null) {
                           var29.setLastHost(var7);
                        }

                        if (SingletonMonitor.DEBUG) {
                           String var11 = var8 >= 0 ? SingletonServicesStateManager.STRINGIFIED_STATE[var8] : null;
                           SingletonMonitor.this.p(var4 + " - LastState: " + var11 + " LastLocation: " + var7);
                        }

                        if (!SingletonMonitor.this.restartInPlace(var4, var7, var8, var29)) {
                           do {
                              if (!SingletonMonitor.this.isActive()) {
                                 return;
                              }

                              ServerMBean var28 = var29.chooseServer();
                              if (var28 == null) {
                                 return;
                              }

                              if (SingletonMonitor.DEBUG) {
                                 SingletonMonitor.this.p(var4 + " - Will attempt to auto-migrate " + " from " + var5 + " to " + var28);
                              }

                              boolean var31 = false;

                              try {
                                 var31 = SingletonMonitor.this.migrate(var4, var5, var28, true);
                              } catch (Exception var24) {
                                 if (SingletonMonitor.DEBUG) {
                                    SingletonMonitor.this.p(var4 + " - Exception while migrating service it " + " from " + var5 + " to " + var28 + " : " + var24);
                                 }
                              }

                              if (var31) {
                                 var29.migrationSuccessful(var28, true);
                                 return;
                              }

                              try {
                                 String[] var12 = SingletonMonitor.this.manager.findExpiredLeases();
                                 List var13 = Arrays.asList(var12);
                                 if (!var13.contains(var4)) {
                                    String var14 = SingletonMonitor.this.manager.findOwner(var4);
                                    if (var14 != null) {
                                       if (SingletonMonitor.DEBUG) {
                                          SingletonMonitor.this.p(var4 + " - Its no more a bad lease. It also has a " + "owner " + var14 + ".No need to migrate it");
                                       }

                                       return;
                                    }
                                 }
                              } catch (LeasingException var23) {
                              }

                              ++var27;
                           } while(var27 != var10.size());

                           ClusterLogger.logNoSuitableServerFoundForSingletonService(var4);
                           return;
                        }

                        MigrationDebugLogger.debug(var4 + " - Restarted in place...");
                     } finally {
                        SingletonMonitor.this.endMigration(var4);
                     }

                  }
               });
            }
         }

         this.stateManager.syncState();
      }
   }

   private synchronized boolean isActive() {
      return !ManagementService.getRuntimeAccess(kernelId).getServerRuntime().isShuttingDown() && this.active;
   }

   public boolean migrate(String var1, String var2, boolean var3, boolean var4) throws RemoteException {
      return this.migrate(var1, var2);
   }

   public boolean migrateJTA(String var1, String var2, boolean var3, boolean var4) throws RemoteException {
      return this.migrate(var1, var2);
   }

   public boolean migrate(String var1, String var2) throws RemoteException {
      ServerMBean var3 = null;

      try {
         String var4 = this.findServiceLocation(var1);
         if (var4 != null) {
            var3 = this.domain.lookupServer(var4);
         }
      } catch (Exception var12) {
         throw new RemoteException("Unavailable to migrate, current location could not be determined because of an error: " + var12);
      }

      ServerMBean var14 = this.domain.lookupServer(var2);
      if (var14 == null) {
         throw new RemoteException("No server named " + var2 + " found.");
      } else {
         boolean var5 = false;

         boolean var15;
         try {
            if (!this.startMigration(var1, (long)(3 * this.leaseRenewInterval))) {
               if (DEBUG) {
                  this.p("Timed out while waiting for auto-migration of " + var1 + " to complete");
               }

               throw new RemoteException("Timed out while waiting for auto-migration of " + var1 + " to complete");
            }

            var5 = true;
            if (DEBUG) {
               this.p(var1 + " - Manually migrating from " + var3 + " to " + var14);
            }

            boolean var6 = this.migrate(var1, var3, var14, false);
            if (var6) {
               ServiceLocationSelector var7 = this.getServiceLocationSelector(var1);
               var7.migrationSuccessful(var14, false);
            }

            var15 = var6;
         } finally {
            if (var5) {
               this.endMigration(var1);
            }

         }

         return var15;
      }
   }

   private SingletonServiceMBean getSingletonServiceMBean(String var1) {
      return this.domain.lookupSingletonService(var1);
   }

   private MigratableTargetMBean getMigratableTarget(String var1) {
      MigratableTargetMBean var2 = null;
      var2 = this.domain.lookupMigratableTarget(var1);
      if (var2 != null) {
         return var2;
      } else {
         ServerMBean[] var3 = this.domain.getServers();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            JTAMigratableTargetMBean var5 = var3[var4].getJTAMigratableTarget();
            if (var5 != null && var5.getName().equals(var1)) {
               return var5;
            }
         }

         return null;
      }
   }

   public void deactivateJTA(String var1, String var2) throws RemoteException {
      try {
         if (!this.startMigration(var1, (long)(3 * this.leaseRenewInterval))) {
            if (DEBUG) {
               this.p(var1 + " - Timed out while waiting for JTA MT auto-migration " + "to complete");
            }

            throw new RemoteException("Timed out while waiting for JTA MT auto-migration of " + var1 + " to complete");
         }

         if (DEBUG) {
            this.p(var1 + " - Going to deactivate JTA MT with host " + var2);
         }

         String var3 = this.findServiceLocation(var1);
         if (var3 == null || var3.equals(var2)) {
            if (DEBUG) {
               this.p(var1 + " - Current location of JTA MT = " + var3 + ".No need to deactivate");
            }

            this.stateManager.removeServiceState(var1);
            return;
         }

         RemoteSingletonServicesControl var4 = this.getRemoteSingletonServicesControl(var3);
         if (var4 != null) {
            if (DEBUG) {
               this.p(var1 + " - Going to deactivate JTA MT on " + var3);
            }

            var4.deactivateService(var1);
            this.stateManager.removeServiceState(var1);
         } else {
            try {
               String var5 = this.manager.findOwner(var1);
               if (var5 != null) {
                  throw new RemoteException("Could not deactivate JTA service " + var1 + ", " + var3 + " could not be reached.");
               }
            } catch (LeasingException var10) {
            }
         }
      } finally {
         this.endMigration(var1);
      }

   }

   private boolean migrate(String var1, ServerMBean var2, ServerMBean var3, boolean var4) {
      if (var1 == null) {
         return true;
      } else if (this.serviceTracker.get(var1) == null && SingletonServicesManager.getInstance().getService(var1) == null) {
         if (DEBUG) {
            this.p(var1 + " - No such service is registered, despite there being a lease for it." + " Ignoring for now.");
         }

         return true;
      } else {
         RemoteSingletonServicesControl var5 = null;
         RemoteSingletonServicesControl var6 = null;

         boolean var8;
         try {
            if (this.getRuntimeMBean() != null) {
               this.getRuntimeMBean().migrationStarted(var1, var2 != null ? var2.getName() : "inactive", var3 != null ? var3.getName() : "no target");
            }

            if (var2 != null) {
               var5 = this.getRemoteSingletonServicesControl(var2.getName());
            }

            var6 = this.getRemoteSingletonServicesControl(var3.getName());
            if (var6 == null) {
               String var7 = var1 + " - Error talking to destination server - " + var3 + " . Can't migrate.";
               if (DEBUG) {
                  this.p(var7);
               }

               throw new MigrationException(var7);
            }

            this.getMigratableTarget(var1);
            if (var5 == null) {
               if (DEBUG) {
                  this.p(var1 + " - Error talking to source server, it may have crashed. " + "Continuing with its migration to " + var3);
               }
            } else {
               if (this.getMigratableTarget(var1) instanceof JTAMigratableTargetMBean && var2.getName().equals(var1)) {
                  ClusterLogger.logAttemptedJTAMigrationFromLivingServer(var2.getName());
                  var8 = true;
                  return var8;
               }

               try {
                  if (DEBUG) {
                     this.p(var1 + " - Attempting to deactivate it" + " on the source server - " + var2);
                  }

                  var5.deactivateService(var1);
               } catch (PeerGoneException var14) {
                  if (DEBUG) {
                     this.p(var1 + " - Error talking to source server - " + var2 + " . It may have crashed. Continuing with the migration.");
                  }
               }
            }

            if (DEBUG) {
               this.p(var1 + " - Attempting to activate the service" + " on the target server - " + var3);
            }

            var6.activateService(var1);
            if (DEBUG) {
               this.p(var1 + " - Service succesfully activated on remote server - " + var3.getName());
            }

            var8 = true;
         } catch (RemoteException var15) {
            if (DEBUG) {
               this.p("Error communicating to remote server, or no named service registered", var15);
            }

            throw new MigrationException("Exception - " + var15);
         } finally {
            if (this.getRuntimeMBean() != null) {
               this.getRuntimeMBean().migrationCompleted(var1, var2 != null ? var2.getName() : "inactive", var3 != null ? var3.getName() : "no target");
            }

         }

         return var8;
      }
   }

   private RemoteMigratableServiceCoordinator getRemoteMigratableServiceCoordinator() {
      String var1;
      try {
         var1 = URLManager.findAdministrationURL(this.domain.getAdminServerName());
         if (DEBUG) {
            this.p("Admin URL for looking up RemoteMigratableServiceCoordinator:" + var1);
         }
      } catch (UnknownHostException var6) {
         return null;
      }

      Environment var2 = new Environment();
      var2.setProviderUrl(var1);

      try {
         Context var3 = var2.getInitialContext();
         return (RemoteMigratableServiceCoordinator)var3.lookup("weblogic.cluster.migration.migratableServiceCoordinator");
      } catch (NamingException var5) {
         if (TxDebug.JTAMigration.isDebugEnabled()) {
            TxDebug.JTAMigration.debug("Unexpected exception while getting RemoteMigratableServiceCoordinator", var5);
         }

         return null;
      }
   }

   private ServerMBean findPreviousServer(String var1) throws LeasingException {
      if (DEBUG) {
         this.p(var1 + " - Finding its previous location");
      }

      String var2 = this.manager.findPreviousOwner(var1);
      if (var2 == null) {
         if (DEBUG) {
            this.p(var1 + " - Couldn't find its current or previous location");
         }

         return null;
      } else {
         if (DEBUG) {
            this.p(var1 + " - Its previous location is: " + LeaseManager.getServerNameFromOwnerIdentity(var2));
         }

         return this.domain.lookupServer(LeaseManager.getServerNameFromOwnerIdentity(var2));
      }
   }

   private boolean isServerRunning(String var1) {
      try {
         if (DEBUG) {
            this.p("Checking if " + var1 + " is running.");
         }

         ClusterMasterRemote var2 = MigratableServerService.theOne().getClusterMasterRemote();
         if (var2 == null) {
            return true;
         }

         String var3 = var2.getServerLocation(var1);
         if (var3 == null) {
            return false;
         }

         DomainMBean var4 = ManagementService.getRuntimeAccess(kernelId).getDomain();
         MachineMBean var5 = var4.lookupMachine(var3);
         ServerMBean var6 = var4.lookupServer(var1);
         NodeManagerRuntime var7 = NodeManagerRuntime.getInstance(var5);
         String var8 = var7.getState(var6);
         if (DEBUG) {
            this.p("Current state of " + var1 + " is: " + var8);
         }

         if (var8 != null && !var8.equals("STARTING") && !var8.equals("RUNNING") && !var8.equals("STANDBY") && !var8.equals("ADMIN") && !var8.equals("RESUMING") && !var8.equals("UNKNOWN")) {
            if (DEBUG) {
               this.p("We consider that state NON-RUNNING.");
            }

            return false;
         }
      } catch (IOException var9) {
         if (DEBUG) {
            this.p("Error while talking to NM, considering server down.", var9);
         }

         return false;
      } catch (LeasingException var10) {
         if (DEBUG) {
            this.p("Error while talking to remote ClusterMaster, considering server down.", var10);
         }

         return false;
      }

      if (DEBUG) {
         this.p("We consider that state RUNNING.");
      }

      return true;
   }

   private RemoteSingletonServicesControl getRemoteSingletonServicesControl(String var1) {
      Environment var2 = new Environment();
      Object var3 = null;
      String var4 = null;
      if (!this.isServerRunning(var1)) {
         return null;
      } else {
         try {
            var4 = URLManager.findAdministrationURL(var1);
         } catch (UnknownHostException var18) {
            var4 = MigratableServerService.findURLOfUnconnectedServer(var1);
         }

         Object var6;
         try {
            if (DEBUG) {
               this.p("Contacting " + var1 + " at " + var4 + " to perform migration tasks.");
            }

            RemoteSingletonServicesControl var5;
            if (var4 != null) {
               var2.setProviderUrl(var4);
               var2.setRequestTimeout(10000L);
               var5 = (RemoteSingletonServicesControl)PortableRemoteObject.narrow(var2.getInitialReference(SingletonServicesManager.class), RemoteSingletonServicesControl.class);
               return var5;
            }

            var5 = null;
            return var5;
         } catch (NamingException var19) {
            if (DEBUG) {
               this.p("Could not find RemoteSingletonServicesControl on " + var1, var19);
            }

            var6 = null;
         } finally {
            if (var3 != null) {
               try {
                  ((Context)var3).close();
               } catch (NamingException var17) {
               }
            }

         }

         return (RemoteSingletonServicesControl)var6;
      }
   }

   private List getAllCandidateServers(String var1) {
      List var2 = null;
      ServerMBean[] var3 = null;
      MigratableTargetMBean var4 = this.getMigratableTarget(var1);
      if (var4 != null) {
         var3 = var4.getAllCandidateServers();
      } else {
         SingletonServiceMBean var5 = this.getSingletonServiceMBean(var1);
         if (var5 != null) {
            var3 = var5.getAllCandidateServers();
         }
      }

      if (var3 != null && var3.length > 0) {
         var2 = Arrays.asList(var3);
      }

      return var2;
   }

   private ServerMBean getUserPreferedServer(String var1) {
      ServerMBean var2 = null;
      MigratableTargetMBean var3 = this.getMigratableTarget(var1);
      if (var3 != null) {
         var2 = var3.getUserPreferredServer();
      }

      return var2;
   }

   private void p(Object var1) {
      SingletonServicesDebugLogger.debug("SingletonMonitor: " + var1);
   }

   private void p(Object var1, Exception var2) {
      SingletonServicesDebugLogger.debug("SingletonMonitor: " + var1, var2);
   }

   public ServiceLocationSelector getServiceLocationSelector(String var1) {
      MigratableTargetMBean var2 = this.getMigratableTarget(var1);
      if (var2 != null) {
         String var3 = var2.getMigrationPolicy();
         if (var3.equals("exactly-once")) {
            return new ExactlyOnceServiceLocationSelector(var2, this.stateManager);
         }

         if (var3.equals("failure-recovery")) {
            return new FailureRecoveryServiceLocationSelector(var2, this.stateManager);
         }
      }

      BasicServiceLocationSelector var5 = new BasicServiceLocationSelector(var1, this.stateManager);
      SingletonServiceMBean var4 = this.getSingletonServiceMBean(var1);
      if (var4 != null) {
         var5.setUPS(var4.getUserPreferredServer());
      }

      return var5;
   }

   public SingletonServicesStateManager getSingletonServicesStateManager() {
      return this.stateManager;
   }

   public void memberAdded(String var1) {
   }

   public void memberRemoved(String var1) {
      Iterator var2 = this.serviceTracker.values().iterator();

      while(var2.hasNext()) {
         SingletonDataObject var3 = (SingletonDataObject)var2.next();
         String var4 = var3.getName();

         try {
            String var5 = this.manager.findOwner(var4);
            if (var5 != null) {
               String var6 = LeaseManager.getServerNameFromOwnerIdentity(var5);
               if (var6.equals(var1)) {
                  String var7 = AbstractConsensusService.getInstance().getServerState(var1);
                  if (canMigrateLease(var7)) {
                     if (DEBUG) {
                        this.p(var1 + " is marked as " + var7 + ". Voiding all its leases");
                     }

                     this.manager.voidLeases(var5);
                     break;
                  }
               }
            }
         } catch (LeasingException var8) {
         }
      }

   }

   public static boolean canMigrateLease(String var0) {
      boolean var1 = false;
      if (var0 != null && (var0.equals("FAILED_NOT_RESTARTABLE") || var0.equals("FAILED_RESTARTING") || var0.equals("FAILED") || var0.equals("ACTIVATE_LATER") || var0.equals("STARTING"))) {
         var1 = true;
      }

      return var1;
   }

   private boolean startMigration(String var1, long var2) {
      synchronized(this.currentlyRunningMigrations) {
         long var5 = System.currentTimeMillis();

         while(this.currentlyRunningMigrations.contains(var1)) {
            if (var2 < 0L || var5 + var2 < System.currentTimeMillis()) {
               return false;
            }

            try {
               if (DEBUG) {
                  this.p("Going to wait for " + var2 + " ms. to get lock for " + var1);
               }

               this.currentlyRunningMigrations.wait(var2);
            } catch (Exception var9) {
            }
         }

         this.currentlyRunningMigrations.add(var1);
         return true;
      }
   }

   private boolean endMigration(String var1) {
      synchronized(this.currentlyRunningMigrations) {
         this.currentlyRunningMigrations.notify();
         return this.currentlyRunningMigrations.remove(var1);
      }
   }

   protected MemberDeathDetector initializeMemberDeathDetector() {
      MemberDeathDetector var1 = null;

      try {
         var1 = getMemberDeathDetector();
         var1.start();
      } catch (ServiceFailureException var3) {
      }

      return var1;
   }

   public void notifyShutdown(String var1) {
      if (this.memberDeathDetector != null) {
         this.memberDeathDetector.removeMember(var1);
      }

   }

   private static MemberDeathDetector getMemberDeathDetector() throws ServiceFailureException {
      try {
         Class var0 = getMemberDeathDetectorClass();
         Method var1 = var0.getMethod("getInstance");
         var1.setAccessible(true);
         return (MemberDeathDetector)var1.invoke((Object)null);
      } catch (ClassNotFoundException var2) {
         var2.printStackTrace();
         throw new ServiceFailureException("Unable to find class: weblogic.cluster.messaging.internal.MemberDeathDetectorImpl");
      } catch (NoSuchMethodException var3) {
         var3.printStackTrace();
         throw new ServiceFailureException("No such method: weblogic.cluster.messaging.internal.MemberDeathDetectorImpl.getInstance()");
      } catch (InvocationTargetException var4) {
         var4.printStackTrace();
         throw new ServiceFailureException(var4.getCause());
      } catch (IllegalAccessException var5) {
         var5.printStackTrace();
         throw new ServiceFailureException(var5.getCause());
      }
   }

   private static Class getMemberDeathDetectorClass() throws ClassNotFoundException {
      Class var0 = Class.forName("weblogic.cluster.messaging.internal.MemberDeathDetectorImpl");
      return var0;
   }
}
