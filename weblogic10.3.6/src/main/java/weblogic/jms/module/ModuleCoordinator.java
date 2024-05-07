package weblogic.jms.module;

import java.security.AccessController;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import weblogic.application.AdminModeCompletionBarrier;
import weblogic.application.ApplicationContext;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.UpdateListener;
import weblogic.cluster.migration.Migratable;
import weblogic.cluster.migration.MigrationException;
import weblogic.cluster.migration.MigrationManager;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorUpdateEvent;
import weblogic.descriptor.DescriptorUpdateListener;
import weblogic.descriptor.DescriptorUpdateRejectedException;
import weblogic.jms.JMSExceptionLogger;
import weblogic.jms.JMSLogger;
import weblogic.jms.JMSService;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSTargetsListener;
import weblogic.jms.common.ModuleName;
import weblogic.jms.saf.SAFService;
import weblogic.management.ManagementException;
import weblogic.management.WebLogicMBean;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSInteropModuleMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.management.configuration.TargetInfoMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.management.utils.GenericBeanListener;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.classloaders.GenericClassLoader;

public abstract class ModuleCoordinator implements Module, UpdateListener, DescriptorUpdateListener {
   public TargetingHelper targeter;
   private static final String DOT = ".";
   protected static final int MOD_TYPE_DEPLOYMENT = 0;
   protected static final int MOD_TYPE_SYSTEM = 1;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private String earModuleName;
   private String uri;
   private ApplicationContextInternal appCtx;
   protected ModuleName moduleName;
   private JMSComponent jmsComponent;
   private int moduleType;
   private DeploymentListener deploymentListener = new DeploymentListener();
   private GenericBeanListener targetingAdditionsListener;
   private GenericBeanListener earBasicListener;
   private JMSTargetsListener jmsListener = new JMSTargetsListenerImpl();
   private JMSTargetsListener safListener = new JMSTargetsListenerImpl();
   private HashMap targetingListenees = new HashMap();
   private HashMap registeredMigratableTargets = new HashMap();
   private boolean hasUpdate = false;
   private Object update;
   private boolean hasCalledPrepareUpdate = false;
   private String hasGottenModulePrepareUpdate = null;
   private boolean hasMigratableUpdate = false;
   private DomainMBean hasMigratableUpdateDomain = null;
   private boolean hasCalledMigratablePrepareUpdate = false;
   private boolean isActive = false;
   private static final HashMap deploymentAdditions = new HashMap();
   private ASMActivateHandler asmActivateHandler = null;
   private ASMDeactivateHandler asmDeactivateHandler = null;

   protected ModuleCoordinator(String var1, String var2) {
      this.earModuleName = var1;
      this.uri = var2;
   }

   protected abstract DescriptorBean getModuleDescriptor();

   protected abstract void initializeModule(ApplicationContextInternal var1, DomainMBean var2) throws ModuleException;

   protected abstract void prepare(DomainMBean var1) throws ModuleException;

   protected abstract void activate(DomainMBean var1) throws ModuleException;

   protected abstract void deactivate(DomainMBean var1) throws ModuleException;

   protected abstract void unprepare(DomainMBean var1) throws ModuleException;

   protected abstract void destroy(DomainMBean var1) throws ModuleException;

   protected abstract void remove(DomainMBean var1) throws ModuleException;

   protected abstract Object prepareUpdate(DomainMBean var1) throws ModuleException;

   protected abstract void activateUpdate(DomainMBean var1, Object var2) throws ModuleException;

   protected abstract void rollbackUpdate(DomainMBean var1, Object var2);

   protected int getModuleType() {
      return this.moduleType;
   }

   public String getId() {
      return this.uri;
   }

   public ComponentRuntimeMBean[] getComponentRuntimeMBeans() {
      return new ComponentRuntimeMBean[]{this.jmsComponent};
   }

   public DescriptorBean[] getDescriptors() {
      DescriptorBean[] var1 = new DescriptorBean[]{this.getModuleDescriptor()};
      return var1;
   }

   public GenericClassLoader init(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      this.internalInit((ApplicationContextInternal)var1, var3);
      return var2;
   }

   public void initUsingLoader(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      this.internalInit((ApplicationContextInternal)var1, var3);
   }

   public void prepare() throws ModuleException {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("INFO: module prepare called in " + this.moduleName);
      }

      DomainMBean var1 = this.getDomainFromAppCtx();
      this.prepare(var1);
   }

   public void activate() throws ModuleException {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("INFO: module activate called in " + this.moduleName);
      }

      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      this.activate(var1);
      TargetInfoMBean var2 = this.getTargetingBean(var1);
      BasicDeploymentMBean var3;
      int var5;
      if (var2 != null && var2 instanceof BasicDeploymentMBean) {
         var3 = (BasicDeploymentMBean)var2;
         SubDeploymentMBean[] var4 = var3.getSubDeployments();

         for(var5 = 0; var5 < var4.length; ++var5) {
            SubDeploymentMBean var6 = var4[var5];
            var6.addBeanUpdateListener(this.deploymentListener);
            this.targetingListenees.put(var6.getName(), var6);
         }

         this.targetingAdditionsListener = new GenericBeanListener(var3, this, (Map)null, deploymentAdditions, true);
      } else {
         if (var2 != null) {
            this.activateSubDeploymentListener((SubDeploymentMBean)var2);
            this.targetingAdditionsListener = new GenericBeanListener(var2, this, (Map)null, deploymentAdditions, true);
         }

         var3 = this.getBasicDeployment(var1);
         this.earBasicListener = new GenericBeanListener(var3, new EARSubDeploymentListener(), (Map)null, deploymentAdditions, true);
      }

      Descriptor var9 = var1.getDescriptor();
      var9.addUpdateListener(this);
      this.asmActivateHandler = new ASMActivateHandler();
      this.asmDeactivateHandler = new ASMDeactivateHandler();
      MigratableTargetMBean[] var10 = var1.getMigratableTargets();

      for(var5 = 0; var5 < var10.length; ++var5) {
         if (this.registeredMigratableTargets.get(var10[var5].getName()) == null) {
            try {
               if (JMSDebug.JMSModule.isDebugEnabled()) {
                  JMSDebug.JMSModule.debug("INFO: Registering JMS module =" + this.getName() + " for MT =" + var10[var5].getName());
               }

               MigrationManager.singleton().register(this.asmActivateHandler, var10[var5]);
               MigrationManager.singleton().register(this.asmDeactivateHandler, var10[var5]);
               this.registeredMigratableTargets.put(var10[var5].getName(), var10[var5]);
            } catch (MigrationException var8) {
               throw new ModuleException(var8);
            }
         }
      }

      JMSService.getJMSService().addJMSServerListener(this.jmsListener);
      SAFService.getSAFService().getDeployer().addSAFAgentListener(this.safListener);
      this.isActive = true;
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("INFO: module activate finished in " + this.moduleName);
      }

   }

   private void activateSubDeploymentListener(SubDeploymentMBean var1) {
      SubDeploymentMBean[] var2 = var1.getSubDeployments();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         SubDeploymentMBean var4 = var2[var3];
         SubDeploymentMBean var5 = var2[var3];
         var5.addBeanUpdateListener(this.deploymentListener);
         this.targetingListenees.put(var4.getName(), var4);
      }

   }

   public void start() throws ModuleException {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("JMSModule: module start called in " + this.moduleName);
      }

   }

   public void deactivate() throws ModuleException {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("INFO: module deactivate called in " + this.moduleName);
      }

      this.isActive = false;
      JMSService.getJMSService().removeJMSServerListener(this.jmsListener);
      SAFService.getSAFService().getDeployer().removeSAFAgentListener(this.safListener);
      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      Descriptor var2 = var1.getDescriptor();
      var2.removeUpdateListener(this);
      Iterator var3 = this.registeredMigratableTargets.values().iterator();

      while(var3.hasNext()) {
         MigratableTargetMBean var4 = (MigratableTargetMBean)var3.next();

         try {
            if (JMSDebug.JMSModule.isDebugEnabled()) {
               JMSDebug.JMSModule.debug("INFO: Unregistering JMS module =" + this.getName() + " for MT =" + var4.getName());
            }

            MigrationManager.singleton().unregister(this.asmActivateHandler, var4);
            MigrationManager.singleton().unregister(this.asmDeactivateHandler, var4);
            var3.remove();
         } catch (MigrationException var6) {
            throw new ModuleException(var6);
         }
      }

      if (this.earBasicListener != null) {
         this.earBasicListener.close();
         this.earBasicListener = null;
      }

      this.deactivateSubDeploymentListener();
      Object var7;
      if (this.hasCalledPrepareUpdate) {
         this.hasUpdate = false;
         var7 = this.update;
         this.update = null;
         this.hasCalledPrepareUpdate = false;
         this.hasGottenModulePrepareUpdate = null;
         this.rollbackUpdate(var1, var7);
      } else if (this.hasCalledMigratablePrepareUpdate) {
         this.hasMigratableUpdate = false;
         var7 = this.update;
         this.update = null;
         this.hasCalledMigratablePrepareUpdate = false;
         this.hasGottenModulePrepareUpdate = null;
         this.rollbackUpdate(var1, var7);
      }

      this.deactivate(var1);
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("INFO: module deactivate finished in " + this.moduleName);
      }

   }

   public void deactivateSubDeploymentListener() {
      if (this.targetingAdditionsListener != null) {
         this.targetingAdditionsListener.close();
         this.targetingAdditionsListener = null;
      }

      Iterator var1 = this.targetingListenees.keySet().iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         DescriptorBean var3 = (DescriptorBean)this.targetingListenees.get(var2);
         var3.removeBeanUpdateListener(this.deploymentListener);
      }

      this.targetingListenees.clear();
   }

   public void unprepare() throws ModuleException {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("INFO: module unprepare called in " + this.moduleName);
      }

      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      this.unprepare(var1);
   }

   public void destroy(UpdateListener.Registration var1) throws ModuleException {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("INFO: module destroy called in " + this.moduleName);
      }

      var1.removeUpdateListener(this);
      this.closeJMSComponent();
      DomainMBean var2 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      this.destroy(var2);
   }

   public void remove() throws ModuleException {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("INFO: module remove called in " + this.moduleName);
      }

      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      this.remove(var1);
   }

   public void adminToProduction() {
   }

   public void gracefulProductionToAdmin(AdminModeCompletionBarrier var1) throws ModuleException {
   }

   public void forceProductionToAdmin() throws ModuleException {
   }

   public boolean acceptURI(String var1) {
      if (var1 != null && !this.uri.equals(var1) && !".".equals(var1)) {
         if (this.targetingListenees.containsKey(var1)) {
            return true;
         } else if (this.earModuleName != null && var1.startsWith(this.earModuleName)) {
            return true;
         } else {
            DomainMBean var2 = this.appCtx.getProposedDomain();
            if (var2 == null) {
               return false;
            } else {
               BasicDeploymentMBean var3 = this.getBasicDeployment(var2);
               SubDeploymentMBean[] var4 = var3.getSubDeployments();

               for(int var5 = 0; var5 < var4.length; ++var5) {
                  SubDeploymentMBean var6 = var4[var5];
                  if (this.earModuleName == null) {
                     if (var6.getName().equals(var1)) {
                        return true;
                     }
                  } else if (this.earModuleName.equals(var6.getName())) {
                     SubDeploymentMBean[] var7 = var6.getSubDeployments();

                     for(int var8 = 0; var8 < var7.length; ++var8) {
                        SubDeploymentMBean var9 = var7[var8];
                        if (var9.getName().equals(var1)) {
                           return true;
                        }
                     }
                  }
               }

               if (JMSDebug.JMSModule.isDebugEnabled()) {
                  JMSDebug.JMSModule.debug("INFO: Saying no because no criteria matched " + this.moduleName + " with proposed uri=\"" + var1 + "\" local uri=\"" + this.uri + "\" ear name=\"" + this.earModuleName + "\"");
               }

               return false;
            }
         }
      } else {
         return true;
      }
   }

   public void prepareUpdate(String var1) throws ModuleException {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("INFO: module prepareUpdate called in " + this.moduleName + " with uri " + var1 + " globalURI=" + this.uri);
      }

      if (!this.isActive) {
         StackTraceUtils.dumpStack();
         throw new AssertionError("We got a prepareUpdate call, but we are not in the ACTIVE state in module " + this.moduleName);
      } else if (this.hasCalledPrepareUpdate) {
         if (this.hasGottenModulePrepareUpdate == null) {
            this.hasGottenModulePrepareUpdate = var1;
         }

         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("INFO: module prepareUpdate exits, work already done for " + this.moduleName);
         }

      } else {
         this.update = this.prepareUpdate(this.getProposedDomain());
         this.hasUpdate = true;
         this.hasCalledPrepareUpdate = true;
         this.hasGottenModulePrepareUpdate = var1;
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("INFO: module prepareUpdate exits normally in " + this.moduleName);
         }

      }
   }

   public void activateUpdate(String var1) throws ModuleException {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("INFO: module activateUpdate called in " + this.moduleName);
      }

      if (!this.hasUpdate) {
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("INFO: module activateUpdate exiting with no updates in " + this.moduleName);
         }

      } else {
         DomainMBean var2 = ManagementService.getRuntimeAccess(kernelId).getDomain();
         this.hasUpdate = false;
         Object var3 = this.update;
         this.update = null;
         this.hasCalledPrepareUpdate = false;
         this.hasGottenModulePrepareUpdate = null;
         this.activateUpdate(var2, var3);
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("INFO: module activateUpdate exiting successfully in " + this.moduleName);
         }

      }
   }

   public void rollbackUpdate(String var1) {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("INFO: module rollbackUpdate called in " + this.moduleName);
      }

      if (!this.hasUpdate) {
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("INFO: module rollbackUpdate exiting with no updates in " + this.moduleName);
         }

      } else {
         DomainMBean var2 = ManagementService.getRuntimeAccess(kernelId).getDomain();
         this.hasUpdate = false;
         Object var3 = this.update;
         this.update = null;
         this.hasCalledPrepareUpdate = false;
         this.hasGottenModulePrepareUpdate = null;
         this.rollbackUpdate(var2, var3);
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("INFO: module rollbackUpdate exiting successfully in " + this.moduleName);
         }

      }
   }

   private void prepareMigratableUpdate() throws DescriptorUpdateRejectedException {
      if (this.hasMigratableUpdate && this.hasMigratableUpdateDomain != null) {
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("INFO: finisher prepareMigratableUpdate called in " + this.moduleName);
         }

         if (!this.hasCalledMigratablePrepareUpdate) {
            try {
               this.update = this.prepareUpdate(this.hasMigratableUpdateDomain);
               this.hasCalledMigratablePrepareUpdate = true;
            } catch (ModuleException var2) {
               throw new DescriptorUpdateRejectedException(var2.getMessage(), var2);
            }
         }

         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("INFO: finisher prepareMigratableUpdate exits in " + this.moduleName);
         }

      } else {
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("INFO: finisher prepareMigratableUpdate exits with no updates in " + this.moduleName);
         }

      }
   }

   public void prepareUpdate(DescriptorUpdateEvent var1) throws DescriptorUpdateRejectedException {
      if (!this.hasUpdate) {
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("INFO: finisher prepareUpdate exits with no updates in " + this.moduleName);
         }

      } else {
         Descriptor var2 = var1.getProposedDescriptor();
         DescriptorBean var3 = var2.getRootBean();
         DomainMBean var4 = (DomainMBean)var3;
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("INFO: finisher prepareUpdate called in " + this.moduleName);
         }

         if (!this.hasCalledPrepareUpdate) {
            try {
               this.update = this.prepareUpdate(var4);
               this.hasCalledPrepareUpdate = true;
            } catch (ModuleException var6) {
               throw new DescriptorUpdateRejectedException(var6.getMessage(), var6);
            }
         }

         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("INFO: finisher prepareUpdate exits in " + this.moduleName);
         }

      }
   }

   private void activateMigratableUpdate() {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("INFO: finisher activateMigratableUpdate called in " + this.moduleName);
      }

      if (!this.hasMigratableUpdate) {
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("INFO: finisher activateMigratableUpdate exiting with no changes in " + this.moduleName + " hasUpdate=" + this.hasMigratableUpdate + " hasGottenModulePrepareUpdate=" + this.hasGottenModulePrepareUpdate);
         }

      } else {
         this.hasMigratableUpdate = false;
         this.resetMigratableUpdateInfo();
         Object var1 = this.update;
         this.update = null;
         this.hasCalledMigratablePrepareUpdate = false;
         DomainMBean var2 = ManagementService.getRuntimeAccess(kernelId).getDomain();

         try {
            this.activateUpdate(var2, var1);
         } catch (ModuleException var4) {
            JMSLogger.logActivateFailedDuringTargetingChange(this.moduleName.toString(), var4.toString());
         }

      }
   }

   public void activateUpdate(DescriptorUpdateEvent var1) {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("INFO: finisher activateUpdate called in " + this.moduleName);
      }

      if (this.hasUpdate && this.hasGottenModulePrepareUpdate == null) {
         this.hasUpdate = false;
         Object var2 = this.update;
         this.update = null;
         this.hasCalledPrepareUpdate = false;
         DomainMBean var3 = ManagementService.getRuntimeAccess(kernelId).getDomain();

         try {
            this.activateUpdate(var3, var2);
         } catch (ModuleException var5) {
            JMSLogger.logActivateFailedDuringTargetingChange(this.moduleName.toString(), var5.toString());
         }

      } else {
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("INFO: finisher activateUpdate exiting with no changes in " + this.moduleName + " hasUpdate=" + this.hasUpdate + " hasGottenModulePrepareUpdate=" + this.hasGottenModulePrepareUpdate);
         }

      }
   }

   private void rollbackMigratableUpdate() {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("INFO: finisher rollbackMigratableUpdate called in " + this.moduleName);
      }

      if (this.hasMigratableUpdate) {
         this.hasMigratableUpdate = false;
         this.resetMigratableUpdateInfo();
         Object var1 = this.update;
         this.update = null;
         this.hasCalledMigratablePrepareUpdate = false;
         DomainMBean var2 = ManagementService.getRuntimeAccess(kernelId).getDomain();
         this.rollbackUpdate(var2, var1);
      }
   }

   public void rollbackUpdate(DescriptorUpdateEvent var1) {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("INFO: finisher rollbackUpdate called in " + this.moduleName);
      }

      if (this.hasUpdate) {
         this.hasUpdate = false;
         Object var2 = this.update;
         this.update = null;
         this.hasCalledPrepareUpdate = false;
         DomainMBean var3 = ManagementService.getRuntimeAccess(kernelId).getDomain();
         this.rollbackUpdate(var3, var2);
      }
   }

   public void migratableUpdate() throws MigrationException {
      synchronized(this) {
         try {
            if (JMSDebug.JMSModule.isDebugEnabled()) {
               JMSDebug.JMSModule.debug("INFO: Calling prepareMigratableUpdate for " + this);
            }

            this.prepareMigratableUpdate();
         } catch (Exception var5) {
            this.resetMigratableUpdateInfo();
            throw new MigrationException(var5);
         }

         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("INFO: Calling activateMigratableUpdate for " + this);
         }

         try {
            this.activateMigratableUpdate();
         } catch (Exception var4) {
            this.rollbackMigratableUpdate();
            this.resetMigratableUpdateInfo();
            throw new MigrationException(var4);
         }

      }
   }

   private void resetMigratableUpdateInfo() {
      synchronized(this) {
         if (this.hasMigratableUpdateDomain != null) {
            this.hasMigratableUpdateDomain = null;
         }

         if (this.targeter != null) {
            synchronized(this.targeter) {
               this.targeter.setUpdateAction(-1);
               this.targeter.setHasUpdate(false);
               this.targeter.setUpdatedTargetMBean((TargetMBean)null);
            }
         }

      }
   }

   private void openJMSComponent() throws ModuleException {
      try {
         this.jmsComponent = new JMSComponent(this.moduleName.toString(), this.getId(), this.appCtx);
         this.jmsComponent.open();
      } catch (ManagementException var2) {
         throw new ModuleException(var2.getMessage(), var2);
      }
   }

   private void closeJMSComponent() {
      if (this.jmsComponent != null) {
         try {
            this.jmsComponent.close();
         } catch (ManagementException var2) {
            JMSLogger.logComponentCloseFailure(this.moduleName.toString(), var2.toString());
         }

         this.jmsComponent = null;
      }

   }

   private String getEARModuleName() {
      return this.earModuleName;
   }

   ApplicationContextInternal getAppCtx() {
      return this.appCtx;
   }

   private void internalInit(ApplicationContextInternal var1, UpdateListener.Registration var2) throws ModuleException {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("INFO: module internalInit called in " + this.moduleName);
      }

      if (JMSService.getJMSService().getFrontEnd() == null) {
         throw new ModuleException(JMSExceptionLogger.logJMSServiceNotInitialized2Loggable().getMessage());
      } else {
         this.appCtx = var1;
         var2.addUpdateListener(this);
         this.moduleName = new ModuleName(this.appCtx.getApplicationId(), this.getEARModuleName());
         this.moduleType = this.getModuleTypeFromAppCtx();
         this.openJMSComponent();
         DomainMBean var3 = this.getDomainFromAppCtx();
         this.initializeModule(this.appCtx, var3);
      }
   }

   private int getModuleTypeFromAppCtx() throws ModuleException {
      if (this.appCtx.getAppDeploymentMBean() != null) {
         return 0;
      } else if (this.appCtx.getSystemResourceMBean() != null) {
         return 1;
      } else {
         throw new ModuleException(JMSExceptionLogger.logUnknownJMSModuleTypeLoggable(this.moduleName.toString()).getMessage());
      }
   }

   private DomainMBean getDomainFromAppCtx() {
      Object var1 = null;
      switch (this.moduleType) {
         case 0:
            var1 = this.appCtx.getAppDeploymentMBean();
            break;
         case 1:
            var1 = this.appCtx.getSystemResourceMBean();
            break;
         default:
            throw new AssertionError("Unknown module type: " + this.moduleType);
      }

      return JMSBeanHelper.getDomain((WebLogicMBean)var1);
   }

   protected TargetInfoMBean getTargetingBean(DomainMBean var1) {
      switch (this.moduleType) {
         case 0:
            AppDeploymentMBean var2 = this.appCtx.getAppDeploymentMBean();
            var2 = var1.lookupAppDeployment(var2.getName());
            if (var2 != null && this.earModuleName != null) {
               return var2.lookupSubDeployment(this.earModuleName);
            }

            return var2;
         case 1:
            SystemResourceMBean var3 = this.appCtx.getSystemResourceMBean();
            Object var4;
            if (var3 instanceof JMSInteropModuleMBean) {
               var4 = JMSBeanHelper.getJMSInteropModule(var1);
            } else {
               var4 = var1.lookupJMSSystemResource(var3.getName());
            }

            return (TargetInfoMBean)var4;
         default:
            throw new AssertionError("Only file-based modules have TargetInfos");
      }
   }

   private DomainMBean getProposedDomain() {
      DomainMBean var1 = this.appCtx.getProposedDomain();
      if (var1 == null) {
         var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      }

      return var1;
   }

   protected BasicDeploymentMBean getBasicDeployment(DomainMBean var1) {
      switch (this.moduleType) {
         case 0:
            AppDeploymentMBean var2 = this.appCtx.getAppDeploymentMBean();
            AppDeploymentMBean var3 = var1.lookupAppDeployment(var2.getName());
            if (var3 == null) {
               var3 = var2;
            }

            return var3;
         case 1:
            SystemResourceMBean var4 = this.appCtx.getSystemResourceMBean();
            Object var5;
            if (var4 instanceof JMSInteropModuleMBean) {
               var5 = JMSBeanHelper.getJMSInteropModule(var1);
            } else {
               var5 = var1.lookupJMSSystemResource(var4.getName());
            }

            if (var5 == null) {
               var5 = var4;
            }

            return (BasicDeploymentMBean)var5;
         default:
            return null;
      }
   }

   public void startAddSubDeployments(SubDeploymentMBean var1) throws BeanUpdateRejectedException {
      this.hasUpdate = true;
   }

   public void finishAddSubDeployments(SubDeploymentMBean var1, boolean var2) {
      if (var2 && this.isActive) {
         if (this.isActive) {
            var1.addBeanUpdateListener(this.deploymentListener);
            this.targetingListenees.put(var1.getName(), var1);
            if (JMSDebug.JMSModule.isDebugEnabled()) {
               JMSDebug.JMSModule.debug("INFO: Now listening on subdeployment " + var1.getName() + " in module " + this.moduleName);
            }

         }
      } else {
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("INFO: Not listening on sub-deployment " + var1.getName() + " either because the module is shutdown(" + !this.isActive + ") or this is a rollback which included an addition of a subdeployment(" + !var2 + ") in module " + this + " of name " + this.moduleName);
         }

      }
   }

   public void startRemoveSubDeployments(SubDeploymentMBean var1) throws BeanUpdateRejectedException {
      Object var2 = this.targetingListenees.get(var1.getName());
      if (var2 == null) {
         throw new BeanUpdateRejectedException(JMSExceptionLogger.logUnknownSubDeploymentLoggable(this.moduleName.toString(), var1.getName()).getMessage());
      } else {
         this.hasUpdate = true;
      }
   }

   public void finishRemoveSubDeployments(SubDeploymentMBean var1, boolean var2) {
      if (var2) {
         var1.removeBeanUpdateListener(this.deploymentListener);
         this.targetingListenees.remove(var1.getName());
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("INFO: listener on subdeployment " + var1.getName() + " in module " + this.moduleName + " has been removed");
         }

         this.targeter.subModuleListenerContextMap.remove(var1.getName());
      }
   }

   public String getName() {
      return this.moduleName.toString();
   }

   public String getType() {
      return WebLogicModuleType.MODULETYPE_JMS;
   }

   public String toString() {
      return "JMSModule(" + System.identityHashCode(this) + ",uri=" + this.uri + ",EARModuleName=" + this.earModuleName + ")" + ", Type(" + this.getType() + ")";
   }

   static {
      deploymentAdditions.put("SubDeployments", SubDeploymentMBean.class);
   }

   public class EARSubDeploymentListener {
      public void startAddSubDeployments(SubDeploymentMBean var1) throws BeanUpdateRejectedException {
         if (ModuleCoordinator.this.earModuleName != null) {
            if (ModuleCoordinator.this.earModuleName.equals(var1.getName())) {
               ModuleCoordinator.this.hasUpdate = true;
            }

         }
      }

      public void finishAddSubDeployments(SubDeploymentMBean var1, boolean var2) {
         if (var2 && ModuleCoordinator.this.isActive) {
            if (ModuleCoordinator.this.earModuleName != null) {
               if (ModuleCoordinator.this.earModuleName.equals(var1.getName())) {
                  ModuleCoordinator.this.activateSubDeploymentListener(var1);
                  ModuleCoordinator.this.targetingAdditionsListener = new GenericBeanListener(var1, ModuleCoordinator.this, (Map)null, ModuleCoordinator.deploymentAdditions, true);
                  if (JMSDebug.JMSModule.isDebugEnabled()) {
                     JMSDebug.JMSModule.debug("INFO: EAR sub-deployment " + ModuleCoordinator.this.earModuleName + " in module " + ModuleCoordinator.this.moduleName + " was added");
                  }

               }
            }
         } else {
            if (JMSDebug.JMSModule.isDebugEnabled()) {
               JMSDebug.JMSModule.debug("INFO: Not listening on sub-deployment " + var1.getName() + " either because the module is shutdown(" + !ModuleCoordinator.this.isActive + ") or this is a rollback which included an addition of a subdeployment(" + !var2 + ") in module " + this + " of name " + ModuleCoordinator.this.moduleName);
            }

         }
      }

      public void startRemoveSubDeployments(SubDeploymentMBean var1) throws BeanUpdateRejectedException {
         if (ModuleCoordinator.this.earModuleName != null) {
            if (ModuleCoordinator.this.earModuleName.equals(var1.getName())) {
               ModuleCoordinator.this.hasUpdate = true;
            }

         }
      }

      public void finishRemoveSubDeployments(SubDeploymentMBean var1, boolean var2) {
         if (var2) {
            if (ModuleCoordinator.this.earModuleName != null) {
               if (ModuleCoordinator.this.earModuleName.equals(var1.getName())) {
                  ModuleCoordinator.this.deactivateSubDeploymentListener();
                  if (JMSDebug.JMSModule.isDebugEnabled()) {
                     JMSDebug.JMSModule.debug("INFO: EAR sub-deployment " + ModuleCoordinator.this.earModuleName + " in module " + ModuleCoordinator.this.moduleName + " was removed");
                  }

               }
            }
         }
      }
   }

   public class ASMDeactivateHandler implements Migratable {
      public void migratableInitialize() {
      }

      public void migratableActivate() throws MigrationException {
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("INFO: Got migratableActivate for " + this);
         }

      }

      public void migratableDeactivate() throws MigrationException {
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("INFO: Got migratableDeactivate for " + this);
         }

         ModuleCoordinator.this.migratableUpdate();
      }

      public String getName() {
         return "[ASMDeactivateHandler for JMSModule " + ModuleCoordinator.this.moduleName.toString() + "]";
      }

      public String toString() {
         return "[ASMDeactivateHandler for JMSModule " + ModuleCoordinator.this.moduleName.toString() + "]";
      }

      public int getOrder() {
         return -1901;
      }
   }

   public class ASMActivateHandler implements Migratable {
      public void migratableInitialize() {
      }

      public void migratableActivate() throws MigrationException {
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("INFO: Got migratableActivate for " + this);
         }

         ModuleCoordinator.this.migratableUpdate();
      }

      public void migratableDeactivate() throws MigrationException {
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("INFO: Got migratableDeactivate for " + this);
         }

      }

      public String getName() {
         return "[ASMActivateHandler for JMSModule " + ModuleCoordinator.this.moduleName.toString() + "]";
      }

      public String toString() {
         return "[ASMActivateHandler for JMSModule " + ModuleCoordinator.this.moduleName.toString() + "]";
      }

      public int getOrder() {
         return 2147483646;
      }
   }

   public class JMSTargetsListenerImpl implements JMSTargetsListener {
      public void prepareUpdate(DomainMBean var1, TargetMBean var2, int var3, boolean var4) throws BeanUpdateRejectedException {
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("INFO: jmsListener prepareUpdate called in " + ModuleCoordinator.this.moduleName);
         }

         synchronized(this) {
            if (!var4) {
               ModuleCoordinator.this.hasUpdate = true;
            } else {
               ModuleCoordinator.this.hasMigratableUpdate = true;
            }

            ModuleCoordinator.this.hasMigratableUpdateDomain = var1;
            if (ModuleCoordinator.this.targeter != null) {
               synchronized(ModuleCoordinator.this.targeter) {
                  ModuleCoordinator.this.targeter.setHasUpdate(true);
                  ModuleCoordinator.this.targeter.setUpdatedTargetMBean(var2);
                  ModuleCoordinator.this.targeter.setUpdateAction(var3);
               }
            }

         }
      }

      public void rollbackUpdate() {
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("INFO: jmsListener rollbackUpdate called in " + ModuleCoordinator.this.moduleName);
         }

      }

      public void activateUpdate() {
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("INFO: jmsListener activateUpdate called in " + ModuleCoordinator.this.moduleName);
         }

      }
   }

   private class DeploymentListener implements BeanUpdateListener {
      private DeploymentListener() {
      }

      public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("INFO: deploymentListener prepareUpdate called in " + ModuleCoordinator.this.moduleName);
         }

         ModuleCoordinator.this.hasUpdate = true;
      }

      public void activateUpdate(BeanUpdateEvent var1) {
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("INFO: deploymentListener activateUpdate called in " + ModuleCoordinator.this.moduleName);
         }

      }

      public void rollbackUpdate(BeanUpdateEvent var1) {
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("INFO: deploymentListener rollbackUpdate called in " + ModuleCoordinator.this.moduleName);
         }

      }

      // $FF: synthetic method
      DeploymentListener(Object var2) {
         this();
      }
   }
}
