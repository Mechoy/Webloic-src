package weblogic.ejb.container.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.ejb.EntityBean;
import javax.transaction.Transaction;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.CachingManager;
import weblogic.ejb.container.interfaces.MultiVersionCache;
import weblogic.ejb.container.interfaces.PassivatibleEntityCache;
import weblogic.ejb.container.interfaces.WLEntityBean;
import weblogic.ejb.container.manager.TTLManager;
import weblogic.ejb.container.persistence.spi.CMPBean;
import weblogic.ejb.container.persistence.spi.RSInfo;
import weblogic.ejb.spi.CachingManagerBase;
import weblogic.ejb.spi.ReInitializableCache;
import weblogic.ejb.spi.ScrubbedCache;
import weblogic.ejb20.cache.CacheFullException;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.utils.Debug;

public final class EntityCache implements MultiVersionCache, PassivatibleEntityCache, ReInitializableCache, ScrubbedCache, TimerListener {
   private static final DebugLogger debugLogger;
   private final ConcurrentHashMap<CacheKey, KeyData> cachedEntitiesByKey = new ConcurrentHashMap();
   private final SizeTracker sizeTracker = new SizeTracker();
   private final boolean usesMaxBeansInCache;
   private long limit;
   private final String cacheName;
   private List<CachingManagerBase> cachingManagers = new ArrayList();
   private boolean disableReadyCache = false;
   private long scrubIntervalMillisDD;
   private long scrubIntervalMillis;
   private EntityPassivator passivator;
   private CacheScrubberTimer scrubberTimer;

   public EntityCache(String var1, int var2) {
      this.cacheName = var1;
      this.limit = (long)var2;
      this.usesMaxBeansInCache = true;
      this.passivator = new EntityPassivator(this);
      this.scrubberTimer = new CacheScrubberTimer(this, 0L, this.cacheName);
      if (debugLogger.isDebugEnabled()) {
         debug(this.cacheName + " Creating EntityCache for " + var1 + " with " + this.limit);
      }

   }

   public EntityCache(String var1, long var2) {
      this.cacheName = var1;
      this.limit = var2;
      this.usesMaxBeansInCache = false;
      this.passivator = new EntityPassivator(this);
      this.scrubberTimer = new CacheScrubberTimer(this, 0L, this.cacheName);
      if (debugLogger.isDebugEnabled()) {
         debug(this.cacheName + " Creating EntityCache for " + var1 + " with " + this.limit);
      }

   }

   public void register(CachingManagerBase var1) {
      if (debugLogger.isDebugEnabled()) {
         debug(this.cacheName + " register CachingManager " + var1);
      }

      if (!this.cachingManagers.contains(var1)) {
         this.cachingManagers.add(var1);
      }

   }

   public List<CachingManagerBase> getCachingManagers() {
      return this.cachingManagers;
   }

   public int getMaxBeansInCache() {
      return (int)this.limit;
   }

   public void setMaxBeansInCache(int var1) {
      this.limit = (long)var1;
   }

   public boolean usesMaxBeansInCache() {
      return this.usesMaxBeansInCache;
   }

   public long getMaxCacheSize() {
      return 2147483647L;
   }

   public void setMaxCacheSize(long var1) {
   }

   public void setDisableReadyCache(boolean var1) {
      this.disableReadyCache = var1;
   }

   public long getCurrentSize() {
      return this.sizeTracker.getSize();
   }

   public boolean contains(Object var1, CacheKey var2) {
      KeyData var3 = (KeyData)this.cachedEntitiesByKey.get(var2);
      return var3 != null && var3.contains(var1);
   }

   public EntityBean get(Object var1, CacheKey var2, boolean var3) throws InternalException {
      return this.get(var1, var2, (RSInfo)null, var3);
   }

   public EntityBean getValid(Object var1, CacheKey var2, boolean var3) throws InternalException {
      KeyData var4 = (KeyData)this.cachedEntitiesByKey.get(var2);
      return var4 == null ? null : var4.getValid(var1, var3);
   }

   public EntityBean get(Object var1, CacheKey var2, RSInfo var3, boolean var4) throws InternalException {
      KeyData var5 = (KeyData)this.cachedEntitiesByKey.get(var2);
      return var5 == null ? null : var5.get(var1, var3, var4);
   }

   public EntityBean getActive(Object var1, CacheKey var2, boolean var3) {
      KeyData var4 = (KeyData)this.cachedEntitiesByKey.get(var2);
      return var4 == null ? null : var4.getActive(var1, var3);
   }

   public EntityBean getIfNotTimedOut(Object var1, CacheKey var2, boolean var3) throws InternalException {
      KeyData var4 = (KeyData)this.cachedEntitiesByKey.get(var2);
      return var4 == null ? null : var4.getIfNotTimedOut(var1, var3);
   }

   public CMPBean getLastLoadedValidInstance(CacheKey var1) {
      KeyData var2 = (KeyData)this.cachedEntitiesByKey.get(var1);
      return var2 == null ? null : var2.getLastLoadedValidInstance();
   }

   public void put(Object var1, CacheKey var2, EntityBean var3, CachingManager var4, boolean var5) throws CacheFullException {
      int var6 = var4.getBeanSize();
      Transaction var7 = null;
      KeyData var8;
      if (!this.disableReadyCache) {
         if (var1 instanceof Transaction) {
            var7 = (Transaction)var1;
         }

         for(var8 = this.sizeTracker.acquireSpace(var7, this.limit, var6, var6); var8 != null; var8 = this.sizeTracker.acquireSpace(var7, this.limit, var6, 0)) {
            var8.shrink();
         }
      }

      var8 = (KeyData)this.cachedEntitiesByKey.get(var2);
      if (var8 == null) {
         var8 = new KeyData(var4, var2);
      }

      while(var8 != null) {
         var8 = var8.add(var3, var1, var5);
      }

   }

   public void unpin(Object var1, CacheKey var2) {
      if (!this.disableReadyCache) {
         KeyData var3 = (KeyData)this.cachedEntitiesByKey.get(var2);
         if (var3 != null) {
            var3.unpin(var1);
         }
      }
   }

   public void release(Object var1, CacheKey var2) {
      KeyData var3 = (KeyData)this.cachedEntitiesByKey.get(var2);
      if (var3 != null) {
         if (this.disableReadyCache) {
            var3.backToPool(var1);
         } else {
            var3.release(var1);
         }
      }

   }

   public int passivateUnModifiedBean(Transaction var1, CacheKey var2) {
      KeyData var3 = (KeyData)this.cachedEntitiesByKey.get(var2);
      if (debugLogger.isDebugEnabled() && var3 == null) {
         debug(this.cacheName + " passivateUnModifiedBean  pk " + var2.getPrimaryKey() + " is not in cache");
      }

      return var3 == null ? 0 : var3.passivateUnModifiedBean(var1);
   }

   public int passivateModifiedBean(Transaction var1, CacheKey var2, boolean var3) {
      KeyData var4 = (KeyData)this.cachedEntitiesByKey.get(var2);
      if (debugLogger.isDebugEnabled() && var4 == null) {
         debug(this.cacheName + " passivateModifiedBean  pk " + var2.getPrimaryKey() + " is not in cache");
      }

      return var4 == null ? 0 : var4.passivateModifiedBean(var1, var3);
   }

   public void removeOnError(Object var1, CacheKey var2) {
      KeyData var3 = (KeyData)this.cachedEntitiesByKey.get(var2);
      if (var3 != null) {
         var3.remove(var1, true);
      }

   }

   public void remove(Object var1, CacheKey var2) {
      KeyData var3 = (KeyData)this.cachedEntitiesByKey.get(var2);
      if (var3 != null) {
         var3.remove(var1, false);
      }

   }

   public void invalidate(Object var1, CacheKey var2) {
      KeyData var3 = (KeyData)this.cachedEntitiesByKey.get(var2);
      if (var3 != null) {
         if (debugLogger.isDebugEnabled() && var1 != null) {
            debug(this.cacheName + " invalidate: skipping invalidate, txOrThread- " + var1 + ", key- " + var2);
         }

         var3.invalidate(var1);
      }
   }

   public void invalidate(Object var1, Collection<CacheKey> var2) {
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         CacheKey var4 = (CacheKey)var3.next();
         this.invalidate(var1, var4);
      }

   }

   public void invalidateAll(Object var1) {
      this.invalidate(var1, (Collection)this.cachedEntitiesByKey.keySet());
   }

   public void beanImplClassChangeNotification() {
      Iterator var1 = this.cachedEntitiesByKey.values().iterator();

      while(var1.hasNext()) {
         ((KeyData)var1.next()).keepEnrolled(var1);
      }

   }

   public void updateMaxBeansInCache(int var1) {
      if (this.usesMaxBeansInCache) {
         this.limit = (long)var1;
      }

   }

   public void updateMaxCacheSize(int var1) {
      if (!this.usesMaxBeansInCache) {
         this.limit = (long)var1;
      }

   }

   public void setScrubInterval(int var1) {
      if (var1 > 0) {
         long var2 = (long)var1 * 1000L;
         if (this.scrubIntervalMillisDD <= 0L) {
            this.scrubIntervalMillisDD = var2;
         } else if (var2 < this.scrubIntervalMillisDD) {
            this.scrubIntervalMillisDD = var2;
         }

         this.scrubIntervalMillis = this.scrubIntervalMillisDD;
         this.scrubberTimer.setScrubInterval(this.scrubIntervalMillisDD);
      }

   }

   public void startScrubber() {
      this.scrubberTimer.startScrubber();
   }

   public void stopScrubber() {
      this.scrubberTimer.stopScrubber();
   }

   public void timerExpired(Timer var1) {
      int var2 = this.scrubCache(true);
      if (var2 <= 0) {
         if (this.scrubIntervalMillis < 120000L) {
            if (debugLogger.isDebugEnabled()) {
               debug(this.cacheName + " scrubIntervalMillis: " + this.scrubIntervalMillis + " is less than 2 minutes and we've scrubbed" + " no beans.  Doubling the interval till next scrubbing.");
            }

            this.scrubIntervalMillis += this.scrubIntervalMillis;
            this.scrubberTimer.stopScrubber();
            this.scrubberTimer.setScrubInterval(this.scrubIntervalMillis);
            this.scrubberTimer.startScrubber();
         }
      } else if (this.scrubIntervalMillis != this.scrubIntervalMillisDD) {
         if (debugLogger.isDebugEnabled()) {
            debug(this.cacheName + " scrubIntervalMillis: " + this.scrubIntervalMillis + " is not equal to the deployed value " + " and we've scrubbed some beans during this scrubbing event. " + " Resetting scrubInterval to its deployed value: " + this.scrubIntervalMillisDD);
         }

         this.resetScrubberTimerToDeployedValue();
      }

   }

   private void resetScrubberTimerToDeployedValue() {
      this.scrubIntervalMillis = this.scrubIntervalMillisDD;
      this.scrubberTimer.stopScrubber();
      this.scrubberTimer.setScrubInterval(this.scrubIntervalMillis);
      this.scrubberTimer.startScrubber();
   }

   private int scrubCache(boolean var1) {
      long var2 = System.currentTimeMillis();
      long var4 = var2;
      int var6 = 0;
      long var7 = 0L;
      if (debugLogger.isDebugEnabled()) {
         var7 = this.sizeTracker.getSize();
         debug("\n\n" + this.cacheName + " scrubCache() at " + var2 + ", cache size: " + var7 + (this.usesMaxBeansInCache ? " beans" : " bytes") + ", start cacheScrub");
      }

      Iterator var9 = this.cachedEntitiesByKey.values().iterator();

      while(true) {
         KeyData var10;
         while(true) {
            if (!var9.hasNext()) {
               if (debugLogger.isDebugEnabled()) {
                  long var13 = System.currentTimeMillis() - var2;
                  debug(this.cacheName + " scrubCache() completed.  Scrubbed " + var6 + " beans from cache in " + var13 + " ms." + "  size of cache is now: " + this.sizeTracker.getSize() + (this.usesMaxBeansInCache ? " beans" : " bytes") + " from orig size " + var7 + (this.usesMaxBeansInCache ? " beans" : " bytes") + "\n\n");
               }

               return var6;
            }

            var10 = (KeyData)var9.next();
            if (!var1) {
               break;
            }

            int var11 = var10.cachingManager.getIdleTimeoutSeconds();
            if (var11 > 0 && var10.oldestTimestamp() > 0L) {
               var4 = var2 - (long)var11 * 1000L;
               break;
            }
         }

         long var14 = var10.oldestTimestamp();
         if (var14 > 0L && var14 <= var4) {
            var6 += var10.shrinkIfExpired(var4, var9);
         }
      }
   }

   public void reInitializeCacheAndPools() {
      if (debugLogger.isDebugEnabled()) {
         debug(this.cacheName + " reInitializeCacheAndPools");
      }

      this.reInitializeCache();
      Iterator var1 = this.cachingManagers.iterator();

      while(var1.hasNext()) {
         CachingManagerBase var2 = (CachingManagerBase)var1.next();
         ((BeanManager)var2).reInitializePool();
      }

   }

   public void reInitializeCache() {
      this.scrubCache(false);
      if (this.scrubIntervalMillis != this.scrubIntervalMillisDD) {
         this.resetScrubberTimerToDeployedValue();
      }

   }

   public synchronized void printDataStructures() {
      System.out.println("ready--------------------------------------------");
      Iterator var1 = this.cachedEntitiesByKey.values().iterator();

      KeyData var2;
      while(var1.hasNext()) {
         var2 = (KeyData)var1.next();
         var2.print(true);
      }

      System.out.println("active--------------------------------------------");
      var1 = this.cachedEntitiesByKey.values().iterator();

      while(var1.hasNext()) {
         var2 = (KeyData)var1.next();
         var2.print(false);
      }

      System.out.println("-------------------------------------------------");
   }

   public void updateIdleTimeoutSeconds(int var1) {
      this.scrubberTimer.resetScrubInterval((long)var1 * 1000L);
   }

   private static void debug(String var0) {
      debugLogger.debug("[EntityCache] " + var0);
   }

   static {
      debugLogger = EJBDebugService.cachingLogger;
   }

   static final class BeanData extends MRUElement {
      BeanData next;
      final EntityBean bean;
      Object txOrThread;
      long timestamp;
      int pinCount;

      BeanData(EntityBean var1, Object var2) {
         this.txOrThread = var2;
         this.bean = var1;
      }

      public boolean associatedWith(Object var1) {
         return var1 == this.txOrThread || var1.equals(this.txOrThread);
      }

      void enroll(Object var1) {
         this.txOrThread = var1;
         super.remove();
      }

      void release() {
         this.txOrThread = null;
         this.timestamp = System.currentTimeMillis();
         this.pinCount = 0;
      }

      int pin() {
         return this.pinCount++;
      }

      int unpin() {
         assert this.pinCount > 0 : " Error ! attempt to unpin bean with pinCount == " + this.pinCount + ", bean: " + this.bean;

         return this.pinCount--;
      }

      boolean isPinned() {
         return this.pinCount > 0;
      }
   }

   final class KeyData extends MRUElement {
      final CacheKey key;
      final CachingManager cachingManager;
      final boolean attemptCopy;
      final boolean categoryEnabled;
      private final Ring readyRing = new Ring();
      private BeanData activeBeanList;

      KeyData(CachingManager var2, CacheKey var3) {
         this.cachingManager = var2;
         this.key = var3;
         if (var2 instanceof TTLManager) {
            this.attemptCopy = ((TTLManager)var2).supportsCopy();
            this.categoryEnabled = ((TTLManager)var2).isCategoryEnabled();
         } else {
            this.attemptCopy = false;
            this.categoryEnabled = false;
         }

      }

      boolean isEmpty() {
         return this.activeBeanList == null && this.readyRing.isEmpty();
      }

      KeyData add(EntityBean var1, Object var2, boolean var3) {
         BeanData var4 = new BeanData(var1, var2);
         synchronized(this) {
            if (var3 && !EntityCache.this.disableReadyCache) {
               var4.pin();
            }

            boolean var6 = !this.isEmpty();
            var4.next = this.activeBeanList;
            this.activeBeanList = var4;
            return var6 ? null : (KeyData)EntityCache.this.cachedEntitiesByKey.putIfAbsent(this.key, this);
         }
      }

      void release(Object var1) {
         boolean var3 = false;
         BeanData var2;
         synchronized(this) {
            var2 = this.getAndUnlinkBeanForTxOrThread(var1);
            if (var2 == null) {
               return;
            }

            if (!this.cachingManager.needsRemoval(var2.bean)) {
               if (this.readyRing.isEmpty()) {
                  EntityCache.this.sizeTracker.insertKeyData(this);
               } else {
                  EntityCache.this.sizeTracker.moveKeyDataToHead(this);
               }

               var2.release();
               this.readyRing.insert(var2);
               var3 = true;
            }

            if (!var3) {
               EntityCache.this.sizeTracker.decrementSize(this.cachingManager.getBeanSize());
               if (this.isEmpty()) {
                  EntityCache.this.cachedEntitiesByKey.remove(this.key);
               }
            }
         }

         if (!var3) {
            this.cachingManager.selectedForReplacement(this.key, var2.bean);
         }

         if (this.categoryEnabled) {
            TTLManager var4 = (TTLManager)this.cachingManager;
            Object var5 = var4.getCategoryValue((CMPBean)var2.bean);
            long var6 = ((WLEntityBean)var2.bean).__WL_getLastLoadTime();
            var4.registerCategoryTimer(var5, var6);
         }

      }

      BeanData getBeanForTxOrThread(Object var1) {
         BeanData var2;
         for(var2 = this.activeBeanList; var2 != null && !var2.associatedWith(var1); var2 = var2.next) {
         }

         return var2;
      }

      BeanData getAndUnlinkBeanForTxOrThread(Object var1) {
         BeanData var2 = this.activeBeanList;

         BeanData var3;
         for(var3 = null; var2 != null && !var2.associatedWith(var1); var2 = var2.next) {
            var3 = var2;
         }

         if (var2 != null) {
            if (var2 == this.activeBeanList) {
               this.activeBeanList = var2.next;
            } else {
               var3.next = var2.next;
            }
         }

         return var2;
      }

      EntityBean getIfNotTimedOut(Object var1, boolean var2) throws InternalException {
         EntityBean var3 = null;
         BeanData var4 = null;
         synchronized(this) {
            label93: {
               var4 = this.getBeanForTxOrThread(var1);
               if (var4 != null) {
                  return var4.bean;
               }

               MRUElement var6 = this.readyRing.fwd;
               if (var6 == this.readyRing) {
                  return null;
               }

               while(true) {
                  BeanData var7;
                  if (var6 != this.readyRing) {
                     var7 = (BeanData)var6;
                     WLEntityBean var8 = (WLEntityBean)var7.bean;
                     if (!var8.__WL_isBeanStateValid()) {
                        var6 = var6.fwd;
                        continue;
                     }

                     var4 = var7;
                  } else {
                     if (!this.attemptCopy) {
                        break;
                     }

                     var7 = null;
                     long var17 = -1L;

                     for(BeanData var10 = this.activeBeanList; var10 != null; var10 = var10.next) {
                        long var11 = ((WLEntityBean)var10.bean).__WL_getLastLoadTime();
                        if (var11 > var17) {
                           var17 = var11;
                           var7 = var10;
                        }
                     }

                     if (var7 == null || !((WLEntityBean)var7.bean).__WL_isBeanStateValid()) {
                        break;
                     }

                     var4 = (BeanData)this.readyRing.fwd;

                     try {
                        ((TTLManager)this.cachingManager).doCopy((CMPBean)var7.bean, (CMPBean)var4.bean);
                     } catch (InternalException var14) {
                        var6.remove();
                        this.cleanupFailedBean((BeanData)var6);
                        throw var14;
                     }
                  }

                  var3 = this.enroll(var4, var1, var2);
                  break label93;
               }

               return null;
            }
         }

         try {
            Transaction var5 = null;
            if (var1 instanceof Transaction) {
               var5 = (Transaction)var1;
            }

            ((TTLManager)this.cachingManager).enrollNotTimedOutBean(var5, this.key, var3);
            return var3;
         } catch (InternalException var15) {
            this.cachingManager.removedOnError(this.key, var3);
            if (!EntityCache.this.disableReadyCache) {
               EntityCache.this.sizeTracker.decrementSize(this.cachingManager.getBeanSize());
            }

            if (this.isEmpty()) {
               EntityCache.this.cachedEntitiesByKey.remove(this.key);
            }

            throw var15;
         }
      }

      private void cleanupFailedBean(BeanData var1) {
         this.cachingManager.removedOnError(this.key, var1.bean);
         if (!EntityCache.this.disableReadyCache) {
            if (this.readyRing.isEmpty()) {
               EntityCache.this.sizeTracker.removeKeyData(this, this.cachingManager.getBeanSize());
            } else {
               EntityCache.this.sizeTracker.decrementSize(this.cachingManager.getBeanSize());
            }
         }

         if (this.isEmpty()) {
            EntityCache.this.cachedEntitiesByKey.remove(this.key);
         }

      }

      public synchronized CMPBean getLastLoadedValidInstance() {
         WLEntityBean var1 = null;
         long var2 = -1L;

         WLEntityBean var4;
         long var6;
         for(BeanData var5 = this.activeBeanList; var5 != null; var5 = var5.next) {
            var4 = (WLEntityBean)var5.bean;
            var6 = var4.__WL_getLastLoadTime();
            if (var6 > var2) {
               var2 = var6;
               var1 = var4;
            }
         }

         if (var1 != null && var1.__WL_isBeanStateValid()) {
            return (CMPBean)var1;
         } else {
            var1 = null;

            for(MRUElement var8 = this.readyRing.fwd; var8 != this.readyRing; var8 = var8.fwd) {
               var4 = (WLEntityBean)((BeanData)var8).bean;
               var6 = var4.__WL_getLastLoadTime();
               if (var6 > var2) {
                  var2 = var6;
                  var1 = var4;
               }
            }

            if (var1 != null && !var1.__WL_isBeanStateValid()) {
               var1 = null;
            }

            return (CMPBean)var1;
         }
      }

      void backToPool(Object var1) {
         BeanData var2;
         synchronized(this) {
            var2 = this.activeBeanList;
            if (var2 == null) {
               return;
            }

            if (var1 != var2.txOrThread && !var1.equals(var2.txOrThread)) {
               BeanData var4;
               do {
                  var4 = var2;
                  var2 = var2.next;
                  if (var2 == null) {
                     return;
                  }
               } while(var1 != var2.txOrThread && !var1.equals(var2.txOrThread));

               var4.next = var2.next;
            } else {
               this.activeBeanList = var2.next;
            }

            if (this.isEmpty()) {
               EntityCache.this.cachedEntitiesByKey.remove(this.key);
            }
         }

         this.cachingManager.passivateAndBacktoPool(this.key, var2.bean);
      }

      void remove(Object var1, boolean var2) {
         BeanData var3;
         synchronized(this) {
            var3 = this.activeBeanList;
            if (var3 == null) {
               return;
            }

            if (var1 != var3.txOrThread && !var1.equals(var3.txOrThread)) {
               BeanData var5;
               do {
                  var5 = var3;
                  var3 = var3.next;
                  if (var3 == null) {
                     return;
                  }
               } while(var1 != var3.txOrThread && !var1.equals(var3.txOrThread));

               var5.next = var3.next;
            } else {
               this.activeBeanList = var3.next;
            }

            if (this.isEmpty()) {
               EntityCache.this.cachedEntitiesByKey.remove(this.key);
            }

            EntityCache.this.sizeTracker.decrementSize(this.cachingManager.getBeanSize());
         }

         if (var2) {
            this.cachingManager.removedOnError(this.key, var3.bean);
         } else {
            this.cachingManager.removedFromCache(this.key, var3.bean);
         }

      }

      synchronized void invalidate(Object var1) {
         for(BeanData var2 = this.activeBeanList; var2 != null; var2 = var2.next) {
            if (var1 == null || var1 != var2.txOrThread && !var1.equals(var2.txOrThread)) {
               ((WLEntityBean)var2.bean).__WL_setBeanStateValid(false);
            }
         }

         for(MRUElement var3 = this.readyRing.fwd; var3 != this.readyRing; var3 = var3.fwd) {
            ((WLEntityBean)((BeanData)var3).bean).__WL_setBeanStateValid(false);
         }

      }

      EntityBean get(Object var1, RSInfo var2, boolean var3) throws InternalException {
         EntityBean var4 = this.getActive(var1, var3);
         if (var4 != null) {
            if (var2 != null) {
               this.cachingManager.loadBeanFromRS(this.key, var4, var2);
            }

            return var4;
         } else if (EntityCache.this.disableReadyCache) {
            return null;
         } else {
            synchronized(this) {
               MRUElement var6 = this.readyRing.fwd;
               if (var6 == this.readyRing) {
                  return null;
               }

               BeanData var7;
               label80: {
                  BeanData var8;
                  for(var7 = null; var6 != this.readyRing; var6 = var6.fwd) {
                     var8 = (BeanData)var6;
                     WLEntityBean var9 = (WLEntityBean)var8.bean;
                     if (var9.__WL_isBeanStateValid()) {
                        var7 = var8;
                        break label80;
                     }
                  }

                  var7 = (BeanData)this.readyRing.fwd;
                  if (this.attemptCopy) {
                     var8 = null;
                     long var17 = -1L;

                     for(BeanData var11 = this.activeBeanList; var11 != null; var11 = var11.next) {
                        long var12 = ((WLEntityBean)var11.bean).__WL_getLastLoadTime();
                        if (var12 > var17) {
                           var17 = var12;
                           var8 = var11;
                        }
                     }

                     if (var8 != null && ((WLEntityBean)var8.bean).__WL_isBeanStateValid()) {
                        try {
                           ((TTLManager)this.cachingManager).doCopy((CMPBean)var8.bean, (CMPBean)var7.bean);
                        } catch (InternalException var15) {
                           var6.remove();
                           this.cleanupFailedBean((BeanData)var6);
                           throw var15;
                        }
                     }
                  }
               }

               var4 = this.enroll(var7, var1, var3);
            }

            Transaction var5 = null;
            if (var1 instanceof Transaction) {
               var5 = (Transaction)var1;
               this.cachingManager.enrollInTransaction(var5, this.key, var4, var2);
               return var4;
            } else {
               return var4;
            }
         }
      }

      EntityBean getValid(Object var1, boolean var2) throws InternalException {
         EntityBean var3 = this.getActive(var1, var2);
         if (var3 != null) {
            return var3;
         } else if (EntityCache.this.disableReadyCache) {
            return null;
         } else {
            synchronized(this) {
               MRUElement var5 = this.readyRing.fwd;
               if (var5 == this.readyRing) {
                  return null;
               }

               while(var5 != this.readyRing && !((WLEntityBean)((BeanData)var5).bean).__WL_isBeanStateValid()) {
                  var5 = var5.fwd;
               }

               if (var5 == this.readyRing) {
                  return null;
               }

               var3 = this.enroll((BeanData)var5, var1, var2);
            }

            Transaction var4 = null;
            if (var1 instanceof Transaction) {
               var4 = (Transaction)var1;
               this.cachingManager.enrollInTransaction(var4, this.key, var3, (RSInfo)null);
               return var3;
            } else {
               return var3;
            }
         }
      }

      private final EntityBean enroll(BeanData var1, Object var2, boolean var3) {
         var1.next = this.activeBeanList;
         this.activeBeanList = var1;
         var1.enroll(var2);
         if (var3 && !EntityCache.this.disableReadyCache) {
            var1.pin();
         }

         if (this.readyRing.fwd == this.readyRing) {
            EntityCache.this.sizeTracker.removeKeyData(this);
         }

         return var1.bean;
      }

      synchronized EntityBean getActive(Object var1, boolean var2) {
         if (var1 == null) {
            return null;
         } else {
            BeanData var3 = this.getBeanForTxOrThread(var1);
            if (var3 != null) {
               if (var2 && !EntityCache.this.disableReadyCache) {
                  var3.pin();
               }

               return var3.bean;
            } else {
               return null;
            }
         }
      }

      boolean contains(Object var1) {
         return this.getBeanForTxOrThread(var1) != null;
      }

      synchronized void unpin(Object var1) {
         if (!EntityCache.this.disableReadyCache) {
            BeanData var2 = this.getBeanForTxOrThread(var1);
            if (var2 != null) {
               var2.unpin();
            }

         }
      }

      int passivateUnModifiedBean(Transaction var1) {
         return this.passivateBean(var1, false, false);
      }

      int passivateModifiedBean(Transaction var1, boolean var2) {
         return this.passivateBean(var1, true, var2);
      }

      private int passivateBean(Transaction var1, boolean var2, boolean var3) {
         BeanData var4 = null;
         synchronized(this) {
            var4 = this.getBeanForTxOrThread(var1);
            if (var4 == null) {
               if (EntityCache.debugLogger.isDebugEnabled()) {
                  EntityCache.debug(EntityCache.this.cacheName + " passivateBean: bean pk " + this.key + ", is not in cache.  Cannot passivate.");
               }

               return 0;
            }

            if (var4.isPinned()) {
               if (EntityCache.debugLogger.isDebugEnabled()) {
                  EntityCache.debug(EntityCache.this.cacheName + " passivateBean: bean pk " + this.key + ", is pinned.  Cannot passivate.");
               }

               return 0;
            }

            boolean var6 = false;
            if (var2) {
               var6 = this.cachingManager.passivateLockedModifiedBean(var1, this.key.getPrimaryKey(), var3, var4.bean);
            } else {
               var6 = this.cachingManager.passivateLockedUnModifiedBean(var1, this.key.getPrimaryKey(), var4.bean);
            }

            if (!var6) {
               if (EntityCache.debugLogger.isDebugEnabled()) {
                  EntityCache.debug(EntityCache.this.cacheName + " passivateBean: bean pk " + this.key + ", is not passivatible.");
               }

               return 0;
            }

            this.getAndUnlinkBeanForTxOrThread(var1);
            if (this.isEmpty()) {
               EntityCache.this.cachedEntitiesByKey.remove(this.key);
            }

            if (!EntityCache.this.disableReadyCache) {
               EntityCache.this.sizeTracker.decrementSize(this.cachingManager.getBeanSize());
            }
         }

         this.cachingManager.passivateAndRelease(this.key, var4.bean);
         return this.cachingManager.getBeanSize();
      }

      synchronized void keepEnrolled(Iterator<KeyData> var1) {
         int var2 = this.cachingManager.getBeanSize();
         int var3 = 0;

         for(MRUElement var4 = this.readyRing.fwd; var4 != this.readyRing; var4 = var4.fwd) {
            this.cachingManager.selectedForReplacement(this.key, ((BeanData)var4).bean);
            var3 += var2;
         }

         EntityCache.this.sizeTracker.removeKeyData(this, var3);
         this.readyRing.reset();
         if (this.activeBeanList == null) {
            var1.remove();
         }

      }

      synchronized void print(boolean var1) {
         if (var1) {
            for(BeanData var3 = this.activeBeanList; var3 != null; var3 = var3.next) {
               System.out.println("key= " + this.key + ", bean= " + var3.bean);
            }

         } else {
            for(MRUElement var2 = this.readyRing.fwd; var2 != this.readyRing; var2 = var2.fwd) {
               System.out.println("key= " + this.key + ", bean= " + ((BeanData)var2).bean);
            }

         }
      }

      synchronized int validateDataStructures(CacheKey var1, SizeTracker var2) {
         Debug.assertion(this.key.equals(var1));
         int var3 = 0;

         for(BeanData var4 = this.activeBeanList; var4 != null; var4 = var4.next) {
            Debug.assertion(var4.txOrThread != null);
            ++var3;
         }

         for(MRUElement var5 = this.readyRing.fwd; var5 != this.readyRing; var5 = var5.fwd) {
            Debug.assertion(((BeanData)var5).txOrThread == null);
            ++var3;
         }

         Debug.assertion(this.readyRing.isEmpty() != var2.contains(this));
         return var3;
      }

      long oldestTimestamp() {
         MRUElement var1 = this.readyRing.bwd;
         return var1 == this.readyRing ? 0L : ((BeanData)var1).timestamp;
      }

      void shrink() {
         MRUElement var1;
         synchronized(this) {
            var1 = this.readyRing.bwd;
            if (var1 == this.readyRing) {
               return;
            }

            if (EntityCache.debugLogger.isDebugEnabled()) {
               EntityCache.debug(EntityCache.this.cacheName + " shrink:  removing " + this.key.getPrimaryKey() + " from active ring ");
            }

            var1.remove();
            if (this.readyRing.isEmpty()) {
               EntityCache.this.sizeTracker.removeKeyData(this, this.cachingManager.getBeanSize());
               if (this.activeBeanList == null) {
                  EntityCache.this.cachedEntitiesByKey.remove(this.key);
               }
            } else {
               EntityCache.this.sizeTracker.decrementSize(this.cachingManager.getBeanSize());
            }
         }

         this.cachingManager.selectedForReplacement(this.key, ((BeanData)var1).bean);
      }

      synchronized int shrinkIfExpired(long var1, Iterator<KeyData> var3) {
         int var5 = 0;
         MRUElement var4 = this.readyRing.bwd;

         for(int var6 = this.cachingManager.getBeanSize(); var4 != this.readyRing && ((BeanData)var4).timestamp < var1; var4 = this.readyRing.bwd) {
            var4.remove();
            ++var5;
            if (this.readyRing.isEmpty()) {
               if (!EntityCache.this.disableReadyCache) {
                  EntityCache.this.sizeTracker.removeKeyData(this, var6);
               }

               if (this.activeBeanList == null) {
                  var3.remove();
               }
            } else if (!EntityCache.this.disableReadyCache) {
               EntityCache.this.sizeTracker.decrementSize(var6);
            }

            this.cachingManager.selectedForReplacement(this.key, ((BeanData)var4).bean);
         }

         return var5;
      }
   }

   final class SizeTracker extends Ring {
      private long size = 0L;

      synchronized void decrementSize(int var1) {
         this.size -= (long)var1;
      }

      synchronized void insertKeyData(KeyData var1) {
         this.insert(var1);
      }

      synchronized void removeKeyData(KeyData var1) {
         var1.remove();
      }

      synchronized void removeKeyData(KeyData var1, int var2) {
         var1.remove();
         this.size -= (long)var2;
      }

      synchronized void moveKeyDataToHead(KeyData var1) {
         var1.remove();
         this.insert(var1);
      }

      synchronized long getSize() {
         return this.size;
      }

      protected KeyData acquireSpace(Transaction var1, long var2, int var4, int var5) throws CacheFullException {
         synchronized(this) {
            this.size += (long)var5;
            if (EntityCache.debugLogger.isDebugEnabled()) {
               EntityCache.debug(EntityCache.this.cacheName + " shrinkNext: increased cache size is now " + this.size + ", size limit is " + var2);
            }

            if (this.size <= var2) {
               return null;
            }

            MRUElement var7 = this.bwd;
            if (var7 != this) {
               var7.remove();
               this.insert(var7);
               return (KeyData)var7;
            }
         }

         synchronized(EntityCache.this.passivator) {
            if (this.size <= var2) {
               return null;
            } else {
               if (EntityCache.debugLogger.isDebugEnabled()) {
                  EntityCache.debug(EntityCache.this.cacheName + " cache full at size: " + this.size + ", begin passivation of beans in tx. ");
               }

               long var12 = EntityCache.this.passivator.passivate(var1, var2, var4);
               if (EntityCache.debugLogger.isDebugEnabled()) {
                  EntityCache.debug(EntityCache.this.cacheName + " after passivate, cache size is now " + this.size);
               }

               if (var12 >= (long)var4) {
                  return null;
               } else {
                  this.decrementSize(var4);
                  throw new CacheFullException("cache size after cleaning=" + this.size + ", max allowable cache size=" + var2 + ", extra free space required but not obtainable = " + var4);
               }
            }
         }
      }
   }

   static class Ring extends MRUElement {
      Ring() {
         this.reset();
      }

      final boolean isEmpty() {
         return this.fwd == this;
      }

      final void reset() {
         this.fwd = this.bwd = this;
      }

      final synchronized boolean contains(MRUElement var1) {
         for(MRUElement var2 = this.fwd; var2 != this; var2 = var2.fwd) {
            if (var2 == var1) {
               return true;
            }
         }

         return false;
      }

      final void insert(MRUElement var1) {
         var1.fwd = this.fwd;
         var1.bwd = this;
         this.fwd.bwd = var1;
         this.fwd = var1;
      }
   }

   static class MRUElement {
      MRUElement fwd;
      MRUElement bwd;

      final void remove() {
         this.bwd.fwd = this.fwd;
         this.fwd.bwd = this.bwd;
         this.fwd = this.bwd = null;
      }
   }
}
