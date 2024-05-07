package weblogic.jms.utils;

import java.util.Locale;

public class Simple {
   public static String getenv(String var0) {
      String var1 = System.getProperty(var0);
      if (var1 != null) {
         return var1;
      } else {
         try {
            var1 = System.getenv(var0.replace('.', '_').toUpperCase(Locale.ENGLISH));
         } catch (SecurityException var3) {
         }

         return var1;
      }
   }
}
