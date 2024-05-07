package weblogic.security.SSL.jsseadapter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLServerSocket;

final class JaSSLServerSocket extends SSLServerSocket {
   private final JaSSLContext jaSSLContext;
   private final JaSSLParameters sslParameters;

   public String[] getEnabledCipherSuites() {
      return JaCipherSuiteNameMap.fromJsse(this.sslParameters.getEnabledCipherSuites());
   }

   public void setEnabledCipherSuites(String[] var1) {
      String[] var2 = JaCipherSuiteNameMap.toJsse(var1);
      SSLContext var3 = this.jaSSLContext.getSSLContext();
      SSLEngine var4 = var3.createSSLEngine();
      var4.setEnabledCipherSuites(var2);
      this.sslParameters.setEnabledCipherSuites(var2);
   }

   public String[] getSupportedCipherSuites() {
      return this.jaSSLContext.getSupportedCipherSuites();
   }

   public String[] getSupportedProtocols() {
      return this.jaSSLContext.getSupportedProtocols();
   }

   public String[] getEnabledProtocols() {
      return this.sslParameters.getEnabledProtocols();
   }

   public void setEnabledProtocols(String[] var1) {
      SSLContext var2 = this.jaSSLContext.getSSLContext();
      SSLEngine var3 = var2.createSSLEngine();
      var3.setEnabledProtocols(var1);
      this.sslParameters.setEnabledProtocols(var1);
   }

   public void setNeedClientAuth(boolean var1) {
      this.sslParameters.setNeedClientAuth(var1);
   }

   public boolean getNeedClientAuth() {
      return this.sslParameters.getNeedClientAuth();
   }

   public void setWantClientAuth(boolean var1) {
      this.sslParameters.setWantClientAuth(var1);
   }

   public boolean getWantClientAuth() {
      return this.sslParameters.getWantClientAuth();
   }

   public void setUseClientMode(boolean var1) {
      this.sslParameters.setUseClientMode(var1);
   }

   public boolean getUseClientMode() {
      return this.sslParameters.getUseClientMode();
   }

   public void setEnableSessionCreation(boolean var1) {
      this.sslParameters.setEnableSessionCreation(var1);
   }

   public boolean getEnableSessionCreation() {
      return this.sslParameters.getEnableSessionCreation();
   }

   public Socket accept() throws IOException {
      JaSSLSocket var1 = null;
      boolean var2 = false;

      try {
         var1 = new JaSSLSocket(this.jaSSLContext);
         this.implAccept(var1);
         var1.init(this.sslParameters);
         var2 = true;
         if (JaLogger.isLoggable(Level.FINEST)) {
            JaLogger.log(Level.FINEST, JaLogger.Component.SSLSERVERSOCKET, "Accepted connection from client: {0}:{1}", var1.getInetAddress().getHostAddress(), var1.getPort());
         }
      } finally {
         if (null != var1 && !var2) {
            var1.close();
            if (JaLogger.isLoggable(Level.FINE)) {
               JaLogger.log(Level.FINE, JaLogger.Component.SSLSERVERSOCKET, "Closed socket due to failure during accept. Host={0}, Port={1}", var1.getInetAddress().getHostAddress(), var1.getPort());
            }
         }

      }

      return var1;
   }

   public void close() throws IOException {
      super.close();
   }

   public boolean isClosed() {
      return super.isClosed();
   }

   public String toString() {
      return super.toString();
   }

   protected Object clone() throws CloneNotSupportedException {
      throw new CloneNotSupportedException();
   }

   JaSSLServerSocket(JaSSLContext var1, int var2, int var3, InetAddress var4) throws IOException {
      super(var2, var3, var4);
      if (null == var1) {
         throw new IllegalArgumentException("Expected non-null JaSSLContext.");
      } else {
         this.jaSSLContext = var1;
         this.sslParameters = new JaSSLParameters(var1.getSSLContext());
         this.sslParameters.setUseClientMode(false);
      }
   }
}
