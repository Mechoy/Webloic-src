package weblogic.auddi.uddi;

public class InvalidValueException extends UDDIException {
   public InvalidValueException() {
      this((String)null);
   }

   public InvalidValueException(String var1) {
      super(20200, var1 == null ? "" : var1);
   }
}
