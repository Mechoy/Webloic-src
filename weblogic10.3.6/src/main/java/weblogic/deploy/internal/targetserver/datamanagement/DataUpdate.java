package weblogic.deploy.internal.targetserver.datamanagement;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.deploy.common.Debug;
import weblogic.deploy.service.DataStream;
import weblogic.deploy.service.FileDataStream;
import weblogic.deploy.service.MultiDataStream;
import weblogic.management.DeploymentException;
import weblogic.utils.FileUtils;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.jars.JarFileUtils;

public abstract class DataUpdate {
   public static final int STATE_UNINITIALIZED = -1;
   public static final int STATE_INITIALIZED = 0;
   public static final int STATE_PREPARED = 1;
   public static final int STATE_COMMITTED = 2;
   private int state = -1;
   private List requestedFiles = null;
   private MultiDataStream downloadedStream = null;
   private long requestId = 0L;
   private Data localData = null;

   protected DataUpdate(Data var1, DataUpdateRequestInfo var2) {
      this.init(var1, var2);
   }

   public final void download(String var1) throws DeploymentException {
      int var2 = this.getState();
      if (var2 != 0) {
         throw new IllegalStateException("Invalid state transition - download on : " + this);
      } else {
         if (isDebugEnabled()) {
            debug(" +++ DataUpdate.download() - preparing data update on : " + this);
         }

         this.doDownload(var1);
         if (isDebugEnabled()) {
            debug(" +++ DataUpdate.download() - prepared data update on : " + this);
         }

         this.setState(1);
      }
   }

   public final void update() throws DeploymentException {
      int var1 = this.getState();
      if (var1 != 1) {
         throw new IllegalStateException("Invalid state transition - commit on " + this);
      } else {
         if (isDebugEnabled()) {
            debug(" +++ DataUpdate.commit() - committing data update on : " + this);
         }

         this.doUpdate();
         if (isDebugEnabled()) {
            debug(" +++ DataUpdate.commit() - committed data update on : " + this);
         }

         this.setState(2);
      }
   }

   public final void cancel() {
      int var1 = this.getState();
      if (var1 >= 1) {
         if (isDebugEnabled()) {
            debug(" +++ DataUpdate.cancel() - cancelling data update on : " + this);
         }

         this.doCancel();
         if (isDebugEnabled()) {
            debug(" +++ DataUpdate.cancel() - cancelling data update on : " + this);
         }

      }
   }

   public final void close(boolean var1) {
      if (isDebugEnabled()) {
         debug(" +++ DataUpdate.close() - closing data update on : " + this);
      }

      this.doClose(var1);
      if (isDebugEnabled()) {
         debug(" +++ DataUpdate.close() - closed data update on : " + this);
      }

   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(super.toString()).append("(localData=");
      var1.append(this.localData).append(", state=").append(this.getStateAsString());
      var1.append(")");
      return var1.toString();
   }

   protected final MultiDataStream getDownloadedStream() {
      if (this.getState() < 1) {
         throw new IllegalStateException("Cannot get downloaded stream since update is not prepared");
      } else {
         return this.downloadedStream;
      }
   }

   protected final List getRequestedFiles() {
      if (this.getState() < 0) {
         throw new IllegalStateException("Cannot get requested files since update is not prepared");
      } else {
         return this.requestedFiles;
      }
   }

   protected final long getRequestId() {
      if (this.getState() < 0) {
         throw new IllegalStateException("Cannot get requestId since update is not prepared");
      } else {
         return this.requestId;
      }
   }

   protected final Data getLocalData() {
      if (this.getState() < 0) {
         throw new IllegalStateException("Cannot get local data since update is not prepared");
      } else {
         return this.localData;
      }
   }

   protected final void setLocalData(Data var1) {
      this.localData = var1;
   }

   protected final void setRequestId(long var1) {
      this.requestId = var1;
   }

   protected final void setRequestedFiles(List var1) {
      this.requestedFiles = new ArrayList();
      this.requestedFiles.addAll(var1);
   }

   protected final void setDownloadedStream(MultiDataStream var1) {
      this.downloadedStream = var1;
   }

   protected void updateLocalData(DataStream var1) throws IOException {
      try {
         String var2 = var1.getName();
         File var3 = new File(this.getLocalData().getLocation(), var2);
         this.copyOrExtractTo(var1, var3);
      } finally {
         var1.close();
      }

   }

   protected final void copyOrExtractTo(DataStream var1, File var2) throws IOException {
      boolean var3 = var1.isZip();
      if (var3) {
         var3 = !var2.exists() || var2.isDirectory();
      }

      if (isDebugEnabled()) {
         debug("copyOrExtractTo() : needsExtract = " + var3);
      }

      if (var1 instanceof FileDataStream) {
         File var4 = ((FileDataStream)var1).getFile();
         if (var3) {
            if (isDebugEnabled()) {
               debug("copyOrExtractTo() : extracting '" + var4.getAbsolutePath() + "' - '" + var2.getAbsolutePath() + "'");
            }

            this.extract(var4, var2);
         } else {
            if (isDebugEnabled()) {
               debug("copyOrExtractTo() : copying '" + var4.getAbsolutePath() + "' - '" + var2.getAbsolutePath() + "'");
            }

            this.copy(var1.getInputStream(), var2);
         }
      } else {
         InputStream var5 = var1.getInputStream();
         if (var3) {
            this.extract(var5, var2);
         } else {
            this.copy(var5, var2);
         }
      }

   }

   protected final void extract(File var1, File var2) throws IOException {
      try {
         JarFileUtils.extract(var1, var2);
      } catch (IOException var4) {
         debug(" Exception occured while copying to '" + var2.getAbsolutePath() + " :: " + StackTraceUtils.throwable2StackTrace(var4));
         throw var4;
      }
   }

   protected final void extract(InputStream var1, File var2) throws IOException {
      try {
         JarFileUtils.extract(var1, var2);
      } catch (IOException var12) {
         debug(" Exception occured while copying to '" + var2.getAbsolutePath() + " :: " + StackTraceUtils.throwable2StackTrace(var12));
         throw var12;
      } finally {
         if (var1 != null) {
            try {
               var1.close();
            } catch (IOException var11) {
            }
         }

      }

   }

   protected final void copy(File var1, File var2) throws IOException {
      try {
         FileUtils.copy(var1, var2);
      } catch (IOException var4) {
         debug(" Exception occured while copying to '" + var2.getAbsolutePath() + " :: " + StackTraceUtils.throwable2StackTrace(var4));
         throw var4;
      }
   }

   protected final void copy(InputStream var1, File var2) throws IOException {
      try {
         if (isDebugEnabled()) {
            debug("copy() : toFile: " + var2.getAbsolutePath());
         }

         File var3 = var2.getParentFile();
         if (isDebugEnabled()) {
            debug("copy() : parent: " + var2.getAbsolutePath());
         }

         boolean var4 = var3.exists();
         if (!var4) {
            var4 = var3.mkdirs();
         }

         if (!var4) {
            throw new IOException("Could not create parent directory : " + var3.getAbsolutePath());
         }

         FileUtils.writeToFile(var1, var2);
         if (isDebugEnabled()) {
            debug("copy() : wrote to : " + var2);
         }
      } catch (IOException var13) {
         debug(" Exception occured while copying to '" + var2.getAbsolutePath() + " :: " + StackTraceUtils.throwable2StackTrace(var13));
         throw var13;
      } finally {
         if (var1 != null) {
            try {
               var1.close();
            } catch (IOException var12) {
            }
         }

      }

   }

   protected void doInit(Data var1, DataUpdateRequestInfo var2) {
      this.setLocalData(var1);
      this.setRequestedFiles(var2.getDeltaFiles());
      this.setRequestId(var2.getRequestId());
   }

   protected abstract void doDownload(String var1) throws DeploymentException;

   protected void doUpdate() throws DeploymentException {
      MultiDataStream var1 = null;

      try {
         var1 = this.getDownloadedStream();
         if (var1 == null) {
            return;
         }

         this.backup();
         Iterator var2 = var1.getDataStreams();

         while(var2.hasNext()) {
            DataStream var3 = (DataStream)var2.next();
            this.updateLocalData(var3);
         }
      } catch (IOException var8) {
         var8.printStackTrace();
         this.restore();
         throw new DeploymentException("Exception occured while copying files", var8);
      } finally {
         if (var1 != null) {
            var1.close();
         }

      }

   }

   protected abstract void doCancel();

   protected abstract void doClose(boolean var1);

   protected abstract void deleteFile(String var1);

   protected abstract File getFileFor(String var1);

   protected static void debug(String var0) {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug(var0);
      }

   }

   protected static boolean isDebugEnabled() {
      return Debug.isDeploymentDebugEnabled();
   }

   protected void backup() {
   }

   protected void restore() {
   }

   protected void deleteBackup() {
   }

   private final void init(Data var1, DataUpdateRequestInfo var2) {
      int var3 = this.getState();
      if (var3 != -1) {
         throw new IllegalStateException("Invalid state transition - init on : " + this);
      } else {
         if (isDebugEnabled()) {
            debug(" +++ DataUpdate.init() - initializing data update with Data=" + var1 + ", files=" + var2.getDeltaFiles() + ", requestId=" + var2.getRequestId() + " : " + this);
         }

         this.doInit(var1, var2);
         if (isDebugEnabled()) {
            debug(" +++ DataUpdate.init() - initialized data update on : " + this);
         }

         this.setState(0);
      }
   }

   private final void setState(int var1) {
      boolean var2 = false;
      switch (var1) {
         case -1:
            break;
         case 0:
            if (this.state != -1) {
               var2 = true;
            }
            break;
         case 1:
            if (this.state != 0) {
               var2 = true;
            }
            break;
         case 2:
            if (this.state != 1) {
               var2 = true;
            }
            break;
         default:
            throw new IllegalStateException("Invalid state '" + var1 + "'");
      }

      if (var2) {
         throw new IllegalStateException("Invalid state transition from " + this.state + " --> " + var1);
      } else {
         this.state = var1;
      }
   }

   private final int getState() {
      return this.state;
   }

   private final String getStateAsString() {
      return this.getStateAsString(this.state);
   }

   private final String getStateAsString(int var1) {
      switch (var1) {
         case -1:
            return "STATE_UNINITIALIZED";
         case 0:
            return "STATE_INITIALIZED";
         case 1:
            return "STATE_PREPARED";
         case 2:
            return "STATE_COMMITTED";
         default:
            throw new IllegalStateException("Invalid state");
      }
   }
}
