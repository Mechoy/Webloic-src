package weblogic.scheduler;

import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.utils.TargetUtils;
import weblogic.cluster.ClusterLogger;
import weblogic.cluster.singleton.LeaseManager;
import weblogic.cluster.singleton.LeaseManagerFactory;
import weblogic.cluster.singleton.LeasingException;
import weblogic.jndi.Environment;
import weblogic.protocol.URLManager;
import weblogic.rmi.extensions.PortableRemoteObject;
import weblogic.timers.NakedTimerListener;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.Debug;

public class TimerExecutor implements NakedTimerListener {
   private static final boolean DEBUG = Debug.getCategory("weblogic.JobScheduler").isEnabled();
   private static final String TIMER_MANAGER = "weblogic.scheduler.TimerExecutor";
   private static TimerExecutor THE_ONE;
   private Context jndiContext;
   private Set unavailableTimerApps = new HashSet();

   static synchronized void initialize() {
      Debug.assertion(THE_ONE == null);
      THE_ONE = new TimerExecutor();
   }

   private Set getUnavailableTimerApplications() {
      HashSet var1 = new HashSet();
      synchronized(this.unavailableTimerApps) {
         Iterator var3 = this.unavailableTimerApps.iterator();

         while(var3.hasNext()) {
            TimerApplication var4 = (TimerApplication)var3.next();
            if (this.isDeployedLocally(var4.getApplicationName())) {
               var3.remove();
            } else {
               var1.add(var4.getTimerId());
            }
         }

         return var1;
      }
   }

   private boolean isDeployedLocally(String var1) {
      ApplicationContextInternal var2 = ApplicationAccess.getApplicationAccess().getApplicationContext(var1);
      boolean var3 = var2 != null && TargetUtils.isDeployedLocally(var2.getBasicDeploymentMBean().getTargets());
      return var3;
   }

   private TimerExecutor() {
      try {
         Environment var1 = new Environment();
         var1.setReplicateBindings(false);
         var1.setCreateIntermediateContexts(true);
         this.jndiContext = var1.getInitialContext();
      } catch (NamingException var2) {
         throw new AssertionError("Failed to create initial context");
      }

      TimerManagerFactory.getTimerManagerFactory().getTimerManager("weblogic.scheduler.TimerExecutor", "weblogic.kernel.System").schedule(this, 0L, 30000L);
   }

   public void timerExpired(weblogic.timers.Timer var1) {
      TimerMasterRemote var2 = this.lookupTimerMaster();
      if (var2 != null) {
         if (DEBUG) {
            debug(" looked up TimerMasterRemote " + var2);
         }

         List var3;
         try {
            Set var4 = this.getUnavailableTimerApplications();
            var3 = var2.getReadyTimers(var4);
            if (DEBUG) {
               debug("getReadyTimers from MASTER size=" + var3.size());
            }
         } catch (TimerException var15) {
            if (DEBUG) {
               var15.printStackTrace();
            }

            return;
         } catch (RemoteException var16) {
            if (DEBUG) {
               var16.printStackTrace();
            }

            return;
         }

         ListIterator var17 = var3.listIterator();

         while(var17.hasNext()) {
            String var5 = (String)var17.next();

            try {
               TimerState var6 = TimerBasisAccess.getTimerBasis().getTimerState(var5);
               var6.fireWhenReady();
            } catch (TimerException var13) {
               Throwable var7 = var13.getCause();
               if (var7 != null && var7 instanceof ApplicationNotFoundException) {
                  ApplicationNotFoundException var8 = (ApplicationNotFoundException)var7;
                  TimerApplication var9 = new TimerApplication(var8.getMessage(), var5);
                  synchronized(this.unavailableTimerApps) {
                     this.unavailableTimerApps.add(var9);
                  }

                  if (DEBUG) {
                     debug("Failed to find " + var9);
                  }
               }
            } catch (NoSuchObjectLocalException var14) {
            }
         }

      }
   }

   private static void debug(String var0) {
      ClusterLogger.logDebug("[TimerExecutor] " + var0);
   }

   private TimerMasterRemote lookupTimerMaster() {
      Context var1 = null;
      String var2 = null;

      String var5;
      try {
         LeaseManager var3 = LeaseManagerFactory.singleton().getLeaseManager("service");
         String var4 = var3.findOwner("TimerMaster");
         if (var4 != null) {
            var2 = LeaseManager.getServerNameFromOwnerIdentity(var4);
            if (DEBUG) {
               debug("The location of TimerMaster is: " + var2);
            }

            var5 = URLManager.findAdministrationURL(var2);
            if (DEBUG) {
               debug("Contacting " + var2 + " at " + var5 + " to fetch the " + "TimerMaster");
            }

            Environment var6;
            if (var5 == null) {
               var6 = null;
               return var6;
            }

            var6 = new Environment();
            var6.setProviderUrl(var5);
            var1 = var6.getInitialContext();
            TimerMasterRemote var7 = (TimerMasterRemote)PortableRemoteObject.narrow(var1.lookup("weblogic.scheduler.TimerMaster"), TimerMasterRemote.class);
            return var7;
         }

         if (DEBUG) {
            debug("Could not find the current owner of the service TimerMaster");
         }

         var5 = null;
      } catch (LeasingException var23) {
         if (DEBUG) {
            debug("Could not find TimerMaster on " + var2 + ". Exception:" + var23.getMessage());
         }

         return null;
      } catch (NamingException var24) {
         if (DEBUG) {
            debug("Could not find TimerMaster on " + var2 + ". Exception:" + var24.getMessage());
         }

         return null;
      } catch (UnknownHostException var25) {
         if (DEBUG) {
            debug("Could not find server " + var2);
         }

         return null;
      } finally {
         if (var1 != null) {
            try {
               var1.close();
            } catch (NamingException var22) {
            }
         }

      }

      return var5;
   }

   private class TimerApplication {
      private String applicationName;
      private String timerId;
      private int hashCode;

      public TimerApplication(String var2, String var3) {
         this.applicationName = var2;
         this.timerId = var3;
         this.hashCode = this.applicationName.hashCode() ^ this.timerId.hashCode();
      }

      public String getApplicationName() {
         return this.applicationName;
      }

      public String getTimerId() {
         return this.timerId;
      }

      public int hashCode() {
         return this.hashCode;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else {
            if (var1 instanceof TimerApplication) {
               TimerApplication var2 = (TimerApplication)var1;
               if (var2.getApplicationName().equals(this.applicationName) && var2.getTimerId().equals(this.timerId)) {
                  return true;
               }
            }

            return false;
         }
      }

      public String toString() {
         return "ApplictionName:" + this.applicationName + " TimerId:" + this.timerId;
      }
   }
}
