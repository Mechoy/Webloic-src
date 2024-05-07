package weblogic.application.internal.flow;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import weblogic.application.AdminModeCompletionBarrier;
import weblogic.application.ConcurrentModule;
import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.ModuleWrapper;
import weblogic.application.UpdateListener;
import weblogic.application.internal.FlowContext;
import weblogic.application.utils.StateChange;
import weblogic.application.utils.StateChangeException;
import weblogic.application.utils.StateMachineDriver;
import weblogic.j2ee.J2EELogger;
import weblogic.management.DeploymentException;
import weblogic.utils.ErrorCollectionException;
import weblogic.utils.StackTraceUtils;

public final class DeploymentCallbackFlow extends BaseFlow {
   private final StateMachineDriver driver = new StateMachineDriver();
   private static final boolean ENABLE_PREPARE_IN_PARALLEL = Boolean.getBoolean("weblogic.application.EnableDeployInParallel");
   private static final StateChange prepareStateChange = new StateChange() {
      public String toString() {
         return "prepare";
      }

      public void next(Object var1) throws Exception {
         ((Module)var1).prepare();
      }

      public void previous(Object var1) throws Exception {
         ((Module)var1).unprepare();
      }

      public void logRollbackError(StateChangeException var1) {
         BaseFlow.log("Ignoring unprepare errors " + StackTraceUtils.throwable2StackTrace(var1.getCause()));
      }
   };
   private static final StateChange activateStateChange = new StateChange() {
      public String toString() {
         return "activate";
      }

      public void next(Object var1) throws Exception {
         ((Module)var1).activate();
      }

      public void previous(Object var1) throws Exception {
         ((Module)var1).deactivate();
      }

      public void logRollbackError(StateChangeException var1) {
         BaseFlow.log("Ignoring deactivate errors " + StackTraceUtils.throwable2StackTrace(var1.getCause()));
      }
   };
   private static final StateChange removeStateChange = new StateChange() {
      public String toString() {
         return "remove";
      }

      public void next(Object var1) throws Exception {
         throw new AssertionError("someone is transitioning up to remove!");
      }

      public void previous(Object var1) throws Exception {
         ((Module)var1).remove();
      }

      public void logRollbackError(StateChangeException var1) {
         BaseFlow.log("Ignoring remove errors " + StackTraceUtils.throwable2StackTrace(var1.getCause()));
      }
   };
   private static final StateChange adminToProductionChange = new StateChange() {
      public String toString() {
         return "adminToProd";
      }

      public void next(Object var1) throws Exception {
         ((Module)var1).adminToProduction();
      }

      public void previous(Object var1) throws Exception {
         ((Module)var1).forceProductionToAdmin();
      }

      public void logRollbackError(StateChangeException var1) {
         BaseFlow.log("Ignoring errors while forcing to admin mode " + StackTraceUtils.throwable2StackTrace(var1.getCause()));
      }
   };
   private static final StateChange prepareUpdateChange = new StateChange() {
      public String toString() {
         return "prepareUpdate";
      }

      public void next(Object var1) throws Exception {
         PendingUpdate var2 = (PendingUpdate)var1;
         var2.listener.prepareUpdate(var2.uri);
      }

      public void previous(Object var1) throws Exception {
         PendingUpdate var2 = (PendingUpdate)var1;
         var2.listener.rollbackUpdate(var2.uri);
      }

      public void logRollbackError(StateChangeException var1) {
         BaseFlow.log("Ignoring errors while rolling back update " + StackTraceUtils.throwable2StackTrace(var1.getCause()));
      }
   };

   public DeploymentCallbackFlow(FlowContext var1) {
      super(var1);
   }

   public void prepare() throws DeploymentException {
      this.prepare(this.appCtx.getApplicationModules());
      this.appCtx.addUpdateListener(new PartialRedeployUpdateListener(this.appCtx));
   }

   private boolean isConcurrent(Module var1) {
      return var1 instanceof ConcurrentModule && ((ConcurrentModule)var1).isParallelEnabled();
   }

   private boolean inInSamePartition(Module var1, Module var2) {
      return !(this.isConcurrent(var1) ^ this.isConcurrent(var2));
   }

   public List<Module[]> partitionModules(Module[] var1) {
      Module[] var2 = new Module[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         Module var4 = var1[var3];
         if (var4 instanceof ModuleWrapper) {
            var4 = ((ModuleWrapper)var4).unwrap();
         }

         var2[var3] = var4;
      }

      ArrayList var8 = new ArrayList();
      if (var1 != null && var1.length > 0) {
         int var9 = 0;
         int var5 = 0;

         boolean var6;
         Module[] var7;
         for(var6 = true; var5 < var2.length; ++var5) {
            if (!this.inInSamePartition(var2[var9], var2[var5])) {
               if (var6) {
                  var6 = false;
                  if (this.isConcurrent(var2[var9])) {
                     var8.add(new Module[0]);
                  }
               }

               var7 = new Module[var5 - var9];
               System.arraycopy(var1, var9, var7, 0, var5 - var9);
               var8.add(var7);
               var9 = var5;
            }
         }

         if (var6) {
            var6 = false;
            if (this.isConcurrent(var2[var9])) {
               var8.add(new Module[0]);
            }
         }

         var7 = new Module[var5 - var9];
         System.arraycopy(var1, var9, var7, 0, var5 - var9);
         var8.add(var7);
      }

      return var8;
   }

   private void prepare(Module[] var1) throws DeploymentException {
      try {
         if (ENABLE_PREPARE_IN_PARALLEL) {
            List var2 = this.partitionModules(var1);
            boolean var3 = ((Module[])var2.get(0)).length == 0;
            boolean var4 = var3;
            int var5 = var3 ? 1 : 0;

            for(int var6 = var5; var6 < var2.size(); ++var6) {
               Module[] var7 = (Module[])var2.get(var6);
               if (var4) {
                  this.driver.nextStateInParallel(prepareStateChange, var7);
               } else {
                  this.driver.nextState(prepareStateChange, var7);
               }

               var4 = !var4;
            }
         } else {
            this.driver.nextState(prepareStateChange, var1);
         }
      } catch (StateChangeException var8) {
         this.throwAppException(var8.getCause());
      }

   }

   public void activate() throws DeploymentException {
      this.activate(this.appCtx.getApplicationModules());
   }

   private void activate(Module[] var1) throws DeploymentException {
      try {
         this.driver.nextState(activateStateChange, var1);
      } catch (StateChangeException var3) {
         this.throwAppException(var3.getCause());
      }

   }

   public void deactivate() throws DeploymentException {
      this.deactivate(this.appCtx.getApplicationModules());
   }

   private void deactivate(Module[] var1) throws DeploymentException {
      try {
         this.driver.previousState(activateStateChange, var1);
      } catch (StateChangeException var3) {
         this.throwAppException(var3.getCause());
      }

   }

   public void unprepare() throws DeploymentException {
      this.unprepare(this.appCtx.getApplicationModules());
   }

   private void unprepare(Module[] var1) throws DeploymentException {
      try {
         this.driver.previousState(prepareStateChange, var1);
      } catch (StateChangeException var7) {
         this.throwAppException(var7.getCause());
      } finally {
         invokeStaticMethodEasy(ResourceBundle.class, "clearCache", new Class[]{ClassLoader.class}, new Object[]{this.appCtx.getAppClassLoader()});
      }

   }

   public static void invokeStaticMethodEasy(Class<?> var0, String var1, Class<?>[] var2, Object[] var3) {
      try {
         Method var4 = var0.getDeclaredMethod(var1, var2);
         var4.invoke((Object)null, var3);
      } catch (SecurityException var5) {
      } catch (NoSuchMethodException var6) {
      } catch (IllegalArgumentException var7) {
      } catch (IllegalAccessException var8) {
      } catch (InvocationTargetException var9) {
      }

   }

   public void remove() throws DeploymentException {
      try {
         this.driver.previousState(removeStateChange, this.appCtx.getApplicationModules());
      } catch (StateChangeException var2) {
         this.throwAppException(var2.getCause());
      }

   }

   public void start(String[] var1) throws DeploymentException {
      Module[] var2 = this.appCtx.getStartingModules();

      try {
         this.prepare(var2);

         try {
            this.activate(var2);
         } catch (Throwable var7) {
            try {
               this.unprepare(var2);
            } catch (DeploymentException var6) {
               J2EELogger.logIgnoringUndeploymentError(var6);
            }

            throw var7;
         }
      } catch (Throwable var8) {
         try {
            this.destroy(var2);
         } catch (Throwable var5) {
            J2EELogger.logIgnoringUndeploymentError(var5);
         }

         this.throwAppException(var8);
      }

   }

   private void destroy(Module[] var1) throws DeploymentException {
      ErrorCollectionException var2 = null;
      int var3 = var1.length;

      for(int var4 = var3 - 1; var4 >= 0; --var4) {
         try {
            var1[var4].destroy(this.appCtx);
         } catch (Throwable var6) {
            if (var2 == null) {
               var2 = new ErrorCollectionException();
            }

            var2.addError(var6);
         }
      }

      if (var2 != null) {
         this.throwAppException(var2);
      }

   }

   public void stop(String[] var1) throws DeploymentException {
      Module[] var2 = this.appCtx.getStoppingModules();

      try {
         this.deactivate(var2);
      } catch (DeploymentException var5) {
         this.log("Ignoring deactivate error ", var5);
      }

      try {
         this.unprepare(var2);
      } catch (DeploymentException var4) {
         this.log("Ignoring unprepare error ", var4);
      }

   }

   private void addPendingUpdates(List var1, String var2) throws DeploymentException {
      Iterator var3 = this.appCtx.getUpdateListeners().iterator();
      boolean var4 = false;

      while(var3.hasNext()) {
         Object var5 = var3.next();
         UpdateListener var6 = (UpdateListener)var5;
         if (var6.acceptURI(var2)) {
            var4 = true;
            var1.add(new PendingUpdate(var2, var6));
         }
      }

      if (!var4) {
         log("No UpdateListener found or none of the found UpdateListeners accepts URI");
         throw new DeploymentException("\n The application " + this.appCtx.getApplicationId() + " cannot have the resource " + var2 + " updated dynamically. Either:\n" + "1.) The resource does not exist. \n" + " or \n" + "2) The resource cannot be changed dynamically. \n" + "Please ensure the resource uri is correct," + " and redeploy the entire application for this change to" + " take effect.");
      }
   }

   private PendingUpdate[] makePendingUpdates(String[] var1) throws DeploymentException {
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         this.addPendingUpdates(var2, var1[var3]);
      }

      return (PendingUpdate[])((PendingUpdate[])var2.toArray(new PendingUpdate[var2.size()]));
   }

   public void prepareUpdate(String[] var1) throws DeploymentException {
      PendingUpdate[] var2 = this.makePendingUpdates(var1);

      try {
         this.driver.nextState(prepareUpdateChange, var2);
      } catch (StateChangeException var4) {
         this.throwAppException(var4.getCause());
      }

   }

   public void rollbackUpdate(String[] var1) throws DeploymentException {
      PendingUpdate[] var2 = this.makePendingUpdates(var1);

      try {
         this.driver.previousState(prepareUpdateChange, var2);
      } catch (StateChangeException var4) {
         this.throwAppException(var4.getCause());
      }

   }

   public void activateUpdate(String[] var1) throws DeploymentException {
      PendingUpdate[] var2 = this.makePendingUpdates(var1);

      try {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            PendingUpdate var4 = var2[var3];

            try {
               var4.listener.activateUpdate(var4.uri);
            } catch (ModuleException var10) {
               throw new DeploymentException(var10);
            }
         }
      } finally {
         var2 = null;
      }

   }

   public void adminToProduction() throws DeploymentException {
      this.adminToProduction(this.appCtx.getApplicationModules());
   }

   public void adminToProduction(Module[] var1) throws DeploymentException {
      try {
         this.driver.nextState(adminToProductionChange, var1);
      } catch (StateChangeException var3) {
         this.throwAppException(var3.getCause());
      }

   }

   private void forceProductionToAdmin(Module[] var1) throws DeploymentException {
      try {
         this.driver.previousState(adminToProductionChange, var1);
      } catch (StateChangeException var3) {
         this.throwAppException(var3.getCause());
      }

   }

   public void forceProductionToAdmin(AdminModeCompletionBarrier var1) throws DeploymentException {
      this.forceProductionToAdmin(this.appCtx.getApplicationModules());
   }

   public void gracefulProductionToAdmin(AdminModeCompletionBarrier var1) throws DeploymentException {
      try {
         this.driver.previousState(new GracefulProductionToAdminChange(var1), this.appCtx.getApplicationModules());
      } catch (StateChangeException var3) {
         this.throwAppException(var3.getCause());
      }

   }

   private static final class GracefulProductionToAdminChange implements StateChange {
      private final AdminModeCompletionBarrier barrier;

      GracefulProductionToAdminChange(AdminModeCompletionBarrier var1) {
         this.barrier = var1;
      }

      public void next(Object var1) throws Exception {
         throw new AssertionError("GracefulProductionToAdminChange.next");
      }

      public void previous(Object var1) throws Exception {
         ((Module)var1).gracefulProductionToAdmin(this.barrier);
      }

      public void logRollbackError(StateChangeException var1) {
         BaseFlow.log("Ignoring errors while bringing to admin mode " + StackTraceUtils.throwable2StackTrace(var1.getCause()));
      }
   }

   private static class PendingUpdate {
      private final String uri;
      private final UpdateListener listener;

      PendingUpdate(String var1, UpdateListener var2) {
         this.uri = var1;
         this.listener = var2;
      }
   }
}
