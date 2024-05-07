package weblogic.jdbc.rmi.internal;

import java.rmi.server.ServerNotActiveException;
import java.security.AccessController;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jdbc.common.internal.JDBCServerHelperImpl;
import weblogic.jdbc.common.internal.JdbcDebug;
import weblogic.jdbc.rmi.RMIWrapperImpl;
import weblogic.rmi.extensions.server.RemoteDomainSecurityHelper;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.rmi.spi.EndPoint;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

public class RMISkelWrapperImpl extends RMIWrapperImpl {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final UnsupportedOperationException noRemoteJDBCException = new UnsupportedOperationException("Remote JDBC not supported");

   public void preInvocationHandler(String var1, Object[] var2) throws Exception {
      super.preInvocationHandler(var1, var2);
      if (JDBCServerHelperImpl.rmiSecure()) {
         EndPoint var3 = null;
         PeerInfo var4 = null;

         try {
            var3 = ServerHelper.getClientEndPoint();
         } catch (ServerNotActiveException var7) {
         }

         if (var3 != null && var3 instanceof PeerInfoable) {
            var4 = ((PeerInfoable)var3).getPeerInfo();
         }

         if (var4 == null || !var4.isServer()) {
            if (JdbcDebug.JDBCRMI.isDebugEnabled()) {
               JdbcDebug.JDBCRMI.debug("rejecting: caller not a server. " + var3);
            }

            throw noRemoteJDBCException;
         }

         AuthenticatedSubject var5 = SecurityServiceManager.getCurrentSubject(KERNEL_ID);
         boolean var6 = true;
         int var8 = RemoteDomainSecurityHelper.acceptRemoteDomainCall(var3.getHostID(), var5);
         if (var8 == 1) {
            if (JdbcDebug.JDBCRMI.isDebugEnabled()) {
               JdbcDebug.JDBCRMI.debug("rejecting: invalid cross-domain subject " + var5);
            }

            throw noRemoteJDBCException;
         }

         if (var8 == 0) {
            if (JdbcDebug.JDBCRMI.isDebugEnabled()) {
               JdbcDebug.JDBCRMI.debug("accepting call from remote domain with subject " + var5);
            }

            return;
         }

         if (var8 == 2) {
            if (!SecurityServiceManager.isKernelIdentity(var5)) {
               if (JdbcDebug.JDBCRMI.isDebugEnabled()) {
                  JdbcDebug.JDBCRMI.debug("rejecting: invalid subject for intra-domain call " + var5);
               }

               throw noRemoteJDBCException;
            }

            if (JdbcDebug.JDBCRMI.isDebugEnabled()) {
               JdbcDebug.JDBCRMI.debug("accepting intra-domain call with subject " + var5);
            }
         }
      }

   }
}
