package weblogic.wsee.addressing;

import javax.xml.namespace.QName;
import weblogic.wsee.message.MsgHeaderType;
import weblogic.wsee.wsa.wsaddressing.WSAddressingConstants;

public class ReplyToHeader extends EndpointReferenceHeader implements AddressingHeader {
   private static final long serialVersionUID = -715771129941939290L;
   private QName name;
   public static final MsgHeaderType TYPE = new MsgHeaderType();

   public ReplyToHeader(EndpointReference var1) {
      super(var1);
      this.name = WSAddressingConstants.WSA_HEADER_REPLY_TO_10;
   }

   public ReplyToHeader(EndpointReference var1, QName var2) {
      super(var1);
      this.name = WSAddressingConstants.WSA_HEADER_REPLY_TO_10;
      this.name = var2;
   }

   public ReplyToHeader() {
      this.name = WSAddressingConstants.WSA_HEADER_REPLY_TO_10;
   }

   public ReplyToHeader(QName var1) {
      this.name = WSAddressingConstants.WSA_HEADER_REPLY_TO_10;
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
}
