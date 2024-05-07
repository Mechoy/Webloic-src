package weblogic.security.acl.internal;

import java.rmi.RemoteException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import weblogic.common.internal.BootServicesStub;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.common.internal.RMIBootServiceStub;
import weblogic.kernel.KernelStatus;
import weblogic.protocol.Protocol;
import weblogic.rjvm.LocalRJVM;
import weblogic.rjvm.RJVM;
import weblogic.rjvm.RJVMManager;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.SecurityService;
import weblogic.security.acl.UserInfo;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityManager;
import weblogic.security.service.SecurityServiceManager;

public final class RemoteAuthenticate {
   private static AuthenticatedSubject kernelID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static boolean enableDefaultUserProperty = false;

   public static AuthenticatedUser authenticate(final UserInfo var0, RJVM var1, Protocol var2, String var3, long var4, boolean var6) throws RemoteException, SecurityException {
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

      if (!KernelStatus.isServer()) {
         AuthenticatedSubject var13 = var7 == null ? null : SecurityServiceManager.getASFromAU(var7);
         if (var6 || enableDefaultUserProperty) {
            SecurityManager.setDefaultUser(var13);
         }
      }

      return var7;
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

   static {
      if (!KernelStatus.isServer() && !KernelStatus.isApplet()) {
         String var0 = System.getProperty("weblogic.jndi.enableDefaultUser");
         if (var0 != null) {
            enableDefaultUserProperty = true;
         }
      }

   }
}
