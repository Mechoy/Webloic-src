package weblogic.diagnostics.i18n;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class DiagnosticsHarvesterLogger {
   private static final String LOCALIZER_CLASS = "weblogic.diagnostics.l10n.DiagnosticsHarvesterLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(DiagnosticsHarvesterLogger.class.getName());
   }

   public static String logServerRuntimeMBeanServerNotAvailable() {
      Object[] var0 = new Object[0];
      DiagnosticsHarvesterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("320300", var0, "weblogic.diagnostics.l10n.DiagnosticsHarvesterLogLocalizer", DiagnosticsHarvesterLogger.class.getClassLoader()));
      return "320300";
   }

   public static Loggable logServerRuntimeMBeanServerNotAvailableLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("320300", var0, "weblogic.diagnostics.l10n.DiagnosticsHarvesterLogLocalizer", DiagnosticsHarvesterLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DiagnosticsHarvesterLogger.class.getClassLoader());
   }

   public static String logErrorActivatingWatchConfiguration(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      DiagnosticsHarvesterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("320301", var1, "weblogic.diagnostics.l10n.DiagnosticsHarvesterLogLocalizer", DiagnosticsHarvesterLogger.class.getClassLoader()));
      return "320301";
   }

   public static Loggable logErrorActivatingWatchConfigurationLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("320301", var1, "weblogic.diagnostics.l10n.DiagnosticsHarvesterLogLocalizer", DiagnosticsHarvesterLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DiagnosticsHarvesterLogger.class.getClassLoader());
   }

   public static String logUnservicableHarvestedTypeNamespaceError(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      DiagnosticsHarvesterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("320303", var2, "weblogic.diagnostics.l10n.DiagnosticsHarvesterLogLocalizer", DiagnosticsHarvesterLogger.class.getClassLoader()));
      return "320303";
   }

   public static Loggable logUnservicableHarvestedTypeNamespaceErrorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("320303", var2, "weblogic.diagnostics.l10n.DiagnosticsHarvesterLogLocalizer", DiagnosticsHarvesterLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DiagnosticsHarvesterLogger.class.getClassLoader());
   }

   public static String logValidationErrors(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      DiagnosticsHarvesterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("320304", var2, "weblogic.diagnostics.l10n.DiagnosticsHarvesterLogLocalizer", DiagnosticsHarvesterLogger.class.getClassLoader()));
      return "320304";
   }

   public static Loggable logValidationErrorsLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("320304", var2, "weblogic.diagnostics.l10n.DiagnosticsHarvesterLogLocalizer", DiagnosticsHarvesterLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DiagnosticsHarvesterLogger.class.getClassLoader());
   }

   public static String logInstanceNameInvalid(String var0) {
      Object[] var1 = new Object[]{var0};
      DiagnosticsHarvesterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("320305", var1, "weblogic.diagnostics.l10n.DiagnosticsHarvesterLogLocalizer", DiagnosticsHarvesterLogger.class.getClassLoader()));
      return "320305";
   }

   public static Loggable logInstanceNameInvalidLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("320305", var1, "weblogic.diagnostics.l10n.DiagnosticsHarvesterLogLocalizer", DiagnosticsHarvesterLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DiagnosticsHarvesterLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = DiagnosticsHarvesterLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = DiagnosticsHarvesterLogger.findMessageLogger();
      }
   }
}
