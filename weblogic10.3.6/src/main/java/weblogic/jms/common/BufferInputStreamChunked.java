package weblogic.jms.common;

import java.io.DataInput;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.StreamCorruptedException;
import java.io.UTFDataFormatException;
import weblogic.jms.JMSClientExceptionLogger;
import weblogic.utils.io.Chunk;
import weblogic.utils.io.ChunkInput;
import weblogic.utils.io.ChunkedDataInputStream;
import weblogic.utils.io.DataIO;
import weblogic.utils.io.StringInput;

public class BufferInputStreamChunked extends BufferInputStream implements StringInput, ChunkInput, ObjectInput {
   private final ObjectIOBypass objectIOBypass;
   private final ChunkedDataInputStream cdis;
   private int mark = 0;
   private static final int VERSION = 1234;

   public BufferInputStreamChunked(ObjectIOBypass var1, ChunkedDataInputStream var2) {
      this.objectIOBypass = var1;
      this.cdis = var2;
      this.internalMarkForCDIS();
   }

   private void internalMarkForCDIS() {
      this.cdis.mark(16384);
   }

   private void internalReset() {
      this.cdis.reset();
      this.internalMarkForCDIS();
   }

   ChunkedDataInputStream getInternalCDIS() {
      return this.cdis;
   }

   public void unput() throws IOException {
      int var1 = this.cdis.pos() - 1;
      this.internalReset();
      this.cdis.skip((long)var1);
   }

   public void reset() throws IOException {
      this.internalReset();
      this.skip((long)this.mark);
      this.mark = 0;
   }

   public void mark(int var1) {
      this.mark = this.pos();
   }

   public int pos() {
      return this.cdis.pos();
   }

   public void gotoPos(int var1) throws IOException {
      this.mark = 0;
      this.internalReset();
      this.skip((long)var1);
   }

   public boolean markSupported() {
      return false;
   }

   public int size() {
      return Chunk.size(this.cdis.getChunks());
   }

   public int read() throws IOException {
      return this.cdis.read();
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      return this.cdis.read(var1, var2, var3);
   }

   public int available() {
      return this.cdis.available();
   }

   public synchronized void close() throws IOException {
   }

   public final void readFully(byte[] var1) throws IOException {
      this.readFully(var1, 0, var1.length);
   }

   public final void readFully(byte[] var1, int var2, int var3) throws IOException {
      this.cdis.readFully(var1, var2, var3);
   }

   public final int skipBytes(int var1) throws IOException {
      return this.cdis.skipBytes(var1);
   }

   public Chunk readChunks() throws IOException {
      return this.cdis.readChunks();
   }

   public int peekInt(int var1) throws IOException {
      return this.cdis.peekInt(var1);
   }

   public final Object readObject() throws IOException, ClassNotFoundException {
      int var1 = this.readInt();
      if (var1 != 1234) {
         throw new StreamCorruptedException(JMSClientExceptionLogger.logUnknownStreamVersionLoggable(var1).getMessage());
      } else if (this.objectIOBypass == null) {
         throw new StreamCorruptedException(JMSClientExceptionLogger.logRawObjectErrorLoggable().getMessage());
      } else {
         return this.objectIOBypass.readObject(this);
      }
   }

   public final boolean readBoolean() throws IOException {
      return this.cdis.readBoolean();
   }

   public final byte readByte() throws IOException {
      return this.cdis.readByte();
   }

   public final int readUnsignedByte() throws IOException {
      return this.cdis.readUnsignedByte();
   }

   public final short readShort() throws IOException {
      return this.cdis.readShort();
   }

   public final int readUnsignedShort() throws IOException {
      return this.cdis.readUnsignedShort();
   }

   public final char readChar() throws IOException {
      return this.cdis.readChar();
   }

   public final int readInt() throws IOException {
      return this.cdis.readInt();
   }

   public final long readLong() throws IOException {
      return this.cdis.readLong();
   }

   public final float readFloat() throws IOException {
      return Float.intBitsToFloat(this.cdis.readInt());
   }

   public final double readDouble() throws IOException {
      return Double.longBitsToDouble(this.cdis.readLong());
   }

   public final String readLine() throws IOException {
      throw new IOException(JMSClientExceptionLogger.logNotImplementedLoggable().getMessage());
   }

   public final String readUTF() throws IOException {
      return DataIO.readUTF(this);
   }

   public final String readASCII() throws IOException {
      return this.cdis.readASCII();
   }

   public final String readUTF8() throws IOException {
      return this.cdis.readUTF8();
   }

   public final String readUTF32() throws IOException {
      return readUTF32(this);
   }

   static String readUTF32(DataInput var0) throws IOException {
      int var1 = var0.readInt();
      StringBuffer var2 = new StringBuffer(var1);
      int var6 = 0;

      while(var6 < var1) {
         int var3 = var0.readByte() & 255;
         byte var4;
         switch (var3 >> 4) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
               ++var6;
               var2.append((char)var3);
               break;
            case 8:
            case 9:
            case 10:
            case 11:
            default:
               throw new UTFDataFormatException();
            case 12:
            case 13:
               var6 += 2;
               if (var6 > var1) {
                  throw new UTFDataFormatException();
               }

               var4 = var0.readByte();
               if ((var4 & 192) != 128) {
                  throw new UTFDataFormatException();
               }

               var2.append((char)((var3 & 31) << 6 | var4 & 63));
               break;
            case 14:
               var6 += 3;
               if (var6 > var1) {
                  throw new UTFDataFormatException();
               }

               var4 = var0.readByte();
               byte var5 = var0.readByte();
               if ((var4 & 192) != 128 || (var5 & 192) != 128) {
                  throw new UTFDataFormatException();
               }

               var2.append((char)((var3 & 15) << 12 | (var4 & 63) << 6 | (var5 & 63) << 0));
         }
      }

      return new String(var2);
   }
}
