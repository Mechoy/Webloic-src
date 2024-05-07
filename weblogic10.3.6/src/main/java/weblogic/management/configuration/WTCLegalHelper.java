package weblogic.management.configuration;

import java.util.StringTokenizer;
import javax.management.InvalidAttributeValueException;
import weblogic.logging.Loggable;
import weblogic.utils.net.InetAddressHelper;
import weblogic.wtc.WTCLogger;

public final class WTCLegalHelper {
   public static boolean isMinLteMax(WTCLocalTuxDomMBean var0, String var1) throws InvalidAttributeValueException {
      String var2 = var0.getMaxEncryptBits();
      if (Integer.parseInt(var1, 10) > Integer.parseInt(var2, 10)) {
         String var3 = var0.getAccessPointId();
         Loggable var4 = WTCLogger.logMinEncryptBitsGreaterThanMaxEncryptBitsLoggable("Local", var3);
         throw new InvalidAttributeValueException(var4.getMessage());
      } else {
         return true;
      }
   }

   public static boolean isMinLteMax(WTCRemoteTuxDomMBean var0, String var1) throws InvalidAttributeValueException {
      String var2 = var0.getMaxEncryptBits();
      if (Integer.parseInt(var1, 10) > Integer.parseInt(var2, 10)) {
         String var3 = var0.getAccessPointId();
         Loggable var4 = WTCLogger.logMinEncryptBitsGreaterThanMaxEncryptBitsLoggable("Remote", var3);
         throw new InvalidAttributeValueException(var4.getMessage());
      } else {
         return true;
      }
   }

   public static boolean isMaxGteMin(WTCLocalTuxDomMBean var0, String var1) throws InvalidAttributeValueException {
      String var2 = var0.getMinEncryptBits();
      if (Integer.parseInt(var1, 10) < Integer.parseInt(var2, 10)) {
         String var3 = var0.getAccessPointId();
         Loggable var4 = WTCLogger.logMinEncryptBitsGreaterThanMaxEncryptBitsLoggable("Local", var3);
         throw new InvalidAttributeValueException(var4.getMessage());
      } else {
         return true;
      }
   }

   public static boolean isMaxGteMin(WTCRemoteTuxDomMBean var0, String var1) throws InvalidAttributeValueException {
      String var2 = var0.getMinEncryptBits();
      if (Integer.parseInt(var1, 10) < Integer.parseInt(var2, 10)) {
         String var3 = var0.getAccessPointId();
         Loggable var4 = WTCLogger.logMinEncryptBitsGreaterThanMaxEncryptBitsLoggable("Remote", var3);
         throw new InvalidAttributeValueException(var4.getMessage());
      } else {
         return true;
      }
   }

   public static boolean checkNWAddrFormat(String var0, String var1) throws InvalidAttributeValueException {
      if (var1 != null && var1.toLowerCase().startsWith("sdp://")) {
         var1 = var1.substring(4);
      }

      if (InetAddressHelper.isIPV6Address(var1)) {
         return true;
      } else {
         boolean var2 = true;
         int var3 = 0;
         if (var1 != null) {
            var3 = var1.indexOf(58);
         }

         if (var1 == null || !var1.startsWith("//") || var3 < 3 || var3 + 1 >= var1.length()) {
            var2 = false;
         }

         if (var2) {
            String var4 = var1.substring(2, var3);
            if (Character.isDigit(var4.charAt(0))) {
               int var5 = 0;
               boolean var6 = false;
               String var7 = null;

               for(StringTokenizer var8 = new StringTokenizer(var4, "."); var8.hasMoreTokens(); ++var5) {
                  var7 = var8.nextToken();

                  try {
                     int var13 = Integer.parseInt(var7, 10);
                     if (var13 < 0 || var13 > 255) {
                        var2 = false;
                        break;
                     }
                  } catch (NumberFormatException var11) {
                     var2 = false;
                     break;
                  }
               }

               if (var5 != 4) {
                  var2 = false;
               }
            }
         }

         if (var2) {
            try {
               if (Integer.parseInt(var1.substring(var3 + 1), 10) < 0) {
                  var2 = false;
               }
            } catch (NumberFormatException var10) {
               var2 = false;
            }
         }

         if (!var2) {
            Loggable var12 = WTCLogger.logInvalidMBeanAttrLoggable("NWAddr", var0);
            throw new InvalidAttributeValueException(var12.getMessage());
         } else {
            return true;
         }
      }
   }

   public static boolean isNWAddrFormat(String var0, String var1) throws InvalidAttributeValueException {
      if (var1 == null) {
         return checkNWAddrFormat(var0, var1);
      } else {
         StringTokenizer var2 = new StringTokenizer(var1, ",");
         int var3 = var2.countTokens();
         String[] var4 = new String[var3];

         for(int var5 = 0; var5 < var3; ++var5) {
            var4[var5] = var2.nextToken();
            if (!checkNWAddrFormat(var0, var4[var5])) {
               return false;
            }
         }

         return true;
      }
   }

   public static void validateWTCLocalTuxDom(WTCLocalTuxDomMBean var0) throws IllegalArgumentException {
      String var1 = var0.getName();
      String var2 = var0.getNWAddr();
      String var3 = var0.getMinEncryptBits();
      String var4 = var0.getMaxEncryptBits();

      try {
         if (!isNWAddrFormat(var1, var2)) {
            throw new IllegalArgumentException("Invalid value for NWAddr: " + var2);
         } else {
            String var5;
            if (!isMinLteMax(var0, var3)) {
               var5 = "Invalid value for MinEncryptBits: " + var3;
               throw new IllegalArgumentException(var5);
            } else if (!isMaxGteMin(var0, var4)) {
               var5 = "Invalid value for MaxEncryptBits: " + var4;
               throw new IllegalArgumentException(var5);
            }
         }
      } catch (InvalidAttributeValueException var6) {
         throw new IllegalArgumentException(var6.getMessage());
      }
   }

   public static void validateWTCRemoteTuxDom(WTCRemoteTuxDomMBean var0) throws IllegalArgumentException {
      String var1 = var0.getName();
      String var2 = var0.getNWAddr();
      String var3 = var0.getMinEncryptBits();
      String var4 = var0.getMaxEncryptBits();

      try {
         if (!isNWAddrFormat(var1, var2)) {
            throw new IllegalArgumentException("Invalid value for NWAddr: " + var2);
         } else {
            String var5;
            if (!isMinLteMax(var0, var3)) {
               var5 = "Invalid value for MinEncryptBits: " + var3;
               throw new IllegalArgumentException(var5);
            } else if (!isMaxGteMin(var0, var4)) {
               var5 = "Invalid value for MaxEncryptBits: " + var4;
               throw new IllegalArgumentException(var5);
            }
         }
      } catch (InvalidAttributeValueException var6) {
         throw new IllegalArgumentException(var6.getMessage());
      }
   }
}
