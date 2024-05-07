package weblogic.entitlement;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;

public class EntitlementLogger {
   private static final String LOCALIZER_CLASS = "weblogic.entitlement.EntitlementLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(EntitlementLogger.class.getName());
   }

   public static String logInvalidPropertyValue(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EntitlementLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("099000", var2, "weblogic.entitlement.EntitlementLogLocalizer", EntitlementLogger.class.getClassLoader()));
      return "099000";
   }

   public static String logPolicyEvaluationFailed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EntitlementLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("099001", var2, "weblogic.entitlement.EntitlementLogLocalizer", EntitlementLogger.class.getClassLoader()));
      return "099001";
   }

   public static String logRetrievedInvalidPredicate(String var0) {
      Object[] var1 = new Object[]{var0};
      EntitlementLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("099002", var1, "weblogic.entitlement.EntitlementLogLocalizer", EntitlementLogger.class.getClassLoader()));
      return "099002";
   }

   public static String logRoleUnregisteredPredicate(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EntitlementLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("099003", var2, "weblogic.entitlement.EntitlementLogLocalizer", EntitlementLogger.class.getClassLoader()));
      return "099003";
   }

   public static String logResourceUnregisteredPredicate(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EntitlementLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("099004", var2, "weblogic.entitlement.EntitlementLogLocalizer", EntitlementLogger.class.getClassLoader()));
      return "099004";
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = EntitlementLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = EntitlementLogger.findMessageLogger();
      }
   }
}
