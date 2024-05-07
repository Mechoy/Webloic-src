package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class SingleSignOnServicesMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SingleSignOnServicesMBean.class;

   public SingleSignOnServicesMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SingleSignOnServicesMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SingleSignOnServicesMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.5.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This MBean represents configuration for SAML 2.0-based local site Single Sign-On Services.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.SingleSignOnServicesMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("ArtifactMaxCacheSize")) {
         var3 = "getArtifactMaxCacheSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setArtifactMaxCacheSize";
         }

         var2 = new PropertyDescriptor("ArtifactMaxCacheSize", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("ArtifactMaxCacheSize", var2);
         var2.setValue("description", "<p>The maximum size of the artifact cache.</p>  <p>This cache contains the artifacts issued by the local site that are awaiting referencing by a partner.  Specify '0' to indicate that the cache is unbounded.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(10000));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ArtifactTimeout")) {
         var3 = "getArtifactTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setArtifactTimeout";
         }

         var2 = new PropertyDescriptor("ArtifactTimeout", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("ArtifactTimeout", var2);
         var2.setValue("description", "<p>The maximum timeout (in seconds) of artifacts stored in the local cache.</p>  <p>This cache stores artifacts issued by the local site that are awaiting referencing by a partner. Artifacts that reach this maximum timeout duration are expired in the local cache even if no reference request has been received from the partner.  If a reference request is subsequently received from the partner, the cache behaves as if the artifact had never been generated.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(300));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("AuthnRequestMaxCacheSize")) {
         var3 = "getAuthnRequestMaxCacheSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAuthnRequestMaxCacheSize";
         }

         var2 = new PropertyDescriptor("AuthnRequestMaxCacheSize", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("AuthnRequestMaxCacheSize", var2);
         var2.setValue("description", "<p>The maximum size of the authentication request cache.</p>  <p>This cache stores documents issued by the local Service Provider that are awaiting response from a partner Identity Provider.  </p>  <p>Specify '0' to indicate that the cache is unbounded.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(10000));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("AuthnRequestTimeout")) {
         var3 = "getAuthnRequestTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAuthnRequestTimeout";
         }

         var2 = new PropertyDescriptor("AuthnRequestTimeout", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("AuthnRequestTimeout", var2);
         var2.setValue("description", "<p>The maximum timeout (in seconds) of &lt;AuthnRequest&gt; documents stored in the local cache.</p>  <p>This cache stores documents issued by the local Service provider that are awaiting response from a partner Identity Provider. Documents that reach this maximum timeout duration are expired from the local cache even if no response is received from the Identity Provider.  If a response is subsequently returned by the Identity Provider, the cache behaves as if the &lt;AuthnRequest&gt; had never been generated.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(300));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("BasicAuthPassword")) {
         var3 = "getBasicAuthPassword";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBasicAuthPassword";
         }

         var2 = new PropertyDescriptor("BasicAuthPassword", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("BasicAuthPassword", var2);
         var2.setValue("description", "<p>The password used to assign Basic Authentication credentials to outgoing HTTPS connections</p> ");
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("BasicAuthPasswordEncrypted")) {
         var3 = "getBasicAuthPasswordEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBasicAuthPasswordEncrypted";
         }

         var2 = new PropertyDescriptor("BasicAuthPasswordEncrypted", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("BasicAuthPasswordEncrypted", var2);
         var2.setValue("description", "<p>The encrypted password used assign Basic Authentication credentials to outgoing HTTPS connections.</p>  <p>To set this attribute, use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the encrypt() method.</p>  <p>To compare a password that a user enters with the encrypted value of this attribute, go to the same WebLogic Server instance that you used to set and encrypt this attribute and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("BasicAuthUsername")) {
         var3 = "getBasicAuthUsername";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBasicAuthUsername";
         }

         var2 = new PropertyDescriptor("BasicAuthUsername", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("BasicAuthUsername", var2);
         var2.setValue("description", "The username that is used to assign Basic authentication credentials to outgoing HTTPS connections. ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ContactPersonCompany")) {
         var3 = "getContactPersonCompany";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setContactPersonCompany";
         }

         var2 = new PropertyDescriptor("ContactPersonCompany", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("ContactPersonCompany", var2);
         var2.setValue("description", "<p>The contact person's company name.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ContactPersonEmailAddress")) {
         var3 = "getContactPersonEmailAddress";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setContactPersonEmailAddress";
         }

         var2 = new PropertyDescriptor("ContactPersonEmailAddress", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("ContactPersonEmailAddress", var2);
         var2.setValue("description", "<p>The contact person's e-mail address.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ContactPersonGivenName")) {
         var3 = "getContactPersonGivenName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setContactPersonGivenName";
         }

         var2 = new PropertyDescriptor("ContactPersonGivenName", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("ContactPersonGivenName", var2);
         var2.setValue("description", "<p>The contact person given (first) name.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ContactPersonSurName")) {
         var3 = "getContactPersonSurName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setContactPersonSurName";
         }

         var2 = new PropertyDescriptor("ContactPersonSurName", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("ContactPersonSurName", var2);
         var2.setValue("description", "<p>The contact person surname (last name).</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ContactPersonTelephoneNumber")) {
         var3 = "getContactPersonTelephoneNumber";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setContactPersonTelephoneNumber";
         }

         var2 = new PropertyDescriptor("ContactPersonTelephoneNumber", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("ContactPersonTelephoneNumber", var2);
         var2.setValue("description", "<p>The contact person's telephone number.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ContactPersonType")) {
         var3 = "getContactPersonType";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setContactPersonType";
         }

         var2 = new PropertyDescriptor("ContactPersonType", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("ContactPersonType", var2);
         var2.setValue("description", "<p>The contact person type.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DefaultURL")) {
         var3 = "getDefaultURL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultURL";
         }

         var2 = new PropertyDescriptor("DefaultURL", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("DefaultURL", var2);
         var2.setValue("description", "<p>The Service Provider's default URL.</p>  <p>When an unsolicited SSO response arrives at the Service Provider without an accompanying target URL, the user (if authenticated) is redirected to this default URL.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("EntityID")) {
         var3 = "getEntityID";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEntityID";
         }

         var2 = new PropertyDescriptor("EntityID", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("EntityID", var2);
         var2.setValue("description", "<p>The string that uniquely identifies the local site.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ErrorPath")) {
         var3 = "getErrorPath";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setErrorPath";
         }

         var2 = new PropertyDescriptor("ErrorPath", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("ErrorPath", var2);
         var2.setValue("description", "Gets the Error Path URL.  Partner sites may redirect users to this URL for more information if SSO fails. ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("IdentityProviderPreferredBinding")) {
         var3 = "getIdentityProviderPreferredBinding";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setIdentityProviderPreferredBinding";
         }

         var2 = new PropertyDescriptor("IdentityProviderPreferredBinding", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("IdentityProviderPreferredBinding", var2);
         var2.setValue("description", "<p>Specifies the preferred binding type for endpoints of the Identity Provider services. Must be set to <code>None</code>, <code>HTTP/POST</code>, <code>HTTP/Artifact</code>, or <code>HTTP/Redirect</code>.</p> ");
         setPropertyDescriptorDefault(var2, "None");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("legalValues", new Object[]{"None", "HTTP/POST", "HTTP/Artifact", "HTTP/Redirect"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("LoginReturnQueryParameter")) {
         var3 = "getLoginReturnQueryParameter";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLoginReturnQueryParameter";
         }

         var2 = new PropertyDescriptor("LoginReturnQueryParameter", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("LoginReturnQueryParameter", var2);
         var2.setValue("description", "The name of the query parameter to be used for conveying the login-return URL to the login form web application. ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("LoginURL")) {
         var3 = "getLoginURL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLoginURL";
         }

         var2 = new PropertyDescriptor("LoginURL", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("LoginURL", var2);
         var2.setValue("description", "<p>The URL of the login form web application to which unauthenticated requests are directed.</p>  <p>By default, the login URL is <code>/saml2/idp/login</code> using Basic authentication. Typically you specify this URL if you are using a custom login web application.</p> ");
         setPropertyDescriptorDefault(var2, "/saml2/idp/login");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("OrganizationName")) {
         var3 = "getOrganizationName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOrganizationName";
         }

         var2 = new PropertyDescriptor("OrganizationName", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("OrganizationName", var2);
         var2.setValue("description", "<p>The organization name.</p>  <p>This string specifies the name of the organization to which a user may refer for obtaining additional information about the local site.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("OrganizationURL")) {
         var3 = "getOrganizationURL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOrganizationURL";
         }

         var2 = new PropertyDescriptor("OrganizationURL", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("OrganizationURL", var2);
         var2.setValue("description", "<p>The organization URL.</p>  <p>This string specifies a location to which a user may refer for information about the local site. This string is not used by SAML 2.0 services for the actual handling or processing of messages.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("PublishedSiteURL")) {
         var3 = "getPublishedSiteURL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPublishedSiteURL";
         }

         var2 = new PropertyDescriptor("PublishedSiteURL", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("PublishedSiteURL", var2);
         var2.setValue("description", "<p>The published site URL.</p>  <p>When publishing SAML 2.0 metadata, this URL is used as a base URL to construct endpoint URLs for the various SAML 2.0 services.  The published site URL is also used during request processing to generate and/or parse various URLs.</p>  <p>The hostname and port portion of the URL should be the hostname and port at which the server is visible externally; this may not be the same as the hostname and port by which the server is known locally. If you are configuring SAML 2.0 services in a cluster, the hostname and port may correspond to the load balancer or proxy server that distributes client requests to servers in the cluster.</p>  <p>The remainder of the URL should be a single path component corresponding to the application context at which the SAML 2.0 services application is deployed (typically <code>/saml2<code>).</p> ");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("#setPublishedSiteURL(String)")};
         var2.setValue("see", var5);
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SSOSigningKeyAlias")) {
         var3 = "getSSOSigningKeyAlias";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSSOSigningKeyAlias";
         }

         var2 = new PropertyDescriptor("SSOSigningKeyAlias", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("SSOSigningKeyAlias", var2);
         var2.setValue("description", "<p>The keystore alias for the key to be used when signing documents.</p>  <p>The key is used to generate signatures on all the outgoing documents, such as authentication requests and responses. If you do not specify an alias, the server’s configured SSL private key alias from the server's SSL configuration is used by default.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SSOSigningKeyPassPhrase")) {
         var3 = "getSSOSigningKeyPassPhrase";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSSOSigningKeyPassPhrase";
         }

         var2 = new PropertyDescriptor("SSOSigningKeyPassPhrase", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("SSOSigningKeyPassPhrase", var2);
         var2.setValue("description", "<p>The passphrase used to retrieve the local site's SSO signing key from the keystore.</p>  <p>If you do not specify a keystore alias and passphrase, the server's configured private key alias and private key passphrase from the server's SSL configuration is used by default.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SSOSigningKeyPassPhraseEncrypted")) {
         var3 = "getSSOSigningKeyPassPhraseEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSSOSigningKeyPassPhraseEncrypted";
         }

         var2 = new PropertyDescriptor("SSOSigningKeyPassPhraseEncrypted", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("SSOSigningKeyPassPhraseEncrypted", var2);
         var2.setValue("description", "<p>The encrypted passphrase used to retrieve the local site's SSO signing key from the keystore.</p>  <p>To set this attribute, use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the encrypt() method.</p>  <p>To compare a password that a user enters with the encrypted value of this attribute, go to the same WebLogic Server instance that you used to set and encrypt this attribute and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ServiceProviderPreferredBinding")) {
         var3 = "getServiceProviderPreferredBinding";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setServiceProviderPreferredBinding";
         }

         var2 = new PropertyDescriptor("ServiceProviderPreferredBinding", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("ServiceProviderPreferredBinding", var2);
         var2.setValue("description", "Specifies the preferred binding type for endpoints of Service Provider services. Must be set to \"None\", \"POST\", or \"Artifact\". ");
         setPropertyDescriptorDefault(var2, "None");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("legalValues", new Object[]{"None", "HTTP/POST", "HTTP/Artifact"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TransportLayerSecurityKeyAlias")) {
         var3 = "getTransportLayerSecurityKeyAlias";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTransportLayerSecurityKeyAlias";
         }

         var2 = new PropertyDescriptor("TransportLayerSecurityKeyAlias", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("TransportLayerSecurityKeyAlias", var2);
         var2.setValue("description", "<p>The string alias used to store and retrieve the server's private key, which is used to establish outgoing TLS/SSL connections.</p>  <p>If you do not specify an alias, the server’s configured SSL private key alias from the server's SSL configuration is used for the TLS alias by default.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TransportLayerSecurityKeyPassPhrase")) {
         var3 = "getTransportLayerSecurityKeyPassPhrase";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTransportLayerSecurityKeyPassPhrase";
         }

         var2 = new PropertyDescriptor("TransportLayerSecurityKeyPassPhrase", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("TransportLayerSecurityKeyPassPhrase", var2);
         var2.setValue("description", "<p>The passphrase used to retrieve the server's private key from the keystore.</p>  <p>If you do not specify either an alias or a passphrase, the server’s configured SSL private key alias and private key passphrase from the server's SSL configuration is used for the TLS alias and passphrase by default.</p> ");
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TransportLayerSecurityKeyPassPhraseEncrypted")) {
         var3 = "getTransportLayerSecurityKeyPassPhraseEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTransportLayerSecurityKeyPassPhraseEncrypted";
         }

         var2 = new PropertyDescriptor("TransportLayerSecurityKeyPassPhraseEncrypted", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("TransportLayerSecurityKeyPassPhraseEncrypted", var2);
         var2.setValue("description", "<p>The encrypted passphrase used to retrieve the local site's TLS/SSL key from the keystore.</p>  <p>To set this attribute, use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the encrypt() method.</p>  <p>To compare a password that a user enters with the encrypted value of this attribute, go to the same WebLogic Server instance that you used to set and encrypt this attribute and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ForceAuthn")) {
         var3 = "isForceAuthn";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setForceAuthn";
         }

         var2 = new PropertyDescriptor("ForceAuthn", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("ForceAuthn", var2);
         var2.setValue("description", "<p>Specifies whether the Identity Provider must authenticate users directly and not use a previous security context. The default is <code>false</code>. </p>  <p>Note the following:</p> <ol> <li>Setting <code>ForceAuthn</code> to <code>true</code> -- that is, enabling Force Authentication -- has no effect in WebLogic Server. SAML logout is not supported in WebLogic Server, so even if the user is already authenticated at the Identity Provider site and <code>ForceAuthn</code> is set to <code>true</code>, the user is not forced to authenticate again at the Identity Provider site.</li> <li>Setting both <code>ForceAuthn</code> and <code>IsPassive</code> to <code>true</code> -- that is, Force Authentication and Passive are enabled -- is an invalid configuration that causes WebLogic server to generate an exception and also causes the single sign-on session to fail.</li> </ol> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("IdentityProviderArtifactBindingEnabled")) {
         var3 = "isIdentityProviderArtifactBindingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setIdentityProviderArtifactBindingEnabled";
         }

         var2 = new PropertyDescriptor("IdentityProviderArtifactBindingEnabled", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("IdentityProviderArtifactBindingEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the Artifact binding is enabled for the Identity Provider. ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("IdentityProviderEnabled")) {
         var3 = "isIdentityProviderEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setIdentityProviderEnabled";
         }

         var2 = new PropertyDescriptor("IdentityProviderEnabled", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("IdentityProviderEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the local site is enabled for the Identity Provider role.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("IdentityProviderPOSTBindingEnabled")) {
         var3 = "isIdentityProviderPOSTBindingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setIdentityProviderPOSTBindingEnabled";
         }

         var2 = new PropertyDescriptor("IdentityProviderPOSTBindingEnabled", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("IdentityProviderPOSTBindingEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the POST binding is enabled for the Identity Provider. ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("IdentityProviderRedirectBindingEnabled")) {
         var3 = "isIdentityProviderRedirectBindingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setIdentityProviderRedirectBindingEnabled";
         }

         var2 = new PropertyDescriptor("IdentityProviderRedirectBindingEnabled", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("IdentityProviderRedirectBindingEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the Redirect binding is enabled for the Identity Provider. ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("POSTOneUseCheckEnabled")) {
         var3 = "isPOSTOneUseCheckEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPOSTOneUseCheckEnabled";
         }

         var2 = new PropertyDescriptor("POSTOneUseCheckEnabled", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("POSTOneUseCheckEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the POST one-use check is enabled.</p>  <p>If set, the local site POST binding endpoints will store identifiers of all inbound documents to ensure that those documents are not presented more than once.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Passive")) {
         var3 = "isPassive";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPassive";
         }

         var2 = new PropertyDescriptor("Passive", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("Passive", var2);
         var2.setValue("description", "<p>Determines whether the Identity Provider and the user must not take control of the user interface from the requester and interact with the user in a noticeable fashion. The default setting is <code>false</code>.</p>  <p>The WebLogic Server SAML 2.0 services generate an exception if Passive (<code>IsPassive</code>) is enabled and the end user is not already authenticated at the Identity Provider site.  In this situation, web single sign-on fails.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RecipientCheckEnabled")) {
         var3 = "isRecipientCheckEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRecipientCheckEnabled";
         }

         var2 = new PropertyDescriptor("RecipientCheckEnabled", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("RecipientCheckEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the recipient/destination check is enabled. When true, the recipient of the SAML Request/Response must match the URL in the HTTP Request.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ReplicatedCacheEnabled")) {
         var3 = "isReplicatedCacheEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setReplicatedCacheEnabled";
         }

         var2 = new PropertyDescriptor("ReplicatedCacheEnabled", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("ReplicatedCacheEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the persistent cache (LDAP or RDBMS) is used for storing SAML 2.0 artifacts and authentication requests.</p>  <p>RDBMS is required by the SAML 2.0 security providers in production environments.  Use LDAP only in development environments.</p>  <p>If this is not set, artifacts and requests are saved in memory.</p>  <p>If you are configuring SAML 2.0 services for two or more WebLogic Server instances in a domain, you must enable the replicated cache individually on each server.  In addition, if you are configuring SAML 2.0 services in a cluster, each Managed Server must also be configured individually.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ServiceProviderArtifactBindingEnabled")) {
         var3 = "isServiceProviderArtifactBindingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setServiceProviderArtifactBindingEnabled";
         }

         var2 = new PropertyDescriptor("ServiceProviderArtifactBindingEnabled", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("ServiceProviderArtifactBindingEnabled", var2);
         var2.setValue("description", "Specifies whether the Artifact binding is enabled for the Service Provider. ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ServiceProviderEnabled")) {
         var3 = "isServiceProviderEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setServiceProviderEnabled";
         }

         var2 = new PropertyDescriptor("ServiceProviderEnabled", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("ServiceProviderEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the local site is enabled for the Service Provider role.</p>  <p>This attribute must be enabled in order to publish the metadata file.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ServiceProviderPOSTBindingEnabled")) {
         var3 = "isServiceProviderPOSTBindingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setServiceProviderPOSTBindingEnabled";
         }

         var2 = new PropertyDescriptor("ServiceProviderPOSTBindingEnabled", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("ServiceProviderPOSTBindingEnabled", var2);
         var2.setValue("description", "Specifies whether the POST binding is enabled for the Service Provider. ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SignAuthnRequests")) {
         var3 = "isSignAuthnRequests";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSignAuthnRequests";
         }

         var2 = new PropertyDescriptor("SignAuthnRequests", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("SignAuthnRequests", var2);
         var2.setValue("description", "<p>Specifies whether authentication requests must be signed.  If set, all outgoing authentication requests are signed.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("WantArtifactRequestsSigned")) {
         var3 = "isWantArtifactRequestsSigned";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWantArtifactRequestsSigned";
         }

         var2 = new PropertyDescriptor("WantArtifactRequestsSigned", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("WantArtifactRequestsSigned", var2);
         var2.setValue("description", "<p>Specifies whether incoming artifact requests must be signed. </p>  <p>This attribute can be set if the Artifact binding is enabled.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("WantAssertionsSigned")) {
         var3 = "isWantAssertionsSigned";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWantAssertionsSigned";
         }

         var2 = new PropertyDescriptor("WantAssertionsSigned", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("WantAssertionsSigned", var2);
         var2.setValue("description", "<p>Specifies whether incoming SAML 2.0 assertions must be signed. ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("WantAuthnRequestsSigned")) {
         var3 = "isWantAuthnRequestsSigned";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWantAuthnRequestsSigned";
         }

         var2 = new PropertyDescriptor("WantAuthnRequestsSigned", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("WantAuthnRequestsSigned", var2);
         var2.setValue("description", "Specifies whether incoming authentication requests must be signed. If set, authentication requests that are not signed are not accepted. ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("WantBasicAuthClientAuthentication")) {
         var3 = "isWantBasicAuthClientAuthentication";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWantBasicAuthClientAuthentication";
         }

         var2 = new PropertyDescriptor("WantBasicAuthClientAuthentication", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("WantBasicAuthClientAuthentication", var2);
         var2.setValue("description", "<p>Specifies whether Basic Authentication client authentication is required.</p>  <p>If enabled, callers to HTTPS bindings of the local site must specify a Basic authentication header, and the username and password must be validated against the Basic authentication values of the binding client partner.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("WantTransportLayerSecurityClientAuthentication")) {
         var3 = "isWantTransportLayerSecurityClientAuthentication";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWantTransportLayerSecurityClientAuthentication";
         }

         var2 = new PropertyDescriptor("WantTransportLayerSecurityClientAuthentication", SingleSignOnServicesMBean.class, var3, var4);
         var1.put("WantTransportLayerSecurityClientAuthentication", var2);
         var2.setValue("description", "<p>Specifies whether TLS/SSL client authentication is required.</p>  <p>If enabled, callers to TLS/SSL bindings of the local site must specify client authentication (two-way SSL), and the identity specified must validate against the TLS certificate of the binding client partner.</p> ");
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
