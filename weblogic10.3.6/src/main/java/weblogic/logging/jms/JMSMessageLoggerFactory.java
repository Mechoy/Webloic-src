package weblogic.logging.jms;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import weblogic.logging.FileStreamHandler;
import weblogic.management.configuration.JMSMessageLogFileMBean;
import weblogic.management.configuration.JMSSAFMessageLogFileMBean;

public class JMSMessageLoggerFactory {
   private static Map jmsMessageLoggers = new HashMap();
   private static Map jmsSAFMessageLoggers = new HashMap();

   public static JMSMessageLogger findOrCreateJMSMessageLogger(JMSMessageLogFileMBean var0) throws IOException {
      String var1 = var0.getName();
      if (!jmsMessageLoggers.containsKey(var1)) {
         JMSMessageLogger var2 = new JMSMessageLogger(var1);
         FileStreamHandler var3 = new FileStreamHandler(var0);
         var3.setFormatter(new JMSMessageLogFileFormatter(var0));
         var2.addHandler(var3);
         jmsMessageLoggers.put(var1, var2);
      }

      return (JMSMessageLogger)jmsMessageLoggers.get(var1);
   }

   public static JMSSAFMessageLogger findOrCreateJMSSAFMessageLogger(JMSSAFMessageLogFileMBean var0) throws IOException {
      String var1 = var0.getName();
      if (!jmsSAFMessageLoggers.containsKey(var1)) {
         JMSSAFMessageLogger var2 = new JMSSAFMessageLogger(var1);
         FileStreamHandler var3 = new FileStreamHandler(var0);
         var3.setFormatter(new JMSMessageLogFileFormatter(var0));
         var2.addHandler(var3);
         jmsSAFMessageLoggers.put(var1, var2);
      }

      return (JMSSAFMessageLogger)jmsSAFMessageLoggers.get(var1);
   }
}
