package weblogic.auddi.uddi;

public class AuthTokenExpiredException extends UDDIException {
   public AuthTokenExpiredException() {
      this((String)null);
   }

   public AuthTokenExpiredException(String var1) {
      super(10110, var1 == null ? "" : var1);
   }
}
