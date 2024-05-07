package weblogic.auddi.uddi;

public class TooManyOptionsException extends UDDIException {
   public TooManyOptionsException() {
      this((String)null);
   }

   public TooManyOptionsException(String var1) {
      super(10030, var1 == null ? "" : var1);
   }
}
