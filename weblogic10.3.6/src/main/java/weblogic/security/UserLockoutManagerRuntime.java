package weblogic.security;

import weblogic.management.ManagementException;
import weblogic.management.runtime.RealmRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.UserLockoutManagerRuntimeMBean;
import weblogic.security.service.UserLockoutManager;

public final class UserLockoutManagerRuntime extends RuntimeMBeanDelegate implements UserLockoutManagerRuntimeMBean {
   private UserLockoutManager userLockoutManager;

   public UserLockoutManagerRuntime(UserLockoutManager var1, RealmRuntimeMBean var2) throws ManagementException {
      super(var1.getName(), var2);
      this.userLockoutManager = var1;
   }

   public long getUserLockoutTotalCount() {
      return this.userLockoutManager.getUserLockoutTotalCount();
   }

   public long getInvalidLoginAttemptsTotalCount() {
      return this.userLockoutManager.getInvalidLoginAttemptsTotalCount();
   }

   public long getLoginAttemptsWhileLockedTotalCount() {
      return this.userLockoutManager.getLoginAttemptsWhileLockedTotalCount();
   }

   public long getInvalidLoginUsersHighCount() {
      return this.userLockoutManager.getInvalidLoginUsersHighCount();
   }

   public long getUnlockedUsersTotalCount() {
      return this.userLockoutManager.getUnlockedUsersTotalCount();
   }

   public long getLockedUsersCurrentCount() {
      return this.userLockoutManager.getLockedUsersCurrentCount();
   }

   public boolean isLockedOut(String var1) {
      return this.userLockoutManager.isLockedOut(var1);
   }

   public void clearLockout(String var1) {
      this.userLockoutManager.clearLockout(var1);
   }

   public long getLastLoginFailure(String var1) {
      return this.userLockoutManager.getLastLoginFailure(var1);
   }

   public long getLoginFailureCount(String var1) {
      return this.userLockoutManager.getLoginFailureCount(var1);
   }
}
