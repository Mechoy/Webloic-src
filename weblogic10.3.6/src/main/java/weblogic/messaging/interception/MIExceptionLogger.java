package weblogic.messaging.interception;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class MIExceptionLogger {
   private static final String LOCALIZER_CLASS = "weblogic.messaging.interception.MIExceptionLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(MIExceptionLogger.class.getName());
   }

   public static String logSetupJNDIException(String var0) {
      Object[] var1 = new Object[]{var0};
      MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("420000", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.class.getClassLoader()));
      return "420000";
   }

   public static Loggable logSetupJNDIExceptionLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("420000", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MIExceptionLogger.class.getClassLoader());
   }

   public static String logAddAssociationInputError(String var0) {
      Object[] var1 = new Object[]{var0};
      MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("420002", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.class.getClassLoader()));
      return "420002";
   }

   public static Loggable logAddAssociationInputErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("420002", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MIExceptionLogger.class.getClassLoader());
   }

   public static String logAddAssociationUnknownInterceptionPointTypeError(String var0) {
      Object[] var1 = new Object[]{var0};
      MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("420003", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.class.getClassLoader()));
      return "420003";
   }

   public static Loggable logAddAssociationUnknownInterceptionPointTypeErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("420003", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MIExceptionLogger.class.getClassLoader());
   }

   public static String logRemoveAssociationInputError(String var0) {
      Object[] var1 = new Object[]{var0};
      MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("420004", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.class.getClassLoader()));
      return "420004";
   }

   public static Loggable logRemoveAssociationInputErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("420004", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MIExceptionLogger.class.getClassLoader());
   }

   public static String logRegisterInterceptionPointNameDescriptionInputError(String var0) {
      Object[] var1 = new Object[]{var0};
      MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("420005", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.class.getClassLoader()));
      return "420005";
   }

   public static Loggable logRegisterInterceptionPointNameDescriptionInputErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("420005", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MIExceptionLogger.class.getClassLoader());
   }

   public static String logRegisterInterceptionPointInputError(String var0) {
      Object[] var1 = new Object[]{var0};
      MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("420006", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.class.getClassLoader()));
      return "420006";
   }

   public static Loggable logRegisterInterceptionPointInputErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("420006", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MIExceptionLogger.class.getClassLoader());
   }

   public static String logRegisterInterceptionPointUnknownInterceptionPointTypeError(String var0) {
      Object[] var1 = new Object[]{var0};
      MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("420007", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.class.getClassLoader()));
      return "420007";
   }

   public static Loggable logRegisterInterceptionPointUnknownInterceptionPointTypeErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("420007", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MIExceptionLogger.class.getClassLoader());
   }

   public static String logUnRegisterInterceptionPointInputError(String var0) {
      Object[] var1 = new Object[]{var0};
      MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("420008", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.class.getClassLoader()));
      return "420008";
   }

   public static Loggable logUnRegisterInterceptionPointInputErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("420008", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MIExceptionLogger.class.getClassLoader());
   }

   public static String logRegisterProcessorTypeInputError(String var0) {
      Object[] var1 = new Object[]{var0};
      MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("420009", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.class.getClassLoader()));
      return "420009";
   }

   public static Loggable logRegisterProcessorTypeInputErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("420009", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MIExceptionLogger.class.getClassLoader());
   }

   public static String logAddProcessorInputError(String var0) {
      Object[] var1 = new Object[]{var0};
      MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("420010", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.class.getClassLoader()));
      return "420010";
   }

   public static Loggable logAddProcessorInputErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("420010", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MIExceptionLogger.class.getClassLoader());
   }

   public static String logAddProcessorUnknownProcessorTypeError(String var0) {
      Object[] var1 = new Object[]{var0};
      MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("420011", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.class.getClassLoader()));
      return "420011";
   }

   public static Loggable logAddProcessorUnknownProcessorTypeErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("420011", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MIExceptionLogger.class.getClassLoader());
   }

   public static String logRemoveProcessorInputError(String var0) {
      Object[] var1 = new Object[]{var0};
      MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("420012", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.class.getClassLoader()));
      return "420012";
   }

   public static Loggable logRemoveProcessorInputErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("420012", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MIExceptionLogger.class.getClassLoader());
   }

   public static String logGetAssociationHandleInputError(String var0) {
      Object[] var1 = new Object[]{var0};
      MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("420013", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.class.getClassLoader()));
      return "420013";
   }

   public static Loggable logGetAssociationHandleInputErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("420013", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MIExceptionLogger.class.getClassLoader());
   }

   public static String logGetProcessorHandlesInputError(String var0) {
      Object[] var1 = new Object[]{var0};
      MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("420014", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.class.getClassLoader()));
      return "420014";
   }

   public static Loggable logGetProcessorHandlesInputErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("420014", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MIExceptionLogger.class.getClassLoader());
   }

   public static String logGetProcessorHandleInputError(String var0) {
      Object[] var1 = new Object[]{var0};
      MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("420015", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.class.getClassLoader()));
      return "420015";
   }

   public static Loggable logGetProcessorHandleInputErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("420015", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MIExceptionLogger.class.getClassLoader());
   }

   public static String logRegisterInterceptionPointNameDescriptionListenerInputError(String var0) {
      Object[] var1 = new Object[]{var0};
      MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("420016", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.class.getClassLoader()));
      return "420016";
   }

   public static Loggable logRegisterInterceptionPointNameDescriptionListenerInputErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("420016", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MIExceptionLogger.class.getClassLoader());
   }

   public static String logRemoveAssociationAlreadyRemoveError(String var0) {
      Object[] var1 = new Object[]{var0};
      MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("420017", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.class.getClassLoader()));
      return "420017";
   }

   public static Loggable logRemoveAssociationAlreadyRemoveErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("420017", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MIExceptionLogger.class.getClassLoader());
   }

   public static String logProcessProcessorNotFoundError(String var0) {
      Object[] var1 = new Object[]{var0};
      MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("420019", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.class.getClassLoader()));
      return "420019";
   }

   public static Loggable logProcessProcessorNotFoundErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("420019", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MIExceptionLogger.class.getClassLoader());
   }

   public static String logProcessIllegalError(String var0) {
      Object[] var1 = new Object[]{var0};
      MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("420020", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.class.getClassLoader()));
      return "420020";
   }

   public static Loggable logProcessIllegalErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("420020", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MIExceptionLogger.class.getClassLoader());
   }

   public static String logProcessIllegalException(String var0) {
      Object[] var1 = new Object[]{var0};
      MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("420021", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.class.getClassLoader()));
      return "420021";
   }

   public static Loggable logProcessIllegalExceptionLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("420021", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MIExceptionLogger.class.getClassLoader());
   }

   public static String logAddAssociationAlreadyExistError(String var0) {
      Object[] var1 = new Object[]{var0};
      MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("420025", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.class.getClassLoader()));
      return "420025";
   }

   public static Loggable logAddAssociationAlreadyExistErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("420025", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MIExceptionLogger.class.getClassLoader());
   }

   public static String logUnregisterInterceptionPointAlreayRemoveError(String var0) {
      Object[] var1 = new Object[]{var0};
      MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("420026", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.class.getClassLoader()));
      return "420026";
   }

   public static Loggable logUnregisterInterceptionPointAlreayRemoveErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("420026", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MIExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidInterceptionPointName(String var0) {
      Object[] var1 = new Object[]{var0};
      MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("420027", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.class.getClassLoader()));
      return "420027";
   }

   public static Loggable logInvalidInterceptionPointNameLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("420027", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MIExceptionLogger.class.getClassLoader());
   }

   public static String logRemoveProcessorAlreadyRemoveError(String var0) {
      Object[] var1 = new Object[]{var0};
      MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("420028", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.class.getClassLoader()));
      return "420028";
   }

   public static Loggable logRemoveProcessorAlreadyRemoveErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("420028", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MIExceptionLogger.class.getClassLoader());
   }

   public static String logProcessorFactoryCreateError(String var0) {
      Object[] var1 = new Object[]{var0};
      MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("420029", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.class.getClassLoader()));
      return "420029";
   }

   public static Loggable logProcessorFactoryCreateErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("420029", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MIExceptionLogger.class.getClassLoader());
   }

   public static String logProcessorFactoryCreateUnknownError(String var0) {
      Object[] var1 = new Object[]{var0};
      MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("420030", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.class.getClassLoader()));
      return "420030";
   }

   public static Loggable logProcessorFactoryCreateUnknownErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("420030", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MIExceptionLogger.class.getClassLoader());
   }

   public static String logProcessProcessorDepthExceededError(String var0) {
      Object[] var1 = new Object[]{var0};
      MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("420031", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.class.getClassLoader()));
      return "420031";
   }

   public static Loggable logProcessProcessorDepthExceededErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("420031", var1, "weblogic.messaging.interception.MIExceptionLogLocalizer", MIExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, MIExceptionLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = MIExceptionLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = MIExceptionLogger.findMessageLogger();
      }
   }
}
