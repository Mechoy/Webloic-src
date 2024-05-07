package weblogic.ejb.container.interfaces;

import java.util.List;
import weblogic.ejb.container.cache.CacheKey;

public interface PassivatibleEntityCache {
   List getCachingManagers();

   void release(Object var1, CacheKey var2);
}
