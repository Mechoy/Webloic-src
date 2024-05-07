package weblogic.ejb.container.internal;

import javax.persistence.EntityManager;
import weblogic.deployment.PersistenceUnitInfoImpl;
import weblogic.ejb.container.EJBLogger;
import weblogic.logging.Loggable;

public final class ExtendedPersistenceContextWrapper {
   private final PersistenceUnitInfoImpl persistenceUnit;
   private final String persistenceUnitName;
   private int referenceCount;
   private EntityManager entityManager = null;

   public ExtendedPersistenceContextWrapper(PersistenceUnitInfoImpl var1) {
      this.persistenceUnit = var1;
      this.persistenceUnitName = var1.getPersistenceUnitId();
      this.referenceCount = 1;
      this.entityManager = this.persistenceUnit.getUnwrappedEntityManagerFactory().createEntityManager();
   }

   public synchronized void incrementReferenceCount() {
      ++this.referenceCount;
   }

   public synchronized void decrementReferenceCount() {
      --this.referenceCount;
      if (this.referenceCount < 1 && this.entityManager != null) {
         this.entityManager.close();
         this.entityManager = null;
      }

   }

   public String getPersistenceUnitName() {
      return this.persistenceUnitName;
   }

   public EntityManager getEntityManager() {
      if (this.referenceCount < 1) {
         Loggable var1 = EJBLogger.logExtendedPersistenceContextClosedLoggable();
         throw new IllegalStateException(var1.getMessage());
      } else {
         return this.entityManager;
      }
   }
}
