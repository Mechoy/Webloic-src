package weblogic.application.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.enterprise.deploy.shared.ModuleType;
import javax.xml.stream.XMLStreamException;
import weblogic.application.CustomModuleContext;
import weblogic.application.ModuleException;
import weblogic.application.compiler.CompilerCtx;
import weblogic.application.compiler.EARModule;
import weblogic.application.utils.IOUtils;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.wl.CustomModuleBean;
import weblogic.utils.Debug;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.NullClassFinder;
import weblogic.utils.compiler.ToolFailureException;

public class DefaultEARModule extends EARModule {
   private ConfigDescriptorManager configDescManager;
   private DescriptorBean moduleDescriptorBean;
   CustomModuleContext customModuleContext;
   String descriptorUri;
   String parentModuleUri;
   String parentModuleId;
   String appModuleName;
   private final ModuleType parentModuleType;
   private GenericClassLoader parentClassLoader;
   private boolean useBindingCache;
   private final boolean ignoreMissingDescriptors;
   private static final boolean debug = Debug.getCategory("weblogic.application.config.DefaultModule").isEnabled();

   public DefaultEARModule(CustomModuleContext var1, CustomModuleBean var2) throws ToolFailureException {
      this(var1, var2, false);
   }

   public DefaultEARModule(CustomModuleContext var1, CustomModuleBean var2, boolean var3) throws ToolFailureException {
      super(var2.getUri(), (String)null);
      this.useBindingCache = true;
      this.customModuleContext = var1;
      this.parentModuleUri = var1.getParentModuleUri();
      this.parentModuleId = var1.getParentModuleId();
      if (this.parentModuleUri != null) {
         this.parentModuleType = ModuleType.WAR;
         this.appModuleName = this.parentModuleUri;
      } else {
         this.parentModuleType = ModuleType.EAR;
      }

      if (var2.getConfigurationSupport() != null && var2.getConfigurationSupport().getBaseUri() != null) {
         this.descriptorUri = var2.getConfigurationSupport().getBaseUri();
         this.ignoreMissingDescriptors = var3;
      } else {
         throw new ToolFailureException("Unable to find base descriptor URI for config module " + this.appModuleName);
      }
   }

   public void compile(GenericClassLoader var1, CompilerCtx var2) throws ToolFailureException {
   }

   public ClassFinder getClassFinder() {
      return NullClassFinder.NULL_FINDER;
   }

   public void initModuleClassLoader(CompilerCtx var1, GenericClassLoader var2) throws ToolFailureException {
      try {
         this.configDescManager = new ConfigDescriptorManager(var1.getApplicationContext(), this.appModuleName, debug);
         this.configDescManager.initBindingInfo(var2, this.customModuleContext.getModuleProviderBean().getBindingJarUri(), this.useBindingCache);
         this.parentClassLoader = var2;
      } catch (ModuleException var4) {
         throw new ToolFailureException("Unable to init module classloader", var4);
      }
   }

   public GenericClassLoader getModuleClassLoader() {
      return this.parentClassLoader;
   }

   public void populateMVI(GenericClassLoader var1, CompilerCtx var2) throws ToolFailureException {
   }

   public void writeDescriptors(CompilerCtx var1) throws ToolFailureException {
      try {
         if (this.moduleDescriptorBean != null && this.descriptorUri != null && this.parentClassLoader != null) {
            FileOutputStream var2 = new FileOutputStream(IOUtils.checkCreateParent(new File(this.getOutputDir(), this.descriptorUri)));
            (new EditableDescriptorManager(this.parentClassLoader)).writeDescriptorAsXML(this.moduleDescriptorBean.getDescriptor(), var2);
            var2.close();
         }

      } catch (IOException var3) {
         throw new ToolFailureException("Unable to write custom module descriptors", var3);
      }
   }

   public void merge(CompilerCtx var1) throws ToolFailureException {
      try {
         this.moduleDescriptorBean = this.configDescManager.parseMergedDescriptorBean(var1.getConfigDir(), var1.getPlanBean(), this.descriptorUri, this.parentModuleId, this.parentModuleType, this.parentModuleUri, true, this.ignoreMissingDescriptors);
         if (this.descriptorUri != null && this.moduleDescriptorBean != null) {
            this.addRootBean(this.descriptorUri, this.moduleDescriptorBean);
         }

      } catch (IOException var3) {
         throw new ToolFailureException("Error reading descriptor: " + this.descriptorUri + " for app module " + this.appModuleName, var3);
      } catch (XMLStreamException var4) {
         throw new ToolFailureException("Error reading descriptor: " + this.descriptorUri + " for app module " + this.appModuleName, var4);
      } catch (ModuleException var5) {
         throw new ToolFailureException("Error reading descriptor: " + this.descriptorUri + " for app module " + this.appModuleName, var5);
      }
   }

   public void cleanup() {
      super.cleanup();
      this.configDescManager.destroy();
   }

   public DescriptorBean getRootBean() {
      return this.getRootBean(this.descriptorUri);
   }

   public ModuleType getModuleType() {
      return WebLogicModuleType.CONFIG;
   }
}
