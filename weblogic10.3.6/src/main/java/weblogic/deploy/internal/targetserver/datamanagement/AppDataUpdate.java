package weblogic.deploy.internal.targetserver.datamanagement;

import java.io.File;
import java.io.IOException;
import java.util.List;
import weblogic.deploy.common.DeploymentConstants;
import weblogic.deploy.service.DataStream;
import weblogic.deploy.service.DataTransferHandler;
import weblogic.deploy.service.DataTransferRequest;
import weblogic.deploy.service.FileDataStream;
import weblogic.deploy.service.datatransferhandlers.DataHandlerManager;
import weblogic.j2ee.J2EEUtils;
import weblogic.management.DeploymentException;

public class AppDataUpdate extends DataUpdate implements DeploymentConstants {
   private boolean isFullDataUpdate = false;
   private boolean isPlanUpdate = false;

   public AppDataUpdate(Data var1, DataUpdateRequestInfo var2) {
      super(var1, var2);
      List var3 = var2.getDeltaFiles();
      this.isFullDataUpdate = var3 == null || var3.isEmpty();
      if (isDebugEnabled()) {
         debug(" +++ isFileDataUpdate : " + this.isFullDataUpdate);
      }

      this.isPlanUpdate = var2.isPlanUpdate();
      if (isDebugEnabled()) {
         debug(" +++ isPlanUpdate : " + this.isPlanUpdate);
      }

   }

   protected final void doDownload(String var1) throws DeploymentException {
      DataTransferRequest var2 = this.createDataTransferRequest();

      try {
         DataTransferHandler var3 = DataHandlerManager.getHandler(var1);
         this.setDownloadedStream(var3.getDataAsStream(var2));
      } catch (IOException var4) {
         var4.printStackTrace();
         throw new DeploymentException("Exception occured while downloading files", var4);
      }
   }

   protected void doCancel() {
      this.restore();
   }

   protected void doClose(boolean var1) {
      if (!var1) {
         this.restore();
      }

      this.deleteBackup();
   }

   protected DataTransferRequest createDataTransferRequest() {
      AppData var1 = this.getLocalAppData();
      return new AppDataTransferRequestImpl(var1.getAppName(), var1.getAppVersionIdentifier(), this.getRequestId(), this.getRequestedFiles(), var1.getLockPath(), this.isPlanUpdate);
   }

   protected void deleteFile(String var1) {
   }

   protected File getFileFor(String var1) {
      return null;
   }

   protected final boolean isFullUpdate() {
      if (isDebugEnabled()) {
         debug(" +++ isFullUpdate() : " + this.isFullDataUpdate);
      }

      return this.isFullDataUpdate;
   }

   protected void end() {
   }

   protected AppData getLocalAppData() {
      return (AppData)this.getLocalData();
   }

   protected void updateLocalData(DataStream var1) throws IOException {
      String var2 = var1.getName();
      boolean var3 = this.isFullUpdate() || this.getLocalAppData().isSystemResource() || var2.endsWith("wl_app_desc.jar") || var2.endsWith("wl_app_src.jar");
      if (isDebugEnabled()) {
         debug("updateLocalData(): isSpecial : " + var3);
      }

      if (!var3) {
         super.updateLocalData(var1);
      } else {
         try {
            File var4 = new File(this.getLocalAppData().isSystemResource() ? this.getLocalAppData().getRootLocation() : (var2.endsWith("wl_app_desc.jar") ? this.getLocalAppData().getRootLocation() : this.getLocalAppData().getLocation()));
            if (isDebugEnabled()) {
               debug(" +++ destFile : " + var4.getAbsolutePath());
            }

            if (!isValidJ2EEArchive(var1) && var1.isZip()) {
               if (var1 instanceof FileDataStream) {
                  this.extract(((FileDataStream)var1).getFile(), var4);
               } else {
                  this.extract(var1.getInputStream(), var4);
               }
            } else {
               if (isDebugEnabled()) {
                  debug(" +++ isValidJ2EEArchive or stream is not zip.....");
               }

               this.copy(var1.getInputStream(), var4);
            }
         } finally {
            var1.close();
         }

      }
   }

   private static boolean isValidJ2EEArchive(DataStream var0) {
      String var1 = var0.getName();
      if (!var1.endsWith("wl_app_src.jar") && !var1.endsWith("wl_app_desc.jar")) {
         return J2EEUtils.isValidArchiveName(var1) && var0.isZip();
      } else {
         return false;
      }
   }
}
