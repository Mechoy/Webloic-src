package weblogic.corba.utils;

import java.util.Arrays;
import java.util.Random;

public final class AlternateIndirectionHashtable {
   private static final boolean DEBUG = false;
   private int[] bucketToHandleMap;
   private Object[] handleToObjectMap;
   private int[] handleMap;
   private int count;
   private float loadFactor;
   private int threshold;
   private int capacity;

   public AlternateIndirectionHashtable(int var1, float var2) {
      if (var1 > 0 && !((double)var2 <= 0.0)) {
         for(this.capacity = 1; this.capacity < var1; this.capacity <<= 1) {
         }

         this.loadFactor = var2;
         this.bucketToHandleMap = new int[this.capacity];
         this.handleToObjectMap = new Object[this.capacity];
         this.handleMap = new int[this.capacity * 2];
         this.threshold = (int)(var2 * (float)this.capacity);
         this.clear();
      } else {
         throw new IllegalArgumentException();
      }
   }

   public AlternateIndirectionHashtable() {
      this(16, 3.0F);
   }

   public boolean isEmpty() {
      return this.count == 0;
   }

   private static final int hash(int var0) {
      var0 += ~(var0 << 9);
      var0 ^= var0 >>> 14;
      var0 += var0 << 4;
      var0 ^= var0 >>> 10;
      return var0;
   }

   public Object get(int var1) {
      int var2 = hash(var1) & this.bucketToHandleMap.length - 1;

      for(int var3 = this.bucketToHandleMap[var2]; var3 >= 0; var3 = this.handleMap[var3]) {
         if (this.handleMap[var3 + this.capacity] == var1) {
            return this.handleToObjectMap[var3];
         }
      }

      return null;
   }

   public Object put(int var1, Object var2) {
      if (var2 == null) {
         throw new NullPointerException();
      } else {
         return this.putUnchecked(var1, var2);
      }
   }

   private Object putUnchecked(int var1, Object var2) {
      if (this.count >= this.capacity) {
         this.increaseCapacity();
      }

      if (this.count >= this.threshold) {
         this.rehash();
      }

      int var3 = hash(var1) & this.bucketToHandleMap.length - 1;
      this.handleToObjectMap[this.count] = var2;
      this.handleMap[this.count] = this.bucketToHandleMap[var3];
      this.handleMap[this.count + this.capacity] = var1;
      this.bucketToHandleMap[var3] = this.count++;
      return var2;
   }

   public int reserve(int var1) {
      this.putUnchecked(var1, (Object)null);
      return this.count - 1;
   }

   public void putReserved(int var1, int var2, Object var3) {
      if (var3 == null) {
         throw new NullPointerException();
      } else {
         this.handleToObjectMap[var1] = var3;
      }
   }

   private void increaseCapacity() {
      int var1 = this.capacity * 2 + 1;
      Object[] var2 = new Object[var1];
      System.arraycopy(this.handleToObjectMap, 0, var2, 0, this.count);
      this.handleToObjectMap = var2;
      int[] var3 = new int[var1 * 2];
      System.arraycopy(this.handleMap, 0, var3, 0, this.count);
      System.arraycopy(this.handleMap, this.capacity, var3, var1, this.count);
      this.handleMap = var3;
      this.capacity = var1;
   }

   private void rehash() {
      int var1 = this.bucketToHandleMap.length << 1;
      this.bucketToHandleMap = new int[var1];
      Arrays.fill(this.bucketToHandleMap, -1);
      this.threshold = (int)((float)var1 * this.loadFactor);

      int var4;
      for(int var2 = 0; var2 < this.count; this.bucketToHandleMap[var4] = var2++) {
         int var3 = this.handleMap[var2 + this.capacity];
         var4 = hash(var3) & var1 - 1;
         this.handleMap[var2] = this.bucketToHandleMap[var4];
      }

   }

   public void clear() {
      Arrays.fill(this.bucketToHandleMap, -1);
      Arrays.fill(this.handleToObjectMap, 0, this.count, (Object)null);
      this.count = 0;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("{");

      for(int var2 = 0; var2 < this.count; ++var2) {
         var1.append("" + this.handleMap[var2 + this.capacity] + "=" + this.handleToObjectMap[var2]);
         var1.append(", ");
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
}
