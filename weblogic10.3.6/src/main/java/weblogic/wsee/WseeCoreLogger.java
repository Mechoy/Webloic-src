package weblogic.wsee;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class WseeCoreLogger {
   private static final String LOCALIZER_CLASS = "weblogic.wsee.WseeCoreLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(WseeCoreLogger.class.getName());
   }

   public static String logUnexpectedException(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeCoreLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("220500", var2, "weblogic.wsee.WseeCoreLogLocalizer", WseeCoreLogger.class.getClassLoader()));
      return "220500";
   }

   public static Loggable logUnexpectedExceptionLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("220500", var2, "weblogic.wsee.WseeCoreLogLocalizer", WseeCoreLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeCoreLogger.class.getClassLoader());
   }

   public static String logServicePortNotAvailableInWSDL(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      WseeCoreLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("220501", var3, "weblogic.wsee.WseeCoreLogLocalizer", WseeCoreLogger.class.getClassLoader()));
      return "220501";
   }

   public static Loggable logServicePortNotAvailableInWSDLLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("220501", var3, "weblogic.wsee.WseeCoreLogLocalizer", WseeCoreLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeCoreLogger.class.getClassLoader());
   }

   public static String logWseeServiceStarting() {
      Object[] var0 = new Object[0];
      WseeCoreLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("220502", var0, "weblogic.wsee.WseeCoreLogLocalizer", WseeCoreLogger.class.getClassLoader()));
      return "220502";
   }

   public static Loggable logWseeServiceStartingLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("220502", var0, "weblogic.wsee.WseeCoreLogLocalizer", WseeCoreLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeCoreLogger.class.getClassLoader());
   }

   public static String logWseeServiceStopping() {
      Object[] var0 = new Object[0];
      WseeCoreLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("220503", var0, "weblogic.wsee.WseeCoreLogLocalizer", WseeCoreLogger.class.getClassLoader()));
      return "220503";
   }

   public static Loggable logWseeServiceStoppingLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("220503", var0, "weblogic.wsee.WseeCoreLogLocalizer", WseeCoreLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeCoreLogger.class.getClassLoader());
   }

   public static String logWseeServiceHalting() {
      Object[] var0 = new Object[0];
      WseeCoreLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("220504", var0, "weblogic.wsee.WseeCoreLogLocalizer", WseeCoreLogger.class.getClassLoader()));
      return "220504";
   }

   public static Loggable logWseeServiceHaltingLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("220504", var0, "weblogic.wsee.WseeCoreLogLocalizer", WseeCoreLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeCoreLogger.class.getClassLoader());
   }

   public static String logImplicitClientIdReusedImproperly(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeCoreLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("220505", var2, "weblogic.wsee.WseeCoreLogLocalizer", WseeCoreLogger.class.getClassLoader()));
      return "220505";
   }

   public static Loggable logImplicitClientIdReusedImproperlyLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("220505", var2, "weblogic.wsee.WseeCoreLogLocalizer", WseeCoreLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeCoreLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = WseeCoreLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = WseeCoreLogger.findMessageLogger();
      }
   }
}
