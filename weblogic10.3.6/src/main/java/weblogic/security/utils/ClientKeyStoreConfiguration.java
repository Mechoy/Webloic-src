package weblogic.security.utils;

import weblogic.security.SecurityMessagesTextFormatter;

public class ClientKeyStoreConfiguration implements KeyStoreConfiguration {
   private String keystores;
   private static ClientKeyStoreConfiguration theInstance;

   public static synchronized ClientKeyStoreConfiguration getInstance() {
      if (theInstance == null) {
         theInstance = new ClientKeyStoreConfiguration();
      }

      return theInstance;
   }

   private String getProperty(String var1) {
      return System.getProperty(var1);
   }

   private ClientKeyStoreConfiguration() {
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

         this.keystores = "CustomIdentityAndJavaStandardTrust";
      }

      if (var1) {
         SecurityMessagesTextFormatter var7 = SecurityMessagesTextFormatter.getInstance();
         String var5 = var7.getSSLClientTrustKeyStoreConfigError();
         String var6 = var7.getSSLClientTrustKeyStoreSyntax();
         throw new RuntimeException(var5 + "\n\n" + var6);
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
      return this.getProperty("weblogic.security.CustomTrustKeyStorePassPhrase");
   }

   public String getJavaStandardTrustKeyStorePassPhrase() {
      return this.getProperty("weblogic.security.JavaStandardTrustKeyStorePassPhrase");
   }

   public static void main(String[] var0) {
      KeyStoreConfigurationHelper var1 = new KeyStoreConfigurationHelper(getInstance());
      KeyStoreInfo[] var2 = var1.getTrustKeyStores();

      for(int var3 = 0; var2 != null && var3 < var2.length; ++var3) {
         System.out.println("TrustKeyStore[" + var3 + "]=" + var2[var3]);
      }

   }
}
