package weblogic.deploy.api.tools;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import javax.enterprise.deploy.shared.StateType;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import javax.enterprise.deploy.spi.exceptions.DeploymentManagerCreationException;
import javax.enterprise.deploy.spi.exceptions.InvalidModuleException;
import javax.enterprise.deploy.spi.status.DeploymentStatus;
import javax.enterprise.deploy.spi.status.ProgressObject;
import weblogic.deploy.api.internal.SPIDeployerLogger;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.management.DomainDir;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.FileUtils;

public class ManagedSessionHelper {
   private boolean debug;
   private static final AuthenticatedSubject kernelid = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final String BAD_PLAN = "_wls_bad_plan.xml";
   private static final String BACKUP_PLAN = "_wls_plan_bk.xml";
   private AppDeploymentMBean mbean;
   private RuntimeAccess rt;
   protected WebLogicDeploymentManager myDM;
   private String server;
   private String host;
   private String port;
   private boolean isadmin;
   private boolean noplan;
   private File planBackup;
   private IOException backupPlanException;
   private boolean enableLightWeightView;
   protected SessionHelper helper;

   protected AppDeploymentMBean getAppDeploymentMBean() {
      return this.mbean;
   }

   protected String getPort() {
      return this.port;
   }

   protected String getHost() {
      return this.host;
   }

   protected void initSessionHelper(WebLogicDeploymentManager var1) {
      this.helper = SessionHelper.getInstance(this.myDM);
   }

   protected boolean isAdmin() {
      return this.isadmin;
   }

   public SessionHelper getHelper() {
      return this.helper;
   }

   public ManagedSessionHelper(AppDeploymentMBean var1) throws ConfigurationException, IOException, DeploymentManagerCreationException, InvalidModuleException {
      this(var1, (String)null, (String)null);
   }

   public ManagedSessionHelper(AppDeploymentMBean var1, boolean var2) throws ConfigurationException, IOException, DeploymentManagerCreationException, InvalidModuleException {
      this.debug = Debug.isDebug("deploy");
      this.noplan = true;
      this.planBackup = null;
      this.backupPlanException = null;
      this.enableLightWeightView = false;
      this.enableLightWeightView = var2;
      this.rt = ManagementService.getRuntimeAccess(kernelid);
      if (this.rt == null) {
         throw new IllegalStateException("Must be running on server");
      } else {
         String var3 = this.rt.getAdminServerName();
         ServerMBean var4 = ((DomainMBean)var1.getParent()).lookupServer(var3);
         this.port = (new Integer(var4.getListenPort())).toString();
         this.host = var4.getListenAddress();
         this.server = this.rt.getServerName();
         this.isadmin = var3.equals(this.server);
         this.mbean = var1;
         this.createSession(var1, (String)null, (String)null);
      }
   }

   public ManagedSessionHelper(AppDeploymentMBean var1, String var2, String var3) throws ConfigurationException, IOException, DeploymentManagerCreationException, InvalidModuleException {
      this.debug = Debug.isDebug("deploy");
      this.noplan = true;
      this.planBackup = null;
      this.backupPlanException = null;
      this.enableLightWeightView = false;
      this.rt = ManagementService.getRuntimeAccess(kernelid);
      if (this.rt == null) {
         throw new IllegalStateException("Must be running on server");
      } else {
         String var4 = this.rt.getAdminServerName();
         ServerMBean var5 = ((DomainMBean)var1.getParent()).lookupServer(var4);
         this.port = (new Integer(var5.getListenPort())).toString();
         this.host = var5.getListenAddress();
         this.server = this.rt.getServerName();
         this.isadmin = var4.equals(this.server);
         this.mbean = var1;
         this.createSession(var1, var2, var3);
      }
   }

   private void createSession(AppDeploymentMBean var1, String var2, String var3) throws DeploymentManagerCreationException, ConfigurationException, IOException, InvalidModuleException {
      this.initDM(var2, var3);
      this.initSessionHelper(this.myDM);
      this.helper = this.getHelper();
      String var4 = var1.getLocalInstallDir();
      if (var4 != null) {
         this.helper.setApplicationRoot(new File(var4));
      }

      var4 = var1.getLocalSourcePath();
      if (var4 != null) {
         this.helper.setApplication(new File(var4));
      }

      var4 = var1.getLocalPlanPath();
      if (var4 != null) {
         this.helper.setPlan(new File(var4));
         this.noplan = false;
      }

      var4 = var1.getLocalPlanDir();
      if (var4 != null) {
         this.helper.setPlandir(new File(var4));
      } else {
         this.helper.setPlandir(this.getOrCreatePlanDir());
      }

      DeploymentPlanBean var5 = var1.getDeploymentPlanDescriptor();
      this.helper.setPlanBean(var5);
      if (var5 != null) {
         var5.setConfigRoot(this.helper.getPlandir().getPath());
      }

      this.helper.enableLibraryMerge();
      if (this.enableLightWeightView) {
         this.helper.setLightWeightAppName(var1.getApplicationName());
      }

      this.helper.initializeConfiguration(var1);
   }

   protected void initDM(String var1, String var2) throws DeploymentManagerCreationException {
      if (var1 != null && !this.isadmin) {
         this.myDM = SessionHelper.getRemoteDeploymentManager(this.host, this.port, var1, var2);
      } else {
         this.myDM = SessionHelper.getDeploymentManager(this.host, this.port);
         if (!this.isadmin) {
            this.myDM.enableFileUploads();
         }
      }

   }

   public ProgressObject saveAndUpdate() throws ConfigurationException, IOException {
      File var1 = this.getNewPlanFile();
      TargetModuleID[] var2 = this.myDM.getModules(this.mbean);
      if (this.debug) {
         Debug.say("Updating " + this.mbean.getName() + " with new plan: " + var1.getPath());
      }

      String[] var3 = this.helper.getChangedDescriptors();
      ProgressObject var4 = this.myDM.update(var2, var1, var3, (DeploymentOptions)null);
      this.waitForTask(var4);
      DeploymentStatus var5 = var4.getDeploymentStatus();
      if (var5.getState() != StateType.COMPLETED) {
         this.restoreOrigPlan(var1);
         SPIDeployerLogger.logRestorePlan(var5.getMessage());
      }

      if (!this.debug && this.planBackup != null) {
         this.planBackup.delete();
      }

      return var4;
   }

   private void waitForTask(ProgressObject var1) {
      while(var1.getDeploymentStatus().isRunning()) {
         try {
            Thread.sleep(500L);
         } catch (InterruptedException var3) {
         }
      }

   }

   private void saveBadPlan(File var1) {
      try {
         FileUtils.copy(var1, new File(var1.getParentFile(), "_wls_bad_plan.xml"));
      } catch (IOException var3) {
         Debug.say("Unable to copy the bad plan to a backup location : " + var3);
      }

   }

   private void restoreOrigPlan(File var1) throws IOException {
      if (this.planBackup != null) {
         if (this.debug) {
            this.saveBadPlan(var1);
         }

         try {
            FileUtils.copy(this.planBackup, var1);
            this.mbean.setDeploymentPlanDescriptor((DeploymentPlanBean)null);
            DeploymentPlanBean var2 = this.mbean.getDeploymentPlanDescriptor();
            this.helper.setPlanBean(var2);
            var1 = null;
            this.planBackup = null;
         } catch (IOException var3) {
            throw new IOException(SPIDeployerLogger.restorePlanFailure(var3.getMessage(), this.planBackup.getPath()));
         }
      }
   }

   protected File getNewPlanFile() throws IOException, ConfigurationException {
      File var1 = this.getOrCreatePlanDir();
      File var2 = new File(var1, this.helper.getNewPlanName());
      if (this.helper.getPlan() != null && var2.getPath().equals(this.helper.getPlan().getPath())) {
         this.planBackup = new File(var1, "_wls_plan_bk.xml");

         try {
            FileUtils.copy(this.helper.getPlan(), this.planBackup);
         } catch (IOException var4) {
            throw new IllegalArgumentException(SPIDeployerLogger.backupPlanError(var4.getMessage(), this.planBackup.getPath()));
         }
      }

      var2 = this.helper.savePlan(var1, this.helper.getNewPlanName());
      return var2;
   }

   private File getOrCreatePlanDir() throws IOException {
      if (this.helper.getPlandir() != null) {
         return this.helper.getPlandir();
      } else {
         File var1;
         if (this.isadmin) {
            if (this.noplan) {
               var1 = new File(DomainDir.getDeploymentsDir());
               if (this.debug) {
                  Debug.say("deployments dir: " + var1);
               }

               var1 = new File(var1, this.mbean.getName());
               var1 = new File(var1, "plan");
               var1.mkdirs();
            } else {
               var1 = this.helper.getPlan().getParentFile();
            }

            if (this.debug) {
               Debug.say("Using plan dir: " + var1);
            }
         } else {
            var1 = new File(DomainDir.getTempDirForServer(this.server));
         }

         return var1;
      }
   }

   public void close() {
      this.helper.close();
      this.myDM.release();
   }
}
