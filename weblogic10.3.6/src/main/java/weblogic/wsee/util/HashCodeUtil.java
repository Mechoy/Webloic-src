package weblogic.wsee.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public final class HashCodeUtil {
   public static final int SEED = 23;
   private static final int fODD_PRIME_NUMBER = 37;

   public static int hash(int var0, boolean var1) {
      return firstTerm(var0) + (var1 ? 1 : 0);
   }

   public static int hash(int var0, char var1) {
      return firstTerm(var0) + var1;
   }

   public static int hash(int var0, int var1) {
      return firstTerm(var0) + var1;
   }

   public static int hash(int var0, long var1) {
      return firstTerm(var0) + (int)(var1 ^ var1 >>> 32);
   }

   public static int hash(int var0, float var1) {
      return hash(var0, Float.floatToIntBits(var1));
   }

   public static int hash(int var0, double var1) {
      return hash(var0, Double.doubleToLongBits(var1));
   }

   public static int hash(int var0, Object var1) {
      int var2 = var0;
      if (var1 == null) {
         var2 = hash(var0, (int)0);
      } else if (!isArray(var1)) {
         var2 = hash(var0, var1.hashCode());
      } else {
         int var3 = Array.getLength(var1);

         for(int var4 = 0; var4 < var3; ++var4) {
            Object var5 = Array.get(var1, var4);
            var2 = hash(var2, var5);
         }
      }

      return var2;
   }

   public static int hash(int var0, Collection var1) {
      int var2 = var0;
      if (var1 == null) {
         var2 = hash(var0, (int)0);
      } else {
         int var3 = var1.size();
         int[] var4 = new int[var3];
         Iterator var5 = var1.iterator();

         int var6;
         for(var6 = 0; var6 < var3; ++var6) {
            var4[var6] = var5.next().hashCode();
         }

         Arrays.sort(var4);

         for(var6 = 0; var6 < var3; ++var6) {
            var2 = hash(var2, var4[var6]);
         }
      }

      return var2;
   }

   private static int firstTerm(int var0) {
      return 37 * var0;
   }

   private static boolean isArray(Object var0) {
      return var0.getClass().isArray();
   }
}
