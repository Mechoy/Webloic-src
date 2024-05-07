package weblogic.security.SSL;

import weblogic.security.SecurityLogger;
import weblogic.security.utils.SSLSetupLogging;

public class SSLEnabledProtocolVersionsLogging implements SSLEnabledProtocolVersions.LogListener {
   public boolean isDebugEnabled() {
      return SSLSetupLogging.isDebugEnabled();
   }

   public void debug(String var1, Throwable var2) {
      if (null == var2) {
         SSLSetupLogging.debug(0, var1);
      } else {
         SSLSetupLogging.debug(0, var2, var1);
      }

   }

   public void logUnsupportedMinimumProtocolVersion(String var1, String var2) {
      SecurityLogger.logUnsupportedSSLMinimumProtocolVersion(var1, var2);
   }
}
