package weblogic.diagnostics.descriptor;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class WLDFWatchNotificationBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(WLDFWatchNotificationBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("Enabled", Class.forName("weblogic.diagnostics.descriptor.WLDFWatchNotificationBeanDConfig"), "isEnabled", "setEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("Severity", Class.forName("weblogic.diagnostics.descriptor.WLDFWatchNotificationBeanDConfig"), "getSeverity", "setSeverity");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("LogWatchSeverity", Class.forName("weblogic.diagnostics.descriptor.WLDFWatchNotificationBeanDConfig"), "getLogWatchSeverity", "setLogWatchSeverity");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("Watches", Class.forName("weblogic.diagnostics.descriptor.WLDFWatchNotificationBeanDConfig"), "getWatches", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("Notifications", Class.forName("weblogic.diagnostics.descriptor.WLDFWatchNotificationBeanDConfig"), "getNotifications", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ImageNotifications", Class.forName("weblogic.diagnostics.descriptor.WLDFWatchNotificationBeanDConfig"), "getImageNotifications", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("JMSNotifications", Class.forName("weblogic.diagnostics.descriptor.WLDFWatchNotificationBeanDConfig"), "getJMSNotifications", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("JMXNotifications", Class.forName("weblogic.diagnostics.descriptor.WLDFWatchNotificationBeanDConfig"), "getJMXNotifications", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("SMTPNotifications", Class.forName("weblogic.diagnostics.descriptor.WLDFWatchNotificationBeanDConfig"), "getSMTPNotifications", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("SNMPNotifications", Class.forName("weblogic.diagnostics.descriptor.WLDFWatchNotificationBeanDConfig"), "getSNMPNotifications", (String)null);
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
            throw new AssertionError("Failed to create PropertyDescriptors for WLDFWatchNotificationBeanDConfigBeanInfo");
         }
      }
   }
}
