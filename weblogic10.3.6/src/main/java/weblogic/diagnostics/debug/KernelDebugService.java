package weblogic.diagnostics.debug;

import java.util.logging.Logger;

public class KernelDebugService {
   private static KernelDebugService singleton = null;

   public static KernelDebugService getKernelDebugService() {
      if (singleton == null) {
         singleton = new KernelDebugService();
      }

      return singleton;
   }

   KernelDebugService() {
   }

   public void initializeDebugLogging(Logger var1) {
      DebugLogger.getDefaultDebugLoggerRepository().setLogger(var1);
   }
}
