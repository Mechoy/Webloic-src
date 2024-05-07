package weblogic.management.mbeanservers.internal;

import java.io.IOException;
import java.security.AccessController;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import weblogic.management.context.JMXContext;
import weblogic.management.context.JMXContextHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

public class JMXAuthenticator implements javax.management.remote.JMXAuthenticator {
   private PrincipalAuthenticator authenticator;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   protected JMXAuthenticator() {
      this.authenticator = SecurityServiceManager.getPrincipalAuthenticator(kernelId, "weblogicDEFAULT");
   }

   public Subject authenticate(Object var1) {
      try {
         if (var1 == null) {
            AuthenticatedSubject var6 = SecurityServiceManager.getCurrentSubject(kernelId);
            if (System.getSecurityManager() != null && SecurityServiceManager.isKernelIdentity(var6)) {
               JMXContext var7 = JMXContextHelper.getJMXContext(false);
               if (var7 != null) {
                  Subject var8 = var7.getSubject();
                  if (var8 != null && SecurityServiceManager.isKernelIdentity(SecurityServiceManager.getASFromWire(AuthenticatedSubject.getFromSubject(var8)))) {
                     return var6.getSubject();
                  }
               }
            }

            return AuthenticatedSubject.ANON.getSubject();
         } else if (!(var1 instanceof String[])) {
            throw new SecurityException("Invalid JMX credential type passed to JMX connector: " + var1.getClass().getName());
         } else {
            String[] var2 = (String[])((String[])var1);
            if (var2.length >= 2 && var2[0] != null && var2[1] != null) {
               AuthenticatedSubject var3 = this.authenticator.authenticate(new JMXCallbackHandler((String[])((String[])var1)));
               AuthenticatedSubject var4 = SecurityServiceManager.getCurrentSubject(kernelId);
               return var4.equals(var3) && var4.getQOS() == var3.getQOS() ? var4.getSubject() : var3.getSubject();
            } else {
               throw new SecurityException("Invalid JMX credential, empty username and/or password");
            }
         }
      } catch (LoginException var5) {
         throw new SecurityException(var5);
      }
   }

   private class JMXCallbackHandler implements CallbackHandler {
      String userName;
      String password;

      protected JMXCallbackHandler(String[] var2) {
         this.userName = var2[0];
         this.password = var2[1];
      }

      public void handle(Callback[] var1) throws IOException, UnsupportedCallbackException {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            Callback var3 = var1[var2];
            if (var3 instanceof NameCallback) {
               ((NameCallback)var3).setName(this.userName);
            } else {
               if (!(var3 instanceof PasswordCallback)) {
                  throw new UnsupportedCallbackException(var1[var2], "Unrecognized Callback");
               }

               ((PasswordCallback)var3).setPassword(this.password.toCharArray());
            }
         }

      }
   }
}
