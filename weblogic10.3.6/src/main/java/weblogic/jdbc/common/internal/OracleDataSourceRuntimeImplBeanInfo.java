package weblogic.jdbc.common.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.JDBCOracleDataSourceRuntimeMBean;

public class OracleDataSourceRuntimeImplBeanInfo extends DataSourceRuntimeMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JDBCOracleDataSourceRuntimeMBean.class;

   public OracleDataSourceRuntimeImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public OracleDataSourceRuntimeImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = OracleDataSourceRuntimeImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "10.3.4.0");
      var2.setValue("package", "weblogic.jdbc.common.internal");
      String var3 = (new String("Runtime MBean that represents a JDBC RAC JDBC data source. ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.JDBCOracleDataSourceRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("ActiveConnectionsAverageCount")) {
         var3 = "getActiveConnectionsAverageCount";
         var4 = null;
         var2 = new PropertyDescriptor("ActiveConnectionsAverageCount", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("ActiveConnectionsAverageCount", var2);
         var2.setValue("description", "<p>Average number of active connections in this instance of the data source.</p>  <p>Active connections are connections in use by an application.  This value is only valid if the resource is configured to allow shrinking.</p> ");
      }

      if (!var1.containsKey("ActiveConnectionsCurrentCount")) {
         var3 = "getActiveConnectionsCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("ActiveConnectionsCurrentCount", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("ActiveConnectionsCurrentCount", var2);
         var2.setValue("description", "<p>The number of connections currently in use by applications.</p> ");
      }

      if (!var1.containsKey("ActiveConnectionsHighCount")) {
         var3 = "getActiveConnectionsHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("ActiveConnectionsHighCount", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("ActiveConnectionsHighCount", var2);
         var2.setValue("description", "<p>Highest number of active database connections in this instance of the data source since the data source was instantiated. </p>  <p>Active connections are connections in use by an application.</p> ");
      }

      if (!var1.containsKey("ConnectionDelayTime")) {
         var3 = "getConnectionDelayTime";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionDelayTime", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("ConnectionDelayTime", var2);
         var2.setValue("description", "<p>The average amount of time, in milliseconds, that it takes to create a physical connection to the database.</p>  <p>The value is calculated as summary of all times to connect divided by the total number of connections.</p> ");
      }

      if (!var1.containsKey("ConnectionsTotalCount")) {
         var3 = "getConnectionsTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionsTotalCount", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("ConnectionsTotalCount", var2);
         var2.setValue("description", "<p>The cumulative total number of database connections created in this data source since the data source was deployed.</p> ");
      }

      if (!var1.containsKey("CurrCapacity")) {
         var3 = "getCurrCapacity";
         var4 = null;
         var2 = new PropertyDescriptor("CurrCapacity", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("CurrCapacity", var2);
         var2.setValue("description", "<p>The current count of JDBC connections in the connection pool in the data source.</p> ");
      }

      if (!var1.containsKey("CurrCapacityHighCount")) {
         var3 = "getCurrCapacityHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("CurrCapacityHighCount", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("CurrCapacityHighCount", var2);
         var2.setValue("description", "<p>Highest number of database connections available or in use (current capacity) in this instance of the data source since the data source was deployed.</p> ");
      }

      if (!var1.containsKey("DatabaseProductName")) {
         var3 = "getDatabaseProductName";
         var4 = null;
         var2 = new PropertyDescriptor("DatabaseProductName", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("DatabaseProductName", var2);
         var2.setValue("description", "<p> The product name of the database that this data source is connected to. </p> ");
      }

      if (!var1.containsKey("DatabaseProductVersion")) {
         var3 = "getDatabaseProductVersion";
         var4 = null;
         var2 = new PropertyDescriptor("DatabaseProductVersion", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("DatabaseProductVersion", var2);
         var2.setValue("description", "<p> The product version of the database that this data source is connected to. </p> ");
      }

      String[] var5;
      if (!var1.containsKey("DeploymentState")) {
         var3 = "getDeploymentState";
         var4 = null;
         var2 = new PropertyDescriptor("DeploymentState", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("DeploymentState", var2);
         var2.setValue("description", "<p>The current deployment state of the module.</p>  <p>A module can be in one and only one of the following states. State can be changed via deployment or administrator console.</p>  <p>- UNPREPARED. State indicating at this  module is neither  prepared or active.</p>  <p>- PREPARED. State indicating at this module of this application is prepared, but not active. The classes have been loaded and the module has been validated.</p>  <p>- ACTIVATED. State indicating at this module  is currently active.</p>  <p>- NEW. State indicating this module has just been created and is being initialized.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#setDeploymentState(int)")};
         var2.setValue("see", var5);
      }

      if (!var1.containsKey("DriverName")) {
         var3 = "getDriverName";
         var4 = null;
         var2 = new PropertyDescriptor("DriverName", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("DriverName", var2);
         var2.setValue("description", "<p> The product name of the JDBC driver that this data source is configured to use. </p> ");
      }

      if (!var1.containsKey("DriverVersion")) {
         var3 = "getDriverVersion";
         var4 = null;
         var2 = new PropertyDescriptor("DriverVersion", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("DriverVersion", var2);
         var2.setValue("description", "<p> The version of the JDBC driver that this data source is configured to use. </p> ");
      }

      if (!var1.containsKey("FailedAffinityBasedBorrowCount")) {
         var3 = "getFailedAffinityBasedBorrowCount";
         var4 = null;
         var2 = new PropertyDescriptor("FailedAffinityBasedBorrowCount", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("FailedAffinityBasedBorrowCount", var2);
         var2.setValue("description", "The number of reserve requests for which an existing connection for the affinity policy was not found. ");
      }

      if (!var1.containsKey("FailedRCLBBasedBorrowCount")) {
         var3 = "getFailedRCLBBasedBorrowCount";
         var4 = null;
         var2 = new PropertyDescriptor("FailedRCLBBasedBorrowCount", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("FailedRCLBBasedBorrowCount", var2);
         var2.setValue("description", "The number of reserve requests for which a connection was not found based on the runtime connection load balancing policy. ");
      }

      if (!var1.containsKey("FailedReserveRequestCount")) {
         var3 = "getFailedReserveRequestCount";
         var4 = null;
         var2 = new PropertyDescriptor("FailedReserveRequestCount", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("FailedReserveRequestCount", var2);
         var2.setValue("description", "<p>The cumulative, running count of requests for a connection from this data source that could not be fulfilled.</p> ");
      }

      if (!var1.containsKey("FailuresToReconnectCount")) {
         var3 = "getFailuresToReconnectCount";
         var4 = null;
         var2 = new PropertyDescriptor("FailuresToReconnectCount", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("FailuresToReconnectCount", var2);
         var2.setValue("description", "<p>The number of times that the data source attempted to refresh a database connection and failed.</p>  <p>Failures may occur when the database is unavailable or when the network connection to the database is interrupted.</p> ");
      }

      if (!var1.containsKey("HighestNumAvailable")) {
         var3 = "getHighestNumAvailable";
         var4 = null;
         var2 = new PropertyDescriptor("HighestNumAvailable", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("HighestNumAvailable", var2);
         var2.setValue("description", "<p>Highest number of database connections that were idle and available to be used by an application at any time in this instance of the data source since the data source was deployed.</p> ");
      }

      if (!var1.containsKey("HighestNumUnavailable")) {
         var3 = "getHighestNumUnavailable";
         var4 = null;
         var2 = new PropertyDescriptor("HighestNumUnavailable", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("HighestNumUnavailable", var2);
         var2.setValue("description", "<p>Highest number of database connections that were in use by applications or being tested by the system in this instance of the data source since the data source was deployed.</p> ");
      }

      if (!var1.containsKey("Instances")) {
         var3 = "getInstances";
         var4 = null;
         var2 = new PropertyDescriptor("Instances", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("Instances", var2);
         var2.setValue("description", "The set of data source RAC instance runtime MBeans that are associated with this data source. ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("JDBCDriverRuntime")) {
         var3 = "getJDBCDriverRuntime";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJDBCDriverRuntime";
         }

         var2 = new PropertyDescriptor("JDBCDriverRuntime", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("JDBCDriverRuntime", var2);
         var2.setValue("description", "<p>Gets the JDBCDriverRuntimeMBean associated with this data source.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("LastTask")) {
         var3 = "getLastTask";
         var4 = null;
         var2 = new PropertyDescriptor("LastTask", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("LastTask", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("LeakedConnectionCount")) {
         var3 = "getLeakedConnectionCount";
         var4 = null;
         var2 = new PropertyDescriptor("LeakedConnectionCount", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("LeakedConnectionCount", var2);
         var2.setValue("description", "<p>The number of leaked connections. A leaked connection is a connection that was reserved from the data source but was not returned to the data source by calling <code>close()</code>.</p> ");
      }

      if (!var1.containsKey("ModuleId")) {
         var3 = "getModuleId";
         var4 = null;
         var2 = new PropertyDescriptor("ModuleId", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("ModuleId", var2);
         var2.setValue("description", "<p>Returns the identifier for this Component.  The identifier is unique within the application.</p>  <p>Typical modules will use the URI for their id.  Web Modules will return their context-root since the web-uri may not be unique within an EAR. ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("NumAvailable")) {
         var3 = "getNumAvailable";
         var4 = null;
         var2 = new PropertyDescriptor("NumAvailable", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("NumAvailable", var2);
         var2.setValue("description", "<p>The number of database connections that are currently idle and  available to be used by applications in this instance of the data source.</p> ");
      }

      if (!var1.containsKey("NumUnavailable")) {
         var3 = "getNumUnavailable";
         var4 = null;
         var2 = new PropertyDescriptor("NumUnavailable", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("NumUnavailable", var2);
         var2.setValue("description", "<p>The number of connections currently in use by applications or being tested in this instance of the data source.</p> ");
      }

      if (!var1.containsKey("ONSClientRuntime")) {
         var3 = "getONSClientRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("ONSClientRuntime", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("ONSClientRuntime", var2);
         var2.setValue("description", "The ONS client runtime MBean associated with this data source. ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("PrepStmtCacheAccessCount")) {
         var3 = "getPrepStmtCacheAccessCount";
         var4 = null;
         var2 = new PropertyDescriptor("PrepStmtCacheAccessCount", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("PrepStmtCacheAccessCount", var2);
         var2.setValue("description", "<p>The cumulative, running count of the number of times that the statement cache was accessed.</p> ");
      }

      if (!var1.containsKey("PrepStmtCacheAddCount")) {
         var3 = "getPrepStmtCacheAddCount";
         var4 = null;
         var2 = new PropertyDescriptor("PrepStmtCacheAddCount", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("PrepStmtCacheAddCount", var2);
         var2.setValue("description", "<p>The cumulative, running count of the number of statements added to the statement cache.</p>  <p>Each connection in the connection pool has its own cache of statements. This number is the sum of the number of statements added to the caches for all connections in the connection pool.</p> ");
      }

      if (!var1.containsKey("PrepStmtCacheCurrentSize")) {
         var3 = "getPrepStmtCacheCurrentSize";
         var4 = null;
         var2 = new PropertyDescriptor("PrepStmtCacheCurrentSize", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("PrepStmtCacheCurrentSize", var2);
         var2.setValue("description", "<p>The number of prepared and callable statements currently cached in the statement cache.</p>  <p>Each connection in the connection pool has its own cache of statements. This number is the sum of the number of statements in the caches for all connections in the connection pool.</p> ");
      }

      if (!var1.containsKey("PrepStmtCacheDeleteCount")) {
         var3 = "getPrepStmtCacheDeleteCount";
         var4 = null;
         var2 = new PropertyDescriptor("PrepStmtCacheDeleteCount", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("PrepStmtCacheDeleteCount", var2);
         var2.setValue("description", "<p>The cumulative, running count of statements discarded from the cache.</p>  <p>Each connection in the connection pool has its own cache of statements. This number is the sum of the number of statements that were discarded from the caches for all connections in the connection pool.</p> ");
      }

      if (!var1.containsKey("PrepStmtCacheHitCount")) {
         var3 = "getPrepStmtCacheHitCount";
         var4 = null;
         var2 = new PropertyDescriptor("PrepStmtCacheHitCount", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("PrepStmtCacheHitCount", var2);
         var2.setValue("description", "<p>The cumulative, running count of the number of times that statements from the cache were used.</p> ");
      }

      if (!var1.containsKey("PrepStmtCacheMissCount")) {
         var3 = "getPrepStmtCacheMissCount";
         var4 = null;
         var2 = new PropertyDescriptor("PrepStmtCacheMissCount", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("PrepStmtCacheMissCount", var2);
         var2.setValue("description", "<p>The number of times that a statement request could not be satisfied with a statement from the cache.</p> ");
      }

      if (!var1.containsKey("Properties")) {
         var3 = "getProperties";
         var4 = null;
         var2 = new PropertyDescriptor("Properties", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("Properties", var2);
         var2.setValue("description", "<p>The list of properties for a data source that are passed to the JDBC driver when creating database connections.</p>  <p>This is a privileged operation that can only be invoked by an authorized user.</p> ");
      }

      if (!var1.containsKey("ReserveRequestCount")) {
         var3 = "getReserveRequestCount";
         var4 = null;
         var2 = new PropertyDescriptor("ReserveRequestCount", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("ReserveRequestCount", var2);
         var2.setValue("description", "<p>The cumulative, running count of requests for a connection from this data source.</p> ");
      }

      if (!var1.containsKey("ServiceName")) {
         var3 = "getServiceName";
         var4 = null;
         var2 = new PropertyDescriptor("ServiceName", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("ServiceName", var2);
         var2.setValue("description", "The database service name for this RAC data source. ");
      }

      if (!var1.containsKey("State")) {
         var3 = "getState";
         var4 = null;
         var2 = new PropertyDescriptor("State", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("State", var2);
         var2.setValue("description", "<p>The current state of the data source.</p> <p>Possible states are:</p> <ul><li><code>Running</code> - the data source is enabled (deployed and not <code>Suspended</code>). This is the normal state of the data source. This state includes conditions when the database server is not available and the data source is created (creation retry must be enabled) or when all connections have failed connection tests (on creation, on reserve, or periodic testing).</li> <li><code>Suspended</code> - the data source has been disabled.</li> <li><code>Shutdown</code> - the data source is shutdown and all database connections have been closed.</li> <li><code>Overloaded</code> - all resources in pool are in use.</li> <li><code>Unknown</code> - the data source state is unknown.</li></ul> ");
      }

      if (!var1.containsKey("SuccessfulAffinityBasedBorrowCount")) {
         var3 = "getSuccessfulAffinityBasedBorrowCount";
         var4 = null;
         var2 = new PropertyDescriptor("SuccessfulAffinityBasedBorrowCount", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("SuccessfulAffinityBasedBorrowCount", var2);
         var2.setValue("description", "The number of reserve requests for which an existing connection was found that satisfied the affinity policy. ");
      }

      if (!var1.containsKey("SuccessfulRCLBBasedBorrowCount")) {
         var3 = "getSuccessfulRCLBBasedBorrowCount";
         var4 = null;
         var2 = new PropertyDescriptor("SuccessfulRCLBBasedBorrowCount", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("SuccessfulRCLBBasedBorrowCount", var2);
         var2.setValue("description", "The number of reserve requests for which existing connections were found that satisfied the runtime connection load balancing policy. ");
      }

      if (!var1.containsKey("VersionJDBCDriver")) {
         var3 = "getVersionJDBCDriver";
         var4 = null;
         var2 = new PropertyDescriptor("VersionJDBCDriver", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("VersionJDBCDriver", var2);
         var2.setValue("description", "<p>The driver class name of the JDBC driver used to create database connections. </p> ");
      }

      if (!var1.containsKey("WaitSecondsHighCount")) {
         var3 = "getWaitSecondsHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("WaitSecondsHighCount", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("WaitSecondsHighCount", var2);
         var2.setValue("description", "<p>The highest number of seconds that an application waited for a connection (the longest connection reserve wait time) from this instance of the connection pool since the connection pool was instantiated.</p> <p>This value is updated when a completed <code>getConnection</code> request takes longer to return a connection than any previous request.</p> ");
      }

      if (!var1.containsKey("WaitingForConnectionCurrentCount")) {
         var3 = "getWaitingForConnectionCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("WaitingForConnectionCurrentCount", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("WaitingForConnectionCurrentCount", var2);
         var2.setValue("description", "<p>The number of connection requests waiting for a database connection.</p> ");
      }

      if (!var1.containsKey("WaitingForConnectionFailureTotal")) {
         var3 = "getWaitingForConnectionFailureTotal";
         var4 = null;
         var2 = new PropertyDescriptor("WaitingForConnectionFailureTotal", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("WaitingForConnectionFailureTotal", var2);
         var2.setValue("description", "<p>The cumulative, running count of requests for a connection from this data source that had to wait before getting a connection and eventually failed to get a connection.</p>  <p>Waiting connection requests can fail for a variety of reasons, including waiting for longer than the ConnectionReserveTimeoutSeconds.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.j2ee.descriptor.wl.ConnectionPoolParamsBean.ConnectionReserveTimeoutSeconds()")};
         var2.setValue("see", var5);
      }

      if (!var1.containsKey("WaitingForConnectionHighCount")) {
         var3 = "getWaitingForConnectionHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("WaitingForConnectionHighCount", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("WaitingForConnectionHighCount", var2);
         var2.setValue("description", "<p>Highest number of application requests concurrently waiting for a connection from this instance of the data source.</p> ");
      }

      if (!var1.containsKey("WaitingForConnectionSuccessTotal")) {
         var3 = "getWaitingForConnectionSuccessTotal";
         var4 = null;
         var2 = new PropertyDescriptor("WaitingForConnectionSuccessTotal", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("WaitingForConnectionSuccessTotal", var2);
         var2.setValue("description", "<p>The cumulative, running count of requests for a connection from this data source that had to wait before getting a connection and eventually succeeded in getting a connection.</p> ");
      }

      if (!var1.containsKey("WaitingForConnectionTotal")) {
         var3 = "getWaitingForConnectionTotal";
         var4 = null;
         var2 = new PropertyDescriptor("WaitingForConnectionTotal", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("WaitingForConnectionTotal", var2);
         var2.setValue("description", "<p>The cumulative, running count of requests for a connection from this data source that had to wait before getting a connection, including those that eventually got a connection and those that did not get a connection.</p> ");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("WorkManagerRuntimes")) {
         var3 = "getWorkManagerRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("WorkManagerRuntimes", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("WorkManagerRuntimes", var2);
         var2.setValue("description", "<p>Get the runtime mbeans for all work managers defined in this component</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("Enabled")) {
         var3 = "isEnabled";
         var4 = null;
         var2 = new PropertyDescriptor("Enabled", JDBCOracleDataSourceRuntimeMBean.class, var3, var4);
         var1.put("Enabled", var2);
         var2.setValue("description", "Indicates whether the data source is enabled or disabled: <ul> <li><code>true</code> if the data source is enabled.</li> <li><code>false</code> if the data source is disabled.</li> </ul> ");
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
      Method var3 = JDBCOracleDataSourceRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = JDBCOracleDataSourceRuntimeMBean.class.getMethod("testPool");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Tests the connection pool in the data source by reserving and releasing a connection from it.</p>  <p>If the pool configuration attribute TestConnectionsOnReserve is enabled, the acquired connection is also tested as part of the reserve operation.</p> ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.JDBCConnectionPoolMBean")};
         var2.setValue("see", var5);
         var2.setValue("role", "operation");
         var2.setValue("rolePermitAll", Boolean.TRUE);
      }

      var3 = JDBCOracleDataSourceRuntimeMBean.class.getMethod("shrink");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Shrinks the database connection pool in the data source to either the current number of reserved connections or the initial size of the connection pool, which ever is greater.</p>  <p>This is a privileged operation that can only be invoked by an authorized user.</p> ");
         var2.setValue("role", "operation");
         var2.setValue("rolePermitAll", Boolean.TRUE);
      }

      var3 = JDBCOracleDataSourceRuntimeMBean.class.getMethod("reset");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Resets the connection pool in the data source by shutting down and recreating all available database connections in the pool.</p>  <p>Use when a data source is in the health state of <code>Unhealthy</code> and needs to be reinitialized.</p>  <p>This is a privileged operation that can only be invoked by an authorized user.</p> ");
         var2.setValue("role", "operation");
         var2.setValue("rolePermitAll", Boolean.TRUE);
      }

      var3 = JDBCOracleDataSourceRuntimeMBean.class.getMethod("suspend");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Suspends a data source has the health state of <code>Running</code> and disables existing connections. If any connections from the data source are currently in use, the operation fails and the health state remains <code>Running</code>.</p>  <p>If successful, the health state is set to <code>Suspended</code>.  <p>This is a privileged operation that can only be invoked by an authorized user.</p> ");
         var2.setValue("role", "operation");
         var2.setValue("rolePermitAll", Boolean.TRUE);
      }

      var3 = JDBCOracleDataSourceRuntimeMBean.class.getMethod("forceSuspend");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Suspends a data source that has the health state of <code>Running</code>, including disconnecting all current connection users. All current connections are closed and recreated.</p>  <p>If successful, the health state is set to <code>Suspended</code>.</p>  <p>This is a privileged operation that can only be invoked by an authorized user.</p> ");
         var2.setValue("role", "operation");
         var2.setValue("rolePermitAll", Boolean.TRUE);
      }

      var3 = JDBCOracleDataSourceRuntimeMBean.class.getMethod("shutdown");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Shuts down a data source that has a health state of <code>Running</code>. If any connections from the data source are currently in use, the operation fails and the health state remains <code>Running</code>.</p>  <p>If successful, the health state is set to <code>Shutdown</code>.</p>  <p>This is a privileged operation that can only be invoked by an authorized user.</p> ");
         var2.setValue("role", "operation");
         var2.setValue("rolePermitAll", Boolean.TRUE);
      }

      var3 = JDBCOracleDataSourceRuntimeMBean.class.getMethod("forceShutdown");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Shuts down a data source that has a health state of <code>Running</code>, including forcing the disconnection of all current connection users.</p>  <p>If successful, the health state is set to <code>Shutdown</code>.</p>  <p>This is a privileged operation that can only be invoked by an authorized user.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JDBCOracleDataSourceRuntimeMBean.class.getMethod("resume");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Restores all access to and operations on a data source that has a health state of <code>Suspended</code>.</p>  <p>If successful, the health state is set to <code>Running</code>.  <p>This is a privileged operation that can only be invoked by an authorized user. </p> ");
         var2.setValue("role", "operation");
         var2.setValue("rolePermitAll", Boolean.TRUE);
      }

      var3 = JDBCOracleDataSourceRuntimeMBean.class.getMethod("start");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Starts a data source that has a health state of <code>Shutdown</code>.</p>  <p>If successful, the health state is set to <code>Running</code>.</p>  <p>This is a privileged operation that can only be invoked by an authorized user.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JDBCOracleDataSourceRuntimeMBean.class.getMethod("poolExists", String.class);
      ParameterDescriptor[] var8 = new ParameterDescriptor[]{createParameterDescriptor("name", "Name of the pool being looked for ")};
      String var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, var8);
         var1.put(var7, var2);
         var2.setValue("description", "<p>Specifies whether a data source with the given name exists.</p>  <p>This is a privileged operation that can only be invoked by an authorized user.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JDBCOracleDataSourceRuntimeMBean.class.getMethod("clearStatementCache");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>For each connection in the connection pool, clears the statement cache of Prepared and Callable Statements.</p> ");
         var2.setValue("role", "operation");
         var2.setValue("rolePermitAll", Boolean.TRUE);
      }

      var3 = JDBCOracleDataSourceRuntimeMBean.class.getMethod("dumpPool");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Prints out information about all the connections in the connection pool in the data source.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JDBCOracleDataSourceRuntimeMBean.class.getMethod("dumpPoolProfile");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Prints out profile information about the data source.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JDBCOracleDataSourceRuntimeMBean.class.getMethod("isOperationAllowed", String.class);
      var8 = new ParameterDescriptor[]{createParameterDescriptor("operation", "The name of the operation to be performed. Valid values include: Start, Shutdown, Suspend, Resume, Reset, Shrink, Clear ")};
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, var8);
         String[] var6 = new String[]{BeanInfoHelper.encodeEntities("IllegalArgumentException Thrown when operation parameter is not recognized as a supported operation.")};
         var2.setValue("throws", var6);
         var1.put(var7, var2);
         var2.setValue("description", "<p>Indicates whether the specified operation is valid based on the state of the underlying DataSource.</p> ");
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
