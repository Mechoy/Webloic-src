package weblogic.rjvm.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ProtocolException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.runtime.SocketRuntime;
import weblogic.protocol.OutgoingMessage;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.rjvm.ConnectionManager;
import weblogic.rjvm.MsgAbbrevJVMConnection;
import weblogic.rjvm.RJVMImpl;
import weblogic.rjvm.RJVMLogger;
import weblogic.security.service.ContextHandler;
import weblogic.server.channels.ServerChannelImpl;
import weblogic.server.channels.ServerConnectionRuntimeImpl;
import weblogic.server.channels.SocketRuntimeImpl;
import weblogic.servlet.FutureServletResponse;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.VirtualConnection;
import weblogic.timers.NakedTimerListener;
import weblogic.timers.Timer;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.Debug;
import weblogic.utils.collections.CircularQueue;
import weblogic.utils.io.Chunk;

class HTTPServerJVMConnection extends MsgAbbrevJVMConnection {
   private static final boolean DEBUG = false;
   private static final boolean ASSERT = true;
   private static final DebugLogger debugMessaging = DebugLogger.getDebugLogger("DebugMessaging");
   private static final DebugLogger debugTunnelingConnection = DebugLogger.getDebugLogger("DebugTunnelingConnection");
   private static final DebugLogger debugTunnelingConnectionTimeout = DebugLogger.getDebugLogger("DebugTunnelingConnectionTimeout");
   private SocketRuntime sockRuntime;
   private VirtualConnection connection;
   private static final ConcurrentHashMap channelOpenSocksMap = new ConcurrentHashMap();
   private static long idCount = 0L;
   private final String sockID = getNextID();
   private final CircularQueue queue = new CircularQueue();
   private FutureServletResponse pending;
   private long lastRecv;
   private boolean closed;
   private int localPort;
   private InetAddress localAddress = null;

   private static void initialize(ServerChannel var0) {
      int var1 = var0.getTunnelingClientPingSecs() * 1000;
      TimerManagerFactory.getTimerManagerFactory().getTimerManager("HTTPTunScavanger", "weblogic.kernel.System").schedule(new TunnelScavenger(var0), (long)var1, (long)var1);
   }

   private static synchronized String getNextID() {
      return String.valueOf((long)(idCount++));
   }

   public static String acceptJVMConnection(HttpServletRequest var0, int var1, int var2, HttpServletResponse var3) throws ProtocolException {
      ServletRequestImpl var4 = (ServletRequestImpl)var0;
      ServerChannel var5 = var4.getConnection().getChannel();
      if (!var5.isTunnelingEnabled()) {
         throw new ProtocolException("HTTP tunneling is disabled");
      } else {
         SocketRuntimeImpl var7 = new SocketRuntimeImpl(var4.getConnection().getSocketRuntime());
         String var8 = var0.getScheme();
         Object var6;
         if ("https".equalsIgnoreCase(var8)) {
            var6 = new HTTPSServerJVMConnection(var0, var1, var2, var7, var4.getConnection());
            if (ChannelHelper.isAdminChannel(var5)) {
               ((HTTPServerJVMConnection)var6).setAdminQOS();
            }
         } else {
            if (!"http".equalsIgnoreCase(var8)) {
               throw new ProtocolException("Unknown protocol: '" + var8 + "'");
            }

            var6 = new HTTPServerJVMConnection(var1, var2, var7, var4.getConnection());
         }

         ((HTTPServerJVMConnection)var6).setLocalPort(var0.getServerPort());
         if (var3 != null) {
            ((HTTPServerJVMConnection)var6).setLocalAddress(var4.getConnection().getSocket().getLocalAddress());
         }

         ConcurrentHashMap var9 = (ConcurrentHashMap)channelOpenSocksMap.get(var5);
         if (var9 == null) {
            synchronized(channelOpenSocksMap) {
               var9 = (ConcurrentHashMap)channelOpenSocksMap.get(var5);
               if (var9 == null) {
                  initialize(var5);
                  var9 = new ConcurrentHashMap();
                  channelOpenSocksMap.put(var5, var9);
               }
            }
         }

         var9.put(((HTTPServerJVMConnection)var6).sockID, var6);
         if (debugTunnelingConnection.isDebugEnabled()) {
            RJVMLogger.logDebug("Opened connection - id: '" + ((HTTPServerJVMConnection)var6).sockID + "'");
         }

         return ((HTTPServerJVMConnection)var6).sockID;
      }
   }

   static HTTPServerJVMConnection findByID(String var0) {
      Iterator var1 = channelOpenSocksMap.values().iterator();

      ConcurrentHashMap var2;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         var2 = (ConcurrentHashMap)var1.next();
      } while(!var2.containsKey(var0));

      return (HTTPServerJVMConnection)var2.get(var0);
   }

   private static Chunk readPacket(HttpServletRequest var0) throws IOException {
      Chunk var1 = Chunk.getChunk();
      ServletInputStream var2 = var0.getInputStream();
      Debug.assertion(var2 != null);

      try {
         int var3 = Chunk.chunkFully(var1, var2);
         if (var3 < 4) {
            throw new ProtocolException("Fewer than: '4' bytes read - nread: '" + var3 + "', content-length: '" + var0.getContentLength() + "', method: '" + var0.getMethod() + "', uri: '" + var0.getRequestURI() + "', path info: '" + var0.getPathInfo() + "', query params: '" + var0.getQueryString() + "'");
         }
      } finally {
         var2.close();
      }

      return var1;
   }

   HTTPServerJVMConnection(int var1, int var2, SocketRuntime var3, VirtualConnection var4) {
      this.init(var1, var2);
      this.closed = false;
      this.lastRecv = System.currentTimeMillis();
      this.connection = var4;
      this.sockRuntime = var3;
      ServerConnectionRuntimeImpl var5 = new ServerConnectionRuntimeImpl(this, this, var3);
      ServerChannel var6 = this.getChannel();
      if (var6 instanceof ServerChannelImpl && ((ServerChannelImpl)var6).getRuntime() != null) {
         ((ServerChannelImpl)var6).getRuntime().addServerConnectionRuntime(var5);
      }

      this.setDispatcher(ConnectionManager.create((RJVMImpl)null));
   }

   public final String toString() {
      return super.toString() + " - id: '" + this.sockID + "', closed: '" + this.closed + "', lastRecv: '" + this.lastRecv + "'";
   }

   public final void setLocalAddress(InetAddress var1) {
      this.localAddress = var1;
   }

   public final InetAddress getLocalAddress() {
      return this.localAddress;
   }

   final void setLocalPort(int var1) {
      this.localPort = var1;
   }

   public final ServerChannel getChannel() {
      return this.connection.getChannel();
   }

   public final ContextHandler getContextHandler() {
      return this.connection.getContextHandler();
   }

   public final int getLocalPort() {
      return this.localPort == 0 ? -1 : this.localPort;
   }

   final int getQueueCount() {
      return this.queue.size();
   }

   private synchronized void checkIsDead() {
      ServerChannel var1 = this.getChannel();
      int var2 = var1.getTunnelingClientTimeoutSecs() * 1000;
      if (var2 != 0) {
         long var3 = System.currentTimeMillis();
         long var5 = var3 - this.lastRecv;
         if ((long)var2 < var5) {
            ConcurrentHashMap var7 = (ConcurrentHashMap)channelOpenSocksMap.get(var1);
            if (this.pending == null) {
               var7.remove(this.sockID);
               this.closed = true;
               String var8 = "Timed out HTTP tunneling connection: '" + this + "' because it had been unavailable for: '" + var5 / 1000L + "' seconds, timeout of: '" + var2 / 1000 + "' seconds.";
               this.gotExceptionReceiving(new IOException(var8));
               if (debugTunnelingConnectionTimeout.isDebugEnabled()) {
                  RJVMLogger.logDebug(var8);
               }

            } else {
               try {
                  if (debugTunnelingConnectionTimeout.isDebugEnabled()) {
                     RJVMLogger.logDebug("Pinging HTTP tunneling connection: '" + this + "' because it had been idle for: '" + var5 / 1000L + "' seconds, timeout of: '" + var2 / 1000 + "' seconds.");
                  }

                  this.lastRecv = var3;
                  this.pending.setHeader("WL-Result", "RETRY");
                  this.pending.getOutputStream().print("RETRY");
               } catch (IOException var17) {
               } finally {
                  try {
                     this.pending.send();
                  } catch (IOException var16) {
                  }

                  this.pending = null;
               }

            }
         }
      }
   }

   final synchronized OutgoingMessage getNextMessage() {
      this.lastRecv = System.currentTimeMillis();
      return (OutgoingMessage)this.queue.remove();
   }

   public final void connect(InetAddress var1, int var2) throws IOException {
      throw new ProtocolException("HTTPServerJVMConnection doesn't connect!");
   }

   final synchronized void registerPending(FutureServletResponse var1) {
      if (this.closed) {
         try {
            Utils.sendDeadResponse(var1);
         } catch (IOException var3) {
         }
      }

      this.lastRecv = System.currentTimeMillis();
      Debug.assertion(this.pending == null);
      this.pending = var1;
   }

   public final synchronized void sendMsg(OutgoingMessage var1) throws IOException {
      if (this.closed) {
         throw new IOException("HTTPServerJVMConnection closed");
      } else if (this.pending == null) {
         if (!this.queue.add(var1)) {
            this.close();
            throw new IOException();
         }
      } else {
         this.lastRecv = System.currentTimeMillis();

         try {
            this.pending.setContentType("application/octet-stream");
            this.pending.setContentLength(var1.getLength());
            this.pending.setHeader("WL-Result", "OK");
            ServletOutputStream var2 = this.pending.getOutputStream();
            var1.writeTo(var2);
         } finally {
            try {
               this.pending.send();
            } finally {
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
            super.dispatch(readPacket(var1));
         } catch (IOException var4) {
            throw var4;
         }
      }
   }

   final synchronized boolean isClosed() {
      return this.closed;
   }

   public final synchronized void close() {
      if (!this.closed) {
         this.closed = true;
         ConcurrentHashMap var1 = (ConcurrentHashMap)channelOpenSocksMap.get(this.getChannel());
         var1.remove(this.sockID);
         if (debugTunnelingConnection.isDebugEnabled()) {
            RJVMLogger.logDebug("Closing JVM socket: '" + this + "'" + new Throwable("Stack trace"));
         }

         if (this.pending != null) {
            try {
               Utils.sendDeadResponse(this.pending);
            } catch (IOException var11) {
            } finally {
               try {
                  this.pending.send();
               } catch (IOException var10) {
               }

               this.pending = null;
            }
         }

         ServerChannel var2 = this.getChannel();
         if (var2 instanceof ServerChannelImpl && ((ServerChannelImpl)var2).getRuntime() != null) {
            ((ServerChannelImpl)var2).getRuntime().removeServerConnectionRuntime(this.sockRuntime);
         }

      }
   }

   static final class TunnelScavenger implements NakedTimerListener {
      ServerChannel networkChannel;
      ServerChannelImpl networkChannelimpl;

      TunnelScavenger(ServerChannel var1) {
         this.networkChannel = var1;
         if (var1 instanceof ServerChannelImpl) {
            this.networkChannelimpl = (ServerChannelImpl)var1;
         }

      }

      public final void timerExpired(Timer var1) {
         ConcurrentHashMap var2 = (ConcurrentHashMap)HTTPServerJVMConnection.channelOpenSocksMap.get(this.networkChannel);
         Iterator var3 = var2.values().iterator();

         while(var3.hasNext()) {
            HTTPServerJVMConnection var4 = (HTTPServerJVMConnection)var3.next();
            var4.checkIsDead();
         }

         if (var2.isEmpty() && this.networkChannelimpl != null && this.networkChannelimpl.getRuntime() == null) {
            HTTPServerJVMConnection.channelOpenSocksMap.remove(this.networkChannel);
            this.networkChannel = null;
            this.networkChannelimpl = null;
            var1.cancel();
         }

      }
   }
}
