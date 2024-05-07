package weblogic.messaging.saf.internal;

import weblogic.messaging.kernel.MessageElement;
import weblogic.messaging.saf.SAFRequest;

public class SAFCursorKey {
   private static final int KEY_TYPE_SAF_CONVERSATIONNAME = 0;
   private static final int KEY_TYPE_SAF_SEQUENCENUMBER = 1;
   private static final int KEY_TYPE_SAF_TIMETOLIVE = 2;
   private static final int KEY_TYPE_SAF_MESSAGEID = 3;
   private static final String PROP_NAME_CONVERSATIONNAME = "ConversationName";
   private static final String PROP_NAME_SEQUENCENUMBER = "SequenceNumber";
   private static final String PROP_NAME_TIMETOLIVE = "TimeToLive";
   private static final String PROP_NAME_MESSAGEID = "MessageId";
   private final boolean asending;
   private int keyType;

   public SAFCursorKey(String var1, boolean var2) {
      this.asending = var2;
      if (var1.equalsIgnoreCase("ConversationName")) {
         this.keyType = 0;
      } else if (var1.equalsIgnoreCase("SequenceNumber")) {
         this.keyType = 1;
      } else if (var1.equalsIgnoreCase("TimeToLive")) {
         this.keyType = 2;
      } else if (var1.equalsIgnoreCase("MessageId")) {
         this.keyType = 3;
      }

   }

   long compareKey(MessageElement var1, MessageElement var2) {
      long var3 = 0L;
      SAFRequest var5 = (SAFRequest)var1.getMessage();
      SAFRequest var6 = (SAFRequest)var2.getMessage();
      switch (this.keyType) {
         case 0:
            if (var5.getConversationName() != null && var6.getConversationName() != null) {
               var3 = (long)var5.getConversationName().compareTo(var6.getConversationName());
            } else if (var5.getConversationName() != null) {
               var3 = 1L;
            } else if (var6.getConversationName() != null) {
               var3 = -1L;
            } else {
               var3 = 0L;
            }
            break;
         case 1:
            if (var5.getConversationName() != null && var6.getConversationName() != null) {
               var3 = var5.getSequenceNumber() - var6.getSequenceNumber();
            }
            break;
         case 2:
            var3 = var5.getSequenceNumber() - var6.getSequenceNumber();
            break;
         case 3:
            if (var5.getMessageId() != null && var6.getMessageId() != null) {
               var3 = (long)var5.getMessageId().compareTo(var6.getMessageId());
            } else if (var5.getMessageId() != null) {
               var3 = 1L;
            } else if (var6.getMessageId() != null) {
               var3 = -1L;
            } else {
               var3 = 0L;
            }
            break;
         default:
            var3 = 0L;
      }

      return this.asending ? var3 : -var3;
   }
}
