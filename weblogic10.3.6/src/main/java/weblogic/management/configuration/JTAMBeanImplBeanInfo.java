package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class JTAMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JTAMBean.class;

   public JTAMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public JTAMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = JTAMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This interface provides access to the JTA configuration attributes.  The methods defined herein are applicable for JTA configuration at the domain level.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.JTAMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("AbandonTimeoutSeconds")) {
         var3 = "getAbandonTimeoutSeconds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAbandonTimeoutSeconds";
         }

         var2 = new PropertyDescriptor("AbandonTimeoutSeconds", JTAMBean.class, var3, var4);
         var1.put("AbandonTimeoutSeconds", var2);
         var2.setValue("description", "<p>Specifies the maximum amount of time, in seconds, a transaction manager persists in attempting to complete the second phase of a two-phase commit transaction. </p>  <p>During the second phase of a two-phase commit transaction, the transaction manager continues to try to complete the transaction until all resource managers indicate that the transaction is completed. After the abandon transaction timer expires, no further attempt is made to resolve the transaction. If the transaction is in a prepared state before being abandoned, the transaction manager rolls back the transaction to release any locks held on behalf of the abandoned transaction.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(86400));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("BeforeCompletionIterationLimit")) {
         var3 = "getBeforeCompletionIterationLimit";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBeforeCompletionIterationLimit";
         }

         var2 = new PropertyDescriptor("BeforeCompletionIterationLimit", JTAMBean.class, var3, var4);
         var1.put("BeforeCompletionIterationLimit", var2);
         var2.setValue("description", "<p>The maximum number of cycles that the transaction manager performs the <tt>beforeCompletion</tt> synchronization callback for this WebLogic Server domain.</p>  <p>Nothing prevents a Synchronization object from registering another during <code>beforeCompletion</code>, even those whose <code>beforeCompletions</code> have already been called. For example, an EJB can call another in its <code>ejbStore()</code> method. To accommodate this, the transaction manager calls all Synchronization objects, then repeats the cycle if new ones have been registered. This count sets a limit to the number of cycles that synchronization occurs.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(10));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CheckpointIntervalSeconds")) {
         var3 = "getCheckpointIntervalSeconds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCheckpointIntervalSeconds";
         }

         var2 = new PropertyDescriptor("CheckpointIntervalSeconds", JTAMBean.class, var3, var4);
         var1.put("CheckpointIntervalSeconds", var2);
         var2.setValue("description", "<p>The interval at which the transaction manager creates a new transaction log file and checks all old transaction log files to see if they are ready to be deleted.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(300));
         var2.setValue("legalMax", new Integer(1800));
         var2.setValue("legalMin", new Integer(10));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CompletionTimeoutSeconds")) {
         var3 = "getCompletionTimeoutSeconds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCompletionTimeoutSeconds";
         }

         var2 = new PropertyDescriptor("CompletionTimeoutSeconds", JTAMBean.class, var3, var4);
         var1.put("CompletionTimeoutSeconds", var2);
         var2.setValue("description", "<p>Specifies the maximum amount of time, in seconds, a transaction manager waits for all resource managers to respond and indicate if the transaction can be committed or rolled back.</p>  <ul><li> The default value is 0, which sets the value to approximately twice the default <code>transaction-timeout</code> value with a maximum value of 120 seconds. This value provides backward compatibility for prior releases without this setting.</li> <li>If the specified value is -1, the maximum value supported by this attribute is used. </li> <li>If the specified value exceeds the value set for <code>abandon-timeout-seconds</code>, the value of <code>abandon-timeout-seconds</code> is used. </li> </ul> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ForgetHeuristics")) {
         var3 = "getForgetHeuristics";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setForgetHeuristics";
         }

         var2 = new PropertyDescriptor("ForgetHeuristics", JTAMBean.class, var3, var4);
         var1.put("ForgetHeuristics", var2);
         var2.setValue("description", "<p>Specifies whether the transaction manager automatically performs an XA Resource <code>forget</code> operation for heuristic transaction completions.</p>  <p>When enabled, the transaction manager automatically performs an XA Resource <code>forget()</code> operation for all resources as soon as the transaction learns of a heuristic outcome. Disable this feature only if you know what to do with the resource when it reports a heuristic decision.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxResourceRequestsOnServer")) {
         var3 = "getMaxResourceRequestsOnServer";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxResourceRequestsOnServer";
         }

         var2 = new PropertyDescriptor("MaxResourceRequestsOnServer", JTAMBean.class, var3, var4);
         var1.put("MaxResourceRequestsOnServer", var2);
         var2.setValue("description", "<p>Maximum number of concurrent requests to resources allowed for each server.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(50));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(10));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxResourceUnavailableMillis")) {
         var3 = "getMaxResourceUnavailableMillis";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxResourceUnavailableMillis";
         }

         var2 = new PropertyDescriptor("MaxResourceUnavailableMillis", JTAMBean.class, var3, var4);
         var1.put("MaxResourceUnavailableMillis", var2);
         var2.setValue("description", "<p>Maximum duration time, in milliseconds, that a resource is declared dead. After the duration, the resource is declared available again, even if the resource provider does not explicitly re-register the resource.</p> ");
         setPropertyDescriptorDefault(var2, new Long(1800000L));
         var2.setValue("legalMax", new Long(Long.MAX_VALUE));
         var2.setValue("legalMin", new Long(0L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxTransactions")) {
         var3 = "getMaxTransactions";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxTransactions";
         }

         var2 = new PropertyDescriptor("MaxTransactions", JTAMBean.class, var3, var4);
         var1.put("MaxTransactions", var2);
         var2.setValue("description", "<p>The maximum number of simultaneous in-progress transactions allowed on a server in this WebLogic Server domain.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(10000));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxTransactionsHealthIntervalMillis")) {
         var3 = "getMaxTransactionsHealthIntervalMillis";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxTransactionsHealthIntervalMillis";
         }

         var2 = new PropertyDescriptor("MaxTransactionsHealthIntervalMillis", JTAMBean.class, var3, var4);
         var1.put("MaxTransactionsHealthIntervalMillis", var2);
         var2.setValue("description", "<p>The interval for which the transaction map must be full for the JTA subsystem to declare its health as CRITICAL.</p> ");
         setPropertyDescriptorDefault(var2, new Long(60000L));
         var2.setValue("legalMax", new Long(Long.MAX_VALUE));
         var2.setValue("legalMin", new Long(1000L));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxUniqueNameStatistics")) {
         var3 = "getMaxUniqueNameStatistics";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxUniqueNameStatistics";
         }

         var2 = new PropertyDescriptor("MaxUniqueNameStatistics", JTAMBean.class, var3, var4);
         var1.put("MaxUniqueNameStatistics", var2);
         var2.setValue("description", "<p>The maximum number of unique transaction names for which statistics are maintained.</p>  <p>The first 1001 unique transaction names are maintained as their own transaction name and stored in each statistic. After the 1001st transaction name is reached, the transaction name is stored as <code>weblogic.transaction.statistics.namedOverflow</code>, and the transaction statistic is also merged and maintained in <code>weblogic.transaction.statistics.namedOverflow</code>.</p>  <p>A transaction name typically represents a category of business transactions, such as \"funds-transfer.\"</p> ");
         setPropertyDescriptorDefault(var2, new Integer(1000));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxXACallMillis")) {
         var3 = "getMaxXACallMillis";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxXACallMillis";
         }

         var2 = new PropertyDescriptor("MaxXACallMillis", JTAMBean.class, var3, var4);
         var1.put("MaxXACallMillis", var2);
         var2.setValue("description", "<p>Maximum allowed time duration, in milliseconds,  for XA calls to resources. If a particular XA call to a resource exceeds the limit, the resource is declared unavailable.</p> ");
         setPropertyDescriptorDefault(var2, new Long(120000L));
         var2.setValue("legalMax", new Long(Long.MAX_VALUE));
         var2.setValue("legalMin", new Long(0L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MigrationCheckpointIntervalSeconds")) {
         var3 = "getMigrationCheckpointIntervalSeconds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMigrationCheckpointIntervalSeconds";
         }

         var2 = new PropertyDescriptor("MigrationCheckpointIntervalSeconds", JTAMBean.class, var3, var4);
         var1.put("MigrationCheckpointIntervalSeconds", var2);
         var2.setValue("description", "<p>The time interval, in seconds,  that the checkpoint is done for the migrated transaction logs (TLOGs).</p> ");
         setPropertyDescriptorDefault(var2, new Integer(60));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("ParallelXADispatchPolicy")) {
         var3 = "getParallelXADispatchPolicy";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setParallelXADispatchPolicy";
         }

         var2 = new PropertyDescriptor("ParallelXADispatchPolicy", JTAMBean.class, var3, var4);
         var1.put("ParallelXADispatchPolicy", var2);
         var2.setValue("description", "<p>The dispatch policy to use when performing XA operations in parallel. By default the policy of the thread coordinating the transaction is used. Note that the named execute queue must be configured on the target server.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("ParallelXAEnabled")) {
         var3 = "getParallelXAEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setParallelXAEnabled";
         }

         var2 = new PropertyDescriptor("ParallelXAEnabled", JTAMBean.class, var3, var4);
         var1.put("ParallelXAEnabled", var2);
         var2.setValue("description", "<p>Indicates that XA calls are executed in parallel if there are available threads.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("PurgeResourceFromCheckpointIntervalSeconds")) {
         var3 = "getPurgeResourceFromCheckpointIntervalSeconds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPurgeResourceFromCheckpointIntervalSeconds";
         }

         var2 = new PropertyDescriptor("PurgeResourceFromCheckpointIntervalSeconds", JTAMBean.class, var3, var4);
         var1.put("PurgeResourceFromCheckpointIntervalSeconds", var2);
         var2.setValue("description", "<p>The interval that a particular resource must be accessed within for it to be included in the checkpoint record.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(86400));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("RecoveryThresholdMillis")) {
         var3 = "getRecoveryThresholdMillis";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRecoveryThresholdMillis";
         }

         var2 = new PropertyDescriptor("RecoveryThresholdMillis", JTAMBean.class, var3, var4);
         var1.put("RecoveryThresholdMillis", var2);
         var2.setValue("description", "<p>The interval that recovery is attempted until the resource becomes available.</p> ");
         setPropertyDescriptorDefault(var2, new Long(300000L));
         var2.setValue("legalMax", new Long(Long.MAX_VALUE));
         var2.setValue("legalMin", new Long(60000L));
         var2.setValue("deprecated", "7.0.0.0 Replaced with nothing. ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("SecurityInteropMode")) {
         var3 = "getSecurityInteropMode";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSecurityInteropMode";
         }

         var2 = new PropertyDescriptor("SecurityInteropMode", JTAMBean.class, var3, var4);
         var1.put("SecurityInteropMode", var2);
         var2.setValue("description", "<p>Specifies the security mode of the communication channel used for XA calls between servers that participate in a global transaction. All server instances in a domain must have the same security mode setting.</p>  <p>Security Interoperability Mode options: <ul> <li><b>default</b>  The transaction coordinator makes calls using the kernel identity over an admin channel if it is enabled, and <code>anonymous</code> otherwise. Man-in-the-middle attacks are possible if the admin channel is not enabled.</li>  <li><b>performance</b>  The transaction coordinator makes calls using <code>anonymous</code> at all times. This implies a security risk since a malicious third party could then try to affect the outcome of transactions using a man-in-the-middle attack.</li>  <li><b>compatibility</b>  The transaction coordinator makes calls as the kernel identity over an insecure channel. This is a high security risk because a successful man-in-the-middle attack would allow the attacker to gain administrative control over both domains. This setting should only be used when strong network security is in place. </li> </ul></p> ");
         setPropertyDescriptorDefault(var2, "default");
         var2.setValue("legalValues", new Object[]{"default", "performance", "compatibility"});
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("SerializeEnlistmentsGCIntervalMillis")) {
         var3 = "getSerializeEnlistmentsGCIntervalMillis";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSerializeEnlistmentsGCIntervalMillis";
         }

         var2 = new PropertyDescriptor("SerializeEnlistmentsGCIntervalMillis", JTAMBean.class, var3, var4);
         var1.put("SerializeEnlistmentsGCIntervalMillis", var2);
         var2.setValue("description", "<p>The time interval, in milliseconds, at which internal objects used to serialize resource enlistment are cleaned up.</p> ");
         setPropertyDescriptorDefault(var2, new Long(30000L));
         var2.setValue("legalMin", new Long(0L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TimeoutSeconds")) {
         var3 = "getTimeoutSeconds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTimeoutSeconds";
         }

         var2 = new PropertyDescriptor("TimeoutSeconds", JTAMBean.class, var3, var4);
         var1.put("TimeoutSeconds", var2);
         var2.setValue("description", "<p>Specifies the maximum amount of time, in seconds, an active transaction is allowed to be in the first phase of a two-phase commit transaction. If the specified amount of time expires, the transaction is automatically rolled back.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(30));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("UnregisterResourceGracePeriod")) {
         var3 = "getUnregisterResourceGracePeriod";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUnregisterResourceGracePeriod";
         }

         var2 = new PropertyDescriptor("UnregisterResourceGracePeriod", JTAMBean.class, var3, var4);
         var1.put("UnregisterResourceGracePeriod", var2);
         var2.setValue("description", "<p>The amount of time, in seconds, a  transaction manager waits for transactions involving the resource to complete before unregistering a resource. This grace period helps minimize the risk of abandoned transactions because of an unregistered resource, such as a JDBC data source module packaged with an application.</p>  <p>During the specified grace period, the <code>unregisterResource</code> call blocks until the call returns and no new transactions are started for the associated resource. If the number of outstanding transactions for the resource goes to <code>0</code>, the <code>unregisterResource</code> call returns immediately.</p>  <p>At the end of the grace period, if outstanding transactions are associated with the resource, the <code>unregisterResource</code> call returns and a log message is written to the server on which the resource was previously registered. </p> ");
         setPropertyDescriptorDefault(var2, new Integer(30));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("WSATTransportSecurityMode")) {
         var3 = "getWSATTransportSecurityMode";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWSATTransportSecurityMode";
         }

         var2 = new PropertyDescriptor("WSATTransportSecurityMode", JTAMBean.class, var3, var4);
         var1.put("WSATTransportSecurityMode", var2);
         var2.setValue("description", "<p> Specifies transport security mode required by WebService Transaction endpoints.</p> <p>Transport Security options: <ul> <li><b>SSLNotRequired</b> All WebService Transaction protocol messages are exchanged over the HTTP channel.</li>  <li><b>SSLRequired</b> All WebService Transaction protocol messages are and can only be exchanged over the HTTPS.</li>  <li><b>ClientCertRequired</b> All WebService Transaction protocol messages are and can only be exchanged over the HTTPS, and WLS enforces the presence of client certificate </li> </ul></p> ");
         setPropertyDescriptorDefault(var2, "SSLNotRequired");
         var2.setValue("legalValues", new Object[]{"SSLNotRequired", "SSLRequired", "ClientCertRequired"});
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("TwoPhaseEnabled")) {
         var3 = "isTwoPhaseEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTwoPhaseEnabled";
         }

         var2 = new PropertyDescriptor("TwoPhaseEnabled", JTAMBean.class, var3, var4);
         var1.put("TwoPhaseEnabled", var2);
         var2.setValue("description", "<p>Indicates that the two-phase commit protocol is used to coordinate transactions across two or more resource managers.</p> <p> If not selected: <ul> <li> Two-phase commit is disabled and any attempt to use two-phase commit results in a RollbackException being thrown.</li> <li> All transaction logging is disabled, including checkpoint records.</li> </ul></p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
      }

      if (!var1.containsKey("WSATIssuedTokenEnabled")) {
         var3 = "isWSATIssuedTokenEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWSATIssuedTokenEnabled";
         }

         var2 = new PropertyDescriptor("WSATIssuedTokenEnabled", JTAMBean.class, var3, var4);
         var1.put("WSATIssuedTokenEnabled", var2);
         var2.setValue("description", "<p>Specifies whether to use <code>issuedtoken</code> to enable authentication between the WS-AT coordinator and participant.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
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
