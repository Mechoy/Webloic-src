package weblogic.connector.common;

import java.util.Hashtable;
import java.util.Properties;
import javax.naming.NamingException;
import javax.resource.ResourceException;
import weblogic.connector.ConnectorLogger;
import weblogic.connector.outbound.ConnectionInfo;
import weblogic.connector.outbound.ConnectionPool;
import weblogic.connector.outbound.ProfileDataRecord;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.logging.NonCatalogLogger;

public class Debug {
   public static final int INDENT_WIDTH = 2;
   private static final String WHITE_SPACE = "";
   private static final int MAX_SPACES = "".length();
   public static boolean verbose = System.getProperty("weblogic.connector.Debug") != null && !System.getProperty("weblogic.connector.Debug").equalsIgnoreCase("false");
   public static int indentLevel = 0;
   static NonCatalogLogger logger = new NonCatalogLogger("ConnectorDebug");
   private static DebugLogger XAIN_LOGGER = null;
   private static DebugLogger CONNECTIONS_LOGGER = null;
   private static DebugLogger CONNEVENTS_LOGGER = null;
   private static DebugLogger DEPLOYMENT_LOGGER = null;
   private static DebugLogger LOCALOUT_LOGGER = null;
   private static DebugLogger PARSING_LOGGER = null;
   private static DebugLogger POOLING_LOGGER = null;
   private static DebugLogger POOLVERBOSE_LOGGER = null;
   private static DebugLogger RALIFECYCLE_LOGGER = null;
   private static DebugLogger SECURITYCTX_LOGGER = null;
   private static DebugLogger SERVICE_LOGGER = null;
   private static DebugLogger WORK_LOGGER = null;
   private static DebugLogger WORKEVENTS_LOGGER = null;
   private static DebugLogger XAOUT_LOGGER = null;
   private static DebugLogger XAWORK_LOGGER = null;
   private static DebugLogger RACLASSLOADING = null;

   public static DebugLogger xain() {
      if (XAIN_LOGGER == null) {
         XAIN_LOGGER = DebugLogger.getDebugLogger("DebugRAXAin");
      }

      return XAIN_LOGGER;
   }

   public static DebugLogger connections() {
      if (CONNECTIONS_LOGGER == null) {
         CONNECTIONS_LOGGER = DebugLogger.getDebugLogger("DebugRAConnections");
      }

      return CONNECTIONS_LOGGER;
   }

   public static DebugLogger connevents() {
      if (CONNEVENTS_LOGGER == null) {
         CONNEVENTS_LOGGER = DebugLogger.getDebugLogger("DebugRAConnEvents");
      }

      return CONNEVENTS_LOGGER;
   }

   public static DebugLogger deployment() {
      if (DEPLOYMENT_LOGGER == null) {
         DEPLOYMENT_LOGGER = DebugLogger.getDebugLogger("DebugRADeployment");
      }

      return DEPLOYMENT_LOGGER;
   }

   public static DebugLogger localout() {
      if (LOCALOUT_LOGGER == null) {
         LOCALOUT_LOGGER = DebugLogger.getDebugLogger("DebugRALocalOut");
      }

      return LOCALOUT_LOGGER;
   }

   public static DebugLogger parsing() {
      if (PARSING_LOGGER == null) {
         PARSING_LOGGER = DebugLogger.getDebugLogger("DebugRAParsing");
      }

      return PARSING_LOGGER;
   }

   public static DebugLogger pooling() {
      if (POOLING_LOGGER == null) {
         POOLING_LOGGER = DebugLogger.getDebugLogger("DebugRAPooling");
      }

      return POOLING_LOGGER;
   }

   public static DebugLogger poolverbose() {
      if (POOLVERBOSE_LOGGER == null) {
         POOLVERBOSE_LOGGER = DebugLogger.getDebugLogger("DebugRAPoolVerbose");
      }

      return POOLVERBOSE_LOGGER;
   }

   public static DebugLogger ralifecycle() {
      if (RALIFECYCLE_LOGGER == null) {
         RALIFECYCLE_LOGGER = DebugLogger.getDebugLogger("DebugRALifecycle");
      }

      return RALIFECYCLE_LOGGER;
   }

   public static DebugLogger securityctx() {
      if (SECURITYCTX_LOGGER == null) {
         SECURITYCTX_LOGGER = DebugLogger.getDebugLogger("DebugRASecurityCtx");
      }

      return SECURITYCTX_LOGGER;
   }

   public static DebugLogger service() {
      if (SERVICE_LOGGER == null) {
         SERVICE_LOGGER = DebugLogger.getDebugLogger("DebugConnectorService");
      }

      return SERVICE_LOGGER;
   }

   public static DebugLogger work() {
      if (WORK_LOGGER == null) {
         WORK_LOGGER = DebugLogger.getDebugLogger("DebugRAWork");
      }

      return WORK_LOGGER;
   }

   public static DebugLogger workevents() {
      if (WORKEVENTS_LOGGER == null) {
         WORKEVENTS_LOGGER = DebugLogger.getDebugLogger("DebugRAWorkEvents");
      }

      return WORKEVENTS_LOGGER;
   }

   public static DebugLogger xaout() {
      if (XAOUT_LOGGER == null) {
         XAOUT_LOGGER = DebugLogger.getDebugLogger("DebugRAXAout");
      }

      return XAOUT_LOGGER;
   }

   public static DebugLogger xawork() {
      if (XAWORK_LOGGER == null) {
         XAWORK_LOGGER = DebugLogger.getDebugLogger("DebugRAXAwork");
      }

      return XAWORK_LOGGER;
   }

   public static DebugLogger classloading() {
      if (RACLASSLOADING == null) {
         RACLASSLOADING = DebugLogger.getDebugLogger("DebugRAClassloader");
      }

      return RACLASSLOADING;
   }

   public static void connEvent(String var0) {
      connevents().debug(var0);
   }

   public static void connections(String var0) {
      connections().debug(var0);
   }

   public static void connections(String var0, Throwable var1) {
      connections().debug(var0, var1);
   }

   public static void deployment(String var0) {
      deployment().debug(var0);
   }

   public static void deployment(String var0, Throwable var1) {
      deployment().debug(var0, var1);
   }

   public static void localOut(String var0) {
      localout().debug(var0);
   }

   public static void parsing(String var0) {
      parsing().debug(var0);
   }

   public static void poolVerbose(String var0) {
      poolverbose().debug(var0);
   }

   public static void pooling(String var0) {
      pooling().debug(var0);
   }

   public static void raLifecycle(String var0) {
      ralifecycle().debug(var0);
   }

   public static void securityCtx(String var0) {
      securityctx().debug(var0);
   }

   public static void service(String var0) {
      service().debug(var0);
   }

   public static void work(String var0) {
      work().debug(var0);
   }

   public static void workEvent(String var0) {
      workevents().debug(var0);
   }

   public static void xaIn(String var0) {
      xain().debug(var0);
   }

   public static void xaOut(String var0) {
      xaout().debug(var0);
   }

   public static void xaWork(String var0) {
      xawork().debug(var0);
   }

   public static void classloading(String var0) {
      classloading().debug(var0);
   }

   public static void localOut(ConnectionPool var0, String var1) {
      if (isLocalOutEnabled()) {
         localOut("For pool '" + (var0 != null ? var0.getName() : "<null>") + "' " + var1);
      }

   }

   public static void xaOut(ConnectionPool var0, String var1) {
      if (isXAoutEnabled()) {
         xaOut("For pool '" + (var0 != null ? var0.getName() : "<null>") + "' " + var1);
      }

   }

   public static void enter(Object var0, String var1) {
      if (verbose || getVerbose(var0)) {
         logger.debug(spaces() + getClassName(var0) + "." + var1 + " entered: " + (var0 != null ? var0.toString() : ""));
         ++indentLevel;
      }

   }

   public static void enter(String var0) {
      if (verbose) {
         logger.debug(spaces() + var0 + "() entered. ");
         ++indentLevel;
      }

   }

   public static void exit(Object var0, String var1) {
      if (verbose || getVerbose(var0)) {
         --indentLevel;
         logger.debug(spaces() + getClassName(var0) + "." + var1 + " exiting: " + (var0 != null ? var0.toString() : ""));
      }

   }

   public static void exit(String var0) {
      if (verbose) {
         --indentLevel;
         logger.debug(spaces() + var0 + "() exiting. ");
      }

   }

   public static void println(Object var0, String var1) {
      if (verbose || getVerbose(var0)) {
         logger.debug(spaces() + getClassName(var0) + var1);
      }

   }

   public static void println(String var0) {
      if (verbose) {
         logger.debug(spaces() + var0);
      }

   }

   public static void printHashtable(Object var0, String var1, String var2, Hashtable var3) {
      if (verbose || getVerbose(var0)) {
         logger.debug(hashtableToString(var0, var1, var2, var3));
      }

   }

   public static String hashtableToString(Object var0, String var1, String var2, Hashtable var3) {
      String var4 = "";
      if (var0 != null) {
         var4 = getClassName(var0) + var1;
      } else {
         var4 = var1;
      }

      String var5 = var4 + " Hashtable " + var2 + " =\n";
      if (var3 == null) {
         return var5 + var4 + " Hashtable " + var2 + " is null ";
      } else {
         return var3.isEmpty() ? var5 + var4 + " Hashtable " + var2 + " is EMPTY " : var5 + var4 + " Hashtable " + var2 + " has " + var3.size() + " entries.\n" + var4 + " Hashtable " + var2 + " = " + var3.toString();
      }
   }

   public static void showClassLoaders(Object var0, Object var1) {
      if (verbose) {
         String var2 = null;
         if (var1 != null) {
            try {
               var2 = var1.toString();
            } catch (Throwable var4) {
               var2 = "obj.toString() threw " + var4;
            }
         }

         println("Classloaders for object " + var2 + ":");
         showClassLoaderTree("\t", var1.getClass().getClassLoader());
         if (var0 != null) {
            showClassLoaderTree("\tCaller's classloader = ", var0.getClass().getClassLoader());
         }

         showClassLoaderTree("\tSystem classloader = ", ClassLoader.getSystemClassLoader());
         showClassLoaderTree("\tThread context classloader = ", Thread.currentThread().getContextClassLoader());
      }

   }

   public static void showClassLoaderTree(String var0, ClassLoader var1) {
      String var2 = "";
      String var3 = "";
      println(var0);

      while(var1 != null) {
         println(var2 + var3 + var1);
         var2 = var2 + "\t";
         var3 = "parent: ";
         var1 = var1.getParent();
      }

   }

   public static void assertion(boolean var0, String var1) {
      if (!var0) {
         throwAssertionError(var1);
      }

   }

   public static String logJarFileProcessingError(Exception var0) {
      return ConnectorLogger.logJarFileProcessingError(var0);
   }

   public static String logStackTrace(String var0, Throwable var1) {
      return ConnectorLogger.logStackTrace(var0, var1);
   }

   public static String logStackTraceString(String var0, String var1) {
      return ConnectorLogger.logStackTraceString(var0, var1);
   }

   public static String logConnectorServiceShutdownError(String var0) {
      return ConnectorLogger.logConnectorServiceShutdownError(var0);
   }

   public static String logConnectorServiceInitializing() {
      return ConnectorLogger.logConnectorServiceInitializing();
   }

   public static String logConnectorServiceInitError(String var0) {
      return ConnectorLogger.logConnectorServiceInitError(var0);
   }

   public static String logConnectorServiceInitialized() {
      return ConnectorLogger.logConnectorServiceInitialized();
   }

   public static String logDeprecationReplacedWarning(String var0, String var1) {
      return ConnectorLogger.logDeprecationReplacedWarning(var0, var1);
   }

   public static String logDeprecationNotUsedWarning(String var0) {
      return ConnectorLogger.logDeprecationNotUsedWarning(var0);
   }

   public static String logDeprecatedLinkref(String var0) {
      return ConnectorLogger.logDeprecatedLinkref(var0);
   }

   public static String logJNDINameAlreadyExists(String var0) {
      return ConnectorLogger.logJNDINameAlreadyExists(var0);
   }

   public static String logRarMarkedForLateDeployment(String var0) {
      return ConnectorLogger.logRarMarkedForLateDeployment(var0);
   }

   public static String logCreateCFforMCFError(String var0, ResourceException var1) {
      return ConnectorLogger.logCreateCFforMCFError(var0, var1);
   }

   public static String logCreateInitialConnectionsError(String var0, String var1) {
      return ConnectorLogger.logCreateInitialConnectionsError(var0, var1);
   }

   public static String logInitConnRTMBeanError(String var0, String var1) {
      return ConnectorLogger.logInitConnRTMBeanError(var0, var1);
   }

   public static String logUnregisterConnRTMBeanError(String var0, String var1) {
      return ConnectorLogger.logUnregisterConnRTMBeanError(var0, var1);
   }

   public static String logInitCPRTMBeanError(String var0, String var1) {
      return ConnectorLogger.logInitCPRTMBeanError(var0, var1);
   }

   public static String logUnregisterCPRTMBeanError(String var0, String var1) {
      return ConnectorLogger.logUnregisterCPRTMBeanError(var0, var1);
   }

   public static String logProxyTestError(String var0, Throwable var1) {
      return ConnectorLogger.logProxyTestError(var0, var1);
   }

   public static String logProxyTestFailureInfo(String var0, String var1) {
      return ConnectorLogger.logProxyTestFailureInfo(var0, var1);
   }

   public static String logProxyTestStarted(String var0) {
      return ConnectorLogger.logProxyTestStarted(var0);
   }

   public static String logProxyTestSuccess(String var0) {
      return ConnectorLogger.logProxyTestSuccess(var0);
   }

   public static String logReReleasingResource(String var0) {
      return ConnectorLogger.logReReleasingResource(var0);
   }

   public static String logSetLogWriterError(String var0) {
      return ConnectorLogger.logSetLogWriterError(var0);
   }

   public static String logSetLogWriterErrorWithCause(String var0, String var1, String var2) {
      return ConnectorLogger.logSetLogWriterErrorWithCause(var0, var1, var2);
   }

   public static String logFindLogWriterError(String var0, String var1) {
      return ConnectorLogger.logFindLogWriterError(var0, var1);
   }

   public static String logCreateManagedConnectionException(String var0, String var1) {
      return ConnectorLogger.logCreateManagedConnectionException(var0, var1);
   }

   public static String logCreateManagedConnectionError(String var0) {
      return ConnectorLogger.logCreateManagedConnectionError(var0);
   }

   public static String logCloseConnectionError(String var0, ConnectionInfo var1, String var2, Throwable var3) {
      return ConnectorLogger.logCloseConnectionError(var0, var1 == null ? "[null]" : var1.toString(), var2, var3);
   }

   public static String logCloseNotFoundOnHandle(String var0) {
      return ConnectorLogger.logCloseNotFoundOnHandle(var0);
   }

   public static String logConnectionAlreadyClosed(String var0) {
      return ConnectorLogger.logConnectionAlreadyClosed(var0);
   }

   public static String logAccessDeniedWarning(String var0, String var1, String var2, String var3) {
      return ConnectorLogger.logAccessDeniedWarning(var0, var1, var2, var3);
   }

   public static String logNoConnectionRequestInfo() {
      return ConnectorLogger.logNoConnectionRequestInfo();
   }

   public static String logNoResourcePrincipalFound() {
      return ConnectorLogger.logNoResourcePrincipalFound();
   }

   public static String logRequestedSecurityType(String var0, String var1) {
      return ConnectorLogger.logRequestedSecurityType(var0, var1);
   }

   public static String logContextProcessingError(NamingException var0) {
      return ConnectorLogger.logContextProcessingError(var0);
   }

   public static String logMCFNotFoundForJNDIName(String var0) {
      return ConnectorLogger.logMCFNotFoundForJNDIName(var0);
   }

   public static String logCreateCFReturnedNull(String var0) {
      return ConnectorLogger.logCreateCFReturnedNull(var0);
   }

   public static String logGetLocalTransactionError(String var0, String var1) {
      return ConnectorLogger.logGetLocalTransactionError(var0, var1);
   }

   public static String logRegisterNonXAResourceError(String var0, String var1) {
      return ConnectorLogger.logRegisterNonXAResourceError(var0, var1);
   }

   public static String logGetXAResourceError(String var0, String var1) {
      return ConnectorLogger.logGetXAResourceError(var0, var1);
   }

   public static String logRegisterXAResourceError(String var0, String var1) {
      return ConnectorLogger.logRegisterXAResourceError(var0, var1);
   }

   public static String logInvokeMethodError(String var0, String var1, String var2) {
      return ConnectorLogger.logInvokeMethodError(var0, var1, var2);
   }

   public static String logDiagImageUnregisterFailure(Throwable var0) {
      return ConnectorLogger.logDiagImageUnregisterFailure(var0);
   }

   public static String logDiagImageRegisterFailure(Throwable var0) {
      return ConnectorLogger.logDiagImageRegisterFailure(var0);
   }

   public static String logConfigPropWarning(String var0, String var1, String var2) {
      return ConnectorLogger.logConfigPropWarning(var0, var1, var2);
   }

   public static String logGetAnonymousSubjectFailed() {
      return ConnectorLogger.logGetAnonymousSubjectFailed();
   }

   public static String logFailedToFindModuleRuntimeMBean(String var0) {
      return ConnectorLogger.logFailedToFindModuleRuntimeMBean(var0);
   }

   public static String logFailedToUnregisterModuleRuntimeMBean(String var0) {
      return ConnectorLogger.logFailedToUnregisterModuleRuntimeMBean(var0);
   }

   public static String logInitJndiSubcontextsFailed(String var0, String var1) {
      return ConnectorLogger.logInitJndiSubcontextsFailed(var0, var1);
   }

   public static String logExtractingNativeLib(String var0, String var1) {
      return ConnectorLogger.logExtractingNativeLib(var0, var1);
   }

   public static String logTimerWarning() {
      return ConnectorLogger.logTimerWarning();
   }

   public static String logInvalidDye(String var0, String var1) {
      return ConnectorLogger.logInvalidDye(var0, var1);
   }

   public static String logRegisterForXARecoveryFailed(String var0) {
      return ConnectorLogger.logRegisterForXARecoveryFailed(var0);
   }

   public static String logUnregisterForXARecoveryFailed(String var0) {
      return ConnectorLogger.logUnregisterForXARecoveryFailed(var0);
   }

   public static String logFailedToApplyPoolChanges(String var0) {
      return ConnectorLogger.logFailedToApplyPoolChanges(var0);
   }

   public static String logMCFNotImplementResourceAdapterAssociation(String var0) {
      return ConnectorLogger.logMCFNotImplementResourceAdapterAssociation(var0);
   }

   public static String logInvalidRecoveryEvent(String var0) {
      return ConnectorLogger.logInvalidRecoveryEvent(var0);
   }

   public static String logCleanupFailure(String var0) {
      return ConnectorLogger.logCleanupFailure(var0);
   }

   public static String logConnectionError(String var0) {
      return ConnectorLogger.logConnectionError(var0);
   }

   public static String logDestroyFailed(String var0) {
      return ConnectorLogger.logDestroyFailed(var0);
   }

   public static String logNullXAResource() {
      return ConnectorLogger.logNullXAResource();
   }

   public static String logDissociateHandlesFailed(String var0, String var1) {
      return ConnectorLogger.logDissociateHandlesFailed(var0, var1);
   }

   public static String logLazyEnlistNullMC() {
      return ConnectorLogger.logLazyEnlistNullMC();
   }

   public static String logRequestedSharingScope(String var0, String var1) {
      return ConnectorLogger.logRequestedSharingScope(var0, var1);
   }

   public static String logFailedToDeployLinkRef(String var0, String var1, String var2) {
      return ConnectorLogger.logFailedToDeployLinkRef(var0, var1, var2);
   }

   public static String logAssertionError(String var0, Throwable var1) {
      return ConnectorLogger.logAssertionError(var0, var1);
   }

   public static String logPoolProfilingRecord(ProfileDataRecord var0) {
      return ConnectorLogger.logPoolProfilingRecord(var0.getPoolName(), var0.getType(), var0.getTimestamp(), var0.getPropertiesString());
   }

   public static String logPropertyVetoWarning(String var0, String var1, String var2, String var3, String var4) {
      return ConnectorLogger.logPropertyVetoWarning(var0, var1, var2, var3, var4);
   }

   public static String logNoAdapterJNDInameSetForInboundRA(String var0, String var1) {
      return ConnectorLogger.logNoAdapterJNDInameSetForInboundRA(var0, var1);
   }

   public static String logDiagnosticImageTimedOut() {
      return ConnectorLogger.logDiagnosticImageTimedOut();
   }

   public static String logBuildOutboundFailed(String var0) {
      return ConnectorLogger.logBuildOutboundFailed(var0);
   }

   public static String logCreateInboundRuntimeMBeanFailed(String var0, String var1) {
      return ConnectorLogger.logCreateInboundRuntimeMBeanFailed(var0, var1);
   }

   public static String logFailedToCloseLog(String var0, String var1) {
      return ConnectorLogger.logFailedToCloseLog(var0, var1);
   }

   public static String logFailedToCreateLogStream(String var0, String var1) {
      return ConnectorLogger.logFailedToCreateLogStream(var0, var1);
   }

   public static String logSecurityPrincipalMapNotAllowed() {
      return ConnectorLogger.logSecurityPrincipalMapNotAllowed();
   }

   public static String logComplianceRAConfigurationException(String var0) {
      return ConnectorLogger.logComplianceRAConfigurationException(var0);
   }

   public static String logComplianceWLRAConfigurationException(String var0) {
      return ConnectorLogger.logComplianceWLRAConfigurationException(var0);
   }

   public static String logNoComplianceErrors(String var0) {
      return ConnectorLogger.logNoComplianceErrors(var0);
   }

   public static String logNumComplianceErrorsAndWarnings(String var0, int var1, int var2) {
      return ConnectorLogger.logNumComplianceErrorsAndWarnings(var0, var1, var2);
   }

   public static String logComplianceWarnings(String var0, String var1) {
      return ConnectorLogger.logComplianceWarnings(var0, var1);
   }

   public static String logComplianceIsLinkRef(String var0) {
      return ConnectorLogger.logComplianceIsLinkRef(var0);
   }

   public static String getStringAnonymousUser() {
      return ConnectorLogger.getStringAnonymousUserLoggable().getMessageText();
   }

   public static String getStringCloseCount() {
      return ConnectorLogger.getStringCloseCountLoggable().getMessageText();
   }

   public static String getStringCreateCount() {
      return ConnectorLogger.getStringCreateCountLoggable().getMessageText();
   }

   public static String getStringFreePoolSize() {
      return ConnectorLogger.getStringFreePoolSizeLoggable().getMessageText();
   }

   public static String getStringPoolSize() {
      return ConnectorLogger.getStringPoolSizeLoggable().getMessageText();
   }

   public static String getStringWaitingThreadCount() {
      return ConnectorLogger.getStringWaitingThreadCountLoggable().getMessageText();
   }

   public static String getStringCloseCountDescription() {
      return ConnectorLogger.getStringCloseCountLoggable().getMessageText();
   }

   public static String getStringCreateCountDescription() {
      return ConnectorLogger.getStringCreateCountDescriptionLoggable().getMessageText();
   }

   public static String getStringFreePoolSizeDescription() {
      return ConnectorLogger.getStringFreePoolSizeDescriptionLoggable().getMessageText();
   }

   public static String getStringPoolSizeDescription() {
      return ConnectorLogger.getStringPoolSizeDescriptionLoggable().getMessageText();
   }

   public static String getStringWaitingThreadCountDescription() {
      return ConnectorLogger.getStringWaitingThreadCountDescriptionLoggable().getMessageText();
   }

   public static String getStringNever() {
      return ConnectorLogger.getStringNeverLoggable().getMessageText();
   }

   public static String getStringUnavailable() {
      return ConnectorLogger.getStringUnavailableLoggable().getMessageText();
   }

   public static String getStringRunning() {
      return ConnectorLogger.getStringRunningLoggable().getMessageText();
   }

   public static String getStringSuspended() {
      return ConnectorLogger.getStringSuspendedLoggable().getMessageText();
   }

   public static String getStringNew() {
      return ConnectorLogger.getStringNewLoggable().getMessageText();
   }

   public static String getStringInitialized() {
      return ConnectorLogger.getStringInitializedLoggable().getMessageText();
   }

   public static String getStringPrepared() {
      return ConnectorLogger.getStringPreparedLoggable().getMessageText();
   }

   public static String getStringActivated() {
      return ConnectorLogger.getStringActivatedLoggable().getMessageText();
   }

   public static String getStringUnknown() {
      return ConnectorLogger.getStringUnknownLoggable().getMessageText();
   }

   public static String getExceptionRANewInstanceFailed(String var0, String var1) {
      return ConnectorLogger.getExceptionRANewInstanceFailedLoggable(var0, var1).getMessageText();
   }

   public static String getExceptionImageSourceCreation(String var0) {
      return ConnectorLogger.getExceptionImageSourceCreationLoggable(var0).getMessageText();
   }

   public static String getExceptionPrepareUninitializedRA() {
      return ConnectorLogger.getExceptionPrepareUninitializedRALoggable().getMessageText();
   }

   public static String getExceptionActivateUnpreparedRA(String var0) {
      return ConnectorLogger.getExceptionActivateUnpreparedRALoggable(var0).getMessageText();
   }

   public static String getExceptionActivateSuspendedRA(String var0) {
      return ConnectorLogger.getExceptionActivateSuspendedRALoggable(var0).getMessageText();
   }

   public static String getExceptionRollbackActivatedRA() {
      return ConnectorLogger.getExceptionRollbackActivatedRALoggable().getMessageText();
   }

   public static String getExceptionCreateNativeLib() {
      return ConnectorLogger.getExceptionCreateNativeLibLoggable().getMessageText();
   }

   public static String getExceptionBadRAClassSpec(String var0, String var1) {
      return ConnectorLogger.getExceptionBadRAClassSpecLoggable(var0, var1).getMessageText();
   }

   public static String getExceptionBadMCFClassSpec(String var0, String var1) {
      return ConnectorLogger.getExceptionBadMCFClassSpecLoggable(var0, var1).getMessageText();
   }

   public static String getExceptionAdapterNotVersionable() {
      return ConnectorLogger.getExceptionAdapterNotVersionableLoggable().getMessageText();
   }

   public static String getExceptionPopulateWorkManager() {
      return ConnectorLogger.getExceptionPopulateWorkManagerLoggable().getMessageText();
   }

   public static String getExceptionStartRA(String var0, String var1) {
      return ConnectorLogger.getExceptionStartRALoggable(var0, var1).getMessageText();
   }

   public static String getExceptionCreateBootstrap(String var0, String var1) {
      return ConnectorLogger.getExceptionCreateBootstrapLoggable(var0, var1).getMessageText();
   }

   public static String getExceptionVersionRA() {
      return ConnectorLogger.getExceptionVersionRALoggable().getMessageText();
   }

   public static String getExceptionWorkRuntimer() {
      return ConnectorLogger.getExceptionWorkRuntimerLoggable().getMessageText();
   }

   public static String getExceptionIntrospectProperties(String var0) {
      return ConnectorLogger.getExceptionIntrospectPropertiesLoggable(var0).getMessageText();
   }

   public static String getExceptionSetterNotFound(String var0) {
      return ConnectorLogger.getExceptionSetterNotFoundLoggable(var0).getMessageText();
   }

   public static String getExceptionInvokeSetter(String var0) {
      return ConnectorLogger.getExceptionInvokeSetterLoggable(var0).getMessageText();
   }

   public static String getExceptionBadPropertyType(String var0) {
      return ConnectorLogger.getExceptionBadPropertyTypeLoggable(var0).getMessageText();
   }

   public static String getExceptionPropertyValueTypeMismatch(String var0, String var1, String var2, String var3) {
      return ConnectorLogger.getExceptionPropertyValueTypeMismatchLoggable(var0, var1, var2, var3).getMessageText();
   }

   public static String getExceptionLoginException(String var0, String var1, String var2) {
      return ConnectorLogger.getExceptionLoginExceptionLoggable(var0, var1, var2).getMessageText();
   }

   public static String getExceptionInitialCapacityMustBePositive() {
      return ConnectorLogger.getExceptionInitialCapacityMustBePositiveLoggable().getMessageText();
   }

   public static String getExceptionMaxCapacityZero() {
      return ConnectorLogger.getExceptionMaxCapacityZeroLoggable().getMessageText();
   }

   public static String getExceptionMaxCapacityNegative() {
      return ConnectorLogger.getExceptionMaxCapacityNegativeLoggable().getMessageText();
   }

   public static String getExceptionMaxCapacityLessThanInitialCapacity(String var0) {
      return ConnectorLogger.getExceptionMaxCapacityLessThanInitialCapacityLoggable(var0).getMessageText();
   }

   public static String getExceptionMaxCapacityIncrementMustBePositive() {
      return ConnectorLogger.getExceptionMaxCapacityIncrementMustBePositiveLoggable().getMessageText();
   }

   public static String getExceptionMaxCapacityTooHigh(String var0) {
      return ConnectorLogger.getExceptionMaxCapacityTooHighLoggable(var0).getMessageText();
   }

   public static String getExceptionShrinkFrequencySecondsMustBePositive() {
      return ConnectorLogger.getExceptionShrinkFrequencySecondsMustBePositiveLoggable().getMessageText();
   }

   public static String getExceptionInactiveConnectionTimeoutSecondsNegative() {
      return ConnectorLogger.getExceptionInactiveConnectionTimeoutSecondsNegativeLoggable().getMessageText();
   }

   public static String getExceptionNoDescriptorOrAltDD() {
      return ConnectorLogger.getExceptionNoDescriptorOrAltDDLoggable().getMessageText();
   }

   public static String getExceptionNoDescriptor() {
      return ConnectorLogger.getExceptionNoDescriptorLoggable().getMessageText();
   }

   public static String getExceptionMissingSchema() {
      return ConnectorLogger.getExceptionMissingSchemaLoggable().getMessageText();
   }

   public static String getExceptionNoComponents(String var0) {
      return ConnectorLogger.getExceptionNoComponentsLoggable(var0).getMessageText();
   }

   public static String getExceptionMoreThanOneComponent(String var0) {
      return ConnectorLogger.getExceptionMoreThanOneComponentLoggable(var0).getMessageText();
   }

   public static String getExceptionRollbackModuleFailed(String var0) {
      return ConnectorLogger.getExceptionRollbackModuleFailedLoggable(var0).getMessageText();
   }

   public static String getExceptionCreateRuntimeMBeanForConnectorModuleFailed(String var0, String var1) {
      return ConnectorLogger.getExceptionCreateRuntimeMBeanForConnectorModuleFailedLoggable(var0, var1).getMessageText();
   }

   public static String getExceptionCloseVJarFailed(String var0, String var1) {
      return ConnectorLogger.getExceptionCloseVJarFailedLoggable(var0, var1).getMessageText();
   }

   public static String getExceptionCreateVJarFailed(String var0, String var1) {
      return ConnectorLogger.getExceptionCreateVJarFailedLoggable(var0, var1).getMessageText();
   }

   public static String getExceptionInitializeJndiSubcontextsFailed(String var0, String var1) {
      return ConnectorLogger.getExceptionInitializeJndiSubcontextsFailedLoggable(var0, var1).getMessageText();
   }

   public static String getExceptionPrepareUpdateFailed(String var0, String var1) {
      return ConnectorLogger.getExceptionPrepareUpdateFailedLoggable(var0, var1).getMessageText();
   }

   public static String getExceptionJndiNameNull() {
      return ConnectorLogger.getExceptionJndiNameNullLoggable().getMessageText();
   }

   public static String getExceptionNoInitialContextForJndi() {
      return ConnectorLogger.getExceptionNoInitialContextForJndiLoggable().getMessageText();
   }

   public static String getExceptionResourceLinkNull() {
      return ConnectorLogger.getExceptionResourceLinkNullLoggable().getMessageText();
   }

   public static String getExceptionNoInitialContextForResourceLink() {
      return ConnectorLogger.getExceptionNoInitialContextForResourceLinkLoggable().getMessageText();
   }

   public static String getExceptionJndiNameAlreadyBound(String var0) {
      return ConnectorLogger.getExceptionAlreadyDeployedLoggable(var0).getMessageText();
   }

   public static String getExceptionAlreadyDeployed(String var0) {
      return ConnectorLogger.getExceptionAlreadyDeployedLoggable(var0).getMessageText();
   }

   public static String getExceptionBindingFailed(String var0, String var1) {
      return ConnectorLogger.getExceptionBindingFailedLoggable(var0, var1).getMessageText();
   }

   public static String getExceptionUnbindFailed(String var0, String var1) {
      return ConnectorLogger.getExceptionUnbindFailedLoggable(var0, var1).getMessageText();
   }

   public static String getExceptionNoInitialContextForUnbind(String var0) {
      return ConnectorLogger.getExceptionNoInitialContextForUnbindLoggable(var0).getMessageText();
   }

   public static String getExceptionResourceLinkAlreadyBound(String var0) {
      return ConnectorLogger.getExceptionResourceLinkAlreadyBoundLoggable(var0).getMessageText();
   }

   public static String getExceptionAppScopedBindFailed(String var0, String var1, String var2) {
      return ConnectorLogger.getExceptionAppScopedBindFailedLoggable(var0, var1, var2).getMessageText();
   }

   public static String getExceptionUnbindAdminObjectFailed(String var0) {
      return ConnectorLogger.getExceptionUnbindAdminObjectFailedLoggable(var0).getMessageText();
   }

   public static String getExceptionGetConnectionFactoryFailed(String var0) {
      return ConnectorLogger.getExceptionGetConnectionFactoryFailedLoggable(var0).getMessageText();
   }

   public static String getExceptionRANotDeployed(String var0) {
      return ConnectorLogger.getExceptionRANotDeployedLoggable(var0).getMessageText();
   }

   public static String getExceptionInitializeActivationSpecFailed(String var0) {
      return ConnectorLogger.getExceptionInitializeActivationSpecFailedLoggable(var0).getMessageText();
   }

   public static String getExceptionInstantiateClassFailed(String var0, String var1) {
      return ConnectorLogger.getExceptionInstantiateClassFailedLoggable(var0, var1).getMessageText();
   }

   public static String getExceptionBadValue(String var0, String var1, String var2) {
      return ConnectorLogger.getExceptionBadValueLoggable(var0, var1, var2).getMessageText();
   }

   public static String getExceptionRANotActive(String var0) {
      return ConnectorLogger.getExceptionRANotActiveLoggable(var0).getMessageText();
   }

   public static String getExceptionRANotFound(String var0) {
      return ConnectorLogger.getExceptionRANotFoundLoggable(var0).getMessageText();
   }

   public static String getExceptionNoMessageListener(String var0, String var1) {
      return ConnectorLogger.getExceptionNoMessageListenerLoggable(var0, var1).getMessageText();
   }

   public static String getExceptionMissingRequiredProperty(String var0) {
      return ConnectorLogger.getExceptionMissingRequiredPropertyLoggable(var0).getMessageText();
   }

   public static String getExceptionNoInboundRAElement() {
      return ConnectorLogger.getExceptionNoInboundRAElementLoggable().getMessageText();
   }

   public static String getExceptionNoMessageAdapterElement() {
      return ConnectorLogger.getExceptionNoMessageAdapterElementLoggable().getMessageText();
   }

   public static String getExceptionNoMessageListenerElement() {
      return ConnectorLogger.getExceptionNoMessageListenerElementLoggable().getMessageText();
   }

   public static String getExceptionAssertionError(String var0) {
      return ConnectorLogger.getExceptionAssertionErrorLoggable(var0).getMessageText();
   }

   public static String getExceptionSetDyeBitsFailedDiagCtxNotEnabled() {
      return ConnectorLogger.getExceptionSetDyeBitsFailedDiagCtxNotEnabledLoggable().getMessageText();
   }

   public static String getExceptionInvalidDyeValue(String var0) {
      return ConnectorLogger.getExceptionInvalidDyeValueLoggable(var0).getMessageText();
   }

   public static String getExceptionFailedToGetDiagCtx(String var0) {
      return ConnectorLogger.getExceptionFailedToGetDiagCtxLoggable(var0).getMessageText();
   }

   public static String getExceptionGetDyeBitsFailedDiagCtxNotEnabled() {
      return ConnectorLogger.getExceptionGetDyeBitsFailedDiagCtxNotEnabledLoggable().getMessageText();
   }

   public static String getExceptionInvalidDye(String var0, String var1) {
      return ConnectorLogger.getExceptionInvalidDyeLoggable(var0, var1).getMessageText();
   }

   public static String getExceptionCannotDeleteConnection() {
      return ConnectorLogger.getExceptionCannotDeleteConnectionLoggable().getMessageText();
   }

   public static String getExceptionEnlistmentFailed(String var0, String var1) {
      return ConnectorLogger.getExceptionEnlistmentFailedLoggable(var0, var1).getMessageText();
   }

   public static String getExceptionMCGetXAResourceReturnedNull() {
      return ConnectorLogger.getExceptionMCGetXAResourceReturnedNullLoggable().getMessageText();
   }

   public static String getExceptionMCGetXAResourceThrewNonResourceException(String var0) {
      return ConnectorLogger.getExceptionMCGetXAResourceThrewNonResourceExceptionLoggable(var0).getMessageText();
   }

   public static String getExceptionMCFCreateManagedConnectionReturnedNull() {
      return ConnectorLogger.getExceptionMCFCreateManagedConnectionReturnedNullLoggable().getMessageText();
   }

   public static String getExceptionInitializeForRecoveryFailed(String var0) {
      return ConnectorLogger.getExceptionInitializeForRecoveryFailedLoggable(var0).getMessageText();
   }

   public static String getExceptionEnlistResourceIllegalType() {
      return ConnectorLogger.getExceptionEnlistResourceIllegalTypeLoggable().getMessageText();
   }

   public static String getExceptionRegisterResourceIllegalType(String var0) {
      return ConnectorLogger.getExceptionRegisterResourceIllegalTypeLoggable(var0).getMessageText();
   }

   public static String getExceptionXAStartInLocalTxIllegal() {
      return ConnectorLogger.getExceptionXAStartInLocalTxIllegalLoggable().getMessageText();
   }

   public static String getExceptionMCGetLocalTransactionThrewNonResourceException(String var0, String var1) {
      return ConnectorLogger.getExceptionMCGetLocalTransactionThrewNonResourceExceptionLoggable(var0, var1).getMessageText();
   }

   public static String getExceptionMCGetLocalTransactionReturnedNull(String var0) {
      return ConnectorLogger.getExceptionMCGetLocalTransactionReturnedNullLoggable(var0).getMessageText();
   }

   public static String getExceptionRegisterNonXAFailed(String var0) {
      return ConnectorLogger.getExceptionRegisterNonXAFailedLoggable(var0).getMessageText();
   }

   public static String getExceptionCommitFailed(String var0, String var1) {
      return ConnectorLogger.getExceptionCommitFailedLoggable(var0, var1).getMessageText();
   }

   public static String getExceptionRollbackFailed(String var0, String var1) {
      return ConnectorLogger.getExceptionRollbackFailedLoggable(var0, var1).getMessageText();
   }

   public static String getExceptionCreateMCFailed(String var0) {
      return ConnectorLogger.getExceptionCreateMCFailedLoggable(var0).getMessageText();
   }

   public static String getExceptionFailedMCSetup() {
      return ConnectorLogger.getExceptionFailedMCSetupLoggable().getMessageText();
   }

   public static String getExceptionObjectIdNull() {
      return ConnectorLogger.getExceptionObjectIdNullLoggable().getMessageText();
   }

   public static String getExceptionMCGetConnectionReturnedNull(String var0) {
      return ConnectorLogger.getExceptionMCGetConnectionReturnedNullLoggable(var0).getMessageText();
   }

   public static String getExceptionDuplicateHandle() {
      return ConnectorLogger.getExceptionDuplicateHandleLoggable().getMessageText();
   }

   public static String getExceptionTestResourceException(String var0) {
      return ConnectorLogger.getExceptionTestResourceExceptionLoggable(var0).getMessageText();
   }

   public static String getExceptionTestNonResourceException(String var0) {
      return ConnectorLogger.getExceptionTestNonResourceExceptionLoggable(var0).getMessageText();
   }

   public static String getExceptionMCFNotImplementValidatingMCF() {
      return ConnectorLogger.getExceptionMCFNotImplementValidatingMCFLoggable().getMessageText();
   }

   public static String getLazyEnlistNullMC(String var0) {
      return ConnectorLogger.getLazyEnlistNullMCLoggable(var0).getMessageText();
   }

   public static String getExceptionRAAccessDenied(String var0) {
      return ConnectorLogger.getExceptionRAAccessDeniedLoggable(var0).getMessageText();
   }

   public static String getExceptionGetConnectionFailed(String var0, String var1) {
      return ConnectorLogger.getExceptionGetConnectionFailedLoggable(var0, var1).getMessageText();
   }

   public static String getExceptionPoolDisabled(String var0) {
      return ConnectorLogger.getExceptionPoolDisabledLoggable(var0).getMessageText();
   }

   public static String getExceptionMCFCreateCFReturnedNull() {
      return ConnectorLogger.getExceptionMCFCreateCFReturnedNullLoggable().getMessageText();
   }

   public static String getExceptionStackTrace() {
      return ConnectorLogger.getExceptionStackTraceLoggable().getMessageText();
   }

   public static String getExceptionLocalTxNotSupported() {
      return ConnectorLogger.getExceptionLocalTxNotSupportedLoggable().getMessageText();
   }

   public static String getExceptionHandleNotSet() {
      return ConnectorLogger.getExceptionHandleNotSetLoggable().getMessageText();
   }

   public static String getExceptionOutboundPrepareFailed(String var0, String var1) {
      return ConnectorLogger.getExceptionOutboundPrepareFailedLoggable(var0, var1).getMessageText();
   }

   public static String getExceptionResumePoolFailed(String var0) {
      return ConnectorLogger.getExceptionResumePoolFailedLoggable(var0).getMessageText();
   }

   public static String getExceptionActivatePoolFailed(String var0) {
      return ConnectorLogger.getExceptionActivatePoolFailedLoggable(var0).getMessageText();
   }

   public static String getExceptionJndiBindFailed(String var0, String var1) {
      return ConnectorLogger.getExceptionJndiBindFailedLoggable(var0, var1).getMessageText();
   }

   public static String getExceptionDeactivateException(String var0, String var1, String var2) {
      return ConnectorLogger.getExceptionDeactivateExceptionLoggable(var0, var1, var2).getMessageText();
   }

   public static String getExceptionShutdownException(String var0, String var1, String var2) {
      return ConnectorLogger.getExceptionShutdownExceptionLoggable(var0, var1, var2).getMessageText();
   }

   public static String getExceptionCFJndiNameDuplicate(String var0) {
      return ConnectorLogger.getExceptionCFJndiNameDuplicateLoggable(var0).getMessageText();
   }

   public static String getExceptionCFResourceLinkDuplicate(String var0) {
      return ConnectorLogger.getExceptionCFResourceLinkDuplicateLoggable(var0).getMessageText();
   }

   public static String getExceptionJndiVerifyFailed(String var0, String var1) {
      return ConnectorLogger.getExceptionJndiVerifyFailedLoggable(var0, var1).getMessageText();
   }

   public static String getExceptionMCFNoImplementResourceAdapterAssociation(String var0) {
      return ConnectorLogger.getExceptionMCFNoImplementResourceAdapterAssociationLoggable(var0).getMessageText();
   }

   public static String getExceptionSetRAClassFailed(String var0, String var1) {
      return ConnectorLogger.getExceptionSetRAClassFailedLoggable(var0, var1).getMessageText();
   }

   public static String getExceptionMCFUnexpectedException(String var0, String var1) {
      return ConnectorLogger.getExceptionMCFUnexpectedExceptionLoggable(var0, var1).getMessageText();
   }

   public static String getExceptionMCFClassNotFound(String var0, String var1) {
      return ConnectorLogger.getExceptionMCFClassNotFoundLoggable(var0, var1).getMessageText();
   }

   public static String getExceptionInstantiateMCFFailed(String var0, String var1) {
      return ConnectorLogger.getExceptionInstantiateMCFFailedLoggable(var0, var1).getMessageText();
   }

   public static String getExceptionAccessMCFFailed(String var0, String var1) {
      return ConnectorLogger.getExceptionAccessMCFFailedLoggable(var0, var1).getMessageText();
   }

   public static String getExceptionGetConnectionFactoryFailedInternalError(String var0) {
      return ConnectorLogger.getExceptionGetConnectionFactoryFailedInternalErrorLoggable(var0).getMessageText();
   }

   public static String getExceptionFailedAccessOutsideApp() {
      return ConnectorLogger.getExceptionFailedAccessOutsideAppLoggable().getMessageText();
   }

   public static String getExceptionNotImplemented(String var0, String var1) {
      return ConnectorLogger.getExceptionNotImplementedLoggable(var0, var1).getMessageText();
   }

   public static String getExceptionMustBeLinkRef() {
      return ConnectorLogger.getExceptionMustBeLinkRefLoggable().getMessageText();
   }

   public static String getExceptionNeedsRAXML() {
      return ConnectorLogger.getExceptionNeedsRAXMLLoggable().getMessageText();
   }

   public static String getExceptionErrorCreatingNativeLibDir(String var0) {
      return ConnectorLogger.getExceptionErrorCreatingNativeLibDirLoggable(var0).getMessageText();
   }

   public static String getExceptionFileNotFoundForNativeLibDir(String var0) {
      return ConnectorLogger.getExceptionFileNotFoundForNativeLibDirLoggable(var0).getMessageText();
   }

   public static String getExceptionExceptionCreatingNativeLibDir(String var0, String var1) {
      return ConnectorLogger.getExceptionExceptionCreatingNativeLibDirLoggable(var0, var1).getMessageText();
   }

   public static String getExceptionStartPoolFailed(String var0) {
      return ConnectorLogger.getExceptionStartPoolFailedLoggable(var0).getMessageText();
   }

   public static String getExceptionTestFrequencyNonZero() {
      return ConnectorLogger.getExceptionTestFrequencyNonZeroLoggable().getMessageText();
   }

   public static String getExceptionInvalidTestingConfig() {
      return ConnectorLogger.getExceptionInvalidTestingConfigLoggable().getMessageText();
   }

   public static String getExceptionWorkIsNull() {
      return ConnectorLogger.getExceptionWorkIsNullLoggable().getMessageText();
   }

   public static String getExceptionDoWorkNotAccepted() {
      return ConnectorLogger.getExceptionDoWorkNotAcceptedLoggable().getMessageText();
   }

   public static String getExceptionWorkManagerSuspended() {
      return ConnectorLogger.getExceptionWorkManagerSuspendedLoggable().getMessageText();
   }

   public static String getExceptionSetExecutionContextFailed(String var0) {
      return ConnectorLogger.getExceptionSetExecutionContextFailedLoggable(var0).getMessageText();
   }

   public static String getExceptionInvalidGid(String var0) {
      return ConnectorLogger.getExceptionInvalidGidLoggable(var0).getMessageText();
   }

   public static String getExceptionGidNotRegistered(String var0) {
      return ConnectorLogger.getExceptionGidNotRegisteredLoggable(var0).getMessageText();
   }

   public static String getExceptionSecurityPrincipalMapNotSupported() {
      return ConnectorLogger.getExceptionSecurityPrincipalMapNotSupportedLoggable().getMessageText();
   }

   public static String getExceptionImportedTxAlreadyActive(String var0) {
      return ConnectorLogger.getExceptionImportedTxAlreadyActiveLoggable(var0).getMessageText();
   }

   public static String getTestConnectionsOnCreateTrue() {
      return ConnectorLogger.getTestConnectionsOnCreateTrue();
   }

   public static String getTestConnectionsOnReleaseTrue() {
      return ConnectorLogger.getTestConnectionsOnReleaseTrue();
   }

   public static String getTestConnectionsOnReserveTrue() {
      return ConnectorLogger.getTestConnectionsOnReserveTrue();
   }

   public static String getFailedToForceLogRotation(String var0) {
      return ConnectorLogger.getFailedToForceLogRotation(var0);
   }

   public static String getFailedToGetCF(String var0, String var1) {
      return ConnectorLogger.getFailedToGetCF(var0, var1);
   }

   public static String getDeploySecurityBumpUpFailed(String var0, String var1, String var2) {
      return ConnectorLogger.getDeploySecurityBumpUpFailed(var0, var1, var2);
   }

   private static String spaces() {
      return indentLevel > 0 ? "".substring(0, Math.min(indentLevel * 2, MAX_SPACES)) : new String();
   }

   public static void setVerbose(boolean var0) {
      verbose = true;
      String var1 = var0 ? "true" : "off";
      println("___Debug=" + var1);
      verbose = var0;
      Properties var2 = System.getProperties();
      var2.put("Debug", var1);
      System.setProperties(var2);
   }

   public static void setVerbose(String var0, boolean var1) {
      boolean var2 = verbose;
      String var3 = var1 ? "true" : "false";
      Properties var4 = System.getProperties();
      var4.put("Debug" + var0, var3);
      System.setProperties(var4);
      verbose = true;
      println("___Debug" + var0 + "=" + var3);
      verbose = var2;
   }

   public static boolean getVerbose(Object var0) {
      String var1 = null;
      String var2 = null;
      String var3;
      if (var0 instanceof String) {
         var3 = (String)var0;
         int var4 = var3.lastIndexOf(46);
         if (var4 == -1) {
            var2 = var3;
         } else {
            var1 = var3.substring(0, var4);
            var2 = var3.substring(var4 + 1);
         }
      } else {
         var2 = var0.getClass().getName();
         Package var5 = var0.getClass().getPackage();
         if (var5 != null) {
            var1 = var5.getName();
         }
      }

      var3 = "Debug" + var2;
      if (var1 != null) {
         String var6 = "Debug" + var1;
         return verbose || System.getProperty(var6) != null && !System.getProperty(var6).equalsIgnoreCase("false") || System.getProperty(var3) != null && !System.getProperty(var3).equalsIgnoreCase("false");
      } else {
         return verbose || System.getProperty(var3) != null && !System.getProperty(var3).equalsIgnoreCase("false");
      }
   }

   public static boolean getVerbose() {
      return verbose;
   }

   public static boolean isRALifecycleEnabled() {
      return ralifecycle().isDebugEnabled();
   }

   public static boolean isPoolVerboseEnabled() {
      return poolverbose().isDebugEnabled();
   }

   public static boolean isXAinEnabled() {
      return xain().isDebugEnabled();
   }

   public static boolean isXAoutEnabled() {
      return xaout().isDebugEnabled();
   }

   public static boolean isXAworkEnabled() {
      return xawork().isDebugEnabled();
   }

   public static boolean isLocalOutEnabled() {
      return localout().isDebugEnabled();
   }

   public static boolean isConnectorServiceEnabled() {
      return service().isDebugEnabled();
   }

   public static boolean isDeploymentEnabled() {
      return deployment().isDebugEnabled();
   }

   public static boolean isSecurityCtxEnabled() {
      return securityctx().isDebugEnabled();
   }

   public static boolean isParsingEnabled() {
      return parsing().isDebugEnabled();
   }

   public static boolean isPoolingEnabled() {
      return pooling().isDebugEnabled();
   }

   public static boolean isConnectionsEnabled() {
      return connections().isDebugEnabled();
   }

   public static boolean isConnEventsEnabled() {
      return connevents().isDebugEnabled();
   }

   public static boolean isWorkEnabled() {
      return work().isDebugEnabled();
   }

   public static boolean isWorkEventsEnabled() {
      return workevents().isDebugEnabled();
   }

   public static boolean isClassLoadingEnabled() {
      return classloading().isDebugEnabled();
   }

   public static void throwAssertionError(String var0) {
      String var1 = getExceptionAssertionError(var0);
      AssertionError var2 = new AssertionError(var1);
      logAssertionError(var0, var2);
      throw var2;
   }

   public static void throwAssertionError(String var0, Throwable var1) {
      String var2 = getExceptionAssertionError(var0);
      AssertionError var3 = new AssertionError(var2);
      var3.initCause(var1);
      logAssertionError(var0, var3);
      throw var3;
   }

   private static String getClassName(Object var0) {
      String var1 = null;
      if (var0 instanceof String) {
         var1 = (String)var0;
      } else {
         var1 = var0.getClass().getName();
      }

      int var2 = var1.lastIndexOf(46);
      return var2 > -1 ? var1.substring(var2 + 1) : var1;
   }
}
