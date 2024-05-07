package weblogic.jms.dotnet.transport.socketplugin;

import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import weblogic.jms.dotnet.transport.MarshalReadable;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.Transport;
import weblogic.utils.StringUtils;

public class ChunkInputStream extends InputStream implements MarshalReader, DataInput {
   private Chunk c = Chunk.alloc();
   private byte[] buf;
   private int pos;
   private int offset;
   private int count;
   private Transport transport;

   ChunkInputStream(Transport var1) {
      this.buf = this.c.getBuffer();
      this.count = this.buf.length;
      this.transport = var1;
   }

   void checkEOF(int var1) {
      if (this.pos + var1 > this.count) {
         throw new RuntimeException("EOF");
      }
   }

   byte[] getBuf() {
      return this.buf;
   }

   public void internalClose() {
      if (this.c != null) {
         this.c.free();
         this.c = null;
         this.buf = null;
      }
   }

   void setCount(int var1) {
      this.count = var1;
   }

   int size() {
      return this.count;
   }

   void setPos(int var1) {
      this.pos = var1;
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

   int getPosition() {
      return this.pos;
   }

   public int available() {
      return this.count - this.pos;
   }

   public void reset() {
      this.pos = this.offset;
   }

   public Transport getTransport() {
      return this.transport;
   }

   public MarshalReadable readMarshalable() {
      int var1 = this.readInt();
      int var2 = this.readInt();
      int var3 = this.pos + var2;
      if (this.pos + var2 > this.count) {
         throw new RuntimeException("EOF detected. Stream does not have enough bytes for reading entire MarshalReadable object(Marshal type code=" + var1 + ")");
      } else {
         MarshalReadable var4 = this.transport.createMarshalReadable(var1);
         var4.unmarshal(this);
         if (this.pos < var3) {
            this.skip((long)(var3 - this.pos));
         }

         return var4;
      }
   }

   public int read() {
      return this.pos < this.count ? this.buf[this.pos++] & 255 : -1;
   }

   public byte readByte() {
      this.checkEOF(1);
      return this.buf[this.pos++];
   }

   public int readUnsignedByte() {
      this.checkEOF(1);
      return this.buf[this.pos++] & 255;
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

   public boolean readBoolean() {
      this.checkEOF(1);
      return this.buf[this.pos++] != 0;
   }

   public short readShort() {
      this.checkEOF(2);
      int var1 = ((short)(this.buf[this.pos] & 255) << 8) + ((short)(this.buf[this.pos + 1] & 255) << 0);
      this.pos += 2;
      return (short)var1;
   }

   public char readChar() {
      this.checkEOF(2);
      int var1 = ((this.buf[this.pos] & 255) << 8) + ((this.buf[this.pos + 1] & 255) << 0);
      this.pos += 2;
      return (char)var1;
   }

   public int readInt() {
      this.checkEOF(4);
      int var1 = ((this.buf[this.pos] & 255) << 24) + ((this.buf[this.pos + 1] & 255) << 16) + ((this.buf[this.pos + 2] & 255) << 8) + ((this.buf[this.pos + 3] & 255) << 0);
      this.pos += 4;
      return var1;
   }

   public long readLong() {
      return ((long)this.readInt() << 32) + ((long)this.readInt() & 4294967295L);
   }

   public float readFloat() {
      return Float.intBitsToFloat(this.readInt());
   }

   public double readDouble() {
      return Double.longBitsToDouble(this.readLong());
   }

   public final String readString() {
      int var1 = this.readInt();
      char[] var2 = new char[var1];
      this.checkEOF(var1);

      int var3;
      int var4;
      for(var3 = 0; var3 < var1; var2[var3++] = (char)var4) {
         var4 = this.buf[this.pos++] & 255;
         if ((var4 & 128) != 0) {
            int var5;
            if ((var4 & 224) == 192) {
               var5 = this.buf[this.pos++] & 255;
               var4 = ((var4 & 31) << 6) + (var5 & 63);
            } else {
               var5 = this.buf[this.pos++] & 255;
               int var6 = this.buf[this.pos++] & 255;
               var4 = ((var4 & 15) << 12) + ((var5 & 63) << 6) + (var6 & 63);
            }
         }
      }

      return StringUtils.getString(var2, 0, var3);
   }

   public byte[] readStringAsBytes() {
      int var1 = this.readInt();
      this.checkEOF(var1);
      this.pos -= 4;
      byte[] var2 = new byte[var1 + 4];
      this.read(var2, 0, var2.length);
      return var2;
   }

   public DataInput getDataInputStream() {
      return this;
   }

   public void readFully(byte[] var1, int var2, int var3) throws IOException {
      throw new IOException("unresolvable");
   }

   public void readFully(byte[] var1) throws IOException {
      throw new IOException("unresolvable");
   }

   public String readLine() throws IOException {
      throw new IOException("unresolvable");
   }

   public int readUnsignedShort() throws IOException {
      throw new IOException("unresolvable");
   }

   public String readUTF() throws IOException {
      throw new IOException("unresolvable");
   }

   public int skipBytes(int var1) throws IOException {
      throw new IOException("unresolvable");
   }
}
