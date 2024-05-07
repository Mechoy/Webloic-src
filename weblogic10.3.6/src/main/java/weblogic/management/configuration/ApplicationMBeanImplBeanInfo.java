package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class ApplicationMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ApplicationMBean.class;

   public ApplicationMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ApplicationMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ApplicationMBeanImpl.class;
      } catch (Throwable var6) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("obsolete", "9.0.0.0");
      var2.setValue("deprecated", "9.0.0.0 Replaced by {@link AppDeploymentMBean} ");
      var2.setValue("notificationTranslator", "weblogic.management.deploy.internal.NotificationTranslator");
      String[] var3 = new String[]{BeanInfoHelper.encodeEntities("TargetMBean"), BeanInfoHelper.encodeEntities("EJBComponentMBean"), BeanInfoHelper.encodeEntities("WebAppComponentMBean"), BeanInfoHelper.encodeEntities("ConnectorComponentMBean")};
      var2.setValue("see", var3);
      var2.setValue("package", "weblogic.management.configuration");
      String var4 = (new String("An application represents a J2EE application contained in an EAR file or EAR directory. The EAR file contains a set of components such as WAR, EJB, and RAR connector components, each of which can be deployed on one or more targets. A target is a server or a cluster.  If the application is provided as a standalone module, then this MBean is a synthetic wrapper application only.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var4);
      var2.setValue("description", var4);
      String[] var5 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var5);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.ApplicationMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("AltDescriptorPath")) {
         var3 = "getAltDescriptorPath";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAltDescriptorPath";
         }

         var2 = new PropertyDescriptor("AltDescriptorPath", ApplicationMBean.class, var3, var4);
         var1.put("AltDescriptorPath", var2);
         var2.setValue("description", "<p>A path on the file system for the application descriptor for this application. If null, the usual location within the ear is used (META-INF/application.xml);</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("AltWLSDescriptorPath")) {
         var3 = "getAltWLSDescriptorPath";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAltWLSDescriptorPath";
         }

         var2 = new PropertyDescriptor("AltWLSDescriptorPath", ApplicationMBean.class, var3, var4);
         var1.put("AltWLSDescriptorPath", var2);
         var2.setValue("description", "<p>A path on the file system for the WLS-specific application descriptor for this application. If null, the usual location within the EAR file is used (META-INF/weblogic-application.xml);</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Components")) {
         var3 = "getComponents";
         var4 = null;
         var2 = new PropertyDescriptor("Components", ApplicationMBean.class, var3, var4);
         var1.put("Components", var2);
         var2.setValue("description", "<p>The J2EE modules (components) that make up this application.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ConnectorComponents")) {
         var3 = "getConnectorComponents";
         var4 = null;
         var2 = new PropertyDescriptor("ConnectorComponents", ApplicationMBean.class, var3, var4);
         var1.put("ConnectorComponents", var2);
         var2.setValue("description", "<p>Returns the Connector components that make up this application. Components represent the J2EE modules associated with this application.<p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createConnectorComponent");
         var2.setValue("destroyer", "destroyConnectorComponent");
      }

      if (BeanInfoHelper.isVersionCompliant("7.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("DeploymentTimeout")) {
         var3 = "getDeploymentTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDeploymentTimeout";
         }

         var2 = new PropertyDescriptor("DeploymentTimeout", ApplicationMBean.class, var3, var4);
         var1.put("DeploymentTimeout", var2);
         var2.setValue("description", "<p>Milliseconds granted for a cluster deployment task on this application. If any deployment tasks remain active for longer, the task will be cancelled.</p>  <p>The larger the application, the larger the timeout value should be, as the gating factor is associated with download time and processing time required to load the application files.</p>  <p>A server instance checks for timed out deployments about once a minute.</p>  <p>Only cluster deployments can be timed out.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(3600000));
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.deploy.api.spi.DeploymentOptions#getClusterDeploymentTimeout()} ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "7.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("7.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("DeploymentType")) {
         var3 = "getDeploymentType";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDeploymentType";
         }

         var2 = new PropertyDescriptor("DeploymentType", ApplicationMBean.class, var3, var4);
         var1.put("DeploymentType", var2);
         var2.setValue("description", "<p>Specifies the category of this application. This attribute will be derived if not specified in the configuration.</p> ");
         setPropertyDescriptorDefault(var2, ApplicationMBean.TYPE_UNKNOWN);
         var2.setValue("legalValues", new Object[]{ApplicationMBean.TYPE_EAR, ApplicationMBean.TYPE_EXPLODED_EAR, ApplicationMBean.TYPE_COMPONENT, ApplicationMBean.TYPE_EXPLODED_COMPONENT, ApplicationMBean.TYPE_UNKNOWN});
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "7.0.0.0");
      }

      if (!var1.containsKey("EJBComponents")) {
         var3 = "getEJBComponents";
         var4 = null;
         var2 = new PropertyDescriptor("EJBComponents", ApplicationMBean.class, var3, var4);
         var1.put("EJBComponents", var2);
         var2.setValue("description", "<p> Returns the EJB components that make up this application. Components represent the J2EE modules associated with this application. <p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyEJBComponent");
         var2.setValue("creator", "createEJBComponent");
      }

      if (!var1.containsKey("FullPath")) {
         var3 = "getFullPath";
         var4 = null;
         var2 = new PropertyDescriptor("FullPath", ApplicationMBean.class, var3, var4);
         var1.put("FullPath", var2);
         var2.setValue("description", "<p>The fully qualified source path of an application on an Administration Server.</p> ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("InternalType")) {
         var3 = "getInternalType";
         var4 = null;
         var2 = new PropertyDescriptor("InternalType", ApplicationMBean.class, var3, var4);
         var1.put("InternalType", var2);
         var2.setValue("description", "<p>Returns the internal type of the application. (EAR, COMPONENT, EXPLODED_EAR, EXPLODED_COMPONENT) This is needed because j2ee.Component needs to be able to determine how the application is packaged in order to correctly deploy it on the managed server.</p> ");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("JDBCPoolComponents")) {
         var3 = "getJDBCPoolComponents";
         var4 = null;
         var2 = new PropertyDescriptor("JDBCPoolComponents", ApplicationMBean.class, var3, var4);
         var1.put("JDBCPoolComponents", var2);
         var2.setValue("description", "<p>Returns the JDBCPool components (JDBC modules) included in this application. </p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createJDBCPoolComponent");
         var2.setValue("destroyer", "destroyJDBCPoolComponent");
         var2.setValue("exclude", Boolean.TRUE);
      }

      String[] var5;
      if (BeanInfoHelper.isVersionCompliant("7.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("LoadOrder")) {
         var3 = "getLoadOrder";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLoadOrder";
         }

         var2 = new PropertyDescriptor("LoadOrder", ApplicationMBean.class, var3, var4);
         var1.put("LoadOrder", var2);
         var2.setValue("description", "<p>A numerical value that indicates when this module or application is deployed, relative to other deployable modules and applications. Modules with lower Load Order values are deployed before those with higher values. (Requires that you enable the two-phase deployment protocol.)</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#isTwoPhase")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(100));
         var2.setValue("since", "7.0.0.0");
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", ApplicationMBean.class, var3, var4);
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

         var2 = new PropertyDescriptor("Notes", ApplicationMBean.class, var3, var4);
         var1.put("Notes", var2);
         var2.setValue("description", "<p>Optional information that you can include to describe this configuration.</p>  <p>WebLogic Server saves this note in the domain's configuration file (<code>config.xml</code>) as XML PCDATA. All left angle brackets (&lt;) are converted to the XML entity <code>&amp;lt;</code>. Carriage returns/line feeds are preserved.</p>  <dl> <dt>Note:</dt>  <dd> <p>If you create or edit a note from the Administration Console, the Administration Console does not preserve carriage returns/line feeds.</p> </dd> </dl> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Path")) {
         var3 = "getPath";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPath";
         }

         var2 = new PropertyDescriptor("Path", ApplicationMBean.class, var3, var4);
         var1.put("Path", var2);
         var2.setValue("description", "<p>The URI, located on the Administration Server, of the original source files for this application.</p>  <p>Relative paths are based on the root of the Administration Server installation directory. It is highly recommended that you use absolute paths to minimize possible issues when upgrading the server.</p>  <p>If the application is not being staged (StagingMode==nostage) then the path must be valid on the target server.</p>  <p>The path to an Enterprise application (EAR) is the location of the EAR file or the root of the EAR if it is unarchived, e.g., Path=\"myapps/app.ear\" is valid. If the application is a standalone module, then the path is the parent directory of the module. For example, if the module is located at myapps/webapp/webapp.war, the Path=\"myapps/webapp\" is correct, whereas Path=\"myapps/webapp/webapp.war\" is incorrect.</p> ");
      }

      if (BeanInfoHelper.isVersionCompliant("7.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("StagedTargets")) {
         var3 = "getStagedTargets";
         var4 = null;
         var2 = new PropertyDescriptor("StagedTargets", ApplicationMBean.class, var3, var4);
         var1.put("StagedTargets", var2);
         var2.setValue("description", "<p>List of servers on which this application is known to be staged. This makes no distinction regarding the version or state of the staged files, just that they are staged. The array returned contains the names of the target servers. This list should not include cluster names.</p> ");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("since", "7.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("7.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("StagingMode")) {
         var3 = "getStagingMode";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStagingMode";
         }

         var2 = new PropertyDescriptor("StagingMode", ApplicationMBean.class, var3, var4);
         var1.put("StagingMode", var2);
         var2.setValue("description", "<p>The mode that specifies whether an application's files are copied from a source on the Administration Server to the Managed Server's staging area during application preparation. Staging mode for an application can only be set the first time the application is deployed. Once staging mode for an application is set, it cannot be changed while the application is configured in the domain. The only way to change staging mode is to undeploy then redeploy the application.</p>  <p>Staging involves distributing the application files from the Administration Server to the targeted Managed Servers staging directory. This attribute is used to override the Managed Server's StagingMode attribute.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean")};
         var2.setValue("see", var5);
         var2.setValue("legalValues", new Object[]{ApplicationMBean.DEFAULT_STAGE, "nostage", "stage", "external_stage"});
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "7.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("7.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("StagingPath")) {
         var3 = "getStagingPath";
         var4 = null;
         var2 = new PropertyDescriptor("StagingPath", ApplicationMBean.class, var3, var4);
         var1.put("StagingPath", var2);
         var2.setValue("description", "<p>The directory that a Managed Server uses to prepare and activate an application.</p>  <p>The directory path is relative to the Managed Server's Staging Path. It is derived from the Path attribute, and depends on whether the application is being staged. If the Path attribute for application, myapp, is foo.ear, the staging path is set to myapp/foo.ear. If the path is C:/myapp.ear, the staging path is myapp/myapp.ear. If the application is not being staged (StagingMode==nostage), then the staging path is the same as the Path attribute. If this application is not being staged, the staging path is equivalent to the source path (Path attribute).</p> ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("since", "7.0.0.0");
      }

      if (!var1.containsKey("WebAppComponents")) {
         var3 = "getWebAppComponents";
         var4 = null;
         var2 = new PropertyDescriptor("WebAppComponents", ApplicationMBean.class, var3, var4);
         var1.put("WebAppComponents", var2);
         var2.setValue("description", "<p>The WebApp components (J2EE modules) that make up this application.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyWebAppComponent");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("WebServiceComponents")) {
         var3 = "getWebServiceComponents";
         var4 = null;
         var2 = new PropertyDescriptor("WebServiceComponents", ApplicationMBean.class, var3, var4);
         var1.put("WebServiceComponents", var2);
         var2.setValue("description", "<p>Returns the WebService components that make up this application. Components represent the J2EE modules associated with this application. <p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createWebServiceComponent");
         var2.setValue("destroyer", "destroyWebServiceComponent");
      }

      if (!var1.containsKey("Deployed")) {
         var3 = "isDeployed";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDeployed";
         }

         var2 = new PropertyDescriptor("Deployed", ApplicationMBean.class, var3, var4);
         var1.put("Deployed", var2);
         var2.setValue("description", "<p>The deployed attribute is no longer supported as of version 9.x It remains here to support parsing of existing configuration files in which this value was stored</p> ");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("InternalApp")) {
         var3 = "isInternalApp";
         var4 = null;
         var2 = new PropertyDescriptor("InternalApp", ApplicationMBean.class, var3, var4);
         var1.put("InternalApp", var2);
         var2.setValue("description", "<p>Indicates whether this application is an internal application. Such applications are not displayed in the console. For OAM internal use only.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("transient", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("7.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("TwoPhase")) {
         var3 = "isTwoPhase";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTwoPhase";
         }

         var2 = new PropertyDescriptor("TwoPhase", ApplicationMBean.class, var3, var4);
         var1.put("TwoPhase", var2);
         var2.setValue("description", "<p>Specifies whether this application is deployed using the two-phase deployment protocol.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.runtime.DeployerRuntimeMBean")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("deprecated", "Always returns true ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "7.0.0.0");
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      MethodDescriptor var2;
      Method var3;
      String var4;
      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = ApplicationMBean.class.getMethod("destroyWebAppComponent", WebAppComponentMBean.class);
         var4 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var4)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("since", "9.0.0.0");
            var1.put(var4, var2);
            var2.setValue("description", "<p>destroys WebAppComponents</p> ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "WebAppComponents");
            var2.setValue("since", "9.0.0.0");
         }
      }

      String var5;
      ParameterDescriptor[] var6;
      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = ApplicationMBean.class.getMethod("createEJBComponent", String.class);
         var6 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var6);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory to create EJBComponentMBean instance in the domain<p>  This method is here to force the binding code to generate correctly. ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "EJBComponents");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = ApplicationMBean.class.getMethod("destroyEJBComponent", EJBComponentMBean.class);
         var4 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var4)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("since", "9.0.0.0");
            var1.put(var4, var2);
            var2.setValue("description", "<p>destroys EJBComponents</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "EJBComponents");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = ApplicationMBean.class.getMethod("createConnectorComponent", String.class);
         var6 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var6);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory to create ConnectorComponentMBean instance in the domain<p>  This method is here to force the binding code to generate correctly. ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "ConnectorComponents");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = ApplicationMBean.class.getMethod("destroyConnectorComponent", ConnectorComponentMBean.class);
         var4 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var4)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("since", "9.0.0.0");
            var1.put(var4, var2);
            var2.setValue("description", "<p>destroys ConnectorComponents</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "ConnectorComponents");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = ApplicationMBean.class.getMethod("createWebServiceComponent", String.class);
         var6 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var6);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory to create WebServiceComponentMBean instance in the domain<p>  This method is here to force the binding code to generate correctly. ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "WebServiceComponents");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = ApplicationMBean.class.getMethod("destroyWebServiceComponent", WebServiceComponentMBean.class);
         var4 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var4)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("since", "9.0.0.0");
            var1.put(var4, var2);
            var2.setValue("description", "<p>destroys WebServiceComponents</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "WebServiceComponents");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = ApplicationMBean.class.getMethod("createJDBCPoolComponent", String.class);
         var6 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var6);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory to create JDBCPoolComponentMBean instances in the domain<p>  This method is here to force the binding code to generate correctly. ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "JDBCPoolComponents");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = ApplicationMBean.class.getMethod("destroyJDBCPoolComponent", JDBCPoolComponentMBean.class);
         var4 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var4)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("since", "9.0.0.0");
            var1.put(var4, var2);
            var2.setValue("description", "<p>destroys JDBCPoolComponent</p> ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "JDBCPoolComponents");
            var2.setValue("since", "9.0.0.0");
         }
      }

   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = ApplicationMBean.class.getMethod("lookupWebAppComponent", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "finder");
         var2.setValue("property", "WebAppComponents");
      }

      var3 = ApplicationMBean.class.getMethod("lookupEJBComponent", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "EJBComponents");
      }

      var3 = ApplicationMBean.class.getMethod("lookupConnectorComponent", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "ConnectorComponents");
      }

      var3 = ApplicationMBean.class.getMethod("lookupWebServiceComponent", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "WebServiceComponents");
      }

      var3 = ApplicationMBean.class.getMethod("lookupJDBCPoolComponent", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns a JDBCComponentMBean with the specified name.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "finder");
         var2.setValue("property", "JDBCPoolComponents");
      }

   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = ApplicationMBean.class.getMethod("freezeCurrentValue", String.class);
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

      var3 = ApplicationMBean.class.getMethod("restoreDefaultValue", String.class);
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

      if (BeanInfoHelper.isVersionCompliant("7.0.0.0", (String)null, this.targetVersion)) {
         var3 = ApplicationMBean.class.getMethod("stagingEnabled", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("server", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "7.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Convenience method for determining whether this application is to be staged on a particular server.</p> ");
            var2.setValue("transient", Boolean.TRUE);
            var2.setValue("role", "operation");
            var2.setValue("since", "7.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("7.0.0.0", (String)null, this.targetVersion)) {
         var3 = ApplicationMBean.class.getMethod("staged", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("server", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "7.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Convenience method for determining whether this application is currently staged on a particular server.</p> ");
            var2.setValue("transient", Boolean.TRUE);
            var2.setValue("role", "operation");
            var2.setValue("since", "7.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("7.0.0.0", (String)null, this.targetVersion)) {
         var3 = ApplicationMBean.class.getMethod("useStagingDirectory", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("server", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "7.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Convenience method for determining where the file will be loaded from on the managed servers.</p> ");
            var2.setValue("transient", Boolean.TRUE);
            var2.setValue("role", "operation");
            var2.setValue("since", "7.0.0.0");
         }
      }

      var3 = ApplicationMBean.class.getMethod("refreshDDsIfNeeded", String[].class, String[].class);
      String var6 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var6)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var6, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = ApplicationMBean.class.getMethod("returnDeployableUnit");
      var6 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var6)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var6, var2);
         var2.setValue("description", " ");
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
