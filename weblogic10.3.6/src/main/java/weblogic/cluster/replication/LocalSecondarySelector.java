package weblogic.cluster.replication;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import weblogic.cluster.ClusterHelper;
import weblogic.cluster.ClusterLogger;
import weblogic.cluster.ClusterMemberInfo;
import weblogic.cluster.ClusterMembersChangeEvent;
import weblogic.cluster.ClusterMembersChangeListener;
import weblogic.cluster.ClusterService;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.ServerIdentity;
import weblogic.rmi.spi.HostID;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public class LocalSecondarySelector implements ClusterMembersChangeListener, SecondarySelector {
   private static final HostID LOCAL_HOSTID = LocalServerIdentity.getIdentity();
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private final String machineName;
   private final String preferredSecondaryGroup;
   private final HashSet serverInfos;
   private final WorkManager workManager;
   private boolean clusterHasSecondarySrvrs;
   private boolean placeSecondariesAutomatically;
   private final ArrayList preferredCandidates = new ArrayList();
   private final ArrayList remoteCandidates = new ArrayList();
   private final ArrayList localCandidates = new ArrayList();
   private HostID currentSecondary = null;
   private final MachineServerMap localMap;
   private final ArrayList machineList = new ArrayList();

   public static SecondarySelector getSecondarySelector() {
      return LocalSecondarySelector.SingletonMaker.singleton;
   }

   protected LocalSecondarySelector() {
      ServerMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer();
      this.machineName = var1.getMachine() == null ? ClusterHelper.getMachineName() : var1.getMachine().getName();
      this.preferredSecondaryGroup = var1.getPreferredSecondaryGroup();
      String var2 = var1.getReplicationGroup();
      this.placeSecondariesAutomatically = this.preferredSecondaryGroup == null || var2 == null;
      this.serverInfos = new HashSet();
      if (var1.getCluster() != null) {
         ClusterService.getClusterService().addClusterMembersListener(this);
      }

      this.workManager = WorkManagerFactory.getInstance().getSystem();
      this.localMap = new MachineServerMap(this.machineName);
   }

   public void clusterMembersChanged(ClusterMembersChangeEvent var1) {
      ServerIdentity var2 = var1.getClusterMemberInfo().identity();
      switch (var1.getAction()) {
         case 0:
            if (ReplicationDebugLogger.isDebugEnabled()) {
               ClusterLogger.logNewServerJoinedCluster(var2.toString());
            }

            this.addNewServer(var1.getClusterMemberInfo());
            break;
         case 1:
            if (ReplicationDebugLogger.isDebugEnabled()) {
               ClusterLogger.logRemovingServerFromCluster(var2.toString());
            }

            this.removeDeadServer(var1.getClusterMemberInfo());
            Iterator var3 = ReplicationManager.theOne().ids();
            if (var3.hasNext()) {
               this.workManager.schedule(new ChangeSecondaryInfo(var3, var2));
            }

            Iterator var4 = AsyncReplicationManager.theOne().ids();
            if (var4.hasNext()) {
               this.workManager.schedule(new ChangeSecondaryInfo(var4, var2));
            }
            break;
         case 2:
            ClusterLogger.logUpdatingServerInTheCluster(var2.toString());
            this.addNewServer(var1.getClusterMemberInfo());
      }

   }

   public synchronized HostID getSecondarySrvr() {
      if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
         ReplicationDetailsDebugLogger.debug("Has secondary servers? " + this.clusterHasSecondarySrvrs);
         ReplicationDetailsDebugLogger.debug("Current secondary server? " + this.currentSecondary);
      }

      if (this.clusterHasSecondarySrvrs) {
         if (this.currentSecondary != null) {
            return this.currentSecondary;
         }

         if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
            ReplicationDetailsDebugLogger.debug("Preferred list : " + this.preferredCandidates);
            ReplicationDetailsDebugLogger.debug("Remote list : " + this.remoteCandidates);
            ReplicationDetailsDebugLogger.debug("Local list : " + this.localCandidates);
         }

         if (this.placeSecondariesAutomatically) {
            this.currentSecondary = this.selectSecondaryAutomatically();
         } else {
            this.currentSecondary = this.selectSecondaryBasedOnConfig();
         }

         if (ReplicationDebugLogger.isDebugEnabled()) {
            ReplicationDebugLogger.debug("New secondary server is " + this.currentSecondary);
         }
      }

      return this.currentSecondary;
   }

   private HostID selectSecondaryAutomatically() {
      int var1 = this.localMap.getServerList().indexOf(LOCAL_HOSTID);
      int var2 = this.machineList.size();
      int var4;
      if (var2 > 1) {
         int var8 = this.machineList.indexOf(this.localMap);
         var4 = 1;

         ArrayList var5;
         for(var5 = new ArrayList(); var4 < var2; ++var4) {
            MachineServerMap var6 = (MachineServerMap)this.machineList.get((var4 + var8) % var2);
            List var7 = var6.getServerList();
            if (ReplicationDebugLogger.isDebugEnabled()) {
               ReplicationDebugLogger.debug("localServerList size " + this.localMap.getServerList().size() + "\n machineName " + var6.machineName + "\n remoteServerList size " + var7.size() + "\n currSrvrIndex " + var1);
            }

            if (var7.size() > var1) {
               return (HostID)var7.get(var1);
            }

            var5.addAll(var7);
         }

         return (HostID)var5.get(var1 % var5.size());
      } else {
         List var3 = this.localMap.getServerList();
         var4 = var3.size();
         return var4 == 1 ? null : (HostID)var3.get((var1 + 1) % var4);
      }
   }

   private HostID selectSecondaryBasedOnConfig() {
      int var1 = this.preferredCandidates.size();
      int var2 = this.remoteCandidates.size();
      int var3 = this.localCandidates.size();
      HostID var4;
      ArrayList var5;
      if (var1 > var3) {
         var5 = this.getCombinedCandidates(this.localCandidates, this.remoteCandidates);
         var4 = (HostID)this.preferredCandidates.get(var5.indexOf(LOCAL_HOSTID) % var1);
      } else if (var1 > 0) {
         var4 = (HostID)this.preferredCandidates.get(this.localCandidates.indexOf(LOCAL_HOSTID) % var1);
      } else if (var2 > var3) {
         var5 = this.getCombinedCandidates(this.localCandidates, this.remoteCandidates);
         var4 = (HostID)this.remoteCandidates.get(var5.indexOf(LOCAL_HOSTID) % var2);
      } else if (var2 > 0) {
         var4 = (HostID)this.remoteCandidates.get(this.localCandidates.indexOf(LOCAL_HOSTID) % var2);
      } else {
         var4 = (HostID)this.localCandidates.get((this.localCandidates.indexOf(LOCAL_HOSTID) + 1) % var3);
      }

      return var4;
   }

   private ArrayList getCombinedCandidates(ArrayList var1, ArrayList var2) {
      if (var2.size() == 0) {
         return var1;
      } else if (var1.size() == 0) {
         return var2;
      } else {
         TreeSet var3 = new TreeSet();
         var3.addAll(var1);
         var3.addAll(var2);
         ArrayList var4 = new ArrayList();
         var4.addAll((Collection)var3.clone());
         return var4;
      }
   }

   public ArrayList getSecondaryCandidates() {
      ArrayList var1 = new ArrayList();
      if (this.placeSecondariesAutomatically) {
         Iterator var2 = null;
         synchronized(this) {
            var2 = ((Collection)this.machineList.clone()).iterator();
         }

         while(var2.hasNext()) {
            MachineServerMap var3 = (MachineServerMap)var2.next();
            Iterator var4 = var3.getServerList().iterator();

            while(var4.hasNext()) {
               var1.add(var4.next());
            }
         }
      } else {
         var1.addAll((Collection)this.preferredCandidates.clone());
         var1.addAll((Collection)this.remoteCandidates.clone());
         var1.addAll((Collection)this.localCandidates.clone());
      }

      var1.remove(LOCAL_HOSTID);
      return var1;
   }

   public synchronized void removeDeadSecondarySrvr(HostID var1) {
      Iterator var2 = this.serverInfos.iterator();

      while(var2.hasNext()) {
         ClusterMemberInfo var3 = (ClusterMemberInfo)var2.next();
         if (var3.identity().equals(var1)) {
            this.removeDeadServer(var3);
            break;
         }
      }

      if (ReplicationDebugLogger.isDebugEnabled()) {
         if (this.clusterHasSecondarySrvrs) {
            ReplicationDebugLogger.debug("Unreachable secondary server: " + var1 + " New secondary server " + this.getSecondarySrvr());
         } else {
            ReplicationDebugLogger.debug("Unreachable secondary server: " + var1 + " and there are no secondary servers currently available to replication");
         }
      }

   }

   private synchronized void addNewServer(ClusterMemberInfo var1) {
      this.serverInfos.add(var1);
      if (var1.replicationGroup() == null || var1.preferredSecondaryGroup() == null) {
         this.placeSecondariesAutomatically = true;
      }

      this.reset();
   }

   private synchronized void removeDeadServer(ClusterMemberInfo var1) {
      this.serverInfos.remove(var1);
      this.reset();
   }

   private void reset() {
      this.currentSecondary = null;
      if (this.placeSecondariesAutomatically) {
         this.recomputeSecondaryAutomatically((Collection)this.serverInfos.clone());
      } else {
         this.recomputeSecondary((Collection)this.serverInfos.clone());
      }

      this.clusterHasSecondarySrvrs = this.clusterHasSecondaryServers();
      if (ReplicationDetailsDebugLogger.isDebugEnabled() && this.clusterHasSecondarySrvrs) {
         HostID var1 = this.getSecondarySrvr();
         ReplicationDetailsDebugLogger.debug("Secondary server " + var1);
      }

   }

   private boolean clusterHasSecondaryServers() {
      return this.preferredCandidates.size() > 0 || this.remoteCandidates.size() > 0 || this.localCandidates.size() > 1 || this.machineList.size() > 0 || this.localMap.getServerList().size() > 1;
   }

   private void recomputeSecondaryAutomatically(Collection var1) {
      HashMap var2 = new HashMap();
      this.localMap.reset();
      this.localMap.addServer(LOCAL_HOSTID);
      var2.put(this.machineName, this.localMap);
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         ClusterMemberInfo var4 = (ClusterMemberInfo)var3.next();
         MachineServerMap var5 = (MachineServerMap)var2.get(var4.machineName());
         if (var5 == null) {
            var5 = new MachineServerMap(var4.machineName());
            var2.put(var4.machineName(), var5);
         }

         var5.addServer(var4.identity());
         if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
            this.printDebug(var4);
         }
      }

      TreeSet var8 = new TreeSet();
      var8.addAll(var2.values());
      synchronized(this) {
         this.machineList.clear();
         this.machineList.addAll(var8);
      }
   }

   private void printDebug(ClusterMemberInfo var1) {
      ReplicationDetailsDebugLogger.debug("**Processing " + var1.identity() + " : " + var1.serverName() + " on " + var1.machineName() + " in " + var1.replicationGroup() + " prefers " + var1.preferredSecondaryGroup());
   }

   private void recomputeSecondary(Collection var1) {
      TreeSet var2 = new TreeSet();
      TreeSet var3 = new TreeSet();
      TreeSet var4 = new TreeSet();
      TreeSet var5 = new TreeSet();
      var5.add(LOCAL_HOSTID);
      Iterator var6 = var1.iterator();

      while(var6.hasNext()) {
         ClusterMemberInfo var7 = (ClusterMemberInfo)var6.next();
         if (this.preferredSecondaryGroup.equals(var7.replicationGroup())) {
            if (this.isServerOnSameMachine(var7)) {
               var4.add(var7.identity());
            } else {
               var2.add(var7.identity());
            }
         } else if (this.isServerOnSameMachine(var7)) {
            var5.add(var7.identity());
         } else {
            var3.add(var7.identity());
         }

         if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
            this.printDebug(var7);
         }
      }

      this.preferredCandidates.clear();
      this.remoteCandidates.clear();
      this.localCandidates.clear();
      this.preferredCandidates.addAll(var2);
      this.preferredCandidates.addAll(var4);
      this.remoteCandidates.addAll(var3);
      this.localCandidates.addAll(var5);
   }

   private boolean isServerOnSameMachine(ClusterMemberInfo var1) {
      return this.machineName.equals(var1.machineName());
   }

   private static class ChangeSecondaryInfo implements Runnable {
      private final Iterator iterator;
      private final HostID hostID;

      private ChangeSecondaryInfo(Iterator var1, HostID var2) {
         this.iterator = var1;
         this.hostID = var2;
      }

      public void run() {
         int var1 = 0;
         long var2 = System.currentTimeMillis();

         while(this.iterator.hasNext()) {
            WrappedRO var4 = (WrappedRO)this.iterator.next();
            if (this.hostID.equals(var4.getOtherHost())) {
               var4.setOtherHost((HostID)null);
               var4.setOtherHostInfo((Object)null);
               var4.ensureStatus((byte)0);
               ++var1;
            }
         }

         long var6 = System.currentTimeMillis();
         if (var1 > 0 && ReplicationDebugLogger.isDebugEnabled()) {
            ReplicationDebugLogger.debug("Changed the status of " + var1 + " objects and it took " + (var6 - var2) + " ms");
         }

      }

      // $FF: synthetic method
      ChangeSecondaryInfo(Iterator var1, HostID var2, Object var3) {
         this(var1, var2);
      }
   }

   private static class MachineServerMap implements Comparable {
      private final TreeSet set;
      private final String machineName;

      private MachineServerMap(String var1) {
         this.set = new TreeSet();
         this.machineName = var1;
      }

      public synchronized void addServer(HostID var1) {
         this.set.add(var1);
      }

      public void reset() {
         this.set.clear();
      }

      public synchronized List getServerList() {
         ArrayList var1 = new ArrayList();
         var1.addAll(this.set);
         return var1;
      }

      public boolean equals(Object var1) {
         if (var1 instanceof MachineServerMap) {
            MachineServerMap var2 = (MachineServerMap)var1;
            return this.machineName.equals(var2.machineName);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.machineName.hashCode();
      }

      public int compareTo(Object var1) {
         try {
            MachineServerMap var2 = (MachineServerMap)var1;
            return this.machineName.compareTo(var2.machineName);
         } catch (ClassCastException var3) {
            throw new AssertionError("Unexpected exception" + var3);
         }
      }

      // $FF: synthetic method
      MachineServerMap(String var1, Object var2) {
         this(var1);
      }
   }

   private static class SingletonMaker {
      private static final LocalSecondarySelector singleton = new LocalSecondarySelector();
   }
}
