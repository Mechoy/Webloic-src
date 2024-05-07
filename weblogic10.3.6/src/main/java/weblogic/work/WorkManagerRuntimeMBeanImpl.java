package weblogic.work;

import java.security.AccessController;
import java.util.ArrayList;
import weblogic.health.HealthState;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ApplicationRuntimeMBean;
import weblogic.management.runtime.ExecuteThread;
import weblogic.management.runtime.MaxThreadsConstraintRuntimeMBean;
import weblogic.management.runtime.MinThreadsConstraintRuntimeMBean;
import weblogic.management.runtime.RequestClassRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.ThreadPoolRuntimeMBean;
import weblogic.management.runtime.WorkManagerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class WorkManagerRuntimeMBeanImpl extends RuntimeMBeanDelegate implements WorkManagerRuntimeMBean {
   private static final HealthState HEALTH_OK = new HealthState(0);
   private final ServerWorkManagerImpl wm;
   private MinThreadsConstraintRuntimeMBean minThreadsConstraintRuntimeMBean;
   private MaxThreadsConstraintRuntimeMBean maxThreadsConstraintRuntimeMBean;
   private RequestClassRuntimeMBean requestClassRuntimeMBean;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private int pastStuckThreadCount;
   private long lastTime;

   WorkManagerRuntimeMBeanImpl(ServerWorkManagerImpl var1) throws ManagementException {
      super(var1.getName());
      this.wm = var1;
   }

   private WorkManagerRuntimeMBeanImpl(ServerWorkManagerImpl var1, RuntimeMBean var2) throws ManagementException {
      super(var1.getName(), var2);
      this.wm = var1;
   }

   public static synchronized WorkManagerRuntimeMBean getWorkManagerRuntime(WorkManager var0, ApplicationRuntimeMBean var1, RuntimeMBean var2) throws ManagementException {
      if (!(var0 instanceof ServerWorkManagerImpl)) {
         return null;
      } else {
         ServerWorkManagerImpl var3 = (ServerWorkManagerImpl)var0;
         WorkManagerRuntimeMBeanImpl var4 = new WorkManagerRuntimeMBeanImpl(var3, var2);
         var4.setRequestClassRuntime(getRequestClassRuntime(var3.getRequestClass(), var1, var4));
         var4.setMinThreadsConstraintRuntime(getMinThreadsConstraintRuntime(var3.getMinThreadsConstraint(), var1, var4));
         var4.setMaxThreadsConstraintRuntime(getMaxThreadsConstraintRuntime(var3.getMaxThreadsConstraint(), var1, var4));
         return var4;
      }
   }

   private static RequestClassRuntimeMBean getRequestClassRuntime(RequestClass var0, ApplicationRuntimeMBean var1, WorkManagerRuntimeMBean var2) throws ManagementException {
      if (var0 == null) {
         return null;
      } else {
         Object var3 = null;
         var3 = var1.lookupRequestClassRuntime(var0.getName());
         if (var3 == null) {
            var3 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime().lookupRequestClassRuntime(var0.getName());
         }

         if (var3 == null) {
            var3 = new RequestClassRuntimeMBeanImpl(var0, var2, true);
         }

         return (RequestClassRuntimeMBean)var3;
      }
   }

   private static MinThreadsConstraintRuntimeMBean getMinThreadsConstraintRuntime(MinThreadsConstraint var0, ApplicationRuntimeMBean var1, WorkManagerRuntimeMBean var2) throws ManagementException {
      if (var0 == null) {
         return null;
      } else {
         Object var3 = null;
         var3 = var1.lookupMinThreadsConstraintRuntime(var0.getName());
         if (var3 == null) {
            var3 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime().lookupMinThreadsConstraintRuntime(var0.getName());
         }

         if (var3 == null) {
            var3 = new MinThreadsConstraintRuntimeMBeanImpl(var0, var2, true);
         }

         return (MinThreadsConstraintRuntimeMBean)var3;
      }
   }

   private static MaxThreadsConstraintRuntimeMBean getMaxThreadsConstraintRuntime(MaxThreadsConstraint var0, ApplicationRuntimeMBean var1, WorkManagerRuntimeMBean var2) throws ManagementException {
      if (var0 == null) {
         return null;
      } else {
         Object var3 = null;
         var3 = var1.lookupMaxThreadsConstraintRuntime(var0.getName());
         if (var3 == null) {
            var3 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime().lookupMaxThreadsConstraintRuntime(var0.getName());
         }

         if (var3 == null) {
            var3 = new MaxThreadsConstraintRuntimeMBeanImpl(var0, var2, true);
         }

         return (MaxThreadsConstraintRuntimeMBean)var3;
      }
   }

   public String getApplicationName() {
      return this.wm.getApplicationName();
   }

   public String getModuleName() {
      return this.wm.getModuleName();
   }

   public int getPendingRequests() {
      int var1 = (int)(this.wm.getAcceptedCount() - this.wm.getCompletedCount());
      return var1 > 0 ? var1 : 0;
   }

   public long getCompletedRequests() {
      return this.wm.getCompletedCount();
   }

   public int getStuckThreadCount() {
      if (this.timeToSync()) {
         ThreadPoolRuntimeMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime().getThreadPoolRuntime();
         ExecuteThread[] var2 = var1.getStuckExecuteThreads();
         if (var2 == null) {
            this.pastStuckThreadCount = 0;
            return 0;
         } else {
            int var3 = 0;

            for(int var4 = 0; var4 < var2.length; ++var4) {
               if (this.claimThread(var2[var4])) {
                  ++var3;
               }
            }

            this.pastStuckThreadCount = var3;
            return var3;
         }
      } else {
         return this.pastStuckThreadCount;
      }
   }

   private synchronized boolean timeToSync() {
      long var1 = System.currentTimeMillis();
      if (var1 - this.lastTime > 10000L) {
         this.lastTime = var1;
         return true;
      } else {
         return false;
      }
   }

   private boolean claimThread(ExecuteThread var1) {
      Thread var2 = var1.getExecuteThread();
      if (var2 != null && var2 instanceof weblogic.work.ExecuteThread) {
         weblogic.work.ExecuteThread var3 = (weblogic.work.ExecuteThread)var2;
         return this.wm.equals(var3.getWorkManager());
      } else {
         return compareNames(var1.getWorkManagerName(), this.getName()) && compareNames(var1.getApplicationName(), this.getApplicationName()) && compareNames(var1.getModuleName(), this.getModuleName());
      }
   }

   private static boolean compareNames(String var0, String var1) {
      if (var0 == null && var1 == null) {
         return true;
      } else {
         return var0 != null && var0.equalsIgnoreCase(var1);
      }
   }

   public MinThreadsConstraintRuntimeMBean getMinThreadsConstraintRuntime() {
      return this.minThreadsConstraintRuntimeMBean;
   }

   public MaxThreadsConstraintRuntimeMBean getMaxThreadsConstraintRuntime() {
      return this.maxThreadsConstraintRuntimeMBean;
   }

   public void setMinThreadsConstraintRuntime(MinThreadsConstraintRuntimeMBean var1) {
      this.minThreadsConstraintRuntimeMBean = var1;
   }

   public void setMaxThreadsConstraintRuntime(MaxThreadsConstraintRuntimeMBean var1) {
      this.maxThreadsConstraintRuntimeMBean = var1;
   }

   public void setRequestClassRuntime(RequestClassRuntimeMBean var1) {
      this.requestClassRuntimeMBean = var1;
   }

   public RequestClassRuntimeMBean getRequestClassRuntime() {
      return this.requestClassRuntimeMBean;
   }

   public HealthState getHealthState() {
      ArrayList var1 = new ArrayList();
      byte var2 = 0;
      if (this.wm.isInternal()) {
         return HEALTH_OK;
      } else {
         if (this.wm.getOverloadManager() != null && !this.wm.getOverloadManager().canAcceptMore()) {
            var2 = 4;
            var1.add(ServerWorkManagerImpl.getOverloadMessage(this.wm.getOverloadManager()));
         }

         if (!ServerWorkManagerImpl.SHARED_OVERLOAD_MANAGER.canAcceptMore()) {
            var2 = 4;
            var1.add(ServerWorkManagerImpl.getOverloadMessage(ServerWorkManagerImpl.SHARED_OVERLOAD_MANAGER));
         }

         if (ServerWorkManagerImpl.LOW_MEMORY_LISTENER.lowMemory()) {
            var2 = 4;
            var1.add(this.wm.getLowMemoryMessage());
         }

         if (this.wm.getStuckThreadManager() != null && this.wm.getStuckThreadManager().getStuckThreadCount() > 0) {
            var2 = 2;
            var1.add(this.wm.getStuckThreadManager().getStuckThreadCount() + " stuck threads detected in WorkManager '" + this.wm.getName() + "'");
         }

         if (var1.size() == 0) {
            return new HealthState(var2);
         } else {
            String[] var3 = new String[var1.size()];
            var1.toArray(var3);
            return new HealthState(var2, var3);
         }
      }
   }
}
