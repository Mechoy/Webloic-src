package weblogic.auddi.uddi;

public class RequestDeniedException extends UDDIException {
   public RequestDeniedException() {
      this((String)null);
   }

   public RequestDeniedException(String var1) {
      super(30210, var1 == null ? "" : var1);
   }
}
