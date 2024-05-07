package weblogic.security.SSL.jsseadapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Cipher;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import weblogic.security.utils.BasicConstraintsSSLContextDelegate;
import weblogic.security.utils.SSLHostnameVerifier;
import weblogic.security.utils.SSLTrustValidator;
import weblogic.security.utils.SSLTruster;
import weblogic.socket.JSSESocketFactory;

public final class JaSSLContextImpl extends JaSSLContext implements BasicConstraintsSSLContextDelegate {
   private String specifiedProviderName;
   private Provider specifiedProvider;
   private SecureRandom secureRandom;
   private SSLContext sslContext;
   private int protocolVersion;
   private static final String DEFAULT_PROTOCOL = "TLS";
   private volatile SSLTruster truster;
   private volatile SSLHostnameVerifier hostnameVerifier;
   private int enforceConstraintsLevel;
   private boolean enableUnencryptedNullCipher;
   private int exportRefreshCount;
   private Vector<X509Certificate> trustedCAs = new Vector();
   private PrivateKey identityPrivateKey;
   private X509Certificate[] certChain;
   private static final String CERT_HEADER = "-----BEGIN CERTIFICATE-----";

   public void addTrustedCA(X509Certificate var1) throws CertificateException {
      if (JaLogger.isLoggable(Level.FINEST)) {
         JaLogger.log(Level.FINEST, JaLogger.Component.SSLCONTEXT, "addTrustedCA called.");
      }

      this.trustedCAs.add(var1);
   }

   public X509Certificate[] getTrustedCAs() {
      X509Certificate[] var1 = new X509Certificate[this.trustedCAs.size()];
      return (X509Certificate[])this.trustedCAs.toArray(var1);
   }

   public PrivateKey inputPrivateKey(InputStream var1, char[] var2) throws KeyManagementException {
      if (JaLogger.isLoggable(Level.FINEST)) {
         JaLogger.log(Level.FINEST, JaLogger.Component.SSLCONTEXT, "inputPrivateKey called.");
      }

      return JaSSLSupport.getLocalIdentityPrivateKey(var1, var2);
   }

   public X509Certificate[] inputCertChain(InputStream var1) throws KeyManagementException {
      if (JaLogger.isLoggable(Level.FINEST)) {
         JaLogger.log(Level.FINEST, JaLogger.Component.SSLCONTEXT, "inputCertChain called.");
      }

      String var2 = null;
      byte[] var3 = null;
      Object var4 = new ArrayList();

      try {
         var3 = JaSSLSupport.readFully(var1);
         if (var3 != null) {
            var2 = new String(var3);
         }
      } catch (IOException var11) {
         if (JaLogger.isLoggable(Level.SEVERE)) {
            JaLogger.log(Level.SEVERE, JaLogger.Component.SSLCONTEXT, var11, "Error reading data from from the certificate inputstream: " + var11.getMessage());
         }
      }

      if (var2 != null) {
         Matcher var6 = Pattern.compile("-----BEGIN CERTIFICATE-----.+?-----END CERTIFICATE-----", 32).matcher(var2);
         CertificateFactory var7 = null;

         try {
            var7 = CertificateFactory.getInstance("X.509");

            int var8;
            for(var8 = 0; var6.find(); ++var8) {
               String var9 = var6.group();
               ((Collection)var4).add(var7.generateCertificate(new ByteArrayInputStream(var9.getBytes())));
            }

            boolean var12 = var8 > 0;
            if (!var12) {
               var4 = var7.generateCertificates(new ByteArrayInputStream(var3));
            }
         } catch (CertificateException var10) {
            if (JaLogger.isLoggable(Level.SEVERE)) {
               JaLogger.log(Level.SEVERE, JaLogger.Component.SSLCONTEXT, var10, "Error retrieving certifcate(s) from inputstream: " + var10.getMessage());
            }
         }
      }

      return (X509Certificate[])((X509Certificate[])((Collection)var4).toArray(new X509Certificate[((Collection)var4).size()]));
   }

   public void loadLocalIdentity(InputStream var1, char[] var2) throws KeyManagementException {
      if (JaLogger.isLoggable(Level.FINEST)) {
         JaLogger.log(Level.FINEST, JaLogger.Component.SSLCONTEXT, "loadLocalIdentity called, ignored by JSSE for now.");
      }

   }

   public void loadTrustedCerts(InputStream var1) throws CertificateException, KeyManagementException {
      if (JaLogger.isLoggable(Level.FINEST)) {
         JaLogger.log(Level.FINEST, JaLogger.Component.SSLCONTEXT, "loadTrustedCerts called.");
      }

      X509Certificate[] var2 = this.inputCertChain(var1);
      X509Certificate[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         X509Certificate var6 = var3[var5];
         this.trustedCAs.add(var6);
      }

   }

   public void addIdentity(X509Certificate[] var1, PrivateKey var2) {
      this.identityPrivateKey = var2;
      this.certChain = var1;
      if (JaLogger.isLoggable(Level.FINEST)) {
         JaLogger.log(Level.FINEST, JaLogger.Component.SSLCONTEXT, "addIdentity called.");
      }

   }

   public boolean doKeysMatch(PublicKey var1, PrivateKey var2) throws KeyManagementException {
      if (JaLogger.isLoggable(Level.FINEST)) {
         JaLogger.log(Level.FINEST, JaLogger.Component.SSLCONTEXT, "doKeysMatch called.");
      }

      boolean var3 = var1.getAlgorithm().equals(var2.getAlgorithm());
      boolean var4 = var3;
      if (var3) {
         String var5 = "Hello there!";

         try {
            Cipher var6 = Cipher.getInstance(var1.getAlgorithm());
            Cipher var7 = Cipher.getInstance(var2.getAlgorithm());
            var6.init(1, var1);
            var7.init(2, var2);
            byte[] var8 = var6.doFinal(var5.getBytes());
            var4 = var5.equals(new String(var7.doFinal(var8)));
         } catch (Exception var9) {
            if (JaLogger.isLoggable(Level.FINEST)) {
               JaLogger.log(Level.FINEST, JaLogger.Component.SSLCONTEXT, "Error getting Cipher instance.", var9.getMessage());
            }
         }
      }

      return var4;
   }

   public void setExportRefreshCount(int var1) {
      this.exportRefreshCount = var1;
      if (JaLogger.isLoggable(Level.FINEST)) {
         JaLogger.log(Level.FINEST, JaLogger.Component.SSLCONTEXT, "Set exportRefreshCount to {0}.", var1);
      }

   }

   public void setProtocolVersion(int var1) throws IllegalArgumentException {
      this.protocolVersion = var1;
      if (JaLogger.isLoggable(Level.FINEST)) {
         JaLogger.log(Level.FINEST, JaLogger.Component.SSLCONTEXT, "Set protocolVersion to {0}.", this.protocolVersion);
      }

   }

   public SSLServerSocketFactory getSSLServerSocketFactory() {
      return new JaSSLServerSocketFactory(this, false);
   }

   public SSLSocketFactory getSSLSocketFactory() {
      return this.getConfiguredSSLSocketFactory();
   }

   public void setTrustManager(SSLTruster var1) {
      this.truster = var1;
      if (JaLogger.isLoggable(Level.FINEST)) {
         JaLogger.log(Level.FINEST, JaLogger.Component.SSLCONTEXT, "Set weblogic.security.utils.SSLTruster to {0}.", var1);
      }

   }

   public SSLTruster getTrustManager() {
      return this.truster;
   }

   public void setHostnameVerifier(SSLHostnameVerifier var1) {
      this.hostnameVerifier = var1;
      if (JaLogger.isLoggable(Level.FINEST)) {
         JaLogger.log(Level.FINEST, JaLogger.Component.SSLCONTEXT, "Set weblogic.security.utils.SSLHostnameVerifier to {0}.", var1);
      }

   }

   public SSLHostnameVerifier getHostnameVerifier() {
      return this.hostnameVerifier;
   }

   public void enforceConstraints(int var1) {
      this.enforceConstraintsLevel = var1;
      JaSSLSupport.setX509BasicConstraintsStrict(2 == var1 || 3 == var1);
      JaSSLSupport.setNoV1CAs(4 == var1 || 3 == var1);
      if (JaLogger.isLoggable(Level.FINEST)) {
         JaLogger.log(Level.FINEST, JaLogger.Component.SSLCONTEXT, "Set enforceConstraints level to {0}.", var1);
      }

   }

   public void enableUnencryptedNullCipher(boolean var1) {
      this.enableUnencryptedNullCipher = var1;
      JaSSLSupport.isUnEncrytedNullCipherAllowed();
      if (JaLogger.isLoggable(Level.FINEST)) {
         JaLogger.log(Level.FINEST, JaLogger.Component.SSLCONTEXT, "Set enableUnencryptedNullCipher to {0}.", var1);
      }

   }

   public boolean isUnencryptedNullCipherEnabled() {
      return this.enableUnencryptedNullCipher;
   }

   public SSLServerSocketFactory getSSLNioServerSocketFactory() {
      return new JaSSLServerSocketFactory(this, true);
   }

   public SSLSocketFactory getSSLNioSocketFactory() {
      return this.getSSLSocketFactory();
   }

   public JaSSLEngine createSSLEngine() throws SSLException {
      try {
         JaSSLEngine var1 = new JaSSLEngine(this, this.getSSLContext().createSSLEngine());
         this.configureSslEngine(var1);
         return var1;
      } catch (Exception var2) {
         throw new SSLException(var2);
      }
   }

   public JaSSLEngine createSSLEngine(String var1, int var2) throws SSLException {
      try {
         JaSSLEngine var3 = new JaSSLEngine(this, this.getSSLContext().createSSLEngine(var1, var2));
         this.configureSslEngine(var3);
         return var3;
      } catch (Exception var4) {
         throw new SSLException(var4);
      }
   }

   public String[] getDefaultCipherSuites() {
      SSLContext var1 = this.getSSLContext();
      SSLEngine var2 = var1.createSSLEngine();
      return JaCipherSuiteNameMap.fromJsse(var2.getEnabledCipherSuites());
   }

   public String[] getSupportedCipherSuites() {
      SSLContext var1 = this.getSSLContext();
      SSLEngine var2 = var1.createSSLEngine();
      return JaCipherSuiteNameMap.fromJsse(var2.getSupportedCipherSuites());
   }

   public String[] getDefaultProtocols() {
      SSLContext var1 = this.getSSLContext();
      SSLEngine var2 = var1.createSSLEngine();
      return var2.getEnabledProtocols();
   }

   public String[] getSupportedProtocols() {
      SSLContext var1 = this.getSSLContext();
      SSLEngine var2 = var1.createSSLEngine();
      return var2.getSupportedProtocols();
   }

   synchronized SSLContext getSSLContext() {
      if (null == this.sslContext) {
         Provider var2 = this.getSpecifiedProvider();

         try {
            SSLContext var3;
            if (null == var2) {
               var3 = SSLContext.getInstance("TLS");
            } else {
               var3 = SSLContext.getInstance("TLS", var2);
            }

            this.initializeContext(var3);
            this.sslContext = var3;
            if (JaLogger.isLoggable(Level.FINEST)) {
               JaLogger.log(Level.FINEST, JaLogger.Component.SSLCONTEXT, "Got SSLContext, protocol={0}, provider={1}", "TLS", var3.getProvider().getName());
            }
         } catch (Exception var4) {
            if (JaLogger.isLoggable(Level.FINE)) {
               JaLogger.log(Level.FINE, JaLogger.Component.SSLCONTEXT, var4, "Unable to construct SSLContext, protocol={0}, provider={1}", "TLS", null == var2 ? var2 : var2.getName());
            }

            throw new RuntimeException(var4);
         }
      }

      return this.sslContext;
   }

   synchronized String getSpecifiedProviderName() {
      return this.specifiedProviderName;
   }

   synchronized SecureRandom getSecureRandom() throws NoSuchAlgorithmException {
      if (null == this.secureRandom) {
         Provider var2 = this.getSpecifiedProvider();

         try {
            SecureRandom var3;
            if (null == var2) {
               var3 = SecureRandom.getInstance("SHA1PRNG");
            } else {
               var3 = SecureRandom.getInstance("SHA1PRNG", var2);
            }

            this.secureRandom = var3;
            if (JaLogger.isLoggable(Level.FINEST)) {
               JaLogger.log(Level.FINEST, JaLogger.Component.SSLCONTEXT, "Got SecureRandom, algorithm={0}, provider={1}", "SHA1PRNG", var3.getProvider().getName());
            }
         } catch (RuntimeException var4) {
            this.log_getSecureRandom(var4, "SHA1PRNG", var2);
            throw var4;
         } catch (NoSuchAlgorithmException var5) {
            this.log_getSecureRandom(var5, "SHA1PRNG", var2);
            throw var5;
         }
      }

      return this.secureRandom;
   }

   private void log_getSecureRandom(Exception var1, String var2, Provider var3) {
      if (JaLogger.isLoggable(Level.FINE)) {
         JaLogger.log(Level.FINE, JaLogger.Component.SSLCONTEXT, var1, "Unable to get SecureRandom, algorithm={0}, provider={1}", var2, null == var3 ? var3 : var3.getName());
      }

   }

   synchronized boolean hasSpecifiedProvider() {
      return null != this.getSpecifiedProviderName();
   }

   synchronized Provider getSpecifiedProvider() {
      if (!this.hasSpecifiedProvider()) {
         return null;
      } else {
         if (null == this.specifiedProvider) {
            String var1 = this.getSpecifiedProviderName();

            try {
               this.specifiedProvider = Security.getProvider(var1);
               if (null == this.specifiedProvider) {
                  throw new IllegalArgumentException("Specified provider \"" + var1 + "\" has not been installed.");
               }

               if (JaLogger.isLoggable(Level.FINEST)) {
                  JaLogger.log(Level.FINEST, JaLogger.Component.SSLCONTEXT, "Got Provider, name={0}", var1);
               }
            } catch (RuntimeException var3) {
               if (JaLogger.isLoggable(Level.FINE)) {
                  JaLogger.log(Level.FINE, JaLogger.Component.SSLCONTEXT, var3, "Unable to get specified provider, name={0}.", var1);
               }

               throw var3;
            }
         }

         return this.specifiedProvider;
      }
   }

   private void log_getKeyStore(Exception var1, String var2, Provider var3) {
      if (JaLogger.isLoggable(Level.FINE)) {
         JaLogger.log(Level.FINE, JaLogger.Component.SSLCONTEXT, var1, "Unable to get KeyStore, type={0}, provider={1}", var2, null == var3 ? var3 : var3.getName());
      }

   }

   private void log_getTrustStore(Exception var1, String var2, Provider var3) {
      if (JaLogger.isLoggable(Level.FINE)) {
         JaLogger.log(Level.FINE, JaLogger.Component.SSLCONTEXT, var1, "Unable to get TrustStore, type={0}, provider={1}", var2, null == var3 ? var3 : var3.getName());
      }

   }

   private void initializeContext(SSLContext var1) throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, KeyManagementException {
      if (var1 != null) {
         X509Certificate[] var2 = new X509Certificate[this.trustedCAs.size()];
         this.trustedCAs.copyInto(var2);
         TrustManager[] var3 = new TrustManager[]{new JaTrustManager(var2)};
         KeyManagerFactory var4 = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
         KeyStore var5 = KeyStore.getInstance(KeyStore.getDefaultType());
         var5.load((InputStream)null, (char[])null);
         char[] var6 = null;
         if (null != this.identityPrivateKey) {
            String var7 = Integer.toString(this.identityPrivateKey.hashCode());
            var6 = var7.toCharArray();
            var5.setKeyEntry(var7, this.identityPrivateKey, var6, this.certChain);
         }

         var4.init(var5, var6);
         var1.init(var4.getKeyManagers(), var3, (SecureRandom)null);
      }

   }

   private void configureSslEngine(JaSSLEngine var1) {
      JaSSLParameters var2 = new JaSSLParameters(this.getSSLContext());
      var2.setUnencryptedNullCipherEnabled(this.isUnencryptedNullCipherEnabled());
      var2.configureSslEngine(var1);
      SSLTruster var3 = this.getTrustManager();
      if (var3 instanceof SSLTrustValidator) {
         SSLTrustValidator var4 = (SSLTrustValidator)var3;
         var1.setNeedClientAuth(var4.isPeerCertsRequired());
      }

   }

   private SSLSocketFactory getConfiguredSSLSocketFactory() {
      JaSSLEngineFactoryImpl var1 = new JaSSLEngineFactoryImpl(this);
      return JSSESocketFactory.getSSLSocketFactory(var1);
   }
}
