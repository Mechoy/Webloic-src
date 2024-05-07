package weblogic.ejb.container.interfaces;

import javax.ejb.EnterpriseBean;
import javax.ejb.EntityBean;
import javax.naming.Context;
import javax.transaction.Transaction;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.cache.CacheKey;
import weblogic.ejb.container.internal.TxManager;
import weblogic.ejb.container.persistence.spi.RSInfo;
import weblogic.ejb.container.utils.PartialOrderSet;
import weblogic.ejb.spi.CachingManagerBase;
import weblogic.ejb.spi.EJBCache;
import weblogic.ejb.spi.WLDeploymentException;

public interface CachingManager extends CachingManagerBase {
   void setup(BaseEJBRemoteHomeIntf var1, BaseEJBLocalHomeIntf var2, BeanInfo var3, Context var4, EJBCache var5) throws WLDeploymentException;

   int getBeanSize();

   boolean isEntityManager();

   void enrollInTransaction(Transaction var1, CacheKey var2, EntityBean var3, RSInfo var4) throws InternalException;

   int getIdleTimeoutSeconds();

   void selectedForReplacement(CacheKey var1, EntityBean var2);

   void loadBeanFromRS(CacheKey var1, EntityBean var2, RSInfo var3) throws InternalException;

   void removedFromCache(CacheKey var1, EnterpriseBean var2);

   void removedOnError(CacheKey var1, EnterpriseBean var2);

   void swapIn(CacheKey var1, EnterpriseBean var2);

   void swapOut(CacheKey var1, EnterpriseBean var2);

   void replicate(CacheKey var1, EnterpriseBean var2);

   boolean needsRemoval(EnterpriseBean var1);

   void updateMaxBeansInCache(int var1);

   void updateIdleTimeoutSecondsCache(int var1);

   boolean hasBeansEnrolledInTx(Transaction var1);

   PartialOrderSet getEnrolledInTxKeys(Transaction var1);

   TxManager getTxManager();

   boolean isFlushPending(Transaction var1, Object var2);

   boolean beanIsOpsComplete(Transaction var1, Object var2);

   void passivateAndRelease(CacheKey var1, EntityBean var2);

   void passivateAndBacktoPool(CacheKey var1, EntityBean var2);

   int passivateModifiedBean(Transaction var1, Object var2, boolean var3);

   int passivateUnModifiedBean(Transaction var1, Object var2);

   int cachePassivateModifiedBean(Transaction var1, Object var2, boolean var3);

   int cachePassivateUnModifiedBean(Transaction var1, Object var2);

   boolean passivateLockedModifiedBean(Transaction var1, Object var2, boolean var3, EntityBean var4);

   boolean passivateLockedUnModifiedBean(Transaction var1, Object var2, EntityBean var3);

   void operationsComplete(Transaction var1, Object var2);
}
