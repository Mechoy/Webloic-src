package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;

/** @deprecated */
public interface CachingRealmMBean extends ConfigurationMBean {
   BasicRealmMBean getBasicRealm();

   void setBasicRealm(BasicRealmMBean var1) throws InvalidAttributeValueException;

   boolean getCacheCaseSensitive();

   void setCacheCaseSensitive(boolean var1);

   boolean getACLCacheEnable();

   void setACLCacheEnable(boolean var1);

   boolean getAuthenticationCacheEnable();

   void setAuthenticationCacheEnable(boolean var1);

   boolean getGroupCacheEnable();

   void setGroupCacheEnable(boolean var1);

   boolean getPermissionCacheEnable();

   void setPermissionCacheEnable(boolean var1);

   boolean getUserCacheEnable();

   void setUserCacheEnable(boolean var1);

   int getACLCacheSize();

   void setACLCacheSize(int var1) throws InvalidAttributeValueException;

   int getAuthenticationCacheSize();

   void setAuthenticationCacheSize(int var1) throws InvalidAttributeValueException;

   int getGroupCacheSize();

   void setGroupCacheSize(int var1) throws InvalidAttributeValueException;

   int getPermissionCacheSize();

   void setPermissionCacheSize(int var1) throws InvalidAttributeValueException;

   int getUserCacheSize();

   void setUserCacheSize(int var1) throws InvalidAttributeValueException;

   int getACLCacheTTLPositive();

   void setACLCacheTTLPositive(int var1) throws InvalidAttributeValueException;

   int getGroupCacheTTLPositive();

   void setGroupCacheTTLPositive(int var1) throws InvalidAttributeValueException;

   int getAuthenticationCacheTTLPositive();

   void setAuthenticationCacheTTLPositive(int var1) throws InvalidAttributeValueException;

   int getPermissionCacheTTLPositive();

   void setPermissionCacheTTLPositive(int var1) throws InvalidAttributeValueException;

   int getUserCacheTTLPositive();

   void setUserCacheTTLPositive(int var1) throws InvalidAttributeValueException;

   int getACLCacheTTLNegative();

   void setACLCacheTTLNegative(int var1) throws InvalidAttributeValueException;

   int getGroupCacheTTLNegative();

   void setGroupCacheTTLNegative(int var1) throws InvalidAttributeValueException;

   int getAuthenticationCacheTTLNegative();

   void setAuthenticationCacheTTLNegative(int var1) throws InvalidAttributeValueException;

   int getPermissionCacheTTLNegative();

   void setPermissionCacheTTLNegative(int var1) throws InvalidAttributeValueException;

   int getUserCacheTTLNegative();

   void setUserCacheTTLNegative(int var1) throws InvalidAttributeValueException;

   int getGroupMembershipCacheTTL();

   void setGroupMembershipCacheTTL(int var1) throws InvalidAttributeValueException;
}
