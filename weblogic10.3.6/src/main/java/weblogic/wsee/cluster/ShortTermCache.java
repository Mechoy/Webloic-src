package weblogic.wsee.cluster;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ShortTermCache<K, V> {
   private long _maxTime;
   private Map<K, ShortTermCache<K, V>.Envelope<K, V>> _cache;

   public ShortTermCache(long var1) {
      this._maxTime = var1;
      this._cache = new HashMap();
   }

   public synchronized V put(K var1, V var2) {
      Envelope var3 = new Envelope(var1, var2);
      Envelope var4 = (Envelope)this._cache.put(var1, var3);
      return var4 != null ? var4.getValue() : null;
   }

   public synchronized void remove(K var1) {
      this._cache.remove(var1);
   }

   public synchronized V get(K var1) {
      Envelope var2 = (Envelope)this._cache.get(var1);
      boolean var3 = var2 != null && var2.isExpired();
      if (var2 != null && !var3) {
         return var2.getValue();
      } else {
         if (var3) {
            this.purgeCache();
         }

         return null;
      }
   }

   private void purgeCache() {
      Iterator var1 = this._cache.keySet().iterator();

      while(var1.hasNext()) {
         Object var2 = var1.next();
         Envelope var3 = (Envelope)this._cache.get(var2);
         if (var3.isExpired()) {
            var1.remove();
         }
      }

   }

   protected class Envelope<K, V> {
      private K _key;
      private V _value;
      public long _cacheTime;

      public Envelope(K var2, V var3) {
         this._key = var2;
         this._value = var3;
         this._cacheTime = System.currentTimeMillis();
      }

      public K getKey() {
         return this._key;
      }

      public V getValue() {
         return this._value;
      }

      public long getCacheTime() {
         return this._cacheTime;
      }

      public boolean isExpired() {
         return System.currentTimeMillis() - this._cacheTime > ShortTermCache.this._maxTime;
      }
   }
}
