package weblogic.auddi.uddi;

public class MessageTooLargeException extends UDDIException {
   public MessageTooLargeException() {
      this((String)null);
   }

   public MessageTooLargeException(String var1) {
      super(30110, var1 == null ? "" : var1);
   }
}
