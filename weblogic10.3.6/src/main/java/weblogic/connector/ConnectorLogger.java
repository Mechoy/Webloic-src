package weblogic.connector;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class ConnectorLogger {
   private static final String LOCALIZER_CLASS = "weblogic.connector.ConnectorLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(ConnectorLogger.class.getName());
   }

   public static String logConnectorServiceInitializing() {
      Object[] var0 = new Object[0];
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190000", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190000";
   }

   public static Loggable logConnectorServiceInitializingLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("190000", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logConnectorServiceInitialized() {
      Object[] var0 = new Object[0];
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190001", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190001";
   }

   public static Loggable logConnectorServiceInitializedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("190001", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logMCFNotFoundForJNDIName(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190004", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190004";
   }

   public static Loggable logMCFNotFoundForJNDINameLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190004", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logCreateCFforMCFError(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190005", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190005";
   }

   public static Loggable logCreateCFforMCFErrorLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("190005", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logCloseConnectionError(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190008", var4, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190008";
   }

   public static Loggable logCloseConnectionErrorLoggable(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("190008", var4, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logNoConnectionRequestInfo() {
      Object[] var0 = new Object[0];
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190009", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190009";
   }

   public static Loggable logNoConnectionRequestInfoLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("190009", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logNoResourcePrincipalFound() {
      Object[] var0 = new Object[0];
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190010", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190010";
   }

   public static Loggable logNoResourcePrincipalFoundLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("190010", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logRequestedSecurityType(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190012", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190012";
   }

   public static Loggable logRequestedSecurityTypeLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("190012", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logContextProcessingError(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190013", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190013";
   }

   public static Loggable logContextProcessingErrorLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190013", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logFindLogWriterError(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190019", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190019";
   }

   public static Loggable logFindLogWriterErrorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("190019", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logSetLogWriterError(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190020", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190020";
   }

   public static Loggable logSetLogWriterErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190020", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logInvokeMethodError(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190023", var3, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190023";
   }

   public static Loggable logInvokeMethodErrorLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("190023", var3, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logCreateInitialConnectionsError(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190024", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190024";
   }

   public static Loggable logCreateInitialConnectionsErrorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("190024", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logCreateManagedConnectionException(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190032", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190032";
   }

   public static Loggable logCreateManagedConnectionExceptionLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("190032", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logCreateManagedConnectionError(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190033", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190033";
   }

   public static Loggable logCreateManagedConnectionErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190033", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logConnectorServiceInitError(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190049", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190049";
   }

   public static Loggable logConnectorServiceInitErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190049", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logConnectorServiceShutdownError(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190050", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190050";
   }

   public static Loggable logConnectorServiceShutdownErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190050", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logUnregisterCPRTMBeanError(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190051", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190051";
   }

   public static Loggable logUnregisterCPRTMBeanErrorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("190051", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logInitCPRTMBeanError(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190052", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190052";
   }

   public static Loggable logInitCPRTMBeanErrorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("190052", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logInitConnRTMBeanError(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190053", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190053";
   }

   public static Loggable logInitConnRTMBeanErrorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("190053", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logUnregisterConnRTMBeanError(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190054", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190054";
   }

   public static Loggable logUnregisterConnRTMBeanErrorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("190054", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logRegisterXAResourceError(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190056", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190056";
   }

   public static Loggable logRegisterXAResourceErrorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("190056", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logAccessDeniedWarning(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190064", var4, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190064";
   }

   public static Loggable logAccessDeniedWarningLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("190064", var4, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logCreateCFReturnedNull(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190075", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190075";
   }

   public static Loggable logCreateCFReturnedNullLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190075", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logDeprecationReplacedWarning(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190079", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190079";
   }

   public static Loggable logDeprecationReplacedWarningLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("190079", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logDeprecationNotUsedWarning(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190080", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190080";
   }

   public static Loggable logDeprecationNotUsedWarningLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190080", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logProxyTestStarted(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190081", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190081";
   }

   public static Loggable logProxyTestStartedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190081", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logProxyTestSuccess(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190082", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190082";
   }

   public static Loggable logProxyTestSuccessLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190082", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logProxyTestError(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190084", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190084";
   }

   public static Loggable logProxyTestErrorLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("190084", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logRarMarkedForLateDeployment(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190085", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190085";
   }

   public static Loggable logRarMarkedForLateDeploymentLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190085", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logJNDINameAlreadyExists(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190088", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190088";
   }

   public static Loggable logJNDINameAlreadyExistsLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190088", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logJarFileProcessingError(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190089", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190089";
   }

   public static Loggable logJarFileProcessingErrorLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190089", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logGetLocalTransactionError(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190090", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190090";
   }

   public static Loggable logGetLocalTransactionErrorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("190090", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logGetXAResourceError(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190091", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190091";
   }

   public static Loggable logGetXAResourceErrorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("190091", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logRegisterNonXAResourceError(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190092", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190092";
   }

   public static Loggable logRegisterNonXAResourceErrorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("190092", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logProxyTestFailureInfo(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190097", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190097";
   }

   public static Loggable logProxyTestFailureInfoLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("190097", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logReReleasingResource(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190098", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190098";
   }

   public static Loggable logReReleasingResourceLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190098", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logConnectionAlreadyClosed(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190099", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190099";
   }

   public static Loggable logConnectionAlreadyClosedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190099", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logCloseNotFoundOnHandle(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190100", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190100";
   }

   public static Loggable logCloseNotFoundOnHandleLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190100", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logDeprecatedLinkref(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190101", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190101";
   }

   public static Loggable logDeprecatedLinkrefLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190101", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logStackTrace(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("199999", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "199999";
   }

   public static Loggable logStackTraceLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199999", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logStackTraceString(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("199998", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "199998";
   }

   public static Loggable logStackTraceStringLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199998", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logDiagImageUnregisterFailure(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190102", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190102";
   }

   public static Loggable logDiagImageUnregisterFailureLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190102", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logDiagImageRegisterFailure(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190103", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190103";
   }

   public static Loggable logDiagImageRegisterFailureLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190103", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logConfigPropWarning(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190104", var3, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190104";
   }

   public static Loggable logConfigPropWarningLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("190104", var3, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logGetAnonymousSubjectFailed() {
      Object[] var0 = new Object[0];
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190105", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190105";
   }

   public static Loggable logGetAnonymousSubjectFailedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("190105", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logFailedToFindModuleRuntimeMBean(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190106", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190106";
   }

   public static Loggable logFailedToFindModuleRuntimeMBeanLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190106", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logFailedToUnregisterModuleRuntimeMBean(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190107", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190107";
   }

   public static Loggable logFailedToUnregisterModuleRuntimeMBeanLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190107", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logInitJndiSubcontextsFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190108", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190108";
   }

   public static Loggable logInitJndiSubcontextsFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("190108", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logExtractingNativeLib(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190109", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190109";
   }

   public static Loggable logExtractingNativeLibLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("190109", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logTimerWarning() {
      Object[] var0 = new Object[0];
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190110", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190110";
   }

   public static Loggable logTimerWarningLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("190110", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logInvalidDye(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190111", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190111";
   }

   public static Loggable logInvalidDyeLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("190111", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logRegisterForXARecoveryFailed(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190112", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190112";
   }

   public static Loggable logRegisterForXARecoveryFailedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190112", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logUnregisterForXARecoveryFailed(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190113", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190113";
   }

   public static Loggable logUnregisterForXARecoveryFailedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190113", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logFailedToApplyPoolChanges(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190114", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190114";
   }

   public static Loggable logFailedToApplyPoolChangesLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190114", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logMCFNotImplementResourceAdapterAssociation(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190115", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190115";
   }

   public static Loggable logMCFNotImplementResourceAdapterAssociationLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190115", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logInvalidRecoveryEvent(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190116", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190116";
   }

   public static Loggable logInvalidRecoveryEventLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190116", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logCleanupFailure(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190117", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190117";
   }

   public static Loggable logCleanupFailureLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190117", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logConnectionError(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190118", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190118";
   }

   public static Loggable logConnectionErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190118", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logDestroyFailed(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190119", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190119";
   }

   public static Loggable logDestroyFailedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190119", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logNullXAResource() {
      Object[] var0 = new Object[0];
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190120", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190120";
   }

   public static Loggable logNullXAResourceLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("190120", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logDissociateHandlesFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190121", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190121";
   }

   public static Loggable logDissociateHandlesFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("190121", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logLazyEnlistNullMC() {
      Object[] var0 = new Object[0];
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190122", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190122";
   }

   public static Loggable logLazyEnlistNullMCLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("190122", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logRequestedSharingScope(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190123", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190123";
   }

   public static Loggable logRequestedSharingScopeLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("190123", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logFailedToDeployLinkRef(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190124", var3, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190124";
   }

   public static Loggable logFailedToDeployLinkRefLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("190124", var3, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logAssertionError(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190125", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190125";
   }

   public static Loggable logAssertionErrorLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("190125", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getStringAnonymousUser() {
      Object[] var0 = new Object[0];
      return (new Loggable("190126", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getStringAnonymousUserLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("190126", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getStringCloseCount() {
      Object[] var0 = new Object[0];
      return (new Loggable("190127", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getStringCloseCountLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("190127", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getStringCreateCount() {
      Object[] var0 = new Object[0];
      return (new Loggable("190128", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getStringCreateCountLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("190128", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getStringFreePoolSize() {
      Object[] var0 = new Object[0];
      return (new Loggable("190129", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getStringFreePoolSizeLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("190129", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getStringPoolSize() {
      Object[] var0 = new Object[0];
      return (new Loggable("190130", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getStringPoolSizeLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("190130", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getStringWaitingThreadCount() {
      Object[] var0 = new Object[0];
      return (new Loggable("190131", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getStringWaitingThreadCountLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("190131", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getStringCreateCountDescription() {
      Object[] var0 = new Object[0];
      return (new Loggable("190133", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getStringCreateCountDescriptionLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("190133", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getStringFreePoolSizeDescription() {
      Object[] var0 = new Object[0];
      return (new Loggable("190134", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getStringFreePoolSizeDescriptionLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("190134", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getStringPoolSizeDescription() {
      Object[] var0 = new Object[0];
      return (new Loggable("190135", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getStringPoolSizeDescriptionLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("190135", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getStringWaitingThreadCountDescription() {
      Object[] var0 = new Object[0];
      return (new Loggable("190136", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getStringWaitingThreadCountDescriptionLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("190136", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getStringNever() {
      Object[] var0 = new Object[0];
      return (new Loggable("190137", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getStringNeverLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("190137", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getStringUnavailable() {
      Object[] var0 = new Object[0];
      return (new Loggable("190138", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getStringUnavailableLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("190138", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getStringRunning() {
      Object[] var0 = new Object[0];
      return (new Loggable("190139", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getStringRunningLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("190139", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getStringSuspended() {
      Object[] var0 = new Object[0];
      return (new Loggable("190140", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getStringSuspendedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("190140", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getStringNew() {
      Object[] var0 = new Object[0];
      return (new Loggable("190141", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getStringNewLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("190141", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getStringInitialized() {
      Object[] var0 = new Object[0];
      return (new Loggable("190142", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getStringInitializedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("190142", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getStringPrepared() {
      Object[] var0 = new Object[0];
      return (new Loggable("190143", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getStringPreparedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("190143", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getStringActivated() {
      Object[] var0 = new Object[0];
      return (new Loggable("190144", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getStringActivatedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("190144", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getStringUnknown() {
      Object[] var0 = new Object[0];
      return (new Loggable("190145", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getStringUnknownLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("190145", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logSetLogWriterErrorWithCause(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190146", var3, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190146";
   }

   public static Loggable logSetLogWriterErrorWithCauseLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("190146", var3, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logPoolProfilingRecord(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190147", var4, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190147";
   }

   public static Loggable logPoolProfilingRecordLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("190147", var4, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionInitialCapacityMustBePositive() {
      Object[] var0 = new Object[0];
      return (new Loggable("199000", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionInitialCapacityMustBePositiveLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199000", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logPropertyVetoWarning(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190148", var5, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190148";
   }

   public static Loggable logPropertyVetoWarningLoggable(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return new Loggable("190148", var5, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logNoAdapterJNDInameSetForInboundRA(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190149", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190149";
   }

   public static Loggable logNoAdapterJNDInameSetForInboundRALoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("190149", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logDiagnosticImageTimedOut() {
      Object[] var0 = new Object[0];
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190150", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190150";
   }

   public static Loggable logDiagnosticImageTimedOutLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("190150", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logBuildOutboundFailed(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190151", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190151";
   }

   public static Loggable logBuildOutboundFailedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190151", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logCreateInboundRuntimeMBeanFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190152", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190152";
   }

   public static Loggable logCreateInboundRuntimeMBeanFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("190152", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logFailedToCloseLog(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190153", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190153";
   }

   public static Loggable logFailedToCloseLogLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("190153", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logFailedToCreateLogStream(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190154", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190154";
   }

   public static Loggable logFailedToCreateLogStreamLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("190154", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logComplianceWarnings(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190155", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190155";
   }

   public static Loggable logComplianceWarningsLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("190155", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logNoComplianceErrors(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190156", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190156";
   }

   public static Loggable logNoComplianceErrorsLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190156", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logNumComplianceErrorsAndWarnings(String var0, int var1, int var2) {
      Object[] var3 = new Object[]{var0, new Integer(var1), new Integer(var2)};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190157", var3, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190157";
   }

   public static Loggable logNumComplianceErrorsAndWarningsLoggable(String var0, int var1, int var2) {
      Object[] var3 = new Object[]{var0, new Integer(var1), new Integer(var2)};
      return new Loggable("190157", var3, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logComplianceIsLinkRef(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190158", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190158";
   }

   public static Loggable logComplianceIsLinkRefLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190158", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logConnectionPoolReset(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190159", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190159";
   }

   public static Loggable logConnectionPoolResetLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190159", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logCannotResetConnectionPoolInuse(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190160", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190160";
   }

   public static Loggable logCannotResetConnectionPoolInuseLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190160", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logCannotExtractRARtoTempDir(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("190161", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "190161";
   }

   public static Loggable logCannotExtractRARtoTempDirLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("190161", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionMaxCapacityZero() {
      Object[] var0 = new Object[0];
      return (new Loggable("199001", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionMaxCapacityZeroLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199001", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionMaxCapacityNegative() {
      Object[] var0 = new Object[0];
      return (new Loggable("199002", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionMaxCapacityNegativeLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199002", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionMaxCapacityLessThanInitialCapacity(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199003", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionMaxCapacityLessThanInitialCapacityLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199003", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionMaxCapacityIncrementMustBePositive() {
      Object[] var0 = new Object[0];
      return (new Loggable("199004", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionMaxCapacityIncrementMustBePositiveLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199004", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionMaxCapacityTooHigh(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199005", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionMaxCapacityTooHighLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199005", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionShrinkFrequencySecondsMustBePositive() {
      Object[] var0 = new Object[0];
      return (new Loggable("199006", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionShrinkFrequencySecondsMustBePositiveLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199006", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionInactiveConnectionTimeoutSecondsNegative() {
      Object[] var0 = new Object[0];
      return (new Loggable("199007", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionInactiveConnectionTimeoutSecondsNegativeLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199007", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionNoDescriptorOrAltDD() {
      Object[] var0 = new Object[0];
      return (new Loggable("199010", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionNoDescriptorOrAltDDLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199010", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionNoDescriptor() {
      Object[] var0 = new Object[0];
      return (new Loggable("199011", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionNoDescriptorLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199011", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionMissingSchema() {
      Object[] var0 = new Object[0];
      return (new Loggable("199012", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionMissingSchemaLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199012", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionNoComponents(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199013", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionNoComponentsLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199013", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionMoreThanOneComponent(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199014", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionMoreThanOneComponentLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199014", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionRollbackModuleFailed(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199015", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionRollbackModuleFailedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199015", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionCreateRuntimeMBeanForConnectorModuleFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199016", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionCreateRuntimeMBeanForConnectorModuleFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199016", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionCloseVJarFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199017", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionCloseVJarFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199017", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionCreateVJarFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199018", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionCreateVJarFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199018", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionInitializeJndiSubcontextsFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199019", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionInitializeJndiSubcontextsFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199019", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionPrepareUpdateFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199020", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionPrepareUpdateFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199020", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionJndiNameNull() {
      Object[] var0 = new Object[0];
      return (new Loggable("199021", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionJndiNameNullLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199021", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionNoInitialContextForJndi() {
      Object[] var0 = new Object[0];
      return (new Loggable("199022", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionNoInitialContextForJndiLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199022", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionResourceLinkNull() {
      Object[] var0 = new Object[0];
      return (new Loggable("199023", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionResourceLinkNullLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199023", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionNoInitialContextForResourceLink() {
      Object[] var0 = new Object[0];
      return (new Loggable("199024", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionNoInitialContextForResourceLinkLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199024", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionAlreadyDeployed(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199028", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionAlreadyDeployedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199028", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionBindingFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199029", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionBindingFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199029", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionUnbindFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199031", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionUnbindFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199031", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionNoInitialContextForUnbind(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199032", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionNoInitialContextForUnbindLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199032", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionResourceLinkAlreadyBound(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199036", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionResourceLinkAlreadyBoundLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199036", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionAppScopedBindFailed(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return (new Loggable("199037", var3, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionAppScopedBindFailedLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("199037", var3, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionUnbindAdminObjectFailed(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199038", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionUnbindAdminObjectFailedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199038", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionGetConnectionFactoryFailed(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199039", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionGetConnectionFactoryFailedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199039", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionRANewInstanceFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199041", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionRANewInstanceFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199041", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionImageSourceCreation(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199042", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionImageSourceCreationLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199042", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionPrepareUninitializedRA() {
      Object[] var0 = new Object[0];
      return (new Loggable("199043", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionPrepareUninitializedRALoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199043", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionActivateUnpreparedRA(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199044", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionActivateUnpreparedRALoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199044", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionRollbackActivatedRA() {
      Object[] var0 = new Object[0];
      return (new Loggable("199045", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionRollbackActivatedRALoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199045", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionCreateNativeLib() {
      Object[] var0 = new Object[0];
      return (new Loggable("199046", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionCreateNativeLibLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199046", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionBadRAClassSpec(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199047", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionBadRAClassSpecLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199047", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionAdapterNotVersionable() {
      Object[] var0 = new Object[0];
      return (new Loggable("199048", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionAdapterNotVersionableLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199048", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionPopulateWorkManager() {
      Object[] var0 = new Object[0];
      return (new Loggable("199049", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionPopulateWorkManagerLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199049", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionStartRA(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199050", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionStartRALoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199050", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionCreateBootstrap(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199051", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionCreateBootstrapLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199051", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionVersionRA() {
      Object[] var0 = new Object[0];
      return (new Loggable("199052", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionVersionRALoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199052", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionWorkRuntimer() {
      Object[] var0 = new Object[0];
      return (new Loggable("199053", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionWorkRuntimerLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199053", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionIntrospectProperties(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199054", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionIntrospectPropertiesLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199054", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionSetterNotFound(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199055", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionSetterNotFoundLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199055", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionInvokeSetter(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199056", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionInvokeSetterLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199056", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionBadPropertyType(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199065", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionBadPropertyTypeLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199065", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionPropertyValueTypeMismatch(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return (new Loggable("199066", var4, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionPropertyValueTypeMismatchLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("199066", var4, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionLoginException(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return (new Loggable("199067", var3, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionLoginExceptionLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("199067", var3, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionRANotDeployed(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199068", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionRANotDeployedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199068", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionInitializeActivationSpecFailed(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199071", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionInitializeActivationSpecFailedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199071", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionInstantiateClassFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199072", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionInstantiateClassFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199072", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionBadValue(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return (new Loggable("199073", var3, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionBadValueLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("199073", var3, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionMissingRequiredProperty(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199074", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionMissingRequiredPropertyLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199074", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionRANotActive(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199075", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionRANotActiveLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199075", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionRANotFound(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199076", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionRANotFoundLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199076", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionNoMessageListener(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199077", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionNoMessageListenerLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199077", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionNoInboundRAElement() {
      Object[] var0 = new Object[0];
      return (new Loggable("199089", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionNoInboundRAElementLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199089", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionNoMessageAdapterElement() {
      Object[] var0 = new Object[0];
      return (new Loggable("199090", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionNoMessageAdapterElementLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199090", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionNoMessageListenerElement() {
      Object[] var0 = new Object[0];
      return (new Loggable("199091", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionNoMessageListenerElementLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199091", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionAssertionError(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199092", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionAssertionErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199092", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionSetDyeBitsFailedDiagCtxNotEnabled() {
      Object[] var0 = new Object[0];
      return (new Loggable("199093", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionSetDyeBitsFailedDiagCtxNotEnabledLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199093", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionInvalidDyeValue(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199094", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionInvalidDyeValueLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199094", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionInvalidDye(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199095", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionInvalidDyeLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199095", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionFailedToGetDiagCtx(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199096", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionFailedToGetDiagCtxLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199096", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionGetDyeBitsFailedDiagCtxNotEnabled() {
      Object[] var0 = new Object[0];
      return (new Loggable("199097", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionGetDyeBitsFailedDiagCtxNotEnabledLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199097", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionCannotDeleteConnection() {
      Object[] var0 = new Object[0];
      return (new Loggable("199098", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionCannotDeleteConnectionLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199098", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionEnlistmentFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199099", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionEnlistmentFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199099", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionMCGetLocalTransactionThrewNonResourceException(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199100", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionMCGetLocalTransactionThrewNonResourceExceptionLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199100", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionMCGetLocalTransactionReturnedNull(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199101", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionMCGetLocalTransactionReturnedNullLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199101", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionRegisterNonXAFailed(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199102", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionRegisterNonXAFailedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199102", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionCommitFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199103", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionCommitFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199103", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionRollbackFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199104", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionRollbackFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199104", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionMCGetXAResourceReturnedNull() {
      Object[] var0 = new Object[0];
      return (new Loggable("199105", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionMCGetXAResourceReturnedNullLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199105", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionMCGetXAResourceThrewNonResourceException(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199106", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionMCGetXAResourceThrewNonResourceExceptionLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199106", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionMCFCreateManagedConnectionReturnedNull() {
      Object[] var0 = new Object[0];
      return (new Loggable("199107", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionMCFCreateManagedConnectionReturnedNullLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199107", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionInitializeForRecoveryFailed(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199108", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionInitializeForRecoveryFailedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199108", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionEnlistResourceIllegalType() {
      Object[] var0 = new Object[0];
      return (new Loggable("199109", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionEnlistResourceIllegalTypeLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199109", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionRegisterResourceIllegalType(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199110", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionRegisterResourceIllegalTypeLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199110", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionXAStartInLocalTxIllegal() {
      Object[] var0 = new Object[0];
      return (new Loggable("199111", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionXAStartInLocalTxIllegalLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199111", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionCreateMCFailed(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199112", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionCreateMCFailedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199112", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionFailedMCSetup() {
      Object[] var0 = new Object[0];
      return (new Loggable("199113", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionFailedMCSetupLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199113", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionObjectIdNull() {
      Object[] var0 = new Object[0];
      return (new Loggable("199114", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionObjectIdNullLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199114", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionMCGetConnectionReturnedNull(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199116", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionMCGetConnectionReturnedNullLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199116", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionDuplicateHandle() {
      Object[] var0 = new Object[0];
      return (new Loggable("199117", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionDuplicateHandleLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199117", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionTestResourceException(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199118", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionTestResourceExceptionLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199118", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionTestNonResourceException(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199119", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionTestNonResourceExceptionLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199119", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionMCFNotImplementValidatingMCF() {
      Object[] var0 = new Object[0];
      return (new Loggable("199120", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionMCFNotImplementValidatingMCFLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199120", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getLazyEnlistNullMC(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199121", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getLazyEnlistNullMCLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199121", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionRAAccessDenied(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199122", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionRAAccessDeniedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199122", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionGetConnectionFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199123", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionGetConnectionFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199123", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionPoolDisabled(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199124", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionPoolDisabledLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199124", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionMCFCreateCFReturnedNull() {
      Object[] var0 = new Object[0];
      return (new Loggable("199125", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionMCFCreateCFReturnedNullLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199125", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionStackTrace() {
      Object[] var0 = new Object[0];
      return (new Loggable("199126", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionStackTraceLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199126", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionLocalTxNotSupported() {
      Object[] var0 = new Object[0];
      return (new Loggable("199127", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionLocalTxNotSupportedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199127", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionHandleNotSet() {
      Object[] var0 = new Object[0];
      return (new Loggable("199128", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionHandleNotSetLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199128", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionOutboundPrepareFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199129", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionOutboundPrepareFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199129", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionResumePoolFailed(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199130", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionResumePoolFailedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199130", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionJndiBindFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199131", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionJndiBindFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199131", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionDeactivateException(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return (new Loggable("199132", var3, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionDeactivateExceptionLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("199132", var3, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionShutdownException(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return (new Loggable("199133", var3, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionShutdownExceptionLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("199133", var3, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionCFJndiNameDuplicate(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199134", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionCFJndiNameDuplicateLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199134", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionCFResourceLinkDuplicate(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199135", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionCFResourceLinkDuplicateLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199135", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionJndiVerifyFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199136", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionJndiVerifyFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199136", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionMCFNoImplementResourceAdapterAssociation(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199137", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionMCFNoImplementResourceAdapterAssociationLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199137", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionSetRAClassFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199138", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionSetRAClassFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199138", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionMCFUnexpectedException(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199139", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionMCFUnexpectedExceptionLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199139", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionMCFClassNotFound(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199140", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionMCFClassNotFoundLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199140", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionInstantiateMCFFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199141", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionInstantiateMCFFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199141", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionAccessMCFFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199142", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionAccessMCFFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199142", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionGetConnectionFactoryFailedInternalError(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199143", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionGetConnectionFactoryFailedInternalErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199143", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionFailedAccessOutsideApp() {
      Object[] var0 = new Object[0];
      return (new Loggable("199144", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionFailedAccessOutsideAppLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199144", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionNotImplemented(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199145", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionNotImplementedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199145", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionMustBeLinkRef() {
      Object[] var0 = new Object[0];
      return (new Loggable("199146", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionMustBeLinkRefLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199146", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionNeedsRAXML() {
      Object[] var0 = new Object[0];
      return (new Loggable("199147", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionNeedsRAXMLLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199147", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionErrorCreatingNativeLibDir(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199148", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionErrorCreatingNativeLibDirLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199148", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionFileNotFoundForNativeLibDir(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199149", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionFileNotFoundForNativeLibDirLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199149", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionExceptionCreatingNativeLibDir(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199150", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionExceptionCreatingNativeLibDirLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199150", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionStartPoolFailed(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199151", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionStartPoolFailedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199151", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionTestFrequencyNonZero() {
      Object[] var0 = new Object[0];
      return (new Loggable("199152", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionTestFrequencyNonZeroLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199152", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionInvalidTestingConfig() {
      Object[] var0 = new Object[0];
      return (new Loggable("199153", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionInvalidTestingConfigLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199153", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionWorkIsNull() {
      Object[] var0 = new Object[0];
      return (new Loggable("199154", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionWorkIsNullLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199154", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionDoWorkNotAccepted() {
      Object[] var0 = new Object[0];
      return (new Loggable("199155", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionDoWorkNotAcceptedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199155", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionWorkManagerSuspended() {
      Object[] var0 = new Object[0];
      return (new Loggable("199156", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionWorkManagerSuspendedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199156", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionSetExecutionContextFailed(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199157", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionSetExecutionContextFailedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199157", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionInvalidGid(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199158", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionInvalidGidLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199158", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionGidNotRegistered(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199159", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionGidNotRegisteredLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199159", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionSecurityPrincipalMapNotSupported() {
      Object[] var0 = new Object[0];
      return (new Loggable("199160", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionSecurityPrincipalMapNotSupportedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199160", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionImportedTxAlreadyActive(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199161", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionImportedTxAlreadyActiveLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199161", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionBadMCFClassSpec(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199162", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionBadMCFClassSpecLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199162", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionActivatePoolFailed(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199164", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionActivatePoolFailedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199164", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getTestConnectionsOnCreateTrue() {
      Object[] var0 = new Object[0];
      return (new Loggable("199165", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getTestConnectionsOnCreateTrueLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199165", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getTestConnectionsOnReleaseTrue() {
      Object[] var0 = new Object[0];
      return (new Loggable("199166", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getTestConnectionsOnReleaseTrueLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199166", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getTestConnectionsOnReserveTrue() {
      Object[] var0 = new Object[0];
      return (new Loggable("199167", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getTestConnectionsOnReserveTrueLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199167", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getFailedToForceLogRotation(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199168", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getFailedToForceLogRotationLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199168", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getFailedToGetCF(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199169", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getFailedToGetCFLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199169", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getDeploySecurityBumpUpFailed(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return (new Loggable("199170", var3, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getDeploySecurityBumpUpFailedLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("199170", var3, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logSecurityPrincipalMapNotAllowed() {
      Object[] var0 = new Object[0];
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("199171", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "199171";
   }

   public static Loggable logSecurityPrincipalMapNotAllowedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199171", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logComplianceRAConfigurationException(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("199172", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "199172";
   }

   public static Loggable logComplianceRAConfigurationExceptionLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199172", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logComplianceWLRAConfigurationException(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("199173", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "199173";
   }

   public static Loggable logComplianceWLRAConfigurationExceptionLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199173", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionActivateSuspendedRA(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199174", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionActivateSuspendedRALoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199174", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logAdapterShouldnotSendLocalTxEvent(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("199175", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable logAdapterShouldnotSendLocalTxEventLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199175", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionAllocateConnectionOnStaleConnectionFactory(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return (new Loggable("199176", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionAllocateConnectionOnStaleConnectionFactoryLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199176", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String getExceptionDeserializeConnectionManager() {
      Object[] var0 = new Object[0];
      return (new Loggable("199177", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getExceptionDeserializeConnectionManagerLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199177", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logDeploySideBySide(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("199178", var3, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "199178";
   }

   public static Loggable logDeploySideBySideLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("199178", var3, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logSkipSideBySide() {
      Object[] var0 = new Object[0];
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("199179", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "199179";
   }

   public static Loggable logSkipSideBySideLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199179", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logAppNotSideBySide() {
      Object[] var0 = new Object[0];
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("199180", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "199180";
   }

   public static Loggable logAppNotSideBySideLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("199180", var0, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logWaitingComplete(String var0) {
      Object[] var1 = new Object[]{var0};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("199181", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "199181";
   }

   public static Loggable logWaitingCompleteLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("199181", var1, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logCompleteCalled(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("199182", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "199182";
   }

   public static Loggable logCompleteCalledLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("199182", var2, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logShareableRefToUnshareableMCF(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("199191", var3, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "199191";
   }

   public static Loggable logShareableRefToUnshareableMCFLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("199191", var3, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logUnknownShareableRefToUnshareableMCF(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("199192", var3, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "199192";
   }

   public static Loggable logUnknownShareableRefToUnshareableMCFLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("199192", var3, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   public static String logMCDestroyedAlready(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("199193", var5, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.class.getClassLoader()));
      return "199193";
   }

   public static Loggable logMCDestroyedAlreadyLoggable(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return new Loggable("199193", var5, "weblogic.connector.ConnectorLogLocalizer", ConnectorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConnectorLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = ConnectorLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = ConnectorLogger.findMessageLogger();
      }
   }
}
