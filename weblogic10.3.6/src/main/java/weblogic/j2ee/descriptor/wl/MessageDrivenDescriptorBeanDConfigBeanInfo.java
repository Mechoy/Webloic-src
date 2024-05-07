package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class MessageDrivenDescriptorBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(MessageDrivenDescriptorBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("Pool", Class.forName("weblogic.j2ee.descriptor.wl.MessageDrivenDescriptorBeanDConfig"), "getPool", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("TimerDescriptor", Class.forName("weblogic.j2ee.descriptor.wl.MessageDrivenDescriptorBeanDConfig"), "getTimerDescriptor", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ResourceAdapterJNDIName", Class.forName("weblogic.j2ee.descriptor.wl.MessageDrivenDescriptorBeanDConfig"), "getResourceAdapterJNDIName", "setResourceAdapterJNDIName");
            var1.setValue("dependency", true);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("DestinationJNDIName", Class.forName("weblogic.j2ee.descriptor.wl.MessageDrivenDescriptorBeanDConfig"), "getDestinationJNDIName", "setDestinationJNDIName");
            var1.setValue("dependency", true);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("InitialContextFactory", Class.forName("weblogic.j2ee.descriptor.wl.MessageDrivenDescriptorBeanDConfig"), "getInitialContextFactory", "setInitialContextFactory");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("ProviderUrl", Class.forName("weblogic.j2ee.descriptor.wl.MessageDrivenDescriptorBeanDConfig"), "getProviderUrl", "setProviderUrl");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("ConnectionFactoryJNDIName", Class.forName("weblogic.j2ee.descriptor.wl.MessageDrivenDescriptorBeanDConfig"), "getConnectionFactoryJNDIName", "setConnectionFactoryJNDIName");
            var1.setValue("dependency", true);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("DestinationResourceLink", Class.forName("weblogic.j2ee.descriptor.wl.MessageDrivenDescriptorBeanDConfig"), "getDestinationResourceLink", "setDestinationResourceLink");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("ConnectionFactoryResourceLink", Class.forName("weblogic.j2ee.descriptor.wl.MessageDrivenDescriptorBeanDConfig"), "getConnectionFactoryResourceLink", "setConnectionFactoryResourceLink");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("JmsPollingIntervalSeconds", Class.forName("weblogic.j2ee.descriptor.wl.MessageDrivenDescriptorBeanDConfig"), "getJmsPollingIntervalSeconds", "setJmsPollingIntervalSeconds");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("JmsClientId", Class.forName("weblogic.j2ee.descriptor.wl.MessageDrivenDescriptorBeanDConfig"), "getJmsClientId", "setJmsClientId");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("GenerateUniqueJmsClientId", Class.forName("weblogic.j2ee.descriptor.wl.MessageDrivenDescriptorBeanDConfig"), "isGenerateUniqueJmsClientId", "setGenerateUniqueJmsClientId");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("DurableSubscriptionDeletion", Class.forName("weblogic.j2ee.descriptor.wl.MessageDrivenDescriptorBeanDConfig"), "isDurableSubscriptionDeletion", "setDurableSubscriptionDeletion");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("MaxMessagesInTransaction", Class.forName("weblogic.j2ee.descriptor.wl.MessageDrivenDescriptorBeanDConfig"), "getMaxMessagesInTransaction", "setMaxMessagesInTransaction");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("DistributedDestinationConnection", Class.forName("weblogic.j2ee.descriptor.wl.MessageDrivenDescriptorBeanDConfig"), "getDistributedDestinationConnection", "setDistributedDestinationConnection");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("Use81StylePolling", Class.forName("weblogic.j2ee.descriptor.wl.MessageDrivenDescriptorBeanDConfig"), "isUse81StylePolling", "setUse81StylePolling");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("InitSuspendSeconds", Class.forName("weblogic.j2ee.descriptor.wl.MessageDrivenDescriptorBeanDConfig"), "getInitSuspendSeconds", "setInitSuspendSeconds");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("MaxSuspendSeconds", Class.forName("weblogic.j2ee.descriptor.wl.MessageDrivenDescriptorBeanDConfig"), "getMaxSuspendSeconds", "setMaxSuspendSeconds");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("SecurityPlugin", Class.forName("weblogic.j2ee.descriptor.wl.MessageDrivenDescriptorBeanDConfig"), "getSecurityPlugin", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Id", Class.forName("weblogic.j2ee.descriptor.wl.MessageDrivenDescriptorBeanDConfig"), "getId", "setId");
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
            throw new AssertionError("Failed to create PropertyDescriptors for MessageDrivenDescriptorBeanDConfigBeanInfo");
         }
      }
   }
}
