package weblogic.deploy.api.internal.utils;

import java.util.Locale;
import weblogic.deploy.api.internal.SPIDeployerLogger;

public class LocaleManager {
   private static Locale defaultLocale = Locale.getDefault();
   private static Locale[] supportedLocales = null;

   private static void setupLocale(Locale var0) {
      Locale.setDefault(var0);
   }

   public static void setLocale(Locale var0) throws UnsupportedOperationException {
      if (isLocaleSupported(var0)) {
         setupLocale(var0);
      } else {
         throw new UnsupportedOperationException(SPIDeployerLogger.unsupportedLocale(var0.toString()));
      }
   }

   public static boolean isLocaleSupported(Locale var0) {
      Locale[] var1 = getSupportedLocales();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2].equals(var0)) {
            return true;
         }
      }

      return false;
   }

   public static Locale getDefaultLocale() {
      return defaultLocale;
   }

   public static Locale getCurrentLocale() {
      return Locale.getDefault();
   }

   public static Locale[] getSupportedLocales() {
      if (supportedLocales == null) {
         supportedLocales = Locale.getAvailableLocales();
      }

      return supportedLocales;
   }

   static {
      setupLocale(defaultLocale);
   }
}
