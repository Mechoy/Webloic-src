package weblogic.security.acl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Vector;
import weblogic.logging.LogOutputStream;

/** @deprecated */
public class TTLCache {
   private LogOutputStream log;
   private Vector[] table;
   private int buckets;
   private int capacity;
   private int size;
   private long ttl;
   private int insertions;
   private int lookups;
   private int staleEvictions;
   private int lruEvictions;
   private int hits;
   private int misses;
   private static final int CLEANUP_PERIOD = 5000;
   private int opsSinceCleanup;

   public TTLCache(int var1, long var2) {
      this(var1, var1 * 6, var2);
   }

   public TTLCache(int var1, int var2, long var3) {
      if (var3 <= 0L) {
         throw new IllegalArgumentException("ttl <= 0");
      } else if (var2 < var1) {
         throw new IllegalArgumentException("capacity < buckets");
      } else {
         this.table = new Vector[var1];

         for(int var5 = 0; var5 < this.table.length; ++var5) {
            this.table[var5] = new Vector();
         }

         this.buckets = var1;
         this.capacity = var2;
         this.size = 0;
         this.ttl = var3;
      }
   }

   public Object put(Object var1, Object var2) {
      if (var2 == null) {
         throw new NullPointerException("null value");
      } else {
         this.maybeCleanup();
         int var3 = this.hash(var1);
         Vector var4 = this.table[var3];
         long var5 = System.currentTimeMillis();
         long var7 = Long.MAX_VALUE;
         int var9 = -1;
         Entry var10 = new Entry(var1, var2, var5);
         ++this.insertions;
         Enumeration var11 = var4.elements();

         for(int var12 = 0; var11.hasMoreElements(); ++var12) {
            Entry var13 = (Entry)var11.nextElement();
            if (var13 == null) {
               var4.setElementAt(var10, var12);
               ++this.size;
               return null;
            }

            if (var13.ttl < var5) {
               var4.setElementAt(var10, var12);
               ++this.staleEvictions;
               return null;
            }

            if (var13.key.equals(var1)) {
               var4.setElementAt(var10, var12);
               return var13.value;
            }

            if (var13.lastUse < var7) {
               var7 = var13.lastUse;
               var9 = var12;
            }
         }

         if (this.size < this.capacity) {
            var4.addElement(var10);
            ++this.size;
            return null;
         } else {
            Entry var16 = null;
            if (var9 != -1) {
               var16 = (Entry)var4.elementAt(var9);
               var4.setElementAt(var10, var9);
            } else {
               var4.addElement(var10);

               for(int var17 = 1; var17 < this.buckets; ++var17) {
                  var4 = this.table[(var3 + var17) % this.buckets];
                  var7 = Long.MAX_VALUE;
                  var9 = -1;
                  var11 = var4.elements();

                  for(int var14 = 0; var11.hasMoreElements(); ++var14) {
                     Entry var15 = (Entry)var11.nextElement();
                     if (var15 != null && var15.lastUse < var7) {
                        var7 = var15.lastUse;
                        var9 = var14;
                     }
                  }

                  if (var9 != -1) {
                     var16 = (Entry)var4.elementAt(var9);
                     var4.setElementAt((Object)null, var9);
                     ++this.lruEvictions;
                     if (var16 != null) {
                        return var16.value;
                     }
                  }
               }
            }

            ++this.lruEvictions;
            return null;
         }
      }
   }

   public Object put(Object var1) {
      return this.put(var1, var1);
   }

   public Object get(Object var1) {
      if (var1 == null) {
         return null;
      } else {
         this.maybeCleanup();
         Entry var2 = this.findEntry(var1);
         return var2 != null ? var2.value : null;
      }
   }

   private int hash(Object var1) {
      int var2 = var1.hashCode();
      return (var2 > 0 ? var2 : -var2) % this.buckets;
   }

   public Object remove(Object var1) {
      Vector var2 = this.table[this.hash(var1)];
      Enumeration var3 = var2.elements();
      long var4 = Long.MIN_VALUE;
      Entry var6 = null;

      for(int var7 = 0; var3.hasMoreElements(); ++var7) {
         Entry var8 = (Entry)var3.nextElement();
         if (var8 != null && var1.equals(var8.key)) {
            if (var6 == null) {
               if (var4 == Long.MIN_VALUE) {
                  var4 = System.currentTimeMillis();
               }

               if (var8.ttl >= var4) {
                  var6 = var8;
               }
            }

            var2.setElementAt((Object)null, var7);
         }
      }

      this.maybeCleanup();
      return var6;
   }

   public void clear() {
      for(int var1 = 0; var1 < this.table.length; ++var1) {
         this.table[var1] = new Vector();
      }

      this.size = 0;
      this.opsSinceCleanup = 0;
      this.insertions = 0;
      this.lookups = 0;
      this.staleEvictions = 0;
      this.lruEvictions = 0;
      this.hits = 0;
      this.misses = 0;
   }

   private void maybeCleanup() {
      ++this.opsSinceCleanup;
      if (this.opsSinceCleanup >= 5000) {
         this.cleanup();
      }

   }

   public void cleanup() {
      long var1 = System.currentTimeMillis();

      for(int var3 = 0; var3 < this.buckets; ++var3) {
         Vector var4 = this.table[var3];
         int var5 = var4.size();
         if (var5 > 0) {
            for(int var6 = 0; var6 < var5; ++var6) {
               Entry var7 = (Entry)var4.elementAt(var6);
               if (var7 != null) {
                  if (var7.ttl >= var1) {
                     continue;
                  }

                  ++this.staleEvictions;
               }

               var4.removeElementAt(var6);
               --this.size;
               --var6;
               --var5;
            }
         }
      }

      this.opsSinceCleanup = 0;
   }

   private Entry findEntry(Object var1) {
      if (var1 == null) {
         return null;
      } else {
         Vector var2 = this.table[this.hash(var1)];
         long var3 = Long.MIN_VALUE;
         Enumeration var5 = var2.elements();

         for(int var6 = 0; var5.hasMoreElements(); ++var6) {
            Entry var7 = (Entry)var5.nextElement();
            if (var7 != null && var1.equals(var7.key)) {
               if (var3 == Long.MIN_VALUE) {
                  var3 = System.currentTimeMillis();
               }

               if (var7.ttl >= var3) {
                  var7.lastUse = var3;
                  ++this.hits;
                  return var7;
               }

               var2.setElementAt((Object)null, var6);
               ++this.staleEvictions;
               --this.size;
            }
         }

         ++this.misses;
         return null;
      }
   }

   public int size() {
      return this.size;
   }

   public boolean isEmpty() {
      return this.size == 0;
   }

   public boolean containsKey(Object var1) {
      this.maybeCleanup();
      return this.findEntry(var1) != null;
   }

   public void setDebugLog(LogOutputStream var1) {
      this.log = var1;
   }

   public LogOutputStream getDebugLog() {
      return this.log;
   }

   public int getLookups() {
      return this.lookups;
   }

   public double getHitRate() {
      return (double)this.hits / (double)this.lookups;
   }

   public int getInsertions() {
      return this.insertions;
   }

   public double getStaleEvictionRate() {
      return (double)this.staleEvictions / (double)this.insertions;
   }

   public double getLRUEvictionRate() {
      return (double)this.lruEvictions / (double)this.insertions;
   }

   private static final class UnitTest {
      public static void main(String[] var0) {
         TTLCache var1 = new TTLCache(3, 25000L);
         BufferedReader var2 = new BufferedReader(new InputStreamReader(System.in));

         try {
            while(true) {
               long var3 = System.currentTimeMillis();
               String var5 = var2.readLine();
               if (var5 == null) {
                  break;
               }

               String var6;
               if (var5.equals("get")) {
                  var6 = var2.readLine();
                  System.out.println(var1.get(var6));
               } else if (var5.equals("put")) {
                  var6 = var2.readLine();
                  String var7 = var2.readLine();
                  var1.put(var6, var7);
               } else {
                  System.err.println("??? bad command: " + var5);
               }
            }
         } catch (IOException var8) {
            var8.printStackTrace();
            System.exit(1);
         }

      }
   }

   class Entry {
      Object key;
      Object value;
      long ttl;
      long lastUse;

      Entry(Object var2, Object var3, long var4) {
         this.key = var2;
         this.value = var3;
         this.ttl = var4 + TTLCache.this.ttl;
         this.lastUse = var4;
      }

      public Object getKey() {
         return this.key;
      }

      public Object getValue() {
         return this.value;
      }

      public long getTTL() {
         return this.ttl;
      }

      public boolean equals(Object var1) {
         if (var1 instanceof Entry && var1 != null) {
            Entry var2 = (Entry)var1;
            return this.ttl == var2.ttl && this.key.equals(var2.key) && this.value.equals(var2.value);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.key.hashCode() ^ this.value.hashCode();
      }
   }
}
