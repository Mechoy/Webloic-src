package weblogic.management.security.authentication;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.commo.StandardInterface;
import weblogic.management.security.RealmMBean;

public interface UserLockoutManagerMBean extends StandardInterface, DescriptorBean {
   /** @deprecated */
   long getUserLockoutTotalCount();

   /** @deprecated */
   long getInvalidLoginAttemptsTotalCount();

   /** @deprecated */
   long getLoginAttemptsWhileLockedTotalCount();

   /** @deprecated */
   long getInvalidLoginUsersHighCount();

   /** @deprecated */
   long getUnlockedUsersTotalCount();

   /** @deprecated */
   long getLockedUsersCurrentCount();

   RealmMBean getRealm();

   boolean isLockoutEnabled();

   void setLockoutEnabled(boolean var1) throws InvalidAttributeValueException;

   long getLockoutThreshold();

   void setLockoutThreshold(long var1) throws InvalidAttributeValueException;

   long getLockoutDuration();

   void setLockoutDuration(long var1) throws InvalidAttributeValueException;

   long getLockoutResetDuration();

   void setLockoutResetDuration(long var1) throws InvalidAttributeValueException;

   long getLockoutCacheSize();

   void setLockoutCacheSize(long var1) throws InvalidAttributeValueException;

   long getLockoutGCThreshold();

   void setLockoutGCThreshold(long var1) throws InvalidAttributeValueException;

   /** @deprecated */
   boolean isLockedOut(String var1);

   /** @deprecated */
   void clearLockout(String var1);

   /** @deprecated */
   long getLastLoginFailure(String var1);

   /** @deprecated */
   long getLoginFailureCount(String var1);

   String getName();

   void setName(String var1) throws InvalidAttributeValueException;

   String getCompatibilityObjectName();
}
