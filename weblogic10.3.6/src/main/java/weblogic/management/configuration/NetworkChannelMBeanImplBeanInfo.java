package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class NetworkChannelMBeanImplBeanInfo extends DeploymentMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = NetworkChannelMBean.class;

   public NetworkChannelMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public NetworkChannelMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = NetworkChannelMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("deprecated", "7.0.0.0 use {@link NetworkAccessPointMBean} ");
      String[] var3 = new String[]{BeanInfoHelper.encodeEntities("ServerMBean#getListenPort"), BeanInfoHelper.encodeEntities("ServerMBean#getAdministrationPort"), BeanInfoHelper.encodeEntities("SSLMBean#getListenPort"), BeanInfoHelper.encodeEntities("ServerMBean#getSSL"), BeanInfoHelper.encodeEntities("NetworkAccessPointMBean"), BeanInfoHelper.encodeEntities("ServerMBean#getNetworkAccessPoints")};
      var2.setValue("see", var3);
      var2.setValue("package", "weblogic.management.configuration");
      String var4 = (new String("This MBean defines a network channel.   A network channel is used to configure additional ports for a server beyond its default listen ports.   Network channels do not support IIOP. <p> A network channel can be targeted at multiple clusters and servers. Targeting a channel at a cluster targets it at every server that is a member of that cluster. A server can support multiple channels.</p> A server can fine-tune its network channel settings by using a NetworkAccessPointMBean.  The NetworkAccessPointMBean also servers to set the listen address and external DNS name that a server uses for a particular channel. <p> A server serves up to three default listen ports: ServerMBean ListenPort, ServerMBean AdministrationPort, and SSLMBean ListenPort. The default listen ports form implicit channel(s) of weight 50.</p> <p> A network channel also defines the creation of server-to-server connections. If a server is initiating a new connection to another server, the highest weighted common (same named) channel that supports the desired protocol is used to determine which port to contact.</p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var4);
      var2.setValue("description", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.NetworkChannelMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      String[] var5;
      if (!var1.containsKey("AcceptBacklog")) {
         var3 = "getAcceptBacklog";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAcceptBacklog";
         }

         var2 = new PropertyDescriptor("AcceptBacklog", NetworkChannelMBean.class, var3, var4);
         var1.put("AcceptBacklog", var2);
         var2.setValue("description", "<p>Allowed backlog of connection requests on the listen port(s). Individual servers may override this value using a NetworkAccessPointMBean. Setting the backlog to 0 may prevent accepting any incoming connection on some of the OS.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.NetworkAccessPointMBean#getAcceptBacklog"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#getAcceptBacklog")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(50));
         var2.setValue("legalMin", new Integer(0));
      }

      if (!var1.containsKey("ChannelWeight")) {
         var3 = "getChannelWeight";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setChannelWeight";
         }

         var2 = new PropertyDescriptor("ChannelWeight", NetworkChannelMBean.class, var3, var4);
         var1.put("ChannelWeight", var2);
         var2.setValue("description", "<p>A weight to give this channel when creating server-to-server connections.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.NetworkChannelMBean")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(50));
         var2.setValue("legalMax", new Integer(100));
         var2.setValue("legalMin", new Integer(1));
      }

      if (!var1.containsKey("ClusterAddress")) {
         var3 = "getClusterAddress";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClusterAddress";
         }

         var2 = new PropertyDescriptor("ClusterAddress", NetworkChannelMBean.class, var3, var4);
         var1.put("ClusterAddress", var2);
         var2.setValue("description", "<p>This channel's cluster address. If this is not set, the cluster address from the cluster configuration is used in its place.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.ClusterMBean#getClusterAddress")};
         var2.setValue("see", var5);
         var2.setValue("defaultValueNull", Boolean.TRUE);
      }

      if (!var1.containsKey("CompleteCOMMessageTimeout")) {
         var3 = "getCompleteCOMMessageTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCompleteCOMMessageTimeout";
         }

         var2 = new PropertyDescriptor("CompleteCOMMessageTimeout", NetworkChannelMBean.class, var3, var4);
         var1.put("CompleteCOMMessageTimeout", var2);
         var2.setValue("description", "<p>The maximum number of seconds spent waiting for a complete COM message to be received. This attribute helps guard against denial of service attacks in which a caller indicates that they will be sending a message of a certain size which they never finish sending. Individual servers may override this value using a NetworkAccessPointMBean.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.KernelMBean#getCompleteCOMMessageTimeout"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.NetworkAccessPointMBean#getCompleteCOMMessageTimeout")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(60));
         var2.setValue("secureValue", new Integer(60));
         var2.setValue("legalMax", new Integer(480));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CompleteHTTPMessageTimeout")) {
         var3 = "getCompleteHTTPMessageTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCompleteHTTPMessageTimeout";
         }

         var2 = new PropertyDescriptor("CompleteHTTPMessageTimeout", NetworkChannelMBean.class, var3, var4);
         var1.put("CompleteHTTPMessageTimeout", var2);
         var2.setValue("description", "<p>The maximum number of seconds spent waiting for a complete HTTP message to be received. This attribute helps guard against denial of service attacks in which a caller indicates that they will be sending a message of a certain size which they never finish sending. Individual servers may override this value using a NetworkAccessPointMBean.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.KernelMBean#getCompleteHTTPMessageTimeout"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.NetworkAccessPointMBean#getCompleteHTTPMessageTimeout")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(60));
         var2.setValue("secureValue", new Integer(60));
         var2.setValue("legalMax", new Integer(480));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CompleteT3MessageTimeout")) {
         var3 = "getCompleteT3MessageTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCompleteT3MessageTimeout";
         }

         var2 = new PropertyDescriptor("CompleteT3MessageTimeout", NetworkChannelMBean.class, var3, var4);
         var1.put("CompleteT3MessageTimeout", var2);
         var2.setValue("description", "<p>The maximum number of seconds spent waiting for a complete T3 message to be received. This attribute helps guard against denial of service attacks in which a caller indicates that they will be sending a message of a certain size which they never finish sending. Individual servers may override this value using a NetworkAccessPointMBean.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.KernelMBean#getCompleteT3MessageTimeout"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.NetworkAccessPointMBean#getCompleteT3MessageTimeout")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(60));
         var2.setValue("secureValue", new Integer(60));
         var2.setValue("legalMax", new Integer(480));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DefaultIIOPPasswordEncrypted")) {
         var3 = "getDefaultIIOPPasswordEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultIIOPPasswordEncrypted";
         }

         var2 = new PropertyDescriptor("DefaultIIOPPasswordEncrypted", NetworkChannelMBean.class, var3, var4);
         var1.put("DefaultIIOPPasswordEncrypted", var2);
         var2.setValue("description", "<p>The encrypted password for the default IIOP user.</p>  <p>To set this attribute, use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the encrypt() method.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.EncryptionHelper")};
         var2.setValue("see", var5);
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("Description")) {
         var3 = "getDescription";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDescription";
         }

         var2 = new PropertyDescriptor("Description", NetworkChannelMBean.class, var3, var4);
         var1.put("Description", var2);
         var2.setValue("description", "<p>Optional short description of this channel for console display purposes. For long descriptions, use the \"Notes\" field.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
      }

      if (!var1.containsKey("ListenPort")) {
         var3 = "getListenPort";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setListenPort";
         }

         var2 = new PropertyDescriptor("ListenPort", NetworkChannelMBean.class, var3, var4);
         var1.put("ListenPort", var2);
         var2.setValue("description", "<p>The plaintext (non-SSL) listen port for the channel. Individual servers may override this value, but may not enable the port if disabled here and may not disable the port if enabled here. Individual servers may override this value using a NetworkAccessPointMBean.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#isListenPortEnabled"), BeanInfoHelper.encodeEntities("#getSSLListenPort"), BeanInfoHelper.encodeEntities("NetworkAccessPointMBean#getListenPort"), BeanInfoHelper.encodeEntities("NetworkAccessPointMBean#getListenAddress"), BeanInfoHelper.encodeEntities("NetworkAccessPointMBean#getExternalDNSName"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#getListenPort")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(8001));
         var2.setValue("legalMax", new Integer(65535));
         var2.setValue("legalMin", new Integer(1));
      }

      if (!var1.containsKey("LoginTimeoutMillis")) {
         var3 = "getLoginTimeoutMillis";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLoginTimeoutMillis";
         }

         var2 = new PropertyDescriptor("LoginTimeoutMillis", NetworkChannelMBean.class, var3, var4);
         var1.put("LoginTimeoutMillis", var2);
         var2.setValue("description", "<p>The login timeout for the server, in milliseconds. This value must be equal to or greater than 0. Individual servers may override this value using a NetworkAccessPointMBean.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.NetworkAccessPointMBean#getLoginTimeoutMillis"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#getLoginTimeoutMillis")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(5000));
         var2.setValue("secureValue", new Integer(5000));
         var2.setValue("legalMax", new Integer(100000));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("LoginTimeoutMillisSSL")) {
         var3 = "getLoginTimeoutMillisSSL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLoginTimeoutMillisSSL";
         }

         var2 = new PropertyDescriptor("LoginTimeoutMillisSSL", NetworkChannelMBean.class, var3, var4);
         var1.put("LoginTimeoutMillisSSL", var2);
         var2.setValue("description", "<p>Duration allowed for an SSL login sequence. If the duration is exceeded, the login is timed out. 0 to disable. Individual servers may override this value using a NetworkAccessPointMBean.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.NetworkAccessPointMBean#getLoginTimeoutMillisSSL"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.SSLMBean#getLoginTimeoutMillis")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(25000));
         var2.setValue("secureValue", new Integer(25000));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxCOMMessageSize")) {
         var3 = "getMaxCOMMessageSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxCOMMessageSize";
         }

         var2 = new PropertyDescriptor("MaxCOMMessageSize", NetworkChannelMBean.class, var3, var4);
         var1.put("MaxCOMMessageSize", var2);
         var2.setValue("description", "<p>The maximum COM message size allowable in a message header. This attribute attempts to prevent a denial of service attack whereby a caller attempts to force the server to allocate more memory than is available thereby keeping the server from responding quickly to other requests. Individual servers may override this value using a NetworkAccessPointMBean.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.NetworkAccessPointMBean#getMaxCOMMessageSize"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.KernelMBean#getMaxCOMMessageSize")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(10000000));
         var2.setValue("secureValue", new Integer(10000000));
         var2.setValue("legalMax", new Integer(2000000000));
         var2.setValue("legalMin", new Integer(4096));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxHTTPMessageSize")) {
         var3 = "getMaxHTTPMessageSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxHTTPMessageSize";
         }

         var2 = new PropertyDescriptor("MaxHTTPMessageSize", NetworkChannelMBean.class, var3, var4);
         var1.put("MaxHTTPMessageSize", var2);
         var2.setValue("description", "<p>The maximum HTTP message size allowable in a message header. This attribute attempts to prevent a denial of service attack whereby a caller attempts to force the server to allocate more memory than is available thereby keeping the server from responding quickly to other requests. Individual servers may override this value using a NetworkAccessPointMBean.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.NetworkAccessPointMBean#getMaxHTTPMessageSize"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.KernelMBean#getMaxHTTPMessageSize")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(10000000));
         var2.setValue("secureValue", new Integer(10000000));
         var2.setValue("legalMax", new Integer(2000000000));
         var2.setValue("legalMin", new Integer(4096));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxT3MessageSize")) {
         var3 = "getMaxT3MessageSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxT3MessageSize";
         }

         var2 = new PropertyDescriptor("MaxT3MessageSize", NetworkChannelMBean.class, var3, var4);
         var1.put("MaxT3MessageSize", var2);
         var2.setValue("description", "<p>The maximum T3 message size allowable in a message header. This attribute attempts to prevent a denial of service attack whereby a caller attempts to force the server to allocate more memory than is available thereby keeping the server from responding quickly to other requests. Individual servers may override this value using a NetworkAccessPointMBean.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.NetworkAccessPointMBean#getMaxT3MessageSize"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.KernelMBean#getMaxT3MessageSize")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(10000000));
         var2.setValue("secureValue", new Integer(10000000));
         var2.setValue("legalMax", new Integer(2000000000));
         var2.setValue("legalMin", new Integer(4096));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", NetworkChannelMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The name of the channel. The name must not start with \".WL\".</p> ");
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("SSLListenPort")) {
         var3 = "getSSLListenPort";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSSLListenPort";
         }

         var2 = new PropertyDescriptor("SSLListenPort", NetworkChannelMBean.class, var3, var4);
         var1.put("SSLListenPort", var2);
         var2.setValue("description", "<p>The SSL listen port for the channel. Individual server's may override this value, but may not enable the port if disabled here and may not disable the port if enabled here. SSL must be configured and enabled for this port to work. Individual servers may override this value using a NetworkAccessPointMBean.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getSSLListenPort"), BeanInfoHelper.encodeEntities("#isSSLListenPortEnabled"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.SSLMBean#getListenPort"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.KernelMBean#getSSL"), BeanInfoHelper.encodeEntities("NetworkAccessPointMBean#getSSLListenPort"), BeanInfoHelper.encodeEntities("NetworkAccessPointMBean#getListenAddress")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(8002));
         var2.setValue("legalMax", new Integer(65535));
         var2.setValue("legalMin", new Integer(1));
      }

      if (!var1.containsKey("TunnelingClientPingSecs")) {
         var3 = "getTunnelingClientPingSecs";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTunnelingClientPingSecs";
         }

         var2 = new PropertyDescriptor("TunnelingClientPingSecs", NetworkChannelMBean.class, var3, var4);
         var1.put("TunnelingClientPingSecs", var2);
         var2.setValue("description", "<p>Interval (in seconds) at which to ping an http-tunneled client to see if its still alive. Individual servers may override this value using a NetworkAccessPointMBean.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#getTunnelingClientPingSecs"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.NetworkAccessPointMBean#getTunnelingClientPingSecs")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(45));
         var2.setValue("legalMin", new Integer(1));
      }

      if (!var1.containsKey("TunnelingClientTimeoutSecs")) {
         var3 = "getTunnelingClientTimeoutSecs";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTunnelingClientTimeoutSecs";
         }

         var2 = new PropertyDescriptor("TunnelingClientTimeoutSecs", NetworkChannelMBean.class, var3, var4);
         var1.put("TunnelingClientTimeoutSecs", var2);
         var2.setValue("description", "<p>Duration (in seconds) after which a missing http-tunneled client is considered dead. Individual servers may override this value using a NetworkAccessPointMBean.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#getTunnelingClientTimeoutSecs"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.NetworkAccessPointMBean#getTunnelingClientTimeoutSecs")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(40));
         var2.setValue("secureValue", new Integer(40));
         var2.setValue("legalMin", new Integer(1));
      }

      if (!var1.containsKey("BoundOutgoingEnabled")) {
         var3 = "isBoundOutgoingEnabled";
         var4 = null;
         var2 = new PropertyDescriptor("BoundOutgoingEnabled", NetworkChannelMBean.class, var3, var4);
         var1.put("BoundOutgoingEnabled", var2);
         var2.setValue("description", "<p>Bind new outgoing server side T3 or T3S connections to the server channel's listen address. Other protocols ignore this field. This field is ignored for connections initiated via URLs, it takes effect if and only if the connection was initiated by accessing a remote reference (such as an EJB or RMI stub.)</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
      }

      if (!var1.containsKey("COMEnabled")) {
         var3 = "isCOMEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCOMEnabled";
         }

         var2 = new PropertyDescriptor("COMEnabled", NetworkChannelMBean.class, var3, var4);
         var1.put("COMEnabled", var2);
         var2.setValue("description", "<p>Indicates whether plaintext (non-SSL) COM traffic is enabled.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#isCOMEnabled")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("HTTPEnabled")) {
         var3 = "isHTTPEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setHTTPEnabled";
         }

         var2 = new PropertyDescriptor("HTTPEnabled", NetworkChannelMBean.class, var3, var4);
         var1.put("HTTPEnabled", var2);
         var2.setValue("description", "<p>Whether or not plaintext (non-SSL) HTTP traffic is enabled.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#isHttpdEnabled"), BeanInfoHelper.encodeEntities("#isHTTPSEnabled"), BeanInfoHelper.encodeEntities("#isTunnelingEnabled")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("HTTPSEnabled")) {
         var3 = "isHTTPSEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setHTTPSEnabled";
         }

         var2 = new PropertyDescriptor("HTTPSEnabled", NetworkChannelMBean.class, var3, var4);
         var1.put("HTTPSEnabled", var2);
         var2.setValue("description", "<p>Whether or not secure (SSL) HTTP traffic is enabled.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#isHttpdEnabled"), BeanInfoHelper.encodeEntities("#isHTTPEnabled"), BeanInfoHelper.encodeEntities("#isTunnelingEnabled")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(true));
      }

      if (!var1.containsKey("ListenPortEnabled")) {
         var3 = "isListenPortEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setListenPortEnabled";
         }

         var2 = new PropertyDescriptor("ListenPortEnabled", NetworkChannelMBean.class, var3, var4);
         var1.put("ListenPortEnabled", var2);
         var2.setValue("description", "<p>Whether or not plaintext port is enabled for the channel.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getListenPort"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#isListenPortEnabled")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("OutgoingEnabled")) {
         var3 = "isOutgoingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOutgoingEnabled";
         }

         var2 = new PropertyDescriptor("OutgoingEnabled", NetworkChannelMBean.class, var3, var4);
         var1.put("OutgoingEnabled", var2);
         var2.setValue("description", "<p>Whether or not new server-to-server connections may consider this channel when initiating.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.NetworkChannelMBean")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(true));
      }

      if (!var1.containsKey("SSLListenPortEnabled")) {
         var3 = "isSSLListenPortEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSSLListenPortEnabled";
         }

         var2 = new PropertyDescriptor("SSLListenPortEnabled", NetworkChannelMBean.class, var3, var4);
         var1.put("SSLListenPortEnabled", var2);
         var2.setValue("description", "<p>Whether or not SSL port is enabled for the channel. SSL must be configured and enabled in addition to this setting for the SSL port to work.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.SSLMBean#isListenPortEnabled"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.KernelMBean#getSSL")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(true));
      }

      if (!var1.containsKey("T3Enabled")) {
         var3 = "isT3Enabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setT3Enabled";
         }

         var2 = new PropertyDescriptor("T3Enabled", NetworkChannelMBean.class, var3, var4);
         var1.put("T3Enabled", var2);
         var2.setValue("description", "<p>Whether or not plaintext (non-SSL) T3 traffic is enabled. Note that it is not possible to disable T3 traffic on the default channel(s).</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.NetworkChannelMBean"), BeanInfoHelper.encodeEntities("#isT3SEnabled")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("T3SEnabled")) {
         var3 = "isT3SEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setT3SEnabled";
         }

         var2 = new PropertyDescriptor("T3SEnabled", NetworkChannelMBean.class, var3, var4);
         var1.put("T3SEnabled", var2);
         var2.setValue("description", "<p>Whether or not secure T3 traffic is enabled. Note that it is not possible to disable T3 traffic on the default channel(s).</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.NetworkChannelMBean"), BeanInfoHelper.encodeEntities("#isT3Enabled")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
      }

      if (!var1.containsKey("TunnelingEnabled")) {
         var3 = "isTunnelingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTunnelingEnabled";
         }

         var2 = new PropertyDescriptor("TunnelingEnabled", NetworkChannelMBean.class, var3, var4);
         var1.put("TunnelingEnabled", var2);
         var2.setValue("description", "<p>Enables tunneling via http.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#isTunnelingEnabled"), BeanInfoHelper.encodeEntities("#isHTTPEnabled"), BeanInfoHelper.encodeEntities("#isHTTPSEnabled")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
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
