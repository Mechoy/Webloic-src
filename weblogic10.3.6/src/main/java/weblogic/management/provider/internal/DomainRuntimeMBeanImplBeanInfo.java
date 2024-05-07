package weblogic.management.provider.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.DomainRuntimeMBean;

public class DomainRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = DomainRuntimeMBean.class;

   public DomainRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public DomainRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = DomainRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.provider.internal");
      String var3 = (new String("<p>This class is used for monitoring a WebLogic domain. A domain may contain zero or more clusters. A cluster may be looked up by a logical name.</p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.DomainRuntimeMBean");
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
         var2 = new PropertyDescriptor("ActivationTime", DomainRuntimeMBean.class, var3, var4);
         var1.put("ActivationTime", var2);
         var2.setValue("description", "<p>The time when the domain became active.</p> ");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("AppRuntimeStateRuntime")) {
         var3 = "getAppRuntimeStateRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("AppRuntimeStateRuntime", DomainRuntimeMBean.class, var3, var4);
         var1.put("AppRuntimeStateRuntime", var2);
         var2.setValue("description", "Returns a service from which it is possible to determine the state applications throughout the domain. ");
         var2.setValue("relationship", "containment");
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.4.0", (String)null, this.targetVersion) && !var1.containsKey("CoherenceServerLifeCycleRuntimes")) {
         var3 = "getCoherenceServerLifeCycleRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("CoherenceServerLifeCycleRuntimes", DomainRuntimeMBean.class, var3, var4);
         var1.put("CoherenceServerLifeCycleRuntimes", var2);
         var2.setValue("description", "<p>The <code>CoherenceServerLifecycleRuntimeMBean</code> for all configured Coherence servers in the domain. ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "10.3.4.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion) && !var1.containsKey("ConsoleRuntime")) {
         var3 = "getConsoleRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("ConsoleRuntime", DomainRuntimeMBean.class, var3, var4);
         var1.put("ConsoleRuntime", var2);
         var2.setValue("description", "Return the MBean which provides access to console runtime services. ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "10.3.1.0");
      }

      if (!var1.containsKey("DeployerRuntime")) {
         var3 = "getDeployerRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("DeployerRuntime", DomainRuntimeMBean.class, var3, var4);
         var1.put("DeployerRuntime", var2);
         var2.setValue("description", "<p>Provides access to the service interface to the interface that is used to deploy new customer applications or modules into this domain.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("deprecated", "9.0.0.0 ");
      }

      if (!var1.containsKey("DeploymentManager")) {
         var3 = "getDeploymentManager";
         var4 = null;
         var2 = new PropertyDescriptor("DeploymentManager", DomainRuntimeMBean.class, var3, var4);
         var1.put("DeploymentManager", var2);
         var2.setValue("description", "<p>Provides access to the service interface to the interface that is used to deploy new customer applications or modules into this domain.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0", (String)null, this.targetVersion) && !var1.containsKey("LogRuntime")) {
         var3 = "getLogRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("LogRuntime", DomainRuntimeMBean.class, var3, var4);
         var1.put("LogRuntime", var2);
         var2.setValue("description", "<p>Return the MBean which provides access to the control interface for WLS server logging.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "9.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0", (String)null, this.targetVersion) && !var1.containsKey("MessageDrivenControlEJBRuntime")) {
         var3 = "getMessageDrivenControlEJBRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("MessageDrivenControlEJBRuntime", DomainRuntimeMBean.class, var3, var4);
         var1.put("MessageDrivenControlEJBRuntime", var2);
         var2.setValue("description", "<p>The MessageDrivenControlEJBRuntimeMBean for this server.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "9.0");
      }

      if (!var1.containsKey("MigratableServiceCoordinatorRuntime")) {
         var3 = "getMigratableServiceCoordinatorRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("MigratableServiceCoordinatorRuntime", DomainRuntimeMBean.class, var3, var4);
         var1.put("MigratableServiceCoordinatorRuntime", var2);
         var2.setValue("description", "Returns the service used for coordinating the migraiton of migratable services. ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("MigrationDataRuntimes")) {
         var3 = "getMigrationDataRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("MigrationDataRuntimes", DomainRuntimeMBean.class, var3, var4);
         var1.put("MigrationDataRuntimes", var2);
         var2.setValue("description", "Returns a history of server migrations. Each array element represents a past or an ongoing migration. ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("PolicySubjectManagerRuntime")) {
         var3 = "getPolicySubjectManagerRuntime";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPolicySubjectManagerRuntime";
         }

         var2 = new PropertyDescriptor("PolicySubjectManagerRuntime", DomainRuntimeMBean.class, var3, var4);
         var1.put("PolicySubjectManagerRuntime", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "reference");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0", (String)null, this.targetVersion) && !var1.containsKey("SNMPAgentRuntime")) {
         var3 = "getSNMPAgentRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("SNMPAgentRuntime", DomainRuntimeMBean.class, var3, var4);
         var1.put("SNMPAgentRuntime", var2);
         var2.setValue("description", "<p>Return the MBean which provides access to the monitoring statistics for WLS SNMP Agent.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "9.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("ServerLifeCycleRuntimes")) {
         var3 = "getServerLifeCycleRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("ServerLifeCycleRuntimes", DomainRuntimeMBean.class, var3, var4);
         var1.put("ServerLifeCycleRuntimes", var2);
         var2.setValue("description", "<p>The <code>ServerLifecycleRuntimeMBean</code> for all configured servers in the domain. ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("ServiceMigrationDataRuntimes")) {
         var3 = "getServiceMigrationDataRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("ServiceMigrationDataRuntimes", DomainRuntimeMBean.class, var3, var4);
         var1.put("ServiceMigrationDataRuntimes", var2);
         var2.setValue("description", "Returns all the service migrations done in the domain ");
         var2.setValue("relationship", "containment");
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      MethodDescriptor var2;
      Method var3;
      ParameterDescriptor[] var4;
      String var5;
      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainRuntimeMBean.class.getMethod("lookupServerLifeCycleRuntime", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "Returns the server life cycle run-time MBean for the specified server. ");
            var2.setValue("role", "finder");
            var2.setValue("property", "ServerLifeCycleRuntimes");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.4.0", (String)null, this.targetVersion)) {
         var3 = DomainRuntimeMBean.class.getMethod("lookupCoherenceServerLifeCycleRuntime", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "10.3.4.0");
            var1.put(var5, var2);
            var2.setValue("description", "Returns the Coherence server life cycle run-time MBean for the specified server. ");
            var2.setValue("role", "finder");
            var2.setValue("property", "CoherenceServerLifeCycleRuntimes");
            var2.setValue("since", "10.3.4.0");
         }
      }

   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = DomainRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainRuntimeMBean.class.getMethod("restartSystemResource", SystemResourceMBean.class);
         var4 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var4)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("since", "9.0.0.0");
            var1.put(var4, var2);
            var2.setValue("description", "Restarts a system resource on all nodes to which it is deployed. ");
            var2.setValue("role", "operation");
            String[] var5 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
            var2.setValue("rolesAllowed", var5);
            var2.setValue("since", "9.0.0.0");
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
