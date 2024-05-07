package weblogic.jms.common;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.StreamCorruptedException;
import java.io.UTFDataFormatException;
import weblogic.jms.JMSClientExceptionLogger;
import weblogic.utils.io.DataIO;

public final class BufferDataInputStream extends BufferInputStream implements ObjectInput {
   private byte[] buf;
   private int pos;
   private int mark = 0;
   private final int offset;
   private int count;
   private final ObjectIOBypass objectIOBypass;
   private static final int VERSION = 1234;

   public BufferDataInputStream(ObjectIOBypass var1, byte[] var2) {
      this.buf = var2;
      this.pos = 0;
      this.count = var2.length;
      this.offset = 0;
      this.objectIOBypass = var1;
   }

   public BufferDataInputStream(ObjectIOBypass var1, byte[] var2, int var3, int var4) {
      this.buf = var2;
      this.pos = var3;
      this.count = Math.min(var3 + var4, var2.length);
      this.offset = var3;
      this.objectIOBypass = var1;
   }

   public int read() {
      return this.pos < this.count ? this.buf[this.pos++] & 255 : -1;
   }

   public void unput() {
      --this.pos;
   }

   public int size() {
      return this.count;
   }

   public int read(byte[] var1, int var2, int var3) {
      if (this.pos >= this.count) {
         return -1;
      } else {
         if (this.pos + var3 > this.count) {
            var3 = this.count - this.pos;
         }

         if (var3 <= 0) {
            return 0;
         } else {
            System.arraycopy(this.buf, this.pos, var1, var2, var3);
            this.pos += var3;
            return var3;
         }
      }
   }

   public long skip(long var1) {
      if ((long)this.pos + var1 > (long)this.count) {
         var1 = (long)(this.count - this.pos);
      }

      if (var1 < 0L) {
         return 0L;
      } else {
         this.pos = (int)((long)this.pos + var1);
         return var1;
      }
   }

   public int available() {
      return this.count - this.pos;
   }

   public boolean markSupported() {
      return false;
   }

   public void mark(int var1) {
      this.mark = this.pos;
   }

   public void reset() {
      this.pos = this.offset;
   }

   public int pos() {
      return this.pos;
   }

   void gotoPos(int var1) throws IOException {
      this.pos = var1;
   }

   public synchronized void close() throws IOException {
   }

   public final void readFully(byte[] var1) throws IOException {
      this.readFully(var1, 0, var1.length);
   }

   public final void readFully(byte[] var1, int var2, int var3) throws IOException {
      if (this.pos + var3 > this.count) {
         throw new EOFException();
      } else {
         System.arraycopy(this.buf, this.pos, var1, var2, var3);
         this.pos += var3;
      }
   }

   public final int skipBytes(int var1) throws IOException {
      int var2 = Math.min(this.count - this.pos, var1);
      this.pos += var2;
      return var2;
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
      if (this.pos == this.count) {
         throw new EOFException();
      } else {
         return this.buf[this.pos++] != 0;
      }
   }

   public final byte readByte() throws IOException {
      if (this.pos == this.count) {
         throw new EOFException();
      } else {
         return this.buf[this.pos++];
      }
   }

   public final int readUnsignedByte() throws IOException {
      if (this.pos == this.count) {
         throw new EOFException();
      } else {
         return this.buf[this.pos++] & 255;
      }
   }

   public final short readShort() throws IOException {
      if (this.pos + 2 > this.count) {
         throw new EOFException();
      } else {
         int var1 = ((short)(this.buf[this.pos] & 255) << 8) + ((short)(this.buf[this.pos + 1] & 255) << 0);
         this.pos += 2;
         return (short)var1;
      }
   }

   public final int readUnsignedShort() throws IOException {
      if (this.pos + 2 > this.count) {
         throw new EOFException();
      } else {
         int var1 = ((this.buf[this.pos] & 255) << 8) + ((this.buf[this.pos + 1] & 255) << 0);
         this.pos += 2;
         return var1;
      }
   }

   public final char readChar() throws IOException {
      if (this.pos + 2 > this.count) {
         throw new EOFException();
      } else {
         int var1 = ((this.buf[this.pos] & 255) << 8) + ((this.buf[this.pos + 1] & 255) << 0);
         this.pos += 2;
         return (char)var1;
      }
   }

   final int peekInt(int var1) throws IOException {
      int var2 = this.pos + var1;
      return var2 + 4 > this.count ? -42 : ((this.buf[this.pos] & 255) << 24) + ((this.buf[this.pos + 1] & 255) << 16) + ((this.buf[this.pos + 2] & 255) << 8) + ((this.buf[this.pos + 3] & 255) << 0);
   }

   public final int readInt() throws IOException {
      if (this.pos + 4 > this.count) {
         throw new EOFException();
      } else {
         int var1 = ((this.buf[this.pos] & 255) << 24) + ((this.buf[this.pos + 1] & 255) << 16) + ((this.buf[this.pos + 2] & 255) << 8) + ((this.buf[this.pos + 3] & 255) << 0);
         this.pos += 4;
         return var1;
      }
   }

   public final long readLong() throws IOException {
      if (this.pos + 8 > this.count) {
         throw new EOFException();
      } else {
         return ((long)this.readInt() << 32) + ((long)this.readInt() & 4294967295L);
      }
   }

   public final float readFloat() throws IOException {
      return Float.intBitsToFloat(this.readInt());
   }

   public final double readDouble() throws IOException {
      return Double.longBitsToDouble(this.readLong());
   }

   public final String readLine() throws IOException {
      throw new IOException(JMSClientExceptionLogger.logNotImplementedLoggable().getMessage());
   }

   public final String readUTF() throws IOException {
      return DataInputStream.readUTF(this);
   }

   public final String readUTF32() throws IOException {
      return readUTF32(this);
   }

   public final String readUTF8() throws IOException {
      return DataIO.readUTF8(this);
   }

   public static String readUTF32(DataInput var0) throws IOException {
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
