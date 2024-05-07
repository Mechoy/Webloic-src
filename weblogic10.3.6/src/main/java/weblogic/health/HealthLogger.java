package weblogic.health;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;

public class HealthLogger {
   private static final String LOCALIZER_CLASS = "weblogic.health.HealthLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(HealthLogger.class.getName());
   }

   public static String logDebugMsg(String var0) {
      Object[] var1 = new Object[]{var0};
      HealthLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("310000", var1, "weblogic.health.HealthLogLocalizer", HealthLogger.class.getClassLoader()));
      return "310000";
   }

   public static String logErrorSubsystemFailed(String var0) {
      Object[] var1 = new Object[]{var0};
      HealthLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("310001", var1, "weblogic.health.HealthLogLocalizer", HealthLogger.class.getClassLoader()));
      return "310001";
   }

   public static String logFreeMemoryChanged(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      HealthLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("310002", var1, "weblogic.health.HealthLogLocalizer", HealthLogger.class.getClassLoader()));
      return "310002";
   }

   public static String logOOMEImminent(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      HealthLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("310003", var2, "weblogic.health.HealthLogLocalizer", HealthLogger.class.getClassLoader()));
      return "310003";
   }

   public static String logErrorSubsystemFailedWithReason(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HealthLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("310006", var2, "weblogic.health.HealthLogLocalizer", HealthLogger.class.getClassLoader()));
      return "310006";
   }

   public static String logNoRegisteredSubsystem(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HealthLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("310007", var2, "weblogic.health.HealthLogLocalizer", HealthLogger.class.getClassLoader()));
      return "310007";
   }

   public static String logNonCriticalSubsystemFailedWithReason(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HealthLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("310008", var2, "weblogic.health.HealthLogLocalizer", HealthLogger.class.getClassLoader()));
      return "310008";
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = HealthLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = HealthLogger.findMessageLogger();
      }
   }
}
