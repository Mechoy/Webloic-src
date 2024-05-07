package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class JMSDistributedTopicMBeanImplBeanInfo extends JMSDistributedDestinationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JMSDistributedTopicMBean.class;

   public JMSDistributedTopicMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public JMSDistributedTopicMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = JMSDistributedTopicMBeanImpl.class;
      } catch (Throwable var6) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "7.0.0.0");
      var2.setValue("obsolete", "9.0.0.0");
      var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.j2ee.descriptor.wl.DistributedTopicBean} ");
      String[] var3 = new String[]{BeanInfoHelper.encodeEntities("weblogic.j2ee.descriptor.wl.UniformDistributedTopicBean")};
      var2.setValue("see", var3);
      var2.setValue("package", "weblogic.management.configuration");
      String var4 = (new String("This class represents a JMS distributed topic, which is comprised of multiple physical JMS topics as members of a single distributed set of topics that can be served by multiple WebLogic Server instances within a cluster.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var4);
      var2.setValue("description", var4);
      String[] var5 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var5);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.JMSDistributedTopicMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("JMSDistributedTopicMembers")) {
         var3 = "getJMSDistributedTopicMembers";
         var4 = null;
         var2 = new PropertyDescriptor("JMSDistributedTopicMembers", JMSDistributedTopicMBean.class, var3, var4);
         var1.put("JMSDistributedTopicMembers", var2);
         var2.setValue("description", "Get all the Members ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createJMSDistributedTopicMember");
         var2.setValue("creator", "createJMSDistributedTopicMember");
         var2.setValue("destroyer", "destroyJMSDistributedTopicMember");
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("JMSTemplate")) {
         var3 = "getJMSTemplate";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJMSTemplate";
         }

         var2 = new PropertyDescriptor("JMSTemplate", JMSDistributedTopicMBean.class, var3, var4);
         var1.put("JMSTemplate", var2);
         var2.setValue("description", "<p>gets JMSTemplateMBean instance off this DistributedDestination</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyJMSTemplate");
         var2.setValue("creator", "createJMSTemplate");
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.j2ee.descriptor.wl.DistributedDestinationBean and weblogic.j2ee.descriptor.wl.UnifrormDistributedDestinationBean} ");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (!var1.containsKey("JNDIName")) {
         var3 = "getJNDIName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJNDIName";
         }

         var2 = new PropertyDescriptor("JNDIName", JMSDistributedTopicMBean.class, var3, var4);
         var1.put("JNDIName", var2);
         var2.setValue("description", "<p>The JNDI name used to look up a virtual destination within the JNDI namespace. Applications can use the JNDI name to look up the virtual destination. If not specified, then the destination is not bound into the JNDI namespace.</p> ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("LoadBalancingPolicy")) {
         var3 = "getLoadBalancingPolicy";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLoadBalancingPolicy";
         }

         var2 = new PropertyDescriptor("LoadBalancingPolicy", JMSDistributedTopicMBean.class, var3, var4);
         var1.put("LoadBalancingPolicy", var2);
         var2.setValue("description", "<p>Defines the load balancing policy for producers sending messages to a distributed destination in order to balance the message load across the members of the distributed set.</p>  <ul> <li><b>Round-Robin</b> <p>- The system maintains an ordering of physical topic members within the set by distributing the messaging load across the topic members one at a time in the order that they are defined in the configuration file. Each WebLogic Server maintains an identical ordering, but may be at a different point within the ordering. If weights are assigned to any of the topic members in the set, then those members appear multiple times in the ordering.</p> </li>  <li><b>Random</b> <p>- The weight assigned to the topic members is used to compute a weighted distribution for the members of the set. The messaging load is distributed across the topic members by pseudo-randomly accessing the distribution. In the short run, the load will not be directly proportional to the weight. In the long run, the distribution will approach the limit of the distribution. A pure random distribution can be achieved by setting all the weights to the same value, which is typically set to 1.</p> </li> </ul> ");
         setPropertyDescriptorDefault(var2, "Round-Robin");
         var2.setValue("legalValues", new Object[]{"Round-Robin", "Random"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("Members")) {
         var3 = "getMembers";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMembers";
         }

         var2 = new PropertyDescriptor("Members", JMSDistributedTopicMBean.class, var3, var4);
         var1.put("Members", var2);
         var2.setValue("description", "<p>The members for this distributed topic.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("adder", "addMember");
         var2.setValue("remover", "removeMember");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", JMSDistributedTopicMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("Notes")) {
         var3 = "getNotes";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNotes";
         }

         var2 = new PropertyDescriptor("Notes", JMSDistributedTopicMBean.class, var3, var4);
         var1.put("Notes", var2);
         var2.setValue("description", "<p>Optional information that you can include to describe this configuration.</p>  <p>WebLogic Server saves this note in the domain's configuration file (<code>config.xml</code>) as XML PCDATA. All left angle brackets (&lt;) are converted to the XML entity <code>&amp;lt;</code>. Carriage returns/line feeds are preserved.</p>  <dl> <dt>Note:</dt>  <dd> <p>If you create or edit a note from the Administration Console, the Administration Console does not preserve carriage returns/line feeds.</p> </dd> </dl> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Targets")) {
         var3 = "getTargets";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTargets";
         }

         var2 = new PropertyDescriptor("Targets", JMSDistributedTopicMBean.class, var3, var4);
         var1.put("Targets", var2);
         var2.setValue("description", "<p>You must select a target on which an MBean will be deployed from this list of the targets in the current domain on which this item can be deployed. Targets must be either servers or clusters. The deployment will only occur once if deployments overlap.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("adder", "addTarget");
         var2.setValue("remover", "removeTarget");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Template")) {
         var3 = "getTemplate";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTemplate";
         }

         var2 = new PropertyDescriptor("Template", JMSDistributedTopicMBean.class, var3, var4);
         var1.put("Template", var2);
         var2.setValue("description", "<p>The JMS template from which the distributed destination is derived.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      MethodDescriptor var2;
      Method var3;
      ParameterDescriptor[] var4;
      String var5;
      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = JMSDistributedTopicMBean.class.getMethod("createJMSTemplate", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "Name of the template to create ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.j2ee.descriptor.wl.DistributedDestinationBean and weblogic.j2ee.descriptor.wl.UnifrormDistributedDestinationBean} ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Creates JMSTemplateMBean instance off this DistributedDestination</p> ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSTemplate");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = JMSDistributedTopicMBean.class.getMethod("destroyJMSTemplate", JMSTemplateMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("template", "The template to delete ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.j2ee.descriptor.wl.DistributedDestinationBean and weblogic.j2ee.descriptor.wl.UnifrormDistributedDestinationBean} ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>deletes JMSTemplate from DistributedDestination</p> ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSTemplate");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = JMSDistributedTopicMBean.class.getMethod("createJMSDistributedTopicMember", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Create a new diagnostic deployment that can be targeted to a server</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSDistributedTopicMembers");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = JMSDistributedTopicMBean.class.getMethod("createJMSDistributedTopicMember", String.class, JMSDistributedTopicMemberMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null), createParameterDescriptor("toclone", "bean which need to be cloned and added to this parent ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>This method is there to support addMember which is relic of old mbean infrastructure but somehow got documented server</p> ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSDistributedTopicMembers");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = JMSDistributedTopicMBean.class.getMethod("destroyJMSDistributedTopicMember", JMSDistributedTopicMemberMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("member", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Delete a diagnostic deployment configuration from the domain.</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSDistributedTopicMembers");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      MethodDescriptor var2;
      Method var3;
      ParameterDescriptor[] var4;
      String var5;
      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = JMSDistributedTopicMBean.class.getMethod("addMember", JMSDistributedTopicMemberMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("Member", "The feature to be added to the Member attribute ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Add a member to this distributed topic.</p> ");
            var2.setValue("role", "collection");
            var2.setValue("property", "Members");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      var3 = JMSDistributedTopicMBean.class.getMethod("addTarget", TargetMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("target", "The feature to be added to the Target attribute ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>You can add a target to specify additional servers on which the deployment can be deployed. The targets must be either clusters or servers.</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "Targets");
      }

      String[] var6;
      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = JMSDistributedTopicMBean.class.getMethod("removeMember", JMSDistributedTopicMemberMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("member", "the JMSDistributedTopicMember to remove from the distributed topic ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var6 = new String[]{BeanInfoHelper.encodeEntities("IllegalArgumentException if the specified member is not a member of this distributed topic")};
            var2.setValue("throws", var6);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Remove a member from this distributed topic.</p> ");
            var2.setValue("role", "collection");
            var2.setValue("property", "Members");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      var3 = JMSDistributedTopicMBean.class.getMethod("removeTarget", TargetMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("target", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes the value of the addTarget attribute.</p> ");
         var6 = new String[]{BeanInfoHelper.encodeEntities("#addTarget")};
         var2.setValue("see", var6);
         var2.setValue("role", "collection");
         var2.setValue("property", "Targets");
      }

   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = JMSDistributedTopicMBean.class.getMethod("lookupJMSDistributedTopicMember", String.class);
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         MethodDescriptor var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "finder");
         var2.setValue("property", "JMSDistributedTopicMembers");
      }

   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      MethodDescriptor var2;
      Method var3;
      ParameterDescriptor[] var4;
      String var5;
      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = JMSDistributedTopicMBean.class.getMethod("destroyJMSDistributedTopicMember", String.class, JMSDistributedTopicMemberMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "name "), createParameterDescriptor("member", "JMSDistributedTopicMember ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "This method is to support removeMember() which is relic of old mbean infrastructure ");
            var2.setValue("role", "operation");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      var3 = JMSDistributedTopicMBean.class.getMethod("freezeCurrentValue", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has not been set explicitly, and if the attribute has a default value, this operation forces the MBean to persist the default value.</p>  <p>Unless you use this operation, the default value is not saved and is subject to change if you update to a newer release of WebLogic Server. Invoking this operation isolates this MBean from the effects of such changes.</p>  <dl> <dt>Note:</dt>  <dd> <p>To insure that you are freezing the default value, invoke the <code>restoreDefaultValue</code> operation before you invoke this.</p> </dd> </dl>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute for which some other value has been set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = JMSDistributedTopicMBean.class.getMethod("restoreDefaultValue", String.class);
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
