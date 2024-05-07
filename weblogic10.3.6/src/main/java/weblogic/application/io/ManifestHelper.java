package weblogic.application.io;

import java.io.File;
import java.util.ArrayList;

public final class ManifestHelper {
   private ManifestHelper() {
   }

   static String[] getMFClassPathElements(File var0) {
      String[] var1 = null;
      ManifestFinder.ClassPathFinder var2 = null;

      try {
         var2 = new ManifestFinder.ClassPathFinder(var0);
         var1 = var2.getPathElements();
      } finally {
         var2.close();
      }

      return var1;
   }

   static File[] getExistingMFClassPathElements(File var0) {
      String[] var1 = getMFClassPathElements(var0);
      ArrayList var2 = new ArrayList(var1.length);

      for(int var3 = 0; var3 < var1.length; ++var3) {
         File var4 = new File(var1[var3]);
         if (var4.exists()) {
            var2.add(var4);
         }
      }

      return (File[])((File[])var2.toArray(new File[var2.size()]));
   }
}
