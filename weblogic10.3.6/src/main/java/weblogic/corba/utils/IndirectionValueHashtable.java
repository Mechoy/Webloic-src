package weblogic.corba.utils;

import java.util.Arrays;
import java.util.Random;

public final class IndirectionValueHashtable {
   private static final boolean DEBUG = false;
   private int[] bucketToHandleMap;
   private Object[] handleToObjectMap;
   private int[] handleMap;
   private int count;
   private int threshold;
   private int capacity;
   private float loadFactor;

   public IndirectionValueHashtable(int var1, float var2) {
      if (var1 > 0 && !((double)var2 <= 0.0)) {
         this.loadFactor = var2;
         this.bucketToHandleMap = new int[var1];
         this.handleToObjectMap = new Object[var1];
         this.handleMap = new int[var1 * 3];
         this.threshold = (int)((float)var1 * var2);
         this.capacity = var1;
         this.clear();
      } else {
         throw new IllegalArgumentException();
      }
   }

   public IndirectionValueHashtable(int var1) {
      this(var1, 3.0F);
   }

   public IndirectionValueHashtable() {
      this(10, 3.0F);
   }

   public int size() {
      return this.count;
   }

   public boolean isEmpty() {
      return this.count == 0;
   }

   private static final int hash(Object var0, int var1) {
      return var0 instanceof String ? var0.hashCode() + var1 & Integer.MAX_VALUE : System.identityHashCode(var0) + var1 & Integer.MAX_VALUE;
   }

   public int get(Object var1, int var2) {
      int var3 = hash(var1, var2);

      for(int var4 = this.bucketToHandleMap[var3 % this.bucketToHandleMap.length]; var4 >= 0; var4 = this.handleMap[var4]) {
         if (var1 instanceof String && var1.equals(this.handleToObjectMap[var4])) {
            return this.handleMap[var4 + this.capacity];
         }

         if (this.handleToObjectMap[var4] == var1) {
            return this.handleMap[var4 + this.capacity];
         }
      }

      return -var3;
   }

   private void increaseCapacity() {
      int var1 = this.capacity * 2 + 1;
      Object[] var2 = new Object[var1];
      System.arraycopy(this.handleToObjectMap, 0, var2, 0, this.count);
      this.handleToObjectMap = var2;
      int[] var3 = new int[var1 * 3];
      System.arraycopy(this.handleMap, 0, var3, 0, this.count);
      System.arraycopy(this.handleMap, this.capacity, var3, var1, this.count);
      System.arraycopy(this.handleMap, this.capacity * 2, var3, var1 * 2, this.count);
      this.handleMap = var3;
      this.capacity = var1;
   }

   private void rehash() {
      int var1 = this.bucketToHandleMap.length * 2 + 1;
      this.bucketToHandleMap = new int[var1];
      Arrays.fill(this.bucketToHandleMap, -1);
      this.threshold = (int)((float)var1 * this.loadFactor);

      int var4;
      for(int var2 = 0; var2 < this.count; this.bucketToHandleMap[var4] = var2++) {
         Object var10000 = this.handleToObjectMap[var2];
         var4 = this.handleMap[var2 + this.capacity * 2] % this.bucketToHandleMap.length;
         this.handleMap[var2] = this.bucketToHandleMap[var4];
      }

   }

   public void put(Object var1, int var2, int var3, int var4) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         if (this.count >= this.capacity) {
            this.increaseCapacity();
         }

         if (this.count >= this.threshold) {
            this.rehash();
         }

         var4 = var4 == 0 ? hash(var1, var2) : -var4;
         int var5 = var4 % this.bucketToHandleMap.length;
         this.handleToObjectMap[this.count] = var1;
         this.handleMap[this.count] = this.bucketToHandleMap[var5];
         this.handleMap[this.count + this.capacity] = var3;
         this.handleMap[this.count + this.capacity * 2] = var4;
         this.bucketToHandleMap[var5] = this.count++;
      }
   }

   public void put(Object var1, int var2, int var3) {
      this.put(var1, var2, var3, 0);
   }

   public void clear() {
      Arrays.fill(this.bucketToHandleMap, -1);
      Arrays.fill(this.handleToObjectMap, 0, this.count, (Object)null);
      this.count = 0;
   }

   public static void main(String[] var0) {
      IndirectionValueHashtable var1 = new IndirectionValueHashtable();
      Random var2 = new Random(System.currentTimeMillis());
      int var3 = var2.nextInt();
      Object[] var4 = new Object[127];
      int[] var5 = new int[127];

      int var6;
      for(var6 = 0; var6 < var4.length; ++var6) {
         var5[var6] = var2.nextInt();
         var4[var6] = String.valueOf(var5[var6]);
         var1.put(var4[var6], var6 % 2, var5[var6]);
      }

      System.out.println("TABLE: \n" + var1);

      for(var6 = 0; var6 < var4.length; ++var6) {
         int var7 = var1.get(var4[var6], var6 % 2);
         if (var7 == 0) {
            System.err.println("not found: " + var4[var6]);
         } else if (var7 != var5[var6]) {
            System.err.println(var7 + "!=" + var5[var6]);
         } else {
            System.out.println("OK: " + var4[var6]);
         }
      }

   }
}
