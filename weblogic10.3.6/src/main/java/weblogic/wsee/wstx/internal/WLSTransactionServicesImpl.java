package weblogic.wsee.wstx.internal;

import javax.naming.NamingException;
import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import javax.xml.ws.EndpointReference;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.transaction.InterposedTransactionManager;
import weblogic.transaction.Transaction;
import weblogic.transaction.TransactionHelper;
import weblogic.transaction.TransactionManager;
import weblogic.transaction.WLXid;
import weblogic.utils.StackTraceUtils;
import weblogic.wsee.WseeWsatLogger;
import weblogic.wsee.wstx.TransactionIdHelper;
import weblogic.wsee.wstx.TransactionServices;
import weblogic.wsee.wstx.wsat.WSATException;
import weblogic.wsee.wstx.wsat.WSATHelper;

public class WLSTransactionServicesImpl implements TransactionServices {
   private final DebugLogger debugWSAT = DebugLogger.getDebugLogger("DebugWSAT");
   private static TransactionServices singleton;

   static synchronized TransactionServices create() throws NamingException {
      if (singleton == null) {
         singleton = new WLSTransactionServicesImpl();
      }

      return singleton;
   }

   public static TransactionServices getInstance() {
      return singleton;
   }

   private WLSTransactionServicesImpl() {
      ForeignRecoveryContextManager.getInstance().start();
   }

   public byte[] enlistResource(XAResource var1, Xid var2) throws WSATException {
      WSATGatewayRM var3 = WSATGatewayRM.getInstance();
      if (var3 == null) {
         throw new WSATException("WS-AT gateway not deployed.");
      } else {
         try {
            return var3.registerWSATResource(var2, var1);
         } catch (IllegalStateException var5) {
            throw new WSATException(var5);
         } catch (RollbackException var6) {
            throw new WSATException(var6);
         } catch (SystemException var7) {
            throw new WSATException(var7);
         }
      }
   }

   public void registerSynchronization(Synchronization var1, Xid var2) throws WSATException {
      Transaction var3 = (Transaction)this.getTM().getTransaction(var2);
      if (var3 == null) {
         this.log("registerSynchronization() no transaction matching " + var2);
         throw new WSATException("WLSTransactionServiceImpl.registerSynchronization() no transaction matching " + var2);
      } else {
         try {
            var3.registerSynchronization(var1);
         } catch (IllegalStateException var5) {
            this.log("registerSynchronization() xid=" + var2 + ", synchronization=" + var1 + ": " + StackTraceUtils.throwable2StackTrace(var5));
            throw new WSATException(var5);
         } catch (RollbackException var6) {
            this.log("registerSynchronization() xid=" + var2 + ", synchronization=" + var1 + ": " + StackTraceUtils.throwable2StackTrace(var6));
            throw new WSATException(var6);
         } catch (SystemException var7) {
            this.log("registerSynchronization() xid=" + var2 + ", synchronization=" + var1 + ": " + StackTraceUtils.throwable2StackTrace(var7));
            throw new WSATException(var7);
         }
      }
   }

   public int getExpires() {
      Transaction var1 = this.currentTransaction();
      return var1 != null ? (int)(var1.getTimeToLiveMillis() / 1000L) : 0;
   }

   public byte[] getGlobalTransactionId() {
      Transaction var1 = this.currentTransaction();
      return var1 != null ? var1.getXid().getGlobalTransactionId() : null;
   }

   public EndpointReference getParentReference(Xid var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("No subordinate transaction " + var1);
      } else {
         Transaction var2 = (Transaction)this.getTM().getTransaction(var1);
         if (var2 == null) {
            throw new IllegalArgumentException("No subordinate transaction " + var1);
         } else {
            ForeignRecoveryContext var3 = (ForeignRecoveryContext)var2.getProperty("weblogic.wsee.wstx.foreignContext");
            if (var3 == null) {
               throw new AssertionError("No recovery context associated with transaction " + var1);
            } else {
               return var3.getEndpointReference();
            }
         }
      }
   }

   public Xid importTransaction(int var1, byte[] var2) throws WSATException {
      Xid var3 = TransactionIdHelper.getInstance().getOrCreateXid(var2);
      if (WSATHelper.isDebugEnabled()) {
         this.debug("importTransaction tId=" + new String(var2) + " foreignXid=" + var3);
      }

      XAResource var4 = this.getGatewayXAResource();

      try {
         var4.setTransactionTimeout(var1);
         var4.start(var3, 0);
         Transaction var5 = (Transaction)this.getTM().getTransaction();
         if (var5 == null) {
            this.log("no local transaction context for imported transaction " + new String(var2) + "(" + var3 + ")");
            throw new WSATException("Unable to import foreign WS-AT transaction " + new String(var2));
         } else {
            return var3;
         }
      } catch (XAException var6) {
         this.log("importTransaction tid=" + new String(var2) + ", XAException=" + StackTraceUtils.throwable2StackTrace(var6));
         throw new WSATException(var6);
      } catch (SystemException var7) {
         this.log("importTransaction tid=" + new String(var2) + ", SystemException=" + StackTraceUtils.throwable2StackTrace(var7));
         throw new WSATException(var7);
      }
   }

   public String prepare(byte[] var1) throws WSATException {
      WLXid var2 = TransactionIdHelper.getInstance().wsatid2xid(new String(var1));
      if (var2 == null) {
         this.log("prepare() tid=" + new String(var1) + " no mapping to foreign Xid");
         throw new WSATException("Transaction identifier does not exist: " + new String(var1));
      } else {
         XAResource var3 = this.getGatewayXAResource();

         try {
            int var4 = var3.prepare(var2);
            switch (var4) {
               case 0:
                  return "Prepared";
               case 3:
                  return "ReadOnly";
               default:
                  this.log("Unknown return value from prepare: " + var4);
                  throw new WSATException("Unknown return value from prepare: " + var4);
            }
         } catch (XAException var5) {
            this.log("prepare() tid=" + new String(var1) + " (" + var2 + ") XAException (" + JTAHelper.xaErrorCodeToString(var5.errorCode, false) + "): " + StackTraceUtils.throwable2StackTrace(var5));
            throw new WSATException("XAException during prepare of " + new String(var1) + ": " + var5, var5);
         }
      }
   }

   public void commit(byte[] var1) throws WSATException {
      WLXid var2 = TransactionIdHelper.getInstance().wsatid2xid(new String(var1));
      if (var2 == null) {
         this.log("commit() tid=" + new String(var1) + " no mapping to foreign Xid");
         throw new WSATException("Transaction identifier does not exist: " + new String(var1));
      } else {
         boolean var3 = false;
         XAResource var4 = this.getGatewayXAResource();

         try {
            var4.commit(var2, false);
            var3 = true;
         } catch (XAException var10) {
            this.log("commit() tid=" + new String(var1) + " (" + var2 + "), XAException (" + JTAHelper.xaErrorCodeToString(var10.errorCode, false) + ") committing imported transaction: " + StackTraceUtils.throwable2StackTrace(var10));
            switch (var10.errorCode) {
               case -7:
               case -6:
               case -5:
               case -3:
                  throw new WSATException("XAException for " + new String(var1) + ": " + var10, var10);
               case -4:
                  var3 = true;
                  break;
               case 5:
               case 6:
               case 8:
                  var3 = true;
                  throw new WSATException("branch completed heuristically for " + new String(var1) + ": " + var10, var10);
               case 7:
                  var3 = true;
                  break;
               case 100:
               case 101:
               case 102:
               case 103:
               case 104:
               case 105:
               case 106:
               case 107:
                  String var6 = "commit() tid=" + new String(var1) + " (" + var2 + "), invalid errorCode XA_RB..., no one-phase support XAException:" + var10;
                  this.log(var6);
                  var3 = true;
                  throw new WSATException(var6, var10);
               default:
                  this.log("commit() tid=" + new String(var1) + " (" + var2 + "), unexpected XAException error code " + var10.errorCode + ": " + var10.getMessage());
                  throw new WSATException("Unexpected XAException: " + var10, var10);
            }

            throw new WSATException("XAException during commit of " + String.valueOf(var1) + ": " + var10, var10);
         } finally {
            if (var3) {
               this.deleteForeignState(var2);
            }

         }

      }
   }

   public void rollback(byte[] var1) throws WSATException {
      WLXid var2 = TransactionIdHelper.getInstance().wsatid2xid(new String(var1));
      if (var2 == null) {
         this.log("rollback() tid=" + new String(var1) + " no mapping to foreign Xid");
      }

      boolean var3 = false;
      XAResource var4 = this.getGatewayXAResource();

      try {
         var4.rollback(var2);
         var3 = true;
      } catch (XAException var9) {
         this.log("rollback() tid=" + new String(var1) + " (" + var2 + "), XAException (" + JTAHelper.xaErrorCodeToString(var9.errorCode, false) + ") rolling back imported transaction: " + StackTraceUtils.throwable2StackTrace(var9));
         switch (var9.errorCode) {
            case -7:
            case -6:
            case -5:
            case -3:
               throw new WSATException("XAException for " + new String(var1) + ": " + var9, var9);
            case -4:
               var3 = true;
               break;
            case -2:
            case -1:
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            default:
               this.log("rollback() tid=" + new String(var1) + " (" + var2 + "), unexpected XAException error code " + var9.errorCode + ": " + var9.getMessage());
               throw new WSATException("Unexpected XAException: " + var9, var9);
            case 5:
            case 7:
            case 8:
               var3 = true;
               throw new WSATException("branch completed heuristically for " + new String(var1) + ": " + var9, var9);
            case 6:
               var3 = true;
         }

         throw new WSATException("XAException during rollback of " + new String(var1) + ": " + var9, var9);
      } finally {
         if (var3) {
            this.deleteForeignState(var2);
         }

      }

   }

   public void replayCompletion(String var1, XAResource var2) throws WSATException {
      WLXid var3 = TransactionIdHelper.getInstance().wsatid2xid(var1);
      Transaction var4 = (Transaction)this.getTM().getTransaction(var3);
      if (WSATHelper.isDebugEnabled()) {
         this.debug("replayCompletion() tid=" + var1 + " xid=" + var3 + " transaction=" + var4);
      }

      if (var4 == null) {
         try {
            var2.rollback(var3);
         } catch (XAException var6) {
            this.log("replayCompletion() tid=" + var1 + " (" + var3 + "), XAException (" + JTAHelper.xaErrorCodeToString(var6.errorCode, false) + ") rolling back imported transaction: " + StackTraceUtils.throwable2StackTrace(var6));
            throw new WSATException("XAException on rollback of subordinate in response to replayCompletion for " + var3 + "(tid=" + var1 + ")", var6);
         }
      }

   }

   private void deleteForeignState(Xid var1) {
      TransactionIdHelper.getInstance().remove(var1);
      ForeignRecoveryContextManager.getInstance().remove(var1);
   }

   private TransactionManager getTM() {
      return (TransactionManager)TransactionHelper.getTransactionHelper().getTransactionManager();
   }

   private Transaction currentTransaction() {
      Transaction var1 = null;

      try {
         var1 = (Transaction)this.getTM().getTransaction();
      } catch (SystemException var3) {
      }

      return var1;
   }

   private XAResource getGatewayXAResource() {
      InterposedTransactionManager var1 = (InterposedTransactionManager)this.getTM();
      return var1.getXAResource();
   }

   private void log(String var1) {
      WseeWsatLogger.logWLSTransactionServicesImpl(var1);
   }

   private void debug(String var1) {
      this.debugWSAT.debug("WLSTransactionServicesImpl:" + var1);
   }
}
