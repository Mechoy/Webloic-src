package weblogic.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class PEMInputStream extends FilterInputStream {
   private static final char[] pem_array = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
   private static final byte[] pem_convert_array = new byte[256];
   static final int EOF = -1;

   public PEMInputStream(InputStream var1) throws IOException {
      super((InputStream)null);
      if (var1.available() == 0) {
         throw new IOException(SecurityLogger.getZeroLengthPemInputStream());
      } else {
         byte[] var2 = new byte[45];

         int var3;
         for(var3 = 0; var3 < var2.length; var3 = decodeAtom(var1, var2, var3) == 3 ? var3 + 3 : 0) {
         }

         ByteArrayOutputStream var4 = new ByteArrayOutputStream();
         var4.write(var2, 0, var3);
         int var5 = 0;

         while(var5 <= 3) {
            int var6 = decodeAtom(var1, var2, 0);
            if (var6 > 0) {
               var4.write(var2, 0, var6);
               if (var6 < 3) {
                  break;
               }

               var5 = 0;
            } else {
               if (var6 == -1) {
                  break;
               }

               ++var5;
            }
         }

         this.in = new ByteArrayInputStream(var4.toByteArray());
      }
   }

   static int decodeAtom(InputStream var0, byte[] var1, int var2) throws IOException {
      int var3 = var0.read();
      if (var3 < 0) {
         return -1;
      } else {
         byte var4 = pem_convert_array[var3];
         if (var4 < 0) {
            return 0;
         } else {
            var1[var2] = (byte)(var4 << 2);
            var3 = var0.read();
            if (var3 < 0) {
               return -1;
            } else {
               var4 = pem_convert_array[var3];
               if (var4 < 0) {
                  return 0;
               } else {
                  var1[var2] = (byte)(var1[var2] | var4 >> 4);
                  var1[var2 + 1] = (byte)(var4 << 4);
                  var3 = var0.read();
                  if (var3 >= 0 && var3 != 61) {
                     var4 = pem_convert_array[var3];
                     if (var4 < 0) {
                        return 0;
                     } else {
                        var1[var2 + 1] = (byte)(var1[var2 + 1] | var4 >> 2);
                        var1[var2 + 2] = (byte)(var4 << 6);
                        var3 = var0.read();
                        if (var3 >= 0 && var3 != 61) {
                           var4 = pem_convert_array[var3];
                           if (var4 < 0) {
                              return 0;
                           } else {
                              var1[var2 + 2] |= var4;
                              return 3;
                           }
                        } else {
                           return 2;
                        }
                     }
                  } else {
                     return 1;
                  }
               }
            }
         }
      }
   }

   static {
      int var0;
      for(var0 = 0; var0 < 255; ++var0) {
         pem_convert_array[var0] = -1;
      }

      for(var0 = 0; var0 < pem_array.length; ++var0) {
         pem_convert_array[pem_array[var0]] = (byte)var0;
      }

   }
}
