package weblogic.work;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.AccessController;
import weblogic.health.HealthMonitorService;
import weblogic.health.HealthState;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class ServerWorkManagerImpl extends SelfTuningWorkManagerImpl {
   static LowMemoryListener LOW_MEMORY_LISTENER;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   static void initialize() {
      SHARED_OVERLOAD_MANAGER = new OverloadManager("global overload manager");
      SHARED_OVERLOAD_MANAGER.setCapacity(ManagementService.getRuntimeAccess(kernelId).getServer().getOverloadProtection().getSharedCapacityForWorkManagers());
      LOW_MEMORY_LISTENER = new LowMemoryListener();
      setDebugLogger(new SelfTuningWorkManagerImpl.Logger() {
         public boolean debugEnabled() {
            return SelfTuningDebugLogger.isDebugEnabled();
         }

         public void log(String var1) {
            SelfTuningDebugLogger.debug("<ThreadPriorityManager>" + var1);
         }
      });
   }

   ServerWorkManagerImpl(String var1, String var2, String var3, RequestClass var4, MaxThreadsConstraint var5, MinThreadsConstraint var6, OverloadManager var7, StuckThreadManager var8) {
      super(var1, var2, var3, var4, var5, var6, var7, var8);
      WorkManagerImageSource.getInstance().register(this);
   }

   protected static void notifyOOME(OutOfMemoryError var0) {
      HealthMonitorService.panic(var0);
   }

   protected boolean accept(Runnable var1) {
      if (this.isInternal()) {
         return true;
      } else if (!(var1 instanceof Work)) {
         return true;
      } else {
         String var2 = null;
         if (LOW_MEMORY_LISTENER.lowMemory()) {
            var2 = this.getLowMemoryMessage();
         } else {
            OverloadManager var3 = this.getRejectingOverloadManager();
            if (var3 != null) {
               var2 = getOverloadMessage(var3);
            }
         }

         if (var2 == null) {
            return true;
         } else {
            Runnable var4 = ((Work)var1).overloadAction(var2);
            if (var4 != null && !isAdminWork(var1)) {
               WorkManagerFactory.getInstance().getRejector().schedule(var4);
               return false;
            } else {
               return true;
            }
         }
      }
   }

   private static final boolean isAdminWork(Runnable var0) {
      if (!(var0 instanceof ServerWorkAdapter)) {
         return false;
      } else {
         AuthenticatedSubject var1 = ((ServerWorkAdapter)var0).getAuthenticatedSubject();
         return var1 == null ? false : SubjectUtils.doesUserHaveAnyAdminRoles(var1);
      }
   }

   final String getLowMemoryMessage() {
      return WorkManagerLogger.logLowMemoryLoggable(this.wmName).getMessage();
   }

   public void cleanup() {
      super.cleanup();
      WorkManagerImageSource.getInstance().deregister(this);
   }

   static final class LowMemoryListener implements PropertyChangeListener {
      private static boolean lowMemory;

      private LowMemoryListener() {
         ServerRuntimeMBean var1 = ManagementService.getRuntimeAccess(ServerWorkManagerImpl.kernelId).getServerRuntime();
         var1.addPropertyChangeListener(this);
      }

      boolean lowMemory() {
         return lowMemory;
      }

      public void propertyChange(PropertyChangeEvent var1) {
         if (var1 != null) {
            if ("HealthState".equals(var1.getPropertyName())) {
               int var2 = ((HealthState)var1.getNewValue()).getState();
               if (var2 == 4) {
                  lowMemory = true;
               } else {
                  if (var2 == 0) {
                     lowMemory = false;
                  }

               }
            }
         }
      }

      // $FF: synthetic method
      LowMemoryListener(Object var1) {
         this();
      }
   }
}
