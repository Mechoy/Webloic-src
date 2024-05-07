package weblogic.store.admin;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.util.HashMap;
import weblogic.kernel.KernelStatus;
import weblogic.management.DeploymentException;
import weblogic.management.DomainDir;
import weblogic.management.configuration.DefaultFileStoreMBean;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.FileStoreMBean;
import weblogic.management.configuration.GenericFileStoreMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.utils.GenericBeanListener;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.store.PersistentStoreException;
import weblogic.store.StoreLogger;
import weblogic.store.StoreWritePolicy;
import weblogic.store.common.StoreDebug;
import weblogic.store.io.file.StoreDir;
import weblogic.store.xa.PersistentStoreManagerXA;

public class FileAdminHandler extends AdminHandler {
   private GenericBeanListener listener;
   private String directoryName;
   private boolean autoCreateDirectory;
   private static final HashMap changeableAttributes = new HashMap();
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void prepare(DeploymentMBean var1) throws DeploymentException {
      super.prepare(var1);
      FileStoreMBean var2 = (FileStoreMBean)var1;
      this.autoCreateDirectory = !ManagementService.getRuntimeAccess(kernelId).getDomain().isProductionModeEnabled();
      String var3 = ManagementService.getRuntimeAccess(kernelId).getServer().getName();
      this.directoryName = var2.getDirectory();
      if (this.directoryName != null && this.directoryName.length() != 0) {
         this.directoryName = canonicalizeDirectoryName(this.directoryName);
      } else {
         this.directoryName = DomainDir.getPathRelativeServersStoreDataDir(var3, var2.getName());
      }

      this.prepareCommon(var2);
   }

   public void prepareDefaultStore(ServerMBean var1, boolean var2) throws DeploymentException {
      this.defaultStore = true;
      this.name = "_WLS_" + var1.getName();
      this.autoCreateDirectory = var2;
      DefaultFileStoreMBean var3 = var1.getDefaultFileStore();
      this.directoryName = var3.getDirectory();
      if (this.directoryName != null && this.directoryName.length() != 0) {
         this.directoryName = canonicalizeDirectoryName(this.directoryName);
      } else {
         this.directoryName = DomainDir.getPathRelativeServersStoreDataDir(var1.getName(), "default");
      }

      this.prepareCommon(var3);
   }

   private void prepareCommon(GenericFileStoreMBean var1) throws DeploymentException {
      this.config = new HashMap();

      try {
         this.config.put("BlockSize", var1.getBlockSize());
         this.config.put("CacheDirectory", canonicalizeDirectoryName(var1.getCacheDirectory()));
         this.config.put("InitialSize", var1.getInitialSize());
         this.config.put("IoBufferSize", var1.getIoBufferSize());
         this.config.put("MaxFileSize", var1.getMaxFileSize());
         this.config.put("MaxWindowBufferSize", var1.getMaxWindowBufferSize());
         this.config.put("MinWindowBufferSize", var1.getMinWindowBufferSize());
         this.config.put("SynchronousWritePolicy", StoreWritePolicy.getPolicy(var1.getSynchronousWritePolicy()));
         this.config.put("FileLockingEnabled", var1.isFileLockingEnabled());
         if (KernelStatus.isInitialized() && KernelStatus.isServer()) {
            this.config.put("DomainName", ManagementService.getRuntimeAccess(kernelId).getDomainName());
         }
      } catch (PersistentStoreException var4) {
         StoreLogger.logStoreDeploymentFailed(this.name, var4.toString(), var4);
         throw new DeploymentException(var4);
      }

      try {
         StoreDir.createDirectory(new File(this.directoryName), this.autoCreateDirectory);
      } catch (IOException var3) {
         StoreLogger.logStoreDeploymentFailed(this.name, var3.toString(), var3);
         throw new DeploymentException(var3);
      }

      this.listener = new GenericBeanListener(var1, this, changeableAttributes);
   }

   public void activate(DeploymentMBean var1) throws DeploymentException {
      try {
         RuntimeHandlerImpl var2 = null;
         if (KernelStatus.isServer()) {
            var2 = new RuntimeHandlerImpl();
         }

         this.store = PersistentStoreManagerXA.makeXAStore(this.name, this.directoryName, this.overrideResourceName, this.autoCreateDirectory, var2);
      } catch (PersistentStoreException var3) {
         StoreLogger.logStoreDeploymentFailed(this.name, var3.toString(), var3);
         throw new DeploymentException(var3);
      }

      if (var1 != null) {
         if (this.listener != null) {
            this.listener.close();
         }

         this.listener = new GenericBeanListener(var1, this, changeableAttributes);
      }

      super.activate(var1);
   }

   public void createMigratedDefaultStore(ServerMBean var1, boolean var2) throws DeploymentException {
      this.prepareDefaultStore(var1, var2);
      this.defaultStore = false;
      this.activate((DeploymentMBean)null);
   }

   public void unprepare(DeploymentMBean var1) {
      if (this.listener != null) {
         this.listener.close();
         this.listener = null;
      }

      super.unprepare(var1);
   }

   public static String canonicalizeDirectoryName(String var0) {
      if (isEmptyString(var0)) {
         return null;
      } else {
         File var1 = new File(var0);
         if (!var1.isAbsolute() && !var1.getPath().startsWith(File.separator)) {
            var0 = DomainDir.getPathRelativeRootDir(var0);
         }

         return var0;
      }
   }

   public void setSynchronousWritePolicy(String var1) {
      if (StoreDebug.storeAdmin.isDebugEnabled()) {
         StoreDebug.storeAdmin.debug("Changing the store's write policy to " + var1);
      }

      try {
         this.store.setConfigValue("SynchronousWritePolicy", StoreWritePolicy.getPolicy(var1));
      } catch (PersistentStoreException var3) {
         var3.log();
      }

   }

   public void setMinWindowBufferSize(int var1) {
      if (StoreDebug.storeAdmin.isDebugEnabled()) {
         StoreDebug.storeAdmin.debug("Changing the store's MinWindowBufferSize to " + var1);
      }

      try {
         this.store.setConfigValue("MinWindowBufferSize", var1);
      } catch (PersistentStoreException var3) {
         var3.log();
      }

   }

   public void setMaxWindowBufferSize(int var1) {
      if (StoreDebug.storeAdmin.isDebugEnabled()) {
         StoreDebug.storeAdmin.debug("Changing the store's MaxWindowBufferSize to " + var1);
      }

      try {
         this.store.setConfigValue("MaxWindowBufferSize", var1);
      } catch (PersistentStoreException var3) {
         var3.log();
      }

   }

   public void setIoBufferSize(int var1) {
      if (StoreDebug.storeAdmin.isDebugEnabled()) {
         StoreDebug.storeAdmin.debug("Changing the store's IOBufferSize to " + var1);
      }

      try {
         this.store.setConfigValue("IoBufferSize", var1);
      } catch (PersistentStoreException var3) {
         var3.log();
      }

   }

   public void setMaxFileSize(long var1) {
      if (StoreDebug.storeAdmin.isDebugEnabled()) {
         StoreDebug.storeAdmin.debug("Changing the store's MaxFileSize to " + var1);
      }

      try {
         this.store.setConfigValue("MaxFileSize", var1);
      } catch (PersistentStoreException var4) {
         var4.log();
      }

   }

   public void setFileLockingEnabled(boolean var1) {
      if (StoreDebug.storeAdmin.isDebugEnabled()) {
         StoreDebug.storeAdmin.debug("Changing the store's FileLockingEnabled to " + var1);
      }

      try {
         this.store.setConfigValue("FileLockingEnabled", var1);
      } catch (PersistentStoreException var3) {
         var3.log();
      }

   }

   public void setBlockSize(int var1) {
      if (StoreDebug.storeAdmin.isDebugEnabled()) {
         StoreDebug.storeAdmin.debug("Changing the store's BlockSize to " + var1);
      }

      try {
         this.store.setConfigValue("BlockSize", var1);
      } catch (PersistentStoreException var3) {
         var3.log();
      }

   }

   public void setInitialSize(long var1) {
      if (StoreDebug.storeAdmin.isDebugEnabled()) {
         StoreDebug.storeAdmin.debug("Changing the store's InitialSize to " + var1);
      }

      try {
         this.store.setConfigValue("InitialSize", var1);
      } catch (PersistentStoreException var4) {
         var4.log();
      }

   }

   public void setCacheDirectory(String var1) {
      if (StoreDebug.storeAdmin.isDebugEnabled()) {
         StoreDebug.storeAdmin.debug("Changing the store's CacheDirectory to " + var1);
      }

      try {
         this.store.setConfigValue("CacheDirectory", canonicalizeDirectoryName(var1));
      } catch (PersistentStoreException var3) {
         var3.log();
      }

   }

   static {
      changeableAttributes.put("SynchronousWritePolicy", String.class);
      changeableAttributes.put("CacheDirectory", String.class);
      changeableAttributes.put("MinWindowBufferSize", Integer.TYPE);
      changeableAttributes.put("MaxWindowBufferSize", Integer.TYPE);
      changeableAttributes.put("IoBufferSize", Integer.TYPE);
      changeableAttributes.put("MaxFileSize", Long.TYPE);
      changeableAttributes.put("BlockSize", Integer.TYPE);
      changeableAttributes.put("InitialSize", Long.TYPE);
      changeableAttributes.put("FileLockingEnabled", Boolean.TYPE);
   }
}
