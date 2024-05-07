package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class ArrayMemberBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(ArrayMemberBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("MemberName", Class.forName("weblogic.j2ee.descriptor.wl.ArrayMemberBeanDConfig"), "getMemberName", "setMemberName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", true);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("MemberValues", Class.forName("weblogic.j2ee.descriptor.wl.ArrayMemberBeanDConfig"), "getMemberValues", "setMemberValues");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("OverrideValues", Class.forName("weblogic.j2ee.descriptor.wl.ArrayMemberBeanDConfig"), "getOverrideValues", "setOverrideValues");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("RequiresEncryption", Class.forName("weblogic.j2ee.descriptor.wl.ArrayMemberBeanDConfig"), "getRequiresEncryption", "setRequiresEncryption");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("CleartextOverrideValues", Class.forName("weblogic.j2ee.descriptor.wl.ArrayMemberBeanDConfig"), "getCleartextOverrideValues", "setCleartextOverrideValues");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("SecuredOverrideValue", Class.forName("weblogic.j2ee.descriptor.wl.ArrayMemberBeanDConfig"), "getSecuredOverrideValue", "setSecuredOverrideValue");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("SecuredOverrideValueEncrypted", Class.forName("weblogic.j2ee.descriptor.wl.ArrayMemberBeanDConfig"), "getSecuredOverrideValueEncrypted", "setSecuredOverrideValueEncrypted");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("ShortDescription", Class.forName("weblogic.j2ee.descriptor.wl.ArrayMemberBeanDConfig"), "getShortDescription", (String)null);
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
            throw new AssertionError("Failed to create PropertyDescriptors for ArrayMemberBeanDConfigBeanInfo");
         }
      }
   }
}
