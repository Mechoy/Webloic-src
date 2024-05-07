package weblogic.security.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.AccessController;
import java.util.Properties;
import weblogic.kernel.Kernel;
import weblogic.management.DomainDir;
import weblogic.management.configuration.SecurityConfigurationMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.PropertyService;
import weblogic.security.SecurityLogger;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.internal.encryption.ClearOrEncryptedService;
import weblogic.security.internal.encryption.EncryptionService;
import weblogic.security.internal.encryption.EncryptionServiceException;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

public final class BootProperties {
   private static final boolean DEBUG = false;
   private static final String USERNAME_PROP = "username";
   private static final String PASSWORD_PROP = "password";
   private static final String FILE = "boot.properties";
   private static String DEFAULT_FILE = null;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static BootProperties theInstance = null;
   private ClearOrEncryptedService encryptionService = null;
   private String filename = null;
   private boolean needServerSecDir = false;
   private boolean useClear = false;
   private String username = null;
   private String password = null;
   private String trustKeyStore = null;
   private String customTrustKeyStoreFileName = null;
   private String customTrustKeyStoreType = null;
   private String customTrustKeyStorePassPhrase = null;
   private String javaStandardTrustKeyStorePassPhrase = null;

   static void upgradeBP(String var0) {
      String var1 = getServerName();
      File var2 = new File(DomainDir.getPathRelativeServersSecurityDir(var1, "boot.properties"));
      if (!var2.exists()) {
         File var3 = new File(DomainDir.getPathRelativeRootDir("boot.properties"));
         if (var3.exists()) {
            try {
               if (null != var0) {
                  File var4 = new File(var0);
                  if (var4.getCanonicalPath().equals(var3.getCanonicalPath())) {
                     return;
                  }
               }

               if (theInstance != null) {
                  theInstance.updateDomainLevelBP(new File(theInstance.filename));
                  weblogic.utils.FileUtils.copy(var3, var2);
               }
            } catch (IOException var5) {
            }

         }
      }
   }

   private void updateDomainLevelBP(File var1) {
      File var2 = new File(DomainDir.getPathRelativeRootDir("boot.properties"));
      if (var2.exists() && var1.exists()) {
         try {
            if (var1.getCanonicalPath().equals(var2.getCanonicalPath())) {
               return;
            }

            weblogic.utils.FileUtils.copy(var1, var2);
         } catch (IOException var4) {
         }

      }
   }

   private void deleteDomainLevelBP() {
      File var1 = new File(DomainDir.getPathRelativeRootDir("boot.properties"));
      if (var1.exists()) {
         var1.delete();
      }

   }

   public static String getServerName() {
      PropertyService var0 = ManagementService.getPropertyService(kernelId);
      return var0 == null ? null : ManagementService.getPropertyService(kernelId).getServerName();
   }

   public static boolean exists(String var0) {
      String var1 = getFileName(var0);
      File var2 = new File(var1);
      boolean var3 = var2.exists();
      return var3;
   }

   public static synchronized void load(String var0, boolean var1) {
      if (theInstance == null) {
         try {
            BootProperties var2 = new BootProperties(var1, var0);
            if (var2.read()) {
               theInstance = var2;
            }
         } catch (Exception var3) {
            SecurityLogger.logBootPropertiesWarning(var3.toString());
         }

      }
   }

   public static synchronized void save() {
      try {
         if (theInstance != null && theInstance.haveUnencryptedValues()) {
            theInstance.write();
         }
      } catch (Exception var1) {
         SecurityLogger.logBootPropertiesWarning(var1.toString());
      }

   }

   public static synchronized void output(SecurityConfigurationMBean var0, String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8) {
      try {
         boolean var9 = false;
         BootProperties var10 = theInstance;
         if (var10 == null) {
            var10 = new BootProperties(var0, var1);
            if (!var10.read()) {
               var9 = true;
            } else {
               theInstance = var10;
            }
         }

         if (valChanged(var10.getOne(kernelId), var2)) {
            var10.username = var2;
            var9 = true;
         }

         if (valChanged(var10.getTwo(kernelId), var3)) {
            var10.password = var3;
            var9 = true;
         }

         if (valChanged(var10.getTrustKeyStore(), var4)) {
            var10.trustKeyStore = var4;
            var9 = true;
         }

         if (valChanged(var10.getCustomTrustKeyStoreFileName(), var5)) {
            var10.customTrustKeyStoreFileName = var5;
            var9 = true;
         }

         if (valChanged(var10.getCustomTrustKeyStoreType(), var6)) {
            var10.customTrustKeyStoreType = var6;
            var9 = true;
         }

         if (valChanged(var10.getCustomTrustKeyStorePassPhrase(), var7)) {
            var10.customTrustKeyStorePassPhrase = var7;
            var9 = true;
         }

         if (valChanged(var10.getJavaStandardTrustKeyStorePassPhrase(), var8)) {
            var10.javaStandardTrustKeyStorePassPhrase = var8;
            var9 = true;
         }

         if (var9 || var10.haveUnencryptedValues()) {
            var10.write();
         }
      } catch (Exception var11) {
         SecurityLogger.logBootPropertiesWarning(var11.toString());
      }

   }

   public static synchronized BootProperties getBootProperties() {
      return theInstance;
   }

   public static synchronized void unload(boolean var0) {
      if (theInstance != null) {
         try {
            if (var0) {
               theInstance.delete();
            }
         } catch (Exception var2) {
            SecurityLogger.logBootPropertiesWarning(var2.toString());
         }

         theInstance = null;
      }
   }

   String getOne(AuthenticatedSubject var1) {
      SecurityServiceManager.checkKernelIdentity(var1);
      return this.getEncryptedValue("username", this.username);
   }

   String getTwo(AuthenticatedSubject var1) {
      SecurityServiceManager.checkKernelIdentity(var1);
      return this.getEncryptedValue("password", this.password);
   }

   public String getOneClient() {
      return !Kernel.isServer() ? this.getOne(kernelId) : null;
   }

   public String getTwoClient() {
      return !Kernel.isServer() ? this.getTwo(kernelId) : null;
   }

   public String getTrustKeyStore() {
      return this.trustKeyStore;
   }

   public String getCustomTrustKeyStoreFileName() {
      return this.customTrustKeyStoreFileName;
   }

   public String getCustomTrustKeyStoreType() {
      return this.customTrustKeyStoreType;
   }

   public String getCustomTrustKeyStorePassPhrase() {
      return this.encryptValue(this.customTrustKeyStorePassPhrase);
   }

   public String getJavaStandardTrustKeyStorePassPhrase() {
      return this.encryptValue(this.javaStandardTrustKeyStorePassPhrase);
   }

   private BootProperties(boolean var1, String var2) {
      this.useClear = var1;
      this.initialize((SecurityConfigurationMBean)null, var2);
   }

   private BootProperties(SecurityConfigurationMBean var1, String var2) {
      this.initialize(var1, var2);
   }

   private static boolean valChanged(String var0, String var1) {
      if (!isSet(var0) && !isSet(var1)) {
         return false;
      } else if (isSet(var0) && isSet(var1)) {
         return !var1.equals(var0);
      } else {
         return true;
      }
   }

   private static boolean isSet(String var0) {
      return var0 != null && var0.length() > 0;
   }

   private String getEncryptedValue(String var1, String var2) {
      if (this.useClear && this.encryptionService == null) {
         return var2;
      } else if (var2 == null) {
         return var2;
      } else {
         try {
            return this.encryptionService.decrypt(var2);
         } catch (EncryptionServiceException var4) {
            SecurityLogger.logBootPropertiesDecryptionFailure(this.filename, var1, var2, var4.toString());
            return "";
         } catch (Exception var5) {
            SecurityLogger.logBootPropertiesWarning(var5.toString());
            return "";
         }
      }
   }

   private String encryptValue(String var1) {
      return this.encryptionService != null && var1 != null ? this.encryptionService.encrypt(var1) : var1;
   }

   private void initialize(SecurityConfigurationMBean var1, String var2) {
      this.filename = getFileName(var2);
      this.needServerSecDir = var2 == null && getServerName() != null;
      if (!this.useClear) {
         EncryptionService var3 = null;
         if (var1 == null) {
            var3 = SerializedSystemIni.getEncryptionService(DomainDir.getRootDir());
         } else {
            var3 = SerializedSystemIni.getEncryptionService(var1.getSalt(), var1.getEncryptedSecretKey(), var1.getEncryptedAESSecretKey());
         }

         this.encryptionService = new ClearOrEncryptedService(var3);
      }
   }

   private boolean read() throws IOException {
      File var1 = new File(this.filename);
      if (!var1.exists()) {
         return false;
      } else {
         Properties var2 = new Properties();
         FileInputStream var3 = new FileInputStream(var1);

         try {
            var2.load(var3);
            this.username = var2.getProperty("username");
            this.password = var2.getProperty("password");
            this.trustKeyStore = var2.getProperty("TrustKeyStore");
            this.customTrustKeyStoreFileName = var2.getProperty("CustomTrustKeyStoreFileName");
            this.customTrustKeyStoreType = var2.getProperty("CustomTrustKeyStoreType");
            this.customTrustKeyStorePassPhrase = var2.getProperty("CustomTrustKeyStorePassPhrase");
            this.javaStandardTrustKeyStorePassPhrase = var2.getProperty("JavaStandardTrustKeyStorePassPhrase");
         } finally {
            var3.close();
         }

         if (this.username != null && this.password != null) {
            if (this.useClear || !this.encryptionService.isEncrypted(this.username)) {
               this.username = this.username.trim();
               if (this.username.length() == 0) {
                  return false;
               }
            }

            return !this.useClear && this.encryptionService.isEncrypted(this.password) || this.password.length() != 0;
         } else {
            return false;
         }
      }
   }

   private boolean haveUnencryptedValues() {
      if (!this.encryptionService.isEncrypted(this.username)) {
         return true;
      } else if (!this.encryptionService.isEncrypted(this.password)) {
         return true;
      } else if (isSet(this.customTrustKeyStorePassPhrase) && !this.encryptionService.isEncrypted(this.customTrustKeyStorePassPhrase)) {
         return true;
      } else {
         return isSet(this.javaStandardTrustKeyStorePassPhrase) && !this.encryptionService.isEncrypted(this.javaStandardTrustKeyStorePassPhrase);
      }
   }

   private void write() throws IOException {
      if (!this.useClear) {
         File var1 = new File(this.filename);

         try {
            SecurityLogger.logStoringBootIdentity(var1.getCanonicalPath());
         } catch (IOException var8) {
            SecurityLogger.logStoringBootIdentity(var1.getAbsolutePath());
         }

         this.ensureServerSecDirExists();
         Properties var2 = new Properties();
         FileOutputStream var3 = new FileOutputStream(var1);

         try {
            var2.setProperty("username", this.encryptionService.encrypt(this.username));
            var2.setProperty("password", this.encryptionService.encrypt(this.password));
            if (isSet(this.trustKeyStore)) {
               var2.setProperty("TrustKeyStore", this.trustKeyStore);
            }

            if (isSet(this.customTrustKeyStoreFileName)) {
               var2.setProperty("CustomTrustKeyStoreFileName", this.customTrustKeyStoreFileName);
            }

            if (isSet(this.customTrustKeyStoreType)) {
               var2.setProperty("CustomTrustKeyStoreType", this.customTrustKeyStoreType);
            }

            if (isSet(this.customTrustKeyStorePassPhrase)) {
               var2.setProperty("CustomTrustKeyStorePassPhrase", this.encryptionService.encrypt(this.customTrustKeyStorePassPhrase));
            }

            if (isSet(this.javaStandardTrustKeyStorePassPhrase)) {
               var2.setProperty("JavaStandardTrustKeyStorePassPhrase", this.encryptionService.encrypt(this.javaStandardTrustKeyStorePassPhrase));
            }

            var2.store(var3, (String)null);
         } finally {
            var3.close();
         }

         this.updateDomainLevelBP(var1);
      }
   }

   private void delete() throws IOException {
      this.deleteDomainLevelBP();
      File var1 = new File(this.filename);
      if (var1.exists()) {
         if (!var1.delete()) {
            throw new IOException(SecurityLogger.getUnableToDelete(this.filename));
         }
      }
   }

   private static String getFileName(String var0) {
      if (var0 != null) {
         return var0;
      } else {
         if (DEFAULT_FILE == null) {
            String var1 = getServerName();
            if (var1 != null) {
               DEFAULT_FILE = DomainDir.getPathRelativeServersSecurityDir(var1, "boot.properties");
            } else {
               DEFAULT_FILE = "boot.properties";
            }
         }

         return DEFAULT_FILE;
      }
   }

   private void ensureServerSecDirExists() {
      if (this.needServerSecDir && getServerName() != null) {
         File var1 = new File(DomainDir.getSecurityDirForServer(getServerName()));
         if (!var1.exists() && var1.mkdir()) {
         }
      }

   }

   private static void debug(String var0) {
   }
}
