package weblogic.deployment;

import java.util.HashSet;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.transaction.Transaction;
import weblogic.ejb.container.internal.ExtendedPersistenceContextManager;
import weblogic.ejb.container.internal.ExtendedPersistenceContextWrapper;

public final class ExtendedEntityManagerProxyImpl extends BasePersistenceContextProxyImpl {
   ExtendedEntityManagerProxyImpl(String var1, String var2, String var3, PersistenceUnitRegistry var4) {
      super(var1, var2, var3, var4);

      try {
         Class var5 = EntityManager.class;
         this.setTransactionAccessMethod(var5.getMethod("getTransaction", (Class[])null));
         this.setCloseMethod(var5.getMethod("close", (Class[])null));
         HashSet var6 = new HashSet();
         var6.add(var5.getMethod("flush", (Class[])null));
         var6.add(var5.getMethod("lock", Object.class, LockModeType.class));
         var6.add(var5.getMethod("joinTransaction", (Class[])null));
         this.setTransactionalMethods(var6);
      } catch (NoSuchMethodException var7) {
         throw new AssertionError("Couldn't get expected method: " + var7);
      }
   }

   protected String getPersistenceContextStyleName() {
      return "EntityManager";
   }

   protected Object getPersistenceContext(Transaction var1) {
      String var2 = this.getUnitName();
      if (var1 != null) {
         Object var3 = this.txRegistry.getResource(var2);
         if (var3 != null) {
            return var3;
         }
      }

      ExtendedPersistenceContextWrapper var5 = ExtendedPersistenceContextManager.getExtendedPersistenceContext(var2);
      if (var5 == null) {
         throw new IllegalStateException("Extended Persistence Contexts can only be invoked from within the context of the stateful session bean that declares the Extended Persistence Context.");
      } else {
         EntityManager var4 = var5.getEntityManager();
         if (var1 != null) {
            this.txRegistry.putResource(var2, var4);
            var4.joinTransaction();
         }

         return var4;
      }
   }

   protected Object newPersistenceContext(PersistenceUnitInfoImpl var1) {
      throw new IllegalStateException("Extended Persistence Contexts should only be created when a Stateful Session Bean is created.");
   }

   protected void close(Object var1) {
   }
}
