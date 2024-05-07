package weblogic.xml.crypto.wss;

public class SecurityTokenConfigurationException extends WSSecurityException {
   public SecurityTokenConfigurationException() {
   }

   public SecurityTokenConfigurationException(Exception var1) {
      super(var1);
   }

   public SecurityTokenConfigurationException(String var1) {
      super(var1);
   }

   public SecurityTokenConfigurationException(String var1, Exception var2) {
      super(var1, var2);
   }

   public SecurityTokenConfigurationException(String var1, Object var2) {
      super(var1 + " " + var2);
   }
}
