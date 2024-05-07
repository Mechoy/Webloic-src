package weblogic.logging.jms;

import com.bea.logging.DateFormatter;
import java.util.Date;
import java.util.logging.LogRecord;
import weblogic.logging.ConsoleFormatter;
import weblogic.management.configuration.LogFileMBean;
import weblogic.utils.PlatformConstants;

public final class JMSMessageLogFileFormatter extends ConsoleFormatter {
   public static final String BEGIN_MARKER = "####";
   private static final String UNKNOWN = "Unknown";

   public JMSMessageLogFileFormatter(LogFileMBean var1) {
      this.setDateFormatter(new DateFormatter(var1.getDateFormatPattern()));
   }

   public String format(LogRecord var1) {
      return this.formatJMSMessageLogRecord((JMSMessageLogRecord)var1);
   }

   private String formatJMSMessageLogRecord(JMSMessageLogRecord var1) {
      long var2 = var1.getEventTimeMillisStamp();
      String var4 = this.formatDate(new Date(var2));
      StringBuilder var5 = new StringBuilder("####");
      appendToBuffer(var5, var4);
      appendToBuffer(var5, var1.getTransactionId());
      appendToBuffer(var5, var1.getDiagnosticContextId());
      appendToBuffer(var5, Long.toString(var2));
      appendToBuffer(var5, Long.toString(var1.getEventTimeNanoStamp()));
      appendToBuffer(var5, var1.getJMSMessageId());
      appendToBuffer(var5, var1.getJMSCorrelationId());
      appendToBuffer(var5, var1.getJMSDestinationName());
      appendToBuffer(var5, var1.getJMSMessageState());
      appendToBuffer(var5, var1.getUser());
      appendToBuffer(var5, var1.getDurableSubscriber());
      appendToBufferEscaped(var5, var1.getMessage());
      if (var1 instanceof JMSMessageConsumerCreationLogRecord) {
         appendToBufferEscaped(var5, ((JMSMessageConsumerCreationLogRecord)var1).getSelector());
      } else {
         appendToBuffer(var5, (String)null);
      }

      var5.append(PlatformConstants.EOL);
      return var5.toString();
   }
}
