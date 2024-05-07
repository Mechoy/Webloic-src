package weblogic.cluster.migration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.naming.NamingException;
import weblogic.cluster.ClusterExtensionLogger;
import weblogic.cluster.ClusterService;
import weblogic.cluster.singleton.MemberDeathDetectorHeartbeatReceiverIntf;
import weblogic.cluster.singleton.SingletonServicesManager;
import weblogic.jndi.Environment;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.JTAMigratableTargetMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.rmi.spi.HostID;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.ServiceFailureException;
import weblogic.utils.Debug;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.utils.collections.FilteringIterator;
import weblogic.utils.collections.Iterators;

public final class MigrationManager implements RemoteMigrationControl {
   private static MigrationManager singleton;
   private static final boolean GRACEFUL_SHUTDOWN = true;
   private Map groups;
   private Map migratableToGroup;
   private Map nameToGroup;
   private int state = 0;
   private Map activeTargets;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   public static long TIMEOUT = 300000L;
   private boolean migrating = false;
   private long timestamp = 0L;
   private boolean memberDeathDetectorHeartbeatReceiverEnabled = false;

   public static MigrationManager singleton() {
      return singleton;
   }

   public MigrationManager() {
      Debug.assertion(singleton == null);
      this.groups = new ConcurrentHashMap();
      this.migratableToGroup = new ConcurrentHashMap();
      this.nameToGroup = new ConcurrentHashMap();
      this.activeTargets = new ConcurrentHashMap();
      singleton = this;
   }

   void initialize() {
      ServerMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer();
      ClusterMBean var2 = var1.getCluster();
      if (var2 != null) {
         Environment var3 = new Environment();
         var3.setReplicateBindings(false);
         var3.setCreateIntermediateContexts(true);

         try {
            var3.getInitialContext().bind("weblogic.cluster.migrationControl", singleton);
         } catch (NamingException var5) {
            throw new AssertionError("Unexpected exception: " + var5);
         }
      }

      this.state = 3;
   }

   void start() throws MigrationException {
      this.activateAllTargets();
      if (ClusterService.getClusterService().isMemberDeathDetectorEnabled()) {
         ClusterExtensionLogger.logStartingMemberDeathDetectorReceiver();

         try {
            invokeStaticMethodOnMemberDeathDetectorHeartbeatReceiver("enableHeartbeatReceiver", new Class[0], new Object[0]);
            this.memberDeathDetectorHeartbeatReceiverEnabled = true;
         } catch (ServiceFailureException var2) {
            System.out.println("Member Death Detector Heartbeat Receiver failed to start. " + var2);
         }
      }

      this.state = 2;
   }

   void halt() throws MigrationException {
      this.deactivateAllTargets(false);
      this.state = 3;
   }

   void stop() throws MigrationException {
      this.deactivateAllTargets(true);
      if (this.memberDeathDetectorHeartbeatReceiverEnabled) {
         try {
            MemberDeathDetectorHeartbeatReceiverIntf var1 = this.getMemberDeathDetectorHeartbeatReceiver();
            var1.stop();
            this.memberDeathDetectorHeartbeatReceiverEnabled = false;
         } catch (ServiceFailureException var2) {
            throw new MigrationException(var2);
         }
      }

      this.state = 3;
   }

   public synchronized void setMigrating(boolean var1) {
      this.migrating = var1;
      if (this.migrating) {
         this.timestamp = System.currentTimeMillis();
      }

   }

   public synchronized boolean isMigrating() {
      if (System.currentTimeMillis() - this.timestamp > TIMEOUT) {
         this.setMigrating(false);
      }

      return this.migrating;
   }

   public MigratableGroup findGroup(String var1) {
      return (MigratableGroup)this.groups.get(var1);
   }

   public void register(Migratable var1, MigratableTargetMBean var2) throws MigrationException {
      this.privateRegister(var1, (String)null, var2);
   }

   public void register(MigratableRemote var1, String var2, MigratableTargetMBean var3) throws MigrationException {
      this.privateRegister(var1, var2, var3);
      this.nameToGroup.put(var2, this.migratableToGroup.get(var1));
   }

   private synchronized void privateRegister(Migratable var1, String var2, MigratableTargetMBean var3) throws MigrationException {
      Debug.assertion(var3.getUserPreferredServer() != null);
      String var4 = var3.getName();
      MigratableGroup var5 = null;
      if (var3.isCandidate(ManagementService.getRuntimeAccess(kernelId).getServer())) {
         var1.migratableInitialize();
      }

      synchronized(this) {
         var5 = (MigratableGroup)this.groups.get(var4);
         if (var5 == null) {
            var5 = new MigratableGroup(var3);
            this.groups.put(var4, var5);
         }
      }

      if (var5.add(var1, var2)) {
         this.migratableToGroup.put(var1, var5);
      }

      boolean var6 = var3.getMigrationPolicy().equals("manual");
      boolean var7 = var3 instanceof JTAMigratableTargetMBean;
      if ((var6 || var7) && var3.isManualActiveOn(ManagementService.getRuntimeAccess(kernelId).getServer()) && !var5.isActive()) {
         try {
            var5.activate();
            this.activeTargets.put(var4, var4);
            if (var7) {
               SingletonServicesManager.getInstance().addActiveService(var4);
            }
         } catch (MigrationException var9) {
            if (var3.getMigrationPolicy() != null && var3.getMigrationPolicy().equals("manual")) {
               throw var9;
            }

            if (var9.isFatal()) {
               throw var9;
            }
         }
      }

   }

   public void unregister(Migratable var1, MigratableTargetMBean var2) throws MigrationException {
      MigratableGroup var3 = (MigratableGroup)this.groups.get(var2.getName());
      Debug.assertion(var3 != null);
      synchronized(this) {
         if (var3.remove(var1)) {
            String var5 = var3.clearUpJNDIMap(var1);
            this.migratableToGroup.remove(var1);
            if (var5 != null) {
               this.nameToGroup.remove(var5);
            }

            this.activeTargets.remove(var2.getName());
         }

      }
   }

   public HostID[] getMigratableHostList(String var1) {
      MigratableGroup var2 = (MigratableGroup)this.nameToGroup.get(var1);
      return var2 != null ? var2.getHostList() : null;
   }

   /** @deprecated */
   public String[] getMigratableGroupServerList(Migratable var1) {
      return null;
   }

   public int getMigratableState(Migratable var1) {
      MigratableGroup var2 = (MigratableGroup)this.migratableToGroup.get(var1);
      return var2 != null ? var2.getMigratableState() : 0;
   }

   public int getMigratableState(String var1) {
      MigratableGroup var2 = (MigratableGroup)this.groups.get(var1);
      if (var2 != null) {
         return var2.getMigratableState();
      } else {
         return this.activeTargets.get(var1) != null ? 1 : 0;
      }
   }

   public void restartTarget(String var1) throws MigrationException {
      MigratableGroup var2 = (MigratableGroup)this.groups.get(var1);
      if (var2 != null) {
         var2.restart();
      } else {
         throw new MigrationException("Could not find a migratable target   named " + var1);
      }
   }

   public void activateTarget(String var1) throws MigrationException {
      MigratableGroup var2 = (MigratableGroup)this.groups.get(var1);
      if (var2 != null) {
         var2.activate();
      }

      this.activeTargets.put(var1, var1);
   }

   public void deactivateTarget(String var1, String var2) throws MigrationException {
      MigratableGroup var3 = (MigratableGroup)this.groups.get(var1);
      if (var3 != null) {
         var3.deactivate();
      }

      this.activeTargets.remove(var1);
   }

   public Collection activatedTargets() {
      FilteringIterator var1 = new FilteringIterator(this.groups.entrySet().iterator()) {
         protected boolean accept(Object var1) {
            MigratableGroup var2 = (MigratableGroup)var1;
            return var2.getMigratableState() == 2 || var2.getMigratableState() == 1;
         }
      };
      ArrayList var2 = new ArrayList();
      Iterators.addAll(var2, var1);
      return var2;
   }

   private void activateAllTargets() throws MigrationException {
      Iterator var1 = this.groups.entrySet().iterator();
      MigratableGroup var2 = null;

      while(var1.hasNext()) {
         var2 = (MigratableGroup)((Map.Entry)var1.next()).getValue();
         MigratableTargetMBean var3 = var2.getTarget();
         if (var3.isManualActiveOn(ManagementService.getRuntimeAccess(kernelId).getServer())) {
            var2.activate();
            this.activeTargets.put(var3.getName(), var3.getName());
         }
      }

   }

   private void deactivateAllTargets(boolean var1) throws MigrationException {
      Iterator var2 = this.groups.entrySet().iterator();
      MigratableGroup var3 = null;

      while(var2.hasNext()) {
         var3 = (MigratableGroup)((Map.Entry)var2.next()).getValue();
         synchronized(var3.activationLock) {
            var3.shutdown();
            this.activeTargets.remove(var3.getTarget().getName());
            if (!var3.getTarget().getMigrationPolicy().equals("manual")) {
               SingletonServicesManager.getInstance().removeActiveService(var3.getName());
            }
         }
      }

   }

   public void handlePriorityShutDownTasks() {
      Iterator var1 = this.groups.entrySet().iterator();

      while(var1.hasNext()) {
         MigratableGroup var2 = (MigratableGroup)((Map.Entry)var1.next()).getValue();
         synchronized(var2.activationLock) {
            var2.handlePriorityShutDownTasks();
         }
      }

   }

   public static void invokeStaticMethodOnMemberDeathDetectorHeartbeatReceiver(String var0, Class[] var1, Object[] var2) throws ServiceFailureException {
      try {
         Class var3 = getMemberDeathDetectorHeartbeatReceiverClass();
         invokeStaticMethodOnClass(var3, var0, var1, var2);
      } catch (ClassNotFoundException var4) {
         var4.printStackTrace();
         throw new ServiceFailureException("Unable to find class: weblogic.cluster.messaging.internal.MemberDeathDetectorImpl");
      }
   }

   public static void invokeStaticMethodOnClass(Class var0, String var1, Class[] var2, Object[] var3) throws ServiceFailureException {
      try {
         Method var4 = var0.getMethod(var1, var2);
         var4.invoke((Object)null, var3);
      } catch (NoSuchMethodException var5) {
         var5.printStackTrace();
         throw new ServiceFailureException("No such method: weblogic.cluster.messaging.internal.MemberDeathDetectorImpl.exportHeartbeatReceiver()");
      } catch (InvocationTargetException var6) {
         var6.printStackTrace();
         throw new ServiceFailureException(var6.getCause());
      } catch (IllegalAccessException var7) {
         var7.printStackTrace();
         throw new ServiceFailureException(var7.getCause());
      }
   }

   public MemberDeathDetectorHeartbeatReceiverIntf getMemberDeathDetectorHeartbeatReceiver() throws ServiceFailureException {
      try {
         Class var1 = getMemberDeathDetectorHeartbeatReceiverClass();
         Method var2 = var1.getMethod("getInstance");
         var2.setAccessible(true);
         return (MemberDeathDetectorHeartbeatReceiverIntf)var2.invoke((Object)null);
      } catch (ClassNotFoundException var3) {
         var3.printStackTrace();
         throw new ServiceFailureException("Unable to find class: weblogic.cluster.messaging.internal.MemberDeathDetectorImpl");
      } catch (NoSuchMethodException var4) {
         var4.printStackTrace();
         throw new ServiceFailureException("No such method: weblogic.cluster.messaging.internal.MemberDeathDetectorImpl.getInstance()");
      } catch (InvocationTargetException var5) {
         var5.printStackTrace();
         throw new ServiceFailureException(var5.getCause());
      } catch (IllegalAccessException var6) {
         var6.printStackTrace();
         throw new ServiceFailureException(var6.getCause());
      }
   }

   private static Class getMemberDeathDetectorHeartbeatReceiverClass() throws ClassNotFoundException {
      Class var0 = Class.forName("weblogic.cluster.messaging.internal.MemberDeathDetectorHeartbeatReceiver");
      return var0;
   }
}
