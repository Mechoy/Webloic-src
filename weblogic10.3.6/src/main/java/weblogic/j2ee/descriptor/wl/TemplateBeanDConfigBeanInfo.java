package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class TemplateBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(TemplateBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("DestinationKeys", Class.forName("weblogic.j2ee.descriptor.wl.TemplateBeanDConfig"), "getDestinationKeys", "setDestinationKeys");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Thresholds", Class.forName("weblogic.j2ee.descriptor.wl.TemplateBeanDConfig"), "getThresholds", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("DeliveryParamsOverrides", Class.forName("weblogic.j2ee.descriptor.wl.TemplateBeanDConfig"), "getDeliveryParamsOverrides", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("DeliveryFailureParams", Class.forName("weblogic.j2ee.descriptor.wl.TemplateBeanDConfig"), "getDeliveryFailureParams", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("MessageLoggingParams", Class.forName("weblogic.j2ee.descriptor.wl.TemplateBeanDConfig"), "getMessageLoggingParams", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("AttachSender", Class.forName("weblogic.j2ee.descriptor.wl.TemplateBeanDConfig"), "getAttachSender", "setAttachSender");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("ProductionPausedAtStartup", Class.forName("weblogic.j2ee.descriptor.wl.TemplateBeanDConfig"), "isProductionPausedAtStartup", "setProductionPausedAtStartup");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("InsertionPausedAtStartup", Class.forName("weblogic.j2ee.descriptor.wl.TemplateBeanDConfig"), "isInsertionPausedAtStartup", "setInsertionPausedAtStartup");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ConsumptionPausedAtStartup", Class.forName("weblogic.j2ee.descriptor.wl.TemplateBeanDConfig"), "isConsumptionPausedAtStartup", "setConsumptionPausedAtStartup");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("MaximumMessageSize", Class.forName("weblogic.j2ee.descriptor.wl.TemplateBeanDConfig"), "getMaximumMessageSize", "setMaximumMessageSize");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("Quota", Class.forName("weblogic.j2ee.descriptor.wl.TemplateBeanDConfig"), "getQuota", "setQuota");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("DefaultUnitOfOrder", Class.forName("weblogic.j2ee.descriptor.wl.TemplateBeanDConfig"), "isDefaultUnitOfOrder", "setDefaultUnitOfOrder");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("SafExportPolicy", Class.forName("weblogic.j2ee.descriptor.wl.TemplateBeanDConfig"), "getSafExportPolicy", "setSafExportPolicy");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Multicast", Class.forName("weblogic.j2ee.descriptor.wl.TemplateBeanDConfig"), "getMulticast", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("GroupParams", Class.forName("weblogic.j2ee.descriptor.wl.TemplateBeanDConfig"), "getGroupParams", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("MessagingPerformancePreference", Class.forName("weblogic.j2ee.descriptor.wl.TemplateBeanDConfig"), "getMessagingPerformancePreference", "setMessagingPerformancePreference");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("UnitOfWorkHandlingPolicy", Class.forName("weblogic.j2ee.descriptor.wl.TemplateBeanDConfig"), "getUnitOfWorkHandlingPolicy", "setUnitOfWorkHandlingPolicy");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("IncompleteWorkExpirationTime", Class.forName("weblogic.j2ee.descriptor.wl.TemplateBeanDConfig"), "getIncompleteWorkExpirationTime", "setIncompleteWorkExpirationTime");
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
            throw new AssertionError("Failed to create PropertyDescriptors for TemplateBeanDConfigBeanInfo");
         }
      }
   }
}
