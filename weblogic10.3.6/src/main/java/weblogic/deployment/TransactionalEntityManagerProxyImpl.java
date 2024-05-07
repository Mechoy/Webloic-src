package weblogic.deployment;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

public final class TransactionalEntityManagerProxyImpl extends BasePersistenceContextProxyImpl {
   Set<Method> queryMethods = new HashSet();

   TransactionalEntityManagerProxyImpl(String var1, String var2, String var3, PersistenceUnitRegistry var4) {
      super(var1, var2, var3, var4);

      try {
         Class var5 = EntityManager.class;
         this.setTransactionAccessMethod(var5.getMethod("getTransaction", (Class[])null));
         this.setCloseMethod(var5.getMethod("close", (Class[])null));
         this.setDelegateMethod(var5.getMethod("getDelegate", (Class[])null));
         HashSet var6 = new HashSet();
         var6.add(var5.getMethod("refresh", Object.class));
         var6.add(var5.getMethod("remove", Object.class));
         var6.add(var5.getMethod("merge", Object.class));
         var6.add(var5.getMethod("persist", Object.class));
         var6.add(var5.getMethod("flush", (Class[])null));
         var6.add(var5.getMethod("lock", Object.class, LockModeType.class));
         var6.add(var5.getMethod("joinTransaction", (Class[])null));
         this.setTransactionalMethods(var6);
         Method[] var7 = var5.getMethods();
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            Method var10 = var7[var9];
            if (var10.getName().startsWith("create") && var10.getName().endsWith("Query")) {
               this.queryMethods.add(var10);
            }
         }

      } catch (NoSuchMethodException var11) {
         throw new AssertionError("Couldn't get expected method: " + var11);
      }
   }

   protected String getPersistenceContextStyleName() {
      return "EntityManager";
   }

   protected Object newPersistenceContext(PersistenceUnitInfoImpl var1) {
      return var1.getUnwrappedEntityManagerFactory().createEntityManager();
   }

   protected Object invoke(Object var1, Method var2, Object[] var3, Transaction var4) throws Throwable {
      return var4 == null && this.queryMethods.contains(var2) ? this.persistenceUnit.createNonTransactionalQueryProxy(this, var2, var3) : super.invoke(var1, var2, var3, var4);
   }

   protected void checkTransactionStatus(Object var1, Transaction var2) {
      EntityManager var3 = (EntityManager)var1;
      if (!var3.isOpen()) {
         try {
            if (var2 != null && var2.getStatus() == 4) {
               throw new IllegalStateException("The transaction associated with this transaction-scoped persistence context has been rolled back and as a result, the EntityManager has been closed.  No further operations are allowed in this transaction context. Please see the server log for the cause of the rollback.");
            }
         } catch (SystemException var5) {
         }
      }

   }

   protected void close(Object var1) {
      EntityManager var2 = (EntityManager)var1;
      if (var2.isOpen()) {
         var2.close();
      }

   }
}
