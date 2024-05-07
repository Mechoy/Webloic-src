package weblogic.ejb.container.interfaces;

import javax.ejb.EnterpriseBean;
import weblogic.ejb.container.cache.CacheKey;
import weblogic.ejb.spi.EJBCache;
import weblogic.ejb20.cache.CacheFullException;

public interface SingleInstanceCache extends EJBCache {
   boolean contains(CacheKey var1);

   EnterpriseBean get(CacheKey var1);

   void put(CacheKey var1, EnterpriseBean var2) throws CacheFullException;

   void release(CacheKey var1);

   void remove(CacheKey var1);

   void removeOnError(CacheKey var1);

   void clear();

   void stopScrubber();

   void beanImplClassChangeNotification();
}
