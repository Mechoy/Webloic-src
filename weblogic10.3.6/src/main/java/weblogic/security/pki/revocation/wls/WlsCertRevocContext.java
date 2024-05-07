package weblogic.security.pki.revocation.wls;

import java.io.File;
import java.math.BigInteger;
import java.net.URI;
import java.security.AccessController;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Set;
import java.util.logging.Level;
import javax.security.auth.x500.X500Principal;
import weblogic.kernel.Kernel;
import weblogic.management.DomainDir;
import weblogic.management.configuration.CertRevocCaMBean;
import weblogic.management.configuration.CertRevocMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.SecurityConfigurationMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.PropertyService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.SecurityLogger;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.pki.revocation.common.AbstractCertRevocContext;
import weblogic.security.pki.revocation.common.CertRevocCheckMethodList;
import weblogic.security.pki.revocation.common.CertRevocStatus;
import weblogic.security.pki.revocation.common.Timer;
import weblogic.security.service.PrivilegedActions;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManagerFactory;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public final class WlsCertRevocContext extends AbstractCertRevocContext {
   public static final ExplicitTrustMethod DEFAULT_OCSP_RESPONDER_EXPLICIT_TRUST_METHOD;
   public static final X500Principal DEFAULT_OCSP_RESPONDER_CERT_SUBJECT_NAME;
   public static final String DEFAULT_OCSP_RESPONDER_CERT_SUBJECT_NAME_STRING;
   public static final X500Principal DEFAULT_OCSP_RESPONDER_CERT_ISSUER_NAME;
   public static final String DEFAULT_OCSP_RESPONDER_CERT_ISSUER_NAME_STRING;
   public static final BigInteger DEFAULT_OCSP_RESPONDER_CERT_SERIAL_NUMBER;
   public static final String DEFAULT_OCSP_RESPONDER_CERT_SERIAL_NUMBER_STRING;
   private static final AuthenticatedSubject kernelId;

   public WlsCertRevocContext(Set<X509Certificate> var1) {
      super(var1, WlsLogListener.getInstance());
   }

   public void logAttemptingCertRevocCheck(X500Principal var1) {
      String var2 = nameFrom(var1);
      SecurityLogger.logAttemptingCertRevocCheck(var2);
   }

   public void logUnknownCertRevocStatusNoFail(X500Principal var1) {
      String var2 = nameFrom(var1);
      SecurityLogger.logUnknownCertRevocStatusNoFail(var2);
   }

   public void logCertRevocStatus(CertRevocStatus var1) {
      String var2 = stringFrom(var1);
      SecurityLogger.logCertRevocStatus(var2);
   }

   public void logIgnoredNonceCertRevocStatus(CertRevocStatus var1) {
      String var2 = stringFrom(var1);
      SecurityLogger.logIgnoredNonceCertRevocStatus(var2);
   }

   public void logUnknownCertRevocStatusFail(X500Principal var1) {
      String var2 = nameFrom(var1);
      SecurityLogger.logUnknownCertRevocStatusFail(var2);
   }

   public void logRevokedCertRevocStatusFail(X500Principal var1) {
      String var2 = nameFrom(var1);
      SecurityLogger.logRevokedCertRevocStatusFail(var2);
   }

   public void logNotRevokedCertRevocStatusNotFail(X500Principal var1) {
      String var2 = nameFrom(var1);
      SecurityLogger.logNotRevokedCertRevocStatusNotFail(var2);
   }

   public void schedule(Runnable var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Expected non-null Runnable.");
      } else {
         WorkManager var2 = WorkManagerFactory.getInstance().getDefault();
         if (null == var2) {
            throw new IllegalStateException("No weblogic.work.WorkManager available.");
         } else {
            var2.schedule(var1);
         }
      }
   }

   public Timer scheduleWithFixedDelay(final Runnable var1, long var2, long var4) {
      if (null == var1) {
         throw new IllegalArgumentException("Unexpected null Runnable.");
      } else {
         TimerListener var6 = new TimerListener() {
            public final void timerExpired(weblogic.timers.Timer var1x) {
               try {
                  var1.run();
               } catch (Exception var3) {
                  if (WlsCertRevocContext.this.isLoggable(Level.FINE)) {
                     WlsCertRevocContext.this.log(Level.FINE, var3, "Exception occurred running timer task {0}.", new Object[]{var1.getClass().getName()});
                  }
               }

            }
         };
         final weblogic.timers.Timer var7 = TimerManagerFactory.getTimerManagerFactory().getDefaultTimerManager().schedule(var6, var2, var4);
         Timer var8 = new Timer() {
            public void cancel() {
               if (WlsCertRevocContext.this.isLoggable(Level.FINEST)) {
                  WlsCertRevocContext.this.log(Level.FINEST, "Cancelling timer task {0}.", new Object[]{var1.getClass().getName()});
               }

               boolean var1x = var7.cancel();
               if (WlsCertRevocContext.this.isLoggable(Level.FINEST)) {
                  WlsCertRevocContext.this.log(Level.FINEST, "Returned from cancel for timer task {0}, Found/cancelled={1}.", new Object[]{var1.getClass().getName(), var1x});
               }

            }
         };
         return var8;
      }
   }

   public boolean isCheckingEnabled() {
      CertRevocMBean var1 = this.getCertRevocMBean();
      return null == var1 ? DEFAULT_CHECKING_ENABLED : var1.isCheckingEnabled();
   }

   public boolean isCheckingDisabled(X500Principal var1) {
      CertRevocMBean var2 = this.getCertRevocMBean();
      if (null == var2) {
         return DEFAULT_CHECKING_DISABLED;
      } else {
         CertRevocCaMBean var3 = this.getCertRevocCaMBean(var2, var1);
         return null == var3 ? DEFAULT_CHECKING_DISABLED : var3.isCheckingDisabled();
      }
   }

   public boolean isFailOnUnknownRevocStatus(X500Principal var1) {
      CertRevocMBean var2 = this.getCertRevocMBean();
      if (null == var2) {
         return DEFAULT_FAIL_ON_UNKNOWN_REVOC_STATUS;
      } else {
         CertRevocCaMBean var3 = this.getCertRevocCaMBean(var2, var1);
         return null == var3 ? var2.isFailOnUnknownRevocStatus() : var3.isFailOnUnknownRevocStatus();
      }
   }

   public CertRevocCheckMethodList getMethodOrder(X500Principal var1) {
      CertRevocMBean var2 = this.getCertRevocMBean();
      if (null == var2) {
         return DEFAULT_METHOD_ORDER;
      } else {
         CertRevocCaMBean var3 = this.getCertRevocCaMBean(var2, var1);
         String var4;
         if (null == var3) {
            var4 = var2.getMethodOrder();
         } else {
            var4 = var3.getMethodOrder();
         }

         try {
            return new CertRevocCheckMethodList(var4);
         } catch (Exception var6) {
            this.logParsingException("MethodOrder", var3, var4, var6);
            return DEFAULT_METHOD_ORDER;
         }
      }
   }

   public URI getOcspResponderUrl(X500Principal var1) {
      CertRevocMBean var2 = this.getCertRevocMBean();
      if (null == var2) {
         return DEFAULT_OCSP_RESPONDER_URL;
      } else {
         CertRevocCaMBean var3 = this.getCertRevocCaMBean(var2, var1);
         if (null == var3) {
            return DEFAULT_OCSP_RESPONDER_URL;
         } else {
            String var4 = var3.getOcspResponderUrl();

            try {
               return null == var4 ? null : new URI(var4);
            } catch (Exception var6) {
               this.logParsingException("OcspResponderUrl", var3, var4, var6);
               return DEFAULT_OCSP_RESPONDER_URL;
            }
         }
      }
   }

   public AbstractCertRevocContext.AttributeUsage getOcspResponderUrlUsage(X500Principal var1) {
      CertRevocMBean var2 = this.getCertRevocMBean();
      if (null == var2) {
         return DEFAULT_OCSP_RESPONDER_URL_USAGE;
      } else {
         CertRevocCaMBean var3 = this.getCertRevocCaMBean(var2, var1);
         if (null == var3) {
            return DEFAULT_OCSP_RESPONDER_URL_USAGE;
         } else {
            String var4 = var3.getOcspResponderUrlUsage();

            try {
               return AbstractCertRevocContext.AttributeUsage.valueOf(var4);
            } catch (Exception var6) {
               this.logParsingException("OcspResponderUrlUsage", var3, var4, var6);
               return DEFAULT_OCSP_RESPONDER_URL_USAGE;
            }
         }
      }
   }

   public X509Certificate getOcspResponderTrustedCert(X500Principal var1) {
      X509Certificate var2 = null;
      ExplicitTrustMethod var3 = this.getOcspResponderExplicitTrustMethod(var1);
      switch (var3) {
         case USE_SUBJECT:
            X500Principal var4 = this.getOcspResponderCertSubjectName(var1);
            if (null != var4) {
               var2 = this.getValidTrustedCert(var4);
            }

            if (null == var2 && this.isLoggable(Level.FINE)) {
               this.log(Level.FINE, "No valid OCSP explicitly trusted certificate for CA \"{0}\" with subject \"{1}\" was found.", new Object[]{var1, var4});
            }
            break;
         case USE_ISSUER_SERIAL_NUMBER:
            X500Principal var5 = this.getOcspResponderCertIssuerName(var1);
            BigInteger var6 = this.getOcspResponderCertSerialNumber(var1);
            if (null != var5 && null != var6) {
               var2 = this.getValidTrustedCert(var5, var6);
            }

            if (null == var2 && this.isLoggable(Level.FINE)) {
               this.log(Level.FINE, "No valid OCSP explicitly trusted certificate for CA \"{0}\" with issuer \"{1}\" serial number \"{2}\" was found.", new Object[]{var1, var5, var6});
            }
            break;
         case NONE:
            if (this.isLoggable(Level.FINEST)) {
               this.log(Level.FINEST, "No OCSP explicitly trusted certificate for CA \"{0}\" using method \"{1}\".", new Object[]{var1, var3});
            }
            break;
         default:
            throw new IllegalStateException("Unknown ExplicitTrustMethod " + var3);
      }

      if (null != var2 && this.isLoggable(Level.FINEST)) {
         this.log(Level.FINEST, "Found valid OCSP explicitly trusted certificate for CA \"{0}\" using method \"{1}\" with subject \"{2}\".", new Object[]{var1, var3, var2.getSubjectX500Principal()});
      }

      return var2;
   }

   ExplicitTrustMethod getOcspResponderExplicitTrustMethod(X500Principal var1) {
      CertRevocMBean var2 = this.getCertRevocMBean();
      if (null == var2) {
         return DEFAULT_OCSP_RESPONDER_EXPLICIT_TRUST_METHOD;
      } else {
         CertRevocCaMBean var3 = this.getCertRevocCaMBean(var2, var1);
         if (null == var3) {
            return DEFAULT_OCSP_RESPONDER_EXPLICIT_TRUST_METHOD;
         } else {
            String var4 = var3.getOcspResponderExplicitTrustMethod();

            try {
               return WlsCertRevocContext.ExplicitTrustMethod.valueOf(var4);
            } catch (Exception var6) {
               this.logParsingException("OcspResponderExplicitTrustMethod", var3, var4, var6);
               return DEFAULT_OCSP_RESPONDER_EXPLICIT_TRUST_METHOD;
            }
         }
      }
   }

   X500Principal getOcspResponderCertSubjectName(X500Principal var1) {
      CertRevocMBean var2 = this.getCertRevocMBean();
      if (null == var2) {
         return DEFAULT_OCSP_RESPONDER_CERT_SUBJECT_NAME;
      } else {
         CertRevocCaMBean var3 = this.getCertRevocCaMBean(var2, var1);
         if (null == var3) {
            return DEFAULT_OCSP_RESPONDER_CERT_SUBJECT_NAME;
         } else {
            String var4 = var3.getOcspResponderCertSubjectName();

            try {
               return null == var4 ? null : new X500Principal(var4);
            } catch (Exception var6) {
               this.logParsingException("OcspResponderCertSubjectName", var3, var4, var6);
               return DEFAULT_OCSP_RESPONDER_CERT_SUBJECT_NAME;
            }
         }
      }
   }

   X500Principal getOcspResponderCertIssuerName(X500Principal var1) {
      CertRevocMBean var2 = this.getCertRevocMBean();
      if (null == var2) {
         return DEFAULT_OCSP_RESPONDER_CERT_ISSUER_NAME;
      } else {
         CertRevocCaMBean var3 = this.getCertRevocCaMBean(var2, var1);
         if (null == var3) {
            return DEFAULT_OCSP_RESPONDER_CERT_ISSUER_NAME;
         } else {
            String var4 = var3.getOcspResponderCertIssuerName();

            try {
               return null == var4 ? null : new X500Principal(var4);
            } catch (Exception var6) {
               this.logParsingException("OcspResponderCertIssuerName", var3, var4, var6);
               return DEFAULT_OCSP_RESPONDER_CERT_ISSUER_NAME;
            }
         }
      }
   }

   BigInteger getOcspResponderCertSerialNumber(X500Principal var1) {
      CertRevocMBean var2 = this.getCertRevocMBean();
      if (null == var2) {
         return DEFAULT_OCSP_RESPONDER_CERT_SERIAL_NUMBER;
      } else {
         CertRevocCaMBean var3 = this.getCertRevocCaMBean(var2, var1);
         if (null == var3) {
            return DEFAULT_OCSP_RESPONDER_CERT_SERIAL_NUMBER;
         } else {
            String var4 = var3.getOcspResponderCertSerialNumber();

            try {
               return null == var4 ? null : new BigInteger(var4);
            } catch (Exception var6) {
               this.logParsingException("OcspResponderCertSerialNumber", var3, var4, var6);
               return DEFAULT_OCSP_RESPONDER_CERT_SERIAL_NUMBER;
            }
         }
      }
   }

   public boolean isOcspNonceEnabled(X500Principal var1) {
      CertRevocMBean var2 = this.getCertRevocMBean();
      if (null == var2) {
         return DEFAULT_OCSP_NONCE_ENABLED;
      } else {
         CertRevocCaMBean var3 = this.getCertRevocCaMBean(var2, var1);
         return null == var3 ? var2.isOcspNonceEnabled() : var3.isOcspNonceEnabled();
      }
   }

   public PrivateKey getOcspRequestSigningPrivateKey(X500Principal var1) {
      return null;
   }

   public X509Certificate getOcspRequestSigningCert(X500Principal var1) {
      return null;
   }

   public boolean isOcspResponseCacheEnabled(X500Principal var1) {
      CertRevocMBean var2 = this.getCertRevocMBean();
      if (null == var2) {
         return DEFAULT_OCSP_RESPONSE_CACHE_ENABLED;
      } else {
         CertRevocCaMBean var3 = this.getCertRevocCaMBean(var2, var1);
         return null == var3 ? var2.isOcspResponseCacheEnabled() : var3.isOcspResponseCacheEnabled();
      }
   }

   public int getOcspResponseCacheCapacity() {
      CertRevocMBean var1 = this.getCertRevocMBean();
      return null == var1 ? DEFAULT_OCSP_RESPONSE_CACHE_CAPACITY : var1.getOcspResponseCacheCapacity();
   }

   public int getOcspResponseCacheRefreshPeriodPercent() {
      CertRevocMBean var1 = this.getCertRevocMBean();
      return null == var1 ? DEFAULT_OCSP_RESPONSE_CACHE_REFRESH_PERIOD_PERCENT : var1.getOcspResponseCacheRefreshPeriodPercent();
   }

   public long getOcspResponseTimeout(X500Principal var1) {
      CertRevocMBean var2 = this.getCertRevocMBean();
      if (null == var2) {
         return DEFAULT_OCSP_RESPONSE_TIMEOUT;
      } else {
         CertRevocCaMBean var3 = this.getCertRevocCaMBean(var2, var1);
         return null == var3 ? var2.getOcspResponseTimeout() : var3.getOcspResponseTimeout();
      }
   }

   public int getOcspTimeTolerance(X500Principal var1) {
      CertRevocMBean var2 = this.getCertRevocMBean();
      if (null == var2) {
         return DEFAULT_OCSP_TIME_TOLERANCE;
      } else {
         CertRevocCaMBean var3 = this.getCertRevocCaMBean(var2, var1);
         return null == var3 ? var2.getOcspTimeTolerance() : var3.getOcspTimeTolerance();
      }
   }

   public AbstractCertRevocContext.CrlCacheType getCrlCacheType() {
      CertRevocMBean var1 = this.getCertRevocMBean();
      if (null == var1) {
         return DEFAULT_CRL_CACHE_TYPE;
      } else {
         String var2 = var1.getCrlCacheType();

         try {
            return AbstractCertRevocContext.CrlCacheType.valueOf(var2);
         } catch (Exception var4) {
            this.logParsingException("CrlCacheType", (CertRevocCaMBean)null, var2, var4);
            return DEFAULT_CRL_CACHE_TYPE;
         }
      }
   }

   public File getCrlCacheImportDir() {
      File var1 = this.getServerSecurityBaseDir();
      if (null == var1) {
         return DEFAULT_CRL_CACHE_IMPORT_DIR;
      } else {
         File var2 = getCrlCacheImportDirectory(var1);
         return null == var2 ? DEFAULT_CRL_CACHE_IMPORT_DIR : var2;
      }
   }

   public File getCrlCacheTypeFileDir() {
      File var1 = this.getServerSecurityBaseDir();
      if (null == var1) {
         return DEFAULT_CRL_CACHE_TYPE_FILE_DIR;
      } else {
         File var2 = getCrlCacheStorageDirectory(var1);
         return null == var2 ? DEFAULT_CRL_CACHE_TYPE_FILE_DIR : var2;
      }
   }

   public String getCrlCacheTypeLdapHostname() {
      CertRevocMBean var1 = this.getCertRevocMBean();
      return null == var1 ? DEFAULT_CRL_CACHE_TYPE_LDAP_HOST_NAME : var1.getCrlCacheTypeLdapHostname();
   }

   public int getCrlCacheTypeLdapPort() {
      CertRevocMBean var1 = this.getCertRevocMBean();
      return null == var1 ? DEFAULT_CRL_CACHE_TYPE_LDAP_PORT : var1.getCrlCacheTypeLdapPort();
   }

   public int getCrlCacheTypeLdapSearchTimeout() {
      CertRevocMBean var1 = this.getCertRevocMBean();
      return null == var1 ? DEFAULT_CRL_CACHE_TYPE_LDAP_SEARCH_TIMEOUT : var1.getCrlCacheTypeLdapSearchTimeout();
   }

   public int getCrlCacheRefreshPeriodPercent() {
      CertRevocMBean var1 = this.getCertRevocMBean();
      return null == var1 ? DEFAULT_CRL_CACHE_REFRESH_PERIOD_PERCENT : var1.getCrlCacheRefreshPeriodPercent();
   }

   public boolean isCrlDpEnabled(X500Principal var1) {
      CertRevocMBean var2 = this.getCertRevocMBean();
      if (null == var2) {
         return DEFAULT_CRL_DP_ENABLED;
      } else {
         CertRevocCaMBean var3 = this.getCertRevocCaMBean(var2, var1);
         return null == var3 ? var2.isCrlDpEnabled() : var3.isCrlDpEnabled();
      }
   }

   public long getCrlDpDownloadTimeout(X500Principal var1) {
      CertRevocMBean var2 = this.getCertRevocMBean();
      if (null == var2) {
         return DEFAULT_CRL_DP_DOWNLOAD_TIMEOUT;
      } else {
         CertRevocCaMBean var3 = this.getCertRevocCaMBean(var2, var1);
         return null == var3 ? var2.getCrlDpDownloadTimeout() : var3.getCrlDpDownloadTimeout();
      }
   }

   public boolean isCrlDpBackgroundDownloadEnabled(X500Principal var1) {
      CertRevocMBean var2 = this.getCertRevocMBean();
      if (null == var2) {
         return DEFAULT_CRL_DP_BACKGROUND_DOWNLOAD_ENABLED;
      } else {
         CertRevocCaMBean var3 = this.getCertRevocCaMBean(var2, var1);
         return null == var3 ? var2.isCrlDpBackgroundDownloadEnabled() : var3.isCrlDpBackgroundDownloadEnabled();
      }
   }

   public URI getCrlDpUrl(X500Principal var1) {
      CertRevocMBean var2 = this.getCertRevocMBean();
      if (null == var2) {
         return DEFAULT_CRL_DP_URL;
      } else {
         CertRevocCaMBean var3 = this.getCertRevocCaMBean(var2, var1);
         if (null == var3) {
            return DEFAULT_CRL_DP_URL;
         } else {
            String var4 = var3.getCrlDpUrl();

            try {
               return null == var4 ? null : new URI(var4);
            } catch (Exception var6) {
               this.logParsingException("CrlDpUrl", var3, var4, var6);
               return DEFAULT_CRL_DP_URL;
            }
         }
      }
   }

   public AbstractCertRevocContext.AttributeUsage getCrlDpUrlUsage(X500Principal var1) {
      CertRevocMBean var2 = this.getCertRevocMBean();
      if (null == var2) {
         return DEFAULT_CRL_DP_URL_USAGE;
      } else {
         CertRevocCaMBean var3 = this.getCertRevocCaMBean(var2, var1);
         if (null == var3) {
            return DEFAULT_CRL_DP_URL_USAGE;
         } else {
            String var4 = var3.getCrlDpUrlUsage();

            try {
               return AbstractCertRevocContext.AttributeUsage.valueOf(var4);
            } catch (Exception var6) {
               this.logParsingException("CrlDpUrlUsage", var3, var4, var6);
               return DEFAULT_CRL_DP_URL_USAGE;
            }
         }
      }
   }

   ServerMBean getServerMBean() {
      Object var1 = null;

      try {
         if (Kernel.isServer()) {
            RuntimeAccess var2 = ManagementService.getRuntimeAccess(kernelId);
            if (null == var2) {
               this.logUnexpectedNullMBean("RuntimeAccess");
            } else {
               ServerMBean var3 = var2.getServer();
               if (null == var3) {
                  this.logUnexpectedNullMBean("ServerMBean");
               }
            }
         } else if (this.isLoggable(Level.FINE)) {
            this.log(Level.FINE, "Certificate revocation checking is currently unavailable outside the server.", new Object[0]);
         }

         return (ServerMBean)var1;
      } catch (RuntimeException var4) {
         if (this.isLoggable(Level.FINE)) {
            this.log(Level.FINE, var4, "Failure getting ServerMBean for kernelId={0}.", new Object[]{kernelId});
         }

         throw var4;
      }
   }

   private String getServerName() {
      String var1 = null;

      try {
         if (Kernel.isServer()) {
            PropertyService var2 = ManagementService.getPropertyService(kernelId);
            if (null == var2) {
               if (this.isLoggable(Level.FINE)) {
                  this.log(Level.FINE, "Unexpected null PropertyService for kernelId={0}.", new Object[]{kernelId});
               }
            } else {
               var1 = var2.getServerName();
            }
         } else if (this.isLoggable(Level.FINE)) {
            this.log(Level.FINE, "Certificate revocation checking is currently unavailable outside the server.", new Object[0]);
         }

         return var1;
      } catch (RuntimeException var3) {
         if (this.isLoggable(Level.FINE)) {
            this.log(Level.FINE, var3, "Failure getting server name for kernelId={0}.", new Object[]{kernelId});
         }

         throw var3;
      }
   }

   CertRevocMBean getCertRevocMBean() {
      CertRevocMBean var1 = null;

      try {
         if (Kernel.isServer()) {
            RuntimeAccess var2 = ManagementService.getRuntimeAccess(kernelId);
            if (null == var2) {
               this.logUnexpectedNullMBean("RuntimeAccess");
            } else {
               DomainMBean var3 = var2.getDomain();
               if (null == var3) {
                  this.logUnexpectedNullMBean("DomainMBean");
               } else {
                  SecurityConfigurationMBean var4 = var3.getSecurityConfiguration();
                  if (null == var4) {
                     this.logUnexpectedNullMBean("SecurityConfigurationMBean");
                  } else {
                     var1 = var4.getCertRevoc();
                     if (null == var1) {
                        this.logUnexpectedNullMBean("CertRevocMBean");
                     }
                  }
               }
            }
         } else if (this.isLoggable(Level.FINE)) {
            this.log(Level.FINE, "Certificate revocation checking is currently unavailable outside the server.", new Object[0]);
         }

         return var1;
      } catch (RuntimeException var5) {
         if (this.isLoggable(Level.FINE)) {
            this.log(Level.FINE, var5, "Failure getting CertRevocMBean for kernelId={0}.", new Object[]{kernelId});
         }

         throw var5;
      }
   }

   CertRevocCaMBean getCertRevocCaMBean(CertRevocMBean var1, X500Principal var2) {
      if (null == var1) {
         throw new IllegalArgumentException("Expected non-null CertRevocMBean.");
      } else if (null == var2) {
         if (this.isLoggable(Level.FINE)) {
            this.log(Level.FINE, "Non-null caDn expected.", new Object[0]);
         }

         return null;
      } else {
         CertRevocCaMBean[] var3 = var1.getCertRevocCas();
         if (null != var3 && 0 != var3.length) {
            CertRevocCaMBean[] var4 = var3;
            int var5 = var3.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               CertRevocCaMBean var7 = var4[var6];
               if (null != var7) {
                  String var8 = var7.getDistinguishedName();
                  if (null != var8 && 0 != var8.length()) {
                     X500Principal var9;
                     try {
                        var9 = new X500Principal(var8);
                     } catch (Exception var11) {
                        continue;
                     }

                     if (var2.equals(var9)) {
                        return var7;
                     }
                  }
               }
            }

            return null;
         } else {
            return null;
         }
      }
   }

   private void logParsingException(String var1, CertRevocCaMBean var2, String var3, Exception var4) {
      if (this.isLoggable(Level.FINE)) {
         String var5 = null == var2 ? "CertRevocMBean" : "CertRevocCaMBean";
         this.log(Level.FINE, var4, "Invalid {0}.{1} value {2}", new Object[]{var5, var1, var3});
      }

   }

   private void logUnexpectedNullMBean(String var1) {
      if (this.isLoggable(Level.FINE)) {
         this.log(Level.FINE, "Unexpected null {0} for kernelId={1}.", new Object[]{var1, kernelId});
      }

   }

   private File getServerSecurityBaseDir() {
      String var1 = this.getServerName();
      if (null != var1 && var1.length() != 0) {
         File var2 = new File(DomainDir.getSecurityDirForServer(var1));
         return var2;
      } else {
         if (this.isLoggable(Level.FINE)) {
            this.log(Level.FINE, "Server name is null or empty.", new Object[0]);
         }

         return null;
      }
   }

   private static String nameFrom(X500Principal var0) {
      String var1 = var0 != null ? var0.getName() : null;
      return var1;
   }

   private static String stringFrom(CertRevocStatus var0) {
      String var1 = var0 != null ? var0.toString() : null;
      return var1;
   }

   static {
      DEFAULT_OCSP_RESPONDER_EXPLICIT_TRUST_METHOD = WlsCertRevocContext.ExplicitTrustMethod.NONE;
      DEFAULT_OCSP_RESPONDER_CERT_SUBJECT_NAME = null;
      DEFAULT_OCSP_RESPONDER_CERT_SUBJECT_NAME_STRING = null;
      DEFAULT_OCSP_RESPONDER_CERT_ISSUER_NAME = null;
      DEFAULT_OCSP_RESPONDER_CERT_ISSUER_NAME_STRING = null;
      DEFAULT_OCSP_RESPONDER_CERT_SERIAL_NUMBER = null;
      DEFAULT_OCSP_RESPONDER_CERT_SERIAL_NUMBER_STRING = null;
      kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }

   public static enum ExplicitTrustMethod {
      NONE,
      USE_SUBJECT,
      USE_ISSUER_SERIAL_NUMBER;
   }
}
