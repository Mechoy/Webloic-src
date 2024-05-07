package weblogic.servlet.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import weblogic.utils.http.BytesToString;
import weblogic.utils.io.Chunk;

public class ChunkOutputWrapper {
   public static final String OUTPUT_ATT_NAME = "weblogic.servlet.BodyTagOutput";
   private ChunkOutput currChunkoutput;
   private ChunkOutput realChunkOutput;
   private boolean writeEnabled = true;
   private boolean nativeControlsPipe = false;
   private boolean enforceCL = false;
   private int clen = -1;
   private static final ChunkOutput readonlyChunkoutput = new NullChunkOutput();
   private static final ChunkOutput nativePipeChunkoutput = new IllegalStateChunkOutput(new IllegalStateException("Can not send data via ServletOutputStream or Writer when native control pipe is used."));

   public static ChunkOutputWrapper getCurrentOutput(ServletRequest var0, ServletResponse var1) throws IOException {
      ChunkOutputWrapper var2 = null;
      var2 = (ChunkOutputWrapper)var0.getAttribute("weblogic.servlet.BodyTagOutput");
      if (var2 != null) {
         return var2;
      } else {
         ServletOutputStream var3 = var1.getOutputStream();
         if (var3 instanceof ServletOutputStreamImpl) {
            ServletOutputStreamImpl var4 = (ServletOutputStreamImpl)var3;
            var2 = var4.getOutput();
         }

         return var2;
      }
   }

   public ChunkOutputWrapper(ChunkOutput var1) {
      this.currChunkoutput = var1;
      this.realChunkOutput = var1;
   }

   public void changeToCharset(String var1, CharsetMap var2) throws UnsupportedEncodingException {
      if (BytesToString.is8BitUnicodeSubset(var1)) {
         this.realChunkOutput = ChunkOutput.create(this.realChunkOutput, (String)null, var2);
      } else {
         this.realChunkOutput = ChunkOutput.create(this.realChunkOutput, var1, var2);
      }

      if (this.writeEnabled) {
         this.currChunkoutput = this.realChunkOutput;
      }

   }

   public ChunkOutput getOutput() {
      return this.currChunkoutput;
   }

   public void setOutput(ChunkOutput var1) {
      this.currChunkoutput = var1;
      this.realChunkOutput = var1;
   }

   public void reset() {
      this.realChunkOutput.reset();
      this.enforceCL = false;
      this.clen = -1;
   }

   public void release() {
      this.realChunkOutput.release();
      this.enforceCL = false;
      this.clen = -1;
   }

   public int getTotal() {
      return this.realChunkOutput.getTotal();
   }

   public int getCount() {
      return this.realChunkOutput.getCount();
   }

   public int getBufferSize() {
      return this.realChunkOutput.getBufferSize();
   }

   public void setBufferSize(int var1) {
      this.realChunkOutput.setBufferSize(var1);
   }

   public boolean isAutoFlush() {
      return this.realChunkOutput.isAutoFlush();
   }

   public void setAutoFlush(boolean var1) {
      this.realChunkOutput.setAutoFlush(var1);
   }

   public boolean isChunking() {
      return this.realChunkOutput.isChunking();
   }

   public void setChunking(boolean var1) {
      this.realChunkOutput.setChunking(var1);
   }

   void writeByte(int var1) throws IOException {
      if (!this.enforceCL) {
         this.currChunkoutput.writeByte(var1);
      } else {
         if (this.remainingContent() > 0) {
            this.currChunkoutput.writeByte(var1);
         }

         if (this.remainingContent() == 0) {
            this.currChunkoutput.commit();
         }
      }

   }

   public void write(int var1) throws IOException {
      if (!this.enforceCL) {
         this.currChunkoutput.write(var1);
      } else {
         if (this.remainingContent() > 0) {
            this.currChunkoutput.write(var1);
         }

         if (this.remainingContent() == 0) {
            this.currChunkoutput.commit();
         }
      }

   }

   public void write(char[] var1, int var2, int var3) throws IOException {
      this.checkBoundary(var1.length, var2, var3);
      if (!this.enforceCL) {
         this.currChunkoutput.write(var1, var2, var3);
      } else {
         int var4 = this.remainingContent();
         if (var3 > var4) {
            var3 = var4;
         }

         this.currChunkoutput.write(var1, var2, var3);
         if (this.remainingContent() == 0) {
            this.currChunkoutput.commit();
         }
      }

   }

   public void write(char[] var1) throws IOException {
      if (!this.enforceCL) {
         this.currChunkoutput.write((char[])var1, 0, var1.length);
      } else {
         int var2 = this.remainingContent();
         int var3 = var1.length;
         if (var3 > var2) {
            var3 = var2;
         }

         this.currChunkoutput.write((char[])var1, 0, var3);
         if (this.remainingContent() == 0) {
            this.currChunkoutput.commit();
         }
      }

   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      this.checkBoundary(var1.length, var2, var3);
      if (!this.enforceCL) {
         this.currChunkoutput.write(var1, var2, var3);
      } else {
         int var4 = this.remainingContent();
         if (var3 > var4) {
            var3 = var4;
         }

         this.currChunkoutput.write(var1, var2, var3);
         if (this.remainingContent() == 0) {
            this.currChunkoutput.commit();
         }
      }

   }

   public void write(ByteBuffer var1) throws IOException {
      if (!this.enforceCL) {
         this.currChunkoutput.write(var1);
      } else {
         int var2 = var1.limit();
         int var3 = this.remainingContent();
         if (var2 > var3) {
            ;
         }

         this.currChunkoutput.write(var1);
         if (this.remainingContent() == 0) {
            this.currChunkoutput.commit();
         }
      }

   }

   public void print(String var1) throws IOException {
      if (!this.enforceCL) {
         this.currChunkoutput.print(var1);
      } else {
         int var2 = this.remainingContent();
         if (var1 == null) {
            var1 = "null";
         }

         int var3 = var1.length();
         if (var3 > var2) {
            var1 = var1.substring(0, var2);
         }

         this.currChunkoutput.print(var1);
         if (this.remainingContent() == 0) {
            this.currChunkoutput.commit();
         }
      }

   }

   public void clearBuffer() {
      this.realChunkOutput.clearBuffer();
   }

   public void flush() throws IOException {
      this.realChunkOutput.flush();
   }

   public void writeStream(InputStream var1, int var2) throws IOException {
      this.currChunkoutput.writeStream(var1, var2, this.clen);
   }

   public String getEncoding() {
      return this.realChunkOutput.getEncoding();
   }

   boolean isWriteEnabled() {
      return this.writeEnabled;
   }

   void setWriteEnabled(boolean var1) {
      this.writeEnabled = var1;
      if (!this.writeEnabled) {
         this.currChunkoutput = readonlyChunkoutput;
      } else {
         this.currChunkoutput = this.realChunkOutput;
      }

   }

   void setNativeControlsPipe(boolean var1) {
      this.nativeControlsPipe = var1;
      if (this.nativeControlsPipe) {
         this.currChunkoutput = nativePipeChunkoutput;
      } else {
         this.currChunkoutput = this.realChunkOutput;
      }

   }

   void setHttpHeaders(Chunk var1) {
      this.realChunkOutput.setHttpHeaders(var1);
   }

   public void commit() throws IOException {
      this.realChunkOutput.commit();
   }

   public void setCL(int var1) {
      this.clen = var1;
      if (this.clen != -1) {
         this.enforceCL = true;
      }

   }

   private int remainingContent() {
      return this.clen - (this.getTotal() + this.getCount());
   }

   private void checkBoundary(int var1, int var2, int var3) {
      if (var2 < 0 || var2 > var1 || var3 < 0 || var2 + var3 > var1 || var2 + var3 < 0) {
         throw new IndexOutOfBoundsException();
      }
   }
}
