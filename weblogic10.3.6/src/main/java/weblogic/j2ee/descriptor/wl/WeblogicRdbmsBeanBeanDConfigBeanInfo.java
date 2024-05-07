package weblogic.j2ee.descriptor.wl;

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
            PropertyDescriptor var1 = new PropertyDescriptor("EjbName", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsBeanBeanDConfig"), "getEjbName", "setEjbName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", true);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("DataSourceJNDIName", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsBeanBeanDConfig"), "getDataSourceJNDIName", "setDataSourceJNDIName");
            var1.setValue("dependency", true);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("UnknownPrimaryKeyField", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsBeanBeanDConfig"), "getUnknownPrimaryKeyField", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("TableMaps", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsBeanBeanDConfig"), "getTableMaps", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("FieldGroups", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsBeanBeanDConfig"), "getFieldGroups", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("RelationshipCachings", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsBeanBeanDConfig"), "getRelationshipCachings", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("SqlShapes", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsBeanBeanDConfig"), "getSqlShapes", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("WeblogicQueries", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsBeanBeanDConfig"), "getWeblogicQueries", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("DelayDatabaseInsertUntil", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsBeanBeanDConfig"), "getDelayDatabaseInsertUntil", "setDelayDatabaseInsertUntil");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("UseSelectForUpdate", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsBeanBeanDConfig"), "isUseSelectForUpdate", "setUseSelectForUpdate");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("LockOrder", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsBeanBeanDConfig"), "getLockOrder", "setLockOrder");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("InstanceLockOrder", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsBeanBeanDConfig"), "getInstanceLockOrder", "setInstanceLockOrder");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("AutomaticKeyGeneration", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsBeanBeanDConfig"), "getAutomaticKeyGeneration", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("CheckExistsOnMethod", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsBeanBeanDConfig"), "isCheckExistsOnMethod", "setCheckExistsOnMethod");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Id", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsBeanBeanDConfig"), "getId", "setId");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ClusterInvalidationDisabled", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsBeanBeanDConfig"), "isClusterInvalidationDisabled", "setClusterInvalidationDisabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("UseInnerJoin", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsBeanBeanDConfig"), "isUseInnerJoin", "setUseInnerJoin");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("CategoryCmpField", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicRdbmsBeanBeanDConfig"), "getCategoryCmpField", "setCategoryCmpField");
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
