package weblogic.common.internal;

import java.rmi.RemoteException;
import java.security.AccessController;
import java.security.cert.X509Certificate;
import javax.security.auth.login.LoginException;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.rmi.spi.InboundRequest;
import weblogic.security.SimpleCallbackHandler;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.DefaultUserInfoImpl;
import weblogic.security.acl.SecurityService;
import weblogic.security.acl.UserInfo;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.acl.internal.AuthenticatedUser;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.utils.Debug;

public final class RMIBootServiceImpl implements SecurityService {
   private static boolean turnOffIA;
   private static boolean propogatePE;
   private static final AuthenticatedSubject kernelId;

   public AuthenticatedUser authenticate(UserInfo var1) throws RemoteException {
      throw new AssertionError("authenticate()");
   }

   public AuthenticatedUser authenticate(UserInfo var1, InboundRequest var2) throws RemoteException {
      Debug.assertion(var2 != null, "Request cannot be null");
      ServerChannel var3 = var2.getServerChannel();
      Object var4 = null;
      AuthenticatedSubject var5 = null;
      X509Certificate[] var6 = var2.getCertificateChain();
      if (var1 instanceof AuthenticatedUser) {
         var4 = (AuthenticatedUser)var1;
      } else {
         String var7 = "weblogicDEFAULT";
         PrincipalAuthenticator var8 = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(kernelId, var7, ServiceType.AUTHENTICATION);
         Debug.assertion(var8 != null, "Security system not initialized");
         if (turnOffIA && var6 != null) {
            return SecurityServiceManager.getCurrentSubject(kernelId);
         }

         if (var6 != null) {
            try {
               String var9 = "X.509";
               var5 = var8.assertIdentity(var9, var6, var2.getContextHandler());
            } catch (LoginException var14) {
            }
         }

         if (var5 == null) {
            if (!(var1 instanceof DefaultUserInfoImpl)) {
               SecurityException var17 = new SecurityException("Received bad UserInfo: " + var1.getClass().getName());
               throw new RemoteException(var17.getMessage(), var17);
            }

            DefaultUserInfoImpl var16 = (DefaultUserInfoImpl)var1;
            String var10 = var16.getName();
            String var11 = var16.getPassword();
            if (var10 == null || var10.length() == 0) {
               return SubjectUtils.getAnonymousSubject();
            }

            try {
               SimpleCallbackHandler var12 = new SimpleCallbackHandler(var10, var11);
               var5 = var8.authenticate(var12, var2.getContextHandler());
            } catch (LoginException var15) {
               SecurityException var13 = null;
               if (propogatePE) {
                  var13 = new SecurityException(var15.getMessage());
               } else {
                  var13 = new SecurityException("User: " + var10 + ", failed to be authenticated.");
               }

               throw new RemoteException(var13.getMessage(), var13);
            }
         }

         this.checkAdminPort(var5, ChannelHelper.isAdminChannel(var3));
         var4 = var5;
      }

      setQOS((AuthenticatedUser)var4, var3);
      return (AuthenticatedUser)var4;
   }

   private void checkAdminPort(AuthenticatedSubject var1, boolean var2) throws RemoteException {
      if (ChannelHelper.isLocalAdminChannelEnabled() && SubjectUtils.isUserAnAdministrator(var1) && !var2) {
         SecurityException var3 = new SecurityException("User '" + var1 + "' has administration role. " + "All tasks by adminstrators must go through an " + "Administration Port.");
         throw new RemoteException(var3.getMessage(), var3);
      }
   }

   private static void setQOS(AuthenticatedUser var0, ServerChannel var1) {
      if (ChannelHelper.isLocalAdminChannelEnabled() && var0 instanceof AuthenticatedSubject && SubjectUtils.isUserAnAdministrator((AuthenticatedSubject)var0) && ChannelHelper.isAdminChannel(var1)) {
         var0.setQOS((byte)103);
      } else {
         var0.setQOS(var1.getProtocol().getQOS());
      }

   }

   static {
      try {
         turnOffIA = Boolean.getBoolean("weblogic.security.disableIdentityAssertion");
         propogatePE = Boolean.getBoolean("weblogic.security.propogateProviderException");
      } catch (SecurityException var1) {
      }

      kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }
}
