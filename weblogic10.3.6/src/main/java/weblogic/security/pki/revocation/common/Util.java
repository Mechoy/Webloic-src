package weblogic.security.pki.revocation.common;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

final class Util {
   private static final Locale FILE_EXTENSION_LOCALE;
   private static final String CRL_FILE_EXTENSION = ".crl";
   public static final CrlFilesOnlyFilter CRL_FILES_ONLY_FILTER;

   public static void checkNotNull(String var0, Object var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Expected non-null \"" + var0 + "\".");
      }
   }

   public static void checkRange(String var0, Long var1, Long var2, Long var3) {
      if (null != var1) {
         if (null != var2 || null != var3) {
            if (null != var2 && var1 < var2) {
               throw new IllegalArgumentException("Value " + formatValueName(var0) + "minimum is " + var2 + ", was " + var1 + ".");
            } else if (null != var3 && var1 > var3) {
               throw new IllegalArgumentException("Value " + formatValueName(var0) + "maximum is " + var3 + ", was " + var1 + ".");
            }
         }
      }
   }

   public static void checkTimeTolerance(int var0) {
      checkRange("timeTolerance", (long)var0, 0L, (Long)null);
   }

   public static void checkRefreshPeriodPercent(int var0) {
      checkRange("refreshPeriodPercent", (long)var0, 1L, 100L);
   }

   public static void backgroundTaskSleep() {
      try {
         Thread.currentThread();
         Thread.sleep(5000L);
      } catch (InterruptedException var1) {
      }

   }

   public static byte[] readAll(InputStream var0) throws IOException {
      checkNotNull("InputStream", var0);
      BufferedInputStream var1 = new BufferedInputStream(var0);
      ByteArrayOutputStream var2 = new ByteArrayOutputStream(2048);
      var2.reset();
      byte[] var4 = new byte[8192];

      int var3;
      while((var3 = var1.read(var4, 0, var4.length)) != -1) {
         var2.write(var4, 0, var3);
      }

      byte[] var5 = var2.toByteArray();
      return var5;
   }

   private static String formatValueName(String var0) {
      return null == var0 ? "" : var0 + " ";
   }

   static {
      FILE_EXTENSION_LOCALE = Locale.US;
      CRL_FILES_ONLY_FILTER = new CrlFilesOnlyFilter();
   }

   private static final class CrlFilesOnlyFilter implements FileFilter {
      private CrlFilesOnlyFilter() {
      }

      public boolean accept(File var1) {
         if (var1 != null && !var1.isDirectory()) {
            String var2 = var1.getName();
            if (null != var2) {
               String var3 = var2.toLowerCase(Util.FILE_EXTENSION_LOCALE);
               return var3.endsWith(".crl");
            }
         }

         return false;
      }

      // $FF: synthetic method
      CrlFilesOnlyFilter(Object var1) {
         this();
      }
   }
}
