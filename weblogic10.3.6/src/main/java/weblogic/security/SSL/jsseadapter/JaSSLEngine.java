package weblogic.security.SSL.jsseadapter;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLKeyException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLEngineResult.Status;
import weblogic.security.SSL.WeblogicSSLEngine;
import weblogic.security.utils.SSLHostnameVerifier;
import weblogic.security.utils.SSLTrustValidator;
import weblogic.security.utils.SSLTruster;

final class JaSSLEngine extends WeblogicSSLEngine {
   private final SSLEngine engine;
   private final JaSSLContext jaSSLContext;
   private volatile SSLSocket associatedSSLSocket;
   private final int HANDSHAKECOMPLETEDLISTENERS_INITIALCAPACITY = 16;
   private final Vector<HandshakeCompletedListener> handshakeCompletedListeners = new Vector(16);

   public String getPeerHost() {
      return this.engine.getPeerHost();
   }

   public int getPeerPort() {
      return this.engine.getPeerPort();
   }

   public SSLEngineResult wrap(final ByteBuffer var1, final ByteBuffer var2) throws SSLException {
      return this.doAction(new SSLEngineResultSSLExceptionAction() {
         public SSLEngineResult run() throws SSLException {
            return JaSSLEngine.this.engine.wrap(var1, var2);
         }
      }, "SSLEngine.wrap(ByteBuffer,ByteBuffer)");
   }

   public SSLEngineResult wrap(final ByteBuffer[] var1, final ByteBuffer var2) throws SSLException {
      return this.doAction(new SSLEngineResultSSLExceptionAction() {
         public SSLEngineResult run() throws SSLException {
            return JaSSLEngine.this.engine.wrap(var1, var2);
         }
      }, "SSLEngine.wrap(ByteBuffer[],ByteBuffer)");
   }

   public SSLEngineResult wrap(final ByteBuffer[] var1, final int var2, final int var3, final ByteBuffer var4) throws SSLException {
      return this.doAction(new SSLEngineResultSSLExceptionAction() {
         public SSLEngineResult run() throws SSLException {
            return JaSSLEngine.this.engine.wrap(var1, var2, var3, var4);
         }
      }, "SSLEngine.wrap(ByteBuffer[],int,int,ByteBuffer)");
   }

   public SSLEngineResult unwrap(final ByteBuffer var1, final ByteBuffer var2) throws SSLException {
      return this.doAction(new SSLEngineResultSSLExceptionAction() {
         public SSLEngineResult run() throws SSLException {
            return JaSSLEngine.this.engine.unwrap(var1, var2);
         }
      }, "SSLEngine.unwrap(ByteBuffer,ByteBuffer)");
   }

   public SSLEngineResult unwrap(final ByteBuffer var1, final ByteBuffer[] var2) throws SSLException {
      return this.doAction(new SSLEngineResultSSLExceptionAction() {
         public SSLEngineResult run() throws SSLException {
            return JaSSLEngine.this.engine.unwrap(var1, var2);
         }
      }, "SSLEngine.unwrap(ByteBuffer,ByteBuffer[])");
   }

   public SSLEngineResult unwrap(final ByteBuffer var1, final ByteBuffer[] var2, final int var3, final int var4) throws SSLException {
      return this.doAction(new SSLEngineResultSSLExceptionAction() {
         public SSLEngineResult run() throws SSLException {
            return JaSSLEngine.this.engine.unwrap(var1, var2, var3, var4);
         }
      }, "SSLEngine.unwrap(ByteBuffer,ByteBuffer[],int,int)");
   }

   public Runnable getDelegatedTask() {
      return this.engine.getDelegatedTask();
   }

   public void closeInbound() throws SSLException {
      this.doAction(new SSLEngineResultSSLExceptionAction() {
         public SSLEngineResult run() throws SSLException {
            JaSSLEngine.this.engine.closeInbound();
            return null;
         }
      }, "SSLEngine.closeInbound()");
   }

   public boolean isInboundDone() {
      return this.engine.isInboundDone();
   }

   public void closeOutbound() {
      this.doAction(new SetValueAction() {
         public void run() {
            JaSSLEngine.this.engine.closeOutbound();
         }

         public String getSetValue() {
            return "closed";
         }
      }, "SSLEngine.closeOutbound()");
   }

   public boolean isOutboundDone() {
      return this.engine.isOutboundDone();
   }

   public String[] getSupportedCipherSuites() {
      return JaCipherSuiteNameMap.fromJsse(this.engine.getSupportedCipherSuites());
   }

   public String[] getEnabledCipherSuites() {
      return JaCipherSuiteNameMap.fromJsse(this.engine.getEnabledCipherSuites());
   }

   public void setEnabledCipherSuites(final String[] var1) {
      this.doAction(new SetValueAction() {
         public void run() {
            JaSSLEngine.this.engine.setEnabledCipherSuites(JaCipherSuiteNameMap.toJsse(var1));
         }

         public String getSetValue() {
            return JaSSLEngine.toString(var1);
         }
      }, "SSLEngine.setEnabledCipherSuites(String[])");
   }

   public String[] getSupportedProtocols() {
      return this.engine.getSupportedProtocols();
   }

   public String[] getEnabledProtocols() {
      return this.engine.getEnabledProtocols();
   }

   public void setEnabledProtocols(final String[] var1) {
      this.doAction(new SetValueAction() {
         public void run() {
            JaSSLEngine.this.engine.setEnabledProtocols(var1);
         }

         public String getSetValue() {
            return JaSSLEngine.toString(var1);
         }
      }, "SSLEngine.setEnabledProtocols(String[])");
   }

   public SSLSession getSession() {
      return this.engine.getSession();
   }

   public void beginHandshake() throws SSLException {
      this.doAction(new SSLEngineResultSSLExceptionAction() {
         public SSLEngineResult run() throws SSLException {
            JaSSLEngine.this.engine.beginHandshake();
            return null;
         }
      }, "SSLEngine.beginHandshake()");
   }

   public SSLEngineResult.HandshakeStatus getHandshakeStatus() {
      return this.engine.getHandshakeStatus();
   }

   public void setUseClientMode(final boolean var1) {
      this.doAction(new SetValueAction() {
         public void run() {
            JaSSLEngine.this.engine.setUseClientMode(var1);
         }

         public String getSetValue() {
            return Boolean.toString(var1);
         }
      }, "SSLEngine.setUseClientMode(boolean)");
   }

   public boolean getUseClientMode() {
      return this.engine.getUseClientMode();
   }

   public void setNeedClientAuth(final boolean var1) {
      this.doAction(new SetValueAction() {
         public void run() {
            JaSSLEngine.this.engine.setNeedClientAuth(var1);
         }

         public String getSetValue() {
            return Boolean.toString(var1);
         }
      }, "SSLEngine.setNeedClientAuth(boolean)");
   }

   public boolean getNeedClientAuth() {
      return this.engine.getNeedClientAuth();
   }

   public void setWantClientAuth(final boolean var1) {
      this.doAction(new SetValueAction() {
         public void run() {
            JaSSLEngine.this.engine.setWantClientAuth(var1);
         }

         public String getSetValue() {
            return Boolean.toString(var1);
         }
      }, "SSLEngine.setWantClientAuth(boolean)");
   }

   public boolean getWantClientAuth() {
      return this.engine.getWantClientAuth();
   }

   public void setEnableSessionCreation(final boolean var1) {
      this.doAction(new SetValueAction() {
         public void run() {
            JaSSLEngine.this.engine.setEnableSessionCreation(var1);
         }

         public String getSetValue() {
            return Boolean.toString(var1);
         }
      }, "SSLEngine.setEnableSessionCreation(boolean)");
   }

   public boolean getEnableSessionCreation() {
      return this.engine.getEnableSessionCreation();
   }

   public void setAssociatedSSLSocket(SSLSocket var1) {
      this.associatedSSLSocket = var1;
   }

   public SSLSocket getAssociatedSSLSocket() {
      return this.associatedSSLSocket;
   }

   public void addHandshakeCompletedListener(HandshakeCompletedListener var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Non-null HandshakeCompletedListener expected.");
      } else {
         boolean var2 = false;
         synchronized(this.handshakeCompletedListeners) {
            if (!this.handshakeCompletedListeners.contains(var1)) {
               this.handshakeCompletedListeners.add(var1);
               var2 = true;
            }
         }

         if (var2) {
            if (JaLogger.isLoggable(Level.FINEST)) {
               JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "Added HandshakeCompletedListener: class={0}, instance={1}.", var1.getClass().getName(), var1);
            }
         } else if (JaLogger.isLoggable(Level.FINEST)) {
            JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "HandshakeCompletedListener previously added: class={0}, instance={1}.", var1.getClass().getName(), var1);
         }

         if (null == this.getAssociatedSSLSocket() && JaLogger.isLoggable(Level.FINER)) {
            JaLogger.log(Level.FINER, JaLogger.Component.SSLENGINE, "No associated SSLSocket when adding HandshakeCompletedListener: class={0}, instance={1}. An associated SSLSocket is required.", var1.getClass().getName(), var1);
         }

      }
   }

   public void removeHandshakeCompletedListener(HandshakeCompletedListener var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Non-null HandshakeCompletedListener expected.");
      } else if (!this.handshakeCompletedListeners.remove(var1)) {
         String var2 = MessageFormat.format("Attempting to remove unregistered HandshakeCompletedListener: class={0}, instance={1}.", var1.getClass().getName(), var1);
         if (JaLogger.isLoggable(Level.FINE)) {
            JaLogger.log(Level.FINE, JaLogger.Component.SSLENGINE, var2);
         }

         throw new IllegalArgumentException(var2);
      } else {
         if (JaLogger.isLoggable(Level.FINEST)) {
            JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "Removed HandshakeCompletedListener: class={0}, instance={1}.", var1.getClass().getName(), var1);
         }

      }
   }

   JaSSLEngine(JaSSLContext var1, SSLEngine var2) {
      if (null == var1) {
         throw new IllegalArgumentException("Expected non-null JaSSLContext.");
      } else if (null == var2) {
         throw new IllegalArgumentException("Expected non-null SSLEngine.");
      } else {
         this.engine = var2;
         this.jaSSLContext = var1;
      }
   }

   final SSLEngine getDelegate() {
      return this.engine;
   }

   static void validateErrToException(int var0) throws IOException {
      if (0 == var0) {
         if (JaLogger.isLoggable(Level.FINEST)) {
            JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "No trust failure, validateErr={0}.", var0);
         }

      } else {
         StringBuilder var1 = new StringBuilder(128);
         var1.append("Trust failure (");
         var1.append(var0);
         var1.append("): ");
         if ((1 & var0) != 0) {
            var1.append(" CERT_CHAIN_INVALID");
         }

         if ((2 & var0) != 0) {
            var1.append(" CERT_EXPIRED");
         }

         if ((4 & var0) != 0) {
            var1.append(" CERT_CHAIN_INCOMPLETE");
         }

         if ((8 & var0) != 0) {
            var1.append(" SIGNATURE_INVALID");
         }

         if ((16 & var0) != 0) {
            var1.append(" CERT_CHAIN_UNTRUSTED");
         }

         if ((32 & var0) != 0) {
            var1.append(" VALIDATION_FAILED");
         }

         String var2 = var1.toString();
         if (JaLogger.isLoggable(Level.FINE)) {
            JaLogger.log(Level.FINE, JaLogger.Component.SSLENGINE, var2);
         }

         throw new SSLKeyException(var2);
      }
   }

   void doPostHandshake() throws IOException {
      SSLSocket var1 = this.getAssociatedSSLSocket();
      if (null == var1) {
         if (JaLogger.isLoggable(Level.FINE)) {
            JaLogger.log(Level.FINE, JaLogger.Component.SSLENGINE, "No associated SSLSocket for WeblogicSSLEngine (class={0}, instance={1}), unable to perform post-handshake processing.", this.getClass().getName(), this);
         }

      } else {
         SSLSession var2 = this.getSession();
         SSLTruster var3 = this.jaSSLContext.getTrustManager();
         String var7;
         if (var3 != null) {
            X509Certificate[] var4 = null;

            try {
               var4 = (X509Certificate[])((X509Certificate[])var2.getPeerCertificates());
            } catch (SSLPeerUnverifiedException var8) {
               if (JaLogger.isLoggable(Level.FINER)) {
                  JaLogger.log(Level.FINER, JaLogger.Component.SSLENGINE, "Trying to get peer certificates from SSLSession, SSLPeerUnverifiedException: {0}.", var8.getMessage());
               }
            }

            X509Certificate[] var6 = this.jaSSLContext.getTrustedCAs();
            var7 = var2.getCipherSuite();
            if (JaLogger.isLoggable(Level.FINEST)) {
               JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "negotiatedCipherSuite: {0}", var7);
               JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "SSLEngine.getNeedClientAuth(): {0}", this.getNeedClientAuth());
               JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "Peer certificate chain: {0}", var4);
               if (var3 instanceof SSLTrustValidator) {
                  JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "weblogic.security.utils.SSLTrustValidator.isPeerCertsRequired(): {0}", ((SSLTrustValidator)var3).isPeerCertsRequired());
               }
            }

            if (var7 != null && var7.toLowerCase().indexOf("_anon_") < 0) {
               validateErrToException(var3.validationCallback(var4, 0, var1, var6));
            }
         }

         if (this.getUseClientMode()) {
            SSLHostnameVerifier var9 = this.jaSSLContext.getHostnameVerifier();
            if (var9 != null) {
               InetAddress var5 = var1.getInetAddress();
               String var12;
               if (null == var5) {
                  var12 = "";
               } else {
                  var12 = var5.getHostName();
               }

               if (!var9.hostnameValidationCallback(var12, var1)) {
                  var7 = MessageFormat.format("Hostname verification failed: HostnameVerifier={0}, hostname={1}.", var9.getClass().getName(), var12);
                  throw new SSLKeyException(var7);
               }
            }
         }

         HandshakeCompletedEvent var11 = new HandshakeCompletedEvent(var1, var2);
         Iterator var10 = this.handshakeCompletedListeners.iterator();

         while(var10.hasNext()) {
            HandshakeCompletedListener var13 = (HandshakeCompletedListener)var10.next();
            var13.handshakeCompleted(var11);
         }

      }
   }

   SSLEngineResult doAction(SSLEngineResultSSLExceptionAction var1, String var2) throws SSLException {
      if (null == var1) {
         throw new IllegalArgumentException("Expected non-null SSLEngineResultSSLExceptionAction object.");
      } else if (null == var2) {
         throw new IllegalArgumentException("Expected non-null actionName object.");
      } else {
         try {
            SSLEngineResult var3 = var1.run();
            if (JaLogger.isLoggable(Level.FINEST)) {
               JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "{0} called: result={1}.", var2, var3);
            }

            if (null == var3) {
               return var3;
            } else {
               SSLEngineResult.Status var4 = var3.getStatus();
               SSLEngineResult.HandshakeStatus var5 = var3.getHandshakeStatus();
               if (Status.OK == var4 && HandshakeStatus.FINISHED == var5) {
                  this.doPostHandshake();
                  if (JaLogger.isLoggable(Level.FINEST)) {
                     JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "Successfully completed post-handshake processing.");
                  }
               }

               return var3;
            }
         } catch (Exception var6) {
            if (JaLogger.isLoggable(Level.FINE)) {
               JaLogger.log(Level.FINE, JaLogger.Component.SSLENGINE, var6, "Exception occurred during {0}.", var2);
            }

            if (var6 instanceof RuntimeException) {
               throw (RuntimeException)var6;
            } else if (var6 instanceof SSLException) {
               throw (SSLException)var6;
            } else {
               throw new SSLException("Occurred during " + var2 + ".", var6);
            }
         }
      }
   }

   private void doAction(SetValueAction var1, String var2) {
      if (null == var1) {
         throw new IllegalArgumentException("Expected non-null SetValueAction object.");
      } else if (null == var2) {
         throw new IllegalArgumentException("Expected non-null actionName object.");
      } else {
         try {
            var1.run();
            if (JaLogger.isLoggable(Level.FINEST)) {
               JaLogger.log(Level.FINEST, JaLogger.Component.SSLENGINE, "{0}: value={1}.", var2, var1.getSetValue());
            }

         } catch (RuntimeException var4) {
            if (JaLogger.isLoggable(Level.FINE)) {
               JaLogger.log(Level.FINE, JaLogger.Component.SSLENGINE, var4, "Exception occurred during {0}: value={1}.", var2, var1.getSetValue());
            }

            throw var4;
         }
      }
   }

   static String toString(String[] var0) {
      if (null == var0) {
         return "<null>";
      } else {
         StringBuilder var1 = new StringBuilder(256);
         String[] var2 = var0;
         int var3 = var0.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            if (null != var5) {
               if (var1.length() > 0) {
                  var1.append(",");
               }

               var1.append(var5);
            }
         }

         return var1.toString();
      }
   }

   private interface SetValueAction {
      void run();

      String getSetValue();
   }

   interface SSLEngineResultSSLExceptionAction {
      SSLEngineResult run() throws SSLException;
   }
}
