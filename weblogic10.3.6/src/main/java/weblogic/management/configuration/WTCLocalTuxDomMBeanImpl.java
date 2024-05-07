package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
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
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class WTCLocalTuxDomMBeanImpl extends ConfigurationMBeanImpl implements WTCLocalTuxDomMBean, Serializable {
   private String _AccessPoint;
   private String _AccessPointId;
   private long _BlockTime;
   private int _CmpLimit;
   private String _ConnPrincipalName;
   private String _ConnectionPolicy;
   private String _IdentityKeyStoreFileName;
   private String _IdentityKeyStorePassPhrase;
   private byte[] _IdentityKeyStorePassPhraseEncrypted;
   private String _Interoperate;
   private int _KeepAlive;
   private int _KeepAliveWait;
   private String _KeyStoresLocation;
   private String _MaxEncryptBits;
   private long _MaxRetries;
   private String _MinEncryptBits;
   private String _NWAddr;
   private String _PrivateKeyAlias;
   private String _PrivateKeyPassPhrase;
   private byte[] _PrivateKeyPassPhraseEncrypted;
   private long _RetryInterval;
   private String _Security;
   private String _TrustKeyStoreFileName;
   private String _TrustKeyStorePassPhrase;
   private byte[] _TrustKeyStorePassPhraseEncrypted;
   private String _UseSSL;
   private static SchemaHelper2 _schemaHelper;

   public WTCLocalTuxDomMBeanImpl() {
      this._initializeProperty(-1);
   }

   public WTCLocalTuxDomMBeanImpl(DescriptorBean var1, int var2) {
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

   public void setSecurity(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"NONE", "APP_PW", "DM_PW"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("Security", var1, var2);
      String var3 = this._Security;
      this._Security = var1;
      this._postSet(9, var3, var1);
   }

   public String getSecurity() {
      return this._Security;
   }

   public boolean isSecuritySet() {
      return this._isSet(9);
   }

   public void setConnectionPolicy(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"ON_DEMAND", "ON_STARTUP", "INCOMING_ONLY"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("ConnectionPolicy", var1, var2);
      String var3 = this._ConnectionPolicy;
      this._ConnectionPolicy = var1;
      this._postSet(10, var3, var1);
   }

   public String getConnectionPolicy() {
      return this._ConnectionPolicy;
   }

   public boolean isConnectionPolicySet() {
      return this._isSet(10);
   }

   public void setConnPrincipalName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ConnPrincipalName;
      this._ConnPrincipalName = var1;
      this._postSet(11, var2, var1);
   }

   public String getConnPrincipalName() {
      return this._ConnPrincipalName;
   }

   public boolean isConnPrincipalNameSet() {
      return this._isSet(11);
   }

   public void setRetryInterval(long var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("RetryInterval", var1, 0L, 2147483647L);
      long var3 = this._RetryInterval;
      this._RetryInterval = var1;
      this._postSet(12, var3, var1);
   }

   public long getRetryInterval() {
      return this._RetryInterval;
   }

   public boolean isRetryIntervalSet() {
      return this._isSet(12);
   }

   public void setMaxRetries(long var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("MaxRetries", var1, 0L, Long.MAX_VALUE);
      long var3 = this._MaxRetries;
      this._MaxRetries = var1;
      this._postSet(13, var3, var1);
   }

   public long getMaxRetries() {
      return this._MaxRetries;
   }

   public boolean isMaxRetriesSet() {
      return this._isSet(13);
   }

   public void setBlockTime(long var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("BlockTime", var1, 0L, 2147483647L);
      long var3 = this._BlockTime;
      this._BlockTime = var1;
      this._postSet(14, var3, var1);
   }

   public long getBlockTime() {
      return this._BlockTime;
   }

   public boolean isBlockTimeSet() {
      return this._isSet(14);
   }

   public void setNWAddr(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._NWAddr;
      this._NWAddr = var1;
      this._postSet(15, var2, var1);
   }

   public String getNWAddr() {
      return this._NWAddr;
   }

   public boolean isNWAddrSet() {
      return this._isSet(15);
   }

   public void setCmpLimit(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("CmpLimit", (long)var1, 0L, 2147483647L);
      int var2 = this._CmpLimit;
      this._CmpLimit = var1;
      this._postSet(16, var2, var1);
   }

   public int getCmpLimit() {
      return this._CmpLimit;
   }

   public boolean isCmpLimitSet() {
      return this._isSet(16);
   }

   public void setMinEncryptBits(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"0", "40", "56", "128", "256"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("MinEncryptBits", var1, var2);
      String var3 = this._MinEncryptBits;
      this._MinEncryptBits = var1;
      this._postSet(17, var3, var1);
   }

   public String getMinEncryptBits() {
      return this._MinEncryptBits;
   }

   public boolean isMinEncryptBitsSet() {
      return this._isSet(17);
   }

   public void setMaxEncryptBits(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"0", "40", "56", "128", "256"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("MaxEncryptBits", var1, var2);
      String var3 = this._MaxEncryptBits;
      this._MaxEncryptBits = var1;
      this._postSet(18, var3, var1);
   }

   public String getMaxEncryptBits() {
      return this._MaxEncryptBits;
   }

   public boolean isMaxEncryptBitsSet() {
      return this._isSet(18);
   }

   public void setInteroperate(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Yes", "No"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("Interoperate", var1, var2);
      String var3 = this._Interoperate;
      this._Interoperate = var1;
      this._postSet(19, var3, var1);
   }

   public String getInteroperate() {
      return this._Interoperate;
   }

   public boolean isInteroperateSet() {
      return this._isSet(19);
   }

   public void setKeepAlive(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("KeepAlive", (long)var1, 0L, 2147483647L);
      int var2 = this._KeepAlive;
      this._KeepAlive = var1;
      this._postSet(20, var2, var1);
   }

   public int getKeepAlive() {
      return this._KeepAlive;
   }

   public boolean isKeepAliveSet() {
      return this._isSet(20);
   }

   public void setKeepAliveWait(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("KeepAliveWait", (long)var1, 0L, 2147483647L);
      int var2 = this._KeepAliveWait;
      this._KeepAliveWait = var1;
      this._postSet(21, var2, var1);
   }

   public int getKeepAliveWait() {
      return this._KeepAliveWait;
   }

   public boolean isKeepAliveWaitSet() {
      return this._isSet(21);
   }

   public String getUseSSL() {
      return this._UseSSL;
   }

   public boolean isUseSSLSet() {
      return this._isSet(22);
   }

   public void setUseSSL(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"Off", "TwoWay", "OneWay"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("UseSSL", var1, var2);
      String var3 = this._UseSSL;
      this._UseSSL = var1;
      this._postSet(22, var3, var1);
   }

   public String getKeyStoresLocation() {
      return this._KeyStoresLocation;
   }

   public boolean isKeyStoresLocationSet() {
      return this._isSet(23);
   }

   public void setKeyStoresLocation(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"WLS Stores", "Custom Stores"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("KeyStoresLocation", var1, var2);
      String var3 = this._KeyStoresLocation;
      this._KeyStoresLocation = var1;
      this._postSet(23, var3, var1);
   }

   public String getIdentityKeyStoreFileName() {
      return this._IdentityKeyStoreFileName;
   }

   public boolean isIdentityKeyStoreFileNameSet() {
      return this._isSet(24);
   }

   public void setIdentityKeyStoreFileName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._IdentityKeyStoreFileName;
      this._IdentityKeyStoreFileName = var1;
      this._postSet(24, var2, var1);
   }

   public String getIdentityKeyStorePassPhrase() {
      byte[] var1 = this.getIdentityKeyStorePassPhraseEncrypted();
      return var1 == null ? null : this._decrypt("IdentityKeyStorePassPhrase", var1);
   }

   public boolean isIdentityKeyStorePassPhraseSet() {
      return this.isIdentityKeyStorePassPhraseEncryptedSet();
   }

   public void setIdentityKeyStorePassPhrase(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this.setIdentityKeyStorePassPhraseEncrypted(var1 == null ? null : this._encrypt("IdentityKeyStorePassPhrase", var1));
   }

   public byte[] getIdentityKeyStorePassPhraseEncrypted() {
      return this._getHelper()._cloneArray(this._IdentityKeyStorePassPhraseEncrypted);
   }

   public String getIdentityKeyStorePassPhraseEncryptedAsString() {
      byte[] var1 = this.getIdentityKeyStorePassPhraseEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isIdentityKeyStorePassPhraseEncryptedSet() {
      return this._isSet(26);
   }

   public void setIdentityKeyStorePassPhraseEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setIdentityKeyStorePassPhraseEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public String getPrivateKeyAlias() {
      return this._PrivateKeyAlias;
   }

   public boolean isPrivateKeyAliasSet() {
      return this._isSet(27);
   }

   public void setPrivateKeyAlias(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._PrivateKeyAlias;
      this._PrivateKeyAlias = var1;
      this._postSet(27, var2, var1);
   }

   public String getPrivateKeyPassPhrase() {
      byte[] var1 = this.getPrivateKeyPassPhraseEncrypted();
      return var1 == null ? null : this._decrypt("PrivateKeyPassPhrase", var1);
   }

   public boolean isPrivateKeyPassPhraseSet() {
      return this.isPrivateKeyPassPhraseEncryptedSet();
   }

   public void setPrivateKeyPassPhrase(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this.setPrivateKeyPassPhraseEncrypted(var1 == null ? null : this._encrypt("PrivateKeyPassPhrase", var1));
   }

   public byte[] getPrivateKeyPassPhraseEncrypted() {
      return this._getHelper()._cloneArray(this._PrivateKeyPassPhraseEncrypted);
   }

   public String getPrivateKeyPassPhraseEncryptedAsString() {
      byte[] var1 = this.getPrivateKeyPassPhraseEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isPrivateKeyPassPhraseEncryptedSet() {
      return this._isSet(29);
   }

   public void setPrivateKeyPassPhraseEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setPrivateKeyPassPhraseEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public String getTrustKeyStoreFileName() {
      return this._TrustKeyStoreFileName;
   }

   public boolean isTrustKeyStoreFileNameSet() {
      return this._isSet(30);
   }

   public void setTrustKeyStoreFileName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._TrustKeyStoreFileName;
      this._TrustKeyStoreFileName = var1;
      this._postSet(30, var2, var1);
   }

   public String getTrustKeyStorePassPhrase() {
      byte[] var1 = this.getTrustKeyStorePassPhraseEncrypted();
      return var1 == null ? null : this._decrypt("TrustKeyStorePassPhrase", var1);
   }

   public boolean isTrustKeyStorePassPhraseSet() {
      return this.isTrustKeyStorePassPhraseEncryptedSet();
   }

   public void setTrustKeyStorePassPhrase(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this.setTrustKeyStorePassPhraseEncrypted(var1 == null ? null : this._encrypt("TrustKeyStorePassPhrase", var1));
   }

   public byte[] getTrustKeyStorePassPhraseEncrypted() {
      return this._getHelper()._cloneArray(this._TrustKeyStorePassPhraseEncrypted);
   }

   public String getTrustKeyStorePassPhraseEncryptedAsString() {
      byte[] var1 = this.getTrustKeyStorePassPhraseEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isTrustKeyStorePassPhraseEncryptedSet() {
      return this._isSet(32);
   }

   public void setTrustKeyStorePassPhraseEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setTrustKeyStorePassPhraseEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      WTCLegalHelper.validateWTCLocalTuxDom(this);
   }

   public void setIdentityKeyStorePassPhraseEncrypted(byte[] var1) {
      byte[] var2 = this._IdentityKeyStorePassPhraseEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: IdentityKeyStorePassPhraseEncrypted of WTCLocalTuxDomMBean");
      } else {
         this._getHelper()._clearArray(this._IdentityKeyStorePassPhraseEncrypted);
         this._IdentityKeyStorePassPhraseEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(26, var2, var1);
      }
   }

   public void setPrivateKeyPassPhraseEncrypted(byte[] var1) {
      byte[] var2 = this._PrivateKeyPassPhraseEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: PrivateKeyPassPhraseEncrypted of WTCLocalTuxDomMBean");
      } else {
         this._getHelper()._clearArray(this._PrivateKeyPassPhraseEncrypted);
         this._PrivateKeyPassPhraseEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(29, var2, var1);
      }
   }

   public void setTrustKeyStorePassPhraseEncrypted(byte[] var1) {
      byte[] var2 = this._TrustKeyStorePassPhraseEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: TrustKeyStorePassPhraseEncrypted of WTCLocalTuxDomMBean");
      } else {
         this._getHelper()._clearArray(this._TrustKeyStorePassPhraseEncrypted);
         this._TrustKeyStorePassPhraseEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(32, var2, var1);
      }
   }

   protected void _unSet(int var1) {
      if (!this._initializeProperty(var1)) {
         super._unSet(var1);
      } else {
         this._markSet(var1, false);
         if (var1 == 25) {
            this._markSet(26, false);
         }

         if (var1 == 28) {
            this._markSet(29, false);
         }

         if (var1 == 31) {
            this._markSet(32, false);
         }
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
               this._AccessPoint = "myLAP";
               if (var2) {
                  break;
               }
            case 8:
               this._AccessPointId = "myLAPId";
               if (var2) {
                  break;
               }
            case 14:
               this._BlockTime = 60L;
               if (var2) {
                  break;
               }
            case 16:
               this._CmpLimit = Integer.MAX_VALUE;
               if (var2) {
                  break;
               }
            case 11:
               this._ConnPrincipalName = null;
               if (var2) {
                  break;
               }
            case 10:
               this._ConnectionPolicy = "ON_DEMAND";
               if (var2) {
                  break;
               }
            case 24:
               this._IdentityKeyStoreFileName = null;
               if (var2) {
                  break;
               }
            case 25:
               this._IdentityKeyStorePassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 26:
               this._IdentityKeyStorePassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 19:
               this._Interoperate = "No";
               if (var2) {
                  break;
               }
            case 20:
               this._KeepAlive = 0;
               if (var2) {
                  break;
               }
            case 21:
               this._KeepAliveWait = 0;
               if (var2) {
                  break;
               }
            case 23:
               this._KeyStoresLocation = "Custom Stores";
               if (var2) {
                  break;
               }
            case 18:
               this._MaxEncryptBits = "128";
               if (var2) {
                  break;
               }
            case 13:
               this._MaxRetries = Long.MAX_VALUE;
               if (var2) {
                  break;
               }
            case 17:
               this._MinEncryptBits = "0";
               if (var2) {
                  break;
               }
            case 15:
               this._NWAddr = "//localhost:8901";
               if (var2) {
                  break;
               }
            case 27:
               this._PrivateKeyAlias = null;
               if (var2) {
                  break;
               }
            case 28:
               this._PrivateKeyPassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 29:
               this._PrivateKeyPassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 12:
               this._RetryInterval = 60L;
               if (var2) {
                  break;
               }
            case 9:
               this._Security = "NONE";
               if (var2) {
                  break;
               }
            case 30:
               this._TrustKeyStoreFileName = null;
               if (var2) {
                  break;
               }
            case 31:
               this._TrustKeyStorePassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 32:
               this._TrustKeyStorePassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 22:
               this._UseSSL = "Off";
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
      return "WTCLocalTuxDom";
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
      } else {
         long var6;
         if (var1.equals("BlockTime")) {
            var6 = this._BlockTime;
            this._BlockTime = (Long)var2;
            this._postSet(14, var6, this._BlockTime);
         } else {
            int var7;
            if (var1.equals("CmpLimit")) {
               var7 = this._CmpLimit;
               this._CmpLimit = (Integer)var2;
               this._postSet(16, var7, this._CmpLimit);
            } else if (var1.equals("ConnPrincipalName")) {
               var3 = this._ConnPrincipalName;
               this._ConnPrincipalName = (String)var2;
               this._postSet(11, var3, this._ConnPrincipalName);
            } else if (var1.equals("ConnectionPolicy")) {
               var3 = this._ConnectionPolicy;
               this._ConnectionPolicy = (String)var2;
               this._postSet(10, var3, this._ConnectionPolicy);
            } else if (var1.equals("IdentityKeyStoreFileName")) {
               var3 = this._IdentityKeyStoreFileName;
               this._IdentityKeyStoreFileName = (String)var2;
               this._postSet(24, var3, this._IdentityKeyStoreFileName);
            } else if (var1.equals("IdentityKeyStorePassPhrase")) {
               var3 = this._IdentityKeyStorePassPhrase;
               this._IdentityKeyStorePassPhrase = (String)var2;
               this._postSet(25, var3, this._IdentityKeyStorePassPhrase);
            } else {
               byte[] var5;
               if (var1.equals("IdentityKeyStorePassPhraseEncrypted")) {
                  var5 = this._IdentityKeyStorePassPhraseEncrypted;
                  this._IdentityKeyStorePassPhraseEncrypted = (byte[])((byte[])var2);
                  this._postSet(26, var5, this._IdentityKeyStorePassPhraseEncrypted);
               } else if (var1.equals("Interoperate")) {
                  var3 = this._Interoperate;
                  this._Interoperate = (String)var2;
                  this._postSet(19, var3, this._Interoperate);
               } else if (var1.equals("KeepAlive")) {
                  var7 = this._KeepAlive;
                  this._KeepAlive = (Integer)var2;
                  this._postSet(20, var7, this._KeepAlive);
               } else if (var1.equals("KeepAliveWait")) {
                  var7 = this._KeepAliveWait;
                  this._KeepAliveWait = (Integer)var2;
                  this._postSet(21, var7, this._KeepAliveWait);
               } else if (var1.equals("KeyStoresLocation")) {
                  var3 = this._KeyStoresLocation;
                  this._KeyStoresLocation = (String)var2;
                  this._postSet(23, var3, this._KeyStoresLocation);
               } else if (var1.equals("MaxEncryptBits")) {
                  var3 = this._MaxEncryptBits;
                  this._MaxEncryptBits = (String)var2;
                  this._postSet(18, var3, this._MaxEncryptBits);
               } else if (var1.equals("MaxRetries")) {
                  var6 = this._MaxRetries;
                  this._MaxRetries = (Long)var2;
                  this._postSet(13, var6, this._MaxRetries);
               } else if (var1.equals("MinEncryptBits")) {
                  var3 = this._MinEncryptBits;
                  this._MinEncryptBits = (String)var2;
                  this._postSet(17, var3, this._MinEncryptBits);
               } else if (var1.equals("NWAddr")) {
                  var3 = this._NWAddr;
                  this._NWAddr = (String)var2;
                  this._postSet(15, var3, this._NWAddr);
               } else if (var1.equals("PrivateKeyAlias")) {
                  var3 = this._PrivateKeyAlias;
                  this._PrivateKeyAlias = (String)var2;
                  this._postSet(27, var3, this._PrivateKeyAlias);
               } else if (var1.equals("PrivateKeyPassPhrase")) {
                  var3 = this._PrivateKeyPassPhrase;
                  this._PrivateKeyPassPhrase = (String)var2;
                  this._postSet(28, var3, this._PrivateKeyPassPhrase);
               } else if (var1.equals("PrivateKeyPassPhraseEncrypted")) {
                  var5 = this._PrivateKeyPassPhraseEncrypted;
                  this._PrivateKeyPassPhraseEncrypted = (byte[])((byte[])var2);
                  this._postSet(29, var5, this._PrivateKeyPassPhraseEncrypted);
               } else if (var1.equals("RetryInterval")) {
                  var6 = this._RetryInterval;
                  this._RetryInterval = (Long)var2;
                  this._postSet(12, var6, this._RetryInterval);
               } else if (var1.equals("Security")) {
                  var3 = this._Security;
                  this._Security = (String)var2;
                  this._postSet(9, var3, this._Security);
               } else if (var1.equals("TrustKeyStoreFileName")) {
                  var3 = this._TrustKeyStoreFileName;
                  this._TrustKeyStoreFileName = (String)var2;
                  this._postSet(30, var3, this._TrustKeyStoreFileName);
               } else if (var1.equals("TrustKeyStorePassPhrase")) {
                  var3 = this._TrustKeyStorePassPhrase;
                  this._TrustKeyStorePassPhrase = (String)var2;
                  this._postSet(31, var3, this._TrustKeyStorePassPhrase);
               } else if (var1.equals("TrustKeyStorePassPhraseEncrypted")) {
                  var5 = this._TrustKeyStorePassPhraseEncrypted;
                  this._TrustKeyStorePassPhraseEncrypted = (byte[])((byte[])var2);
                  this._postSet(32, var5, this._TrustKeyStorePassPhraseEncrypted);
               } else if (var1.equals("UseSSL")) {
                  var3 = this._UseSSL;
                  this._UseSSL = (String)var2;
                  this._postSet(22, var3, this._UseSSL);
               } else {
                  super.putValue(var1, var2);
               }
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AccessPoint")) {
         return this._AccessPoint;
      } else if (var1.equals("AccessPointId")) {
         return this._AccessPointId;
      } else if (var1.equals("BlockTime")) {
         return new Long(this._BlockTime);
      } else if (var1.equals("CmpLimit")) {
         return new Integer(this._CmpLimit);
      } else if (var1.equals("ConnPrincipalName")) {
         return this._ConnPrincipalName;
      } else if (var1.equals("ConnectionPolicy")) {
         return this._ConnectionPolicy;
      } else if (var1.equals("IdentityKeyStoreFileName")) {
         return this._IdentityKeyStoreFileName;
      } else if (var1.equals("IdentityKeyStorePassPhrase")) {
         return this._IdentityKeyStorePassPhrase;
      } else if (var1.equals("IdentityKeyStorePassPhraseEncrypted")) {
         return this._IdentityKeyStorePassPhraseEncrypted;
      } else if (var1.equals("Interoperate")) {
         return this._Interoperate;
      } else if (var1.equals("KeepAlive")) {
         return new Integer(this._KeepAlive);
      } else if (var1.equals("KeepAliveWait")) {
         return new Integer(this._KeepAliveWait);
      } else if (var1.equals("KeyStoresLocation")) {
         return this._KeyStoresLocation;
      } else if (var1.equals("MaxEncryptBits")) {
         return this._MaxEncryptBits;
      } else if (var1.equals("MaxRetries")) {
         return new Long(this._MaxRetries);
      } else if (var1.equals("MinEncryptBits")) {
         return this._MinEncryptBits;
      } else if (var1.equals("NWAddr")) {
         return this._NWAddr;
      } else if (var1.equals("PrivateKeyAlias")) {
         return this._PrivateKeyAlias;
      } else if (var1.equals("PrivateKeyPassPhrase")) {
         return this._PrivateKeyPassPhrase;
      } else if (var1.equals("PrivateKeyPassPhraseEncrypted")) {
         return this._PrivateKeyPassPhraseEncrypted;
      } else if (var1.equals("RetryInterval")) {
         return new Long(this._RetryInterval);
      } else if (var1.equals("Security")) {
         return this._Security;
      } else if (var1.equals("TrustKeyStoreFileName")) {
         return this._TrustKeyStoreFileName;
      } else if (var1.equals("TrustKeyStorePassPhrase")) {
         return this._TrustKeyStorePassPhrase;
      } else if (var1.equals("TrustKeyStorePassPhraseEncrypted")) {
         return this._TrustKeyStorePassPhraseEncrypted;
      } else {
         return var1.equals("UseSSL") ? this._UseSSL : super.getValue(var1);
      }
   }

   public static void validateGeneration() {
      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("AccessPoint", "myLAP");
      } catch (IllegalArgumentException var2) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property AccessPoint in WTCLocalTuxDomMBean" + var2.getMessage());
      }

      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("AccessPointId", "myLAPId");
      } catch (IllegalArgumentException var1) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property AccessPointId in WTCLocalTuxDomMBean" + var1.getMessage());
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 6:
               if (var1.equals("usessl")) {
                  return 22;
               }
               break;
            case 7:
               if (var1.equals("nw-addr")) {
                  return 15;
               }
               break;
            case 8:
               if (var1.equals("security")) {
                  return 9;
               }
               break;
            case 9:
               if (var1.equals("cmp-limit")) {
                  return 16;
               }
               break;
            case 10:
               if (var1.equals("block-time")) {
                  return 14;
               }

               if (var1.equals("keep-alive")) {
                  return 20;
               }
               break;
            case 11:
               if (var1.equals("max-retries")) {
                  return 13;
               }
               break;
            case 12:
               if (var1.equals("access-point")) {
                  return 7;
               }

               if (var1.equals("interoperate")) {
                  return 19;
               }
            case 13:
            case 18:
            case 20:
            case 21:
            case 22:
            case 24:
            case 26:
            case 29:
            case 31:
            case 32:
            case 34:
            case 35:
            case 36:
            case 38:
            case 39:
            default:
               break;
            case 14:
               if (var1.equals("retry-interval")) {
                  return 12;
               }
               break;
            case 15:
               if (var1.equals("access-point-id")) {
                  return 8;
               }

               if (var1.equals("keep-alive-wait")) {
                  return 21;
               }
               break;
            case 16:
               if (var1.equals("max-encrypt-bits")) {
                  return 18;
               }

               if (var1.equals("min-encrypt-bits")) {
                  return 17;
               }
               break;
            case 17:
               if (var1.equals("connection-policy")) {
                  return 10;
               }

               if (var1.equals("private-key-alias")) {
                  return 27;
               }
               break;
            case 19:
               if (var1.equals("conn-principal-name")) {
                  return 11;
               }

               if (var1.equals("key-stores-location")) {
                  return 23;
               }
               break;
            case 23:
               if (var1.equals("private-key-pass-phrase")) {
                  return 28;
               }
               break;
            case 25:
               if (var1.equals("trust-key-store-file-name")) {
                  return 30;
               }
               break;
            case 27:
               if (var1.equals("trust-key-store-pass-phrase")) {
                  return 31;
               }
               break;
            case 28:
               if (var1.equals("identity-key-store-file-name")) {
                  return 24;
               }
               break;
            case 30:
               if (var1.equals("identity-key-store-pass-phrase")) {
                  return 25;
               }
               break;
            case 33:
               if (var1.equals("private-key-pass-phrase-encrypted")) {
                  return 29;
               }
               break;
            case 37:
               if (var1.equals("trust-key-store-pass-phrase-encrypted")) {
                  return 32;
               }
               break;
            case 40:
               if (var1.equals("identity-key-store-pass-phrase-encrypted")) {
                  return 26;
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
               return "security";
            case 10:
               return "connection-policy";
            case 11:
               return "conn-principal-name";
            case 12:
               return "retry-interval";
            case 13:
               return "max-retries";
            case 14:
               return "block-time";
            case 15:
               return "nw-addr";
            case 16:
               return "cmp-limit";
            case 17:
               return "min-encrypt-bits";
            case 18:
               return "max-encrypt-bits";
            case 19:
               return "interoperate";
            case 20:
               return "keep-alive";
            case 21:
               return "keep-alive-wait";
            case 22:
               return "usessl";
            case 23:
               return "key-stores-location";
            case 24:
               return "identity-key-store-file-name";
            case 25:
               return "identity-key-store-pass-phrase";
            case 26:
               return "identity-key-store-pass-phrase-encrypted";
            case 27:
               return "private-key-alias";
            case 28:
               return "private-key-pass-phrase";
            case 29:
               return "private-key-pass-phrase-encrypted";
            case 30:
               return "trust-key-store-file-name";
            case 31:
               return "trust-key-store-pass-phrase";
            case 32:
               return "trust-key-store-pass-phrase-encrypted";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            default:
               return super.isArray(var1);
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
      private WTCLocalTuxDomMBeanImpl bean;

      protected Helper(WTCLocalTuxDomMBeanImpl var1) {
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
               return "Security";
            case 10:
               return "ConnectionPolicy";
            case 11:
               return "ConnPrincipalName";
            case 12:
               return "RetryInterval";
            case 13:
               return "MaxRetries";
            case 14:
               return "BlockTime";
            case 15:
               return "NWAddr";
            case 16:
               return "CmpLimit";
            case 17:
               return "MinEncryptBits";
            case 18:
               return "MaxEncryptBits";
            case 19:
               return "Interoperate";
            case 20:
               return "KeepAlive";
            case 21:
               return "KeepAliveWait";
            case 22:
               return "UseSSL";
            case 23:
               return "KeyStoresLocation";
            case 24:
               return "IdentityKeyStoreFileName";
            case 25:
               return "IdentityKeyStorePassPhrase";
            case 26:
               return "IdentityKeyStorePassPhraseEncrypted";
            case 27:
               return "PrivateKeyAlias";
            case 28:
               return "PrivateKeyPassPhrase";
            case 29:
               return "PrivateKeyPassPhraseEncrypted";
            case 30:
               return "TrustKeyStoreFileName";
            case 31:
               return "TrustKeyStorePassPhrase";
            case 32:
               return "TrustKeyStorePassPhraseEncrypted";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("AccessPoint")) {
            return 7;
         } else if (var1.equals("AccessPointId")) {
            return 8;
         } else if (var1.equals("BlockTime")) {
            return 14;
         } else if (var1.equals("CmpLimit")) {
            return 16;
         } else if (var1.equals("ConnPrincipalName")) {
            return 11;
         } else if (var1.equals("ConnectionPolicy")) {
            return 10;
         } else if (var1.equals("IdentityKeyStoreFileName")) {
            return 24;
         } else if (var1.equals("IdentityKeyStorePassPhrase")) {
            return 25;
         } else if (var1.equals("IdentityKeyStorePassPhraseEncrypted")) {
            return 26;
         } else if (var1.equals("Interoperate")) {
            return 19;
         } else if (var1.equals("KeepAlive")) {
            return 20;
         } else if (var1.equals("KeepAliveWait")) {
            return 21;
         } else if (var1.equals("KeyStoresLocation")) {
            return 23;
         } else if (var1.equals("MaxEncryptBits")) {
            return 18;
         } else if (var1.equals("MaxRetries")) {
            return 13;
         } else if (var1.equals("MinEncryptBits")) {
            return 17;
         } else if (var1.equals("NWAddr")) {
            return 15;
         } else if (var1.equals("PrivateKeyAlias")) {
            return 27;
         } else if (var1.equals("PrivateKeyPassPhrase")) {
            return 28;
         } else if (var1.equals("PrivateKeyPassPhraseEncrypted")) {
            return 29;
         } else if (var1.equals("RetryInterval")) {
            return 12;
         } else if (var1.equals("Security")) {
            return 9;
         } else if (var1.equals("TrustKeyStoreFileName")) {
            return 30;
         } else if (var1.equals("TrustKeyStorePassPhrase")) {
            return 31;
         } else if (var1.equals("TrustKeyStorePassPhraseEncrypted")) {
            return 32;
         } else {
            return var1.equals("UseSSL") ? 22 : super.getPropertyIndex(var1);
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

            if (this.bean.isBlockTimeSet()) {
               var2.append("BlockTime");
               var2.append(String.valueOf(this.bean.getBlockTime()));
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

            if (this.bean.isIdentityKeyStoreFileNameSet()) {
               var2.append("IdentityKeyStoreFileName");
               var2.append(String.valueOf(this.bean.getIdentityKeyStoreFileName()));
            }

            if (this.bean.isIdentityKeyStorePassPhraseSet()) {
               var2.append("IdentityKeyStorePassPhrase");
               var2.append(String.valueOf(this.bean.getIdentityKeyStorePassPhrase()));
            }

            if (this.bean.isIdentityKeyStorePassPhraseEncryptedSet()) {
               var2.append("IdentityKeyStorePassPhraseEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getIdentityKeyStorePassPhraseEncrypted())));
            }

            if (this.bean.isInteroperateSet()) {
               var2.append("Interoperate");
               var2.append(String.valueOf(this.bean.getInteroperate()));
            }

            if (this.bean.isKeepAliveSet()) {
               var2.append("KeepAlive");
               var2.append(String.valueOf(this.bean.getKeepAlive()));
            }

            if (this.bean.isKeepAliveWaitSet()) {
               var2.append("KeepAliveWait");
               var2.append(String.valueOf(this.bean.getKeepAliveWait()));
            }

            if (this.bean.isKeyStoresLocationSet()) {
               var2.append("KeyStoresLocation");
               var2.append(String.valueOf(this.bean.getKeyStoresLocation()));
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

            if (this.bean.isPrivateKeyAliasSet()) {
               var2.append("PrivateKeyAlias");
               var2.append(String.valueOf(this.bean.getPrivateKeyAlias()));
            }

            if (this.bean.isPrivateKeyPassPhraseSet()) {
               var2.append("PrivateKeyPassPhrase");
               var2.append(String.valueOf(this.bean.getPrivateKeyPassPhrase()));
            }

            if (this.bean.isPrivateKeyPassPhraseEncryptedSet()) {
               var2.append("PrivateKeyPassPhraseEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getPrivateKeyPassPhraseEncrypted())));
            }

            if (this.bean.isRetryIntervalSet()) {
               var2.append("RetryInterval");
               var2.append(String.valueOf(this.bean.getRetryInterval()));
            }

            if (this.bean.isSecuritySet()) {
               var2.append("Security");
               var2.append(String.valueOf(this.bean.getSecurity()));
            }

            if (this.bean.isTrustKeyStoreFileNameSet()) {
               var2.append("TrustKeyStoreFileName");
               var2.append(String.valueOf(this.bean.getTrustKeyStoreFileName()));
            }

            if (this.bean.isTrustKeyStorePassPhraseSet()) {
               var2.append("TrustKeyStorePassPhrase");
               var2.append(String.valueOf(this.bean.getTrustKeyStorePassPhrase()));
            }

            if (this.bean.isTrustKeyStorePassPhraseEncryptedSet()) {
               var2.append("TrustKeyStorePassPhraseEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getTrustKeyStorePassPhraseEncrypted())));
            }

            if (this.bean.isUseSSLSet()) {
               var2.append("UseSSL");
               var2.append(String.valueOf(this.bean.getUseSSL()));
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
            WTCLocalTuxDomMBeanImpl var2 = (WTCLocalTuxDomMBeanImpl)var1;
            this.computeDiff("AccessPoint", this.bean.getAccessPoint(), var2.getAccessPoint(), true);
            this.computeDiff("AccessPointId", this.bean.getAccessPointId(), var2.getAccessPointId(), true);
            this.computeDiff("BlockTime", this.bean.getBlockTime(), var2.getBlockTime(), true);
            this.computeDiff("CmpLimit", this.bean.getCmpLimit(), var2.getCmpLimit(), true);
            this.computeDiff("ConnPrincipalName", this.bean.getConnPrincipalName(), var2.getConnPrincipalName(), true);
            this.computeDiff("ConnectionPolicy", this.bean.getConnectionPolicy(), var2.getConnectionPolicy(), true);
            this.computeDiff("IdentityKeyStoreFileName", this.bean.getIdentityKeyStoreFileName(), var2.getIdentityKeyStoreFileName(), true);
            this.computeDiff("IdentityKeyStorePassPhraseEncrypted", this.bean.getIdentityKeyStorePassPhraseEncrypted(), var2.getIdentityKeyStorePassPhraseEncrypted(), true);
            this.computeDiff("Interoperate", this.bean.getInteroperate(), var2.getInteroperate(), true);
            this.computeDiff("KeepAlive", this.bean.getKeepAlive(), var2.getKeepAlive(), true);
            this.computeDiff("KeepAliveWait", this.bean.getKeepAliveWait(), var2.getKeepAliveWait(), true);
            this.computeDiff("KeyStoresLocation", this.bean.getKeyStoresLocation(), var2.getKeyStoresLocation(), true);
            this.computeDiff("MaxEncryptBits", this.bean.getMaxEncryptBits(), var2.getMaxEncryptBits(), true);
            this.computeDiff("MaxRetries", this.bean.getMaxRetries(), var2.getMaxRetries(), true);
            this.computeDiff("MinEncryptBits", this.bean.getMinEncryptBits(), var2.getMinEncryptBits(), true);
            this.computeDiff("NWAddr", this.bean.getNWAddr(), var2.getNWAddr(), true);
            this.computeDiff("PrivateKeyAlias", this.bean.getPrivateKeyAlias(), var2.getPrivateKeyAlias(), true);
            this.computeDiff("PrivateKeyPassPhraseEncrypted", this.bean.getPrivateKeyPassPhraseEncrypted(), var2.getPrivateKeyPassPhraseEncrypted(), true);
            this.computeDiff("RetryInterval", this.bean.getRetryInterval(), var2.getRetryInterval(), true);
            this.computeDiff("Security", this.bean.getSecurity(), var2.getSecurity(), true);
            this.computeDiff("TrustKeyStoreFileName", this.bean.getTrustKeyStoreFileName(), var2.getTrustKeyStoreFileName(), true);
            this.computeDiff("TrustKeyStorePassPhraseEncrypted", this.bean.getTrustKeyStorePassPhraseEncrypted(), var2.getTrustKeyStorePassPhraseEncrypted(), true);
            this.computeDiff("UseSSL", this.bean.getUseSSL(), var2.getUseSSL(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WTCLocalTuxDomMBeanImpl var3 = (WTCLocalTuxDomMBeanImpl)var1.getSourceBean();
            WTCLocalTuxDomMBeanImpl var4 = (WTCLocalTuxDomMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("AccessPoint")) {
                  var3.setAccessPoint(var4.getAccessPoint());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("AccessPointId")) {
                  var3.setAccessPointId(var4.getAccessPointId());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("BlockTime")) {
                  var3.setBlockTime(var4.getBlockTime());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("CmpLimit")) {
                  var3.setCmpLimit(var4.getCmpLimit());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
               } else if (var5.equals("ConnPrincipalName")) {
                  var3.setConnPrincipalName(var4.getConnPrincipalName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("ConnectionPolicy")) {
                  var3.setConnectionPolicy(var4.getConnectionPolicy());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("IdentityKeyStoreFileName")) {
                  var3.setIdentityKeyStoreFileName(var4.getIdentityKeyStoreFileName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 24);
               } else if (!var5.equals("IdentityKeyStorePassPhrase")) {
                  if (var5.equals("IdentityKeyStorePassPhraseEncrypted")) {
                     var3.setIdentityKeyStorePassPhraseEncrypted(var4.getIdentityKeyStorePassPhraseEncrypted());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 26);
                  } else if (var5.equals("Interoperate")) {
                     var3.setInteroperate(var4.getInteroperate());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 19);
                  } else if (var5.equals("KeepAlive")) {
                     var3.setKeepAlive(var4.getKeepAlive());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 20);
                  } else if (var5.equals("KeepAliveWait")) {
                     var3.setKeepAliveWait(var4.getKeepAliveWait());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 21);
                  } else if (var5.equals("KeyStoresLocation")) {
                     var3.setKeyStoresLocation(var4.getKeyStoresLocation());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 23);
                  } else if (var5.equals("MaxEncryptBits")) {
                     var3.setMaxEncryptBits(var4.getMaxEncryptBits());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 18);
                  } else if (var5.equals("MaxRetries")) {
                     var3.setMaxRetries(var4.getMaxRetries());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 13);
                  } else if (var5.equals("MinEncryptBits")) {
                     var3.setMinEncryptBits(var4.getMinEncryptBits());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 17);
                  } else if (var5.equals("NWAddr")) {
                     var3.setNWAddr(var4.getNWAddr());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 15);
                  } else if (var5.equals("PrivateKeyAlias")) {
                     var3.setPrivateKeyAlias(var4.getPrivateKeyAlias());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 27);
                  } else if (!var5.equals("PrivateKeyPassPhrase")) {
                     if (var5.equals("PrivateKeyPassPhraseEncrypted")) {
                        var3.setPrivateKeyPassPhraseEncrypted(var4.getPrivateKeyPassPhraseEncrypted());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 29);
                     } else if (var5.equals("RetryInterval")) {
                        var3.setRetryInterval(var4.getRetryInterval());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                     } else if (var5.equals("Security")) {
                        var3.setSecurity(var4.getSecurity());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 9);
                     } else if (var5.equals("TrustKeyStoreFileName")) {
                        var3.setTrustKeyStoreFileName(var4.getTrustKeyStoreFileName());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 30);
                     } else if (!var5.equals("TrustKeyStorePassPhrase")) {
                        if (var5.equals("TrustKeyStorePassPhraseEncrypted")) {
                           var3.setTrustKeyStorePassPhraseEncrypted(var4.getTrustKeyStorePassPhraseEncrypted());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 32);
                        } else if (var5.equals("UseSSL")) {
                           var3.setUseSSL(var4.getUseSSL());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 22);
                        } else {
                           super.applyPropertyUpdate(var1, var2);
                        }
                     }
                  }
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
            WTCLocalTuxDomMBeanImpl var5 = (WTCLocalTuxDomMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("AccessPoint")) && this.bean.isAccessPointSet()) {
               var5.setAccessPoint(this.bean.getAccessPoint());
            }

            if ((var3 == null || !var3.contains("AccessPointId")) && this.bean.isAccessPointIdSet()) {
               var5.setAccessPointId(this.bean.getAccessPointId());
            }

            if ((var3 == null || !var3.contains("BlockTime")) && this.bean.isBlockTimeSet()) {
               var5.setBlockTime(this.bean.getBlockTime());
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

            if ((var3 == null || !var3.contains("IdentityKeyStoreFileName")) && this.bean.isIdentityKeyStoreFileNameSet()) {
               var5.setIdentityKeyStoreFileName(this.bean.getIdentityKeyStoreFileName());
            }

            byte[] var4;
            if ((var3 == null || !var3.contains("IdentityKeyStorePassPhraseEncrypted")) && this.bean.isIdentityKeyStorePassPhraseEncryptedSet()) {
               var4 = this.bean.getIdentityKeyStorePassPhraseEncrypted();
               var5.setIdentityKeyStorePassPhraseEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("Interoperate")) && this.bean.isInteroperateSet()) {
               var5.setInteroperate(this.bean.getInteroperate());
            }

            if ((var3 == null || !var3.contains("KeepAlive")) && this.bean.isKeepAliveSet()) {
               var5.setKeepAlive(this.bean.getKeepAlive());
            }

            if ((var3 == null || !var3.contains("KeepAliveWait")) && this.bean.isKeepAliveWaitSet()) {
               var5.setKeepAliveWait(this.bean.getKeepAliveWait());
            }

            if ((var3 == null || !var3.contains("KeyStoresLocation")) && this.bean.isKeyStoresLocationSet()) {
               var5.setKeyStoresLocation(this.bean.getKeyStoresLocation());
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

            if ((var3 == null || !var3.contains("PrivateKeyAlias")) && this.bean.isPrivateKeyAliasSet()) {
               var5.setPrivateKeyAlias(this.bean.getPrivateKeyAlias());
            }

            if ((var3 == null || !var3.contains("PrivateKeyPassPhraseEncrypted")) && this.bean.isPrivateKeyPassPhraseEncryptedSet()) {
               var4 = this.bean.getPrivateKeyPassPhraseEncrypted();
               var5.setPrivateKeyPassPhraseEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("RetryInterval")) && this.bean.isRetryIntervalSet()) {
               var5.setRetryInterval(this.bean.getRetryInterval());
            }

            if ((var3 == null || !var3.contains("Security")) && this.bean.isSecuritySet()) {
               var5.setSecurity(this.bean.getSecurity());
            }

            if ((var3 == null || !var3.contains("TrustKeyStoreFileName")) && this.bean.isTrustKeyStoreFileNameSet()) {
               var5.setTrustKeyStoreFileName(this.bean.getTrustKeyStoreFileName());
            }

            if ((var3 == null || !var3.contains("TrustKeyStorePassPhraseEncrypted")) && this.bean.isTrustKeyStorePassPhraseEncryptedSet()) {
               var4 = this.bean.getTrustKeyStorePassPhraseEncrypted();
               var5.setTrustKeyStorePassPhraseEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("UseSSL")) && this.bean.isUseSSLSet()) {
               var5.setUseSSL(this.bean.getUseSSL());
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
