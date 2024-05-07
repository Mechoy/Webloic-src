package weblogic.security.SSL.jsseadapter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.SSLSocketFactory;

final class JaSSLSocketFactory extends SSLSocketFactory {
   private final JaSSLContext jaSSLContext;

   public Socket createSocket(String var1, int var2) throws IOException, UnknownHostException {
      JaSSLParameters var3 = new JaSSLParameters(this.jaSSLContext.getSSLContext());
      var3.setUseClientMode(true);
      return new JaSSLSocket(this.jaSSLContext, var3, var1, var2);
   }

   public Socket createSocket(InetAddress var1, int var2) throws IOException {
      JaSSLParameters var3 = new JaSSLParameters(this.jaSSLContext.getSSLContext());
      var3.setUseClientMode(true);
      return new JaSSLSocket(this.jaSSLContext, var3, var1, var2);
   }

   public Socket createSocket(String var1, int var2, InetAddress var3, int var4) throws IOException, UnknownHostException {
      JaSSLParameters var5 = new JaSSLParameters(this.jaSSLContext.getSSLContext());
      var5.setUseClientMode(true);
      return new JaSSLSocket(this.jaSSLContext, var5, var1, var2, var3, var4);
   }

   public Socket createSocket(InetAddress var1, int var2, InetAddress var3, int var4) throws IOException {
      JaSSLParameters var5 = new JaSSLParameters(this.jaSSLContext.getSSLContext());
      var5.setUseClientMode(true);
      return new JaSSLSocket(this.jaSSLContext, var5, var1, var2, var3, var4);
   }

   public Socket createSocket(Socket var1, String var2, int var3, boolean var4) throws IOException {
      JaSSLParameters var5 = new JaSSLParameters(this.jaSSLContext.getSSLContext());
      var5.setUseClientMode(true);
      return new JaLayeredSSLSocket(var1, this.jaSSLContext, var5, var4);
   }

   public String[] getDefaultCipherSuites() {
      return this.jaSSLContext.getDefaultCipherSuites();
   }

   public String[] getSupportedCipherSuites() {
      return this.jaSSLContext.getSupportedCipherSuites();
   }

   JaSSLSocketFactory(JaSSLContext var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Expected non-null JaSSLContext instance.");
      } else {
         this.jaSSLContext = var1;
      }
   }
}
