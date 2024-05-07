package weblogic.security.pki.revocation.common;

import java.util.logging.Level;
import java.util.logging.Logger;

class DefaultLogListener extends AbstractLogListener {
   private static final Logger LOGGER = Logger.getLogger(DefaultLogListener.class.getPackage().getName());

   private DefaultLogListener() {
   }

   public static DefaultLogListener getInstance() {
      return new DefaultLogListener();
   }

   public boolean isLoggable(Level var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Non-null Level expected.");
      } else {
         return LOGGER.isLoggable(var1);
      }
   }

   public void log(Level var1, Throwable var2, String var3, Object... var4) {
      if (this.isLoggable(var1)) {
         String var5 = this.formatMessage(var3, var4);
         if (null != var2) {
            LOGGER.log(var1, var5, var2);
         } else {
            LOGGER.log(var1, var5);
         }

      }
   }
}
