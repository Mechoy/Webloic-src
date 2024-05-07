package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class JMSBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(JMSBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("Version", Class.forName("weblogic.j2ee.descriptor.wl.JMSBeanDConfig"), "getVersion", "setVersion");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("Notes", Class.forName("weblogic.j2ee.descriptor.wl.JMSBeanDConfig"), "getNotes", "setNotes");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("Quotas", Class.forName("weblogic.j2ee.descriptor.wl.JMSBeanDConfig"), "getQuotas", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("Templates", Class.forName("weblogic.j2ee.descriptor.wl.JMSBeanDConfig"), "getTemplates", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("DestinationKeys", Class.forName("weblogic.j2ee.descriptor.wl.JMSBeanDConfig"), "getDestinationKeys", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("ConnectionFactories", Class.forName("weblogic.j2ee.descriptor.wl.JMSBeanDConfig"), "getConnectionFactories", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("ForeignServers", Class.forName("weblogic.j2ee.descriptor.wl.JMSBeanDConfig"), "getForeignServers", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("Queues", Class.forName("weblogic.j2ee.descriptor.wl.JMSBeanDConfig"), "getQueues", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("Topics", Class.forName("weblogic.j2ee.descriptor.wl.JMSBeanDConfig"), "getTopics", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("DistributedQueues", Class.forName("weblogic.j2ee.descriptor.wl.JMSBeanDConfig"), "getDistributedQueues", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("DistributedTopics", Class.forName("weblogic.j2ee.descriptor.wl.JMSBeanDConfig"), "getDistributedTopics", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("UniformDistributedQueues", Class.forName("weblogic.j2ee.descriptor.wl.JMSBeanDConfig"), "getUniformDistributedQueues", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("UniformDistributedTopics", Class.forName("weblogic.j2ee.descriptor.wl.JMSBeanDConfig"), "getUniformDistributedTopics", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("SAFImportedDestinations", Class.forName("weblogic.j2ee.descriptor.wl.JMSBeanDConfig"), "getSAFImportedDestinations", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("SAFRemoteContexts", Class.forName("weblogic.j2ee.descriptor.wl.JMSBeanDConfig"), "getSAFRemoteContexts", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("SAFErrorHandlings", Class.forName("weblogic.j2ee.descriptor.wl.JMSBeanDConfig"), "getSAFErrorHandlings", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("SecondaryDescriptors", Class.forName("weblogic.j2ee.descriptor.wl.JMSBeanDConfig"), "getSecondaryDescriptors", (String)null);
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
            throw new AssertionError("Failed to create PropertyDescriptors for JMSBeanDConfigBeanInfo");
         }
      }
   }
}
