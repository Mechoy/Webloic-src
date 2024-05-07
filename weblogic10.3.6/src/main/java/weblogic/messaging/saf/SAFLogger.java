package weblogic.messaging.saf;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class SAFLogger {
   private static final String LOCALIZER_CLASS = "weblogic.messaging.saf.SAFLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(SAFLogger.class.getName());
   }

   public static String logSAFStarted() {
      Object[] var0 = new Object[0];
      SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("281002", var0, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.class.getClassLoader()));
      return "281002";
   }

   public static Loggable logSAFStartedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("281002", var0, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SAFLogger.class.getClassLoader());
   }

   public static String logSAFInitialized() {
      Object[] var0 = new Object[0];
      SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("281003", var0, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.class.getClassLoader()));
      return "281003";
   }

   public static Loggable logSAFInitializedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("281003", var0, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SAFLogger.class.getClassLoader());
   }

   public static String logSAFSuspended() {
      Object[] var0 = new Object[0];
      SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("281004", var0, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.class.getClassLoader()));
      return "281004";
   }

   public static Loggable logSAFSuspendedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("281004", var0, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SAFLogger.class.getClassLoader());
   }

   public static String logSAFShutdown() {
      Object[] var0 = new Object[0];
      SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("281005", var0, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.class.getClassLoader()));
      return "281005";
   }

   public static Loggable logSAFShutdownLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("281005", var0, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SAFLogger.class.getClassLoader());
   }

   public static String logExpiredMessage(String var0) {
      Object[] var1 = new Object[]{var0};
      SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("281006", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.class.getClassLoader()));
      return "281006";
   }

   public static Loggable logExpiredMessageLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("281006", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SAFLogger.class.getClassLoader());
   }

   public static String logSAFAgentPrepared(String var0) {
      Object[] var1 = new Object[]{var0};
      SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("281007", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.class.getClassLoader()));
      return "281007";
   }

   public static Loggable logSAFAgentPreparedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("281007", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SAFLogger.class.getClassLoader());
   }

   public static String logSAFAgentActivated(String var0) {
      Object[] var1 = new Object[]{var0};
      SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("281008", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.class.getClassLoader()));
      return "281008";
   }

   public static Loggable logSAFAgentActivatedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("281008", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SAFLogger.class.getClassLoader());
   }

   public static String logSAFAgentDeactivated(String var0) {
      Object[] var1 = new Object[]{var0};
      SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("281009", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.class.getClassLoader()));
      return "281009";
   }

   public static Loggable logSAFAgentDeactivatedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("281009", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SAFLogger.class.getClassLoader());
   }

   public static String logSAFAgentUnprepared(String var0) {
      Object[] var1 = new Object[]{var0};
      SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("281010", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.class.getClassLoader()));
      return "281010";
   }

   public static Loggable logSAFAgentUnpreparedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("281010", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SAFLogger.class.getClassLoader());
   }

   public static String logErrorPrepareSAFAgent(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("281011", var2, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.class.getClassLoader()));
      return "281011";
   }

   public static Loggable logErrorPrepareSAFAgentLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("281011", var2, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SAFLogger.class.getClassLoader());
   }

   public static String logErrorStartSAFAgent(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("281012", var2, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.class.getClassLoader()));
      return "281012";
   }

   public static Loggable logErrorStartSAFAgentLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("281012", var2, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SAFLogger.class.getClassLoader());
   }

   public static String logBytesThresholdHighAgent(String var0) {
      Object[] var1 = new Object[]{var0};
      SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("281016", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.class.getClassLoader()));
      return "281016";
   }

   public static Loggable logBytesThresholdHighAgentLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("281016", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SAFLogger.class.getClassLoader());
   }

   public static String logBytesThresholdLowAgent(String var0) {
      Object[] var1 = new Object[]{var0};
      SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("281017", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.class.getClassLoader()));
      return "281017";
   }

   public static Loggable logBytesThresholdLowAgentLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("281017", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SAFLogger.class.getClassLoader());
   }

   public static String logMessagesThresholdHighAgent(String var0) {
      Object[] var1 = new Object[]{var0};
      SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("281018", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.class.getClassLoader()));
      return "281018";
   }

   public static Loggable logMessagesThresholdHighAgentLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("281018", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SAFLogger.class.getClassLoader());
   }

   public static String logMessagesThresholdLowAgent(String var0) {
      Object[] var1 = new Object[]{var0};
      SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("281019", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.class.getClassLoader()));
      return "281019";
   }

   public static Loggable logMessagesThresholdLowAgentLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("281019", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SAFLogger.class.getClassLoader());
   }

   public static String logSAFConnected(String var0) {
      Object[] var1 = new Object[]{var0};
      SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("281020", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.class.getClassLoader()));
      return "281020";
   }

   public static Loggable logSAFConnectedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("281020", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SAFLogger.class.getClassLoader());
   }

   public static String logSAFDisconnected(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("281021", var2, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.class.getClassLoader()));
      return "281021";
   }

   public static Loggable logSAFDisconnectedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("281021", var2, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SAFLogger.class.getClassLoader());
   }

   public static String logErrorResumeAgent(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("281022", var2, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.class.getClassLoader()));
      return "281022";
   }

   public static Loggable logErrorResumeAgentLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("281022", var2, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SAFLogger.class.getClassLoader());
   }

   public static String logIncomingPauseOfSAFAgent(String var0) {
      Object[] var1 = new Object[]{var0};
      SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("281025", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.class.getClassLoader()));
      return "281025";
   }

   public static Loggable logIncomingPauseOfSAFAgentLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("281025", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SAFLogger.class.getClassLoader());
   }

   public static String logIncomingResumeOfSAFAgent(String var0) {
      Object[] var1 = new Object[]{var0};
      SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("281026", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.class.getClassLoader()));
      return "281026";
   }

   public static Loggable logIncomingResumeOfSAFAgentLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("281026", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SAFLogger.class.getClassLoader());
   }

   public static String logForwardingPauseOfSAFAgent(String var0) {
      Object[] var1 = new Object[]{var0};
      SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("281027", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.class.getClassLoader()));
      return "281027";
   }

   public static Loggable logForwardingPauseOfSAFAgentLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("281027", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SAFLogger.class.getClassLoader());
   }

   public static String logForwardingResumeOfSAFAgent(String var0) {
      Object[] var1 = new Object[]{var0};
      SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("281028", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.class.getClassLoader()));
      return "281028";
   }

   public static Loggable logForwardingResumeOfSAFAgentLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("281028", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SAFLogger.class.getClassLoader());
   }

   public static String logIncomingPauseOfRemoteEndpoint(String var0) {
      Object[] var1 = new Object[]{var0};
      SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("281029", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.class.getClassLoader()));
      return "281029";
   }

   public static Loggable logIncomingPauseOfRemoteEndpointLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("281029", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SAFLogger.class.getClassLoader());
   }

   public static String logIncomingResumeOfRemoteEndpoint(String var0) {
      Object[] var1 = new Object[]{var0};
      SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("281030", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.class.getClassLoader()));
      return "281030";
   }

   public static Loggable logIncomingResumeOfRemoteEndpointLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("281030", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SAFLogger.class.getClassLoader());
   }

   public static String logForwardingPauseOfRemoteEndpoint(String var0) {
      Object[] var1 = new Object[]{var0};
      SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("281031", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.class.getClassLoader()));
      return "281031";
   }

   public static Loggable logForwardingPauseOfRemoteEndpointLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("281031", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SAFLogger.class.getClassLoader());
   }

   public static String logForwardingResumeOfRemoteEndpoint(String var0) {
      Object[] var1 = new Object[]{var0};
      SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("281032", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.class.getClassLoader()));
      return "281032";
   }

   public static Loggable logForwardingResumeOfRemoteEndpointLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("281032", var1, "weblogic.messaging.saf.SAFLogLocalizer", SAFLogger.MessageLoggerInitializer.INSTANCE.messageLogger, SAFLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = SAFLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = SAFLogger.findMessageLogger();
      }
   }
}
