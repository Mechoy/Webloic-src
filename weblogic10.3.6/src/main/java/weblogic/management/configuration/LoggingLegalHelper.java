package weblogic.management.configuration;

import com.bea.logging.LoggingConfigValidator;

public class LoggingLegalHelper {
   /** @deprecated */
   public static boolean isLogStartTimeValid(String var0, String var1) {
      return LoggingConfigValidator.isLogStartTimeValid(var0, var1);
   }

   public static void validateLogTimeString(String var0) throws IllegalArgumentException {
      if (var0 != null && !var0.equals("")) {
         if (!isLogStartTimeValid("H:mm", var0)) {
            throw new IllegalArgumentException("Illegal time string: " + var0);
         }
      } else {
         String var1 = "LogTimeString can't be null or empty string";
         throw new IllegalArgumentException(var1);
      }
   }

   public static void validateWebServerLogRotationTimeBegin(String var0) throws IllegalArgumentException {
      if (var0 != null && !var0.equals("")) {
         if (!isLogStartTimeValid("MM-dd-yyyy-k:mm:ss", var0) && !isLogStartTimeValid("H:mm", var0)) {
            throw new IllegalArgumentException("Illegal time string: " + var0);
         }
      }
   }
}
