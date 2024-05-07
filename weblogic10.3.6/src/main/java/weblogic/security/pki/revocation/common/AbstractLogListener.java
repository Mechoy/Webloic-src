package weblogic.security.pki.revocation.common;

import java.text.MessageFormat;
import java.util.logging.Level;

public abstract class AbstractLogListener implements LogListener {
   protected AbstractLogListener() {
   }

   public void log(Level var1, String var2, Object... var3) {
      this.log(var1, (Throwable)null, var2, var3);
   }

   public abstract void log(Level var1, Throwable var2, String var3, Object... var4);

   protected String formatMessage(String var1, Object... var2) {
      String var3;
      if (null != var1) {
         var3 = MessageFormat.format(var1, var2);
      } else {
         var3 = "";
      }

      return var3;
   }
}
