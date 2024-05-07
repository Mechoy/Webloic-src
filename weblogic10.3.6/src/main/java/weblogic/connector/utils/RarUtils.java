package weblogic.connector.utils;

import java.io.File;

public final class RarUtils {
   public static boolean isRar(File var0) {
      if (!var0.isDirectory()) {
         return var0.getName().endsWith(".rar");
      } else {
         return (new File(var0, "META-INF" + File.separator + "ra.xml")).exists() || (new File(var0, "META-INF" + File.separator + "weblogic-ra.xml")).exists();
      }
   }
}
