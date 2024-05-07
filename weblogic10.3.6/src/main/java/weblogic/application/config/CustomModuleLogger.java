package weblogic.application.config;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;

public class CustomModuleLogger {
   private static final String LOCALIZER_CLASS = "weblogic.application.config.CustomModuleLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(CustomModuleLogger.class.getName());
   }

   public static String logNoConfigSupport(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      CustomModuleLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("340450", var2, "weblogic.application.config.CustomModuleLogLocalizer", CustomModuleLogger.class.getClassLoader()));
      return "340450";
   }

   public static String logConfigSupportUriMismatch(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      CustomModuleLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("340451", var3, "weblogic.application.config.CustomModuleLogLocalizer", CustomModuleLogger.class.getClassLoader()));
      return "340451";
   }

   public static String logPrepareDeploy(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      CustomModuleLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("340452", var2, "weblogic.application.config.CustomModuleLogLocalizer", CustomModuleLogger.class.getClassLoader()));
      return "340452";
   }

   public static String logPrepareUpdate(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      CustomModuleLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("340454", var2, "weblogic.application.config.CustomModuleLogLocalizer", CustomModuleLogger.class.getClassLoader()));
      return "340454";
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = CustomModuleLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = CustomModuleLogger.findMessageLogger();
      }
   }
}
