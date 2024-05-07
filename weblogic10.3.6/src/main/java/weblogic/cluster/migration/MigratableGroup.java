package weblogic.cluster.migration;

import java.io.Serializable;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import weblogic.cluster.ClusterExtensionLogger;
import weblogic.cluster.ClusterLogger;
import weblogic.cluster.ClusterMemberInfo;
import weblogic.cluster.ClusterMembersChangeEvent;
import weblogic.cluster.ClusterMembersChangeListener;
import weblogic.cluster.ClusterService;
import weblogic.cluster.singleton.LeaseLostListener;
import weblogic.cluster.singleton.LeaseManager;
import weblogic.cluster.singleton.LeaseObtainedListener;
import weblogic.cluster.singleton.LeasingException;
import weblogic.cluster.singleton.SingletonService;
import weblogic.cluster.singleton.SingletonServicesDebugLogger;
import weblogic.cluster.singleton.SingletonServicesManager;
import weblogic.cluster.singleton.SingletonServicesState;
import weblogic.cluster.singleton.SingletonServicesStateManagerRemote;
import weblogic.health.HealthFeedbackCallback;
import weblogic.health.HealthMonitorService;
import weblogic.health.HealthState;
import weblogic.jndi.Environment;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.JTAMigratableTargetMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.protocol.LocalServerIdentity;
import weblogic.rmi.spi.HostID;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.work.WorkManagerFactory;

public class MigratableGroup implements ClusterMembersChangeListener, SingletonService, LeaseLostListener, LeaseObtainedListener, HealthFeedbackCallback {
   private static final boolean DEBUG = SingletonServicesDebugLogger.isDebugEnabled();
   private static final String HEALTH_JTA_SUBSYSTEM_NAME = "JTA";
   private final MigratableTargetMBean target;
   private final Map migratablesToJNDINameMap;
   private final TreeSet migratablesAsList = new TreeSet(new MigratableComparator());
   private HostID[] cachedHostList = new HostID[0];
   private boolean isActive;
   private int migratableState;
   private final Context ctx;
   Object activationLock = new Object();
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public MigratableGroup(MigratableTargetMBean var1) {
      this.target = var1;
      this.migratablesToJNDINameMap = Collections.synchronizedMap(new HashMap());
      this.isActive = false;
      this.migratableState = 0;
      this.updateHostList();
      if (!var1.getMigrationPolicy().equals("manual")) {
         SingletonServicesManager.getInstance().addConfiguredService(this.getName(), this);
      }

      try {
         this.ctx = new InitialContext();
      } catch (NamingException var3) {
         throw new AssertionError("Error creating initial context", var3);
      }
   }

   public MigratableTargetMBean getTarget() {
      return this.target;
   }

   boolean isActive() {
      return this.isActive;
   }

   public int getMigratableState() {
      return this.migratableState;
   }

   void failed() {
      if (DEBUG) {
         this.p("Failed " + this.getName());
      }

      WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
         public void run() {
            synchronized(MigratableGroup.this.activationLock) {
               if (MigratableGroup.this.isActive) {
                  try {
                     MigratableGroup.this.deactivateAllMigratables();
                  } finally {
                     boolean var4 = true;

                     try {
                        var4 = MigratableGroup.this.executePostScript();
                     } finally {
                        if (!MigratableGroup.this.target.getMigrationPolicy().equals("manual")) {
                           if (var4) {
                              try {
                                 MigratableGroup.this.updateState(0, ManagementService.getRuntimeAccess(MigratableGroup.kernelId).getServer().getName());
                              } catch (Exception var40) {
                                 ClusterLogger.logLeasingError(MigratableGroup.this.target.getName(), var40);
                              }
                           }

                           try {
                              ClusterService.getServices().getDefaultLeaseManager("service").release(MigratableGroup.this.target.getName());
                           } catch (LeasingException var39) {
                              ClusterExtensionLogger.logReleaseLeaseError(MigratableGroup.this.target.getName(), var39);
                           }
                        }

                     }
                  }

               }
            }
         }
      });
   }

   public void healthStateChange(HealthState var1) {
      if (var1 != null) {
         if (var1.getSubsystemName() != null) {
            if (var1.getState() == 3 || var1.getState() == 2) {
               Iterator var2 = this.migratablesAsList.iterator();

               while(var2.hasNext()) {
                  String var3 = ((Migratable)var2.next()).getName();
                  if (var3 != null) {
                     if (var1.getSubsystemName().equals(var3)) {
                        this.failed();
                     } else if (var1.getSubsystemName().startsWith("PersistentStore.")) {
                        String var4 = var1.getSubsystemName().substring("PersistentStore.".length(), var1.getSubsystemName().length());
                        if (var3.equals(var4)) {
                           this.failed();
                        }
                     }
                  }
               }

            }
         }
      }
   }

   public boolean add(Migratable var1, String var2) throws MigrationException {
      synchronized(this.activationLock) {
         if (this.migratablesAsList.contains(var1)) {
            return false;
         } else {
            try {
               if (var2 != null) {
                  this.ctx.bind(var2, var1);
               }
            } catch (NamingException var6) {
            }

            if (this.isActive) {
               var1.migratableActivate();
            }

            if (var2 != null) {
               this.migratablesToJNDINameMap.put(var1, var2);
            }

            if (DEBUG) {
               this.p("adding migratable " + var1.getName() + " to group " + this.getName() + " Migratable class - " + var1.getClass().getName());
            }

            return this.migratablesAsList.add(var1);
         }
      }
   }

   public boolean remove(Migratable var1) throws MigrationException {
      synchronized(this.activationLock) {
         if (this.isActive) {
            var1.migratableDeactivate();
         }

         this.migratablesToJNDINameMap.remove(var1);
         return this.migratablesAsList.remove(var1);
      }
   }

   String clearUpJNDIMap(Migratable var1) {
      return (String)this.migratablesToJNDINameMap.remove(var1);
   }

   int size() {
      return this.migratablesAsList.size();
   }

   public String getName() {
      return this.getTarget().getName();
   }

   public void onAcquire(String var1) {
      LeaseManager var2 = ClusterService.getServices().getDefaultLeaseManager("service");
      var2.addLeaseLostListener(this);
   }

   public void onException(Exception var1, String var2) {
   }

   public void restart() throws MigrationException {
      this.deactivate();
      this.activate();
   }

   public void activate() {
      synchronized(this.activationLock) {
         if (!this.isActive) {
            if (!this.target.getMigrationPolicy().equals("manual")) {
               LeaseManager var2 = ClusterService.getServices().getDefaultLeaseManager("service");
               if (var2 == null) {
                  if (DEBUG) {
                     this.p("No lease manager, cannot start auto-migratable service.");
                  }

                  throw new MigrationException("No lease manager, cannot start auto-migratable service.");
               }

               try {
                  if (!var2.tryAcquire(this.target.getName())) {
                     if (DEBUG) {
                        this.p("Could not claim lease for " + this.getName() + ", someone else must have it already.");
                     }

                     if (!this.permitActivateWithoutLease(this.target)) {
                        throw new MigrationException("Could not claim lease, someone else must have it already.");
                     }

                     try {
                        var2.acquire(this.target.getName(), this);
                     } catch (LeasingException var5) {
                        throw new MigrationException(var5);
                     }
                  } else {
                     var2.addLeaseLostListener(this);
                  }
               } catch (LeasingException var6) {
                  ClusterLogger.logLeasingError(this.target.getName(), var6);
                  if (DEBUG) {
                     this.p("Could not claim lease due to error: " + var6);
                  }

                  throw new MigrationException("Could not claim lease due to error: " + var6, var6);
               }
            }

            if (!ScriptExecutor.runNMScript(this.target.getPreScript(), this.target)) {
               if (DEBUG) {
                  this.p("couldn't run prescript");
               }

               throw new MigrationException("couldn't run prescript", true);
            } else {
               this.updateHostList();
               this.activateAllMigratables();
               if (!this.target.getMigrationPolicy().equals("manual")) {
                  HealthMonitorService.registerForCallback(this);
               }

            }
         }
      }
   }

   private void activateAllMigratables() {
      this.migratableState = 2;
      synchronized(this.migratablesAsList) {
         if (DEBUG) {
            this.p("activating " + this.migratablesAsList.size() + " migratables" + " for " + this.getName());
         }

         Iterator var2 = this.migratablesAsList.iterator();
         GroupActivationException var3 = new GroupActivationException();
         ArrayList var4 = new ArrayList();

         while(var2.hasNext()) {
            Migratable var5 = (Migratable)var2.next();
            if (DEBUG) {
               this.p("activating migratable '" + var5.getName() + "' for " + this.getName());
            }

            this.activateTarget(var5, var3);
            if (var3.getCauses().size() > 0) {
               this.handleFailedStateChange(var4, true);
               throw var3;
            }

            var4.add(var5);
         }

         this.isActive = true;
         this.migratableState = 1;
      }
   }

   private void handleFailedStateChange(List var1, boolean var2) {
      for(int var3 = var1.size() - 1; var3 >= 0; --var3) {
         Migratable var4 = (Migratable)var1.get(var3);

         try {
            if (var2) {
               if (DEBUG) {
                  this.p("Going to call migratableDeactivate on " + var4 + " for " + this.getName());
               }

               var4.migratableDeactivate();
            } else {
               if (DEBUG) {
                  this.p("Going to call migratableActivate on " + var4 + " for " + this.getName());
               }

               this.activateTarget(var4, new GroupActivationException());
            }
         } catch (MigrationException var6) {
         }
      }

   }

   private void activateTarget(Migratable var1, GroupActivationException var2) throws MigrationException {
      try {
         var1.migratableActivate();
         String var3 = (String)this.migratablesToJNDINameMap.get(var1);
         if (var3 != null) {
            this.ctx.rebind(var3, var1);
         }
      } catch (NamingException var4) {
         throw new MigrationException("Failed to migrate", var4);
      } catch (MigrationException var5) {
         if (DEBUG) {
            this.p("Failed to activate " + var1.getName() + " - " + var5.getCause());
         }

         var2.addCause(var5);
      }

   }

   void shutdown() {
      this.deactivate(true);
   }

   private void deactivateAllMigratables() {
      synchronized(this.migratablesAsList) {
         if (DEBUG) {
            this.p("deactivating " + this.migratablesAsList.size() + " migratables" + " for " + this.getName());
         }

         Migratable[] var2 = new Migratable[this.migratablesAsList.size()];
         this.migratablesAsList.toArray(var2);
         GroupDeactivationException var3 = null;

         for(int var4 = var2.length - 1; var4 >= 0; --var4) {
            Migratable var5 = var2[var4];

            try {
               if (DEBUG) {
                  this.p("Going to call migratableDeactivate on " + var5.getName() + " for " + this.getName());
               }

               var5.migratableDeactivate();
            } catch (MigrationException var8) {
               if (var3 == null) {
                  var3 = new GroupDeactivationException();
               }

               if (DEBUG) {
                  this.p("Failed to deactivate " + var5.getName() + " - " + var8.getCause());
               }

               var3.addCause(var8);
            }
         }

         this.isActive = false;
         this.migratableState = 0;
         if (var3 != null) {
            throw var3;
         }
      }
   }

   public void deactivate() {
      if (DEBUG) {
         this.p("deactivating " + this.getName());
      }

      this.deactivate(false);
   }

   private void deactivate(boolean var1) {
      synchronized(this.activationLock) {
         if (this.isActive) {
            this.updateHostList();
            if (!this.target.getMigrationPolicy().equals("manual")) {
               HealthMonitorService.deregisterForCallback(this);
            }

            try {
               this.deactivateAllMigratables();
            } finally {
               try {
                  if (!this.executePostScript() && !var1) {
                     throw new MigrationException("Execution of post deactivation script failed");
                  }
               } finally {
                  if (!this.target.getMigrationPolicy().equals("manual")) {
                     this.releaseLease();
                  }

               }

            }

         }
      }
   }

   private void releaseLease() {
      try {
         ClusterService.getServices().getDefaultLeaseManager("service").release(this.target.getName());
      } catch (LeasingException var2) {
         ClusterExtensionLogger.logReleaseLeaseError(this.target.getName(), var2);
      }

   }

   public void onRelease() {
      LeaseManager var1 = ClusterService.getServices().getDefaultLeaseManager("service");
      var1.removeLeaseLostListener(this);
      ClusterMBean var2 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster();
      if (this.isActive && this.isLocalJTAMigratableTarget() && isLocalJTAAutoMigratable()) {
         HealthMonitorService.subsystemFailedForceShutdown("JTAMigratableGroup", "JTA migratable target lost a lease");
      } else {
         this.deactivate();
      }

   }

   public int hashCode() {
      return this.target.getName().hashCode();
   }

   public boolean equals(Object var1) {
      return var1 instanceof MigratableGroup && ((MigratableGroup)var1).target.getName().equals(this.target.getName());
   }

   public String toString() {
      return "MigratableGroup " + this.target.getName() + " with " + this.migratablesToJNDINameMap.toString();
   }

   public HostID[] getHostList() {
      this.updateHostList();
      return this.cachedHostList;
   }

   public void clusterMembersChanged(ClusterMembersChangeEvent var1) {
      this.updateHostList();
   }

   private void updateHostList() {
      try {
         RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
         Object var2 = var1.getDomain().lookupMigratableTarget(this.target.getName());
         if (var2 == null) {
            ServerMBean var3 = var1.getDomain().lookupServer(this.target.getName());
            var2 = var3.getJTAMigratableTarget();
         }

         Debug.assertion(var2 != null, "Could find neither MigratableTarget nor JTAMigratableTarget " + this.target.getName());
         ServerMBean[] var11 = ((MigratableTargetMBean)var2).getAllCandidateServers();
         Collection var4 = ClusterService.getServices().getRemoteMembers();
         ArrayList var5 = new ArrayList();

         for(int var6 = 0; var6 < var11.length; ++var6) {
            String var7 = var11[var6].getName();
            if (ManagementService.getRuntimeAccess(kernelId).getServerName().equals(var7)) {
               var5.add(LocalServerIdentity.getIdentity());
            } else {
               Iterator var8 = var4.iterator();

               while(var8.hasNext()) {
                  ClusterMemberInfo var9 = (ClusterMemberInfo)var8.next();
                  if (var9.serverName().equals(var7)) {
                     var5.add(var9.identity());
                     break;
                  }
               }
            }
         }

         this.cachedHostList = (HostID[])((HostID[])var5.toArray(this.cachedHostList));
      } catch (Exception var10) {
      }

   }

   private boolean permitActivateWithoutLease(MigratableTargetMBean var1) {
      if (!this.isJTAMigratableGroup()) {
         return false;
      } else {
         ClusterMBean var2 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster();
         return var2 != null && "consensus".equals(var2.getMigrationBasis()) && isAutoServiceMigrationEnabled(var2);
      }
   }

   private static boolean isAutoServiceMigrationEnabled(ClusterMBean var0) {
      MigratableTargetMBean[] var1 = var0.getMigratableTargets();
      if (var1 == null) {
         return false;
      } else {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (!"manual".equals(var1[var2].getMigrationPolicy())) {
               return true;
            }
         }

         return false;
      }
   }

   private boolean isJTAMigratableGroup() {
      return this.target instanceof JTAMigratableTargetMBean;
   }

   private boolean isLocalJTAMigratableTarget() {
      if (!this.isJTAMigratableGroup()) {
         return false;
      } else {
         JTAMigratableTargetMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer().getJTAMigratableTarget();
         return var1 == null ? false : var1.getName().equals(this.target.getName());
      }
   }

   private static boolean isLocalJTAAutoMigratable() {
      ServerMBean var0 = ManagementService.getRuntimeAccess(kernelId).getServer();
      return var0.getJTAMigratableTarget() != null && "failure-recovery".equals(var0.getJTAMigratableTarget().getMigrationPolicy());
   }

   private void p(Object var1) {
      SingletonServicesDebugLogger.debug("MigratableGroup: " + var1.toString());
   }

   private boolean executePostScript() {
      String var1 = this.target.getPostScript();
      if (var1 == null) {
         return true;
      } else {
         boolean var2 = !this.target.getMigrationPolicy().equals("manual");
         if (!ScriptExecutor.runNMScript(var1, this.target) && this.target.isPostScriptFailureFatal()) {
            this.logPostScriptExecutionFailed();
            if (var2) {
               try {
                  this.updateState(4, (Serializable)null);
               } catch (Exception var4) {
                  ClusterExtensionLogger.logFailedToNotifyPostScriptFailureToStateManager(this.getName(), var1, var4);
               }
            }

            return false;
         } else {
            return true;
         }
      }
   }

   private static SingletonServicesStateManagerRemote getLocalSingletonServicesStateManager() {
      SingletonServicesStateManagerRemote var0 = null;

      try {
         Environment var1 = new Environment();
         Context var2 = var1.getInitialContext();
         var0 = (SingletonServicesStateManagerRemote)var2.lookup("weblogic.cluster.singleton.SingletonServicesStateManager");
      } catch (NamingException var3) {
      }

      return var0;
   }

   public void handlePriorityShutDownTasks() {
      if (this.isActive && this.target.isManualActiveOn(ManagementService.getRuntimeAccess(kernelId).getServer()) && this.target.getMigrationPolicy().equals("failure-recovery")) {
         if (this.isJTAMigratableGroup() && this.isJTAHealthFailed()) {
            if (DEBUG) {
               this.p("Not marking the state of jta migratable group " + this.getName() + " as shutdown because it is failed");
            }

            return;
         }

         try {
            this.updateState(3, (Serializable)null);
         } catch (Exception var2) {
            ClusterLogger.logLeasingError(this.target.getName(), var2);
         }
      }

   }

   private boolean isJTAHealthFailed() {
      HealthState[] var1 = HealthMonitorService.getHealthStates();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         HealthState var3 = var1[var2];
         if (var3.getSubsystemName().equals("JTA")) {
            if (var3.getState() == 3) {
               return true;
            }

            return false;
         }
      }

      return false;
   }

   private void updateState(int var1, Serializable var2) throws Exception {
      SingletonServicesStateManagerRemote var3 = getLocalSingletonServicesStateManager();
      if (var3 != null) {
         SingletonServicesState var4 = new SingletonServicesState(var1);
         var4.setStateData(var2);
         HashMap var5 = new HashMap();
         var5.put("Sender", ManagementService.getRuntimeAccess(kernelId).getServerName());
         var5.put("SvcName", this.getName());
         var5.put("SvcState", var4);
         Boolean var6 = (Boolean)var3.invoke(1001, var5);
         if (var6) {
            if (DEBUG) {
               this.p("Updated state of " + this.getName() + " to " + var4);
            }

         } else {
            throw new Exception("Failed to update state of " + this.getName() + " to " + var4);
         }
      } else {
         throw new Exception("Failed to lookup local state manager to update state of " + this.getName());
      }
   }

   private void logPostScriptExecutionFailed() {
      ClusterExtensionLogger.logPostDeactivationScriptFailure(this.target.getPostScript(), this.target.getName());
   }

   private static class MigratableComparator implements Comparator {
      private MigratableComparator() {
      }

      public int compare(Object var1, Object var2) {
         Migratable var3 = (Migratable)var1;
         Migratable var4 = (Migratable)var2;
         if (var3.getOrder() < var4.getOrder()) {
            return -1;
         } else {
            return var3.getOrder() > var4.getOrder() ? 1 : var3.toString().compareTo(var4.toString());
         }
      }

      public boolean equals(Object var1) {
         return var1 instanceof MigratableComparator;
      }

      // $FF: synthetic method
      MigratableComparator(Object var1) {
         this();
      }
   }
}
