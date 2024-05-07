package weblogic.entitlement.util;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class OrderedHashMap extends HashMap {
   private Entry mFirst;
   private Entry mLast;
   private transient int modCount;
   private transient Set keySet;
   private transient Set entrySet;
   private transient Collection values;

   public OrderedHashMap() {
      this.modCount = 0;
      this.keySet = null;
      this.entrySet = null;
      this.values = null;
   }

   public OrderedHashMap(int var1) {
      super(var1);
      this.modCount = 0;
      this.keySet = null;
      this.entrySet = null;
      this.values = null;
   }

   public OrderedHashMap(int var1, float var2) {
      super(var1, var2);
      this.modCount = 0;
      this.keySet = null;
      this.entrySet = null;
      this.values = null;
   }

   public OrderedHashMap(Map var1) {
      this(Math.max(2 * var1.size(), 11));
      this.putAll(var1);
   }

   public OrderedHashMap(OrderedHashMap var1) {
      this(Math.max(2 * var1.size(), 11));
      this.putAll(var1);
   }

   public boolean containsValue(Object var1) {
      Entry var2;
      if (var1 == null) {
         for(var2 = this.mFirst; var2 != null; var2 = var2.next) {
            if (var2.value == null) {
               return true;
            }
         }
      } else {
         for(var2 = this.mFirst; var2 != null; var2 = var2.next) {
            if (var1.equals(var2.value)) {
               return true;
            }
         }
      }

      return false;
   }

   public Object get(Object var1) {
      Entry var2 = (Entry)super.get(var1);
      return var2 == null ? null : var2.value;
   }

   public Map.Entry getFirst() {
      return this.mFirst;
   }

   public Map.Entry getLast() {
      return this.mLast;
   }

   public Object put(Object var1, Object var2) {
      return this.put(var1, var2, false, false);
   }

   public Object putFirst(Object var1, Object var2) {
      return this.put(var1, var2, true, true);
   }

   public Object putLast(Object var1, Object var2) {
      return this.put(var1, var2, false, true);
   }

   public Object put(Object var1, Object var2, boolean var3, boolean var4) {
      Object var5 = null;
      Entry var6 = (Entry)super.get(var1);
      if (var6 == null) {
         ++this.modCount;
         var6 = new Entry(var1, var2);
         super.put(var1, var6);
         if (var3) {
            this.addFirstListEntry(var6);
         } else {
            this.addLastListEntry(var6);
         }
      } else {
         var5 = var6.value;
         var6.value = var2;
         if (var4) {
            if (var3) {
               this.moveListEntryToFirst(var6);
            } else {
               this.moveListEntryToLast(var6);
            }
         }
      }

      return var5;
   }

   public void putAll(Map var1) {
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         this.put(var3.getKey(), var3.getValue(), false, false);
      }

   }

   public Object moveToFirst(Object var1) {
      Entry var2 = (Entry)super.get(var1);
      if (var2 != null && var2 != this.mFirst) {
         ++this.modCount;
         this.moveListEntryToFirst(var2);
      }

      return var2 == null ? null : var2.value;
   }

   public Object moveToLast(Object var1) {
      Entry var2 = (Entry)super.get(var1);
      if (var2 != null && var2 != this.mLast) {
         ++this.modCount;
         this.moveListEntryToLast(var2);
      }

      return var2 == null ? null : var2.value;
   }

   public Object remove(Object var1) {
      if (super.containsKey(var1)) {
         ++this.modCount;
      }

      Object var2 = null;
      Entry var3 = (Entry)super.remove(var1);
      if (var3 != null) {
         var2 = var3.value;
         this.removeListEntry(var3);
      }

      return var2;
   }

   public Map.Entry removeFirst() {
      if (this.mFirst != null) {
         ++this.modCount;
         super.remove(this.mFirst.key);
         this.removeListEntry(this.mFirst);
      }

      return this.mFirst;
   }

   public Map.Entry removeLast() {
      if (this.mLast != null) {
         ++this.modCount;
         super.remove(this.mLast.key);
         this.removeListEntry(this.mLast);
      }

      return this.mLast;
   }

   public void clear() {
      ++this.modCount;
      super.clear();
      this.mFirst = this.mLast = null;
   }

   public Object clone() {
      return new OrderedHashMap(this);
   }

   public Set keySet() {
      if (this.keySet == null) {
         this.keySet = new AbstractSet() {
            public Iterator iterator() {
               return (Iterator)(OrderedHashMap.this.mFirst == null ? EmptyIterator.INSTANCE : OrderedHashMap.this.new KeyIterator(OrderedHashMap.this.mFirst));
            }

            public int size() {
               return OrderedHashMap.this.size();
            }

            public boolean contains(Object var1) {
               return OrderedHashMap.this.containsKey(var1);
            }

            public boolean remove(Object var1) {
               int var2 = OrderedHashMap.this.size();
               OrderedHashMap.this.remove(var1);
               return OrderedHashMap.this.size() != var2;
            }

            public void clear() {
               OrderedHashMap.this.clear();
            }
         };
      }

      return this.keySet;
   }

   public Collection values() {
      if (this.values == null) {
         this.values = new AbstractCollection() {
            public Iterator iterator() {
               return (Iterator)(OrderedHashMap.this.mFirst == null ? EmptyIterator.INSTANCE : OrderedHashMap.this.new ValueIterator(OrderedHashMap.this.mFirst));
            }

            public int size() {
               return OrderedHashMap.this.size();
            }

            public boolean contains(Object var1) {
               return OrderedHashMap.this.containsValue(var1);
            }

            public void clear() {
               OrderedHashMap.this.clear();
            }
         };
      }

      return this.values;
   }

   public Set entrySet() {
      if (this.entrySet == null) {
         this.entrySet = new AbstractSet() {
            public Iterator iterator() {
               return (Iterator)(OrderedHashMap.this.mFirst == null ? EmptyIterator.INSTANCE : OrderedHashMap.this.new EntryIterator(OrderedHashMap.this.mFirst));
            }

            public boolean contains(Object var1) {
               return !(var1 instanceof Map.Entry) ? false : OrderedHashMap.this.containsKey(((Map.Entry)var1).getKey());
            }

            public boolean remove(Object var1) {
               if (!(var1 instanceof Map.Entry)) {
                  return false;
               } else {
                  int var2 = OrderedHashMap.this.size();
                  OrderedHashMap.this.remove(((Map.Entry)var1).getKey());
                  return var2 != OrderedHashMap.this.size();
               }
            }

            public int size() {
               return OrderedHashMap.this.size();
            }

            public void clear() {
               OrderedHashMap.this.clear();
            }
         };
      }

      return this.entrySet;
   }

   public Iterator iterator() {
      return (Iterator)(this.mFirst == null ? EmptyIterator.INSTANCE : new EntryIterator(this.mFirst));
   }

   private void moveListEntryToLast(Entry var1) {
      if (var1 != this.mLast) {
         this.removeListEntry(var1);
         this.addLastListEntry(var1);
      }

   }

   private void moveListEntryToFirst(Entry var1) {
      if (var1 != this.mFirst) {
         this.removeListEntry(var1);
         this.addFirstListEntry(var1);
      }

   }

   private void addLastListEntry(Entry var1) {
      if (this.mLast == null) {
         this.mFirst = this.mLast = var1;
      } else {
         this.mLast.next = var1;
         var1.prev = this.mLast;
         this.mLast = var1;
      }

   }

   private void addFirstListEntry(Entry var1) {
      if (this.mFirst == null) {
         this.mFirst = this.mLast = var1;
      } else {
         this.mFirst.prev = var1;
         var1.next = this.mFirst;
         this.mFirst = var1;
      }

   }

   private void removeListEntry(Entry var1) {
      Entry var2 = var1.prev;
      Entry var3 = var1.next;
      if (var2 == null) {
         this.mFirst = var3;
      } else {
         var2.next = var3;
      }

      if (var3 == null) {
         this.mLast = var2;
      } else {
         var3.prev = var2;
      }

      var1.next = var1.prev = null;
   }

   private class Entry implements Map.Entry {
      Entry next;
      Entry prev;
      Object key;
      Object value;

      Entry(Object var2, Object var3) {
         this.key = var2;
         this.value = var3;
         this.next = this.prev = null;
      }

      public Object getValue() {
         return this.value;
      }

      public Object setValue(Object var1) {
         Object var2 = this.value;
         this.value = var1;
         return var2;
      }

      public Object getKey() {
         return this.key;
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof Map.Entry)) {
            return false;
         } else {
            boolean var10000;
            label38: {
               label27: {
                  Map.Entry var2 = (Map.Entry)var1;
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
         }
      }

      public int hashCode() {
         int var1 = 0;
         if (this.key != null) {
            var1 ^= this.key.hashCode();
         }

         if (this.value != null) {
            var1 ^= this.value.hashCode();
         }

         return var1;
      }

      public String toString() {
         return this.key + "=" + this.value;
      }
   }

   private class ValueIterator extends EntryIterator {
      public ValueIterator(Entry var2) {
         super(var2);
      }

      public Object next() {
         Entry var1 = (Entry)super.next();
         return var1.value;
      }
   }

   private class KeyIterator extends EntryIterator {
      public KeyIterator(Entry var2) {
         super(var2);
      }

      public Object next() {
         Entry var1 = (Entry)super.next();
         return var1.key;
      }
   }

   private class EntryIterator implements Iterator {
      private Entry entry = null;
      private Entry lastReturned = null;
      private int expectedModCount;

      public EntryIterator(Entry var2) {
         this.expectedModCount = OrderedHashMap.this.modCount;
         this.entry = var2;
      }

      public boolean hasNext() {
         return this.entry != null;
      }

      public Object next() {
         if (OrderedHashMap.this.modCount != this.expectedModCount) {
            throw new ConcurrentModificationException();
         } else if (this.entry == null) {
            throw new NoSuchElementException();
         } else {
            this.lastReturned = this.entry;
            this.entry = this.entry.next;
            return this.lastReturned;
         }
      }

      public void remove() {
         if (this.lastReturned == null) {
            throw new IllegalStateException();
         } else if (OrderedHashMap.this.modCount != this.expectedModCount) {
            throw new ConcurrentModificationException();
         } else {
            ++this.expectedModCount;
            OrderedHashMap.this.remove(this.lastReturned.key);
            if (OrderedHashMap.this.modCount != this.expectedModCount) {
               throw new ConcurrentModificationException();
            }
         }
      }
   }
}
