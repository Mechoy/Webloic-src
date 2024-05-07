package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanRemoveRejectedException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorValidateException;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class CertRevocMBeanImpl extends ConfigurationMBeanImpl implements CertRevocMBean, Serializable {
   private CertRevocCaMBean[] _CertRevocCas;
   private boolean _CheckingEnabled;
   private int _CrlCacheRefreshPeriodPercent;
   private String _CrlCacheType;
   private String _CrlCacheTypeLdapHostname;
   private int _CrlCacheTypeLdapPort;
   private int _CrlCacheTypeLdapSearchTimeout;
   private boolean _CrlDpBackgroundDownloadEnabled;
   private long _CrlDpDownloadTimeout;
   private boolean _CrlDpEnabled;
   private boolean _FailOnUnknownRevocStatus;
   private String _MethodOrder;
   private boolean _OcspNonceEnabled;
   private int _OcspResponseCacheCapacity;
   private boolean _OcspResponseCacheEnabled;
   private int _OcspResponseCacheRefreshPeriodPercent;
   private long _OcspResponseTimeout;
   private int _OcspTimeTolerance;
   private static SchemaHelper2 _schemaHelper;

   public CertRevocMBeanImpl() {
      this._initializeProperty(-1);
   }

   public CertRevocMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public boolean isCheckingEnabled() {
      return this._CheckingEnabled;
   }

   public boolean isCheckingEnabledSet() {
      return this._isSet(7);
   }

   public void setCheckingEnabled(boolean var1) {
      boolean var2 = this._CheckingEnabled;
      this._CheckingEnabled = var1;
      this._postSet(7, var2, var1);
   }

   public boolean isFailOnUnknownRevocStatus() {
      return this._FailOnUnknownRevocStatus;
   }

   public boolean isFailOnUnknownRevocStatusSet() {
      return this._isSet(8);
   }

   public void setFailOnUnknownRevocStatus(boolean var1) {
      boolean var2 = this._FailOnUnknownRevocStatus;
      this._FailOnUnknownRevocStatus = var1;
      this._postSet(8, var2, var1);
   }

   public String getMethodOrder() {
      return this._MethodOrder;
   }

   public boolean isMethodOrderSet() {
      return this._isSet(9);
   }

   public void setMethodOrder(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{CertRevocMBean.METHOD_OCSP, CertRevocMBean.METHOD_CRL, CertRevocMBean.METHOD_OCSP_THEN_CRL, CertRevocMBean.METHOD_CRL_THEN_OCSP};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("MethodOrder", var1, var2);
      String var3 = this._MethodOrder;
      this._MethodOrder = var1;
      this._postSet(9, var3, var1);
   }

   public boolean isOcspNonceEnabled() {
      return this._OcspNonceEnabled;
   }

   public boolean isOcspNonceEnabledSet() {
      return this._isSet(10);
   }

   public void setOcspNonceEnabled(boolean var1) {
      boolean var2 = this._OcspNonceEnabled;
      this._OcspNonceEnabled = var1;
      this._postSet(10, var2, var1);
   }

   public boolean isOcspResponseCacheEnabled() {
      return this._OcspResponseCacheEnabled;
   }

   public boolean isOcspResponseCacheEnabledSet() {
      return this._isSet(11);
   }

   public void setOcspResponseCacheEnabled(boolean var1) {
      boolean var2 = this._OcspResponseCacheEnabled;
      this._OcspResponseCacheEnabled = var1;
      this._postSet(11, var2, var1);
   }

   public int getOcspResponseCacheCapacity() {
      return this._OcspResponseCacheCapacity;
   }

   public boolean isOcspResponseCacheCapacitySet() {
      return this._isSet(12);
   }

   public void setOcspResponseCacheCapacity(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("OcspResponseCacheCapacity", (long)var1, 1L, 2147483647L);
      int var2 = this._OcspResponseCacheCapacity;
      this._OcspResponseCacheCapacity = var1;
      this._postSet(12, var2, var1);
   }

   public int getOcspResponseCacheRefreshPeriodPercent() {
      return this._OcspResponseCacheRefreshPeriodPercent;
   }

   public boolean isOcspResponseCacheRefreshPeriodPercentSet() {
      return this._isSet(13);
   }

   public void setOcspResponseCacheRefreshPeriodPercent(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("OcspResponseCacheRefreshPeriodPercent", (long)var1, 1L, 100L);
      int var2 = this._OcspResponseCacheRefreshPeriodPercent;
      this._OcspResponseCacheRefreshPeriodPercent = var1;
      this._postSet(13, var2, var1);
   }

   public long getOcspResponseTimeout() {
      return this._OcspResponseTimeout;
   }

   public boolean isOcspResponseTimeoutSet() {
      return this._isSet(14);
   }

   public void setOcspResponseTimeout(long var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("OcspResponseTimeout", var1, 1L, 300L);
      long var3 = this._OcspResponseTimeout;
      this._OcspResponseTimeout = var1;
      this._postSet(14, var3, var1);
   }

   public int getOcspTimeTolerance() {
      return this._OcspTimeTolerance;
   }

   public boolean isOcspTimeToleranceSet() {
      return this._isSet(15);
   }

   public void setOcspTimeTolerance(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("OcspTimeTolerance", (long)var1, 0L, 900L);
      int var2 = this._OcspTimeTolerance;
      this._OcspTimeTolerance = var1;
      this._postSet(15, var2, var1);
   }

   public String getCrlCacheType() {
      return this._CrlCacheType;
   }

   public boolean isCrlCacheTypeSet() {
      return this._isSet(16);
   }

   public void setCrlCacheType(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{CertRevocMBean.CRL_CACHE_TYPE_FILE, CertRevocMBean.CRL_CACHE_TYPE_LDAP};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("CrlCacheType", var1, var2);
      String var3 = this._CrlCacheType;
      this._CrlCacheType = var1;
      this._postSet(16, var3, var1);
   }

   public String getCrlCacheTypeLdapHostname() {
      return this._CrlCacheTypeLdapHostname;
   }

   public boolean isCrlCacheTypeLdapHostnameSet() {
      return this._isSet(17);
   }

   public void setCrlCacheTypeLdapHostname(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._CrlCacheTypeLdapHostname;
      this._CrlCacheTypeLdapHostname = var1;
      this._postSet(17, var2, var1);
   }

   public int getCrlCacheTypeLdapPort() {
      return this._CrlCacheTypeLdapPort;
   }

   public boolean isCrlCacheTypeLdapPortSet() {
      return this._isSet(18);
   }

   public void setCrlCacheTypeLdapPort(int var1) {
      CertRevocValidator.validatePort(var1);
      int var2 = this._CrlCacheTypeLdapPort;
      this._CrlCacheTypeLdapPort = var1;
      this._postSet(18, var2, var1);
   }

   public int getCrlCacheTypeLdapSearchTimeout() {
      return this._CrlCacheTypeLdapSearchTimeout;
   }

   public boolean isCrlCacheTypeLdapSearchTimeoutSet() {
      return this._isSet(19);
   }

   public void setCrlCacheTypeLdapSearchTimeout(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("CrlCacheTypeLdapSearchTimeout", (long)var1, 1L, 300L);
      int var2 = this._CrlCacheTypeLdapSearchTimeout;
      this._CrlCacheTypeLdapSearchTimeout = var1;
      this._postSet(19, var2, var1);
   }

   public int getCrlCacheRefreshPeriodPercent() {
      return this._CrlCacheRefreshPeriodPercent;
   }

   public boolean isCrlCacheRefreshPeriodPercentSet() {
      return this._isSet(20);
   }

   public void setCrlCacheRefreshPeriodPercent(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("CrlCacheRefreshPeriodPercent", (long)var1, 1L, 100L);
      int var2 = this._CrlCacheRefreshPeriodPercent;
      this._CrlCacheRefreshPeriodPercent = var1;
      this._postSet(20, var2, var1);
   }

   public boolean isCrlDpEnabled() {
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

   public void addCertRevocCa(CertRevocCaMBean var1) {
      this._getHelper()._ensureNonNull(var1);
      if (!((AbstractDescriptorBean)var1).isChildProperty(this, 24)) {
         CertRevocCaMBean[] var2;
         if (this._isSet(24)) {
            var2 = (CertRevocCaMBean[])((CertRevocCaMBean[])this._getHelper()._extendArray(this.getCertRevocCas(), CertRevocCaMBean.class, var1));
         } else {
            var2 = new CertRevocCaMBean[]{var1};
         }

         try {
            this.setCertRevocCas(var2);
         } catch (Exception var4) {
            if (var4 instanceof RuntimeException) {
               throw (RuntimeException)var4;
            }

            throw new UndeclaredThrowableException(var4);
         }
      }

   }

   public CertRevocCaMBean[] getCertRevocCas() {
      return this._CertRevocCas;
   }

   public boolean isCertRevocCasSet() {
      return this._isSet(24);
   }

   public void removeCertRevocCa(CertRevocCaMBean var1) {
      this.destroyCertRevocCa(var1);
   }

   public void setCertRevocCas(CertRevocCaMBean[] var1) throws InvalidAttributeValueException {
      Object var4 = var1 == null ? new CertRevocCaMBeanImpl[0] : var1;

      for(int var2 = 0; var2 < ((Object[])var4).length; ++var2) {
         AbstractDescriptorBean var3 = (AbstractDescriptorBean)((Object[])var4)[var2];
         if (this._setParent(var3, this, 24)) {
            this._getReferenceManager().registerBean(var3, false);
            this._postCreate(var3);
         }
      }

      CertRevocCaMBean[] var5 = this._CertRevocCas;
      this._CertRevocCas = (CertRevocCaMBean[])var4;
      this._postSet(24, var5, var4);
   }

   public CertRevocCaMBean createCertRevocCa(String var1) {
      CertRevocCaMBeanImpl var2 = new CertRevocCaMBeanImpl(this, -1);

      try {
         var2.setName(var1);
         this.addCertRevocCa(var2);
         return var2;
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            throw new UndeclaredThrowableException(var4);
         }
      }
   }

   public void destroyCertRevocCa(CertRevocCaMBean var1) {
      try {
         this._checkIsPotentialChild(var1, 24);
         CertRevocCaMBean[] var2 = this.getCertRevocCas();
         CertRevocCaMBean[] var3 = (CertRevocCaMBean[])((CertRevocCaMBean[])this._getHelper()._removeElement(var2, CertRevocCaMBean.class, var1));
         if (var2.length != var3.length) {
            this._preDestroy((AbstractDescriptorBean)var1);

            try {
               AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1;
               if (var4 == null) {
                  return;
               }

               List var5 = this._getReferenceManager().getResolvedReferences(var4);
               if (var5 != null && var5.size() > 0) {
                  throw new BeanRemoveRejectedException(var4, var5);
               }

               this._getReferenceManager().unregisterBean(var4);
               this._markDestroyed(var4);
               this.setCertRevocCas(var3);
            } catch (Exception var6) {
               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               throw new UndeclaredThrowableException(var6);
            }
         }

      } catch (Exception var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new UndeclaredThrowableException(var7);
         }
      }
   }

   public CertRevocCaMBean lookupCertRevocCa(String var1) {
      Iterator var2 = Arrays.asList((Object[])this._CertRevocCas).iterator();

      CertRevocCaMBeanImpl var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (CertRevocCaMBeanImpl)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      CertRevocValidator.validateCertRevoc(this);
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
         var1 = 24;
      }

      try {
         switch (var1) {
            case 24:
               this._CertRevocCas = new CertRevocCaMBean[0];
               if (var2) {
                  break;
               }
            case 20:
               this._CrlCacheRefreshPeriodPercent = CertRevocMBean.DEFAULT_CRL_CACHE_REFRESH_PERIOD_PERCENT;
               if (var2) {
                  break;
               }
            case 16:
               this._CrlCacheType = CertRevocMBean.DEFAULT_CRL_CACHE_TYPE;
               if (var2) {
                  break;
               }
            case 17:
               this._CrlCacheTypeLdapHostname = CertRevocMBean.DEFAULT_CRL_CACHE_TYPE_LDAP_HOST_NAME;
               if (var2) {
                  break;
               }
            case 18:
               this._CrlCacheTypeLdapPort = CertRevocMBean.DEFAULT_CRL_CACHE_TYPE_LDAP_PORT;
               if (var2) {
                  break;
               }
            case 19:
               this._CrlCacheTypeLdapSearchTimeout = CertRevocMBean.DEFAULT_CRL_CACHE_TYPE_LDAP_SEARCH_TIMEOUT;
               if (var2) {
                  break;
               }
            case 22:
               this._CrlDpDownloadTimeout = CertRevocMBean.DEFAULT_CRL_DP_DOWNLOAD_TIMEOUT;
               if (var2) {
                  break;
               }
            case 9:
               this._MethodOrder = CertRevocMBean.DEFAULT_METHOD_ORDER;
               if (var2) {
                  break;
               }
            case 12:
               this._OcspResponseCacheCapacity = CertRevocMBean.DEFAULT_OCSP_RESPONSE_CACHE_CAPACITY;
               if (var2) {
                  break;
               }
            case 13:
               this._OcspResponseCacheRefreshPeriodPercent = CertRevocMBean.DEFAULT_OCSP_RESPONSE_CACHE_REFRESH_PERIOD_PERCENT;
               if (var2) {
                  break;
               }
            case 14:
               this._OcspResponseTimeout = CertRevocMBean.DEFAULT_OCSP_RESPONSE_TIMEOUT;
               if (var2) {
                  break;
               }
            case 15:
               this._OcspTimeTolerance = CertRevocMBean.DEFAULT_OCSP_TIME_TOLERANCE;
               if (var2) {
                  break;
               }
            case 7:
               this._CheckingEnabled = CertRevocMBean.DEFAULT_CHECKING_ENABLED;
               if (var2) {
                  break;
               }
            case 23:
               this._CrlDpBackgroundDownloadEnabled = CertRevocMBean.DEFAULT_CRL_DP_BACKGROUND_DOWNLOAD_ENABLED;
               if (var2) {
                  break;
               }
            case 21:
               this._CrlDpEnabled = CertRevocMBean.DEFAULT_CRL_DP_ENABLED;
               if (var2) {
                  break;
               }
            case 8:
               this._FailOnUnknownRevocStatus = CertRevocMBean.DEFAULT_FAIL_ON_UNKNOWN_REVOC_STATUS;
               if (var2) {
                  break;
               }
            case 10:
               this._OcspNonceEnabled = CertRevocMBean.DEFAULT_OCSP_NONCE_ENABLED;
               if (var2) {
                  break;
               }
            case 11:
               this._OcspResponseCacheEnabled = CertRevocMBean.DEFAULT_OCSP_RESPONSE_CACHE_ENABLED;
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
      return "CertRevoc";
   }

   public void putValue(String var1, Object var2) {
      if (var1.equals("CertRevocCas")) {
         CertRevocCaMBean[] var8 = this._CertRevocCas;
         this._CertRevocCas = (CertRevocCaMBean[])((CertRevocCaMBean[])var2);
         this._postSet(24, var8, this._CertRevocCas);
      } else {
         boolean var6;
         if (var1.equals("CheckingEnabled")) {
            var6 = this._CheckingEnabled;
            this._CheckingEnabled = (Boolean)var2;
            this._postSet(7, var6, this._CheckingEnabled);
         } else {
            int var3;
            if (var1.equals("CrlCacheRefreshPeriodPercent")) {
               var3 = this._CrlCacheRefreshPeriodPercent;
               this._CrlCacheRefreshPeriodPercent = (Integer)var2;
               this._postSet(20, var3, this._CrlCacheRefreshPeriodPercent);
            } else {
               String var7;
               if (var1.equals("CrlCacheType")) {
                  var7 = this._CrlCacheType;
                  this._CrlCacheType = (String)var2;
                  this._postSet(16, var7, this._CrlCacheType);
               } else if (var1.equals("CrlCacheTypeLdapHostname")) {
                  var7 = this._CrlCacheTypeLdapHostname;
                  this._CrlCacheTypeLdapHostname = (String)var2;
                  this._postSet(17, var7, this._CrlCacheTypeLdapHostname);
               } else if (var1.equals("CrlCacheTypeLdapPort")) {
                  var3 = this._CrlCacheTypeLdapPort;
                  this._CrlCacheTypeLdapPort = (Integer)var2;
                  this._postSet(18, var3, this._CrlCacheTypeLdapPort);
               } else if (var1.equals("CrlCacheTypeLdapSearchTimeout")) {
                  var3 = this._CrlCacheTypeLdapSearchTimeout;
                  this._CrlCacheTypeLdapSearchTimeout = (Integer)var2;
                  this._postSet(19, var3, this._CrlCacheTypeLdapSearchTimeout);
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
                  } else if (var1.equals("FailOnUnknownRevocStatus")) {
                     var6 = this._FailOnUnknownRevocStatus;
                     this._FailOnUnknownRevocStatus = (Boolean)var2;
                     this._postSet(8, var6, this._FailOnUnknownRevocStatus);
                  } else if (var1.equals("MethodOrder")) {
                     var7 = this._MethodOrder;
                     this._MethodOrder = (String)var2;
                     this._postSet(9, var7, this._MethodOrder);
                  } else if (var1.equals("OcspNonceEnabled")) {
                     var6 = this._OcspNonceEnabled;
                     this._OcspNonceEnabled = (Boolean)var2;
                     this._postSet(10, var6, this._OcspNonceEnabled);
                  } else if (var1.equals("OcspResponseCacheCapacity")) {
                     var3 = this._OcspResponseCacheCapacity;
                     this._OcspResponseCacheCapacity = (Integer)var2;
                     this._postSet(12, var3, this._OcspResponseCacheCapacity);
                  } else if (var1.equals("OcspResponseCacheEnabled")) {
                     var6 = this._OcspResponseCacheEnabled;
                     this._OcspResponseCacheEnabled = (Boolean)var2;
                     this._postSet(11, var6, this._OcspResponseCacheEnabled);
                  } else if (var1.equals("OcspResponseCacheRefreshPeriodPercent")) {
                     var3 = this._OcspResponseCacheRefreshPeriodPercent;
                     this._OcspResponseCacheRefreshPeriodPercent = (Integer)var2;
                     this._postSet(13, var3, this._OcspResponseCacheRefreshPeriodPercent);
                  } else if (var1.equals("OcspResponseTimeout")) {
                     var5 = this._OcspResponseTimeout;
                     this._OcspResponseTimeout = (Long)var2;
                     this._postSet(14, var5, this._OcspResponseTimeout);
                  } else if (var1.equals("OcspTimeTolerance")) {
                     var3 = this._OcspTimeTolerance;
                     this._OcspTimeTolerance = (Integer)var2;
                     this._postSet(15, var3, this._OcspTimeTolerance);
                  } else {
                     super.putValue(var1, var2);
                  }
               }
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("CertRevocCas")) {
         return this._CertRevocCas;
      } else if (var1.equals("CheckingEnabled")) {
         return new Boolean(this._CheckingEnabled);
      } else if (var1.equals("CrlCacheRefreshPeriodPercent")) {
         return new Integer(this._CrlCacheRefreshPeriodPercent);
      } else if (var1.equals("CrlCacheType")) {
         return this._CrlCacheType;
      } else if (var1.equals("CrlCacheTypeLdapHostname")) {
         return this._CrlCacheTypeLdapHostname;
      } else if (var1.equals("CrlCacheTypeLdapPort")) {
         return new Integer(this._CrlCacheTypeLdapPort);
      } else if (var1.equals("CrlCacheTypeLdapSearchTimeout")) {
         return new Integer(this._CrlCacheTypeLdapSearchTimeout);
      } else if (var1.equals("CrlDpBackgroundDownloadEnabled")) {
         return new Boolean(this._CrlDpBackgroundDownloadEnabled);
      } else if (var1.equals("CrlDpDownloadTimeout")) {
         return new Long(this._CrlDpDownloadTimeout);
      } else if (var1.equals("CrlDpEnabled")) {
         return new Boolean(this._CrlDpEnabled);
      } else if (var1.equals("FailOnUnknownRevocStatus")) {
         return new Boolean(this._FailOnUnknownRevocStatus);
      } else if (var1.equals("MethodOrder")) {
         return this._MethodOrder;
      } else if (var1.equals("OcspNonceEnabled")) {
         return new Boolean(this._OcspNonceEnabled);
      } else if (var1.equals("OcspResponseCacheCapacity")) {
         return new Integer(this._OcspResponseCacheCapacity);
      } else if (var1.equals("OcspResponseCacheEnabled")) {
         return new Boolean(this._OcspResponseCacheEnabled);
      } else if (var1.equals("OcspResponseCacheRefreshPeriodPercent")) {
         return new Integer(this._OcspResponseCacheRefreshPeriodPercent);
      } else if (var1.equals("OcspResponseTimeout")) {
         return new Long(this._OcspResponseTimeout);
      } else {
         return var1.equals("OcspTimeTolerance") ? new Integer(this._OcspTimeTolerance) : super.getValue(var1);
      }
   }

   public static void validateGeneration() {
      String[] var0;
      try {
         var0 = new String[]{CertRevocMBean.CRL_CACHE_TYPE_FILE, CertRevocMBean.CRL_CACHE_TYPE_LDAP};
         weblogic.descriptor.beangen.LegalChecks.checkInEnum("CrlCacheType", CertRevocMBean.DEFAULT_CRL_CACHE_TYPE, var0);
      } catch (IllegalArgumentException var2) {
         throw new DescriptorValidateException("Default value for a property  should be one of the legal values. Refer annotation legalValues on property CrlCacheType in CertRevocMBean" + var2.getMessage());
      }

      try {
         var0 = new String[]{CertRevocMBean.METHOD_OCSP, CertRevocMBean.METHOD_CRL, CertRevocMBean.METHOD_OCSP_THEN_CRL, CertRevocMBean.METHOD_CRL_THEN_OCSP};
         weblogic.descriptor.beangen.LegalChecks.checkInEnum("MethodOrder", CertRevocMBean.DEFAULT_METHOD_ORDER, var0);
      } catch (IllegalArgumentException var1) {
         throw new DescriptorValidateException("Default value for a property  should be one of the legal values. Refer annotation legalValues on property MethodOrder in CertRevocMBean" + var1.getMessage());
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 12:
               if (var1.equals("method-order")) {
                  return 9;
               }
               break;
            case 13:
               if (var1.equals("cert-revoc-ca")) {
                  return 24;
               }
               break;
            case 14:
               if (var1.equals("crl-cache-type")) {
                  return 16;
               }

               if (var1.equals("crl-dp-enabled")) {
                  return 21;
               }
            case 15:
            case 17:
            case 20:
            case 22:
            case 25:
            case 26:
            case 29:
            case 30:
            case 31:
            case 33:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            default:
               break;
            case 16:
               if (var1.equals("checking-enabled")) {
                  return 7;
               }
               break;
            case 18:
               if (var1.equals("ocsp-nonce-enabled")) {
                  return 10;
               }
               break;
            case 19:
               if (var1.equals("ocsp-time-tolerance")) {
                  return 15;
               }
               break;
            case 21:
               if (var1.equals("ocsp-response-timeout")) {
                  return 14;
               }
               break;
            case 23:
               if (var1.equals("crl-dp-download-timeout")) {
                  return 22;
               }
               break;
            case 24:
               if (var1.equals("crl-cache-type-ldap-port")) {
                  return 18;
               }
               break;
            case 27:
               if (var1.equals("ocsp-response-cache-enabled")) {
                  return 11;
               }
               break;
            case 28:
               if (var1.equals("crl-cache-type-ldap-hostname")) {
                  return 17;
               }

               if (var1.equals("ocsp-response-cache-capacity")) {
                  return 12;
               }

               if (var1.equals("fail-on-unknown-revoc-status")) {
                  return 8;
               }
               break;
            case 32:
               if (var1.equals("crl-cache-refresh-period-percent")) {
                  return 20;
               }
               break;
            case 34:
               if (var1.equals("crl-cache-type-ldap-search-timeout")) {
                  return 19;
               }

               if (var1.equals("crl-dp-background-download-enabled")) {
                  return 23;
               }
               break;
            case 42:
               if (var1.equals("ocsp-response-cache-refresh-period-percent")) {
                  return 13;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 24:
               return new CertRevocCaMBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 7:
               return "checking-enabled";
            case 8:
               return "fail-on-unknown-revoc-status";
            case 9:
               return "method-order";
            case 10:
               return "ocsp-nonce-enabled";
            case 11:
               return "ocsp-response-cache-enabled";
            case 12:
               return "ocsp-response-cache-capacity";
            case 13:
               return "ocsp-response-cache-refresh-period-percent";
            case 14:
               return "ocsp-response-timeout";
            case 15:
               return "ocsp-time-tolerance";
            case 16:
               return "crl-cache-type";
            case 17:
               return "crl-cache-type-ldap-hostname";
            case 18:
               return "crl-cache-type-ldap-port";
            case 19:
               return "crl-cache-type-ldap-search-timeout";
            case 20:
               return "crl-cache-refresh-period-percent";
            case 21:
               return "crl-dp-enabled";
            case 22:
               return "crl-dp-download-timeout";
            case 23:
               return "crl-dp-background-download-enabled";
            case 24:
               return "cert-revoc-ca";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 24:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 24:
               return true;
            default:
               return super.isBean(var1);
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
      private CertRevocMBeanImpl bean;

      protected Helper(CertRevocMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "CheckingEnabled";
            case 8:
               return "FailOnUnknownRevocStatus";
            case 9:
               return "MethodOrder";
            case 10:
               return "OcspNonceEnabled";
            case 11:
               return "OcspResponseCacheEnabled";
            case 12:
               return "OcspResponseCacheCapacity";
            case 13:
               return "OcspResponseCacheRefreshPeriodPercent";
            case 14:
               return "OcspResponseTimeout";
            case 15:
               return "OcspTimeTolerance";
            case 16:
               return "CrlCacheType";
            case 17:
               return "CrlCacheTypeLdapHostname";
            case 18:
               return "CrlCacheTypeLdapPort";
            case 19:
               return "CrlCacheTypeLdapSearchTimeout";
            case 20:
               return "CrlCacheRefreshPeriodPercent";
            case 21:
               return "CrlDpEnabled";
            case 22:
               return "CrlDpDownloadTimeout";
            case 23:
               return "CrlDpBackgroundDownloadEnabled";
            case 24:
               return "CertRevocCas";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("CertRevocCas")) {
            return 24;
         } else if (var1.equals("CrlCacheRefreshPeriodPercent")) {
            return 20;
         } else if (var1.equals("CrlCacheType")) {
            return 16;
         } else if (var1.equals("CrlCacheTypeLdapHostname")) {
            return 17;
         } else if (var1.equals("CrlCacheTypeLdapPort")) {
            return 18;
         } else if (var1.equals("CrlCacheTypeLdapSearchTimeout")) {
            return 19;
         } else if (var1.equals("CrlDpDownloadTimeout")) {
            return 22;
         } else if (var1.equals("MethodOrder")) {
            return 9;
         } else if (var1.equals("OcspResponseCacheCapacity")) {
            return 12;
         } else if (var1.equals("OcspResponseCacheRefreshPeriodPercent")) {
            return 13;
         } else if (var1.equals("OcspResponseTimeout")) {
            return 14;
         } else if (var1.equals("OcspTimeTolerance")) {
            return 15;
         } else if (var1.equals("CheckingEnabled")) {
            return 7;
         } else if (var1.equals("CrlDpBackgroundDownloadEnabled")) {
            return 23;
         } else if (var1.equals("CrlDpEnabled")) {
            return 21;
         } else if (var1.equals("FailOnUnknownRevocStatus")) {
            return 8;
         } else if (var1.equals("OcspNonceEnabled")) {
            return 10;
         } else {
            return var1.equals("OcspResponseCacheEnabled") ? 11 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         var1.add(new ArrayIterator(this.bean.getCertRevocCas()));
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
            var5 = 0L;

            for(int var7 = 0; var7 < this.bean.getCertRevocCas().length; ++var7) {
               var5 ^= this.computeChildHashValue(this.bean.getCertRevocCas()[var7]);
            }

            if (var5 != 0L) {
               var2.append(String.valueOf(var5));
            }

            if (this.bean.isCrlCacheRefreshPeriodPercentSet()) {
               var2.append("CrlCacheRefreshPeriodPercent");
               var2.append(String.valueOf(this.bean.getCrlCacheRefreshPeriodPercent()));
            }

            if (this.bean.isCrlCacheTypeSet()) {
               var2.append("CrlCacheType");
               var2.append(String.valueOf(this.bean.getCrlCacheType()));
            }

            if (this.bean.isCrlCacheTypeLdapHostnameSet()) {
               var2.append("CrlCacheTypeLdapHostname");
               var2.append(String.valueOf(this.bean.getCrlCacheTypeLdapHostname()));
            }

            if (this.bean.isCrlCacheTypeLdapPortSet()) {
               var2.append("CrlCacheTypeLdapPort");
               var2.append(String.valueOf(this.bean.getCrlCacheTypeLdapPort()));
            }

            if (this.bean.isCrlCacheTypeLdapSearchTimeoutSet()) {
               var2.append("CrlCacheTypeLdapSearchTimeout");
               var2.append(String.valueOf(this.bean.getCrlCacheTypeLdapSearchTimeout()));
            }

            if (this.bean.isCrlDpDownloadTimeoutSet()) {
               var2.append("CrlDpDownloadTimeout");
               var2.append(String.valueOf(this.bean.getCrlDpDownloadTimeout()));
            }

            if (this.bean.isMethodOrderSet()) {
               var2.append("MethodOrder");
               var2.append(String.valueOf(this.bean.getMethodOrder()));
            }

            if (this.bean.isOcspResponseCacheCapacitySet()) {
               var2.append("OcspResponseCacheCapacity");
               var2.append(String.valueOf(this.bean.getOcspResponseCacheCapacity()));
            }

            if (this.bean.isOcspResponseCacheRefreshPeriodPercentSet()) {
               var2.append("OcspResponseCacheRefreshPeriodPercent");
               var2.append(String.valueOf(this.bean.getOcspResponseCacheRefreshPeriodPercent()));
            }

            if (this.bean.isOcspResponseTimeoutSet()) {
               var2.append("OcspResponseTimeout");
               var2.append(String.valueOf(this.bean.getOcspResponseTimeout()));
            }

            if (this.bean.isOcspTimeToleranceSet()) {
               var2.append("OcspTimeTolerance");
               var2.append(String.valueOf(this.bean.getOcspTimeTolerance()));
            }

            if (this.bean.isCheckingEnabledSet()) {
               var2.append("CheckingEnabled");
               var2.append(String.valueOf(this.bean.isCheckingEnabled()));
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
         } catch (Exception var8) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var8);
         }
      }

      protected void computeDiff(AbstractDescriptorBean var1) {
         try {
            super.computeDiff(var1);
            CertRevocMBeanImpl var2 = (CertRevocMBeanImpl)var1;
            this.computeChildDiff("CertRevocCas", this.bean.getCertRevocCas(), var2.getCertRevocCas(), true);
            this.computeDiff("CrlCacheRefreshPeriodPercent", this.bean.getCrlCacheRefreshPeriodPercent(), var2.getCrlCacheRefreshPeriodPercent(), true);
            this.computeDiff("CrlCacheType", this.bean.getCrlCacheType(), var2.getCrlCacheType(), false);
            this.computeDiff("CrlCacheTypeLdapHostname", this.bean.getCrlCacheTypeLdapHostname(), var2.getCrlCacheTypeLdapHostname(), false);
            this.computeDiff("CrlCacheTypeLdapPort", this.bean.getCrlCacheTypeLdapPort(), var2.getCrlCacheTypeLdapPort(), false);
            this.computeDiff("CrlCacheTypeLdapSearchTimeout", this.bean.getCrlCacheTypeLdapSearchTimeout(), var2.getCrlCacheTypeLdapSearchTimeout(), true);
            this.computeDiff("CrlDpDownloadTimeout", this.bean.getCrlDpDownloadTimeout(), var2.getCrlDpDownloadTimeout(), true);
            this.computeDiff("MethodOrder", this.bean.getMethodOrder(), var2.getMethodOrder(), true);
            this.computeDiff("OcspResponseCacheCapacity", this.bean.getOcspResponseCacheCapacity(), var2.getOcspResponseCacheCapacity(), true);
            this.computeDiff("OcspResponseCacheRefreshPeriodPercent", this.bean.getOcspResponseCacheRefreshPeriodPercent(), var2.getOcspResponseCacheRefreshPeriodPercent(), true);
            this.computeDiff("OcspResponseTimeout", this.bean.getOcspResponseTimeout(), var2.getOcspResponseTimeout(), true);
            this.computeDiff("OcspTimeTolerance", this.bean.getOcspTimeTolerance(), var2.getOcspTimeTolerance(), true);
            this.computeDiff("CheckingEnabled", this.bean.isCheckingEnabled(), var2.isCheckingEnabled(), true);
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
            CertRevocMBeanImpl var3 = (CertRevocMBeanImpl)var1.getSourceBean();
            CertRevocMBeanImpl var4 = (CertRevocMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("CertRevocCas")) {
                  if (var6 == 2) {
                     var2.resetAddedObject(this.createCopy((AbstractDescriptorBean)var2.getAddedObject()));
                     var3.addCertRevocCa((CertRevocCaMBean)var2.getAddedObject());
                  } else {
                     if (var6 != 3) {
                        throw new AssertionError("Invalid type: " + var6);
                     }

                     var3.removeCertRevocCa((CertRevocCaMBean)var2.getRemovedObject());
                  }

                  if (var3.getCertRevocCas() == null || var3.getCertRevocCas().length == 0) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 24);
                  }
               } else if (var5.equals("CrlCacheRefreshPeriodPercent")) {
                  var3.setCrlCacheRefreshPeriodPercent(var4.getCrlCacheRefreshPeriodPercent());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 20);
               } else if (var5.equals("CrlCacheType")) {
                  var3.setCrlCacheType(var4.getCrlCacheType());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
               } else if (var5.equals("CrlCacheTypeLdapHostname")) {
                  var3.setCrlCacheTypeLdapHostname(var4.getCrlCacheTypeLdapHostname());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 17);
               } else if (var5.equals("CrlCacheTypeLdapPort")) {
                  var3.setCrlCacheTypeLdapPort(var4.getCrlCacheTypeLdapPort());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 18);
               } else if (var5.equals("CrlCacheTypeLdapSearchTimeout")) {
                  var3.setCrlCacheTypeLdapSearchTimeout(var4.getCrlCacheTypeLdapSearchTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 19);
               } else if (var5.equals("CrlDpDownloadTimeout")) {
                  var3.setCrlDpDownloadTimeout(var4.getCrlDpDownloadTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 22);
               } else if (var5.equals("MethodOrder")) {
                  var3.setMethodOrder(var4.getMethodOrder());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("OcspResponseCacheCapacity")) {
                  var3.setOcspResponseCacheCapacity(var4.getOcspResponseCacheCapacity());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("OcspResponseCacheRefreshPeriodPercent")) {
                  var3.setOcspResponseCacheRefreshPeriodPercent(var4.getOcspResponseCacheRefreshPeriodPercent());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("OcspResponseTimeout")) {
                  var3.setOcspResponseTimeout(var4.getOcspResponseTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("OcspTimeTolerance")) {
                  var3.setOcspTimeTolerance(var4.getOcspTimeTolerance());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("CheckingEnabled")) {
                  var3.setCheckingEnabled(var4.isCheckingEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("CrlDpBackgroundDownloadEnabled")) {
                  var3.setCrlDpBackgroundDownloadEnabled(var4.isCrlDpBackgroundDownloadEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 23);
               } else if (var5.equals("CrlDpEnabled")) {
                  var3.setCrlDpEnabled(var4.isCrlDpEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 21);
               } else if (var5.equals("FailOnUnknownRevocStatus")) {
                  var3.setFailOnUnknownRevocStatus(var4.isFailOnUnknownRevocStatus());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("OcspNonceEnabled")) {
                  var3.setOcspNonceEnabled(var4.isOcspNonceEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("OcspResponseCacheEnabled")) {
                  var3.setOcspResponseCacheEnabled(var4.isOcspResponseCacheEnabled());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
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
            CertRevocMBeanImpl var5 = (CertRevocMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("CertRevocCas")) && this.bean.isCertRevocCasSet() && !var5._isSet(24)) {
               CertRevocCaMBean[] var6 = this.bean.getCertRevocCas();
               CertRevocCaMBean[] var7 = new CertRevocCaMBean[var6.length];

               for(int var8 = 0; var8 < var7.length; ++var8) {
                  var7[var8] = (CertRevocCaMBean)((CertRevocCaMBean)this.createCopy((AbstractDescriptorBean)var6[var8], var2));
               }

               var5.setCertRevocCas(var7);
            }

            if ((var3 == null || !var3.contains("CrlCacheRefreshPeriodPercent")) && this.bean.isCrlCacheRefreshPeriodPercentSet()) {
               var5.setCrlCacheRefreshPeriodPercent(this.bean.getCrlCacheRefreshPeriodPercent());
            }

            if ((var3 == null || !var3.contains("CrlCacheType")) && this.bean.isCrlCacheTypeSet()) {
               var5.setCrlCacheType(this.bean.getCrlCacheType());
            }

            if ((var3 == null || !var3.contains("CrlCacheTypeLdapHostname")) && this.bean.isCrlCacheTypeLdapHostnameSet()) {
               var5.setCrlCacheTypeLdapHostname(this.bean.getCrlCacheTypeLdapHostname());
            }

            if ((var3 == null || !var3.contains("CrlCacheTypeLdapPort")) && this.bean.isCrlCacheTypeLdapPortSet()) {
               var5.setCrlCacheTypeLdapPort(this.bean.getCrlCacheTypeLdapPort());
            }

            if ((var3 == null || !var3.contains("CrlCacheTypeLdapSearchTimeout")) && this.bean.isCrlCacheTypeLdapSearchTimeoutSet()) {
               var5.setCrlCacheTypeLdapSearchTimeout(this.bean.getCrlCacheTypeLdapSearchTimeout());
            }

            if ((var3 == null || !var3.contains("CrlDpDownloadTimeout")) && this.bean.isCrlDpDownloadTimeoutSet()) {
               var5.setCrlDpDownloadTimeout(this.bean.getCrlDpDownloadTimeout());
            }

            if ((var3 == null || !var3.contains("MethodOrder")) && this.bean.isMethodOrderSet()) {
               var5.setMethodOrder(this.bean.getMethodOrder());
            }

            if ((var3 == null || !var3.contains("OcspResponseCacheCapacity")) && this.bean.isOcspResponseCacheCapacitySet()) {
               var5.setOcspResponseCacheCapacity(this.bean.getOcspResponseCacheCapacity());
            }

            if ((var3 == null || !var3.contains("OcspResponseCacheRefreshPeriodPercent")) && this.bean.isOcspResponseCacheRefreshPeriodPercentSet()) {
               var5.setOcspResponseCacheRefreshPeriodPercent(this.bean.getOcspResponseCacheRefreshPeriodPercent());
            }

            if ((var3 == null || !var3.contains("OcspResponseTimeout")) && this.bean.isOcspResponseTimeoutSet()) {
               var5.setOcspResponseTimeout(this.bean.getOcspResponseTimeout());
            }

            if ((var3 == null || !var3.contains("OcspTimeTolerance")) && this.bean.isOcspTimeToleranceSet()) {
               var5.setOcspTimeTolerance(this.bean.getOcspTimeTolerance());
            }

            if ((var3 == null || !var3.contains("CheckingEnabled")) && this.bean.isCheckingEnabledSet()) {
               var5.setCheckingEnabled(this.bean.isCheckingEnabled());
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
         } catch (RuntimeException var9) {
            throw var9;
         } catch (Exception var10) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var10);
         }
      }

      protected void inferSubTree(Class var1, Object var2) {
         super.inferSubTree(var1, var2);
         Object var3 = null;
         this.inferSubTree(this.bean.getCertRevocCas(), var1, var2);
      }
   }
}
