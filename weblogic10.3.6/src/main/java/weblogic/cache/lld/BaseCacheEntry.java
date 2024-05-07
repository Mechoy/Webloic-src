package weblogic.cache.lld;

public class BaseCacheEntry<K, V> implements CacheEntry<K, V> {
   protected final K key;
   protected V value;
   protected final long createTime;
   protected long lastAccessTime;
   protected long lastUpdateTime;
   protected long ttl;
   protected boolean discarded;

   public BaseCacheEntry(K var1, V var2) {
      this(var1, var2, -1L);
   }

   public BaseCacheEntry(K var1, V var2, long var3) {
      this.key = var1;
      this.value = var2;
      this.createTime = this.lastUpdateTime = this.lastAccessTime = System.currentTimeMillis();
      this.ttl = var3;
   }

   public K getKey() {
      return this.key;
   }

   public V getValue() {
      this.lastAccessTime = System.currentTimeMillis();
      return this.value;
   }

   public V setValue(V var1) {
      this.lastUpdateTime = this.lastAccessTime = System.currentTimeMillis();
      Object var2 = this.value;
      this.value = var1;
      return var2;
   }

   public long expiration() {
      return this.lastUpdateTime + this.ttl * 1000L;
   }

   public boolean expired() {
      if (this.ttl < 0L) {
         return false;
      } else {
         return System.currentTimeMillis() > this.expiration();
      }
   }

   public long getCreationTime() {
      return this.createTime;
   }

   public long getLastAccessTime() {
      return this.lastAccessTime;
   }

   public long getLastUpdateTime() {
      return this.lastUpdateTime;
   }

   public boolean isDiscarded() {
      return this.discarded;
   }

   public void touch() {
      this.lastAccessTime = System.currentTimeMillis();
   }

   public void discard() {
      this.discarded = true;
   }

   public boolean equals(Object var1) {
      if (var1 != null && var1 instanceof BaseCacheEntry) {
         boolean var10000;
         label38: {
            label27: {
               BaseCacheEntry var2 = (BaseCacheEntry)var1;
               if (this.key == null) {
                  if (var2.getKey() != null) {
                     break label27;
                  }
               } else if (!this.key.equals(var2.getKey())) {
                  break label27;
               }

               if (this.value == null) {
                  if (var2.getValue() == null) {
                     break label38;
                  }
               } else if (this.value.equals(var2.getValue())) {
                  break label38;
               }
            }

            var10000 = false;
            return var10000;
         }

         var10000 = true;
         return var10000;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
   }

   public String toString() {
      return super.toString() + "|key:" + this.key.toString() + "|value:" + this.value.toString();
   }
}
