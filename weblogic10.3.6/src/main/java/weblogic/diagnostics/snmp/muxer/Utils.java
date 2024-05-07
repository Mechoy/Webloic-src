package weblogic.diagnostics.snmp.muxer;

import weblogic.diagnostics.debug.DebugLogger;

class Utils {
   static void debug(DebugLogger var0, String var1, String var2, String var3) {
      var0.debug(var1 + "." + var2 + "(): " + var3);
   }

   private static int byteArrayToInt(byte[] var0, int var1, int var2) {
      int var3 = 0;

      for(int var4 = 0; var4 < var2; ++var4) {
         int var5 = (var2 - 1 - var4) * 8;
         var3 += (var0[var4 + var1] & 255) << var5;
      }

      return var3;
   }
}
