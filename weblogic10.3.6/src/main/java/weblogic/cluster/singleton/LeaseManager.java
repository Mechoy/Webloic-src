package weblogic.cluster.singleton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.cluster.ClusterLogger;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.ServerIdentity;
import weblogic.timers.NakedTimerListener;
import weblogic.timers.Timer;
import weblogic.timers.TimerManagerFactory;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public class LeaseManager implements Leasing, NakedTimerListener, MigratableServiceConstants {
   private LeasingBasis basis;
   private int heartbeatPeriod;
   private int healthCheckPeriod;
   private int gracePeriod;
   private Map leaseObtainedListeners;
   private Timer timer;
   private String leaseType;
   private ArrayList leaseLostListeners;
   private int missedHeartbeats = 0;
   private static volatile Set outstandingLeasesSet = new HashSet();
   private final Set myLeasesSet = new HashSet();
   private final boolean DEBUG = SingletonServicesDebugLogger.isDebugEnabled();
   public static final String FAILED = "WLS_FAILED_SERVICE";
   public static final String DELIMITER = ".";
   public static final String DIVIDER = "/";
   private boolean warnedAboutLazyTimer = false;
   private long lastTimerTime = 0L;

   public LeaseManager(LeasingBasis var1, int var2, int var3, int var4, String var5) {
      this.basis = var1;
      this.heartbeatPeriod = var2;
      this.healthCheckPeriod = var3;
      this.gracePeriod = var4;
      this.leaseType = var5;
      this.leaseObtainedListeners = new HashMap();
      this.leaseLostListeners = new ArrayList();
      this.lastTimerTime = System.currentTimeMillis();
   }

   public boolean tryAcquire(String var1) throws LeasingException {
      synchronized(outstandingLeasesSet) {
         if (this.leaseObtainedListeners.containsKey(this.qualifyLeaseName(var1))) {
            throw new LeasingException("Previously registered to obtain lease");
         } else {
            boolean var10000;
            try {
               if (this.isCurrentOwner(var1)) {
                  var10000 = true;
                  return var10000;
               }

               if (this.basis.acquire(this.qualifyLeaseName(var1), getOwnerIdentity(LocalServerIdentity.getIdentity()), this.healthCheckPeriod)) {
                  this.addToOutStandingLeasesSet(var1);
                  var10000 = true;
                  return var10000;
               }

               var10000 = false;
            } catch (IOException var5) {
               throw new LeasingException("tryAcquire()", var5);
            }

            return var10000;
         }
      }
   }

   public void acquire(String var1, LeaseObtainedListener var2) throws LeasingException {
      synchronized(outstandingLeasesSet) {
         if (this.leaseObtainedListeners.containsKey(this.qualifyLeaseName(var1))) {
            throw new LeasingException("Previously registered to obtain lease");
         } else if (this.isCurrentOwner(var1)) {
            var2.onAcquire(var1);
            if (!outstandingLeasesSet.contains(this.qualifyLeaseName(var1))) {
               this.addToOutStandingLeasesSet(var1);
            }

         } else {
            try {
               if (this.basis.acquire(this.qualifyLeaseName(var1), getOwnerIdentity(LocalServerIdentity.getIdentity()), this.healthCheckPeriod)) {
                  this.addToOutStandingLeasesSet(var1);
                  var2.onAcquire(var1);
                  return;
               }
            } catch (IOException var6) {
            }

            this.leaseObtainedListeners.put(this.qualifyLeaseName(var1), var2);
         }
      }
   }

   public void release(String var1) throws LeasingException {
      synchronized(outstandingLeasesSet) {
         if (this.leaseObtainedListeners.containsKey(this.qualifyLeaseName(var1))) {
            this.leaseObtainedListeners.remove(this.qualifyLeaseName(var1));
         } else {
            try {
               this.basis.release(this.qualifyLeaseName(var1), getOwnerIdentity(LocalServerIdentity.getIdentity()));
               this.removeFromOutStandingLeasesSet(var1);
               if (this.DEBUG) {
                  p("release " + var1 + " successful");
               }
            } catch (IOException var7) {
               try {
                  this.basis.release(this.qualifyLeaseName(var1), "WLS_FAILED_SERVICE/" + LocalServerIdentity.getIdentity().getServerName());
               } catch (IOException var6) {
                  if (this.DEBUG) {
                     p("release " + var1 + " failed");
                  }

                  throw new LeasingException("release()", var7);
               }

               return;
            }

         }
      }
   }

   public String findOwner(String var1) throws LeasingException {
      try {
         return this.basis.findOwner(this.qualifyLeaseName(var1));
      } catch (IOException var3) {
         throw new LeasingException("findOwner()", var3);
      }
   }

   public String findPreviousOwner(String var1) throws LeasingException {
      try {
         return this.basis.findPreviousOwner(this.qualifyLeaseName(var1));
      } catch (IOException var3) {
         throw new LeasingException("findPreviousOwner()", var3);
      }
   }

   public static String getOwnerIdentity(ServerIdentity var0) {
      return var0.getTransientIdentity().getIdentityAsLong() + "/" + var0.getServerName();
   }

   public static String getServerNameFromOwnerIdentity(String var0) {
      return var0.substring(var0.indexOf("/") + 1).trim();
   }

   private boolean isCurrentOwner(String var1) {
      try {
         String var2 = this.basis.findOwner(this.qualifyLeaseName(var1));
         return var2 != null && var2.equals(getOwnerIdentity(LocalServerIdentity.getIdentity()));
      } catch (IOException var3) {
         return false;
      }
   }

   private String qualifyLeaseName(String var1) {
      return this.leaseType + "." + var1;
   }

   private String dequalifyLeaseName(String var1) {
      return var1 == null ? null : var1.substring(var1.lastIndexOf(".") + 1, var1.length());
   }

   public void addLeaseLostListener(LeaseLostListener var1) {
      synchronized(this.leaseLostListeners) {
         this.leaseLostListeners.add(var1);
      }
   }

   public void removeLeaseLostListener(LeaseLostListener var1) {
      synchronized(this.leaseLostListeners) {
         int var3 = this.leaseLostListeners.indexOf(var1);
         if (var3 > -1) {
            this.leaseLostListeners.remove(var3);
         }

      }
   }

   public int getGracePeriod() {
      return this.gracePeriod;
   }

   LeasingBasis getLeasingBasis() {
      return this.basis;
   }

   public String[] findExpiredLeases() {
      try {
         String[] var1 = this.basis.findExpiredLeases(this.gracePeriod);
         ArrayList var2 = new ArrayList();

         int var3;
         for(var3 = 0; var3 < var1.length; ++var3) {
            if (var1[var3].startsWith(this.leaseType)) {
               var2.add(var1[var3]);
            }
         }

         var1 = new String[var2.size()];
         var3 = 0;

         for(Iterator var4 = var2.iterator(); var4.hasNext(); ++var3) {
            var1[var3] = this.dequalifyLeaseName((String)var4.next());
         }

         return var1;
      } catch (IOException var5) {
         return new String[0];
      }
   }

   public void stop() {
      if (this.timer != null) {
         try {
            this.timer.cancel();
            this.basis.release(this.qualifyLeaseName(""), getOwnerIdentity(LocalServerIdentity.getIdentity()));
         } catch (IOException var2) {
         }

      }
   }

   public void start() {
      WorkManager var1 = WorkManagerFactory.getInstance().findOrCreate("WorkManagerForAaronLeaseManagerPatch", 2, -1);
      this.timer = TimerManagerFactory.getTimerManagerFactory().getTimerManager("MigratableServerTimerManager", var1).schedule(this, 0L, (long)this.heartbeatPeriod);
      this.lastTimerTime = System.currentTimeMillis();
   }

   public void voidLeases() {
      this.voidLeases(getOwnerIdentity(LocalServerIdentity.getIdentity()));
   }

   public void voidLeases(String var1) {
      if (this.DEBUG) {
         p("Voiding leases for " + var1);
      }

      synchronized(outstandingLeasesSet) {
         try {
            this.basis.renewAllLeases(-this.healthCheckPeriod * 3, var1);
            if (this.DEBUG) {
               p("Successfully voided leases for " + var1);
            }
         } catch (Exception var5) {
            if (this.DEBUG) {
               p("Failed voiding leases for " + var1 + ":" + var5);
            }
         }

      }
   }

   public void timerExpired(Timer var1) {
      long var2 = System.currentTimeMillis();
      if (var2 - this.lastTimerTime > (long)(this.healthCheckPeriod * 3) && !this.warnedAboutLazyTimer) {
         this.warnedAboutLazyTimer = true;
         ClusterLogger.logDelayedLeaseRenewal((var2 - this.lastTimerTime) / 1000L);
      }

      this.lastTimerTime = var2;
      synchronized(outstandingLeasesSet) {
         Iterator var5 = this.leaseObtainedListeners.keySet().iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            LeaseObtainedListener var7 = (LeaseObtainedListener)this.leaseObtainedListeners.get(var6);

            try {
               if (this.basis.acquire(var6, getOwnerIdentity(LocalServerIdentity.getIdentity()), this.healthCheckPeriod)) {
                  this.addToOutStandingLeasesSet(this.dequalifyLeaseName(var6));
                  var7.onAcquire(this.dequalifyLeaseName(var6));
                  var5.remove();
               }
            } catch (IOException var14) {
               if (this.DEBUG) {
                  p("IOException during lease checking: " + var14);
               }

               var7.onException(var14, var6);
            } catch (LeasingException var15) {
               if (this.DEBUG) {
                  p("LeasingException during lease checking: " + var15);
               }

               var7.onException(var15, var6);
            }
         }
      }

      try {
         synchronized(outstandingLeasesSet) {
            if (outstandingLeasesSet.size() == 0) {
               return;
            }

            int var17 = this.basis.renewLeases(getOwnerIdentity(LocalServerIdentity.getIdentity()), outstandingLeasesSet, this.healthCheckPeriod);
            if (var17 != outstandingLeasesSet.size()) {
               this.warnedAboutLazyTimer = false;
               p("Signal Lease Lost because total leases renewed = " + var17 + " .Outstanding leases=" + outstandingLeasesSet);
               this.signalLeaseLost();
               this.removeOutstandingLeases();
            } else if (this.warnedAboutLazyTimer) {
               this.warnedAboutLazyTimer = false;
               ClusterLogger.logLeaseRenewedAfterDelay();
            }
         }

         this.missedHeartbeats = 0;
      } catch (ClusterReformationInProgressException var12) {
         if (this.DEBUG) {
            p("Consensus leasing basis is in reformation. will NOT signal lease lost. exception:" + var12.getMessage());
         }
      } catch (Exception var13) {
         ++this.missedHeartbeats;
         if (this.DEBUG) {
            p("missed heartbeat " + this.missedHeartbeats + ", " + this.basis.getClass().getName() + ": " + var13 + ")");
         }

         if (this.missedHeartbeats * this.heartbeatPeriod >= this.healthCheckPeriod) {
            if (this.DEBUG) {
               p("Signal Lease Lost because of missed heartbeats beyond healthCheckPeriod (" + this.missedHeartbeats + ", " + (this.missedHeartbeats * this.heartbeatPeriod >= this.healthCheckPeriod) + ", " + this.basis.getClass().getName() + ": " + var13 + ")");
            }

            this.signalLeaseLost();
            this.removeOutstandingLeases();
         }
      }

   }

   private void removeOutstandingLeases() {
      Iterator var1 = this.myLeasesSet.iterator();

      while(var1.hasNext()) {
         outstandingLeasesSet.remove(var1.next());
      }

      this.myLeasesSet.clear();
   }

   private void signalLeaseLost() {
      if (this.DEBUG) {
         p("signalLeaseLost() called on " + this + " with leaseLostListeners: " + this.leaseLostListeners.size());
      }

      synchronized(this.leaseLostListeners) {
         final LeaseLostListener var3;
         for(Iterator var2 = this.leaseLostListeners.iterator(); var2.hasNext(); WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
            public void run() {
               var3.onRelease();
            }
         })) {
            var3 = (LeaseLostListener)var2.next();
            if (this.DEBUG) {
               p("Notifying " + var3 + " of " + this.missedHeartbeats + " missed heartbeats.");
            }
         }

      }
   }

   private void addToOutStandingLeasesSet(String var1) {
      String var2 = this.qualifyLeaseName(var1);
      outstandingLeasesSet.add(var2);
      this.myLeasesSet.add(var2);
      if (this.DEBUG) {
         p("Added service " + var1 + " successfully. " + "Total outstanding leases = " + outstandingLeasesSet);
      }

   }

   private void removeFromOutStandingLeasesSet(String var1) {
      String var2 = this.qualifyLeaseName(var1);
      outstandingLeasesSet.remove(var2);
      this.myLeasesSet.remove(var2);
      if (this.DEBUG) {
         p("Removed service " + var1 + " successfully. " + "Total outstanding leases = " + outstandingLeasesSet);
      }

   }

   private static final void p(String var0) {
      SingletonServicesDebugLogger.debug("LeaseManager: " + var0);
   }
}
