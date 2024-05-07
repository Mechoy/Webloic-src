package weblogic.deploy.api.internal;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class SPIDeployerLogger {
   private static final String LOCALIZER_CLASS = "weblogic.deploy.api.internal.SPIDeployerLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(SPIDeployerLogger.class.getName());
   }

   public static String getDisplayName() {
      Object[] var0 = new Object[0];
      return (new Loggable("260000", var0, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getDisplayNameLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("260000", var0, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String getInvalidURI(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260001", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getInvalidURILoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260001", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String getInvalidServerAuth(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260002", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getInvalidServerAuthLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260002", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String notConnected() {
      Object[] var0 = new Object[0];
      return (new Loggable("260003", var0, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable notConnectedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("260003", var0, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String unsupportedLocale(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260004", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable unsupportedLocaleLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260004", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String unsupportedVersion(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260008", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable unsupportedVersionLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260008", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String noClass(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("260009", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable noClassLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("260009", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String failedMBeanConnection(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return (new Loggable("260010", var3, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable failedMBeanConnectionLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("260010", var3, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String noSuchTarget(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("260012", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable noSuchTargetLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("260012", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String nullTargetArray() {
      Object[] var0 = new Object[0];
      return (new Loggable("260013", var0, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable nullTargetArrayLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("260013", var0, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String unsupported(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260015", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable unsupportedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260015", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String noFile(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260016", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable noFileLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260016", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String noSuchApp(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260017", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable noSuchAppLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260017", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String successfulTransition(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return (new Loggable("260020", var5, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable successfulTransitionLoggable(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return new Loggable("260020", var5, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String failedTransition(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return (new Loggable("260021", var5, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable failedTransitionLoggable(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return new Loggable("260021", var5, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String appNotification(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return (new Loggable("260022", var3, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable appNotificationLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("260022", var3, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String lostTask() {
      Object[] var0 = new Object[0];
      return (new Loggable("260023", var0, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable lostTaskLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("260023", var0, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String reportErrorEvent(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return (new Loggable("260024", var5, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable reportErrorEventLoggable(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return new Loggable("260024", var5, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String unknownError(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260026", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable unknownErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260026", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String notRootTMID(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("260027", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable notRootTMIDLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("260027", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String nullTMID(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260028", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable nullTMIDLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260028", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String diffTMID(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return (new Loggable("260029", var3, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable diffTMIDLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("260029", var3, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String noAppForTMID(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260030", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable noAppForTMIDLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260030", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String noop(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260031", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable noopLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260031", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String unsupportedModuleType(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260036", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable unsupportedModuleTypeLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260036", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String unexpectedDD(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260037", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable unexpectedDDLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260037", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String getNoDelta(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260040", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getNoDeltaLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260040", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String uploadFailure(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("260041", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable uploadFailureLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("260041", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String invalidClass(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("260050", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable invalidClassLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("260050", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String nullDCB() {
      Object[] var0 = new Object[0];
      return (new Loggable("260052", var0, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable nullDCBLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("260052", var0, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String badPlan(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260055", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable badPlanLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260055", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String invalidMBean(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260056", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable invalidMBeanLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260056", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String badDDBean(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260061", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable badDDBeanLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260061", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String logRestoreDCB(String var0) {
      Object[] var1 = new Object[]{var0};
      SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("260067", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.class.getClassLoader()));
      return "260067";
   }

   public static Loggable logRestoreDCBLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260067", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String logRemoveDCB(String var0) {
      Object[] var1 = new Object[]{var0};
      SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("260068", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.class.getClassLoader()));
      return "260068";
   }

   public static Loggable logRemoveDCBLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260068", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String logSaveDCB(String var0) {
      Object[] var1 = new Object[]{var0};
      SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("260070", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.class.getClassLoader()));
      return "260070";
   }

   public static Loggable logSaveDCBLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260070", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String logRestore(String var0) {
      Object[] var1 = new Object[]{var0};
      SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("260071", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.class.getClassLoader()));
      return "260071";
   }

   public static Loggable logRestoreLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260071", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String logSave(String var0) {
      Object[] var1 = new Object[]{var0};
      SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("260072", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.class.getClassLoader()));
      return "260072";
   }

   public static Loggable logSaveLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260072", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String logBeanError(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("260073", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.class.getClassLoader()));
      return "260073";
   }

   public static Loggable logBeanErrorLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("260073", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String logNoDCB(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("260078", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.class.getClassLoader()));
      return "260078";
   }

   public static Loggable logNoDCBLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("260078", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String invalidInstallDir(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260080", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable invalidInstallDirLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260080", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String logDDCreateError(String var0) {
      Object[] var1 = new Object[]{var0};
      SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("260081", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.class.getClassLoader()));
      return "260081";
   }

   public static Loggable logDDCreateErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260081", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String logNoCMPDD(String var0) {
      Object[] var1 = new Object[]{var0};
      SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("260082", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.class.getClassLoader()));
      return "260082";
   }

   public static Loggable logNoCMPDDLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260082", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String logNoPlan(String var0) {
      Object[] var1 = new Object[]{var0};
      SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("260083", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.class.getClassLoader()));
      return "260083";
   }

   public static Loggable logNoPlanLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260083", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String logNoSave(String var0, boolean var1, boolean var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("260085", var3, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.class.getClassLoader()));
      return "260085";
   }

   public static Loggable logNoSaveLoggable(String var0, boolean var1, boolean var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("260085", var3, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String logSaveDD(String var0) {
      Object[] var1 = new Object[]{var0};
      SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("260086", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.class.getClassLoader()));
      return "260086";
   }

   public static Loggable logSaveDDLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260086", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String getMissingExt(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return (new Loggable("260087", var4, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getMissingExtLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("260087", var4, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String logMissingDD(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("260088", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.class.getClassLoader()));
      return "260088";
   }

   public static Loggable logMissingDDLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("260088", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String logAppReadError(String var0, String var1, Exception var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("260091", var3, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.class.getClassLoader()));
      return "260091";
   }

   public static Loggable logAppReadErrorLoggable(String var0, String var1, Exception var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("260091", var3, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String logAddDS(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("260094", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.class.getClassLoader()));
      return "260094";
   }

   public static Loggable logAddDSLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("260094", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String getRenameError(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260095", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getRenameErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260095", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String getUnknownType(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260096", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getUnknownTypeLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260096", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String badRootBean(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260097", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable badRootBeanLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260097", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String noTagRegistered(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260098", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable noTagRegisteredLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260098", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String noTagFound(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return (new Loggable("260099", var3, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable noTagFoundLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("260099", var3, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String invalidSecurityModel(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("260100", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable invalidSecurityModelLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("260100", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String logNullParam(String var0) {
      Object[] var1 = new Object[]{var0};
      SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("260101", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.class.getClassLoader()));
      return "260101";
   }

   public static Loggable logNullParamLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260101", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String invalidExport(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return (new Loggable("260102", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable invalidExportLoggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("260102", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String noSuchBean(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260103", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable noSuchBeanLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260103", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String notChangable(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260104", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable notChangableLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260104", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String parseError(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return (new Loggable("260106", var3, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable parseErrorLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("260106", var3, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String marshalError(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("260107", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable marshalErrorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("260107", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String restoreError(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260108", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable restoreErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260108", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String marshalPlanError(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260109", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable marshalPlanErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260109", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String createError(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260110", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable createErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260110", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String connectionError() {
      Object[] var0 = new Object[0];
      return (new Loggable("260111", var0, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable connectionErrorLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("260111", var0, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String nullTmids() {
      Object[] var0 = new Object[0];
      return (new Loggable("260112", var0, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable nullTmidsLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("260112", var0, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String planIsDir(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260113", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable planIsDirLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260113", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String rootIsFile(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260114", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable rootIsFileLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260114", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String noAppProvided() {
      Object[] var0 = new Object[0];
      return (new Loggable("260115", var0, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable noAppProvidedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("260115", var0, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String mustInit() {
      Object[] var0 = new Object[0];
      return (new Loggable("260116", var0, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable mustInitLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("260116", var0, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String invalidFactory(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260117", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable invalidFactoryLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260117", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String nullTarget() {
      Object[] var0 = new Object[0];
      return (new Loggable("260118", var0, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable nullTargetLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("260118", var0, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String notDir(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260119", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable notDirLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260119", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String logInitOperation(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("260121", var4, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.class.getClassLoader()));
      return "260121";
   }

   public static Loggable logInitOperationLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("260121", var4, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String logInitStreamOperation(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("260122", var3, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.class.getClassLoader()));
      return "260122";
   }

   public static Loggable logInitStreamOperationLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("260122", var3, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String noTargetInfo(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260123", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable noTargetInfoLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260123", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String getConfiguredTargets() {
      Object[] var0 = new Object[0];
      return (new Loggable("260124", var0, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getConfiguredTargetsLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("260124", var0, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String logConnectionError(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("260125", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.class.getClassLoader()));
      return "260125";
   }

   public static Loggable logConnectionErrorLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("260125", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String versionMismatchPlan(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("260126", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable versionMismatchPlanLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("260126", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String logDTDDDUpdate(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("260128", var3, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.class.getClassLoader()));
      return "260128";
   }

   public static Loggable logDTDDDUpdateLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("260128", var3, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String logDTDDDExport(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("260129", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.class.getClassLoader()));
      return "260129";
   }

   public static Loggable logDTDDDExportLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("260129", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String logPollerError(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("260130", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.class.getClassLoader()));
      return "260130";
   }

   public static Loggable logPollerErrorLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260130", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String noURI() {
      Object[] var0 = new Object[0];
      return (new Loggable("260131", var0, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable noURILoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("260131", var0, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String notAChild(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("260132", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable notAChildLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("260132", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String getDDBeanCreateError(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("260133", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getDDBeanCreateErrorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("260133", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String nullAppName(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("260134", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable nullAppNameLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260134", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String restorePlanFailure(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("260137", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable restorePlanFailureLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("260137", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String backupPlanError(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("260138", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable backupPlanErrorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("260138", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String logRestorePlan(String var0) {
      Object[] var1 = new Object[]{var0};
      SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("260139", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.class.getClassLoader()));
      return "260139";
   }

   public static Loggable logRestorePlanLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("260139", var1, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String logUnableToRemoveDescriptorBean(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("260140", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.class.getClassLoader()));
      return "260140";
   }

   public static Loggable logUnableToRemoveDescriptorBeanLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("260140", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String getReinitializeError() {
      Object[] var0 = new Object[0];
      return (new Loggable("260141", var0, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getReinitializeErrorLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("260141", var0, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   public static String unknownDD(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("260142", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable unknownDDLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("260142", var2, "weblogic.deploy.api.internal.SPIDeployerLogLocalizer", SPIDeployerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SPIDeployerLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = SPIDeployerLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = SPIDeployerLogger.findMessageLogger();
      }
   }
}
