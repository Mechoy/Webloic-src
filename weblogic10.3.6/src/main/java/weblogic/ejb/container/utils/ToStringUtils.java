package weblogic.ejb.container.utils;

import weblogic.utils.AssertionError;
import weblogic.utils.Debug;

public final class ToStringUtils {
   public static String isoToString(Integer var0) {
      return isoToString(var0 == null ? -1 : var0);
   }

   public static String isoToString(int var0) {
      switch (var0) {
         case -1:
            return "No Isolation Level Set";
         case 0:
            return "NONE";
         case 1:
            return "READ_UNCOMMITTED";
         case 2:
            return "READ_COMMITED";
         case 3:
         case 5:
         case 6:
         case 7:
         default:
            throw new AssertionError("Bad iso level: " + var0);
         case 4:
            return "REPEATABLE_READ";
         case 8:
            return "SERIALIZABLE";
      }
   }

   public static String txAttributeToString(int var0) {
      switch (var0) {
         case 0:
            return "TX_NOT_SUPPORTED";
         case 1:
            return "TX_REQUIRED";
         case 2:
            return "TX_SUPPORTS";
         case 3:
            return "TX_REQUIRES_NEW";
         case 4:
            return "TX_MANDATORY";
         case 5:
            return "TX_NEVER";
         default:
            Debug.stackdump("Bad tx attribute: " + var0);
            throw new AssertionError("Bad tx attribute: " + var0);
      }
   }

   public static String escapedQuotesToString(String var0) {
      int var1 = 0;

      while(true) {
         var1 = var0.indexOf("'", var1);
         if (var1 == -1) {
            return var0;
         }

         String var2;
         if (var1 > 0) {
            var2 = var0.substring(0, var1) + "''";
         } else {
            var2 = "''";
         }

         var0 = var1 < var0.length() - 1 ? var2 + var0.substring(var1 + 1) : var2;
         var1 += 2;
      }
   }

   public static String escapeBackSlash(String var0) {
      int var1 = 0;
      int var2 = var0.indexOf(92, var1);
      if (var2 == -1) {
         return var0;
      } else {
         StringBuffer var3 = new StringBuffer();

         int var4;
         for(var4 = var0.length() - 1; var2 != -1; var2 = var0.indexOf(92, var1)) {
            var3.append(var0.substring(var1, var2));
            var3.append("\\\\");
            var1 = var2 + 1;
            if (var1 > var4) {
               break;
            }
         }

         if (var1 <= var4) {
            var3.append(var0.substring(var1, var4 + 1));
         }

         return var3.toString();
      }
   }

   public static String chop(String var0) {
      if (var0 == null) {
         return var0;
      } else if (var0.length() == 0) {
         return "";
      } else {
         for(int var1 = var0.length() - 1; var0.charAt(var1) == ' '; --var1) {
            var0 = var0.substring(0, var1);
         }

         return var0;
      }
   }

   public static String adjust(String var0, String var1) {
      int var2 = var1.length();
      int var3 = var0.length();
      if (var3 == var2) {
         return var0;
      } else {
         if (var3 < var2) {
            for(int var4 = 0; var4 < var2 - var3; ++var4) {
               var0 = var0.concat(" ");
            }
         } else {
            var0 = var0.substring(0, var2);
         }

         return var0;
      }
   }
}
