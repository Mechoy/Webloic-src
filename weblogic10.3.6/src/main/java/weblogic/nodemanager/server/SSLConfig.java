package weblogic.nodemanager.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Properties;
import java.util.logging.Level;
import weblogic.nodemanager.NodeManagerTextTextFormatter;
import weblogic.nodemanager.common.Config;
import weblogic.nodemanager.common.ConfigException;
import weblogic.security.utils.KeyStoreConfiguration;
import weblogic.security.utils.KeyStoreConfigurationHelper;
import weblogic.security.utils.KeyStoreInfo;
import weblogic.security.utils.SSLCertUtility;
import weblogic.security.utils.SSLContextWrapper;

class SSLConfig extends Config {
   private String keyStores = "DemoIdentityAndDemoTrust";
   private String customIdentityKeyStoreFileName;
   private String customIdentityKeyStoreType;
   private String customIdentityKeyStorePassPhrase;
   private String customIdentityAlias;
   private String customIdentityPrivateKeyPassPhrase;
   private String cipherSuite;
   private String keyFile = "config/demokey.pm";
   private String keyPassword = "password";
   private String certificateFile = "config/democert.pm";
   private PrivateKey privateKey;
   private X509Certificate[] certChain;
   private Encryptor encryptor;
   public static final String KEY_STORES_PROP = "KeyStores";
   public static final String CUSTOM_IDENTITY_KEY_STORE_FILE_NAME_PROP = "CustomIdentityKeyStoreFileName";
   public static final String CUSTOM_IDENTITY_KEY_STORE_TYPE_PROP = "CustomIdentityKeyStoreType";
   public static final String CUSTOM_IDENTITY_KEY_STORE_PASS_PHRASE_PROP = "CustomIdentityKeyStorePassPhrase";
   public static final String CUSTOM_IDENTITY_ALIAS_PROP = "CustomIdentityAlias";
   public static final String CUSTOM_IDENTITY_PRIVATE_KEY_PASS_PHRASE_PROP = "CustomIdentityPrivateKeyPassPhrase";
   public static final String CUSTOM_TRUST_KEY_STORE_PASS_PHRASE_PROP = "CustomTrustKeyStorePassPhrase";
   public static final String JAVA_STANDARD_TRUST_KEY_STORE_PASS_PHRASE_PROP = "JavaStandardTrustKeyStorePassPhrase";
   public static final String CIPHER_SUITE_PROP = "CipherSuite";
   public static final String KEY_FILE_PROP = "keyFile";
   public static final String KEY_PASSWORD_PROP = "keyPassword";
   public static final String CERTIFICATE_FILE_PROP = "certificateFile";
   public static final String DEMO_IDENTITY = "DemoIdentity";
   public static final String CUSTOM_IDENTITY = "CustomIdentity";
   private static final NodeManagerTextTextFormatter nmText = NodeManagerTextTextFormatter.getInstance();

   public SSLConfig(Properties var1, Encryptor var2) throws IOException, ConfigException {
      super(var1);
      this.encryptor = var2;
      this.loadProperties();
      var1.remove("CustomIdentityKeyStorePassPhrase");
      var1.remove("CustomIdentityPrivateKeyPassPhrase");
      if (this.keyFile != null && this.keyPassword != null && this.certificateFile != null) {
         this.loadCompatibilityConfig();
      } else {
         this.loadKeyStoreConfig();
      }

   }

   private void loadProperties() {
      this.keyStores = this.getProperty("KeyStores", this.keyStores);
      if ("DemoIdentity".equals(this.keyStores)) {
         this.keyStores = "DemoIdentityAndDemoTrust";
      } else if ("CustomIdentity".equals(this.keyStores)) {
         this.keyStores = "CustomIdentityAndCustomTrust";
      }

      this.customIdentityKeyStoreFileName = this.getProperty("CustomIdentityKeyStoreFileName");
      this.customIdentityKeyStoreType = this.getProperty("CustomIdentityKeyStoreType");
      this.customIdentityAlias = this.getProperty("CustomIdentityAlias");
      this.customIdentityKeyStorePassPhrase = this.encryptor.encrypt(this.getProperty("CustomIdentityKeyStorePassPhrase"));
      this.customIdentityPrivateKeyPassPhrase = this.encryptor.encrypt(this.getProperty("CustomIdentityPrivateKeyPassPhrase"));
      this.cipherSuite = this.getProperty("CipherSuite");
      this.keyFile = this.getProperty("keyFile");
      this.keyPassword = this.getProperty("keyPassword");
      this.certificateFile = this.getProperty("certificateFile");
   }

   private void loadKeyStoreConfig() throws IOException, ConfigException {
      KeyStoreConfigurationHelper var1 = new KeyStoreConfigurationHelper(new KeyStoreConfig());
      KeyStoreInfo var2 = var1.getIdentityKeyStore();
      NMServer.nmLog.info(nmText.getLoadingIDStore(var2.toString()));

      KeyStore var3;
      try {
         var3 = KeyStore.getInstance(var2.getType());
      } catch (KeyStoreException var25) {
         throw new ConfigException(nmText.getUnknownIDStoreType(var2.getType().toString()));
      }

      FileInputStream var4;
      try {
         var4 = new FileInputStream(var2.getFileName());
      } catch (FileNotFoundException var24) {
         throw new ConfigException(nmText.getIDStoreNotFound(var2.getFileName()));
      }

      try {
         var3.load(var4, var2.getPassPhrase());
      } catch (NoSuchAlgorithmException var21) {
         throw new ConfigException(nmText.getIDAlgorithmNotFound(), var21);
      } catch (CertificateException var22) {
         throw new ConfigException(nmText.getCertificatesNotLoaded());
      } finally {
         var4.close();
      }

      String var5 = var1.getIdentityAlias();

      try {
         this.privateKey = (PrivateKey)var3.getKey(var5, var1.getIdentityPrivateKeyPassPhrase());
      } catch (UnrecoverableKeyException var18) {
         throw new ConfigException(nmText.getIncorrectIDPassword());
      } catch (NoSuchAlgorithmException var19) {
         throw new ConfigException(nmText.getIDAlgorithmNotFound(), var19);
      } catch (KeyStoreException var20) {
         throw new InternalError("Identity key store not initialized");
      }

      if (this.privateKey == null) {
         throw new ConfigException(nmText.getUnknownKeyStoreID(var5));
      } else {
         Certificate[] var6;
         try {
            var6 = var3.getCertificateChain(var5);
         } catch (KeyStoreException var17) {
            throw new IllegalStateException(nmText.getIdentityStoreNotInit());
         }

         if (var6 != null && var6.length != 0) {
            this.certChain = SSLCertUtility.toJavaX5092(var6);
            if (this.certChain == null) {
               throw new ConfigException(nmText.getNoX509());
            }
         } else {
            throw new ConfigException(nmText.getNoCertificate(var5));
         }
      }
   }

   private void loadCompatibilityConfig() throws IOException, ConfigException {
      SSLContextWrapper var1;
      try {
         var1 = SSLContextWrapper.getInstance();
      } catch (Exception var26) {
         throw (InternalError)(new InternalError("Could not instantiate SSLContextWrapper")).initCause(var26);
      }

      FileInputStream var2;
      try {
         var2 = new FileInputStream(this.certificateFile);
      } catch (FileNotFoundException var25) {
         throw new ConfigException(nmText.getCertificateFileNF(this.certificateFile.toString()));
      }

      try {
         this.certChain = var1.inputCertChain(var2);
      } catch (KeyManagementException var23) {
         throw (ConfigException)(new ConfigException(nmText.getInvalidCertFile(this.certificateFile.toString()))).initCause(var23);
      } finally {
         var2.close();
      }

      try {
         var2 = new FileInputStream(this.keyFile);
      } catch (FileNotFoundException var22) {
         throw new ConfigException(nmText.getKeyFileNotFound(this.keyFile.toString()));
      }

      try {
         char[] var3 = null;
         if (this.keyPassword != null && this.keyPassword.length() > 0) {
            var3 = this.keyPassword.toCharArray();
         }

         this.privateKey = var1.inputPrivateKey(var2, var3);
      } catch (KeyManagementException var27) {
         throw (ConfigException)(new ConfigException(nmText.getInvalidKeyFile(this.keyFile.toString()))).initCause(var27);
      } finally {
         var2.close();
      }

   }

   public PrivateKey getIdentityPrivateKey() {
      return this.privateKey;
   }

   public X509Certificate[] getIdentityCertificateChain() {
      return this.certChain;
   }

   public String getCipherSuite() {
      return this.cipherSuite;
   }

   public static boolean checkUpgrade(Properties var0, Encryptor var1, boolean var2) {
      boolean var3 = false;
      String var4 = var0.getProperty("CustomIdentityKeyStorePassPhrase");
      String var5 = var0.getProperty("CustomIdentityPrivateKeyPassPhrase");
      String var6 = var0.getProperty("CustomTrustKeyStorePassPhrase");
      String var7 = var0.getProperty("JavaStandardTrustKeyStorePassPhrase");
      String var8 = var1.encrypt(var4);
      String var9 = var1.encrypt(var5);
      if (var4 != null && !var4.equals(var8)) {
         var0.setProperty("CustomIdentityKeyStorePassPhrase", var8);
         if (var2) {
            Upgrader.log(Level.INFO, nmText.getEncryptingProp("CustomIdentityKeyStorePassPhrase"));
         }

         var3 = true;
      }

      if (var5 != null && !var5.equals(var9)) {
         var0.setProperty("CustomIdentityPrivateKeyPassPhrase", var9);
         if (var2) {
            Upgrader.log(Level.INFO, nmText.getEncryptingProp("CustomIdentityPrivateKeyPassPhrase"));
         }

         var3 = true;
      }

      if (var6 != null) {
         var0.remove("CustomTrustKeyStorePassPhrase");
         if (var2) {
            Upgrader.log(Level.INFO, nmText.getRemovingProp("CustomTrustKeyStorePassPhrase"));
         }

         var3 = true;
      }

      if (var7 != null) {
         var0.remove("JavaStandardTrustKeyStorePassPhrase");
         if (var2) {
            Upgrader.log(Level.INFO, nmText.getRemovingProp("JavaStandardTrustKeyStorePassPhrase"));
         }

         var3 = true;
      }

      return var3;
   }

   private class KeyStoreConfig implements KeyStoreConfiguration {
      private KeyStoreConfig() {
      }

      public String getKeyStores() {
         return SSLConfig.this.keyStores;
      }

      public String getCustomIdentityKeyStoreFileName() {
         return SSLConfig.this.customIdentityKeyStoreFileName;
      }

      public String getCustomIdentityKeyStoreType() {
         return SSLConfig.this.customIdentityKeyStoreType;
      }

      public String getCustomIdentityKeyStorePassPhrase() {
         return SSLConfig.this.encryptor.decrypt(SSLConfig.this.customIdentityKeyStorePassPhrase);
      }

      public String getCustomIdentityAlias() {
         return SSLConfig.this.customIdentityAlias;
      }

      public String getCustomIdentityPrivateKeyPassPhrase() {
         return SSLConfig.this.encryptor.decrypt(SSLConfig.this.customIdentityPrivateKeyPassPhrase);
      }

      public String getCustomTrustKeyStoreFileName() {
         return null;
      }

      public String getCustomTrustKeyStoreType() {
         return null;
      }

      public String getCustomTrustKeyStorePassPhrase() {
         return null;
      }

      public String getJavaStandardTrustKeyStorePassPhrase() {
         return null;
      }

      public String getOutboundPrivateKeyAlias() {
         return null;
      }

      public String getOutboundPrivateKeyPassPhrase() {
         return null;
      }

      // $FF: synthetic method
      KeyStoreConfig(Object var2) {
         this();
      }
   }
}
