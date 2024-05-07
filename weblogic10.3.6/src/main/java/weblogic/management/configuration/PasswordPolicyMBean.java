package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;

/** @deprecated */
public interface PasswordPolicyMBean extends ConfigurationMBean {
   int getMinimumPasswordLength();

   void setMinimumPasswordLength(int var1) throws InvalidAttributeValueException;

   boolean isLockoutEnabled();

   void setLockoutEnabled(boolean var1);

   int getLockoutThreshold();

   void setLockoutThreshold(int var1) throws InvalidAttributeValueException;

   int getLockoutDuration();

   void setLockoutDuration(int var1) throws InvalidAttributeValueException;

   int getLockoutResetDuration();

   void setLockoutResetDuration(int var1) throws InvalidAttributeValueException;

   int getLockoutCacheSize();

   void setLockoutCacheSize(int var1) throws InvalidAttributeValueException;

   int getLockoutGCThreshold();

   void setLockoutGCThreshold(int var1) throws InvalidAttributeValueException;
}
