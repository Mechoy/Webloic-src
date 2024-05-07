package weblogic.server;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.CoherenceServerLifeCycleRuntimeMBean;

public class CoherenceServerLifeCycleRuntimeBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = CoherenceServerLifeCycleRuntimeMBean.class;

   public CoherenceServerLifeCycleRuntimeBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public CoherenceServerLifeCycleRuntimeBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = CoherenceServerLifeCycleRuntime.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.server");
      String var3 = (new String("<p>Provides methods that transition servers from one state to another. This class is instantiated only on the Administration Server, but you can use it to transition the states of all managed Coherence cache servers.</p>  <p>To start Coherence cache servers, you must first configure a Node Manager on each Coherence cache server's host machine.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.CoherenceServerLifeCycleRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("NodeManagerRestartCount")) {
         var3 = "getNodeManagerRestartCount";
         var4 = null;
         var2 = new PropertyDescriptor("NodeManagerRestartCount", CoherenceServerLifeCycleRuntimeMBean.class, var3, (String)var4);
         var1.put("NodeManagerRestartCount", var2);
         var2.setValue("description", "The number of times the server has been restarted using the Node Manager since its creation. The first start does not count. The count is valid only if the Node Manager is used to start and restart the server every time. ");
      }

      if (!var1.containsKey("State")) {
         var3 = "getState";
         var4 = null;
         var2 = new PropertyDescriptor("State", CoherenceServerLifeCycleRuntimeMBean.class, var3, (String)var4);
         var1.put("State", var2);
         var2.setValue("description", "<p>The current state of the server.</p> Server states are described in \"Managing Server Startup and Shutdown for Oracle WebLogic Server.\" ");
      }

      if (!var1.containsKey("StateVal")) {
         var3 = "getStateVal";
         var4 = null;
         var2 = new PropertyDescriptor("StateVal", CoherenceServerLifeCycleRuntimeMBean.class, var3, (String)var4);
         var1.put("StateVal", var2);
         var2.setValue("description", "<p>An integer that identifies the current state of the server. Values range from <code>0</code> to <code>8</code>.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("Tasks")) {
         var3 = "getTasks";
         var4 = null;
         var2 = new PropertyDescriptor("Tasks", CoherenceServerLifeCycleRuntimeMBean.class, var3, (String)var4);
         var1.put("Tasks", var2);
         var2.setValue("description", "Gets pre-existing server life cycle tasks. ");
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
      Method var3 = CoherenceServerLifeCycleRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = CoherenceServerLifeCycleRuntimeMBean.class.getMethod("start");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Uses Node Manager to start a Coherence cache server.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = CoherenceServerLifeCycleRuntimeMBean.class.getMethod("forceShutdown");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Immediately transitions a server to the <code>SHUTDOWN</code> state. The server immediately terminates all current work, moves through the <code>SHUTTING_DOWN</code> state, and ends in the <code>SHUTDOWN</code> state.</p>  <p>You can forcefully shut down a server from any state except <code>UNKNOWN</code>.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = CoherenceServerLifeCycleRuntimeMBean.class.getMethod("clearOldServerLifeCycleTaskRuntimes");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "Removes all CoherenceServerLifeCycleTaskRuntimeMBeans that have completed and been around for over 30 minutes. ");
         var2.setValue("exclude", Boolean.TRUE);
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
