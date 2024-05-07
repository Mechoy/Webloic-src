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
import weblogic.management.runtime.MinThreadsConstraintRuntimeMBean;

public class MinThreadsConstraintRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = MinThreadsConstraintRuntimeMBean.class;

   public MinThreadsConstraintRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public MinThreadsConstraintRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = MinThreadsConstraintRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.work");
      String var3 = (new String("Monitoring information for MinThreadsConstraint  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.MinThreadsConstraintRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("CompletedRequests")) {
         var3 = "getCompletedRequests";
         var4 = null;
         var2 = new PropertyDescriptor("CompletedRequests", MinThreadsConstraintRuntimeMBean.class, var3, (String)var4);
         var1.put("CompletedRequests", var2);
         var2.setValue("description", "<p>Completed request count.</p> ");
      }

      if (!var1.containsKey("CurrentWaitTime")) {
         var3 = "getCurrentWaitTime";
         var4 = null;
         var2 = new PropertyDescriptor("CurrentWaitTime", MinThreadsConstraintRuntimeMBean.class, var3, (String)var4);
         var1.put("CurrentWaitTime", var2);
         var2.setValue("description", "<p>The last measured time a request had to wait for a thread. Only requests whose execution is needed to satisfy the constraint are considered.</p> ");
      }

      if (!var1.containsKey("ExecutingRequests")) {
         var3 = "getExecutingRequests";
         var4 = null;
         var2 = new PropertyDescriptor("ExecutingRequests", MinThreadsConstraintRuntimeMBean.class, var3, (String)var4);
         var1.put("ExecutingRequests", var2);
         var2.setValue("description", "<p>Number of requests that are currently executing.</p> ");
      }

      if (!var1.containsKey("MaxWaitTime")) {
         var3 = "getMaxWaitTime";
         var4 = null;
         var2 = new PropertyDescriptor("MaxWaitTime", MinThreadsConstraintRuntimeMBean.class, var3, (String)var4);
         var1.put("MaxWaitTime", var2);
         var2.setValue("description", "<p>The max time a request had to wait for a thread. Only requests whose execution is needed to satisfy the constraint are considered.</p> ");
      }

      if (!var1.containsKey("MustRunCount")) {
         var3 = "getMustRunCount";
         var4 = null;
         var2 = new PropertyDescriptor("MustRunCount", MinThreadsConstraintRuntimeMBean.class, var3, (String)var4);
         var1.put("MustRunCount", var2);
         var2.setValue("description", "<p>Number of requests that must be executed to satisfy the constraint.</p> ");
      }

      if (!var1.containsKey("OutOfOrderExecutionCount")) {
         var3 = "getOutOfOrderExecutionCount";
         var4 = null;
         var2 = new PropertyDescriptor("OutOfOrderExecutionCount", MinThreadsConstraintRuntimeMBean.class, var3, (String)var4);
         var1.put("OutOfOrderExecutionCount", var2);
         var2.setValue("description", "<p>Number of requests executed out of turn to satisfy this constraint.</p> ");
      }

      if (!var1.containsKey("PendingRequests")) {
         var3 = "getPendingRequests";
         var4 = null;
         var2 = new PropertyDescriptor("PendingRequests", MinThreadsConstraintRuntimeMBean.class, var3, (String)var4);
         var1.put("PendingRequests", var2);
         var2.setValue("description", "<p>Pending requests that are waiting for an available thread.</p> ");
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
      Method var3 = MinThreadsConstraintRuntimeMBean.class.getMethod("preDeregister");
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
