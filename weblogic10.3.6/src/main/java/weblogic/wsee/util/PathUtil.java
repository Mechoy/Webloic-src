package weblogic.wsee.util;

public class PathUtil {
   public static String normalizePath(String var0) {
      String var1 = var0.replace('\\', '/');
      if (!var1.startsWith("/")) {
         var1 = "/" + var1;
      }

      return var1;
   }
}
