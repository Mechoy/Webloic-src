package weblogic.cluster.messaging.internal;

public class DebugLogger {
   private static final weblogic.diagnostics.debug.DebugLogger logger = weblogic.diagnostics.debug.DebugLogger.getDebugLogger("DebugConsensusLeasing");

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
