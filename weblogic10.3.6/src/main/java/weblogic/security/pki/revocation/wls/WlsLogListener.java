package weblogic.security.pki.revocation.wls;

import java.util.logging.Level;
import weblogic.security.pki.revocation.common.AbstractLogListener;
import weblogic.security.shared.LoggerWrapper;

class WlsLogListener extends AbstractLogListener {
   private static final LoggerWrapper LOGGER = LoggerWrapper.getInstance("CertRevocCheck");

   private WlsLogListener() {
   }

   public static WlsLogListener getInstance() {
      return new WlsLogListener();
   }

   public boolean isLoggable(Level var1) {
      return LOGGER.isDebugEnabled();
   }

   public void log(Level var1, Throwable var2, String var3, Object... var4) {
      if (this.isLoggable(var1)) {
         String var5 = this.formatMessage(var3, var4);
         if (null != var2) {
            if (LOGGER.isDebugEnabled()) {
               LOGGER.debug(var5, var2);
            }
         } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(var5);
         }

      }
   }
}
