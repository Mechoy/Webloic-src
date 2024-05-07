package weblogic.jms.common;

import java.io.ByteArrayInputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.jms.Message;
import org.w3c.dom.Document;
import weblogic.common.WLObjectOutput;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.JMSClientExceptionLogger;
import weblogic.jms.client.JMSSession;
import weblogic.jms.extensions.WLAcknowledgeInfo;
import weblogic.jms.extensions.WLMessage;
import weblogic.messaging.MessageID;
import weblogic.messaging.common.MessagingUtilities;
import weblogic.utils.expressions.ExpressionMap;

public abstract class MessageImpl implements Message, weblogic.messaging.Message, WLMessage, ExpressionMap, Cloneable, Externalizable {
   static final long serialVersionUID = 7571220996297716034L;
   static final byte EXTVERSION1 = 1;
   static final byte EXTVERSION2 = 10;
   static final byte EXTVERSION3 = 20;
   static final byte EXTVERSION4 = 30;
   static final byte EXTVERSION5 = 40;
   public static final String USER_ID_PROPERTY_NAME = "JMSXUserID";
   public static final String UNIT_OF_ORDER_PROPERTY_NAME = "JMS_BEA_UnitOfOrder";
   public static final String SAF_SEQUENCE_NAME = "JMS_BEA_SAF_SEQUENCE_NAME";
   public static final String SAF_SEQUENCE_NUMBER = "JMS_BEA_SAF_SEQUENCE";
   public static final String DELIVERY_TIME_PROPERTY_NAME = "JMS_BEA_DeliveryTime";
   public static final String REDELIVERY_LIMIT_PROPERTY_NAME = "JMS_BEA_RedeliveryLimit";
   public static final String DELIVERY_COUNT_PROPERTY_NAME = "JMSXDeliveryCount";
   public static final String SIZE_PROPERTY_NAME = "JMS_BEA_Size";
   public static final String STATE_PROPERTY_NAME = "JMS_BEA_State";
   public static final String XID_PROPERTY_NAME = "JMS_BEA_Xid";
   public static final String INTERNAL_SEQUENCE_NUMBER_PROPERTY_NAME = "JMS_BEA_SequenceNumber";
   public static final String DD_FORWARDED_PROPERTY_NAME = "JMS_WL_DDForwarded";
   private static final boolean mydebug = false;
   private int deliveryMode;
   private transient int adjustedDeliveryMode;
   private int deliveryCount;
   private long expiration;
   private long deliveryTime;
   private int redeliveryLimit;
   private byte priority;
   private int userdatalen;
   transient long bodySize;
   private boolean clientResponsibleForAcknowledge;
   private transient long sequenceNumber;
   private transient boolean bodyWritable;
   private transient boolean propertiesWritable;
   private boolean serializeDestination;
   private boolean ddforwarded;
   private String correlationId;
   private DestinationImpl destination;
   private DestinationImpl replyTo;
   private String type;
   private PrimitiveObjectMap properties;
   static final byte NULLMESSAGEIMPL = 0;
   static final byte BYTESMESSAGEIMPL = 1;
   static final byte HDRMESSAGEIMPL = 2;
   static final byte MAPMESSAGEIMPL = 3;
   static final byte OBJECTMESSAGEIMPL = 4;
   static final byte STREAMMESSAGEIMPL = 5;
   static final byte TEXTMESSAGEIMPL = 6;
   static final byte XMLMESSAGEIMPL = 7;
   static final byte COMPRESSION = -128;
   protected static final int SUBFLAG_TOKENIZE = 1;
   protected static final int SUBFLAG_UTF8 = 2;
   protected static final int SUBFLAG_OBJECT = 4;
   protected static final int SUBFLAG_STRING = 128;
   private boolean hasBeenCompressed;
   private Externalizable bexaXid;
   private JMSMessageId messageId;
   private JMSID connectionId;
   private String messageIdString;
   private String userId;
   private String clientId;
   private transient JMSSession session;
   private String unitOfOrderName;
   private String safSequenceName;
   private long safSequenceNumber;
   private transient boolean keepSAFSequenceNameAndNumber;
   private Object workContext;
   private transient MessageReference mRef;
   private boolean userIDRequested;
   private transient boolean deliveryCountIncluded;
   private boolean pre90Message;
   private PayloadStream payloadCompressed;
   private boolean compressed;
   protected int originalLength;
   private boolean clean;
   protected transient boolean payloadCopyOnWrite;
   private boolean safNeedReorder;
   private boolean jmsClientForward;
   private int totalForwardsCount;
   private static final int _PRIORITYMASK = 15;
   private static final int _PRIORITYSHIFT = 0;
   private static final int _RESERVEDEXTENSION2 = 16;
   private static final int _DDFORWARDED = 128;
   private static final int _HASREDELIVERYLIMIT = 256;
   private static final int _ISPERSISTENT = 512;
   private static final int _HASCORRID = 1024;
   private static final int _HASDESTINATION = 2048;
   private static final int _HASREPLYTO = 4096;
   private static final int _ISREDELIVERED = 8192;
   private static final int _HASTYPE = 16384;
   private static final int _HASEXPIRATION = 32768;
   private static final int _HASPROPERTIES = 65536;
   private static final int _HASXID = 131072;
   private static final int _HASMESSAGEID = 262144;
   private static final int _HASUSERDATALEN = 524288;
   private static final int _HASDELIVERYTIME = 1048576;
   private static final int _SERIALIZEDEST = 2097152;
   private static final int _CLIENTRESPONSIBLEFORACKNOWLEDGE = 4194304;
   private static final int _RESERVEDEXTENSION1 = 8388608;
   private static final int _VERSIONMASK = -16777216;
   private static final int _VERSIONSHIFT = 24;
   private static final int _DESTINATIONMASK = 7;
   private static final int _REPLYTODESTINATIONMASK = 56;
   private static final int _REPLYTODESTINATIONSHIFT = 3;
   private static final int _JMSCLIENTFORWARD = 1;
   private static final int _WORKCONTEXT = 2;
   private static final int _OLDMESSAGE = 4;
   private static final int _REQUESTUSERID = 16;
   private static final int _UNITOFORDER = 32;
   private static final int _HASSAFSEQUENCE = 64;
   private static final int _HASSAFSEQUENCENUMBER = 128;
   private static final int _HASUSERID = 256;
   private static final int _SAFNEEDREORDER = 512;
   private static final int _HASCLIENTID = 1024;
   protected static final int _CONTROL_MESSAGE_MASK = 16711680;
   public static final int _CONTROL_SEQUENCE_RELEASE_FANOUT = 65536;
   public static final int _CONTROL_SEQUENCE_RELEASE = 131072;
   public static final int _CONTROL_SEQUENCE_RESERVE = 196608;
   private static final int GZIP_COMPRESSION = 0;
   static boolean debugWire = true;

   public final boolean hasBeenCompressed() {
      return this.hasBeenCompressed;
   }

   public MessageImpl() {
      this.deliveryMode = 1;
      this.adjustedDeliveryMode = 1;
      this.deliveryCount = 0;
      this.redeliveryLimit = -1;
      this.userdatalen = -1;
      this.bodySize = -1L;
      this.clientResponsibleForAcknowledge = false;
      this.serializeDestination = true;
      this.userIDRequested = false;
      this.pre90Message = false;
      this.jmsClientForward = false;
      this.bodyWritable = true;
      this.propertiesWritable = true;
   }

   public final void setOldMessage(boolean var1) {
      this.pre90Message = var1;
   }

   public final boolean isOldMessage() {
      return this.pre90Message;
   }

   public final void incForwardsCount() {
      ++this.totalForwardsCount;
   }

   public final void resetForwardsCount() {
      this.totalForwardsCount = 0;
   }

   public final void setForward(boolean var1) {
      this.jmsClientForward = var1;
   }

   public final void setSAFNeedReorder(boolean var1) {
      this.safNeedReorder = var1;
   }

   public final boolean isSAFNeedReorder() {
      return this.safNeedReorder;
   }

   public final boolean isForwarded() {
      return this.totalForwardsCount > 0;
   }

   public final boolean isForwardable() {
      return this.jmsClientForward;
   }

   public final int getForwardsCount() {
      return this.totalForwardsCount;
   }

   public abstract byte getType();

   public int getControlOpcode() {
      return 0;
   }

   public void setControlOpcode(int var1) throws IOException {
      throw new IOException("opcode not allocated");
   }

   public static MessageImpl createMessageImpl(byte var0) throws IOException {
      switch (var0) {
         case 1:
            return new BytesMessageImpl();
         case 2:
            return new HdrMessageImpl();
         case 3:
            return new MapMessageImpl();
         case 4:
            return new ObjectMessageImpl();
         case 5:
            return new StreamMessageImpl();
         case 6:
            return new TextMessageImpl();
         case 7:
            return new XMLMessageImpl();
         default:
            throw new IOException(JMSClientExceptionLogger.logUnknownMessageTypeLoggable(var0).getMessage());
      }
   }

   public MessageImpl(Message var1) throws javax.jms.JMSException {
      this(var1, (javax.jms.Destination)null, (javax.jms.Destination)null);
   }

   public MessageImpl(Message var1, javax.jms.Destination var2, javax.jms.Destination var3) throws javax.jms.JMSException {
      this();
      this.initializeFromMessage(var1, var2, var3);
   }

   public void initializeFromMessage(Message var1) throws javax.jms.JMSException {
      this.initializeFromMessage(var1, (javax.jms.Destination)null, (javax.jms.Destination)null);
   }

   public void initializeFromMessage(Message var1, javax.jms.Destination var2, javax.jms.Destination var3) throws javax.jms.JMSException {
      this.setJMSCorrelationID(var1.getJMSCorrelationID());
      this.setJMSType(var1.getJMSType());
      if (var3 == null) {
         this.setJMSReplyTo(var1.getJMSReplyTo());
      } else {
         this.setJMSReplyTo(var3);
      }

      this.userdatalen = 0;
      if (this.correlationId != null) {
         this.userdatalen = this.correlationId.length();
      }

      if (this.type != null) {
         this.userdatalen += this.type.length();
      }

      if (var2 == null) {
         this.setJMSDestination(var1.getJMSDestination());
      } else {
         this.setJMSDestination(var2);
      }

      if (var1.getJMSRedelivered()) {
         this.setDeliveryCount(2);
      }

      this.setJMSExpiration(var1.getJMSExpiration());
      if (var1 instanceof WLMessage) {
         this.setJMSPriority(var1.getJMSPriority());
         this.setJMSDeliveryMode(var1.getJMSDeliveryMode());
         this.setJMSDeliveryTime(((WLMessage)var1).getJMSDeliveryTime());
         this.setJMSRedeliveryLimit(((WLMessage)var1).getJMSRedeliveryLimit());
      }

      Enumeration var4 = var1.getPropertyNames();

      while(var4.hasMoreElements()) {
         String var5 = (String)var4.nextElement();
         Object var6 = var1.getObjectProperty(var5);
         this.userdatalen += 4 + (var5.length() << 2);
         this.userdatalen += MessagingUtilities.calcObjectSize(var6);
         this.setObjectProperty(var5, var6);
      }

   }

   private void copyToMessageReference() {
      if (this.mRef != null) {
         try {
            MessageImpl var1 = this.copy();
            this.mRef.setMessage(var1);
         } catch (javax.jms.JMSException var2) {
            JMSClientExceptionLogger.logStackTrace(var2);
         }

         this.mRef = null;
      }
   }

   public final String getJMSMessageID() {
      if (this.messageIdString == null && this.messageId != null) {
         if (this.pre90Message) {
            this.messageIdString = (this.deliveryMode == 1 ? "ID:N" : "ID:P") + this.messageId;
         } else {
            this.messageIdString = "ID:" + this.messageId;
         }
      }

      return this.messageIdString;
   }

   public final void setJMSMessageID(String var1) {
      if (this.mRef != null) {
         this.copyToMessageReference();
      }

      this.messageIdString = var1;
      this.jmsClientForward = false;
   }

   public final long getJMSTimestamp() {
      return this.messageId != null ? this.messageId.getTimestamp() : 0L;
   }

   public final void setJMSTimestamp(long var1) {
   }

   public final byte[] getJMSCorrelationIDAsBytes() throws javax.jms.JMSException {
      if (this.correlationId == null) {
         return null;
      } else {
         byte[] var1 = new byte[this.correlationId.length()];

         try {
            var1 = this.correlationId.getBytes("UTF-16BE");
         } catch (UnsupportedEncodingException var3) {
         }

         return var1;
      }
   }

   public final void setJMSCorrelationIDAsBytes(byte[] var1) throws javax.jms.JMSException {
      if (this.mRef != null) {
         this.copyToMessageReference();
      }

      try {
         if (var1 == null) {
            this.setJMSCorrelationID((String)null);
         } else {
            this.setJMSCorrelationID(new String(var1, "UTF-16BE"));
         }
      } catch (UnsupportedEncodingException var3) {
      }

   }

   public final void setJMSCorrelationID(String var1) {
      if (this.mRef != null) {
         this.copyToMessageReference();
      }

      this.jmsClientForward = false;
      this.correlationId = var1;
   }

   public final String getJMSCorrelationID() {
      return this.correlationId;
   }

   public final javax.jms.Destination getJMSReplyTo() {
      return this.replyTo;
   }

   public final void setJMSReplyTo(javax.jms.Destination var1) {
      if (this.mRef != null) {
         this.copyToMessageReference();
      }

      if (var1 != null && !(var1 instanceof DestinationImpl)) {
         this.replyTo = null;
      } else {
         this.replyTo = (DestinationImpl)var1;
      }

   }

   public final javax.jms.Destination getJMSDestination() {
      return this.destination;
   }

   public final DestinationImpl getDestination() {
      return this.destination;
   }

   public final void setJMSDestination(javax.jms.Destination var1) throws javax.jms.JMSException {
      if (this.mRef != null) {
         this.copyToMessageReference();
      }

      if (var1 != null && !(var1 instanceof DestinationImpl)) {
         this.destination = null;
      } else {
         this.destination = (DestinationImpl)var1;
      }

   }

   public final void setJMSDestinationImpl(DestinationImpl var1) {
      this.destination = var1;
   }

   public final int getJMSDeliveryMode() {
      return this.deliveryMode;
   }

   public final void setJMSDeliveryMode(int var1) throws javax.jms.JMSException {
      if (this.mRef != null) {
         this.copyToMessageReference();
      }

      if (var1 != 2 && var1 != 1) {
         throw new JMSException(JMSClientExceptionLogger.logInvalidDeliveryMode2Loggable(var1).getMessage());
      } else {
         this.deliveryMode = var1;
         this.adjustedDeliveryMode = this.deliveryMode;
      }
   }

   public final int getAdjustedDeliveryMode() {
      return this.adjustedDeliveryMode;
   }

   public final void setAdjustedDeliveryMode(int var1) {
      this.adjustedDeliveryMode = var1;
   }

   public final boolean getJMSRedelivered() {
      return this.deliveryCount > 1;
   }

   public void setDeliveryCount(int var1) {
      this.deliveryCount = var1;
   }

   public int getDeliveryCount() {
      return this.deliveryCount;
   }

   public final void setJMSRedelivered(boolean var1) throws javax.jms.JMSException {
      if (var1 != this.getJMSRedelivered()) {
         if (this.mRef != null) {
            this.copyToMessageReference();
         }

         if (var1) {
            this.setDeliveryCount(2);
         } else {
            this.setDeliveryCount(0);
         }

         assert var1 == this.getJMSRedelivered();

      }
   }

   public final void incrementDeliveryCount() {
      if (this.mRef != null) {
         this.copyToMessageReference();
      }

      this.setDeliveryCount(++this.deliveryCount);
   }

   public final void setClientResponsibleForAcknowledge(boolean var1) {
      this.clientResponsibleForAcknowledge = var1;
   }

   public final boolean getClientResponsibleForAcknowledge() {
      return this.clientResponsibleForAcknowledge;
   }

   public final String getJMSType() {
      return this.type;
   }

   public final void setJMSType(String var1) throws javax.jms.JMSException {
      this.jmsClientForward = false;
      if (this.mRef != null) {
         this.copyToMessageReference();
      }

      this.type = var1;
   }

   public final long getJMSExpiration() {
      return this.expiration;
   }

   public final void setJMSExpiration(long var1) throws javax.jms.JMSException {
      if (this.mRef != null) {
         this.copyToMessageReference();
      }

      this.expiration = var1;
   }

   public final void _setJMSExpiration(long var1) {
      if (this.mRef != null) {
         this.copyToMessageReference();
      }

      this.expiration = var1;
   }

   public final void setJMSDeliveryTime(long var1) throws javax.jms.JMSException {
      if (this.mRef != null) {
         this.copyToMessageReference();
      }

      this.deliveryTime = var1;
   }

   public final void setDeliveryTime(long var1) {
      if (this.mRef != null) {
         this.copyToMessageReference();
      }

      this.deliveryTime = var1;
   }

   public final long getJMSDeliveryTime() throws javax.jms.JMSException {
      return this.deliveryTime;
   }

   public final long getDeliveryTime() {
      return this.deliveryTime;
   }

   public final void setJMSRedeliveryLimit(int var1) throws javax.jms.JMSException {
      if (this.mRef != null) {
         this.copyToMessageReference();
      }

      if (var1 < -1) {
         throw new JMSException(JMSClientExceptionLogger.logInvalidRedeliveryLimit2Loggable().getMessage());
      } else {
         this.redeliveryLimit = var1;
      }
   }

   public final void _setJMSRedeliveryLimit(int var1) {
      this.redeliveryLimit = var1;
   }

   public final int getJMSRedeliveryLimit() throws javax.jms.JMSException {
      return this.redeliveryLimit;
   }

   public final int _getJMSRedeliveryLimit() {
      return this.redeliveryLimit;
   }

   public final int getJMSPriority() {
      return this.priority;
   }

   public final void setJMSPriority(int var1) throws javax.jms.JMSException {
      if (this.mRef != null) {
         this.copyToMessageReference();
      }

      if (var1 >= 0 && var1 <= 9) {
         this.priority = (byte)var1;
      } else {
         if (var1 != -1) {
            throw new JMSException(JMSClientExceptionLogger.logInvalidPriority2Loggable(var1).getMessage());
         }

         this.priority = 4;
      }

   }

   public final void setMessageReference(MessageReference var1) {
      this.mRef = var1;
   }

   public final void clearProperties() {
      if (this.mRef != null) {
         this.copyToMessageReference();
      }

      this.jmsClientForward = false;
      this.propertiesWritable = true;
      this.deliveryCountIncluded = false;
      this.properties = null;
   }

   private boolean isUnitOfOrderSet() {
      return this.unitOfOrderName != null;
   }

   private boolean isUserIDSet() {
      return this.userId != null;
   }

   private boolean isSAFSequenceNameSet() {
      return this.safSequenceName != null;
   }

   private boolean isSAFSequenceNumberSet() {
      return this.safSequenceNumber != 0L;
   }

   private boolean isDeliveryTimeSet() {
      return this.deliveryTime != 0L;
   }

   private boolean isRedeliveryLimitSet() {
      return this.redeliveryLimit != -1;
   }

   boolean hasProperties() {
      return this.properties != null && !this.properties.isEmpty();
   }

   public final boolean propertyExists(String var1) throws javax.jms.JMSException {
      if (var1.equals("JMS_BEA_UnitOfOrder")) {
         return this.isUnitOfOrderSet();
      } else if (var1.equals("JMSXUserID")) {
         return this.isUserIDSet();
      } else if (var1.equals("JMS_BEA_SAF_SEQUENCE_NAME")) {
         return this.isSAFSequenceNameSet();
      } else if (var1.equals("JMS_BEA_SAF_SEQUENCE")) {
         return this.isSAFSequenceNumberSet();
      } else if (var1.equals("JMS_BEA_DeliveryTime")) {
         return this.isDeliveryTimeSet();
      } else if (var1.equals("JMS_BEA_RedeliveryLimit")) {
         return this.isRedeliveryLimitSet();
      } else if (var1.equals("JMSXDeliveryCount")) {
         return true;
      } else {
         return this.properties != null && this.properties.containsKey(var1);
      }
   }

   public final boolean getBooleanProperty(String var1) throws javax.jms.JMSException {
      return TypeConverter.toBoolean(this.getObjectProperty(var1));
   }

   public final byte getByteProperty(String var1) throws javax.jms.JMSException {
      return TypeConverter.toByte(this.getObjectProperty(var1));
   }

   public final short getShortProperty(String var1) throws javax.jms.JMSException {
      return TypeConverter.toShort(this.getObjectProperty(var1));
   }

   public final int getIntProperty(String var1) throws javax.jms.JMSException {
      return TypeConverter.toInt(this.getObjectProperty(var1));
   }

   public final long getLongProperty(String var1) throws javax.jms.JMSException {
      return TypeConverter.toLong(this.getObjectProperty(var1));
   }

   public final float getFloatProperty(String var1) throws javax.jms.JMSException {
      return TypeConverter.toFloat(this.getObjectProperty(var1));
   }

   public final double getDoubleProperty(String var1) throws javax.jms.JMSException {
      return TypeConverter.toDouble(this.getObjectProperty(var1));
   }

   public final String getStringProperty(String var1) throws javax.jms.JMSException {
      Object var2 = this.getObjectProperty(var1);
      if (var2 != null && var2 instanceof byte[]) {
         try {
            byte[] var3 = (byte[])((byte[])var2);
            ByteArrayInputStream var4 = new ByteArrayInputStream(var3);
            ObjectInputStream var5 = new ObjectInputStream(var4);
            return (String)var5.readObject();
         } catch (Exception var6) {
            throw new JMSException(JMSClientExceptionLogger.logInvalidStringPropertyLoggable());
         }
      } else {
         return TypeConverter.toString(this.getObjectProperty(var1));
      }
   }

   public final Object getObjectProperty(String var1) throws javax.jms.JMSException {
      if (var1.equals("JMS_BEA_UnitOfOrder")) {
         return this.unitOfOrderName;
      } else if (var1.equals("JMS_BEA_SAF_SEQUENCE_NAME")) {
         return this.safSequenceName;
      } else if (var1.equals("JMS_BEA_SAF_SEQUENCE")) {
         return new Long(this.safSequenceNumber);
      } else if (var1.equals("JMS_BEA_DeliveryTime")) {
         return new Long(this.deliveryTime);
      } else if (var1.equals("JMS_BEA_RedeliveryLimit")) {
         return new Integer(this.redeliveryLimit);
      } else if (var1.equals("JMSXDeliveryCount")) {
         return new Integer(this.deliveryCount);
      } else if (var1.equals("JMSXUserID")) {
         return this.userId;
      } else {
         return this.properties != null ? this.properties.get(var1) : null;
      }
   }

   public final Collection getPropertyNameCollection() throws javax.jms.JMSException {
      ArrayList var1;
      if (this.properties == null) {
         var1 = new ArrayList();
      } else {
         var1 = new ArrayList(this.properties.keySet());
      }

      if (this.isUnitOfOrderSet()) {
         var1.add("JMS_BEA_UnitOfOrder");
      }

      if (this.isSAFSequenceNameSet()) {
         var1.add("JMS_BEA_SAF_SEQUENCE_NAME");
      }

      if (this.isSAFSequenceNumberSet()) {
         var1.add("JMS_BEA_SAF_SEQUENCE");
      }

      if (this.isDeliveryTimeSet()) {
         var1.add("JMS_BEA_DeliveryTime");
      }

      if (this.isRedeliveryLimitSet()) {
         var1.add("JMS_BEA_RedeliveryLimit");
      }

      if (this.isUserIDSet()) {
         var1.add("JMSXUserID");
      }

      if (this.deliveryCount > 0 && !this.propertiesWritable || this.deliveryCountIncluded) {
         var1.add("JMSXDeliveryCount");
      }

      return var1;
   }

   private PrimitiveObjectMap getInteropProperties() throws IOException {
      PrimitiveObjectMap var1 = new PrimitiveObjectMap();

      try {
         Iterator var2 = this.getPropertyNameCollection().iterator();

         while(var2.hasNext()) {
            String var5 = (String)var2.next();
            var1.put(var5, this.getObjectProperty(var5));
         }

         return var1;
      } catch (javax.jms.JMSException var4) {
         IOException var3 = new IOException(var4.toString());
         var3.initCause(var4);
         throw var3;
      }
   }

   public void removeProperty(String var1) {
      try {
         this.properties.remove(var1);
      } catch (javax.jms.JMSException var3) {
      }

   }

   private void readInteropProperties(ObjectInput var1, int var2) throws IOException {
      this.properties = new PrimitiveObjectMap(var1, var2);

      try {
         this.properties.remove("JMSXDeliveryCount");
         this.properties.remove("JMS_BEA_DeliveryTime");
         this.properties.remove("JMS_BEA_RedeliveryLimit");
         this.properties.remove("JMS_BEA_SAF_SEQUENCE_NAME");
         this.properties.remove("JMS_BEA_SAF_SEQUENCE");
         this.properties.remove("JMS_BEA_UnitOfOrder");
         this.properties.remove("JMSXUserID");
         if (this.properties.isEmpty()) {
            this.properties = null;
         }

      } catch (javax.jms.JMSException var5) {
         IOException var4 = new IOException(var5.toString());
         var4.initCause(var5);
         throw var4;
      }
   }

   public final Enumeration getPropertyNames() throws javax.jms.JMSException {
      return Collections.enumeration(this.getPropertyNameCollection());
   }

   public final void setBooleanProperty(String var1, boolean var2) throws javax.jms.JMSException {
      this.setObjectProperty(var1, var2);
   }

   public final void setByteProperty(String var1, byte var2) throws javax.jms.JMSException {
      this.setObjectProperty(var1, new Byte(var2));
   }

   public final void setShortProperty(String var1, short var2) throws javax.jms.JMSException {
      this.setObjectProperty(var1, new Short(var2));
   }

   public final void setIntProperty(String var1, int var2) throws javax.jms.JMSException {
      this.setObjectProperty(var1, new Integer(var2));
   }

   public final void setLongProperty(String var1, long var2) throws javax.jms.JMSException {
      this.setObjectProperty(var1, new Long(var2));
   }

   public final void setFloatProperty(String var1, float var2) throws javax.jms.JMSException {
      this.setObjectProperty(var1, new Float(var2));
   }

   public final void setDoubleProperty(String var1, double var2) throws javax.jms.JMSException {
      this.setObjectProperty(var1, new Double(var2));
   }

   public final void setStringProperty(String var1, String var2) throws javax.jms.JMSException {
      this.setObjectProperty(var1, var2);
   }

   private String illegalPropertyName(String var1) {
      return JMSClientExceptionLogger.logInvalidPropertyName2Loggable(var1).getMessage();
   }

   public final void setObjectProperty(String var1, Object var2) throws javax.jms.JMSException {
      if (var1 != null && var1.length() != 0) {
         if (isHeaderField(var1)) {
            throw new MessageFormatException(this.illegalPropertyName(var1));
         } else if (!Character.isJavaIdentifierStart(var1.charAt(0))) {
            throw new MessageFormatException(this.illegalPropertyName(var1));
         } else {
            int var3;
            for(var3 = 1; var3 < var1.length(); ++var3) {
               if (!Character.isJavaIdentifierPart(var1.charAt(var3))) {
                  throw new MessageFormatException(this.illegalPropertyName(var1));
               }
            }

            if (!this.propertiesWritable) {
               throw new MessageNotWriteableException(JMSClientExceptionLogger.logWriteInReadModeLoggable().getMessage());
            } else if (!(var2 instanceof Number) && !(var2 instanceof String) && !(var2 instanceof Boolean) && var2 != null) {
               throw new MessageFormatException(JMSClientExceptionLogger.logInvalidPropertyValueLoggable(var2.toString()).getMessage());
            } else if (var1.equals("JMS_BEA_UnitOfOrder")) {
               this.unitOfOrderName = TypeConverter.toString(var2);
            } else if (var1.equals("JMS_BEA_SAF_SEQUENCE_NAME")) {
               this.safSequenceName = TypeConverter.toString(var2);
               this.keepSAFSequenceNameAndNumber = true;
            } else if (var1.equals("JMS_BEA_SAF_SEQUENCE")) {
               this.safSequenceNumber = TypeConverter.toLong(var2);
               this.keepSAFSequenceNameAndNumber = true;
            } else if (var1.equals("JMS_BEA_DeliveryTime")) {
               this.deliveryTime = TypeConverter.toLong(var2);
            } else if (var1.equals("JMS_BEA_RedeliveryLimit")) {
               var3 = TypeConverter.toInt(var2);
               if (var3 < -1) {
                  throw new JMSException(JMSClientExceptionLogger.logInvalidRedeliveryLimit2Loggable());
               } else {
                  this.redeliveryLimit = var3;
               }
            } else if (var1.equals("JMSXDeliveryCount")) {
               this.deliveryCount = TypeConverter.toInt(var2);
            } else if (var1.equals("JMSXUserID")) {
               this.userId = TypeConverter.toString(var2);
            } else {
               if (this.properties == null) {
                  this.properties = new PrimitiveObjectMap();
               }

               this.properties.put(var1, var2);
            }
         }
      } else {
         throw new IllegalArgumentException(this.illegalPropertyName(var1));
      }
   }

   public final void acknowledge() throws javax.jms.JMSException {
      if (this.session != null) {
         this.session.acknowledge((WLAcknowledgeInfo)this);
         this.session = null;
      }

   }

   public final void clearBody() throws javax.jms.JMSException {
      if (this.mRef != null) {
         this.copyToMessageReference();
      }

      this.jmsClientForward = false;
      this.bodyWritable = true;
      this.bodySize = -1L;
      this.nullBody();
      this.cleanupCompressedMessageBody();
   }

   public abstract void nullBody();

   public final void setId(JMSMessageId var1) {
      this.messageId = var1;
      this.messageIdString = null;
   }

   public final JMSMessageId getId() {
      return this.messageId;
   }

   public final JMSMessageId getMessageId() {
      return this.getId();
   }

   public final void setSession(JMSSession var1) {
      this.session = var1;
   }

   public final void setConnectionId(JMSID var1) {
      this.connectionId = var1;
   }

   public final JMSID getConnectionId() {
      return this.connectionId;
   }

   public final void setClientId(String var1) {
      this.clientId = var1;
   }

   public final String getClientId() {
      return this.clientId;
   }

   public final long getSequenceNumber() {
      return this.sequenceNumber;
   }

   public final void setSequenceNumber(long var1) {
      this.sequenceNumber = var1;
   }

   public final Object get(String var1) {
      try {
         if (var1.startsWith("JMS")) {
            if (var1.equals("JMSCorrelationID")) {
               return this.getJMSCorrelationID();
            }

            if (var1.equals("JMSDeliveryMode")) {
               if (this.getJMSDeliveryMode() == 2) {
                  return "PERSISTENT";
               }

               return "NON_PERSISTENT";
            }

            if (var1.equals("JMSDeliveryTime")) {
               return new Long(this.getDeliveryTime());
            }

            if (var1.equals("JMSExpiration")) {
               return new Long(this.getJMSExpiration());
            }

            if (var1.equals("JMSMessageID")) {
               return this.getJMSMessageID();
            }

            if (var1.equals("JMSPriority")) {
               return new Integer(this.getJMSPriority());
            }

            if (var1.equals("JMSRedelivered")) {
               return this.getJMSRedelivered();
            }

            if (var1.equals("JMSRedeliveryLimit")) {
               return new Integer(this.getJMSRedeliveryLimit());
            }

            if (var1.equals("JMSTimestamp")) {
               return new Long(this.getJMSTimestamp());
            }

            if (var1.equals("JMSType")) {
               return this.getJMSType();
            }

            if (var1.equals("JMS_BEA_Size")) {
               return new Long(this.size());
            }
         }

         return this.getObjectProperty(var1);
      } catch (javax.jms.JMSException var3) {
         return null;
      }
   }

   public Object parse() throws Exception {
      return null;
   }

   public final synchronized MessageImpl cloneit() {
      try {
         return (MessageImpl)this.clone();
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }

   public abstract MessageImpl copy() throws javax.jms.JMSException;

   final void copy(MessageImpl var1) throws javax.jms.JMSException {
      boolean var2 = false;
      var1.destination = this.destination;
      var1.replyTo = this.replyTo;
      var1.deliveryMode = this.deliveryMode;
      var1.correlationId = this.correlationId;
      var1.deliveryCount = this.deliveryCount;
      var1.type = this.type;
      var1.deliveryTime = this.deliveryTime;
      var1.redeliveryLimit = this.redeliveryLimit;
      var1.expiration = this.expiration;
      var1.priority = this.priority;
      var1.jmsClientForward = this.jmsClientForward;
      var1.totalForwardsCount = this.totalForwardsCount;
      var1.safNeedReorder = this.safNeedReorder;
      if (var1.compressed = this.compressed) {
         var1.originalLength = this.originalLength;
         var1.payloadCompressed = this.payloadCompressed.copyPayloadWithoutSharedStream();
      }

      var1.clientResponsibleForAcknowledge = this.clientResponsibleForAcknowledge;
      if (this.userdatalen == -1) {
         var2 = true;
         var1.userdatalen = 0;
         if (var1.correlationId != null) {
            var1.userdatalen = var1.correlationId.length();
         }

         if (var1.type != null) {
            var1.userdatalen += var1.type.length();
         }
      } else {
         var1.userdatalen = this.userdatalen;
      }

      var1.connectionId = this.connectionId;
      var1.clientId = this.clientId;
      var1.messageId = this.messageId;
      if (this.properties != null) {
         var1.properties = new PrimitiveObjectMap(this.properties);
         if (var2) {
            var1.userdatalen += var1.properties.getSizeInBytes();
         }
      }

      var1.bodyWritable = this.bodyWritable;
      var1.propertiesWritable = this.propertiesWritable;
      var1.serializeDestination = this.serializeDestination;
      var1.ddforwarded = this.ddforwarded;
      var1.unitOfOrderName = this.unitOfOrderName;
      var1.safSequenceName = this.safSequenceName;
      var1.safSequenceNumber = this.safSequenceNumber;
      var1.userId = this.userId;
      var1.workContext = this.workContext;
      var1.setOldMessage(this.isOldMessage());
   }

   protected int getVersion(Object var1) throws IOException {
      if (var1 instanceof PeerInfoable) {
         PeerInfo var2 = ((PeerInfoable)var1).getPeerInfo();
         int var3 = var2.getMajor();
         if (debugWire && JMSDebug.JMSDispatcher.isDebugEnabled()) {
            JMSDebug.JMSDispatcher.debug("MessageImpl PeerInfo " + var2);
         }

         if (var3 < 6) {
            throw new IOException(JMSClientExceptionLogger.logIncompatibleVersion9Loggable((byte)1, (byte)10, (byte)20, (byte)30, var2.toString()).getMessage());
         }

         switch (var3) {
            case 6:
               return 10;
            case 7:
            case 8:
               return 20;
            default:
               if (var2.getMajor() == 9 || var2.getMajor() > 9 && var2.compareTo(PeerInfo.VERSION_1033) < 0) {
                  return 30;
               }

               if (var2.compareTo(PeerInfo.VERSION_1033) >= 0) {
                  return 40;
               }
         }
      }

      if (debugWire && JMSDebug.JMSDispatcher.isDebugEnabled()) {
         JMSDebug.JMSDispatcher.debug("MessageImpl write NOT! PeerInfoable");
      }

      return 40;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      ObjectOutput var2;
      if (var1 instanceof JMSObjectOutputWrapper) {
         var2 = ((JMSObjectOutputWrapper)var1).getInnerObjectOutput();
      } else {
         var2 = var1;
      }

      int var3 = 0;
      int var4 = this.getVersion(var2);
      var3 |= var4 << 24;
      if (this.deliveryMode == 2) {
         var3 |= 512;
      }

      String var5 = this.correlationId;
      if (var5 != null) {
         var3 |= 1024;
      }

      if (this.serializeDestination) {
         var3 |= 2097152;
      }

      if (this.ddforwarded) {
         var3 |= 128;
      }

      DestinationImpl var6 = this.destination;
      if (var6 != null) {
         if (this.serializeDestination) {
            var3 |= 2048;
         } else if (!(var2 instanceof WLObjectOutput) && (!(var2 instanceof JMSOutputStream) || !((JMSOutputStream)var2).isJMSMulticastOutputStream() && !((JMSOutputStream)var2).isBypassOutputStream())) {
            var3 |= 2048;
         }
      }

      int var7 = 0;
      if (var4 >= 20 && (var3 & 2048) != 0) {
         var7 |= Destination.getDestinationType(var6, 0);
         if (var7 != 0) {
            var3 |= 8388608;
         }
      }

      int var8;
      if (var4 >= 30) {
         var8 = this.getControlOpcode();
         if (this.jmsClientForward) {
            var8 |= 1;
         }

         if (this.workContext != null) {
            var8 |= 2;
         }

         if (this.userIDRequested) {
            var8 |= 16;
         }

         if (this.isUnitOfOrderSet()) {
            var8 |= 32;
         }

         if (this.isSAFSequenceNameSet()) {
            var8 |= 64;
         }

         if (this.isSAFSequenceNumberSet()) {
            var8 |= 128;
         }

         if (this.isUserIDSet()) {
            var8 |= 256;
         }

         if (this.safNeedReorder) {
            var8 |= 512;
         }

         if (this.pre90Message) {
            var8 |= 4;
         }

         if (var4 >= 40 && this.clientId != null) {
            var8 |= 1024;
         }

         if (var8 != 0) {
            var3 |= 16;
         }
      } else {
         var8 = 0;
      }

      DestinationImpl var9 = this.replyTo;
      if (var9 != null) {
         var3 |= 4096;
      }

      if (var4 >= 20 && var9 != null) {
         var7 |= Destination.getDestinationType(var9, 3);
         if ((var7 & 56) != 0) {
            var3 |= 8388608;
         }
      }

      if (this.getJMSRedelivered()) {
         var3 |= 8192;
      }

      if (this.type != null) {
         var3 |= 16384;
      }

      if (this.deliveryTime != 0L) {
         var3 |= 1048576;
      }

      if (var4 >= 20 && this.redeliveryLimit != -1) {
         var3 |= 256;
      }

      if (this.expiration != 0L) {
         var3 |= 32768;
      }

      var3 |= this.priority << 0;
      boolean var10 = var4 < 30 || this.hasProperties();
      if (var10) {
         var3 |= 65536;
      }

      if (this.messageId != null) {
         var3 |= 262144;
      }

      if (this.bexaXid != null) {
         var3 |= 131072;
      }

      if ((var3 & 82944) != 0 || (var8 & 32) != 0) {
         var3 |= 524288;
      }

      if (this.clientResponsibleForAcknowledge) {
         var3 |= 4194304;
      }

      this.writeFlags(var3, var2);
      int var11;
      if (var5 != null) {
         var2.writeUTF(var5);
         var11 = var5.length();
      } else {
         var11 = 0;
      }

      if (var4 >= 20 && (var3 & 8388608) != 0) {
         var2.writeByte(var7);
      }

      if ((var3 & 2048) != 0) {
         var6.writeExternal(var2);
      }

      if (var9 != null) {
         var9.writeExternal(var2);
      }

      if (var4 >= 30 && var8 != 0) {
         var2.writeInt(var8);
         if (this.jmsClientForward) {
            var2.writeInt(this.totalForwardsCount);
         }
      }

      if (this.type != null) {
         var2.writeUTF(this.type);
         var11 += this.type.length();
      }

      if (this.isDeliveryTimeSet()) {
         var2.writeLong(this.deliveryTime);
      }

      if (var4 >= 20 && this.isRedeliveryLimitSet()) {
         var2.writeInt(this.redeliveryLimit);
      }

      if (this.expiration != 0L) {
         var2.writeLong(this.expiration);
      }

      if (var4 >= 30) {
         var2.writeInt(this.deliveryCount);
      }

      if (var10) {
         if (var4 >= 30) {
            this.properties.writeToStream(var2, var4);
         } else {
            PeerInfo var12 = var2 instanceof PeerInfoable ? ((PeerInfoable)var2).getPeerInfo() : PeerInfo.getPeerInfo();
            this.getInteropProperties().writeToStream(var2, var12);
         }
      }

      if (this.messageId != null) {
         this.messageId.writeExternal(var2);
      }

      if (var4 >= 30) {
         if (this.isUnitOfOrderSet()) {
            var2.writeUTF(this.unitOfOrderName);
         }

         if (this.isUserIDSet()) {
            var2.writeUTF(this.userId);
            var11 += this.userId.length();
         }

         if (this.isSAFSequenceNameSet()) {
            var2.writeUTF(this.safSequenceName);
         }

         if (this.isSAFSequenceNumberSet()) {
            var2.writeLong(this.safSequenceNumber);
         }

         if (this.workContext != null) {
            JMSWorkContextHelper.writeWorkContext(this.workContext, var2);
         }
      }

      if (this.bexaXid != null) {
         var2.writeObject(this.bexaXid);
      }

      if ((var3 & 524288) != 0) {
         var2.writeInt(var11);
      }

      this.userdatalen = var11;
      if ((var8 & 1024) != 0) {
         var2.writeUTF(this.clientId);
      }

   }

   private void writeFlags(int var1, ObjectOutput var2) throws IOException {
      if (debugWire && JMSDebug.JMSDispatcher.isDebugEnabled()) {
         JMSDebug.JMSDispatcher.debug("MessageImpl.write versionInt 0x" + Integer.toHexString(var1).toUpperCase());
      }

      var2.writeInt(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      byte var3 = (byte)((var2 & -16777216) >>> 24 & 255);
      if (debugWire && JMSDebug.JMSDispatcher.isDebugEnabled()) {
         JMSDebug.JMSDispatcher.debug("MessageImpl.read  versionInt 0x" + Integer.toHexString(var2).toUpperCase());
      }

      if (var3 < 30) {
         this.pre90Message = true;
         if (var3 == 1) {
            if (!(var1 instanceof PutBackable)) {
               throw new IOException(JMSClientExceptionLogger.logUnknownStreamTypeLoggable().getMessage());
            }

            ((PutBackable)var1).unput();
            ((PutBackable)var1).unput();
            ((PutBackable)var1).unput();
            this.readExternalVersion1(var1);
            return;
         }

         if (var3 == 10) {
            this.readExternalVersion2(var1, var2);
            return;
         }
      }

      if (var3 > 40) {
         throw JMSUtilities.versionIOException(var3, 1, 40);
      } else {
         if ((var2 & 512) != 0) {
            this.deliveryMode = 2;
         } else {
            this.deliveryMode = 1;
         }

         this.adjustedDeliveryMode = this.deliveryMode;
         if ((var2 & 1024) != 0) {
            this.correlationId = var1.readUTF();
         }

         int var4;
         int var5;
         if ((var2 & 8388608) != 0) {
            var4 = var1.readByte();
            if ((var2 & 2048) != 0) {
               var5 = (byte)(var4 & 7);
               this.destination = Destination.createDestination((byte)var5, var1);
            }

            if ((var2 & 4096) != 0) {
               var5 = (byte)((var4 & 56) >>> 3);
               this.replyTo = Destination.createDestination((byte)var5, var1);
            }
         }

         var4 = 0;
         if ((var2 & 16) != 0) {
            var4 = var1.readInt();
            this.pre90Message = (var4 & 4) != 0;
            this.userIDRequested = (var4 & 16) != 0;
            this.jmsClientForward = (var4 & 1) != 0;
            this.safNeedReorder = (var4 & 512) != 0;
            var5 = 16711680 & var4;
            if (var5 != 0) {
               this.setControlOpcode(var5);
            }

            if ((var4 & 1) != 0) {
               this.totalForwardsCount = var1.readInt();
            }
         }

         if ((var2 & 16384) != 0) {
            this.type = var1.readUTF();
         }

         if ((var2 & 1048576) != 0) {
            this.deliveryTime = var1.readLong();
         }

         if ((var2 & 256) != 0) {
            this.redeliveryLimit = var1.readInt();
         }

         if ((var2 & '耀') != 0) {
            this.expiration = var1.readLong();
         }

         if (var3 >= 30) {
            this.deliveryCount = var1.readInt();
         }

         this.priority = (byte)((var2 & 15) >>> 0 & 255);
         if ((var2 & 65536) != 0) {
            if (var3 >= 30) {
               this.properties = new PrimitiveObjectMap(var1, var3);
            } else {
               this.readInteropProperties(var1, var3);
            }
         }

         if ((var2 & 262144) != 0) {
            this.messageId = new JMSMessageId();
            this.messageId.readExternal(var1);
         }

         if ((var4 & 32) != 0) {
            this.unitOfOrderName = var1.readUTF().intern();
         }

         if ((var4 & 256) != 0) {
            this.userId = var1.readUTF().intern();
         }

         if ((var4 & 64) != 0) {
            this.safSequenceName = var1.readUTF().intern();
         }

         if ((var4 & 128) != 0) {
            this.safSequenceNumber = var1.readLong();
         }

         if ((var4 & 2) != 0) {
            this.workContext = JMSWorkContextHelper.readWorkContext(var1);
         }

         if ((var2 & 131072) != 0) {
            this.bexaXid = (Externalizable)var1.readObject();
         }

         if ((var2 & 524288) != 0) {
            this.userdatalen = var1.readInt();
         } else {
            this.userdatalen = 0;
         }

         this.clientResponsibleForAcknowledge = (var2 & 4194304) != 0;
         this.bodyWritable = false;
         this.propertiesWritable = false;
         this.serializeDestination = (var2 & 2097152) != 0;
         this.ddforwarded = (var2 & 128) != 0;
         if ((var4 & 1024) != 0) {
            this.clientId = var1.readUTF();
         }

      }
   }

   private void readExternalVersion2(ObjectInput var1, int var2) throws IOException, ClassNotFoundException {
      if ((var2 & 512) != 0) {
         this.deliveryMode = 2;
      } else {
         this.deliveryMode = 1;
      }

      this.adjustedDeliveryMode = this.deliveryMode;
      if ((var2 & 1024) != 0) {
         this.correlationId = var1.readUTF();
      }

      if ((var2 & 2048) != 0) {
         this.destination = new DestinationImpl();
         this.destination.readExternal(var1);
      }

      if ((var2 & 4096) != 0) {
         this.replyTo = new DestinationImpl();
         this.replyTo.readExternal(var1);
      }

      boolean var3 = (var2 & 8192) != 0;
      if (var3) {
         this.setDeliveryCount(2);
      } else {
         this.setDeliveryCount(1);
      }

      if ((var2 & 16384) != 0) {
         this.type = var1.readUTF();
      }

      if ((var2 & 1048576) != 0) {
         this.deliveryTime = var1.readLong();
      }

      if ((var2 & '耀') != 0) {
         this.expiration = var1.readLong();
      }

      this.priority = (byte)((var2 & 15) >>> 0 & 255);
      if ((var2 & 65536) != 0) {
         this.readInteropProperties(var1, 10);
      }

      if ((var2 & 262144) != 0) {
         this.messageId = new JMSMessageId();
         this.messageId.readExternal(var1);
      }

      if ((var2 & 131072) != 0) {
         this.bexaXid = (Externalizable)var1.readObject();
      }

      if ((var2 & 524288) != 0) {
         this.userdatalen = var1.readInt();
      } else {
         this.userdatalen = 0;
      }

      this.bodyWritable = false;
      this.propertiesWritable = false;
      this.serializeDestination = (var2 & 2097152) != 0;
   }

   private void readExternalVersion1(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.deliveryMode = var1.readByte();
      this.adjustedDeliveryMode = this.deliveryMode;
      if (this.deliveryMode != 2) {
         throw new IOException(JMSClientExceptionLogger.logCorruptedStreamLoggable().getMessage());
      } else {
         if (var1.readBoolean()) {
            this.correlationId = var1.readUTF();
         } else {
            this.correlationId = null;
         }

         var1.readLong();
         if (var1.readBoolean()) {
            this.destination = new DestinationImpl();
            this.destination.readExternal(var1);
         }

         if (var1.readBoolean()) {
            this.replyTo = new DestinationImpl();
            this.replyTo.readExternal(var1);
         }

         boolean var2 = var1.readBoolean();
         if (var2) {
            this.setDeliveryCount(2);
         } else {
            this.setDeliveryCount(1);
         }

         if (var1.readBoolean()) {
            this.type = var1.readUTF();
         } else {
            this.type = null;
         }

         this.expiration = var1.readLong();
         this.priority = var1.readByte();
         if (var1.readBoolean()) {
            this.readInteropProperties(var1, 1);
         }

         if (var1.readBoolean()) {
            this.messageId = new JMSMessageId();
            this.messageId.readExternal(var1);
         }

         if (this.messageId == null && this.expiration != 0L) {
            throw new IOException(JMSClientExceptionLogger.logVersionErrorLoggable().getMessage());
         } else {
            if (var1.readBoolean()) {
               this.bexaXid = (Externalizable)var1.readObject();
            }

            this.userdatalen = var1.readInt();
            this.connectionId = new JMSID();
            this.clientResponsibleForAcknowledge = false;
            this.connectionId.readExternal(var1);
            this.bodyWritable = false;
            this.propertiesWritable = false;
         }
      }
   }

   public static boolean isHeaderField(String var0) {
      return var0.startsWith("JMS") && (var0.equals("JMSCorrelationID") || var0.equals("JMSDeliveryMode") || var0.equals("JMSDestination") || var0.equals("JMSExpiration") || var0.equals("JMSPriority") || var0.equals("JMSRedelivered") || var0.equals("JMSReplyTo") || var0.equals("JMSTimestamp") || var0.equals("JMSType"));
   }

   public void reset() throws javax.jms.JMSException {
   }

   public final void setBodyWritable() {
      this.setBodyWritable(true);
      this.jmsClientForward = false;
   }

   public final void setBodyWritable(boolean var1) {
      this.bodyWritable = var1;
   }

   public final void setPropertiesWritable(boolean var1) {
      this.propertiesWritable = var1;
   }

   final void readMode() throws javax.jms.JMSException {
      if (this.bodyWritable) {
         throw new MessageNotReadableException(JMSClientExceptionLogger.logReadInWriteModeLoggable().getMessage());
      }
   }

   public void setJMSXUserID(String var1) {
      this.userId = var1;
   }

   public boolean isJMSXUserIDRequested() {
      return this.userIDRequested;
   }

   public void requestJMSXUserID(boolean var1) {
      this.userIDRequested = var1;
   }

   public boolean includeJMSXDeliveryCount(boolean var1) {
      boolean var2 = this.deliveryCountIncluded;
      this.deliveryCountIncluded = var1;
      return var2;
   }

   final void writeMode() throws javax.jms.JMSException {
      if (!this.bodyWritable) {
         throw new MessageNotWriteableException(JMSClientExceptionLogger.logWriteInReadMode2Loggable().getMessage());
      }
   }

   public final Externalizable getBEXAXid() {
      return this.bexaXid;
   }

   public final void setBEXAXid(Externalizable var1) {
      this.bexaXid = var1;
   }

   public abstract long getPayloadSize();

   public final int getUserPropertySize() {
      return this.userdatalen;
   }

   public final void resetUserPropertySize() {
      this.bodySize = (long)(this.userdatalen = -1);
   }

   public final void setSerializeDestination(boolean var1) {
      this.serializeDestination = var1;
   }

   public final void setDDForwarded(boolean var1) {
      this.ddforwarded = var1;
   }

   public final boolean getDDForwarded() {
      return this.ddforwarded;
   }

   public final void setUnitOfOrderName(String var1) {
      this.unitOfOrderName = var1;
   }

   public final String getUnitOfOrder() {
      return this.unitOfOrderName;
   }

   public final boolean getKeepSAFSequenceNameAndNumber() {
      return this.keepSAFSequenceNameAndNumber;
   }

   public final void setSAFSequenceName(String var1) {
      this.safSequenceName = var1;
      this.keepSAFSequenceNameAndNumber = true;
   }

   public final String getSAFSequenceName() {
      return this.safSequenceName;
   }

   public void setSAFSeqNumber(long var1) {
      this.safSequenceNumber = var1;
      this.keepSAFSequenceNameAndNumber = true;
   }

   public long getSAFSeqNumber() {
      return this.safSequenceNumber;
   }

   public final String getGroup() {
      return this.unitOfOrderName;
   }

   public final void setWorkContext(Object var1) {
      this.workContext = var1;
   }

   public final Object getWorkContext() {
      return this.workContext;
   }

   public MessageID getMessageID() {
      return this.messageId;
   }

   public long getExpirationTime() {
      return this.getJMSExpiration();
   }

   public int getRedeliveryLimit() {
      return this._getJMSRedeliveryLimit();
   }

   public long size() {
      return this.getPayloadSize() + (long)this.getUserPropertySize();
   }

   public weblogic.messaging.Message duplicate() {
      return this.cloneit();
   }

   public Document getJMSMessageDocument() throws javax.jms.JMSException {
      try {
         Class var1 = Class.forName("weblogic.jms.common.XMLHelper");
         Method var5 = var1.getMethod("getDocument", WLMessage.class);
         return (Document)var5.invoke((Object)null, this);
      } catch (InvocationTargetException var3) {
         Throwable var2 = var3.getTargetException();
         if (var2 instanceof javax.jms.JMSException) {
            throw (javax.jms.JMSException)var2;
         } else {
            throw new AssertionError(var2);
         }
      } catch (Exception var4) {
         throw new AssertionError(var4);
      }
   }

   public final boolean isCompressed() {
      return this.compressed;
   }

   protected final boolean shouldCompress(ObjectOutput var1, int var2) throws IOException {
      if (this.compressed) {
         return true;
      } else if (this.getVersion(var1) < 30) {
         return false;
      } else {
         long var3 = this.getPayloadSize();
         return var3 > (long)var2;
      }
   }

   protected void cleanupCompressedMessageBody() {
      if (!this.clean) {
         this.originalLength = 0;
         this.payloadCompressed = null;
         this.compressed = false;
         this.clean = true;
      }
   }

   protected void readExternalCompressedMessageBody(ObjectInput var1) throws IOException {
      byte var2 = var1.readByte();
      if (var2 != 0) {
         throw new IOException(JMSClientExceptionLogger.logErrorCompressionTagLoggable(var2).getMessage());
      } else {
         this.compressed = true;
         this.originalLength = var1.readInt();
         this.payloadCompressed = (PayloadStream)PayloadFactoryImpl.createPayload((InputStream)var1);
      }
   }

   public int getCompressedMessageBodySize() {
      return this.payloadCompressed.getLength();
   }

   public int getOriginalMessageBodySize() {
      return this.originalLength;
   }

   protected void flushCompressedMessageBody(ObjectOutput var1) throws IOException {
      var1.writeByte(0);
      var1.writeInt(this.originalLength);
      this.payloadCompressed.writeLengthAndData(var1);
   }

   public abstract void decompressMessageBody() throws javax.jms.JMSException;

   protected Payload decompress() throws IOException {
      this.hasBeenCompressed = true;
      GZIPInputStream var1 = new GZIPInputStream(this.payloadCompressed.getInputStream(), this.payloadCompressed.getLength());
      return PayloadFactoryImpl.copyPayloadFromStream(var1, this.originalLength);
   }

   protected final void writeExternalCompressPayload(ObjectOutput var1, Payload var2) throws IOException {
      BufferOutputStream var4 = PayloadFactoryImpl.createOutputStream();
      GZIPOutputStream var3 = new GZIPOutputStream(var4);
      var2.writeTo(var3);
      var3.finish();
      var4.flush();
      var1.writeByte(0);
      var1.writeInt(var2.getLength());
      var4.writeLengthAndData(var1);
   }

   public static final JMSObjectOutputWrapper createJMSObjectOutputWrapper(ObjectOutput var0, int var1, boolean var2) {
      assert var0 instanceof PeerInfoable;

      return new JMSObjectOutputWrapper(var0, var1, var2);
   }

   public static int getPosition(ObjectOutput var0) {
      try {
         Class var1 = Class.forName("weblogic.protocol.AsyncOutgoingMessage");
         Method var2 = var1.getMethod("getLength");
         return (Integer)var2.invoke(var0);
      } catch (ClassNotFoundException var3) {
      } catch (NoSuchMethodException var4) {
      } catch (IllegalAccessException var5) {
      } catch (InvocationTargetException var6) {
      }

      return -1;
   }

   public static final class JMSObjectOutputWrapper implements ObjectOutput, PeerInfoable {
      private final ObjectOutput out;
      private int compressionThreshold;
      private boolean readStringAsObject;

      private JMSObjectOutputWrapper(ObjectOutput var1, int var2, boolean var3) {
         this.compressionThreshold = Integer.MAX_VALUE;
         this.readStringAsObject = false;
         this.out = var1;
         this.compressionThreshold = var2;
         this.readStringAsObject = var3;
      }

      private JMSObjectOutputWrapper(ObjectOutput var1, int var2) {
         this.compressionThreshold = Integer.MAX_VALUE;
         this.readStringAsObject = false;
         this.out = var1;
         this.compressionThreshold = var2;
      }

      public PeerInfo getPeerInfo() {
         return ((PeerInfoable)this.out).getPeerInfo();
      }

      final ObjectOutput getInnerObjectOutput() {
         return this.out;
      }

      final boolean getReadStringAsObject() {
         return this.readStringAsObject;
      }

      final int getCompressionThreshold() {
         return this.compressionThreshold;
      }

      public void close() throws IOException {
      }

      public void flush() throws IOException {
      }

      public void writeObject(Object var1) throws IOException {
      }

      public void writeDouble(double var1) throws IOException {
      }

      public void writeFloat(float var1) throws IOException {
      }

      public void writeByte(int var1) throws IOException {
      }

      public void writeChar(int var1) throws IOException {
      }

      public void writeInt(int var1) throws IOException {
      }

      public void writeShort(int var1) throws IOException {
      }

      public void writeLong(long var1) throws IOException {
      }

      public void writeBoolean(boolean var1) throws IOException {
      }

      public void write(int var1) throws IOException {
      }

      public void write(byte[] var1) throws IOException {
      }

      public void write(byte[] var1, int var2, int var3) throws IOException {
      }

      public void writeBytes(String var1) throws IOException {
      }

      public void writeChars(String var1) throws IOException {
      }

      public void writeUTF(String var1) throws IOException {
      }

      // $FF: synthetic method
      JMSObjectOutputWrapper(ObjectOutput var1, int var2, boolean var3, Object var4) {
         this(var1, var2, var3);
      }
   }
}
