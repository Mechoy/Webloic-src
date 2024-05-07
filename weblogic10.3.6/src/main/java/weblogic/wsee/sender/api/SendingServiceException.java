package weblogic.wsee.sender.api;

public class SendingServiceException extends Exception {
   private static final long serialVersionUID = 1L;

   public SendingServiceException() {
   }

   public SendingServiceException(String var1) {
      super(var1);
   }

   public SendingServiceException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public SendingServiceException(Throwable var1) {
      super(var1);
   }
}
