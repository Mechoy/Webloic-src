package weblogic.cluster;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;

public class ClusterExtensionLogger {
   private static final String LOCALIZER_CLASS = "weblogic.cluster.ClusterExtensionLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(ClusterExtensionLogger.class.getName());
   }

   public static String logUpdatingNonDynamicPropertyOnAdminServer(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterExtensionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("003101", var1, "weblogic.cluster.ClusterExtensionLogLocalizer", ClusterExtensionLogger.class.getClassLoader()));
      return "003101";
   }

   public static String logPostDeactivationScriptFailure(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ClusterExtensionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("003102", var2, "weblogic.cluster.ClusterExtensionLogLocalizer", ClusterExtensionLogger.class.getClassLoader()));
      return "003102";
   }

   public static String logReleaseLeaseError(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      ClusterExtensionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("003103", var2, "weblogic.cluster.ClusterExtensionLogLocalizer", ClusterExtensionLogger.class.getClassLoader()));
      return "003103";
   }

   public static String logFailedToNotifyPostScriptFailureToStateManager(String var0, String var1, Exception var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      ClusterExtensionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("003104", var3, "weblogic.cluster.ClusterExtensionLogLocalizer", ClusterExtensionLogger.class.getClassLoader()));
      return "003104";
   }

   public static String logAsyncReplicationRequestTimeout(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterExtensionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("003105", var1, "weblogic.cluster.ClusterExtensionLogLocalizer", ClusterExtensionLogger.class.getClassLoader()));
      return "003105";
   }

   public static String logUnexpectedExceptionDuringReplication(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      ClusterExtensionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("003106", var1, "weblogic.cluster.ClusterExtensionLogLocalizer", ClusterExtensionLogger.class.getClassLoader()));
      return "003106";
   }

   public static String logLostUnicastMessages(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      ClusterExtensionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("003107", var2, "weblogic.cluster.ClusterExtensionLogLocalizer", ClusterExtensionLogger.class.getClassLoader()));
      return "003107";
   }

   public static String logUnicastReceiveError(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      ClusterExtensionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("003108", var1, "weblogic.cluster.ClusterExtensionLogLocalizer", ClusterExtensionLogger.class.getClassLoader()));
      return "003108";
   }

   public static String logDataSourceForDatabaseLeasingNotSet(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterExtensionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("003109", var1, "weblogic.cluster.ClusterExtensionLogLocalizer", ClusterExtensionLogger.class.getClassLoader()));
      return "003109";
   }

   public static String logNoChannelForReplicationCalls(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterExtensionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("003111", var1, "weblogic.cluster.ClusterExtensionLogLocalizer", ClusterExtensionLogger.class.getClassLoader()));
      return "003111";
   }

   public static String logUsingMultipleChannelsForReplication(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterExtensionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("003112", var1, "weblogic.cluster.ClusterExtensionLogLocalizer", ClusterExtensionLogger.class.getClassLoader()));
      return "003112";
   }

   public static String logUsingOneWayRMIForReplication() {
      Object[] var0 = new Object[0];
      ClusterExtensionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("003113", var0, "weblogic.cluster.ClusterExtensionLogLocalizer", ClusterExtensionLogger.class.getClassLoader()));
      return "003113";
   }

   public static String logIgnoringOneWayRMIWithoutMultipleChannels() {
      Object[] var0 = new Object[0];
      ClusterExtensionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("003114", var0, "weblogic.cluster.ClusterExtensionLogLocalizer", ClusterExtensionLogger.class.getClassLoader()));
      return "003114";
   }

   public static String logOutOfOrderUpdateOneWayRequest() {
      Object[] var0 = new Object[0];
      ClusterExtensionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("003115", var0, "weblogic.cluster.ClusterExtensionLogLocalizer", ClusterExtensionLogger.class.getClassLoader()));
      return "003115";
   }

   public static String logStartingMemberDeathDetector() {
      Object[] var0 = new Object[0];
      ClusterExtensionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("003116", var0, "weblogic.cluster.ClusterExtensionLogLocalizer", ClusterExtensionLogger.class.getClassLoader()));
      return "003116";
   }

   public static String logStartingMemberDeathDetectorReceiver() {
      Object[] var0 = new Object[0];
      ClusterExtensionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("003117", var0, "weblogic.cluster.ClusterExtensionLogLocalizer", ClusterExtensionLogger.class.getClassLoader()));
      return "003117";
   }

   public static String logServerWithNoMachineConfigured(String var0) {
      Object[] var1 = new Object[]{var0};
      ClusterExtensionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("003118", var1, "weblogic.cluster.ClusterExtensionLogLocalizer", ClusterExtensionLogger.class.getClassLoader()));
      return "003118";
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = ClusterExtensionLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = ClusterExtensionLogger.findMessageLogger();
      }
   }
}
