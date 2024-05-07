package weblogic.cluster.singleton;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;

public class SingletonLogger {
   private static final String LOCALIZER_CLASS = "weblogic.cluster.singleton.SingletonLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(SingletonLogger.class.getName());
   }

   public static String logServerMigrationFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      SingletonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("003501", var2, "weblogic.cluster.singleton.SingletonLogLocalizer", SingletonLogger.class.getClassLoader()));
      return "003501";
   }

   public static String logServerMigrationStarting(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      SingletonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("003502", var3, "weblogic.cluster.singleton.SingletonLogLocalizer", SingletonLogger.class.getClassLoader()));
      return "003502";
   }

   public static String logServerMigrationFinished(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      SingletonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("003503", var3, "weblogic.cluster.singleton.SingletonLogLocalizer", SingletonLogger.class.getClassLoader()));
      return "003503";
   }

   public static String logServerMigrationTargetUnreachable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      SingletonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("003504", var3, "weblogic.cluster.singleton.SingletonLogLocalizer", SingletonLogger.class.getClassLoader()));
      return "003504";
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = SingletonLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = SingletonLogger.findMessageLogger();
      }
   }
}
