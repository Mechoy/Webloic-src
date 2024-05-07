package weblogic.security;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javax.security.auth.Subject;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

public final class Security {
   private static AuthenticatedSubject kernelID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static Object runAs(Subject var0, PrivilegedAction var1) {
      AuthenticatedSubject var2 = AuthenticatedSubject.getFromSubject(var0);
      return SecurityServiceManager.runAs(kernelID, var2, var1);
   }

   public static Object runAs(Subject var0, PrivilegedExceptionAction var1) throws PrivilegedActionException {
      AuthenticatedSubject var2 = AuthenticatedSubject.getFromSubject(var0);
      return SecurityServiceManager.runAs(kernelID, var2, var1);
   }

   public static Subject getCurrentSubject() {
      return SecurityServiceManager.getCurrentSubject(kernelID).getSubject();
   }
}
