package weblogic.server.channels;

import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.security.AccessController;
import weblogic.kernel.T3SrvrLogger;
import weblogic.management.configuration.SSLMBean;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.ServerChannel;
import weblogic.security.SSL.SSLEngineFactory;
import weblogic.security.SSL.WeblogicSSLEngine;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.utils.SSLCipherUtility;
import weblogic.security.utils.SSLContextManager;
import weblogic.security.utils.SSLSetup;
import weblogic.socket.JSSEFilterImpl;
import weblogic.socket.JSSESocket;
import weblogic.socket.MuxableSocketDiscriminator;
import weblogic.socket.SocketMuxer;

final class DynamicJSSEListenThread extends DynamicListenThread {
   private static final String SSL_LISTEN_THREAD_NAME = "DynamicJSSEListenThread";
   private static final String ADMIN_LISTEN_THREAD_NAME = "AdminJSSEListenThread";
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private final SSLEngineFactory sslEngineFactory;

   DynamicJSSEListenThread(ServerChannel[] var1, DynamicListenThreadManager var2) throws IOException {
      super(var1, var2);
      this.port = var1[0].getPort();

      try {
         this.sslEngineFactory = SSLContextManager.getSSLEngineFactory(var1[0], kernelId);
      } catch (Exception var4) {
         T3SrvrLogger.logInconsistentSecurityConfig(var4);
         throw (IOException)(new IOException(var4.getMessage())).initCause(var4);
      }

      this.loginTimeout = var1[0].getLoginTimeoutMillis();
   }

   final String getName() {
      return "DynamicJSSEListenThread[" + this.getChannelName() + "]";
   }

   public void registerSocket(Socket var1) {
      try {
         var1.setSoTimeout(this.loginTimeout);
         WeblogicSSLEngine var2 = this.sslEngineFactory.createSSLEngine(var1.getInetAddress().getHostAddress(), var1.getPort());
         var2.setUseClientMode(false);
         SSLMBean var3 = ManagementService.getRuntimeAccess(kernelId).getServer().getSSL();
         String[] var4 = var3.getCiphersuites();
         var4 = SSLCipherUtility.removeNullCipherSuites(var4);
         if (var4 != null && var4.length > 0) {
            var2.setEnabledCipherSuites(var4);
         }

         if (SSLSetup.isDebugEnabled(3)) {
            String[] var5 = var2.getEnabledCipherSuites();
            SSLSetup.info(this.getName() + " " + var5.length + " cipher suites enabled:");

            for(int var6 = 0; var6 < var5.length; ++var6) {
               SSLSetup.info(var5[var6]);
            }
         }

         var2.setWantClientAuth(this.channels[0].isTwoWaySSLEnabled());
         if (this.channels[0].isTwoWaySSLEnabled() && this.channels[0].isClientCertificateEnforced()) {
            var2.setNeedClientAuth(this.channels[0].isClientCertificateEnforced());
         } else if (!this.channels[0].isTwoWaySSLEnabled() && this.channels[0].isClientCertificateEnforced()) {
            T3SrvrLogger.logInconsistentSSLConfig();
         }

         JSSEFilterImpl var11 = new JSSEFilterImpl(var1, var2);
         JSSESocket var12 = new JSSESocket(var1, var11);
         MuxableSocketDiscriminator var7 = new MuxableSocketDiscriminator(var12, this.handlers, this.channels);
         var11.setDelegate(var7);
         var7.setSocketFilter(var11);
         SocketMuxer.getMuxer().register(var11);
         SocketMuxer.getMuxer().read(var11);
      } catch (InterruptedIOException var8) {
         this.rejectCatastrophe(var1, "Login timed out after: '" + this.loginTimeout + "' ms on socket: '" + socketInfo(var1) + "'", var8);
      } catch (EOFException var9) {
         this.rejectCatastrophe(var1, "Client closed socket '" + socketInfo(var1) + "' before completing connection.", var9);
      } catch (IOException var10) {
         this.rejectCatastrophe(var1, "Unable to read from socket: '" + socketInfo(var1) + "'", var10);
      }

   }
}
