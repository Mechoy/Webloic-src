package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class JDBCOracleParamsBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(JDBCOracleParamsBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("FanEnabled", Class.forName("weblogic.j2ee.descriptor.wl.JDBCOracleParamsBeanDConfig"), "isFanEnabled", "setFanEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("OnsNodeList", Class.forName("weblogic.j2ee.descriptor.wl.JDBCOracleParamsBeanDConfig"), "getOnsNodeList", "setOnsNodeList");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("OnsWalletFile", Class.forName("weblogic.j2ee.descriptor.wl.JDBCOracleParamsBeanDConfig"), "getOnsWalletFile", "setOnsWalletFile");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("OnsWalletPasswordEncrypted", Class.forName("weblogic.j2ee.descriptor.wl.JDBCOracleParamsBeanDConfig"), "getOnsWalletPasswordEncrypted", "setOnsWalletPasswordEncrypted");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("OnsWalletPassword", Class.forName("weblogic.j2ee.descriptor.wl.JDBCOracleParamsBeanDConfig"), "getOnsWalletPassword", "setOnsWalletPassword");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("OracleEnableJavaNetFastPath", Class.forName("weblogic.j2ee.descriptor.wl.JDBCOracleParamsBeanDConfig"), "isOracleEnableJavaNetFastPath", "setOracleEnableJavaNetFastPath");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("OracleOptimizeUtf8Conversion", Class.forName("weblogic.j2ee.descriptor.wl.JDBCOracleParamsBeanDConfig"), "isOracleOptimizeUtf8Conversion", "setOracleOptimizeUtf8Conversion");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("ConnectionInitializationCallback", Class.forName("weblogic.j2ee.descriptor.wl.JDBCOracleParamsBeanDConfig"), "getConnectionInitializationCallback", "setConnectionInitializationCallback");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("AffinityPolicy", Class.forName("weblogic.j2ee.descriptor.wl.JDBCOracleParamsBeanDConfig"), "getAffinityPolicy", "setAffinityPolicy");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("OracleProxySession", Class.forName("weblogic.j2ee.descriptor.wl.JDBCOracleParamsBeanDConfig"), "isOracleProxySession", "setOracleProxySession");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("UseDatabaseCredentials", Class.forName("weblogic.j2ee.descriptor.wl.JDBCOracleParamsBeanDConfig"), "isUseDatabaseCredentials", "setUseDatabaseCredentials");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            pds = (PropertyDescriptor[])((PropertyDescriptor[])var2.toArray(new PropertyDescriptor[0]));
            return pds;
         } catch (Throwable var4) {
            var4.printStackTrace();
            throw new AssertionError("Failed to create PropertyDescriptors for JDBCOracleParamsBeanDConfigBeanInfo");
         }
      }
   }
}
