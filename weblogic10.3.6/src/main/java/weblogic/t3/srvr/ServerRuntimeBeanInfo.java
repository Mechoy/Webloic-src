package weblogic.t3.srvr;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.RequestClassRuntimeMBean;
import weblogic.management.runtime.ServerRuntimeMBean;

public class ServerRuntimeBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ServerRuntimeMBean.class;

   public ServerRuntimeBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ServerRuntimeBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ServerRuntime.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.t3.srvr");
      String var3 = (new String("Provides methods for retrieving runtime information about a server instance and for transitioning a server from one state to another.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.ServerRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("ActivationTime")) {
         var3 = "getActivationTime";
         var4 = null;
         var2 = new PropertyDescriptor("ActivationTime", ServerRuntimeMBean.class, var3, var4);
         var1.put("ActivationTime", var2);
         var2.setValue("description", "<p>The time when the server was started.</p> ");
      }

      if (!var1.containsKey("AdminServerHost")) {
         var3 = "getAdminServerHost";
         var4 = null;
         var2 = new PropertyDescriptor("AdminServerHost", ServerRuntimeMBean.class, var3, var4);
         var1.put("AdminServerHost", var2);
         var2.setValue("description", "<p>The address on which the Administration Server is listening for connections. For example, this might return the string: santiago</p> ");
      }

      if (!var1.containsKey("AdminServerListenPort")) {
         var3 = "getAdminServerListenPort";
         var4 = null;
         var2 = new PropertyDescriptor("AdminServerListenPort", ServerRuntimeMBean.class, var3, var4);
         var1.put("AdminServerListenPort", var2);
         var2.setValue("description", "<p>The port on which the Administration Server is listening for connections.</p> ");
      }

      if (!var1.containsKey("AdministrationPort")) {
         var3 = "getAdministrationPort";
         var4 = null;
         var2 = new PropertyDescriptor("AdministrationPort", ServerRuntimeMBean.class, var3, var4);
         var1.put("AdministrationPort", var2);
         var2.setValue("description", "<p>The port on which this server is listening for administrative requests.</p> ");
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link #getAdministrationURL} ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("AdministrationURL")) {
         var3 = "getAdministrationURL";
         var4 = null;
         var2 = new PropertyDescriptor("AdministrationURL", ServerRuntimeMBean.class, var3, var4);
         var1.put("AdministrationURL", var2);
         var2.setValue("description", "<p>The URL that the server and its clients use for administrative connections.</p>  <p>If no administration channel is enabled, then this method returns the URL for connections through the default channel. If the default channel is de-activated, this method returns the URL for a secure channel. If no secure channel is enabled, the method returns null.</p>  <p>The returned URL will be consistent with dynamic channel updates.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("ApplicationRuntimes")) {
         var3 = "getApplicationRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("ApplicationRuntimes", ServerRuntimeMBean.class, var3, var4);
         var1.put("ApplicationRuntimes", var2);
         var2.setValue("description", "<p> Returns the list of currently running Applications </p> ");
         var2.setValue("relationship", "reference");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.0.0", (String)null, this.targetVersion) && !var1.containsKey("AsyncReplicationRuntime")) {
         var3 = "getAsyncReplicationRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("AsyncReplicationRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("AsyncReplicationRuntime", var2);
         var2.setValue("description", "<p>Return an MBean which represents this server's view of its AsyncReplicationRuntime, if any.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "10.3.0.0");
      }

      if (!var1.containsKey("ClusterRuntime")) {
         var3 = "getClusterRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("ClusterRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("ClusterRuntime", var2);
         var2.setValue("description", "<p>Return an MBean which represents this server's view of its cluster, if any.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("ConnectorServiceRuntime")) {
         var3 = "getConnectorServiceRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectorServiceRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("ConnectorServiceRuntime", var2);
         var2.setValue("description", "The access point for server wide control and monitoring of the Connector Container. ");
         var2.setValue("relationship", "reference");
      }

      String[] var5;
      if (!var1.containsKey("CurrentDirectory")) {
         var3 = "getCurrentDirectory";
         var4 = null;
         var2 = new PropertyDescriptor("CurrentDirectory", ServerRuntimeMBean.class, var3, var4);
         var1.put("CurrentDirectory", var2);
         var2.setValue("description", "<p>The absolute path of the directory from which the server was started.</p>  <p>This may be used in conjunction with other relative paths in ServerMBean to compute full paths.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean")};
         var2.setValue("see", var5);
      }

      if (!var1.containsKey("CurrentMachine")) {
         var3 = "getCurrentMachine";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCurrentMachine";
         }

         var2 = new PropertyDescriptor("CurrentMachine", ServerRuntimeMBean.class, var3, var4);
         var1.put("CurrentMachine", var2);
         var2.setValue("description", "Return the machine on which the server is running. This will be different from the configuration if the server gets migrated automatically. * @return Machine on which server is running. ");
      }

      if (!var1.containsKey("DefaultExecuteQueueRuntime")) {
         var3 = "getDefaultExecuteQueueRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("DefaultExecuteQueueRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("DefaultExecuteQueueRuntime", var2);
         var2.setValue("description", "<p>Return an MBean which exposes this server's default execute queue.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("DefaultURL")) {
         var3 = "getDefaultURL";
         var4 = null;
         var2 = new PropertyDescriptor("DefaultURL", ServerRuntimeMBean.class, var3, var4);
         var1.put("DefaultURL", var2);
         var2.setValue("description", "<p>The URL that clients use to connect to this server's default network channel.</p>  <p>The returned value indicates the default protocol, listen address and listen port:<br clear=\"none\" /> <i>protocol</i>://<i>listen-address</i>:<i>listen-port</i></p>  <dl> <dt>Note:</dt>  <dd> <p>The default protocol, listen address and listen port are persisted in the domain's <code>config.xml</code> file, however when a server instance is started, command-line options can override these persisted values. This <code>getDefaultURL</code> method returns the URL values that are currently being used, not necessarily the values that are specified in <code>config.xml</code>.</p> </dd> </dl>  <p>The returned URL will be consistent with dynamic channel updates.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("EntityCacheCumulativeRuntime")) {
         var3 = "getEntityCacheCumulativeRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("EntityCacheCumulativeRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("EntityCacheCumulativeRuntime", var2);
         var2.setValue("description", "<p>Return an MBean which represents Cumulative Status of the XML Cache.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("EntityCacheCurrentStateRuntime")) {
         var3 = "getEntityCacheCurrentStateRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("EntityCacheCurrentStateRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("EntityCacheCurrentStateRuntime", var2);
         var2.setValue("description", "<p>Return an MBean which represents Current Status of the XML Cache.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("EntityCacheHistoricalRuntime")) {
         var3 = "getEntityCacheHistoricalRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("EntityCacheHistoricalRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("EntityCacheHistoricalRuntime", var2);
         var2.setValue("description", "<p>Return an MBean which represents Historical Status of the XML Cache.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("ExecuteQueueRuntimes")) {
         var3 = "getExecuteQueueRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("ExecuteQueueRuntimes", ServerRuntimeMBean.class, var3, var4);
         var1.put("ExecuteQueueRuntimes", var2);
         var2.setValue("description", "<p>Returns an array of MBeans which exposes this server's active execute queues.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("HealthState")) {
         var3 = "getHealthState";
         var4 = null;
         var2 = new PropertyDescriptor("HealthState", ServerRuntimeMBean.class, var3, var4);
         var1.put("HealthState", var2);
         var2.setValue("description", "<p>The health state of the server as reported by the server's self-health monitoring. See <a href=../../e13941/weblogic/health/HealthState.html>weblogic.health.HealthState</a> for state values. </p>  <p>For example, the server can report if it is overloaded by too many requests, if it needs more memory resources, or if it will soon fail for other reasons.</p> ");
      }

      if (!var1.containsKey("JDBCServiceRuntime")) {
         var3 = "getJDBCServiceRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("JDBCServiceRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("JDBCServiceRuntime", var2);
         var2.setValue("description", "<p>The JDBCServiceRuntimeMBean for this server.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("JMSRuntime")) {
         var3 = "getJMSRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("JMSRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("JMSRuntime", var2);
         var2.setValue("description", "<p>The JMSRuntimeMBean for this server.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("JTARuntime")) {
         var3 = "getJTARuntime";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJTARuntime";
         }

         var2 = new PropertyDescriptor("JTARuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("JTARuntime", var2);
         var2.setValue("description", "<p>The transaction RuntimeMBean for this server.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("JTARuntimeMBean")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("JVMRuntime")) {
         var3 = "getJVMRuntime";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJVMRuntime";
         }

         var2 = new PropertyDescriptor("JVMRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("JVMRuntime", var2);
         var2.setValue("description", "<p>The JVMRuntimeMBean for this server.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("JVMRuntimeMBean")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("JoltRuntime")) {
         var3 = "getJoltRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("JoltRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("JoltRuntime", var2);
         var2.setValue("description", "<p>The JoltConnectionServiceRuntimeMBean for this server.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("LibraryRuntimes")) {
         var3 = "getLibraryRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("LibraryRuntimes", ServerRuntimeMBean.class, var3, var4);
         var1.put("LibraryRuntimes", var2);
         var2.setValue("description", "<p> Returns all deployed Libraries </p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("ListenAddress")) {
         var3 = "getListenAddress";
         var4 = null;
         var2 = new PropertyDescriptor("ListenAddress", ServerRuntimeMBean.class, var3, var4);
         var1.put("ListenAddress", var2);
         var2.setValue("description", "<p>The address on which this server is listening for connections through the default network channel.</p>  <p>For example this might return the string: <code>santiago/172.17.9.220</code>.</p>  <p>You can configure other network channels for this server, and the other channels can use different listen addresses.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getServerChannel(String)")};
         var2.setValue("see", var5);
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link #getURL} ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("ListenPort")) {
         var3 = "getListenPort";
         var4 = null;
         var2 = new PropertyDescriptor("ListenPort", ServerRuntimeMBean.class, var3, var4);
         var1.put("ListenPort", var2);
         var2.setValue("description", "<p>The port on which this server is listening for connections.</p> ");
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link #getURL} ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("LogBroadcasterRuntime")) {
         var3 = "getLogBroadcasterRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("LogBroadcasterRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("LogBroadcasterRuntime", var2);
         var2.setValue("description", "The object which generates notifications on behalf of the logging subystem. ");
         var2.setValue("relationship", "containment");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("LogRuntime")) {
         var3 = "getLogRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("LogRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("LogRuntime", var2);
         var2.setValue("description", "<p>Return the MBean which provides access to the control interface for WLS server logging.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.0.0", (String)null, this.targetVersion) && !var1.containsKey("MANAsyncReplicationRuntime")) {
         var3 = "getMANAsyncReplicationRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("MANAsyncReplicationRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("MANAsyncReplicationRuntime", var2);
         var2.setValue("description", "<p>Return an MBean which represents this server's view of its MANAsyncReplicationRuntime, if any.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "10.3.0.0");
      }

      if (!var1.containsKey("MANReplicationRuntime")) {
         var3 = "getMANReplicationRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("MANReplicationRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("MANReplicationRuntime", var2);
         var2.setValue("description", "<p>Return an MBean which represents this server's view of its MANReplicationRuntime, if any.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("MailSessionRuntimes")) {
         var3 = "getMailSessionRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("MailSessionRuntimes", ServerRuntimeMBean.class, var3, var4);
         var1.put("MailSessionRuntimes", var2);
         var2.setValue("description", "<p>Return the runtimeMBeans for JavaMail Mail Sessions ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("MaxThreadsConstraintRuntimes")) {
         var3 = "getMaxThreadsConstraintRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("MaxThreadsConstraintRuntimes", ServerRuntimeMBean.class, var3, var4);
         var1.put("MaxThreadsConstraintRuntimes", var2);
         var2.setValue("description", "<p>Returns an array of RuntimeMBeans which exposes this server's globally defined MaxThreadsConstraints.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("MessagingBridgeRuntime")) {
         var3 = "getMessagingBridgeRuntime";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMessagingBridgeRuntime";
         }

         var2 = new PropertyDescriptor("MessagingBridgeRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("MessagingBridgeRuntime", var2);
         var2.setValue("description", "<p> The MessagingBridgeRuntimeMBean for this server </p> ");
         var2.setValue("relationship", "reference");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.3.0", (String)null, this.targetVersion) && !var1.containsKey("MiddlewareHome")) {
         var3 = "getMiddlewareHome";
         var4 = null;
         var2 = new PropertyDescriptor("MiddlewareHome", ServerRuntimeMBean.class, var3, var4);
         var1.put("MiddlewareHome", var2);
         var2.setValue("description", "<p>The Oracle Middleware installation directory. </p> ");
         var2.setValue("since", "10.3.3.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("MinThreadsConstraintRuntimes")) {
         var3 = "getMinThreadsConstraintRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("MinThreadsConstraintRuntimes", ServerRuntimeMBean.class, var3, var4);
         var1.put("MinThreadsConstraintRuntimes", var2);
         var2.setValue("description", "<p>Returns an array of RuntimeMBeans which exposes this server's globally defined MinThreadsConstraints.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("OpenSocketsCurrentCount")) {
         var3 = "getOpenSocketsCurrentCount";
         var4 = null;
         var2 = new PropertyDescriptor("OpenSocketsCurrentCount", ServerRuntimeMBean.class, var3, var4);
         var1.put("OpenSocketsCurrentCount", var2);
         var2.setValue("description", "<p>The current number of sockets registered for socket muxing on this server.</p> ");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion) && !var1.containsKey("OracleHome")) {
         var3 = "getOracleHome";
         var4 = null;
         var2 = new PropertyDescriptor("OracleHome", ServerRuntimeMBean.class, var3, var4);
         var1.put("OracleHome", var2);
         var2.setValue("description", "<p>The directory where Oracle products are installed. </p> Deprecated since 10.3.3 ");
         var2.setValue("deprecated", "WLS can no longer find out what ORACLE_HOME is ");
         var2.setValue("since", "10.3.1.0");
      }

      if (!var1.containsKey("OverallHealthState")) {
         var3 = "getOverallHealthState";
         var4 = null;
         var2 = new PropertyDescriptor("OverallHealthState", ServerRuntimeMBean.class, var3, var4);
         var1.put("OverallHealthState", var2);
         var2.setValue("description", "<p> Determine the overall health state of this server, taking into account the health of each of its subsystems. See <a href=../../e13941/weblogic/health/HealthState.html>weblogic.health.HealthState</a> for state values. </p> ");
      }

      if (!var1.containsKey("PathServiceRuntime")) {
         var3 = "getPathServiceRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("PathServiceRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("PathServiceRuntime", var2);
         var2.setValue("description", "<p>The PathServiceRuntimeMBean for this server.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("PendingRestartSystemResources")) {
         var3 = "getPendingRestartSystemResources";
         var4 = null;
         var2 = new PropertyDescriptor("PendingRestartSystemResources", ServerRuntimeMBean.class, var3, var4);
         var1.put("PendingRestartSystemResources", var2);
         var2.setValue("description", "<p> Returns all the System Resources that need to be restarted </p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("PersistentStoreRuntimes")) {
         var3 = "getPersistentStoreRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("PersistentStoreRuntimes", ServerRuntimeMBean.class, var3, var4);
         var1.put("PersistentStoreRuntimes", var2);
         var2.setValue("description", "<p>Returns the mbeans that provides runtime information for each PersistentStore.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("RequestClassRuntimes")) {
         var3 = "getRequestClassRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("RequestClassRuntimes", ServerRuntimeMBean.class, var3, var4);
         var1.put("RequestClassRuntimes", var2);
         var2.setValue("description", "<p>Returns an array of RuntimeMBeans which exposes this server's globally defined Request Classes.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("adder", "addRequestClassRuntime");
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("RestartsTotalCount")) {
         var3 = "getRestartsTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("RestartsTotalCount", ServerRuntimeMBean.class, var3, var4);
         var1.put("RestartsTotalCount", var2);
         var2.setValue("description", "<p>The total number of restarts for this server since the cluster was last started.</p> ");
         var2.setValue("deprecated", "This attribute always returns a value of 0. Please use {@link ServerLifeCycleRuntimeMBean#getNodeManagerRestartCount()} if the NodeManager is used to start servers ");
      }

      if (!var1.containsKey("SAFRuntime")) {
         var3 = "getSAFRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("SAFRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("SAFRuntime", var2);
         var2.setValue("description", "<p>The SAFRuntimeMBean for this server.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("SNMPAgentRuntime")) {
         var3 = "getSNMPAgentRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("SNMPAgentRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("SNMPAgentRuntime", var2);
         var2.setValue("description", "<p>Return the MBean which provides access to the monitoring statistics for WLS SNMP Agent.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "10.0.0.0");
      }

      if (!var1.containsKey("SSLListenAddress")) {
         var3 = "getSSLListenAddress";
         var4 = null;
         var2 = new PropertyDescriptor("SSLListenAddress", ServerRuntimeMBean.class, var3, var4);
         var1.put("SSLListenAddress", var2);
         var2.setValue("description", "<p>The address on which this server is listening for SSL connections. For example this might return the string: santiago/172.17.9.220</p> ");
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link #getURL} ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("SSLListenPort")) {
         var3 = "getSSLListenPort";
         var4 = null;
         var2 = new PropertyDescriptor("SSLListenPort", ServerRuntimeMBean.class, var3, var4);
         var1.put("SSLListenPort", var2);
         var2.setValue("description", "<p>The port on which this server is listening for SSL connections.</p> ");
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link #getURL} ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("ServerChannelRuntimes")) {
         var3 = "getServerChannelRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("ServerChannelRuntimes", ServerRuntimeMBean.class, var3, var4);
         var1.put("ServerChannelRuntimes", var2);
         var2.setValue("description", "<p>The network channels that are currently configured on the server.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("ServerClasspath")) {
         var3 = "getServerClasspath";
         var4 = null;
         var2 = new PropertyDescriptor("ServerClasspath", ServerRuntimeMBean.class, var3, var4);
         var1.put("ServerClasspath", var2);
         var2.setValue("description", "Get the classpath for this server including domain/lib contents that are automatically picked up and appended to the classpath. ");
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("ServerSecurityRuntime")) {
         var3 = "getServerSecurityRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("ServerSecurityRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("ServerSecurityRuntime", var2);
         var2.setValue("description", "<p>Return the ServerSecurityRuntimeMBean for this server.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("ServerServiceVersions")) {
         var3 = "getServerServiceVersions";
         var4 = null;
         var2 = new PropertyDescriptor("ServerServiceVersions", ServerRuntimeMBean.class, var3, var4);
         var1.put("ServerServiceVersions", var2);
         var2.setValue("description", "Returns a map of ServerService names and their versions. The key is the service name and the value is the version string. This method is provided primarily for console and is not intended for remote use. ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("ServerStartupTime")) {
         var3 = "getServerStartupTime";
         var4 = null;
         var2 = new PropertyDescriptor("ServerStartupTime", ServerRuntimeMBean.class, var3, var4);
         var1.put("ServerStartupTime", var2);
         var2.setValue("description", "The amount of time taken for the server to transition from <code>STARTING</code> to <code>RUNNING</code> state. ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("SingleSignOnServicesRuntime")) {
         var3 = "getSingleSignOnServicesRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("SingleSignOnServicesRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("SingleSignOnServicesRuntime", var2);
         var2.setValue("description", "<p>Get the runtime interface to publish single sign-on services information. </p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("SocketsOpenedTotalCount")) {
         var3 = "getSocketsOpenedTotalCount";
         var4 = null;
         var2 = new PropertyDescriptor("SocketsOpenedTotalCount", ServerRuntimeMBean.class, var3, var4);
         var1.put("SocketsOpenedTotalCount", var2);
         var2.setValue("description", "<p>The total number of registrations for socket muxing on this sever.</p> ");
         var2.setValue("deprecated", "Use {@link #getOpenSocketsCurrentCount} instead. Both methods return the same value. This method is being deprecated in favor of the other method. ");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.0.0", (String)null, this.targetVersion) && !var1.containsKey("StableState")) {
         var3 = "getStableState";
         var4 = null;
         var2 = new PropertyDescriptor("StableState", ServerRuntimeMBean.class, var3, var4);
         var1.put("StableState", var2);
         var2.setValue("description", "It returns the end state for the server if it's transitioning or the current state if it is already in stable state ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("since", "10.3.0.0");
      }

      if (!var1.containsKey("State")) {
         var3 = "getState";
         var4 = null;
         var2 = new PropertyDescriptor("State", ServerRuntimeMBean.class, var3, var4);
         var1.put("State", var2);
         var2.setValue("description", "<p>The current life cycle state of this server.</p>  <p>For example, a server can be in a RUNNING state in which it can receive and process requests or in an ADMIN state in which it can receive only administrative requests.</p> ");
      }

      if (!var1.containsKey("StateVal")) {
         var3 = "getStateVal";
         var4 = null;
         var2 = new PropertyDescriptor("StateVal", ServerRuntimeMBean.class, var3, var4);
         var1.put("StateVal", var2);
         var2.setValue("description", "<p>Returns current state of the server as in integer. {@link weblogic.management.runtime.ServerStates} has more information about the available server states</p> ");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("SubsystemHealthStates")) {
         var3 = "getSubsystemHealthStates";
         var4 = null;
         var2 = new PropertyDescriptor("SubsystemHealthStates", ServerRuntimeMBean.class, var3, var4);
         var1.put("SubsystemHealthStates", var2);
         var2.setValue("description", "Returns an array of health states for major subsystems in the server. Exposed only to console to display a table of health states ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("ThreadPoolRuntime")) {
         var3 = "getThreadPoolRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("ThreadPoolRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("ThreadPoolRuntime", var2);
         var2.setValue("description", "<p>Get the self-tuning thread pool's runtime information. This call will return <code>null</code> if the self-tuning implementation is not enabled.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("TimeServiceRuntime")) {
         var3 = "getTimeServiceRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("TimeServiceRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("TimeServiceRuntime", var2);
         var2.setValue("description", "<p>Get the runtime information about the WebLogic timer implementation. </p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("TimerRuntime")) {
         var3 = "getTimerRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("TimerRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("TimerRuntime", var2);
         var2.setValue("description", "<p>Get the runtime information about the WebLogic timer implementation. </p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("WANReplicationRuntime")) {
         var3 = "getWANReplicationRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("WANReplicationRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("WANReplicationRuntime", var2);
         var2.setValue("description", "<p>Return an MBean which represents this server's view of its WANReplicationRuntime, if any.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("WLDFRuntime")) {
         var3 = "getWLDFRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("WLDFRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("WLDFRuntime", var2);
         var2.setValue("description", "<p>Return the MBean which provides access to all Diagnostic runtime MBeans.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("WLECConnectionServiceRuntime")) {
         var3 = "getWLECConnectionServiceRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("WLECConnectionServiceRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("WLECConnectionServiceRuntime", var2);
         var2.setValue("description", "<p>The WLECConnectionServiceRuntime for this server.</p> ");
      }

      if (!var1.containsKey("WTCRuntime")) {
         var3 = "getWTCRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("WTCRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("WTCRuntime", var2);
         var2.setValue("description", "<p>The WTCRuntimeMBean for this server.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("WebServerRuntimes")) {
         var3 = "getWebServerRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("WebServerRuntimes", ServerRuntimeMBean.class, var3, var4);
         var1.put("WebServerRuntimes", var2);
         var2.setValue("description", "<p> Returns all the initialized webservers </p> ");
         var2.setValue("relationship", "reference");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion) && !var1.containsKey("WeblogicHome")) {
         var3 = "getWeblogicHome";
         var4 = null;
         var2 = new PropertyDescriptor("WeblogicHome", ServerRuntimeMBean.class, var3, var4);
         var1.put("WeblogicHome", var2);
         var2.setValue("description", "<p>The directory where the WebLogic Server instance (server) is installed, without the trailing \"/server\".</p> ");
         var2.setValue("since", "10.3.1.0");
      }

      if (!var1.containsKey("WeblogicVersion")) {
         var3 = "getWeblogicVersion";
         var4 = null;
         var2 = new PropertyDescriptor("WeblogicVersion", ServerRuntimeMBean.class, var3, var4);
         var1.put("WeblogicVersion", var2);
         var2.setValue("description", "<p>The version of this WebLogic Server instance (server).</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("WorkManagerRuntimes")) {
         var3 = "getWorkManagerRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("WorkManagerRuntimes", ServerRuntimeMBean.class, var3, var4);
         var1.put("WorkManagerRuntimes", var2);
         var2.setValue("description", "<p>Returns an array of MBeans which exposes this server's active internal WorkManagers.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.2", (String)null, this.targetVersion) && !var1.containsKey("WseeClusterFrontEndRuntime")) {
         var3 = "getWseeClusterFrontEndRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("WseeClusterFrontEndRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("WseeClusterFrontEndRuntime", var2);
         var2.setValue("description", "This is non-null only when this server is running as a host to a front-end proxy (HttpClusterServlet) instance. ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "10.3.1.2");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.2", (String)null, this.targetVersion) && !var1.containsKey("WseeWsrmRuntime")) {
         var3 = "getWseeWsrmRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("WseeWsrmRuntime", ServerRuntimeMBean.class, var3, var4);
         var1.put("WseeWsrmRuntime", var2);
         var2.setValue("description", "Get statistics for web services reliable messaging across the entire server if any web service is deployed that employs reliable messaging. This MBean is null otherwise. ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "10.3.1.2");
      }

      if (!var1.containsKey("AdminServer")) {
         var3 = "isAdminServer";
         var4 = null;
         var2 = new PropertyDescriptor("AdminServer", ServerRuntimeMBean.class, var3, var4);
         var1.put("AdminServer", var2);
         var2.setValue("description", "<p>Indicates whether the server is an Administration Server.</p> ");
      }

      if (!var1.containsKey("AdminServerListenPortSecure")) {
         var3 = "isAdminServerListenPortSecure";
         var4 = null;
         var2 = new PropertyDescriptor("AdminServerListenPortSecure", ServerRuntimeMBean.class, var3, var4);
         var1.put("AdminServerListenPortSecure", var2);
         var2.setValue("description", "<p>Indicates whether the port that the server uses for administrative traffic is configured to use a secure protocol.</p> ");
      }

      if (!var1.containsKey("AdministrationPortEnabled")) {
         var3 = "isAdministrationPortEnabled";
         var4 = null;
         var2 = new PropertyDescriptor("AdministrationPortEnabled", ServerRuntimeMBean.class, var3, var4);
         var1.put("AdministrationPortEnabled", var2);
         var2.setValue("description", "<p>Indicates whether the administration port is enabled on the server</p> ");
      }

      if (!var1.containsKey("ClusterMaster")) {
         var3 = "isClusterMaster";
         var4 = null;
         var2 = new PropertyDescriptor("ClusterMaster", ServerRuntimeMBean.class, var3, var4);
         var1.put("ClusterMaster", var2);
         var2.setValue("description", "<p>Indicates whether the server is the ClusterMaster of a cluster which is configured for server migration.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("ListenPortEnabled")) {
         var3 = "isListenPortEnabled";
         var4 = null;
         var2 = new PropertyDescriptor("ListenPortEnabled", ServerRuntimeMBean.class, var3, var4);
         var1.put("ListenPortEnabled", var2);
         var2.setValue("description", "<p>Indicates whether the default listen port is enabled on the server.</p> ");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("RestartRequired")) {
         var3 = "isRestartRequired";
         var4 = null;
         var2 = new PropertyDescriptor("RestartRequired", ServerRuntimeMBean.class, var3, var4);
         var1.put("RestartRequired", var2);
         var2.setValue("description", "<p>Indicates whether the server must be restarted in order to activate configuration changes.</p> ");
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("SSLListenPortEnabled")) {
         var3 = "isSSLListenPortEnabled";
         var4 = null;
         var2 = new PropertyDescriptor("SSLListenPortEnabled", ServerRuntimeMBean.class, var3, var4);
         var1.put("SSLListenPortEnabled", var2);
         var2.setValue("description", "<p>Indicates whether the default SSL listen port is enabled on the server</p> ");
      }

      if (!var1.containsKey("ShuttingDown")) {
         var3 = "isShuttingDown";
         var4 = null;
         var2 = new PropertyDescriptor("ShuttingDown", ServerRuntimeMBean.class, var3, var4);
         var1.put("ShuttingDown", var2);
         var2.setValue("description", "<p>Check if the server is shutting down.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = ServerRuntimeMBean.class.getMethod("addRequestClassRuntime", RequestClassRuntimeMBean.class);
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         MethodDescriptor var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "collection");
         var2.setValue("property", "RequestClassRuntimes");
      }

   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = ServerRuntimeMBean.class.getMethod("lookupMinThreadsConstraintRuntime", String.class);
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "finder");
         var2.setValue("property", "MinThreadsConstraintRuntimes");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = ServerRuntimeMBean.class.getMethod("lookupRequestClassRuntime", String.class);
         var4 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var4)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("since", "9.0.0.0");
            var1.put(var4, var2);
            var2.setValue("description", " ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "finder");
            var2.setValue("property", "RequestClassRuntimes");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = ServerRuntimeMBean.class.getMethod("lookupMaxThreadsConstraintRuntime", String.class);
         var4 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var4)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("since", "9.0.0.0");
            var1.put(var4, var2);
            var2.setValue("description", " ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "finder");
            var2.setValue("property", "MaxThreadsConstraintRuntimes");
            var2.setValue("since", "9.0.0.0");
         }
      }

      var3 = ServerRuntimeMBean.class.getMethod("lookupApplicationRuntime", String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p> Returns the ApplicationRuntimeMBean asked for, by name. </p> ");
         var2.setValue("role", "finder");
         var2.setValue("property", "ApplicationRuntimes");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
         var2.setValue("rolesAllowed", var5);
      }

      var3 = ServerRuntimeMBean.class.getMethod("lookupLibraryRuntime", String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p> Returns the LibraryRuntimeMBean asked for, by name. </p> ");
         var2.setValue("role", "finder");
         var2.setValue("property", "LibraryRuntimes");
      }

      var3 = ServerRuntimeMBean.class.getMethod("lookupPersistentStoreRuntime", String.class);
      ParameterDescriptor[] var7 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      String var6 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var6)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var6, var2);
         var2.setValue("description", "Returns the Runtime mbean for the persistent store with the specified short name. ");
         var2.setValue("role", "finder");
         var2.setValue("property", "PersistentStoreRuntimes");
      }

   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = ServerRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = ServerRuntimeMBean.class.getMethod("suspend");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      String[] var5;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Suspend server. Deny new requests (except by privileged users). Allow pending requests to complete. This operation transitions the server into <code>ADMIN</code> state. Applications and resources are fully available to administrators in <code>ADMIN</code> state. But non-admin users are denied access to applications and resources</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#suspend(int, boolean)")};
         var2.setValue("see", var5);
         var2.setValue("role", "operation");
      }

      var3 = ServerRuntimeMBean.class.getMethod("suspend", Integer.TYPE, Boolean.TYPE);
      ParameterDescriptor[] var7 = new ParameterDescriptor[]{createParameterDescriptor("timeout", "Seconds to wait for server to transition gracefully. The server calls {@link #forceSuspend()} after timeout. "), createParameterDescriptor("ignoreSessions", "drop inflight HTTP sessions during graceful suspend ")};
      String var8 = BeanInfoHelper.buildMethodKey(var3);
      String[] var6;
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var6 = new String[]{BeanInfoHelper.encodeEntities("ServerLifecycleException server failed to suspend gracefully.  A {@link #forceSuspend()} or a {@link #forceShutdown()} operation can be  invoked.")};
         var2.setValue("throws", var6);
         var1.put(var8, var2);
         var2.setValue("description", "<p>Transitions the server from <code>RUNNING</code> to <code>ADMIN</code> state gracefully.</p>  <p>Applications are in admin mode. Inflight work is completed. Applications and resources are fully available to administrators in <code>ADMIN</code> state. But non-admin users are denied access to applications and resources</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ServerRuntimeMBean.class.getMethod("forceSuspend");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var5 = new String[]{BeanInfoHelper.encodeEntities("ServerLifecycleException server failed to force suspend.  A {@link #forceShutdown()} operation can be invoked.")};
         var2.setValue("throws", var5);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Transitions the server from <code>RUNNING</code> to <code>ADMIN</code> state forcefully cancelling inflight work.</p>  <p>Work that cannot be cancelled is dropped. Applications are brought into the admin mode. This is the supported way of force suspending the server and getting it into <code>ADMIN</code> state. ");
         var2.setValue("role", "operation");
      }

      var3 = ServerRuntimeMBean.class.getMethod("resume");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Resume suspended server. Allow new requests. This operation transitions the server into <code>RUNNING</code> state.</p> ");
         var2.setValue("role", "operation");
         var5 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
         var2.setValue("rolesAllowed", var5);
      }

      var3 = ServerRuntimeMBean.class.getMethod("shutdown");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Gracefully shuts down the server after handling inflight work.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#shutdown(int, boolean)")};
         var2.setValue("see", var5);
         var2.setValue("role", "operation");
         var6 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
         var2.setValue("rolesAllowed", var6);
      }

      var3 = ServerRuntimeMBean.class.getMethod("shutdown", Integer.TYPE, Boolean.TYPE);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("timeout", "Number of seconds to wait before aborting inflight work and shutting down the server. "), createParameterDescriptor("ignoreSessions", "<code>true</code> indicates ignore pending HTTP sessions during inflight work handling. ")};
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var8, var2);
         var2.setValue("description", "<p>Gracefully shuts down the server after handling inflight work; optionally ignores pending HTTP sessions while handling inflight work.</p>  <p>The following inflight work is allowed to complete before shutdown:</p>  <ul> <li> <p>Pending transaction's and TLOG checkpoint</p> </li>  <li> <p>Pending HTTP sessions</p> </li>  <li> <p>Pending JMS work</p> </li>  <li> <p>Pending work in the execute queues</p> </li>  <li> <p>RMI requests with transaction context</p> </li> </ul>  <p>Further administrative calls are accepted while the server is completing inflight work. For example a forceShutdown command can be issued to quickly shutdown the server if graceful shutdown takes a long time.</p> ");
         var2.setValue("role", "operation");
         var6 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
         var2.setValue("rolesAllowed", var6);
      }

      var3 = ServerRuntimeMBean.class.getMethod("forceShutdown");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Force shutdown the server. Causes the server to reject new requests and fail pending requests.</p> ");
         var2.setValue("role", "operation");
         var5 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
         var2.setValue("rolesAllowed", var5);
      }

      var3 = ServerRuntimeMBean.class.getMethod("start");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "Use {@link #suspend} and {@link #resume} operations instead of lock/unlock. ");
         var1.put(var4, var2);
         var2.setValue("description", "Unlocks a server and enables it to receive new requests. <p> Servers can be locked with the <code>java weblogic.Admin LOCK</code> command. In a locked state, a server instance accepts only administrative logins. ");
         var2.setValue("role", "operation");
         var5 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
         var2.setValue("rolesAllowed", var5);
      }

      var3 = ServerRuntimeMBean.class.getMethod("getServerChannel", String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("protocol", "the desired protocol ")};
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var8, var2);
         var2.setValue("description", "<p>The address on which this server is listening for connections that use the specified protocol.</p>  <dl> <dt>Note:</dt>  <dd> <p>The listen address is persisted in the domain's <code>config.xml</code> file, however when a server instance is started, a command-line option can override the persisted listen address. This <code>getServerChannel</code> method returns the listen address that is currently being used, not necessarily the address that is specified in <code>config.xml</code>.</p> </dd> </dl>  <p>The returned address will always be resolved.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ServerRuntimeMBean.class.getMethod("getURL", String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("protocol", "the desired protocol ")};
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var8, var2);
         var2.setValue("description", "<p>The URL that clients use when connecting to this server using the specified protocol.</p>  <dl> <dt>Note:</dt>  <dd> <p>The listen address and listen port for a given protocol are persisted in the domain's <code>config.xml</code> file, however when a server instance is started, command-line options can override these persisted values. This <code>getURL</code> method returns the URL values that are currently being used, not necessarily the values that are specified in <code>config.xml</code>.</p> </dd> </dl> ");
         var2.setValue("role", "operation");
         var2.setValue("rolePermitAll", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion)) {
         var3 = ServerRuntimeMBean.class.getMethod("getIPv4URL", String.class);
         var7 = new ParameterDescriptor[]{createParameterDescriptor("protocol", "the desired protocol ")};
         var8 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var8)) {
            var2 = new MethodDescriptor(var3, var7);
            var2.setValue("since", "10.3.1.0");
            var1.put(var8, var2);
            var2.setValue("description", "<p>The URL that clients use when connecting to this server using the specified protocol.</p>  <dl> <dt>Note:</dt>  <dd> <p>The listen address and listen port for a given protocol are persisted in the domain's <code>config.xml</code> file, however when a server instance is started, command-line options can override these persisted values. This <code>getURL</code> method returns the URL values that are currently being used, not necessarily the values that are specified in <code>config.xml</code>.</p> </dd> </dl> ");
            var2.setValue("role", "operation");
            var2.setValue("rolePermitAll", Boolean.TRUE);
            var2.setValue("since", "10.3.1.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion)) {
         var3 = ServerRuntimeMBean.class.getMethod("getIPv6URL", String.class);
         var7 = new ParameterDescriptor[]{createParameterDescriptor("protocol", "the desired protocol ")};
         var8 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var8)) {
            var2 = new MethodDescriptor(var3, var7);
            var2.setValue("since", "10.3.1.0");
            var1.put(var8, var2);
            var2.setValue("description", "<p>The URL that clients use when connecting to this server using the specified protocol.</p>  <dl> <dt>Note:</dt>  <dd> <p>The listen address and listen port for a given protocol are persisted in the domain's <code>config.xml</code> file, however when a server instance is started, command-line options can override these persisted values. This <code>getURL</code> method returns the URL values that are currently being used, not necessarily the values that are specified in <code>config.xml</code>.</p> </dd> </dl> ");
            var2.setValue("role", "operation");
            var2.setValue("rolePermitAll", Boolean.TRUE);
            var2.setValue("since", "10.3.1.0");
         }
      }

      var3 = ServerRuntimeMBean.class.getMethod("setHealthState", Integer.TYPE, String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("state", "The new healthState value "), createParameterDescriptor("reason", "The new healthState value ")};
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var8, var2);
         var2.setValue("description", "<p>For Server Health Monitoring.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = ServerRuntimeMBean.class.getMethod("restartSSLChannels");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "Restart all SSL channels on which the server is listening. This could be necessary because of some change that the server is not aware of, for instance updates to the keystore. ");
         var2.setValue("role", "operation");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.0.0", (String)null, this.targetVersion)) {
         var3 = ServerRuntimeMBean.class.getMethod("isServiceAvailable", String.class);
         var4 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var4)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("since", "10.3.0.0");
            var1.put(var4, var2);
            var2.setValue("description", "<p>returns true iff the named service is available (configured, licensed & running)</p> The service String is either ServerService.EJB, ServerService.CONNECTOR, ServerService.JMS or the Bundle-SymbolicName of a service plugin ");
            var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.server.ServerService")};
            var2.setValue("see", var5);
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "operation");
            var2.setValue("rolePermitAll", Boolean.TRUE);
            var2.setValue("since", "10.3.0.0");
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
