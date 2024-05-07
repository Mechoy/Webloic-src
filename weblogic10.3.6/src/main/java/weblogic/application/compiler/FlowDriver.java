package weblogic.application.compiler;

import weblogic.application.compiler.flow.CompilerFlow;
import weblogic.application.utils.StateChange;
import weblogic.application.utils.StateChangeException;
import weblogic.application.utils.StateMachineDriver;
import weblogic.utils.compiler.ToolFailureException;

class FlowDriver {
   private static final String DEFAULT_ERROR = "weblogic.appc failed";
   private final FlowStateChange fsc = new FlowStateChange();
   private final StateMachineDriver driver = new StateMachineDriver();

   public FlowDriver() {
   }

   void run(CompilerFlow[] var1) throws ToolFailureException {
      this.nextState(var1);
      this.previousState(var1);
   }

   void nextState(CompilerFlow[] var1) throws ToolFailureException {
      try {
         this.driver.nextState(this.fsc, var1);
      } catch (StateChangeException var3) {
         handleStateChangeException(var3);
      }

   }

   void previousState(CompilerFlow[] var1) throws ToolFailureException {
      try {
         this.driver.previousState(this.fsc, var1);
      } catch (StateChangeException var3) {
         handleStateChangeException(var3);
      }

   }

   private static void handleStateChangeException(StateChangeException var0) throws ToolFailureException {
      Throwable var1 = getCause(var0);
      if (var1 instanceof ToolFailureException) {
         throw (ToolFailureException)var1;
      } else {
         throw new ToolFailureException(var1.getMessage() == null ? "weblogic.appc failed" : var1.getMessage(), var1);
      }
   }

   private static Throwable getCause(Throwable var0) {
      return var0.getCause() == null ? var0 : getCause(var0.getCause());
   }

   public static class CompilerFlowDriver extends FlowDriver {
      CompilerFlow[] flows;

      public CompilerFlowDriver(CompilerFlow[] var1) {
         this.flows = var1;
      }

      public void compile() throws ToolFailureException {
         this.nextState(this.flows);
      }

      public void cleanup() throws ToolFailureException {
         this.previousState(this.flows);
      }
   }

   private static final class FlowStateChange implements StateChange {
      private FlowStateChange() {
      }

      public void next(Object var1) throws Exception {
         ((CompilerFlow)var1).compile();
      }

      public void previous(Object var1) throws Exception {
         ((CompilerFlow)var1).cleanup();
      }

      public void logRollbackError(StateChangeException var1) {
         System.err.println("Error cleaning up " + var1);
         var1.printStackTrace();
      }

      // $FF: synthetic method
      FlowStateChange(Object var1) {
         this();
      }
   }
}
