package weblogic.servlet.internal;

import weblogic.diagnostics.debug.DebugLogger;

public final class HTTPDebugLogger {
   public static final DebugLogger DEBUG_HTTP = DebugLogger.getDebugLogger("DebugHttp");

   public static final boolean isEnabled() {
      return DEBUG_HTTP.isDebugEnabled();
   }

   public static final void debug(String var0) {
      DEBUG_HTTP.debug(var0);
   }

   public static final void debug(String var0, Exception var1) {
      DEBUG_HTTP.debug(var0, var1);
   }
}
