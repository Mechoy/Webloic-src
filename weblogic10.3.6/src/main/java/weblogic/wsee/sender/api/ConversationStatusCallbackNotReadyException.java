package weblogic.wsee.sender.api;

public class ConversationStatusCallbackNotReadyException extends SendingServiceException {
   private static final long serialVersionUID = 1L;

   public ConversationStatusCallbackNotReadyException() {
   }

   public ConversationStatusCallbackNotReadyException(String var1) {
      super(var1);
   }

   public ConversationStatusCallbackNotReadyException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public ConversationStatusCallbackNotReadyException(Throwable var1) {
      super(var1);
   }
}
