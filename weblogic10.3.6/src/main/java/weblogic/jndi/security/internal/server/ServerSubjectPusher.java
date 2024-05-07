package weblogic.jndi.security.internal.server;

import java.rmi.RemoteException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Hashtable;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.security.auth.login.LoginException;
import org.omg.CORBA.portable.ObjectImpl;
import weblogic.corba.j2ee.naming.ContextImpl;
import weblogic.corba.j2ee.naming.NameParser;
import weblogic.iiop.IIOPClient;
import weblogic.iiop.IIOPLogger;
import weblogic.iiop.spi.IORDelegate;
import weblogic.jndi.Environment;
import weblogic.jndi.internal.ExceptionTranslator;
import weblogic.jndi.security.SubjectPusher;
import weblogic.kernel.Kernel;
import weblogic.rmi.extensions.PortableRemoteObject;
import weblogic.rmi.internal.InitialReferenceConstants;
import weblogic.security.SimpleCallbackHandler;
import weblogic.security.acl.DefaultUserInfoImpl;
import weblogic.security.acl.SecurityService;
import weblogic.security.acl.UserInfo;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.acl.internal.AuthenticatedUser;
import weblogic.security.auth.login.PasswordCredential;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SubjectManagerImpl;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public final class ServerSubjectPusher implements SubjectPusher, InitialReferenceConstants {
   private static final boolean DEBUG = false;
   private static AuthenticatedSubject kernelId;
   private static final DebugCategory debugSecurity = Debug.getCategory("weblogic.jndi.security");

   public ServerSubjectPusher() {
      SubjectManagerImpl.ensureInitialized();
      kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }

   public final void pushSubject(Hashtable var1, Context var2) throws NamingException {
      Environment var3 = new Environment(var1);
      if (Kernel.isServer()) {
         serverPushSubject(var3, var2);
      } else {
         clientPushSubject(var3, var2);
      }

   }

   public final void popSubject() {
      SecurityServiceManager.popSubject(kernelId);
   }

   private static final void clientPushSubject(Environment var0, Context var1) throws NamingException {
      Object var2 = var0.getSecurityUser();
      if (var2 == null && var0.isClientCertAvailable()) {
         var2 = new DefaultUserInfoImpl((String)null, (Object)null);
      }

      if (var2 != null && var2 instanceof DefaultUserInfoImpl) {
         AuthenticatedSubject var3 = var0.getSecuritySubject();
         if (var3 == null) {
            ContextImpl var4 = (ContextImpl)var1;
            if (IIOPClient.isEnabled() && var4.getContext() != null && ((IORDelegate)((ObjectImpl)var4.getContext())._get_delegate()).getIOR().getProfile().useSAS()) {
               var3 = new AuthenticatedSubject();
               var3.getPrincipals().add(var2);
               if (debugSecurity.isEnabled()) {
                  IIOPLogger.logDebugSecurity("pushing user " + var2 + " using CSIv2");
               }

               addPasswordCreds((DefaultUserInfoImpl)var2, var3);
            } else {
               if (debugSecurity.isEnabled()) {
                  IIOPLogger.logDebugSecurity("authenticating " + var2);
               }

               Object var5 = var1.lookup("weblogic/security/SecurityManager");
               SecurityService var6 = (SecurityService)PortableRemoteObject.narrow(var5, SecurityService.class);

               try {
                  AuthenticatedUser var7 = var6.authenticate((UserInfo)var2);
                  var3 = SecurityServiceManager.getASFromAU(var7);
               } catch (RemoteException var8) {
                  throw ExceptionTranslator.toNamingException(var8);
               }
            }

            var0.setSecuritySubject(var3);
         }

         SecurityServiceManager.pushSubject(kernelId, var3);
         if (var1 instanceof ContextImpl) {
            ((ContextImpl)var1).enableLogoutOnClose();
         }
      }

   }

   private static final void serverPushSubject(Environment var0, Context var1) throws NamingException {
      UserInfo var2 = var0.getSecurityUser();
      AuthenticatedSubject var3 = null;
      if (debugSecurity.isEnabled()) {
         IIOPLogger.logDebugSecurity("pushing user: " + var2);
      }

      if (var2 != null && var2.getName() != null && var2.getName().length() != 0) {
         if ((var3 = var0.getSecuritySubject()) != null) {
            var3 = var0.getSecuritySubject();
         } else {
            try {
               if (var2 instanceof DefaultUserInfoImpl) {
                  DefaultUserInfoImpl var4 = (DefaultUserInfoImpl)var2;
                  if (var0.getProviderUrl() == "local://" || NameParser.getProtocol(var0.getProviderUrl()) == "tgiop") {
                     var3 = authenticateLocally(var4);
                  }

                  if (var4.getName() != null && var4.getPassword() != null) {
                     if (isLocalServer(var1)) {
                        var3 = authenticateLocally(var4);
                     } else {
                        if (var3 == null) {
                           var3 = new AuthenticatedSubject();
                           var3.getPrincipals().add(var2);
                        }

                        addPasswordCreds(var4, var3);
                     }
                  } else if (var3 == null) {
                     var3 = authenticateLocally(var4);
                  }
               }
            } catch (LoginException var6) {
               AuthenticationException var5 = new AuthenticationException();
               var5.setRootCause(var6);
               throw var5;
            }
         }
      }

      if (var3 != null) {
         var0.setSecuritySubject(var3);
         SecurityServiceManager.pushSubject(kernelId, var3);
         if (var1 instanceof ContextImpl) {
            ((ContextImpl)var1).enableLogoutOnClose();
         }
      }

   }

   private static boolean isLocalServer(Context var0) {
      if (!(var0 instanceof ContextImpl)) {
         return false;
      } else {
         ContextImpl var1 = (ContextImpl)var0;

         try {
            boolean var2 = var1.getContext() != null && ((IORDelegate)((ObjectImpl)var1.getContext())._get_delegate()).getIOR().isLocal();
            return var2;
         } catch (Throwable var3) {
            if (debugSecurity.isEnabled()) {
               IIOPLogger.logDebugSecurity("isLocalServer: false; Unable to determine whether is local or not; will treat it as remote. Exception: " + var3);
            }

            return false;
         }
      }
   }

   private static AuthenticatedSubject authenticateLocally(DefaultUserInfoImpl var0) throws LoginException {
      String var1 = "weblogicDEFAULT";
      PrincipalAuthenticator var2 = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(kernelId, var1, ServiceType.AUTHENTICATION);
      SimpleCallbackHandler var3 = new SimpleCallbackHandler(var0.getName(), var0.getPassword());
      return var2.authenticate(var3);
   }

   private static void addPasswordCreds(DefaultUserInfoImpl var0, final AuthenticatedSubject var1) {
      if (var0.getName() != null && var0.getPassword() != null) {
         if (debugSecurity.isEnabled()) {
            IIOPLogger.logDebugSecurity("adding password credentials to " + var0);
         }

         final PasswordCredential var2 = new PasswordCredential(var0.getName(), var0.getPassword());
         AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
               var1.getPrivateCredentials(ServerSubjectPusher.kernelId).add(var2);
               return null;
            }
         });
      }

   }

   static void p(String var0) {
      System.err.println("<ServerSecurityManager> " + var0);
   }
}
