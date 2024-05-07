package weblogic.security.pki.revocation.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;

final class ImportCrlFromFileRunnable implements Runnable {
   private final File importDir;
   private final CrlCacheAccessor crlCacheAccessor;
   private final LogListener logger;

   ImportCrlFromFileRunnable(File var1, CrlCacheAccessor var2, LogListener var3) {
      Util.checkNotNull("importDir", var1);
      Util.checkNotNull("crlCacheAccessor", var2);
      this.importDir = var1;
      this.crlCacheAccessor = var2;
      this.logger = var3;
   }

   public void run() {
      Object var1 = null;

      try {
         File[] var2 = this.importDir.listFiles(Util.CRL_FILES_ONLY_FILTER);
         if (null == var2) {
            return;
         }

         File[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File var6 = var3[var5];
            if (null != var6) {
               if (null != this.logger && this.logger.isLoggable(Level.FINEST)) {
                  this.logger.log(Level.FINEST, "Trying to import CRL from file \"{0}\".", var6);
               }

               this.loadCrl(var6);
               this.delete(var6);
               if (null != this.logger && this.logger.isLoggable(Level.FINEST)) {
                  this.logger.log(Level.FINEST, "Successfully imported CRL from file \"{0}\".", var6);
               }

               Util.backgroundTaskSleep();
            }
         }
      } catch (Exception var7) {
         if (null != this.logger && this.logger.isLoggable(Level.FINE)) {
            this.logger.log(Level.FINE, var7, "Trying to import CRLs from directory \"{0}\", last attempted file \"{1}\".", this.importDir, var1);
         }
      }

   }

   private void delete(File var1) {
      try {
         if (var1.delete()) {
            if (null != this.logger && this.logger.isLoggable(Level.FINEST)) {
               this.logger.log(Level.FINEST, "Successfully deleted CRL file \"{0}\".", var1);
            }
         } else if (null != this.logger && this.logger.isLoggable(Level.FINE)) {
            this.logger.log(Level.FINE, "Unable to delete CRL file \"{0}\".", var1);
         }
      } catch (Exception var3) {
         if (null != this.logger && this.logger.isLoggable(Level.FINE)) {
            this.logger.log(Level.FINE, var3, "Error occurred while deleting CRL file \"{0}\".", var1);
         }
      }

   }

   private void loadCrl(File var1) {
      FileInputStream var2 = null;

      try {
         var2 = new FileInputStream(var1);
         boolean var3 = this.crlCacheAccessor.loadCrl(var2);
         if (null != this.logger && this.logger.isLoggable(Level.FINEST)) {
            this.logger.log(Level.FINEST, "Loaded CRL cache from file \"{0}\", cacheUpdated={1}.", var1, var3);
         }
      } catch (Exception var12) {
         if (null != this.logger && this.logger.isLoggable(Level.FINE)) {
            this.logger.log(Level.FINE, var12, "Error occurred trying to load CRL cache from file \"{0}\".", var1);
         }
      } finally {
         if (null != var2) {
            try {
               var2.close();
            } catch (IOException var11) {
            }
         }

      }

   }
}
