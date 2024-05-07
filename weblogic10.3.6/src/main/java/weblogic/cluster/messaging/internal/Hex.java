package weblogic.cluster.messaging.internal;

import java.io.UnsupportedEncodingException;

public final class Hex {
   private static final char[] HEX_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
   private static final int COLS_PER_ROW = 8;
   private static final int BYTES_PER_ROW = 16;

   private Hex() {
   }

   public static String asHex(byte[] var0, int var1) {
      return asHex(var0, var1, true);
   }

   public static String asUnicode(String var0) {
      String var1 = "";

      for(int var2 = 0; var2 < var0.length(); ++var2) {
         var1 = var1 + "\\u" + asHex(var0.charAt(var2) >> 8) + asHex(var0.charAt(var2));
      }

      return var1;
   }

   public static String asUnicode(char var0) {
      StringBuffer var1 = new StringBuffer();
      var1.append("\\u");
      var1.append(asHex(var0 >> 8) + asHex(var0));
      return var1.toString();
   }

   public static String asHex(String var0, String var1) throws UnsupportedEncodingException {
      byte[] var2 = var0.getBytes(var1);
      return asHex(var2);
   }

   public static byte[] asHexBytes(byte[] var0, int var1, boolean var2) {
      int var3 = Math.min(var1, var0.length);
      int var5 = 0;
      byte[] var4;
      if (var2) {
         var4 = new byte[var3 * 2 + 2];
         var4[0] = 48;
         var4[1] = 120;
         var5 += 2;
      } else {
         var4 = new byte[var3 * 2];
      }

      for(int var6 = 0; var6 < var3; ++var6) {
         var4[var5++] = (byte)HEX_CHARS[(var0[var6] & 240) >> 4];
         var4[var5++] = (byte)HEX_CHARS[(var0[var6] & 15) >> 0];
      }

      return var4;
   }

   public static String asHex(byte[] var0, int var1, boolean var2) {
      return new String(asHexBytes(var0, var1, var2), 0);
   }

   public static byte[] fromHexString(byte[] var0, int var1) {
      int var2 = 0;
      if (var0[0] == 48 && (var0[1] == 120 || var0[1] == 88)) {
         var2 += 2;
         var1 -= 2;
      }

      int var3 = var1 / 2;
      byte[] var4 = new byte[var3];

      for(int var5 = 0; var5 < var3; ++var5) {
         var4[var5] = (byte)(hexValueOf(var0[var2++]) << 4 | hexValueOf(var0[var2++]));
      }

      return var4;
   }

   public static byte[] fromHexString(String var0) {
      byte[] var1;
      try {
         var1 = var0.getBytes("US-ASCII");
      } catch (UnsupportedEncodingException var4) {
         var1 = new byte[var0.length()];

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var1[var3] = (byte)var0.charAt(var3);
         }
      }

      return fromHexString(var1, var1.length);
   }

   public static String asHex(int var0) {
      char[] var1 = new char[]{HEX_CHARS[(var0 & 240) >> 4], HEX_CHARS[(var0 & 15) >> 0]};
      return new String(var1);
   }

   public static String asHex(byte[] var0) {
      return asHex(var0, var0.length);
   }

   public static int hexValueOf(int var0) {
      if (var0 >= 48 && var0 <= 57) {
         return var0 - 48;
      } else if (var0 >= 97 && var0 <= 102) {
         return var0 - 97 + 10;
      } else {
         return var0 >= 65 && var0 <= 70 ? var0 - 65 + 10 : 0;
      }
   }

   public static String dump(byte[] var0) {
      return var0 == null ? "" + var0 : dump(var0, 0, var0.length);
   }

   public static String dump(byte[] var0, int var1, int var2) {
      if (var1 < 0) {
         var1 = 0;
      }

      int var3 = Math.min(var0.length, var1 + var2);
      int var4 = var1 & -16;
      int var5 = var3 + 15 & -16;
      StringBuffer var6 = new StringBuffer();
      int var8 = var4;

      for(int var9 = var4; var9 < var5; ++var9) {
         if (var9 % 16 == 0) {
            lineLabel(var6, var9);
            var8 = var9;
         }

         if (var9 >= var1 && var9 < var3) {
            var6.append(asHex(var0[var9]));
         } else {
            var6.append("  ");
         }

         if (var9 % 2 == 1) {
            var6.append(' ');
         }

         if (var9 % 16 == 15) {
            var6.append("  ");

            for(int var10 = var8; var10 < var8 + 16; ++var10) {
               if (var10 >= var1 && var10 < var3) {
                  var6.append(toPrint(var0[var10]));
               } else {
                  var6.append(' ');
               }
            }

            var6.append('\n');
         }
      }

      return var6.toString();
   }

   public static final boolean isHexChar(int var0) {
      switch (var0) {
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 97:
         case 98:
         case 99:
         case 100:
         case 101:
         case 102:
            return true;
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 79:
         case 80:
         case 81:
         case 82:
         case 83:
         case 84:
         case 85:
         case 86:
         case 87:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
         case 93:
         case 94:
         case 95:
         case 96:
         default:
            return false;
      }
   }

   private static char toPrint(byte var0) {
      return var0 >= 32 && var0 <= 126 ? (char)var0 : '.';
   }

   private static void lineLabel(StringBuffer var0, int var1) {
      String var2 = (new Integer(var1)).toString();
      StringBuffer var3;
      if (var2.length() <= 5) {
         var3 = new StringBuffer("    ");
         var3.insert(5 - var2.length(), var2);
         var3.setLength(5);
      } else {
         var3 = new StringBuffer(var2);
      }

      var0.append(var3);
      var0.append(": ");
   }
}
