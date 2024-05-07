package weblogic.diagnostics.utils;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import weblogic.logging.Loggable;
import weblogic.management.ManagementLogger;
import weblogic.management.NoAccessRuntimeException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.AdminResource;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.RoleManager;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;

public class SecurityHelper {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static RoleManager roleManager;
   private static AdminResource adminMBeanResource = new AdminResource("Configuration", (String)null, (String)null);
   private static final String ADMIN_ROLENAME = "Admin";

   private SecurityHelper() {
   }

   public static void checkForAdminRole() {
      checkForRole("Admin");
   }

   public static void checkForRole(final String var0) {
      AuthenticatedSubject var1 = SecurityServiceManager.getCurrentSubject(KERNEL_ID);
      if (!SecurityServiceManager.isKernelIdentity(var1)) {
         final AuthenticatedSubject var2 = SecurityServiceManager.seal(KERNEL_ID, var1);
         Boolean var3 = (Boolean)SecurityServiceManager.runAs(KERNEL_ID, KERNEL_ID, new PrivilegedAction() {
            public Object run() {
               Map var1 = SecurityHelper.getRoleManager().getRoles(var2, SecurityHelper.adminMBeanResource, (ContextHandler)null);
               return var1 == null || var1.get("Admin") == null && var1.get(var0) == null ? Boolean.FALSE : Boolean.TRUE;
            }
         });
         if (!var3) {
            Loggable var4 = ManagementLogger.logNoAccessForSubjectRoleLoggable(var2.toString(), var0);
            throw new NoAccessRuntimeException(var4.getMessage());
         }
      }
   }

   private static RoleManager getRoleManager() {
      return roleManager != null ? roleManager : (roleManager = (RoleManager)SecurityServiceManager.getSecurityService(KERNEL_ID, "weblogicDEFAULT", ServiceType.ROLE));
   }
}
