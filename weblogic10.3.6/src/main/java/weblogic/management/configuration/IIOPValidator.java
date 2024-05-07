package weblogic.management.configuration;

public class IIOPValidator {
   public static void validateMaxMessageSize(int var0) {
      if (var0 != -1 && (var0 < 4096 || var0 > 2000000000)) {
         throw new IllegalArgumentException("Illegal value for MaxMessageSize: " + var0);
      }
   }
}
