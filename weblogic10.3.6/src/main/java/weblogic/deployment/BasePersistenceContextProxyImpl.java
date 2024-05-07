package weblogic.deployment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;
import javax.persistence.TransactionRequiredException;
import javax.transaction.Synchronization;
import javax.transaction.Transaction;
import javax.transaction.TransactionSynchronizationRegistry;
import weblogic.transaction.TransactionHelper;
import weblogic.transaction.internal.InterpositionTier;
import weblogic.transaction.internal.ServerTransactionManagerImpl;

abstract class BasePersistenceContextProxyImpl implements InterceptingInvocationHandler {
   private final String appName;
   private final String moduleName;
   private final String persistenceUnitName;
   private final String unqualifiedPersistenceUnitName;
   private InvocationHandlerInterceptor iceptor;
   protected final PersistenceUnitInfoImpl persistenceUnit;
   protected Method transactionAccessMethod;
   protected Method closeMethod;
   protected Method delegateMethod;
   protected Set transactionalMethods;
   protected final TransactionSynchronizationRegistry txRegistry;
   protected final TransactionHelper txHelper;

   public BasePersistenceContextProxyImpl(String var1, String var2, String var3, PersistenceUnitRegistry var4) {
      this.transactionalMethods = Collections.EMPTY_SET;
      this.persistenceUnit = var4.getPersistenceUnit(var3);
      this.unqualifiedPersistenceUnitName = var3;
      this.persistenceUnitName = this.persistenceUnit.getPersistenceUnitId();
      this.appName = var1;
      this.moduleName = var2;
      this.txHelper = TransactionHelper.getTransactionHelper();
      this.txRegistry = (TransactionSynchronizationRegistry)this.txHelper.getTransactionManager();
   }

   public void setInterceptor(InvocationHandlerInterceptor var1) {
      this.iceptor = var1;
   }

   public void setTransactionAccessMethod(Method var1) {
      this.transactionAccessMethod = var1;
   }

   public void setCloseMethod(Method var1) {
      this.closeMethod = var1;
   }

   public void setTransactionalMethods(Set var1) {
      this.transactionalMethods = var1;
   }

   public void setDelegateMethod(Method var1) {
      this.delegateMethod = var1;
   }

   public String getAppName() {
      return this.appName;
   }

   public String getModuleName() {
      return this.moduleName;
   }

   public String getUnitName() {
      return this.persistenceUnitName;
   }

   public String getUnqualifiedUnitName() {
      return this.unqualifiedPersistenceUnitName;
   }

   public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
      if (this.iceptor != null) {
         this.iceptor.preInvoke(var2, var3);
      }

      Transaction var4 = this.txHelper.getTransaction();
      Object var5 = this.invoke(var1, var2, var3, var4);
      return this.iceptor != null ? this.iceptor.postInvoke(var2, var3, var5) : var5;
   }

   protected Object invoke(Object var1, Method var2, Object[] var3, Transaction var4) throws Throwable {
      this.validateInvocation(var2, var4);
      Object var5 = this.getPersistenceContext(var4);
      this.checkTransactionStatus(var5, var4);

      Object var6;
      try {
         var6 = var2.invoke(var5, var3);
      } catch (InvocationTargetException var11) {
         throw var11.getCause();
      } finally {
         if (var4 == null && !var2.equals(this.delegateMethod)) {
            this.close(var5);
         }

      }

      return var6;
   }

   protected void checkTransactionStatus(Object var1, Transaction var2) {
   }

   protected void validateInvocation(Method var1, Transaction var2) {
      if (var1.equals(this.transactionAccessMethod)) {
         throw new IllegalStateException("The method " + var1 + " cannot be invoked in the context of a JTA " + this.getPersistenceContextStyleName() + ".");
      } else if (var1.equals(this.closeMethod)) {
         throw new IllegalStateException("The method " + var1 + " cannot be invoked on a container-managed EntityManager" + this.getPersistenceContextStyleName() + ".");
      } else if (var2 == null && this.transactionalMethods.contains(var1)) {
         throw new TransactionRequiredException("The method " + var1 + " must be called in the context of a transaction.");
      }
   }

   protected abstract String getPersistenceContextStyleName();

   protected Object getPersistenceContext(Transaction var1) {
      Object var2;
      if (var1 != null) {
         var2 = this.txRegistry.getResource(this.persistenceUnitName);
         if (var2 != null) {
            return var2;
         }
      }

      var2 = this.newPersistenceContext(this.persistenceUnit);
      if (var1 != null) {
         this.txRegistry.putResource(this.persistenceUnitName, var2);
         ((ServerTransactionManagerImpl)this.txRegistry).registerInterposedSynchronization(new PersistenceContextCloser(var2), InterpositionTier.WLS_INTERNAL_SYNCHRONIZATION);
      }

      return var2;
   }

   protected abstract Object newPersistenceContext(PersistenceUnitInfoImpl var1);

   protected abstract void close(Object var1);

   private final class PersistenceContextCloser implements Synchronization {
      private final Object pc;

      private PersistenceContextCloser(Object var2) {
         this.pc = var2;
      }

      public void beforeCompletion() {
      }

      public void afterCompletion(int var1) {
         BasePersistenceContextProxyImpl.this.close(this.pc);
      }

      // $FF: synthetic method
      PersistenceContextCloser(Object var2, Object var3) {
         this(var2);
      }
   }
}
