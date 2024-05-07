package weblogic.cache.lld;

import java.io.IOException;
import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;

public class LLDLogger {
   private static final String LOCALIZER_CLASS = "weblogic.cache.lld.LLDLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(LLDLogger.class.getName());
   }

   public static String logMessageException(String var0, IOException var1) {
      Object[] var2 = new Object[]{var0, var1};
      LLDLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("003090", var2, "weblogic.cache.lld.LLDLogLocalizer", LLDLogger.class.getClassLoader()));
      return "003090";
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = LLDLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = LLDLogger.findMessageLogger();
      }
   }
}
