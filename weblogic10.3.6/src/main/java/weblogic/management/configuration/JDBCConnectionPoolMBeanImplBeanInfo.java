package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class JDBCConnectionPoolMBeanImplBeanInfo extends DeploymentMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JDBCConnectionPoolMBean.class;

   public JDBCConnectionPoolMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public JDBCConnectionPoolMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = JDBCConnectionPoolMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("obsolete", "9.0.0.0");
      var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.configuration.JDBCSystemResourceMBean} ");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This bean defines a JDBC connection pool.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.JDBCConnectionPoolMBean");
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

         var2 = new PropertyDescriptor("ACLName", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("ACLName", var2);
         var2.setValue("description", "<p>The access control list (ACL) used to control access to this connection pool.</p>  <p>Permissions available to this ACL are:</p>  <ul> <li><code>Reserve</code>  <p>Allows users to get logical connections from this connection pool.</p> </li>  <li><code>Admin</code>  <p>Allows all other operations on this connection pool, including: reset, shrink, shutdown, disable, and enable.</p> </li> </ul>  <p>Lack of an ACL allows any user open access (provided that the user passes other WLS security controls).</p> ");
         var2.setValue("secureValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      String[] var5;
      if (!var1.containsKey("CapacityIncrement")) {
         var3 = "getCapacityIncrement";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCapacityIncrement";
         }

         var2 = new PropertyDescriptor("CapacityIncrement", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("CapacityIncrement", var2);
         var2.setValue("description", "<p>The increment by which this JDBC connection pool's capacity is expanded. In WebLogic Server 10.3.1 and higher releases, the <code>capacityIncrement</code> is no longer configurable and is set to a value of 1.</p>  <p>When there are no more available physical connections to service requests, the connection pool will create this number of additional physical database connections and add them to the connection pool. The connection pool will ensure that it does not exceed the maximum number of physical connections.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getMaxCapacity")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(1));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("deprecated", "10.3.6.0 ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ConnectionCreationRetryFrequencySeconds")) {
         var3 = "getConnectionCreationRetryFrequencySeconds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnectionCreationRetryFrequencySeconds";
         }

         var2 = new PropertyDescriptor("ConnectionCreationRetryFrequencySeconds", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("ConnectionCreationRetryFrequencySeconds", var2);
         var2.setValue("description", "<p>The number of seconds between when the connection pool retries to establish connections to the database. Do not enable connection retries for connection pools included in a High Availability MultiPool.</p>  <p>If you do not set this value, connection pool creation fails if the database is unavailable. If set and if the database is unavailable when the connection pool is created, WebLogic Server will attempt to create connections in the pool again after the number of seconds you specify, and will continue to attempt to create the connections until it succeeds.</p>  <p><b>Note</b>: Do not enable connection creation retries for connection pools included in a High Availability MultiPool. Connection requests to the MultiPool will fail (not fail-over) when a connection pool in the list is dead and the number of connection requests equals the number of connections in the first connection pool, even if connections are available in subsequent connection pools in the MultiPool.</p>  <p>When set to <code>0</code>, connection retry is disabled.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ConnectionReserveTimeoutSeconds")) {
         var3 = "getConnectionReserveTimeoutSeconds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnectionReserveTimeoutSeconds";
         }

         var2 = new PropertyDescriptor("ConnectionReserveTimeoutSeconds", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("ConnectionReserveTimeoutSeconds", var2);
         var2.setValue("description", "<p>The number of seconds after which a call to reserve a connection from the connection pool will timeout. </p> <p>When set to <tt>0</tt>, a call will never timeout.</p> <p>When set to <tt>-1</tt>, a call will timeout immediately.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getHighestNumWaiters")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(10));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CountOfRefreshFailuresTillDisable")) {
         var3 = "getCountOfRefreshFailuresTillDisable";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCountOfRefreshFailuresTillDisable";
         }

         var2 = new PropertyDescriptor("CountOfRefreshFailuresTillDisable", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("CountOfRefreshFailuresTillDisable", var2);
         var2.setValue("description", "<p>The number of consecutive pool con refresh failures till we disable the pool.</p>  <p>If a pool loses DBMS connectivity, all the connections are dead and we won't be able to replace them till the DBMS is back. Applications that continue to ask the pool for connections will suffer slowness while the pool tries in vain to test dead connections and tries in vain to make a new one. Sometiimes this can take minutes. This setting tells the pool to disable itself so the applications will at least get a quick failure response, and if a multipool is involved, it can fail over to the next pool quickly. The self-disabled pool will periodically try to reconnect, and will re-enable itself asap.  <p>Setting the count to zero means we will not disable.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(2));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CountOfTestFailuresTillFlush")) {
         var3 = "getCountOfTestFailuresTillFlush";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCountOfTestFailuresTillFlush";
         }

         var2 = new PropertyDescriptor("CountOfTestFailuresTillFlush", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("CountOfTestFailuresTillFlush", var2);
         var2.setValue("description", "<p>The number of consecutive pool con test failures till we flush the pool.</p>  <p>If a pool temporarily loses DBMS connectivity, all the connections are probably dead. It is much faster to close them all, rather than have the pool test each one during a reserve, and then replace it. This setting tells the pool to flush all the connections in the pool after N consecutive times finding that a connection being tested is dead.</p>  <p>Setting the count to zero means we will not flush.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(2));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DriverName")) {
         var3 = "getDriverName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDriverName";
         }

         var2 = new PropertyDescriptor("DriverName", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("DriverName", var2);
         var2.setValue("description", "<p>The full package name of JDBC driver class used to create the physical database connections in the connection pool. (Note that this driver class must be in the classpath of any server to which it is deployed.)</p>  <p>For example: <code>oracle.jdbc.OracleDriver</code></p>  <p>It must be the name of a class that implements the <code>java.sql.Driver</code> interface. Check the documentation for the JDBC driver to find the full pathname.</p> ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("EnableResourceHealthMonitoring")) {
         var3 = "getEnableResourceHealthMonitoring";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEnableResourceHealthMonitoring";
         }

         var2 = new PropertyDescriptor("EnableResourceHealthMonitoring", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("EnableResourceHealthMonitoring", var2);
         var2.setValue("description", "Returns whether JTA resource health monitoring is enabled for this XA connection pool. <p> This property applies to XA connection pools only, and is ignored for connection pools that use a non-XA driver. ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("HighestNumUnavailable")) {
         var3 = "getHighestNumUnavailable";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setHighestNumUnavailable";
         }

         var2 = new PropertyDescriptor("HighestNumUnavailable", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("HighestNumUnavailable", var2);
         var2.setValue("description", "<p>The maximum number of connections in the connection pool that can be made unavailable for use by an application. Connections become unavailable while being tested or refreshed.</p>  <p>Note that in cases likes the back end system being unavailable, this specified value could be exceeded due to factors outside the pool's control.</p>  <p>When set to <code>0</code>, this feature is disabled.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("HighestNumWaiters")) {
         var3 = "getHighestNumWaiters";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setHighestNumWaiters";
         }

         var2 = new PropertyDescriptor("HighestNumWaiters", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("HighestNumWaiters", var2);
         var2.setValue("description", "<p>The maximum number of connection requests that can concurrently block threads while waiting to reserve a connection from the connection pool.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getConnectionReserveTimeoutSeconds")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("InactiveConnectionTimeoutSeconds")) {
         var3 = "getInactiveConnectionTimeoutSeconds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setInactiveConnectionTimeoutSeconds";
         }

         var2 = new PropertyDescriptor("InactiveConnectionTimeoutSeconds", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("InactiveConnectionTimeoutSeconds", var2);
         var2.setValue("description", "<p>The number of inactive seconds on a reserved connection before WebLogic Server reclaims the connection and releases it back into the connection pool.</p>  <p>When set to <code>0</code>, the feature is disabled.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("InitSQL")) {
         var3 = "getInitSQL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setInitSQL";
         }

         var2 = new PropertyDescriptor("InitSQL", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("InitSQL", var2);
         var2.setValue("description", "<p>SQL statement to execute that will initialize a newly created physical connection. Start the statement with SQL followed by a space.</p>  <p>If the statement begins with <code>\"SQL \"</code>, then the rest of the string following that leading token will be taken as a literal SQL statement that will be used to initialize a connection. Else, the statement will be treated as the name of a table and the following SQL statement will be used to initialize a connection: <code><br clear=\"none\" /> \"select count(*) from InitSQL\"</code></p>  <p>The table <code>InitSQL</code> must exist and be accessible to the database user for the connection. Most database servers optimize this SQL to avoid a table scan, but it is still a good idea to set <code>InitSQL</code> to the name of a table that is known to have few rows, or even no rows.</p> ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("InitialCapacity")) {
         var3 = "getInitialCapacity";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setInitialCapacity";
         }

         var2 = new PropertyDescriptor("InitialCapacity", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("InitialCapacity", var2);
         var2.setValue("description", "<p>The number of physical database connections to create when creating this JDBC connection pool. If unable to create this number of connections, creation of this connection pool will fail.</p>  <p>This is also the minimum number of physical connections the connection pool will keep available.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(1));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("JDBCXADebugLevel")) {
         var3 = "getJDBCXADebugLevel";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJDBCXADebugLevel";
         }

         var2 = new PropertyDescriptor("JDBCXADebugLevel", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("JDBCXADebugLevel", var2);
         var2.setValue("description", " ");
         setPropertyDescriptorDefault(var2, new Integer(10));
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("KeepLogicalConnOpenOnRelease")) {
         var3 = "getKeepLogicalConnOpenOnRelease";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setKeepLogicalConnOpenOnRelease";
         }

         var2 = new PropertyDescriptor("KeepLogicalConnOpenOnRelease", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("KeepLogicalConnOpenOnRelease", var2);
         var2.setValue("description", "<p>Specifies whether the logical JDBC connection is kept open when the physical XA connection is returned to the XA connection pool. Only applies to connection pools that use an XA driver.</p>  <p>Select this option if the XA driver used to create database connections or the DBMS requires that a logical JDBC connection be kept open while transaction processing continues (although the physical XA connection can returned to the XA connection pool).</p>  <p>Use this setting to work around specific problems with third party vendor's XA driver.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("KeepXAConnTillTxComplete")) {
         var3 = "getKeepXAConnTillTxComplete";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setKeepXAConnTillTxComplete";
         }

         var2 = new PropertyDescriptor("KeepXAConnTillTxComplete", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("KeepXAConnTillTxComplete", var2);
         var2.setValue("description", "<p>Specifies whether the XA connection pool associates the same XA connection with the distributed transaction until the transaction completes. Only applies to connection pools that use an XA driver.</p>  <p>Use this setting to work around specific problems with third party vendor's XA driver.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("LoginDelaySeconds")) {
         var3 = "getLoginDelaySeconds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLoginDelaySeconds";
         }

         var2 = new PropertyDescriptor("LoginDelaySeconds", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("LoginDelaySeconds", var2);
         var2.setValue("description", "<p>The number of seconds to delay before creating each physical database connection.</p>  <p>This delay supports database servers that cannot handle multiple requests for connections in rapid succession</p>  <p>The delay takes place both during initial pool creation and during the lifetime of the pool whenever a physical database connection is created.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("secureValue", new Integer(0));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("MaxCapacity")) {
         var3 = "getMaxCapacity";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxCapacity";
         }

         var2 = new PropertyDescriptor("MaxCapacity", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("MaxCapacity", var2);
         var2.setValue("description", "<p>The maximum number of physical database connections that this JDBC connection pool can contain.</p>  <p>Different JDBC Drivers and database servers may limit the number of possible physical connections.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(15));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("NeedTxCtxOnClose")) {
         var3 = "getNeedTxCtxOnClose";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNeedTxCtxOnClose";
         }

         var2 = new PropertyDescriptor("NeedTxCtxOnClose", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("NeedTxCtxOnClose", var2);
         var2.setValue("description", "<p>Specifies whether the XA driver requires a distributed transaction context when closing various JDBC objects (result sets, statements, connections, and so on). Only applies to connection pools that use an XA driver.</p>  <p>When enabled, SQL exceptions that are thrown while closing the JDBC objects in no transaction context will be swallowed.</p>  <p>Use this setting to work around specific problems with third party vendor's XA driver.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("NewXAConnForCommit")) {
         var3 = "getNewXAConnForCommit";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNewXAConnForCommit";
         }

         var2 = new PropertyDescriptor("NewXAConnForCommit", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("NewXAConnForCommit", var2);
         var2.setValue("description", "<p>Specifies whether a dedicated XA connection is used for commit/rollback processing of a particular distributed transaction. Only applies to connection pools that use an XA driver.</p>  <p>Use this setting to work around specific problems with third party vendor's XA driver.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      String[] var6;
      if (!var1.containsKey("Password")) {
         var3 = "getPassword";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPassword";
         }

         var2 = new PropertyDescriptor("Password", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("Password", var2);
         var2.setValue("description", "<p>The database password.</p> <p>The value of this attribute can be set indirectly by passing it as a key=value pair by passing it to the MBean server's <code>setAttribute</code> method for the <code>Properties</code> attribute. However, Oracle recommends that you set the value directly by passing it to the MBean server's <code>setAttribute</code> method for this (<code>PasswordEncrypted</code>) attribute. <p>When you set the value of this attribute, WebLogic Server does the following:</p> <ol><li>Encrypts the value.</li> <li>Sets the value of the <code>PasswordEncrypted</code> attribute to the encrypted value.</li> </ol> <p>Using this <code>Password</code> attribute is a potential security risk because the String object (which contains the unencrypted password) remains in the JVM's memory until garbage collection removes it and the memory is reallocated. Depending on how memory is allocated in the JVM, a significant amount of time could pass before this unencrypted data is removed from memory.</p>  <p>Instead of using this attribute, use <code>PasswordEncrypted</code>.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getProperties"), BeanInfoHelper.encodeEntities("#getPasswordEncrypted")};
         var2.setValue("see", var5);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
         var6 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
         var2.setValue("rolesAllowedGet", var6);
      }

      if (!var1.containsKey("PasswordEncrypted")) {
         var3 = "getPasswordEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPasswordEncrypted";
         }

         var2 = new PropertyDescriptor("PasswordEncrypted", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("PasswordEncrypted", var2);
         var2.setValue("description", "<p>The encrypted database password.</p> <p>The value of this attribute can be set indirectly by passing it as a key=value pair by passing it to the MBean server's <code>setAttribute</code> method for the <code>Properties</code> attribute. However, Oracle recommends that you set the value directly by using <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the encrypt() method. Doing so overrides the corresponding key=value pair in <code>Properties</code> if you set one.</p>  <p>To compare a password that a user enters with the encrypted value of this attribute, go to the same WebLogic Server instance that you used to set and encrypt this attribute and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values.</p> ");
         var2.setValue("encrypted", Boolean.TRUE);
         var5 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
         var2.setValue("rolesAllowedGet", var5);
      }

      if (!var1.containsKey("PrepStmtCacheProfilingThreshold")) {
         var3 = "getPrepStmtCacheProfilingThreshold";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPrepStmtCacheProfilingThreshold";
         }

         var2 = new PropertyDescriptor("PrepStmtCacheProfilingThreshold", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("PrepStmtCacheProfilingThreshold", var2);
         var2.setValue("description", "<p>The number of statement requests after which a connection pool logs the state of the prepared statement cache.</p>  <p>It is done to minimize output volume. This is a resource-consuming feature, so it's recommended that it's turned off on a production server.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(10));
         var2.setValue("legalMin", new Integer(10));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("PreparedStatementCacheSize")) {
         var3 = "getPreparedStatementCacheSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPreparedStatementCacheSize";
         }

         var2 = new PropertyDescriptor("PreparedStatementCacheSize", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("PreparedStatementCacheSize", var2);
         var2.setValue("description", "<p>The number of prepared statements stored in the cache. (This might increase server performance.)</p>  <p>WebLogic Server can reuse prepared statements in the cache without reloading them, which can increase server performance. Setting the size of the prepared statement cache to 0 turns it off.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("legalMax", new Integer(1024));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Properties")) {
         var3 = "getProperties";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setProperties";
         }

         var2 = new PropertyDescriptor("Properties", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("Properties", var2);
         var2.setValue("description", "<p>The list of properties passed to the JDBC driver that are used to create physical database connections. List each property=value pair on a separate line.</p>  <p>For example: <tt>server=dbserver1</tt>.</p>  <p>The list consists of attribute=value tags, separated by semi-colons. For example <code>user=scott;server=myDB</code>.</p>  <p><strong>Note:</strong> Oracle recommends that you do not store passwords in this properties list. Instead, store them in <code>PasswordEncrypted</code> so that the value is encrypted in <code>config.xml</code> and in the Administration Console display.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getPasswordEncrypted")};
         var2.setValue("see", var5);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("RecoverOnlyOnce")) {
         var3 = "getRecoverOnlyOnce";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRecoverOnlyOnce";
         }

         var2 = new PropertyDescriptor("RecoverOnlyOnce", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("RecoverOnlyOnce", var2);
         var2.setValue("description", "<p>Specifies whether JTA TM should call recover on the resource only once. Only applies to connection pools that use an XA driver.</p>  <p>Use this setting to work around specific problems with third party vendor's XA driver.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("RefreshMinutes")) {
         var3 = "getRefreshMinutes";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRefreshMinutes";
         }

         var2 = new PropertyDescriptor("RefreshMinutes", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("RefreshMinutes", var2);
         var2.setValue("description", "<p>The number of minutes between database connection tests. (Requires that you specify a Test Table Name.)</p>  <p>At the specified interval, unused database connections are tested. Connections that do not pass the test will be closed and reopened to re-establish a valid physical database connection.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getTestTableName")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("legalMax", new Integer(35791394));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RollbackLocalTxUponConnClose")) {
         var3 = "getRollbackLocalTxUponConnClose";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRollbackLocalTxUponConnClose";
         }

         var2 = new PropertyDescriptor("RollbackLocalTxUponConnClose", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("RollbackLocalTxUponConnClose", var2);
         var2.setValue("description", "<p>Specifies whether the WLS connection pool will call <code>rollback()</code> on the connection before putting it back in the pool.</p>  <p>Enabling this attribute will have a performance impact as the rollback call requires communication with the database server.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("SecondsToTrustAnIdlePoolConnection")) {
         var3 = "getSecondsToTrustAnIdlePoolConnection";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSecondsToTrustAnIdlePoolConnection";
         }

         var2 = new PropertyDescriptor("SecondsToTrustAnIdlePoolConnection", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("SecondsToTrustAnIdlePoolConnection", var2);
         var2.setValue("description", "<p>The number of seconds within the use of a pooled connection that WebLogic Server trusts that the connection is still viable and will skip connection testing. </p>  <p>If an application requests a connection within the time specified since the connection was tested or successfully used and returned to the connection pool, WebLogic Server skips the connection test before delivering it to an application (if TestConnectionsOnReserve is enabled). </p>  <p>WebLogic Server also skips the automatic refresh connection test if the connection was successfully used and returned to the connection pool within the time specified (if TestFrequencySeconds is specified).</p>  <p>SecondsToTrustAnIdlePoolConnection is a tuning feature that can improve application performance by minimizing the delay caused by database connection testing, especially during heavy traffic. However, it can reduce the effectiveness of connection testing, especially if the value is set too high. The appropriate value depends on your environment and the likelihood that a connection will become defunct.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(10));
         var2.setValue("secureValue", new Integer(0));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ShrinkFrequencySeconds")) {
         var3 = "getShrinkFrequencySeconds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setShrinkFrequencySeconds";
         }

         var2 = new PropertyDescriptor("ShrinkFrequencySeconds", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("ShrinkFrequencySeconds", var2);
         var2.setValue("description", "<p>The number of seconds before WebLogic Server shrinks the connection pool to the original number of connections or number of connections currently in use. (Requires that you enable connection pool shrinking.)</p> ");
         setPropertyDescriptorDefault(var2, new Integer(900));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ShrinkPeriodMinutes")) {
         var3 = "getShrinkPeriodMinutes";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setShrinkPeriodMinutes";
         }

         var2 = new PropertyDescriptor("ShrinkPeriodMinutes", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("ShrinkPeriodMinutes", var2);
         var2.setValue("description", "<p>The number of minutes to wait before shrinking a connection pool that has incrementally increased to meet demand. (Requires that you enable connection pool shrinking.)</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#isShrinkingEnabled")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(15));
         var2.setValue("secureValue", new Integer(15));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SqlStmtMaxParamLength")) {
         var3 = "getSqlStmtMaxParamLength";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSqlStmtMaxParamLength";
         }

         var2 = new PropertyDescriptor("SqlStmtMaxParamLength", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("SqlStmtMaxParamLength", var2);
         var2.setValue("description", "<p>The maximum length of the string passed as a parameter for JDBC SQL round trip profiling. (Requires that you enable the logging of prepared statement parameters.)</p>  <p>As it is a resource-consuming feature, limiting length of data for a parameter allows to reduce output volume.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#isSqlStmtParamLoggingEnabled")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(10));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("StatementCacheSize")) {
         var3 = "getStatementCacheSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStatementCacheSize";
         }

         var2 = new PropertyDescriptor("StatementCacheSize", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("StatementCacheSize", var2);
         var2.setValue("description", "<p>The number of prepared and callable statements stored in the cache. (This might increase server performance.)</p>  <p>WebLogic Server can reuse statements in the cache without reloading them, which can increase server performance. Each connection in the pool has its own cache of statements.</p>  <p>Setting the size of the statement cache to 0 turns it off.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(10));
         var2.setValue("legalMax", new Integer(1024));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("StatementCacheType")) {
         var3 = "getStatementCacheType";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStatementCacheType";
         }

         var2 = new PropertyDescriptor("StatementCacheType", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("StatementCacheType", var2);
         var2.setValue("description", "<p>The algorithm used for maintaining the prepared statements stored in the cache.</p>  <p>Connection pools support the following cache types:</p>  <ul> <li><tt>LRU</tt> <p>replaces the least recently used statements when new statements are used.</p> </li>  <li><tt>FIXED</tt> <p>keeps the first fixed number of statements in the cache.</p> </li> </ul> ");
         setPropertyDescriptorDefault(var2, "LRU");
         var2.setValue("legalValues", new Object[]{"LRU", "FIXED"});
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("StatementTimeout")) {
         var3 = "getStatementTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStatementTimeout";
         }

         var2 = new PropertyDescriptor("StatementTimeout", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("StatementTimeout", var2);
         var2.setValue("description", "<p>The time after which a statement currently being executed will be timed-out.</p>  <p>Efficacy of this feature relies on underlying JDBC driver support. WebLogic Server passes the time specified to the JDBC driver using the <code>java.sql.Statement.setQueryTimeout()</code> method. If your JDBC driver does not support this method, it may throw an exception and the timeout value is ignored.</p>  <p>A value of <code>-1</code> disables this feature.</p> <p>A value of <code>0</code> means that statements will not time out.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SupportsLocalTransaction")) {
         var3 = "getSupportsLocalTransaction";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSupportsLocalTransaction";
         }

         var2 = new PropertyDescriptor("SupportsLocalTransaction", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("SupportsLocalTransaction", var2);
         var2.setValue("description", "<p>Specifies whether the XA driver used to create physical database connections supports SQL without global transactions. Only applies to connection pools that use an XA driver.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("Targets")) {
         var3 = "getTargets";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTargets";
         }

         var2 = new PropertyDescriptor("Targets", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("Targets", var2);
         var2.setValue("description", "<p>You must select a target on which an MBean will be deployed from this list of the targets in the current domain on which this item can be deployed. Targets must be either servers or clusters. The deployment will only occur once if deployments overlap.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("adder", "addTarget");
         var2.setValue("remover", "removeTarget");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TestConnectionsOnCreate")) {
         var3 = "getTestConnectionsOnCreate";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTestConnectionsOnCreate";
         }

         var2 = new PropertyDescriptor("TestConnectionsOnCreate", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("TestConnectionsOnCreate", var2);
         var2.setValue("description", "<p>Specifies whether WebLogic Server tests a connection after creating it but before adding it to the list of connections available in the pool. (Requires that you specify a Test Table Name.)</p>  <p>The test adds a small delay in creating the connection, but ensures that the client receives a working connection (assuming that the DBMS is available and accessible).</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getTestTableName")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TestConnectionsOnRelease")) {
         var3 = "getTestConnectionsOnRelease";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTestConnectionsOnRelease";
         }

         var2 = new PropertyDescriptor("TestConnectionsOnRelease", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("TestConnectionsOnRelease", var2);
         var2.setValue("description", "<p>Specifies whether WebLogic Server tests a connection before returning it to this JDBC connection pool. (Requires that you specify a Test Table Name.)</p>  <p>If all connections in the pool are already in use and a client is waiting for a connection, the client's wait will be slightly longer while the connection is tested.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getTestTableName")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TestConnectionsOnReserve")) {
         var3 = "getTestConnectionsOnReserve";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTestConnectionsOnReserve";
         }

         var2 = new PropertyDescriptor("TestConnectionsOnReserve", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("TestConnectionsOnReserve", var2);
         var2.setValue("description", "<p>Specifies whether WebLogic Server tests a connection before giving it to the client. This test is required for connection pools used within a MultiPool that use the High Availability algorithm. (Requires that you specify a Test Table Name.)</p>  <p>The test adds a small delay in serving the client's request for a connection from the pool, but ensures that the client receives a working connection (assuming that the DBMS is available and accessible).</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getTestTableName")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TestFrequencySeconds")) {
         var3 = "getTestFrequencySeconds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTestFrequencySeconds";
         }

         var2 = new PropertyDescriptor("TestFrequencySeconds", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("TestFrequencySeconds", var2);
         var2.setValue("description", "<p>The number of seconds between when WebLogic Server tests unused database connections. (Requires that you specify a Test Table Name.)</p>  <p>Connections that fail the test are closed and reopened to re-establish a valid physical database connection. If the test fails again, the connection is closed.</p>  <p>When set to <code>0</code>, periodic testing is disabled.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getTestTableName"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.JDBCConnectionPoolMBean#setHighestNumUnavailable(int)")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(120));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TestStatementTimeout")) {
         var3 = "getTestStatementTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTestStatementTimeout";
         }

         var2 = new PropertyDescriptor("TestStatementTimeout", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("TestStatementTimeout", var2);
         var2.setValue("description", "<p>The time after which the test statement (configured by applications using the pool attribute Test Table Name) or initialization statement (configured by applications using the pool attribute InitSQL) currently being executed will be timed out.</p>  <p>Efficacy of this feature relies on underlying JDBC driver support. WebLogic Server passes the time specified to the JDBC driver using the <code>java.sql.Statement.setQueryTimeout()</code> method. If your JDBC driver does not support this method, it may throw an exception and the timeout value is ignored.</p>  <p>A value of <code>-1</code> disables this feature.</p> <p>A value of <code>0</code> means that statements will not time out.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getTestTableName"), BeanInfoHelper.encodeEntities("#getInitSQL")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TestTableName")) {
         var3 = "getTestTableName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTestTableName";
         }

         var2 = new PropertyDescriptor("TestTableName", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("TestTableName", var2);
         var2.setValue("description", "<p>The name of the database table to use when testing physical database connections. This name is required when you specify a Test Frequency and enable Test Reserved Connections, Test Created Connections, and Test Released Connections.</p>  <p>The default SQL code used to test a connection is<br clear=\"none\" /> </p>  <code>\"select count(*) from TestTableName\"</code> <p>Most database servers optimize this SQL to avoid a table scan, but it is still a good idea to set the Test Table Name to the name of a table that is known to have few rows, or even no rows.</p>  <p>If the name begins with <code>\"SQL \"</code>, then the rest of the string following that leading token will be taken as a literal SQL statement that will be used to test a connection.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("URL")) {
         var3 = "getURL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setURL";
         }

         var2 = new PropertyDescriptor("URL", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("URL", var2);
         var2.setValue("description", "<p>The URL of the database to connect to. The format of the URL varies by JDBC driver.</p>  <p>The URL is passed to the JDBC driver to create the physical database connections.</p> ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("XAEndOnlyOnce")) {
         var3 = "getXAEndOnlyOnce";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setXAEndOnlyOnce";
         }

         var2 = new PropertyDescriptor("XAEndOnlyOnce", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("XAEndOnlyOnce", var2);
         var2.setValue("description", "<p>Specifies that <tt>XAResource.end()</tt> is called only once for each pending <tt>XAResource.start()</tt>. The XA driver will not call <tt>XAResource.end(TMSUSPEND)</tt>, <tt>XAResource.end(TMSUCCESS)</tt> successively. Only applies to connection pools that use an XA driver.</p>  <p>Use this setting to work around specific problems with third party vendor's XA driver.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("XAPassword")) {
         var3 = "getXAPassword";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setXAPassword";
         }

         var2 = new PropertyDescriptor("XAPassword", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("XAPassword", var2);
         var2.setValue("description", "<p>The password that is used to create physical XA database connections.</p>  <p>The value of this attribute can be set indirectly by passing it as a key=value pair by passing it to the MBean server's <code>setAttribute</code> method for the <code>Properties</code> attribute. However, Oracle recommends that you set the value directly by passing it to the MBean server's <code>setAttribute</code> method for this (<code>XAPassword</code>) attribute.  <p>When you set the value of this attribute, WebLogic Server does the following:</p> <ol><li>Encrypts the value.</li> <li>Sets the value of the <code>XAPasswordEncrypted</code> attribute to the encrypted value.</li> </ol> <p>Using this <code>XAPassword</code> attribute is a potential security risk because the String object (which contains the unencrypted password) remains in the JVM's memory until garbage collection removes it and the memory is reallocated. Depending on how memory is allocated in the JVM, a significant amount of time could pass before this unencrypted data is removed from memory.</p>  <p>Instead of using this attribute, use <code>XAPasswordEncrypted</code>.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getProperties"), BeanInfoHelper.encodeEntities("#getXAPasswordEncrypted")};
         var2.setValue("see", var5);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
         var6 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
         var2.setValue("rolesAllowedGet", var6);
      }

      if (!var1.containsKey("XAPasswordEncrypted")) {
         var3 = "getXAPasswordEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setXAPasswordEncrypted";
         }

         var2 = new PropertyDescriptor("XAPasswordEncrypted", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("XAPasswordEncrypted", var2);
         var2.setValue("description", "<p>The password that is used to create physical XA database connections.</p>  <p>The value of this attribute can be set indirectly by passing it as a key=value pair by passing it to the MBean server's <code>setAttribute</code> method for the <code>Properties</code> attribute. However, Oracle recommends that you set the value directly by using <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the encrypt() method. Doing so overrides the corresponding key=value pair in <code>Properties</code> if you set one. It also causes WebLogic Server to encrypt the value and set the attribute to the encrypted value.</p>  <p>To compare a password that a user enters with the encrypted value of this attribute, go to the same WebLogic Server instance that you used to set and encrypt this attribute and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values.</p> ");
         var2.setValue("encrypted", Boolean.TRUE);
         var5 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
         var2.setValue("rolesAllowedGet", var5);
      }

      if (!var1.containsKey("XAPreparedStatementCacheSize")) {
         var3 = "getXAPreparedStatementCacheSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setXAPreparedStatementCacheSize";
         }

         var2 = new PropertyDescriptor("XAPreparedStatementCacheSize", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("XAPreparedStatementCacheSize", var2);
         var2.setValue("description", "<p>The maximum number of prepared statements cached by this particular XA connection pool. Only applies to connection pools that use an XA driver.</p>  <p>If set to <code>0</code>, caching is turned off.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("legalMax", new Integer(1024));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("XARetryDurationSeconds")) {
         var3 = "getXARetryDurationSeconds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setXARetryDurationSeconds";
         }

         var2 = new PropertyDescriptor("XARetryDurationSeconds", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("XARetryDurationSeconds", var2);
         var2.setValue("description", "Determines the duration in seconds for which the transaction manager will perform recover operations on the resource.  A value of zero indicates that no retries will be performed. ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("XARetryIntervalSeconds")) {
         var3 = "getXARetryIntervalSeconds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setXARetryIntervalSeconds";
         }

         var2 = new PropertyDescriptor("XARetryIntervalSeconds", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("XARetryIntervalSeconds", var2);
         var2.setValue("description", "Determines the time in seconds between XA retry operations if the XARetryDurationSeconds attribute is set to a positive value. ");
         setPropertyDescriptorDefault(var2, new Integer(60));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("XASetTransactionTimeout")) {
         var3 = "getXASetTransactionTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setXASetTransactionTimeout";
         }

         var2 = new PropertyDescriptor("XASetTransactionTimeout", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("XASetTransactionTimeout", var2);
         var2.setValue("description", "<p>When set to true, the WebLogic Server Transaction Manager calls XAResource.setTransactionTimeout() before calling XAResource.start, and passes either the XATransactionTimeout or the global transaction timeout in seconds. </p> <p>When set to false, the Transaction Manager does not call setTransactionTimeout().</p> <p>This property applies to XA connection pools only, and is ignored for connection pools that use a non-XA driver. </p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("XATransactionTimeout")) {
         var3 = "getXATransactionTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setXATransactionTimeout";
         }

         var2 = new PropertyDescriptor("XATransactionTimeout", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("XATransactionTimeout", var2);
         var2.setValue("description", "<p>Determines the number of seconds to pass as the transaction timeout value in the XAResource.setTransactionTimeout() method. When this property is set to 0, the WebLogic Server Transaction Manager passes the global WebLogic Server transaction timeout in seconds in the method.</p> <p>If set, this value should be greater than or equal to the global WebLogic Server transaction timeout.</p> <p>XASetTransactionTimeout must be set to \"true\" or this property is ignored. </p> <p>This property applies to XA connection pools only, and is ignored for connection pools that use a non-XA driver.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ConnLeakProfilingEnabled")) {
         var3 = "isConnLeakProfilingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnLeakProfilingEnabled";
         }

         var2 = new PropertyDescriptor("ConnLeakProfilingEnabled", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("ConnLeakProfilingEnabled", var2);
         var2.setValue("description", "<p>Specifies that JDBC connection leak information is gathered. This option is required to view leaked connections from the connection pool.</p>  <p>A Connection leak occurs when a connection from the pool is not closed explicitly by calling <code>close()</code> on that connection.</p>  <p>When connection leak profiling is active, the connection pool will store the stack trace at the time the Connection object is allocated from the connection pool and given to the client. When a connection leak is detected (when the Connection object is garbage collected), this stack trace is reported.</p>  <p>This feature uses extra resources and will likely slow down connection pool operations, so it is not recommended for production use.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ConnProfilingEnabled")) {
         var3 = "isConnProfilingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnProfilingEnabled";
         }

         var2 = new PropertyDescriptor("ConnProfilingEnabled", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("ConnProfilingEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the connection pool detects local transaction work left incomplete by application code, which can interfere with subsequent operations related to global (XA) transactions.</p>  <p>When connection profiling is active, the pool will store the stack trace at the time the connection object is released back into the pool by the client. If an exception is thrown during a subsequent operation related to global (XA) transactions, this stack trace is reported.</p>  <p>This feature uses extra resources and will likely slowdown Connection Pool operations, so it is not recommended for production use.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CredentialMappingEnabled")) {
         var3 = "isCredentialMappingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCredentialMappingEnabled";
         }

         var2 = new PropertyDescriptor("CredentialMappingEnabled", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("CredentialMappingEnabled", var2);
         var2.setValue("description", "Returns the configured ignoreInUseConnectionsEnabled value. ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("IgnoreInUseConnectionsEnabled")) {
         var3 = "isIgnoreInUseConnectionsEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setIgnoreInUseConnectionsEnabled";
         }

         var2 = new PropertyDescriptor("IgnoreInUseConnectionsEnabled", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("IgnoreInUseConnectionsEnabled", var2);
         var2.setValue("description", "Returns the configured ignoreInUseConnectionsEnabled value. ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("PrepStmtCacheProfilingEnabled")) {
         var3 = "isPrepStmtCacheProfilingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPrepStmtCacheProfilingEnabled";
         }

         var2 = new PropertyDescriptor("PrepStmtCacheProfilingEnabled", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("PrepStmtCacheProfilingEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the connection pool stores prepared statement cache profiles in external storage for further analysis.</p>  <p>This is a resource-consuming feature, so it's recommended that it's turned off on a production server.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("RemoveInfectedConnectionsEnabled")) {
         var3 = "isRemoveInfectedConnectionsEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRemoveInfectedConnectionsEnabled";
         }

         var2 = new PropertyDescriptor("RemoveInfectedConnectionsEnabled", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("RemoveInfectedConnectionsEnabled", var2);
         var2.setValue("description", "<p>Specifies whether a connection will be removed from the connection pool when the application asks for the underlying vendor connection object.</p>  <p>If you use this setting, you must make sure that the database connection is suitable for reuse by other applications.</p>  <p>When enabled, the physical connection is not returned to the connection pool after the application closes the logical connection. Instead, the physical connection is closed and recreated.</p>  <p>When not enabled, when you close the logical connection, the physical connection is returned to the connection pool.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.jdbc.extensions.WLConnection#getVendorConnection")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ShrinkingEnabled")) {
         var3 = "isShrinkingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setShrinkingEnabled";
         }

         var2 = new PropertyDescriptor("ShrinkingEnabled", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("ShrinkingEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the JDBC connection pool can shrink back to its initial capacity or to the current number of connections in use if it detects that connections created during increased traffic are not being used.</p>  <p>When shrinking, the number of connections is reduced to the greater of either the initial capacity or the current number of connections in use.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("secureValue", new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SqlStmtParamLoggingEnabled")) {
         var3 = "isSqlStmtParamLoggingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSqlStmtParamLoggingEnabled";
         }

         var2 = new PropertyDescriptor("SqlStmtParamLoggingEnabled", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("SqlStmtParamLoggingEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the connection pool stores the values of prepared statement parameters. (Requires that you enable SQL statement profiling.)</p>  <p>This is a resource-consuming feature, so it's recommended that it's turned off on a production server.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#isSqlStmtProfilingEnabled")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("SqlStmtProfilingEnabled")) {
         var3 = "isSqlStmtProfilingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSqlStmtProfilingEnabled";
         }

         var2 = new PropertyDescriptor("SqlStmtProfilingEnabled", JDBCConnectionPoolMBean.class, var3, var4);
         var1.put("SqlStmtProfilingEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the connection pool stores SQL statement text, execution time and other metrics.</p>  <p>This is a resource-consuming feature, so it's recommended that it's turned off on a production server.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = JDBCConnectionPoolMBean.class.getMethod("addTarget", TargetMBean.class);
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

      var3 = JDBCConnectionPoolMBean.class.getMethod("removeTarget", TargetMBean.class);
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
      Method var3 = JDBCConnectionPoolMBean.class.getMethod("freezeCurrentValue", String.class);
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

      var3 = JDBCConnectionPoolMBean.class.getMethod("restoreDefaultValue", String.class);
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
