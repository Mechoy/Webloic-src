package weblogic.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSocketFactory;
import weblogic.security.SSL.SSLEngineFactory;
import weblogic.security.SSL.WeblogicSSLEngine;

public class JSSESocketFactory extends SSLSocketFactory {
   private static boolean isMuxerReady;
   private final SSLEngineFactory sslEngineFactory;

   public static JSSESocketFactory getSSLSocketFactory(SSLEngineFactory var0) {
      return new JSSESocketFactory(var0);
   }

   private JSSESocketFactory(SSLEngineFactory var1) {
      this.sslEngineFactory = var1;
   }

   public Socket createSocket(Socket var1, String var2, int var3, boolean var4) throws IOException {
      SSLEngine var5 = this.getSSLEngine(var1);
      JSSEFilterImpl var6 = new JSSEFilterImpl(var1, var5, true);
      JSSESocket var7 = new JSSESocket(var1, var6);
      var7.setAutoClose(var4);
      return var7;
   }

   public String[] getDefaultCipherSuites() {
      return this.sslEngineFactory.getDefaultCipherSuites();
   }

   public String[] getSupportedCipherSuites() {
      return this.sslEngineFactory.getSupportedCipherSuites();
   }

   public Socket createSocket(String var1, int var2) throws IOException, UnknownHostException {
      Socket var3 = this.getConnectedSocket(InetAddress.getByName(var1), var2, 0);
      return this.createSocket(var3, var1, var2, true);
   }

   public Socket createSocket(InetAddress var1, int var2) throws IOException {
      Socket var3 = this.getConnectedSocket(var1, var2, 0);
      return this.createSocket(var3, var1.getHostAddress(), var2, true);
   }

   public Socket createSocket(String var1, int var2, InetAddress var3, int var4) throws IOException, UnknownHostException {
      Socket var5 = this.getConnectedSocket(InetAddress.getByName(var1), var2, var3, var4, 0);
      return this.createSocket(var5, var1, var2, true);
   }

   public Socket createSocket(InetAddress var1, int var2, InetAddress var3, int var4) throws IOException {
      Socket var5 = this.getConnectedSocket(var1, var2, var3, var4, 0);
      return this.createSocket(var5, var1.getHostAddress(), var2, true);
   }

   private Socket getConnectedSocket(InetAddress var1, int var2, int var3) throws IOException {
      Socket var4 = null;
      if (!isMuxerReady) {
         isMuxerReady = SocketMuxer.isAvailable();
      }

      if (isMuxerReady) {
         var4 = SocketMuxer.getMuxer().newSSLClientSocket(var1, var2, var3);
         if (var4 == null) {
            var4 = SocketMuxer.getMuxer().newSocket(var1, var2, var3);
         }
      } else {
         var4 = new Socket();
         var4.setTcpNoDelay(true);
         var4.connect(new InetSocketAddress(var1, var2), var3);
      }

      return var4;
   }

   private Socket getConnectedSocket(InetAddress var1, int var2, InetAddress var3, int var4, int var5) throws IOException {
      Socket var6 = null;
      if (!isMuxerReady) {
         isMuxerReady = SocketMuxer.isAvailable();
      }

      if (isMuxerReady) {
         var6 = SocketMuxer.getMuxer().newSSLClientSocket(var1, var2, var3, var4, var5);
         if (var6 == null) {
            var6 = SocketMuxer.getMuxer().newSocket(var1, var2, var3, var4, var5);
         }
      } else {
         var6 = new Socket();
         var6.setTcpNoDelay(true);
         var6.bind(new InetSocketAddress(var3, var4));
         var6.connect(new InetSocketAddress(var1, var2), var5);
      }

      return var6;
   }

   private SSLEngine getSSLEngine(Socket var1) throws IOException {
      try {
         WeblogicSSLEngine var2 = this.sslEngineFactory.createSSLEngine(var1.getInetAddress().getHostAddress(), var1.getPort());
         var2.setUseClientMode(true);
         return var2;
      } catch (Exception var3) {
         var3.printStackTrace();
         return null;
      }
   }
}
