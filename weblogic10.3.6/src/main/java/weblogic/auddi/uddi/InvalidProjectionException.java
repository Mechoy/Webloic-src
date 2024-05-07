package weblogic.auddi.uddi;

public class InvalidProjectionException extends UDDIException {
   public InvalidProjectionException() {
      this((String)null);
   }

   public InvalidProjectionException(String var1) {
      super(20230, var1 == null ? "" : var1);
   }
}
