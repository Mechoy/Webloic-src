package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class MemberBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(MemberBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("MemberName", Class.forName("weblogic.j2ee.descriptor.wl.MemberBeanDConfig"), "getMemberName", "setMemberName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", true);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("MemberValue", Class.forName("weblogic.j2ee.descriptor.wl.MemberBeanDConfig"), "getMemberValue", "setMemberValue");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("OverrideValue", Class.forName("weblogic.j2ee.descriptor.wl.MemberBeanDConfig"), "getOverrideValue", "setOverrideValue");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("RequiresEncryption", Class.forName("weblogic.j2ee.descriptor.wl.MemberBeanDConfig"), "getRequiresEncryption", "setRequiresEncryption");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("CleartextOverrideValue", Class.forName("weblogic.j2ee.descriptor.wl.MemberBeanDConfig"), "getCleartextOverrideValue", "setCleartextOverrideValue");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("SecuredOverrideValue", Class.forName("weblogic.j2ee.descriptor.wl.MemberBeanDConfig"), "getSecuredOverrideValue", "setSecuredOverrideValue");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("SecuredOverrideValueEncrypted", Class.forName("weblogic.j2ee.descriptor.wl.MemberBeanDConfig"), "getSecuredOverrideValueEncrypted", "setSecuredOverrideValueEncrypted");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("ShortDescription", Class.forName("weblogic.j2ee.descriptor.wl.MemberBeanDConfig"), "getShortDescription", (String)null);
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
            throw new AssertionError("Failed to create PropertyDescriptors for MemberBeanDConfigBeanInfo");
         }
      }
   }
}
