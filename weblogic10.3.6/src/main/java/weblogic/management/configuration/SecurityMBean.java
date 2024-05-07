package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.management.runtime.ServerSecurityRuntimeMBean;

/** @deprecated */
public interface SecurityMBean extends ConfigurationMBean {
   RealmMBean getRealm();

   void setRealm(RealmMBean var1) throws InvalidAttributeValueException;

   String getAuditProviderClassName();

   void setAuditProviderClassName(String var1) throws InvalidAttributeValueException;

   boolean isGuestDisabled();

   void setGuestDisabled(boolean var1);

   /** @deprecated */
   String getConnectionFilter();

   /** @deprecated */
   void setConnectionFilter(String var1) throws InvalidAttributeValueException;

   String getSystemUser();

   void setSystemUser(String var1) throws InvalidAttributeValueException;

   boolean getLogAllChecksEnabled();

   void setLogAllChecksEnabled(boolean var1);

   PasswordPolicyMBean getPasswordPolicy();

   void setPasswordPolicy(PasswordPolicyMBean var1) throws InvalidAttributeValueException;

   byte[] getSalt();

   byte[] getEncryptedSecretKey();

   ServerSecurityRuntimeMBean getServerSecurityRuntime();

   void setServerSecurityRuntime(ServerSecurityRuntimeMBean var1);

   boolean isCompatibilityMode();

   void setCompatibilityMode(boolean var1);

   /** @deprecated */
   String[] getConnectionFilterRules();

   /** @deprecated */
   void setConnectionFilterRules(String[] var1);

   /** @deprecated */
   boolean getConnectionLoggerEnabled();

   /** @deprecated */
   void setConnectionLoggerEnabled(boolean var1) throws InvalidAttributeValueException;
}
