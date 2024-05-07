package weblogic.management.provider.internal;

import weblogic.descriptor.DescriptorClassLoader;
import weblogic.management.provider.beaninfo.BeanInfoAccess;
import weblogic.management.provider.beaninfo.BeanInfoAccessFactory;
import weblogic.management.provider.beaninfo.BeanInfoRegistration;

class BeanInfoAccessSingleton {
   static BeanInfoAccess getInstance() {
      return BeanInfoAccessSingleton.SINGLETON.instance;
   }

   private static class SINGLETON {
      static BeanInfoAccess instance = BeanInfoAccessFactory.getBeanInfoAccess();

      static {
         BeanInfoRegistration var0 = BeanInfoAccessFactory.getBeanInfoRegistration();
         var0.registerBeanInfoFactoryClass("weblogic.management.runtime.BeanInfoFactory", (ClassLoader)null);
         var0.registerBeanInfoFactoryClass("weblogic.management.configuration.BeanInfoFactory", (ClassLoader)null);
         var0.registerBeanInfoFactoryClass("weblogic.j2ee.descriptor.BeanInfoFactory", (ClassLoader)null);
         var0.registerBeanInfoFactoryClass("weblogic.management.security.WLMANAGEMENTBeanInfoFactory", (ClassLoader)null);
         var0.registerBeanInfoFactoryClass("weblogic.diagnostics.descriptor.BeanInfoFactory", (ClassLoader)null);
         var0.registerBeanInfoFactoryClass("weblogic.management.mbeanservers.internal.BeanInfoFactory", (ClassLoader)null);
         var0.registerBeanInfoFactoryClass("kodo.conf.descriptor.BeanInfoFactory", (ClassLoader)null);
         var0.registerBeanInfoFactoryClass("weblogic.diagnostics.accessor.BeanInfoFactory", (ClassLoader)null);
         var0.registerBeanInfoFactoryClass("weblogic.diagnostics.accessor.ExtraBeanInfoFactory", (ClassLoader)null);
         var0.registerBeanInfoFactoryClass("com.bea.wls.redef.runtime.BeanInfoFactory", (ClassLoader)null);
         var0.discoverBeanInfoFactories(DescriptorClassLoader.getClassLoader());
      }
   }
}
