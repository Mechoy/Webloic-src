package weblogic.jms.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.dispatcher.Response;

public final class JMSDestinationCreateResponse extends Response implements Externalizable {
   static final long serialVersionUID = -5700193800061807432L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int DESTINATION_MASK = 1792;
   private static final int DESTINATION_SHIFT = 8;
   private DestinationImpl destination;

   public JMSDestinationCreateResponse(DestinationImpl var1) {
      this.destination = var1;
   }

   public final DestinationImpl getDestination() {
      return this.destination;
   }

   public JMSDestinationCreateResponse() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1 = this.getVersionedStream(var1);
      int var2 = 1;
      var2 |= Destination.getDestinationType(this.destination, 8);
      var1.writeInt(var2);
      super.writeExternal(var1);
      this.destination.writeExternal(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         if ((var2 & 1792) != 0) {
            this.destination = Destination.createDestination((byte)((var2 & 1792) >>> 8), var1);
         } else if (((PeerInfoable)var1).getPeerInfo().compareTo(PeerInfo.VERSION_70) < 0) {
            this.destination = new DestinationImpl();
            this.destination.readExternal(var1);
         }

      }
   }
}
