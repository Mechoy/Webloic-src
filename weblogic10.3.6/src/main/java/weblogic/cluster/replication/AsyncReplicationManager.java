package weblogic.cluster.replication;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.rmi.ConnectException;
import java.rmi.ConnectIOException;
import java.rmi.MarshalException;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import javax.naming.NamingException;
import weblogic.cluster.ClusterExtensionLogger;
import weblogic.cluster.ClusterLogger;
import weblogic.cluster.ClusterService;
import weblogic.management.ManagementException;
import weblogic.protocol.ServerIdentity;
import weblogic.rmi.ServerShuttingDownException;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.RequestTimeoutException;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.rmi.spi.HostID;

public class AsyncReplicationManager extends ReplicationManager implements AsyncFlush {
   private HostID secondaryHost;
   private AsyncQueueManager queue = new AsyncQueueManager(this, true);

   public static ReplicationManager theOne() {
      return AsyncReplicationManager.SingletonMaker.singleton;
   }

   public static ReplicationServices services() {
      return theOne();
   }

   protected AsyncReplicationManager() {
      this.initializeRuntime();
   }

   protected void initializeRuntime() {
      try {
         new AsyncReplicationRuntime();
      } catch (ManagementException var2) {
         throw new AssertionError(var2);
      }
   }

   public long getTimeAtLastUpdateFlush() {
      return this.queue != null ? this.queue.getTimeAtLastUpdateFlush() : 0L;
   }

   public int getSessionsWaitingForFlushCount() {
      return this.queue != null ? this.queue.getQueueSize() : 0;
   }

   public static void start() {
      try {
         if (ClusterService.getClusterService().isReplicationTimeoutEnabled()) {
            ServerHelper.exportObject(theOne(), ClusterService.getClusterService().getHeartbeatTimeoutMillis());
         } else {
            ServerHelper.exportObject(theOne());
         }

      } catch (RemoteException var1) {
         throw new AssertionError("Failed to export replication system" + var1);
      }
   }

   public static void stop() {
      try {
         ServerHelper.unexportObject(theOne(), false);
      } catch (RemoteException var1) {
         throw new AssertionError("Failed to unexport replication system" + var1);
      }

      AsyncReplicationManager.SingletonMaker.singleton.blockingFlush();
   }

   public void unregister(ROID[] var1, Object var2) {
      this.removeFromQueue(var1);
      super.unregister(var1, var2);
   }

   public synchronized void removeFromQueue(ROID[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         Iterator var3 = this.queue.iterator();

         while(var3.hasNext()) {
            AsyncUpdate var4 = (AsyncUpdate)var3.next();
            if (var4.getId().equals(var1[var2])) {
               this.queue.remove(var4);
            }
         }
      }

   }

   public Object updateSecondary(ROID var1, Serializable var2, Object var3) throws NotFoundException {
      WrappedRO var4 = this.getPrimary(var1, false, var3);
      HostID var5 = var4.getOtherHost();
      if (var5 != null && var5.equals(this.getSecondarySelector().getSecondarySrvr())) {
         AsyncReplicatable var6 = (AsyncReplicatable)var2;
         synchronized(var6) {
            if (var6.isQueued()) {
               if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
                  ReplicationDetailsDebugLogger.debug("Avoided a duplicate update to the queue for " + var1 + " with version: " + var4.getVersion(var3) + " and key: " + var3 + " and secondary info: " + var4.getSecondaryROInfo());
               }

               return var4.getSecondaryROInfo();
            }

            var6.setQueued();
         }

         if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
            ReplicationDetailsDebugLogger.debug("Adding a new update to the queue for " + var1 + " with version: " + var4.getVersion(var3) + " and key: " + var3);
         }

         var4.incrementVersion(var3);
         this.queue.addToUpdates(new AsyncUpdate(var1, var4.getVersion(var3), var6, var3));
      } else {
         if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
            ReplicationDetailsDebugLogger.debug("No Secondary for this session or Secondary become New Primary or Cluster has changed and we need to choose a new secondary. Hence creating a new Secondary for session " + var1 + " and key: " + var3);
         }

         var4.incrementVersion(var3);
         this.createSecondary(var4, var3);
      }

      return var4.getSecondaryROInfo();
   }

   public void update(AsyncBatch var1) throws RemoteException {
      AsyncUpdate[] var2 = var1.getUpdates();
      ArrayList var3 = new ArrayList();

      for(int var4 = 0; var4 < var2.length; ++var4) {
         if (var2[var4].isUpdate()) {
            try {
               this.update(var2[var4].getId(), var2[var4].getVersion(), var2[var4].getChange(), var2[var4].getKey());
            } catch (Exception var6) {
               var3.add(var2[var4].getId());
               if (ReplicationDebugLogger.isDebugEnabled()) {
                  ReplicationDebugLogger.debug(var2[var4].getId(), "Error updating secondary with version " + var2[var4].getVersion() + " from " + var2[var4].getPrimaryHost() + " on this server:  " + LOCAL_HOSTID + ". Re-creating secondary.", var6);
               }
            }
         } else {
            this.create(var2[var4].getPrimaryHost(), var2[var4].getVersion(), var2[var4].getId(), var2[var4].getRO());
         }
      }

      if (var3.size() > 0) {
         ROID[] var7 = new ROID[var3.size()];
         var3.toArray(var7);
         throw new AsyncBatchFailedException(var7);
      }
   }

   public void blockingFlush() {
      this.queue.flushOnce();
   }

   protected void createSecondary(WrappedRO var1, Object var2) {
      HostID var3 = this.getSecondarySelector().getSecondarySrvr();
      if (var3 == null) {
         var3 = this.secondaryHost;
      }

      if (var3 == null) {
         var3 = var1.getOtherHost();
      }

      if (this.secondaryHost == null) {
         this.secondaryHost = var3;
      }

      this.trySecondary(var1, var3, var2);
   }

   protected boolean trySecondary(WrappedRO var1, HostID var2, Object var3) {
      var1.setOtherHostInfo(var2);
      var1.setOtherHost(var2);
      if (var3 == null) {
         Iterator var9 = var1.keys();
         boolean var5 = false;

         while(var9.hasNext()) {
            Object var6 = var9.next();
            if (var6 != null) {
               var5 = this.trySecondary(var1, var2, var6);
            }
         }

         return var5;
      } else {
         AsyncReplicatable var4 = (AsyncReplicatable)var1.getRO(var3);
         synchronized(var4.getBatchedChanges()) {
            if (var4.isQueued()) {
               if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
                  ReplicationDetailsDebugLogger.debug("Avoided a duplicate create to the queue for " + var1.getID() + " with version: " + var1.getVersion(var3) + " and key: " + var3 + " and secondary info: " + var1.getSecondaryROInfo());
               }

               return true;
            }

            var4.setQueued();
         }

         if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
            ReplicationDetailsDebugLogger.debug(var1.getID(), "Adding a new create to the queue with version: " + var1.getVersion(var3) + " and key: " + var3);
         }

         this.queue.addToUpdates(new AsyncUpdate(LOCAL_HOSTID, var1.getID(), var1.getVersion(var3), var3, var4));
         return true;
      }
   }

   public synchronized void flushQueue(BlockingQueue var1) {
      HashSet var2 = new HashSet();
      var1.drainTo(var2);
      this.flush(var2);
   }

   protected ReplicationServicesInternal getRepMan(HostID var1) throws RemoteException {
      if (var1.isLocal()) {
         return this;
      } else {
         ReplicationServicesInternal var2 = (ReplicationServicesInternal)this.cache.get(var1);
         if (var2 == null) {
            try {
               var2 = svcLocator.replicationServicesLookup((ServerIdentity)var1, AsyncReplicationManager.class);
               this.cache.put(var1, var2);
            } catch (NamingException var4) {
               throw new RemoteException(var4.getMessage(), var4);
            }
         }

         return var2;
      }
   }

   protected ReplicationServicesInternal getRepManWithChannelName(HostID var1, String var2) throws RemoteException {
      try {
         ReplicationServicesInternal var3 = svcLocator.replicationServicesLookup((ServerIdentity)var1, var2, AsyncReplicationManager.class);
         return var3;
      } catch (NamingException var5) {
         throw new RemoteException(var5.getMessage(), var5);
      }
   }

   public void flush(Set var1) {
      if (ReplicationDebugLogger.isDebugEnabled()) {
         ReplicationDebugLogger.debug("FLUSH");
      }

      HostID var2 = this.getSecondarySelector().getSecondarySrvr();
      if (var2 != null && !var2.equals(this.secondaryHost)) {
         this.resetSecondaryHost(var2, var1);
         this.secondaryHost = var2;
      }

      while(var2 != null) {
         try {
            ReplicationServicesInternal var3 = this.getRepMan(var2);
            int var4 = var1.size();
            AsyncUpdate[] var5 = new AsyncUpdate[var4];
            var1.toArray(var5);
            AsyncBatch var6 = new AsyncBatch(var5);
            var3.update(var6);
            resetTimeOut(var2);
            if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
               ReplicationDetailsDebugLogger.debug("AsyncBatch flushed to " + var2);
            }

            return;
         } catch (ConnectException var7) {
            if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
               ReplicationDetailsDebugLogger.debug((String)("Failed to reach secondary server " + var2 + " trying to create/update secondary for batch"), (Throwable)var7);
            }

            this.cleanupDeadServer(var2);
         } catch (ServerShuttingDownException var8) {
            if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
               ReplicationDetailsDebugLogger.debug((String)("Secondary server " + var2 + " is shutting down. Failed to create/update secondary '" + var2 + "' for batch"), (Throwable)var8);
            }

            this.cleanupDeadServer(var2);
         } catch (ConnectIOException var9) {
            if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
               ReplicationDetailsDebugLogger.debug((String)("ConnectIOException while trying to connect to secondary server " + var2 + " for " + "creating/updating secondary '" + var2 + "' for batch"), (Throwable)var9);
            }

            this.cleanupDeadServer(var2);
         } catch (MarshalException var10) {
            if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
               ReplicationDetailsDebugLogger.debug((String)"Marshalling error", (Throwable)var10);
            }

            ClusterLogger.logUnableToUpdateNonSerializableObject(var10);
            if (var10.detail instanceof NotSerializableException) {
               this.decrementUpdates(var1);
            }

            return;
         } catch (UnmarshalException var11) {
            if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
               ReplicationDetailsDebugLogger.debug((String)"Unmarshalling error", (Throwable)var11);
            }

            ClusterLogger.logUnableToUpdateNonSerializableObject(var11);
            if (var11.detail instanceof NotSerializableException) {
               this.decrementUpdates(var1);
            }

            return;
         } catch (AsyncBatchFailedException var12) {
            if (ReplicationDebugLogger.isDebugEnabled()) {
               ReplicationDebugLogger.debug("AsyncBatchFailed updating secondary for batch on " + var2 + ". Re-creating secondaries.");
            }

            var1 = this.recreateList(var12.getIDs(), var2, var1);
            this.flush(var1);
            return;
         } catch (RemoteRuntimeException var13) {
            ClusterExtensionLogger.logUnexpectedExceptionDuringReplication(var13.getCause());
            return;
         } catch (RequestTimeoutException var14) {
            if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
               ReplicationDetailsDebugLogger.debug((String)("Error sending batched sessions on " + var2), (Throwable)var14);
            }

            this.cleanupDeadServer(var2);
         } catch (RemoteException var15) {
            if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
               ReplicationDetailsDebugLogger.debug((String)("Error creating secondary for batched sessions on " + var2), (Throwable)var15);
            }
         }

         var2 = this.getSecondarySelector().getSecondarySrvr();
         if (var2 != null) {
            this.secondaryHost = var2;
            if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
               ReplicationDetailsDebugLogger.debug("Recreating secondaries on " + var2);
            }

            this.resetSecondaryHost(var2, var1);
         }
      }

      this.secondaryHost = null;
      this.resetSecondaryHost(this.secondaryHost, var1);
      if (ReplicationDebugLogger.isDebugEnabled()) {
         ReplicationDebugLogger.debug("Unable to create secondarys for async batch on " + var2);
      }

   }

   private Set recreateList(ROID[] var1, HostID var2, Set var3) {
      if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
         ReplicationDetailsDebugLogger.debug("Recreating list for retry on secondary: " + var2);
      }

      HashSet var4 = new HashSet();

      for(int var5 = 0; var5 < var1.length; ++var5) {
         Iterator var6 = var3.iterator();

         while(var6.hasNext()) {
            AsyncUpdate var7 = (AsyncUpdate)var6.next();
            if (var7.getId().equals(var1[var5])) {
               this.resetSecondary(var2, var7);
               var4.add(var7);
            }
         }
      }

      return var4;
   }

   private void decrementUpdates(Set var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         AsyncUpdate var3 = (AsyncUpdate)var2.next();
         if (var3.isUpdate()) {
            WrappedRO var4 = this.wroMan.find(var3.getId());
            if (var4 != null) {
               var4.decrementVersion(var3.getRO().getKey());
            }
         }
      }

   }

   private void resetSecondaryHost(HostID var1, Set var2) {
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         AsyncUpdate var4 = (AsyncUpdate)var3.next();
         this.resetSecondary(var1, var4);
      }

   }

   private void resetSecondary(HostID var1, AsyncUpdate var2) {
      var2.recreate(LOCAL_HOSTID);
      WrappedRO var3 = this.wroMan.find(var2.getId());
      if (var3 == null) {
         if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
            ReplicationDetailsDebugLogger.debug("Problem setting new secondary for " + var2.getId());
         }

      } else {
         var3.setOtherHost(var1);
         var3.setOtherHostInfo(var1);
      }
   }

   private static class SingletonMaker {
      private static final AsyncReplicationManager singleton = new AsyncReplicationManager();
   }
}
