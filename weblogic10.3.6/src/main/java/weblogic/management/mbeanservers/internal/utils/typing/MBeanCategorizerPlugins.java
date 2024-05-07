package weblogic.management.mbeanservers.internal.utils.typing;

import javax.management.Descriptor;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.modelmbean.ModelMBeanInfo;

public interface MBeanCategorizerPlugins {
   String MODEL_MBEAN_TYPE_TAG = "DiagnosticTypeName";

   public static class CustomPlugin implements MBeanCategorizer.Plugin {
      static final long serialVersionUID = 1L;
      public static final String CATEGORY_NAME = "Custom-MBean";

      public boolean handles(MBeanServerConnection var1, ObjectName var2) {
         return true;
      }

      public String getTypeName(MBeanServerConnection var1, ObjectName var2) {
         String var3 = null;

         try {
            if (var1.isInstanceOf(var2, "javax.management.modelmbean.ModelMBean")) {
               ModelMBeanInfo var4 = (ModelMBeanInfo)var1.getMBeanInfo(var2);
               Descriptor var5 = var4.getMBeanDescriptor();
               var3 = (String)var5.getFieldValue("DiagnosticTypeName");
            } else {
               MBeanInfo var8 = var1.getMBeanInfo(var2);
               var3 = var8.getClassName();
            }

            return var3;
         } catch (RuntimeException var6) {
            throw var6;
         } catch (Exception var7) {
            throw new RuntimeException(var7);
         }
      }

      public String getCategoryName() {
         return "Custom-MBean";
      }
   }

   public static class PlatformPlugin extends CustomPlugin {
      static final long serialVersionUID = 1L;
      public static final String CATEGORY_NAME = "Platform-MBean";

      public boolean handles(MBeanServerConnection var1, ObjectName var2) {
         String var3 = this.getTypeName(var1, var2);
         return var3 != null && (var3.startsWith("sun.") || var3.startsWith("com.sun"));
      }

      public String getCategoryName(ObjectName var1) {
         return "Platform-MBean";
      }
   }

   public static class JMXPlugin extends CustomPlugin {
      static final long serialVersionUID = 1L;
      public static final String CATEGORY_NAME = "JMX-MBean";

      public boolean handles(MBeanServerConnection var1, ObjectName var2) {
         String var3 = this.getTypeName(var1, var2);
         return var3 != null && var3.startsWith("javax.management");
      }

      public String getCategoryName(ObjectName var1) {
         return "JMX-MBean";
      }
   }

   public static class NonWLSPlugin extends CustomPlugin {
      static final long serialVersionUID = 1L;
      public static final String CATEGORY_NAME = "Non-WLS-MBean";

      public boolean handles(MBeanServerConnection var1, ObjectName var2) {
         try {
            return !var1.isInstanceOf(var2, "weblogic.management.jmx.modelmbean.WLSModelMBean");
         } catch (InstanceNotFoundException var4) {
            return false;
         } catch (Exception var5) {
            throw new RuntimeException(var5);
         }
      }

      public String getCategoryName(ObjectName var1) {
         return "Non-WLS-MBean";
      }
   }

   public static class WLSPlugin implements MBeanCategorizer.Plugin {
      static final long serialVersionUID = 1L;
      public static final String CATEGORY_NAME = "WLS-MBean";

      public boolean handles(MBeanServerConnection var1, ObjectName var2) {
         try {
            return var1.isInstanceOf(var2, "weblogic.management.jmx.modelmbean.WLSModelMBean");
         } catch (InstanceNotFoundException var4) {
            return false;
         } catch (Exception var5) {
            throw new RuntimeException(var5);
         }
      }

      public String getTypeName(MBeanServerConnection var1, ObjectName var2) {
         try {
            ModelMBeanInfo var3 = (ModelMBeanInfo)var1.getMBeanInfo(var2);
            Descriptor var4 = var3.getMBeanDescriptor();
            return (String)var4.getFieldValue("interfaceclassname");
         } catch (RuntimeException var5) {
            throw var5;
         } catch (Exception var6) {
            throw new RuntimeException(var6);
         }
      }

      public String getCategoryName() {
         return "WLS-MBean";
      }
   }
}
