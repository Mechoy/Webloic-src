package weblogic.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import javax.net.SocketFactory;
import weblogic.management.runtime.SocketRuntime;
import weblogic.protocol.Protocol;
import weblogic.protocol.ServerChannel;
import weblogic.security.service.ContextElement;
import weblogic.security.service.ContextHandler;
import weblogic.server.channels.SocketRuntimeImpl;
import weblogic.utils.Debug;
import weblogic.utils.concurrent.Latch;
import weblogic.utils.io.Chunk;

public abstract class BaseAbstractMuxableSocket implements MuxableSocket, SocketRuntime, ContextHandler, Serializable {
   private static final long serialVersionUID = 7960171920400419300L;
   private static final int DEFAULT_CONNECTION_TIMEOUT = 0;
   private static final boolean DEBUG = false;
   protected final ServerChannel channel;
   protected Socket socket;
   protected int soTimeout;
   protected InputStream sis;
   protected OutputStream sos;
   protected SocketInfo info;
   protected MuxableSocket filter;
   protected SocketFactory socketFactory;
   protected final Latch closeLatch;
   protected final int maxMessageSize;
   protected Chunk head;
   protected Chunk tail;
   protected int availBytes;
   protected int msgLength;
   private final long connectTime;
   private long messagesReceived;
   private long bytesReceived;
   private static final String[] KEYS = new String[]{"com.bea.contextelement.channel.Port", "com.bea.contextelement.channel.PublicPort", "com.bea.contextelement.channel.RemotePort", "com.bea.contextelement.channel.Protocol", "com.bea.contextelement.channel.Address", "com.bea.contextelement.channel.PublicAddress", "com.bea.contextelement.channel.RemoteAddress", "com.bea.contextelement.channel.ChannelName", "com.bea.contextelement.channel.Secure"};

   protected BaseAbstractMuxableSocket(Chunk var1, ServerChannel var2) {
      this.filter = null;
      this.closeLatch = new Latch();
      this.availBytes = 0;
      this.msgLength = -1;
      this.connectTime = System.currentTimeMillis();
      this.messagesReceived = 0L;
      this.bytesReceived = 0L;
      this.channel = var2;
      Debug.assertion(this.channel != null);
      this.socketFactory = new ChannelSocketFactory(this.channel);
      this.maxMessageSize = var2.getMaxMessageSize();
      this.setSocketFilter(this);
      this.tail = this.head = var1;
   }

   protected BaseAbstractMuxableSocket(ServerChannel var1) {
      this(Chunk.getChunk(), var1);
   }

   public final void connect(Socket var1) throws IOException {
      this.socket = var1;
      this.sis = var1.getInputStream();
      this.sos = var1.getOutputStream();
   }

   public void connect(InetAddress var1, int var2) throws IOException {
      this.connect(this.createSocket(var1, var2));
   }

   public void connect(InetAddress var1, int var2, int var3) throws IOException {
      this.connect(this.createSocket(var1, var2, var3));
   }

   protected Socket createSocket(InetAddress var1, int var2) throws IOException {
      return this.createSocket(var1, var2, 0);
   }

   protected Socket createSocket(InetAddress var1, int var2, int var3) throws IOException {
      try {
         WeblogicSocketFactory var4 = (WeblogicSocketFactory)this.socketFactory;
         return var4.createSocket(var1, var2, var3);
      } catch (ClassCastException var5) {
         return this.socketFactory.createSocket(var1, var2);
      }
   }

   protected static void p(String var0) {
      System.out.println("<BaseAbstractMuxableSocket>: " + var0);
   }

   protected void setSocketFactory(SocketFactory var1) {
      this.socketFactory = var1;
   }

   public final Chunk getChunk() {
      return this.head;
   }

   public byte[] getBuffer() {
      Chunk var1 = this.ensureCapacity(this.head);
      return var1.buf;
   }

   public int getBufferOffset() {
      Chunk var1 = this.tailWithSpace(this.head);
      return var1.end;
   }

   public ByteBuffer[] getAvailableBufferofSize(int var1) {
      if (this.tail == null) {
         this.tail = this.head;
      }

      this.tail = this.ensureCapacity(this.tail);
      Chunk var2 = this.tail;
      ByteBuffer var3 = var2.getReadByteBuffer();
      int var4 = var3.remaining();
      ArrayList var5 = new ArrayList();
      var5.add(var3);

      while(var4 < var1) {
         Chunk var6 = var2.next;
         if (var6 == null) {
            var6 = Chunk.getChunk();
            var2.next = var6;
         }

         ByteBuffer var7 = var6.getReadByteBuffer();
         var5.add(var7);
         var4 += var7.remaining();
         var2 = var6;
      }

      return (ByteBuffer[])var5.toArray(new ByteBuffer[var5.size()]);
   }

   public void incrementBufferOffset(int var1) throws MaxMessageSizeExceededException {
      if (this.tail == null) {
         this.tail = this.head;
      }

      this.tail = this.tailWithSpace(this.tail);
      Chunk var2 = this.tail;
      this.availBytes += var1;

      while(true) {
         int var3 = Math.min(var1, Chunk.CHUNK_SIZE - var2.end);
         var2.end += var3;
         var1 -= var3;
         if (var1 == 0) {
            if (var2.next != null && var2.next.end == 0) {
               Chunk.releaseChunks(var2.next);
               var2.next = null;
            }

            this.tail = var2;
            if (this.availBytes > this.maxMessageSize) {
               throw new MaxMessageSizeExceededException(this.availBytes, this.maxMessageSize, this.channel.getConfiguredProtocol());
            } else {
               return;
            }
         }

         var2 = var2.next;
      }
   }

   private Chunk tailWithSpace(Chunk var1) {
      Chunk var2;
      for(var2 = var1; var2.next != null && (var2.end == Chunk.CHUNK_SIZE || var2.next.end != 0); var2 = var2.next) {
      }

      return var2;
   }

   private Chunk ensureCapacity(Chunk var1) {
      var1 = this.tailWithSpace(var1);
      if (Chunk.CHUNK_SIZE == var1.end) {
         var1.next = Chunk.getChunk();
         return var1.next;
      } else {
         return var1;
      }
   }

   protected Chunk makeChunkList() {
      Chunk var1 = this.head;
      if (this.availBytes == this.msgLength) {
         this.head = this.tail = Chunk.getChunk();
      } else {
         this.head = Chunk.split(this.head, this.msgLength);
         this.tail = null;
      }

      ++this.messagesReceived;
      this.bytesReceived += (long)this.msgLength;
      this.availBytes -= this.msgLength;
      this.msgLength = -1;
      return var1;
   }

   public long getMessagesReceivedCount() {
      return this.messagesReceived;
   }

   public long getBytesReceivedCount() {
      return this.bytesReceived;
   }

   public final long getConnectTime() {
      return this.connectTime;
   }

   public final int getFileDescriptor() {
      if (this.isClosed()) {
         return -1;
      } else {
         SocketInfo var1 = this.getSocketInfo();
         return var1 == null ? -1 : var1.getFD();
      }
   }

   public final String getLocalAddress() {
      return !this.isClosed() && this.socket != null ? this.socket.getLocalAddress().toString() : "<closed>";
   }

   public final int getLocalPort() {
      return !this.isClosed() && this.socket != null ? this.socket.getLocalPort() : -1;
   }

   public final String getRemoteAddress() {
      return !this.isClosed() && this.socket != null ? this.socket.getInetAddress().toString() : "<closed>";
   }

   public final int getRemotePort() {
      return !this.isClosed() && this.socket != null ? this.socket.getPort() : -1;
   }

   public final Protocol getProtocol() {
      return this.channel.getProtocol();
   }

   private Object writeReplace() {
      return new SocketRuntimeImpl(this);
   }

   public boolean isMessageComplete() {
      if (this.msgLength > -1) {
         return this.availBytes >= this.msgLength;
      } else if (this.availBytes < this.getHeaderLength()) {
         return false;
      } else {
         this.msgLength = this.getMessageLength();
         return this.availBytes >= this.msgLength && this.msgLength > -1;
      }
   }

   protected final byte getHeaderByte(int var1) {
      return this.head.end > var1 ? this.head.buf[var1] : this.head.next.buf[var1 - this.head.end];
   }

   public void dispatch() {
      while(this.isMessageComplete()) {
         this.dispatch(this.makeChunkList());
      }

      SocketMuxer.getMuxer().read(this.getSocketFilter());
   }

   protected int getMessageLength() {
      throw new UnsupportedOperationException("getMessageLength()");
   }

   protected int getHeaderLength() {
      throw new UnsupportedOperationException("getHeaderLength()");
   }

   protected final int getAvailableBytes() {
      return this.availBytes;
   }

   protected void dispatch(Chunk var1) {
      throw new UnsupportedOperationException("dispatch()");
   }

   public final ServerChannel getChannel() {
      return this.channel;
   }

   public final Socket getSocket() {
      return this.socket;
   }

   public boolean closeSocketOnError() {
      return true;
   }

   public final InputStream getSocketInputStream() {
      return this.sis;
   }

   public final OutputStream getSocketOutputStream() {
      return this.sos;
   }

   public final void setSoTimeout(int var1) throws SocketException {
      if (var1 != this.soTimeout) {
         this.soTimeout = var1;
         this.socket.setSoTimeout(var1);
      }
   }

   protected final int getSoTimeout() {
      return this.soTimeout;
   }

   protected final boolean isClosed() {
      return this.closeLatch.isLocked();
   }

   public final void close() {
      if (this.closeLatch.tryLock()) {
         this.cleanup();
      }

   }

   protected void cleanup() {
      this.sis = null;
      this.sos = null;
   }

   public void hasException(Throwable var1) {
      this.close();
   }

   public void endOfStream() {
      this.close();
   }

   public boolean timeout() {
      this.close();
      return true;
   }

   public boolean requestTimeout() {
      return true;
   }

   public int getIdleTimeoutMillis() {
      return this.channel.getIdleConnectionTimeout() * 1000;
   }

   public int getCompleteMessageTimeoutMillis() {
      return this.channel.getCompleteMessageTimeout() * 1000;
   }

   public final MuxableSocket getSocketFilter() {
      return this.filter;
   }

   public final void setSocketFilter(MuxableSocket var1) {
      this.filter = var1;
   }

   public final SocketInfo getSocketInfo() {
      return this.info;
   }

   public final void setSocketInfo(SocketInfo var1) {
      this.info = var1;
   }

   public String toString() {
      return super.toString() + ":" + this.socket;
   }

   public int size() {
      return KEYS.length;
   }

   public String[] getNames() {
      return KEYS;
   }

   public Object getValue(String var1) {
      if (var1.equals("com.bea.contextelement.channel.Port")) {
         return new Integer(this.channel.getPort());
      } else if (var1.equals("com.bea.contextelement.channel.PublicPort")) {
         return new Integer(this.channel.getPublicPort());
      } else if (var1.equals("com.bea.contextelement.channel.RemotePort")) {
         return new Integer(this.getRemotePort());
      } else if (var1.equals("com.bea.contextelement.channel.Protocol")) {
         return this.getProtocol().getAsURLPrefix();
      } else if (var1.equals("com.bea.contextelement.channel.Address")) {
         return this.channel.getAddress();
      } else if (var1.equals("com.bea.contextelement.channel.PublicAddress")) {
         return this.channel.getPublicAddress();
      } else if (var1.equals("com.bea.contextelement.channel.RemoteAddress")) {
         return this.getRemoteAddress();
      } else if (var1.equals("com.bea.contextelement.channel.ChannelName")) {
         return this.channel.getChannelName();
      } else {
         return var1.equals("com.bea.contextelement.channel.Secure") ? new Boolean(this.channel.supportsTLS()) : null;
      }
   }

   public ContextElement[] getValues(String[] var1) {
      ContextElement[] var2 = new ContextElement[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = new ContextElement(var1[var3], this.getValue(var1[var3]));
      }

      return var2;
   }

   protected void resetData() {
      this.tail = this.head = Chunk.getChunk();
      this.availBytes = 0;
      this.msgLength = -1;
      this.messagesReceived = this.bytesReceived = 0L;
   }

   public boolean supportsScatteredRead() {
      return false;
   }

   public long read(NIOConnection var1) throws IOException {
      throw new UnsupportedOperationException();
   }
}
