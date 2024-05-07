package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class LoggingBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(LoggingBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("LogFilename", Class.forName("weblogic.j2ee.descriptor.wl.LoggingBeanDConfig"), "getLogFilename", "setLogFilename");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("LoggingEnabled", Class.forName("weblogic.j2ee.descriptor.wl.LoggingBeanDConfig"), "isLoggingEnabled", "setLoggingEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("RotationType", Class.forName("weblogic.j2ee.descriptor.wl.LoggingBeanDConfig"), "getRotationType", "setRotationType");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("NumberOfFilesLimited", Class.forName("weblogic.j2ee.descriptor.wl.LoggingBeanDConfig"), "isNumberOfFilesLimited", "setNumberOfFilesLimited");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("FileCount", Class.forName("weblogic.j2ee.descriptor.wl.LoggingBeanDConfig"), "getFileCount", "setFileCount");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("FileSizeLimit", Class.forName("weblogic.j2ee.descriptor.wl.LoggingBeanDConfig"), "getFileSizeLimit", "setFileSizeLimit");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("RotateLogOnStartup", Class.forName("weblogic.j2ee.descriptor.wl.LoggingBeanDConfig"), "isRotateLogOnStartup", "setRotateLogOnStartup");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("LogFileRotationDir", Class.forName("weblogic.j2ee.descriptor.wl.LoggingBeanDConfig"), "getLogFileRotationDir", "setLogFileRotationDir");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("RotationTime", Class.forName("weblogic.j2ee.descriptor.wl.LoggingBeanDConfig"), "getRotationTime", "setRotationTime");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("FileTimeSpan", Class.forName("weblogic.j2ee.descriptor.wl.LoggingBeanDConfig"), "getFileTimeSpan", "setFileTimeSpan");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("DateFormatPattern", Class.forName("weblogic.j2ee.descriptor.wl.LoggingBeanDConfig"), "getDateFormatPattern", "setDateFormatPattern");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Id", Class.forName("weblogic.j2ee.descriptor.wl.LoggingBeanDConfig"), "getId", "setId");
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
            throw new AssertionError("Failed to create PropertyDescriptors for LoggingBeanDConfigBeanInfo");
         }
      }
   }
}
