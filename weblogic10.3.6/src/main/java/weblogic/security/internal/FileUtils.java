package weblogic.security.internal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import weblogic.security.SecurityLogger;

public final class FileUtils {
   private static final boolean DEBUG = false;

   private static void debug(String var0) {
   }

   private static void error(String var0, Throwable var1) throws FileUtilsException {
      throw new FileUtilsException(var0, var1);
   }

   private static void error(String var0) throws FileUtilsException {
      throw new FileUtilsException(var0);
   }

   private static void writeFile(File var0, FileWriter var1) throws FileUtilsException {
      String var2 = (String)null;

      try {
         boolean var3 = false;
         FileOutputStream var4 = new FileOutputStream(var0);

         try {
            var1.write(var4);
            var3 = true;
         } finally {
            try {
               var4.close();
            } catch (IOException var13) {
            }

            if (!var3) {
               var0.delete();
            }

         }
      } catch (FileNotFoundException var15) {
         error(SecurityLogger.getErrorCreatingFile(var0.getAbsolutePath()), var15);
      } catch (IOException var16) {
         error(SecurityLogger.getErrorWritingRealmContents(var0.getAbsolutePath()), var16);
      }

   }

   public static void replace(String var0, FileWriter var1) throws FileUtilsException {
      String var2 = (String)null;
      File var3 = new File(var0);
      String var4 = var3.getName();
      File var5 = var3.getAbsoluteFile().getParentFile();
      File var6 = null;

      try {
         var6 = File.createTempFile(var4, ".new", var5);
      } catch (IOException var10) {
         error(SecurityLogger.getCouldNotCreateTempFileNew(var4, var5.getAbsolutePath()), var10);
      }

      writeFile(var6, var1);
      File var7 = null;
      if (var3.exists()) {
         try {
            var7 = File.createTempFile(var4, ".old", var5);
         } catch (IOException var9) {
            error(SecurityLogger.getCouldNotCreateTempFileOld(var4, var5.getAbsolutePath()), var9);
         }

         if (!var7.delete()) {
            error(SecurityLogger.getCouldNotClearTempFile(var7.getAbsolutePath()));
         }
      }

      if (var7 != null && !var3.renameTo(var7)) {
         error(SecurityLogger.getCouldNotRenameTempFile(var3.getAbsolutePath(), var7.getAbsolutePath()));
      }

      if (!var6.renameTo(var3)) {
         error(SecurityLogger.getCouldNotRenameTempFile(var6.getAbsolutePath(), var3.getAbsolutePath()));
      }

      if (var7 != null && !var7.delete()) {
         error(SecurityLogger.getCouldNotDeleteTempFile("Couldn't delete " + var7.getAbsolutePath()));
      }

   }
}
