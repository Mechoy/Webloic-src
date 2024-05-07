package weblogic.diagnostics.descriptor;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class WLDFHarvestedTypeBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(WLDFHarvestedTypeBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("Name", Class.forName("weblogic.diagnostics.descriptor.WLDFHarvestedTypeBeanDConfig"), "getName", "setName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Enabled", Class.forName("weblogic.diagnostics.descriptor.WLDFHarvestedTypeBeanDConfig"), "isEnabled", "setEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("KnownType", Class.forName("weblogic.diagnostics.descriptor.WLDFHarvestedTypeBeanDConfig"), "isKnownType", "setKnownType");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("HarvestedAttributes", Class.forName("weblogic.diagnostics.descriptor.WLDFHarvestedTypeBeanDConfig"), "getHarvestedAttributes", "setHarvestedAttributes");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("HarvestedInstances", Class.forName("weblogic.diagnostics.descriptor.WLDFHarvestedTypeBeanDConfig"), "getHarvestedInstances", "setHarvestedInstances");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("Namespace", Class.forName("weblogic.diagnostics.descriptor.WLDFHarvestedTypeBeanDConfig"), "getNamespace", "setNamespace");
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
            throw new AssertionError("Failed to create PropertyDescriptors for WLDFHarvestedTypeBeanDConfigBeanInfo");
         }
      }
   }
}
