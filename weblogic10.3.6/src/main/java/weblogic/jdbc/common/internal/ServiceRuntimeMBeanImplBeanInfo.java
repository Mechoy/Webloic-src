package weblogic.jdbc.common.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.JDBCServiceRuntimeMBean;

public class ServiceRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JDBCServiceRuntimeMBean.class;

   public ServiceRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ServiceRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ServiceRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.jdbc.common.internal");
      String var3 = (new String("<p>This class is used for monitoring a WebLogic JDBC service. It maps to a JDBCResource JMO.</p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.JDBCServiceRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("HealthState")) {
         var3 = "getHealthState";
         var4 = null;
         var2 = new PropertyDescriptor("HealthState", JDBCServiceRuntimeMBean.class, var3, (String)var4);
         var1.put("HealthState", var2);
         var2.setValue("description", "<p>The health state of the JDBC subsystem.</p> ");
      }

      if (!var1.containsKey("JDBCDataSourceRuntimeMBeans")) {
         var3 = "getJDBCDataSourceRuntimeMBeans";
         var4 = null;
         var2 = new PropertyDescriptor("JDBCDataSourceRuntimeMBeans", JDBCServiceRuntimeMBean.class, var3, (String)var4);
         var1.put("JDBCDataSourceRuntimeMBeans", var2);
         var2.setValue("description", "The list of JDBCDataSourceRuntimeMBeans created in this domain. ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("JDBCDriverRuntimeMBeans")) {
         var3 = "getJDBCDriverRuntimeMBeans";
         var4 = null;
         var2 = new PropertyDescriptor("JDBCDriverRuntimeMBeans", JDBCServiceRuntimeMBean.class, var3, (String)var4);
         var1.put("JDBCDriverRuntimeMBeans", var2);
         var2.setValue("description", "The list of JDBCDriverRuntimeMBeans created in this domain. ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("JDBCMultiDataSourceRuntimeMBeans")) {
         var3 = "getJDBCMultiDataSourceRuntimeMBeans";
         var4 = null;
         var2 = new PropertyDescriptor("JDBCMultiDataSourceRuntimeMBeans", JDBCServiceRuntimeMBean.class, var3, (String)var4);
         var1.put("JDBCMultiDataSourceRuntimeMBeans", var2);
         var2.setValue("description", "The list of JDBCMultiDataSourceRuntimeMBeans created in this domain. ");
         var2.setValue("relationship", "containment");
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
      Method var3 = JDBCServiceRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         MethodDescriptor var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
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
