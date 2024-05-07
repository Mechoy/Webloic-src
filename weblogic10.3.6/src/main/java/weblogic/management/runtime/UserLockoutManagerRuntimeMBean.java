package weblogic.management.runtime;

public interface UserLockoutManagerRuntimeMBean extends RuntimeMBean {
   long getUserLockoutTotalCount();

   long getInvalidLoginAttemptsTotalCount();

   long getLoginAttemptsWhileLockedTotalCount();

   long getInvalidLoginUsersHighCount();

   long getUnlockedUsersTotalCount();

   long getLockedUsersCurrentCount();

   boolean isLockedOut(String var1);

   void clearLockout(String var1);

   long getLastLoginFailure(String var1);

   long getLoginFailureCount(String var1);
}
