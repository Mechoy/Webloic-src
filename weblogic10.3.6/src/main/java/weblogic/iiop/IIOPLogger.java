package weblogic.iiop;

import java.io.IOException;
import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.i18n.logging.MessageResetScheduler;

public class IIOPLogger {
   private static final String LOCALIZER_CLASS = "weblogic.iiop.IIOPLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(IIOPLogger.class.getName());
   }

   public static String logGarbageMessage() {
      if (MessageResetScheduler.getInstance().isMessageLoggingDisabled("002001")) {
         return "002001";
      } else {
         Object[] var0 = new Object[0];
         IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002001", var0, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
         MessageResetScheduler.getInstance().scheduleMessageReset("002001", 5000L);
         return "002001";
      }
   }

   public static void resetlogGarbageMessage() {
      MessageResetScheduler.getInstance().resetLogMessage("002001");
   }

   public static String logMethodParseFailure(String var0) {
      Object[] var1 = new Object[]{var0};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002002", var1, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002002";
   }

   public static String logSendFailure(IOException var0) {
      Object[] var1 = new Object[]{var0};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002003", var1, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002003";
   }

   public static String logOutOfMemory(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002005", var1, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002005";
   }

   public static String logScavengeCreateFailure(Exception var0) {
      Object[] var1 = new Object[]{var0};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002006", var1, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002006";
   }

   public static String logExceptionSending(IOException var0) {
      Object[] var1 = new Object[]{var0};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002024", var1, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002024";
   }

   public static String logExceptionReceiving(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002025", var1, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002025";
   }

   public static String logFailedToExport(String var0, IOException var1) {
      Object[] var2 = new Object[]{var0, var1};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002008", var2, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002008";
   }

   public static String logMarshalExceptionFailure(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002011", var2, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002011";
   }

   public static String logSendExceptionFailed(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002012", var2, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002012";
   }

   public static String logSendExceptionCompletelyFailed(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002013", var2, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002013";
   }

   public static String logEnabled() {
      Object[] var0 = new Object[0];
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002014", var0, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002014";
   }

   public static String logUtilClassNotInstalled(String var0) {
      Object[] var1 = new Object[]{var0};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002015", var1, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002015";
   }

   public static String logPROClassNotInstalled(String var0) {
      Object[] var1 = new Object[]{var0};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002016", var1, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002016";
   }

   public static String logLocateRequest(String var0) {
      Object[] var1 = new Object[]{var0};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002017", var1, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002017";
   }

   public static String logGIOPVersion(byte var0) {
      Object[] var1 = new Object[]{new Byte(var0)};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002018", var1, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002018";
   }

   public static String logLocationForwardPolicy(String var0) {
      Object[] var1 = new Object[]{var0};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002019", var1, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002019";
   }

   public static String logSecurityService(String var0) {
      Object[] var1 = new Object[]{var0};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002021", var1, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002021";
   }

   public static String logSecurityServiceFailed(Exception var0) {
      Object[] var1 = new Object[]{var0};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002022", var1, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002022";
   }

   public static String logBadRuntime(Exception var0) {
      Object[] var1 = new Object[]{var0};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002023", var1, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002023";
   }

   public static String logJTAEnabled(String var0) {
      Object[] var1 = new Object[]{var0};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002026", var1, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002026";
   }

   public static String logCompleteMarshalExceptionFailure(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002027", var2, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002027";
   }

   public static String logOTSError(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002028", var2, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002028";
   }

   public static String logDebugOTS(String var0) {
      Object[] var1 = new Object[]{var0};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002029", var1, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002029";
   }

   public static String logDebugTransport(String var0) {
      Object[] var1 = new Object[]{var0};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002030", var1, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002030";
   }

   public static String logDebugMarshal(String var0) {
      Object[] var1 = new Object[]{var0};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002031", var1, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002031";
   }

   public static String logDebugMarshalError(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002032", var2, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002032";
   }

   public static String logCodeSet(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002033", var3, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002033";
   }

   public static String logDebugNaming(String var0) {
      Object[] var1 = new Object[]{var0};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002034", var1, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002034";
   }

   public static String logDebugSecurity(String var0) {
      Object[] var1 = new Object[]{var0};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002035", var1, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002035";
   }

   public static String logDebugReplacer(String var0) {
      Object[] var1 = new Object[]{var0};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002036", var1, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002036";
   }

   public static String logCosNamingService(String var0) {
      Object[] var1 = new Object[]{var0};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002037", var1, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002037";
   }

   public static String logCosNamingServiceFailed(Exception var0) {
      Object[] var1 = new Object[]{var0};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002038", var1, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002038";
   }

   public static String logReplacerFailure(String var0) {
      Object[] var1 = new Object[]{var0};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002039", var1, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002039";
   }

   public static String logConnectionRejected() {
      Object[] var0 = new Object[0];
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002041", var0, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002041";
   }

   public static String logDebugConnection(String var0) {
      Object[] var1 = new Object[]{var0};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002042", var1, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002042";
   }

   public static String logNamingException(Exception var0) {
      Object[] var1 = new Object[]{var0};
      IIOPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("002043", var1, "weblogic.iiop.IIOPLogLocalizer", IIOPLogger.class.getClassLoader()));
      return "002043";
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = IIOPLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = IIOPLogger.findMessageLogger();
      }
   }
}
