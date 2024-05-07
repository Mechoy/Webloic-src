package weblogic.application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public final class SplitDirectoryUtils implements SplitDirectoryConstants {
   public static void generatePropFile(File var0, File var1) throws IOException {
      String var2 = var0.getAbsolutePath();
      if ("\\".equals(File.separator)) {
         var2 = var2.replaceAll("\\\\", "/");
      }

      Properties var3 = new Properties();
      var3.setProperty("bea.srcdir", var2);
      FileOutputStream var4 = null;

      try {
         var4 = new FileOutputStream(new File(var1, ".beabuild.txt"));
         var3.store(var4, "");
      } finally {
         if (var4 != null) {
            try {
               var4.close();
            } catch (Exception var11) {
            }
         }

      }

   }
}
