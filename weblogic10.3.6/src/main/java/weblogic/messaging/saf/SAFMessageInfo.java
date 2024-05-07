package weblogic.messaging.saf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.SimpleType;
import weblogic.messaging.runtime.MessageInfo;
import weblogic.utils.encoders.BASE64Decoder;
import weblogic.utils.encoders.BASE64Encoder;

public class SAFMessageInfo extends MessageInfo {
   private static final String OPEN_TYPE_NAME = "SAFMessageInfo";
   private static final String OPEN_DESCRIPTION = "This object represents information about a SAFRequest instance.";
   private static final String ITEM_VERSION_NUMBER = "VersionNumber";
   private static final String ITEM_CONVERSATION_NAME = "ConversationName";
   private static final String ITEM_DELIVERY_MODE = "DeliveryMode";
   private static final String ITEM_MESSAGE_ID = "MessageId";
   private static final String ITEM_PAYLOAD_SIZE = "PayloadSize";
   private static final String ITEM_SEQUENCE_NUMBER = "SequenceNumber";
   private static final String ITEM_TIMESTAMP = "Timestamp";
   private static final String ITEM_TIME_TO_LIVE = "TimeToLive";
   private static final String ITEM_END_OF_CONVERSATION = "EndOfConversation";
   private static final String ITEM_DESTINATION_URL = "DestinationURL";
   private static final String ITEM_SERIALIZED_REQUEST = "SerializedRequest";
   private static final int VERSION = 1;
   private String conversationName;
   private int deliveryMode;
   private String messageId;
   private long payloadSize;
   private long sequenceNumber;
   private long timestamp;
   private long timeToLive;
   private boolean endOfConversation;
   private String destinationURL;
   private SAFRequest safRequest;

   public SAFMessageInfo() {
   }

   public SAFMessageInfo(CompositeData var1) throws OpenDataException {
      super(var1);
   }

   public SAFMessageInfo(long var1, int var3, String var4, long var5, String var7, SAFRequest var8, String var9) {
      super(var1, var3, var4, var5, var7);
      this.safRequest = var8;
      this.conversationName = var8.getConversationName();
      this.deliveryMode = var8.getDeliveryMode();
      this.messageId = var8.getMessageId();
      this.payloadSize = var8.getPayloadSize();
      this.sequenceNumber = var8.getSequenceNumber();
      this.timestamp = var8.getTimestamp();
      this.timeToLive = var8.getTimeToLive();
      this.endOfConversation = var8.isEndOfConversation();
      this.destinationURL = var9;
   }

   protected void initOpenInfo() {
      super.initOpenInfo();
      this.openItemNames.add("VersionNumber");
      this.openItemNames.add("ConversationName");
      this.openItemNames.add("DeliveryMode");
      this.openItemNames.add("MessageId");
      this.openItemNames.add("PayloadSize");
      this.openItemNames.add("Timestamp");
      this.openItemNames.add("TimeToLive");
      this.openItemNames.add("EndOfConversation");
      this.openItemNames.add("DestinationURL");
      this.openItemNames.add("SerializedRequest");
      this.openItemDescriptions.add("The version number.");
      this.openItemDescriptions.add("The name that uniquely identifies the SAF conversation.");
      this.openItemDescriptions.add("Indicates whether the delivery mode is persistent or non-persistent.");
      this.openItemDescriptions.add("The message ID of the request.");
      this.openItemDescriptions.add("The size of the payload in bytes.");
      this.openItemDescriptions.add("The timestamp of the message in milliseconds.");
      this.openItemDescriptions.add("The time-to-live value in milliseconds.");
      this.openItemDescriptions.add("Indicates whether this is the last message in the conversation.");
      this.openItemDescriptions.add("The URL of the destination to which this message is to be forwarded to.");
      this.openItemDescriptions.add("The serialized SAFRequest.");
      this.openItemTypes.add(SimpleType.INTEGER);
      this.openItemTypes.add(SimpleType.STRING);
      this.openItemTypes.add(SimpleType.INTEGER);
      this.openItemTypes.add(SimpleType.STRING);
      this.openItemTypes.add(SimpleType.LONG);
      this.openItemTypes.add(SimpleType.LONG);
      this.openItemTypes.add(SimpleType.LONG);
      this.openItemTypes.add(SimpleType.BOOLEAN);
      this.openItemTypes.add(SimpleType.STRING);
      this.openItemTypes.add(SimpleType.STRING);
   }

   public String getConversationName() {
      return this.conversationName;
   }

   public void setConversationName(String var1) {
      this.conversationName = var1;
   }

   public int getDeliveryMode() {
      return this.deliveryMode;
   }

   public void setDeliveryMode(int var1) {
      this.deliveryMode = var1;
   }

   public boolean isEndOfConversation() {
      return this.endOfConversation;
   }

   public void setEndOfConversation(boolean var1) {
      this.endOfConversation = var1;
   }

   public String getMessageId() {
      return this.messageId;
   }

   public void setMessageId(String var1) {
      this.messageId = var1;
   }

   public long getPayloadSize() {
      return this.payloadSize;
   }

   public void setPayloadSize(long var1) {
      this.payloadSize = var1;
   }

   public SAFRequest getSAFRequest() {
      return this.safRequest;
   }

   public void setSAFRequest(SAFRequest var1) {
      this.safRequest = var1;
   }

   public long getSequenceNumber() {
      return this.sequenceNumber;
   }

   public void setSequenceNumber(long var1) {
      this.sequenceNumber = var1;
   }

   public long getTimestamp() {
      return this.timestamp;
   }

   public void setTimestamp(long var1) {
      this.timestamp = var1;
   }

   public long getTimeToLive() {
      return this.timeToLive;
   }

   public void setTimeToLive(long var1) {
      this.timeToLive = var1;
   }

   public String getDestinationURL() {
      return this.destinationURL;
   }

   public void setDestinationURL(String var1) {
      this.destinationURL = var1;
   }

   public Externalizable getMessage() {
      return this.safRequest != null ? this.safRequest.getPayload() : null;
   }

   public static Object from(CompositeData var0) throws OpenDataException {
      SAFMessageInfo var1 = new SAFMessageInfo();
      var1.readCompositeData(var0);
      return var1;
   }

   protected void readCompositeData(CompositeData var1) throws OpenDataException {
      super.readCompositeData(var1);
      String var2 = (String)var1.get("ConversationName");
      if (var2 != null) {
         this.setConversationName(var2);
      }

      Integer var3 = (Integer)var1.get("DeliveryMode");
      if (var3 != null) {
         this.setDeliveryMode(var3);
      }

      Boolean var4 = (Boolean)var1.get("EndOfConversation");
      if (var4 != null) {
         this.setEndOfConversation(var4);
      }

      String var5 = (String)var1.get("MessageId");
      if (var5 != null) {
         this.setMessageId(var5);
      }

      Long var6 = (Long)var1.get("PayloadSize");
      if (var6 != null) {
         this.setPayloadSize(var6);
      }

      Long var7 = (Long)var1.get("SequenceNumber");
      if (var7 != null) {
         this.setSequenceNumber(var7);
      }

      Long var8 = (Long)var1.get("Timestamp");
      if (var8 != null) {
         this.setTimestamp(var8);
      }

      Long var9 = (Long)var1.get("TimeToLive");
      if (var9 != null) {
         this.setTimeToLive(var9);
      }

      String var10 = (String)var1.get("DestinationURL");
      if (var10 != null) {
         this.setDestinationURL(var10);
      }

      String var11 = (String)var1.get("SerializedRequest");
      if (var11 != null) {
         try {
            BASE64Decoder var12 = new BASE64Decoder();
            byte[] var13 = var12.decodeBuffer(var11);
            ByteArrayInputStream var14 = new ByteArrayInputStream(var13);
            ObjectInputStream var15 = new ObjectInputStream(var14);
            this.setSAFRequest((SAFRequest)var15.readObject());
         } catch (IOException var16) {
            throw new OpenDataException(var16.toString());
         } catch (ClassNotFoundException var17) {
            throw new OpenDataException(var17.toString());
         }
      }

   }

   protected Map getCompositeDataMap() throws OpenDataException {
      Map var1 = super.getCompositeDataMap();
      var1.put("VersionNumber", new Integer(1));
      var1.put("ConversationName", this.conversationName);
      var1.put("DeliveryMode", new Integer(this.deliveryMode));
      var1.put("EndOfConversation", new Boolean(this.endOfConversation));
      var1.put("MessageId", this.messageId);
      var1.put("PayloadSize", new Long(this.payloadSize));
      var1.put("Timestamp", new Long(this.timestamp));
      var1.put("TimeToLive", new Long(this.timeToLive));
      var1.put("DestinationURL", this.destinationURL);

      try {
         ByteArrayOutputStream var2 = new ByteArrayOutputStream();
         ObjectOutputStream var3 = new ObjectOutputStream(var2);
         var3.writeObject(this.safRequest);
         BASE64Encoder var4 = new BASE64Encoder();
         String var5 = var4.encodeBuffer(var2.toByteArray());
         var1.put("SerializedRequest", var5);
         return var1;
      } catch (IOException var10) {
         throw new OpenDataException(var10.toString());
      } finally {
         ;
      }
   }
}
