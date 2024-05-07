package weblogic.auddi.uddi;

public class AssertionNotFoundException extends UDDIException {
   public AssertionNotFoundException() {
      this((String)null);
   }

   public AssertionNotFoundException(String var1) {
      super(30000, var1 == null ? "" : var1);
   }
}
