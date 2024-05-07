package weblogic.wsee;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class WseeRmLogger {
   private static final String LOCALIZER_CLASS = "weblogic.wsee.WseeRmLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(WseeRmLogger.class.getName());
   }

   public static String logUsingStaleSequence(String var0) {
      Object[] var1 = new Object[]{var0};
      WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224000", var1, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.class.getClassLoader()));
      return "224000";
   }

   public static Loggable logUsingStaleSequenceLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224000", var1, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeRmLogger.class.getClassLoader());
   }

   public static String logSourceSequenceNotReadyToSend() {
      Object[] var0 = new Object[0];
      WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224001", var0, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.class.getClassLoader()));
      return "224001";
   }

   public static Loggable logSourceSequenceNotReadyToSendLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("224001", var0, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeRmLogger.class.getClassLoader());
   }

   public static String logAttemptedSecondaryClientSideResponseFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224002", var2, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.class.getClassLoader()));
      return "224002";
   }

   public static Loggable logAttemptedSecondaryClientSideResponseFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224002", var2, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeRmLogger.class.getClassLoader());
   }

   public static String logPolicyProcessingFailedOnClient(Exception var0) {
      Object[] var1 = new Object[]{var0};
      WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224003", var1, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.class.getClassLoader()));
      return "224003";
   }

   public static Loggable logPolicyProcessingFailedOnClientLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224003", var1, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeRmLogger.class.getClassLoader());
   }

   public static String logPolicyProcessingFailedOnServer(Exception var0) {
      Object[] var1 = new Object[]{var0};
      WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224004", var1, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.class.getClassLoader()));
      return "224004";
   }

   public static Loggable logPolicyProcessingFailedOnServerLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224004", var1, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeRmLogger.class.getClassLoader());
   }

   public static String logNoCreateSequenceMessageIDOnCreateSequenceResponse(String var0) {
      Object[] var1 = new Object[]{var0};
      WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224005", var1, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.class.getClassLoader()));
      return "224005";
   }

   public static Loggable logNoCreateSequenceMessageIDOnCreateSequenceResponseLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224005", var1, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeRmLogger.class.getClassLoader());
   }

   public static String logGotAcceptWithNoOffer(String var0) {
      Object[] var1 = new Object[]{var0};
      WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224006", var1, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.class.getClassLoader()));
      return "224006";
   }

   public static Loggable logGotAcceptWithNoOfferLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224006", var1, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeRmLogger.class.getClassLoader());
   }

   public static String logNullSequenceID() {
      Object[] var0 = new Object[0];
      WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224007", var0, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.class.getClassLoader()));
      return "224007";
   }

   public static Loggable logNullSequenceIDLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("224007", var0, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeRmLogger.class.getClassLoader());
   }

   public static String logNullMessageID() {
      Object[] var0 = new Object[0];
      WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224008", var0, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.class.getClassLoader()));
      return "224008";
   }

   public static Loggable logNullMessageIDLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("224008", var0, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeRmLogger.class.getClassLoader());
   }

   public static String logAddingNullOrDuplicateSequence(String var0) {
      Object[] var1 = new Object[]{var0};
      WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224009", var1, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.class.getClassLoader()));
      return "224009";
   }

   public static Loggable logAddingNullOrDuplicateSequenceLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224009", var1, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeRmLogger.class.getClassLoader());
   }

   public static String logUpdatingNullOrUnknownSequence(String var0) {
      Object[] var1 = new Object[]{var0};
      WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224010", var1, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.class.getClassLoader()));
      return "224010";
   }

   public static Loggable logUpdatingNullOrUnknownSequenceLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224010", var1, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeRmLogger.class.getClassLoader());
   }

   public static String logCannotAcceptDestinationSideRequest() {
      Object[] var0 = new Object[0];
      WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224011", var0, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.class.getClassLoader()));
      return "224011";
   }

   public static Loggable logCannotAcceptDestinationSideRequestLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("224011", var0, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeRmLogger.class.getClassLoader());
   }

   public static String logUnknownSequence(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224012", var2, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.class.getClassLoader()));
      return "224012";
   }

   public static Loggable logUnknownSequenceLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224012", var2, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeRmLogger.class.getClassLoader());
   }

   public static String logWsrmClientNotEnabled() {
      Object[] var0 = new Object[0];
      WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224013", var0, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.class.getClassLoader()));
      return "224013";
   }

   public static Loggable logWsrmClientNotEnabledLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("224013", var0, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeRmLogger.class.getClassLoader());
   }

   public static String logCannotInteractWithPendingSequence(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224014", var2, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.class.getClassLoader()));
      return "224014";
   }

   public static Loggable logCannotInteractWithPendingSequenceLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224014", var2, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeRmLogger.class.getClassLoader());
   }

   public static String logRmVersionMismatch(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224015", var3, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.class.getClassLoader()));
      return "224015";
   }

   public static Loggable logRmVersionMismatchLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("224015", var3, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeRmLogger.class.getClassLoader());
   }

   public static String logUnexpectedException(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224016", var2, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.class.getClassLoader()));
      return "224016";
   }

   public static Loggable logUnexpectedExceptionLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224016", var2, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeRmLogger.class.getClassLoader());
   }

   public static String logClientIdentityNotProvided() {
      Object[] var0 = new Object[0];
      WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224017", var0, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.class.getClassLoader()));
      return "224017";
   }

   public static Loggable logClientIdentityNotProvidedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("224017", var0, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeRmLogger.class.getClassLoader());
   }

   public static String logNoErrorListenerProvided(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224018", var2, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.class.getClassLoader()));
      return "224018";
   }

   public static Loggable logNoErrorListenerProvidedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224018", var2, "weblogic.wsee.WseeRmLogLocalizer", WseeRmLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeRmLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = WseeRmLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = WseeRmLogger.findMessageLogger();
      }
   }
}
