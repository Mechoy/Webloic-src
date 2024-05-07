package weblogic.servlet.internal;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

class IllegalStateChunkOutput extends ChunkOutput {
   IllegalStateException exception;

   IllegalStateChunkOutput(IllegalStateException var1) {
      this.exception = var1;
   }

   void writeByte(int var1) throws IOException {
      throw this.exception;
   }

   public void write(int var1) throws IOException {
      throw this.exception;
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      throw this.exception;
   }

   public void write(ByteBuffer var1) throws IOException {
      throw this.exception;
   }

   public void write(char[] var1, int var2, int var3) throws IOException {
      throw this.exception;
   }

   public void print(String var1) throws IOException {
      throw this.exception;
   }

   public void println(String var1) throws IOException {
      throw this.exception;
   }

   public void println() throws IOException {
      throw this.exception;
   }

   public void commit() throws IOException {
      throw this.exception;
   }

   public void flush() throws IOException {
      throw this.exception;
   }

   public void writeStream(InputStream var1, int var2, int var3) throws IOException {
      throw this.exception;
   }
}
