package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class ReliabilityConfigBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(ReliabilityConfigBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("Customized", Class.forName("weblogic.j2ee.descriptor.wl.ReliabilityConfigBeanDConfig"), "isCustomized", "setCustomized");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("InactivityTimeout", Class.forName("weblogic.j2ee.descriptor.wl.ReliabilityConfigBeanDConfig"), "getInactivityTimeout", "setInactivityTimeout");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("BaseRetransmissionInterval", Class.forName("weblogic.j2ee.descriptor.wl.ReliabilityConfigBeanDConfig"), "getBaseRetransmissionInterval", "setBaseRetransmissionInterval");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("RetransmissionExponentialBackoff", Class.forName("weblogic.j2ee.descriptor.wl.ReliabilityConfigBeanDConfig"), "getRetransmissionExponentialBackoff", "setRetransmissionExponentialBackoff");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("NonBufferedSource", Class.forName("weblogic.j2ee.descriptor.wl.ReliabilityConfigBeanDConfig"), "getNonBufferedSource", "setNonBufferedSource");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("AcknowledgementInterval", Class.forName("weblogic.j2ee.descriptor.wl.ReliabilityConfigBeanDConfig"), "getAcknowledgementInterval", "setAcknowledgementInterval");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("SequenceExpiration", Class.forName("weblogic.j2ee.descriptor.wl.ReliabilityConfigBeanDConfig"), "getSequenceExpiration", "setSequenceExpiration");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("BufferRetryCount", Class.forName("weblogic.j2ee.descriptor.wl.ReliabilityConfigBeanDConfig"), "getBufferRetryCount", "setBufferRetryCount");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("BufferRetryDelay", Class.forName("weblogic.j2ee.descriptor.wl.ReliabilityConfigBeanDConfig"), "getBufferRetryDelay", "setBufferRetryDelay");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("NonBufferedDestination", Class.forName("weblogic.j2ee.descriptor.wl.ReliabilityConfigBeanDConfig"), "getNonBufferedDestination", "setNonBufferedDestination");
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
            throw new AssertionError("Failed to create PropertyDescriptors for ReliabilityConfigBeanDConfigBeanInfo");
         }
      }
   }
}
