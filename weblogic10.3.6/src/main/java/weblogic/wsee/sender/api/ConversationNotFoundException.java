package weblogic.wsee.sender.api;

public class ConversationNotFoundException extends SendingServiceException {
   public ConversationNotFoundException() {
   }

   public ConversationNotFoundException(String var1) {
      super(var1);
   }

   public ConversationNotFoundException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public ConversationNotFoundException(Throwable var1) {
      super(var1);
   }
}
