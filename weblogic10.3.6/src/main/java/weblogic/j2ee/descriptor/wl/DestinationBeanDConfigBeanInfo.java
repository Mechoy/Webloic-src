package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class DestinationBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(DestinationBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("Template", Class.forName("weblogic.j2ee.descriptor.wl.DestinationBeanDConfig"), "getTemplate", "setTemplate");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("DestinationKeys", Class.forName("weblogic.j2ee.descriptor.wl.DestinationBeanDConfig"), "getDestinationKeys", "setDestinationKeys");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Thresholds", Class.forName("weblogic.j2ee.descriptor.wl.DestinationBeanDConfig"), "getThresholds", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("DeliveryParamsOverrides", Class.forName("weblogic.j2ee.descriptor.wl.DestinationBeanDConfig"), "getDeliveryParamsOverrides", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("DeliveryFailureParams", Class.forName("weblogic.j2ee.descriptor.wl.DestinationBeanDConfig"), "getDeliveryFailureParams", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("MessageLoggingParams", Class.forName("weblogic.j2ee.descriptor.wl.DestinationBeanDConfig"), "getMessageLoggingParams", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("AttachSender", Class.forName("weblogic.j2ee.descriptor.wl.DestinationBeanDConfig"), "getAttachSender", "setAttachSender");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("ProductionPausedAtStartup", Class.forName("weblogic.j2ee.descriptor.wl.DestinationBeanDConfig"), "isProductionPausedAtStartup", "setProductionPausedAtStartup");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("InsertionPausedAtStartup", Class.forName("weblogic.j2ee.descriptor.wl.DestinationBeanDConfig"), "isInsertionPausedAtStartup", "setInsertionPausedAtStartup");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ConsumptionPausedAtStartup", Class.forName("weblogic.j2ee.descriptor.wl.DestinationBeanDConfig"), "isConsumptionPausedAtStartup", "setConsumptionPausedAtStartup");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("MaximumMessageSize", Class.forName("weblogic.j2ee.descriptor.wl.DestinationBeanDConfig"), "getMaximumMessageSize", "setMaximumMessageSize");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("Quota", Class.forName("weblogic.j2ee.descriptor.wl.DestinationBeanDConfig"), "getQuota", "setQuota");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("JNDIName", Class.forName("weblogic.j2ee.descriptor.wl.DestinationBeanDConfig"), "getJNDIName", "setJNDIName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("LocalJNDIName", Class.forName("weblogic.j2ee.descriptor.wl.DestinationBeanDConfig"), "getLocalJNDIName", "setLocalJNDIName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("JMSCreateDestinationIdentifier", Class.forName("weblogic.j2ee.descriptor.wl.DestinationBeanDConfig"), "getJMSCreateDestinationIdentifier", "setJMSCreateDestinationIdentifier");
            var1.setValue("dependency", false);
            var1.setValue("declaration", true);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("DefaultUnitOfOrder", Class.forName("weblogic.j2ee.descriptor.wl.DestinationBeanDConfig"), "isDefaultUnitOfOrder", "setDefaultUnitOfOrder");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("SAFExportPolicy", Class.forName("weblogic.j2ee.descriptor.wl.DestinationBeanDConfig"), "getSAFExportPolicy", "setSAFExportPolicy");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("MessagingPerformancePreference", Class.forName("weblogic.j2ee.descriptor.wl.DestinationBeanDConfig"), "getMessagingPerformancePreference", "setMessagingPerformancePreference");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("UnitOfWorkHandlingPolicy", Class.forName("weblogic.j2ee.descriptor.wl.DestinationBeanDConfig"), "getUnitOfWorkHandlingPolicy", "setUnitOfWorkHandlingPolicy");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("IncompleteWorkExpirationTime", Class.forName("weblogic.j2ee.descriptor.wl.DestinationBeanDConfig"), "getIncompleteWorkExpirationTime", "setIncompleteWorkExpirationTime");
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
            throw new AssertionError("Failed to create PropertyDescriptors for DestinationBeanDConfigBeanInfo");
         }
      }
   }
}
