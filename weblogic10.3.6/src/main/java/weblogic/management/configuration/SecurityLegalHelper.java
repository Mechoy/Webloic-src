package weblogic.management.configuration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import javax.security.auth.login.LoginException;
import weblogic.kernel.Kernel;
import weblogic.security.SecurityLogger;
import weblogic.security.SecurityService;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.net.ConnectionFilter;
import weblogic.security.net.ConnectionFilterRulesListener;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;

public final class SecurityLegalHelper {
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static boolean isLegalFilterRules(SecurityMBean var0, String[] var1) {
      return isLegalFilterRules(var0.getConnectionFilter(), var1);
   }

   public static boolean isLegalFilterRules(SecurityConfigurationMBean var0, String[] var1) {
      return isLegalFilterRules(var0.getConnectionFilter(), var1);
   }

   private static boolean isLegalFilterRules(String var0, String[] var1) {
      if (var0 != null && SecurityService.getConnectionFilterEnabled()) {
         ConnectionFilter var2 = SecurityService.getConnectionFilter();

         try {
            Class var3 = Class.forName(var0);
            if (ConnectionFilterRulesListener.class.isAssignableFrom(var3)) {
               try {
                  String var10 = "checkRules";
                  Class[] var11 = new Class[]{String[].class};
                  Method var12 = var3.getMethod(var10, var11);
                  Object[] var7 = new Object[]{var1};
                  var12.invoke(var2, var7);
               } catch (InvocationTargetException var8) {
                  Throwable var5 = var8.getTargetException();
                  if (var5.toString().startsWith("java.text.ParseException")) {
                     String var6 = var5.getMessage();
                     SecurityLogger.logUpdateFilterWarn(var6);
                     throw new IllegalArgumentException(var6 + "  Rules will not be updated.");
                  }

                  throw var8;
               }
            }
         } catch (Throwable var9) {
            IllegalArgumentException var4 = new IllegalArgumentException("problem with connection filter. Exception:" + var9);
            var4.initCause(var9);
            throw var4;
         }
      }

      return true;
   }

   public static void validateSecurity(SecurityMBean var0) throws IllegalArgumentException {
      boolean var1 = isLegalFilterRules(var0, var0.getConnectionFilterRules());
      if (!var1) {
         throw new IllegalArgumentException("ConnectionFilterRules string is not valid");
      }
   }

   public static void validateSecurityConfiguration(SecurityConfigurationMBean var0) throws IllegalArgumentException {
      boolean var1 = isLegalFilterRules(var0, var0.getConnectionFilterRules());
      if (!var1) {
         throw new IllegalArgumentException("ConnectionFilterRules string is not valid");
      }
   }

   public static void validatePrincipalName(String var0) throws IllegalArgumentException {
      try {
         if (Kernel.isServer()) {
            AuthenticatedSubject var1 = SecurityServiceManager.getCurrentSubject(kernelId);
            if (!SubjectUtils.isUserAnAdministrator(var1)) {
               PrincipalAuthenticator var2 = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(kernelId, SecurityServiceManager.getDefaultRealmName(), ServiceType.AUTHENTICATION);
               AuthenticatedSubject var3 = var2.impersonateIdentity(var0);
               if (SubjectUtils.isUserAnAdministrator(var3)) {
                  throw new IllegalArgumentException("The principal name : " + var0 + " has higher privileges than the current user: " + var1 + ". Hence the current user cannot set the principal name." + " Modify the principal name with admin privileged user.");
               }
            }
         }
      } catch (LoginException var4) {
         throw new IllegalArgumentException("Invalid principal name: " + var0, var4);
      } catch (Exception var5) {
         throw new IllegalArgumentException("Invalid principal name: " + var0, var5);
      }
   }
}
