package weblogic.deploy.internal.targetserver.datamanagement;

import java.io.File;
import weblogic.management.DeploymentException;
import weblogic.management.DomainDir;

public class ConfigData extends Data {
   private String rootLocation = null;

   public ConfigData(String var1, String var2, String var3) {
      super(var1, var2, var3);
      this.rootLocation = DomainDir.getRootDir();
   }

   protected DataUpdate createDataUpdate(DataUpdateRequestInfo var1) {
      return this.isStagingEnabled() ? new ConfigDataUpdate(this, var1) : null;
   }

   public final String getRootLocation() {
      return this.rootLocation;
   }

   public boolean removeStagedFiles() {
      return true;
   }

   public File getSourceFile() {
      return new File(this.getLocation());
   }

   protected final void preCommitDataUpdate() throws DeploymentException {
      if (isDebugEnabled()) {
         debug(" Commiting DataUpdate for : " + this);
      }

   }

   protected void postCommitDataUpdate() {
      if (isDebugEnabled()) {
         debug(" Committed DataUpdate for : " + this);
      }

   }

   protected void onFailure(Throwable var1) {
      if (isDebugEnabled()) {
         debug(" Failure occured : " + this);
      }

      super.onFailure(var1);
   }

   public final void deleteFile(String var1, long var2) {
      DataUpdate var4 = this.getDataUpdate(var2);
      if (var4 != null) {
         var4.deleteFile(var1);
      }
   }

   public final void releaseLock(long var1) {
      DataUpdate var3 = this.getDataUpdate(var1);
      if (var3 != null) {
         ((ConfigDataUpdate)var3).releaseLock();
      }
   }

   public final File getFileFor(long var1, String var3) {
      DataUpdate var4 = this.getDataUpdate(var1);
      return var4 == null ? new File(this.getRootLocation(), var3) : var4.getFileFor(var3);
   }
}
