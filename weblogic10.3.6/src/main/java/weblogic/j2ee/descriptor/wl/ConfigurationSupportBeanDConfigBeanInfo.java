package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class ConfigurationSupportBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(ConfigurationSupportBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("BaseRootElement", Class.forName("weblogic.j2ee.descriptor.wl.ConfigurationSupportBeanDConfig"), "getBaseRootElement", "setBaseRootElement");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ConfigRootElement", Class.forName("weblogic.j2ee.descriptor.wl.ConfigurationSupportBeanDConfig"), "getConfigRootElement", "setConfigRootElement");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("BaseNamespace", Class.forName("weblogic.j2ee.descriptor.wl.ConfigurationSupportBeanDConfig"), "getBaseNamespace", "setBaseNamespace");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ConfigNamespace", Class.forName("weblogic.j2ee.descriptor.wl.ConfigurationSupportBeanDConfig"), "getConfigNamespace", "setConfigNamespace");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("BaseUri", Class.forName("weblogic.j2ee.descriptor.wl.ConfigurationSupportBeanDConfig"), "getBaseUri", "setBaseUri");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ConfigUri", Class.forName("weblogic.j2ee.descriptor.wl.ConfigurationSupportBeanDConfig"), "getConfigUri", "setConfigUri");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("BasePackageName", Class.forName("weblogic.j2ee.descriptor.wl.ConfigurationSupportBeanDConfig"), "getBasePackageName", "setBasePackageName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ConfigPackageName", Class.forName("weblogic.j2ee.descriptor.wl.ConfigurationSupportBeanDConfig"), "getConfigPackageName", "setConfigPackageName");
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
            throw new AssertionError("Failed to create PropertyDescriptors for ConfigurationSupportBeanDConfigBeanInfo");
         }
      }
   }
}
