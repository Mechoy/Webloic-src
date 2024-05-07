package weblogic.t3.srvr;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;
import weblogic.health.HealthMonitorService;
import weblogic.health.LowMemoryNotificationService;
import weblogic.health.MemoryEvent;
import weblogic.health.MemoryListener;
import weblogic.kernel.Kernel;
import weblogic.kernel.T3SrvrLogger;
import weblogic.management.configuration.OverloadProtectionMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ExecuteQueueRuntimeMBean;
import weblogic.management.runtime.ExecuteThread;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.management.runtime.ThreadPoolRuntimeMBean;
import weblogic.platform.GCMonitorThread;
import weblogic.platform.VM;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.io.Chunk;
import weblogic.work.WorkManagerFactory;

public final class CoreHealthService extends AbstractServerService implements MemoryListener {
   private static final String SUBSYSTEM_NAME = "core";
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private Timer healthTimer;
   private static ServerRuntimeMBean serverRuntimeMBean;
   private static OverloadProtectionMBean olp;

   public void start() throws ServiceFailureException {
      try {
         ServerMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer();
         serverRuntimeMBean = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
         HealthMonitorService.register("ServerRuntime", serverRuntimeMBean, true);
         olp = var1.getOverloadProtection();
         int var2 = olp.getFreeMemoryPercentHighThreshold();
         int var3 = olp.getFreeMemoryPercentLowThreshold();
         GCMonitorThread.init();
         LowMemoryNotificationService.initialize(var3, var2);
         LowMemoryNotificationService.addMemoryListener(new CoreHealthService());
         long var4 = (long)var1.getStuckThreadTimerInterval() * 1000L;
         long var6 = this.getConfiguredStuckThreadMaxTime(var1);
         TimerManager var8 = TimerManagerFactory.getTimerManagerFactory().getTimerManager("weblogic.health.ThreadMonitor", WorkManagerFactory.getInstance().getSystem());
         this.healthTimer = var8.schedule(new ThreadMonitoringTimer(var6, var4), 0L, var4);
      } catch (Exception var9) {
         T3SrvrLogger.logWarnRegisterHealthMonitor("ServerRuntime", var9);
         throw new ServiceFailureException(var9);
      }
   }

   private long getConfiguredStuckThreadMaxTime(ServerMBean var1) {
      return var1.getOverloadProtection().getServerFailureTrigger() != null ? (long)var1.getOverloadProtection().getServerFailureTrigger().getMaxStuckThreadTime() * 1000L : (long)var1.getStuckThreadMaxTime() * 1000L;
   }

   public void stop() throws ServiceFailureException {
      this.halt();
   }

   public void halt() throws ServiceFailureException {
      try {
         HealthMonitorService.unregister("ServerRuntime");
         if (this.healthTimer != null) {
            this.healthTimer.cancel();
         }
      } catch (Exception var2) {
         T3SrvrLogger.logWarnUnregisterHealthMonitor("ServerRuntime", var2);
      }

   }

   public void memoryChanged(final MemoryEvent var1) {
      SecurityServiceManager.runAs(kernelId, kernelId, new PrivilegedAction() {
         public Object run() {
            if (var1.getEventType() == 1) {
               CoreHealthService.serverRuntimeMBean.setHealthState(4, "server is low on memory");
               Chunk.signalLowMemoryCondition();
            }

            if (var1.getEventType() == 0) {
               CoreHealthService.serverRuntimeMBean.setHealthState(0, (String)null);
               Chunk.clearLowMemoryCondition();
            }

            return null;
         }
      });
   }

   private static final class ThreadMonitoringTimer implements TimerListener {
      private final long stuckThreadMaxTime;
      private final long timerInterval;
      private boolean alreadyDeadlocked = false;

      ThreadMonitoringTimer(long var1, long var3) {
         this.stuckThreadMaxTime = var1;
         this.timerInterval = var3;
      }

      public void timerExpired(Timer var1) {
         SecurityServiceManager.runAs(CoreHealthService.kernelId, CoreHealthService.kernelId, new PrivilegedAction() {
            public Object run() {
               ThreadMonitoringTimer.this.checkDeadlockedThreads();
               ThreadMonitoringTimer.this.checkStuckThreads();
               return null;
            }
         });
      }

      private void checkDeadlockedThreads() {
         if (!this.alreadyDeadlocked) {
            String var1 = VM.getVM().dumpDeadlockedThreads();
            if (var1 != null) {
               T3SrvrLogger.logDeadlockedThreads(var1);
               CoreHealthService.serverRuntimeMBean.setHealthState(3, "Thread deadlock detected.");
               HealthMonitorService.subsystemFailed("core", "Thread deadlock detected");
               this.alreadyDeadlocked = true;
            }
         }
      }

      private void checkStuckThreads() {
         boolean var1 = true;
         ExecuteQueueRuntimeMBean[] var2 = CoreHealthService.serverRuntimeMBean.getExecuteQueueRuntimes();
         List var3 = Kernel.getApplicationDispatchPolicies();

         boolean var6;
         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var3.contains(var2[var4].getName())) {
               ExecuteQueueRuntimeMBean var5 = var2[var4];
               var6 = this.logStuckThreads(var5.getStuckExecuteThreads(), var5.getExecuteThreadTotalCount(), var5.getName());
               if (!var6) {
                  var1 = false;
               }
            }
         }

         ThreadPoolRuntimeMBean var7 = CoreHealthService.serverRuntimeMBean.getThreadPoolRuntime();
         if (var7 != null) {
            ExecuteThread[] var8 = var7.getStuckExecuteThreads();
            var6 = this.logStuckThreads(var8, var7.getExecuteThreadTotalCount(), var7.getName());
            if (!var6) {
               var1 = false;
            }
         }

         if (var1) {
            HealthMonitorService.subsystemFailed("core", "All execute queues and the self-tuning thread pool are stuck");
         }

      }

      private boolean logStuckThreads(ExecuteThread[] var1, int var2, String var3) {
         if (var1 == null) {
            return false;
         } else {
            long var4 = System.currentTimeMillis();

            for(int var6 = 0; var6 < var1.length; ++var6) {
               ExecuteThread var7 = var1[var6];
               long var8 = var4 - var7.getCurrentRequestStartTime();
               if (this.logStuckThreadMessage(var8)) {
                  String var10 = VM.getVM().threadDumpAsString(var7.getExecuteThread());
                  T3SrvrLogger.logWarnPossibleStuckThread(var7.getName(), var8 / 1000L, var7.getCurrentRequest(), this.stuckThreadMaxTime / 1000L, var10);
               }
            }

            if (var2 == var1.length) {
               CoreHealthService.serverRuntimeMBean.setHealthState(1, "All Threads in the queue " + var3 + " are stuck.");
               return true;
            } else {
               if (CoreHealthService.serverRuntimeMBean.getHealthState().getState() == 1) {
                  CoreHealthService.serverRuntimeMBean.setHealthState(0, "");
               }

               return false;
            }
         }
      }

      private boolean logStuckThreadMessage(long var1) {
         return var1 > this.stuckThreadMaxTime && var1 < this.stuckThreadMaxTime + 2L * this.timerInterval;
      }
   }
}
