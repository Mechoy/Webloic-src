package weblogic.management.internal;

import java.net.URL;
import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class ConfigLogger {
   private static final String LOCALIZER_CLASS = "weblogic.management.internal.ConfigLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(ConfigLogger.class.getName());
   }

   public static String logErrorConnectingAdminServerForHome(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("150000", var2, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.class.getClassLoader()));
      return "150000";
   }

   public static Loggable logErrorConnectingAdminServerForHomeLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("150000", var2, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigLogger.class.getClassLoader());
   }

   public static String logErrorConnectionAdminServerForBootstrap(URL var0, String var1, Exception var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("150001", var3, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.class.getClassLoader()));
      return "150001";
   }

   public static Loggable logErrorConnectionAdminServerForBootstrapLoggable(URL var0, String var1, Exception var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("150001", var3, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigLogger.class.getClassLoader());
   }

   public static String logMSINotEnabled(String var0) {
      Object[] var1 = new Object[]{var0};
      ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("150014", var1, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.class.getClassLoader()));
      return "150014";
   }

   public static Loggable logMSINotEnabledLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("150014", var1, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigLogger.class.getClassLoader());
   }

   public static String logStartingIndependentManagerServer() {
      Object[] var0 = new Object[0];
      ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("150018", var0, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.class.getClassLoader()));
      return "150018";
   }

   public static Loggable logStartingIndependentManagerServerLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("150018", var0, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigLogger.class.getClassLoader());
   }

   public static String logErrorConnectingToAdminServer(String var0) {
      Object[] var1 = new Object[]{var0};
      ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("150020", var1, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.class.getClassLoader()));
      return "150020";
   }

   public static Loggable logErrorConnectingToAdminServerLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("150020", var1, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigLogger.class.getClassLoader());
   }

   public static String logAuthenticationFailedWhileStartingManagedServer(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("150021", var2, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.class.getClassLoader()));
      return "150021";
   }

   public static Loggable logAuthenticationFailedWhileStartingManagedServerLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("150021", var2, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigLogger.class.getClassLoader());
   }

   public static String logServerNameSameAsAdmin(String var0) {
      Object[] var1 = new Object[]{var0};
      ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("150024", var1, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.class.getClassLoader()));
      return "150024";
   }

   public static Loggable logServerNameSameAsAdminLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("150024", var1, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigLogger.class.getClassLoader());
   }

   public static String logInvalidReleaseLevel(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("150026", var3, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.class.getClassLoader()));
      return "150026";
   }

   public static Loggable logInvalidReleaseLevelLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("150026", var3, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigLogger.class.getClassLoader());
   }

   public static String logServerNameNotFound(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("150027", var2, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.class.getClassLoader()));
      return "150027";
   }

   public static Loggable logServerNameNotFoundLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("150027", var2, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigLogger.class.getClassLoader());
   }

   public static String logBootStrapException(Exception var0) {
      Object[] var1 = new Object[]{var0};
      ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("150028", var1, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.class.getClassLoader()));
      return "150028";
   }

   public static Loggable logBootStrapExceptionLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("150028", var1, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigLogger.class.getClassLoader());
   }

   public static String logAckAdminServerIsRunning(String var0) {
      Object[] var1 = new Object[]{var0};
      ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("150030", var1, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.class.getClassLoader()));
      return "150030";
   }

   public static Loggable logAckAdminServerIsRunningLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("150030", var1, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigLogger.class.getClassLoader());
   }

   public static String logManagedServerConfigWritten(String var0) {
      Object[] var1 = new Object[]{var0};
      ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("150031", var1, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.class.getClassLoader()));
      return "150031";
   }

   public static Loggable logManagedServerConfigWrittenLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("150031", var1, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigLogger.class.getClassLoader());
   }

   public static String logUnknownReleaseLevel() {
      Object[] var0 = new Object[0];
      ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("150032", var0, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.class.getClassLoader()));
      return "150032";
   }

   public static Loggable logUnknownReleaseLevelLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("150032", var0, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigLogger.class.getClassLoader());
   }

   public static String logBootstrapMissingCredentials(String var0) {
      Object[] var1 = new Object[]{var0};
      ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("150034", var1, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.class.getClassLoader()));
      return "150034";
   }

   public static Loggable logBootstrapMissingCredentialsLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("150034", var1, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigLogger.class.getClassLoader());
   }

   public static String logBootstrapInvalidCredentials(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("150035", var2, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.class.getClassLoader()));
      return "150035";
   }

   public static Loggable logBootstrapInvalidCredentialsLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("150035", var2, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigLogger.class.getClassLoader());
   }

   public static String logBootstrapUnauthorizedUser(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("150036", var2, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.class.getClassLoader()));
      return "150036";
   }

   public static Loggable logBootstrapUnauthorizedUserLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("150036", var2, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigLogger.class.getClassLoader());
   }

   public static String logConfigXMLFoundInParentDir(String var0) {
      Object[] var1 = new Object[]{var0};
      ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("150038", var1, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.class.getClassLoader()));
      return "150038";
   }

   public static Loggable logConfigXMLFoundInParentDirLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("150038", var1, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigLogger.class.getClassLoader());
   }

   public static String logCouldNotDetermineServerName() {
      Object[] var0 = new Object[0];
      ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("150039", var0, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.class.getClassLoader()));
      return "150039";
   }

   public static Loggable logCouldNotDetermineServerNameLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("150039", var0, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigLogger.class.getClassLoader());
   }

   public static String logServerNameDoesNotExist(String var0) {
      Object[] var1 = new Object[]{var0};
      ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("150041", var1, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.class.getClassLoader()));
      return "150041";
   }

   public static Loggable logServerNameDoesNotExistLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("150041", var1, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigLogger.class.getClassLoader());
   }

   public static String logAdminRequiredButNotSpecified(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("150042", var2, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.class.getClassLoader()));
      return "150042";
   }

   public static Loggable logAdminRequiredButNotSpecifiedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("150042", var2, "weblogic.management.internal.ConfigLogLocalizer", ConfigLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ConfigLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = ConfigLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = ConfigLogger.findMessageLogger();
      }
   }
}
