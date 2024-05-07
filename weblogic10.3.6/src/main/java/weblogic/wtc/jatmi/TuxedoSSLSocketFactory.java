package weblogic.wtc.jatmi;

import com.bea.core.jatmi.common.ntrace;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.AccessController;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import javax.net.ssl.SSLServerSocketFactory;
import weblogic.management.configuration.SSLMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.SSL.SSLSocketFactory;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.utils.KeyStoreUtils;
import weblogic.security.utils.SSLCertUtility;
import weblogic.security.utils.SSLContextManager;
import weblogic.security.utils.SSLContextWrapper;
import weblogic.wtc.WTCLogger;

public final class TuxedoSSLSocketFactory extends SSLSocketFactory {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   public static final String[] CIPHER0 = new String[]{"TLS_RSA_WITH_NULL_MD5", "TLS_RSA_WITH_NULL_SHA"};
   public static final String[] CIPHER56 = new String[]{"TLS_RSA_WITH_DES_CBC_SHA"};
   public static final String[] CIPHER112 = new String[]{"TLS_RSA_WITH_3DES_EDE_CBC_SHA"};
   public static final String[] CIPHER128 = new String[]{"TLS_RSA_WITH_RC4_128_SHA", "TLS_RSA_WITH_RC4_128_MD5"};
   public static final String[] CIPHER256 = new String[]{"TLS_RSA_WITH_AES_256_CBC_SHA"};
   private String ksType;
   private String trustKsType;
   private String identityKeyStore;
   private String identityKeyStorePassphrase;
   private String identityKeyAlias;
   private String identityKeyPassphrase;
   private String trustKeyStore;
   private String trustKeyStorePassphrase;

   public TuxedoSSLSocketFactory(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8) {
      super((javax.net.ssl.SSLSocketFactory)null);
      this.ksType = var1;
      this.identityKeyStore = var2;
      this.identityKeyStorePassphrase = var3;
      this.identityKeyAlias = var4;
      this.identityKeyPassphrase = var5;
      this.trustKsType = var6;
      this.trustKeyStore = var7;
      this.trustKeyStorePassphrase = var8;
   }

   public Socket createSocket(InetAddress var1, int var2) throws IOException {
      javax.net.ssl.SSLSocketFactory var3 = this.getSocketFactory();
      return var3.createSocket(var1, var2);
   }

   public String[] getDefaultCipherSuites() {
      try {
         return this.getSocketFactory().getDefaultCipherSuites();
      } catch (IOException var2) {
         throw (RuntimeException)(new IllegalStateException()).initCause(var2);
      }
   }

   public String[] getSupportedCipherSuites() {
      try {
         return this.getSocketFactory().getSupportedCipherSuites();
      } catch (IOException var2) {
         throw (RuntimeException)(new IllegalStateException()).initCause(var2);
      }
   }

   public ServerSocket createServerSocket(int var1, int var2, InetAddress var3) throws IOException {
      return this.getServerSocketFactory().createServerSocket(var1, var2, var3);
   }

   public Socket createSocket(Socket var1, String var2, int var3, boolean var4) throws IOException {
      return this.getSocketFactory().createSocket(var1, var2, var3, var4);
   }

   private SSLServerSocketFactory getServerSocketFactory() throws IOException {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/TuxedoSSLSocketFactory/getServerSocketFactory()");
      }

      SSLContextWrapper var2 = SSLContextWrapper.getInstance();
      String var3 = "getServerSocketFactory";
      File var4 = null;
      KeyStore var5 = null;
      Key var7 = null;
      Certificate[] var8 = null;
      String var6 = this.identityKeyStore == null ? "unspecified" : this.identityKeyStore;
      if (this.identityKeyStore != null) {
         var4 = new File(this.identityKeyStore);
         if (var4.exists()) {
            var5 = KeyStoreUtils.load(var4, this.identityKeyStorePassphrase, this.ksType);
         }
      }

      if (var5 == null) {
         WTCLogger.logErrorInvalidPrivateKeyStoreInfo(var6, var3);
         if (var1) {
            ntrace.doTrace("*]/TuxedoSSLSocketFactory/getServerSocketFactory(10)/bad key store");
         }

         throw new IOException("Problem with private key store " + var6);
      } else {
         try {
            var7 = var5.getKey(this.identityKeyAlias, this.identityKeyPassphrase.toCharArray());
            var8 = var5.getCertificateChain(this.identityKeyAlias);
         } catch (Exception var15) {
            if (var1) {
               var15.printStackTrace();
            }
         }

         if (var7 != null && var7 instanceof PrivateKey && var8 instanceof Certificate[]) {
            X509Certificate[] var9 = new X509Certificate[var8.length];

            for(int var10 = 0; var10 < var9.length; ++var10) {
               var9[var10] = (X509Certificate)var8[var10];
            }

            var2.addIdentity(var9, (PrivateKey)var7);
            KeyStore var16 = null;
            var4 = null;
            if (this.trustKeyStore == null && this.trustKeyStorePassphrase == null && this.trustKsType == null) {
               try {
                  var2.addTrustedCA(SSLContextManager.getServerTrustedCAs());
               } catch (Exception var13) {
                  WTCLogger.logErrorInvalidServerTrustCertificate(var3);
                  if (var1) {
                     ntrace.doTrace("*]/TuxedoSSLSocketFactory/getServerSocketFactory(50)/bad trust cert");
                  }

                  throw new IOException(var13.getMessage());
               }
            } else {
               var6 = this.trustKeyStore == null ? "unspecified" : this.trustKeyStore;
               if (this.trustKeyStore != null) {
                  var4 = new File(var6);
                  if (var4.exists()) {
                     var16 = KeyStoreUtils.load(var4, this.trustKeyStorePassphrase, this.trustKsType);
                  }
               }

               if (var16 == null) {
                  WTCLogger.logErrorInvalidTrustCertStoreInfo(var6, var3);
                  if (var1) {
                     ntrace.doTrace("*]/TuxedoSSLSocketFactory/getServerSocketFactory(30)/bad trust store");
                  }

                  throw new IOException("Problem with trust certificate store " + var6);
               }

               try {
                  X509Certificate[] var11 = null;
                  ArrayList var12 = new ArrayList();
                  var12.addAll(SSLCertUtility.getX509Certificates(var16));
                  var11 = (X509Certificate[])((X509Certificate[])var12.toArray(new X509Certificate[var12.size()]));
                  var2.addTrustedCA(var11);
               } catch (Exception var14) {
                  WTCLogger.logErrorInvalidTrustCertificate(var6, var3);
                  if (var1) {
                     ntrace.doTrace("*]/TuxedoSSLSocketFactory/getServerSocketFactory(40)/bad trust cert");
                  }

                  throw new IOException(var14.getMessage());
               }
            }

            if (var1) {
               ntrace.doTrace("]/TuxedoSSLSocketFactory/getServerSocketFactory(60)/success");
            }

            return var2.getSSLServerSocketFactory();
         } else {
            WTCLogger.logErrorInvalidPrivateKeyInfo(this.identityKeyAlias, var6, var3);
            if (var1) {
               ntrace.doTrace("*]/TuxedoSSLSocketFactory/getServerSocketFactory(20)/bad key or cert");
            }

            throw new IOException("Problem with key or certificate");
         }
      }
   }

   private javax.net.ssl.SSLSocketFactory getSocketFactory() throws IOException {
      boolean var1 = ntrace.isTraceEnabled(4);
      if (var1) {
         ntrace.doTrace("[/TuxedoSSLSocketFactory/getSocketFactory()");
      }

      SSLContextWrapper var2 = SSLContextWrapper.getInstance();
      String var3 = "getSocketFactory";
      File var4 = null;
      KeyStore var5 = null;
      Key var6 = null;
      Certificate[] var7 = null;
      String var8 = this.identityKeyStore == null ? "unspecified" : this.identityKeyStore;
      if (this.identityKeyStore != null) {
         var4 = new File(this.identityKeyStore);
         if (var4.exists()) {
            var5 = KeyStoreUtils.load(var4, this.identityKeyStorePassphrase, this.ksType);
         }
      }

      if (var5 == null) {
         WTCLogger.logErrorInvalidPrivateKeyStoreInfo(var8, var3);
         if (var1) {
            ntrace.doTrace("*]/TuxedoSSLSocketFactory/getSocketFactory(10)/bad key store");
         }

         throw new IOException("Problem with private key store " + var8);
      } else {
         try {
            var6 = var5.getKey(this.identityKeyAlias, this.identityKeyPassphrase.toCharArray());
            var7 = var5.getCertificateChain(this.identityKeyAlias);
         } catch (Exception var15) {
            if (var1) {
               var15.printStackTrace();
            }
         }

         if (var6 != null && var6 instanceof PrivateKey && var7 instanceof Certificate[]) {
            X509Certificate[] var9 = new X509Certificate[var7.length];

            for(int var10 = 0; var10 < var9.length; ++var10) {
               var9[var10] = (X509Certificate)var7[var10];
            }

            var2.addIdentity(var9, (PrivateKey)var6);
            KeyStore var16 = null;
            var4 = null;
            X509Certificate[] var11;
            if (this.trustKeyStore == null && this.trustKeyStorePassphrase == null && this.trustKsType == null) {
               try {
                  var11 = SSLContextManager.getServerTrustedCAs();
                  var2.addTrustedCA(var11);
               } catch (Exception var13) {
                  WTCLogger.logErrorInvalidServerTrustCertificate(var3);
                  if (var1) {
                     ntrace.doTrace("*]/TuxedoSSLSocketFactory/getSocketFactory(50)/bad trust cert");
                  }

                  throw new IOException(var13.getMessage());
               }
            } else {
               var8 = this.trustKeyStore == null ? "unspecified" : this.trustKeyStore;
               if (this.trustKeyStore != null) {
                  var4 = new File(var8);
                  if (var4.exists()) {
                     var16 = KeyStoreUtils.load(var4, this.trustKeyStorePassphrase, this.trustKsType);
                  }
               }

               if (var16 == null) {
                  WTCLogger.logErrorInvalidTrustCertStoreInfo(var8, var3);
                  if (var1) {
                     ntrace.doTrace("*]/TuxedoSSLSocketFactory/getSocketFactory(30)/bad trust store");
                  }

                  throw new IOException("Problem with trust certiticate store " + var8);
               }

               try {
                  var11 = null;
                  ArrayList var12 = new ArrayList();
                  var12.addAll(SSLCertUtility.getX509Certificates(var16));
                  var11 = (X509Certificate[])((X509Certificate[])var12.toArray(new X509Certificate[var12.size()]));
                  var2.addTrustedCA(var11);
               } catch (Exception var14) {
                  WTCLogger.logErrorInvalidTrustCertificate(var8, var3);
                  if (var1) {
                     ntrace.doTrace("*]/TuxedoSSLSocketFactory/getSocketFactory(40)/bad trust cert");
                  }

                  throw new IOException(var14.getMessage());
               }
            }

            if (var1) {
               ntrace.doTrace("]/TuxedoSSLSocketFactory/getSocketFactory(60)/get Factory");
            }

            return var2.getSSLSocketFactory();
         } else {
            WTCLogger.logErrorInvalidPrivateKeyInfo(this.identityKeyAlias, var8, var3);
            if (var1) {
               ntrace.doTrace("*]/TuxedoSSLSocketFactory/getSocketFactory(20)/bad key store");
            }

            throw new IOException("Problem with key or certificate");
         }
      }
   }

   public static String[] getCiphers(int var0, int var1) {
      int var2 = 0;
      boolean var3 = false;
      if (var0 == 0) {
         SSLMBean var4 = ManagementService.getRuntimeAccess(KERNEL_ID).getServer().getSSL();
         var3 = var4.isAllowUnencryptedNullCipher();
         if (!var3) {
            if (var1 == 0) {
               WTCLogger.logErrorNoNullCiphersAllowed();
               return null;
            }

            WTCLogger.logWarnNoNullCiphersAllowed();
         }
      }

      if (var0 <= 256 && var1 >= 256) {
         var2 += CIPHER256.length;
      }

      if (var0 <= 128 && var1 >= 128) {
         var2 += CIPHER128.length;
      }

      if (var0 <= 112 && var1 >= 112) {
         var2 += CIPHER112.length;
      }

      if (var0 <= 56 && var1 >= 56) {
         var2 += CIPHER56.length;
      }

      if (var3) {
         var2 += CIPHER0.length;
      }

      String[] var7 = new String[var2];
      int var5 = 0;
      int var6;
      if (var0 <= 256 && var1 >= 256) {
         for(var6 = 0; var6 < CIPHER256.length; ++var6) {
            var7[var5] = CIPHER256[var6];
            ++var5;
         }
      }

      if (var0 <= 128 && var1 >= 128) {
         for(var6 = 0; var6 < CIPHER128.length; ++var6) {
            var7[var5] = CIPHER128[var6];
            ++var5;
         }
      }

      if (var0 <= 112 && var1 >= 112) {
         for(var6 = 0; var6 < CIPHER112.length; ++var6) {
            var7[var5] = CIPHER112[var6];
            ++var5;
         }
      }

      if (var0 <= 56 && var1 >= 56) {
         for(var6 = 0; var6 < CIPHER56.length; ++var6) {
            var7[var5] = CIPHER56[var6];
            ++var5;
         }
      }

      if (var3) {
         for(var6 = 0; var6 < CIPHER0.length; ++var6) {
            var7[var5] = CIPHER0[var6];
            ++var5;
         }
      }

      return var7;
   }
}
