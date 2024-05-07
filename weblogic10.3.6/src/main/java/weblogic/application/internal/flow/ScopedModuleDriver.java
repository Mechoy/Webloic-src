package weblogic.application.internal.flow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import weblogic.application.AdminModeCompletionBarrier;
import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContext;
import weblogic.application.MergedDescriptorModule;
import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.ModuleWrapper;
import weblogic.application.UpdateListener;
import weblogic.application.internal.FlowContext;
import weblogic.application.utils.EarUtils;
import weblogic.application.utils.ExtensionDescriptorParser;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.WeblogicExtensionBean;
import weblogic.management.DeploymentException;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFile;

public final class ScopedModuleDriver extends ModuleWrapper implements Module, MergedDescriptorModule {
   private final Module module;
   private Module[] scopedModules = new Module[0];
   private Module[] allModules;
   private final ModuleStateDriver driver;
   private final String moduleUri;
   private DescriptorBean[] descriptors = null;
   private FlowContext appCtx;
   private WeblogicExtensionBean extDescriptor;
   private final String extensionLocationUri;
   private final ExtensionDescriptorParser extLoader;

   public ScopedModuleDriver(Module var1, FlowContext var2, String var3, VirtualJarFile var4, String var5) throws ModuleException {
      if (var1 == null) {
         throw new IllegalArgumentException("module cannot be null");
      } else if (this.scopedModules == null) {
         throw new IllegalArgumentException("scopedModules cannot be null");
      } else {
         this.module = var1;
         this.appCtx = var2;
         this.driver = new ModuleStateDriver(var2);
         this.moduleUri = var3;
         this.setScopedModules(new Module[0]);
         this.extensionLocationUri = var5;
         this.extLoader = this.createModuleExtensionLoader(var4);

         try {
            this.extDescriptor = this.extLoader.getWlExtensionDescriptor();
         } catch (IOException var7) {
            throw new ModuleException(var7);
         } catch (XMLStreamException var8) {
            throw new ModuleException(var8);
         }
      }
   }

   private void setScopedModules(Module[] var1) {
      this.scopedModules = var1;
      this.allModules = new Module[var1.length + 1];
      this.allModules[0] = this.module;
      if (var1.length > 0) {
         System.arraycopy(var1, 0, this.allModules, 1, var1.length);
      }

   }

   public Module getDelegate() {
      return this.module;
   }

   public String getId() {
      return this.module.getId();
   }

   public String getType() {
      return this.module.getType();
   }

   public ComponentRuntimeMBean[] getComponentRuntimeMBeans() {
      return this.module.getComponentRuntimeMBeans();
   }

   public synchronized DescriptorBean[] getDescriptors() {
      if (this.descriptors != null) {
         return this.descriptors;
      } else {
         ArrayList var1 = new ArrayList();
         this.addDescriptors(this.module, var1);
         var1.add(this.extDescriptor);

         for(int var2 = 0; var2 < this.scopedModules.length; ++var2) {
            this.addDescriptors(this.scopedModules[var2], var1);
         }

         this.descriptors = (DescriptorBean[])((DescriptorBean[])var1.toArray(new DescriptorBean[var1.size()]));
         return this.descriptors;
      }
   }

   private void addDescriptors(Module var1, List var2) {
      DescriptorBean[] var3 = this.module.getDescriptors();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var2.add(var3[var4]);
      }

   }

   public GenericClassLoader init(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      GenericClassLoader var4 = this.module.init(var1, var2, var3);
      this.initScopedModules(var1, var4, var3);
      return var4;
   }

   public void initUsingLoader(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      this.module.initUsingLoader(var1, var2, var3);
      this.initScopedModules(var1, var2, var3);
   }

   private void initScopedModules(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      for(int var4 = 0; var4 < this.scopedModules.length; ++var4) {
         this.scopedModules[var4].init(var1, var2, var3);
      }

   }

   public void prepare() throws ModuleException {
      this.module.prepare();
      this.findAndInitScopedCustomModules();
      this.prepareScopedModules();
   }

   private void prepareScopedModules() throws ModuleException {
      this.driver.prepare(this.scopedModules);
   }

   public void activate() throws ModuleException {
      this.driver.activate(this.allModules);
   }

   public void start() throws ModuleException {
      this.driver.start(this.allModules);
   }

   public void deactivate() throws ModuleException {
      this.driver.deactivate(this.allModules);
   }

   public void unprepare() throws ModuleException {
      this.driver.unprepare(this.allModules);
   }

   public void destroy(UpdateListener.Registration var1) throws ModuleException {
      this.driver.destroy(this.allModules);
   }

   public void remove() throws ModuleException {
      this.driver.remove(this.allModules);
   }

   public void adminToProduction() {
      try {
         this.driver.adminToProduction(this.allModules);
      } catch (ModuleException var2) {
         if (var2.getCause() != null && var2.getCause() instanceof RuntimeException) {
            throw (RuntimeException)var2.getCause();
         } else {
            throw new RuntimeException(var2);
         }
      }
   }

   public void gracefulProductionToAdmin(AdminModeCompletionBarrier var1) throws ModuleException {
      this.driver.gracefulProductionToAdmin(var1, this.allModules);
   }

   public void forceProductionToAdmin() throws ModuleException {
      this.driver.forceProductionToAdmin(this.allModules);
   }

   private void findAndInitScopedCustomModules() throws ModuleException {
      GenericClassLoader var1 = ApplicationAccess.getApplicationAccess().findModuleLoader(this.appCtx.getApplicationId(), this.module.getId());

      try {
         this.extLoader.mergeWlExtensionDescriptorsFromLibraries(this.appCtx);
         WeblogicExtensionBean var2 = this.extLoader.getWlExtensionDescriptor();
         if (var2 != null) {
            try {
               Module[] var3 = CustomModuleHelper.createScopedCustomModules(this.module, this.moduleUri, var2, var1);
               this.setScopedModules(var3);
               this.initScopedModules(this.appCtx, var1, this.appCtx);
            } catch (DeploymentException var4) {
               throw new ModuleException(var4);
            }
         }

      } catch (IOException var5) {
         throw new ModuleException(var5);
      } catch (XMLStreamException var6) {
         throw new ModuleException(var6);
      }
   }

   private ExtensionDescriptorParser createModuleExtensionLoader(VirtualJarFile var1) throws ModuleException {
      return new ExtensionDescriptorParser(var1, EarUtils.getConfigDir(this.appCtx), this.appCtx.getAppDeploymentMBean().getDeploymentPlanDescriptor(), this.module.getId(), this.extensionLocationUri + "/" + "weblogic-extension.xml");
   }

   public Map getDescriptorMappings() {
      HashMap var1 = null;

      for(int var2 = 0; var2 < this.allModules.length; ++var2) {
         if (this.allModules[var2] != this && this.allModules[var2] instanceof MergedDescriptorModule) {
            MergedDescriptorModule var3 = (MergedDescriptorModule)this.allModules[var2];
            if (var3.getDescriptorMappings() != null) {
               if (var1 == null) {
                  var1 = new HashMap();
               }

               var1.putAll(var3.getDescriptorMappings());
            }
         }
      }

      return var1;
   }

   public void handleMergedFinder(ClassFinder var1) {
      for(int var2 = 0; var2 < this.allModules.length; ++var2) {
         if (this.allModules[var2] != this && this.allModules[var2] instanceof MergedDescriptorModule) {
            ((MergedDescriptorModule)this.allModules[var2]).handleMergedFinder(var1);
         }
      }

   }
}
