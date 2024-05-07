package weblogic.management.deploy.internal;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class SlaveDeployerLogger {
   private static final String LOCALIZER_CLASS = "weblogic.i18ntools.SlaveDeployerLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(SlaveDeployerLogger.class.getName());
   }

   public static String logCommitUpdateFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149202", var2, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.class.getClassLoader()));
      return "149202";
   }

   public static Loggable logCommitUpdateFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149202", var2, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader());
   }

   public static String logIntialPrepareApplicationFailed(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149205", var2, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.class.getClassLoader()));
      return "149205";
   }

   public static Loggable logIntialPrepareApplicationFailedLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149205", var2, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader());
   }

   public static String logSlaveResumeStart() {
      Object[] var0 = new Object[0];
      SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149209", var0, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.class.getClassLoader()));
      return "149209";
   }

   public static Loggable logSlaveResumeStartLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("149209", var0, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader());
   }

   public static String logStartupFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149229", var2, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.class.getClassLoader()));
      return "149229";
   }

   public static Loggable logStartupFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149229", var2, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader());
   }

   public static String logSetActivationStateFailed(String var0, boolean var1, Exception var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149231", var3, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.class.getClassLoader()));
      return "149231";
   }

   public static Loggable logSetActivationStateFailedLoggable(String var0, boolean var1, Exception var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149231", var3, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader());
   }

   public static String logUnexpectedThrowable() {
      Object[] var0 = new Object[0];
      SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149233", var0, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.class.getClassLoader()));
      return "149233";
   }

   public static Loggable logUnexpectedThrowableLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("149233", var0, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader());
   }

   public static String logFailedDeployClusterAS(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149239", var2, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.class.getClassLoader()));
      return "149239";
   }

   public static Loggable logFailedDeployClusterASLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149239", var2, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader());
   }

   public static String logRetryingInternalAppDeployment(String var0) {
      Object[] var1 = new Object[]{var0};
      SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149243", var1, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.class.getClassLoader()));
      return "149243";
   }

   public static Loggable logRetryingInternalAppDeploymentLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149243", var1, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader());
   }

   public static String logUnknownPlan(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149245", var2, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.class.getClassLoader()));
      return "149245";
   }

   public static Loggable logUnknownPlanLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149245", var2, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader());
   }

   public static String logSecurityRealmDoesNotSupportAppVersioning(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149246", var2, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.class.getClassLoader()));
      return "149246";
   }

   public static Loggable logSecurityRealmDoesNotSupportAppVersioningLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149246", var2, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader());
   }

   public static String logTransitionAppFromAdminToRunningFailed(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149247", var2, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.class.getClassLoader()));
      return "149247";
   }

   public static Loggable logTransitionAppFromAdminToRunningFailedLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149247", var2, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader());
   }

   public static String logTransitionAppFromRunningToAdminFailed(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149248", var2, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.class.getClassLoader()));
      return "149248";
   }

   public static Loggable logTransitionAppFromRunningToAdminFailedLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149248", var2, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader());
   }

   public static String logUnprepareFailed(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149250", var2, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.class.getClassLoader()));
      return "149250";
   }

   public static Loggable logUnprepareFailedLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149250", var2, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader());
   }

   public static String logOperationFailed(String var0, String var1, Exception var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149251", var3, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.class.getClassLoader()));
      return "149251";
   }

   public static Loggable logOperationFailedLoggable(String var0, String var1, Exception var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149251", var3, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader());
   }

   public static String logNoDeployment(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149252", var2, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.class.getClassLoader()));
      return "149252";
   }

   public static Loggable logNoDeploymentLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149252", var2, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader());
   }

   public static String logFailedToFindDeployment(String var0) {
      Object[] var1 = new Object[]{var0};
      SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149256", var1, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.class.getClassLoader()));
      return "149256";
   }

   public static Loggable logFailedToFindDeploymentLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149256", var1, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader());
   }

   public static String logInvalidDistribute(String var0) {
      Object[] var1 = new Object[]{var0};
      SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149257", var1, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.class.getClassLoader()));
      return "149257";
   }

   public static Loggable logInvalidDistributeLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149257", var1, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader());
   }

   public static String logRemoveStagedFilesFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149258", var2, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.class.getClassLoader()));
      return "149258";
   }

   public static Loggable logRemoveStagedFilesFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149258", var2, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader());
   }

   public static String logStartupFailedTransitionToAdmin(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149259", var2, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.class.getClassLoader()));
      return "149259";
   }

   public static Loggable logStartupFailedTransitionToAdminLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149259", var2, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader());
   }

   public static String logAppStartupFailed(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149260", var1, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.class.getClassLoader()));
      return "149260";
   }

   public static Loggable logAppStartupFailedLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149260", var1, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader());
   }

   public static String logInvalidStateForRedeploy(String var0) {
      Object[] var1 = new Object[]{var0};
      SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149264", var1, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.class.getClassLoader()));
      return "149264";
   }

   public static Loggable logInvalidStateForRedeployLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149264", var1, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader());
   }

   public static String logTaskFailed(String var0, String var1, Exception var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149265", var3, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.class.getClassLoader()));
      return "149265";
   }

   public static Loggable logTaskFailedLoggable(String var0, String var1, Exception var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149265", var3, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader());
   }

   public static String illegalStateForDeploy(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("149266", var1, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable illegalStateForDeployLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149266", var1, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader());
   }

   public static String logCancelFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("149267", var2, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable logCancelFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149267", var2, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader());
   }

   public static String logStaticDeploymentOfNonVersionAppCheck(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("149268", var1, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable logStaticDeploymentOfNonVersionAppCheckLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149268", var1, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader());
   }

   public static String logBothStaticFileRedeployAndModuleRedeploy() {
      Object[] var0 = new Object[0];
      return (new Loggable("149269", var0, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable logBothStaticFileRedeployAndModuleRedeployLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("149269", var0, "weblogic.i18ntools.SlaveDeployerLogLocalizer", SlaveDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SlaveDeployerLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = SlaveDeployerLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = SlaveDeployerLogger.findMessageLogger();
      }
   }
}
