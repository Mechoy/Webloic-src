package weblogic.management.security;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;
import weblogic.management.commo.AbstractCommoConfigurationBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class ExportMBeanImplBeanInfo extends AbstractCommoConfigurationBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ExportMBean.class;

   public ExportMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ExportMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ExportMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("abstract", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.security");
      String var3 = (new String("Provides a set of methods for exporting provider specific data. A provider MBean can optionally extend this MBean. The WebLogic Server Administration Console detects when a provider extends this MBean and automatically provides a GUI for using these methods.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">In addition to being used as a base class that provides functionality to security provider MBeans, JMX applications can use this class directly as a type-safe interface. When used as a type-safe interface, a JMX application imports this class and accesses it through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, JMX applications that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.security.ExportMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("SupportedExportConstraints")) {
         var3 = "getSupportedExportConstraints";
         var4 = null;
         var2 = new PropertyDescriptor("SupportedExportConstraints", ExportMBean.class, var3, (String)var4);
         var1.put("SupportedExportConstraints", var2);
         var2.setValue("description", "Returns the list of export constraints that this provider supports. ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("SupportedExportFormats")) {
         var3 = "getSupportedExportFormats";
         var4 = null;
         var2 = new PropertyDescriptor("SupportedExportFormats", ExportMBean.class, var3, (String)var4);
         var1.put("SupportedExportFormats", var2);
         var2.setValue("description", "Returns the list of export data formats that this provider supports. ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = ExportMBean.class.getMethod("exportData", String.class, String.class, Properties.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("format", "- The format for exporting provider specific data. "), createParameterDescriptor("filename", "- The full path to the filename used to write data. "), createParameterDescriptor("constraints", "- The constraints to be used when exporting data. A null value indicates that all data will be exported. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         MethodDescriptor var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Exports provider specific data in a specified format. When errors occur, the MBean throws an ErrorCollectionException containing a list of <code>java.lang.Exceptions</code>, where the text of each exception describes the error. ");
         var2.setValue("role", "operation");
      }

   }

   protected void buildMethodDescriptors(Map var1) throws IntrospectionException, NoSuchMethodException {
      this.fillinFinderMethodInfos(var1);
      if (!this.readOnly) {
         this.fillinCollectionMethodInfos(var1);
         this.fillinFactoryMethodInfos(var1);
      }

      this.fillinOperationMethodInfos(var1);
      super.buildMethodDescriptors(var1);
   }

   protected void buildEventSetDescriptors(Map var1) throws IntrospectionException {
   }
}
