package weblogic.auddi.uddi;

public class UnknownUserException extends UDDIException {
   public UnknownUserException() {
      this((String)null);
   }

   public UnknownUserException(String var1) {
      super(10150, var1 == null ? "" : var1);
   }
}
