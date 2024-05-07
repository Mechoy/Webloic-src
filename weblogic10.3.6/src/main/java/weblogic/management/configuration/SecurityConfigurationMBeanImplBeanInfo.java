package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class SecurityConfigurationMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SecurityConfigurationMBean.class;

   public SecurityConfigurationMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SecurityConfigurationMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SecurityConfigurationMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "7.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("Provides domain-wide security configuration information.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.SecurityConfigurationMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("CertRevoc")) {
         var3 = "getCertRevoc";
         var4 = null;
         var2 = new PropertyDescriptor("CertRevoc", SecurityConfigurationMBean.class, var3, var4);
         var1.put("CertRevoc", var2);
         var2.setValue("description", "Determines the domain's X509 certificate revocation checking configuration. <p> A CertRevocMBean is always associated with a domain's security configuration and cannot be changed, although CertRevocMBean attributes may be changed as documented. ");
         var2.setValue("relationship", "containment");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("CompatibilityConnectionFiltersEnabled")) {
         var3 = "getCompatibilityConnectionFiltersEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCompatibilityConnectionFiltersEnabled";
         }

         var2 = new PropertyDescriptor("CompatibilityConnectionFiltersEnabled", SecurityConfigurationMBean.class, var3, var4);
         var1.put("CompatibilityConnectionFiltersEnabled", var2);
         var2.setValue("description", "<p>Specifies whether this WebLogic Server domain enables compatiblity with previous connection filters.</p>  <p>This attribute changes the protocols names used when filtering needs to be performed.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("ConnectionFilter")) {
         var3 = "getConnectionFilter";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnectionFilter";
         }

         var2 = new PropertyDescriptor("ConnectionFilter", SecurityConfigurationMBean.class, var3, var4);
         var1.put("ConnectionFilter", var2);
         var2.setValue("description", "<p>The name of the Java class that implements a connection filter (that is, the <tt>weblogic.security.net.ConnectionFilter</tt> interface). If no class name is specified, no connection filter will be used.</p>  <p> This attribute replaces the deprecated ConnectionFilter attribute on the SecurityMBean.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("ConnectionFilterRules")) {
         var3 = "getConnectionFilterRules";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnectionFilterRules";
         }

         var2 = new PropertyDescriptor("ConnectionFilterRules", SecurityConfigurationMBean.class, var3, var4);
         var1.put("ConnectionFilterRules", var2);
         var2.setValue("description", "<p>The rules used by any connection filter that implements the <tt>ConnectionFilterRulesListener</tt> interface. When using the default implementation and when no rules are specified, all connections are accepted. The default implementation rules are in the format: <tt>target localAddress localPort action protocols</tt>.  <p> This attribute replaces the deprecated ConnectionFilterRules attribute on the SecurityMBean. ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("ConnectionLoggerEnabled")) {
         var3 = "getConnectionLoggerEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnectionLoggerEnabled";
         }

         var2 = new PropertyDescriptor("ConnectionLoggerEnabled", SecurityConfigurationMBean.class, var3, var4);
         var1.put("ConnectionLoggerEnabled", var2);
         var2.setValue("description", "<p>Specifies whether this WebLogic Server domain should log accepted connections.</p>  <p>This attribute can be used by a system administrator to dynamically check the incoming connections in the log file to determine if filtering needs to be performed.</p>  <p> This attribute replaces the deprecated ConnectionLoggerEnabled attribute on the SecurityMBean.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      String[] var5;
      if (!var1.containsKey("Credential")) {
         var3 = "getCredential";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCredential";
         }

         var2 = new PropertyDescriptor("Credential", SecurityConfigurationMBean.class, var3, var4);
         var1.put("Credential", var2);
         var2.setValue("description", "<p>The password for the domain. In WebLogic Server version 6.0, this attribute was the password of the system user. In WebLogic Server version 7.0, this attribute can be any string. For the two domains to interoperate, the string must be the same for both domains.</p>  <p>When you set the value of this attribute, WebLogic Server does the following:</p> <ol><li>Encrypts the value.</li> <li>Sets the value of the <code>UserPasswordEncrypted</code> attribute to the encrypted value.</li> </ol> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getCredentialEncrypted()")};
         var2.setValue("see", var5);
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("CredentialEncrypted")) {
         var3 = "getCredentialEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCredentialEncrypted";
         }

         var2 = new PropertyDescriptor("CredentialEncrypted", SecurityConfigurationMBean.class, var3, var4);
         var1.put("CredentialEncrypted", var2);
         var2.setValue("description", "<p>The encrypted password for the domain. In WebLogic Server version 6.0, this attribute was the password of the system user. In WebLogic Server version 7.0, this attribute can be any string. For the two domains to interoperate, the string must be the same for both domains.</p>  <p>To set this attribute, pass an unencrypted string to the MBean server's <code>setAttribute</code> method. WebLogic Server encrypts the value and sets the attribute to the encrypted value.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DefaultRealm")) {
         var3 = "getDefaultRealm";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultRealm";
         }

         var2 = new PropertyDescriptor("DefaultRealm", SecurityConfigurationMBean.class, var3, var4);
         var1.put("DefaultRealm", var2);
         var2.setValue("description", "Returns the default security realm or null if no realm has been selected as the default security realm. ");
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("DowngradeUntrustedPrincipals")) {
         var3 = "getDowngradeUntrustedPrincipals";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDowngradeUntrustedPrincipals";
         }

         var2 = new PropertyDescriptor("DowngradeUntrustedPrincipals", SecurityConfigurationMBean.class, var3, var4);
         var1.put("DowngradeUntrustedPrincipals", var2);
         var2.setValue("description", "Whether or not to downgrade to anonymous principals that cannot be verified. This is useful for server-server communication between untrusted domains. ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("EnforceStrictURLPattern")) {
         var3 = "getEnforceStrictURLPattern";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEnforceStrictURLPattern";
         }

         var2 = new PropertyDescriptor("EnforceStrictURLPattern", SecurityConfigurationMBean.class, var3, var4);
         var1.put("EnforceStrictURLPattern", var2);
         var2.setValue("description", "Whether or not the system should enforce strict URL pattern or not. ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.2", (String)null, this.targetVersion) && !var1.containsKey("EnforceValidBasicAuthCredentials")) {
         var3 = "getEnforceValidBasicAuthCredentials";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEnforceValidBasicAuthCredentials";
         }

         var2 = new PropertyDescriptor("EnforceValidBasicAuthCredentials", SecurityConfigurationMBean.class, var3, var4);
         var1.put("EnforceValidBasicAuthCredentials", var2);
         var2.setValue("description", "Whether or not the system should allow requests with invalid Basic Authentication credentials to access unsecure resources. ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("since", "9.2");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0", (String)null, this.targetVersion) && !var1.containsKey("ExcludedDomainNames")) {
         var3 = "getExcludedDomainNames";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setExcludedDomainNames";
         }

         var2 = new PropertyDescriptor("ExcludedDomainNames", SecurityConfigurationMBean.class, var3, var4);
         var1.put("ExcludedDomainNames", var2);
         var2.setValue("description", "<p> Specifies a list of remote domains for which cross-domain check should not be applied. ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "10.0");
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", SecurityConfigurationMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("NodeManagerPassword")) {
         var3 = "getNodeManagerPassword";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNodeManagerPassword";
         }

         var2 = new PropertyDescriptor("NodeManagerPassword", SecurityConfigurationMBean.class, var3, var4);
         var1.put("NodeManagerPassword", var2);
         var2.setValue("description", "<p>The password that the Administration Server uses to communicate with Node Manager when starting, stopping, or restarting Managed Servers. </p>  <p>When you get the value of this attribute, WebLogic Server does the following:</p> <ol><li>Retrieves the value of the <code>NodeManagerPasswordEncrypted</code> attribute. <li>Decrypts the value and returns the unencrypted password as a String. </ol>  <p>When you set the value of this attribute, WebLogic Server does the following:</p> <ol><li>Encrypts the value.</li> <li>Sets the value of the <code>NodeManagerPasswordEncrypted</code> attribute to the encrypted value.</li> </ol> <p>Using this attribute (<code>NodeManagerPassword</code>) is a potential security risk because the String object (which contains the unencrypted password) remains in the JVM's memory until garbage collection removes it and the memory is reallocated. Depending on how memory is allocated in the JVM, a significant amount of time could pass before this unencrypted data is removed from memory.</p>  <p>Instead of using this attribute, you should use <code>NodeManagerPasswordEncrypted</code>.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getNodeManagerPasswordEncrypted()")};
         var2.setValue("see", var5);
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("NodeManagerPasswordEncrypted")) {
         var3 = "getNodeManagerPasswordEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNodeManagerPasswordEncrypted";
         }

         var2 = new PropertyDescriptor("NodeManagerPasswordEncrypted", SecurityConfigurationMBean.class, var3, var4);
         var1.put("NodeManagerPasswordEncrypted", var2);
         var2.setValue("description", "<p>The password that the Administration Server passes to a Node Manager when it instructs the Node Manager to start, stop, or restart Managed Servers. </p>  <p>To set this attribute, use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the value. Then set this attribute to the output of the encrypt() method.</p>  <p>To compare a password that a user enters with the encrypted value of this attribute, go to the same WebLogic Server instance that you used to set and encrypt this attribute and use <code>weblogic.management.EncryptionHelper.encrypt()</code> to encrypt the user-supplied password. Then compare the encrypted values.</p> ");
         setPropertyDescriptorDefault(var2, "".getBytes());
         var2.setValue("encrypted", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("NodeManagerUsername")) {
         var3 = "getNodeManagerUsername";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNodeManagerUsername";
         }

         var2 = new PropertyDescriptor("NodeManagerUsername", SecurityConfigurationMBean.class, var3, var4);
         var1.put("NodeManagerUsername", var2);
         var2.setValue("description", "<p>The user name that the Administration Server uses to communicate with Node Manager when starting, stopping, or restarting Managed Servers. </p> ");
         setPropertyDescriptorDefault(var2, "");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("RealmBootStrapVersion")) {
         var3 = "getRealmBootStrapVersion";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRealmBootStrapVersion";
         }

         var2 = new PropertyDescriptor("RealmBootStrapVersion", SecurityConfigurationMBean.class, var3, var4);
         var1.put("RealmBootStrapVersion", var2);
         var2.setValue("description", "<p>Indicates which version of the default security realm MBeans should be loaded if none exist. The value is set to current version on initial read if it has not already been set.</p> ");
         var2.setValue("legalValues", new Object[]{"unknown", "1"});
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("Realms")) {
         var3 = "getRealms";
         var4 = null;
         var2 = new PropertyDescriptor("Realms", SecurityConfigurationMBean.class, var3, var4);
         var1.put("Realms", var2);
         var2.setValue("description", "Returns all the realms in the domain. ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyRealm");
         var2.setValue("creator", "createRealm");
         var2.setValue("creator", "createRealm");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("WebAppFilesCaseInsensitive")) {
         var3 = "getWebAppFilesCaseInsensitive";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWebAppFilesCaseInsensitive";
         }

         var2 = new PropertyDescriptor("WebAppFilesCaseInsensitive", SecurityConfigurationMBean.class, var3, var4);
         var1.put("WebAppFilesCaseInsensitive", var2);
         var2.setValue("description", "<p>This property defines the case sensitive URL-pattern matching behavior for security constraints, servlets, filters, virtual-hosts, and so on, in the Web application container and external security policies.  <b>Note:</b> This is a Windows-only flag that is provided for backward compatibility when upgrading from pre-9.0 versions of WebLogic Server. On Unix platforms, setting this value to <code>true</code> causes undesired behavior and is not supported.  When the value is set to <code>os</code>, the pattern matching will be case- sensitive on all platforms except the Windows file system.  Note that on non-Windows file systems, WebLogic Server does not enforce case sensitivity and relies on the file system for optimization. As a result, if you have a Windows Samba mount from Unix or Mac OS that has been installed in case-insensitive mode, there is a chance of a security risk. If so, specify case-insensitive lookups by setting this attribute to <code>true</code>.  Note also that this property is used to preserve backward compatibility on Windows file systems only. In prior releases, WebLogic Server was case- insensitive on Windows. As of WebLogic Server 9.0, URL-pattern matching is strictly enforced.  During the upgrade of older domains, the value of this parameter is explicitly set to <code>os</code> by the upgrade plug-in to preserve backward compatibility.</p> ");
         setPropertyDescriptorDefault(var2, "false");
         var2.setValue("legalValues", new Object[]{"os", "true", "false"});
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("AnonymousAdminLookupEnabled")) {
         var3 = "isAnonymousAdminLookupEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAnonymousAdminLookupEnabled";
         }

         var2 = new PropertyDescriptor("AnonymousAdminLookupEnabled", SecurityConfigurationMBean.class, var3, var4);
         var1.put("AnonymousAdminLookupEnabled", var2);
         var2.setValue("description", "<p>Returns true if anonymous JNDI access for Admin MBean home is permitted. This is overridden by the Java property <code>-Dweblogic.management.anonymousAdminLookupEnabled</code>.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ClearTextCredentialAccessEnabled")) {
         var3 = "isClearTextCredentialAccessEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setClearTextCredentialAccessEnabled";
         }

         var2 = new PropertyDescriptor("ClearTextCredentialAccessEnabled", SecurityConfigurationMBean.class, var3, var4);
         var1.put("ClearTextCredentialAccessEnabled", var2);
         var2.setValue("description", "<p>Returns true if allow access to credential in clear text. This can be overridden by the system property <code>-Dweblogic.management.clearTextCredentialAccessEnabled</code></p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.2.0.0", (String)null, this.targetVersion) && !var1.containsKey("ConsoleFullDelegationEnabled")) {
         var3 = "isConsoleFullDelegationEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConsoleFullDelegationEnabled";
         }

         var2 = new PropertyDescriptor("ConsoleFullDelegationEnabled", SecurityConfigurationMBean.class, var3, var4);
         var1.put("ConsoleFullDelegationEnabled", var2);
         var2.setValue("description", "<p>Indicates whether the console is enabled for fully delegate authorization.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("since", "9.2.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, (String)null, this.targetVersion) && !var1.containsKey("CredentialGenerated")) {
         var3 = "isCredentialGenerated";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCredentialGenerated";
         }

         var2 = new PropertyDescriptor("CredentialGenerated", SecurityConfigurationMBean.class, var3, var4);
         var1.put("CredentialGenerated", var2);
         var2.setValue("description", "DO NOT USE THIS METHOD..... This method is only here for backward compatibility with old config.xml files which have been persisted and now contain it. ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("obsolete", "true");
      }

      if (!var1.containsKey("CrossDomainSecurityEnabled")) {
         var3 = "isCrossDomainSecurityEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCrossDomainSecurityEnabled";
         }

         var2 = new PropertyDescriptor("CrossDomainSecurityEnabled", SecurityConfigurationMBean.class, var3, var4);
         var1.put("CrossDomainSecurityEnabled", var2);
         var2.setValue("description", "<p> Indicates whether or not cross-domain security is enabled ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("PrincipalEqualsCaseInsensitive")) {
         var3 = "isPrincipalEqualsCaseInsensitive";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPrincipalEqualsCaseInsensitive";
         }

         var2 = new PropertyDescriptor("PrincipalEqualsCaseInsensitive", SecurityConfigurationMBean.class, var3, var4);
         var1.put("PrincipalEqualsCaseInsensitive", var2);
         var2.setValue("description", "<p>Specifies whether the WebLogic Server principal name is compared using a case insensitive match when the equals method for the principal object is performed.</p>  <p>If this attribute is enabled, matches are case insensitive.</p>  <p><b>Note:</b> Note that principal comparison is not used by the WebLogic Security Service to determine access to protected resources. This attribute is intended for use with JAAS authorization, which may require case insensitive principal matching behavior.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("PrincipalEqualsCompareDnAndGuid")) {
         var3 = "isPrincipalEqualsCompareDnAndGuid";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPrincipalEqualsCompareDnAndGuid";
         }

         var2 = new PropertyDescriptor("PrincipalEqualsCompareDnAndGuid", SecurityConfigurationMBean.class, var3, var4);
         var1.put("PrincipalEqualsCompareDnAndGuid", var2);
         var2.setValue("description", "<p>Specifies whether the GUID and DN data in a WebLogic Server principal object are used when the equals method of that object is invoked. </p>  <p>If enabled, the GUID and DN data (if included among the attributes in a WebLogic Server principal object) and the principal name are compared when this method is invoked.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = SecurityConfigurationMBean.class.getMethod("createRealm", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "- The name of this realm, for example, <code>myrealm</code> ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Creates a realm. ");
         var2.setValue("role", "factory");
         var2.setValue("property", "Realms");
      }

      var3 = SecurityConfigurationMBean.class.getMethod("createRealm");
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "a String containing the realm's name.  This name must be unique among all realms in the domain.  If the name can be converted to a JMX object name, then it is used as the provider's JMX object name.  The encouraged convention is: &quot;Security:Name&#61;realmDisplayName&quot;.  For example: &quot;Security:Name&#61;myrealm&quot;. "), createParameterDescriptor("displayName", "a String containing the realm's display name (ie. the name that will be displayed in the console). ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Creates a realm. ");
         var2.setValue("role", "factory");
         var2.setValue("property", "Realms");
      }

      var3 = SecurityConfigurationMBean.class.getMethod("destroyRealm", weblogic.management.security.RealmMBean.class);
      String var6 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var6)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var6, var2);
         var2.setValue("description", "Destroys a realm.  This does not destroy its providers or its user lockout manager. ");
         var2.setValue("role", "factory");
         var2.setValue("property", "Realms");
      }

   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = SecurityConfigurationMBean.class.getMethod("lookupRealm", String.class);
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         MethodDescriptor var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "Finds a realm given it's name.  The name is often its JMX object name (e.g. Security:Name=myrealm) ");
         var2.setValue("role", "finder");
         var2.setValue("property", "Realms");
      }

   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = SecurityConfigurationMBean.class.getMethod("findRealms");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "9.0.0.0  Replaced by {@link #getRealms} ");
         var1.put(var4, var2);
         var2.setValue("description", "Returns all the realms in the domain. ");
         var2.setValue("role", "operation");
         var2.setValue("rolePermitAll", Boolean.TRUE);
      }

      var3 = SecurityConfigurationMBean.class.getMethod("findDefaultRealm");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "9.0.0.0  Replaced by {@link #getDefaultRealm} ");
         var1.put(var4, var2);
         var2.setValue("description", "Finds the default security realm. Returns null if a default security realm is not defined. ");
         var2.setValue("role", "operation");
         var2.setValue("rolePermitAll", Boolean.TRUE);
      }

      var3 = SecurityConfigurationMBean.class.getMethod("findRealm", String.class);
      ParameterDescriptor[] var6 = new ParameterDescriptor[]{createParameterDescriptor("realmDisplayName", "A String containing the realm's display name. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var2.setValue("deprecated", "9.0.0.0  Replaced by {@link #lookupRealm} ");
         var1.put(var5, var2);
         var2.setValue("description", "Finds a realm by name (that is, by the display name of the realm). Returns null no realm with that name has been defined. Throws a configuration error if there are multiple matches. ");
         var2.setValue("role", "operation");
         var2.setValue("rolePermitAll", Boolean.TRUE);
      }

      var3 = SecurityConfigurationMBean.class.getMethod("pre90getProviders");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = SecurityConfigurationMBean.class.getMethod("freezeCurrentValue", String.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has not been set explicitly, and if the attribute has a default value, this operation forces the MBean to persist the default value.</p>  <p>Unless you use this operation, the default value is not saved and is subject to change if you update to a newer release of WebLogic Server. Invoking this operation isolates this MBean from the effects of such changes.</p>  <dl> <dt>Note:</dt>  <dd> <p>To insure that you are freezing the default value, invoke the <code>restoreDefaultValue</code> operation before you invoke this.</p> </dd> </dl>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute for which some other value has been set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = SecurityConfigurationMBean.class.getMethod("restoreDefaultValue", String.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5) && !this.readOnly) {
         var2 = new MethodDescriptor(var3, var6);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has a default value, this operation removes any value that has been set explicitly and causes the attribute to use the default value.</p>  <p>Default values are subject to change if you update to a newer release of WebLogic Server. To prevent the value from changing if you update to a newer release, invoke the <code>freezeCurrentValue</code> operation.</p>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute that is already using the default.</p> ");
         var2.setValue("role", "operation");
         var2.setValue("impact", "action");
      }

      var3 = SecurityConfigurationMBean.class.getMethod("generateCredential");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Generates a new encrypted byte array which can be use when calling #setCredentialEncrypted</p> ");
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
