package weblogic.ejb.container.monitoring;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import weblogic.ejb.spi.ReInitializableCache;
import weblogic.management.ManagementException;
import weblogic.management.runtime.EJBCacheRuntimeMBean;
import weblogic.management.runtime.EJBRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public final class EJBCacheRuntimeMBeanImpl extends RuntimeMBeanDelegate implements EJBCacheRuntimeMBean {
   private static final long serialVersionUID = -4664975769315082605L;
   private AtomicInteger cachedBeansCurrentCount = new AtomicInteger(0);
   private AtomicLong cacheAccessCount = new AtomicLong(0L);
   private AtomicLong cacheHitCount = new AtomicLong(0L);
   private AtomicLong activationCount = new AtomicLong(0L);
   private AtomicLong passivationCount = new AtomicLong(0L);
   private ReInitializableCache cache = null;

   public EJBCacheRuntimeMBeanImpl(String var1, EJBRuntimeMBean var2) throws ManagementException {
      super(var1, var2, true, "CacheRuntime");
   }

   public void setReInitializableCache(ReInitializableCache var1) {
      this.cache = var1;
   }

   public void reInitializeCacheAndPools() {
      this.cache.reInitializeCacheAndPools();
   }

   public int getCachedBeansCurrentCount() {
      return this.cachedBeansCurrentCount.get();
   }

   public void incrementCachedBeansCurrentCount() {
      this.cachedBeansCurrentCount.incrementAndGet();
   }

   public void decrementCachedBeansCurrentCount() {
      this.cachedBeansCurrentCount.decrementAndGet();
   }

   public long getCacheAccessCount() {
      return this.cacheAccessCount.get();
   }

   public void incrementCacheAccessCount() {
      this.cacheAccessCount.incrementAndGet();
   }

   public long getCacheHitCount() {
      return this.cacheHitCount.get();
   }

   public long getCacheMissCount() {
      return this.cacheAccessCount.get() - this.cacheHitCount.get();
   }

   public void incrementCacheHitCount() {
      this.cacheHitCount.incrementAndGet();
   }

   public long getActivationCount() {
      return this.activationCount.get();
   }

   public void incrementActivationCount() {
      this.activationCount.incrementAndGet();
   }

   public long getPassivationCount() {
      return this.passivationCount.get();
   }

   public void incrementPassivationCount() {
      this.passivationCount.incrementAndGet();
   }
}
