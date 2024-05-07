package weblogic.wsee.sender.api;

import java.io.Serializable;

public interface SenderFactory extends Serializable {
   Sender createSender(String var1) throws SendingServiceException;

   ConversationStatusCallback getStatusCallback(String var1) throws SendingServiceException;
}
