package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import javax.management.JMException;
import weblogic.management.security.ProviderMBean;
import weblogic.management.security.RealmContainer;

public interface SecurityConfigurationMBean extends ConfigurationMBean, RealmContainer {
   String REALM_BOOTSTRAP_VERSION_UNKNOWN = "unknown";
   String REALM_BOOTSTRAP_VERSION_1 = "1";
   String REALM_BOOTSTRAP_VERSION_CURRENT = "1";

   weblogic.management.security.RealmMBean createRealm(String var1) throws JMException;

   weblogic.management.security.RealmMBean createRealm() throws JMException;

   void destroyRealm(weblogic.management.security.RealmMBean var1);

   weblogic.management.security.RealmMBean[] getRealms();

   weblogic.management.security.RealmMBean lookupRealm(String var1);

   /** @deprecated */
   weblogic.management.security.RealmMBean[] findRealms();

   /** @deprecated */
   weblogic.management.security.RealmMBean findDefaultRealm();

   /** @deprecated */
   weblogic.management.security.RealmMBean findRealm(String var1);

   weblogic.management.security.RealmMBean getDefaultRealm();

   void setDefaultRealm(weblogic.management.security.RealmMBean var1) throws InvalidAttributeValueException;

   ProviderMBean[] pre90getProviders();

   byte[] getSalt();

   byte[] getEncryptedSecretKey();

   boolean isAnonymousAdminLookupEnabled();

   void setAnonymousAdminLookupEnabled(boolean var1);

   boolean isClearTextCredentialAccessEnabled();

   void setClearTextCredentialAccessEnabled(boolean var1);

   boolean isCredentialGenerated();

   void setCredentialGenerated(boolean var1);

   byte[] generateCredential();

   String getCredential();

   void setCredential(String var1) throws InvalidAttributeValueException;

   byte[] getCredentialEncrypted();

   void setCredentialEncrypted(byte[] var1) throws InvalidAttributeValueException;

   String getWebAppFilesCaseInsensitive();

   void setWebAppFilesCaseInsensitive(String var1) throws InvalidAttributeValueException;

   String getRealmBootStrapVersion();

   void setRealmBootStrapVersion(String var1);

   String getConnectionFilter();

   void setConnectionFilter(String var1) throws InvalidAttributeValueException;

   String[] getConnectionFilterRules();

   void setConnectionFilterRules(String[] var1);

   boolean getConnectionLoggerEnabled();

   void setConnectionLoggerEnabled(boolean var1) throws InvalidAttributeValueException;

   boolean getCompatibilityConnectionFiltersEnabled();

   void setCompatibilityConnectionFiltersEnabled(boolean var1) throws InvalidAttributeValueException;

   String getNodeManagerUsername();

   void setNodeManagerUsername(String var1);

   String getNodeManagerPassword();

   void setNodeManagerPassword(String var1);

   byte[] getNodeManagerPasswordEncrypted();

   void setNodeManagerPasswordEncrypted(byte[] var1);

   boolean isPrincipalEqualsCaseInsensitive();

   void setPrincipalEqualsCaseInsensitive(boolean var1);

   boolean isPrincipalEqualsCompareDnAndGuid();

   void setPrincipalEqualsCompareDnAndGuid(boolean var1);

   boolean getDowngradeUntrustedPrincipals();

   void setDowngradeUntrustedPrincipals(boolean var1);

   boolean getEnforceStrictURLPattern();

   void setEnforceStrictURLPattern(boolean var1);

   boolean getEnforceValidBasicAuthCredentials();

   void setEnforceValidBasicAuthCredentials(boolean var1);

   boolean isConsoleFullDelegationEnabled();

   void setConsoleFullDelegationEnabled(boolean var1);

   weblogic.management.security.RealmMBean getDefaultRealmInternal();

   void setDefaultRealmInternal(weblogic.management.security.RealmMBean var1);

   String[] getExcludedDomainNames();

   void setExcludedDomainNames(String[] var1);

   boolean isCrossDomainSecurityEnabled();

   void setCrossDomainSecurityEnabled(boolean var1);

   byte[] getEncryptedAESSecretKey();

   CertRevocMBean getCertRevoc();
}
