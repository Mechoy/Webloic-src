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
import weblogic.management.runtime.ThreadPoolRuntimeMBean;

public class ThreadPoolRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ThreadPoolRuntimeMBean.class;

   public ThreadPoolRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ThreadPoolRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ThreadPoolRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.work");
      String var3 = (new String("This bean is used to monitor the self-tuning queue  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.ThreadPoolRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("CompletedRequestCount")) {
         var3 = "getCompletedRequestCount";
         var4 = null;
         var2 = new PropertyDescriptor("CompletedRequestCount", ThreadPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("CompletedRequestCount", var2);
         var2.setValue("description", "<p>The number of completed requests in the priority queue.</p> ");
      }

      if (!var1.containsKey("ExecuteThreadIdleCount")) {
         var3 = "getExecuteThreadIdleCount";
         var4 = null;
         var2 = new PropertyDescriptor("ExecuteThreadIdleCount", ThreadPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ExecuteThreadIdleCount", var2);
         var2.setValue("description", "<p>The number of idle threads in the pool. This count does not include standby threads and stuck threads. The count indicates threads that are ready to pick up new work when it arrives</p> ");
      }

      if (!var1.containsKey("ExecuteThreadTotalCount")) {
         var3 = "getExecuteThreadTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("ExecuteThreadTotalCount", ThreadPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ExecuteThreadTotalCount", var2);
         var2.setValue("description", "<p>The total number of threads in the pool.</p> ");
      }

      if (!var1.containsKey("ExecuteThreads")) {
         var3 = "getExecuteThreads";
         var4 = null;
         var2 = new PropertyDescriptor("ExecuteThreads", ThreadPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ExecuteThreads", var2);
         var2.setValue("description", "<p>An array of the threads currently processing work in the active thread pool.</p> ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.runtime.ExecuteThread")};
         var2.setValue("see", var5);
      }

      if (!var1.containsKey("HealthState")) {
         var3 = "getHealthState";
         var4 = null;
         var2 = new PropertyDescriptor("HealthState", ThreadPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("HealthState", var2);
         var2.setValue("description", "<p>The health state of this pool.</p> ");
      }

      if (!var1.containsKey("HoggingThreadCount")) {
         var3 = "getHoggingThreadCount";
         var4 = null;
         var2 = new PropertyDescriptor("HoggingThreadCount", ThreadPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("HoggingThreadCount", var2);
         var2.setValue("description", "<p> The threads that are being held by a request right now. These threads will either be declared as stuck after the configured timeout or will return to the pool before that. The self-tuning mechanism will backfill if necessary. </p> ");
      }

      if (!var1.containsKey("MinThreadsConstraintsCompleted")) {
         var3 = "getMinThreadsConstraintsCompleted";
         var4 = null;
         var2 = new PropertyDescriptor("MinThreadsConstraintsCompleted", ThreadPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("MinThreadsConstraintsCompleted", var2);
         var2.setValue("description", "<p>Number of requests with min threads constraint picked up out of order for execution immediately since their min threads requirement was not met. This does not include the case where threads are idle during schedule.</p> ");
      }

      if (!var1.containsKey("MinThreadsConstraintsPending")) {
         var3 = "getMinThreadsConstraintsPending";
         var4 = null;
         var2 = new PropertyDescriptor("MinThreadsConstraintsPending", ThreadPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("MinThreadsConstraintsPending", var2);
         var2.setValue("description", "<p>Number of requests that should be executed now to satisfy the min threads requirement.</p> ");
      }

      if (!var1.containsKey("PendingUserRequestCount")) {
         var3 = "getPendingUserRequestCount";
         var4 = null;
         var2 = new PropertyDescriptor("PendingUserRequestCount", ThreadPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("PendingUserRequestCount", var2);
         var2.setValue("description", "<p>The number of pending user requests in the priority queue. The priority queue contains requests from internal subsystems and users. This is just the count of all user requests.</p> ");
      }

      if (!var1.containsKey("QueueLength")) {
         var3 = "getQueueLength";
         var4 = null;
         var2 = new PropertyDescriptor("QueueLength", ThreadPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("QueueLength", var2);
         var2.setValue("description", "<p>The number of pending requests in the priority queue. This is the total of internal system requests and user requests.</p> ");
      }

      if (!var1.containsKey("SharedCapacityForWorkManagers")) {
         var3 = "getSharedCapacityForWorkManagers";
         var4 = null;
         var2 = new PropertyDescriptor("SharedCapacityForWorkManagers", ThreadPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("SharedCapacityForWorkManagers", var2);
         var2.setValue("description", "<p>Maximum amount of requests that can be accepted in the priority queue. Note that a request with higher priority will be accepted in place of a lower priority request already in the queue even after the threshold is reached. The lower priority request is kept waiting in the queue till all high priority requests are executed. Also note that further enqueues of the low priority requests are rejected right away. </p> ");
      }

      if (!var1.containsKey("StandbyThreadCount")) {
         var3 = "getStandbyThreadCount";
         var4 = null;
         var2 = new PropertyDescriptor("StandbyThreadCount", ThreadPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("StandbyThreadCount", var2);
         var2.setValue("description", "<p> The number of threads in the standby pool. Threads that are not needed to handle the present work load are designated as standby and added to the standby pool. These threads are activated when more threads are needed. </p> ");
      }

      if (!var1.containsKey("Throughput")) {
         var3 = "getThroughput";
         var4 = null;
         var2 = new PropertyDescriptor("Throughput", ThreadPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("Throughput", var2);
         var2.setValue("description", "<p>The mean number of requests completed per second.</p> ");
      }

      if (!var1.containsKey("Suspended")) {
         var3 = "isSuspended";
         var4 = null;
         var2 = new PropertyDescriptor("Suspended", ThreadPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("Suspended", var2);
         var2.setValue("description", "<p>Indicates if the RequestManager is suspended. A suspended manager will not dequeue work and dispatch threads till it is resumed.</p> ");
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
      Method var3 = ThreadPoolRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = ThreadPoolRuntimeMBean.class.getMethod("getExecuteThread", String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "The execute thread with the given thread name. ");
         var2.setValue("role", "operation");
         var2.setValue("rolePermitAll", Boolean.TRUE);
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
