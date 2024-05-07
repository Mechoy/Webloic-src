package weblogic.wsee.sender.api;

import java.io.Serializable;
import weblogic.wsee.persistence.Storable;

public interface SendRequest extends Storable {
   String getConversationName();

   void setConversationName(String var1);

   long getSequenceNumber();

   void setSequenceNumber(long var1);

   long getTimestamp();

   void setTimestamp(long var1);

   Serializable getPayload();

   String getMessageId();

   Serializable getObjectId();
}
