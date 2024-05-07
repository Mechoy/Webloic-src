package weblogic.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import weblogic.kernel.Kernel;
import weblogic.security.SSL.WLSSSLNioSocket;
import weblogic.server.channels.ServerThrottle;
import weblogic.socket.utils.SDPSocketUtils;
import weblogic.utils.io.Chunk;

final class NIOSocketMuxer extends ServerSocketMuxer {
   private static final int MULTIPLIER = 1540483477;
   private final Selector[] selectors;
   private final ArrayList<NIOSocketInfo>[] registerLists;
   private int nextId;

   public NIOSocketMuxer() throws IOException {
      this.selectors = new Selector[rdrThreads];
      this.registerLists = new ArrayList[rdrThreads];
      int var1 = rdrThreads;

      while(true) {
         --var1;
         if (var1 < 0) {
            this.startSocketReaderThreads("weblogic.socket.Muxer");
            return;
         }

         this.selectors[var1] = SelectorProvider.provider().openSelector();
         this.registerLists[var1] = new ArrayList(1024);
      }
   }

   private static int selectorIndex(SocketChannel var0) {
      int var1 = var0.hashCode();
      int var2 = var1 * 1540483477;
      var2 ^= var2 >> 24;
      var2 *= 1540483477;
      var2 ^= var2 >> 13;
      var2 *= 1540483477;
      var2 ^= var2 >> 15;
      int var3 = var2 % rdrThreads;
      if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxerDetail()) {
         SocketLogger.logDebug("NIOSocketMuxer | hash start = " + var1 + " | hash result = " + var2 + " | index = " + var3);
      }

      return var3;
   }

   public boolean isAsyncMuxer() {
      return true;
   }

   public void register(MuxableSocket var1) throws IOException {
      SocketChannel var2 = var1.getSocket().getChannel();
      int var3 = 0;
      if (var2 != null && rdrThreads > 1) {
         var3 = selectorIndex(var2);
      }

      var1.setSocketInfo(new NIOSocketInfo(var1, var3));
      super.register(var1);
   }

   public void read(MuxableSocket var1) {
      this.internalRead(var1, (NIOSocketInfo)var1.getSocketInfo());
   }

   protected void cancelIo(MuxableSocket var1) {
      super.cancelIo(var1);
      if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxer()) {
         SocketLogger.logDebug("explicitly calling cleanupSocket for ms=" + var1);
      }

      this.cleanupSocket(var1, var1.getSocketInfo());
   }

   protected void closeSocket(Socket var1) {
      if (!var1.isOutputShutdown()) {
         try {
            var1.shutdownOutput();
         } catch (Exception var5) {
            if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxerDetail()) {
               SocketLogger.logDebugException("shutdownOutput error for socket=" + var1, var5);
            }
         }
      }

      SocketChannel var2 = var1.getChannel();
      int var3;
      if (var2 != null) {
         if (var2.isOpen()) {
            if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxerDetail()) {
               SocketLogger.logDebug("close socket=" + var1);
            }

            try {
               var2.close();
            } catch (Exception var4) {
               if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxerDetail()) {
                  SocketLogger.logDebugException("close channel error for socket=" + var1, var4);
               }
            }
         }

         var3 = selectorIndex(var2);
         this.selectors[var3].wakeup();
      } else {
         for(var3 = 0; var3 < this.selectors.length; ++var3) {
            this.selectors[var3].wakeup();
         }
      }

   }

   private void internalRead(MuxableSocket var1, NIOSocketInfo var2) {
      if (this.initiateIO(var2)) {
         try {
            if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxerDetail()) {
               SocketLogger.logDebug("read: sockInfo=" + var2);
            }

            SocketChannel var3 = var2.getSocketChannel();
            if (var3 == null) {
               var1.hasException(new IOException("SocketChannel not available"));
               return;
            }

            int var4 = var2.getSelectorIndex();
            if (var3.isBlocking()) {
               var3.configureBlocking(false);
            }

            SelectionKey var5 = var2.getSelectionKey();
            if (var5 == null) {
               synchronized(this.registerLists[var4]) {
                  this.registerLists[var4].add(var2);
               }
            } else {
               if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxerDetail()) {
                  SocketLogger.logDebug("set interest ops for: sockInfo=" + var2);
               }

               var5.interestOps(1);
            }

            this.selectors[var4].wakeup();
         } catch (ThreadDeath var9) {
            throw var9;
         } catch (Throwable var10) {
            if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxer()) {
               SocketLogger.logDebugException("register for Selection failed for ms=" + var1.getSocketInfo() + " with: ", (Exception)var10);
            }

            this.deliverHasException(var1, var10);
         }

      }
   }

   public InputStream getInputStream(Socket var1) throws IOException {
      SocketChannel var2 = var1.getChannel();
      NetworkInterfaceInfo var3 = NetworkInterfaceInfo.getNetworkInterfaceInfo(var1.getLocalAddress());
      return new NIOInputStream(var2, var3);
   }

   public OutputStream getOutputStream(Socket var1) throws IOException {
      SocketChannel var2 = var1.getChannel();
      NetworkInterfaceInfo var3 = NetworkInterfaceInfo.getNetworkInterfaceInfo(var1.getLocalAddress());
      return new NIOOutputStream(var2, var3);
   }

   public Socket newSocket(InetAddress var1, int var2, int var3) throws IOException {
      SocketChannel var4 = SocketChannel.open();
      Socket var5 = var4.socket();
      initSocket(var5);
      var5.connect(new InetSocketAddress(var1, var2), var3);
      return this.createWeblogicSocket(var5);
   }

   public Socket newSocket(InetAddress var1, int var2, InetAddress var3, int var4, int var5) throws IOException {
      SocketChannel var6 = SocketChannel.open();
      Socket var7 = var6.socket();
      initSocket(var7);
      var7.bind(new InetSocketAddress(var3, var4));
      var7.connect(new InetSocketAddress(var1, var2), var5);
      return this.createWeblogicSocket(var7);
   }

   public Socket newSDPSocket(InetAddress var1, int var2, InetAddress var3, int var4, int var5) throws IOException {
      Socket var6 = SDPSocketUtils.createSDPSocket();
      var6 = this.initSocket(var6, var1, var2, var3, var4, var5);
      return this.createWeblogicSocket(var6);
   }

   public WeblogicSocket newWeblogicSocket(Socket var1) throws IOException {
      initSocket(var1);
      return ServerThrottle.getServerThrottle().isEnabled() ? this.createWeblogicSocketImpl(var1) : this.createWeblogicSocket(var1);
   }

   private WeblogicSocket createWeblogicSocketImpl(Socket var1) {
      final NetworkInterfaceInfo var2 = NetworkInterfaceInfo.getNetworkInterfaceInfo(var1.getLocalAddress());
      return new WeblogicSocketImpl(var1) {
         public InputStream getInputStream() throws IOException {
            return NIOSocketMuxer.this.new NIOInputStream(this.getSocket().getChannel(), var2);
         }

         public OutputStream getOutputStream() throws IOException {
            return NIOSocketMuxer.this.new NIOOutputStream(this.getSocket().getChannel(), var2);
         }
      };
   }

   private WeblogicSocket createWeblogicSocket(Socket var1) {
      final NetworkInterfaceInfo var2 = NetworkInterfaceInfo.getNetworkInterfaceInfo(var1.getLocalAddress());
      return new WeblogicSocket(var1) {
         public InputStream getInputStream() throws IOException {
            return NIOSocketMuxer.this.new NIOInputStream(this.getSocket().getChannel(), var2);
         }

         public OutputStream getOutputStream() throws IOException {
            return NIOSocketMuxer.this.new NIOOutputStream(this.getSocket().getChannel(), var2);
         }
      };
   }

   public ServerSocket newServerSocket(InetAddress var1, int var2, int var3, boolean var4) throws IOException {
      ServerSocketChannel var5 = ServerSocketChannel.open();
      if (var1 == null) {
         var5.socket().bind(new InetSocketAddress(var2), var3);
      } else {
         var5.socket().bind(new InetSocketAddress(var1, var2), var3);
      }

      return new WeblogicServerSocket(var5.socket(), true);
   }

   public void processSockets() {
      boolean var1 = false;
      boolean var2 = false;
      int var17;
      synchronized(this) {
         var17 = this.nextId++;
      }

      if (var17 > rdrThreads) {
         throw new IllegalStateException("index > THREAD_COUNT | " + var17 + " > " + rdrThreads);
      } else {
         while(true) {
            while(true) {
               label87:
               while(true) {
                  try {
                     int var18;
                     try {
                        var18 = this.selectors[var17].select();
                     } catch (CancelledKeyException var14) {
                        continue;
                     }

                     if (this.registerLists[var17].size() > 0) {
                        this.registerNewSockets(var17);
                     }

                     if (var18 != 0) {
                        if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxerDetail()) {
                           SocketLogger.logDebug("select returns " + var18 + " keys");
                        }

                        Set var3 = this.selectors[var17].selectedKeys();
                        Iterator var4 = var3.iterator();
                        boolean var5 = false;

                        while(true) {
                           if (!var4.hasNext()) {
                              break label87;
                           }

                           SelectionKey var6 = (SelectionKey)var4.next();
                           var4.remove();
                           NIOSocketInfo var7 = (NIOSocketInfo)var6.attachment();
                           if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxerDetail()) {
                              SocketLogger.logDebug("clear interest ops for: sockInfo=" + var7);
                           }

                           try {
                              var6.interestOps(var6.interestOps() & ~var6.readyOps());
                           } catch (CancelledKeyException var12) {
                           }

                           MuxableSocket var8 = var7.getMuxableSocket();
                           if (this.completeIO(var8, var7)) {
                              try {
                                 this.readReadySocket(var8, var7, 0L);
                              } catch (Throwable var11) {
                                 this.deliverHasException(var8, var11);
                              }
                           }
                        }
                     }
                  } catch (ThreadDeath var15) {
                     throw var15;
                  } catch (Throwable var16) {
                     SocketLogger.logUncaughtThrowable(var16);
                     break;
                  }
               }

               try {
                  if (DELAYPOLLWAKEUP > 0L) {
                     Thread.sleep(DELAYPOLLWAKEUP);
                  }
               } catch (InterruptedException var10) {
                  Thread.interrupted();
               }
            }
         }
      }
   }

   private void registerNewSockets(int var1) throws ClosedChannelException {
      NIOSocketInfo[] var2;
      synchronized(this.registerLists[var1]) {
         int var4 = this.registerLists[var1].size();
         if (var4 == 0) {
            return;
         }

         var2 = (NIOSocketInfo[])this.registerLists[var1].toArray(new NIOSocketInfo[var4]);
         this.registerLists[var1].clear();
      }

      int var3 = var2.length - 1;

      while(var3 >= 0) {
         NIOSocketInfo var14 = var2[var3--];
         SelectionKey var5 = var14.getSelectionKey();
         if (var5 == null) {
            SocketChannel var6 = var14.getSocketChannel();
            if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxerDetail()) {
               SocketLogger.logDebug("SocketChannel.register: sockInfo=" + var14);
            }

            try {
               var5 = var6.register(this.selectors[var1], 1, var14);
               var14.setSelectionKey(var5);
            } catch (CancelledKeyException var11) {
               synchronized(this.registerLists[var1]) {
                  this.registerLists[var1].add(var14);
               }
            } catch (ClosedChannelException var12) {
               MuxableSocket var8 = var14.getMuxableSocket();
               this.deliverHasException(var8, var12);
            }
         }
      }

   }

   protected int readFromSocket(MuxableSocket var1) throws IOException {
      InputStream var2 = var1.getSocketInputStream();
      synchronized(var1.getSocket()) {
         if (var1.supportsScatteredRead() && var2 instanceof NIOConnection && ((NIOConnection)var2).supportsScatteredReads()) {
            int var4 = (int)var1.read((NIOConnection)var2);
            return var4;
         } else {
            return super.readFromSocket(var1);
         }
      }
   }

   protected void internalWrite(AsyncOutputStream var1) {
      OutputStream var2 = var1.getOutputStream();
      Chunk var3 = var1.getOutputBuffer();
      if (var3 != null && var3.next != null && var1.supportsGatheringWrite() && var2 instanceof NIOConnection && ((NIOConnection)var2).supportsGatheredWrites()) {
         try {
            var1.write((NIOConnection)var2);
         } catch (IOException var5) {
            var1.handleException(var5);
            return;
         }
      } else {
         super.internalWrite(var1);
      }

   }

   static {
      initThreadCount(3, "weblogic.socket.Muxer", "");
   }

   private class NIOOutputStream extends OutputStream implements GatheringByteChannel, NIOConnection {
      private static final int MAX_WRITE_RETRY = 10;
      private static final int SLEEP_BEFORE_RETRY = 2;
      private final GatheringByteChannel wc;
      private ByteBuffer lastByteBuffer;
      private byte[] lastByteArray;
      private NetworkInterfaceInfo nwInfo;

      private NIOOutputStream(SocketChannel var2, NetworkInterfaceInfo var3) {
         Socket var4 = var2.socket();
         this.nwInfo = var3;
         if (var4 instanceof WLSSSLNioSocket) {
            WritableByteChannel var5 = ((WLSSSLNioSocket)var4).getWritableByteChannel();
            if (var5 instanceof GatheringByteChannel) {
               this.wc = (GatheringByteChannel)var5;
            } else {
               this.wc = this.getGatheringByteChannel(var5);
            }

            SocketLogger.logDebug("NIOOutputStream constructed with writableByteChannel: " + this.wc);
         } else {
            this.wc = var2;
         }

         if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxerDetail()) {
            SocketLogger.logDebug("NIOOutputStream created");
         }

      }

      private GatheringByteChannel getGatheringByteChannel(final WritableByteChannel var1) {
         return new GatheringByteChannel() {
            public boolean isOpen() {
               return var1.isOpen();
            }

            public void close() throws IOException {
               var1.close();
            }

            public int write(ByteBuffer var1x) throws IOException {
               return var1.write(var1x);
            }

            public long write(ByteBuffer[] var1x, int var2, int var3) throws IOException {
               long var4 = 0L;
               int var6 = var2 + var3;
               if (var6 > var1x.length) {
                  throw new IndexOutOfBoundsException();
               } else {
                  int var7 = var2;

                  while(var7 < var6) {
                     var4 += (long)var1.write(var1x[var7]);
                     if (!var1x[var7].hasRemaining()) {
                        ++var7;
                     }
                  }

                  return var4;
               }
            }

            public long write(ByteBuffer[] var1x) throws IOException {
               return this.write(var1x, 0, var1x.length);
            }
         };
      }

      private ByteBuffer getByteBuffer(byte[] var1, int var2, int var3) {
         if (var1 != this.lastByteArray) {
            this.lastByteArray = var1;
            this.lastByteBuffer = ByteBuffer.wrap(var1);
         }

         return (ByteBuffer)this.lastByteBuffer.position(var2).limit(var2 + var3);
      }

      public void write(int var1) throws IOException {
         this.write((byte[])(new byte[]{(byte)var1}), 0, 1);
      }

      public void write(byte[] var1, int var2, int var3) throws IOException {
         int var4 = 0;

         for(ByteBuffer var5 = this.getByteBuffer(var1, var2, var3); var5.hasRemaining(); var4 += this.wc.write(var5)) {
         }

         if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxer()) {
            SocketLogger.logDebug("NIOOutputStream.write: expected to write " + var3 + " bytes, actually write " + var4 + " bytes");
         }

      }

      public long write(ByteBuffer[] var1) throws IOException {
         return this.write((ByteBuffer[])var1, 0, var1.length);
      }

      public long write(ByteBuffer[] var1, int var2, int var3) throws IOException {
         long var4 = 0L;
         int var6 = var2 + var3;
         int var7 = var2;
         int var8 = 0;

         while(var7 < var6) {
            long var9 = this.wc.write(var1, var7, var3 - var7);
            var8 = this.backOffIfNeeded(var9, var8);

            for(var4 += var9; var7 < var6 && !var1[var7].hasRemaining(); ++var7) {
            }
         }

         return var4;
      }

      private int backOffIfNeeded(long var1, int var3) {
         if (var1 > 0L) {
            return 0;
         } else {
            if (var3 == 10) {
               try {
                  var3 = 0;
                  Thread.sleep(2L);
               } catch (Exception var5) {
               }
            } else {
               ++var3;
            }

            return var3;
         }
      }

      public int write(ByteBuffer var1) throws IOException {
         return this.wc.write(var1);
      }

      public boolean isOpen() {
         return this.wc.isOpen();
      }

      public InetAddress getLocalInetAddress() {
         return this.nwInfo.getLocalInetAddress();
      }

      public int getMTU() {
         return this.nwInfo.getMTU();
      }

      public int getOptimalNumberOfBuffers() {
         return this.nwInfo.getOptimalNumberOfBuffers();
      }

      public boolean supportsGatheredWrites() {
         return this.nwInfo.supportsGatheredWrites();
      }

      public GatheringByteChannel getGatheringByteChannel() {
         return this;
      }

      public boolean supportsScatteredReads() {
         return false;
      }

      public ScatteringByteChannel getScatteringByteChannel() {
         throw new UnsupportedOperationException();
      }

      // $FF: synthetic method
      NIOOutputStream(SocketChannel var2, NetworkInterfaceInfo var3, Object var4) {
         this(var2, var3);
      }
   }

   private class NIOInputStream extends InputStream implements ScatteringByteChannel, NIOConnection {
      private final SocketChannel sc;
      private ByteBuffer lastByteBuffer;
      private byte[] lastByteArray;
      private NetworkInterfaceInfo nwInfo;
      private int index;

      private NIOInputStream(SocketChannel var2, NetworkInterfaceInfo var3) {
         this.sc = var2;
         this.nwInfo = var3;
         this.index = NIOSocketMuxer.selectorIndex(var2);
         if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxerDetail()) {
            SocketLogger.logDebug("NIOInputStream created");
         }

      }

      private ByteBuffer getByteBuffer(byte[] var1, int var2, int var3) {
         if (var1 != this.lastByteArray) {
            this.lastByteArray = var1;
            this.lastByteBuffer = ByteBuffer.wrap(var1);
         }

         return (ByteBuffer)this.lastByteBuffer.position(var2).limit(var2 + var3);
      }

      public int read() throws IOException {
         byte[] var1 = new byte[1];
         int var2 = this.read((byte[])var1, 0, 1);
         return var2 == 1 ? var1[0] : var2;
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         if (var3 == 0) {
            return 0;
         } else {
            ByteBuffer var4 = this.getByteBuffer(var1, var2, var3);
            int var5 = this.sc.read(var4);
            if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxerDetail()) {
               SocketLogger.logDebug("NIOInputStream.read: expected to read " + var3 + " bytes, actually read " + var5 + " bytes");
            }

            if (var5 == 0) {
               this.configureBlockingChannel();
               var5 = this.sc.read(var4);
            }

            return var5;
         }
      }

      public long read(ByteBuffer[] var1) throws IOException {
         return this.read((ByteBuffer[])var1, 0, var1.length);
      }

      public long read(ByteBuffer[] var1, int var2, int var3) throws IOException {
         long var4 = this.sc.read(var1, var2, var3);
         if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxerDetail()) {
            SocketLogger.logDebug("NIOInputStream.read: " + var4 + " bytes");
         }

         if (var4 == 0L) {
            this.configureBlockingChannel();
            var4 = this.sc.read(var1, var2, var3);
         }

         return var4;
      }

      public int read(ByteBuffer var1) throws IOException {
         ByteBuffer[] var2 = new ByteBuffer[]{var1};
         return (int)this.read((ByteBuffer[])var2, 0, 1);
      }

      public boolean isOpen() {
         return this.sc.isOpen();
      }

      private void configureBlockingChannel() throws IOException {
         SelectionKey var1 = this.sc.keyFor(NIOSocketMuxer.this.selectors[this.index]);
         if (var1 != null) {
            NIOSocketInfo var2 = (NIOSocketInfo)var1.attachment();
            var1.cancel();
            var2.setSelectionKey((SelectionKey)null);
         }

         this.sc.configureBlocking(true);
      }

      public InetAddress getLocalInetAddress() {
         return this.nwInfo.getLocalInetAddress();
      }

      public int getMTU() {
         return this.nwInfo.getMTU();
      }

      public int getOptimalNumberOfBuffers() {
         return this.nwInfo.getOptimalNumberOfBuffers();
      }

      public boolean supportsScatteredReads() {
         return this.nwInfo.supportsScatteredReads();
      }

      public ScatteringByteChannel getScatteringByteChannel() {
         return this;
      }

      public boolean supportsGatheredWrites() {
         return false;
      }

      public GatheringByteChannel getGatheringByteChannel() {
         throw new UnsupportedOperationException();
      }

      // $FF: synthetic method
      NIOInputStream(SocketChannel var2, NetworkInterfaceInfo var3, Object var4) {
         this(var2, var3);
      }
   }
}
