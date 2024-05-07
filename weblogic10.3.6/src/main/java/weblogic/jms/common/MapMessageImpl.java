package weblogic.jms.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import javax.jms.MapMessage;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.JMSClientExceptionLogger;

public final class MapMessageImpl extends MessageImpl implements MapMessage, Externalizable {
   private static final byte EXTVERSION = 1;
   private static final byte EXTVERSION2 = 2;
   private static final byte VERSIONMASK = 127;
   static final long serialVersionUID = -3363325517439700010L;
   private HashMap hashMap;
   private PayloadStream payload;
   private PeerInfo peerInfo;

   public MapMessageImpl() {
      this.peerInfo = PeerInfo.getPeerInfo();
   }

   public MapMessageImpl(MapMessage var1) throws javax.jms.JMSException {
      this(var1, (javax.jms.Destination)null, (javax.jms.Destination)null);
   }

   public MapMessageImpl(MapMessage var1, javax.jms.Destination var2, javax.jms.Destination var3) throws javax.jms.JMSException {
      super(var1, var2, var3);
      Enumeration var4 = var1.getMapNames();
      if (var4.hasMoreElements()) {
         this.hashMap = new HashMap();

         do {
            String var5 = (String)var4.nextElement();
            this.hashMap.put(var5, var1.getObject(var5));
         } while(var4.hasMoreElements());
      }

      this.peerInfo = PeerInfo.getPeerInfo();
   }

   public byte getType() {
      return 3;
   }

   public boolean getBoolean(String var1) throws javax.jms.JMSException {
      return TypeConverter.toBoolean(this.getObject(var1));
   }

   public byte getByte(String var1) throws javax.jms.JMSException {
      return TypeConverter.toByte(this.getObject(var1));
   }

   public byte[] getBytes(String var1) throws javax.jms.JMSException {
      return TypeConverter.toByteArray(this.getObject(var1));
   }

   public char getChar(String var1) throws javax.jms.JMSException {
      return TypeConverter.toChar(this.getObject(var1));
   }

   public double getDouble(String var1) throws javax.jms.JMSException {
      return TypeConverter.toDouble(this.getObject(var1));
   }

   public float getFloat(String var1) throws javax.jms.JMSException {
      return TypeConverter.toFloat(this.getObject(var1));
   }

   public short getShort(String var1) throws javax.jms.JMSException {
      return TypeConverter.toShort(this.getObject(var1));
   }

   public int getInt(String var1) throws javax.jms.JMSException {
      return TypeConverter.toInt(this.getObject(var1));
   }

   public long getLong(String var1) throws javax.jms.JMSException {
      return TypeConverter.toLong(this.getObject(var1));
   }

   public String getString(String var1) throws javax.jms.JMSException {
      return TypeConverter.toString(this.getObject(var1));
   }

   public Object getObject(String var1) throws javax.jms.JMSException {
      return this.getHashMap().get(var1);
   }

   public Enumeration getMapNames() throws javax.jms.JMSException {
      return Collections.enumeration(this.getHashMap().keySet());
   }

   public void setBoolean(String var1, boolean var2) throws javax.jms.JMSException {
      this.setObjectInternal(var1, new Boolean(var2));
   }

   public void setByte(String var1, byte var2) throws javax.jms.JMSException {
      this.setObjectInternal(var1, new Byte(var2));
   }

   public void setBytes(String var1, byte[] var2) throws javax.jms.JMSException {
      this.setObjectInternal(var1, var2);
   }

   public void setBytes(String var1, byte[] var2, int var3, int var4) throws javax.jms.JMSException {
      byte[] var5 = new byte[var4];
      System.arraycopy(var2, var3, var5, 0, var4);
      this.setObjectInternal(var1, var5);
   }

   public void setChar(String var1, char var2) throws javax.jms.JMSException {
      this.setObjectInternal(var1, new Character(var2));
   }

   public void setDouble(String var1, double var2) throws javax.jms.JMSException {
      this.setObjectInternal(var1, new Double(var2));
   }

   public void setFloat(String var1, float var2) throws javax.jms.JMSException {
      this.setObjectInternal(var1, new Float(var2));
   }

   public void setInt(String var1, int var2) throws javax.jms.JMSException {
      this.setObjectInternal(var1, new Integer(var2));
   }

   public void setLong(String var1, long var2) throws javax.jms.JMSException {
      this.setObjectInternal(var1, new Long(var2));
   }

   public void setObject(String var1, Object var2) throws javax.jms.JMSException {
      if (!(var2 instanceof Number) && !(var2 instanceof String) && !(var2 instanceof Boolean) && !(var2 instanceof byte[]) && !(var2 instanceof Character) && var2 != null) {
         throw new MessageFormatException(JMSClientExceptionLogger.logInvalidDataTypeLoggable(var2.getClass().getName()).getMessage());
      } else {
         this.setObjectInternal(var1, var2);
      }
   }

   public void setShort(String var1, short var2) throws javax.jms.JMSException {
      this.setObjectInternal(var1, new Short(var2));
   }

   public void setString(String var1, String var2) throws javax.jms.JMSException {
      this.setObjectInternal(var1, var2);
   }

   private void setObjectInternal(String var1, Object var2) throws javax.jms.JMSException {
      this.writeMode();
      if (var1 != null && var1.length() != 0) {
         this.getHashMap().put(var1, var2);
      } else {
         throw new IllegalArgumentException(JMSClientExceptionLogger.logIllegalNameLoggable(var1).getMessage());
      }
   }

   public boolean itemExists(String var1) throws javax.jms.JMSException {
      return this.getHashMap().containsKey(var1);
   }

   public void nullBody() {
      this.payload = null;
      this.hashMap = null;
   }

   public String toString() {
      return "MapMessage[" + this.getJMSMessageID() + "]";
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      int var3 = Integer.MAX_VALUE;
      ObjectOutput var2;
      if (var1 instanceof MessageImpl.JMSObjectOutputWrapper) {
         var3 = ((MessageImpl.JMSObjectOutputWrapper)var1).getCompressionThreshold();
         var2 = ((MessageImpl.JMSObjectOutputWrapper)var1).getInnerObjectOutput();
      } else {
         var2 = var1;
      }

      PeerInfo var6;
      if (var2 instanceof PeerInfoable) {
         var6 = ((PeerInfoable)var2).getPeerInfo();
      } else {
         var6 = PeerInfo.getPeerInfo();
      }

      PayloadStream var4;
      if (!this.isCompressed()) {
         synchronized(this) {
            var4 = this.getMapPayload(var6);
            if (this.payload == null) {
               this.payload = var4;
               this.peerInfo = var6;
               this.hashMap = null;
            }
         }
      } else {
         var4 = null;
      }

      byte var5;
      if (this.getVersion(var2) >= 30) {
         var5 = (byte)(2 | (this.shouldCompress(var2, var3) ? -128 : 0));
      } else {
         var5 = 1;
      }

      var2.writeByte(var5);
      if (this.isCompressed()) {
         if (var5 == 1) {
            this.decompress().writeLengthAndData(var2);
         } else {
            this.flushCompressedMessageBody(var2);
         }
      } else if ((var5 & -128) != 0) {
         this.writeExternalCompressPayload(var2, var4);
      } else {
         var4.writeLengthAndData(var2);
      }

   }

   public final void decompressMessageBody() throws javax.jms.JMSException {
      if (this.isCompressed()) {
         try {
            this.payload = (PayloadStream)this.decompress();
         } catch (IOException var6) {
            throw new JMSException(JMSClientExceptionLogger.logErrorDecompressMessageBodyLoggable().getMessage(), var6);
         } finally {
            this.cleanupCompressedMessageBody();
         }

      }
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      byte var2 = var1.readByte();
      byte var3 = (byte)(var2 & 127);
      if (var3 >= 1 && var3 <= 2) {
         this.peerInfo = PeerInfo.getPeerInfo();
         if (var1 instanceof PeerInfoable) {
            PeerInfo var4 = ((PeerInfoable)var1).getPeerInfo();
            if (var4 != null && var4.compareTo(this.peerInfo) < 0) {
               this.peerInfo = var4;
            }
         }

         if ((var2 & -128) != 0) {
            this.readExternalCompressedMessageBody(var1);
         } else {
            this.payload = (PayloadStream)PayloadFactoryImpl.createPayload((InputStream)var1);
         }

      } else {
         throw JMSUtilities.versionIOException(var3, 1, 2);
      }
   }

   public MessageImpl copy() throws javax.jms.JMSException {
      MapMessageImpl var1 = new MapMessageImpl();
      this.copy(var1);

      try {
         var1.peerInfo = PeerInfo.getPeerInfo();
         PayloadStream var2 = this.getMapPayload(var1.peerInfo);
         var1.payload = var2.copyPayloadWithoutSharedStream();
      } catch (IOException var3) {
         throw new JMSException(JMSClientExceptionLogger.logCopyErrorLoggable().getMessage(), var3);
      }

      var1.setBodyWritable(false);
      var1.setPropertiesWritable(false);
      return var1;
   }

   public long getPayloadSize() {
      if (this.isCompressed()) {
         return (long)this.getCompressedMessageBodySize();
      } else {
         return super.bodySize != -1L ? super.bodySize : (super.bodySize = this.payload == null ? 0L : (long)this.payload.getLength());
      }
   }

   private HashMap getHashMap() throws javax.jms.JMSException {
      if (this.hashMap != null) {
         return this.hashMap;
      } else {
         this.decompressMessageBody();
         if (this.payload == null) {
            this.hashMap = new HashMap();
         } else {
            try {
               ObjectInputStream var1 = new ObjectInputStream(this.payload.getInputStream());
               this.hashMap = JMSUtilities.readBigStringBasicMap(var1);
            } catch (IOException var2) {
               throw new JMSException(JMSClientExceptionLogger.logDeserializationErrorLoggable().getMessage(), var2);
            }
         }

         this.payload = null;
         return this.hashMap;
      }
   }

   private PayloadStream getMapPayload(PeerInfo var1) throws IOException {
      HashMap var2 = this.hashMap;
      if (this.payload != null) {
         if (this.peerInfo != null && this.peerInfo.equals(var1)) {
            return this.payload;
         }

         ObjectInputStream var3 = new ObjectInputStream(this.payload.getInputStream());
         var2 = JMSUtilities.readBigStringBasicMap(var3);
      } else if (var2 == null) {
         var2 = new HashMap();
      }

      BufferOutputStream var5 = PayloadFactoryImpl.createOutputStream();
      ObjectOutputStream var4 = new ObjectOutputStream(var5);
      JMSUtilities.writeBigStringBasicMap(var4, var2, var1, true);
      var4.flush();
      return (PayloadStream)var5.moveToPayload();
   }
}
