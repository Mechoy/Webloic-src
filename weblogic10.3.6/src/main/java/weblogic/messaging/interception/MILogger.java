package weblogic.messaging.interception;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;

public class MILogger {
   private static final String LOCALIZER_CLASS = "weblogic.messaging.interception.MILogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(MILogger.class.getName());
   }

   public static String logStartMessageInterceptionService() {
      Object[] var0 = new Object[0];
      MILogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("400000", var0, "weblogic.messaging.interception.MILogLocalizer", MILogger.class.getClassLoader()));
      return "400000";
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = MILogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = MILogger.findMessageLogger();
      }
   }
}
