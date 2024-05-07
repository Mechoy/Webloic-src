package weblogic.jms;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;

public class JMSLogger {
   private static final String LOCALIZER_CLASS = "weblogic.jms.JMSLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(JMSLogger.class.getName());
   }

   public static String logCntPools(String var0, int var1) {
      Object[] var2 = new Object[]{var0, new Integer(var1)};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040010", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040010";
   }

   public static String logJMSFailedInit() {
      Object[] var0 = new Object[0];
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040014", var0, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040014";
   }

   public static String logJMSShutdown() {
      Object[] var0 = new Object[0];
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040015", var0, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040015";
   }

   public static String logConnFactoryFailed(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040017", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040017";
   }

   public static String logErrorInitialCtx(Exception var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040018", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040018";
   }

   public static String logBackEndBindingFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040019", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040019";
   }

   public static String logBytesThresholdHighDestination(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040024", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040024";
   }

   public static String logBytesThresholdLowDestination(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040025", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040025";
   }

   public static String logMessagesThresholdHighDestination(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040026", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040026";
   }

   public static String logMessagesThresholdLowDestination(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040027", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040027";
   }

   public static String logBytesThresholdHighServer(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040028", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040028";
   }

   public static String logBytesThresholdLowServer(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040029", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040029";
   }

   public static String logMessagesThresholdHighServer(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040030", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040030";
   }

   public static String logMessagesThresholdLowServer(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040031", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040031";
   }

   public static String logErrorUnregisteringBackEndDestination(String var0, Object var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040068", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040068";
   }

   public static String logErrorUnregisteringProducer(String var0, Object var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040069", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040069";
   }

   public static String logErrorUnregisteringFrontEndConnection(String var0, Object var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040070", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040070";
   }

   public static String logErrorUnregisteringFrontEndSession(String var0, Object var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040071", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040071";
   }

   public static String logErrorUnregisteringConsumer(String var0, Object var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040072", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040072";
   }

   public static String logInfoMigrationOkay(String var0, String var1, int var2) {
      Object[] var3 = new Object[]{var0, var1, new Integer(var2)};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040089", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040089";
   }

   public static String logCntDefCFactory(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040090", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040090";
   }

   public static String logStoreError(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040095", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040095";
   }

   public static String logCntDefCFactoryUndeployed(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040107", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040107";
   }

   public static String logCFactoryDeployed(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040108", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040108";
   }

   public static String logJMSServerDeployed(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040109", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040109";
   }

   public static String logStoreOpen(String var0, String var1, String var2, int var3, long var4) {
      Object[] var6 = new Object[]{var0, var1, var2, new Integer(var3), new Long(var4)};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040113", var6, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040113";
   }

   public static String logErrorCreateCF(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040119", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040119";
   }

   public static String logErrorBindCF(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040120", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040120";
   }

   public static String logErrorBEMultiDeployed(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040121", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040121";
   }

   public static String logErrorCreateBE(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040122", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040122";
   }

   public static String logErrorStartBE(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040123", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040123";
   }

   public static String logErrorCreateSSP(String var0, String var1, Exception var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040124", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040124";
   }

   public static String logErrorCreateCC(String var0, String var1, Exception var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040125", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040125";
   }

   public static String logErrorMulticastOpen(Exception var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040127", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040127";
   }

   public static String logIllegalThresholdValue(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040215", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040215";
   }

   public static String logJMSInitialized() {
      Object[] var0 = new Object[0];
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040305", var0, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040305";
   }

   public static String logJMSActive() {
      Object[] var0 = new Object[0];
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040306", var0, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040306";
   }

   public static String logJMSSuspending() {
      Object[] var0 = new Object[0];
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040307", var0, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040307";
   }

   public static String logJMSForceSuspending() {
      Object[] var0 = new Object[0];
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040308", var0, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040308";
   }

   public static String logJMSServerResuming(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040321", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040321";
   }

   public static String logJMSServerSuspending(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040324", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040324";
   }

   public static String logJMSServerSuspended(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040325", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040325";
   }

   public static String logExpiredMessageHeaderProperty(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040351", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040351";
   }

   public static String logExpiredMessageHeader(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040352", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040352";
   }

   public static String logExpiredMessageProperty(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040353", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040353";
   }

   public static String logExpiredMessageNoHeaderProperty(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040354", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040354";
   }

   public static String logJMSDDNullMember(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040359", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040359";
   }

   public static String logStackTrace(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040368", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040368";
   }

   public static String logStackTraceLinked(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040370", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040370";
   }

   public static String logAddedSessionPoolToBeRemoved(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040371", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040371";
   }

   public static String logErrorUnregisterJMSServer(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040372", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040372";
   }

   public static String logJMSServiceNotInitialized() {
      Object[] var0 = new Object[0];
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040373", var0, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040373";
   }

   public static String logProductionPauseOfDestination(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040376", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040376";
   }

   public static String logProductionResumeOfDestination(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040377", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040377";
   }

   public static String logInsertionPauseOfDestination(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040378", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040378";
   }

   public static String logInsertionResumeOfDestination(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040379", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040379";
   }

   public static String logConsumptionPauseOfDestination(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040380", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040380";
   }

   public static String logConsumptionResumeOfDestination(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040381", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040381";
   }

   public static String logProductionPauseOfJMSServer(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040382", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040382";
   }

   public static String logProductionResumeOfJMSServer(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040383", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040383";
   }

   public static String logInsertionPauseOfJMSServer(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040384", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040384";
   }

   public static String logInsertionResumeOfJMSServer(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040385", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040385";
   }

   public static String logConsumptionPauseOfJMSServer(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040386", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040386";
   }

   public static String logConsumptionResumeOfJMSServer(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040387", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040387";
   }

   public static String logForeignJMSDeployed(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040404", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040404";
   }

   public static String logErrorBindForeignJMS(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040405", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040405";
   }

   public static String logDDDeployed(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040406", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040406";
   }

   public static String logDefaultCFactoryDeployed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040407", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040407";
   }

   public static String logErrorBindDefaultCF(String var0, String var1, Exception var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040408", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040408";
   }

   public static String logExpiredSAFMessageHeaderProperty(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040409", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040409";
   }

   public static String logExpiredSAFMessageHeader(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040410", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040410";
   }

   public static String logExpiredSAFMessageProperty(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040411", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040411";
   }

   public static String logExpiredSAFMessageNoHeaderProperty(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040412", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040412";
   }

   public static String logAdminForceCommit(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040420", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040420";
   }

   public static String logAdminForceCommitError(String var0, String var1, Exception var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040421", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040421";
   }

   public static String logAdminForceRollback(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040422", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040422";
   }

   public static String logAdminForceRollbackError(String var0, String var1, Exception var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040423", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040423";
   }

   public static String logUserTransactionsEnabledDeprecated(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040430", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040430";
   }

   public static String logXAServerEnabledDeprecated(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040431", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040431";
   }

   public static String logNameConflictBindingGlobalJNDIName(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040442", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040442";
   }

   public static String logNameConflictChangingGlobalJNDIName(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040443", var4, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040443";
   }

   public static String logCouldNotUnbindGlobalJNDIName(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040444", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040444";
   }

   public static String logNameConflictChangingLocalJNDIName(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040445", var4, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040445";
   }

   public static String logCouldNotUnbindLocalJNDIName(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040446", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040446";
   }

   public static String logNameConflictBindingLocalJNDIName(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040447", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040447";
   }

   public static String logReplacingJMSFileStoreMBean(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040448", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040448";
   }

   public static String logReplacingJMSJDBCStoreMBean(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040449", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040449";
   }

   public static String logReplacingJMSPagingStore(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040450", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040450";
   }

   public static String logReplacingJMSJDBCPagingStore(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040451", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040451";
   }

   public static String logServerSessionPoolsDeprecated() {
      Object[] var0 = new Object[0];
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040452", var0, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040452";
   }

   public static String logServerPagingParametersDeprecated(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040453", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040453";
   }

   public static String logFlowControlEnabledDueToLowMemory(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040455", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040455";
   }

   public static String logInvalidJMSModuleSubDeploymentConfiguration(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040456", var4, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040456";
   }

   public static String logTemplateOnDDNotSupported(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040457", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040457";
   }

   public static String logUnprepareFailedInPrepare(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040458", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040458";
   }

   public static String logDeactivateFailedInActivate(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040459", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040459";
   }

   public static String logComponentCloseFailure(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040460", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040460";
   }

   public static String logDeactivateFailedInRollbackUpdate(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040461", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040461";
   }

   public static String logDestroyFailedInAdd(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040464", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040464";
   }

   public static String logDeactivateFailedInInit(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040467", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040467";
   }

   public static String logDeactivateFailedInActivateUpdate(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040470", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040470";
   }

   public static String logBytesMaximumNoEffect(String var0, long var1) {
      Object[] var3 = new Object[]{var0, new Long(var1)};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040475", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040475";
   }

   public static String logMessagesMaximumNoEffect(String var0, long var1) {
      Object[] var3 = new Object[]{var0, new Long(var1)};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040476", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040476";
   }

   public static String logErrorInJNDIUnbind(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040477", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040477";
   }

   public static String logErrorDeployingDefaultFactories(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040478", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040478";
   }

   public static String logErrorRollingBackConnectionConsumer(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040479", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040479";
   }

   public static String logErrorRemovingConnectionConsumer(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040480", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040480";
   }

   public static String logDestinationNameConflict(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040490", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040490";
   }

   public static String logSplitDeployment(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040491", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040491";
   }

   public static String logJMSServerShutdownError(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040494", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040494";
   }

   public static String logTemplateBytesMaximumNoEffect(String var0, long var1) {
      Object[] var3 = new Object[]{var0, new Long(var1)};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040496", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040496";
   }

   public static String logTemplateMessagesMaximumNoEffect(String var0, long var1) {
      Object[] var3 = new Object[]{var0, new Long(var1)};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040497", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040497";
   }

   public static String logDDForwardingError(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040498", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040498";
   }

   public static String logErrorPushingMessage(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040499", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040499";
   }

   public static String logRollbackChangeFailedInInit(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040500", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040500";
   }

   public static String logRollbackChangedFailedInRollbackUpdate(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040501", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040501";
   }

   public static String logActivateFailedDuringTargetingChange(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040502", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040502";
   }

   public static String logChangingDeliveryModeOverride(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040503", var4, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040503";
   }

   public static String logUnableToAddEntity(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040504", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040504";
   }

   public static String logNoEARSubDeployment(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040505", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040505";
   }

   public static String logSAFForwarderConnected(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040506", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040506";
   }

   public static String logSAFForwarderDisconnected(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040507", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040507";
   }

   public static String logReplacingBridgeDestinationMBean(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040508", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040508";
   }

   public static String logErrorDeployingBE(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040509", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040509";
   }

   public static String logErrorEstablishingJNDIListener(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040510", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040510";
   }

   public static String logInfoWaitForUnbind(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040511", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040511";
   }

   public static String logErrorWaitForUnbind(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040512", var3, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040512";
   }

   public static String logErrorRemovingJNDIListener(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040513", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040513";
   }

   public static String logJNDIDynamicChangeException(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040514", var2, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040514";
   }

   public static String logFailedToUnregisterInterceptionPoint(Exception var0) {
      Object[] var1 = new Object[]{var0};
      JMSLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("040515", var1, "weblogic.jms.JMSLogLocalizer", JMSLogger.class.getClassLoader()));
      return "040515";
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = JMSLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = JMSLogger.findMessageLogger();
      }
   }
}
