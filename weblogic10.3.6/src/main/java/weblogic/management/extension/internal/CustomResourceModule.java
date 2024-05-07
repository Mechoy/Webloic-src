package weblogic.management.extension.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import weblogic.application.AdminModeCompletionBarrier;
import weblogic.application.ApplicationContext;
import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.UpdateListener;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateFailedException;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.DescriptorBean;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.extension.Resource;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.utils.classloaders.GenericClassLoader;

public class CustomResourceModule implements Module, UpdateListener {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugConfigurationRuntime");
   String id;
   DescriptorBean descriptor;
   String resourceClass;
   Resource resource;

   public CustomResourceModule(String var1, DescriptorBean var2, String var3) {
      this.id = var1;
      this.descriptor = var2;
      this.resourceClass = var3;
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Added custom resource with id " + var1 + " resource class " + var3 + " descriptor bean " + var2);
      }

   }

   public String getId() {
      return this.id;
   }

   public String getType() {
      return "Custom";
   }

   public ComponentRuntimeMBean[] getComponentRuntimeMBeans() {
      return new ComponentRuntimeMBean[0];
   }

   public DescriptorBean[] getDescriptors() {
      return new DescriptorBean[]{this.descriptor};
   }

   public GenericClassLoader init(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      this.initializeResource(var2);
      var3.addUpdateListener(this);
      return var2;
   }

   public void initUsingLoader(ApplicationContext var1, GenericClassLoader var2, UpdateListener.Registration var3) throws ModuleException {
      this.initializeResource(var2);
   }

   public void prepare() throws ModuleException {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Custom Resource prepare() about to call resource prepare() " + this.resource);
      }

      this.resource.prepare();
      this.descriptor.addBeanUpdateListener(new BeanUpdateListener() {
         public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
            CustomResourceModule.this.resource.prepareUpdate(var1);
         }

         public void activateUpdate(BeanUpdateEvent var1) throws BeanUpdateFailedException {
            CustomResourceModule.this.resource.activateUpdate(var1);
         }

         public void rollbackUpdate(BeanUpdateEvent var1) {
            CustomResourceModule.this.resource.rollbackUpdate(var1);
         }
      });
   }

   public void activate() throws ModuleException {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Custom Resource activate() about to call resource activate() " + this.resource);
      }

      this.resource.activate();
   }

   public void start() throws ModuleException {
   }

   public void deactivate() throws ModuleException {
      this.resource.deactivate();
   }

   public void unprepare() throws ModuleException {
      this.resource.unprepare();
   }

   public void destroy(UpdateListener.Registration var1) throws ModuleException {
   }

   public void remove() throws ModuleException {
      this.resource.remove();
   }

   public void adminToProduction() {
   }

   public void gracefulProductionToAdmin(AdminModeCompletionBarrier var1) throws ModuleException {
   }

   public void forceProductionToAdmin() throws ModuleException {
   }

   void initializeResource(ClassLoader var1) throws ModuleException {
      try {
         Class var2 = var1.loadClass(this.resourceClass);
         Constructor var3 = var2.getConstructor((Class[])null);
         this.resource = (Resource)var3.newInstance((Object[])null);
         this.resource.initialize(this.descriptor);
      } catch (ClassNotFoundException var4) {
         throw new ModuleException(var4);
      } catch (NoSuchMethodException var5) {
         throw new ModuleException(var5);
      } catch (IllegalAccessException var6) {
         throw new ModuleException(var6);
      } catch (InvocationTargetException var7) {
         throw new ModuleException(var7);
      } catch (InstantiationException var8) {
         throw new ModuleException(var8);
      }
   }

   public boolean acceptURI(String var1) {
      return true;
   }

   public void prepareUpdate(String var1) throws ModuleException {
   }

   public void activateUpdate(String var1) throws ModuleException {
   }

   public void rollbackUpdate(String var1) {
   }
}
