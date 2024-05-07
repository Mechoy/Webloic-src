package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class WeblogicQueryBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(WeblogicQueryBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("Description", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicQueryBeanDConfig"), "getDescription", "setDescription");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("QueryMethod", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicQueryBeanDConfig"), "getQueryMethod", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("EjbQlQuery", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicQueryBeanDConfig"), "getEjbQlQuery", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("SqlQuery", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicQueryBeanDConfig"), "getSqlQuery", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("MaxElements", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicQueryBeanDConfig"), "getMaxElements", "setMaxElements");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("IncludeUpdates", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicQueryBeanDConfig"), "isIncludeUpdates", "setIncludeUpdates");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("IncludeUpdatesSet", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicQueryBeanDConfig"), "isIncludeUpdatesSet", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("SqlSelectDistinct", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicQueryBeanDConfig"), "isSqlSelectDistinct", "setSqlSelectDistinct");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Id", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicQueryBeanDConfig"), "getId", "setId");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("EnableQueryCaching", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicQueryBeanDConfig"), "getEnableQueryCaching", "setEnableQueryCaching");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("EnableEagerRefresh", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicQueryBeanDConfig"), "getEnableEagerRefresh", "setEnableEagerRefresh");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("IncludeResultCacheHint", Class.forName("weblogic.j2ee.descriptor.wl.WeblogicQueryBeanDConfig"), "isIncludeResultCacheHint", "setIncludeResultCacheHint");
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
            throw new AssertionError("Failed to create PropertyDescriptors for WeblogicQueryBeanDConfigBeanInfo");
         }
      }
   }
}
