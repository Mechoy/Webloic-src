package weblogic.diagnostics.descriptor;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class WLDFInstrumentationMonitorBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(WLDFInstrumentationMonitorBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("Description", Class.forName("weblogic.diagnostics.descriptor.WLDFInstrumentationMonitorBeanDConfig"), "getDescription", "setDescription");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("Enabled", Class.forName("weblogic.diagnostics.descriptor.WLDFInstrumentationMonitorBeanDConfig"), "isEnabled", "setEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("DyeMask", Class.forName("weblogic.diagnostics.descriptor.WLDFInstrumentationMonitorBeanDConfig"), "getDyeMask", "setDyeMask");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("DyeFilteringEnabled", Class.forName("weblogic.diagnostics.descriptor.WLDFInstrumentationMonitorBeanDConfig"), "isDyeFilteringEnabled", "setDyeFilteringEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("Properties", Class.forName("weblogic.diagnostics.descriptor.WLDFInstrumentationMonitorBeanDConfig"), "getProperties", "setProperties");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("Actions", Class.forName("weblogic.diagnostics.descriptor.WLDFInstrumentationMonitorBeanDConfig"), "getActions", "setActions");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("LocationType", Class.forName("weblogic.diagnostics.descriptor.WLDFInstrumentationMonitorBeanDConfig"), "getLocationType", "setLocationType");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Pointcut", Class.forName("weblogic.diagnostics.descriptor.WLDFInstrumentationMonitorBeanDConfig"), "getPointcut", "setPointcut");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Includes", Class.forName("weblogic.diagnostics.descriptor.WLDFInstrumentationMonitorBeanDConfig"), "getIncludes", "setIncludes");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("Excludes", Class.forName("weblogic.diagnostics.descriptor.WLDFInstrumentationMonitorBeanDConfig"), "getExcludes", "setExcludes");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            pds = (PropertyDescriptor[])((PropertyDescriptor[])var2.toArray(new PropertyDescriptor[0]));
            return pds;
         } catch (Throwable var4) {
            var4.printStackTrace();
            throw new AssertionError("Failed to create PropertyDescriptors for WLDFInstrumentationMonitorBeanDConfigBeanInfo");
         }
      }
   }
}
