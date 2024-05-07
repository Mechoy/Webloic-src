package weblogic.wsee.addressing;

import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.MsgHeaderType;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.wsa.wsaddressing.WSAddressingConstants;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class MessageIdHeader extends MsgHeader implements AddressingHeader {
   private static final long serialVersionUID = 5008941738536940940L;
   private QName name;
   public static final MsgHeaderType TYPE = new MsgHeaderType();
   private String messageId;

   public MessageIdHeader(String var1) {
      this.name = WSAddressingConstants.WSA_HEADER_MESSAGE_ID_10;
      this.messageId = var1;
   }

   public MessageIdHeader(String var1, QName var2) {
      this.name = WSAddressingConstants.WSA_HEADER_MESSAGE_ID_10;
      this.messageId = var1;
      this.name = var2;
   }

   public MessageIdHeader() {
      this.name = WSAddressingConstants.WSA_HEADER_MESSAGE_ID_10;
   }

   public MessageIdHeader(QName var1) {
      this.name = WSAddressingConstants.WSA_HEADER_MESSAGE_ID_10;
      this.name = var1;
   }

   public QName getName() {
      return this.name;
   }

   public void setName(QName var1) {
      this.name = var1;
   }

   public MsgHeaderType getType() {
      return TYPE;
   }

   public String getMessageId() {
      return this.messageId;
   }

   public void setMessageId(String var1) {
      this.messageId = var1;
   }

   public void read(Element var1) throws MsgHeaderException {
      try {
         this.messageId = DOMUtils.getTextData(var1);
      } catch (DOMProcessingException var3) {
         throw new MsgHeaderException("Could not get messageId", var3);
      }
   }

   public void write(Element var1) throws MsgHeaderException {
      DOMUtils.addTextData(var1, this.messageId);
   }

   public void toString(ToStringWriter var1) {
      super.toString(var1);
      var1.writeField("messageId", this.messageId);
   }
}
