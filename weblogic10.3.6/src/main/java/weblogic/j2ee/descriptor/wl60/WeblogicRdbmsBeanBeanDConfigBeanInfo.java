package weblogic.j2ee.descriptor.wl60;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class WeblogicRdbmsBeanBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(WeblogicRdbmsBeanBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("EjbName", Class.forName("weblogic.j2ee.descriptor.wl60.WeblogicRdbmsBeanBeanDConfig"), "getEjbName", "setEjbName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", true);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("PoolName", Class.forName("weblogic.j2ee.descriptor.wl60.WeblogicRdbmsBeanBeanDConfig"), "getPoolName", "setPoolName");
            var1.setValue("dependency", true);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("DataSourceJndiName", Class.forName("weblogic.j2ee.descriptor.wl60.WeblogicRdbmsBeanBeanDConfig"), "getDataSourceJndiName", "setDataSourceJndiName");
            var1.setValue("dependency", true);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("TableName", Class.forName("weblogic.j2ee.descriptor.wl60.WeblogicRdbmsBeanBeanDConfig"), "getTableName", "setTableName");
            var1.setValue("dependency", true);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("FieldMaps", Class.forName("weblogic.j2ee.descriptor.wl60.WeblogicRdbmsBeanBeanDConfig"), "getFieldMaps", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Finders", Class.forName("weblogic.j2ee.descriptor.wl60.WeblogicRdbmsBeanBeanDConfig"), "getFinders", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("EnableTunedUpdates", Class.forName("weblogic.j2ee.descriptor.wl60.WeblogicRdbmsBeanBeanDConfig"), "isEnableTunedUpdates", "setEnableTunedUpdates");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Id", Class.forName("weblogic.j2ee.descriptor.wl60.WeblogicRdbmsBeanBeanDConfig"), "getId", "setId");
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
            throw new AssertionError("Failed to create PropertyDescriptors for WeblogicRdbmsBeanBeanDConfigBeanInfo");
         }
      }
   }
}
