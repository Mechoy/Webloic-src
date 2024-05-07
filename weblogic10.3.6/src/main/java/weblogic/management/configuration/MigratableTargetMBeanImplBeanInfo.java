package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class MigratableTargetMBeanImplBeanInfo extends SingletonServiceBaseMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = MigratableTargetMBean.class;

   public MigratableTargetMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public MigratableTargetMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = MigratableTargetMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "7.0.0.0");
      String[] var3 = new String[]{BeanInfoHelper.encodeEntities("TargetMBean"), BeanInfoHelper.encodeEntities("SingletonServiceBaseMBean")};
      var2.setValue("see", var3);
      var2.setValue("package", "weblogic.management.configuration");
      String var4 = (new String("A target that is suitable for services that shall be active on at most one server of a cluster at a time.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var4);
      var2.setValue("description", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.MigratableTargetMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("AllCandidateServers")) {
         var3 = "getAllCandidateServers";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAllCandidateServers";
         }

         var2 = new PropertyDescriptor("AllCandidateServers", MigratableTargetMBean.class, var3, var4);
         var1.put("AllCandidateServers", var2);
         var2.setValue("description", "<p>The list of servers that are candidates to host the migratable services deployed to this migratable target. If the constrainedCandidateServers list is empty, all servers in the cluster are returned. If the constrainedCandidateServers list is not empty, only those servers will be returned. The user-preferred server will be the first element in the list.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Cluster")) {
         var3 = "getCluster";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCluster";
         }

         var2 = new PropertyDescriptor("Cluster", MigratableTargetMBean.class, var3, var4);
         var1.put("Cluster", var2);
         var2.setValue("description", "<p>Returns the cluster this singleton service is associated with.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("ConstrainedCandidateServers")) {
         var3 = "getConstrainedCandidateServers";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConstrainedCandidateServers";
         }

         var2 = new PropertyDescriptor("ConstrainedCandidateServers", MigratableTargetMBean.class, var3, var4);
         var1.put("ConstrainedCandidateServers", var2);
         var2.setValue("description", "<p>The (user-restricted) list of servers that can host the migratable services deployed to this migratable target. The migratable service will not be allowed to migrate to a server that is not in the returned list of servers.</p>  <p>For example, this feature may be used to configure two servers that have access to a dual-ported ported disk. All servers in this list must be part of the cluster that is associated with the migratable target.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("remover", "removeConstrainedCandidateServer");
         var2.setValue("adder", "addConstrainedCandidateServer");
      }

      if (!var1.containsKey("HostingServer")) {
         var3 = "getHostingServer";
         var4 = null;
         var2 = new PropertyDescriptor("HostingServer", MigratableTargetMBean.class, var3, var4);
         var1.put("HostingServer", var2);
         var2.setValue("description", "<p>Returns the name of the server that currently hosts the singleton service.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MigrationPolicy")) {
         var3 = "getMigrationPolicy";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMigrationPolicy";
         }

         var2 = new PropertyDescriptor("MigrationPolicy", MigratableTargetMBean.class, var3, var4);
         var1.put("MigrationPolicy", var2);
         var2.setValue("description", "<p>Defines the type of migration policy to use for the services hosted by this migratable target. Valid options are:</p> <ul> <li><code>Manual Service Migration Only</code> Indicates that no automatic migration of services hosted by this migratable target will occur.</li> <li><code>Auto-Migrate Exactly-Once Services</code> Indicates that if at least one Managed Server in the candidate server list is running, the services hosted by this migratable target will be active somewhere in the cluster if servers should fail or are administratively shut down (either gracefully or forcibly). For example, it is a recommended best practice to use this policy when a migratable target hosts a path service, so if its preferred server fails or is shut down, the path service will automatically migrate to another candidate server, and so will always be active in the cluster. <p><b>Note</b> This value can lead to target grouping on a server member. For example, if you have five exactly-once migratable targets and only one Managed Server is started in the cluster, then all five targets will be activated on that server.</p></li> <li><code>Auto-Migrate Failure-Recovery Services</code> Indicates that the services hosted by this migratable target will only start if the migratable target's User Preferred Server (UPS) is started. If an administrator manually shuts down the UPS, either gracefully or forcibly, then a failure-recovery service will not migrate. However, if the UPS fails due to an internal error, then the service will be migrated to another candidate server. If such a candidate server is unavailable (due to either a manual shutdown or an internal failure), then the migration framework will first attempt to reactivate the service on its UPS server. If the UPS server is not available at that time, then the service will be migrated to another candidate server.</li> </ul> ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("#setUserPreferredServer")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, "manual");
         var2.setValue("legalValues", new Object[]{"manual", "exactly-once", "failure-recovery"});
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", MigratableTargetMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("NumberOfRestartAttempts")) {
         var3 = "getNumberOfRestartAttempts";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNumberOfRestartAttempts";
         }

         var2 = new PropertyDescriptor("NumberOfRestartAttempts", MigratableTargetMBean.class, var3, var4);
         var1.put("NumberOfRestartAttempts", var2);
         var2.setValue("description", "<p>Specifies how many restart attempts to make before migrating the failed service.</p>  <p>Note that these are consecutive attempts. If the value is set to 6, and the service restart fails 5 times before succeeding, but then fails again later, it will not instantly migrate. Each failure gets its own count of restart attempts.</p>  <p>A value of 0 is identical to setting {@link #getRestartOnFailure} to false. A value of -1 indicates the service should <i> never</i> be migrated; instead, it will be restarted until it either works or the server shuts down.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(6));
      }

      if (!var1.containsKey("PostScript")) {
         var3 = "getPostScript";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPostScript";
         }

         var2 = new PropertyDescriptor("PostScript", MigratableTargetMBean.class, var3, var4);
         var1.put("PostScript", var2);
         var2.setValue("description", "<p>Specifies the path to the post-migration script to run after a migratable target is fully deactivated. The script <i>must</i> be in the <code><i>MIDDLEWARE_HOME</i>/user_projects/domains/<i>mydomain</i>/bin/service_migration</code> directory.</p>  <p>After the migratable target is deactivated, if there is a script specified, <i>and</i> Node Manager is available, then the script will run. Specifying a script without an available Node Manager will result in an error upon migration.</p> ");
      }

      if (!var1.containsKey("PreScript")) {
         var3 = "getPreScript";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPreScript";
         }

         var2 = new PropertyDescriptor("PreScript", MigratableTargetMBean.class, var3, var4);
         var1.put("PreScript", var2);
         var2.setValue("description", "<p>Specifies the path to the pre-migration script to run before a migratable target is actually activated. The script <i>must</i> be in the <code><i>MIDDLEWARE_HOME</i>/user_projects/domains/<i>mydomain</i>/bin/service_migration</code> directory.</p>  <p>Before the migratable target is activated, if there is a script specified, <i>and</i> Node Manager is available, then the script will run. Specifying a script without an available Node Manager will result in an error upon migration.</p>  <p>If the script fails or cannot be found, migration will not proceed on the current server, and will be tried on the next suitable server. This could be the next server in the candidate server list, or in the cluster, if there is no candidate server list.</p> ");
      }

      if (!var1.containsKey("RestartOnFailure")) {
         var3 = "getRestartOnFailure";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRestartOnFailure";
         }

         var2 = new PropertyDescriptor("RestartOnFailure", MigratableTargetMBean.class, var3, var4);
         var1.put("RestartOnFailure", var2);
         var2.setValue("description", "<p>Specifies whether or not a failed service will first be deactivated and reactivated in place, instead of being migrated.</p>  <p>The number of restart attempts is controlled by {@link #getNumberOfRestartAttempts}. Once these restart attempts are exhausted, the service will migrate. A restarting migratable target will deactivate all services on it in order, then reactivate them all.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
      }

      if (!var1.containsKey("SecondsBetweenRestarts")) {
         var3 = "getSecondsBetweenRestarts";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSecondsBetweenRestarts";
         }

         var2 = new PropertyDescriptor("SecondsBetweenRestarts", MigratableTargetMBean.class, var3, var4);
         var1.put("SecondsBetweenRestarts", var2);
         var2.setValue("description", "<p>Specifies how many seconds to wait in between attempts to restart the failed service.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(30));
      }

      if (!var1.containsKey("UserPreferredServer")) {
         var3 = "getUserPreferredServer";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUserPreferredServer";
         }

         var2 = new PropertyDescriptor("UserPreferredServer", MigratableTargetMBean.class, var3, var4);
         var1.put("UserPreferredServer", var2);
         var2.setValue("description", "<p>Returns the server that the user prefers the singleton service to be active on.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("NonLocalPostAllowed")) {
         var3 = "isNonLocalPostAllowed";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNonLocalPostAllowed";
         }

         var2 = new PropertyDescriptor("NonLocalPostAllowed", MigratableTargetMBean.class, var3, var4);
         var1.put("NonLocalPostAllowed", var2);
         var2.setValue("description", "<p>Specifies whether or not the post-deactivation script is allowed to run on a different machine.</p>  <p>Normally, when auto migration occurs, the post-deactivation script will be run on the service's current location, and the pre-activation script on the service's new location. If the current location is unreachable for some reason, this value will be checked to see if it is safe to run it on the service's new machine.</p>  <p>This is useful if the post-deactivation script controls access to a networked resource and does not need any data from the current machine.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
      }

      if (!var1.containsKey("PostScriptFailureFatal")) {
         var3 = "isPostScriptFailureFatal";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPostScriptFailureFatal";
         }

         var2 = new PropertyDescriptor("PostScriptFailureFatal", MigratableTargetMBean.class, var3, var4);
         var1.put("PostScriptFailureFatal", var2);
         var2.setValue("description", "<p>Specifies whether or not a failure during execution of the post-deactivation script is fatal to the migration.</p>  <p>If it is fatal, the migratable target will <i>not</i> be automatically migrated until an administrator manually migrates it to a server, thus reactivating it.</p>  <p><b>Note:</b> Enabling this value will result in weakening the exactly-once guarantee. It is provided to prevent more dangerous data corruption if the post-deactivation script fails. Also if this value is enabled, then the script may be called more than once by the migration framework after the Migratable Target is deactivated or the server or machine hosting the Migratable Target crashed or is network partitioned. The script is expected not to return different exit values when invoked multiple times in such scenarios.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = MigratableTargetMBean.class.getMethod("addConstrainedCandidateServer", ServerMBean.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("constrainedCandidateServer", "The feature to be added to the ConstrainedCandidateServer attribute ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "collection");
         var2.setValue("property", "ConstrainedCandidateServers");
      }

      var3 = MigratableTargetMBean.class.getMethod("removeConstrainedCandidateServer", ServerMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("constrainedCandidateServer", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "collection");
         var2.setValue("property", "ConstrainedCandidateServers");
      }

   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = MigratableTargetMBean.class.getMethod("freezeCurrentValue", String.class);
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

      var3 = MigratableTargetMBean.class.getMethod("restoreDefaultValue", String.class);
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
