package weblogic.corba.cos.transactions;

import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.xa.Xid;
import org.omg.CORBA.INVALID_TRANSACTION;
import org.omg.CORBA.TRANSACTION_ROLLEDBACK;
import org.omg.CosTransactions.Control;
import org.omg.CosTransactions.Coordinator;
import org.omg.CosTransactions.Inactive;
import org.omg.CosTransactions.NotSubtransaction;
import org.omg.CosTransactions.PropagationContext;
import org.omg.CosTransactions.RecoveryCoordinator;
import org.omg.CosTransactions.Resource;
import org.omg.CosTransactions.Status;
import org.omg.CosTransactions.SubtransactionAwareResource;
import org.omg.CosTransactions.SubtransactionsUnavailable;
import org.omg.CosTransactions.Synchronization;
import org.omg.CosTransactions.SynchronizationUnavailable;
import org.omg.CosTransactions.Unavailable;
import org.omg.CosTransactions._CoordinatorImplBase;
import weblogic.iiop.IIOPLogger;
import weblogic.rmi.extensions.activation.Activatable;
import weblogic.rmi.extensions.activation.Activator;
import weblogic.transaction.Transaction;
import weblogic.transaction.TransactionManager;
import weblogic.transaction.TxHelper;

public final class CoordinatorImpl extends _CoordinatorImplBase implements Activatable {
   protected static final boolean DEBUG = false;
   protected Xid xid;

   CoordinatorImpl(Xid var1) {
      this.xid = var1;
   }

   public Status get_parent_status() {
      return this.get_status();
   }

   public Status get_status() {
      Transaction var1 = this.getTx();
      if (var1 == null) {
         return Status.StatusNoTransaction;
      } else {
         int var2;
         try {
            var2 = var1.getStatus();
         } catch (SystemException var4) {
            throw new INVALID_TRANSACTION(var4.getMessage());
         }

         return OTSHelper.jta2otsStatus(var2);
      }
   }

   public Status get_top_level_status() {
      return this.get_status();
   }

   public String get_transaction_name() {
      String var1 = "";
      Transaction var2 = this.getTx();
      if (var2 != null) {
         var1 = var2.getName();
      }

      return var1;
   }

   public PropagationContext get_txcontext() throws Unavailable {
      Transaction var1 = this.getTx();
      if (var1 == null) {
         throw new Unavailable();
      } else {
         try {
            return OTSHelper.exportTransaction((Transaction)var1, 1).getPropagationContext();
         } catch (Inactive var3) {
            throw (Unavailable)(new Unavailable()).initCause(var3);
         } catch (Throwable var4) {
            throw new INVALID_TRANSACTION(var4.getMessage());
         }
      }
   }

   public int hash_top_level_tran() {
      return this.hash_transaction();
   }

   public int hash_transaction() {
      return this.xid.hashCode();
   }

   public boolean is_same_transaction(Coordinator var1) {
      return var1 instanceof CoordinatorImpl ? this.xid.equals(((CoordinatorImpl)var1).xid) : false;
   }

   public boolean is_ancestor_transaction(Coordinator var1) {
      return this.is_same_transaction(var1);
   }

   public boolean is_descendant_transaction(Coordinator var1) {
      return this.is_same_transaction(var1);
   }

   public boolean is_related_transaction(Coordinator var1) {
      return this.is_same_transaction(var1);
   }

   public boolean is_top_level_transaction() {
      return true;
   }

   public RecoveryCoordinator register_resource(Resource var1) throws Inactive {
      if (OTSHelper.isDebugEnabled()) {
         IIOPLogger.logDebugOTS("register_resource(" + var1 + ")");
      }

      TransactionManager var2 = TxHelper.getTransactionManager();
      Transaction var3 = this.getTx();
      if (var3 == null) {
         throw new Inactive();
      } else {
         if (!(var1 instanceof ResourceImpl)) {
            try {
               ForeignTransactionManager var4 = ForeignTransactionManager.registerResource(var1, this.xid);
               var3.enlistResource(var4);
               if (OTSHelper.isDebugEnabled()) {
                  IIOPLogger.logDebugOTS("register_resource(" + var4.toString() + "): enlisted " + var4);
               }
            } catch (RollbackException var5) {
               IIOPLogger.logOTSError("register_resource() failed", var5);
               throw new TRANSACTION_ROLLEDBACK("register_resource() failed" + var5.getMessage());
            } catch (SystemException var6) {
               IIOPLogger.logOTSError("register_resource() failed", var6);
               throw new INVALID_TRANSACTION("register_resource() failed: " + var6.getMessage());
            }
         }

         RecoveryCoordinatorImpl var7 = (RecoveryCoordinatorImpl)RecoveryCoordinatorImpl.getRecoveryCoordinator(this.xid, var1);
         RecoveryCoordinatorReleaser.getReleaser().register(this.getTx(), var7);
         return var7;
      }
   }

   public Control create_subtransaction() throws SubtransactionsUnavailable, Inactive {
      throw new SubtransactionsUnavailable();
   }

   public void register_subtran_aware(SubtransactionAwareResource var1) throws Inactive, NotSubtransaction {
      throw new NotSubtransaction();
   }

   public void register_synchronization(Synchronization var1) throws Inactive, SynchronizationUnavailable {
      Transaction var2 = this.getTx();
      if (var2 == null) {
         throw new Inactive();
      } else {
         try {
            var2.registerSynchronization(new SynchronizationDispatcher(var1));
         } catch (RollbackException var4) {
            IIOPLogger.logOTSError("register_synchronization() failed", var4);
            throw new TRANSACTION_ROLLEDBACK(var4.getMessage());
         } catch (SystemException var5) {
            IIOPLogger.logOTSError("register_synchronization() failed", var5);
            throw new SynchronizationUnavailable();
         }
      }
   }

   public void rollback_only() throws Inactive {
      Transaction var1 = this.getTx();
      if (var1 == null) {
         throw new Inactive();
      } else {
         try {
            var1.setRollbackOnly();
         } catch (IllegalStateException var3) {
            IIOPLogger.logOTSError("rollback_only() failed", var3);
            throw new Inactive();
         } catch (SystemException var4) {
            IIOPLogger.logOTSError("rollback_only() failed", var4);
            throw new INVALID_TRANSACTION(var4.getMessage());
         }
      }
   }

   private final Transaction getTx() {
      Transaction var1 = (Transaction)TxHelper.getTransactionManager().getTransaction(this.xid);
      return var1;
   }

   public Object getActivationID() {
      return this.xid;
   }

   public Activator getActivator() {
      return CoordinatorImplFactory.getActivator();
   }

   protected static void p(String var0) {
      System.err.println("<CoordinatorImpl> " + var0);
   }
}
