package weblogic.wsee.sender.api;

import java.io.Serializable;

public interface ConversationCancelledException extends Serializable {
   String getConversationName();
}
