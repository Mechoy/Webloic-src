package weblogic.jms.dotnet.transport.socketplugin;

import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;
import weblogic.jms.dotnet.transport.MarshalWritable;
import weblogic.jms.dotnet.transport.MarshalWriter;
import weblogic.jms.dotnet.transport.Transport;

public class ChunkOutputStream extends OutputStream implements MarshalWriter, DataOutput {
   private Chunk c = Chunk.alloc();
   private byte[] buf;
   private int count;
   private Transport transport;

   ChunkOutputStream(Transport var1) {
      this.buf = this.c.getBuffer();
      this.transport = var1;
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

   void reset() {
      this.count = 0;
   }

   public void reposition(int var1) {
      this.count = var1;
   }

   public int size() {
      return this.count;
   }

   int getPosition() {
      return this.count;
   }

   void skip(int var1) {
      this.count += var1;
   }

   public Transport getTransport() {
      return this.transport;
   }

   public void writeMarshalable(MarshalWritable var1) {
      this.writeInt(var1.getMarshalTypeCode());
      int var2 = this.count;
      this.writeInt(Integer.MIN_VALUE);
      int var3 = this.count;
      var1.marshal(this);
      int var4 = this.count;
      int var5 = var4 - var3;
      this.buf[var2] = (byte)(var5 >>> 24);
      this.buf[var2 + 1] = (byte)(var5 >>> 16);
      this.buf[var2 + 2] = (byte)(var5 >>> 8);
      this.buf[var2 + 3] = (byte)(var5 >>> 0);
   }

   public void writeByte(byte var1) {
      this.buf[this.count++] = var1;
   }

   public void writeUnsignedByte(int var1) {
      this.buf[this.count++] = (byte)(var1 & 255);
   }

   public void write(byte[] var1, int var2, int var3) {
      if (var3 != 0) {
         int var4 = this.count + var3;
         System.arraycopy(var1, var2, this.buf, this.count, var3);
         this.count = var4;
      }
   }

   public void writeBoolean(boolean var1) {
      this.buf[this.count++] = (byte)(var1 ? 1 : 0);
   }

   public void writeShort(short var1) {
      this.buf[this.count++] = (byte)(var1 >>> 8);
      this.buf[this.count++] = (byte)(var1 >>> 0);
   }

   public void writeChar(char var1) {
      this.buf[this.count++] = (byte)(var1 >>> 8);
      this.buf[this.count++] = (byte)(var1 >>> 0);
   }

   public void writeInt(int var1) {
      this.buf[this.count++] = (byte)(var1 >>> 24);
      this.buf[this.count++] = (byte)(var1 >>> 16);
      this.buf[this.count++] = (byte)(var1 >>> 8);
      this.buf[this.count++] = (byte)(var1 >>> 0);
   }

   public void writeLong(long var1) {
      this.buf[this.count++] = (byte)((int)(var1 >>> 56));
      this.buf[this.count++] = (byte)((int)(var1 >>> 48));
      this.buf[this.count++] = (byte)((int)(var1 >>> 40));
      this.buf[this.count++] = (byte)((int)(var1 >>> 32));
      this.buf[this.count++] = (byte)((int)(var1 >>> 24));
      this.buf[this.count++] = (byte)((int)(var1 >>> 16));
      this.buf[this.count++] = (byte)((int)(var1 >>> 8));
      this.buf[this.count++] = (byte)((int)(var1 >>> 0));
   }

   public void writeFloat(float var1) {
      int var2 = Float.floatToIntBits(var1);
      this.buf[this.count++] = (byte)(var2 >>> 24);
      this.buf[this.count++] = (byte)(var2 >>> 16);
      this.buf[this.count++] = (byte)(var2 >>> 8);
      this.buf[this.count++] = (byte)(var2 >>> 0);
   }

   public void writeDouble(double var1) {
      long var3 = Double.doubleToLongBits(var1);
      this.buf[this.count++] = (byte)((int)(var3 >>> 56));
      this.buf[this.count++] = (byte)((int)(var3 >>> 48));
      this.buf[this.count++] = (byte)((int)(var3 >>> 40));
      this.buf[this.count++] = (byte)((int)(var3 >>> 32));
      this.buf[this.count++] = (byte)((int)(var3 >>> 24));
      this.buf[this.count++] = (byte)((int)(var3 >>> 16));
      this.buf[this.count++] = (byte)((int)(var3 >>> 8));
      this.buf[this.count++] = (byte)((int)(var3 >>> 0));
   }

   public void writeString(String var1) {
      int var2 = var1.length();
      int var3 = this.count;
      this.count += 4;

      int var4;
      for(var4 = 0; var4 < var2; ++var4) {
         char var5 = var1.charAt(var4);
         if ((var5 & 'ﾀ') == 0) {
            this.buf[this.count++] = (byte)var5;
         } else if ((var5 & '\uf800') == 0) {
            this.buf[this.count++] = (byte)(192 | var5 >> 6 & 31);
            this.buf[this.count++] = (byte)(128 | var5 >> 0 & 63);
         } else {
            this.buf[this.count++] = (byte)(224 | var5 >> 12 & 15);
            this.buf[this.count++] = (byte)(128 | var5 >> 6 & 63);
            this.buf[this.count++] = (byte)(128 | var5 >> 0 & 63);
         }
      }

      var4 = this.count - var3 - 4;
      this.reposition(var3);
      this.writeInt(var4);
      this.skip(var4);
   }

   public DataOutput getDataOutputStream() {
      return this;
   }

   public void writeByte(int var1) throws IOException {
      throw new IOException("unresolvable");
   }

   public void writeBytes(String var1) throws IOException {
      throw new IOException("unresolvable");
   }

   public void writeChar(int var1) throws IOException {
      throw new IOException("unresolvable");
   }

   public void writeChars(String var1) throws IOException {
      throw new IOException("unresolvable");
   }

   public void writeShort(int var1) throws IOException {
      throw new IOException("unresolvable");
   }

   public void writeUTF(String var1) throws IOException {
      throw new IOException("unresolvable");
   }

   public void write(int var1) throws IOException {
      throw new IOException("unresolvable");
   }
}
