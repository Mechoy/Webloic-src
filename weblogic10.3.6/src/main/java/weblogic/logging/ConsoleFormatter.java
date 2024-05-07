package weblogic.logging;

import com.bea.logging.DateFormatter;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.regex.Pattern;
import weblogic.management.configuration.CommonLogMBean;
import weblogic.management.configuration.KernelMBean;
import weblogic.management.configuration.LogMBean;
import weblogic.utils.PlatformConstants;

public class ConsoleFormatter extends Formatter implements LogEntryFormatter {
   public static final String FIELD_PREFIX = "<";
   public static final String FIELD_SUFFIX = "> ";
   private static Pattern FIELD_PREFIX_PATTERN = Pattern.compile("<");
   private static Pattern FIELD_SUFFIX_PATTERN = Pattern.compile(">");
   private static final boolean LOG_ANALYZER = Boolean.getBoolean("logAnalyzer");
   private CommonLogMBean logMBean;
   private DateFormatter dateFormatter;

   public static String formatForConsole(CommonLogMBean var0, LogEntry var1, DateFormatter var2) {
      StringBuilder var3 = new StringBuilder();
      long var4 = var1.getTimestamp();
      if (LOG_ANALYZER) {
         appendToBuffer(var3, Long.toString(var4));
      }

      var1.ensureFormattedDateInitialized(var2);
      String var6 = var1.getFormattedDate();
      appendToBuffer(var3, var6);
      appendToBuffer(var3, SeverityI18N.severityNumToString(var1.getSeverity()));
      appendToBuffer(var3, var1.getSubsystem());
      if (var0 == null || var0.getStdoutFormat().equals("standard")) {
         appendToBuffer(var3, var1.getId());
      }

      var3.append("<");
      var3.append(var1.getLogMessage());
      if ((var0 == null || var0.isStdoutLogStack()) && var1.getThrowableWrapper() != null) {
         var3.append(PlatformConstants.EOL);
         if (var0 != null && Severities.severityStringToNum(var0.getStdoutSeverity()) < 64) {
            var3.append(var1.getThrowableWrapper().toString(var0.getStacktraceDepth()));
         } else {
            var3.append(var1.getThrowableWrapper());
         }
      }

      var3.append("> ");
      var3.append(PlatformConstants.EOL);
      return var3.toString();
   }

   public ConsoleFormatter() {
      this.dateFormatter = DateFormatter.getDefaultInstance();
   }

   /** @deprecated */
   public ConsoleFormatter(KernelMBean var1) {
      this(var1.getLog());
   }

   /** @deprecated */
   public ConsoleFormatter(LogMBean var1) {
      this((CommonLogMBean)var1);
   }

   public ConsoleFormatter(CommonLogMBean var1) {
      this.dateFormatter = DateFormatter.getDefaultInstance();
      this.logMBean = var1;
      this.dateFormatter = new DateFormatter(var1.getDateFormatPattern());
   }

   public DateFormatter getDateFormatter() {
      return this.dateFormatter;
   }

   public void setDateFormatter(DateFormatter var1) {
      this.dateFormatter = var1;
   }

   public String format(LogRecord var1) {
      WLLogRecord var2 = WLLogger.normalizeLogRecord(var1);
      return this.toString(var2);
   }

   public String toString(LogEntry var1) {
      return formatForConsole(this.logMBean, var1, this.dateFormatter);
   }

   public static final String formatDateObject(Date var0) {
      return DateFormatter.getDefaultInstance().formatDate(var0);
   }

   /** @deprecated */
   protected static final void appendToBufferEscaped(StringBuffer var0, String var1) {
      if (var1 != null) {
         var1 = FIELD_PREFIX_PATTERN.matcher(var1).replaceAll("&lt;");
         var1 = FIELD_SUFFIX_PATTERN.matcher(var1).replaceAll("&gt;");
      } else {
         var1 = "";
      }

      appendToBuffer(var0, var1);
   }

   /** @deprecated */
   protected static final void appendToBuffer(StringBuffer var0, String var1) {
      var0.append("<");
      var0.append(var1 != null ? var1 : "");
      var0.append("> ");
   }

   /** @deprecated */
   protected String formatDate(Date var1) {
      return this.dateFormatter.formatDate(var1);
   }

   /** @deprecated */
   protected void appendBuf(StringBuffer var1, String var2) {
      appendToBuffer(var1, var2);
   }

   protected static final void appendToBuffer(StringBuilder var0, String var1) {
      var0.append("<");
      var0.append(var1 != null ? var1 : "");
      var0.append("> ");
   }

   protected static final void appendToBufferEscaped(StringBuilder var0, String var1) {
      if (var1 != null) {
         var1 = FIELD_PREFIX_PATTERN.matcher(var1).replaceAll("&lt;");
         var1 = FIELD_SUFFIX_PATTERN.matcher(var1).replaceAll("&gt;");
      } else {
         var1 = "";
      }

      appendToBuffer(var0, var1);
   }
}
