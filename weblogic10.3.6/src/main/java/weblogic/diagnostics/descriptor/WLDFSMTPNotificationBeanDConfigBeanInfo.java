package weblogic.diagnostics.descriptor;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class WLDFSMTPNotificationBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(WLDFSMTPNotificationBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("MailSessionJNDIName", Class.forName("weblogic.diagnostics.descriptor.WLDFSMTPNotificationBeanDConfig"), "getMailSessionJNDIName", "setMailSessionJNDIName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("Subject", Class.forName("weblogic.diagnostics.descriptor.WLDFSMTPNotificationBeanDConfig"), "getSubject", "setSubject");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("Body", Class.forName("weblogic.diagnostics.descriptor.WLDFSMTPNotificationBeanDConfig"), "getBody", "setBody");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("Recipients", Class.forName("weblogic.diagnostics.descriptor.WLDFSMTPNotificationBeanDConfig"), "getRecipients", "setRecipients");
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
            throw new AssertionError("Failed to create PropertyDescriptors for WLDFSMTPNotificationBeanDConfigBeanInfo");
         }
      }
   }
}
