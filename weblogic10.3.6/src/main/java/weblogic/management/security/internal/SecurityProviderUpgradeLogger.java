package weblogic.management.security.internal;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;

public class SecurityProviderUpgradeLogger {
   private static final String LOCALIZER_CLASS = "weblogic.management.security.internal.SecurityProviderUpgradeLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(SecurityProviderUpgradeLogger.class.getName());
   }

   public static String logIncorrectArgs() {
      Object[] var0 = new Object[0];
      SecurityProviderUpgradeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("400200", var0, "weblogic.management.security.internal.SecurityProviderUpgradeLogLocalizer", SecurityProviderUpgradeLogger.class.getClassLoader()));
      return "400200";
   }

   public static String logMigratingOldProvidersFrom1Arg(String var0) {
      Object[] var1 = new Object[]{var0};
      SecurityProviderUpgradeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("400201", var1, "weblogic.management.security.internal.SecurityProviderUpgradeLogLocalizer", SecurityProviderUpgradeLogger.class.getClassLoader()));
      return "400201";
   }

   public static String logNoJarsUpgraded() {
      Object[] var0 = new Object[0];
      SecurityProviderUpgradeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("400204", var0, "weblogic.management.security.internal.SecurityProviderUpgradeLogLocalizer", SecurityProviderUpgradeLogger.class.getClassLoader()));
      return "400204";
   }

   public static String logCopyProviderTo(String var0) {
      Object[] var1 = new Object[]{var0};
      SecurityProviderUpgradeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("400205", var1, "weblogic.management.security.internal.SecurityProviderUpgradeLogLocalizer", SecurityProviderUpgradeLogger.class.getClassLoader()));
      return "400205";
   }

   public static String logCompletedJars(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      SecurityProviderUpgradeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("400206", var1, "weblogic.management.security.internal.SecurityProviderUpgradeLogLocalizer", SecurityProviderUpgradeLogger.class.getClassLoader()));
      return "400206";
   }

   public static String logSkippedCount(String var0) {
      Object[] var1 = new Object[]{var0};
      SecurityProviderUpgradeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("400207", var1, "weblogic.management.security.internal.SecurityProviderUpgradeLogLocalizer", SecurityProviderUpgradeLogger.class.getClassLoader()));
      return "400207";
   }

   public static String logNoMDF(String var0) {
      Object[] var1 = new Object[]{var0};
      SecurityProviderUpgradeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("400208", var1, "weblogic.management.security.internal.SecurityProviderUpgradeLogLocalizer", SecurityProviderUpgradeLogger.class.getClassLoader()));
      return "400208";
   }

   public static String logNewFromOld(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      SecurityProviderUpgradeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("400209", var2, "weblogic.management.security.internal.SecurityProviderUpgradeLogLocalizer", SecurityProviderUpgradeLogger.class.getClassLoader()));
      return "400209";
   }

   public static String logInvalidMDF(String var0) {
      Object[] var1 = new Object[]{var0};
      SecurityProviderUpgradeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("400210", var1, "weblogic.management.security.internal.SecurityProviderUpgradeLogLocalizer", SecurityProviderUpgradeLogger.class.getClassLoader()));
      return "400210";
   }

   public static String logSkippingJar(String var0) {
      Object[] var1 = new Object[]{var0};
      SecurityProviderUpgradeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("400211", var1, "weblogic.management.security.internal.SecurityProviderUpgradeLogLocalizer", SecurityProviderUpgradeLogger.class.getClassLoader()));
      return "400211";
   }

   public static String logNowProcessing(String var0) {
      Object[] var1 = new Object[]{var0};
      SecurityProviderUpgradeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("400212", var1, "weblogic.management.security.internal.SecurityProviderUpgradeLogLocalizer", SecurityProviderUpgradeLogger.class.getClassLoader()));
      return "400212";
   }

   public static String logCannotConvert(String var0) {
      Object[] var1 = new Object[]{var0};
      SecurityProviderUpgradeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("400213", var1, "weblogic.management.security.internal.SecurityProviderUpgradeLogLocalizer", SecurityProviderUpgradeLogger.class.getClassLoader()));
      return "400213";
   }

   public static String logRunningFirstPhase(String var0) {
      Object[] var1 = new Object[]{var0};
      SecurityProviderUpgradeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("400214", var1, "weblogic.management.security.internal.SecurityProviderUpgradeLogLocalizer", SecurityProviderUpgradeLogger.class.getClassLoader()));
      return "400214";
   }

   public static String logRunningSecondPhase(String var0) {
      Object[] var1 = new Object[]{var0};
      SecurityProviderUpgradeLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("400215", var1, "weblogic.management.security.internal.SecurityProviderUpgradeLogLocalizer", SecurityProviderUpgradeLogger.class.getClassLoader()));
      return "400215";
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = SecurityProviderUpgradeLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = SecurityProviderUpgradeLogger.findMessageLogger();
      }
   }
}
