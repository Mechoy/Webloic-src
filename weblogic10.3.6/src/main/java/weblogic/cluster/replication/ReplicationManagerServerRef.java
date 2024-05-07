package weblogic.cluster.replication;

import java.rmi.RemoteException;
import java.security.AccessController;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import weblogic.cluster.ClusterExtensionLogger;
import weblogic.cluster.ClusterLogger;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelManager;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.internal.BasicServerRef;
import weblogic.rmi.spi.EndPoint;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.channels.ChannelService;
import weblogic.utils.collections.LRUCacheHashMap;
import weblogic.work.ExecuteQueueFactory;
import weblogic.work.ServerWorkManagerFactory;
import weblogic.work.WorkManager;

public class ReplicationManagerServerRef extends BasicServerRef {
   private static final String REP_CHANNEL_NAME;
   private static ServerChannel replicationChannel;
   private static Map<ServerChannel, WorkManager> dispatchPolicyMapping;
   private static boolean USE_SSL;
   private static final String SERVER_NAME;
   private static final String CLUSTER_NAME;
   private static final int MAX_CACHE = 512;
   private static final Object PRIVILDEGED = new Object();
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static Map<AuthenticatedSubject, Object> cache = Collections.synchronizedMap(new LRUCacheHashMap(512));

   public ReplicationManagerServerRef(int var1, Object var2) throws RemoteException {
      super(var1, var2);
   }

   private static void initialize(ServerMBean var0) {
      List var1 = ChannelService.getReplicationChannelNames();
      if (var1.size() > 0) {
         String[] var2 = new String[var1.size()];
         var2 = (String[])var1.toArray(var2);
         dispatchPolicyMapping = new HashMap();
         String[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            ServerChannel var7 = ServerChannelManager.findLocalServerChannel(var6);
            if (var7 == null) {
               ClusterExtensionLogger.logNoChannelForReplicationCalls(CLUSTER_NAME);
               throw new AssertionError("Can't find replication server channel '" + var6 + "' for " + SERVER_NAME);
            }

            WorkManager var8;
            if (var0.getUse81StyleExecuteQueues()) {
               var8 = ExecuteQueueFactory.createExecuteQueue(var6, 1);
            } else {
               var8 = ServerWorkManagerFactory.createExecuteQueue(var6, 1);
            }

            dispatchPolicyMapping.put(var7, var8);
         }
      }

      replicationChannel = ServerChannelManager.findLocalServerChannel(REP_CHANNEL_NAME);
      if (replicationChannel != null) {
         USE_SSL = replicationChannel.getProtocol().isSecure();
      } else {
         replicationChannel = ServerChannelManager.findLocalServerChannel(USE_SSL ? ProtocolManager.getDefaultSecureProtocol() : ProtocolManager.getDefaultProtocol());
      }

      if (replicationChannel == null) {
         ClusterExtensionLogger.logNoChannelForReplicationCalls(CLUSTER_NAME);
         throw new AssertionError("No replication server channel for " + SERVER_NAME);
      }
   }

   private void reqReceivedOnReplicationChannel(ServerChannel var1) throws RemoteException {
      if (dispatchPolicyMapping == null || !dispatchPolicyMapping.containsKey(var1)) {
         if (!replicationChannel.equals(var1)) {
            ClusterLogger.logWrongChannelForReplicationCalls(CLUSTER_NAME, var1.getChannelName());
            SecurityException var2 = new SecurityException("Incorrect channel used for replication " + var1);
            throw new RemoteException(var2.getMessage(), var2);
         }
      }
   }

   protected WorkManager getWorkManager(ServerChannel var1, RuntimeMethodDescriptor var2, AuthenticatedSubject var3) {
      WorkManager var4;
      if (replicationChannel.equals(var1)) {
         var4 = super.getWorkManager(var2, var3);
      } else {
         var4 = (WorkManager)dispatchPolicyMapping.get(var1);
      }

      return var4;
   }

   protected void checkPriviledges(AuthenticatedSubject var1, ServerChannel var2, EndPoint var3) throws RemoteException {
      SecurityException var4;
      if (var2 == null) {
         var4 = new SecurityException("ServerChannel is null");
         throw new RemoteException(var4.getMessage(), var4);
      } else {
         this.reqReceivedOnReplicationChannel(var2);
         if (var1 == null) {
            var4 = new SecurityException("AuthenticatedSubject is null");
            throw new RemoteException(var4.getMessage(), var4);
         } else if (doesCacheContains(var1)) {
            getFromCache(var1);
         } else {
            verifyAndCachePriviledgesFor(var1);
         }
      }
   }

   private static boolean doesCacheContains(AuthenticatedSubject var0) {
      return cache.containsKey(var0);
   }

   private static void getFromCache(AuthenticatedSubject var0) throws RemoteException {
      Object var1 = cache.get(var0);
      if (var1 instanceof RemoteException) {
         throw (RemoteException)var1;
      }
   }

   private static void verifyAndCachePriviledgesFor(AuthenticatedSubject var0) throws RemoteException {
      try {
         SecureReplicationInvocationHandler.checkPriviledges(var0, USE_SSL);
      } catch (SecurityException var3) {
         RemoteException var2 = new RemoteException(var3.getMessage(), var3);
         cache.put(var0, var2);
         throw var2;
      }

      cache.put(var0, PRIVILDEGED);
   }

   static {
      ServerMBean var0 = ManagementService.getRuntimeAccess(KERNEL_ID).getServer();
      SERVER_NAME = var0.getName();
      ClusterMBean var1 = var0.getCluster();
      if (var1 == null) {
         throw new AssertionError("Can't have replication without a cluster");
      } else {
         CLUSTER_NAME = var1.getName();
         REP_CHANNEL_NAME = var1.getReplicationChannel();
         USE_SSL = var1.isSecureReplicationEnabled();
         initialize(var0);
      }
   }
}
