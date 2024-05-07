package weblogic.jms.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

final class LockedMap implements Cloneable, Map {
   private final String name;
   private Object lock;
   private HashMap map = new HashMap();

   LockedMap(String var1, Object var2) {
      this.name = var1;
      this.lock = var2;
   }

   String getName() {
      return this.name;
   }

   void setLock(Object var1) {
      this.lock = var1;
   }

   public Object getLock() {
      return this.lock;
   }

   Iterator cloneValuesIterator() {
      synchronized(this.lock) {
         Iterator var10000;
         try {
            var10000 = ((LockedMap)this.clone()).valuesIterator();
         } catch (CloneNotSupportedException var4) {
            throw new AssertionError(var4);
         }

         return var10000;
      }
   }

   Iterator valuesIterator() {
      synchronized(this.lock) {
         return this.values().iterator();
      }
   }

   protected Object clone() throws CloneNotSupportedException {
      synchronized(this.lock) {
         LockedMap var2 = (LockedMap)super.clone();
         var2.map = (HashMap)this.map.clone();
         return var2;
      }
   }

   public int size() {
      synchronized(this.lock) {
         return this.map.size();
      }
   }

   public boolean isEmpty() {
      synchronized(this.lock) {
         return this.map.isEmpty();
      }
   }

   public boolean containsKey(Object var1) {
      synchronized(this.lock) {
         return this.map.containsKey(var1);
      }
   }

   public boolean containsValue(Object var1) {
      synchronized(this.lock) {
         return this.map.containsValue(var1);
      }
   }

   public Object get(Object var1) {
      synchronized(this.lock) {
         return this.map.get(var1);
      }
   }

   public Object put(Object var1, Object var2) {
      synchronized(this.lock) {
         return this.map.put(var1, var2);
      }
   }

   public Object remove(Object var1) {
      synchronized(this.lock) {
         return this.map.remove(var1);
      }
   }

   public void putAll(Map var1) {
      synchronized(this.lock) {
         this.map.putAll(var1);
      }
   }

   public void clear() {
      synchronized(this.lock) {
         this.map.clear();
      }
   }

   public Set keySet() {
      synchronized(this.lock) {
         return this.map.keySet();
      }
   }

   public Collection values() {
      synchronized(this.lock) {
         return this.map.values();
      }
   }

   public Set entrySet() {
      synchronized(this.lock) {
         return this.map.entrySet();
      }
   }

   public boolean equals(Object var1) {
      synchronized(this.lock) {
         return this.map.equals(var1);
      }
   }

   public int hashCode() {
      synchronized(this.lock) {
         return this.map.hashCode();
      }
   }

   public String toString() {
      Iterator var1 = this.valuesIterator();
      byte var2 = 1;
      String var3 = "{ [" + this.getName() + "] (";
      Object var4;
      if (var1.hasNext() && (var4 = var1.next()) != null) {
         for(var3 = var3 + var4.getClass().getName() + " " + var4.toString() + ")"; var1.hasNext(); var3 = var3 + "  (" + var4.getClass().getName() + "#" + var2 + " " + var4.toString() + ")") {
            var4 = var1.next();
         }

         return var3 + " }";
      } else {
         return var3 + null + "#" + var2 + " null) }";
      }
   }
}
