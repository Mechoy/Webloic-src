package weblogic.security.SSL.jsseadapter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLServerSocket;

final class JaNioSSLServerSocket extends SSLServerSocket {
   private final JaSSLContext jaSSLContext;
   private final JaSSLParameters sslParameters;
   private final ServerSocketChannel serverSocketChannel;
   private final ServerSocket serverSocket;

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

   public void bind(SocketAddress var1) throws IOException {
      this.serverSocket.bind(var1);
   }

   public void bind(SocketAddress var1, int var2) throws IOException {
      this.serverSocket.bind(var1, var2);
   }

   public InetAddress getInetAddress() {
      return this.serverSocket.getInetAddress();
   }

   public int getLocalPort() {
      return this.serverSocket.getLocalPort();
   }

   public SocketAddress getLocalSocketAddress() {
      return this.serverSocket.getLocalSocketAddress();
   }

   public Socket accept() throws IOException {
      SocketChannel var1 = this.serverSocketChannel.accept();
      if (null == var1) {
         if (JaLogger.isLoggable(Level.FINE)) {
            JaLogger.log(Level.FINE, JaLogger.Component.NIOSSLSERVERSOCKET, "Expected blocking ServerSocketChannel; accept() returned null.");
         }

         throw new IllegalStateException("Expected blocking ServerSocketChannel; accept() returned null.");
      } else {
         Socket var2 = var1.socket();
         if (JaLogger.isLoggable(Level.FINEST)) {
            JaLogger.log(Level.FINEST, JaLogger.Component.NIOSSLSERVERSOCKET, "Accepted connection from client: {0}", var2.getInetAddress().getHostAddress());
         }

         var1.configureBlocking(true);
         JaNioSSLSocket var3 = null;

         try {
            var3 = new JaNioSSLSocket(var1, this.jaSSLContext, new JaSSLParameters(this.sslParameters), true);
         } finally {
            if (null == var3) {
               var2.close();
               if (JaLogger.isLoggable(Level.FINE)) {
                  JaLogger.log(Level.FINE, JaLogger.Component.NIOSSLSERVERSOCKET, "Closed channel socket since unable to construct JaNioSSLSocket. Host={0}, Port={1}", var2.getInetAddress().getHostAddress(), var2.getPort());
               }
            }

         }

         return var3;
      }
   }

   public void close() throws IOException {
      this.serverSocket.close();
   }

   public ServerSocketChannel getChannel() {
      return this.serverSocketChannel;
   }

   public boolean isBound() {
      return this.serverSocket.isBound();
   }

   public boolean isClosed() {
      return this.serverSocket.isClosed();
   }

   public synchronized void setSoTimeout(int var1) throws SocketException {
      this.serverSocket.setSoTimeout(var1);
   }

   public synchronized int getSoTimeout() throws IOException {
      return this.serverSocket.getSoTimeout();
   }

   public void setReuseAddress(boolean var1) throws SocketException {
      this.serverSocket.setReuseAddress(var1);
   }

   public boolean getReuseAddress() throws SocketException {
      return this.serverSocket.getReuseAddress();
   }

   public String toString() {
      return this.serverSocket.toString();
   }

   public synchronized void setReceiveBufferSize(int var1) throws SocketException {
      this.serverSocket.setReceiveBufferSize(var1);
   }

   public synchronized int getReceiveBufferSize() throws SocketException {
      return this.serverSocket.getReceiveBufferSize();
   }

   public void setPerformancePreferences(int var1, int var2, int var3) {
      this.serverSocket.setPerformancePreferences(var1, var2, var3);
   }

   public int hashCode() {
      return this.serverSocket.hashCode();
   }

   public boolean equals(Object var1) {
      return this.serverSocket.equals(var1);
   }

   protected Object clone() throws CloneNotSupportedException {
      throw new CloneNotSupportedException();
   }

   JaNioSSLServerSocket(JaSSLContext var1, int var2, int var3, InetAddress var4) throws IOException {
      if (null == var1) {
         throw new IllegalArgumentException("Expected non-null JaSSLContext.");
      } else {
         this.jaSSLContext = var1;
         SSLContext var5 = var1.getSSLContext();
         this.sslParameters = new JaSSLParameters(var5);
         this.sslParameters.setUseClientMode(false);
         this.serverSocketChannel = ServerSocketChannel.open();
         this.serverSocketChannel.configureBlocking(true);
         ServerSocket var6 = this.serverSocketChannel.socket();
         InetSocketAddress var7;
         if (null != var4) {
            var7 = new InetSocketAddress(var4, var2);
         } else {
            var7 = new InetSocketAddress(var2);
         }

         if (var3 > 0) {
            var6.bind(var7, var3);
         } else {
            var6.bind(var7);
         }

         this.serverSocket = var6;
      }
   }
}
