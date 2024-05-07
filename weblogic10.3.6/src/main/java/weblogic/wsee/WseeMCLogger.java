package weblogic.wsee;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class WseeMCLogger {
   private static final String LOCALIZER_CLASS = "weblogic.wsee.WseeMCLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(WseeMCLogger.class.getName());
   }

   public static String logUnexpectedException(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("222500", var2, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.class.getClassLoader()));
      return "222500";
   }

   public static Loggable logUnexpectedExceptionLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("222500", var2, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeMCLogger.class.getClassLoader());
   }

   public static String logSOAPBodyException() {
      Object[] var0 = new Object[0];
      WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("222501", var0, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.class.getClassLoader()));
      return "222501";
   }

   public static Loggable logSOAPBodyExceptionLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("222501", var0, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeMCLogger.class.getClassLoader());
   }

   public static String logNoSOAPBody() {
      Object[] var0 = new Object[0];
      WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("222502", var0, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.class.getClassLoader()));
      return "222502";
   }

   public static Loggable logNoSOAPBodyLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("222502", var0, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeMCLogger.class.getClassLoader());
   }

   public static String logMCMsgException() {
      Object[] var0 = new Object[0];
      WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("222503", var0, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.class.getClassLoader()));
      return "222503";
   }

   public static Loggable logMCMsgExceptionLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("222503", var0, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeMCLogger.class.getClassLoader());
   }

   public static String logPolicyURIMismatch() {
      Object[] var0 = new Object[0];
      WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("222504", var0, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.class.getClassLoader()));
      return "222504";
   }

   public static Loggable logPolicyURIMismatchLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("222504", var0, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeMCLogger.class.getClassLoader());
   }

   public static String logMalformedReplyTo(String var0) {
      Object[] var1 = new Object[]{var0};
      WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("222505", var1, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.class.getClassLoader()));
      return "222505";
   }

   public static Loggable logMalformedReplyToLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("222505", var1, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeMCLogger.class.getClassLoader());
   }

   public static String logMalformedFaultTo(String var0) {
      Object[] var1 = new Object[]{var0};
      WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("222506", var1, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.class.getClassLoader()));
      return "222506";
   }

   public static Loggable logMalformedFaultToLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("222506", var1, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeMCLogger.class.getClassLoader());
   }

   public static String logNoPendingList(String var0) {
      Object[] var1 = new Object[]{var0};
      WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("222507", var1, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.class.getClassLoader()));
      return "222507";
   }

   public static Loggable logNoPendingListLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("222507", var1, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeMCLogger.class.getClassLoader());
   }

   public static String logCannotUseAsyncClientTransport() {
      Object[] var0 = new Object[0];
      WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("222508", var0, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.class.getClassLoader()));
      return "222508";
   }

   public static Loggable logCannotUseAsyncClientTransportLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("222508", var0, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeMCLogger.class.getClassLoader());
   }

   public static String logIllegalPollId(String var0) {
      Object[] var1 = new Object[]{var0};
      WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("222509", var1, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.class.getClassLoader()));
      return "222509";
   }

   public static Loggable logIllegalPollIdLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("222509", var1, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeMCLogger.class.getClassLoader());
   }

   public static String logIllegalPollState(String var0) {
      Object[] var1 = new Object[]{var0};
      WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("222510", var1, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.class.getClassLoader()));
      return "222510";
   }

   public static Loggable logIllegalPollStateLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("222510", var1, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeMCLogger.class.getClassLoader());
   }

   public static String logCannotResetTimer() {
      Object[] var0 = new Object[0];
      WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("222511", var0, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.class.getClassLoader()));
      return "222511";
   }

   public static Loggable logCannotResetTimerLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("222511", var0, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeMCLogger.class.getClassLoader());
   }

   public static String logUnexpectedResponse(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("222512", var2, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.class.getClassLoader()));
      return "222512";
   }

   public static Loggable logUnexpectedResponseLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("222512", var2, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeMCLogger.class.getClassLoader());
   }

   public static String logNoAddress() {
      Object[] var0 = new Object[0];
      WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("222513", var0, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.class.getClassLoader()));
      return "222513";
   }

   public static Loggable logNoAddressLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("222513", var0, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeMCLogger.class.getClassLoader());
   }

   public static String logFailedToBuildHeader(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("222514", var2, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.class.getClassLoader()));
      return "222514";
   }

   public static Loggable logFailedToBuildHeaderLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("222514", var2, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeMCLogger.class.getClassLoader());
   }

   public static String logClientIdentityNotProvided() {
      Object[] var0 = new Object[0];
      WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("222515", var0, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.class.getClassLoader()));
      return "222515";
   }

   public static Loggable logClientIdentityNotProvidedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("222515", var0, "weblogic.wsee.WseeMCLogLocalizer", WseeMCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeMCLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = WseeMCLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = WseeMCLogger.findMessageLogger();
      }
   }
}
