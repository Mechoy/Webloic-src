package weblogic.wtc.jatmi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import weblogic.wtc.WTCLogger;

public final class Utilities {
   public static final String encode = getEncode();
   public static final boolean supportIllformedEncodedData = getIllformedDataSupport();

   public static String getEncode() {
      String var0 = null;

      try {
         var0 = System.getProperty("weblogic.wtc.encoding");
         if (var0 != null) {
            String var1 = new String("test");
            var1.getBytes(var0);
         }
      } catch (Exception var2) {
         WTCLogger.logErrorUnsupportedEncoding(var0);
         var0 = null;
      }

      return var0;
   }

   public static boolean getIllformedDataSupport() {
      boolean var0 = false;
      String var1 = System.getProperty("weblogic.wtc.supportIllformedEncodedData");
      if (var1 != null && "true".equals(var1)) {
         var0 = true;
      }

      return var0;
   }

   public static byte[] getEncBytes(String var0) {
      if (encode != null) {
         try {
            return var0.getBytes(encode);
         } catch (UnsupportedEncodingException var2) {
            WTCLogger.logErrorUnsupportedEncoding(encode);
            return var0.getBytes();
         }
      } else {
         return var0.getBytes();
      }
   }

   public static String getEncString(byte[] var0) {
      String var1;
      if (encode != null) {
         try {
            var1 = new String(var0, encode);
         } catch (UnsupportedEncodingException var5) {
            WTCLogger.logErrorUnsupportedEncoding(encode);
            return new String(var0);
         }
      } else {
         var1 = new String(var0);
      }

      if (supportIllformedEncodedData && var1.length() == 0 && var0.length > 1) {
         for(int var2 = 1; var2 <= 4; ++var2) {
            if (encode != null) {
               try {
                  var1 = new String(var0, 0, var0.length - var2, encode);
               } catch (UnsupportedEncodingException var4) {
                  WTCLogger.logErrorUnsupportedEncoding(encode);
                  return new String(var0);
               }
            } else {
               var1 = new String(var0, 0, var0.length - var2);
            }

            if (var1.length() > 0 || var0.length - var2 <= 1) {
               break;
            }
         }
      }

      return var1;
   }

   public static String getEncString(byte[] var0, int var1, int var2) {
      if (encode != null) {
         try {
            return new String(var0, var1, var2, encode);
         } catch (UnsupportedEncodingException var4) {
            WTCLogger.logErrorUnsupportedEncoding(encode);
            return new String(var0, var1, var2);
         }
      } else {
         return new String(var0, var1, var2);
      }
   }

   public static int roundup4(int var0) {
      return var0 + 3 & -4;
   }

   public static int xdr_encode_string_length(DataOutputStream var0, String var1, int var2) throws IOException {
      var0.writeInt(var2);
      int var3;
      if (var1 != null) {
         byte[] var6 = getEncBytes(var1);
         var3 = var6.length;
         var0.write(var6);
      } else {
         var3 = 0;
      }

      int var4 = roundup4(var2) - var3;

      for(int var5 = 0; var5 < var4; ++var5) {
         var0.writeByte(0);
      }

      return 4 + var2;
   }

   public static int xdr_encode_string_length(DataOutputStream var0, String var1, int var2, boolean var3) throws IOException {
      return xdr_encode_string_length(var0, var1, var2);
   }

   public static int xdr_encode_string(DataOutputStream var0, String var1) throws IOException {
      if (var1 == null) {
         var0.writeInt(0);
         return 4;
      } else {
         byte[] var5 = getEncBytes(var1);
         int var2 = var5.length;
         int var3 = roundup4(var2) - var2;
         var0.writeInt(var2);
         if (var2 == 0) {
            return 4;
         } else {
            var0.write(var5);

            for(int var4 = 0; var4 < var3; ++var4) {
               var0.writeByte(0);
            }

            return 4 + var2 + var3;
         }
      }
   }

   public static String xdr_decode_string(DataInputStream var0, byte[] var1) throws IOException {
      int var2 = var0.readInt();
      if (var2 == 0) {
         return null;
      } else {
         int var3 = roundup4(var2);
         byte[] var5;
         if (var1 != null && var1.length >= var3) {
            var5 = var1;
         } else {
            var5 = new byte[var3];
         }

         for(int var4 = 0; var4 < var3; ++var4) {
            var5[var4] = var0.readByte();
         }

         return getEncString(var5, 0, var2);
      }
   }

   public static int xdr_encode_string(DataOutputStream var0, String var1, boolean var2) throws IOException {
      if (var2) {
         char[] var6 = new char[]{'\u0000'};
         if (var1 == null) {
            var1 = new String(var6);
         } else {
            var1 = var1 + new String(var6);
         }
      } else if (var1 == null) {
         var0.writeInt(0);
         return 4;
      }

      byte[] var7 = getEncBytes(var1);
      int var3 = var7.length;
      int var4 = roundup4(var3) - var3;
      var0.writeInt(var3);
      if (var3 == 0) {
         return 4;
      } else {
         var0.write(var7);

         for(int var5 = 0; var5 < var4; ++var5) {
            var0.writeByte(0);
         }

         return 4 + var3 + var4;
      }
   }

   public static String xdr_decode_string(DataInputStream var0, byte[] var1, boolean var2) throws IOException {
      int var3 = var0.readInt();
      if (var3 == 0) {
         return null;
      } else {
         int var4 = roundup4(var3);
         byte[] var6;
         if (var1 != null && var1.length >= var4) {
            var6 = var1;
         } else {
            var6 = new byte[var4];
         }

         for(int var5 = 0; var5 < var4; ++var5) {
            var6[var5] = var0.readByte();
         }

         String var7 = getEncString(var6, 0, var3);
         if (var2) {
            int var8 = var7.indexOf(0);
            if (var8 >= 0) {
               if (var8 == 0) {
                  var7 = null;
               } else {
                  var7 = var7.substring(0, var8);
               }
            }
         }

         return var7;
      }
   }

   public static int xdr_length_bstring(byte[] var0) {
      return var0 == null ? 4 : 4 + roundup4(var0.length);
   }

   public static int xdr_encode_bstring(DataOutputStream var0, byte[] var1) throws IOException {
      if (var1 == null) {
         var0.writeInt(0);
         return 4;
      } else {
         int var2 = var1.length;
         int var3 = roundup4(var2) - var2;
         var0.writeInt(var2);
         if (var2 == 0) {
            return 4;
         } else {
            var0.write(var1, 0, var2);

            for(int var4 = 0; var4 < var3; ++var4) {
               var0.writeByte(0);
            }

            return 4 + var2 + var3;
         }
      }
   }

   public static byte[] xdr_decode_bstring(DataInputStream var0) throws IOException {
      int var1 = var0.readInt();
      if (var1 == 0) {
         return null;
      } else {
         int var3 = roundup4(var1) - var1;
         byte[] var4 = new byte[var1];
         int var2 = 0;

         do {
            int var5 = var0.read(var4, var2, var1 - var2);
            if (var5 == -1) {
               break;
            }

            var2 += var5;
         } while(var2 < var1);

         if (var3 > 0) {
            var0.skipBytes(var3);
         }

         return var4;
      }
   }

   public static int xdr_encode_decimal(DataOutputStream var0, Decimal var1) throws IOException {
      if (var1 == null) {
         return 0;
      } else {
         var0.writeInt(var1.exponent());
         var0.writeInt(var1.sign());
         var0.writeInt(var1.numDigits());
         byte[] var2 = var1.digits();
         var0.writeInt(16);
         int var3;
         if (var2.length > 16) {
            var3 = 16;
         } else {
            var3 = var2.length;
         }

         var0.write(var2, 0, var3);
         int var4 = roundup4(16) - var3;

         for(int var5 = 0; var5 < var4; ++var5) {
            var0.writeByte(0);
         }

         return 16 + var3 + var4;
      }
   }

   public static Decimal xdr_decode_decimal(DataInputStream var0) throws IOException {
      int var2 = var0.readInt();
      int var1 = var0.readInt();
      int var3 = var0.readInt();
      int var4 = var0.readInt();
      if (var4 == 0) {
         return new Decimal();
      } else {
         int var5 = roundup4(var4) - var4;
         byte[] var6 = new byte[var4];
         int var7 = 0;

         do {
            int var8 = var0.read(var6, var7, var4 - var7);
            if (var8 == -1) {
               break;
            }

            var7 += var8;
         } while(var7 < var4);

         if (var5 > 0) {
            var0.skipBytes(var5);
         }

         try {
            return new Decimal(var1, var2, var3, var6);
         } catch (NumberFormatException var9) {
            return new Decimal();
         }
      }
   }
}
