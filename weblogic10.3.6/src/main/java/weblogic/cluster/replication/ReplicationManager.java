package weblogic.cluster.replication;

import java.io.Externalizable;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.rmi.ConnectException;
import java.rmi.ConnectIOException;
import java.rmi.MarshalException;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.naming.NamingException;
import weblogic.cluster.ClusterExtensionLogger;
import weblogic.cluster.ClusterLogger;
import weblogic.cluster.ClusterService;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.ServerIdentity;
import weblogic.rjvm.PeerGoneException;
import weblogic.rmi.ServerShuttingDownException;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.extensions.RequestTimeoutException;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.rmi.spi.HostID;
import weblogic.server.ServiceFailureException;
import weblogic.server.channels.ChannelService;
import weblogic.utils.collections.NumericValueHashtable;
import weblogic.utils.collections.WeakConcurrentHashMap;

public class ReplicationManager implements ReplicationServices, ReplicationServicesInternal {
   private static final boolean DELL_FLAG = false;
   protected static final HostID LOCAL_HOSTID = LocalServerIdentity.getIdentity();
   static String[] replicationChannels;
   private static final int MAX_LOG_MESSAGES = 1000;
   private static int counter;
   protected final wroManager wroMan = new wroManager();
   protected static SecondarySelector selector;
   protected static ReplicationServiceLocator svcLocator;
   protected final Map cache = new WeakConcurrentHashMap(11);
   protected final Map cache2 = new WeakConcurrentHashMap(11);

   public static ReplicationManager theOne() {
      return ReplicationManager.SingletonMaker.singleton;
   }

   public static void start() throws ServiceFailureException {
      try {
         if (ClusterService.getClusterService().isReplicationTimeoutEnabled()) {
            ServerHelper.exportObject(theOne(), ClusterService.getClusterService().getHeartbeatTimeoutMillis());
         } else {
            ServerHelper.exportObject(theOne());
         }

         List var0 = ChannelService.getReplicationChannelNames();
         if (var0.size() > 0) {
            replicationChannels = new String[var0.size()];
            replicationChannels = (String[])var0.toArray(replicationChannels);
            ClusterExtensionLogger.logUsingMultipleChannelsForReplication(Arrays.toString(replicationChannels));
            if (ClusterService.getClusterService().useOneWayRMI()) {
               ClusterExtensionLogger.logUsingOneWayRMIForReplication();
            }
         } else if (ClusterService.getClusterService().useOneWayRMI()) {
            ClusterExtensionLogger.logIgnoringOneWayRMIWithoutMultipleChannels();
         }

      } catch (RemoteException var1) {
         throw new ServiceFailureException(var1.getMessage());
      }
   }

   public static void stop() {
      try {
         ServerHelper.unexportObject(theOne(), false);
      } catch (RemoteException var1) {
         throw new AssertionError("Failed to unexport replication system" + var1);
      }
   }

   protected ReplicationManager() {
      if (selector == null) {
         selector = LocalSecondarySelector.getSecondarySelector();
      }

      if (svcLocator == null) {
         svcLocator = new ReplicationServiceLocator();
      }

   }

   public static ReplicationServices services() {
      return theOne();
   }

   public final ROInfo register(Replicatable var1) {
      return this.add(ROID.create(), var1);
   }

   public ROInfo add(ROID var1, Replicatable var2) {
      WrappedRO var3 = this.wroMan.create(var2, var1, (byte)0, 0);
      this.createSecondary(var3, var2.getKey());
      return var3.getROInfo();
   }

   public final Replicatable lookup(ROID var1, Object var2) throws NotFoundException {
      WrappedRO var3 = this.getPrimary(var1, true, var2);
      return var3.getRO(var2);
   }

   public final Replicatable invalidationLookup(ROID var1, Object var2) throws NotFoundException {
      WrappedRO var3 = this.wroMan.find(var1);
      if (var3 != null) {
         return var3.getRO(var2);
      } else {
         throw new NotFoundException("Failed to located " + var1);
      }
   }

   public final Replicatable registerLocally(HostID var1, ROID var2, Object var3) throws RemoteException {
      if (ReplicationDebugLogger.isDebugEnabled()) {
         ReplicationDebugLogger.debug(var2, "Fetching replicatable object from remote server " + var1);
      }

      ReplicationServicesInternal var4 = this.getRepMan(var1);

      ROObject var5;
      try {
         var5 = var4.fetch(var2);
      } catch (NotFoundException var12) {
         return null;
      } catch (ConnectException var13) {
         return null;
      }

      resetTimeOut(var1);
      Map var6 = var5.getROS();
      if (var6.get(var3) == null) {
         return null;
      } else {
         WrappedRO var7 = this.wroMan.create((Replicatable)var6.get(var3), var2, (byte)0, var5.getVersion(var3));
         Iterator var8 = var6.entrySet().iterator();

         while(var8.hasNext()) {
            Map.Entry var9 = (Map.Entry)var8.next();
            Object var10 = var9.getKey();
            if (!var10.equals(var3)) {
               Replicatable var11 = (Replicatable)var9.getValue();
               var7.addRO(var11, var5.getVersion(var10));
            }
         }

         this.createSecondary(var7, (Object)null);
         resetTimeOut(var1);
         return var7.getRO(var3);
      }
   }

   public final void removeOrphanedSecondary(ROID var1, Object var2) {
      WrappedRO var3 = this.wroMan.remove(var1, var2);
      if (var3 != null) {
         if (ReplicationDebugLogger.isDebugEnabled()) {
            ReplicationDebugLogger.debug(var1, "Removed orphaned secondary");
         }
      } else if (ReplicationDebugLogger.isDebugEnabled()) {
         ReplicationDebugLogger.debug(var1, "Attempt to remove non-existent object");
      }

   }

   public final Object getSecondaryInfo(ROID var1) throws NotFoundException {
      WrappedRO var2 = this.getPrimary(var1, false, (Object)null);
      return var2.getSecondaryROInfo();
   }

   public final void unregister(ROID var1, Object var2) {
      this.unregister(new ROID[]{var1}, var2);
   }

   public void unregister(ROID[] var1, Object var2) {
      if (replicationChannels != null) {
         this.unregisterWithMultipleChannels(var1, var2);
      } else {
         this.unregisterWithoutMultipleChannels(var1, var2);
      }

   }

   private void unregisterWithMultipleChannels(ROID[] var1, Object var2) {
      HashMap var3 = new HashMap();
      ROID[] var4 = var1;
      int var5 = var1.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         ROID var7 = var4[var6];
         WrappedRO var8 = this.wroMan.remove(var7, var2);
         if (var8 != null) {
            HostID var9 = var8.getOtherHost();
            if (var9 != null) {
               CompositeKey var10 = new CompositeKey(var9, replicationChannels[var8.channelIndex]);
               ArrayList var11 = (ArrayList)var3.get(var10);
               if (var11 == null) {
                  var11 = new ArrayList();
                  var3.put(var10, var11);
               }

               var11.add(var7);
            }
         }
      }

      Iterator var13 = var3.entrySet().iterator();

      while(var13.hasNext()) {
         Map.Entry var14 = (Map.Entry)var13.next();
         ArrayList var15 = (ArrayList)var14.getValue();
         var4 = new ROID[var15.size()];

         try {
            ReplicationServicesInternal var16 = this.getRepMan(((CompositeKey)var14.getKey()).hostID, ((CompositeKey)var14.getKey()).channelName);
            if (ClusterService.getClusterService().useOneWayRMI()) {
               if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
                  ReplicationDetailsDebugLogger.debug("Using Multi-Channels for 1-way removeOneWay() on channel " + ((CompositeKey)var14.getKey()).channelName);
               }

               var16.removeOneWay((ROID[])var15.toArray(var4), var2);
            } else {
               if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
                  ReplicationDetailsDebugLogger.debug("Using Multiple-Channels for 2-way remove() on channel " + ((CompositeKey)var14.getKey()).channelName);
               }

               var16.remove((ROID[])var15.toArray(var4), var2);
            }

            resetTimeOut(((CompositeKey)var14.getKey()).hostID);
         } catch (RemoteException var12) {
            if (ReplicationDebugLogger.isDebugEnabled()) {
               ReplicationDebugLogger.debug("Unable to reach " + ((CompositeKey)var14.getKey()).hostID + " to remove roids: " + Arrays.asList((Object[])var4));
            }
         }
      }

   }

   private void unregisterWithoutMultipleChannels(ROID[] var1, Object var2) {
      HashMap var3 = new HashMap();
      ROID[] var4 = var1;
      int var5 = var1.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         ROID var7 = var4[var6];
         WrappedRO var8 = this.wroMan.remove(var7, var2);
         if (var8 != null) {
            HostID var9 = var8.getOtherHost();
            if (var9 != null) {
               ArrayList var10 = (ArrayList)var3.get(var9);
               if (var10 == null) {
                  var10 = new ArrayList();
                  var3.put(var9, var10);
               }

               var10.add(var7);
            }
         }
      }

      Iterator var12 = var3.entrySet().iterator();

      while(var12.hasNext()) {
         Map.Entry var13 = (Map.Entry)var12.next();
         ArrayList var14 = (ArrayList)var13.getValue();
         var4 = new ROID[var14.size()];

         try {
            ReplicationServicesInternal var15 = this.getRepMan((HostID)var13.getKey());
            if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
               ReplicationDetailsDebugLogger.debug("Using Single-Channel for 2-way remove()");
            }

            var15.remove((ROID[])var14.toArray(var4), var2);
            resetTimeOut((HostID)var13.getKey());
         } catch (RemoteException var11) {
            if (ReplicationDebugLogger.isDebugEnabled()) {
               ReplicationDebugLogger.debug("Unable to reach " + var13.getKey() + " to remove roids: " + Arrays.asList((Object[])var4));
            }
         }
      }

   }

   public Object updateSecondary(ROID var1, Serializable var2, Object var3) throws NotFoundException {
      WrappedRO var4;
      if (var2 == null) {
         var4 = this.getPrimary(var1, true, var3);
         return var4.getSecondaryROInfo();
      } else {
         var4 = this.getPrimary(var1, false, var3);
         HostID var5 = var4.getOtherHost();
         if (var5 != null) {
            try {
               this.sendUpdateRequestToSecondary(var4, var1, var2, var3);
               resetTimeOut(var5);
               if (ReplicationDebugLogger.isDebugEnabled()) {
                  ReplicationDebugLogger.debug(var1, "Secondary server " + var5);
               }

               return var4.getSecondaryROInfo();
            } catch (MarshalException var7) {
               if (ReplicationDebugLogger.isDebugEnabled()) {
                  ReplicationDebugLogger.debug((String)("MarshalException updating secondary for " + var1 + ", key " + var3 + ", on " + var5), (Throwable)var7);
               }

               ClusterLogger.logUnableToUpdateNonSerializableObject(var7);
               if (var7.detail instanceof NotSerializableException) {
                  var4.decrementVersion(var3);
                  return var4.getSecondaryROInfo();
               }
            } catch (RemoteException var8) {
               if (ReplicationDebugLogger.isDebugEnabled()) {
                  ReplicationDebugLogger.debug(var1, "Error updating secondary for " + var1 + ", key " + var3 + ", on " + var5, var8);
               }
            } catch (NotFoundException var9) {
               if (ReplicationDebugLogger.isDebugEnabled()) {
                  ReplicationDebugLogger.debug(var1, "Error updating secondary for " + var1 + ", key " + var3 + ", on " + var5 + ". Re-creating secondary.", var9);
               }
            } catch (Exception var10) {
               if (ReplicationDebugLogger.isDebugEnabled()) {
                  ReplicationDebugLogger.debug(var1, "Error updating secondary for " + var1 + ", key " + var3 + ", on " + var5, var10);
               }
            }

            var4.setOtherHost((HostID)null);
            var4.setOtherHostInfo((Object)null);
         }

         this.createSecondary(var4, (Object)null);
         return var4.getSecondaryROInfo();
      }
   }

   private void sendUpdateRequestToSecondary(WrappedRO var1, ROID var2, Serializable var3, Object var4) throws RemoteException, NotFoundException {
      HostID var5 = var1.getOtherHost();
      int var6 = var1.incrementVersion(var4);
      if (replicationChannels != null) {
         int var7 = var1.channelIndex;

         assert var7 != -1;

         ReplicationServicesInternal var8 = this.getRepMan(var5, replicationChannels[var7]);
         if (ClusterService.getClusterService().useOneWayRMI()) {
            if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
               ReplicationDetailsDebugLogger.debug(var2, "Using Multi-Channels for 1-way updateOneWay() on channel " + replicationChannels[var7]);
            }

            var8.updateOneWay(var2, var6, var3, var4);
         } else {
            if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
               ReplicationDetailsDebugLogger.debug(var2, "Using Multiple-Channels for 2-way update() on channel" + replicationChannels[var7]);
            }

            var8.update(var2, var6, var3, var4);
         }
      } else {
         if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
            ReplicationDetailsDebugLogger.debug(var2, "Using Single-Channel for 2-way update()");
         }

         ReplicationServicesInternal var9 = this.getRepMan(var5);
         var9.update(var2, var6, var3, var4);
      }

   }

   protected ReplicationServicesInternal getRepMan(HostID var1, String var2) throws RemoteException {
      if (var2 != null && var2.length() != 0) {
         CompositeKey var3 = new CompositeKey(var1, var2);
         ReplicationServicesInternal var4 = (ReplicationServicesInternal)this.cache2.get(var3);
         if (var4 == null) {
            var4 = this.getRepManWithChannelName(var1, var2);
            if (ReplicationDebugLogger.isDebugEnabled()) {
               ReplicationDebugLogger.debug("Created new stub for hostID " + var1 + " using channel " + var2);
            }

            this.cache2.put(var3, var4);
         }

         return var4;
      } else {
         return this.getRepMan(var1);
      }
   }

   protected ReplicationServicesInternal getRepManWithChannelName(HostID var1, String var2) throws RemoteException {
      try {
         ReplicationServicesInternal var3 = svcLocator.replicationServicesLookup((ServerIdentity)var1, var2, ReplicationManager.class);
         return var3;
      } catch (NamingException var5) {
         throw new RemoteException(var5.getMessage(), var5);
      }
   }

   protected ReplicationServicesInternal getRepMan(HostID var1) throws RemoteException {
      if (var1.isLocal()) {
         return this;
      } else {
         ReplicationServicesInternal var2 = (ReplicationServicesInternal)this.cache.get(var1);
         if (var2 == null) {
            try {
               var2 = svcLocator.replicationServicesLookup((ServerIdentity)var1, ReplicationManager.class);
            } catch (NamingException var4) {
               throw new RemoteException(var4.getMessage(), var4);
            }

            this.cache.put(var1, var2);
         }

         return var2;
      }
   }

   protected WrappedRO getPrimary(ROID var1, boolean var2, Object var3) throws NotFoundException {
      WrappedRO var4 = this.wroMan.find(var1);
      if (var4 == null || var3 != null && var4.getRO(var3) == null) {
         String var5 = "Unable to find object for roid:" + var1;
         if (ReplicationDebugLogger.isDebugEnabled()) {
            ReplicationDebugLogger.debug(var1, var5);
         }

         throw new NotFoundException(var5);
      } else {
         var4.ensureStatus((byte)0);
         if (var2 && var4.getOtherHost() == null) {
            this.createSecondary(var4, (Object)null);
         }

         return var4;
      }
   }

   protected void createSecondary(WrappedRO var1, Object var2) {
      HostID var3 = var1.getOtherHost();
      if (var3 == null) {
         var3 = this.getSecondarySelector().getSecondarySrvr();
      }

      Object var4 = var2;

      try {
         while(var3 != null) {
            if (this.trySecondary(var1, var3, var4)) {
               return;
            }

            var4 = null;
            var3 = this.getSecondarySelector().getSecondarySrvr();
         }
      } catch (ApplicationUnavailableException var6) {
         if (ReplicationDebugLogger.isDebugEnabled()) {
            ReplicationDebugLogger.debug(var1.getID(), "Failed to create secondary as secondary " + var3 + " doesn't have the " + "application ready; Will try other hosts", var6);
         }

         if (this.trySecondaryOnOtherServers(var1)) {
            return;
         }
      }

      var1.setOtherHost((HostID)null);
      var1.setOtherHostInfo((Object)null);
      if (ReplicationDebugLogger.isDebugEnabled()) {
         ReplicationDebugLogger.debug(var1.getID(), "Unable to create secondary on " + var3);
      }

   }

   protected boolean trySecondary(WrappedRO var1, HostID var2, Object var3) {
      try {
         ReplicationServicesInternal var4;
         try {
            var4 = this.getRepMan(var2);
         } catch (RemoteException var8) {
            this.cleanupDeadServer(var2);
            throw var8;
         }

         if (var3 == null) {
            Iterator var5 = var1.keys();

            while(var5.hasNext()) {
               Object var6 = var5.next();
               int var7 = var1.getVersion(var6);
               var1.setOtherHostInfo(var4.create(LOCAL_HOSTID, var7, var1.getID(), var1.getRO(var6)));
            }
         } else {
            int var18 = var1.getVersion(var3);
            var1.setOtherHostInfo(var4.create(LOCAL_HOSTID, var18, var1.getID(), var1.getRO(var3)));
         }

         var1.setOtherHost(var2);
         resetTimeOut(var2);
         if (ReplicationDebugLogger.isDebugEnabled()) {
            ReplicationDebugLogger.debug(var1.getID(), "Created secondary on " + var2);
         }

         return true;
      } catch (ConnectException var9) {
         if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
            ReplicationDetailsDebugLogger.debug(var1.getID(), "Failed to reach secondary server " + var2 + " trying to create secondary", var9);
         } else if (ReplicationDebugLogger.isDebugEnabled()) {
            ReplicationDebugLogger.debug(var1.getID(), "Failed to reach secondary server " + var2 + " trying to create secondary");
         }

         this.cleanupDeadServer(var2);
         return false;
      } catch (ServerShuttingDownException var10) {
         if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
            ReplicationDetailsDebugLogger.debug(var1.getID(), "Secondary server " + var2 + " is shutting down. Failed to create secondary.", var10);
         } else if (ReplicationDebugLogger.isDebugEnabled()) {
            ReplicationDebugLogger.debug(var1.getID(), "Secondary server " + var2 + " is shutting down. Failed to create secondary");
         }

         this.cleanupDeadServer(var2);
         return false;
      } catch (ConnectIOException var11) {
         if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
            ReplicationDetailsDebugLogger.debug(var1.getID(), "ConnectIOException while trying to connect to secondary server " + var2 + " for creating secondary", var11);
         } else if (ReplicationDebugLogger.isDebugEnabled()) {
            ReplicationDebugLogger.debug(var1.getID(), "ConnectIOException while trying to connect to secondary server " + var2 + " for creating secondary");
         }

         this.cleanupDeadServer(var2);
         return false;
      } catch (MarshalException var12) {
         if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
            ReplicationDetailsDebugLogger.debug((String)"Marshalling error", (Throwable)var12);
         }

         ClusterLogger.logUnableToUpdateNonSerializableObject(var12);
         return true;
      } catch (PeerGoneException var13) {
         if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
            ReplicationDetailsDebugLogger.debug(var1.getID(), "Error creating secondary on " + var2, var13);
         } else if (ReplicationDebugLogger.isDebugEnabled()) {
            ReplicationDebugLogger.debug(var1.getID(), "PeerGoneException while creating secondary on " + var2);
         }

         return false;
      } catch (UnmarshalException var14) {
         if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
            ReplicationDetailsDebugLogger.debug((String)"Unmarshalling error", (Throwable)var14);
         }

         ClusterLogger.logUnableToUpdateNonSerializableObject(var14);
         return true;
      } catch (RemoteRuntimeException var15) {
         ClusterExtensionLogger.logUnexpectedExceptionDuringReplication(var15.getCause());
         return true;
      } catch (RequestTimeoutException var16) {
         if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
            ReplicationDetailsDebugLogger.debug(var1.getID(), "Error creating secondary on " + var2, var16);
         } else if (ReplicationDebugLogger.isDebugEnabled()) {
            ReplicationDebugLogger.debug(var1.getID(), "RequestTimeoutException while creating secondary on " + var2);
         }

         return false;
      } catch (RemoteException var17) {
         if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
            ReplicationDetailsDebugLogger.debug(var1.getID(), "Error creating secondary on " + var2, var17);
         } else if (ReplicationDebugLogger.isDebugEnabled()) {
            ReplicationDebugLogger.debug(var1.getID(), "RemoteException while creating secondary on " + var2);
         }

         return false;
      }
   }

   protected void cleanupDeadServer(HostID var1) {
      this.cache.remove(var1);
      this.getSecondarySelector().removeDeadSecondarySrvr(var1);
   }

   private boolean trySecondaryOnOtherServers(WrappedRO var1) {
      Iterator var2 = this.getSecondarySelector().getSecondaryCandidates().iterator();

      while(var2.hasNext()) {
         HostID var3 = (HostID)var2.next();

         try {
            if (this.trySecondary(var1, var3, (Object)null)) {
               return true;
            }
         } catch (ApplicationUnavailableException var5) {
            if (ReplicationDebugLogger.isDebugEnabled()) {
               ReplicationDebugLogger.debug(var1.getID(), "Failed to create secondary as secondary " + var3 + " doesn't have the application ready ", var5);
            }
         }
      }

      return false;
   }

   public final Object create(HostID var1, int var2, ROID var3, Replicatable var4) throws RemoteException {
      if (var4 == null) {
         throw new RemoteException("Got a Null replicatable for id " + var3 + " version " + var2 + " from primary " + var1.toString());
      } else {
         resetTimeOut(var1);
         WrappedRO var5 = this.wroMan.find(var3);
         if (var5 != null) {
            int var6 = var5.getVersion(var4.getKey());
            if (var6 > var2) {
               if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
                  ReplicationDetailsDebugLogger.debug(var3, "Received stale update request with new version " + var2 + " from primary: " + var1 + " Existing Object info" + "\n\totherhost " + (var5.getOtherHost() != null ? var5.getOtherHost() : "null") + "\n\tcurrent version " + var6);
               }

               ClusterLogger.logStaleReplicationRequest(var3.toString());
               return null;
            }
         }

         var5 = this.wroMan.create(var4, var3, (byte)1, var2);
         var5.setOtherHost(var1);
         Object var7 = var5.getSecondaryROInfo();
         return var7;
      }
   }

   public void update(ROID var1, int var2, Serializable var3, Object var4) throws NotFoundException, RemoteException {
      this.updateInternal("update", var1, var2, var3, var4);
   }

   public void updateOneWay(ROID var1, int var2, Serializable var3, Object var4) throws NotFoundException, RemoteException {
      this.updateInternal("updateOneWay", var1, var2, var3, var4);
   }

   private void updateInternal(String var1, ROID var2, int var3, Serializable var4, Object var5) throws NotFoundException {
      WrappedRO var6 = this.wroMan.find(var2);
      if (var6 == null) {
         throw new NotFoundException("Unable to find " + var2);
      } else {
         HostID var7 = var6.getOtherHost();
         if (var7 != null) {
            resetTimeOut(var7);
         }

         int var8 = var6.getVersion(var5);
         if (var8 > var3) {
            ClusterLogger.logStaleReplicationRequest(var2.toString());
            if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
               ReplicationDetailsDebugLogger.debug(var6.getID(), "Got stale replication request for UPDATE with version " + var3 + " from " + var6.getOtherHost());
            }
         } else if (var8 == -1) {
            if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
               ReplicationDetailsDebugLogger.debug(var2, "RO not found for key " + var5);
            }
         } else {
            int var9 = var3 - var8;
            if (var9 != 1) {
               if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
                  ReplicationDetailsDebugLogger.debug("Missed " + var9 + " updates for " + var2.toString() + ", key " + var5 + ", update version: " + var3 + ", current version: " + var8);
               }

               if (var1.equals("update")) {
                  ClusterLogger.logReplicationVersionMismatch(var9, var2 + "; update version: " + var3 + ", current version: " + var8);
                  throw new NotFoundException("Lost " + var9 + " updates of " + var2);
               }

               if (counter < 1000) {
                  ++counter;
                  ClusterExtensionLogger.logOutOfOrderUpdateOneWayRequest();
               }
            } else {
               var6.ensureStatus((byte)1);
               var6.incrementVersion(var5);
               Replicatable var10 = var6.getRO(var5);
               if (var10 == null) {
                  if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
                     ReplicationDetailsDebugLogger.debug("Remote object was null for key " + var5 + ", for version update " + var3 + " from " + var6.getOtherHost() + "for replication object " + var2.toString());
                  }

                  throw new AssertionError("Found the session for " + var2 + " but not the application for " + var5 + ". Double-check that proxy/loadbalancers are respecting " + "session stickiness.");
               }

               var10.update(var2, var4);
               if (ReplicationDebugLogger.isDebugEnabled()) {
                  ReplicationDebugLogger.debug("Updated local secondary with version " + var3 + " from " + var6.getOtherHost() + "for replication object " + var2.toString() + ", key " + var5 + ", ro = " + var10.toString());
               }
            }
         }

      }
   }

   public void update(AsyncBatch var1) throws RemoteException {
      throw new UnsupportedOperationException("ReplicationManager should not take batched updates");
   }

   public final void remove(ROID[] var1, Object var2) throws RemoteException {
      this.removeInternal("remove", var1, var2);
   }

   public final void removeOneWay(ROID[] var1, Object var2) throws RemoteException {
      this.removeInternal("removeOneWay", var1, var2);
   }

   private void removeInternal(String var1, ROID[] var2, Object var3) throws RemoteException {
      for(int var4 = 0; var4 < var2.length; ++var4) {
         WrappedRO var5 = this.wroMan.find(var2[var4]);
         if (var5 != null && var5.getStatus() != 0) {
            HostID var6 = var5.getOtherHost();
            if (var6 != null) {
               resetTimeOut(var6);
            }

            this.wroMan.remove(var2[var4], var3);
            if (ReplicationDebugLogger.isDebugEnabled()) {
               ReplicationDebugLogger.debug("Removed secondary for roids: " + var2[var4]);
            }
         } else if (ReplicationDebugLogger.isDebugEnabled()) {
            if (var5 != null && var5.getStatus() == 0) {
               ReplicationDebugLogger.debug("Attempt to remove current primary which is old secondary: " + var2[var4]);
            } else {
               ReplicationDebugLogger.debug("Attempt to remove non-existent object for roids: " + var2[var4]);
            }
         }
      }

   }

   public final void remove(ROID[] var1) throws RemoteException {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         WrappedRO var3 = this.wroMan.find(var1[var2]);
         if (var3 != null) {
            var3.ensureStatus((byte)1);
            this.wroMan.removeAll(var1[var2]);
            if (ReplicationDebugLogger.isDebugEnabled()) {
               ReplicationDebugLogger.debug("Removed migrated secondary for roids: " + var1[var2]);
            }
         } else if (ReplicationDebugLogger.isDebugEnabled()) {
            ReplicationDebugLogger.debug("Attempt to remove non-existent object for roids: " + var1[var2]);
         }
      }

   }

   public final ROObject fetch(ROID var1) throws RemoteException, NotFoundException {
      if (ReplicationDetailsDebugLogger.isDebugEnabled()) {
         ReplicationDetailsDebugLogger.debug("Fetching " + var1);
      }

      WrappedRO var2 = this.wroMan.find(var1);
      if (var2 != null) {
         return new ROObject(var2.getMap(), var2.getVersionMap(), var2.getVersion());
      } else {
         throw new NotFoundException("Failed to locate ROID " + var1);
      }
   }

   public final Iterator<WrappedRO> ids() {
      return this.wroMan.iterator();
   }

   public final long getPrimaryCount() {
      int var1 = 0;
      Iterator var2 = this.wroMan.iterator();

      while(var2.hasNext()) {
         WrappedRO var3 = (WrappedRO)var2.next();
         if (var3.getStatus() == 0) {
            ++var1;
         }
      }

      return (long)var1;
   }

   public final long getSecondaryCount() {
      int var1 = 0;
      Iterator var2 = this.wroMan.iterator();

      while(var2.hasNext()) {
         WrappedRO var3 = (WrappedRO)var2.next();
         if (var3.getStatus() == 1) {
            ++var1;
         }
      }

      return (long)var1;
   }

   public final String[] getSecondaryDistributionNames() {
      NumericValueHashtable var1 = new NumericValueHashtable();
      Iterator var2 = this.wroMan.iterator();

      while(var2.hasNext()) {
         WrappedRO var3 = (WrappedRO)var2.next();
         if (var3.getStatus() == 1) {
            HostID var4 = var3.getOtherHost();
            if (var4 != null) {
               if (!var1.containsKey(var4)) {
                  var1.put(var4, 1L);
               } else {
                  var1.put(var4, var1.get(var4) + 1L);
               }
            }
         }
      }

      ArrayList var8 = new ArrayList();
      Iterator var9 = this.getSecondarySelector().getSecondaryCandidates().iterator();

      while(var9.hasNext()) {
         ServerIdentity var5 = (ServerIdentity)var9.next();
         long var6 = var1.get(var5);
         if (var6 > 0L) {
            var8.add(var5.getServerName() + " : " + var6);
         }
      }

      String[] var10 = new String[var8.size()];

      for(int var11 = 0; var11 < var8.size(); ++var11) {
         var10[var11] = (String)var8.get(var11);
      }

      return var10;
   }

   protected SecondarySelector getSecondarySelector() {
      return selector;
   }

   protected static void resetTimeOut(HostID var0) {
   }

   public static final class ROObject implements Externalizable {
      private static final long serialVersionUID = -5018057544806950295L;
      private int version;
      private Map ros;
      private Map<Object, Integer> roVersions;

      public ROObject() {
      }

      ROObject(Map var1, Map<Object, Integer> var2, int var3) {
         this.ros = var1;
         this.version = var3;
         this.roVersions = var2;
      }

      final int getVersion() {
         return this.version;
      }

      final int getVersion(Object var1) {
         if (this.roVersions != null) {
            Integer var2 = (Integer)this.roVersions.get(var1);
            if (var2 != null) {
               return var2;
            }
         }

         return this.version;
      }

      final Map<Object, Integer> getVersions() {
         return this.roVersions;
      }

      final Map getROS() {
         return this.ros;
      }

      public final void writeExternal(ObjectOutput var1) throws IOException {
         var1.writeObject(this.ros);
         var1.writeInt(this.version);
         var1.writeObject(this.roVersions);
      }

      public final void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
         this.ros = (Map)var1.readObject();
         this.version = var1.readInt();
         if (var1 instanceof PeerInfoable) {
            PeerInfo var2 = ((PeerInfoable)var1).getPeerInfo();
            if (var2.compareTo(PeerInfo.VERSION_1030) > 0) {
               this.roVersions = (Map)var1.readObject();
            }
         }

      }
   }

   private static class CompositeKey {
      private final HostID hostID;
      private final String channelName;
      private final int hashcode;

      CompositeKey(HostID var1, String var2) {
         this.hostID = var1;
         this.channelName = var2;
         this.hashcode = var1.hashCode() ^ var2.hashCode();
      }

      public int hashCode() {
         return this.hashcode;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (!(var1 instanceof CompositeKey)) {
            return false;
         } else {
            CompositeKey var2 = (CompositeKey)var1;
            return (this.channelName == var2.channelName || this.channelName != null && this.channelName.equals(var2.channelName)) && (this.hostID == var2.hostID || this.hostID != null && this.hostID.equals(var2.hostID));
         }
      }
   }

   protected static final class wroManager {
      private final Map<ROID, WrappedRO> wros = new ConcurrentHashMap(317);

      public WrappedRO create(Replicatable var1, ROID var2, byte var3, int var4) {
         WrappedRO var5;
         synchronized(var2) {
            var5 = (WrappedRO)this.wros.get(var2);
            if (var5 == null) {
               if (ReplicationDebugLogger.isDebugEnabled()) {
                  ReplicationDebugLogger.debug(var2, "Creating " + (var3 == 0 ? "primary " : "secondary ") + " for application key " + (var1 != null ? var1.getKey() : "null"));
               }

               var5 = new WrappedRO(var1, var2, var3, var4);
               this.wros.put(var2, var5);
               return var5;
            }
         }

         var5.ensureStatus(var3);
         if (ReplicationDebugLogger.isDebugEnabled()) {
            ReplicationDebugLogger.debug(var2, "Found " + (var3 == 0 ? "primary " : "secondary ") + " for application key " + (var1 != null ? var1.getKey() : "null"));
         }

         int var6 = var5.getVersion(var1.getKey());
         if (var6 == -1) {
            var5.addRO(var1, var4);
         } else {
            var5.addRO(var1, var6);
         }

         return var5;
      }

      public WrappedRO remove(ROID var1, Object var2) {
         WrappedRO var3;
         Replicatable var4;
         synchronized(var1) {
            var3 = (WrappedRO)this.wros.get(var1);
            if (var3 == null) {
               return null;
            }

            if (ReplicationDebugLogger.isDebugEnabled()) {
               ReplicationDebugLogger.debug(var3.getID(), "Removing " + (var3.getStatus() == 0 ? "primary " : "secondary ") + " for key " + var2);
            }

            var4 = var3.getRO(var2);
            if (var2 == null || var3.removeRO(var2)) {
               this.wros.remove(var3.getID());
            }
         }

         if (var4 != null && var3.getStatus() == 1) {
            var4.becomeUnregistered(var1);
         }

         return var3;
      }

      public WrappedRO removeAll(ROID var1) {
         synchronized(var1) {
            WrappedRO var3 = (WrappedRO)this.wros.remove(var1);
            if (var3 == null) {
               return null;
            } else {
               if (ReplicationDebugLogger.isDebugEnabled()) {
                  ReplicationDebugLogger.debug(var3.getID(), "Removing " + (var3.getStatus() == 0 ? "primary " : "secondary "));
               }

               var3.removeAll();
               return var3;
            }
         }
      }

      public WrappedRO find(ROID var1) {
         synchronized(var1) {
            return (WrappedRO)this.wros.get(var1);
         }
      }

      public Iterator<WrappedRO> iterator() {
         return this.wros.values().iterator();
      }
   }

   private static class SingletonMaker {
      private static final ReplicationManager singleton = new ReplicationManager();
   }
}
