package weblogic.jms.backend;

import weblogic.application.ModuleException;
import weblogic.j2ee.descriptor.wl.DestinationKeyBean;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.extensions.JMSMessageInfo;
import weblogic.messaging.kernel.MessageElement;

public class BECursorDestinationKey extends BEDestinationKey {
   protected static final int KEY_TYPE_JMS_BEA_XID = 101;
   protected static final int KEY_TYPE_JMS_BEA_STATE = 102;
   protected static final int KEY_TYPE_JMS_BEA_SEQUENCE_NUMBER = 103;

   public BECursorDestinationKey(BEDestinationImpl var1, DestinationKeyBean var2) throws ModuleException {
      super(var1, var2);
      if (this.property.equalsIgnoreCase("JMS_BEA_Xid")) {
         this.keyType = 101;
      } else if (this.property.equalsIgnoreCase("JMS_BEA_State")) {
         this.keyType = 102;
      } else if (this.property.equalsIgnoreCase("JMS_BEA_SequenceNumber")) {
         this.keyType = 103;
      }

   }

   long compareKey(MessageElement var1, MessageElement var2, boolean var3) {
      long var4 = 0L;
      switch (this.keyType) {
         case 101:
            if (var1.getXid() != null && var2.getXid() != null) {
               var4 = (long)var1.getXid().toString().compareTo(var2.getXid().toString());
            } else if (var1.getXid() != null) {
               var4 = 1L;
            } else if (var2.getXid() != null) {
               var4 = -1L;
            } else {
               var4 = 0L;
            }
            break;
         case 102:
            var4 = (long)JMSMessageInfo.getStateString(var1.getState()).compareTo(JMSMessageInfo.getStateString(var2.getState()));
            break;
         case 103:
            var4 = var1.getInternalSequenceNumber() - var2.getInternalSequenceNumber();
            break;
         default:
            if (var4 == 0L) {
               return super.compareKey((MessageImpl)var1.getMessage(), (MessageImpl)var2.getMessage(), var3);
            }
      }

      if (var3) {
         return this.direction == 0 ? -var4 : var4;
      } else {
         return this.direction == 0 ? var4 : -var4;
      }
   }
}
