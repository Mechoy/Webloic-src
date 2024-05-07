package weblogic.management.mbeans.custom;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.enterprise.deploy.shared.ModuleType;
import javax.management.InvalidAttributeValueException;
import weblogic.application.ModuleListener;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.deploy.internal.targetserver.DeployHelper;
import weblogic.j2ee.DeploymentInfo;
import weblogic.j2ee.J2EEUtils;
import weblogic.logging.Loggable;
import weblogic.management.DistributedManagementException;
import weblogic.management.DomainDir;
import weblogic.management.ManagementLogger;
import weblogic.management.bootstrap.BootStrap;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ComponentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.configuration.VirtualHostMBean;
import weblogic.management.deploy.ApplicationsDirPoller;
import weblogic.management.deploy.DeploymentCompatibilityEvent;
import weblogic.management.deploy.DeploymentCompatibilityEventHandler;
import weblogic.management.deploy.DeploymentCompatibilityEventManager;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;
import weblogic.management.utils.AppDeploymentHelper;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public final class Application extends ConfigurationMBeanCustomizer implements DeploymentCompatibilityEventManager {
   private static final long serialVersionUID = -2233500325718755084L;
   private static final DebugCategory debugging = Debug.getCategory("weblogic.deployment");
   private static final DebugCategory debugnotif = Debug.getCategory("weblogic.deployment.notif");
   private transient String fullPath = null;
   private transient String deploymentType;
   private transient boolean delegationEnabled = false;
   private transient AppDeploymentMBean delegate = null;
   private String stagingMode = null;
   private boolean deployed = false;
   private boolean internal = false;
   private int internalType = 6;
   String path = null;
   String notes = null;
   private final CopyOnWriteArraySet handlers = new CopyOnWriteArraySet();

   public Application(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   private String lookupPath() {
      return ((ApplicationMBean)this.getMbean()).getPath();
   }

   public synchronized void unstageTargets(String[] var1) {
      if (debugging.isEnabled()) {
         Debug.say("Unstaging Targets");
      }

      if (var1 != null) {
         String[] var2 = this.getApplication().getStagedTargets();
         if (var2 != null && var2.length != 0) {
            TreeSet var3 = new TreeSet();

            int var4;
            for(var4 = 0; var4 < var2.length; ++var4) {
               var3.add(var2[var4]);
            }

            if (debugging.isEnabled()) {
               Debug.say("Currently Staged Targets " + var3);
            }

            for(var4 = 0; var4 < var1.length; ++var4) {
               String var5 = var1[var4];
               if (!var3.remove(var5)) {
                  ClusterMBean var6 = ((DomainMBean)((DomainMBean)this.getMbean().getParent())).lookupCluster(var5);
                  if (var6 != null) {
                     if (debugging.isEnabled()) {
                        Debug.say("Unstaging Cluster :" + var5);
                     }

                     ServerMBean[] var12 = var6.getServers();
                     if (var12 != null) {
                        for(int var13 = 0; var13 < var12.length; ++var13) {
                           String var14 = var12[var13].getName();
                           var3.remove(var14);
                           if (debugging.isEnabled()) {
                              Debug.say("Unstaged Clustered :" + var14);
                           }
                        }
                     }
                  } else {
                     VirtualHostMBean var7 = ((DomainMBean)((DomainMBean)this.getMbean().getParent())).lookupVirtualHost(var5);
                     if (var7 != null) {
                        if (debugging.isEnabled()) {
                           Debug.say("Unstaging VirtualHost :" + var5);
                        }

                        TargetMBean[] var8 = var7.getTargets();

                        for(int var9 = 0; var9 < var8.length; ++var9) {
                           String var10 = var8[var9].getName();
                           var3.remove(var10);
                           if (debugging.isEnabled()) {
                              Debug.say("Unstaged Virtual Target : " + var10);
                           }
                        }
                     }
                  }
               }
            }

            String[] var11 = new String[var3.size()];
            var11 = (String[])((String[])var3.toArray(var11));
            if (debugging.isEnabled()) {
               Debug.say("Remaining Staged Targets" + var3);
            }

            this.getApplication().setStagedTargets(var11);
         }
      }
   }

   public boolean useStagingDirectory(String var1) {
      throw new UnsupportedOperationException();
   }

   public boolean stagingEnabled(String var1) {
      return "stage".equals(this.getStagingMode(var1));
   }

   public boolean staged(String var1) {
      boolean var2 = true;
      if (this.stagingEnabled(var1)) {
         List var3 = Arrays.asList((Object[])this.getApplication().getStagedTargets());
         if (var3 == null || !var3.contains(var1)) {
            var2 = false;
         }
      }

      if (debugging.isEnabled()) {
         Debug.say("isStaged  " + var1 + " = " + var2);
      }

      return var2;
   }

   public void sendAppLevelNotification(String var1, String var2, String var3) {
      DeploymentCompatibilityEvent var4 = new DeploymentCompatibilityEvent("weblogic.deployment.application", var1, this.getName(), var2, (String)null, (String)null, (String)null, (String)null, var3);
      this.sendNotification(var4);
   }

   public void sendModuleNotification(String var1, String var2, String var3, String var4, String var5, String var6, long var7) {
      var4 = this.convertStateNamesTo81(var4);
      var5 = this.convertStateNamesTo81(var5);
      DeploymentCompatibilityEvent var9 = new DeploymentCompatibilityEvent("weblogic.deployment.application.module", var1, this.getName(), (String)null, var3, var2, var4, var5, var6);
      this.sendNotification(var9);
   }

   private String convertStateNamesTo81(String var1) {
      if (var1.equals(ModuleListener.STATE_PREPARED.toString())) {
         return "unprepared".toString();
      } else if (var1.equals(ModuleListener.STATE_ADMIN.toString())) {
         return "prepared".toString();
      } else if (var1.equals(ModuleListener.STATE_ACTIVE.toString())) {
         return "active".toString();
      } else {
         if (debugnotif.isEnabled()) {
            Debug.say("Illegal state " + var1);
         }

         return null;
      }
   }

   private String typeAsString(int var1) {
      switch (var1) {
         case 0:
            return DeploymentInfo.TYPE_EAR;
         case 1:
            return DeploymentInfo.TYPE_COMPONENT;
         case 2:
            return DeploymentInfo.TYPE_EXPLODED_EAR;
         case 3:
            return DeploymentInfo.TYPE_EXPLODED_COMPONENT;
         default:
            return DeploymentInfo.TYPE_UNKNOWN;
      }
   }

   private String getTypeFromNew(String var1) {
      if (var1 == null) {
         return DeploymentInfo.TYPE_UNKNOWN;
      } else if (var1.equals(WebLogicModuleType.UNKNOWN.toString())) {
         return DeploymentInfo.TYPE_UNKNOWN;
      } else {
         return var1.equals(ModuleType.EAR.toString()) ? DeploymentInfo.TYPE_EAR : DeploymentInfo.TYPE_COMPONENT;
      }
   }

   private int stringAsType(String var1) {
      if (var1.equals(DeploymentInfo.TYPE_EAR)) {
         return 0;
      } else if (var1.equals(DeploymentInfo.TYPE_EXPLODED_EAR)) {
         return 2;
      } else if (var1.equals(DeploymentInfo.TYPE_COMPONENT)) {
         return 1;
      } else {
         return var1.equals(DeploymentInfo.TYPE_EXPLODED_COMPONENT) ? 3 : 4;
      }
   }

   public int findExplodedEar() {
      int var1 = 0;
      if (this.isAdmin()) {
         File var2 = new File(this.getFullPath());
         var1 = var2.isDirectory() ? 1 : 0;
      }

      return var1;
   }

   public boolean isInternalApp() {
      return this.getAppDeployment() == null ? this.internal : this.getAppDeployment().isInternalApp();
   }

   public void setInternalApp(boolean var1) {
      if (this.getAppDeployment() == null) {
         this.internal = var1;
      } else {
         this.getAppDeployment().setInternalApp(var1);
      }

   }

   public void setStagingMode(String var1) {
      this.stagingMode = var1;
   }

   public String getStagingMode() {
      if (!this.isDelegationEnabled()) {
         return this.stagingMode;
      } else {
         return this.getAppDeployment() != null ? this.getAppDeployment().getStagingMode() : null;
      }
   }

   public boolean isDDEditingEnabled() {
      return false;
   }

   public String getDDEditingDisabledReason() {
      return "Not supported anymore.";
   }

   public String getFullPath() {
      if (this.fullPath != null) {
         return this.fullPath;
      } else {
         File var1;
         try {
            var1 = BootStrap.apply(this.lookupPath());
         } catch (InvalidAttributeValueException var6) {
            throw new AssertionError(var6);
         }

         int var3 = this.getInternalType();
         if (var3 == 1 || var3 == 3 || var3 == 5) {
            ComponentMBean[] var4 = this.getApplication().getComponents();
            if (var4 != null && var4.length > 0) {
               String var2 = var4[0].getURI();
               if (var2 != null && var2.length() > 0) {
                  var1 = new File(var1, var2);
               } else {
                  DeployerRuntimeLogger.logNoURI(this.getMbean().getName(), var4[0].getName());
                  var1 = null;
               }
            } else {
               DeployerRuntimeLogger.logNoModules(this.getMbean().getName());
               var1 = null;
            }
         }

         if (var1 != null) {
            try {
               this.fullPath = var1.getCanonicalPath();
            } catch (IOException var5) {
               this.fullPath = var1.getAbsolutePath();
            }
         }

         return this.fullPath;
      }
   }

   public int getInternalType() {
      if (this.internalType == 6) {
         if (this.deploymentType != null && this.deploymentType != ApplicationMBean.TYPE_UNKNOWN) {
            this.internalType = this.stringAsType(this.deploymentType);
         } else {
            try {
               this.internalType = J2EEUtils.getDeploymentCategory(this.getApplication());
               this.deploymentType = this.typeAsString(this.internalType);
            } catch (IOException var3) {
               this.internalType = 4;
               Loggable var2 = DeployerRuntimeLogger.logUnknownAppTypeLoggable(this.getMbean().getName(), this.lookupPath());
               var2.log();
            }
         }
      }

      return this.internalType;
   }

   public String getDeploymentType() {
      if (!this.isDelegationEnabled()) {
         if (this.deploymentType == null) {
            this.deploymentType = this.typeAsString(this.getInternalType());
         }

         return this.deploymentType;
      } else {
         AppDeploymentMBean var1 = this.getAppDeployment();
         if (var1 != null) {
            String var2 = var1.getModuleType();
            if (var2 != null) {
               return this.getTypeFromNew(var2);
            }
         }

         return DeploymentInfo.TYPE_UNKNOWN;
      }
   }

   public void setDeploymentType(String var1) {
      this.deploymentType = var1;
   }

   public void setDeployed(boolean var1) {
      this.deployed = var1;
   }

   public boolean isDeployed() {
      AppDeploymentMBean var1 = this.getAppDeployment();
      if (var1 == null) {
         return this.deployed;
      } else {
         TargetMBean[] var2 = var1.getTargets();
         SubDeploymentMBean[] var3 = var1.getSubDeployments();
         return var2 != null && var2.length != 0 || var3 != null && var3.length != 0;
      }
   }

   private String getStagingMode(String var1) {
      String var2 = this.getAppDeployment().getStagingMode();
      if (var2 != null && var2.length() != 0) {
         if (debugging.isEnabled()) {
            Debug.say(" Value of application's staging mode is " + var2);
         }
      } else {
         var2 = DeployHelper.getServerStagingMode(var1);
         if (debugging.isEnabled()) {
            Debug.say("Using " + var2 + " from serverMBean " + var1);
         }
      }

      return var2;
   }

   public boolean isEar() {
      int var1 = this.getInternalType();
      return var1 == 0 || var1 == 2;
   }

   boolean isExplodedEar() {
      return this.getInternalType() == 2;
   }

   public boolean isLoadedFromAppDir() {
      return !this.isAdmin() ? false : ApplicationsDirPoller.isInAppsDir(new File(DomainDir.getAppPollerDir()), this.lookupPath());
   }

   public void refreshDDsIfNeeded(String[] var1, String[] var2) {
      HashSet var3 = null;
      if (debugging.isEnabled()) {
         Debug.say("app.refreshDDsIfNeeded " + var2);
      }

      if (!this.isConfig()) {
         if (var2 != null && var2.length > 0) {
            var3 = new HashSet(Arrays.asList((Object[])var2));
         }

         if (debugging.isEnabled()) {
            Debug.say("app.refreshDDsIfNeeded " + var3);
         }

         ComponentMBean[] var4 = this.getApplication().getComponents();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            if (var3 == null || var3.contains(var4[var5].getName())) {
               var4[var5].refreshDDsIfNeeded(var1);
            }
         }

      }
   }

   private boolean containsAppDD(String[] var1) {
      int var2 = 0;

      while(true) {
         if (var1 != null && var2 < var1.length) {
            if (!var1[var2].endsWith("META-INF/application.xml") && !var1[var2].endsWith("META-INF/weblogic-application.xml")) {
               ++var2;
               continue;
            }

            return true;
         }

         return false;
      }
   }

   public ComponentMBean[] getComponents() {
      HashSet var1 = new HashSet();
      ApplicationMBean var2 = (ApplicationMBean)this.getMbean();
      this.buildComponentSet(var2.getEJBComponents(), var1);
      this.buildComponentSet(var2.getWebAppComponents(), var1);
      this.buildComponentSet(var2.getWebServiceComponents(), var1);
      this.buildComponentSet(var2.getConnectorComponents(), var1);
      this.buildComponentSet(var2.getJDBCPoolComponents(), var1);
      return (ComponentMBean[])((ComponentMBean[])var1.toArray(new ComponentMBean[var1.size()]));
   }

   private void buildComponentSet(ComponentMBean[] var1, Set var2) {
      if (var1 != null && var1.length > 0) {
         List var3 = Arrays.asList((Object[])var1);
         var2.addAll(var3);
      }

   }

   private ApplicationMBean getApplication() {
      return (ApplicationMBean)this.getMbean();
   }

   public AppDeploymentMBean returnDeployableUnit() {
      return this.getAppDeployment();
   }

   public void setDelegationEnabled(boolean var1) {
      this.delegationEnabled = var1;
   }

   public boolean isDelegationEnabled() {
      return this.delegationEnabled;
   }

   public String getPath() {
      if (this.delegate == null) {
         this.getAppDeployment();
      }

      return this.delegate != null ? this.delegate.getSourcePath() : this.path;
   }

   public void setPath(String var1) {
      if (this.delegate == null) {
         this.getAppDeployment();
      }

      if (this.delegate != null) {
         this.delegate.setSourcePath(var1);
         this.delegate.setModuleType(WebLogicModuleType.getFileModuleTypeAsString(new File(this.delegate.getLocalSourcePath())));
      } else {
         this.path = var1;
      }

   }

   public String getNotes() {
      if (this.delegate == null) {
         this.getAppDeployment();
      }

      return this.delegate != null ? this.delegate.getNotes() : this.notes;
   }

   public void setNotes(String var1) throws InvalidAttributeValueException, DistributedManagementException {
      if (this.delegate == null) {
         this.getAppDeployment();
      }

      if (this.delegate != null) {
         this.delegate.setNotes(var1);
      } else {
         this.notes = var1;
      }

   }

   public AppDeploymentMBean getAppDeployment() {
      if (!this.delegationEnabled) {
         return null;
      } else {
         ApplicationMBean var1 = (ApplicationMBean)this.getMbean();
         DomainMBean var2 = (DomainMBean)var1.getParent();
         if (var2 == null) {
            return null;
         } else {
            this.delegate = AppDeploymentHelper.lookupAppOrLib(var1.getName(), var2);
            return this.delegate;
         }
      }
   }

   public void addHandler(DeploymentCompatibilityEventHandler var1) {
      this.handlers.add(var1);
   }

   private void sendNotification(DeploymentCompatibilityEvent var1) {
      if (debugnotif.isEnabled()) {
         String var2 = this.getMbean().getObjectName().getType().endsWith("Config") ? "LOCAL" : "REMOTE";
         Debug.say(var2 + " " + var1);
      }

      Iterator var5 = this.handlers.iterator();

      while(var5.hasNext()) {
         try {
            ((DeploymentCompatibilityEventHandler)var5.next()).handleEvent(var1);
         } catch (Throwable var4) {
            ManagementLogger.logExceptionInCustomizer(var4);
         }
      }

   }

   public boolean isTwoPhase() {
      return true;
   }
}
