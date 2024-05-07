package weblogic.wsee;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class WseePersistLogger {
   private static final String LOCALIZER_CLASS = "weblogic.wsee.WseePersistLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(WseePersistLogger.class.getName());
   }

   public static String logUnexpectedException(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseePersistLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("221000", var2, "weblogic.wsee.WseePersistLogLocalizer", WseePersistLogger.class.getClassLoader()));
      return "221000";
   }

   public static Loggable logUnexpectedExceptionLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("221000", var2, "weblogic.wsee.WseePersistLogLocalizer", WseePersistLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseePersistLogger.class.getClassLoader());
   }

   public static String logStoreNameNotSet() {
      Object[] var0 = new Object[0];
      WseePersistLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("221001", var0, "weblogic.wsee.WseePersistLogLocalizer", WseePersistLogger.class.getClassLoader()));
      return "221001";
   }

   public static Loggable logStoreNameNotSetLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("221001", var0, "weblogic.wsee.WseePersistLogLocalizer", WseePersistLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseePersistLogger.class.getClassLoader());
   }

   public static String logNetworkStoreNotXA(String var0) {
      Object[] var1 = new Object[]{var0};
      WseePersistLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("221002", var1, "weblogic.wsee.WseePersistLogLocalizer", WseePersistLogger.class.getClassLoader()));
      return "221002";
   }

   public static Loggable logNetworkStoreNotXALoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("221002", var1, "weblogic.wsee.WseePersistLogLocalizer", WseePersistLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseePersistLogger.class.getClassLoader());
   }

   public static String logStoreExists(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseePersistLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("221003", var2, "weblogic.wsee.WseePersistLogLocalizer", WseePersistLogger.class.getClassLoader()));
      return "221003";
   }

   public static Loggable logStoreExistsLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("221003", var2, "weblogic.wsee.WseePersistLogLocalizer", WseePersistLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseePersistLogger.class.getClassLoader());
   }

   public static String logLogicalStoreNotFound(String var0) {
      Object[] var1 = new Object[]{var0};
      WseePersistLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("221004", var1, "weblogic.wsee.WseePersistLogLocalizer", WseePersistLogger.class.getClassLoader()));
      return "221004";
   }

   public static Loggable logLogicalStoreNotFoundLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("221004", var1, "weblogic.wsee.WseePersistLogLocalizer", WseePersistLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseePersistLogger.class.getClassLoader());
   }

   public static String logAttemptedClientIdReset(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseePersistLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("221005", var2, "weblogic.wsee.WseePersistLogLocalizer", WseePersistLogger.class.getClassLoader()));
      return "221005";
   }

   public static Loggable logAttemptedClientIdResetLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("221005", var2, "weblogic.wsee.WseePersistLogLocalizer", WseePersistLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseePersistLogger.class.getClassLoader());
   }

   public static String logImproperBufferingQueueType(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      WseePersistLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("221006", var3, "weblogic.wsee.WseePersistLogLocalizer", WseePersistLogger.class.getClassLoader()));
      return "221006";
   }

   public static Loggable logImproperBufferingQueueTypeLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("221006", var3, "weblogic.wsee.WseePersistLogLocalizer", WseePersistLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseePersistLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = WseePersistLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = WseePersistLogger.findMessageLogger();
      }
   }
}
