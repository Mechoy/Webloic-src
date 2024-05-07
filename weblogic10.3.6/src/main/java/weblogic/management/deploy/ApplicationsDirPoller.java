package weblogic.management.deploy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.exceptions.DeploymentManagerCreationException;
import javax.enterprise.deploy.spi.exceptions.TargetException;
import javax.enterprise.deploy.spi.status.DeploymentStatus;
import javax.enterprise.deploy.spi.status.ProgressObject;
import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;
import weblogic.deploy.api.tools.SessionHelper;
import weblogic.j2ee.J2EEUtils;
import weblogic.management.DomainDir;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ConfigurationError;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.LibraryMBean;
import weblogic.management.deploy.internal.ApplicationPollerLogger;
import weblogic.management.provider.EditAccess;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.ManagementServiceRestricted;
import weblogic.management.utils.AppDeploymentHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.Debug;
import weblogic.utils.FileUtils;

public class ApplicationsDirPoller extends GenericAppPoller {
   private static final boolean debug = false;
   private static final boolean debug2 = false;
   private static final boolean methodTrace = false;
   private static final int MIN_POLLER_INTERVAL = 3000;
   private WebLogicDeploymentManager deployer;
   private boolean firstRun;
   private static final String LAST_RUN_FILE_NAME = ".app_poller_lastrun";
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static DomainMBean domain;
   private static final String SERVER_CACHE_DIR;
   private Set filesBeingCopied;

   private static String ensureCacheDirInitialized() {
      String var0 = DomainDir.getCacheDirForServer(ManagementService.getPropertyService(kernelId).getServerName());
      File var1 = new File(var0);
      if (!var1.exists()) {
         var1.mkdir();
      }

      return var0;
   }

   private ApplicationsDirPoller(File var1, boolean var2, long var3, String var5) {
      super(var1, var2, var3, var5);
      this.filesBeingCopied = new HashSet();
      if (!this.startDirFound) {
         boolean var6 = var1.mkdir();
         if (!var6) {
            ApplicationPollerLogger.logCouldnotCreateAutodeployDir(var1.toString());
         }
      }

      this.setSleepInterval(var3);
      this.firstRun = true;

      try {
         this.doit();
      } catch (Throwable var7) {
         ApplicationPollerLogger.logThrowableOnServerStartup(var7);
      }

   }

   public ApplicationsDirPoller(File var1, boolean var2, long var3) {
      this(var1, var2, var3, SERVER_CACHE_DIR + File.separatorChar + ".app_poller_lastrun");
   }

   protected final boolean shouldActivate(File var1) {
      if (this.ignoreFile(var1)) {
         return false;
      } else if (var1.isDirectory()) {
         Long var2 = this.getLastCheckPoint(var1);
         if (var2 == null) {
            this.setCheckPoint(var1, (new Date()).getTime());
            return true;
         } else {
            File var3 = this.getRedeployFile(var1);
            if (var3 == null) {
               return false;
            } else {
               this.setCheckPoint(var1, var3.lastModified());
               return var3.lastModified() > var2;
            }
         }
      } else {
         return !J2EEUtils.isValidArchiveName(var1.getName()) && !J2EEUtils.isValidWLSModuleName(var1.getName()) ? false : super.shouldActivate(var1);
      }
   }

   public final void doActivate() {
      if (this.verbose) {
         Debug.say("doActivate");
      }

      Iterator var1 = this.getActivateFileList().iterator();

      while(var1.hasNext()) {
         DeploymentOptions var2 = this.createDeploymentOptions();
         boolean var3 = false;
         String var4 = (String)var1.next();
         File var5 = new File(var4);
         String var6 = this.getUnusedNameForApp(var5);
         var2.setName(var6);
         AppDeploymentMBean var7 = ManagementService.getRuntimeAccess(kernelId).getDomain().lookupAppDeployment(var6);
         if (var7 != null) {
            var3 = true;
            if (this.firstRun) {
               ApplicationPollerLogger.logRedeployingOnStartup(var6);
            }
         }

         String var8 = var5.getAbsolutePath();

         try {
            var5 = var5.getCanonicalFile();
            var8 = var5.getCanonicalPath();
         } catch (IOException var14) {
         }

         String var9 = null;
         if (!var3) {
            if (var5.isDirectory()) {
               var9 = "nostage";
            } else {
               var9 = "stage";
            }

            var2.setStageMode(var9);
         }

         try {
            if (var5.isDirectory() && var3 || !this.fileIsLocked(var5)) {
               ApplicationPollerLogger.logActivate(var6);
               String var10 = DomainDir.getRootDir();
               debugSay(" +++ rootDir (before canonicalization) : " + var10);
               File var11 = new File(var10);

               try {
                  var10 = var11.getCanonicalPath();
               } catch (IOException var13) {
               }

               debugSay(" +++ rootDir : " + var10);
               debugSay(" +++ fileCanPath : " + var8);
               this.ensureDeploymentMgrInitialized();
               var2.setDefaultSubmoduleTargets(domain.isAutoDeployForSubmodulesEnabled());
               ProgressObject var12 = this.deployer.deploy((TargetModuleID[])(new TargetModuleID[0]), new File(var8), (File)null, var2);
               if (var12.getDeploymentStatus().isFailed()) {
                  throw new ManagementException(var12.getDeploymentStatus().getMessage());
               }

               if (var5.isDirectory()) {
                  this.warnOnManagedServerTargets(var12, var6);
               }

               this.waitForTimeout(var12);
            }
         } catch (ManagementException var15) {
            ApplicationPollerLogger.logThrowableOnActivate(var6, var15);
         } catch (TargetException var16) {
            ApplicationPollerLogger.logThrowableOnActivate(var6, var16);
         } catch (DeploymentManagerCreationException var17) {
            ApplicationPollerLogger.logThrowableOnActivate(var6, var17);
         }
      }

      this.firstRun = false;
   }

   public final void doDeactivate() {
      Iterator var2 = this.getDeactivateFileList().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         File var4 = new File(var3);
         String var5 = this.getUnusedNameForApp(var4);

         try {
            AppDeploymentMBean var1 = domain.lookupAppDeployment(var5);
            if (var1 != null) {
               ApplicationPollerLogger.logRemove(var5);
               TargetModuleID[] var6 = new TargetModuleID[0];
               DeploymentOptions var7 = this.createDeploymentOptions();
               var7.setName(var5);
               this.ensureDeploymentMgrInitialized();
               ProgressObject var8 = null;

               try {
                  var8 = this.deployer.undeploy(var6, var7);
               } catch (Exception var10) {
                  throw new ManagementException(var10);
               }

               if (var8.getDeploymentStatus().isFailed()) {
                  throw new ManagementException(var8.getDeploymentStatus().getMessage());
               }

               this.waitForTimeout(var8);
            }
         } catch (ManagementException var11) {
            ApplicationPollerLogger.logThrowableOnDeactivate(var5, var11);
         } catch (DeploymentManagerCreationException var12) {
            ApplicationPollerLogger.logThrowableOnDeactivate(var5, var12);
         }
      }

   }

   private DeploymentOptions createDeploymentOptions() {
      DeploymentOptions var1 = new DeploymentOptions();
      var1.setUseNonexclusiveLock(true);
      var1.setOperationInitiatedByAutoDeployPoller(true);
      return var1;
   }

   private void warnOnManagedServerTargets(ProgressObject var1, String var2) {
      TargetModuleID[] var3 = var1.getResultTargetModuleIDs();
      if (var3 != null) {
         HashSet var4 = new HashSet();

         for(int var5 = 0; var5 < var3.length; ++var5) {
            TargetModuleID var6 = var3[var5];
            var4.add(var6.getTarget());
         }

         if (var4.size() > 1) {
            ApplicationPollerLogger.logWarnOnManagedServerTargets(var2);
         }
      }

   }

   private void waitForTimeout(ProgressObject var1) {
      boolean var2 = false;

      while(!var2) {
         DeploymentStatus var3 = var1.getDeploymentStatus();
         if (!var3.isRunning()) {
            return;
         }

         try {
            Thread.sleep(2000L);
         } catch (InterruptedException var5) {
         }
      }

   }

   public void setSleepInterval(long var1) {
      if (var1 < 3000L) {
         super.setSleepInterval(3000L);
      } else {
         super.setSleepInterval(var1);
      }

   }

   public static void removeStagedFilesForAppsRemovedSinceLastShutdown() {
      File var0 = new File(DomainDir.getAppPollerDir());
      ArrayList var1 = new ArrayList();
      AppDeploymentMBean[] var2 = AppDeploymentHelper.getAppsAndLibs(domain);

      String var5;
      for(int var3 = 0; var2 != null && var3 < var2.length; ++var3) {
         AppDeploymentMBean var4 = var2[var3];
         var5 = var4.getAbsoluteSourcePath();
         if (var5 != null && isInAppsDir(var0, var5)) {
            File var6 = new File(var5);
            if (!var6.exists()) {
               String var7 = ManagementService.getRuntimeAccess(kernelId).getServer().getStagingDirectoryName() + File.separatorChar + var4.getName();
               FileUtils.remove(new File(var7));
               AppDeploymentHelper.destroyAppOrLib(var4, domain);
               if (!var4.isInternalApp()) {
                  var1.add(var4);
               }
            }
         }
      }

      if (var1.size() != 0) {
         AppDeploymentMBean[] var13 = (AppDeploymentMBean[])((AppDeploymentMBean[])var1.toArray(new AppDeploymentMBean[0]));
         EditAccess var14 = ManagementServiceRestricted.getEditAccess(kernelId);

         try {
            var14.startEdit(0, 120000, true);
         } catch (ManagementException var12) {
            return;
         }

         var5 = null;

         DomainMBean var15;
         try {
            var15 = var14.getDomainBean();
         } catch (ManagementException var11) {
            return;
         }

         for(int var16 = 0; var16 < var13.length; ++var16) {
            AppDeploymentMBean var17 = var13[var16];
            String var8 = var17.getName();
            Object var9;
            if (var17 instanceof LibraryMBean) {
               var9 = var15.lookupLibrary(var8);
            } else {
               var9 = var15.lookupAppDeployment(var8);
            }

            if (var9 != null) {
               AppDeploymentHelper.destroyAppOrLib((AppDeploymentMBean)var9, var15);
            }
         }

         try {
            var14.saveChanges();
            var14.activateChanges(3600000L);
         } catch (ManagementException var10) {
         }

      }
   }

   private boolean fileIsLocked(File var1) {
      FileInputStream var2 = null;

      boolean var3;
      try {
         if (var1.isDirectory()) {
            if (var1.renameTo(var1)) {
               this.fileFree(var1);
               var3 = false;
               return var3;
            }

            this.fileHeld(var1);
            this.removeFileFromMap(var1);
            var3 = true;
            return var3;
         }

         var2 = new FileInputStream(var1);
         this.fileFree(var1);
         var3 = false;
      } catch (IOException var16) {
         this.fileHeld(var1);
         this.removeFileFromMap(var1);
         boolean var4 = true;
         return var4;
      } finally {
         if (var2 != null) {
            try {
               var2.close();
               var2 = null;
            } catch (IOException var15) {
               ApplicationPollerLogger.logIOException(var15);
               var2 = null;
            }
         }

      }

      return var3;
   }

   private void fileHeld(File var1) {
      if (!this.filesBeingCopied.contains(var1)) {
         ApplicationPollerLogger.logFileHeld(var1);
         this.filesBeingCopied.add(var1);
      }
   }

   private void fileFree(File var1) {
      if (this.filesBeingCopied.contains(var1)) {
         this.filesBeingCopied.remove(var1);
      }

   }

   private String getUnusedNameForApp(File var1) {
      String var2 = var1.getName();
      String var3;
      if (J2EEUtils.isValidArchiveName(var2)) {
         var3 = new String(J2EEUtils.getArchiveName(var2) + "_" + J2EEUtils.getArchivePostfix(var2));
      } else if (J2EEUtils.isValidWLSModuleName(var2)) {
         var3 = new String(J2EEUtils.getWLSModuleName(var2) + "_" + J2EEUtils.getWLSModulePostfix(var2));
      } else {
         var3 = var2 + "_dir";
      }

      var3 = "_appsdir_" + var3;
      String var4 = var3;
      AppDeploymentMBean[] var5 = AppDeploymentHelper.getAppsAndLibs(domain);

      for(int var6 = 0; var5 != null && var6 < var5.length; ++var6) {
         AppDeploymentMBean var7 = var5[var6];
         if (!var7.isInternalApp()) {
            String var8 = var7.getAbsoluteSourcePath();
            File var9 = new File(var8);

            try {
               File var10 = var9.getCanonicalFile();
               File var11 = var1.getCanonicalFile();
               if (var10.equals(var11)) {
                  return var7.getName();
               }
            } catch (IOException var13) {
               ApplicationPollerLogger.logIOException(var13);
            }
         }
      }

      boolean var14 = false;
      int var15 = 0;

      while(!var14) {
         AppDeploymentMBean var16 = domain.lookupAppDeployment(var3);
         if (var16 == null) {
            var14 = true;
         } else {
            String var17 = var16.getAbsoluteSourcePath();

            String var18;
            try {
               var18 = var1.getCanonicalPath();
            } catch (IOException var12) {
               ApplicationPollerLogger.logIOException(var12);
               var18 = var1.getAbsolutePath();
            }

            if (!var17.equals(var18)) {
               var3 = var4 + "-" + var15++;
            } else {
               var14 = true;
            }
         }
      }

      if (!var14) {
         throw new ConfigurationError("cannot have more than 10000 entries with the same name");
      } else {
         return var3;
      }
   }

   private File getRedeployFile(File var1) {
      File var2 = new File(var1.getAbsolutePath() + File.separatorChar + "META-INF" + File.separatorChar + "REDEPLOY");
      if (!var2.exists()) {
         var2 = new File(var1.getAbsolutePath() + File.separatorChar + "WEB-INF" + File.separatorChar + "REDEPLOY");
      }

      return var2.exists() ? var2 : null;
   }

   private boolean ignoreFile(File var1) {
      return var1.getName().startsWith(".wlnot");
   }

   private void ensureDeploymentMgrInitialized() throws DeploymentManagerCreationException {
      if (this.deployer == null) {
         this.deployer = SessionHelper.getDeploymentManager((String)null, (String)null);
      }

   }

   public static boolean isInAppsDir(File var0, String var1) {
      String var2 = var0.getName();

      try {
         var0 = var0.getCanonicalFile();
         var2 = var0.getCanonicalPath();
      } catch (IOException var6) {
         ApplicationPollerLogger.logIOException(var6);
      }

      File var3 = new File(var1);

      try {
         var3 = var3.getCanonicalFile();
         var1 = var3.getCanonicalPath();
      } catch (IOException var5) {
         ApplicationPollerLogger.logIOException(var5);
      }

      return var1.indexOf(var2) > -1;
   }

   private static void debugSay(String var0) {
   }

   static {
      domain = ManagementService.getRuntimeAccess(kernelId).getDomain();
      SERVER_CACHE_DIR = ensureCacheDirInitialized();
   }
}
