package weblogic.management.configuration;

public class NetworkChannelValidator {
   public static void validateName(String var0) throws IllegalArgumentException {
      if (var0 != null && var0.trim().length() != 0) {
         if (var0.startsWith(".WL")) {
            throw new IllegalArgumentException("Name may not start with .WL");
         } else if (var0.equals("Default") || var0.equals("Administrator")) {
            throw new IllegalArgumentException("Name may not be 'Default' or 'Administrator'");
         }
      } else {
         throw new IllegalArgumentException("Name may not be null or empty string");
      }
   }
}
