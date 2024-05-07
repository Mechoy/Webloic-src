package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class ForeignJNDIObjectBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(ForeignJNDIObjectBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("LocalJNDIName", Class.forName("weblogic.j2ee.descriptor.wl.ForeignJNDIObjectBeanDConfig"), "getLocalJNDIName", "setLocalJNDIName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", true);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("RemoteJNDIName", Class.forName("weblogic.j2ee.descriptor.wl.ForeignJNDIObjectBeanDConfig"), "getRemoteJNDIName", "setRemoteJNDIName");
            var1.setValue("dependency", true);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            pds = (PropertyDescriptor[])((PropertyDescriptor[])var2.toArray(new PropertyDescriptor[0]));
            return pds;
         } catch (Throwable var4) {
            var4.printStackTrace();
            throw new AssertionError("Failed to create PropertyDescriptors for ForeignJNDIObjectBeanDConfigBeanInfo");
         }
      }
   }
}
