package weblogic.management.mbeans.custom;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import weblogic.application.utils.TargetUtils;
import weblogic.j2ee.J2EEApplicationService;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.ComponentMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.deploy.internal.AppRuntimeStateManager;
import weblogic.management.deploy.internal.MBeanConverter;
import weblogic.management.descriptors.TopLevelDescriptorMBean;
import weblogic.management.provider.DomainAccess;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;
import weblogic.management.runtime.ServerLifeCycleRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.t3.srvr.T3Srvr;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.FileUtils;
import weblogic.utils.jars.VirtualJarFile;

public class Component extends ConfigurationMBeanCustomizer {
   private static final long serialVersionUID = -2559563505270982380L;
   private static final DebugCategory DEBUGGING = Debug.getCategory("weblogic.deployment");
   protected static final boolean DEBUG;
   private transient File compFile = null;
   private transient File tmpdir = null;
   private transient File altddFile = null;
   private boolean isArchivedEar;
   private String altDDURI = null;
   private transient ApplicationMBean applicationMBean;
   private transient AppDeploymentMBean appDeployment = null;
   private static final AuthenticatedSubject kernelId;
   TargetMBean[] targets = new TargetMBean[0];

   public Component(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   protected boolean isServerRunning() {
      T3Srvr var1 = T3Srvr.getT3Srvr();
      if (var1 != null) {
         return var1.getRunState() == 2;
      } else {
         return false;
      }
   }

   protected boolean isPhysicalModule() {
      throw new UnsupportedOperationException("Abstract method not supported in Customizers for new bean model");
   }

   protected TopLevelDescriptorMBean readDescriptor(VirtualJarFile var1, File var2, String var3) throws Exception {
      throw new UnsupportedOperationException("Abstract method not supported in Customizers for new bean model");
   }

   public boolean activated(TargetMBean var1) {
      return Arrays.asList((Object[])this.getComponentMBean().getActivatedTargets()).contains(var1);
   }

   private void removeTempModule() {
      if (this.isArchivedEar) {
         if (this.altddFile != null) {
            this.altddFile.delete();
         }

         String[] var1 = this.tmpdir.list();
         if (var1 == null) {
            return;
         }

         if (var1.length == 1) {
            FileUtils.remove(this.tmpdir);
         } else {
            FileUtils.remove(this.compFile);
         }
      }

   }

   protected boolean containsDD(String[] var1, String var2) {
      for(int var3 = 0; var1 != null && var3 < var1.length; ++var3) {
         if (var1[var3].endsWith(var2)) {
            return true;
         }
      }

      return false;
   }

   private void initTempDir() throws IOException {
      if (this.tmpdir == null) {
         this.tmpdir = new File(J2EEApplicationService.getTempDir().getCanonicalPath() + File.separator + "dde" + File.separator + this.getApplication().getName());
      }

      if (!this.tmpdir.exists() && !this.tmpdir.mkdirs() && !this.tmpdir.exists()) {
         throw new IOException("Failed to create tmpdir : " + this.tmpdir);
      }
   }

   protected ComponentMBean getComponentMBean() {
      return (ComponentMBean)this.getMbean();
   }

   public ApplicationMBean getApplication() {
      if (this.applicationMBean != null) {
         return this.applicationMBean;
      } else {
         this.setApplication((ApplicationMBean)this.getMbean().getParent());
         return this.applicationMBean;
      }
   }

   public void setApplication(ApplicationMBean var1) {
      this.applicationMBean = var1;
      if (var1 == null) {
         this.appDeployment = null;
      } else {
         this.appDeployment = var1.getAppDeployment();
      }
   }

   protected AppDeploymentMBean getAppDeployment() {
      if (this.appDeployment == null) {
         this.initFromParent();
      }

      return this.appDeployment;
   }

   private void initFromParent() {
      this.applicationMBean = (ApplicationMBean)this.getMbean().getParent();
      this.setApplication(this.applicationMBean);
   }

   public void setTargets(TargetMBean[] var1) {
      if (this.getAppDeployment() != null) {
         MBeanConverter.setTargetsForComponent(this.getAppDeployment(), this.getComponentMBean(), var1);
      }

      this.putValueNotify("Targets", var1);
      this.targets = var1;
   }

   public TargetMBean[] getTargets() {
      return this.getAppDeployment() != null ? MBeanConverter.getTargetsForComponent(this.getAppDeployment(), this.getComponentMBean()) : this.targets;
   }

   public TargetMBean[] getActivatedTargets() {
      DomainAccess var1 = ManagementService.getDomainAccess(kernelId);
      AppRuntimeStateManager var2 = AppRuntimeStateManager.getManager();
      TargetMBean[] var3 = this.getTargets();
      HashSet var4 = new HashSet();

      for(int var5 = 0; var5 < var3.length; ++var5) {
         String var6 = var3[var5].getName();
         if (this.targetAlive(var3[var5], var1)) {
            String var7 = var2.getCurrentState(this.getApplication().getName(), this.getName(), var6);
            if (this.compActive(var7)) {
               var4.add(var3[var5]);
            }
         }
      }

      return (TargetMBean[])((TargetMBean[])var4.toArray(new TargetMBean[0]));
   }

   private boolean targetAlive(TargetMBean var1, DomainAccess var2) {
      if (var2 != null) {
         Set var3 = var1.getServerNames();
         Iterator var4 = var3.iterator();

         ServerLifeCycleRuntimeMBean var6;
         do {
            if (!var4.hasNext()) {
               return false;
            }

            String var5 = (String)var4.next();
            var6 = var2.lookupServerLifecycleRuntime(var5);
         } while(!this.serverAlive(var6));

         return true;
      } else {
         return TargetUtils.isDeployedLocally(new TargetMBean[]{var1});
      }
   }

   private boolean serverAlive(ServerLifeCycleRuntimeMBean var1) {
      return var1 != null && ("ADMIN".equals(var1.getState()) || "RUNNING".equals(var1.getState()));
   }

   private boolean compActive(String var1) {
      return "STATE_ADMIN".equals(var1) || "STATE_ACTIVE".equals(var1) || "STATE_UPDATE_PENDING".equals(var1);
   }

   static {
      DEBUG = DEBUGGING.isEnabled();
      kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }
}
