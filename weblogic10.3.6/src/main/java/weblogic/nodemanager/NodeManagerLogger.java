package weblogic.nodemanager;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class NodeManagerLogger {
   private static final String LOCALIZER_CLASS = "weblogic.nodemanager.NodeManagerLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(NodeManagerLogger.class.getName());
   }

   public static String logDebugMsg(String var0) {
      Object[] var1 = new Object[]{var0};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300000", var1, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300000";
   }

   public static String logMsg(String var0) {
      Object[] var1 = new Object[]{var0};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("309999", var1, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "309999";
   }

   public static String logErrorNoServerName(String var0) {
      Object[] var1 = new Object[]{var0};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300001", var1, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300001";
   }

   public static String logErrorNoDomainName(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300002", var2, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300002";
   }

   public static String logErrorNotAdminSvr(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300004", var2, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300004";
   }

   public static String logErrorAdminSvrStart(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300005", var2, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300005";
   }

   public static String logErrorAdminSvrGetLogs(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300006", var2, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300006";
   }

   public static String logErrorAdminSvrMonitor(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300007", var2, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300007";
   }

   public static String logErrorNoAdminPort(String var0) {
      Object[] var1 = new Object[]{var0};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300009", var1, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300009";
   }

   public static String logErrorNoMachine(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300010", var2, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300010";
   }

   public static String logErrorNotViaNM(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300011", var2, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300011";
   }

   public static String logErrorNoNM(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300012", var3, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300012";
   }

   public static String logErrorNoNMNotViaNM(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300013", var3, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300013";
   }

   public static String logErrorNoAddr(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300014", var2, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300014";
   }

   public static String logErrorNoPort(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300015", var2, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300015";
   }

   public static String logErrorNoStart(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300016", var2, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300016";
   }

   public static String logErrorNoUser(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300017", var2, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300017";
   }

   public static String logErrorNoSvrAddr(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300018", var2, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300018";
   }

   public static String logErrorNoSvrPort(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300019", var2, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300019";
   }

   public static String logErrorLogType(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300020", var2, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300020";
   }

   public static String logErrorFileCreate(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300021", var4, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300021";
   }

   public static String logErrorInternal(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300022", var2, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300022";
   }

   public static String logErrorReply() {
      Object[] var0 = new Object[0];
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300026", var0, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300026";
   }

   public static String logErrorInvalidCmd() {
      Object[] var0 = new Object[0];
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300027", var0, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300027";
   }

   public static String logErrorAuthFailed(String var0) {
      Object[] var1 = new Object[]{var0};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300028", var1, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300028";
   }

   public static String logErrorActionFailed(String var0) {
      Object[] var1 = new Object[]{var0};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300029", var1, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300029";
   }

   public static String logErrorCloseFailed(String var0) {
      Object[] var1 = new Object[]{var0};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300030", var1, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300030";
   }

   public static String logDebugServerRunningMsg(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300031", var2, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300031";
   }

   public static String logErrorNMCmdFailedFileCreate(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300032", var3, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300032";
   }

   public static String logNMCmdFailedReason(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300033", var2, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300033";
   }

   public static String logSvrCmdFailedReason(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300034", var3, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300034";
   }

   public static String logWarningMsg(String var0) {
      Object[] var1 = new Object[]{var0};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300035", var1, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300035";
   }

   public static String logErrorMsg(String var0) {
      Object[] var1 = new Object[]{var0};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300036", var1, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300036";
   }

   public static String logNMNotRunning(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300037", var2, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300037";
   }

   public static String logNoSSLConnToNM(String var0) {
      Object[] var1 = new Object[]{var0};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300038", var1, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300038";
   }

   public static String logRegistrationFailedMessage(String var0) {
      Object[] var1 = new Object[]{var0};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300039", var1, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300039";
   }

   public static String logStateChangeNotificationFailureMsg(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300040", var1, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300040";
   }

   public static String logStartupModeNotificationRegFailure(String var0) {
      Object[] var1 = new Object[]{var0};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300041", var1, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300041";
   }

   public static String logStartupRequestCannotBeCancelled(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300042", var2, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300042";
   }

   public static String logNativePidSupportUnavailable() {
      Object[] var0 = new Object[0];
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300043", var0, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300043";
   }

   public static String logErrorWritingPidFile(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300044", var2, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300044";
   }

   public static String logWarningNMCmdFailedIOError(String var0) {
      Object[] var1 = new Object[]{var0};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300045", var1, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300045";
   }

   public static String logWarningSvrCmdFailedIOError(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300046", var2, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300046";
   }

   public static String logDebugMsgWithException(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300047", var2, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300047";
   }

   public static String logServerStartFailure(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300048", var2, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300048";
   }

   public static String logErrorWritingURLFile(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300049", var2, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300049";
   }

   public static String logErrorUpdatingServerProps(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300051", var2, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300051";
   }

   public static String getInvalidScriptNameMsg(String var0) {
      Object[] var1 = new Object[]{var0};
      return (new Loggable("300052", var1, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, NodeManagerLogger.class.getClassLoader())).getMessage();
   }

   public static String logNoIPFoundForMigratableServer() {
      Object[] var0 = new Object[0];
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300053", var0, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300053";
   }

   public static String logUnknownMigratableListenAddress(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      NodeManagerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("300054", var2, "weblogic.nodemanager.NodeManagerLogLocalizer", NodeManagerLogger.class.getClassLoader()));
      return "300054";
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = NodeManagerLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = NodeManagerLogger.findMessageLogger();
      }
   }
}
