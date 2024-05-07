package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class SAFDestinationBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(SAFDestinationBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("RemoteJNDIName", Class.forName("weblogic.j2ee.descriptor.wl.SAFDestinationBeanDConfig"), "getRemoteJNDIName", "setRemoteJNDIName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("LocalJNDIName", Class.forName("weblogic.j2ee.descriptor.wl.SAFDestinationBeanDConfig"), "getLocalJNDIName", "setLocalJNDIName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("PersistentQos", Class.forName("weblogic.j2ee.descriptor.wl.SAFDestinationBeanDConfig"), "getPersistentQos", "setPersistentQos");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("NonPersistentQos", Class.forName("weblogic.j2ee.descriptor.wl.SAFDestinationBeanDConfig"), "getNonPersistentQos", "setNonPersistentQos");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("SAFErrorHandling", Class.forName("weblogic.j2ee.descriptor.wl.SAFDestinationBeanDConfig"), "getSAFErrorHandling", "setSAFErrorHandling");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("TimeToLiveDefault", Class.forName("weblogic.j2ee.descriptor.wl.SAFDestinationBeanDConfig"), "getTimeToLiveDefault", "setTimeToLiveDefault");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("UseSAFTimeToLiveDefault", Class.forName("weblogic.j2ee.descriptor.wl.SAFDestinationBeanDConfig"), "isUseSAFTimeToLiveDefault", "setUseSAFTimeToLiveDefault");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("UnitOfOrderRouting", Class.forName("weblogic.j2ee.descriptor.wl.SAFDestinationBeanDConfig"), "getUnitOfOrderRouting", "setUnitOfOrderRouting");
            var1.setValue("dependency", false);
            var1.setValue("declaration", true);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("MessageLoggingParams", Class.forName("weblogic.j2ee.descriptor.wl.SAFDestinationBeanDConfig"), "getMessageLoggingParams", (String)null);
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
            throw new AssertionError("Failed to create PropertyDescriptors for SAFDestinationBeanDConfigBeanInfo");
         }
      }
   }
}
