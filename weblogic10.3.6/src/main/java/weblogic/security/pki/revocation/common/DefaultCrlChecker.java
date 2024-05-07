package weblogic.security.pki.revocation.common;

import com.rsa.certj.CertJ;
import com.rsa.certj.DatabaseService;
import com.rsa.certj.InvalidParameterException;
import com.rsa.certj.InvalidUseException;
import com.rsa.certj.ProviderManagementException;
import com.rsa.certj.cert.X500Name;
import com.rsa.certj.cert.X509CRL;
import com.rsa.certj.provider.db.FlatFileDB;
import com.rsa.certj.provider.path.X509V1CertPath;
import com.rsa.certj.provider.revocation.CRLCertStatus;
import com.rsa.certj.provider.revocation.CRLEvidence;
import com.rsa.certj.spi.path.CertPathCtx;
import com.rsa.certj.spi.revocation.CertRevocationInfo;
import java.io.File;
import java.io.InputStream;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import javax.security.auth.x500.X500Principal;

class DefaultCrlChecker extends CrlChecker {
   private static final String CRL_CACHE_DB_PROVIDER_NAME = "CRL_CACHE_DB_PROVIDER";
   private static final String CRL_CERT_PATH_PROVIDER_NAME = "CRL_CERT_PATH_PROVIDER";
   private static final String CRL_CERT_STATUS_PROVIDER_NAME = "CRL_CERT_STATUS_PROVIDER";

   DefaultCrlChecker(AbstractCertRevocContext var1) {
      super(var1);
   }

   CertRevocStatus getCrlStatus(X509Certificate var1, X509Certificate var2) {
      Util.checkNotNull("Issuer X509Certificate.", var1);
      Util.checkNotNull("X509Certificate to be checked.", var2);
      AbstractCertRevocContext var3 = this.getContext();
      LogListener var4 = var3.getLogListener();
      X500Principal var5 = var2.getIssuerX500Principal();
      com.rsa.certj.cert.X509Certificate var6 = RsaUtil.toRsaCert(var2, var4);
      com.rsa.certj.cert.X509Certificate var7 = RsaUtil.toRsaCert(var1, var4);
      if (null != var6 && null != var7) {
         if (!RsaUtil.isFIPS140UsageOk(var4)) {
            return null;
         } else {
            CertRevocationInfo var8;
            try {
               var8 = this.checkCertRevocation(var5, var6, var2, var7, var1);
            } catch (OutOfMemoryError var10) {
               logThrowableDuringCrlCheck(var3, var10);
               return null;
            } catch (Exception var11) {
               logThrowableDuringCrlCheck(var3, var11);
               return null;
            }

            CertRevocStatus var9 = this.evalRevocationInfo(var2, var8);
            return var9;
         }
      } else {
         if (var3.isLoggable(Level.FINE)) {
            var3.log(Level.FINE, "Unable to check revocation status, unable to convert both subject and issuer certificates.");
         }

         return null;
      }
   }

   private static void logThrowableDuringCrlCheck(AbstractCertRevocContext var0, Throwable var1) {
      if (var0.isLoggable(Level.FINE)) {
         var0.log(Level.FINE, var1, "Exception while checking revocation status using CRLs.");
      }

   }

   private CertRevocationInfo checkCertRevocation(X500Principal var1, com.rsa.certj.cert.X509Certificate var2, X509Certificate var3, com.rsa.certj.cert.X509Certificate var4, X509Certificate var5) throws Exception {
      Util.checkNotNull("issuerDn", var1);
      Util.checkNotNull("rsaCertToCheck", var2);
      Util.checkNotNull("certToCheck", var3);
      Util.checkNotNull("rsaCertToCheckIssuer", var4);
      Util.checkNotNull("certToCheckIssuer", var5);
      CertJ var6 = this.newCertJ();
      DefaultCrlCacheAccessor var7 = this.addCrlCacheProvider(var6);
      this.addCertPathProvider(var6);
      this.addCrlCertStatusProvider(var6);
      CertPathCtx var8 = this.initCertPathCtx(var4, var7.getDatabaseService());
      AbstractCertRevocContext var9 = this.getContext();
      CertRevocationInfo var10 = null;
      boolean var11 = false;
      boolean var12 = true;

      while(var12) {
         var12 = false;
         var10 = var6.checkCertRevocation(var8, var2);
         if (null == var10) {
            if (var9.isLoggable(Level.FINE)) {
               var9.log(Level.FINE, "CRL processing implementation returned no revocation information.");
            }
            break;
         }

         if (2 != var10.getStatus()) {
            break;
         }

         if (!var11) {
            var11 = true;
            if (var9.isLoggable(Level.FINEST)) {
               var9.log(Level.FINEST, "Attempting CRL fetch from Distribution Point, CRL is not cached.");
            }

            boolean var13 = var9.isCrlDpEnabled(var1);
            if (!var13) {
               if (var9.isLoggable(Level.FINEST)) {
                  var9.log(Level.FINEST, "CRL fetch from Distribution Point is disabled.");
               }
            } else if (!var7.isCrlCacheUpdatable()) {
               if (var9.isLoggable(Level.FINEST)) {
                  var9.log(Level.FINEST, "Not attempting CRL fetch from Distribution Point, CRL cache is not updatable.");
               }
            } else {
               boolean var14 = false;

               try {
                  var14 = this.updateCrlCacheFromDP(var3, var7);
               } catch (Exception var16) {
                  if (var9.isLoggable(Level.FINE)) {
                     var9.log(Level.FINE, var16, "Exception while updating CRL cache from Distribution Point.");
                  }
               }

               if (var14) {
                  var12 = true;
               }
            }
         }
      }

      return var10;
   }

   private boolean updateCrlCacheFromDP(X509Certificate var1, CrlCacheAccessor var2) throws Exception {
      AbstractCertRevocContext var3 = this.getContext();
      boolean var4 = CrlCacheUpdater.updateCrlCacheFromDP(var1, var2, var3);
      if (var3.isLoggable(Level.FINEST)) {
         var3.log(Level.FINEST, "Attempted to update CRL cache from DP, updatedCache={0}.", var4);
      }

      return var4;
   }

   private CertJ newCertJ() throws ProviderManagementException, InvalidUseException, InvalidParameterException {
      CertJ var1 = new CertJ();
      var1.setDevice(RsaUtil.getCryptoJDeviceList());
      return var1;
   }

   private void addCrlCertStatusProvider(CertJ var1) throws Exception {
      Util.checkNotNull("CertJ", var1);
      CRLCertStatus var2 = new CRLCertStatus("CRL_CERT_STATUS_PROVIDER");
      var1.addProvider(var2);
   }

   private void addCertPathProvider(CertJ var1) throws Exception {
      Util.checkNotNull("CertJ", var1);
      X509V1CertPath var2 = new X509V1CertPath("CRL_CERT_PATH_PROVIDER");
      var1.addProvider(var2);
   }

   private DefaultCrlCacheAccessor addCrlCacheProvider(CertJ var1) throws Exception {
      Util.checkNotNull("CertJ", var1);
      AbstractCertRevocContext var2 = this.getContext();
      AbstractCertRevocContext.CrlCacheType var3 = var2.getCrlCacheType();
      if (var2.isLoggable(Level.FINEST)) {
         var2.log(Level.FINEST, "CrlCacheType={0}", var3);
      }

      switch (var3) {
         case FILE:
            boolean var4 = this.addCrlCacheFileProvider(var1);
            DatabaseService var5 = (DatabaseService)var1.bindService(1, "CRL_CACHE_DB_PROVIDER");
            return new DefaultCrlCacheAccessor(var5, var4, var2.getLogListener());
         default:
            throw new IllegalStateException("Unable to initialize file-based CRL cache, unsupported CrlCacheType \"" + var3 + "\".");
      }
   }

   private boolean addCrlCacheFileProvider(CertJ var1) throws Exception {
      Util.checkNotNull("CertJ", var1);
      AbstractCertRevocContext var2 = this.getContext();
      File var3 = var2.getCrlCacheTypeFileDir();
      CrlCacheUpdater.ensureCrlCacheDir(var3);
      String var4 = var3.getAbsolutePath();
      if (var2.isLoggable(Level.FINEST)) {
         var2.log(Level.FINEST, "CrlCacheTypeFileDir=\"{0}\"", var4);
      }

      char[] var5 = new char[0];
      FlatFileDB var8 = new FlatFileDB("CRL_CACHE_DB_PROVIDER", var4, var5, 6, 2);
      var1.addProvider(var8);
      return true;
   }

   private CertPathCtx initCertPathCtx(com.rsa.certj.cert.X509Certificate var1, DatabaseService var2) {
      Util.checkNotNull("issuerCert", var1);
      Util.checkNotNull("dbService", var2);
      AbstractCertRevocContext var3 = this.getContext();
      com.rsa.certj.cert.X509Certificate[] var4 = new com.rsa.certj.cert.X509Certificate[]{var1};
      byte var5 = 4;
      byte[][] var6 = (byte[][])null;
      Date var7 = new Date();
      if (var3.isLoggable(Level.FINEST)) {
         var3.log(Level.FINEST, "Validation time=\"{0}\"", var7);
      }

      CertPathCtx var8 = new CertPathCtx(var5, var4, var6, var7, var2);
      return var8;
   }

   private CertRevocStatus evalRevocationInfo(X509Certificate var1, CertRevocationInfo var2) {
      Util.checkNotNull("certToCheck", var1);
      AbstractCertRevocContext var3 = this.getContext();
      if (null == var2) {
         if (var3.isLoggable(Level.FINER)) {
            var3.log(Level.FINER, "Revocation status unavailable from CRL (CertRevocationInfo is null).");
         }

         return null;
      } else {
         Boolean var4 = RsaUtil.evalRevocStatusCode(CertRevocCheckMethodList.SelectableMethod.CRL, var2.getStatus(), var3.getLogListener());
         if (null == var4) {
            return null;
         } else {
            int var5 = var2.getType();
            if (var5 != 1) {
               if (var3.isLoggable(Level.FINE)) {
                  var3.log(Level.FINE, "Revocation status unavailable from CRL, unexpected evidence type {0}.", var5);
               }

               return null;
            } else {
               CRLEvidence var6 = (CRLEvidence)var2.getEvidence();
               if (null == var6) {
                  if (var3.isLoggable(Level.FINE)) {
                     var3.log(Level.FINE, "Revocation status unavailable from CRL, no evidence available.");
                  }

                  return null;
               } else {
                  Date var7 = null;
                  Date var8 = null;
                  X509CRL var9 = (X509CRL)var6.getCRL();
                  if (null == var9) {
                     Vector var10 = var6.getCRLList();
                     if (null == var10 || var10.isEmpty()) {
                        if (var3.isLoggable(Level.FINE)) {
                           var3.log(Level.FINE, "Revocation status unavailable from CRL, no CRL evidence available.");
                        }

                        return null;
                     } else {
                        Iterator var11 = var10.iterator();

                        while(true) {
                           X509CRL var13;
                           do {
                              if (!var11.hasNext()) {
                                 return new CertRevocStatus(CertRevocCheckMethodList.SelectableMethod.CRL, var1.getSubjectX500Principal(), var1.getIssuerX500Principal(), var1.getSerialNumber(), var7, var8, var4, (Boolean)null, (Map)null);
                              }

                              Object var12 = var11.next();
                              if (!(var12 instanceof X509CRL) && var3.isLoggable(Level.FINE)) {
                                 var3.log(Level.FINE, "Found non-X509CRL object in evidence.getCRLList(), foundClass={0}", null == var12 ? null : var12.getClass().getName());
                              }

                              var13 = (X509CRL)var12;
                              if (null == var13.getNextUpdate()) {
                                 var7 = var13.getThisUpdate();
                                 var8 = var13.getNextUpdate();
                                 return new CertRevocStatus(CertRevocCheckMethodList.SelectableMethod.CRL, var1.getSubjectX500Principal(), var1.getIssuerX500Principal(), var1.getSerialNumber(), var7, var8, var4, (Boolean)null, (Map)null);
                              }
                           } while(null != var8 && !var13.getNextUpdate().before(var8));

                           var7 = var13.getThisUpdate();
                           var8 = var13.getNextUpdate();
                        }
                     }
                  } else {
                     var7 = var9.getThisUpdate();
                     var8 = var9.getNextUpdate();
                     return new CertRevocStatus(CertRevocCheckMethodList.SelectableMethod.CRL, var1.getSubjectX500Principal(), var1.getIssuerX500Principal(), var1.getSerialNumber(), var7, var8, var4, (Boolean)null, (Map)null);
                  }
               }
            }
         }
      }
   }

   CrlCacheAccessor getCrlCacheAccessor() {
      DefaultCrlCacheAccessor var1 = null;

      try {
         var1 = this.addCrlCacheProvider(this.newCertJ());
      } catch (Exception var4) {
         AbstractCertRevocContext var3 = this.getContext();
         if (var3.isLoggable(Level.FINE)) {
            var3.log(Level.FINE, var4, "Unable to get CrlCacheAccessor.");
         }
      }

      return var1;
   }

   private static final class DefaultCrlCacheAccessor implements CrlCacheAccessor {
      private final DatabaseService dbService;
      private final boolean crlCacheUpdatable;
      private final LogListener logger;

      private DefaultCrlCacheAccessor(DatabaseService var1, boolean var2, LogListener var3) {
         Util.checkNotNull("DatabaseService", var1);
         this.dbService = var1;
         this.crlCacheUpdatable = var2;
         this.logger = var3;
      }

      public boolean loadCrl(InputStream var1) throws Exception {
         Util.checkNotNull("InputStream", var1);

         byte[] var2;
         try {
            var2 = Util.readAll(var1);
         } catch (OutOfMemoryError var11) {
            this.logErrorLoadCrl(var11, "reading");
            throw new RuntimeException(var11);
         } catch (Exception var12) {
            this.logErrorLoadCrl(var12, "reading");
            throw var12;
         }

         X509CRL var5;
         try {
            var5 = new X509CRL(var2, 0, 0);
         } catch (OutOfMemoryError var9) {
            this.logErrorLoadCrl(var9, "parsing");
            throw new RuntimeException(var9);
         } catch (Exception var10) {
            this.logErrorLoadCrl(var10, "parsing");
            throw var10;
         }

         Object var13 = null;

         try {
            this.dbService.insertCRL(var5);
            return true;
         } catch (OutOfMemoryError var7) {
            this.logErrorLoadCrl(var7, "inserting");
            throw new RuntimeException(var7);
         } catch (Exception var8) {
            this.logErrorLoadCrl(var8, "inserting");
            throw var8;
         }
      }

      public void deleteCrl(X500Principal var1, Date var2) throws Exception {
         Util.checkNotNull("issuerX500Name", var1);
         Util.checkNotNull("thisUpdate", var2);

         X500Name var3;
         try {
            byte[] var4 = var1.getEncoded();
            var3 = new X500Name(var4, 0, 0);
         } catch (Exception var9) {
            throw new IllegalArgumentException("Illegal issuer distinguished name: " + var1, var9);
         }

         try {
            this.dbService.deleteCRL(var3, var2);
         } catch (OutOfMemoryError var7) {
            throw new RuntimeException(var7);
         } catch (Exception var8) {
            throw var8;
         }
      }

      public boolean isCrlCacheUpdatable() {
         return this.crlCacheUpdatable;
      }

      private DatabaseService getDatabaseService() {
         return this.dbService;
      }

      private void logErrorLoadCrl(Throwable var1, String var2) {
         if (null != this.logger && this.logger.isLoggable(Level.FINE)) {
            this.logger.log(Level.FINE, var1, "Unable to load CRL, while " + var2 + ".");
         }

      }

      // $FF: synthetic method
      DefaultCrlCacheAccessor(DatabaseService var1, boolean var2, LogListener var3, Object var4) {
         this(var1, var2, var3);
      }
   }
}
