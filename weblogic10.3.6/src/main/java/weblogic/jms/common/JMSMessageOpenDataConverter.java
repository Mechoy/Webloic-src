package weblogic.jms.common;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.OpenDataException;
import javax.transaction.xa.Xid;
import weblogic.jms.extensions.JMSMessageInfo;
import weblogic.messaging.kernel.MessageElement;
import weblogic.messaging.kernel.Queue;
import weblogic.messaging.runtime.OpenDataConverter;

public class JMSMessageOpenDataConverter implements OpenDataConverter {
   boolean bodyIncluded;

   public JMSMessageOpenDataConverter(boolean var1) {
      this.bodyIncluded = var1;
   }

   public CompositeData createCompositeData(Object var1) throws OpenDataException {
      if (var1 == null) {
         return null;
      } else if (!(var1 instanceof MessageElement)) {
         throw new OpenDataException("Unexpected class " + var1.getClass().getName());
      } else {
         MessageElement var2 = (MessageElement)var1;
         MessageImpl var3 = (MessageImpl)var2.getMessage();
         if (var3 == null) {
            throw new OpenDataException("MessageElement " + var2 + " contained null msg");
         } else {
            var3 = var3.cloneit();
            var3.setDeliveryCount(var2.getDeliveryCount());
            var3.setPropertiesWritable(false);
            var3.setBodyWritable(false);
            var3.includeJMSXDeliveryCount(true);
            String var4 = null;
            Xid var5 = var2.getXid();
            if (var5 != null) {
               var4 = var5.toString();
            }

            String var6 = null;
            Queue var7 = var2.getQueue();
            if (var7 != null) {
               var6 = var7.getName();
            }

            JMSMessageInfo var8 = new JMSMessageInfo(var2.getInternalSequenceNumber(), var2.getState(), var4, var2.getInternalSequenceNumber(), var2.getConsumerID(), var3, var6, this.bodyIncluded);
            return var8.toCompositeData();
         }
      }
   }
}
