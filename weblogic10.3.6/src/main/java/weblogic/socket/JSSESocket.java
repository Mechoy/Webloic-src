package weblogic.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import weblogic.security.SSL.WeblogicSSLEngine;

public class JSSESocket extends WeblogicSSLSocket {
   private InputStream in;
   private OutputStream out;
   private JSSEFilterImpl filter;
   private SSLEngine sslEngine;

   public JSSESocket(Socket var1, JSSEFilterImpl var2) {
      super(var1);
      this.filter = var2;
      this.sslEngine = var2.getSSLEngine();
      if (this.sslEngine instanceof WeblogicSSLEngine) {
         ((WeblogicSSLEngine)this.sslEngine).setAssociatedSSLSocket(this);
      }

      this.in = new JSSEInputStream(var2);
      this.out = new JSSEOutputStream(var2);
   }

   public InputStream getInputStream() {
      return this.in;
   }

   public OutputStream getOutputStream() {
      return this.out;
   }

   public JSSEFilterImpl getFilter() {
      return this.filter;
   }

   public void addHandshakeCompletedListener(HandshakeCompletedListener var1) {
      this.filter.addHandshakeCompletedListener(new HandshakeListenerImpl(var1));
   }

   public boolean getEnableSessionCreation() {
      return this.sslEngine.getEnableSessionCreation();
   }

   public String[] getEnabledCipherSuites() {
      return this.sslEngine.getEnabledCipherSuites();
   }

   public String[] getEnabledProtocols() {
      return this.sslEngine.getEnabledProtocols();
   }

   public boolean getNeedClientAuth() {
      return this.sslEngine.getNeedClientAuth();
   }

   public SSLSession getSession() {
      return this.sslEngine.getSession();
   }

   public String[] getSupportedCipherSuites() {
      return this.sslEngine.getSupportedCipherSuites();
   }

   public String[] getSupportedProtocols() {
      return this.sslEngine.getSupportedProtocols();
   }

   public boolean getUseClientMode() {
      return this.sslEngine.getUseClientMode();
   }

   public boolean getWantClientAuth() {
      return this.sslEngine.getWantClientAuth();
   }

   public void removeHandshakeCompletedListener(HandshakeCompletedListener var1) {
      this.filter.removeHandshakeCompletedListener(var1);
   }

   public void setEnableSessionCreation(boolean var1) {
      this.sslEngine.setEnableSessionCreation(var1);
   }

   public void setEnabledCipherSuites(String[] var1) {
      this.sslEngine.setEnabledCipherSuites(var1);
   }

   public void setEnabledProtocols(String[] var1) {
      this.sslEngine.setEnabledProtocols(var1);
   }

   public void setNeedClientAuth(boolean var1) {
      this.sslEngine.setNeedClientAuth(var1);
   }

   public void setUseClientMode(boolean var1) {
      this.sslEngine.setUseClientMode(var1);
   }

   public void setWantClientAuth(boolean var1) {
      this.sslEngine.setWantClientAuth(var1);
   }

   public void startHandshake() throws IOException {
      this.filter.doHandshake();
   }

   public class HandshakeListenerImpl implements JSSEFilterImpl.HandshakeListener {
      private HandshakeCompletedListener listener;

      public HandshakeListenerImpl(HandshakeCompletedListener var2) {
         this.listener = var2;
      }

      public void handshakeDone(SSLSession var1) {
         HandshakeCompletedEvent var2 = new HandshakeCompletedEvent(JSSESocket.this, var1);
         this.listener.handshakeCompleted(var2);
      }
   }

   private class JSSEOutputStream extends OutputStream {
      private JSSEFilterImpl filter;

      JSSEOutputStream(JSSEFilterImpl var2) {
         this.filter = var2;
      }

      public void write(int var1) throws IOException {
         this.write(new byte[]{(byte)var1}, 0, 1);
      }

      public void write(byte[] var1, int var2, int var3) throws IOException {
         this.filter.write(var1, var2, var3);
      }
   }

   private class JSSEInputStream extends InputStream {
      private JSSEFilterImpl filter;

      JSSEInputStream(JSSEFilterImpl var2) {
         this.filter = var2;
      }

      public int read() throws IOException {
         byte[] var1 = new byte[1];
         int var2 = this.read(var1, 0, 1);
         return var2 == 1 ? var1[0] : var2;
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         int var4;
         for(var4 = 0; var4 == 0; var4 = this.filter.read(var1, var2, var3)) {
         }

         return var4;
      }
   }
}
