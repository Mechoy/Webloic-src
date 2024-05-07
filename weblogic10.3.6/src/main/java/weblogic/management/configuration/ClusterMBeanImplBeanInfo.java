package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class ClusterMBeanImplBeanInfo extends TargetMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ClusterMBean.class;

   public ClusterMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ClusterMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ClusterMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This bean represents a cluster in the domain. Servers join a cluster by calling ServerMBean.setCluster with the logical name of the cluster. A configuration may define zero or more clusters. They may be looked up by logical name.  The name of a cluster denotes its logical cluster name.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.ClusterMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("AdditionalAutoMigrationAttempts")) {
         var3 = "getAdditionalAutoMigrationAttempts";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAdditionalAutoMigrationAttempts";
         }

         var2 = new PropertyDescriptor("AdditionalAutoMigrationAttempts", ClusterMBean.class, var3, var4);
         var1.put("AdditionalAutoMigrationAttempts", var2);
         var2.setValue("description", "A migratable server could fail to come up on every possible configured machine. This attribute controls how many further attempts, after the first one, should be tried.  Note that each attempt specified here indicates another full circuit of migrations amongst all the configured machines. So for a 3-server cluster, and the default value of 3, a total of 9 migrations will be attempted.  If it is set to -1, migrations will go on forever until the server starts. ");
         setPropertyDescriptorDefault(var2, new Integer(3));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("AsyncSessionQueueTimeout")) {
         var3 = "getAsyncSessionQueueTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAsyncSessionQueueTimeout";
         }

         var2 = new PropertyDescriptor("AsyncSessionQueueTimeout", ClusterMBean.class, var3, var4);
         var1.put("AsyncSessionQueueTimeout", var2);
         var2.setValue("description", "<p>Interval in seconds until the producer thread will wait for the AsyncSessionQueue to become unblocked.  Should be similar to the RequestTimeOut as that will determine the longest that the queue should remain full.<p> ");
         setPropertyDescriptorDefault(var2, new Integer(30));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("AutoMigrationTableName")) {
         var3 = "getAutoMigrationTableName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAutoMigrationTableName";
         }

         var2 = new PropertyDescriptor("AutoMigrationTableName", ClusterMBean.class, var3, var4);
         var1.put("AutoMigrationTableName", var2);
         var2.setValue("description", "Return the name of the table to be used for server migration. ");
         setPropertyDescriptorDefault(var2, "ACTIVE");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      String[] var5;
      if (!var1.containsKey("CandidateMachinesForMigratableServers")) {
         var3 = "getCandidateMachinesForMigratableServers";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCandidateMachinesForMigratableServers";
         }

         var2 = new PropertyDescriptor("CandidateMachinesForMigratableServers", ClusterMBean.class, var3, var4);
         var1.put("CandidateMachinesForMigratableServers", var2);
         var2.setValue("description", "<p>The set of machines (and order of preference) on which Node Manager will restart failed servers. (Requires you to enable each server for automatic migration.)</p>  <p>Each server can specify a subset of these cluster-wide candidates, which limits the machines on which the server can be restarted. Servers can also specify their own order of preference.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("ServerMBean#getCandidateMachinesForMigratableServers")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ClusterAddress")) {
         var3 = "getClusterAddress";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClusterAddress";
         }

         var2 = new PropertyDescriptor("ClusterAddress", ClusterMBean.class, var3, var4);
         var1.put("ClusterAddress", var2);
         var2.setValue("description", "<p>The address that forms a portion of the URL a client uses to connect to this cluster, and that is used for generating EJB handles and entity EJB failover addresses. (This address may be either a DNS host name that maps to multiple IP addresses or a comma-separated list of single address host names or IP addresses.)</p>  <p>Defines the address to be used by clients to connect to this cluster. This address may be either a DNS host name that maps to multiple IP addresses or a comma separated list of single address host names or IP addresses. If network channels are configured, it is possible to set the cluster address on a per channel basis.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.NetworkChannelMBean#getClusterAddress")};
         var2.setValue("see", var5);
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ClusterBroadcastChannel")) {
         var3 = "getClusterBroadcastChannel";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClusterBroadcastChannel";
         }

         var2 = new PropertyDescriptor("ClusterBroadcastChannel", ClusterMBean.class, var3, var4);
         var1.put("ClusterBroadcastChannel", var2);
         var2.setValue("description", "<p>Specifies the channel used to handle communications within a cluster. If no channel is specified the default channel is used.</p> <p>ClusterBroadcastChannel is only are supported if the unicast messaging type is used. ");
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("ClusterMessagingMode")) {
         var3 = "getClusterMessagingMode";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClusterMessagingMode";
         }

         var2 = new PropertyDescriptor("ClusterMessagingMode", ClusterMBean.class, var3, var4);
         var1.put("ClusterMessagingMode", var2);
         var2.setValue("description", "<p>Specifies the messaging type used in the cluster.</p> <p>Multicast messaging, the default, is provided for backwards compatibility.</p> <p>Unicast message is recommended for new clusters.</p> ");
         setPropertyDescriptorDefault(var2, "multicast");
         var2.setValue("legalValues", new Object[]{"multicast", "unicast"});
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ClusterType")) {
         var3 = "getClusterType";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClusterType";
         }

         var2 = new PropertyDescriptor("ClusterType", ClusterMBean.class, var3, var4);
         var1.put("ClusterType", var2);
         var2.setValue("description", "<p>Optimizes cross-cluster replication for the type of network that servers in the clusters use for administrative communication.</p>  <p>To enhance the reliability of HTTP sessions, you can configure servers in one cluster to replicate the session data to servers in a different cluster. In such an environment, configure the clusters to be one of the following types:</p>  <ul> <li><code>man</code> <p>If the clustered servers can send their data through a metro area network (man) in which latency is negligible. With this ClusterType value, servers replicate session state synchronously and in memory only. For example, when serverA in cluster1 starts an HTTP session, its backup server, serverB in cluster2, immediately replicates this session in memory to Server B.</p> </li>  <li><code>wan</code> <p>If the clusters are far apart or send their data through a wide area network (wan) that experiences significant network latency. With this ClusterType value, a server replicates session state synchronously to the backup server in the same cluster and asynchronously to a server in the remote cluster. For example, when serverA in cluster1 starts an HTTP session, it sends the data to serverB in cluster1 and then asynchronously sends data to serverX in cluster 2. ServerX will persist the session state in the database.</p>  <p>If you persist session data in a replicating database, and if you prefer to use the database to replicate the data instead of WebLogic Server, choose a cluster type of <code>wan</code> and leave the remote cluster address undefined. WebLogic Server saves the session data to the local database and assumes that the database replicates data as needed.</p> </li> </ul> ");
         setPropertyDescriptorDefault(var2, "none");
         var2.setValue("legalValues", new Object[]{"none", "wan", "man"});
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ConsensusParticipants")) {
         var3 = "getConsensusParticipants";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConsensusParticipants";
         }

         var2 = new PropertyDescriptor("ConsensusParticipants", ClusterMBean.class, var3, var4);
         var1.put("ConsensusParticipants", var2);
         var2.setValue("description", "Controls the number of cluster participants in determining consensus. ");
         var2.setValue("legalMax", new Integer(65536));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("DataSourceForAutomaticMigration")) {
         var3 = "getDataSourceForAutomaticMigration";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDataSourceForAutomaticMigration";
         }

         var2 = new PropertyDescriptor("DataSourceForAutomaticMigration", ClusterMBean.class, var3, var4);
         var1.put("DataSourceForAutomaticMigration", var2);
         var2.setValue("description", "<p>The data source used by servers in the cluster during migration. (You must configure each Migratable Server within the cluster to use this data source.)</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("DataSourceForJobScheduler")) {
         var3 = "getDataSourceForJobScheduler";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDataSourceForJobScheduler";
         }

         var2 = new PropertyDescriptor("DataSourceForJobScheduler", ClusterMBean.class, var3, var4);
         var1.put("DataSourceForJobScheduler", var2);
         var2.setValue("description", "<p>Data source required to support persistence of jobs scheduled with the job scheduler</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("DataSourceForSessionPersistence")) {
         var3 = "getDataSourceForSessionPersistence";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDataSourceForSessionPersistence";
         }

         var2 = new PropertyDescriptor("DataSourceForSessionPersistence", ClusterMBean.class, var3, var4);
         var1.put("DataSourceForSessionPersistence", var2);
         var2.setValue("description", "<p>To support HTTP Session failover across data centers, a datasource is required to dump session state on disk.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("DatabaseLessLeasingBasis")) {
         var3 = "getDatabaseLessLeasingBasis";
         var4 = null;
         var2 = new PropertyDescriptor("DatabaseLessLeasingBasis", ClusterMBean.class, var3, var4);
         var1.put("DatabaseLessLeasingBasis", var2);
         var2.setValue("description", "Get attributes associated with database less leasing basis used for server migration and singleton services. ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("DeathDetectorHeartbeatPeriod")) {
         var3 = "getDeathDetectorHeartbeatPeriod";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDeathDetectorHeartbeatPeriod";
         }

         var2 = new PropertyDescriptor("DeathDetectorHeartbeatPeriod", ClusterMBean.class, var3, var4);
         var1.put("DeathDetectorHeartbeatPeriod", var2);
         var2.setValue("description", "Gets the DeathDetectory HeartbeatPeriod value. The ClusterMaster sends a heartbeat every period seconds to ascertian the health of a member. Members monitor this heartbeat in order to ascertain the health of the server hosting the DeathDetector.  In this case, the ClusterMaster. ");
         setPropertyDescriptorDefault(var2, new Integer(1));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("DefaultLoadAlgorithm")) {
         var3 = "getDefaultLoadAlgorithm";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultLoadAlgorithm";
         }

         var2 = new PropertyDescriptor("DefaultLoadAlgorithm", ClusterMBean.class, var3, var4);
         var1.put("DefaultLoadAlgorithm", var2);
         var2.setValue("description", "<p>Defines the algorithm to be used for load-balancing between replicated services if none is specified for a particular service. The <tt>round-robin</tt> algorithm cycles through a list of WebLogic Server instances in order. <tt>Weight-based</tt> load balancing improves on the round-robin algorithm by taking into account a pre-assigned weight for each server. In <tt>random</tt> load balancing, requests are routed to servers at random.</p> ");
         setPropertyDescriptorDefault(var2, "round-robin");
         var2.setValue("legalValues", new Object[]{"round-robin", "weight-based", "random", "round-robin-affinity", "weight-based-affinity", "random-affinity"});
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("FencingGracePeriodMillis")) {
         var3 = "getFencingGracePeriodMillis";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFencingGracePeriodMillis";
         }

         var2 = new PropertyDescriptor("FencingGracePeriodMillis", ClusterMBean.class, var3, var4);
         var1.put("FencingGracePeriodMillis", var2);
         var2.setValue("description", "<p>During automatic migration, if the Cluster Master determines a server to be dead, it waits for this period of time (in milliseconds) before the Cluster Master migrates the service to another server in the cluster.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(30000));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("FrontendHTTPPort")) {
         var3 = "getFrontendHTTPPort";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFrontendHTTPPort";
         }

         var2 = new PropertyDescriptor("FrontendHTTPPort", ClusterMBean.class, var3, var4);
         var1.put("FrontendHTTPPort", var2);
         var2.setValue("description", "<p>The name of the HTTP port to which all redirected URLs will be sent.</p>  <p>Sets the FrontendHTTPPort for the default webserver (not virtual hosts) for all the servers in the cluster. Provides a method to ensure that the webapp will always have the correct PORT information, even when the request is coming through a firewall or a proxy. If this parameter is configured, the HOST header will be ignored and the information in this parameter will be used in its place, when constructing the absolute urls for redirects.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.WebServerMBean#getFrontendHTTPPort")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("FrontendHTTPSPort")) {
         var3 = "getFrontendHTTPSPort";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFrontendHTTPSPort";
         }

         var2 = new PropertyDescriptor("FrontendHTTPSPort", ClusterMBean.class, var3, var4);
         var1.put("FrontendHTTPSPort", var2);
         var2.setValue("description", "<p>The name of the secure HTTP port to which all redirected URLs will be sent.</p>  <p>Sets the FrontendHTTPSPort for the default webserver (not virtual hosts) for all the servers in the cluster. Provides a method to ensure that the webapp will always have the correct PORT information, even when the request is coming through a firewall or a proxy. If this parameter is configured, the HOST header will be ignored and the information in this parameter will be used in its place, when constructing the absolute urls for redirects.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.WebServerMBean#getFrontendHTTPSPort")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("FrontendHost")) {
         var3 = "getFrontendHost";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFrontendHost";
         }

         var2 = new PropertyDescriptor("FrontendHost", ClusterMBean.class, var3, var4);
         var1.put("FrontendHost", var2);
         var2.setValue("description", "<p>The name of the host to which all redirected URLs will be sent.</p>  <p>Sets the HTTP FrontendHost for the default webserver (not virtual hosts) for all the servers in the cluster. Provides a method to ensure that the webapp will always have the correct HOST information, even when the request is coming through a firewall or a proxy. If this parameter is configured, the HOST header will be ignored and the information in this parameter will be used in its place, when constructing the absolute urls for redirects.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.WebServerMBean#getFrontendHost")};
         var2.setValue("see", var5);
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("GreedySessionFlushInterval")) {
         var3 = "getGreedySessionFlushInterval";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setGreedySessionFlushInterval";
         }

         var2 = new PropertyDescriptor("GreedySessionFlushInterval", ClusterMBean.class, var3, var4);
         var1.put("GreedySessionFlushInterval", var2);
         var2.setValue("description", "<p>Interval in seconds until HTTP Sessions are periodically flushed to secondary server. ");
         setPropertyDescriptorDefault(var2, new Integer(3));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("HTTPPingRetryCount")) {
         var3 = "getHTTPPingRetryCount";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setHTTPPingRetryCount";
         }

         var2 = new PropertyDescriptor("HTTPPingRetryCount", ClusterMBean.class, var3, var4);
         var1.put("HTTPPingRetryCount", var2);
         var2.setValue("description", "Get the number of HTTP pings to execute before declaring a server unreachable. This comes into effect only when MaxServerCountForHTTPPing is > 0. ");
         setPropertyDescriptorDefault(var2, new Integer(3));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("HealthCheckIntervalMillis")) {
         var3 = "getHealthCheckIntervalMillis";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setHealthCheckIntervalMillis";
         }

         var2 = new PropertyDescriptor("HealthCheckIntervalMillis", ClusterMBean.class, var3, var4);
         var1.put("HealthCheckIntervalMillis", var2);
         var2.setValue("description", "<p>Interval in milliseconds at which Migratable Servers and Cluster Masters prove their liveness via the database.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(10000));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("HealthCheckPeriodsUntilFencing")) {
         var3 = "getHealthCheckPeriodsUntilFencing";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setHealthCheckPeriodsUntilFencing";
         }

         var2 = new PropertyDescriptor("HealthCheckPeriodsUntilFencing", ClusterMBean.class, var3, var4);
         var1.put("HealthCheckPeriodsUntilFencing", var2);
         var2.setValue("description", "<p>Maximum number of periods that a cluster member will wait before timing out a Cluster Master and also the maximum number of periods the Cluster Master will wait before timing out a Migratable Server.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(3));
         var2.setValue("legalMin", new Integer(2));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("IdlePeriodsUntilTimeout")) {
         var3 = "getIdlePeriodsUntilTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setIdlePeriodsUntilTimeout";
         }

         var2 = new PropertyDescriptor("IdlePeriodsUntilTimeout", ClusterMBean.class, var3, var4);
         var1.put("IdlePeriodsUntilTimeout", var2);
         var2.setValue("description", "<p>Maximum number of periods that a cluster member will wait before timing out a member of a cluster.</p>  <p>Maximum number of periods that a cluster member will wait before timing out a member of a cluster.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(3));
         var2.setValue("legalMin", new Integer(3));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("InterClusterCommLinkHealthCheckInterval")) {
         var3 = "getInterClusterCommLinkHealthCheckInterval";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setInterClusterCommLinkHealthCheckInterval";
         }

         var2 = new PropertyDescriptor("InterClusterCommLinkHealthCheckInterval", ClusterMBean.class, var3, var4);
         var1.put("InterClusterCommLinkHealthCheckInterval", var2);
         var2.setValue("description", "<p>If the cluster link between two clusters goes down, a trigger will run periodically to see if the link is restored. The duration is specified in milliseconds.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(30000));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("JobSchedulerTableName")) {
         var3 = "getJobSchedulerTableName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJobSchedulerTableName";
         }

         var2 = new PropertyDescriptor("JobSchedulerTableName", ClusterMBean.class, var3, var4);
         var1.put("JobSchedulerTableName", var2);
         var2.setValue("description", "<p>The table name to use for storing timers active with the job scheduler</p> ");
         setPropertyDescriptorDefault(var2, "weblogic_timers");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("MaxServerCountForHttpPing")) {
         var3 = "getMaxServerCountForHttpPing";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxServerCountForHttpPing";
         }

         var2 = new PropertyDescriptor("MaxServerCountForHttpPing", ClusterMBean.class, var3, var4);
         var1.put("MaxServerCountForHttpPing", var2);
         var2.setValue("description", "Get the maximum number of servers that can be pinged via HTTP when the local server has lost multicast heartbeats from remote members. By default the server is taken out of the cluster when 3 consecutive heartbeats are lost. With this value > 0, the server attempts to ping the remote server point-to-point before declaring it unreachable. The ping is considered successful only when the cluster is in a stable state which means that the servers have already exchanged annoucements and the data on multicast is primarily liveliness heartbeat. <p> NOTE: This mechanism is useful only as a substitute for multicast heartbeats. If subsystems rely on sending data over multicast then they will continue to fail. If an application relies on WebLogic features that use multicast for sending and receiving data over multicast, this option is not useful. It is useful for HTTP session replication based applications where replication updates are sent point-to-point and multicast is only used to determine liveliness. </p> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("MemberWarmupTimeoutSeconds")) {
         var3 = "getMemberWarmupTimeoutSeconds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMemberWarmupTimeoutSeconds";
         }

         var2 = new PropertyDescriptor("MemberWarmupTimeoutSeconds", ClusterMBean.class, var3, var4);
         var1.put("MemberWarmupTimeoutSeconds", var2);
         var2.setValue("description", "<p>Maximum number of seconds that a cluster member will wait to discover and synchronize with other servers in the cluster. Normally, the member will be able to sync in 30 seconds. If the value of this attribute is higher, that does not necessarily mean that it will take longer for the member to warmup. Instead it defines an upper bound on the time that a server will wait to sync with the servers that it has discovered. If the value is set 0, servers will not attempt to discover other running server in the cluster during server initialization</p> ");
         setPropertyDescriptorDefault(var2, new Integer(30));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("MigratableTargets")) {
         var3 = "getMigratableTargets";
         var4 = null;
         var2 = new PropertyDescriptor("MigratableTargets", ClusterMBean.class, var3, var4);
         var1.put("MigratableTargets", var2);
         var2.setValue("description", "<p>Returns all the MigratableTargets for this cluster</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MigrationBasis")) {
         var3 = "getMigrationBasis";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMigrationBasis";
         }

         var2 = new PropertyDescriptor("MigrationBasis", ClusterMBean.class, var3, var4);
         var1.put("MigrationBasis", var2);
         var2.setValue("description", "Controls the mechanism used for server and service migration. <ul> <li><b>Database</b> -- Requires the availability of a high-availability database, such as Oracle RAC, to store leasing information. <li><b>Consensus</b> -- Stores the leasing information in-memory within a cluster member. This option requires Node Manager to be configured and running. </ul> <p><b>Note:</b> Within a WebLogic Server installation, you can only use one type of leasing. Although it is possible to implement multiple features that use leasing within your environment, each must use the same kind of leasing. ");
         setPropertyDescriptorDefault(var2, "database");
         var2.setValue("legalValues", new Object[]{"database", "consensus"});
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("MillisToSleepBetweenAutoMigrationAttempts")) {
         var3 = "getMillisToSleepBetweenAutoMigrationAttempts";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMillisToSleepBetweenAutoMigrationAttempts";
         }

         var2 = new PropertyDescriptor("MillisToSleepBetweenAutoMigrationAttempts", ClusterMBean.class, var3, var4);
         var1.put("MillisToSleepBetweenAutoMigrationAttempts", var2);
         var2.setValue("description", "Controls how long of a pause there should be between the migration attempts described in getAdditionalAutoMigrationAttempts(). Note that this delay only happens when the server has failed to come up on every machine. ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getAdditionalAutoMigrationAttempts")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Long(180000L));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("MulticastAddress")) {
         var3 = "getMulticastAddress";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMulticastAddress";
         }

         var2 = new PropertyDescriptor("MulticastAddress", ClusterMBean.class, var3, var4);
         var1.put("MulticastAddress", var2);
         var2.setValue("description", "<p>The multicast address used by cluster members to communicate with each other.</p>  <p>The valid range is from from 224.0.0.0 to 239.255.255.255. The default value used by WebLogic Server is 239.192.0.0.  You should avoid using multicast addresses in the range x.0.0.1</p>  <p>This address should be unique to this cluster and should not be shared by other applications.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#getInterfaceAddress"), BeanInfoHelper.encodeEntities("#getMulticastPort")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, "239.192.0.0");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("MulticastBufferSize")) {
         var3 = "getMulticastBufferSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMulticastBufferSize";
         }

         var2 = new PropertyDescriptor("MulticastBufferSize", ClusterMBean.class, var3, var4);
         var1.put("MulticastBufferSize", var2);
         var2.setValue("description", "<p>The multicast socket send/receive buffer size (at least 64 kilobytes).</p>  <p>Returns the multicast socket send/receive buffer size.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(64));
         var2.setValue("legalMin", new Integer(64));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("MulticastDataEncryption")) {
         var3 = "getMulticastDataEncryption";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMulticastDataEncryption";
         }

         var2 = new PropertyDescriptor("MulticastDataEncryption", ClusterMBean.class, var3, var4);
         var1.put("MulticastDataEncryption", var2);
         var2.setValue("description", "Enables multicast data to be encrypted. Only the multicast data is encrypted. Multicast header information is not encrypted. ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (BeanInfoHelper.isVersionCompliant("7.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("MulticastPort")) {
         var3 = "getMulticastPort";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMulticastPort";
         }

         var2 = new PropertyDescriptor("MulticastPort", ClusterMBean.class, var3, var4);
         var1.put("MulticastPort", var2);
         var2.setValue("description", "<p>The multicast port (between 1 and 65535) used by cluster members to communicate with each other.</p>  <p>Defines the multicast port used by cluster members to communicate with each other.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#setMulticastPort"), BeanInfoHelper.encodeEntities("#getMulticastAddress")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(7001));
         var2.setValue("legalMax", new Integer(65535));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("since", "7.0.0.0");
      }

      if (!var1.containsKey("MulticastSendDelay")) {
         var3 = "getMulticastSendDelay";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMulticastSendDelay";
         }

         var2 = new PropertyDescriptor("MulticastSendDelay", ClusterMBean.class, var3, var4);
         var1.put("MulticastSendDelay", var2);
         var2.setValue("description", "<p>The amount of time (between 0 and 250 milliseconds) to delay sending message fragments over multicast in order to avoid OS-level buffer overflow.</p>  <p>Defines the number of milliseconds to delay sending message fragments over multicast in order to avoid OS-level buffer overflow.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(3));
         var2.setValue("legalMax", new Integer(250));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("MulticastTTL")) {
         var3 = "getMulticastTTL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMulticastTTL";
         }

         var2 = new PropertyDescriptor("MulticastTTL", ClusterMBean.class, var3, var4);
         var1.put("MulticastTTL", var2);
         var2.setValue("description", "<p>The number of network hops (between 1 and 255) that a cluster multicast message is allowed to travel.</p>  <p>Defines the number of network hops that a cluster multicast message is allowed to travel. 1 restricts the cluster to one subnet.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(1));
         var2.setValue("legalMax", new Integer(255));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", ClusterMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("NumberOfServersInClusterAddress")) {
         var3 = "getNumberOfServersInClusterAddress";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNumberOfServersInClusterAddress";
         }

         var2 = new PropertyDescriptor("NumberOfServersInClusterAddress", ClusterMBean.class, var3, var4);
         var1.put("NumberOfServersInClusterAddress", var2);
         var2.setValue("description", "Number of servers to be listed from this cluster when generating a cluster address automatically. This setting has no effect if Cluster Address is explicitly set. ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getClusterAddress")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(3));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("OverloadProtection")) {
         var3 = "getOverloadProtection";
         var4 = null;
         var2 = new PropertyDescriptor("OverloadProtection", ClusterMBean.class, var3, var4);
         var1.put("OverloadProtection", var2);
         var2.setValue("description", "Get attributes related to server overload protection. The default values for all cluster members are set here. Individual servers can override them as needed. ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("PersistSessionsOnShutdown")) {
         var3 = "getPersistSessionsOnShutdown";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPersistSessionsOnShutdown";
         }

         var2 = new PropertyDescriptor("PersistSessionsOnShutdown", ClusterMBean.class, var3, var4);
         var1.put("PersistSessionsOnShutdown", var2);
         var2.setValue("description", "<p>When shutting down servers, sessions are not updated. If the primary and secondary servers of a session are shut down with no session updates, the session will be lost. Turning on PersistSessionsOnShutdown will save any active sessions to the database specified in {@link ClusterMBean#getDataSourceForSessionPersistence()} when a server is shutdown. The sessions will not be written at any other time. (For example, they are not saved via this mechanism if there is a server crash.)</p>  <p>This attribute is applicable both to session persistence on server shutdown or session persistence across a WAN.</p>  <p>Rolling upgrade can potentially have a bad interaction with traditional in-memory session replication.  As managed servers are shutdown and upgraded, in-memory servlet sessions will be lost if both primary and secondary are rebooted before a new request arrives for the session.</p> ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("RemoteClusterAddress")) {
         var3 = "getRemoteClusterAddress";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRemoteClusterAddress";
         }

         var2 = new PropertyDescriptor("RemoteClusterAddress", ClusterMBean.class, var3, var4);
         var1.put("RemoteClusterAddress", var2);
         var2.setValue("description", "<p>Set the foreign cluster. Cluster infrastructure uses this address to connect to foreign cluster for HTTP Session WAN/MAN failover.</p> ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ReplicationChannel")) {
         var3 = "getReplicationChannel";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setReplicationChannel";
         }

         var2 = new PropertyDescriptor("ReplicationChannel", ClusterMBean.class, var3, var4);
         var1.put("ReplicationChannel", var2);
         var2.setValue("description", "<p>The channel name to be used for replication traffic. Cluster infrastructure uses this channel to send updates for HTTP sessions and stateful session beans. If none is set then the default channel will be used.</p>  <p>In order for this feature to work, the named channel must exist on all members of the cluster and must be configured to use the same protocol. It is valid for the selected channel to be configured to use a secure protocol.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getRemoteClusterAddress")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, "ReplicationChannel");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Servers")) {
         var3 = "getServers";
         var4 = null;
         var2 = new PropertyDescriptor("Servers", ClusterMBean.class, var3, var4);
         var1.put("Servers", var2);
         var2.setValue("description", "<p>The servers which have declared membership in this cluster.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("ServiceAgeThresholdSeconds")) {
         var3 = "getServiceAgeThresholdSeconds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setServiceAgeThresholdSeconds";
         }

         var2 = new PropertyDescriptor("ServiceAgeThresholdSeconds", ClusterMBean.class, var3, var4);
         var1.put("ServiceAgeThresholdSeconds", var2);
         var2.setValue("description", "<p>The number of seconds (between 0 and 65534) by which the age of two conflicting services must differ before one is considered older than the other.</p>  <p>Defines the number of seconds by which the age of two conflicting services must differ before one is considered older than the other.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(180));
         var2.setValue("legalMax", new Integer(65534));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SessionFlushInterval")) {
         var3 = "getSessionFlushInterval";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSessionFlushInterval";
         }

         var2 = new PropertyDescriptor("SessionFlushInterval", ClusterMBean.class, var3, var4);
         var1.put("SessionFlushInterval", var2);
         var2.setValue("description", "<p>Interval in seconds until HTTP Sessions are periodically flushed to the backup cluster to dump session state on disk.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(180));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SessionFlushThreshold")) {
         var3 = "getSessionFlushThreshold";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSessionFlushThreshold";
         }

         var2 = new PropertyDescriptor("SessionFlushThreshold", ClusterMBean.class, var3, var4);
         var1.put("SessionFlushThreshold", var2);
         var2.setValue("description", "<p>When number of sessions to be flushed reaches this threshold limit, sessions will be flushed to the backup cluster before the flush interval. This helps the server to flush sessions faster under load.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(10000));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SingletonSQLQueryHelper")) {
         var3 = "getSingletonSQLQueryHelper";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSingletonSQLQueryHelper";
         }

         var2 = new PropertyDescriptor("SingletonSQLQueryHelper", ClusterMBean.class, var3, var4);
         var1.put("SingletonSQLQueryHelper", var2);
         var2.setValue("description", "<p>Singleton Services uses certain SQL commands to talk to the database. By default, the commands are obtained from a WebLogic-supplied implementation of weblogic.cluster.singleton.QueryHelper. If the database is not suported, or the SQL needs to be more optimized or altered for a particular use case, one can change the class used by setting this variable. The classname provided will be loaded at boot time, and used to execute the various SQL queries.</p> ");
         setPropertyDescriptorDefault(var2, "");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("UnicastDiscoveryPeriodMillis")) {
         var3 = "getUnicastDiscoveryPeriodMillis";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUnicastDiscoveryPeriodMillis";
         }

         var2 = new PropertyDescriptor("UnicastDiscoveryPeriodMillis", ClusterMBean.class, var3, var4);
         var1.put("UnicastDiscoveryPeriodMillis", var2);
         var2.setValue("description", "<p>The timer period that determines how  often other members in the cluster are discovered in unicast messaging scheme. This is not applicable to multicast mode. It applies only to unicast mode.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(3000));
         var2.setValue("legalMin", new Integer(1000));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("WANSessionPersistenceTableName")) {
         var3 = "getWANSessionPersistenceTableName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWANSessionPersistenceTableName";
         }

         var2 = new PropertyDescriptor("WANSessionPersistenceTableName", ClusterMBean.class, var3, var4);
         var1.put("WANSessionPersistenceTableName", var2);
         var2.setValue("description", "Return the name of the table to be used for WAN session persistence. ");
         setPropertyDescriptorDefault(var2, "WLS_WAN_PERSISTENCE_TABLE");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ClientCertProxyEnabled")) {
         var3 = "isClientCertProxyEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClientCertProxyEnabled";
         }

         var2 = new PropertyDescriptor("ClientCertProxyEnabled", ClusterMBean.class, var3, var4);
         var1.put("ClientCertProxyEnabled", var2);
         var2.setValue("description", "<p>Specifies whether to honor the WL-Proxy-Client-Cert header coming with the request or not. </p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.WebAppContainerMBean#isClientCertProxyEnabled()"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#isClientCertProxyEnabled()")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("HttpTraceSupportEnabled")) {
         var3 = "isHttpTraceSupportEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setHttpTraceSupportEnabled";
         }

         var2 = new PropertyDescriptor("HttpTraceSupportEnabled", ClusterMBean.class, var3, var4);
         var1.put("HttpTraceSupportEnabled", var2);
         var2.setValue("description", "<p> Returns the value of HttpTraceSupportEnabled. </p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.WebAppContainerMBean#isHttpTraceSupportEnabled"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#isHttpTraceSupportEnabled")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, (String)null, this.targetVersion) && !var1.containsKey("MemberDeathDetectorEnabled")) {
         var3 = "isMemberDeathDetectorEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMemberDeathDetectorEnabled";
         }

         var2 = new PropertyDescriptor("MemberDeathDetectorEnabled", ClusterMBean.class, var3, var4);
         var1.put("MemberDeathDetectorEnabled", var2);
         var2.setValue("description", "Enables faster Automatic Service Migration times with Database Leasing Basis. ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("since", "true");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.3.0", (String)null, this.targetVersion) && !var1.containsKey("MessageOrderingEnabled")) {
         var3 = "isMessageOrderingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMessageOrderingEnabled";
         }

         var2 = new PropertyDescriptor("MessageOrderingEnabled", ClusterMBean.class, var3, var4);
         var1.put("MessageOrderingEnabled", var2);
         var2.setValue("description", "<p>Forces unicast messages to be processed in order. There are scenarios where JMS may update JNDI very frequently. It will result in a lot of messages over unicast. Due to the close proximity of messages the probability of out of order handling of messages increases which would trigger frequent state dumps. Frequent JNDI tree refresh may result in NameNotFoundException. Use this property to prevent out of order handling of messages.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("since", "10.3.3.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.4.0", (String)null, this.targetVersion) && !var1.containsKey("OneWayRmiForReplicationEnabled")) {
         var3 = "isOneWayRmiForReplicationEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOneWayRmiForReplicationEnabled";
         }

         var2 = new PropertyDescriptor("OneWayRmiForReplicationEnabled", ClusterMBean.class, var3, var4);
         var1.put("OneWayRmiForReplicationEnabled", var2);
         var2.setValue("description", "Indicates if one-way RMI is being used for replication. One-way RMI also requires configuring replication ports on each server in the cluster. ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#getReplicationPorts()")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("since", "10.3.4.0");
      }

      if (!var1.containsKey("ReplicationTimeoutEnabled")) {
         var3 = "isReplicationTimeoutEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setReplicationTimeoutEnabled";
         }

         var2 = new PropertyDescriptor("ReplicationTimeoutEnabled", ClusterMBean.class, var3, var4);
         var1.put("ReplicationTimeoutEnabled", var2);
         var2.setValue("description", "Indicates if timeout should be applied to session replication calls. ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (BeanInfoHelper.isVersionCompliant("7.0.8.0", (String)null, this.targetVersion) && !var1.containsKey("SecureReplicationEnabled")) {
         var3 = "isSecureReplicationEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSecureReplicationEnabled";
         }

         var2 = new PropertyDescriptor("SecureReplicationEnabled", ClusterMBean.class, var3, var4);
         var1.put("SecureReplicationEnabled", var2);
         var2.setValue("description", "Servers in a cluster replicate session data. If a replication channel is defined then the session data will be sent using the replication channel protocol and secured replication settings will be ignored. If no replication channel is defined and secured replication is enabled then session data for in-memory replication will be sent over SSL using the default secured channel. However, this added security for replication traffic comes with a significant cluster performance degradation. It should only be enabled if security is of greater concern than performance degradation. ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("since", "7.0.8.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.4.0", (String)null, this.targetVersion) && !var1.containsKey("SessionLazyDeserializationEnabled")) {
         var3 = "isSessionLazyDeserializationEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSessionLazyDeserializationEnabled";
         }

         var2 = new PropertyDescriptor("SessionLazyDeserializationEnabled", ClusterMBean.class, var3, var4);
         var1.put("SessionLazyDeserializationEnabled", var2);
         var2.setValue("description", "Enables increased efficiency with session replication. Enabling this attribute should be used only when configuring a WebLogic domain for Oracle Exalogic. For more information, see \"Enabling Exalogic-Specific Enhancements in Oracle WebLogic Server 11g Release 1 (10.3.4)\" in the Oracle Exalogic Deployment Guide. ");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("since", "10.3.4.0");
      }

      if (!var1.containsKey("WeblogicPluginEnabled")) {
         var3 = "isWeblogicPluginEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWeblogicPluginEnabled";
         }

         var2 = new PropertyDescriptor("WeblogicPluginEnabled", ClusterMBean.class, var3, var4);
         var1.put("WeblogicPluginEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the proprietary <tt>WL-Proxy-Client-IP</tt> header should be used. (This needed only when WebLogic plugins are configured.)</p>  <p>Gets the weblogicPluginEnabled attribute of the ClusterMBean object</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.WebAppContainerMBean#isWeblogicPluginEnabled()"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#isWeblogicPluginEnabled()")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
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
      Method var3 = ClusterMBean.class.getMethod("freezeCurrentValue", String.class);
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

      var3 = ClusterMBean.class.getMethod("restoreDefaultValue", String.class);
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

      var3 = ClusterMBean.class.getMethod("start");
      String var6 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var6)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var6, var2);
         var2.setValue("description", "<p>Used to start all the servers belonging to the Cluster. HashMap contains references to TaskRuntimeMBeans corresponding to each server in the Cluster, keyed using the server name.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = ClusterMBean.class.getMethod("kill");
      var6 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var6)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var6, var2);
         var2.setValue("description", "<p>Used to force a Shutdown of all the servers belonging to the Cluster.</p> ");
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
