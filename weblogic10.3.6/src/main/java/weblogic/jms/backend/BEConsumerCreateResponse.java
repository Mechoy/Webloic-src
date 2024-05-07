package weblogic.jms.backend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.common.ConsumerReconnectInfo;
import weblogic.jms.common.JMSUtilities;
import weblogic.messaging.dispatcher.Response;

public final class BEConsumerCreateResponse extends Response implements Externalizable {
   static final long serialVersionUID = -2336586559673535552L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int _HAS_CONSUMER_RECON_INFO = 256;
   private ConsumerReconnectInfo consumerReconnectInfo;

   public BEConsumerCreateResponse(ConsumerReconnectInfo var1) {
      this.consumerReconnectInfo = var1;
   }

   public ConsumerReconnectInfo getConsumerReconnectInfo() {
      return this.consumerReconnectInfo;
   }

   public BEConsumerCreateResponse() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1 = this.getVersionedStream(var1);
      if (var1 instanceof PeerInfoable && ((PeerInfoable)var1).getPeerInfo().compareTo(PeerInfo.VERSION_920) < 0) {
         throw JMSUtilities.versionIOException(0, 1, 1);
      } else {
         int var2 = 1;
         if (this.consumerReconnectInfo != null) {
            var2 |= 256;
         }

         var1.writeInt(var2);
         super.writeExternal(var1);
         if ((var2 & 256) != 0) {
            this.consumerReconnectInfo.writeExternal(var1);
         }

      }
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         super.readExternal(var1);
         if ((var2 & 256) != 0) {
            this.consumerReconnectInfo = new ConsumerReconnectInfo();
            this.consumerReconnectInfo.readExternal(var1);
         }

      }
   }
}
