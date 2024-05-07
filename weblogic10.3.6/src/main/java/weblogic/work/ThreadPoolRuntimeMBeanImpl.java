package weblogic.work;

import java.security.AccessController;
import java.util.ArrayList;
import weblogic.health.HealthState;
import weblogic.management.ManagementException;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ExecuteThread;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.ThreadPoolRuntimeMBean;
import weblogic.platform.VM;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class ThreadPoolRuntimeMBeanImpl extends RuntimeMBeanDelegate implements ThreadPoolRuntimeMBean {
   private static final boolean DESTRUCTIVE_DUMP = false;
   private static final String SHARED_CAPACITY_EXCEEDED = "Shared WorkManager capacity exceeded";
   private static final String STUCK_THREADS = "ThreadPool has stuck threads";
   private static final HealthState HEALTH_OK = new HealthState(0);
   private static final HealthState HEALTH_OVERLOADED = new HealthState(4, "Shared WorkManager capacity exceeded");
   private static final HealthState HEALTH_WARNING = new HealthState(1, "ThreadPool has stuck threads");
   private final transient RequestManager requestManager;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private HealthState healthState;

   ThreadPoolRuntimeMBeanImpl(RequestManager var1) throws ManagementException {
      super("ThreadPoolRuntime");
      this.healthState = HEALTH_OK;
      this.requestManager = var1;
      ManagementService.getRuntimeAccess(kernelId).getServerRuntime().setThreadPoolRuntime(this);
   }

   public ExecuteThread[] getExecuteThreads() {
      weblogic.work.ExecuteThread[] var1 = this.requestManager.getExecuteThreads();
      int var2 = var1.length;
      ExecuteThread[] var3 = new ExecuteThread[var2];

      for(int var4 = 0; var4 < var2; ++var4) {
         var3[var4] = new ExecuteThreadRuntime(var1[var4]);
      }

      return var3;
   }

   public ExecuteThread getExecuteThread(String var1) {
      if (var1 != null) {
         weblogic.work.ExecuteThread[] var2 = this.requestManager.getExecuteThreads();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            weblogic.work.ExecuteThread var5 = var2[var4];
            if (var5.getName() != null && var1.equals(var5.getName())) {
               return new ExecuteThreadRuntime(var5);
            }
         }
      }

      return null;
   }

   private int getStuckThreadCount() {
      long var1 = getConfiguredStuckThreadMaxTime(ManagementService.getRuntimeAccess(kernelId).getServer());
      ArrayList var3 = this.requestManager.getStuckThreads(var1);
      return var3 != null ? var3.size() : 0;
   }

   public ExecuteThread[] getStuckExecuteThreads() {
      long var1 = getConfiguredStuckThreadMaxTime(ManagementService.getRuntimeAccess(kernelId).getServer());
      ArrayList var3 = this.requestManager.getStuckThreads(var1);
      if (var3 != null && var3.size() != 0) {
         int var4 = var3.size();
         ExecuteThread[] var5 = new ExecuteThread[var4];

         for(int var6 = 0; var6 < var4; ++var6) {
            var5[var6] = new ExecuteThreadRuntime((weblogic.work.ExecuteThread)var3.get(var6));
         }

         return var5;
      } else {
         return null;
      }
   }

   private static final long getConfiguredStuckThreadMaxTime(ServerMBean var0) {
      return var0.getOverloadProtection().getServerFailureTrigger() != null ? (long)var0.getOverloadProtection().getServerFailureTrigger().getMaxStuckThreadTime() * 1000L : (long)var0.getStuckThreadMaxTime() * 1000L;
   }

   public int getExecuteThreadTotalCount() {
      return this.requestManager.getExecuteThreadCount();
   }

   public int getHoggingThreadCount() {
      return this.requestManager.getHogSize();
   }

   public int getStandbyThreadCount() {
      return this.requestManager.getStandbyCount();
   }

   public int getExecuteThreadIdleCount() {
      return this.requestManager.getIdleThreadCount();
   }

   public int getPendingUserRequestCount() {
      return ServerWorkManagerImpl.SHARED_OVERLOAD_MANAGER.getQueueDepth();
   }

   public int getQueueLength() {
      return this.requestManager.getQueueDepth();
   }

   public int getSharedCapacityForWorkManagers() {
      return ServerWorkManagerImpl.SHARED_OVERLOAD_MANAGER.getCapacity();
   }

   public long getCompletedRequestCount() {
      return this.requestManager.getQueueDepartures();
   }

   public double getThroughput() {
      double var1 = this.requestManager.getThroughput();
      return var1 > 0.0 ? var1 : 0.0;
   }

   public int getMinThreadsConstraintsPending() {
      return this.requestManager.getMustRunCount();
   }

   public long getMinThreadsConstraintsCompleted() {
      return this.requestManager.getMinThreadsConstraintsCompleted();
   }

   public boolean isSuspended() {
      return false;
   }

   public HealthState getHealthState() {
      HealthState var1 = HEALTH_OK;
      if (!ServerWorkManagerImpl.SHARED_OVERLOAD_MANAGER.canAcceptMore()) {
         var1 = HEALTH_OVERLOADED;
      } else if (this.getStuckThreadCount() > 0) {
         var1 = HEALTH_WARNING;
      }

      if (var1 != this.healthState) {
         this._postSet("HealthState", this.healthState, var1);
         this.healthState = var1;
      }

      return this.healthState;
   }

   private void dumpAndDestroy() {
      synchronized(RequestManager.getInstance()) {
         this.log("\n\n" + VM.getVM().threadDumpAsString() + "\n\n");
         CalendarQueue var2 = RequestManager.getInstance().queue;
         new StringBuffer();

         for(int var4 = 0; var4 < var2.size(); ++var4) {
            WorkAdapter var5 = (WorkAdapter)var2.pop();
            this.log("---- count " + var4 + " ------------- ");
            this.log(var5.dump() + "\n");
         }

         this.log("###### PRINTING MIN THREADS CONSTRAINTS #######");
         MinThreadsConstraint[] var9 = RequestManager.getInstance().minThreadsConstraints;

         for(int var8 = 0; var8 < var9.length; ++var8) {
            this.log("@@@@@@@@ MTC @@@@@@ " + var9[var8].getName());
            var9[var8].dumpAndDestroy();
         }

      }
   }

   private void log(String var1) {
      System.out.println(var1);
   }
}
