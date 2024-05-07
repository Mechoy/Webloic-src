package weblogic.jms;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;

public class BridgeLogger {
   private static final String LOCALIZER_CLASS = "weblogic.jms.BridgeLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(BridgeLogger.class.getName());
   }

   public static String logBridgeFailedInit() {
      Object[] var0 = new Object[0];
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200000", var0, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200000";
   }

   public static String logBridgeShutdown() {
      Object[] var0 = new Object[0];
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200001", var0, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200001";
   }

   public static String logErrorCreateBridgeWhenShutdown(String var0) {
      Object[] var1 = new Object[]{var0};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200002", var1, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200002";
   }

   public static String logErrorCreateBridge(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200003", var2, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200003";
   }

   public static String logBridgeDeployed(String var0) {
      Object[] var1 = new Object[]{var0};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200004", var1, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200004";
   }

   public static String logErrorStartBridge(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200005", var2, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200005";
   }

   public static String logErrorNoSource(String var0) {
      Object[] var1 = new Object[]{var0};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200008", var1, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200008";
   }

   public static String logErrorNoTarget(String var0) {
      Object[] var1 = new Object[]{var0};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200009", var1, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200009";
   }

   public static String logWarningAdapterNotFound(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200011", var2, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200011";
   }

   public static String logInfoAdaptersFound(String var0) {
      Object[] var1 = new Object[]{var0};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200012", var1, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200012";
   }

   public static String logErrorInvalidSourceProps(String var0) {
      Object[] var1 = new Object[]{var0};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200013", var1, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200013";
   }

   public static String logErrorInvalidTargetProps(String var0) {
      Object[] var1 = new Object[]{var0};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200014", var1, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200014";
   }

   public static String logErrorProcessMsgs(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200015", var2, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200015";
   }

   public static String logInfoStopped(String var0) {
      Object[] var1 = new Object[]{var0};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200020", var1, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200020";
   }

   public static String logInfoAdaptersLookupFailed(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200021", var2, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200021";
   }

   public static String logErrorFailGetAdpInfo(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200022", var2, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200022";
   }

   public static String logInfoShuttingdown(String var0) {
      Object[] var1 = new Object[]{var0};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200024", var1, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200024";
   }

   public static String logErrorQOSNotAvail(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200025", var2, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200025";
   }

   public static String logInfoReconnect(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200026", var2, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200026";
   }

   public static String logInfoAsyncReconnect(String var0) {
      Object[] var1 = new Object[]{var0};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200027", var1, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200027";
   }

   public static String logInfoBeginForwaring(String var0) {
      Object[] var1 = new Object[]{var0};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200028", var1, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200028";
   }

   public static String logInfoWorkMode(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200030", var3, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200030";
   }

   public static String logInfoQOSDegradationAllowed(String var0) {
      Object[] var1 = new Object[]{var0};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200031", var1, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200031";
   }

   public static String logInfoQOSDegradationNotAllowed(String var0) {
      Object[] var1 = new Object[]{var0};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200032", var1, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200032";
   }

   public static String logInfoGetConnections(String var0) {
      Object[] var1 = new Object[]{var0};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200033", var1, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200033";
   }

   public static String logInfoShutdown(String var0) {
      Object[] var1 = new Object[]{var0};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200034", var1, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200034";
   }

   public static String logInfoSyncReconnect(String var0) {
      Object[] var1 = new Object[]{var0};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200035", var1, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200035";
   }

   public static String logInfoAttributeStartedChanged(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200036", var3, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200036";
   }

   public static String logInfoAttributeChanged(String var0, String var1, long var2, long var4) {
      Object[] var6 = new Object[]{var0, var1, new Long(var2), new Long(var4)};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200037", var6, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200037";
   }

   public static String logInfoInitiallyStopped(String var0) {
      Object[] var1 = new Object[]{var0};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200038", var1, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200038";
   }

   public static String logErrorSameSourceTarget(String var0) {
      Object[] var1 = new Object[]{var0};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200039", var1, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200039";
   }

   public static String logErrorInvalidURL(String var0) {
      Object[] var1 = new Object[]{var0};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200040", var1, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200040";
   }

   public static String logErrorNeedsJNDINames(String var0) {
      Object[] var1 = new Object[]{var0};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200041", var1, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200041";
   }

   public static String logErrorFailedToConnectToSource(String var0, Exception var1, long var2) {
      Object[] var4 = new Object[]{var0, var1, new Long(var2)};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200042", var4, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200042";
   }

   public static String logErrorFailedToConnectToTarget(String var0, Exception var1, long var2) {
      Object[] var4 = new Object[]{var0, var1, new Long(var2)};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200043", var4, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200043";
   }

   public static String logStackTrace(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200044", var1, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200044";
   }

   public static String logFailedStart(String var0) {
      Object[] var1 = new Object[]{var0};
      BridgeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("200045", var1, "weblogic.jms.BridgeLogLocalizer", BridgeLogger.class.getClassLoader()));
      return "200045";
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = BridgeLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = BridgeLogger.findMessageLogger();
      }
   }
}
