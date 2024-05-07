package weblogic.security.utils;

import java.io.File;
import weblogic.management.configuration.NetworkAccessPointMBean;

public class KeyStoreConfigurationHelper {
   private KeyStoreConfiguration config;
   private NetworkAccessPointMBean channel = null;

   private boolean isValid() {
      return this.config.getKeyStores() != null;
   }

   private boolean isDemoIdentity() {
      return "DemoIdentityAndDemoTrust".equals(this.config.getKeyStores());
   }

   private boolean isDemoTrust() {
      return "DemoIdentityAndDemoTrust".equals(this.config.getKeyStores());
   }

   private boolean isJavaStandardTrust() {
      return "CustomIdentityAndJavaStandardTrust".equals(this.config.getKeyStores());
   }

   private boolean isCustomTrust() {
      return "CustomIdentityAndCustomTrust".equals(this.config.getKeyStores());
   }

   private String emptyToNull(String var1) {
      return var1 != null && var1.length() < 1 ? null : var1;
   }

   private String getAbsolutePath(String var1) {
      return this.emptyToNull(var1) == null ? null : (new File(var1)).getAbsolutePath();
   }

   public KeyStoreConfigurationHelper(KeyStoreConfiguration var1) {
      this.config = var1;
   }

   public KeyStoreConfigurationHelper(KeyStoreConfiguration var1, NetworkAccessPointMBean var2) {
      this.config = var1;
      this.channel = var2;
   }

   private KeyStoreInfo getDemoIdentityKeyStoreInfo() {
      return new KeyStoreInfo(KeyStoreConstants.getDemoIdentityKeyStoreFileName(), "jks", "DemoIdentityKeyStorePassPhrase");
   }

   private KeyStoreInfo getCustomIdentityKeyStoreInfo() {
      return new KeyStoreInfo(this.getAbsolutePath(this.config.getCustomIdentityKeyStoreFileName()), this.emptyToNull(this.config.getCustomIdentityKeyStoreType()), this.emptyToNull(this.config.getCustomIdentityKeyStorePassPhrase()));
   }

   private KeyStoreInfo getDemoTrustKeyStoreInfo() {
      return new KeyStoreInfo(KeyStoreConstants.getDemoTrustKeyStoreFileName(), "jks", "DemoTrustKeyStorePassPhrase");
   }

   private KeyStoreInfo getJavaStandardTrustKeyStoreInfo() {
      return new KeyStoreInfo(KeyStoreConstants.getJavaStandardTrustKeyStoreFileName(), "jks", this.emptyToNull(this.config.getJavaStandardTrustKeyStorePassPhrase()));
   }

   private KeyStoreInfo getCustomTrustKeyStoreInfo() {
      return new KeyStoreInfo(this.getAbsolutePath(this.config.getCustomTrustKeyStoreFileName()), this.emptyToNull(this.config.getCustomTrustKeyStoreType()), this.emptyToNull(this.config.getCustomTrustKeyStorePassPhrase()));
   }

   public KeyStoreInfo getIdentityKeyStore() {
      if (!this.isValid()) {
         return null;
      } else {
         return this.isDemoIdentity() ? this.getDemoIdentityKeyStoreInfo() : this.getCustomIdentityKeyStoreInfo();
      }
   }

   public KeyStoreInfo[] getTrustKeyStores() {
      if (!this.isValid()) {
         return null;
      } else {
         KeyStoreInfo[] var1 = null;
         if (this.isDemoTrust()) {
            var1 = new KeyStoreInfo[]{this.getDemoTrustKeyStoreInfo(), this.getJavaStandardTrustKeyStoreInfo()};
         } else if (this.isJavaStandardTrust()) {
            var1 = new KeyStoreInfo[]{this.getJavaStandardTrustKeyStoreInfo()};
         } else {
            var1 = new KeyStoreInfo[]{this.getCustomTrustKeyStoreInfo()};
         }

         return var1;
      }
   }

   public String getIdentityAlias() {
      if (!this.isValid()) {
         return null;
      } else if (this.isDemoTrust()) {
         return "DemoIdentity";
      } else {
         return this.channel != null ? this.emptyToNull(this.channel.getPrivateKeyAlias()) : this.emptyToNull(this.config.getCustomIdentityAlias());
      }
   }

   public char[] getIdentityPrivateKeyPassPhrase() {
      if (!this.isValid()) {
         return null;
      } else {
         String var1 = null;
         if (this.isDemoTrust()) {
            var1 = "DemoIdentityPassPhrase";
         } else if (this.channel != null) {
            var1 = this.emptyToNull(this.channel.getPrivateKeyPassPhrase());
         } else {
            var1 = this.emptyToNull(this.config.getCustomIdentityPrivateKeyPassPhrase());
         }

         return var1 != null && var1.length() > 0 ? var1.toCharArray() : null;
      }
   }

   public String getOutboundPrivateKeyAlias() {
      if (!this.isValid()) {
         return null;
      } else if (this.isDemoIdentity()) {
         return "DemoIdentity";
      } else {
         return this.channel != null ? this.emptyToNull(this.channel.getOutboundPrivateKeyAlias()) : this.emptyToNull(this.config.getOutboundPrivateKeyAlias());
      }
   }

   public char[] getOutboundPrivateKeyPassPhrase() {
      if (!this.isValid()) {
         return null;
      } else {
         String var1;
         if (this.isDemoIdentity()) {
            var1 = "DemoIdentityPassPhrase";
         } else if (this.channel != null) {
            var1 = this.emptyToNull(this.channel.getOutboundPrivateKeyPassPhrase());
         } else {
            var1 = this.emptyToNull(this.config.getOutboundPrivateKeyPassPhrase());
         }

         return var1 != null && var1.length() > 0 ? var1.toCharArray() : null;
      }
   }
}
