package weblogic.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLEngineResult.Status;

public class JSSEFilterImpl implements MuxableSocket {
   private SSLEngine sslEngine;
   private ByteBuffer readNWDataInBuf;
   private ByteBuffer writeNWDataOutBuf;
   private ByteBuffer clearTextBuf;
   private boolean initialHSComplete;
   private SSLEngineResult.HandshakeStatus handshakeStatus;
   private Socket sock;
   private MuxableSocket delegate;
   private SocketInfo sockInfo;
   private InputStream in;
   private OutputStream out;
   private Set<HandshakeListener> handshakeCompletedListeners;
   private static final boolean BLOCKING_READ_ALLOWED = true;
   private static final boolean NO_BLOCKING_READ = false;
   private IOException cachedException;

   public JSSEFilterImpl(Socket var1, SSLEngine var2) throws IOException {
      this(var1, var2, false);
   }

   public JSSEFilterImpl(Socket var1, SSLEngine var2, boolean var3) throws IOException {
      this.handshakeCompletedListeners = new HashSet();
      this.cachedException = null;
      this.sslEngine = var2;
      this.sock = var1;
      this.in = var1.getInputStream();
      this.out = var1.getOutputStream();
      this.sslEngine.setUseClientMode(var3);
      this.readNWDataInBuf = ByteBuffer.allocate(this.sslEngine.getSession().getPacketBufferSize() + 50);
      this.writeNWDataOutBuf = ByteBuffer.allocate(this.sslEngine.getSession().getPacketBufferSize() + 50);
      this.clearTextBuf = ByteBuffer.allocate(this.sslEngine.getSession().getApplicationBufferSize() + 50);
      if (var3) {
         this.handshakeStatus = HandshakeStatus.NEED_WRAP;
      } else {
         this.handshakeStatus = HandshakeStatus.NEED_UNWRAP;
      }

   }

   public synchronized boolean doHandshake() throws IOException {
      this.initialHSComplete = false;
      this.handshakeStatus = HandshakeStatus.NEED_WRAP;
      return this.doHandshake(this, true);
   }

   private boolean doHandshake(MuxableSocket var1, boolean var2) throws IOException {
      return this.doHandshake((ByteBuffer)null, var1, var2);
   }

   private boolean doHandshake(ByteBuffer var1, MuxableSocket var2, boolean var3) throws IOException {
      SSLEngineResult var4 = null;
      if (this.initialHSComplete) {
         return this.initialHSComplete;
      } else {
         while(this.handshakeStatus != HandshakeStatus.FINISHED && this.handshakeStatus != HandshakeStatus.NOT_HANDSHAKING) {
            switch (this.handshakeStatus) {
               case NEED_UNWRAP:
                  var4 = this.unwrapAndHandleResults(var1, var2, var3);
                  this.handshakeStatus = var4.getHandshakeStatus();
                  if (!var3 && var4.getStatus() == Status.BUFFER_UNDERFLOW) {
                     return false;
                  }
                  break;
               case NEED_WRAP:
                  var4 = this.wrapAndWrite(ByteBuffer.allocate(0), var3);
                  this.handshakeStatus = var4.getHandshakeStatus();
                  break;
               case NEED_TASK:
                  this.handshakeStatus = this.doTasks();
                  break;
               default:
                  throw new RuntimeException("Invalid Handshaking State" + this.handshakeStatus);
            }

            if (var4.getStatus() == Status.CLOSED) {
               return false;
            }
         }

         this.initialHSComplete = true;
         this.notifyHandshakeComplete();
         return this.initialHSComplete;
      }
   }

   private void notifyHandshakeComplete() {
      if (!this.handshakeCompletedListeners.isEmpty()) {
         Iterator var1 = this.handshakeCompletedListeners.iterator();

         while(var1.hasNext()) {
            HandshakeListener var2 = (HandshakeListener)var1.next();
            var2.handshakeDone(this.sslEngine.getSession());
         }

      }
   }

   public ByteBuffer getClearTextBuf() {
      return this.clearTextBuf;
   }

   public void addHandshakeCompletedListener(HandshakeListener var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Non-null HandshakeCompletedListener expected.");
      } else {
         this.handshakeCompletedListeners.add(var1);
         if (null == this.delegate || null == this.delegate.getSocket()) {
            SocketLogger.logDebug("No SSLSocket when adding HandshakeCompletedListener: class=" + var1.getClass().getName() + ", instance=" + var1 + ", on " + this + ". An associated SSLSocket is required.");
         }

         SocketLogger.logDebug("Added HandshakeCompletedListener: class=" + var1.getClass().getName() + ", instance=" + var1 + ", on " + this + " .");
      }
   }

   public void removeHandshakeCompletedListener(HandshakeCompletedListener var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Non-null HandshakeCompletedListener expected.");
      } else if (!this.handshakeCompletedListeners.remove(var1)) {
         String var2 = SocketLogger.logUnregisteredHandshakeCompletedListener(var1.getClass().getName(), var1.toString());
         throw new IllegalArgumentException(var2);
      } else {
         SocketLogger.logDebug("Removed HandshakeCompletedListener: class=" + var1.getClass().getName() + ", instance=" + var1 + ".");
      }
   }

   private SSLEngineResult.HandshakeStatus doTasks() {
      Runnable var1;
      while((var1 = this.sslEngine.getDelegatedTask()) != null) {
         var1.run();
      }

      return this.sslEngine.getHandshakeStatus();
   }

   public boolean closeSocketOnError() {
      return true;
   }

   public void dispatch() {
      this.delegate.dispatch();
   }

   public void endOfStream() {
      this.delegate.endOfStream();
   }

   public byte[] getBuffer() {
      return this.readNWDataInBuf.array();
   }

   public int getBufferOffset() {
      return this.readNWDataInBuf.position();
   }

   public int getCompleteMessageTimeoutMillis() {
      return this.delegate.getCompleteMessageTimeoutMillis();
   }

   public int getIdleTimeoutMillis() {
      return this.delegate.getIdleTimeoutMillis();
   }

   public Socket getSocket() {
      return this.sock;
   }

   public MuxableSocket getSocketFilter() {
      return this;
   }

   public SocketInfo getSocketInfo() {
      return this.sockInfo;
   }

   public InputStream getSocketInputStream() {
      return this.in;
   }

   public void hasException(Throwable var1) {
      this.delegate.hasException(var1);
   }

   public void incrementBufferOffset(int var1) throws MaxMessageSizeExceededException {
      this.readNWDataInBuf.position(this.readNWDataInBuf.position() + var1);
   }

   public int getAvailableBytes() {
      return this.getClearTextBuf().position();
   }

   private boolean isRehandshakeNeeded(SSLEngineResult var1) throws IOException {
      return this.initialHSComplete && var1.getStatus() == Status.OK && var1.getHandshakeStatus() != HandshakeStatus.NOT_HANDSHAKING && var1.getHandshakeStatus() != HandshakeStatus.FINISHED;
   }

   public boolean isMessageComplete() {
      try {
         synchronized(this) {
            if (this.getAvailableBytes() > 0) {
               ByteBuffer[] var2 = this.delegate.getAvailableBufferofSize(this.getAvailableBytes());
               int var3 = this.fillAppBuf(var2);
               this.delegate.incrementBufferOffset(var3);
            }
         }

         if (!this.initialHSComplete && !this.doHandshake(this.delegate, false)) {
            return false;
         } else {
            SSLEngineResult var1;
            do {
               var1 = this.unwrapAndHandleResults(this.delegate, false);
               if (var1.bytesProduced() > 0) {
                  this.delegate.incrementBufferOffset(var1.bytesProduced());
               }
            } while(this.readNWDataInBuf.position() > 0 && var1.getStatus() == Status.OK);

            return !this.initialHSComplete ? false : this.delegate.isMessageComplete();
         }
      } catch (Exception var6) {
         SocketLogger.logDebug("Caught sslException: " + var6 + " returning false for isMessageComplete");
         this.delegate.hasException(var6);
         return false;
      }
   }

   public boolean requestTimeout() {
      return this.delegate.requestTimeout();
   }

   public void setSoTimeout(int var1) throws SocketException {
      this.delegate.setSoTimeout(var1);
   }

   public void setSocketFilter(MuxableSocket var1) {
      throw new UnsupportedOperationException("Re-register Muxer not allowed on JSSEFilterImpl");
   }

   public void setSocketInfo(SocketInfo var1) {
      this.sockInfo = var1;
   }

   public boolean timeout() {
      return this.delegate.timeout();
   }

   public MuxableSocket getDelegate() {
      return this.delegate;
   }

   public void setDelegate(MuxableSocket var1) {
      this.delegate = var1;
   }

   public synchronized int read(byte[] var1, int var2, int var3) throws IOException {
      if (this.sslEngine.isInboundDone()) {
         this.checkCauseSSLEngineClosed();
      }

      if (var3 == 0) {
         return 0;
      } else {
         ByteBuffer var4 = ByteBuffer.wrap(var1);
         var4.position(var2);
         var4.limit(var2 + var3);
         if (this.getAvailableBytes() > 0) {
            return this.fillAppBuf(new ByteBuffer[]{var4});
         } else {
            if (this.getAvailableBytes(this.readNWDataInBuf) == 0) {
               int var5 = this.readFromNetwork();
               if (var5 <= -1) {
                  return var5;
               }
            }

            this.unwrapAndHandleResults(var4, this, true);
            return var4.position() - var2;
         }
      }
   }

   private void checkCauseSSLEngineClosed() throws IOException {
      if (this.cachedException != null) {
         throw this.cachedException;
      } else {
         throw new IOException("SSLEngine is closed");
      }
   }

   private int getAvailableBytes(ByteBuffer var1) {
      return var1.position();
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      if (this.sslEngine.isOutboundDone()) {
         this.checkCauseSSLEngineClosed();
      }

      if (!this.initialHSComplete) {
         this.doHandshake();
      }

      ByteBuffer var4 = ByteBuffer.wrap(var1);
      var4.position(var2);
      var4.limit(var2 + var3);

      while(var4.position() < var4.limit()) {
         this.wrapAndWrite(var4, true);
      }

   }

   private int readFromNetwork() throws IOException {
      int var1 = this.in.read(this.readNWDataInBuf.array(), this.readNWDataInBuf.position(), this.readNWDataInBuf.remaining());
      if (var1 > 0) {
         this.incrementBufferOffset(var1);
      }

      return var1;
   }

   private SSLEngineResult unwrapAndHandleResults(MuxableSocket var1, boolean var2) throws IOException {
      return this.unwrapAndHandleResults((ByteBuffer)null, var1, var2);
   }

   private SSLEngineResult unwrapAndHandleResults(ByteBuffer var1, MuxableSocket var2, boolean var3) throws IOException {
      int var4 = this.sslEngine.getSession().getApplicationBufferSize();
      int var5 = var4;
      ByteBuffer[] var7 = this.createBuffers(var1, var2, var4);

      SSLEngineResult var6;
      for(var6 = this.unwrap(var7); var6.getStatus() == Status.BUFFER_OVERFLOW; var6 = this.unwrap(var7)) {
         var4 += var5;
         var7 = this.createBuffers(var1, var2, var4);
      }

      this.handleUnwrapResults(var6, var1, var2, var3);
      return var6;
   }

   private ByteBuffer[] createBuffers(ByteBuffer var1, MuxableSocket var2, int var3) throws IOException {
      ByteBuffer[] var4;
      if (var1 != null) {
         var4 = this.getBufferArray(var1, var3);
      } else {
         var4 = var2.getAvailableBufferofSize(var3);
      }

      return var4;
   }

   public ByteBuffer[] getAvailableBufferofSize(int var1) {
      return this.getBufferArray((ByteBuffer)null, var1);
   }

   private ByteBuffer growClearTextBuf(int var1) {
      ByteBuffer var2 = ByteBuffer.allocate(var1);
      if (this.getClearTextBuf().position() == 0) {
         this.clearTextBuf = var2;
      } else {
         this.clearTextBuf = var2.put(this.getClearTextBuf());
      }

      return this.clearTextBuf;
   }

   private ByteBuffer[] getBufferArray(ByteBuffer var1, int var2) {
      ByteBuffer var3 = this.getClearTextBuf();
      if (var3.remaining() < var2) {
         var3 = this.growClearTextBuf(var2);
      }

      return var1 != null ? new ByteBuffer[]{var1, var3} : new ByteBuffer[]{var3};
   }

   private SSLEngineResult unwrap(ByteBuffer[] var1) throws IOException {
      this.readNWDataInBuf.flip();

      SSLEngineResult var2;
      try {
         var2 = this.sslEngine.unwrap(this.readNWDataInBuf, var1);
      } catch (SSLException var8) {
         this.cachedException = var8;
         this.cleanupSSLEngine();
         throw var8;
      } finally {
         this.readNWDataInBuf.compact();
      }

      return var2;
   }

   private void handleUnwrapResults(SSLEngineResult var1, ByteBuffer var2, MuxableSocket var3, boolean var4) throws IOException {
      if (var1.getStatus() == Status.BUFFER_UNDERFLOW) {
         if (var4) {
            int var5 = this.readFromNetwork();
            if (var5 <= -1) {
               SocketLogger.logDebug("read EOF on socket");
               this.cachedException = new IOException("Connection closed, EOF detected");
               if (!this.sslEngine.isInboundDone()) {
                  this.cleanupSSLEngine();
               }

               throw this.cachedException;
            }
         }
      } else {
         this.handleResultsCommonly(var1, var2, var3, var4);
      }

   }

   private void cleanupSSLEngine() throws IOException {
      this.sslEngine.closeOutbound();
      this.writeToNetwork();
      this.writeNWDataOutBuf.clear();
      SSLEngineResult var1 = null;

      while(!this.sslEngine.isOutboundDone()) {
         try {
            var1 = this.sslEngine.wrap(ByteBuffer.allocate(0), this.writeNWDataOutBuf);
            if (var1.getStatus() == Status.BUFFER_OVERFLOW) {
               this.writeToNetwork();
            }
         } catch (SSLException var3) {
            return;
         }
      }

      if (var1 == null || var1.getStatus() == Status.OK || var1.getStatus() == Status.CLOSED) {
         this.writeToNetwork();
      }

   }

   private int fillAppBuf(ByteBuffer[] var1) {
      int var2 = 0;
      ByteBuffer var3 = this.getClearTextBuf();
      var3.flip();

      try {
         for(int var4 = 0; var4 < var1.length; ++var4) {
            if (var1[var4].remaining() != 0) {
               int var5 = var3.remaining();

               while(var3.remaining() > 0 && var1[var4].remaining() > 0) {
                  var1[var4].put(var3.get());
               }

               var2 += var5 - var3.remaining();
               if (var3.remaining() == 0) {
                  break;
               }
            }
         }
      } finally {
         var3.compact();
      }

      return var2;
   }

   private SSLEngineResult wrapAndWrite(ByteBuffer var1, boolean var2) throws IOException {
      SSLEngineResult var3;
      try {
         var3 = this.sslEngine.wrap(var1, this.writeNWDataOutBuf);
      } catch (SSLException var5) {
         this.cachedException = var5;
         this.cleanupSSLEngine();
         throw var5;
      }

      if (var3.getStatus() == Status.BUFFER_UNDERFLOW) {
         throw new IOException("SSLException not enough data in the buffer: " + var1 + " to encrypt!?! Results: " + var3);
      } else if (var3.getStatus() == Status.BUFFER_OVERFLOW && this.getAvailableBytes(this.writeNWDataOutBuf) == 0) {
         throw new IOException("SSLException, writeNWBuf: " + this.writeNWDataOutBuf + " is not large enough for SSLEngine.unwrapAndHandleResults, results: " + var3);
      } else {
         this.writeToNetwork();
         this.handleResultsCommonly(var3, (ByteBuffer)null, this, var2);
         return var3;
      }
   }

   private void handleResultsCommonly(SSLEngineResult var1, ByteBuffer var2, MuxableSocket var3, boolean var4) throws IOException {
      if (var1.getStatus() == Status.CLOSED) {
         this.cleanupSSLEngine();
      } else if (var1.getStatus() == Status.OK && this.isRehandshakeNeeded(var1)) {
         this.initialHSComplete = false;
         this.handshakeStatus = var1.getHandshakeStatus();
         if (!this.doHandshake(var2, var3, var4) && var4) {
            throw new IOException("Requested re-handshake failed: " + var1);
         }
      }

   }

   private void writeToNetwork() throws IOException {
      this.writeNWDataOutBuf.flip();

      try {
         if (this.writeNWDataOutBuf.hasRemaining()) {
            this.out.write(this.writeNWDataOutBuf.array(), this.writeNWDataOutBuf.arrayOffset(), this.writeNWDataOutBuf.limit());
         }
      } finally {
         this.writeNWDataOutBuf.clear();
      }

   }

   public SSLEngine getSSLEngine() {
      return this.sslEngine;
   }

   public long read(NIOConnection var1) throws IOException {
      throw new UnsupportedOperationException();
   }

   public boolean supportsScatteredRead() {
      return false;
   }

   public String toString() {
      return super.toString() + " with delegate: " + this.delegate;
   }

   public interface HandshakeListener {
      void handshakeDone(SSLSession var1);
   }
}
