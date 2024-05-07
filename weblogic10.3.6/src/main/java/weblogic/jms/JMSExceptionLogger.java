package weblogic.jms;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class JMSExceptionLogger {
   private static final String LOCALIZER_CLASS = "weblogic.jms.JMSExceptionLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(JMSExceptionLogger.class.getName());
   }

   public static String logErrorInJNDIBind(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045002", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045002";
   }

   public static Loggable logErrorInJNDIBindLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("045002", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logMessagesThresholdTimeExceeded(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045028", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045028";
   }

   public static Loggable logMessagesThresholdTimeExceededLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("045028", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logMessagesThresholdRunningTimeExceeded(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045029", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045029";
   }

   public static Loggable logMessagesThresholdRunningTimeExceededLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("045029", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logBytesThresholdTimeExceeded(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045030", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045030";
   }

   public static Loggable logBytesThresholdTimeExceededLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("045030", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logBytesThresholdRunningTimeExceeded(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045031", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045031";
   }

   public static Loggable logBytesThresholdRunningTimeExceededLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("045031", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logNoBackEnd(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045032", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045032";
   }

   public static Loggable logNoBackEndLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("045032", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logAddUnknownType(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045039", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045039";
   }

   public static Loggable logAddUnknownTypeLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("045039", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logErrorAddingType(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045040", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045040";
   }

   public static Loggable logErrorAddingTypeLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("045040", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logDeleteUnknownType(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045041", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045041";
   }

   public static Loggable logDeleteUnknownTypeLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("045041", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logAddFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045042", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045042";
   }

   public static Loggable logAddFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("045042", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logRemoveFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045043", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045043";
   }

   public static Loggable logRemoveFailedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("045043", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logUnknownSubDeployment(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045045", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045045";
   }

   public static Loggable logUnknownSubDeploymentLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("045045", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidDeploymentTarget(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045047", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045047";
   }

   public static Loggable logInvalidDeploymentTargetLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("045047", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logNoTemporaryTemplate(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045048", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045048";
   }

   public static Loggable logNoTemporaryTemplateLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("045048", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logTemporaryTemplateNotConfigured(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045049", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045049";
   }

   public static Loggable logTemporaryTemplateNotConfiguredLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("045049", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logCreateDestinationIdentifierNameConflict(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045050", var4, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045050";
   }

   public static Loggable logCreateDestinationIdentifierNameConflictLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("045050", var4, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logNameConflict(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045051", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045051";
   }

   public static Loggable logNameConflictLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("045051", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidTargetChange(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045052", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045052";
   }

   public static Loggable logInvalidTargetChangeLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("045052", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logCannotDynamicallyAddDDMember(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045054", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045054";
   }

   public static Loggable logCannotDynamicallyAddDDMemberLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("045054", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logCannotDynamicallyRemoveDDMember(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045055", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045055";
   }

   public static Loggable logCannotDynamicallyRemoveDDMemberLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("045055", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logUnknownJMSModuleType(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045060", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045060";
   }

   public static Loggable logUnknownJMSModuleTypeLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("045060", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logBadDurableSubscription(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045061", var4, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045061";
   }

   public static Loggable logBadDurableSubscriptionLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("045061", var4, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logMoreThanOneInteropModule() {
      Object[] var0 = new Object[0];
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045062", var0, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045062";
   }

   public static Loggable logMoreThanOneInteropModuleLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("045062", var0, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidInteropModule(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045063", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045063";
   }

   public static Loggable logInvalidInteropModuleLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("045063", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidModuleTarget(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045064", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045064";
   }

   public static Loggable logInvalidModuleTargetLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("045064", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidSubTargeting(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045065", var5, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045065";
   }

   public static Loggable logInvalidSubTargetingLoggable(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return new Loggable("045065", var5, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidSubDeploymentTarget(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045066", var4, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045066";
   }

   public static Loggable logInvalidSubDeploymentTargetLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("045066", var4, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logUseOfInteropField(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045067", var4, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045067";
   }

   public static Loggable logUseOfInteropFieldLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("045067", var4, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logPhysicalDestinationNotPresent(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045068", var4, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045068";
   }

   public static Loggable logPhysicalDestinationNotPresentLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("045068", var4, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logEntityNotFoundInJMSSystemResource(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045069", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045069";
   }

   public static Loggable logEntityNotFoundInJMSSystemResourceLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("045069", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logEntityNotFoundInDomain(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045070", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045070";
   }

   public static Loggable logEntityNotFoundInDomainLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("045070", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logCannotCreateEntityInJMSSystemResource(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045071", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045071";
   }

   public static Loggable logCannotCreateEntityInJMSSystemResourceLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("045071", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logCannotCreateEntityInDomain(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045072", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045072";
   }

   public static Loggable logCannotCreateEntityInDomainLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("045072", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logCannotDeleteEntityFromJMSSystemResource(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045073", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045073";
   }

   public static Loggable logCannotDeleteEntityFromJMSSystemResourceLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("045073", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logCannotDeleteEntityFromDomain(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045074", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045074";
   }

   public static Loggable logCannotDeleteEntityFromDomainLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("045074", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logCannotFindAndModifyEntityFromJMSSystemResource(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045075", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045075";
   }

   public static Loggable logCannotFindAndModifyEntityFromJMSSystemResourceLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("045075", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidModuleEntityModifier(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045076", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045076";
   }

   public static Loggable logInvalidModuleEntityModifierLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("045076", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logDeliveryModeMismatch(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045077", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045077";
   }

   public static Loggable logDeliveryModeMismatchLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("045077", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logDeliveryModeMismatch2(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045078", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045078";
   }

   public static Loggable logDeliveryModeMismatch2Loggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("045078", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logNoPersistentMessages(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045079", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045079";
   }

   public static Loggable logNoPersistentMessagesLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("045079", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logFlowInterval(String var0, int var1, int var2) {
      Object[] var3 = new Object[]{var0, new Integer(var1), new Integer(var2)};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045080", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045080";
   }

   public static Loggable logFlowIntervalLoggable(String var0, int var1, int var2) {
      Object[] var3 = new Object[]{var0, new Integer(var1), new Integer(var2)};
      return new Loggable("045080", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logBadSessionsMax(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045081", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045081";
   }

   public static Loggable logBadSessionsMaxLoggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("045081", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logBindNamingException(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045082", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045082";
   }

   public static Loggable logBindNamingExceptionLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("045082", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logLocalBindNamingException(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045083", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045083";
   }

   public static Loggable logLocalBindNamingExceptionLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("045083", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logAppBindNamingException(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045084", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045084";
   }

   public static Loggable logAppBindNamingExceptionLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("045084", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logJMSServiceNotInitialized2() {
      Object[] var0 = new Object[0];
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045085", var0, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045085";
   }

   public static Loggable logJMSServiceNotInitialized2Loggable() {
      Object[] var0 = new Object[0];
      return new Loggable("045085", var0, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logJMSSystemResourceModuleCannotHaveInteropJmsName() {
      Object[] var0 = new Object[0];
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045086", var0, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045086";
   }

   public static Loggable logJMSSystemResourceModuleCannotHaveInteropJmsNameLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("045086", var0, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logJMSDeploymentModuleCannotHaveInteropJmsDescriptorName(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045087", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045087";
   }

   public static Loggable logJMSDeploymentModuleCannotHaveInteropJmsDescriptorNameLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("045087", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidJMSSystemResourceModuleDescriptorFileName(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045088", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045088";
   }

   public static Loggable logInvalidJMSSystemResourceModuleDescriptorFileNameLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("045088", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logBadErrorDestination(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045089", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045089";
   }

   public static Loggable logBadErrorDestinationLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("045089", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logErrorHandlingNotFound(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045090", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045090";
   }

   public static Loggable logErrorHandlingNotFoundLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("045090", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logIllegalTargetType(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045091", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045091";
   }

   public static Loggable logIllegalTargetTypeLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("045091", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logIllegalAgentType(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045092", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045092";
   }

   public static Loggable logIllegalAgentTypeLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("045092", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logInteropUDQ(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045093", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045093";
   }

   public static Loggable logInteropUDQLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("045093", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logInteropUDT(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045094", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045094";
   }

   public static Loggable logInteropUDTLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("045094", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logInteropSID(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045095", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045095";
   }

   public static Loggable logInteropSIDLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("045095", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logInteropSRC(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045096", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045096";
   }

   public static Loggable logInteropSRCLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("045096", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logInteropSEH(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045097", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045097";
   }

   public static Loggable logInteropSEHLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("045097", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logDuplicateResourceName(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045098", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045098";
   }

   public static Loggable logDuplicateResourceNameLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("045098", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logNoTemporaryTemplates() {
      Object[] var0 = new Object[0];
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045099", var0, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045099";
   }

   public static Loggable logNoTemporaryTemplatesLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("045099", var0, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logNoDestinationName() {
      Object[] var0 = new Object[0];
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045100", var0, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045100";
   }

   public static Loggable logNoDestinationNameLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("045100", var0, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidDestinationFormat(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045101", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045101";
   }

   public static Loggable logInvalidDestinationFormatLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("045101", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logDestinationNotFound(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045102", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045102";
   }

   public static Loggable logDestinationNotFoundLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("045102", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logBackEndUnreachable() {
      Object[] var0 = new Object[0];
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045103", var0, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045103";
   }

   public static Loggable logBackEndUnreachableLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("045103", var0, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logBackEndUnknown() {
      Object[] var0 = new Object[0];
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045104", var0, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045104";
   }

   public static Loggable logBackEndUnknownLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("045104", var0, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logFindFailed() {
      Object[] var0 = new Object[0];
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045105", var0, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045105";
   }

   public static Loggable logFindFailedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("045105", var0, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidDestinationType(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045106", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045106";
   }

   public static Loggable logInvalidDestinationTypeLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("045106", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logErrorRemovingSubscription() {
      Object[] var0 = new Object[0];
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045107", var0, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045107";
   }

   public static Loggable logErrorRemovingSubscriptionLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("045107", var0, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logNoMethod(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045108", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045108";
   }

   public static Loggable logNoMethodLoggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("045108", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidForeignServer(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045109", var4, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045109";
   }

   public static Loggable logInvalidForeignServerLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("045109", var4, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logConflictingTargetingInformation(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045110", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045110";
   }

   public static Loggable logConflictingTargetingInformationLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("045110", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logDefaultTargetingNotSupported(String var0) {
      Object[] var1 = new Object[]{var0};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045111", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045111";
   }

   public static Loggable logDefaultTargetingNotSupportedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("045111", var1, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logFlowLimits(String var0, int var1, int var2) {
      Object[] var3 = new Object[]{var0, new Integer(var1), new Integer(var2)};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045112", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045112";
   }

   public static Loggable logFlowLimitsLoggable(String var0, int var1, int var2) {
      Object[] var3 = new Object[]{var0, new Integer(var1), new Integer(var2)};
      return new Loggable("045112", var3, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logDDForwardRequestDenied(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045113", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045113";
   }

   public static Loggable logDDForwardRequestDeniedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("045113", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   public static String logInvalidUnrestrictedUnsubscribe(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("045114", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.class.getClassLoader()));
      return "045114";
   }

   public static Loggable logInvalidUnrestrictedUnsubscribeLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("045114", var2, "weblogic.jms.JMSExceptionLogLocalizer", JMSExceptionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, JMSExceptionLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = JMSExceptionLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = JMSExceptionLogger.findMessageLogger();
      }
   }
}
