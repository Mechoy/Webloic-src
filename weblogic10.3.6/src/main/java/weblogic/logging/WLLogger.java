package weblogic.logging;

import com.bea.logging.BaseLogger;
import java.util.WeakHashMap;
import java.util.logging.LogRecord;

public class WLLogger extends BaseLogger {
   private boolean isDomainLogger;
   private static final WeakHashMap NORMALIZED_CACHE = new WeakHashMap();

   public WLLogger(String var1) {
      this(var1, false);
   }

   public WLLogger(String var1, boolean var2) {
      super(var1);
      this.isDomainLogger = false;
      this.isDomainLogger = var2;
   }

   public void log(LogRecord var1) {
      if (!(var1 instanceof WLLogRecord)) {
         var1 = normalizeLogRecord((LogRecord)var1);
      }

      if (!this.isDomainLogger) {
         LogEntryInitializer.initializeLogEntry((WLLogRecord)var1);
      }

      super.log((LogRecord)var1);
   }

   public static WLLogRecord normalizeLogRecord(LogRecord var0) {
      if (var0 instanceof WLLogRecord) {
         return (WLLogRecord)var0;
      } else {
         WLLogRecord var1 = null;
         synchronized(NORMALIZED_CACHE) {
            var1 = (WLLogRecord)NORMALIZED_CACHE.get(var0);
         }

         if (var1 != null) {
            return var1;
         } else {
            var1 = new WLLogRecord(var0.getLevel(), formatMessage(var0), var0.getThrown());
            var1.setLoggerName(var0.getLoggerName() == null ? "Default" : var0.getLoggerName());
            var1.setMillis(var0.getMillis());
            var1.setParameters(var0.getParameters());
            var1.setResourceBundle(var0.getResourceBundle());
            var1.setResourceBundleName(var0.getResourceBundleName());
            var1.setSequenceNumber(var0.getSequenceNumber());
            var1.setSourceClassName(var0.getSourceClassName());
            var1.setSourceMethodName(var0.getSourceMethodName());
            var1.setThreadID(var0.getThreadID());
            var1.setThrown(var0.getThrown());
            synchronized(NORMALIZED_CACHE) {
               NORMALIZED_CACHE.put(var0, var1);
               return var1;
            }
         }
      }
   }
}
