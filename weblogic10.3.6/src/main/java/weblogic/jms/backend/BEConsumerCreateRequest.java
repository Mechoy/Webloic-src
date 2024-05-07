package weblogic.jms.backend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.common.ConsumerReconnectInfo;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.dispatcher.Request;
import weblogic.messaging.dispatcher.Response;
import weblogic.store.PersistentHandle;

public final class BEConsumerCreateRequest extends Request implements Externalizable {
   static final long serialVersionUID = 3890402952967020905L;
   private static final int EXTVERSION61 = 1;
   private static final int EXTVERSION92 = 2;
   private static final int EXTVERSION1033 = 3;
   private static final int EXTVERSION = 3;
   private static final int VERSION_MASK = 255;
   private static final int CLIENT_ID_MASK = 256;
   private static final int NAME_MASK = 512;
   private static final int SELECTOR_MASK = 1024;
   private static final int SUBJECT_MASK = 2048;
   private static final int RECONNECT_MASK = 4096;
   private JMSID beConnId;
   private JMSID consumerId;
   private String clientId;
   private String name;
   private JMSID destinationId;
   private String selector;
   private boolean noLocal;
   private int messagesMaximum;
   private int flag;
   private long redeliveryDelay;
   private String subject;
   private ConsumerReconnectInfo consumerReconnectInfo;
   private int clientIdPolicy;
   private int subscriptionSharingPolicy;
   private transient PersistentHandle persistentHandle;

   public BEConsumerCreateRequest(JMSID var1, JMSID var2, JMSID var3, String var4, String var5, JMSID var6, String var7, boolean var8, int var9, int var10, long var11, String var13, ConsumerReconnectInfo var14) {
      this(var1, var2, var3, var4, 0, var5, var6, var7, var8, var9, var10, var11, var13, var14, 0);
   }

   public BEConsumerCreateRequest(JMSID var1, JMSID var2, JMSID var3, String var4, int var5, String var6, JMSID var7, String var8, boolean var9, int var10, int var11, long var12, String var14, ConsumerReconnectInfo var15, int var16) {
      super(var2, 10256);
      this.clientIdPolicy = 0;
      this.subscriptionSharingPolicy = 0;
      this.beConnId = var1;
      this.consumerId = var3;
      this.clientId = var4;
      this.name = var6;
      this.destinationId = var7;
      this.selector = var8;
      this.noLocal = var9;
      this.messagesMaximum = var10;
      this.flag = var11;
      this.redeliveryDelay = var12;
      this.subject = var14;
      this.consumerReconnectInfo = var15;
      this.clientIdPolicy = var5;
      this.subscriptionSharingPolicy = var16;
   }

   public final JMSID getConsumerId() {
      return this.consumerId;
   }

   public final String getClientId() {
      return this.clientId;
   }

   final void setClientId(String var1) {
      this.clientId = var1;
   }

   public final String getName() {
      return this.name;
   }

   final void setName(String var1) {
      this.name = var1;
   }

   public final JMSID getDestinationId() {
      return this.destinationId;
   }

   final String getSelector() {
      return this.selector;
   }

   public final boolean getNoLocal() {
      return this.noLocal;
   }

   public void setNoLocal(boolean var1) {
      this.noLocal = var1;
   }

   public final int getMessagesMaximum() {
      return this.messagesMaximum;
   }

   public final String getSubject() {
      return this.subject;
   }

   public final int getFlag() {
      return this.flag;
   }

   public final long getRedeliveryDelay() {
      return this.redeliveryDelay;
   }

   final PersistentHandle getPersistentHandle() {
      return this.persistentHandle;
   }

   final void setPersistentHandle(PersistentHandle var1) {
      this.persistentHandle = var1;
   }

   public ConsumerReconnectInfo getConsumerReconnectInfo() {
      return this.consumerReconnectInfo;
   }

   void setClientIdPolicy(int var1) {
      this.clientIdPolicy = var1;
   }

   public final int getClientIdPolicy() {
      return this.clientIdPolicy;
   }

   void setSubscriptionSharingPolicy(int var1) {
      this.subscriptionSharingPolicy = var1;
   }

   public final int getSubscriptionSharingPolicy() {
      return this.subscriptionSharingPolicy;
   }

   public final void setConsumerReconnectInfo(ConsumerReconnectInfo var1) {
      this.consumerReconnectInfo = var1;
   }

   public int remoteSignature() {
      return 18;
   }

   public Response createResponse() {
      return new BEConsumerCreateResponse();
   }

   public BEConsumerCreateRequest() {
      this.clientIdPolicy = 0;
      this.subscriptionSharingPolicy = 0;
   }

   private byte getVersion(Object var1) throws IOException {
      if (var1 instanceof PeerInfoable) {
         PeerInfo var2 = ((PeerInfoable)var1).getPeerInfo();
         if (var2.compareTo(PeerInfo.VERSION_61) < 0) {
            throw JMSUtilities.versionIOException(0, 1, 2);
         }

         if (var2.compareTo(PeerInfo.VERSION_920) < 0) {
            return 1;
         }

         if (var2.compareTo(PeerInfo.VERSION_1033) < 0) {
            return 3;
         }
      }

      return 3;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1 = this.getVersionedStream(var1);
      byte var2 = this.getVersion(var1);
      int var3;
      if (var2 >= 2 && this.consumerReconnectInfo != null) {
         var3 = var2 | 4096;
      } else {
         var3 = var2;
      }

      if (this.clientId != null) {
         var3 |= 256;
      }

      if (this.name != null) {
         var3 |= 512;
      }

      if (this.selector != null) {
         var3 |= 1024;
      }

      if (this.subject != null) {
         var3 |= 2048;
      }

      var1.writeInt(var3);
      super.writeExternal(var1);
      this.beConnId.writeExternal(var1);
      this.consumerId.writeExternal(var1);
      this.destinationId.writeExternal(var1);
      if (this.clientId != null) {
         var1.writeUTF(this.clientId);
      }

      if (this.name != null) {
         var1.writeUTF(this.name);
      }

      if (this.selector != null) {
         var1.writeUTF(this.selector);
      }

      var1.writeBoolean(this.noLocal);
      var1.writeInt(this.messagesMaximum);
      var1.writeInt(this.flag);
      var1.writeLong(this.redeliveryDelay);
      if (this.subject != null) {
         var1.writeUTF(this.subject);
      }

      if ((var3 & 4096) != 0) {
         this.consumerReconnectInfo.writeExternal(var1);
      }

      if (var2 >= 3) {
         var1.writeInt(this.clientIdPolicy);
         var1.writeInt(this.subscriptionSharingPolicy);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1 && 2 != var3 && var3 != 3) {
         throw JMSUtilities.versionIOException(var3, 1, 3);
      } else {
         super.readExternal(var1);
         this.beConnId = new JMSID();
         this.beConnId.readExternal(var1);
         this.consumerId = new JMSID();
         this.consumerId.readExternal(var1);
         this.destinationId = new JMSID();
         this.destinationId.readExternal(var1);
         if ((var2 & 256) != 0) {
            this.clientId = var1.readUTF();
         }

         if ((var2 & 512) != 0) {
            this.name = var1.readUTF();
         }

         if ((var2 & 1024) != 0) {
            this.selector = var1.readUTF();
         }

         this.noLocal = var1.readBoolean();
         this.messagesMaximum = var1.readInt();
         this.flag = var1.readInt();
         this.redeliveryDelay = var1.readLong();
         if ((var2 & 2048) != 0) {
            this.subject = var1.readUTF();
         }

         if ((var2 & 4096) != 0) {
            this.consumerReconnectInfo = new ConsumerReconnectInfo();
            this.consumerReconnectInfo.readExternal(var1);
         }

         if (var3 >= 3) {
            this.clientIdPolicy = var1.readInt();
            this.subscriptionSharingPolicy = var1.readInt();
         }

      }
   }
}
