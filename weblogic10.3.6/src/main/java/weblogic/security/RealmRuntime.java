package weblogic.security;

import java.security.AccessController;
import weblogic.management.ManagementException;
import weblogic.management.runtime.RealmRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.ServerSecurityRuntimeMBean;
import weblogic.management.runtime.UserLockoutManagerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.InvalidParameterException;
import weblogic.security.service.NotYetInitializedException;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.UserLockoutManager;

public final class RealmRuntime extends RuntimeMBeanDelegate implements RealmRuntimeMBean {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private UserLockoutManagerRuntimeMBean userLockoutManagerRuntime = null;

   public RealmRuntime(String var1, ServerSecurityRuntimeMBean var2) throws ManagementException {
      super(var1, var2, true, "DefaultRealmRuntime");

      try {
         PrincipalAuthenticator var3 = SecurityServiceManager.getPrincipalAuthenticator(kernelId, var1);
         UserLockoutManager var4 = var3.getUserLockoutManager();
         if (var4.isLockoutEnabled()) {
            this.userLockoutManagerRuntime = new UserLockoutManagerRuntime(var4, this);
         }
      } catch (InvalidParameterException var5) {
      } catch (NotYetInitializedException var6) {
         throw new AssertionError(var6);
      }

   }

   public UserLockoutManagerRuntimeMBean getUserLockoutManagerRuntime() {
      return this.userLockoutManagerRuntime;
   }
}
