package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class JDBCDataSourceMBeanImplBeanInfo extends DeploymentMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JDBCDataSourceMBean.class;

   public JDBCDataSourceMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public JDBCDataSourceMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = JDBCDataSourceMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("obsolete", "9.0.0.0");
      var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.configuration.JDBCSystemResourceMBean} ");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>This MBean defines a non-transactional JDBC data source.</p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.JDBCDataSourceMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("ConnectionWaitPeriod")) {
         var3 = "getConnectionWaitPeriod";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnectionWaitPeriod";
         }

         var2 = new PropertyDescriptor("ConnectionWaitPeriod", JDBCDataSourceMBean.class, var3, var4);
         var1.put("ConnectionWaitPeriod", var2);
         var2.setValue("description", "<p>The number of seconds that the system will wait for a free connection to become available if a request is received when there are no free connections in the connection pool. (Requires that you enable the system to wait for connections.)</p>  <p>A WebLogic Server server thread is occupied while waiting for a free connection, which can result in a decrease in system throughput. Therefore, the recommended way to handle the situation is to increase the number of connections in the connection pool.</p> ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("#getWaitForConnectionEnabled")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(1));
         var2.setValue("legalMax", new Integer(60));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("JNDIName")) {
         var3 = "getJNDIName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJNDIName";
         }

         var2 = new PropertyDescriptor("JNDIName", JDBCDataSourceMBean.class, var3, var4);
         var1.put("JNDIName", var2);
         var2.setValue("description", "<p>The JNDI path to where this Data Source is bound.</p>  <p>Applications that look up the JNDI path will get a <code>javax.sql.DataSource</code> instance that corresponds to this Data Source. Note that the old style usage of <code>DriverManager.getConnection()</code> or <code>Driver.connect()</code> has been deprecated in favor of looking up a Data Source to obtain a connection.</p> ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("JNDINameSeparator")) {
         var3 = "getJNDINameSeparator";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJNDINameSeparator";
         }

         var2 = new PropertyDescriptor("JNDINameSeparator", JDBCDataSourceMBean.class, var3, var4);
         var1.put("JNDINameSeparator", var2);
         var2.setValue("description", "The JNDI Seperator is used to seperate JNDIName list for this DataSource. <p> Applications that look up the JNDI path will get a <CODE>javax.sql.DataSource</CODE> instance that corresponds to this DataSource. ");
         setPropertyDescriptorDefault(var2, "JNDINameSeparator");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", JDBCDataSourceMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("PoolName")) {
         var3 = "getPoolName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPoolName";
         }

         var2 = new PropertyDescriptor("PoolName", JDBCDataSourceMBean.class, var3, var4);
         var1.put("PoolName", var2);
         var2.setValue("description", "<p>The PoolName attribute applies to legacy data source configurations only. Do not set a PoolName for a WebLogic Server 9.0 or later JDBC data source.</p>  <p>For JDBC data source configurations before WebLogic Server 9.0, the PoolName is the name of the JDBC connection pool that is associated with this data source. Calls from applications to <code>getConnection()</code> on this Data Source return a connection from the associated connection pool.</p> ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("RowPrefetchSize")) {
         var3 = "getRowPrefetchSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRowPrefetchSize";
         }

         var2 = new PropertyDescriptor("RowPrefetchSize", JDBCDataSourceMBean.class, var3, var4);
         var1.put("RowPrefetchSize", var2);
         var2.setValue("description", "<p>Specifies the number of result set rows to prefetch for a client.</p>  <p>The optimal value depends on the particulars of the query. In general, increasing this number will increase performance, until a particular value is reached. At that point further increases do not result in any significant performance increase. Very rarely will increased performance result from exceeding 100 rows. The default value should be reasonable for most situations.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(48));
         var2.setValue("legalMax", new Integer(65536));
         var2.setValue("legalMin", new Integer(2));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("StreamChunkSize")) {
         var3 = "getStreamChunkSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStreamChunkSize";
         }

         var2 = new PropertyDescriptor("StreamChunkSize", JDBCDataSourceMBean.class, var3, var4);
         var1.put("StreamChunkSize", var2);
         var2.setValue("description", "<p>Specifies the data chunk size for steaming data types.</p>  <p>Streaming data types (for example resulting from a call to <code>getBinaryStream()</code>) will be pulled in sized chunks from WebLogic Server to the client as needed.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(256));
         var2.setValue("legalMax", new Integer(65536));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Targets")) {
         var3 = "getTargets";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTargets";
         }

         var2 = new PropertyDescriptor("Targets", JDBCDataSourceMBean.class, var3, var4);
         var1.put("Targets", var2);
         var2.setValue("description", "<p>You must select a target on which an MBean will be deployed from this list of the targets in the current domain on which this item can be deployed. Targets must be either servers or clusters. The deployment will only occur once if deployments overlap.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("adder", "addTarget");
         var2.setValue("remover", "removeTarget");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RowPrefetchEnabled")) {
         var3 = "isRowPrefetchEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRowPrefetchEnabled";
         }

         var2 = new PropertyDescriptor("RowPrefetchEnabled", JDBCDataSourceMBean.class, var3, var4);
         var1.put("RowPrefetchEnabled", var2);
         var2.setValue("description", "<p>Specifies whether multiple rows should be \"prefetched\" (that is, sent from the server to the client) in one server access.</p>  <p>When an external client accesses a database using JDBC through WebLogic Server, row prefetching improves performance by fetching multiple rows from the server to the client in one server access. WebLogic Server ignores this setting and does not use row prefetching when the client and WebLogic Server are in the same JVM.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("WaitForConnectionEnabled")) {
         var3 = "isWaitForConnectionEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWaitForConnectionEnabled";
         }

         var2 = new PropertyDescriptor("WaitForConnectionEnabled", JDBCDataSourceMBean.class, var3, var4);
         var1.put("WaitForConnectionEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the system waits for a connection to become available if a request is made when all connections in the connection pool are in use. Oracle recommends that you do not use this feature in a production environment.</p>  <p>A WebLogic Server server thread is occupied while waiting for a free connection, which can result in a decrease in system throughput. Therefore, the recommended way to handle the situation is to increase the number of connections in the connection pool.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = JDBCDataSourceMBean.class.getMethod("addTarget", TargetMBean.class);
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

      var3 = JDBCDataSourceMBean.class.getMethod("removeTarget", TargetMBean.class);
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
      Method var3 = JDBCDataSourceMBean.class.getMethod("freezeCurrentValue", String.class);
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

      var3 = JDBCDataSourceMBean.class.getMethod("restoreDefaultValue", String.class);
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
