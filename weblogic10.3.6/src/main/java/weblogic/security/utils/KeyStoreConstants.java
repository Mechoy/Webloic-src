package weblogic.security.utils;

import java.io.File;
import weblogic.Home;

public class KeyStoreConstants {
   public static final String DEMO_CA_NAME = "CACERT";
   public static final String DEMO_IDENTITY_KEYSTORE_FILENAME = "DemoIdentity.jks";
   public static final String DEMO_IDENTITY_KEYSTORE_PASSPHRASE = "DemoIdentityKeyStorePassPhrase";
   public static final String DEMO_IDENTITY_KEYSTORE_TYPE = "jks";
   public static final String DEMO_IDENTITY_ALIAS = "DemoIdentity";
   public static final String DEMO_IDENTITY_PRIVATE_KEY_PASSPHRASE = "DemoIdentityPassPhrase";
   public static final String DEMO_TRUST_KEYSTORE_PASSPHRASE = "DemoTrustKeyStorePassPhrase";
   public static final String DEMO_TRUST_KEYSTORE_TYPE = "jks";
   public static final String COMPATIBILITY_DEMO_TRUST_KEYSTORE_TYPE = "jks";
   public static final String JAVA_STANDARD_TRUST_KEYSTORE_TYPE = "jks";
   public static final String DEMO_IDENTITY_AND_DEMO_TRUST = "DemoIdentityAndDemoTrust";
   public static final String CUSTOM_IDENTITY_AND_JAVA_STANDARD_TRUST = "CustomIdentityAndJavaStandardTrust";
   public static final String CUSTOM_IDENTITY_AND_CUSTOM_TRUST = "CustomIdentityAndCustomTrust";
   public static final String CUSTOM_IDENTITY_AND_COMMAND_LINE_TRUST = "CustomIdentityAndCommandLineTrust";
   public static final String DEMO_TRUST = "DemoTrust";
   public static final String JAVA_STANDARD_TRUST = "JavaStandardTrust";
   public static final String CUSTOM_TRUST = "CustomTrust";
   public static final String TRUST_KEYSTORE_PROP = "weblogic.security.TrustKeyStore";
   public static final String CUSTOM_TRUST_KEYSTORE_FILENAME_PROP = "weblogic.security.CustomTrustKeyStoreFileName";
   public static final String CUSTOM_TRUST_KEYSTORE_TYPE_PROP = "weblogic.security.CustomTrustKeyStoreType";
   public static final String CUSTOM_TRUST_KEYSTORE_PASSPHRASE_PROP = "weblogic.security.CustomTrustKeyStorePassPhrase";
   public static final String JAVA_STANDARD_TRUST_KEYSTORE_PASSPHRASE_PROP = "weblogic.security.JavaStandardTrustKeyStorePassPhrase";
   public static final String TRUST_KEYSTORE_BOOT_PROP = "TrustKeyStore";
   public static final String CUSTOM_TRUST_KEYSTORE_FILENAME_BOOT_PROP = "CustomTrustKeyStoreFileName";
   public static final String CUSTOM_TRUST_KEYSTORE_TYPE_BOOT_PROP = "CustomTrustKeyStoreType";
   public static final String CUSTOM_TRUST_KEYSTORE_PASSPHRASE_BOOT_PROP = "CustomTrustKeyStorePassPhrase";
   public static final String JAVA_STANDARD_TRUST_KEYSTORE_PASSPHRASE_BOOT_PROP = "JavaStandardTrustKeyStorePassPhrase";

   private static final String getWLSLibRelativePath(String var0) {
      return Home.getPath() + File.separator + "lib" + File.separator + var0;
   }

   public static final String getDemoIdentityKeyStoreFileName() {
      return getWLSLibRelativePath("DemoIdentity.jks");
   }

   public static final String getDemoTrustKeyStoreFileName() {
      return getWLSLibRelativePath("DemoTrust.jks");
   }

   public static final String getDemoCompatibilityTrustKeyStoreFileName() {
      return getWLSLibRelativePath("cacerts");
   }

   public static final String getJavaStandardTrustKeyStoreFileName() {
      return System.getProperty("java.home") + File.separator + "lib" + File.separator + "security" + File.separator + "cacerts";
   }
}
