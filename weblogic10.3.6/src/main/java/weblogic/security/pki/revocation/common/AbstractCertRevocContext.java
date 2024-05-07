package weblogic.security.pki.revocation.common;

import java.io.File;
import java.math.BigInteger;
import java.net.URI;
import java.security.PrivateKey;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import javax.security.auth.x500.X500Principal;

public abstract class AbstractCertRevocContext {
   public static final Boolean DEFAULT_CHECKING_ENABLED;
   public static final Boolean DEFAULT_CHECKING_DISABLED;
   public static final Boolean DEFAULT_FAIL_ON_UNKNOWN_REVOC_STATUS;
   public static final CertRevocCheckMethodList.SelectableMethodList DEFAULT_SELECTABLE_METHOD_LIST;
   public static final CertRevocCheckMethodList DEFAULT_METHOD_ORDER;
   public static final URI DEFAULT_OCSP_RESPONDER_URL;
   public static final String DEFAULT_OCSP_RESPONDER_URL_STRING;
   public static final AttributeUsage DEFAULT_OCSP_RESPONDER_URL_USAGE;
   public static final X509Certificate DEFAULT_OCSP_RESPONDER_TRUSTED_CERT;
   public static final Boolean DEFAULT_OCSP_NONCE_ENABLED;
   public static final PrivateKey DEFAULT_OCSP_REQUEST_SIGNING_PRIVATE_KEY;
   public static final String DEFAULT_OCSP_REQUEST_SIGNING_PRIVATE_KEY_ALIAS;
   public static final X509Certificate DEFAULT_OCSP_REQUEST_SIGNING_CERT;
   public static final Boolean DEFAULT_OCSP_RESPONSE_CACHE_ENABLED;
   public static final Integer DEFAULT_OCSP_RESPONSE_CACHE_CAPACITY;
   public static final Integer DEFAULT_OCSP_RESPONSE_CACHE_REFRESH_PERIOD_PERCENT;
   public static final Long DEFAULT_OCSP_RESPONSE_TIMEOUT;
   public static final long MIN_OCSP_RESPONSE_TIMEOUT = 1L;
   public static final long MAX_OCSP_RESPONSE_TIMEOUT = 300L;
   public static final Integer DEFAULT_OCSP_TIME_TOLERANCE;
   public static final int MIN_OCSP_TIME_TOLERANCE = 0;
   public static final int MAX_OCSP_TIME_TOLERANCE = 900;
   public static final CrlCacheType DEFAULT_CRL_CACHE_TYPE;
   public static final File DEFAULT_CRL_CACHE_IMPORT_DIR;
   public static final File DEFAULT_CRL_CACHE_TYPE_FILE_DIR;
   public static final String DEFAULT_CRL_CACHE_TYPE_LDAP_HOST_NAME;
   public static final Integer DEFAULT_CRL_CACHE_TYPE_LDAP_PORT;
   public static final Integer DEFAULT_CRL_CACHE_TYPE_LDAP_SEARCH_TIMEOUT;
   public static final int MIN_CRL_CACHE_TYPE_LDAP_SEARCH_TIMEOUT = 1;
   public static final int MAX_CRL_CACHE_TYPE_LDAP_SEARCH_TIMEOUT = 300;
   public static final Integer DEFAULT_CRL_CACHE_REFRESH_PERIOD_PERCENT;
   public static final Boolean DEFAULT_CRL_DP_ENABLED;
   public static final Long DEFAULT_CRL_DP_DOWNLOAD_TIMEOUT;
   public static final long MIN_CRL_DP_DOWNLOAD_TIMEOUT = 1L;
   public static final long MAX_CRL_DP_DOWNLOAD_TIMEOUT = 300L;
   public static final Boolean DEFAULT_CRL_DP_BACKGROUND_DOWNLOAD_ENABLED;
   public static final URI DEFAULT_CRL_DP_URL;
   public static final String DEFAULT_CRL_DP_URL_STRING;
   public static final AttributeUsage DEFAULT_CRL_DP_URL_USAGE;
   private static final String REVOCATION_SUB_DIRECTORY_NAME = "certrevocation";
   private static final String REVOCATION_CRLCACHE_SUB_DIRECTORY_NAME = "crlcache";
   private static final String REVOCATION_CRLCACHE_STORAGE_SUB_DIRECTORY_NAME = "storage";
   private static final String REVOCATION_CRLCACHE_IMPORT_SUB_DIRECTORY_NAME = "import";
   private final LogListener logListener;
   private final Set<X509Certificate> trustedCerts;

   protected AbstractCertRevocContext(Set<X509Certificate> var1, LogListener var2) {
      try {
         Util.checkNotNull("trustedCerts", var1);
         if (var1.isEmpty()) {
            throw new IllegalArgumentException("Expected populated set of trusted certificates.");
         } else {
            this.trustedCerts = Collections.unmodifiableSet(new HashSet(var1));
            if (null != var2) {
               this.logListener = var2;
            } else {
               this.logListener = DefaultLogListener.getInstance();
            }

         }
      } catch (RuntimeException var4) {
         if (var2.isLoggable(Level.FINE)) {
            var2.log(Level.FINE, var4, "AbstractCertRevocContext initialization");
         }

         throw var4;
      }
   }

   public final LogListener getLogListener() {
      return this.logListener;
   }

   public final boolean isLoggable(Level var1) {
      return this.getLogListener().isLoggable(var1);
   }

   public final void log(Level var1, String var2, Object... var3) {
      this.getLogListener().log(var1, var2, var3);
   }

   public final void log(Level var1, Throwable var2, String var3, Object... var4) {
      this.getLogListener().log(var1, var2, var3, var4);
   }

   public abstract void logAttemptingCertRevocCheck(X500Principal var1);

   public abstract void logUnknownCertRevocStatusNoFail(X500Principal var1);

   public abstract void logCertRevocStatus(CertRevocStatus var1);

   public abstract void logIgnoredNonceCertRevocStatus(CertRevocStatus var1);

   public abstract void logUnknownCertRevocStatusFail(X500Principal var1);

   public abstract void logRevokedCertRevocStatusFail(X500Principal var1);

   public abstract void logNotRevokedCertRevocStatusNotFail(X500Principal var1);

   public final Set<X509Certificate> getTrustedCerts() {
      return this.trustedCerts;
   }

   public final X509Certificate getValidTrustedCert(X500Principal var1) {
      Util.checkNotNull("subject", var1);
      Iterator var2 = this.getTrustedCerts().iterator();

      X509Certificate var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (X509Certificate)var2.next();
      } while(null == var3 || !var1.equals(var3.getSubjectX500Principal()) || !isWithinValidityPeriod(var3));

      return var3;
   }

   public final X509Certificate getValidTrustedCert(X500Principal var1, BigInteger var2) {
      Util.checkNotNull("issuer", var1);
      Util.checkNotNull("serialNumber", var2);
      Iterator var3 = this.getTrustedCerts().iterator();

      X509Certificate var4;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         var4 = (X509Certificate)var3.next();
      } while(null == var4 || !var1.equals(var4.getIssuerX500Principal()) || !var2.equals(var4.getSerialNumber()) || !isWithinValidityPeriod(var4));

      return var4;
   }

   public abstract void schedule(Runnable var1);

   public abstract Timer scheduleWithFixedDelay(Runnable var1, long var2, long var4);

   public abstract boolean isCheckingEnabled();

   public abstract boolean isCheckingDisabled(X500Principal var1);

   public abstract boolean isFailOnUnknownRevocStatus(X500Principal var1);

   public abstract CertRevocCheckMethodList getMethodOrder(X500Principal var1);

   public abstract URI getOcspResponderUrl(X500Principal var1);

   public abstract AttributeUsage getOcspResponderUrlUsage(X500Principal var1);

   public abstract X509Certificate getOcspResponderTrustedCert(X500Principal var1);

   public abstract boolean isOcspNonceEnabled(X500Principal var1);

   public abstract PrivateKey getOcspRequestSigningPrivateKey(X500Principal var1);

   public abstract X509Certificate getOcspRequestSigningCert(X500Principal var1);

   public abstract boolean isOcspResponseCacheEnabled(X500Principal var1);

   public abstract int getOcspResponseCacheCapacity();

   public abstract int getOcspResponseCacheRefreshPeriodPercent();

   public abstract long getOcspResponseTimeout(X500Principal var1);

   public abstract int getOcspTimeTolerance(X500Principal var1);

   public abstract CrlCacheType getCrlCacheType();

   public abstract File getCrlCacheImportDir();

   public abstract File getCrlCacheTypeFileDir();

   public abstract String getCrlCacheTypeLdapHostname();

   public abstract int getCrlCacheTypeLdapPort();

   public abstract int getCrlCacheTypeLdapSearchTimeout();

   public abstract int getCrlCacheRefreshPeriodPercent();

   public abstract boolean isCrlDpEnabled(X500Principal var1);

   public abstract long getCrlDpDownloadTimeout(X500Principal var1);

   public abstract boolean isCrlDpBackgroundDownloadEnabled(X500Principal var1);

   public abstract URI getCrlDpUrl(X500Principal var1);

   public abstract AttributeUsage getCrlDpUrlUsage(X500Principal var1);

   private static boolean isWithinValidityPeriod(X509Certificate var0) {
      Util.checkNotNull("X509Certificate", var0);

      try {
         var0.checkValidity();
         return true;
      } catch (CertificateNotYetValidException var2) {
      } catch (CertificateExpiredException var3) {
      }

      return false;
   }

   private static void checkCrlCacheBaseDirectory(File var0) {
      if (null == var0) {
         throw new IllegalArgumentException("Unexpected null CRL cache base directory.");
      } else if (!var0.exists()) {
         throw new IllegalArgumentException("CRL cache base directory does not exist on the given path \"" + var0.getAbsolutePath() + "\".");
      } else if (!var0.isDirectory()) {
         throw new IllegalArgumentException("CRL cache base directory path is not pointing to a directory: \"" + var0.getAbsolutePath() + "\".");
      }
   }

   protected static File getCrlCacheStorageDirectory(File var0) {
      checkCrlCacheBaseDirectory(var0);
      StringBuilder var1 = new StringBuilder(256);
      var1.append("certrevocation");
      var1.append(File.separator);
      var1.append("crlcache");
      var1.append(File.separator);
      var1.append("storage");
      String var2 = var1.toString();
      File var3 = new File(var0, var2);
      return var3;
   }

   protected static File getCrlCacheImportDirectory(File var0) {
      checkCrlCacheBaseDirectory(var0);
      StringBuilder var1 = new StringBuilder(256);
      var1.append("certrevocation");
      var1.append(File.separator);
      var1.append("crlcache");
      var1.append(File.separator);
      var1.append("import");
      String var2 = var1.toString();
      File var3 = new File(var0, var2);
      return var3;
   }

   static {
      DEFAULT_CHECKING_ENABLED = Boolean.FALSE;
      DEFAULT_CHECKING_DISABLED = Boolean.FALSE;
      DEFAULT_FAIL_ON_UNKNOWN_REVOC_STATUS = Boolean.FALSE;
      DEFAULT_SELECTABLE_METHOD_LIST = CertRevocCheckMethodList.SelectableMethodList.OCSP_THEN_CRL;
      DEFAULT_METHOD_ORDER = new CertRevocCheckMethodList(DEFAULT_SELECTABLE_METHOD_LIST);
      DEFAULT_OCSP_RESPONDER_URL = null;
      DEFAULT_OCSP_RESPONDER_URL_STRING = null;
      DEFAULT_OCSP_RESPONDER_URL_USAGE = AbstractCertRevocContext.AttributeUsage.FAILOVER;
      DEFAULT_OCSP_RESPONDER_TRUSTED_CERT = null;
      DEFAULT_OCSP_NONCE_ENABLED = Boolean.FALSE;
      DEFAULT_OCSP_REQUEST_SIGNING_PRIVATE_KEY = null;
      DEFAULT_OCSP_REQUEST_SIGNING_PRIVATE_KEY_ALIAS = null;
      DEFAULT_OCSP_REQUEST_SIGNING_CERT = null;
      DEFAULT_OCSP_RESPONSE_CACHE_ENABLED = Boolean.TRUE;
      DEFAULT_OCSP_RESPONSE_CACHE_CAPACITY = 1024;
      DEFAULT_OCSP_RESPONSE_CACHE_REFRESH_PERIOD_PERCENT = 100;
      DEFAULT_OCSP_RESPONSE_TIMEOUT = 10L;
      DEFAULT_OCSP_TIME_TOLERANCE = 0;
      DEFAULT_CRL_CACHE_TYPE = AbstractCertRevocContext.CrlCacheType.FILE;
      DEFAULT_CRL_CACHE_IMPORT_DIR = null;
      DEFAULT_CRL_CACHE_TYPE_FILE_DIR = null;
      DEFAULT_CRL_CACHE_TYPE_LDAP_HOST_NAME = null;
      DEFAULT_CRL_CACHE_TYPE_LDAP_PORT = -1;
      DEFAULT_CRL_CACHE_TYPE_LDAP_SEARCH_TIMEOUT = 10;
      DEFAULT_CRL_CACHE_REFRESH_PERIOD_PERCENT = 100;
      DEFAULT_CRL_DP_ENABLED = Boolean.TRUE;
      DEFAULT_CRL_DP_DOWNLOAD_TIMEOUT = 10L;
      DEFAULT_CRL_DP_BACKGROUND_DOWNLOAD_ENABLED = Boolean.FALSE;
      DEFAULT_CRL_DP_URL = null;
      DEFAULT_CRL_DP_URL_STRING = null;
      DEFAULT_CRL_DP_URL_USAGE = AbstractCertRevocContext.AttributeUsage.FAILOVER;
   }

   public static enum AttributeUsage {
      FAILOVER,
      OVERRIDE;
   }

   public static enum CrlCacheType {
      FILE,
      LDAP;
   }
}
