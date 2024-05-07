package weblogic.common;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;

public class CommonLogger {
   private static final String LOCALIZER_CLASS = "weblogic.common.CommonLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(CommonLogger.class.getName());
   }

   public static String logCallbackFailed(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000600", var2, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000600";
   }

   public static String logEnabled(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000601", var2, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000601";
   }

   public static String logEnableFailed() {
      Object[] var0 = new Object[0];
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000602", var0, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000602";
   }

   public static String logDisabled() {
      Object[] var0 = new Object[0];
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000603", var0, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000603";
   }

   public static String logDisableFailed() {
      Object[] var0 = new Object[0];
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000604", var0, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000604";
   }

   public static String logLost() {
      Object[] var0 = new Object[0];
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000605", var0, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000605";
   }

   public static String logNoEcho(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000606", var1, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000606";
   }

   public static String logEcho() {
      Object[] var0 = new Object[0];
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000607", var0, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000607";
   }

   public static String logTick(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000608", var1, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000608";
   }

   public static String logErrorWhileServerShutdown(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000609", var1, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000609";
   }

   public static String logDebugMsg(String var0) {
      Object[] var1 = new Object[]{var0};
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000610", var1, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000610";
   }

   public static String logMaxUnavlReached(String var0, int var1) {
      Object[] var2 = new Object[]{var0, new Integer(var1)};
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000611", var2, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000611";
   }

   public static String logWarnTestingAllAvl(String var0, int var1) {
      Object[] var2 = new Object[]{var0, new Integer(var1)};
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000612", var2, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000612";
   }

   public static String logTestOnCreateEnabled(String var0) {
      Object[] var1 = new Object[]{var0};
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000613", var1, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000613";
   }

   public static String logTestOnCreateDisabled(String var0) {
      Object[] var1 = new Object[]{var0};
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000614", var1, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000614";
   }

   public static String logTestOnReserveDisabled(String var0) {
      Object[] var1 = new Object[]{var0};
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000615", var1, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000615";
   }

   public static String logTestOnReserveEnabled(String var0) {
      Object[] var1 = new Object[]{var0};
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000616", var1, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000616";
   }

   public static String logTestOnReleaseEnabled(String var0) {
      Object[] var1 = new Object[]{var0};
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000617", var1, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000617";
   }

   public static String logTestOnReleaseDisabled(String var0) {
      Object[] var1 = new Object[]{var0};
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000618", var1, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000618";
   }

   public static String logErrForcedRelease(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000619", var3, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000619";
   }

   public static String logForcedRelease(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000620", var2, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000620";
   }

   public static String logUnexpectedProblem(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000621", var1, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000621";
   }

   public static String logWarnShutdownRelease(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000622", var2, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000622";
   }

   public static String logAdjustedCapacityIncrement(String var0, int var1) {
      Object[] var2 = new Object[]{var0, new Integer(var1)};
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000623", var2, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000623";
   }

   public static String logAdjustedTestSeconds(String var0, int var1) {
      Object[] var2 = new Object[]{var0, new Integer(var1)};
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000624", var2, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000624";
   }

   public static String logNoTest(String var0) {
      Object[] var1 = new Object[]{var0};
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000625", var1, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000625";
   }

   public static String logTest(String var0, int var1) {
      Object[] var2 = new Object[]{var0, new Integer(var1)};
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000626", var2, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000626";
   }

   public static String logAdjustedMakeCount(String var0, int var1, int var2) {
      Object[] var3 = new Object[]{var0, new Integer(var1), new Integer(var2)};
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000627", var3, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000627";
   }

   public static String logResourcesMade(String var0, int var1, int var2, int var3) {
      Object[] var4 = new Object[]{var0, new Integer(var1), new Integer(var2), new Integer(var3)};
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000628", var4, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000628";
   }

   public static String logWarnReclaimIncomplete(String var0, int var1, int var2) {
      Object[] var3 = new Object[]{var0, new Integer(var1), new Integer(var2)};
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000629", var3, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000629";
   }

   public static String logPoolRetryFailure(String var0, int var1) {
      Object[] var2 = new Object[]{var0, new Integer(var1)};
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000630", var2, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000630";
   }

   public static String logWarnUnknownResRelease(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000631", var3, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000631";
   }

   public static String logShuttingDownIgnoringInUse(String var0, int var1) {
      Object[] var2 = new Object[]{var0, new Integer(var1)};
      CommonLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000632", var2, "weblogic.common.CommonLogLocalizer", CommonLogger.class.getClassLoader()));
      return "000632";
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = CommonLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = CommonLogger.findMessageLogger();
      }
   }
}
