package weblogic.application.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.deploy.shared.ModuleType;
import javax.xml.stream.XMLStreamException;
import weblogic.application.AdminModeCompletionBarrier;
import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContext;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.CustomModuleContext;
import weblogic.application.MergedDescriptorModule;
import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.UpdateListener;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorUpdateFailedException;
import weblogic.descriptor.DescriptorUpdateRejectedException;
import weblogic.j2ee.descriptor.wl.ConfigurationSupportBean;
import weblogic.j2ee.descriptor.wl.CustomModuleBean;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.utils.Debug;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.GenericClassLoader;

public final class DefaultModule implements Module, UpdateListener, MergedDescriptorModule {
   private static final boolean debug = Debug.getCategory("weblogic.application.config.DefaultModule").isEnabled();
   private final CustomModuleContext customModuleContext;
   private final String descriptorUri;
   private final String parentModuleUri;
   private final ModuleType parentModuleType;
   private ApplicationContextInternal applicationContext;
   private DescriptorBean descriptorBean;
   private String descriptorNamespace;
   private String appModuleName;
   private final ConfigModuleCallbackHandler callbackHandler;
   private final boolean failOnNonDynamicChanges;
   private final boolean mergeLibraryDescriptors;
   private String bindingJarUri;
   private ConfigDescriptorManager configDescManager;
   private boolean descriptorParsed = false;
   private boolean useBindingCache = true;

   public DefaultModule(CustomModuleContext var1, CustomModuleBean var2, ConfigModuleCallbackHandler var3, String var4, boolean var5, boolean var6) {
      this.customModuleContext = var1;
      this.parentModuleUri = var1.getParentModuleUri();
      this.descriptorUri = var2.getUri();
      this.failOnNonDynamicChanges = var5;
      this.mergeLibraryDescriptors = var6;
      this.callbackHandler = var3;
      this.appModuleName = ApplicationAccess.getApplicationAccess().getCurrentApplicationName();
      if (this.parentModuleUri != null) {
         this.parentModuleType = ModuleType.WAR;
         if (this.appModuleName == null) {
            this.appModuleName = this.parentModuleUri;
         } else {
            this.appModuleName = this.appModuleName + "/" + this.parentModuleUri;
         }
      } else {
         this.parentModuleType = ModuleType.EAR;
      }

      if (debug) {
         Debug.say(this.appModuleName + "/" + this.descriptorUri);
      }

      ConfigurationSupportBean var7 = var2.getConfigurationSupport();
      if (var7 == null) {
         CustomModuleLogger.logNoConfigSupport(this.appModuleName, this.descriptorUri);
      } else {
         if (!this.descriptorUri.equals(var7.getBaseUri())) {
            CustomModuleLogger.logConfigSupportUriMismatch(this.appModuleName, this.descriptorUri, var7.getBaseUri());
         }

         if (var4 != null) {
            this.descriptorNamespace = var4;
         } else {
            this.descriptorNamespace = var7.getBaseNamespace();
         }
      }

      this.bindingJarUri = var1.getModuleProviderBean().getBindingJarUri();
   }

   public String getId() {
      return this.descriptorUri;
   }

   public String getType() {
      return WebLogicModuleType.MODULETYPE_CONFIG;
   }

   public DescriptorBean[] getDescriptors() {
      if (!this.descriptorParsed) {
         try {
            this.descriptorBean = this.parseDescriptorBean();
         } catch (ModuleException var2) {
         }
      }

      return this.descriptorBean == null ? new DescriptorBean[0] : new DescriptorBean[]{this.descriptorBean};
   }

   public ComponentRuntimeMBean[] getComponentRuntimeMBeans() {
      return new ComponentRuntimeMBean[0];
   }

   public GenericClassLoader init(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      this.applicationContext = (ApplicationContextInternal)var1;
      this.appModuleName = this.applicationContext.getApplicationId();
      if (this.parentModuleUri != null) {
         this.appModuleName = this.appModuleName + "/" + this.parentModuleUri;
      }

      if (debug) {
         Debug.say(this.appModuleName + "/" + this.descriptorUri);
      }

      var3.addUpdateListener(this);
      this.configDescManager = new ConfigDescriptorManager(this.applicationContext, this.appModuleName, debug);
      this.configDescManager.initBindingInfo(var2, this.bindingJarUri, this.useBindingCache);
      return var2;
   }

   public void initUsingLoader(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      if (debug) {
         Debug.say(this.appModuleName + "/" + this.descriptorUri);
      }

      this.init(var1, var2, var3);
   }

   public void prepare() throws ModuleException {
      if (debug) {
         Debug.say(this.appModuleName + "/" + this.descriptorUri);
      }

      CustomModuleLogger.logPrepareDeploy(this.appModuleName, this.descriptorUri);
      this.descriptorBean = this.parseDescriptorBean();
      this.descriptorParsed = true;
      if (this.callbackHandler != null) {
         this.callbackHandler.prepare(this.descriptorUri, this.descriptorNamespace, this.descriptorBean);
      }

   }

   public void activate() throws ModuleException {
      if (debug) {
         Debug.say(this.appModuleName + "/" + this.descriptorUri);
      }

      if (this.callbackHandler != null) {
         this.callbackHandler.activate(this.descriptorUri, this.descriptorNamespace, this.descriptorBean);
      }

   }

   public void start() {
      if (debug) {
         Debug.say(this.appModuleName + "/" + this.descriptorUri);
      }

   }

   public void deactivate() {
      if (debug) {
         Debug.say(this.appModuleName + "/" + this.descriptorUri);
      }

      if (this.callbackHandler != null) {
         this.callbackHandler.deactivate(this.descriptorUri, this.descriptorNamespace, this.descriptorBean);
      }

   }

   public void unprepare() {
      if (debug) {
         Debug.say(this.appModuleName + "/" + this.descriptorUri);
      }

      if (this.callbackHandler != null) {
         this.callbackHandler.unprepare(this.descriptorUri, this.descriptorNamespace, this.descriptorBean);
      }

      this.descriptorBean = null;
   }

   public void destroy(UpdateListener.Registration var1) {
      if (debug) {
         Debug.say(this.appModuleName + "/" + this.descriptorUri);
      }

      var1.removeUpdateListener(this);
      this.applicationContext = null;
      this.descriptorBean = null;
      this.configDescManager.destroy();
   }

   public boolean acceptURI(String var1) {
      return this.parentModuleType == ModuleType.WAR ? var1.equals(this.parentModuleUri + "/" + this.descriptorUri) : var1.equals(this.descriptorUri);
   }

   public void prepareUpdate(String var1) throws ModuleException {
      if (debug) {
         Debug.say(var1);
      }

      CustomModuleLogger.logPrepareUpdate(this.appModuleName, var1);
      DescriptorBean var2 = this.parseDescriptorBean();
      this.descriptorParsed = true;
      if (debug) {
         Debug.say("prepareUpdate: " + var2);
      }

      try {
         this.descriptorBean.getDescriptor().prepareUpdate(var2.getDescriptor(), this.failOnNonDynamicChanges);
      } catch (DescriptorUpdateRejectedException var4) {
         if (debug) {
            Debug.say(var4.toString());
         }

         throw new ModuleException("Prepare failed for update to " + this.descriptorUri + " for app module " + this.appModuleName, var4);
      }
   }

   public void activateUpdate(String var1) throws ModuleException {
      if (debug) {
         Debug.say(var1);
      }

      try {
         this.descriptorBean.getDescriptor().activateUpdate();
      } catch (DescriptorUpdateFailedException var3) {
         if (debug) {
            Debug.say(var3.toString());
         }

         throw new ModuleException("Activate failed for update to " + this.descriptorUri + " for app module " + this.appModuleName, var3);
      }
   }

   public void rollbackUpdate(String var1) {
      if (debug) {
         Debug.say(var1);
      }

      this.descriptorBean.getDescriptor().rollbackUpdate();
   }

   public void remove() {
      if (debug) {
         Debug.say(this.appModuleName + "/" + this.descriptorUri);
      }

   }

   public void adminToProduction() {
   }

   public void gracefulProductionToAdmin(AdminModeCompletionBarrier var1) {
   }

   public void forceProductionToAdmin() {
   }

   public Map getDescriptorMappings() {
      if (!this.mergeLibraryDescriptors) {
         return null;
      } else {
         DescriptorBean[] var1 = this.getDescriptors();
         if (var1 != null && var1.length != 0) {
            HashMap var2 = new HashMap(1);
            var2.put(this.descriptorUri, var1[0]);
            return var2;
         } else {
            return null;
         }
      }
   }

   public void handleMergedFinder(ClassFinder var1) {
   }

   private DescriptorBean parseDescriptorBean() throws ModuleException {
      try {
         File var1 = null;
         if (this.applicationContext.getAppDeploymentMBean() != null && this.applicationContext.getAppDeploymentMBean().getLocalPlanDir() != null) {
            var1 = new File(this.applicationContext.getAppDeploymentMBean().getLocalPlanDir());
         }

         return this.configDescManager.parseMergedDescriptorBean(var1, this.applicationContext.findDeploymentPlan(), this.descriptorUri, this.customModuleContext.getParentModuleId(), this.parentModuleType, this.customModuleContext.getParentModuleUri(), this.mergeLibraryDescriptors, false);
      } catch (IOException var2) {
         throw new ModuleException("Error reading descriptor: " + this.descriptorUri + " for app module " + this.appModuleName, var2);
      } catch (XMLStreamException var3) {
         throw new ModuleException("Error reading descriptor: " + this.descriptorUri + " for app module " + this.appModuleName, var3);
      }
   }
}
