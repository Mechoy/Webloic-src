package weblogic.server;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.CoherenceServerLifeCycleTaskRuntimeMBean;

public class CoherenceServerLifeCycleTaskRuntimeBeanInfo extends ServerLifeCycleTaskRuntimeBeanInfo {
   public static Class INTERFACE_CLASS = CoherenceServerLifeCycleTaskRuntimeMBean.class;

   public CoherenceServerLifeCycleTaskRuntimeBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public CoherenceServerLifeCycleTaskRuntimeBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = CoherenceServerLifeCycleTaskRuntime.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.server");
      String var3 = (new String("Exposes monitoring information about a server's life cycle. Remote clients as well as clients running within a server can access this information. <p> An operation (task) to change a server's state will fork a separate thread to perform the actual work and immediately return an instance of this MBean to the caller. The caller can then use this MBean to track the task's progress as desired.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.CoherenceServerLifeCycleTaskRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("BeginTime")) {
         var3 = "getBeginTime";
         var4 = null;
         var2 = new PropertyDescriptor("BeginTime", CoherenceServerLifeCycleTaskRuntimeMBean.class, var3, var4);
         var1.put("BeginTime", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("Description")) {
         var3 = "getDescription";
         var4 = null;
         var2 = new PropertyDescriptor("Description", CoherenceServerLifeCycleTaskRuntimeMBean.class, var3, var4);
         var1.put("Description", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("EndTime")) {
         var3 = "getEndTime";
         var4 = null;
         var2 = new PropertyDescriptor("EndTime", CoherenceServerLifeCycleTaskRuntimeMBean.class, var3, var4);
         var1.put("EndTime", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("Error")) {
         var3 = "getError";
         var4 = null;
         var2 = new PropertyDescriptor("Error", CoherenceServerLifeCycleTaskRuntimeMBean.class, var3, var4);
         var1.put("Error", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("Operation")) {
         var3 = "getOperation";
         var4 = null;
         var2 = new PropertyDescriptor("Operation", CoherenceServerLifeCycleTaskRuntimeMBean.class, var3, var4);
         var1.put("Operation", var2);
         var2.setValue("description", "Gets the name of the method that was invoked on the ServerLifeCycleRuntime to initiate this task. ");
      }

      if (!var1.containsKey("ParentTask")) {
         var3 = "getParentTask";
         var4 = null;
         var2 = new PropertyDescriptor("ParentTask", CoherenceServerLifeCycleTaskRuntimeMBean.class, var3, var4);
         var1.put("ParentTask", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("ServerName")) {
         var3 = "getServerName";
         var4 = null;
         var2 = new PropertyDescriptor("ServerName", CoherenceServerLifeCycleTaskRuntimeMBean.class, var3, var4);
         var1.put("ServerName", var2);
         var2.setValue("description", "The name of the server. ");
      }

      if (!var1.containsKey("Status")) {
         var3 = "getStatus";
         var4 = null;
         var2 = new PropertyDescriptor("Status", CoherenceServerLifeCycleTaskRuntimeMBean.class, var3, var4);
         var1.put("Status", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("SubTasks")) {
         var3 = "getSubTasks";
         var4 = null;
         var2 = new PropertyDescriptor("SubTasks", CoherenceServerLifeCycleTaskRuntimeMBean.class, var3, var4);
         var1.put("SubTasks", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("Running")) {
         var3 = "isRunning";
         var4 = null;
         var2 = new PropertyDescriptor("Running", CoherenceServerLifeCycleTaskRuntimeMBean.class, var3, var4);
         var1.put("Running", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("SystemTask")) {
         var3 = "isSystemTask";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSystemTask";
         }

         var2 = new PropertyDescriptor("SystemTask", CoherenceServerLifeCycleTaskRuntimeMBean.class, var3, var4);
         var1.put("SystemTask", var2);
         var2.setValue("description", " ");
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
      Method var3 = CoherenceServerLifeCycleTaskRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = CoherenceServerLifeCycleTaskRuntimeMBean.class.getMethod("cancel");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = CoherenceServerLifeCycleTaskRuntimeMBean.class.getMethod("printLog", PrintWriter.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
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
