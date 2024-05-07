package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class AnnotationManifestBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(AnnotationManifestBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("AnnotatedClasses", Class.forName("weblogic.j2ee.descriptor.wl.AnnotationManifestBeanDConfig"), "getAnnotatedClasses", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("AnnotationDefinitions", Class.forName("weblogic.j2ee.descriptor.wl.AnnotationManifestBeanDConfig"), "getAnnotationDefinitions", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("EnumDefinitions", Class.forName("weblogic.j2ee.descriptor.wl.AnnotationManifestBeanDConfig"), "getEnumDefinitions", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("UpdateCount", Class.forName("weblogic.j2ee.descriptor.wl.AnnotationManifestBeanDConfig"), "getUpdateCount", "setUpdateCount");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("Version", Class.forName("weblogic.j2ee.descriptor.wl.AnnotationManifestBeanDConfig"), "getVersion", "setVersion");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("SecondaryDescriptors", Class.forName("weblogic.j2ee.descriptor.wl.AnnotationManifestBeanDConfig"), "getSecondaryDescriptors", (String)null);
            var1.setValue("dependency", Boolean.FALSE);
            var1.setValue("declaration", Boolean.FALSE);
            var1.setValue("configurable", Boolean.FALSE);
            var1.setValue("key", Boolean.FALSE);
            var1.setValue("dynamic", Boolean.FALSE);
            var2.add(var1);
            pds = (PropertyDescriptor[])((PropertyDescriptor[])var2.toArray(new PropertyDescriptor[0]));
            return pds;
         } catch (Throwable var4) {
            var4.printStackTrace();
            throw new AssertionError("Failed to create PropertyDescriptors for AnnotationManifestBeanDConfigBeanInfo");
         }
      }
   }
}
