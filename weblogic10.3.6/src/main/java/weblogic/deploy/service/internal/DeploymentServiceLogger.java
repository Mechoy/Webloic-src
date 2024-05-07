package weblogic.deploy.service.internal;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class DeploymentServiceLogger {
   private static final String LOCALIZER_CLASS = "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(DeploymentServiceLogger.class.getName());
   }

   public static String logBadContentTypeServletRequest(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290001", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290001";
   }

   public static Loggable logBadContentTypeServletRequestLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("290001", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logExceptionInServletRequest(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290003", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290003";
   }

   public static Loggable logExceptionInServletRequestLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("290003", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logRequestWithNoAppName(String var0) {
      Object[] var1 = new Object[]{var0};
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290006", var1, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290006";
   }

   public static Loggable logRequestWithNoAppNameLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("290006", var1, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logNoUploadDirectory(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290007", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290007";
   }

   public static Loggable logNoUploadDirectoryLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("290007", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logExceptionOnExtract(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290008", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290008";
   }

   public static Loggable logExceptionOnExtractLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("290008", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logServletFailedToInit() {
      Object[] var0 = new Object[0];
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290009", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290009";
   }

   public static Loggable logServletFailedToInitLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("290009", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logServletInitFailedDueToPrivilegedActionViolation(String var0) {
      Object[] var1 = new Object[]{var0};
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290010", var1, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290010";
   }

   public static Loggable logServletInitFailedDueToPrivilegedActionViolationLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("290010", var1, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logUnautherizedRequest(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290011", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290011";
   }

   public static Loggable logUnautherizedRequestLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("290011", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logNoUploadFileRequest() {
      Object[] var0 = new Object[0];
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290012", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290012";
   }

   public static Loggable logNoUploadFileRequestLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("290012", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logNoUserNameOrPassword() {
      Object[] var0 = new Object[0];
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290013", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290013";
   }

   public static Loggable logNoUserNameOrPasswordLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("290013", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logInvalidUserNameOrPassword() {
      Object[] var0 = new Object[0];
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290014", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290014";
   }

   public static Loggable logInvalidUserNameOrPasswordLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("290014", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logDomainWideSecretMismatch() {
      Object[] var0 = new Object[0];
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290015", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290015";
   }

   public static Loggable logDomainWideSecretMismatchLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("290015", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logAccessNotAllowed(String var0) {
      Object[] var1 = new Object[]{var0};
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290016", var1, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290016";
   }

   public static Loggable logAccessNotAllowedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("290016", var1, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logAppListenerException() {
      Object[] var0 = new Object[0];
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290020", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290020";
   }

   public static Loggable logAppListenerExceptionLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("290020", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logNoFile(String var0) {
      Object[] var1 = new Object[]{var0};
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290021", var1, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290021";
   }

   public static Loggable logNoFileLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("290021", var1, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logStartControl() {
      Object[] var0 = new Object[0];
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290022", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290022";
   }

   public static Loggable logStartControlLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("290022", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logCallbackAlreadyRegistered(String var0) {
      Object[] var1 = new Object[]{var0};
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290024", var1, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290024";
   }

   public static Loggable logCallbackAlreadyRegisteredLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("290024", var1, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logInvalidState() {
      Object[] var0 = new Object[0];
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290025", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290025";
   }

   public static Loggable logInvalidStateLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("290025", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logAlreadyCancelled(long var0, String var2) {
      Object[] var3 = new Object[]{new Long(var0), var2};
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290026", var3, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290026";
   }

   public static Loggable logAlreadyCancelledLoggable(long var0, String var2) {
      Object[] var3 = new Object[]{new Long(var0), var2};
      return new Loggable("290026", var3, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logTooLateToCancel(long var0, String var2) {
      Object[] var3 = new Object[]{new Long(var0), var2};
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290027", var3, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290027";
   }

   public static Loggable logTooLateToCancelLoggable(long var0, String var2) {
      Object[] var3 = new Object[]{new Long(var0), var2};
      return new Loggable("290027", var3, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logCancelled(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290028", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290028";
   }

   public static Loggable logCancelledLoggable(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return new Loggable("290028", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logRequestTimedOut(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290029", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290029";
   }

   public static Loggable logRequestTimedOutLoggable(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return new Loggable("290029", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logNoDataHandlerRegistered(String var0) {
      Object[] var1 = new Object[]{var0};
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290030", var1, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290030";
   }

   public static Loggable logNoDataHandlerRegisteredLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("290030", var1, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logNoTaskToCancel() {
      Object[] var0 = new Object[0];
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290031", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290031";
   }

   public static Loggable logNoTaskToCancelLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("290031", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logDataHandlerExists(String var0) {
      Object[] var1 = new Object[]{var0};
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290032", var1, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290032";
   }

   public static Loggable logDataHandlerExistsLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("290032", var1, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logDeferredDueToDisconnect(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290036", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290036";
   }

   public static Loggable logDeferredDueToDisconnectLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("290036", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String unrecognizedCallback() {
      Object[] var0 = new Object[0];
      return (new Loggable("290039", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable unrecognizedCallbackLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("290039", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String cancelRejected(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return (new Loggable("290040", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable cancelRejectedLoggable(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return new Loggable("290040", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String commitFailed(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return (new Loggable("290041", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable commitFailedLoggable(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return new Loggable("290041", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String commitNoRequest() {
      Object[] var0 = new Object[0];
      return (new Loggable("290042", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable commitNoRequestLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("290042", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String optimisticConcurrencyErr(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return (new Loggable("290043", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable optimisticConcurrencyErrLoggable(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return new Loggable("290043", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String operationDelivery(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("290044", var1, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable operationDeliveryLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("290044", var1, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String prepareOperation() {
      Object[] var0 = new Object[0];
      return (new Loggable("290045", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable prepareOperationLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("290045", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String commitOperation() {
      Object[] var0 = new Object[0];
      return (new Loggable("290046", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable commitOperationLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("290046", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String cancelOperation() {
      Object[] var0 = new Object[0];
      return (new Loggable("290047", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable cancelOperationLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("290047", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String timedOut(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return (new Loggable("290048", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable timedOutLoggable(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return new Loggable("290048", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String noTargets(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return (new Loggable("290049", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable noTargetsLoggable(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return new Loggable("290049", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String commitDeliveryFailure(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("290050", var1, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable commitDeliveryFailureLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("290050", var1, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String commitTimedOut(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return (new Loggable("290051", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable commitTimedOutLoggable(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return new Loggable("290051", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String operationTimeout(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("290052", var1, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable operationTimeoutLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("290052", var1, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String timedOutAdmin(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return (new Loggable("290053", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable timedOutAdminLoggable(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return new Loggable("290053", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String deploymentCancelled(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return (new Loggable("290054", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable deploymentCancelledLoggable(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return new Loggable("290054", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String timedOutDuring(long var0, String var2) {
      Object[] var3 = new Object[]{new Long(var0), var2};
      return (new Loggable("290055", var3, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable timedOutDuringLoggable(long var0, String var2) {
      Object[] var3 = new Object[]{new Long(var0), var2};
      return new Loggable("290055", var3, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String indeterminate() {
      Object[] var0 = new Object[0];
      return (new Loggable("290056", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable indeterminateLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("290056", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String incompatibleModification() {
      Object[] var0 = new Object[0];
      return (new Loggable("290057", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable incompatibleModificationLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("290057", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String noRequestToCancel(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return (new Loggable("290058", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable noRequestToCancelLoggable(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return new Loggable("290058", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String unsupportedOperation(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("290059", var1, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable unsupportedOperationLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("290059", var1, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String duplicateRegistration(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("290060", var1, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable duplicateRegistrationLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("290060", var1, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String unrecognizedTypes(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("290061", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable unrecognizedTypesLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("290061", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String transitioningServerToAdminState() {
      Object[] var0 = new Object[0];
      return (new Loggable("290062", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable transitioningServerToAdminStateLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("290062", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logCommitPendingRestart(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290063", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290063";
   }

   public static Loggable logCommitPendingRestartLoggable(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return new Loggable("290063", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logExceptionInServletRequestForDeploymentMsg(long var0, String var2, String var3) {
      Object[] var4 = new Object[]{new Long(var0), var2, var3};
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290064", var4, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290064";
   }

   public static Loggable logExceptionInServletRequestForDeploymentMsgLoggable(long var0, String var2, String var3) {
      Object[] var4 = new Object[]{new Long(var0), var2, var3};
      return new Loggable("290064", var4, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logExceptionInServletRequestForDatatransferMsg(long var0, String var2, String var3) {
      Object[] var4 = new Object[]{new Long(var0), var2, var3};
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290065", var4, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290065";
   }

   public static Loggable logExceptionInServletRequestForDatatransferMsgLoggable(long var0, String var2, String var3) {
      Object[] var4 = new Object[]{new Long(var0), var2, var3};
      return new Loggable("290065", var4, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logExceptionWhileGettingDataAsStream(long var0, String var2) {
      Object[] var3 = new Object[]{new Long(var0), var2};
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290066", var3, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290066";
   }

   public static Loggable logExceptionWhileGettingDataAsStreamLoggable(long var0, String var2) {
      Object[] var3 = new Object[]{new Long(var0), var2};
      return new Loggable("290066", var3, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String cancelledDueToClusterConstraints(long var0, String var2) {
      Object[] var3 = new Object[]{new Long(var0), var2};
      return (new Loggable("290067", var3, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable cancelledDueToClusterConstraintsLoggable(long var0, String var2) {
      Object[] var3 = new Object[]{new Long(var0), var2};
      return new Loggable("290067", var3, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logExceptionInServletRequestIntendedForAdminServer(String var0) {
      Object[] var1 = new Object[]{var0};
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290068", var1, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290068";
   }

   public static Loggable logExceptionInServletRequestIntendedForAdminServerLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("290068", var1, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String sendCommitFailMsgFailed(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return (new Loggable("290069", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable sendCommitFailMsgFailedLoggable(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return new Loggable("290069", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logExceptionOnUpload(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290070", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290070";
   }

   public static Loggable logExceptionOnUploadLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("290070", var2, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   public static String logFailedOnUploadingFile() {
      Object[] var0 = new Object[0];
      DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("290071", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.class.getClassLoader()));
      return "290071";
   }

   public static Loggable logFailedOnUploadingFileLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("290071", var0, "weblogic.deploy.service.internal.DeploymentServiceLogLocalizer", DeploymentServiceLogger.MessageLoggerInitializer.INSTANCE.messageLogger, DeploymentServiceLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = DeploymentServiceLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = DeploymentServiceLogger.findMessageLogger();
      }
   }
}
