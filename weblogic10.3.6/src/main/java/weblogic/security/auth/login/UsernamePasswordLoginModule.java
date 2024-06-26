package weblogic.security.auth.login;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import weblogic.jndi.Environment;
import weblogic.security.SecurityLogger;
import weblogic.security.auth.Authenticate;
import weblogic.security.auth.callback.URLCallback;
import weblogic.security.shared.LoggerWrapper;

public class UsernamePasswordLoginModule implements LoginModule {
   private Subject subject;
   private CallbackHandler callbackHandler = null;
   private Map sharedState = null;
   private Map options = null;
   private static LoggerWrapper log = LoggerWrapper.getInstance("SecurityAtn");
   private String url = null;
   private boolean succeeded = false;
   private boolean commitSucceeded = false;
   private String username = null;
   private String password = null;

   public void initialize(Subject var1, CallbackHandler var2, Map var3, Map var4) {
      this.callbackHandler = var2;
      this.sharedState = var3;
      this.options = var4;
      this.subject = var1;
      if (var4 != null) {
         Object var5 = var4.get("debug");
         if (var5 != null && ((String)var5).equalsIgnoreCase("true")) {
            this.log("UsernamePasswordLoginModule.initialize(), debug enabled");
         }

         var5 = var4.get("URL");
         if (var5 != null) {
            this.url = (String)var5;
            this.log("UsernamePasswordLoginModule.initialize(), URL " + this.url);
         }
      }

   }

   public boolean login() throws LoginException {
      if (this.callbackHandler == null) {
         this.log("UsernamePasswordLoginModule.login(), no callback handler specifed");
         throw new LoginException(SecurityLogger.getNoCallbackHandlerSpecified());
      } else {
         Callback[] var1 = new Callback[]{new NameCallback("username: "), new PasswordCallback("password: ", false), new URLCallback("URL: ")};

         try {
            this.callbackHandler.handle(var1);
            this.username = ((NameCallback)var1[0]).getName();
            if (log.isDebugEnabled()) {
               if (this.username == null) {
                  this.log("UsernamePasswordLoginModule.login(), No username");
               } else {
                  this.log("UsernamePasswordLoginModule.login(), username " + this.username);
               }
            }

            if (this.username == null) {
               throw new LoginException(SecurityLogger.getNoUsernameSpecified());
            }

            char[] var2 = ((PasswordCallback)var1[1]).getPassword();
            if (var2 == null) {
               var2 = new char[0];
            }

            this.password = new String(var2);
            String var3 = ((URLCallback)var1[2]).getURL();
            if (var3 != null) {
               this.url = var3;
            }

            if (log.isDebugEnabled()) {
               if (this.url == null) {
                  this.log("UsernamePasswordLoginModule.login(), No URL");
               } else {
                  this.log("UsernamePasswordLoginModule.login(), URL " + this.url);
               }
            }

            if (this.url == null) {
               this.url = "";
            }
         } catch (IOException var7) {
            this.log("UsernamePasswordLoginModule CallbackHandler Error: " + var7.getMessage());
            throw new LoginException(var7.toString());
         } catch (UnsupportedCallbackException var8) {
            this.log("UsernamePasswordLoginModule CallbackHandler Error: " + var8.getMessage());
            throw new LoginException(SecurityLogger.getErrorCallbackNotAvailable(var8.getCallback().toString()));
         }

         if (this.url != null) {
            Environment var9 = new Environment();
            var9.setProviderUrl(this.url);
            var9.setSecurityPrincipal(this.username);
            var9.setSecurityCredentials(this.password);

            try {
               Authenticate.authenticate(var9, this.subject);
            } catch (RemoteException var4) {
               this.log("UsernamePasswordLoginModule Error: Remote Exception on authenticate, " + var4.getMessage());
               throw new LoginException(var4.toString());
            } catch (IOException var5) {
               this.log("UsernamePasswordLoginModule Error: IO Exception on authenticate, " + var5.getMessage());
               throw new LoginException(var5.toString());
            } catch (LoginException var6) {
               this.log("UsernamePasswordLoginModule Error: Login Exception on authenticate, " + var6.getMessage());
               throw new LoginException(var6.toString());
            }
         }

         this.succeeded = true;
         return this.succeeded;
      }
   }

   public boolean commit() throws LoginException {
      if (this.succeeded) {
         final PasswordCredential var1 = new PasswordCredential(this.username, this.password);
         AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
               UsernamePasswordLoginModule.this.subject.getPrivateCredentials().add(var1);
               return null;
            }
         });
         this.url = null;
         this.commitSucceeded = true;
         return true;
      } else {
         this.username = null;
         this.password = null;
         this.url = null;
         return false;
      }
   }

   public boolean abort() throws LoginException {
      if (!this.succeeded) {
         return false;
      } else {
         if (this.succeeded && !this.commitSucceeded) {
            this.succeeded = false;
            this.username = null;
            this.password = null;
            this.url = null;
         } else {
            this.logout();
         }

         return true;
      }
   }

   public boolean logout() throws LoginException {
      this.succeeded = false;
      this.commitSucceeded = false;
      this.username = null;
      this.password = null;
      this.url = null;

      try {
         Authenticate.logout(this.subject);
      } catch (LoginException var2) {
      } catch (IOException var3) {
      }

      return true;
   }

   private void log(String var1) {
      if (log != null) {
         log.debug(var1);
      } else {
         System.out.println(var1);
      }

   }
}
