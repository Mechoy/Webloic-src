package weblogic.management.deploy.internal;

import java.io.File;
import java.io.IOException;
import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class ApplicationPollerLogger {
   private static final String LOCALIZER_CLASS = "weblogic.management.deploy.internal.ApplicationPollerLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(ApplicationPollerLogger.class.getName());
   }

   public static String logActivate(String var0) {
      Object[] var1 = new Object[]{var0};
      ApplicationPollerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149400", var1, "weblogic.management.deploy.internal.ApplicationPollerLogLocalizer", ApplicationPollerLogger.class.getClassLoader()));
      return "149400";
   }

   public static Loggable logActivateLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149400", var1, "weblogic.management.deploy.internal.ApplicationPollerLogLocalizer", ApplicationPollerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ApplicationPollerLogger.class.getClassLoader());
   }

   public static String logRemove(String var0) {
      Object[] var1 = new Object[]{var0};
      ApplicationPollerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149401", var1, "weblogic.management.deploy.internal.ApplicationPollerLogLocalizer", ApplicationPollerLogger.class.getClassLoader()));
      return "149401";
   }

   public static Loggable logRemoveLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149401", var1, "weblogic.management.deploy.internal.ApplicationPollerLogLocalizer", ApplicationPollerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ApplicationPollerLogger.class.getClassLoader());
   }

   public static String logThrowableOnActivate(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      ApplicationPollerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149408", var2, "weblogic.management.deploy.internal.ApplicationPollerLogLocalizer", ApplicationPollerLogger.class.getClassLoader()));
      return "149408";
   }

   public static Loggable logThrowableOnActivateLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149408", var2, "weblogic.management.deploy.internal.ApplicationPollerLogLocalizer", ApplicationPollerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ApplicationPollerLogger.class.getClassLoader());
   }

   public static String logWarnOnManagedServerTargets(String var0) {
      Object[] var1 = new Object[]{var0};
      ApplicationPollerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149403", var1, "weblogic.management.deploy.internal.ApplicationPollerLogLocalizer", ApplicationPollerLogger.class.getClassLoader()));
      return "149403";
   }

   public static Loggable logWarnOnManagedServerTargetsLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149403", var1, "weblogic.management.deploy.internal.ApplicationPollerLogLocalizer", ApplicationPollerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ApplicationPollerLogger.class.getClassLoader());
   }

   public static String logThrowableOnDeactivate(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      ApplicationPollerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149407", var2, "weblogic.management.deploy.internal.ApplicationPollerLogLocalizer", ApplicationPollerLogger.class.getClassLoader()));
      return "149407";
   }

   public static Loggable logThrowableOnDeactivateLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149407", var2, "weblogic.management.deploy.internal.ApplicationPollerLogLocalizer", ApplicationPollerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ApplicationPollerLogger.class.getClassLoader());
   }

   public static String logThrowableOnServerStartup(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      ApplicationPollerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149409", var1, "weblogic.management.deploy.internal.ApplicationPollerLogLocalizer", ApplicationPollerLogger.class.getClassLoader()));
      return "149409";
   }

   public static Loggable logThrowableOnServerStartupLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149409", var1, "weblogic.management.deploy.internal.ApplicationPollerLogLocalizer", ApplicationPollerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ApplicationPollerLogger.class.getClassLoader());
   }

   public static String logUncaughtThrowable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      ApplicationPollerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149410", var1, "weblogic.management.deploy.internal.ApplicationPollerLogLocalizer", ApplicationPollerLogger.class.getClassLoader()));
      return "149410";
   }

   public static Loggable logUncaughtThrowableLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149410", var1, "weblogic.management.deploy.internal.ApplicationPollerLogLocalizer", ApplicationPollerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ApplicationPollerLogger.class.getClassLoader());
   }

   public static String logIOException(IOException var0) {
      Object[] var1 = new Object[]{var0};
      ApplicationPollerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149411", var1, "weblogic.management.deploy.internal.ApplicationPollerLogLocalizer", ApplicationPollerLogger.class.getClassLoader()));
      return "149411";
   }

   public static Loggable logIOExceptionLoggable(IOException var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149411", var1, "weblogic.management.deploy.internal.ApplicationPollerLogLocalizer", ApplicationPollerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ApplicationPollerLogger.class.getClassLoader());
   }

   public static String logRedeployingOnStartup(String var0) {
      Object[] var1 = new Object[]{var0};
      ApplicationPollerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149412", var1, "weblogic.management.deploy.internal.ApplicationPollerLogLocalizer", ApplicationPollerLogger.class.getClassLoader()));
      return "149412";
   }

   public static Loggable logRedeployingOnStartupLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149412", var1, "weblogic.management.deploy.internal.ApplicationPollerLogLocalizer", ApplicationPollerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ApplicationPollerLogger.class.getClassLoader());
   }

   public static String logCouldnotCreateAutodeployDir(String var0) {
      Object[] var1 = new Object[]{var0};
      ApplicationPollerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149414", var1, "weblogic.management.deploy.internal.ApplicationPollerLogLocalizer", ApplicationPollerLogger.class.getClassLoader()));
      return "149414";
   }

   public static Loggable logCouldnotCreateAutodeployDirLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149414", var1, "weblogic.management.deploy.internal.ApplicationPollerLogLocalizer", ApplicationPollerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ApplicationPollerLogger.class.getClassLoader());
   }

   public static String logExceptionWhileMigrating(Exception var0) {
      Object[] var1 = new Object[]{var0};
      ApplicationPollerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149415", var1, "weblogic.management.deploy.internal.ApplicationPollerLogLocalizer", ApplicationPollerLogger.class.getClassLoader()));
      return "149415";
   }

   public static Loggable logExceptionWhileMigratingLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149415", var1, "weblogic.management.deploy.internal.ApplicationPollerLogLocalizer", ApplicationPollerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ApplicationPollerLogger.class.getClassLoader());
   }

   public static String logApplicationMigrated(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      ApplicationPollerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149416", var2, "weblogic.management.deploy.internal.ApplicationPollerLogLocalizer", ApplicationPollerLogger.class.getClassLoader()));
      return "149416";
   }

   public static Loggable logApplicationMigratedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("149416", var2, "weblogic.management.deploy.internal.ApplicationPollerLogLocalizer", ApplicationPollerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ApplicationPollerLogger.class.getClassLoader());
   }

   public static String logFileHeld(File var0) {
      Object[] var1 = new Object[]{var0};
      ApplicationPollerLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("149417", var1, "weblogic.management.deploy.internal.ApplicationPollerLogLocalizer", ApplicationPollerLogger.class.getClassLoader()));
      return "149417";
   }

   public static Loggable logFileHeldLoggable(File var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("149417", var1, "weblogic.management.deploy.internal.ApplicationPollerLogLocalizer", ApplicationPollerLogger.MessageLoggerInitializer.INSTANCE.messageLogger, ApplicationPollerLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = ApplicationPollerLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = ApplicationPollerLogger.findMessageLogger();
      }
   }
}
