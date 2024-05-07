package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class StatefulSessionDescriptorBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(StatefulSessionDescriptorBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("StatefulSessionCache", Class.forName("weblogic.j2ee.descriptor.wl.StatefulSessionDescriptorBeanDConfig"), "getStatefulSessionCache", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("PersistentStoreDir", Class.forName("weblogic.j2ee.descriptor.wl.StatefulSessionDescriptorBeanDConfig"), "getPersistentStoreDir", "setPersistentStoreDir");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("StatefulSessionClustering", Class.forName("weblogic.j2ee.descriptor.wl.StatefulSessionDescriptorBeanDConfig"), "getStatefulSessionClustering", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("AllowConcurrentCalls", Class.forName("weblogic.j2ee.descriptor.wl.StatefulSessionDescriptorBeanDConfig"), "isAllowConcurrentCalls", "setAllowConcurrentCalls");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("AllowRemoveDuringTransaction", Class.forName("weblogic.j2ee.descriptor.wl.StatefulSessionDescriptorBeanDConfig"), "isAllowRemoveDuringTransaction", "setAllowRemoveDuringTransaction");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("BusinessInterfaceJndiNameMaps", Class.forName("weblogic.j2ee.descriptor.wl.StatefulSessionDescriptorBeanDConfig"), "getBusinessInterfaceJndiNameMaps", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Id", Class.forName("weblogic.j2ee.descriptor.wl.StatefulSessionDescriptorBeanDConfig"), "getId", "setId");
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
            throw new AssertionError("Failed to create PropertyDescriptors for StatefulSessionDescriptorBeanDConfigBeanInfo");
         }
      }
   }
}
