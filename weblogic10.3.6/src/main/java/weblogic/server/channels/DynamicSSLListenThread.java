package weblogic.server.channels;

import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.AccessController;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import weblogic.kernel.T3SrvrLogger;
import weblogic.management.configuration.SSLMBean;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.utils.SSLCipherUtility;
import weblogic.security.utils.SSLContextManager;
import weblogic.security.utils.SSLIOContext;
import weblogic.security.utils.SSLIOContextTable;
import weblogic.security.utils.SSLSetup;
import weblogic.socket.MuxableSocketDiscriminator;
import weblogic.socket.SSLFilter;
import weblogic.socket.SocketMuxer;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManagerFactory;

final class DynamicSSLListenThread extends DynamicListenThread {
   private static final String SSL_LISTEN_THREAD_NAME = "DynamicSSLListenThread";
   private static final String ADMIN_LISTEN_THREAD_NAME = "AdminListenThread";
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private SSLServerSocketFactory serverFactory = null;

   DynamicSSLListenThread(ServerChannel[] var1, DynamicListenThreadManager var2) throws IOException {
      super(var1, var2);
      this.port = var1[0].getPort();

      try {
         if (SocketMuxer.getMuxer().isAsyncMuxer()) {
            this.serverFactory = SSLContextManager.getSSLNioServerSocketFactory(var1[0], kernelId);
         } else {
            this.serverFactory = SSLContextManager.getSSLServerSocketFactory(var1[0], kernelId);
         }
      } catch (Exception var4) {
         T3SrvrLogger.logInconsistentSecurityConfig(var4);
         throw (IOException)(new IOException(var4.getMessage())).initCause(var4);
      }

      this.loginTimeout = var1[0].getLoginTimeoutMillis();
   }

   final String getName() {
      return "DynamicSSLListenThread[" + this.getChannelName() + "]";
   }

   protected ServerSocket newServerSocket(int var1) throws IOException {
      int var2 = ManagementService.getRuntimeAccess(kernelId).getServer().getAcceptBacklog();
      SSLServerSocket var3 = (SSLServerSocket)this.serverFactory.createServerSocket(var1, var2, this.getListenAddress());
      SSLMBean var4 = ManagementService.getRuntimeAccess(kernelId).getServer().getSSL();
      String[] var5 = var4.getCiphersuites();
      SSLCipherUtility.normalizeNames(var5);
      var5 = SSLCipherUtility.removeNullCipherSuites(var5);
      if (var5 != null && var5.length > 0) {
         var3.setEnabledCipherSuites(var5);
      }

      if (SSLSetup.isDebugEnabled(3)) {
         String[] var6 = var3.getEnabledCipherSuites();
         SSLSetup.info(this.getName() + " " + var6.length + " cipher suites enabled:");

         for(int var7 = 0; var7 < var6.length; ++var7) {
            SSLSetup.info(var6[var7]);
         }
      }

      var3.setNeedClientAuth(this.channels[0].isTwoWaySSLEnabled() || this.channels[0].isClientCertificateEnforced());
      return var3;
   }

   public void registerSocket(Socket var1) {
      SSLIOContextTable.registerForThrottling((SSLSocket)var1);
      final SSLSocket var2 = (SSLSocket)var1;
      final long var3 = (long)this.loginTimeout;
      WorkAdapter var5 = new WorkAdapter() {
         final SSLSocket theSock = var2;
         final long timeout = var3;

         public void run() {
            try {
               this.theSock.setSoTimeout(DynamicSSLListenThread.this.loginTimeout);
               MuxableSocketDiscriminator var1 = new MuxableSocketDiscriminator(this.theSock, DynamicSSLListenThread.this.handlers, DynamicSSLListenThread.this.channels);
               var2.startHandshake();
               SSLIOContext var2x = SSLIOContextTable.findContext(this.theSock);
               SSLFilter var3x = (SSLFilter)var2x.getFilter();
               var3x.setDelegate(var1);
               var3x.activateNoRegister();
               var1.setSocketFilter(var3x);
               SocketMuxer.getMuxer().register(var3x);
               SocketMuxer.getMuxer().read(var3x);
            } catch (InterruptedIOException var4) {
               DynamicSSLListenThread.this.rejectCatastrophe(var2, "Login timed out after: '" + this.timeout + "' ms on socket: '" + DynamicListenThread.socketInfo(var2) + "'", var4);
            } catch (EOFException var5) {
               DynamicSSLListenThread.this.rejectCatastrophe(var2, "Client closed socket '" + DynamicListenThread.socketInfo(var2) + "' before completing connection.", var5);
            } catch (IOException var6) {
               DynamicSSLListenThread.this.rejectCatastrophe(var2, "Unable to read from socket: '" + DynamicListenThread.socketInfo(var2) + "'", var6);
            }

         }
      };
      if (ChannelHelper.isAdminChannel(this.channels[0])) {
         WorkManagerFactory.getInstance().getSystem().schedule(var5);
      } else {
         WorkManagerFactory.getInstance().getDefault().schedule(var5);
      }

   }
}
