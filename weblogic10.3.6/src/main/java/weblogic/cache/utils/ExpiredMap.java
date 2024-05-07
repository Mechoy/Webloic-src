package weblogic.cache.utils;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.messaging.util.AbstractElement;
import weblogic.messaging.util.Element;
import weblogic.messaging.util.List;

public class ExpiredMap extends AbstractElement implements Map, Cloneable {
   private List list;
   private Map map;
   private int maxCapacity;
   private long ttl;

   public ExpiredMap(int var1, Map var2, long var3) {
      this.maxCapacity = var1;
      this.ttl = var3;
      this.map = var2;
      this.list = new List();
   }

   public synchronized int size() {
      return this.map.size();
   }

   public synchronized boolean isEmpty() {
      return this.map.isEmpty();
   }

   public synchronized boolean containsKey(Object var1) {
      return this.map.containsKey(var1);
   }

   public synchronized boolean equals(Object var1) {
      return this.map.equals(var1);
   }

   public synchronized int hashCode() {
      return this.map.hashCode();
   }

   public synchronized void putAll(Map var1) {
      Iterator var2 = var1.keySet().iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         this.put(var3, var1.get(var3));
      }

   }

   public synchronized Object clone() {
      ExpiredMap var1;
      try {
         var1 = (ExpiredMap)super.clone();
      } catch (CloneNotSupportedException var4) {
         throw new RuntimeException(var4.getMessage(), var4);
      }

      var1.list = new List();
      var1.map.clear();

      for(Node var2 = (Node)this.list.getFirst(); var2 != null; var2 = (Node)var2.getNext()) {
         Node var3 = new Node(var2.getKey(), var2.getValue());
         var1.list.add((Element)var3);
         var1.map.put(var2.getKey(), var3);
         var3.setExpiration(var2.getExpiration());
      }

      return var1;
   }

   public synchronized void clear() {
      this.map.clear();
      this.list.clear();
   }

   private void evictExpiredOrOverMaxCapacity() {
      long var1 = System.currentTimeMillis();
      Node var4 = (Node)this.list.getFirst();

      while(var4 != null && (var4.getExpiration() <= var1 || this.list.size() > this.maxCapacity)) {
         Node var3 = var4;
         var4 = (Node)var4.getNext();
         this.evictOne(var3);
      }

   }

   private void evictOne(Node var1) {
      assert var1.getList() == this.list;

      Node var2 = (Node)this.list.remove(var1);

      assert var2 == var1;

      var2 = (Node)this.map.remove(var1.getKey());

      assert var2 == var1;

   }

   public synchronized boolean containsValue(Object var1) {
      long var2 = System.currentTimeMillis();
      Node var5 = (Node)this.list.getFirst();

      while(true) {
         while(var5 != null) {
            Node var4 = var5;
            var5 = (Node)var5.getNext();
            if (var4.getExpiration() < var2) {
               if (var4.getValue() == var1) {
                  this.list.remove(var4);
                  var4.append();
                  this.evictExpiredOrOverMaxCapacity();
                  return true;
               }

               this.evictOne(var4);
            } else {
               for(var5 = (Node)this.list.getLast(); var5 != null; var5 = (Node)var5.getPrev()) {
                  if (var4.getValue() == var1) {
                     this.list.remove(var4);
                     var4.append();
                     return true;
                  }
               }
            }
         }

         return false;
      }
   }

   public synchronized Object get(Object var1) {
      Node var2 = (Node)this.map.get(var1);
      if (var2 == null) {
         this.evictExpiredOrOverMaxCapacity();
         return null;
      } else {
         this.list.remove(var2);
         var2.append();
         this.evictExpiredOrOverMaxCapacity();
         return var2.getValue();
      }
   }

   public synchronized Object put(Object var1, Object var2) {
      Node var3 = (Node)this.map.remove(var1);
      if (var3 == null) {
         var3 = new Node(var1, var2);
         var3.append();
         this.evictExpiredOrOverMaxCapacity();
         return this.map.put(var1, var3);
      } else {
         this.list.remove(var3);
         var3.append();
         this.evictExpiredOrOverMaxCapacity();
         return var3.setValue(var2);
      }
   }

   public synchronized Object putIfAbsent(Object var1, Object var2) {
      Node var3 = (Node)this.map.get(var1);
      if (var3 == null) {
         var3 = new Node(var1, var2);
         var3.append();
         this.evictExpiredOrOverMaxCapacity();
         return this.map.put(var1, var3);
      } else {
         this.list.remove(var3);
         var3.append();
         this.evictExpiredOrOverMaxCapacity();
         return var3.getValue();
      }
   }

   public synchronized Object remove(Object var1) {
      Node var2 = (Node)this.map.remove(var1);
      if (var2 == null) {
         this.evictExpiredOrOverMaxCapacity();
         return null;
      } else {
         this.list.remove(var2);
         this.evictExpiredOrOverMaxCapacity();
         return var2.getValue();
      }
   }

   public synchronized Set keySet() {
      return new AbstractSet() {
         public Iterator iterator() {
            return new IteratorImpl() {
               Object nextImpl(Node var1) {
                  return var1.getKey();
               }
            };
         }

         public int size() {
            return ExpiredMap.this.size();
         }

         public boolean contains(Object var1) {
            return ExpiredMap.this.containsValue(var1);
         }
      };
   }

   public synchronized Collection values() {
      return new AbstractCollection() {
         public Iterator iterator() {
            return new IteratorImpl() {
               Object nextImpl(Node var1) {
                  return var1.getValue();
               }
            };
         }

         public int size() {
            return ExpiredMap.this.size();
         }

         public boolean contains(Object var1) {
            return ExpiredMap.this.containsValue(var1);
         }
      };
   }

   public synchronized Set entrySet() {
      return new AbstractSet() {
         public Iterator iterator() {
            return new IteratorImpl() {
               Object nextImpl(Node var1) {
                  return var1;
               }
            };
         }

         public boolean contains(Object var1) {
            synchronized(ExpiredMap.this) {
               if (var1 instanceof Node && ExpiredMap.this.list.contains((Node)var1)) {
                  return true;
               } else if (!(var1 instanceof Map.Entry)) {
                  return false;
               } else {
                  Map.Entry var3 = (Map.Entry)var1;
                  Node var4 = (Node)ExpiredMap.this.map.get(var3.getKey());
                  return var4 != null && var4.equals(var3);
               }
            }
         }

         public boolean remove(Object var1) {
            synchronized(ExpiredMap.this) {
               if (!(var1 instanceof Map.Entry)) {
                  return false;
               } else {
                  Node var3 = (Node)ExpiredMap.this.map.remove(((Map.Entry)var1).getKey());
                  if (var3 == null) {
                     return false;
                  } else {
                     ExpiredMap.this.list.remove(var3);
                     return true;
                  }
               }
            }
         }

         public int size() {
            return ExpiredMap.this.size();
         }

         public void clear0() {
            ExpiredMap.this.clear();
         }
      };
   }

   private abstract class IteratorImpl implements Iterator {
      private Node node;

      private IteratorImpl() {
         this.node = (Node)ExpiredMap.this.list.getFirst();
      }

      public boolean hasNext() {
         synchronized(ExpiredMap.this) {
            return this.node != null;
         }
      }

      abstract Object nextImpl(Node var1);

      public Object next() {
         synchronized(ExpiredMap.this) {
            Node var2 = this.node;
            if (var2 == null) {
               throw new IllegalStateException("no next");
            } else {
               this.node = (Node)var2.getNext();
               return this.nextImpl(var2);
            }
         }
      }

      public void remove() {
         synchronized(ExpiredMap.this) {
            Node var2 = this.node;
            if (var2 == null) {
               throw new IllegalStateException("no next");
            } else {
               this.node = (Node)var2.getNext();
               ExpiredMap.this.list.remove(var2);
               ExpiredMap.this.map.remove(var2.getKey());
            }
         }
      }

      // $FF: synthetic method
      IteratorImpl(Object var2) {
         this();
      }
   }

   private class Node extends AbstractElement implements Map.Entry {
      private final Object key;
      private Object value;
      private long expiration;

      private Node(Object var2, Object var3) {
         this.key = var2;
         this.value = var3;
         this.expiration = System.currentTimeMillis() + ExpiredMap.this.ttl;
      }

      private void append() {
         ExpiredMap.this.list.add((Element)this);
         this.expiration = System.currentTimeMillis() + ExpiredMap.this.ttl;
      }

      public Object getKey() {
         return this.key;
      }

      public Object getValue() {
         return this.value;
      }

      public Object setValue(Object var1) {
         Object var2 = this.value;
         this.value = var1;
         return var2;
      }

      public long getExpiration() {
         return this.expiration;
      }

      public void setExpiration(long var1) {
         this.expiration = var1;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (!(var1 instanceof Map.Entry)) {
            return false;
         } else {
            boolean var10000;
            label47: {
               Map.Entry var2 = (Map.Entry)var1;
               if (this.key == null) {
                  if (var2.getKey() != null) {
                     break label47;
                  }
               } else if (this.key != var2.getKey() && !this.key.equals(var2.getKey())) {
                  break label47;
               }

               if (this.value == null) {
                  if (var2.getValue() != null) {
                     break label47;
                  }
               } else if (this.value != var2.getValue() && !this.value.equals(var2.getValue())) {
                  break label47;
               }

               var10000 = true;
               return var10000;
            }

            var10000 = false;
            return var10000;
         }
      }

      public int hashCode() {
         return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
      }

      public String toString() {
         return super.toString() + " - key: " + this.key + " value: " + this.value;
      }

      // $FF: synthetic method
      Node(Object var2, Object var3, Object var4) {
         this(var2, var3);
      }
   }
}
