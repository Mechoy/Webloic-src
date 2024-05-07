package weblogic.application.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import weblogic.application.AdminModeCompletionBarrier;
import weblogic.application.ApplicationContext;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.UpdateListener;
import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.descriptor.DescriptorUpdateFailedException;
import weblogic.descriptor.DescriptorUpdateRejectedException;
import weblogic.descriptor.DescriptorValidateException;
import weblogic.j2ee.descriptor.wl.ConfigurationSupportBean;
import weblogic.j2ee.descriptor.wl.CustomModuleBean;
import weblogic.j2ee.descriptor.wl.ModuleProviderBean;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFile;

public abstract class ConfigModule implements Module, UpdateListener {
   private static boolean VERBOSE = false;
   private final ModuleProviderBean provider;
   private final String uri;
   private final String descriptorURI;
   private DescriptorManager manager;
   private Descriptor currentDescriptor;
   private ApplicationContextInternal appCtx;

   public ConfigModule(ModuleProviderBean var1, CustomModuleBean var2) throws ModuleException {
      this.provider = var1;
      this.uri = var2.getUri();
      if (var1.getBindingJarUri() == null) {
         throw new ModuleException("Your module-provider needs to specify a binding-jar-uri in its weblogic-extension.xml");
      } else {
         ConfigurationSupportBean var3 = var2.getConfigurationSupport();
         if (var3 == null) {
            throw new ModuleException("Your config module must include configuration-support in the weblogic-extension.xml.");
         } else {
            this.descriptorURI = var3.getBaseUri();
         }
      }
   }

   public String getId() {
      return this.uri;
   }

   public String getType() {
      return WebLogicModuleType.MODULETYPE_CONFIG;
   }

   public boolean acceptURI(String var1) {
      return this.descriptorURI.equals(var1);
   }

   public DescriptorBean[] getDescriptors() {
      return new DescriptorBean[]{this.currentDescriptor.getRootBean()};
   }

   public ComponentRuntimeMBean[] getComponentRuntimeMBeans() {
      return new ComponentRuntimeMBean[0];
   }

   public GenericClassLoader init(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) {
      this.appCtx = (ApplicationContextInternal)var1;
      var3.addUpdateListener(this);
      return var2;
   }

   public void initUsingLoader(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) {
      this.init(var1, var2, var3);
   }

   private InputStream findResource(VirtualJarFile var1, String var2) throws IOException {
      ZipEntry var3 = var1.getEntry(var2);
      return var3 == null ? null : var1.getInputStream(var3);
   }

   protected InputStream findResource(String var1) throws IOException {
      VirtualJarFile var2 = null;

      try {
         var2 = this.appCtx.getApplicationFileManager().getVirtualJarFile(this.uri);
      } catch (IOException var4) {
         var2 = null;
      }

      if (var2 != null) {
         InputStream var3 = this.findResource(var2, var1);
         if (var3 != null) {
            return var3;
         }

         var2.close();
      }

      var2 = this.appCtx.getApplicationFileManager().getVirtualJarFile();
      return this.findResource(var2, var1);
   }

   protected Descriptor parseDescriptor() throws ModuleException {
      InputStream var1 = null;

      Descriptor var3;
      try {
         var1 = this.findResource(this.descriptorURI);
         if (var1 == null) {
            throw new ModuleException("Unable to find descriptor at " + this.descriptorURI);
         }

         Descriptor var2 = this.manager.createDescriptor(var1);
         if (VERBOSE) {
            this.manager.writeDescriptorAsXML(var2, System.out);
         }

         var3 = var2;
      } catch (IOException var12) {
         throw new ModuleException(var12);
      } finally {
         if (var1 != null) {
            try {
               var1.close();
            } catch (Exception var11) {
            }
         }

      }

      return var3;
   }

   public void prepare() throws ModuleException {
      this.manager = new DescriptorManager(this.provider.getBindingJarUri());
      this.currentDescriptor = this.parseDescriptor();
      this.readDescriptor(this.currentDescriptor.getRootBean());
   }

   public void activate() throws ModuleException {
   }

   public void start() throws ModuleException {
   }

   public void deactivate() throws ModuleException {
   }

   public void unprepare() throws ModuleException {
   }

   public void destroy(UpdateListener.Registration var1) throws ModuleException {
      var1.removeUpdateListener(this);
   }

   public void prepareUpdate(String var1) throws ModuleException {
      Descriptor var2 = this.parseDescriptor();

      try {
         this.currentDescriptor.prepareUpdate(var2);
      } catch (DescriptorUpdateRejectedException var4) {
         throw new ModuleException(var4);
      } catch (DescriptorValidateException var5) {
         throw new ModuleException(var5);
      }
   }

   public void activateUpdate(String var1) throws ModuleException {
      try {
         this.currentDescriptor.activateUpdate();
      } catch (DescriptorUpdateFailedException var6) {
         throw new ModuleException(var6);
      } finally {
         this.updatedDescriptor(this.currentDescriptor.getRootBean());
      }

   }

   public void rollbackUpdate(String var1) {
      this.currentDescriptor.rollbackUpdate();
   }

   public void remove() throws ModuleException {
   }

   public void adminToProduction() {
   }

   public void gracefulProductionToAdmin(AdminModeCompletionBarrier var1) {
   }

   public void forceProductionToAdmin() {
   }

   public abstract void readDescriptor(DescriptorBean var1) throws ModuleException;

   public abstract void updatedDescriptor(DescriptorBean var1) throws ModuleException;
}
