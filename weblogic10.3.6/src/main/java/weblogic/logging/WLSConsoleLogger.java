package weblogic.logging;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;

public class WLSConsoleLogger {
   private static final String LOCALIZER_CLASS = "weblogic.logging.WLSConsoleLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(WLSConsoleLogger.class.getName());
   }

   public static String logConsoleDebug(String var0) {
      Object[] var1 = new Object[]{var0};
      WLSConsoleLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("240000", var1, "weblogic.logging.WLSConsoleLogLocalizer", WLSConsoleLogger.class.getClassLoader()));
      return "240000";
   }

   public static String logConsoleInfo(String var0) {
      Object[] var1 = new Object[]{var0};
      WLSConsoleLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("240001", var1, "weblogic.logging.WLSConsoleLogLocalizer", WLSConsoleLogger.class.getClassLoader()));
      return "240001";
   }

   public static String logConsoleWarn(String var0) {
      Object[] var1 = new Object[]{var0};
      WLSConsoleLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("240002", var1, "weblogic.logging.WLSConsoleLogLocalizer", WLSConsoleLogger.class.getClassLoader()));
      return "240002";
   }

   public static String logConsoleError(String var0) {
      Object[] var1 = new Object[]{var0};
      WLSConsoleLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("240003", var1, "weblogic.logging.WLSConsoleLogLocalizer", WLSConsoleLogger.class.getClassLoader()));
      return "240003";
   }

   public static String logConsoleFatal(String var0) {
      Object[] var1 = new Object[]{var0};
      WLSConsoleLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("240004", var1, "weblogic.logging.WLSConsoleLogLocalizer", WLSConsoleLogger.class.getClassLoader()));
      return "240004";
   }

   public static String logCSRF(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      WLSConsoleLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("240005", var3, "weblogic.logging.WLSConsoleLogLocalizer", WLSConsoleLogger.class.getClassLoader()));
      return "240005";
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = WLSConsoleLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = WLSConsoleLogger.findMessageLogger();
      }
   }
}
