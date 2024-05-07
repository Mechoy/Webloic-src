package weblogic.cluster.leasing.databaseless;

import java.io.IOException;
import java.util.Set;
import weblogic.cluster.messaging.internal.ClusterMessageFactory;
import weblogic.cluster.messaging.internal.ClusterMessageSender;
import weblogic.cluster.messaging.internal.DebugLogger;
import weblogic.cluster.messaging.internal.ServerInformation;
import weblogic.cluster.singleton.ClusterReformationInProgressException;
import weblogic.cluster.singleton.LeasingBasis;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public final class LeaseClient implements LeasingBasis, ClusterStateChangeListener {
   private static final DebugCategory debugLeaseClient = Debug.getCategory("weblogic.cluster.leasing.LeaseClient");
   private static final boolean DEBUG = debugEnabled();
   private boolean clusterFormationInProgress = true;

   public LeaseClient() {
      ClusterState.getInstance().addStateChangeListener(this);
   }

   public boolean acquire(String var1, String var2, int var3) throws IOException {
      ServerInformation var4 = ClusterLeaderService.getLeader();
      if (var4 == null) {
         if (DEBUG) {
            debug("Cluster leader is not present ! refuse lease acquisition for " + var1);
         }

         return false;
      } else {
         ClusterMessageSender var5 = ClusterMessageFactory.getInstance().getDefaultMessageSender();
         LeaseMessage var6 = new LeaseMessage(var1, var2, var3);
         if (DEBUG) {
            debug("requesting new lease " + var6 + " from LeaseServer " + var4);
         }

         LeaseResponse var7 = (LeaseResponse)var5.send(var6, (ServerInformation)var4);
         if (DEBUG) {
            debug("received response to the lease message " + var7);
         }

         boolean var8 = (Boolean)var7.getResult();
         if (var8 && EnvironmentFactory.getClusterMember().getLeaseView() != null) {
            EnvironmentFactory.getClusterMember().getLeaseView().leaseAcquiredByLocalServer(var1, var3);
         }

         return var8;
      }
   }

   public void release(String var1, String var2) throws IOException {
      ServerInformation var3 = ClusterLeaderService.getLeader();
      if (var3 == null) {
         if (DEBUG) {
            debug("Cluster leader is not present ! refuse lease release for " + var1);
         }

      } else {
         ClusterMessageSender var4 = ClusterMessageFactory.getInstance().getDefaultMessageSender();
         LeaseMessage var5 = new LeaseMessage(var1, var2);
         if (DEBUG) {
            debug("requesting lease release " + var5 + " from LeaseServer " + var3);
         }

         try {
            LeaseResponse var6 = (LeaseResponse)var4.send(var5, (ServerInformation)var3);
            if (DEBUG) {
               debug("received response to the lease message " + var6);
            }

            if (EnvironmentFactory.getClusterMember().getLeaseView() != null) {
               EnvironmentFactory.getClusterMember().getLeaseView().leaseReleasedByLocalServer(var1);
            }

         } catch (LeaseTableUpdateException var7) {
            throw new IOException(var7.getMessage());
         }
      }
   }

   public String findOwner(String var1) throws IOException {
      ServerInformation var2 = ClusterLeaderService.getLeader();
      if (var2 == null) {
         var2 = PrimordialClusterLeaderService.getInstance().getLeaderInformationInternal(true);
         if (var2 == null) {
            if (DEBUG) {
               debug("No server knows about the Cluster leader yet ! refuse find owner for " + var1);
            }

            throw this.createIOException("unable to find owner for lease " + var1 + " as the cluster leader is unavailable");
         }
      }

      return this.findOwner(var1, var2);
   }

   public String findOwner(String var1, ServerInformation var2) throws IOException {
      ClusterMessageSender var3 = ClusterMessageFactory.getInstance().getDefaultMessageSender();
      LeaseMessage var4 = new LeaseMessage(var1);
      if (DEBUG) {
         debug("requesting find owner " + var4 + " from LeaseServer " + var2);
      }

      LeaseResponse var5 = (LeaseResponse)var3.send(var4, (ServerInformation)var2);
      if (DEBUG) {
         debug("received response to the lease message " + var5);
      }

      return (String)var5.getResult();
   }

   public String findPreviousOwner(String var1) throws IOException {
      ServerInformation var2 = ClusterLeaderService.getLeader();
      if (var2 == null) {
         if (DEBUG) {
            debug("Cluster leader is not present ! refuse find previous owner for " + var1);
         }

         throw this.createIOException("unable to find previous owner for lease " + var1 + " as the cluster leader is unavailable");
      } else {
         ClusterMessageSender var3 = ClusterMessageFactory.getInstance().getDefaultMessageSender();
         LeaseMessage var4 = LeaseMessage.createFindPreviousOwnerMessage(var1);
         if (DEBUG) {
            debug("requesting find previous owner " + var4 + " from LeaseServer " + var2);
         }

         LeaseResponse var5 = (LeaseResponse)var3.send(var4, (ServerInformation)var2);
         if (DEBUG) {
            debug("received response to the lease message " + var5);
         }

         return (String)var5.getResult();
      }
   }

   public int renewLeases(String var1, Set var2, int var3) throws IOException {
      ServerInformation var4 = ClusterLeaderService.getLeader();
      if (var4 == null) {
         if (DEBUG) {
            debug("Cluster leader is not present ! refuse renew leases for " + var1);
         }

         throw this.createIOException("Cluster leader is not present ! refuse renew leases for " + var1);
      } else {
         ClusterMessageSender var5 = ClusterMessageFactory.getInstance().getDefaultMessageSender();
         LeaseMessage var6 = new LeaseMessage(var1, var2, var3);
         if (DEBUG) {
            debug("requesting renew leases " + var6 + " from LeaseServer " + var4);
         }

         LeaseResponse var7 = (LeaseResponse)var5.send(var6, (ServerInformation)var4);
         if (DEBUG) {
            debug("received response to the lease message " + var7);
         }

         return (Integer)var7.getResult();
      }
   }

   public int renewAllLeases(int var1, String var2) throws IOException {
      ServerInformation var3 = ClusterLeaderService.getLeader();
      if (var3 == null) {
         if (DEBUG) {
            debug("Cluster leader is not present ! refuse renew all leases for " + var2);
         }

         throw this.createIOException("Cluster leader is not present ! refuse renew all leases for " + var2);
      } else {
         ClusterMessageSender var4 = ClusterMessageFactory.getInstance().getDefaultMessageSender();
         LeaseMessage var5 = new LeaseMessage(var1, var2);
         if (DEBUG) {
            debug("requesting renew all leases " + var5 + " from LeaseServer " + var3);
         }

         LeaseResponse var6 = (LeaseResponse)var4.send(var5, (ServerInformation)var3);
         if (DEBUG) {
            debug("received response to the lease message " + var6);
         }

         return (Integer)var6.getResult();
      }
   }

   public String[] findExpiredLeases(int var1) throws IOException {
      ServerInformation var2 = ClusterLeaderService.getLeader();
      if (var2 == null) {
         if (DEBUG) {
            debug("Cluster leader is not present ! refuse find expired leases");
         }

         throw this.createIOException("unable to find expired leases  as the cluster leader is unavailable");
      } else {
         ClusterMessageSender var3 = ClusterMessageFactory.getInstance().getDefaultMessageSender();
         LeaseMessage var4 = LeaseMessage.findExpiredLeasesMessage(var1);
         if (DEBUG) {
            debug("requesting expired leases " + var4 + " from LeaseServer " + var2);
         }

         LeaseResponse var5 = (LeaseResponse)var3.send(var4, (ServerInformation)var2);
         if (DEBUG) {
            debug("received response to the lease message " + var5);
         }

         return (String[])((String[])var5.getResult());
      }
   }

   public synchronized void stateChanged(String var1, String var2) {
      String var3 = var2.intern();
      if (var3 != "discovery" && var3 != "formation" && var3 != "formation_leader") {
         this.clusterFormationInProgress = false;
      } else {
         this.clusterFormationInProgress = true;
      }

   }

   private synchronized IOException createIOException(String var1) {
      return (IOException)(this.clusterFormationInProgress ? new ClusterReformationInProgressException(var1) : new IOException(var1));
   }

   private static boolean debugEnabled() {
      return debugLeaseClient.isEnabled() || DebugLogger.isDebugEnabled();
   }

   private static void debug(String var0) {
      DebugLogger.debug("[LeaseClient] " + var0);
   }
}
