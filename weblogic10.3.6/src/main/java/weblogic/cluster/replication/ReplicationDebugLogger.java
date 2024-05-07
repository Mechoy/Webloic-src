package weblogic.cluster.replication;

import weblogic.diagnostics.debug.DebugLogger;

public class ReplicationDebugLogger {
   private static final DebugLogger logger = DebugLogger.getDebugLogger("DebugReplication");

   public static final boolean isDebugEnabled() {
      return logger.isDebugEnabled();
   }

   public static void debug(String var0) {
      logger.debug(var0);
   }

   public static void debug(ROID var0, String var1) {
      logger.debug("[roid:" + var0 + "] " + var1);
   }

   public static void debug(ROID var0, String var1, Throwable var2) {
      logger.debug("[roid:" + var0 + "] " + var1, var2);
   }

   public static void debug(String var0, Throwable var1) {
      logger.debug(var0, var1);
   }
}
