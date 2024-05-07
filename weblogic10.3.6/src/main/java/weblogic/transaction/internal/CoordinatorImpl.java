package weblogic.transaction.internal;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.xa.XAException;
import javax.transaction.xa.Xid;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.ServerIdentity;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.rmi.spi.EndPoint;
import weblogic.rmi.spi.HostID;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.t3.srvr.T3Srvr;
import weblogic.transaction.TransactionSystemException;

class CoordinatorImpl extends SubCoordinatorImpl implements Coordinator3, CoordinatorOneway2, Constants, CoordinatorService {
   private static final AuthenticatedSubject kernelID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void commit(PropagationContext var1) throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, SystemException {
      PeerInfo var2 = null;

      try {
         EndPoint var3 = ServerHelper.getClientEndPoint();
         if (var3 != null && var3 instanceof PeerInfoable) {
            var2 = ((PeerInfoable)var3).getPeerInfo();
         }
      } catch (ServerNotActiveException var8) {
      }

      if (TxDebug.JTA2PC.isDebugEnabled()) {
         TxDebug.JTA2PC.debug("co.commit  is the call coming from a server: " + var2.isServer());
      }

      if (var2 != null && var2.isServer() && ReceiveSecureAction.stranger(this.getHostID(), "co.commit")) {
         TXLogger.logUserNotAuthorizedForCommit(this.getUserName());
      } else if (!TransactionService.isRunning()) {
         throw new SystemException("The server is being suspended or shut down.  No new transaction commit requests will be accepted.");
      } else {
         ServerTransactionImpl var9;
         try {
            var9 = (ServerTransactionImpl)var1.getTransaction();
         } catch (TransactionSystemException var7) {
            throw new SystemException(var7.getMessage());
         }

         if (var9 == null) {
            throw new RollbackException("This transaction does not exist on the coordinating server.  It was probably rolled back and forgotten.");
         } else {
            if (!var9.isImportedTransaction()) {
               var9.setOwnerTransactionManager(this.getTM());
               var9.commit();
            } else {
               try {
                  var9.internalCommit(true);
               } catch (AbortRequestedException var5) {
               } catch (XAException var6) {
               }
            }

         }
      }
   }

   public void ackPrePrepare(PropagationContext var1) {
      if (ReceiveSecureAction.stranger(this.getHostID(), "ackPrePrepare")) {
         TXLogger.logUserNotAuthorizedForAckPrePrepare(this.getUserName());
      } else {
         ServerTransactionImpl var2;
         try {
            var2 = (ServerTransactionImpl)var1.getTransaction();
         } catch (TransactionSystemException var4) {
            return;
         }

         if (var2 != null) {
            var2.ackPrePrepare();
         }
      }
   }

   public void ackPrepare(Xid var1, String var2, int var3) {
      if (ReceiveSecureAction.stranger(this.getHostID(), "ackPrepare")) {
         TXLogger.logUserNotAuthorizedForAckPrepare(this.getUserName());
      } else {
         try {
            ServerTransactionImpl var4 = (ServerTransactionImpl)this.getTM().getTransaction(var1);
            if (var4 == null) {
               return;
            }

            var4.ackPrepare(var2, var3);
         } catch (Throwable var5) {
            if (TxDebug.JTA2PC.isDebugEnabled()) {
               TxDebug.JTA2PC.debug("ackPrepare FAILED", var5);
            }
         }

      }
   }

   public void rollback(PropagationContext var1) throws IllegalStateException, SystemException {
      PeerInfo var2 = null;

      try {
         EndPoint var3 = ServerHelper.getClientEndPoint();
         if (var3 != null && var3 instanceof PeerInfoable) {
            var2 = ((PeerInfoable)var3).getPeerInfo();
         }
      } catch (ServerNotActiveException var6) {
      }

      if (var2 != null && var2.isServer() && ReceiveSecureAction.stranger(this.getHostID(), "co.rollback")) {
         TXLogger.logUserNotAuthorizedForRollback(this.getUserName());
      } else {
         ServerTransactionImpl var7;
         try {
            var7 = (ServerTransactionImpl)var1.getTransaction();
         } catch (TransactionSystemException var5) {
            throw new SystemException(var5.getMessage());
         }

         if (var7 != null) {
            if (!var7.isImportedTransaction()) {
               var7.rollback();
            } else {
               var7.internalRollback();
            }
         }

      }
   }

   public void checkStatus(Xid[] var1, String var2) {
      if (T3Srvr.getT3Srvr().getRunState() != 2) {
         if (TxDebug.JTA2PC.isDebugEnabled()) {
            TxDebug.JTA2PC.debug("CO checkStatus: return because server is not in Running state, for SC=" + var2);
         }

      } else if (ReceiveSecureAction.stranger(this.getHostID(), "checkStatus")) {
         TXLogger.logUserNotAuthorizedForCheckStatus(this.getUserName());
      } else {
         ServerTransactionImpl var3 = null;
         ArrayList var4 = null;
         int var5 = var1 == null ? 0 : var1.length;
         if (TxDebug.JTA2PC.isDebugEnabled()) {
            TxDebug.JTA2PC.debug("CO checkStatus: for SC=" + var2 + " " + var5 + " XIDs");
         }

         for(int var6 = 0; var6 < var5; ++var6) {
            var3 = (ServerTransactionImpl)this.getTM().getTransaction(var1[var6]);
            if (var3 == null) {
               if (var4 == null) {
                  var4 = new ArrayList(1);
               }

               var4.add(var1[var6]);
            }
         }

         if (var4 != null) {
            CoordinatorDescriptor var11 = ServerCoordinatorDescriptor.getOrCreate(var2);
            SubCoordinatorOneway var7 = JNDIAdvertiser.getSubCoordinator(var11, var3);
            if (var7 != null) {
               Xid[] var8 = new Xid[var4.size()];

               try {
                  SecureAction.runAction(kernelID, new StartRollbackAction(var7, (Xid[])((Xid[])var4.toArray(var8))), CoordinatorDescriptor.getServerURL(var2), "sc.startRollback");
               } catch (Exception var10) {
                  if (TxDebug.JTA2PC.isDebugEnabled()) {
                     TxDebug.JTA2PC.debug("CO get sc.startRollback failure: ", var10);
                  }
               }
            }
         }

      }
   }

   public void ackCommit(Xid var1, String var2) {
      if (ReceiveSecureAction.stranger(this.getHostID(), "ackCommit")) {
         TXLogger.logUserNotAuthorizedForAckCommit(this.getUserName());
      } else {
         ServerTransactionImpl var3 = (ServerTransactionImpl)this.getTM().getTransaction(var1);
         if (var3 != null) {
            var3.ackCommit(var2);
         }
      }
   }

   public void ackCommit(Xid var1, String var2, String[] var3) {
      if (ReceiveSecureAction.stranger(this.getHostID(), "ackCommit")) {
         TXLogger.logUserNotAuthorizedForAckCommit(this.getUserName());
      } else {
         ServerTransactionImpl var4 = (ServerTransactionImpl)this.getTM().getTransaction(var1);
         if (var4 != null) {
            var4.ackCommit(var2, var3);
         }
      }
   }

   public void nakCommit(Xid var1, String var2, short var3, String var4) {
      if (ReceiveSecureAction.stranger(this.getHostID(), "nakCommit")) {
         TXLogger.logUserNotAuthorizedForNakCommit(this.getUserName());
      } else {
         ServerTransactionImpl var5 = (ServerTransactionImpl)this.getTM().getTransaction(var1);
         if (var5 != null) {
            var5.nakCommit(var2, var3, var4);
         }
      }
   }

   public void nakCommit(Xid var1, String var2, short var3, String var4, String[] var5, String[] var6) {
      if (ReceiveSecureAction.stranger(this.getHostID(), "nakCommit")) {
         TXLogger.logUserNotAuthorizedForNakCommit(this.getUserName());
      } else {
         ServerTransactionImpl var7 = (ServerTransactionImpl)this.getTM().getTransaction(var1);
         if (var7 != null) {
            var7.nakCommit(var2, var3, var4, var5, var6);
         }
      }
   }

   public void startRollback(PropagationContext var1) throws RemoteException {
      if (ReceiveSecureAction.stranger(this.getHostID(), "startRollback")) {
         TXLogger.logUserNotAuthorizedForStartRollback(this.getUserName());
      } else {
         ServerTransactionImpl var2 = (ServerTransactionImpl)var1.getTransaction();
         if (var2 != null) {
            try {
               var2.rollback();
            } catch (Exception var4) {
            }
         }

      }
   }

   public void ackRollback(Xid var1, String var2) {
      if (ReceiveSecureAction.stranger(this.getHostID(), "ackRollback")) {
         TXLogger.logUserNotAuthorizedForAckRollback(this.getUserName());
      } else {
         ServerTransactionImpl var3 = (ServerTransactionImpl)this.getTM().getTransaction(var1);
         if (var3 != null) {
            var3.ackRollback(var2);
         }
      }
   }

   public void ackRollback(Xid var1, String var2, String[] var3) {
      if (ReceiveSecureAction.stranger(this.getHostID(), "ackRollback")) {
         TXLogger.logUserNotAuthorizedForAckRollback(this.getUserName());
      } else {
         ServerTransactionImpl var4 = (ServerTransactionImpl)this.getTM().getTransaction(var1);
         if (var4 != null) {
            var4.ackRollback(var2, var3);
         }
      }
   }

   public void nakRollback(Xid var1, String var2, short var3, String var4) {
      if (ReceiveSecureAction.stranger(this.getHostID(), "nakRollback")) {
         TXLogger.logUserNotAuthorizedForNakRollback(this.getUserName());
      } else {
         ServerTransactionImpl var5 = (ServerTransactionImpl)this.getTM().getTransaction(var1);
         if (var5 != null) {
            var5.nakRollback(var2, var3, var4);
         }
      }
   }

   public void nakRollback(Xid var1, String var2, short var3, String var4, String[] var5, String[] var6) {
      if (ReceiveSecureAction.stranger(this.getHostID(), "nakRollback")) {
         TXLogger.logUserNotAuthorizedForNakRollback(this.getUserName());
      } else {
         ServerTransactionImpl var7 = (ServerTransactionImpl)this.getTM().getTransaction(var1);
         if (var7 != null) {
            var7.nakRollback(var2, var3, var4, var5, var6);
         }
      }
   }

   public Map getProperties() {
      HashMap var1 = new HashMap(5);
      var1.put("propagationContextVersion", new Integer(PropagationContext.getVersion()));
      var1.put("coordinatorURL", this.getTM().getLocalCoordinatorURL());
      String var2 = ManagementService.getRuntimeAccess(kernelID).getServerName();
      var1.put("serverName", var2);
      return var1;
   }

   public void xaPrepare(PropagationContext var1) throws XAException {
      ServerTransactionImpl var2 = null;

      try {
         var2 = (ServerTransactionImpl)var1.getTransaction();
      } catch (TransactionSystemException var4) {
         XAResourceHelper.throwXAException(-3, "Cannot prepare imported transaction.", var4);
      }

      if (var2 != null) {
         this.getTM().getXAResource().prepare(var2.getForeignXid());
      }

   }

   public void xaCommit(Xid var1) throws XAException {
      if (!TransactionService.isRunning()) {
         XAResourceHelper.throwXAException(-3, "The server is being suspend or shut down.  No new transaction commit requests will be accepted.");
      }

      this.getTM().getXAResource().commit(var1, false);
   }

   public void xaRollback(Xid var1) throws XAException {
      this.getTM().getXAResource().rollback(var1);
   }

   public Xid[] xaRecover() throws XAException {
      return this.getTM().getXAResource().recover(25165824);
   }

   public void xaForget(Xid var1) {
      if (TxDebug.JTAGateway.isDebugEnabled()) {
         TxDebug.JTAGateway.debug("Coordinator.forget(foreignXid=" + XAResourceHelper.xidToString(var1) + ")");
      }

      try {
         this.getTM().getXAResource().forget(var1);
      } catch (XAException var3) {
      }

   }

   public void forceGlobalRollback(Xid var1) throws SystemException, RemoteException {
      if (TxDebug.JTA2PC.isDebugEnabled()) {
         TxDebug.JTA2PC.debug("SC.forceGlobalRollback: " + var1);
      }

      PeerInfo var2 = null;

      try {
         EndPoint var3 = ServerHelper.getClientEndPoint();
         if (var3 != null && var3 instanceof PeerInfoable) {
            var2 = ((PeerInfoable)var3).getPeerInfo();
         }
      } catch (ServerNotActiveException var4) {
      }

      if (var2 != null && var2.isServer()) {
         if (ReceiveSecureAction.stranger(this.getHostID(), "forceGlobalRollback")) {
            TXLogger.logUserNotAuthorizedForForceGlobalRollback(this.getUserName());
         } else {
            ServerTransactionImpl var5 = (ServerTransactionImpl)this.getTM().getTransaction(var1);
            if (var5 == null) {
               TXLogger.logForceGlobalRollbackNoTx(var1.toString());
            } else {
               var5.forceGlobalRollback();
            }
         }
      }
   }

   public void forceGlobalCommit(Xid var1) throws SystemException, RemoteException {
      if (TxDebug.JTA2PC.isDebugEnabled()) {
         TxDebug.JTA2PC.debug("SC.forceGlobalCommit: " + var1);
      }

      PeerInfo var2 = null;

      try {
         EndPoint var3 = ServerHelper.getClientEndPoint();
         if (var3 != null && var3 instanceof PeerInfoable) {
            var2 = ((PeerInfoable)var3).getPeerInfo();
         }
      } catch (ServerNotActiveException var4) {
      }

      if (var2 != null && var2.isServer()) {
         if (ReceiveSecureAction.stranger(this.getHostID(), "forceGlobalCommit")) {
            TXLogger.logUserNotAuthorizedForForceGlobalCommit(this.getUserName());
         } else {
            ServerTransactionImpl var5 = (ServerTransactionImpl)this.getTM().getTransaction(var1);
            if (var5 == null) {
               TXLogger.logForceGlobalCommitNoTx(var1.toString());
            } else {
               var5.forceGlobalCommit();
            }
         }
      }
   }

   public Object invokeCoordinatorService(String var1, Object var2) throws RemoteException, SystemException {
      ServerTransactionManagerImpl var3 = this.getTM();
      return var3.invokeCoordinatorService(var1, var2);
   }

   protected HostID getHostID() {
      try {
         EndPoint var1 = ServerHelper.getClientEndPoint();
         HostID var2 = var1.getHostID();
         if (TxDebug.JTANaming.isDebugEnabled()) {
            TxDebug.JTANaming.debug("RMI call coming from = " + ((ServerIdentity)var2).getDomainName());
         }

         return var2;
      } catch (Exception var3) {
         return null;
      }
   }

   private String getUserName() {
      return SubjectUtils.getUsername(SecurityServiceManager.getCurrentSubject(kernelID));
   }

   private class StartRollbackAction implements PrivilegedExceptionAction {
      private SubCoordinatorOneway sc;
      private Xid[] xids;

      StartRollbackAction(SubCoordinatorOneway var2, Xid[] var3) {
         this.sc = var2;
         this.xids = var3;
      }

      public Object run() throws Exception {
         this.sc.startRollback(this.xids);
         return null;
      }
   }
}
