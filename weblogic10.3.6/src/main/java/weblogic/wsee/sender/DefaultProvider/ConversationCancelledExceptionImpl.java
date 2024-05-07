package weblogic.wsee.sender.DefaultProvider;

import weblogic.wsee.WseeSenderLogger;
import weblogic.wsee.sender.api.ConversationCancelledException;

public class ConversationCancelledExceptionImpl extends Exception implements ConversationCancelledException {
   private static final long serialVersionUID = 1L;
   private String conversationName;

   public ConversationCancelledExceptionImpl(String var1) {
      super(WseeSenderLogger.logConversationCancelledLoggable(var1).getMessage());
      this.conversationName = var1;
   }

   public String getConversationName() {
      return this.conversationName;
   }
}
