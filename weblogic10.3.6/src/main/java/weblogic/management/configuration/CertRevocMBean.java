package weblogic.management.configuration;

import weblogic.security.pki.revocation.common.AbstractCertRevocContext;
import weblogic.security.pki.revocation.common.CertRevocCheckMethodList;

public interface CertRevocMBean extends ConfigurationMBean {
   String METHOD_OCSP = CertRevocCheckMethodList.SelectableMethodList.OCSP.toString();
   String METHOD_CRL = CertRevocCheckMethodList.SelectableMethodList.CRL.toString();
   String METHOD_OCSP_THEN_CRL = CertRevocCheckMethodList.SelectableMethodList.OCSP_THEN_CRL.toString();
   String METHOD_CRL_THEN_OCSP = CertRevocCheckMethodList.SelectableMethodList.CRL_THEN_OCSP.toString();
   String CRL_CACHE_TYPE_FILE = AbstractCertRevocContext.CrlCacheType.FILE.toString();
   String CRL_CACHE_TYPE_LDAP = AbstractCertRevocContext.CrlCacheType.LDAP.toString();
   boolean DEFAULT_CHECKING_ENABLED = AbstractCertRevocContext.DEFAULT_CHECKING_ENABLED;
   boolean DEFAULT_FAIL_ON_UNKNOWN_REVOC_STATUS = AbstractCertRevocContext.DEFAULT_FAIL_ON_UNKNOWN_REVOC_STATUS;
   String DEFAULT_METHOD_ORDER = AbstractCertRevocContext.DEFAULT_SELECTABLE_METHOD_LIST.toString();
   boolean DEFAULT_OCSP_NONCE_ENABLED = AbstractCertRevocContext.DEFAULT_OCSP_NONCE_ENABLED;
   boolean DEFAULT_OCSP_RESPONSE_CACHE_ENABLED = AbstractCertRevocContext.DEFAULT_OCSP_RESPONSE_CACHE_ENABLED;
   int DEFAULT_OCSP_RESPONSE_CACHE_CAPACITY = AbstractCertRevocContext.DEFAULT_OCSP_RESPONSE_CACHE_CAPACITY;
   int DEFAULT_OCSP_RESPONSE_CACHE_REFRESH_PERIOD_PERCENT = AbstractCertRevocContext.DEFAULT_OCSP_RESPONSE_CACHE_REFRESH_PERIOD_PERCENT;
   long DEFAULT_OCSP_RESPONSE_TIMEOUT = AbstractCertRevocContext.DEFAULT_OCSP_RESPONSE_TIMEOUT;
   long MIN_OCSP_RESPONSE_TIMEOUT = 1L;
   long MAX_OCSP_RESPONSE_TIMEOUT = 300L;
   int DEFAULT_OCSP_TIME_TOLERANCE = AbstractCertRevocContext.DEFAULT_OCSP_TIME_TOLERANCE;
   int MIN_OCSP_TIME_TOLERANCE = 0;
   int MAX_OCSP_TIME_TOLERANCE = 900;
   String DEFAULT_CRL_CACHE_TYPE = AbstractCertRevocContext.DEFAULT_CRL_CACHE_TYPE.toString();
   String DEFAULT_CRL_CACHE_TYPE_LDAP_HOST_NAME = AbstractCertRevocContext.DEFAULT_CRL_CACHE_TYPE_LDAP_HOST_NAME;
   int DEFAULT_CRL_CACHE_TYPE_LDAP_PORT = AbstractCertRevocContext.DEFAULT_CRL_CACHE_TYPE_LDAP_PORT;
   int DEFAULT_CRL_CACHE_TYPE_LDAP_SEARCH_TIMEOUT = AbstractCertRevocContext.DEFAULT_CRL_CACHE_TYPE_LDAP_SEARCH_TIMEOUT;
   int MIN_CRL_CACHE_TYPE_LDAP_SEARCH_TIMEOUT = 1;
   int MAX_CRL_CACHE_TYPE_LDAP_SEARCH_TIMEOUT = 300;
   int DEFAULT_CRL_CACHE_REFRESH_PERIOD_PERCENT = AbstractCertRevocContext.DEFAULT_CRL_CACHE_REFRESH_PERIOD_PERCENT;
   boolean DEFAULT_CRL_DP_ENABLED = AbstractCertRevocContext.DEFAULT_CRL_DP_ENABLED;
   long DEFAULT_CRL_DP_DOWNLOAD_TIMEOUT = AbstractCertRevocContext.DEFAULT_CRL_DP_DOWNLOAD_TIMEOUT;
   long MIN_CRL_DP_DOWNLOAD_TIMEOUT = 1L;
   long MAX_CRL_DP_DOWNLOAD_TIMEOUT = 300L;
   boolean DEFAULT_CRL_DP_BACKGROUND_DOWNLOAD_ENABLED = AbstractCertRevocContext.DEFAULT_CRL_DP_BACKGROUND_DOWNLOAD_ENABLED;

   boolean isCheckingEnabled();

   void setCheckingEnabled(boolean var1);

   boolean isFailOnUnknownRevocStatus();

   void setFailOnUnknownRevocStatus(boolean var1);

   String getMethodOrder();

   void setMethodOrder(String var1);

   boolean isOcspNonceEnabled();

   void setOcspNonceEnabled(boolean var1);

   boolean isOcspResponseCacheEnabled();

   void setOcspResponseCacheEnabled(boolean var1);

   int getOcspResponseCacheCapacity();

   void setOcspResponseCacheCapacity(int var1);

   int getOcspResponseCacheRefreshPeriodPercent();

   void setOcspResponseCacheRefreshPeriodPercent(int var1);

   long getOcspResponseTimeout();

   void setOcspResponseTimeout(long var1);

   int getOcspTimeTolerance();

   void setOcspTimeTolerance(int var1);

   String getCrlCacheType();

   void setCrlCacheType(String var1);

   String getCrlCacheTypeLdapHostname();

   void setCrlCacheTypeLdapHostname(String var1);

   int getCrlCacheTypeLdapPort();

   void setCrlCacheTypeLdapPort(int var1);

   int getCrlCacheTypeLdapSearchTimeout();

   void setCrlCacheTypeLdapSearchTimeout(int var1);

   int getCrlCacheRefreshPeriodPercent();

   void setCrlCacheRefreshPeriodPercent(int var1);

   boolean isCrlDpEnabled();

   void setCrlDpEnabled(boolean var1);

   long getCrlDpDownloadTimeout();

   void setCrlDpDownloadTimeout(long var1);

   boolean isCrlDpBackgroundDownloadEnabled();

   void setCrlDpBackgroundDownloadEnabled(boolean var1);

   CertRevocCaMBean[] getCertRevocCas();

   CertRevocCaMBean createCertRevocCa(String var1);

   void destroyCertRevocCa(CertRevocCaMBean var1);

   CertRevocCaMBean lookupCertRevocCa(String var1);
}
