package weblogic.security.utils;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.KeyManagementException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import weblogic.security.SecurityLogger;
import weblogic.security.SSL.WeblogicSSLEngine;

public class SSLContextWrapper {
   private static final String GET_ENABLENULLCIPHER_METHOD = "enableUnencryptedNullCipher";
   private static final String GET_ISNULLCIPHER_METHOD = "isUnencryptedNullCipherEnabled";
   private static final String GET_SSL_NIO_SSF_METHOD = "getSSLNioServerSocketFactory";
   private static final String GET_SSL_NIO_SOCK_METHOD = "getSSLNioSocketFactory";
   private boolean b1stCall_EnableNullCipher = true;
   private Method mtd_EnableNullCipher = null;
   private boolean b1stCall_isEnableNullCipher = true;
   private Method mtd_isEnableNullCipher = null;
   private boolean b1stCall_getNioServerFact = true;
   private Method mtd_getNioServerFact = null;
   private boolean b1stCall_getNioSockFact = true;
   private Method mtd_getNioSockFact = null;
   private SSLContextDelegate sslContext = SSLSetup.getSSLDelegateInstance();
   private ConcurrentMap<String, Long> unsupportedCerts;
   private static final int LOG_PERIOD = 300000;

   public static final SSLContextWrapper getInstance() {
      return new SSLContextWrapper();
   }

   private SSLContextWrapper() {
      if (!SSLSetup.isJSSEEnabled()) {
         this.sslContext.setProtocolVersion(SSLSetup.getProtocolVersion());
      }

      this.sslContext.setTrustManager(new SSLTrustValidator());
      this.sslContext.setHostnameVerifier(new SSLWLSHostnameVerifier());
      this.sslContext.enforceConstraints(SSLSetup.getEnforceConstraints());
      this.unsupportedCerts = new ConcurrentHashMap();
   }

   private void logCertError(X509Certificate var1, Exception var2) {
      if (var1 != null) {
         boolean var3 = false;
         boolean var4 = Boolean.getBoolean("weblogic.security.suppressUnsupportedCANotice");
         String var5 = var1.getIssuerX500Principal().getName() + var1.getSerialNumber().toString();
         long var6 = System.currentTimeMillis();
         if (this.unsupportedCerts.containsKey(var5)) {
            Long var8 = (Long)this.unsupportedCerts.get(var5);
            if (var6 - var8 >= 300000L) {
               var3 = true;
            }
         } else {
            var3 = true;
         }

         if (var3 && !var4) {
            this.unsupportedCerts.put(var5, new Long(var6));
            SecurityLogger.logFailedToAddaCA2Server(var1.getSubjectX500Principal().getName(), var2.getMessage());
         }
      }

   }

   public void addTrustedCA(X509Certificate var1) throws CertificateException {
      this.sslContext.addTrustedCA(var1);
   }

   public void addTrustedCA(X509Certificate[] var1) throws CertificateException {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         try {
            this.sslContext.addTrustedCA(var1[var2]);
         } catch (CertificateParsingException var4) {
            this.logCertError(var1[var2], var4);
         }
      }

   }

   public X509Certificate[] getTrustedCAs() {
      return this.sslContext.getTrustedCAs();
   }

   public PrivateKey inputPrivateKey(InputStream var1, char[] var2) throws KeyManagementException {
      return this.sslContext.inputPrivateKey(var1, var2);
   }

   public X509Certificate[] inputCertChain(InputStream var1) throws KeyManagementException {
      return this.sslContext.inputCertChain(var1);
   }

   public void loadLocalIdentity(InputStream var1, char[] var2) throws KeyManagementException {
      this.sslContext.loadLocalIdentity(var1, var2);
   }

   public void loadTrustedCerts(InputStream var1) throws CertificateException, KeyManagementException {
      this.sslContext.loadTrustedCerts(var1);
   }

   public void addIdentity(X509Certificate[] var1, PrivateKey var2) {
      this.sslContext.addIdentity(var1, var2);
   }

   public boolean doKeysMatch(PublicKey var1, PrivateKey var2) throws KeyManagementException {
      return this.sslContext.doKeysMatch(var1, var2);
   }

   public void setExportRefreshCount(int var1) {
      this.sslContext.setExportRefreshCount(var1);
   }

   public SSLServerSocketFactory getSSLServerSocketFactory() {
      return this.sslContext.getSSLServerSocketFactory();
   }

   public SSLSocketFactory getSSLSocketFactory() {
      return this.sslContext.getSSLSocketFactory();
   }

   public void setTrustManager(SSLTrustValidator var1) {
      this.sslContext.setTrustManager(var1);
   }

   public SSLTrustValidator getTrustManager() {
      return (SSLTrustValidator)this.sslContext.getTrustManager();
   }

   public void setHostnameVerifier(SSLWLSHostnameVerifier var1) {
      this.sslContext.setHostnameVerifier(var1);
   }

   public SSLWLSHostnameVerifier getHostnameVerifier() {
      return (SSLWLSHostnameVerifier)this.sslContext.getHostnameVerifier();
   }

   public void enableUnencryptedNullCipher(boolean var1) {
      if (this.b1stCall_EnableNullCipher) {
         Class var2 = this.sslContext.getClass();

         try {
            this.mtd_EnableNullCipher = var2.getMethod("enableUnencryptedNullCipher", Boolean.TYPE);
         } catch (NoSuchMethodException var4) {
            SSLSetup.info(var4, "Method enableUnencryptedNullCipher() does not exist for class " + var2.getName());
         }
      }

      String var3;
      try {
         if (this.mtd_EnableNullCipher != null) {
            this.mtd_EnableNullCipher.invoke(this.sslContext, new Boolean(var1));
         }
      } catch (IllegalAccessException var5) {
         var3 = "Method enableUnencryptedNullCipher() can not be accessed; detail: " + var5.getMessage();
         if (this.b1stCall_EnableNullCipher) {
            SSLSetup.info(var5, var3);
         }

         throw new RuntimeException(var3, var5);
      } catch (InvocationTargetException var6) {
         var3 = "Method enableUnencryptedNullCipher() can not be involked with object " + this.sslContext.toString() + " detail: " + var6.getMessage();
         if (this.b1stCall_EnableNullCipher) {
            SSLSetup.info(var6, var3);
         }

         throw new RuntimeException(var3, var6);
      }

      if (this.b1stCall_EnableNullCipher) {
         this.b1stCall_EnableNullCipher = false;
      }

   }

   public boolean isUnencryptedNullCipherEnabled() {
      Object var1 = null;
      if (this.b1stCall_isEnableNullCipher) {
         Class var2 = this.sslContext.getClass();

         try {
            this.mtd_isEnableNullCipher = var2.getMethod("isUnencryptedNullCipherEnabled");
         } catch (NoSuchMethodException var4) {
            SSLSetup.info(var4, "Method isUnencryptedNullCipher() does not exist for class " + var2.getName());
         }
      }

      String var3;
      try {
         if (this.mtd_isEnableNullCipher != null) {
            var1 = this.mtd_isEnableNullCipher.invoke(this.sslContext);
         }
      } catch (IllegalAccessException var5) {
         var3 = "Method isUnencryptedNullCipher() can not be accessed; detail: " + var5.getMessage();
         if (this.b1stCall_isEnableNullCipher) {
            SSLSetup.info(var5, var3);
         }

         throw new RuntimeException(var3, var5);
      } catch (InvocationTargetException var6) {
         var3 = "Method isUnencryptedNullCipher() can not be involked with object " + this.sslContext.toString() + " detail: " + var6.getMessage();
         if (this.b1stCall_isEnableNullCipher) {
            SSLSetup.info(var6, var3);
         }

         throw new RuntimeException(var3, var6);
      }

      if (this.b1stCall_isEnableNullCipher) {
         this.b1stCall_isEnableNullCipher = false;
      }

      return var1 != null ? (Boolean)var1 : false;
   }

   public SSLServerSocketFactory getSSLNioServerSocketFactory() {
      Object var1 = null;
      if (this.b1stCall_getNioServerFact) {
         Class var2 = this.sslContext.getClass();

         try {
            this.mtd_getNioServerFact = var2.getMethod("getSSLNioServerSocketFactory");
         } catch (NoSuchMethodException var4) {
            SSLSetup.info(var4, "Method getSSLNioServerSocketFactory() does not exist for class " + var2.getName());
         }
      }

      String var3;
      try {
         if (this.mtd_getNioServerFact != null) {
            var1 = this.mtd_getNioServerFact.invoke(this.sslContext);
         }
      } catch (IllegalAccessException var5) {
         var3 = "Method getSSLNioServerSocketFactory() can not be accessed; detail: " + var5.getMessage();
         if (this.b1stCall_getNioServerFact) {
            SSLSetup.info(var5, var3);
         }

         throw new RuntimeException(var3, var5);
      } catch (InvocationTargetException var6) {
         var3 = "Method getSSLNioServerSocketFactory() can not be involked with object " + this.sslContext.toString() + " detail: " + var6.getMessage();
         if (this.b1stCall_getNioServerFact) {
            SSLSetup.info(var6, var3);
         }

         throw new RuntimeException(var3, var6);
      }

      if (this.b1stCall_getNioServerFact) {
         this.b1stCall_getNioServerFact = false;
      }

      if (var1 != null) {
         if (this.b1stCall_getNioServerFact) {
            SSLSetup.info("SSL Nio version of SSLServerSocketFactory is created");
         }

         return (SSLServerSocketFactory)var1;
      } else {
         throw new UnsupportedOperationException("Method of getSSLNioServerSocketFactory() is not supported");
      }
   }

   public SSLSocketFactory getSSLNioSocketFactory() {
      Object var1 = null;
      if (this.b1stCall_getNioSockFact) {
         Class var2 = this.sslContext.getClass();

         try {
            this.mtd_getNioSockFact = var2.getMethod("getSSLNioSocketFactory");
         } catch (NoSuchMethodException var4) {
            SSLSetup.info(var4, "Method getSSLNioServerSocketFactory() does not exist for class " + var2.getName());
         }
      }

      String var3;
      try {
         if (this.mtd_getNioSockFact != null) {
            var1 = this.mtd_getNioSockFact.invoke(this.sslContext);
         }
      } catch (IllegalAccessException var5) {
         var3 = "Method getSSLNioSocketFactory() can not be accessed; detail: " + var5.getMessage();
         if (this.b1stCall_getNioSockFact) {
            SSLSetup.info(var5, var3);
         }

         throw new RuntimeException(var3, var5);
      } catch (InvocationTargetException var6) {
         var3 = "Method getSSLNioSocketFactory() can not be involked with object " + this.sslContext.toString() + " detail: " + var6.getMessage();
         if (this.b1stCall_getNioSockFact) {
            SSLSetup.info(var6, var3);
         }

         throw new RuntimeException(var3, var6);
      }

      if (this.b1stCall_getNioSockFact) {
         this.b1stCall_getNioSockFact = false;
      }

      if (var1 != null) {
         if (this.b1stCall_getNioServerFact) {
            SSLSetup.info("SSL Nio version of SSLSocketFactory is created");
         }

         return (SSLSocketFactory)var1;
      } else {
         throw new UnsupportedOperationException("Method of getSSLNioSocketFactory() is not supported");
      }
   }

   public WeblogicSSLEngine createSSLEngine() throws SSLException {
      if (this.sslContext instanceof SSLContextDelegate2) {
         SSLContextDelegate2 var1 = (SSLContextDelegate2)this.sslContext;
         return var1.createSSLEngine();
      } else {
         throw new UnsupportedOperationException("createSSLEngine is not supported by selected SSL implementation.");
      }
   }

   public WeblogicSSLEngine createSSLEngine(String var1, int var2) throws SSLException {
      if (this.sslContext instanceof SSLContextDelegate2) {
         SSLContextDelegate2 var3 = (SSLContextDelegate2)this.sslContext;
         return var3.createSSLEngine(var1, var2);
      } else {
         throw new UnsupportedOperationException("createSSLEngine is not supported by selected SSL implementation.");
      }
   }

   public String[] getDefaultCipherSuites() {
      if (this.sslContext instanceof SSLContextDelegate2) {
         SSLContextDelegate2 var1 = (SSLContextDelegate2)this.sslContext;
         return var1.getDefaultCipherSuites();
      } else {
         throw new UnsupportedOperationException("getDefaultCipherSuites is not supported by selected SSL implementation.");
      }
   }

   public String[] getSupportedCipherSuites() {
      if (this.sslContext instanceof SSLContextDelegate2) {
         SSLContextDelegate2 var1 = (SSLContextDelegate2)this.sslContext;
         return var1.getSupportedCipherSuites();
      } else {
         throw new UnsupportedOperationException("getSupportedCipherSuites is not supported by selected SSL implementation.");
      }
   }
}
