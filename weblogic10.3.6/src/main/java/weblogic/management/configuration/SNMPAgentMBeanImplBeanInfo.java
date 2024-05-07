package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class SNMPAgentMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SNMPAgentMBean.class;

   public SNMPAgentMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SNMPAgentMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SNMPAgentMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("dynamic", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This singleton MBean represents an SNMP agent that is scoped to a WebLogic Server domain. <p>This MBean is provided to support domains that were created with WebLogic Server release 9.2 and earlier. For new domains, create an instance of {@link SNMPAgentDeploymentMBean} and target it to the domain's Administration Server.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.SNMPAgentMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("AuthenticationProtocol")) {
         var3 = "getAuthenticationProtocol";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAuthenticationProtocol";
         }

         var2 = new PropertyDescriptor("AuthenticationProtocol", SNMPAgentMBean.class, var3, var4);
         var1.put("AuthenticationProtocol", var2);
         var2.setValue("description", "<p>The protocol that this SNMP agent uses to ensure that only authorized users can request or receive information about your WebLogic Server domain. Applicable only with SNMPv3.</p> <p>The protocol also ensures message integrity and prevents masquerading and reordered, delayed, or replayed messages.</p> <p>To use this protocol when receiving requests from SNMP managers, you must configure credential mapping in the WebLogic Server security realm. To use this protocol when sending responses or notifications, you must configure the security level of your trap destinations.</p> <p>If you do not choose an authentication protocol, then the SNMP agent does not authenticate incoming SNMPv3 requests; anyone can use SNMPv3 to retrieve information about your WebLogic Server domain.</p> ");
         setPropertyDescriptorDefault(var2, "noAuth");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("legalValues", new Object[]{"noAuth", "MD5", "SHA"});
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "10.0.0.0");
      }

      if (!var1.containsKey("CommunityPrefix")) {
         var3 = "getCommunityPrefix";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCommunityPrefix";
         }

         var2 = new PropertyDescriptor("CommunityPrefix", SNMPAgentMBean.class, var3, var4);
         var1.put("CommunityPrefix", var2);
         var2.setValue("description", "<p>The password (community name) that you want this SNMP agent to use to secure SNMPv1 or v2 communication with SNMP managers. Requires you to enable community based access for this agent.</p>  <p>SNMPv3 does not use community names. Instead, it encrypts user names and passwords in its PDUs. </p>  <p>When you use SNMPv1 or v2, there are two community names that are needed when the WebLogic SNMP agent and SNMP managers interact:</p>  <ul> <li> <p>The name that you specify in this community prefix. All SNMP managers must send this name when connecting to this SNMP agent.</p> </li>  <li> <p>The community name that the SNMP manager defines. The SNMP agent must send this name when connecting to the manager. (You supply this community name when you configure a trap destination.)</p> </li> </ul>  <p>In addition to using the community prefix as a password, an SNMP agent on an Administration Server uses the prefix to qualify requests from SNMP managers. Because the Administration Server can access data for all WebLogic Server instances in a domain, a request that specifies only an attribute name is potentially ambiguous. For example, the attribute <code>serverUptime</code> exists for each WebLogic Server instance in a domain. To clarify requests that you send to SNMP agents on Administration Servers, use the community prefix as follows:</p>  <ul> <li> <p>To request the value of an attribute on a specific Managed Server, when you send a request from an SNMP manager, append the name of the server instance to the community prefix: <code><i>community_prefix@server_name</i></code>.</p> </li>  <li> <p>To request the value of an attribute for all server instances in a domain, send a community name with the following form: <code><i>community_prefix</i></code></p> </li> </ul>  <p>To secure access to the values of the WebLogic attributes when using the SNMPv1 or v2 protocols, it is recommended that you set community prefix to a value other than <code>public</code>.</p>  <p>You cannot specify a null (empty) value for the community prefix. If you delete the prefix value, WebLogic Server resets the value to <code>public</code>. If you do not want this agent to receive SNMPv1 or v2 requests, instead of trying to set the community prefix to a null value, disable community based access. With community based access disabled, WebLogic Server ignores the community prefix value.</p> ");
         setPropertyDescriptorDefault(var2, "public");
         var2.setValue("secureValue", "not public");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DebugLevel")) {
         var3 = "getDebugLevel";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDebugLevel";
         }

         var2 = new PropertyDescriptor("DebugLevel", SNMPAgentMBean.class, var3, var4);
         var1.put("DebugLevel", var2);
         var2.setValue("description", "<p>The minimum severity of debug messages that this SNMP agent generates.</p>  <p>The SNMP agent writes all debug messages to standard out; they are not written to the WebLogic Server log files. Debug messages provide a detailed description of the SNMP agent's actions. For example, the agent outputs a noncritical message each time it generates a notification.</p>  <p>Valid values are:</p>  <ul> <li><code>0</code>  <p>No debug messages.</p> </li>  <li><code>1</code>  <p>Fatal messages only.</p> </li>  <li><code>2</code>  <p>Critical and fatal messages.</p> </li>  <li><code>3</code>  <p>Non-critical, critical, and, fatal messages.</p> </li> </ul> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("secureValue", new Integer(0));
         var2.setValue("legalValues", new Object[]{new Integer(0), new Integer(1), new Integer(2), new Integer(3)});
         var2.setValue("deprecated", "10.0.0.0 Use the ServerDebugMBean.DebugSNMPToolkit attribute to configure the SNMP Toolkit debug ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("InformRetryInterval")) {
         var3 = "getInformRetryInterval";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setInformRetryInterval";
         }

         var2 = new PropertyDescriptor("InformRetryInterval", SNMPAgentMBean.class, var3, var4);
         var1.put("InformRetryInterval", var2);
         var2.setValue("description", "<p>The number of milliseconds that this SNMP agent will wait for a response to an INFORM notification.</p> <p>If the agent does not receive a response within the specified interval, it will resend the notification.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(10000));
         var2.setValue("legalMax", new Integer(30000));
         var2.setValue("legalMin", new Integer(3000));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "10.0.0.0");
      }

      String[] var5;
      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("LocalizedKeyCacheInvalidationInterval")) {
         var3 = "getLocalizedKeyCacheInvalidationInterval";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLocalizedKeyCacheInvalidationInterval";
         }

         var2 = new PropertyDescriptor("LocalizedKeyCacheInvalidationInterval", SNMPAgentMBean.class, var3, var4);
         var1.put("LocalizedKeyCacheInvalidationInterval", var2);
         var2.setValue("description", "<p>The number of milliseconds after which WebLogic Server invalidates its cache of SNMP security keys. Setting a high value creates a risk that users whose credentials have been removed can still access SNMP data.</p> <p>An SNMP security key is an encrypted version of an SNMP agent's engine ID and an authentication password or privacy password. WebLogic Server generates one security key for each entry that you create in the SNMP credential map. When a WebLogic Server SNMP agent receives an SNMPv3 request, it compares the key that is in the request with its WebLogic Server keys. If it finds a match, it processes the request. The SNMP agent also encodes these keys in its responses and notifications. (You configure which keys are encoded when you create a trap destination.)</p> <p>Instead of regenerating the keys for each SNMPv3 communication, WebLogic Server caches the keys. To make sure that the cache contains the latest set of SNMP credentials, WebLogic Server periodically invalidates the cache. After the cache is invalidated, the next time an SNMP agent requests credentials, WebLogic Server regenerates the cache.</p> <p>Note that making a change to the credential map does not automatically update the cache. Instead, the cache is updated only after it has been invalidated.</p> <p>For example, if you update a privacy password in an existing entry in the SNMP credential map, the SNMP agent is not aware of the new password until the key cache is invalidated and regenerated. An SNMP user with the old security password can still access WebLogic Server data until the cache is invalidated.</p> <p>You can invalidate a key immediately instead of waiting for this invalidation interval to expire. </p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.runtime.SNMPAgentRuntimeMBean#invalidateLocalizedKeyCache(String)"), BeanInfoHelper.encodeEntities("SNMPTrapDestinationMBean")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Long(3600000L));
         var2.setValue("legalMax", new Long(86400000L));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "10.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("MasterAgentXPort")) {
         var3 = "getMasterAgentXPort";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMasterAgentXPort";
         }

         var2 = new PropertyDescriptor("MasterAgentXPort", SNMPAgentMBean.class, var3, var4);
         var1.put("MasterAgentXPort", var2);
         var2.setValue("description", "<p>The port that this SNMP agent uses to communicate with its subagents.</p> <p>The agent uses subagents to provide access to custom MBeans (MBeans that you create and register) and to other software components. WebLogic Server SNMP agents do not enable users to register their own subagents.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(705));
         var2.setValue("legalMax", new Integer(65535));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "10.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("MaxInformRetryCount")) {
         var3 = "getMaxInformRetryCount";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxInformRetryCount";
         }

         var2 = new PropertyDescriptor("MaxInformRetryCount", SNMPAgentMBean.class, var3, var4);
         var1.put("MaxInformRetryCount", var2);
         var2.setValue("description", "<p>The maximum number of times that this SNMP agent will resend INFORM notifications for which it has not received a response.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(1));
         var2.setValue("legalMax", new Integer(3));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "10.0.0.0");
      }

      if (!var1.containsKey("MibDataRefreshInterval")) {
         var3 = "getMibDataRefreshInterval";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMibDataRefreshInterval";
         }

         var2 = new PropertyDescriptor("MibDataRefreshInterval", SNMPAgentMBean.class, var3, var4);
         var1.put("MibDataRefreshInterval", var2);
         var2.setValue("description", "<p>The minimum number of seconds that this SNMP agent caches OIDs before checking if new ones have been added to the Management Information Base (MIB).</p>  <p>A MIB is a database of all objects that can be managed through SNMP. When you create a new WebLogic Server resource, the SNMP agent assigns a unique OID to the resource and adds it to the MIB. For example, when you create a new server, the SNMP agent adds an OID to the MIB.</p>  <p>This attribute is not used by the SNMP Agent anymore. The SNMP Agent retrieves internal notifications about MBean registrations in the WLS MBeanServers.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(120));
         var2.setValue("legalMax", new Integer(65535));
         var2.setValue("legalMin", new Integer(30));
         var2.setValue("deprecated", "10.0.0.0 There is no replacement for this attribute. ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", SNMPAgentMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("PrivacyProtocol")) {
         var3 = "getPrivacyProtocol";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPrivacyProtocol";
         }

         var2 = new PropertyDescriptor("PrivacyProtocol", SNMPAgentMBean.class, var3, var4);
         var1.put("PrivacyProtocol", var2);
         var2.setValue("description", "<p>The protocol that this SNMP agent uses to encrypt and unencrypt messages. Applicable only with SNMPv3. Requires you to also use an authentication protocol.</p> <p>To use this protocol when sending responses or notifications, you must also configure the security level of your trap destinations.</p> <p>If you do not choose a privacy protocol, then communication between this agent and managers can be viewed (but not altered) by unauthorized users.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("SNMPTrapDestinationMBean#getSecurityLevel()")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, "noPriv");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("legalValues", new Object[]{"noPriv", "DES", "AES_128"});
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "10.0.0.0");
      }

      if (!var1.containsKey("SNMPAttributeChanges")) {
         var3 = "getSNMPAttributeChanges";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSNMPAttributeChanges";
         }

         var2 = new PropertyDescriptor("SNMPAttributeChanges", SNMPAgentMBean.class, var3, var4);
         var1.put("SNMPAttributeChanges", var2);
         var2.setValue("description", "<p>The <code>SNMPAttributeChangeMBeans</code> which describe the MBean type and Attribute name for which attribute change notification should be sent when an attribute change is observed.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.SNMPAttributeChangeMBean")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroySNMPAttributeChange");
         var2.setValue("creator", "createSNMPAttributeChange");
         var2.setValue("creator", "createSNMPAttributeChange");
         var2.setValue("setterDeprecated", "9.0.0.0 Instead, use standard JMX design patterns using <code>javax.management.MBeanServerConnection</code> interface. ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SNMPCounterMonitors")) {
         var3 = "getSNMPCounterMonitors";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSNMPCounterMonitors";
         }

         var2 = new PropertyDescriptor("SNMPCounterMonitors", SNMPAgentMBean.class, var3, var4);
         var1.put("SNMPCounterMonitors", var2);
         var2.setValue("description", "<p>The <code>SNMPCounterMonitorMBeans</code> which describe the criteria for generating notifications based on JMX CounterMonitor.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.SNMPCounterMonitorMBean")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroySNMPCounterMonitor");
         var2.setValue("creator", "createSNMPCounterMonitor");
         var2.setValue("creator", "createSNMPCounterMonitor");
         var2.setValue("setterDeprecated", "9.0.0.0 Instead, use standard JMX design patterns using <code>javax.management.MBeanServerConnection</code> interface. ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("SNMPEngineId")) {
         var3 = "getSNMPEngineId";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSNMPEngineId";
         }

         var2 = new PropertyDescriptor("SNMPEngineId", SNMPAgentMBean.class, var3, var4);
         var1.put("SNMPEngineId", var2);
         var2.setValue("description", "<p>An identifier for this SNMP agent that is unique amongst all other SNMP agents in the current WebLogic Server domain.</p> <p>If you use SNMPv3 to send messages to this SNMP agent, you must specify the SNMP engine ID when you configure the SNMP manager.</p> <p>For an SNMP agent on an Administration Server, the default value is the name of the WebLogic Server domain. For an agent on a Managed Server, the default is the name of the server.</p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "10.0.0.0");
      }

      if (!var1.containsKey("SNMPGaugeMonitors")) {
         var3 = "getSNMPGaugeMonitors";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSNMPGaugeMonitors";
         }

         var2 = new PropertyDescriptor("SNMPGaugeMonitors", SNMPAgentMBean.class, var3, var4);
         var1.put("SNMPGaugeMonitors", var2);
         var2.setValue("description", "<p>The <code>SNMPGaugeMonitorMBeans</code> which describe the criteria for generating notifications based on JMX GaugeMonitor.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.SNMPGaugeMonitorMBean")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroySNMPGaugeMonitor");
         var2.setValue("creator", "createSNMPGaugeMonitor");
         var2.setValue("creator", "createSNMPGaugeMonitor");
         var2.setValue("setterDeprecated", "9.0.0.0 Instead, use standard JMX design patterns using <code>javax.management.MBeanServerConnection</code> interface. ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SNMPLogFilters")) {
         var3 = "getSNMPLogFilters";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSNMPLogFilters";
         }

         var2 = new PropertyDescriptor("SNMPLogFilters", SNMPAgentMBean.class, var3, var4);
         var1.put("SNMPLogFilters", var2);
         var2.setValue("description", "<p>The <code>SNMPLogFilterMBeans</code> which describe filters for generating notifications based on server log messages.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.SNMPLogFilterMBean")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroySNMPLogFilter");
         var2.setValue("creator", "createSNMPLogFilter");
         var2.setValue("creator", "createSNMPLogFilter");
         var2.setValue("setterDeprecated", "9.0.0.0 Instead, use standard JMX design patterns using <code>javax.management.MBeanServerConnection</code> interface. ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SNMPPort")) {
         var3 = "getSNMPPort";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSNMPPort";
         }

         var2 = new PropertyDescriptor("SNMPPort", SNMPAgentMBean.class, var3, var4);
         var1.put("SNMPPort", var2);
         var2.setValue("description", "<p>The port on which you want this SNMP agent to listen for incoming requests from SNMP managers that use the UDP protocol.</p>  <p>SNMP managers can use this port to ping the SNMP agent and request the status of specific attributes.</p>  <p>If you target this SNMP agent to multiple server instances, and if two or more servers are running on the same computer, WebLogic Server will automatically increment this UDP port value by 1 for each agent. WebLogic Server never assigns port 162 because it is the default port that an agent uses to send notifications. In addition, if any port is already in use, WebLogic Server skips the port and assigns the next available port.</p> <p>For example, if you use the default value of this attribute and then target this agent to ManagedServer1 and ManagedServer2, and if both servers are running on the same computer, then the agent on ManagedServer1 will listen on UDP port 161 and the agent on ManagedServer2 will listen on UDP port 163.</p> <p>The incremented port number is not persisted in the domain's configuration; when WebLogic Server increments port numbers, it does so in the order in which servers are started on the same computer.</p> <p>If WebLogic Server re-assigns the UDP port for an SNMP agent, look in the agent's SNMPAgentRuntimeMBean to see the agent's runtime UDP port.</p> <p>SNMP agents can also communicate through the host server's TCP listen port (7001 by default) or through a TCP port that is configured by a custom network channel.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.runtime.SNMPAgentRuntimeMBean"), BeanInfoHelper.encodeEntities("ServerMBean#getListenPort()"), BeanInfoHelper.encodeEntities("NetworkAccessPointMBean")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(161));
         var2.setValue("legalMax", new Integer(65535));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SNMPProxies")) {
         var3 = "getSNMPProxies";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSNMPProxies";
         }

         var2 = new PropertyDescriptor("SNMPProxies", SNMPAgentMBean.class, var3, var4);
         var1.put("SNMPProxies", var2);
         var2.setValue("description", "<p>The SNMP agents for which this SNMP agent is a proxy. <code>SNMPProxyMBeans</code> describe settings for SNMP agents to be proxied by this SNMP agent.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.SNMPProxyMBean")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createSNMPProxy");
         var2.setValue("creator", "createSNMPProxy");
         var2.setValue("destroyer", "destroySNMPProxy");
         var2.setValue("setterDeprecated", "9.0.0.0 Instead, use standard JMX design patterns using <code>javax.management.MBeanServerConnection</code> interface. ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SNMPStringMonitors")) {
         var3 = "getSNMPStringMonitors";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSNMPStringMonitors";
         }

         var2 = new PropertyDescriptor("SNMPStringMonitors", SNMPAgentMBean.class, var3, var4);
         var1.put("SNMPStringMonitors", var2);
         var2.setValue("description", "<p>The <code>SNMPStringMonitorMBeans</code> which describe the criteria for generating notifications based on JMX StringMonitor.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.SNMPStringMonitorMBean")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroySNMPStringMonitor");
         var2.setValue("creator", "createSNMPStringMonitor");
         var2.setValue("creator", "createSNMPStringMonitor");
         var2.setValue("setterDeprecated", "9.0.0.0 Instead, use standard JMX design patterns using <code>javax.management.MBeanServerConnection</code> interface. ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("SNMPTrapDestinations")) {
         var3 = "getSNMPTrapDestinations";
         var4 = null;
         var2 = new PropertyDescriptor("SNMPTrapDestinations", SNMPAgentMBean.class, var3, var4);
         var1.put("SNMPTrapDestinations", var2);
         var2.setValue("description", "<p>WebLogic Server uses a trap destination to specify the SNMP management station and the community name used by the SNMP agent to send notifications. Select which trap destination(s) should be used in this WebLogic Server domain from the list of available trap destinations.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroySNMPTrapDestination");
         var2.setValue("creator", "createSNMPTrapDestination");
         var2.setValue("creator", "createSNMPTrapDestination");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("SNMPTrapVersion")) {
         var3 = "getSNMPTrapVersion";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSNMPTrapVersion";
         }

         var2 = new PropertyDescriptor("SNMPTrapVersion", SNMPAgentMBean.class, var3, var4);
         var1.put("SNMPTrapVersion", var2);
         var2.setValue("description", "<p>The SNMP notification version that this SNMP agent generates.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("SNMPAgentMBean#isCommunityBasedAccessEnabled")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(1));
         var2.setValue("legalValues", new Object[]{new Integer(1), new Integer(2), new Integer(3)});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ServerStatusCheckIntervalFactor")) {
         var3 = "getServerStatusCheckIntervalFactor";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setServerStatusCheckIntervalFactor";
         }

         var2 = new PropertyDescriptor("ServerStatusCheckIntervalFactor", SNMPAgentMBean.class, var3, var4);
         var1.put("ServerStatusCheckIntervalFactor", var2);
         var2.setValue("description", "<p>The multiplier used to calculate the interval at which this SNMP agent checks for newly started or shut down server instances.</p>  <p>You can enable the SNMP agent to automatically generate serverStartup and serverShutdown notifications when servers start or shut down. See {@link SNMPAgentMBean#isSendAutomaticTrapsEnabled}.</p>  <p>This status check value is multiplied by the MIB Data Refresh Interval to determine the interval:<br clear=\"none\" /> <code>interval = n * MibDataRefreshInterval</code></p>  <p>The SNMP Agent uses internal notifications to update itself when a server is restarted so there is no need to poll the server for their status.</p>  <p>For the most frequent interval, specify <code>1</code> as the multiplier value.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(1));
         var2.setValue("legalMax", new Integer(65535));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("deprecated", "10.0.0.0 There is no replacement for this attribute. ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("TargetedTrapDestinations")) {
         var3 = "getTargetedTrapDestinations";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTargetedTrapDestinations";
         }

         var2 = new PropertyDescriptor("TargetedTrapDestinations", SNMPAgentMBean.class, var3, var4);
         var1.put("TargetedTrapDestinations", var2);
         var2.setValue("description", "<p>WebLogic Server uses a trap destination to specify the SNMP management station and the community name used by the SNMP agent to send trap notifications.</p> <p>This attribute contains the collection of trap destinations that have been configured for this SNMP agent.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("adder", "addTargetedTrapDestination");
         var2.setValue("remover", "removeTargetedTrapDestination");
         var2.setValue("deprecated", "9.0.0.0 Use the getSNMPTrapDestinations() method instead. ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("CommunityBasedAccessEnabled")) {
         var3 = "isCommunityBasedAccessEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCommunityBasedAccessEnabled";
         }

         var2 = new PropertyDescriptor("CommunityBasedAccessEnabled", SNMPAgentMBean.class, var3, var4);
         var1.put("CommunityBasedAccessEnabled", var2);
         var2.setValue("description", "<p>Specifies whether this SNMP agent supports SNMPv1 and v2.</p> <p>SNMPv1 and v2 use community strings for authentication. If you disable community strings for this SNMP agent, the agent will process only SNMPv3 requests. If an SNMP manager sends a v1 or v2 message, the agent discards the message and returns an error code to the manager.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "10.0.0.0");
      }

      if (!var1.containsKey("Enabled")) {
         var3 = "isEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEnabled";
         }

         var2 = new PropertyDescriptor("Enabled", SNMPAgentMBean.class, var3, var4);
         var1.put("Enabled", var2);
         var2.setValue("description", "<p>Specifies whether this SNMP agent is enabled.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("InformEnabled")) {
         var3 = "isInformEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setInformEnabled";
         }

         var2 = new PropertyDescriptor("InformEnabled", SNMPAgentMBean.class, var3, var4);
         var1.put("InformEnabled", var2);
         var2.setValue("description", "<p>Configures this SNMP agent to send notifications as an INFORM instead of a TRAP. Requires you to specify the agent's SNMPTrapVersion as SNMPv2 or SNMPv3.</p> <p>When an agent sends an INFORM notification, it waits for a confirmation response from the SNMP manager. If it does not receive a response, it resends the INFORM notification.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getSNMPTrapVersion()")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "10.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("SNMPAccessForUserMBeansEnabled")) {
         var3 = "isSNMPAccessForUserMBeansEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSNMPAccessForUserMBeansEnabled";
         }

         var2 = new PropertyDescriptor("SNMPAccessForUserMBeansEnabled", SNMPAgentMBean.class, var3, var4);
         var1.put("SNMPAccessForUserMBeansEnabled", var2);
         var2.setValue("description", "<p>Configures this SNMP agent to provide read-only access to MBean types that you have created and registered (custom MBeans).</p> <p>If you enable this access, when you register a custom MBean in a WebLogic Server MBeanServer, this SNMP agent dynamically updates a runtime MIB module that WebLogic Server maintains for custom MBeans. </p> <p>For each custom MBean type, WebLogic Server adds a table to the MIB module. For each instance of the custom MBean, it adds a table row. While WebLogic Server does not persist the MIB as a file or other data structure, the OIDs in the MIB remain constant across server sessions.</p> <p>The MIB module for custom MBeans is managed by a subAgent. Its master agent is this WebLogic Server SNMP agent and it communicates with the master agent through the AgentX port.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getMasterAgentXPort()")};
         var2.setValue("see", var5);
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "10.0.0.0");
      }

      if (!var1.containsKey("SendAutomaticTrapsEnabled")) {
         var3 = "isSendAutomaticTrapsEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSendAutomaticTrapsEnabled";
         }

         var2 = new PropertyDescriptor("SendAutomaticTrapsEnabled", SNMPAgentMBean.class, var3, var4);
         var1.put("SendAutomaticTrapsEnabled", var2);
         var2.setValue("description", "<p>Specifies whether this SNMP agent sends automatically generated notifications to SNMP managers.</p>  <p>The SNMP agent generates automatic notifications when any of the following events occur:</p>  <ul> <li><p>The WebLogic Server instance that is hosting the SNMP agent starts.</p> <p>This type of notification (coldStart) has no variable bindings.</p></li> <li> <p>A server instance starts or stops.</p> <p>An SNMP agent on a Managed Server generates these notifications only when its host Managed Server starts or stops. An SNMP agent on an Administration Server generates these notifications when any server in the domain starts or stops.</p> <p>These notification types (serverStart and serverShutdown) contain variable bindings to identify the server that started or stopped and the time at which the notification was generated.</p> </li> </ul> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("SNMPAgentMBean#getServerStatusCheckIntervalFactor")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      MethodDescriptor var2;
      Method var3;
      ParameterDescriptor[] var4;
      String var5;
      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SNMPAgentMBean.class.getMethod("createSNMPTrapDestination", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to create SNMPTrapDestination objects</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPTrapDestinations");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = SNMPAgentMBean.class.getMethod("createSNMPTrapDestination", String.class, SNMPTrapDestinationMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null), createParameterDescriptor("toClone", "is the SNMPTrapDestinationMBean that is being moved from the DomainMBean ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to clone an SNMPTrapDestination object</p> ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPTrapDestinations");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SNMPAgentMBean.class.getMethod("destroySNMPTrapDestination", SNMPTrapDestinationMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("SNMPTrapDestination", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Removes a SNMPTrapDestination from this SNMPAgent</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPTrapDestinations");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SNMPAgentMBean.class.getMethod("createSNMPProxy", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to create SNMPProxy objects</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPProxies");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = SNMPAgentMBean.class.getMethod("createSNMPProxy", String.class, SNMPProxyMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null), createParameterDescriptor("toClone", "is the SNMPProxyMBean that is being moved from the DomainMBean ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to clone an SNMPProxy object</p> ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPProxies");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SNMPAgentMBean.class.getMethod("destroySNMPProxy", SNMPProxyMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("SNMPProxy", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Removes a SNMPProxy from this domain</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPProxies");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SNMPAgentMBean.class.getMethod("createSNMPGaugeMonitor", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to create SNMPGaugeMonitor objects</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPGaugeMonitors");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = SNMPAgentMBean.class.getMethod("createSNMPGaugeMonitor", String.class, SNMPGaugeMonitorMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null), createParameterDescriptor("toClone", "is the SNMPGaugeMonitorMBean that is being moved from the DomainMBean ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to clone an SNMPGaugeMonitor object</p> ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPGaugeMonitors");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SNMPAgentMBean.class.getMethod("destroySNMPGaugeMonitor", SNMPGaugeMonitorMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("SNMPGaugeMonitor", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Removes a SNMPGaugeMonitor from this domain</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPGaugeMonitors");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SNMPAgentMBean.class.getMethod("createSNMPStringMonitor", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to create SNMPStringMonitor objects</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPStringMonitors");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = SNMPAgentMBean.class.getMethod("createSNMPStringMonitor", String.class, SNMPStringMonitorMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null), createParameterDescriptor("toClone", "is the SNMPStringMonitorMBean that is being moved from the DomainMBean ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to clone an SNMPStringMonitor object</p> ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPStringMonitors");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SNMPAgentMBean.class.getMethod("destroySNMPStringMonitor", SNMPStringMonitorMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("SNMPStringMonitor", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Removes a SNMPStringMonitor from this domain</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPStringMonitors");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SNMPAgentMBean.class.getMethod("createSNMPCounterMonitor", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to create SNMPCounterMonitor objects</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPCounterMonitors");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = SNMPAgentMBean.class.getMethod("createSNMPCounterMonitor", String.class, SNMPCounterMonitorMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null), createParameterDescriptor("toClone", "is the SNMPCounterMonitorMBean that is being moved from the DomainMBean ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to clone an SNMPCounterMonitor object</p> ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPCounterMonitors");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SNMPAgentMBean.class.getMethod("destroySNMPCounterMonitor", SNMPCounterMonitorMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("SNMPCounterMonitor", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Removes a SNMPCounterMonitor from this domain</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPCounterMonitors");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SNMPAgentMBean.class.getMethod("createSNMPLogFilter", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to create SNMPLogFilter objects</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPLogFilters");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = SNMPAgentMBean.class.getMethod("createSNMPLogFilter", String.class, SNMPLogFilterMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null), createParameterDescriptor("toClone", "is the SNMPLogFilterMBean that is being moved from the DomainMBean ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to clone an SNMPLogFilter object</p> ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPLogFilters");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SNMPAgentMBean.class.getMethod("destroySNMPLogFilter", SNMPLogFilterMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("SNMPLogFilter", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Removes a SNMPLogFilter from this domain</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPLogFilters");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SNMPAgentMBean.class.getMethod("createSNMPAttributeChange", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to create SNMPAttributeChange objects</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPAttributeChanges");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion)) {
         var3 = SNMPAgentMBean.class.getMethod("createSNMPAttributeChange", String.class, SNMPAttributeChangeMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null), createParameterDescriptor("toClone", "is the SNMPAttributeChangeMBean that is being moved from the DomainMBean ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("obsolete", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Factory method to clone an SNMPAttributeChange object</p> ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPAttributeChanges");
            var2.setValue("obsolete", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SNMPAgentMBean.class.getMethod("destroySNMPAttributeChange", SNMPAttributeChangeMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("SNMPAttributeChange", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "<p>Removes a SNMPAttributeChange from this domain</p> ");
            var2.setValue("role", "factory");
            var2.setValue("property", "SNMPAttributeChanges");
            var2.setValue("since", "9.0.0.0");
         }
      }

   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = SNMPAgentMBean.class.getMethod("addTargetedTrapDestination", SNMPTrapDestinationMBean.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("trapdestination", "The feature to be added to the TargetedTrapDestination attribute ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      String[] var6;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Adds a single trap destination to this SNMP agent's list of targeted trap destinations.</p> ");
         var6 = new String[]{BeanInfoHelper.encodeEntities("#getTargetedTrapDestinations")};
         var2.setValue("see", var6);
         var2.setValue("role", "collection");
         var2.setValue("property", "TargetedTrapDestinations");
      }

      var3 = SNMPAgentMBean.class.getMethod("removeTargetedTrapDestination", SNMPTrapDestinationMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("trapdestination", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "collection");
         var2.setValue("property", "TargetedTrapDestinations");
      }

      var3 = SNMPAgentMBean.class.getMethod("addSNMPTrapDestination", SNMPTrapDestinationMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("trapdestination", "The feature to be added to the SNMPTrapDestination attribute ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 Instead, use standard JMX design patterns using <code>javax.management.MBeanServerConnection</code> interface. ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Adds an SNMPTrapDestination to the SNMPAgentMBean object</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "SNMPTrapDestinations");
      }

      var3 = SNMPAgentMBean.class.getMethod("addSNMPProxy", SNMPProxyMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("snmpProxy", "The feature to be added to the SNMPProxy attribute ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 Instead, use standard JMX design patterns using <code>javax.management.MBeanServerConnection</code> interface. ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Adds a feature to the SNMPProxy attribute of the SNMPAgentMBean object</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "SNMPProxies");
      }

      var3 = SNMPAgentMBean.class.getMethod("removeSNMPProxy", SNMPProxyMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("snmpProxy", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 Instead, use standard JMX design patterns using <code>javax.management.MBeanServerConnection</code> interface. ");
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "collection");
         var2.setValue("property", "SNMPProxies");
      }

      var3 = SNMPAgentMBean.class.getMethod("addSNMPGaugeMonitor", SNMPGaugeMonitorMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("gaugemonitor", "The feature to be added to the SNMPGaugeMonitor attribute ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 Instead, use standard JMX design patterns using <code>javax.management.MBeanServerConnection</code> interface. ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Adds a feature to the SNMPGaugeMonitor attribute of the SNMPAgentMBean object</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "SNMPGaugeMonitors");
      }

      var3 = SNMPAgentMBean.class.getMethod("removeSNMPGaugeMonitor", SNMPGaugeMonitorMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("gaugemonitor", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 Instead, use standard JMX design patterns using <code>javax.management.MBeanServerConnection</code> interface. ");
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "collection");
         var2.setValue("property", "SNMPGaugeMonitors");
      }

      var3 = SNMPAgentMBean.class.getMethod("addSNMPStringMonitor", SNMPStringMonitorMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("stringmonitor", "The feature to be added to the SNMPStringMonitor attribute ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 Instead, use standard JMX design patterns using <code>javax.management.MBeanServerConnection</code> interface. ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Adds a single <code>SNMPStringMonitorMBeans</code> to this SNMP agent's collection.</p> ");
         var6 = new String[]{BeanInfoHelper.encodeEntities("#getSNMPStringMonitors()")};
         var2.setValue("see", var6);
         var2.setValue("role", "collection");
         var2.setValue("property", "SNMPStringMonitors");
      }

      var3 = SNMPAgentMBean.class.getMethod("removeSNMPStringMonitor", SNMPStringMonitorMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("stringmonitor", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 Instead, use standard JMX design patterns using <code>javax.management.MBeanServerConnection</code> interface. ");
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "collection");
         var2.setValue("property", "SNMPStringMonitors");
      }

      var3 = SNMPAgentMBean.class.getMethod("addSNMPCounterMonitor", SNMPCounterMonitorMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("countermonitor", "The feature to be added to the SNMPCounterMonitor attribute ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 Instead, use standard JMX design patterns using <code>javax.management.MBeanServerConnection</code> interface. ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Adds a feature to the SNMPCounterMonitor attribute of the SNMPAgentMBean object</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "SNMPCounterMonitors");
      }

      var3 = SNMPAgentMBean.class.getMethod("removeSNMPCounterMonitor", SNMPCounterMonitorMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("countermonitor", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 Instead, use standard JMX design patterns using <code>javax.management.MBeanServerConnection</code> interface. ");
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "collection");
         var2.setValue("property", "SNMPCounterMonitors");
      }

      var3 = SNMPAgentMBean.class.getMethod("addSNMPLogFilter", SNMPLogFilterMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("logfilter", "The feature to be added to the SNMPLogFilter attribute ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 Instead, use standard JMX design patterns using <code>javax.management.MBeanServerConnection</code> interface. ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Adds a feature to the SNMPLogFilter attribute of the SNMPAgentMBean object</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "SNMPLogFilters");
      }

      var3 = SNMPAgentMBean.class.getMethod("removeSNMPLogFilter", SNMPLogFilterMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("logfilter", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 Instead, use standard JMX design patterns using <code>javax.management.MBeanServerConnection</code> interface. ");
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "collection");
         var2.setValue("property", "SNMPLogFilters");
      }

      var3 = SNMPAgentMBean.class.getMethod("addSNMPAttributeChange", SNMPAttributeChangeMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("attrchange", "The feature to be added to the SNMPAttributeChange attribute ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 Instead, use standard JMX design patterns using <code>javax.management.MBeanServerConnection</code> interface. ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Adds a feature to the SNMPAttributeChange attribute of the SNMPAgentMBean object</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "SNMPAttributeChanges");
      }

      var3 = SNMPAgentMBean.class.getMethod("removeSNMPAttributeChange", SNMPAttributeChangeMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("attrchange", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 Instead, use standard JMX design patterns using <code>javax.management.MBeanServerConnection</code> interface. ");
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "collection");
         var2.setValue("property", "SNMPAttributeChanges");
      }

   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      MethodDescriptor var2;
      Method var3;
      ParameterDescriptor[] var4;
      String var5;
      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SNMPAgentMBean.class.getMethod("lookupSNMPTrapDestination", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "SNMPTrapDestinations");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SNMPAgentMBean.class.getMethod("lookupSNMPProxy", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "SNMPProxies");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SNMPAgentMBean.class.getMethod("lookupSNMPGaugeMonitor", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "SNMPGaugeMonitors");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SNMPAgentMBean.class.getMethod("lookupSNMPStringMonitor", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "SNMPStringMonitors");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SNMPAgentMBean.class.getMethod("lookupSNMPCounterMonitor", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "SNMPCounterMonitors");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SNMPAgentMBean.class.getMethod("lookupSNMPLogFilter", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "SNMPLogFilters");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SNMPAgentMBean.class.getMethod("lookupSNMPAttributeChange", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", " ");
            var2.setValue("role", "finder");
            var2.setValue("property", "SNMPAttributeChanges");
            var2.setValue("since", "9.0.0.0");
         }
      }

   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = SNMPAgentMBean.class.getMethod("freezeCurrentValue", String.class);
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

      var3 = SNMPAgentMBean.class.getMethod("restoreDefaultValue", String.class);
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
