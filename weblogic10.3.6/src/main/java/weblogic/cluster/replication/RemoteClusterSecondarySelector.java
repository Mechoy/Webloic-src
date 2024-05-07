package weblogic.cluster.replication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import weblogic.cluster.ClusterLogger;
import weblogic.cluster.ClusterMemberInfo;
import weblogic.cluster.ClusterMembersChangeEvent;
import weblogic.cluster.ClusterMembersChangeListener;
import weblogic.cluster.ClusterService;
import weblogic.cluster.RemoteClusterMemberManager;
import weblogic.cluster.RemoteClusterMembersChangeListener;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.ServerIdentity;
import weblogic.rmi.spi.HostID;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public class RemoteClusterSecondarySelector implements ClusterMembersChangeListener, RemoteClusterMembersChangeListener, SecondarySelector {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final HostID LOCAL_HOSTID = LocalServerIdentity.getIdentity();
   private final Map hostIDToRepChannelMap;
   private final WorkManager workManager;
   private final HashSet remoteServerInfos;
   private final HashSet localServerInfos;
   private HostID secondaryHostID;

   public static RemoteClusterSecondarySelector getSecondarySelector() {
      return RemoteClusterSecondarySelector.SingletonMaker.singleton;
   }

   private RemoteClusterSecondarySelector() {
      this.hostIDToRepChannelMap = new ConcurrentHashMap(11);
      this.workManager = WorkManagerFactory.getInstance().getSystem();
      this.remoteServerInfos = new HashSet();
      this.localServerInfos = new HashSet();
      ClusterMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster();
      if (var1 != null) {
         ClusterService var2 = ClusterService.getClusterService();
         var2.addClusterMembersListener(this);
         this.localServerInfos.add(var2.getLocalMember());
      }

      RemoteClusterMemberManager.getInstance().addRemoteClusterMemberListener(this);
   }

   public synchronized void clusterMembersChanged(ClusterMembersChangeEvent var1) {
      this.secondaryHostID = null;
      ServerIdentity var2 = var1.getClusterMemberInfo().identity();
      switch (var1.getAction()) {
         case 0:
            if (ReplicationDebugLogger.isDebugEnabled()) {
               ClusterLogger.logNewServerJoinedCluster(var2.toString());
            }

            this.addNewLocalClusterServer(var1.getClusterMemberInfo());
            break;
         case 1:
            if (ReplicationDebugLogger.isDebugEnabled()) {
               ClusterLogger.logRemovingServerFromCluster(var2.toString());
            }

            this.localServerInfos.remove(var1.getClusterMemberInfo());
            if (!ManagementService.getRuntimeAccess(kernelId).getServerRuntime().isShuttingDown()) {
               HostID[] var3 = new HostID[]{var2};
               Iterator var4 = MANReplicationManager.theOne().ids();
               this.workManager.schedule(new ChangeSecondaryInfo(var4, var3, MANReplicationManager.theOne()));
               Iterator var5 = MANAsyncReplicationManager.theOne().ids();
               this.workManager.schedule(new ChangeSecondaryInfo(var5, var3, MANAsyncReplicationManager.theOne()));
            }
            break;
         case 2:
            this.addNewLocalClusterServer(var1.getClusterMemberInfo());
      }

   }

   private void addNewLocalClusterServer(ClusterMemberInfo var1) {
      this.localServerInfos.add(var1);
      this.hostIDToRepChannelMap.put(var1.identity(), var1.replicationChannel());
   }

   public synchronized void remoteClusterMembersChanged(ArrayList var1) {
      this.secondaryHostID = null;
      if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
         ReplicationDetailsDebugLogger.debug("Received new cluster list from remote cluster " + var1);
      }

      this.remoteServerInfos.clear();
      this.hostIDToRepChannelMap.clear();
      int var2 = var1.size();
      if (var2 > 0) {
         this.remoteServerInfos.addAll(var1);
      }

      ClusterMemberInfo var4;
      for(int var3 = 0; var3 < var2; ++var3) {
         var4 = (ClusterMemberInfo)var1.get(var3);
         this.hostIDToRepChannelMap.put(var4.identity(), var4.replicationChannel());
      }

      Iterator var10 = this.localServerInfos.iterator();

      while(var10.hasNext()) {
         var4 = (ClusterMemberInfo)var10.next();
         this.hostIDToRepChannelMap.put(var4.identity(), var4.replicationChannel());
      }

      if (!this.canReplicateToRemoteCluster()) {
         HashSet var11 = (HashSet)this.localServerInfos.clone();
         Iterator var5 = var11.iterator();
         ArrayList var6 = new ArrayList(var11.size());

         while(var5.hasNext()) {
            ClusterMemberInfo var7 = (ClusterMemberInfo)var5.next();
            if (!var7.identity().equals(LOCAL_HOSTID)) {
               var6.add(var7.identity());
            }
         }

         HostID[] var12 = new HostID[var6.size()];
         var6.toArray(var12);
         Iterator var8 = MANReplicationManager.theOne().ids();
         this.workManager.schedule(new ChangeSecondaryInfo(var8, var12, MANReplicationManager.theOne()));
         Iterator var9 = MANAsyncReplicationManager.theOne().ids();
         this.workManager.schedule(new ChangeSecondaryInfo(var9, var12, MANAsyncReplicationManager.theOne()));
      }

   }

   public HostID getSecondarySrvr() {
      if (this.secondaryHostID != null) {
         return this.secondaryHostID;
      } else {
         synchronized(this) {
            if (this.canReplicateToRemoteCluster()) {
               this.secondaryHostID = this.selectSecondaryFromRemoteCluster();
            } else if (this.canReplicateToLocalCluster()) {
               this.secondaryHostID = this.selectSecondaryBasedOnLoad();
            } else {
               this.secondaryHostID = null;
            }
         }

         if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
            ReplicationDetailsDebugLogger.debug("New Secondary server " + this.secondaryHostID);
         }

         return this.secondaryHostID;
      }
   }

   synchronized boolean canReplicateToRemoteCluster() {
      int var1 = this.localServerInfos.size();
      int var2 = this.remoteServerInfos.size();
      if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
         ReplicationDetailsDebugLogger.debug("RemoteClusterSecondarySelector.canReplicateToRemoteCluster(): Local: " + var1 + " Remote: " + var2 + " canReplicateToRemote " + (var1 <= 2 * var2 ? "TRUE" : "FALSE"));
      }

      return var1 <= 2 * var2;
   }

   synchronized String getReplicationChannelFor(HostID var1) {
      return (String)this.hostIDToRepChannelMap.get(var1);
   }

   private boolean canReplicateToLocalCluster() {
      int var1 = this.localServerInfos.size();
      int var2 = this.remoteServerInfos.size();
      if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
         ReplicationDetailsDebugLogger.debug("RemoteClusterSecondarySelector.canReplicateToRemoteCluster(): Local: " + var1 + " Remote: " + var2 + " canReplicateToRemote " + (var1 + var2 >= 2 ? "TRUE" : "FALSE"));
      }

      return var1 + var2 >= 2;
   }

   public ArrayList getSecondaryCandidates() {
      ArrayList var1 = new ArrayList();
      Iterator var2;
      synchronized(this) {
         var2 = ((HashSet)this.remoteServerInfos.clone()).iterator();
      }

      while(var2.hasNext()) {
         ClusterMemberInfo var3 = (ClusterMemberInfo)var2.next();
         var1.add(var3.identity());
      }

      var1.addAll(LocalSecondarySelector.getSecondarySelector().getSecondaryCandidates());
      return var1;
   }

   String[] getActiveServersInRemoteCluster() {
      ArrayList var1 = new ArrayList();
      Iterator var2;
      synchronized(this) {
         var2 = ((HashSet)this.remoteServerInfos.clone()).iterator();
      }

      while(var2.hasNext()) {
         ClusterMemberInfo var3 = (ClusterMemberInfo)var2.next();
         var1.add(var3.serverName());
      }

      String[] var6 = new String[var1.size()];
      var1.toArray(var6);
      return var6;
   }

   private HostID selectSecondaryFromRemoteCluster() {
      TreeSet var1 = new TreeSet();
      TreeSet var2 = new TreeSet();
      Iterator var3 = ((HashSet)this.remoteServerInfos.clone()).iterator();

      ClusterMemberInfo var4;
      while(var3.hasNext()) {
         var4 = (ClusterMemberInfo)var3.next();
         var1.add(var4.identity());
      }

      var3 = ((HashSet)this.localServerInfos.clone()).iterator();

      while(var3.hasNext()) {
         var4 = (ClusterMemberInfo)var3.next();
         var2.add(var4.identity());
      }

      int var8 = var1.size();
      ArrayList var5 = new ArrayList(var1);
      ArrayList var6 = new ArrayList(var2);
      int var7 = var6.indexOf(LOCAL_HOSTID);
      return (HostID)var5.get(var7 % var8);
   }

   private HostID selectSecondaryBasedOnLoad() {
      TreeSet var1 = new TreeSet();
      TreeSet var2 = new TreeSet();
      Iterator var3 = ((HashSet)this.remoteServerInfos.clone()).iterator();

      ClusterMemberInfo var4;
      while(var3.hasNext()) {
         var4 = (ClusterMemberInfo)var3.next();
         var1.add(var4.identity());
      }

      var3 = ((HashSet)this.localServerInfos.clone()).iterator();

      while(var3.hasNext()) {
         var4 = (ClusterMemberInfo)var3.next();
         if (this.isServerOnSameMachine(var4)) {
            var2.add(var4.identity());
         } else {
            var1.add(var4.identity());
         }
      }

      int var9 = var1.size();
      int var5 = var2.size();
      ArrayList var6 = new ArrayList(var1);
      ArrayList var7 = new ArrayList(var2);
      int var8 = var7.indexOf(LOCAL_HOSTID);
      if (var9 == 0) {
         return (HostID)var7.get((var8 + 1) % var5);
      } else {
         return (HostID)var6.get(var8 % var9);
      }
   }

   private boolean isServerOnSameMachine(ClusterMemberInfo var1) {
      try {
         new ServerSocket(0, 0, InetAddress.getByName(var1.hostAddress()));
         return true;
      } catch (IOException var3) {
         return false;
      }
   }

   public synchronized void removeDeadSecondarySrvr(HostID var1) {
      if (var1.equals(this.secondaryHostID)) {
         this.secondaryHostID = null;
      }

      boolean var2 = false;
      Iterator var3 = this.remoteServerInfos.iterator();

      ClusterMemberInfo var4;
      while(var3.hasNext()) {
         var4 = (ClusterMemberInfo)var3.next();
         if (var4.identity().equals(var1)) {
            var3.remove();
            var2 = true;
            break;
         }
      }

      if (!var2) {
         var3 = this.localServerInfos.iterator();

         while(var3.hasNext()) {
            var4 = (ClusterMemberInfo)var3.next();
            if (var4.identity().equals(var1)) {
               var3.remove();
               break;
            }
         }
      }

      this.hostIDToRepChannelMap.remove(var1);
   }

   // $FF: synthetic method
   RemoteClusterSecondarySelector(Object var1) {
      this();
   }

   private static class ChangeSecondaryInfo implements Runnable {
      private final Iterator iterator;
      private final HostID[] hostIDs;
      private final HashMap hostIDToROIDMap;
      private final ReplicationManager mngr;

      private ChangeSecondaryInfo(Iterator var1, HostID[] var2, ReplicationManager var3) {
         this.iterator = var1;
         this.hostIDs = var2;
         this.hostIDToROIDMap = new HashMap();
         this.mngr = var3;
      }

      public void run() {
         int var1 = 0;
         long var2 = System.currentTimeMillis();

         for(int var4 = 0; var4 < this.hostIDs.length; ++var4) {
            ArrayList var5 = (ArrayList)this.hostIDToROIDMap.get(this.hostIDs[var4]);
            if (var5 == null) {
               var5 = new ArrayList();
               this.hostIDToROIDMap.put(this.hostIDs[var4], var5);
            }

            while(this.iterator.hasNext()) {
               WrappedRO var6 = (WrappedRO)this.iterator.next();
               if (this.hostIDs[var4].equals(var6.getOtherHost())) {
                  var5.add(var6.getROInfo().getROID());
                  var6.setOtherHost((HostID)null);
                  var6.setOtherHostInfo((Object)null);
                  var6.ensureStatus((byte)0);
                  ++var1;
               }
            }
         }

         long var12 = System.currentTimeMillis();

         for(int var13 = 0; var13 < this.hostIDs.length; ++var13) {
            ArrayList var7 = (ArrayList)this.hostIDToROIDMap.get(this.hostIDs[var13]);
            int var8 = var7.size();
            if (var8 > 0) {
               try {
                  ReplicationServicesInternal var9 = this.mngr.getRepMan(this.hostIDs[var13]);
                  ROID[] var10 = new ROID[var8];
                  var7.toArray(var10);
                  var9.remove(var10);
               } catch (RemoteException var11) {
               }
            }
         }

         if (var1 > 0 && ReplicationDetailsDebugLogger.isDebugEnabled()) {
            ReplicationDetailsDebugLogger.debug("Changed the status of " + var1 + " objects and it took " + (var12 - var2) + " ms");
         }

      }

      // $FF: synthetic method
      ChangeSecondaryInfo(Iterator var1, HostID[] var2, ReplicationManager var3, Object var4) {
         this(var1, var2, var3);
      }
   }

   private static class SingletonMaker {
      private static final RemoteClusterSecondarySelector singleton = new RemoteClusterSecondarySelector();
   }
}
