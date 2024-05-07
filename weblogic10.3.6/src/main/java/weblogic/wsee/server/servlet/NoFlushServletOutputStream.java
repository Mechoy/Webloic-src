package weblogic.wsee.server.servlet;

import java.io.IOException;
import javax.servlet.ServletOutputStream;

class NoFlushServletOutputStream extends ServletOutputStream {
   private final ServletOutputStream servletOutputStream;

   NoFlushServletOutputStream(ServletOutputStream var1) {
      this.servletOutputStream = var1;
   }

   public void write(int var1) throws IOException {
      this.servletOutputStream.write(var1);
   }

   public void write(byte[] var1) throws IOException {
      this.servletOutputStream.write(var1);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      this.servletOutputStream.write(var1, var2, var3);
   }

   public void flush() throws IOException {
   }

   public void close() throws IOException {
      this.servletOutputStream.close();
   }

   public void print(String var1) throws IOException {
      this.servletOutputStream.print(var1);
   }

   public void print(boolean var1) throws IOException {
      this.servletOutputStream.print(var1);
   }

   public void print(char var1) throws IOException {
      this.servletOutputStream.print(var1);
   }

   public void print(int var1) throws IOException {
      this.servletOutputStream.print(var1);
   }

   public void print(long var1) throws IOException {
      this.servletOutputStream.print(var1);
   }

   public void print(float var1) throws IOException {
      this.servletOutputStream.print(var1);
   }

   public void print(double var1) throws IOException {
      this.servletOutputStream.print(var1);
   }

   public void println() throws IOException {
      this.servletOutputStream.println();
   }

   public void println(String var1) throws IOException {
      this.servletOutputStream.println(var1);
   }

   public void println(boolean var1) throws IOException {
      this.servletOutputStream.println(var1);
   }

   public void println(char var1) throws IOException {
      this.servletOutputStream.println(var1);
   }

   public void println(int var1) throws IOException {
      this.servletOutputStream.println(var1);
   }

   public void println(long var1) throws IOException {
      this.servletOutputStream.println(var1);
   }

   public void println(float var1) throws IOException {
      this.servletOutputStream.println(var1);
   }

   public void println(double var1) throws IOException {
      this.servletOutputStream.println(var1);
   }
}
