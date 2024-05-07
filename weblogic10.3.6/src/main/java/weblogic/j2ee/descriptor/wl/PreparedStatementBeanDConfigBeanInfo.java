package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class PreparedStatementBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(PreparedStatementBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("ProfilingEnabled", Class.forName("weblogic.j2ee.descriptor.wl.PreparedStatementBeanDConfig"), "isProfilingEnabled", "setProfilingEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("CacheProfilingThreshold", Class.forName("weblogic.j2ee.descriptor.wl.PreparedStatementBeanDConfig"), "getCacheProfilingThreshold", "setCacheProfilingThreshold");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("CacheSize", Class.forName("weblogic.j2ee.descriptor.wl.PreparedStatementBeanDConfig"), "getCacheSize", "setCacheSize");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ParameterLoggingEnabled", Class.forName("weblogic.j2ee.descriptor.wl.PreparedStatementBeanDConfig"), "isParameterLoggingEnabled", "setParameterLoggingEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("MaxParameterLength", Class.forName("weblogic.j2ee.descriptor.wl.PreparedStatementBeanDConfig"), "getMaxParameterLength", "setMaxParameterLength");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("CacheType", Class.forName("weblogic.j2ee.descriptor.wl.PreparedStatementBeanDConfig"), "getCacheType", "setCacheType");
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
            throw new AssertionError("Failed to create PropertyDescriptors for PreparedStatementBeanDConfigBeanInfo");
         }
      }
   }
}
