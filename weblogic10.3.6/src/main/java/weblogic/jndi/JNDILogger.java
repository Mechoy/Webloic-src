package weblogic.jndi;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;

public class JNDILogger {
   private static final String LOCALIZER_CLASS = "weblogic.jndi.JNDILogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(JNDILogger.class.getName());
   }

   public static String logObsoleteProp(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JNDILogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("050000", var2, "weblogic.jndi.JNDILogLocalizer", JNDILogger.class.getClassLoader()));
      return "050000";
   }

   public static String logDiffThread() {
      Object[] var0 = new Object[0];
      JNDILogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("050001", var0, "weblogic.jndi.JNDILogLocalizer", JNDILogger.class.getClassLoader()));
      return "050001";
   }

   public static String logCannotReplicateObjectInCluster(String var0) {
      Object[] var1 = new Object[]{var0};
      JNDILogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("050002", var1, "weblogic.jndi.JNDILogLocalizer", JNDILogger.class.getClassLoader()));
      return "050002";
   }

   public static String logCannotCreateInitialContext(String var0) {
      Object[] var1 = new Object[]{var0};
      JNDILogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("050003", var1, "weblogic.jndi.JNDILogLocalizer", JNDILogger.class.getClassLoader()));
      return "050003";
   }

   public static String logUnableToBind(String var0) {
      Object[] var1 = new Object[]{var0};
      JNDILogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("050004", var1, "weblogic.jndi.JNDILogLocalizer", JNDILogger.class.getClassLoader()));
      return "050004";
   }

   public static String logUnableToUnBind(String var0) {
      Object[] var1 = new Object[]{var0};
      JNDILogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("050005", var1, "weblogic.jndi.JNDILogLocalizer", JNDILogger.class.getClassLoader()));
      return "050005";
   }

   public static String logExternalAppLookupWarning(String var0) {
      Object[] var1 = new Object[]{var0};
      JNDILogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("050006", var1, "weblogic.jndi.JNDILogLocalizer", JNDILogger.class.getClassLoader()));
      return "050006";
   }

   public static String logGlobalResourceLookupWarning(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      JNDILogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("050007", var2, "weblogic.jndi.JNDILogLocalizer", JNDILogger.class.getClassLoader()));
      return "050007";
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = JNDILogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = JNDILogger.findMessageLogger();
      }
   }
}
