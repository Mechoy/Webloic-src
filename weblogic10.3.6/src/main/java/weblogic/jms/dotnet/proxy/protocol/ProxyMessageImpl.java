package weblogic.jms.dotnet.proxy.protocol;

import java.util.Enumeration;
import java.util.Iterator;
import javax.jms.JMSException;
import javax.jms.Message;
import weblogic.jms.common.JMSMessageId;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.dotnet.proxy.util.ProxyUtil;
import weblogic.jms.dotnet.transport.MarshalReadable;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWritable;
import weblogic.jms.dotnet.transport.MarshalWriter;
import weblogic.jms.extensions.WLMessage;

public abstract class ProxyMessageImpl implements MarshalReadable, MarshalWritable {
   private static final int EXTVERSION = 1;
   public static final byte NULLMESSAGEIMPL = 0;
   public static final byte BYTESMESSAGEIMPL = 1;
   public static final byte HDRMESSAGEIMPL = 2;
   public static final byte MAPMESSAGEIMPL = 3;
   public static final byte OBJECTMESSAGEIMPL = 4;
   public static final byte STREAMMESSAGEIMPL = 5;
   public static final byte TEXTMESSAGEIMPL = 6;
   static final String[] headerFields = new String[]{"JMSCorrelationID", "JMSDeliveryMode", "JMSDestination", "JMSExpiration", "JMSPriority", "JMSRedelivered", "JMSReplyTo", "JMSTimestamp", "JMSType"};
   private int deliveryMode;
   private int deliveryCount;
   private int redeliveryLimit;
   private int seed;
   private long timestamp;
   private int counter;
   private long expiration;
   private byte priority;
   private String messageIdString;
   private JMSMessageId messageId;
   private String correlationId;
   private ProxyDestinationImpl destination;
   private ProxyDestinationImpl replyTo;
   private String type;
   private PrimitiveMap properties;
   private boolean redelivered;
   private long sequenceNumber;
   private int pipelineGeneration;
   private transient Message message;
   private static final int _IS_PERSISTENT = 1;
   private static final int _HAS_CORRID = 2;
   private static final int _HAS_DESTINATION = 3;
   private static final int _HAS_REPLYTO = 4;
   private static final int _IS_REDELIVERED = 5;
   private static final int _HAS_TYPE = 6;
   private static final int _HAS_EXPIRATION = 7;
   private static final int _HAS_STRMESSAGEID = 8;
   private static final int _HAS_PROPERTIES = 9;
   private static final int _HAS_TIMESTAMP = 10;
   private static final int _HAS_SEQUENCE_NUMBER = 11;
   private static final int _HAS_PIPELINE_GENERATION = 12;
   private static final int _HAS_DELIVERY_COUNT = 13;
   private static final int _HAS_REDELIVERY_LIMIT = 14;
   private static final int _HAS_WLMESSAGEID = 15;

   public ProxyMessageImpl() {
      this.deliveryMode = -1;
      this.deliveryCount = 1;
      this.redeliveryLimit = -1;
      this.timestamp = -1L;
      this.priority = -1;
      this.sequenceNumber = -1L;
      this.pipelineGeneration = 0;
      this.properties = new PrimitiveMap();
   }

   public abstract byte getType();

   public static ProxyMessageImpl createMessageImpl(byte var0) {
      switch (var0) {
         case 1:
            return new ProxyBytesMessageImpl();
         case 2:
            return new ProxyHdrMessageImpl();
         case 3:
            return new ProxyMapMessageImpl();
         case 4:
            return new ProxyObjectMessageImpl();
         case 5:
            return new ProxyStreamMessageImpl();
         case 6:
            return new ProxyTextMessageImpl();
         default:
            return null;
      }
   }

   public ProxyMessageImpl(Message var1) throws JMSException {
      this();
      this.initializeFromMessage(var1);
   }

   public void setSequenceNumber(long var1) {
      this.sequenceNumber = var1;
   }

   public void setPipelineGeneration(int var1) {
      this.pipelineGeneration = var1;
   }

   public final void setRedelivered(boolean var1) {
      this.redelivered = var1;
   }

   public void setDeliveryCount(int var1) {
      this.deliveryCount = var1;
   }

   private void initializeFromMessage(Message var1) throws JMSException {
      this.message = var1;
      this.timestamp = var1.getJMSTimestamp();
      this.type = var1.getJMSType();
      this.correlationId = var1.getJMSCorrelationID();
      if (var1.getJMSReplyTo() != null) {
         this.replyTo = new ProxyDestinationImpl(var1.getJMSReplyTo());
      } else {
         this.replyTo = null;
      }

      this.redelivered = var1.getJMSRedelivered();
      this.expiration = var1.getJMSExpiration();
      this.priority = (byte)var1.getJMSPriority();
      this.deliveryMode = var1.getJMSDeliveryMode();
      Enumeration var2 = var1.getPropertyNames();
      if (var2.hasMoreElements()) {
         this.properties = new PrimitiveMap();

         while(var2.hasMoreElements()) {
            String var3 = (String)var2.nextElement();
            if (!var3.equals("JMSXDeliveryCount") && !var3.equals("JMS_BEA_RedeliveryLimit")) {
               Object var4 = var1.getObjectProperty(var3);
               this.setObjectProperty(var3, var4);
            }
         }
      }

      if (var1 instanceof WLMessage) {
         if (((MessageImpl)var1).isOldMessage()) {
            this.messageIdString = var1.getJMSMessageID();
         } else {
            this.messageId = ((MessageImpl)var1).getId();
            this.seed = this.messageId.getSeed();
            this.counter = this.messageId.getCounter();
         }

         this.sequenceNumber = ((WLMessage)var1).getSequenceNumber();
         if (((WLMessage)var1).getUnitOfOrder() != null) {
            this.properties.put((String)"JMS_BEA_UnitOfOrder", ((WLMessage)var1).getUnitOfOrder());
         }

         this.deliveryCount = ((MessageImpl)var1).getDeliveryCount();
         this.redeliveryLimit = ((WLMessage)var1).getJMSRedeliveryLimit();
      } else {
         this.messageIdString = var1.getJMSMessageID();
      }

   }

   public final String getCorrelationID() {
      return this.correlationId;
   }

   public final String getMessageID() {
      if (this.messageIdString != null) {
         return this.messageIdString;
      } else {
         return this.messageId != null ? "ID:" + this.messageId.toString() : "null";
      }
   }

   public final int getDeliveryMode() {
      return this.deliveryMode;
   }

   public final ProxyDestinationImpl getDestination() {
      return this.destination;
   }

   public final long getExpiration() {
      return this.expiration;
   }

   public final long getDeliveryTime() {
      return (Long)this.properties.get("JMS_BEA_DeliveryTime");
   }

   public final int getPriority() {
      return this.priority;
   }

   public long getSequenceNumber() {
      return this.sequenceNumber;
   }

   public Object getProperty(String var1) {
      return this.properties.get(var1);
   }

   public final void clearProperties() {
      if (this.properties != null) {
         this.properties.clear();
      }

   }

   boolean hasProperties() {
      return this.properties != null && !this.properties.isEmpty();
   }

   public final boolean propertyExists(String var1) {
      return this.properties.get(var1) != null;
   }

   public void removeProperty(String var1) {
      this.properties.remove(var1);
   }

   public final void setObjectProperty(String var1, Object var2) {
      this.properties.put(var1, var2);
   }

   public long getTimestamp() {
      return this.timestamp;
   }

   public void populateJMSMessage(Message var1) throws JMSException {
      if (this.correlationId != null) {
         var1.setJMSCorrelationID(this.correlationId);
      }

      var1.setJMSType(this.type);
      if (this.replyTo != null) {
         var1.setJMSReplyTo(this.replyTo.getJMSDestination());
      }

      if (this.redelivered) {
         var1.setJMSRedelivered(true);
      }

      if (this.properties != null) {
         Iterator var2 = this.properties.keySet().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            Object var4 = this.properties.get(var3);
            var1.setObjectProperty(var3, var4);
         }
      }

   }

   public void marshal(MarshalWriter var1) {
      MarshalBitMask var2 = new MarshalBitMask(1);
      if (this.messageIdString != null) {
         var2.setBit(8);
      }

      if (this.messageId != null) {
         var2.setBit(15);
      }

      if (this.timestamp != -1L) {
         var2.setBit(10);
      }

      if (this.deliveryMode == 2) {
         var2.setBit(1);
      }

      if (this.correlationId != null) {
         var2.setBit(2);
      }

      if (this.destination != null) {
         var2.setBit(3);
      }

      if (this.replyTo != null) {
         var2.setBit(4);
      }

      if (this.redelivered) {
         var2.setBit(5);
      }

      if (this.type != null) {
         var2.setBit(6);
      }

      if (this.deliveryCount != 1) {
         var2.setBit(13);
      }

      if (this.redeliveryLimit != -1) {
         var2.setBit(14);
      }

      if (this.expiration != 0L) {
         var2.setBit(7);
      }

      boolean var3 = this.hasProperties();
      if (var3) {
         var2.setBit(9);
      }

      if (this.sequenceNumber != -1L) {
         var2.setBit(11);
      }

      if (this.pipelineGeneration != 0) {
         var2.setBit(12);
      }

      var2.marshal(var1);
      if (this.messageIdString != null) {
         var1.writeString(this.messageIdString);
      }

      if (this.messageId != null) {
         var1.writeInt(this.seed);
         var1.writeInt(this.counter);
      }

      if (this.timestamp != -1L) {
         var1.writeLong(this.timestamp);
      }

      if (this.correlationId != null) {
         var1.writeString(this.correlationId);
      }

      if (this.destination != null) {
         this.destination.marshal(var1);
      }

      if (this.replyTo != null) {
         this.replyTo.marshal(var1);
      }

      if (this.type != null) {
         var1.writeString(this.type);
      }

      if (this.expiration != 0L) {
         var1.writeLong(this.expiration);
      }

      if (this.deliveryCount != 1) {
         var1.writeInt(this.deliveryCount);
      }

      if (this.redeliveryLimit != -1) {
         var1.writeInt(this.redeliveryLimit);
      }

      var1.writeByte(this.priority);
      if (var3) {
         this.properties.marshal(var1);
      }

      if (this.sequenceNumber != -1L) {
         var1.writeLong(this.sequenceNumber);
      }

      if (this.pipelineGeneration != 0) {
         var1.writeInt(this.pipelineGeneration);
      }

   }

   public void unmarshal(MarshalReader var1) {
      MarshalBitMask var2 = new MarshalBitMask();
      var2.unmarshal(var1);
      ProxyUtil.checkVersion(var2.getVersion(), 1, 1);
      if (var2.isSet(1)) {
         this.deliveryMode = 2;
      } else {
         this.deliveryMode = 1;
      }

      if (var2.isSet(8)) {
         this.messageIdString = var1.readString();
      }

      if (var2.isSet(15)) {
         this.seed = var1.readInt();
         this.counter = var1.readInt();
         this.messageIdString = "ID:<" + this.seed + "." + this.timestamp + "." + this.counter + ">";
      }

      if (var2.isSet(10)) {
         this.timestamp = var1.readLong();
      }

      if (var2.isSet(2)) {
         this.correlationId = var1.readString();
      }

      if (var2.isSet(3)) {
         this.destination = new ProxyDestinationImpl();
         this.destination.unmarshal(var1);
      }

      if (var2.isSet(4)) {
         this.replyTo = new ProxyDestinationImpl();
         this.replyTo.unmarshal(var1);
      }

      this.redelivered = var2.isSet(5);
      if (var2.isSet(6)) {
         this.type = var1.readString();
      }

      if (var2.isSet(7)) {
         this.expiration = var1.readLong();
      }

      if (var2.isSet(13)) {
         this.deliveryCount = var1.readInt();
      }

      if (var2.isSet(14)) {
         this.redeliveryLimit = var1.readInt();
      }

      this.priority = var1.readByte();
      if (var2.isSet(9)) {
         this.properties = new PrimitiveMap();
         this.properties.unmarshal(var1);
      }

      if (var2.isSet(11)) {
         this.sequenceNumber = var1.readLong();
      }

      if (var2.isSet(12)) {
         this.pipelineGeneration = var1.readInt();
      }

   }

   public static boolean isHeaderField(String var0) {
      if (var0.startsWith("JMS")) {
         for(int var1 = 0; var1 < headerFields.length; ++var1) {
            if (var0.equals(headerFields[var1])) {
               return true;
            }
         }
      }

      return false;
   }
}
