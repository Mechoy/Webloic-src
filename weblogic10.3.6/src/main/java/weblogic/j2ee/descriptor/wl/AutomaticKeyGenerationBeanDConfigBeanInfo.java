package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class AutomaticKeyGenerationBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(AutomaticKeyGenerationBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("GeneratorType", Class.forName("weblogic.j2ee.descriptor.wl.AutomaticKeyGenerationBeanDConfig"), "getGeneratorType", "setGeneratorType");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("GeneratorName", Class.forName("weblogic.j2ee.descriptor.wl.AutomaticKeyGenerationBeanDConfig"), "getGeneratorName", "setGeneratorName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("KeyCacheSize", Class.forName("weblogic.j2ee.descriptor.wl.AutomaticKeyGenerationBeanDConfig"), "getKeyCacheSize", "setKeyCacheSize");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("Id", Class.forName("weblogic.j2ee.descriptor.wl.AutomaticKeyGenerationBeanDConfig"), "getId", "setId");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("SelectFirstSequenceKeyBeforeUpdate", Class.forName("weblogic.j2ee.descriptor.wl.AutomaticKeyGenerationBeanDConfig"), "getSelectFirstSequenceKeyBeforeUpdate", "setSelectFirstSequenceKeyBeforeUpdate");
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
            throw new AssertionError("Failed to create PropertyDescriptors for AutomaticKeyGenerationBeanDConfigBeanInfo");
         }
      }
   }
}
