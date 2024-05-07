package weblogic.wsee;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class WseeSenderLogger {
   private static final String LOCALIZER_CLASS = "weblogic.wsee.WseeSenderLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(WseeSenderLogger.class.getName());
   }

   public static String logUnexpectedException(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeSenderLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("221100", var2, "weblogic.wsee.WseeSenderLogLocalizer", WseeSenderLogger.class.getClassLoader()));
      return "221100";
   }

   public static Loggable logUnexpectedExceptionLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("221100", var2, "weblogic.wsee.WseeSenderLogLocalizer", WseeSenderLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeSenderLogger.class.getClassLoader());
   }

   public static String logConversationExists(String var0) {
      Object[] var1 = new Object[]{var0};
      WseeSenderLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("221101", var1, "weblogic.wsee.WseeSenderLogLocalizer", WseeSenderLogger.class.getClassLoader()));
      return "221101";
   }

   public static Loggable logConversationExistsLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("221101", var1, "weblogic.wsee.WseeSenderLogLocalizer", WseeSenderLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeSenderLogger.class.getClassLoader());
   }

   public static String logConversationNotFound(String var0) {
      Object[] var1 = new Object[]{var0};
      WseeSenderLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("221102", var1, "weblogic.wsee.WseeSenderLogLocalizer", WseeSenderLogger.class.getClassLoader()));
      return "221102";
   }

   public static Loggable logConversationNotFoundLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("221102", var1, "weblogic.wsee.WseeSenderLogLocalizer", WseeSenderLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeSenderLogger.class.getClassLoader());
   }

   public static String logSendRequestNotFound(String var0) {
      Object[] var1 = new Object[]{var0};
      WseeSenderLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("221103", var1, "weblogic.wsee.WseeSenderLogLocalizer", WseeSenderLogger.class.getClassLoader()));
      return "221103";
   }

   public static Loggable logSendRequestNotFoundLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("221103", var1, "weblogic.wsee.WseeSenderLogLocalizer", WseeSenderLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeSenderLogger.class.getClassLoader());
   }

   public static String logSendRequestWithSeqNumNotFound(String var0, long var1) {
      Object[] var3 = new Object[]{var0, new Long(var1)};
      WseeSenderLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("221104", var3, "weblogic.wsee.WseeSenderLogLocalizer", WseeSenderLogger.class.getClassLoader()));
      return "221104";
   }

   public static Loggable logSendRequestWithSeqNumNotFoundLoggable(String var0, long var1) {
      Object[] var3 = new Object[]{var0, new Long(var1)};
      return new Loggable("221104", var3, "weblogic.wsee.WseeSenderLogLocalizer", WseeSenderLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeSenderLogger.class.getClassLoader());
   }

   public static String logConversationCancelled(String var0) {
      Object[] var1 = new Object[]{var0};
      WseeSenderLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("221105", var1, "weblogic.wsee.WseeSenderLogLocalizer", WseeSenderLogger.class.getClassLoader()));
      return "221105";
   }

   public static Loggable logConversationCancelledLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("221105", var1, "weblogic.wsee.WseeSenderLogLocalizer", WseeSenderLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeSenderLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = WseeSenderLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = WseeSenderLogger.findMessageLogger();
      }
   }
}
