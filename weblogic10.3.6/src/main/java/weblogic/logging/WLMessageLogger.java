package weblogic.logging;

import java.util.logging.Level;
import weblogic.i18n.logging.LogMessage;
import weblogic.i18n.logging.MessageDispatcher;
import weblogic.kernel.KernelLogManager;

public class WLMessageLogger implements weblogic.i18n.logging.MessageLogger, MessageDispatcher {
   public boolean isSeverityEnabled(String var1, int var2) {
      return this.isSeverityEnabled(var2);
   }

   public void log(String var1, int var2, String var3) {
      this.log(var1, var2, var3, (Throwable)null);
   }

   public void log(String var1, int var2, String var3, Throwable var4) {
      MessageLogger.log(new LogMessage((String)null, (String)null, var1, var2, var3, var4));
   }

   public void log(LogMessage var1) {
      MessageLogger.log(var1);
   }

   public MessageDispatcher getMessageDispatcher(String var1) {
      return this;
   }

   public String getName() {
      return "";
   }

   public boolean isSeverityEnabled(int var1) {
      Level var2 = WLLevel.getLevel(var1);
      return KernelLogManager.getLogger().isLoggable(var2);
   }
}
