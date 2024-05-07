package weblogic.wsee.jaxws;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import weblogic.servlet.http.RequestResponseKey;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.util.VerboseInputStream;
import weblogic.wsee.util.VerboseOutputStream;

class VerboseHttpProcessor implements HTTPProcessor {
   private HTTPProcessor delegate = null;

   public VerboseHttpProcessor(HTTPProcessor var1) {
      this.delegate = var1;
   }

   public boolean get(RequestResponseKey var1, boolean var2) throws ServletException {
      Verbose.log((Object)"*** JAXWS get start ***");
      boolean var3 = this.delegate.get(var1, true);
      Verbose.log((Object)"*** JAXWS get finish ***");
      Verbose.say("");
      return var3;
   }

   public boolean post(RequestResponseKey var1, boolean var2) throws ServletException {
      Verbose.log((Object)"*** JAXWS post start ***");
      boolean var3 = this.delegate.post(var1, true);
      Verbose.log((Object)"*** JAXWS post finish ***");
      Verbose.say("");
      return var3;
   }

   public static class VerboseServletOutputStream extends ServletOutputStream {
      private OutputStream delegate = null;

      public VerboseServletOutputStream(OutputStream var1) {
         this.delegate = new VerboseOutputStream(var1);
      }

      public void write(int var1) throws IOException {
         this.delegate.write(var1);
      }

      public void write(byte[] var1) throws IOException {
         this.delegate.write(var1);
      }

      public void write(byte[] var1, int var2, int var3) throws IOException {
         this.delegate.write(var1, var2, var3);
      }

      public void flush() throws IOException {
         this.delegate.flush();
      }

      public void close() throws IOException {
         this.delegate.close();
      }
   }

   static class VerboseHttpServletResponse extends HttpServletResponseWrapper {
      public VerboseHttpServletResponse(HttpServletResponse var1) {
         super(var1);
      }

      public ServletOutputStream getOutputStream() throws IOException {
         return new VerboseServletOutputStream(super.getOutputStream());
      }
   }

   private static class VerboseSerlvetInputStream extends ServletInputStream {
      private InputStream delegate = null;

      public VerboseSerlvetInputStream(InputStream var1) {
         this.delegate = new VerboseInputStream(var1);
      }

      public int read() throws IOException {
         return this.delegate.read();
      }

      public int readLine(byte[] var1, int var2, int var3) throws IOException {
         return this.delegate.read(var1, var2, var3);
      }

      public int read(byte[] var1) throws IOException {
         return this.delegate.read(var1);
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         return this.delegate.read(var1, var2, var3);
      }

      public long skip(long var1) throws IOException {
         return this.delegate.skip(var1);
      }

      public int available() throws IOException {
         return this.delegate.available();
      }

      public void close() throws IOException {
         this.delegate.close();
      }

      public synchronized void mark(int var1) {
         this.delegate.mark(var1);
      }

      public synchronized void reset() throws IOException {
         this.delegate.reset();
      }

      public boolean markSupported() {
         return this.delegate.markSupported();
      }
   }

   static class VerboseHttpServletRequest extends HttpServletRequestWrapper {
      public VerboseHttpServletRequest(HttpServletRequest var1) {
         super(var1);
      }

      public ServletInputStream getInputStream() throws IOException {
         return new VerboseSerlvetInputStream(super.getInputStream());
      }
   }
}
