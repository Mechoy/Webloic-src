package weblogic.wsee.sender.api;

import java.util.List;
import weblogic.wsee.sender.DefaultProvider.Conversation;

public interface SendingService {
   void addConversation(String var1, Resources var2) throws SendingServiceException;

   void continueConversation(String var1, Resources var2) throws SendingServiceException;

   void cancelConversation(String var1) throws SendingServiceException;

   boolean conversationExists(String var1) throws SendingServiceException;

   void addRequest(String var1, SendRequest var2) throws SendingServiceException;

   List<Long> getPendingRequests(String var1) throws SendingServiceException;

   SendRequest getRequestBySequenceNumber(String var1, long var2) throws SendingServiceException;

   SendRequest getRequestByMessageID(String var1) throws SendingServiceException;

   void acknowledgeRequests(String var1, long var2, long var4) throws SendingServiceException;

   void closeConversation(String var1) throws SendingServiceException;

   void stopConversation(String var1) throws SendingServiceException;

   void stop() throws SendingServiceException;

   Conversation getConversation(String var1) throws SendingServiceException;
}
