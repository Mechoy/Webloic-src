package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class ExecuteQueueMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ExecuteQueueMBean.class;

   public ExecuteQueueMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ExecuteQueueMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ExecuteQueueMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This bean is used to configure an execute queue and its associated thread pool.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.ExecuteQueueMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("QueueLength")) {
         var3 = "getQueueLength";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setQueueLength";
         }

         var2 = new PropertyDescriptor("QueueLength", ExecuteQueueMBean.class, var3, var4);
         var1.put("QueueLength", var2);
         var2.setValue("description", "<p>The maximum number of simultaneous requests that this server can hold in the queue.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(65536));
         var2.setValue("legalMax", new Integer(1073741824));
         var2.setValue("legalMin", new Integer(256));
      }

      if (!var1.containsKey("QueueLengthThresholdPercent")) {
         var3 = "getQueueLengthThresholdPercent";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setQueueLengthThresholdPercent";
         }

         var2 = new PropertyDescriptor("QueueLengthThresholdPercent", ExecuteQueueMBean.class, var3, var4);
         var1.put("QueueLengthThresholdPercent", var2);
         var2.setValue("description", "<p>The percentage of the Queue Length size that can be reached before this server indicates an overflow condition for the queue. If the overflow condition is reached and the current thread count has not reached the ThreadsMaximum value, then ThreadsIncrease number of threads are added.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(90));
         var2.setValue("legalMax", new Integer(99));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ThreadCount")) {
         var3 = "getThreadCount";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setThreadCount";
         }

         var2 = new PropertyDescriptor("ThreadCount", ExecuteQueueMBean.class, var3, var4);
         var1.put("ThreadCount", var2);
         var2.setValue("description", "<p>The number of threads assigned to this queue.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(15));
         var2.setValue("legalMax", new Integer(65536));
         var2.setValue("legalMin", new Integer(0));
      }

      if (!var1.containsKey("ThreadsIncrease")) {
         var3 = "getThreadsIncrease";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setThreadsIncrease";
         }

         var2 = new PropertyDescriptor("ThreadsIncrease", ExecuteQueueMBean.class, var3, var4);
         var1.put("ThreadsIncrease", var2);
         var2.setValue("description", "<p>Specifies the number of threads to increase the queue length when the queue length theshold is reached. This threshold is determined by the QueueLengthThresholdPercent value.</p>  <p>The following consideration applies to the dynamic nature of ThreadsMaximum and ThreadsIncrease attributes. If any of these attributes change during runtime, the changed value comes into effect when the next request is submitted to the execute queue and the scheduler decides to increase threads depending on the queue threshold conditions.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("legalMax", new Integer(65536));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ThreadsMaximum")) {
         var3 = "getThreadsMaximum";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setThreadsMaximum";
         }

         var2 = new PropertyDescriptor("ThreadsMaximum", ExecuteQueueMBean.class, var3, var4);
         var1.put("ThreadsMaximum", var2);
         var2.setValue("description", "<p>The maximum number of threads that this queue is allowed to have; this value prevents WebLogic Server from creating an overly high thread count in the queue in response to continual overflow conditions.</p>  <p>A note about dynamic nature of ThreadsMaximum and ThreadsIncrease attributes. If any of these attributes change during runtime, the changed value comes into effect when the next request is submitted to the execute queue and the scheduler decides to increase threads depending on the queue threshold conditions. Imagine a scenario where the queue capacity has already reached the max threshold and the current thread count is already equal to ThreadsMaximum value. If more work is coming into the queue and the administrator wishes to increase the ThreadsMaximum a little to add a few more threads, he/she can do so by changing these attributes dynamically. Please note that the changed value is evaluated when the next request is submitted.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(400));
         var2.setValue("legalMax", new Integer(65536));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ThreadsMinimum")) {
         var3 = "getThreadsMinimum";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setThreadsMinimum";
         }

         var2 = new PropertyDescriptor("ThreadsMinimum", ExecuteQueueMBean.class, var3, var4);
         var1.put("ThreadsMinimum", var2);
         var2.setValue("description", "<p>The minimum number of threads that WebLogic Server will maintain in the queue.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(5));
         var2.setValue("legalMax", new Integer(65536));
         var2.setValue("legalMin", new Integer(0));
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
