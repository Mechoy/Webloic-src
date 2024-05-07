package weblogic.ejb.container.internal.usertransactioncheck;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

public class BaseUserTransactionProxy implements UserTransaction {
   private boolean isEntityBean;
   private UserTransaction delegate;

   public BaseUserTransactionProxy(boolean var1, UserTransaction var2) {
      this.isEntityBean = var1;
      this.delegate = var2;
   }

   public void begin() throws NotSupportedException, SystemException {
      this.checkAllowedInvoke();
      this.delegate.begin();
   }

   public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {
      this.checkAllowedInvoke();
      this.delegate.commit();
   }

   public void rollback() throws IllegalStateException, SecurityException, SystemException {
      this.checkAllowedInvoke();
      this.delegate.rollback();
   }

   public void setRollbackOnly() throws IllegalStateException, SystemException {
      this.checkAllowedInvoke();
      this.delegate.setRollbackOnly();
   }

   public int getStatus() throws SystemException {
      this.checkAllowedInvoke();
      return this.delegate.getStatus();
   }

   public void setTransactionTimeout(int var1) throws SystemException {
      this.checkAllowedInvoke();
      this.delegate.setTransactionTimeout(var1);
   }

   protected void checkAllowedInvoke() {
      if (this.isEntityBean) {
         throw new IllegalStateException("It is illegal to invoke UserTransaction methods in entity bean.");
      }
   }
}
