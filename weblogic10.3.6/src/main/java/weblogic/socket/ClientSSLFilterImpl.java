package weblogic.socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import javax.net.ssl.SSLSocket;
import weblogic.security.utils.SSLSetupLogging;
import weblogic.utils.AssertionError;
import weblogic.utils.io.ChunkedInputStream;

public final class ClientSSLFilterImpl implements SSLFilter {
   private static final boolean ASSERT = true;
   private static final boolean DEBUG = false;
   private boolean activated = false;
   private final InputStream in;
   private int pos;
   private int availBytes;
   private MuxableSocket delegate;
   private final SSLSocket sslSocket;
   private SocketInfo sockInfo;

   public ClientSSLFilterImpl(InputStream var1, SSLSocket var2) throws IOException {
      this.in = var1;
      this.sslSocket = var2;
      SSLSetupLogging.info("Filtering JSSE SSLSocket");
   }

   public String toString() {
      return "ClientSSLFilterImpl[" + this.getDelegate() + "]";
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

   public int available() throws IOException {
      return !this.activated ? this.in.available() : this.availBytes;
   }

   public void activate() throws IOException {
      SSLSetupLogging.info("ClientSSLFilterImpl.activate()");
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
      this.activated = true;
      SSLSetupLogging.info("ClientSSLFilterImpl.activate(): activated: " + this.in.hashCode());
   }

   public int getIdleTimeoutMillis() {
      return this.delegate.getIdleTimeoutMillis();
   }

   public byte[] getBuffer() {
      return this.delegate.getBuffer();
   }

   public int getBufferOffset() {
      return this.delegate.getBufferOffset();
   }

   private static void p(String var0) {
      System.out.println("ClientSSLFilterImpl: " + var0);
   }

   public void incrementBufferOffset(int var1) throws MaxMessageSizeExceededException {
      this.delegate.incrementBufferOffset(var1);
   }

   public boolean isMessageComplete() {
      return this.delegate.isMessageComplete();
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
      throw new AssertionError("Re-register Muxer not allowed on ClientSSLFilterImpl");
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

   public ChunkedInputStream getInputStream() {
      return null;
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
