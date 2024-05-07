package weblogic.ldap;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class EmbeddedLDAPLogger {
   private static final String LOCALIZER_CLASS = "weblogic.ldap.EmbeddedLDAPLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(EmbeddedLDAPLogger.class.getName());
   }

   public static String logConfigFileNotFound(String var0) {
      Object[] var1 = new Object[]{var0};
      EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("171500", var1, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.class.getClassLoader()));
      return "171500";
   }

   public static Loggable logConfigFileNotFoundLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("171500", var1, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EmbeddedLDAPLogger.class.getClassLoader());
   }

   public static String logCouldNotGetAdminListenAddress() {
      Object[] var0 = new Object[0];
      EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("171503", var0, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.class.getClassLoader()));
      return "171503";
   }

   public static Loggable logCouldNotGetAdminListenAddressLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("171503", var0, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EmbeddedLDAPLogger.class.getClassLoader());
   }

   public static String logErrorWritingReplicasFile(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("171507", var2, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.class.getClassLoader()));
      return "171507";
   }

   public static Loggable logErrorWritingReplicasFileLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("171507", var2, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EmbeddedLDAPLogger.class.getClassLoader());
   }

   public static String logCouldNotDeleteOnCleanup(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("171512", var2, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.class.getClassLoader()));
      return "171512";
   }

   public static Loggable logCouldNotDeleteOnCleanupLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("171512", var2, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EmbeddedLDAPLogger.class.getClassLoader());
   }

   public static String logCouldNotScheduleTrigger(String var0) {
      Object[] var1 = new Object[]{var0};
      EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("171513", var1, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.class.getClassLoader()));
      return "171513";
   }

   public static Loggable logCouldNotScheduleTriggerLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("171513", var1, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EmbeddedLDAPLogger.class.getClassLoader());
   }

   public static String logErrorGettingExclusiveAccess(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("171517", var2, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.class.getClassLoader()));
      return "171517";
   }

   public static Loggable logErrorGettingExclusiveAccessLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("171517", var2, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EmbeddedLDAPLogger.class.getClassLoader());
   }

   public static String logStackTrace(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("171518", var1, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.class.getClassLoader()));
      return "171518";
   }

   public static Loggable logStackTraceLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("171518", var1, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EmbeddedLDAPLogger.class.getClassLoader());
   }

   public static String logEmbeddedLDAPServerAlreadyRunning(String var0) {
      Object[] var1 = new Object[]{var0};
      EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("171519", var1, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.class.getClassLoader()));
      return "171519";
   }

   public static Loggable logEmbeddedLDAPServerAlreadyRunningLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("171519", var1, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EmbeddedLDAPLogger.class.getClassLoader());
   }

   public static String logEmbeddedLDAPServerRunningRetry(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("171520", var2, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.class.getClassLoader()));
      return "171520";
   }

   public static Loggable logEmbeddedLDAPServerRunningRetryLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("171520", var2, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EmbeddedLDAPLogger.class.getClassLoader());
   }

   public static String logErrorInitializingLDAPReplica(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("171521", var1, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.class.getClassLoader()));
      return "171521";
   }

   public static Loggable logErrorInitializingLDAPReplicaLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("171521", var1, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EmbeddedLDAPLogger.class.getClassLoader());
   }

   public static String logErrorInitializingLDAPMaster(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("171522", var2, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.class.getClassLoader()));
      return "171522";
   }

   public static Loggable logErrorInitializingLDAPMasterLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("171522", var2, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EmbeddedLDAPLogger.class.getClassLoader());
   }

   public static String getCredUnavailable() {
      Object[] var0 = new Object[0];
      return (new Loggable("171523", var0, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EmbeddedLDAPLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getCredUnavailableLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("171523", var0, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EmbeddedLDAPLogger.class.getClassLoader());
   }

   public static String logInvalidAdminListenAddress(String var0) {
      Object[] var1 = new Object[]{var0};
      EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("171524", var1, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.class.getClassLoader()));
      return "171524";
   }

   public static Loggable logInvalidAdminListenAddressLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("171524", var1, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EmbeddedLDAPLogger.class.getClassLoader());
   }

   public static String getErrLoadInitReplicaFile() {
      Object[] var0 = new Object[0];
      return (new Loggable("171525", var0, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EmbeddedLDAPLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getErrLoadInitReplicaFileLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("171525", var0, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EmbeddedLDAPLogger.class.getClassLoader());
   }

   public static String logReloadInitReplicaFile() {
      Object[] var0 = new Object[0];
      EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("171526", var0, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.class.getClassLoader()));
      return "171526";
   }

   public static Loggable logReloadInitReplicaFileLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("171526", var0, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EmbeddedLDAPLogger.class.getClassLoader());
   }

   public static String logSuccessReloadInitReplicaFile() {
      Object[] var0 = new Object[0];
      EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("171527", var0, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.class.getClassLoader()));
      return "171527";
   }

   public static Loggable logSuccessReloadInitReplicaFileLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("171527", var0, "weblogic.ldap.EmbeddedLDAPLogLocalizer", EmbeddedLDAPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, EmbeddedLDAPLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = EmbeddedLDAPLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = EmbeddedLDAPLogger.findMessageLogger();
      }
   }
}
