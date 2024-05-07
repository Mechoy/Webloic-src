package weblogic.security.utils;

import java.io.IOException;
import java.io.InputStream;
import weblogic.security.shared.LoggerWrapper;

public class ContextElementMessageStream extends InputStream implements weblogic.security.service.ContextElementMessageStream {
   private static LoggerWrapper LOGGER = LoggerWrapper.getInstance("SecurityAuditor");
   private boolean resetCalled = false;
   private int readLimit = Integer.MAX_VALUE;
   private InputStream realStream = null;

   private ContextElementMessageStream() {
   }

   public ContextElementMessageStream(InputStream var1, int var2) {
      this.realStream = var1;
      this.readLimit = var2;
   }

   public void resetToStart() {
      if (this.resetCalled) {
         try {
            this.realStream.reset();
         } catch (IOException var2) {
            LOGGER.debug("Failure while resetting ContextElementMessageStream", var2);
         }

         this.resetCalled = true;
      }

      this.realStream.mark(this.readLimit);
   }

   public int available() throws IOException {
      return this.realStream.available();
   }

   public void close() throws IOException {
   }

   public void mark(int var1) {
   }

   public boolean markSupported() {
      return false;
   }

   public int read() throws IOException {
      return this.realStream.read();
   }

   public int read(byte[] var1) throws IOException {
      return this.realStream.read(var1);
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      return this.realStream.read(var1, var2, var3);
   }

   public void reset() throws IOException {
      throw new IOException("Reset not supported on InputStream");
   }

   public long skip(long var1) throws IOException {
      return this.realStream.skip(var1);
   }
}
