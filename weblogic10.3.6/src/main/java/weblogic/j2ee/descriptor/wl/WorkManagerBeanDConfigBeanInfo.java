package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class WorkManagerBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(WorkManagerBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("Name", Class.forName("weblogic.j2ee.descriptor.wl.WorkManagerBeanDConfig"), "getName", "setName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", true);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ResponseTimeRequestClass", Class.forName("weblogic.j2ee.descriptor.wl.WorkManagerBeanDConfig"), "getResponseTimeRequestClass", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("FairShareRequestClass", Class.forName("weblogic.j2ee.descriptor.wl.WorkManagerBeanDConfig"), "getFairShareRequestClass", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ContextRequestClass", Class.forName("weblogic.j2ee.descriptor.wl.WorkManagerBeanDConfig"), "getContextRequestClass", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("RequestClassName", Class.forName("weblogic.j2ee.descriptor.wl.WorkManagerBeanDConfig"), "getRequestClassName", "setRequestClassName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("MinThreadsConstraint", Class.forName("weblogic.j2ee.descriptor.wl.WorkManagerBeanDConfig"), "getMinThreadsConstraint", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("MinThreadsConstraintName", Class.forName("weblogic.j2ee.descriptor.wl.WorkManagerBeanDConfig"), "getMinThreadsConstraintName", "setMinThreadsConstraintName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("MaxThreadsConstraint", Class.forName("weblogic.j2ee.descriptor.wl.WorkManagerBeanDConfig"), "getMaxThreadsConstraint", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("MaxThreadsConstraintName", Class.forName("weblogic.j2ee.descriptor.wl.WorkManagerBeanDConfig"), "getMaxThreadsConstraintName", "setMaxThreadsConstraintName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Capacity", Class.forName("weblogic.j2ee.descriptor.wl.WorkManagerBeanDConfig"), "getCapacity", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("CapacityName", Class.forName("weblogic.j2ee.descriptor.wl.WorkManagerBeanDConfig"), "getCapacityName", "setCapacityName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("WorkManagerShutdownTrigger", Class.forName("weblogic.j2ee.descriptor.wl.WorkManagerBeanDConfig"), "getWorkManagerShutdownTrigger", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("IgnoreStuckThreads", Class.forName("weblogic.j2ee.descriptor.wl.WorkManagerBeanDConfig"), "getIgnoreStuckThreads", "setIgnoreStuckThreads");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Id", Class.forName("weblogic.j2ee.descriptor.wl.WorkManagerBeanDConfig"), "getId", "setId");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            pds = (PropertyDescriptor[])((PropertyDescriptor[])var2.toArray(new PropertyDescriptor[0]));
            return pds;
         } catch (Throwable var4) {
            var4.printStackTrace();
            throw new AssertionError("Failed to create PropertyDescriptors for WorkManagerBeanDConfigBeanInfo");
         }
      }
   }
}
