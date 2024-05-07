package weblogic.transaction.internal;

import java.rmi.server.ServerNotActiveException;
import java.security.AccessController;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.rmi.extensions.server.RemoteDomainSecurityHelper;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.rmi.spi.EndPoint;
import weblogic.rmi.spi.HostID;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

final class ReceiveSecureAction {
   private static final AuthenticatedSubject kernelID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   static final boolean stranger(HostID var0, String var1) {
      AuthenticatedSubject var2 = SecurityServiceManager.getCurrentSubject(kernelID);
      int var3 = RemoteDomainSecurityHelper.acceptRemoteDomainCall(var0, var2);
      if (var3 == 1) {
         if (TxDebug.JTANaming.isDebugEnabled()) {
            TxDebug.JTANaming.debug("SecureAction.stranger  Subject used on received call: " + SubjectUtils.getUsername(var2) + " for action :" + var1 + " AUTHENTICATION FAILED ");
         }

         return true;
      } else if (var3 == 0) {
         if (TxDebug.JTANaming.isDebugEnabled()) {
            TxDebug.JTANaming.debug("SecureAction.stranger  Subject used on received call: " + SubjectUtils.getUsername(var2) + " for action: " + var1 + " AUTHENTICATION SUCCESSFUL ");
         }

         return false;
      } else {
         if (var3 == 2) {
            if (TxDebug.JTANaming.isDebugEnabled()) {
               TxDebug.JTANaming.debug("SecureAction.stranger  Subject used on received call: " + SubjectUtils.getUsername(var2) + " for action: " + var1 + " AUTHENTICATION UNDETERMINABLE " + " use SecurityInteropMode");
            }

            int var4 = ((ServerTransactionManagerImpl)ServerTransactionManagerImpl.getTransactionManager()).getInteropMode();
            if (var2 == null || SubjectUtils.isUserAnonymous(var2)) {
               return var4 != 1 && kernelID.getQOS() == 103;
            }

            if (SecurityServiceManager.isKernelIdentity(var2)) {
               return false;
            }

            if (var4 == 2) {
               return SecurityServiceManager.isTrustedServerIdentity(var2);
            }

            PeerInfo var5 = null;

            try {
               EndPoint var6 = ServerHelper.getClientEndPoint();
               if (var6 != null && var6 instanceof PeerInfoable) {
                  var5 = ((PeerInfoable)var6).getPeerInfo();
               }
            } catch (ServerNotActiveException var7) {
               return true;
            }

            if (var5 == null) {
               return true;
            }

            if (var5.getMajor() == 6 && "system".equals(SubjectUtils.getUsername(var2))) {
               return false;
            }
         }

         return true;
      }
   }
}
