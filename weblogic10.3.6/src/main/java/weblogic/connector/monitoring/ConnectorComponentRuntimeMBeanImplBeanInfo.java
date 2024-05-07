package weblogic.connector.monitoring;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;
import weblogic.j2ee.ComponentRuntimeMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.ConnectorComponentRuntimeMBean;

public class ConnectorComponentRuntimeMBeanImplBeanInfo extends ComponentRuntimeMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ConnectorComponentRuntimeMBean.class;

   public ConnectorComponentRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ConnectorComponentRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ConnectorComponentRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "8.1.0.0");
      var2.setValue("package", "weblogic.connector.monitoring");
      String var3 = (new String("Generates notifications about the deployment state of resource adapters. (Each resource adapter is represented by an instance of {@link weblogic.management.configuration.ConnectorComponentMBean}.) <p> In 2-phase deployment, if a resource adapter's state is <code>PREPARED</code> then it has achieved the first phase of deployment (everything is set up and all that remains is to enable a reference to the adapter). When the resource adapter is in an <code>ACTIVATED</code> state, it has achieved the second phase of deployment, in which applications can obtain a reference to the adapter. <p> A server instance creates an instance of this interface when it creates an instance of <code>weblogic.management.configuration.ConnectorComponentMBean</code>.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.ConnectorComponentRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("ActiveVersionId")) {
         var3 = "getActiveVersionId";
         var4 = null;
         var2 = new PropertyDescriptor("ActiveVersionId", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("ActiveVersionId", var2);
         var2.setValue("description", "<p>Get the active version Id.</p> ");
      }

      if (!var1.containsKey("AppDeploymentMBean")) {
         var3 = "getAppDeploymentMBean";
         var4 = null;
         var2 = new PropertyDescriptor("AppDeploymentMBean", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("AppDeploymentMBean", var2);
         var2.setValue("description", "<p> Gets the AppDeploymentMBean for the Connector Component </p> ");
         var2.setValue("deprecated", "9.1.0.0 Acquire this by looking up the AppDeploymentMBean with the same name as the parent ApplicationRuntime ");
      }

      if (!var1.containsKey("ComponentName")) {
         var3 = "getComponentName";
         var4 = null;
         var2 = new PropertyDescriptor("ComponentName", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("ComponentName", var2);
         var2.setValue("description", "<p>Get the name of the connector component.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("Configuration")) {
         var3 = "getConfiguration";
         var4 = null;
         var2 = new PropertyDescriptor("Configuration", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("Configuration", var2);
         var2.setValue("description", "<p>Return the xml string representing the RA configuration. The xml corresponding to the latest schema is returned. The current supported version is \"1.0\"</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("ConfigurationVersion")) {
         var3 = "getConfigurationVersion";
         var4 = null;
         var2 = new PropertyDescriptor("ConfigurationVersion", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("ConfigurationVersion", var2);
         var2.setValue("description", "<p>Return the latest configuration version.<.p> ");
      }

      if (!var1.containsKey("ConfiguredProperties")) {
         var3 = "getConfiguredProperties";
         var4 = null;
         var2 = new PropertyDescriptor("ConfiguredProperties", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("ConfiguredProperties", var2);
         var2.setValue("description", "<p> Gets a subset of the resource adapter descriptor information. </p> ");
      }

      if (!var1.containsKey("ConnectionPoolCount")) {
         var3 = "getConnectionPoolCount";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionPoolCount", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectionPoolCount", var2);
         var2.setValue("description", "<p>The number of connection pools.</p> ");
      }

      String[] var5;
      if (!var1.containsKey("ConnectionPools")) {
         var3 = "getConnectionPools";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectionPools", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectionPools", var2);
         var2.setValue("description", "<p>An array of <code>ConnectorConnectionPoolRuntimeMBeans</code>, each of which represents the runtime data for a connection pool in the resource adapter.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.runtime.ConnectorConnectionPoolRuntimeMBean")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("ConnectorComponentMBean")) {
         var3 = "getConnectorComponentMBean";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectorComponentMBean", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectorComponentMBean", var2);
         var2.setValue("description", "<p>Gets the ConnectorComponentMBean for the Connector Component.</p> ");
         var2.setValue("deprecated", "9.1.0.0 The connector component mbean was already deprecated. ");
      }

      if (!var1.containsKey("ConnectorServiceRuntime")) {
         var3 = "getConnectorServiceRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectorServiceRuntime", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("ConnectorServiceRuntime", var2);
         var2.setValue("description", "<p>Return the connector service runtime.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("DeploymentState")) {
         var3 = "getDeploymentState";
         var4 = null;
         var2 = new PropertyDescriptor("DeploymentState", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("DeploymentState", var2);
         var2.setValue("description", "<p>The current deployment state of the module.</p>  <p>A module can be in one and only one of the following states. State can be changed via deployment or administrator console.</p>  <p>- UNPREPARED. State indicating at this  module is neither  prepared or active.</p>  <p>- PREPARED. State indicating at this module of this application is prepared, but not active. The classes have been loaded and the module has been validated.</p>  <p>- ACTIVATED. State indicating at this module  is currently active.</p>  <p>- NEW. State indicating this module has just been created and is being initialized.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#setDeploymentState(int)")};
         var2.setValue("see", var5);
      }

      if (!var1.containsKey("Description")) {
         var3 = "getDescription";
         var4 = null;
         var2 = new PropertyDescriptor("Description", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("Description", var2);
         var2.setValue("description", "<p>Get the Description for the resource adapter.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("Descriptions")) {
         var3 = "getDescriptions";
         var4 = null;
         var2 = new PropertyDescriptor("Descriptions", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("Descriptions", var2);
         var2.setValue("description", "<p>Get the Descriptions for the resource adapter.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("EISResourceId")) {
         var3 = "getEISResourceId";
         var4 = null;
         var2 = new PropertyDescriptor("EISResourceId", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("EISResourceId", var2);
         var2.setValue("description", "<p>Returns the EISResourceId for the component.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("EISType")) {
         var3 = "getEISType";
         var4 = null;
         var2 = new PropertyDescriptor("EISType", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("EISType", var2);
         var2.setValue("description", "<p>Get the EIS type.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("InboundConnections")) {
         var3 = "getInboundConnections";
         var4 = null;
         var2 = new PropertyDescriptor("InboundConnections", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("InboundConnections", var2);
         var2.setValue("description", "<p>An array of runtime information for all inbound connections for the resource adapter.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("InboundConnectionsCount")) {
         var3 = "getInboundConnectionsCount";
         var4 = null;
         var2 = new PropertyDescriptor("InboundConnectionsCount", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("InboundConnectionsCount", var2);
         var2.setValue("description", "<p>The number of inbound connections for the resource adapter.</p> ");
      }

      if (!var1.containsKey("JndiName")) {
         var3 = "getJndiName";
         var4 = null;
         var2 = new PropertyDescriptor("JndiName", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("JndiName", var2);
         var2.setValue("description", "<p>Get the Jndi name of the resource adapter.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("Linkref")) {
         var3 = "getLinkref";
         var4 = null;
         var2 = new PropertyDescriptor("Linkref", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("Linkref", var2);
         var2.setValue("description", "<p>Get the linkref.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("ModuleId")) {
         var3 = "getModuleId";
         var4 = null;
         var2 = new PropertyDescriptor("ModuleId", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("ModuleId", var2);
         var2.setValue("description", "<p>Returns the identifier for this Component.  The identifier is unique within the application.</p>  <p>Typical modules will use the URI for their id.  Web Modules will return their context-root since the web-uri may not be unique within an EAR. ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("Schema")) {
         var3 = "getSchema";
         var4 = null;
         var2 = new PropertyDescriptor("Schema", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("Schema", var2);
         var2.setValue("description", "<p>Get the latest schema for RA configuration.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("SpecVersion")) {
         var3 = "getSpecVersion";
         var4 = null;
         var2 = new PropertyDescriptor("SpecVersion", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("SpecVersion", var2);
         var2.setValue("description", "<p>Get the spec version.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("State")) {
         var3 = "getState";
         var4 = null;
         var2 = new PropertyDescriptor("State", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("State", var2);
         var2.setValue("description", "<p>Get the state of the resource adapter.</p> ");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("Stats")) {
         var3 = "getStats";
         var4 = null;
         var2 = new PropertyDescriptor("Stats", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("Stats", var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("SuspendedState")) {
         var3 = "getSuspendedState";
         var4 = null;
         var2 = new PropertyDescriptor("SuspendedState", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("SuspendedState", var2);
         var2.setValue("description", "<p>Gets the suspended state information of the resource adapter.</p> If getState() returns SUSPENDED then getSuspendedState() returns an integer describing which functions of the resource adapter are suspended: one or more of INBOUND, OUTBOUND or WORK (or ALL) or 0 for nothing suspended ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.connector.extensions.Suspendable")};
         var2.setValue("see", var5);
      }

      if (!var1.containsKey("VendorName")) {
         var3 = "getVendorName";
         var4 = null;
         var2 = new PropertyDescriptor("VendorName", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("VendorName", var2);
         var2.setValue("description", "<p>Get the vendor name.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("Version")) {
         var3 = "getVersion";
         var4 = null;
         var2 = new PropertyDescriptor("Version", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("Version", var2);
         var2.setValue("description", "<p>Get the version.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("VersionId")) {
         var3 = "getVersionId";
         var4 = null;
         var2 = new PropertyDescriptor("VersionId", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("VersionId", var2);
         var2.setValue("description", "<p>Get the version Id.</p> ");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("WorkManagerRuntimes")) {
         var3 = "getWorkManagerRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("WorkManagerRuntimes", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("WorkManagerRuntimes", var2);
         var2.setValue("description", "<p>Get the runtime mbeans for all work managers defined in this component</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("ActiveVersion")) {
         var3 = "isActiveVersion";
         var4 = null;
         var2 = new PropertyDescriptor("ActiveVersion", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("ActiveVersion", var2);
         var2.setValue("description", "<p>Return true if this version is the active version. Returns true if this resource adapter is not versioned.</p> ");
      }

      if (!var1.containsKey("Versioned")) {
         var3 = "isVersioned";
         var4 = null;
         var2 = new PropertyDescriptor("Versioned", ConnectorComponentRuntimeMBean.class, var3, (String)var4);
         var1.put("Versioned", var2);
         var2.setValue("description", "<p>Check if the resource adapter is versioned. Returns true if it is.</p> ");
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
      Method var3 = ConnectorComponentRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorComponentRuntimeMBean.class.getMethod("getConnectionPool", String.class);
      ParameterDescriptor[] var7 = new ParameterDescriptor[]{createParameterDescriptor("key", "JNDI name or resource-link name of the connection pool. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns a <code>ConnectorConnectionPoolRuntimeMBean</code> that represents the statistics for a connection pool. The pool that is accessed in this call must be part of the resource adapter that is being accessed. A null is returned if the JNDI name or resource-link name is not found.</p> ");
         String[] var6 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.runtime.ConnectorConnectionPoolRuntimeMBean")};
         var2.setValue("see", var6);
         var2.setValue("role", "operation");
      }

      var3 = ConnectorComponentRuntimeMBean.class.getMethod("getInboundConnection", String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("messageListenerType", "Message listener type. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Runtime information for the specified inbound connection. A null is returned if the inbound connection is not found in the resource adapter.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorComponentRuntimeMBean.class.getMethod("suspendAll");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Resumes all activities of this resource adapter.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorComponentRuntimeMBean.class.getMethod("suspend", Integer.TYPE);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("type", "int The type of activity(ies), @see weblogic.connector.extensions.Suspendable ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Suspend a particular type of activity for this resource adapter</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorComponentRuntimeMBean.class.getMethod("suspend", Integer.TYPE, Properties.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("type", "int The type of activity(ies), @see weblogic.connector.extensions.Suspendable "), createParameterDescriptor("props", "Properties to pass on to the RA or null ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Suspends the specified type of activity for this resource adapter</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorComponentRuntimeMBean.class.getMethod("resumeAll");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Resumes all activities of this resource adapter.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorComponentRuntimeMBean.class.getMethod("resume", Integer.TYPE);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("type", "int The type of activity(ies), @see weblogic.connector.extensions.Suspendable ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Resumes the specified type of activity for this resource adapter</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorComponentRuntimeMBean.class.getMethod("resume", Integer.TYPE, Properties.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("type", "int The type of activity(ies), @see weblogic.connector.extensions.Suspendable "), createParameterDescriptor("props", "Properties to pass on to the RA or null ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Resumes the specified type of activity for this resource adapter</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorComponentRuntimeMBean.class.getMethod("getSchema", String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("version", "String ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Get the schema for RA configuration based on the version that is provided. Return null if the version is not found. The current supported version is \"1.0\"</p> ");
         var2.setValue("role", "operation");
      }

      var3 = ConnectorComponentRuntimeMBean.class.getMethod("getConfiguration", String.class);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("version", "String ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var7);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Return the xml string representing the RA configuration. The xml corresponding to the version specified is returned.</p> ");
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
