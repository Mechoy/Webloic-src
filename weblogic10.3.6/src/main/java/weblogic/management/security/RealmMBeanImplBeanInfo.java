package weblogic.management.security;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.commo.AbstractCommoConfigurationBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.security.audit.AuditorMBean;
import weblogic.management.security.authentication.AuthenticationProviderMBean;
import weblogic.management.security.authentication.PasswordValidatorMBean;
import weblogic.management.security.authorization.AuthorizerMBean;
import weblogic.management.security.authorization.RoleMapperMBean;
import weblogic.management.security.credentials.CredentialMapperMBean;
import weblogic.management.security.pk.CertPathProviderMBean;
import weblogic.management.security.pk.KeyStoreMBean;

public class RealmMBeanImplBeanInfo extends AbstractCommoConfigurationBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = RealmMBean.class;

   public RealmMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public RealmMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = RealmMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.security");
      String var3 = (new String("The MBean that represents configuration attributes for the security realm. <p> A security realm contains a set of security configuration settings, including the list of security providers to use (for example, for authentication and authorization). <p> Code using security can either use the default security realm for the domain or refer to a particular security realm by name (by using the JMX display name of the security realm). <p> One security realm in the WebLogic domain must have the <code>DefaultRealm</code> attribute set to true. The security realm with the <code>DefaultRealm</code> attribute set to true is used as the default security realm for the WebLogic domain. Note that other available security realms must have the <code>DefaultRealm</code> attribute set to false. <p> When WebLogic Server boots, it locates and uses the default security realm. The security realm is considered active since it is used when WebLogic Server runs. Any security realm that is not used when WebLogic Server runs is considered inactive. All active security realms must be configured before WebLogic Server is boots. <p> Since security providers are scoped by realm, the <code>Realm</code> attribute on a security provider must be set to the realm that uses the provider.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">In addition to being used as a base class that provides functionality to security provider MBeans, JMX applications can use this class directly as a type-safe interface. When used as a type-safe interface, a JMX application imports this class and accesses it through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, JMX applications that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.security.RealmMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("Adjudicator")) {
         var3 = "getAdjudicator";
         var4 = null;
         var2 = new PropertyDescriptor("Adjudicator", RealmMBean.class, var3, var4);
         var1.put("Adjudicator", var2);
         var2.setValue("description", "Returns the Adjudication provider for this security realm. ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createAdjudicator");
         var2.setValue("creator", "createAdjudicator");
         var2.setValue("destroyer", "destroyAdjudicator");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("AdjudicatorTypes")) {
         var3 = "getAdjudicatorTypes";
         var4 = null;
         var2 = new PropertyDescriptor("AdjudicatorTypes", RealmMBean.class, var3, var4);
         var1.put("AdjudicatorTypes", var2);
         var2.setValue("description", "Returns the types of Adjudication providers that may be created in this security realm, for example, <code>weblogic.security.providers.authorization.DefaultAdjudicator</code>. Use this method to find the available types to pass to <code>createAdjudicator</code> ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("AuditorTypes")) {
         var3 = "getAuditorTypes";
         var4 = null;
         var2 = new PropertyDescriptor("AuditorTypes", RealmMBean.class, var3, var4);
         var1.put("AuditorTypes", var2);
         var2.setValue("description", "Returns the types of Auditing providers that may be created in this security realm, for example, <code>weblogic.security.providers.audit.DefaultAuditor</code>. Use this method to find the available types to pass to <code>createAuditor</code> ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Auditors")) {
         var3 = "getAuditors";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAuditors";
         }

         var2 = new PropertyDescriptor("Auditors", RealmMBean.class, var3, var4);
         var1.put("Auditors", var2);
         var2.setValue("description", "Returns the Auditing providers for this security realm (in invocation order). ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createAuditor");
         var2.setValue("creator", "createAuditor");
         var2.setValue("destroyer", "destroyAuditor");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.2.0.0", (String)null, this.targetVersion) && !var1.containsKey("AuthMethods")) {
         var3 = "getAuthMethods";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAuthMethods";
         }

         var2 = new PropertyDescriptor("AuthMethods", RealmMBean.class, var3, var4);
         var1.put("AuthMethods", var2);
         var2.setValue("description", "Returns a comma separated string of authentication methods that should be used when the Web application specifies \"REALM\" as its auth-method. The authentication methods will be applied in order in which they appear in the list. ");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("since", "9.2.0.0");
      }

      if (!var1.containsKey("AuthenticationProviderTypes")) {
         var3 = "getAuthenticationProviderTypes";
         var4 = null;
         var2 = new PropertyDescriptor("AuthenticationProviderTypes", RealmMBean.class, var3, var4);
         var1.put("AuthenticationProviderTypes", var2);
         var2.setValue("description", "Returns the types of Authentication providers that may be created in this security realm, for example, <code>weblogic.security.providers.authentication.DefaultAuthenticator</code>. Use this method to find the available types to pass to <code>createAuthenticationProvider</code> ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("AuthenticationProviders")) {
         var3 = "getAuthenticationProviders";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAuthenticationProviders";
         }

         var2 = new PropertyDescriptor("AuthenticationProviders", RealmMBean.class, var3, var4);
         var1.put("AuthenticationProviders", var2);
         var2.setValue("description", "Returns the Authentication providers for this security realm (in invocation order). ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createAuthenticationProvider");
         var2.setValue("creator", "createAuthenticationProvider");
         var2.setValue("destroyer", "destroyAuthenticationProvider");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("AuthorizerTypes")) {
         var3 = "getAuthorizerTypes";
         var4 = null;
         var2 = new PropertyDescriptor("AuthorizerTypes", RealmMBean.class, var3, var4);
         var1.put("AuthorizerTypes", var2);
         var2.setValue("description", "Returns the types of Authorization providers that may be created in this security realm, for example, <code>weblogic.security.providers.authorization.DefaultAuthorizer</code>. Use this method to find the available types to pass to <code>createAuthorizer</code> ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Authorizers")) {
         var3 = "getAuthorizers";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAuthorizers";
         }

         var2 = new PropertyDescriptor("Authorizers", RealmMBean.class, var3, var4);
         var1.put("Authorizers", var2);
         var2.setValue("description", "Returns the Authorization providers for this security realm (in invocation order). ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyAuthorizer");
         var2.setValue("creator", "createAuthorizer");
         var2.setValue("creator", "createAuthorizer");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("CertPathBuilder")) {
         var3 = "getCertPathBuilder";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCertPathBuilder";
         }

         var2 = new PropertyDescriptor("CertPathBuilder", RealmMBean.class, var3, var4);
         var1.put("CertPathBuilder", var2);
         var2.setValue("description", "Returns the CertPath Builder provider in this security realm that will be used by the security system to build certification paths.  Returns null if none has been selected.  The provider will be one of this security realm's <code>CertPathProviders</code>. ");
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("CertPathProviderTypes")) {
         var3 = "getCertPathProviderTypes";
         var4 = null;
         var2 = new PropertyDescriptor("CertPathProviderTypes", RealmMBean.class, var3, var4);
         var1.put("CertPathProviderTypes", var2);
         var2.setValue("description", "Returns the types of Certification Path providers that may be created in this security realm, for example, <code>weblogic.security.providers.pk.WebLogicCertPathProvider</code>. Use this method to find the available types to pass to <code>createCertPathProvider</code> ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("CertPathProviders")) {
         var3 = "getCertPathProviders";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCertPathProviders";
         }

         var2 = new PropertyDescriptor("CertPathProviders", RealmMBean.class, var3, var4);
         var1.put("CertPathProviders", var2);
         var2.setValue("description", "Returns the Certification Path providers for this security realm (in invocation order). ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyCertPathProvider");
         var2.setValue("creator", "createCertPathProvider");
         var2.setValue("creator", "createCertPathProvider");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("CredentialMapperTypes")) {
         var3 = "getCredentialMapperTypes";
         var4 = null;
         var2 = new PropertyDescriptor("CredentialMapperTypes", RealmMBean.class, var3, var4);
         var1.put("CredentialMapperTypes", var2);
         var2.setValue("description", "Returns the types of Credential Mapping providers that may be created in this security realm, for example, <code>weblogic.security.providers.credentials.DefaultCredentialMapper</code>. Use this method to find the available types to pass to <code>createCredentialMapper</code> ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("CredentialMappers")) {
         var3 = "getCredentialMappers";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCredentialMappers";
         }

         var2 = new PropertyDescriptor("CredentialMappers", RealmMBean.class, var3, var4);
         var1.put("CredentialMappers", var2);
         var2.setValue("description", "Returns the Credential Mapping providers for this security realm (in invocation order). ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyCredentialMapper");
         var2.setValue("creator", "createCredentialMapper");
         var2.setValue("creator", "createCredentialMapper");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (BeanInfoHelper.isVersionCompliant("10.3", (String)null, this.targetVersion) && !var1.containsKey("DeployableProviderSynchronizationTimeout")) {
         var3 = "getDeployableProviderSynchronizationTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDeployableProviderSynchronizationTimeout";
         }

         var2 = new PropertyDescriptor("DeployableProviderSynchronizationTimeout", RealmMBean.class, var3, var4);
         var1.put("DeployableProviderSynchronizationTimeout", var2);
         var2.setValue("description", "Returns the timeout value, in milliseconds, for the deployable security provider synchronization operation. This value is only used if <code>DeployableProviderSynchronizationEnabled</code> is set to <code>true</code> ");
         setPropertyDescriptorDefault(var2, new Integer(60000));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("since", "10.3");
      }

      if (!var1.containsKey("KeyStoreTypes")) {
         var3 = "getKeyStoreTypes";
         var4 = null;
         var2 = new PropertyDescriptor("KeyStoreTypes", RealmMBean.class, var3, var4);
         var1.put("KeyStoreTypes", var2);
         var2.setValue("description", "Returns the types of KeyStore providers that may be created in this security realm, for example, <code>weblogic.security.providers.pk.DefaultKeyStore</code>. Use this method to find the available types to pass to <code>createKeyStore</code> ");
         var2.setValue("deprecated", "8.1.0.0 ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("KeyStores")) {
         var3 = "getKeyStores";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setKeyStores";
         }

         var2 = new PropertyDescriptor("KeyStores", RealmMBean.class, var3, var4);
         var1.put("KeyStores", var2);
         var2.setValue("description", "Returns the KeyStore providers for this security realm (in invocation order). ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyKeyStore");
         var2.setValue("creator", "createKeyStore");
         var2.setValue("creator", "createKeyStore");
         var2.setValue("deprecated", "8.1.0.0 ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("MaxWebLogicPrincipalsInCache")) {
         var3 = "getMaxWebLogicPrincipalsInCache";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxWebLogicPrincipalsInCache";
         }

         var2 = new PropertyDescriptor("MaxWebLogicPrincipalsInCache", RealmMBean.class, var3, var4);
         var1.put("MaxWebLogicPrincipalsInCache", var2);
         var2.setValue("description", "Returns the maximum size of the LRU cache for holding WebLogic Principal signatures. This value is only used if <code>EnableWebLogicPrincipalValidatorCache</code> is set to <code>true</code> ");
         setPropertyDescriptorDefault(var2, new Integer(500));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         var2 = new PropertyDescriptor("Name", RealmMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "The name of this configuration. WebLogic Server uses an MBean to implement and persist the configuration. ");
         setPropertyDescriptorDefault(var2, "Realm");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("legal", "");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0", (String)null, this.targetVersion) && !var1.containsKey("PasswordValidatorTypes")) {
         var3 = "getPasswordValidatorTypes";
         var4 = null;
         var2 = new PropertyDescriptor("PasswordValidatorTypes", RealmMBean.class, var3, var4);
         var1.put("PasswordValidatorTypes", var2);
         var2.setValue("description", "Returns the types of Password Validator providers that may be created in this security realm, for example, <code>com.bea.security.providers.authentication.passwordvalidator.SystemPasswordValidator</code>. Use this method to find the available types to pass to <code>createPasswordValidator</code> ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("since", "10.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0", (String)null, this.targetVersion) && !var1.containsKey("PasswordValidators")) {
         var3 = "getPasswordValidators";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPasswordValidators";
         }

         var2 = new PropertyDescriptor("PasswordValidators", RealmMBean.class, var3, var4);
         var1.put("PasswordValidators", var2);
         var2.setValue("description", "Returns the Password Validator providers for this security realm (in invocation order). ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyPasswordValidator");
         var2.setValue("creator", "createPasswordValidator");
         var2.setValue("creator", "createPasswordValidator");
         var2.setValue("creator", "createPasswordValidator");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("since", "10.0");
      }

      String[] var5;
      if (!var1.containsKey("RDBMSSecurityStore")) {
         var3 = "getRDBMSSecurityStore";
         var4 = null;
         var2 = new PropertyDescriptor("RDBMSSecurityStore", RealmMBean.class, var3, var4);
         var1.put("RDBMSSecurityStore", var2);
         var2.setValue("description", "Returns RDBMSSecurityStoreMBean for this realm, which is a singleton MBean describing RDBMS security store configuration. ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#createRDBMSSecurityStore")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyRDBMSSecurityStore");
         var2.setValue("creator", "createRDBMSSecurityStore");
         var2.setValue("creator", "createRDBMSSecurityStore");
      }

      if (!var1.containsKey("RoleMapperTypes")) {
         var3 = "getRoleMapperTypes";
         var4 = null;
         var2 = new PropertyDescriptor("RoleMapperTypes", RealmMBean.class, var3, var4);
         var1.put("RoleMapperTypes", var2);
         var2.setValue("description", "Returns the types of Role Mapping providers that may be created in this security realm, for example, <code>weblogic.security.providers.authorization.DefaultRoleMapper</code>. Use this method to find the available types to pass to <code>createRoleMapper</code> ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("RoleMappers")) {
         var3 = "getRoleMappers";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRoleMappers";
         }

         var2 = new PropertyDescriptor("RoleMappers", RealmMBean.class, var3, var4);
         var1.put("RoleMappers", var2);
         var2.setValue("description", "Returns the Role Mapping providers for this security realm (in invocation order). ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createRoleMapper");
         var2.setValue("creator", "createRoleMapper");
         var2.setValue("destroyer", "destroyRoleMapper");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("SecurityDDModel")) {
         var3 = "getSecurityDDModel";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSecurityDDModel";
         }

         var2 = new PropertyDescriptor("SecurityDDModel", RealmMBean.class, var3, var4);
         var1.put("SecurityDDModel", var2);
         var2.setValue("description", "<p>Specifies the default security model for Web applications or EJBs that are secured by this security realm. You can override this default during deployment.</p> <dl><dt>Note:</dt> <dd>If you deploy a module by modifying the domain's <code>config.xml</code> file and restarting the server, and if you do not specify a security model value for the module in <code>config.xml</code>, the module is secured with the default value of the <code>AppDeploymentMBean SecurityDDModel</code>  attribute (see {@link weblogic.management.AppDeploymentMBean#SecurityDDModel AppDeploymentMBean SecurityDDModel}). </dd> <p>Choose one of these security models:</p>  <ul> <li><code>Deployment Descriptors Only (DDOnly)</code> <ul> <li>For EJBs and URL patterns, this model uses only the roles and policies in the J2EE deployment descriptors (DD); the Administration Console allows only read access for this data. With this model, EJBs and URL patterns are not protected by roles and policies of a broader scope (such as a policy scoped to an entire Web application). If an EJB or URL pattern is not protected by a role or policy in the DD, then it is unprotected: anyone can access it. </li>  <li>For application-scoped <i>roles</i> in an EAR, this model uses only the roles defined in the WebLogic Server DD; the Administration Console allows only read access for this data. If the WebLogic Server DD does not define roles, then there will be no such scoped roles defined for this EAR.</li>  <li>For all other types of resources, you can use the Administration Console to create roles or policies. For example, with this model, you can use the Administration Console to create application-scoped <i>policies</i> for an EAR.</li>   <li>Applies for the life of the deployment. If you want to use a different model, you must delete the deployment and reinstall it.</li> </ul></li>  <li><code>Customize Roles Only (CustomRoles)</code> <ul> <li>For EJBs and URL patterns, this model uses only the <i>policies</i> in the J2EE deployment descriptors (DD). EJBs and URL patterns are not protected by policies of a broader scope (such as a policy scoped to an entire Web application). This model ignores any <i>roles</i> defined in the DDs; an administrator completes the role mappings using the Administration Console.</li>  <li>For all other types of resources, you can use the Administration Console to create roles or policies. For example, with this model, you can use the Administration Console to create application-scoped policies or roles for an EAR.</li>  <li>Applies for the life of the deployment. If you want to use a different model, you must delete the deployment and reinstall it.</li> </ul></li>  <li><code>Customize Roles and Policies (CustomRolesAndPolicies)</code> <ul> <li>Ignores any roles and policies defined in deployment descriptors. An administrator uses the Administration Console to secure the resources.</li>  <li>Performs security checks for <b>all</b> URLs or EJB methods in the module.</li>  <li>Applies for the life of the deployment. If you want to use a different model, you must delete the deployment and reinstall it.</li> </ul></li>  <li><code>Advanced (Advanced)</code> <p>You configure how this model behaves by setting values for the following options:</p> <ul> <li><code>When Deploying Web Applications or EJBs</code> <dl><dt>Note:</dt> <dd>When using the WebLogic Scripting Tool or JMX APIs, there is no single MBean attribute for this setting. Instead, you must set the values for the <code>DeployPolicyIgnored</code> and <code>DeployRoleIgnored</code> attributes of <code>RealmMBean</code>.</dd> </dl></li>  <li><code>Check Roles and Policies (FullyDelegateAuthorization)</code></li>  <li><code>Combined Role Mapping Enabled (CombinedRoleMappingEnabled)</code></li> </ul>You can change the configuration of this model. Any changes immediately apply to all modules that use the Advanced model. For example, you can specify that all modules using this model will copy roles and policies from their deployment descriptors into the appropriate provider databases upon deployment. After you deploy all of your modules, you can change this behavior to ignore roles and policies in deployment descriptors so that when you redeploy modules they will not re-copy roles and policies.<dl> <dt>Note:</dt>  <dd>Prior to WebLogic Server version 9.0 the Advanced model was the only security model available. Use this model if you want to continue to secure EJBs and Web Applications as in releases prior to 9.0.</dd> </dl></li> </ul> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#isDeployPolicyIgnored()"), BeanInfoHelper.encodeEntities("#isDeployRoleIgnored()"), BeanInfoHelper.encodeEntities("#isFullyDelegateAuthorization()"), BeanInfoHelper.encodeEntities("#isCombinedRoleMappingEnabled()")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, "DDOnly");
         var2.setValue("legalValues", new Object[]{"DDOnly", "CustomRoles", "CustomRolesAndPolicies", "Advanced"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("UserLockoutManager")) {
         var3 = "getUserLockoutManager";
         var4 = null;
         var2 = new PropertyDescriptor("UserLockoutManager", RealmMBean.class, var3, var4);
         var1.put("UserLockoutManager", var2);
         var2.setValue("description", "Returns the User Lockout Manager for this security realm. ");
         var2.setValue("relationship", "containment");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("CombinedRoleMappingEnabled")) {
         var3 = "isCombinedRoleMappingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCombinedRoleMappingEnabled";
         }

         var2 = new PropertyDescriptor("CombinedRoleMappingEnabled", RealmMBean.class, var3, var4);
         var1.put("CombinedRoleMappingEnabled", var2);
         var2.setValue("description", "<p>Determines how the role mappings in the Enterprise Application, Web application, and EJB containers interact. This setting is valid only for Web applications and EJBs that use the Advanced security model and that initialize roles from deployment descriptors.</p> <p>When enabled:</p>  <ul> <li>Application role mappings are combined with EJB and Web application mappings so that all principal mappings are included. The Security Service combines the role mappings with a logical <code>OR</code> operator.</li>  <li>If one or more policies in the <code>web.xml</code> file specify a role for which no mapping exists in the <code>weblogic.xml</code> file, the Web application container creates an empty map for the undefined role (that is, the role is explicitly defined as containing no principal). Therefore, no one can access URL patterns that are secured by such policies.</li>  <li>If one or more policies in the <code>ejb-jar.xml</code> file specify a role for which no mapping exists in the <code>weblogic-ejb-jar.xml</code> file, the EJB container creates an empty map for the undefined role (that is, the role is explicitly defined as containing no principal). Therefore, no one can access methods that are secured by such policies.</li> </ul>  <p>When disabled:</p>  <ul> <li>Role mappings for each container are exclusive to other containers unless defined by the <code>&lt;externally-defined&gt;</code> descriptor element.</li>  <li>If one or more policies in the <code>web.xml</code> file specify a role for which no role mapping exists in the <code>weblogic.xml</code> file, the Web application container assumes that the undefined role is the name of a principal. It therefore maps the assumed principal to the role name. For example, if the <code>web.xml</code> file contains the following stanza in one of its policies:<br /><code>&lt;auth-constraint&gt; &lt;role-name&gt;PrivilegedUser&lt;/role-name&gt; &lt;/auth-constraint&gt;</code><br />but the <code>weblogic.xml</code> file has no role mapping for <code>PrivilegedUser</code>, then the Web application container creates an in-memory mapping that is equivalent to the following stanza:<br /><code>&lt;security-role-assignment&gt; &lt;role-name&gt;PrivilegedUser&lt;/role-name&gt; &lt;principal-name&gt;PrivilegedUser&lt;/principal-name&gt; &lt;/security-role-assignment&gt;</code></li>  <li>Role mappings for EJB methods must be defined in the <code>weblogic-ejb-jar.xml</code> file. Role mappings defined in the other containers are not used unless defined by the <code>&lt;externally-defined&gt;</code> descriptor element.</li> </ul>  <dl> <dt>Note:</dt>  <dd>For all applications previously deployed in version 8.1 and upgraded to version 9.x, the combining role mapping is disabled by default.</dd> </dl> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("DefaultRealm")) {
         var3 = "isDefaultRealm";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultRealm";
         }

         var2 = new PropertyDescriptor("DefaultRealm", RealmMBean.class, var3, var4);
         var1.put("DefaultRealm", var2);
         var2.setValue("description", "Returns whether this security realm is the Default realm for the WebLogic domain. Deprecated in this release of WebLogic Server and replaced by <code>weblogic.management.configuration.SecurityConfigurationMBean.getDefaultRealm</code>. ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.configuration.SecurityConfigurationMBean#getDefaultRealm()} ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (BeanInfoHelper.isVersionCompliant("9.1.0.0", (String)null, this.targetVersion) && !var1.containsKey("DelegateMBeanAuthorization")) {
         var3 = "isDelegateMBeanAuthorization";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDelegateMBeanAuthorization";
         }

         var2 = new PropertyDescriptor("DelegateMBeanAuthorization", RealmMBean.class, var3, var4);
         var1.put("DelegateMBeanAuthorization", var2);
         var2.setValue("description", "<p>Configures the WebLogic Server MBean servers to use the security realm's Authorization providers to determine whether a JMX client has permission to access an MBean attribute or invoke an MBean operation.</p> <p>You can continue to use WebLogic Server's default security settings or modify the defaults to suit your needs.</p> <p>If you do not delegate authorization to the realm's Authorization providers, the WebLogic MBean servers allow access only to the four default security roles (Admin, Deployer, Operator, and Monitor) and only as specified by WebLogic Server's default security settings.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("<a href=\"../html/mbeansecroles.html\">Default Security Policies for MBeans</a>")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("since", "9.1.0.0");
      }

      if (!var1.containsKey("DeployCredentialMappingIgnored")) {
         var3 = "isDeployCredentialMappingIgnored";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDeployCredentialMappingIgnored";
         }

         var2 = new PropertyDescriptor("DeployCredentialMappingIgnored", RealmMBean.class, var3, var4);
         var1.put("DeployCredentialMappingIgnored", var2);
         var2.setValue("description", "Returns whether credential mapping deployment calls on the security system are ignored or passed to the configured Credential Mapping providers. ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DeployPolicyIgnored")) {
         var3 = "isDeployPolicyIgnored";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDeployPolicyIgnored";
         }

         var2 = new PropertyDescriptor("DeployPolicyIgnored", RealmMBean.class, var3, var4);
         var1.put("DeployPolicyIgnored", var2);
         var2.setValue("description", "Returns whether policy deployment calls on the security system are ignored or passed to the configured Authorization providers. ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DeployRoleIgnored")) {
         var3 = "isDeployRoleIgnored";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDeployRoleIgnored";
         }

         var2 = new PropertyDescriptor("DeployRoleIgnored", RealmMBean.class, var3, var4);
         var1.put("DeployRoleIgnored", var2);
         var2.setValue("description", "Returns whether role deployment calls on the security system are ignored or passed to the configured Role Mapping providers. ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("10.3", (String)null, this.targetVersion) && !var1.containsKey("DeployableProviderSynchronizationEnabled")) {
         var3 = "isDeployableProviderSynchronizationEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDeployableProviderSynchronizationEnabled";
         }

         var2 = new PropertyDescriptor("DeployableProviderSynchronizationEnabled", RealmMBean.class, var3, var4);
         var1.put("DeployableProviderSynchronizationEnabled", var2);
         var2.setValue("description", "Specifies whether synchronization for deployable Authorization and Role Mapping providers is enabled. <p>The Authorization and Role Mapping providers may or may not support parallel security policy and role modification, respectively, in the security provider database. If the security providers do not support parallel modification, the WebLogic Security Framework enforces a synchronization mechanism that results in each application and module being placed in a queue and deployed sequentially. ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("since", "10.3");
      }

      if (!var1.containsKey("EnableWebLogicPrincipalValidatorCache")) {
         var3 = "isEnableWebLogicPrincipalValidatorCache";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEnableWebLogicPrincipalValidatorCache";
         }

         var2 = new PropertyDescriptor("EnableWebLogicPrincipalValidatorCache", RealmMBean.class, var3, var4);
         var1.put("EnableWebLogicPrincipalValidatorCache", var2);
         var2.setValue("description", "Returns whether the WebLogic Principal Validator caching is enabled. <p> The Principal Validator is used by Oracle supplied authentication providers and may be used by custom authentication providers. If enabled, the default principal validator will cache WebLogic Principal signatures. ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("FullyDelegateAuthorization")) {
         var3 = "isFullyDelegateAuthorization";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFullyDelegateAuthorization";
         }

         var2 = new PropertyDescriptor("FullyDelegateAuthorization", RealmMBean.class, var3, var4);
         var1.put("FullyDelegateAuthorization", var2);
         var2.setValue("description", "Returns whether the Web and EJB containers should call the security framework on every access. <p> If false the containers are free to only call the security framework when security is set in the deployment descriptors. ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ValidateDDSecurityData")) {
         var3 = "isValidateDDSecurityData";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setValidateDDSecurityData";
         }

         var2 = new PropertyDescriptor("ValidateDDSecurityData", RealmMBean.class, var3, var4);
         var1.put("ValidateDDSecurityData", var2);
         var2.setValue("description", "<p>Not used in this release.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = RealmMBean.class.getMethod("createAuditor", String.class, String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "- The name of this Auditing provider, for example, <code>DefaultAuditor</code> "), createParameterDescriptor("type", "- The type of this Auditing provider, for example, <code>weblogic.security.providers.audit.DefaultAuditor</code> Use <code>getAuditorTypes</code> to find the list of types that may be specified. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Creates an Auditing provider in this security realm. The new Auditing provider is added to the end of the list of Auditing providers  configured in this security realm. ");
         var2.setValue("role", "factory");
         var2.setValue("property", "Auditors");
      }

      var3 = RealmMBean.class.getMethod("createAuditor", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("type", "- The type of this Auditing provider, for example, <code>weblogic.security.providers.audit.DefaultAuditor</code> Use <code>getAuditorTypes</code> to find the list of types that may be specified. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Creates an Auditing provider in this security realm. The new Auditing provider is added to the end of the list of Auditing providers  configured in this security realm. ");
         var2.setValue("role", "factory");
         var2.setValue("property", "Auditors");
      }

      var3 = RealmMBean.class.getMethod("destroyAuditor", AuditorMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("auditor", "- The Auditing provider to remove. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Removes the configuration for an Auditing provider in this security realm. It does not remove any persistent data for the Auditing provider (such as databases or files). <code>weblogic.management.configuration.SecurityConfigurationMBean.destroyRealm</code> automatically removes the security realm's Auditing providers. ");
         var2.setValue("role", "factory");
         var2.setValue("property", "Auditors");
      }

      var3 = RealmMBean.class.getMethod("createAuthenticationProvider", String.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "- The name of this Authentication provider, for example, <code>DefaultAuthenticator</code> "), createParameterDescriptor("type", "- The type of this Authentication provider, for example, <code>weblogic.security.providers.authentication.DefaultAuthenticator</code> Use <code>getAuthenticationProviderTypes</code> to find the list of types that may be specified. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Creates an Authentication provider in this security realm. The new Authentication provider is added to the end of the list of Authentication providers  configured in this security realm. ");
         var2.setValue("role", "factory");
         var2.setValue("property", "AuthenticationProviders");
      }

      var3 = RealmMBean.class.getMethod("createAuthenticationProvider", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("type", "- The type of this Authentication provider, for example, <code>weblogic.security.providers.authentication.DefaultAuthenticator</code> Use <code>getAuthenticationProviderTypes</code> to find the list of types that may be specified. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Creates an Authentication provider in this security realm. The new Authentication provider is added to the end of the list of Authentication providers  configured in this security realm. ");
         var2.setValue("role", "factory");
         var2.setValue("property", "AuthenticationProviders");
      }

      var3 = RealmMBean.class.getMethod("destroyAuthenticationProvider", AuthenticationProviderMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("authenticationProvider", "- The Authentication provider to remove. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Removes the configuration for an Authentication provider in this security realm. It does not remove any persistent data for the Authentication provider (such as databases or files). <code>weblogic.management.configuration.SecurityConfigurationMBean.destroyRealm</code> automatically removes the security realm's Authentication providers. ");
         var2.setValue("role", "factory");
         var2.setValue("property", "AuthenticationProviders");
      }

      var3 = RealmMBean.class.getMethod("createRoleMapper", String.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "- The name of this Role Mapping provider, for example, <code>DefaultRoleMapper</code> "), createParameterDescriptor("type", "- The type of this Role Mapping provider, for example, <code>weblogic.security.providers.authorization.DefaultRoleMapper</code> Use <code>getRoleMapperTypes</code> to find the list of types that may be specified. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Creates a Role Mapping provider in this security realm. The new Role Mapping provider is added to the end of the list of Role Mapping providers  configured in this security realm. ");
         var2.setValue("role", "factory");
         var2.setValue("property", "RoleMappers");
      }

      var3 = RealmMBean.class.getMethod("createRoleMapper", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("type", "- The type of this Role Mapping provider, for example, <code>weblogic.security.providers.authorization.DefaultRoleMapper</code> Use <code>getRoleMapperTypes</code> to find the list of types that may be specified. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Creates a Role Mapping provider in this security realm. The new Role Mapping provider is added to the end of the list of Role Mapping providers  configured in this security realm. ");
         var2.setValue("role", "factory");
         var2.setValue("property", "RoleMappers");
      }

      var3 = RealmMBean.class.getMethod("destroyRoleMapper", RoleMapperMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("roleMapper", "- The Role Mapping provider to remove. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Removes the configuration for a Role Mapping provider in this security realm. It does not remove any persistent data for the Role Mapping provider (such as databases or files). <code>weblogic.management.configuration.SecurityConfigurationMBean.destroyRealm</code> automatically removes the security realm's Role Mapping providers. ");
         var2.setValue("role", "factory");
         var2.setValue("property", "RoleMappers");
      }

      var3 = RealmMBean.class.getMethod("createAuthorizer", String.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "- The name of this Authorization provider, for example, <code>DefaultAuthorizer</code> "), createParameterDescriptor("type", "- The type of this Authorization provider, for example, <code>weblogic.security.providers.authorization.DefaultAuthorizer</code> Use <code>getAuthorizerTypes</code> to find the list of types that may be specified. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Creates an Authorization provider in this security realm. The new Authorization provider is added to the end of the list of Authorization providers  configured in this security realm. ");
         var2.setValue("role", "factory");
         var2.setValue("property", "Authorizers");
      }

      var3 = RealmMBean.class.getMethod("createAuthorizer", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("type", "- The type of this Authorization provider, for example, <code>weblogic.security.providers.authorization.DefaultAuthorizer</code> Use <code>getAuthorizerTypes</code> to find the list of types that may be specified. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Creates an Authorization provider in this security realm. The new Authorization provider is added to the end of the list of Authorization providers  configured in this security realm. ");
         var2.setValue("role", "factory");
         var2.setValue("property", "Authorizers");
      }

      var3 = RealmMBean.class.getMethod("destroyAuthorizer", AuthorizerMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("authorizer", "- The Authorization provider to remove. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Removes the configuration for an Authorization provider in this security realm. It does not remove any persistent data for the Authorization provider (such as databases or files). <code>weblogic.management.configuration.SecurityConfigurationMBean.destroyRealm</code> automatically removes the security realm's Authorization providers. ");
         var2.setValue("role", "factory");
         var2.setValue("property", "Authorizers");
      }

      var3 = RealmMBean.class.getMethod("createAdjudicator", String.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "- The name of this Adjudication provider, for example, <code>DefaultAdjudicator</code> "), createParameterDescriptor("type", "- The type of this Adjudication provider, for example, <code>weblogic.security.providers.authorization.DefaultAdjudicator</code> Use <code>getAdjudicatorTypes</code> to find the list of types that may be specified. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Creates an Adjudication provider in this security realm and removes this security realm's previous Adjudication provider. ");
         var2.setValue("role", "factory");
         var2.setValue("property", "Adjudicator");
      }

      var3 = RealmMBean.class.getMethod("createAdjudicator", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("type", "- The type of this Adjudication provider, for example, <code>weblogic.security.providers.authorization.DefaultAdjudicator</code> Use <code>getAdjudicatorTypes</code> to find the list of types that may be specified. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Creates an Adjudication provider in this security realm and removes this security realm's previous Adjudication provider. ");
         var2.setValue("role", "factory");
         var2.setValue("property", "Adjudicator");
      }

      var3 = RealmMBean.class.getMethod("destroyAdjudicator");
      String var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var8, var2);
         var2.setValue("description", "Removes the configuration this security realm's Adjudication provider (if there is one). It does not remove any persistent data for the Adjudication provider (such as databases or files). <code>weblogic.management.configuration.SecurityConfigurationMBean.destroyRealm</code> automatically removes the security realm's Adjudication provider. ");
         var2.setValue("role", "factory");
         var2.setValue("property", "Adjudicator");
      }

      var3 = RealmMBean.class.getMethod("createCredentialMapper", String.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "- The name of this Credential Mapping provider, for example, <code>DefaultCredentialMapper</code> "), createParameterDescriptor("type", "- The type of this Credential Mapping provider, for example, <code>weblogic.security.providers.credentials.DefaultCredentialMapper</code> Use <code>getCredentialMapperTypes</code> to find the list of types that may be specified. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Creates a Credential Mapping provider in this security realm. The new Credential Mapping provider is added to the end of the list of Credential Mapping providers  configured in this security realm. ");
         var2.setValue("role", "factory");
         var2.setValue("property", "CredentialMappers");
      }

      var3 = RealmMBean.class.getMethod("createCredentialMapper", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("type", "- The type of this Credential Mapping provider, for example, <code>weblogic.security.providers.credentials.DefaultCredentialMapper</code> Use <code>getCredentialMapperTypes</code> to find the list of types that may be specified. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Creates a Credential Mapping provider in this security realm. The new Credential Mapping provider is added to the end of the list of Credential Mapping providers  configured in this security realm. ");
         var2.setValue("role", "factory");
         var2.setValue("property", "CredentialMappers");
      }

      var3 = RealmMBean.class.getMethod("destroyCredentialMapper", CredentialMapperMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("credentialMapper", "- The Credential Mapping provider to remove. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Removes the configuration for a Credential Mapping provider in this security realm. It does not remove any persistent data for the Credential Mapping provider (such as databases or files). <code>weblogic.management.configuration.SecurityConfigurationMBean.destroyRealm</code> automatically removes the security realm's Credential Mapping providers. ");
         var2.setValue("role", "factory");
         var2.setValue("property", "CredentialMappers");
      }

      var3 = RealmMBean.class.getMethod("createCertPathProvider", String.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "- The name of this Certification Path provider, for example, <code>WebLogicCertPathProvider</code> "), createParameterDescriptor("type", "- The type of this Certification Path provider, for example, <code>weblogic.security.providers.pk.WebLogicCertPathProvider</code> Use <code>getCertPathProviderTypes</code> to find the list of types that may be specified. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Creates a Certification Path provider in this security realm. The new Certification Path provider is added to the end of the list of Certification Path providers  configured in this security realm.  The active security realm must contain at least one Certification Path provider that is a CertPath Builder provider and at least one Certificate Path provider that is a CertPath Validator provider. ");
         var2.setValue("role", "factory");
         var2.setValue("property", "CertPathProviders");
      }

      var3 = RealmMBean.class.getMethod("createCertPathProvider", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("type", "- The type of this Certification Path provider, for example, <code>weblogic.security.providers.pk.WebLogicCertPathProvider</code> Use <code>getCertPathProviderTypes</code> to find the list of types that may be specified. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Creates a Certification Path provider in this security realm. The new Certification Path provider is added to the end of the list of Certification Path providers  configured in this security realm. <p> The active security realm must contain at least one Certification Path provider that is a CertPath Builder provider and at least one Certificate Path provider that is a CertPath Validator provider. ");
         var2.setValue("role", "factory");
         var2.setValue("property", "CertPathProviders");
      }

      var3 = RealmMBean.class.getMethod("destroyCertPathProvider", CertPathProviderMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("certPathProvider", "- The Certification Path provider to remove. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Removes the configuration for a Certification Path provider in this security realm. It does not remove any persistent data for the Certification Path provider (such as databases or files). <code>weblogic.management.configuration.SecurityConfigurationMBean.destroyRealm</code> automatically removes the security realm's Certification Path providers. <p> If <code>certPathProvider</code> has been selected as this security realm's <code>CertPathBuilder</code>, then this security realm's will have no <code>CertPathBuilder</code>. ");
         var2.setValue("role", "factory");
         var2.setValue("property", "CertPathProviders");
      }

      var3 = RealmMBean.class.getMethod("createKeyStore", String.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "- The name of this KeyStore provider, for example, <code>DefaultKeyStore</code> "), createParameterDescriptor("type", "- The type of this KeyStore provider, for example, <code>weblogic.security.providers.pk.DefaultKeyStore</code> Use <code>getKeyStoreTypes</code> to find the list of types that may be specified. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "8.1.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "Creates a KeyStore provider in this security realm. The new KeyStore provider is added to the end of the list of KeyStore providers  configured in this security realm. ");
         var2.setValue("role", "factory");
         var2.setValue("property", "KeyStores");
      }

      var3 = RealmMBean.class.getMethod("createKeyStore", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("type", "- The type of this KeyStore provider, for example, <code>weblogic.security.providers.pk.DefaultKeyStore</code> Use <code>getKeyStoreTypes</code> to find the list of types that may be specified. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "8.1.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "Creates a KeyStore provider in this security realm. The new KeyStore provider is added to the end of the list of KeyStore providers  configured in this security realm. ");
         var2.setValue("role", "factory");
         var2.setValue("property", "KeyStores");
      }

      var3 = RealmMBean.class.getMethod("destroyKeyStore", KeyStoreMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("keystore", "- The KeyStore provider to remove. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "8.1.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "Removes the configuration for a KeyStore provider in this security realm. It does not remove any persistent data for the KeyStore provider (such as databases or files). <code>weblogic.management.configuration.SecurityConfigurationMBean.destroyRealm</code> automatically removes the security realm's KeyStore providers. ");
         var2.setValue("role", "factory");
         var2.setValue("property", "KeyStores");
      }

      var3 = RealmMBean.class.getMethod("createRDBMSSecurityStore");
      var8 = BeanInfoHelper.buildMethodKey(var3);
      String[] var6;
      String[] var9;
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var9 = new String[]{BeanInfoHelper.encodeEntities("JMException if an error occurs when creating a RDBMS security store")};
         var2.setValue("throws", var9);
         var1.put(var8, var2);
         var2.setValue("description", "Creates configuration for the RDBMS security store. This can be called only once unless the existing instance is destroyed by invoking <code>destroyRDBMSSecurityStore</code> operation. The new security store MBean will have this realm as its parent. ");
         var6 = new String[]{BeanInfoHelper.encodeEntities("#destroyRDBMSSecurityStore")};
         var2.setValue("see", var6);
         var2.setValue("role", "factory");
         var2.setValue("property", "RDBMSSecurityStore");
      }

      var3 = RealmMBean.class.getMethod("createRDBMSSecurityStore", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "the name of this RDBMS security store ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("JMException if an error occurs when creating a RDBMS security store")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "Creates configuration for the RDBMS security store with the specified name. This can be called only once unless the existing instance is destroyed by invoking <code>destroyRDBMSSecurityStore</code> operation. The new security store MBean will have this realm as its parent. ");
         String[] var7 = new String[]{BeanInfoHelper.encodeEntities("#destroyRDBMSSecurityStore")};
         var2.setValue("see", var7);
         var2.setValue("role", "factory");
         var2.setValue("property", "RDBMSSecurityStore");
      }

      var3 = RealmMBean.class.getMethod("destroyRDBMSSecurityStore");
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var8, var2);
         var2.setValue("description", "Destroys and removes the existing RDBMS security store which is a child of this realm. It only removes the security store configuration, not any data persisted in the store. ");
         var9 = new String[]{BeanInfoHelper.encodeEntities("#createRDBMSSecurityStore")};
         var2.setValue("see", var9);
         var2.setValue("role", "factory");
         var2.setValue("property", "RDBMSSecurityStore");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0", (String)null, this.targetVersion)) {
         var3 = RealmMBean.class.getMethod("createPasswordValidator", Class.class, String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("subClass", "Class The class of a Password Validator provider MBean implementation "), createParameterDescriptor("name", "String The name for the given Password Validator provider MBean ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var6 = new String[]{BeanInfoHelper.encodeEntities("JMException")};
            var2.setValue("throws", var6);
            var2.setValue("since", "10.0");
            var1.put(var5, var2);
            var2.setValue("description", "Creates a Password Validator provider in this security realm. The new Password Validator provider is added to the end of the list of Password Validator providers configured in this security realm. ");
            var2.setValue("role", "factory");
            var2.setValue("property", "PasswordValidators");
            var2.setValue("since", "10.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("10.0", (String)null, this.targetVersion)) {
         var3 = RealmMBean.class.getMethod("createPasswordValidator", String.class, String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "String The name for the given Password Validator provider MBean "), createParameterDescriptor("type", "String The type of a Password Validator provider, all available types are in method <code>getPasswordValidatorTypes</code> ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var6 = new String[]{BeanInfoHelper.encodeEntities("ClassNotFoundException"), BeanInfoHelper.encodeEntities("JMException")};
            var2.setValue("throws", var6);
            var2.setValue("since", "10.0");
            var1.put(var5, var2);
            var2.setValue("description", "Creates a Password Validator provider in this security realm. The new Password Validator provider is added to the end of the list of Password Validator providers configured in this security realm. ");
            var2.setValue("role", "factory");
            var2.setValue("property", "PasswordValidators");
            var2.setValue("since", "10.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("10.0", (String)null, this.targetVersion)) {
         var3 = RealmMBean.class.getMethod("createPasswordValidator", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("type", "String The type of a Password Validator provider, all available types are in method <code>getPasswordValidatorTypes</code> ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var6 = new String[]{BeanInfoHelper.encodeEntities("ClassNotFoundException"), BeanInfoHelper.encodeEntities("JMException")};
            var2.setValue("throws", var6);
            var2.setValue("since", "10.0");
            var1.put(var5, var2);
            var2.setValue("description", "Creates a Password Validator provider in this security realm. The new Password Validator provider is added to the end of the list of Password Validator providers configured in this security realm. ");
            var2.setValue("role", "factory");
            var2.setValue("property", "PasswordValidators");
            var2.setValue("since", "10.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("10.0", (String)null, this.targetVersion)) {
         var3 = RealmMBean.class.getMethod("destroyPasswordValidator", PasswordValidatorMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("provider", "PasswordValidatorMBean The Password Validator provider to remove ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "10.0");
            var1.put(var5, var2);
            var2.setValue("description", "Removes the configuration for a Password Validator provider in this security realm. ");
            var2.setValue("role", "factory");
            var2.setValue("property", "PasswordValidators");
            var2.setValue("since", "10.0");
         }
      }

   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = RealmMBean.class.getMethod("lookupAuditor", String.class);
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "Finds an Auditing provider in this security realm. Returns null if this security realm has no Auditing provider of the specified name. ");
         var2.setValue("role", "finder");
         var2.setValue("property", "Auditors");
      }

      var3 = RealmMBean.class.getMethod("lookupAuthenticationProvider", String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "Finds an Authentication provider in this security realm. Returns null if this security realm has no Authentication provider of the specified name. ");
         var2.setValue("role", "finder");
         var2.setValue("property", "AuthenticationProviders");
      }

      var3 = RealmMBean.class.getMethod("lookupRoleMapper", String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "Finds a Role Mapping provider in this security realm. Returns null if this security realm has no Role Mapping provider of the specified name. ");
         var2.setValue("role", "finder");
         var2.setValue("property", "RoleMappers");
      }

      var3 = RealmMBean.class.getMethod("lookupAuthorizer", String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "Finds an Authorization provider in this security realm. Returns null if this security realm has no Authorization provider of the specified name. ");
         var2.setValue("role", "finder");
         var2.setValue("property", "Authorizers");
      }

      var3 = RealmMBean.class.getMethod("lookupCredentialMapper", String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "Finds a Credential Mapping provider in this security realm. Returns null if this security realm has no Credential Mapping provider of the specified name. ");
         var2.setValue("role", "finder");
         var2.setValue("property", "CredentialMappers");
      }

      var3 = RealmMBean.class.getMethod("lookupCertPathProvider", String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "Finds a Certification Path provider in this security realm. Returns null if this security realm has no Certification Path provider of the specified name. ");
         var2.setValue("role", "finder");
         var2.setValue("property", "CertPathProviders");
      }

      var3 = RealmMBean.class.getMethod("lookupKeyStore", String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "8.1.0.0 ");
         var1.put(var4, var2);
         var2.setValue("description", "Finds a KeyStore provider in this security realm. Returns null if this security realm has no KeyStore provider of the specified name. ");
         var2.setValue("role", "finder");
         var2.setValue("property", "KeyStores");
      }

      if (BeanInfoHelper.isVersionCompliant("10.0", (String)null, this.targetVersion)) {
         var3 = RealmMBean.class.getMethod("lookupPasswordValidator", String.class);
         ParameterDescriptor[] var6 = new ParameterDescriptor[]{createParameterDescriptor("name", "String The name of a Password Validator provider MBean ")};
         String var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var6);
            var2.setValue("since", "10.0");
            var1.put(var5, var2);
            var2.setValue("description", "Finds an Password Validator provider in this security realm. Returns null if this security realm has no Password Validator provider with the specified name. ");
            var2.setValue("role", "finder");
            var2.setValue("property", "PasswordValidators");
            var2.setValue("since", "10.0");
         }
      }

   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = RealmMBean.class.getMethod("validate");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         MethodDescriptor var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", "9.0.0.0 This method is no longer required since activating a configuration transaction does this check automatically on the default realm, and will not allow the configuration to be saved if the domain does not have a valid default realm configured. ");
         var1.put(var4, var2);
         var2.setValue("description", "Checks that the realm is valid. ");
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
