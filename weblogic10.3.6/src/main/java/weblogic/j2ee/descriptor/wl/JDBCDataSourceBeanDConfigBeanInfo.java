package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class JDBCDataSourceBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(JDBCDataSourceBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("Name", Class.forName("weblogic.j2ee.descriptor.wl.JDBCDataSourceBeanDConfig"), "getName", "setName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", true);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("InternalProperties", Class.forName("weblogic.j2ee.descriptor.wl.JDBCDataSourceBeanDConfig"), "getInternalProperties", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("JDBCDriverParams", Class.forName("weblogic.j2ee.descriptor.wl.JDBCDataSourceBeanDConfig"), "getJDBCDriverParams", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("JDBCConnectionPoolParams", Class.forName("weblogic.j2ee.descriptor.wl.JDBCDataSourceBeanDConfig"), "getJDBCConnectionPoolParams", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("JDBCDataSourceParams", Class.forName("weblogic.j2ee.descriptor.wl.JDBCDataSourceBeanDConfig"), "getJDBCDataSourceParams", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("JDBCXAParams", Class.forName("weblogic.j2ee.descriptor.wl.JDBCDataSourceBeanDConfig"), "getJDBCXAParams", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("JDBCOracleParams", Class.forName("weblogic.j2ee.descriptor.wl.JDBCDataSourceBeanDConfig"), "getJDBCOracleParams", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Version", Class.forName("weblogic.j2ee.descriptor.wl.JDBCDataSourceBeanDConfig"), "getVersion", "setVersion");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("SecondaryDescriptors", Class.forName("weblogic.j2ee.descriptor.wl.JDBCDataSourceBeanDConfig"), "getSecondaryDescriptors", (String)null);
            var1.setValue("dependency", Boolean.FALSE);
            var1.setValue("declaration", Boolean.FALSE);
            var1.setValue("configurable", Boolean.FALSE);
            var1.setValue("key", Boolean.FALSE);
            var1.setValue("dynamic", Boolean.FALSE);
            var2.add(var1);
            pds = (PropertyDescriptor[])((PropertyDescriptor[])var2.toArray(new PropertyDescriptor[0]));
            return pds;
         } catch (Throwable var4) {
            var4.printStackTrace();
            throw new AssertionError("Failed to create PropertyDescriptors for JDBCDataSourceBeanDConfigBeanInfo");
         }
      }
   }
}
