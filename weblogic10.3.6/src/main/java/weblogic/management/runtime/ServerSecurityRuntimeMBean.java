package weblogic.management.runtime;

public interface ServerSecurityRuntimeMBean extends RuntimeMBean {
   RealmRuntimeMBean getDefaultRealmRuntime();

   boolean isJACCEnabled();

   long getUserLockoutTotalCount();

   long getInvalidLoginAttemptsTotalCount();

   long getLoginAttemptsWhileLockedTotalCount();

   long getInvalidLoginUsersHighCount();

   long getUnlockedUsersTotalCount();

   long getLockedUsersCurrentCount();

   boolean isLockedOut(String var1);

   void clearLockout(String var1);

   long getLastLoginFailure(String var1);

   int getLoginFailureCount(String var1);
}
