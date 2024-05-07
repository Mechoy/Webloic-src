package weblogic.work;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.WorkManagerRuntimeMBean;

public class WorkManagerRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WorkManagerRuntimeMBean.class;

   public WorkManagerRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WorkManagerRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WorkManagerRuntimeMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.work");
      String var3 = (new String("WorkManager Runtime information.   <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.WorkManagerRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("ApplicationName")) {
         var3 = "getApplicationName";
         var4 = null;
         var2 = new PropertyDescriptor("ApplicationName", WorkManagerRuntimeMBean.class, var3, (String)var4);
         var1.put("ApplicationName", var2);
         var2.setValue("description", "<p>Get the name of the application this WorkManager is associated with</p> ");
      }

      if (!var1.containsKey("CompletedRequests")) {
         var3 = "getCompletedRequests";
         var4 = null;
         var2 = new PropertyDescriptor("CompletedRequests", WorkManagerRuntimeMBean.class, var3, (String)var4);
         var1.put("CompletedRequests", var2);
         var2.setValue("description", "<p>The number of requests that have been processed</p> ");
      }

      if (!var1.containsKey("HealthState")) {
         var3 = "getHealthState";
         var4 = null;
         var2 = new PropertyDescriptor("HealthState", WorkManagerRuntimeMBean.class, var3, (String)var4);
         var1.put("HealthState", var2);
         var2.setValue("description", "<p>Returns the HealthState mbean for the work manager. </p> ");
      }

      if (!var1.containsKey("MaxThreadsConstraintRuntime")) {
         var3 = "getMaxThreadsConstraintRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("MaxThreadsConstraintRuntime", WorkManagerRuntimeMBean.class, var3, (String)var4);
         var1.put("MaxThreadsConstraintRuntime", var2);
         var2.setValue("description", "<p>Runtime information on MaxThreadsConstraint associated with this WorkManager</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("MinThreadsConstraintRuntime")) {
         var3 = "getMinThreadsConstraintRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("MinThreadsConstraintRuntime", WorkManagerRuntimeMBean.class, var3, (String)var4);
         var1.put("MinThreadsConstraintRuntime", var2);
         var2.setValue("description", "<p>Runtime information on MinThreadsConstraint associated with this WorkManager</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("ModuleName")) {
         var3 = "getModuleName";
         var4 = null;
         var2 = new PropertyDescriptor("ModuleName", WorkManagerRuntimeMBean.class, var3, (String)var4);
         var1.put("ModuleName", var2);
         var2.setValue("description", "<p>Get the name of the module this WorkManager is associated with</p> ");
      }

      if (!var1.containsKey("PendingRequests")) {
         var3 = "getPendingRequests";
         var4 = null;
         var2 = new PropertyDescriptor("PendingRequests", WorkManagerRuntimeMBean.class, var3, (String)var4);
         var1.put("PendingRequests", var2);
         var2.setValue("description", "<p>The number of waiting requests in the queue.</p> ");
      }

      if (!var1.containsKey("RequestClassRuntime")) {
         var3 = "getRequestClassRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("RequestClassRuntime", WorkManagerRuntimeMBean.class, var3, (String)var4);
         var1.put("RequestClassRuntime", var2);
         var2.setValue("description", "<p>Runtime information on RequestClass associated with this WorkManager</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("StuckThreadCount")) {
         var3 = "getStuckThreadCount";
         var4 = null;
         var2 = new PropertyDescriptor("StuckThreadCount", WorkManagerRuntimeMBean.class, var3, (String)var4);
         var1.put("StuckThreadCount", var2);
         var2.setValue("description", "<p>The number of threads that are considered to be stuck on the basis of any stuck thread constraints.</p> ");
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
      Method var3 = WorkManagerRuntimeMBean.class.getMethod("preDeregister");
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
