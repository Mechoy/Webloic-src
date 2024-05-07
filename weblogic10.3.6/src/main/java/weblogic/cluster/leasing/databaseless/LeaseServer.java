package weblogic.cluster.leasing.databaseless;

import java.io.IOException;
import java.util.Set;
import weblogic.cluster.messaging.internal.ClusterMessageProcessingException;
import weblogic.cluster.messaging.internal.ClusterResponse;
import weblogic.cluster.messaging.internal.DebugLogger;
import weblogic.cluster.singleton.LeasingBasis;
import weblogic.cluster.singleton.MissedHeartbeatException;
import weblogic.cluster.singleton.SimpleLeasingBasis;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public final class LeaseServer implements LeasingBasis {
   private static final DebugCategory debugLeaseServer = Debug.getCategory("weblogic.cluster.leasing.LeaseServer");
   private static final boolean DEBUG = debugEnabled();
   private final SimpleLeasingBasis simpleBasis;

   LeaseServer(ClusterLeader var1, LeaseView var2) {
      ReplicatedLeaseTable var3 = new ReplicatedLeaseTable(var1, var2);
      this.simpleBasis = new SimpleLeasingBasis(var3);
      if (DEBUG) {
         debug("created basis with entries " + var2.getLeaseTableReplica());
      }

   }

   public boolean acquire(String var1, String var2, int var3) {
      synchronized(this.simpleBasis) {
         boolean var10000;
         try {
            boolean var5 = this.simpleBasis.acquire(var1, var2, var3);
            if (var5) {
               if (DEBUG) {
                  debug("acquire: successfully updated other servers for " + var1 + " requested by " + var2 + ". Updated master lease table " + "and granted lease to " + var2 + " for lease timeout of " + var3);
               }

               var10000 = true;
               return var10000;
            }

            if (DEBUG) {
               String var6 = this.simpleBasis.findOwner(var1);
               debug("acquire: " + var6 + " owns the lease " + var1 + " requested by " + var2 + ". lease acquire is denied");
            }

            var10000 = false;
         } catch (LeaseTableUpdateException var8) {
            if (DEBUG) {
               debug("acquire: failed to update other servers for " + var1 + " requested by " + var2 + ". Lease is denied!");
            }

            return false;
         }

         return var10000;
      }
   }

   public void release(String var1, String var2) throws IOException {
      synchronized(this.simpleBasis) {
         String var4 = this.simpleBasis.findOwner(var1);
         if (DEBUG) {
            debug("release: got owner " + var4 + " for lease name " + var1);
         }

         if (var4 != null) {
            if (var4 == null || var4.equalsIgnoreCase(var2)) {
               if (DEBUG) {
                  debug("release: removing " + var1 + " from master " + "after updating other servers");
               }

               this.simpleBasis.release(var1, var2);
            }
         }
      }
   }

   public String findOwner(String var1) {
      synchronized(this.simpleBasis) {
         String var3 = this.simpleBasis.findOwner(var1);
         if (DEBUG) {
            debug("findOwner: got owner " + var3 + " for lease name " + var1);
         }

         return var3;
      }
   }

   public String findPreviousOwner(String var1) {
      synchronized(this.simpleBasis) {
         String var3 = this.simpleBasis.findPreviousOwner(var1);
         if (DEBUG) {
            debug("findPreviousOwner: got owner " + var3 + " for lease name " + var1);
         }

         return var3;
      }
   }

   public int renewAllLeases(int var1, String var2) throws MissedHeartbeatException {
      synchronized(this.simpleBasis) {
         int var10000;
         try {
            int var4 = this.simpleBasis.renewAllLeases(var1, var2);
            if (DEBUG) {
               debug("renewAllLeases: successfully renewed " + var4 + " leases for " + var2);
            }

            var10000 = var4;
         } catch (LeaseTableUpdateException var6) {
            if (DEBUG) {
               debug("renewAllLeases: update failed on other servers. not renewing leases owned by " + var2);
            }

            return 0;
         }

         return var10000;
      }
   }

   public int renewLeases(String var1, Set var2, int var3) throws MissedHeartbeatException {
      synchronized(this.simpleBasis) {
         int var10000;
         try {
            int var5 = this.simpleBasis.renewLeases(var1, var2, var3);
            if (DEBUG) {
               debug("renewLeases: successfully renewed " + var5 + " leases for " + var1);
            }

            var10000 = var5;
         } catch (LeaseTableUpdateException var7) {
            if (DEBUG) {
               debug("renewLeases: update failed on other servers. not renewing leases owned by " + var1);
            }

            return 0;
         }

         return var10000;
      }
   }

   public String[] findExpiredLeases(int var1) {
      synchronized(this.simpleBasis) {
         return this.simpleBasis.findExpiredLeases(var1);
      }
   }

   public ClusterResponse process(LeaseMessage var1) throws ClusterMessageProcessingException {
      if ("acquire".equals(var1.getRequestType())) {
         boolean var8 = this.acquire(var1.getLeaseName(), var1.getOwner(), var1.getLeaseTimeout());
         return new LeaseResponse(var8, var1);
      } else if ("release".equals(var1.getRequestType())) {
         try {
            this.release(var1.getLeaseName(), var1.getOwner());
         } catch (IOException var3) {
            throw new ClusterMessageProcessingException(var3);
         }

         return new LeaseResponse(Boolean.TRUE, var1);
      } else {
         String var2;
         if ("find_owner".equals(var1.getRequestType())) {
            var2 = this.findOwner(var1.getLeaseName());
            return new LeaseResponse(var2, var1);
         } else {
            int var7;
            if ("renew_all".equals(var1.getRequestType())) {
               try {
                  var7 = this.renewAllLeases(var1.getHealthCheckPeriod(), var1.getOwner());
                  return new LeaseResponse(var7, var1);
               } catch (MissedHeartbeatException var4) {
                  throw new ClusterMessageProcessingException(var4);
               }
            } else if ("renew_leases".equals(var1.getRequestType())) {
               try {
                  var7 = this.renewLeases(var1.getOwner(), var1.getLeasesToRenew(), var1.getHealthCheckPeriod());
                  return new LeaseResponse(var7, var1);
               } catch (MissedHeartbeatException var5) {
                  throw new ClusterMessageProcessingException(var5);
               }
            } else if ("expired".equals(var1.getRequestType())) {
               String[] var6 = this.findExpiredLeases(var1.getGracePeriod());
               return new LeaseResponse(var6, var1);
            } else if ("find_previous_owner".equals(var1.getRequestType())) {
               var2 = this.findPreviousOwner(var1.getLeaseName());
               return new LeaseResponse(var2, var1);
            } else {
               throw new AssertionError(var1 + " is unsupported !");
            }
         }
      }
   }

   private static boolean debugEnabled() {
      return debugLeaseServer.isEnabled() || DebugLogger.isDebugEnabled();
   }

   private static void debug(String var0) {
      DebugLogger.debug("[LeaseServer] " + var0);
   }
}
