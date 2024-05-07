package weblogic.corba.cos.transactions;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.xa.Xid;
import org.omg.CORBA.INVALID_TRANSACTION;
import org.omg.CORBA.TRANSACTION_ROLLEDBACK;
import org.omg.CosTransactions.Control;
import org.omg.CosTransactions.HeuristicHazard;
import org.omg.CosTransactions.HeuristicMixed;
import org.omg.CosTransactions._TerminatorImplBase;
import weblogic.iiop.Connection;
import weblogic.rmi.internal.OIDManager;
import weblogic.transaction.Transaction;
import weblogic.transaction.TxHelper;

public final class TerminatorImpl extends _TerminatorImplBase {
   private Connection connection;
   private Xid xid;
   private Control control;
   private static final long serialVersionUID = 4068654570203577465L;

   TerminatorImpl(Xid var1, Connection var2, Control var3) {
      this.xid = var1;
      this.connection = var2;
      this.control = var3;
   }

   public void commit(boolean var1) throws HeuristicMixed, HeuristicHazard {
      if (this.connection != null) {
         this.connection.setTxContext((Object)null);
      }

      Transaction var2 = (Transaction)TxHelper.getTransactionManager().getTransaction(this.xid);
      if (var2 == null) {
         this.release();
         throw new INVALID_TRANSACTION("No transaction");
      } else {
         try {
            var2.commit();
         } catch (HeuristicMixedException var11) {
            if (var1) {
               throw new HeuristicMixed();
            }
         } catch (HeuristicRollbackException var12) {
            if (var1) {
               throw new HeuristicHazard();
            }
         } catch (RollbackException var13) {
            throw new TRANSACTION_ROLLEDBACK(var13.getMessage());
         } catch (SecurityException var14) {
            throw new INVALID_TRANSACTION(var14.getMessage());
         } catch (SystemException var15) {
            throw new INVALID_TRANSACTION(var15.getMessage());
         } finally {
            this.release();
         }

      }
   }

   public void rollback() {
      if (this.connection != null) {
         this.connection.setTxContext((Object)null);
      }

      Transaction var1 = (Transaction)TxHelper.getTransactionManager().getTransaction(this.xid);
      if (var1 == null) {
         this.release();
         throw new INVALID_TRANSACTION("No transaction");
      } else {
         try {
            var1.rollback();
         } catch (IllegalStateException var7) {
            throw new INVALID_TRANSACTION(var7.getMessage());
         } catch (SystemException var8) {
            throw new INVALID_TRANSACTION(var8.getMessage());
         } finally {
            this.release();
         }

      }
   }

   private void release() {
      OIDManager var1 = OIDManager.getInstance();
      if (var1.getServerReference(this) != null) {
         var1.removeServerReference(var1.getServerReference(this));
      }

      if (var1.getServerReference(this.control) != null) {
         var1.removeServerReference(var1.getServerReference(this.control));
      }

   }
}
