package weblogic.servlet.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import weblogic.utils.Hex;

final class ProxyUtils {
   static final int readChunkSize(InputStream var0) throws IOException {
      StringBuilder var1 = new StringBuilder();

      int var2;
      int var3;
      while((var2 = var0.read()) != -1 && (var2 != 13 || (var2 = var0.read()) != 10)) {
         var3 = (char)var2;
         if (Hex.isHexChar(var3)) {
            var1.append((char)var3);
         }
      }

      var3 = 0;
      if (var1.length() > 0) {
         var3 = Integer.parseInt(var1.toString(), 16);
         if (var3 == 0) {
            var0.read();
            var0.read();
         }
      }

      return var3;
   }

   static final String readHTTPHeader(PushbackInputStream var0) throws IOException {
      char[] var1 = new char[128];
      char[] var2 = new char[128];
      int var3 = var1.length;
      int var4 = 0;
      int var5 = 0;
      boolean var6 = false;

      while(!var6) {
         switch (var5 = var0.read()) {
            case -1:
            case 10:
               var6 = true;
               break;
            case 13:
               int var7 = var0.read();
               if (var7 != 10 && var7 != -1) {
                  var0.unread(var7);
               }

               var6 = true;
               break;
            default:
               --var3;
               if (var3 < 0) {
                  var1 = new char[var4 + 128];
                  var3 = var1.length - var4 - 1;
                  System.arraycopy(var2, 0, var1, 0, var4);
                  var2 = var1;
               }

               var1[var4++] = (char)var5;
         }
      }

      if (var5 == -1 && var4 == 0) {
         return null;
      } else if (var4 == 0) {
         return new String();
      } else {
         return new String(var1, 0, var4);
      }
   }
}
