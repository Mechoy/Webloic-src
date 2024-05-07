package weblogic.auddi.uddi;

public class UserMismatchException extends UDDIException {
   public UserMismatchException() {
      this((String)null);
   }

   public UserMismatchException(String var1) {
      super(10140, var1 == null ? "" : var1);
   }
}
