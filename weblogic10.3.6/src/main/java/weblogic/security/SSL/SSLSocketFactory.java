package weblogic.security.SSL;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.net.SocketFactory;
import weblogic.security.utils.SSLSetup;
import weblogic.socket.SocketMuxer;
import weblogic.socket.WeblogicSocketFactory;

public class SSLSocketFactory extends WeblogicSocketFactory {
   protected static SocketFactory defFactory = null;
   protected javax.net.ssl.SSLSocketFactory jsseFactory;

   public SSLSocketFactory() {
      this((SSLClientInfo)null);
   }

   private SSLSocketFactory(SSLClientInfo var1) {
      this.jsseFactory = null;
      this.setSSLClientInfo(var1);
   }

   protected SSLSocketFactory(javax.net.ssl.SSLSocketFactory var1) {
      this.jsseFactory = null;
      this.jsseFactory = var1;
   }

   public static SocketFactory getDefault() {
      if (defFactory == null) {
         Class var0 = SSLSocketFactory.class;
         synchronized(SSLSocketFactory.class) {
            if (defFactory == null) {
               defFactory = new SSLSocketFactory();
            }
         }
      }

      return defFactory;
   }

   public static SSLSocketFactory getInstance(SSLClientInfo var0) {
      return new SSLSocketFactory(var0);
   }

   /** @deprecated */
   public static synchronized SocketFactory getDefaultJSSE() {
      if (defFactory == null) {
         defFactory = getJSSE((SSLClientInfo)null);
      }

      return defFactory;
   }

   /** @deprecated */
   public static SSLSocketFactory getJSSE(SSLClientInfo var0) {
      return new SSLSocketFactory(var0);
   }

   public Socket createSocket(Socket var1, String var2, int var3, boolean var4) throws UnknownHostException, IOException {
      return this.jsseFactory.createSocket(var1, var2, var3, var4);
   }

   public Socket createSocket(String var1, int var2) throws UnknownHostException, IOException {
      return this.jsseFactory.createSocket(var1, var2);
   }

   public Socket createSocket(InetAddress var1, int var2) throws UnknownHostException, IOException {
      return this.jsseFactory.createSocket(var1, var2);
   }

   public Socket createSocket(String var1, int var2, InetAddress var3, int var4) throws UnknownHostException, IOException {
      return this.jsseFactory.createSocket(var1, var2, var3, var4);
   }

   public Socket createSocket(InetAddress var1, int var2, InetAddress var3, int var4) throws UnknownHostException, IOException {
      return this.jsseFactory.createSocket(var1, var2, var3, var4);
   }

   public Socket createSocket(InetAddress var1, int var2, int var3) throws IOException {
      Socket var4 = SocketMuxer.getMuxer().newSocket(var1, var2, var3);
      return this.createSocket(var4, var1.getHostName(), var2, true);
   }

   public String[] getDefaultCipherSuites() {
      return this.jsseFactory.getDefaultCipherSuites();
   }

   public String[] getSupportedCipherSuites() {
      return this.jsseFactory.getSupportedCipherSuites();
   }

   public void setSSLClientInfo(SSLClientInfo var1) {
      try {
         this.jsseFactory = var1 == null ? SSLSetup.getSSLContext(var1).getSSLSocketFactory() : var1.getSSLSocketFactory();
      } catch (SocketException var3) {
         SSLSetup.debug(3, var3, "Failed to create context");
         throw new RuntimeException("Failed to update factory: " + var3.getMessage());
      }
   }
}
