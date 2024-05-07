package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class AdminObjectInstanceBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(AdminObjectInstanceBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("JNDIName", Class.forName("weblogic.j2ee.descriptor.wl.AdminObjectInstanceBeanDConfig"), "getJNDIName", "setJNDIName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", true);
            var1.setValue("configurable", false);
            var1.setValue("key", true);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ResourceLink", Class.forName("weblogic.j2ee.descriptor.wl.AdminObjectInstanceBeanDConfig"), "getResourceLink", "setResourceLink");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Properties", Class.forName("weblogic.j2ee.descriptor.wl.AdminObjectInstanceBeanDConfig"), "getProperties", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Id", Class.forName("weblogic.j2ee.descriptor.wl.AdminObjectInstanceBeanDConfig"), "getId", "setId");
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
            throw new AssertionError("Failed to create PropertyDescriptors for AdminObjectInstanceBeanDConfigBeanInfo");
         }
      }
   }
}
