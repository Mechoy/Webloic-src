package weblogic.connector.deploy;

import java.util.List;
import weblogic.utils.PlatformConstants;

public class ClassPathUtil {
   public static String computeClasspath(String var0, List<String> var1) {
      return computeClasspath(var0, (String[])var1.toArray(new String[var1.size()]));
   }

   public static String computeClasspath(String var0, String[] var1) {
      boolean var2 = true;
      StringBuilder var3 = new StringBuilder();
      String[] var4 = var1;
      int var5 = var1.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String var7 = var4[var6];
         if (var7.endsWith(".jar")) {
            if (!var2) {
               var3.append(PlatformConstants.PATH_SEP);
            } else {
               var2 = false;
            }

            var3.append(var0).append(PlatformConstants.FILE_SEP).append(var7);
         }
      }

      return var3.toString();
   }
}
