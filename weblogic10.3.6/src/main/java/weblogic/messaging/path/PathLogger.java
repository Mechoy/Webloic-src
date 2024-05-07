package weblogic.messaging.path;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;

public class PathLogger {
   private static final String LOCALIZER_CLASS = "weblogic.messaging.path.PathLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(PathLogger.class.getName());
   }

   public static String logPathStarted(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      PathLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("283002", var3, "weblogic.messaging.path.PathLogLocalizer", PathLogger.class.getClassLoader()));
      return "283002";
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = PathLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = PathLogger.findMessageLogger();
      }
   }
}
