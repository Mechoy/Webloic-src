package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class EntityCacheBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(EntityCacheBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("MaxBeansInCache", Class.forName("weblogic.j2ee.descriptor.wl.EntityCacheBeanDConfig"), "getMaxBeansInCache", "setMaxBeansInCache");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("MaxQueriesInCache", Class.forName("weblogic.j2ee.descriptor.wl.EntityCacheBeanDConfig"), "getMaxQueriesInCache", "setMaxQueriesInCache");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("IdleTimeoutSeconds", Class.forName("weblogic.j2ee.descriptor.wl.EntityCacheBeanDConfig"), "getIdleTimeoutSeconds", "setIdleTimeoutSeconds");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("ReadTimeoutSeconds", Class.forName("weblogic.j2ee.descriptor.wl.EntityCacheBeanDConfig"), "getReadTimeoutSeconds", "setReadTimeoutSeconds");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("ConcurrencyStrategy", Class.forName("weblogic.j2ee.descriptor.wl.EntityCacheBeanDConfig"), "getConcurrencyStrategy", "setConcurrencyStrategy");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("CacheBetweenTransactions", Class.forName("weblogic.j2ee.descriptor.wl.EntityCacheBeanDConfig"), "isCacheBetweenTransactions", "setCacheBetweenTransactions");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("DisableReadyInstances", Class.forName("weblogic.j2ee.descriptor.wl.EntityCacheBeanDConfig"), "isDisableReadyInstances", "setDisableReadyInstances");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Id", Class.forName("weblogic.j2ee.descriptor.wl.EntityCacheBeanDConfig"), "getId", "setId");
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
            throw new AssertionError("Failed to create PropertyDescriptors for EntityCacheBeanDConfigBeanInfo");
         }
      }
   }
}
