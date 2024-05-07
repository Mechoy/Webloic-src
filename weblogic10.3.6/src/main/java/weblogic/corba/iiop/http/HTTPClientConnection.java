package weblogic.corba.iiop.http;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import weblogic.common.internal.VersionInfo;
import weblogic.iiop.Connection;
import weblogic.iiop.ConnectionKey;
import weblogic.iiop.ConnectionManager;
import weblogic.iiop.EndPoint;
import weblogic.iiop.EndPointImpl;
import weblogic.iiop.IIOPLogger;
import weblogic.kernel.KernelStatus;
import weblogic.protocol.AsyncOutgoingMessage;
import weblogic.protocol.ChannelImpl;
import weblogic.protocol.Protocol;
import weblogic.protocol.ServerChannel;
import weblogic.rmi.spi.Channel;
import weblogic.security.acl.UserInfo;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.servlet.internal.ProtocolHandlerHTTP;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.io.Chunk;
import weblogic.work.WorkManagerFactory;

public class HTTPClientConnection extends Connection implements Runnable {
   private static final boolean ASSERT = true;
   private static final boolean DEBUG = false;
   private static final DebugCategory debugTransport = Debug.getCategory("weblogic.iiop.transport");
   private boolean closed;
   private String host;
   private int port;
   private final Protocol protocol;
   private ServerChannel networkChannel;
   private Channel channel;
   private String connectionID = null;
   private EndPoint endPoint = null;
   private ConnectionKey key = null;

   public final ServerChannel getChannel() {
      return this.networkChannel;
   }

   public Channel getRemoteChannel() {
      return this.channel;
   }

   private static String getTunellingURLExtension() {
      return "/a.tun";
   }

   private static Chunk readPacket(InputStream var0) throws IOException {
      Chunk var1 = Chunk.getChunk();
      Chunk var2 = var1;
      int var3 = 0;

      while(true) {
         if (var2.end == Chunk.CHUNK_SIZE) {
            var2.next = Chunk.getChunk();
            var2 = var2.next;
         }

         int var4 = Chunk.CHUNK_SIZE - var2.end;
         Debug.assertion(var4 > 0);
         int var5 = var0.read(var2.buf, var2.end, var4);
         if (var5 == -1) {
            var0.close();
            Debug.assertion(var3 > 12);
            return var1;
         }

         var3 += var5;
         var2.end += var5;
      }
   }

   public static Connection createConnection(InetAddress var0, int var1, ServerChannel var2) throws IOException {
      HTTPClientConnection var3 = new HTTPClientConnection(var2);
      var3.connect(var0, var1);
      WorkManagerFactory.getInstance().getSystem().schedule(var3);
      return var3;
   }

   HTTPClientConnection(ServerChannel var1) {
      this.networkChannel = var1;
      this.closed = true;
      this.protocol = ProtocolHandlerHTTP.PROTOCOL_HTTP;
   }

   URLConnection createURLConnection(URL var1) throws IOException {
      return var1.openConnection();
   }

   public final String toString() {
      return super.toString() + " - id: '" + this.connectionID + "', host: '" + this.host + "', port: '" + this.port + " closed: '" + this.closed + "'";
   }

   public final Protocol getProtocol() {
      return this.protocol;
   }

   public final InetAddress getLocalAddress() {
      return null;
   }

   public final int getLocalPort() {
      return -1;
   }

   final Socket getSocket() {
      return null;
   }

   private final String getRequestArgs() {
      return "?connectionID=" + this.connectionID + "&rand=" + TunnelUtils.getNextRandom();
   }

   private final void handleNullResponse(URLConnection var1) throws ProtocolException {
      if (debugTransport.isEnabled()) {
         StringBuffer var2 = new StringBuffer();
         int var3 = 0;

         while(true) {
            String var4 = var1.getHeaderFieldKey(var3);
            String var5 = var1.getHeaderField(var3);
            if (var4 == null && var5 == null) {
               IIOPLogger.logDebugTransport("Result unspecified - tunneled connection: '" + var1 + "', headers:\n" + var2);
               break;
            }

            var2.append('\t');
            var2.append(var3);
            var2.append(") ");
            var2.append(var4);
            var2.append(": ");
            var2.append(var5);
            var2.append('\n');
            ++var3;
         }
      }

      throw new ProtocolException("Tunneling result unspecified - is the HTTP server at host: '" + this.host + "' and port: '" + this.port + "' a WebLogic Server?");
   }

   public final synchronized void connect(InetAddress var1, int var2) throws IOException {
      if (!this.closed) {
         throw new ProtocolException("Already connected");
      } else {
         this.host = null;
         this.port = var2;
         this.host = var1.getHostName();
         String var3 = URLEncoder.encode(this.getProtocol().getProtocolName() + " dummy WLREQS " + VersionInfo.theOne().getReleaseVersion() + " dummy \n");
         URL var4 = new URL(this.getProtocol().getProtocolName(), this.host, var2, KernelStatus.getTunellingURL("/bea_wls_internal/iiop/ClientLogin") + getTunellingURLExtension() + "?wl-login=" + var3 + "&rand=" + TunnelUtils.getNextRandom() + "&" + "HL" + "=" + 12);
         URLConnection var5 = null;

         try {
            var5 = this.createURLConnection(var4);
         } catch (IOException var12) {
            IIOPLogger.logDebugTransport(var12.getMessage());
            return;
         }

         var5.setUseCaches(false);
         InputStream var6 = null;

         try {
            var6 = var5.getInputStream();
            String var7 = var5.getHeaderField("WL-Result");
            if (var7 == null) {
               this.handleNullResponse(var5);
            }

            if (!var7.equals("OK")) {
               throw new ProtocolException("Tunneling result not OK, result: '" + var7 + "'");
            }

            String var8 = var5.getHeaderField("WL-Version");
            this.connectionID = var5.getHeaderField("Conn-Id");
            if (this.connectionID == null) {
               throw new ProtocolException("Tunneling could not ascertain a connection ID from the server");
            }

            TunnelUtils.readConnectionParams(new DataInputStream(var6));
            this.closed = false;
         } finally {
            TunnelUtils.drainStream(var6);
         }

         if (debugTransport.isEnabled()) {
            IIOPLogger.logDebugTransport("tunneled connect() succesful to host: '" + this.host + "' port: '" + var2 + "' connectionID: '" + this.connectionID + "'");
         }

         this.key = new ConnectionKey(var1.getHostAddress(), var2);
         this.channel = new ChannelImpl(this.host, var2, this.protocol.getProtocolName());
      }
   }

   public synchronized void run() {
      try {
         while(!this.closed) {
            this.receiveAndDispatch();
         }

      } catch (ThreadDeath var2) {
         throw var2;
      } catch (Throwable var3) {
         ConnectionManager.getConnectionManager().gotExceptionReceiving(this, var3);
         this.close();
      }
   }

   public final void send(AsyncOutgoingMessage var1) throws IOException {
      if (this.closed) {
         throw new IOException("Connection closed");
      } else {
         URL var2 = new URL(this.getProtocol().getProtocolName(), this.host, this.port, KernelStatus.getTunellingURL("/bea_wls_internal/iiop/ClientSend") + getTunellingURLExtension() + this.getRequestArgs());
         URLConnection var3 = this.createURLConnection(var2);
         var3.setUseCaches(false);
         InputStream var4 = null;

         try {
            var3.setDoOutput(true);
            OutputStream var5 = var3.getOutputStream();
            var1.writeTo(var5);
            var5.flush();
            var4 = var3.getInputStream();
            String var6 = var3.getHeaderField("WL-Result");

            try {
               if (var6 == null) {
                  this.handleNullResponse(var3);
               }

               if (!var6.equals("OK")) {
                  throw new ProtocolException("Tunneling result not OK, result: '" + var6 + "', id: '" + this.connectionID + "'");
               }
            } catch (ProtocolException var11) {
               if (debugTransport.isEnabled()) {
                  IIOPLogger.logDebugTransport("Problem sending tunneled message: '" + var1 + var11.getMessage());
               }

               throw var11;
            }
         } finally {
            TunnelUtils.drainStream(var4);
         }

      }
   }

   public final boolean isClosed() {
      return this.closed;
   }

   public final void close() {
      if (!this.isClosed()) {
         synchronized(this) {
            try {
               this.close0();
            } catch (IOException var4) {
               if (debugTransport.isEnabled()) {
                  IIOPLogger.logDebugTransport("Problem closing tunneled connection id: '" + this.connectionID + "'" + var4.getMessage());
               }
            }

         }
      }
   }

   private final void close0() throws IOException {
      if (!this.isClosed()) {
         this.closed = true;
         if (debugTransport.isEnabled()) {
            IIOPLogger.logDebugTransport("Closing tunneled connection id: '" + this.connectionID + "'");
         }

      }
   }

   private final synchronized void receiveAndDispatch() throws IOException {
      if (!this.closed) {
         InputStream var1 = null;
         String var2 = null;

         while(!this.closed) {
            URL var3 = new URL(this.getProtocol().getProtocolName(), this.host, this.port, KernelStatus.getTunellingURL("/bea_wls_internal/iiop/ClientRecv") + getTunellingURLExtension() + this.getRequestArgs());
            URLConnection var4 = this.createURLConnection(var3);
            var4.setUseCaches(false);
            var1 = var4.getInputStream();
            var2 = var4.getHeaderField("WL-Result");
            if (var2 == null) {
               this.handleNullResponse(var4);
            }

            if (var2.equals("RETRY")) {
               var1.close();
            } else {
               if (!var2.equals("OK")) {
                  throw new ProtocolException("Tunneling result not OK, result: '" + var2 + "', id: '" + this.connectionID + "'");
               }

               ConnectionManager.getConnectionManager().dispatch(this, readPacket(var1));
            }
         }

      }
   }

   public final EndPoint getEndPoint() {
      if (this.endPoint == null) {
         this.endPoint = new EndPointImpl(this, ConnectionManager.getConnectionManager());
      }

      return this.endPoint;
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
      return false;
   }

   private static final void p(String var0) {
      System.out.println("<HTTPClientConnection>: " + var0);
   }
}
