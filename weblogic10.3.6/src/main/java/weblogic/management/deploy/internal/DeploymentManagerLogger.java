package weblogic.management.deploy.internal;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class DeploymentManagerLogger {
   private static final String LOCALIZER_CLASS = "weblogic.i18ntools.DeploymentManagerLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(DeploymentManagerLogger.class.getName());
   }

   public static String logResumeFailure() {
      Object[] var0 = new Object[0];
      DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149601", var0, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.class.getClassLoader()));
      return "149601";
   }

   public static Loggable logResumeFailureLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("149601", var0, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentManagerLogger.class.getClassLoader());
   }

   public static String logShutdownFailure() {
      Object[] var0 = new Object[0];
      DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149603", var0, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.class.getClassLoader()));
      return "149603";
   }

   public static Loggable logShutdownFailureLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("149603", var0, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentManagerLogger.class.getClassLoader());
   }

   public static String logConversionToAppMBeanFailed(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149605", var2, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.class.getClassLoader()));
      return "149605";
   }

   public static Loggable logConversionToAppMBeanFailedLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149605", var2, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentManagerLogger.class.getClassLoader());
   }

   public static String logConfigureAppMBeanFailed(String var0) {
      Object[] var1 = new Object[]{var0};
      DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149606", var1, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.class.getClassLoader()));
      return "149606";
   }

   public static Loggable logConfigureAppMBeanFailedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149606", var1, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentManagerLogger.class.getClassLoader());
   }

   public static String logStatePersistenceFailed(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149607", var2, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.class.getClassLoader()));
      return "149607";
   }

   public static Loggable logStatePersistenceFailedLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149607", var2, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentManagerLogger.class.getClassLoader());
   }

   public static String logUnknownDeployable(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("149608", var1, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentManagerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable logUnknownDeployableLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149608", var1, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentManagerLogger.class.getClassLoader());
   }

   public static String logMBeanUnavailable() {
      Object[] var0 = new Object[0];
      return (new Loggable("149609", var0, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentManagerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable logMBeanUnavailableLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("149609", var0, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentManagerLogger.class.getClassLoader());
   }

   public static String storeCreateFailed(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("149610", var1, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentManagerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable storeCreateFailedLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149610", var1, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentManagerLogger.class.getClassLoader());
   }

   public static String cannotReadStore(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("149611", var1, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentManagerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable cannotReadStoreLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149611", var1, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentManagerLogger.class.getClassLoader());
   }

   public static String cannotSaveStore(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("149612", var2, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentManagerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable cannotSaveStoreLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149612", var2, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentManagerLogger.class.getClassLoader());
   }

   public static String cannotDeleteStore(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("149613", var2, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentManagerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable cannotDeleteStoreLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149613", var2, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentManagerLogger.class.getClassLoader());
   }

   public static String logDisconnectListenerError(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149614", var2, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.class.getClassLoader()));
      return "149614";
   }

   public static Loggable logDisconnectListenerErrorLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149614", var2, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentManagerLogger.class.getClassLoader());
   }

   public static String unrecognizedType(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("149615", var1, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentManagerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable unrecognizedTypeLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149615", var1, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentManagerLogger.class.getClassLoader());
   }

   public static String logCriticalInternalAppNotDeployed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("149616", var2, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentManagerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable logCriticalInternalAppNotDeployedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149616", var2, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentManagerLogger.class.getClassLoader());
   }

   public static String logNonCriticalInternalAppNotDeployed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149617", var2, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.class.getClassLoader()));
      return "149617";
   }

   public static Loggable logNonCriticalInternalAppNotDeployedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149617", var2, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentManagerLogger.class.getClassLoader());
   }

   public static String logDeployFailedForInternalApp(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149618", var2, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.class.getClassLoader()));
      return "149618";
   }

   public static Loggable logDeployFailedForInternalAppLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149618", var2, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentManagerLogger.class.getClassLoader());
   }

   public static String logDeleteFileFailed(String var0) {
      Object[] var1 = new Object[]{var0};
      DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149619", var1, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.class.getClassLoader()));
      return "149619";
   }

   public static Loggable logDeleteFileFailedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149619", var1, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentManagerLogger.class.getClassLoader());
   }

   public static String logFailureOnConfigRecovery(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149621", var1, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.class.getClassLoader()));
      return "149621";
   }

   public static Loggable logFailureOnConfigRecoveryLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149621", var1, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentManagerLogger.class.getClassLoader());
   }

   public static String logCouldNotGetFileLock() {
      Object[] var0 = new Object[0];
      DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149622", var0, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.class.getClassLoader()));
      return "149622";
   }

   public static Loggable logCouldNotGetFileLockLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("149622", var0, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentManagerLogger.class.getClassLoader());
   }

   public static String logInitFailed(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149623", var1, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.class.getClassLoader()));
      return "149623";
   }

   public static Loggable logInitFailedLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149623", var1, "weblogic.i18ntools.DeploymentManagerLogLocalizer", DeploymentManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentManagerLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = DeploymentManagerLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = DeploymentManagerLogger.findMessageLogger();
      }
   }
}
