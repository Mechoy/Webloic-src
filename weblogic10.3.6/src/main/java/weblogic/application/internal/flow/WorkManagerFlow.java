package weblogic.application.internal.flow;

import java.util.Iterator;
import weblogic.application.AdminModeCompletionBarrier;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.management.DeploymentException;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.work.RMIGracePeriodManager;
import weblogic.work.ShutdownCallback;
import weblogic.work.WorkManagerCollection;
import weblogic.work.WorkManagerLogger;
import weblogic.work.WorkManagerService;

public final class WorkManagerFlow extends BaseFlow {
   private static final DebugCategory debugWMFlow = Debug.getCategory("weblogic.workmanagerflow");

   public WorkManagerFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void prepare() throws DeploymentException {
      try {
         if (debugWMFlow.isEnabled()) {
            WorkManagerLogger.logDebug("-- wmflow -- calling prepare on - " + this.appCtx.getApplicationId());
         }

         WorkManagerCollection var1 = this.appCtx.getWorkManagerCollection();
         var1.setApplicationRuntime(this.appCtx.getRuntime());
         var1.initialize(this.appCtx.getWLApplicationDD());
      } catch (DeploymentException var2) {
         throw new DeploymentException(var2);
      }
   }

   public void activate() throws DeploymentException {
      if (debugWMFlow.isEnabled()) {
         WorkManagerLogger.logDebug("-- wmflow -- calling activate on - " + this.appCtx.getApplicationId());
      }

      WorkManagerCollection var1 = this.appCtx.getWorkManagerCollection();
      var1.setState(1);

      WorkManagerService var3;
      for(Iterator var2 = var1.iterator(); var2.hasNext(); var3.start()) {
         var3 = (WorkManagerService)var2.next();
         if (debugWMFlow.isEnabled()) {
            WorkManagerLogger.logDebug("-- wmflow -- starting - " + var3.toString());
         }
      }

   }

   public void adminToProduction() throws DeploymentException {
      if (debugWMFlow.isEnabled()) {
         WorkManagerLogger.logDebug("-- wmflow -- calling adminToProduction on - " + this.appCtx.getApplicationId());
      }

      WorkManagerCollection var1 = this.appCtx.getWorkManagerCollection();
      if (var1.getState() != 1) {
         var1.setState(1);
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            WorkManagerService var3 = (WorkManagerService)var2.next();
            if (var3.isShutdown()) {
               if (debugWMFlow.isEnabled()) {
                  WorkManagerLogger.logDebug("-- wmflow -- starting - " + var3.toString());
               }

               var3.start();
            }
         }
      }

   }

   public void forceProductionToAdmin(AdminModeCompletionBarrier var1) throws DeploymentException {
      if (debugWMFlow.isEnabled()) {
         WorkManagerLogger.logDebug("-- wmflow -- calling forceProductionToAdmin on - " + this.appCtx.getApplicationId());
      }

      WorkManagerCollection var2 = this.appCtx.getWorkManagerCollection();
      var2.setState(0);

      WorkManagerService var4;
      for(Iterator var3 = var2.iterator(); var3.hasNext(); var4.shutdown(var1.registerWMShutdown())) {
         var4 = (WorkManagerService)var3.next();
         if (debugWMFlow.isEnabled()) {
            WorkManagerLogger.logDebug("-- wmflow -- shutdown no callback - " + var4.toString());
         }
      }

   }

   public void gracefulProductionToAdmin(AdminModeCompletionBarrier var1) throws DeploymentException {
      if (debugWMFlow.isEnabled()) {
         WorkManagerLogger.logDebug("-- wmflow -- calling gracefulProductionToAdmin on - " + this.appCtx.getApplicationId());
      }

      WorkManagerCollection var2 = this.appCtx.getWorkManagerCollection();
      var2.setState(0);
      Iterator var3 = var2.iterator();
      int var4 = ApplicationVersionUtils.getRMIGracePeriodAppCtxParam(this.appCtx);
      RMIGracePeriodManager var5 = null;
      if (var4 > 0) {
         var5 = new RMIGracePeriodManager(var2, var4);
      }

      WorkManagerService var6;
      for(; var3.hasNext(); var6.shutdown(var1.registerWMShutdown())) {
         var6 = (WorkManagerService)var3.next();
         if (debugWMFlow.isEnabled()) {
            WorkManagerLogger.logDebug("-- wmflow -- shutdown with callback - " + var6.toString());
         }

         if (var4 > 0) {
            var6.startRMIGracePeriod(var5);
         }
      }

   }

   public void deactivate() throws DeploymentException {
      if (debugWMFlow.isEnabled()) {
         WorkManagerLogger.logDebug("-- wmflow -- calling deactivate on - " + this.appCtx.getApplicationId());
      }

      WorkManagerCollection var1 = this.appCtx.getWorkManagerCollection();
      if (var1.getState() != 0) {
         var1.setState(0);
         ShutdownCallback var2 = new ShutdownCallback() {
            public void completed() {
            }
         };

         WorkManagerService var4;
         for(Iterator var3 = var1.iterator(); var3.hasNext(); var4.shutdown(var2)) {
            var4 = (WorkManagerService)var3.next();
            if (debugWMFlow.isEnabled()) {
               WorkManagerLogger.logDebug("-- wmflow -- shutdown with noop callback - " + var4.toString());
            }
         }
      }

   }

   public void unprepare() throws DeploymentException {
      WorkManagerCollection var1 = this.appCtx.getWorkManagerCollection();
      if (var1 != null) {
         var1.close();
      }

   }
}
