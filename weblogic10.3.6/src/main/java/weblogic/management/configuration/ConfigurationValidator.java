package weblogic.management.configuration;

import java.util.Arrays;

public class ConfigurationValidator {
   static char[] invalid_chars = new char[]{':', ',', '=', '*', '?', '%'};

   public static void validateName(String var0) throws IllegalArgumentException {
      if (var0 != null && var0.length() != 0) {
         char[] var1 = var0.toCharArray();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (Arrays.binarySearch(invalid_chars, var1[var2]) >= 0) {
               throw new IllegalArgumentException("Name '" + var0 + "' contains illegal character '" + var1[var2] + "'");
            }
         }

      } else {
         throw new IllegalArgumentException("Name may not be null or empty string");
      }
   }

   public static void validateClassName(String var0) throws IllegalArgumentException {
      if (var0 != null) {
         if (var0.endsWith(".class")) {
            throw new IllegalArgumentException("Invalid class name: " + var0 + " - Classnames may not end with '.class'");
         }
      }
   }

   static {
      Arrays.sort(invalid_chars);
   }
}
