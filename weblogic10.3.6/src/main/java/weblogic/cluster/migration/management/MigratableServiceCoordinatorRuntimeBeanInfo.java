package weblogic.cluster.migration.management;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.SingletonServiceMBean;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.MigratableServiceCoordinatorRuntimeMBean;

public class MigratableServiceCoordinatorRuntimeBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = MigratableServiceCoordinatorRuntimeMBean.class;

   public MigratableServiceCoordinatorRuntimeBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public MigratableServiceCoordinatorRuntimeBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = MigratableServiceCoordinatorRuntime.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.cluster.migration.management");
      String var3 = (new String("<h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.MigratableServiceCoordinatorRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      Object var2 = null;
      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = MigratableServiceCoordinatorRuntimeMBean.class.getMethod("migrate", MigratableTargetMBean.class, ServerMBean.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("migratableTarget", "- all services targeted to this target are to be migrated to the destination server. THIS MUST BE A CONFIG MBEAN "), createParameterDescriptor("destination", "- the new server where the services deployed to migratableTarget shall be activated ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Migrates all services deployed to the migratableTarget to the destination server. This method assumes that the source and the destination server are up and running. Precondition: The migratableTarget must contain at least one server. The destination server must be a member of the migratableTarget's list of candidate servers. If automatic migration mode is disabled, the destination server must not be the currently hosting server (i.e. head of candidate list of the migratableTarget). Postcondition: If automatic migration mode is disabled and if the migration succeeded, the head of the candidate server list in the migratableTarget will be the destination server.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = MigratableServiceCoordinatorRuntimeMBean.class.getMethod("preDeregister");
      String var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var7, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = MigratableServiceCoordinatorRuntimeMBean.class.getMethod("migrateSingleton", SingletonServiceMBean.class, ServerMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("singletonService", "- SingletonService to be migrated to the destination server. THIS MUST BE A CONFIG MBEAN "), createParameterDescriptor("destination", "- the new server where the singleton service shall be activated ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Migrates the singleton service specified by the SingletonServiceMBean to the destination server. This method assumes that the source and the destination server are up and running. Precondition: The SingletonServiceMBean must contain at least one server. The destination server must be a member of the SingletonServiceMBean's list of candidate servers. If automatic migration mode is disabled, the destination server must not be the currently hosting server (i.e. head of candidate list of the migratableTarget). Postcondition: If automatic migration mode is disabled and if the migration succeeded, the head of the candidate server list in the migratableTarget will be the destination server.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = MigratableServiceCoordinatorRuntimeMBean.class.getMethod("migrate", MigratableTargetMBean.class, ServerMBean.class, Boolean.TYPE, Boolean.TYPE);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("migratableTarget", "- all services targeted to this target are to be                            migrated to the destination server.                            THIS MUST BE A CONFIG MBEAN "), createParameterDescriptor("destination", "- the new server where the services deployed to                       migratableTarget shall be activated "), createParameterDescriptor("sourceUp", "- the currently active server is up and running. If false,                    the administrator must ensure that the services deployed                    to migratableTarget are NOT active. "), createParameterDescriptor("destinationUp", "- the destination server is up and running. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Migrates all services deployed to the migratableTarget to the destination server. Use this method if either the source or the destination or both are not running. Precondition: The migratableTarget must contain at least one server. The destination server must be a member of the migratableTarget's list of candidate servers. If automatic migration mode is disabled, the destination server must not be the currently hosting server (i.e. head of candidate list of the migratableTarget). Postcondition: If automatic migration mode is disabled and if the migration succeeded, the head of the candidate server list in the migratableTarget will be the destination server.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = MigratableServiceCoordinatorRuntimeMBean.class.getMethod("migrateJTA", MigratableTargetMBean.class, ServerMBean.class, Boolean.TYPE, Boolean.TYPE);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("migratableTarget", (String)null), createParameterDescriptor("destination", (String)null), createParameterDescriptor("sourceUp", (String)null), createParameterDescriptor("destinationUp", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Migrates the JTARecoveryManager deployed to a migratableTarget to the destination server.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = MigratableServiceCoordinatorRuntimeMBean.class.getMethod("startMigrateTask", MigratableTargetMBean.class, ServerMBean.class, Boolean.TYPE);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("migratableTarget", (String)null), createParameterDescriptor("destination", (String)null), createParameterDescriptor("jta", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Starts the migration from the targeted server to the destination.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = MigratableServiceCoordinatorRuntimeMBean.class.getMethod("startMigrateTask", MigratableTargetMBean.class, ServerMBean.class, Boolean.TYPE, Boolean.TYPE, Boolean.TYPE);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("migratableTarget", (String)null), createParameterDescriptor("destination", (String)null), createParameterDescriptor("jta", (String)null), createParameterDescriptor("sourceDown", (String)null), createParameterDescriptor("destinationDown", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Starts the migration from the targeted server to the destination. If the targeted server is down, sourceDown should be set to true. If the destination server is down, destinationDown should be set to true.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = MigratableServiceCoordinatorRuntimeMBean.class.getMethod("deactivateJTATarget", MigratableTargetMBean.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("migratableTarget", (String)null), createParameterDescriptor("host", "Server that should host the service. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         String[] var6 = new String[]{BeanInfoHelper.encodeEntities("MigrationException")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "Each server in a cluster has a JTAMigratableTarget and this target is associated with transaction log. When the server hosting this target fails, this target can be migrated to another server in the cluster so that the all the pending transactions can be recovered. When the dead server becomes alive, the tlog has to be remigrated to the original server and it has to be guaranteed that only one server has the target active at an given point of time. Automatic JTAMigratable Target recovery steps:  We have two managed servers in the cluster s1 and s2 and an admin server AS. 1. All three servers are running. 2. MS1 crashes 3. Administrator migrates JTAMigratableTarget to MS2 4. AS records this information in its file store. 5. MS1 is restarted. 6. During boot process, MS1 checks on the AS if JTAMT has been migrated. 7. If the JTAMT is not migrated, AS just returns 8. If the JTAMT is migrated, then AS will deactivate the traget on the that is currently hosting the service. 9. AS records this information in its file store and returns and MS1 activates the JTAMT locally 10.If it fails to deactivate, then MS1 will not reboot. ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = MigratableServiceCoordinatorRuntimeMBean.class.getMethod("clearOldMigrationTaskRuntimes");
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var7, var2);
         var2.setValue("description", "Removes all MigrationTaskRuntimeMBeans that have completed and been around for over 30 minutes. ");
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
