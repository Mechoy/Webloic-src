package weblogic.auddi.uddi;

public class UnsupportedException extends UDDIException {
   public UnsupportedException() {
      this((String)null);
   }

   public UnsupportedException(String var1) {
      super(10050, var1 == null ? "" : var1);
   }
}
