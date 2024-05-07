package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class DeliveryParamsOverridesBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(DeliveryParamsOverridesBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("DeliveryMode", Class.forName("weblogic.j2ee.descriptor.wl.DeliveryParamsOverridesBeanDConfig"), "getDeliveryMode", "setDeliveryMode");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("TimeToDeliver", Class.forName("weblogic.j2ee.descriptor.wl.DeliveryParamsOverridesBeanDConfig"), "getTimeToDeliver", "setTimeToDeliver");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("TimeToLive", Class.forName("weblogic.j2ee.descriptor.wl.DeliveryParamsOverridesBeanDConfig"), "getTimeToLive", "setTimeToLive");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("Priority", Class.forName("weblogic.j2ee.descriptor.wl.DeliveryParamsOverridesBeanDConfig"), "getPriority", "setPriority");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("RedeliveryDelay", Class.forName("weblogic.j2ee.descriptor.wl.DeliveryParamsOverridesBeanDConfig"), "getRedeliveryDelay", "setRedeliveryDelay");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("TemplateBean", Class.forName("weblogic.j2ee.descriptor.wl.DeliveryParamsOverridesBeanDConfig"), "getTemplateBean", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            pds = (PropertyDescriptor[])((PropertyDescriptor[])var2.toArray(new PropertyDescriptor[0]));
            return pds;
         } catch (Throwable var4) {
            var4.printStackTrace();
            throw new AssertionError("Failed to create PropertyDescriptors for DeliveryParamsOverridesBeanDConfigBeanInfo");
         }
      }
   }
}
