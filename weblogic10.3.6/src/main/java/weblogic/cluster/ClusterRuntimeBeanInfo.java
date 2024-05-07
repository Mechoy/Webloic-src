package weblogic.cluster;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.ClusterRuntimeMBean;
import weblogic.management.runtime.ReplicationRuntimeBeanInfo;

public class ClusterRuntimeBeanInfo extends ReplicationRuntimeBeanInfo {
   public static Class INTERFACE_CLASS = ClusterRuntimeMBean.class;

   public ClusterRuntimeBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ClusterRuntimeBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ClusterRuntime.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.cluster");
      String var3 = (new String("This class is used for monitoring a server's view of the members of a WebLogic cluster within a WebLogic domain.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.ClusterRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("ActiveSingletonServices")) {
         var3 = "getActiveSingletonServices";
         var4 = null;
         var2 = new PropertyDescriptor("ActiveSingletonServices", ClusterRuntimeMBean.class, var3, (String)var4);
         var1.put("ActiveSingletonServices", var2);
         var2.setValue("description", "Returns an array of the names of the Singleton Services that are active on this server. ");
      }

      if (!var1.containsKey("AliveServerCount")) {
         var3 = "getAliveServerCount";
         var4 = null;
         var2 = new PropertyDescriptor("AliveServerCount", ClusterRuntimeMBean.class, var3, (String)var4);
         var1.put("AliveServerCount", var2);
         var2.setValue("description", "<p>Provides the current total number of alive servers in this cluster.</p>  <p>Returns the current total number of alive servers in this cluster.</p> ");
      }

      if (!var1.containsKey("CurrentMachine")) {
         var3 = "getCurrentMachine";
         var4 = null;
         var2 = new PropertyDescriptor("CurrentMachine", ClusterRuntimeMBean.class, var3, (String)var4);
         var1.put("CurrentMachine", var2);
         var2.setValue("description", "<p>Provides the current MachineMBean of the server. In most cases this is the MachineMBean that the server is configured to run on. The only time when this will not be true is if auto-migration is enabled. This method will report the current host machine for the server, in that case. ");
      }

      String[] var5;
      if (!var1.containsKey("CurrentSecondaryServer")) {
         var3 = "getCurrentSecondaryServer";
         var4 = null;
         var2 = new PropertyDescriptor("CurrentSecondaryServer", ClusterRuntimeMBean.class, var3, (String)var4);
         var1.put("CurrentSecondaryServer", var2);
         var2.setValue("description", " ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("getSecondaryServerDetails")};
         var2.setValue("see", var5);
         var2.setValue("deprecated", "10.3.0.0. deprecated in favor of getSecondaryServerDetails ");
      }

      if (!var1.containsKey("DetailedSecondariesDistribution")) {
         var3 = "getDetailedSecondariesDistribution";
         var4 = null;
         var2 = new PropertyDescriptor("DetailedSecondariesDistribution", ClusterRuntimeMBean.class, var3, (String)var4);
         var1.put("DetailedSecondariesDistribution", var2);
         var2.setValue("description", "<p>Provides the names of the remote servers (such as myserver) for which the local server is hosting secondary objects. The name is appended with a number to indicate the number of secondaries hosted on behalf of that server.</p> ");
      }

      if (!var1.containsKey("ForeignFragmentsDroppedCount")) {
         var3 = "getForeignFragmentsDroppedCount";
         var4 = null;
         var2 = new PropertyDescriptor("ForeignFragmentsDroppedCount", ClusterRuntimeMBean.class, var3, (String)var4);
         var1.put("ForeignFragmentsDroppedCount", var2);
         var2.setValue("description", "<p>Provides the number of fragments that originated in foreign domains or clusters which use the same multicast address.</p>  <p>Answer the number of fragments that originated in foreign domains/cluster that use the same multicast address.</p> ");
      }

      if (!var1.containsKey("FragmentsReceivedCount")) {
         var3 = "getFragmentsReceivedCount";
         var4 = null;
         var2 = new PropertyDescriptor("FragmentsReceivedCount", ClusterRuntimeMBean.class, var3, (String)var4);
         var1.put("FragmentsReceivedCount", var2);
         var2.setValue("description", "<p>Provides the total number of messages received on this server from the cluster. This is applicable to both multicast and unicast message types.</p> ");
      }

      if (!var1.containsKey("FragmentsSentCount")) {
         var3 = "getFragmentsSentCount";
         var4 = null;
         var2 = new PropertyDescriptor("FragmentsSentCount", ClusterRuntimeMBean.class, var3, (String)var4);
         var1.put("FragmentsSentCount", var2);
         var2.setValue("description", "<p>Returns the total number of message fragments sent from this server into the cluster. This is applicable to both multicast and unicast message types.</p> ");
      }

      if (!var1.containsKey("HealthState")) {
         var3 = "getHealthState";
         var4 = null;
         var2 = new PropertyDescriptor("HealthState", ClusterRuntimeMBean.class, var3, (String)var4);
         var1.put("HealthState", var2);
         var2.setValue("description", "<p>Provides health information returned by the server self-health monitor service.</p> <ul> <li>HEALTH_OK = 0, Server service is healthy.</li>  <li>HEALTH_WARN = 1, Service could have problems in the future. Check the server logs and the corresponding RuntimeMBean for more details.</li>  <li>HEALTH_CRITICAL = 2, Something must be done now to prevent service failure. Check the server logs and the corresponding RuntimeMBean for more details.</li>  <li>HEALTH_FAILED = 3, Service has failed - must be restarted. Check the server logs and the corresponding RuntimeMBean for more details.</li>  <li>HEALTH_OVERLOADED = 4, Service is functioning normally but there is too much work in it. CRITICAL and OVERLOADED are different. A subsystem is in the critical state when a part of it is malfunctioning, for example, stuck threads. An overloaded state means that there is more work assigned to the service than the configured threshold. A service might refuse more work in this state.</li>  <li>LOW_MEMORY_REASON = \"server is low on memory\", Reason code that indicates that the server is low on memory. Administrators can configure low and high thresholds for memory usage. The server health changes to <code>OVERLOADED</code> with this reason code if the low threshold is reached. </li></ul> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.OverloadProtectionMBean")};
         var2.setValue("see", var5);
      }

      if (!var1.containsKey("JobSchedulerRuntime")) {
         var3 = "getJobSchedulerRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("JobSchedulerRuntime", ClusterRuntimeMBean.class, var3, (String)var4);
         var1.put("JobSchedulerRuntime", var2);
         var2.setValue("description", "Provides information about past jobs executed on this server. Jobs must have been submitted to the JobScheduler functionality and should have executed atleast once on this server. ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("MulticastMessagesLostCount")) {
         var3 = "getMulticastMessagesLostCount";
         var4 = null;
         var2 = new PropertyDescriptor("MulticastMessagesLostCount", ClusterRuntimeMBean.class, var3, (String)var4);
         var1.put("MulticastMessagesLostCount", var2);
         var2.setValue("description", "<p>Provides the total number of in-coming multicast messages that were lost according to this server.</p> ");
      }

      if (!var1.containsKey("PrimaryCount")) {
         var3 = "getPrimaryCount";
         var4 = null;
         var2 = new PropertyDescriptor("PrimaryCount", ClusterRuntimeMBean.class, var3, (String)var4);
         var1.put("PrimaryCount", var2);
         var2.setValue("description", "<p>Provides the number of object that the local server hosts as primaries.</p>  <p>Answer the number of object that the local server hosts as primaries.</p> ");
      }

      if (!var1.containsKey("ResendRequestsCount")) {
         var3 = "getResendRequestsCount";
         var4 = null;
         var2 = new PropertyDescriptor("ResendRequestsCount", ClusterRuntimeMBean.class, var3, (String)var4);
         var1.put("ResendRequestsCount", var2);
         var2.setValue("description", "<p>Provides the number of state-delta messages that had to be resent because a receiving server in the cluster missed a message.</p>  <p>Returns the number of state-delta messages that had to be resent because a receiving server in the cluster missed a message.</p> ");
      }

      if (!var1.containsKey("SecondaryCount")) {
         var3 = "getSecondaryCount";
         var4 = null;
         var2 = new PropertyDescriptor("SecondaryCount", ClusterRuntimeMBean.class, var3, (String)var4);
         var1.put("SecondaryCount", var2);
         var2.setValue("description", "<p>Answer the number of object that the local server hosts as secondaries.</p> ");
      }

      if (!var1.containsKey("SecondaryDistributionNames")) {
         var3 = "getSecondaryDistributionNames";
         var4 = null;
         var2 = new PropertyDescriptor("SecondaryDistributionNames", ClusterRuntimeMBean.class, var3, (String)var4);
         var1.put("SecondaryDistributionNames", var2);
         var2.setValue("description", "<p>Provides the names of the remote servers (such as myserver) for which the local server is hosting secondary objects. The name is appended with a number to indicate the number of secondaries hosted on behalf of that server.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("getDetailedSecondariesDistribution")};
         var2.setValue("see", var5);
         var2.setValue("deprecated", "10.3.0.0 deprecated in favor of getDetailedSecondariesDistribution ");
      }

      if (!var1.containsKey("SecondaryServerDetails")) {
         var3 = "getSecondaryServerDetails";
         var4 = null;
         var2 = new PropertyDescriptor("SecondaryServerDetails", ClusterRuntimeMBean.class, var3, (String)var4);
         var1.put("SecondaryServerDetails", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("ServerMigrationRuntime")) {
         var3 = "getServerMigrationRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("ServerMigrationRuntime", ClusterRuntimeMBean.class, var3, (String)var4);
         var1.put("ServerMigrationRuntime", var2);
         var2.setValue("description", "Provides information about server migrations performed by this server. If the current server is not responsible for migrations, it points to the server that is responsible for it. ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("ServerNames")) {
         var3 = "getServerNames";
         var4 = null;
         var2 = new PropertyDescriptor("ServerNames", ClusterRuntimeMBean.class, var3, (String)var4);
         var1.put("ServerNames", var2);
         var2.setValue("description", "<p>Provides the names of the servers in the cluster.</p> ");
      }

      if (!var1.containsKey("UnicastMessaging")) {
         var3 = "getUnicastMessaging";
         var4 = null;
         var2 = new PropertyDescriptor("UnicastMessaging", ClusterRuntimeMBean.class, var3, (String)var4);
         var1.put("UnicastMessaging", var2);
         var2.setValue("description", "Provides information about unicast messaging mode if enabled ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("UnreliableServers")) {
         var3 = "getUnreliableServers";
         var4 = null;
         var2 = new PropertyDescriptor("UnreliableServers", ClusterRuntimeMBean.class, var3, (String)var4);
         var1.put("UnreliableServers", var2);
         var2.setValue("description", "<p>Provides a HashMap of the servers that, from this server's perspective, have dropped out of the cluster at some point during this server's current lifetime. The key is the server name, and the value is an Integer of the number of times the server has disconnected.</p>  <p> This view may not be consistent with the views of other servers. Servers that have never disconnected will not be in the HashMap. The view is not maintained across server restarts. No distinction is made between the possible causes of the disconnect; proper shutdowns cause the disconnect count to go up just like a network outage or a crash would.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
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
      Method var3 = ClusterRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         MethodDescriptor var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
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
