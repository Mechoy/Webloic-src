package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class ConnectionCheckParamsBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(ConnectionCheckParamsBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("TableName", Class.forName("weblogic.j2ee.descriptor.wl.ConnectionCheckParamsBeanDConfig"), "getTableName", "setTableName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("CheckOnReserveEnabled", Class.forName("weblogic.j2ee.descriptor.wl.ConnectionCheckParamsBeanDConfig"), "isCheckOnReserveEnabled", "setCheckOnReserveEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("CheckOnReleaseEnabled", Class.forName("weblogic.j2ee.descriptor.wl.ConnectionCheckParamsBeanDConfig"), "isCheckOnReleaseEnabled", "setCheckOnReleaseEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("RefreshMinutes", Class.forName("weblogic.j2ee.descriptor.wl.ConnectionCheckParamsBeanDConfig"), "getRefreshMinutes", "setRefreshMinutes");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("CheckOnCreateEnabled", Class.forName("weblogic.j2ee.descriptor.wl.ConnectionCheckParamsBeanDConfig"), "isCheckOnCreateEnabled", "setCheckOnCreateEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ConnectionReserveTimeoutSeconds", Class.forName("weblogic.j2ee.descriptor.wl.ConnectionCheckParamsBeanDConfig"), "getConnectionReserveTimeoutSeconds", "setConnectionReserveTimeoutSeconds");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ConnectionCreationRetryFrequencySeconds", Class.forName("weblogic.j2ee.descriptor.wl.ConnectionCheckParamsBeanDConfig"), "getConnectionCreationRetryFrequencySeconds", "setConnectionCreationRetryFrequencySeconds");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("InactiveConnectionTimeoutSeconds", Class.forName("weblogic.j2ee.descriptor.wl.ConnectionCheckParamsBeanDConfig"), "getInactiveConnectionTimeoutSeconds", "setInactiveConnectionTimeoutSeconds");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("TestFrequencySeconds", Class.forName("weblogic.j2ee.descriptor.wl.ConnectionCheckParamsBeanDConfig"), "getTestFrequencySeconds", "setTestFrequencySeconds");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("InitSql", Class.forName("weblogic.j2ee.descriptor.wl.ConnectionCheckParamsBeanDConfig"), "getInitSql", "setInitSql");
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
            throw new AssertionError("Failed to create PropertyDescriptors for ConnectionCheckParamsBeanDConfigBeanInfo");
         }
      }
   }
}
