package weblogic.connector.monitoring.outbound;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.logging.LogRuntimeBeanInfo;
import weblogic.management.runtime.ConnectorConnectionPoolRuntimeMBean;

public class ConnectionPoolRuntimeMBeanImplBeanInfo extends LogRuntimeBeanInfo {
   public static Class INTERFACE_CLASS = ConnectorConnectionPoolRuntimeMBean.class;

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
      var2.setValue("since", "6.1.0.0");
      var2.setValue("package", "weblogic.connector.monitoring.outbound");
      String var3 = (new String("<p>This class is used for monitoring a WebLogic Connector Connection Pool</p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.ConnectorConnectionPoolRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("ActiveConnectionsCurrentCount")) {
         var3 = "getActiveConnectionsCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("ActiveConnectionsCurrentCount", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ActiveConnectionsCurrentCount", var2);
         var2.setValue("description", "<p>The current total active connections.</p> ");
      }

      if (!var1.containsKey("ActiveConnectionsHighCount")) {
         var3 = "getActiveConnectionsHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("ActiveConnectionsHighCount", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ActiveConnectionsHighCount", var2);
         var2.setValue("description", "<p>The high water mark of active connections in this Connector Pool since the pool was instantiated.</p> ");
      }

      if (!var1.containsKey("AverageActiveUsage")) {
         var3 = "getAverageActiveUsage";
         var4 = null;
         var2 = new PropertyDescriptor("AverageActiveUsage", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("AverageActiveUsage", var2);
         var2.setValue("description", "<p>The running average usage of created connections that are active in the Connector Pool since the pool was last shrunk.</p> ");
      }

      if (!var1.containsKey("CapacityIncrement")) {
         var3 = "getCapacityIncrement";
         var4 = null;
         var2 = new PropertyDescriptor("CapacityIncrement", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("CapacityIncrement", var2);
         var2.setValue("description", "<p>The initial capacity configured for this Connector connection pool.</p> ");
      }

      if (!var1.containsKey("CloseCount")) {
         var3 = "getCloseCount";
         var4 = null;
         var2 = new PropertyDescriptor("CloseCount", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("CloseCount", var2);
         var2.setValue("description", "<p>The number of connections that were closed for the connection pool.</p> ");
      }

      if (!var1.containsKey("ConnectionFactoryClassName")) {
         var3 = "getConnectionFactoryClassName";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionFactoryClassName", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectionFactoryClassName", var2);
         var2.setValue("description", "<p>The ConnectionFactoryName of this Connector connection pool.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("ConnectionFactoryName")) {
         var3 = "getConnectionFactoryName";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionFactoryName", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectionFactoryName", var2);
         var2.setValue("description", "<p>For 1.0 link-ref resource adapters only, the base resource adapter's connection factory name.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("ConnectionIdleProfileCount")) {
         var3 = "getConnectionIdleProfileCount";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionIdleProfileCount", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectionIdleProfileCount", var2);
         var2.setValue("description", "<p>The number of Idle connection profiles stored for this pool.</p> ");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("ConnectionIdleProfiles")) {
         var3 = "getConnectionIdleProfiles";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionIdleProfiles", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectionIdleProfiles", var2);
         var2.setValue("description", "<p>An array of count LeakProfiles starting at the passed index, in the entire array of Idle profiles.</p> ");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("ConnectionLeakProfileCount")) {
         var3 = "getConnectionLeakProfileCount";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionLeakProfileCount", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectionLeakProfileCount", var2);
         var2.setValue("description", "<p>The number of Leak connection profiles stored for this pool.</p> ");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("ConnectionLeakProfiles")) {
         var3 = "getConnectionLeakProfiles";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionLeakProfiles", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectionLeakProfiles", var2);
         var2.setValue("description", "<p>An array of count LeakProfiles</p> ");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("ConnectionProfilingEnabled")) {
         var3 = "getConnectionProfilingEnabled";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionProfilingEnabled", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectionProfilingEnabled", var2);
         var2.setValue("description", "<p>Indicates whether connection profiling is enabled for this pool.</p> ");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("Connections")) {
         var3 = "getConnections";
         var4 = null;
         var2 = new PropertyDescriptor("Connections", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("Connections", var2);
         var2.setValue("description", "<p>An array of <code>ConnectorConnectionRuntimeMBeans</code> that each represents the statistics for a Connector Connection.</p> ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.runtime.ConnectorConnectionRuntimeMBean")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("ConnectionsCreatedTotalCount")) {
         var3 = "getConnectionsCreatedTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionsCreatedTotalCount", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectionsCreatedTotalCount", var2);
         var2.setValue("description", "<p>The total number of Connector connections created in this Connector Pool since the pool is instantiated.</p> ");
      }

      if (!var1.containsKey("ConnectionsDestroyedByErrorTotalCount")) {
         var3 = "getConnectionsDestroyedByErrorTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionsDestroyedByErrorTotalCount", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectionsDestroyedByErrorTotalCount", var2);
         var2.setValue("description", "<p>Return the number of connections that were destroyed because an error event was received.</p> ");
      }

      if (!var1.containsKey("ConnectionsDestroyedByShrinkingTotalCount")) {
         var3 = "getConnectionsDestroyedByShrinkingTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionsDestroyedByShrinkingTotalCount", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectionsDestroyedByShrinkingTotalCount", var2);
         var2.setValue("description", "<p>Return the number of connections that were destroyed as a result of shrinking.</p> ");
      }

      if (!var1.containsKey("ConnectionsDestroyedTotalCount")) {
         var3 = "getConnectionsDestroyedTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionsDestroyedTotalCount", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectionsDestroyedTotalCount", var2);
         var2.setValue("description", "<p>The total number of Connector connections destroyed in this Connector Pool since the pool is instantiated.</p> ");
      }

      if (!var1.containsKey("ConnectionsMatchedTotalCount")) {
         var3 = "getConnectionsMatchedTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionsMatchedTotalCount", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectionsMatchedTotalCount", var2);
         var2.setValue("description", "<p>The total number of times a request for a Connector connections was satisfied via the use of an existing created connection since the pool is instantiated.</p> ");
      }

      if (!var1.containsKey("ConnectionsRejectedTotalCount")) {
         var3 = "getConnectionsRejectedTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionsRejectedTotalCount", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectionsRejectedTotalCount", var2);
         var2.setValue("description", "<p>The total number of rejected requests for a Connector connections in this Connector Pool since the pool is instantiated.</p> ");
      }

      if (!var1.containsKey("ConnectorEisType")) {
         var3 = "getConnectorEisType";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectorEisType", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectorEisType", var2);
         var2.setValue("description", "<p>The EIS type of this Connector connection pool.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("CurrentCapacity")) {
         var3 = "getCurrentCapacity";
         var4 = null;
         var2 = new PropertyDescriptor("CurrentCapacity", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("CurrentCapacity", var2);
         var2.setValue("description", "<p>The PoolSize of this Connector connection pool.</p> ");
      }

      if (!var1.containsKey("EISResourceId")) {
         var3 = "getEISResourceId";
         var4 = null;
         var2 = new PropertyDescriptor("EISResourceId", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("EISResourceId", var2);
         var2.setValue("description", "<p>The EISResourceId of this Connector connection pool.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("FreeConnectionsCurrentCount")) {
         var3 = "getFreeConnectionsCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("FreeConnectionsCurrentCount", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("FreeConnectionsCurrentCount", var2);
         var2.setValue("description", "<p>The current total free connections.</p> ");
      }

      if (!var1.containsKey("FreeConnectionsHighCount")) {
         var3 = "getFreeConnectionsHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("FreeConnectionsHighCount", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("FreeConnectionsHighCount", var2);
         var2.setValue("description", "<p>The high water mark of free connections in this Connector Pool since the pool was instantiated.</p> ");
      }

      if (!var1.containsKey("FreePoolSizeHighWaterMark")) {
         var3 = "getFreePoolSizeHighWaterMark";
         var4 = null;
         var2 = new PropertyDescriptor("FreePoolSizeHighWaterMark", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("FreePoolSizeHighWaterMark", var2);
         var2.setValue("description", "<p>The FreePoolSizeHighWaterMark of this Connector connection pool.</p> ");
      }

      if (!var1.containsKey("FreePoolSizeLowWaterMark")) {
         var3 = "getFreePoolSizeLowWaterMark";
         var4 = null;
         var2 = new PropertyDescriptor("FreePoolSizeLowWaterMark", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("FreePoolSizeLowWaterMark", var2);
         var2.setValue("description", "<p>The FreePoolSizeLowWaterMark of this Connector connection pool.</p> ");
      }

      if (!var1.containsKey("HighestNumWaiters")) {
         var3 = "getHighestNumWaiters";
         var4 = null;
         var2 = new PropertyDescriptor("HighestNumWaiters", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("HighestNumWaiters", var2);
         var2.setValue("description", "<p>Gets the highest number of waiters.</p> ");
      }

      if (!var1.containsKey("InitialCapacity")) {
         var3 = "getInitialCapacity";
         var4 = null;
         var2 = new PropertyDescriptor("InitialCapacity", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("InitialCapacity", var2);
         var2.setValue("description", "<p>The initial capacity configured for this Connector connection pool.</p> ");
      }

      if (!var1.containsKey("JNDIName")) {
         var3 = "getJNDIName";
         var4 = null;
         var2 = new PropertyDescriptor("JNDIName", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("JNDIName", var2);
         var2.setValue("description", "<p>The configured JNDI Name for the Connection Factory using this Connector connection pool.</p> ");
         var2.setValue("deprecated", " ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("Key")) {
         var3 = "getKey";
         var4 = null;
         var2 = new PropertyDescriptor("Key", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("Key", var2);
         var2.setValue("description", "<p>The configured Key for the Connection Factory using this Connector connection pool.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("LastShrinkTime")) {
         var3 = "getLastShrinkTime";
         var4 = null;
         var2 = new PropertyDescriptor("LastShrinkTime", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("LastShrinkTime", var2);
         var2.setValue("description", "<p>Return the last time that the pool was shrunk.</p> ");
      }

      if (!var1.containsKey("LogFileName")) {
         var3 = "getLogFileName";
         var4 = null;
         var2 = new PropertyDescriptor("LogFileName", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("LogFileName", var2);
         var2.setValue("description", "<p>The Log File used by the Resource Adapter for this Connector connection pool.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("LogRuntime")) {
         var3 = "getLogRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("LogRuntime", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("LogRuntime", var2);
         var2.setValue("description", "<p>Get the RuntimeMBean that allows monitoring and control of the log file.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("MCFClassName")) {
         var3 = "getMCFClassName";
         var4 = null;
         var2 = new PropertyDescriptor("MCFClassName", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("MCFClassName", var2);
         var2.setValue("description", "Get the MCF class name. ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("ManagedConnectionFactoryClassName")) {
         var3 = "getManagedConnectionFactoryClassName";
         var4 = null;
         var2 = new PropertyDescriptor("ManagedConnectionFactoryClassName", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ManagedConnectionFactoryClassName", var2);
         var2.setValue("description", "<p>The ManagedConnectionFactoryName of this Connector connection pool.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxCapacity")) {
         var3 = "getMaxCapacity";
         var4 = null;
         var2 = new PropertyDescriptor("MaxCapacity", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("MaxCapacity", var2);
         var2.setValue("description", "<p>The maximum capacity configured for this Connector connection pool.</p> ");
      }

      if (!var1.containsKey("MaxIdleTime")) {
         var3 = "getMaxIdleTime";
         var4 = null;
         var2 = new PropertyDescriptor("MaxIdleTime", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("MaxIdleTime", var2);
         var2.setValue("description", "<p>The configured MaxIdle time for this pool</p> ");
      }

      if (!var1.containsKey("NumUnavailableCurrentCount")) {
         var3 = "getNumUnavailableCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("NumUnavailableCurrentCount", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("NumUnavailableCurrentCount", var2);
         var2.setValue("description", "<p>Return the number of unavailable connections.</p> ");
      }

      if (!var1.containsKey("NumUnavailableHighCount")) {
         var3 = "getNumUnavailableHighCount";
         var4 = null;
         var2 = new PropertyDescriptor("NumUnavailableHighCount", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("NumUnavailableHighCount", var2);
         var2.setValue("description", "<p>Return the highest unavailable number of connections at any given time.</p> ");
      }

      if (!var1.containsKey("NumWaiters")) {
         var3 = "getNumWaiters";
         var4 = null;
         var2 = new PropertyDescriptor("NumWaiters", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("NumWaiters", var2);
         var2.setValue("description", "<p>Gets the current number of waiters.</p> ");
      }

      if (!var1.containsKey("NumWaitersCurrentCount")) {
         var3 = "getNumWaitersCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("NumWaitersCurrentCount", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("NumWaitersCurrentCount", var2);
         var2.setValue("description", "<p>Return the number of waiters.</p> ");
      }

      if (!var1.containsKey("NumberDetectedIdle")) {
         var3 = "getNumberDetectedIdle";
         var4 = null;
         var2 = new PropertyDescriptor("NumberDetectedIdle", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("NumberDetectedIdle", var2);
         var2.setValue("description", "<p>The total number of idle connections detected in the life time of this pool.</p> ");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("NumberDetectedLeaks")) {
         var3 = "getNumberDetectedLeaks";
         var4 = null;
         var2 = new PropertyDescriptor("NumberDetectedLeaks", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("NumberDetectedLeaks", var2);
         var2.setValue("description", "<p>The total number of leaked connections detected in the life time of this pool.</p> ");
         var2.setValue("deprecated", " ");
      }

      if (!var1.containsKey("PoolName")) {
         var3 = "getPoolName";
         var4 = null;
         var2 = new PropertyDescriptor("PoolName", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("PoolName", var2);
         var2.setValue("description", "<p>The configured Logical Name for the Connection Factory using this Connector connection pool.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("PoolSizeHighWaterMark")) {
         var3 = "getPoolSizeHighWaterMark";
         var4 = null;
         var2 = new PropertyDescriptor("PoolSizeHighWaterMark", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("PoolSizeHighWaterMark", var2);
         var2.setValue("description", "<p>The PoolSizeHighWaterMark of this Connector connection pool.</p> ");
      }

      if (!var1.containsKey("PoolSizeLowWaterMark")) {
         var3 = "getPoolSizeLowWaterMark";
         var4 = null;
         var2 = new PropertyDescriptor("PoolSizeLowWaterMark", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("PoolSizeLowWaterMark", var2);
         var2.setValue("description", "<p>The PoolSizeLowWaterMark of this Connector connection pool.</p> ");
      }

      if (!var1.containsKey("RecycledTotal")) {
         var3 = "getRecycledTotal";
         var4 = null;
         var2 = new PropertyDescriptor("RecycledTotal", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("RecycledTotal", var2);
         var2.setValue("description", "<p>The total number of Connector connections that have been recycled in this Connector Pool since the pool is instantiated.</p> ");
      }

      if (!var1.containsKey("ResourceAdapterLinkRefName")) {
         var3 = "getResourceAdapterLinkRefName";
         var4 = null;
         var2 = new PropertyDescriptor("ResourceAdapterLinkRefName", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ResourceAdapterLinkRefName", var2);
         var2.setValue("description", "<p>The Resource Adapter Link Reference for cases where this Connection Factory refers to an existing Resource Adapter deployment.</p> ");
         var2.setValue("deprecated", " ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("ResourceLink")) {
         var3 = "getResourceLink";
         var4 = null;
         var2 = new PropertyDescriptor("ResourceLink", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ResourceLink", var2);
         var2.setValue("description", "Get the resource link. ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("ShrinkCountDownTime")) {
         var3 = "getShrinkCountDownTime";
         var4 = null;
         var2 = new PropertyDescriptor("ShrinkCountDownTime", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ShrinkCountDownTime", var2);
         var2.setValue("description", "<p>The amount of time left (in minutes) until an attempt to shrink the pool will be made.</p> ");
      }

      if (!var1.containsKey("ShrinkPeriodMinutes")) {
         var3 = "getShrinkPeriodMinutes";
         var4 = null;
         var2 = new PropertyDescriptor("ShrinkPeriodMinutes", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ShrinkPeriodMinutes", var2);
         var2.setValue("description", "<p>The Shrink Period (in minutes) of this Connector connection pool.</p> ");
      }

      if (!var1.containsKey("State")) {
         var3 = "getState";
         var4 = null;
         var2 = new PropertyDescriptor("State", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("State", var2);
         var2.setValue("description", "<p>Get the state of the pool.</p> ");
      }

      if (!var1.containsKey("TransactionSupport")) {
         var3 = "getTransactionSupport";
         var4 = null;
         var2 = new PropertyDescriptor("TransactionSupport", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("TransactionSupport", var2);
         var2.setValue("description", "<p>The transaction support level for the Resource Adapter for this Connector connection pool.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("LoggingEnabled")) {
         var3 = "isLoggingEnabled";
         var4 = null;
         var2 = new PropertyDescriptor("LoggingEnabled", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("LoggingEnabled", var2);
         var2.setValue("description", "<p>Indicates whether logging is enabled for this Connector connection pool.</p> ");
      }

      if (!var1.containsKey("ProxyOn")) {
         var3 = "isProxyOn";
         var4 = null;
         var2 = new PropertyDescriptor("ProxyOn", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ProxyOn", var2);
         var2.setValue("description", "<p>Return a flag indicating if the proxy is on. Returns true if it is.</p> ");
      }

      if (!var1.containsKey("ShrinkingEnabled")) {
         var3 = "isShrinkingEnabled";
         var4 = null;
         var2 = new PropertyDescriptor("ShrinkingEnabled", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("ShrinkingEnabled", var2);
         var2.setValue("description", "<p>Specifies whether shrinking of this Connector connection pool is enabled.</p> ");
      }

      if (!var1.containsKey("Testable")) {
         var3 = "isTestable";
         var4 = null;
         var2 = new PropertyDescriptor("Testable", ConnectorConnectionPoolRuntimeMBean.class, var3, (String)var4);
         var1.put("Testable", var2);
         var2.setValue("description", "<p>This indicates whether the connection pool is testable or not.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
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
      Method var3 = ConnectorConnectionPoolRuntimeMBean.class.getMethod("forceLogRotation");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      String[] var5;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var5 = new String[]{BeanInfoHelper.encodeEntities("ManagementException If there is an error during the log file rotation.")};
         var2.setValue("throws", var5);
         var1.put(var4, var2);
         var2.setValue("description", "Forces the rotation of the underlying log immediately. ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorConnectionPoolRuntimeMBean.class.getMethod("preDeregister");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorConnectionPoolRuntimeMBean.class.getMethod("ensureLogOpened");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var5 = new String[]{BeanInfoHelper.encodeEntities("ManagementException If the log could not be opened successfully.")};
         var2.setValue("throws", var5);
         var1.put(var4, var2);
         var2.setValue("description", "Ensures that that the output stream to the underlying is opened if it got closed previously due to errors. ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorConnectionPoolRuntimeMBean.class.getMethod("getConnectionIdleProfiles", Integer.TYPE, Integer.TYPE);
      ParameterDescriptor[] var6 = new ParameterDescriptor[]{createParameterDescriptor("index", "The starting index of the of the idle profiles. "), createParameterDescriptor("count", "The number of idle profiles needed from the index. ")};
      String var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, var6);
         var2.setValue("deprecated", " ");
         var1.put(var7, var2);
         var2.setValue("description", "<p>An array of count LeakProfiles starting at the passed index, in the entire array of Idle profiles.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorConnectionPoolRuntimeMBean.class.getMethod("getConnectionLeakProfiles", Integer.TYPE, Integer.TYPE);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("index", "The starting index of the of the leak profiles. "), createParameterDescriptor("count", "The number of leak profiles needed from the index. ")};
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, var6);
         var2.setValue("deprecated", " ");
         var1.put(var7, var2);
         var2.setValue("description", "<p>An array of count LeakProfiles starting at the passed index, in the entire array of Leak profiles.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorConnectionPoolRuntimeMBean.class.getMethod("testPool");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Test all the available connections in the pool. Returns true if all the connections passed the test and false it at least one failed the test.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorConnectionPoolRuntimeMBean.class.getMethod("forceReset");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var5 = new String[]{BeanInfoHelper.encodeEntities("ManagementException            If any error occurs during resetting the pool. Â Â ")};
         var2.setValue("throws", var5);
         var1.put(var4, var2);
         var2.setValue("description", "<p> Force immediately discard all used/unused connections and recreate connection pool (and using new configuration if user update the pool's configuration). </p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorConnectionPoolRuntimeMBean.class.getMethod("reset");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var5 = new String[]{BeanInfoHelper.encodeEntities("ManagementException            If any error occurs during resetting the pool. Â Â Â ")};
         var2.setValue("throws", var5);
         var1.put(var4, var2);
         var2.setValue("description", "<p> Reset connection pool Discard all unused connections and recreate connection pool (and using new configuration if user update the pool's configuration) if no connection from pool is reserved by client application. If any connection from the connection pool is currently in use, the operation fails and false will be returned, otherwise all connections will be reset and true will be returned. </p> ");
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
