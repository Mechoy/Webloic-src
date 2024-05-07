package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class ConnectionPoolParamsBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(ConnectionPoolParamsBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("InitialCapacity", Class.forName("weblogic.j2ee.descriptor.wl.ConnectionPoolParamsBeanDConfig"), "getInitialCapacity", "setInitialCapacity");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("MaxCapacity", Class.forName("weblogic.j2ee.descriptor.wl.ConnectionPoolParamsBeanDConfig"), "getMaxCapacity", "setMaxCapacity");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("CapacityIncrement", Class.forName("weblogic.j2ee.descriptor.wl.ConnectionPoolParamsBeanDConfig"), "getCapacityIncrement", "setCapacityIncrement");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("ShrinkingEnabled", Class.forName("weblogic.j2ee.descriptor.wl.ConnectionPoolParamsBeanDConfig"), "isShrinkingEnabled", "setShrinkingEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("ShrinkFrequencySeconds", Class.forName("weblogic.j2ee.descriptor.wl.ConnectionPoolParamsBeanDConfig"), "getShrinkFrequencySeconds", "setShrinkFrequencySeconds");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("HighestNumWaiters", Class.forName("weblogic.j2ee.descriptor.wl.ConnectionPoolParamsBeanDConfig"), "getHighestNumWaiters", "setHighestNumWaiters");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("HighestNumUnavailable", Class.forName("weblogic.j2ee.descriptor.wl.ConnectionPoolParamsBeanDConfig"), "getHighestNumUnavailable", "setHighestNumUnavailable");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("ConnectionCreationRetryFrequencySeconds", Class.forName("weblogic.j2ee.descriptor.wl.ConnectionPoolParamsBeanDConfig"), "getConnectionCreationRetryFrequencySeconds", "setConnectionCreationRetryFrequencySeconds");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("ConnectionReserveTimeoutSeconds", Class.forName("weblogic.j2ee.descriptor.wl.ConnectionPoolParamsBeanDConfig"), "getConnectionReserveTimeoutSeconds", "setConnectionReserveTimeoutSeconds");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("TestFrequencySeconds", Class.forName("weblogic.j2ee.descriptor.wl.ConnectionPoolParamsBeanDConfig"), "getTestFrequencySeconds", "setTestFrequencySeconds");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("TestConnectionsOnCreate", Class.forName("weblogic.j2ee.descriptor.wl.ConnectionPoolParamsBeanDConfig"), "isTestConnectionsOnCreate", "setTestConnectionsOnCreate");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("TestConnectionsOnRelease", Class.forName("weblogic.j2ee.descriptor.wl.ConnectionPoolParamsBeanDConfig"), "isTestConnectionsOnRelease", "setTestConnectionsOnRelease");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("TestConnectionsOnReserve", Class.forName("weblogic.j2ee.descriptor.wl.ConnectionPoolParamsBeanDConfig"), "isTestConnectionsOnReserve", "setTestConnectionsOnReserve");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("ProfileHarvestFrequencySeconds", Class.forName("weblogic.j2ee.descriptor.wl.ConnectionPoolParamsBeanDConfig"), "getProfileHarvestFrequencySeconds", "setProfileHarvestFrequencySeconds");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("IgnoreInUseConnectionsEnabled", Class.forName("weblogic.j2ee.descriptor.wl.ConnectionPoolParamsBeanDConfig"), "isIgnoreInUseConnectionsEnabled", "setIgnoreInUseConnectionsEnabled");
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
            throw new AssertionError("Failed to create PropertyDescriptors for ConnectionPoolParamsBeanDConfigBeanInfo");
         }
      }
   }
}
