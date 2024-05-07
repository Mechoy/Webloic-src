package weblogic.wsee.conversation;

import org.w3c.dom.Element;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.ToStringWriter;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public abstract class ConversationHeader extends MsgHeader {
   private static final long serialVersionUID = 894328051313640482L;
   protected String conversationId;
   protected String callbackLocation;

   public ConversationHeader(String var1, String var2) {
      this.conversationId = var1;
      this.callbackLocation = var2;
   }

   public ConversationHeader(String var1) {
      this.conversationId = var1;
   }

   public ConversationHeader() {
   }

   public String getConversationId() {
      return this.conversationId;
   }

   public void setConversationId(String var1) {
      if (!StringUtil.isEmpty(var1)) {
         throw new IllegalArgumentException("Cannot change a conversationId on the startHeader if one has already been set.");
      } else {
         this.conversationId = var1;
      }
   }

   public String getCallbackLocation() {
      return this.callbackLocation;
   }

   public void read(Element var1) throws MsgHeaderException {
      try {
         this.conversationId = DOMUtils.getValueByTagNameNS(var1, "http://www.openuri.org/2002/04/soap/conversation/", "conversationID");
         this.callbackLocation = DOMUtils.getOptionalValueByTagNameNS(var1, "http://www.openuri.org/2002/04/soap/conversation/", "callbackLocation");
      } catch (DOMProcessingException var3) {
         throw new MsgHeaderException("Could not parse ContinueHeader", var3);
      }
   }

   public void write(Element var1) throws MsgHeaderException {
      DOMUtils.addValueNS(var1, "http://www.openuri.org/2002/04/soap/conversation/", "conv:conversationID", this.conversationId);
      if (this.callbackLocation != null) {
         DOMUtils.addValueNS(var1, "http://www.openuri.org/2002/04/soap/conversation/", "conv:callbackLocation", this.callbackLocation);
      }

   }

   public void toString(ToStringWriter var1) {
      super.toString(var1);
      var1.writeField("conversationId", this.conversationId);
      var1.writeField("callbackLocation", this.callbackLocation);
   }
}
