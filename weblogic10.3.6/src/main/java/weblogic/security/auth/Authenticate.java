package weblogic.security.auth;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;
import weblogic.jndi.Environment;
import weblogic.jndi.internal.ThreadEnvironment;
import weblogic.kernel.Kernel;
import weblogic.protocol.Protocol;
import weblogic.protocol.ProtocolManager;
import weblogic.rjvm.RJVM;
import weblogic.rjvm.RJVMManager;
import weblogic.rjvm.ServerURL;
import weblogic.rjvm.t3.ProtocolHandlerT3;
import weblogic.security.acl.DefaultUserInfoImpl;
import weblogic.security.acl.UserInfo;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.acl.internal.AuthenticatedUser;
import weblogic.security.acl.internal.Security;
import weblogic.security.auth.login.PasswordCredential;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.AssertionError;

public final class Authenticate {
   public static void authenticate(Environment var0, final Subject var1) throws LoginException, IOException, RemoteException {
      String var2 = var0.getProviderUrl();
      RJVM var3 = null;
      if (var2 != null && var2.length() != 0 && var2 != "local://") {
         ThreadEnvironment.push(var0);

         try {
            var3 = (new ServerURL(var2)).findOrCreateRJVM(var0.getProviderChannel());
         } finally {
            ThreadEnvironment.pop();
         }
      } else {
         if (!Kernel.isServer()) {
            return;
         }

         var3 = RJVMManager.getLocalRJVM();
         var0.setProperty("java.naming.provider.url", (Object)null);
      }

      Object var4 = var0.getSecurityUser();
      AuthenticatedUser var5 = null;
      boolean var6 = var3.equals(RJVMManager.getLocalRJVM());
      if (var4 == null && var0.isClientCertAvailable()) {
         var4 = new DefaultUserInfoImpl((String)null, (Object)null);
      }

      if (var4 != null) {
         ThreadEnvironment.push(var0);

         try {
            var5 = Security.authenticate((UserInfo)var4, var3, getProtocol(var0), var0.getProviderChannel());
         } catch (SecurityException var21) {
            String var8 = var21.toString();
            int var9 = var8.indexOf("Start server side stack trace:");
            if (var9 > 0) {
               var8 = var8.substring(0, var9 - 1);
            }

            throw new LoginException(var8);
         } finally {
            ThreadEnvironment.pop();
         }

         AuthenticatedSubject var7 = SecurityServiceManager.getASFromAU(var5);
         var0.setSecuritySubject(var7);

         try {
            if (Boolean.getBoolean("weblogic.security.authenticatePushSubject")) {
               AuthenticatedSubject var23 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
               SecurityServiceManager.pushSubject(var23, var7);
            }
         } catch (SecurityException var19) {
         }

         var1.getPrincipals().addAll(var7.getPrincipals());
         if (var4 instanceof DefaultUserInfoImpl) {
            DefaultUserInfoImpl var24 = (DefaultUserInfoImpl)var4;
            if (var24.getName() != null && var24.getPassword() != null) {
               final PasswordCredential var25 = new PasswordCredential(var24.getName(), var24.getPassword());
               AccessController.doPrivileged(new PrivilegedAction() {
                  public Object run() {
                     var1.getPrivateCredentials().add(var25);
                     return null;
                  }
               });
            }

            var0.setSecurityUser(var5);
            if (!Kernel.isServer() && var5 != null) {
               var3.setUser(var5);
            }
         }
      }

   }

   private static Protocol getProtocol(Environment var0) {
      try {
         String var1 = var0.getProviderUrl();
         Protocol var2 = null;
         if (var1 == "local://") {
            var2 = ProtocolHandlerT3.PROTOCOL_T3;
         } else {
            var2 = ProtocolManager.getProtocolByName((new ServerURL(var1)).getProtocol());
         }

         return var2;
      } catch (MalformedURLException var3) {
         throw new AssertionError(var3);
      }
   }

   public static void logout(Subject var0) throws LoginException, IOException, RemoteException {
      Set var1 = var0.getPrincipals();
      var1.clear();
      var1 = var0.getPrivateCredentials();
      var1.clear();
      var1 = var0.getPublicCredentials();
      var1.clear();
   }
}
