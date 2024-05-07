package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class JTAMigratableTargetMBeanImplBeanInfo extends MigratableTargetMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JTAMigratableTargetMBean.class;

   public JTAMigratableTargetMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public JTAMigratableTargetMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = JTAMigratableTargetMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "7.0.0.0");
      String[] var3 = new String[]{BeanInfoHelper.encodeEntities("MigratableTargetMBean")};
      var2.setValue("see", var3);
      var2.setValue("package", "weblogic.management.configuration");
      String var4 = (new String("The target that is used internally to register the JTA recovery manager to the Migration Manager.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var4);
      var2.setValue("description", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.JTAMigratableTargetMBean");
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

         var2 = new PropertyDescriptor("AllCandidateServers", JTAMigratableTargetMBean.class, var3, var4);
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

         var2 = new PropertyDescriptor("Cluster", JTAMigratableTargetMBean.class, var3, var4);
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

         var2 = new PropertyDescriptor("ConstrainedCandidateServers", JTAMigratableTargetMBean.class, var3, var4);
         var1.put("ConstrainedCandidateServers", var2);
         var2.setValue("description", "<p>The (user-restricted) list of servers that can host the migratable services deployed to this migratable target. The migratable service will not be allowed to migrate to a server that is not in the returned list of servers.</p>  <p>For example, this feature may be used to configure two servers that have access to a dual-ported ported disk. All servers in this list must be part of the cluster that is associated with the migratable target.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("remover", "removeConstrainedCandidateServer");
         var2.setValue("adder", "addConstrainedCandidateServer");
      }

      if (!var1.containsKey("HostingServer")) {
         var3 = "getHostingServer";
         var4 = null;
         var2 = new PropertyDescriptor("HostingServer", JTAMigratableTargetMBean.class, var3, var4);
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

         var2 = new PropertyDescriptor("MigrationPolicy", JTAMigratableTargetMBean.class, var3, var4);
         var1.put("MigrationPolicy", var2);
         var2.setValue("description", " ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("MigratableTargetMBean#getMigrationPolicy")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, "manual");
         var2.setValue("legalValues", new Object[]{"manual", "failure-recovery"});
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", JTAMigratableTargetMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("UserPreferredServer")) {
         var3 = "getUserPreferredServer";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUserPreferredServer";
         }

         var2 = new PropertyDescriptor("UserPreferredServer", JTAMigratableTargetMBean.class, var3, var4);
         var1.put("UserPreferredServer", var2);
         var2.setValue("description", "<p>Returns the server that the user prefers the singleton service to be active on.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("StrictOwnershipCheck")) {
         var3 = "isStrictOwnershipCheck";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStrictOwnershipCheck";
         }

         var2 = new PropertyDescriptor("StrictOwnershipCheck", JTAMigratableTargetMBean.class, var3, var4);
         var1.put("StrictOwnershipCheck", var2);
         var2.setValue("description", "<p>Whether continue to boot if cannot find the current owner of TRS to do failback. This attribute is only meaningful for servers in cluster. </p>  <p>If true: server will fail to boot under this situation.</p> <p>If false: server will continue to boot without trying to do failback.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = JTAMigratableTargetMBean.class.getMethod("addConstrainedCandidateServer", ServerMBean.class);
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

      var3 = JTAMigratableTargetMBean.class.getMethod("removeConstrainedCandidateServer", ServerMBean.class);
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
      Method var3 = JTAMigratableTargetMBean.class.getMethod("freezeCurrentValue", String.class);
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

      var3 = JTAMigratableTargetMBean.class.getMethod("restoreDefaultValue", String.class);
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
