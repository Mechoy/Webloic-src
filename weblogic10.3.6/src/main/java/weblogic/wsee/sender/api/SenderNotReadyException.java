package weblogic.wsee.sender.api;

public class SenderNotReadyException extends SendException {
   private static final long serialVersionUID = 1L;

   public SenderNotReadyException() {
   }

   public SenderNotReadyException(String var1) {
      super(var1);
   }

   public SenderNotReadyException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public SenderNotReadyException(Throwable var1) {
      super(var1);
   }
}
