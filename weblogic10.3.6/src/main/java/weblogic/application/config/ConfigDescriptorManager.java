package weblogic.application.config;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.enterprise.deploy.shared.ModuleType;
import javax.xml.stream.XMLStreamException;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.ModuleException;
import weblogic.application.descriptor.AbstractDescriptorLoader2;
import weblogic.application.utils.IOUtils;
import weblogic.application.utils.LibraryUtils;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.utils.Debug;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFile;

class ConfigDescriptorManager {
   private ApplicationContextInternal applicationContext;
   private boolean debug;
   private String appModuleName;
   private GenericClassLoader bindingClassLoader;
   private boolean useBindingCache = false;

   public ConfigDescriptorManager(ApplicationContextInternal var1, String var2, boolean var3) {
      this.applicationContext = var1;
      this.debug = var3;
      this.appModuleName = var2;
   }

   public void initBindingInfo(GenericClassLoader var1, String var2, boolean var3) throws ModuleException {
      this.bindingClassLoader = var1;
      this.useBindingCache = var3;
      if (var2 != null) {
         URL var4 = this.bindingClassLoader.getResource(var2);
         if (var4 == null) {
            throw new ModuleException("Unable to load " + var2 + " for module");
         }

         this.bindingClassLoader.addClassFinder(new ClasspathClassFinder2(var4.getPath()));
      }

   }

   public void destroy() {
      if (this.useBindingCache) {
         ConfigDescriptorManagerCache.SINGLETON.removeEntry(this.bindingClassLoader);
      }

   }

   public DescriptorBean parseMergedDescriptorBean(File var1, DeploymentPlanBean var2, String var3, String var4, ModuleType var5, String var6, boolean var7, boolean var8) throws XMLStreamException, IOException, ModuleException {
      VirtualJarFile var9;
      String var10;
      if (var5 == ModuleType.EAR) {
         var9 = this.applicationContext.getApplicationFileManager().getVirtualJarFile();
         var10 = this.applicationContext.getApplicationFileName();
      } else {
         var9 = this.applicationContext.getApplicationFileManager().getVirtualJarFile(var6);
         var10 = var6;
      }

      AbstractDescriptorLoader2 var11 = new AbstractDescriptorLoader2(var9, var1, var2, var10, var3);
      DescriptorManager var12;
      if (this.useBindingCache) {
         var12 = ConfigDescriptorManagerCache.SINGLETON.getEntry(this.bindingClassLoader);
      } else {
         var12 = new DescriptorManager(this.bindingClassLoader);
      }

      var11.setDescriptorManager(var12);
      if (var7) {
         VirtualJarFile[] var13 = null;

         try {
            var13 = LibraryUtils.getLibraryVjarsWithDescriptor(this.applicationContext, var4, var3);
            if (var13.length > 0) {
               DescriptorBean var14 = var11.mergeDescriptors(var13);
               if (this.debug) {
                  Debug.say("loaded descriptor and libs to bean: " + var14);
               }

               if (var14 == null && !var8) {
                  throw new ModuleException("Descriptor not found: " + var3 + " for app module " + this.appModuleName);
               }

               DescriptorBean var15 = var14;
               return var15;
            }
         } finally {
            IOUtils.forceClose(var13);
         }
      }

      DescriptorBean var20 = var11.loadDescriptorBean();
      if (this.debug) {
         Debug.say("loaded descriptor to bean: " + var20);
      }

      if (var20 == null && !var8) {
         throw new ModuleException("Descriptor not found: " + var3 + " for app module " + this.appModuleName);
      } else {
         return var20;
      }
   }
}
