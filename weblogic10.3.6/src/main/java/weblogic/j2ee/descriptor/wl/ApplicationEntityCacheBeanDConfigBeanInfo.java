package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class ApplicationEntityCacheBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(ApplicationEntityCacheBeanDConfig.class);
   static PropertyDescriptor[] pds = null;

   public BeanDescriptor getBeanDescriptor() {
      return this.bd;
   }

   public PropertyDescriptor[] getPropertyDescriptors() {
      if (pds != null) {
         return pds;
      } else {
         ArrayList var2 = new ArrayList();

         try {
            PropertyDescriptor var1 = new PropertyDescriptor("EntityCacheName", Class.forName("weblogic.j2ee.descriptor.wl.ApplicationEntityCacheBeanDConfig"), "getEntityCacheName", "setEntityCacheName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", true);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("MaxBeansInCache", Class.forName("weblogic.j2ee.descriptor.wl.ApplicationEntityCacheBeanDConfig"), "getMaxBeansInCache", "setMaxBeansInCache");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("MaxCacheSize", Class.forName("weblogic.j2ee.descriptor.wl.ApplicationEntityCacheBeanDConfig"), "getMaxCacheSize", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("MaxQueriesInCache", Class.forName("weblogic.j2ee.descriptor.wl.ApplicationEntityCacheBeanDConfig"), "getMaxQueriesInCache", "setMaxQueriesInCache");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("CachingStrategy", Class.forName("weblogic.j2ee.descriptor.wl.ApplicationEntityCacheBeanDConfig"), "getCachingStrategy", "setCachingStrategy");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            pds = (PropertyDescriptor[])((PropertyDescriptor[])var2.toArray(new PropertyDescriptor[0]));
            return pds;
         } catch (Throwable var4) {
            var4.printStackTrace();
            throw new AssertionError("Failed to create PropertyDescriptors for ApplicationEntityCacheBeanDConfigBeanInfo");
         }
      }
   }
}
