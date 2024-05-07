package weblogic.auddi.uddi;

public class NameTooLongException extends UDDIException {
   public NameTooLongException() {
      this((String)null);
   }

   public NameTooLongException(String var1) {
      super(10020, var1 == null ? "" : var1);
   }
}
