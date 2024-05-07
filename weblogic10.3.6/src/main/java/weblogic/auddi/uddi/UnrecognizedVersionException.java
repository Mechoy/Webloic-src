package weblogic.auddi.uddi;

public class UnrecognizedVersionException extends UDDIException {
   public UnrecognizedVersionException() {
      this((String)null);
   }

   public UnrecognizedVersionException(String var1) {
      super(10040, var1 == null ? "" : var1);
   }
}
