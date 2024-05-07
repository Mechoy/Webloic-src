package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class WeblogicRdbmsJarBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(WeblogicRdbmsJarBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("WeblogicRdbmsBeans", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBeanDConfig"), "getWeblogicRdbmsBeans", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("WeblogicRdbmsRelations", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBeanDConfig"), "getWeblogicRdbmsRelations", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("OrderDatabaseOperations", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBeanDConfig"), "isOrderDatabaseOperations", "setOrderDatabaseOperations");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("EnableBatchOperations", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBeanDConfig"), "isEnableBatchOperations", "setEnableBatchOperations");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("CreateDefaultDbmsTables", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBeanDConfig"), "getCreateDefaultDbmsTables", "setCreateDefaultDbmsTables");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ValidateDbSchemaWith", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBeanDConfig"), "getValidateDbSchemaWith", "setValidateDbSchemaWith");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("DatabaseType", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBeanDConfig"), "getDatabaseType", "setDatabaseType");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("DefaultDbmsTablesDdl", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBeanDConfig"), "getDefaultDbmsTablesDdl", "setDefaultDbmsTablesDdl");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Compatibility", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBeanDConfig"), "getCompatibility", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Id", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBeanDConfig"), "getId", "setId");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Version", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBeanDConfig"), "getVersion", "setVersion");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("SecondaryDescriptors", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBeanDConfig"), "getSecondaryDescriptors", (String)null);
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
            throw new AssertionError("Failed to create PropertyDescriptors for WeblogicRdbmsJarBeanDConfigBeanInfo");
         }
      }
   }
}
