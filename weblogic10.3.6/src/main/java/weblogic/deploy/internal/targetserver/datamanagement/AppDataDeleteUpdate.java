package weblogic.deploy.internal.targetserver.datamanagement;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import weblogic.management.DeploymentException;
import weblogic.utils.FileUtils;

public class AppDataDeleteUpdate extends DataUpdate {
   public AppDataDeleteUpdate(Data var1, DataUpdateRequestInfo var2) {
      super(var1, var2);
   }

   protected final void doDownload(String var1) throws DeploymentException {
   }

   protected final void doUpdate() throws DeploymentException {
      try {
         Iterator var1 = this.getRequestedFiles().iterator();
         if (var1 != null && var1.hasNext()) {
            File var3;
            boolean var4;
            do {
               if (!var1.hasNext()) {
                  return;
               }

               String var2 = (String)var1.next();
               var3 = new File(this.getLocalAppData().getSourceFile(), var2);
               var4 = FileUtils.remove(var3);
            } while(var4);

            throw new IOException("Could not remove file or dir : " + var3.getAbsolutePath());
         }
      } catch (IOException var5) {
         var5.printStackTrace();
         throw new DeploymentException("Exception occured while copying files", var5);
      }
   }

   protected void doCancel() {
   }

   protected void doClose(boolean var1) {
   }

   protected void deleteFile(String var1) {
   }

   protected File getFileFor(String var1) {
      return null;
   }

   private AppData getLocalAppData() {
      return (AppData)this.getLocalData();
   }
}
