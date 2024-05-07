package weblogic.ejb.container.cache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EnterpriseBean;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.CachingManager;
import weblogic.ejb.spi.CachingManagerBase;
import weblogic.ejb.spi.ReInitializableCache;
import weblogic.ejb20.cache.CacheFullException;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;

public final class LRUCache extends BaseCache implements ReInitializableCache {
   private BaseCache.Node lruChainHead;
   private BaseCache.Node lruChainTail;
   private CachingManager cachingManager;

   public LRUCache(String var1, int var2) {
      super(var1, var2);
      this.setLruChainTail((BaseCache.Node)null);
      this.setLruChainHead((BaseCache.Node)null);
   }

   public LRUCache(String var1, long var2) {
      super(var1, var2);
      this.setLruChainTail((BaseCache.Node)null);
      this.setLruChainHead((BaseCache.Node)null);
   }

   public void register(CachingManagerBase var1) {
      this.cachingManager = (CachingManager)var1;
   }

   public synchronized EnterpriseBean get(CacheKey var1) {
      assert this.validateDataStructures();

      BaseCache.Node var2 = null;
      var2 = (BaseCache.Node)this.cache.get(var1);
      if (var2 != null) {
         var2.touch();
         var2.pin();
         this.lruUse(var2);

         assert this.validateDataStructures();

         return var2.getBean();
      } else {
         return null;
      }
   }

   public void releaseEntityBean(BaseCache.Node var1) {
   }

   public synchronized void put(CacheKey var1, EnterpriseBean var2) throws CacheFullException {
      assert !this.contains(var1);

      List var3 = null;
      int var4 = var1.getCallback().getBeanSize();
      long var5 = this.currentCacheSize + (long)var4 - this.getMaxCacheSize();

      try {
         if (var5 > 0L) {
            var3 = this.attemptToFreeSpace(var5);
         }

         if (this.currentCacheSize + (long)var4 > this.getMaxCacheSize()) {
            throw new CacheFullException();
         }

         BaseCache.Node var7 = new BaseCache.Node();
         var7.setBean(var2);
         var7.setKey(var1);
         var7.setActive();
         var7.pin();
         BaseCache.Node var8 = (BaseCache.Node)this.cache.put(var1, var7);
         if (var8 != null) {
            this.cache.put(var1, var8);
            throw new AssertionError("Attempt to replace ctx - old ctx: '" + var8 + "', new ctx: " + var7 + "'");
         }

         this.currentCacheSize += (long)var4;
         this.prependToLRU(var7);
         if (debugLogger.isDebugEnabled()) {
            debug("node " + var7.getKey() + " is added to cache, size:" + this.currentCacheSize);
         }
      } finally {
         if (var3 != null) {
            Iterator var11 = var3.iterator();

            while(var11.hasNext()) {
               BaseCache.Node var12 = (BaseCache.Node)var11.next();

               assert !this.inLRUChain(var12);

               var12.getCallback().swapOut(var12.getKey(), var12.getBean());
               if (debugLogger.isDebugEnabled()) {
                  debug("node " + var12.getKey() + " is passivated.");
               }
            }
         }

         assert this.validateDataStructures();

      }

   }

   public void removeOnError(CacheKey var1) {
      throw new AssertionError("removeOnError in LRUCache");
   }

   public synchronized void remove(CacheKey var1) {
      assert this.validateDataStructures();

      BaseCache.Node var2 = (BaseCache.Node)this.cache.remove(var1);
      if (debugLogger.isDebugEnabled()) {
         debug("*** Removing key: " + var1);
      }

      if (var2 != null) {
         this.currentCacheSize -= (long)var2.getSize();

         assert var2.getKey().equals(var1);

         if (var2.pinned()) {
            var2.unpin();
         }

         this.removeFromLRU(var2);
         var2.setFree();
         var2.getCallback().removedFromCache(var1, var2.getBean());

         assert !this.contains(var1);

      }
   }

   synchronized void cacheScrubber() {
      Iterator var1 = this.getPassivationList();
      if (debugLogger.isDebugEnabled()) {
         debug(" Timer passivating: " + this.cacheName);
      }

      while(var1.hasNext()) {
         BaseCache.Node var2 = (BaseCache.Node)var1.next();
         this.passivateNode(var2);
      }

      if (dumpCache) {
         Debug.say(this.cacheDump());
      }

   }

   public Iterator getPassivationList() {
      ArrayList var1 = new ArrayList();
      boolean var2 = ++this.scrubCount % 5 == 0;
      BaseCache.Node var3 = this.lruChainTail;

      BaseCache.Node var6;
      for(long var4 = System.currentTimeMillis() - this.scrubIntervalMillis; var3 != null; var3 = var6) {
         assert this.inLRUChain(var3);

         var6 = var3.prev;
         if (var3.olderThan(var4) && !var3.pinned()) {
            this.remove(var3.getKey());
            var1.add(var3);
         } else if (!var2) {
            break;
         }
      }

      return var1.iterator();
   }

   public void reInitializeCacheAndPools() {
      if (debugLogger.isDebugEnabled()) {
         debug(this.cacheName + " reInitializeCacheAndPools, cache size is " + this.cache.size());
      }

      ((BeanManager)this.cachingManager).reInitializePool();
      this.reInitializeCache();
      if (debugLogger.isDebugEnabled()) {
         debug(this.cacheName + " cache reInitialization complete, size is " + this.cache.size());
      }

   }

   private synchronized void reInitializeCache() {
      BaseCache.Node var2;
      for(BaseCache.Node var1 = this.lruChainTail; var1 != null; var1 = var2) {
         assert this.inLRUChain(var1);

         var2 = var1.prev;
         if (!var1.pinned()) {
            this.remove(var1.getKey());
            this.passivateNode(var1);
         }
      }

      if (dumpCache) {
         Debug.say(this.cacheDump());
      }

   }

   public void beanImplClassChangeNotification() {
      throw new AssertionError("LRUCache doesn't support bean impl changes");
   }

   public synchronized void updateMaxBeansInCache(int var1) {
      if (this.usesMaxBeansInCache()) {
         List var2 = null;
         this.setMaxBeansInCache(var1);
         if (this.getCurrentSize() > (long)var1) {
            var2 = this.attemptToFreeSpace(this.getCurrentSize() - (long)var1);
         }

         if (var2 != null) {
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               BaseCache.Node var4 = (BaseCache.Node)var3.next();
               var4.getCallback().swapOut(var4.getKey(), var4.getBean());
            }
         }
      }

   }

   public void updateMaxCacheSize(int var1) {
      throw new AssertionError("LRUCache doesn't support max-cache-size");
   }

   private List attemptToFreeSpace(long var1) {
      ArrayList var3 = null;
      BaseCache.Node var4 = this.lruChainTail;
      long var5 = 0L;

      while(var5 < var1) {
         while(var4 != null && var4.pinned()) {
            var4 = var4.prev;
         }

         if (var4 == null) {
            return var3;
         }

         if (var3 == null) {
            var3 = new ArrayList();
         }

         var3.add(var4);
         BaseCache.Node var7 = var4;
         var4 = var4.prev;
         this.remove(var7.getKey());
         if (debugLogger.isDebugEnabled()) {
            debug("node " + var4.getKey() + " is removed from cache, size:" + this.cache.size());
         }
      }

      return var3;
   }

   protected String cacheDump() {
      StringBuffer var1 = new StringBuffer();
      if (!this.cache.isEmpty()) {
         var1.append("Dumping EJBCache for: ");
         var1.append(this.cacheName);
         var1.append(" [ Cache size: ");
         var1.append(this.cache.size());
         var1.append(" ]");

         for(BaseCache.Node var2 = this.lruChainHead; var2 != null; var2 = var2.next) {
            CacheKey var3 = var2.getKey();
            var1.append(" [ PK: ");
            var1.append(var3);
            if (var2.pinned()) {
               var1.append(" Locked by: ");
               var1.append("owner");
            }

            var1.append(" ] ");
         }
      } else {
         var1.append("Cache is empty for: " + this.cacheName + "\n");
      }

      return var1.toString();
   }

   private void chain(BaseCache.Node var1, BaseCache.Node var2) {
      if (var1 != null) {
         var1.next = var2;
      }

      if (var2 != null) {
         var2.prev = var1;
      }

      if (var2 == this.lruChainHead && var1 != null) {
         this.setLruChainHead(var1);
      }

      if (var1 == this.lruChainTail && var2 != null) {
         this.setLruChainTail(var2);
      }

   }

   private void prependToLRU(BaseCache.Node var1) {
      assert !this.inLRUChain(var1);

      this.chain((BaseCache.Node)null, var1);
      this.chain(var1, this.lruChainHead);
   }

   private void removeFromLRU(BaseCache.Node var1) {
      assert this.inLRUChain(var1);

      BaseCache.Node var2 = var1.prev;
      BaseCache.Node var3 = var1.next;
      this.chain(var2, var3);
      var1.next = null;
      var1.prev = null;
      if (var1 == this.lruChainHead) {
         this.setLruChainHead(var3);
      }

      if (var1 == this.lruChainTail) {
         this.setLruChainTail(var2);
      }

   }

   private void setLruChainHead(BaseCache.Node var1) {
      this.lruChainHead = var1;
   }

   private void setLruChainTail(BaseCache.Node var1) {
      this.lruChainTail = var1;
   }

   private void lruUse(BaseCache.Node var1) {
      assert this.lruUse_assertion(var1);

      if (var1 != this.lruChainHead && var1.prev != null) {
         BaseCache.Node var2 = var1.prev;
         BaseCache.Node var3 = var1.next;
         this.chain(var2, var3);
         if (var1 == this.lruChainHead) {
            this.setLruChainHead(var3);
         }

         if (var1 == this.lruChainTail) {
            this.setLruChainTail(var2);
         }

         this.chain((BaseCache.Node)null, var1);
         this.chain(var1, this.lruChainHead);

         assert this.validateDataStructures();

      }
   }

   private boolean lruUse_assertion(BaseCache.Node var1) {
      assert this.inLRUChain(var1);

      assert this.validateDataStructures();

      return true;
   }

   protected boolean validateDataStructures() {
      this.validateLRUChain();
      this.validateCache();
      return true;
   }

   private boolean inLRUChain(BaseCache.Node var1) {
      for(BaseCache.Node var2 = var1; var2 != null; var2 = var2.prev) {
         if (var2 == this.lruChainHead) {
            return true;
         }
      }

      return false;
   }

   private void validateCache() {
      BaseCache.Node var1 = this.lruChainHead;
      int var2 = 0;
      HashSet var3 = new HashSet();

      long var4;
      for(var4 = 0L; var1 != null; ++var2) {
         EnterpriseBean var6 = var1.getBean();

         assert this.cache.get(var1.getKey()) != null;

         assert var3.add(var6);

         var4 += (long)var1.getSize();
         var1 = var1.next;
      }

      assert var2 == this.cache.size();

      assert var4 == this.currentCacheSize;

   }

   private void validateLRUChain() {
      if (this.lruChainHead == null) {
         assert this.lruChainTail == null && this.cache.size() == 0 && this.currentCacheSize == 0L : "lruChainTail = " + this.lruChainTail + ", size = " + this.cache.size() + ", currentCacheSize= " + this.currentCacheSize;
      } else if (this.lruChainTail == null) {
         assert this.lruChainHead == null && this.cache.size() == 0 && this.currentCacheSize == 0L : "lruChainHead = " + this.lruChainHead + ", size = " + this.cache.size() + ", currentCacheSize= " + this.currentCacheSize;
      } else if (this.cache.size() == 0) {
         assert this.lruChainHead == null && this.lruChainTail == null && this.currentCacheSize == 0L : "lruChainHead = " + this.lruChainHead + ", lruChainTail = " + this.lruChainTail + ", size = " + this.cache.size() + ", currentCacheSize= " + this.currentCacheSize;
      } else if (this.currentCacheSize != 0L) {
         assert this.lruChainHead.prev == null;

         assert this.lruChainTail.next == null;

         int var1 = 0;
         HashSet var2 = new HashSet();

         for(BaseCache.Node var3 = this.lruChainHead; var3 != null; var3 = var3.next) {
            ++var1;

            assert var2.add(var3);
         }

         assert var1 == this.cache.size();

      } else {
         assert this.lruChainHead == null && this.lruChainTail == null && this.cache.size() == 0 : "lruChainHead = " + this.lruChainHead + ", lruChainTail = " + this.lruChainTail + ", size = " + this.cache.size() + ", currentCacheSize= " + this.currentCacheSize;
      }
   }

   private static void debug(String var0) {
      debugLogger.debug("[LRUCache] " + var0);
   }
}
