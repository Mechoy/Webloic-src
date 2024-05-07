package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WTCRemoteTuxDomMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WTCRemoteTuxDomMBean.class;

   public WTCRemoteTuxDomMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WTCRemoteTuxDomMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WTCRemoteTuxDomMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "7.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This interface provides access to the WTC remote Tuxedo Domain configuration attributes. The methods defined herein are applicable for WTC configuration at the WLS domain level.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WTCRemoteTuxDomMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("AccessPoint")) {
         var3 = "getAccessPoint";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAccessPoint";
         }

         var2 = new PropertyDescriptor("AccessPoint", WTCRemoteTuxDomMBean.class, var3, var4);
         var1.put("AccessPoint", var2);
         var2.setValue("description", "<p>The unique name used to identify this remote Tuxedo access point. This name should be unique for all local and remote Tuxedo access points defined within a WTC Service. This allows you to define unique configurations having the same Access Point ID.</p> ");
         setPropertyDescriptorDefault(var2, "myRAP");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("AccessPointId")) {
         var3 = "getAccessPointId";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAccessPointId";
         }

         var2 = new PropertyDescriptor("AccessPointId", WTCRemoteTuxDomMBean.class, var3, var4);
         var1.put("AccessPointId", var2);
         var2.setValue("description", "<p>The connection principal name used to identify this remote Tuxedo access point when attempting to establish a session connection to local Tuxedo access points.</p> ");
         setPropertyDescriptorDefault(var2, "myRAPId");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("AclPolicy")) {
         var3 = "getAclPolicy";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAclPolicy";
         }

         var2 = new PropertyDescriptor("AclPolicy", WTCRemoteTuxDomMBean.class, var3, var4);
         var1.put("AclPolicy", var2);
         var2.setValue("description", "<p>The inbound access control list (ACL) policy toward requests from a remote Tuxedo access point.</p>  <p>The allowed values are:</p>  <ul> <li>LOCAL: The local Tuxedo access point modifies the identity of service requests received from a given remote Tuxedo access point to the principal name specified in the local principal name for a given remote Tuxedo access point.</li>  <li>GLOBAL: The local Tuxedo access point passes the service request with no change in identity.</li> </ul>  <p><i>Note:</i> If Interoperate is set to Yes, AclPolicy is ignored.</p> ");
         setPropertyDescriptorDefault(var2, "LOCAL");
         var2.setValue("legalValues", new Object[]{"GLOBAL", "LOCAL"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("AllowAnonymous")) {
         var3 = "getAllowAnonymous";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAllowAnonymous";
         }

         var2 = new PropertyDescriptor("AllowAnonymous", WTCRemoteTuxDomMBean.class, var3, var4);
         var1.put("AllowAnonymous", var2);
         var2.setValue("description", "<p>Specifies whether the anonymous user is allowed to access remote Tuxedo services.</p>  <p><i>Note:</i> If the anonymous user is allowed to access Tuxedo, the default AppKey will be used for <code>TpUsrFile</code> and <code>LDAP</code> AppKey plug-ins. Interaction with the <code>Custom</code> AppKey plug-in depends on the design of the Custom AppKey generator.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("AppKey")) {
         var3 = "getAppKey";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAppKey";
         }

         var2 = new PropertyDescriptor("AppKey", WTCRemoteTuxDomMBean.class, var3, var4);
         var1.put("AppKey", var2);
         var2.setValue("description", "<p>Specifies the type of AppKey plug-in used.</p>  <p>The allowed values are:</p>  <ul> <li>TpUsrFile: <code>TpUsrFile</code> is the default plug-in. It uses an imported Tuxedo TPUSR file to provide user security information. Previous releases of WebLogic Tuxedo Connector support this option.</li>  <li>LDAP: The <code>LDAP</code> plug-in utilizes an embedded LDAP server to provide user security information. The user record must define the Tuxedo UID and GID information in the description field. This functionality is not supported in previous releases of WebLogic Tuxedo Connector. </li>  <li>Custom: The <code>Custom</code> plug-in provides the ability to write your own AppKey generator class to provide the security information required by Tuxedo. This functionality is not supported in previous releases of WebLogic Tuxedo Connector. </li> </ul> ");
         setPropertyDescriptorDefault(var2, "TpUsrFile");
         var2.setValue("legalValues", new Object[]{"TpUsrFile", "LDAP", "Custom"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CmpLimit")) {
         var3 = "getCmpLimit";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCmpLimit";
         }

         var2 = new PropertyDescriptor("CmpLimit", WTCRemoteTuxDomMBean.class, var3, var4);
         var1.put("CmpLimit", var2);
         var2.setValue("description", "<p>The compression threshold this remote Tuxedo access point uses when sending data to a local Tuxedo access point. Application buffers larger than this size are compressed.</p>  <p><b>Range of Values:</b> Between <code>0</code> and a positive 32-bit integer.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ConnPrincipalName")) {
         var3 = "getConnPrincipalName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnPrincipalName";
         }

         var2 = new PropertyDescriptor("ConnPrincipalName", WTCRemoteTuxDomMBean.class, var3, var4);
         var1.put("ConnPrincipalName", var2);
         var2.setValue("description", "<p>The principal name used to verify the identity of this remote Tuxedo access point when it establishes a session connection with a local Tuxedo access point. If not specified, the connection principal name defaults to the AccessPointID for this access point.</p>  <p><i>Note:</i> This parameter only applies to domains of type TDOMAIN that are running Oracle  Tuxedo 7.1 or later software.</p>  <p><b>Note:</b> ConnPrincipalName is not supported in this release.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ConnectionPolicy")) {
         var3 = "getConnectionPolicy";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnectionPolicy";
         }

         var2 = new PropertyDescriptor("ConnectionPolicy", WTCRemoteTuxDomMBean.class, var3, var4);
         var1.put("ConnectionPolicy", var2);
         var2.setValue("description", "<p>The conditions under which this remote Tuxedo access point establishes a session connection with a local Tuxedo access point.</p>  <p>The allowed values are:</p>  <ul> <li>ON_DEMAND: A connection is attempted only when requested by either a client request to a remote service or an administrative connect command.</li>  <li>ON_STARTUP: A domain gateway attempts to establish a connection with its remote Tuxedo access points at gateway server initialization time. Remote services (services advertised in JNDI by the domain gateway for this local Tuxedo access point) are advertised only if a connection is successfully established to that remote Tuxedo access point. If there is no active connection to a remote Tuxedo access point, then the remote services are suspended. By default, this connection policy retries failed connections every 60 seconds. Use the MaxRetry and RetryInterval attributes to specify application specific values.</li>  <li>INCOMING_ONLY: A domain gateway does not attempt an initial connection to remote Tuxedo access points at startup and remote services are initially suspended. The domain gateway is available for incoming connections from remote Tuxedo access points and remote services are advertised when the domain gateway for this local Tuxedo access point receives an incoming connection. Connection retry processing is not allowed.</li>  <li>LOCAL: Specifies that the remote domain connection policy is explicitly defaulted to the local domain ConnectionPolicy attribute value.</li> </ul> ");
         setPropertyDescriptorDefault(var2, "LOCAL");
         var2.setValue("legalValues", new Object[]{"ON_DEMAND", "ON_STARTUP", "INCOMING_ONLY", "LOCAL"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CredentialPolicy")) {
         var3 = "getCredentialPolicy";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCredentialPolicy";
         }

         var2 = new PropertyDescriptor("CredentialPolicy", WTCRemoteTuxDomMBean.class, var3, var4);
         var1.put("CredentialPolicy", var2);
         var2.setValue("description", "<p>The outbound access control list (ACL) policy toward requests to a remote Tuxedo access point.</p>  <p>The allowed values are:</p>  <ul> <li>LOCAL: The remote Tuxedo access point controls the identity of service requests received from the local Tuxedo access point to the principal name specified in the local principal name for this remote Tuxedo access point.</li>  <li>GLOBAL: The remote Tuxedo access point passes the service request with no change.</li> </ul>  <p><i>Note:</i>If Interoperate is set to Yes, CredentialPolicy is ignored.</p> ");
         setPropertyDescriptorDefault(var2, "LOCAL");
         var2.setValue("legalValues", new Object[]{"GLOBAL", "LOCAL"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CustomAppKeyClass")) {
         var3 = "getCustomAppKeyClass";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCustomAppKeyClass";
         }

         var2 = new PropertyDescriptor("CustomAppKeyClass", WTCRemoteTuxDomMBean.class, var3, var4);
         var1.put("CustomAppKeyClass", var2);
         var2.setValue("description", "<p>The full pathname to the custom <code>AppKey</code> generator class. (This class is only relevant if you specify <code>Custom</code> as the AppKey Generator.)</p>  <p><i>Note:</i> This class is loaded at runtime if the <code>Custom</code> AppKey generator plug-in is selected.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CustomAppKeyClassParam")) {
         var3 = "getCustomAppKeyClassParam";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCustomAppKeyClassParam";
         }

         var2 = new PropertyDescriptor("CustomAppKeyClassParam", WTCRemoteTuxDomMBean.class, var3, var4);
         var1.put("CustomAppKeyClassParam", var2);
         var2.setValue("description", "<p>The optional parameters to be used by the custom <code>AppKey</code> class at the class initialization time. This class is only relevant if you specify <code>Custom</code> as the AppKey Generator.)</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DefaultAppKey")) {
         var3 = "getDefaultAppKey";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultAppKey";
         }

         var2 = new PropertyDescriptor("DefaultAppKey", WTCRemoteTuxDomMBean.class, var3, var4);
         var1.put("DefaultAppKey", var2);
         var2.setValue("description", "<p>The default <code>AppKey</code> value to be used by the anonymous user and other users who are not defined in the user database if the plug-in allows them to access Tuxedo.</p>  <p><i>Note:</i> The <code>TpUsrFile</code> and <code>LDAP</code> plug-ins do not allow users that are not defined in user database to access Tuxedo unless Allow Anonymous is enabled.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("FederationName")) {
         var3 = "getFederationName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFederationName";
         }

         var2 = new PropertyDescriptor("FederationName", WTCRemoteTuxDomMBean.class, var3, var4);
         var1.put("FederationName", var2);
         var2.setValue("description", "<p>The context at which this remote Tuxedo access point federates to a foreign name service. If omitted, the default federation point is <code>tuxedo.domains</code>.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("FederationURL")) {
         var3 = "getFederationURL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFederationURL";
         }

         var2 = new PropertyDescriptor("FederationURL", WTCRemoteTuxDomMBean.class, var3, var4);
         var1.put("FederationURL", var2);
         var2.setValue("description", "<p>The URL for a foreign name service that is federated into JNDI.</p>  <p><i>Note:</i> The Weblogic Tuxedo Connector can federate to non-CORBA service providers.</p>  <p><i>Note:</i> If this value is not specified, the WebLogic Tuxedo Connector:</p>  <ul> <li>Assumes there is a CosNaming server in the foreign domain.</li>  <li>Federates to the CosNaming server using TGIOP. </li> </ul> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("KeepAlive")) {
         var3 = "getKeepAlive";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setKeepAlive";
         }

         var2 = new PropertyDescriptor("KeepAlive", WTCRemoteTuxDomMBean.class, var3, var4);
         var1.put("KeepAlive", var2);
         var2.setValue("description", "<p>Return value tells whether this local Tuxedo access point is configured with Application Level Keep Alive, and it maximum idle time value before wait timer start ticking.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("KeepAliveWait")) {
         var3 = "getKeepAliveWait";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setKeepAliveWait";
         }

         var2 = new PropertyDescriptor("KeepAliveWait", WTCRemoteTuxDomMBean.class, var3, var4);
         var1.put("KeepAliveWait", var2);
         var2.setValue("description", "<p>Return value that tells whether this local Tuxedo access point requires the acknowledgement of Application Level Keep Alive, and how long it will wait without receiving acknowledgement before declare the connection is inaccessible.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("LocalAccessPoint")) {
         var3 = "getLocalAccessPoint";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLocalAccessPoint";
         }

         var2 = new PropertyDescriptor("LocalAccessPoint", WTCRemoteTuxDomMBean.class, var3, var4);
         var1.put("LocalAccessPoint", var2);
         var2.setValue("description", "<p>The local domain name from which this remote Tuxedo domain is reached.</p> ");
         setPropertyDescriptorDefault(var2, "myLAP");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxEncryptBits")) {
         var3 = "getMaxEncryptBits";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxEncryptBits";
         }

         var2 = new PropertyDescriptor("MaxEncryptBits", WTCRemoteTuxDomMBean.class, var3, var4);
         var1.put("MaxEncryptBits", var2);
         var2.setValue("description", "<p>The maximum encryption key length (in bits) this remote Tuxedo access point uses when establishing a session connection. A value of <code>0</code> indicates no encryption is used.</p>  <p style=\"font-weight: bold\">Value Restrictions:</p>  <ul> <li>The value of the MaxEncryptBits attribute must be greater than or equal to the value of the MinEncrypBits attribute. </li>  <li>A MaxEncryptBits of 40 can be used only with domains running Tuxedo 7.1 or higher. </li> </ul> ");
         setPropertyDescriptorDefault(var2, "128");
         var2.setValue("legalValues", new Object[]{"0", "40", "56", "128"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxRetries")) {
         var3 = "getMaxRetries";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxRetries";
         }

         var2 = new PropertyDescriptor("MaxRetries", WTCRemoteTuxDomMBean.class, var3, var4);
         var1.put("MaxRetries", var2);
         var2.setValue("description", "<p>The maximum number of times that this remote Tuxedo access point tries to establish a session connections to local Tuxedo access points. Use only when the ConnectionPolicy value is set to <code>ON_STARTUP</code>.</p>  <p><i>Note:</i> A value of <code>-1</code> indicates that the Max Retries value from the local Tuxedo access point is used. Be sure to set the ConnectionPolicy value for the local Tuxedo access point to <code>ON_STARTUP</code> also.</p>  <p><b>Range of Values:</b> Between <code>-1</code> and a positive 64-bit integer.</p>  <ul> <li><p>Use <code>0</code> to disable the retry mechanism.</li>  <li>Use the maximum value to try until a connection is established.</li> </ul> ");
         setPropertyDescriptorDefault(var2, new Long(-1L));
         var2.setValue("legalMax", new Long(Long.MAX_VALUE));
         var2.setValue("legalMin", new Long(-1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MinEncryptBits")) {
         var3 = "getMinEncryptBits";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMinEncryptBits";
         }

         var2 = new PropertyDescriptor("MinEncryptBits", WTCRemoteTuxDomMBean.class, var3, var4);
         var1.put("MinEncryptBits", var2);
         var2.setValue("description", "<p>The minimum encryption key length (in bits) this remote Tuxedo access point uses when establishing a session connection. A value of <code>0</code> indicates no encryption is used.</p>  <p style=\"font-weight: bold\">Value Restrictions:</p>  <ul> <li>The MinEncrypBits value must be less than or equal to the MaxEncrypBits value. </li>  <li>A MinEncrypBits value of 40 can be used only with domains running Tuxedo 7.1 or higher. </li> </ul> ");
         setPropertyDescriptorDefault(var2, "0");
         var2.setValue("secureValue", "40");
         var2.setValue("legalValues", new Object[]{"0", "40", "56", "128"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("NWAddr")) {
         var3 = "getNWAddr";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNWAddr";
         }

         var2 = new PropertyDescriptor("NWAddr", WTCRemoteTuxDomMBean.class, var3, var4);
         var1.put("NWAddr", var2);
         var2.setValue("description", "<p>The network address and port number of this remote Tuxedo access point.</p> Specify the address in one of the following formats: <ul> <li>TCP/IP address in the format <code>//hostname:port_number</code> or <code>//#.#.#.#:port_number</code>.</li> <li>SDP address in the format <code>sdp://hostname:port_number</code> or <code>sdp://#.#.#.#:port_number</code>.</li> </ul>  <i>Notes:</i> <ul> <li>If the hostname is used, the access point finds an address for hostname using the local name resolution facilities (usually DNS). If dotted decimal format is used, each # should be a number from 0 to 255. This dotted decimal number represents the IP address of the local machine. The port_number is the TCP/SDP port number at which the access point listens for incoming requests.</li>  <li>If SDP format address is specified, the transport protocol for this access point is SDP instead of TCP. This feature is only available when WTC and Tuxedo domain gateway are both deployed on Oracle Exalogic platform. Requires Tuxedo 11gR1PS2 and higher.</li> </ul> ");
         setPropertyDescriptorDefault(var2, "//localhost:8902");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RetryInterval")) {
         var3 = "getRetryInterval";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRetryInterval";
         }

         var2 = new PropertyDescriptor("RetryInterval", WTCRemoteTuxDomMBean.class, var3, var4);
         var1.put("RetryInterval", var2);
         var2.setValue("description", "<p>The number of seconds that this remote Tuxedo access point waits between automatic connection attempts to local Tuxedo access points. Use this only when the ConnectionPolicy value is set to <code>ON_STARTUP</code>.</p>  <p><b>Range of Values:</b> Between <code>-1</code> and a positive 32-bit integer.</p>  <p><i>Note:</i> A value of <code>-1</code> means that the RetryInterval value from the local Tuxedo access point is used.  Be sure to set the ConnectionPolicy value for the local Tuxedo access point to <code>ON_STARTUP</code> also.</p> ");
         setPropertyDescriptorDefault(var2, new Long(-1L));
         var2.setValue("legalMax", new Long(2147483647L));
         var2.setValue("legalMin", new Long(-1L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TpUsrFile")) {
         var3 = "getTpUsrFile";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTpUsrFile";
         }

         var2 = new PropertyDescriptor("TpUsrFile", WTCRemoteTuxDomMBean.class, var3, var4);
         var1.put("TpUsrFile", var2);
         var2.setValue("description", "<p>The full path to the user password file containing UID/GID information. (This field is only relevant if you specify <code>TpUsrFile</code> as the AppKey Generator.)</p>  <p><i>Note:</i> This file is generated by the Tuxedo <code>tpusradd</code> utility on the remote Tuxedo domain specified by the remote Tuxedo access point. A copy of this file must be available in your WebLogic Tuxedo Connector environment to provide correct authorization, authentication, and auditing.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TuxedoGidKw")) {
         var3 = "getTuxedoGidKw";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTuxedoGidKw";
         }

         var2 = new PropertyDescriptor("TuxedoGidKw", WTCRemoteTuxDomMBean.class, var3, var4);
         var1.put("TuxedoGidKw", var2);
         var2.setValue("description", "<p>The keyword for Tuxedo GID (Group ID) used in the <code>WlsUser</code> when using the Tuxedo migration utility <code>tpmigldap</code>. This field is only relevant if you specify <code>LDAP</code> as the AppKey Generator.)</p>  <p><i>Note:</i> The keyword is used to find Tuxedo GID in the user record in the embedded LDAP database.</p> ");
         setPropertyDescriptorDefault(var2, "TUXEDO_GID");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TuxedoUidKw")) {
         var3 = "getTuxedoUidKw";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTuxedoUidKw";
         }

         var2 = new PropertyDescriptor("TuxedoUidKw", WTCRemoteTuxDomMBean.class, var3, var4);
         var1.put("TuxedoUidKw", var2);
         var2.setValue("description", "<p>The keyword for Tuxedo UID (User ID) used in the <code>WlsUser</code> when using the Tuxedo migration utility <code>tpmigldap</code>. This keyword is only relevant if you specify <code>LDAP</code> as the AppKey Generator.)</p>  <p><i>Note:</i> The keyword is used to find Tuxedo UID in the user record in the embedded LDAP database.</p> ");
         setPropertyDescriptorDefault(var2, "TUXEDO_UID");
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
