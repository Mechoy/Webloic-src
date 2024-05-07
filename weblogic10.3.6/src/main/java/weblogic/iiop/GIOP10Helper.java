package weblogic.iiop;

import java.io.UnsupportedEncodingException;
import org.omg.CORBA.CODESET_INCOMPATIBLE;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;

final class GIOP10Helper {
   static void write_wstring(String var0, OutputStream var1, int var2, boolean var3) {
      if (var0 == null) {
         throw new MARSHAL("null value passes to write_wstring(String)");
      } else {
         byte[] var4 = null;

         try {
            switch (var2) {
               case 65792:
                  if (var3) {
                     var4 = var0.getBytes("utf-16le");
                  } else {
                     var4 = var0.getBytes("utf-16be");
                  }
                  break;
               case 65801:
                  var4 = var0.getBytes("utf-16");
                  break;
               case 83951617:
                  var4 = var0.getBytes("utf-8");
            }
         } catch (UnsupportedEncodingException var6) {
            throw new CODESET_INCOMPATIBLE(var6.getMessage());
         }

         int var5 = var4.length / 2 + 1;
         var1.write_ulong(var5);
         var1.write_octet_array(var4, 0, var4.length);
         var1.write_octet((byte)0);
         var1.write_octet((byte)0);
      }
   }

   static final String read_wstring(InputStream var0, int var1, boolean var2, int var3) {
      var3 *= 2;
      var3 -= 2;
      byte[] var4 = new byte[var3];
      var0.read_octet_array(var4, 0, var4.length);
      String var5 = null;

      try {
         switch (var1) {
            case 65792:
               if (var2) {
                  var5 = new String(var4, "utf-16le");
               } else {
                  var5 = new String(var4, "utf-16be");
               }
               break;
            case 65801:
               if (var3 <= 1 || (var4[0] != -1 || var4[1] != -2) && (var4[0] != -2 || var4[1] != -1)) {
                  var5 = new String(var4, "utf-16be");
               } else {
                  var5 = new String(var4, "utf-16");
               }
               break;
            default:
               throw new CODESET_INCOMPATIBLE();
         }
      } catch (UnsupportedEncodingException var7) {
         throw new CODESET_INCOMPATIBLE(var7.getMessage());
      }

      var0.read_octet();
      var0.read_octet();
      return var5;
   }
}
