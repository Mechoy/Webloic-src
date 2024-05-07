package weblogic.auddi.uddi;

public class TransferAbortedException extends UDDIException {
   public TransferAbortedException() {
      this((String)null);
   }

   public TransferAbortedException(String var1) {
      super(30200, var1 == null ? "" : var1);
   }
}
