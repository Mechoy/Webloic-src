package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.zip.CRC32;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.beangen.StringHelper;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.FederationServices;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class FederationServicesMBeanImpl extends ConfigurationMBeanImpl implements FederationServicesMBean, Serializable {
   private boolean _ACSRequiresSSL;
   private boolean _ARSRequiresSSL;
   private boolean _ARSRequiresTwoWaySSL;
   private String[] _AssertionConsumerURIs;
   private String[] _AssertionRetrievalURIs;
   private String _AssertionStoreClassName;
   private Properties _AssertionStoreProperties;
   private boolean _DestinationSiteEnabled;
   private boolean _ITSRequiresSSL;
   private String[] _IntersiteTransferURIs;
   private String _Name;
   private boolean _POSTOneUseCheckEnabled;
   private boolean _POSTRecipientCheckEnabled;
   private String _SSLClientIdentityAlias;
   private String _SSLClientIdentityPassPhrase;
   private byte[] _SSLClientIdentityPassPhraseEncrypted;
   private String _SigningKeyAlias;
   private String _SigningKeyPassPhrase;
   private byte[] _SigningKeyPassPhraseEncrypted;
   private String _SourceIdBase64;
   private String _SourceIdHex;
   private boolean _SourceSiteEnabled;
   private String _SourceSiteURL;
   private String _UsedAssertionCacheClassName;
   private Properties _UsedAssertionCacheProperties;
   private FederationServices _customizer;
   private static SchemaHelper2 _schemaHelper;

   public FederationServicesMBeanImpl() {
      try {
         this._customizer = new FederationServices(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public FederationServicesMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new FederationServices(this);
      } catch (Exception var4) {
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         }

         throw new UndeclaredThrowableException(var4);
      }

      this._initializeProperty(-1);
   }

   public String getName() {
      if (!this._isSet(2)) {
         try {
            return ((ConfigurationMBean)this.getParent()).getName();
         } catch (NullPointerException var2) {
         }
      }

      return this._customizer.getName();
   }

   public boolean isNameSet() {
      return this._isSet(2);
   }

   public boolean isSourceSiteEnabled() {
      return this._SourceSiteEnabled;
   }

   public boolean isSourceSiteEnabledSet() {
      return this._isSet(7);
   }

   public void setName(String var1) throws InvalidAttributeValueException, ManagementException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("Name", var1);
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("Name", var1);
      ConfigurationValidator.validateName(var1);
      String var2 = this.getName();
      this._customizer.setName(var1);
      this._postSet(2, var2, var1);
   }

   public void setSourceSiteEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._SourceSiteEnabled;
      this._SourceSiteEnabled = var1;
      this._postSet(7, var2, var1);
   }

   public String getSourceSiteURL() {
      return this._SourceSiteURL;
   }

   public boolean isSourceSiteURLSet() {
      return this._isSet(8);
   }

   public void setSourceSiteURL(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._SourceSiteURL;
      this._SourceSiteURL = var1;
      this._postSet(8, var2, var1);
   }

   public String getSourceIdHex() {
      return this._customizer.getSourceIdHex();
   }

   public boolean isSourceIdHexSet() {
      return this._isSet(9);
   }

   public void setSourceIdHex(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._SourceIdHex = var1;
   }

   public String getSourceIdBase64() {
      return this._customizer.getSourceIdBase64();
   }

   public boolean isSourceIdBase64Set() {
      return this._isSet(10);
   }

   public void setSourceIdBase64(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this._SourceIdBase64 = var1;
   }

   public String[] getIntersiteTransferURIs() {
      return this._IntersiteTransferURIs;
   }

   public boolean isIntersiteTransferURIsSet() {
      return this._isSet(11);
   }

   public void setIntersiteTransferURIs(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this._IntersiteTransferURIs;
      this._IntersiteTransferURIs = var1;
      this._postSet(11, var2, var1);
   }

   public boolean isITSRequiresSSL() {
      return this._ITSRequiresSSL;
   }

   public boolean isITSRequiresSSLSet() {
      return this._isSet(12);
   }

   public void setITSRequiresSSL(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._ITSRequiresSSL;
      this._ITSRequiresSSL = var1;
      this._postSet(12, var2, var1);
   }

   public String[] getAssertionRetrievalURIs() {
      return this._AssertionRetrievalURIs;
   }

   public boolean isAssertionRetrievalURIsSet() {
      return this._isSet(13);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public void setAssertionRetrievalURIs(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this._AssertionRetrievalURIs;
      this._AssertionRetrievalURIs = var1;
      this._postSet(13, var2, var1);
   }

   public boolean isARSRequiresSSL() {
      return this._ARSRequiresSSL;
   }

   public boolean isARSRequiresSSLSet() {
      return this._isSet(14);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setARSRequiresSSL(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._ARSRequiresSSL;
      this._ARSRequiresSSL = var1;
      this._postSet(14, var2, var1);
   }

   public boolean isARSRequiresTwoWaySSL() {
      return this._ARSRequiresTwoWaySSL;
   }

   public boolean isARSRequiresTwoWaySSLSet() {
      return this._isSet(15);
   }

   public void setARSRequiresTwoWaySSL(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._ARSRequiresTwoWaySSL;
      this._ARSRequiresTwoWaySSL = var1;
      this._postSet(15, var2, var1);
   }

   public String getAssertionStoreClassName() {
      return this._AssertionStoreClassName;
   }

   public boolean isAssertionStoreClassNameSet() {
      return this._isSet(16);
   }

   public void setAssertionStoreClassName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._AssertionStoreClassName;
      this._AssertionStoreClassName = var1;
      this._postSet(16, var2, var1);
   }

   public Properties getAssertionStoreProperties() {
      return this._AssertionStoreProperties;
   }

   public String getAssertionStorePropertiesAsString() {
      return StringHelper.objectToString(this.getAssertionStoreProperties());
   }

   public boolean isAssertionStorePropertiesSet() {
      return this._isSet(17);
   }

   public void setAssertionStorePropertiesAsString(String var1) {
      try {
         this.setAssertionStoreProperties(StringHelper.stringToProperties(var1));
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public void setAssertionStoreProperties(Properties var1) throws InvalidAttributeValueException {
      Properties var2 = this._AssertionStoreProperties;
      this._AssertionStoreProperties = var1;
      this._postSet(17, var2, var1);
   }

   public String getSigningKeyAlias() {
      return this._SigningKeyAlias;
   }

   public boolean isSigningKeyAliasSet() {
      return this._isSet(18);
   }

   public void setSigningKeyAlias(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._SigningKeyAlias;
      this._SigningKeyAlias = var1;
      this._postSet(18, var2, var1);
   }

   public String getSigningKeyPassPhrase() {
      byte[] var1 = this.getSigningKeyPassPhraseEncrypted();
      return var1 == null ? null : this._decrypt("SigningKeyPassPhrase", var1);
   }

   public boolean isSigningKeyPassPhraseSet() {
      return this.isSigningKeyPassPhraseEncryptedSet();
   }

   public void setSigningKeyPassPhrase(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this.setSigningKeyPassPhraseEncrypted(var1 == null ? null : this._encrypt("SigningKeyPassPhrase", var1));
   }

   public byte[] getSigningKeyPassPhraseEncrypted() {
      return this._getHelper()._cloneArray(this._SigningKeyPassPhraseEncrypted);
   }

   public String getSigningKeyPassPhraseEncryptedAsString() {
      byte[] var1 = this.getSigningKeyPassPhraseEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isSigningKeyPassPhraseEncryptedSet() {
      return this._isSet(20);
   }

   public void setSigningKeyPassPhraseEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setSigningKeyPassPhraseEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public boolean isDestinationSiteEnabled() {
      return this._DestinationSiteEnabled;
   }

   public boolean isDestinationSiteEnabledSet() {
      return this._isSet(21);
   }

   public void setDestinationSiteEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._DestinationSiteEnabled;
      this._DestinationSiteEnabled = var1;
      this._postSet(21, var2, var1);
   }

   public String[] getAssertionConsumerURIs() {
      return this._AssertionConsumerURIs;
   }

   public boolean isAssertionConsumerURIsSet() {
      return this._isSet(22);
   }

   public void setAssertionConsumerURIs(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this._AssertionConsumerURIs;
      this._AssertionConsumerURIs = var1;
      this._postSet(22, var2, var1);
   }

   public boolean isACSRequiresSSL() {
      return this._ACSRequiresSSL;
   }

   public boolean isACSRequiresSSLSet() {
      return this._isSet(23);
   }

   public void setACSRequiresSSL(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._ACSRequiresSSL;
      this._ACSRequiresSSL = var1;
      this._postSet(23, var2, var1);
   }

   public boolean isPOSTRecipientCheckEnabled() {
      return this._POSTRecipientCheckEnabled;
   }

   public boolean isPOSTRecipientCheckEnabledSet() {
      return this._isSet(24);
   }

   public void setPOSTRecipientCheckEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._POSTRecipientCheckEnabled;
      this._POSTRecipientCheckEnabled = var1;
      this._postSet(24, var2, var1);
   }

   public boolean isPOSTOneUseCheckEnabled() {
      return this._POSTOneUseCheckEnabled;
   }

   public boolean isPOSTOneUseCheckEnabledSet() {
      return this._isSet(25);
   }

   public void setPOSTOneUseCheckEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._POSTOneUseCheckEnabled;
      this._POSTOneUseCheckEnabled = var1;
      this._postSet(25, var2, var1);
   }

   public String getUsedAssertionCacheClassName() {
      return this._UsedAssertionCacheClassName;
   }

   public boolean isUsedAssertionCacheClassNameSet() {
      return this._isSet(26);
   }

   public void setUsedAssertionCacheClassName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._UsedAssertionCacheClassName;
      this._UsedAssertionCacheClassName = var1;
      this._postSet(26, var2, var1);
   }

   public Properties getUsedAssertionCacheProperties() {
      return this._UsedAssertionCacheProperties;
   }

   public String getUsedAssertionCachePropertiesAsString() {
      return StringHelper.objectToString(this.getUsedAssertionCacheProperties());
   }

   public boolean isUsedAssertionCachePropertiesSet() {
      return this._isSet(27);
   }

   public void setUsedAssertionCachePropertiesAsString(String var1) {
      try {
         this.setUsedAssertionCacheProperties(StringHelper.stringToProperties(var1));
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public void setUsedAssertionCacheProperties(Properties var1) throws InvalidAttributeValueException {
      Properties var2 = this._UsedAssertionCacheProperties;
      this._UsedAssertionCacheProperties = var1;
      this._postSet(27, var2, var1);
   }

   public String getSSLClientIdentityAlias() {
      return this._SSLClientIdentityAlias;
   }

   public boolean isSSLClientIdentityAliasSet() {
      return this._isSet(28);
   }

   public void setSSLClientIdentityAlias(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._SSLClientIdentityAlias;
      this._SSLClientIdentityAlias = var1;
      this._postSet(28, var2, var1);
   }

   public String getSSLClientIdentityPassPhrase() {
      byte[] var1 = this.getSSLClientIdentityPassPhraseEncrypted();
      return var1 == null ? null : this._decrypt("SSLClientIdentityPassPhrase", var1);
   }

   public boolean isSSLClientIdentityPassPhraseSet() {
      return this.isSSLClientIdentityPassPhraseEncryptedSet();
   }

   public void setSSLClientIdentityPassPhrase(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this.setSSLClientIdentityPassPhraseEncrypted(var1 == null ? null : this._encrypt("SSLClientIdentityPassPhrase", var1));
   }

   public byte[] getSSLClientIdentityPassPhraseEncrypted() {
      return this._getHelper()._cloneArray(this._SSLClientIdentityPassPhraseEncrypted);
   }

   public String getSSLClientIdentityPassPhraseEncryptedAsString() {
      byte[] var1 = this.getSSLClientIdentityPassPhraseEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isSSLClientIdentityPassPhraseEncryptedSet() {
      return this._isSet(30);
   }

   public void setSSLClientIdentityPassPhraseEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setSSLClientIdentityPassPhraseEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      ServerLegalHelper.validateFederationServices(this);
   }

   public void setSSLClientIdentityPassPhraseEncrypted(byte[] var1) {
      byte[] var2 = this._SSLClientIdentityPassPhraseEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: SSLClientIdentityPassPhraseEncrypted of FederationServicesMBean");
      } else {
         this._getHelper()._clearArray(this._SSLClientIdentityPassPhraseEncrypted);
         this._SSLClientIdentityPassPhraseEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(30, var2, var1);
      }
   }

   public void setSigningKeyPassPhraseEncrypted(byte[] var1) {
      byte[] var2 = this._SigningKeyPassPhraseEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: SigningKeyPassPhraseEncrypted of FederationServicesMBean");
      } else {
         this._getHelper()._clearArray(this._SigningKeyPassPhraseEncrypted);
         this._SigningKeyPassPhraseEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(20, var2, var1);
      }
   }

   public boolean _hasKey() {
      return true;
   }

   public boolean _isPropertyAKey(Munger.ReaderEventInfo var1) {
      String var2 = var1.getElementName();
      switch (var2.length()) {
         case 4:
            if (var2.equals("name")) {
               return var1.compareXpaths(this._getPropertyXpath("name"));
            }

            return super._isPropertyAKey(var1);
         default:
            return super._isPropertyAKey(var1);
      }
   }

   protected void _unSet(int var1) {
      if (!this._initializeProperty(var1)) {
         super._unSet(var1);
      } else {
         this._markSet(var1, false);
         if (var1 == 29) {
            this._markSet(30, false);
         }

         if (var1 == 19) {
            this._markSet(20, false);
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
         var1 = 22;
      }

      try {
         switch (var1) {
            case 22:
               this._AssertionConsumerURIs = StringHelper.split("/samlacs/acs");
               if (var2) {
                  break;
               }
            case 13:
               this._AssertionRetrievalURIs = StringHelper.split("/samlars/ars");
               if (var2) {
                  break;
               }
            case 16:
               this._AssertionStoreClassName = null;
               if (var2) {
                  break;
               }
            case 17:
               this._AssertionStoreProperties = null;
               if (var2) {
                  break;
               }
            case 11:
               this._IntersiteTransferURIs = new String[]{"/samlits_ba/its", "/samlits_ba/its/post", "/samlits_ba/its/artifact", "/samlits_cc/its", "/samlits_cc/its/post", "/samlits_cc/its/artifact"};
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 28:
               this._SSLClientIdentityAlias = null;
               if (var2) {
                  break;
               }
            case 29:
               this._SSLClientIdentityPassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 30:
               this._SSLClientIdentityPassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 18:
               this._SigningKeyAlias = null;
               if (var2) {
                  break;
               }
            case 19:
               this._SigningKeyPassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 20:
               this._SigningKeyPassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 10:
               this._SourceIdBase64 = null;
               if (var2) {
                  break;
               }
            case 9:
               this._SourceIdHex = null;
               if (var2) {
                  break;
               }
            case 8:
               this._SourceSiteURL = null;
               if (var2) {
                  break;
               }
            case 26:
               this._UsedAssertionCacheClassName = null;
               if (var2) {
                  break;
               }
            case 27:
               this._UsedAssertionCacheProperties = null;
               if (var2) {
                  break;
               }
            case 23:
               this._ACSRequiresSSL = true;
               if (var2) {
                  break;
               }
            case 14:
               this._ARSRequiresSSL = true;
               if (var2) {
                  break;
               }
            case 15:
               this._ARSRequiresTwoWaySSL = false;
               if (var2) {
                  break;
               }
            case 21:
               this._DestinationSiteEnabled = false;
               if (var2) {
                  break;
               }
            case 12:
               this._ITSRequiresSSL = true;
               if (var2) {
                  break;
               }
            case 25:
               this._POSTOneUseCheckEnabled = true;
               if (var2) {
                  break;
               }
            case 24:
               this._POSTRecipientCheckEnabled = true;
               if (var2) {
                  break;
               }
            case 7:
               this._SourceSiteEnabled = false;
               if (var2) {
                  break;
               }
            case 3:
            case 4:
            case 5:
            case 6:
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
      return "FederationServices";
   }

   public void putValue(String var1, Object var2) {
      boolean var6;
      if (var1.equals("ACSRequiresSSL")) {
         var6 = this._ACSRequiresSSL;
         this._ACSRequiresSSL = (Boolean)var2;
         this._postSet(23, var6, this._ACSRequiresSSL);
      } else if (var1.equals("ARSRequiresSSL")) {
         var6 = this._ARSRequiresSSL;
         this._ARSRequiresSSL = (Boolean)var2;
         this._postSet(14, var6, this._ARSRequiresSSL);
      } else if (var1.equals("ARSRequiresTwoWaySSL")) {
         var6 = this._ARSRequiresTwoWaySSL;
         this._ARSRequiresTwoWaySSL = (Boolean)var2;
         this._postSet(15, var6, this._ARSRequiresTwoWaySSL);
      } else {
         String[] var8;
         if (var1.equals("AssertionConsumerURIs")) {
            var8 = this._AssertionConsumerURIs;
            this._AssertionConsumerURIs = (String[])((String[])var2);
            this._postSet(22, var8, this._AssertionConsumerURIs);
         } else if (var1.equals("AssertionRetrievalURIs")) {
            var8 = this._AssertionRetrievalURIs;
            this._AssertionRetrievalURIs = (String[])((String[])var2);
            this._postSet(13, var8, this._AssertionRetrievalURIs);
         } else {
            String var5;
            if (var1.equals("AssertionStoreClassName")) {
               var5 = this._AssertionStoreClassName;
               this._AssertionStoreClassName = (String)var2;
               this._postSet(16, var5, this._AssertionStoreClassName);
            } else {
               Properties var4;
               if (var1.equals("AssertionStoreProperties")) {
                  var4 = this._AssertionStoreProperties;
                  this._AssertionStoreProperties = (Properties)var2;
                  this._postSet(17, var4, this._AssertionStoreProperties);
               } else if (var1.equals("DestinationSiteEnabled")) {
                  var6 = this._DestinationSiteEnabled;
                  this._DestinationSiteEnabled = (Boolean)var2;
                  this._postSet(21, var6, this._DestinationSiteEnabled);
               } else if (var1.equals("ITSRequiresSSL")) {
                  var6 = this._ITSRequiresSSL;
                  this._ITSRequiresSSL = (Boolean)var2;
                  this._postSet(12, var6, this._ITSRequiresSSL);
               } else if (var1.equals("IntersiteTransferURIs")) {
                  var8 = this._IntersiteTransferURIs;
                  this._IntersiteTransferURIs = (String[])((String[])var2);
                  this._postSet(11, var8, this._IntersiteTransferURIs);
               } else if (var1.equals("Name")) {
                  var5 = this._Name;
                  this._Name = (String)var2;
                  this._postSet(2, var5, this._Name);
               } else if (var1.equals("POSTOneUseCheckEnabled")) {
                  var6 = this._POSTOneUseCheckEnabled;
                  this._POSTOneUseCheckEnabled = (Boolean)var2;
                  this._postSet(25, var6, this._POSTOneUseCheckEnabled);
               } else if (var1.equals("POSTRecipientCheckEnabled")) {
                  var6 = this._POSTRecipientCheckEnabled;
                  this._POSTRecipientCheckEnabled = (Boolean)var2;
                  this._postSet(24, var6, this._POSTRecipientCheckEnabled);
               } else if (var1.equals("SSLClientIdentityAlias")) {
                  var5 = this._SSLClientIdentityAlias;
                  this._SSLClientIdentityAlias = (String)var2;
                  this._postSet(28, var5, this._SSLClientIdentityAlias);
               } else if (var1.equals("SSLClientIdentityPassPhrase")) {
                  var5 = this._SSLClientIdentityPassPhrase;
                  this._SSLClientIdentityPassPhrase = (String)var2;
                  this._postSet(29, var5, this._SSLClientIdentityPassPhrase);
               } else {
                  byte[] var7;
                  if (var1.equals("SSLClientIdentityPassPhraseEncrypted")) {
                     var7 = this._SSLClientIdentityPassPhraseEncrypted;
                     this._SSLClientIdentityPassPhraseEncrypted = (byte[])((byte[])var2);
                     this._postSet(30, var7, this._SSLClientIdentityPassPhraseEncrypted);
                  } else if (var1.equals("SigningKeyAlias")) {
                     var5 = this._SigningKeyAlias;
                     this._SigningKeyAlias = (String)var2;
                     this._postSet(18, var5, this._SigningKeyAlias);
                  } else if (var1.equals("SigningKeyPassPhrase")) {
                     var5 = this._SigningKeyPassPhrase;
                     this._SigningKeyPassPhrase = (String)var2;
                     this._postSet(19, var5, this._SigningKeyPassPhrase);
                  } else if (var1.equals("SigningKeyPassPhraseEncrypted")) {
                     var7 = this._SigningKeyPassPhraseEncrypted;
                     this._SigningKeyPassPhraseEncrypted = (byte[])((byte[])var2);
                     this._postSet(20, var7, this._SigningKeyPassPhraseEncrypted);
                  } else if (var1.equals("SourceIdBase64")) {
                     var5 = this._SourceIdBase64;
                     this._SourceIdBase64 = (String)var2;
                     this._postSet(10, var5, this._SourceIdBase64);
                  } else if (var1.equals("SourceIdHex")) {
                     var5 = this._SourceIdHex;
                     this._SourceIdHex = (String)var2;
                     this._postSet(9, var5, this._SourceIdHex);
                  } else if (var1.equals("SourceSiteEnabled")) {
                     var6 = this._SourceSiteEnabled;
                     this._SourceSiteEnabled = (Boolean)var2;
                     this._postSet(7, var6, this._SourceSiteEnabled);
                  } else if (var1.equals("SourceSiteURL")) {
                     var5 = this._SourceSiteURL;
                     this._SourceSiteURL = (String)var2;
                     this._postSet(8, var5, this._SourceSiteURL);
                  } else if (var1.equals("UsedAssertionCacheClassName")) {
                     var5 = this._UsedAssertionCacheClassName;
                     this._UsedAssertionCacheClassName = (String)var2;
                     this._postSet(26, var5, this._UsedAssertionCacheClassName);
                  } else if (var1.equals("UsedAssertionCacheProperties")) {
                     var4 = this._UsedAssertionCacheProperties;
                     this._UsedAssertionCacheProperties = (Properties)var2;
                     this._postSet(27, var4, this._UsedAssertionCacheProperties);
                  } else if (var1.equals("customizer")) {
                     FederationServices var3 = this._customizer;
                     this._customizer = (FederationServices)var2;
                  } else {
                     super.putValue(var1, var2);
                  }
               }
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("ACSRequiresSSL")) {
         return new Boolean(this._ACSRequiresSSL);
      } else if (var1.equals("ARSRequiresSSL")) {
         return new Boolean(this._ARSRequiresSSL);
      } else if (var1.equals("ARSRequiresTwoWaySSL")) {
         return new Boolean(this._ARSRequiresTwoWaySSL);
      } else if (var1.equals("AssertionConsumerURIs")) {
         return this._AssertionConsumerURIs;
      } else if (var1.equals("AssertionRetrievalURIs")) {
         return this._AssertionRetrievalURIs;
      } else if (var1.equals("AssertionStoreClassName")) {
         return this._AssertionStoreClassName;
      } else if (var1.equals("AssertionStoreProperties")) {
         return this._AssertionStoreProperties;
      } else if (var1.equals("DestinationSiteEnabled")) {
         return new Boolean(this._DestinationSiteEnabled);
      } else if (var1.equals("ITSRequiresSSL")) {
         return new Boolean(this._ITSRequiresSSL);
      } else if (var1.equals("IntersiteTransferURIs")) {
         return this._IntersiteTransferURIs;
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("POSTOneUseCheckEnabled")) {
         return new Boolean(this._POSTOneUseCheckEnabled);
      } else if (var1.equals("POSTRecipientCheckEnabled")) {
         return new Boolean(this._POSTRecipientCheckEnabled);
      } else if (var1.equals("SSLClientIdentityAlias")) {
         return this._SSLClientIdentityAlias;
      } else if (var1.equals("SSLClientIdentityPassPhrase")) {
         return this._SSLClientIdentityPassPhrase;
      } else if (var1.equals("SSLClientIdentityPassPhraseEncrypted")) {
         return this._SSLClientIdentityPassPhraseEncrypted;
      } else if (var1.equals("SigningKeyAlias")) {
         return this._SigningKeyAlias;
      } else if (var1.equals("SigningKeyPassPhrase")) {
         return this._SigningKeyPassPhrase;
      } else if (var1.equals("SigningKeyPassPhraseEncrypted")) {
         return this._SigningKeyPassPhraseEncrypted;
      } else if (var1.equals("SourceIdBase64")) {
         return this._SourceIdBase64;
      } else if (var1.equals("SourceIdHex")) {
         return this._SourceIdHex;
      } else if (var1.equals("SourceSiteEnabled")) {
         return new Boolean(this._SourceSiteEnabled);
      } else if (var1.equals("SourceSiteURL")) {
         return this._SourceSiteURL;
      } else if (var1.equals("UsedAssertionCacheClassName")) {
         return this._UsedAssertionCacheClassName;
      } else if (var1.equals("UsedAssertionCacheProperties")) {
         return this._UsedAssertionCacheProperties;
      } else {
         return var1.equals("customizer") ? this._customizer : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 4:
               if (var1.equals("name")) {
                  return 2;
               }
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 14:
            case 18:
            case 20:
            case 21:
            case 27:
            case 29:
            case 30:
            case 32:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            default:
               break;
            case 13:
               if (var1.equals("source-id-hex")) {
                  return 9;
               }
               break;
            case 15:
               if (var1.equals("source-site-url")) {
                  return 8;
               }

               if (var1.equals("acs-requiresssl")) {
                  return 23;
               }

               if (var1.equals("ars-requiresssl")) {
                  return 14;
               }

               if (var1.equals("its-requiresssl")) {
                  return 12;
               }
               break;
            case 16:
               if (var1.equals("source-id-base64")) {
                  return 10;
               }
               break;
            case 17:
               if (var1.equals("signing-key-alias")) {
                  return 18;
               }
               break;
            case 19:
               if (var1.equals("source-site-enabled")) {
                  return 7;
               }
               break;
            case 22:
               if (var1.equals("assertion-consumer-uri")) {
                  return 22;
               }

               if (var1.equals("intersite-transfer-uri")) {
                  return 11;
               }
               break;
            case 23:
               if (var1.equals("assertion-retrieval-uri")) {
                  return 13;
               }

               if (var1.equals("signing-key-pass-phrase")) {
                  return 19;
               }

               if (var1.equals("ars-requires-two-wayssl")) {
                  return 15;
               }
               break;
            case 24:
               if (var1.equals("destination-site-enabled")) {
                  return 21;
               }
               break;
            case 25:
               if (var1.equals("ssl-client-identity-alias")) {
                  return 28;
               }
               break;
            case 26:
               if (var1.equals("assertion-store-class-name")) {
                  return 16;
               }

               if (var1.equals("assertion-store-properties")) {
                  return 17;
               }

               if (var1.equals("post-one-use-check-enabled")) {
                  return 25;
               }
               break;
            case 28:
               if (var1.equals("post-recipient-check-enabled")) {
                  return 24;
               }
               break;
            case 31:
               if (var1.equals("ssl-client-identity-pass-phrase")) {
                  return 29;
               }

               if (var1.equals("used-assertion-cache-class-name")) {
                  return 26;
               }

               if (var1.equals("used-assertion-cache-properties")) {
                  return 27;
               }
               break;
            case 33:
               if (var1.equals("signing-key-pass-phrase-encrypted")) {
                  return 20;
               }
               break;
            case 41:
               if (var1.equals("ssl-client-identity-pass-phrase-encrypted")) {
                  return 30;
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
            case 2:
               return "name";
            case 3:
            case 4:
            case 5:
            case 6:
            default:
               return super.getElementName(var1);
            case 7:
               return "source-site-enabled";
            case 8:
               return "source-site-url";
            case 9:
               return "source-id-hex";
            case 10:
               return "source-id-base64";
            case 11:
               return "intersite-transfer-uri";
            case 12:
               return "its-requiresssl";
            case 13:
               return "assertion-retrieval-uri";
            case 14:
               return "ars-requiresssl";
            case 15:
               return "ars-requires-two-wayssl";
            case 16:
               return "assertion-store-class-name";
            case 17:
               return "assertion-store-properties";
            case 18:
               return "signing-key-alias";
            case 19:
               return "signing-key-pass-phrase";
            case 20:
               return "signing-key-pass-phrase-encrypted";
            case 21:
               return "destination-site-enabled";
            case 22:
               return "assertion-consumer-uri";
            case 23:
               return "acs-requiresssl";
            case 24:
               return "post-recipient-check-enabled";
            case 25:
               return "post-one-use-check-enabled";
            case 26:
               return "used-assertion-cache-class-name";
            case 27:
               return "used-assertion-cache-properties";
            case 28:
               return "ssl-client-identity-alias";
            case 29:
               return "ssl-client-identity-pass-phrase";
            case 30:
               return "ssl-client-identity-pass-phrase-encrypted";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 11:
               return true;
            case 13:
               return true;
            case 22:
               return true;
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

      public boolean hasKey() {
         return true;
      }

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private FederationServicesMBeanImpl bean;

      protected Helper(FederationServicesMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 2:
               return "Name";
            case 3:
            case 4:
            case 5:
            case 6:
            default:
               return super.getPropertyName(var1);
            case 7:
               return "SourceSiteEnabled";
            case 8:
               return "SourceSiteURL";
            case 9:
               return "SourceIdHex";
            case 10:
               return "SourceIdBase64";
            case 11:
               return "IntersiteTransferURIs";
            case 12:
               return "ITSRequiresSSL";
            case 13:
               return "AssertionRetrievalURIs";
            case 14:
               return "ARSRequiresSSL";
            case 15:
               return "ARSRequiresTwoWaySSL";
            case 16:
               return "AssertionStoreClassName";
            case 17:
               return "AssertionStoreProperties";
            case 18:
               return "SigningKeyAlias";
            case 19:
               return "SigningKeyPassPhrase";
            case 20:
               return "SigningKeyPassPhraseEncrypted";
            case 21:
               return "DestinationSiteEnabled";
            case 22:
               return "AssertionConsumerURIs";
            case 23:
               return "ACSRequiresSSL";
            case 24:
               return "POSTRecipientCheckEnabled";
            case 25:
               return "POSTOneUseCheckEnabled";
            case 26:
               return "UsedAssertionCacheClassName";
            case 27:
               return "UsedAssertionCacheProperties";
            case 28:
               return "SSLClientIdentityAlias";
            case 29:
               return "SSLClientIdentityPassPhrase";
            case 30:
               return "SSLClientIdentityPassPhraseEncrypted";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("AssertionConsumerURIs")) {
            return 22;
         } else if (var1.equals("AssertionRetrievalURIs")) {
            return 13;
         } else if (var1.equals("AssertionStoreClassName")) {
            return 16;
         } else if (var1.equals("AssertionStoreProperties")) {
            return 17;
         } else if (var1.equals("IntersiteTransferURIs")) {
            return 11;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("SSLClientIdentityAlias")) {
            return 28;
         } else if (var1.equals("SSLClientIdentityPassPhrase")) {
            return 29;
         } else if (var1.equals("SSLClientIdentityPassPhraseEncrypted")) {
            return 30;
         } else if (var1.equals("SigningKeyAlias")) {
            return 18;
         } else if (var1.equals("SigningKeyPassPhrase")) {
            return 19;
         } else if (var1.equals("SigningKeyPassPhraseEncrypted")) {
            return 20;
         } else if (var1.equals("SourceIdBase64")) {
            return 10;
         } else if (var1.equals("SourceIdHex")) {
            return 9;
         } else if (var1.equals("SourceSiteURL")) {
            return 8;
         } else if (var1.equals("UsedAssertionCacheClassName")) {
            return 26;
         } else if (var1.equals("UsedAssertionCacheProperties")) {
            return 27;
         } else if (var1.equals("ACSRequiresSSL")) {
            return 23;
         } else if (var1.equals("ARSRequiresSSL")) {
            return 14;
         } else if (var1.equals("ARSRequiresTwoWaySSL")) {
            return 15;
         } else if (var1.equals("DestinationSiteEnabled")) {
            return 21;
         } else if (var1.equals("ITSRequiresSSL")) {
            return 12;
         } else if (var1.equals("POSTOneUseCheckEnabled")) {
            return 25;
         } else if (var1.equals("POSTRecipientCheckEnabled")) {
            return 24;
         } else {
            return var1.equals("SourceSiteEnabled") ? 7 : super.getPropertyIndex(var1);
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
            if (this.bean.isAssertionConsumerURIsSet()) {
               var2.append("AssertionConsumerURIs");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getAssertionConsumerURIs())));
            }

            if (this.bean.isAssertionRetrievalURIsSet()) {
               var2.append("AssertionRetrievalURIs");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getAssertionRetrievalURIs())));
            }

            if (this.bean.isAssertionStoreClassNameSet()) {
               var2.append("AssertionStoreClassName");
               var2.append(String.valueOf(this.bean.getAssertionStoreClassName()));
            }

            if (this.bean.isAssertionStorePropertiesSet()) {
               var2.append("AssertionStoreProperties");
               var2.append(String.valueOf(this.bean.getAssertionStoreProperties()));
            }

            if (this.bean.isIntersiteTransferURIsSet()) {
               var2.append("IntersiteTransferURIs");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getIntersiteTransferURIs())));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isSSLClientIdentityAliasSet()) {
               var2.append("SSLClientIdentityAlias");
               var2.append(String.valueOf(this.bean.getSSLClientIdentityAlias()));
            }

            if (this.bean.isSSLClientIdentityPassPhraseSet()) {
               var2.append("SSLClientIdentityPassPhrase");
               var2.append(String.valueOf(this.bean.getSSLClientIdentityPassPhrase()));
            }

            if (this.bean.isSSLClientIdentityPassPhraseEncryptedSet()) {
               var2.append("SSLClientIdentityPassPhraseEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getSSLClientIdentityPassPhraseEncrypted())));
            }

            if (this.bean.isSigningKeyAliasSet()) {
               var2.append("SigningKeyAlias");
               var2.append(String.valueOf(this.bean.getSigningKeyAlias()));
            }

            if (this.bean.isSigningKeyPassPhraseSet()) {
               var2.append("SigningKeyPassPhrase");
               var2.append(String.valueOf(this.bean.getSigningKeyPassPhrase()));
            }

            if (this.bean.isSigningKeyPassPhraseEncryptedSet()) {
               var2.append("SigningKeyPassPhraseEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getSigningKeyPassPhraseEncrypted())));
            }

            if (this.bean.isSourceIdBase64Set()) {
               var2.append("SourceIdBase64");
               var2.append(String.valueOf(this.bean.getSourceIdBase64()));
            }

            if (this.bean.isSourceIdHexSet()) {
               var2.append("SourceIdHex");
               var2.append(String.valueOf(this.bean.getSourceIdHex()));
            }

            if (this.bean.isSourceSiteURLSet()) {
               var2.append("SourceSiteURL");
               var2.append(String.valueOf(this.bean.getSourceSiteURL()));
            }

            if (this.bean.isUsedAssertionCacheClassNameSet()) {
               var2.append("UsedAssertionCacheClassName");
               var2.append(String.valueOf(this.bean.getUsedAssertionCacheClassName()));
            }

            if (this.bean.isUsedAssertionCachePropertiesSet()) {
               var2.append("UsedAssertionCacheProperties");
               var2.append(String.valueOf(this.bean.getUsedAssertionCacheProperties()));
            }

            if (this.bean.isACSRequiresSSLSet()) {
               var2.append("ACSRequiresSSL");
               var2.append(String.valueOf(this.bean.isACSRequiresSSL()));
            }

            if (this.bean.isARSRequiresSSLSet()) {
               var2.append("ARSRequiresSSL");
               var2.append(String.valueOf(this.bean.isARSRequiresSSL()));
            }

            if (this.bean.isARSRequiresTwoWaySSLSet()) {
               var2.append("ARSRequiresTwoWaySSL");
               var2.append(String.valueOf(this.bean.isARSRequiresTwoWaySSL()));
            }

            if (this.bean.isDestinationSiteEnabledSet()) {
               var2.append("DestinationSiteEnabled");
               var2.append(String.valueOf(this.bean.isDestinationSiteEnabled()));
            }

            if (this.bean.isITSRequiresSSLSet()) {
               var2.append("ITSRequiresSSL");
               var2.append(String.valueOf(this.bean.isITSRequiresSSL()));
            }

            if (this.bean.isPOSTOneUseCheckEnabledSet()) {
               var2.append("POSTOneUseCheckEnabled");
               var2.append(String.valueOf(this.bean.isPOSTOneUseCheckEnabled()));
            }

            if (this.bean.isPOSTRecipientCheckEnabledSet()) {
               var2.append("POSTRecipientCheckEnabled");
               var2.append(String.valueOf(this.bean.isPOSTRecipientCheckEnabled()));
            }

            if (this.bean.isSourceSiteEnabledSet()) {
               var2.append("SourceSiteEnabled");
               var2.append(String.valueOf(this.bean.isSourceSiteEnabled()));
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
            FederationServicesMBeanImpl var2 = (FederationServicesMBeanImpl)var1;
            this.computeDiff("AssertionConsumerURIs", this.bean.getAssertionConsumerURIs(), var2.getAssertionConsumerURIs(), false);
            this.computeDiff("AssertionRetrievalURIs", this.bean.getAssertionRetrievalURIs(), var2.getAssertionRetrievalURIs(), false);
            this.computeDiff("AssertionStoreClassName", this.bean.getAssertionStoreClassName(), var2.getAssertionStoreClassName(), false);
            this.computeDiff("AssertionStoreProperties", this.bean.getAssertionStoreProperties(), var2.getAssertionStoreProperties(), false);
            this.computeDiff("IntersiteTransferURIs", this.bean.getIntersiteTransferURIs(), var2.getIntersiteTransferURIs(), false);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("SSLClientIdentityAlias", this.bean.getSSLClientIdentityAlias(), var2.getSSLClientIdentityAlias(), true);
            this.computeDiff("SSLClientIdentityPassPhraseEncrypted", this.bean.getSSLClientIdentityPassPhraseEncrypted(), var2.getSSLClientIdentityPassPhraseEncrypted(), true);
            this.computeDiff("SigningKeyAlias", this.bean.getSigningKeyAlias(), var2.getSigningKeyAlias(), true);
            this.computeDiff("SigningKeyPassPhraseEncrypted", this.bean.getSigningKeyPassPhraseEncrypted(), var2.getSigningKeyPassPhraseEncrypted(), true);
            this.computeDiff("SourceSiteURL", this.bean.getSourceSiteURL(), var2.getSourceSiteURL(), false);
            this.computeDiff("UsedAssertionCacheClassName", this.bean.getUsedAssertionCacheClassName(), var2.getUsedAssertionCacheClassName(), false);
            this.computeDiff("UsedAssertionCacheProperties", this.bean.getUsedAssertionCacheProperties(), var2.getUsedAssertionCacheProperties(), false);
            this.computeDiff("ACSRequiresSSL", this.bean.isACSRequiresSSL(), var2.isACSRequiresSSL(), true);
            this.computeDiff("ARSRequiresSSL", this.bean.isARSRequiresSSL(), var2.isARSRequiresSSL(), true);
            this.computeDiff("ARSRequiresTwoWaySSL", this.bean.isARSRequiresTwoWaySSL(), var2.isARSRequiresTwoWaySSL(), true);
            this.computeDiff("DestinationSiteEnabled", this.bean.isDestinationSiteEnabled(), var2.isDestinationSiteEnabled(), false);
            this.computeDiff("ITSRequiresSSL", this.bean.isITSRequiresSSL(), var2.isITSRequiresSSL(), true);
            this.computeDiff("POSTOneUseCheckEnabled", this.bean.isPOSTOneUseCheckEnabled(), var2.isPOSTOneUseCheckEnabled(), false);
            this.computeDiff("POSTRecipientCheckEnabled", this.bean.isPOSTRecipientCheckEnabled(), var2.isPOSTRecipientCheckEnabled(), false);
            this.computeDiff("SourceSiteEnabled", this.bean.isSourceSiteEnabled(), var2.isSourceSiteEnabled(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            FederationServicesMBeanImpl var3 = (FederationServicesMBeanImpl)var1.getSourceBean();
            FederationServicesMBeanImpl var4 = (FederationServicesMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("AssertionConsumerURIs")) {
                  var3.setAssertionConsumerURIs(var4.getAssertionConsumerURIs());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 22);
               } else if (var5.equals("AssertionRetrievalURIs")) {
                  var3.setAssertionRetrievalURIs(var4.getAssertionRetrievalURIs());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("AssertionStoreClassName")) {
                  var3.setAssertionStoreClassName(var4.getAssertionStoreClassName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
               } else if (var5.equals("AssertionStoreProperties")) {
                  var3.setAssertionStoreProperties(var4.getAssertionStoreProperties() == null ? null : (Properties)var4.getAssertionStoreProperties().clone());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 17);
               } else if (var5.equals("IntersiteTransferURIs")) {
                  var3.setIntersiteTransferURIs(var4.getIntersiteTransferURIs());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("Name")) {
                  var3.setName(var4.getName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 2);
               } else if (var5.equals("SSLClientIdentityAlias")) {
                  var3.setSSLClientIdentityAlias(var4.getSSLClientIdentityAlias());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 28);
               } else if (!var5.equals("SSLClientIdentityPassPhrase")) {
                  if (var5.equals("SSLClientIdentityPassPhraseEncrypted")) {
                     var3.setSSLClientIdentityPassPhraseEncrypted(var4.getSSLClientIdentityPassPhraseEncrypted());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 30);
                  } else if (var5.equals("SigningKeyAlias")) {
                     var3.setSigningKeyAlias(var4.getSigningKeyAlias());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 18);
                  } else if (!var5.equals("SigningKeyPassPhrase")) {
                     if (var5.equals("SigningKeyPassPhraseEncrypted")) {
                        var3.setSigningKeyPassPhraseEncrypted(var4.getSigningKeyPassPhraseEncrypted());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 20);
                     } else if (!var5.equals("SourceIdBase64") && !var5.equals("SourceIdHex")) {
                        if (var5.equals("SourceSiteURL")) {
                           var3.setSourceSiteURL(var4.getSourceSiteURL());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 8);
                        } else if (var5.equals("UsedAssertionCacheClassName")) {
                           var3.setUsedAssertionCacheClassName(var4.getUsedAssertionCacheClassName());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 26);
                        } else if (var5.equals("UsedAssertionCacheProperties")) {
                           var3.setUsedAssertionCacheProperties(var4.getUsedAssertionCacheProperties() == null ? null : (Properties)var4.getUsedAssertionCacheProperties().clone());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 27);
                        } else if (var5.equals("ACSRequiresSSL")) {
                           var3.setACSRequiresSSL(var4.isACSRequiresSSL());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 23);
                        } else if (var5.equals("ARSRequiresSSL")) {
                           var3.setARSRequiresSSL(var4.isARSRequiresSSL());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 14);
                        } else if (var5.equals("ARSRequiresTwoWaySSL")) {
                           var3.setARSRequiresTwoWaySSL(var4.isARSRequiresTwoWaySSL());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 15);
                        } else if (var5.equals("DestinationSiteEnabled")) {
                           var3.setDestinationSiteEnabled(var4.isDestinationSiteEnabled());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 21);
                        } else if (var5.equals("ITSRequiresSSL")) {
                           var3.setITSRequiresSSL(var4.isITSRequiresSSL());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                        } else if (var5.equals("POSTOneUseCheckEnabled")) {
                           var3.setPOSTOneUseCheckEnabled(var4.isPOSTOneUseCheckEnabled());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 25);
                        } else if (var5.equals("POSTRecipientCheckEnabled")) {
                           var3.setPOSTRecipientCheckEnabled(var4.isPOSTRecipientCheckEnabled());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 24);
                        } else if (var5.equals("SourceSiteEnabled")) {
                           var3.setSourceSiteEnabled(var4.isSourceSiteEnabled());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 7);
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
            FederationServicesMBeanImpl var5 = (FederationServicesMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            String[] var4;
            if ((var3 == null || !var3.contains("AssertionConsumerURIs")) && this.bean.isAssertionConsumerURIsSet()) {
               var4 = this.bean.getAssertionConsumerURIs();
               var5.setAssertionConsumerURIs(var4 == null ? null : (String[])((String[])((String[])((String[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("AssertionRetrievalURIs")) && this.bean.isAssertionRetrievalURIsSet()) {
               var4 = this.bean.getAssertionRetrievalURIs();
               var5.setAssertionRetrievalURIs(var4 == null ? null : (String[])((String[])((String[])((String[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("AssertionStoreClassName")) && this.bean.isAssertionStoreClassNameSet()) {
               var5.setAssertionStoreClassName(this.bean.getAssertionStoreClassName());
            }

            if ((var3 == null || !var3.contains("AssertionStoreProperties")) && this.bean.isAssertionStorePropertiesSet()) {
               var5.setAssertionStoreProperties(this.bean.getAssertionStoreProperties());
            }

            if ((var3 == null || !var3.contains("IntersiteTransferURIs")) && this.bean.isIntersiteTransferURIsSet()) {
               var4 = this.bean.getIntersiteTransferURIs();
               var5.setIntersiteTransferURIs(var4 == null ? null : (String[])((String[])((String[])((String[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("SSLClientIdentityAlias")) && this.bean.isSSLClientIdentityAliasSet()) {
               var5.setSSLClientIdentityAlias(this.bean.getSSLClientIdentityAlias());
            }

            byte[] var8;
            if ((var3 == null || !var3.contains("SSLClientIdentityPassPhraseEncrypted")) && this.bean.isSSLClientIdentityPassPhraseEncryptedSet()) {
               var8 = this.bean.getSSLClientIdentityPassPhraseEncrypted();
               var5.setSSLClientIdentityPassPhraseEncrypted(var8 == null ? null : (byte[])((byte[])((byte[])((byte[])var8)).clone()));
            }

            if ((var3 == null || !var3.contains("SigningKeyAlias")) && this.bean.isSigningKeyAliasSet()) {
               var5.setSigningKeyAlias(this.bean.getSigningKeyAlias());
            }

            if ((var3 == null || !var3.contains("SigningKeyPassPhraseEncrypted")) && this.bean.isSigningKeyPassPhraseEncryptedSet()) {
               var8 = this.bean.getSigningKeyPassPhraseEncrypted();
               var5.setSigningKeyPassPhraseEncrypted(var8 == null ? null : (byte[])((byte[])((byte[])((byte[])var8)).clone()));
            }

            if ((var3 == null || !var3.contains("SourceSiteURL")) && this.bean.isSourceSiteURLSet()) {
               var5.setSourceSiteURL(this.bean.getSourceSiteURL());
            }

            if ((var3 == null || !var3.contains("UsedAssertionCacheClassName")) && this.bean.isUsedAssertionCacheClassNameSet()) {
               var5.setUsedAssertionCacheClassName(this.bean.getUsedAssertionCacheClassName());
            }

            if ((var3 == null || !var3.contains("UsedAssertionCacheProperties")) && this.bean.isUsedAssertionCachePropertiesSet()) {
               var5.setUsedAssertionCacheProperties(this.bean.getUsedAssertionCacheProperties());
            }

            if ((var3 == null || !var3.contains("ACSRequiresSSL")) && this.bean.isACSRequiresSSLSet()) {
               var5.setACSRequiresSSL(this.bean.isACSRequiresSSL());
            }

            if ((var3 == null || !var3.contains("ARSRequiresSSL")) && this.bean.isARSRequiresSSLSet()) {
               var5.setARSRequiresSSL(this.bean.isARSRequiresSSL());
            }

            if ((var3 == null || !var3.contains("ARSRequiresTwoWaySSL")) && this.bean.isARSRequiresTwoWaySSLSet()) {
               var5.setARSRequiresTwoWaySSL(this.bean.isARSRequiresTwoWaySSL());
            }

            if ((var3 == null || !var3.contains("DestinationSiteEnabled")) && this.bean.isDestinationSiteEnabledSet()) {
               var5.setDestinationSiteEnabled(this.bean.isDestinationSiteEnabled());
            }

            if ((var3 == null || !var3.contains("ITSRequiresSSL")) && this.bean.isITSRequiresSSLSet()) {
               var5.setITSRequiresSSL(this.bean.isITSRequiresSSL());
            }

            if ((var3 == null || !var3.contains("POSTOneUseCheckEnabled")) && this.bean.isPOSTOneUseCheckEnabledSet()) {
               var5.setPOSTOneUseCheckEnabled(this.bean.isPOSTOneUseCheckEnabled());
            }

            if ((var3 == null || !var3.contains("POSTRecipientCheckEnabled")) && this.bean.isPOSTRecipientCheckEnabledSet()) {
               var5.setPOSTRecipientCheckEnabled(this.bean.isPOSTRecipientCheckEnabled());
            }

            if ((var3 == null || !var3.contains("SourceSiteEnabled")) && this.bean.isSourceSiteEnabledSet()) {
               var5.setSourceSiteEnabled(this.bean.isSourceSiteEnabled());
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
