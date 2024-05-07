package weblogic.security.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.net.ssl.SSLSocket;
import weblogic.socket.SSLFilter;
import weblogic.socket.SSLFilterImpl;
import weblogic.utils.io.ChunkedInputStream;

public final class SSLIOContext {
   private static final boolean DEBUG = SSLSetupLogging.isDebugEnabled(3);
   private SSLSocket sslSocket;
   private SSLFilter muxerFilter;
   private ChunkedInputStream muxerIS;
   private OutputStream outputStream;
   private InputStream rawInputStream;

   public SSLIOContext(InputStream var1, OutputStream var2, SSLSocket var3) throws IOException {
      this.rawInputStream = var1;
      this.outputStream = var2;
      this.sslSocket = var3;
      this.muxerFilter = new SSLFilterImpl(var1, var3);
      this.muxerIS = this.muxerFilter.getInputStream();
   }

   public SSLIOContext(InputStream var1, OutputStream var2, SSLSocket var3, SSLFilter var4) throws IOException {
      this.rawInputStream = var1;
      this.outputStream = var2;
      this.sslSocket = var3;
      this.muxerFilter = var4;
      this.muxerIS = this.muxerFilter.getInputStream();
   }

   public boolean isMuxerActivated() {
      if (DEBUG) {
         SSLSetupLogging.info("isMuxerActivated: " + this.muxerFilter.isActivated());
      }

      return this.muxerFilter.isActivated();
   }

   public synchronized boolean hasSSLRecord() {
      if (DEBUG) {
         SSLSetupLogging.info("hasSSLRecord()");
      }

      int var1 = this.muxerIS.peek(3);
      int var2 = this.muxerIS.peek(4);
      if (var1 != -1 && var2 != -1) {
         int var3 = (var1 & 255) << 8 | var2 & 255;
         boolean var4 = var3 + 5 <= this.muxerIS.available();
         if (DEBUG) {
            SSLSetupLogging.info("hasSSLRecord returns " + var4);
         }

         return var4;
      } else {
         if (DEBUG) {
            SSLSetupLogging.info("hasSSLRecord returns false 1");
         }

         return false;
      }
   }

   public InputStream getMuxerIS() {
      return this.muxerIS;
   }

   public SSLSocket getSSLSocket() {
      return this.sslSocket;
   }

   public Object getFilter() {
      return this.muxerFilter;
   }

   public InputStream getRawInputStream() {
      return this.rawInputStream;
   }
}
