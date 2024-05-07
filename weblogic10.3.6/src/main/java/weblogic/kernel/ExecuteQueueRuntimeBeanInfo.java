package weblogic.kernel;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.ExecuteQueueRuntimeMBean;

public class ExecuteQueueRuntimeBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ExecuteQueueRuntimeMBean.class;

   public ExecuteQueueRuntimeBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ExecuteQueueRuntimeBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ExecuteQueueRuntime.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.kernel");
      String var3 = (new String("This bean is used to monitor an execute queue and its associated thread pool.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.ExecuteQueueRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("ExecuteThreadCurrentIdleCount")) {
         var3 = "getExecuteThreadCurrentIdleCount";
         var4 = null;
         var2 = new PropertyDescriptor("ExecuteThreadCurrentIdleCount", ExecuteQueueRuntimeMBean.class, var3, (String)var4);
         var1.put("ExecuteThreadCurrentIdleCount", var2);
         var2.setValue("description", "<p>The number of idle threads assigned to the queue.</p> ");
      }

      if (!var1.containsKey("ExecuteThreadTotalCount")) {
         var3 = "getExecuteThreadTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("ExecuteThreadTotalCount", ExecuteQueueRuntimeMBean.class, var3, (String)var4);
         var1.put("ExecuteThreadTotalCount", var2);
         var2.setValue("description", "<p>The total number of execute threads assigned to the queue.</p> ");
      }

      if (!var1.containsKey("ExecuteThreads")) {
         var3 = "getExecuteThreads";
         var4 = null;
         var2 = new PropertyDescriptor("ExecuteThreads", ExecuteQueueRuntimeMBean.class, var3, (String)var4);
         var1.put("ExecuteThreads", var2);
         var2.setValue("description", "<p>The execute threads currently assigned to the queue.</p> ");
      }

      if (!var1.containsKey("PendingRequestCurrentCount")) {
         var3 = "getPendingRequestCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("PendingRequestCurrentCount", ExecuteQueueRuntimeMBean.class, var3, (String)var4);
         var1.put("PendingRequestCurrentCount", var2);
         var2.setValue("description", "<p>The number of waiting requests in the queue.</p> ");
      }

      if (!var1.containsKey("PendingRequestOldestTime")) {
         var3 = "getPendingRequestOldestTime";
         var4 = null;
         var2 = new PropertyDescriptor("PendingRequestOldestTime", ExecuteQueueRuntimeMBean.class, var3, (String)var4);
         var1.put("PendingRequestOldestTime", var2);
         var2.setValue("description", "<p>The time since the longest waiting request was placed in the queue.</p> ");
      }

      if (!var1.containsKey("ServicedRequestTotalCount")) {
         var3 = "getServicedRequestTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("ServicedRequestTotalCount", ExecuteQueueRuntimeMBean.class, var3, (String)var4);
         var1.put("ServicedRequestTotalCount", var2);
         var2.setValue("description", "<p>The number of requests that have been processed by the queue.</p> ");
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
      Method var3 = ExecuteQueueRuntimeMBean.class.getMethod("preDeregister");
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
