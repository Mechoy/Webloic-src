package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class DomainMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = DomainMBean.class;

   public DomainMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public DomainMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = DomainMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>A WebLogic Domain is a group of servers and/or clusters which are administered as a group.</p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.DomainMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("AdminConsole")) {
         var3 = "getAdminConsole";
         var4 = null;
         var2 = new PropertyDescriptor("AdminConsole", DomainMBean.class, var3, var4);
         var1.put("AdminConsole", var2);
         var2.setValue("description", "get AdminConsoleMBean object, a console specific MBean to configure weblogic administration console attributes. ");
         var2.setValue("relationship", "containment");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("AdminServerMBean")) {
         var3 = "getAdminServerMBean";
         var4 = null;
         var2 = new PropertyDescriptor("AdminServerMBean", DomainMBean.class, var3, var4);
         var1.put("AdminServerMBean", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createAdminServerMBean");
         var2.setValue("destroyer", "destroyAdminServerMBean");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (!var1.containsKey("AdminServerName")) {
         var3 = "getAdminServerName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAdminServerName";
         }

         var2 = new PropertyDescriptor("AdminServerName", DomainMBean.class, var3, var4);
         var1.put("AdminServerName", var2);
         var2.setValue("description", " ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      String[] var5;
      if (!var1.containsKey("AdministrationPort")) {
         var3 = "getAdministrationPort";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAdministrationPort";
         }

         var2 = new PropertyDescriptor("AdministrationPort", DomainMBean.class, var3, var4);
         var1.put("AdministrationPort", var2);
         var2.setValue("description", "<p>The common secure administration port for this WebLogic Server domain. (Requires you to enable the administration port.)</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#isAdministrationPortEnabled"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#getAdministrationPort")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(9002));
         var2.setValue("legalMax", new Integer(65535));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("AdministrationProtocol")) {
         var3 = "getAdministrationProtocol";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAdministrationProtocol";
         }

         var2 = new PropertyDescriptor("AdministrationProtocol", DomainMBean.class, var3, var4);
         var1.put("AdministrationProtocol", var2);
         var2.setValue("description", "<p>The default protocol for communicating through the administration port or administration channels. (Requires you to enable the administration port or to create an administration channel.)</p>  <p>If requests through the administration port or an administration channel do not specify a protocol, WebLogic Server uses the protocol specified here.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#isAdministrationPortEnabled")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, "t3s");
         var2.setValue("legalValues", new Object[]{"t3s", "https", "iiops", "t3", "http", "iiop"});
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("AppDeployments")) {
         var3 = "getAppDeployments";
         var4 = null;
         var2 = new PropertyDescriptor("AppDeployments", DomainMBean.class, var3, var4);
         var1.put("AppDeployments", var2);
         var2.setValue("description", "<p>The collection of deployable entities in this domain. This replaces the Application in previous versions. </p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("Applications")) {
         var3 = "getApplications";
         var4 = null;
         var2 = new PropertyDescriptor("Applications", DomainMBean.class, var3, var4);
         var1.put("Applications", var2);
         var2.setValue("description", "<p>Define applications for this Domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createApplication");
         var2.setValue("destroyer", "destroyApplication");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (!var1.containsKey("ArchiveConfigurationCount")) {
         var3 = "getArchiveConfigurationCount";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setArchiveConfigurationCount";
         }

         var2 = new PropertyDescriptor("ArchiveConfigurationCount", DomainMBean.class, var3, var4);
         var1.put("ArchiveConfigurationCount", var2);
         var2.setValue("description", "<p>The number of archival versions of <tt>config.xml</tt> saved by the Administration Server each time the domain configuration is modified.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
      }

      if (!var1.containsKey("BasicRealms")) {
         var3 = "getBasicRealms";
         var4 = null;
         var2 = new PropertyDescriptor("BasicRealms", DomainMBean.class, var3, var4);
         var1.put("BasicRealms", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "reference");
         var2.setValue("deprecated", "7.0.0.0 ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("BridgeDestinations")) {
         var3 = "getBridgeDestinations";
         var4 = null;
         var2 = new PropertyDescriptor("BridgeDestinations", DomainMBean.class, var3, var4);
         var1.put("BridgeDestinations", var2);
         var2.setValue("description", "<p>Return the BridgeDestinations for this Domain.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyBridgeDestination");
         var2.setValue("creator", "createBridgeDestination");
         var2.setValue("deprecated", "9.0.0.0 ");
      }

      if (!var1.containsKey("CachingRealms")) {
         var3 = "getCachingRealms";
         var4 = null;
         var2 = new PropertyDescriptor("CachingRealms", DomainMBean.class, var3, var4);
         var1.put("CachingRealms", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createCachingRealm");
         var2.setValue("destroyer", "destroyCachingRealm");
         var2.setValue("deprecated", "7.0.0.0 ");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("Clusters")) {
         var3 = "getClusters";
         var4 = null;
         var2 = new PropertyDescriptor("Clusters", DomainMBean.class, var3, var4);
         var1.put("Clusters", var2);
         var2.setValue("description", "<p>Returns the ClusterMBeans representing the cluster that have been configured to be part of this domain.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createCluster");
         var2.setValue("destroyer", "destroyCluster");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.3.0", (String)null, this.targetVersion) && !var1.containsKey("CoherenceClusterSystemResources")) {
         var3 = "getCoherenceClusterSystemResources";
         var4 = null;
         var2 = new PropertyDescriptor("CoherenceClusterSystemResources", DomainMBean.class, var3, var4);
         var1.put("CoherenceClusterSystemResources", var2);
         var2.setValue("description", "<p>The CoherenceClusterSystemResourceMBeans that have been defined for this domain. </p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createCoherenceClusterSystemResource");
         var2.setValue("destroyer", "destroyCoherenceClusterSystemResource");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "10.3.3.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.4.0", (String)null, this.targetVersion) && !var1.containsKey("CoherenceServers")) {
         var3 = "getCoherenceServers";
         var4 = null;
         var2 = new PropertyDescriptor("CoherenceServers", DomainMBean.class, var3, var4);
         var1.put("CoherenceServers", var2);
         var2.setValue("description", "<p>The CoherenceServerMBeans that have been defined for this domain. </p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyCoherenceServer");
         var2.setValue("creator", "createCoherenceServer");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "10.3.4.0");
      }

      if (!var1.containsKey("ConfigurationAuditType")) {
         var3 = "getConfigurationAuditType";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConfigurationAuditType";
         }

         var2 = new PropertyDescriptor("ConfigurationAuditType", DomainMBean.class, var3, var4);
         var1.put("ConfigurationAuditType", var2);
         var2.setValue("description", "Returns the criteria used for auditing configuration events  (configuration changes and other operations): <ul> <li><code>CONFIG_CHANGE_NONE</code> Configuration events will neither be written to the server log or directed to the Security Audit Framework.</li> <li><code>CONFIG_CHANGE_LOG</code> Configuration events will be written to the server log.</li> <li><code>CONFIG_CHANGE_AUDIT</code>Configuration events will be directed to the Security Audit Framework.</li> <li><code>CONFIG_CHANGE_LOG_AND_AUDIT</code> Configuration events will be written to the server log and directed to the Security Audit Framework.</li> </ul> ");
         setPropertyDescriptorDefault(var2, "none");
         var2.setValue("legalValues", new Object[]{"none", "log", "audit", "logaudit"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ConfigurationVersion")) {
         var3 = "getConfigurationVersion";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConfigurationVersion";
         }

         var2 = new PropertyDescriptor("ConfigurationVersion", DomainMBean.class, var3, var4);
         var1.put("ConfigurationVersion", var2);
         var2.setValue("description", "<p>The release identifier for the configuration. This identifier will be used to indicate the version of the configuration. All server generated configurations will be established with the release identifier of the running server. The form of the version is major.minor.servicepack.rollingpatch. Not all parts of the version are required. i.e. \"7\" is acceptable.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ConsoleContextPath")) {
         var3 = "getConsoleContextPath";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConsoleContextPath";
         }

         var2 = new PropertyDescriptor("ConsoleContextPath", DomainMBean.class, var3, var4);
         var1.put("ConsoleContextPath", var2);
         var2.setValue("description", "<p>The context path that you want to use in URLs that specify the Administration Console. (Requires you to enable the Administration Console for the current domain.)</p>  <p>To access the Administration Console, you use the following URL: http://<i>listen-addess</i>:<i>listen-port</i>/<i>context-path</i>. For example, if you set the context path to <code>myconsole</code>, then you use the following URL to access the Administration Console: <code>http://localhost:7001/myconsole</code>.</p>  <p>To specify the listen address and listen port that you use to access the Administration Console, configure the listen address and listen port of the Administration Server.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#isConsoleEnabled"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#getListenAddress"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#getListenPort")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, "console");
      }

      if (!var1.containsKey("ConsoleExtensionDirectory")) {
         var3 = "getConsoleExtensionDirectory";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConsoleExtensionDirectory";
         }

         var2 = new PropertyDescriptor("ConsoleExtensionDirectory", DomainMBean.class, var3, var4);
         var1.put("ConsoleExtensionDirectory", var2);
         var2.setValue("description", "<p>Returns the directory path that console extensions are loaded from.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#isConsoleEnabled")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, "console-ext");
      }

      if (!var1.containsKey("CustomRealms")) {
         var3 = "getCustomRealms";
         var4 = null;
         var2 = new PropertyDescriptor("CustomRealms", DomainMBean.class, var3, var4);
         var1.put("CustomRealms", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyCustomRealm");
         var2.setValue("creator", "createCustomRealm");
         var2.setValue("deprecated", "7.0.0.0 ");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.1.0.0", (String)null, this.targetVersion) && !var1.containsKey("CustomResources")) {
         var3 = "getCustomResources";
         var4 = null;
         var2 = new PropertyDescriptor("CustomResources", DomainMBean.class, var3, var4);
         var1.put("CustomResources", var2);
         var2.setValue("description", "<p>Returns the JMSSystemResourceMBeans that have been defined for this domain. </p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createCustomResource");
         var2.setValue("creator", "createCustomResource");
         var2.setValue("destroyer", "destroyCustomResource");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.1.0.0");
      }

      if (!var1.containsKey("DeploymentConfiguration")) {
         var3 = "getDeploymentConfiguration";
         var4 = null;
         var2 = new PropertyDescriptor("DeploymentConfiguration", DomainMBean.class, var3, var4);
         var1.put("DeploymentConfiguration", var2);
         var2.setValue("description", "<p>Return the deployment configuration for this Domain.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("Deployments")) {
         var3 = "getDeployments";
         var4 = null;
         var2 = new PropertyDescriptor("Deployments", DomainMBean.class, var3, var4);
         var1.put("Deployments", var2);
         var2.setValue("description", "Returns the DeploymentsMBeans representing the deployments that have been deployed to be part of this domain. ");
         var2.setValue("relationship", "reference");
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("DomainLibraries")) {
         var3 = "getDomainLibraries";
         var4 = null;
         var2 = new PropertyDescriptor("DomainLibraries", DomainMBean.class, var3, var4);
         var1.put("DomainLibraries", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("DomainLogFilters")) {
         var3 = "getDomainLogFilters";
         var4 = null;
         var2 = new PropertyDescriptor("DomainLogFilters", DomainMBean.class, var3, var4);
         var1.put("DomainLogFilters", var2);
         var2.setValue("description", "Gets the array of domain log filters defined in the domain ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createDomainLogFilter");
         var2.setValue("destroyer", "destroyDomainLogFilter");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (!var1.containsKey("DomainVersion")) {
         var3 = "getDomainVersion";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDomainVersion";
         }

         var2 = new PropertyDescriptor("DomainVersion", DomainMBean.class, var3, var4);
         var1.put("DomainVersion", var2);
         var2.setValue("description", "<p>Defines the common version of all servers in a domain. In a domain containing servers that are not all at the same release version, this attribute is used to determine the feature level that servers will assume.  The value must be less than or equal to the version of any managed server in the domain.  If this value is not equal to the version of the release version of the admin server, then the admin server will not be allowed to make modifications to the configuration. ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("EJBContainer")) {
         var3 = "getEJBContainer";
         var4 = null;
         var2 = new PropertyDescriptor("EJBContainer", DomainMBean.class, var3, var4);
         var1.put("EJBContainer", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createEJBContainer");
         var2.setValue("destroyer", "destroyEJBContainer");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("EmbeddedLDAP")) {
         var3 = "getEmbeddedLDAP";
         var4 = null;
         var2 = new PropertyDescriptor("EmbeddedLDAP", DomainMBean.class, var3, var4);
         var1.put("EmbeddedLDAP", var2);
         var2.setValue("description", "<p>Returns the embedded LDAP configuration for this domain.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("ErrorHandlings")) {
         var3 = "getErrorHandlings";
         var4 = null;
         var2 = new PropertyDescriptor("ErrorHandlings", DomainMBean.class, var3, var4);
         var1.put("ErrorHandlings", var2);
         var2.setValue("description", "<p>Get ErrorHandlingMBean for this Domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyErrorHandling");
         var2.setValue("creator", "createErrorHandling");
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("FileRealms")) {
         var3 = "getFileRealms";
         var4 = null;
         var2 = new PropertyDescriptor("FileRealms", DomainMBean.class, var3, var4);
         var1.put("FileRealms", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyFileRealm");
         var2.setValue("creator", "createFileRealm");
         var2.setValue("deprecated", "7.0.0.0 ");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("FileStores")) {
         var3 = "getFileStores";
         var4 = null;
         var2 = new PropertyDescriptor("FileStores", DomainMBean.class, var3, var4);
         var1.put("FileStores", var2);
         var2.setValue("description", "<p>Return file stores defined in this domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createFileStore");
         var2.setValue("destroyer", "destroyFileStore");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("FileT3s")) {
         var3 = "getFileT3s";
         var4 = null;
         var2 = new PropertyDescriptor("FileT3s", DomainMBean.class, var3, var4);
         var1.put("FileT3s", var2);
         var2.setValue("description", "<p>Returns the FileT3MBeans representing the FileT3s that have been configured to be part of this domain.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createFileT3");
         var2.setValue("destroyer", "destroyFileT3");
         var2.setValue("deprecated", "8.1.0.0 ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("ForeignJMSConnectionFactories")) {
         var3 = "getForeignJMSConnectionFactories";
         var4 = null;
         var2 = new PropertyDescriptor("ForeignJMSConnectionFactories", DomainMBean.class, var3, var4);
         var1.put("ForeignJMSConnectionFactories", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createForeignJMSConnectionFactory");
         var2.setValue("destroyer", "destroyForeignJMSConnectionFactory");
         var2.setValue("creator", "createForeignJMSConnectionFactory");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("ForeignJMSDestinations")) {
         var3 = "getForeignJMSDestinations";
         var4 = null;
         var2 = new PropertyDescriptor("ForeignJMSDestinations", DomainMBean.class, var3, var4);
         var1.put("ForeignJMSDestinations", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyForeignJMSDestination");
         var2.setValue("creator", "createForeignJMSDestination");
         var2.setValue("creator", "createForeignJMSDestination");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("ForeignJMSServers")) {
         var3 = "getForeignJMSServers";
         var4 = null;
         var2 = new PropertyDescriptor("ForeignJMSServers", DomainMBean.class, var3, var4);
         var1.put("ForeignJMSServers", var2);
         var2.setValue("description", "Get all the defined Foreign JMS Servers ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyForeignJMSServer");
         var2.setValue("creator", "createForeignJMSServer");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("ForeignJNDIProviders")) {
         var3 = "getForeignJNDIProviders";
         var4 = null;
         var2 = new PropertyDescriptor("ForeignJNDIProviders", DomainMBean.class, var3, var4);
         var1.put("ForeignJNDIProviders", var2);
         var2.setValue("description", "Get all the defined Foreign JNDI Providers ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createForeignJNDIProvider");
         var2.setValue("destroyer", "destroyForeignJNDIProvider");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("InternalAppDeployments")) {
         var3 = "getInternalAppDeployments";
         var4 = null;
         var2 = new PropertyDescriptor("InternalAppDeployments", DomainMBean.class, var3, var4);
         var1.put("InternalAppDeployments", var2);
         var2.setValue("description", "<p>The collection of internal application deployments in this domain</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("InternalLibraries")) {
         var3 = "getInternalLibraries";
         var4 = null;
         var2 = new PropertyDescriptor("InternalLibraries", DomainMBean.class, var3, var4);
         var1.put("InternalLibraries", var2);
         var2.setValue("description", "<p>The collection of internal libraries in this domain</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("JDBCConnectionPools")) {
         var3 = "getJDBCConnectionPools";
         var4 = null;
         var2 = new PropertyDescriptor("JDBCConnectionPools", DomainMBean.class, var3, var4);
         var1.put("JDBCConnectionPools", var2);
         var2.setValue("description", "<p>Returns the JDBCConnectionPoolMBeans representing the pools that have been configured to be part of this domain.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyJDBCConnectionPool");
         var2.setValue("creator", "createJDBCConnectionPool");
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.configuration.DomainMBean#JDBCSystemResources} ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (!var1.containsKey("JDBCDataSourceFactories")) {
         var3 = "getJDBCDataSourceFactories";
         var4 = null;
         var2 = new PropertyDescriptor("JDBCDataSourceFactories", DomainMBean.class, var3, var4);
         var1.put("JDBCDataSourceFactories", var2);
         var2.setValue("description", "<p>Define JDBCDataSourceFactories for this Domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createJDBCDataSourceFactory");
         var2.setValue("destroyer", "destroyJDBCDataSourceFactory");
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.configuration.DomainMBean#AppDeployment} ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("JDBCDataSources")) {
         var3 = "getJDBCDataSources";
         var4 = null;
         var2 = new PropertyDescriptor("JDBCDataSources", DomainMBean.class, var3, var4);
         var1.put("JDBCDataSources", var2);
         var2.setValue("description", "<p>Returns the JDBCDataSourceMBeans representing the data sources that have been configured to be part of this domain.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyJDBCDataSource");
         var2.setValue("creator", "createJDBCDataSource");
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.configuration.DomainMBean#JDBCSystemResources} ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("JDBCMultiPools")) {
         var3 = "getJDBCMultiPools";
         var4 = null;
         var2 = new PropertyDescriptor("JDBCMultiPools", DomainMBean.class, var3, var4);
         var1.put("JDBCMultiPools", var2);
         var2.setValue("description", "<p>Returns the JDBCMultiPool representing the multi-pools that have been configured to be part of this domain.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyJDBCMultiPool");
         var2.setValue("creator", "createJDBCMultiPool");
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.configuration.DomainMBean#JDBCSystemResources} ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("JDBCStores")) {
         var3 = "getJDBCStores";
         var4 = null;
         var2 = new PropertyDescriptor("JDBCStores", DomainMBean.class, var3, var4);
         var1.put("JDBCStores", var2);
         var2.setValue("description", "<p>Return file stores defined in this domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createJDBCStore");
         var2.setValue("destroyer", "destroyJDBCStore");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("JDBCSystemResources")) {
         var3 = "getJDBCSystemResources";
         var4 = null;
         var2 = new PropertyDescriptor("JDBCSystemResources", DomainMBean.class, var3, var4);
         var1.put("JDBCSystemResources", var2);
         var2.setValue("description", "<p>Returns the JDBCSystemResourceMBeans that have been defined for this domain </p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createJDBCSystemResource");
         var2.setValue("creator", "createJDBCSystemResource");
         var2.setValue("destroyer", "destroyJDBCSystemResource");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("JDBCTxDataSources")) {
         var3 = "getJDBCTxDataSources";
         var4 = null;
         var2 = new PropertyDescriptor("JDBCTxDataSources", DomainMBean.class, var3, var4);
         var1.put("JDBCTxDataSources", var2);
         var2.setValue("description", "<p>Returns the JDBCTxDataSource representing the JDBCTxDataSource which has been configured in this domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyJDBCTxDataSource");
         var2.setValue("creator", "createJDBCTxDataSource");
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.configuration.DomainMBean#JDBCSystemResources} ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (!var1.containsKey("JMSBridgeDestinations")) {
         var3 = "getJMSBridgeDestinations";
         var4 = null;
         var2 = new PropertyDescriptor("JMSBridgeDestinations", DomainMBean.class, var3, var4);
         var1.put("JMSBridgeDestinations", var2);
         var2.setValue("description", "<p>Return the JMSBridgeDestinations for this Domain.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createJMSBridgeDestination");
         var2.setValue("destroyer", "destroyJMSBridgeDestination");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("JMSConnectionConsumers")) {
         var3 = "getJMSConnectionConsumers";
         var4 = null;
         var2 = new PropertyDescriptor("JMSConnectionConsumers", DomainMBean.class, var3, var4);
         var1.put("JMSConnectionConsumers", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyJMSConnectionConsumer");
         var2.setValue("creator", "createJMSConnectionConsumer");
         var2.setValue("deprecated", " ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("JMSConnectionFactories")) {
         var3 = "getJMSConnectionFactories";
         var4 = null;
         var2 = new PropertyDescriptor("JMSConnectionFactories", DomainMBean.class, var3, var4);
         var1.put("JMSConnectionFactories", var2);
         var2.setValue("description", "<p>Return the JMSConnectionFactorys for this Domain.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyJMSConnectionFactory");
         var2.setValue("creator", "createJMSConnectionFactory");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("JMSDestinationKeys")) {
         var3 = "getJMSDestinationKeys";
         var4 = null;
         var2 = new PropertyDescriptor("JMSDestinationKeys", DomainMBean.class, var3, var4);
         var1.put("JMSDestinationKeys", var2);
         var2.setValue("description", "<p>Retrieve JMSDestinationKeys for this Domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyJMSDestinationKey");
         var2.setValue("creator", "createJMSDestinationKey");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("JMSDestinations")) {
         var3 = "getJMSDestinations";
         var4 = null;
         var2 = new PropertyDescriptor("JMSDestinations", DomainMBean.class, var3, var4);
         var1.put("JMSDestinations", var2);
         var2.setValue("description", "<p>Retrieve JMSDestinations for this Domain</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("JMSDistributedQueueMembers")) {
         var3 = "getJMSDistributedQueueMembers";
         var4 = null;
         var2 = new PropertyDescriptor("JMSDistributedQueueMembers", DomainMBean.class, var3, var4);
         var1.put("JMSDistributedQueueMembers", var2);
         var2.setValue("description", "<p>Define JMSDistributedQueueMembers for this Domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createJMSDistributedQueueMember");
         var2.setValue("creator", "createJMSDistributedQueueMember");
         var2.setValue("destroyer", "destroyJMSDistributedQueueMember");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("JMSDistributedQueues")) {
         var3 = "getJMSDistributedQueues";
         var4 = null;
         var2 = new PropertyDescriptor("JMSDistributedQueues", DomainMBean.class, var3, var4);
         var1.put("JMSDistributedQueues", var2);
         var2.setValue("description", "<p>Define JMSDistributedQueues for this Domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyJMSDistributedQueue");
         var2.setValue("creator", "createJMSDistributedQueue");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("JMSDistributedTopicMembers")) {
         var3 = "getJMSDistributedTopicMembers";
         var4 = null;
         var2 = new PropertyDescriptor("JMSDistributedTopicMembers", DomainMBean.class, var3, var4);
         var1.put("JMSDistributedTopicMembers", var2);
         var2.setValue("description", "<p>Define JMSDistributedTopicMembers for this Domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createJMSDistributedTopicMember");
         var2.setValue("creator", "createJMSDistributedTopicMember");
         var2.setValue("destroyer", "destroyJMSDistributedTopicMember");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("JMSDistributedTopics")) {
         var3 = "getJMSDistributedTopics";
         var4 = null;
         var2 = new PropertyDescriptor("JMSDistributedTopics", DomainMBean.class, var3, var4);
         var1.put("JMSDistributedTopics", var2);
         var2.setValue("description", "<p>Define JMSDistributedTopics for this Domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyJMSDistributedTopic");
         var2.setValue("creator", "createJMSDistributedTopic");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("JMSFileStores")) {
         var3 = "getJMSFileStores";
         var4 = null;
         var2 = new PropertyDescriptor("JMSFileStores", DomainMBean.class, var3, var4);
         var1.put("JMSFileStores", var2);
         var2.setValue("description", "<p>Define JMSFileStores for this Domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createJMSFileStore");
         var2.setValue("destroyer", "destroyJMSFileStore");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("JMSInteropModules")) {
         var3 = "getJMSInteropModules";
         var4 = null;
         var2 = new PropertyDescriptor("JMSInteropModules", DomainMBean.class, var3, var4);
         var1.put("JMSInteropModules", var2);
         var2.setValue("description", "<p>Returns the JMS Interop Module that have been defined for this domain. </p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createJMSInteropModule");
         var2.setValue("destroyer", "destroyJMSInteropModule");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("JMSJDBCStores")) {
         var3 = "getJMSJDBCStores";
         var4 = null;
         var2 = new PropertyDescriptor("JMSJDBCStores", DomainMBean.class, var3, var4);
         var1.put("JMSJDBCStores", var2);
         var2.setValue("description", "<p>Define JMSJDBCStores for this Domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createJMSJDBCStore");
         var2.setValue("destroyer", "destroyJMSJDBCStore");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("JMSQueues")) {
         var3 = "getJMSQueues";
         var4 = null;
         var2 = new PropertyDescriptor("JMSQueues", DomainMBean.class, var3, var4);
         var1.put("JMSQueues", var2);
         var2.setValue("description", "<p>Define JMSQueues for this Domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyJMSQueue");
         var2.setValue("creator", "createJMSQueue");
         var2.setValue("creator", "createJMSQueue");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (!var1.containsKey("JMSServers")) {
         var3 = "getJMSServers";
         var4 = null;
         var2 = new PropertyDescriptor("JMSServers", DomainMBean.class, var3, var4);
         var1.put("JMSServers", var2);
         var2.setValue("description", "<p>Define JMSServers for this Domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createJMSServer");
         var2.setValue("destroyer", "destroyJMSServer");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("JMSSessionPools")) {
         var3 = "getJMSSessionPools";
         var4 = null;
         var2 = new PropertyDescriptor("JMSSessionPools", DomainMBean.class, var3, var4);
         var1.put("JMSSessionPools", var2);
         var2.setValue("description", "<p>Return the JMSSessionPools for this Domain.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyJMSSessionPool");
         var2.setValue("creator", "createJMSSessionPool");
         var2.setValue("creator", "createJMSSessionPool");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("JMSStores")) {
         var3 = "getJMSStores";
         var4 = null;
         var2 = new PropertyDescriptor("JMSStores", DomainMBean.class, var3, var4);
         var1.put("JMSStores", var2);
         var2.setValue("description", "<p>Define JMSStores for this Domain</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("JMSSystemResources")) {
         var3 = "getJMSSystemResources";
         var4 = null;
         var2 = new PropertyDescriptor("JMSSystemResources", DomainMBean.class, var3, var4);
         var1.put("JMSSystemResources", var2);
         var2.setValue("description", "<p>Returns the JMSSystemResourceMBeans that have been defined for this domain. </p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyJMSSystemResource");
         var2.setValue("creator", "createJMSSystemResource");
         var2.setValue("creator", "createJMSSystemResource");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("JMSTemplates")) {
         var3 = "getJMSTemplates";
         var4 = null;
         var2 = new PropertyDescriptor("JMSTemplates", DomainMBean.class, var3, var4);
         var1.put("JMSTemplates", var2);
         var2.setValue("description", "<p>Define JMSTemplates for this Domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyJMSTemplate");
         var2.setValue("creator", "createJMSTemplate");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("JMSTopics")) {
         var3 = "getJMSTopics";
         var4 = null;
         var2 = new PropertyDescriptor("JMSTopics", DomainMBean.class, var3, var4);
         var1.put("JMSTopics", var2);
         var2.setValue("description", "<p>Define JMSTopics for this Domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createJMSTopic");
         var2.setValue("creator", "createJMSTopic");
         var2.setValue("destroyer", "destroyJMSTopic");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("JMX")) {
         var3 = "getJMX";
         var4 = null;
         var2 = new PropertyDescriptor("JMX", DomainMBean.class, var3, var4);
         var1.put("JMX", var2);
         var2.setValue("description", "The configuration of the JMX Subsystem. ");
         var2.setValue("relationship", "containment");
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("JPA")) {
         var3 = "getJPA";
         var4 = null;
         var2 = new PropertyDescriptor("JPA", DomainMBean.class, var3, var4);
         var1.put("JPA", var2);
         var2.setValue("description", "<p>Return the JPA configuration for this Domain.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("JTA")) {
         var3 = "getJTA";
         var4 = null;
         var2 = new PropertyDescriptor("JTA", DomainMBean.class, var3, var4);
         var1.put("JTA", var2);
         var2.setValue("description", "<p>Return the JTA configuration for this Domain.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("JoltConnectionPools")) {
         var3 = "getJoltConnectionPools";
         var4 = null;
         var2 = new PropertyDescriptor("JoltConnectionPools", DomainMBean.class, var3, var4);
         var1.put("JoltConnectionPools", var2);
         var2.setValue("description", "<p>Return the JoltConnectionPools for this Domain.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createJoltConnectionPool");
         var2.setValue("destroyer", "destroyJoltConnectionPool");
      }

      if (!var1.containsKey("LDAPRealms")) {
         var3 = "getLDAPRealms";
         var4 = null;
         var2 = new PropertyDescriptor("LDAPRealms", DomainMBean.class, var3, var4);
         var1.put("LDAPRealms", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyLDAPRealm");
         var2.setValue("creator", "createLDAPRealm");
         var2.setValue("deprecated", "7.0.0.0 ");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("LastModificationTime")) {
         var3 = "getLastModificationTime";
         var4 = null;
         var2 = new PropertyDescriptor("LastModificationTime", DomainMBean.class, var3, var4);
         var1.put("LastModificationTime", var2);
         var2.setValue("description", "<p>Return the last time this domain was updated. This is guaranteed to be unique for a given transactional modification.</p> ");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("Libraries")) {
         var3 = "getLibraries";
         var4 = null;
         var2 = new PropertyDescriptor("Libraries", DomainMBean.class, var3, var4);
         var1.put("Libraries", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("Log")) {
         var3 = "getLog";
         var4 = null;
         var2 = new PropertyDescriptor("Log", DomainMBean.class, var3, var4);
         var1.put("Log", var2);
         var2.setValue("description", "<p>Return the domain logfile configuration for this domain.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("LogFilters")) {
         var3 = "getLogFilters";
         var4 = null;
         var2 = new PropertyDescriptor("LogFilters", DomainMBean.class, var3, var4);
         var1.put("LogFilters", var2);
         var2.setValue("description", "Gets the array of log filters defined in the domain ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createLogFilter");
         var2.setValue("destroyer", "destroyLogFilter");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("Machines")) {
         var3 = "getMachines";
         var4 = null;
         var2 = new PropertyDescriptor("Machines", DomainMBean.class, var3, var4);
         var1.put("Machines", var2);
         var2.setValue("description", "<p>Define machines for this Domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createMachine");
         var2.setValue("destroyer", "destroyMachine");
         var2.setValue("creator.UnixMachineMBean", "createUnixMachine");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MailSessions")) {
         var3 = "getMailSessions";
         var4 = null;
         var2 = new PropertyDescriptor("MailSessions", DomainMBean.class, var3, var4);
         var1.put("MailSessions", var2);
         var2.setValue("description", "<p>Retrieve MailSessions for this Domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyMailSession");
         var2.setValue("creator", "createMailSession");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MessagingBridges")) {
         var3 = "getMessagingBridges";
         var4 = null;
         var2 = new PropertyDescriptor("MessagingBridges", DomainMBean.class, var3, var4);
         var1.put("MessagingBridges", var2);
         var2.setValue("description", "<p>Returns the MessagingBridgeMBean representing the messaging bridges that have been configured to be part of this domain.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyMessagingBridge");
         var2.setValue("creator", "createMessagingBridge");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MigratableRMIServices")) {
         var3 = "getMigratableRMIServices";
         var4 = null;
         var2 = new PropertyDescriptor("MigratableRMIServices", DomainMBean.class, var3, var4);
         var1.put("MigratableRMIServices", var2);
         var2.setValue("description", "<p>Returns an array of the contained MigratableRMIService MBeans</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyMigratableRMIService");
         var2.setValue("creator", "createMigratableRMIService");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("MigratableTargets")) {
         var3 = "getMigratableTargets";
         var4 = null;
         var2 = new PropertyDescriptor("MigratableTargets", DomainMBean.class, var3, var4);
         var1.put("MigratableTargets", var2);
         var2.setValue("description", "<p>Returns an array of the contained MigratableTarget MBeans</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyMigratableTarget");
         var2.setValue("creator", "createMigratableTarget");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("NTRealms")) {
         var3 = "getNTRealms";
         var4 = null;
         var2 = new PropertyDescriptor("NTRealms", DomainMBean.class, var3, var4);
         var1.put("NTRealms", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createNTRealm");
         var2.setValue("destroyer", "destroyNTRealm");
         var2.setValue("deprecated", "7.0.0.0 ");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", DomainMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("NetworkChannels")) {
         var3 = "getNetworkChannels";
         var4 = null;
         var2 = new PropertyDescriptor("NetworkChannels", DomainMBean.class, var3, var4);
         var1.put("NetworkChannels", var2);
         var2.setValue("description", "<p>Define NetworkChannels for this Domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyNetworkChannel");
         var2.setValue("creator", "createNetworkChannel");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (!var1.containsKey("PasswordPolicies")) {
         var3 = "getPasswordPolicies";
         var4 = null;
         var2 = new PropertyDescriptor("PasswordPolicies", DomainMBean.class, var3, var4);
         var1.put("PasswordPolicies", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyPasswordPolicy");
         var2.setValue("creator", "createPasswordPolicy");
         var2.setValue("deprecated", "7.0.0.0 ");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("PathServices")) {
         var3 = "getPathServices";
         var4 = null;
         var2 = new PropertyDescriptor("PathServices", DomainMBean.class, var3, var4);
         var1.put("PathServices", var2);
         var2.setValue("description", "Define PathService for this Domain ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyPathService");
         var2.setValue("creator", "createPathService");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("RDBMSRealms")) {
         var3 = "getRDBMSRealms";
         var4 = null;
         var2 = new PropertyDescriptor("RDBMSRealms", DomainMBean.class, var3, var4);
         var1.put("RDBMSRealms", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createRDBMSRealm");
         var2.setValue("destroyer", "destroyRDBMSRealm");
         var2.setValue("deprecated", "7.0.0.0 ");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("Realms")) {
         var3 = "getRealms";
         var4 = null;
         var2 = new PropertyDescriptor("Realms", DomainMBean.class, var3, var4);
         var1.put("Realms", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyRealm");
         var2.setValue("creator", "createRealm");
         var2.setValue("deprecated", "7.0.0.0 ");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("RemoteSAFContexts")) {
         var3 = "getRemoteSAFContexts";
         var4 = null;
         var2 = new PropertyDescriptor("RemoteSAFContexts", DomainMBean.class, var3, var4);
         var1.put("RemoteSAFContexts", var2);
         var2.setValue("description", "<p>Get RemoteSAFContextMBean for this Domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyRemoteSAFContext");
         var2.setValue("creator", "createRemoteSAFContext");
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.6.0", (String)null, this.targetVersion) && !var1.containsKey("RestfulManagementServices")) {
         var3 = "getRestfulManagementServices";
         var4 = null;
         var2 = new PropertyDescriptor("RestfulManagementServices", DomainMBean.class, var3, var4);
         var1.put("RestfulManagementServices", var2);
         var2.setValue("description", "The configuration of the Management Services Subsystem. ");
         var2.setValue("relationship", "containment");
         var2.setValue("since", "10.3.6.0");
      }

      if (!var1.containsKey("RootDirectory")) {
         var3 = "getRootDirectory";
         var4 = null;
         var2 = new PropertyDescriptor("RootDirectory", DomainMBean.class, var3, var4);
         var1.put("RootDirectory", var2);
         var2.setValue("description", "<p>Return the root directory for the domain. In other words for a server process [ServerMBean.getRootDirectory] or [ServerMBean.getDomainDirectory]</p> ");
      }

      if (!var1.containsKey("SAFAgents")) {
         var3 = "getSAFAgents";
         var4 = null;
         var2 = new PropertyDescriptor("SAFAgents", DomainMBean.class, var3, var4);
         var1.put("SAFAgents", var2);
         var2.setValue("description", "<p>Get SAFAgentMBean for this Domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createSAFAgent");
         var2.setValue("destroyer", "destroySAFAgent");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SNMPAgent")) {
         var3 = "getSNMPAgent";
         var4 = null;
         var2 = new PropertyDescriptor("SNMPAgent", DomainMBean.class, var3, var4);
         var1.put("SNMPAgent", var2);
         var2.setValue("description", "<p>Return the SNMPAgentMBean for this domain. This is a singleton MBean describing SNMP Agent configuration details. This MBean has getters and setters for other SNMP related configuration MBeans.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("SNMPAgentDeployments")) {
         var3 = "getSNMPAgentDeployments";
         var4 = null;
         var2 = new PropertyDescriptor("SNMPAgentDeployments", DomainMBean.class, var3, var4);
         var1.put("SNMPAgentDeployments", var2);
         var2.setValue("description", "The SNMPAgentDeployments defined in the domain. ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroySNMPAgentDeployment");
         var2.setValue("creator", "createSNMPAgentDeployment");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "10.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("SNMPAttributeChanges")) {
         var3 = "getSNMPAttributeChanges";
         var4 = null;
         var2 = new PropertyDescriptor("SNMPAttributeChanges", DomainMBean.class, var3, var4);
         var1.put("SNMPAttributeChanges", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroySNMPAttributeChange");
         var2.setValue("creator", "createSNMPAttributeChange");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("SNMPCounterMonitors")) {
         var3 = "getSNMPCounterMonitors";
         var4 = null;
         var2 = new PropertyDescriptor("SNMPCounterMonitors", DomainMBean.class, var3, var4);
         var1.put("SNMPCounterMonitors", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroySNMPCounterMonitor");
         var2.setValue("creator", "createSNMPCounterMonitor");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("SNMPGaugeMonitors")) {
         var3 = "getSNMPGaugeMonitors";
         var4 = null;
         var2 = new PropertyDescriptor("SNMPGaugeMonitors", DomainMBean.class, var3, var4);
         var1.put("SNMPGaugeMonitors", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroySNMPGaugeMonitor");
         var2.setValue("creator", "createSNMPGaugeMonitor");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("SNMPLogFilters")) {
         var3 = "getSNMPLogFilters";
         var4 = null;
         var2 = new PropertyDescriptor("SNMPLogFilters", DomainMBean.class, var3, var4);
         var1.put("SNMPLogFilters", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroySNMPLogFilter");
         var2.setValue("creator", "createSNMPLogFilter");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("SNMPProxies")) {
         var3 = "getSNMPProxies";
         var4 = null;
         var2 = new PropertyDescriptor("SNMPProxies", DomainMBean.class, var3, var4);
         var1.put("SNMPProxies", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createSNMPProxy");
         var2.setValue("destroyer", "destroySNMPProxy");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("SNMPStringMonitors")) {
         var3 = "getSNMPStringMonitors";
         var4 = null;
         var2 = new PropertyDescriptor("SNMPStringMonitors", DomainMBean.class, var3, var4);
         var1.put("SNMPStringMonitors", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroySNMPStringMonitor");
         var2.setValue("creator", "createSNMPStringMonitor");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("SNMPTrapDestinations")) {
         var3 = "getSNMPTrapDestinations";
         var4 = null;
         var2 = new PropertyDescriptor("SNMPTrapDestinations", DomainMBean.class, var3, var4);
         var1.put("SNMPTrapDestinations", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroySNMPTrapDestination");
         var2.setValue("creator", "createSNMPTrapDestination");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (!var1.containsKey("Security")) {
         var3 = "getSecurity";
         var4 = null;
         var2 = new PropertyDescriptor("Security", DomainMBean.class, var3, var4);
         var1.put("Security", var2);
         var2.setValue("description", "<p>Return the (old) security configuration for this domain.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("deprecated", "9.0.0.0 ");
      }

      if (!var1.containsKey("SecurityConfiguration")) {
         var3 = "getSecurityConfiguration";
         var4 = null;
         var2 = new PropertyDescriptor("SecurityConfiguration", DomainMBean.class, var3, var4);
         var1.put("SecurityConfiguration", var2);
         var2.setValue("description", "<p>Return the (new) security configuration for this domain.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("SelfTuning")) {
         var3 = "getSelfTuning";
         var4 = null;
         var2 = new PropertyDescriptor("SelfTuning", DomainMBean.class, var3, var4);
         var1.put("SelfTuning", var2);
         var2.setValue("description", "Get the WorkManager configuration pieces for this domain ");
         var2.setValue("relationship", "containment");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Servers")) {
         var3 = "getServers";
         var4 = null;
         var2 = new PropertyDescriptor("Servers", DomainMBean.class, var3, var4);
         var1.put("Servers", var2);
         var2.setValue("description", "<p>Returns the ServerMBeans representing the servers that have been configured to be part of this domain.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyServer");
         var2.setValue("creator", "createServer");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ShutdownClasses")) {
         var3 = "getShutdownClasses";
         var4 = null;
         var2 = new PropertyDescriptor("ShutdownClasses", DomainMBean.class, var3, var4);
         var1.put("ShutdownClasses", var2);
         var2.setValue("description", "<p>Retrieve ShutdownClasses for this Domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyShutdownClass");
         var2.setValue("creator", "createShutdownClass");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SingletonServices")) {
         var3 = "getSingletonServices";
         var4 = null;
         var2 = new PropertyDescriptor("SingletonServices", DomainMBean.class, var3, var4);
         var1.put("SingletonServices", var2);
         var2.setValue("description", "<p>Retrieve SingletonServicees for this Domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createSingletonService");
         var2.setValue("destroyer", "destroySingletonService");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("StartupClasses")) {
         var3 = "getStartupClasses";
         var4 = null;
         var2 = new PropertyDescriptor("StartupClasses", DomainMBean.class, var3, var4);
         var1.put("StartupClasses", var2);
         var2.setValue("description", "<p>Retrieve StartupClasses for this Domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createStartupClass");
         var2.setValue("destroyer", "destroyStartupClass");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("SystemResources")) {
         var3 = "getSystemResources";
         var4 = null;
         var2 = new PropertyDescriptor("SystemResources", DomainMBean.class, var3, var4);
         var1.put("SystemResources", var2);
         var2.setValue("description", "<p>Return the SystemResourceMBeans in this Domain.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("Targets")) {
         var3 = "getTargets";
         var4 = null;
         var2 = new PropertyDescriptor("Targets", DomainMBean.class, var3, var4);
         var1.put("Targets", var2);
         var2.setValue("description", "<p>Define targets for this Domain</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("UnixRealms")) {
         var3 = "getUnixRealms";
         var4 = null;
         var2 = new PropertyDescriptor("UnixRealms", DomainMBean.class, var3, var4);
         var1.put("UnixRealms", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyUnixRealm");
         var2.setValue("creator", "createUnixRealm");
         var2.setValue("deprecated", "7.0.0.0 ");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("VirtualHosts")) {
         var3 = "getVirtualHosts";
         var4 = null;
         var2 = new PropertyDescriptor("VirtualHosts", DomainMBean.class, var3, var4);
         var1.put("VirtualHosts", var2);
         var2.setValue("description", "<p>Define VirtualHosts for this Domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createVirtualHost");
         var2.setValue("destroyer", "destroyVirtualHost");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("WLDFSystemResources")) {
         var3 = "getWLDFSystemResources";
         var4 = null;
         var2 = new PropertyDescriptor("WLDFSystemResources", DomainMBean.class, var3, var4);
         var1.put("WLDFSystemResources", var2);
         var2.setValue("description", "<p>Returns the WLDFSystemResourceMBeans that have been defined for this domain. </p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyWLDFSystemResource");
         var2.setValue("creator", "createWLDFSystemResource");
         var2.setValue("creator", "createWLDFSystemResource");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("WLECConnectionPools")) {
         var3 = "getWLECConnectionPools";
         var4 = null;
         var2 = new PropertyDescriptor("WLECConnectionPools", DomainMBean.class, var3, var4);
         var1.put("WLECConnectionPools", var2);
         var2.setValue("description", "<p>Get WLECConnectionPools for this Domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyWLECConnectionPool");
         var2.setValue("creator", "createWLECConnectionPool");
      }

      if (!var1.containsKey("WSReliableDeliveryPolicies")) {
         var3 = "getWSReliableDeliveryPolicies";
         var4 = null;
         var2 = new PropertyDescriptor("WSReliableDeliveryPolicies", DomainMBean.class, var3, var4);
         var1.put("WSReliableDeliveryPolicies", var2);
         var2.setValue("description", "<p>Define wSReliableDeliveryPolicies for this Domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createWSReliableDeliveryPolicy");
         var2.setValue("destroyer", "destroyWSReliableDeliveryPolicy");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("WTCServers")) {
         var3 = "getWTCServers";
         var4 = null;
         var2 = new PropertyDescriptor("WTCServers", DomainMBean.class, var3, var4);
         var1.put("WTCServers", var2);
         var2.setValue("description", "<p>Return the WTCServerMBeans for this Domain.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createWTCServer");
         var2.setValue("destroyer", "destroyWTCServer");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("WebAppContainer")) {
         var3 = "getWebAppContainer";
         var4 = null;
         var2 = new PropertyDescriptor("WebAppContainer", DomainMBean.class, var3, var4);
         var1.put("WebAppContainer", var2);
         var2.setValue("description", "Collection of global properties to be applied on all webapps in this domain. ");
         var2.setValue("relationship", "containment");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("WebserviceSecurities")) {
         var3 = "getWebserviceSecurities";
         var4 = null;
         var2 = new PropertyDescriptor("WebserviceSecurities", DomainMBean.class, var3, var4);
         var1.put("WebserviceSecurities", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createWebserviceSecurity");
         var2.setValue("destroyer", "destroyWebserviceSecurity");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("XMLEntityCaches")) {
         var3 = "getXMLEntityCaches";
         var4 = null;
         var2 = new PropertyDescriptor("XMLEntityCaches", DomainMBean.class, var3, var4);
         var1.put("XMLEntityCaches", var2);
         var2.setValue("description", "Returns all the XMLEntityCache objects defined in this domain ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createXMLEntityCache");
         var2.setValue("destroyer", "destroyXMLEntityCache");
      }

      if (!var1.containsKey("XMLRegistries")) {
         var3 = "getXMLRegistries";
         var4 = null;
         var2 = new PropertyDescriptor("XMLRegistries", DomainMBean.class, var3, var4);
         var1.put("XMLRegistries", var2);
         var2.setValue("description", "<p>Define xMLRegistries for this Domain</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createXMLRegistry");
         var2.setValue("destroyer", "destroyXMLRegistry");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("AdministrationMBeanAuditingEnabled")) {
         var3 = "isAdministrationMBeanAuditingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAdministrationMBeanAuditingEnabled";
         }

         var2 = new PropertyDescriptor("AdministrationMBeanAuditingEnabled", DomainMBean.class, var3, var4);
         var1.put("AdministrationMBeanAuditingEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the Administration Server generates a log message when this WebLogic Server domain's configuration has been modified.</p>  <p>Any change to a server, module, or other item in the domain (either through the Administration Console, command-line utilities, or the APIs) will cause the Administration Server to generate this informational message.</p>  <p> This attribute has been deprecated in favor of ConfigurationAuditType. If values for both attributes are specified, the resultant behavior will be the logical OR condition of the two settings.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("deprecated", "Please use <code>DomainMBean.getConfigurationAuditType()</code> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("AdministrationPortEnabled")) {
         var3 = "isAdministrationPortEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAdministrationPortEnabled";
         }

         var2 = new PropertyDescriptor("AdministrationPortEnabled", DomainMBean.class, var3, var4);
         var1.put("AdministrationPortEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the domain-wide administration port should be enabled for this WebLogic Server domain. Because the administration port uses SSL, enabling the administration port requires that SSL must be configured for all servers in the domain.</p>  <p>The domain-wide administration port enables you to start a WebLogic Server instance in <code>STANDBY</code> state. It also allows you to separate administration traffic from application traffic in your domain. Because all servers in the domain must enable or disable the administration port at once, you configure the default administration port settings at the domain level.</p>  <p>If you enable the administration port:</p>  <ul> <li> <p>The administration port accepts only connections that specify administrator credentials.</p> </li>  <li> <p>Connections that specify administrator credentials can use only the administration port.</p> </li>  <li> <p>The command that starts managed servers must specify a secure protocol and the administration port: <code>-Dweblogic.management.server=https://<i>admin_server:administration_port</i></code></p> </li> </ul> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getAdministrationPort"), BeanInfoHelper.encodeEntities("#getAdministrationProtocol"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#isAdministrationPortEnabled"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#getAdministrationPort"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.KernelMBean#getSSL")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("AutoConfigurationSaveEnabled")) {
         var3 = "isAutoConfigurationSaveEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAutoConfigurationSaveEnabled";
         }

         var2 = new PropertyDescriptor("AutoConfigurationSaveEnabled", DomainMBean.class, var3, var4);
         var1.put("AutoConfigurationSaveEnabled", var2);
         var2.setValue("description", "<p>Causes the server to periodically persist changes to its configuration.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("deprecated", "9.0.0.0 The configuration is explicit written on a save call. ");
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (!var1.containsKey("AutoDeployForSubmodulesEnabled")) {
         var3 = "isAutoDeployForSubmodulesEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAutoDeployForSubmodulesEnabled";
         }

         var2 = new PropertyDescriptor("AutoDeployForSubmodulesEnabled", DomainMBean.class, var3, var4);
         var1.put("AutoDeployForSubmodulesEnabled", var2);
         var2.setValue("description", "Indicates whether autodeployed applications could include JMS modules. If true then any submodules defined in the application's JMS modules will be deployed with default targets. The submodules define the different destinations in the JMS module, eg topics and queues, and if they aren't provided with explicit targets they may not be properly deployed. ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("ClusterConstraintsEnabled")) {
         var3 = "isClusterConstraintsEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClusterConstraintsEnabled";
         }

         var2 = new PropertyDescriptor("ClusterConstraintsEnabled", DomainMBean.class, var3, var4);
         var1.put("ClusterConstraintsEnabled", var2);
         var2.setValue("description", "<p>Specifies that deployments targeted to a cluster succeed only if all servers in the cluster are running.</p>  <p>By default, cluster constraints are disabled and deployment is attempted only on the servers that are reachable at the time of deployment from the Administration Server. Any servers that have been shut down or are temporarily partitioned from the Administration Server will retrieve the deployment during server startup or shortly after the network partition is resolved.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
      }

      if (!var1.containsKey("ConfigBackupEnabled")) {
         var3 = "isConfigBackupEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConfigBackupEnabled";
         }

         var2 = new PropertyDescriptor("ConfigBackupEnabled", DomainMBean.class, var3, var4);
         var1.put("ConfigBackupEnabled", var2);
         var2.setValue("description", "<p>If true, then backups of the configuration will be made during server boot.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
      }

      if (!var1.containsKey("ConsoleEnabled")) {
         var3 = "isConsoleEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConsoleEnabled";
         }

         var2 = new PropertyDescriptor("ConsoleEnabled", DomainMBean.class, var3, var4);
         var1.put("ConsoleEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the Administration Server automatically deploys the Administration Console in the current domain.</p>  <p>If the Administration Console is not deployed, you can still use the WebLogic Scripting Tool or the management APIs to configure and monitor the domain.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("secureValue", new Boolean(false));
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.4.0", (String)null, this.targetVersion) && !var1.containsKey("ExalogicOptimizationsEnabled")) {
         var3 = "isExalogicOptimizationsEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setExalogicOptimizationsEnabled";
         }

         var2 = new PropertyDescriptor("ExalogicOptimizationsEnabled", DomainMBean.class, var3, var4);
         var1.put("ExalogicOptimizationsEnabled", var2);
         var2.setValue("description", "<p>Specifies whether optimizations for Oracle Exalogic should be enabled. Optimizations include improved thread management and request processing, and reduced lock contention. This attribute should be enabled only when configuring a WebLogic domain for Oracle Exalogic. For more information, see \"Enabling Exalogic-Specific Enhancements in Oracle WebLogic Server 11g Release 1 (10.3.4)\" in the Oracle Exalogic Deployment Guide.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("since", "10.3.4.0");
      }

      if (!var1.containsKey("GuardianEnabled")) {
         var3 = "isGuardianEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setGuardianEnabled";
         }

         var2 = new PropertyDescriptor("GuardianEnabled", DomainMBean.class, var3, var4);
         var1.put("GuardianEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the Guardian Agent is deployed when starting servers in the current domain.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
      }

      if (!var1.containsKey("InternalAppsDeployOnDemandEnabled")) {
         var3 = "isInternalAppsDeployOnDemandEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setInternalAppsDeployOnDemandEnabled";
         }

         var2 = new PropertyDescriptor("InternalAppsDeployOnDemandEnabled", DomainMBean.class, var3, var4);
         var1.put("InternalAppsDeployOnDemandEnabled", var2);
         var2.setValue("description", "<p>Specifies whether internal applications such as the console, uddi, wlstestclient, and uddiexplorer are deployed on demand (first access) instead of during server startup. </p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
      }

      if (!var1.containsKey("OCMEnabled")) {
         var3 = "isOCMEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOCMEnabled";
         }

         var2 = new PropertyDescriptor("OCMEnabled", DomainMBean.class, var3, var4);
         var1.put("OCMEnabled", var2);
         var2.setValue("description", "<p>Specifies whether OCM functionality should be enabled for this domain. </p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("ProductionModeEnabled")) {
         var3 = "isProductionModeEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setProductionModeEnabled";
         }

         var2 = new PropertyDescriptor("ProductionModeEnabled", DomainMBean.class, var3, var4);
         var1.put("ProductionModeEnabled", var2);
         var2.setValue("description", "<p>Specifies whether all servers in this domain run in production mode.</p> <p>You can configure servers in your domain to start in one of two modes, development or production. You use development mode while you are developing your applications. Development mode uses a relaxed security configuration and enables you to auto-deploy applications. You use production mode when your application is running in its final form. A production domain uses full security and may use clusters or other advanced features.</p>  <p>The runtime mode is a domain-wide setting. As each Managed Server starts, it refers to the mode of the Administration Server to determine its runtime mode. If you configure the domain to run in production mode, the Administration Server saves this setting to the domain's configuration document.</p> ");
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = DomainMBean.class.getMethod("createWTCServer", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Factory to create WTCServer instance in the domain</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "WTCServers");
      }

      var3 = DomainMBean.class.getMethod("destroyWTCServer", WTCServerMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("wtcServer", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>deletes WTCServer object</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "WTCServers");
      }

      var3 = DomainMBean.class.getMethod("createSNMPAgentDeployment", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Creates a SNMPAgentDeploymentMBean with the specified name ");
         var2.setValue("role", "factory");
         var2.setValue("property", "SNMPAgentDeployments");
      }

      var3 = DomainMBean.class.getMethod("destroySNMPAgentDeployment", SNMPAgentDeploymentMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("mbean", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Destroys the specified SNMPAgentDeploymentMBean ");
         var2.setValue("role", "factory");
         var2.setValue("property", "SNMPAgentDeployments");
      }

      var3 = DomainMBean.class.getMethod("createServer", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>This is the factory method for Servers that are scoped at the domain level. The short name which is specified must be unique among all object instances of type ServerMBean. The new Server which is create will have this Domain as its parent and must be destroyed with the destroyServer method.</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "Servers");
      }

      var3 = DomainMBean.class.getMethod("destroyServer", ServerMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("server", "to destroy ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Destroys and removes a server which is a child of this Domain with the specified short name .</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "Servers");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.4.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createCoherenceServer", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "10.3.4.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>This is the factory method for Coherence servers that are scoped at the domain level. The short name which is specified must be unique among all object instances of type CoherenceServerMBean. The new Coherence server which is created will have this domain as its parent and must be destroyed with the destroyCoherenceServer method.</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "CoherenceServers");
            var2.setValue("since", "10.3.4.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.4.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyCoherenceServer", CoherenceServerMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("bean", "- bean to destroy ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "10.3.4.0");
            var1.put(var5, var2);
            var2.setValue("description", "Destroy the given Coherence server. ");
            var2.setValue("role", "factory");
            var2.setValue("property", "CoherenceServers");
            var2.setValue("since", "10.3.4.0");
         }
      }

      var3 = DomainMBean.class.getMethod("createCluster", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Factory for creating Clusters.</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "Clusters");
      }

      var3 = DomainMBean.class.getMethod("destroyCluster", ClusterMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cluster", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Deletes the cluster object</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "Clusters");
      }

      var3 = DomainMBean.class.getMethod("createFileT3", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "8.1.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Factory to create FileT3 objects</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "FileT3s");
      }

      var3 = DomainMBean.class.getMethod("destroyFileT3", FileT3MBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("fileT3", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "8.1.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>deletes the FileT3 Object</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "FileT3s");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createJDBCConnectionPool", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.configuration.DomainMBean#JDBCSystemResources} ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>This is the factory method for JDBCConnectionPools that are scoped at the domain level. The short name which is specified must be unique among all object instances of type Target. The new JDBCConnectionPool which gets created will have this Domain as its parent and must be destroyed with the destroyServer method.</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JDBCConnectionPools");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyJDBCConnectionPool", JDBCConnectionPoolMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("pool", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.configuration.DomainMBean#JDBCSystemResources} ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Destroys and removes a JDBCConnectionPool which is a child of this Domain with the specified short name .</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JDBCConnectionPools");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createJDBCDataSource", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.configuration.DomainMBean#JDBCSystemResources} ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>This is the factory method for JDBCDataSources that are scoped at the domain level. The new JDBCDataSource which gets created will have this Domain as its parent and must be destroyed with the destroyServer method.</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JDBCDataSources");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyJDBCDataSource", JDBCDataSourceMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("dataSource", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.configuration.DomainMBean#JDBCSystemResources} ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Destroys and removes a JDBCDataSource which is a child of this Domain with the specified short name .</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JDBCDataSources");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createJDBCTxDataSource", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.configuration.DomainMBean#JDBCSystemResources} ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>This is the factory method for JDBCTxDataSources that are scoped at the domain level. The new JDBCDataSource which gets created will have this Domain as its parent and must be destroyed with the destroyServer method.</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JDBCTxDataSources");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyJDBCTxDataSource", JDBCTxDataSourceMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("dataSource", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.configuration.DomainMBean#JDBCSystemResources} ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Destroys and removes a JDBCTxDataSource which is a child of this Domain with the specified short name .</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JDBCTxDataSources");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createJDBCMultiPool", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.configuration.DomainMBean#JDBCSystemResources} ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>This is the factory method for JDBCMultiPools that are scoped at the domain level. The new JDBCMultiPool which gets created will have this Domain as its parent and must be destroyed with the destroyServer method.</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JDBCMultiPools");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyJDBCMultiPool", JDBCMultiPoolMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("pool", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.configuration.DomainMBean#JDBCSystemResources} ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Destroys and removes a JDBCMultiPool which is a child of this Domain with the specified short name.</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JDBCMultiPools");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      var3 = DomainMBean.class.getMethod("createMessagingBridge", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Factory for creating MessagingBridges</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "MessagingBridges");
      }

      var3 = DomainMBean.class.getMethod("destroyMessagingBridge", MessagingBridgeMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("bridge", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>deletes MessagingBridge object</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "MessagingBridges");
      }

      var3 = DomainMBean.class.getMethod("createApplication", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>factory to create Applications</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "Applications");
      }

      var3 = DomainMBean.class.getMethod("destroyApplication", ApplicationMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("application", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>destroys Applications</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "Applications");
      }

      var3 = DomainMBean.class.getMethod("createWSReliableDeliveryPolicy", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Factory method to create WSReliableDeliveryPolicy object</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "WSReliableDeliveryPolicies");
      }

      var3 = DomainMBean.class.getMethod("destroyWSReliableDeliveryPolicy", WSReliableDeliveryPolicyMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("policy", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes a WSReliableDeliveryPolicy from this domain</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "WSReliableDeliveryPolicies");
      }

      var3 = DomainMBean.class.getMethod("createJDBCDataSourceFactory", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.configuration.DomainMBean#AppDeployment} ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Factory to create JDBCDataSourceFactories.</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "JDBCDataSourceFactories");
      }

      var3 = DomainMBean.class.getMethod("destroyJDBCDataSourceFactory", JDBCDataSourceFactoryMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("factory", "name of the object ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.configuration.DomainMBean#AppDeployment} ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes a JDBCDataSourceFactory object from configuration</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "JDBCDataSourceFactories");
      }

      var3 = DomainMBean.class.getMethod("createMachine", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Factory method to create a Machine object</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "Machines");
      }

      var3 = DomainMBean.class.getMethod("createUnixMachine", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Creates a UnixMachineMBean and adds it to the list returned by getMachines.  You may use destroyMachine to destroy beans of this type. ");
         var2.setValue("role", "factory");
         var2.setValue("property", "Machines");
      }

      var3 = DomainMBean.class.getMethod("destroyMachine", MachineMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("machine", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes a Machine from this domain</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "Machines");
      }

      var3 = DomainMBean.class.getMethod("createXMLEntityCache", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "of the XMLEntityCache to be created. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Factory method to create an XMLEntityCache Object ");
         var2.setValue("role", "factory");
         var2.setValue("property", "XMLEntityCaches");
      }

      var3 = DomainMBean.class.getMethod("destroyXMLEntityCache", XMLEntityCacheMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("XMLEntityCache", "object to be destroyed ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Factory method to destroys an XMLEntityCache Object ");
         var2.setValue("role", "factory");
         var2.setValue("property", "XMLEntityCaches");
      }

      var3 = DomainMBean.class.getMethod("createXMLRegistry", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Factory method to create XMLRegistry object</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "XMLRegistries");
      }

      var3 = DomainMBean.class.getMethod("destroyXMLRegistry", XMLRegistryMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("registry", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes a XMLRegistry from this domain</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "XMLRegistries");
      }

      var3 = DomainMBean.class.getMethod("createFileRealm", String.class);
      String var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "7.0.0.0 ");
         var1.put(var7, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "FileRealms");
      }

      var3 = DomainMBean.class.getMethod("destroyFileRealm", FileRealmMBean.class);
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "7.0.0.0 ");
         var1.put(var7, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "FileRealms");
      }

      var3 = DomainMBean.class.getMethod("createCachingRealm", String.class);
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "7.0.0.0 ");
         var1.put(var7, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "CachingRealms");
      }

      var3 = DomainMBean.class.getMethod("destroyCachingRealm", CachingRealmMBean.class);
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "7.0.0.0 ");
         var1.put(var7, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "CachingRealms");
      }

      var3 = DomainMBean.class.getMethod("createRealm", String.class);
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "7.0.0.0 ");
         var1.put(var7, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "Realms");
      }

      var3 = DomainMBean.class.getMethod("destroyRealm", RealmMBean.class);
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "7.0.0.0 ");
         var1.put(var7, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "Realms");
      }

      var3 = DomainMBean.class.getMethod("createPasswordPolicy", String.class);
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "7.0.0.0 ");
         var1.put(var7, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "PasswordPolicies");
      }

      var3 = DomainMBean.class.getMethod("destroyPasswordPolicy", PasswordPolicyMBean.class);
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "7.0.0.0 ");
         var1.put(var7, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "PasswordPolicies");
      }

      var3 = DomainMBean.class.getMethod("createCustomRealm", String.class);
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "7.0.0.0 ");
         var1.put(var7, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "CustomRealms");
      }

      var3 = DomainMBean.class.getMethod("destroyCustomRealm", CustomRealmMBean.class);
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "7.0.0.0 ");
         var1.put(var7, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "CustomRealms");
      }

      var3 = DomainMBean.class.getMethod("createLDAPRealm", String.class);
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "7.0.0.0 ");
         var1.put(var7, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "LDAPRealms");
      }

      var3 = DomainMBean.class.getMethod("destroyLDAPRealm", LDAPRealmMBean.class);
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "7.0.0.0 ");
         var1.put(var7, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "LDAPRealms");
      }

      var3 = DomainMBean.class.getMethod("createNTRealm", String.class);
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "7.0.0.0 ");
         var1.put(var7, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "NTRealms");
      }

      var3 = DomainMBean.class.getMethod("destroyNTRealm", NTRealmMBean.class);
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "7.0.0.0 ");
         var1.put(var7, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "NTRealms");
      }

      var3 = DomainMBean.class.getMethod("createRDBMSRealm", String.class);
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "7.0.0.0 ");
         var1.put(var7, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "RDBMSRealms");
      }

      var3 = DomainMBean.class.getMethod("destroyRDBMSRealm", RDBMSRealmMBean.class);
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "7.0.0.0 ");
         var1.put(var7, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "RDBMSRealms");
      }

      var3 = DomainMBean.class.getMethod("createUnixRealm", String.class);
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "7.0.0.0 ");
         var1.put(var7, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "UnixRealms");
      }

      var3 = DomainMBean.class.getMethod("destroyUnixRealm", UnixRealmMBean.class);
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "7.0.0.0 ");
         var1.put(var7, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "UnixRealms");
      }

      var3 = DomainMBean.class.getMethod("createJMSServer", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      String[] var6;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Factory method to create JMSServer object</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "JMSServers");
         var6 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
         var2.setValue("rolesAllowed", var6);
      }

      var3 = DomainMBean.class.getMethod("destroyJMSServer", JMSServerMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("jmsServer", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes a JMSServer from this domain</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "JMSServers");
         var6 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
         var2.setValue("rolesAllowed", var6);
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createJMSJDBCStore", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to create JMSJDBCStore object</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSJDBCStores");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyJMSJDBCStore", JMSJDBCStoreMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("store", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Removes a JMSJDBCStore from this domain</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSJDBCStores");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createJMSFileStore", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to create JMSFileStore object</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSFileStores");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyJMSFileStore", JMSFileStoreMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("store", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Removes a JMSFileStore from this domain</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSFileStores");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createJMSQueue", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to create JMSQueue object</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSQueues");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyJMSQueue", JMSQueueMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("queue", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Removes a JMSQueue from this domain</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSQueues");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createJMSTopic", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to create JMSTopic object</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSTopics");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyJMSTopic", JMSTopicMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("topic", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Removes a JMSTopic from this domain</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSTopics");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createJMSDistributedQueue", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to create JMSDistributedQueue object</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSDistributedQueues");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyJMSDistributedQueue", JMSDistributedQueueMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("member", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Removes a JMSDistributedQueue from this domain</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSDistributedQueues");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createJMSDistributedTopic", String.class);
         var7 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var7)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var7, var2);
            var2.setValue("description", "<p>Factory method to create JMSDistributedTopic object</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSDistributedTopics");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyJMSDistributedTopic", JMSDistributedTopicMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("member", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Removes a JMSDistributedTopic from this domain</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSDistributedTopics");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createJMSTemplate", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to create JMSTemplate object</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSTemplates");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyJMSTemplate", JMSTemplateMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("template", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Removes a JMSTemplate from this domain</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSTemplates");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createNetworkChannel", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to create NetworkChannel object</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "NetworkChannels");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyNetworkChannel", NetworkChannelMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("channel", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Removes a NetworkChannel from this domain</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "NetworkChannels");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      var3 = DomainMBean.class.getMethod("createVirtualHost", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Factory method to create VirtualHost object</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "VirtualHosts");
      }

      var3 = DomainMBean.class.getMethod("destroyVirtualHost", VirtualHostMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("host", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes a VirtualHost from this domain</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "VirtualHosts");
      }

      var3 = DomainMBean.class.getMethod("createMigratableTarget", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>This is the factory method for MigratableTargets</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "MigratableTargets");
      }

      var3 = DomainMBean.class.getMethod("destroyMigratableTarget", MigratableTargetMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("bean", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Destroys and removes a MigratableTarget which with the specified short name .</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "MigratableTargets");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createEJBContainer");
         var7 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var7)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("since", "9.0.0.0");
            var1.put(var7, var2);
            var2.setValue("description", "creates EJBContainer object ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "EJBContainer");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyEJBContainer");
         var7 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var7)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("since", "9.0.0.0");
            var1.put(var7, var2);
            var2.setValue("description", "destroy EJBContainer object ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "EJBContainer");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createPathService", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "Factory method to create PathService object ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "PathServices");
            var6 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
            var2.setValue("rolesAllowed", var6);
            var2.setValue("since", "9.0.0.0");
         }
      }

      var3 = DomainMBean.class.getMethod("destroyPathService", PathServiceMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("pathService", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes a PathService from this domain</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "PathServices");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createJMSDestinationKey", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to create JMSDestination object</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSDestinationKeys");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyJMSDestinationKey", JMSDestinationKeyMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("destination", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Removes a JMSDestinationKey from this domain</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSDestinationKeys");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createJMSConnectionFactory", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory to create JMSConnectionFactory instance in the domain</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSConnectionFactories");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyJMSConnectionFactory", JMSConnectionFactoryMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("jmsConnectionFactory", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>deletes JMSConnectionFactory object</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSConnectionFactories");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createJMSSessionPool", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory to create JMSSessionPool instance in the domain</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSSessionPools");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createJMSSessionPool", String.class, JMSSessionPoolMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory to create JMSSessionPool instance in the domain</p> ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSSessionPools");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyJMSSessionPool", JMSSessionPoolMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("jmsSessionPool", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>deletes JMSSessionPool object</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSSessionPools");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      var3 = DomainMBean.class.getMethod("createJMSBridgeDestination", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Factory to create JMSBridgeDestination instance in the domain</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "JMSBridgeDestinations");
         var6 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
         var2.setValue("rolesAllowed", var6);
      }

      var3 = DomainMBean.class.getMethod("destroyJMSBridgeDestination", JMSBridgeDestinationMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("jmsBridgeDestination", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>deletes JMSBridgeDestination object</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "JMSBridgeDestinations");
         var6 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
         var2.setValue("rolesAllowed", var6);
      }

      var3 = DomainMBean.class.getMethod("createBridgeDestination", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Factory to create BridgeDestination instance in the domain</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "BridgeDestinations");
      }

      var3 = DomainMBean.class.getMethod("destroyBridgeDestination", BridgeDestinationMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("bridgeDestination", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>deletes BridgeDestination object</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "BridgeDestinations");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createForeignJMSServer", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Create a new diagnostic deployment that can be targeted to a server</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "ForeignJMSServers");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyForeignJMSServer", ForeignJMSServerMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("jmsServer", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Delete a diagnostic deployment configuration from the domain.</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "ForeignJMSServers");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      var3 = DomainMBean.class.getMethod("createShutdownClass", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Factory method to create ShutdownClass object</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "ShutdownClasses");
      }

      var3 = DomainMBean.class.getMethod("destroyShutdownClass", ShutdownClassMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("sc", "The Shutdown class to destroy ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes a ShutdownClass from this domain</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "ShutdownClasses");
      }

      var3 = DomainMBean.class.getMethod("createStartupClass", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Factory method to create StartupClass object</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "StartupClasses");
      }

      var3 = DomainMBean.class.getMethod("destroyStartupClass", StartupClassMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("sc", "the Shutdown class to destroy ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes a StartupClass from this domain</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "StartupClasses");
      }

      var3 = DomainMBean.class.getMethod("createSingletonService", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Factory method to create SingletonService object</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "SingletonServices");
      }

      var3 = DomainMBean.class.getMethod("destroySingletonService", SingletonServiceMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("sc", "the SingletonService class to destroy ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes a SingletonService from this domain</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "SingletonServices");
      }

      var3 = DomainMBean.class.getMethod("createMailSession", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Factory method to create MailSession objects</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "MailSessions");
      }

      var3 = DomainMBean.class.getMethod("destroyMailSession", MailSessionMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("ms", "the MailSession to destroy ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes a MailSession from this domain</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "MailSessions");
      }

      var3 = DomainMBean.class.getMethod("createJoltConnectionPool", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Factory to create a JoltConnectionPool instance in the domain</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "JoltConnectionPools");
      }

      var3 = DomainMBean.class.getMethod("destroyJoltConnectionPool", JoltConnectionPoolMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("joltConnectionPool", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>deletes a JoltConnectionPool object</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "JoltConnectionPools");
      }

      var3 = DomainMBean.class.getMethod("createLogFilter", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Creates a log filter MBean instance</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "LogFilters");
      }

      var3 = DomainMBean.class.getMethod("destroyLogFilter", LogFilterMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("logFilter", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Destroy the given log filter MBean</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "LogFilters");
      }

      var3 = DomainMBean.class.getMethod("createDomainLogFilter", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Creates a log filter MBean instance</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "DomainLogFilters");
      }

      var3 = DomainMBean.class.getMethod("destroyDomainLogFilter", DomainLogFilterMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("logFilter", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Destroy the given domain log filter MBean</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "DomainLogFilters");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createFileStore", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Create a new FileStore</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "FileStores");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyFileStore", FileStoreMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("store", "to destroy ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Destroy a file store</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "FileStores");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createJDBCStore", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Create a new JDBCStore</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JDBCStores");
            var6 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
            var2.setValue("rolesAllowed", var6);
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyJDBCStore", JDBCStoreMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("store", "to destroy ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Destroy a file store</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JDBCStores");
            var6 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
            var2.setValue("rolesAllowed", var6);
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createJMSInteropModule", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "- name of bean and base name for descriptor file. ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "Create a new JMS interop module.  The file for this resource will be DOMAIN_DIR/config/<name>.xml ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSInteropModules");
            var6 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
            var2.setValue("rolesAllowed", var6);
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyJMSInteropModule", JMSInteropModuleMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("bean", "- bean to destroy ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "Destroy the given interop module bean and delete the descriptor file that it refers to. ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSInteropModules");
            var6 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
            var2.setValue("rolesAllowed", var6);
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createJMSSystemResource", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "- name of bean and base name for descriptor file. ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "Create a new JMS system resource.  The file for this resource will be DOMAIN_DIR/config/<name>.xml ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSSystemResources");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createJMSSystemResource", String.class, String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "- name of bean "), createParameterDescriptor("descriptorFileName", "- name of descriptor file relative to DOMAIN_DIR/config/jms.. ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Create a new JMS system resource whose descriptor is stored in the given fileName relative to DOMAIN_DIR/config. If not file by this name is defined, it will be created. If a file by this name exists and contains a valid JMS descriptor, the new bean will link to that descriptor.</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSSystemResources");
            var6 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
            var2.setValue("rolesAllowed", var6);
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyJMSSystemResource", JMSSystemResourceMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("bean", "- bean to destroy ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "Destroy the given system resource bean and delete the descriptor file that it refers to. ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSSystemResources");
            var6 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
            var2.setValue("rolesAllowed", var6);
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.1.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createCustomResource", String.class, String.class, String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "- name of bean and base name for descriptor file. "), createParameterDescriptor("resourceClass", "- the name of the class that manages resource's lifecycle. "), createParameterDescriptor("descriptorBeanClass", "- the interface class name for this descriptor bean ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.1.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "Create a new Custom system resource.  The file for this resource will be DOMAIN_DIR/config/custom/<name>.xml ");
            var2.setValue("role", "factory");
            var2.setValue("property", "CustomResources");
            var2.setValue("since", "9.1.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.1.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createCustomResource", String.class, String.class, String.class, String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "- name of bean "), createParameterDescriptor("resourceClass", "- the name of the class that manages resource's lifecycle. "), createParameterDescriptor("descriptorBeanClass", "- the interface class name for this descriptor bean "), createParameterDescriptor("descriptorFileName", "- name of descriptor file relative to DOMAIN_DIR/config/custom/.. ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.1.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Create a new Custom system resource whose descriptor is stored in the given fileName relative to DOMAIN_DIR/config/custom. If not file by this name is defined, it will be created. If a file by this name exists and contains a valid descriptor, the new bean will link to that descriptor.</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "CustomResources");
            var2.setValue("since", "9.1.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.1.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyCustomResource", CustomResourceMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("bean", "- bean to destroy ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.1.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "Destroy the given system resource bean and delete the descriptor file that it refers to. ");
            var2.setValue("role", "factory");
            var2.setValue("property", "CustomResources");
            var2.setValue("since", "9.1.0.0");
         }
      }

      var3 = DomainMBean.class.getMethod("createForeignJNDIProvider", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Create a new diagnostic deployment that can be targeted to a server</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "ForeignJNDIProviders");
      }

      var3 = DomainMBean.class.getMethod("destroyForeignJNDIProvider", ForeignJNDIProviderMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("provider", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Delete a diagnostic deployment configuration from the domain.</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "ForeignJNDIProviders");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createWLDFSystemResource", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "- name of bean and base name for descriptor file. ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "Create a new JMS system resource.  The file for this resource will be DOMAIN_DIR/config/<name>.xml ");
            var2.setValue("role", "factory");
            var2.setValue("property", "WLDFSystemResources");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createWLDFSystemResource", String.class, String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "- name of bean "), createParameterDescriptor("descriptorFileName", "- name of descriptor file relative to DOMAIN_DIR/config/diagnostics. ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Create a new WLDF system resource whose descriptor is stored in the given fileName relative to DOMAIN_DIR/config. If not file by this name is defined, it will be created. If a file by this name exists and contains a valid WLDF descriptor, the new bean will link to that descriptor.</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "WLDFSystemResources");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyWLDFSystemResource", WLDFSystemResourceMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("bean", "- bean to destroy ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "Destroy the given system resource bean and delete the descriptor file that it refers to. ");
            var2.setValue("role", "factory");
            var2.setValue("property", "WLDFSystemResources");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createJDBCSystemResource", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "- name of bean and base name for descriptor file. ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "Create a new JDBC system resource.  The file for this resource will be DOMAIN_DIR/config/<name>.xml ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JDBCSystemResources");
            var6 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
            var2.setValue("rolesAllowed", var6);
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createJDBCSystemResource", String.class, String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "- name of bean "), createParameterDescriptor("descriptorFileName", "- name of descriptor file relative to DOMAIN_DIR/config/jdbc. ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Create a new JDBC system resource whose descriptor is stored in the given fileName relative to DOMAIN_DIR/config. If not file by this name is defined, it will be created. If a file by this name exists and contains a valid JDBC descriptor, the new bean will link to that descriptor.</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JDBCSystemResources");
            var6 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
            var2.setValue("rolesAllowed", var6);
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyJDBCSystemResource", JDBCSystemResourceMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("bean", "- bean to destroy ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "Destroy the given system resource bean and delete the descriptor file that it refers to. ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JDBCSystemResources");
            var6 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
            var2.setValue("rolesAllowed", var6);
            var2.setValue("since", "9.0.0.0");
         }
      }

      var3 = DomainMBean.class.getMethod("createSAFAgent", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Factory method to create SAFAgent object</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "SAFAgents");
      }

      var3 = DomainMBean.class.getMethod("destroySAFAgent", SAFAgentMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("sAFAgent", "object ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes a SAFAgent from this domain</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "SAFAgents");
      }

      var3 = DomainMBean.class.getMethod("createWLECConnectionPool", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Factory method to create WLECConnectionPool object</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "WLECConnectionPools");
      }

      var3 = DomainMBean.class.getMethod("destroyWLECConnectionPool", WLECConnectionPoolMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("store", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes a WLECConnectionPool from this domain</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "WLECConnectionPools");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createErrorHandling", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to create ErrorHandling object</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "ErrorHandlings");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyErrorHandling", ErrorHandlingMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("errorHandling", "object ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Removes a ErrorHandling from this domain</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "ErrorHandlings");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createRemoteSAFContext", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to create SAFRemoteContext object</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "RemoteSAFContexts");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyRemoteSAFContext", RemoteSAFContextMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("remoteSAFContext", "object ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Removes a SAFRemoteContext from this domain</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "RemoteSAFContexts");
            var2.setValue("since", "9.0.0.0");
         }
      }

      var3 = DomainMBean.class.getMethod("createMigratableRMIService", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>This is the factory method for MigratableRMIServices</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "MigratableRMIServices");
      }

      var3 = DomainMBean.class.getMethod("destroyMigratableRMIService", MigratableRMIServiceMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("bean", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Destroys and removes a MigratableRMIService which with the specified short name .</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "MigratableRMIServices");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createAdminServerMBean");
         var7 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var7)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var7, var2);
            var2.setValue("description", " ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "AdminServerMBean");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyAdminServerMBean");
         var7 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var7)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var7, var2);
            var2.setValue("description", " ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "AdminServerMBean");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createJMSDistributedQueueMember", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to create JMSDistributedQueueMember object</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSDistributedQueueMembers");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyJMSDistributedQueueMember", JMSDistributedQueueMemberMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("queue", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Removes a JMSDistributedQueueMember from this domain</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSDistributedQueueMembers");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createJMSDistributedTopicMember", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to create JMSDistributedTopicMember object</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSDistributedTopicMembers");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyJMSDistributedTopicMember", JMSDistributedTopicMemberMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("topic", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Removes a JMSDistributedTopicMember from this domain</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "JMSDistributedTopicMembers");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createSNMPTrapDestination", String.class);
         var7 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var7)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var7, var2);
            var2.setValue("description", " ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPTrapDestinations");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroySNMPTrapDestination", SNMPTrapDestinationMBean.class);
         var7 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var7)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var7, var2);
            var2.setValue("description", " ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPTrapDestinations");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createSNMPProxy", String.class);
         var7 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var7)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var7, var2);
            var2.setValue("description", " ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPProxies");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroySNMPProxy", SNMPProxyMBean.class);
         var7 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var7)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var7, var2);
            var2.setValue("description", " ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPProxies");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createSNMPGaugeMonitor", String.class);
         var7 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var7)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var7, var2);
            var2.setValue("description", " ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPGaugeMonitors");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroySNMPGaugeMonitor", SNMPGaugeMonitorMBean.class);
         var7 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var7)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var7, var2);
            var2.setValue("description", " ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPGaugeMonitors");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createSNMPStringMonitor", String.class);
         var7 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var7)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var7, var2);
            var2.setValue("description", " ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPStringMonitors");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroySNMPStringMonitor", SNMPStringMonitorMBean.class);
         var7 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var7)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var7, var2);
            var2.setValue("description", " ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPStringMonitors");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createSNMPCounterMonitor", String.class);
         var7 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var7)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var7, var2);
            var2.setValue("description", " ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPCounterMonitors");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroySNMPCounterMonitor", SNMPCounterMonitorMBean.class);
         var7 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var7)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var7, var2);
            var2.setValue("description", " ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPCounterMonitors");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createSNMPLogFilter", String.class);
         var7 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var7)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var7, var2);
            var2.setValue("description", " ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPLogFilters");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroySNMPLogFilter", SNMPLogFilterMBean.class);
         var7 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var7)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var7, var2);
            var2.setValue("description", " ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPLogFilters");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createSNMPAttributeChange", String.class);
         var7 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var7)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var7, var2);
            var2.setValue("description", " ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPAttributeChanges");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroySNMPAttributeChange", SNMPAttributeChangeMBean.class);
         var7 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var7)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var7, var2);
            var2.setValue("description", " ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPAttributeChanges");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createWebserviceSecurity", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "of WebserviceSecurity ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "create WebserviceSecurity object ");
            var2.setValue("role", "factory");
            var2.setValue("property", "WebserviceSecurities");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyWebserviceSecurity", WebserviceSecurityMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("wsc", "WebserviceSecurity ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "destroy WebserviceSecurity object ");
            var2.setValue("role", "factory");
            var2.setValue("property", "WebserviceSecurities");
            var2.setValue("since", "9.0.0.0");
         }
      }

      var3 = DomainMBean.class.getMethod("createForeignJMSConnectionFactory", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "of ForeignJMSConnectionFactory ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "create ForeignJMSConnectionFactory object ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "ForeignJMSConnectionFactories");
      }

      var3 = DomainMBean.class.getMethod("destroyForeignJMSConnectionFactory", ForeignJMSConnectionFactoryMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("wsc", "ForeignJMSConnectionFactory ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "destroy ForeignJMSConnectionFactory object ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "ForeignJMSConnectionFactories");
      }

      var3 = DomainMBean.class.getMethod("createForeignJMSDestination", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "of ForeignJMSDestination ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "create ForeignJMSDestination object ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "ForeignJMSDestinations");
      }

      var3 = DomainMBean.class.getMethod("destroyForeignJMSDestination", ForeignJMSDestinationMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("wsc", "ForeignJMSDestination ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "destroy ForeignJMSDestination object ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "ForeignJMSDestinations");
      }

      var3 = DomainMBean.class.getMethod("createJMSConnectionConsumer", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "of JMSConnectionConsumer ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "create JMSConnectionConsumer object ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "JMSConnectionConsumers");
      }

      var3 = DomainMBean.class.getMethod("destroyJMSConnectionConsumer", JMSConnectionConsumerMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("wsc", "JMSConnectionConsumer ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "destroy JMSConnectionConsumer object ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "JMSConnectionConsumers");
      }

      var3 = DomainMBean.class.getMethod("createForeignJMSDestination", String.class, ForeignJMSDestinationMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null), createParameterDescriptor("destination", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "ForeignJMSDestinations");
      }

      var3 = DomainMBean.class.getMethod("createForeignJMSConnectionFactory", String.class, ForeignJMSConnectionFactoryMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null), createParameterDescriptor("factory", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "ForeignJMSConnectionFactories");
      }

      var3 = DomainMBean.class.getMethod("createJMSDistributedQueueMember", String.class, JMSDistributedQueueMemberMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null), createParameterDescriptor("member", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "JMSDistributedQueueMembers");
      }

      var3 = DomainMBean.class.getMethod("createJMSDistributedTopicMember", String.class, JMSDistributedTopicMemberMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null), createParameterDescriptor("member", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "JMSDistributedTopicMembers");
      }

      var3 = DomainMBean.class.getMethod("createJMSTopic", String.class, JMSTopicMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null), createParameterDescriptor("destination", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "JMSTopics");
      }

      var3 = DomainMBean.class.getMethod("createJMSQueue", String.class, JMSQueueMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null), createParameterDescriptor("destination", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "JMSQueues");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.3.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("createCoherenceClusterSystemResource", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "- name of bean. ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "10.3.3.0");
            var1.put(var5, var2);
            var2.setValue("description", "Create a new CoherenceClusterSystemResource.  The file for this resource will be in DOMAIN_DIR/config/coherence/<name>/<name_xxx>.xml ");
            var2.setValue("role", "factory");
            var2.setValue("property", "CoherenceClusterSystemResources");
            var2.setValue("since", "10.3.3.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.3.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("destroyCoherenceClusterSystemResource", CoherenceClusterSystemResourceMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("bean", "- bean to destroy ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "10.3.3.0");
            var1.put(var5, var2);
            var2.setValue("description", "Destroy the given CoherenceClusterSystemResource. ");
            var2.setValue("role", "factory");
            var2.setValue("property", "CoherenceClusterSystemResources");
            var2.setValue("since", "10.3.3.0");
         }
      }

   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = DomainMBean.class.getMethod("lookupWTCServer", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "WTCServers");
      }

      var3 = DomainMBean.class.getMethod("lookupSNMPAgentDeployment", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Finds a SNMPAgentDeploymentMBean with the specified name ");
         var2.setValue("role", "finder");
         var2.setValue("property", "SNMPAgentDeployments");
      }

      var3 = DomainMBean.class.getMethod("lookupServer", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Lookup a particular server from the list.</p> ");
         var2.setValue("role", "finder");
         var2.setValue("property", "Servers");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.4.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupCoherenceServer", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "The name of the Coherence server ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "10.3.4.0");
            var1.put(var5, var2);
            var2.setValue("description", "Find a Coherence server with the given name. ");
            var2.setValue("role", "finder");
            var2.setValue("property", "CoherenceServers");
            var2.setValue("since", "10.3.4.0");
         }
      }

      var3 = DomainMBean.class.getMethod("lookupCluster", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "Clusters");
      }

      var3 = DomainMBean.class.getMethod("lookupFileT3", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "8.1.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "finder");
         var2.setValue("property", "FileT3s");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupJDBCConnectionPool", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.configuration.DomainMBean#JDBCSystemResources} ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Lookup a particular JDBCConnectionPool from the list.</p> ");
            var2.setValue("role", "finder");
            var2.setValue("property", "JDBCConnectionPools");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupJDBCDataSource", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.configuration.DomainMBean#JDBCSystemResources} ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Lookup a particular JDBCDataSource from the list.</p> ");
            var2.setValue("role", "finder");
            var2.setValue("property", "JDBCDataSources");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupJDBCTxDataSource", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.configuration.DomainMBean#JDBCSystemResources} ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Lookup a particular JDBCTxDataSource from the list.</p> ");
            var2.setValue("role", "finder");
            var2.setValue("property", "JDBCTxDataSources");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupJDBCMultiPool", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.configuration.DomainMBean#JDBCSystemResources} ");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Lookup a particular JDBCMultiPool from the list.</p> ");
            var2.setValue("role", "finder");
            var2.setValue("property", "JDBCMultiPools");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      var3 = DomainMBean.class.getMethod("lookupMessagingBridge", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "MessagingBridges");
      }

      var3 = DomainMBean.class.getMethod("lookupApplication", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "Applications");
      }

      String var7;
      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupAppDeployment", String.class);
         var7 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var7)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("since", "9.0.0.0");
            var1.put(var7, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "AppDeployments");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupInternalAppDeployment", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "of the internal application deployment ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "InternalAppDeployments");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupLibrary", String.class);
         var7 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var7)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("since", "9.0.0.0");
            var1.put(var7, var2);
            var2.setValue("description", "Look up the named module. ");
            var2.setValue("role", "finder");
            var2.setValue("property", "Libraries");
            var2.setValue("since", "9.0.0.0");
         }
      }

      var3 = DomainMBean.class.getMethod("lookupDomainLibrary", String.class);
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var7, var2);
         var2.setValue("description", "Look up the named module. ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "finder");
         var2.setValue("property", "DomainLibraries");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupInternalLibrary", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "of the internal library ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "InternalLibraries");
            var2.setValue("since", "9.0.0.0");
         }
      }

      var3 = DomainMBean.class.getMethod("lookupWSReliableDeliveryPolicy", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "WSReliableDeliveryPolicies");
      }

      var3 = DomainMBean.class.getMethod("lookupJDBCDataSourceFactory", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.configuration.DomainMBean#AppDeployment} ");
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "JDBCDataSourceFactories");
      }

      var3 = DomainMBean.class.getMethod("lookupMachine", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "Machines");
      }

      var3 = DomainMBean.class.getMethod("lookupXMLEntityCache", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "of the XMLEntityCache ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Returns an XMLEntityCache object if the given name ");
         var2.setValue("role", "finder");
         var2.setValue("property", "XMLEntityCaches");
      }

      var3 = DomainMBean.class.getMethod("lookupXMLRegistry", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "XMLRegistries");
      }

      var3 = DomainMBean.class.getMethod("lookupFileRealm", String.class);
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "7.0.0.0 ");
         var1.put(var7, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "finder");
         var2.setValue("property", "FileRealms");
      }

      var3 = DomainMBean.class.getMethod("lookupCachingRealm", String.class);
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "7.0.0.0 ");
         var1.put(var7, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "finder");
         var2.setValue("property", "CachingRealms");
      }

      var3 = DomainMBean.class.getMethod("lookupRealm", String.class);
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "7.0.0.0 ");
         var1.put(var7, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "finder");
         var2.setValue("property", "Realms");
      }

      var3 = DomainMBean.class.getMethod("lookupPasswordPolicy", String.class);
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "7.0.0.0 ");
         var1.put(var7, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "finder");
         var2.setValue("property", "PasswordPolicies");
      }

      var3 = DomainMBean.class.getMethod("lookupCustomRealm", String.class);
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "7.0.0.0 ");
         var1.put(var7, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "finder");
         var2.setValue("property", "CustomRealms");
      }

      var3 = DomainMBean.class.getMethod("lookupLDAPRealm", String.class);
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "7.0.0.0 ");
         var1.put(var7, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "finder");
         var2.setValue("property", "LDAPRealms");
      }

      var3 = DomainMBean.class.getMethod("lookupNTRealm", String.class);
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "7.0.0.0 ");
         var1.put(var7, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "finder");
         var2.setValue("property", "NTRealms");
      }

      var3 = DomainMBean.class.getMethod("lookupRDBMSRealm", String.class);
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "7.0.0.0 ");
         var1.put(var7, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "finder");
         var2.setValue("property", "RDBMSRealms");
      }

      var3 = DomainMBean.class.getMethod("lookupUnixRealm", String.class);
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "7.0.0.0 ");
         var1.put(var7, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "finder");
         var2.setValue("property", "UnixRealms");
      }

      var3 = DomainMBean.class.getMethod("lookupJMSServer", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      String[] var6;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "JMSServers");
         var6 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
         var2.setValue("rolesAllowed", var6);
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupJMSStore", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "JMSStores");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupJMSJDBCStore", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "JMSJDBCStores");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupJMSFileStore", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "JMSFileStores");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      var3 = DomainMBean.class.getMethod("lookupJMSDestination", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "JMSDestinations");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupJMSQueue", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "JMSQueues");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupJMSTopic", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "JMSTopics");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupJMSDistributedQueue", String.class);
         var7 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var7)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var7, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "JMSDistributedQueues");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupJMSDistributedTopic", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "JMSDistributedTopics");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupJMSTemplate", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "JMSTemplates");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupNetworkChannel", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "NetworkChannels");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      var3 = DomainMBean.class.getMethod("lookupVirtualHost", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "VirtualHosts");
      }

      var3 = DomainMBean.class.getMethod("lookupMigratableTarget", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Lookup a particular MigratableTarget from the list.</p> ");
         var2.setValue("role", "finder");
         var2.setValue("property", "MigratableTargets");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupPathService", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "finder");
            var2.setValue("property", "PathServices");
            var6 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
            var2.setValue("rolesAllowed", var6);
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupJMSDestinationKey", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "JMSDestinationKeys");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupJMSConnectionFactory", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "JMSConnectionFactories");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupJMSSessionPool", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "JMSSessionPools");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      var3 = DomainMBean.class.getMethod("lookupJMSBridgeDestination", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "JMSBridgeDestinations");
         var6 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
         var2.setValue("rolesAllowed", var6);
      }

      var3 = DomainMBean.class.getMethod("lookupBridgeDestination", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "BridgeDestinations");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupForeignJMSServer", String.class);
         var7 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var7)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var7, var2);
            var2.setValue("description", "Locates a Foreign JMS Server ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "finder");
            var2.setValue("property", "ForeignJMSServers");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      var3 = DomainMBean.class.getMethod("lookupShutdownClass", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "ShutdownClasses");
      }

      var3 = DomainMBean.class.getMethod("lookupStartupClass", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "StartupClasses");
      }

      var3 = DomainMBean.class.getMethod("lookupSingletonService", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "SingletonServices");
      }

      var3 = DomainMBean.class.getMethod("lookupMailSession", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "MailSessions");
      }

      var3 = DomainMBean.class.getMethod("lookupJoltConnectionPool", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Find a JoltConnectionPool object with this name</p> ");
         var2.setValue("role", "finder");
         var2.setValue("property", "JoltConnectionPools");
      }

      var3 = DomainMBean.class.getMethod("lookupLogFilter", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Looks up a log filter by name ");
         var2.setValue("role", "finder");
         var2.setValue("property", "LogFilters");
      }

      var3 = DomainMBean.class.getMethod("lookupDomainLogFilter", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "Looks up a domain log filter by name ");
         var2.setValue("role", "finder");
         var2.setValue("property", "DomainLogFilters");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupFileStore", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "FileStores");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupJDBCStore", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "JDBCStores");
            var6 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
            var2.setValue("rolesAllowed", var6);
            var2.setValue("since", "9.0.0.0");
         }
      }

      var3 = DomainMBean.class.getMethod("lookupJMSInteropModule", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "The name of the JMS Interop resource ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Find a JMSInterop resource with the given name ");
         var2.setValue("role", "finder");
         var2.setValue("property", "JMSInteropModules");
         var6 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
         var2.setValue("rolesAllowed", var6);
      }

      var3 = DomainMBean.class.getMethod("lookupJMSSystemResource", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "The name of the JMS system resource ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Find a JMSSystem resource with the given name ");
         var2.setValue("role", "finder");
         var2.setValue("property", "JMSSystemResources");
         var6 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
         var2.setValue("rolesAllowed", var6);
      }

      var3 = DomainMBean.class.getMethod("lookupCustomResource", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "The name of the JMS system resource ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Find a JMSSystem resource with the given name ");
         var2.setValue("role", "finder");
         var2.setValue("property", "CustomResources");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupForeignJNDIProvider", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "Find a ForeignJNDIProvider resource with the given name ");
            var2.setValue("role", "finder");
            var2.setValue("property", "ForeignJNDIProviders");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupWLDFSystemResource", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "The name of the WLDFSystemResource. ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "Looks up a WLDFSystemResourceMBean by name. ");
            var2.setValue("role", "finder");
            var2.setValue("property", "WLDFSystemResources");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupJDBCSystemResource", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "JDBCSystemResources");
            var6 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
            var2.setValue("rolesAllowed", var6);
            var2.setValue("since", "9.0.0.0");
         }
      }

      var3 = DomainMBean.class.getMethod("lookupSAFAgent", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "SAFAgents");
      }

      var3 = DomainMBean.class.getMethod("lookupWLECConnectionPool", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "WLECConnectionPools");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupErrorHandling", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "ErrorHandlings");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupRemoteSAFContext", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "RemoteSAFContexts");
            var2.setValue("since", "9.0.0.0");
         }
      }

      var3 = DomainMBean.class.getMethod("lookupMigratableRMIService", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Lookup a particular MigratableRMIService from the list.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "finder");
         var2.setValue("property", "MigratableRMIServices");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupJMSDistributedQueueMember", String.class);
         var7 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var7)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var7, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "JMSDistributedQueueMembers");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupJMSDistributedTopicMember", String.class);
         var7 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var7)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var7, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "JMSDistributedTopicMembers");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupWebserviceSecurity", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "name of WebserviceSecurityConfiguration ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "look up WebserviceSecurityConfiguration object ");
            var2.setValue("role", "finder");
            var2.setValue("property", "WebserviceSecurities");
            var2.setValue("since", "9.0.0.0");
         }
      }

      var3 = DomainMBean.class.getMethod("lookupForeignJMSConnectionFactory", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "name of ForeignJMSConnectionFactory ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "look up ForeignJMSConnectionFactory object ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "finder");
         var2.setValue("property", "ForeignJMSConnectionFactories");
      }

      var3 = DomainMBean.class.getMethod("lookupForeignJMSDestination", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "name of ForeignJMSDestination ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "look up ForeignJMSDestinationMBean object ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "finder");
         var2.setValue("property", "ForeignJMSDestinations");
      }

      var3 = DomainMBean.class.getMethod("lookupJMSConnectionConsumer", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "name of the JMSConnectionConsumer ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "look up JMSConnectionConsumer object ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "finder");
         var2.setValue("property", "JMSConnectionConsumers");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.3.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupCoherenceClusterSystemResource", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "The name of the CoherenceClusterSystemResource ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "10.3.3.0");
            var1.put(var5, var2);
            var2.setValue("description", "Find a CoherenceClusterSystemResource with the given name. ");
            var2.setValue("role", "finder");
            var2.setValue("property", "CoherenceClusterSystemResources");
            var2.setValue("since", "10.3.3.0");
         }
      }

   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = DomainMBean.class.getMethod("freezeCurrentValue", String.class);
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

      var3 = DomainMBean.class.getMethod("restoreDefaultValue", String.class);
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

      String var6;
      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("discoverManagedServers");
         var6 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var6)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var6, var2);
            var2.setValue("description", "<p>Admin Server's knowledge of running Managed Servers is refreshed. Particularly useful when Admin Server is re-started.</p> ");
            var2.setValue("role", "operation");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("discoverManagedServer", String.class);
         var6 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var6)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var6, var2);
            var2.setValue("description", "<p>Admin Server's knowledge of running Managed Servers is refreshed. Particularly useful when Admin Server is re-started.</p> ");
            var2.setValue("role", "operation");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("getDisconnectedManagedServers");
         var6 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var6)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "Use the ServerLifecycleRuntime ");
            var1.put(var6, var2);
            var2.setValue("description", "<p> this method provides an array of strings that are the names of the managed servers that are not currently connected.</p> ");
            var2.setValue("role", "operation");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("start");
         var6 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var6)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var6, var2);
            var2.setValue("description", "<p>These operations are used to start and forceShutdown all the servers belonging to the Domain. HashMap contains references to TaskRuntimeMBeans corresponding to each server in the Domain, keyed using the server name.</p> ");
            var2.setValue("role", "operation");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("kill");
         var6 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var6)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("obsolete", "9.0.0.0");
            var2.setValue("deprecated", "9.0.0.0 ");
            var1.put(var6, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "operation");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      var3 = DomainMBean.class.getMethod("lookupTarget", String.class);
      var6 = BeanInfoHelper.buildMethodKey(var3);
      String[] var7;
      if (!var1.containsKey(var6)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var6, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
         var7 = new String[]{BeanInfoHelper.encodeEntities("Deployer"), BeanInfoHelper.encodeEntities("Operator")};
         var2.setValue("rolesAllowed", var7);
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = DomainMBean.class.getMethod("lookupSystemResource", String.class);
         var6 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var6)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("since", "9.0.0.0");
            var1.put(var6, var2);
            var2.setValue("description", "<p>Lookup a particular SystemResource in this domain.</p> ");
            var2.setValue("role", "operation");
            var7 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
            var2.setValue("rolesAllowed", var7);
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
