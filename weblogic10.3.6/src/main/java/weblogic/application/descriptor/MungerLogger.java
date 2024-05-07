package weblogic.application.descriptor;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class MungerLogger {
   private static final String LOCALIZER_CLASS = "weblogic.application.descriptor.MungerLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(MungerLogger.class.getName());
   }

   public static String logUnableToValidateDescriptor(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      MungerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("2156200", var3, "weblogic.application.descriptor.MungerLogLocalizer", MungerLogger.class.getClassLoader()));
      return "2156200";
   }

   public static Loggable logUnableToValidateDescriptorLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("2156200", var3, "weblogic.application.descriptor.MungerLogLocalizer", MungerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MungerLogger.class.getClassLoader());
   }

   public static String logDescriptorParseError(String var0) {
      Object[] var1 = new Object[]{var0};
      MungerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("2156201", var1, "weblogic.application.descriptor.MungerLogLocalizer", MungerLogger.class.getClassLoader()));
      return "2156201";
   }

   public static Loggable logDescriptorParseErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("2156201", var1, "weblogic.application.descriptor.MungerLogLocalizer", MungerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MungerLogger.class.getClassLoader());
   }

   public static String logValidPlanMerged(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      MungerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("2156202", var2, "weblogic.application.descriptor.MungerLogLocalizer", MungerLogger.class.getClassLoader()));
      return "2156202";
   }

   public static Loggable logValidPlanMergedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("2156202", var2, "weblogic.application.descriptor.MungerLogLocalizer", MungerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MungerLogger.class.getClassLoader());
   }

   public static String logMissingVersionAttribute(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      MungerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("2156203", var2, "weblogic.application.descriptor.MungerLogLocalizer", MungerLogger.class.getClassLoader()));
      return "2156203";
   }

   public static Loggable logMissingVersionAttributeLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("2156203", var2, "weblogic.application.descriptor.MungerLogLocalizer", MungerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MungerLogger.class.getClassLoader());
   }

   public static String logMissingRootElement(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      MungerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("2156204", var2, "weblogic.application.descriptor.MungerLogLocalizer", MungerLogger.class.getClassLoader()));
      return "2156204";
   }

   public static Loggable logMissingRootElementLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("2156204", var2, "weblogic.application.descriptor.MungerLogLocalizer", MungerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MungerLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = MungerLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = MungerLogger.findMessageLogger();
      }
   }
}
