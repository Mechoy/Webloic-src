package weblogic.cluster.replication;

import java.rmi.RemoteException;
import java.security.AccessController;
import javax.naming.NamingException;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.ServerIdentity;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.rmi.spi.HostID;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class MANAsyncReplicationManager extends AsyncReplicationManager {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private RemoteClusterSecondarySelector remoteSelector;

   public static ReplicationManager theOne() {
      return MANAsyncReplicationManager.SingletonMaker.singleton;
   }

   public static ReplicationServices services() {
      return theOne();
   }

   public static void start() {
      try {
         ServerHelper.exportObject(theOne());
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
   }

   private MANAsyncReplicationManager() {
      this.remoteSelector = RemoteClusterSecondarySelector.getSecondarySelector();
   }

   protected void initializeRuntime() {
      try {
         new MANAsyncReplicationRuntime(ManagementService.getRuntimeAccess(kernelId).getServerName(), this);
      } catch (ManagementException var2) {
         throw new AssertionError(var2);
      }
   }

   protected SecondarySelector getSecondarySelector() {
      return this.remoteSelector;
   }

   protected ReplicationServicesInternal getRepMan(HostID var1) throws RemoteException {
      if (var1.isLocal()) {
         return this;
      } else {
         ReplicationServicesInternal var2 = (ReplicationServicesInternal)this.cache.get(var1);
         if (var2 == null) {
            try {
               var2 = svcLocator.replicationServicesLookup((ServerIdentity)var1, this.remoteSelector.getReplicationChannelFor(var1), MANAsyncReplicationManager.class);
            } catch (NamingException var4) {
               throw new RemoteException(var4.getMessage(), var4);
            }

            this.cache.put(var1, var2);
         }

         return var2;
      }
   }

   // $FF: synthetic method
   MANAsyncReplicationManager(Object var1) {
      this();
   }

   private static class SingletonMaker {
      private static final MANAsyncReplicationManager singleton = new MANAsyncReplicationManager();
   }
}
