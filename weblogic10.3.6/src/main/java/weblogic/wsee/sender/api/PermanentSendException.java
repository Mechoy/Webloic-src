package weblogic.wsee.sender.api;

public class PermanentSendException extends SendException {
   private static final long serialVersionUID = 1L;

   public PermanentSendException() {
   }

   public PermanentSendException(String var1) {
      super(var1);
   }

   public PermanentSendException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public PermanentSendException(Throwable var1) {
      super(var1);
   }
}
