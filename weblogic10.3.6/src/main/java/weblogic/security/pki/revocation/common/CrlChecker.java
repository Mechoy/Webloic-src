package weblogic.security.pki.revocation.common;

import java.security.cert.X509Certificate;
import java.util.logging.Level;

abstract class CrlChecker extends AbstractRevocChecker {
   private static final boolean crlMemCacheEnabled = false;
   private static final int CRL_MEM_CACHE_MAX_SIZE = 1024;
   private static final CertRevocStatusCache crlMemCache = CertRevocStatusCache.getInstance();

   public static CrlChecker getInstance(AbstractCertRevocContext var0) {
      return new DefaultCrlChecker(var0);
   }

   CrlChecker(AbstractCertRevocContext var1) {
      super(var1);
   }

   final CertRevocStatus getCertRevocStatus(X509Certificate var1, X509Certificate var2) {
      Util.checkNotNull("Issuer X509Certificate.", var1);
      Util.checkNotNull("X509Certificate to be checked.", var2);
      AbstractCertRevocContext var3 = this.getContext();
      if (var3.isLoggable(Level.FINEST)) {
         var3.log(Level.FINEST, "CrlMemCacheEnabled={0}", false);
      }

      CertRevocStatus var4 = this.getCrlStatus(var1, var2);
      return var4;
   }

   private CertRevocStatus getCachedStatus(X509Certificate var1) {
      Util.checkNotNull("X509Certificate to be checked.", var1);
      AbstractCertRevocContext var2 = this.getContext();
      int var3 = var2.getCrlCacheRefreshPeriodPercent();
      if (var2.isLoggable(Level.FINEST)) {
         var2.log(Level.FINEST, "CrlCacheRefreshPeriodPercent={0}", var3);
      }

      return crlMemCache.getStatus(var1, 0, var3, var2.getLogListener());
   }

   private void updateCachedStatus(X509Certificate var1, CertRevocStatus var2) {
      Util.checkNotNull("X509Certificate to be checked.", var1);
      AbstractCertRevocContext var3 = this.getContext();
      int var4 = var3.getCrlCacheRefreshPeriodPercent();
      if (var3.isLoggable(Level.FINEST)) {
         var3.log(Level.FINEST, "CrlCacheRefreshPeriodPercent={0}", var4);
      }

      crlMemCache.putStatus(var1, var2, 0, var4, 1024, var3.getLogListener());
   }

   abstract CertRevocStatus getCrlStatus(X509Certificate var1, X509Certificate var2);

   abstract CrlCacheAccessor getCrlCacheAccessor();
}
