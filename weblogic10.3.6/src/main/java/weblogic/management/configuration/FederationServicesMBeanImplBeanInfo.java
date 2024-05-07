package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class FederationServicesMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = FederationServicesMBean.class;

   public FederationServicesMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public FederationServicesMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = FederationServicesMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.1.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This MBean represents configuration for SAML 1.1-based Federation Services, including the intersite transfer service, assertion consumer service, and assertion retrieval service.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.FederationServicesMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("AssertionConsumerURIs")) {
         var3 = "getAssertionConsumerURIs";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAssertionConsumerURIs";
         }

         var2 = new PropertyDescriptor("AssertionConsumerURIs", FederationServicesMBean.class, var3, var4);
         var1.put("AssertionConsumerURIs", var2);
         var2.setValue("description", "<p>The Assertion Consumer URIs.</p> ");
         setPropertyDescriptorDefault(var2, BeanInfoHelper.stringArray("/samlacs/acs"));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("AssertionRetrievalURIs")) {
         var3 = "getAssertionRetrievalURIs";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAssertionRetrievalURIs";
         }

         var2 = new PropertyDescriptor("AssertionRetrievalURIs", FederationServicesMBean.class, var3, var4);
         var1.put("AssertionRetrievalURIs", var2);
         var2.setValue("description", "<p>One or more URIs on which to listen for incoming assertion retrieval requests.</p>  <p>For artifact profile, the destination site receives a SAML artifact that represents a source site (why we need the source site ID values) and an assertion ID. The destination site sends a request containing the artifact to the source site's assertion retrieval URL, and the source site responds with the corresponding assertion. You may configure multiple URIs here, although typically one will be sufficient. The URI includes the application context, followed by the resource context. For example:</p>  <p><code> /my_application/saml/ars</code></p>  <p>which would be accessible from the outside as <code>https://my.example.com/my_application/saml/ars</code></p> ");
         setPropertyDescriptorDefault(var2, BeanInfoHelper.stringArray("/samlars/ars"));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("AssertionStoreClassName")) {
         var3 = "getAssertionStoreClassName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAssertionStoreClassName";
         }

         var2 = new PropertyDescriptor("AssertionStoreClassName", FederationServicesMBean.class, var3, var4);
         var1.put("AssertionStoreClassName", var2);
         var2.setValue("description", "<p>The class that provides persistent storage for assertions, if you use an Assertion Store class other than the default class.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("AssertionStoreProperties")) {
         var3 = "getAssertionStoreProperties";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAssertionStoreProperties";
         }

         var2 = new PropertyDescriptor("AssertionStoreProperties", FederationServicesMBean.class, var3, var4);
         var1.put("AssertionStoreProperties", var2);
         var2.setValue("description", "<p>Properties passed to Assertion Store class initStore() method.</p> <p>This may be useful if you have implemented a custom Assertion Store class.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("IntersiteTransferURIs")) {
         var3 = "getIntersiteTransferURIs";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setIntersiteTransferURIs";
         }

         var2 = new PropertyDescriptor("IntersiteTransferURIs", FederationServicesMBean.class, var3, var4);
         var1.put("IntersiteTransferURIs", var2);
         var2.setValue("description", "<p>The Intersite Transfer URIs.</p> ");
         var2.setValue("default", new String[]{"/samlits_ba/its", "/samlits_ba/its/post", "/samlits_ba/its/artifact", "/samlits_cc/its", "/samlits_cc/its/post", "/samlits_cc/its/artifact"});
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", FederationServicesMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("SSLClientIdentityAlias")) {
         var3 = "getSSLClientIdentityAlias";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSSLClientIdentityAlias";
         }

         var2 = new PropertyDescriptor("SSLClientIdentityAlias", FederationServicesMBean.class, var3, var4);
         var1.put("SSLClientIdentityAlias", var2);
         var2.setValue("description", "<p>The alias used to store and retrieve the Destination Site's SSL client identity in the keystore.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SSLClientIdentityPassPhrase")) {
         var3 = "getSSLClientIdentityPassPhrase";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSSLClientIdentityPassPhrase";
         }

         var2 = new PropertyDescriptor("SSLClientIdentityPassPhrase", FederationServicesMBean.class, var3, var4);
         var1.put("SSLClientIdentityPassPhrase", var2);
         var2.setValue("description", "<p>The passphrase used to retrieve the Destination Site's SSL client identity from the keystore.</p> ");
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("SSLClientIdentityPassPhraseEncrypted")) {
         var3 = "getSSLClientIdentityPassPhraseEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSSLClientIdentityPassPhraseEncrypted";
         }

         var2 = new PropertyDescriptor("SSLClientIdentityPassPhraseEncrypted", FederationServicesMBean.class, var3, var4);
         var1.put("SSLClientIdentityPassPhraseEncrypted", var2);
         var2.setValue("description", "<p>The encrypted passphrase used to retrieve the Destination Site's SSL client identity from the keystore.</p>  <p>To set this attribute, use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the encrypt() method.</p>  <p>To compare a password that a user enters with the encrypted value of this attribute, go to the same WebLogic Server instance that you used to set and encrypt this attribute and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SigningKeyAlias")) {
         var3 = "getSigningKeyAlias";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSigningKeyAlias";
         }

         var2 = new PropertyDescriptor("SigningKeyAlias", FederationServicesMBean.class, var3, var4);
         var1.put("SigningKeyAlias", var2);
         var2.setValue("description", "<p>The alias used to store and retrieve the Source Site's signing key in the keystore. This key is used to sign POST profile responses.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SigningKeyPassPhrase")) {
         var3 = "getSigningKeyPassPhrase";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSigningKeyPassPhrase";
         }

         var2 = new PropertyDescriptor("SigningKeyPassPhrase", FederationServicesMBean.class, var3, var4);
         var1.put("SigningKeyPassPhrase", var2);
         var2.setValue("description", "<p>The passphrase used to retrieve the Source Site's signing key from the keystore.</p> ");
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("SigningKeyPassPhraseEncrypted")) {
         var3 = "getSigningKeyPassPhraseEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSigningKeyPassPhraseEncrypted";
         }

         var2 = new PropertyDescriptor("SigningKeyPassPhraseEncrypted", FederationServicesMBean.class, var3, var4);
         var1.put("SigningKeyPassPhraseEncrypted", var2);
         var2.setValue("description", "<p>The encrypted passphrase used to retrieve the Source Site's signing key from the keystore.</p>  <p>To set this attribute, use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the encrypt() method.</p>  <p>To compare a password that a user enters with the encrypted value of this attribute, go to the same WebLogic Server instance that you used to set and encrypt this attribute and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SourceIdBase64")) {
         var3 = "getSourceIdBase64";
         var4 = null;
         var2 = new PropertyDescriptor("SourceIdBase64", FederationServicesMBean.class, var3, var4);
         var1.put("SourceIdBase64", var2);
         var2.setValue("description", "<p>The Source Site ID base64-encoded.</p> <p>This read-only value is a Base64 representation of a 20-byte binary value that is calculated from the <code>SourceSiteURL</code>. If you want to configure ARTIFACT profile with another site, you will need to give a <code>SourceId</code> value to the other site. This value is automatically updated when the <code>SourceSiteURL</code> changes.</p> ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("SourceIdHex")) {
         var3 = "getSourceIdHex";
         var4 = null;
         var2 = new PropertyDescriptor("SourceIdHex", FederationServicesMBean.class, var3, var4);
         var1.put("SourceIdHex", var2);
         var2.setValue("description", "<p>The Source Site ID in hexadecimal.</p> <p>This read-only value is a hexadecimal representation of a 20-byte binary value that is calculated from the <code>SourceSiteURL</code>. If you want to configure ARTIFACT profile with another site, you will need to give a <code>SourceId</code> value to the other site. This value is automatically updated when the <code>SourceSiteURL</code> changes.</p> ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("SourceSiteURL")) {
         var3 = "getSourceSiteURL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSourceSiteURL";
         }

         var2 = new PropertyDescriptor("SourceSiteURL", FederationServicesMBean.class, var3, var4);
         var1.put("SourceSiteURL", var2);
         var2.setValue("description", "<p>The URL for the Source Site.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("UsedAssertionCacheClassName")) {
         var3 = "getUsedAssertionCacheClassName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUsedAssertionCacheClassName";
         }

         var2 = new PropertyDescriptor("UsedAssertionCacheClassName", FederationServicesMBean.class, var3, var4);
         var1.put("UsedAssertionCacheClassName", var2);
         var2.setValue("description", "<p>The class used as the persistent store for the Used Assertion Cache. When no class is specified, the default Used Assertion Cache implementation is used.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("UsedAssertionCacheProperties")) {
         var3 = "getUsedAssertionCacheProperties";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUsedAssertionCacheProperties";
         }

         var2 = new PropertyDescriptor("UsedAssertionCacheProperties", FederationServicesMBean.class, var3, var4);
         var1.put("UsedAssertionCacheProperties", var2);
         var2.setValue("description", "<p>Properties to be passed to the Used Assertion Cache class.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ACSRequiresSSL")) {
         var3 = "isACSRequiresSSL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setACSRequiresSSL";
         }

         var2 = new PropertyDescriptor("ACSRequiresSSL", FederationServicesMBean.class, var3, var4);
         var1.put("ACSRequiresSSL", var2);
         var2.setValue("description", "<p>Specifies whether the Assertion Consumer Service requires SSL.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ARSRequiresSSL")) {
         var3 = "isARSRequiresSSL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setARSRequiresSSL";
         }

         var2 = new PropertyDescriptor("ARSRequiresSSL", FederationServicesMBean.class, var3, var4);
         var1.put("ARSRequiresSSL", var2);
         var2.setValue("description", "<p>Specifies whether the Assertion Retrieval Service requires SSL.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ARSRequiresTwoWaySSL")) {
         var3 = "isARSRequiresTwoWaySSL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setARSRequiresTwoWaySSL";
         }

         var2 = new PropertyDescriptor("ARSRequiresTwoWaySSL", FederationServicesMBean.class, var3, var4);
         var1.put("ARSRequiresTwoWaySSL", var2);
         var2.setValue("description", "<p>Specifies whether the Assertion Retrieval Service requires two-way SSL authentication.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DestinationSiteEnabled")) {
         var3 = "isDestinationSiteEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDestinationSiteEnabled";
         }

         var2 = new PropertyDescriptor("DestinationSiteEnabled", FederationServicesMBean.class, var3, var4);
         var1.put("DestinationSiteEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the Destination Site is enabled.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ITSRequiresSSL")) {
         var3 = "isITSRequiresSSL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setITSRequiresSSL";
         }

         var2 = new PropertyDescriptor("ITSRequiresSSL", FederationServicesMBean.class, var3, var4);
         var1.put("ITSRequiresSSL", var2);
         var2.setValue("description", "<p>Specifies whether the Intersite Transfer Service requires SSL.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("POSTOneUseCheckEnabled")) {
         var3 = "isPOSTOneUseCheckEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPOSTOneUseCheckEnabled";
         }

         var2 = new PropertyDescriptor("POSTOneUseCheckEnabled", FederationServicesMBean.class, var3, var4);
         var1.put("POSTOneUseCheckEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the POST one-use check is enabled.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("POSTRecipientCheckEnabled")) {
         var3 = "isPOSTRecipientCheckEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPOSTRecipientCheckEnabled";
         }

         var2 = new PropertyDescriptor("POSTRecipientCheckEnabled", FederationServicesMBean.class, var3, var4);
         var1.put("POSTRecipientCheckEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the POST recipient check is enabled. When true, the recipient of the SAML Response must match the URL in the HTTP Request.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("SourceSiteEnabled")) {
         var3 = "isSourceSiteEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSourceSiteEnabled";
         }

         var2 = new PropertyDescriptor("SourceSiteEnabled", FederationServicesMBean.class, var3, var4);
         var1.put("SourceSiteEnabled", var2);
         var2.setValue("description", "<p>Indicates whether the Source Site is enabled.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
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
      Method var3 = FederationServicesMBean.class.getMethod("freezeCurrentValue", String.class);
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

      var3 = FederationServicesMBean.class.getMethod("restoreDefaultValue", String.class);
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
