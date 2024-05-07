package weblogic.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ProtocolException;
import java.net.Socket;
import java.util.Locale;
import javax.net.ssl.SSLSocket;
import weblogic.logging.Loggable;
import weblogic.protocol.ProtocolHandler;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.ServerChannel;
import weblogic.security.SecurityService;
import weblogic.security.net.ConnectionEvent;
import weblogic.security.net.FilterException;
import weblogic.security.utils.SSLIOContext;
import weblogic.security.utils.SSLIOContextTable;
import weblogic.server.channels.ServerConnectionRuntimeImpl;
import weblogic.socket.utils.JSSEUtils;

public final class MuxableSocketDiscriminator extends AbstractMuxableSocket {
   private static final long serialVersionUID = -7142489085349898142L;
   private static final boolean DEBUG = false;
   private final ProtocolHandler[] handlers;
   private final ServerChannel[] channels;
   private int claimedIndex = -1;
   private int timeoutMillis;

   public MuxableSocketDiscriminator(Socket var1, ProtocolHandler[] var2, ServerChannel[] var3) throws IOException {
      super(var3[0]);
      this.connect(var1);
      this.setSoTimeout(this.timeoutMillis = this.getSocket().getSoTimeout());
      this.handlers = var2;
      this.channels = var3;
   }

   public String toString() {
      return super.toString() + " - number of bytes read: '" + this.availBytes + "'";
   }

   private boolean isSecure() {
      return this.getSocket() instanceof SSLSocket;
   }

   public boolean isMessageComplete() {
      int var1 = 0;

      for(int var2 = 0; var2 < this.handlers.length; ++var2) {
         ProtocolHandler var3 = this.handlers[var2];
         if (var3.claimSocket(this.head)) {
            this.claimedIndex = var2;
            break;
         }

         var1 = Math.max(var1, var3.getHeaderLength());
      }

      if (this.availBytes < var1) {
         return false;
      } else if (this.claimedIndex < 0) {
         SocketLogger.logConnectionRejected(this.channels[0].getChannelName());
         SocketMuxer.getMuxer().deliverHasException(this.getSocketFilter(), new ProtocolException("Incoming socket: '" + this.getSocket() + "' has unhandled protocol prefix"));
         return false;
      } else {
         return true;
      }
   }

   public void dispatch() {
      if (this.claimedIndex >= 0) {
         try {
            String var1 = ProtocolManager.getRealProtocol(this.channels[this.claimedIndex].getProtocol()).getProtocolName();
            this.maybeFilter(var1);
         } catch (FilterException var10) {
            this.onFilterException(var10);
            return;
         }

         try {
            MuxableSocket var11 = this.handlers[this.claimedIndex].createSocket(this.head, this.getSocket(), this.channels[this.claimedIndex]);
            if (SecurityService.getConnectionLoggerEnabled()) {
               SocketLogger.logInfoAcceptConnection(SecurityService.getConnectionFilterEnabled(), this.getSocket().getInetAddress().toString(), this.getSocket().getPort(), this.getSocket().getLocalAddress().toString(), this.getSocket().getLocalPort(), this.handlers[this.claimedIndex].getProtocol().getProtocolName());
            }

            Object var12;
            if (this.isSecure()) {
               SSLSocket var3 = (SSLSocket)((SSLSocket)this.getSocket());
               JSSESocket var4 = JSSEUtils.getJSSESocket(var3);
               if (var4 != null) {
                  JSSEFilterImpl var5 = (JSSEFilterImpl)this.getSocketFilter();
                  var11.setSocketFilter(var5);
                  var5.setDelegate(var11);
                  var12 = var5;
               } else {
                  SSLIOContext var13 = SSLIOContextTable.findContext(var3);
                  if (var13 == null) {
                     throw new IOException("SSL transport layer closed the socket!");
                  }

                  SSLFilter var6 = (SSLFilter)var13.getFilter();
                  var11.setSocketFilter(var6);
                  var6.setDelegate(var11);
                  var6.activateNoRegister();
                  var12 = var6;
               }
            } else {
               SocketMuxer.getMuxer().reRegister(this.getSocketFilter(), var11);
               var12 = var11;
            }

            this.setSocketFilter((MuxableSocket)var12);
            if (((MuxableSocket)var12).isMessageComplete()) {
               ((MuxableSocket)var12).dispatch();
            } else {
               SocketMuxer.getMuxer().read((MuxableSocket)var12);
            }
         } catch (IOException var7) {
            SocketMuxer.getMuxer().deliverHasException(this.getSocketFilter(), var7);
         } catch (ThreadDeath var8) {
            throw var8;
         } catch (Throwable var9) {
            SocketLogger.logThrowable(var9);
            IOException var2 = new IOException("Exception in protocol discrimination");
            var2.initCause(var9);
            SocketMuxer.getMuxer().deliverHasException(this.getSocketFilter(), var2);
         }

      }
   }

   public int getIdleTimeoutMillis() {
      return this.timeoutMillis;
   }

   public int getCompleteMessageTimeoutMillis() {
      return this.timeoutMillis;
   }

   public boolean timeout() {
      SocketLogger.logSocketIdleTimeout((long)(this.timeoutMillis / 1000), this.socket.getInetAddress().getHostAddress(), this.socket.getPort());
      return super.timeout();
   }

   private void maybeFilter(String var1) throws FilterException {
      if (SecurityService.getConnectionFilterEnabled()) {
         if (SecurityService.getCompatibilityConnectionFiltersEnabled()) {
            var1 = var1.toUpperCase(Locale.ENGLISH);
            if (var1.equals("IIOP")) {
               var1 = "GIOP";
            } else if (var1.equals("IIOPS")) {
               var1 = "GIOPS";
            } else if (var1.equals("COM")) {
               var1 = "DCOM";
            }
         }

         ConnectionEvent var2 = new ConnectionEvent(this.getSocket(), var1);
         SecurityService.getConnectionFilter().accept(var2);
      }

   }

   private void onFilterException(FilterException var1) {
      String var2 = var1.getMessage();
      Loggable var3;
      if (SecurityService.getConnectionLoggerEnabled()) {
         var3 = SocketLogger.logConnectionRejectedFilterExLoggable(this.socketInfo(), var1);
         var3.log();
      }

      var3 = SocketLogger.logConnectionRejectedFilterExLoggable("Socket", var1);
      var2 = var3.getMessage();
      boolean var4 = false;
      byte var5 = this.channels[this.claimedIndex].getProtocol().toByte();
      if (var5 == 1 || var5 == 3) {
         var4 = true;
      }

      Login.rejectConnection(this.getSocket(), 1, var2, var4);
      SocketMuxer.getMuxer().deliverHasException(this.getSocketFilter(), var1);
   }

   private String socketInfo() {
      Socket var1 = this.getSocket();
      if (var1 == null) {
         return "";
      } else {
         InetAddress var2 = var1.getInetAddress();
         return "Socket[" + (var2 != null ? "addr=" + var2.getHostAddress() : "") + ",port=" + var1.getPort() + ",localport=" + var1.getLocalPort() + "]";
      }
   }

   protected void registerForRuntimeMonitoring(ServerChannel var1, ServerConnectionRuntimeImpl var2) {
   }

   protected static void p(String var0) {
      System.err.println("<MuxableSocketDiscriminator:" + System.currentTimeMillis() + "> " + var0);
   }
}
