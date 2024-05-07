package weblogic.servlet.cluster;

import weblogic.diagnostics.debug.DebugLogger;

public class WANReplicationDetailsDebugLogger {
   private static final DebugLogger logger = DebugLogger.getDebugLogger("DebugWANReplicationDetails");

   public static final boolean isDebugEnabled() {
      return logger.isDebugEnabled();
   }

   public static void debug(String var0) {
      logger.debug(var0);
   }

   public static void debug(String var0, Throwable var1) {
      logger.debug(var0, var1);
   }
}
