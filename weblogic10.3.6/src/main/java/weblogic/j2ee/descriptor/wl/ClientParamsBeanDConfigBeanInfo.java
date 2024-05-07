package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class ClientParamsBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(ClientParamsBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("ClientId", Class.forName("weblogic.j2ee.descriptor.wl.ClientParamsBeanDConfig"), "getClientId", "setClientId");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("ClientIdPolicy", Class.forName("weblogic.j2ee.descriptor.wl.ClientParamsBeanDConfig"), "getClientIdPolicy", "setClientIdPolicy");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("SubscriptionSharingPolicy", Class.forName("weblogic.j2ee.descriptor.wl.ClientParamsBeanDConfig"), "getSubscriptionSharingPolicy", "setSubscriptionSharingPolicy");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("AcknowledgePolicy", Class.forName("weblogic.j2ee.descriptor.wl.ClientParamsBeanDConfig"), "getAcknowledgePolicy", "setAcknowledgePolicy");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("AllowCloseInOnMessage", Class.forName("weblogic.j2ee.descriptor.wl.ClientParamsBeanDConfig"), "isAllowCloseInOnMessage", "setAllowCloseInOnMessage");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("MessagesMaximum", Class.forName("weblogic.j2ee.descriptor.wl.ClientParamsBeanDConfig"), "getMessagesMaximum", "setMessagesMaximum");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("MulticastOverrunPolicy", Class.forName("weblogic.j2ee.descriptor.wl.ClientParamsBeanDConfig"), "getMulticastOverrunPolicy", "setMulticastOverrunPolicy");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("SynchronousPrefetchMode", Class.forName("weblogic.j2ee.descriptor.wl.ClientParamsBeanDConfig"), "getSynchronousPrefetchMode", "setSynchronousPrefetchMode");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("ReconnectPolicy", Class.forName("weblogic.j2ee.descriptor.wl.ClientParamsBeanDConfig"), "getReconnectPolicy", "setReconnectPolicy");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("ReconnectBlockingMillis", Class.forName("weblogic.j2ee.descriptor.wl.ClientParamsBeanDConfig"), "getReconnectBlockingMillis", "setReconnectBlockingMillis");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("TotalReconnectPeriodMillis", Class.forName("weblogic.j2ee.descriptor.wl.ClientParamsBeanDConfig"), "getTotalReconnectPeriodMillis", "setTotalReconnectPeriodMillis");
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
            throw new AssertionError("Failed to create PropertyDescriptors for ClientParamsBeanDConfigBeanInfo");
         }
      }
   }
}
