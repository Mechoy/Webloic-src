package weblogic.auddi.uddi;

public class AuthTokenRequiredException extends UDDIException {
   public AuthTokenRequiredException() {
      this((String)null);
   }

   public AuthTokenRequiredException(String var1) {
      super(10120, var1 == null ? "" : var1);
   }
}
