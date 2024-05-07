package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class SAFImportedDestinationsBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(SAFImportedDestinationsBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("SAFQueues", Class.forName("weblogic.j2ee.descriptor.wl.SAFImportedDestinationsBeanDConfig"), "getSAFQueues", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("SAFTopics", Class.forName("weblogic.j2ee.descriptor.wl.SAFImportedDestinationsBeanDConfig"), "getSAFTopics", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("JNDIPrefix", Class.forName("weblogic.j2ee.descriptor.wl.SAFImportedDestinationsBeanDConfig"), "getJNDIPrefix", "setJNDIPrefix");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("SAFRemoteContext", Class.forName("weblogic.j2ee.descriptor.wl.SAFImportedDestinationsBeanDConfig"), "getSAFRemoteContext", "setSAFRemoteContext");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("SAFErrorHandling", Class.forName("weblogic.j2ee.descriptor.wl.SAFImportedDestinationsBeanDConfig"), "getSAFErrorHandling", "setSAFErrorHandling");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("TimeToLiveDefault", Class.forName("weblogic.j2ee.descriptor.wl.SAFImportedDestinationsBeanDConfig"), "getTimeToLiveDefault", "setTimeToLiveDefault");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("UseSAFTimeToLiveDefault", Class.forName("weblogic.j2ee.descriptor.wl.SAFImportedDestinationsBeanDConfig"), "isUseSAFTimeToLiveDefault", "setUseSAFTimeToLiveDefault");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("UnitOfOrderRouting", Class.forName("weblogic.j2ee.descriptor.wl.SAFImportedDestinationsBeanDConfig"), "getUnitOfOrderRouting", "setUnitOfOrderRouting");
            var1.setValue("dependency", false);
            var1.setValue("declaration", true);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("MessageLoggingParams", Class.forName("weblogic.j2ee.descriptor.wl.SAFImportedDestinationsBeanDConfig"), "getMessageLoggingParams", (String)null);
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
            throw new AssertionError("Failed to create PropertyDescriptors for SAFImportedDestinationsBeanDConfigBeanInfo");
         }
      }
   }
}
