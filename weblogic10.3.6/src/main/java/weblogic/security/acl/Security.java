package weblogic.security.acl;

import java.security.AccessController;
import java.security.Principal;
import java.security.acl.Acl;
import java.security.acl.Permission;
import java.util.Set;
import weblogic.management.configuration.SecurityMBean;
import weblogic.management.provider.ManagementService;
import weblogic.rmi.spi.HostID;
import weblogic.security.SecurityLogger;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.acl.internal.AuthenticatedUser;
import weblogic.security.audit.Audit;
import weblogic.security.principal.RealmAdapterUser;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

/** @deprecated */
public final class Security {
   private static BasicRealm wlRealm;
   private static RealmProxy wlRealmProxy;
   private static final boolean verbose = false;
   static final String AUDIT_NAME = "Central Security";
   private static PasswordGuessing passwordguessing;
   private static AuthenticatedSubject kernelID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static long cum_user_lockout_count;
   private static long cum_invalid_login_count;
   private static long cum_locked_attempts_count;
   private static long high_invalid_login_users;
   private static long cum_user_unlock_count;
   private static long current_lock_count;

   public static BasicRealm getRealm() {
      return wlRealm;
   }

   public static boolean hasPermission(Principal var0, String var1, String var2, char var3) {
      return hasPermission("Central Security", var0, var1, var2, var3);
   }

   public static boolean hasPermission(String var0, Principal var1, String var2, String var3, char var4) {
      return hasPermission(var0, var1, var2, wlRealm.getPermission(var3), var4);
   }

   public static boolean hasPermission(Principal var0, String var1, Permission var2, char var3) {
      return hasPermission("Central Security", var0, var1, var2, var3);
   }

   public static boolean hasPermission(String var0, Principal var1, String var2, Permission var3, char var4) {
      User var5 = null;
      if (var1 instanceof RealmAdapterUser) {
         var5 = getRealm().getUser(var1.getName());
      }

      Acl var6 = wlRealm.getAcl(var2, var4);
      boolean var7 = var1 != null && var3 != null && var6 != null && var6.checkPermission((Principal)(var5 != null ? var5 : var1), var3);
      Audit.checkPermission(var0, var6, (Principal)(var5 != null ? var5 : var1), var3, var7);
      return var7;
   }

   public static boolean hasPermission(Principal var0, String var1, Permission var2, char var3, Acl var4) {
      return hasPermission("Central Security", var0, var1, var2, var3, var4);
   }

   public static boolean hasPermission(String var0, Principal var1, String var2, Permission var3, char var4, Acl var5) {
      boolean var6 = false;
      User var7 = null;
      if (var1 instanceof RealmAdapterUser) {
         var7 = getRealm().getUser(var1.getName());
      }

      Acl var8 = wlRealm.getAcl(var2, var4);
      if (var8 == null) {
         if (var5 == null) {
            var6 = true;
         }

         var8 = var5;
      }

      if (!var6) {
         var6 = var1 != null && var3 != null && var8.checkPermission((Principal)(var7 != null ? var7 : var1), var3);
      }

      Audit.checkPermission(var0, var8, (Principal)(var7 != null ? var7 : var1), var3, var6);
      return var6;
   }

   public static boolean hasPermission(String var0, Permission var1, char var2) {
      return hasPermission(getCurrentUser(), var0, (Permission)var1, var2);
   }

   public static boolean hasPermission(String var0, Permission var1, char var2, Acl var3) {
      return hasPermission(getCurrentUser(), var0, var1, var2, var3);
   }

   public static String getThreadCurrentUserName() {
      User var0 = getCurrentUser();
      return var0 != null ? var0.getName() : null;
   }

   public static User getCurrentUser() {
      AuthenticatedSubject var0 = SecurityServiceManager.getCurrentSubject(kernelID);
      if (SecurityServiceManager.isKernelIdentity(var0)) {
         return wlRealm.getUser(ManagementService.getRuntimeAccess(kernelID).getDomain().getSecurity().getSystemUser());
      } else {
         RealmAdapterUser var1 = null;
         Set var2 = var0.getPrincipals(RealmAdapterUser.class);
         Object[] var3 = var2.toArray();
         if (var3 != null && var3.length > 0) {
            var1 = (RealmAdapterUser)var3[0];
         }

         User var4 = null;
         if (wlRealm != null) {
            if (var1 == null) {
               return wlRealm.getUser("guest");
            }

            var4 = wlRealm.getUser(var1.getName());
            if (var4 == null) {
               var4 = new User(var1.getName());
            }
         }

         return var4;
      }
   }

   public static void checkPermission(Principal var0, String var1, Permission var2, char var3, Acl var4) throws SecurityException {
      checkPermission("Central Security", var0, var1, var2, var3, var4);
   }

   public static void checkPermission(String var0, Principal var1, String var2, Permission var3, char var4, Acl var5) throws SecurityException {
      if (!hasPermission(var0, var1, var2, var3, var4, var5)) {
         logAndThrow("User \"" + var1 + "\" does not have Permission \"" + var3 + "\" based on ACL \"" + var2 + "\".");
      }
   }

   public static void checkPermission(Principal var0, String var1, Permission var2, char var3) throws SecurityException {
      checkPermission("Central Security", var0, var1, var2, var3);
   }

   public static void checkPermission(String var0, Principal var1, String var2, Permission var3, char var4) throws SecurityException {
      SecurityMBean var5 = ManagementService.getRuntimeAccess(kernelID).getDomain().getSecurity();
      if (var5.getLogAllChecksEnabled()) {
         Acl var6 = wlRealm.getAcl(var2, var4);
         String var7 = var6 == null ? " on (unknown) ACL \"" + var2 + "\"" : "on ACL \"" + var6.toString() + "\"";
         SecurityLogger.logCheckUserPermissionInfo(var1.toString(), var3.toString(), var7);
      }

      if (!hasPermission(var0, var1, var2, var3, var4)) {
         logAndThrow("User \"" + var1 + "\" does not have Permission \"" + var3 + "\" based on ACL \"" + var2 + "\".");
      }
   }

   public static void checkPermission(String var0, Permission var1, char var2) throws SecurityException {
      checkPermission("Central Security", var0, var1, var2);
   }

   public static void checkPermission(String var0, String var1, Permission var2, char var3) throws SecurityException {
      checkPermission(var0, getCurrentUser(), var1, var2, var3);
   }

   public static UserInfo getUserInfo(String var0, Object var1) {
      return wlRealmProxy.createUserInfo(var0, var1);
   }

   public static User getUser(String var0, Object var1) {
      return wlRealm.getUser(getUserInfo(var0, var1));
   }

   public static void logAndThrow(String var0) throws SecurityException {
      SecurityException var1 = new SecurityException(var0);
      SecurityLogger.logAccessFailedInfo(Thread.currentThread().toString(), var1.toString());
      throw var1;
   }

   public static void init(BasicRealm var0) {
      wlRealm = var0;
      wlRealmProxy = RealmProxy.getRealmProxy(wlRealm.getName());
      weblogic.security.acl.internal.Security.init();
   }

   public static Object doAsPrivileged(UserInfo var0, PrivilegedAction var1) {
      if (var1 == null) {
         throw new NullPointerException("null action provided");
      } else {
         AuthenticatedUser var2 = weblogic.security.acl.internal.Security.authenticate(var0);
         if (var2 != null) {
            SecurityServiceManager.pushSubject(kernelID, SecurityServiceManager.getASFromAU(var2));
            Object var3 = null;

            try {
               var3 = var1.run();
            } finally {
               SecurityServiceManager.popSubject(kernelID);
            }

            return var3;
         } else {
            throw new SecurityException("Unable to authenticate " + var0);
         }
      }
   }

   public static Object doAsPrivileged(UserInfo var0, PrivilegedExceptionAction var1) throws Exception {
      if (var1 == null) {
         throw new NullPointerException("null action provided");
      } else {
         AuthenticatedUser var2 = weblogic.security.acl.internal.Security.authenticate(var0);
         if (var2 != null) {
            SecurityServiceManager.pushSubject(kernelID, SecurityServiceManager.getASFromAU(var2));
            Object var3 = null;

            try {
               var3 = var1.run();
            } finally {
               SecurityServiceManager.popSubject(kernelID);
            }

            return var3;
         } else {
            throw new SecurityException("Unable to authenticate " + var0);
         }
      }
   }

   public static Principal getPrincipal(String var0) {
      if (wlRealm instanceof CachingRealm) {
         return ((CachingRealm)wlRealm).getPrincipal(var0);
      } else {
         Object var1 = wlRealm.getGroup(var0);
         if (var1 == null) {
            var1 = wlRealm.getUser(var0);
         }

         return (Principal)var1;
      }
   }

   public static long getUserLockoutTotalCount() {
      return cum_user_lockout_count;
   }

   static void incrementUserLockoutTotalCount() {
      ++cum_user_lockout_count;
   }

   public static long getInvalidLoginAttemptsTotalCount() {
      return cum_invalid_login_count;
   }

   static void incrementInvalidLoginAttemptsTotalCount() {
      ++cum_invalid_login_count;
   }

   public static long getLoginAttemptsWhileLockedTotalCount() {
      return cum_locked_attempts_count;
   }

   static void incrementLoginAttemptsWhileLockedTotalCount() {
      ++cum_locked_attempts_count;
   }

   public static long getInvalidLoginUsersHighCount() {
      return high_invalid_login_users;
   }

   static void setInvalidLoginUsersHighCount(long var0) {
      high_invalid_login_users = var0;
   }

   public static long getUnlockedUsersTotalCount() {
      return cum_user_unlock_count;
   }

   static void incrementUnlockedUsersTotalCount() {
      ++cum_user_unlock_count;
   }

   public static long getLockedUsersCurrentCount() {
      return current_lock_count;
   }

   static void incrementLockedUsersCurrentCount() {
      ++current_lock_count;
   }

   static void decrementLockedUsersCurrentCount() {
      --current_lock_count;
   }

   public static PasswordGuessing getPasswordGuessing() {
      Class var0 = PasswordGuessing.class;
      synchronized(PasswordGuessing.class) {
         if (passwordguessing == null) {
            passwordguessing = (PasswordGuessing)SecurityServiceManager.runAs(kernelID, kernelID, new java.security.PrivilegedAction() {
               public Object run() {
                  return new PasswordGuessing();
               }
            });
         }
      }

      return passwordguessing;
   }

   public static void receiveSecurityMessage(HostID var0, SecurityMessage var1) {
      passwordguessing.processSecurityMessage(var1.nextSeqNo(), var1.record());
   }
}
