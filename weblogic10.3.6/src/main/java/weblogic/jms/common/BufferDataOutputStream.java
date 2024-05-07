package weblogic.jms.common;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.OutputStream;
import weblogic.jms.JMSClientExceptionLogger;
import weblogic.utils.StringUtils;
import weblogic.utils.io.DataIO;

public final class BufferDataOutputStream extends BufferOutputStream implements PayloadStream, PayloadText, ObjectOutput {
   private byte[] buf;
   private final byte[] fixedSizeBuf;
   private int count;
   private final ObjectIOBypass objectIOBypass;
   private boolean isBypassOutputStream;
   private boolean isJMSStoreOutputStream;
   private boolean isJMSMulticastOutputStream;
   private static final int VERSION = 1234;

   public BufferDataOutputStream(ObjectIOBypass var1, int var2) {
      this.objectIOBypass = var1;
      this.buf = new byte[var2];
      this.fixedSizeBuf = null;
   }

   public BufferDataOutputStream(ObjectIOBypass var1, byte[] var2) {
      this.objectIOBypass = var1;
      this.buf = this.fixedSizeBuf = var2;
   }

   BufferDataOutputStream(ObjectIOBypass var1, DataInput var2) throws IOException {
      this(var1, var2, var2.readInt());
   }

   BufferDataOutputStream(ObjectIOBypass var1, DataInput var2, int var3) throws IOException {
      this(var1, var3);
      var2.readFully(this.buf);
   }

   private void resizeBuf(int var1, int var2) throws IOException {
      if (var1 < var2) {
         if (this.fixedSizeBuf != null) {
            throw new IOException("exceeded fixed size allocation");
         } else {
            byte[] var3;
            if (var2 >= 2097152) {
               if (var2 >= 33554432) {
                  if (var2 >= 2139095038) {
                     var3 = new byte[Integer.MAX_VALUE];
                  } else {
                     var3 = new byte[var2 + 8388607 & -8388608];
                  }
               } else {
                  var3 = new byte[var2 + 1048575 & -1048576];
               }
            } else {
               var3 = new byte[var2 << 1];
            }

            System.arraycopy(this.buf, 0, var3, 0, var1);
            this.buf = var3;
         }
      }
   }

   public final boolean isJMSStoreOutputStream() {
      return this.isJMSStoreOutputStream;
   }

   public final boolean isBypassOutputStream() {
      return this.isBypassOutputStream;
   }

   public final boolean isJMSMulticastOutputStream() {
      return this.isJMSMulticastOutputStream;
   }

   public final void setIsJMSStoreOutputStream() {
      this.isJMSStoreOutputStream = true;
   }

   public final void setIsBypassOutputStream() {
      this.isBypassOutputStream = true;
   }

   public final void setIsJMSMulticastOutputStream() {
      this.isJMSMulticastOutputStream = true;
   }

   public final void write(int var1) throws IOException {
      if (this.count >= this.buf.length) {
         this.resizeBuf(this.count, this.count + 1);
      }

      this.buf[this.count++] = (byte)var1;
   }

   public final void write(byte[] var1, int var2, int var3) throws IOException {
      if (var3 != 0) {
         int var4 = this.count + var3;
         if (var4 > this.buf.length) {
            this.resizeBuf(this.count, var4);
         }

         System.arraycopy(var1, var2, this.buf, this.count, var3);
         this.count = var4;
      }
   }

   public final void writeTo(OutputStream var1) throws IOException {
      var1.write(this.buf, 0, this.count);
   }

   public final void reset() {
      this.count = 0;
   }

   public final byte[] getBuffer() {
      return this.buf;
   }

   public final ObjectOutput getObjectOutput() {
      return this;
   }

   final void copyBuffer() throws JMSException {
      byte[] var1 = new byte[this.buf.length];
      System.arraycopy(this.buf, 0, var1, 0, this.buf.length);
      this.buf = var1;
   }

   public final int size() {
      return this.count;
   }

   public final void writeObject(Object var1) throws IOException {
      this.writeInt(1234);
      if (this.objectIOBypass == null) {
         throw new IOException(JMSClientExceptionLogger.logRawObjectError2Loggable().getMessage());
      } else {
         this.objectIOBypass.writeObject(this, var1);
      }
   }

   public final void writeBoolean(boolean var1) throws IOException {
      if (this.count >= this.buf.length) {
         this.resizeBuf(this.count, this.count + 1);
      }

      this.buf[this.count++] = (byte)(var1 ? 1 : 0);
   }

   public final void writeByte(int var1) throws IOException {
      if (this.count >= this.buf.length) {
         this.resizeBuf(this.count, this.count + 1);
      }

      this.buf[this.count++] = (byte)var1;
   }

   public final void writeShort(int var1) throws IOException {
      if (this.count + 2 > this.buf.length) {
         this.resizeBuf(this.count, this.count + 2);
      }

      this.buf[this.count++] = (byte)(var1 >>> 8);
      this.buf[this.count++] = (byte)(var1 >>> 0);
   }

   public final void writeChar(int var1) throws IOException {
      if (this.count + 2 > this.buf.length) {
         this.resizeBuf(this.count, this.count + 2);
      }

      this.buf[this.count++] = (byte)(var1 >>> 8);
      this.buf[this.count++] = (byte)(var1 >>> 0);
   }

   public final void writeInt(int var1) throws IOException {
      if (this.count + 4 > this.buf.length) {
         this.resizeBuf(this.count, this.count + 4);
      }

      this.buf[this.count++] = (byte)(var1 >>> 24);
      this.buf[this.count++] = (byte)(var1 >>> 16);
      this.buf[this.count++] = (byte)(var1 >>> 8);
      this.buf[this.count++] = (byte)(var1 >>> 0);
   }

   public final void writeLong(long var1) throws IOException {
      if (this.count + 8 > this.buf.length) {
         this.resizeBuf(this.count, this.count + 8);
      }

      this.buf[this.count++] = (byte)((int)(var1 >>> 56));
      this.buf[this.count++] = (byte)((int)(var1 >>> 48));
      this.buf[this.count++] = (byte)((int)(var1 >>> 40));
      this.buf[this.count++] = (byte)((int)(var1 >>> 32));
      this.buf[this.count++] = (byte)((int)(var1 >>> 24));
      this.buf[this.count++] = (byte)((int)(var1 >>> 16));
      this.buf[this.count++] = (byte)((int)(var1 >>> 8));
      this.buf[this.count++] = (byte)((int)(var1 >>> 0));
   }

   public final void writeFloat(float var1) throws IOException {
      int var2 = Float.floatToIntBits(var1);
      if (this.count + 4 > this.buf.length) {
         this.resizeBuf(this.count, this.count + 4);
      }

      this.buf[this.count++] = (byte)(var2 >>> 24);
      this.buf[this.count++] = (byte)(var2 >>> 16);
      this.buf[this.count++] = (byte)(var2 >>> 8);
      this.buf[this.count++] = (byte)(var2 >>> 0);
   }

   public final void writeDouble(double var1) throws IOException {
      long var3 = Double.doubleToLongBits(var1);
      if (this.count + 8 > this.buf.length) {
         this.resizeBuf(this.count, this.count + 8);
      }

      this.buf[this.count++] = (byte)((int)(var3 >>> 56));
      this.buf[this.count++] = (byte)((int)(var3 >>> 48));
      this.buf[this.count++] = (byte)((int)(var3 >>> 40));
      this.buf[this.count++] = (byte)((int)(var3 >>> 32));
      this.buf[this.count++] = (byte)((int)(var3 >>> 24));
      this.buf[this.count++] = (byte)((int)(var3 >>> 16));
      this.buf[this.count++] = (byte)((int)(var3 >>> 8));
      this.buf[this.count++] = (byte)((int)(var3 >>> 0));
   }

   public final void writeBytes(String var1) throws IOException {
      BufferDataOutputStream var2 = this;
      int var3 = var1.length();

      for(int var4 = 0; var4 < var3; ++var4) {
         var2.write((byte)var1.charAt(var4));
      }

   }

   public final void writeChars(String var1) throws IOException {
      BufferDataOutputStream var2 = this;
      int var3 = var1.length();

      for(int var4 = 0; var4 < var3; ++var4) {
         char var5 = var1.charAt(var4);
         var2.write(var5 >>> 8 & 255);
         var2.write(var5 >>> 0 & 255);
      }

   }

   public final void writeUTF32(String var1) throws IOException {
      writeUTF32(this, var1);
   }

   public static void writeUTF32(ObjectOutput var0, String var1) throws IOException {
      int var2 = StringUtils.getUTFLength(var1);
      var0.writeInt(var2);
      int var3 = var1.length();

      for(int var4 = 0; var4 < var3; ++var4) {
         DataIO.writeUTFChar(var0, var1.charAt(var4));
      }

   }

   public final void writeUTF(String var1) throws IOException {
      DataIO.writeUTF(this, var1);
   }

   public void writeLengthAndData(DataOutput var1) throws IOException {
      var1.writeInt(this.count);
      var1.write(this.buf, 0, this.count);
   }

   public PayloadText copyPayloadWithoutSharedText() throws JMSException {
      byte[] var1 = new byte[this.count];
      System.arraycopy(this.buf, 0, var1, 0, this.count);
      BufferDataOutputStream var2 = new BufferDataOutputStream(this.objectIOBypass, var1);
      var2.count = this.count;
      return var2;
   }

   public PayloadStream copyPayloadWithoutSharedStream() throws JMSException {
      byte[] var1 = new byte[this.count];
      System.arraycopy(this.buf, 0, var1, 0, this.count);
      BufferDataOutputStream var2 = new BufferDataOutputStream(this.objectIOBypass, var1);
      var2.count = this.count;
      return var2;
   }

   public PayloadStream moveToPayload() {
      return this;
   }

   public BufferInputStream getInputStream() throws IOException {
      return new BufferDataInputStream(this.objectIOBypass, this.buf, 0, this.count);
   }

   public int getLength() {
      return this.count;
   }

   public String readUTF8() throws IOException {
      return (new BufferDataInputStream(this.objectIOBypass, this.buf, 0, this.count)).readUTF8();
   }
}
