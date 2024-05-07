package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class OverloadProtectionMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = OverloadProtectionMBean.class;

   public OverloadProtectionMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public OverloadProtectionMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = OverloadProtectionMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This Mbean has attributes concerning server overload protection.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.OverloadProtectionMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("FailureAction")) {
         var3 = "getFailureAction";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFailureAction";
         }

         var2 = new PropertyDescriptor("FailureAction", OverloadProtectionMBean.class, var3, var4);
         var1.put("FailureAction", var2);
         var2.setValue("description", "Enable automatic forceshutdown of the server on failed state. The server self-health monitoring detects fatal failures and mark the server as failed. The server can be restarted using NodeManager or a HA agent. ");
         setPropertyDescriptorDefault(var2, "no-action");
         var2.setValue("legalValues", new Object[]{"no-action", "force-shutdown", "admin-state"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("FreeMemoryPercentHighThreshold")) {
         var3 = "getFreeMemoryPercentHighThreshold";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFreeMemoryPercentHighThreshold";
         }

         var2 = new PropertyDescriptor("FreeMemoryPercentHighThreshold", OverloadProtectionMBean.class, var3, var4);
         var1.put("FreeMemoryPercentHighThreshold", var2);
         var2.setValue("description", "Percentage free memory after which the server overload condition is cleared. WorkManagers stop performing overloadActions and start regular execution once the overload condition is cleared. ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("legalMax", new Integer(99));
         var2.setValue("legalMin", new Integer(0));
      }

      if (!var1.containsKey("FreeMemoryPercentLowThreshold")) {
         var3 = "getFreeMemoryPercentLowThreshold";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFreeMemoryPercentLowThreshold";
         }

         var2 = new PropertyDescriptor("FreeMemoryPercentLowThreshold", OverloadProtectionMBean.class, var3, var4);
         var1.put("FreeMemoryPercentLowThreshold", var2);
         var2.setValue("description", "Percentage free memory below which the server is considered overloaded. WorkManagers perform overloadAction once the server is marked as overloaded. ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("legalMax", new Integer(99));
         var2.setValue("legalMin", new Integer(0));
      }

      if (!var1.containsKey("PanicAction")) {
         var3 = "getPanicAction";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPanicAction";
         }

         var2 = new PropertyDescriptor("PanicAction", OverloadProtectionMBean.class, var3, var4);
         var1.put("PanicAction", var2);
         var2.setValue("description", "Exit the server process when the kernel encounters a panic condition like an unhandled OOME. An unhandled OOME could lead to inconsistent state and a server restart is advisable if backed by node manager or a HA agent. ");
         setPropertyDescriptorDefault(var2, "no-action");
         var2.setValue("legalValues", new Object[]{"no-action", "system-exit"});
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("ServerFailureTrigger")) {
         var3 = "getServerFailureTrigger";
         var4 = null;
         var2 = new PropertyDescriptor("ServerFailureTrigger", OverloadProtectionMBean.class, var3, var4);
         var1.put("ServerFailureTrigger", var2);
         var2.setValue("description", "Configure a trigger that marks the server as failed when the condition is met. A failed server in turn can be configured to shutdown or go into admin state. ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyServerFailureTrigger");
         var2.setValue("creator", "createServerFailureTrigger");
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("SharedCapacityForWorkManagers")) {
         var3 = "getSharedCapacityForWorkManagers";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSharedCapacityForWorkManagers";
         }

         var2 = new PropertyDescriptor("SharedCapacityForWorkManagers", OverloadProtectionMBean.class, var3, var4);
         var1.put("SharedCapacityForWorkManagers", var2);
         var2.setValue("description", "Total number of requests that can be present in the server. This includes requests that are enqueued and those under execution. <p> The server performs a differentiated denial of service on reaching the shared capacity. A request with higher priority will be accepted in place of a lower priority request already in the queue. The lower priority request is kept waiting in the queue till all high priority requests are executed. Further enqueues of the low priority requests are rejected right away. </p> ");
         setPropertyDescriptorDefault(var2, new Integer(65536));
         var2.setValue("legalMax", new Integer(1073741824));
         var2.setValue("legalMin", new Integer(1));
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      MethodDescriptor var2;
      Method var3;
      String var4;
      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = OverloadProtectionMBean.class.getMethod("createServerFailureTrigger");
         var4 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var4)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("since", "9.0.0.0");
            var1.put(var4, var2);
            var2.setValue("description", "Configure a trigger that marks the server as failed when the condition is met. A failed server in turn can be configured to shutdown or go into admin state. ");
            var2.setValue("role", "factory");
            var2.setValue("property", "ServerFailureTrigger");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = OverloadProtectionMBean.class.getMethod("destroyServerFailureTrigger");
         var4 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var4)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("since", "9.0.0.0");
            var1.put(var4, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "factory");
            var2.setValue("property", "ServerFailureTrigger");
            var2.setValue("since", "9.0.0.0");
         }
      }

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
