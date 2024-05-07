package weblogic.auddi.uddi;

public class UDDIExtendedException extends UDDIException {
   private int errno = 0;

   public UDDIExtendedException(int var1) {
      super(var1);
   }

   public UDDIExtendedException(int var1, String var2) {
      super(var1, var2);
   }
}
