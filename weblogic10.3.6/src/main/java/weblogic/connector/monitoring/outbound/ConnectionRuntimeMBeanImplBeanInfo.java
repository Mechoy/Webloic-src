package weblogic.connector.monitoring.outbound;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.ConnectorConnectionRuntimeMBean;

public class ConnectionRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ConnectorConnectionRuntimeMBean.class;

   public ConnectionRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ConnectionRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ConnectionRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.connector.monitoring.outbound");
      String var3 = (new String("<p>This class is used for monitoring individual WebLogic Connector connections</p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.ConnectorConnectionRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("ActiveHandlesCurrentCount")) {
         var3 = "getActiveHandlesCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("ActiveHandlesCurrentCount", ConnectorConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("ActiveHandlesCurrentCount", var2);
         var2.setValue("description", "<p>The current total active connection handles for this connection.</p> ");
      }

      if (!var1.containsKey("ActiveHandlesHighCount")) {
         var3 = "getActiveHandlesHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("ActiveHandlesHighCount", ConnectorConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("ActiveHandlesHighCount", var2);
         var2.setValue("description", "<p>The high water mark of active connection handles for this connection since the connection was created.</p> ");
      }

      if (!var1.containsKey("ConnectionFactoryClassName")) {
         var3 = "getConnectionFactoryClassName";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionFactoryClassName", ConnectorConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectionFactoryClassName", var2);
         var2.setValue("description", "<p>Returns the connection factory class name.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("CreationDurationTime")) {
         var3 = "getCreationDurationTime";
         var4 = null;
         var2 = new PropertyDescriptor("CreationDurationTime", ConnectorConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("CreationDurationTime", var2);
         var2.setValue("description", "<p>Return the time taken to create the connection.</p> ");
      }

      if (!var1.containsKey("EISProductName")) {
         var3 = "getEISProductName";
         var4 = null;
         var2 = new PropertyDescriptor("EISProductName", ConnectorConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("EISProductName", var2);
         var2.setValue("description", "Returns the EISProductName associated with the ManagedConnection's MetaData ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("EISProductVersion")) {
         var3 = "getEISProductVersion";
         var4 = null;
         var2 = new PropertyDescriptor("EISProductVersion", ConnectorConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("EISProductVersion", var2);
         var2.setValue("description", "<p>Returns the EISProductVersion associated with the ManagedConnection's MetaData.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("HandlesCreatedTotalCount")) {
         var3 = "getHandlesCreatedTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("HandlesCreatedTotalCount", ConnectorConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("HandlesCreatedTotalCount", var2);
         var2.setValue("description", "<p>The total number of connection handles created for this connection since the connection was created.</p> ");
      }

      if (!var1.containsKey("LastUsage")) {
         var3 = "getLastUsage";
         var4 = null;
         var2 = new PropertyDescriptor("LastUsage", ConnectorConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("LastUsage", var2);
         var2.setValue("description", "<p>The last usage time stamp for the connection in milliseconds, as returned by <code>System.currentTimeMillis()</code>.</p> ");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("LastUsageString")) {
         var3 = "getLastUsageString";
         var4 = null;
         var2 = new PropertyDescriptor("LastUsageString", ConnectorConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("LastUsageString", var2);
         var2.setValue("description", "<p>The last usage time stamp for the connection as a string.</p> ");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("ManagedConnectionFactoryClassName")) {
         var3 = "getManagedConnectionFactoryClassName";
         var4 = null;
         var2 = new PropertyDescriptor("ManagedConnectionFactoryClassName", ConnectorConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("ManagedConnectionFactoryClassName", var2);
         var2.setValue("description", "<p>Returns the managed connection factory class name.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxConnections")) {
         var3 = "getMaxConnections";
         var4 = null;
         var2 = new PropertyDescriptor("MaxConnections", ConnectorConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("MaxConnections", var2);
         var2.setValue("description", "<p>Returns the MaxConnections associated with the ManagedConnection's MetaData</p> ");
      }

      if (!var1.containsKey("ReserveDurationTime")) {
         var3 = "getReserveDurationTime";
         var4 = null;
         var2 = new PropertyDescriptor("ReserveDurationTime", ConnectorConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("ReserveDurationTime", var2);
         var2.setValue("description", "<p>Get the time taken to reserve this connection.</p> ");
      }

      if (!var1.containsKey("ReserveTime")) {
         var3 = "getReserveTime";
         var4 = null;
         var2 = new PropertyDescriptor("ReserveTime", ConnectorConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("ReserveTime", var2);
         var2.setValue("description", "<p>Return the last time the connection was reserved.</p> ");
      }

      if (!var1.containsKey("StackTrace")) {
         var3 = "getStackTrace";
         var4 = null;
         var2 = new PropertyDescriptor("StackTrace", ConnectorConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("StackTrace", var2);
         var2.setValue("description", "<p>The stack trace for the current connection, which will be blank unless connection-profiling-enabled is set to true in weblogic-ra.xml</p> ");
         var2.setValue("deprecated", " ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("TransactionId")) {
         var3 = "getTransactionId";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionId", ConnectorConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionId", var2);
         var2.setValue("description", "<p>Get the Transaction ID of the transaction that this connection is being used with.</p> ");
      }

      if (!var1.containsKey("UserName")) {
         var3 = "getUserName";
         var4 = null;
         var2 = new PropertyDescriptor("UserName", ConnectorConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("UserName", var2);
         var2.setValue("description", "<p>Returns the UserName associated with the ManagedConnection's MetaData</p> ");
      }

      if (!var1.containsKey("CurrentlyInUse")) {
         var3 = "isCurrentlyInUse";
         var4 = null;
         var2 = new PropertyDescriptor("CurrentlyInUse", ConnectorConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("CurrentlyInUse", var2);
         var2.setValue("description", "<p>Indicates whether the connection is currently in use.</p> ");
      }

      if (!var1.containsKey("Deletable")) {
         var3 = "isDeletable";
         var4 = null;
         var2 = new PropertyDescriptor("Deletable", ConnectorConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("Deletable", var2);
         var2.setValue("description", "<p>Indicates whether the connection can be closed manually through the console.</p> ");
      }

      if (!var1.containsKey("Idle")) {
         var3 = "isIdle";
         var4 = null;
         var2 = new PropertyDescriptor("Idle", ConnectorConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("Idle", var2);
         var2.setValue("description", "<p>Indicates whether the connection has been idle for a period extending beyond the configured maximum.</p> ");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("InTransaction")) {
         var3 = "isInTransaction";
         var4 = null;
         var2 = new PropertyDescriptor("InTransaction", ConnectorConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("InTransaction", var2);
         var2.setValue("description", "<p>Indicates whether the connection is currently in use in a transaction.</p> ");
      }

      if (!var1.containsKey("Shared")) {
         var3 = "isShared";
         var4 = null;
         var2 = new PropertyDescriptor("Shared", ConnectorConnectionRuntimeMBean.class, var3, (String)var4);
         var1.put("Shared", var2);
         var2.setValue("description", "<p>Indicates whether the connection is currently being shared by more than one invoker.</p> ");
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
      Method var3 = ConnectorConnectionRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorConnectionRuntimeMBean.class.getMethod("delete");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Provides a way to manually close a connection through the console.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorConnectionRuntimeMBean.class.getMethod("testConnection");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Test the connection. Returns true if the test was successful.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorConnectionRuntimeMBean.class.getMethod("hasError");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Return a flag indicating whether the connection has an error or not. A \"true\" is returned if there is an error.</p> ");
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
