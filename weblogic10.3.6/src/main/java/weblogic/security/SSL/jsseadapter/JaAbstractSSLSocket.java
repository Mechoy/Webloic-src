package weblogic.security.SSL.jsseadapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ProtocolException;
import java.net.SocketException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.util.logging.Level;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import weblogic.security.SSL.WLSSSLNioSocket;
import weblogic.security.SSL.WeblogicSSLEngine;
import weblogic.security.utils.SSLIOContext;
import weblogic.security.utils.SSLIOContextTable;
import weblogic.security.utils.SSLTrustValidator;
import weblogic.security.utils.SSLTruster;

abstract class JaAbstractSSLSocket extends SSLSocket implements WLSSSLNioSocket {
   private final JaSSLContext jaSSLContext;
   private SSLIOContext sslIoContext;
   private final JaSSLEngineRunner.Context sslEngineRunnerContext = new JaSSLEngineRunner.Context();
   private final ReadableByteChannel appReadableByteChannel;
   private final WritableByteChannel appWritableByteChannel;
   private final JaChannelInputStream appInStream;
   private final OutputStream appOutStream;

   public String[] getSupportedCipherSuites() {
      return this.sslEngineRunnerContext.getSslEngine().getSupportedCipherSuites();
   }

   public String[] getEnabledCipherSuites() {
      return this.sslEngineRunnerContext.getSslEngine().getEnabledCipherSuites();
   }

   public void setEnabledCipherSuites(String[] var1) {
      this.sslEngineRunnerContext.getSslEngine().setEnabledCipherSuites(var1);
   }

   public String[] getSupportedProtocols() {
      return this.sslEngineRunnerContext.getSslEngine().getSupportedProtocols();
   }

   public String[] getEnabledProtocols() {
      return this.sslEngineRunnerContext.getSslEngine().getEnabledProtocols();
   }

   public void setEnabledProtocols(String[] var1) {
      this.sslEngineRunnerContext.getSslEngine().setEnabledProtocols(var1);
   }

   public SSLSession getSession() {
      return this.sslEngineRunnerContext.getSslEngine().getSession();
   }

   public void addHandshakeCompletedListener(HandshakeCompletedListener var1) {
      SSLEngine var2 = this.sslEngineRunnerContext.getSslEngine();
      if (var2 instanceof WeblogicSSLEngine) {
         ((WeblogicSSLEngine)var2).addHandshakeCompletedListener(var1);
      } else {
         throw new UnsupportedOperationException("HandshakeCompletedListener is only supported when using WeblogicSSLEngine.");
      }
   }

   public void removeHandshakeCompletedListener(HandshakeCompletedListener var1) {
      SSLEngine var2 = this.sslEngineRunnerContext.getSslEngine();
      if (var2 instanceof WeblogicSSLEngine) {
         ((WeblogicSSLEngine)var2).removeHandshakeCompletedListener(var1);
      } else {
         throw new UnsupportedOperationException("HandshakeCompletedListener is only supported when using WeblogicSSLEngine.");
      }
   }

   public void startHandshake() throws IOException {
      SSLEngine var1 = this.sslEngineRunnerContext.getSslEngine();
      if (HandshakeStatus.NOT_HANDSHAKING == var1.getHandshakeStatus() || HandshakeStatus.FINISHED == var1.getHandshakeStatus()) {
         this.sslEngineRunnerContext.getSslEngine().beginHandshake();
      }

      byte[] var2 = var1.getSession().getId();
      boolean var3 = null == var2 || 0 == var2.length;
      if (!var3) {
         if (JaLogger.isLoggable(Level.FINEST)) {
            String var8 = this.getInetAddress().getHostAddress();
            int var9 = this.getPort();
            JaLogger.log(Level.FINEST, JaLogger.Component.SSLSOCKET, "startHandshake() called for rehandshaking, not blocking on socket to peer {0}:{1}.", var8, var9);
         }

      } else {
         SocketChannel var4 = this.getChannel();
         boolean var5 = null == var4 ? true : var4.isBlocking();
         boolean var6 = false;
         if (null != var4 && !var4.isBlocking()) {
            var4.configureBlocking(true);
            var6 = true;
            if (JaLogger.isLoggable(Level.FINER)) {
               JaLogger.log(Level.FINER, JaLogger.Component.SSLSOCKET, "Socket Channel {0} temporarily changed to blocking.", var4);
            }
         }

         JaSSLEngineRunner.RunnerResult var7;
         while(JaSSLEngineRunner.RunnerResult.OK != (var7 = JaSSLEngineRunner.wrap(this.sslEngineRunnerContext))) {
            if (JaLogger.isLoggable(Level.FINEST)) {
               JaLogger.log(Level.FINEST, JaLogger.Component.SSLSOCKET, "Trying to complete handshake on socket channel {0}, last result={1}.", var4, var7);
            }

            Thread.currentThread();
            Thread.yield();
         }

         if (JaLogger.isLoggable(Level.FINEST)) {
            JaLogger.log(Level.FINEST, JaLogger.Component.SSLSOCKET, "Completed initial handshake on socket channel {0}, last result={1}.", var4, var7);
         }

         if (var6 && null != var4) {
            var4.configureBlocking(var5);
            if (JaLogger.isLoggable(Level.FINER)) {
               JaLogger.log(Level.FINER, JaLogger.Component.SSLSOCKET, "Socket Channel {0} restored to blocking mode={1}.", var4, var5);
            }
         }

      }
   }

   public void setUseClientMode(boolean var1) {
      this.sslEngineRunnerContext.getSslEngine().setUseClientMode(var1);
   }

   public boolean getUseClientMode() {
      return this.sslEngineRunnerContext.getSslEngine().getUseClientMode();
   }

   public void setNeedClientAuth(boolean var1) {
      this.sslEngineRunnerContext.getSslEngine().setNeedClientAuth(var1);
   }

   public boolean getNeedClientAuth() {
      return this.sslEngineRunnerContext.getSslEngine().getNeedClientAuth();
   }

   public void setWantClientAuth(boolean var1) {
      this.sslEngineRunnerContext.getSslEngine().setWantClientAuth(var1);
   }

   public boolean getWantClientAuth() {
      return this.sslEngineRunnerContext.getSslEngine().getWantClientAuth();
   }

   public void setEnableSessionCreation(boolean var1) {
      this.sslEngineRunnerContext.getSslEngine().setEnableSessionCreation(var1);
   }

   public boolean getEnableSessionCreation() {
      return this.sslEngineRunnerContext.getSslEngine().getEnableSessionCreation();
   }

   public InputStream getInputStream() throws IOException {
      return this.appInStream;
   }

   public OutputStream getOutputStream() throws IOException {
      return this.appOutStream;
   }

   public void close() throws IOException {
      JaSSLEngineRunner.close(this.sslEngineRunnerContext, false);
      if (this.sslIoContext != null) {
         SSLIOContextTable.removeContext(this.sslIoContext);
      }

      super.close();
   }

   public void shutdownInput() throws IOException {
      try {
         JaSSLEngineRunner.close(this.sslEngineRunnerContext, false);
      } catch (Exception var2) {
      }

      super.shutdownInput();
   }

   public void shutdownOutput() throws IOException {
      try {
         JaSSLEngineRunner.closeOutbound(this.sslEngineRunnerContext);
      } catch (Exception var2) {
      }

      super.shutdownOutput();
   }

   public int hashCode() {
      return super.hashCode();
   }

   public boolean equals(Object var1) {
      return super.equals(var1);
   }

   protected Object clone() throws CloneNotSupportedException {
      throw new CloneNotSupportedException();
   }

   public String toString() {
      return super.toString();
   }

   public ReadableByteChannel getReadableByteChannel() {
      return this.appReadableByteChannel;
   }

   public WritableByteChannel getWritableByteChannel() {
      return this.appWritableByteChannel;
   }

   public SelectableChannel getSelectableChannel() {
      return this.getChannel();
   }

   JaSSLEngineRunner.Context getSslEngineRunnerContext() {
      return this.sslEngineRunnerContext;
   }

   WeblogicSSLEngine createSslEngine(JaSSLParameters var1) throws SSLException {
      String var2 = this.getInetAddress().getHostAddress();
      int var3 = this.getPort();
      WeblogicSSLEngine var4 = this.jaSSLContext.createSSLEngine(var2, var3);
      var1.setUnencryptedNullCipherEnabled(this.jaSSLContext.isUnencryptedNullCipherEnabled());
      var1.configureSslEngine(var4);
      SSLTruster var5 = this.jaSSLContext.getTrustManager();
      if (var5 instanceof SSLTrustValidator) {
         SSLTrustValidator var6 = (SSLTrustValidator)var5;
         var4.setNeedClientAuth(var6.isPeerCertsRequired());
      }

      return var4;
   }

   void init(SocketChannel var1, JaSSLParameters var2) throws IOException {
      if (null == var1) {
         throw new IllegalArgumentException("Expected non-null SocketChannel.");
      } else {
         this.appInStream.setSelectableChannel(var1);
         JaChannelInputStream var3 = new JaChannelInputStream(var1);
         var3.setSelectableChannel(var1);
         this.sslIoContext = new SSLIOContext(var3, var1.socket().getOutputStream(), this);
         InputStream var4 = this.sslIoContext.getMuxerIS();
         SSLIOContextTable.addContext(this.sslIoContext);
         ReadableByteChannel var5 = Channels.newChannel(var4);
         if (JaLogger.isLoggable(Level.FINEST)) {
            JaLogger.log(Level.FINEST, JaLogger.Component.SSLSOCKET, "{0}[{1}] Accessing network using SocketChannel.", this.getClass().getName(), this.hashCode());
         }

         this.init((ReadableByteChannel)var5, (WritableByteChannel)var1, (JaSSLParameters)var2);
      }
   }

   void init(JaSSLParameters var1) throws IOException {
      InputStream var2 = super.getInputStream();
      OutputStream var3 = super.getOutputStream();
      this.init(var1, var2, var3);
   }

   void init(JaSSLParameters var1, InputStream var2, OutputStream var3) throws IOException {
      this.sslIoContext = new SSLIOContext(var2, var3, this);
      InputStream var4 = this.sslIoContext.getMuxerIS();
      SSLIOContextTable.addContext(this.sslIoContext);
      ReadableByteChannel var5 = Channels.newChannel(var4);
      WritableByteChannel var6 = Channels.newChannel(var3);
      if (JaLogger.isLoggable(Level.FINEST)) {
         JaLogger.log(Level.FINEST, JaLogger.Component.SSLSOCKET, "{0}[{1}] Accessing network using InputStream and OutputStream.", this.getClass().getName(), this.hashCode());
      }

      this.init(var5, var6, var1);
   }

   void init(ReadableByteChannel var1, WritableByteChannel var2, JaSSLParameters var3) throws IOException {
      if (null == var3) {
         throw new IllegalArgumentException("Expected non-null JaSSLParameters.");
      } else {
         try {
            this.setTcpNoDelay(true);
         } catch (SocketException var5) {
            throw new ProtocolException(var5.getMessage());
         }

         WeblogicSSLEngine var4 = this.createSslEngine(var3);
         var4.setAssociatedSSLSocket(this);
         this.sslEngineRunnerContext.init(var4, var1, var2, this.sslIoContext);
      }
   }

   JaAbstractSSLSocket(JaSSLContext var1) throws IOException {
      this.appReadableByteChannel = new JaApplicationReadableByteChannel(this.sslEngineRunnerContext);
      this.appWritableByteChannel = new JaApplicationWritableByteChannel(this.sslEngineRunnerContext);
      this.appInStream = new JaChannelInputStream(this.appReadableByteChannel);
      this.appOutStream = new JaApplicationOutputStream(this);
      if (null == var1) {
         throw new IllegalArgumentException("Expected non-null JaSSLContext.");
      } else {
         this.jaSSLContext = var1;
      }
   }

   JaAbstractSSLSocket(JaSSLContext var1, String var2, int var3) throws IOException {
      super(var2, var3);
      this.appReadableByteChannel = new JaApplicationReadableByteChannel(this.sslEngineRunnerContext);
      this.appWritableByteChannel = new JaApplicationWritableByteChannel(this.sslEngineRunnerContext);
      this.appInStream = new JaChannelInputStream(this.appReadableByteChannel);
      this.appOutStream = new JaApplicationOutputStream(this);
      if (null == var1) {
         throw new IllegalArgumentException("Expected non-null JaSSLContext.");
      } else {
         this.jaSSLContext = var1;
      }
   }

   JaAbstractSSLSocket(JaSSLContext var1, InetAddress var2, int var3) throws IOException {
      super(var2, var3);
      this.appReadableByteChannel = new JaApplicationReadableByteChannel(this.sslEngineRunnerContext);
      this.appWritableByteChannel = new JaApplicationWritableByteChannel(this.sslEngineRunnerContext);
      this.appInStream = new JaChannelInputStream(this.appReadableByteChannel);
      this.appOutStream = new JaApplicationOutputStream(this);
      if (null == var1) {
         throw new IllegalArgumentException("Expected non-null JaSSLContext.");
      } else {
         this.jaSSLContext = var1;
      }
   }

   JaAbstractSSLSocket(JaSSLContext var1, String var2, int var3, InetAddress var4, int var5) throws IOException {
      super(var2, var3, var4, var5);
      this.appReadableByteChannel = new JaApplicationReadableByteChannel(this.sslEngineRunnerContext);
      this.appWritableByteChannel = new JaApplicationWritableByteChannel(this.sslEngineRunnerContext);
      this.appInStream = new JaChannelInputStream(this.appReadableByteChannel);
      this.appOutStream = new JaApplicationOutputStream(this);
      if (null == var1) {
         throw new IllegalArgumentException("Expected non-null JaSSLContext.");
      } else {
         this.jaSSLContext = var1;
      }
   }

   JaAbstractSSLSocket(JaSSLContext var1, InetAddress var2, int var3, InetAddress var4, int var5) throws IOException {
      super(var2, var3, var4, var5);
      this.appReadableByteChannel = new JaApplicationReadableByteChannel(this.sslEngineRunnerContext);
      this.appWritableByteChannel = new JaApplicationWritableByteChannel(this.sslEngineRunnerContext);
      this.appInStream = new JaChannelInputStream(this.appReadableByteChannel);
      this.appOutStream = new JaApplicationOutputStream(this);
      if (null == var1) {
         throw new IllegalArgumentException("Expected non-null JaSSLContext.");
      } else {
         this.jaSSLContext = var1;
      }
   }
}
