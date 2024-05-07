package weblogic.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import weblogic.kernel.ExecuteThreadManager;
import weblogic.kernel.Kernel;
import weblogic.kernel.KernelStatus;
import weblogic.management.configuration.KernelMBean;
import weblogic.platform.VM;
import weblogic.server.ServiceFailureException;
import weblogic.socket.internal.SocketEnvironment;
import weblogic.socket.utils.ProxyUtils;
import weblogic.socket.utils.SDPSocketUtils;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.AssertionError;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.io.Chunk;
import weblogic.utils.net.SocketResetException;
import weblogic.work.WorkManagerFactory;

public abstract class SocketMuxer {
   private static final String TIMER_MANAGER_NAME = "MuxerTimerManager";
   private static final int TIMER_MANAGER_INTERVAL = 5000;
   private static final String SOCKET_CHANNEL_ENABLE_PROP = "weblogic.server.enablesocketchannels";
   private static final String DELAYPOLLWAKEUPPROP = "weblogic.socket.SocketMuxer.DELAY_POLL_WAKEUP";
   protected static final long DELAYPOLLWAKEUP = initMuxerDelayPollProp();
   private static final int EOS = -1;
   private static final String sockCreateTimeoutProp = "weblogic.client.socket.ConnectTimeout";
   private static final int sockCreateTimeout = initSockCreateTimeoutProp();
   protected static final String SOCKET_READERS_QUEUE_NAME = "weblogic.socket.Muxer";
   protected static final boolean enableSocketChannels = KernelStatus.isServer() && Boolean.getBoolean("weblogic.server.enablesocketchannels");
   protected final ConcurrentHashMap<MuxableSocket, Object> sockets = new ConcurrentHashMap(4096);
   private static final Object ISPRESENT = new Object();
   private static boolean isAvailable = false;
   protected static KernelMBean config = Kernel.getConfig();
   private static final String osName = initOSNameProp();
   private static final boolean isLinux;
   private static final boolean isAix;
   private static final boolean isJrve;
   protected static int rdrThreads;

   private static int initSockCreateTimeoutProp() {
      if (!KernelStatus.isServer()) {
         try {
            return Integer.getInteger("weblogic.client.socket.ConnectTimeout", 0) * 1000;
         } catch (SecurityException var1) {
            return 0;
         } catch (NumberFormatException var2) {
            return 0;
         }
      } else {
         return 0;
      }
   }

   private static long initMuxerDelayPollProp() {
      return KernelStatus.isServer() ? Long.getLong("weblogic.socket.SocketMuxer.DELAY_POLL_WAKEUP", 0L) : 0L;
   }

   private static String initOSNameProp() {
      String var0 = "UNKNOWN";

      try {
         var0 = System.getProperty("os.name", "UNKNOWN").toLowerCase(Locale.ENGLISH);
      } catch (SecurityException var2) {
      }

      return var0;
   }

   public static SocketMuxer getMuxer() {
      return SocketMuxer.SingletonMaker.singleton;
   }

   public static boolean isAvailable() {
      return isAvailable;
   }

   static SocketMuxer initSocketMuxerOnServer() throws ServiceFailureException {
      if (enableSocketChannels && !VM.getVM().supportsNIOSocketChannels()) {
         String var0 = SocketLogger.logNoSocketChannelSupportForVM();
         throw new ServiceFailureException(var0);
      } else {
         return SocketMuxer.SingletonMaker.singleton;
      }
   }

   private static SocketMuxer makeTheMuxer() {
      JavaSocketMuxer var1;
      try {
         if (!KernelStatus.isServer()) {
            try {
               JavaSocketMuxer var21 = new JavaSocketMuxer();
               return var21;
            } catch (IOException var16) {
               SocketLogger.logJavaMuxerCreationError2();
               if (KernelStatus.DEBUG && Kernel.getDebug().getDebugMuxer()) {
                  SocketLogger.logDebugException("Java muxer creation failed", var16);
               }

               var1 = null;
               return var1;
            }
         }

         String var0 = getNativeMuxerClassName();

         SocketMuxer var2;
         try {
            SocketMuxer var22 = (SocketMuxer)Class.forName(var0).newInstance();
            if (var0 != null && var0.indexOf("Java") == -1) {
               SocketLogger.logNativeIOEnabled();
            } else {
               SocketLogger.logNativeIODisabled();
            }

            var2 = var22;
            return var2;
         } catch (ThreadDeath var17) {
            throw var17;
         } catch (UnsatisfiedLinkError var18) {
            SocketLogger.logMuxerUnsatisfiedLinkError(getLinkError(var0));
            if (KernelStatus.DEBUG && Kernel.getDebug().getDebugMuxer()) {
               SocketLogger.logDebugException("Muxer creation failed", (Exception)var18);
            }
         } catch (Throwable var19) {
            if (!var0.equals("weblogic.socket.DevPollSocketMuxer") && !var0.equals("weblogic.socket.EPollSocketMuxer")) {
               SocketLogger.logNativeMuxerError(var19);
            } else {
               if (var0.equals("weblogic.socket.DevPollSocketMuxer")) {
                  SocketLogger.logNativeDevPollMuxerError(var19);
               } else if (KernelStatus.DEBUG && Kernel.getDebug().getDebugMuxer()) {
                  SocketLogger.logNativeDevPollMuxerError(var19);
               }

               try {
                  var2 = (SocketMuxer)Class.forName("weblogic.socket.PosixSocketMuxer").newInstance();
                  return var2;
               } catch (ThreadDeath var14) {
                  throw var14;
               } catch (Throwable var15) {
                  SocketLogger.logNativeMuxerError(var15);
               }
            }
         }

         SocketLogger.logNativeIODisabled();

         try {
            var1 = new JavaSocketMuxer();
         } catch (IOException var13) {
            SocketLogger.logJavaMuxerCreationError2();
            if (KernelStatus.DEBUG && Kernel.getDebug().getDebugMuxer()) {
               SocketLogger.logDebugException("Java muxer creation failed", var13);
            }

            var2 = null;
            return var2;
         }
      } finally {
         isAvailable = true;
      }

      return var1;
   }

   private static String getNativeMuxerClassName() {
      try {
         String var0 = Kernel.getConfig().getMuxerClass();
         if (var0 != null) {
            return var0;
         } else if (!VM.getVM().isNativeThreads()) {
            return "weblogic.socket.JavaSocketMuxer";
         } else if (!Kernel.getConfig().isNativeIOEnabled()) {
            return "weblogic.socket.JavaSocketMuxer";
         } else if (osName.startsWith("windows")) {
            return "weblogic.socket.NTSocketMuxer";
         } else if (Kernel.getConfig().isDevPollDisabled()) {
            return "weblogic.socket.PosixSocketMuxer";
         } else if (!osName.equals("hp-ux") && !osName.equals("sunos")) {
            if (isLinux) {
               try {
                  Class.forName("jrockit.ext.epoll.EPoll");
                  return "weblogic.socket.EPollSocketMuxer";
               } catch (Throwable var2) {
                  return "weblogic.socket.PosixSocketMuxer";
               }
            } else {
               return isJrve ? "weblogic.socket.EPollSocketMuxer" : "weblogic.socket.PosixSocketMuxer";
            }
         } else {
            return "weblogic.socket.DevPollSocketMuxer";
         }
      } catch (SecurityException var3) {
         return "weblogic.socket.JavaSocketMuxer";
      }
   }

   private static String getLinkError(String var0) {
      String var1 = null;

      try {
         var1 = System.getProperty("java.library.path", "java.library.path");
      } catch (SecurityException var3) {
      }

      if ("weblogic.socket.NTSocketMuxer".equals(var0)) {
         return "Please ensure that wlntio.dll is in: '" + var1 + "'";
      } else {
         return "weblogic.socket.PosixSocketMuxer".equals(var0) ? "Please ensure that libmuxer library is in :'" + var1 + "'" : "Please ensure that a native performance library is in: '" + var1 + "'";
      }
   }

   /** @deprecated */
   protected void initSocketReaderThreads(int var1, String var2, String var3) {
      initThreadCount(var1, var2, var3);
      this.startSocketReaderThreads(var2);
   }

   protected static void initThreadCount(int var0, String var1, String var2) {
      ExecuteThreadManager var4 = Kernel.getExecuteThreadManager(var1);
      int var3;
      if (var4 != null && var4.getName().equalsIgnoreCase(var1)) {
         var3 = var4.getExecuteThreadCount();
      } else {
         var3 = config.getSocketReaders();
         if (var3 <= 0) {
            var3 = Integer.getInteger(var2, -1);
            if (var3 <= 0) {
               int var5 = Runtime.getRuntime().availableProcessors();
               if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxer()) {
                  SocketLogger.logDebug("Number of CPUs=" + var5);
               }

               if (var5 > 0) {
                  var3 = var5 + 1;
               } else {
                  var3 = var0;
               }

               if (var3 > 4) {
                  var3 = 4;
               }
            }
         }

         Kernel.addExecuteQueue(var1, var3);
      }

      rdrThreads = var3;
   }

   protected void startSocketReaderThreads(String var1) {
      if (rdrThreads == -1) {
         throw new IllegalStateException("Socket Reader threads not initialized");
      } else {
         SocketLogger.logAllocSocketReaders(rdrThreads);

         for(int var2 = 0; var2 < rdrThreads; ++var2) {
            Kernel.execute(new SocketReaderRequest(), var1);
         }

      }
   }

   public boolean isAsyncMuxer() {
      return false;
   }

   public InputStream getInputStream(Socket var1) throws IOException {
      return var1.getInputStream();
   }

   public OutputStream getOutputStream(Socket var1) throws IOException {
      return var1.getOutputStream();
   }

   public Socket newSocket(InetAddress var1, int var2) throws IOException {
      return this.newSocket(var1, var2, sockCreateTimeout);
   }

   public Socket newSocket(InetAddress var1, int var2, int var3) throws IOException {
      Socket var4 = new Socket();
      initSocket(var4);
      var4.connect(new InetSocketAddress(var1, var2), var3);
      return var4;
   }

   public Socket newClientSocket(InetAddress var1, int var2, int var3) throws IOException {
      return ProxyUtils.canProxy(var1, false) ? ProxyUtils.getClientProxy(var1.getHostAddress(), var2, var3) : this.newSocket(var1, var2, var3);
   }

   public Socket newClientSocket(InetAddress var1, int var2) throws IOException {
      return this.newClientSocket(var1, var2, 0);
   }

   public Socket newSSLClientSocket(InetAddress var1, int var2, int var3) throws IOException {
      return ProxyUtils.canProxy(var1, true) ? ProxyUtils.getSSLClientProxy(var1.getHostAddress(), var2, var3) : null;
   }

   public Socket newSSLClientSocket(InetAddress var1, int var2, InetAddress var3, int var4, int var5) throws IOException {
      return ProxyUtils.canProxy(var1, true) ? ProxyUtils.getSSLClientProxy(var1.getHostAddress(), var2, var3.getHostAddress(), var4, var5) : null;
   }

   public Socket newSSLClientSocket(InetAddress var1, int var2) throws IOException {
      return this.newSSLClientSocket(var1, var2, 0);
   }

   public Socket newSocket(InetAddress var1, int var2, InetAddress var3, int var4, int var5) throws IOException {
      Socket var6 = new Socket();
      return this.initSocket(var6, var1, var2, var3, var4, var5);
   }

   public Socket newSDPSocket(InetAddress var1, int var2, InetAddress var3, int var4, int var5) throws IOException {
      Socket var6 = SDPSocketUtils.createSDPSocket();
      return this.initSocket(var6, var1, var2, var3, var4, var5);
   }

   protected Socket initSocket(Socket var1, InetAddress var2, int var3, InetAddress var4, int var5, int var6) throws IOException {
      initSocket(var1);
      var1.bind(new InetSocketAddress(var4, var5));
      var1.connect(new InetSocketAddress(var2, var3), var6);
      return var1;
   }

   public Socket newProxySocket(InetAddress var1, int var2, InetAddress var3, int var4, InetAddress var5, int var6, int var7) throws IOException {
      Socket var8 = this.newSocket(var5, var6, var3, var4, var7);
      return ProxyUtils.getProxySocket(var8, var1.getHostName(), var2, var5.getHostName(), var6);
   }

   public Socket newWeblogicSocket(Socket var1) throws IOException {
      initSocket(var1);
      return SocketEnvironment.getSocketEnvironment().serverThrottleEnabled() ? SocketEnvironment.getSocketEnvironment().getWeblogicSocket(var1) : var1;
   }

   static void initSocket(Socket var0) throws SocketOptionException {
      try {
         var0.setTcpNoDelay(true);
      } catch (SocketException var4) {
         try {
            var0.close();
         } catch (IOException var3) {
         }

         throw new SocketOptionException(var4.getMessage());
      }
   }

   protected void closeSocket(Socket var1) {
      this.closeSocket(var1, true);
   }

   private void closeSocket(Socket var1, boolean var2) {
      try {
         if (isLinux || isAix || isJrve) {
            if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxerConnection()) {
               SocketLogger.logDebug("Closing input and output of socket " + var1);
            }

            try {
               var1.shutdownInput();
            } catch (IOException var5) {
            }

            try {
               var1.shutdownOutput();
            } catch (IOException var4) {
            }
         }

         if (var2) {
            if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxerConnection()) {
               SocketLogger.logDebug("Closing raw socket " + var1);
            }

            var1.close();
         }
      } catch (Exception var6) {
      }

   }

   protected SocketMuxer() throws IOException {
      TimerManager var1 = TimerManagerFactory.getTimerManagerFactory().getTimerManager("MuxerTimerManager", WorkManagerFactory.getInstance().getSystem());
      var1.scheduleAtFixedRate(new TimerListenerImpl(), 0L, 5000L);
   }

   public void register(MuxableSocket var1) throws IOException {
      if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxer()) {
         SocketLogger.logDebug("register: sockInfo=" + var1.getSocketInfo());
      }

      this.sockets.put(var1, ISPRESENT);
   }

   public void reRegister(MuxableSocket var1, MuxableSocket var2) {
      if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxer()) {
         SocketLogger.logDebug("reRegister: oldSockInfo=" + var1.getSocketInfo() + ", newSock=" + var2);
      }

      this.sockets.remove(var1);
      SocketInfo var3 = var1.getSocketInfo();
      var1.setSocketInfo((SocketInfo)null);
      var3.setMuxableSocket(var2);
      var2.setSocketInfo(var3);
      this.sockets.put(var2, ISPRESENT);
   }

   public abstract void read(MuxableSocket var1);

   public final void closeSocket(MuxableSocket var1) {
      this.deliverEndOfStream(var1);
   }

   protected abstract void processSockets();

   public final int getNumSockets() {
      return this.sockets.size();
   }

   public final Iterator<MuxableSocket> getSocketsIterator() {
      return this.sockets.keySet().iterator();
   }

   public final MuxableSocket[] getSockets() {
      Set var1 = this.sockets.keySet();
      MuxableSocket[] var2 = new MuxableSocket[var1.size()];
      return (MuxableSocket[])var1.toArray(var2);
   }

   final boolean initiateIO(SocketInfo var1) {
      return var1.ioInitiated();
   }

   final boolean completeIO(MuxableSocket var1, SocketInfo var2) {
      int var3 = var2.ioCompleted();
      if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxerDetail()) {
         SocketLogger.logDebug("completeIO: " + var3 + ", info=" + var2);
      }

      if (var3 == 1) {
         return false;
      } else if ((var3 & 2) != 0) {
         if ((var3 & 8) == 0) {
            this.cleanupSocket(var1, var2);
         }

         return false;
      } else {
         return true;
      }
   }

   public final void finishExceptionHandling(MuxableSocket var1) {
      SocketInfo var2 = var1.getSocketInfo();
      int var3 = var2.exceptionHandlingCompleted();
      if (var3 == 0) {
         this.cleanupSocket(var1, var2);
      } else if (var3 == 4) {
         this.cancelIo(var1);
      }

   }

   public final void deliverEndOfStream(MuxableSocket var1) {
      this.deliverExceptionAndCleanup(var1, (Throwable)null);
   }

   public final void deliverHasException(MuxableSocket var1, Throwable var2) {
      this.deliverExceptionAndCleanup(var1, var2);
   }

   private void deliverExceptionAndCleanup(MuxableSocket var1, Throwable var2) {
      if (var1 == null) {
         throw new AssertionError(var2);
      } else {
         SocketInfo var3 = var1.getSocketInfo();
         if (var3 == null) {
            MuxableSocket var4 = var1.getSocketFilter();
            if (var4 != null) {
               var3 = var4.getSocketInfo();
            }
         }

         if (var3 == null) {
            if (Kernel.DEBUG && (Kernel.getDebug().getDebugMuxer() || Kernel.getDebug().getDebugMuxerConnection())) {
               SocketLogger.logDebug("Unable to find internal data record for socket " + var1);
            }

         } else {
            int var6 = var3.close();
            if (var6 != 1) {
               if (Kernel.DEBUG && (Kernel.getDebug().getDebugMuxer() || Kernel.getDebug().getDebugMuxerConnection())) {
                  StringBuffer var5 = new StringBuffer(100);
                  var5.append("deliver");
                  if (var2 == null) {
                     var5.append("EndOfStream");
                  } else {
                     var5.append("HasException");
                  }

                  var5.append(": sockInfo=").append(var1.getSocketInfo()).append("\n");
                  if (var2 == null) {
                     var5.append(StackTraceUtils.throwable2StackTrace(new Exception()));
                  } else {
                     var5.append(StackTraceUtils.throwable2StackTrace(var2));
                  }

                  SocketLogger.logDebug(var5.toString());
               }

               if (var2 == null) {
                  var1.endOfStream();
               } else {
                  var1.hasException(var2);
               }

               var6 = var3.exceptionHandlingCompleted();
               if (var6 == 0) {
                  this.cleanupSocket(var1, var3);
               } else if (var6 == 4) {
                  this.cancelIo(var1);
               } else {
                  throw new AssertionError("Socket ms=" + var3 + " in unexpected state: " + var6);
               }
            }
         }
      }
   }

   protected void cancelIo(MuxableSocket var1) {
      if (isAix) {
         this.closeSocket(var1.getSocket(), false);
      } else {
         this.closeSocket(var1.getSocket());
      }

      if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxer()) {
         SocketLogger.logDebug("cancelIo: ms=" + var1 + ", sockInfo=" + var1.getSocketInfo());
      }

   }

   void cleanupSocket(MuxableSocket var1, SocketInfo var2) {
      if (this.sockets.remove(var1) != null) {
         if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxer()) {
            SocketLogger.logDebug("cleanupSocket: sockInfo=" + var1.getSocketInfo());
         }

         try {
            var2.cleanup();
         } finally {
            if (var1.closeSocketOnError()) {
               this.closeSocket(var1.getSocket());
            }

         }

      }
   }

   final void readReadySocket(MuxableSocket var1, SocketInfo var2, long var3) {
      if (var3 > 0L) {
         long var5 = System.currentTimeMillis() + var3;

         while(this.readReadySocketOnce(var1, var2)) {
            if (System.currentTimeMillis() > var5) {
               this.read(var1);
               break;
            }

            this.initiateIO(var2);
         }
      } else if (this.readReadySocketOnce(var1, var2)) {
         this.read(var1);
      }

   }

   private final boolean readReadySocketOnce(MuxableSocket var1, SocketInfo var2) {
      Socket var3 = null;
      int var4 = 0;

      try {
         var3 = var1.getSocket();
         InputStream var9 = var1.getSocketInputStream();
         if (var9 == null) {
            this.readCompleted(var1);
            return false;
         }

         var4 = this.readFromSocket(var1);
         if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxerDetail()) {
            SocketLogger.logDebug("read " + var4 + " bytes");
         }
      } catch (InterruptedIOException var6) {
         this.handleReadTimeout(var1);
         return false;
      } catch (IOException var7) {
         Object var5 = var7;
         this.readCompleted(var1);
         if (SocketResetException.isResetException(var7)) {
            var5 = new SocketResetException(var7);
            if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxer()) {
               SocketLogger.logDebugException("Connection reset on socket: '" + var1 + "'", (Exception)var5);
            }
         } else {
            SocketLogger.logIOException(var3.toString(), var7);
         }

         this.deliverHasException(var1, (Throwable)var5);
      } catch (Throwable var8) {
         if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxerDetail()) {
            var8.printStackTrace();
         }
      }

      this.readCompleted(var1);
      if (var4 == -1) {
         if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxer()) {
            SocketLogger.logDebug("EOF on socket: " + var1.getSocketInfo());
         }

         this.deliverEndOfStream(var1);
         return false;
      } else if (var1.isMessageComplete()) {
         var2.messageCompleted();
         if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxerDetail()) {
            SocketLogger.logDebug("dispatch " + var2);
         }

         var1.dispatch();
         return false;
      } else {
         var2.messageInitiated();
         return true;
      }
   }

   protected int readFromSocket(MuxableSocket var1) throws IOException {
      byte[] var2 = var1.getBuffer();
      int var3 = var1.getBufferOffset();
      int var4 = var2.length - var3;
      boolean var5 = false;
      InputStream var6 = var1.getSocketInputStream();
      if (var6 == null) {
         if (Kernel.DEBUG && Kernel.getDebug().getDebugMuxer()) {
            SocketLogger.logDebug("Socket has been closed and cleanedup: " + var1 + " will return EOS from read");
         }

         return -1;
      } else {
         int var7 = var6.read(var2, var3, var4);
         if (var7 > 0) {
            var1.incrementBufferOffset(var7);
         }

         return var7;
      }
   }

   protected void handleReadTimeout(MuxableSocket var1) {
      this.read(var1);
   }

   protected void readCompleted(MuxableSocket var1) {
   }

   public final void write(AsyncOutputStream var1) {
      this.internalWrite(var1);
   }

   protected void internalWrite(AsyncOutputStream var1) {
      OutputStream var2 = var1.getOutputStream();
      Chunk var3 = null;

      while(var2 != null && (var3 = var1.getOutputBuffer()) != null) {
         try {
            var2.write(var3.buf, 0, var3.end);
            var1.handleWrite(var3);
         } catch (IOException var5) {
            var1.handleException(var5);
            return;
         }
      }

   }

   protected TimerListener createTimeoutTrigger() {
      return new TimerListenerImpl();
   }

   static {
      isLinux = "linux".equals(osName);
      isAix = "aix".equals(osName);
      isJrve = "jrve".equalsIgnoreCase(osName);
      rdrThreads = -1;
   }

   protected class TimerListenerImpl implements TimerListener {
      public void timerExpired(Timer var1) {
         Iterator var2 = SocketMuxer.this.getSocketsIterator();

         while(true) {
            MuxableSocket var3;
            SocketInfo var4;
            label63:
            while(true) {
               do {
                  if (!var2.hasNext()) {
                     return;
                  }

                  var3 = (MuxableSocket)var2.next();
                  var4 = var3.getSocketInfo();
               } while(var4 == null);

               long var5 = (long)var3.getIdleTimeoutMillis();
               long var7 = (long)var3.getCompleteMessageTimeoutMillis();
               switch (var4.checkTimeout(var5, var7)) {
                  case 0:
                  default:
                     break;
                  case 2:
                     if (var3.getSocket().isClosed()) {
                        SocketMuxer.this.cleanupSocket(var3, var4);
                     }
                     break;
                  case 16:
                     if (Kernel.DEBUG && (Kernel.getDebug().getDebugMuxer() || Kernel.getDebug().getDebugMuxerTimeout())) {
                        SocketLogger.logDebug("Timeout on socket: '" + var3 + "', sockInfo: " + var3.getSocketInfo() + ", timeout of: '" + var5 / 1000L + " s");
                     }

                     if (var3.timeout()) {
                        break label63;
                     }
                     break;
                  case 32:
                     if (Kernel.isServer()) {
                        String var9 = "A complete message could not be read on socket: '" + var3 + "', in the configured timeout period of '" + var7 / 1000L + "' secs";
                        if (Kernel.DEBUG && (Kernel.getDebug().getDebugMuxer() || Kernel.getDebug().getDebugMuxerTimeout())) {
                           SocketLogger.logDebug(var9 + ", sockInfo=" + var3.getSocketInfo());
                        }

                        var3.hasException(new IOException(var9));
                        break label63;
                     }
               }
            }

            int var10 = var4.exceptionHandlingCompleted();
            if (var10 == 0) {
               SocketMuxer.this.cleanupSocket(var3, var4);
            } else if (var10 == 4) {
               SocketMuxer.this.cancelIo(var3);
            }
         }
      }
   }

   protected static final class SingletonMaker {
      protected static final SocketMuxer singleton = SocketMuxer.makeTheMuxer();
   }
}
