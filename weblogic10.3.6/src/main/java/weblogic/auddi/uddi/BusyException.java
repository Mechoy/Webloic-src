package weblogic.auddi.uddi;

public class BusyException extends UDDIException {
   public BusyException() {
      this((String)null);
   }

   public BusyException(String var1) {
      super(10400, var1 == null ? "" : var1);
   }
}
