package weblogic.auddi.uddi;

public class InvalidCompletionStatusException extends UDDIException {
   public InvalidCompletionStatusException() {
      this((String)null);
   }

   public InvalidCompletionStatusException(String var1) {
      super(30100, var1 == null ? "" : var1);
   }
}
