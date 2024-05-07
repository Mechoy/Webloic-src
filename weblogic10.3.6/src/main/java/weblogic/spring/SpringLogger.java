package weblogic.spring;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class SpringLogger {
   private static final String LOCALIZER_CLASS = "weblogic.spring.SpringLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(SpringLogger.class.getName());
   }

   public static String logNotWebContext(String var0) {
      Object[] var1 = new Object[]{var0};
      SpringLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("2160000", var1, "weblogic.spring.SpringLogLocalizer", SpringLogger.class.getClassLoader()));
      return "2160000";
   }

   public static Loggable logNotWebContextLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("2160000", var1, "weblogic.spring.SpringLogLocalizer", SpringLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SpringLogger.class.getClassLoader());
   }

   public static String logRuntimeMBeanNotFound() {
      Object[] var0 = new Object[0];
      SpringLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("2160001", var0, "weblogic.spring.SpringLogLocalizer", SpringLogger.class.getClassLoader()));
      return "2160001";
   }

   public static Loggable logRuntimeMBeanNotFoundLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("2160001", var0, "weblogic.spring.SpringLogLocalizer", SpringLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SpringLogger.class.getClassLoader());
   }

   public static String getUnregisteredScopeName(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("2160002", var1, "weblogic.spring.SpringLogLocalizer", SpringLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SpringLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getUnregisteredScopeNameLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("2160002", var1, "weblogic.spring.SpringLogLocalizer", SpringLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SpringLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = SpringLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = SpringLogger.findMessageLogger();
      }
   }
}
