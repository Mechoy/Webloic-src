package weblogic.ejb.container.deployer;

import java.util.Collections;
import java.util.Iterator;
import weblogic.ejb.container.dd.DDDefaults;
import weblogic.ejb.container.interfaces.CachingDescriptor;

class CachingDescriptorImpl implements CachingDescriptor {
   private int maxBeansInCache = DDDefaults.getMaxBeansInCache();
   private int maxQueriesInCache = DDDefaults.getMaxQueriesInCache();
   private int maxBeansInFreePool = DDDefaults.getMaxBeansInFreePool();
   private int initialBeansInFreePool = DDDefaults.getInitialBeansInFreePool();
   private int idleTimeoutSecondsCache = DDDefaults.getIdleTimeoutSeconds();
   private int idleTimeoutSecondsPool = 0;
   private String cacheType = DDDefaults.getCacheType();
   private String concurrencyStrategy = DDDefaults.getConcurrencyStrategy();
   private int readTimeoutSeconds = DDDefaults.getReadTimeoutSeconds();

   public CachingDescriptorImpl() {
   }

   public void setMaxBeansInCache(int var1) {
      this.maxBeansInCache = var1;
   }

   public int getMaxBeansInCache() {
      return this.maxBeansInCache;
   }

   public void setMaxQueriesInCache(int var1) {
      this.maxQueriesInCache = var1;
   }

   public int getMaxQueriesInCache() {
      return this.maxQueriesInCache;
   }

   public void setMaxBeansInFreePool(int var1) {
      this.maxBeansInFreePool = var1;
   }

   public int getMaxBeansInFreePool() {
      return this.maxBeansInFreePool;
   }

   public void setInitialBeansInFreePool(int var1) {
      this.initialBeansInFreePool = var1;
   }

   public int getInitialBeansInFreePool() {
      return this.initialBeansInFreePool;
   }

   public void setIdleTimeoutSecondsCache(int var1) {
      this.idleTimeoutSecondsCache = var1;
   }

   public int getIdleTimeoutSecondsCache() {
      return this.idleTimeoutSecondsCache;
   }

   public void setIdleTimeoutSecondsPool(int var1) {
      this.idleTimeoutSecondsPool = var1;
   }

   public int getIdleTimeoutSecondsPool() {
      return this.idleTimeoutSecondsPool;
   }

   public void setCacheType(String var1) {
      this.cacheType = var1;
   }

   public String getCacheType() {
      return this.cacheType;
   }

   public void setReadTimeoutSeconds(int var1) {
      this.readTimeoutSeconds = var1;
   }

   public int getReadTimeoutSeconds() {
      return this.readTimeoutSeconds;
   }

   public void setConcurrencyStrategy(String var1) {
      this.concurrencyStrategy = var1;
   }

   public String getConcurrencyStrategy() {
      return this.concurrencyStrategy;
   }

   public void validateSelf() {
   }

   public Iterator getSubObjectsIterator() {
      return Collections.EMPTY_SET.iterator();
   }
}
