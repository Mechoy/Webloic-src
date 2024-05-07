package weblogic.xml.registry;

import java.util.HashSet;
import java.util.Set;

public class RefreshCacheLock {
   private int capacity;
   private Set<Key>[] containers;

   public RefreshCacheLock() {
      this(1);
   }

   public RefreshCacheLock(int var1) {
      this.capacity = 0;
      this.containers = null;
      if (var1 < 1) {
         throw new IllegalArgumentException("concurrent capacity must be great than 0");
      } else {
         this.capacity = var1;
         this.containers = new Set[this.capacity];

         for(int var2 = 0; var2 < this.containers.length; ++var2) {
            this.containers[var2] = new HashSet();
         }

      }
   }

   public void lock(String var1, String var2) {
      Key var3 = this.createKey(var1, var2);
      Set var4 = this.containers[Math.abs(var3.hashCode() % this.capacity)];
      synchronized(var4) {
         while(var4.contains(var3)) {
            try {
               var4.wait();
            } catch (InterruptedException var8) {
            }
         }

         if (!var4.contains(var3)) {
            var4.add(var3);
         }

      }
   }

   public void unlock(String var1, String var2) {
      Key var3 = this.createKey(var1, var2);
      Set var4 = this.containers[Math.abs(var3.hashCode() % this.capacity)];
      synchronized(var4) {
         if (var4.contains(var3)) {
            var4.remove(var3);
         }

         var4.notifyAll();
      }
   }

   private Key createKey(String var1, String var2) {
      return new Key(var1, var2);
   }

   private class Key {
      private String publicId;
      private String systemId;

      public Key(String var2, String var3) {
         this.publicId = var2;
         this.systemId = var3;
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof Key)) {
            return false;
         } else if (this == var1) {
            return true;
         } else {
            Key var2 = (Key)var1;
            return (this.publicId == var2.publicId || this.publicId != null && this.publicId.equals(var2.publicId)) && (this.systemId == var2.systemId || this.systemId != null && this.systemId.equals(var2.systemId));
         }
      }

      public int hashCode() {
         int var2 = 1;
         var2 = 31 * var2 + (this.publicId == null ? 0 : this.publicId.hashCode());
         var2 = 31 * var2 + (this.systemId == null ? 0 : this.systemId.hashCode());
         return var2;
      }
   }
}
