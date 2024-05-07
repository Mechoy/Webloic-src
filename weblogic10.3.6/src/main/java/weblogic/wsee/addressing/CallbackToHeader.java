package weblogic.wsee.addressing;

import javax.xml.namespace.QName;
import weblogic.wsee.callback.CallbackConstants;
import weblogic.wsee.message.MsgHeaderType;

public class CallbackToHeader extends EndpointReferenceHeader {
   public static final QName NAME;
   public static final MsgHeaderType TYPE;

   public CallbackToHeader(EndpointReference var1) {
      super(var1);
   }

   public CallbackToHeader() {
   }

   public QName getName() {
      return NAME;
   }

   public MsgHeaderType getType() {
      return TYPE;
   }

   static {
      NAME = CallbackConstants.HEADER_CALLBACK_TO;
      TYPE = new MsgHeaderType();
   }
}
