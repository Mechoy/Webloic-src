package weblogic.security;

import java.security.AccessController;
import weblogic.management.ManagementException;
import weblogic.management.configuration.SecurityConfigurationMBean;
import weblogic.management.configuration.SecurityMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.RealmRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.ServerSecurityRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

public final class SecurityRuntime extends RuntimeMBeanDelegate implements ServerSecurityRuntimeMBean {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private RealmRuntimeMBean defaultRealmRuntime = new RealmRuntime(SecurityServiceManager.getDefaultRealmName(), this);

   private static RuntimeAccess getRuntimeAccess() {
      return ManagementService.getRuntimeAccess(kernelId);
   }

   public SecurityRuntime(SecurityConfigurationMBean var1, SecurityMBean var2) throws ManagementException {
      super(getRuntimeAccess().getServerName(), getRuntimeAccess().getServerRuntime(), true, var1);
      var2.setServerSecurityRuntime(this);
      getRuntimeAccess().getServerRuntime().setServerSecurityRuntime(this);
   }

   public RealmRuntimeMBean getDefaultRealmRuntime() {
      return this.defaultRealmRuntime;
   }

   public boolean isJACCEnabled() {
      return SecurityServiceManager.isJACCEnabled();
   }

   public long getUserLockoutTotalCount() {
      return weblogic.security.acl.Security.getUserLockoutTotalCount();
   }

   public long getInvalidLoginAttemptsTotalCount() {
      return weblogic.security.acl.Security.getInvalidLoginAttemptsTotalCount();
   }

   public long getLoginAttemptsWhileLockedTotalCount() {
      return weblogic.security.acl.Security.getLoginAttemptsWhileLockedTotalCount();
   }

   public long getInvalidLoginUsersHighCount() {
      return weblogic.security.acl.Security.getInvalidLoginUsersHighCount();
   }

   public long getUnlockedUsersTotalCount() {
      return weblogic.security.acl.Security.getUnlockedUsersTotalCount();
   }

   public long getLockedUsersCurrentCount() {
      return weblogic.security.acl.Security.getLockedUsersCurrentCount();
   }

   public boolean isLockedOut(String var1) {
      return weblogic.security.acl.Security.getPasswordGuessing().runtimeIsLocked(var1);
   }

   public void clearLockout(String var1) {
      weblogic.security.acl.Security.getPasswordGuessing().runtimeClearLockout(var1);
   }

   public long getLastLoginFailure(String var1) {
      return weblogic.security.acl.Security.getPasswordGuessing().getLastLoginFailure(var1);
   }

   public int getLoginFailureCount(String var1) {
      return weblogic.security.acl.Security.getPasswordGuessing().getLoginFailureCount(var1);
   }
}
