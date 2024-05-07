package weblogic.security.utils;

import weblogic.security.SecurityLogger;
import weblogic.security.internal.SerializedSystemIni;
import weblogic.security.internal.encryption.ClearOrEncryptedService;
import weblogic.security.shared.LoggerWrapper;

public class PreMBeanKeyStoreConfiguration implements KeyStoreConfiguration {
   private static LoggerWrapper logger = LoggerWrapper.getInstance("SecurityKeyStore");
   private String keystores;
   private static PreMBeanKeyStoreConfiguration theInstance;
   private ClearOrEncryptedService ces;

   public static synchronized PreMBeanKeyStoreConfiguration getInstance() {
      if (theInstance == null) {
         theInstance = new PreMBeanKeyStoreConfiguration();
      }

      return theInstance;
   }

   private void debug(String var1) {
      logger.debug("PreMBeanKeyStoreConfiguration: " + var1);
   }

   private String getProperty(String var1) {
      return System.getProperty(var1);
   }

   private PreMBeanKeyStoreConfiguration() {
      boolean var1 = false;
      String var2 = this.getProperty("weblogic.security.TrustKeyStore");
      String var3 = this.getProperty("weblogic.security.CustomTrustKeyStoreFileName");
      if ("DemoTrust".equals(var2)) {
         this.keystores = "DemoIdentityAndDemoTrust";
      } else if ("JavaStandardTrust".equals(var2)) {
         this.keystores = "CustomIdentityAndJavaStandardTrust";
      } else if ("CustomTrust".equals(var2)) {
         String var4 = this.getProperty("weblogic.security.CustomTrustKeyStoreFileName");
         if (var4 == null || var4.length() < 1) {
            var1 = true;
         }

         this.keystores = "CustomIdentityAndCustomTrust";
      } else {
         if (var2 != null && var2.length() > 0) {
            var1 = true;
         }

         this.keystores = "DemoIdentityAndDemoTrust";
      }

      if (var1) {
         this.keystores = null;
         SecurityLogger.logServerTrustKeyStoreConfigError();
      }

      if (logger.isDebugEnabled()) {
         this.debug("constructor - explicitly configured=" + this.isExplicitlyConfigured());
         KeyStoreInfo[] var6 = (new KeyStoreConfigurationHelper(this)).getTrustKeyStores();

         for(int var5 = 0; var6 != null && var5 < var6.length; ++var5) {
            this.debug("constructor - TrustKeyStore[" + var5 + "]=" + var6[var5]);
         }
      }

      this.ces = new ClearOrEncryptedService(SerializedSystemIni.getEncryptionService());
   }

   public boolean isExplicitlyConfigured() {
      if (this.getProperty("weblogic.security.TrustKeyStore") != null) {
         return true;
      } else if (this.getProperty("weblogic.security.CustomTrustKeyStoreFileName") != null) {
         return true;
      } else if (this.getProperty("weblogic.security.CustomTrustKeyStoreType") != null) {
         return true;
      } else if (this.getProperty("weblogic.security.CustomTrustKeyStorePassPhrase") != null) {
         return true;
      } else {
         return this.getProperty("weblogic.security.JavaStandardTrustKeyStorePassPhrase") != null;
      }
   }

   public String getKeyStores() {
      return this.keystores;
   }

   public String getCustomIdentityKeyStoreFileName() {
      return null;
   }

   public String getCustomIdentityKeyStoreType() {
      return null;
   }

   public String getCustomIdentityKeyStorePassPhrase() {
      return null;
   }

   public String getCustomIdentityAlias() {
      return null;
   }

   public String getCustomIdentityPrivateKeyPassPhrase() {
      return null;
   }

   public String getOutboundPrivateKeyAlias() {
      return null;
   }

   public String getOutboundPrivateKeyPassPhrase() {
      return null;
   }

   public String getCustomTrustKeyStoreFileName() {
      return this.getProperty("weblogic.security.CustomTrustKeyStoreFileName");
   }

   public String getCustomTrustKeyStoreType() {
      return this.getProperty("weblogic.security.CustomTrustKeyStoreType");
   }

   public String getCustomTrustKeyStorePassPhrase() {
      return this.decryptValue(this.getProperty("weblogic.security.CustomTrustKeyStorePassPhrase"));
   }

   public String getJavaStandardTrustKeyStorePassPhrase() {
      return this.decryptValue(this.getProperty("weblogic.security.JavaStandardTrustKeyStorePassPhrase"));
   }

   private String decryptValue(String var1) {
      return this.ces != null && var1 != null ? this.ces.decrypt(var1) : var1;
   }
}
