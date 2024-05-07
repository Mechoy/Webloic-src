package weblogic.scheduler;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import weblogic.cluster.ClusterLogger;
import weblogic.cluster.ClusterService;
import weblogic.cluster.singleton.LeaseLostListener;
import weblogic.cluster.singleton.LeaseManager;
import weblogic.cluster.singleton.LeaseManagerFactory;
import weblogic.cluster.singleton.LeasingBasis;
import weblogic.cluster.singleton.LeasingException;
import weblogic.cluster.singleton.RemoteLeasingBasisImpl;
import weblogic.cluster.singleton.SimpleLeasingBasis;
import weblogic.cluster.singleton.SingletonService;
import weblogic.cluster.singleton.SingletonServicesManager;
import weblogic.jndi.Environment;
import weblogic.timers.NakedTimerListener;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.Debug;

public class TimerMaster implements TimerMasterRemote, NakedTimerListener, SingletonService, LeaseLostListener {
   private static final boolean DEBUG = Debug.getCategory("weblogic.JobScheduler").isEnabled();
   private static final String TIMER_MANAGER = "weblogic.scheduler.TimerMaster";
   static final String JNDI_NAME = "weblogic.scheduler.TimerMaster";
   static final String LEASE_NAME = "TimerMaster";
   static final String LEASE_TYPE = "timer";
   static final int eventHorizonSeconds = 60;
   static final int pollsPerPeriod = 2;
   private static LeaseManager leaseMgr;
   private List readyTimers;
   private int numServersCalledIn;
   private int status;
   private static final int WAITING_TO_REGISTER = 0;
   private static final long registrationRetryPeriodSeconds = 20L;
   private static final int WAITING_TO_LEASE = 1;
   private static final int IS_MASTER = 2;
   private Context context;
   private weblogic.timers.Timer timer;
   private static TimerMaster THE_ONE;

   static synchronized void initialize() {
      Debug.assertion(THE_ONE == null);
      THE_ONE = new TimerMaster();
   }

   private TimerMaster() {
      try {
         Environment var1 = new Environment();
         var1.setReplicateBindings(false);
         var1.setCreateIntermediateContexts(true);
         this.context = var1.getInitialContext();

         try {
            this.context.bind("weblogic.scheduler.TimerMaster", this);
         } catch (NamingException var3) {
            var3.printStackTrace();
            throw new AssertionError("Unable to bind TimerMaster");
         }

         getLeaseManager();
      } catch (NamingException var4) {
         throw new AssertionError("Failed to create initial context");
      }

      this.setupRegistration();
   }

   static synchronized LeaseManager getLeaseManager() throws NamingException {
      if (leaseMgr != null) {
         return leaseMgr;
      } else {
         if (Boolean.getBoolean("weblogic.UseSimpleLeasingForJobScheduler")) {
            Object var0 = null;
            InitialContext var1 = new InitialContext();

            try {
               var0 = (LeasingBasis)var1.lookup("RemoteLeasingBasis");
            } catch (NamingException var3) {
            }

            if (var0 == null) {
               var0 = new RemoteLeasingBasisImpl(new SimpleLeasingBasis());
               var1.bind("RemoteLeasingBasis", var0);
            }

            leaseMgr = new LeaseManager((LeasingBasis)var0, 500, 1000, 1000, "timer");
            leaseMgr.start();
         } else {
            leaseMgr = ClusterService.getClusterService().getDefaultLeaseManager("timer");
         }

         return leaseMgr;
      }
   }

   private void setupRegistration() {
      this.status = 1;
      SingletonServicesManager.getInstance().add(this.getName(), this);
   }

   public synchronized void timerExpired(weblogic.timers.Timer var1) {
      if (this.status != 2) {
         throw new AssertionError("TimerMaster executing in " + this.status);
      } else {
         try {
            if (DEBUG) {
               debug("we are the master of lease. get ready timers");
            }

            List var2 = TimerBasisAccess.getTimerBasis().getReadyTimers(60);
            ListIterator var3 = var2.listIterator();

            String var4;
            while(var3.hasNext()) {
               var4 = (String)var3.next();
               if (!this.readyTimers.contains(var4)) {
                  this.readyTimers.add(var4);
               }
            }

            if (DEBUG) {
               debug("total number of ready timers = " + this.readyTimers.size());
            }

            var3 = this.readyTimers.listIterator();

            while(var3.hasNext()) {
               var4 = (String)var3.next();

               try {
                  if (leaseMgr.findOwner(var4) != null) {
                     if (DEBUG) {
                        debug("timer already has a owner:" + var4);
                     }

                     var3.remove();
                  } else if (DEBUG) {
                     debug("timer is ready:" + var4);
                  }
               } catch (LeasingException var6) {
               }
            }

            this.numServersCalledIn = 0;
         } catch (TimerException var7) {
         }

      }
   }

   private static void debug(String var0) {
      ClusterLogger.logDebug("[TimerMaster] " + var0);
   }

   public synchronized void activate() {
      if (this.status != 1) {
         throw new AssertionError("State machine failure: lease obtained while not WAITING_TO_LEASE");
      } else {
         this.status = 2;
         if (DEBUG) {
            debug("Obtained lease NOTIFICATION on " + this.getName());
         }

         this.readyTimers = new ArrayList();
         this.numServersCalledIn = 0;
         this.timer = TimerManagerFactory.getTimerManagerFactory().getTimerManager("weblogic.scheduler.TimerMaster", "weblogic.kernel.System").schedule(this, 0L, 30000L);
         this.registerWithSingletonMonitorLeaseManager();
      }
   }

   public synchronized void deactivate() {
      if (this.status == 2) {
         this.timer.cancel();
         if (DEBUG) {
            debug("Lease LOST [TimerMaster] ");
         }

         this.status = 1;
      }
   }

   public synchronized List getReadyTimers(Set var1) throws RemoteException, TimerException {
      if (this.status != 2) {
         throw new TimerException("Attempt to get ready timers from server that is not master");
      } else {
         int var2 = ClusterService.getClusterService().getRemoteMembers().size() + 1 - this.numServersCalledIn;
         int var3 = 1;
         if (var2 < 1) {
            var2 = 1;
         }

         if (var2 < this.readyTimers.size()) {
            var3 = this.readyTimers.size() / var2;
         }

         int var4 = 0;
         ArrayList var5 = new ArrayList();
         if (DEBUG) {
            debug("getReadyTimers size=" + this.readyTimers.size() + ", numServersCalledIn=" + this.numServersCalledIn + ", numServerToCall=" + var2 + ", timers to send=" + var3);
         }

         for(Iterator var6 = this.readyTimers.iterator(); var4 >= 0 && var4 < var3 && var6.hasNext(); ++var4) {
            Object var7 = var6.next();
            if (!var1.contains(var7)) {
               var5.add(var7);
               var6.remove();
               if (DEBUG) {
                  debug("Sending ready timer " + var7 + " to TimerExecutor");
               }
            } else if (DEBUG) {
               debug("Skipping to send ready timer " + var7 + " to TimerExecutor");
            }
         }

         ++this.numServersCalledIn;
         return var5;
      }
   }

   public String getName() {
      return "TimerMaster";
   }

   private void registerWithSingletonMonitorLeaseManager() {
      LeaseManager var1 = LeaseManagerFactory.singleton().getLeaseManager("service");
      var1.addLeaseLostListener(this);
   }

   private void unregisterWithSingletonMonitorLeaseManager() {
      LeaseManager var1 = LeaseManagerFactory.singleton().getLeaseManager("service");
      var1.removeLeaseLostListener(this);
   }

   public void onRelease() {
      if (this.status == 2) {
         if (DEBUG) {
            debug("TimerMaster Got a callback for LeaseLost.");
         }

         this.deactivate();
         this.unregisterWithSingletonMonitorLeaseManager();
      }
   }
}
