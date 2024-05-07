package weblogic.kernel;

public class WLSKernelEnvironmentImpl extends KernelEnvironment {
   public void addExecuteQueueRuntime(ExecuteThreadManager var1) {
      if (KernelStatus.isServer()) {
         ExecuteQueueRuntime.addExecuteQueueRuntime(var1);
      }

   }

   public boolean isInitializeFromSystemPropertiesAllowed(String var1) {
      return true;
   }
}
