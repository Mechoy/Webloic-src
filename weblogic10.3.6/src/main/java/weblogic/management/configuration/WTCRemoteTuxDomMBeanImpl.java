package weblogic.management.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorValidateException;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.CombinedIterator;

public class WTCRemoteTuxDomMBeanImpl extends ConfigurationMBeanImpl implements WTCRemoteTuxDomMBean, Serializable {
   private String _AccessPoint;
   private String _AccessPointId;
   private String _AclPolicy;
   private boolean _AllowAnonymous;
   private String _AppKey;
   private int _CmpLimit;
   private String _ConnPrincipalName;
   private String _ConnectionPolicy;
   private String _CredentialPolicy;
   private String _CustomAppKeyClass;
   private String _CustomAppKeyClassParam;
   private int _DefaultAppKey;
   private String _FederationName;
   private String _FederationURL;
   private int _KeepAlive;
   private int _KeepAliveWait;
   private String _LocalAccessPoint;
   private String _MaxEncryptBits;
   private long _MaxRetries;
   private String _MinEncryptBits;
   private String _NWAddr;
   private long _RetryInterval;
   private String _TpUsrFile;
   private String _TuxedoGidKw;
   private String _TuxedoUidKw;
   private static SchemaHelper2 _schemaHelper;

   public WTCRemoteTuxDomMBeanImpl() {
      this._initializeProperty(-1);
   }

   public WTCRemoteTuxDomMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public void setAccessPoint(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("AccessPoint", var1);
      String var2 = this._AccessPoint;
      this._AccessPoint = var1;
      this._postSet(7, var2, var1);
   }

   public String getAccessPoint() {
      return this._AccessPoint;
   }

   public boolean isAccessPointSet() {
      return this._isSet(7);
   }

   public void setAccessPointId(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("AccessPointId", var1);
      String var2 = this._AccessPointId;
      this._AccessPointId = var1;
      this._postSet(8, var2, var1);
   }

   public String getAccessPointId() {
      return this._AccessPointId;
   }

   public boolean isAccessPointIdSet() {
      return this._isSet(8);
   }

   public void setConnectionPolicy(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"ON_DEMAND", "ON_STARTUP", "INCOMING_ONLY", "LOCAL"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("ConnectionPolicy", var1, var2);
      String var3 = this._ConnectionPolicy;
      this._ConnectionPolicy = var1;
      this._postSet(9, var3, var1);
   }

   public String getConnectionPolicy() {
      return this._ConnectionPolicy;
   }

   public boolean isConnectionPolicySet() {
      return this._isSet(9);
   }

   public void setAclPolicy(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"GLOBAL", "LOCAL"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("AclPolicy", var1, var2);
      String var3 = this._AclPolicy;
      this._AclPolicy = var1;
      this._postSet(10, var3, var1);
   }

   public String getAclPolicy() {
      return this._AclPolicy;
   }

   public boolean isAclPolicySet() {
      return this._isSet(10);
   }

   public void setCredentialPolicy(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"GLOBAL", "LOCAL"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("CredentialPolicy", var1, var2);
      String var3 = this._CredentialPolicy;
      this._CredentialPolicy = var1;
      this._postSet(11, var3, var1);
   }

   public String getCredentialPolicy() {
      return this._CredentialPolicy;
   }

   public boolean isCredentialPolicySet() {
      return this._isSet(11);
   }

   public void setTpUsrFile(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._TpUsrFile;
      this._TpUsrFile = var1;
      this._postSet(12, var2, var1);
   }

   public String getTpUsrFile() {
      return this._TpUsrFile;
   }

   public boolean isTpUsrFileSet() {
      return this._isSet(12);
   }

   public void setLocalAccessPoint(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("LocalAccessPoint", var1);
      String var2 = this._LocalAccessPoint;
      this._LocalAccessPoint = var1;
      this._postSet(13, var2, var1);
   }

   public String getLocalAccessPoint() {
      return this._LocalAccessPoint;
   }

   public boolean isLocalAccessPointSet() {
      return this._isSet(13);
   }

   public void setConnPrincipalName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ConnPrincipalName;
      this._ConnPrincipalName = var1;
      this._postSet(14, var2, var1);
   }

   public String getConnPrincipalName() {
      return this._ConnPrincipalName;
   }

   public boolean isConnPrincipalNameSet() {
      return this._isSet(14);
   }

   public void setRetryInterval(long var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("RetryInterval", var1, -1L, 2147483647L);
      long var3 = this._RetryInterval;
      this._RetryInterval = var1;
      this._postSet(15, var3, var1);
   }

   public long getRetryInterval() {
      return this._RetryInterval;
   }

   public boolean isRetryIntervalSet() {
      return this._isSet(15);
   }

   public void setMaxRetries(long var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaxRetries", var1, -1L, Long.MAX_VALUE);
      long var3 = this._MaxRetries;
      this._MaxRetries = var1;
      this._postSet(16, var3, var1);
   }

   public long getMaxRetries() {
      return this._MaxRetries;
   }

   public boolean isMaxRetriesSet() {
      return this._isSet(16);
   }

   public void setNWAddr(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._NWAddr;
      this._NWAddr = var1;
      this._postSet(17, var2, var1);
   }

   public String getNWAddr() {
      return this._NWAddr;
   }

   public boolean isNWAddrSet() {
      return this._isSet(17);
   }

   public void setFederationURL(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._FederationURL;
      this._FederationURL = var1;
      this._postSet(18, var2, var1);
   }

   public String getFederationURL() {
      return this._FederationURL;
   }

   public boolean isFederationURLSet() {
      return this._isSet(18);
   }

   public void setFederationName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._FederationName;
      this._FederationName = var1;
      this._postSet(19, var2, var1);
   }

   public String getFederationName() {
      return this._FederationName;
   }

   public boolean isFederationNameSet() {
      return this._isSet(19);
   }

   public void setCmpLimit(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("CmpLimit", (long)var1, 0L, 2147483647L);
      int var2 = this._CmpLimit;
      this._CmpLimit = var1;
      this._postSet(20, var2, var1);
   }

   public int getCmpLimit() {
      return this._CmpLimit;
   }

   public boolean isCmpLimitSet() {
      return this._isSet(20);
   }

   public void setMinEncryptBits(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"0", "40", "56", "128"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("MinEncryptBits", var1, var2);
      String var3 = this._MinEncryptBits;
      this._MinEncryptBits = var1;
      this._postSet(21, var3, var1);
   }

   public String getMinEncryptBits() {
      return this._MinEncryptBits;
   }

   public boolean isMinEncryptBitsSet() {
      return this._isSet(21);
   }

   public void setMaxEncryptBits(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"0", "40", "56", "128"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("MaxEncryptBits", var1, var2);
      String var3 = this._MaxEncryptBits;
      this._MaxEncryptBits = var1;
      this._postSet(22, var3, var1);
   }

   public String getMaxEncryptBits() {
      return this._MaxEncryptBits;
   }

   public boolean isMaxEncryptBitsSet() {
      return this._isSet(22);
   }

   public void setAppKey(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"TpUsrFile", "LDAP", "Custom"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("AppKey", var1, var2);
      String var3 = this._AppKey;
      this._AppKey = var1;
      this._postSet(23, var3, var1);
   }

   public String getAppKey() {
      return this._AppKey;
   }

   public boolean isAppKeySet() {
      return this._isSet(23);
   }

   public void setAllowAnonymous(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._AllowAnonymous;
      this._AllowAnonymous = var1;
      this._postSet(24, var2, var1);
   }

   public boolean getAllowAnonymous() {
      return this._AllowAnonymous;
   }

   public boolean isAllowAnonymousSet() {
      return this._isSet(24);
   }

   public void setDefaultAppKey(int var1) throws InvalidAttributeValueException {
      int var2 = this._DefaultAppKey;
      this._DefaultAppKey = var1;
      this._postSet(25, var2, var1);
   }

   public int getDefaultAppKey() {
      return this._DefaultAppKey;
   }

   public boolean isDefaultAppKeySet() {
      return this._isSet(25);
   }

   public void setTuxedoUidKw(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._TuxedoUidKw;
      this._TuxedoUidKw = var1;
      this._postSet(26, var2, var1);
   }

   public String getTuxedoUidKw() {
      return this._TuxedoUidKw;
   }

   public boolean isTuxedoUidKwSet() {
      return this._isSet(26);
   }

   public void setTuxedoGidKw(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._TuxedoGidKw;
      this._TuxedoGidKw = var1;
      this._postSet(27, var2, var1);
   }

   public String getTuxedoGidKw() {
      return this._TuxedoGidKw;
   }

   public boolean isTuxedoGidKwSet() {
      return this._isSet(27);
   }

   public void setCustomAppKeyClass(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._CustomAppKeyClass;
      this._CustomAppKeyClass = var1;
      this._postSet(28, var2, var1);
   }

   public String getCustomAppKeyClass() {
      return this._CustomAppKeyClass;
   }

   public boolean isCustomAppKeyClassSet() {
      return this._isSet(28);
   }

   public void setCustomAppKeyClassParam(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._CustomAppKeyClassParam;
      this._CustomAppKeyClassParam = var1;
      this._postSet(29, var2, var1);
   }

   public String getCustomAppKeyClassParam() {
      return this._CustomAppKeyClassParam;
   }

   public boolean isCustomAppKeyClassParamSet() {
      return this._isSet(29);
   }

   public void setKeepAlive(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("KeepAlive", (long)var1, -1L, 2147483647L);
      int var2 = this._KeepAlive;
      this._KeepAlive = var1;
      this._postSet(30, var2, var1);
   }

   public int getKeepAlive() {
      return this._KeepAlive;
   }

   public boolean isKeepAliveSet() {
      return this._isSet(30);
   }

   public void setKeepAliveWait(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("KeepAliveWait", (long)var1, 0L, 2147483647L);
      int var2 = this._KeepAliveWait;
      this._KeepAliveWait = var1;
      this._postSet(31, var2, var1);
   }

   public int getKeepAliveWait() {
      return this._KeepAliveWait;
   }

   public boolean isKeepAliveWaitSet() {
      return this._isSet(31);
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      WTCLegalHelper.validateWTCRemoteTuxDom(this);
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
         var1 = 7;
      }

      try {
         switch (var1) {
            case 7:
               this._AccessPoint = "myRAP";
               if (var2) {
                  break;
               }
            case 8:
               this._AccessPointId = "myRAPId";
               if (var2) {
                  break;
               }
            case 10:
               this._AclPolicy = "LOCAL";
               if (var2) {
                  break;
               }
            case 24:
               this._AllowAnonymous = false;
               if (var2) {
                  break;
               }
            case 23:
               this._AppKey = "TpUsrFile";
               if (var2) {
                  break;
               }
            case 20:
               this._CmpLimit = Integer.MAX_VALUE;
               if (var2) {
                  break;
               }
            case 14:
               this._ConnPrincipalName = null;
               if (var2) {
                  break;
               }
            case 9:
               this._ConnectionPolicy = "LOCAL";
               if (var2) {
                  break;
               }
            case 11:
               this._CredentialPolicy = "LOCAL";
               if (var2) {
                  break;
               }
            case 28:
               this._CustomAppKeyClass = null;
               if (var2) {
                  break;
               }
            case 29:
               this._CustomAppKeyClassParam = null;
               if (var2) {
                  break;
               }
            case 25:
               this._DefaultAppKey = -1;
               if (var2) {
                  break;
               }
            case 19:
               this._FederationName = null;
               if (var2) {
                  break;
               }
            case 18:
               this._FederationURL = null;
               if (var2) {
                  break;
               }
            case 30:
               this._KeepAlive = 0;
               if (var2) {
                  break;
               }
            case 31:
               this._KeepAliveWait = 0;
               if (var2) {
                  break;
               }
            case 13:
               this._LocalAccessPoint = "myLAP";
               if (var2) {
                  break;
               }
            case 22:
               this._MaxEncryptBits = "128";
               if (var2) {
                  break;
               }
            case 16:
               this._MaxRetries = -1L;
               if (var2) {
                  break;
               }
            case 21:
               this._MinEncryptBits = "0";
               if (var2) {
                  break;
               }
            case 17:
               this._NWAddr = "//localhost:8902";
               if (var2) {
                  break;
               }
            case 15:
               this._RetryInterval = -1L;
               if (var2) {
                  break;
               }
            case 12:
               this._TpUsrFile = null;
               if (var2) {
                  break;
               }
            case 27:
               this._TuxedoGidKw = "TUXEDO_GID";
               if (var2) {
                  break;
               }
            case 26:
               this._TuxedoUidKw = "TUXEDO_UID";
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
      return "WTCRemoteTuxDom";
   }

   public void putValue(String var1, Object var2) {
      String var3;
      if (var1.equals("AccessPoint")) {
         var3 = this._AccessPoint;
         this._AccessPoint = (String)var2;
         this._postSet(7, var3, this._AccessPoint);
      } else if (var1.equals("AccessPointId")) {
         var3 = this._AccessPointId;
         this._AccessPointId = (String)var2;
         this._postSet(8, var3, this._AccessPointId);
      } else if (var1.equals("AclPolicy")) {
         var3 = this._AclPolicy;
         this._AclPolicy = (String)var2;
         this._postSet(10, var3, this._AclPolicy);
      } else if (var1.equals("AllowAnonymous")) {
         boolean var7 = this._AllowAnonymous;
         this._AllowAnonymous = (Boolean)var2;
         this._postSet(24, var7, this._AllowAnonymous);
      } else if (var1.equals("AppKey")) {
         var3 = this._AppKey;
         this._AppKey = (String)var2;
         this._postSet(23, var3, this._AppKey);
      } else {
         int var6;
         if (var1.equals("CmpLimit")) {
            var6 = this._CmpLimit;
            this._CmpLimit = (Integer)var2;
            this._postSet(20, var6, this._CmpLimit);
         } else if (var1.equals("ConnPrincipalName")) {
            var3 = this._ConnPrincipalName;
            this._ConnPrincipalName = (String)var2;
            this._postSet(14, var3, this._ConnPrincipalName);
         } else if (var1.equals("ConnectionPolicy")) {
            var3 = this._ConnectionPolicy;
            this._ConnectionPolicy = (String)var2;
            this._postSet(9, var3, this._ConnectionPolicy);
         } else if (var1.equals("CredentialPolicy")) {
            var3 = this._CredentialPolicy;
            this._CredentialPolicy = (String)var2;
            this._postSet(11, var3, this._CredentialPolicy);
         } else if (var1.equals("CustomAppKeyClass")) {
            var3 = this._CustomAppKeyClass;
            this._CustomAppKeyClass = (String)var2;
            this._postSet(28, var3, this._CustomAppKeyClass);
         } else if (var1.equals("CustomAppKeyClassParam")) {
            var3 = this._CustomAppKeyClassParam;
            this._CustomAppKeyClassParam = (String)var2;
            this._postSet(29, var3, this._CustomAppKeyClassParam);
         } else if (var1.equals("DefaultAppKey")) {
            var6 = this._DefaultAppKey;
            this._DefaultAppKey = (Integer)var2;
            this._postSet(25, var6, this._DefaultAppKey);
         } else if (var1.equals("FederationName")) {
            var3 = this._FederationName;
            this._FederationName = (String)var2;
            this._postSet(19, var3, this._FederationName);
         } else if (var1.equals("FederationURL")) {
            var3 = this._FederationURL;
            this._FederationURL = (String)var2;
            this._postSet(18, var3, this._FederationURL);
         } else if (var1.equals("KeepAlive")) {
            var6 = this._KeepAlive;
            this._KeepAlive = (Integer)var2;
            this._postSet(30, var6, this._KeepAlive);
         } else if (var1.equals("KeepAliveWait")) {
            var6 = this._KeepAliveWait;
            this._KeepAliveWait = (Integer)var2;
            this._postSet(31, var6, this._KeepAliveWait);
         } else if (var1.equals("LocalAccessPoint")) {
            var3 = this._LocalAccessPoint;
            this._LocalAccessPoint = (String)var2;
            this._postSet(13, var3, this._LocalAccessPoint);
         } else if (var1.equals("MaxEncryptBits")) {
            var3 = this._MaxEncryptBits;
            this._MaxEncryptBits = (String)var2;
            this._postSet(22, var3, this._MaxEncryptBits);
         } else {
            long var5;
            if (var1.equals("MaxRetries")) {
               var5 = this._MaxRetries;
               this._MaxRetries = (Long)var2;
               this._postSet(16, var5, this._MaxRetries);
            } else if (var1.equals("MinEncryptBits")) {
               var3 = this._MinEncryptBits;
               this._MinEncryptBits = (String)var2;
               this._postSet(21, var3, this._MinEncryptBits);
            } else if (var1.equals("NWAddr")) {
               var3 = this._NWAddr;
               this._NWAddr = (String)var2;
               this._postSet(17, var3, this._NWAddr);
            } else if (var1.equals("RetryInterval")) {
               var5 = this._RetryInterval;
               this._RetryInterval = (Long)var2;
               this._postSet(15, var5, this._RetryInterval);
            } else if (var1.equals("TpUsrFile")) {
               var3 = this._TpUsrFile;
               this._TpUsrFile = (String)var2;
               this._postSet(12, var3, this._TpUsrFile);
            } else if (var1.equals("TuxedoGidKw")) {
               var3 = this._TuxedoGidKw;
               this._TuxedoGidKw = (String)var2;
               this._postSet(27, var3, this._TuxedoGidKw);
            } else if (var1.equals("TuxedoUidKw")) {
               var3 = this._TuxedoUidKw;
               this._TuxedoUidKw = (String)var2;
               this._postSet(26, var3, this._TuxedoUidKw);
            } else {
               super.putValue(var1, var2);
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AccessPoint")) {
         return this._AccessPoint;
      } else if (var1.equals("AccessPointId")) {
         return this._AccessPointId;
      } else if (var1.equals("AclPolicy")) {
         return this._AclPolicy;
      } else if (var1.equals("AllowAnonymous")) {
         return new Boolean(this._AllowAnonymous);
      } else if (var1.equals("AppKey")) {
         return this._AppKey;
      } else if (var1.equals("CmpLimit")) {
         return new Integer(this._CmpLimit);
      } else if (var1.equals("ConnPrincipalName")) {
         return this._ConnPrincipalName;
      } else if (var1.equals("ConnectionPolicy")) {
         return this._ConnectionPolicy;
      } else if (var1.equals("CredentialPolicy")) {
         return this._CredentialPolicy;
      } else if (var1.equals("CustomAppKeyClass")) {
         return this._CustomAppKeyClass;
      } else if (var1.equals("CustomAppKeyClassParam")) {
         return this._CustomAppKeyClassParam;
      } else if (var1.equals("DefaultAppKey")) {
         return new Integer(this._DefaultAppKey);
      } else if (var1.equals("FederationName")) {
         return this._FederationName;
      } else if (var1.equals("FederationURL")) {
         return this._FederationURL;
      } else if (var1.equals("KeepAlive")) {
         return new Integer(this._KeepAlive);
      } else if (var1.equals("KeepAliveWait")) {
         return new Integer(this._KeepAliveWait);
      } else if (var1.equals("LocalAccessPoint")) {
         return this._LocalAccessPoint;
      } else if (var1.equals("MaxEncryptBits")) {
         return this._MaxEncryptBits;
      } else if (var1.equals("MaxRetries")) {
         return new Long(this._MaxRetries);
      } else if (var1.equals("MinEncryptBits")) {
         return this._MinEncryptBits;
      } else if (var1.equals("NWAddr")) {
         return this._NWAddr;
      } else if (var1.equals("RetryInterval")) {
         return new Long(this._RetryInterval);
      } else if (var1.equals("TpUsrFile")) {
         return this._TpUsrFile;
      } else if (var1.equals("TuxedoGidKw")) {
         return this._TuxedoGidKw;
      } else {
         return var1.equals("TuxedoUidKw") ? this._TuxedoUidKw : super.getValue(var1);
      }
   }

   public static void validateGeneration() {
      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("AccessPoint", "myRAP");
      } catch (IllegalArgumentException var3) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property AccessPoint in WTCRemoteTuxDomMBean" + var3.getMessage());
      }

      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("AccessPointId", "myRAPId");
      } catch (IllegalArgumentException var2) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property AccessPointId in WTCRemoteTuxDomMBean" + var2.getMessage());
      }

      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("LocalAccessPoint", "myLAP");
      } catch (IllegalArgumentException var1) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property LocalAccessPoint in WTCRemoteTuxDomMBean" + var1.getMessage());
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 7:
               if (var1.equals("app-key")) {
                  return 23;
               }

               if (var1.equals("nw-addr")) {
                  return 17;
               }
            case 8:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            default:
               break;
            case 9:
               if (var1.equals("cmp-limit")) {
                  return 20;
               }
               break;
            case 10:
               if (var1.equals("acl-policy")) {
                  return 10;
               }

               if (var1.equals("keep-alive")) {
                  return 30;
               }
               break;
            case 11:
               if (var1.equals("max-retries")) {
                  return 16;
               }

               if (var1.equals("tp-usr-file")) {
                  return 12;
               }
               break;
            case 12:
               if (var1.equals("access-point")) {
                  return 7;
               }
               break;
            case 13:
               if (var1.equals("tuxedo-gid-kw")) {
                  return 27;
               }

               if (var1.equals("tuxedo-uid-kw")) {
                  return 26;
               }
               break;
            case 14:
               if (var1.equals("federation-url")) {
                  return 18;
               }

               if (var1.equals("retry-interval")) {
                  return 15;
               }
               break;
            case 15:
               if (var1.equals("access-point-id")) {
                  return 8;
               }

               if (var1.equals("allow-anonymous")) {
                  return 24;
               }

               if (var1.equals("default-app-key")) {
                  return 25;
               }

               if (var1.equals("federation-name")) {
                  return 19;
               }

               if (var1.equals("keep-alive-wait")) {
                  return 31;
               }
               break;
            case 16:
               if (var1.equals("max-encrypt-bits")) {
                  return 22;
               }

               if (var1.equals("min-encrypt-bits")) {
                  return 21;
               }
               break;
            case 17:
               if (var1.equals("connection-policy")) {
                  return 9;
               }

               if (var1.equals("credential-policy")) {
                  return 11;
               }
               break;
            case 18:
               if (var1.equals("local-access-point")) {
                  return 13;
               }
               break;
            case 19:
               if (var1.equals("conn-principal-name")) {
                  return 14;
               }
               break;
            case 20:
               if (var1.equals("custom-app-key-class")) {
                  return 28;
               }
               break;
            case 26:
               if (var1.equals("custom-app-key-class-param")) {
                  return 29;
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
               return "access-point";
            case 8:
               return "access-point-id";
            case 9:
               return "connection-policy";
            case 10:
               return "acl-policy";
            case 11:
               return "credential-policy";
            case 12:
               return "tp-usr-file";
            case 13:
               return "local-access-point";
            case 14:
               return "conn-principal-name";
            case 15:
               return "retry-interval";
            case 16:
               return "max-retries";
            case 17:
               return "nw-addr";
            case 18:
               return "federation-url";
            case 19:
               return "federation-name";
            case 20:
               return "cmp-limit";
            case 21:
               return "min-encrypt-bits";
            case 22:
               return "max-encrypt-bits";
            case 23:
               return "app-key";
            case 24:
               return "allow-anonymous";
            case 25:
               return "default-app-key";
            case 26:
               return "tuxedo-uid-kw";
            case 27:
               return "tuxedo-gid-kw";
            case 28:
               return "custom-app-key-class";
            case 29:
               return "custom-app-key-class-param";
            case 30:
               return "keep-alive";
            case 31:
               return "keep-alive-wait";
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
      private WTCRemoteTuxDomMBeanImpl bean;

      protected Helper(WTCRemoteTuxDomMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "AccessPoint";
            case 8:
               return "AccessPointId";
            case 9:
               return "ConnectionPolicy";
            case 10:
               return "AclPolicy";
            case 11:
               return "CredentialPolicy";
            case 12:
               return "TpUsrFile";
            case 13:
               return "LocalAccessPoint";
            case 14:
               return "ConnPrincipalName";
            case 15:
               return "RetryInterval";
            case 16:
               return "MaxRetries";
            case 17:
               return "NWAddr";
            case 18:
               return "FederationURL";
            case 19:
               return "FederationName";
            case 20:
               return "CmpLimit";
            case 21:
               return "MinEncryptBits";
            case 22:
               return "MaxEncryptBits";
            case 23:
               return "AppKey";
            case 24:
               return "AllowAnonymous";
            case 25:
               return "DefaultAppKey";
            case 26:
               return "TuxedoUidKw";
            case 27:
               return "TuxedoGidKw";
            case 28:
               return "CustomAppKeyClass";
            case 29:
               return "CustomAppKeyClassParam";
            case 30:
               return "KeepAlive";
            case 31:
               return "KeepAliveWait";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("AccessPoint")) {
            return 7;
         } else if (var1.equals("AccessPointId")) {
            return 8;
         } else if (var1.equals("AclPolicy")) {
            return 10;
         } else if (var1.equals("AllowAnonymous")) {
            return 24;
         } else if (var1.equals("AppKey")) {
            return 23;
         } else if (var1.equals("CmpLimit")) {
            return 20;
         } else if (var1.equals("ConnPrincipalName")) {
            return 14;
         } else if (var1.equals("ConnectionPolicy")) {
            return 9;
         } else if (var1.equals("CredentialPolicy")) {
            return 11;
         } else if (var1.equals("CustomAppKeyClass")) {
            return 28;
         } else if (var1.equals("CustomAppKeyClassParam")) {
            return 29;
         } else if (var1.equals("DefaultAppKey")) {
            return 25;
         } else if (var1.equals("FederationName")) {
            return 19;
         } else if (var1.equals("FederationURL")) {
            return 18;
         } else if (var1.equals("KeepAlive")) {
            return 30;
         } else if (var1.equals("KeepAliveWait")) {
            return 31;
         } else if (var1.equals("LocalAccessPoint")) {
            return 13;
         } else if (var1.equals("MaxEncryptBits")) {
            return 22;
         } else if (var1.equals("MaxRetries")) {
            return 16;
         } else if (var1.equals("MinEncryptBits")) {
            return 21;
         } else if (var1.equals("NWAddr")) {
            return 17;
         } else if (var1.equals("RetryInterval")) {
            return 15;
         } else if (var1.equals("TpUsrFile")) {
            return 12;
         } else if (var1.equals("TuxedoGidKw")) {
            return 27;
         } else {
            return var1.equals("TuxedoUidKw") ? 26 : super.getPropertyIndex(var1);
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
            if (this.bean.isAccessPointSet()) {
               var2.append("AccessPoint");
               var2.append(String.valueOf(this.bean.getAccessPoint()));
            }

            if (this.bean.isAccessPointIdSet()) {
               var2.append("AccessPointId");
               var2.append(String.valueOf(this.bean.getAccessPointId()));
            }

            if (this.bean.isAclPolicySet()) {
               var2.append("AclPolicy");
               var2.append(String.valueOf(this.bean.getAclPolicy()));
            }

            if (this.bean.isAllowAnonymousSet()) {
               var2.append("AllowAnonymous");
               var2.append(String.valueOf(this.bean.getAllowAnonymous()));
            }

            if (this.bean.isAppKeySet()) {
               var2.append("AppKey");
               var2.append(String.valueOf(this.bean.getAppKey()));
            }

            if (this.bean.isCmpLimitSet()) {
               var2.append("CmpLimit");
               var2.append(String.valueOf(this.bean.getCmpLimit()));
            }

            if (this.bean.isConnPrincipalNameSet()) {
               var2.append("ConnPrincipalName");
               var2.append(String.valueOf(this.bean.getConnPrincipalName()));
            }

            if (this.bean.isConnectionPolicySet()) {
               var2.append("ConnectionPolicy");
               var2.append(String.valueOf(this.bean.getConnectionPolicy()));
            }

            if (this.bean.isCredentialPolicySet()) {
               var2.append("CredentialPolicy");
               var2.append(String.valueOf(this.bean.getCredentialPolicy()));
            }

            if (this.bean.isCustomAppKeyClassSet()) {
               var2.append("CustomAppKeyClass");
               var2.append(String.valueOf(this.bean.getCustomAppKeyClass()));
            }

            if (this.bean.isCustomAppKeyClassParamSet()) {
               var2.append("CustomAppKeyClassParam");
               var2.append(String.valueOf(this.bean.getCustomAppKeyClassParam()));
            }

            if (this.bean.isDefaultAppKeySet()) {
               var2.append("DefaultAppKey");
               var2.append(String.valueOf(this.bean.getDefaultAppKey()));
            }

            if (this.bean.isFederationNameSet()) {
               var2.append("FederationName");
               var2.append(String.valueOf(this.bean.getFederationName()));
            }

            if (this.bean.isFederationURLSet()) {
               var2.append("FederationURL");
               var2.append(String.valueOf(this.bean.getFederationURL()));
            }

            if (this.bean.isKeepAliveSet()) {
               var2.append("KeepAlive");
               var2.append(String.valueOf(this.bean.getKeepAlive()));
            }

            if (this.bean.isKeepAliveWaitSet()) {
               var2.append("KeepAliveWait");
               var2.append(String.valueOf(this.bean.getKeepAliveWait()));
            }

            if (this.bean.isLocalAccessPointSet()) {
               var2.append("LocalAccessPoint");
               var2.append(String.valueOf(this.bean.getLocalAccessPoint()));
            }

            if (this.bean.isMaxEncryptBitsSet()) {
               var2.append("MaxEncryptBits");
               var2.append(String.valueOf(this.bean.getMaxEncryptBits()));
            }

            if (this.bean.isMaxRetriesSet()) {
               var2.append("MaxRetries");
               var2.append(String.valueOf(this.bean.getMaxRetries()));
            }

            if (this.bean.isMinEncryptBitsSet()) {
               var2.append("MinEncryptBits");
               var2.append(String.valueOf(this.bean.getMinEncryptBits()));
            }

            if (this.bean.isNWAddrSet()) {
               var2.append("NWAddr");
               var2.append(String.valueOf(this.bean.getNWAddr()));
            }

            if (this.bean.isRetryIntervalSet()) {
               var2.append("RetryInterval");
               var2.append(String.valueOf(this.bean.getRetryInterval()));
            }

            if (this.bean.isTpUsrFileSet()) {
               var2.append("TpUsrFile");
               var2.append(String.valueOf(this.bean.getTpUsrFile()));
            }

            if (this.bean.isTuxedoGidKwSet()) {
               var2.append("TuxedoGidKw");
               var2.append(String.valueOf(this.bean.getTuxedoGidKw()));
            }

            if (this.bean.isTuxedoUidKwSet()) {
               var2.append("TuxedoUidKw");
               var2.append(String.valueOf(this.bean.getTuxedoUidKw()));
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
            WTCRemoteTuxDomMBeanImpl var2 = (WTCRemoteTuxDomMBeanImpl)var1;
            this.computeDiff("AccessPoint", this.bean.getAccessPoint(), var2.getAccessPoint(), true);
            this.computeDiff("AccessPointId", this.bean.getAccessPointId(), var2.getAccessPointId(), true);
            this.computeDiff("AclPolicy", this.bean.getAclPolicy(), var2.getAclPolicy(), true);
            this.computeDiff("AllowAnonymous", this.bean.getAllowAnonymous(), var2.getAllowAnonymous(), true);
            this.computeDiff("AppKey", this.bean.getAppKey(), var2.getAppKey(), true);
            this.computeDiff("CmpLimit", this.bean.getCmpLimit(), var2.getCmpLimit(), true);
            this.computeDiff("ConnPrincipalName", this.bean.getConnPrincipalName(), var2.getConnPrincipalName(), true);
            this.computeDiff("ConnectionPolicy", this.bean.getConnectionPolicy(), var2.getConnectionPolicy(), true);
            this.computeDiff("CredentialPolicy", this.bean.getCredentialPolicy(), var2.getCredentialPolicy(), true);
            this.computeDiff("CustomAppKeyClass", this.bean.getCustomAppKeyClass(), var2.getCustomAppKeyClass(), true);
            this.computeDiff("CustomAppKeyClassParam", this.bean.getCustomAppKeyClassParam(), var2.getCustomAppKeyClassParam(), true);
            this.computeDiff("DefaultAppKey", this.bean.getDefaultAppKey(), var2.getDefaultAppKey(), true);
            this.computeDiff("FederationName", this.bean.getFederationName(), var2.getFederationName(), true);
            this.computeDiff("FederationURL", this.bean.getFederationURL(), var2.getFederationURL(), true);
            this.computeDiff("KeepAlive", this.bean.getKeepAlive(), var2.getKeepAlive(), true);
            this.computeDiff("KeepAliveWait", this.bean.getKeepAliveWait(), var2.getKeepAliveWait(), true);
            this.computeDiff("LocalAccessPoint", this.bean.getLocalAccessPoint(), var2.getLocalAccessPoint(), true);
            this.computeDiff("MaxEncryptBits", this.bean.getMaxEncryptBits(), var2.getMaxEncryptBits(), true);
            this.computeDiff("MaxRetries", this.bean.getMaxRetries(), var2.getMaxRetries(), true);
            this.computeDiff("MinEncryptBits", this.bean.getMinEncryptBits(), var2.getMinEncryptBits(), true);
            this.computeDiff("NWAddr", this.bean.getNWAddr(), var2.getNWAddr(), true);
            this.computeDiff("RetryInterval", this.bean.getRetryInterval(), var2.getRetryInterval(), true);
            this.computeDiff("TpUsrFile", this.bean.getTpUsrFile(), var2.getTpUsrFile(), true);
            this.computeDiff("TuxedoGidKw", this.bean.getTuxedoGidKw(), var2.getTuxedoGidKw(), true);
            this.computeDiff("TuxedoUidKw", this.bean.getTuxedoUidKw(), var2.getTuxedoUidKw(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WTCRemoteTuxDomMBeanImpl var3 = (WTCRemoteTuxDomMBeanImpl)var1.getSourceBean();
            WTCRemoteTuxDomMBeanImpl var4 = (WTCRemoteTuxDomMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("AccessPoint")) {
                  var3.setAccessPoint(var4.getAccessPoint());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("AccessPointId")) {
                  var3.setAccessPointId(var4.getAccessPointId());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("AclPolicy")) {
                  var3.setAclPolicy(var4.getAclPolicy());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("AllowAnonymous")) {
                  var3.setAllowAnonymous(var4.getAllowAnonymous());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 24);
               } else if (var5.equals("AppKey")) {
                  var3.setAppKey(var4.getAppKey());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 23);
               } else if (var5.equals("CmpLimit")) {
                  var3.setCmpLimit(var4.getCmpLimit());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 20);
               } else if (var5.equals("ConnPrincipalName")) {
                  var3.setConnPrincipalName(var4.getConnPrincipalName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("ConnectionPolicy")) {
                  var3.setConnectionPolicy(var4.getConnectionPolicy());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("CredentialPolicy")) {
                  var3.setCredentialPolicy(var4.getCredentialPolicy());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("CustomAppKeyClass")) {
                  var3.setCustomAppKeyClass(var4.getCustomAppKeyClass());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 28);
               } else if (var5.equals("CustomAppKeyClassParam")) {
                  var3.setCustomAppKeyClassParam(var4.getCustomAppKeyClassParam());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 29);
               } else if (var5.equals("DefaultAppKey")) {
                  var3.setDefaultAppKey(var4.getDefaultAppKey());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 25);
               } else if (var5.equals("FederationName")) {
                  var3.setFederationName(var4.getFederationName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 19);
               } else if (var5.equals("FederationURL")) {
                  var3.setFederationURL(var4.getFederationURL());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 18);
               } else if (var5.equals("KeepAlive")) {
                  var3.setKeepAlive(var4.getKeepAlive());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 30);
               } else if (var5.equals("KeepAliveWait")) {
                  var3.setKeepAliveWait(var4.getKeepAliveWait());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 31);
               } else if (var5.equals("LocalAccessPoint")) {
                  var3.setLocalAccessPoint(var4.getLocalAccessPoint());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("MaxEncryptBits")) {
                  var3.setMaxEncryptBits(var4.getMaxEncryptBits());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 22);
               } else if (var5.equals("MaxRetries")) {
                  var3.setMaxRetries(var4.getMaxRetries());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
               } else if (var5.equals("MinEncryptBits")) {
                  var3.setMinEncryptBits(var4.getMinEncryptBits());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 21);
               } else if (var5.equals("NWAddr")) {
                  var3.setNWAddr(var4.getNWAddr());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 17);
               } else if (var5.equals("RetryInterval")) {
                  var3.setRetryInterval(var4.getRetryInterval());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("TpUsrFile")) {
                  var3.setTpUsrFile(var4.getTpUsrFile());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("TuxedoGidKw")) {
                  var3.setTuxedoGidKw(var4.getTuxedoGidKw());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 27);
               } else if (var5.equals("TuxedoUidKw")) {
                  var3.setTuxedoUidKw(var4.getTuxedoUidKw());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 26);
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
            WTCRemoteTuxDomMBeanImpl var5 = (WTCRemoteTuxDomMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("AccessPoint")) && this.bean.isAccessPointSet()) {
               var5.setAccessPoint(this.bean.getAccessPoint());
            }

            if ((var3 == null || !var3.contains("AccessPointId")) && this.bean.isAccessPointIdSet()) {
               var5.setAccessPointId(this.bean.getAccessPointId());
            }

            if ((var3 == null || !var3.contains("AclPolicy")) && this.bean.isAclPolicySet()) {
               var5.setAclPolicy(this.bean.getAclPolicy());
            }

            if ((var3 == null || !var3.contains("AllowAnonymous")) && this.bean.isAllowAnonymousSet()) {
               var5.setAllowAnonymous(this.bean.getAllowAnonymous());
            }

            if ((var3 == null || !var3.contains("AppKey")) && this.bean.isAppKeySet()) {
               var5.setAppKey(this.bean.getAppKey());
            }

            if ((var3 == null || !var3.contains("CmpLimit")) && this.bean.isCmpLimitSet()) {
               var5.setCmpLimit(this.bean.getCmpLimit());
            }

            if ((var3 == null || !var3.contains("ConnPrincipalName")) && this.bean.isConnPrincipalNameSet()) {
               var5.setConnPrincipalName(this.bean.getConnPrincipalName());
            }

            if ((var3 == null || !var3.contains("ConnectionPolicy")) && this.bean.isConnectionPolicySet()) {
               var5.setConnectionPolicy(this.bean.getConnectionPolicy());
            }

            if ((var3 == null || !var3.contains("CredentialPolicy")) && this.bean.isCredentialPolicySet()) {
               var5.setCredentialPolicy(this.bean.getCredentialPolicy());
            }

            if ((var3 == null || !var3.contains("CustomAppKeyClass")) && this.bean.isCustomAppKeyClassSet()) {
               var5.setCustomAppKeyClass(this.bean.getCustomAppKeyClass());
            }

            if ((var3 == null || !var3.contains("CustomAppKeyClassParam")) && this.bean.isCustomAppKeyClassParamSet()) {
               var5.setCustomAppKeyClassParam(this.bean.getCustomAppKeyClassParam());
            }

            if ((var3 == null || !var3.contains("DefaultAppKey")) && this.bean.isDefaultAppKeySet()) {
               var5.setDefaultAppKey(this.bean.getDefaultAppKey());
            }

            if ((var3 == null || !var3.contains("FederationName")) && this.bean.isFederationNameSet()) {
               var5.setFederationName(this.bean.getFederationName());
            }

            if ((var3 == null || !var3.contains("FederationURL")) && this.bean.isFederationURLSet()) {
               var5.setFederationURL(this.bean.getFederationURL());
            }

            if ((var3 == null || !var3.contains("KeepAlive")) && this.bean.isKeepAliveSet()) {
               var5.setKeepAlive(this.bean.getKeepAlive());
            }

            if ((var3 == null || !var3.contains("KeepAliveWait")) && this.bean.isKeepAliveWaitSet()) {
               var5.setKeepAliveWait(this.bean.getKeepAliveWait());
            }

            if ((var3 == null || !var3.contains("LocalAccessPoint")) && this.bean.isLocalAccessPointSet()) {
               var5.setLocalAccessPoint(this.bean.getLocalAccessPoint());
            }

            if ((var3 == null || !var3.contains("MaxEncryptBits")) && this.bean.isMaxEncryptBitsSet()) {
               var5.setMaxEncryptBits(this.bean.getMaxEncryptBits());
            }

            if ((var3 == null || !var3.contains("MaxRetries")) && this.bean.isMaxRetriesSet()) {
               var5.setMaxRetries(this.bean.getMaxRetries());
            }

            if ((var3 == null || !var3.contains("MinEncryptBits")) && this.bean.isMinEncryptBitsSet()) {
               var5.setMinEncryptBits(this.bean.getMinEncryptBits());
            }

            if ((var3 == null || !var3.contains("NWAddr")) && this.bean.isNWAddrSet()) {
               var5.setNWAddr(this.bean.getNWAddr());
            }

            if ((var3 == null || !var3.contains("RetryInterval")) && this.bean.isRetryIntervalSet()) {
               var5.setRetryInterval(this.bean.getRetryInterval());
            }

            if ((var3 == null || !var3.contains("TpUsrFile")) && this.bean.isTpUsrFileSet()) {
               var5.setTpUsrFile(this.bean.getTpUsrFile());
            }

            if ((var3 == null || !var3.contains("TuxedoGidKw")) && this.bean.isTuxedoGidKwSet()) {
               var5.setTuxedoGidKw(this.bean.getTuxedoGidKw());
            }

            if ((var3 == null || !var3.contains("TuxedoUidKw")) && this.bean.isTuxedoUidKwSet()) {
               var5.setTuxedoUidKw(this.bean.getTuxedoUidKw());
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
