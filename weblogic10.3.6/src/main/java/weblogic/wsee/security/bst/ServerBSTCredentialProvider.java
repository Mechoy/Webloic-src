package weblogic.wsee.security.bst;

import java.io.File;
import java.security.AccessController;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.utils.KeyStoreConfigurationHelper;
import weblogic.security.utils.KeyStoreInfo;
import weblogic.security.utils.KeyStoreUtils;
import weblogic.security.utils.MBeanKeyStoreConfiguration;
import weblogic.wsee.security.configuration.WssConfigurationException;
import weblogic.wsee.util.Verbose;
import weblogic.xml.crypto.utils.CertUtils;
import weblogic.xml.crypto.wss.BSTUtils;
import weblogic.xml.crypto.wss.WssPolicyContextHandler;
import weblogic.xml.crypto.wss.X509Credential;
import weblogic.xml.crypto.wss.provider.Purpose;

public class ServerBSTCredentialProvider extends BST11CredentialProvider {
   private static X509Credential SSLKeyPairCredential = null;
   private X509Credential credForIntegrity = null;
   private X509Credential credForConfidentiality = null;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public Object getCredential(String var1, String var2, ContextHandler var3, Purpose var4) {
      if (var3 instanceof WssPolicyContextHandler) {
         return isForEncryption(var4) ? this.credForConfidentiality : null;
      } else if (isForEncryption(var4)) {
         return null;
      } else if ((isForSigning(var4) || isForIdentity(var4)) && BSTUtils.matches(this.credForIntegrity, var3)) {
         return this.credForIntegrity;
      } else if (isForDecryption(var4) && BSTUtils.matches(this.credForConfidentiality, var3)) {
         return this.credForConfidentiality;
      } else {
         return isForVerification(var4) && BSTUtils.matches(this.credForIntegrity, var3) ? this.credForIntegrity : null;
      }
   }

   public void initCredentials(WssPolicyContextHandler var1) throws WssConfigurationException {
      KeyStoreConfigurationHelper var2 = new KeyStoreConfigurationHelper(MBeanKeyStoreConfiguration.getInstance());
      if (SSLKeyPairCredential == null) {
         this.initSSLCredential(var2);
      }

      this.credForIntegrity = initCredentialFromContext(var1, var2, true);
      this.credForConfidentiality = initCredentialFromContext(var1, var2, false);
   }

   private static X509Credential initCredentialFromContext(WssPolicyContextHandler var0, KeyStoreConfigurationHelper var1, boolean var2) throws WssConfigurationException {
      String var3;
      char[] var4;
      String var5;
      char[] var6;
      if (var2) {
         var3 = (String)var0.getValue("IntegrityKeyAlias");
         var4 = (char[])((char[])convertToCharArrayObject(var0.getValue("IntegrityKeyPassword")));
         var5 = (String)var0.getValue("IntegrityKeyStore");
         var6 = (char[])((char[])convertToCharArrayObject(var0.getValue("IntegrityKeyStorePassword")));
      } else {
         var3 = (String)var0.getValue("ConfidentialityKeyAlias");
         var4 = (char[])((char[])convertToCharArrayObject(var0.getValue("ConfidentialityKeyPassword")));
         var5 = (String)var0.getValue("ConfidentialityKeyStore");
         var6 = (char[])((char[])convertToCharArrayObject(var0.getValue("ConfidentialityKeyStorePassword")));
      }

      if (var3 == null) {
         return SSLKeyPairCredential;
      } else if (var4 == null) {
         throw new WssConfigurationException("Must specify private key pass for alias: " + var3);
      } else {
         KeyStore var7;
         if (var5 == null) {
            KeyStoreInfo var8 = var1.getIdentityKeyStore();
            if (var8 == null) {
               throw new WssConfigurationException("can't find the keystore of alias: " + var3);
            }

            var7 = KeyStoreUtils.load(getKeyStoreFile(var8.getFileName()), var8.getPassPhrase(), var8.getType());
         } else {
            if (var6 == null) {
               throw new WssConfigurationException("Must specify keystore passphase for keystore: " + var5);
            }

            var7 = KeyStoreUtils.load(getKeyStoreFile(var5), var6, getKeyStoreType(var0));
         }

         X509Credential var9 = getX509Credential(var7, var3, var4);
         if (var2 && !CertUtils.supportsSign(var9.getCertificate())) {
            throw new WssConfigurationException("Key/Certificate specified for integrity does not support signing.");
         } else if (!var2 && !CertUtils.supportsKeyEncrypt(var9.getCertificate())) {
            throw new WssConfigurationException("Key/Certificate specified for confidentiality does not support key encryption.");
         } else {
            return var9;
         }
      }
   }

   private static String getKeyStoreType(WssPolicyContextHandler var0) {
      String var1 = (String)var0.getValue("KeyStoreType");
      if (var1 == null) {
         var1 = "JKS";
      }

      return var1;
   }

   private static X509Credential getX509Credential(KeyStore var0, String var1, char[] var2) throws WssConfigurationException {
      try {
         Key var3 = var0.getKey(var1, var2);
         if (!(var3 instanceof PrivateKey)) {
            throw new WssConfigurationException("Private Key not found");
         } else {
            Certificate[] var4 = var0.getCertificateChain(var1);
            if (var4 == null) {
               throw new WssConfigurationException("Can not find any public key for alias: " + var1);
            } else {
               List var5 = Arrays.asList(var4);
               if (var5.size() < 1) {
                  throw new WssConfigurationException("Certificate not found");
               } else {
                  return new X509Credential((X509Certificate)var5.get(0), (PrivateKey)var3);
               }
            }
         }
      } catch (NoSuchAlgorithmException var6) {
         throw new WssConfigurationException(var6);
      } catch (UnrecoverableKeyException var7) {
         throw new WssConfigurationException(var7);
      } catch (KeyStoreException var8) {
         throw new WssConfigurationException(var8);
      }
   }

   private static File getKeyStoreFile(String var0) throws WssConfigurationException {
      File var1 = new File(var0);
      if (!var1.exists()) {
         var1 = new File(ManagementService.getRuntimeAccess(kernelId).getServer().getRootDirectory(), var0);
         if (!var1.exists()) {
            throw new WssConfigurationException("KeyStoreFile does not exist");
         }
      }

      return var1;
   }

   private void initSSLCredential(KeyStoreConfigurationHelper var1) throws WssConfigurationException {
      KeyStoreInfo var2 = var1.getIdentityKeyStore();
      if (var2 != null) {
         String var3 = var2.getFileName();
         if (var3 != null && var3.length() != 0) {
            File var4 = getKeyStoreFile(var3);
            String var5 = var2.getType();
            char[] var6 = var2.getPassPhrase();
            String var7 = var1.getIdentityAlias();
            char[] var8 = var1.getIdentityPrivateKeyPassPhrase();
            if (var7 != null && var7.length() != 0) {
               if (var8 == null) {
                  throw new WssConfigurationException("PassPhrase not supplied");
               } else {
                  if (verbose) {
                     Verbose.log((Object)("KeyStore File:  " + var4.getAbsolutePath()));
                     Verbose.log((Object)("KeyStore Type:  " + var5));
                     Verbose.log((Object)("KeyStore Alias: " + var7));
                     Provider[] var9 = Security.getProviders();
                     Verbose.log((Object)"Security Providers:  ");

                     for(int var10 = 0; var10 < var9.length; ++var10) {
                        Verbose.log((Object)("  " + var9[var10].getName() + "  " + var9[var10].getVersion()));
                     }
                  }

                  KeyStore var11 = KeyStoreUtils.load(var4, var6, var5);
                  if (var11 == null) {
                     throw new WssConfigurationException("Unable to load KeyStore");
                  } else {
                     SSLKeyPairCredential = getX509Credential(var11, var7, var8);
                     this.credForIntegrity = SSLKeyPairCredential;
                     this.credForConfidentiality = SSLKeyPairCredential;
                  }
               }
            } else {
               throw new WssConfigurationException("Certificate Alias not supplied");
            }
         } else {
            throw new WssConfigurationException("KeyStoreFilename not supplied");
         }
      }
   }

   public static boolean isSSLUsingKeyStores() {
      RuntimeAccess var0 = ManagementService.getRuntimeAccess(kernelId);
      return var0 != null ? "KeyStores".equals(var0.getServer().getSSL().getIdentityAndTrustLocations()) : false;
   }

   private static Object convertToCharArrayObject(Object var0) {
      return var0 instanceof String ? ((String)var0).toCharArray() : var0;
   }
}
