package weblogic.management.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorValidateException;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.CombinedIterator;

public class CertRevocCaMBeanImpl extends ConfigurationMBeanImpl implements CertRevocCaMBean, Serializable {
   private boolean _CheckingDisabled;
   private boolean _CrlDpBackgroundDownloadEnabled;
   private long _CrlDpDownloadTimeout;
   private boolean _CrlDpEnabled;
   private String _CrlDpUrl;
   private String _CrlDpUrlUsage;
   private String _DistinguishedName;
   private boolean _FailOnUnknownRevocStatus;
   private String _MethodOrder;
   private boolean _OcspNonceEnabled;
   private String _OcspResponderCertIssuerName;
   private String _OcspResponderCertSerialNumber;
   private String _OcspResponderCertSubjectName;
   private String _OcspResponderExplicitTrustMethod;
   private String _OcspResponderUrl;
   private String _OcspResponderUrlUsage;
   private boolean _OcspResponseCacheEnabled;
   private long _OcspResponseTimeout;
   private int _OcspTimeTolerance;
   private static SchemaHelper2 _schemaHelper;

   public CertRevocCaMBeanImpl() {
      this._initializeProperty(-1);
   }

   public CertRevocCaMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getDistinguishedName() {
      return this._DistinguishedName;
   }

   public boolean isDistinguishedNameSet() {
      return this._isSet(7);
   }

   public void setDistinguishedName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      CertRevocValidator.validateX500PrincipalDN(var1);
      String var2 = this._DistinguishedName;
      this._DistinguishedName = var1;
      this._postSet(7, var2, var1);
   }

   public boolean isCheckingDisabled() {
      return this._CheckingDisabled;
   }

   public boolean isCheckingDisabledSet() {
      return this._isSet(8);
   }

   public void setCheckingDisabled(boolean var1) {
      boolean var2 = this._CheckingDisabled;
      this._CheckingDisabled = var1;
      this._postSet(8, var2, var1);
   }

   public boolean isFailOnUnknownRevocStatus() {
      if (!this._isSet(9)) {
         try {
            return ((CertRevocMBean)this.getParent()).isFailOnUnknownRevocStatus();
         } catch (NullPointerException var2) {
         }
      }

      return this._FailOnUnknownRevocStatus;
   }

   public boolean isFailOnUnknownRevocStatusSet() {
      return this._isSet(9);
   }

   public void setFailOnUnknownRevocStatus(boolean var1) {
      boolean var2 = this._FailOnUnknownRevocStatus;
      this._FailOnUnknownRevocStatus = var1;
      this._postSet(9, var2, var1);
   }

   public String getMethodOrder() {
      if (!this._isSet(10)) {
         try {
            return ((CertRevocMBean)this.getParent()).getMethodOrder();
         } catch (NullPointerException var2) {
         }
      }

      return this._MethodOrder;
   }

   public boolean isMethodOrderSet() {
      return this._isSet(10);
   }

   public void setMethodOrder(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{CertRevocMBean.METHOD_OCSP, CertRevocMBean.METHOD_CRL, CertRevocMBean.METHOD_OCSP_THEN_CRL, CertRevocMBean.METHOD_CRL_THEN_OCSP};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("MethodOrder", var1, var2);
      String var3 = this._MethodOrder;
      this._MethodOrder = var1;
      this._postSet(10, var3, var1);
   }

   public String getOcspResponderUrl() {
      return this._OcspResponderUrl;
   }

   public boolean isOcspResponderUrlSet() {
      return this._isSet(11);
   }

   public void setOcspResponderUrl(String var1) {
      var1 = var1 == null ? null : var1.trim();
      CertRevocValidator.validateURL(var1);
      String var2 = this._OcspResponderUrl;
      this._OcspResponderUrl = var1;
      this._postSet(11, var2, var1);
   }

   public String getOcspResponderUrlUsage() {
      return this._OcspResponderUrlUsage;
   }

   public boolean isOcspResponderUrlUsageSet() {
      return this._isSet(12);
   }

   public void setOcspResponderUrlUsage(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{CertRevocCaMBean.USAGE_FAILOVER, CertRevocCaMBean.USAGE_OVERRIDE};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("OcspResponderUrlUsage", var1, var2);
      String var3 = this._OcspResponderUrlUsage;
      this._OcspResponderUrlUsage = var1;
      this._postSet(12, var3, var1);
   }

   public String getOcspResponderExplicitTrustMethod() {
      return this._OcspResponderExplicitTrustMethod;
   }

   public boolean isOcspResponderExplicitTrustMethodSet() {
      return this._isSet(13);
   }

   public void setOcspResponderExplicitTrustMethod(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{CertRevocCaMBean.OCSP_EXPLICIT_TRUST_METHOD_NONE, CertRevocCaMBean.OCSP_EXPLICIT_TRUST_METHOD_USE_SUBJECT, CertRevocCaMBean.OCSP_EXPLICIT_TRUST_METHOD_USE_ISSUER_SERIAL_NUMBER};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("OcspResponderExplicitTrustMethod", var1, var2);
      String var3 = this._OcspResponderExplicitTrustMethod;
      this._OcspResponderExplicitTrustMethod = var1;
      this._postSet(13, var3, var1);
   }

   public String getOcspResponderCertSubjectName() {
      return this._OcspResponderCertSubjectName;
   }

   public boolean isOcspResponderCertSubjectNameSet() {
      return this._isSet(14);
   }

   public void setOcspResponderCertSubjectName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      CertRevocValidator.validateX500PrincipalDN(var1);
      String var2 = this._OcspResponderCertSubjectName;
      this._OcspResponderCertSubjectName = var1;
      this._postSet(14, var2, var1);
   }

   public String getOcspResponderCertIssuerName() {
      return this._OcspResponderCertIssuerName;
   }

   public boolean isOcspResponderCertIssuerNameSet() {
      return this._isSet(15);
   }

   public void setOcspResponderCertIssuerName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      CertRevocValidator.validateX500PrincipalDN(var1);
      String var2 = this._OcspResponderCertIssuerName;
      this._OcspResponderCertIssuerName = var1;
      this._postSet(15, var2, var1);
   }

   public String getOcspResponderCertSerialNumber() {
      return this._OcspResponderCertSerialNumber;
   }

   public boolean isOcspResponderCertSerialNumberSet() {
      return this._isSet(16);
   }

   public void setOcspResponderCertSerialNumber(String var1) {
      var1 = var1 == null ? null : var1.trim();
      CertRevocValidator.validateCertSerialNumber(var1);
      String var2 = this._OcspResponderCertSerialNumber;
      this._OcspResponderCertSerialNumber = var1;
      this._postSet(16, var2, var1);
   }

   public boolean isOcspNonceEnabled() {
      if (!this._isSet(17)) {
         try {
            return ((CertRevocMBean)this.getParent()).isOcspNonceEnabled();
         } catch (NullPointerException var2) {
         }
      }

      return this._OcspNonceEnabled;
   }

   public boolean isOcspNonceEnabledSet() {
      return this._isSet(17);
   }

   public void setOcspNonceEnabled(boolean var1) {
      boolean var2 = this._OcspNonceEnabled;
      this._OcspNonceEnabled = var1;
      this._postSet(17, var2, var1);
   }

   public boolean isOcspResponseCacheEnabled() {
      if (!this._isSet(18)) {
         try {
            return ((CertRevocMBean)this.getParent()).isOcspResponseCacheEnabled();
         } catch (NullPointerException var2) {
         }
      }

      return this._OcspResponseCacheEnabled;
   }

   public boolean isOcspResponseCacheEnabledSet() {
      return this._isSet(18);
   }

   public void setOcspResponseCacheEnabled(boolean var1) {
      boolean var2 = this._OcspResponseCacheEnabled;
      this._OcspResponseCacheEnabled = var1;
      this._postSet(18, var2, var1);
   }

   public long getOcspResponseTimeout() {
      if (!this._isSet(19)) {
         try {
            return ((CertRevocMBean)this.getParent()).getOcspResponseTimeout();
         } catch (NullPointerException var2) {
         }
      }

      return this._OcspResponseTimeout;
   }

   public boolean isOcspResponseTimeoutSet() {
      return this._isSet(19);
   }

   public void setOcspResponseTimeout(long var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("OcspResponseTimeout", var1, 1L, 300L);
      long var3 = this._OcspResponseTimeout;
      this._OcspResponseTimeout = var1;
      this._postSet(19, var3, var1);
   }

   public int getOcspTimeTolerance() {
      if (!this._isSet(20)) {
         try {
            return ((CertRevocMBean)this.getParent()).getOcspTimeTolerance();
         } catch (NullPointerException var2) {
         }
      }

      return this._OcspTimeTolerance;
   }

   public boolean isOcspTimeToleranceSet() {
      return this._isSet(20);
   }

   public void setOcspTimeTolerance(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("OcspTimeTolerance", (long)var1, 0L, 900L);
      int var2 = this._OcspTimeTolerance;
      this._OcspTimeTolerance = var1;
      this._postSet(20, var2, var1);
   }

   public boolean isCrlDpEnabled() {
      if (!this._isSet(21)) {
         try {
            return ((CertRevocMBean)this.getParent()).isCrlDpEnabled();
         } catch (NullPointerException var2) {
         }
      }

      return this._CrlDpEnabled;
   }

   public boolean isCrlDpEnabledSet() {
      return this._isSet(21);
   }

   public void setCrlDpEnabled(boolean var1) {
      boolean var2 = this._CrlDpEnabled;
      this._CrlDpEnabled = var1;
      this._postSet(21, var2, var1);
   }

   public long getCrlDpDownloadTimeout() {
      if (!this._isSet(22)) {
         try {
            return ((CertRevocMBean)this.getParent()).getCrlDpDownloadTimeout();
         } catch (NullPointerException var2) {
         }
      }

      return this._CrlDpDownloadTimeout;
   }

   public boolean isCrlDpDownloadTimeoutSet() {
      return this._isSet(22);
   }

   public void setCrlDpDownloadTimeout(long var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("CrlDpDownloadTimeout", var1, 1L, 300L);
      long var3 = this._CrlDpDownloadTimeout;
      this._CrlDpDownloadTimeout = var1;
      this._postSet(22, var3, var1);
   }

   public boolean isCrlDpBackgroundDownloadEnabled() {
      if (!this._isSet(23)) {
         try {
            return ((CertRevocMBean)this.getParent()).isCrlDpBackgroundDownloadEnabled();
         } catch (NullPointerException var2) {
         }
      }

      return this._CrlDpBackgroundDownloadEnabled;
   }

   public boolean isCrlDpBackgroundDownloadEnabledSet() {
      return this._isSet(23);
   }

   public void setCrlDpBackgroundDownloadEnabled(boolean var1) {
      boolean var2 = this._CrlDpBackgroundDownloadEnabled;
      this._CrlDpBackgroundDownloadEnabled = var1;
      this._postSet(23, var2, var1);
   }

   public String getCrlDpUrl() {
      return this._CrlDpUrl;
   }

   public boolean isCrlDpUrlSet() {
      return this._isSet(24);
   }

   public void setCrlDpUrl(String var1) {
      var1 = var1 == null ? null : var1.trim();
      CertRevocValidator.validateURL(var1);
      String var2 = this._CrlDpUrl;
      this._CrlDpUrl = var1;
      this._postSet(24, var2, var1);
   }

   public String getCrlDpUrlUsage() {
      return this._CrlDpUrlUsage;
   }

   public boolean isCrlDpUrlUsageSet() {
      return this._isSet(25);
   }

   public void setCrlDpUrlUsage(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{CertRevocCaMBean.USAGE_FAILOVER, CertRevocCaMBean.USAGE_OVERRIDE};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("CrlDpUrlUsage", var1, var2);
      String var3 = this._CrlDpUrlUsage;
      this._CrlDpUrlUsage = var1;
      this._postSet(25, var3, var1);
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   protected void _unSet(int var1) {
      if (!this._initializeProperty(var1)) {
         super._unSet(var1);
      } else {
         this._markSet(var1, false);
      }

   }

   protected AbstractDescriptorBeanHelper _createHelper() {
      return new Helper(this);
   }

   public boolean _isAnythingSet() {
      return super._isAnythingSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 22;
      }

      try {
         switch (var1) {
            case 22:
               this._CrlDpDownloadTimeout = 0L;
               if (var2) {
                  break;
               }
            case 24:
               this._CrlDpUrl = CertRevocCaMBean.DEFAULT_CRL_DP_URL;
               if (var2) {
                  break;
               }
            case 25:
               this._CrlDpUrlUsage = CertRevocCaMBean.DEFAULT_CRL_DP_URL_USAGE;
               if (var2) {
                  break;
               }
            case 7:
               this._DistinguishedName = CertRevocCaMBean.DEFAULT_DISTINGUISHED_NAME;
               if (var2) {
                  break;
               }
            case 10:
               this._MethodOrder = null;
               if (var2) {
                  break;
               }
            case 15:
               this._OcspResponderCertIssuerName = CertRevocCaMBean.DEFAULT_OCSP_RESPONDER_CERT_ISSUER_NAME;
               if (var2) {
                  break;
               }
            case 16:
               this._OcspResponderCertSerialNumber = CertRevocCaMBean.DEFAULT_OCSP_RESPONDER_CERT_SERIAL_NUMBER;
               if (var2) {
                  break;
               }
            case 14:
               this._OcspResponderCertSubjectName = CertRevocCaMBean.DEFAULT_OCSP_RESPONDER_CERT_SUBJECT_NAME;
               if (var2) {
                  break;
               }
            case 13:
               this._OcspResponderExplicitTrustMethod = CertRevocCaMBean.DEFAULT_OCSP_RESPONDER_EXPLICIT_TRUST_METHOD;
               if (var2) {
                  break;
               }
            case 11:
               this._OcspResponderUrl = CertRevocCaMBean.DEFAULT_OCSP_RESPONDER_URL;
               if (var2) {
                  break;
               }
            case 12:
               this._OcspResponderUrlUsage = CertRevocCaMBean.DEFAULT_OCSP_RESPONDER_URL_USAGE;
               if (var2) {
                  break;
               }
            case 19:
               this._OcspResponseTimeout = 0L;
               if (var2) {
                  break;
               }
            case 20:
               this._OcspTimeTolerance = 0;
               if (var2) {
                  break;
               }
            case 8:
               this._CheckingDisabled = CertRevocCaMBean.DEFAULT_CHECKING_DISABLED;
               if (var2) {
                  break;
               }
            case 23:
               this._CrlDpBackgroundDownloadEnabled = false;
               if (var2) {
                  break;
               }
            case 21:
               this._CrlDpEnabled = false;
               if (var2) {
                  break;
               }
            case 9:
               this._FailOnUnknownRevocStatus = false;
               if (var2) {
                  break;
               }
            case 17:
               this._OcspNonceEnabled = false;
               if (var2) {
                  break;
               }
            case 18:
               this._OcspResponseCacheEnabled = false;
               if (var2) {
                  break;
               }
            default:
               if (var2) {
                  return false;
               }
         }

         return true;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (Exception var5) {
         throw (Error)(new AssertionError("Impossible Exception")).initCause(var5);
      }
   }

   public Munger.SchemaHelper _getSchemaHelper() {
      return null;
   }

   public String _getElementName(int var1) {
      return this._getSchemaHelper2().getElementName(var1);
   }

   protected String getSchemaLocation() {
      return "http://xmlns.oracle.com/weblogic/1.0/domain.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/domain";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public String getType() {
      return "CertRevocCa";
   }

   public void putValue(String var1, Object var2) {
      boolean var6;
      if (var1.equals("CheckingDisabled")) {
         var6 = this._CheckingDisabled;
         this._CheckingDisabled = (Boolean)var2;
         this._postSet(8, var6, this._CheckingDisabled);
      } else if (var1.equals("CrlDpBackgroundDownloadEnabled")) {
         var6 = this._CrlDpBackgroundDownloadEnabled;
         this._CrlDpBackgroundDownloadEnabled = (Boolean)var2;
         this._postSet(23, var6, this._CrlDpBackgroundDownloadEnabled);
      } else {
         long var5;
         if (var1.equals("CrlDpDownloadTimeout")) {
            var5 = this._CrlDpDownloadTimeout;
            this._CrlDpDownloadTimeout = (Long)var2;
            this._postSet(22, var5, this._CrlDpDownloadTimeout);
         } else if (var1.equals("CrlDpEnabled")) {
            var6 = this._CrlDpEnabled;
            this._CrlDpEnabled = (Boolean)var2;
            this._postSet(21, var6, this._CrlDpEnabled);
         } else {
            String var7;
            if (var1.equals("CrlDpUrl")) {
               var7 = this._CrlDpUrl;
               this._CrlDpUrl = (String)var2;
               this._postSet(24, var7, this._CrlDpUrl);
            } else if (var1.equals("CrlDpUrlUsage")) {
               var7 = this._CrlDpUrlUsage;
               this._CrlDpUrlUsage = (String)var2;
               this._postSet(25, var7, this._CrlDpUrlUsage);
            } else if (var1.equals("DistinguishedName")) {
               var7 = this._DistinguishedName;
               this._DistinguishedName = (String)var2;
               this._postSet(7, var7, this._DistinguishedName);
            } else if (var1.equals("FailOnUnknownRevocStatus")) {
               var6 = this._FailOnUnknownRevocStatus;
               this._FailOnUnknownRevocStatus = (Boolean)var2;
               this._postSet(9, var6, this._FailOnUnknownRevocStatus);
            } else if (var1.equals("MethodOrder")) {
               var7 = this._MethodOrder;
               this._MethodOrder = (String)var2;
               this._postSet(10, var7, this._MethodOrder);
            } else if (var1.equals("OcspNonceEnabled")) {
               var6 = this._OcspNonceEnabled;
               this._OcspNonceEnabled = (Boolean)var2;
               this._postSet(17, var6, this._OcspNonceEnabled);
            } else if (var1.equals("OcspResponderCertIssuerName")) {
               var7 = this._OcspResponderCertIssuerName;
               this._OcspResponderCertIssuerName = (String)var2;
               this._postSet(15, var7, this._OcspResponderCertIssuerName);
            } else if (var1.equals("OcspResponderCertSerialNumber")) {
               var7 = this._OcspResponderCertSerialNumber;
               this._OcspResponderCertSerialNumber = (String)var2;
               this._postSet(16, var7, this._OcspResponderCertSerialNumber);
            } else if (var1.equals("OcspResponderCertSubjectName")) {
               var7 = this._OcspResponderCertSubjectName;
               this._OcspResponderCertSubjectName = (String)var2;
               this._postSet(14, var7, this._OcspResponderCertSubjectName);
            } else if (var1.equals("OcspResponderExplicitTrustMethod")) {
               var7 = this._OcspResponderExplicitTrustMethod;
               this._OcspResponderExplicitTrustMethod = (String)var2;
               this._postSet(13, var7, this._OcspResponderExplicitTrustMethod);
            } else if (var1.equals("OcspResponderUrl")) {
               var7 = this._OcspResponderUrl;
               this._OcspResponderUrl = (String)var2;
               this._postSet(11, var7, this._OcspResponderUrl);
            } else if (var1.equals("OcspResponderUrlUsage")) {
               var7 = this._OcspResponderUrlUsage;
               this._OcspResponderUrlUsage = (String)var2;
               this._postSet(12, var7, this._OcspResponderUrlUsage);
            } else if (var1.equals("OcspResponseCacheEnabled")) {
               var6 = this._OcspResponseCacheEnabled;
               this._OcspResponseCacheEnabled = (Boolean)var2;
               this._postSet(18, var6, this._OcspResponseCacheEnabled);
            } else if (var1.equals("OcspResponseTimeout")) {
               var5 = this._OcspResponseTimeout;
               this._OcspResponseTimeout = (Long)var2;
               this._postSet(19, var5, this._OcspResponseTimeout);
            } else if (var1.equals("OcspTimeTolerance")) {
               int var3 = this._OcspTimeTolerance;
               this._OcspTimeTolerance = (Integer)var2;
               this._postSet(20, var3, this._OcspTimeTolerance);
            } else {
               super.putValue(var1, var2);
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("CheckingDisabled")) {
         return new Boolean(this._CheckingDisabled);
      } else if (var1.equals("CrlDpBackgroundDownloadEnabled")) {
         return new Boolean(this._CrlDpBackgroundDownloadEnabled);
      } else if (var1.equals("CrlDpDownloadTimeout")) {
         return new Long(this._CrlDpDownloadTimeout);
      } else if (var1.equals("CrlDpEnabled")) {
         return new Boolean(this._CrlDpEnabled);
      } else if (var1.equals("CrlDpUrl")) {
         return this._CrlDpUrl;
      } else if (var1.equals("CrlDpUrlUsage")) {
         return this._CrlDpUrlUsage;
      } else if (var1.equals("DistinguishedName")) {
         return this._DistinguishedName;
      } else if (var1.equals("FailOnUnknownRevocStatus")) {
         return new Boolean(this._FailOnUnknownRevocStatus);
      } else if (var1.equals("MethodOrder")) {
         return this._MethodOrder;
      } else if (var1.equals("OcspNonceEnabled")) {
         return new Boolean(this._OcspNonceEnabled);
      } else if (var1.equals("OcspResponderCertIssuerName")) {
         return this._OcspResponderCertIssuerName;
      } else if (var1.equals("OcspResponderCertSerialNumber")) {
         return this._OcspResponderCertSerialNumber;
      } else if (var1.equals("OcspResponderCertSubjectName")) {
         return this._OcspResponderCertSubjectName;
      } else if (var1.equals("OcspResponderExplicitTrustMethod")) {
         return this._OcspResponderExplicitTrustMethod;
      } else if (var1.equals("OcspResponderUrl")) {
         return this._OcspResponderUrl;
      } else if (var1.equals("OcspResponderUrlUsage")) {
         return this._OcspResponderUrlUsage;
      } else if (var1.equals("OcspResponseCacheEnabled")) {
         return new Boolean(this._OcspResponseCacheEnabled);
      } else if (var1.equals("OcspResponseTimeout")) {
         return new Long(this._OcspResponseTimeout);
      } else {
         return var1.equals("OcspTimeTolerance") ? new Integer(this._OcspTimeTolerance) : super.getValue(var1);
      }
   }

   public static void validateGeneration() {
      String[] var0;
      try {
         var0 = new String[]{CertRevocCaMBean.USAGE_FAILOVER, CertRevocCaMBean.USAGE_OVERRIDE};
         weblogic.descriptor.beangen.LegalChecks.checkInEnum("CrlDpUrlUsage", CertRevocCaMBean.DEFAULT_CRL_DP_URL_USAGE, var0);
      } catch (IllegalArgumentException var3) {
         throw new DescriptorValidateException("Default value for a property  should be one of the legal values. Refer annotation legalValues on property CrlDpUrlUsage in CertRevocCaMBean" + var3.getMessage());
      }

      try {
         var0 = new String[]{CertRevocCaMBean.OCSP_EXPLICIT_TRUST_METHOD_NONE, CertRevocCaMBean.OCSP_EXPLICIT_TRUST_METHOD_USE_SUBJECT, CertRevocCaMBean.OCSP_EXPLICIT_TRUST_METHOD_USE_ISSUER_SERIAL_NUMBER};
         weblogic.descriptor.beangen.LegalChecks.checkInEnum("OcspResponderExplicitTrustMethod", CertRevocCaMBean.DEFAULT_OCSP_RESPONDER_EXPLICIT_TRUST_METHOD, var0);
      } catch (IllegalArgumentException var2) {
         throw new DescriptorValidateException("Default value for a property  should be one of the legal values. Refer annotation legalValues on property OcspResponderExplicitTrustMethod in CertRevocCaMBean" + var2.getMessage());
      }

      try {
         var0 = new String[]{CertRevocCaMBean.USAGE_FAILOVER, CertRevocCaMBean.USAGE_OVERRIDE};
         weblogic.descriptor.beangen.LegalChecks.checkInEnum("OcspResponderUrlUsage", CertRevocCaMBean.DEFAULT_OCSP_RESPONDER_URL_USAGE, var0);
      } catch (IllegalArgumentException var1) {
         throw new DescriptorValidateException("Default value for a property  should be one of the legal values. Refer annotation legalValues on property OcspResponderUrlUsage in CertRevocCaMBean" + var1.getMessage());
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 10:
               if (var1.equals("crl-dp-url")) {
                  return 24;
               }
            case 11:
            case 13:
            case 15:
            case 20:
            case 22:
            case 25:
            case 26:
            case 29:
            case 30:
            case 35:
            default:
               break;
            case 12:
               if (var1.equals("method-order")) {
                  return 10;
               }
               break;
            case 14:
               if (var1.equals("crl-dp-enabled")) {
                  return 21;
               }
               break;
            case 16:
               if (var1.equals("crl-dp-url-usage")) {
                  return 25;
               }
               break;
            case 17:
               if (var1.equals("checking-disabled")) {
                  return 8;
               }
               break;
            case 18:
               if (var1.equals("distinguished-name")) {
                  return 7;
               }

               if (var1.equals("ocsp-responder-url")) {
                  return 11;
               }

               if (var1.equals("ocsp-nonce-enabled")) {
                  return 17;
               }
               break;
            case 19:
               if (var1.equals("ocsp-time-tolerance")) {
                  return 20;
               }
               break;
            case 21:
               if (var1.equals("ocsp-response-timeout")) {
                  return 19;
               }
               break;
            case 23:
               if (var1.equals("crl-dp-download-timeout")) {
                  return 22;
               }
               break;
            case 24:
               if (var1.equals("ocsp-responder-url-usage")) {
                  return 12;
               }
               break;
            case 27:
               if (var1.equals("ocsp-response-cache-enabled")) {
                  return 18;
               }
               break;
            case 28:
               if (var1.equals("fail-on-unknown-revoc-status")) {
                  return 9;
               }
               break;
            case 31:
               if (var1.equals("ocsp-responder-cert-issuer-name")) {
                  return 15;
               }
               break;
            case 32:
               if (var1.equals("ocsp-responder-cert-subject-name")) {
                  return 14;
               }
               break;
            case 33:
               if (var1.equals("ocsp-responder-cert-serial-number")) {
                  return 16;
               }
               break;
            case 34:
               if (var1.equals("crl-dp-background-download-enabled")) {
                  return 23;
               }
               break;
            case 36:
               if (var1.equals("ocsp-responder-explicit-trust-method")) {
                  return 13;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 7:
               return "distinguished-name";
            case 8:
               return "checking-disabled";
            case 9:
               return "fail-on-unknown-revoc-status";
            case 10:
               return "method-order";
            case 11:
               return "ocsp-responder-url";
            case 12:
               return "ocsp-responder-url-usage";
            case 13:
               return "ocsp-responder-explicit-trust-method";
            case 14:
               return "ocsp-responder-cert-subject-name";
            case 15:
               return "ocsp-responder-cert-issuer-name";
            case 16:
               return "ocsp-responder-cert-serial-number";
            case 17:
               return "ocsp-nonce-enabled";
            case 18:
               return "ocsp-response-cache-enabled";
            case 19:
               return "ocsp-response-timeout";
            case 20:
               return "ocsp-time-tolerance";
            case 21:
               return "crl-dp-enabled";
            case 22:
               return "crl-dp-download-timeout";
            case 23:
               return "crl-dp-background-download-enabled";
            case 24:
               return "crl-dp-url";
            case 25:
               return "crl-dp-url-usage";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isKey(int var1) {
         switch (var1) {
            case 2:
               return true;
            default:
               return super.isKey(var1);
         }
      }

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private CertRevocCaMBeanImpl bean;

      protected Helper(CertRevocCaMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "DistinguishedName";
            case 8:
               return "CheckingDisabled";
            case 9:
               return "FailOnUnknownRevocStatus";
            case 10:
               return "MethodOrder";
            case 11:
               return "OcspResponderUrl";
            case 12:
               return "OcspResponderUrlUsage";
            case 13:
               return "OcspResponderExplicitTrustMethod";
            case 14:
               return "OcspResponderCertSubjectName";
            case 15:
               return "OcspResponderCertIssuerName";
            case 16:
               return "OcspResponderCertSerialNumber";
            case 17:
               return "OcspNonceEnabled";
            case 18:
               return "OcspResponseCacheEnabled";
            case 19:
               return "OcspResponseTimeout";
            case 20:
               return "OcspTimeTolerance";
            case 21:
               return "CrlDpEnabled";
            case 22:
               return "CrlDpDownloadTimeout";
            case 23:
               return "CrlDpBackgroundDownloadEnabled";
            case 24:
               return "CrlDpUrl";
            case 25:
               return "CrlDpUrlUsage";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("CrlDpDownloadTimeout")) {
            return 22;
         } else if (var1.equals("CrlDpUrl")) {
            return 24;
         } else if (var1.equals("CrlDpUrlUsage")) {
            return 25;
         } else if (var1.equals("DistinguishedName")) {
            return 7;
         } else if (var1.equals("MethodOrder")) {
            return 10;
         } else if (var1.equals("OcspResponderCertIssuerName")) {
            return 15;
         } else if (var1.equals("OcspResponderCertSerialNumber")) {
            return 16;
         } else if (var1.equals("OcspResponderCertSubjectName")) {
            return 14;
         } else if (var1.equals("OcspResponderExplicitTrustMethod")) {
            return 13;
         } else if (var1.equals("OcspResponderUrl")) {
            return 11;
         } else if (var1.equals("OcspResponderUrlUsage")) {
            return 12;
         } else if (var1.equals("OcspResponseTimeout")) {
            return 19;
         } else if (var1.equals("OcspTimeTolerance")) {
            return 20;
         } else if (var1.equals("CheckingDisabled")) {
            return 8;
         } else if (var1.equals("CrlDpBackgroundDownloadEnabled")) {
            return 23;
         } else if (var1.equals("CrlDpEnabled")) {
            return 21;
         } else if (var1.equals("FailOnUnknownRevocStatus")) {
            return 9;
         } else if (var1.equals("OcspNonceEnabled")) {
            return 17;
         } else {
            return var1.equals("OcspResponseCacheEnabled") ? 18 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         return new CombinedIterator(var1);
      }

      protected long computeHashValue(CRC32 var1) {
         try {
            StringBuffer var2 = new StringBuffer();
            long var3 = super.computeHashValue(var1);
            if (var3 != 0L) {
               var2.append(String.valueOf(var3));
            }

            long var5 = 0L;
            if (this.bean.isCrlDpDownloadTimeoutSet()) {
               var2.append("CrlDpDownloadTimeout");
               var2.append(String.valueOf(this.bean.getCrlDpDownloadTimeout()));
            }

            if (this.bean.isCrlDpUrlSet()) {
               var2.append("CrlDpUrl");
               var2.append(String.valueOf(this.bean.getCrlDpUrl()));
            }

            if (this.bean.isCrlDpUrlUsageSet()) {
               var2.append("CrlDpUrlUsage");
               var2.append(String.valueOf(this.bean.getCrlDpUrlUsage()));
            }

            if (this.bean.isDistinguishedNameSet()) {
               var2.append("DistinguishedName");
               var2.append(String.valueOf(this.bean.getDistinguishedName()));
            }

            if (this.bean.isMethodOrderSet()) {
               var2.append("MethodOrder");
               var2.append(String.valueOf(this.bean.getMethodOrder()));
            }

            if (this.bean.isOcspResponderCertIssuerNameSet()) {
               var2.append("OcspResponderCertIssuerName");
               var2.append(String.valueOf(this.bean.getOcspResponderCertIssuerName()));
            }

            if (this.bean.isOcspResponderCertSerialNumberSet()) {
               var2.append("OcspResponderCertSerialNumber");
               var2.append(String.valueOf(this.bean.getOcspResponderCertSerialNumber()));
            }

            if (this.bean.isOcspResponderCertSubjectNameSet()) {
               var2.append("OcspResponderCertSubjectName");
               var2.append(String.valueOf(this.bean.getOcspResponderCertSubjectName()));
            }

            if (this.bean.isOcspResponderExplicitTrustMethodSet()) {
               var2.append("OcspResponderExplicitTrustMethod");
               var2.append(String.valueOf(this.bean.getOcspResponderExplicitTrustMethod()));
            }

            if (this.bean.isOcspResponderUrlSet()) {
               var2.append("OcspResponderUrl");
               var2.append(String.valueOf(this.bean.getOcspResponderUrl()));
            }

            if (this.bean.isOcspResponderUrlUsageSet()) {
               var2.append("OcspResponderUrlUsage");
               var2.append(String.valueOf(this.bean.getOcspResponderUrlUsage()));
            }

            if (this.bean.isOcspResponseTimeoutSet()) {
               var2.append("OcspResponseTimeout");
               var2.append(String.valueOf(this.bean.getOcspResponseTimeout()));
            }

            if (this.bean.isOcspTimeToleranceSet()) {
               var2.append("OcspTimeTolerance");
               var2.append(String.valueOf(this.bean.getOcspTimeTolerance()));
            }

            if (this.bean.isCheckingDisabledSet()) {
               var2.append("CheckingDisabled");
               var2.append(String.valueOf(this.bean.isCheckingDisabled()));
            }

            if (this.bean.isCrlDpBackgroundDownloadEnabledSet()) {
               var2.append("CrlDpBackgroundDownloadEnabled");
               var2.append(String.valueOf(this.bean.isCrlDpBackgroundDownloadEnabled()));
            }

            if (this.bean.isCrlDpEnabledSet()) {
               var2.append("CrlDpEnabled");
               var2.append(String.valueOf(this.bean.isCrlDpEnabled()));
            }

            if (this.bean.isFailOnUnknownRevocStatusSet()) {
               var2.append("FailOnUnknownRevocStatus");
               var2.append(String.valueOf(this.bean.isFailOnUnknownRevocStatus()));
            }

            if (this.bean.isOcspNonceEnabledSet()) {
               var2.append("OcspNonceEnabled");
               var2.append(String.valueOf(this.bean.isOcspNonceEnabled()));
            }

            if (this.bean.isOcspResponseCacheEnabledSet()) {
               var2.append("OcspResponseCacheEnabled");
               var2.append(String.valueOf(this.bean.isOcspResponseCacheEnabled()));
            }

            var1.update(var2.toString().getBytes());
            return var1.getValue();
         } catch (Exception var7) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var7);
         }
      }

      protected void computeDiff(AbstractDescriptorBean var1) {
         try {
            super.computeDiff(var1);
            CertRevocCaMBeanImpl var2 = (CertRevocCaMBeanImpl)var1;
            this.computeDiff("CrlDpDownloadTimeout", this.bean.getCrlDpDownloadTimeout(), var2.getCrlDpDownloadTimeout(), true);
            this.computeDiff("CrlDpUrl", this.bean.getCrlDpUrl(), var2.getCrlDpUrl(), true);
            this.computeDiff("CrlDpUrlUsage", this.bean.getCrlDpUrlUsage(), var2.getCrlDpUrlUsage(), true);
            this.computeDiff("DistinguishedName", this.bean.getDistinguishedName(), var2.getDistinguishedName(), true);
            this.computeDiff("MethodOrder", this.bean.getMethodOrder(), var2.getMethodOrder(), true);
            this.computeDiff("OcspResponderCertIssuerName", this.bean.getOcspResponderCertIssuerName(), var2.getOcspResponderCertIssuerName(), true);
            this.computeDiff("OcspResponderCertSerialNumber", this.bean.getOcspResponderCertSerialNumber(), var2.getOcspResponderCertSerialNumber(), true);
            this.computeDiff("OcspResponderCertSubjectName", this.bean.getOcspResponderCertSubjectName(), var2.getOcspResponderCertSubjectName(), true);
            this.computeDiff("OcspResponderExplicitTrustMethod", this.bean.getOcspResponderExplicitTrustMethod(), var2.getOcspResponderExplicitTrustMethod(), true);
            this.computeDiff("OcspResponderUrl", this.bean.getOcspResponderUrl(), var2.getOcspResponderUrl(), true);
            this.computeDiff("OcspResponderUrlUsage", this.bean.getOcspResponderUrlUsage(), var2.getOcspResponderUrlUsage(), true);
            this.computeDiff("OcspResponseTimeout", this.bean.getOcspResponseTimeout(), var2.getOcspResponseTimeout(), true);
            this.computeDiff("OcspTimeTolerance", this.bean.getOcspTimeTolerance(), var2.getOcspTimeTolerance(), true);
            this.computeDiff("CheckingDisabled", this.bean.isCheckingDisabled(), var2.isCheckingDisabled(), true);
            this.computeDiff("CrlDpBackgroundDownloadEnabled", this.bean.isCrlDpBackgroundDownloadEnabled(), var2.isCrlDpBackgroundDownloadEnabled(), true);
            this.computeDiff("CrlDpEnabled", this.bean.isCrlDpEnabled(), var2.isCrlDpEnabled(), true);
            this.computeDiff("FailOnUnknownRevocStatus", this.bean.isFailOnUnknownRevocStatus(), var2.isFailOnUnknownRevocStatus(), true);
            this.computeDiff("OcspNonceEnabled", this.bean.isOcspNonceEnabled(), var2.isOcspNonceEnabled(), true);
            this.computeDiff("OcspResponseCacheEnabled", this.bean.isOcspResponseCacheEnabled(), var2.isOcspResponseCacheEnabled(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            CertRevocCaMBeanImpl var3 = (CertRevocCaMBeanImpl)var1.getSourceBean();
            CertRevocCaMBeanImpl var4 = (CertRevocCaMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("CrlDpDownloadTimeout")) {
                  var3.setCrlDpDownloadTimeout(var4.getCrlDpDownloadTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 22);
               } else if (var5.equals("CrlDpUrl")) {
                  var3.setCrlDpUrl(var4.getCrlDpUrl());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 24);
               } else if (var5.equals("CrlDpUrlUsage")) {
                  var3.setCrlDpUrlUsage(var4.getCrlDpUrlUsage());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 25);
               } else if (var5.equals("DistinguishedName")) {
                  var3.setDistinguishedName(var4.getDistinguishedName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("MethodOrder")) {
                  var3.setMethodOrder(var4.getMethodOrder());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("OcspResponderCertIssuerName")) {
                  var3.setOcspResponderCertIssuerName(var4.getOcspResponderCertIssuerName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("OcspResponderCertSerialNumber")) {
                  var3.setOcspResponderCertSerialNumber(var4.getOcspResponderCertSerialNumber());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
               } else if (var5.equals("OcspResponderCertSubjectName")) {
                  var3.setOcspResponderCertSubjectName(var4.getOcspResponderCertSubjectName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("OcspResponderExplicitTrustMethod")) {
                  var3.setOcspResponderExplicitTrustMethod(var4.getOcspResponderExplicitTrustMethod());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("OcspResponderUrl")) {
                  var3.setOcspResponderUrl(var4.getOcspResponderUrl());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("OcspResponderUrlUsage")) {
                  var3.setOcspResponderUrlUsage(var4.getOcspResponderUrlUsage());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("OcspResponseTimeout")) {
                  var3.setOcspResponseTimeout(var4.getOcspResponseTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 19);
               } else if (var5.equals("OcspTimeTolerance")) {
                  var3.setOcspTimeTolerance(var4.getOcspTimeTolerance());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 20);
               } else if (var5.equals("CheckingDisabled")) {
                  var3.setCheckingDisabled(var4.isCheckingDisabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("CrlDpBackgroundDownloadEnabled")) {
                  var3.setCrlDpBackgroundDownloadEnabled(var4.isCrlDpBackgroundDownloadEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 23);
               } else if (var5.equals("CrlDpEnabled")) {
                  var3.setCrlDpEnabled(var4.isCrlDpEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 21);
               } else if (var5.equals("FailOnUnknownRevocStatus")) {
                  var3.setFailOnUnknownRevocStatus(var4.isFailOnUnknownRevocStatus());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("OcspNonceEnabled")) {
                  var3.setOcspNonceEnabled(var4.isOcspNonceEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 17);
               } else if (var5.equals("OcspResponseCacheEnabled")) {
                  var3.setOcspResponseCacheEnabled(var4.isOcspResponseCacheEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 18);
               } else {
                  super.applyPropertyUpdate(var1, var2);
               }

            }
         } catch (RuntimeException var7) {
            throw var7;
         } catch (Exception var8) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var8);
         }
      }

      protected AbstractDescriptorBean finishCopy(AbstractDescriptorBean var1, boolean var2, List var3) {
         try {
            CertRevocCaMBeanImpl var5 = (CertRevocCaMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("CrlDpDownloadTimeout")) && this.bean.isCrlDpDownloadTimeoutSet()) {
               var5.setCrlDpDownloadTimeout(this.bean.getCrlDpDownloadTimeout());
            }

            if ((var3 == null || !var3.contains("CrlDpUrl")) && this.bean.isCrlDpUrlSet()) {
               var5.setCrlDpUrl(this.bean.getCrlDpUrl());
            }

            if ((var3 == null || !var3.contains("CrlDpUrlUsage")) && this.bean.isCrlDpUrlUsageSet()) {
               var5.setCrlDpUrlUsage(this.bean.getCrlDpUrlUsage());
            }

            if ((var3 == null || !var3.contains("DistinguishedName")) && this.bean.isDistinguishedNameSet()) {
               var5.setDistinguishedName(this.bean.getDistinguishedName());
            }

            if ((var3 == null || !var3.contains("MethodOrder")) && this.bean.isMethodOrderSet()) {
               var5.setMethodOrder(this.bean.getMethodOrder());
            }

            if ((var3 == null || !var3.contains("OcspResponderCertIssuerName")) && this.bean.isOcspResponderCertIssuerNameSet()) {
               var5.setOcspResponderCertIssuerName(this.bean.getOcspResponderCertIssuerName());
            }

            if ((var3 == null || !var3.contains("OcspResponderCertSerialNumber")) && this.bean.isOcspResponderCertSerialNumberSet()) {
               var5.setOcspResponderCertSerialNumber(this.bean.getOcspResponderCertSerialNumber());
            }

            if ((var3 == null || !var3.contains("OcspResponderCertSubjectName")) && this.bean.isOcspResponderCertSubjectNameSet()) {
               var5.setOcspResponderCertSubjectName(this.bean.getOcspResponderCertSubjectName());
            }

            if ((var3 == null || !var3.contains("OcspResponderExplicitTrustMethod")) && this.bean.isOcspResponderExplicitTrustMethodSet()) {
               var5.setOcspResponderExplicitTrustMethod(this.bean.getOcspResponderExplicitTrustMethod());
            }

            if ((var3 == null || !var3.contains("OcspResponderUrl")) && this.bean.isOcspResponderUrlSet()) {
               var5.setOcspResponderUrl(this.bean.getOcspResponderUrl());
            }

            if ((var3 == null || !var3.contains("OcspResponderUrlUsage")) && this.bean.isOcspResponderUrlUsageSet()) {
               var5.setOcspResponderUrlUsage(this.bean.getOcspResponderUrlUsage());
            }

            if ((var3 == null || !var3.contains("OcspResponseTimeout")) && this.bean.isOcspResponseTimeoutSet()) {
               var5.setOcspResponseTimeout(this.bean.getOcspResponseTimeout());
            }

            if ((var3 == null || !var3.contains("OcspTimeTolerance")) && this.bean.isOcspTimeToleranceSet()) {
               var5.setOcspTimeTolerance(this.bean.getOcspTimeTolerance());
            }

            if ((var3 == null || !var3.contains("CheckingDisabled")) && this.bean.isCheckingDisabledSet()) {
               var5.setCheckingDisabled(this.bean.isCheckingDisabled());
            }

            if ((var3 == null || !var3.contains("CrlDpBackgroundDownloadEnabled")) && this.bean.isCrlDpBackgroundDownloadEnabledSet()) {
               var5.setCrlDpBackgroundDownloadEnabled(this.bean.isCrlDpBackgroundDownloadEnabled());
            }

            if ((var3 == null || !var3.contains("CrlDpEnabled")) && this.bean.isCrlDpEnabledSet()) {
               var5.setCrlDpEnabled(this.bean.isCrlDpEnabled());
            }

            if ((var3 == null || !var3.contains("FailOnUnknownRevocStatus")) && this.bean.isFailOnUnknownRevocStatusSet()) {
               var5.setFailOnUnknownRevocStatus(this.bean.isFailOnUnknownRevocStatus());
            }

            if ((var3 == null || !var3.contains("OcspNonceEnabled")) && this.bean.isOcspNonceEnabledSet()) {
               var5.setOcspNonceEnabled(this.bean.isOcspNonceEnabled());
            }

            if ((var3 == null || !var3.contains("OcspResponseCacheEnabled")) && this.bean.isOcspResponseCacheEnabledSet()) {
               var5.setOcspResponseCacheEnabled(this.bean.isOcspResponseCacheEnabled());
            }

            return var5;
         } catch (RuntimeException var6) {
            throw var6;
         } catch (Exception var7) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var7);
         }
      }

      protected void inferSubTree(Class var1, Object var2) {
         super.inferSubTree(var1, var2);
         Object var3 = null;
      }
   }
}
