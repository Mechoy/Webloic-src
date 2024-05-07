package weblogic.socket;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class SocketLogger {
   private static final String LOCALIZER_CLASS = "weblogic.socket.SocketLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(SocketLogger.class.getName());
   }

   public static String logDebug(String var0) {
      Object[] var1 = new Object[]{var0};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000400", var1, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000400";
   }

   public static Loggable logDebugLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("000400", var1, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logSocketQueueFull(Exception var0) {
      Object[] var1 = new Object[]{var0};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000401", var1, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000401";
   }

   public static Loggable logSocketQueueFullLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("000401", var1, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logSocketConfig(int var0, int var1) {
      Object[] var2 = new Object[]{new Integer(var0), new Integer(var1)};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000402", var2, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000402";
   }

   public static Loggable logSocketConfigLoggable(int var0, int var1) {
      Object[] var2 = new Object[]{new Integer(var0), new Integer(var1)};
      return new Loggable("000402", var2, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logIOException(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000403", var2, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000403";
   }

   public static Loggable logIOExceptionLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("000403", var2, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logThreadDeath(ThreadDeath var0) {
      Object[] var1 = new Object[]{var0};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000404", var1, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000404";
   }

   public static Loggable logThreadDeathLoggable(ThreadDeath var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("000404", var1, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logThrowable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000405", var1, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000405";
   }

   public static Loggable logThrowableLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("000405", var1, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logTimeStamp(String var0) {
      Object[] var1 = new Object[]{var0};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000406", var1, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000406";
   }

   public static Loggable logTimeStampLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("000406", var1, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logRegisterSocketProblem(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000409", var2, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000409";
   }

   public static Loggable logRegisterSocketProblemLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("000409", var2, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logInitPerf() {
      Object[] var0 = new Object[0];
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000414", var0, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000414";
   }

   public static Loggable logInitPerfLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("000414", var0, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logFdLimit(int var0, int var1) {
      Object[] var2 = new Object[]{new Integer(var0), new Integer(var1)};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000415", var2, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000415";
   }

   public static Loggable logFdLimitLoggable(int var0, int var1) {
      Object[] var2 = new Object[]{new Integer(var0), new Integer(var1)};
      return new Loggable("000415", var2, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logFdCurrent(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000416", var1, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000416";
   }

   public static Loggable logFdCurrentLoggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("000416", var1, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logUncaughtThrowable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000421", var1, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000421";
   }

   public static Loggable logUncaughtThrowableLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("000421", var1, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logMuxerError(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000429", var2, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000429";
   }

   public static Loggable logMuxerErrorLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("000429", var2, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logDebugException(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000430", var2, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000430";
   }

   public static Loggable logDebugExceptionLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("000430", var2, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logInfoAcceptConnection(boolean var0, String var1, int var2, String var3, int var4, String var5) {
      Object[] var6 = new Object[]{var0, var1, new Integer(var2), var3, new Integer(var4), var5};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000431", var6, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000431";
   }

   public static Loggable logInfoAcceptConnectionLoggable(boolean var0, String var1, int var2, String var3, int var4, String var5) {
      Object[] var6 = new Object[]{var0, var1, new Integer(var2), var3, new Integer(var4), var5};
      return new Loggable("000431", var6, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logNativeMuxerError(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000432", var1, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000432";
   }

   public static Loggable logNativeMuxerErrorLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("000432", var1, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logPosixMuxerMaxFdExceededError(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000435", var1, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000435";
   }

   public static Loggable logPosixMuxerMaxFdExceededErrorLoggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("000435", var1, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logAllocSocketReaders(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000436", var1, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000436";
   }

   public static Loggable logAllocSocketReadersLoggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("000436", var1, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logMuxerUnsatisfiedLinkError(String var0) {
      Object[] var1 = new Object[]{var0};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000438", var1, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000438";
   }

   public static Loggable logMuxerUnsatisfiedLinkErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("000438", var1, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logJavaMuxerCreationError2() {
      Object[] var0 = new Object[0];
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000439", var0, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000439";
   }

   public static Loggable logJavaMuxerCreationError2Loggable() {
      Object[] var0 = new Object[0];
      return new Loggable("000439", var0, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logNTMuxerInitiateIOError(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000440", var2, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000440";
   }

   public static Loggable logNTMuxerInitiateIOErrorLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("000440", var2, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logNTMuxerSocketInfoNotFound(String var0, boolean var1) {
      Object[] var2 = new Object[]{var0, var1};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000441", var2, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000441";
   }

   public static Loggable logNTMuxerSocketInfoNotFoundLoggable(String var0, boolean var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("000441", var2, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logConnectionRejected(String var0) {
      Object[] var1 = new Object[]{var0};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000442", var1, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000442";
   }

   public static Loggable logConnectionRejectedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("000442", var1, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logConnectionRejectedProtocol(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000443", var2, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000443";
   }

   public static Loggable logConnectionRejectedProtocolLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("000443", var2, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logNativeDevPollMuxerError(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000444", var1, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000444";
   }

   public static Loggable logNativeDevPollMuxerErrorLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("000444", var1, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logConnectionRejectedFilterEx(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000445", var2, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000445";
   }

   public static Loggable logConnectionRejectedFilterExLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("000445", var2, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logNativeIOEnabled() {
      Object[] var0 = new Object[0];
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000446", var0, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000446";
   }

   public static Loggable logNativeIOEnabledLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("000446", var0, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logNativeIODisabled() {
      Object[] var0 = new Object[0];
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000447", var0, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000447";
   }

   public static Loggable logNativeIODisabledLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("000447", var0, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logNoSocketChannelSupportForVM() {
      Object[] var0 = new Object[0];
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000448", var0, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000448";
   }

   public static Loggable logNoSocketChannelSupportForVMLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("000448", var0, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logSocketIdleTimeout(long var0, String var2, int var3) {
      Object[] var4 = new Object[]{new Long(var0), var2, new Integer(var3)};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000449", var4, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000449";
   }

   public static Loggable logSocketIdleTimeoutLoggable(long var0, String var2, int var3) {
      Object[] var4 = new Object[]{new Long(var0), var2, new Integer(var3)};
      return new Loggable("000449", var4, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logSocketInfoNotFound(int var0, int var1) {
      Object[] var2 = new Object[]{new Integer(var0), new Integer(var1)};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000450", var2, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000450";
   }

   public static Loggable logSocketInfoNotFoundLoggable(int var0, int var1) {
      Object[] var2 = new Object[]{new Integer(var0), new Integer(var1)};
      return new Loggable("000450", var2, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logONSSocketEndOfStream(String var0) {
      Object[] var1 = new Object[]{var0};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000451", var1, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000451";
   }

   public static Loggable logONSSocketEndOfStreamLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("000451", var1, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logONSSocketHasException(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000452", var2, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000452";
   }

   public static Loggable logONSSocketHasExceptionLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("000452", var2, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logONSDeliverMessageException(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000453", var2, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000453";
   }

   public static Loggable logONSDeliverMessageExceptionLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("000453", var2, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logONSHandShakeException(String var0, int var1, Exception var2) {
      Object[] var3 = new Object[]{var0, new Integer(var1), var2};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000454", var3, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000454";
   }

   public static Loggable logONSHandShakeExceptionLoggable(String var0, int var1, Exception var2) {
      Object[] var3 = new Object[]{var0, new Integer(var1), var2};
      return new Loggable("000454", var3, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   public static String logUnregisteredHandshakeCompletedListener(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000455", var2, "weblogic.socket.SocketLogLocalizer", SocketLogger.class.getClassLoader()));
      return "000455";
   }

   public static Loggable logUnregisteredHandshakeCompletedListenerLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("000455", var2, "weblogic.socket.SocketLogLocalizer", SocketLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SocketLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = SocketLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = SocketLogger.findMessageLogger();
      }
   }
}
