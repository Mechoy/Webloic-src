package weblogic.cache;

import java.io.IOException;
import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;

public class CacheLogger {
   private static final String LOCALIZER_CLASS = "weblogic.cache.CacheLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(CacheLogger.class.getName());
   }

   public static String logMessageException(String var0, IOException var1) {
      Object[] var2 = new Object[]{var0, var1};
      CacheLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("003000", var2, "weblogic.cache.CacheLogLocalizer", CacheLogger.class.getClassLoader()));
      return "003000";
   }

   public static String logLeaseException(Exception var0) {
      Object[] var1 = new Object[]{var0};
      CacheLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("003001", var1, "weblogic.cache.CacheLogLocalizer", CacheLogger.class.getClassLoader()));
      return "003001";
   }

   public static String logReplicationException(IOException var0) {
      Object[] var1 = new Object[]{var0};
      CacheLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("003002", var1, "weblogic.cache.CacheLogLocalizer", CacheLogger.class.getClassLoader()));
      return "003002";
   }

   public static String logDebug(String var0) {
      Object[] var1 = new Object[]{var0};
      CacheLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("003003", var1, "weblogic.cache.CacheLogLocalizer", CacheLogger.class.getClassLoader()));
      return "003003";
   }

   public static String logWarning(String var0) {
      Object[] var1 = new Object[]{var0};
      CacheLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("003004", var1, "weblogic.cache.CacheLogLocalizer", CacheLogger.class.getClassLoader()));
      return "003004";
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = CacheLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = CacheLogger.findMessageLogger();
      }
   }
}
