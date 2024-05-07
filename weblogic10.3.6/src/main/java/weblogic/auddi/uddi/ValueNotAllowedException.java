package weblogic.auddi.uddi;

public class ValueNotAllowedException extends UDDIException {
   public ValueNotAllowedException() {
      this((String)null);
   }

   public ValueNotAllowedException(String var1) {
      super(20210, var1 == null ? "" : var1);
   }
}
