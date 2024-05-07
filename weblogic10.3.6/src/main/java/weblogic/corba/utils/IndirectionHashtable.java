package weblogic.corba.utils;

import java.util.Random;

public final class IndirectionHashtable {
   private IndirectionTableEntry table;
   private IndirectionTableEntry tail;
   private int count;
   private final int tableSize;
   private static final int DEFAULT_TABLE_SIZE = 16;
   private static final Object NULL_OBJECT = new Object();

   public IndirectionHashtable(int var1) {
      if (var1 <= 0) {
         throw new IllegalArgumentException();
      } else {
         this.tableSize = var1;
         this.table = this.tail = new IndirectionTableEntry(var1);
      }
   }

   public IndirectionHashtable() {
      this(16);
   }

   public boolean isEmpty() {
      return this.table.count == 0;
   }

   public Object get(int var1) {
      IndirectionTableEntry var2 = this.table;
      if (var2.count == 0) {
         return null;
      } else {
         while(var2 != null && var2.positions[var2.count - 1] < var1) {
            var2 = var2.next;
         }

         if (var2 == null) {
            return null;
         } else {
            int var3 = 0;
            int var4 = var2.count - 1;

            while(var2.positions[var3] < var1 && var1 < var2.positions[var4]) {
               int var5 = (var4 - var3) / 2;
               if (var5 == 0) {
                  return null;
               }

               if (var1 < var2.positions[var3 + var5]) {
                  var4 = var3 + var5;
               } else {
                  var3 += var5;
               }
            }

            Object var6 = null;
            if (var2.positions[var3] == var1) {
               var6 = var2.values[var3];
            } else {
               if (var2.positions[var4] != var1) {
                  return null;
               }

               var6 = var2.values[var4];
            }

            return var6 == NULL_OBJECT ? null : var6;
         }
      }
   }

   public Object remove(int var1) {
      IndirectionTableEntry var2 = this.table;
      if (var2.count == 0) {
         return null;
      } else if (this.tail.positions[this.tail.count - 1] == var1) {
         --this.tail.count;
         Object var6 = this.tail.values[this.tail.count];
         this.tail.values[this.tail.count] = null;
         this.tail.positions[this.tail.count] = 0;
         if (this.tail.count == 0 && var2 != this.tail) {
            while(var2.next != this.tail) {
               var2 = var2.next;
            }

            this.tail = var2;
            var2.next = null;
         }

         return var6;
      } else {
         while(var2 != null && var2.positions[var2.count - 1] < var1) {
            var2 = var2.next;
         }

         if (var2 == null) {
            return null;
         } else {
            int var3 = 0;
            int var4 = var2.count - 1;

            while(var2.positions[var3] < var1 && var1 < var2.positions[var4]) {
               int var5 = (var4 - var3) / 2;
               if (var5 == 0) {
                  return null;
               }

               if (var1 < var2.positions[var3 + var5]) {
                  var4 = var3 + var5;
               } else {
                  var3 += var5;
               }
            }

            Object var7 = null;
            if (var2.positions[var3] == var1) {
               var7 = var2.values[var3];
               var2.values[var3] = null;
            } else if (var2.positions[var4] == var1) {
               var7 = var2.values[var4];
               var2.values[var4] = null;
            }

            return var7 == NULL_OBJECT ? null : var7;
         }
      }
   }

   public Object put(int var1, Object var2) {
      if (var2 == null) {
         var2 = NULL_OBJECT;
      }

      if (this.tail.count > 0 && var1 < this.tail.positions[this.tail.count - 1]) {
         throw new IllegalArgumentException("Out of order key: " + var1 + " " + this.toString());
      } else {
         if (this.tail.count >= this.tableSize) {
            this.tail.next = new IndirectionTableEntry(this.tableSize);
            this.tail.next.prev = this.tail;
            this.tail = this.tail.next;
         }

         this.tail.positions[this.tail.count] = var1;
         this.tail.values[this.tail.count++] = var2;
         return var2;
      }
   }

   public int reserve(int var1) {
      if (this.tail.count > 0 && var1 < this.tail.positions[this.tail.count - 1]) {
         throw new IllegalArgumentException("Out of order key: " + var1 + " " + this.toString());
      } else {
         if (this.tail.count >= this.tableSize) {
            this.tail.next = new IndirectionTableEntry(this.tableSize);
            this.tail.next.prev = this.tail;
            this.tail = this.tail.next;
         }

         this.tail.positions[this.tail.count] = var1;
         this.tail.values[this.tail.count++] = null;
         return this.tail.count - 1;
      }
   }

   public void putReserved(int var1, int var2, Object var3) {
      if (var3 == null) {
         var3 = NULL_OBJECT;
      }

      IndirectionTableEntry var4;
      for(var4 = this.tail; var4.count < var1 || var4.positions[var1] != var2; var4 = var4.prev) {
      }

      if (var4.count >= var1 && var4.positions[var1] == var2) {
         var4.values[var1] = var3;
      } else {
         throw new IllegalArgumentException("No reserved slot for: " + var1);
      }
   }

   public void clear() {
      this.table.next = null;
      this.tail = this.table;

      for(int var1 = 0; var1 < this.table.count; ++var1) {
         this.table.positions[var1] = 0;
         this.table.values[var1] = null;
      }

      this.table.count = 0;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("{");

      for(IndirectionTableEntry var2 = this.table; var2 != null; var2 = var2.next) {
         for(int var3 = 0; var3 < var2.count; ++var3) {
            var1.append("" + var2.positions[var3] + "=" + var2.values[var3]);
            var1.append(", ");
         }
      }

      var1.append("}");
      return var1.toString();
   }

   public static void main(String[] var0) {
      IndirectionHashtable var1 = new IndirectionHashtable();
      Random var2 = new Random(System.currentTimeMillis());
      int var3 = var2.nextInt();
      int[] var4 = new int[63];

      int var5;
      for(var5 = 0; var5 < var4.length; ++var5) {
         var4[var5] = var3 + var5 * 3;
         String var6 = String.valueOf(var4[var5]);
         var1.put(var4[var5], var6);
         System.out.println("put: " + var4[var5] + ", '" + var6 + "'");
      }

      System.out.println("TABLE: \n" + var1);

      for(var5 = 0; var5 < var4.length; ++var5) {
         Object var7 = var1.get(var4[var5]);
         if (var7 == null) {
            System.err.println("not found: " + var4[var5]);
         } else if (!var7.equals(String.valueOf(var4[var5]))) {
            System.err.println(var7 + "!=" + var4[var5]);
         } else {
            System.out.println("OK: " + var7);
         }
      }

   }

   static final class IndirectionTableEntry {
      int count = 0;
      final int[] positions;
      final Object[] values;
      IndirectionTableEntry next;
      IndirectionTableEntry prev;

      IndirectionTableEntry(int var1) {
         this.positions = new int[var1];
         this.values = new Object[var1];
      }
   }
}
