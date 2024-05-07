package weblogic.xml.jaxp;

import java.util.MissingResourceException;

public class Utils {
   public static Object getDelegate(String[] var0) {
      Object var1 = null;
      Object var2 = null;
      String var3 = "";
      String var4 = "";

      for(int var5 = 0; var5 < var0.length; ++var5) {
         try {
            var1 = Class.forName(var0[var5]).newInstance();
         } catch (Exception var7) {
            var3 = var3 + var7.toString() + "\n";
            var4 = var4 + var0[var5] + "\n";
         }

         if (var1 != null) {
            break;
         }
      }

      if (var1 == null) {
         throw new MissingResourceException("Could not instantiate factory delegate, got exception(s):\n" + var3, "class", var4);
      } else {
         return var1;
      }
   }
}
