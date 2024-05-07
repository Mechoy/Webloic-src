package weblogic.wsee.sender.api;

import java.util.List;

public interface ConversationStatusCallback {
   void deliveryFailure(SendRequest var1, List<Throwable> var2) throws SendingServiceException;

   void conversationClosed(String var1, List<Throwable> var2) throws SendingServiceException;

   void conversationNotStarted(String var1, List<Throwable> var2) throws SendingServiceException;
}
