package weblogic.ejb.spi;

public interface EJBCache {
   void setMaxBeansInCache(int var1);

   int getMaxBeansInCache();

   boolean usesMaxBeansInCache();

   void setMaxCacheSize(long var1);

   long getMaxCacheSize();

   long getCurrentSize();

   void register(CachingManagerBase var1);

   void updateMaxBeansInCache(int var1);

   void updateMaxCacheSize(int var1);

   void reInitializeCacheAndPools();
}
