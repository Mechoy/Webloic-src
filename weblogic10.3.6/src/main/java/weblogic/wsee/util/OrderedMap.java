package weblogic.wsee.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OrderedMap implements Map {
   private HashMap map = new HashMap();
   private List keys = new ArrayList();
   private List values = new ArrayList();

   public String toString() {
      return this.map.toString();
   }

   public int size() {
      return this.map.size();
   }

   public boolean isEmpty() {
      return this.map.isEmpty();
   }

   public boolean containsKey(Object var1) {
      return this.map.containsKey(var1);
   }

   public boolean containsValue(Object var1) {
      return this.map.containsValue(var1);
   }

   public Object get(Object var1) {
      return this.map.get(var1);
   }

   public Object put(Object var1, Object var2) {
      this.map.put(var1, var2);
      this.keys.add(var1);
      this.values.add(var2);
      return var2;
   }

   public Object remove(Object var1) {
      Object var2 = this.map.get(var1);
      this.keys.remove(var1);
      this.values.remove(var2);
      return this.map.remove(var1);
   }

   public void putAll(Map var1) {
      Iterator var2 = var1.keySet().iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         Object var4 = var1.get(var3);
         this.put(var3, var4);
      }

   }

   public void clear() {
      this.map.clear();
      this.keys.clear();
      this.values.clear();
   }

   public Set keySet() {
      return new OrderedSet(this.keys);
   }

   public Collection values() {
      return this.values;
   }

   public Set entrySet() {
      throw new Error("not supported");
   }

   public class OrderedSet implements Set {
      List list;

      public OrderedSet(List var2) {
         this.list = var2;
      }

      public int size() {
         return this.list.size();
      }

      public boolean isEmpty() {
         return this.list.isEmpty();
      }

      public boolean contains(Object var1) {
         return this.list.contains(var1);
      }

      public Iterator iterator() {
         return this.list.iterator();
      }

      public Object[] toArray() {
         return this.list.toArray();
      }

      public Object[] toArray(Object[] var1) {
         return this.list.toArray(var1);
      }

      public boolean add(Object var1) {
         throw new Error("method not supported");
      }

      public boolean remove(Object var1) {
         throw new Error("method not supported");
      }

      public boolean containsAll(Collection var1) {
         return this.list.containsAll(var1);
      }

      public boolean addAll(Collection var1) {
         throw new Error("method not supported");
      }

      public boolean retainAll(Collection var1) {
         throw new Error("method not supported");
      }

      public boolean removeAll(Collection var1) {
         throw new Error("method not supported");
      }

      public void clear() {
         throw new Error("method not supported");
      }
   }
}
