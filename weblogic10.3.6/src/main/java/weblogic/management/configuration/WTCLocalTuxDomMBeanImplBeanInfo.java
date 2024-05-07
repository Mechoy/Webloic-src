package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WTCLocalTuxDomMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WTCLocalTuxDomMBean.class;

   public WTCLocalTuxDomMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WTCLocalTuxDomMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WTCLocalTuxDomMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "7.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This interface provides access to the WTC local Tuxedo Domain configuration attributes.  The methods defined herein are applicable for WTC configuration at the WLS domain level.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WTCLocalTuxDomMBean");
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

         var2 = new PropertyDescriptor("AccessPoint", WTCLocalTuxDomMBean.class, var3, var4);
         var1.put("AccessPoint", var2);
         var2.setValue("description", "<p>The unique name used to identify this local Tuxedo access point. This name should be unique for all local and remote Tuxedo access points defined within a WTC Service. This allows you to define unique configurations having the same Access Point ID.</p> ");
         setPropertyDescriptorDefault(var2, "myLAP");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("AccessPointId")) {
         var3 = "getAccessPointId";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAccessPointId";
         }

         var2 = new PropertyDescriptor("AccessPointId", WTCLocalTuxDomMBean.class, var3, var4);
         var1.put("AccessPointId", var2);
         var2.setValue("description", "<p>The connection principal name used to identify this local Tuxedo access point when attempting to establish a session connection with remote Tuxedo access points.</p>  <p><i>Note:</i> The AccessPointId must match the corresponding DOMAINID in the *DM_REMOTE_DOMAINS section of your Tuxedo DMCONFIG file.</p> ");
         setPropertyDescriptorDefault(var2, "myLAPId");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("BlockTime")) {
         var3 = "getBlockTime";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBlockTime";
         }

         var2 = new PropertyDescriptor("BlockTime", WTCLocalTuxDomMBean.class, var3, var4);
         var1.put("BlockTime", var2);
         var2.setValue("description", "<p>The maximum number of seconds this local Tuxedo access point allows for a blocking call.</p>  <p><b>Range of Values:</b> Between <code>0</code> and a positive 32 bit integer.</p> ");
         setPropertyDescriptorDefault(var2, new Long(60L));
         var2.setValue("legalMax", new Long(2147483647L));
         var2.setValue("legalMin", new Long(0L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CmpLimit")) {
         var3 = "getCmpLimit";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCmpLimit";
         }

         var2 = new PropertyDescriptor("CmpLimit", WTCLocalTuxDomMBean.class, var3, var4);
         var1.put("CmpLimit", var2);
         var2.setValue("description", "<p>The compression threshold this local Tuxedo access point uses when sending data to a remote Tuxedo access point. Application buffers larger than this size are compressed.</p>  <p><b>Range of Values:</b> Between <code>0</code> and a positive 32-bit integer.</p> ");
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

         var2 = new PropertyDescriptor("ConnPrincipalName", WTCLocalTuxDomMBean.class, var3, var4);
         var1.put("ConnPrincipalName", var2);
         var2.setValue("description", "<p>The principal name used to verify the identity of this domain when establishing a connection to another domain.</p>  <p><i>Note:</i> This parameter only applies to domains of type TDOMAIN that are running Oracle  Tuxedo 7.1 or later software. If not specified, the connection principal name defaults to the AccessPointID for this <code>domain.ConnPrincipalName</code>.</p>  <p><i>Note:</i> ConnPrincipalName is not supported in this release.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ConnectionPolicy")) {
         var3 = "getConnectionPolicy";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnectionPolicy";
         }

         var2 = new PropertyDescriptor("ConnectionPolicy", WTCLocalTuxDomMBean.class, var3, var4);
         var1.put("ConnectionPolicy", var2);
         var2.setValue("description", "<p>The conditions under which this local Tuxedo access point establishes a session connection with a remote Tuxedo access point.</p>  <p>The connection policies are:</p>  <ul> <li>ON_DEMAND: A connection is attempted only when requested by either a client request to a remote service or an administrative connect command.</li> <li>ON_STARTUP: A domain gateway attempts to establish a connection with its remote Tuxedo access points at gateway server initialization time. Remote services (services advertised in JNDI by the domain gateway for this local access point) are advertised only if a connection is successfully established to that remote Tuxedo access point. If there is no active connection to a remote Tuxedo access point, then the remote services are suspended. By default, this connection policy retries failed connections every 60 seconds. Use the MaxRetry and RetryInterval values to specify application specific values.</li> <li>INCOMING_ONLY: A domain gateway does not attempt an initial connection to remote Tuxedo access points at startup and remote services are initially suspended. The domain gateway is available for incoming connections from remote Tuxedo access points and remote services are advertised when the domain gateway for this local Tuxedo access point receives an incoming connection. Connection retry processing is not allowed.</li> </ul> ");
         setPropertyDescriptorDefault(var2, "ON_DEMAND");
         var2.setValue("legalValues", new Object[]{"ON_DEMAND", "ON_STARTUP", "INCOMING_ONLY"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("IdentityKeyStoreFileName")) {
         var3 = "getIdentityKeyStoreFileName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setIdentityKeyStoreFileName";
         }

         var2 = new PropertyDescriptor("IdentityKeyStoreFileName", WTCLocalTuxDomMBean.class, var3, var4);
         var1.put("IdentityKeyStoreFileName", var2);
         var2.setValue("description", "<p>The path and file name of the identity keystore.</p>  <p>The path name must either be absolute or relative to where the server was booted. The identity key store file name is only used if KeystoreLocation is \"Custom Stores\".</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      String[] var5;
      if (!var1.containsKey("IdentityKeyStorePassPhrase")) {
         var3 = "getIdentityKeyStorePassPhrase";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setIdentityKeyStorePassPhrase";
         }

         var2 = new PropertyDescriptor("IdentityKeyStorePassPhrase", WTCLocalTuxDomMBean.class, var3, var4);
         var1.put("IdentityKeyStorePassPhrase", var2);
         var2.setValue("description", "<p>The custom identity keystore's passphrase. If empty or null, then the keystore will be opened without a passphrase.</p>  <p>This attribute is only used if KeyStores is \"Custom Stores\".</p>  <p>When you get the value of this attribute, WebLogic Server does the following:</p> <ol><li>Retrieves the value of the <code>IdentityKeyStorePassPhraseEncrypted</code> attribute. <li>Decrypts the value and returns the unencrypted password as a String. </ol>  <p>When you set the value of this attribute, WebLogic Server does the following:</p> <ol><li>Encrypts the value.</li> <li>Sets the value of the <code>CustomIdentityKeyStorePassPhraseEncrypted</code> attribute to the encrypted value.</li> </ol> <p>Using this attribute (<code>CustomIdentityKeyStorePassPhrase</code>) is a potential security risk because the String object (which contains the unencrypted password) remains in the JVM's memory until garbage collection removes it and the memory is reallocated. Depending on how memory is allocated in the JVM, a significant amount of time could pass before this unencrypted data is removed from memory.</p>  <p>Instead of using this attribute, use <code>CustomIdentityKeyStorePassPhraseEncrypted</code>.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getCustomIdentityKeyStorePassPhraseEncrypted()")};
         var2.setValue("see", var5);
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("IdentityKeyStorePassPhraseEncrypted")) {
         var3 = "getIdentityKeyStorePassPhraseEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setIdentityKeyStorePassPhraseEncrypted";
         }

         var2 = new PropertyDescriptor("IdentityKeyStorePassPhraseEncrypted", WTCLocalTuxDomMBean.class, var3, var4);
         var1.put("IdentityKeyStorePassPhraseEncrypted", var2);
         var2.setValue("description", "<p>The identity keystore's encrypted passphrase. If empty or null, then the keystore will be opened without a passphrase.</p>  <p>This attribute is only used if KeyStores is \"Custom Stores\".</p>  <p>To set this attribute, use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the encrypt() method. </p>  <p>To compare a password that a user enters with the encrypted value of this attribute, use the same WebLogic Server instance that you used to set and encrypt this attribute and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Interoperate")) {
         var3 = "getInteroperate";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setInteroperate";
         }

         var2 = new PropertyDescriptor("Interoperate", WTCLocalTuxDomMBean.class, var3, var4);
         var1.put("Interoperate", var2);
         var2.setValue("description", "<p>Specifies whether this local Tuxedo access point interoperates with remote Tuxedo access points that are based upon Tuxedo release 6.5. If this value is set to <code>Yes</code>, the local Tuxedo access point interoperates with a Tuxedo 6.5 domain.</p> ");
         setPropertyDescriptorDefault(var2, "No");
         var2.setValue("legalValues", new Object[]{"Yes", "No"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("KeepAlive")) {
         var3 = "getKeepAlive";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setKeepAlive";
         }

         var2 = new PropertyDescriptor("KeepAlive", WTCLocalTuxDomMBean.class, var3, var4);
         var1.put("KeepAlive", var2);
         var2.setValue("description", "<p>Return value tells whether this local Tuxedo access point is configured with Application Level Keep Alive, and it maximum idle time value before wait timer start ticking.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("KeepAliveWait")) {
         var3 = "getKeepAliveWait";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setKeepAliveWait";
         }

         var2 = new PropertyDescriptor("KeepAliveWait", WTCLocalTuxDomMBean.class, var3, var4);
         var1.put("KeepAliveWait", var2);
         var2.setValue("description", "<p>Return value that tells whether this local Tuxedo access point requires the acknowledgement of Application Level Keep Alive, and how long it will wait without receiving acknowledgement before declare the connection is inaccessible.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("KeyStoresLocation")) {
         var3 = "getKeyStoresLocation";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setKeyStoresLocation";
         }

         var2 = new PropertyDescriptor("KeyStoresLocation", WTCLocalTuxDomMBean.class, var3, var4);
         var1.put("KeyStoresLocation", var2);
         var2.setValue("description", "<p>Provides the configuration rule to be used for finding Local Access Point's identity key store and trust key store. In plain text, it contains information on where the identity key store and trust key store are configured.  When KeyStoreLocation is configured with <code>WLS Store</code>, WTC uses configuration information from the WLS Key Stores configuration.  Otherwise, it uses the key stores information configured in the Local Access Point.</p> ");
         setPropertyDescriptorDefault(var2, "Custom Stores");
         var2.setValue("legalValues", new Object[]{"WLS Stores", "Custom Stores"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxEncryptBits")) {
         var3 = "getMaxEncryptBits";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxEncryptBits";
         }

         var2 = new PropertyDescriptor("MaxEncryptBits", WTCLocalTuxDomMBean.class, var3, var4);
         var1.put("MaxEncryptBits", var2);
         var2.setValue("description", "<p>The maximum encryption key length (in bits) this local Tuxedo access point uses when establishing a session connection. A value of <code>0</code> indicates no encryption is used.</p>  <p style=\"font-weight: bold\">Value Restrictions:</p>  <ul> <li>The MaxEncryptBits value must be greater than or equal to the MinEncrypBits value.</li> <li>A MaxEncryptBits of <code>40</code> can be used only with domains running Tuxedo 7.1 or higher.</li> </ul> ");
         setPropertyDescriptorDefault(var2, "128");
         var2.setValue("secureValue", "128");
         var2.setValue("legalValues", new Object[]{"0", "40", "56", "128", "256"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxRetries")) {
         var3 = "getMaxRetries";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxRetries";
         }

         var2 = new PropertyDescriptor("MaxRetries", WTCLocalTuxDomMBean.class, var3, var4);
         var1.put("MaxRetries", var2);
         var2.setValue("description", "<p>The maximum number of times that this local Tuxedo access point tries to establish a session connection to remote Tuxedo access points. Use this value only when Connection Policy is set to <code>ON_STARTUP</code>.</p>  <p><b>Range of Values:</b> Between <code>0</code> and a positive 64 bit integer.</p>  <p><i>Note:</i> Use the minimum value to disable the retry mechanism. Use the maximum value to try until a connection is established.</p> ");
         setPropertyDescriptorDefault(var2, new Long(Long.MAX_VALUE));
         var2.setValue("legalMax", new Long(Long.MAX_VALUE));
         var2.setValue("legalMin", new Long(0L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MinEncryptBits")) {
         var3 = "getMinEncryptBits";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMinEncryptBits";
         }

         var2 = new PropertyDescriptor("MinEncryptBits", WTCLocalTuxDomMBean.class, var3, var4);
         var1.put("MinEncryptBits", var2);
         var2.setValue("description", "<p>The minimum encryption key length (in bits) this local Tuxedo access point uses when establishing a session connection. A value of <code>0</code> indicates no encryption is used.</p>  <p style=\"font-weight: bold\">Value Restrictions:</p>  <ul> <li>The MinEncrypBits value must be less than or equal to the MaxEncrypBits value.</li> <li>A MinEncrypBits value of <code>40</code> can be used only with domains running Tuxedo 7.1 or higher.</li> </ul> ");
         setPropertyDescriptorDefault(var2, "0");
         var2.setValue("secureValue", "40");
         var2.setValue("legalValues", new Object[]{"0", "40", "56", "128", "256"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("NWAddr")) {
         var3 = "getNWAddr";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNWAddr";
         }

         var2 = new PropertyDescriptor("NWAddr", WTCLocalTuxDomMBean.class, var3, var4);
         var1.put("NWAddr", var2);
         var2.setValue("description", "<p>The network address and port number of this local Tuxedo access point.</p> Specify the address in one of the following formats: <ul> <li>TCP/IP address in the format <code>//hostname:port_number</code> or <code>//#.#.#.#:port_number</code>.</li> <li>SDP address in the format <code>sdp://hostname:port_number</code> or <code>sdp://#.#.#.#:port_number</code>.</li> </ul>  <i>Notes:</i> <ul> <li>If the hostname is used, the access point finds an address for hostname using the local name resolution facilities (usually DNS). If dotted decimal format is used, each # should be a number from 0 to 255. This dotted decimal number represents the IP address of the local machine. The port_number is the TCP/SDP port number at which the access point listens for incoming requests.</li>  <li>If SDP format address is specified, the transport protocol for this access point is SDP instead of TCP. This feature is only available when WTC and Tuxedo domain gateway are both deployed on Oracle Exalogic platform. Requires Tuxedo 11gR1PS2 and higher.</li> </ul> ");
         setPropertyDescriptorDefault(var2, "//localhost:8901");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("PrivateKeyAlias")) {
         var3 = "getPrivateKeyAlias";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPrivateKeyAlias";
         }

         var2 = new PropertyDescriptor("PrivateKeyAlias", WTCLocalTuxDomMBean.class, var3, var4);
         var1.put("PrivateKeyAlias", var2);
         var2.setValue("description", "<p>The string alias used to store and retrieve the Local Tuxedo access point's private key in the keystore. This private key is associated with the Local Tuxedo access point's digital certificate.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("PrivateKeyPassPhrase")) {
         var3 = "getPrivateKeyPassPhrase";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPrivateKeyPassPhrase";
         }

         var2 = new PropertyDescriptor("PrivateKeyPassPhrase", WTCLocalTuxDomMBean.class, var3, var4);
         var1.put("PrivateKeyPassPhrase", var2);
         var2.setValue("description", "<p>The passphrase used to retrieve the server's private key from the keystore. This passphrase is assigned to the private key when it is generated.</p>  <p>When you get the value of this attribute, WebLogic Server does the following:</p> <ol><li>Retrieves the value of the <code>PrivateKeyPassPhraseEncrypted</code> attribute. <li>Decrypts the value and returns the unencrypted passphrase as a String. </ol>  <p>When you set the value of this attribute, WebLogic Server does the following:</p> <ol><li>Encrypts the value.</li> <li>Sets the value of the <code>PrivateKeyPassPhraseEncrypted</code> attribute to the encrypted value.</li> </ol>  <p>Using this attribute (<code>PrivateKeyPassPhrase</code>) is a potential security risk because the String object (which contains the unencrypted passphrase) remains in the JVM's memory until garbage collection removes it and the memory is reallocated. Depending on how memory is allocated in the JVM, a significant amount of time could pass before this unencrypted data is removed from memory.</p>  <p>Instead of using this attribute, use <code>getPrivateKeyPassPhraseEncrypted</code>.</p> ");
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("PrivateKeyPassPhraseEncrypted")) {
         var3 = "getPrivateKeyPassPhraseEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPrivateKeyPassPhraseEncrypted";
         }

         var2 = new PropertyDescriptor("PrivateKeyPassPhraseEncrypted", WTCLocalTuxDomMBean.class, var3, var4);
         var1.put("PrivateKeyPassPhraseEncrypted", var2);
         var2.setValue("description", "<p>The encrypted passphrase used to retrieve the Local Tuxedo access point's private key from the keystore. This passphrase is assigned to the private key when it is generated.</p>  <p>To set this attribute, use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the encrypt() method.</p>  <p>To compare a password that a user enters with the encrypted value of this attribute, go to the same WebLogic Server instance that you used to set and encrypt this attribute and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RetryInterval")) {
         var3 = "getRetryInterval";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRetryInterval";
         }

         var2 = new PropertyDescriptor("RetryInterval", WTCLocalTuxDomMBean.class, var3, var4);
         var1.put("RetryInterval", var2);
         var2.setValue("description", "<p>The number of seconds that this local Tuxedo access point waits between automatic connection attempts to remote Tuxedo access points. Use this value only when Connection Policy is set to <code>ON_STARTUP</code>.</p>  <p><b>Range of Values:</b> Between 0 and a positive 32-bit integer.</p> ");
         setPropertyDescriptorDefault(var2, new Long(60L));
         var2.setValue("legalMax", new Long(2147483647L));
         var2.setValue("legalMin", new Long(0L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Security")) {
         var3 = "getSecurity";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSecurity";
         }

         var2 = new PropertyDescriptor("Security", WTCLocalTuxDomMBean.class, var3, var4);
         var1.put("Security", var2);
         var2.setValue("description", "<p>The type of application security enforced.</p>  <p>The types of security are:</p>  <ul> <li>NONE: No security is used.</li> <li>APP_PW: Password security is enforced when a connection is established from a remote domain. The application password is defined in the WTCResourcesMBean.</li> <li>DM_PW: Domain password security is enforced when a connection is established from a remote domain. The domain password is defined in the WTCPasswordsMBean.</li> </ul> ");
         setPropertyDescriptorDefault(var2, "NONE");
         var2.setValue("secureValue", "DM_PW");
         var2.setValue("legalValues", new Object[]{"NONE", "APP_PW", "DM_PW"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TrustKeyStoreFileName")) {
         var3 = "getTrustKeyStoreFileName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTrustKeyStoreFileName";
         }

         var2 = new PropertyDescriptor("TrustKeyStoreFileName", WTCLocalTuxDomMBean.class, var3, var4);
         var1.put("TrustKeyStoreFileName", var2);
         var2.setValue("description", "<p>The path and file name of the trust keystore.</p>  <p>The path name must either be absolute or relative to where the server was booted. This file name is only used if KeyStores is \"Custom Stores\".</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TrustKeyStorePassPhrase")) {
         var3 = "getTrustKeyStorePassPhrase";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTrustKeyStorePassPhrase";
         }

         var2 = new PropertyDescriptor("TrustKeyStorePassPhrase", WTCLocalTuxDomMBean.class, var3, var4);
         var1.put("TrustKeyStorePassPhrase", var2);
         var2.setValue("description", "<p>The trust keystore's passphrase. If empty or null, then the keystore will be opened without a passphrase.</p>  <p>This attribute is only used if KeyStores is \"Custom Stores\".</p>  <p>When you get the value of this attribute, WebLogic Server does the following:</p> <ol><li>Retrieves the value of the <code>TrustKeyStorePassPhraseEncrypted</code> attribute. <li>Decrypts the value and returns the unencrypted password as a String. </ol>  <p>When you set the value of this attribute, WebLogic Server does the following:</p> <ol><li>Encrypts the value.</li> <li>Sets the value of the <code>TrustKeyStorePassPhraseEncrypted</code> attribute to the encrypted value.</li> </ol> <p>Using this attribute (<code>TrustKeyStorePassPhrase</code>) is a potential security risk because the String object (which contains the unencrypted password) remains in the JVM's memory until garbage collection removes it and the memory is reallocated. Depending on how memory is allocated in the JVM, a significant amount of time could pass before this unencrypted data is removed from memory.</p>  <p>Instead of using this attribute, use <code>TrustKeyStorePassPhraseEncrypted</code>.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getTrustKeyStorePassPhraseEncrypted()")};
         var2.setValue("see", var5);
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("TrustKeyStorePassPhraseEncrypted")) {
         var3 = "getTrustKeyStorePassPhraseEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTrustKeyStorePassPhraseEncrypted";
         }

         var2 = new PropertyDescriptor("TrustKeyStorePassPhraseEncrypted", WTCLocalTuxDomMBean.class, var3, var4);
         var1.put("TrustKeyStorePassPhraseEncrypted", var2);
         var2.setValue("description", "<p>The trust keystore's encrypted passphrase. If empty or null, then the keystore will be opened without a passphrase.</p>  <p>This attribute is only used if KeyStores is \"Custom Stores\".</p>  <p>To set this attribute, use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the encrypt() method. </p>  <p>To compare a password that a user enters with the encrypted value of this attribute, use the same WebLogic Server instance that you used to set and encrypt this attribute and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("UseSSL")) {
         var3 = "getUseSSL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUseSSL";
         }

         var2 = new PropertyDescriptor("UseSSL", WTCLocalTuxDomMBean.class, var3, var4);
         var1.put("UseSSL", var2);
         var2.setValue("description", "<p>Specifies if the connection initiated or accepted by this Local Tuxedo access point uses SSL on top of its transport layer. Values are:</p>  <ul><li>Off: SSL not used.</li> <li>TwoWay: Mutual Authentication with SSL required.</li> <li>OneWay: Server Authentication with SSL required.</li> </ul>  <p><i>Note:</i> If SDP transport is configured for this access point, the configured value of this attribute is ignored and <code>off</code> is used. ");
         setPropertyDescriptorDefault(var2, "Off");
         var2.setValue("legalValues", new Object[]{"Off", "TwoWay", "OneWay"});
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
