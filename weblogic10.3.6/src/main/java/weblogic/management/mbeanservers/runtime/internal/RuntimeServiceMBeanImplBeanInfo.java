package weblogic.management.mbeanservers.runtime.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.mbeanservers.internal.ServiceImplBeanInfo;
import weblogic.management.mbeanservers.runtime.RuntimeServiceMBean;
import weblogic.management.runtime.RuntimeMBean;

public class RuntimeServiceMBeanImplBeanInfo extends ServiceImplBeanInfo {
   public static Class INTERFACE_CLASS = RuntimeServiceMBean.class;

   public RuntimeServiceMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public RuntimeServiceMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = RuntimeServiceMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.management.mbeanservers.runtime.internal");
      String var3 = (new String("<p>Provides an entry point for navigating the hierarchy of WebLogic Server runtime MBeans and active configuration MBeans for the current server.</p>  <p>Each server instance in a domain provides its own instance of this MBean.</p>  <p>The <code>javax.management.ObjectName</code> of this MBean is \"<code>com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean</code>\".</p>  <p>This is the only object name that a JMX client needs to navigate the hierarchy available from this MBean. To start navigating, a JMX client invokes the <code>javax.management.MBeanServerConnection.getAttribute()</code> method and passes the following as parameters:</p>  <ul> <li> <p>The <code>ObjectName</code> of this service MBean</p> </li>  <li> <p>A <code>String</code> representation for the name of an attribute in this MBean that contains the root of an MBean hierarchy</p> </li> </ul>  <p>This method call returns the <code>ObjectName</code> for the root MBean. To access MBeans below the root, the JMX client passes the root MBean's <code>ObjectName</code> and the name of a root MBean attribute that contains a child MBean to the <code>MBeanServerConnection.getAttribute()</code> method. This method call returns the <code>ObjectName</code> of the child MBean.</p>  <p>For example:<br clear=\"none\" /> <code>ObjectName rs = new ObjectName(\"com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean\");</code> <br clear=\"none\" /> <code>// Get the ObjectName of the server's ServerRuntimeMBean by getting the value<br clear=\"none\" /> // of the RuntimeServiceMBean ServerRuntime attribute<br clear=\"none\" /> ObjectName serverrt =<br clear=\"none\" /> (ObjectName) MBeanServerConnection.getAttribute(rs,\"ServerRuntime\");<br clear=\"none\" /> // Get the ObjectNames for all ApplicationRuntimeMBeans on the server by getting<br clear=\"none\" /> // the value of the ServerRuntimeMBean ApplicationRuntimes attribute<br clear=\"none\" /> ObjectName[] apprt =<br clear=\"none\" /> (ObjectName[]) MBeanServerConnection.getAttribute(serverrt, \"ApplicationRuntimes\");</code></p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("rolePermitAll", Boolean.TRUE);
      var2.setValue("interfaceclassname", "weblogic.management.mbeanservers.runtime.RuntimeServiceMBean");
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
         var2 = new PropertyDescriptor("DomainConfiguration", RuntimeServiceMBean.class, var3, (String)var4);
         var1.put("DomainConfiguration", var2);
         var2.setValue("description", "<p>Contains the active <code>DomainMBean</code> for the current WebLogic Server domain.</p>  <p>Get this MBean to learn about the active configuration of all servers and resources in the domain.</p>  <dl> <dt>Note:</dt>  <dd> <p>The <code>DomainMBean</code> that can be accessed from this (<code>RuntimeServiceMBean</code>) MBean attribute represents the active configuration of the domain and cannot be edited. The <i>pending</i> <code>DomainMBean</code>, which can be edited, is available only from the Edit MBean Server and its <code>EditServiceMBean</code>.</p> </dd> </dl> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         var2 = new PropertyDescriptor("Name", RuntimeServiceMBean.class, var3, (String)var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>A unique key that WebLogic Server generates to identify the current instance of this MBean type.</p>  <p>For a singleton, such as <code>DomainRuntimeServiceMBean</code>, this key is often just the bean's short class name.</p> ");
      }

      if (!var1.containsKey("ParentAttribute")) {
         var3 = "getParentAttribute";
         var4 = null;
         var2 = new PropertyDescriptor("ParentAttribute", RuntimeServiceMBean.class, var3, (String)var4);
         var1.put("ParentAttribute", var2);
         var2.setValue("description", "<p>The name of the attribute of the parent that refers to this bean</p> ");
      }

      if (!var1.containsKey("ParentService")) {
         var3 = "getParentService";
         var4 = null;
         var2 = new PropertyDescriptor("ParentService", RuntimeServiceMBean.class, var3, (String)var4);
         var1.put("ParentService", var2);
         var2.setValue("description", "<p>The MBean that created the current MBean instance.</p>  <p>In the data model for WebLogic Server MBeans, an MBean that creates another MBean is called a <i>parent</i>. MBeans at the top of the hierarchy have no parents.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("Path")) {
         var3 = "getPath";
         var4 = null;
         var2 = new PropertyDescriptor("Path", RuntimeServiceMBean.class, var3, (String)var4);
         var1.put("Path", var2);
         var2.setValue("description", "<p>Returns the path to the bean relative to the reoot of the heirarchy of services</p> ");
      }

      if (!var1.containsKey("ServerConfiguration")) {
         var3 = "getServerConfiguration";
         var4 = null;
         var2 = new PropertyDescriptor("ServerConfiguration", RuntimeServiceMBean.class, var3, (String)var4);
         var1.put("ServerConfiguration", var2);
         var2.setValue("description", "<p>Contains the active <code>ServerMBean</code> for the current server instance.</p>  <p>Get this MBean to learn about the configuration of the current server, including any values that were overridden by the server's startup command.</p>  <dl> <dt>Note:</dt>  <dd> <p>The <code>ServerMBean</code> that can be accessed from this (<code>RuntimeServiceMBean</code>) MBean attribute represents the active configuration of the server and cannot be edited.</p> </dd> </dl> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("ServerName")) {
         var3 = "getServerName";
         var4 = null;
         var2 = new PropertyDescriptor("ServerName", RuntimeServiceMBean.class, var3, (String)var4);
         var1.put("ServerName", var2);
         var2.setValue("description", "<p>The name of the current WebLogic Server instance as defined in the domain configuration.</p> ");
      }

      if (!var1.containsKey("ServerRuntime")) {
         var3 = "getServerRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("ServerRuntime", RuntimeServiceMBean.class, var3, (String)var4);
         var1.put("ServerRuntime", var2);
         var2.setValue("description", "<p>Contains <code>ServerRuntimeMBean</code> for the current server.</p>  <p>The <code>ServerRuntimeMBean</code> is the root of runtime MBean hierarchy for this server instance. Each runtime MBean in the hierarchy provides access to the server's status and control as well as statistical information about any deployed or configured service on the server.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("Services")) {
         var3 = "getServices";
         var4 = null;
         var2 = new PropertyDescriptor("Services", RuntimeServiceMBean.class, var3, (String)var4);
         var1.put("Services", var2);
         var2.setValue("description", "<p>Returns all the services that do not have a parent i.e all the root services ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("Type")) {
         var3 = "getType";
         var4 = null;
         var2 = new PropertyDescriptor("Type", RuntimeServiceMBean.class, var3, (String)var4);
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
   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = RuntimeServiceMBean.class.getMethod("findRuntime", DescriptorBean.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("configurationMBean", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Enables a JMX client to retrieve monitoring statistics for a specified resource on the current server. To use this operation, a JMX client passes a single configuration MBean. The operation returns the corresponding runtime MBean for the resource on the current server.</p>  <p>For example, given the <code>JMSServerMBean</code> for a JMS server named \"JS1\" on a the current server, this operation returns the <code>JMSServerRuntimeMBean</code> for \"JS1.\"</p> ");
         var2.setValue("role", "operation");
      }

      var3 = RuntimeServiceMBean.class.getMethod("findConfiguration", RuntimeMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("runtimeMBean", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Enables a JMX client to retrieve configuration data for a specific instance of a resource. To use this operation, a JMX client passes a single runtime MBean and the operation returns the active configuration MBean for the resource.</p>  <p>For example, given the <code>JMSServerRuntimeMBean</code> for a JMS server named \"JS1\" on the current server instance, this operation returns the active <code>JMSServerMBean</code> for \"JS1.\"</p> ");
         var2.setValue("role", "operation");
      }

      var3 = RuntimeServiceMBean.class.getMethod("findService", String.class, String.class);
      String var6 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var6)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var6, var2);
         var2.setValue("description", "<p>Enables client to retrieve a specific named service </p> ");
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
