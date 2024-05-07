package weblogic.application.internal.flow;

import weblogic.application.AdminModeCompletionBarrier;
import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.UpdateListener;
import weblogic.application.utils.ExceptionUtils;
import weblogic.application.utils.StateChange;
import weblogic.application.utils.StateChangeException;
import weblogic.application.utils.StateMachineDriver;
import weblogic.utils.Debug;
import weblogic.utils.StackTraceUtils;

final class ModuleStateDriver {
   private final StateMachineDriver driver = new StateMachineDriver();
   private final UpdateListener.Registration updateListenerRegistration;
   private static final StateChange prepareStateChange = new StateChange() {
      public String toString() {
         return "module_prepare";
      }

      public void next(Object var1) throws Exception {
         ((Module)var1).prepare();
      }

      public void previous(Object var1) throws Exception {
         ((Module)var1).unprepare();
      }

      public void logRollbackError(StateChangeException var1) {
         ModuleStateDriver.log("Ignoring unprepare errors " + StackTraceUtils.throwable2StackTrace(var1.getCause()));
      }
   };
   private static final StateChange activateStateChange = new StateChange() {
      public String toString() {
         return "module_activate";
      }

      public void next(Object var1) throws Exception {
         ((Module)var1).activate();
      }

      public void previous(Object var1) throws Exception {
         ((Module)var1).deactivate();
      }

      public void logRollbackError(StateChangeException var1) {
         ModuleStateDriver.log("Ignoring deactivate errors " + StackTraceUtils.throwable2StackTrace(var1.getCause()));
      }
   };
   private static final StateChange startStateChange = new StateChange() {
      public String toString() {
         return "module_start";
      }

      public void next(Object var1) throws Exception {
         ((Module)var1).start();
      }

      public void previous(Object var1) {
      }

      public void logRollbackError(StateChangeException var1) {
      }
   };
   private static final StateChange removeStateChange = new StateChange() {
      public String toString() {
         return "module_remove";
      }

      public void next(Object var1) throws Exception {
         throw new AssertionError("someone is transitioning up to remove!");
      }

      public void previous(Object var1) throws Exception {
         ((Module)var1).remove();
      }

      public void logRollbackError(StateChangeException var1) {
         ModuleStateDriver.log("Ignoring remove errors " + StackTraceUtils.throwable2StackTrace(var1.getCause()));
      }
   };
   private static final StateChange adminToProductionChange = new StateChange() {
      public String toString() {
         return "module_adminToProd";
      }

      public void next(Object var1) throws Exception {
         ((Module)var1).adminToProduction();
      }

      public void previous(Object var1) throws Exception {
         ((Module)var1).forceProductionToAdmin();
      }

      public void logRollbackError(StateChangeException var1) {
         ModuleStateDriver.log("Ignoring errors while forcing to admin mode " + StackTraceUtils.throwable2StackTrace(var1.getCause()));
      }
   };

   ModuleStateDriver(UpdateListener.Registration var1) {
      this.updateListenerRegistration = var1;
   }

   public void prepare(Module[] var1) throws ModuleException {
      try {
         this.driver.nextState(prepareStateChange, var1);
      } catch (StateChangeException var3) {
         this.throwException(var3.getCause());
      }

   }

   public void activate(Module[] var1) throws ModuleException {
      try {
         this.driver.nextState(activateStateChange, var1);
      } catch (StateChangeException var3) {
         this.throwException(var3.getCause());
      }

   }

   public void start(Module[] var1) throws ModuleException {
      try {
         this.driver.nextState(startStateChange, var1);
      } catch (StateChangeException var3) {
         this.throwException(var3.getCause());
      }

   }

   public void deactivate(Module[] var1) throws ModuleException {
      try {
         this.driver.previousState(activateStateChange, var1);
      } catch (StateChangeException var3) {
         this.throwException(var3.getCause());
      }

   }

   public void unprepare(Module[] var1) throws ModuleException {
      try {
         this.driver.previousState(prepareStateChange, var1);
      } catch (StateChangeException var3) {
         this.throwException(var3.getCause());
      }

   }

   public void remove(Module[] var1) throws ModuleException {
      try {
         this.driver.previousState(removeStateChange, var1);
      } catch (StateChangeException var3) {
         this.throwException(var3.getCause());
      }

   }

   public void destroy(Module[] var1) throws ModuleException {
      try {
         this.driver.previousState(new DestroyStateChange(this.updateListenerRegistration), var1);
      } catch (StateChangeException var3) {
         this.throwException(var3.getCause());
      }

   }

   public void adminToProduction(Module[] var1) throws ModuleException {
      try {
         this.driver.nextState(adminToProductionChange, var1);
      } catch (StateChangeException var3) {
         this.throwException(var3.getCause());
      }

   }

   public void forceProductionToAdmin(Module[] var1) throws ModuleException {
      try {
         this.driver.previousState(adminToProductionChange, var1);
      } catch (StateChangeException var3) {
         this.throwException(var3.getCause());
      }

   }

   public void gracefulProductionToAdmin(AdminModeCompletionBarrier var1, Module[] var2) throws ModuleException {
      try {
         this.driver.previousState(new GracefulProductionToAdminChange(var1), var2);
      } catch (StateChangeException var4) {
         this.throwException(var4.getCause());
      }

   }

   private static void log(String var0) {
      Debug.say(var0);
   }

   private void throwException(Throwable var1) throws ModuleException {
      ExceptionUtils.throwModuleException(var1);
   }

   private static final class GracefulProductionToAdminChange implements StateChange {
      private final AdminModeCompletionBarrier barrier;

      public String toString() {
         return "module_gracefulProdToAdmin";
      }

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
         ModuleStateDriver.log("Ignoring errors while bringing to admin mode " + StackTraceUtils.throwable2StackTrace(var1.getCause()));
      }
   }

   private static final class DestroyStateChange implements StateChange {
      private final UpdateListener.Registration reg;

      DestroyStateChange(UpdateListener.Registration var1) {
         this.reg = var1;
      }

      public String toString() {
         return "module_destroy";
      }

      public void next(Object var1) throws Exception {
         throw new AssertionError("someone is transitioning up to destroy!");
      }

      public void previous(Object var1) throws Exception {
         ((Module)var1).destroy(this.reg);
      }

      public void logRollbackError(StateChangeException var1) {
         ModuleStateDriver.log("Ignoring destroy errors " + StackTraceUtils.throwable2StackTrace(var1.getCause()));
      }
   }
}
