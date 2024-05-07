package weblogic.security.SSL.jsseadapter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

final class JaSSLServerSocketFactory extends SSLServerSocketFactory {
   private final JaSSLContext jaSSLContext;
   private final boolean nio;

   JaSSLServerSocketFactory(JaSSLContext var1, boolean var2) {
      if (null == var1) {
         throw new IllegalArgumentException("Expected non-null JaSSLContext instance.");
      } else {
         this.jaSSLContext = var1;
         this.nio = var2;
      }
   }

   boolean isNio() {
      return this.nio;
   }

   public ServerSocket createServerSocket() throws IOException {
      throw new UnsupportedOperationException("Unbound ServerSocket instances are not supported.");
   }

   public ServerSocket createServerSocket(int var1) throws IOException {
      return this.createServerSocket(var1, -1, (InetAddress)null);
   }

   public ServerSocket createServerSocket(int var1, int var2) throws IOException {
      return this.createServerSocket(var1, var2, (InetAddress)null);
   }

   public ServerSocket createServerSocket(int var1, int var2, InetAddress var3) throws IOException {
      Object var4;
      if (this.isNio()) {
         var4 = new JaNioSSLServerSocket(this.jaSSLContext, var1, var2, var3);
      } else {
         var4 = new JaSSLServerSocket(this.jaSSLContext, var1, var2, var3);
      }

      return (ServerSocket)var4;
   }

   public String[] getDefaultCipherSuites() {
      return this.jaSSLContext.getDefaultCipherSuites();
   }

   public String[] getSupportedCipherSuites() {
      return this.jaSSLContext.getSupportedCipherSuites();
   }
}
