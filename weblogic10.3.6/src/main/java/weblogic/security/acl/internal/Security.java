package weblogic.security.acl.internal;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.security.PrivateKey;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.cert.Certificate;
import javax.security.auth.login.LoginException;
import weblogic.common.internal.BootServicesStub;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.common.internal.RMIBootServiceStub;
import weblogic.kernel.AuditableThreadLocal;
import weblogic.kernel.AuditableThreadLocalFactory;
import weblogic.kernel.Kernel;
import weblogic.kernel.ThreadLocalInitialValue;
import weblogic.protocol.Protocol;
import weblogic.rjvm.LocalRJVM;
import weblogic.rjvm.RJVM;
import weblogic.rjvm.RJVMManager;
import weblogic.security.SimpleCallbackHandler;
import weblogic.security.SubjectUtils;
import weblogic.security.SSL.SSLClientInfo;
import weblogic.security.acl.DefaultUserInfoImpl;
import weblogic.security.acl.Realm;
import weblogic.security.acl.SecurityService;
import weblogic.security.acl.UserInfo;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityManager;
import weblogic.security.service.SecurityServiceManager;

public final class Security {
   private static final AuditableThreadLocal threadSSLClientInfo = AuditableThreadLocalFactory.createThreadLocal(new ThreadLocalInitialValue(true));
   private static AuthenticatedSubject kernelID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static boolean enableDefaultUserProperty = false;

   public static void init() {
   }

   public static AuthenticatedUser verify(AuthenticatedUser var0) throws SecurityException {
      return ClusterRealm.THE_ONE != null && ClusterRealm.THE_ONE.verify(var0) ? var0 : authenticate(var0);
   }

   private static AuthenticatedUser authenticateLocally(UserInfo var0) throws SecurityException {
      AuthenticatedSubject var1 = null;
      PrincipalAuthenticator var2 = SecurityServiceManager.getPrincipalAuthenticator(kernelID, "weblogicDEFAULT");

      try {
         if (var0 instanceof DefaultUserInfoImpl) {
            DefaultUserInfoImpl var3 = (DefaultUserInfoImpl)var0;
            var1 = var2.authenticate(new SimpleCallbackHandler(var3.getName(), var3.getPassword()), (ContextHandler)null);
         }

         return var1;
      } catch (LoginException var4) {
         throw new SecurityException(var4.getMessage());
      }
   }

   public static AuthenticatedUser authenticate(UserInfo var0, RJVM var1, Protocol var2, String var3) throws RemoteException, SecurityException {
      return authenticate(var0, var1, var2, var3, 0L, false);
   }

   public static AuthenticatedUser authenticate(final UserInfo var0, RJVM var1, Protocol var2, String var3, long var4, boolean var6) throws RemoteException, SecurityException {
      if (var1.getID().isLocal()) {
         return authenticateLocally(var0);
      } else {
         var1 = RJVMManager.getRJVMManager().findOrCreate(var1.getID());
         AuthenticatedUser var7 = null;
         if (isRMIBootstrapPossible(var1)) {
            final SecurityService var8 = RMIBootServiceStub.getStub(var1, var3, var4);

            try {
               AuthenticatedSubject var9 = SubjectUtils.getAnonymousSubject();
               var7 = (AuthenticatedUser)SecurityManager.runAs(kernelID, var9, new PrivilegedExceptionAction() {
                  public Object run() throws RemoteException {
                     return var8.authenticate(var0);
                  }
               });
            } catch (PrivilegedActionException var11) {
               RemoteException var10 = (RemoteException)var11.getException();
               if (var10.getCause() instanceof SecurityException) {
                  throw (SecurityException)var10.getCause();
               }

               throw var10;
            }
         } else {
            BootServicesStub var12 = new BootServicesStub(var1, var2);
            var7 = var12.authenticate(var0);
         }

         if (!Kernel.isServer()) {
            AuthenticatedSubject var13 = var7 == null ? null : SecurityServiceManager.getASFromAU(var7);
            if (var6 || enableDefaultUserProperty) {
               SecurityManager.setDefaultUser(var13);
            }
         }

         return var7;
      }
   }

   private static boolean isRMIBootstrapPossible(RJVM var0) {
      if (!(var0 instanceof PeerInfoable)) {
         return false;
      } else {
         PeerInfo var1 = ((PeerInfoable)var0).getPeerInfo();
         if (LocalRJVM.getLocalRJVM().getPeerInfo().equals(var1)) {
            return true;
         } else if (var1 == null) {
            return false;
         } else {
            int var2 = var1.getMajor();
            int var3 = var1.getMinor();
            int var4 = var1.getServicePack();
            return var2 > 8 || var2 == 6 && var3 == 1 && var4 >= 5 || var2 == 7 && var3 == 0 && var4 >= 3 || var2 == 8 && var3 == 1 && var4 >= 1;
         }
      }
   }

   public static AuthenticatedUser authenticate(UserInfo var0) throws SecurityException {
      String var1 = Realm.getAuthenticatedName(var0);
      return ClusterRealm.THE_ONE.certify(var1);
   }

   /** @deprecated */
   public static SSLClientInfo getThreadSSLClientInfo() {
      Object var0 = threadSSLClientInfo.get();
      SSLClientInfo var1 = null;
      if (var0 != null && var0 instanceof SSLClientInfo) {
         var1 = (SSLClientInfo)var0;
      } else {
         var1 = new SSLClientInfo();
         threadSSLClientInfo.set(var1);
      }

      return var1;
   }

   public static void setThreadSSLClientInfo(SSLClientInfo var0) {
      threadSSLClientInfo.set(var0);
   }

   /** @deprecated */
   public static final void setSSLRootCAFingerprints(String var0) {
      getThreadSSLClientInfo().setRootCAfingerprints(var0);
   }

   /** @deprecated */
   public static final void setSSLRootCAFingerprints(byte[][] var0) {
      getThreadSSLClientInfo().setRootCAfingerprints(var0);
   }

   /** @deprecated */
   public static final byte[][] getSSLRootCAFingerprints() {
      return getThreadSSLClientInfo().getRootCAfingerprints();
   }

   /** @deprecated */
   public static final void setSSLServerName(String var0) {
      getThreadSSLClientInfo().setExpectedName(var0);
   }

   /** @deprecated */
   public static final String getSSLServerName() {
      return getThreadSSLClientInfo().getExpectedName();
   }

   /** @deprecated */
   public static final Object getSSLClientCertificate() throws IOException {
      return getThreadSSLClientInfo().getSSLClientCertificate();
   }

   /** @deprecated */
   public static final void setSSLClientCertificate(InputStream[] var0) {
      getThreadSSLClientInfo().setSSLClientCertificate(var0);
   }

   /** @deprecated */
   public static final void setSSLClientKeyPassword(String var0) {
      getThreadSSLClientInfo().setSSLClientKeyPassword(var0);
   }

   /** @deprecated */
   public static final String getSSLClientKeyPassword() {
      return getThreadSSLClientInfo().getSSLClientKeyPassword();
   }

   /** @deprecated */
   public static final void loadLocalIdentity(Certificate[] var0, PrivateKey var1) {
      getThreadSSLClientInfo().loadLocalIdentity(var0, var1);
   }

   public static final boolean isClientCertAvailable() {
      return getThreadSSLClientInfo().isClientCertAvailable();
   }

   static {
      if (!Kernel.isServer() && !Kernel.isApplet()) {
         String var0 = System.getProperty("weblogic.jndi.enableDefaultUser");
         if (var0 != null) {
            enableDefaultUserProperty = true;
         }
      }

   }
}
