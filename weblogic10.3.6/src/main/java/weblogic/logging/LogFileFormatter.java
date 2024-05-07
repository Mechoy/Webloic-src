package weblogic.logging;

import com.bea.logging.DateFormatter;
import com.bea.logging.ThrowableWrapper;
import java.util.logging.LogRecord;
import weblogic.kernel.Kernel;
import weblogic.management.configuration.LogFileMBean;
import weblogic.trace.Trace;
import weblogic.utils.PlatformConstants;

public final class LogFileFormatter extends ConsoleFormatter {
   public static final String BEGIN_MARKER = "####";
   private static final boolean TRACING_ENABLED = Kernel.isTracingEnabled();

   public LogFileFormatter() {
   }

   public LogFileFormatter(LogFileMBean var1) {
      this.setDateFormatter(new DateFormatter(var1.getDateFormatPattern()));
   }

   public String format(LogRecord var1) {
      WLLogRecord var2 = WLLogger.normalizeLogRecord(var1);
      return this.toString(var2);
   }

   public String toString(LogEntry var1) {
      return formatForLogFile(var1, this.getDateFormatter());
   }

   public static String formatForLogFile(LogEntry var0, DateFormatter var1) {
      String var2 = var0.getMachineName();
      String var3 = var0.getServerName();
      String var4 = var0.getThreadName();
      if (var4 == null) {
         var4 = Thread.currentThread().getName();
      }

      long var5 = var0.getTimestamp();
      StringBuilder var7 = new StringBuilder("####");
      if (TRACING_ENABLED && Trace.currentTrace() != null) {
         appendToBuffer(var7, "Tracing Data here");
      }

      var0.ensureFormattedDateInitialized(var1);
      appendToBuffer(var7, var0.getFormattedDate());
      appendToBuffer(var7, SeverityI18N.severityNumToString(var0.getSeverity()));
      appendToBuffer(var7, var0.getSubsystem());
      appendToBuffer(var7, var2);
      if (Kernel.isServer()) {
         appendToBuffer(var7, var3);
         appendToBuffer(var7, var4);
      }

      appendToBuffer(var7, var0.getUserId());
      appendToBuffer(var7, var0.getTransactionId());
      appendToBuffer(var7, var0.getDiagnosticContextId());
      appendToBuffer(var7, Long.toString(var5));
      appendToBuffer(var7, var0.getId());
      StringBuilder var8 = new StringBuilder();
      var8.append(var0.getLogMessage());
      ThrowableWrapper var9 = var0.getThrowableWrapper();
      if (var9 != null) {
         var8.append(PlatformConstants.EOL);
         var8.append(var9.toString(-1));
      }

      appendToBuffer(var7, var8.toString());
      var7.append(PlatformConstants.EOL);
      return var7.toString();
   }
}
