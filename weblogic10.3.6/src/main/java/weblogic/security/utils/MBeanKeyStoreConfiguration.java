package weblogic.security.utils;

import java.security.AccessController;
import weblogic.management.configuration.SSLMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.SecurityLogger;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.shared.LoggerWrapper;

public class MBeanKeyStoreConfiguration implements KeyStoreConfiguration {
   private static LoggerWrapper logger = LoggerWrapper.getInstance("SecurityKeyStore");
   private static MBeanKeyStoreConfiguration theInstance;
   private ServerMBean server;
   private SSLMBean ssl;
   private boolean usePreCfg;
   private boolean valid;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static synchronized MBeanKeyStoreConfiguration getInstance() {
      if (theInstance == null) {
         theInstance = new MBeanKeyStoreConfiguration();
      }

      return theInstance;
   }

   private void debug(String var1) {
      logger.debug("MBeanKeyStoreConfiguration: " + var1);
   }

   private PreMBeanKeyStoreConfiguration getPreCfg() {
      return PreMBeanKeyStoreConfiguration.getInstance();
   }

   private MBeanKeyStoreConfiguration() {
      this.server = ManagementService.getRuntimeAccess(kernelId).getServer();
      this.ssl = this.server.getSSL();
      this.valid = true;
      if ("CustomIdentityAndCommandLineTrust".equals(this.server.getKeyStores())) {
         this.usePreCfg = true;
         if (logger.isDebugEnabled()) {
            this.debug("constructor - using command line trust config");
         }

         if ("DemoIdentityAndDemoTrust".equals(this.getPreCfg().getKeyStores())) {
            this.valid = false;
            SecurityLogger.logServerDemoCommandLineTrust();
         }
      } else {
         this.usePreCfg = false;
         if (logger.isDebugEnabled()) {
            this.debug("constructor - using mbean trust config");
         }

         if (!this.MBeanAndCommandLineTrustEqual()) {
            this.valid = false;
            SecurityLogger.logServerTrustKeyStoreMisMatchError();
         }
      }

      if (logger.isDebugEnabled()) {
         KeyStoreInfo[] var1 = (new KeyStoreConfigurationHelper(this)).getTrustKeyStores();

         for(int var2 = 0; var1 != null && var2 < var1.length; ++var2) {
            this.debug("constructor - TrustKeyStore[" + var2 + "]=" + var1[var2]);
         }
      }

   }

   private boolean MBeanAndCommandLineTrustEqual() {
      PreMBeanKeyStoreConfiguration var1 = this.getPreCfg();
      if (!var1.isExplicitlyConfigured()) {
         return true;
      } else {
         KeyStoreInfo[] var2 = (new KeyStoreConfigurationHelper(this)).getTrustKeyStores();
         KeyStoreInfo[] var3 = (new KeyStoreConfigurationHelper(var1)).getTrustKeyStores();
         if (var2 == null && var3 == null) {
            return true;
         } else if (var2 != null && var3 != null) {
            if (var2.length != var3.length) {
               return false;
            } else {
               for(int var4 = 0; var4 < var2.length; ++var4) {
                  if (!var2[var4].equals(var3[var4])) {
                     return false;
                  }
               }

               return true;
            }
         } else {
            return false;
         }
      }
   }

   public String getKeyStores() {
      if (!this.valid) {
         return null;
      } else {
         return this.usePreCfg ? this.getPreCfg().getKeyStores() : this.server.getKeyStores();
      }
   }

   public String getCustomIdentityKeyStoreFileName() {
      return this.server.getCustomIdentityKeyStoreFileName();
   }

   public String getCustomIdentityKeyStoreType() {
      return this.server.getCustomIdentityKeyStoreType();
   }

   public String getCustomIdentityKeyStorePassPhrase() {
      return this.server.getCustomIdentityKeyStorePassPhrase();
   }

   public String getCustomIdentityAlias() {
      return this.ssl.getServerPrivateKeyAlias();
   }

   public String getCustomIdentityPrivateKeyPassPhrase() {
      return this.ssl.getServerPrivateKeyPassPhrase();
   }

   public String getOutboundPrivateKeyAlias() {
      return this.ssl.getOutboundPrivateKeyAlias();
   }

   public String getOutboundPrivateKeyPassPhrase() {
      return this.ssl.getOutboundPrivateKeyPassPhrase();
   }

   public String getCustomTrustKeyStoreFileName() {
      return this.usePreCfg ? this.getPreCfg().getCustomTrustKeyStoreFileName() : this.server.getCustomTrustKeyStoreFileName();
   }

   public String getCustomTrustKeyStoreType() {
      return this.usePreCfg ? this.getPreCfg().getCustomTrustKeyStoreType() : this.server.getCustomTrustKeyStoreType();
   }

   public String getCustomTrustKeyStorePassPhrase() {
      return this.usePreCfg ? this.getPreCfg().getCustomTrustKeyStorePassPhrase() : this.server.getCustomTrustKeyStorePassPhrase();
   }

   public String getJavaStandardTrustKeyStorePassPhrase() {
      return this.usePreCfg ? this.getPreCfg().getJavaStandardTrustKeyStorePassPhrase() : this.server.getJavaStandardTrustKeyStorePassPhrase();
   }
}
