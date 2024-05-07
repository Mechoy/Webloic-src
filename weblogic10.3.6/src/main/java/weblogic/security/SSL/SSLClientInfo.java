package weblogic.security.SSL;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.security.KeyManagementException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.StringTokenizer;
import javax.net.ssl.SSLSession;
import weblogic.security.SecurityLogger;
import weblogic.security.utils.InputStreamsCloner;
import weblogic.security.utils.SSLCertUtility;
import weblogic.security.utils.SSLContextWrapper;
import weblogic.security.utils.SSLSetup;
import weblogic.utils.Hex;

public final class SSLClientInfo {
   private String expectedName;
   private HostnameVerifier hostnameVerifier;
   private TrustManager trustManager;
   private HostnameVerifierJSSE hostnameVerifierJSSE;
   private TrustManagerJSSE trustManagerJSSE;
   private String clientKeyPassword;
   private PrivateKey clientPrivateKey;
   private InputStreamsCloner clientCertCloner;
   private X509Certificate[] clientCertChain;
   private X509Certificate[] trustedCA;
   private byte[][] rootCAfingerprints;
   private transient javax.net.ssl.SSLSocketFactory socketFactory;
   private boolean bNio;

   public SSLClientInfo(boolean var1) {
      this.expectedName = null;
      this.clientKeyPassword = null;
      this.clientPrivateKey = null;
      this.clientCertCloner = null;
      this.clientCertChain = null;
      this.trustedCA = null;
      this.rootCAfingerprints = (byte[][])null;
      this.bNio = false;
      this.bNio = var1;
   }

   public SSLClientInfo() {
      this(false);
   }

   public void setNio(boolean var1) {
      if (var1 != this.bNio) {
         this.socketFactory = null;
         this.bNio = var1;
      }

   }

   public boolean isNioSet() {
      return this.bNio;
   }

   public synchronized javax.net.ssl.SSLSocketFactory getSSLSocketFactory() throws SocketException {
      if (this.socketFactory == null) {
         if (!this.bNio) {
            this.socketFactory = SSLSetup.getSSLContext(this).getSSLSocketFactory();
         } else {
            this.socketFactory = SSLSetup.getSSLContext(this).getSSLNioSocketFactory();
         }
      }

      return this.socketFactory;
   }

   public boolean isEmpty() {
      return this.expectedName == null && this.hostnameVerifier == null && this.trustManager == null && this.trustedCA == null && this.clientKeyPassword == null && this.clientPrivateKey == null && this.clientCertChain == null && this.clientCertCloner == null && this.rootCAfingerprints == null;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 != null && var1 instanceof SSLClientInfo) {
         SSLClientInfo var2 = (SSLClientInfo)var1;
         if (this.isEmpty()) {
            return var2.isEmpty();
         } else {
            return !var2.isEmpty() && equals((Object)this.expectedName, (Object)var2.expectedName) && equals((Object)this.hostnameVerifier, (Object)var2.hostnameVerifier) && equals((Object)this.trustManager, (Object)var2.trustManager) && equals((Object)this.trustedCA, (Object)var2.trustedCA) && equals((Object)this.clientKeyPassword, (Object)var2.clientKeyPassword) && equals((Object)this.clientPrivateKey, (Object)var2.clientPrivateKey) && Arrays.equals(this.clientCertChain, var2.clientCertChain) && equals((Object)this.clientCertCloner, (Object)var2.clientCertCloner) && equals(this.rootCAfingerprints, this.rootCAfingerprints);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      if (this.isEmpty()) {
         return 0;
      } else {
         int var1 = 1;
         var1 = var1 * 31 + getHashCode((Object)this.expectedName);
         var1 = var1 * 31 + getHashCode((Object)this.hostnameVerifier);
         var1 = var1 * 31 + getHashCode((Object)this.trustManager);
         var1 = var1 * 31 + getHashCode((Object[])this.trustedCA);
         var1 = var1 * 31 + getHashCode((Object)this.clientKeyPassword);
         var1 = var1 * 31 + getHashCode((Object)this.clientPrivateKey);
         var1 = var1 * 31 + getHashCode((Object[])this.clientCertChain);
         var1 = var1 * 31 + getHashCode((Object)this.clientCertCloner);
         var1 = var1 * 31 + getHashCode(this.rootCAfingerprints);
         return var1;
      }
   }

   private static final boolean equals(byte[][] var0, byte[][] var1) {
      if (var0 == var1) {
         return true;
      } else if (var0 != null && var1 != null) {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            if (!Arrays.equals(var0[var2], var1[var2])) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private static final boolean equals(Object var0, Object var1) {
      return var0 == var1 || var0 != null && var1 != null && var0.equals(var1);
   }

   private static final int getHashCode(byte[][] var0) {
      if (var0 == null) {
         return 0;
      } else {
         int var1 = 1;

         for(int var2 = 0; var2 < var0.length; ++var2) {
            int var3 = 1;

            for(int var4 = 0; var4 < var0[var2].length; ++var4) {
               var3 = var3 * 31 + var0[var2][var4];
            }

            var1 = var1 * 31 + var3;
         }

         return var1;
      }
   }

   private static final int getHashCode(Object[] var0) {
      if (var0 == null) {
         return 0;
      } else {
         int var1 = 1;

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1 = var1 * 31 + getHashCode(var0[var2]);
         }

         return var1;
      }
   }

   private static final int getHashCode(Object var0) {
      return var0 == null ? 0 : var0.hashCode();
   }

   public InputStream[] getSSLClientCertificate() {
      if (this.clientCertCloner != null) {
         try {
            return this.clientCertCloner.cloneStreams();
         } catch (IOException var2) {
            SecurityLogger.logStackTrace(var2);
         }
      }

      return null;
   }

   public final synchronized void setSSLClientCertificate(InputStream[] var1) {
      this.socketFactory = null;
      if (var1 != null && var1.length > 0) {
         this.clientCertCloner = new InputStreamsCloner(var1);
      } else {
         this.clientCertCloner = null;
      }

   }

   public synchronized void setSSLClientCertificateCloner(InputStreamsCloner var1) {
      this.socketFactory = null;
      this.clientCertCloner = var1;
   }

   /** @deprecated */
   public String getExpectedName() {
      return this.expectedName;
   }

   /** @deprecated */
   public synchronized void setExpectedName(String var1) {
      this.socketFactory = null;
      this.expectedName = var1;
   }

   public HostnameVerifier getHostnameVerifier() {
      return this.hostnameVerifier;
   }

   public synchronized void setHostnameVerifier(HostnameVerifier var1) {
      this.socketFactory = null;
      this.hostnameVerifier = var1;
      this.hostnameVerifierJSSE = null;
   }

   /** @deprecated */
   public synchronized void setHostnameVerifierJSSE(HostnameVerifierJSSE var1) {
      this.socketFactory = null;
      this.hostnameVerifierJSSE = var1;
      this.hostnameVerifier = var1 == null ? null : new HostnameVerifier() {
         public boolean verify(String var1, SSLSession var2) {
            return SSLClientInfo.this.hostnameVerifierJSSE.verify(var1, SSLCertUtility.getCommonName(var2));
         }
      };
   }

   /** @deprecated */
   public HostnameVerifierJSSE getHostnameVerifierJSSE() {
      return this.hostnameVerifierJSSE;
   }

   public TrustManager getTrustManager() {
      return this.trustManager;
   }

   public synchronized void setTrustManager(TrustManager var1) {
      this.socketFactory = null;
      this.trustManager = var1;
      this.trustManagerJSSE = null;
   }

   /** @deprecated */
   public synchronized void setTrustManagerJSSE(TrustManagerJSSE var1) {
      this.socketFactory = null;
      this.trustManagerJSSE = var1;
      this.trustManager = var1 == null ? null : new TrustManager() {
         public boolean certificateCallback(X509Certificate[] var1, int var2) {
            return SSLClientInfo.this.trustManagerJSSE.certificateCallback(SSLCertUtility.toJavaX509((Certificate[])var1), var2);
         }
      };
   }

   /** @deprecated */
   public TrustManagerJSSE getTrustManagerJSSE() {
      return this.trustManagerJSSE;
   }

   public byte[][] getRootCAfingerprints() {
      return this.rootCAfingerprints;
   }

   public synchronized void setRootCAfingerprints(byte[][] var1) {
      this.socketFactory = null;
      this.rootCAfingerprints = var1;
   }

   public synchronized void setRootCAfingerprints(String var1) {
      this.socketFactory = null;
      if (var1 == null) {
         this.rootCAfingerprints = (byte[][])null;
      } else {
         StringTokenizer var2 = new StringTokenizer(var1, ", \t");
         byte[][] var3 = new byte[var2.countTokens()][];

         for(int var4 = 0; var4 < var3.length; ++var4) {
            var3[var4] = Hex.fromHexString(var2.nextToken());
         }

         this.rootCAfingerprints = var3;
      }
   }

   public final synchronized void setSSLClientKeyPassword(String var1) {
      this.socketFactory = null;
      this.clientKeyPassword = var1;
   }

   public final String getSSLClientKeyPassword() {
      return this.clientKeyPassword;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer("SSLClientInfo: rootCAFingerprints ");
      var1.append(this.rootCAfingerprints != null ? String.valueOf(this.rootCAfingerprints.length) : "null");
      var1.append(", expectedServerName ").append(this.expectedName);
      var1.append(", key/certificates ");
      var1.append(this.clientCertCloner != null ? String.valueOf(this.clientCertCloner.size()) : "null");
      var1.append(", HostnameVerifier ");
      var1.append(this.hostnameVerifier != null ? this.hostnameVerifier.getClass().getName() : "null");
      var1.append(", TrustManager ");
      var1.append(this.trustManager != null ? this.trustManager.getClass().getName() : "null");
      var1.append(", ClientCertChain ");
      var1.append(this.clientCertChain != null ? String.valueOf(this.clientCertChain.length) : "null");
      var1.append(", ClientPrivateKey ").append(this.clientPrivateKey);
      return var1.toString();
   }

   public synchronized void loadLocalIdentity(InputStream var1, InputStream var2, char[] var3) {
      this.socketFactory = null;

      try {
         SSLContextWrapper var4 = SSLSetup.getSSLContext();
         this.clientCertChain = SSLCertUtility.inputCertificateChain(var4, var1);
         this.clientPrivateKey = var4.inputPrivateKey(var2, var3);
      } catch (SocketException var5) {
         SSLSetup.info(var5, "Problem getting SSLContext");
      } catch (KeyManagementException var6) {
         SSLSetup.info(var6, "Problem reading certificate/key");
      } catch (IOException var7) {
         SSLSetup.info(var7, "Problem reading certificate/key");
      }

   }

   public synchronized void loadLocalIdentity(Certificate[] var1, PrivateKey var2) {
      this.socketFactory = null;

      try {
         this.clientCertChain = SSLCertUtility.toJavaX5092(var1);
      } catch (Exception var4) {
         throw new IllegalArgumentException("Cannot convert to X509 certificates\n" + var4.getMessage());
      }

      this.clientPrivateKey = var2;
   }

   public X509Certificate[] getClientLocalIdentityCert() {
      return this.clientCertChain;
   }

   public PrivateKey getClientLocalIdentityKey() {
      return this.clientPrivateKey;
   }

   public boolean isClientCertAvailable() {
      return this.clientCertCloner != null && this.clientCertCloner.size() > 0;
   }

   public boolean isLocalIdentitySet() {
      return this.clientCertChain != null && this.clientCertChain.length > 0;
   }
}
