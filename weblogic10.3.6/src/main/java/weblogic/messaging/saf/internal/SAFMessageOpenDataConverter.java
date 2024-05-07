package weblogic.messaging.saf.internal;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.OpenDataException;
import javax.transaction.xa.Xid;
import weblogic.messaging.kernel.MessageElement;
import weblogic.messaging.runtime.OpenDataConverter;
import weblogic.messaging.saf.SAFMessageInfo;
import weblogic.messaging.saf.SAFRequest;

public final class SAFMessageOpenDataConverter implements OpenDataConverter {
   private final RemoteEndpointRuntimeDelegate destination;

   public SAFMessageOpenDataConverter(RemoteEndpointRuntimeDelegate var1) {
      this.destination = var1;
   }

   public CompositeData createCompositeData(Object var1) throws OpenDataException {
      if (var1 == null) {
         return null;
      } else if (!(var1 instanceof MessageElement)) {
         throw new OpenDataException("Unexpected class " + var1.getClass().getName());
      } else {
         MessageElement var2 = (MessageElement)var1;
         String var3 = null;
         Xid var4 = var2.getXid();
         if (var4 != null) {
            var3 = var4.toString();
         }

         SAFMessageInfo var5 = new SAFMessageInfo(var2.getInternalSequenceNumber(), var2.getState(), var3, var2.getInternalSequenceNumber(), var2.getConsumerID(), (SAFRequest)var2.getMessage(), this.destination.getURL());
         return var5.toCompositeData();
      }
   }
}
