package weblogic.jms.dotnet.proxy.protocol;

import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWriter;

public final class ProxyProducerSendResponse extends ProxyResponse {
   private static final int EXTVERSION = 1;
   private static final int _HAS_DELIVERY_MODE = 1;
   private static final int _HAS_PRIORITY = 2;
   private static final int _HAS_TIME_STAMP = 3;
   private static final int _HAS_EXPIRATION = 4;
   private static final int _HAS_TIME_TO_DELIVER = 5;
   private static final int _HAS_FLOW_CONTROL = 6;
   private static final int _HAS_REDELIVERY_LIMIT = 7;
   private static final int _HAS_WLMESSAGEID = 8;
   private String messageId;
   private int deliveryMode = -1;
   private int priority;
   private int seed;
   private long timestamp;
   private int counter;
   private long expirationTime;
   private long deliveryTime;
   private int redeliveryLimit;
   private long flowControlTime;

   public ProxyProducerSendResponse(String var1) {
      this.messageId = var1;
      this.versionFlags = new MarshalBitMask(1);
   }

   public ProxyProducerSendResponse(int var1, int var2) {
      this.seed = var1;
      this.counter = var2;
      this.versionFlags = new MarshalBitMask(1);
      this.versionFlags.setBit(8);
   }

   public String getMessageId() {
      return this.messageId != null ? this.messageId : "ID:<" + this.seed + "." + this.timestamp + "." + this.counter + ">";
   }

   public void setDeliveryMode(int var1) {
      this.versionFlags.setBit(1);
      this.deliveryMode = var1;
   }

   public int getDeliveryMode() {
      return !this.versionFlags.isSet(1) ? -1 : this.deliveryMode;
   }

   public void setPriority(int var1) {
      this.versionFlags.setBit(2);
      this.priority = var1;
   }

   public int getPriority() {
      return !this.versionFlags.isSet(2) ? -1 : this.priority;
   }

   public void setExpirationTime(long var1) {
      this.versionFlags.setBit(4);
      this.expirationTime = var1;
   }

   public long getExpirationTime() {
      return !this.versionFlags.isSet(4) ? -1L : this.expirationTime;
   }

   public void setTimestamp(long var1) {
      this.versionFlags.setBit(3);
      this.timestamp = var1;
   }

   public long getTimestamp() {
      return !this.versionFlags.isSet(3) ? -1L : this.timestamp;
   }

   public void setDeliveryTime(long var1) {
      this.versionFlags.setBit(5);
      this.deliveryTime = var1;
   }

   public long getTimeToDeliver() {
      return !this.versionFlags.isSet(5) ? -1L : this.deliveryTime;
   }

   public void setRedeliveryLimit(int var1) {
      this.versionFlags.setBit(7);
      this.redeliveryLimit = var1;
   }

   public int getRedeliveryLimit() {
      return !this.versionFlags.isSet(7) ? 0 : this.redeliveryLimit;
   }

   public void setFlowControlTime(long var1) {
      if (var1 != 0L) {
         this.versionFlags.setBit(6);
      }

      this.flowControlTime = var1;
   }

   public long getFlowControlTime() {
      return !this.versionFlags.isSet(6) ? 0L : this.flowControlTime;
   }

   public ProxyProducerSendResponse() {
   }

   public int getMarshalTypeCode() {
      return 26;
   }

   public void marshal(MarshalWriter var1) {
      this.versionFlags.marshal(var1);
      if (this.versionFlags.isSet(8)) {
         var1.writeInt(this.seed);
         var1.writeInt(this.counter);
      } else {
         var1.writeString(this.messageId);
      }

      if (this.versionFlags.isSet(1)) {
         var1.writeInt(this.deliveryMode);
      }

      if (this.versionFlags.isSet(2)) {
         var1.writeInt(this.priority);
      }

      if (this.versionFlags.isSet(4)) {
         var1.writeLong(this.expirationTime);
      }

      if (this.versionFlags.isSet(3)) {
         var1.writeLong(this.timestamp);
      }

      if (this.versionFlags.isSet(5)) {
         var1.writeLong(this.deliveryTime);
      }

      if (this.versionFlags.isSet(7)) {
         var1.writeInt(this.redeliveryLimit);
      }

      if (this.versionFlags.isSet(6)) {
         var1.writeLong(this.flowControlTime);
      }

   }

   public void unmarshal(MarshalReader var1) {
      MarshalBitMask var2 = new MarshalBitMask();
      var2.unmarshal(var1);
      ProxyUtil.checkVersion(var2.getVersion(), 1, 1);
      if (var2.isSet(8)) {
         this.seed = var1.readInt();
         this.counter = var1.readInt();
      } else {
         this.messageId = var1.readString();
      }

      if (var2.isSet(1)) {
         this.deliveryMode = var1.readInt();
      }

      if (var2.isSet(2)) {
         this.priority = var1.readInt();
      }

      if (var2.isSet(4)) {
         this.expirationTime = var1.readLong();
      }

      if (var2.isSet(3)) {
         this.timestamp = var1.readLong();
      }

      if (var2.isSet(5)) {
         this.deliveryTime = var1.readLong();
      }

      if (var2.isSet(7)) {
         this.redeliveryLimit = var1.readInt();
      }

      if (var2.isSet(6)) {
         this.flowControlTime = var1.readLong();
      }

   }
}
