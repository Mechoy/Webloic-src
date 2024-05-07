package weblogic.application.utils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import weblogic.utils.jars.VirtualJarFile;

public final class IOUtils {
   private IOUtils() {
   }

   public static void forceClose(VirtualJarFile var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (IOException var2) {
         }
      }

   }

   public static void forceClose(VirtualJarFile[] var0) {
      if (var0 != null) {
         for(int var1 = 0; var1 < var0.length; ++var1) {
            forceClose(var0[var1]);
         }
      }

   }

   public static void forceClose(OutputStream var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (IOException var2) {
         }
      }

   }

   public static File checkCreateParent(File var0) throws IOException {
      if (var0 != null) {
         File var1 = var0.getParentFile();
         if (!var1.exists()) {
            var1.mkdirs();
         }
      }

      return var0;
   }
}
