package weblogic.xml.crypto.wss;

public class WSSecurityConfigurationException extends WSSecurityException {
   public WSSecurityConfigurationException() {
   }

   public WSSecurityConfigurationException(String var1) {
      super(var1);
   }

   public WSSecurityConfigurationException(String var1, Exception var2) {
      super(var1, var2);
   }

   public WSSecurityConfigurationException(String var1, Object var2) {
      super(var1 + " " + var2);
   }
}
