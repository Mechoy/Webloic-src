package weblogic.security.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.AccessController;
import java.security.Key;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import utils.ValidateCertChain;
import weblogic.kernel.T3SrvrLogger;
import weblogic.logging.Loggable;
import weblogic.management.bootstrap.BootStrap;
import weblogic.management.configuration.ConfigurationException;
import weblogic.management.configuration.NetworkAccessPointMBean;
import weblogic.management.configuration.SSLMBean;
import weblogic.management.provider.CommandLine;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.protocol.ServerChannel;
import weblogic.security.SecurityLogger;
import weblogic.security.SSL.SSLClientInfo;
import weblogic.security.SSL.SSLEngineFactory;
import weblogic.security.SSL.TrustManager;
import weblogic.security.SSL.jsseadapter.JaSSLEngineFactoryBuilder;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.KeyManager;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.server.channels.ServerChannelImpl;

public final class SSLContextManager {
   private static final int ONE_DAY = 86400000;
   private static final int WARNING_PERIOD = 30;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static SSLSocketFactory defaultFactory = null;
   private static SSLSocketFactory defaultNioFactory = null;
   private static SSLContextWrapper defaultContext = null;
   private static Map channelContexts = new HashMap();
   private static Map sslIdentities = new Hashtable();
   private static X509Certificate[] trustedCACerts = null;
   private static char[] keyFilePwd = null;
   private boolean debug = SSLSetup.isDebugEnabled(3);
   private RuntimeAccess runtimeAccess = null;

   public static SSLServerSocketFactory getSSLServerSocketFactory(ServerChannel var0, AuthenticatedSubject var1) throws ConfigurationException, CertificateException {
      SSLContextWrapper var2 = getChannelSSLContext(var0, var1);
      return var2.getSSLServerSocketFactory();
   }

   public static SSLServerSocketFactory getSSLNioServerSocketFactory(ServerChannel var0, AuthenticatedSubject var1) throws ConfigurationException, CertificateException {
      SSLContextWrapper var2 = getChannelSSLContext(var0, var1);
      return var2.getSSLNioServerSocketFactory();
   }

   public static SSLSocketFactory getSSLSocketFactory(ServerChannel var0, AuthenticatedSubject var1) throws ConfigurationException, CertificateException {
      SSLContextWrapper var2 = getChannelSSLContext(var0, var1);
      return var2.getSSLSocketFactory();
   }

   public static SSLSocketFactory getSSLNioSocketFactory(ServerChannel var0, AuthenticatedSubject var1) throws ConfigurationException, CertificateException {
      SSLContextWrapper var2 = getChannelSSLContext(var0, var1);
      return var2.getSSLNioSocketFactory();
   }

   public static SSLClientInfo getChannelSSLClientInfo(ServerChannel var0, AuthenticatedSubject var1) throws ConfigurationException, CertificateException {
      SSLContextWrapper var2 = getChannelSSLContext(var0, var1);
      SSLContextManager var3 = new SSLContextManager(var1);
      SSLIdentity var4 = var3.getServerSSLIdentity(((ServerChannelImpl)var0).getConfig(), var2, true);
      SSLClientInfo var5 = new SSLClientInfo();
      var5.loadLocalIdentity(var4.certChain, var4.key);
      return var5;
   }

   public static SSLClientInfo getNioChannelSSLClientInfo(ServerChannel var0, AuthenticatedSubject var1) throws ConfigurationException, CertificateException {
      SSLContextWrapper var2 = getChannelSSLContext(var0, var1);
      SSLContextManager var3 = new SSLContextManager(var1);
      SSLIdentity var4 = var3.getServerSSLIdentity(((ServerChannelImpl)var0).getConfig(), var2, true);
      SSLClientInfo var5 = new SSLClientInfo(true);
      var5.loadLocalIdentity(var4.certChain, var4.key);
      return var5;
   }

   public static synchronized SSLSocketFactory getDefaultSSLSocketFactory(AuthenticatedSubject var0) throws ConfigurationException, CertificateException {
      if (defaultFactory == null) {
         SSLContextWrapper var1 = getDefaultServerSSLContext(var0);
         defaultFactory = var1.getSSLSocketFactory();
      } else {
         ManagementService.getRuntimeAccess(var0);
      }

      return defaultFactory;
   }

   public static synchronized SSLSocketFactory getDefaultNioSSLSocketFactory(AuthenticatedSubject var0) throws ConfigurationException, CertificateException {
      if (defaultNioFactory == null) {
         SSLContextWrapper var1 = getDefaultServerSSLContext(var0);
         defaultNioFactory = var1.getSSLNioSocketFactory();
      } else {
         ManagementService.getRuntimeAccess(var0);
      }

      return defaultNioFactory;
   }

   public static SSLSocketFactory getSSLSocketFactory(AuthenticatedSubject var0, TrustManager var1) throws ConfigurationException, CertificateException {
      SSLContextManager var2 = new SSLContextManager(var0);
      SSLContextWrapper var3 = var2.createServerSSLContext((ServerChannel)null);
      var3.getTrustManager().setTrustManager(var1);
      return var3.getSSLSocketFactory();
   }

   public static SSLSocketFactory getSSLNioSocketFactory(AuthenticatedSubject var0, TrustManager var1) throws ConfigurationException, CertificateException {
      SSLContextManager var2 = new SSLContextManager(var0);
      SSLContextWrapper var3 = var2.createServerSSLContext((ServerChannel)null);
      var3.getTrustManager().setTrustManager(var1);
      return var3.getSSLNioSocketFactory();
   }

   public static synchronized X509Certificate[] getServerTrustedCAs() throws ConfigurationException, CertificateException {
      if (trustedCACerts != null) {
         X509Certificate[] var0 = new X509Certificate[trustedCACerts.length];
         System.arraycopy(trustedCACerts, 0, var0, 0, trustedCACerts.length);
         return var0;
      } else {
         return getDefaultServerSSLContext(kernelId).getTrustedCAs();
      }
   }

   public static synchronized void clearSSLContextCache() {
      trustedCACerts = null;
      defaultFactory = null;
      defaultNioFactory = null;
      defaultContext = null;
      channelContexts.clear();
      sslIdentities.clear();
   }

   public static X509Certificate[] getTrustedCAs(KeyStoreInfo[] var0) {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var0 != null && var2 < var0.length; ++var2) {
         Collection var3 = getTrustedCAs(var0[var2].getFileName(), var0[var2].getType(), var0[var2].getPassPhrase());
         if (var3 != null) {
            var1.addAll(var3);
         }
      }

      return (X509Certificate[])((X509Certificate[])var1.toArray(new X509Certificate[var1.size()]));
   }

   public static final SSLEngineFactory getSSLEngineFactory(ServerChannel var0, AuthenticatedSubject var1) throws ConfigurationException, CertificateException {
      SSLContextWrapper var2 = getChannelSSLContext(var0, var1);
      return JaSSLEngineFactoryBuilder.getFactoryInstance(var2);
   }

   private static synchronized SSLContextWrapper getDefaultServerSSLContext(AuthenticatedSubject var0) throws ConfigurationException, CertificateException {
      if (defaultContext == null) {
         SSLContextManager var1 = new SSLContextManager(var0);
         defaultContext = var1.createServerSSLContext((ServerChannel)null);
      } else {
         ManagementService.getRuntimeAccess(var0);
      }

      return defaultContext;
   }

   private static synchronized SSLContextWrapper getChannelSSLContext(ServerChannel var0, AuthenticatedSubject var1) throws ConfigurationException, CertificateException {
      boolean var2 = SSLSetup.isDebugEnabled(3);
      String var3 = ((ServerChannelImpl)var0).getConfig().getName();
      SSLContextWrapper var4 = (SSLContextWrapper)channelContexts.get(var3);
      if (var4 == null) {
         if (var2) {
            SSLSetup.info("SSLContextManager: initializing SSL context for channel " + var3);
         }

         SSLContextManager var5 = new SSLContextManager(var1);
         var4 = var5.createServerSSLContext(var0);
         channelContexts.put(var3, var4);
      } else {
         if (var2) {
            SSLSetup.info("SSLContextManager: reusing SSL context of channel " + var3);
         }

         ManagementService.getRuntimeAccess(var1);
      }

      return var4;
   }

   private SSLContextManager(AuthenticatedSubject var1) {
      checkLicense();
      if (var1 == null) {
         throw new NullPointerException("null privileged subject");
      } else {
         this.runtimeAccess = ManagementService.getRuntimeAccess(var1);
      }
   }

   private SSLContextWrapper createServerSSLContext(ServerChannel var1) throws ConfigurationException, CertificateException {
      SSLContextWrapper var2 = SSLContextWrapper.getInstance();
      SSLMBean var3 = this.runtimeAccess.getServer().getSSL();
      if (var3 != null) {
         boolean var4 = var3.isAllowUnencryptedNullCipher();
         var2.enableUnencryptedNullCipher(var4);
      }

      NetworkAccessPointMBean var12 = var1 == null ? null : ((ServerChannelImpl)var1).getConfig();
      SSLIdentity var5 = null;

      try {
         var5 = this.getServerSSLIdentity(var12, var2, false);
      } catch (ConfigurationException var11) {
         if (this.debug) {
            SSLSetup.info(var11, "SSLContextManager: couldnot get server SSL identity");
         }

         if (var1 != null) {
            throw var11;
         }
      }

      if (var5 != null) {
         this.checkIdentity(var2, var5);
         var2.addIdentity(var5.certChain, var5.key);
      }

      X509Certificate[] var6 = trustedCACerts;
      if (var6 == null) {
         var6 = this.getServerTrustedCAs(var2);
         Class var7 = SSLContextManager.class;
         synchronized(SSLContextManager.class) {
            trustedCACerts = var6;
         }

         if (var6 != null && var6.length > 0 && this.runtimeAccess.getDomain().isProductionModeEnabled()) {
            X509Certificate var13 = findDemoCert(var6);
            if (var13 != null) {
               SecurityLogger.logDemoTrustCertificateUsed(var13.toString());
            }
         }
      }

      boolean var14 = var12 != null ? var12.isClientCertificateEnforced() : var3.isClientCertificateEnforced();
      if (var6 == null || var6.length == 0) {
         if (var14) {
            fail(SecurityLogger.logClientCertEnforcedNoTrustedCALoggable(), (Throwable)null);
         }

         SecurityLogger.logNoTrustedCAsLoaded();
      }

      if (var6 != null) {
         var2.addTrustedCA(var6);
      }

      SSLTrustValidator var8 = var2.getTrustManager();
      var8.setPeerCertsRequired(var14);
      var8.setAllowOverride(false);
      var2.setTrustManager(var8);
      int var9 = var3.getExportKeyLifespan();
      var2.setExportRefreshCount(var9);
      T3SrvrLogger.logExportableKeyMaxLifespan(var9);
      return var2;
   }

   private X509Certificate[] getServerTrustedCAs(SSLContextWrapper var1) {
      if (this.usePerServerKeyStores()) {
         KeyStoreInfo[] var2 = (new KeyStoreConfigurationHelper(MBeanKeyStoreConfiguration.getInstance())).getTrustKeyStores();
         return getTrustedCAs(var2);
      } else {
         return this.getOldConfigServerTrustedCAs(var1);
      }
   }

   private static void checkLicense() {
      try {
         SSLSetup.getLicenseLevel();
      } catch (RuntimeException var1) {
         throw var1;
      }
   }

   private final boolean usePerServerKeyStores() {
      String var1 = this.runtimeAccess.getServer().getSSL().getIdentityAndTrustLocations();
      return "KeyStores".equals(var1);
   }

   private static X509Certificate findDemoCert(X509Certificate[] var0) {
      if (var0 != null) {
         for(int var1 = 0; var1 < var0.length; ++var1) {
            if (isDemoCertificate(var0[var1])) {
               return var0[var1];
            }
         }
      }

      return null;
   }

   private static boolean isDemoCertificate(X509Certificate var0) {
      String var2 = var0.getIssuerDN().getName();
      int var3 = var2.lastIndexOf("CN=CACERT");
      return var3 >= 0 && (var3 + "CN=CACERT".length() >= var2.length() || !Character.isLetter(var2.charAt(var3 + "CN=CACERT".length())));
   }

   private void checkIdentity(SSLContextWrapper var1, SSLIdentity var2) throws ConfigurationException {
      X509Certificate[] var3 = var2.certChain;
      PrivateKey var4 = var2.key;
      String var5;
      if (var3 != null && var3.length != 0) {
         var5 = null;

         try {
            for(int var6 = 0; var6 < var3.length; ++var6) {
               X509Certificate var14 = var3[var6];
               var14.checkValidity();
               if (var6 + 1 < var3.length) {
                  var14.verify(var3[var6 + 1].getPublicKey());
               } else if (var14.getIssuerDN().equals(var14.getSubjectDN())) {
                  var14.verify(var14.getPublicKey());
               }

               long var7 = (var14.getNotAfter().getTime() - System.currentTimeMillis()) / 86400000L;
               if (var7 <= 30L) {
                  T3SrvrLogger.logCertificateExpiresSoon(var7, var14.toString());
               }
            }
         } catch (CertificateExpiredException var10) {
            fail(SecurityLogger.logIdentityCertificateExpiredLoggable(var5.toString()), var10);
         } catch (CertificateNotYetValidException var11) {
            fail(SecurityLogger.logIdentityCertificateNotYetValidLoggable(var5.toString()), var11);
         } catch (SignatureException var12) {
            fail(SecurityLogger.logIdentityCertificateNotValidLoggable(var5.toString()), var12);
         } catch (Exception var13) {
            fail(SecurityLogger.logUnableToVerifyIdentityCertificateLoggable(var5.toString()), var13);
         }

         if (this.runtimeAccess.getDomain().isProductionModeEnabled()) {
            X509Certificate var15 = findDemoCert(var3);
            if (var15 != null) {
               SecurityLogger.logDemoIdentityCertificateUsed(var15.toString());
            }
         }

         try {
            if (!var1.doKeysMatch(var3[0].getPublicKey(), var4)) {
               fail(SecurityLogger.logCertificateAndPrivateKeyMismatchedLoggable(), (Throwable)null);
            }
         } catch (KeyManagementException var9) {
            if (this.debug) {
               SSLSetup.info(var9, "Key match check failed with exception, may not have access to private key data to perform this check");
            }

            SecurityLogger.logCantCheckKeyMatch();
         }

         if (!"RSA".equalsIgnoreCase(var4.getAlgorithm())) {
            SecurityLogger.logUnsupportedKeyAlgorithm(var4.getAlgorithm());
         }

      } else {
         var5 = T3SrvrLogger.logNoCertificatesSpecified();
         throw new ConfigurationException((new Loggable(var5, (Object[])null)).getMessageText());
      }
   }

   private static Collection getTrustedCAs(String var0, String var1, char[] var2) {
      boolean var3 = SSLSetup.isDebugEnabled(3);
      File var4 = var0 == null ? null : new File(var0);
      if (var4 != null) {
         SecurityLogger.logLoadTrustedCAsFromKeyStore(var4.getAbsolutePath(), var1);
         KeyStore var5 = KeyStoreUtils.load(var4, var2, var1);
         if (var5 != null) {
            try {
               Collection var6 = SSLCertUtility.getX509Certificates(var5);
               if (var3) {
                  SSLSetup.info("SSLContextManager: loaded " + var6.size() + " trusted CAs from " + var4.getAbsolutePath());
                  debugCerts(var6);
               }

               return var6;
            } catch (KeyStoreException var7) {
               SecurityLogger.logKeyStoreException(var0, ManagementService.getRuntimeAccess(kernelId).getServer().getName());
            }
         } else {
            SecurityLogger.logTrustedCAFromKeyStoreLoadFailed(var4.getAbsolutePath(), var1);
         }
      } else {
         SecurityLogger.logTrustedCAKeyStoreNotFound(var0, ManagementService.getRuntimeAccess(kernelId).getServer().getName());
      }

      return null;
   }

   private SSLIdentity getServerSSLIdentity(NetworkAccessPointMBean var1, SSLContextWrapper var2, boolean var3) throws ConfigurationException {
      if (this.debug) {
         SSLSetup.info("SSLContextManager: loading server SSL identity, forOutboundSSL=" + var3);
      }

      if (this.usePerServerKeyStores()) {
         KeyStoreConfigurationHelper var4 = new KeyStoreConfigurationHelper(MBeanKeyStoreConfiguration.getInstance(), var1);
         KeyStoreInfo var5 = var4.getIdentityKeyStore();
         if (var5 == null) {
            fail(SecurityLogger.logInvalidServerSSLConfigurationLoggable(this.getServerName()), (Throwable)null);
         }

         File var6 = this.findFile(var5.getFileName());
         if (var6 == null) {
            fail(SecurityLogger.logIdentityKeyStoreFileNotFoundLoggable(this.getServerName(), var5.getFileName()), (Throwable)null);
         }

         String var7 = var5.getType();
         char[] var8 = var5.getPassPhrase();
         String var9;
         char[] var10;
         if (var3) {
            var9 = var4.getOutboundPrivateKeyAlias();
            var10 = var4.getOutboundPrivateKeyPassPhrase();
         } else {
            var9 = var4.getIdentityAlias();
            var10 = var4.getIdentityPrivateKeyPassPhrase();
         }

         if (this.debug) {
            SSLSetup.info("forOutboundSSL=" + var3 + ", resolved alias=" + var9);
         }

         if (var9 == null) {
            fail(SecurityLogger.logIdentityKeyStoreAliasNotSpecifiedLoggable(this.getServerName()), (Throwable)null);
         }

         SSLIdentityKey var11 = new SSLIdentityKey(var6, var7, var9);
         SSLIdentity var12 = (SSLIdentity)sslIdentities.get(var11);
         if (var12 != null && var12.verify(var8, var10)) {
            if (this.debug) {
               SSLSetup.info("Reusing cached identity certs for keystore " + var6.getAbsolutePath() + ", and alias " + var9);
            }

            return var12;
         } else {
            SecurityLogger.logLoadIdentityCertificateFromKeyStore(var6.getAbsolutePath(), var7, var9);
            KeyStore var13 = KeyStoreUtils.load(var6, var8, var7);
            if (var13 == null) {
               fail(SecurityLogger.logIdentityKeyStoreLoadFailedLoggable(this.getServerName(), var6.getAbsolutePath(), var7), (Throwable)null);
            }

            if (var10 == null) {
               SecurityLogger.logSSLDidNotFindPrivateKeyPassPhrase(this.getServerName(), this.getRealmName());
            }

            Key var14 = null;
            Certificate[] var15 = null;

            try {
               var14 = var13.getKey(var9, var10);
               var15 = var13.getCertificateChain(var9);
            } catch (Exception var18) {
               fail(SecurityLogger.logFailedReadingIdentityEntryLoggable(this.getServerName(), var6.getAbsolutePath(), var9), var18);
            }

            if (!(var14 instanceof PrivateKey) || !(var15 instanceof Certificate[])) {
               fail(SecurityLogger.logIdentityEntryNotFoundUnderAliasLoggable(this.getServerName(), var6.getAbsolutePath(), var9), (Throwable)null);
            }

            X509Certificate[] var16 = new X509Certificate[var15.length];

            for(int var17 = 0; var17 < var16.length; ++var17) {
               var16[var17] = (X509Certificate)var15[var17];
            }

            if (this.debug) {
               SSLSetup.info("Loaded public identity certificate chain:");
               debugCerts(var16);
            }

            var12 = new SSLIdentity((PrivateKey)var14, var16, var8, var10);
            sslIdentities.put(var11, var12);
            return var12;
         }
      } else {
         return this.getOldConfigServerSSLIdentity(var2);
      }
   }

   private static void fail(Loggable var0, Throwable var1) throws ConfigurationException {
      var0.log();
      ConfigurationException var2 = new ConfigurationException(var0.getMessageText());
      if (var1 != null) {
         var2.initCause(var1);
      }

      throw var2;
   }

   private File findFile(String var1) {
      File var2 = null;
      if (var1 != null) {
         var2 = new File(var1);
         if (!var2.exists()) {
            var2 = new File(this.runtimeAccess.getServer().getRootDirectory(), var1);
            if (!var2.exists()) {
               var2 = null;
            }
         }
      }

      return var2;
   }

   private final String getServerName() {
      return this.runtimeAccess.getServer().getName();
   }

   private final String getRealmName() {
      return this.runtimeAccess.getDomain().getSecurity().getRealm().getName();
   }

   private SSLIdentity getOldConfigServerSSLIdentity(SSLContextWrapper var1) throws ConfigurationException {
      SSLMBean var2 = this.runtimeAccess.getServer().getSSL();
      PrivateKey var3 = null;
      KeyManager var4 = getKeyManager();
      String var5;
      String var6;
      if (var4 != null) {
         var5 = var2.getServerPrivateKeyAlias();
         if (this.debug) {
            SSLSetup.info("SSLContextManager: key alias - " + var5);
         }

         if (var5 != null) {
            var6 = var2.getServerPrivateKeyPassPhrase();
            if (this.debug) {
               SSLSetup.info("SSLContextManager: key passphrase: " + (var6 != null ? "<non-null>" : "<null>"));
            }

            if (var6 == null) {
               SecurityLogger.logSSLDidNotFindPrivateKeyPassPhrase(this.getServerName(), this.getRealmName());
            }

            KeyStore[] var7 = var4.getPrivateKeyStore();
            if (var7 != null && var7.length > 0) {
               if (this.debug) {
                  SSLSetup.info("Looking for key in keystore");
               }

               char[] var8 = var6 != null ? var6.toCharArray() : null;

               try {
                  for(int var9 = 0; var9 < var7.length; ++var9) {
                     if (var7[var9] != null) {
                        var3 = (PrivateKey)var7[var9].getKey(var5, var8);
                        if (var3 != null) {
                           SecurityLogger.logUsingPrivateKeyFromKeyStore(this.getServerName(), var5);
                           if (this.debug) {
                              SSLSetup.info("Found private key in keystore");
                           }
                           break;
                        }
                     }
                  }
               } catch (Exception var45) {
                  fail(SecurityLogger.logUnexpectedExceptionPrivateKeyStoreLoggable(this.getServerName(), this.getRealmName(), var5), var45);
               }

               if (var3 == null) {
                  String[] var52 = var4.getPrivateKeyStoreLocation();
                  String var10 = "<empty location>";
                  if (var52.length > 0) {
                     StringBuffer var11 = new StringBuffer();

                     for(int var12 = 0; var12 < var7.length; ++var12) {
                        if (var12 > 0) {
                           var11.append(" or ");
                        }

                        var11.append(var52[var12] != null && var52[var12].length() > 0 ? var52[var12] : "<empty location entry>");
                     }

                     var10 = var11.toString();
                  }

                  SecurityLogger.logPrivateKeyStoreNotFound(var5, var10, this.getServerName(), this.getRealmName());
                  throw new ConfigurationException(SecurityLogger.getCanNotFindPrivateKeyWithAlias(var5, var10));
               }
            }
         } else {
            SecurityLogger.logSSLDidNotFindPrivateKeyAlias(this.getServerName(), this.getRealmName());
         }
      } else {
         SecurityLogger.logCannotFindKeyManager(this.getServerName(), this.getRealmName());
      }

      if (var3 == null) {
         if (this.debug) {
            SSLSetup.info("Assuming 6.x file based configuration for SSL Server Identity");
         }

         var5 = var2.getServerKeyFileName();
         File var47 = this.findFile(var5);
         if (var47 == null) {
            SecurityLogger.logSSLKeyFileNameError(this.getServerName());
            throw new ConfigurationException(SecurityLogger.getCanNotFindPrivateKeyFile(this.getServerName(), var5));
         }

         FileInputStream var48 = null;

         try {
            var48 = new FileInputStream(var47);
            String var50 = ManagementService.getPropertyService(kernelId).getTimestamp3();
            if (keyFilePwd == null) {
               if (var50 == null) {
                  var50 = var2.getServerPrivateKeyPassPhrase();
               }

               keyFilePwd = var50 == null ? new char[1] : var50.toCharArray();
               ManagementService.getPropertyService(kernelId).updateTimestamp3();
            }

            var3 = var1.inputPrivateKey(var48, keyFilePwd);
            SecurityLogger.logFoundPrivateKeyInSSLConfig(this.getServerName());
         } catch (IOException var42) {
            throw new ConfigurationException(SecurityLogger.getCanNotPrivateKeyFile(var47.getAbsolutePath(), var42.getMessage()));
         } catch (KeyManagementException var43) {
            CommandLine var53 = CommandLine.getCommandLine();
            throw new ConfigurationException(SecurityLogger.getCanNotReadPrivateKeyFile(var47.getAbsolutePath(), var53.getAdminPKPasswordProp(), var43.getMessage()));
         } finally {
            try {
               if (var48 != null) {
                  var48.close();
               }
            } catch (Exception var39) {
            }

         }
      }

      var5 = null;
      var6 = var2.getServerCertificateFileName();
      File var49 = this.findFile(var6);
      if (var49 == null) {
         SecurityLogger.logSSLCertificateFileNameError(this.getServerName(), "ServerCertificateFileName");
         throw new ConfigurationException(SecurityLogger.getUnableToFindServerCertFile(this.getServerName(), var6));
      } else {
         FileInputStream var51 = null;

         X509Certificate[] var46;
         try {
            var51 = new FileInputStream(var49);
            var46 = SSLCertUtility.inputCertificateChain(var1, var51);
            ValidateCertChain.validateServerCertChain((Certificate[])var46);
         } catch (Exception var40) {
            throw (ConfigurationException)(new ConfigurationException(var40.getMessage())).initCause(var40);
         } finally {
            try {
               if (var51 != null) {
                  var51.close();
               }
            } catch (Exception var38) {
            }

         }

         if (this.debug) {
            SSLSetup.info("Loaded public identity certificate chain using old SSL configuration:");
            debugCerts(var46);
         }

         return new SSLIdentity(var3, var46);
      }
   }

   private X509Certificate[] getOldConfigServerTrustedCAs(SSLContextWrapper var1) {
      X509Certificate[] var2 = null;
      String var3 = CommandLine.getCommandLine().getKeyStoreFileName();
      String var18;
      if (var3 != null) {
         if (this.debug) {
            SSLSetup.info("SSLContextManager, loading trusted CAs from cmd line keystore: " + var3);
         }

         File var14 = this.findFile(var3);
         Collection var16 = getTrustedCAs(var14 == null ? var3 : var14.getAbsolutePath(), "jks", (char[])null);
         if (var16 != null) {
            var2 = (X509Certificate[])((X509Certificate[])var16.toArray(new X509Certificate[var16.size()]));
         }

         var18 = var2 == null ? "0" : String.valueOf(var2.length);
         SecurityLogger.logTrustedCAsLoadedFromCmdLnKeyStore(var18, var3);
         if (this.debug) {
            debugCerts(var2);
         }

         return var2;
      } else {
         KeyManager var4 = getKeyManager();
         if (var4 != null) {
            KeyStore[] var5 = var4.getRootCAKeyStore();
            boolean var6 = false;
            if (var5 != null) {
               for(int var7 = 0; var7 < var5.length && !var6; ++var7) {
                  if (var5[var7] != null) {
                     var6 = true;
                  }
               }
            }

            if (var6) {
               if (this.debug) {
                  SSLSetup.info("SSLContextManager, loading trusted CAs from RootCAKeyStore");
               }

               ArrayList var19 = new ArrayList();

               for(int var8 = 0; var8 < var5.length; ++var8) {
                  if (var5[var8] != null) {
                     try {
                        var19.addAll(SSLCertUtility.getX509Certificates(var5[var8]));
                     } catch (KeyStoreException var10) {
                        SSLSetup.debug(2, var10, "Unable to load trusted CAs KeyStore file (" + var8 + ")");
                     }
                  }
               }

               var2 = (X509Certificate[])((X509Certificate[])var19.toArray(new X509Certificate[var19.size()]));
               SecurityLogger.logTrustedCAsLoadedFromKeyStore(String.valueOf(var2.length));
               if (this.debug) {
                  debugCerts(var2);
               }

               return var2;
            }
         } else {
            SecurityLogger.logCannotFindKeyManager(this.getServerName(), this.getRealmName());
         }

         String var15 = this.runtimeAccess.getServer().getSSL().getTrustedCAFileName();
         if (var15 != null) {
            if (this.debug) {
               SSLSetup.info("SSLContextManager, loading trusted CAs from TrustedCAFile: " + var15);
            }

            File var17 = this.findFile(var15);
            if (var17 != null) {
               try {
                  FileInputStream var21 = new FileInputStream(var17);
                  var2 = SSLCertUtility.inputCertificateChain(var1, var21);
                  var21.close();
               } catch (FileNotFoundException var11) {
                  SecurityLogger.logTrustedCAFileNotFound(var15, this.getServerName());
                  if (this.debug) {
                     SSLSetup.info("Cannot find the specified trusted CA file " + var15);
                  }
               } catch (IOException var12) {
                  SecurityLogger.logCannotAccessTrustedCAFile(var15, this.getServerName());
                  if (this.debug) {
                     SSLSetup.info(var12, "The Server was not able to read trusted CA file " + var15);
                  }
               } catch (KeyManagementException var13) {
                  SecurityLogger.logInvalidTrustedCAFileFormat(var15, this.getServerName());
                  if (this.debug) {
                     SSLSetup.info(var13, "The Server was not able to read certificate from trusted CA file " + var15);
                  }
               }

               String var23 = var2 == null ? "0" : String.valueOf(var2.length);
               SecurityLogger.logTrustedCAsLoadedFromTrustedCAFile(var23);
               if (this.debug) {
                  debugCerts(var2);
               }

               return var2;
            }

            SecurityLogger.logTrustedCAFileNotFound(var15, this.getServerName());
            if (this.debug) {
               SSLSetup.info("Cannot find the specified trusted CA file " + var15);
            }
         }

         var18 = BootStrap.getWebLogicHome() + File.separator + "lib" + File.separator + "cacerts";
         if (this.debug) {
            SSLSetup.info("SSLContextManager, loading trusted CAs from default key store: " + var18);
         }

         Collection var20 = getTrustedCAs(var18, "jks", (char[])null);
         if (var20 != null) {
            var2 = (X509Certificate[])((X509Certificate[])var20.toArray(new X509Certificate[var20.size()]));
         }

         String var22 = var2 == null ? "0" : String.valueOf(var2.length);
         SecurityLogger.logTrustedCAsLoadedFromDefaultKeyStore(var22, var18);
         if (this.debug) {
            debugCerts(var2);
         }

         return var2;
      }
   }

   private static KeyManager getKeyManager() {
      try {
         return (KeyManager)SecurityServiceManager.getSecurityService(kernelId, "weblogicDEFAULT", ServiceType.KEYMANAGER);
      } catch (Exception var1) {
         SSLSetup.debug(1, var1, "Failed to get key manager");
         SecurityLogger.logSSLCouldNotGetSecurityService();
         return null;
      }
   }

   private static void debugCerts(Collection var0) {
      Iterator var1 = var0.iterator();

      while(var1.hasNext()) {
         SSLSetup.info(toString((X509Certificate)var1.next()));
      }

   }

   private static void debugCerts(X509Certificate[] var0) {
      for(int var1 = 0; var1 < var0.length; ++var1) {
         SSLSetup.info(toString(var0[var1]));
      }

   }

   private static String toString(X509Certificate var0) {
      return "Subject: " + var0.getSubjectDN() + "; Issuer: " + var0.getIssuerDN();
   }

   private static class SSLIdentityKey {
      public File ksFile;
      public String ksType;
      public String alias;

      public SSLIdentityKey(File var1, String var2, String var3) {
         assert var1 != null : "null keystore file";

         assert var2 != null : "null keystore type";

         assert var3 != null : "null key alias";

         this.ksFile = var1;
         this.ksType = var2.toUpperCase(Locale.ENGLISH);
         this.alias = var3;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (!(var1 instanceof SSLIdentityKey)) {
            return false;
         } else {
            SSLIdentityKey var2 = (SSLIdentityKey)var1;
            return this.alias.equals(var2.alias) && this.ksType.equals(var2.ksType) && this.ksFile.equals(var2.ksFile);
         }
      }

      public int hashCode() {
         int var1 = this.ksFile.hashCode();
         var1 = 31 * var1 + this.ksType.hashCode();
         var1 = 31 * var1 + this.alias.hashCode();
         return var1;
      }
   }

   private static class SSLIdentity {
      public PrivateKey key;
      public X509Certificate[] certChain;
      private char[] ksPwd;
      private char[] keyPwd;

      public SSLIdentity(PrivateKey var1, X509Certificate[] var2) {
         this(var1, var2, (char[])null, (char[])null);
      }

      public SSLIdentity(PrivateKey var1, X509Certificate[] var2, char[] var3, char[] var4) {
         this.key = var1;
         this.certChain = var2;
         this.ksPwd = var3;
         this.keyPwd = var4;
      }

      public boolean verify(char[] var1, char[] var2) {
         return Arrays.equals(this.ksPwd, var1) && Arrays.equals(this.keyPwd, var2);
      }
   }
}
