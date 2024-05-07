package weblogic.management.internal;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class ConfigAuditorLogger {
   private static final String LOCALIZER_CLASS = "weblogic.management.internal.ConfigAuditorLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(ConfigAuditorLogger.class.getName());
   }

   public static String logInfoAuditCreateSuccess(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConfigAuditorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("159900", var2, "weblogic.management.internal.ConfigAuditorLogLocalizer", ConfigAuditorLogger.class.getClassLoader()));
      return "159900";
   }

   public static Loggable logInfoAuditCreateSuccessLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("159900", var2, "weblogic.management.internal.ConfigAuditorLogLocalizer", ConfigAuditorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigAuditorLogger.class.getClassLoader());
   }

   public static String logInfoAuditCreateFailure(String var0, String var1, Exception var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      ConfigAuditorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("159901", var3, "weblogic.management.internal.ConfigAuditorLogLocalizer", ConfigAuditorLogger.class.getClassLoader()));
      return "159901";
   }

   public static Loggable logInfoAuditCreateFailureLoggable(String var0, String var1, Exception var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("159901", var3, "weblogic.management.internal.ConfigAuditorLogLocalizer", ConfigAuditorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigAuditorLogger.class.getClassLoader());
   }

   public static String logInfoAuditRemoveSuccess(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConfigAuditorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("159902", var2, "weblogic.management.internal.ConfigAuditorLogLocalizer", ConfigAuditorLogger.class.getClassLoader()));
      return "159902";
   }

   public static Loggable logInfoAuditRemoveSuccessLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("159902", var2, "weblogic.management.internal.ConfigAuditorLogLocalizer", ConfigAuditorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigAuditorLogger.class.getClassLoader());
   }

   public static String logInfoAuditRemoveFailure(String var0, String var1, Exception var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      ConfigAuditorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("159903", var3, "weblogic.management.internal.ConfigAuditorLogLocalizer", ConfigAuditorLogger.class.getClassLoader()));
      return "159903";
   }

   public static Loggable logInfoAuditRemoveFailureLoggable(String var0, String var1, Exception var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("159903", var3, "weblogic.management.internal.ConfigAuditorLogLocalizer", ConfigAuditorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigAuditorLogger.class.getClassLoader());
   }

   public static String logInfoAuditModifySuccess(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      ConfigAuditorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("159904", var5, "weblogic.management.internal.ConfigAuditorLogLocalizer", ConfigAuditorLogger.class.getClassLoader()));
      return "159904";
   }

   public static Loggable logInfoAuditModifySuccessLoggable(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return new Loggable("159904", var5, "weblogic.management.internal.ConfigAuditorLogLocalizer", ConfigAuditorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigAuditorLogger.class.getClassLoader());
   }

   public static String logInfoAuditModifyFailure(String var0, String var1, String var2, String var3, String var4, Exception var5) {
      Object[] var6 = new Object[]{var0, var1, var2, var3, var4, var5};
      ConfigAuditorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("159905", var6, "weblogic.management.internal.ConfigAuditorLogLocalizer", ConfigAuditorLogger.class.getClassLoader()));
      return "159905";
   }

   public static Loggable logInfoAuditModifyFailureLoggable(String var0, String var1, String var2, String var3, String var4, Exception var5) {
      Object[] var6 = new Object[]{var0, var1, var2, var3, var4, var5};
      return new Loggable("159905", var6, "weblogic.management.internal.ConfigAuditorLogLocalizer", ConfigAuditorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigAuditorLogger.class.getClassLoader());
   }

   public static String logInfoAuditInvokeSuccess(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      ConfigAuditorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("159907", var4, "weblogic.management.internal.ConfigAuditorLogLocalizer", ConfigAuditorLogger.class.getClassLoader()));
      return "159907";
   }

   public static Loggable logInfoAuditInvokeSuccessLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("159907", var4, "weblogic.management.internal.ConfigAuditorLogLocalizer", ConfigAuditorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigAuditorLogger.class.getClassLoader());
   }

   public static String logInfoAuditInvokeFailure(String var0, String var1, String var2, String var3, Exception var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      ConfigAuditorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("159908", var5, "weblogic.management.internal.ConfigAuditorLogLocalizer", ConfigAuditorLogger.class.getClassLoader()));
      return "159908";
   }

   public static Loggable logInfoAuditInvokeFailureLoggable(String var0, String var1, String var2, String var3, Exception var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return new Loggable("159908", var5, "weblogic.management.internal.ConfigAuditorLogLocalizer", ConfigAuditorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigAuditorLogger.class.getClassLoader());
   }

   public static String logInfoConfigurationAuditingEnabled(String var0) {
      Object[] var1 = new Object[]{var0};
      ConfigAuditorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("159909", var1, "weblogic.management.internal.ConfigAuditorLogLocalizer", ConfigAuditorLogger.class.getClassLoader()));
      return "159909";
   }

   public static Loggable logInfoConfigurationAuditingEnabledLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("159909", var1, "weblogic.management.internal.ConfigAuditorLogLocalizer", ConfigAuditorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigAuditorLogger.class.getClassLoader());
   }

   public static String logInfoConfigurationAuditingDisabled(String var0) {
      Object[] var1 = new Object[]{var0};
      ConfigAuditorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("159910", var1, "weblogic.management.internal.ConfigAuditorLogLocalizer", ConfigAuditorLogger.class.getClassLoader()));
      return "159910";
   }

   public static Loggable logInfoConfigurationAuditingDisabledLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("159910", var1, "weblogic.management.internal.ConfigAuditorLogLocalizer", ConfigAuditorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigAuditorLogger.class.getClassLoader());
   }

   public static String logInvalidNumberReplacingClearText(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConfigAuditorLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("159911", var2, "weblogic.management.internal.ConfigAuditorLogLocalizer", ConfigAuditorLogger.class.getClassLoader()));
      return "159911";
   }

   public static Loggable logInvalidNumberReplacingClearTextLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("159911", var2, "weblogic.management.internal.ConfigAuditorLogLocalizer", ConfigAuditorLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigAuditorLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = ConfigAuditorLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = ConfigAuditorLogger.findMessageLogger();
      }
   }
}
