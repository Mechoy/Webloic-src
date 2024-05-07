package weblogic.wsee.sender.api;

public interface Sender {
   void setConversationCallback(ConversationCallback var1);

   SendResult send(SendRequest var1) throws SendException;

   void close() throws SendException;

   public static enum SendResult {
      SUCCESS,
      FAILURE,
      IN_PROCESS;
   }

   public interface ConversationCallback {
      void conversationReady();

      void sendSucceeded(long var1);

      boolean conversationFailedToStart(Throwable var1);

      boolean sendFailed(long var1, Throwable var3);
   }
}
