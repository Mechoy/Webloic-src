package weblogic.servlet.internal;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import javax.servlet.ServletResponse;
import weblogic.utils.io.Chunk;

public class CharChunkOutput extends ChunkOutput {
   private int count;
   private char[] buf;
   private CharsetDecoder decoder;
   private static final int BUF_SIZE = 64;
   private String encoding;

   public CharChunkOutput(ServletResponse var1) {
      this.autoflush = false;
      this.buflimit = -1;
      this.encoding = var1.getCharacterEncoding();
   }

   public String getEncoding() {
      return this.encoding;
   }

   public void reset() {
      this.count = 0;
   }

   public void release() {
      this.count = 0;
      this.buf = null;
      this.decoder = null;
   }

   public Chunk getHead() {
      return null;
   }

   public int getTotal() {
      return 0;
   }

   public int getCount() {
      return this.count;
   }

   public int getBufferSize() {
      return -1;
   }

   public void setBufferSize(int var1) {
   }

   public boolean isAutoFlush() {
      return this.autoflush;
   }

   public void setAutoFlush(boolean var1) {
   }

   public boolean isChunking() {
      return false;
   }

   public void setChunking(boolean var1) {
   }

   public void write(int var1) throws IOException {
      this.initBuf();
      int var2 = this.adjustBuf(1);
      this.buf[this.count] = (char)var1;
      this.count = var2;
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      this.initBuf();
      if (this.decoder == null) {
         Charset var4 = Charset.forName(this.getEncoding());
         this.decoder = var4.newDecoder();
      }

      ByteBuffer var9 = ByteBuffer.wrap(var1, var2, var3);
      int var5 = (int)(this.decoder.averageCharsPerByte() * (float)var3);
      this.adjustBuf(var5);
      boolean var6 = false;

      while(var9.hasRemaining()) {
         CharBuffer var7 = CharBuffer.wrap(this.buf, this.count, this.buf.length - this.count);
         int var10 = var7.position();
         CoderResult var8 = this.decoder.decode(var9, var7, true);
         this.count += var7.position() - var10;
         if (var8 == CoderResult.UNDERFLOW) {
            break;
         }

         if (var8 == CoderResult.OVERFLOW) {
            var5 = (int)((float)var9.remaining() * this.decoder.averageCharsPerByte());
            this.adjustBuf(var5 == 0 ? 1 : var5);
         } else {
            var8.throwException();
         }
      }

   }

   public void write(char[] var1, int var2, int var3) throws IOException {
      this.initBuf();
      int var4 = this.adjustBuf(var3);
      System.arraycopy(var1, var2, this.buf, this.count, var3);
      this.count = var4;
   }

   public void print(String var1) throws IOException {
      this.initBuf();
      int var2 = this.adjustBuf(var1.length());
      var1.getChars(0, var1.length(), this.buf, this.count);
      this.count = var2;
   }

   public void commit() throws IOException {
   }

   public void clearBuffer() {
      this.count = 0;
   }

   public void flush() throws IOException {
   }

   public void writeStream(InputStream var1, int var2, int var3) throws IOException {
      Chunk var4 = Chunk.getChunk();
      byte[] var5 = var4.buf;

      try {
         boolean var7 = var2 == -1;
         int var8 = 0;

         int var6;
         while((var6 = var1.read()) != -1) {
            if (var8 == Chunk.CHUNK_SIZE) {
               this.write((byte[])var5, 0, var8);
               var8 = 0;
            }

            var5[var8] = (byte)var6;
            --var2;
            ++var8;
            if (!var7 && var2 == 0) {
               break;
            }
         }

         if (var8 > 0) {
            this.write((byte[])var5, 0, var8);
            boolean var13 = false;
         }
      } finally {
         Chunk.releaseChunk(var4);
      }

   }

   private int adjustBuf(int var1) {
      int var2 = this.count + var1;
      if (var2 > this.buf.length) {
         char[] var3 = new char[Math.max(this.buf.length << 1, var2)];
         System.arraycopy(this.buf, 0, var3, 0, this.count);
         this.buf = var3;
      }

      return var2;
   }

   private void initBuf() {
      if (this.buf == null) {
         this.buf = new char[64];
      }

   }

   public char[] getCharBuffer() {
      return this.buf;
   }

   private void p(String var1) {
      System.err.println("[CharChunkOutput]" + var1);
   }
}
