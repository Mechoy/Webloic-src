package weblogic.jms;

import java.io.IOException;
import java.rmi.RemoteException;
import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class JMSClientExceptionLogger {
   private static final String LOCALIZER_CLASS = "weblogic.jms.JMSClientExceptionLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(JMSClientExceptionLogger.class.getName());
   }

   public static String logInvalidTimeToDeliver() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055001", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055001";
   }

   public static Loggable logInvalidTimeToDeliverLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055001", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logConvertBoolean(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055002", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055002";
   }

   public static Loggable logConvertBooleanLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055002", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logNullByte() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055003", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055003";
   }

   public static Loggable logNullByteLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055003", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logConvertByte(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055004", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055004";
   }

   public static Loggable logConvertByteLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055004", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logNullShort() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055005", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055005";
   }

   public static Loggable logNullShortLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055005", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logConvertShort(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055006", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055006";
   }

   public static Loggable logConvertShortLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055006", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logSerializationError() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055007", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055007";
   }

   public static Loggable logSerializationErrorLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055007", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidRedeliveryLimit() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055008", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055008";
   }

   public static Loggable logInvalidRedeliveryLimitLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055008", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidSendTimeout() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055009", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055009";
   }

   public static Loggable logInvalidSendTimeoutLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055009", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logErrorSendingMessage() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055014", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055014";
   }

   public static Loggable logErrorSendingMessageLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055014", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidDeliveryMode() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055015", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055015";
   }

   public static Loggable logInvalidDeliveryModeLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055015", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidPriority() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055016", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055016";
   }

   public static Loggable logInvalidPriorityLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055016", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logUnsupportedSubscription() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055017", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055017";
   }

   public static Loggable logUnsupportedSubscriptionLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055017", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logNoMulticastOnQueueSessions() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055018", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055018";
   }

   public static Loggable logNoMulticastOnQueueSessionsLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055018", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logDuplicateSession() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055019", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055019";
   }

   public static Loggable logDuplicateSessionLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055019", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logNullClientID() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055021", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055021";
   }

   public static Loggable logNullClientIDLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055021", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logZeroClientID() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055022", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055022";
   }

   public static Loggable logZeroClientIDLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055022", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidMessagesMaximum(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055023", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055023";
   }

   public static Loggable logInvalidMessagesMaximumLoggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("055023", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logNoSuchMethod(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055024", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055024";
   }

   public static Loggable logNoSuchMethodLoggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("055024", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidMessagesMaximumValue() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055025", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055025";
   }

   public static Loggable logInvalidMessagesMaximumValueLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055025", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidOverrunPolicy(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055026", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055026";
   }

   public static Loggable logInvalidOverrunPolicyLoggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("055026", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidRedeliveryDelay() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055027", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055027";
   }

   public static Loggable logInvalidRedeliveryDelayLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055027", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logNoSubscriberName() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055028", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055028";
   }

   public static Loggable logNoSubscriberNameLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055028", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logZeroLengthSubscriberName() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055029", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055029";
   }

   public static Loggable logZeroLengthSubscriberNameLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055029", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidDistributedTopic() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055030", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055030";
   }

   public static Loggable logInvalidDistributedTopicLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055030", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidUnsubscribe() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055031", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055031";
   }

   public static Loggable logInvalidUnsubscribeLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055031", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidConsumerCreation(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055032", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055032";
   }

   public static Loggable logInvalidConsumerCreationLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055032", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logNoMulticastForQueues() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055033", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055033";
   }

   public static Loggable logNoMulticastForQueuesLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055033", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logTopicNoMulticast(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055034", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055034";
   }

   public static Loggable logTopicNoMulticastLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055034", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logCannotOpenMulticastSocket(IOException var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055035", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055035";
   }

   public static Loggable logCannotOpenMulticastSocketLoggable(IOException var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055035", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logCannotJoinMulticastGroup(String var0, IOException var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055036", var2, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055036";
   }

   public static Loggable logCannotJoinMulticastGroupLoggable(String var0, IOException var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("055036", var2, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logSubscriptionNameInUse(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055037", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055037";
   }

   public static Loggable logSubscriptionNameInUseLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055037", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidFrontEndResponse(Object var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055038", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055038";
   }

   public static Loggable logInvalidFrontEndResponseLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055038", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logSystemError(Exception var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055039", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055039";
   }

   public static Loggable logSystemErrorLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055039", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logSubscriptionNameInUse2(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055040", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055040";
   }

   public static Loggable logSubscriptionNameInUse2Loggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055040", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logCannotLeaveMulticastGroup(String var0, IOException var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055041", var2, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055041";
   }

   public static Loggable logCannotLeaveMulticastGroupLoggable(String var0, IOException var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("055041", var2, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logNoSuchMethod2(int var0, String var1) {
      Object[] var2 = new Object[]{new Integer(var0), var1};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055042", var2, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055042";
   }

   public static Loggable logNoSuchMethod2Loggable(int var0, String var1) {
      Object[] var2 = new Object[]{new Integer(var0), var1};
      return new Loggable("055042", var2, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logNoSuchMethod3(int var0, String var1) {
      Object[] var2 = new Object[]{new Integer(var0), var1};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055043", var2, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055043";
   }

   public static Loggable logNoSuchMethod3Loggable(int var0, String var1) {
      Object[] var2 = new Object[]{new Integer(var0), var1};
      return new Loggable("055043", var2, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logNoSynchronousMulticastReceive() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055044", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055044";
   }

   public static Loggable logNoSynchronousMulticastReceiveLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055044", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidTimeout(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055045", var2, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055045";
   }

   public static Loggable logInvalidTimeoutLoggable(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      return new Loggable("055045", var2, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logListenerExists() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055046", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055046";
   }

   public static Loggable logListenerExistsLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055046", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logNoSuchMethod4(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055047", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055047";
   }

   public static Loggable logNoSuchMethod4Loggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("055047", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logMulticastSelectors() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055048", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055048";
   }

   public static Loggable logMulticastSelectorsLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055048", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInternalError(IllegalAccessException var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055049", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055049";
   }

   public static Loggable logInternalErrorLoggable(IllegalAccessException var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055049", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInternalError2(NoSuchMethodException var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055050", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055050";
   }

   public static Loggable logInternalError2Loggable(NoSuchMethodException var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055050", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInternalError3(InstantiationException var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055051", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055051";
   }

   public static Loggable logInternalError3Loggable(InstantiationException var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055051", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidSelector(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055052", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055052";
   }

   public static Loggable logInvalidSelectorLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055052", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logErrorCreatingConnection(RemoteException var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055053", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055053";
   }

   public static Loggable logErrorCreatingConnectionLoggable(RemoteException var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055053", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logErrorFindingDispatcher(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055054", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055054";
   }

   public static Loggable logErrorFindingDispatcherLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055054", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logNoTransaction() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055055", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055055";
   }

   public static Loggable logNoTransactionLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055055", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logErrorCommittingSession() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055056", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055056";
   }

   public static Loggable logErrorCommittingSessionLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055056", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logNoTransaction2() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055057", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055057";
   }

   public static Loggable logNoTransaction2Loggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055057", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logErrorRollingBackSession() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055058", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055058";
   }

   public static Loggable logErrorRollingBackSessionLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055058", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logOnlyFromServer() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055059", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055059";
   }

   public static Loggable logOnlyFromServerLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055059", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logNoTransaction3() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055060", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055060";
   }

   public static Loggable logNoTransaction3Loggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055060", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logNoTransaction4() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055061", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055061";
   }

   public static Loggable logNoTransaction4Loggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055061", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logTransacted() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055062", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055062";
   }

   public static Loggable logTransactedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055062", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logSessionHasConsumers() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055063", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055063";
   }

   public static Loggable logSessionHasConsumersLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055063", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logUnsupportedTopicOperation() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055064", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055064";
   }

   public static Loggable logUnsupportedTopicOperationLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055064", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logUnsupportedTopicOperation2() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055065", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055065";
   }

   public static Loggable logUnsupportedTopicOperation2Loggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055065", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logUnsupportedTopicOperation3() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055066", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055066";
   }

   public static Loggable logUnsupportedTopicOperation3Loggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055066", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logUnsupportedTopicOperation4() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055067", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055067";
   }

   public static Loggable logUnsupportedTopicOperation4Loggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055067", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidSubscription() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055068", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055068";
   }

   public static Loggable logInvalidSubscriptionLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055068", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logUnsupportedQueueOperation() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055069", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055069";
   }

   public static Loggable logUnsupportedQueueOperationLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055069", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logUnsupportedQueueOperation2() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055070", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055070";
   }

   public static Loggable logUnsupportedQueueOperation2Loggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055070", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logUnsupportedQueueOperation3() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055071", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055071";
   }

   public static Loggable logUnsupportedQueueOperation3Loggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055071", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidConnection() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055072", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055072";
   }

   public static Loggable logInvalidConnectionLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055072", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logDropNewer() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055073", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055073";
   }

   public static Loggable logDropNewerLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055073", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logDropOlder() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055074", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055074";
   }

   public static Loggable logDropOlderLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055074", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logClientThrowingException() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055075", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055075";
   }

   public static Loggable logClientThrowingExceptionLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055075", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logSessionIsClosed() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055076", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055076";
   }

   public static Loggable logSessionIsClosedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055076", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logCannotOverrideDestination() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055077", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055077";
   }

   public static Loggable logCannotOverrideDestinationLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055077", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logCannotOverrideDestination2() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055078", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055078";
   }

   public static Loggable logCannotOverrideDestination2Loggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055078", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logNeedDestination() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055079", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055079";
   }

   public static Loggable logNeedDestinationLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055079", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logNeedDestination2() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055080", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055080";
   }

   public static Loggable logNeedDestination2Loggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055080", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logUnsupportedTopicOperation5() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055081", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055081";
   }

   public static Loggable logUnsupportedTopicOperation5Loggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055081", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logClosedConnection() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055082", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055082";
   }

   public static Loggable logClosedConnectionLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055082", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logClientIDSet(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055083", var2, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055083";
   }

   public static Loggable logClientIDSetLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("055083", var2, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logConnectionConsumerOnClient() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055084", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055084";
   }

   public static Loggable logConnectionConsumerOnClientLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055084", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logNullDestination() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055085", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055085";
   }

   public static Loggable logNullDestinationLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055085", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logForeignDestination() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055086", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055086";
   }

   public static Loggable logForeignDestinationLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055086", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logMessageListenerExists() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055087", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055087";
   }

   public static Loggable logMessageListenerExistsLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055087", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logClosedConsumer() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055088", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055088";
   }

   public static Loggable logClosedConsumerLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055088", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logNullDestination2() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055089", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055089";
   }

   public static Loggable logNullDestination2Loggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055089", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logForeignDestination2() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055090", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055090";
   }

   public static Loggable logForeignDestination2Loggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055090", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logMustBeAQueue(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055091", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055091";
   }

   public static Loggable logMustBeAQueueLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055091", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logMustBeATopic(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055092", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055092";
   }

   public static Loggable logMustBeATopicLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055092", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logErrorConvertingForeignMessage() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055093", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055093";
   }

   public static Loggable logErrorConvertingForeignMessageLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055093", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logClosedProducer() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055094", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055094";
   }

   public static Loggable logClosedProducerLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055094", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logClosedBrowser() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055095", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055095";
   }

   public static Loggable logClosedBrowserLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055095", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logNullChar() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055096", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055096";
   }

   public static Loggable logNullCharLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055096", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logConvertChar(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055097", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055097";
   }

   public static Loggable logConvertCharLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055097", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logNullInt() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055098", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055098";
   }

   public static Loggable logNullIntLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055098", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logConvertInt(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055099", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055099";
   }

   public static Loggable logConvertIntLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055099", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logNullLong() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055100", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055100";
   }

   public static Loggable logNullLongLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055100", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logConvertLong(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055101", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055101";
   }

   public static Loggable logConvertLongLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055101", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logNullFloat() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055102", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055102";
   }

   public static Loggable logNullFloatLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055102", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logConvertFloat(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055103", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055103";
   }

   public static Loggable logConvertFloatLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055103", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logNullDouble() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055104", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055104";
   }

   public static Loggable logNullDoubleLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055104", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logConvertDouble(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055105", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055105";
   }

   public static Loggable logConvertDoubleLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055105", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logConvertByteArray() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055106", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055106";
   }

   public static Loggable logConvertByteArrayLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055106", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logConvertToByteArray(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055107", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055107";
   }

   public static Loggable logConvertToByteArrayLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055107", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logReadPastEnd() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055108", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055108";
   }

   public static Loggable logReadPastEndLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055108", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logStreamReadError() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055109", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055109";
   }

   public static Loggable logStreamReadErrorLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055109", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logStreamWriteError() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055110", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055110";
   }

   public static Loggable logStreamWriteErrorLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055110", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logConversionError(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055111", var2, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055111";
   }

   public static Loggable logConversionErrorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("055111", var2, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logStreamReadErrorIndex() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055112", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055112";
   }

   public static Loggable logStreamReadErrorIndexLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055112", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logStreamReadErrorStore() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055113", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055113";
   }

   public static Loggable logStreamReadErrorStoreLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055113", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logDeserializeIO() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055114", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055114";
   }

   public static Loggable logDeserializeIOLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055114", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logDeserializeCNFE() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055115", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055115";
   }

   public static Loggable logDeserializeCNFELoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055115", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logUnknownMessageType(byte var0) {
      Object[] var1 = new Object[]{new Byte(var0)};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055116", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055116";
   }

   public static Loggable logUnknownMessageTypeLoggable(byte var0) {
      Object[] var1 = new Object[]{new Byte(var0)};
      return new Loggable("055116", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidDeliveryMode2(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055117", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055117";
   }

   public static Loggable logInvalidDeliveryMode2Loggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("055117", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidRedeliveryLimit2() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055118", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055118";
   }

   public static Loggable logInvalidRedeliveryLimit2Loggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055118", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidPriority2(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055119", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055119";
   }

   public static Loggable logInvalidPriority2Loggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("055119", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidPropertyName2(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055121", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055121";
   }

   public static Loggable logInvalidPropertyName2Loggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055121", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logWriteInReadMode() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055122", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055122";
   }

   public static Loggable logWriteInReadModeLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055122", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidPropertyValue(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055123", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055123";
   }

   public static Loggable logInvalidPropertyValueLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055123", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logUnknownStreamType() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055125", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055125";
   }

   public static Loggable logUnknownStreamTypeLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055125", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logCorruptedStream() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055126", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055126";
   }

   public static Loggable logCorruptedStreamLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055126", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logVersionError() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055127", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055127";
   }

   public static Loggable logVersionErrorLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055127", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logReadInWriteMode() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055128", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055128";
   }

   public static Loggable logReadInWriteModeLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055128", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logWriteInReadMode2() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055129", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055129";
   }

   public static Loggable logWriteInReadMode2Loggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055129", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidDataType(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055130", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055130";
   }

   public static Loggable logInvalidDataTypeLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055130", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logIllegalName(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055131", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055131";
   }

   public static Loggable logIllegalNameLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055131", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logCopyError() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055132", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055132";
   }

   public static Loggable logCopyErrorLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055132", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logDeserializationError() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055133", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055133";
   }

   public static Loggable logDeserializationErrorLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055133", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logUnsupportedClassVersion(int var0, int var1, int var2) {
      Object[] var3 = new Object[]{new Integer(var0), new Integer(var1), new Integer(var2)};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055134", var3, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055134";
   }

   public static Loggable logUnsupportedClassVersionLoggable(int var0, int var1, int var2) {
      Object[] var3 = new Object[]{new Integer(var0), new Integer(var1), new Integer(var2)};
      return new Loggable("055134", var3, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logSimpleObject(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055135", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055135";
   }

   public static Loggable logSimpleObjectLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055135", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logUnrecognizedClassCode(short var0) {
      Object[] var1 = new Object[]{new Short(var0)};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055136", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055136";
   }

   public static Loggable logUnrecognizedClassCodeLoggable(short var0) {
      Object[] var1 = new Object[]{new Short(var0)};
      return new Loggable("055136", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidResponse(int var0, int var1, String var2) {
      Object[] var3 = new Object[]{new Integer(var0), new Integer(var1), var2};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055137", var3, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055137";
   }

   public static Loggable logInvalidResponseLoggable(int var0, int var1, String var2) {
      Object[] var3 = new Object[]{new Integer(var0), new Integer(var1), var2};
      return new Loggable("055137", var3, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidPeer(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055138", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055138";
   }

   public static Loggable logInvalidPeerLoggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("055138", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidTemporaryDestination() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055139", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055139";
   }

   public static Loggable logInvalidTemporaryDestinationLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055139", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInternalMarshallingError(byte var0) {
      Object[] var1 = new Object[]{new Byte(var0)};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055140", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055140";
   }

   public static Loggable logInternalMarshallingErrorLoggable(byte var0) {
      Object[] var1 = new Object[]{new Byte(var0)};
      return new Loggable("055140", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logDestinationNull() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055141", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055141";
   }

   public static Loggable logDestinationNullLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055141", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logForeignDestination3(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055142", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055142";
   }

   public static Loggable logForeignDestination3Loggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055142", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logDestinationMustBeQueue(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055143", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055143";
   }

   public static Loggable logDestinationMustBeQueueLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055143", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logDestinationMustBeTopic(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055144", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055144";
   }

   public static Loggable logDestinationMustBeTopicLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055144", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logUnknownStreamVersion(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055145", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055145";
   }

   public static Loggable logUnknownStreamVersionLoggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("055145", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logRawObjectError() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055146", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055146";
   }

   public static Loggable logRawObjectErrorLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055146", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logNotImplemented() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055147", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055147";
   }

   public static Loggable logNotImplementedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055147", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logRawObjectError2() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055148", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055148";
   }

   public static Loggable logRawObjectError2Loggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055148", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logReadPastEnd2(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055149", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055149";
   }

   public static Loggable logReadPastEnd2Loggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("055149", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logReadError(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055150", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055150";
   }

   public static Loggable logReadErrorLoggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("055150", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logNegativeLength(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055151", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055151";
   }

   public static Loggable logNegativeLengthLoggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("055151", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logTooMuchLength(int var0, int var1) {
      Object[] var2 = new Object[]{new Integer(var0), new Integer(var1)};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055152", var2, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055152";
   }

   public static Loggable logTooMuchLengthLoggable(int var0, int var1) {
      Object[] var2 = new Object[]{new Integer(var0), new Integer(var1)};
      return new Loggable("055152", var2, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logWriteError(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055153", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055153";
   }

   public static Loggable logWriteErrorLoggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("055153", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidObject(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055154", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055154";
   }

   public static Loggable logInvalidObjectLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055154", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logNotForwardable2() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055155", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055155";
   }

   public static Loggable logNotForwardable2Loggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055155", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logIncompatibleVersion9(byte var0, byte var1, byte var2, byte var3, String var4) {
      Object[] var5 = new Object[]{new Byte(var0), new Byte(var1), new Byte(var2), new Byte(var3), var4};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055156", var5, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055156";
   }

   public static Loggable logIncompatibleVersion9Loggable(byte var0, byte var1, byte var2, byte var3, String var4) {
      Object[] var5 = new Object[]{new Byte(var0), new Byte(var1), new Byte(var2), new Byte(var3), var4};
      return new Loggable("055156", var5, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidCompressionThreshold() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055157", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055157";
   }

   public static Loggable logInvalidCompressionThresholdLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055157", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logErrorDecompressMessageBody() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055158", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055158";
   }

   public static Loggable logErrorDecompressMessageBodyLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055158", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logErrorCompressionTag(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055159", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055159";
   }

   public static Loggable logErrorCompressionTagLoggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("055159", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logErrorDeserializeMessageBody() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055160", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055160";
   }

   public static Loggable logErrorDeserializeMessageBodyLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055160", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logNotForwardable3() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055161", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055161";
   }

   public static Loggable logNotForwardable3Loggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055161", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logErrorInteropTextMessage() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055162", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055162";
   }

   public static Loggable logErrorInteropTextMessageLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055162", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logErrorInteropXMLMessage() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055163", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055163";
   }

   public static Loggable logErrorInteropXMLMessageLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055163", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logUnsupported() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055164", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055164";
   }

   public static Loggable logUnsupportedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055164", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logStackTrace(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055165", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055165";
   }

   public static Loggable logStackTraceLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("055165", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logMultiplePrefetchConsumerPerSession() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055167", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055167";
   }

   public static Loggable logMultiplePrefetchConsumerPerSessionLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055167", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logUserTXNotSupportPrefetchConsumerPerSession() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055168", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055168";
   }

   public static Loggable logUserTXNotSupportPrefetchConsumerPerSessionLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055168", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logLostServerConnection() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055169", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055169";
   }

   public static Loggable logLostServerConnectionLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055169", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidStringProperty() {
      Object[] var0 = new Object[0];
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055170", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055170";
   }

   public static Loggable logInvalidStringPropertyLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("055170", var0, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidUnrestrictedUnsubscribe(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055171", var2, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055171";
   }

   public static Loggable logInvalidUnrestrictedUnsubscribeLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("055171", var2, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidUnrestrictedUnsubscribe2(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055172", var2, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055172";
   }

   public static Loggable logInvalidUnrestrictedUnsubscribe2Loggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("055172", var2, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logReadPastEnd3(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055173", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055173";
   }

   public static Loggable logReadPastEnd3Loggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("055173", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logStreamReadError2(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055174", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055174";
   }

   public static Loggable logStreamReadError2Loggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("055174", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   public static String logStreamWriteError3(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("055175", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.class.getClassLoader()));
      return "055175";
   }

   public static Loggable logStreamWriteError3Loggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("055175", var1, "weblogic.jms.JMSClientExceptionLogLocalizer", JMSClientExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSClientExceptionLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = JMSClientExceptionLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = JMSClientExceptionLogger.findMessageLogger();
      }
   }
}
