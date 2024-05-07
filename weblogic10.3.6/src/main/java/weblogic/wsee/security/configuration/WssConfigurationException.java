package weblogic.wsee.security.configuration;

import weblogic.xml.crypto.wss.WSSecurityException;

public class WssConfigurationException extends WSSecurityException {
   public WssConfigurationException() {
   }

   public WssConfigurationException(Exception var1) {
      super(var1);
   }

   public WssConfigurationException(String var1) {
      super(var1);
   }

   public WssConfigurationException(String var1, Exception var2) {
      super(var1, var2);
   }

   public WssConfigurationException(String var1, Object var2) {
      super(var1 + " " + var2);
   }
}
