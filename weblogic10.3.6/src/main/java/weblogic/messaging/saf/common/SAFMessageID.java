package weblogic.messaging.saf.common;

import weblogic.messaging.MessageID;

public final class SAFMessageID implements MessageID {
   private final String messageId;

   public SAFMessageID(String var1) {
      this.messageId = var1;
   }

   public int compareTo(Object var1) throws ClassCastException {
      if (this.messageId == null) {
         return ((SAFMessageID)var1).messageId == null ? 1 : -1;
      } else {
         return ((SAFMessageID)var1).messageId == null ? 1 : ((SAFMessageID)var1).messageId.compareTo(this.messageId);
      }
   }

   public String toString() {
      return this.messageId;
   }
}
