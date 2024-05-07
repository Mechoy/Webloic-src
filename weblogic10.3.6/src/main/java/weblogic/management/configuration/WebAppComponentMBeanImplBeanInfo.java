package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WebAppComponentMBeanImplBeanInfo extends ComponentMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WebAppComponentMBean.class;

   public WebAppComponentMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WebAppComponentMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WebAppComponentMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("deprecated", "9.0.0.0 in favor of {@link AppDeploymentMBean} ");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("Provides methods for configuring a J2EE web application that is deployed on a WebLogic Server instance. WebLogic Server instantiates this interface only when you deploy a web application. <p>This interface can configure web applications that are deployed as a WAR file or an exploded directory. </p> <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WebAppComponentMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (BeanInfoHelper.isVersionCompliant("7.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("ActivatedTargets")) {
         var3 = "getActivatedTargets";
         var4 = null;
         var2 = new PropertyDescriptor("ActivatedTargets", WebAppComponentMBean.class, var3, var4);
         var1.put("ActivatedTargets", var2);
         var2.setValue("description", "<p>List of servers and clusters where this module is currently active. This attribute is valid only for modules deployed via the two phase protocol. Modules deployed with the WLS 6.x deployment protocol do not maintain this attribute. To determine active targets for a module regardless of deployment protocol, use {@link weblogic.management.runtime.DeployerRuntimeMBean#lookupActiveTargetsForComponent}.</p> ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.ApplicationMBean#isTwoPhase")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "reference");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("since", "7.0.0.0");
      }

      if (!var1.containsKey("Application")) {
         var3 = "getApplication";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setApplication";
         }

         var2 = new PropertyDescriptor("Application", WebAppComponentMBean.class, var3, var4);
         var1.put("Application", var2);
         var2.setValue("description", "<p>The application this component is a part of. This is guaranteed to never be null.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("AuthFilter")) {
         var3 = "getAuthFilter";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAuthFilter";
         }

         var2 = new PropertyDescriptor("AuthFilter", WebAppComponentMBean.class, var3, var4);
         var1.put("AuthFilter", var2);
         var2.setValue("description", "<p>Provides the name of the AuthFilter Servlet class, which will be called before and after all authentication and authorization checks in the Web Application.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("deprecated", "8.0.0.0 Use weblogic.xml. ");
      }

      if (!var1.containsKey("AuthRealmName")) {
         var3 = "getAuthRealmName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAuthRealmName";
         }

         var2 = new PropertyDescriptor("AuthRealmName", WebAppComponentMBean.class, var3, var4);
         var1.put("AuthRealmName", var2);
         var2.setValue("description", "<p>Provides the name of the Realm in the Basic Authentication HTTP dialog box, which pops up on the browsers. authRealmName is now set in weblogic.xml.</p> ");
         setPropertyDescriptorDefault(var2, "weblogic");
         var2.setValue("deprecated", "8.1.0.0 Use weblogic.xml. ");
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", WebAppComponentMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("ServletReloadCheckSecs")) {
         var3 = "getServletReloadCheckSecs";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setServletReloadCheckSecs";
         }

         var2 = new PropertyDescriptor("ServletReloadCheckSecs", WebAppComponentMBean.class, var3, var4);
         var1.put("ServletReloadCheckSecs", var2);
         var2.setValue("description", "<p>The amount of time (in seconds) that WebLogic Server waits to check if a servlet was modified and needs to be reloaded.</p>  <p>How often WebLogic checks whether a servlet has been modified, and if so reloads it. When the value is set to -1, the servlet is never reloaded, and when the vlue is set to 0, the servlet is reloaded after each check.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(1));
         var2.setValue("deprecated", "8.1.0.0 Use weblogic.xml or update using console. ");
      }

      if (!var1.containsKey("SingleThreadedServletPoolSize")) {
         var3 = "getSingleThreadedServletPoolSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSingleThreadedServletPoolSize";
         }

         var2 = new PropertyDescriptor("SingleThreadedServletPoolSize", WebAppComponentMBean.class, var3, var4);
         var1.put("SingleThreadedServletPoolSize", var2);
         var2.setValue("description", "<p>This provides size of the pool used for single threaded mode servlets. It</p>  <p>defines the size of the pool used for SingleThreadedModel instance pools.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(5));
         var2.setValue("deprecated", "8.1.0.0 Use weblogic.xml or update using console. ");
      }

      if (!var1.containsKey("Targets")) {
         var3 = "getTargets";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTargets";
         }

         var2 = new PropertyDescriptor("Targets", WebAppComponentMBean.class, var3, var4);
         var1.put("Targets", var2);
         var2.setValue("description", "<p>You must select a target on which an MBean will be deployed from this list of the targets in the current domain on which this item can be deployed. Targets must be either servers or clusters. The deployment will only occur once if deployments overlap.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("adder", "addTarget");
         var2.setValue("remover", "removeTarget");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("7.0.0.0.", (String)null, this.targetVersion) && !var1.containsKey("VirtualHosts")) {
         var3 = "getVirtualHosts";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setVirtualHosts";
         }

         var2 = new PropertyDescriptor("VirtualHosts", WebAppComponentMBean.class, var3, var4);
         var1.put("VirtualHosts", var2);
         var2.setValue("description", "<p>Provides a means to target your deployments to specific virtual hosts.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("remover", "removeVirtualHost");
         var2.setValue("adder", "addVirtualHost");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "7.0.0.0.");
      }

      if (!var1.containsKey("WebServers")) {
         var3 = "getWebServers";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWebServers";
         }

         var2 = new PropertyDescriptor("WebServers", WebAppComponentMBean.class, var3, var4);
         var1.put("WebServers", var2);
         var2.setValue("description", "<p>Returns a list of the targets on which this deployment is deployed.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("adder", "addWebServer");
         var2.setValue("remover", "removeWebServer");
         var2.setValue("deprecated", "7.0.0.0 This attribute is being replaced by VirtualHosts attribute. To target  an actual web server, the ComponentMBean.Targets attribute should be used. ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("IndexDirectoryEnabled")) {
         var3 = "isIndexDirectoryEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setIndexDirectoryEnabled";
         }

         var2 = new PropertyDescriptor("IndexDirectoryEnabled", WebAppComponentMBean.class, var3, var4);
         var1.put("IndexDirectoryEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the target should automatically generate an HTML directory listing if no suitable index file is found.</p>  <p>Indicates whether or not to automatically generate an HTML directory listing if no suitable index file is found.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("deprecated", "8.1.0.0 Use weblogic.xml or update using console. ");
      }

      if (!var1.containsKey("PreferWebInfClasses")) {
         var3 = "isPreferWebInfClasses";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPreferWebInfClasses";
         }

         var2 = new PropertyDescriptor("PreferWebInfClasses", WebAppComponentMBean.class, var3, var4);
         var1.put("PreferWebInfClasses", var2);
         var2.setValue("description", "<p>Specifies whether classes loaded in the WEB-INF directory will be loaded in preference to classes loaded in the application or system calssloader.</p>  <p>Deprecated the setting from console beginning with version 8.1. You must now set this in weblogic.xml.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("deprecated", "8.0.0.0 Use weblogic.xml. ");
      }

      if (!var1.containsKey("ServletExtensionCaseSensitive")) {
         var3 = "isServletExtensionCaseSensitive";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setServletExtensionCaseSensitive";
         }

         var2 = new PropertyDescriptor("ServletExtensionCaseSensitive", WebAppComponentMBean.class, var3, var4);
         var1.put("ServletExtensionCaseSensitive", var2);
         var2.setValue("description", "<p>Indicates whether servlet extensions should be treated as though they are lower case even if they are written in upper case.</p>  <p>If True, the server treats all .extensions except .html as lower case. This is only necessary on WindowsNT. This property is being deprecated. The extension comparision will be case insensitive by default on Win32.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("deprecated", " ");
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("SessionMonitoringEnabled")) {
         var3 = "isSessionMonitoringEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSessionMonitoringEnabled";
         }

         var2 = new PropertyDescriptor("SessionMonitoringEnabled", WebAppComponentMBean.class, var3, var4);
         var1.put("SessionMonitoringEnabled", var2);
         var2.setValue("description", "<p>Specifies whether runtime MBeans will be created for session monitoring.</p>  <p>If true, then runtime MBeans will be created for sessions; otherwise, they will not.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("deprecated", "8.0.0.0 Use weblogic.xml or update using console. ");
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = WebAppComponentMBean.class.getMethod("addTarget", TargetMBean.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("target", "The feature to be added to the Target attribute ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>You can add a target to specify additional servers on which the deployment can be deployed. The targets must be either clusters or servers.</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "Targets");
      }

      var3 = WebAppComponentMBean.class.getMethod("addWebServer", WebServerMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("target", "The feature to be added to the WebServer attribute ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "7.0.0.0 This attribute is being replaced by VirtualHosts attribute ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>This adds a target to the list of web servers to which a deployment may be targeted.</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "WebServers");
      }

      var3 = WebAppComponentMBean.class.getMethod("removeTarget", TargetMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("target", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes the value of the addTarget attribute.</p> ");
         String[] var6 = new String[]{BeanInfoHelper.encodeEntities("#addTarget")};
         var2.setValue("see", var6);
         var2.setValue("role", "collection");
         var2.setValue("property", "Targets");
      }

      var3 = WebAppComponentMBean.class.getMethod("removeWebServer", WebServerMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("target", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "7.0.0.0 This attribute is being replaced by VirtualHosts attribute ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>This removes a target from the list of web servers which may be targeted for deployments.</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "WebServers");
      }

      if (BeanInfoHelper.isVersionCompliant("7.0.0.0.", (String)null, this.targetVersion)) {
         var3 = WebAppComponentMBean.class.getMethod("addVirtualHost", VirtualHostMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("target", "The feature to be added to the VirtualHost attribute ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "7.0.0.0.");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Used to add a virtual host to the list of virtual hosts to which deployments may be targeted.</p> ");
            var2.setValue("role", "collection");
            var2.setValue("property", "VirtualHosts");
            var2.setValue("since", "7.0.0.0.");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("7.0.0.0.", (String)null, this.targetVersion)) {
         var3 = WebAppComponentMBean.class.getMethod("removeVirtualHost", VirtualHostMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("target", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "7.0.0.0.");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Used to remove a virtual host from the list of virtual hosts to which deployments may be targeted.</p> ");
            var2.setValue("role", "collection");
            var2.setValue("property", "VirtualHosts");
            var2.setValue("since", "7.0.0.0.");
         }
      }

   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      MethodDescriptor var2;
      Method var3;
      ParameterDescriptor[] var4;
      String var5;
      if (BeanInfoHelper.isVersionCompliant("7.0.0.0", (String)null, this.targetVersion)) {
         var3 = WebAppComponentMBean.class.getMethod("activated", TargetMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("target", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "7.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Indicates whether component has been activated on a server</p> ");
            var2.setValue("role", "operation");
            var2.setValue("since", "7.0.0.0");
         }
      }

      var3 = WebAppComponentMBean.class.getMethod("refreshDDsIfNeeded", String[].class);
      String var6 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var6)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var6, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WebAppComponentMBean.class.getMethod("freezeCurrentValue", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has not been set explicitly, and if the attribute has a default value, this operation forces the MBean to persist the default value.</p>  <p>Unless you use this operation, the default value is not saved and is subject to change if you update to a newer release of WebLogic Server. Invoking this operation isolates this MBean from the effects of such changes.</p>  <dl> <dt>Note:</dt>  <dd> <p>To insure that you are freezing the default value, invoke the <code>restoreDefaultValue</code> operation before you invoke this.</p> </dd> </dl>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute for which some other value has been set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = WebAppComponentMBean.class.getMethod("restoreDefaultValue", String.class);
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
