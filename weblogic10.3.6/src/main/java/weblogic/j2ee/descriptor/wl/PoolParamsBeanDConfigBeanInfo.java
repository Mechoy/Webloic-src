package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class PoolParamsBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(PoolParamsBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("MaxCapacity", Class.forName("weblogic.j2ee.descriptor.wl.PoolParamsBeanDConfig"), "getMaxCapacity", "setMaxCapacity");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("ConnectionReserveTimeoutSeconds", Class.forName("weblogic.j2ee.descriptor.wl.PoolParamsBeanDConfig"), "getConnectionReserveTimeoutSeconds", "setConnectionReserveTimeoutSeconds");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("HighestNumWaiters", Class.forName("weblogic.j2ee.descriptor.wl.PoolParamsBeanDConfig"), "getHighestNumWaiters", "setHighestNumWaiters");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("MatchConnectionsSupported", Class.forName("weblogic.j2ee.descriptor.wl.PoolParamsBeanDConfig"), "isMatchConnectionsSupported", "setMatchConnectionsSupported");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("UseFirstAvailable", Class.forName("weblogic.j2ee.descriptor.wl.PoolParamsBeanDConfig"), "isUseFirstAvailable", "setUseFirstAvailable");
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
            throw new AssertionError("Failed to create PropertyDescriptors for PoolParamsBeanDConfigBeanInfo");
         }
      }
   }
}
