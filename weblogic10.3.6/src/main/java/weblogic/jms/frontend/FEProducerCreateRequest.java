package weblogic.jms.frontend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.Destination;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.messaging.dispatcher.Response;

public final class FEProducerCreateRequest extends Request implements Externalizable {
   static final long serialVersionUID = -1183676357733894095L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int DESTINATION_MASK = 1792;
   private static final int DESTINATION_SHIFT = 8;
   private DestinationImpl destination;

   public FEProducerCreateRequest(JMSID var1, DestinationImpl var2) {
      super(var1, 4872);
      this.destination = var2;
   }

   public final DestinationImpl getDestination() {
      return this.destination;
   }

   public int remoteSignature() {
      return 18;
   }

   public Response createResponse() {
      return new FEProducerCreateResponse();
   }

   public FEProducerCreateRequest() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1 = this.getVersionedStream(var1);
      int var2 = 1;
      if (this.destination != null) {
         var2 |= Destination.getDestinationType(this.destination, 8);
      }

      var1.writeInt(var2);
      super.writeExternal(var1);
      if (this.destination != null) {
         this.destination.writeExternal(var1);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         if ((var2 & 1792) != 0) {
            byte var4 = (byte)((var2 & 1792) >>> 8);
            this.destination = Destination.createDestination(var4, var1);
         }

      }
   }
}
