package weblogic.wsee.callback;

import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.conversation.ConversationConstants;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.MsgHeaderType;
import weblogic.wsee.util.ToStringWriter;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class Wlw81CallbackHeader extends MsgHeader {
   private static final long serialVersionUID = 3753811341397224049L;
   private String conversationId;
   public static final QName NAME;
   public static final MsgHeaderType TYPE;

   public QName getName() {
      return NAME;
   }

   public MsgHeaderType getType() {
      return TYPE;
   }

   public Wlw81CallbackHeader(String var1) {
      this.conversationId = var1;
   }

   public Wlw81CallbackHeader() {
   }

   public String getConversationId() {
      return this.conversationId;
   }

   public void read(Element var1) throws MsgHeaderException {
      try {
         this.conversationId = DOMUtils.getValueByTagNameNS(var1, "http://www.openuri.org/2002/04/soap/conversation/", "conversationID");
      } catch (DOMProcessingException var3) {
         throw new MsgHeaderException("Could not parse Wlw81CallbackHeader", var3);
      }
   }

   public void write(Element var1) throws MsgHeaderException {
      DOMUtils.addValueNS(var1, "http://www.openuri.org/2002/04/soap/conversation/", "conv:conversationID", this.conversationId);
   }

   public void toString(ToStringWriter var1) {
      super.toString(var1);
      var1.writeField("conversationId", this.conversationId);
   }

   static {
      NAME = ConversationConstants.WLW81_CALLBACK_HEADER;
      TYPE = new MsgHeaderType();
   }
}
