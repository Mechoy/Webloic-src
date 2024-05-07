package weblogic.wsee.util;

import java.io.File;
import java.util.Locale;

public class StringUtil {
   private StringUtil() {
   }

   public static boolean isEmpty(String var0) {
      return var0 == null || var0.trim().length() == 0;
   }

   public static String getPackage(String var0) {
      int var1 = var0.lastIndexOf(46);
      return var1 <= 0 ? "" : var0.substring(0, var1);
   }

   public static String getSimpleClassName(String var0) {
      return var0.substring(var0.lastIndexOf(46) + 1);
   }

   public static String getRelativeSourcePath(String var0) {
      if (isEmpty(var0)) {
         throw new IllegalArgumentException("fqn is empty");
      } else {
         String var1 = var0.replace(".", File.separator);
         int var2 = var1.lastIndexOf(File.separator);
         if (var2 < 0) {
            var2 = 0;
         }

         int var3 = var1.indexOf("$", var2);
         if (var3 > 0) {
            var1 = var1.substring(0, var3);
         }

         return var1 + ".java";
      }
   }

   public static boolean isLowerCase(String var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("String must not be null.");
      } else {
         return var0.toLowerCase(Locale.ENGLISH).equals(var0);
      }
   }
}
