package weblogic.auddi.uddi;

public class InvalidKeyPassedException extends UDDIException {
   public InvalidKeyPassedException() {
      this((String)null);
   }

   public InvalidKeyPassedException(String var1) {
      super(10210, var1 == null ? "" : var1);
   }
}
