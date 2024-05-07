package weblogic.rjvm.t3.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import weblogic.protocol.ServerChannel;
import weblogic.socket.WeblogicSocketFactory;

public class T3ClientWeblogicSocketFactory extends WeblogicSocketFactory {
   private final SSLSocketFactory sslSocketFactory;
   private final ServerChannel channel;

   public T3ClientWeblogicSocketFactory(SSLSocketFactory var1, ServerChannel var2) {
      this.sslSocketFactory = var1;
      this.channel = var2;
   }

   public T3ClientWeblogicSocketFactory(SocketFactory var1, ServerChannel var2) {
      throw new UnsupportedOperationException("Only SSLSocketFactory is supported");
   }

   public Socket createSocket(String var1, int var2) throws IOException, UnknownHostException {
      return this.createSocket(InetAddress.getByName(var1), var2);
   }

   public Socket createSocket(InetAddress var1, int var2) throws IOException {
      return this.createSocket(var1, var2, 0);
   }

   public Socket createSocket(InetAddress var1, int var2, int var3) throws IOException {
      int var4 = var3 <= 0 && this.channel != null ? this.channel.getConnectTimeout() * 1000 : var3;
      SSLSocket var5 = (SSLSocket)this.sslSocketFactory.createSocket();
      var5.connect(new InetSocketAddress(var1, var2), var4);
      var5.setTcpNoDelay(true);
      var5.setTrafficClass(16);
      var5.startHandshake();
      return var5;
   }

   public Socket createSocket(InetAddress var1, int var2, InetAddress var3, int var4) {
      throw new UnsupportedOperationException("Binding characteristics are determined by the channel");
   }

   public Socket createSocket(String var1, int var2, InetAddress var3, int var4) {
      throw new UnsupportedOperationException("Binding characteristics are determined by the channel");
   }
}
