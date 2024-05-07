package weblogic.cluster.leasing.databaseless;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;

public class DatabaseLessLeasingLogger {
   private static final String LOCALIZER_CLASS = "weblogic.cluster.leasing.databaseless.DatabaseLessLeasingLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(DatabaseLessLeasingLogger.class.getName());
   }

   public static String logServerNotStartedByNodeManager() {
      Object[] var0 = new Object[0];
      DatabaseLessLeasingLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000001", var0, "weblogic.cluster.leasing.databaseless.DatabaseLessLeasingLogLocalizer", DatabaseLessLeasingLogger.class.getClassLoader()));
      return "000001";
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = DatabaseLessLeasingLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = DatabaseLessLeasingLogger.findMessageLogger();
      }
   }
}
