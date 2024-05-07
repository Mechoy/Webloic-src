package weblogic.security.pki.revocation.common;

import java.security.cert.X509Certificate;
import java.util.logging.Level;
import javax.security.auth.x500.X500Principal;

abstract class OcspChecker extends AbstractRevocChecker {
   private static final CertRevocStatusCache ocspStatusCache = CertRevocStatusCache.getInstance();

   public static OcspChecker getInstance(AbstractCertRevocContext var0) {
      return new DefaultOcspChecker(var0);
   }

   OcspChecker(AbstractCertRevocContext var1) {
      super(var1);
   }

   final CertRevocStatus getCertRevocStatus(X509Certificate var1, X509Certificate var2) {
      Util.checkNotNull("Issuer X509Certificate.", var1);
      Util.checkNotNull("X509Certificate to be checked.", var2);
      AbstractCertRevocContext var3 = this.getContext();
      X500Principal var4 = var2.getIssuerX500Principal();
      boolean var5 = var3.isOcspResponseCacheEnabled(var4);
      if (var3.isLoggable(Level.FINEST)) {
         var3.log(Level.FINEST, "OcspResponseCacheEnabled={0}", var5);
      }

      boolean var6 = var3.isOcspNonceEnabled(var4);
      if (var3.isLoggable(Level.FINEST)) {
         var3.log(Level.FINEST, "OcspNonceEnabled={0}", var6);
      }

      CertRevocStatus var7 = null;
      if (var5) {
         if (var6) {
            this.updateCachedStatus(var2, (CertRevocStatus)null);
         } else {
            var7 = this.getCachedStatus(var2);
            if (null != var7) {
               if (var3.isLoggable(Level.FINEST)) {
                  var3.log(Level.FINEST, "Revocation status found in OCSP cache.");
               }

               return var7;
            }

            if (var3.isLoggable(Level.FINEST)) {
               var3.log(Level.FINEST, "Revocation status not found in OCSP cache.");
            }
         }
      }

      var7 = this.getRemoteStatus(var1, var2);
      if (null != var7 && var6) {
         var7 = this.checkRequiredNonce(var7);
      }

      if (var5 && !var6) {
         this.updateCachedStatus(var2, var7);
      }

      return var7;
   }

   private CertRevocStatus getCachedStatus(X509Certificate var1) {
      Util.checkNotNull("X509Certificate to be checked.", var1);
      AbstractCertRevocContext var2 = this.getContext();
      X500Principal var3 = var1.getIssuerX500Principal();
      int var4 = var2.getOcspTimeTolerance(var3);
      if (var2.isLoggable(Level.FINEST)) {
         var2.log(Level.FINEST, "OcspTimeTolerance={0}", var4);
      }

      int var5 = var2.getOcspResponseCacheRefreshPeriodPercent();
      if (var2.isLoggable(Level.FINEST)) {
         var2.log(Level.FINEST, "OcspResponseCacheRefreshPeriodPercent={0}", var5);
      }

      return ocspStatusCache.getStatus(var1, var4, var5, var2.getLogListener());
   }

   private void updateCachedStatus(X509Certificate var1, CertRevocStatus var2) {
      Util.checkNotNull("X509Certificate to be checked.", var1);
      AbstractCertRevocContext var3 = this.getContext();
      X500Principal var4 = var1.getIssuerX500Principal();
      int var5 = var3.getOcspTimeTolerance(var4);
      if (var3.isLoggable(Level.FINEST)) {
         var3.log(Level.FINEST, "OcspTimeTolerance={0}", var5);
      }

      int var6 = var3.getOcspResponseCacheRefreshPeriodPercent();
      if (var3.isLoggable(Level.FINEST)) {
         var3.log(Level.FINEST, "OcspResponseCacheRefreshPeriodPercent={0}", var6);
      }

      int var7 = var3.getOcspResponseCacheCapacity();
      if (var3.isLoggable(Level.FINEST)) {
         var3.log(Level.FINEST, "OcspResponseCacheCapacity={0}", var7);
      }

      ocspStatusCache.putStatus(var1, var2, var5, var6, var7, var3.getLogListener());
   }

   private CertRevocStatus checkRequiredNonce(CertRevocStatus var1) {
      Util.checkNotNull("CertRevocStatus", var1);
      if (!var1.isNonceIgnored()) {
         return var1;
      } else {
         AbstractCertRevocContext var2 = this.getContext();
         if (var2.isLoggable(Level.FINE)) {
            var2.log(Level.FINE, "OCSP responder ignored nonce, so response was ignored, which was:\n{0}", var1);
         }

         var2.logIgnoredNonceCertRevocStatus(var1);
         return null;
      }
   }

   abstract CertRevocStatus getRemoteStatus(X509Certificate var1, X509Certificate var2);
}
