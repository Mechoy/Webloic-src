package weblogic.cluster;

import weblogic.diagnostics.debug.DebugLogger;

public class ClusterFragmentsDebugLogger {
   private static final DebugLogger logger = DebugLogger.getDebugLogger("DebugClusterFragments");

   public static final boolean isDebugEnabled() {
      return logger.isDebugEnabled();
   }

   public static final void debug(String var0) {
      logger.debug(var0);
   }

   public static final void debug(String var0, Throwable var1) {
      logger.debug(var0, var1);
   }
}
