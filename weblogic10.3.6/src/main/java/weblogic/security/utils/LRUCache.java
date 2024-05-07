package weblogic.security.utils;

import java.util.Iterator;
import java.util.Map;

public class LRUCache implements Cache {
   private int mMaxSize = 1024;
   private OrderedHashMap mCache;

   public LRUCache() {
      this.mCache = new OrderedHashMap(this.mMaxSize);
   }

   public LRUCache(int var1) {
      this.mCache = new OrderedHashMap(this.mMaxSize);
      this.setMaximumSize(var1);
   }

   public int getMaximumSize() {
      return this.mMaxSize;
   }

   public void setMaximumSize(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("Maximum size cannot be negative");
      } else {
         if (var1 == 0) {
            this.clear();
         } else {
            while(this.size() > var1) {
               this.remove();
            }
         }

         this.mMaxSize = var1;
      }
   }

   public int size() {
      return this.mCache.size();
   }

   public Object put(Object var1, Object var2) {
      if (this.mCache.size() >= this.mMaxSize) {
         if (this.mMaxSize == 0) {
            return null;
         }

         this.mCache.removeFirst();
      }

      return this.mCache.putLast(var1, var2);
   }

   public Object get(Object var1) {
      return this.mCache.moveToLast(var1);
   }

   public Object lookup(Object var1) {
      return this.mCache.get(var1);
   }

   public void putOff(Object var1) {
      this.mCache.moveToFirst(var1);
   }

   public boolean containsKey(Object var1) {
      return this.mCache.containsKey(var1);
   }

   public boolean containsValue(Object var1) {
      return this.mCache.containsValue(var1);
   }

   public Map.Entry remove() {
      return this.mCache.removeFirst();
   }

   public Object remove(Object var1) {
      return this.mCache.remove(var1);
   }

   public void clear() {
      this.mCache.clear();
   }

   public Iterator iterator() {
      return this.mCache.iterator();
   }
}
