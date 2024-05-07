package weblogic.deploy.internal.targetserver.datamanagement;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import weblogic.deploy.compatibility.NotificationBroadcaster;
import weblogic.deploy.internal.targetserver.BasicDeployment;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.management.DomainDir;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.deploy.internal.MBeanConverter;
import weblogic.management.deploy.internal.SlaveDeployerLogger;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.FileUtils;

public class AppData extends Data {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String serverName;
   private String appName = null;
   private String appVersionIdentifier = null;
   private String appId = null;
   private String appRootLocation = null;
   private String stageLocation = null;
   private String originalPlanPath = null;
   private String relativePlanPath = null;
   private BasicDeployment deployment = null;
   private final boolean isAppDeployment;
   private File srcFile = null;

   public AppData(BasicDeploymentMBean var1, BasicDeployment var2, String var3, String var4, String var5) {
      super(var4, var5, var3);
      this.deployment = var2;
      this.appId = var1.getName();
      this.isAppDeployment = var1 instanceof AppDeploymentMBean;
      this.setAppRootLocation(var1);
      if (this.isAppDeployment) {
         AppDeploymentMBean var6 = (AppDeploymentMBean)var1;
         this.appName = var6.getApplicationName();
         this.appVersionIdentifier = var6.getVersionIdentifier();
         this.originalPlanPath = var6.getLocalPlanPath();
         if (!this.isStagingEnabled() && !"external_stage".equals(var3)) {
            this.srcFile = new File(var6.getAbsoluteSourcePath());
         } else {
            this.srcFile = new File(this.getLocation());
         }
      } else {
         this.appName = var1.getName();
         this.srcFile = new File(this.getLocation());
      }

   }

   public String getAppName() {
      return this.appName;
   }

   public String getAppVersionIdentifier() {
      return this.appVersionIdentifier;
   }

   protected DataUpdate createDataUpdate(DataUpdateRequestInfo var1) {
      if (var1.isDelete()) {
         return this.isStagingEnabled() ? new AppDataDeleteUpdate(this, var1) : null;
      } else {
         boolean var2 = false;
         if (this.isStagingEnabled()) {
            if (var1 instanceof ModuleRedeployDataUpdateRequestInfo) {
               return new ModuleRedeployDataUpdate(this, var1);
            }

            if (var1.isPlanUpdate()) {
               return new PlanDataUpdate(this, var1);
            }

            if (isDebugEnabled()) {
               debug(" createDataUpdate - staging location: " + this.getLocation());
            }

            File var3 = new File(this.getLocation());
            if (!var3.exists()) {
               var2 = true;
            } else if (!this.isAppDeployment) {
               var2 = false;
            } else if (var1.isStatic()) {
               int var4 = this.deployment.getStagingState();
               if (!this.isRestageOnlyOnRedeploy()) {
                  if (var4 < 1) {
                     var2 = true;
                     if (isDebugEnabled()) {
                        debug(" createDataUpdate - needs restage stagingState: " + var4);
                     }
                  } else {
                     long var5 = this.deployment.getArchiveTimeStamp();
                     long var7 = this.deployment.getPlanTimeStamp();
                     if (isDebugEnabled()) {
                        debug(" createDataUpdate - check if needs restage archive ts: " + var5 + " loc.lastModified: " + var3.lastModified());
                     }

                     if (var5 > 0L && var3.lastModified() <= var5) {
                        var2 = true;
                     } else if (this.originalPlanPath != null) {
                        File var9 = new File(this.originalPlanPath);
                        if (isDebugEnabled()) {
                           debug(" createDataUpdate - check if needs restage plan ts: " + var7 + " plan.lastModified: " + var9.lastModified());
                        }

                        if (!var9.exists() || var7 > 0L && var9.lastModified() <= var7) {
                           var2 = true;
                        }
                     }
                  }
               }
            } else {
               var2 = true;
            }
         }

         if (isDebugEnabled()) {
            debug(" +++ [" + this + "] needsRestage : " + var2);
         }

         if (var2) {
            AppDataUpdate var10 = new AppDataUpdate(this, var1);
            return var10;
         } else {
            return null;
         }
      }
   }

   public final String getRootLocation() {
      return this.appRootLocation;
   }

   public final String getPlanPath() {
      return this.originalPlanPath;
   }

   public final String getRelativePlanPath() throws IOException {
      if (this.relativePlanPath == null || this.relativePlanPath.length() == 0) {
         if (!this.isStagingEnabled()) {
            if (isDebugEnabled()) {
               debug(" getRelativePlanPath():nostage - returning original plan path :" + this.originalPlanPath);
            }

            this.relativePlanPath = this.originalPlanPath;
         } else {
            if (this.originalPlanPath == null) {
               return null;
            }

            File var1 = new File(this.originalPlanPath);
            if (isDebugEnabled()) {
               debug(" getRelativePlanPath(): plan file name : " + var1.getAbsolutePath());
            }

            File var2 = new File(this.getRootLocation());
            if (isDebugEnabled()) {
               debug(" getRelativePlanPath(): application root dir : " + var2);
            }

            String var3 = createRelativePath(var2, var1);
            if (isDebugEnabled()) {
               debug(" getRelativePlanPath(): Created relative path for plan file : " + var3);
            }

            this.relativePlanPath = var3;
         }
      }

      return this.relativePlanPath;
   }

   public final void updateDescriptorsPathInfo(BasicDeploymentMBean var1) {
      if (this.isAppDeployment && var1 instanceof AppDeploymentMBean) {
         this.originalPlanPath = ((AppDeploymentMBean)var1).getLocalPlanPath();
         this.relativePlanPath = null;
      }

   }

   public void releaseLock(long var1) {
   }

   public boolean removeStagedFiles() {
      if (this.isAppDeployment && this.isStagingEnabled()) {
         File var1 = null;
         var1 = new File(this.getRootLocation());
         if (var1.exists()) {
            File var2 = new File(this.stageLocation);
            File var3 = var1;
            File var4 = null;

            boolean var5;
            do {
               var4 = var3.getParentFile();
               var5 = FileUtils.remove(var3);
               var3 = var4;
            } while(var5 && !var2.equals(var4) && var4.listFiles().length == 0);

            String var6 = var5 ? " Removed staged files for deployment  - " + this.appName + " successfully" : " Couldn't remove staged files for app deployment  - " + this.appName;
            if (isDebugEnabled()) {
               debug(var6);
            }

            if (!var5) {
               this.dumpFiles(var1, "");
            }

            return var5;
         }

         if (isDebugEnabled()) {
            debug(" Staging directory '" + var1 + "' does not exists. " + "So, nothing to remove here.");
         }
      }

      return true;
   }

   public File getSourceFile() {
      return this.srcFile;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(super.toString()).append("(appId=");
      var1.append(this.appId).append(")");
      return var1.toString();
   }

   protected final void prePrepareDataUpdate() {
      NotificationBroadcaster.sendAppNotification("distributing", this.appId, (String)null);
   }

   protected final void preCommitDataUpdate() throws DeploymentException {
      DataUpdate var1 = this.getCurrentDataUpate();
      if (var1 instanceof AppDataUpdate) {
         if (var1 != null && ((AppDataUpdate)var1).isFullUpdate()) {
            boolean var2 = this.removeStagedFiles();
            File var3 = new File(this.getLocation());
            if (!var2 && var3.exists()) {
               this.deployment.relayStagingState(0);
               Loggable var4 = SlaveDeployerLogger.logRemoveStagedFilesFailedLoggable(this.appName, var3.toString());
               throw new DeploymentException(var4.getMessage());
            }
         }

      }
   }

   protected void postCommitDataUpdate() {
      NotificationBroadcaster.sendAppNotification("distributed", this.appId, (String)null);
      this.deployment.relayStagingState(1);
      if (this.isAppDeployment) {
         MBeanConverter.addStagedTarget((AppDeploymentMBean)this.deployment.getDeploymentMBean(), serverName);
      }

   }

   protected void onFailure(Throwable var1) {
      super.onFailure(var1);
      this.deployment.relayStagingState(0);
   }

   public void deleteFile(String var1, long var2) {
   }

   public File getFileFor(long var1, String var3) {
      return null;
   }

   protected final boolean isSystemResource() {
      return !this.isAppDeployment;
   }

   private static String createRelativePath(File var0, File var1) throws IOException {
      boolean var2 = isDebugEnabled();
      String var3 = var0.getCanonicalPath();
      if (var2) {
         debug(" createRelativePath(): given baseFile: " + var3);
      }

      String var4 = var1.getCanonicalPath();
      if (var2) {
         debug(" createRelativePath(): given givenURI: " + var4);
      }

      int var5 = var4.indexOf(var3);
      if (var2) {
         debug(" createRelativePath(): indexOf givenURI in given baseFile: " + var5);
      }

      if (var5 == -1) {
         throw new AssertionError("uri '" + var4 + "' is not sub dir of" + "'" + var3 + "'");
      } else {
         String var6 = var4.substring(var3.length() + 1, var4.indexOf(var1.getName()));
         if (var2) {
            debug(" createRelativePath(): relative rootPath of givenURI : " + var6);
         }

         String var7 = var6 + var1.getName();
         if (var2) {
            debug(" createRelativePath(): returning result : " + var7);
         }

         return var7;
      }
   }

   private void setAppRootLocation(BasicDeploymentMBean var1) {
      if (!this.isStagingEnabled()) {
         this.appRootLocation = null;
      }

      if (this.isAppDeployment) {
         this.appRootLocation = ((AppDeploymentMBean)var1).getRootStagingDir();
         this.stageLocation = ManagementService.getRuntimeAccess(kernelId).getServer().getStagingDirectoryName();
      } else {
         this.appRootLocation = DomainDir.getConfigDir();
      }

   }

   private boolean isRestageOnlyOnRedeploy() {
      return ManagementService.getRuntimeAccess(kernelId).getDomain().getDeploymentConfiguration().isRestageOnlyOnRedeploy();
   }

   static {
      serverName = ManagementService.getRuntimeAccess(kernelId).getServerName();
   }
}
