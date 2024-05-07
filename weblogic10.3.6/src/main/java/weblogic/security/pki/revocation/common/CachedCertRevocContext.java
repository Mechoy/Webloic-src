package weblogic.security.pki.revocation.common;

import java.io.File;
import java.net.URI;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import javax.security.auth.x500.X500Principal;

public final class CachedCertRevocContext extends AbstractCertRevocContext {
   private final Attribute<Boolean> checkingEnabled;
   private final CaAttribute<Boolean> checkingDisabled;
   private final CaAttribute<Boolean> failOnUnknownRevocStatus;
   private final CaAttribute<CertRevocCheckMethodList> methodOrder;
   private final CaAttribute<URI> ocspResponderUrl;
   private final CaAttribute<AbstractCertRevocContext.AttributeUsage> ocspResponderUrlUsage;
   private final CaAttribute<X509Certificate> ocspResponderTrustedCert;
   private final CaAttribute<Boolean> ocspNonceEnabled;
   private final CaAttribute<PrivateKey> ocspRequestSigningPrivateKey;
   private final CaAttribute<X509Certificate> ocspRequestSigningCert;
   private final CaAttribute<Boolean> ocspResponseCacheEnabled;
   private final Attribute<Integer> ocspResponseCacheCapacity;
   private final Attribute<Integer> ocspResponseCacheRefreshPeriodPercent;
   private final CaAttribute<Long> ocspResponseTimeout;
   private final CaAttribute<Integer> ocspTimeTolerance;
   private final Attribute<AbstractCertRevocContext.CrlCacheType> crlCacheType;
   private final Attribute<File> crlCacheImportDir;
   private final Attribute<File> crlCacheTypeFileDir;
   private final Attribute<String> crlCacheTypeLdapHostname;
   private final Attribute<Integer> crlCacheTypeLdapPort;
   private final Attribute<Integer> crlCacheTypeLdapSearchTimeout;
   private final Attribute<Integer> crlCacheRefreshPeriodPercent;
   private final CaAttribute<Boolean> crlDpEnabled;
   private final CaAttribute<Long> crlDpDownloadTimeout;
   private final CaAttribute<Boolean> crlDpBackgroundDownloadEnabled;
   private final CaAttribute<URI> crlDpUrl;
   private final CaAttribute<AbstractCertRevocContext.AttributeUsage> crlDpUrlUsage;
   private final ExecutorService executorService;
   private final java.util.Timer timer;

   public CachedCertRevocContext(Set<X509Certificate> var1, ExecutorService var2) {
      super(var1, (LogListener)null);
      this.checkingEnabled = new Attribute(DEFAULT_CHECKING_ENABLED, (Long)null, (Long)null);
      this.checkingDisabled = new CaAttribute(DEFAULT_CHECKING_DISABLED, (Long)null, (Long)null);
      this.failOnUnknownRevocStatus = new CaAttribute(DEFAULT_FAIL_ON_UNKNOWN_REVOC_STATUS, (Long)null, (Long)null);
      this.methodOrder = new CaAttribute(DEFAULT_METHOD_ORDER, (Long)null, (Long)null);
      this.ocspResponderUrl = new CaAttribute(DEFAULT_OCSP_RESPONDER_URL, (Long)null, (Long)null);
      this.ocspResponderUrlUsage = new CaAttribute(DEFAULT_OCSP_RESPONDER_URL_USAGE, (Long)null, (Long)null);
      this.ocspResponderTrustedCert = new CaAttribute(DEFAULT_OCSP_RESPONDER_TRUSTED_CERT, (Long)null, (Long)null);
      this.ocspNonceEnabled = new CaAttribute(DEFAULT_OCSP_NONCE_ENABLED, (Long)null, (Long)null);
      this.ocspRequestSigningPrivateKey = new CaAttribute(DEFAULT_OCSP_REQUEST_SIGNING_PRIVATE_KEY, (Long)null, (Long)null);
      this.ocspRequestSigningCert = new CaAttribute(DEFAULT_OCSP_REQUEST_SIGNING_CERT, (Long)null, (Long)null);
      this.ocspResponseCacheEnabled = new CaAttribute(DEFAULT_OCSP_RESPONSE_CACHE_ENABLED, (Long)null, (Long)null);
      this.ocspResponseCacheCapacity = new Attribute(DEFAULT_OCSP_RESPONSE_CACHE_CAPACITY, 1L, (Long)null);
      this.ocspResponseCacheRefreshPeriodPercent = new Attribute(DEFAULT_OCSP_RESPONSE_CACHE_REFRESH_PERIOD_PERCENT, 1L, 100L);
      this.ocspResponseTimeout = new CaAttribute(DEFAULT_OCSP_RESPONSE_TIMEOUT, 1L, 300L);
      this.ocspTimeTolerance = new CaAttribute(DEFAULT_OCSP_TIME_TOLERANCE, 0L, 900L);
      this.crlCacheType = new Attribute(DEFAULT_CRL_CACHE_TYPE, (Long)null, (Long)null);
      this.crlCacheImportDir = new Attribute(DEFAULT_CRL_CACHE_IMPORT_DIR, (Long)null, (Long)null);
      this.crlCacheTypeFileDir = new Attribute(DEFAULT_CRL_CACHE_TYPE_FILE_DIR, (Long)null, (Long)null);
      this.crlCacheTypeLdapHostname = new Attribute(DEFAULT_CRL_CACHE_TYPE_LDAP_HOST_NAME, (Long)null, (Long)null);
      this.crlCacheTypeLdapPort = new Attribute(DEFAULT_CRL_CACHE_TYPE_LDAP_PORT, -1L, 65535L);
      this.crlCacheTypeLdapSearchTimeout = new Attribute(DEFAULT_CRL_CACHE_TYPE_LDAP_SEARCH_TIMEOUT, 1L, 300L);
      this.crlCacheRefreshPeriodPercent = new Attribute(DEFAULT_CRL_CACHE_REFRESH_PERIOD_PERCENT, 1L, 100L);
      this.crlDpEnabled = new CaAttribute(DEFAULT_CRL_DP_ENABLED, (Long)null, (Long)null);
      this.crlDpDownloadTimeout = new CaAttribute(DEFAULT_CRL_DP_DOWNLOAD_TIMEOUT, 1L, 300L);
      this.crlDpBackgroundDownloadEnabled = new CaAttribute(DEFAULT_CRL_DP_BACKGROUND_DOWNLOAD_ENABLED, (Long)null, (Long)null);
      this.crlDpUrl = new CaAttribute(DEFAULT_CRL_DP_URL, (Long)null, (Long)null);
      this.crlDpUrlUsage = new CaAttribute(DEFAULT_CRL_DP_URL_USAGE, (Long)null, (Long)null);
      this.executorService = var2;
      this.timer = new java.util.Timer(true);
   }

   public void logAttemptingCertRevocCheck(X500Principal var1) {
   }

   public void logUnknownCertRevocStatusNoFail(X500Principal var1) {
   }

   public void logCertRevocStatus(CertRevocStatus var1) {
   }

   public void logIgnoredNonceCertRevocStatus(CertRevocStatus var1) {
   }

   public void logUnknownCertRevocStatusFail(X500Principal var1) {
   }

   public void logRevokedCertRevocStatusFail(X500Principal var1) {
   }

   public void logNotRevokedCertRevocStatusNotFail(X500Principal var1) {
   }

   public ExecutorService getExecutorService() {
      return this.executorService;
   }

   public void schedule(Runnable var1) {
      Util.checkNotNull("Runnable", var1);
      if (null == this.executorService) {
         var1.run();
      } else {
         this.executorService.execute(var1);
      }

   }

   public Timer scheduleWithFixedDelay(final Runnable var1, long var2, long var4) {
      Util.checkNotNull("Runnable", var1);
      Util.checkRange("delay", var2, 0L, (Long)null);
      Util.checkRange("period", var4, 0L, (Long)null);
      final TimerTask var6 = new TimerTask() {
         public void run() {
            try {
               var1.run();
            } catch (Exception var2) {
               if (CachedCertRevocContext.this.isLoggable(Level.FINE)) {
                  CachedCertRevocContext.this.log(Level.FINE, var2, "Exception occurred running timer task {0}.", new Object[]{var1.getClass().getName()});
               }
            }

         }
      };
      this.timer.schedule(var6, var2, var4);
      Timer var7 = new Timer() {
         public void cancel() {
            if (CachedCertRevocContext.this.isLoggable(Level.FINEST)) {
               CachedCertRevocContext.this.log(Level.FINEST, "Cancelling timer task {0}.", new Object[]{var1.getClass().getName()});
            }

            boolean var1x = var6.cancel();
            if (CachedCertRevocContext.this.isLoggable(Level.FINEST)) {
               CachedCertRevocContext.this.log(Level.FINEST, "Returned from cancel for timer task {0}, Found/cancelled={1}.", new Object[]{var1.getClass().getName(), var1x});
            }

         }
      };
      return var7;
   }

   public boolean isCheckingEnabled() {
      return (Boolean)this.checkingEnabled.getValue();
   }

   public Attribute<Boolean> getAttribute_CheckingEnabled() {
      return this.checkingEnabled;
   }

   public boolean isCheckingDisabled(X500Principal var1) {
      return (Boolean)this.checkingDisabled.getResolvedValue(var1);
   }

   public CaAttribute<Boolean> getAttribute_CheckingDisabled() {
      return this.checkingDisabled;
   }

   public boolean isFailOnUnknownRevocStatus(X500Principal var1) {
      return (Boolean)this.failOnUnknownRevocStatus.getResolvedValue(var1);
   }

   public CaAttribute<Boolean> getAttribute_FailOnUnknownRevocStatus() {
      return this.failOnUnknownRevocStatus;
   }

   public CertRevocCheckMethodList getMethodOrder(X500Principal var1) {
      return (CertRevocCheckMethodList)this.methodOrder.getResolvedValue(var1);
   }

   public CaAttribute<CertRevocCheckMethodList> getAttribute_MethodOrder() {
      return this.methodOrder;
   }

   public URI getOcspResponderUrl(X500Principal var1) {
      return (URI)this.ocspResponderUrl.getResolvedValue(var1);
   }

   public CaAttribute<URI> getAttribute_OcspResponderUrl() {
      return this.ocspResponderUrl;
   }

   public AbstractCertRevocContext.AttributeUsage getOcspResponderUrlUsage(X500Principal var1) {
      return (AbstractCertRevocContext.AttributeUsage)this.ocspResponderUrlUsage.getResolvedValue(var1);
   }

   public CaAttribute<AbstractCertRevocContext.AttributeUsage> getAttribute_OcspResponderUrlUsage() {
      return this.ocspResponderUrlUsage;
   }

   public X509Certificate getOcspResponderTrustedCert(X500Principal var1) {
      return (X509Certificate)this.ocspResponderTrustedCert.getResolvedValue(var1);
   }

   public CaAttribute<X509Certificate> getAttribute_OcspResponderTrustedCert() {
      return this.ocspResponderTrustedCert;
   }

   public boolean isOcspNonceEnabled(X500Principal var1) {
      return (Boolean)this.ocspNonceEnabled.getResolvedValue(var1);
   }

   public CaAttribute<Boolean> getAttribute_OcspNonceEnabled() {
      return this.ocspNonceEnabled;
   }

   public PrivateKey getOcspRequestSigningPrivateKey(X500Principal var1) {
      return (PrivateKey)this.ocspRequestSigningPrivateKey.getResolvedValue(var1);
   }

   public CaAttribute<PrivateKey> getAttribute_OcspRequestSigningPrivateKey() {
      return this.ocspRequestSigningPrivateKey;
   }

   public X509Certificate getOcspRequestSigningCert(X500Principal var1) {
      return (X509Certificate)this.ocspRequestSigningCert.getResolvedValue(var1);
   }

   public CaAttribute<X509Certificate> getAttribute_OcspRequestSigningCert() {
      return this.ocspRequestSigningCert;
   }

   public boolean isOcspResponseCacheEnabled(X500Principal var1) {
      return (Boolean)this.ocspResponseCacheEnabled.getResolvedValue(var1);
   }

   public CaAttribute<Boolean> getAttribute_OcspResponseCacheEnabled() {
      return this.ocspResponseCacheEnabled;
   }

   public int getOcspResponseCacheCapacity() {
      return (Integer)this.ocspResponseCacheCapacity.getValue();
   }

   public Attribute<Integer> getAttribute_OcspResponseCacheCapacity() {
      return this.ocspResponseCacheCapacity;
   }

   public int getOcspResponseCacheRefreshPeriodPercent() {
      return (Integer)this.ocspResponseCacheRefreshPeriodPercent.getValue();
   }

   public Attribute<Integer> getAttribute_OcspResponseCacheRefreshPeriodPercent() {
      return this.ocspResponseCacheRefreshPeriodPercent;
   }

   public long getOcspResponseTimeout(X500Principal var1) {
      return (Long)this.ocspResponseTimeout.getResolvedValue(var1);
   }

   public CaAttribute<Long> getAttribute_OcspResponseTimeout() {
      return this.ocspResponseTimeout;
   }

   public int getOcspTimeTolerance(X500Principal var1) {
      return (Integer)this.ocspTimeTolerance.getResolvedValue(var1);
   }

   public CaAttribute<Integer> getAttribute_OcspTimeTolerance() {
      return this.ocspTimeTolerance;
   }

   public AbstractCertRevocContext.CrlCacheType getCrlCacheType() {
      return (AbstractCertRevocContext.CrlCacheType)this.crlCacheType.getValue();
   }

   public Attribute<AbstractCertRevocContext.CrlCacheType> getAttribute_CrlCacheType() {
      return this.crlCacheType;
   }

   public File getCrlCacheImportDir() {
      return (File)this.crlCacheImportDir.getValue();
   }

   public Attribute<File> getAttribute_CrlCacheImportDir() {
      return this.crlCacheImportDir;
   }

   public File getCrlCacheTypeFileDir() {
      return (File)this.crlCacheTypeFileDir.getValue();
   }

   public Attribute<File> getAttribute_CrlCacheTypeFileDir() {
      return this.crlCacheTypeFileDir;
   }

   public String getCrlCacheTypeLdapHostname() {
      return (String)this.crlCacheTypeLdapHostname.getValue();
   }

   public Attribute<String> getAttribute_CrlCacheTypeLdapHostname() {
      return this.crlCacheTypeLdapHostname;
   }

   public int getCrlCacheTypeLdapPort() {
      return (Integer)this.crlCacheTypeLdapPort.getValue();
   }

   public Attribute<Integer> getAttribute_CrlCacheTypeLdapPort() {
      return this.crlCacheTypeLdapPort;
   }

   public int getCrlCacheTypeLdapSearchTimeout() {
      return (Integer)this.crlCacheTypeLdapSearchTimeout.getValue();
   }

   public Attribute<Integer> getAttribute_CrlCacheTypeLdapSearchTimeout() {
      return this.crlCacheTypeLdapSearchTimeout;
   }

   public int getCrlCacheRefreshPeriodPercent() {
      return (Integer)this.crlCacheRefreshPeriodPercent.getValue();
   }

   public Attribute<Integer> getAttribute_CrlCacheRefreshPeriodPercent() {
      return this.crlCacheRefreshPeriodPercent;
   }

   public boolean isCrlDpEnabled(X500Principal var1) {
      return (Boolean)this.crlDpEnabled.getResolvedValue(var1);
   }

   public CaAttribute<Boolean> getAttribute_CrlDpEnabled() {
      return this.crlDpEnabled;
   }

   public long getCrlDpDownloadTimeout(X500Principal var1) {
      return (Long)this.crlDpDownloadTimeout.getResolvedValue(var1);
   }

   public CaAttribute<Long> getAttribute_CrlDpDownloadTimeout() {
      return this.crlDpDownloadTimeout;
   }

   public boolean isCrlDpBackgroundDownloadEnabled(X500Principal var1) {
      return (Boolean)this.crlDpBackgroundDownloadEnabled.getResolvedValue(var1);
   }

   public CaAttribute<Boolean> getAttribute_CrlDpBackgroundDownloadEnabled() {
      return this.crlDpBackgroundDownloadEnabled;
   }

   public URI getCrlDpUrl(X500Principal var1) {
      return (URI)this.crlDpUrl.getResolvedValue(var1);
   }

   public CaAttribute<URI> getAttribute_CrlDpUrl() {
      return this.crlDpUrl;
   }

   public AbstractCertRevocContext.AttributeUsage getCrlDpUrlUsage(X500Principal var1) {
      return (AbstractCertRevocContext.AttributeUsage)this.crlDpUrlUsage.getResolvedValue(var1);
   }

   public CaAttribute<AbstractCertRevocContext.AttributeUsage> getAttribute_CrlDpUrlUsage() {
      return this.crlDpUrlUsage;
   }

   public static class CaAttribute<T> {
      private final Attribute<T> defaultValue;
      private static final int CA_VALUES_INIT_CAPACITY = 16;
      private final ConcurrentHashMap<X500Principal, T> caValues = new ConcurrentHashMap(16);

      CaAttribute(T var1, Long var2, Long var3) {
         this.defaultValue = new Attribute(var1, var2, var3);
      }

      public T getResolvedValue(X500Principal var1) {
         Object var2 = this.getCaValue(var1);
         return null != var2 ? var2 : this.getDefaultValue();
      }

      public T getDefaultValue() {
         return this.defaultValue.getValue();
      }

      public T setDefaultValue(T var1) {
         return this.defaultValue.setValue(var1);
      }

      public T getCaValue(X500Principal var1) {
         if (null == var1) {
            throw new IllegalArgumentException("Non-null CA DN expected.");
         } else {
            return this.caValues.get(var1);
         }
      }

      public T setCaValue(X500Principal var1, T var2) {
         if (null == var1) {
            throw new IllegalArgumentException("Non-null CA DN expected.");
         } else if (null == var2) {
            return this.caValues.remove(var1);
         } else {
            this.defaultValue.checkRange(var2);
            return this.caValues.put(var1, var2);
         }
      }
   }

   public static class Attribute<T> {
      private volatile T value;
      private final Long minValue;
      private final Long maxValue;

      Attribute(T var1, Long var2, Long var3) {
         this.minValue = var2;
         this.maxValue = var3;
         this.setValue(var1);
      }

      public T getValue() {
         return this.value;
      }

      public T setValue(T var1) {
         this.checkRange(var1);
         Object var2 = this.value;
         this.value = var1;
         return var2;
      }

      private void checkRange(T var1) {
         if (null != var1) {
            if (null != this.minValue || null != this.maxValue) {
               boolean var2 = false;
               long var3 = 0L;
               if (var1 instanceof Integer) {
                  var3 = (long)(Integer)var1;
                  var2 = true;
               }

               if (var1 instanceof Long) {
                  var3 = (Long)var1;
                  var2 = true;
               }

               if (var2) {
                  Util.checkRange((String)null, var3, this.minValue, this.maxValue);
               }
            }

         }
      }
   }
}
