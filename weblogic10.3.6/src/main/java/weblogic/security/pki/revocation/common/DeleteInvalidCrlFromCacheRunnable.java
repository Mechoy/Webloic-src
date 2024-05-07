package weblogic.security.pki.revocation.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import javax.security.auth.x500.X500Principal;

final class DeleteInvalidCrlFromCacheRunnable implements Runnable {
   private final AbstractCertRevocContext context;
   private final File crlCacheDir;
   private final CrlCacheAccessor crlCacheAccessor;
   private static final int INDEX_INITIAL_CAPACITY = 96;
   private static final Map<File, IndexValue> crlFileIndex = new ConcurrentHashMap(96);

   DeleteInvalidCrlFromCacheRunnable(AbstractCertRevocContext var1, File var2, CrlCacheAccessor var3) {
      Util.checkNotNull("AbstractCertRevocContext", var1);
      Util.checkNotNull("crlCacheDir", var2);
      Util.checkNotNull("crlCacheAccessor", var3);
      var2 = new File(var2, "crls");
      this.context = var1;
      this.crlCacheDir = var2;
      this.crlCacheAccessor = var3;
   }

   public void run() {
      try {
         this.syncIndex();
         this.deleteInvalid();
      } catch (Exception var2) {
         if (this.context.isLoggable(Level.FINE)) {
            this.context.log(Level.FINE, var2, "Exception while checking for invalid CRLs within directory \"{0}\".", this.crlCacheDir);
         }
      }

   }

   private void syncIndex() {
      HashMap var1 = new HashMap(crlFileIndex);
      File[] var2 = this.crlCacheDir.listFiles(Util.CRL_FILES_ONLY_FILTER);
      if (null != var2) {
         File[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File var6 = var3[var5];
            if (null != var6) {
               IndexValue var7 = (IndexValue)crlFileIndex.get(var6);
               if (null == var7) {
                  IndexValue var8 = null;

                  try {
                     var8 = this.getIndexValue(var6);
                  } catch (Exception var10) {
                     if (this.context.isLoggable(Level.FINE)) {
                        this.context.log(Level.FINE, var10, "Exception while indexing found CRL in cache: file={0}", var6);
                     }
                  }

                  if (null != var8) {
                     crlFileIndex.put(var6, var8);
                  }

                  Util.backgroundTaskSleep();
               } else {
                  var1.remove(var6);
               }
            }
         }

         if (!var1.isEmpty()) {
            Iterator var11 = var1.keySet().iterator();

            while(var11.hasNext()) {
               File var12 = (File)var11.next();
               crlFileIndex.remove(var12);
            }
         }

      }
   }

   private void deleteInvalid() {
      if (!crlFileIndex.isEmpty()) {
         int var1 = this.context.getCrlCacheRefreshPeriodPercent();
         Iterator var2 = crlFileIndex.entrySet().iterator();

         while(var2.hasNext()) {
            Map.Entry var3 = (Map.Entry)var2.next();
            if (this.isInvalid(var3, var1)) {
               this.deleteFromCrlCache(var3);
               Util.backgroundTaskSleep();
            }
         }
      }

   }

   private boolean isInvalid(Map.Entry<File, IndexValue> var1, int var2) {
      IndexValue var3 = (IndexValue)var1.getValue();
      Date var4 = var3.getNextUpdate();
      if (null == var4) {
         return true;
      } else {
         long var5 = var3.getThisUpdate().getTime();
         long var7 = var4.getTime();
         if (var7 <= var5) {
            return true;
         } else {
            long var9 = var7 - var5;
            long var11 = var9 * (long)var2 / 100L;
            long var13 = var5 + var11;
            long var15 = (new Date()).getTime();
            return var15 >= var13;
         }
      }
   }

   private void deleteFromCrlCache(Map.Entry<File, IndexValue> var1) {
      IndexValue var2 = (IndexValue)var1.getValue();

      try {
         this.crlCacheAccessor.deleteCrl(var2.getIssuerX500Name(), var2.getThisUpdate());
         if (this.context.isLoggable(Level.FINEST)) {
            this.context.log(Level.FINEST, "Deleted CRL from cache: file={0}, {1}", var1.getKey(), var1.getValue());
         }
      } catch (Exception var4) {
         if (this.context.isLoggable(Level.FINE)) {
            this.context.log(Level.FINE, var4, "Exception while deleting CRL from cache: file={0}, {1}", var1.getKey(), var1.getValue());
         }
      }

   }

   private IndexValue getIndexValue(File var1) throws Exception {
      X509CRL var2 = null;
      Object var3 = null;
      FileInputStream var4 = null;

      try {
         var4 = new FileInputStream(var1);
         CertificateFactory var5 = CertificateFactory.getInstance("X.509");
         var2 = (X509CRL)var5.generateCRL(var4);
      } catch (OutOfMemoryError var17) {
         var3 = new RuntimeException(var17);
      } catch (Exception var18) {
         var3 = var18;
      } finally {
         if (null != var4) {
            try {
               var4.close();
            } catch (IOException var16) {
            }
         }

      }

      IndexValue var20 = null;
      if (null != var3) {
         throw (Exception)var3;
      } else {
         if (null == var2) {
            if (this.context.isLoggable(Level.FINER)) {
               this.context.log(Level.FINER, "Reading CRL from file \"{0}\", no generated CRL.", var1);
            }
         } else {
            X500Principal var6 = var2.getIssuerX500Principal();
            if (null == var6 && this.context.isLoggable(Level.FINER)) {
               this.context.log(Level.FINER, "Reading CRL from file \"{0}\", no issuer.", var1);
            }

            Date var7 = var2.getThisUpdate();
            if (null == var7 && this.context.isLoggable(Level.FINER)) {
               this.context.log(Level.FINER, "Reading CRL from file \"{0}\", no ThisUpdate.", var1);
            }

            Date var8 = var2.getNextUpdate();
            if (null != var6 && null != var7) {
               var20 = new IndexValue(var6, var7, var8);
            }
         }

         return var20;
      }
   }

   private static final class IndexValue {
      private final Date thisUpdate;
      private final Date nextUpdate;
      private final X500Principal issuerX500Name;

      private IndexValue(X500Principal var1, Date var2, Date var3) {
         Util.checkNotNull("issuerX500Name", var1);
         Util.checkNotNull("thisUpdate", var2);
         this.issuerX500Name = var1;
         this.thisUpdate = var2;
         this.nextUpdate = var3;
      }

      public X500Principal getIssuerX500Name() {
         return this.issuerX500Name;
      }

      public Date getThisUpdate() {
         return this.thisUpdate;
      }

      public Date getNextUpdate() {
         return this.nextUpdate;
      }

      public String toString() {
         return MessageFormat.format("Issuer={0}, ThisUpdate={1}, NextUpdate={2}", this.issuerX500Name, this.thisUpdate, this.nextUpdate);
      }

      // $FF: synthetic method
      IndexValue(X500Principal var1, Date var2, Date var3, Object var4) {
         this(var1, var2, var3);
      }
   }
}
