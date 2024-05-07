package weblogic.jms.common;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.jms.BytesMessage;
import weblogic.jms.JMSClientExceptionLogger;

public final class BytesMessageImpl extends MessageImpl implements BytesMessage, Externalizable {
   private static final byte EXTVERSION1 = 1;
   private static final byte EXTVERSION2 = 2;
   private static final byte EXTVERSION3 = 3;
   private static final byte VERSIONMASK = 127;
   static final long serialVersionUID = -8264735281046103996L;
   transient PayloadStream payload;
   private transient BufferOutputStream bos;
   private transient BufferInputStream bis;

   public BytesMessageImpl() {
   }

   public BytesMessageImpl(BytesMessage var1) throws IOException, javax.jms.JMSException {
      this(var1, (javax.jms.Destination)null, (javax.jms.Destination)null);
   }

   public BytesMessageImpl(BytesMessage var1, javax.jms.Destination var2, javax.jms.Destination var3) throws IOException, javax.jms.JMSException {
      super(var1, var2, var3);
      short var4 = 4096;
      byte[] var5 = new byte[var4];
      var1.reset();

      for(int var6 = var1.readBytes(var5, var4); var6 > 0; var6 = var1.readBytes(var5, var4)) {
         this.writeBytes(var5, 0, var6);
      }

      this.reset();
      this.setPropertiesWritable(false);
   }

   public byte getType() {
      return 1;
   }

   public void nullBody() {
      this.payload = null;
      this.bis = null;
      this.bos = null;
      this.payloadCopyOnWrite = false;
   }

   private String getReadPastEnd(int var1) {
      return JMSClientExceptionLogger.logReadPastEnd2Loggable(var1).getMessage();
   }

   static String getReadError(int var0) {
      return JMSClientExceptionLogger.logReadErrorLoggable(var0).getMessage();
   }

   public boolean readBoolean() throws javax.jms.JMSException {
      try {
         this.checkReadable();
         return this.bis.readBoolean();
      } catch (EOFException var2) {
         throw new MessageEOFException(this.getReadPastEnd(10), var2);
      } catch (IOException var3) {
         throw new JMSException(getReadError(10), var3);
      }
   }

   public byte readByte() throws javax.jms.JMSException {
      try {
         this.checkReadable();
         return this.bis.readByte();
      } catch (EOFException var2) {
         throw new MessageEOFException(this.getReadPastEnd(20), var2);
      } catch (IOException var3) {
         throw new JMSException(getReadError(20), var3);
      }
   }

   public int readUnsignedByte() throws javax.jms.JMSException {
      try {
         this.checkReadable();
         return this.bis.readUnsignedByte();
      } catch (EOFException var2) {
         throw new MessageEOFException(this.getReadPastEnd(30), var2);
      } catch (IOException var3) {
         throw new JMSException(getReadError(30), var3);
      }
   }

   public short readShort() throws javax.jms.JMSException {
      try {
         this.checkReadable();
         return this.bis.readShort();
      } catch (EOFException var2) {
         throw new MessageEOFException(this.getReadPastEnd(40), var2);
      } catch (IOException var3) {
         throw new JMSException(getReadError(40), var3);
      }
   }

   public int readUnsignedShort() throws javax.jms.JMSException {
      try {
         this.checkReadable();
         return this.bis.readUnsignedShort();
      } catch (EOFException var2) {
         throw new MessageEOFException(this.getReadPastEnd(50), var2);
      } catch (IOException var3) {
         throw new JMSException(getReadError(50), var3);
      }
   }

   public char readChar() throws javax.jms.JMSException {
      try {
         this.checkReadable();
         return this.bis.readChar();
      } catch (EOFException var2) {
         throw new MessageEOFException(this.getReadPastEnd(60), var2);
      } catch (IOException var3) {
         throw new JMSException(getReadError(60), var3);
      }
   }

   public int readInt() throws javax.jms.JMSException {
      try {
         this.checkReadable();
         return this.bis.readInt();
      } catch (EOFException var2) {
         throw new MessageEOFException(this.getReadPastEnd(70), var2);
      } catch (IOException var3) {
         throw new JMSException(getReadError(70), var3);
      }
   }

   public long readLong() throws javax.jms.JMSException {
      try {
         this.checkReadable();
         return this.bis.readLong();
      } catch (EOFException var2) {
         throw new MessageEOFException(this.getReadPastEnd(80), var2);
      } catch (IOException var3) {
         throw new JMSException(getReadError(80), var3);
      }
   }

   public float readFloat() throws javax.jms.JMSException {
      try {
         this.checkReadable();
         return this.bis.readFloat();
      } catch (EOFException var2) {
         throw new MessageEOFException(this.getReadPastEnd(90), var2);
      } catch (IOException var3) {
         throw new JMSException(getReadError(90), var3);
      }
   }

   public double readDouble() throws javax.jms.JMSException {
      try {
         this.checkReadable();
         return this.bis.readDouble();
      } catch (EOFException var2) {
         throw new MessageEOFException(this.getReadPastEnd(100), var2);
      } catch (IOException var3) {
         throw new JMSException(getReadError(100), var3);
      }
   }

   public String readUTF() throws javax.jms.JMSException {
      try {
         this.checkReadable();
         return this.bis.readUTF();
      } catch (EOFException var2) {
         throw new MessageEOFException(this.getReadPastEnd(110), var2);
      } catch (IOException var3) {
         throw new JMSException(getReadError(110), var3);
      }
   }

   public int readBytes(byte[] var1) throws javax.jms.JMSException {
      return this.readBytes(var1, var1.length);
   }

   public int readBytes(byte[] var1, int var2) throws javax.jms.JMSException {
      if (var2 < 0) {
         throw new IndexOutOfBoundsException(JMSClientExceptionLogger.logNegativeLengthLoggable(var2).getMessage());
      } else if (var2 > var1.length) {
         throw new IndexOutOfBoundsException(JMSClientExceptionLogger.logTooMuchLengthLoggable(var2, var1.length).getMessage());
      } else {
         try {
            this.checkReadable();
         } catch (javax.jms.MessageEOFException var5) {
            return -1;
         }

         try {
            return this.bis.read(var1, 0, var2);
         } catch (IOException var4) {
            throw new JMSException(getReadError(5), var4);
         }
      }
   }

   private String getWriteError(int var1) {
      return JMSClientExceptionLogger.logWriteErrorLoggable(var1).getMessage();
   }

   public void writeBoolean(boolean var1) throws javax.jms.JMSException {
      try {
         this.checkWritable();
         this.bos.writeBoolean(var1);
      } catch (IOException var3) {
         throw new JMSException(this.getWriteError(10), var3);
      }
   }

   public void writeByte(byte var1) throws javax.jms.JMSException {
      try {
         this.checkWritable();
         this.bos.writeByte(var1);
      } catch (IOException var3) {
         throw new JMSException(this.getWriteError(20), var3);
      }
   }

   public void writeShort(short var1) throws javax.jms.JMSException {
      try {
         this.checkWritable();
         this.bos.writeShort(var1);
      } catch (IOException var3) {
         throw new JMSException(this.getWriteError(30), var3);
      }
   }

   public void writeChar(char var1) throws javax.jms.JMSException {
      try {
         this.checkWritable();
         this.bos.writeChar(var1);
      } catch (IOException var3) {
         throw new JMSException(this.getWriteError(40), var3);
      }
   }

   public void writeInt(int var1) throws javax.jms.JMSException {
      try {
         this.checkWritable();
         this.bos.writeInt(var1);
      } catch (IOException var3) {
         throw new JMSException(this.getWriteError(50), var3);
      }
   }

   public void writeLong(long var1) throws javax.jms.JMSException {
      try {
         this.checkWritable();
         this.bos.writeLong(var1);
      } catch (IOException var4) {
         throw new JMSException(this.getWriteError(60), var4);
      }
   }

   public void writeFloat(float var1) throws javax.jms.JMSException {
      try {
         this.checkWritable();
         this.bos.writeFloat(var1);
      } catch (IOException var3) {
         throw new JMSException(this.getWriteError(70), var3);
      }
   }

   public void writeDouble(double var1) throws javax.jms.JMSException {
      try {
         this.checkWritable();
         this.bos.writeDouble(var1);
      } catch (IOException var4) {
         throw new JMSException(this.getWriteError(80), var4);
      }
   }

   public void writeUTF(String var1) throws javax.jms.JMSException {
      try {
         this.checkWritable();
         this.bos.writeUTF(var1);
      } catch (IOException var3) {
         throw new JMSException(this.getWriteError(90), var3);
      }
   }

   public void writeBytes(byte[] var1) throws javax.jms.JMSException {
      try {
         this.checkWritable();
         this.bos.write(var1);
      } catch (IOException var3) {
         throw new JMSException(this.getWriteError(100), var3);
      }
   }

   public void writeBytes(byte[] var1, int var2, int var3) throws javax.jms.JMSException {
      this.checkWritable();

      try {
         this.bos.write(var1, var2, var3);
      } catch (IOException var5) {
         throw new JMSException(var5);
      }
   }

   public void writeObject(Object var1) throws javax.jms.JMSException {
      if (var1 instanceof Boolean) {
         this.writeBoolean((Boolean)var1);
      } else if (var1 instanceof Number) {
         if (var1 instanceof Byte) {
            this.writeByte(((Number)var1).byteValue());
         } else if (var1 instanceof Short) {
            this.writeShort(((Number)var1).shortValue());
         } else if (var1 instanceof Integer) {
            this.writeInt(((Number)var1).intValue());
         } else if (var1 instanceof Long) {
            this.writeLong(((Number)var1).longValue());
         } else if (var1 instanceof Float) {
            this.writeFloat(((Number)var1).floatValue());
         } else if (var1 instanceof Double) {
            this.writeDouble(((Number)var1).doubleValue());
         }
      } else if (var1 instanceof String) {
         this.writeUTF((String)var1);
      } else {
         if (!(var1 instanceof byte[])) {
            throw new MessageFormatException(JMSClientExceptionLogger.logInvalidObjectLoggable(var1.getClass().getName()).getMessage());
         }

         this.writeBytes((byte[])((byte[])var1));
      }

   }

   public void reset() throws javax.jms.JMSException {
      try {
         this.setBodyWritable(false);
         if (this.bis != null) {
            this.bis.reset();
         } else if (this.bos != null) {
            this.payload = (PayloadStream)this.bos.moveToPayload();
            this.bos = null;
         }

         this.payloadCopyOnWrite = false;
      } catch (IOException var2) {
         throw new JMSException(getReadError(578), var2);
      }
   }

   public MessageImpl copy() throws javax.jms.JMSException {
      BytesMessageImpl var1 = new BytesMessageImpl();
      super.copy(var1);
      if (this.bos != null) {
         var1.payload = this.bos.copyPayloadWithoutSharedStream();
      } else if (this.payload != null) {
         var1.payload = this.payload.copyPayloadWithoutSharedStream();
      }

      var1.payloadCopyOnWrite = this.payloadCopyOnWrite = true;
      var1.setBodyWritable(false);
      var1.setPropertiesWritable(false);
      return var1;
   }

   private void checkWritable() throws javax.jms.JMSException {
      super.writeMode();
      if (this.bos == null) {
         this.bos = PayloadFactoryImpl.createOutputStream();
      } else if (this.payloadCopyOnWrite) {
         this.bos.copyBuffer();
         this.payloadCopyOnWrite = false;
      }

   }

   private void checkReadable() throws javax.jms.JMSException {
      super.readMode();
      this.decompressMessageBody();
      if (this.bis == null) {
         if (this.payload == null) {
            throw new MessageEOFException(this.getReadPastEnd(120));
         }

         try {
            this.bis = this.payload.getInputStream();
         } catch (IOException var2) {
            throw new JMSException(getReadError(510), var2);
         }
      }

   }

   public String toString() {
      return "BytesMessage[" + this.getJMSMessageID() + "]";
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      int var3 = Integer.MAX_VALUE;
      ObjectOutput var2;
      if (var1 instanceof MessageImpl.JMSObjectOutputWrapper) {
         var2 = ((MessageImpl.JMSObjectOutputWrapper)var1).getInnerObjectOutput();
         var3 = ((MessageImpl.JMSObjectOutputWrapper)var1).getCompressionThreshold();
      } else {
         var2 = var1;
      }

      byte var4;
      if (this.getVersion(var2) >= 30) {
         var4 = (byte)(3 | (this.shouldCompress(var2, var3) ? -128 : 0));
      } else {
         var4 = 2;
      }

      var2.writeByte(var4);
      if (debugWire && JMSDebug.JMSDispatcher.isDebugEnabled()) {
         JMSDebug.JMSDispatcher.debug("BytesMessageImpl.write versionInt x" + Integer.toHexString(var4).toUpperCase() + ((var4 & -128) != 0 ? " compression is on" : ""));
      }

      if (this.isCompressed()) {
         if (var4 == 2) {
            this.decompress().writeLengthAndData(var2);
         } else {
            this.flushCompressedMessageBody(var2);
         }

      } else {
         Object var5;
         if (this.bos != null) {
            var5 = this.bos;
         } else {
            if (this.payload == null) {
               var2.writeInt(0);
               return;
            }

            var5 = this.payload;
         }

         if ((var4 & -128) != 0) {
            this.writeExternalCompressPayload(var2, (Payload)var5);
         } else {
            ((Payload)var5).writeLengthAndData(var2);
         }

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
      if (debugWire && JMSDebug.JMSDispatcher.isDebugEnabled()) {
         JMSDebug.JMSDispatcher.debug("BytesMessageImpl.read  versionInt x" + Integer.toHexString(var3).toUpperCase());
      }

      if (var3 >= 1 && var3 <= 3) {
         switch (var3) {
            case 1:
               if (var1.readBoolean()) {
                  this.payload = (PayloadStream)PayloadFactoryImpl.createPayload((InputStream)var1);
               }
               break;
            case 3:
               if ((var2 & -128) != 0) {
                  this.readExternalCompressedMessageBody(var1);
                  break;
               }
            case 2:
               this.payload = (PayloadStream)PayloadFactoryImpl.createPayload((InputStream)var1);
         }

         this.setBodyWritable(false);
         this.setPropertiesWritable(false);
      } else {
         throw JMSUtilities.versionIOException(var3, 1, 3);
      }
   }

   public long getPayloadSize() {
      if (this.isCompressed()) {
         return (long)this.getCompressedMessageBodySize();
      } else if (super.bodySize != -1L) {
         return super.bodySize;
      } else if (this.payload != null) {
         return super.bodySize = (long)this.payload.getLength();
      } else {
         return this.bos != null ? (long)this.bos.size() : (super.bodySize = 0L);
      }
   }

   private long getLen() {
      if (this.bos != null) {
         return (long)this.bos.size();
      } else {
         return this.payload != null ? (long)this.payload.getLength() : 0L;
      }
   }

   public long getBodyLength() throws javax.jms.JMSException {
      super.readMode();
      return this.getLen();
   }

   public byte[] getBodyBytes() throws javax.jms.JMSException {
      Object var1;
      if (this.payload != null) {
         var1 = this.payload;
      } else {
         if (this.bos == null) {
            return new byte[0];
         }

         var1 = this.bos;
      }

      try {
         ByteArrayOutputStream var2 = new ByteArrayOutputStream();
         ((Payload)var1).writeTo(var2);
         var2.flush();
         return var2.toByteArray();
      } catch (IOException var3) {
         throw new JMSException(var3);
      }
   }

   public PayloadStream getPayload() throws javax.jms.JMSException {
      if (this.isCompressed()) {
         try {
            this.payload = (PayloadStream)this.decompress();
         } catch (IOException var2) {
            throw new JMSException(JMSClientExceptionLogger.logErrorDecompressMessageBodyLoggable().getMessage(), var2);
         }
      }

      return this.payload;
   }

   public void setPayload(PayloadStream var1) {
      if (this.payload == null && this.bis == null && this.bos == null && !this.payloadCopyOnWrite) {
         try {
            this.writeMode();
         } catch (javax.jms.JMSException var3) {
            throw new AssertionError(var3);
         }

         this.payload = var1;
      } else {
         throw new AssertionError();
      }
   }
}
