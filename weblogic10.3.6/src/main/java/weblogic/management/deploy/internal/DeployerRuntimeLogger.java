package weblogic.management.deploy.internal;

import java.util.ArrayList;
import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class DeployerRuntimeLogger {
   private static final String LOCALIZER_CLASS = "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(DeployerRuntimeLogger.class.getName());
   }

   public static String logInitFailed(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149000", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149000";
   }

   public static Loggable logInitFailedLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149000", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logNullApp(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149001", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149001";
   }

   public static Loggable logNullAppLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149001", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logNoSource(String var0) {
      Object[] var1 = new Object[]{var0};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149002", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149002";
   }

   public static Loggable logNoSourceLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149002", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logInvalidSource(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149003", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149003";
   }

   public static Loggable logInvalidSourceLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149003", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logTaskFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149004", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149004";
   }

   public static Loggable logTaskFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149004", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logInvalidNewSource(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149007", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149007";
   }

   public static Loggable logInvalidNewSourceLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149007", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logUnconfigTargets(ArrayList var0) {
      Object[] var1 = new Object[]{var0};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149011", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149011";
   }

   public static Loggable logUnconfigTargetsLoggable(ArrayList var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149011", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logNoSuchModule(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149013", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149013";
   }

   public static Loggable logNoSuchModuleLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149013", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logNoSuchTarget(String var0) {
      Object[] var1 = new Object[]{var0};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149014", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149014";
   }

   public static Loggable logNoSuchTargetLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149014", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logAddTarget(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149015", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149015";
   }

   public static Loggable logAddTargetLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149015", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logTaskInUse(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149021", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149021";
   }

   public static Loggable logTaskInUseLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149021", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logInvalidTargetForComponent(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149025", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149025";
   }

   public static Loggable logInvalidTargetForComponentLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149025", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logDescription(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149026", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149026";
   }

   public static Loggable logDescriptionLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149026", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logInvalidApp(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149027", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149027";
   }

   public static Loggable logInvalidAppLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149027", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logUnknownAppType(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149028", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149028";
   }

   public static Loggable logUnknownAppTypeLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149028", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logInvalidStagingMode(String var0) {
      Object[] var1 = new Object[]{var0};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149031", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149031";
   }

   public static Loggable logInvalidStagingModeLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149031", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logAlreadyStarted() {
      Object[] var0 = new Object[0];
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149032", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149032";
   }

   public static Loggable logAlreadyStartedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("149032", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logAppNotification(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149033", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149033";
   }

   public static Loggable logAppNotificationLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149033", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logExceptionReceived(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149034", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149034";
   }

   public static Loggable logExceptionReceivedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149034", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logModuleMessage(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149035", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149035";
   }

   public static Loggable logModuleMessageLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149035", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logStartedDeployment(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149038", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149038";
   }

   public static Loggable logStartedDeploymentLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149038", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logRejectStagingChange(String var0) {
      Object[] var1 = new Object[]{var0};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149040", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149040";
   }

   public static Loggable logRejectStagingChangeLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149040", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logMisMatchStagingMode(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149043", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149043";
   }

   public static Loggable logMisMatchStagingModeLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149043", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logClusterMemberAlreadyTargeted(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149045", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149045";
   }

   public static Loggable logClusterMemberAlreadyTargetedLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149045", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logServerAlreadyTargetedByCluster(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149048", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149048";
   }

   public static Loggable logServerAlreadyTargetedByClusterLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149048", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logHostTargetForNonWebApp(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149054", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149054";
   }

   public static Loggable logHostTargetForNonWebAppLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149054", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logServerAlreadyTargetedByOtherClusterMember(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149055", var4, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149055";
   }

   public static Loggable logServerAlreadyTargetedByOtherClusterMemberLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("149055", var4, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logErrorStagingMode(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149058", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149058";
   }

   public static Loggable logErrorStagingModeLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149058", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logStartTransition(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149059", var5, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149059";
   }

   public static Loggable logStartTransitionLoggable(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return new Loggable("149059", var5, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logSuccessfulTransition(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149060", var5, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149060";
   }

   public static Loggable logSuccessfulTransitionLoggable(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return new Loggable("149060", var5, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logFailedTransition(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149061", var5, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149061";
   }

   public static Loggable logFailedTransitionLoggable(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return new Loggable("149061", var5, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logErrorCannotCancelCompletedTask(String var0) {
      Object[] var1 = new Object[]{var0};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149062", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149062";
   }

   public static Loggable logErrorCannotCancelCompletedTaskLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149062", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logNoURI(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149066", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149066";
   }

   public static Loggable logNoURILoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149066", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logInternalError(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149068", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149068";
   }

   public static Loggable logInternalErrorLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149068", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String getOldActivate(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("149069", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getOldActivateLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149069", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logNoModules(String var0) {
      Object[] var1 = new Object[]{var0};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149073", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149073";
   }

   public static Loggable logNoModulesLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149073", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logTaskSuccess(String var0) {
      Object[] var1 = new Object[]{var0};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149074", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149074";
   }

   public static Loggable logTaskSuccessLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149074", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logTrace(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149078", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149078";
   }

   public static Loggable logTraceLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149078", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logPartialRedeployOfArchive(String var0) {
      Object[] var1 = new Object[]{var0};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149080", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149080";
   }

   public static Loggable logPartialRedeployOfArchiveLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149080", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logInvalidAppVersion(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149081", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149081";
   }

   public static Loggable logInvalidAppVersionLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149081", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logInvalidAppVersion2(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149082", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149082";
   }

   public static Loggable logInvalidAppVersion2Loggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149082", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logActiveAppVersionWarning(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149085", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149085";
   }

   public static Loggable logActiveAppVersionWarningLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149085", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logRetireGracefully(String var0) {
      Object[] var1 = new Object[]{var0};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149086", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149086";
   }

   public static Loggable logRetireGracefullyLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149086", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logRetireTimeout(String var0, int var1) {
      Object[] var2 = new Object[]{var0, new Integer(var1)};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149087", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149087";
   }

   public static Loggable logRetireTimeoutLoggable(String var0, int var1) {
      Object[] var2 = new Object[]{var0, new Integer(var1)};
      return new Loggable("149087", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logRetirementCancelled(String var0, long var1) {
      Object[] var3 = new Object[]{var0, new Long(var1)};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149088", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149088";
   }

   public static Loggable logRetirementCancelledLoggable(String var0, long var1) {
      Object[] var3 = new Object[]{var0, new Long(var1)};
      return new Loggable("149088", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logActivateWhileRetireInProgress(String var0) {
      Object[] var1 = new Object[]{var0};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149089", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149089";
   }

   public static Loggable logActivateWhileRetireInProgressLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149089", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logAppVersionMismatch(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149091", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149091";
   }

   public static Loggable logAppVersionMismatchLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149091", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logAppVersionMismatch2(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149092", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149092";
   }

   public static Loggable logAppVersionMismatch2Loggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149092", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logNoActiveApp(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149093", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149093";
   }

   public static Loggable logNoActiveAppLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149093", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logInvalidNewSource2(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149094", var4, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149094";
   }

   public static Loggable logInvalidNewSource2Loggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("149094", var4, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logRetireNow(String var0) {
      Object[] var1 = new Object[]{var0};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149095", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149095";
   }

   public static Loggable logRetireNowLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149095", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logVersionNotAllowedForDeprecatedOp(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149096", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149096";
   }

   public static Loggable logVersionNotAllowedForDeprecatedOpLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149096", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logRetirementFailed(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149097", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149097";
   }

   public static Loggable logRetirementFailedLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149097", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logGracefulUndeployWhileRetireInProgress(String var0) {
      Object[] var1 = new Object[]{var0};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149098", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149098";
   }

   public static Loggable logGracefulUndeployWhileRetireInProgressLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149098", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logSendDeploymentEventError(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149099", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149099";
   }

   public static Loggable logSendDeploymentEventErrorLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149099", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logSendVetoableDeployEventError(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149100", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149100";
   }

   public static Loggable logSendVetoableDeployEventErrorLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149100", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logInvalidTargetForPinnedAppUndeploy(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149101", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149101";
   }

   public static Loggable logInvalidTargetForPinnedAppUndeployLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149101", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logModuleTypeNotSupported(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149102", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149102";
   }

   public static Loggable logModuleTypeNotSupportedLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149102", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logExceptionOccurred(String var0, String var1, Exception var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149103", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149103";
   }

   public static Loggable logExceptionOccurredLoggable(String var0, String var1, Exception var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149103", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logNullInfo(String var0) {
      Object[] var1 = new Object[]{var0};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149104", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149104";
   }

   public static Loggable logNullInfoLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149104", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logLibNameMismatch(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149105", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149105";
   }

   public static Loggable logLibNameMismatchLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149105", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logNoLibName(String var0) {
      Object[] var1 = new Object[]{var0};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149106", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149106";
   }

   public static Loggable logNoLibNameLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149106", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logLibVersionMismatch(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149107", var5, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149107";
   }

   public static Loggable logLibVersionMismatchLoggable(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return new Loggable("149107", var5, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logTaskFailedNoApp(String var0) {
      Object[] var1 = new Object[]{var0};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149108", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149108";
   }

   public static Loggable logTaskFailedNoAppLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149108", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logErrorPersistingActiveAppState(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149110", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149110";
   }

   public static Loggable logErrorPersistingActiveAppStateLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149110", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logRedeployWithSrcNotAllowedForNonVersion(String var0) {
      Object[] var1 = new Object[]{var0};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149111", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149111";
   }

   public static Loggable logRedeployWithSrcNotAllowedForNonVersionLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149111", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logRedeployWithSrcNotAllowedForSameVersion(String var0) {
      Object[] var1 = new Object[]{var0};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149112", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149112";
   }

   public static Loggable logRedeployWithSrcNotAllowedForSameVersionLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149112", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logMaxAppVersionsExceeded(String var0, String var1, int var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, new Integer(var2), var3};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149113", var4, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149113";
   }

   public static Loggable logMaxAppVersionsExceededLoggable(String var0, String var1, int var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, new Integer(var2), var3};
      return new Loggable("149113", var4, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logVersionIdLengthExceeded(String var0, String var1, int var2) {
      Object[] var3 = new Object[]{var0, var1, new Integer(var2)};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149114", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149114";
   }

   public static Loggable logVersionIdLengthExceededLoggable(String var0, String var1, int var2) {
      Object[] var3 = new Object[]{var0, var1, new Integer(var2)};
      return new Loggable("149114", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logInvalidVersionId(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149115", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149115";
   }

   public static Loggable logInvalidVersionIdLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149115", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logFailedToObtainConfig() {
      Object[] var0 = new Object[0];
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149116", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149116";
   }

   public static Loggable logFailedToObtainConfigLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("149116", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logLibraryDescription(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149117", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149117";
   }

   public static Loggable logLibraryDescriptionLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149117", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logAppNotTargeted(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149118", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149118";
   }

   public static Loggable logAppNotTargetedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149118", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logInvalidTargetsForAppVersion(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149119", var4, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149119";
   }

   public static Loggable logInvalidTargetsForAppVersionLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("149119", var4, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logPlanVersionNotAllowed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149120", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149120";
   }

   public static Loggable logPlanVersionNotAllowedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149120", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logDeploymentServiceNotStarted(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149121", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149121";
   }

   public static Loggable logDeploymentServiceNotStartedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149121", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logNoOpOnInternalApp(String var0) {
      Object[] var1 = new Object[]{var0};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149122", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149122";
   }

   public static Loggable logNoOpOnInternalAppLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149122", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logDiffSecurityModelIgnoredForRedeploy(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149123", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149123";
   }

   public static Loggable logDiffSecurityModelIgnoredForRedeployLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149123", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logTaskFailedWithError(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149124", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149124";
   }

   public static Loggable logTaskFailedWithErrorLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149124", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logRemoveRetiredAppVersion(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149125", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149125";
   }

   public static Loggable logRemoveRetiredAppVersionLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149125", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logRetiringAppVersionNotRemoved(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149126", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149126";
   }

   public static Loggable logRetiringAppVersionNotRemovedLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149126", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logTaskFailedNoAppWithError(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149127", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149127";
   }

   public static Loggable logTaskFailedNoAppWithErrorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149127", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logRemoveAllRetiredAppVersion(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149128", var4, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149128";
   }

   public static Loggable logRemoveAllRetiredAppVersionLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("149128", var4, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logRemoveAllRetiringAppVersion(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149129", var4, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149129";
   }

   public static Loggable logRemoveAllRetiringAppVersionLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("149129", var4, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logRemoveAllActiveAppVersion(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149130", var4, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149130";
   }

   public static Loggable logRemoveAllActiveAppVersionLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("149130", var4, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logNonActiveApp(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149131", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149131";
   }

   public static Loggable logNonActiveAppLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149131", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logNoReTargetOnSplitDirApp(String var0) {
      Object[] var1 = new Object[]{var0};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149132", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149132";
   }

   public static Loggable logNoReTargetOnSplitDirAppLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149132", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logNoReTargetOnAutoDeployedApp(String var0) {
      Object[] var1 = new Object[]{var0};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149133", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149133";
   }

   public static Loggable logNoReTargetOnAutoDeployedAppLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149133", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logPartialRedeployOfVersionedArchive(String var0) {
      Object[] var1 = new Object[]{var0};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149134", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149134";
   }

   public static Loggable logPartialRedeployOfVersionedArchiveLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149134", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logInvalidTargetForOperation(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149135", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149135";
   }

   public static Loggable logInvalidTargetForOperationLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149135", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String invalidDelta(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("149137", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable invalidDeltaLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149137", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logInvalidTargetSubset(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149138", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149138";
   }

   public static Loggable logInvalidTargetSubsetLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149138", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logInvalidIndividualTarget(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149139", var4, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149139";
   }

   public static Loggable logInvalidIndividualTargetLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("149139", var4, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String pendingActivation() {
      Object[] var0 = new Object[0];
      return (new Loggable("149140", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable pendingActivationLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("149140", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String appAlreadyExists(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("149141", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable appAlreadyExistsLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149141", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String emptyCluster(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("149142", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable emptyClusterLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149142", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logAppSubModuleTargetErr() {
      Object[] var0 = new Object[0];
      return (new Loggable("149143", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable logAppSubModuleTargetErrLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("149143", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String serverUnreachable(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("149145", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable serverUnreachableLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149145", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String altURLFailed(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("149146", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable altURLFailedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149146", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String adminUnreachable(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("149147", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable adminUnreachableLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149147", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String errorReceivingMessage() {
      Object[] var0 = new Object[0];
      return (new Loggable("149148", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable errorReceivingMessageLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("149148", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String malformedURL(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("149149", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable malformedURLLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149149", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String errorReadingInput() {
      Object[] var0 = new Object[0];
      return (new Loggable("149150", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable errorReadingInputLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("149150", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String receiverNotFound(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("149151", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable receiverNotFoundLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149151", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String noDeploymentRequest() {
      Object[] var0 = new Object[0];
      return (new Loggable("149152", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable noDeploymentRequestLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("149152", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String invalidPrepare(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return (new Loggable("149153", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable invalidPrepareLoggable(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return new Loggable("149153", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String invalidHandleResponse(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return (new Loggable("149154", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable invalidHandleResponseLoggable(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return new Loggable("149154", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String duplicateHandleResponse(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("149155", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable duplicateHandleResponseLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149155", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String illegalStateForStart(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("149156", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable illegalStateForStartLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149156", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String nothingToDoForTask(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("149157", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable nothingToDoForTaskLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149157", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String noAppFilesExist(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("149158", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable noAppFilesExistLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149158", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String nullStagingDirectory() {
      Object[] var0 = new Object[0];
      return (new Loggable("149159", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable nullStagingDirectoryLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("149159", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String nullName() {
      Object[] var0 = new Object[0];
      return (new Loggable("149160", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable nullNameLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("149160", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String operationRequiresTarget() {
      Object[] var0 = new Object[0];
      return (new Loggable("149161", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable operationRequiresTargetLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("149161", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String operationRequiresPlan() {
      Object[] var0 = new Object[0];
      return (new Loggable("149162", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable operationRequiresPlanLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("149162", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String nonExclusiveModeLock() {
      Object[] var0 = new Object[0];
      return (new Loggable("149163", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable nonExclusiveModeLockLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("149163", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String exclusiveModeLock() {
      Object[] var0 = new Object[0];
      return (new Loggable("149164", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable exclusiveModeLockLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("149164", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String invalidTarget() {
      Object[] var0 = new Object[0];
      return (new Loggable("149165", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable invalidTargetLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("149165", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String configLocked() {
      Object[] var0 = new Object[0];
      return (new Loggable("149166", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable configLockedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("149166", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String nothingToCommit(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("149167", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable nothingToCommitLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149167", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String requestCompletedOrCancelled(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return (new Loggable("149168", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable requestCompletedOrCancelledLoggable(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return new Loggable("149168", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String requiresRestart() {
      Object[] var0 = new Object[0];
      return (new Loggable("149169", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable requiresRestartLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("149169", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String mixedTargetError() {
      Object[] var0 = new Object[0];
      return (new Loggable("149170", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable mixedTargetErrorLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("149170", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logTaskDeferred(String var0) {
      Object[] var1 = new Object[]{var0};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149171", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149171";
   }

   public static Loggable logTaskDeferredLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149171", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logNonDynamicPropertyChange(long var0, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{new Long(var0), var2, var3, var4};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149172", var5, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149172";
   }

   public static Loggable logNonDynamicPropertyChangeLoggable(long var0, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{new Long(var0), var2, var3, var4};
      return new Loggable("149172", var5, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logPartialClusterTarget(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149178", var4, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149178";
   }

   public static Loggable logPartialClusterTargetLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("149178", var4, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logErrorOnAbortEditSession(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149181", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149181";
   }

   public static Loggable logErrorOnAbortEditSessionLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149181", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String invalidAltDDDuringRedeploy() {
      Object[] var0 = new Object[0];
      return (new Loggable("149182", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable invalidAltDDDuringRedeployLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("149182", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String invalidRetireTimeout(String var0, String var1, int var2) {
      Object[] var3 = new Object[]{var0, var1, new Integer(var2)};
      return (new Loggable("149183", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable invalidRetireTimeoutLoggable(String var0, String var1, int var2) {
      Object[] var3 = new Object[]{var0, var1, new Integer(var2)};
      return new Loggable("149183", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String invalidRedeployOnAutodeployedApp(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("149184", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable invalidRedeployOnAutodeployedAppLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149184", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String invalidUndeployOnAutodeployedApp(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("149185", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable invalidUndeployOnAutodeployedAppLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149185", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String invalidDistributeOnAutodeployedApp(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("149186", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable invalidDistributeOnAutodeployedAppLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149186", var1, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logApplicationUpgradeProblem(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149187", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149187";
   }

   public static Loggable logApplicationUpgradeProblemLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149187", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logNullAppNonDynamic(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149188", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149188";
   }

   public static Loggable logNullAppNonDynamicLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149188", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logNullDeploymentMBean(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149189", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149189";
   }

   public static Loggable logNullDeploymentMBeanLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149189", var2, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String expiredLockPendingChanges() {
      Object[] var0 = new Object[0];
      return (new Loggable("149190", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable expiredLockPendingChangesLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("149190", var0, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logInitStatus(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return (new Loggable("149191", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable logInitStatusLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149191", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logProgressStatus(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return (new Loggable("149192", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable logProgressStatusLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149192", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logFailedStatus(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return (new Loggable("149193", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable logFailedStatusLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149193", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logSuccessStatus(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return (new Loggable("149194", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable logSuccessStatusLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149194", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logUnavailableStatus(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return (new Loggable("149195", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable logUnavailableStatusLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149195", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logTaskConflict(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149196", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149196";
   }

   public static Loggable logTaskConflictLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149196", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   public static String logInvalidAppState(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149197", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.class.getClassLoader()));
      return "149197";
   }

   public static Loggable logInvalidAppStateLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("149197", var3, "weblogic.management.deploy.internal.DeployerRuntimeLogLocalizer", DeployerRuntimeLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeployerRuntimeLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = DeployerRuntimeLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = DeployerRuntimeLogger.findMessageLogger();
      }
   }
}
