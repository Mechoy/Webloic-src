package weblogic.cache.lld;

import java.io.Serializable;
import java.util.Map;

public interface CacheEntry<K, V> extends Map.Entry<K, V>, Serializable {
   void touch();

   void discard();

   long expiration();

   long getCreationTime();

   long getLastAccessTime();

   long getLastUpdateTime();

   boolean isDiscarded();
}
