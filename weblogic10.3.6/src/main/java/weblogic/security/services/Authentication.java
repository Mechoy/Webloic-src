package weblogic.security.services;

import java.io.Serializable;
import java.security.AccessController;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import weblogic.security.SecurityLogger;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.AdminResource;
import weblogic.security.service.AppContextHandler;
import weblogic.security.service.AuthorizationManager;
import weblogic.security.service.ChallengeContext;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.InvalidParameterException;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.shared.LoggerWrapper;
import weblogic.security.spi.IdentityAssertionException;

public final class Authentication {
   private static LoggerWrapper log = LoggerWrapper.getInstance("SecurityAtn");
   private static AuthenticatedSubject kernelID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static Subject login(CallbackHandler var0) throws LoginException {
      return login((CallbackHandler)var0, (AppContext)null);
   }

   public static Subject login(CallbackHandler var0, AppContext var1) throws LoginException {
      PrincipalAuthenticator var2 = SecurityServiceManager.getPrincipalAuthenticator(kernelID, "weblogicDEFAULT");
      return doLogin(var0, var1, var2);
   }

   public static Subject login(String var0, CallbackHandler var1) throws LoginException {
      return login(var0, var1, (AppContext)null);
   }

   public static Subject login(String var0, CallbackHandler var1, AppContext var2) throws LoginException {
      PrincipalAuthenticator var3 = SecurityServiceManager.getPrincipalAuthenticator(kernelID, var0);
      return doLogin(var1, var2, var3);
   }

   private static Subject doLogin(CallbackHandler var0, AppContext var1, PrincipalAuthenticator var2) throws LoginException {
      if (var2 == null) {
         throw new InvalidParameterException(SecurityLogger.getSecurityServiceUnavailable());
      } else {
         AppContextHandler var3 = AppContextHandler.getInstance(var1);
         AuthenticatedSubject var4 = var2.authenticate(var0, var3);
         return var4.getSubject();
      }
   }

   public static Subject assertIdentity(String var0, Object var1) throws LoginException {
      return assertIdentity(var0, (Object)var1, (AppContext)null);
   }

   public static Subject assertIdentity(String var0, Object var1, AppContext var2) throws LoginException {
      return doAssertIdentity(var0, var1, var2, "weblogicDEFAULT");
   }

   public static Subject assertIdentity(String var0, String var1, Object var2) throws LoginException {
      return assertIdentity(var0, var1, var2, (AppContext)null);
   }

   public static Subject assertIdentity(String var0, String var1, Object var2, AppContext var3) throws LoginException {
      return doAssertIdentity(var1, var2, var3, var0);
   }

   private static Subject doAssertIdentity(String var0, Object var1, AppContext var2, String var3) throws LoginException {
      PrincipalAuthenticator var4 = SecurityServiceManager.getPrincipalAuthenticator(kernelID, var3);
      if (var4 == null) {
         throw new InvalidParameterException("Security Service Unavailable");
      } else {
         AuthorizationManager var5 = SecurityServiceManager.getAuthorizationManager(kernelID, var3);
         if (var5 != null) {
            AuthenticatedSubject var6 = SecurityServiceManager.getCurrentSubject(kernelID);
            AdminResource var7 = new AdminResource("IdentityAssertion", var3, "assertIdentity");
            if (log.isDebugEnabled()) {
               log.debug(" isAccessAllowed:  checking Permission for: '" + var7 + "', currentSubject: '" + SubjectUtils.displaySubject(var6) + "'");
            }

            if (!var5.isAccessAllowed(var6, var7, (ContextHandler)null)) {
               if (log.isDebugEnabled()) {
                  log.debug(" isAccessAllowed:  currentSubject: " + var6 + " does not have permission to assert identity of type " + var0 + " in realm " + var3);
               }

               throw new SecurityException(" isAccessAllowed:  currentSubject: " + var6 + " does not have permission to assert identity of type " + var0 + " in realm " + var3);
            } else {
               AppContextHandler var8 = AppContextHandler.getInstance(var2);
               AuthenticatedSubject var9 = var4.assertIdentity(var0, var1, var8);
               return var9.getSubject();
            }
         } else {
            throw new SecurityException("Security Service Unavailable");
         }
      }
   }

   public Object getChallengeToken(String var1, AppContext var2) throws LoginException {
      PrincipalAuthenticator var3 = SecurityServiceManager.getPrincipalAuthenticator(kernelID, "weblogicDEFAULT");
      if (var3 == null) {
         throw new InvalidParameterException("Security Service Unavailable");
      } else {
         Object var4 = null;
         AppContextHandler var5 = AppContextHandler.getInstance(var2);

         try {
            var4 = var3.getChallengeToken(var1, var5);
         } catch (IdentityAssertionException var7) {
            new LoginException(var7.getMessage());
         }

         return var4;
      }
   }

   public void continueChallengeIdentity(AppChallengeContext var1, String var2, Object var3, AppContext var4) throws LoginException {
      PrincipalAuthenticator var5 = SecurityServiceManager.getPrincipalAuthenticator(kernelID, "weblogicDEFAULT");
      if (var5 == null) {
         throw new InvalidParameterException("Security Service Unavailable");
      } else {
         AppContextHandler var6 = AppContextHandler.getInstance(var4);
         ChallengeContext var7 = ((AppChallengeContextImpl)var1).getChallengeContext();
         var5.continueChallengeIdentity(var7, var2, var3, var6);
      }
   }

   public AppChallengeContext assertChallengeIdentity(String var1, Object var2, AppContext var3) throws LoginException {
      String var4 = "weblogicDEFAULT";
      PrincipalAuthenticator var5 = SecurityServiceManager.getPrincipalAuthenticator(kernelID, var4);
      if (var5 == null) {
         throw new InvalidParameterException("Security Service Unavailable");
      } else {
         AuthorizationManager var6 = SecurityServiceManager.getAuthorizationManager(kernelID, var4);
         if (var6 != null) {
            AuthenticatedSubject var7 = SecurityServiceManager.getCurrentSubject(kernelID);
            AdminResource var8 = new AdminResource("IdentityAssertion", var4, "assertIdentity");
            if (log.isDebugEnabled()) {
               log.debug(" isAccessAllowed:  checking Permission for: '" + var8 + "', currentSubject: '" + SubjectUtils.displaySubject(var7) + "'");
            }

            if (!var6.isAccessAllowed(var7, var8, (ContextHandler)null)) {
               if (log.isDebugEnabled()) {
                  log.debug(" isAccessAllowed:  currentSubject: " + var7 + " does not have permission to assert identity of type " + var1 + " in realm " + var4);
               }

               throw new SecurityException(" isAccessAllowed:  currentSubject: " + var7 + " does not have permission to assert identity of type " + var1 + " in realm " + var4);
            } else {
               AppContextHandler var9 = AppContextHandler.getInstance(var3);
               ChallengeContext var10 = var5.assertChallengeIdentity(var1, var2, var9);
               return new AppChallengeContextImpl(var10, log);
            }
         } else {
            throw new SecurityException("Security Service Unavailable");
         }
      }
   }

   private final class AppChallengeContextImpl implements AppChallengeContext, Serializable {
      private static final long serialVersionUID = 5847810460507567453L;
      private ChallengeContext challengeContext;
      private LoggerWrapper log = null;

      public AppChallengeContextImpl(ChallengeContext var2, LoggerWrapper var3) {
         this.challengeContext = var2;
         this.log = var3;
      }

      public boolean hasChallengeIdentityCompleted() {
         return this.challengeContext.hasChallengeIdentityCompleted();
      }

      public Subject getAuthenticatedSubject() {
         if (!this.hasChallengeIdentityCompleted()) {
            throw new IllegalStateException(SecurityLogger.getChallengeNotCompleted());
         } else {
            AuthenticatedSubject var1 = this.challengeContext.getAuthenticatedSubject();
            return var1 != null ? var1.getSubject() : null;
         }
      }

      public Object getChallengeToken() {
         if (this.hasChallengeIdentityCompleted()) {
            throw new IllegalStateException(SecurityLogger.getChallengeHasCompleted());
         } else {
            return this.challengeContext.getChallengeToken();
         }
      }

      public ChallengeContext getChallengeContext() {
         return this.challengeContext;
      }
   }
}
