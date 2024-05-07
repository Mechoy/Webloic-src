package weblogic.diagnostics.utils;

import java.util.Date;

public abstract class DateUtils {
   public static final long NANOS_PER_MILLI = 1000000L;
   public static final long NANOS_PER_SEC = 1000000000L;

   public static String nanoDateToString(long var0) {
      return nanoDateToString(var0, false);
   }

   public static String nanoDateToString(long var0, boolean var2) {
      long var3 = var0 / 1000000000L;
      long var5 = var0 / 1000000L;
      long var7 = var0 - var3 * 1000000000L;
      String var9 = "" + new Date(var5);
      String var10 = var9.substring(0, 19) + ":" + var7 + var9.substring(19);
      if (var2) {
         var10 = var10 + " [" + var0 + "]";
      }

      return var10;
   }
}
