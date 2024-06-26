package weblogic.ejb.container.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;

public class PartialOrderSet implements Set {
   public static final int ORDER_BY_TIME = 100;
   public static final int ORDER_BY_VALUE = 101;
   private static final Object PRESENT = new Object();
   private int ordering = 100;
   private boolean initialized = false;
   private boolean isComparable = false;
   private Map contents = null;
   private Node first = null;
   private Node last = null;
   private int size = 0;

   public PartialOrderSet() {
   }

   public PartialOrderSet(int var1) {
      if (var1 != 100 && var1 != 101) {
         throw new IllegalArgumentException("Illegal ordering specified for " + this.getClass().getName());
      } else {
         this.ordering = var1;
      }
   }

   public int size() {
      return this.size;
   }

   public boolean isEmpty() {
      return this.size == 0;
   }

   public boolean contains(Object var1) {
      if (var1 == null) {
         return false;
      } else {
         if (!this.initialized) {
            this.initialize(var1);
         }

         if (!this.isComparable && this.ordering != 100) {
            Integer var2 = new Integer(var1.hashCode());

            for(Node var3 = (Node)this.contents.get(var2); var3 != null; var3 = var3.getNext()) {
               if (var3.getValue().equals(var1)) {
                  return true;
               }
            }

            return false;
         } else {
            return this.contents.get(var1) != null;
         }
      }
   }

   public Iterator iterator() {
      return new PartialOrderIterator();
   }

   public Object[] toArray() {
      throw new UnsupportedOperationException("Method not implemented.");
   }

   public Object[] toArray(Object[] var1) {
      throw new UnsupportedOperationException("Method not implemented.");
   }

   private void initialize(Object var1) {
      if (this.ordering == 101) {
         if (var1 instanceof Comparable) {
            this.isComparable = true;
         }

         this.contents = new TreeMap();
      } else {
         this.contents = new HashMap();
      }

      this.initialized = true;
   }

   public boolean add(Object var1) {
      if (!this.initialized) {
         this.initialize(var1);
      }

      if (var1 == null) {
         throw new IllegalArgumentException("Null not supported.");
      } else {
         boolean var2 = false;
         if (this.ordering == 100) {
            if (this.contents.get(var1) == null) {
               Node var3 = new Node(var1);
               var3.setPrev(this.last);
               if (this.last == null) {
                  this.first = var3;
               } else {
                  this.last.setNext(var3);
               }

               this.last = var3;
               this.contents.put(var1, var3);
               var2 = true;
            }
         } else if (this.isComparable) {
            var2 = this.contents.put(var1, PRESENT) == null;
         } else if (!this.contains(var1)) {
            Integer var6 = new Integer(var1.hashCode());
            Node var4 = (Node)this.contents.get(var6);
            if (var4 == null) {
               this.contents.put(var6, new Node(var1));
            } else {
               Node var5 = new Node(var1);
               var5.next = var4.next;
               var4.next = var5;
            }

            var2 = true;
         }

         if (var2) {
            ++this.size;
         }

         return var2;
      }
   }

   public boolean remove(Object var1) {
      throw new UnsupportedOperationException("Method not implemented.");
   }

   public boolean containsAll(Collection var1) {
      throw new UnsupportedOperationException("Method not implemented.");
   }

   public boolean addAll(Collection var1) {
      throw new UnsupportedOperationException("Method not implemented.");
   }

   public boolean addAll(int var1, Collection var2) {
      throw new UnsupportedOperationException("Method not implemented.");
   }

   public boolean removeAll(Collection var1) {
      throw new UnsupportedOperationException("Method not implemented.");
   }

   public boolean retainAll(Collection var1) {
      throw new UnsupportedOperationException("Method not implemented.");
   }

   public void clear() {
      throw new UnsupportedOperationException("Method not implemented.");
   }

   public boolean equals(Object var1) {
      throw new UnsupportedOperationException("Method not implemented.");
   }

   public int hashCode() {
      throw new UnsupportedOperationException("Method not implemented.");
   }

   static class Node {
      private Object value = null;
      private Node next = null;
      private Node prev = null;

      public Node(Object var1) {
         this.value = var1;
      }

      public Object getValue() {
         return this.value;
      }

      public void setNext(Node var1) {
         this.next = var1;
      }

      public Node getNext() {
         return this.next;
      }

      public void setPrev(Node var1) {
         this.prev = var1;
      }

      public Node getPrev() {
         return this.prev;
      }
   }

   class PartialOrderIterator implements Iterator {
      Iterator iterator = null;
      Node curr = null;

      public PartialOrderIterator() {
         if (PartialOrderSet.this.ordering == 100) {
            this.curr = PartialOrderSet.this.first;
         } else if (!PartialOrderSet.this.initialized) {
            this.iterator = (new HashMap()).keySet().iterator();
         } else {
            this.iterator = PartialOrderSet.this.contents.keySet().iterator();
            if (!PartialOrderSet.this.isComparable && this.iterator.hasNext()) {
               this.curr = (Node)PartialOrderSet.this.contents.get(this.iterator.next());
            }
         }

      }

      public boolean hasNext() {
         return this.curr != null || this.iterator != null && this.iterator.hasNext();
      }

      public Object next() {
         if (this.curr != null) {
            Object var1 = this.curr.getValue();
            this.curr = this.curr.next;
            if (this.curr == null && this.iterator != null && this.iterator.hasNext()) {
               this.curr = (Node)PartialOrderSet.this.contents.get(this.iterator.next());
            }

            return var1;
         } else if (this.iterator == null) {
            throw new NoSuchElementException();
         } else {
            return this.iterator.next();
         }
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }
   }
}
