package weblogic.security.utils;

import java.util.Iterator;
import java.util.Map;

public class TTLLRUCache extends LRUCache {
   private long timeToLive = 0L;

   public TTLLRUCache() {
   }

   public TTLLRUCache(int var1) {
      super(var1);
   }

   public TTLLRUCache(int var1, int var2) {
      super(var1);
      this.setTimeToLive(var2);
   }

   public void setTimeToLive(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("Seconds to live cannot be negative");
      } else {
         this.timeToLive = (long)(var1 * 1000);
      }
   }

   public Object put(Object var1, Object var2) {
      TTLEntry var3 = (TTLEntry)super.put(var1, new TTLEntry(var2));
      return var3 == null ? null : var3.value;
   }

   public Object get(Object var1) {
      return this.extractValue(var1, (TTLEntry)super.get(var1));
   }

   public Object lookup(Object var1) {
      return this.extractValue(var1, (TTLEntry)super.lookup(var1));
   }

   private Object extractValue(Object var1, TTLEntry var2) {
      Object var3 = null;
      if (var2 != null) {
         if (var2.isValid()) {
            var3 = var2.value;
         } else {
            super.remove(var1);
         }
      }

      return var3;
   }

   public boolean containsKey(Object var1) {
      return this.lookup(var1) != null;
   }

   public boolean containsValue(Object var1) {
      return super.containsValue(new TTLEntry(var1));
   }

   public Map.Entry remove() {
      Map.Entry var1 = super.remove();
      if (var1 != null) {
         var1.setValue(((TTLEntry)var1.getValue()).value);
      }

      return var1;
   }

   public Object remove(Object var1) {
      TTLEntry var2 = (TTLEntry)super.remove(var1);
      return var2 == null ? null : var2.value;
   }

   public Iterator iterator() {
      return new TTLIterator();
   }

   private class Entry implements Map.Entry {
      Object key;
      Object value;

      Entry(Object var2, Object var3) {
         this.key = var2;
         this.value = var3;
      }

      public Object getValue() {
         return this.value;
      }

      public Object setValue(Object var1) {
         throw new UnsupportedOperationException();
      }

      public Object getKey() {
         return this.key;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (!(var1 instanceof Map.Entry)) {
            return false;
         } else {
            boolean var10000;
            label43: {
               label29: {
                  Map.Entry var2 = (Map.Entry)var1;
                  if (this.key == null) {
                     if (var2.getKey() != null) {
                        break label29;
                     }
                  } else if (!this.key.equals(var2.getKey())) {
                     break label29;
                  }

                  if (this.value == null) {
                     if (var2.getValue() == null) {
                        break label43;
                     }
                  } else if (this.value.equals(var2.getValue())) {
                     break label43;
                  }
               }

               var10000 = false;
               return var10000;
            }

            var10000 = true;
            return var10000;
         }
      }

      public String toString() {
         return this.key + "=" + this.value;
      }
   }

   private class TTLIterator implements Iterator {
      Iterator iterator = TTLLRUCache.this.iterator();

      public TTLIterator() {
      }

      public Object next() {
         Object var1 = (Map.Entry)this.iterator.next();
         if (var1 != null) {
            var1 = TTLLRUCache.this.new Entry(((Map.Entry)var1).getKey(), ((TTLEntry)((Map.Entry)var1).getValue()).value);
         }

         return var1;
      }

      public boolean hasNext() {
         return this.iterator.hasNext();
      }

      public void remove() {
         this.iterator.remove();
      }
   }

   private class TTLEntry {
      public Object value;
      public long timeStamp;

      public TTLEntry(Object var2) {
         this.value = var2;
         this.timeStamp = System.currentTimeMillis();
      }

      public boolean isValid() {
         return TTLLRUCache.this.timeToLive == 0L || System.currentTimeMillis() - this.timeStamp < TTLLRUCache.this.timeToLive;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (!(var1 instanceof TTLEntry)) {
            return false;
         } else {
            TTLEntry var2 = (TTLEntry)var1;
            return this.value == null ? var2.value == null : this.value.equals(var2);
         }
      }

      public int hashCode() {
         return this.value != null ? this.value.hashCode() : 0;
      }
   }
}
