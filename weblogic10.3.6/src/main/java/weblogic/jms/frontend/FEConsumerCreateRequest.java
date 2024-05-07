package weblogic.jms.frontend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.common.ConsumerReconnectInfo;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.DistributedDestinationImpl;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.messaging.dispatcher.Response;

public final class FEConsumerCreateRequest extends Request implements Externalizable {
   static final long serialVersionUID = -2345606550673535552L;
   private static final int EXTVERSION_PRE811 = 1;
   private static final int EXTVERSION_PRE920 = 10;
   private static final int EXTVERSION_PRE1033 = 12;
   private static final int EXTVERSION4 = 14;
   private static final int EXTVERSION = 14;
   private static final int VERSION_MASK = 255;
   private static final int _HASCLIENTID = 256;
   private static final int _HASNAME = 512;
   private static final int _HASSELECTOR = 1024;
   private static final int _ISNOLOCAL = 2048;
   private static final int _HASREDELIVERYDELAY = 4096;
   private static final int _HASDISTRIBUTEDDESTINATION = 8192;
   private static final int _HAS_CONSUMER_RECON_INFO = 16384;
   private String clientId;
   private String name;
   private DestinationImpl destination;
   private String selector;
   private boolean noLocal;
   private int messagesMaximum;
   private long redeliveryDelay;
   private transient int numberOfRetries;
   private ConsumerReconnectInfo consumerReconnectInfo;
   private int subscriptionSharingPolicy = -1;

   public FEConsumerCreateRequest(JMSID var1, String var2, String var3, DestinationImpl var4, String var5, boolean var6, int var7, long var8, ConsumerReconnectInfo var10, int var11) {
      super(var1, 2824);
      this.clientId = var2;
      this.name = var3;
      this.destination = var4;
      this.selector = var5;
      this.noLocal = var6;
      this.messagesMaximum = var7;
      this.redeliveryDelay = var8;
      this.consumerReconnectInfo = var10;
      this.subscriptionSharingPolicy = var11;
   }

   String getClientId() {
      return this.clientId;
   }

   String getName() {
      return this.name;
   }

   DestinationImpl getDestination() {
      return this.destination;
   }

   void setDestination(DestinationImpl var1) {
      this.destination = var1;
   }

   void setSelector(String var1) {
      this.selector = var1;
   }

   String getSelector() {
      return this.selector;
   }

   boolean getNoLocal() {
      return this.noLocal;
   }

   int getMessagesMaximum() {
      return this.messagesMaximum;
   }

   long getRedeliveryDelay() {
      return this.redeliveryDelay;
   }

   int getSubscriptionSharingPolicy() {
      return this.subscriptionSharingPolicy;
   }

   public int remoteSignature() {
      return 18;
   }

   public Response createResponse() {
      return new FEConsumerCreateResponse();
   }

   int getNumberOfRetries() {
      return this.numberOfRetries;
   }

   void setNumberOfRetries(int var1) {
      this.numberOfRetries = var1;
   }

   public ConsumerReconnectInfo getConsumerReconnectInfo() {
      return this.consumerReconnectInfo;
   }

   public void setConsumerReconnectInfo(ConsumerReconnectInfo var1) {
      this.consumerReconnectInfo = var1;
   }

   public FEConsumerCreateRequest() {
   }

   protected byte getVersion(Object var1) throws IOException {
      if (var1 instanceof PeerInfoable) {
         PeerInfo var2 = ((PeerInfoable)var1).getPeerInfo();
         if (var2.compareTo(PeerInfo.VERSION_61) < 0) {
            throw JMSUtilities.versionIOException(0, 1, 14);
         }

         if (var2.compareTo(PeerInfo.VERSION_811) < 0) {
            return 1;
         }

         if (var2.compareTo(PeerInfo.VERSION_920) < 0) {
            return 10;
         }

         if (var2.compareTo(PeerInfo.VERSION_1033) < 0) {
            return 12;
         }
      }

      return 14;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1 = this.getVersionedStream(var1);
      byte var2 = this.getVersion(var1);
      int var3 = var2;
      boolean var4 = false;
      if (this.name != null) {
         var3 = var2 | 512;
      }

      if (var2 == 1 && this.name != null && this.clientId != null) {
         var3 |= 256;
         var4 = true;
      }

      if (var2 >= 12 && this.consumerReconnectInfo != null) {
         var3 |= 16384;
      }

      if (this.selector != null) {
         var3 |= 1024;
      }

      if (this.noLocal) {
         var3 |= 2048;
      }

      if (this.redeliveryDelay != 0L) {
         var3 |= 4096;
      }

      if (this.destination instanceof DistributedDestinationImpl) {
         var3 |= 8192;
      }

      var1.writeInt(var3);
      super.writeExternal(var1);
      if (var4) {
         var1.writeUTF(this.clientId);
      }

      if (this.name != null) {
         var1.writeUTF(this.name);
      }

      if (this.selector != null) {
         var1.writeUTF(this.selector);
      }

      this.destination.writeExternal(var1);
      var1.writeInt(this.messagesMaximum);
      if (this.redeliveryDelay != 0L) {
         var1.writeLong(this.redeliveryDelay);
      }

      if ((var3 & 16384) != 0) {
         this.consumerReconnectInfo.writeExternal(var1);
      }

      if (var2 >= 14) {
         var1.writeInt(this.subscriptionSharingPolicy);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 >= 1 && var3 <= 14) {
         super.readExternal(var1);
         if ((var2 & 256) != 0) {
            this.clientId = var1.readUTF();
         }

         if ((var2 & 512) != 0) {
            this.name = var1.readUTF();
         }

         if ((var2 & 1024) != 0) {
            this.selector = var1.readUTF();
         }

         if ((var2 & 2048) != 0) {
            this.noLocal = true;
         }

         if ((var2 & 8192) != 0) {
            this.destination = new DistributedDestinationImpl();
            this.destination.readExternal(var1);
         } else {
            this.destination = new DestinationImpl();
            this.destination.readExternal(var1);
         }

         this.messagesMaximum = var1.readInt();
         if ((var2 & 4096) != 0) {
            this.redeliveryDelay = var1.readLong();
         }

         if ((var2 & 16384) != 0) {
            this.consumerReconnectInfo = new ConsumerReconnectInfo();
            this.consumerReconnectInfo.readExternal(var1);
         }

         if (var3 >= 14) {
            this.subscriptionSharingPolicy = var1.readInt();
         }

      } else {
         throw JMSUtilities.versionIOException(var3, 1, 14);
      }
   }
}
