package weblogic.messaging.saf.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Enumeration;
import java.util.Properties;
import weblogic.messaging.Message;
import weblogic.messaging.MessageID;
import weblogic.messaging.saf.SAFRequest;
import weblogic.messaging.saf.utils.Util;

public final class SAFRequestImpl implements SAFRequest, Message, Cloneable {
   static final long serialVersionUID = -8254052743194579406L;
   private static final int EXTVERSION = 1;
   private static final int VERSION_MASK = 255;
   private static final int _HASPAYLOAD = 131072;
   private static final int _ISLASTMESSAGE = 262144;
   private static final int _HASPAYLOADCONTEXT = 524288;
   private boolean isLastMessage;
   private int deliveryMode = 2;
   private String conversationName;
   private String messageId;
   private long timestamp;
   private long timeToLive;
   private long sequenceNumber;
   private Externalizable payload;
   private Externalizable payloadContext;
   private Properties properties;
   private long userdataSize = 40L;
   private long payloadSize;
   private AgentDeliverRequest agentRequest;

   public final String getMessageId() {
      return this.messageId;
   }

   public final void setMessageId(String var1) {
      this.messageId = var1;
      this.userdataSize += (long)var1.length();
   }

   public final String getConversationName() {
      return this.conversationName;
   }

   public final void setConversationName(String var1) {
      this.conversationName = var1;
      this.userdataSize += (long)var1.length();
   }

   public final long getSequenceNumber() {
      return this.sequenceNumber;
   }

   public final void setSequenceNumber(long var1) {
      this.sequenceNumber = var1;
   }

   public final int getDeliveryMode() {
      return this.deliveryMode;
   }

   public final void setDeliveryMode(int var1) {
      this.deliveryMode = var1;
   }

   public final long getTimeToLive() {
      return this.timeToLive;
   }

   public final void setTimeToLive(long var1) {
      this.timeToLive = var1;
   }

   public final long getTimestamp() {
      return this.timestamp;
   }

   public final void setTimestamp(long var1) {
      this.timestamp = var1;
   }

   public final boolean isEndOfConversation() {
      return this.isLastMessage;
   }

   public final void setEndOfConversation(boolean var1) {
      this.isLastMessage = var1;
   }

   public final Externalizable getPayload() {
      return this.payload;
   }

   public final void setPayload(Externalizable var1) {
      this.payload = var1;
   }

   public void setPayloadContext(Externalizable var1) {
      this.payloadContext = var1;
   }

   public Externalizable getPayloadContext() {
      return this.payloadContext;
   }

   public long getPayloadSize() {
      return this.payloadSize;
   }

   public void setPayloadSize(long var1) {
      this.payloadSize = var1;
   }

   public MessageID getMessageID() {
      return new SAFMessageID(this.messageId);
   }

   public long size() {
      return this.userdataSize + this.payloadSize;
   }

   public Message duplicate() {
      try {
         return (Message)this.clone();
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException(var2);
      }
   }

   public Object getWorkContext() {
      return null;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      byte var2 = 1;
      int var3 = var2;
      if (this.payload != null) {
         var3 = var2 | 131072;
         if (this.payloadContext != null) {
            var3 |= 524288;
         }
      }

      if (this.isLastMessage) {
         var3 |= 262144;
      }

      var1.writeInt(var3);
      var1.writeInt(this.deliveryMode);
      var1.writeInt(0);
      var1.writeUTF(this.messageId);
      var1.writeUTF(this.conversationName);
      var1.writeLong(this.sequenceNumber);
      var1.writeLong(this.timeToLive);
      var1.writeLong(this.timestamp);
      if (this.payload != null) {
         var1.writeObject(this.payload);
         var1.writeLong(this.payloadSize);
         if (this.payloadContext != null) {
            var1.writeObject(this.payloadContext);
         }
      }

      if (this.properties != null && this.properties.size() > 0) {
         var1.writeInt(this.properties.size());
         Enumeration var4 = this.properties.keys();

         while(var4.hasMoreElements()) {
            String var5 = (String)var4.nextElement();
            var1.writeObject(var5);
            var1.writeObject(this.properties.get(var5));
         }
      } else {
         var1.writeInt(0);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 != 1) {
         throw Util.versionIOException(var3, 1, 1);
      } else {
         this.deliveryMode = var1.readInt();
         var1.readInt();
         this.messageId = var1.readUTF();
         this.conversationName = var1.readUTF();
         this.sequenceNumber = var1.readLong();
         this.timeToLive = var1.readLong();
         this.timestamp = var1.readLong();
         if ((var2 & 262144) != 0) {
            this.isLastMessage = true;
         }

         if ((var2 & 131072) != 0) {
            this.payload = (Externalizable)var1.readObject();
            this.payloadSize = var1.readLong();
            if ((var2 & 524288) != 0) {
               this.payloadContext = (Externalizable)var1.readObject();
            }
         }

         int var4 = var1.readInt();
         if (var4 > 0) {
            this.properties = new Properties();

            for(int var5 = 0; var5 < var4; ++var5) {
               String var6 = (String)var1.readObject();
               this.properties.put(var6, var1.readObject());
            }
         }

      }
   }

   public void setAgentRequest(AgentDeliverRequest var1) {
      this.agentRequest = var1;
   }

   public AgentDeliverRequest getAgentRequest() {
      return this.agentRequest;
   }

   public synchronized boolean isExpired() {
      if (this.timeToLive == 0L) {
         return false;
      } else {
         return System.currentTimeMillis() - this.timestamp > this.timeToLive;
      }
   }
}
