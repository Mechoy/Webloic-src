package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class DefaultSAFAgentBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(DefaultSAFAgentBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("Notes", Class.forName("weblogic.j2ee.descriptor.wl.DefaultSAFAgentBeanDConfig"), "getNotes", "setNotes");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("BytesMaximum", Class.forName("weblogic.j2ee.descriptor.wl.DefaultSAFAgentBeanDConfig"), "getBytesMaximum", "setBytesMaximum");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("MessagesMaximum", Class.forName("weblogic.j2ee.descriptor.wl.DefaultSAFAgentBeanDConfig"), "getMessagesMaximum", "setMessagesMaximum");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("MaximumMessageSize", Class.forName("weblogic.j2ee.descriptor.wl.DefaultSAFAgentBeanDConfig"), "getMaximumMessageSize", "setMaximumMessageSize");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("DefaultRetryDelayBase", Class.forName("weblogic.j2ee.descriptor.wl.DefaultSAFAgentBeanDConfig"), "getDefaultRetryDelayBase", "setDefaultRetryDelayBase");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("DefaultRetryDelayMaximum", Class.forName("weblogic.j2ee.descriptor.wl.DefaultSAFAgentBeanDConfig"), "getDefaultRetryDelayMaximum", "setDefaultRetryDelayMaximum");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("DefaultRetryDelayMultiplier", Class.forName("weblogic.j2ee.descriptor.wl.DefaultSAFAgentBeanDConfig"), "getDefaultRetryDelayMultiplier", "setDefaultRetryDelayMultiplier");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("WindowSize", Class.forName("weblogic.j2ee.descriptor.wl.DefaultSAFAgentBeanDConfig"), "getWindowSize", "setWindowSize");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("LoggingEnabled", Class.forName("weblogic.j2ee.descriptor.wl.DefaultSAFAgentBeanDConfig"), "isLoggingEnabled", "setLoggingEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("DefaultTimeToLive", Class.forName("weblogic.j2ee.descriptor.wl.DefaultSAFAgentBeanDConfig"), "getDefaultTimeToLive", "setDefaultTimeToLive");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("MessageBufferSize", Class.forName("weblogic.j2ee.descriptor.wl.DefaultSAFAgentBeanDConfig"), "getMessageBufferSize", "setMessageBufferSize");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("PagingDirectory", Class.forName("weblogic.j2ee.descriptor.wl.DefaultSAFAgentBeanDConfig"), "getPagingDirectory", "setPagingDirectory");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("WindowInterval", Class.forName("weblogic.j2ee.descriptor.wl.DefaultSAFAgentBeanDConfig"), "getWindowInterval", "setWindowInterval");
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
            throw new AssertionError("Failed to create PropertyDescriptors for DefaultSAFAgentBeanDConfigBeanInfo");
         }
      }
   }
}
