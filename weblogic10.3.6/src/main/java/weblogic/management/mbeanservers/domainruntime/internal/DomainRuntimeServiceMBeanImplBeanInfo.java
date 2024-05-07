package weblogic.management.mbeanservers.domainruntime.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean;
import weblogic.management.mbeanservers.internal.ServiceImplBeanInfo;
import weblogic.management.runtime.RuntimeMBean;

public class DomainRuntimeServiceMBeanImplBeanInfo extends ServiceImplBeanInfo {
   public static Class INTERFACE_CLASS = DomainRuntimeServiceMBean.class;

   public DomainRuntimeServiceMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public DomainRuntimeServiceMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = DomainRuntimeServiceMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.management.mbeanservers.domainruntime.internal");
      String var3 = (new String("<p>Provides a common access point for navigating to all runtime and configuration MBeans in the domain as well as to MBeans that provide domain-wide services (such as controlling and monitoring the life cycles of servers and message-driven EJBs and coordinating the migration of migratable services).</p>  <p>This MBean is available only on the Administration Server.</p>  <p>The <code>javax.management.ObjectName</code> of this MBean is \"<code>com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean</code>\".</p>  <p>This is the only object name that a JMX client needs to navigate the hierarchy available from this MBean.</p> <dl> <dt>Note:</dt>  <dd> <p>If your JMX client uses the Domain Runtime MBean Server to access runtime or configuration MBeans by constructing object names (instead of by using this <code>DomainRuntimeServiceMBean</code> to navigate the MBean hierarchy), the client must add a <code>Location=<i>servername</i></code> key property to the MBean object name. The MBean server uses this key property to route the JMX request to the appropriate WebLogic Server instance. If your client uses the <code>DomainRuntimeServiceMBean</code> to navigate the MBean hierarchy, the object names that it obtains automatically contain the location key property.</p> </dd> </dl> <p>To start navigating, a JMX client invokes the <code>javax.management.MBeanServerConnection.getAttribute()</code> method and passes the following as parameters:</p>  <ul> <li> <p>The <code>ObjectName</code> of this service MBean</p> </li>  <li> <p>A <code>String</code> representation for the name of an attribute in this MBean that contains the root of an MBean hierarchy</p> </li> </ul>  <p>This method call returns the <code>ObjectName</code> for the root MBean. To access MBeans below the root, the JMX client passes the root MBean's <code>ObjectName</code> and the name of a root MBean attribute that contains a child MBean to the <code>MBeanServerConnection.getAttribute()</code> method. This method call returns the <code>ObjectName</code> of the child MBean.</p>  <p>For example:<br clear=\"none\" /> <code>ObjectName drs = new ObjectName(\"com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean\");</code> <br clear=\"none\" /> <code>// Get the ObjectName of the domain's DomainMBean by getting the value<br clear=\"none\" /> // of the DomainRuntimeServiceMBean DomainConfiguration attribute<br clear=\"none\" /> ObjectName domainconfig =<br clear=\"none\" /> (ObjectName) MBeanServerConnection.getAttribute(drs, \"DomainConfiguration\");<br clear=\"none\" /> // Get the ObjectNames for all ServerMBeans in the domain by getting<br clear=\"none\" /> // the value of the DomainMBean Servers attribute<br clear=\"none\" /> ObjectName[] servers =<br clear=\"none\" /> (ObjectName[]) MBeanServerConnection.getAttribute(domainconfig, \"Servers\");</code></p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("rolePermitAll", Boolean.TRUE);
      var2.setValue("interfaceclassname", "weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("DomainConfiguration")) {
         var3 = "getDomainConfiguration";
         var4 = null;
         var2 = new PropertyDescriptor("DomainConfiguration", DomainRuntimeServiceMBean.class, var3, (String)var4);
         var1.put("DomainConfiguration", var2);
         var2.setValue("description", "<p>Contains the active <code>DomainMBean</code> for the current WebLogic Server domain.</p>  <p>Get this MBean to learn about the active configuration of all servers and resources in the domain. Any command line options that were used to start servers in this domain override the values in this <code>DomainMBean</code>. For example, if you used a command line option to override a server's listen port, the <code>ServerMBean</code> that you navigate to from this <code>DomainMBean</code> will show the value persisted in the <code>config.xml</code> file; it will not show the value that was passed in the command line option.</p>  <dl> <dt>Note:</dt>  <dd> <p>The <code>DomainMBean</code> that can be accessed from this (<code>DomainRuntimeServiceMBean</code>) MBean attribute represents the active configuration of the domain and cannot be edited. The <i>pending</i> <code>DomainMBean</code>, which can be edited, is returned by the {@link weblogic.management.mbeanservers.edit.ConfigurationManagerMBean#startEdit startEdit} operation in the <code>ConfigurationManagerMBean</code>.</p> </dd> </dl> ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("#findDomainConfiguration"), BeanInfoHelper.encodeEntities("#findServerConfiguration")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("DomainPending")) {
         var3 = "getDomainPending";
         var4 = null;
         var2 = new PropertyDescriptor("DomainPending", DomainRuntimeServiceMBean.class, var3, (String)var4);
         var1.put("DomainPending", var2);
         var2.setValue("description", "<p>Contains a read-only version of the pending <code>DomainMBean</code> for the current WebLogic Server domain. You cannot use this MBean to modify a domain's configuration. </p> <p>If you want to modify a domain's configuration, use the {@link ConfigurationManagerMBean#startEdit startEdit} operation in the <code>ConfigurationManagerMBean</code> to start an edit session. The <code>startEdit</code> operation returns an editable <code>DomainMBean</code>.</p>  <p>Get this read-only version of the MBean to learn about the pending configuration of all servers and resources in the domain.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("DomainRuntime")) {
         var3 = "getDomainRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("DomainRuntime", DomainRuntimeServiceMBean.class, var3, (String)var4);
         var1.put("DomainRuntime", var2);
         var2.setValue("description", "<p>Contains the <code>DomainRuntimeMBean</code> for the current WebLogic Server domain.</p><p>This MBean provides access to the special service interfaces that exist only on the Administration Server and provide life cycle control over the domain.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         var2 = new PropertyDescriptor("Name", DomainRuntimeServiceMBean.class, var3, (String)var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>A unique key that WebLogic Server generates to identify the current instance of this MBean type.</p>  <p>For a singleton, such as <code>DomainRuntimeServiceMBean</code>, this key is often just the bean's short class name.</p> ");
      }

      if (!var1.containsKey("ParentAttribute")) {
         var3 = "getParentAttribute";
         var4 = null;
         var2 = new PropertyDescriptor("ParentAttribute", DomainRuntimeServiceMBean.class, var3, (String)var4);
         var1.put("ParentAttribute", var2);
         var2.setValue("description", "<p>The name of the attribute of the parent that refers to this bean</p> ");
      }

      if (!var1.containsKey("ParentService")) {
         var3 = "getParentService";
         var4 = null;
         var2 = new PropertyDescriptor("ParentService", DomainRuntimeServiceMBean.class, var3, (String)var4);
         var1.put("ParentService", var2);
         var2.setValue("description", "<p>The MBean that created the current MBean instance.</p>  <p>In the data model for WebLogic Server MBeans, an MBean that creates another MBean is called a <i>parent</i>. MBeans at the top of the hierarchy have no parents.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("Path")) {
         var3 = "getPath";
         var4 = null;
         var2 = new PropertyDescriptor("Path", DomainRuntimeServiceMBean.class, var3, (String)var4);
         var1.put("Path", var2);
         var2.setValue("description", "<p>Returns the path to the bean relative to the reoot of the heirarchy of services</p> ");
      }

      if (!var1.containsKey("ServerName")) {
         var3 = "getServerName";
         var4 = null;
         var2 = new PropertyDescriptor("ServerName", DomainRuntimeServiceMBean.class, var3, (String)var4);
         var1.put("ServerName", var2);
         var2.setValue("description", "<p>The name of this WebLogic Server instance as defined in the domain configuration.</p> ");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("ServerRuntimes")) {
         var3 = "getServerRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("ServerRuntimes", DomainRuntimeServiceMBean.class, var3, (String)var4);
         var1.put("ServerRuntimes", var2);
         var2.setValue("description", "<p>Contains all <code>ServerRuntimeMBean</code> instances on all servers in the domain.</p> <p>Get these MBeans to learn about the current runtime statistics for all server instances in the domain.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("Type")) {
         var3 = "getType";
         var4 = null;
         var2 = new PropertyDescriptor("Type", DomainRuntimeServiceMBean.class, var3, (String)var4);
         var1.put("Type", var2);
         var2.setValue("description", "<p>The MBean type for this instance. This is useful for MBean types that support multiple intances, such as <code>ActivationTaskMBean</code>.</p> ");
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         Method var3 = DomainRuntimeServiceMBean.class.getMethod("lookupServerRuntime", String.class);
         ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         String var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            MethodDescriptor var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Returns the <code>ServerRuntimeMBean</code> for the specified server instance. The operation will return a null value if the named server is not currently running.</p> <p>The <code>ServerRuntimeMBean</code> is the root of runtime MBean hierarchy for a server instance. Each runtime MBean in the hierarchy provides access to the server's status and control as well as statistical information about any deployed or configured service on the server.</p> ");
            var2.setValue("role", "finder");
            var2.setValue("property", "ServerRuntimes");
            var2.setValue("since", "9.0.0.0");
         }
      }

   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = DomainRuntimeServiceMBean.class.getMethod("findDomainConfiguration", String.class);
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Returns the active <code>DomainMBean</code> for the specified server. </p><p>Get this MBean to learn about the current configuration of the server, including any values that were overridden by the server's startup command. For example, if you used a command line option to override a server's listen port, the <code>ServerMBean</code> that you navigate to from this <code>DomainMBean</code> will show the value that was passed in the command line option.</p> <dl><dt>Note:</dt><dd><p>The <i>pending</i> <code>DomainMBean</code>, which can be edited, is available only from the Edit MBean Server and its <code>EditServiceMBean</code>. The <code>DomainMBean</code> that can be accessed from this (<code>DomainRuntimeServiceMBean</code>) MBean attribute represents the active configuration of the specified server and cannot be edited.</p></dd></dl> ");
         var2.setValue("role", "operation");
      }

      var3 = DomainRuntimeServiceMBean.class.getMethod("findServerConfiguration", String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Returns the active <code>ServerMBean</code> for the specified server. </p><p>Get this MBean to learn about the current configuration of the server, including any values that were overridden by the server's startup command. For example, if you used a command line option to override a server's listen port, this <code>ServerMBean</code> will show the value that was passed in the command line option.</p> <dl><dt>Note:</dt><dd><p>The <i>pending</i> <code>ServerMBean</code>, which can be edited, is available only from the Edit MBean Server and its <code>EditServiceMBean</code>. The <code>ServerMBean</code> that can be accessed from this (<code>DomainRuntimeServiceMBean</code>) MBean attribute represents the active configuration of the specified server and cannot be edited.</p></dd></dl> ");
         var2.setValue("role", "operation");
      }

      var3 = DomainRuntimeServiceMBean.class.getMethod("findRuntimes", DescriptorBean.class);
      ParameterDescriptor[] var6 = new ParameterDescriptor[]{createParameterDescriptor("configurationMBean", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Enables a JMX client to retrieve monitoring statistics for all instances of a specific resource on all servers in a domain. To use this operation, a JMX client passes a single configuration MBean and the operation returns runtime MBeans for this resource from all servers in the domain.</p> <p>For example, a JMX client connects to the Domain Runtime MBean server and gets the <code>JMSServerMBean</code> for a JMS server named \"JS1.\" The JMX client then invokes this operation and the operation determines the active server instances on which the \"JS1\" JMS server has been targeted. It then returns all of the <code>JMSServerRuntimeMBean</code>s for \"JS1\" from all servers in the domain.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = DomainRuntimeServiceMBean.class.getMethod("findRuntime", DescriptorBean.class, String.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("configurationMBean", (String)null), createParameterDescriptor("serverName", "that owns that runtime mbean. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Enables a JMX client to retrieve monitoring statistics for a specific resource on a specific server. To use this operation, a JMX client passes a single configuration MBean and the name of a server instance. The operation returns the corresponding runtime MBean for the resource on the named server, assuming that the resource has been targeted or deployed to the server.</p> <p>For example, given the <code>JMSServerMBean</code> for a JMS server named \"JS1\" on a server instance named \"ManagedServer1,\" this operation returns the <code>JMSServerRuntimeMBean</code> for \"JS1\" on \"ManagedServer1.\"</p> ");
         var2.setValue("role", "operation");
      }

      var3 = DomainRuntimeServiceMBean.class.getMethod("findConfiguration", RuntimeMBean.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("runtimeMBean", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Enables a JMX client to retrieve configuration data for a specific instance of a resource. To use this operation, a JMX client passes a single runtime MBean and the operation returns the active configuration MBean for the resource.</p> <p>For example, given the <code>JMSServerRuntimeMBean</code> for a JMS server named \"JS1\" on the current server instance, this operation returns the active <code>JMSServerMBean</code> for \"JS1.\"</p> ");
         var2.setValue("role", "operation");
      }

      var3 = DomainRuntimeServiceMBean.class.getMethod("findService", String.class, String.class, String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Returns the Service on the specified Server or in the primary MBeanServer if the location is not specified. ");
         var2.setValue("role", "operation");
      }

      var3 = DomainRuntimeServiceMBean.class.getMethod("getServices", String.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("serverName", "String ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns all the services that do not have a parent i.e all the root services ");
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
