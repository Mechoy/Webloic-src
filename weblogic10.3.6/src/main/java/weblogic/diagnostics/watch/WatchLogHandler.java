package weblogic.diagnostics.watch;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.logging.WLErrorManager;
import weblogic.logging.WLLevel;
import weblogic.logging.WLLogger;

public class WatchLogHandler extends Handler {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugDiagnosticWatch");
   private WatchManager watchManager;

   WatchLogHandler(WatchManager var1, int var2) {
      this.watchManager = var1;
      this.setErrorManager(new WLErrorManager(this));
      this.setLevel(WLLevel.getLevel(var2));
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Set Watch log handler with severity " + var2);
      }

   }

   public void close() {
   }

   public void flush() {
   }

   public void publish(LogRecord var1) {
      if (this.isLoggable(var1)) {
         this.watchManager.evaluateLogEventRules(WLLogger.normalizeLogRecord(var1));
      }
   }
}
