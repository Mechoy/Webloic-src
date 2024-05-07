package weblogic.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import javax.net.ssl.SSLSocket;
import weblogic.security.utils.SSLSetupLogging;
import weblogic.utils.AssertionError;
import weblogic.utils.io.Chunk;
import weblogic.utils.io.ChunkedInputStream;

public final class SSLFilterImpl implements SSLFilter {
   private static final boolean ASSERT = true;
   private static final boolean DEBUG = false;
   private boolean activated = false;
   private final InputStream in;
   private Chunk head;
   private Chunk tail;
   private int pos;
   private int availBytes;
   private MuxableSocket delegate;
   private final SSLSocket sslSocket;
   private InputStream clearText;
   private SocketInfo sockInfo;
   private ChunkedInputStream cis = null;

   public SSLFilterImpl(InputStream var1, SSLSocket var2) throws IOException {
      this.in = var1;
      this.sslSocket = var2;
      this.head = this.tail = Chunk.getChunk();
      SSLSetupLogging.info("Filtering JSSE SSLSocket");
   }

   public String toString() {
      return "SSLFilterImpl[" + this.getDelegate() + "]";
   }

   public MuxableSocket getDelegate() {
      return this.delegate;
   }

   public void setDelegate(MuxableSocket var1) {
      this.delegate = var1;
   }

   public void asyncOn() {
      this.activated = true;
   }

   public void asyncOff() {
      this.activated = false;
   }

   public boolean isActivated() {
      return this.activated;
   }

   public ChunkedInputStream getInputStream() throws IOException {
      if (this.cis == null) {
         synchronized(this) {
            if (this.cis == null) {
               this.cis = new ChunkedInputStream(this.head, 0, this.in);
            }

            synchronized(this.cis) {
               ;
            }
         }
      }

      return this.cis;
   }

   public int available() throws IOException {
      return !this.activated ? this.in.available() : this.availBytes;
   }

   public void activate() throws IOException {
      SSLSetupLogging.info("SSLFilteriImpl.activate()");
      this.activateNoRegister();
      if (this.delegate.isMessageComplete()) {
         SocketMuxer.getMuxer().register(this);
         this.delegate.dispatch();
      } else {
         SocketMuxer.getMuxer().register(this);
         SocketMuxer.getMuxer().read(this);
      }

   }

   public void activateNoRegister() throws IOException {
      SSLSetupLogging.info("activateNoRegister()");
      this.clearText = this.sslSocket.getInputStream();
      int var1 = 0;

      try {
         boolean var2 = false;

         while(var1 > 0) {
            SSLSetupLogging.info("clearTextAvail = " + var1);
            byte[] var3 = this.delegate.getBuffer();
            int var4 = this.delegate.getBufferOffset();
            int var6 = this.clearText.read(var3, var4, Math.min(var3.length - var4, var1));
            var1 -= var6;
            this.delegate.incrementBufferOffset(var6);
         }
      } catch (InterruptedIOException var5) {
      }

      this.activated = true;
      SSLSetupLogging.info("SSLFilterImpl.activate(): activated: " + this.in.hashCode() + " " + this.clearText.hashCode());
   }

   public int getIdleTimeoutMillis() {
      return this.delegate.getIdleTimeoutMillis();
   }

   public byte[] getBuffer() {
      if (this.tail.end == this.tail.buf.length) {
         this.tail.next = Chunk.getChunk();
         this.tail = this.tail.next;
      }

      return this.tail.buf;
   }

   public int getBufferOffset() {
      return this.tail.end;
   }

   private static void p(String var0) {
      System.out.println("SSLFilterImpl: " + var0);
   }

   public void incrementBufferOffset(int var1) throws MaxMessageSizeExceededException {
      this.availBytes += var1;
      Chunk var10000 = this.tail;
      var10000.end += var1;
   }

   public boolean isMessageComplete() {
      while(true) {
         byte[] var1 = this.delegate.getBuffer();
         int var2 = this.delegate.getBufferOffset();

         try {
            int var3 = this.clearText.read(var1, var2, var1.length - var2);
            if (var3 != -1) {
               this.delegate.incrementBufferOffset(var3);
               continue;
            }

            try {
               SocketMuxer.getMuxer().deliverEndOfStream(this);
            } catch (Throwable var5) {
               if (!(var5 instanceof SocketException)) {
                  SocketLogger.logDebugException("isMessageComplete", (Exception)var5);
               }
            }
         } catch (InterruptedIOException var6) {
         } catch (MaxMessageSizeExceededException var7) {
            SocketMuxer.getMuxer().deliverHasException(this, var7);
         } catch (IOException var8) {
            if (!(var8 instanceof SocketException)) {
               SocketLogger.logDebugException("isMessageComplete", var8);
            }

            SocketMuxer.getMuxer().deliverHasException(this, var8);
            continue;
         } catch (Throwable var9) {
            SocketLogger.logDebugException("isMessageComplete", (Exception)var9);
            SocketMuxer.getMuxer().deliverHasException(this, var9);
            continue;
         }

         boolean var10 = this.delegate.isMessageComplete();
         return var10;
      }
   }

   public void dispatch() {
      this.delegate.dispatch();
   }

   public InputStream getSocketInputStream() {
      return this.in;
   }

   public void setSoTimeout(int var1) throws SocketException {
      this.delegate.setSoTimeout(var1);
   }

   public Socket getSocket() {
      return this.sslSocket;
   }

   public boolean closeSocketOnError() {
      return true;
   }

   public void hasException(Throwable var1) {
      if (!(var1 instanceof SocketException)) {
         SSLSetupLogging.debug(3, var1, "hasException");
      }

      this.delegate.hasException(var1);
   }

   public void endOfStream() {
      this.delegate.endOfStream();
   }

   public boolean timeout() {
      return this.delegate.timeout();
   }

   public boolean requestTimeout() {
      return this.delegate.requestTimeout();
   }

   public int getCompleteMessageTimeoutMillis() {
      return this.delegate.getCompleteMessageTimeoutMillis();
   }

   public void setSocketFilter(MuxableSocket var1) {
      throw new AssertionError("Re-register Muxer not allowed on SSLFilterImpl");
   }

   public MuxableSocket getSocketFilter() {
      return this;
   }

   public void setSocketInfo(SocketInfo var1) {
      this.sockInfo = var1;
   }

   public SocketInfo getSocketInfo() {
      return this.sockInfo;
   }

   public void ensureForceClose() {
      try {
         this.sslSocket.getOutputStream().close();
      } catch (IOException var2) {
      }

   }

   public boolean supportsScatteredRead() {
      return false;
   }

   public long read(NIOConnection var1) throws IOException {
      throw new UnsupportedOperationException();
   }

   public ByteBuffer[] getAvailableBufferofSize(int var1) {
      throw new UnsupportedOperationException();
   }
}
