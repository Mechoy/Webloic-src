package weblogic.servlet.jsp;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.ByteBuffer;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import weblogic.servlet.internal.CharChunkOutput;
import weblogic.servlet.internal.ChunkOutputWrapper;
import weblogic.servlet.internal.DelegateChunkWriter;

public final class BodyContentImpl extends BodyContent implements ByteWriter {
   ChunkOutputWrapper co;
   private CharChunkOutput coimpl;
   PageContextImpl pc;
   private boolean usingWriter;
   private static final String EOL = System.getProperty("line.separator");

   public BodyContentImpl(JspWriter var1, PageContextImpl var2, Writer var3) {
      super(var1);
      this.pc = var2;
      if (var3 == null) {
         ServletResponse var4 = var2.getResponse();
         this.coimpl = new CharChunkOutput(var4);
         this.coimpl.setStickyBufferSize(true);
         this.co = new ChunkOutputWrapper(this.coimpl);
      } else {
         this.usingWriter = true;
         DelegateChunkWriter.DelegateJspChunkWriter var5 = new DelegateChunkWriter.DelegateJspChunkWriter(var3);
         this.co = new ChunkOutputWrapper(var5);
      }

   }

   public void clearBuffer() throws IOException {
      this.co.clearBuffer();
   }

   public void clear() throws IOException {
      if (this.usingWriter) {
         throw new IOException("Cannot call clear() on an unbuffered Writer. ");
      } else {
         this.co.clearBuffer();
      }
   }

   public void flush() throws IOException {
      if (!this.usingWriter) {
         throw new IOException();
      } else {
         this.co.flush();
      }
   }

   public void close() throws IOException {
   }

   public int getBufferSize() {
      return this.co.getBufferSize();
   }

   public int getRemaining() {
      if (this.usingWriter) {
         return 0;
      } else {
         int var1 = this.co.getBufferSize() - this.co.getCount();
         return var1 > 0 ? var1 : 0;
      }
   }

   public boolean isAutoFlush() {
      return true;
   }

   public void newLine() throws IOException {
      this.co.print(EOL);
   }

   public void print(boolean var1) throws IOException {
      this.co.print(String.valueOf(var1));
   }

   public void print(char var1) throws IOException {
      this.co.write(var1);
   }

   public void print(char[] var1) throws IOException {
      this.co.write((char[])var1, 0, var1.length);
   }

   public void print(double var1) throws IOException {
      this.co.print(String.valueOf(var1));
   }

   public void print(float var1) throws IOException {
      this.co.print(String.valueOf(var1));
   }

   public void print(int var1) throws IOException {
      this.co.print(String.valueOf(var1));
   }

   public void print(long var1) throws IOException {
      this.co.print(String.valueOf(var1));
   }

   public void print(Object var1) throws IOException {
      if (var1 != null) {
         this.co.print(String.valueOf(var1));
      }

   }

   public void print(String var1) throws IOException {
      if (var1 != null) {
         this.co.print(var1);
      }

   }

   public void println(boolean var1) throws IOException {
      this.print(var1);
      this.newLine();
   }

   public void println(char var1) throws IOException {
      this.print(var1);
      this.newLine();
   }

   public void println(char[] var1) throws IOException {
      this.print(var1);
      this.newLine();
   }

   public void println(double var1) throws IOException {
      this.print(var1);
      this.newLine();
   }

   public void println(float var1) throws IOException {
      this.print(var1);
      this.newLine();
   }

   public void println(int var1) throws IOException {
      this.print(var1);
      this.newLine();
   }

   public void println(long var1) throws IOException {
      this.print(var1);
      this.newLine();
   }

   public void println(Object var1) throws IOException {
      if (var1 != null) {
         this.print(var1);
         this.newLine();
      }

   }

   public void println(String var1) throws IOException {
      if (var1 != null) {
         this.print(var1);
         this.newLine();
      }

   }

   public void println() throws IOException {
      this.newLine();
   }

   public void write(char[] var1) throws IOException {
      this.co.write((char[])var1, 0, var1.length);
   }

   public void write(char[] var1, int var2, int var3) throws IOException {
      this.co.write(var1, var2, var3);
   }

   public void write(int var1) throws IOException {
      this.co.write(var1);
   }

   public void write(String var1) throws IOException {
      if (var1 != null) {
         this.co.print(var1);
      }

   }

   public void write(String var1, int var2, int var3) throws IOException {
      this.co.print(var1.substring(var2, var3));
   }

   public void clearBody() {
      this.co.clearBuffer();
   }

   public Reader getReader() {
      if (this.usingWriter) {
         return null;
      } else {
         char[] var1 = this.coimpl.getCharBuffer();
         int var2 = this.coimpl.getCount();
         return (Reader)(var1 != null && var2 != 0 ? new CharArrayReader(var1, 0, var2) : new StringReader(""));
      }
   }

   public String getString() {
      if (this.usingWriter) {
         return null;
      } else {
         String var1 = "";
         char[] var2 = this.coimpl.getCharBuffer();
         int var3 = this.coimpl.getCount();
         return var2 != null && var3 != 0 ? new String(var2, 0, var3) : var1;
      }
   }

   public void writeOut(Writer var1) throws IOException {
      if (!this.usingWriter) {
         char[] var2 = this.coimpl.getCharBuffer();
         int var3 = this.coimpl.getCount();
         if (var2 == null || var3 == 0) {
            return;
         }

         var1.write(var2, 0, var3);
      }

   }

   public void write(byte[] var1, String var2) throws IOException {
      this.write((String)var2, 0, var2.length());
   }

   public void write(ByteBuffer var1, String var2) throws IOException {
      this.write((String)var2, 0, var2.length());
   }

   public void setInitCharacterEncoding(String var1, boolean var2) {
   }

   ChunkOutputWrapper getChunkOutputWrapper() {
      return this.co;
   }

   private static void p(String var0) {
      System.err.println("[BC]: " + var0);
   }
}
