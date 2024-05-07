package weblogic.wsee.addressing;

import javax.xml.namespace.QName;
import weblogic.wsee.message.MsgHeaderType;
import weblogic.wsee.wsa.wsaddressing.WSAddressingConstants;

public class FromHeader extends EndpointReferenceHeader implements AddressingHeader {
   private static final long serialVersionUID = 2634040806699477614L;
   private QName name;
   public static final MsgHeaderType TYPE = new MsgHeaderType();

   public FromHeader(EndpointReference var1, QName var2) {
      this(var1);
      this.name = var2;
   }

   public FromHeader(EndpointReference var1) {
      super(var1);
      this.name = WSAddressingConstants.WSA_HEADER_SOURCE_10;
      this.setMustUnderstand(false);
   }

   public FromHeader(QName var1) {
      this.name = WSAddressingConstants.WSA_HEADER_SOURCE_10;
      this.name = var1;
   }

   public FromHeader() {
      this.name = WSAddressingConstants.WSA_HEADER_SOURCE_10;
      this.setMustUnderstand(false);
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
