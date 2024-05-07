package weblogic.management.security;

import javax.management.InvalidAttributeValueException;

public class RDBMSSecurityStoreValidator {
   private static final String STRING_DELIMITER = ",";
   private static final String PROPERTY_DELIMITER = "=";

   public static void validateProperties(String var0) throws InvalidAttributeValueException {
      if (var0 != null) {
         boolean var1 = true;
         String[] var2;
         if (var0.indexOf(",") < 0) {
            var2 = new String[]{var0};
         } else {
            var2 = var0.split(",");
         }

         for(int var3 = 0; var3 < var2.length; ++var3) {
            String var4 = var2[var3].trim();
            if (var4.length() > 0) {
               int var5 = var4.indexOf("=");
               if (var5 < 0) {
                  var1 = false;
                  break;
               }

               String var6 = var4.substring(0, var5).trim();
               String var7 = var4.substring(var5 + 1, var4.length()).trim();
               if (var6.length() < 1 || var7.length() < 1) {
                  var1 = false;
                  break;
               }

               if (!var6.matches("[a-zA-Z0-9._]+")) {
                  var1 = false;
                  break;
               }
            }
         }

         if (!var1) {
            throw new InvalidAttributeValueException("Illegal value for Properties: " + var0);
         }
      }
   }
}
