package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class JDBCMultiPoolMBeanImplBeanInfo extends DeploymentMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JDBCMultiPoolMBean.class;

   public JDBCMultiPoolMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public JDBCMultiPoolMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = JDBCMultiPoolMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("obsolete", "9.0.0.0");
      var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.configuration.JDBCSystemResourceMBean} ");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This MBean represents a JDBC Multipool, which is a pool of JDBC connection pools.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.JDBCMultiPoolMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("ACLName")) {
         var3 = "getACLName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setACLName";
         }

         var2 = new PropertyDescriptor("ACLName", JDBCMultiPoolMBean.class, var3, var4);
         var1.put("ACLName", var2);
         var2.setValue("description", "<p>The access control list (ACL) used to control access to this MultiPool.</p> ");
         var2.setValue("secureValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      String[] var5;
      if (!var1.containsKey("AlgorithmType")) {
         var3 = "getAlgorithmType";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAlgorithmType";
         }

         var2 = new PropertyDescriptor("AlgorithmType", JDBCMultiPoolMBean.class, var3, var4);
         var1.put("AlgorithmType", var2);
         var2.setValue("description", "<p>The algorithm type for this Multipool.</p>  <p>Multipools support the following algorithm types:</p>  <ul> <li><b>High availability</b> <p>Connection requests are sent to the first connection pool in the list; if the request fails, the request is sent to the next connection pool in the list, and so forth. The process is repeated until a valid connection is obtained, or until the end of the list is reached, in which case an exception will be thrown.</p>  <p>Note that unless FailoverRequestIfBusy=\"true\", the Multipool will only move to the next pool in the list when there is a real problem with the pool, for example the database is down or the pool disabled. For the cases where all connections are busy, the Multipool behaves as a single pool and an exception is thrown.</p> </li>  <li><b>Load balancing</b> <p>The Multipool will distribute the connection requests evenly to its member pools. This algorithm also performs the same failover behavior as the high availability algorithm.</p> </li> </ul> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getFailoverRequestIfBusy()")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, "High-Availability");
         var2.setValue("legalValues", new Object[]{"High-Availability", "Load-Balancing", "Failover"});
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ConnectionPoolFailoverCallbackHandler")) {
         var3 = "getConnectionPoolFailoverCallbackHandler";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnectionPoolFailoverCallbackHandler";
         }

         var2 = new PropertyDescriptor("ConnectionPoolFailoverCallbackHandler", JDBCMultiPoolMBean.class, var3, var4);
         var1.put("ConnectionPoolFailoverCallbackHandler", var2);
         var2.setValue("description", "Returns the current value of connectionPoolFailoverCallbackHandler ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.jdbc.extensions.ConnectionPoolFailoverCallback")};
         var2.setValue("see", var5);
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("FailoverRequestIfBusy")) {
         var3 = "getFailoverRequestIfBusy";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFailoverRequestIfBusy";
         }

         var2 = new PropertyDescriptor("FailoverRequestIfBusy", JDBCMultiPoolMBean.class, var3, var4);
         var1.put("FailoverRequestIfBusy", var2);
         var2.setValue("description", "If enabled, application requests for connections will be routed to alternate pools if current pool is busy. This is only relevant when running with the HIGH_ALGORITHM algorithm. Default implies feature is disabled. ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("HealthCheckFrequencySeconds")) {
         var3 = "getHealthCheckFrequencySeconds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setHealthCheckFrequencySeconds";
         }

         var2 = new PropertyDescriptor("HealthCheckFrequencySeconds", JDBCMultiPoolMBean.class, var3, var4);
         var1.put("HealthCheckFrequencySeconds", var2);
         var2.setValue("description", "Returns the current value of healthCheckFrequencySeconds ");
         setPropertyDescriptorDefault(var2, new Integer(300));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", JDBCMultiPoolMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("PoolList")) {
         var3 = "getPoolList";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPoolList";
         }

         var2 = new PropertyDescriptor("PoolList", JDBCMultiPoolMBean.class, var3, var4);
         var1.put("PoolList", var2);
         var2.setValue("description", "<p>The list of connection pools in the MultiPool.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Targets")) {
         var3 = "getTargets";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTargets";
         }

         var2 = new PropertyDescriptor("Targets", JDBCMultiPoolMBean.class, var3, var4);
         var1.put("Targets", var2);
         var2.setValue("description", "<p>You must select a target on which an MBean will be deployed from this list of the targets in the current domain on which this item can be deployed. Targets must be either servers or clusters. The deployment will only occur once if deployments overlap.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("adder", "addTarget");
         var2.setValue("remover", "removeTarget");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = JDBCMultiPoolMBean.class.getMethod("addTarget", TargetMBean.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("target", "The feature to be added to the Target attribute ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>You can add a target to specify additional servers on which the deployment can be deployed. The targets must be either clusters or servers.</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "Targets");
      }

      var3 = JDBCMultiPoolMBean.class.getMethod("removeTarget", TargetMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("target", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes the value of the addTarget attribute.</p> ");
         String[] var6 = new String[]{BeanInfoHelper.encodeEntities("#addTarget")};
         var2.setValue("see", var6);
         var2.setValue("role", "collection");
         var2.setValue("property", "Targets");
      }

   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = JDBCMultiPoolMBean.class.getMethod("freezeCurrentValue", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has not been set explicitly, and if the attribute has a default value, this operation forces the MBean to persist the default value.</p>  <p>Unless you use this operation, the default value is not saved and is subject to change if you update to a newer release of WebLogic Server. Invoking this operation isolates this MBean from the effects of such changes.</p>  <dl> <dt>Note:</dt>  <dd> <p>To insure that you are freezing the default value, invoke the <code>restoreDefaultValue</code> operation before you invoke this.</p> </dd> </dl>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute for which some other value has been set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JDBCMultiPoolMBean.class.getMethod("restoreDefaultValue", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5) && !this.readOnly) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has a default value, this operation removes any value that has been set explicitly and causes the attribute to use the default value.</p>  <p>Default values are subject to change if you update to a newer release of WebLogic Server. To prevent the value from changing if you update to a newer release, invoke the <code>freezeCurrentValue</code> operation.</p>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute that is already using the default.</p> ");
         var2.setValue("role", "operation");
         var2.setValue("impact", "action");
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
