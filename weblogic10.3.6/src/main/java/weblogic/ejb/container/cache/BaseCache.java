package weblogic.ejb.container.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.ejb.EnterpriseBean;
import javax.ejb.SessionBean;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.container.EJBDebugService;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.interfaces.CachingManager;
import weblogic.ejb.container.interfaces.SingleInstanceCache;
import weblogic.ejb.spi.ScrubbedCache;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;

public abstract class BaseCache implements SingleInstanceCache, ScrubbedCache, TimerListener {
   protected static final DebugLogger debugLogger;
   protected static boolean dumpCache;
   private boolean usesMaxBeansInCache = false;
   private long maxCacheSize = 0L;
   protected long currentCacheSize = 0L;
   protected HashMap cache;
   protected String cacheName = null;
   protected long scrubIntervalMillisDD;
   protected long scrubIntervalMillis;
   protected int scrubCount = 0;
   protected CacheScrubberTimer scrubberTimer;

   public BaseCache(String var1, long var2) {
      this.maxCacheSize = var2;
      this.usesMaxBeansInCache = false;
      this.setup(var1);
   }

   public BaseCache(String var1, int var2) {
      this.maxCacheSize = (long)var2;
      this.usesMaxBeansInCache = true;
      this.setup(var1);
   }

   private void setup(String var1) {
      assert var1 != null;

      this.cacheName = var1;
      this.cache = new HashMap();
      this.scrubberTimer = new CacheScrubberTimer(this, 0L, var1);
   }

   public int getMaxBeansInCache() {
      return (int)this.maxCacheSize;
   }

   public void setMaxBeansInCache(int var1) {
      this.maxCacheSize = (long)var1;
   }

   public boolean usesMaxBeansInCache() {
      return this.usesMaxBeansInCache;
   }

   public long getMaxCacheSize() {
      return this.maxCacheSize;
   }

   public void setMaxCacheSize(long var1) {
      this.maxCacheSize = var1;
   }

   protected String getCacheUnits() {
      return this.usesMaxBeansInCache() ? "beans" : "bytes";
   }

   public synchronized long getCurrentSize() {
      return this.currentCacheSize;
   }

   public synchronized boolean contains(CacheKey var1) {
      assert this.validateDataStructures();

      return this.cache.get(var1) != null;
   }

   public synchronized void release(CacheKey var1) {
      assert this.validateDataStructures();

      Node var2 = (Node)this.cache.get(var1);
      if (var2 != null) {
         assert var2.isActive();

         assert var2.pinned();

         var2.unpin();
         EnterpriseBean var3 = var2.getBean();
         if (var2.getCallback().needsRemoval(var3)) {
            this.remove(var1);
         }

         this.releaseEntityBean(var2);

         assert this.validateDataStructures();

      }
   }

   public synchronized void clear() {
      Iterator var1 = this.cache.keySet().iterator();

      while(var1.hasNext()) {
         CacheKey var2 = (CacheKey)var1.next();
         EnterpriseBean var3 = this.get(var2);

         try {
            if (var3 instanceof SessionBean) {
               SessionBean var4 = (SessionBean)var3;
               var4.ejbRemove();
            }
         } catch (Throwable var5) {
            EJBLogger.logExceptionDuringEJBRemove(var5);
         }
      }

      this.cache.clear();
      this.currentCacheSize = 0L;
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
      this.scrubCount = 0;
      this.scrubberTimer.startScrubber();
   }

   public void stopScrubber() {
      this.scrubberTimer.stopScrubber();
   }

   public void timerExpired(Timer var1) {
      this.cacheScrubber();
   }

   public void updateIdleTimeoutSeconds(int var1) {
      this.scrubberTimer.resetScrubInterval((long)var1 * 1000L);
   }

   abstract void cacheScrubber();

   abstract void releaseEntityBean(Node var1);

   protected void passivateNode(Node var1) {
      if (debugLogger.isDebugEnabled()) {
         debug("passivateNode key: " + var1.getKey());
      }

      assert !var1.pinned();

      assert var1.getBean() != null;

      assert var1.getKey() != null;

      assert var1.getCallback() != null;

      CacheKey var2 = var1.getKey();
      EnterpriseBean var3 = var1.getBean();
      var1.getCallback().swapOut(var2, var3);
   }

   protected String cacheDump() {
      StringBuffer var1 = new StringBuffer();
      if (!this.cache.isEmpty()) {
         var1.append("Dumping EJBCache for: ");
         var1.append(this.cacheName);
         var1.append(" [ Cache size: ");
         var1.append(this.cache.size());
         var1.append(" ]");
         Set var2 = this.cache.keySet();

         for(Iterator var3 = var2.iterator(); var3.hasNext(); var1.append(" ] ")) {
            CacheKey var4 = (CacheKey)var3.next();
            var1.append(" [ Key: ");
            var1.append(var4);
            Node var5 = (Node)this.cache.get(var4);
            if (var5.pinned()) {
               var1.append(" Locked by: ");
               var1.append("owner");
            }
         }
      } else {
         var1.append("Cache is empty for: " + this.cacheName + "\n");
      }

      return var1.toString();
   }

   protected abstract boolean validateDataStructures();

   private static void debug(String var0) {
      debugLogger.debug("[BaseCache] " + var0);
   }

   static {
      debugLogger = EJBDebugService.cachingLogger;
      dumpCache = System.getProperty("ejb.enableCacheDump") != null;
   }

   protected static class Node {
      private EnterpriseBean bean = null;
      private CacheKey key = null;
      private CachingManager callback = null;
      private int size;
      private final int FREE = 0;
      private final int INACTIVE = 1;
      private final int ACTIVE = 2;
      private int state = 0;
      private long lastTouchedAt;
      private boolean pinned = false;
      Node prev = null;
      Node next = null;

      Node() {
         this.touch();
      }

      void touch() {
         this.lastTouchedAt = System.currentTimeMillis();
      }

      long timeSinceLastTouch() {
         return Math.abs(System.currentTimeMillis() - this.lastTouchedAt);
      }

      boolean idleLongerThan(long var1) {
         long var3 = this.timeSinceLastTouch();
         return var3 > var1;
      }

      void pin() {
         if (BaseCache.debugLogger.isDebugEnabled()) {
            BaseCache.debug("PINNING key: " + this.key);
         }

         this.pinned = true;
      }

      void unpin() {
         if (BaseCache.debugLogger.isDebugEnabled()) {
            BaseCache.debug("UNPINNING key: " + this.key);
         }

         assert this.pinned;

         this.pinned = false;
      }

      boolean pinned() {
         return this.pinned;
      }

      EnterpriseBean getBean() {
         return this.bean;
      }

      void setBean(EnterpriseBean var1) {
         this.bean = var1;
      }

      CacheKey getKey() {
         return this.key;
      }

      void setKey(CacheKey var1) {
         this.key = var1;
         if (this.key != null) {
            this.callback = var1.getCallback();
            this.size = this.callback.getBeanSize();
         } else {
            this.callback = null;
         }

      }

      CachingManager getCallback() {
         return this.callback;
      }

      int getSize() {
         return this.size;
      }

      boolean olderThan(long var1) {
         return this.lastTouchedAt < var1;
      }

      void setActive() {
         this.state = 2;
      }

      void setInActive() {
         this.state = 1;
      }

      void setFree() {
         this.state = 0;
      }

      boolean isActive() {
         return this.state == 2;
      }

      boolean isFree() {
         return this.state == 0;
      }

      boolean isInActive() {
         return this.state == 1;
      }
   }
}
