package weblogic.security.pki.revocation.common;

import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

final class CertRevocStatusCache {
   private static final int INITIAL_CAPACITY = 1024;
   private final CacheImpl cache = new CacheImpl();
   private final Object cacheSync = new Object();

   public static CertRevocStatusCache getInstance() {
      return new CertRevocStatusCache();
   }

   private CertRevocStatusCache() {
   }

   public CertRevocStatus getStatus(X509Certificate var1, int var2, int var3, LogListener var4) {
      Util.checkNotNull("X509Certificate", var1);
      Util.checkTimeTolerance(var2);
      Util.checkRefreshPeriodPercent(var3);
      Object var5 = CertRevocStatus.createKey(var1);
      synchronized(this.cacheSync) {
         CertRevocStatus var7 = (CertRevocStatus)this.cache.get(var5);
         if (null == var7) {
            return null;
         } else if (!var7.isValid(var2, var3, var4)) {
            this.cache.remove(var7.getKey());
            return null;
         } else {
            return var7;
         }
      }
   }

   public boolean putStatus(X509Certificate var1, CertRevocStatus var2, int var3, int var4, int var5, LogListener var6) {
      Util.checkNotNull("X509Certificate", var1);
      Util.checkTimeTolerance(var3);
      Util.checkRefreshPeriodPercent(var4);
      checkCapacity(var5);
      Object var7 = CertRevocStatus.createKey(var1);
      synchronized(this.cacheSync) {
         if (null == var2) {
            return null != this.cache.remove(var7);
         } else if (!var2.isValid(var3, var4, var6)) {
            return false;
         } else {
            this.adjustCapacity(var5);
            this.cache.put(var7, var2);
            return true;
         }
      }
   }

   public int getSize() {
      synchronized(this.cacheSync) {
         return this.cache.size();
      }
   }

   private static void checkCapacity(int var0) {
      Util.checkRange("capacity", (long)var0, 1L, (Long)null);
   }

   private void adjustCapacity(int var1) {
      synchronized(this.cacheSync) {
         this.cache.setMaxEntries(var1);
         int var3 = this.cache.size() - var1;
         if (var3 > 0) {
            for(Iterator var4 = this.cache.entrySet().iterator(); var3 > 0 && var4.hasNext(); --var3) {
               Map.Entry var5 = (Map.Entry)var4.next();
               var4.remove();
            }
         }

      }
   }

   private static class CacheImpl extends LinkedHashMap<Object, CertRevocStatus> {
      private static final float LOAD_FACTOR = 0.75F;
      private static final boolean ACCESS_ORDER = true;
      private volatile int maxEntries;

      private CacheImpl() {
         super(1024, 0.75F, true);
         this.setMaxEntries(1024);
      }

      protected boolean removeEldestEntry(Map.Entry<Object, CertRevocStatus> var1) {
         return this.size() > this.maxEntries;
      }

      public void setMaxEntries(int var1) {
         CertRevocStatusCache.checkCapacity(var1);
         this.maxEntries = var1;
      }

      // $FF: synthetic method
      CacheImpl(Object var1) {
         this();
      }
   }
}
