package weblogic.jndi.internal;

import weblogic.diagnostics.debug.DebugLogger;

public final class NamingResolutionDebugLogger {
   private static final DebugLogger logger = DebugLogger.getDebugLogger("DebugJNDIResolution");
   private static final boolean isDebugEnabled;

   public static final boolean isDebugEnabled() {
      return isDebugEnabled;
   }

   public static final void debug(String var0) {
      logger.debug(var0);
   }

   public static final void debug(String var0, Throwable var1) {
      logger.debug(var0, var1);
   }

   static {
      isDebugEnabled = logger.isDebugEnabled();
   }
}
