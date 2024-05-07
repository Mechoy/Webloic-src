package weblogic.health;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import weblogic.cluster.migration.MTCustomValidator;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.t3.srvr.T3Srvr;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.Debug;
import weblogic.work.WorkManagerFactory;

public final class HealthMonitorService extends AbstractServerService implements TimerListener {
   private static ArrayList callbackListeners = new ArrayList();
   private static final HashMap monSysTbl = new HashMap();
   private TimerManager timerManager;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void start() {
      this.timerManager = TimerManagerFactory.getTimerManagerFactory().getTimerManager("HealthMonitorTask", WorkManagerFactory.getInstance().getSystem());
      int var1 = ManagementService.getRuntimeAccess(kernelId).getServer().getHealthCheckIntervalSeconds();
      this.timerManager.scheduleAtFixedRate(this, (long)(var1 * 1000), (long)(var1 * 1000));
   }

   public void stop() {
      this.shutdown();
   }

   public void halt() {
      this.shutdown();
   }

   private void shutdown() {
      if (this.timerManager != null) {
         this.timerManager.stop();
      }

   }

   public static void register(String var0, HealthFeedback var1, boolean var2) {
      HealthDebug.log("> HealthMonitorService::register keyArg = " + var0);
      Debug.assertion(var0 != null && var1 != null);
      if (var1 instanceof RuntimeMBean) {
         synchronized(monSysTbl) {
            monSysTbl.put(var0.trim(), new MonitoredSystemTableEntry(var0, (RuntimeMBean)var1, var2));
         }
      } else {
         synchronized(monSysTbl) {
            monSysTbl.put(var0.trim(), new MonitoredSystemTableEntry(var0, var1, var2));
         }
      }

   }

   public static void registerForCallback(HealthFeedbackCallback var0) {
      synchronized(callbackListeners) {
         callbackListeners.add(var0);
      }
   }

   public static void deregisterForCallback(HealthFeedbackCallback var0) {
      synchronized(callbackListeners) {
         callbackListeners.remove(var0);
      }
   }

   private static void notifyListeners(HealthState var0) {
      synchronized(callbackListeners) {
         Iterator var2 = callbackListeners.iterator();

         while(var2.hasNext()) {
            ((HealthFeedbackCallback)var2.next()).healthStateChange(var0);
         }

      }
   }

   public static void unregister(String var0) {
      HealthDebug.log("> HealthMonitorService::unregister keyArg = " + var0);
      Debug.assertion(var0 != null);
      synchronized(monSysTbl) {
         monSysTbl.remove(var0);
      }
   }

   public static synchronized void subsystemFailedNonFatal(final String var0, final String var1) {
      if (MTCustomValidator.ASM_JMS_DISABLED) {
         subsystemFailed(var0, var1);
      } else {
         WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
            public void run() {
               HealthLogger.logNonCriticalSubsystemFailedWithReason(var0, var1);
               MonitoredSystemTableEntry var1x = (MonitoredSystemTableEntry)HealthMonitorService.monSysTbl.get(var0);
               if (var1x == null) {
                  HealthLogger.logNoRegisteredSubsystem(var0, var1);
               } else {
                  HealthMonitorService.monSysTbl.get(var0);
                  HealthState var2 = var1x.getHealthFeedback().getHealthState();
                  var2.setSubsystemName(var1x.getKey());
                  var2.setCritical(var1x.getIsCritical());
                  var2.setMBeanName(var1x.getMBeanName());
                  var2.setMBeanType(var1x.getMBeanType());
                  HealthMonitorService.notifyListeners(var2);
               }
            }
         });
      }
   }

   public static synchronized void subsystemFailed(final String var0, final String var1) {
      WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
         public void run() {
            HealthLogger.logErrorSubsystemFailedWithReason(var0, var1);
            T3Srvr.getT3Srvr().failed("health of critical service '" + var0 + "' failed");
         }
      });
   }

   public static synchronized void subsystemFailedForceShutdown(final String var0, final String var1) {
      WorkManagerFactory.getInstance().getSystem().schedule(new Runnable() {
         public void run() {
            HealthLogger.logErrorSubsystemFailedWithReason(var0, var1);
            T3Srvr.getT3Srvr().failedForceShutdown("health of critical service '" + var0 + "' failed");
         }
      });
   }

   public static synchronized void panic(Throwable var0) {
      if ("system-exit".equals(ManagementService.getRuntimeAccess(kernelId).getServer().getOverloadProtection().getPanicAction())) {
         T3Srvr.getT3Srvr().exitImmediately(var0);
      }

   }

   public void timerExpired(Timer var1) {
      HealthDebug.log("> HealthMonitorTask::run (10)");
      synchronized(monSysTbl) {
         if (monSysTbl.size() == 0) {
            return;
         }

         Iterator var3 = monSysTbl.values().iterator();

         while(var3.hasNext()) {
            MonitoredSystemTableEntry var4 = (MonitoredSystemTableEntry)var3.next();
            HealthState var5 = var4.getHealthFeedback().getHealthState();
            var5.setSubsystemName(var4.getKey());
            var5.setCritical(var4.getIsCritical());
            var5.setMBeanName(var4.getMBeanName());
            var5.setMBeanType(var4.getMBeanType());
            HealthDebug.log("Health state of " + var4.getKey() + " is " + HealthState.mapToString(var5.getState()));
            if (var5.getState() == 3) {
               notifyListeners(var5);
            }

            if (var4.getIsCritical() && var5.getState() == 3) {
               HealthLogger.logErrorSubsystemFailed(var4.getKey());
               T3Srvr.getT3Srvr().failed("health of critical service '" + var4.getKey() + "' failed");
               break;
            }
         }
      }

      HealthDebug.log("< HealthMonitorTask::run (20)");
   }

   public static HealthState[] getHealthStates() {
      synchronized(monSysTbl) {
         if (monSysTbl.size() == 0) {
            return null;
         } else {
            HealthState[] var1 = new HealthState[monSysTbl.size()];
            int var2 = 0;

            for(Iterator var3 = monSysTbl.values().iterator(); var3.hasNext(); ++var2) {
               MonitoredSystemTableEntry var4 = (MonitoredSystemTableEntry)var3.next();
               HealthState var5 = var4.getHealthFeedback().getHealthState();
               var1[var2] = new HealthState(var5.getState(), var5.getReasonCode());
               var1[var2].setSubsystemName(var4.getKey());
               var1[var2].setCritical(var4.getIsCritical());
               var1[var2].setMBeanName(var4.getMBeanName());
               var1[var2].setMBeanType(var4.getMBeanType());
            }

            return var1;
         }
      }
   }
}
