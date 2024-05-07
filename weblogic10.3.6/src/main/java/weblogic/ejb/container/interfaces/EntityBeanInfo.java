package weblogic.ejb.container.interfaces;

import java.util.Collection;
import weblogic.ejb.container.persistence.spi.PersistenceManager;

public interface EntityBeanInfo extends ClientDrivenBeanInfo {
   String ENTITY_BEAN_INFO_IS_CMP_PROP = "weblogic.ejb.entity.bean.is.cmp";
   String ENTITY_BEAN_INFO_PERSISTENCE_MANAGER_PROP = "weblogic.ejb.entity.bean.persistence.manager";
   int EXCLUSIVE_CONCURRENCY = 1;
   int DATABASE_CONCURRENCY = 2;
   int READONLY_EXCLUSIVE_CONCURRENCY = 4;
   int READONLY_CONCURRENCY = 5;
   int OPTIMISTIC_CONCURRENCY = 6;

   int getConcurrencyStrategy();

   boolean isUnknownPrimaryKey();

   Class getPrimaryKeyClass();

   String getPrimaryKeyClassName();

   boolean getIsBeanManagedPersistence();

   String getPersistenceUseIdentifier();

   String getPersistenceUseVersion();

   String getPersistenceUseStorage();

   boolean getBoxCarUpdates();

   String getIsModifiedMethodName();

   boolean isReentrant();

   boolean isReadOnlyWithSingleInstance();

   boolean isReadOnly();

   boolean isOptimistic();

   String getCacheName();

   int getEstimatedBeanSize();

   boolean getCacheBetweenTransactions();

   boolean getDisableReadyInstances();

   String getCategoryCmpField();

   void setCategoryCmpField(String var1);

   Collection getAllQueries();

   PersistenceManager getPersistenceManager();

   CMPInfo getCMPInfo();

   String getJarFileName();

   InvalidationBeanManager getInvalidationTargetBeanManager();

   String getInvalidationTargetEJBName();

   boolean isDynamicQueriesEnabled();

   void updateReadTimeoutSeconds(int var1);

   void updateKeyCacheSize(int var1);

   int getInstanceLockOrder();

   int getMaxQueriesInCache();
}
