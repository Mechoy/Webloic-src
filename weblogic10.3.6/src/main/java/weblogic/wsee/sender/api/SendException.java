package weblogic.wsee.sender.api;

public class SendException extends Exception {
   private static final long serialVersionUID = 1L;

   public SendException() {
   }

   public SendException(String var1) {
      super(var1);
   }

   public SendException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public SendException(Throwable var1) {
      super(var1);
   }
}
