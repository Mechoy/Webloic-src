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
import weblogic.management.runtime.ServerLifeCycleRuntimeMBean;

public class ServerLifeCycleRuntimeBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ServerLifeCycleRuntimeMBean.class;

   public ServerLifeCycleRuntimeBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ServerLifeCycleRuntimeBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ServerLifeCycleRuntime.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.server");
      String var3 = (new String("<p>Provides methods that transition servers from one state to another. This class is instantiated only on the Administration Server, but you can use it to transition the states of Managed Servers as well as Administration Servers.</p>  <p>You cannot use it to start an Administration Server, and if you want to use it to start Managed Servers, you must first set up a Node Manager on each Managed Server's host machine.</p>  <p>If you want to use the methods that transition a server into the <code>ADMIN</code> state, you must first set up an administration channel for that server.</p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.ServerLifeCycleRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (BeanInfoHelper.isVersionCompliant("10.3.3.0", (String)null, this.targetVersion) && !var1.containsKey("MiddlewareHome")) {
         var3 = "getMiddlewareHome";
         var4 = null;
         var2 = new PropertyDescriptor("MiddlewareHome", ServerLifeCycleRuntimeMBean.class, var3, (String)var4);
         var1.put("MiddlewareHome", var2);
         var2.setValue("description", "<p>The Oracle Middleware installation directory. </p> ");
         var2.setValue("since", "10.3.3.0");
      }

      if (!var1.containsKey("NodeManagerRestartCount")) {
         var3 = "getNodeManagerRestartCount";
         var4 = null;
         var2 = new PropertyDescriptor("NodeManagerRestartCount", ServerLifeCycleRuntimeMBean.class, var3, (String)var4);
         var1.put("NodeManagerRestartCount", var2);
         var2.setValue("description", "Number of times the server has been restarted using the NodeManager since creation. The first start does not count. The count is valid only if the NodeManager is used to start and restart the server everytime. ");
      }

      if (!var1.containsKey("State")) {
         var3 = "getState";
         var4 = null;
         var2 = new PropertyDescriptor("State", ServerLifeCycleRuntimeMBean.class, var3, (String)var4);
         var1.put("State", var2);
         var2.setValue("description", "<p>The current state of the server.</p> Server states are described in ");
      }

      if (!var1.containsKey("StateVal")) {
         var3 = "getStateVal";
         var4 = null;
         var2 = new PropertyDescriptor("StateVal", ServerLifeCycleRuntimeMBean.class, var3, (String)var4);
         var1.put("StateVal", var2);
         var2.setValue("description", "<p>An integer that identifies the current state of the server. Values range from <code>0</code> to <code>8</code>.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("Tasks")) {
         var3 = "getTasks";
         var4 = null;
         var2 = new PropertyDescriptor("Tasks", ServerLifeCycleRuntimeMBean.class, var3, (String)var4);
         var1.put("Tasks", var2);
         var2.setValue("description", "Get preexisting Server Lifecycle Tasks ");
         var2.setValue("relationship", "containment");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.3.0", (String)null, this.targetVersion) && !var1.containsKey("WeblogicHome")) {
         var3 = "getWeblogicHome";
         var4 = null;
         var2 = new PropertyDescriptor("WeblogicHome", ServerLifeCycleRuntimeMBean.class, var3, (String)var4);
         var1.put("WeblogicHome", var2);
         var2.setValue("description", "<p>The directory where the WebLogic Server instance (server) is installed, without the trailing \"/server\".</p> ");
         var2.setValue("since", "10.3.3.0");
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
      Method var3 = ServerLifeCycleRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = ServerLifeCycleRuntimeMBean.class.getMethod("start");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Uses the Node Manager to start a Managed Server.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ServerLifeCycleRuntimeMBean.class.getMethod("startInStandby");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Uses the Node Manager to start a Managed Server and place it in the <code>STANDBY</code> state.</p>  <p>The server transitions through the following states:<br clear=\"none\" /> <code>SHUTDOWN</code>&gt;<code>SUSPENDING</code>&gt;<code>STANDBY</code>. </p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = ServerLifeCycleRuntimeMBean.class.getMethod("resume");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Transitions the server from <code>ADMIN</code> to <code>RUNNING</code> state.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ServerLifeCycleRuntimeMBean.class.getMethod("suspend");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      String[] var5;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Gracefully suspends server to <code>ADMIN</code> state. New requests are rejected and inflight work is allowed to complete.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#suspend(int, boolean)")};
         var2.setValue("see", var5);
         var2.setValue("role", "operation");
      }

      var3 = ServerLifeCycleRuntimeMBean.class.getMethod("suspend", Integer.TYPE, Boolean.TYPE);
      ParameterDescriptor[] var8 = new ParameterDescriptor[]{createParameterDescriptor("timeout", "Seconds to wait for server to transition gracefully. The server calls {@link #forceSuspend()} after timeout. "), createParameterDescriptor("ignoreSessions", "drop inflight HTTP sessions during graceful suspend ")};
      String var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, var8);
         String[] var6 = new String[]{BeanInfoHelper.encodeEntities("ServerLifecycleException server failed to suspend gracefully.            A {@link #forceSuspend()} or a {@link #forceShutdown()} operation can be   invoked.")};
         var2.setValue("throws", var6);
         var1.put(var7, var2);
         var2.setValue("description", "<p>Transitions the server from <code>RUNNING</code> to <code>ADMIN</code> state gracefully.</p>  <p>Applications are in admin mode. Inflight work is completed.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ServerLifeCycleRuntimeMBean.class.getMethod("forceSuspend");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var5 = new String[]{BeanInfoHelper.encodeEntities("ServerLifecycleException server failed to force suspend.            A {@link #forceShutdown()} operation can be invoked.")};
         var2.setValue("throws", var5);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Transitions the server from RUNNING to ADMIN state forcefully cancelling inflight work.</p> <p>Work that cannot be cancelled is dropped. Applications are brought into the admin mode forcefully.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ServerLifeCycleRuntimeMBean.class.getMethod("shutdown");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Gracefully transitions a server to the <code>SHUTDOWN</code> state. The server completes all current work before it shuts down. ");
         var2.setValue("role", "operation");
      }

      var3 = ServerLifeCycleRuntimeMBean.class.getMethod("shutdown", Integer.TYPE, Boolean.TYPE);
      var8 = new ParameterDescriptor[]{createParameterDescriptor("timeout", "Number of seconds to wait before aborting inflight work and force shutting down the server. "), createParameterDescriptor("ignoreSessions", "Set to <code>true</code> to ignore pending HTTP sessions during inflight work handling. ")};
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, var8);
         var1.put(var7, var2);
         var2.setValue("description", "<p>Gracefully shutdown the server after handling inflight work. The following inflight work is handled :</p>  <ul> <li> <p>Pending transaction's and TLOG checkpoint</p> </li>  <li> <p>Pending HTTP sessions</p> </li>  <li> <p>Pending JMS work</p> </li>  <li> <p>Pending work in the Work Managers</p> </li>  <li> <p>RMI requests with tx context or administrator calls</p> </li> </ul> ");
         var2.setValue("role", "operation");
      }

      var3 = ServerLifeCycleRuntimeMBean.class.getMethod("forceShutdown");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Immediately transitions a server to the <code>SHUTDOWN</code> state. The server immediately terminates all current work, moves through the <code>SHUTTING_DOWN</code> state, and ends in the <code>SHUTDOWN</code> state.</p>  <p>You can forcefully shut down a server from any state except <code>UNKNOWN</code>.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ServerLifeCycleRuntimeMBean.class.getMethod("clearOldServerLifeCycleTaskRuntimes");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "Removes all ServerLifeCycleTaskRuntimeMBeans that have completed and been around for over 30 minutes. ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.3.0", (String)null, this.targetVersion)) {
         var3 = ServerLifeCycleRuntimeMBean.class.getMethod("getIPv4URL", String.class);
         var8 = new ParameterDescriptor[]{createParameterDescriptor("protocol", "the desired protocol ")};
         var7 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var7)) {
            var2 = new MethodDescriptor(var3, var8);
            var2.setValue("since", "10.3.3.0");
            var1.put(var7, var2);
            var2.setValue("description", "<p>The IPv4 URL that clients use when connecting to this server using the specified protocol.</p> ");
            var2.setValue("role", "operation");
            var2.setValue("since", "10.3.3.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.3.0", (String)null, this.targetVersion)) {
         var3 = ServerLifeCycleRuntimeMBean.class.getMethod("getIPv6URL", String.class);
         var8 = new ParameterDescriptor[]{createParameterDescriptor("protocol", "the desired protocol ")};
         var7 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var7)) {
            var2 = new MethodDescriptor(var3, var8);
            var2.setValue("since", "10.3.3.0");
            var1.put(var7, var2);
            var2.setValue("description", "<p>The IPv6 URL that clients use when connecting to this server using the specified protocol.</p> ");
            var2.setValue("role", "operation");
            var2.setValue("since", "10.3.3.0");
         }
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
