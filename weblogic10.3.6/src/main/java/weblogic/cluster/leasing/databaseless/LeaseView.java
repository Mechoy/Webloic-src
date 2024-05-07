package weblogic.cluster.leasing.databaseless;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import weblogic.cluster.messaging.internal.ClusterMessageProcessingException;
import weblogic.cluster.messaging.internal.DebugLogger;
import weblogic.cluster.singleton.LeaseManager;
import weblogic.cluster.singleton.SimpleLeasingBasis;
import weblogic.protocol.LocalServerIdentity;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public final class LeaseView implements Serializable {
   static final long serialVersionUID = 7210518548000338390L;
   private static final DebugCategory debugLeaseView = Debug.getCategory("weblogic.cluster.leasing.LeaseView");
   private static final boolean DEBUG = debugEnabled();
   private static final long GUARD_TIME = 5000L;
   private long versionNumber;
   private HashMap leaseTableReplica;
   private HashMap localLeases;
   private String serverName;

   LeaseView(String var1, HashMap var2) {
      this(var1, var2, 0L);
   }

   LeaseView(String var1, HashMap var2, long var3) {
      this.versionNumber = var3;
      this.serverName = var1;
      if (var2 == null) {
         this.leaseTableReplica = new HashMap();
      } else {
         this.leaseTableReplica = var2;
      }

      this.localLeases = new HashMap();
      if (DEBUG) {
         debug("created lease view for " + var1 + " with entries " + this.leaseTableReplica);
      }

   }

   private static void debug(String var0) {
      DebugLogger.debug("[LeaseView] " + var0);
   }

   synchronized void process(LeaseTableUpdateMessage var1) throws ClusterMessageProcessingException {
      if (var1.getVersion() != this.versionNumber + 1L) {
         throw new ClusterMessageProcessingException("unacceptable lease view update. local version " + this.versionNumber + " and received version is " + var1.getVersion());
      } else {
         ++this.versionNumber;
         if (DEBUG) {
            debug("executing " + var1);
         }

         if (var1.getOperation() == 1) {
            this.leaseTableReplica.put(var1.getKey(), var1.getValue());
            Object var2 = this.localLeases.get(var1.getKey());
            if (var2 != null && !var2.equals(var1.getValue())) {
               if (DEBUG) {
                  debug("removing " + var1.getKey() + " as it has expired. local server no longer owns this lease !");
               }

               this.localLeases.remove(var1.getKey());
            }
         } else if (var1.getOperation() == 2) {
            this.leaseTableReplica.remove(var1.getKey());
         } else {
            if (var1.getOperation() != 3) {
               throw new AssertionError("unsupported lease update operation " + var1);
            }

            this.leaseTableReplica.putAll(var1.getMap());
         }

      }
   }

   synchronized void leaseAcquiredByLocalServer(String var1, int var2) {
      String var3 = LeaseManager.getOwnerIdentity(LocalServerIdentity.getIdentity());
      SimpleLeasingBasis.LeaseEntry var4 = new SimpleLeasingBasis.LeaseEntry(var3, var1, var2);
      this.localLeases.put(var4.getLeaseName(), var4);
      if (DEBUG) {
         debug("added " + var4 + " to list of leases owned by " + "this server");
      }

   }

   synchronized void leaseReleasedByLocalServer(String var1) {
      this.localLeases.remove(var1);
      if (DEBUG) {
         debug("removed " + var1 + " from the list of leases owned " + "by this server");
      }

   }

   synchronized int leasesOwnedByLocalServer() {
      return this.localLeases.size();
   }

   synchronized void merge(LeaseView var1) {
      if (var1 != null) {
         this.leaseTableReplica.putAll(var1.localLeases);
      }
   }

   synchronized void prepareToBecomeLeader() {
      this.leaseTableReplica.putAll(this.localLeases);
      Iterator var1 = this.leaseTableReplica.values().iterator();
      long var2 = System.currentTimeMillis() + 5000L;

      while(var1.hasNext()) {
         SimpleLeasingBasis.LeaseEntry var4 = (SimpleLeasingBasis.LeaseEntry)var1.next();
         var4.setTimestamp(var2);
      }

   }

   public HashMap getLeaseTableReplica() {
      return this.leaseTableReplica;
   }

   long getVersionNumber() {
      return this.versionNumber;
   }

   public synchronized void processStateDump(LeaseView var1) {
      if (var1 != null && this.versionNumber != var1.getVersionNumber()) {
         if (DEBUG) {
            debug("resetting lease view with " + var1.getLeaseTableReplica());
         }

         this.leaseTableReplica = var1.getLeaseTableReplica();
         this.versionNumber = var1.getVersionNumber();
      }
   }

   void incrementVersionNumber() {
      ++this.versionNumber;
   }

   public String toString() {
      return "LeaseView for " + this.serverName + " with version " + this.versionNumber + "\nLeaseTableReplica contents: " + this.leaseTableReplica + "\nleases owned: " + this.localLeases;
   }

   private static boolean debugEnabled() {
      return debugLeaseView.isEnabled() || DebugLogger.isDebugEnabled();
   }
}
