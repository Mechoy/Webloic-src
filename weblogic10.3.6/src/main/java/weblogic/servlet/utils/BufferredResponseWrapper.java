package weblogic.servlet.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class BufferredResponseWrapper extends HttpServletResponseWrapper {
   private ServletOutputStream sos;
   private BufferredOutputStream bos = new BufferredOutputStream();
   private PrintWriter writer;
   private int status = 200;
   private boolean verbose = false;

   public BufferredResponseWrapper(HttpServletResponse var1) throws IOException {
      super(var1);
   }

   public int getStatus() {
      return this.status;
   }

   public byte[] getContent() throws IOException {
      this.bos.flush();
      return this.bos.getContent();
   }

   public boolean getVerbose() {
      return this.verbose;
   }

   public void setVerbose(boolean var1) {
      this.verbose = var1;
   }

   public void setBufferSize(int var1) {
   }

   public void setContentType(String var1) {
   }

   public void setContentLength(int var1) {
   }

   public void addCookie(Cookie var1) {
   }

   public void addDateHeader(String var1, long var2) {
   }

   public void addHeader(String var1, String var2) {
   }

   public void addIntHeader(String var1, int var2) {
   }

   public void sendRedirect(String var1) {
   }

   public void setDateHeader(String var1, long var2) {
   }

   public void setHeader(String var1, String var2) {
   }

   public void setIntHeader(String var1, int var2) {
   }

   public void sendError(int var1) {
      this.status = var1;
   }

   public void sendError(int var1, String var2) {
      this.status = var1;
   }

   public void setStatus(int var1) {
      this.status = var1;
   }

   public void setStatus(int var1, String var2) {
      this.status = var1;
   }

   public boolean isCommitted() {
      return false;
   }

   public ServletOutputStream getOutputStream() throws IOException {
      if (this.writer != null) {
         throw new IllegalStateException("Cannot get Writer then OutputStream");
      } else {
         this.sos = this.bos;
         return this.sos;
      }
   }

   public PrintWriter getWriter() throws IOException {
      if (this.sos != null) {
         throw new IllegalStateException("Cannot get OutputStream then Writer");
      } else {
         if (this.writer == null) {
            this.writer = new PrintWriter(new OutputStreamWriter(this.bos, this.getCharacterEncoding()));
         }

         return this.writer;
      }
   }

   public void flushBuffer() {
      try {
         if (this.writer != null) {
            this.writer.flush();
         }

         if (this.sos != null) {
            this.sos.flush();
         }
      } catch (IOException var2) {
         if (this.verbose) {
            var2.printStackTrace();
         }
      }

   }

   public void reset() {
      this.status = 200;
      this.resetBuffer();
   }

   public void resetBuffer() {
      this.bos.reset();
      if (this.writer != null) {
         try {
            this.writer = new PrintWriter(new OutputStreamWriter(this.bos, this.getCharacterEncoding()));
         } catch (UnsupportedEncodingException var2) {
         }
      } else if (this.sos != null) {
         this.sos = this.bos;
      }

   }

   public static class BufferredOutputStream extends ServletOutputStream {
      private ByteArrayOutputStream baos = new ByteArrayOutputStream();

      public byte[] getContent() {
         return this.baos.toByteArray();
      }

      public void write(int var1) throws IOException {
         this.baos.write(var1);
      }

      public void write(byte[] var1, int var2, int var3) throws IOException {
         this.baos.write(var1, var2, var3);
      }

      public void reset() {
         this.baos.reset();
      }
   }
}
