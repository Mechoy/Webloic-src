package weblogic.iiop;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.AccessController;
import javax.security.auth.login.LoginException;
import org.omg.CORBA.portable.ObjectImpl;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.kernel.Kernel;
import weblogic.kernel.KernelStatus;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.AsyncMessageSenderImpl;
import weblogic.protocol.AsyncOutgoingMessage;
import weblogic.protocol.ChannelImpl;
import weblogic.protocol.MessageSender;
import weblogic.protocol.OutgoingMessage;
import weblogic.protocol.ProtocolHandlerAdmin;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelManager;
import weblogic.rmi.RMILogger;
import weblogic.rmi.spi.Channel;
import weblogic.security.SimpleCallbackHandler;
import weblogic.security.acl.DefaultUserInfoImpl;
import weblogic.security.acl.UserInfo;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.acl.internal.AuthenticatedUser;
import weblogic.security.auth.login.PasswordCredential;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.socket.AbstractMuxableSocket;
import weblogic.socket.SocketMuxer;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.io.Chunk;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManagerFactory;

class MuxableSocketIIOP extends AbstractMuxableSocket implements MessageSender, MessageHeaderConstants {
   protected static final int INITIAL_SO_TIMEOUT = 60000;
   private static final int CONNECT_TIMEOUT = Integer.getInteger("weblogic.iiop.connectTimeout", 0);
   private static final DebugCategory debugTransport = Debug.getCategory("weblogic.iiop.transport");
   private static final DebugCategory debugConnection = Debug.getCategory("weblogic.iiop.connection");
   private static final DebugLogger debugIIOPTransport = DebugLogger.getDebugLogger("DebugIIOPTransport");
   private static final DebugLogger debugIIOPConnection = DebugLogger.getDebugLogger("DebugIIOPConnection");
   private static final boolean DEBUG = false;
   private static final int CONNECT_MAX_RETRY = 1;
   private static final int BACKOFF_INTERVAL = Kernel.getConfig().getSocketReaderTimeoutMinMillis();
   private static boolean enabled = false;
   private boolean timeoutPingFailed = false;
   private AuthenticatedSubject subject = null;
   static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private final IIOPConnection connection = new IIOPConnection();
   private ConnectionKey key;
   private Channel channel;

   void setSubject(AuthenticatedSubject var1) {
      this.subject = var1;
   }

   protected AuthenticatedSubject getSubject() {
      return this.subject;
   }

   private AuthenticatedSubject authenticateLocally(UserInfo var1) {
      if (var1 instanceof DefaultUserInfoImpl) {
         DefaultUserInfoImpl var2 = (DefaultUserInfoImpl)var1;
         String var3 = var2.getName();
         String var4 = var2.getPassword();
         if (var3 != null && var3.length() != 0) {
            PrincipalAuthenticator var5 = Connection.getPrincipalAuthenticator();

            try {
               SimpleCallbackHandler var6 = new SimpleCallbackHandler(var3, var4);
               AuthenticatedSubject var7 = var5.authenticate(var6, this);
               PasswordCredential var8 = new PasswordCredential(var3, var4);
               var7.getPrivateCredentials(kernelId).add(var8);
               return var7;
            } catch (LoginException var9) {
               throw new SecurityException("User failed to be authenticated: " + var9.getMessage());
            }
         } else {
            return null;
         }
      } else {
         throw new SecurityException("Received bad UserInfo: " + var1.getClass().getName());
      }
   }

   protected static void p(String var0) {
      System.err.println("<MuxableSocketIIOP:" + System.currentTimeMillis() + "> " + var0);
   }

   public static void initialize() {
      if (!Kernel.isServer()) {
         enabled = true;
      } else {
         enabled = ManagementService.getRuntimeAccess(kernelId).getServer().isIIOPEnabled();
      }

   }

   static void disable() {
      enabled = false;
   }

   public static boolean isEnabled() {
      return enabled;
   }

   public MuxableSocketIIOP(Chunk var1, Socket var2, ServerChannel var3) throws IOException {
      super(var1, var2, var3);
      this.setSoTimeout(60000);
      this.key = new ConnectionKey(this.getSocket().getInetAddress().getHostAddress(), this.getSocket().getPort());
      this.channel = new ChannelImpl(this.getSocket().getInetAddress().getHostAddress(), this.getSocket().getPort(), ProtocolHandlerIIOP.PROTOCOL_IIOP.getProtocolName());
   }

   protected MuxableSocketIIOP(ServerChannel var1) {
      super(var1);
   }

   static MuxableSocketIIOP createConnection(InetAddress var0, int var1, String var2) throws IOException {
      ServerChannel var3 = null;
      if (KernelStatus.isServer() && kernelId.getQOS() == 103 && SecurityServiceManager.getCurrentSubject(kernelId) == kernelId) {
         if (ProtocolManager.getDefaultAdminProtocol().toByte() != ProtocolHandlerIIOP.PROTOCOL_IIOP.toByte()) {
            throw new IOException("Attempted to use IIOP as the admin protocol");
         }

         var3 = ServerChannelManager.findOutboundServerChannel(ProtocolHandlerAdmin.PROTOCOL_ADMIN, var2);
      } else {
         var3 = ServerChannelManager.findOutboundServerChannel(ProtocolHandlerIIOP.PROTOCOL_IIOP, var2);
      }

      if (debugConnection.isEnabled() || debugIIOPConnection.isDebugEnabled()) {
         IIOPLogger.logDebugConnection("Initiating connection to " + var0 + ":" + var1 + " on " + var3);
      }

      MuxableSocketIIOP var4 = new MuxableSocketIIOP(var3);
      var4.connect(var0, var1);
      SocketMuxer.getMuxer().register(var4);
      SocketMuxer.getMuxer().read(var4);
      if (debugConnection.isEnabled() || debugIIOPConnection.isDebugEnabled()) {
         IIOPLogger.logDebugConnection("Connected to " + var0 + ":" + var1);
      }

      return var4;
   }

   public final void connect(InetAddress var1, int var2) throws IOException, UnknownHostException {
      super.connect(var1, var2);
      this.setSoTimeout(60000);
      this.key = new ConnectionKey(var1.getHostAddress(), var2, this.getSocket().getLocalPort());
      this.channel = new ChannelImpl(var1.getHostAddress(), var2, ProtocolHandlerIIOP.PROTOCOL_IIOP.getProtocolName());
   }

   protected final Socket createSocket(InetAddress var1, int var2) throws IOException {
      int var3 = 0;

      while(true) {
         try {
            return this.newSocket(var1, var2);
         } catch (SocketException var7) {
            if (var3 == 1) {
               throw var7;
            }

            try {
               Thread.sleep((long)(Math.random() * (double)(BACKOFF_INTERVAL << var3)));
            } catch (InterruptedException var6) {
            }

            ++var3;
         }
      }
   }

   protected Socket newSocket(InetAddress var1, int var2) throws IOException {
      return super.createSocket(var1, var2, CONNECT_TIMEOUT);
   }

   public final int getIdleTimeoutMillis() {
      int var1 = super.getIdleTimeoutMillis();
      if (this.connection.hasPendingResponses()) {
         if (this.connection.getChannel().getTimeoutConnectionWithPendingResponses()) {
            var1 *= Kernel.getConfig().getIdlePeriodsUntilTimeout();
         } else {
            var1 = 0;
         }
      }

      Debug.assertion(var1 == 0 || var1 >= 1000);
      if (var1 == 0 && this.connection.getHeartbeatStub() != null && !Kernel.isServer()) {
         var1 = Kernel.getConfig().getPeriodLength();
      }

      return var1;
   }

   public final void dispatch(Chunk var1) {
      ConnectionManager.getConnectionManager().dispatch(this.connection, var1);
   }

   protected int getMessageLength() {
      int var1 = this.getHeaderByte(6) & 1;
      int var2 = this.getHeaderByte(8) & 255;
      int var3 = this.getHeaderByte(9) & 255;
      int var4 = this.getHeaderByte(10) & 255;
      int var5 = this.getHeaderByte(11) & 255;
      boolean var6 = false;
      int var7;
      if (var1 == 0) {
         var7 = (var2 << 24 | var3 << 16 | var4 << 8 | var5) + 12;
      } else {
         var7 = (var5 << 24 | var4 << 16 | var3 << 8 | var2) + 12;
      }

      Debug.assertion(var7 >= 0 && var7 < 134217728);
      return var7;
   }

   protected int getHeaderLength() {
      return 12;
   }

   public final void hasException(Throwable var1) {
      boolean var2 = false;
      if (this.getSocket() != null) {
         var2 = true;
      }

      if (var2) {
         new ConnectionShutdownHandler(this.connection, var1);
      }

   }

   public final void endOfStream() {
      boolean var1 = false;
      if (this.getSocket() != null) {
         var1 = true;
      }

      if (var1) {
         new ConnectionShutdownHandler(this.connection, new EOFException("endOfStream called by muxer"), false);
      }

   }

   public boolean isDead() {
      return !this.getSocketFilter().getSocketInfo().touch();
   }

   public final boolean timeout() {
      new ConnectionShutdownHandler(this.connection, new EOFException("Idle connection was timed out"));
      return false;
   }

   public final boolean requestTimeout() {
      if (this.connection.getHeartbeatStub() != null && !this.timeoutPingFailed) {
         WorkManagerFactory.getInstance().getSystem().schedule(new WorkAdapter() {
            public void run() {
               try {
                  MuxableSocketIIOP.this.timeoutPingFailed = true;
                  if (((ObjectImpl)MuxableSocketIIOP.this.connection.getHeartbeatStub())._non_existent()) {
                     RMILogger.logHeartbeatPeerClosed();
                  } else {
                     MuxableSocketIIOP.this.timeoutPingFailed = false;
                     if (MuxableSocketIIOP.debugTransport.isEnabled() || MuxableSocketIIOP.debugIIOPTransport.isDebugEnabled()) {
                        IIOPLogger.logDebugTransport("Heartbeat sent successfully to: " + MuxableSocketIIOP.this.connection.getHeartbeatStub());
                     }
                  }
               } catch (Throwable var2) {
                  RMILogger.logHeartbeatPeerClosed();
                  MuxableSocketIIOP.this.timeoutPingFailed = true;
               }

            }
         });
         return false;
      } else {
         return true;
      }
   }

   public String toString() {
      return super.toString() + ", key = " + this.key + ", raw socket = " + this.getSocket();
   }

   public Connection getConnection() {
      return this.connection;
   }

   public final void send(OutgoingMessage var1) throws IOException {
      OutputStream var2 = this.getSocketOutputStream();
      if (this.isClosed()) {
         throw new EOFException("Attempt to send message on closed socket");
      } else {
         try {
            this.getSocketFilter().getSocketInfo().touch();
         } catch (Throwable var4) {
         }

         var1.writeTo(var2);
      }
   }

   public void gotExceptionSending(OutgoingMessage var1, IOException var2) {
      ConnectionManager.getConnectionManager().handleExceptionSending(this.connection, var2);
   }

   protected final void cleanup() {
      super.cleanup();
      SocketMuxer.getMuxer().deliverEndOfStream(this.getSocketFilter());
      SocketMuxer.getMuxer().finishExceptionHandling(this.getSocketFilter());
   }

   public AuthenticatedSubject getUser() {
      return this.subject == null ? Connection.getDefaultSubject() : this.subject;
   }

   public void authenticate(UserInfo var1) throws SecurityException {
      if (var1 != null) {
         if (var1 instanceof AuthenticatedUser) {
            this.subject = SecurityServiceManager.getASFromAU((AuthenticatedUser)var1);
         } else {
            this.subject = this.authenticateLocally(var1);
         }

      }
   }

   protected boolean isSecure() {
      return false;
   }

   private class IIOPConnection extends Connection {
      private final AsyncMessageSenderImpl sender;
      private EndPoint endPoint;
      private Object txContext;

      private IIOPConnection() {
         this.txContext = null;
         this.sender = new AsyncMessageSenderImpl(MuxableSocketIIOP.this);
         MuxableSocketIIOP.this.addSenderStatistics(this.sender);
      }

      public final EndPoint getEndPoint() {
         if (this.endPoint == null) {
            synchronized(this) {
               if (this.endPoint == null) {
                  this.endPoint = new EndPointImpl(this, ConnectionManager.getConnectionManager());
               }
            }
         }

         return this.endPoint;
      }

      private boolean hasPendingResponses() {
         EndPoint var1 = this.getEndPoint();
         return var1 == null ? false : var1.hasPendingResponses();
      }

      public Object getTxContext() {
         return this.txContext;
      }

      public void setTxContext(Object var1) {
         this.txContext = var1;
      }

      public void authenticate(UserInfo var1) throws SecurityException {
         MuxableSocketIIOP.this.authenticate(var1);
      }

      public AuthenticatedSubject getUser() {
         return MuxableSocketIIOP.this.getUser();
      }

      public final void send(AsyncOutgoingMessage var1) throws IOException {
         this.sender.send(var1);
      }

      public final ServerChannel getChannel() {
         return MuxableSocketIIOP.this.getChannel();
      }

      public final boolean isClosed() {
         return MuxableSocketIIOP.this.isClosed();
      }

      public final void close() {
         MuxableSocketIIOP.this.close();
      }

      public final ConnectionKey getConnectionKey() {
         return MuxableSocketIIOP.this.key;
      }

      public final Channel getRemoteChannel() {
         return MuxableSocketIIOP.this.channel;
      }

      public final ContextHandler getContextHandler() {
         return MuxableSocketIIOP.this;
      }

      public final void setConnectionKey(ConnectionKey var1) {
         MuxableSocketIIOP.this.key = var1;
         MuxableSocketIIOP.this.channel = new ChannelImpl(MuxableSocketIIOP.this.key.getAddress(), MuxableSocketIIOP.this.key.getPort(), ProtocolHandlerIIOP.PROTOCOL_IIOP.getProtocolName());
      }

      protected boolean isSecure() {
         return MuxableSocketIIOP.this.isSecure();
      }

      // $FF: synthetic method
      IIOPConnection(Object var2) {
         this();
      }
   }
}
