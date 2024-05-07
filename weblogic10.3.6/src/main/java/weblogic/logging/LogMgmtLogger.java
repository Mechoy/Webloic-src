package weblogic.logging;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;

public class LogMgmtLogger {
   private static final String LOCALIZER_CLASS = "weblogic.logging.LogMgmtLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(LogMgmtLogger.class.getName());
   }

   public static String logCannotOpenDomainLogfile(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      LogMgmtLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("170003", var2, "weblogic.logging.LogMgmtLogLocalizer", LogMgmtLogger.class.getClassLoader()));
      return "170003";
   }

   public static Loggable logCannotOpenDomainLogfileLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("170003", var2, "weblogic.logging.LogMgmtLogLocalizer", LogMgmtLogger.MessageLoggerInitializer.INSTANCE.messageLogger, LogMgmtLogger.class.getClassLoader());
   }

   public static String logCannotGetDomainLogHandler(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      LogMgmtLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("170011", var1, "weblogic.logging.LogMgmtLogLocalizer", LogMgmtLogger.class.getClassLoader()));
      return "170011";
   }

   public static Loggable logCannotGetDomainLogHandlerLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("170011", var1, "weblogic.logging.LogMgmtLogLocalizer", LogMgmtLogger.MessageLoggerInitializer.INSTANCE.messageLogger, LogMgmtLogger.class.getClassLoader());
   }

   public static String logServerLogFileOpened(String var0) {
      Object[] var1 = new Object[]{var0};
      LogMgmtLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("170019", var1, "weblogic.logging.LogMgmtLogLocalizer", LogMgmtLogger.class.getClassLoader()));
      return "170019";
   }

   public static Loggable logServerLogFileOpenedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("170019", var1, "weblogic.logging.LogMgmtLogLocalizer", LogMgmtLogger.MessageLoggerInitializer.INSTANCE.messageLogger, LogMgmtLogger.class.getClassLoader());
   }

   public static String logErrorOpeningLogFile(String var0) {
      Object[] var1 = new Object[]{var0};
      LogMgmtLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("170020", var1, "weblogic.logging.LogMgmtLogLocalizer", LogMgmtLogger.class.getClassLoader()));
      return "170020";
   }

   public static Loggable logErrorOpeningLogFileLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("170020", var1, "weblogic.logging.LogMgmtLogLocalizer", LogMgmtLogger.MessageLoggerInitializer.INSTANCE.messageLogger, LogMgmtLogger.class.getClassLoader());
   }

   public static String logErrorInitializingLog4jLogging(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      LogMgmtLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("170022", var2, "weblogic.logging.LogMgmtLogLocalizer", LogMgmtLogger.class.getClassLoader()));
      return "170022";
   }

   public static Loggable logErrorInitializingLog4jLoggingLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("170022", var2, "weblogic.logging.LogMgmtLogLocalizer", LogMgmtLogger.MessageLoggerInitializer.INSTANCE.messageLogger, LogMgmtLogger.class.getClassLoader());
   }

   public static String logDefaultServerLoggingInitialized() {
      Object[] var0 = new Object[0];
      LogMgmtLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("170023", var0, "weblogic.logging.LogMgmtLogLocalizer", LogMgmtLogger.class.getClassLoader()));
      return "170023";
   }

   public static Loggable logDefaultServerLoggingInitializedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("170023", var0, "weblogic.logging.LogMgmtLogLocalizer", LogMgmtLogger.MessageLoggerInitializer.INSTANCE.messageLogger, LogMgmtLogger.class.getClassLoader());
   }

   public static String logLog4jServerLoggingInitialized() {
      Object[] var0 = new Object[0];
      LogMgmtLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("170024", var0, "weblogic.logging.LogMgmtLogLocalizer", LogMgmtLogger.class.getClassLoader()));
      return "170024";
   }

   public static Loggable logLog4jServerLoggingInitializedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("170024", var0, "weblogic.logging.LogMgmtLogLocalizer", LogMgmtLogger.MessageLoggerInitializer.INSTANCE.messageLogger, LogMgmtLogger.class.getClassLoader());
   }

   public static String logInitializedDomainLogFile(String var0) {
      Object[] var1 = new Object[]{var0};
      LogMgmtLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("170025", var1, "weblogic.logging.LogMgmtLogLocalizer", LogMgmtLogger.class.getClassLoader()));
      return "170025";
   }

   public static Loggable logInitializedDomainLogFileLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("170025", var1, "weblogic.logging.LogMgmtLogLocalizer", LogMgmtLogger.MessageLoggerInitializer.INSTANCE.messageLogger, LogMgmtLogger.class.getClassLoader());
   }

   public static String logDomainLogHandlerInitialized() {
      Object[] var0 = new Object[0];
      LogMgmtLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("170027", var0, "weblogic.logging.LogMgmtLogLocalizer", LogMgmtLogger.class.getClassLoader()));
      return "170027";
   }

   public static Loggable logDomainLogHandlerInitializedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("170027", var0, "weblogic.logging.LogMgmtLogLocalizer", LogMgmtLogger.MessageLoggerInitializer.INSTANCE.messageLogger, LogMgmtLogger.class.getClassLoader());
   }

   public static String logDomainLogHandlerNotAvailableForTrap() {
      Object[] var0 = new Object[0];
      LogMgmtLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("170028", var0, "weblogic.logging.LogMgmtLogLocalizer", LogMgmtLogger.class.getClassLoader()));
      return "170028";
   }

   public static Loggable logDomainLogHandlerNotAvailableForTrapLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("170028", var0, "weblogic.logging.LogMgmtLogLocalizer", LogMgmtLogger.MessageLoggerInitializer.INSTANCE.messageLogger, LogMgmtLogger.class.getClassLoader());
   }

   public static String logErrorInitializingDataGatheringHandler(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      LogMgmtLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("170029", var1, "weblogic.logging.LogMgmtLogLocalizer", LogMgmtLogger.class.getClassLoader()));
      return "170029";
   }

   public static Loggable logErrorInitializingDataGatheringHandlerLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("170029", var1, "weblogic.logging.LogMgmtLogLocalizer", LogMgmtLogger.MessageLoggerInitializer.INSTANCE.messageLogger, LogMgmtLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = LogMgmtLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = LogMgmtLogger.findMessageLogger();
      }
   }
}
