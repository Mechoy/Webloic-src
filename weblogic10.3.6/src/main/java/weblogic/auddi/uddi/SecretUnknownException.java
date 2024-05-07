package weblogic.auddi.uddi;

public class SecretUnknownException extends UDDIException {
   public SecretUnknownException() {
      this((String)null);
   }

   public SecretUnknownException(String var1) {
      super(30230, var1 == null ? "" : var1);
   }
}
