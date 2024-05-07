package weblogic.corba.j2ee.transaction;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.UserTransaction;
import javax.transaction.xa.XAResource;
import weblogic.transaction.ClientTransactionManager;

public class TransactionManagerWrapper implements UserTransaction, ClientTransactionManager, Transaction {
   private static final ThreadLocal currentTx = new ThreadLocal();

   public static TransactionManagerWrapper getTransactionManager() {
      return TransactionManagerWrapper.TMMaker.SINGLETON;
   }

   protected TransactionManagerImpl getTM() {
      TransactionManagerImpl var1 = (TransactionManagerImpl)currentTx.get();
      if (var1 == null) {
         var1 = new TransactionManagerImpl();
         currentTx.set(var1);
      }

      return var1;
   }

   protected TransactionManagerWrapper() {
   }

   public void begin() throws NotSupportedException, SystemException {
      this.getTM().begin();
   }

   public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {
      this.getTM().commit();
   }

   public int getStatus() throws SystemException {
      return this.getTM().getStatus();
   }

   public void rollback() throws IllegalStateException, SecurityException, SystemException {
      this.getTM().rollback();
   }

   public void setRollbackOnly() throws IllegalStateException, SystemException {
      this.getTM().setRollbackOnly();
   }

   public void setTransactionTimeout(int var1) throws SystemException {
      this.getTM().setTransactionTimeout(var1);
   }

   public void resume(Transaction var1) throws InvalidTransactionException, IllegalStateException, SystemException {
      if (currentTx.get() != null) {
         throw new IllegalStateException();
      } else {
         try {
            TransactionManagerImpl var2 = (TransactionManagerImpl)var1;
            var2.resume(var1);
            currentTx.set(var2);
         } catch (ClassCastException var3) {
            throw new InvalidTransactionException();
         }
      }
   }

   public Transaction suspend() throws SystemException {
      if (currentTx.get() == null) {
         throw new SystemException("No transaction in progress");
      } else {
         Transaction var1 = this.getTM().suspend();
         currentTx.set((Object)null);
         return var1;
      }
   }

   public void forceResume(Transaction var1) {
      try {
         if (var1 == null) {
            return;
         }

         TransactionManagerImpl var2 = (TransactionManagerImpl)var1;
         var2.forceResume(var1);
         currentTx.set(var2);
      } catch (ClassCastException var3) {
      }

   }

   public Transaction forceSuspend() {
      if (currentTx.get() == null) {
         return null;
      } else {
         Transaction var1 = this.getTM().forceSuspend();
         currentTx.set((Object)null);
         return var1;
      }
   }

   public Transaction getTransaction() throws SystemException {
      return this.getTM().getTransaction();
   }

   public void registerSynchronization(Synchronization var1) throws RollbackException, IllegalStateException, SystemException {
      throw new SystemException("Not implemented");
   }

   public boolean delistResource(XAResource var1, int var2) throws IllegalStateException, SystemException {
      throw new SystemException("Not implemented");
   }

   public boolean enlistResource(XAResource var1) throws RollbackException, IllegalStateException, SystemException {
      throw new SystemException("Not implemented");
   }

   public String toString() {
      return super.toString() + " TransactionManagerWrapper";
   }

   private static final class TMMaker {
      private static final TransactionManagerWrapper SINGLETON = new TransactionManagerWrapper();
   }
}
