package weblogic.cluster;

import java.io.IOException;
import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.i18n.logging.MessageResetScheduler;

public class ClusterLogger {
   private static final String LOCALIZER_CLASS = "weblogic.cluster.ClusterLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(ClusterLogger.class.getName());
   }

   public static String logCannotResolveClusterAddressWarning(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000101", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000101";
   }

   public static String logJoinedCluster(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000102", var3, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000102";
   }

   public static String logLeavingCluster(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000103", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000103";
   }

   public static String logIncompatibleVersionsError(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000104", var3, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000104";
   }

   public static String logIncompatibleServerLeavingCluster() {
      Object[] var0 = new Object[0];
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000105", var0, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000105";
   }

   public static String logOfferReplacementError(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000107", var2, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000107";
   }

   public static String logRetractUnrecognizedOfferError(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000108", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000108";
   }

   public static String logMulticastSendError(IOException var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000109", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000109";
   }

   public static String logMulticastReceiveError(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000110", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000110";
   }

   public static String logAddingServer(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000111", var3, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000111";
   }

   public static String logRemovingServerDueToTimeout(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000112", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000112";
   }

   public static String logRemovingServerDueToPeerGone(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000113", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000113";
   }

   public static String logNoClusterLicenseError(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000114", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000114";
   }

   public static String logLostMulticastMessages(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000115", var2, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000115";
   }

   public static String logFailedToJoinClusterError(String var0, String var1, Exception var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000116", var3, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000116";
   }

   public static String logStaleReplicationRequest(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000117", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000117";
   }

   public static String logReplicationVersionMismatch(int var0, String var1) {
      Object[] var2 = new Object[]{new Integer(var0), var1};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000118", var2, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000118";
   }

   public static String logMissingClusterMulticastAddressError(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000119", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000119";
   }

   public static String logErrorCreatingClusterRuntime(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000120", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000120";
   }

   public static String logMultipleDomainsCannotUseSameMulticastAddress(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000121", var2, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000121";
   }

   public static String logMultipleClustersCannotUseSameMulticastAddress(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000122", var2, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000122";
   }

   public static String logConflictStartNonClusterableObject(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000123", var2, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000123";
   }

   public static String logConflictStartInCompatibleClusterableObject(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000124", var2, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000124";
   }

   public static String logConflictStop(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000125", var2, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000125";
   }

   public static String logUnableToUpdateNonSerializableObject(Exception var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000126", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000126";
   }

   public static String logNewServerJoinedCluster(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000127", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000127";
   }

   public static String logUpdatingServerInTheCluster(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000128", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000128";
   }

   public static String logRemovingServerFromCluster(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000129", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000129";
   }

   public static String logStartWarmup(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000133", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000133";
   }

   public static String logMulticastSendErrorMsg(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000137", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000137";
   }

   public static String logListeningToCluster(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000138", var3, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000138";
   }

   public static String logMulticastAddressCollision(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000139", var3, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000139";
   }

   public static String logFailedToDeserializeStateDump(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000140", var2, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000140";
   }

   public static String logFailedWhileReceivingStateDump(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000141", var2, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000141";
   }

   public static String logFetchClusterStateDump(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000142", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000142";
   }

   public static String logFetchServerStateDump(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000143", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000143";
   }

   public static String logServerSuspended(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000144", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000144";
   }

   public static String logMissingJDBCConfigurationForAutoMigration(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000145", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000145";
   }

   public static String logServerFailedtoRenewLease(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000147", var2, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000147";
   }

   public static String logMigratableServerNotTargetToAMachine(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000148", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000148";
   }

   public static String logDatabaseUnreachable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000149", var2, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000149";
   }

   public static String logDatabaseUnreachableForLeaseRenewal(int var0, String var1) {
      Object[] var2 = new Object[]{new Integer(var0), var1};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000150", var2, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000150";
   }

   public static String logClusterMasterElected(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000151", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000151";
   }

   public static String logRevokeClusterMasterRole(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000152", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000152";
   }

   public static String logMisconfiguredMigratableCluster() {
      Object[] var0 = new Object[0];
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000153", var0, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000153";
   }

   public static String logIncorrectRemoteClusterAddress(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000154", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000154";
   }

   public static String logInvalidConfiguredClusterAddress(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000155", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000155";
   }

   public static String logFailureUpdatingServerInTheCluster(String var0, IOException var1) {
      Object[] var2 = new Object[]{var0, var1};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000156", var2, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000156";
   }

   public static String logOutboundClusterServiceStopped() {
      Object[] var0 = new Object[0];
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000158", var0, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000158";
   }

   public static String logMachineTimesOutOfSync(String var0, long var1) {
      Object[] var3 = new Object[]{var0, new Long(var1)};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000159", var3, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000159";
   }

   public static String logEnforceSecureRequest() {
      Object[] var0 = new Object[0];
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000160", var0, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000160";
   }

   public static String logMessageDigestInvalid(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000161", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000161";
   }

   public static String logStartingReplicationService(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000162", var2, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000162";
   }

   public static String logStoppingReplicationService(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000163", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000163";
   }

   public static String logFetchClusterStateDumpComplete(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000164", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000164";
   }

   public static String logMultipleDomainsCannotUseSameMulticastAddress2(String var0) {
      if (MessageResetScheduler.getInstance().isMessageLoggingDisabled("000165")) {
         return "000165";
      } else {
         Object[] var1 = new Object[]{var0};
         ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000165", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
         MessageResetScheduler.getInstance().scheduleMessageReset("000165", 120000L);
         return "000165";
      }
   }

   public static void resetlogMultipleDomainsCannotUseSameMulticastAddress2() {
      MessageResetScheduler.getInstance().resetLogMessage("000165");
   }

   public static String logMultipleClustersCannotUseSameMulticastAddress2(String var0) {
      if (MessageResetScheduler.getInstance().isMessageLoggingDisabled("000166")) {
         return "000166";
      } else {
         Object[] var1 = new Object[]{var0};
         ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000166", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
         MessageResetScheduler.getInstance().scheduleMessageReset("000166", 120000L);
         return "000166";
      }
   }

   public static void resetlogMultipleClustersCannotUseSameMulticastAddress2() {
      MessageResetScheduler.getInstance().resetLogMessage("000166");
   }

   public static String logFailedToDeactivateMigratableServicesDuringRollback(Exception var0) {
      if (MessageResetScheduler.getInstance().isMessageLoggingDisabled("000167")) {
         return "000167";
      } else {
         Object[] var1 = new Object[]{var0};
         ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000167", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
         MessageResetScheduler.getInstance().scheduleMessageReset("000167", 120000L);
         return "000167";
      }
   }

   public static void resetlogFailedToDeactivateMigratableServicesDuringRollback() {
      MessageResetScheduler.getInstance().resetLogMessage("000167");
   }

   public static String logFailedToAutomaticallyMigrateServers2(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000168", var2, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000168";
   }

   public static String logMessageCannotReceiveOwnMessages(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000170", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000170";
   }

   public static String logUnableToLoadCustomQueryHelper(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000171", var2, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000171";
   }

   public static String logFailedWhileReceivingStateDumpWithMessage(String var0, Exception var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000172", var4, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000172";
   }

   public static String logMissingMachine(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000176", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000176";
   }

   public static String logScriptFailed(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000178", var2, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000178";
   }

   public static String logLeasingError(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000179", var2, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000179";
   }

   public static String logInvalidTimerState(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000180", var2, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000180";
   }

   public static String logDebug(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000181", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000181";
   }

   public static String logCreatedJob(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000182", var2, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000182";
   }

   public static String logCancelledJob(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000183", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000183";
   }

   public static String logDelayedLeaseRenewal(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000184", var2, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000184";
   }

   public static String logLeaseRenewedAfterDelay() {
      Object[] var0 = new Object[0];
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000185", var0, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000185";
   }

   public static String logExceptionWhileMigratingService(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000186", var2, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000186";
   }

   public static String logRegisteredSingletonService(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000187", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000187";
   }

   public static String logUnregisteredSingletonService(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000188", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000188";
   }

   public static String logActivatedSingletonService(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000189", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000189";
   }

   public static String logDeactivatedSingletonService(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000190", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000190";
   }

   public static String logMonitoringMigratableServer(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000191", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000191";
   }

   public static String logNoSuitableServerFoundForSingletonService(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000192", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000192";
   }

   public static String logAttemptedJTAMigrationFromLivingServer(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000193", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000193";
   }

   public static String logErrorReportingMigrationRuntimeInfo(Exception var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000194", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000194";
   }

   public static String logErrorUpdatingMigrationRuntimeInfo(Exception var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000195", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000195";
   }

   public static String logUnknownMigrationDataType(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000196", var1, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000196";
   }

   public static String logUnicastEnabled() {
      Object[] var0 = new Object[0];
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000197", var0, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000197";
   }

   public static String logWrongChannelForReplicationCalls(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000198", var2, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000198";
   }

   public static String logWrongPriviledgesForReplicationCalls(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ClusterLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000199", var2, "weblogic.cluster.ClusterLogLocalizer", ClusterLogger.class.getClassLoader()));
      return "000199";
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = ClusterLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = ClusterLogger.findMessageLogger();
      }
   }
}
