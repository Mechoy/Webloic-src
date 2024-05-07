package weblogic.wsee.addressing;

import javax.xml.namespace.QName;
import weblogic.wsee.message.MsgHeaderType;
import weblogic.wsee.wsa.wsaddressing.WSAddressingConstants;

public class FaultToHeader extends EndpointReferenceHeader implements AddressingHeader {
   private static final long serialVersionUID = -3271641454609363884L;
   private QName name;
   public static final MsgHeaderType TYPE = new MsgHeaderType();

   public FaultToHeader() {
      this.name = WSAddressingConstants.WSA_HEADER_FAULT_TO_10;
   }

   public FaultToHeader(EndpointReference var1) {
      super(var1);
      this.name = WSAddressingConstants.WSA_HEADER_FAULT_TO_10;
   }

   public FaultToHeader(QName var1) {
      this.name = WSAddressingConstants.WSA_HEADER_FAULT_TO_10;
      this.name = var1;
   }

   public FaultToHeader(EndpointReference var1, QName var2) {
      super(var1);
      this.name = WSAddressingConstants.WSA_HEADER_FAULT_TO_10;
      this.name = var2;
   }

   public MsgHeaderType getType() {
      return TYPE;
   }

   public QName getName() {
      return this.name;
   }

   public void setName(QName var1) {
      this.name = var1;
   }
}
