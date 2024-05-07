package weblogic.jms.frontend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.common.ConsumerReconnectInfo;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Response;

public final class FEConsumerCreateResponse extends Response implements Externalizable {
   static final long serialVersionUID = 4370603925624654360L;
   private static final byte EXTVERSION1 = 1;
   private static final byte EXTVERSION2 = 2;
   private static final byte EXTVERSION3 = 3;
   private static final byte VERSION_MASK = 15;
   private static final byte _HAS_CONSUMER_RECON_INFO = 16;
   private JMSID consumerId;
   private String runtimeMBeanName;
   private ConsumerReconnectInfo consumerReconnectInfo;

   public FEConsumerCreateResponse(JMSID var1, String var2, ConsumerReconnectInfo var3) {
      this.consumerId = var1;
      this.runtimeMBeanName = var2;
      this.consumerReconnectInfo = var3;
   }

   public JMSID getConsumerId() {
      return this.consumerId;
   }

   public ConsumerReconnectInfo getConsumerReconnectInfo() {
      return this.consumerReconnectInfo;
   }

   public String getRuntimeMBeanName() {
      return this.runtimeMBeanName;
   }

   public FEConsumerCreateResponse() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1 = this.getVersionedStream(var1);
      byte var2;
      if (var1 instanceof PeerInfoable) {
         PeerInfo var3 = ((PeerInfoable)var1).getPeerInfo();
         if (var3 != null && var3.compareTo(PeerInfo.VERSION_920) < 0) {
            if (var3.compareTo(PeerInfo.VERSION_81) >= 0) {
               var2 = 2;
            } else {
               var2 = 1;
            }
         } else {
            var2 = 3;
         }
      } else {
         var2 = 3;
      }

      byte var4;
      if (var2 >= 3 && this.consumerReconnectInfo != null) {
         var4 = (byte)(var2 | 16);
      } else {
         var4 = var2;
      }

      assert 3 == var2;

      var1.writeByte(var4);
      super.writeExternal(var1);
      this.consumerId.writeExternal(var1);
      if (var2 >= 2) {
         var1.writeUTF(this.runtimeMBeanName);
      }

      if ((var4 & 16) != 0) {
         this.consumerReconnectInfo.writeExternal(var1);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      byte var2 = var1.readByte();
      byte var3 = (byte)(var2 & 15);
      if (var3 >= 1 && 3 >= var3) {
         super.readExternal(var1);
         this.consumerId = new JMSID();
         this.consumerId.readExternal(var1);
         if (var3 >= 2) {
            this.runtimeMBeanName = var1.readUTF();
            if (var3 >= 3 && (var2 & 16) != 0) {
               this.consumerReconnectInfo = new ConsumerReconnectInfo();
               this.consumerReconnectInfo.readExternal(var1);
            }
         }

      } else {
         throw JMSUtilities.versionIOException(var3, 1, 3);
      }
   }
}
