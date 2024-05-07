package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class FlowControlParamsBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(FlowControlParamsBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("FlowMinimum", Class.forName("weblogic.j2ee.descriptor.wl.FlowControlParamsBeanDConfig"), "getFlowMinimum", "setFlowMinimum");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("FlowMaximum", Class.forName("weblogic.j2ee.descriptor.wl.FlowControlParamsBeanDConfig"), "getFlowMaximum", "setFlowMaximum");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("FlowInterval", Class.forName("weblogic.j2ee.descriptor.wl.FlowControlParamsBeanDConfig"), "getFlowInterval", "setFlowInterval");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("FlowSteps", Class.forName("weblogic.j2ee.descriptor.wl.FlowControlParamsBeanDConfig"), "getFlowSteps", "setFlowSteps");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("FlowControlEnabled", Class.forName("weblogic.j2ee.descriptor.wl.FlowControlParamsBeanDConfig"), "isFlowControlEnabled", "setFlowControlEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("OneWaySendMode", Class.forName("weblogic.j2ee.descriptor.wl.FlowControlParamsBeanDConfig"), "getOneWaySendMode", "setOneWaySendMode");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("OneWaySendWindowSize", Class.forName("weblogic.j2ee.descriptor.wl.FlowControlParamsBeanDConfig"), "getOneWaySendWindowSize", "setOneWaySendWindowSize");
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
            throw new AssertionError("Failed to create PropertyDescriptors for FlowControlParamsBeanDConfigBeanInfo");
         }
      }
   }
}
