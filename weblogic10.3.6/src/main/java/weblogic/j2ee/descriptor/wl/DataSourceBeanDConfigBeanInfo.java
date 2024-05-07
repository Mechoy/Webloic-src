package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class DataSourceBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(DataSourceBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("Description", Class.forName("weblogic.j2ee.descriptor.wl.DataSourceBeanDConfig"), "getDescription", "setDescription");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Name", Class.forName("weblogic.j2ee.descriptor.wl.DataSourceBeanDConfig"), "getName", "setName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", true);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ClassName", Class.forName("weblogic.j2ee.descriptor.wl.DataSourceBeanDConfig"), "getClassName", "setClassName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ServerName", Class.forName("weblogic.j2ee.descriptor.wl.DataSourceBeanDConfig"), "getServerName", "setServerName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("PortNumber", Class.forName("weblogic.j2ee.descriptor.wl.DataSourceBeanDConfig"), "getPortNumber", "setPortNumber");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("DatabaseName", Class.forName("weblogic.j2ee.descriptor.wl.DataSourceBeanDConfig"), "getDatabaseName", "setDatabaseName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Url", Class.forName("weblogic.j2ee.descriptor.wl.DataSourceBeanDConfig"), "getUrl", "setUrl");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("User", Class.forName("weblogic.j2ee.descriptor.wl.DataSourceBeanDConfig"), "getUser", "setUser");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Password", Class.forName("weblogic.j2ee.descriptor.wl.DataSourceBeanDConfig"), "getPassword", "setPassword");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Properties", Class.forName("weblogic.j2ee.descriptor.wl.DataSourceBeanDConfig"), "getProperties", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("LoginTimeout", Class.forName("weblogic.j2ee.descriptor.wl.DataSourceBeanDConfig"), "getLoginTimeout", "setLoginTimeout");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Transactional", Class.forName("weblogic.j2ee.descriptor.wl.DataSourceBeanDConfig"), "isTransactional", "setTransactional");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("IsolationLevel", Class.forName("weblogic.j2ee.descriptor.wl.DataSourceBeanDConfig"), "getIsolationLevel", "setIsolationLevel");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("InitialPoolSize", Class.forName("weblogic.j2ee.descriptor.wl.DataSourceBeanDConfig"), "getInitialPoolSize", "setInitialPoolSize");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("MaxPoolSize", Class.forName("weblogic.j2ee.descriptor.wl.DataSourceBeanDConfig"), "getMaxPoolSize", "setMaxPoolSize");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("MinPoolSize", Class.forName("weblogic.j2ee.descriptor.wl.DataSourceBeanDConfig"), "getMinPoolSize", "setMinPoolSize");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("MaxIdleTime", Class.forName("weblogic.j2ee.descriptor.wl.DataSourceBeanDConfig"), "getMaxIdleTime", "setMaxIdleTime");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("MaxStatements", Class.forName("weblogic.j2ee.descriptor.wl.DataSourceBeanDConfig"), "getMaxStatements", "setMaxStatements");
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
            throw new AssertionError("Failed to create PropertyDescriptors for DataSourceBeanDConfigBeanInfo");
         }
      }
   }
}
