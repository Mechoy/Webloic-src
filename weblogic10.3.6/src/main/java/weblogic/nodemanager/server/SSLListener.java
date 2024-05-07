package weblogic.nodemanager.server;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.Channel;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import weblogic.nodemanager.NodeManagerTextTextFormatter;
import weblogic.security.utils.SSLContextWrapper;
import weblogic.security.utils.SSLTrustValidator;

class SSLListener extends Listener {
   SSLContextWrapper sslContext;
   SSLConfig sslConfig;
   SSLSocketFactory cltFactory;
   private static final NodeManagerTextTextFormatter nmText = NodeManagerTextTextFormatter.getInstance();

   SSLListener(NMServer var1, Channel var2) throws IOException {
      super(var1, var2);
   }

   public void init() throws IOException {
      try {
         this.sslContext = SSLContextWrapper.getInstance();
         this.sslConfig = this.server.getSSLConfig();
      } catch (Exception var7) {
         throw (InternalError)(new InternalError("Could not instantiate SSLContextWrapper")).initCause(var7);
      }

      PrivateKey var3 = this.sslConfig.getIdentityPrivateKey();
      X509Certificate[] var4 = this.sslConfig.getIdentityCertificateChain();
      this.sslContext.addIdentity(var4, var3);
      SSLTrustValidator var5 = new SSLTrustValidator();
      var5.setPeerCertsRequired(false);
      var5.setAllowOverride(false);
      this.sslContext.setTrustManager(var5);
      this.sslContext.setExportRefreshCount(500);
      SSLServerSocketFactory var2 = this.sslContext.getSSLServerSocketFactory();
      if (this.inheritedChannel != null) {
         this.cltFactory = this.sslContext.getSSLSocketFactory();
      } else {
         SSLServerSocket var1;
         if (this.host != null) {
            var1 = (SSLServerSocket)var2.createServerSocket(this.port, this.backlog, this.host);
         } else {
            var1 = (SSLServerSocket)var2.createServerSocket(this.port, this.backlog);
         }

         String var6 = this.sslConfig.getCipherSuite();
         if (var6 != null) {
            var1.setEnabledCipherSuites(new String[]{var6});
            if (!var1.getEnabledCipherSuites()[0].equals(var6)) {
               throw new IOException(nmText.getUnsupportedCipher(var6));
            }
         }

         var1.setNeedClientAuth(false);
         this.serverSocket = var1;
      }
   }

   public void run() throws IOException {
      String var1 = this.host != null ? nmText.getSecureSocketListenerHost(Integer.toString(this.port), this.host.toString()) : nmText.getSecureSocketListener(Integer.toString(this.port));
      NMServer.nmLog.info(var1);

      while(true) {
         while(true) {
            try {
               Object var2;
               if ((var2 = this.serverSocket.accept()) != null) {
                  if (this.inheritedChannel != null) {
                     SSLSocket var3 = (SSLSocket)this.cltFactory.createSocket((Socket)var2, ((Socket)var2).getInetAddress().getHostName(), ((Socket)var2).getLocalPort(), true);
                     NMServer.nmLog.info(nmText.upgradeToSecure());
                     String var4 = this.sslConfig.getCipherSuite();
                     if (var4 != null) {
                        var3.setEnabledCipherSuites(new String[]{var4});
                        if (!var3.getEnabledCipherSuites()[0].equals(var4)) {
                           throw new IOException(nmText.getUnsupportedCipher(var4));
                        }
                     }

                     var3.setUseClientMode(false);
                     var3.setNeedClientAuth(false);

                     try {
                        var3.startHandshake();
                     } catch (IOException var8) {
                        if (!var3.isClosed()) {
                           try {
                              var3.close();
                           } catch (IOException var7) {
                           }
                        }

                        throw var8;
                     }

                     var2 = var3;
                  }

                  Handler var10 = new Handler(this.server, (Socket)var2);
                  Thread var11 = new Thread(var10);
                  var11.start();
               }
            } catch (IOException var9) {
               NMServer.nmLog.warning(nmText.getFailedSecureConnection(Integer.toString(this.port), this.host.toString()) + "" + var9);
            }
         }
      }
   }
}
