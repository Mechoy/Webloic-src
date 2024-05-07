package weblogic.deploy.internal.targetserver.datamanagement;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import weblogic.deploy.internal.targetserver.DeployHelper;
import weblogic.management.DeploymentException;
import weblogic.utils.Debug;

public abstract class Data {
   private static final String INDENT = "  ";
   private String location;
   private String lockPath;
   private boolean stagingEnabled;
   private String stagingMode;
   private Map dataUpdates;
   private DataUpdate currentDataUpdate;

   protected Data(String var1, String var2) {
      this(var1, var2, false);
   }

   protected Data(String var1, String var2, boolean var3) {
      this.stagingMode = null;
      this.dataUpdates = new HashMap();
      this.currentDataUpdate = null;
      this.location = var1;
      this.lockPath = var2;
      this.stagingEnabled = var3;
   }

   protected Data(String var1, String var2, String var3) {
      this.stagingMode = null;
      this.dataUpdates = new HashMap();
      this.currentDataUpdate = null;
      this.location = var1;
      this.lockPath = var2;
      this.stagingMode = var3;
      this.stagingEnabled = "stage".equals(this.stagingMode);
   }

   public final String getLocation() {
      return this.location;
   }

   public final String getLockPath() {
      return this.lockPath;
   }

   public final boolean isStagingEnabled() {
      return this.stagingEnabled;
   }

   public String getStagingMode() {
      return this.stagingMode;
   }

   protected final DataUpdate getCurrentDataUpate() {
      return this.currentDataUpdate;
   }

   public void initDataUpdate(DataUpdateRequestInfo var1) {
      DataUpdate var2 = this.createDataUpdate(var1);
      if (var2 != null) {
         this.setCurrentDataUpdate(var2);
      }
   }

   public final void prepareDataUpdate(String var1) throws DeploymentException {
      try {
         DataUpdate var2 = this.getCurrentDataUpate();
         if (var2 == null) {
            return;
         }

         this.prePrepareDataUpdate();
         var2.download(var1);
         this.postPrepareDataUpdate();
      } catch (Throwable var3) {
         this.resetOnFailure(var3);
      }

   }

   public final void commitDataUpdate() throws DeploymentException {
      try {
         DataUpdate var1 = this.getCurrentDataUpate();
         if (var1 == null) {
            return;
         }

         this.preCommitDataUpdate();
         var1.update();
         this.postCommitDataUpdate();
      } catch (Throwable var6) {
         this.resetOnFailure(var6);
      } finally {
         this.setCurrentDataUpdate((DataUpdate)null);
      }

   }

   public final void cancelDataUpdate(long var1) {
      DataUpdate var3 = this.getDataUpdate(var1);
      if (var3 != null) {
         var3.cancel();
         this.closeDataUpdate(var1, false);
      }
   }

   public void closeDataUpdate(long var1, boolean var3) {
      DataUpdate var4 = this.getDataUpdate(var1);
      if (var4 != null) {
         var4.close(var3);
         this.removeDataUpdate(var1);
      }
   }

   public abstract void releaseLock(long var1);

   public abstract boolean removeStagedFiles();

   public abstract File getSourceFile();

   protected abstract DataUpdate createDataUpdate(DataUpdateRequestInfo var1);

   protected abstract String getRootLocation();

   protected void prePrepareDataUpdate() {
      if (isDebugEnabled()) {
         debug(" Preparing DataUpdate for : " + this);
      }

   }

   protected void postPrepareDataUpdate() {
      if (isDebugEnabled()) {
         debug(" Prepared DataUpdate for : " + this);
      }

   }

   protected void preCommitDataUpdate() throws DeploymentException {
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
      this.closeCurrentDataUpdate();
      this.setCurrentDataUpdate((DataUpdate)null);
   }

   protected final DataUpdate getDataUpdate(long var1) {
      if (isDebugEnabled()) {
         debug(" dataUpdates.get(" + var1 + ")");
      }

      synchronized(this.dataUpdates) {
         return (DataUpdate)this.dataUpdates.get(var1);
      }
   }

   protected final void putDataUpdate(long var1, DataUpdate var3) {
      if (isDebugEnabled()) {
         debug(" dataUpdates.put(" + var1 + " = " + var3);
      }

      synchronized(this.dataUpdates) {
         this.dataUpdates.put(var1, var3);
      }
   }

   protected final void removeDataUpdate(long var1) {
      if (isDebugEnabled()) {
         debug(" dataUpdates.remove(" + var1 + ")");
      }

      synchronized(this.dataUpdates) {
         DataUpdate var4 = (DataUpdate)this.dataUpdates.remove(var1);
         if (var4 != null && var4 == this.currentDataUpdate) {
            this.currentDataUpdate = null;
         }

      }
   }

   protected void dumpFiles(File var1, String var2) {
      if (isDebugEnabled()) {
         debug(var2 + var1);
         if (var1.isDirectory()) {
            File[] var3 = var1.listFiles();
            if (var3 != null) {
               for(int var4 = 0; var4 < var3.length; ++var4) {
                  String var5 = var2 + "  ";
                  this.dumpFiles(var3[var4], var5);
               }
            }
         }
      }

   }

   public abstract void deleteFile(String var1, long var2);

   public abstract File getFileFor(long var1, String var3);

   private void resetOnFailure(Throwable var1) throws DeploymentException {
      Debug.assertion(var1 != null);
      this.onFailure(var1);
      throw DeployHelper.convertThrowable(var1);
   }

   protected static void debug(String var0) {
      weblogic.deploy.common.Debug.deploymentDebug(var0);
   }

   protected static boolean isDebugEnabled() {
      return weblogic.deploy.common.Debug.isDeploymentDebugEnabled();
   }

   private void closeCurrentDataUpdate() {
      DataUpdate var1 = this.getCurrentDataUpate();
      if (var1 != null) {
         this.cancelDataUpdate(var1.getRequestId());
         this.setCurrentDataUpdate((DataUpdate)null);
      }

   }

   private void setCurrentDataUpdate(DataUpdate var1) {
      this.currentDataUpdate = var1;
      if (this.currentDataUpdate != null) {
         this.putDataUpdate(this.currentDataUpdate.getRequestId(), var1);
      }

   }
}
