package weblogic.cacheprovider;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class CacheProviderLogger {
   private static final String LOCALIZER_CLASS = "weblogic.cacheprovider.CacheProviderLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(CacheProviderLogger.class.getName());
   }

   public static String logMultipleCoherenceClusterSystemResourceMBeanTargetted(String var0) {
      Object[] var1 = new Object[]{var0};
      CacheProviderLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("2156300", var1, "weblogic.cacheprovider.CacheProviderLogLocalizer", CacheProviderLogger.class.getClassLoader()));
      return "2156300";
   }

   public static Loggable logMultipleCoherenceClusterSystemResourceMBeanTargettedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("2156300", var1, "weblogic.cacheprovider.CacheProviderLogLocalizer", CacheProviderLogger.MessageLoggerInitializer.INSTANCE.messageLogger, CacheProviderLogger.class.getClassLoader());
   }

   public static String logFailedToUnprepare(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      CacheProviderLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("2156301", var2, "weblogic.cacheprovider.CacheProviderLogLocalizer", CacheProviderLogger.class.getClassLoader()));
      return "2156301";
   }

   public static Loggable logFailedToUnprepareLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("2156301", var2, "weblogic.cacheprovider.CacheProviderLogLocalizer", CacheProviderLogger.MessageLoggerInitializer.INSTANCE.messageLogger, CacheProviderLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = CacheProviderLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = CacheProviderLogger.findMessageLogger();
      }
   }
}
