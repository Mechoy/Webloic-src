package weblogic.deploy.internal.targetserver.datamanagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.deploy.service.DataStream;
import weblogic.deploy.service.DataTransferHandler;
import weblogic.deploy.service.FileDataStream;
import weblogic.deploy.service.MultiDataStream;
import weblogic.deploy.service.datatransferhandlers.DataHandlerManager;
import weblogic.management.DeploymentException;
import weblogic.management.DomainDir;
import weblogic.management.deploy.internal.DeploymentManagerLogger;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.FileUtils;
import weblogic.utils.StackTraceUtils;

public class ConfigDataUpdate extends DataUpdate {
   private static final int LOCK_WAIT_TIME_INTERVAL = Integer.parseInt(System.getProperty("weblogic.deploy.FileLockWaitTimeInterval", "100"));
   private static final String BOOTING_SERVER_LOCK = "BOOTING_SERVER_LOCK";
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final ConfigBackupRecoveryManager backupManager = ConfigBackupRecoveryManager.getInstance();
   private CopyLockInfo copyLockInfo = null;

   public ConfigDataUpdate(Data var1, DataUpdateRequestInfo var2) {
      super(var1, var2);
   }

   protected final void doDownload(String var1) throws DeploymentException {
      CopyLockInfo var2 = new CopyLockInfo(this.getLocalData().getLockPath(), this.getRequestId());
      this.setCopyLock(var2);
      if (var2 != null && !var2.isUpdateNeeded()) {
         var2.release();
         if (isDebugEnabled()) {
            debug("Not preparing '" + this + "' - since some other server acquired lock");
         }

      } else {
         ConfigData var3 = this.getLocalConfigData();
         ConfigDataTransferRequestImpl var4 = new ConfigDataTransferRequestImpl(this.getRequestId(), this.getRequestedFiles(), var3.getLockPath());

         try {
            DataTransferHandler var5 = DataHandlerManager.getHandler(var1);
            ReadOnlyMultiDataStream var6 = new ReadOnlyMultiDataStream(var5.getDataAsStream(var4));
            this.setDownloadedStream(var6);
            this.handleBackup(var6);
         } catch (IOException var7) {
            var7.printStackTrace();
            throw new DeploymentException("Exception occured while downloading files", var7);
         }
      }
   }

   protected final void doUpdate() throws DeploymentException {
      CopyLockInfo var1 = this.getCopyLock();
      if (var1 != null && !var1.isUpdateNeeded()) {
         if (isDebugEnabled()) {
            debug("Not committing '" + this + "' - since some other server acquired lock");
         }

      } else {
         FileLock var2 = null;

         try {
            var2 = this.acquireFileLock(this.getLocalData().getLockPath());
            super.doUpdate();
         } finally {
            this.releaseFileLock(var2);
         }

      }
   }

   protected void doCancel() {
      this.restoreFromBackup();
   }

   protected void doClose(boolean var1) {
      if (var1) {
         this.saveBackupToPrev();
      }

      CopyLockInfo var2 = this.getCopyLock();
      if (var2 != null) {
         var2.release();
         var2.removeFile();
      }

      this.setCopyLock((CopyLockInfo)null);
   }

   public final void releaseLock() {
      CopyLockInfo var1 = this.getCopyLock();
      if (var1 != null) {
         var1.release();
      }

   }

   protected final File getFileFor(String var1) {
      if (var1 == null) {
         throw new NullPointerException("targetPath null");
      } else {
         File var2 = null;
         MultiDataStream var3 = this.getDownloadedStream();
         if (var3 != null) {
            Iterator var4 = var3.getDataStreams();

            while(var4.hasNext()) {
               DataStream var5 = (DataStream)var4.next();
               if (var5.getName().equals(var1)) {
                  if (!(var5 instanceof FileDataStream)) {
                     break;
                  }

                  var2 = ((FileDataStream)var5).getFile();
               }
            }
         }

         if (var2 == null) {
            var2 = new File(this.getLocalData().getRootLocation(), var1);
         }

         return var2;
      }
   }

   protected final void deleteFile(String var1) {
      if (var1 != null) {
         File var2 = new File(this.getLocalData().getRootLocation(), var1);
         CopyLockInfo var3 = this.getCopyLock();
         if (var3 != null && !var3.isUpdateNeeded()) {
            if (isDebugEnabled()) {
               debug("Not deleting file '" + var2.getAbsolutePath() + "' for '" + this + "' - since copy lock acquired by some other server");
            }

         } else {
            if (isDebugEnabled()) {
               debug("Deleting file " + var2.getAbsolutePath());
            }

            FileLock var4 = null;
            boolean var5 = false;

            try {
               var4 = this.acquireFileLock(this.getLocalData().getLockPath());
               var5 = var2.delete();
            } finally {
               this.releaseFileLock(var4);
            }

            if (!var5) {
               var2.deleteOnExit();
               DeploymentManagerLogger.logDeleteFileFailed(var1);
            } else if (isDebugEnabled()) {
               debug("Deleted file " + var2.getAbsolutePath());
            }

         }
      }
   }

   private ConfigData getLocalConfigData() {
      return (ConfigData)this.getLocalData();
   }

   private final void saveBackupToPrev() {
      CopyLockInfo var1 = this.getCopyLock();
      if (var1 != null && !var1.isUpdateNeeded()) {
         if (isDebugEnabled()) {
            debug("Not saving config_bak dir to config_prev dir for '" + this + "' - since copy lock acquired by some other server");
         }

      } else {
         try {
            if (isDebugEnabled()) {
               debug("Saving config_bak dir to config_prev dir.");
            }

            backupManager.saveConfigBakDirToConfigPrevDir();
            if (isDebugEnabled()) {
               debug("Saving config_bak dir to config_prev dir - completed.");
            }
         } catch (Throwable var3) {
            var3.printStackTrace();
         }

      }
   }

   private final void restoreFromBackup() {
      CopyLockInfo var1 = this.getCopyLock();
      if (var1 != null && !var1.isUpdateNeeded()) {
         if (isDebugEnabled()) {
            debug("Not restoring from config_bak dir for '" + this + "' - " + "since copy lock acquired by some other server");
         }

      } else {
         try {
            if (isDebugEnabled()) {
               debug("Restoring from config_bak dir.");
            }

            backupManager.restoreFromBackup();
            if (isDebugEnabled()) {
               debug("Restoring from config_bak dir. - completed");
            }
         } catch (Throwable var3) {
            var3.printStackTrace();
         }

      }
   }

   private static File copyDataStreamToTempFile(DataStream var0, String var1) throws IOException {
      if (var0 instanceof FileDataStream) {
         File var2 = ((FileDataStream)var0).getFile();
         return var2.getName().startsWith("wl_comp") ? var2 : copyStreamToTempFile(var0.getInputStream(), var1);
      } else {
         return copyStreamToTempFile(var0.getInputStream(), var1);
      }
   }

   private static File copyStreamToTempFile(InputStream var0, String var1) throws IOException {
      File var2 = new File(DomainDir.getRootDir(), var1);
      File var3 = null;
      String var4 = var2.getName();
      if (var4.length() < 3) {
         var4 = var4 + "aaa".substring(var4.length());
      }

      try {
         var3 = File.createTempFile(var4, "new");
      } catch (IOException var13) {
         var3 = File.createTempFile(var4, "new", new File(DomainDir.getRootDir()));
      }

      var3.deleteOnExit();

      try {
         FileUtils.writeToFile(var0, var3);
      } finally {
         if (var0 != null) {
            try {
               var0.close();
            } catch (IOException var12) {
            }
         }

      }

      return var3;
   }

   private void setCopyLock(CopyLockInfo var1) {
      this.copyLockInfo = var1;
   }

   private CopyLockInfo getCopyLock() {
      return this.copyLockInfo;
   }

   private String getCopyLockFileName(long var1) {
      File var3 = new File(DomainDir.getTempDir());
      if (!var3.exists()) {
         var3.mkdirs();
      }

      boolean var4 = !ManagementService.isRuntimeAccessInitialized();
      if (isDebugEnabled()) {
         debug(" Is Server Booting.... : " + var4);
      }

      String var5 = var4 ? "BOOTING_SERVER_LOCK" : Long.toString(var1);
      return DomainDir.getPathRelativeTempDir(var5 + ".lok");
   }

   private FileLock acquireFileLock(String var1) {
      if (var1 == null) {
         return null;
      } else {
         FileLock var2 = null;
         String var3 = this.getLocalData().getRootLocation() + File.separator + var1;
         var2 = this.acquireFileLock(new File(var3));
         return var2;
      }
   }

   private FileLock acquireFileLock(File var1) {
      FileLock var2 = getFileLock(var1);
      if (isDebugEnabled()) {
         debug(" Acquired FileLock on '" + var1.getAbsolutePath() + "' -- " + System.currentTimeMillis());
      }

      return var2;
   }

   public static FileLock getFileLock(File var0) {
      try {
         FileOutputStream var1 = new FileOutputStream(var0);
         FileChannel var2 = var1.getChannel();
         if (isDebugEnabled()) {
            debug("Trying to get lock for File: " + var0.getAbsolutePath() + " with channel : " + var2);
         }

         return getFileLock(var2, 300000L);
      } catch (FileNotFoundException var3) {
         return null;
      }
   }

   private static FileLock getFileLock(FileChannel var0, long var1) {
      long var3 = System.currentTimeMillis();
      long var5 = var3 + var1;
      FileLock var7 = null;

      while(var7 == null && var3 <= var5) {
         if (isDebugEnabled()) {
            debug("Trying to get lock channel: '" + var0 + "' currentTime: " + var3 + ", timeoutTime: " + var5);
         }

         try {
            var7 = var0.tryLock();
         } catch (Throwable var13) {
            if (isDebugEnabled()) {
               debug("Experienced Exception while c.tryLock() and it is ignored :: " + StackTraceUtils.throwable2StackTrace(var13));
            }
         }

         if (var7 == null) {
            long var8 = var5 - var3;

            try {
               long var10 = (long)LOCK_WAIT_TIME_INTERVAL;
               if (isDebugEnabled()) {
                  debug("Sleeping for '" + var10 + "' ...");
               }

               Thread.sleep(var8 > var10 ? var10 : var8);
            } catch (InterruptedException var12) {
            }

            var3 = System.currentTimeMillis();
         }
      }

      if (var7 == null) {
         if (isDebugEnabled()) {
            debug("Could not get lock on channel: '" + var0 + "'.");
         }

         DeploymentManagerLogger.logCouldNotGetFileLock();
      } else if (isDebugEnabled()) {
         debug("Got lock on channel: '" + var0 + "' : " + var7);
      }

      return var7;
   }

   private void releaseFileLock(FileLock var1) {
      if (var1 != null) {
         try {
            if (isDebugEnabled()) {
               debug(" Releasing FileLock : " + var1 + " -- " + System.currentTimeMillis());
            }

            var1.release();
            var1.channel().close();
         } catch (IOException var3) {
         }
      }

   }

   private final void handleBackup(MultiDataStream var1) throws IOException {
      CopyLockInfo var2 = this.getCopyLock();
      if (var2 != null && !var2.isUpdateNeeded()) {
         if (isDebugEnabled()) {
            debug("Not taking backup for update '" + this + "' - since copy lock acquired by some other server");
         }

      } else {
         Iterator var3 = getAllTargetPaths(var1).iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            File var5 = new File(this.getLocalData().getRootLocation(), var4);

            try {
               if (isDebugEnabled()) {
                  debug("Saving file '" + var5.getAbsolutePath() + "' to config_bak");
               }

               backupManager.handleBackup(var5, var4);
               if (isDebugEnabled()) {
                  debug("Saved file '" + var5.getAbsolutePath() + "' to config_bak");
               }
            } catch (IOException var7) {
               backupManager.deleteConfigBakFile(var4);
               this.restoreFromBackup();
               throw var7;
            }
         }

      }
   }

   private static List getAllTargetPaths(MultiDataStream var0) {
      ArrayList var1 = new ArrayList();
      if (var0 != null) {
         Iterator var2 = var0.getDataStreams();

         while(var2.hasNext()) {
            DataStream var3 = (DataStream)var2.next();
            var1.add(var3.getName());
         }
      }

      return var1;
   }

   private class DelegatingFileDataStream extends DelegatingDataStream implements FileDataStream {
      private File referringFile = null;

      DelegatingFileDataStream(DataStream var2) throws IOException {
         super(var2);
         this.referringFile = ConfigDataUpdate.copyDataStreamToTempFile(var2, this.getName());
      }

      public File getFile() {
         if (this.referringFile != null) {
            return this.referringFile;
         } else {
            return this.delegate instanceof FileDataStream ? ((FileDataStream)this.delegate).getFile() : null;
         }
      }

      public int getLength() throws IOException {
         File var1 = this.getFile();
         return var1 != null ? (int)var1.length() : 0;
      }

      public InputStream getInputStream() throws IOException {
         return (InputStream)(this.referringFile != null ? new FileInputStream(this.referringFile) : super.getInputStream());
      }

      public void close() {
         if (this.referringFile != null) {
            if (DataUpdate.isDebugEnabled()) {
               DataUpdate.debug("Removing temporary file : " + this.referringFile.getAbsolutePath());
            }

            this.referringFile.delete();
         } else {
            super.close();
         }

      }
   }

   private class DelegatingDataStream implements DataStream {
      protected final DataStream delegate;

      DelegatingDataStream(DataStream var2) {
         this.delegate = var2;
      }

      public String getName() {
         return this.delegate.getName();
      }

      public boolean isZip() {
         return this.delegate.isZip();
      }

      public InputStream getInputStream() throws IOException {
         return this.delegate.getInputStream();
      }

      public void close() {
         this.delegate.close();
      }
   }

   private class ReadOnlyMultiDataStream implements MultiDataStream {
      List allStreams = new ArrayList();

      ReadOnlyMultiDataStream(MultiDataStream var2) throws IOException {
         FileLock var3 = null;

         try {
            if (ManagementService.getPropertyService(ConfigDataUpdate.KERNEL_ID).isAdminServer()) {
               var3 = ConfigDataUpdate.this.acquireFileLock(ConfigDataUpdate.this.getLocalData().getLockPath());
            }

            Iterator var4 = var2.getDataStreams();

            while(var4.hasNext()) {
               DataStream var5 = (DataStream)var4.next();
               String var6 = var5.getName();
               final String var7 = var6.startsWith("pending") ? "config" + var6.substring("pending".length()) : var6;
               this.allStreams.add(new DelegatingFileDataStream((FileDataStream)var5) {
                  public String getName() {
                     return var7;
                  }
               });
            }
         } finally {
            ConfigDataUpdate.this.releaseFileLock(var3);
         }

      }

      public int getSize() {
         return this.allStreams.size();
      }

      public Iterator getDataStreams() {
         return this.allStreams.iterator();
      }

      public Iterator getInputStreams() throws IOException {
         ArrayList var1 = new ArrayList();
         Iterator var2 = this.getDataStreams();

         while(var2.hasNext()) {
            var1.add(((DataStream)var2.next()).getInputStream());
         }

         return var1.iterator();
      }

      public void close() {
         Iterator var1 = this.getDataStreams();

         while(var1.hasNext()) {
            DataStream var2 = (DataStream)var1.next();
            var2.close();
         }

      }

      public void addDataStream(DataStream var1) {
         throw new UnsupportedOperationException("[ReadOnly].addDataStream(DataStream) unsupported");
      }

      public void addFileDataStream(String var1, File var2, boolean var3) {
         throw new UnsupportedOperationException("[ReadOnly].addFileDataStream(String, File, boolean) unsupported");
      }

      public void addFileDataStream(String var1, boolean var2) {
         throw new UnsupportedOperationException("[ReadOnly].addFileDataStream(String, boolean) unsupported");
      }

      public void removeDataStream(DataStream var1) {
         throw new UnsupportedOperationException("[ReadOnly].removeDataStream(DataStream) unsupported");
      }
   }

   private final class CopyLockInfo {
      boolean updateIsNeeded = false;
      FileLock copyLock = null;
      File targetFile = null;

      protected CopyLockInfo(String var2, long var3) {
         this.copyLock = this.acquireCopyLock(var2, var3);
         if (DataUpdate.isDebugEnabled()) {
            DataUpdate.debug("Got lock on '" + this.targetFile.getAbsolutePath() + "' == " + System.currentTimeMillis());
         }

      }

      protected FileLock getLock() {
         return this.copyLock;
      }

      protected boolean isUpdateNeeded() {
         return this.updateIsNeeded;
      }

      protected void release() {
         if (this.copyLock != null) {
            ConfigDataUpdate.this.releaseFileLock(this.copyLock);
            this.copyLock = null;
            if (DataUpdate.isDebugEnabled()) {
               DataUpdate.debug(" Released lock on '" + this.targetFile.getAbsolutePath() + "' == " + System.currentTimeMillis());
            }

         }
      }

      protected void removeFile() {
         if (this.targetFile.exists()) {
            this.targetFile.delete();
            if (DataUpdate.isDebugEnabled()) {
               DataUpdate.debug(" Removed file '" + this.targetFile.getAbsolutePath() + "'");
            }
         }

      }

      private FileLock acquireCopyLock(String var1, long var2) {
         FileLock var4 = ConfigDataUpdate.this.acquireFileLock(var1);

         FileLock var6;
         try {
            String var5 = ConfigDataUpdate.this.getCopyLockFileName(var2);
            this.targetFile = new File(var5);
            this.targetFile.deleteOnExit();
            if (this.targetFile.exists()) {
               ConfigDataUpdate.this.releaseFileLock(var4);
               var4 = null;
               this.updateIsNeeded = false;
            } else {
               this.updateIsNeeded = true;
            }

            if (DataUpdate.isDebugEnabled()) {
               DataUpdate.debug("Is update required : " + this.updateIsNeeded);
            }

            var6 = ConfigDataUpdate.this.acquireFileLock(this.targetFile);
         } finally {
            ConfigDataUpdate.this.releaseFileLock(var4);
         }

         return var6;
      }
   }
}
