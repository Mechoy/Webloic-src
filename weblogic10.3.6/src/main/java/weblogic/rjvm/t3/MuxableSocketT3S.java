package weblogic.rjvm.t3;

import java.io.IOException;
import java.net.Socket;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLSocket;
import weblogic.kernel.KernelStatus;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.rjvm.JVMID;
import weblogic.security.SSL.CertCallback;
import weblogic.security.SSL.ClientCertificatePlugin;
import weblogic.security.SSL.SSLClientInfo;
import weblogic.security.utils.SSLCertUtility;
import weblogic.socket.ChannelSSLSocketFactory;
import weblogic.socket.SSLFilter;
import weblogic.socket.SocketMuxer;
import weblogic.utils.io.Chunk;

public final class MuxableSocketT3S extends MuxableSocketT3 {
   private static final long serialVersionUID = -1499853227078510946L;
   private static String clientCertPlugin;

   public MuxableSocketT3S(Chunk var1, Socket var2, ServerChannel var3) throws IOException {
      super(var1, var2, var3);
      if (ChannelHelper.isAdminChannel(var3)) {
         this.connection.setAdminQOS();
      }

   }

   void initializeClientCertPlugin(JVMID var1, ServerChannel var2) throws IOException {
      if (clientCertPlugin != null) {
         SSLClientInfo var3 = getClientCertificate(var1, var2);
         if (var3 != null) {
            ChannelSSLSocketFactory var4 = (ChannelSSLSocketFactory)this.socketFactory;
            var4.setSSLClientInfo(var3);
         }
      }

   }

   MuxableSocketT3S(ServerChannel var1) {
      super(var1);
      if (ChannelHelper.isAdminChannel(var1)) {
         this.connection.setAdminQOS();
      }

   }

   static SSLClientInfo getClientCertificate(JVMID var0, ServerChannel var1) {
      try {
         Class var2 = Class.forName(clientCertPlugin);
         ClientCertificatePlugin var3 = (ClientCertificatePlugin)var2.newInstance();
         CertCallback var4 = new CertCallback(SocketMuxer.getMuxer().isAsyncMuxer(), var1.getAddress(), var1.getPort(), var0.getPublicAddress(), var0.getPublicPort());
         var3.loadClientCertificate(var4, var0.getDomainName(), var0.getServerName());
         return var4.getSSLClientInfo();
      } catch (Throwable var5) {
         var5.printStackTrace();
         return null;
      }
   }

   public X509Certificate[] getJavaCertChain() {
      SSLSocket var1 = (SSLSocket)this.getSocket();

      try {
         return SSLCertUtility.getPeerCertChain(var1);
      } catch (Exception var3) {
         return null;
      }
   }

   public void ensureForceClose() {
      ((SSLFilter)this.getSocketFilter()).ensureForceClose();
   }

   static {
      if (KernelStatus.isServer()) {
         clientCertPlugin = System.getProperty("weblogic.security.SSL.ClientCertPlugin");
      }

   }
}
