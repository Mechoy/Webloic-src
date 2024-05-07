package weblogic.xml.util;

public final class HexBin {
   private static final int BASELENGTH = 255;
   private static final int LOOKUPLENGTH = 16;
   private static final byte[] hexNumberTable = new byte[255];
   private static final byte[] lookUpHexAlphabet = new byte[16];

   static boolean isHex(byte var0) {
      return hexNumberTable[var0] != -1;
   }

   public static byte[] encode(byte[] var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = var0.length;
         int var2 = var1 * 2;
         byte[] var3 = new byte[var2];

         for(int var4 = 0; var4 < var1; ++var4) {
            var3[var4 * 2] = lookUpHexAlphabet[var0[var4] >> 4];
            var3[var4 * 2 + 1] = lookUpHexAlphabet[var0[var4] & 15];
         }

         return var3;
      }
   }

   public static byte[] decode(byte[] var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = var0.length;
         if (var1 % 2 != 0) {
            return null;
         } else {
            int var2 = var1 / 2;
            byte[] var3 = new byte[var2];

            for(int var4 = 0; var4 < var2; ++var4) {
               if (!isHex(var0[var4 * 2]) || !isHex(var0[var4 * 2 + 1])) {
                  return null;
               }

               var3[var4] = (byte)(hexNumberTable[var0[var4 * 2]] << 4 | hexNumberTable[var0[var4 * 2 + 1]]);
            }

            return var3;
         }
      }
   }

   public static String decode(String param0) {
      // $FF: Couldn't be decompiled
   }

   static {
      int var0;
      for(var0 = 0; var0 < 255; ++var0) {
         hexNumberTable[var0] = -1;
      }

      for(var0 = 57; var0 >= 48; --var0) {
         hexNumberTable[var0] = (byte)(var0 - 48);
      }

      for(var0 = 70; var0 >= 65; --var0) {
         hexNumberTable[var0] = (byte)(var0 - 65 + 10);
      }

      for(var0 = 102; var0 >= 97; --var0) {
         hexNumberTable[var0] = (byte)(var0 - 97 + 10);
      }

      for(var0 = 0; var0 < 10; ++var0) {
         lookUpHexAlphabet[var0] = (byte)(48 + var0);
      }

      for(var0 = 10; var0 <= 15; ++var0) {
         lookUpHexAlphabet[var0] = (byte)(65 + var0 - 10);
      }

   }
}
