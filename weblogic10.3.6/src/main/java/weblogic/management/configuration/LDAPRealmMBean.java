package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;

/** @deprecated */
public interface LDAPRealmMBean extends BasicRealmMBean {
   String getLDAPURL();

   void setLDAPURL(String var1) throws InvalidAttributeValueException;

   String getPrincipal();

   void setPrincipal(String var1) throws InvalidAttributeValueException;

   String getCredential();

   void setCredential(String var1) throws InvalidAttributeValueException;

   byte[] getCredentialEncrypted();

   void setCredentialEncrypted(byte[] var1) throws InvalidAttributeValueException;

   boolean getSSLEnable();

   void setSSLEnable(boolean var1);

   String getLdapProvider();

   void setLdapProvider(String var1) throws InvalidAttributeValueException;

   String getAuthProtocol();

   void setAuthProtocol(String var1) throws InvalidAttributeValueException;

   String getUserAuthentication();

   void setUserAuthentication(String var1) throws InvalidAttributeValueException;

   String getUserPasswordAttribute();

   void setUserPasswordAttribute(String var1) throws InvalidAttributeValueException;

   String getUserDN();

   void setUserDN(String var1) throws InvalidAttributeValueException;

   String getUserNameAttribute();

   void setUserNameAttribute(String var1) throws InvalidAttributeValueException;

   String getGroupDN();

   void setGroupDN(String var1) throws InvalidAttributeValueException;

   String getGroupNameAttribute();

   void setGroupNameAttribute(String var1) throws InvalidAttributeValueException;

   boolean getGroupIsContext();

   void setGroupIsContext(boolean var1) throws InvalidAttributeValueException;

   String getGroupUsernameAttribute();

   void setGroupUsernameAttribute(String var1) throws InvalidAttributeValueException;
}
