package weblogic.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.AccessController;
import weblogic.kernel.KernelStatus;
import weblogic.protocol.ServerChannel;
import weblogic.security.SSL.SSLClientInfo;
import weblogic.security.SSL.SSLSocketFactory;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.acl.internal.Security;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.utils.SSLContextManager;
import weblogic.security.utils.SSLSetup;

public final class ChannelSSLSocketFactory extends SSLSocketFactory {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private ServerChannel channel;
   private SSLClientInfo sslInfo;

   public ChannelSSLSocketFactory(ServerChannel var1) {
      super((javax.net.ssl.SSLSocketFactory)null);
      if (var1 == null) {
         throw new IllegalArgumentException("Channel must not be null");
      } else {
         this.channel = var1;
      }
   }

   public Socket createSocket(String var1, int var2) throws IOException, UnknownHostException {
      return this.createSocket(InetAddress.getByName(var1), var2);
   }

   public SSLSocketFactory initializeFromThread() throws IOException {
      this.sslInfo = this.createSSLClientInfo();
      return this;
   }

   public Socket createSocket(String var1, int var2, InetAddress var3, int var4) {
      throw new UnsupportedOperationException("Binding characteristics are determined by the channel");
   }

   public Socket createSocket(InetAddress var1, int var2) throws IOException {
      javax.net.ssl.SSLSocketFactory var3 = this.getSocketFactory();
      if (KernelStatus.isServer() && this.channel.isOutboundEnabled()) {
         return var3.createSocket(var1, var2, this.getInetAddressFor(this.channel.getAddress()), 0);
      } else {
         Socket var4 = SocketMuxer.getMuxer().newSSLClientSocket(var1, var2);
         return var4 != null ? var3.createSocket(var4, var1.getHostAddress(), var2, true) : var3.createSocket(var1, var2);
      }
   }

   public Socket createSocket(InetAddress var1, int var2, InetAddress var3, int var4) {
      throw new UnsupportedOperationException("Binding characteristics are determined by the channel");
   }

   public Socket createSocket(InetAddress var1, int var2, int var3) throws IOException {
      int var4 = var3 > 0 ? var3 : this.channel.getConnectTimeout() * 1000;
      if (var4 == 0) {
         return this.createSocket(var1, var2);
      } else {
         Socket var5;
         if (KernelStatus.isServer() && this.channel.isOutboundEnabled()) {
            if (this.channel.getProxyAddress() != null) {
               var5 = SocketMuxer.getMuxer().newProxySocket(var1, var2, this.getInetAddressFor(this.channel.getAddress()), 0, InetAddress.getByName(this.channel.getProxyAddress()), this.channel.getProxyPort(), var4);
            } else {
               var5 = SocketMuxer.getMuxer().newSocket(var1, var2, this.getInetAddressFor(this.channel.getAddress()), 0, var4);
            }
         } else {
            var5 = SocketMuxer.getMuxer().newSSLClientSocket(var1, var2, var4);
            if (var5 == null) {
               var5 = SocketMuxer.getMuxer().newSocket(var1, var2, var4);
            }
         }

         return this.createSocket(var5, var1.getHostName(), var2, true);
      }
   }

   private InetAddress getInetAddressFor(String var1) throws UnknownHostException {
      return var1 != null ? InetAddress.getByName(var1) : (InetAddress)null;
   }

   public String[] getDefaultCipherSuites() {
      try {
         return this.getSocketFactory().getDefaultCipherSuites();
      } catch (IOException var2) {
         throw (RuntimeException)(new IllegalStateException()).initCause(var2);
      }
   }

   public String[] getSupportedCipherSuites() {
      try {
         return this.getSocketFactory().getSupportedCipherSuites();
      } catch (IOException var2) {
         throw (RuntimeException)(new IllegalStateException()).initCause(var2);
      }
   }

   public Socket createSocket(Socket var1, String var2, int var3, boolean var4) throws IOException {
      return this.getSocketFactory().createSocket(var1, var2, var3, var4);
   }

   private javax.net.ssl.SSLSocketFactory getSocketFactory() throws IOException {
      if (this.sslInfo == null) {
         this.sslInfo = this.createSSLClientInfo();
         this.sslInfo.setNio(SocketMuxer.getMuxer().isAsyncMuxer());
      }

      return this.sslInfo.getSSLSocketFactory();
   }

   public SSLClientInfo getSSLClientInfo() {
      return this.sslInfo;
   }

   private SSLClientInfo createSSLClientInfo() throws IOException {
      SSLClientInfo var1 = Security.getThreadSSLClientInfo();
      if ((!KernelStatus.isServer() || var1 != null && !var1.isEmpty() || kernelId != SecurityServiceManager.getCurrentSubject(kernelId)) && (!this.channel.isOutboundEnabled() || !this.channel.isOutboundPrivateKeyEnabled())) {
         return var1;
      } else {
         try {
            return SSLContextManager.getChannelSSLClientInfo(this.channel, kernelId);
         } catch (Exception var3) {
            throw (IOException)(new IOException(var3.getMessage())).initCause(var3);
         }
      }
   }

   public void setSSLClientInfo(SSLClientInfo var1) {
      try {
         if (SocketMuxer.getMuxer().isAsyncMuxer()) {
            if (var1 != null && !var1.isNioSet()) {
               var1.setNio(true);
            }

            this.jsseFactory = var1 == null ? SSLSetup.getSSLContext(var1).getSSLNioSocketFactory() : var1.getSSLSocketFactory();
         } else {
            this.jsseFactory = var1 == null ? SSLSetup.getSSLContext(var1).getSSLSocketFactory() : var1.getSSLSocketFactory();
         }

         this.sslInfo = var1;
      } catch (SocketException var3) {
         SSLSetup.debug(3, var3, "Failed to create context");
         throw new RuntimeException("Failed to update factory: " + var3.getMessage());
      }
   }
}
