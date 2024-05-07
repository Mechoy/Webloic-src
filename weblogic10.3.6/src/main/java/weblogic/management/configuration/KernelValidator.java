package weblogic.management.configuration;

public class KernelValidator {
   public static void validateMaxCommMessageSize(int var0) {
      if (var0 != -1 && (var0 < 4096 || var0 > 2000000000)) {
         throw new IllegalArgumentException("Illegal value for MaxCOMMMessageSize: " + var0);
      }
   }

   public static void validateMaxHTTPMessageSize(int var0) {
      if (var0 != -1 && (var0 < 4096 || var0 > 2000000000)) {
         throw new IllegalArgumentException("Illegal value for MaxHTTPMessageSize: " + var0);
      }
   }

   public static void validateMaxIIOPMessageSize(int var0) {
      if (var0 != -1 && (var0 < 4096 || var0 > 2000000000)) {
         throw new IllegalArgumentException("Illegal value for MaxIIOPMessageSize: " + var0);
      }
   }

   public static void validateMaxT3MessageSize(int var0) {
      if (var0 != -1 && (var0 < 4096 || var0 > 2000000000)) {
         throw new IllegalArgumentException("Illegal value for MaxT3MessageSize: " + var0);
      }
   }
}
