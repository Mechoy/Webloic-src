package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class SSLMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SSLMBean.class;

   public SSLMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SSLMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SSLMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This MBean represents the configuration of the SSL protocol.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.SSLMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("CertAuthenticator")) {
         var3 = "getCertAuthenticator";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCertAuthenticator";
         }

         var2 = new PropertyDescriptor("CertAuthenticator", SSLMBean.class, var3, var4);
         var1.put("CertAuthenticator", var2);
         var2.setValue("description", "<p>The name of the Java class that implements the <code>weblogic.security.acl.CertAuthenticator</code> class, which is deprecated in this release of WebLogic Server. This field is for Compatibility security only, and is only used when the Realm Adapter Authentication provider is configured.</p>  <p>The <code>weblogic.security.acl.CertAuthenticator</code> class maps the digital certificate of a client to a WebLogic Server user. The class has an <code>authenticate()</code> method that WebLogic Server calls after validating the digital certificate presented by the client.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("secureValue", "weblogic.security.acl.CertAuthenticator");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("CertificateCacheSize")) {
         var3 = "getCertificateCacheSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCertificateCacheSize";
         }

         var2 = new PropertyDescriptor("CertificateCacheSize", SSLMBean.class, var3, var4);
         var1.put("CertificateCacheSize", var2);
         var2.setValue("description", "<p>The number of certificates held that have not been redeemed by tokens.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(3));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("Ciphersuites")) {
         var3 = "getCiphersuites";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCiphersuites";
         }

         var2 = new PropertyDescriptor("Ciphersuites", SSLMBean.class, var3, var4);
         var1.put("Ciphersuites", var2);
         var2.setValue("description", "<p>Indicates the cipher suites being used on a particular WebLogic Server.</p>  <p>For a list of possible values, see <a href=\"http://www.oracle.com/pls/as1111/lookup?id=SECMG502\">Cipher Suites Supported in WebLogic Server</a>.</p>  <p>The default is SSL_RSA_EXPORT_WITH_RC4_40_MD5 (for JSSE) or TLS_RSA_EXPORT_WITH_RC4_40_MD5 (Certicom).</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      String[] var5;
      if (!var1.containsKey("ClientCertAlias")) {
         var3 = "getClientCertAlias";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClientCertAlias";
         }

         var2 = new PropertyDescriptor("ClientCertAlias", SSLMBean.class, var3, var4);
         var1.put("ClientCertAlias", var2);
         var2.setValue("description", "Determines the alias of the client SSL certificate to be used as identity for outbound SSL connections. The certificate is assumed to be stored in the server configured keystore. <p/> Note that to use the client SSL certificate, <code>{@link #setUseClientCertForOutbound}</code> must be enabled. ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#setClientCertAlias"), BeanInfoHelper.encodeEntities("#isUseClientCertForOutbound")};
         var2.setValue("see", var5);
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ClientCertPrivateKeyPassPhrase")) {
         var3 = "getClientCertPrivateKeyPassPhrase";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClientCertPrivateKeyPassPhrase";
         }

         var2 = new PropertyDescriptor("ClientCertPrivateKeyPassPhrase", SSLMBean.class, var3, var4);
         var1.put("ClientCertPrivateKeyPassPhrase", var2);
         var2.setValue("description", "The passphrase used to retrieve the private key for the client SSL certificate specified in <code>{@link #getClientCertAlias}</code> from the server configured keystore. This passphrase is assigned to the private key when the private key is generated. <p/> Note that this attribute is usually used when outbound SSL connections specify a client SSL certificate identity. <p/> Note that when you get the value of this attribute, WebLogic Server does the following: <ol> <li>Retrieves the value of the <code>ClientCertPrivateKeyPassPhraseEncrypted</code> attribute. <li>Decrypts the value and returns the unencrypted passphrase. </ol> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#setClientCertPrivateKeyPassPhrase"), BeanInfoHelper.encodeEntities("#isUseClientCertForOutbound"), BeanInfoHelper.encodeEntities("#getClientCertAlias")};
         var2.setValue("see", var5);
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ClientCertPrivateKeyPassPhraseEncrypted")) {
         var3 = "getClientCertPrivateKeyPassPhraseEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClientCertPrivateKeyPassPhraseEncrypted";
         }

         var2 = new PropertyDescriptor("ClientCertPrivateKeyPassPhraseEncrypted", SSLMBean.class, var3, var4);
         var1.put("ClientCertPrivateKeyPassPhraseEncrypted", var2);
         var2.setValue("description", "The encrypted passphrase used to retrieve the private key for the client SSL certificate specified in <code>{@link #getClientCertAlias}</code> from the server configured keystore. This passphrase is assigned to the private key when the private key is generated. <p/> To compare a password that a user enters with the encrypted value of this attribute, go to the same WebLogic Server instance that you used to set and encrypt this attribute, and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values. <p/> Note that this attribute is usually used when outbound SSL connections specify a client SSL certificate identity. ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#setClientCertPrivateKeyPassPhraseEncrypted"), BeanInfoHelper.encodeEntities("#isUseClientCertForOutbound"), BeanInfoHelper.encodeEntities("#getClientCertAlias"), BeanInfoHelper.encodeEntities("#getClientCertPrivateKeyPassPhrase")};
         var2.setValue("see", var5);
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ExportKeyLifespan")) {
         var3 = "getExportKeyLifespan";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setExportKeyLifespan";
         }

         var2 = new PropertyDescriptor("ExportKeyLifespan", SSLMBean.class, var3, var4);
         var1.put("ExportKeyLifespan", var2);
         var2.setValue("description", "<p>Indicates the number of times WebLogic Server can use an exportable key between a domestic server and an exportable client before generating a new key. The more secure you want WebLogic Server to be, the fewer times the key should be used before generating a new key.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(500));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("HostnameVerifier")) {
         var3 = "getHostnameVerifier";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setHostnameVerifier";
         }

         var2 = new PropertyDescriptor("HostnameVerifier", SSLMBean.class, var3, var4);
         var1.put("HostnameVerifier", var2);
         var2.setValue("description", "<p>The name of the class that implements the <code>weblogic.security.SSL.HostnameVerifier</code> interface.</p>  <p>This class verifies whether the connection to the host with the hostname from URL should be allowed. The class is used to prevent man-in-the-middle attacks. The <code>weblogic.security.SSL.HostnameVerifier</code> has a <code>verify()</code> method that WebLogic Server calls on the client during the SSL handshake.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("secureValue", "weblogic.security.SSL.HostnameVerifier");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("IdentityAndTrustLocations")) {
         var3 = "getIdentityAndTrustLocations";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setIdentityAndTrustLocations";
         }

         var2 = new PropertyDescriptor("IdentityAndTrustLocations", SSLMBean.class, var3, var4);
         var1.put("IdentityAndTrustLocations", var2);
         var2.setValue("description", "<p>Indicates where SSL should find the server's identity (certificate and private key) as well as the server's trust (trusted CAs).</p>  <ul> <li> <p>If set to <code>KEYSTORES</code>, then SSL retrieves the identity and trust from the server's keystores (that are configured on the Server).</p> </li>  <li> <p>If set to <code>FILES_OR_KEYSTORE_PROVIDERS</code>, then SSL first looks in the deprecated KeyStore providers for the identity and trust. If not found, then it looks in the flat files indicated by the SSL Trusted CA File Name, Server Certificate File Name, and Server Key File Name attributes.</p> </li> </ul>  <p>Domains created in WebLogic Server version 8.1 or later, default to <code>KEYSTORES</code>. Domains created before WebLogic Server version 8.1, default to <code>FILES_OR_KEYSTORE_PROVIDERS.</code></p> ");
         setPropertyDescriptorDefault(var2, "KeyStores");
         var2.setValue("legalValues", new Object[]{"KeyStores", "FilesOrKeyStoreProviders"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("InboundCertificateValidation")) {
         var3 = "getInboundCertificateValidation";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setInboundCertificateValidation";
         }

         var2 = new PropertyDescriptor("InboundCertificateValidation", SSLMBean.class, var3, var4);
         var1.put("InboundCertificateValidation", var2);
         var2.setValue("description", "<p>Indicates the client certificate validation rules for inbound SSL.</p>  <p>This attribute only applies to ports and network channels using 2-way SSL.</p> ");
         setPropertyDescriptorDefault(var2, "BuiltinSSLValidationOnly");
         var2.setValue("legalValues", new Object[]{"BuiltinSSLValidationOnly", "BuiltinSSLValidationAndCertPathValidators"});
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("ListenPort")) {
         var3 = "getListenPort";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setListenPort";
         }

         var2 = new PropertyDescriptor("ListenPort", SSLMBean.class, var3, var4);
         var1.put("ListenPort", var2);
         var2.setValue("description", "<p>The TCP/IP port at which this server listens for SSL connection requests.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#isEnabled()"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#getListenPort()"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#getAdministrationPort()"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.NetworkAccessPointMBean#getListenPort()")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(7002));
         var2.setValue("legalMax", new Integer(65535));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("LoginTimeoutMillis")) {
         var3 = "getLoginTimeoutMillis";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLoginTimeoutMillis";
         }

         var2 = new PropertyDescriptor("LoginTimeoutMillis", SSLMBean.class, var3, var4);
         var1.put("LoginTimeoutMillis", var2);
         var2.setValue("description", "<p>Specifies the number of milliseconds that WebLogic Server waits for an SSL connection before timing out. SSL connections take longer to negotiate than regular connections.</p>  <p>If clients are connecting over the Internet, raise the default number to accommodate additional network latency.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.ServerMBean#getLoginTimeoutMillis()"), BeanInfoHelper.encodeEntities("weblogic.management.configuration.NetworkChannelMBean#getLoginTimeoutMillisSSL()")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(25000));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", SSLMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("OutboundCertificateValidation")) {
         var3 = "getOutboundCertificateValidation";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOutboundCertificateValidation";
         }

         var2 = new PropertyDescriptor("OutboundCertificateValidation", SSLMBean.class, var3, var4);
         var1.put("OutboundCertificateValidation", var2);
         var2.setValue("description", "<p>Indicates the server certificate validation rules for outbound SSL.</p>  <p>This attribute always applies to outbound SSL that is part of WebLogic Server (that is, an Administration Server talking to the Node Manager). It does not apply to application code in the server that is using outbound SSL unless the application code uses a <code>weblogic.security.SSL.ServerTrustManager</code> that is configured to use outbound SSL validation.</p> ");
         setPropertyDescriptorDefault(var2, "BuiltinSSLValidationOnly");
         var2.setValue("legalValues", new Object[]{"BuiltinSSLValidationOnly", "BuiltinSSLValidationAndCertPathValidators"});
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("OutboundPrivateKeyAlias")) {
         var3 = "getOutboundPrivateKeyAlias";
         var4 = null;
         var2 = new PropertyDescriptor("OutboundPrivateKeyAlias", SSLMBean.class, var3, var4);
         var1.put("OutboundPrivateKeyAlias", var2);
         var2.setValue("description", "<p>The string alias used to store and retrieve the outbound private key in the keystore. This private key is associated with either a server or a client digital certificate. This attribute value is derived from other settings and cannot be physically set.</p>  <p> The returned value is determined as follows: <ul> <li>If the <code>{@link #isUseClientCertForOutbound}</code> returns true, the value from <code>{@link #getClientCertAlias}</code> is returned. <li> Otherwise, the value from <code>{@link #getServerPrivateKeyAlias}</code> is returned. </ul> </p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#isUseClientCertForOutbound"), BeanInfoHelper.encodeEntities("#getClientCertAlias"), BeanInfoHelper.encodeEntities("#getServerPrivateKeyAlias")};
         var2.setValue("see", var5);
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("OutboundPrivateKeyPassPhrase")) {
         var3 = "getOutboundPrivateKeyPassPhrase";
         var4 = null;
         var2 = new PropertyDescriptor("OutboundPrivateKeyPassPhrase", SSLMBean.class, var3, var4);
         var1.put("OutboundPrivateKeyPassPhrase", var2);
         var2.setValue("description", "<p>The passphrase used to retrieve the outbound private key from the keystore. This passphrase is assigned to the private key when it is generated. This attribute value is derived from other settings and cannot be physically set.</p>  <p> The returned value is determined as follows: <ul> <li>If the <code>{@link #isUseClientCertForOutbound}</code> returns true, the value from <code>{@link #getClientCertPrivateKeyPassPhrase}</code> is returned. <li> Otherwise, the value from <code>{@link #getServerPrivateKeyPassPhrase}</code> is returned. </ul> </p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#isUseClientCertForOutbound"), BeanInfoHelper.encodeEntities("#getClientCertPrivateKeyPassPhrase"), BeanInfoHelper.encodeEntities("#getServerPrivateKeyPassPhrase")};
         var2.setValue("see", var5);
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("PeerValidationEnforced")) {
         var3 = "getPeerValidationEnforced";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPeerValidationEnforced";
         }

         var2 = new PropertyDescriptor("PeerValidationEnforced", SSLMBean.class, var3, var4);
         var1.put("PeerValidationEnforced", var2);
         var2.setValue("description", " ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("deprecated", "6.1.0.0 this is an unused attribute. ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ServerCertificateChainFileName")) {
         var3 = "getServerCertificateChainFileName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setServerCertificateChainFileName";
         }

         var2 = new PropertyDescriptor("ServerCertificateChainFileName", SSLMBean.class, var3, var4);
         var1.put("ServerCertificateChainFileName", var2);
         var2.setValue("description", "<p>The full directory location and name of the file containing an ordered list of certificate authorities trusted by WebLogic Server.</p>  <p>The <code>.pem</code> file extension indicates that method that should be used to read the file. Note that as of WebLogic Server version 7.0, the digital certificate for WebLogic Server should not be stored in a file.</p> ");
         setPropertyDescriptorDefault(var2, "server-certchain.pem");
         var2.setValue("deprecated", "7.0.0.0 server certificates (and chains) should be stored in keystores. ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ServerCertificateFileName")) {
         var3 = "getServerCertificateFileName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setServerCertificateFileName";
         }

         var2 = new PropertyDescriptor("ServerCertificateFileName", SSLMBean.class, var3, var4);
         var1.put("ServerCertificateFileName", var2);
         var2.setValue("description", "<p>The full directory location of the digital certificate file (<code>.der</code> or <code>.pem</code>) for the server.</p>  <p>The pathname should either be absolute or relative to the directory from which the server is booted. This field provides backward compatibility for security configurations that stored digital certificates in files.</p>  <p>The file extension ( <code>.der</code> or <code>.pem</code>) tells WebLogic Server how to read the contents of the file.</p> ");
         setPropertyDescriptorDefault(var2, "server-cert.der");
         var2.setValue("deprecated", "8.1.0.0 server certificates (and chains) should be stored in keystores. ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ServerKeyFileName")) {
         var3 = "getServerKeyFileName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setServerKeyFileName";
         }

         var2 = new PropertyDescriptor("ServerKeyFileName", SSLMBean.class, var3, var4);
         var1.put("ServerKeyFileName", var2);
         var2.setValue("description", "<p>The full directory location of the private key file (<code>.der</code> or <code>.pem</code>) for the server.</p>  <p>The pathname should either be absolute or relative to the directory from which the server is booted. This field provides backward compatibility for security configurations that store private keys in files. For a more secure deployment, Oracle recommends saving private keys in keystores.</p>  <p>The file extension (<code>.der</code> or <code>.pem</code>) indicates the method that should be used to read the file.</p> ");
         setPropertyDescriptorDefault(var2, "server-key.der");
         var2.setValue("deprecated", "8.1.0.0 private keys should be stored in keystores. ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ServerPrivateKeyAlias")) {
         var3 = "getServerPrivateKeyAlias";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setServerPrivateKeyAlias";
         }

         var2 = new PropertyDescriptor("ServerPrivateKeyAlias", SSLMBean.class, var3, var4);
         var1.put("ServerPrivateKeyAlias", var2);
         var2.setValue("description", "<p>The string alias used to store and retrieve the server's private key in the keystore. This private key is associated with the server's digital certificate.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ServerPrivateKeyPassPhrase")) {
         var3 = "getServerPrivateKeyPassPhrase";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setServerPrivateKeyPassPhrase";
         }

         var2 = new PropertyDescriptor("ServerPrivateKeyPassPhrase", SSLMBean.class, var3, var4);
         var1.put("ServerPrivateKeyPassPhrase", var2);
         var2.setValue("description", "<p>The passphrase used to retrieve the server's private key from the keystore. This passphrase is assigned to the private key when it is generated.</p> ");
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("ServerPrivateKeyPassPhraseEncrypted")) {
         var3 = "getServerPrivateKeyPassPhraseEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setServerPrivateKeyPassPhraseEncrypted";
         }

         var2 = new PropertyDescriptor("ServerPrivateKeyPassPhraseEncrypted", SSLMBean.class, var3, var4);
         var1.put("ServerPrivateKeyPassPhraseEncrypted", var2);
         var2.setValue("description", "<p>The encrypted passphrase used to retrieve the server's private key from the keystore. This passphrase is assigned to the private key when it is generated.</p>  <p>To set this attribute, use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the encrypt() method.</p>  <p>To compare a password that a user enters with the encrypted value of this attribute, go to the same WebLogic Server instance that you used to set and encrypt this attribute and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TrustedCAFileName")) {
         var3 = "getTrustedCAFileName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTrustedCAFileName";
         }

         var2 = new PropertyDescriptor("TrustedCAFileName", SSLMBean.class, var3, var4);
         var1.put("TrustedCAFileName", var2);
         var2.setValue("description", "<p>The full directory location of the file that specifies the certificate authorities trusted by the server.</p>  <p>The pathname should either be absolute or relative to the directory from which the server is booted. This field provides backward compatibility for security configurations that store trusted certificate authorities in files.</p>  <p>The file specified in this attribute can contain a single digital certificate or multiple digital certificates. The file extension ( <code>.der</code> or <code>.pem</code>) tells WebLogic Server how to read the contents of the file.</p> ");
         setPropertyDescriptorDefault(var2, "trusted-ca.pem");
         var2.setValue("deprecated", "8.1.0.0 trusted CAs should be stored in keystores. ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.0.0", (String)null, this.targetVersion) && !var1.containsKey("AllowUnencryptedNullCipher")) {
         var3 = "isAllowUnencryptedNullCipher";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAllowUnencryptedNullCipher";
         }

         var2 = new PropertyDescriptor("AllowUnencryptedNullCipher", SSLMBean.class, var3, var4);
         var1.put("AllowUnencryptedNullCipher", var2);
         var2.setValue("description", "<p>Test if the AllowUnEncryptedNullCipher is enabled</p> <p>see <code>setAllowUnencryptedNullCipher(boolean enable)</code> for the NullCipher feature.</p> ");
         var2.setValue("since", "10.3.0.0");
      }

      if (!var1.containsKey("ClientCertificateEnforced")) {
         var3 = "isClientCertificateEnforced";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClientCertificateEnforced";
         }

         var2 = new PropertyDescriptor("ClientCertificateEnforced", SSLMBean.class, var3, var4);
         var1.put("ClientCertificateEnforced", var2);
         var2.setValue("description", "<p>Indicates whether or not clients must present digital certificates from a trusted certificate authority to WebLogic Server.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Enabled")) {
         var3 = "isEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEnabled";
         }

         var2 = new PropertyDescriptor("Enabled", SSLMBean.class, var3, var4);
         var1.put("Enabled", var2);
         var2.setValue("description", "<p>Indicates whether the server can be reached through the default SSL listen port.</p>  <p>If the administration port is enabled for the WebLogic Server domain, then administrative traffic travels over the administration port and application traffic travels over the Listen Port and SSL Listen Port. If the administration port is disabled, then all traffic travels over the Listen Port and SSL Listen Port.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("HandlerEnabled")) {
         var3 = "isHandlerEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setHandlerEnabled";
         }

         var2 = new PropertyDescriptor("HandlerEnabled", SSLMBean.class, var3, var4);
         var1.put("HandlerEnabled", var2);
         var2.setValue("description", "<p>Not used.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("HostnameVerificationIgnored")) {
         var3 = "isHostnameVerificationIgnored";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setHostnameVerificationIgnored";
         }

         var2 = new PropertyDescriptor("HostnameVerificationIgnored", SSLMBean.class, var3, var4);
         var1.put("HostnameVerificationIgnored", var2);
         var2.setValue("description", "<p>Specifies whether to ignore the installed implementation of the <code>weblogic.security.SSL.HostnameVerifier</code> interface (when this server is acting as a client to another application server).</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("JSSEEnabled")) {
         var3 = "isJSSEEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJSSEEnabled";
         }

         var2 = new PropertyDescriptor("JSSEEnabled", SSLMBean.class, var3, var4);
         var1.put("JSSEEnabled", var2);
         var2.setValue("description", "Determines whether the SSL implementation in Weblogic Server is JSSE based. ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("KeyEncrypted")) {
         var3 = "isKeyEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setKeyEncrypted";
         }

         var2 = new PropertyDescriptor("KeyEncrypted", SSLMBean.class, var3, var4);
         var1.put("KeyEncrypted", var2);
         var2.setValue("description", "<p>Indicates whether or not the private key for the WebLogic Server has been encrypted with a password. This attribute is no longer used as of WebLogic Server version 7.0.</p>  <ul> <li> <p>If the attribute is set to <code>true,</code> the private key requires a password be supplied in order to use the key.</p> </li>  <li> <p>If the attribute is set to <code>false</code>, the private key is unencrypted and may be used without providing a password.</p> </li> </ul> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(true));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("8.1.0.0", (String)null, this.targetVersion) && !var1.containsKey("SSLRejectionLoggingEnabled")) {
         var3 = "isSSLRejectionLoggingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSSLRejectionLoggingEnabled";
         }

         var2 = new PropertyDescriptor("SSLRejectionLoggingEnabled", SSLMBean.class, var3, var4);
         var1.put("SSLRejectionLoggingEnabled", var2);
         var2.setValue("description", "<p>Indicates whether warning messages are logged in the server log when SSL connections are rejected.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "8.1.0.0");
      }

      if (!var1.containsKey("TwoWaySSLEnabled")) {
         var3 = "isTwoWaySSLEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTwoWaySSLEnabled";
         }

         var2 = new PropertyDescriptor("TwoWaySSLEnabled", SSLMBean.class, var3, var4);
         var1.put("TwoWaySSLEnabled", var2);
         var2.setValue("description", "<p>The form of SSL that should be used.</p>  <p>By default, WebLogic Server is configured to use one-way SSL (implied by the <code>Client Certs Not Requested</code> value). Selecting <code>Client Certs Requested But Not Enforced</code> enables two-way SSL. With this option, the server requests a certificate from the client, but the connection continues if the client does not present a certificate. Selecting <code>Client Certs Requested And Enforced</code> also enables two-way SSL and requires a client to present a certificate. However, if a certificate is not presented, the SSL connection is terminated.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("UseClientCertForOutbound")) {
         var3 = "isUseClientCertForOutbound";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUseClientCertForOutbound";
         }

         var2 = new PropertyDescriptor("UseClientCertForOutbound", SSLMBean.class, var3, var4);
         var1.put("UseClientCertForOutbound", var2);
         var2.setValue("description", "Determines whether to use the configured client SSL certificate as identity for outbound SSL connections. <p/> Note that to use a client SSL certificate, one must be specified in <code>{@link #setClientCertAlias}</code>. ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#setUseClientCertForOutbound"), BeanInfoHelper.encodeEntities("#getClientCertAlias")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("UseJava")) {
         var3 = "isUseJava";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUseJava";
         }

         var2 = new PropertyDescriptor("UseJava", SSLMBean.class, var3, var4);
         var1.put("UseJava", var2);
         var2.setValue("description", "<p>Enables the use of native Java libraries.</p>  <p>WebLogic Server provides a pure-Java implementation of the SSL protocol. Native libraries enhance the performance for SSL operations on the Solaris, Windows NT, and IBM AIX platforms.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("UseServerCerts")) {
         var3 = "isUseServerCerts";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUseServerCerts";
         }

         var2 = new PropertyDescriptor("UseServerCerts", SSLMBean.class, var3, var4);
         var1.put("UseServerCerts", var2);
         var2.setValue("description", "Sets whether the client should use the server certificates/key as the client identity when initiating an outbound connection over https. ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
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
      Method var3 = SSLMBean.class.getMethod("freezeCurrentValue", String.class);
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

      var3 = SSLMBean.class.getMethod("restoreDefaultValue", String.class);
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
