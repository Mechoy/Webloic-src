package weblogic.jdbc.common.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.j2ee.ComponentRuntimeMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.JDBCConnectionPoolRuntimeMBean;

public class ConnectionPoolRuntimeMBeanImplBeanInfo extends ComponentRuntimeMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JDBCConnectionPoolRuntimeMBean.class;

   public ConnectionPoolRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ConnectionPoolRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ConnectionPoolRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.runtime.JDBCDataSourceRuntimeMBean} ");
      var2.setValue("package", "weblogic.jdbc.common.internal");
      String var3 = (new String("<p>This class is used for monitoring a WebLogic JDBC component</p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe inteface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, <code>MBeanHome</code> inteface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> inteface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.JDBCConnectionPoolRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("ActiveConnectionsAverageCount")) {
         var3 = "getActiveConnectionsAverageCount";
         var4 = null;
         var2 = new PropertyDescriptor("ActiveConnectionsAverageCount", JDBCConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ActiveConnectionsAverageCount", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("ActiveConnectionsCurrentCount")) {
         var3 = "getActiveConnectionsCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("ActiveConnectionsCurrentCount", JDBCConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ActiveConnectionsCurrentCount", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("ActiveConnectionsHighCount")) {
         var3 = "getActiveConnectionsHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("ActiveConnectionsHighCount", JDBCConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ActiveConnectionsHighCount", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("ConnectionDelayTime")) {
         var3 = "getConnectionDelayTime";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionDelayTime", JDBCConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectionDelayTime", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("ConnectionLeakProfileCount")) {
         var3 = "getConnectionLeakProfileCount";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionLeakProfileCount", JDBCConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectionLeakProfileCount", var2);
         var2.setValue("description", "<p>Connection leak is a situation when connection from the pool was not closed explicitly by calling close() and was garbage collected.</p>  <p>This method should be used first before requesting connection leak profiles from the profile storage.</p> ");
      }

      if (!var1.containsKey("ConnectionsTotalCount")) {
         var3 = "getConnectionsTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionsTotalCount", JDBCConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectionsTotalCount", var2);
         var2.setValue("description", "<p>The total number of JDBC connections in this <code>JDBCConnectionPoolRuntimeMBean</code> since the pool has been instantiated.</p> ");
      }

      if (!var1.containsKey("CurrCapacity")) {
         var3 = "getCurrCapacity";
         var4 = null;
         var2 = new PropertyDescriptor("CurrCapacity", JDBCConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("CurrCapacity", var2);
         var2.setValue("description", "<p>The current capacity of this connection pool.</p> ");
      }

      if (!var1.containsKey("DeploymentState")) {
         var3 = "getDeploymentState";
         var4 = null;
         var2 = new PropertyDescriptor("DeploymentState", JDBCConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("DeploymentState", var2);
         var2.setValue("description", "<p>The current deployment state of the module.</p>  <p>A module can be in one and only one of the following states. State can be changed via deployment or administrator console.</p>  <p>- UNPREPARED. State indicating at this  module is neither  prepared or active.</p>  <p>- PREPARED. State indicating at this module of this application is prepared, but not active. The classes have been loaded and the module has been validated.</p>  <p>- ACTIVATED. State indicating at this module  is currently active.</p>  <p>- NEW. State indicating this module has just been created and is being initialized.</p> ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("#setDeploymentState(int)")};
         var2.setValue("see", var5);
      }

      if (!var1.containsKey("FailuresToReconnectCount")) {
         var3 = "getFailuresToReconnectCount";
         var4 = null;
         var2 = new PropertyDescriptor("FailuresToReconnectCount", JDBCConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("FailuresToReconnectCount", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("HighestNumAvailable")) {
         var3 = "getHighestNumAvailable";
         var4 = null;
         var2 = new PropertyDescriptor("HighestNumAvailable", JDBCConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("HighestNumAvailable", var2);
         var2.setValue("description", "<p>The highest number of available connections in this pool.</p> ");
      }

      if (!var1.containsKey("HighestNumUnavailable")) {
         var3 = "getHighestNumUnavailable";
         var4 = null;
         var2 = new PropertyDescriptor("HighestNumUnavailable", JDBCConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("HighestNumUnavailable", var2);
         var2.setValue("description", "<p>The highest number of unavailable connections in this pool.</p> ");
      }

      if (!var1.containsKey("LeakedConnectionCount")) {
         var3 = "getLeakedConnectionCount";
         var4 = null;
         var2 = new PropertyDescriptor("LeakedConnectionCount", JDBCConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("LeakedConnectionCount", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("MaxCapacity")) {
         var3 = "getMaxCapacity";
         var4 = null;
         var2 = new PropertyDescriptor("MaxCapacity", JDBCConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("MaxCapacity", var2);
         var2.setValue("description", "<p>The maximum capacity of this connection pool.</p> ");
      }

      if (!var1.containsKey("ModuleId")) {
         var3 = "getModuleId";
         var4 = null;
         var2 = new PropertyDescriptor("ModuleId", JDBCConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ModuleId", var2);
         var2.setValue("description", "<p>Returns the identifier for this Component.  The identifier is unique within the application.</p>  <p>Typical modules will use the URI for their id.  Web Modules will return their context-root since the web-uri may not be unique within an EAR. ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("NumAvailable")) {
         var3 = "getNumAvailable";
         var4 = null;
         var2 = new PropertyDescriptor("NumAvailable", JDBCConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("NumAvailable", var2);
         var2.setValue("description", "<p>The number of available connections in this pool.</p> ");
      }

      if (!var1.containsKey("NumUnavailable")) {
         var3 = "getNumUnavailable";
         var4 = null;
         var2 = new PropertyDescriptor("NumUnavailable", JDBCConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("NumUnavailable", var2);
         var2.setValue("description", "<p>The number of unavailable connections in this pool.</p> ");
      }

      if (!var1.containsKey("PoolState")) {
         var3 = "getPoolState";
         var4 = null;
         var2 = new PropertyDescriptor("PoolState", JDBCConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("PoolState", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("Properties")) {
         var3 = "getProperties";
         var4 = null;
         var2 = new PropertyDescriptor("Properties", JDBCConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("Properties", var2);
         var2.setValue("description", "<p>Returns the properties for a pool.</p>  <p>This is a privileged method, and can only be invoked on a client that has specified an authorized Principal.</p> ");
      }

      if (!var1.containsKey("State")) {
         var3 = "getState";
         var4 = null;
         var2 = new PropertyDescriptor("State", JDBCConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("State", var2);
         var2.setValue("description", "<p>The current state of the connection pool.</p>  <p>Possible values are:</p>  <ul> <li><code>Running</code> <p>if the pool is enabled (deployed and not <code>Suspended</code>). This is the normal state of the connection pool.</p> </li>  <li><code>Suspended</code> <p>if the pool disabled.</p> </li>  <li><code>Shutdown</code> <p>if the pool is shutdown and all database connections have been closed.</p> </li>  <li><code>Unknown</code> <p>if the pool state is unknown.</p> </li>  <li><code>Unhealthy</code> <p>if all connections are unavailable (not because they are in use). This state occurs if the database server is unavailable when the connection pool is created (creation retry must be enabled) or if all connections have failed connection tests (on creation, on reserve, on release, or periodic testing).</p> </li> </ul> ");
      }

      if (!var1.containsKey("StatementProfileCount")) {
         var3 = "getStatementProfileCount";
         var4 = null;
         var2 = new PropertyDescriptor("StatementProfileCount", JDBCConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("StatementProfileCount", var2);
         var2.setValue("description", "<p>SQL roundtrip profiling stores SQL statement text, execution time and other metrics.</p>  <p>This method should be used first before requesting SQL statement profiles from the profile storage.</p> ");
      }

      if (!var1.containsKey("VersionJDBCDriver")) {
         var3 = "getVersionJDBCDriver";
         var4 = null;
         var2 = new PropertyDescriptor("VersionJDBCDriver", JDBCConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("VersionJDBCDriver", var2);
         var2.setValue("description", "The class name of the JDBC driver used to create database connections. ");
      }

      if (!var1.containsKey("WaitSecondsHighCount")) {
         var3 = "getWaitSecondsHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("WaitSecondsHighCount", JDBCConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("WaitSecondsHighCount", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("WaitingForConnectionCurrentCount")) {
         var3 = "getWaitingForConnectionCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("WaitingForConnectionCurrentCount", JDBCConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("WaitingForConnectionCurrentCount", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("WaitingForConnectionHighCount")) {
         var3 = "getWaitingForConnectionHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("WaitingForConnectionHighCount", JDBCConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("WaitingForConnectionHighCount", var2);
         var2.setValue("description", " ");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("WorkManagerRuntimes")) {
         var3 = "getWorkManagerRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("WorkManagerRuntimes", JDBCConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("WorkManagerRuntimes", var2);
         var2.setValue("description", "<p>Get the runtime mbeans for all work managers defined in this component</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("Enabled")) {
         var3 = "isEnabled";
         var4 = null;
         var2 = new PropertyDescriptor("Enabled", JDBCConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("Enabled", var2);
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
      Method var3 = JDBCConnectionPoolRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = JDBCConnectionPoolRuntimeMBean.class.getMethod("getConnectionLeakProfiles", Integer.TYPE, Integer.TYPE);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      String[] var5;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Connection leak profiling stores stack trace at the time when connection was created in case of connection leak. This enables you to identify where the leak occurred.</p>  <p>Connection leak is a situation when connection from the pool was not closed explicitly by calling close() and was garbage collected.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.runtime.JDBCConnectionLeakProfile")};
         var2.setValue("see", var5);
         var2.setValue("role", "operation");
      }

      var3 = JDBCConnectionPoolRuntimeMBean.class.getMethod("getStatementProfiles", Integer.TYPE, Integer.TYPE);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>SQL roundtrip profiling stores SQL statement text, execution time and other metrics.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.runtime.JDBCStatementProfile")};
         var2.setValue("see", var5);
         var2.setValue("role", "operation");
      }

      var3 = JDBCConnectionPoolRuntimeMBean.class.getMethod("testPool");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Test the pool by reserving and releasing a connection from it. If the pool configuration attribute TestConnectionsOnReserve or TestConnectionsOnRelease is enabled, the acquired connection is also tested as part of the reserve and release operation.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.JDBCConnectionPoolMBean")};
         var2.setValue("see", var5);
         var2.setValue("role", "operation");
         var2.setValue("rolePermitAll", Boolean.TRUE);
      }

      var3 = JDBCConnectionPoolRuntimeMBean.class.getMethod("resetConnectionLeakProfile");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Resets connection leak profile.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JDBCConnectionPoolRuntimeMBean.class.getMethod("resetStatementProfile");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Resets SQL statement profile.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JDBCConnectionPoolRuntimeMBean.class.getMethod("shrink");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Shrinks the named database ConnectionPool to the current number of reserved connections or the initial connection pool size, which ever is greater.</p>  <p>This is a privileged method, and can only be invoked on a client that has specified an authorized Principal.</p> ");
         var2.setValue("role", "operation");
         var2.setValue("rolePermitAll", Boolean.TRUE);
      }

      var3 = JDBCConnectionPoolRuntimeMBean.class.getMethod("reset");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Resets the connection pool by shutting down and re-establishing all available connecton pool connections. This method should be used when the connection pool is in the <code>Unhealthy</code> state and needs to be reinitialized.</p>  <p>This is a privileged method, and can only be invoked on a client that has specified an authorized Principal.</p> ");
         var2.setValue("role", "operation");
         var2.setValue("rolePermitAll", Boolean.TRUE);
      }

      var3 = JDBCConnectionPoolRuntimeMBean.class.getMethod("suspend");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Disables the connection pool, suspending all operations on pool connections until the pool is re-enabled.</p>  <p>This is a privileged method, and can only be invoked on a client that has specified an authorized Principal.</p> ");
         var2.setValue("role", "operation");
         var2.setValue("rolePermitAll", Boolean.TRUE);
      }

      var3 = JDBCConnectionPoolRuntimeMBean.class.getMethod("forceSuspend");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Forcibly disables the pool, suspending all operations on pool connections until the pool is re-enabled. All current users of the pool are forcibly disconnected. All connections currently in use are closed and recreated.</p>  <p>This is a privileged method, and can only be invoked on a client that has specified an authorized Principal.</p> ");
         var2.setValue("role", "operation");
         var2.setValue("rolePermitAll", Boolean.TRUE);
      }

      var3 = JDBCConnectionPoolRuntimeMBean.class.getMethod("shutdown");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Shuts down the pool. If any connections from the pool are currently in use, the operation will fail.</p>  <p>This is a privileged method, and can only be invoked on a client that has specified an authorized Principal.</p> ");
         var2.setValue("role", "operation");
         var2.setValue("rolePermitAll", Boolean.TRUE);
      }

      var3 = JDBCConnectionPoolRuntimeMBean.class.getMethod("forceShutdown");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Forcibly shuts down the connection pool. All current users of the pool are forcibly disconnected.</p>  <p>This is a privileged method, and can only be invoked on a client that has specified an authorized Principal.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JDBCConnectionPoolRuntimeMBean.class.getMethod("resume");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Restores all access to and operations on a connection pool that has been suspended (marked as disabled).</p>  <p>This is a privileged method, and can only be invoked on a client that has specified an authorized Principal.</p> ");
         var2.setValue("role", "operation");
         var2.setValue("rolePermitAll", Boolean.TRUE);
      }

      var3 = JDBCConnectionPoolRuntimeMBean.class.getMethod("disableDroppingUsers");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Disable the pool, immediately disconnecting all users.</p>  <p>This is a privileged method, and can only be invoked on a client that has specified an authorized Principal.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.runtime.JDBCConnectionPoolRuntimeMBean#forceSuspend")};
         var2.setValue("see", var5);
         var2.setValue("role", "operation");
         var2.setValue("rolePermitAll", Boolean.TRUE);
      }

      var3 = JDBCConnectionPoolRuntimeMBean.class.getMethod("disableFreezingUsers");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Disable the pool, suspending all operations on pool connections until the pool is re-enabled.</p>  <p>This is a privileged method, and can only be invoked on a client that has specified an authorized Principal.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.runtime.JDBCConnectionPoolRuntimeMBean#suspend")};
         var2.setValue("see", var5);
         var2.setValue("role", "operation");
      }

      var3 = JDBCConnectionPoolRuntimeMBean.class.getMethod("enable");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Restore all access to and operations on the pool.</p>  <p>This is a privileged method, and can only be invoked on a client that has specified an authorized Principal.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.runtime.JDBCConnectionPoolRuntimeMBean#resume")};
         var2.setValue("see", var5);
         var2.setValue("role", "operation");
         var2.setValue("rolePermitAll", Boolean.TRUE);
      }

      var3 = JDBCConnectionPoolRuntimeMBean.class.getMethod("poolExists", String.class);
      ParameterDescriptor[] var7 = new ParameterDescriptor[]{createParameterDescriptor("name", "Name of the pool being looked for ")};
      String var6 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var6)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var6, var2);
         var2.setValue("description", "<p>Indicates whether the specified pool exists.</p>  <p>This is a privileged method, and can only be invoked on a client that has specified an authorized Principal.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JDBCConnectionPoolRuntimeMBean.class.getMethod("clearStatementCache");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Clears the cache of Prepared and Callable Statements maintained for each connection in the connection pool.</p> ");
         var2.setValue("role", "operation");
         var2.setValue("rolePermitAll", Boolean.TRUE);
      }

      var3 = JDBCConnectionPoolRuntimeMBean.class.getMethod("dumpPool");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Prints out the data structure of the connection pool in the following lists: <ul> <li>alvList - lists details about database connections in the connection pool that are not currently in use by a client.</li> <li>unavlList - lists details about database connections that WebLogic Server failed to create on server startup or failed to recreate after a failed database connection test.</li> <li>resvList - lists details about database connections in teh connection pool that are currently in use by a client.</li> </ul> </p> ");
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
