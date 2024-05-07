package weblogic.net;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;

public class NetLogger {
   private static final String LOCALIZER_CLASS = "weblogic.net.NetLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(NetLogger.class.getName());
   }

   public static String logDuplicateExpression(String var0, String var1, Exception var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      NetLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000902", var3, "weblogic.net.NetLogLocalizer", NetLogger.class.getClassLoader()));
      return "000902";
   }

   public static String logIOException(String var0, String var1, String var2, String var3, Throwable var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      NetLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000903", var5, "weblogic.net.NetLogLocalizer", NetLogger.class.getClassLoader()));
      return "000903";
   }

   public static String logHandlerInitFailure(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      NetLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000904", var3, "weblogic.net.NetLogLocalizer", NetLogger.class.getClassLoader()));
      return "000904";
   }

   public static String logFailedToConnect(String var0, String var1, Exception var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      NetLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000905", var3, "weblogic.net.NetLogLocalizer", NetLogger.class.getClassLoader()));
      return "000905";
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = NetLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = NetLogger.findMessageLogger();
      }
   }
}
