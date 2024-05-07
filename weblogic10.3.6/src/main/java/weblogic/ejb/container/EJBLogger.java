package weblogic.ejb.container;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class EJBLogger {
   private static final String LOCALIZER_CLASS = "weblogic.ejb.container.EJBLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(EJBLogger.class.getName());
   }

   public static String logExceptionDuringEJBActivate(Exception var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010000", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010000";
   }

   public static Loggable logExceptionDuringEJBActivateLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010000", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logRedeployClasspathFailure(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010001", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010001";
   }

   public static Loggable logRedeployClasspathFailureLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010001", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorUndeploying(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010002", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010002";
   }

   public static Loggable logErrorUndeployingLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010002", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExceptionLoadingTimestamp(Exception var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010003", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010003";
   }

   public static Loggable logExceptionLoadingTimestampLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010003", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorSavingTimestamps(Exception var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010006", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010006";
   }

   public static Loggable logErrorSavingTimestampsLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010006", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logLicenseValidation(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010007", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010007";
   }

   public static Loggable logLicenseValidationLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010007", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logDeploying(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010008", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010008";
   }

   public static Loggable logDeployingLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010008", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logDeployedWithJNDIName(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010009", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010009";
   }

   public static Loggable logDeployedWithJNDINameLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010009", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExcepLookingUpXn2(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010011", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010011";
   }

   public static Loggable logExcepLookingUpXn2Loggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010011", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExcepLookingUpXn3(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010012", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010012";
   }

   public static Loggable logExcepLookingUpXn3Loggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010012", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorOnRollback(Exception var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010014", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010014";
   }

   public static Loggable logErrorOnRollbackLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010014", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorMarkingRollback(Exception var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010015", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010015";
   }

   public static Loggable logErrorMarkingRollbackLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010015", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorMarkingForRollback(Exception var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010016", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010016";
   }

   public static Loggable logErrorMarkingForRollbackLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010016", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorDuringRollback(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010017", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010017";
   }

   public static Loggable logErrorDuringRollbackLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010017", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorResumingTx(Exception var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010018", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010018";
   }

   public static Loggable logErrorResumingTxLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010018", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logNoClusterName() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010019", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010019";
   }

   public static Loggable logNoClusterNameLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010019", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorStartingJMSConnection(Exception var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010020", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010020";
   }

   public static Loggable logErrorStartingJMSConnectionLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010020", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logClustersNotHomogeneous(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010021", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010021";
   }

   public static Loggable logClustersNotHomogeneousLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010021", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorPassivating(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010022", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010022";
   }

   public static Loggable logErrorPassivatingLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010022", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorDuringPassivation(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010024", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010024";
   }

   public static Loggable logErrorDuringPassivationLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010024", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorDuringRollback1(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010025", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010025";
   }

   public static Loggable logErrorDuringRollback1Loggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010025", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorDuringCommit(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010026", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010026";
   }

   public static Loggable logErrorDuringCommitLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010026", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorDuringCommit2(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010029", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010029";
   }

   public static Loggable logErrorDuringCommit2Loggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010029", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logIgnoreExcepOnRollback(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010030", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010030";
   }

   public static Loggable logIgnoreExcepOnRollbackLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010030", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExcepInMethod(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010031", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010031";
   }

   public static Loggable logExcepInMethodLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010031", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorDuringActivate(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010032", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010032";
   }

   public static Loggable logErrorDuringActivateLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010032", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorFromLoad(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010033", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010033";
   }

   public static Loggable logErrorFromLoadLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010033", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorFromStore(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010034", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010034";
   }

   public static Loggable logErrorFromStoreLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010034", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExcepFromStore(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010036", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010036";
   }

   public static Loggable logExcepFromStoreLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010036", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExcepFromSuperLoad(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010038", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010038";
   }

   public static Loggable logExcepFromSuperLoadLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010038", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExcepInStore(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010039", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010039";
   }

   public static Loggable logExcepInStoreLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010039", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExcepInStore1(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010040", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010040";
   }

   public static Loggable logExcepInStore1Loggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010040", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExcepInActivate(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010043", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010043";
   }

   public static Loggable logExcepInActivateLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010043", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExcepResumingTx(Exception var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010044", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010044";
   }

   public static Loggable logExcepResumingTxLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010044", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExcepInAfterBegin(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010045", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010045";
   }

   public static Loggable logExcepInAfterBeginLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010045", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExcepInBeforeCompletion(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010046", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010046";
   }

   public static Loggable logExcepInBeforeCompletionLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010046", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExcepDuringSetRollbackOnly(Exception var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010047", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010047";
   }

   public static Loggable logExcepDuringSetRollbackOnlyLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010047", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExcepInAfterCompletion(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010048", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010048";
   }

   public static Loggable logExcepInAfterCompletionLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010048", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExcepInMethod1(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010049", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010049";
   }

   public static Loggable logExcepInMethod1Loggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010049", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logMustCommit() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010050", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010050";
   }

   public static Loggable logMustCommitLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010050", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExcepDuringInvocFromHome(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010051", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010051";
   }

   public static Loggable logExcepDuringInvocFromHomeLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010051", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorCreatingFreepool(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010052", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010052";
   }

   public static Loggable logErrorCreatingFreepoolLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010052", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logEJBClassFoundInClasspath(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010054", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010054";
   }

   public static Loggable logEJBClassFoundInClasspathLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010054", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logCouldNotFindSpecifiedRDBMSDescriptorInJarFile(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010055", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010055";
   }

   public static Loggable logCouldNotFindSpecifiedRDBMSDescriptorInJarFileLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("010055", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logServiceNotInitialized() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010057", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010057";
   }

   public static Loggable logServiceNotInitializedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010057", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logEJBBeingRecompiledOnServer(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010058", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010058";
   }

   public static Loggable logEJBBeingRecompiledOnServerLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010058", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExceptionDuringROInvalidation(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010059", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010059";
   }

   public static Loggable logExceptionDuringROInvalidationLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010059", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logMDBReConnectedToJMS(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010060", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010060";
   }

   public static Loggable logMDBReConnectedToJMSLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010060", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logMDBUnableToConnectToJMS(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010061", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010061";
   }

   public static Loggable logMDBUnableToConnectToJMSLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("010061", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExcepInOnMessageCallOnMDB(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010065", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010065";
   }

   public static Loggable logExcepInOnMessageCallOnMDBLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010065", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExceptionDuringEJBUnsetEntityContext(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010066", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010066";
   }

   public static Loggable logExceptionDuringEJBUnsetEntityContextLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010066", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExceptionDuringEJBRemove(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010067", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010067";
   }

   public static Loggable logExceptionDuringEJBRemoveLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010067", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorPoppingCallerPrincipal(Exception var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010071", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010071";
   }

   public static Loggable logErrorPoppingCallerPrincipalLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010071", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExceptionDuringEJBModuleStart(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010072", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010072";
   }

   public static Loggable logExceptionDuringEJBModuleStartLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010072", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logUnableToAddToClientJarDueToClasspath(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010073", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010073";
   }

   public static Loggable logUnableToAddToClientJarDueToClasspathLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010073", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logUnableToCreateClientJarDueToClasspathIssues() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010074", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010074";
   }

   public static Loggable logUnableToCreateClientJarDueToClasspathIssuesLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010074", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logSkippingClientJarCreationSinceNoRemoteEJBsFound() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010075", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010075";
   }

   public static Loggable logSkippingClientJarCreationSinceNoRemoteEJBsFoundLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010075", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logClientJarCreated(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010076", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010076";
   }

   public static Loggable logClientJarCreatedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010076", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logJMSExceptionReceivingForMDB(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010079", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010079";
   }

   public static Loggable logJMSExceptionReceivingForMDBLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010079", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logJMSExceptionProcessingMDB(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010080", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010080";
   }

   public static Loggable logJMSExceptionProcessingMDBLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010080", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logUsingSingleThreadForMDBTopic(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010081", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010081";
   }

   public static Loggable logUsingSingleThreadForMDBTopicLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010081", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFailedToUndeploySecurityRole(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010082", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010082";
   }

   public static Loggable logFailedToUndeploySecurityRoleLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010082", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFailedToUndeploySecurityPolicy(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010083", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010083";
   }

   public static Loggable logFailedToUndeploySecurityPolicyLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010083", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logMDBsBeingSuspended() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010084", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010084";
   }

   public static Loggable logMDBsBeingSuspendedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010084", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logMDBsDoneSuspending() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010085", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010085";
   }

   public static Loggable logMDBsDoneSuspendingLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010085", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorOnStartMDBs(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010086", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010086";
   }

   public static Loggable logErrorOnStartMDBsLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010086", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logEJBBeingRecompiledOnServerKeepgenerated(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010087", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010087";
   }

   public static Loggable logEJBBeingRecompiledOnServerKeepgeneratedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010087", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logNoInMemoryReplicationLicense() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010088", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010088";
   }

   public static Loggable logNoInMemoryReplicationLicenseLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010088", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFailedToCreateCopy(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010089", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010089";
   }

   public static Loggable logFailedToCreateCopyLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010089", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFailedToUpdateSecondaryCopy(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010090", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010090";
   }

   public static Loggable logFailedToUpdateSecondaryCopyLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010090", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFailureInReplication(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010091", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010091";
   }

   public static Loggable logFailureInReplicationLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010091", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFailedToUpdateSecondaryDuringReplication(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010092", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010092";
   }

   public static Loggable logFailedToUpdateSecondaryDuringReplicationLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010092", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFailedToUpdateSecondary(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010094", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010094";
   }

   public static Loggable logFailedToUpdateSecondaryLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010094", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logMDBReconnectInfo(String var0, String var1, int var2, int var3, long var4) {
      Object[] var6 = new Object[]{var0, var1, new Integer(var2), new Integer(var3), new Long(var4)};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010096", var6, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010096";
   }

   public static Loggable logMDBReconnectInfoLoggable(String var0, String var1, int var2, int var3, long var4) {
      Object[] var6 = new Object[]{var0, var1, new Integer(var2), new Integer(var3), new Long(var4)};
      return new Loggable("010096", var6, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorDuringBeanInvocation(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010097", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010097";
   }

   public static Loggable logErrorDuringBeanInvocationLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("010097", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorAboutDatabaseType(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010098", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010098";
   }

   public static Loggable logErrorAboutDatabaseTypeLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010098", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logWarningFromEJBQLCompiler(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010099", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010099";
   }

   public static Loggable logWarningFromEJBQLCompilerLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010099", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logWarningOnSFSBInMemoryReplicationFeature(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010100", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010100";
   }

   public static Loggable logWarningOnSFSBInMemoryReplicationFeatureLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010100", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logMismatchBetweenBeanAndGeneratedCode(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010101", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010101";
   }

   public static Loggable logMismatchBetweenBeanAndGeneratedCodeLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010101", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logerrorCreatingDefaultDBMSTable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010102", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010102";
   }

   public static Loggable logerrorCreatingDefaultDBMSTableLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010102", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logmissingInMemoryRepLicense(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010103", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010103";
   }

   public static Loggable logmissingInMemoryRepLicenseLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010103", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logduplicateRelationshipRoleName(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010105", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010105";
   }

   public static Loggable logduplicateRelationshipRoleNameLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010105", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logunableToInitializeMethodInfo(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010106", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010106";
   }

   public static Loggable logunableToInitializeMethodInfoLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010106", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String loglockRequestTimeOut(String var0, Object var1, Object var2, long var3) {
      Object[] var5 = new Object[]{var0, var1, var2, new Long(var3)};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010107", var5, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010107";
   }

   public static Loggable loglockRequestTimeOutLoggable(String var0, Object var1, Object var2, long var3) {
      Object[] var5 = new Object[]{var0, var1, var2, new Long(var3)};
      return new Loggable("010107", var5, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logunlockCouldNotFindPk(String var0, Object var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010108", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010108";
   }

   public static Loggable logunlockCouldNotFindPkLoggable(String var0, Object var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("010108", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logduplicateEJBName(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010110", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010110";
   }

   public static Loggable logduplicateEJBNameLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010110", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logproviderIsNotTransactedButMDBIsTransacted(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010112", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010112";
   }

   public static Loggable logproviderIsNotTransactedButMDBIsTransactedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010112", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logrelationshipCachingCannotEnableIfSelectTypeIsNotObject(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010113", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010113";
   }

   public static Loggable logrelationshipCachingCannotEnableIfSelectTypeIsNotObjectLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010113", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logdynamicQueriesNotEnabled() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010114", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010114";
   }

   public static Loggable logdynamicQueriesNotEnabledLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010114", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String loginvalidResultTypeMapping(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010115", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010115";
   }

   public static Loggable loginvalidResultTypeMappingLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010115", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logaccessedCmrCollectionInDifferentTransaction(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010117", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010117";
   }

   public static Loggable logaccessedCmrCollectionInDifferentTransactionLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010117", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logmissingDescriptor(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010120", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010120";
   }

   public static Loggable logmissingDescriptorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010120", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logmissingEnterpriseBeanMBean(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010122", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010122";
   }

   public static Loggable logmissingEnterpriseBeanMBeanLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010122", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logentityMBeanWrongVersion(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010123", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010123";
   }

   public static Loggable logentityMBeanWrongVersionLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010123", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logmissingCallerPrincipal(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010124", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010124";
   }

   public static Loggable logmissingCallerPrincipalLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010124", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String lognonResultSetFinderHasIntegerOrderByOrGroupByArg(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010125", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010125";
   }

   public static Loggable lognonResultSetFinderHasIntegerOrderByOrGroupByArgLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010125", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logintegerOrderByOrGroupByArgExceedsSelectListSize(String var0, String var1, int var2) {
      Object[] var3 = new Object[]{var0, var1, new Integer(var2)};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010126", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010126";
   }

   public static Loggable logintegerOrderByOrGroupByArgExceedsSelectListSizeLoggable(String var0, String var1, int var2) {
      Object[] var3 = new Object[]{var0, var1, new Integer(var2)};
      return new Loggable("010126", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logcmpBeanMustHaveTXDataSourceSpecified(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010127", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010127";
   }

   public static Loggable logcmpBeanMustHaveTXDataSourceSpecifiedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010127", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logmissingCacheDefinition(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010128", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010128";
   }

   public static Loggable logmissingCacheDefinitionLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010128", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String lognotAnExclusiveCache(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010129", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010129";
   }

   public static Loggable lognotAnExclusiveCacheLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010129", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String lognotAMultiVersionCache(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010130", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010130";
   }

   public static Loggable lognotAMultiVersionCacheLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010130", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logselectForUpdateNotSupported(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010132", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010132";
   }

   public static Loggable logselectForUpdateNotSupportedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010132", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logillegalAttemptToAssignRemovedBeanToCMRField(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010133", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010133";
   }

   public static Loggable logillegalAttemptToAssignRemovedBeanToCMRFieldLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010133", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logparamInteger() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010136", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010136";
   }

   public static Loggable logparamIntegerLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010136", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logparamPositiveInteger() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010137", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010137";
   }

   public static Loggable logparamPositiveIntegerLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010137", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String loginvalidFieldGroupName(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010138", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010138";
   }

   public static Loggable loginvalidFieldGroupNameLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010138", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logduplicateKeyFound(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010139", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010139";
   }

   public static Loggable logduplicateKeyFoundLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010139", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String lognoSuchEntityException(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010140", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010140";
   }

   public static Loggable lognoSuchEntityExceptionLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010140", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logerrorInsertingInJoinTable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010141", var4, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010141";
   }

   public static Loggable logerrorInsertingInJoinTableLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("010141", var4, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logbeanDoesNotExist(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010142", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010142";
   }

   public static Loggable logbeanDoesNotExistLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010142", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logoptimisticUpdateFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010143", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010143";
   }

   public static Loggable logoptimisticUpdateFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010143", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logcannotCallSetOnPk() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010144", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010144";
   }

   public static Loggable logcannotCallSetOnPkLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010144", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logcannotCallSetOnCmpCmrField() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010145", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010145";
   }

   public static Loggable logcannotCallSetOnCmpCmrFieldLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010145", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logsetCheckForCmrFieldAsPk() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010146", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010146";
   }

   public static Loggable logsetCheckForCmrFieldAsPkLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010146", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logsetCheckForCmrFieldDuringEjbCreate() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010147", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010147";
   }

   public static Loggable logsetCheckForCmrFieldDuringEjbCreateLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010147", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logpkNotSet(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010148", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010148";
   }

   public static Loggable logpkNotSetLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010148", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String lognullAssignedToCmrField() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010149", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010149";
   }

   public static Loggable lognullAssignedToCmrFieldLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010149", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logillegalConcurrencyStrategy(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010152", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010152";
   }

   public static Loggable logillegalConcurrencyStrategyLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010152", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logonlyRemoteCanInvokeGetEJBObject() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010153", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010153";
   }

   public static Loggable logonlyRemoteCanInvokeGetEJBObjectLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010153", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logonlyLocalCanInvokeGetEJBObject() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010154", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010154";
   }

   public static Loggable logonlyLocalCanInvokeGetEJBObjectLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010154", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logonlyCMTBeanCanInvokeGetRollbackOnly() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010155", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010155";
   }

   public static Loggable logonlyCMTBeanCanInvokeGetRollbackOnlyLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010155", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logillegalCallToGetRollbackOnly() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010156", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010156";
   }

   public static Loggable logillegalCallToGetRollbackOnlyLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010156", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logonlyCMTBeanCanInvokeSetRollbackOnly() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010157", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010157";
   }

   public static Loggable logonlyCMTBeanCanInvokeSetRollbackOnlyLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010157", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logillegalCallToSetRollbackOnly() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010158", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010158";
   }

   public static Loggable logillegalCallToSetRollbackOnlyLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010158", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logneedJNDINameForHomeHandles(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010159", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010159";
   }

   public static Loggable logneedJNDINameForHomeHandlesLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010159", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logaccessDeniedOnEJBResource(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010160", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010160";
   }

   public static Loggable logaccessDeniedOnEJBResourceLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010160", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String loghandleNull() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010161", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010161";
   }

   public static Loggable loghandleNullLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010161", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logejbObjectNull() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010162", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010162";
   }

   public static Loggable logejbObjectNullLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010162", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String loghomeWasNull() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010163", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010163";
   }

   public static Loggable loghomeWasNullLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010163", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logejbObjectNotFromThisHome() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010164", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010164";
   }

   public static Loggable logejbObjectNotFromThisHomeLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010164", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logillegalAttemptToInvokeGetPrimaryKeyClass() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010165", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010165";
   }

   public static Loggable logillegalAttemptToInvokeGetPrimaryKeyClassLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010165", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logerrorStartingMDBTx(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010166", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010166";
   }

   public static Loggable logerrorStartingMDBTxLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010166", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logmdbCannotInvokeThisMethod(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010167", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010167";
   }

   public static Loggable logmdbCannotInvokeThisMethodLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010167", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logaccessException(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010168", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010168";
   }

   public static Loggable logaccessExceptionLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010168", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String loginsufficientPermission(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010169", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010169";
   }

   public static Loggable loginsufficientPermissionLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010169", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logsessionBeanCannotCallGetPrimaryKey() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010171", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010171";
   }

   public static Loggable logsessionBeanCannotCallGetPrimaryKeyLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010171", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String loginvalidRemoveCall() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010172", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010172";
   }

   public static Loggable loginvalidRemoveCallLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010172", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logcharEnvEntryHasLengthZero() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010173", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010173";
   }

   public static Loggable logcharEnvEntryHasLengthZeroLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010173", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String lognoJNDIForResourceRef(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010174", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010174";
   }

   public static Loggable lognoJNDIForResourceRefLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010174", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String lognoJNDIForResourceEnvRef(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010176", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010176";
   }

   public static Loggable lognoJNDIForResourceEnvRefLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010176", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logunableToCreateJMSConnectionFactory(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010177", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010177";
   }

   public static Loggable logunableToCreateJMSConnectionFactoryLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010177", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String loglocalSessionBeanCannotCallGetPrimaryKey() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010179", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010179";
   }

   public static Loggable loglocalSessionBeanCannotCallGetPrimaryKeyLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010179", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logillegalReentrantCall(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010180", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010180";
   }

   public static Loggable logillegalReentrantCallLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010180", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logfindByPkReturnedNull(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010181", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010181";
   }

   public static Loggable logfindByPkReturnedNullLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010181", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logfinderReturnedNull(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010182", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010182";
   }

   public static Loggable logfinderReturnedNullLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010182", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String loggetEnvironmentDeprecated() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010183", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010183";
   }

   public static Loggable loggetEnvironmentDeprecatedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010183", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String loginvalidMethodSignature(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010184", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010184";
   }

   public static Loggable loginvalidMethodSignatureLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010184", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logbeanNotCreated(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010185", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010185";
   }

   public static Loggable logbeanNotCreatedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010185", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logbeanNotFound(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010186", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010186";
   }

   public static Loggable logbeanNotFoundLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010186", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logerrorDroppingDefaultDBMSTable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010188", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010188";
   }

   public static Loggable logerrorDroppingDefaultDBMSTableLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010188", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logerrorAlteringDefaultDBMSTable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010189", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010189";
   }

   public static Loggable logerrorAlteringDefaultDBMSTableLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010189", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logcannotSpecifyBlobClobInOrderby(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010190", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010190";
   }

   public static Loggable logcannotSpecifyBlobClobInOrderbyLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010190", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logalterTableNotSupported(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010191", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010191";
   }

   public static Loggable logalterTableNotSupportedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010191", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logillegalCallToEJBContextMethod(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010193", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010193";
   }

   public static Loggable logillegalCallToEJBContextMethodLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010193", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logbmtCanUseUserTransaction() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010194", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010194";
   }

   public static Loggable logbmtCanUseUserTransactionLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010194", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String loginsufficientPermissionToUser(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010195", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010195";
   }

   public static Loggable loginsufficientPermissionToUserLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010195", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logAnomalousRRBehaviorPossible(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010197", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010197";
   }

   public static Loggable logAnomalousRRBehaviorPossibleLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010197", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExcepionUninitializing(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010198", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010198";
   }

   public static Loggable logExcepionUninitializingLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010198", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logRunAsPrincipalChosenFromSecurityRoleAssignment(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010199", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010199";
   }

   public static Loggable logRunAsPrincipalChosenFromSecurityRoleAssignmentLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("010199", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExceptionRecoveringJMSSession(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010201", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010201";
   }

   public static Loggable logExceptionRecoveringJMSSessionLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010201", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logCallByReferenceNotEnabled(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010202", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010202";
   }

   public static Loggable logCallByReferenceNotEnabledLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010202", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logEJBModuleRolledBackToUpdateNonBeanClass(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010204", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010204";
   }

   public static Loggable logEJBModuleRolledBackToUpdateNonBeanClassLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010204", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logEJBModuleRolledBackSinceImplCLDisabled(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010205", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010205";
   }

   public static Loggable logEJBModuleRolledBackSinceImplCLDisabledLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010205", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logEJBModuleRolledBackSinceChangeIncompatible(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010206", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010206";
   }

   public static Loggable logEJBModuleRolledBackSinceChangeIncompatibleLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010206", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logLicenseValidationError(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010207", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010207";
   }

   public static Loggable logLicenseValidationErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010207", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logDDLFileCreated(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010208", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010208";
   }

   public static Loggable logDDLFileCreatedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010208", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logUnableToDeleteDDLFile(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010209", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010209";
   }

   public static Loggable logUnableToDeleteDDLFileLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010209", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logUnableToWriteToDDLFile(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010210", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010210";
   }

   public static Loggable logUnableToWriteToDDLFileLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010210", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logalterTableNotSupportedForPointbaseLoggable() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010211", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010211";
   }

   public static Loggable logalterTableNotSupportedForPointbaseLoggableLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("010211", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logEJBUsesDefaultTXAttribute(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010212", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010212";
   }

   public static Loggable logEJBUsesDefaultTXAttributeLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("010212", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logTxRolledbackInfo(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010213", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010213";
   }

   public static Loggable logTxRolledbackInfoLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010213", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorStartingMDB(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010214", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010214";
   }

   public static Loggable logErrorStartingMDBLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010214", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logMDBDurableSubscriptionDeletion(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010215", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010215";
   }

   public static Loggable logMDBDurableSubscriptionDeletionLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010215", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logMDBRedeliveryInfo(String var0, int var1, long var2) {
      Object[] var4 = new Object[]{var0, new Integer(var1), new Long(var2)};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010216", var4, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010216";
   }

   public static Loggable logMDBRedeliveryInfoLoggable(String var0, int var1, long var2) {
      Object[] var4 = new Object[]{var0, new Integer(var1), new Long(var2)};
      return new Loggable("010216", var4, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logMDBUnableToConnectToJCA(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010221", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010221";
   }

   public static Loggable logMDBUnableToConnectToJCALoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("010221", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logNoResultsForAggregateQuery(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010222", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010222";
   }

   public static Loggable logNoResultsForAggregateQueryLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010222", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logDeployedMDB(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010223", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010223";
   }

   public static Loggable logDeployedMDBLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010223", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logConfiguredEJBTimeoutDelayApplied(String var0, int var1, long var2) {
      Object[] var4 = new Object[]{var0, new Integer(var1), new Long(var2)};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010224", var4, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010224";
   }

   public static Loggable logConfiguredEJBTimeoutDelayAppliedLoggable(String var0, int var1, long var2) {
      Object[] var4 = new Object[]{var0, new Integer(var1), new Long(var2)};
      return new Loggable("010224", var4, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logEJBTimeoutDelayAutomaticallyApplied(String var0, int var1, int var2) {
      Object[] var3 = new Object[]{var0, new Integer(var1), new Integer(var2)};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010225", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010225";
   }

   public static Loggable logEJBTimeoutDelayAutomaticallyAppliedLoggable(String var0, int var1, int var2) {
      Object[] var3 = new Object[]{var0, new Integer(var1), new Integer(var2)};
      return new Loggable("010225", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logClusteredTimerFailedToLookupTimerHandler(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010226", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010226";
   }

   public static Loggable logClusteredTimerFailedToLookupTimerHandlerLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("010226", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExcepDuringInvocFromHomeOrBusiness(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010227", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010227";
   }

   public static Loggable logExcepDuringInvocFromHomeOrBusinessLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010227", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFailedToUpdateSecondaryFromBusiness(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010228", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010228";
   }

   public static Loggable logFailedToUpdateSecondaryFromBusinessLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("010228", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logunableToInitializeInterfaceMethodInfo(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("010229", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "010229";
   }

   public static Loggable logunableToInitializeInterfaceMethodInfoLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("010229", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logInstalledPersistFileNotExist(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011001", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011001";
   }

   public static Loggable logInstalledPersistFileNotExistLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011001", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logInstalledPersistFileNotReadable(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011002", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011002";
   }

   public static Loggable logInstalledPersistFileNotReadableLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011002", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logInstalledPersistFileCouldNotOpen(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011003", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011003";
   }

   public static Loggable logInstalledPersistFileCouldNotOpenLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011003", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logInstalledPersistErrorLoadingResource(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011004", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011004";
   }

   public static Loggable logInstalledPersistErrorLoadingResourceLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011004", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logInstalledPersistNoXMLProcessor(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011005", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011005";
   }

   public static Loggable logInstalledPersistNoXMLProcessorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011005", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logStatelessEOJNDIBindError(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011006", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011006";
   }

   public static Loggable logStatelessEOJNDIBindErrorLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011006", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logPersistenceManagerSetupError(Exception var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011007", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011007";
   }

   public static Loggable logPersistenceManagerSetupErrorLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011007", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logHomeJNDIRebindFailed(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011008", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011008";
   }

   public static Loggable logHomeJNDIRebindFailedLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011008", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFailedJNDIContextToJMSProvider(Exception var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011009", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011009";
   }

   public static Loggable logFailedJNDIContextToJMSProviderLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011009", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logJmsDestinationNotFound(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011010", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011010";
   }

   public static Loggable logJmsDestinationNotFoundLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011010", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logJndiNameWasNotAJMSDestination(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011011", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011011";
   }

   public static Loggable logJndiNameWasNotAJMSDestinationLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011011", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logJmsConnectionFactoryNotFound(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011012", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011012";
   }

   public static Loggable logJmsConnectionFactoryNotFoundLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011012", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logJndiNameWasNotAJMSConnectionFactory(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011013", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011013";
   }

   public static Loggable logJndiNameWasNotAJMSConnectionFactoryLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011013", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logJmsExceptionWhileCreatingConnection(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011014", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011014";
   }

   public static Loggable logJmsExceptionWhileCreatingConnectionLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011014", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logNoDestinationJNDINameSpecified() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011015", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011015";
   }

   public static Loggable logNoDestinationJNDINameSpecifiedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011015", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logPersistenceTypeSetupError(Exception var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011016", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011016";
   }

   public static Loggable logPersistenceTypeSetupErrorLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011016", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logPersistenceTypeSetupErrorWithFileName(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011017", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011017";
   }

   public static Loggable logPersistenceTypeSetupErrorWithFileNameLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011017", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logRdbmsDescriptorNotFoundInJar(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011018", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011018";
   }

   public static Loggable logRdbmsDescriptorNotFoundInJarLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011018", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logPersistenceTypeSetupErrorWithFileNameAndLineNumber(String var0, String var1, int var2) {
      Object[] var3 = new Object[]{var0, var1, new Integer(var2)};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011019", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011019";
   }

   public static Loggable logPersistenceTypeSetupErrorWithFileNameAndLineNumberLoggable(String var0, String var1, int var2) {
      Object[] var3 = new Object[]{var0, var1, new Integer(var2)};
      return new Loggable("011019", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logCmpTableMissingColumns(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011020", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011020";
   }

   public static Loggable logCmpTableMissingColumnsLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011020", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logUnableToCreateTempDir(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011022", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011022";
   }

   public static Loggable logUnableToCreateTempDirLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011022", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorReadingDD(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011023", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011023";
   }

   public static Loggable logErrorReadingDDLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011023", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logXmlParsingError(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011024", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011024";
   }

   public static Loggable logXmlParsingErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011024", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logXmlProcessingError(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011025", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011025";
   }

   public static Loggable logXmlProcessingErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011025", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFailureWhileCreatingCompEnv(Exception var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011026", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011026";
   }

   public static Loggable logFailureWhileCreatingCompEnvLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011026", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logUnableToLoadJTSDriver(Exception var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011027", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011027";
   }

   public static Loggable logUnableToLoadJTSDriverLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011027", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logDataSourceNotFound(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011028", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011028";
   }

   public static Loggable logDataSourceNotFoundLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011028", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logCouldNotGetConnectionFromDataSource(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011029", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011029";
   }

   public static Loggable logCouldNotGetConnectionFromDataSourceLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011029", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logCouldNotGetConnectionFrom(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011030", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011030";
   }

   public static Loggable logCouldNotGetConnectionFromLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011030", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logCouldNotInitializeFieldSQLTypeMap(Exception var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011032", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011032";
   }

   public static Loggable logCouldNotInitializeFieldSQLTypeMapLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011032", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExecGenKeyError(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011033", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011033";
   }

   public static Loggable logExecGenKeyErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011033", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logNoConcurrentSFSB() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011042", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011042";
   }

   public static Loggable logNoConcurrentSFSBLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011042", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logNullInvalidateParameter() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011043", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011043";
   }

   public static Loggable logNullInvalidateParameterLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011043", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorWhileMulticastingInvalidation(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011044", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011044";
   }

   public static Loggable logErrorWhileMulticastingInvalidationLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011044", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFailedToCreateRuntimeMBean(Exception var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011046", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011046";
   }

   public static Loggable logFailedToCreateRuntimeMBeanLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011046", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logContainerTransactionSetForBeanManagedEJB(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011047", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011047";
   }

   public static Loggable logContainerTransactionSetForBeanManagedEJBLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011047", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logPersistenceUsesFinderExpressions() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011048", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011048";
   }

   public static Loggable logPersistenceUsesFinderExpressionsLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011048", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logUnableToFindBeanInRDBMSDescriptor(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011049", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011049";
   }

   public static Loggable logUnableToFindBeanInRDBMSDescriptorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011049", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logUnableToFindBeanInRDBMSDescriptor1(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011050", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011050";
   }

   public static Loggable logUnableToFindBeanInRDBMSDescriptor1Loggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011050", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFinderCollectionIsNull(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011051", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011051";
   }

   public static Loggable logFinderCollectionIsNullLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011051", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logCmp20DDHasWrongDocumentType() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011052", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011052";
   }

   public static Loggable logCmp20DDHasWrongDocumentTypeLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011052", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logCmpBeanDescriptorIsNull(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011053", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011053";
   }

   public static Loggable logCmpBeanDescriptorIsNullLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011053", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logDuplicateBeanOrRelation(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011054", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011054";
   }

   public static Loggable logDuplicateBeanOrRelationLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011054", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logJNDINameAlreadyInUse(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011055", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011055";
   }

   public static Loggable logJNDINameAlreadyInUseLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011055", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logEJBModuleNeedsManualCompilation(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011056", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011056";
   }

   public static Loggable logEJBModuleNeedsManualCompilationLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011056", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logTableCreatedByUser(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011057", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011057";
   }

   public static Loggable logTableCreatedByUserLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011057", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logUnableToCreateDDLFile(Exception var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011059", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011059";
   }

   public static Loggable logUnableToCreateDDLFileLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011059", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logCouldNotInitializeFieldSQLTypeMapWithoutException() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011060", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011060";
   }

   public static Loggable logCouldNotInitializeFieldSQLTypeMapWithoutExceptionLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011060", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logStackTraceAndMessage(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011061", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011061";
   }

   public static Loggable logStackTraceAndMessageLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011061", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logStackTrace(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011062", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011062";
   }

   public static Loggable logStackTraceLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011062", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logSLSBMethodDidNotCompleteTX(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011063", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011063";
   }

   public static Loggable logSLSBMethodDidNotCompleteTXLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011063", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logWarningUnusedFieldGroups(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011070", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011070";
   }

   public static Loggable logWarningUnusedFieldGroupsLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011070", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logWarningUnusedRelationshipCachings(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011071", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011071";
   }

   public static Loggable logWarningUnusedRelationshipCachingsLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011071", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logLocalHomeJNDIRebindFailed(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011072", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011072";
   }

   public static Loggable logLocalHomeJNDIRebindFailedLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011072", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorGettingTableInformation(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011073", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011073";
   }

   public static Loggable logErrorGettingTableInformationLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("011073", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logCannotCreateReadOnlyBean(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011074", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011074";
   }

   public static Loggable logCannotCreateReadOnlyBeanLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011074", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logCannotRemoveReadOnlyBean(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011075", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011075";
   }

   public static Loggable logCannotRemoveReadOnlyBeanLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011075", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logDeploymentFailedTableDoesNotExist(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011076", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011076";
   }

   public static Loggable logDeploymentFailedTableDoesNotExistLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011076", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logTableCannotBeCreatedInProductionMode() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011077", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011077";
   }

   public static Loggable logTableCannotBeCreatedInProductionModeLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011077", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorStartingFreepoolTimer(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011078", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011078";
   }

   public static Loggable logErrorStartingFreepoolTimerLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011078", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorStoppingFreepoolTimer(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011079", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011079";
   }

   public static Loggable logErrorStoppingFreepoolTimerLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011079", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorStartingCacheTimer(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011080", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011080";
   }

   public static Loggable logErrorStartingCacheTimerLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011080", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorStoppingCacheTimer(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011081", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011081";
   }

   public static Loggable logErrorStoppingCacheTimerLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011081", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logUnableToResolveMDBMessageDestinationLink(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011082", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011082";
   }

   public static Loggable logUnableToResolveMDBMessageDestinationLinkLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("011082", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logStatefulSessionBeanAttemptToAccessTimerService() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011083", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011083";
   }

   public static Loggable logStatefulSessionBeanAttemptToAccessTimerServiceLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011083", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logIllegalAttemptToAccessTimerService() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011084", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011084";
   }

   public static Loggable logIllegalAttemptToAccessTimerServiceLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011084", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logIllegalAttemptToUseCancelledTimer() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011085", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011085";
   }

   public static Loggable logIllegalAttemptToUseCancelledTimerLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011085", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExpiredTimerHandle() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011086", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011086";
   }

   public static Loggable logExpiredTimerHandleLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011086", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logTimerHandleInvokedOutsideOriginalAppContext() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011087", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011087";
   }

   public static Loggable logTimerHandleInvokedOutsideOriginalAppContextLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011087", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExceptionInvokingEJBTimeout(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011088", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011088";
   }

   public static Loggable logExceptionInvokingEJBTimeoutLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011088", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorRemovingTimer(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011089", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011089";
   }

   public static Loggable logErrorRemovingTimerLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011089", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExceptionBeforeInvokingEJBTimeout(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011090", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011090";
   }

   public static Loggable logExceptionBeforeInvokingEJBTimeoutLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011090", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logTableUsesTriggerCannotBeDroppedOrCreated(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011091", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011091";
   }

   public static Loggable logTableUsesTriggerCannotBeDroppedOrCreatedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011091", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logSequenceCannotBeAlteredInProductionMode(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011092", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011092";
   }

   public static Loggable logSequenceCannotBeAlteredInProductionModeLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011092", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logSequenceNotExist(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011093", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011093";
   }

   public static Loggable logSequenceNotExistLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011093", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logSequenceIncrementMismatch(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011094", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011094";
   }

   public static Loggable logSequenceIncrementMismatchLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011094", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFailedToCreateSequence(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011095", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011095";
   }

   public static Loggable logFailedToCreateSequenceLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("011095", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logSequenceSetupFailure(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011096", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011096";
   }

   public static Loggable logSequenceSetupFailureLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("011096", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFailedToAlterSequence(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011097", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011097";
   }

   public static Loggable logFailedToAlterSequenceLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("011097", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logNoGeneratedPKReturned() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011098", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011098";
   }

   public static Loggable logNoGeneratedPKReturnedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011098", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logMultiplGeneratedKeysReturned() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011099", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011099";
   }

   public static Loggable logMultiplGeneratedKeysReturnedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011099", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logGenKeySequenceTableSetupFailure(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011100", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011100";
   }

   public static Loggable logGenKeySequenceTableSetupFailureLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011100", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logGenKeySequenceTableEmpty(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011101", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011101";
   }

   public static Loggable logGenKeySequenceTableEmptyLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011101", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logGenKeySequenceTableNewTxFailure(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011102", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011102";
   }

   public static Loggable logGenKeySequenceTableNewTxFailureLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011102", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logGenKeySequenceTableUpdateFailure(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011103", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011103";
   }

   public static Loggable logGenKeySequenceTableUpdateFailureLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011103", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logGenKeySequenceTableLocalCommitFailure(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011104", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011104";
   }

   public static Loggable logGenKeySequenceTableLocalCommitFailureLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011104", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logGenKeySequenceTableTxResumeFailure(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011105", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011105";
   }

   public static Loggable logGenKeySequenceTableTxResumeFailureLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011105", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logCannotCallSetOnDBMSDefaultFieldBeforeInsert() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011106", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011106";
   }

   public static Loggable logCannotCallSetOnDBMSDefaultFieldBeforeInsertLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011106", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logCannotInvokeTimerObjectsFromEjbCreate() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011107", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011107";
   }

   public static Loggable logCannotInvokeTimerObjectsFromEjbCreateLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011107", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logCannotInvokeTimerObjectsFromAfterCompletion() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011108", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011108";
   }

   public static Loggable logCannotInvokeTimerObjectsFromAfterCompletionLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011108", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFailedToRegisterPolicyContextHandlers(Exception var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011109", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011109";
   }

   public static Loggable logFailedToRegisterPolicyContextHandlersLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011109", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logWarningSequenceIncrementLesserThanDBIncrement(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011110", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011110";
   }

   public static Loggable logWarningSequenceIncrementLesserThanDBIncrementLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011110", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorSequenceIncrementGreaterThanDBIncrement(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011111", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011111";
   }

   public static Loggable logErrorSequenceIncrementGreaterThanDBIncrementLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011111", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logUnableToFindPersistentStore(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011112", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011112";
   }

   public static Loggable logUnableToFindPersistentStoreLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("011112", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logNoMdbDestinationConfigured(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011113", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011113";
   }

   public static Loggable logNoMdbDestinationConfiguredLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011113", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logNoPlanOverridesWithDTDDescriptors(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011114", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011114";
   }

   public static Loggable logNoPlanOverridesWithDTDDescriptorsLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011114", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logCannotCallSetForReadOnlyBean(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011115", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011115";
   }

   public static Loggable logCannotCallSetForReadOnlyBeanLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011115", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logPersistenceTypeSetupEjbqlParsingError(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011116", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011116";
   }

   public static Loggable logPersistenceTypeSetupEjbqlParsingErrorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011116", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logAttemptToBumpUpPrivilegesWithRunAs(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011117", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011117";
   }

   public static Loggable logAttemptToBumpUpPrivilegesWithRunAsLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011117", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logRunAsPrincipalNotFound(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011118", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011118";
   }

   public static Loggable logRunAsPrincipalNotFoundLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("011118", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logoptimisticColumnIsNull(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011119", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011119";
   }

   public static Loggable logoptimisticColumnIsNullLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("011119", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logBatchingTurnedOff(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011120", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011120";
   }

   public static Loggable logBatchingTurnedOffLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011120", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logPluginClassNotFound(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011121", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011121";
   }

   public static Loggable logPluginClassNotFoundLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011121", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logPluginClassInstantiationError(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011122", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011122";
   }

   public static Loggable logPluginClassInstantiationErrorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011122", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logPluginClassIllegalAccess(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011123", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011123";
   }

   public static Loggable logPluginClassIllegalAccessLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011123", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logPluginClassNotImplment(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011124", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011124";
   }

   public static Loggable logPluginClassNotImplmentLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011124", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorRemovingEJBTimersFromStore(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011125", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011125";
   }

   public static Loggable logErrorRemovingEJBTimersFromStoreLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011125", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logSuppressingEJBTimeoutErrors(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011126", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011126";
   }

   public static Loggable logSuppressingEJBTimeoutErrorsLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011126", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logEJBTimerSerializationError(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011127", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011127";
   }

   public static Loggable logEJBTimerSerializationErrorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011127", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorInstantiatingBeanInstance(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011128", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011128";
   }

   public static Loggable logErrorInstantiatingBeanInstanceLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011128", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logClusteredTimersRequireCluster(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011129", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011129";
   }

   public static Loggable logClusteredTimersRequireClusterLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011129", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logJobSchedulerNotConfiguredForClusteredTimers(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011130", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011130";
   }

   public static Loggable logJobSchedulerNotConfiguredForClusteredTimersLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011130", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorBindingBusinessInterfaceToJNDI(String var0, String var1, Exception var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011132", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011132";
   }

   public static Loggable logErrorBindingBusinessInterfaceToJNDILoggable(String var0, String var1, Exception var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("011132", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logBusinessJNDIRebindFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011133", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011133";
   }

   public static Loggable logBusinessJNDIRebindFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011133", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logLocalBusinessJNDIRebindFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011134", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011134";
   }

   public static Loggable logLocalBusinessJNDIRebindFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011134", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logMethodNotFoundInInterface(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("012000", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "012000";
   }

   public static Loggable logMethodNotFoundInInterfaceLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("012000", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logBadAutoKeyGeneratorName(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("012001", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "012001";
   }

   public static Loggable logBadAutoKeyGeneratorNameLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("012001", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logAutoKeyCannotBePartOfFK() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("012004", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "012004";
   }

   public static Loggable logAutoKeyCannotBePartOfFKLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("012004", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logUnableToLoadClass(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("012005", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "012005";
   }

   public static Loggable logUnableToLoadClassLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("012005", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFieldNotFoundInClass(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("012006", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "012006";
   }

   public static Loggable logFieldNotFoundInClassLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("012006", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logCouldNotGenerateFinder(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("012007", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "012007";
   }

   public static Loggable logCouldNotGenerateFinderLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("012007", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logNullFinder(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("012008", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "012008";
   }

   public static Loggable logNullFinderLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("012008", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logNoCMRFieldForRemoteRelationship(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("012009", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "012009";
   }

   public static Loggable logNoCMRFieldForRemoteRelationshipLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("012009", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logNoRemoteHome(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("012012", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "012012";
   }

   public static Loggable logNoRemoteHomeLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("012012", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logMethodHasWrongParamCount(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("012013", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "012013";
   }

   public static Loggable logMethodHasWrongParamCountLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("012013", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logRemoteFinderNameNull(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("012014", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "012014";
   }

   public static Loggable logRemoteFinderNameNullLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("012014", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logGotNullXForFinder(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("012015", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "012015";
   }

   public static Loggable logGotNullXForFinderLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("012015", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logGotNullBeanFromBeanMap(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("012016", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "012016";
   }

   public static Loggable logGotNullBeanFromBeanMapLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("012016", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorWhileGenerating(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("012017", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "012017";
   }

   public static Loggable logErrorWhileGeneratingLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("012017", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logCouldNotProduceProductionRule(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("012019", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "012019";
   }

   public static Loggable logCouldNotProduceProductionRuleLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("012019", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logCouldNotGetClassForParam(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("012020", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "012020";
   }

   public static Loggable logCouldNotGetClassForParamLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("012020", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logpersistentTypeMissing(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("012021", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "012021";
   }

   public static Loggable logpersistentTypeMissingLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("012021", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logmismatchBetweenEJBNames(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("012022", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "012022";
   }

   public static Loggable logmismatchBetweenEJBNamesLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("012022", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logmismatchBetweenslsbEJBNames(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("012023", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "012023";
   }

   public static Loggable logmismatchBetweenslsbEJBNamesLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("012023", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logmismatchBetweensfsbEJBNames(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("012024", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "012024";
   }

   public static Loggable logmismatchBetweensfsbEJBNamesLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("012024", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logmismatchBetweenmdbEJBNames(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("012025", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "012025";
   }

   public static Loggable logmismatchBetweenmdbEJBNamesLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("012025", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logincorrectXMLFileVersion(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("012029", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "012029";
   }

   public static Loggable logincorrectXMLFileVersionLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("012029", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logincorrectDocType(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("012030", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "012030";
   }

   public static Loggable logincorrectDocTypeLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("012030", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logSqlSelectDistinctDeprecated(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("012031", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "012031";
   }

   public static Loggable logSqlSelectDistinctDeprecatedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("012031", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logWarningWeblogicQueryHasNoMatchingEjbQuery(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("012032", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "012032";
   }

   public static Loggable logWarningWeblogicQueryHasNoMatchingEjbQueryLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("012032", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logJavaCompilerOutput(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("012033", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "012033";
   }

   public static Loggable logJavaCompilerOutputLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("012033", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logMissingMessageDestinationDescriptor(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("012034", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "012034";
   }

   public static Loggable logMissingMessageDestinationDescriptorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("012034", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logWarningParameterIsNotSerializable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("012035", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "012035";
   }

   public static Loggable logWarningParameterIsNotSerializableLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("012035", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logJavaCompilerErrorOutput(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("012036", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "012036";
   }

   public static Loggable logJavaCompilerErrorOutputLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("012036", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFinderDoesNotReturnBean(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013000", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013000";
   }

   public static Loggable logFinderDoesNotReturnBeanLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("013000", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFinderReturnsBeanOfWrongType(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013001", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013001";
   }

   public static Loggable logFinderReturnsBeanOfWrongTypeLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("013001", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExpressionWrongNumberOfTerms(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013002", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013002";
   }

   public static Loggable logExpressionWrongNumberOfTermsLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("013002", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExpressionRequiresX(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013003", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013003";
   }

   public static Loggable logExpressionRequiresXLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("013003", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFinderParamsMustBeGTOne(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013004", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013004";
   }

   public static Loggable logFinderParamsMustBeGTOneLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("013004", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFinderParamMissing(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013005", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013005";
   }

   public static Loggable logFinderParamMissingLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("013005", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFinderCouldNotGetClassForIdBean(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013006", var4, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013006";
   }

   public static Loggable logFinderCouldNotGetClassForIdBeanLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("013006", var4, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFinderCouldNotGetXForY(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013007", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013007";
   }

   public static Loggable logFinderCouldNotGetXForYLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("013007", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFinderSelectWrongBean(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013008", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013008";
   }

   public static Loggable logFinderSelectWrongBeanLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("013008", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFinderSelectTargetNoJoinNode(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013009", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013009";
   }

   public static Loggable logFinderSelectTargetNoJoinNodeLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013009", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logSelectClauseRequired() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013010", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013010";
   }

   public static Loggable logSelectClauseRequiredLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("013010", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFromClauseRequired() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013011", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013011";
   }

   public static Loggable logFromClauseRequiredLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("013011", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFinderRVDCannotBePathExpression(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013012", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013012";
   }

   public static Loggable logFinderRVDCannotBePathExpressionLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013012", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFinderCouldNotGetAbstractSchemaNameForRVD(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013013", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013013";
   }

   public static Loggable logFinderCouldNotGetAbstractSchemaNameForRVDLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013013", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFinderCouldNotGetAbstractSchemaNameForBean(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013014", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013014";
   }

   public static Loggable logFinderCouldNotGetAbstractSchemaNameForBeanLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013014", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFinderCouldNotGetRDBMSBeanForAbstractSchemaName(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013015", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013015";
   }

   public static Loggable logFinderCouldNotGetRDBMSBeanForAbstractSchemaNameLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013015", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFinderCouldNotGetLastJoinNode(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013017", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013017";
   }

   public static Loggable logFinderCouldNotGetLastJoinNodeLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("013017", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFinderExpectedSingleFK(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013018", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013018";
   }

   public static Loggable logFinderExpectedSingleFKLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("013018", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFinderNotNullOnWrongType(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013019", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013019";
   }

   public static Loggable logFinderNotNullOnWrongTypeLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("013019", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFinderNotNullOnBadPath(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013020", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013020";
   }

   public static Loggable logFinderNotNullOnBadPathLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013020", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logFinderCouldNotFindCMRPointingToBean(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013021", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013021";
   }

   public static Loggable logFinderCouldNotFindCMRPointingToBeanLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("013021", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logfinderFKColumnsMissing(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013022", var4, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013022";
   }

   public static Loggable logfinderFKColumnsMissingLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("013022", var4, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logfinderCMRFieldNotFK(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013023", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013023";
   }

   public static Loggable logfinderCMRFieldNotFKLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("013023", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logfinderCouldNotGetFKColumns(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013024", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013024";
   }

   public static Loggable logfinderCouldNotGetFKColumnsLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("013024", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logfinderArgMustBeCollectionValued(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013025", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013025";
   }

   public static Loggable logfinderArgMustBeCollectionValuedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013025", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logfinderCouldNotGetJoinTable() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013026", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013026";
   }

   public static Loggable logfinderCouldNotGetJoinTableLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("013026", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logfinderCouldNotGetFKTable() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013027", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013027";
   }

   public static Loggable logfinderCouldNotGetFKTableLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("013027", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logfinderMemberLHSWrongType(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013029", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013029";
   }

   public static Loggable logfinderMemberLHSWrongTypeLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013029", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logfinderMemberRHSWrongType(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013031", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013031";
   }

   public static Loggable logfinderMemberRHSWrongTypeLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013031", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logfinderNoPKClassForField(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013032", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013032";
   }

   public static Loggable logfinderNoPKClassForFieldLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013032", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logfinderMemberMismatch(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013033", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013033";
   }

   public static Loggable logfinderMemberMismatchLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("013033", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logfinderInvalidBooleanLiteral() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013035", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013035";
   }

   public static Loggable logfinderInvalidBooleanLiteralLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("013035", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logfinderCouldNotGetTableAndField(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013036", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013036";
   }

   public static Loggable logfinderCouldNotGetTableAndFieldLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013036", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logfinderInvalidStringExpression() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013037", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013037";
   }

   public static Loggable logfinderInvalidStringExpressionLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("013037", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logfinderInvalidArithExpression() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013038", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013038";
   }

   public static Loggable logfinderInvalidArithExpressionLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("013038", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logfinderTerminalCMRNotRemote(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013039", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013039";
   }

   public static Loggable logfinderTerminalCMRNotRemoteLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("013039", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logfinderPathEndsInXNotY(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013040", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013040";
   }

   public static Loggable logfinderPathEndsInXNotYLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("013040", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logejbqlArgNotACmpField(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013041", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013041";
   }

   public static Loggable logejbqlArgNotACmpFieldLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("013041", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logejbqlIdNotFieldAndNotBean(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013042", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013042";
   }

   public static Loggable logejbqlIdNotFieldAndNotBeanLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013042", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logejbqlNoTokenSpecial(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013043", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013043";
   }

   public static Loggable logejbqlNoTokenSpecialLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013043", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logejbqlCanOnlyTestBeanVsSameBeanType(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013045", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013045";
   }

   public static Loggable logejbqlCanOnlyTestBeanVsSameBeanTypeLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013045", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logejbqlSubQuerySelectCanOnlyHaveOneItem() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013046", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013046";
   }

   public static Loggable logejbqlSubQuerySelectCanOnlyHaveOneItemLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("013046", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logejbqlOrderByIsDifferent() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013047", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013047";
   }

   public static Loggable logejbqlOrderByIsDifferentLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("013047", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logejbqlSubQueryBeansCannotTestVariables() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013048", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013048";
   }

   public static Loggable logejbqlSubQueryBeansCannotTestVariablesLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("013048", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logejbqlWrongBeanTestedAgainstVariable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013049", var4, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013049";
   }

   public static Loggable logejbqlWrongBeanTestedAgainstVariableLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("013049", var4, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logejbqlSubQueryMissingClause(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013050", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013050";
   }

   public static Loggable logejbqlSubQueryMissingClauseLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013050", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logejbqlMissingRangeVariable(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013051", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013051";
   }

   public static Loggable logejbqlMissingRangeVariableLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013051", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logejbqlMissingRangeVariableDeclaration(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013052", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013052";
   }

   public static Loggable logejbqlMissingRangeVariableDeclarationLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013052", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logejbqlArgMustBeIDorINT(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013053", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013053";
   }

   public static Loggable logejbqlArgMustBeIDorINTLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("013053", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logejbqlClauseNotAllowedInResultSetQueriesReturningBeans(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013054", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013054";
   }

   public static Loggable logejbqlClauseNotAllowedInResultSetQueriesReturningBeansLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013054", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logejbqlResultSetFinderCannotSelectBeans(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013055", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013055";
   }

   public static Loggable logejbqlResultSetFinderCannotSelectBeansLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("013055", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logejbqlSelectObjectMustBeRangeOrCollectionId(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013057", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013057";
   }

   public static Loggable logejbqlSelectObjectMustBeRangeOrCollectionIdLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013057", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logejbqlSelectObjectMustBeIdentificationVarNotCMPField(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013059", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013059";
   }

   public static Loggable logejbqlSelectObjectMustBeIdentificationVarNotCMPFieldLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013059", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logejbqlSELECTmustUseOBJECTargument(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013061", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013061";
   }

   public static Loggable logejbqlSELECTmustUseOBJECTargumentLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013061", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logejbqlSubQueryBeansCanOnlyHaveSimplePKs(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013062", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013062";
   }

   public static Loggable logejbqlSubQueryBeansCanOnlyHaveSimplePKsLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013062", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logNotMemberOfLHSNotInSelect(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013064", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013064";
   }

   public static Loggable logNotMemberOfLHSNotInSelectLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013064", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logCannotDoOuterJoinForUnspecifiedDB() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013065", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013065";
   }

   public static Loggable logCannotDoOuterJoinForUnspecifiedDBLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("013065", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logCannotDoOuterJoinForDB(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013066", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013066";
   }

   public static Loggable logCannotDoOuterJoinForDBLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("013066", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logCannotDoMultiOuterJoinForDB(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013067", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013067";
   }

   public static Loggable logCannotDoMultiOuterJoinForDBLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("013067", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logCannotDoNOuterJoinForDB(int var0, String var1) {
      Object[] var2 = new Object[]{new Integer(var0), var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013068", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013068";
   }

   public static Loggable logCannotDoNOuterJoinForDBLoggable(int var0, String var1) {
      Object[] var2 = new Object[]{new Integer(var0), var1};
      return new Loggable("013068", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logOracleCannotDoOuterJoinAndOR(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013069", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013069";
   }

   public static Loggable logOracleCannotDoOuterJoinAndORLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013069", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logmustUseTwoPhaseDeployment(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013070", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013070";
   }

   public static Loggable logmustUseTwoPhaseDeploymentLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("013070", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logfinderNotFound11Message(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013071", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013071";
   }

   public static Loggable logfinderNotFound11MessageLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("013071", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logshouldNotDefineJoinTableForOneToMany(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013072", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013072";
   }

   public static Loggable logshouldNotDefineJoinTableForOneToManyLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("013072", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logfinderReturnedMultipleValues(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013073", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013073";
   }

   public static Loggable logfinderReturnedMultipleValuesLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013073", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logduplicateAsDefinition(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013074", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013074";
   }

   public static Loggable logduplicateAsDefinitionLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013074", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logduplicateRangeVariableDefinition(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013075", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013075";
   }

   public static Loggable logduplicateRangeVariableDefinitionLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013075", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String lograngeVariableNotFound(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013076", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013076";
   }

   public static Loggable lograngeVariableNotFoundLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("013076", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logduplicateCollectionMemberDefinition(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013077", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013077";
   }

   public static Loggable logduplicateCollectionMemberDefinitionLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013077", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logcorrelationVarDefinedMultipleTimes(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013078", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013078";
   }

   public static Loggable logcorrelationVarDefinedMultipleTimesLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013078", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logidNotDefinedInAsDeclaration(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013079", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013079";
   }

   public static Loggable logidNotDefinedInAsDeclarationLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("013079", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logpathExpressionNotInContextOfQueryTree(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013080", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013080";
   }

   public static Loggable logpathExpressionNotInContextOfQueryTreeLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("013080", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logOrMayYieldEmptyCrossProduct(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013081", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013081";
   }

   public static Loggable logOrMayYieldEmptyCrossProductLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013081", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logISNULLArgMustBePathExpressionOrVariable(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013082", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013082";
   }

   public static Loggable logISNULLArgMustBePathExpressionOrVariableLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013082", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logselectForUpdateSpecifiedWithOrderBy(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013084", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013084";
   }

   public static Loggable logselectForUpdateSpecifiedWithOrderByLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("013084", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logSelectMultipleFieldsButReturnCollection(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013085", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013085";
   }

   public static Loggable logSelectMultipleFieldsButReturnCollectionLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013085", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logLIKEmissingArgument() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013086", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013086";
   }

   public static Loggable logLIKEmissingArgumentLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("013086", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logInvalidStartCharacterForEJBQLIdentifier(char var0, String var1) {
      Object[] var2 = new Object[]{new Character(var0), var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013087", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013087";
   }

   public static Loggable logInvalidStartCharacterForEJBQLIdentifierLoggable(char var0, String var1) {
      Object[] var2 = new Object[]{new Character(var0), var1};
      return new Loggable("013087", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logInvalidPartCharacterForEJBQLIdentifier(char var0, String var1) {
      Object[] var2 = new Object[]{new Character(var0), var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013088", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013088";
   }

   public static Loggable logInvalidPartCharacterForEJBQLIdentifierLoggable(char var0, String var1) {
      Object[] var2 = new Object[]{new Character(var0), var1};
      return new Loggable("013088", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logEJBQLCharAllowedForBackwardsCompatibility(char var0, String var1) {
      Object[] var2 = new Object[]{new Character(var0), var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013089", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013089";
   }

   public static Loggable logEJBQLCharAllowedForBackwardsCompatibilityLoggable(char var0, String var1) {
      Object[] var2 = new Object[]{new Character(var0), var1};
      return new Loggable("013089", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logejbqlSelectCaseMustBePathExpression(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013090", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013090";
   }

   public static Loggable logejbqlSelectCaseMustBePathExpressionLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("013090", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logAggregateFunctionMustHaveCMPFieldArg(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013091", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013091";
   }

   public static Loggable logAggregateFunctionMustHaveCMPFieldArgLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("013091", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logEjbqlHasBeenRewritten(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013092", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013092";
   }

   public static Loggable logEjbqlHasBeenRewrittenLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013092", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logEJBQL_REWRITE_REASON_FACTOR_OUT_NOT_TEXT() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013093", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013093";
   }

   public static Loggable logEJBQL_REWRITE_REASON_FACTOR_OUT_NOT_TEXTLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("013093", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logMayNotComplyWithEJB21_11_2_7_1_mustReturnAnyNullBeans(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013094", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013094";
   }

   public static Loggable logMayNotComplyWithEJB21_11_2_7_1_mustReturnAnyNullBeansLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013094", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logInvalidRelationshipCachingName(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013095", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013095";
   }

   public static Loggable logInvalidRelationshipCachingNameLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013095", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logInvalidEJBQLSELECTExpression(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("013096", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "013096";
   }

   public static Loggable logInvalidEJBQLSELECTExpressionLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("013096", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logIllegalValueForTransactionIsolation(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("014000", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "014000";
   }

   public static Loggable logIllegalValueForTransactionIsolationLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("014000", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logIsolationLevelSetInRDBMSDescriptor(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("014001", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "014001";
   }

   public static Loggable logIsolationLevelSetInRDBMSDescriptorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("014001", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logWarningOptimisticBeanUsesIncludeUpdate(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("014003", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "014003";
   }

   public static Loggable logWarningOptimisticBeanUsesIncludeUpdateLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("014003", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logWarningBatchOperationOffForAutoKeyGen(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("014004", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "014004";
   }

   public static Loggable logWarningBatchOperationOffForAutoKeyGenLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("014004", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logMDBDispatchPolicyIgnored(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("014005", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "014005";
   }

   public static Loggable logMDBDispatchPolicyIgnoredLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("014005", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logMDBUnknownDispatchPolicy(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("014006", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "014006";
   }

   public static Loggable logMDBUnknownDispatchPolicyLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("014006", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logWarningOptimisticBlobBeanHasNoVersionTimestamp(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("014007", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "014007";
   }

   public static Loggable logWarningOptimisticBlobBeanHasNoVersionTimestampLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("014007", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logSqlShapeDoesNotExist(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("014008", var4, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "014008";
   }

   public static Loggable logSqlShapeDoesNotExistLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("014008", var4, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logSuspendMDB(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("014009", var4, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "014009";
   }

   public static Loggable logSuspendMDBLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("014009", var4, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logSuspendNonDurableSubscriber(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("014010", var4, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "014010";
   }

   public static Loggable logSuspendNonDurableSubscriberLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("014010", var4, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logWarningOptimisticBeanUsesUseSelectForUpdate(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("014011", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "014011";
   }

   public static Loggable logWarningOptimisticBeanUsesUseSelectForUpdateLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("014011", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logWarningOCBeanIsVerifyModAndNoClustInvalidate(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("014012", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "014012";
   }

   public static Loggable logWarningOCBeanIsVerifyModAndNoClustInvalidateLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("014012", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logWarningNonOCOrROBeanDisablesClustInvalidate(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("014013", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "014013";
   }

   public static Loggable logWarningNonOCOrROBeanDisablesClustInvalidateLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("014013", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logMDBUnknownDispatchPolicyWM(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("014014", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "014014";
   }

   public static Loggable logMDBUnknownDispatchPolicyWMLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("014014", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logWarningReadOnlyBeanUsesUseSelectForUpdate(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("014016", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "014016";
   }

   public static Loggable logWarningReadOnlyBeanUsesUseSelectForUpdateLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("014016", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logWarningExclusiveBeanUsesUseSelectForUpdate(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("014017", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "014017";
   }

   public static Loggable logWarningExclusiveBeanUsesUseSelectForUpdateLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("014017", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logWarningNonOptimisticBeanUsesVerifyColumns(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("014018", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "014018";
   }

   public static Loggable logWarningNonOptimisticBeanUsesVerifyColumnsLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("014018", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logWarningNonOptimisticBeanUsesVerifyRows(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("014019", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "014019";
   }

   public static Loggable logWarningNonOptimisticBeanUsesVerifyRowsLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("014019", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logWarningNonOptimisticBeanUsesOptimisticColumn(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("014020", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "014020";
   }

   public static Loggable logWarningNonOptimisticBeanUsesOptimisticColumnLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("014020", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logDeployedWithEJBName(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("014021", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "014021";
   }

   public static Loggable logDeployedWithEJBNameLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("014021", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logJNDINamesMap(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("014022", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "014022";
   }

   public static Loggable logJNDINamesMapLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("014022", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logInvokeSatefulCallbackError(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011223", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011223";
   }

   public static Loggable logInvokeSatefulCallbackErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011223", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logAlreadyBindInterfaceWithName(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011224", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011224";
   }

   public static Loggable logAlreadyBindInterfaceWithNameLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011224", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logAnotherInterfaceBindWithName(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011225", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011225";
   }

   public static Loggable logAnotherInterfaceBindWithNameLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011225", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logDuringFindCannotGetConnection(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011226", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011226";
   }

   public static Loggable logDuringFindCannotGetConnectionLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("011226", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExceptionWhilePrepareingQuery(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011227", var4, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011227";
   }

   public static Loggable logExceptionWhilePrepareingQueryLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("011227", var4, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorSetQueryParametor(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011232", var4, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011232";
   }

   public static Loggable logErrorSetQueryParametorLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("011232", var4, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorExecuteQuery(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011233", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011233";
   }

   public static Loggable logErrorExecuteQueryLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("011233", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorMapColumn(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011234", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011234";
   }

   public static Loggable logErrorMapColumnLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("011234", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorMapRelatioship(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011235", var5, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011235";
   }

   public static Loggable logErrorMapRelatioshipLoggable(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return new Loggable("011235", var5, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorExecuteFinder(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011236", var4, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011236";
   }

   public static Loggable logErrorExecuteFinderLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("011236", var4, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logNoSqlShapeSpecified(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011237", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011237";
   }

   public static Loggable logNoSqlShapeSpecifiedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011237", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logNotSelectForAllPrimaryKey(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011238", var4, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011238";
   }

   public static Loggable logNotSelectForAllPrimaryKeyLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("011238", var4, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logQueryCacheNotSupportReadWriteBean() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011239", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011239";
   }

   public static Loggable logQueryCacheNotSupportReadWriteBeanLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011239", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorObtainNativeQuery(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011240", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011240";
   }

   public static Loggable logErrorObtainNativeQueryLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011240", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logNonWeblogicEntityManagerExecuteQuery(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011241", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011241";
   }

   public static Loggable logNonWeblogicEntityManagerExecuteQueryLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011241", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExtendedPersistenceContextClosed() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011242", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011242";
   }

   public static Loggable logExtendedPersistenceContextClosedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011242", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logIllegalStateTransaction(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011243", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011243";
   }

   public static Loggable logIllegalStateTransactionLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011243", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logIllegalCallEJBContextMethod() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011244", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011244";
   }

   public static Loggable logIllegalCallEJBContextMethodLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011244", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logBeanIsNotEJB3Bean(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011245", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011245";
   }

   public static Loggable logBeanIsNotEJB3BeanLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011245", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logEjbBeanWithoutHomeInterface(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011246", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011246";
   }

   public static Loggable logEjbBeanWithoutHomeInterfaceLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011246", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logEjbNoImplementBusinessInterface(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011247", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011247";
   }

   public static Loggable logEjbNoImplementBusinessInterfaceLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("011247", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logBeanNotInvokedThroughBusinessInterface() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011248", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011248";
   }

   public static Loggable logBeanNotInvokedThroughBusinessInterfaceLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011248", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logMDBIllegalInvokeUserTransactionMethodInEjbCreateOrPostConstruct() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011249", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011249";
   }

   public static Loggable logMDBIllegalInvokeUserTransactionMethodInEjbCreateOrPostConstructLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011249", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logMDBIllegalInvokeUserTransactionMethodInEjbRemoveOrPreDestroy() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011250", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011250";
   }

   public static Loggable logMDBIllegalInvokeUserTransactionMethodInEjbRemoveOrPreDestroyLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011250", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logMDBIllegalInvokeUserTransactionMethodInSetSessionContextOrDI() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011251", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011251";
   }

   public static Loggable logMDBIllegalInvokeUserTransactionMethodInSetSessionContextOrDILoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011251", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logSFSBIllegalInvokeUserTransactionMethodInSetSessionContextOrDI() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011252", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011252";
   }

   public static Loggable logSFSBIllegalInvokeUserTransactionMethodInSetSessionContextOrDILoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011252", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logSLSBIllegalInvokeUserTransactionMethodInEjbCreateOrPostConstruct() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011253", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011253";
   }

   public static Loggable logSLSBIllegalInvokeUserTransactionMethodInEjbCreateOrPostConstructLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011253", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logSLSBIllegalInvokeUserTransactionMethodInEjbRemoveOrPreDestroy() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011254", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011254";
   }

   public static Loggable logSLSBIllegalInvokeUserTransactionMethodInEjbRemoveOrPreDestroyLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011254", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logSLSBIllegalInvokeUserTransactionMethodInSetSessionContextOrDI() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011255", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011255";
   }

   public static Loggable logSLSBIllegalInvokeUserTransactionMethodInSetSessionContextOrDILoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011255", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logSingleExpirationTimerCannotBeCancelled() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011256", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011256";
   }

   public static Loggable logSingleExpirationTimerCannotBeCancelledLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011256", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logIllegalInvokeTimerMethodInEJbRemoveOrPreDestroy() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011257", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011257";
   }

   public static Loggable logIllegalInvokeTimerMethodInEJbRemoveOrPreDestroyLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011257", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logIllegalInvokeTimerMethodInEJbRAvitvateOrPostActivate() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011258", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011258";
   }

   public static Loggable logIllegalInvokeTimerMethodInEJbRAvitvateOrPostActivateLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011258", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logIllegalInvokeTimerMethodInEjbPassivateOrPrePassivate() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011259", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011259";
   }

   public static Loggable logIllegalInvokeTimerMethodInEjbPassivateOrPrePassivateLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011259", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logIllegalInvokeTimerMethodDuringDI() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011260", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011260";
   }

   public static Loggable logIllegalInvokeTimerMethodDuringDILoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011260", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logInvovationTimeout() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011261", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011261";
   }

   public static Loggable logInvovationTimeoutLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011261", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorCacelTimer() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011262", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011262";
   }

   public static Loggable logErrorCacelTimerLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011262", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logEjBJarBeanNotSet() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011263", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011263";
   }

   public static Loggable logEjBJarBeanNotSetLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011263", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logTxNerverMethodCalledWithnInTx(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011264", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011264";
   }

   public static Loggable logTxNerverMethodCalledWithnInTxLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011264", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExceptionAferDelivery(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011265", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011265";
   }

   public static Loggable logExceptionAferDeliveryLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011265", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExecuteGetDatabaseProductnameUseNonWeblogicEntityManager() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011266", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011266";
   }

   public static Loggable logExecuteGetDatabaseProductnameUseNonWeblogicEntityManagerLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("011266", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorCallGetdatabaseProductName(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011267", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011267";
   }

   public static Loggable logErrorCallGetdatabaseProductNameLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011267", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logExecuteNativeQueryUseNonWeblogicEntitymanager(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011268", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011268";
   }

   public static Loggable logExecuteNativeQueryUseNonWeblogicEntitymanagerLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011268", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logEntityErrorObtainNativeQuery(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011269", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011269";
   }

   public static Loggable logEntityErrorObtainNativeQueryLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011269", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logErrorPrepareQuery(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011270", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011270";
   }

   public static Loggable logErrorPrepareQueryLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011270", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logIllegalMakeReentrantCallSFSB(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011276", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011276";
   }

   public static Loggable logIllegalMakeReentrantCallSFSBLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011276", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logIllegalMakeReentrantCallSFSBFromHome(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("011277", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "011277";
   }

   public static Loggable logIllegalMakeReentrantCallSFSBFromHomeLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("011277", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logUnableLinkClass(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015001", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015001";
   }

   public static Loggable logUnableLinkClassLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("015001", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logUnableLoadClass(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015002", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015002";
   }

   public static Loggable logUnableLoadClassLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("015002", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logUnableCreateJar(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015003", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015003";
   }

   public static Loggable logUnableCreateJarLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("015003", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logSessionBeanWithSessionBeanParent(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015004", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015004";
   }

   public static Loggable logSessionBeanWithSessionBeanParentLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("015004", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logSessionBeanWithoutSetSessionType(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015005", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015005";
   }

   public static Loggable logSessionBeanWithoutSetSessionTypeLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("015005", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logEjbCreateNotFoundForInitMethod(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015007", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015007";
   }

   public static Loggable logEjbCreateNotFoundForInitMethodLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("015007", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logNoMatchCreateMethodForInitMethod(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015008", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015008";
   }

   public static Loggable logNoMatchCreateMethodForInitMethodLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("015008", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logCannotLoadInterceptorClass(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015009", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015009";
   }

   public static Loggable logCannotLoadInterceptorClassLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("015009", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logMutipleMehtodPermissionMethodForMethod(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015010", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015010";
   }

   public static Loggable logMutipleMehtodPermissionMethodForMethodLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("015010", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logMutipleMehtodPermissionMethodForClass(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015011", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015011";
   }

   public static Loggable logMutipleMehtodPermissionMethodForClassLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("015011", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logUnableLoadInterfaceClass(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015012", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015012";
   }

   public static Loggable logUnableLoadInterfaceClassLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("015012", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logBeanClassNotImplementInterfaceMethod(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015013", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015013";
   }

   public static Loggable logBeanClassNotImplementInterfaceMethodLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("015013", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logCannotFoundServiceEndPointClass(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015014", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015014";
   }

   public static Loggable logCannotFoundServiceEndPointClassLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("015014", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logMDBWithMDBParent(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015015", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015015";
   }

   public static Loggable logMDBWithMDBParentLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("015015", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logNoMessageListenerSpecifiedForMDB(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015016", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015016";
   }

   public static Loggable logNoMessageListenerSpecifiedForMDBLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("015016", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logNoSetBeanInterfaceForBean(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015017", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015017";
   }

   public static Loggable logNoSetBeanInterfaceForBeanLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("015017", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logNoSetBeanInterfaceForInterceptor(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015018", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015018";
   }

   public static Loggable logNoSetBeanInterfaceForInterceptorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("015018", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logNoExecuteSFSBMethodInDifferentTx() {
      Object[] var0 = new Object[0];
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015019", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015019";
   }

   public static Loggable logNoExecuteSFSBMethodInDifferentTxLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("015019", var0, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logDuplicateJNDINameAnnotation(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015020", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015020";
   }

   public static Loggable logDuplicateJNDINameAnnotationLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("015020", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logNoJNDINameOnMultiInterfaceImpl(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015021", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015021";
   }

   public static Loggable logNoJNDINameOnMultiInterfaceImplLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("015021", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logAnnotationOnInvalidClass(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015022", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015022";
   }

   public static Loggable logAnnotationOnInvalidClassLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("015022", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logAnnotationOnInvalidMethod(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015023", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015023";
   }

   public static Loggable logAnnotationOnInvalidMethodLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("015023", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logJNDINameAnnotationOnLocalInterface(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015024", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015024";
   }

   public static Loggable logJNDINameAnnotationOnLocalInterfaceLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("015024", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logUnableLoadClassSpecifiedInDD(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015025", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015025";
   }

   public static Loggable logUnableLoadClassSpecifiedInDDLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("015025", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logRefrenceNameDuplicated(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015026", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015026";
   }

   public static Loggable logRefrenceNameDuplicatedLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("015026", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logJndiNameWasNotAXAJMSConnectionFactory(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015027", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015027";
   }

   public static Loggable logJndiNameWasNotAXAJMSConnectionFactoryLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("015027", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logIllegalDistributedDestinationConnectionValue(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015028", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015028";
   }

   public static Loggable logIllegalDistributedDestinationConnectionValueLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("015028", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logIllegalTopicMessagesDistributionModeValue(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015029", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015029";
   }

   public static Loggable logIllegalTopicMessagesDistributionModeValueLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("015029", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logIllegalPermutationOnPDTAndComp(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015030", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015030";
   }

   public static Loggable logIllegalPermutationOnPDTAndCompLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("015030", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logInvalidConfigurationForDistributionConnection(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015031", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015031";
   }

   public static Loggable logInvalidConfigurationForDistributionConnectionLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("015031", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logInvalidConfigurationForTopicMessagesDistributionMode(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015032", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015032";
   }

   public static Loggable logInvalidConfigurationForTopicMessagesDistributionModeLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("015032", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logInvalidConfigurationForPre1033(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015033", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015033";
   }

   public static Loggable logInvalidConfigurationForPre1033Loggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("015033", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logIllegalSubscriptionOnDurRemoteRDT(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015034", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015034";
   }

   public static Loggable logIllegalSubscriptionOnDurRemoteRDTLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("015034", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logOverridenLocalOnlyWithEveryMember(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015035", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015035";
   }

   public static Loggable logOverridenLocalOnlyWithEveryMemberLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("015035", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logOverridenActivationConfigProperty(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015036", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015036";
   }

   public static Loggable logOverridenActivationConfigPropertyLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("015036", var3, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logComplianceWarning(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015037", var4, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015037";
   }

   public static Loggable logComplianceWarningLoggable(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("015037", var4, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logMayBeMissingBridgeMethod(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015038", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015038";
   }

   public static Loggable logMayBeMissingBridgeMethodLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("015038", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logInactiveMDBStartFail(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015039", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015039";
   }

   public static Loggable logInactiveMDBStartFailLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("015039", var2, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logMDBInactive(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015040", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015040";
   }

   public static Loggable logMDBInactiveLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("015040", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   public static String logMDBActive(String var0) {
      Object[] var1 = new Object[]{var0};
      EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("015041", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.class.getClassLoader()));
      return "015041";
   }

   public static Loggable logMDBActiveLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("015041", var1, "weblogic.ejb.container.EJBLogLocalizer", EJBLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EJBLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = EJBLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = EJBLogger.findMessageLogger();
      }
   }
}
