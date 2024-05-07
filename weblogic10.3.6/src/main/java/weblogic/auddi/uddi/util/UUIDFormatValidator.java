package weblogic.auddi.uddi.util;

import java.util.StringTokenizer;

public class UUIDFormatValidator {
   private static boolean isValid(String var0) {
      int var1 = 0;

      for(int var2 = var0.length(); var1 < var2; ++var1) {
         Character var3 = new Character(var0.charAt(var1));
         if (!Character.isLetterOrDigit(var3)) {
            return false;
         }
      }

      return true;
   }

   private static boolean isWellFormed(String var0, int var1, boolean var2) {
      if (var2) {
         return var0.equalsIgnoreCase("uuid:");
      } else {
         switch (var1) {
            case 1:
               if (var0.length() != 8) {
                  return false;
               }

               return isValid(var0);
            case 2:
            case 3:
            case 4:
               if (var0.length() != 4) {
                  return false;
               }

               return isValid(var0);
            case 5:
               if (var0.length() != 12) {
                  return false;
               }

               return isValid(var0);
            default:
               return false;
         }
      }
   }

   public static boolean validate(String var0) {
      return validate(var0, false);
   }

   public static boolean validate(String var0, boolean var1) {
      if (var1 && var0.length() != 41) {
         return false;
      } else if (!var1 && var0.length() != 36) {
         return false;
      } else {
         String var2 = "";
         if (var1) {
            String var3 = var0.substring(0, 5);
            var0 = var0.substring(5);
            if (!isWellFormed(var3, 0, var1)) {
               return false;
            }
         }

         int var5 = 0;
         StringTokenizer var4 = new StringTokenizer(var0, "-", false);

         do {
            if (!var4.hasMoreTokens()) {
               if (var5 != 5) {
                  return false;
               }

               return true;
            }

            ++var5;
            var2 = var4.nextToken();
         } while(isWellFormed(var2, var5, false));

         return false;
      }
   }
}
