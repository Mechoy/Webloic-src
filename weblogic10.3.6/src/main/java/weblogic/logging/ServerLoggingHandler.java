package weblogic.logging;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import weblogic.diagnostics.debug.DebugLogger;

public class ServerLoggingHandler extends Handler {
   public void close() throws SecurityException {
   }

   public void flush() {
   }

   public void publish(LogRecord var1) {
      if (this.isLoggable(var1)) {
         String var2 = var1.getLoggerName();
         if (var2 == null) {
            var2 = "";
         }

         Logger var3 = DebugLogger.getDefaultDebugLoggerRepository().getLogger();
         var3.log(var1);
      }

   }
}
