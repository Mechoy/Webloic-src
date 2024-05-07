package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class NetworkAccessPointMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = NetworkAccessPointMBean.class;

   public NetworkAccessPointMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public NetworkAccessPointMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = NetworkAccessPointMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "7.0.0.0");
      String[] var3 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#getNetworkAccessPoints")};
      var2.setValue("see", var3);
      var2.setValue("package", "weblogic.management.configuration");
      String var4 = (new String("A server can specifiy additional network connections by using a NetworkAccessPointMBean.  The NetworkAccessPointMBean is also used to set the listen address and external DNS name that a server uses for a particular channel.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var4);
      var2.setValue("description", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.NetworkAccessPointMBean");
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

         var2 = new PropertyDescriptor("AcceptBacklog", NetworkAccessPointMBean.class, var3, var4);
         var1.put("AcceptBacklog", var2);
         var2.setValue("description", "<p>The number of backlogged, new TCP connection requests that this network channel allows. A value of <tt>-1</tt> indicates that the network channel obtains its backlog configuration from the server's configuration.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("ServerMBean#getAcceptBacklog")};
         var2.setValue("see", var5);
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ChannelWeight")) {
         var3 = "getChannelWeight";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setChannelWeight";
         }

         var2 = new PropertyDescriptor("ChannelWeight", NetworkAccessPointMBean.class, var3, var4);
         var1.put("ChannelWeight", var2);
         var2.setValue("description", "<p>A weight to give this channel when creating server-to-server connections.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(50));
         var2.setValue("legalMax", new Integer(100));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ClusterAddress")) {
         var3 = "getClusterAddress";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClusterAddress";
         }

         var2 = new PropertyDescriptor("ClusterAddress", NetworkAccessPointMBean.class, var3, var4);
         var1.put("ClusterAddress", var2);
         var2.setValue("description", "<p>The address this network channel uses to generate EJB handles and failover addresses for use in a cluster. This value is determined according to the following order of precedence: <ol> <li>If the cluster address is specified via the NAPMBean, then that value is used</li> <li>If this value is not specified, the value of PublicAddress is used. <li>If PublicAddress is not set, this value is derive from the ClusterAddress attribute of the ClusterMbean.</li> <li>If ClusterMbean.clusterAddress is not set, this value is derive from the listen address of the NAPMbean.</li> </p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getPublicAddress"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ClusterMBean#getClusterAddress")};
         var2.setValue("see", var5);
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CompleteMessageTimeout")) {
         var3 = "getCompleteMessageTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCompleteMessageTimeout";
         }

         var2 = new PropertyDescriptor("CompleteMessageTimeout", NetworkAccessPointMBean.class, var3, var4);
         var1.put("CompleteMessageTimeout", var2);
         var2.setValue("description", "<p>The maximum amount of time this network channel waits for a complete message to be received. A value of <code>0</code> disables network channel complete message timeout. A value of <tt>-1</tt> indicates that the network channel obtains this timeout value from the ServerMBean.</p>  <p>This timeout helps guard against denial of service attacks in which a caller indicates that they will be sending a message of a certain size which they never finish sending.</p> ");
         var2.setValue("legalMax", new Integer(480));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ConnectTimeout")) {
         var3 = "getConnectTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnectTimeout";
         }

         var2 = new PropertyDescriptor("ConnectTimeout", NetworkAccessPointMBean.class, var3, var4);
         var1.put("ConnectTimeout", var2);
         var2.setValue("description", "<p>The amount of time that this network channel should wait to establish an outbound socket connection before timing out. A value of <code>0</code> disables network channel connect timeout.</p> ");
         var2.setValue("legalMax", new Integer(240));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CustomPrivateKeyAlias")) {
         var3 = "getCustomPrivateKeyAlias";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCustomPrivateKeyAlias";
         }

         var2 = new PropertyDescriptor("CustomPrivateKeyAlias", NetworkAccessPointMBean.class, var3, var4);
         var1.put("CustomPrivateKeyAlias", var2);
         var2.setValue("description", "<p>The string alias used to store and retrieve the channel's private key in the keystore. This private key is associated with the server's digital certificate. A value of <tt>null</tt> indicates that the network channel uses the alias specified in the server's SSL configuration.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.SSLMBean#getServerPrivateKeyAlias")};
         var2.setValue("see", var5);
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CustomPrivateKeyPassPhrase")) {
         var3 = "getCustomPrivateKeyPassPhrase";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCustomPrivateKeyPassPhrase";
         }

         var2 = new PropertyDescriptor("CustomPrivateKeyPassPhrase", NetworkAccessPointMBean.class, var3, var4);
         var1.put("CustomPrivateKeyPassPhrase", var2);
         var2.setValue("description", "<p>The passphrase used to retrieve the server's private key from the keystore. This passphrase is assigned to the private key when it is generated. A value of <tt>null</tt> indicates that the network channel uses the pass phrase specified in the server's SSL configuration.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.SSLMBean#getServerPrivateKeyPassPhrase")};
         var2.setValue("see", var5);
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CustomPrivateKeyPassPhraseEncrypted")) {
         var3 = "getCustomPrivateKeyPassPhraseEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCustomPrivateKeyPassPhraseEncrypted";
         }

         var2 = new PropertyDescriptor("CustomPrivateKeyPassPhraseEncrypted", NetworkAccessPointMBean.class, var3, var4);
         var1.put("CustomPrivateKeyPassPhraseEncrypted", var2);
         var2.setValue("description", "<p>The encrypted form of passphrase used to retrieve the server's private key from the keystore. </p> ");
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("CustomProperties")) {
         var3 = "getCustomProperties";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCustomProperties";
         }

         var2 = new PropertyDescriptor("CustomProperties", NetworkAccessPointMBean.class, var3, var4);
         var1.put("CustomProperties", var2);
         var2.setValue("description", "Get custom protocol properties specified for this channel. The contents of the map are only know the custom protocol implementators like SIP. ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("IdleConnectionTimeout")) {
         var3 = "getIdleConnectionTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setIdleConnectionTimeout";
         }

         var2 = new PropertyDescriptor("IdleConnectionTimeout", NetworkAccessPointMBean.class, var3, var4);
         var1.put("IdleConnectionTimeout", var2);
         var2.setValue("description", "<p>The maximum amount of time (in seconds) that a connection is allowed to be idle before it is closed by this network channel. A value of <tt>-1</tt> indicates that the network channel obtains this timeout value from the ServerMBean.</p>  <p>This timeout helps guard against server deadlock through too many open connections.</p> ");
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ListenAddress")) {
         var3 = "getListenAddress";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setListenAddress";
         }

         var2 = new PropertyDescriptor("ListenAddress", NetworkAccessPointMBean.class, var3, var4);
         var1.put("ListenAddress", var2);
         var2.setValue("description", "<p>The IP address or DNS name this network channel uses to listen for incoming connections. A value of <tt>null</tt> indicates that the network channel should obtain this value from the server's configuration.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#getListenAddress")};
         var2.setValue("see", var5);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ListenPort")) {
         var3 = "getListenPort";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setListenPort";
         }

         var2 = new PropertyDescriptor("ListenPort", NetworkAccessPointMBean.class, var3, var4);
         var1.put("ListenPort", var2);
         var2.setValue("description", "<p>The default TCP port this network channel uses to listen for regular (non-SSL) incoming connections. A value of <tt>-1</tt> indicates that the network channel should obtain this value from the server's configuration.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#getListenPort")};
         var2.setValue("see", var5);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("LoginTimeoutMillis")) {
         var3 = "getLoginTimeoutMillis";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLoginTimeoutMillis";
         }

         var2 = new PropertyDescriptor("LoginTimeoutMillis", NetworkAccessPointMBean.class, var3, var4);
         var1.put("LoginTimeoutMillis", var2);
         var2.setValue("description", "<p>The amount of time that this network channel should wait for a connection before timing out. A value of <code>0</code> disables network channel login timeout. A value of <tt>-1</tt> indicates that the network channel obtains this timeout value from the server's configuration.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("ServerMBean#getLoginTimeoutMillis")};
         var2.setValue("see", var5);
         var2.setValue("legalMax", new Integer(100000));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxBackoffBetweenFailures")) {
         var3 = "getMaxBackoffBetweenFailures";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxBackoffBetweenFailures";
         }

         var2 = new PropertyDescriptor("MaxBackoffBetweenFailures", NetworkAccessPointMBean.class, var3, var4);
         var1.put("MaxBackoffBetweenFailures", var2);
         var2.setValue("description", "<p>The maximum back off time between failures while accepting client connections. -1 implies that this value is inherited from the server.</p> ");
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxConnectedClients")) {
         var3 = "getMaxConnectedClients";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxConnectedClients";
         }

         var2 = new PropertyDescriptor("MaxConnectedClients", NetworkAccessPointMBean.class, var3, var4);
         var1.put("MaxConnectedClients", var2);
         var2.setValue("description", "<p>The maximum number of clients that can be connected on this network channel.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(Integer.MAX_VALUE));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxMessageSize")) {
         var3 = "getMaxMessageSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxMessageSize";
         }

         var2 = new PropertyDescriptor("MaxMessageSize", NetworkAccessPointMBean.class, var3, var4);
         var1.put("MaxMessageSize", var2);
         var2.setValue("description", "<p>The maximum message size allowable in a message header.</p>  <p>This maximum attempts to prevent a denial of service attack whereby a caller attempts to force the server to allocate more memory than is available thereby keeping the server from responding quickly to other requests.</p> ");
         var2.setValue("legalMax", new Integer(100000000));
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

         var2 = new PropertyDescriptor("Name", NetworkAccessPointMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The name of this network channel.</p> ");
         setPropertyDescriptorDefault(var2, "<unknown>");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("OutboundPrivateKeyAlias")) {
         var3 = "getOutboundPrivateKeyAlias";
         var4 = null;
         var2 = new PropertyDescriptor("OutboundPrivateKeyAlias", NetworkAccessPointMBean.class, var3, var4);
         var1.put("OutboundPrivateKeyAlias", var2);
         var2.setValue("description", "<p>The string alias used to store and retrieve the outbound private key in the keystore. This private key is associated with either a server or a client digital certificate. This attribute value is derived from other settings and cannot be physically set.</p>  <p> The returned value is determined as follows: <ul> <li>If <code>{@link #isOutboundPrivateKeyEnabled}</code> and <code>{@link #isChannelIdentityCustomized}</code> return true, the value from <code>{@link #getCustomPrivateKeyAlias}</code> is returned. <li> Otherwise, the value from <code>{@link SSLMBean#getOutboundPrivateKeyAlias}</code> is returned from the <code>{@link ServerMBean}</code> for the channel. </ul> </p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#isOutboundPrivateKeyEnabled"), BeanInfoHelper.encodeEntities("#isChannelIdentityCustomized"), BeanInfoHelper.encodeEntities("#getCustomPrivateKeyAlias"), BeanInfoHelper.encodeEntities("SSLMBean#getOutboundPrivateKeyAlias")};
         var2.setValue("see", var5);
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("OutboundPrivateKeyPassPhrase")) {
         var3 = "getOutboundPrivateKeyPassPhrase";
         var4 = null;
         var2 = new PropertyDescriptor("OutboundPrivateKeyPassPhrase", NetworkAccessPointMBean.class, var3, var4);
         var1.put("OutboundPrivateKeyPassPhrase", var2);
         var2.setValue("description", "<p>The passphrase used to retrieve the outbound private key from the keystore. This passphrase is assigned to the private key when it is generated. This attribute value is derived from other settings and cannot be physically set.</p>  <p> The returned value is determined as follows: <ul> <li>If <code>{@link #isOutboundPrivateKeyEnabled}</code> and <code>{@link #isChannelIdentityCustomized}</code> return true, the value from <code>{@link #getCustomPrivateKeyPassPhrase}</code> is returned. <li> Otherwise, the value from <code>{@link SSLMBean#getOutboundPrivateKeyPassPhrase}</code> is returned from the <code>{@link ServerMBean}</code> for the channel. </ul> </p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#isOutboundPrivateKeyEnabled"), BeanInfoHelper.encodeEntities("#isChannelIdentityCustomized"), BeanInfoHelper.encodeEntities("#getCustomPrivateKeyPassPhrase"), BeanInfoHelper.encodeEntities("SSLMBean#getOutboundPrivateKeyPassPhrase")};
         var2.setValue("see", var5);
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("PrivateKeyAlias")) {
         var3 = "getPrivateKeyAlias";
         var4 = null;
         var2 = new PropertyDescriptor("PrivateKeyAlias", NetworkAccessPointMBean.class, var3, var4);
         var1.put("PrivateKeyAlias", var2);
         var2.setValue("description", "<p>The string alias used to store and retrieve the channel's private key in the keystore. This private key is associated with the server's digital certificate. This value is derived from other settings on the channel and cannot be physically set.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.SSLMBean#getServerPrivateKeyAlias"), BeanInfoHelper.encodeEntities("#getCustomPrivateKeyAlias")};
         var2.setValue("see", var5);
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("PrivateKeyPassPhrase")) {
         var3 = "getPrivateKeyPassPhrase";
         var4 = null;
         var2 = new PropertyDescriptor("PrivateKeyPassPhrase", NetworkAccessPointMBean.class, var3, var4);
         var1.put("PrivateKeyPassPhrase", var2);
         var2.setValue("description", "<p>The passphrase used to retrieve the server's private key from the keystore. This passphrase is assigned to the private key when it is generated. This value is derived from other settings on the channel and cannot be physically set.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.SSLMBean#getServerPrivateKeyPassPhrase"), BeanInfoHelper.encodeEntities("#getCustomPrivateKeyPassPhrase")};
         var2.setValue("see", var5);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Protocol")) {
         var3 = "getProtocol";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setProtocol";
         }

         var2 = new PropertyDescriptor("Protocol", NetworkAccessPointMBean.class, var3, var4);
         var1.put("Protocol", var2);
         var2.setValue("description", "<p>The protocol this network channel should use for connections.</p> ");
         setPropertyDescriptorDefault(var2, "t3");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ProxyAddress")) {
         var3 = "getProxyAddress";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setProxyAddress";
         }

         var2 = new PropertyDescriptor("ProxyAddress", NetworkAccessPointMBean.class, var3, var4);
         var1.put("ProxyAddress", var2);
         var2.setValue("description", "<p>The IP address or DNS name of the HTTP proxy to use for outbound connections on this channel. The HTTP proxy must support the CONNECT tunneling command.</p>  <p>This option is only effective when OutboundEnabled is set on the channel.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ProxyPort")) {
         var3 = "getProxyPort";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setProxyPort";
         }

         var2 = new PropertyDescriptor("ProxyPort", NetworkAccessPointMBean.class, var3, var4);
         var1.put("ProxyPort", var2);
         var2.setValue("description", "<p>The port of the HTTP proxy to use for outbound connections on this channel. The HTTP proxy must support the CONNECT tunneling command.</p>  <p>This option is only effective when OutboundEnabled and ProxyHost are set on the channel.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(80));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("PublicAddress")) {
         var3 = "getPublicAddress";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPublicAddress";
         }

         var2 = new PropertyDescriptor("PublicAddress", NetworkAccessPointMBean.class, var3, var4);
         var1.put("PublicAddress", var2);
         var2.setValue("description", "<p>The IP address or DNS name representing the external identity of this network channel. A value of <tt>null</tt> indicates that the network channel's Listen Address is also its external address. If the Listen Address is <tt>null,</tt>the network channel obtains its external identity from the server's configuration.</p>  <p>This is required for the configurations which need to cross a firewall doing Network Address Translation.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getExternalDNSName"), BeanInfoHelper.encodeEntities("#getListenAddress"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#getListenAddress")};
         var2.setValue("see", var5);
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("PublicPort")) {
         var3 = "getPublicPort";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPublicPort";
         }

         var2 = new PropertyDescriptor("PublicPort", NetworkAccessPointMBean.class, var3, var4);
         var1.put("PublicPort", var2);
         var2.setValue("description", "<p>The externally published listen port for this network channel. A value of <tt>-1</tt> indicates that the network channel's Listen Port is also its public listen port. If the Listen Port is <tt>-1,</tt>the network channel obtains its public listen port from the server's configuration.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getListenPort"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#getListenPort")};
         var2.setValue("see", var5);
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TimeoutConnectionWithPendingResponses")) {
         var3 = "getTimeoutConnectionWithPendingResponses";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTimeoutConnectionWithPendingResponses";
         }

         var2 = new PropertyDescriptor("TimeoutConnectionWithPendingResponses", NetworkAccessPointMBean.class, var3, var4);
         var1.put("TimeoutConnectionWithPendingResponses", var2);
         var2.setValue("description", "<p>Determines if connections with pending responses are allowed to timeout. It defaults to false. If set to true, the connection will be timed out for this channel if it exceeds the idleConnectionTimeout value.</p>  <p>Note: This setting only applies to IIOP connections. ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TunnelingClientPingSecs")) {
         var3 = "getTunnelingClientPingSecs";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTunnelingClientPingSecs";
         }

         var2 = new PropertyDescriptor("TunnelingClientPingSecs", NetworkAccessPointMBean.class, var3, var4);
         var1.put("TunnelingClientPingSecs", var2);
         var2.setValue("description", "<p>The interval (in seconds) at which this network channel should ping an HTTP-tunneled client to see if its still alive. A value of <tt>-1</tt> indicates that the network channel obtains this interval from the ServerMBean. (Requires you to enable tunneling for the network channel.)</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#getTunnelingClientPingSecs"), BeanInfoHelper.encodeEntities("#isTunnelingEnabled")};
         var2.setValue("see", var5);
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TunnelingClientTimeoutSecs")) {
         var3 = "getTunnelingClientTimeoutSecs";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTunnelingClientTimeoutSecs";
         }

         var2 = new PropertyDescriptor("TunnelingClientTimeoutSecs", NetworkAccessPointMBean.class, var3, var4);
         var1.put("TunnelingClientTimeoutSecs", var2);
         var2.setValue("description", "<p>The amount of time (in seconds) after which this network channel considers a missing HTTP-tunneled client to be dead. A value of <tt>-1</tt> indicates that the network channel obtains this timeout value from the ServerMBean. (Requires you to enable tunneling for the network channel.)</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#getTunnelingClientTimeoutSecs"), BeanInfoHelper.encodeEntities("#isTunnelingEnabled")};
         var2.setValue("see", var5);
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("UseFastSerialization")) {
         var3 = "getUseFastSerialization";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUseFastSerialization";
         }

         var2 = new PropertyDescriptor("UseFastSerialization", NetworkAccessPointMBean.class, var3, var4);
         var1.put("UseFastSerialization", var2);
         var2.setValue("description", "<p>Specifies whether to use non-standard object serialization for performance. This option works in different ways for different protocols. In particular under IIOP this option uses Java serialization rather than RMI-IIOP serialization. In general using non-standard serialization is not suitable for interop scenarios and may imply some feature loss. ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ChannelIdentityCustomized")) {
         var3 = "isChannelIdentityCustomized";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setChannelIdentityCustomized";
         }

         var2 = new PropertyDescriptor("ChannelIdentityCustomized", NetworkAccessPointMBean.class, var3, var4);
         var1.put("ChannelIdentityCustomized", var2);
         var2.setValue("description", "<p>Whether or not the channel's custom identity should be used. This setting only has an effect if the server is using a customized keystore. By default the channel's identity is inherited from the server's identity. ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.SSLMBean#getServerPrivateKeyAlias"), BeanInfoHelper.encodeEntities("#getCustomPrivateKeyAlias")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ClientCertificateEnforced")) {
         var3 = "isClientCertificateEnforced";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClientCertificateEnforced";
         }

         var2 = new PropertyDescriptor("ClientCertificateEnforced", NetworkAccessPointMBean.class, var3, var4);
         var1.put("ClientCertificateEnforced", var2);
         var2.setValue("description", "<p>Specifies whether clients must present digital certificates from a trusted certificate authority to WebLogic Server on this channel.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(true));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Enabled")) {
         var3 = "isEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEnabled";
         }

         var2 = new PropertyDescriptor("Enabled", NetworkAccessPointMBean.class, var3, var4);
         var1.put("Enabled", var2);
         var2.setValue("description", "<p>Specifies whether this channel should be started.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("HttpEnabledForThisProtocol")) {
         var3 = "isHttpEnabledForThisProtocol";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setHttpEnabledForThisProtocol";
         }

         var2 = new PropertyDescriptor("HttpEnabledForThisProtocol", NetworkAccessPointMBean.class, var3, var4);
         var1.put("HttpEnabledForThisProtocol", var2);
         var2.setValue("description", "<p>Specifies whether HTTP traffic should be allowed over this network channel.</p>  <p>HTTP is generally required by binary protocols for downloading stubs and other resources.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("OutboundEnabled")) {
         var3 = "isOutboundEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOutboundEnabled";
         }

         var2 = new PropertyDescriptor("OutboundEnabled", NetworkAccessPointMBean.class, var3, var4);
         var1.put("OutboundEnabled", var2);
         var2.setValue("description", "<p>Specifies whether new server-to-server connections may consider this network channel when initiating a connection. This is only relevant if the connection needs to be bound to the network channel's listen address. This will only work for binary protocols that support both outbound and inbound traffic.</p>   <p>When this feature is not enabled, connections are initiated using a local address selected by the underlying hardware.</p>  <p>The default is true for all admin channels and false otherwise.</p>  <p>Outbound channels are selected at runtime either by virtue of the fact of being the only outbound-enabled channel for the required protocol, or by name in <code>weblogic.jndi.Environment#setProviderChannel</code>.</p>  <p>The HTTP protocol is implicitly enabled for all the outbound channels, but you need to use the WLS client library (HTTP client) and set the channel on the connection. See <code><a href=\"../../../apirefs.1111/e13941/weblogic/net/http/HttpURLConnection.html#setSocketFactory\"> HttpURLConnection.setSocketFactory()</a></code>.</p> ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("OutboundPrivateKeyEnabled")) {
         var3 = "isOutboundPrivateKeyEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOutboundPrivateKeyEnabled";
         }

         var2 = new PropertyDescriptor("OutboundPrivateKeyEnabled", NetworkAccessPointMBean.class, var3, var4);
         var1.put("OutboundPrivateKeyEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the identity specifed by {@link #getCustomPrivateKeyAlias} should be used for outbound SSL connections on this channel. In normal circumstances the outbound identity is determined by the caller's environment. ");
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SDPEnabled")) {
         var3 = "isSDPEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSDPEnabled";
         }

         var2 = new PropertyDescriptor("SDPEnabled", NetworkAccessPointMBean.class, var3, var4);
         var1.put("SDPEnabled", var2);
         var2.setValue("description", "Enables Socket Direct Protocol (SDP) on this channel. Enable this attribute when configuring session replication enhancements for Managed Servers in a WebLogic cluster for Oracle Exalogic. For more information, see \"Enabling Exalogic-Specific Enhancements in Oracle WebLogic Server 11g Release 1 (10.3.4)\" in the Oracle Exalogic Deployment Guide. ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("TunnelingEnabled")) {
         var3 = "isTunnelingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTunnelingEnabled";
         }

         var2 = new PropertyDescriptor("TunnelingEnabled", NetworkAccessPointMBean.class, var3, var4);
         var1.put("TunnelingEnabled", var2);
         var2.setValue("description", "<p>Specifies whether tunneling via HTTP should be enabled for this network channel. This value is not inherited from the server's configuration.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TwoWaySSLEnabled")) {
         var3 = "isTwoWaySSLEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTwoWaySSLEnabled";
         }

         var2 = new PropertyDescriptor("TwoWaySSLEnabled", NetworkAccessPointMBean.class, var3, var4);
         var1.put("TwoWaySSLEnabled", var2);
         var2.setValue("description", "<p>Specifies whether this network channel uses two way SSL.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(true));
         var2.setValue("configurable", Boolean.TRUE);
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
