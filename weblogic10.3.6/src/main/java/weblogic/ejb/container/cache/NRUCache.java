package weblogic.ejb.container.cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EnterpriseBean;
import javax.ejb.EntityBean;
import javax.ejb.SessionBean;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.CachingManager;
import weblogic.ejb.spi.CachingManagerBase;
import weblogic.ejb.spi.ReInitializableCache;
import weblogic.ejb20.cache.CacheFullException;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;

public final class NRUCache extends BaseCache implements ReInitializableCache {
   private final Queue freeQueue = new Queue();
   private final Queue activeQueue = new Queue();
   private final Queue inActiveQueue = new Queue();
   private static final int MIN_CAPACITY = 8;
   private long minFreeSize;
   private long targetFreeSize;
   private long targetInactiveSize;
   private int maxBeanSize = 0;
   private List cachingManagers = new ArrayList();
   private boolean isEntityCache = false;

   public NRUCache(String var1, int var2) {
      super(var1, var2);
   }

   public NRUCache(String var1, long var2) {
      super(var1, var2);
   }

   public void setMaxBeansInCache(int var1) {
      super.setMaxBeansInCache(var1);
      this.updateCacheValues();
   }

   public void register(CachingManagerBase var1) {
      CachingManager var2 = (CachingManager)var1;
      if (!this.cachingManagers.contains(var2)) {
         this.cachingManagers.add(var2);
      }

      this.isEntityCache = var2.isEntityManager();
      this.maxBeanSize = Math.max(this.maxBeanSize, var2.getBeanSize());
      this.updateCacheValues();
   }

   public void updateCacheValues() {
      this.setMaxCacheSize(Math.max((long)(8 * this.maxBeanSize), this.getMaxCacheSize()));
      this.minFreeSize = Math.min(this.getMaxCacheSize() / 8L, (long)(20 * this.maxBeanSize));
      this.targetFreeSize = Math.min(this.getMaxCacheSize() / 8L, (long)(10 * this.maxBeanSize));
      this.targetInactiveSize = this.getMaxCacheSize() / 8L;
      if (debugLogger.isDebugEnabled()) {
         debug("cache values for: " + this.cacheName);
         debug("maxCacheSize- " + this.getMaxCacheSize());
         debug("maxBeanSize- " + this.maxBeanSize);
         debug("minFreeSize- " + this.minFreeSize);
         debug("targetFreeSize- " + this.targetFreeSize);
         debug("targetInactiveSize- " + this.targetInactiveSize);
      }

   }

   public synchronized EnterpriseBean get(CacheKey var1, boolean var2) {
      assert this.validateDataStructures();

      BaseCache.Node var3 = (BaseCache.Node)this.cache.get(var1);
      if (var3 == null) {
         assert this.findKeyInQueues(var1) == null;

         return null;
      } else {
         EnterpriseBean var4;
         if (var3.isFree()) {
            assert this.freeQueue.contains(var3);

            var3.setActive();
            var4 = var3.getBean();
            CachingManager var5 = var3.getCallback();
            var5.swapIn(var1, var4);
            this.freeQueue.remove(var3);
            this.activeQueue.push(var3);
         } else if (var3.isInActive()) {
            assert this.inActiveQueue.contains(var3);

            var3.setActive();
            this.inActiveQueue.remove(var3);
            this.activeQueue.push(var3);
         } else {
            assert this.activeQueue.contains(var3);
         }

         var4 = var3.getBean();

         assert this.activeQueue.contains(var3);

         assert this.validateDataStructures();

         if (var2) {
            var3.pin();
         }

         return var4;
      }
   }

   public EnterpriseBean get(CacheKey var1) {
      return this.get(var1, true);
   }

   private BaseCache.Node getFreeNode(int var1) throws CacheFullException {
      BaseCache.Node var2 = null;
      int var3;
      CacheKey var4;
      EnterpriseBean var5;
      CachingManager var6;
      if (this.freeQueue.size() >= (long)var1) {
         for(var3 = 0; var3 < var1; var3 += var2.getSize()) {
            var2 = this.freeQueue.pop();
            var4 = var2.getKey();
            var5 = var2.getBean();
            var6 = var2.getCallback();
            if (var5 != null) {
               this.cache.remove(var4);
               this.currentCacheSize -= (long)var2.getSize();
               var6.removedFromCache(var4, var5);
            }
         }
      }

      if (this.currentCacheSize + (long)var1 <= this.getMaxCacheSize()) {
         if (var2 == null) {
            var2 = new BaseCache.Node();
         }
      } else {
         this.reclaimNodes(this.currentCacheSize + (long)var1 - this.getMaxCacheSize());
         if (this.freeQueue.size() < (long)var1) {
            throw new CacheFullException("Cache '" + this.cacheName + "' is at its limit of: " + this.getMaxCacheSize() + " *active* " + this.getCacheUnits() + ".");
         }

         for(var3 = 0; var3 < var1; var3 += var2.getSize()) {
            var2 = this.freeQueue.pop();
            var4 = var2.getKey();
            var5 = var2.getBean();
            var6 = var2.getCallback();
            if (var5 != null) {
               this.cache.remove(var4);
               this.currentCacheSize -= (long)var2.getSize();
               var6.removedFromCache(var4, var5);
            }
         }

         if (var2 == null) {
            var2 = new BaseCache.Node();
         }
      }

      assert var2 != null;

      return var2;
   }

   private void initializeNode(EnterpriseBean var1, CacheKey var2, BaseCache.Node var3) {
      var3.setBean(var1);
      var3.setKey(var2);
      var3.setActive();
      var3.pin();
      this.activeQueue.push(var3);
      BaseCache.Node var4 = (BaseCache.Node)this.cache.put(var2, var3);
      this.currentCacheSize += (long)var3.getSize();

      assert var4 == null : "Adding bean:" + var1 + " with key: " + var2 + " that was already in cache '" + this.cacheName + "' .";

      assert this.cache.get(var2) == var3;

      assert var3.getKey().equals(var2);

      assert this.validateDataStructures();

   }

   public synchronized void releaseEntityBean(BaseCache.Node var1) {
      if (this.isEntityCache) {
         var1.touch();
      }

   }

   public synchronized void put(CacheKey var1, EnterpriseBean var2) throws CacheFullException {
      if (debugLogger.isDebugEnabled()) {
         debug("Putting key: " + var1 + " into cache, current size is: " + this.getCurrentSize());
      }

      assert this.validateDataStructures();

      assert !this.contains(var1);

      int var3 = var1.getCallback().getBeanSize();
      BaseCache.Node var4 = this.getFreeNode(var3);
      this.initializeNode(var2, var1, var4);

      assert this.validateDataStructures();

   }

   public synchronized void remove(CacheKey var1) {
      this.remove(var1, false);
   }

   public synchronized void removeOnError(CacheKey var1) {
      this.remove(var1, true);
   }

   private void remove(CacheKey var1, boolean var2) {
      assert this.validateDataStructures();

      BaseCache.Node var3 = (BaseCache.Node)this.cache.remove(var1);
      if (debugLogger.isDebugEnabled()) {
         debug("*** Removing key: " + var1);
      }

      if (var3 != null) {
         this.currentCacheSize -= (long)var3.getSize();
         if (var2) {
            var3.getCallback().removedOnError(var1, var3.getBean());
         } else {
            var3.getCallback().removedFromCache(var1, var3.getBean());
         }

         assert var3 != null;

         assert var3.getKey().equals(var1);

         if (var3.pinned()) {
            var3.unpin();
         }

         if (var3.isActive()) {
            assert this.activeQueue.contains(var3);

            this.activeQueue.remove(var3);
            var3.setFree();
            this.freeQueue.push(var3);
         } else if (var3.isInActive()) {
            assert this.inActiveQueue.contains(var3);

            this.inActiveQueue.remove(var3);
            var3.setFree();
            this.freeQueue.push(var3);
         } else {
            assert this.freeQueue.contains(var3);
         }

         var3.setBean((EnterpriseBean)null);
         var3.setKey((CacheKey)null);

         assert this.findKeyInQueues(var1) == null;

         assert this.validateDataStructures();

      }
   }

   void cacheScrubber() {
      int var1 = this.scrubCache(true);
      if (this.isEntityCache) {
         if (debugLogger.isDebugEnabled()) {
            debug(this.cacheName + " after cache scrub, we've scrubbed " + var1 + " beans.");
         }

         if (var1 <= 0) {
            if (this.scrubIntervalMillis < 120000L) {
               this.scrubIntervalMillis += this.scrubIntervalMillis;
               if (debugLogger.isDebugEnabled()) {
                  debug(this.cacheName + "  " + " scrubIntervalMillis: " + this.scrubIntervalMillis + " is less than 2 minutes " + "and we've scrubbed no beans.  Doubling the interval till next scrubbing to " + this.scrubIntervalMillis);
               }

               this.scrubberTimer.stopScrubber();
               this.scrubberTimer.setScrubInterval(this.scrubIntervalMillis);
               this.scrubberTimer.startScrubber();
            }
         } else if (this.scrubIntervalMillis != this.scrubIntervalMillisDD) {
            if (debugLogger.isDebugEnabled()) {
               debug(this.cacheName + "  " + " scrubIntervalMillis: " + this.scrubIntervalMillis + " is not equal to the deployed value " + " and we've scrubbed some beans during this scrubbing event.  Resetting scrubInterval to it's " + " deployed value: " + this.scrubIntervalMillisDD);
            }

            this.scrubIntervalMillis = this.scrubIntervalMillisDD;
            this.scrubberTimer.stopScrubber();
            this.scrubberTimer.setScrubInterval(this.scrubIntervalMillis);
            this.scrubberTimer.startScrubber();
         }
      }

   }

   private int scrubCache(boolean var1) {
      if (this.isEntityCache) {
         return this.newScrubber(var1);
      } else {
         return var1 ? this.legacyScrubber(var1) : this.newScrubber(var1);
      }
   }

   private int legacyScrubber(boolean var1) {
      if (var1) {
         int var2 = 0;
         synchronized(this) {
            long var4 = this.freeQueue.size() + (this.getMaxCacheSize() - this.getCurrentSize());
            if (var4 < this.minFreeSize) {
               var2 += this.reclaimNodes(this.targetFreeSize);
            }
         }

         if (dumpCache) {
            Debug.say(this.cacheDump());
         }

         return var2;
      } else {
         throw new AssertionError(" legacyScrubber called with idleTimeout = " + var1 + ", it should only be called with idleTimeout==true");
      }
   }

   private int newScrubber(boolean var1) {
      ArrayList var2 = new ArrayList();
      long var3 = System.currentTimeMillis();
      int var7 = 0;
      synchronized(this) {
         int var9 = (int)this.activeQueue.size();

         int var10;
         BaseCache.Node var11;
         int var12;
         for(var10 = 0; var10 < var9; ++var10) {
            var11 = this.activeQueue.pop();
            if (!var11.pinned()) {
               if (var1) {
                  var12 = var11.getCallback().getIdleTimeoutSeconds();
                  if (var12 <= 0) {
                     this.activeQueue.push(var11);
                     continue;
                  }

                  if (!var11.idleLongerThan((long)(var12 * 1000))) {
                     this.activeQueue.push(var11);
                     continue;
                  }
               }

               this.removeNode(var11);
               var2.add(var11);
               ++var7;
               if (!this.isEntityCache) {
                  var11.getCallback().removedFromCache(var11.getKey(), var11.getBean());
               }
            } else {
               this.activeQueue.push(var11);
            }
         }

         var9 = (int)this.inActiveQueue.size();

         for(var10 = 0; var10 < var9; ++var10) {
            var11 = this.inActiveQueue.pop();
            if (!var11.pinned()) {
               if (var1) {
                  var12 = var11.getCallback().getIdleTimeoutSeconds();
                  if (var12 <= 0) {
                     this.inActiveQueue.push(var11);
                     continue;
                  }

                  if (!var11.idleLongerThan((long)(var12 * 1000))) {
                     this.inActiveQueue.push(var11);
                     continue;
                  }
               }

               this.removeNode(var11);
               var2.add(var11);
               ++var7;
               if (!this.isEntityCache) {
                  var11.getCallback().removedFromCache(var11.getKey(), var11.getBean());
               }
            }
         }

         var9 = (int)this.freeQueue.size();

         for(var10 = 0; var10 < var9; ++var10) {
            var11 = this.freeQueue.pop();
            if (var11.getBean() != null) {
               if (var1) {
                  var12 = var11.getCallback().getIdleTimeoutSeconds();
                  if (var12 <= 0) {
                     this.freeQueue.push(var11);
                     continue;
                  }

                  if (!var11.idleLongerThan((long)(var12 * 1000))) {
                     this.freeQueue.push(var11);
                     continue;
                  }
               }

               var11.getCallback().removedFromCache(var11.getKey(), var11.getBean());
            }

            this.removeNode(var11);
            ++var7;
         }
      }

      if (this.isEntityCache) {
         Iterator var8 = var2.iterator();

         while(var8.hasNext()) {
            BaseCache.Node var15 = (BaseCache.Node)var8.next();
            CachingManager var16 = var15.getCallback();
            var16.swapOut(var15.getKey(), (EntityBean)var15.getBean());
            var16.removedFromCache(var15.getKey(), (EntityBean)var15.getBean());
         }
      }

      return var7;
   }

   public void reInitializeCacheAndPools() {
      if (debugLogger.isDebugEnabled()) {
         debug(this.cacheName + " reInitializeCacheAndPools, cache size is " + this.cache.size());
      }

      Iterator var1 = this.cachingManagers.iterator();

      while(var1.hasNext()) {
         BeanManager var2 = (BeanManager)var1.next();
         var2.reInitializePool();
      }

      this.reInitializeCache();
      if (debugLogger.isDebugEnabled()) {
         debug(this.cacheName + " cache reInitialization complete, size is " + this.cache.size());
      }

   }

   public void reInitializeCache() {
      this.scrubCache(false);
   }

   public void beanImplClassChangeNotification() {
      ArrayList var1 = new ArrayList();
      synchronized(this) {
         long var3 = this.freeQueue.size();

         long var5;
         for(var5 = 0L; var5 < var3; ++var5) {
            BaseCache.Node var7 = this.freeQueue.pop();
            this.removeNode(var7);
            CachingManager var8 = var7.getCallback();
            var8.removedFromCache(var7.getKey(), var7.getBean());
         }

         var5 = this.inActiveQueue.size();

         long var15;
         for(var15 = 0L; var15 < var5; ++var15) {
            BaseCache.Node var9 = this.inActiveQueue.pop();
            this.removeNode(var9);
            var1.add(var9);
         }

         var15 = this.activeQueue.size();

         for(long var16 = 0L; var16 < var15; ++var16) {
            BaseCache.Node var11 = this.activeQueue.pop();
            if (!var11.pinned()) {
               this.removeNode(var11);
               var1.add(var11);
            } else {
               this.activeQueue.push(var11);
            }
         }

         assert this.validateDataStructures();
      }

      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         BaseCache.Node var14 = (BaseCache.Node)var2.next();
         CachingManager var4 = var14.getCallback();
         var4.swapOut(var14.getKey(), (EntityBean)var14.getBean());
         var4.removedFromCache(var14.getKey(), (EntityBean)var14.getBean());
      }

   }

   public synchronized void updateMaxBeansInCache(int var1) {
      if (this.usesMaxBeansInCache()) {
         this.setMaxBeansInCache(var1);
         this.cacheScrubber();
      }

   }

   public void updateMaxCacheSize(int var1) {
      if (!this.usesMaxBeansInCache()) {
         this.setMaxCacheSize((long)var1);
         this.updateCacheValues();
         this.cacheScrubber();
      }

   }

   private void removeNode(BaseCache.Node var1) {
      CacheKey var2 = var1.getKey();
      EnterpriseBean var3 = var1.getBean();
      if (var3 != null) {
         this.cache.remove(var2);
         this.currentCacheSize -= (long)var1.getSize();
      }

   }

   private void moveActiveToInactive() {
      BaseCache.Node var3;
      for(long var1 = this.activeQueue.size(); var1 > 0L; var1 -= (long)var3.getSize()) {
         if (this.inActiveQueue.size() >= this.targetInactiveSize) {
            return;
         }

         var3 = this.activeQueue.pop();

         assert var3 != null;

         if (var3.pinned()) {
            this.activeQueue.push(var3);
         } else {
            if (!this.isEntityCache) {
               var3.touch();
            }

            var3.setInActive();
            this.inActiveQueue.push(var3);
         }
      }

   }

   private int moveInActiveToFree(long var1) {
      int var3 = 0;

      while(this.freeQueue.size() < var1) {
         BaseCache.Node var4 = this.inActiveQueue.pop();
         if (var4 == null) {
            return var3;
         }

         EnterpriseBean var5 = var4.getBean();
         if (var5 instanceof SessionBean && var4.idleLongerThan(this.scrubIntervalMillis)) {
            CacheKey var6 = var4.getKey();
            this.cache.remove(var6);
            int var7 = var4.getSize();
            this.currentCacheSize -= (long)var7;
            var3 += var7;
            SessionBean var8 = (SessionBean)var5;

            try {
               var8.ejbRemove();
            } catch (Throwable var10) {
               EJBLogger.logExceptionDuringEJBRemove(var10);
            }

            var4.getCallback().removedFromCache(var6, var5);
            var4.setBean((EnterpriseBean)null);
            var4.setKey((CacheKey)null);
         } else {
            assert !var4.pinned();

            assert var4.getBean() != null;

            assert var4.getKey() != null;

            assert var4.getCallback() != null;

            var3 += var4.getSize();
            var4.getCallback().swapOut(var4.getKey(), var4.getBean());
         }

         var4.setFree();
         this.freeQueue.push(var4);
      }

      return var3;
   }

   private int reclaimNodes(long var1) {
      assert this.validateDataStructures();

      int var3 = 0;
      var3 += this.moveInActiveToFree(var1);
      this.moveActiveToInactive();
      if (this.freeQueue.size() == 0L) {
         var3 += this.moveInActiveToFree(this.targetFreeSize);
      }

      return var3;
   }

   protected boolean validateDataStructures() {
      int var2 = 0;

      BaseCache.Node var1;
      for(var1 = this.freeQueue.head; var1 != null; var1 = var1.next) {
         ++var2;
      }

      for(var1 = this.inActiveQueue.head; var1 != null; var1 = var1.next) {
         ++var2;
      }

      for(var1 = this.activeQueue.head; var1 != null; var1 = var1.next) {
         ++var2;
      }

      if (var2 < this.cache.size()) {
         throw new AssertionError("nodeCnt was :" + var2 + " but cache size is " + this.cache.size());
      } else {
         Debug.assertion(var2 >= this.cache.size());
         Debug.assertion(var2 <= this.getMaxBeansInCache());
         Debug.assertion(this.freeQueue.size() >= 0L);
         Debug.assertion(this.inActiveQueue.size() >= 0L);
         Debug.assertion(this.activeQueue.size() >= 0L);
         Debug.assertion(this.currentCacheSize >= 0L);
         long var3 = this.freeQueue.size() + this.inActiveQueue.size() + this.activeQueue.size();
         if (var3 < this.currentCacheSize) {
            throw new AssertionError("listSize was :" + var3 + " but cache size is " + this.currentCacheSize);
         } else {
            long var5 = 0L;

            CacheKey var8;
            int var9;
            for(Iterator var7 = this.cache.keySet().iterator(); var7.hasNext(); Debug.assertion(var9 == 1)) {
               var8 = (CacheKey)var7.next();
               var1 = (BaseCache.Node)this.cache.get(var8);
               var5 += (long)var1.getSize();
               Debug.assertion(var8 == var1.getKey());
               var9 = 0;
               if (this.activeQueue.contains(var1)) {
                  ++var9;
               }

               if (this.inActiveQueue.contains(var1)) {
                  ++var9;
               }

               if (this.freeQueue.contains(var1)) {
                  ++var9;
               }
            }

            Debug.assertion(var5 == this.currentCacheSize);
            var5 = 0L;

            EnterpriseBean var10;
            for(var1 = this.activeQueue.head; var1 != null; var1 = var1.next) {
               var10 = var1.getBean();
               Debug.assertion(var10 != null);
               var8 = var1.getKey();
               Debug.assertion(var8 != null);
               Debug.assertion(var1 == this.cache.get(var8));
               var5 += (long)var1.getSize();
            }

            Debug.assertion(var5 == this.activeQueue.size());
            var5 = 0L;

            for(var1 = this.inActiveQueue.head; var1 != null; var1 = var1.next) {
               var10 = var1.getBean();
               Debug.assertion(var10 != null);
               var8 = var1.getKey();
               Debug.assertion(var8 != null);
               Debug.assertion(var1 == this.cache.get(var8));
               var5 += (long)var1.getSize();
            }

            Debug.assertion(var5 == this.inActiveQueue.size());
            var5 = 0L;

            for(var1 = this.freeQueue.head; var1 != null; var1 = var1.next) {
               var10 = var1.getBean();
               if (var10 != null) {
                  var8 = var1.getKey();
                  Debug.assertion(var8 != null);
                  Debug.assertion(var1 == this.cache.get(var8));
               }

               var5 += (long)var1.getSize();
            }

            Debug.assertion(var5 == this.freeQueue.size());

            for(var1 = this.inActiveQueue.head; var1 != null; var1 = var1.next) {
               Debug.assertion(!var1.pinned());
            }

            for(var1 = this.freeQueue.head; var1 != null; var1 = var1.next) {
               Debug.assertion(!var1.pinned());
            }

            return true;
         }
      }
   }

   private BaseCache.Node findKeyInQueue(Queue var1, CacheKey var2) {
      for(BaseCache.Node var3 = var1.head; var3 != null; var3 = var3.next) {
         if (var2.equals(var3.getKey())) {
            return var3;
         }
      }

      return null;
   }

   private BaseCache.Node findKeyInQueues(CacheKey var1) {
      BaseCache.Node var2 = this.findKeyInQueue(this.activeQueue, var1);
      if (var2 != null) {
         return var2;
      } else {
         var2 = this.findKeyInQueue(this.inActiveQueue, var1);
         return var2 != null ? var2 : this.findKeyInQueue(this.freeQueue, var1);
      }
   }

   private static void debug(String var0) {
      debugLogger.debug("[NRUCache] " + var0);
   }

   private static class Queue {
      private BaseCache.Node head = null;
      private BaseCache.Node tail = null;
      private long size = 0L;

      Queue() {
      }

      public boolean contains(BaseCache.Node var1) {
         for(BaseCache.Node var2 = this.head; var2 != null; var2 = var2.next) {
            if (var2 == var1) {
               return true;
            }
         }

         return false;
      }

      final void remove(BaseCache.Node var1) {
         assert this.contains(var1);

         this.size -= (long)var1.getSize();
         if (this.head == var1) {
            this.head = var1.next;
         } else {
            var1.prev.next = var1.next;
         }

         if (this.tail == var1) {
            this.tail = var1.prev;
         } else {
            var1.next.prev = var1.prev;
         }

         assert !this.contains(var1);

         assert this.tail == null || this.contains(this.tail);

         assert this.head == null || this.contains(this.head);

      }

      long size() {
         return this.size;
      }

      final void push(BaseCache.Node var1) {
         assert !this.contains(var1);

         this.size += (long)var1.getSize();
         if (this.tail == null) {
            assert this.head == null;

            this.head = var1;
            this.tail = var1;
            var1.prev = null;
            var1.next = null;
         } else {
            assert this.head != null;

            assert this.tail.next == null;

            this.tail.next = var1;
            var1.prev = this.tail;
            var1.next = null;
            this.tail = var1;
         }

      }

      final BaseCache.Node pop() {
         if (this.head == null) {
            assert this.tail == null;

            assert this.size == 0L;

            return null;
         } else {
            assert this.size > 0L;

            this.size -= (long)this.head.getSize();
            BaseCache.Node var1 = this.head;
            this.head = this.head.next;
            if (this.head == null) {
               assert this.size == 0L;

               this.tail = null;
            } else {
               this.head.prev = null;
            }

            assert !this.contains(var1);

            return var1;
         }
      }
   }
}
