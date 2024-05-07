package weblogic.deployment;

import java.lang.reflect.Method;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import kodo.jdo.KodoJDOHelper;
import org.apache.openjpa.persistence.JPAFacadeHelper;
import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactory;

public final class TransactionalPersistenceManagerProxyImpl extends BasePersistenceContextProxyImpl {
   public TransactionalPersistenceManagerProxyImpl(String var1, String var2, String var3, PersistenceUnitRegistry var4) {
      super(var1, var2, var3, var4);

      try {
         Class var5 = PersistenceManager.class;
         this.setTransactionAccessMethod(var5.getMethod("currentTransaction", (Class[])null));
         this.setCloseMethod(var5.getMethod("close", (Class[])null));
      } catch (NoSuchMethodException var6) {
         throw new AssertionError("Couldn't get expected method: " + var6);
      }
   }

   protected String getPersistenceContextStyleName() {
      return "PersistenceManager";
   }

   protected Object newPersistenceContext(PersistenceUnitInfoImpl var1) {
      EntityManager var2 = var1.getEntityManagerFactory().createEntityManager();
      return toPersistenceManager(var2);
   }

   protected void close(Object var1) {
      ((PersistenceManager)var1).close();
   }

   protected static PersistenceManager toPersistenceManager(EntityManager var0) {
      if (!(var0 instanceof OpenJPAEntityManager)) {
         try {
            Class var1 = Class.forName(JPAFacadeHelper.class.getName(), true, var0.getClass().getClassLoader());
            Method var2 = var1.getMethod("toBroker", EntityManager.class);
            Class var3 = Class.forName(KodoJDOHelper.class.getName(), true, var0.getClass().getClassLoader());
            Class var4 = var2.getReturnType();
            Method var5 = var3.getMethod("toPersistenceManager", var4);
            Object var6 = var2.invoke((Object)null, var0);
            return (PersistenceManager)var5.invoke((Object)null, var6);
         } catch (Exception var7) {
            if (var7 instanceof RuntimeException) {
               throw (RuntimeException)var7;
            } else {
               throw new RuntimeException("Error converting EntityManager to Broker", var7);
            }
         }
      } else {
         return KodoJDOHelper.toPersistenceManager(JPAFacadeHelper.toBroker(var0));
      }
   }

   protected static PersistenceManagerFactory toPersistenceManagerFactory(EntityManagerFactory var0) {
      if (!(var0 instanceof OpenJPAEntityManagerFactory)) {
         try {
            Class var1 = Class.forName(JPAFacadeHelper.class.getName(), true, var0.getClass().getClassLoader());
            Method var2 = var1.getMethod("toBrokerFactory", EntityManagerFactory.class);
            Class var3 = Class.forName(KodoJDOHelper.class.getName(), true, var0.getClass().getClassLoader());
            Class var4 = var2.getReturnType();
            Method var5 = var3.getMethod("toPersistenceManagerFactory", var4);
            Object var6 = var2.invoke((Object)null, var0);
            return (PersistenceManagerFactory)var5.invoke((Object)null, var6);
         } catch (Exception var7) {
            if (var7 instanceof RuntimeException) {
               throw (RuntimeException)var7;
            } else {
               throw new RuntimeException("Error converting EntityManagerFactory to BrokerFactory", var7);
            }
         }
      } else {
         return KodoJDOHelper.toPersistenceManagerFactory(JPAFacadeHelper.toBrokerFactory(var0));
      }
   }
}
