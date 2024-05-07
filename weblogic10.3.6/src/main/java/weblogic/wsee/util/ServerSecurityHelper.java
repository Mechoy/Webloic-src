package weblogic.wsee.util;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import javax.security.auth.login.LoginException;
import weblogic.security.SimpleCallbackHandler;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.AuthorizationManager;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;

public class ServerSecurityHelper {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final Map AUTHENTICATORS = new HashMap();

   public static final AuthenticatedSubject getCurrentSubject() {
      AuthenticatedSubject var0 = SecurityServiceManager.getCurrentSubject(KERNEL_ID);
      return var0;
   }

   public static AuthenticatedSubject assertIdentity(String var0, String var1, String var2) throws LoginException {
      SimpleCallbackHandler var3 = new SimpleCallbackHandler(var0, var1);
      return getPrincipalAuthenticator(var2).authenticate(var3);
   }

   public static AuthenticatedSubject assertIdentity(X509Certificate[] var0, String var1) throws LoginException {
      try {
         return getPrincipalAuthenticator(var1).assertIdentity("X.509", var0);
      } catch (SecurityException var3) {
         return null;
      } catch (ClassCastException var4) {
         return null;
      }
   }

   public static void assertAnonymousIdentity() {
      AuthenticatedSubject var0 = SubjectUtils.getAnonymousSubject();
      SecurityServiceManager.pushSubject(KERNEL_ID, var0);
   }

   public static void assertX509Identity(AuthenticatedSubject var0) {
      SecurityServiceManager.pushSubject(KERNEL_ID, var0);
   }

   private static PrincipalAuthenticator getPrincipalAuthenticator(String var0) {
      if (var0 == null) {
         var0 = "weblogicDEFAULT";
      }

      PrincipalAuthenticator var1 = (PrincipalAuthenticator)AUTHENTICATORS.get(var0);
      if (var1 == null) {
         var1 = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(KERNEL_ID, var0, ServiceType.AUTHENTICATION);
         AUTHENTICATORS.put(var0, var1);
      }

      return var1;
   }

   public static final AuthorizationManager getAuthManager(String var0) {
      return (AuthorizationManager)SecurityServiceManager.getSecurityService(KERNEL_ID, var0, ServiceType.AUTHORIZE);
   }

   public static final void authenticatedInvoke(AuthenticatedSubject var0, PrivilegedExceptionAction var1) throws IOException {
      try {
         SecurityServiceManager.runAs(KERNEL_ID, var0, var1);
      } catch (PrivilegedActionException var3) {
         if (var3.getException() instanceof IOException) {
            throw (IOException)var3.getException();
         } else {
            throw new UndeclaredThrowableException(var3.getException());
         }
      }
   }
}
