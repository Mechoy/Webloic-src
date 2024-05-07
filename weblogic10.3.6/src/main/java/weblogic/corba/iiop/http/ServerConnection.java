package weblogic.corba.iiop.http;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.ProtocolException;
import java.security.AccessController;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.iiop.Connection;
import weblogic.iiop.ConnectionKey;
import weblogic.iiop.ConnectionManager;
import weblogic.iiop.EndPoint;
import weblogic.iiop.EndPointImpl;
import weblogic.iiop.EndPointManager;
import weblogic.iiop.IIOPLogger;
import weblogic.management.configuration.ServerDebugMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.SocketRuntime;
import weblogic.protocol.AsyncOutgoingMessage;
import weblogic.protocol.ChannelImpl;
import weblogic.protocol.MessageReceiverStatistics;
import weblogic.protocol.MessageSenderStatistics;
import weblogic.protocol.ServerChannel;
import weblogic.rmi.spi.Channel;
import weblogic.security.acl.UserInfo;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.channels.ServerChannelImpl;
import weblogic.server.channels.ServerConnectionRuntimeImpl;
import weblogic.server.channels.SocketRuntimeImpl;
import weblogic.servlet.FutureServletResponse;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.collections.CircularQueue;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.utils.io.Chunk;
import weblogic.work.WorkManagerFactory;

class ServerConnection extends Connection implements MessageSenderStatistics, MessageReceiverStatistics {
   private static final boolean DEBUG = false;
   private static final boolean ASSERT = true;
   private static final DebugCategory debugTransport = Debug.getCategory("weblogic.iiop.transport");
   private static final DebugLogger debugIIOPTunneling = DebugLogger.getDebugLogger("DebugIIOPTunneling");
   private static ServerDebugMBean debugBean;
   private ServerChannel networkChannel;
   private SocketRuntime sockRuntime;
   private ConnectionKey key = null;
   private static final ConcurrentHashMap channelOpenSocksMap = new ConcurrentHashMap();
   private static long idCount = 0L;
   private final String sockID = getNextID();
   private final CircularQueue queue = new CircularQueue();
   private FutureServletResponse pending;
   private HttpServletRequest pendingRequest;
   private long lastRecv;
   private boolean closed;
   private Channel channel;
   private long messagesSent = 0L;
   private long bytesSent = 0L;
   private final long connectTime = System.currentTimeMillis();
   private long messagesReceived = 0L;
   private long bytesReceived = 0L;

   private static void initialize(ServerChannel var0) {
      AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      debugBean = ManagementService.getRuntimeAccess(var1).getServer().getServerDebug();

      try {
         TunnelScavenger var2 = new TunnelScavenger(var0);
         TimerManager var3 = TimerManagerFactory.getTimerManagerFactory().getTimerManager(ServerConnection.class.getName(), WorkManagerFactory.getInstance().getSystem());
         var3.scheduleAtFixedRate(var2, 0L, (long)(var0.getTunnelingClientTimeoutSecs() * 1000));
      } catch (IllegalArgumentException var4) {
         IIOPLogger.logScavengeCreateFailure(var4);
      } catch (IllegalStateException var5) {
         IIOPLogger.logScavengeCreateFailure(var5);
      }

   }

   private static synchronized String getNextID() {
      return String.valueOf((long)(idCount++));
   }

   public static String acceptConnection(HttpServletRequest var0, int var1, HttpServletResponse var2) throws ProtocolException {
      ServletRequestImpl var3 = (ServletRequestImpl)var0;
      ServerChannel var4 = var3.getConnection().getChannel();
      String var5 = var0.getScheme();
      if (!var4.isTunnelingEnabled()) {
         throw new ProtocolException("HTTP tunneling is disabled for channel " + var4.getChannelName());
      } else {
         ServerConnection var6 = null;
         SocketRuntimeImpl var7 = new SocketRuntimeImpl(var3.getConnection().getSocketRuntime());
         if (!"http".equalsIgnoreCase(var5) && !"https".equalsIgnoreCase(var5)) {
            throw new ProtocolException("Unknown protocol: '" + var5 + "'");
         } else {
            var6 = new ServerConnection(var1, var4, var7);
            String var8 = var0.getHeader("WL-Client-Address");
            if (var8 != null) {
               StringTokenizer var9 = new StringTokenizer(var8, ":");
               String var10 = var9.nextToken();
               int var11 = Integer.parseInt(var9.nextToken());
               ConnectionKey var12 = new ConnectionKey(var10, var11);
               var6.setConnectionKey(var12);
               EndPoint var13 = EndPointManager.findEndPoint(var12);
               if (var13 != null) {
                  ServerConnection var14 = (ServerConnection)var13.getConnection();
                  String var15 = "Closing HTTP tunneling connection: '" + var14 + "' because  a new connection request: '" + var6 + " came in.";
                  ConnectionManager.getConnectionManager().gotExceptionReceiving(var14, new IOException(var15));
               }

               try {
                  EndPointManager.findOrCreateEndPoint((Connection)var6);
               } catch (IOException var18) {
               }
            } else {
               var6.setConnectionKey(new ConnectionKey(var3.getConnection().getSocket().getInetAddress().getHostAddress(), Integer.parseInt(var6.sockID)));
            }

            var6.channel = new ChannelImpl(var3.getConnection().getSocket().getInetAddress().getHostAddress(), Integer.parseInt(var6.sockID), var5);
            ConcurrentHashMap var19 = (ConcurrentHashMap)channelOpenSocksMap.get(var4);
            if (var19 == null) {
               synchronized(channelOpenSocksMap) {
                  var19 = (ConcurrentHashMap)channelOpenSocksMap.get(var4);
                  if (var19 == null) {
                     initialize(var4);
                     var19 = new ConcurrentHashMap();
                     channelOpenSocksMap.put(var4, var19);
                  }
               }
            }

            var19.put(var6.sockID, var6);
            if (debugTransport.isEnabled() || debugIIOPTunneling.isDebugEnabled() || debugBean.getDebugTunnelingConnection()) {
               IIOPLogger.logDebugTransport("Opened tunneled connection - id: '" + var6.sockID + "', keyed on: " + var6.getConnectionKey());
            }

            return var6.sockID;
         }
      }
   }

   static ServerConnection findByID(String var0) {
      Iterator var1 = channelOpenSocksMap.values().iterator();

      ConcurrentHashMap var2;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         var2 = (ConcurrentHashMap)var1.next();
      } while(!var2.containsKey(var0));

      return (ServerConnection)var2.get(var0);
   }

   private Chunk readPacket(HttpServletRequest var1) throws IOException {
      ServletInputStream var2 = var1.getInputStream();
      Chunk var3 = Chunk.getChunk();
      Chunk var4 = var3;
      int var5 = 0;
      var3.end = 0;
      var3.next = null;

      while(true) {
         if (var4.end == Chunk.CHUNK_SIZE) {
            var4.next = Chunk.getChunk();
            var4 = var4.next;
            var4.end = 0;
            var4.next = null;
         }

         int var7 = Chunk.CHUNK_SIZE - var4.end;
         Debug.assertion(var7 > 0);

         int var6;
         try {
            var6 = var2.read(var4.buf, var4.end, var7);
         } catch (InterruptedIOException var9) {
            if (debugTransport.isEnabled() || debugIIOPTunneling.isDebugEnabled()) {
               IIOPLogger.logDebugTransport("Problem reading tunneled packet - nread: '" + var5 + "' content-length: '" + var1.getContentLength() + "'" + var9.getMessage());
            }

            throw var9;
         }

         if (var6 == -1) {
            var2.close();
            this.bytesReceived += (long)var5;
            if (var5 < 12) {
               throw new ProtocolException("Fewer than: '12' bytes read - nread: '" + var5 + "', content-length: '" + var1.getContentLength() + "', method: '" + var1.getMethod() + "', uri: '" + var1.getRequestURI() + "', path info: '" + var1.getPathInfo() + "', query params: '" + var1.getQueryString() + "'");
            }

            return var3;
         }

         var5 += var6;
         var4.end += var6;
      }
   }

   private static int toInt(int var0) {
      return var0 & 255;
   }

   ServerConnection(int var1, ServerChannel var2, SocketRuntime var3) {
      this.networkChannel = var2;
      this.closed = false;
      this.lastRecv = System.currentTimeMillis();
      this.sockRuntime = var3;
      ServerConnectionRuntimeImpl var4 = new ServerConnectionRuntimeImpl(this, this, var3);
      if (var2 instanceof ServerChannelImpl && ((ServerChannelImpl)var2).getRuntime() != null) {
         ((ServerChannelImpl)var2).getRuntime().addServerConnectionRuntime(var4);
      }

   }

   public String toString() {
      return super.toString() + " - id: '" + this.sockID + "', closed: '" + this.closed + "', lastRecv: '" + this.lastRecv + "'";
   }

   private final void p(String var1) {
      System.out.println("<ServerConnection> id: '" + this.sockID + "', closed: '" + this.closed + "', lastRecv: '" + this.lastRecv + "': " + var1);
   }

   public final ServerChannel getChannel() {
      return this.networkChannel;
   }

   public final Channel getRemoteChannel() {
      return this.channel;
   }

   final int getQueueCount() {
      return this.queue.size();
   }

   final synchronized void checkIsDead() {
      int var1 = this.networkChannel.getTunnelingClientTimeoutSecs() * 1000;
      if (var1 != 0) {
         long var2 = System.currentTimeMillis();
         long var4 = var2 - this.lastRecv;
         if ((long)var1 < var4) {
            ConcurrentHashMap var6 = (ConcurrentHashMap)channelOpenSocksMap.get(this.networkChannel);
            if (this.pending == null) {
               var6.remove(this.sockID);
               this.closed = true;
               String var7 = "Timed out HTTP tunneling connection: '" + this + "' because it had been unavailable for: '" + var4 / 1000L + "' seconds, timeout of: '" + var1 / 1000 + "' seconds.";
               ConnectionManager.getConnectionManager().gotExceptionReceiving(this, new IOException(var7));
               if (debugTransport.isEnabled() || debugIIOPTunneling.isDebugEnabled() || debugBean.getDebugTunnelingConnectionTimeout()) {
                  IIOPLogger.logDebugTransport(var7);
               }

            } else {
               try {
                  if (debugTransport.isEnabled() || debugIIOPTunneling.isDebugEnabled() || debugBean.getDebugTunnelingConnectionTimeout()) {
                     IIOPLogger.logDebugTransport("Pinging HTTP tunneling connection: '" + this + "' because it had been idle for: '" + var4 / 1000L + "' seconds, timeout of: '" + var1 / 1000 + "' seconds.");
                  }

                  this.lastRecv = var2;
                  this.pending.setHeader("WL-Result", "RETRY");
                  this.pending.getOutputStream().print("RETRY");
                  this.pending.getOutputStream().flush();
               } catch (IOException var16) {
               } finally {
                  try {
                     this.pending.send();
                  } catch (IOException var15) {
                  }

                  this.pendingRequest = null;
                  this.pending = null;
               }

            }
         }
      }
   }

   final synchronized AsyncOutgoingMessage getNextMessage() {
      this.lastRecv = System.currentTimeMillis();
      return (AsyncOutgoingMessage)this.queue.remove();
   }

   public final void connect(InetAddress var1, int var2) throws IOException {
      throw new ProtocolException("ServerConnection doesn't connect!");
   }

   final synchronized void registerPending(HttpServletRequest var1, FutureServletResponse var2) throws IOException {
      if (this.closed) {
         try {
            Utils.sendDeadResponse(var2);
         } catch (IOException var5) {
         }
      }

      this.lastRecv = System.currentTimeMillis();
      if (this.pending != null && this.pendingRequest != null) {
         String var3 = this.pendingRequest.getParameter("connectionID");
         String var4 = this.pendingRequest.getParameter("rand");
         if (var3 != null && var4 != null && var3.equals(var1.getParameter("connectionID")) && var4.equals(var1.getParameter("rand"))) {
            this.pendingRequest = null;
            this.pending = null;
         }
      }

      Debug.assertion(this.pending == null);
      this.pendingRequest = var1;
      this.pending = var2;
   }

   public final synchronized void send(AsyncOutgoingMessage var1) throws IOException {
      if (this.closed) {
         throw new IOException("ServerConnection closed");
      } else if (this.pending == null) {
         var1.enqueue();
         if (!this.queue.add(var1)) {
            this.close();
            throw new IOException();
         }
      } else {
         this.lastRecv = System.currentTimeMillis();
         ++this.messagesSent;
         this.bytesSent += (long)var1.getLength();

         try {
            var1.enqueue();
            Utils.sendResponse(this.pending, var1);
         } finally {
            try {
               this.pending.send();
            } finally {
               this.pendingRequest = null;
               this.pending = null;
            }
         }

      }
   }

   final void dispatch(HttpServletRequest var1, HttpServletResponse var2) throws IOException {
      if (this.isClosed()) {
         throw new IOException("Socket is closed");
      } else {
         try {
            this.lastRecv = System.currentTimeMillis();
            ++this.messagesReceived;
            ConnectionManager.getConnectionManager().dispatch(this, this.readPacket(var1));
         } catch (IOException var4) {
            if (debugTransport.isEnabled() || debugIIOPTunneling.isDebugEnabled()) {
               IIOPLogger.logDebugTransport("Problem dispatching tunneled message to: '" + this + "'");
            }

            throw var4;
         }
      }
   }

   public final long getMessagesSentCount() {
      return this.messagesSent;
   }

   public final long getBytesSentCount() {
      return this.bytesSent;
   }

   public final long getMessagesReceivedCount() {
      return this.messagesReceived;
   }

   public final long getBytesReceivedCount() {
      return this.bytesReceived;
   }

   public final long getConnectTime() {
      return this.connectTime;
   }

   public final boolean isClosed() {
      return this.closed;
   }

   public final void close() {
      if (!this.isClosed()) {
         synchronized(this) {
            this.closed = true;
            ConcurrentHashMap var2 = (ConcurrentHashMap)channelOpenSocksMap.get(this.networkChannel);
            var2.remove(this.sockID);
            if (debugTransport.isEnabled() || debugIIOPTunneling.isDebugEnabled() || debugBean.getDebugTunnelingConnectionTimeout()) {
               IIOPLogger.logDebugTransport("Closing tunneled socket: '" + this + "'" + new Throwable("Stack trace"));
            }

            if (this.pending != null) {
               try {
                  Utils.sendDeadResponse(this.pending);
                  this.pending.getOutputStream().flush();
               } catch (IOException var14) {
               } finally {
                  try {
                     this.pending.send();
                  } catch (IOException var13) {
                  }

                  this.pendingRequest = null;
                  this.pending = null;
               }
            }

            if (this.networkChannel instanceof ServerChannelImpl && ((ServerChannelImpl)this.networkChannel).getRuntime() != null) {
               ((ServerChannelImpl)this.networkChannel).getRuntime().removeServerConnectionRuntime(this.sockRuntime);
            }

         }
      }
   }

   public final EndPoint getEndPoint() {
      return new EndPointImpl(this, ConnectionManager.getConnectionManager());
   }

   public final ConnectionKey getConnectionKey() {
      return this.key;
   }

   public final void setConnectionKey(ConnectionKey var1) {
      this.key = var1;
   }

   public AuthenticatedSubject getUser() {
      return null;
   }

   public void authenticate(UserInfo var1) {
   }

   public Object getTxContext() {
      return null;
   }

   public void setTxContext(Object var1) {
   }

   protected final boolean isSecure() {
      return this.getChannel().supportsTLS();
   }

   private static final class TunnelScavenger implements TimerListener {
      ServerChannel networkChannel;
      ServerChannelImpl networkChannelimpl;

      TunnelScavenger(ServerChannel var1) {
         this.networkChannel = var1;
         if (var1 instanceof ServerChannelImpl) {
            this.networkChannelimpl = (ServerChannelImpl)var1;
         }

      }

      public void timerExpired(Timer var1) {
         ConcurrentHashMap var2 = (ConcurrentHashMap)ServerConnection.channelOpenSocksMap.get(this.networkChannel);
         Iterator var3 = var2.values().iterator();

         while(var3.hasNext()) {
            ServerConnection var4 = (ServerConnection)var3.next();
            var4.checkIsDead();
         }

         if (var2.isEmpty() && this.networkChannelimpl != null && this.networkChannelimpl.getRuntime() == null) {
            ServerConnection.channelOpenSocksMap.remove(this.networkChannel);
            this.networkChannel = null;
            this.networkChannelimpl = null;
            var1.cancel();
         }

      }
   }
}
