package weblogic.management.security;

import javax.management.InvalidAttributeValueException;
import javax.management.JMException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.commo.StandardInterface;
import weblogic.management.security.audit.AuditorMBean;
import weblogic.management.security.authentication.AuthenticationProviderMBean;
import weblogic.management.security.authentication.PasswordValidatorMBean;
import weblogic.management.security.authentication.UserLockoutManagerMBean;
import weblogic.management.security.authorization.AdjudicatorMBean;
import weblogic.management.security.authorization.AuthorizerMBean;
import weblogic.management.security.authorization.RoleMapperMBean;
import weblogic.management.security.credentials.CredentialMapperMBean;
import weblogic.management.security.pk.CertPathBuilderMBean;
import weblogic.management.security.pk.CertPathProviderMBean;
import weblogic.management.security.pk.KeyStoreMBean;
import weblogic.management.utils.ErrorCollectionException;

public interface RealmMBean extends StandardInterface, DescriptorBean {
   String SECURITY_XML_NAMESPACE = "http://xmlns.oracle.com/weblogic/security";

   AuditorMBean[] getAuditors();

   void setAuditors(AuditorMBean[] var1) throws InvalidAttributeValueException;

   String[] getAuditorTypes();

   AuditorMBean createAuditor(String var1, String var2) throws ClassNotFoundException, JMException;

   AuditorMBean createAuditor(String var1) throws ClassNotFoundException, JMException;

   AuditorMBean createAuditor(Class var1) throws JMException;

   AuditorMBean createAuditor(Class var1, String var2) throws JMException;

   void destroyAuditor(AuditorMBean var1);

   AuditorMBean lookupAuditor(String var1);

   AuthenticationProviderMBean[] getAuthenticationProviders();

   void setAuthenticationProviders(AuthenticationProviderMBean[] var1) throws InvalidAttributeValueException;

   String[] getAuthenticationProviderTypes();

   AuthenticationProviderMBean createAuthenticationProvider(String var1, String var2) throws ClassNotFoundException, JMException;

   AuthenticationProviderMBean createAuthenticationProvider(String var1) throws ClassNotFoundException, JMException;

   AuthenticationProviderMBean createAuthenticationProvider(Class var1) throws JMException;

   AuthenticationProviderMBean createAuthenticationProvider(Class var1, String var2) throws JMException;

   void destroyAuthenticationProvider(AuthenticationProviderMBean var1);

   AuthenticationProviderMBean lookupAuthenticationProvider(String var1);

   RoleMapperMBean[] getRoleMappers();

   void setRoleMappers(RoleMapperMBean[] var1) throws InvalidAttributeValueException;

   String[] getRoleMapperTypes();

   RoleMapperMBean createRoleMapper(String var1, String var2) throws ClassNotFoundException, JMException;

   RoleMapperMBean createRoleMapper(String var1) throws ClassNotFoundException, JMException;

   RoleMapperMBean createRoleMapper(Class var1) throws JMException;

   RoleMapperMBean createRoleMapper(Class var1, String var2) throws JMException;

   void destroyRoleMapper(RoleMapperMBean var1);

   RoleMapperMBean lookupRoleMapper(String var1);

   AuthorizerMBean[] getAuthorizers();

   void setAuthorizers(AuthorizerMBean[] var1) throws InvalidAttributeValueException;

   String[] getAuthorizerTypes();

   AuthorizerMBean createAuthorizer(String var1, String var2) throws ClassNotFoundException, JMException;

   AuthorizerMBean createAuthorizer(String var1) throws ClassNotFoundException, JMException;

   AuthorizerMBean createAuthorizer(Class var1) throws JMException;

   AuthorizerMBean createAuthorizer(Class var1, String var2) throws JMException;

   void destroyAuthorizer(AuthorizerMBean var1);

   AuthorizerMBean lookupAuthorizer(String var1);

   AdjudicatorMBean getAdjudicator();

   String[] getAdjudicatorTypes();

   AdjudicatorMBean createAdjudicator(String var1, String var2) throws ClassNotFoundException, JMException;

   AdjudicatorMBean createAdjudicator(String var1) throws ClassNotFoundException, JMException;

   AdjudicatorMBean createAdjudicator(Class var1) throws JMException;

   AdjudicatorMBean createAdjudicator(Class var1, String var2) throws JMException;

   void destroyAdjudicator();

   CredentialMapperMBean[] getCredentialMappers();

   void setCredentialMappers(CredentialMapperMBean[] var1) throws InvalidAttributeValueException;

   String[] getCredentialMapperTypes();

   CredentialMapperMBean createCredentialMapper(String var1, String var2) throws ClassNotFoundException, JMException;

   CredentialMapperMBean createCredentialMapper(String var1) throws ClassNotFoundException, JMException;

   CredentialMapperMBean createCredentialMapper(Class var1) throws JMException;

   CredentialMapperMBean createCredentialMapper(Class var1, String var2) throws JMException;

   void destroyCredentialMapper(CredentialMapperMBean var1);

   CredentialMapperMBean lookupCredentialMapper(String var1);

   CertPathProviderMBean[] getCertPathProviders();

   void setCertPathProviders(CertPathProviderMBean[] var1) throws InvalidAttributeValueException;

   String[] getCertPathProviderTypes();

   CertPathProviderMBean createCertPathProvider(String var1, String var2) throws ClassNotFoundException, JMException;

   CertPathProviderMBean createCertPathProvider(String var1) throws ClassNotFoundException, JMException;

   CertPathProviderMBean createCertPathProvider(Class var1) throws JMException;

   CertPathProviderMBean createCertPathProvider(Class var1, String var2) throws JMException;

   void destroyCertPathProvider(CertPathProviderMBean var1);

   CertPathProviderMBean lookupCertPathProvider(String var1);

   CertPathBuilderMBean getCertPathBuilder();

   void setCertPathBuilder(CertPathBuilderMBean var1) throws InvalidAttributeValueException;

   /** @deprecated */
   KeyStoreMBean[] getKeyStores();

   /** @deprecated */
   void setKeyStores(KeyStoreMBean[] var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String[] getKeyStoreTypes();

   /** @deprecated */
   KeyStoreMBean createKeyStore(String var1, String var2) throws ClassNotFoundException, JMException;

   /** @deprecated */
   KeyStoreMBean createKeyStore(String var1) throws ClassNotFoundException, JMException;

   KeyStoreMBean createKeyStore(Class var1) throws JMException;

   KeyStoreMBean createKeyStore(Class var1, String var2) throws JMException;

   /** @deprecated */
   void destroyKeyStore(KeyStoreMBean var1);

   /** @deprecated */
   KeyStoreMBean lookupKeyStore(String var1);

   UserLockoutManagerMBean getUserLockoutManager();

   /** @deprecated */
   boolean isDeployRoleIgnored();

   /** @deprecated */
   void setDeployRoleIgnored(boolean var1) throws InvalidAttributeValueException;

   /** @deprecated */
   boolean isDeployPolicyIgnored();

   /** @deprecated */
   void setDeployPolicyIgnored(boolean var1) throws InvalidAttributeValueException;

   /** @deprecated */
   boolean isDeployCredentialMappingIgnored();

   /** @deprecated */
   void setDeployCredentialMappingIgnored(boolean var1) throws InvalidAttributeValueException;

   /** @deprecated */
   boolean isFullyDelegateAuthorization();

   /** @deprecated */
   void setFullyDelegateAuthorization(boolean var1) throws InvalidAttributeValueException;

   boolean isValidateDDSecurityData();

   void setValidateDDSecurityData(boolean var1) throws InvalidAttributeValueException;

   String getSecurityDDModel();

   void setSecurityDDModel(String var1) throws InvalidAttributeValueException;

   boolean isCombinedRoleMappingEnabled();

   void setCombinedRoleMappingEnabled(boolean var1) throws InvalidAttributeValueException;

   /** @deprecated */
   void validate() throws ErrorCollectionException;

   /** @deprecated */
   boolean isDefaultRealm();

   /** @deprecated */
   void setDefaultRealm(boolean var1) throws InvalidAttributeValueException;

   boolean isEnableWebLogicPrincipalValidatorCache();

   void setEnableWebLogicPrincipalValidatorCache(boolean var1) throws InvalidAttributeValueException;

   Integer getMaxWebLogicPrincipalsInCache();

   void setMaxWebLogicPrincipalsInCache(Integer var1) throws InvalidAttributeValueException;

   String getName();

   void setName(String var1) throws InvalidAttributeValueException;

   boolean isDelegateMBeanAuthorization();

   void setDelegateMBeanAuthorization(boolean var1) throws InvalidAttributeValueException;

   String getAuthMethods();

   void setAuthMethods(String var1);

   String getCompatibilityObjectName();

   RDBMSSecurityStoreMBean getRDBMSSecurityStore();

   RDBMSSecurityStoreMBean createRDBMSSecurityStore() throws JMException;

   RDBMSSecurityStoreMBean createRDBMSSecurityStore(String var1) throws JMException;

   void destroyRDBMSSecurityStore();

   PasswordValidatorMBean createPasswordValidator(Class var1) throws JMException;

   PasswordValidatorMBean createPasswordValidator(Class var1, String var2) throws JMException;

   PasswordValidatorMBean createPasswordValidator(String var1, String var2) throws ClassNotFoundException, JMException;

   PasswordValidatorMBean createPasswordValidator(String var1) throws ClassNotFoundException, JMException;

   String[] getPasswordValidatorTypes();

   PasswordValidatorMBean[] getPasswordValidators();

   void setPasswordValidators(PasswordValidatorMBean[] var1) throws InvalidAttributeValueException;

   PasswordValidatorMBean lookupPasswordValidator(String var1);

   void destroyPasswordValidator(PasswordValidatorMBean var1);

   boolean isDeployableProviderSynchronizationEnabled();

   void setDeployableProviderSynchronizationEnabled(boolean var1) throws InvalidAttributeValueException;

   Integer getDeployableProviderSynchronizationTimeout();

   void setDeployableProviderSynchronizationTimeout(Integer var1) throws InvalidAttributeValueException;
}
