package weblogic.ejb.container.interfaces;

public interface CachingDescriptor {
   void setMaxBeansInCache(int var1);

   int getMaxBeansInCache();

   void setMaxQueriesInCache(int var1);

   int getMaxQueriesInCache();

   void setMaxBeansInFreePool(int var1);

   int getMaxBeansInFreePool();

   void setInitialBeansInFreePool(int var1);

   int getInitialBeansInFreePool();

   void setIdleTimeoutSecondsCache(int var1);

   int getIdleTimeoutSecondsCache();

   void setCacheType(String var1);

   String getCacheType();

   void setReadTimeoutSeconds(int var1);

   int getReadTimeoutSeconds();

   void setIdleTimeoutSecondsPool(int var1);

   int getIdleTimeoutSecondsPool();

   void setConcurrencyStrategy(String var1);

   String getConcurrencyStrategy();
}
