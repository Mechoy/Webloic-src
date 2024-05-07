package weblogic.security.pki.revocation.common;

import java.io.File;
import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import javax.security.auth.x500.X500Principal;

final class CrlCacheUpdater {
   private static final long IMPORT_CRL_FROM_FILE_PERIOD_MILLIS = getTimerPeriodMillis("weblogic.security.pki.revocation.importCrlFromFilePeriodMillis", 60000L);
   private static final Object importCrlFromFileSync = new Object();
   private static volatile Timer importCrlFromFileTimer = null;
   private static final long DELETE_INVALID_CRL_FROM_CACHE_PERIOD_MILLIS = getTimerPeriodMillis("weblogic.security.pki.revocation.deleteInvalidCrlFromCachePeriodMillis", 300000L);
   private static final Object deleteInvalidCrlFromCacheSync = new Object();
   private static volatile Timer deleteInvalidCrlFromCacheTimer = null;

   static void startAllMaintenanceTasks(CrlCacheAccessor var0, AbstractCertRevocContext var1) {
      startImportCrlFromFile(var0, var1);
      startDeleteInvalidCrlFromCache(var0, var1);
   }

   static void cancelAllMaintenanceTasks(LogListener var0) {
      cancelImportCrlFromFile(var0);
      cancelDeleteInvalidCrlFromCache(var0);
   }

   static boolean isAllMaintenanceTasksActive() {
      boolean var0 = isImportCrlFromFileActive();
      var0 &= isDeleteInvalidCrlFromCacheActive();
      return var0;
   }

   static boolean updateCrlCacheFromDP(X509Certificate var0, CrlCacheAccessor var1, AbstractCertRevocContext var2) {
      Util.checkNotNull("certToCheck", var0);
      Util.checkNotNull("crlCacheAccessor", var1);
      Util.checkNotNull("AbstractCertRevocContext", var2);
      X500Principal var3 = var0.getIssuerX500Principal();
      Util.checkNotNull("certToCheck issuer", var3);
      long var4 = var2.getCrlDpDownloadTimeout(var3);
      if (var2.isLoggable(Level.FINEST)) {
         var2.log(Level.FINEST, "CrlDpDownloadTimeout={0}", var4);
      }

      long var6 = var4 * 1000L;
      URI var8 = var2.getCrlDpUrl(var3);
      if (var2.isLoggable(Level.FINEST)) {
         var2.log(Level.FINEST, "CrlDpUrl={0}", var8);
      }

      AbstractCertRevocContext.AttributeUsage var9 = var2.getCrlDpUrlUsage(var3);
      if (var2.isLoggable(Level.FINEST)) {
         var2.log(Level.FINEST, "CrlDpUrlUsage={0}", var9);
      }

      Object var11 = new Object();
      synchronized(var11) {
         DownloadCrlFromDpRunnable var10 = new DownloadCrlFromDpRunnable(var1, var11, var0, var2.getLogListener(), var8, var9, var6);
         var2.schedule(var10);
         long var13 = var6 <= 0L ? Long.MAX_VALUE : var6;
         long var15 = System.currentTimeMillis();
         long var17 = Long.MAX_VALUE - var15;

         for(long var19 = var17 <= var13 ? Long.MAX_VALUE : var15 + var13; var10.isRunning() && var13 > 0L; var13 = var19 - System.currentTimeMillis()) {
            try {
               var11.wait(var13);
            } catch (InterruptedException var23) {
            }
         }

         return var10.isCrlCacheUpdated();
      }
   }

   private static boolean isImportCrlFromFileActive() {
      return null != importCrlFromFileTimer;
   }

   private static boolean startImportCrlFromFile(CrlCacheAccessor var0, AbstractCertRevocContext var1) {
      if (null != importCrlFromFileTimer) {
         return true;
      } else {
         synchronized(importCrlFromFileSync) {
            if (null != importCrlFromFileTimer) {
               return true;
            } else {
               Util.checkNotNull("crlCacheAccessor", var0);
               Util.checkNotNull("AbstractCertRevocContext", var1);
               if (var1.isLoggable(Level.FINEST)) {
                  var1.log(Level.FINEST, "Attempting to start automatic checking for CRL files to import.");
               }

               try {
                  File var3 = var1.getCrlCacheImportDir();
                  ensureCrlCacheDir(var3);
                  String var4 = var3.getAbsolutePath();
                  if (var1.isLoggable(Level.FINEST)) {
                     var1.log(Level.FINEST, "CrlCacheImportDir=\"{0}\"", var4);
                  }

                  ImportCrlFromFileRunnable var5 = new ImportCrlFromFileRunnable(var3, var0, var1.getLogListener());
                  long var6 = IMPORT_CRL_FROM_FILE_PERIOD_MILLIS;
                  long var8 = IMPORT_CRL_FROM_FILE_PERIOD_MILLIS;
                  importCrlFromFileTimer = var1.scheduleWithFixedDelay(var5, var6, var8);
                  if (null == importCrlFromFileTimer) {
                     throw new IllegalStateException("Scheduler returned null importCrlFromFileTimer.");
                  }
               } catch (Exception var11) {
                  if (var1.isLoggable(Level.FINE)) {
                     var1.log(Level.FINE, var11, "Unable to start automatic checking for CRL files to import.");
                  }

                  return false;
               }

               if (var1.isLoggable(Level.FINEST)) {
                  var1.log(Level.FINEST, "Successfully started automatic checking for CRL files to import.");
               }

               return true;
            }
         }
      }
   }

   private static void cancelImportCrlFromFile(LogListener var0) {
      if (null != importCrlFromFileTimer) {
         synchronized(importCrlFromFileSync) {
            if (null != importCrlFromFileTimer) {
               if (null != var0 && var0.isLoggable(Level.FINEST)) {
                  var0.log(Level.FINEST, "Attempting to stop automatic checking for CRL files to import.");
               }

               try {
                  importCrlFromFileTimer.cancel();
                  importCrlFromFileTimer = null;
                  if (null != var0 && var0.isLoggable(Level.FINEST)) {
                     var0.log(Level.FINEST, "Successfully stopped automatic checking for CRL files to import.");
                  }
               } catch (Exception var4) {
                  if (null != var0 && var0.isLoggable(Level.FINE)) {
                     var0.log(Level.FINE, var4, "Error occured while stopping automatic checking for CRL files to import.");
                  }
               }

            }
         }
      }
   }

   private static boolean isDeleteInvalidCrlFromCacheActive() {
      return null != deleteInvalidCrlFromCacheTimer;
   }

   private static boolean startDeleteInvalidCrlFromCache(CrlCacheAccessor var0, AbstractCertRevocContext var1) {
      if (null != deleteInvalidCrlFromCacheTimer) {
         return true;
      } else {
         synchronized(deleteInvalidCrlFromCacheSync) {
            if (null != deleteInvalidCrlFromCacheTimer) {
               return true;
            } else {
               Util.checkNotNull("crlCacheAccessor", var0);
               Util.checkNotNull("AbstractCertRevocContext", var1);
               if (var1.isLoggable(Level.FINEST)) {
                  var1.log(Level.FINEST, "Attempting to start automatic deleting of invalid CRLs in cache.");
               }

               try {
                  File var3 = var1.getCrlCacheTypeFileDir();
                  ensureCrlCacheDir(var3);
                  String var4 = var3.getAbsolutePath();
                  if (var1.isLoggable(Level.FINEST)) {
                     var1.log(Level.FINEST, "CrlCacheTypeFileDir=\"{0}\"", var4);
                  }

                  DeleteInvalidCrlFromCacheRunnable var5 = new DeleteInvalidCrlFromCacheRunnable(var1, var3, var0);
                  long var6 = DELETE_INVALID_CRL_FROM_CACHE_PERIOD_MILLIS;
                  long var8 = DELETE_INVALID_CRL_FROM_CACHE_PERIOD_MILLIS;
                  deleteInvalidCrlFromCacheTimer = var1.scheduleWithFixedDelay(var5, var6, var8);
                  if (null == deleteInvalidCrlFromCacheTimer) {
                     throw new IllegalStateException("Scheduler returned null deleteInvalidCrlFromCacheTimer.");
                  }
               } catch (Exception var11) {
                  if (var1.isLoggable(Level.FINE)) {
                     var1.log(Level.FINE, var11, "Unable to start automatic deleting of invalid CRLs in cache.");
                  }

                  return false;
               }

               if (var1.isLoggable(Level.FINEST)) {
                  var1.log(Level.FINEST, "Successfully started automatic deleting of invalid CRLs in cache.");
               }

               return true;
            }
         }
      }
   }

   private static void cancelDeleteInvalidCrlFromCache(LogListener var0) {
      if (null != deleteInvalidCrlFromCacheTimer) {
         synchronized(deleteInvalidCrlFromCacheSync) {
            if (null != deleteInvalidCrlFromCacheTimer) {
               if (null != var0 && var0.isLoggable(Level.FINEST)) {
                  var0.log(Level.FINEST, "Attempting to stop automatic deleting of invalid CRLs in cache.");
               }

               try {
                  deleteInvalidCrlFromCacheTimer.cancel();
                  deleteInvalidCrlFromCacheTimer = null;
                  if (null != var0 && var0.isLoggable(Level.FINEST)) {
                     var0.log(Level.FINEST, "Successfully stopped automatic deleting of invalid CRLs in cache.");
                  }
               } catch (Exception var4) {
                  if (null != var0 && var0.isLoggable(Level.FINE)) {
                     var0.log(Level.FINE, var4, "Error occured while stopping automatic deleting of invalid CRLs in cache.");
                  }
               }

            }
         }
      }
   }

   static void ensureCrlCacheDir(File var0) {
      if (null == var0) {
         throw new IllegalArgumentException("Unexpected null directory used by CRL cache.");
      } else {
         if (var0.exists()) {
            if (!var0.isDirectory()) {
               throw new IllegalArgumentException("Directory reference for CRL cache is not a directory: \"" + var0.getAbsolutePath() + "\".");
            }
         } else {
            var0.mkdirs();
            if (!var0.exists()) {
               throw new IllegalStateException("Unable to create CRL cache directory: \"" + var0.getAbsolutePath() + "\".");
            }
         }

      }
   }

   private static long getTimerPeriodMillis(String var0, long var1) {
      if (var1 < 0L) {
         var1 = 0L;
      }

      Long var3 = Long.getLong(var0, var1);
      return null != var3 && var3 >= 0L ? var3 : var1;
   }
}
