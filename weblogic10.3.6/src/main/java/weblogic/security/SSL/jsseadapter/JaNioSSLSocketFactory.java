package weblogic.security.SSL.jsseadapter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.text.MessageFormat;
import javax.net.ssl.SSLSocketFactory;

final class JaNioSSLSocketFactory extends SSLSocketFactory {
   private final JaSSLContext jaSSLContext;

   public Socket createSocket(String var1, int var2) throws IOException, UnknownHostException {
      SocketChannel var3 = SocketChannel.open();
      var3.configureBlocking(true);
      var3.connect(new InetSocketAddress(var1, var2));
      JaSSLParameters var4 = new JaSSLParameters(this.jaSSLContext.getSSLContext());
      var4.setUseClientMode(true);
      return new JaNioSSLSocket(var3, this.jaSSLContext, var4, true);
   }

   public Socket createSocket(InetAddress var1, int var2) throws IOException {
      SocketChannel var3 = SocketChannel.open();
      var3.configureBlocking(true);
      var3.connect(new InetSocketAddress(var1, var2));
      JaSSLParameters var4 = new JaSSLParameters(this.jaSSLContext.getSSLContext());
      var4.setUseClientMode(true);
      return new JaNioSSLSocket(var3, this.jaSSLContext, var4, true);
   }

   public Socket createSocket(String var1, int var2, InetAddress var3, int var4) throws IOException, UnknownHostException {
      SocketChannel var5 = SocketChannel.open();
      var5.configureBlocking(true);
      var5.socket().bind(new InetSocketAddress(var3, var4));
      var5.connect(new InetSocketAddress(var1, var2));
      JaSSLParameters var6 = new JaSSLParameters(this.jaSSLContext.getSSLContext());
      var6.setUseClientMode(true);
      return new JaNioSSLSocket(var5, this.jaSSLContext, var6, true);
   }

   public Socket createSocket(InetAddress var1, int var2, InetAddress var3, int var4) throws IOException {
      SocketChannel var5 = SocketChannel.open();
      var5.configureBlocking(true);
      var5.socket().bind(new InetSocketAddress(var3, var4));
      var5.connect(new InetSocketAddress(var1.getHostAddress(), var2));
      JaSSLParameters var6 = new JaSSLParameters(this.jaSSLContext.getSSLContext());
      var6.setUseClientMode(true);
      return new JaNioSSLSocket(var5, this.jaSSLContext, var6, true);
   }

   public Socket createSocket(Socket var1, String var2, int var3, boolean var4) throws IOException {
      SocketChannel var5 = var1.getChannel();
      if (null == var5) {
         String var7 = MessageFormat.format("The Socket argument is not created by SocketChannel.open() :{0}", var1.toString());
         throw new IllegalArgumentException(var7);
      } else {
         var5.configureBlocking(true);
         JaSSLParameters var6 = new JaSSLParameters(this.jaSSLContext.getSSLContext());
         var6.setUseClientMode(true);
         return new JaNioSSLSocket(var5, this.jaSSLContext, var6, var4);
      }
   }

   public String[] getDefaultCipherSuites() {
      return this.jaSSLContext.getDefaultCipherSuites();
   }

   public String[] getSupportedCipherSuites() {
      return this.jaSSLContext.getSupportedCipherSuites();
   }

   JaNioSSLSocketFactory(JaSSLContext var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Expected non-null JaSSLContext instance.");
      } else {
         this.jaSSLContext = var1;
      }
   }
}
