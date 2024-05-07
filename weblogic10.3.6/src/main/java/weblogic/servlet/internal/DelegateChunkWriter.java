package weblogic.servlet.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;

public class DelegateChunkWriter extends ChunkOutput {
   private static final boolean DEBUG = false;
   protected Writer writer;
   private ServletResponse response;
   private boolean autoFlush;
   private String enc;

   public DelegateChunkWriter(ServletResponse var1, boolean var2, int var3) throws IOException {
      this.response = var1;
      this.autoFlush = var2;
   }

   protected DelegateChunkWriter(Writer var1) {
      this.writer = var1;
   }

   public String getEncoding() {
      return this.response != null ? this.response.getCharacterEncoding() : null;
   }

   public void reset() {
   }

   public void release() {
   }

   public int getTotal() {
      return 0;
   }

   public int getCount() {
      return 0;
   }

   public int getBufferSize() {
      return this.response != null ? this.response.getBufferSize() : 0;
   }

   public void setBufferSize(int var1) {
   }

   public void setStickyBufferSize(boolean var1) {
   }

   public boolean isAutoFlush() {
      return this.autoFlush;
   }

   public void setAutoFlush(boolean var1) {
      this.autoFlush = var1;
   }

   public boolean isChunking() {
      return false;
   }

   public void setChunking(boolean var1) {
   }

   public void write(int var1) throws IOException {
      this.getWriter().write(var1);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      if (this.response != null) {
         this.response.getOutputStream().write(var1, var2, var3);
      }

   }

   public void write(char[] var1, int var2, int var3) throws IOException {
      this.getWriter().write(var1, var2, var3);
   }

   public void print(String var1) throws IOException {
      if (var1 != null) {
         this.getWriter().write(var1, 0, var1.length());
      }

   }

   public void commit() throws IOException {
   }

   public void clearBuffer() {
      if (this.response != null) {
         this.response.resetBuffer();
      }

   }

   public void flush() throws IOException {
      this.getWriter().flush();
      if (this.response != null) {
         this.response.flushBuffer();
      }

   }

   public void writeStream(InputStream var1, int var2, int var3) throws IOException {
   }

   protected Writer getWriter() throws IOException {
      String var1 = this.response.getCharacterEncoding();
      if (this.writer == null || var1 != null && !this.enc.equalsIgnoreCase(var1)) {
         ServletOutputStream var2 = this.response.getOutputStream();
         this.enc = var1;
         if (this.enc == null) {
            this.enc = "ISO-8859-1";
         }

         this.writer = new WLOutputStreamWriter(var2, this.enc);
      }

      if (this.writer == null) {
         throw new IOException("Writer already closed ");
      } else {
         return this.writer;
      }
   }

   static void p(String var0) {
      System.out.println("[DelegateChunkWriter]" + var0);
   }

   public static class DelegateJspChunkWriter extends DelegateChunkWriter {
      public DelegateJspChunkWriter(Writer var1) {
         super(var1);
      }

      protected Writer getWriter() throws IOException {
         return this.writer;
      }

      public void clearBuffer() {
      }

      public int getBufferSize() {
         return 0;
      }

      public int getRemaining() {
         return 0;
      }

      public String getEncoding() {
         return null;
      }

      public void flush() throws IOException {
         this.writer.flush();
      }

      public void write(byte[] var1, int var2, int var3) throws IOException {
      }

      public void writeStream(InputStream var1, int var2, int var3) throws IOException {
         throw new AssertionError("writeStream called ");
      }
   }
}
