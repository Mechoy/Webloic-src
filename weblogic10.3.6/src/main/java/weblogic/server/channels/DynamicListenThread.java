package weblogic.server.channels;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ProtocolException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import weblogic.kernel.KernelStatus;
import weblogic.logging.Loggable;
import weblogic.management.configuration.UnixMachineMBean;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.ProtocolHandler;
import weblogic.protocol.ServerChannel;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.server.ServerLogger;
import weblogic.socket.Login;
import weblogic.socket.MuxableSocketDiscriminator;
import weblogic.socket.ServerSocketMuxer;
import weblogic.socket.SocketOptionException;
import weblogic.t3.srvr.ServerServicesManager;
import weblogic.t3.srvr.SetUIDRendezvous;
import weblogic.t3.srvr.T3Srvr;
import weblogic.time.common.internal.TimeEventGenerator;
import weblogic.utils.Debug;

public class DynamicListenThread implements Runnable {
   private static final String LISTEN_THREAD_NAME = "DynamicListenThread";
   private static final byte[] UNAVAIL_RESPONSE = "HTTP/1.0 503 Unavailable\r\nWL-Result: UNAVAIL\r\nContent-Type: text/html\r\n\r\n<TITLE>503 Unavailable</TITLE>The Server is not able to service this request: <b>".getBytes();
   private static final boolean DEBUG = false;
   private final SocketAccepter accepter = new SocketAccepter();
   private final DynamicListenThreadManager manager;
   protected int port;
   protected boolean managed;
   private static final int PRIVILEDGED_PORT_HWM = 1024;
   private InetAddress listenAddress = null;
   private boolean isAdminChannel;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   protected ProtocolHandler[] handlers;
   protected ServerChannel[] channels;
   private volatile boolean bindingDone = false;
   private volatile boolean bindingFail = false;
   private volatile boolean shutdown = false;
   protected int loginTimeout;
   private static HashMap boundErrorTable = new HashMap();

   protected final InetAddress getListenAddress() {
      return this.listenAddress;
   }

   final boolean isBindingDone() {
      return this.bindingDone;
   }

   final boolean isBindingFail() {
      return this.bindingFail;
   }

   DynamicListenThread(ServerChannel[] var1, DynamicListenThreadManager var2) {
      Debug.assertion(var1 != null && var1.length > 0);
      this.channels = var1;
      this.handlers = new ProtocolHandler[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         this.handlers[var3] = var1[var3].getProtocol().getHandler();
      }

      this.listenAddress = var1[0].getInetAddress();
      this.loginTimeout = var1[0].getLoginTimeoutMillis();
      this.manager = var2;
      this.port = this.channels[0].getPort();
   }

   public static DynamicListenThread createClientListener(ServerChannel var0) {
      if (KernelStatus.isServer()) {
         throw new IllegalStateException("createClientListener() called in a server");
      } else {
         DynamicListenThread var1 = new DynamicListenThread(new ServerChannel[]{var0}, (DynamicListenThreadManager)null);
         var1.start(true, false, false);
         return var1;
      }
   }

   protected ServerSocket newServerSocket(int var1) throws IOException {
      ServerSocket var2 = null;
      if (this.channels[0].isSDPEnabled()) {
         var2 = ServerSocketMuxer.getMuxer().newSDPServerSocket(this.listenAddress, var1, this.channels[0].getAcceptBacklog(), this.isAdminChannel);
      } else {
         var2 = ServerSocketMuxer.getMuxer().newServerSocket(this.listenAddress, var1, this.channels[0].getAcceptBacklog(), this.isAdminChannel);
      }

      ServerSocket var3 = (ServerSocket)var2;
      return var3;
   }

   String getName() {
      return "DynamicListenThread[" + this.getChannelName() + "]";
   }

   protected String getChannelName() {
      return ((ServerChannelImpl)this.channels[0]).getRealName();
   }

   String getKey() {
      return this.channels[0].getListenerKey();
   }

   public final void run() {
      SecurityServiceManager.pushSubject(kernelId, kernelId);

      try {
         this.accepter.open();

         while(true) {
            Socket var1;
            do {
               var1 = this.accepter.accept();
            } while(KernelStatus.isServer() && !this.manager.checkDumpThreads(var1));

            if (this.accepter.isSuspended()) {
               this.rejectSuspendedServer(var1);
            } else {
               this.registerSocket(var1);
            }
         }
      } catch (SocketException var7) {
         this.bindingFail = true;
         if (!this.shutdown) {
            ServerLogger.logUnableToCreateSocket(this.listenAddress == null ? "IP_ANY" : this.listenAddress.getHostAddress(), this.port, var7, this.getChannelName());
         }
      } catch (Throwable var8) {
         if (!this.shutdown) {
            ServerLogger.logListenThreadFailure(var8);
         }
      } finally {
         if (this.shutdown) {
            ServerLogger.logChannelClosed(this.listenAddress == null ? "IP_ANY" : this.listenAddress.getHostAddress(), this.port, this.getChannelName());
         }

         if (this.accepter.isOpen()) {
            this.bindingDone = false;
            this.accepter.close();
         }

         SecurityServiceManager.popSubject(kernelId);
      }

   }

   public boolean start() {
      return this.start(true, true, false);
   }

   public boolean start(boolean var1, boolean var2, boolean var3) {
      this.managed = var2;
      ServerThread var4;
      if (this.manager == null) {
         var4 = new ServerThread(this, this.getName());
         var4.setDaemon(true);
         var4.start();
         var4.setPriority(9);
      } else {
         var4 = new ServerThread(this.manager.getThreadGroup(), this, this.getName());
         var4.setPriority(9);
         var4.start();
      }

      if (var2) {
         this.manager.addListener(this);
      }

      int var9 = var1 ? 1000 * (KernelStatus.isServer() ? ManagementService.getRuntimeAccess(kernelId).getServer().getListenThreadStartDelaySecs() : 60) : 0;

      while(var9 > 0 && !this.isBindingDone() && !this.isBindingFail()) {
         try {
            synchronized(this) {
               this.wait(100L);
            }

            var9 -= 100;
         } catch (InterruptedException var8) {
         }
      }

      if (!var1 || this.isBindingDone() && !this.isBindingFail()) {
         if (!var3) {
            this.enable();
         }

         return true;
      } else {
         ServerLogger.logChannelHung(this.listenAddress == null ? "IP_ANY" : this.listenAddress.getHostAddress(), this.port, this.getChannelName());
         return false;
      }
   }

   boolean enable() {
      return this.accepter.enable();
   }

   protected final void rejectCatastrophe(Socket var1, String var2, IOException var3) {
      if (ManagementService.getRuntimeAccess(kernelId).getServer().getServerDebug().getListenThreadDebug()) {
         ServerLogger.logDebugThreadException(var2, var3);
      }

      try {
         var1.close();
      } catch (IOException var5) {
      }

   }

   protected final void rejectSuspendedServer(Socket var1) {
      ServerServicesManager.StartupSnapshot var2 = ServerServicesManager.getStartupSnapshot();
      Loggable var3 = ServerLogger.logServerUnavailableLoggable(T3Srvr.getT3Srvr().getState(), var2.getCurrentServiceName(), var2.getCurrentServiceIndex(), ServerServicesManager.getServicesCount(), var2.getPreviousServiceName(), var2.getPreviousServiceStartupTime());

      try {
         InputStream var4 = var1.getInputStream();
         int var5 = var4.read();
         int var6 = var4.read();
         int var7 = var4.read();
         int var8 = var4.read();
         if ((var5 != 71 || var6 != 69 || var7 != 84) && (var5 != 80 || var6 != 79 || var7 != 83 || var8 != 84)) {
            Login.connectReply(var1, 3, var3.getMessage());
         } else {
            var1.getOutputStream().write(UNAVAIL_RESPONSE);
            var1.getOutputStream().write(var3.getMessage().getBytes());
            var1.getOutputStream().write(Login.UNAVAIL_END);
         }

         var1.close();
      } catch (IOException var9) {
      }

      var3.log();
   }

   public final void stop() {
      this.bindingDone = false;
      this.shutdown = true;
      synchronized(this) {
         this.notifyAll();
      }

      this.accepter.close();
      if (this.managed) {
         this.manager.removeListener(this);
      }

      if (this.isAdminChannel) {
         AdminPortService.getInstance().removeListener(this);
      }

   }

   final void unmanage() {
      this.managed = false;
   }

   public void registerSocket(Socket var1) {
      try {
         var1.setSoTimeout(this.loginTimeout);
         MuxableSocketDiscriminator var2 = new MuxableSocketDiscriminator(var1, this.handlers, this.channels);
         ServerSocketMuxer.getMuxer().register(var2);
         ServerSocketMuxer.getMuxer().read(var2);
      } catch (IOException var3) {
         this.rejectCatastrophe(var1, "Can't Read from socket: '" + socketInfo(var1) + "'", var3);
      }

   }

   protected static final String socketInfo(Socket var0) {
      if (var0 == null) {
         return "null";
      } else {
         InetAddress var1 = var0.getInetAddress();
         return var0.toString() + " - address: '" + (var1 != null ? var1.getHostAddress() : null) + "', port: '" + var0.getPort() + "', localport: '" + var0.getLocalPort() + "'";
      }
   }

   private static synchronized void checkForMultipleAddressBind(String var0) throws UnknownHostException {
      if (var0 != null && boundErrorTable.get(var0) == null) {
         InetAddress[] var1 = InetAddress.getAllByName(var0);
         if (var1.length > 1) {
            StringBuffer var2 = new StringBuffer();

            for(int var3 = 0; var3 < var1.length; ++var3) {
               var2.append(var1[var3].getHostAddress());
               if (var3 != var1.length - 1) {
                  var2.append(", ");
               }
            }

            String var4 = var2.toString();
            ServerLogger.logHostMapsToMultipleAddress(var0, var4);
         }

         boundErrorTable.put(var0, var0);
      }
   }

   void setAdminChannel(boolean var1) {
      this.isAdminChannel = var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < this.channels.length; ++var2) {
         var1.append(this.channels[var2].toString()).append(", ");
      }

      return var1.toString();
   }

   private final class SocketAccepter {
      private static final int PER_FAILURE_BACKOFF = 1000;
      private ServerSocket acceptSocket;
      private volatile boolean listening;
      private volatile boolean suspended;
      private int failures;
      private long lastFailure;

      private SocketAccepter() {
         this.suspended = true;
      }

      private void open() throws IOException {
         try {
            this.acceptSocket = this.createServerSocket(DynamicListenThread.this.port);
            DynamicListenThread.this.bindingDone = true;
         } catch (IOException var6) {
            DynamicListenThread.this.bindingFail = true;
            throw var6;
         } finally {
            if (DynamicListenThread.this.managed) {
               DynamicListenThread.this.manager.waitForBinding();
            }

         }

         if (KernelStatus.isServer()) {
            String var1 = ManagementService.getRuntimeAccess(DynamicListenThread.kernelId).getServer().getListenAddress();
            if (var1 == null || var1.equals("")) {
               DynamicListenThread.checkForMultipleAddressBind(this.acceptSocket.getInetAddress().getHostName());
            }
         }

         this.listening = true;
         if (!this.suspended) {
            this.enable();
         }

      }

      private boolean isThrottlingEnabled() {
         if (DynamicListenThread.this.isAdminChannel) {
         }

         return true;
      }

      private boolean isOpen() {
         return this.listening;
      }

      private boolean isSuspended() {
         return this.suspended;
      }

      private synchronized boolean enable() {
         if (!this.suspended) {
            return true;
         } else if (!DynamicListenThread.this.isBindingDone()) {
            return false;
         } else {
            this.suspended = false;
            StringBuffer var1 = new StringBuffer();

            for(int var2 = 0; var2 < DynamicListenThread.this.channels.length; ++var2) {
               if (var2 != 0) {
                  var1.append(", ");
               }

               var1.append(DynamicListenThread.this.channels[var2].getProtocol().getProtocolName());
            }

            if (DynamicListenThread.this.channels[0].isSDPEnabled()) {
               var1.append(" using SDP for I/O");
            }

            ServerLogger.logChannelOpen(DynamicListenThread.this.listenAddress == null ? "IP_ANY" : DynamicListenThread.this.listenAddress.getHostAddress(), DynamicListenThread.this.port, DynamicListenThread.this.getChannelName(), var1.toString());
            this.notifyAll();
            return true;
         }
      }

      private void close() {
         if (this.acceptSocket != null) {
            try {
               this.suspended = true;
               this.listening = false;
               this.acceptSocket.close();
            } catch (IOException var2) {
            }
         }

      }

      private Socket accept() throws IOException {
         boolean var1 = false;

         while(true) {
            try {
               if (this.acceptSocket == null) {
                  ServerLogger.logChannelReopening(DynamicListenThread.this.listenAddress == null ? "IP_ANY" : DynamicListenThread.this.listenAddress.getHostAddress(), DynamicListenThread.this.port, DynamicListenThread.this.getChannelName());
                  this.acceptSocket = this.createServerSocket(DynamicListenThread.this.port);
                  if (var1) {
                     this.suspended = false;
                     var1 = false;
                     this.listening = true;
                  }
               }

               if (this.isThrottlingEnabled()) {
                  ServerThrottle.getServerThrottle().acquireSocketPermit();
               }

               Socket var2 = this.acceptSocket.accept();
               this.onAccept();
               return var2;
            } catch (SocketOptionException var3) {
            } catch (ProtocolException var4) {
            } catch (IOException var5) {
               if (!this.listening) {
                  throw var5;
               }

               var1 = this.onAcceptException(var5);
            }
         }
      }

      private ServerSocket createServerSocket(final int var1) throws IOException {
         PrivilegedAction var2 = new PrivilegedAction() {
            public Object run() {
               try {
                  SocketAccepter.this.acceptSocket = DynamicListenThread.this.newServerSocket(var1);
                  return null;
               } catch (IOException var2) {
                  return var2;
               }
            }
         };
         IOException var3 = null;
         if (KernelStatus.isServer() && var1 <= 1024) {
            var3 = (IOException)SetUIDRendezvous.doPrivileged(var2);
         } else {
            var3 = (IOException)var2.run();
         }

         if (var3 != null) {
            throw var3;
         } else {
            return this.acceptSocket;
         }
      }

      private void onAccept() {
         if (this.failures > 0) {
            this.failures = 0;
            ServerLogger.logChannelRestored(DynamicListenThread.this.listenAddress == null ? "IP_ANY" : DynamicListenThread.this.listenAddress.getHostAddress(), DynamicListenThread.this.port, DynamicListenThread.this.getChannelName());
         }

      }

      private boolean onAcceptException(IOException var1) {
         boolean var2 = false;
         long var3 = TimeEventGenerator.getCurrentMillis();
         if (this.failures == 0) {
            this.lastFailure = var3;
         }

         long var5 = var3 - this.lastFailure;
         ++this.failures;
         ServerLogger.logChannelFailed(DynamicListenThread.this.listenAddress == null ? "IP_ANY" : DynamicListenThread.this.listenAddress.getHostAddress(), DynamicListenThread.this.port, DynamicListenThread.this.getChannelName(), this.failures, var5 / 1000L, var1);
         int var7 = 1000 * this.failures;
         if (var7 < DynamicListenThread.this.channels[0].getMaxBackoffBetweenFailures()) {
            try {
               synchronized(this) {
                  this.wait((long)var7);
               }
            } catch (InterruptedException var14) {
            }
         } else {
            boolean var8 = false;
            if (DynamicListenThread.this.port > 0 && DynamicListenThread.this.port < 1024 && ManagementService.getRuntimeAccess(DynamicListenThread.kernelId).getServer().getMachine() instanceof UnixMachineMBean) {
               UnixMachineMBean var9 = (UnixMachineMBean)ManagementService.getRuntimeAccess(DynamicListenThread.kernelId).getServer().getMachine();
               var8 = var9.isPostBindGIDEnabled() || var9.isPostBindUIDEnabled();
            }

            if (!var8) {
               ServerLogger.logCloseAndReopenChannel(DynamicListenThread.this.listenAddress == null ? "IP_ANY" : DynamicListenThread.this.listenAddress.getHostAddress(), DynamicListenThread.this.port, DynamicListenThread.this.getChannelName());
               this.close();
               this.acceptSocket = null;
               var2 = true;
            }

            try {
               synchronized(this) {
                  this.wait((long)DynamicListenThread.this.channels[0].getMaxBackoffBetweenFailures());
               }
            } catch (InterruptedException var12) {
            }
         }

         return var2;
      }

      // $FF: synthetic method
      SocketAccepter(Object var2) {
         this();
      }
   }
}
