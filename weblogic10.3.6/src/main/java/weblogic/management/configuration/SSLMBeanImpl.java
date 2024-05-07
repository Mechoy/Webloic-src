package weblogic.management.configuration;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.management.ManagementException;
import weblogic.management.mbeans.custom.SSL;
import weblogic.utils.ArrayUtils;
import weblogic.utils.collections.CombinedIterator;

public class SSLMBeanImpl extends ConfigurationMBeanImpl implements SSLMBean, Serializable {
   private boolean _AllowUnencryptedNullCipher;
   private String _CertAuthenticator;
   private int _CertificateCacheSize;
   private String[] _Ciphersuites;
   private String _ClientCertAlias;
   private String _ClientCertPrivateKeyPassPhrase;
   private byte[] _ClientCertPrivateKeyPassPhraseEncrypted;
   private boolean _ClientCertificateEnforced;
   private boolean _Enabled;
   private int _ExportKeyLifespan;
   private boolean _HandlerEnabled;
   private boolean _HostnameVerificationIgnored;
   private String _HostnameVerifier;
   private String _IdentityAndTrustLocations;
   private String _InboundCertificateValidation;
   private boolean _JSSEEnabled;
   private boolean _KeyEncrypted;
   private int _ListenPort;
   private boolean _ListenPortEnabled;
   private int _LoginTimeoutMillis;
   private String _Name;
   private String _OutboundCertificateValidation;
   private String _OutboundPrivateKeyAlias;
   private String _OutboundPrivateKeyPassPhrase;
   private int _PeerValidationEnforced;
   private boolean _SSLRejectionLoggingEnabled;
   private String _ServerCertificateChainFileName;
   private String _ServerCertificateFileName;
   private String _ServerKeyFileName;
   private String _ServerPrivateKeyAlias;
   private String _ServerPrivateKeyPassPhrase;
   private byte[] _ServerPrivateKeyPassPhraseEncrypted;
   private String _TrustedCAFileName;
   private boolean _TwoWaySSLEnabled;
   private boolean _UseClientCertForOutbound;
   private boolean _UseJava;
   private boolean _UseServerCerts;
   private SSL _customizer;
   private static SchemaHelper2 _schemaHelper;

   public SSLMBeanImpl() {
      try {
         this._customizer = new SSL(this);
      } catch (Exception var2) {
         if (var2 instanceof RuntimeException) {
            throw (RuntimeException)var2;
         }

         throw new UndeclaredThrowableException(var2);
      }

      this._initializeProperty(-1);
   }

   public SSLMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);

      try {
         this._customizer = new SSL(this);
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

   public boolean isUseJava() {
      return this._UseJava;
   }

   public boolean isUseJavaSet() {
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

   public void setUseJava(boolean var1) {
      boolean var2 = this._UseJava;
      this._UseJava = var1;
      this._postSet(7, var2, var1);
   }

   public boolean isEnabled() {
      return this._Enabled;
   }

   public boolean isEnabledSet() {
      return this._isSet(8);
   }

   public void setEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._Enabled;
      this._Enabled = var1;
      this._postSet(8, var2, var1);
   }

   public String[] getCiphersuites() {
      return this._Ciphersuites;
   }

   public boolean isCiphersuitesSet() {
      return this._isSet(9);
   }

   public void setCiphersuites(String[] var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? new String[0] : var1;
      var1 = this._getHelper()._trimElements(var1);
      String[] var2 = this._Ciphersuites;
      this._Ciphersuites = var1;
      this._postSet(9, var2, var1);
   }

   public String getCertAuthenticator() {
      return this._CertAuthenticator;
   }

   public boolean isCertAuthenticatorSet() {
      return this._isSet(10);
   }

   public void setCertAuthenticator(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._CertAuthenticator;
      this._CertAuthenticator = var1;
      this._postSet(10, var2, var1);
   }

   public String getHostnameVerifier() {
      return this._HostnameVerifier;
   }

   public boolean isHostnameVerifierSet() {
      return this._isSet(11);
   }

   public void setHostnameVerifier(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._HostnameVerifier;
      this._HostnameVerifier = var1;
      this._postSet(11, var2, var1);
   }

   public boolean isHostnameVerificationIgnored() {
      return this._HostnameVerificationIgnored;
   }

   public boolean isHostnameVerificationIgnoredSet() {
      return this._isSet(12);
   }

   public void touch() throws ConfigurationException {
      this._customizer.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      this._customizer.freezeCurrentValue(var1);
   }

   public void setHostnameVerificationIgnored(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._HostnameVerificationIgnored;
      this._HostnameVerificationIgnored = var1;
      this._postSet(12, var2, var1);
   }

   public String getTrustedCAFileName() {
      return this._TrustedCAFileName;
   }

   public boolean isTrustedCAFileNameSet() {
      return this._isSet(13);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      this._customizer.restoreDefaultValue(var1);
   }

   public void setTrustedCAFileName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._TrustedCAFileName;
      this._TrustedCAFileName = var1;
      this._postSet(13, var2, var1);
   }

   public int getPeerValidationEnforced() {
      return this._PeerValidationEnforced;
   }

   public boolean isPeerValidationEnforcedSet() {
      return this._isSet(14);
   }

   public void setPeerValidationEnforced(int var1) throws InvalidAttributeValueException {
      this._PeerValidationEnforced = var1;
   }

   public boolean isKeyEncrypted() {
      return this._KeyEncrypted;
   }

   public boolean isKeyEncryptedSet() {
      return this._isSet(15);
   }

   public void setKeyEncrypted(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._KeyEncrypted;
      this._KeyEncrypted = var1;
      this._postSet(15, var2, var1);
   }

   public int getExportKeyLifespan() {
      return this._ExportKeyLifespan;
   }

   public boolean isExportKeyLifespanSet() {
      return this._isSet(16);
   }

   public void setExportKeyLifespan(int var1) throws InvalidAttributeValueException {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ExportKeyLifespan", (long)var1, 1L, 2147483647L);
      int var2 = this._ExportKeyLifespan;
      this._ExportKeyLifespan = var1;
      this._postSet(16, var2, var1);
   }

   public boolean isClientCertificateEnforced() {
      return this._ClientCertificateEnforced;
   }

   public boolean isClientCertificateEnforcedSet() {
      return this._isSet(17);
   }

   public void setClientCertificateEnforced(boolean var1) {
      boolean var2 = this._ClientCertificateEnforced;
      this._ClientCertificateEnforced = var1;
      this._postSet(17, var2, var1);
   }

   public String getServerCertificateFileName() {
      return this._ServerCertificateFileName;
   }

   public boolean isServerCertificateFileNameSet() {
      return this._isSet(18);
   }

   public void setServerCertificateFileName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ServerCertificateFileName;
      this._ServerCertificateFileName = var1;
      this._postSet(18, var2, var1);
   }

   public int getListenPort() {
      return this._ListenPort;
   }

   public boolean isListenPortSet() {
      return this._isSet(19);
   }

   public void setListenPort(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("ListenPort", (long)var1, 1L, 65535L);
      int var2 = this._ListenPort;
      this._ListenPort = var1;
      this._postSet(19, var2, var1);
   }

   public boolean isListenPortEnabled() {
      return this._customizer.isListenPortEnabled();
   }

   public boolean isListenPortEnabledSet() {
      return this._isSet(20);
   }

   public void setListenPortEnabled(boolean var1) throws InvalidAttributeValueException {
      this._ListenPortEnabled = var1;
   }

   public String getServerCertificateChainFileName() {
      return this._ServerCertificateChainFileName;
   }

   public boolean isServerCertificateChainFileNameSet() {
      return this._isSet(21);
   }

   public void setServerCertificateChainFileName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ServerCertificateChainFileName;
      this._ServerCertificateChainFileName = var1;
      this._postSet(21, var2, var1);
   }

   public int getCertificateCacheSize() {
      return this._CertificateCacheSize;
   }

   public boolean isCertificateCacheSizeSet() {
      return this._isSet(22);
   }

   public void setCertificateCacheSize(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("CertificateCacheSize", (long)var1, 1L, 2147483647L);
      int var2 = this._CertificateCacheSize;
      this._CertificateCacheSize = var1;
      this._postSet(22, var2, var1);
   }

   public boolean isHandlerEnabled() {
      return this._HandlerEnabled;
   }

   public boolean isHandlerEnabledSet() {
      return this._isSet(23);
   }

   public void setHandlerEnabled(boolean var1) {
      boolean var2 = this._HandlerEnabled;
      this._HandlerEnabled = var1;
      this._postSet(23, var2, var1);
   }

   public int getLoginTimeoutMillis() {
      return this._LoginTimeoutMillis;
   }

   public boolean isLoginTimeoutMillisSet() {
      return this._isSet(24);
   }

   public void setLoginTimeoutMillis(int var1) {
      weblogic.descriptor.beangen.LegalChecks.checkInRange("LoginTimeoutMillis", (long)var1, 1L, 2147483647L);
      int var2 = this._LoginTimeoutMillis;
      this._LoginTimeoutMillis = var1;
      this._postSet(24, var2, var1);
   }

   public String getServerKeyFileName() {
      return this._ServerKeyFileName;
   }

   public boolean isServerKeyFileNameSet() {
      return this._isSet(25);
   }

   public void setServerKeyFileName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ServerKeyFileName;
      this._ServerKeyFileName = var1;
      this._postSet(25, var2, var1);
   }

   public boolean isTwoWaySSLEnabled() {
      return this._TwoWaySSLEnabled;
   }

   public boolean isTwoWaySSLEnabledSet() {
      return this._isSet(26);
   }

   public void setTwoWaySSLEnabled(boolean var1) {
      boolean var2 = this._TwoWaySSLEnabled;
      this._TwoWaySSLEnabled = var1;
      this._postSet(26, var2, var1);
   }

   public String getServerPrivateKeyAlias() {
      return this._ServerPrivateKeyAlias;
   }

   public boolean isServerPrivateKeyAliasSet() {
      return this._isSet(27);
   }

   public void setServerPrivateKeyAlias(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ServerPrivateKeyAlias;
      this._ServerPrivateKeyAlias = var1;
      this._postSet(27, var2, var1);
   }

   public String getServerPrivateKeyPassPhrase() {
      byte[] var1 = this.getServerPrivateKeyPassPhraseEncrypted();
      return var1 == null ? null : this._decrypt("ServerPrivateKeyPassPhrase", var1);
   }

   public boolean isServerPrivateKeyPassPhraseSet() {
      return this.isServerPrivateKeyPassPhraseEncryptedSet();
   }

   public void setServerPrivateKeyPassPhrase(String var1) {
      var1 = var1 == null ? null : var1.trim();
      this.setServerPrivateKeyPassPhraseEncrypted(var1 == null ? null : this._encrypt("ServerPrivateKeyPassPhrase", var1));
   }

   public byte[] getServerPrivateKeyPassPhraseEncrypted() {
      return this._getHelper()._cloneArray(this._ServerPrivateKeyPassPhraseEncrypted);
   }

   public String getServerPrivateKeyPassPhraseEncryptedAsString() {
      byte[] var1 = this.getServerPrivateKeyPassPhraseEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isServerPrivateKeyPassPhraseEncryptedSet() {
      return this._isSet(29);
   }

   public void setServerPrivateKeyPassPhraseEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setServerPrivateKeyPassPhraseEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public boolean isSSLRejectionLoggingEnabled() {
      return this._SSLRejectionLoggingEnabled;
   }

   public boolean isSSLRejectionLoggingEnabledSet() {
      return this._isSet(30);
   }

   public void setSSLRejectionLoggingEnabled(boolean var1) {
      boolean var2 = this._SSLRejectionLoggingEnabled;
      this._SSLRejectionLoggingEnabled = var1;
      this._postSet(30, var2, var1);
   }

   public String getIdentityAndTrustLocations() {
      return this._IdentityAndTrustLocations;
   }

   public boolean isIdentityAndTrustLocationsSet() {
      return this._isSet(31);
   }

   public void setIdentityAndTrustLocations(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"KeyStores", "FilesOrKeyStoreProviders"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("IdentityAndTrustLocations", var1, var2);
      String var3 = this._IdentityAndTrustLocations;
      this._IdentityAndTrustLocations = var1;
      this._postSet(31, var3, var1);
   }

   public String getInboundCertificateValidation() {
      return this._InboundCertificateValidation;
   }

   public boolean isInboundCertificateValidationSet() {
      return this._isSet(32);
   }

   public void setInboundCertificateValidation(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"BuiltinSSLValidationOnly", "BuiltinSSLValidationAndCertPathValidators"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("InboundCertificateValidation", var1, var2);
      String var3 = this._InboundCertificateValidation;
      this._InboundCertificateValidation = var1;
      this._postSet(32, var3, var1);
   }

   public String getOutboundCertificateValidation() {
      return this._OutboundCertificateValidation;
   }

   public boolean isOutboundCertificateValidationSet() {
      return this._isSet(33);
   }

   public void setOutboundCertificateValidation(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"BuiltinSSLValidationOnly", "BuiltinSSLValidationAndCertPathValidators"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("OutboundCertificateValidation", var1, var2);
      String var3 = this._OutboundCertificateValidation;
      this._OutboundCertificateValidation = var1;
      this._postSet(33, var3, var1);
   }

   public void setAllowUnencryptedNullCipher(boolean var1) {
      boolean var2 = this._AllowUnencryptedNullCipher;
      this._AllowUnencryptedNullCipher = var1;
      this._postSet(34, var2, var1);
   }

   public boolean isAllowUnencryptedNullCipher() {
      return this._AllowUnencryptedNullCipher;
   }

   public boolean isAllowUnencryptedNullCipherSet() {
      return this._isSet(34);
   }

   public boolean isUseServerCerts() {
      return this._UseServerCerts;
   }

   public boolean isUseServerCertsSet() {
      return this._isSet(35);
   }

   public void setUseServerCerts(boolean var1) {
      boolean var2 = this._UseServerCerts;
      this._UseServerCerts = var1;
      this._postSet(35, var2, var1);
   }

   public void setJSSEEnabled(boolean var1) {
      boolean var2 = this._JSSEEnabled;
      this._JSSEEnabled = var1;
      this._postSet(36, var2, var1);
   }

   public boolean isJSSEEnabled() {
      if (!this._isSet(36)) {
         try {
            return ((DomainMBean)((ServerMBean)this.getParent()).getParent()).isExalogicOptimizationsEnabled();
         } catch (NullPointerException var2) {
         }
      }

      return this._JSSEEnabled;
   }

   public boolean isJSSEEnabledSet() {
      return this._isSet(36);
   }

   public void setUseClientCertForOutbound(boolean var1) {
      boolean var2 = this._UseClientCertForOutbound;
      this._UseClientCertForOutbound = var1;
      this._postSet(37, var2, var1);
   }

   public boolean isUseClientCertForOutbound() {
      return this._UseClientCertForOutbound;
   }

   public boolean isUseClientCertForOutboundSet() {
      return this._isSet(37);
   }

   public void setClientCertAlias(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ClientCertAlias;
      this._ClientCertAlias = var1;
      this._postSet(38, var2, var1);
   }

   public String getClientCertAlias() {
      return this._ClientCertAlias;
   }

   public boolean isClientCertAliasSet() {
      return this._isSet(38);
   }

   public String getClientCertPrivateKeyPassPhrase() {
      byte[] var1 = this.getClientCertPrivateKeyPassPhraseEncrypted();
      return var1 == null ? null : this._decrypt("ClientCertPrivateKeyPassPhrase", var1);
   }

   public boolean isClientCertPrivateKeyPassPhraseSet() {
      return this.isClientCertPrivateKeyPassPhraseEncryptedSet();
   }

   public void setClientCertPrivateKeyPassPhrase(String var1) {
      var1 = var1 == null ? null : var1.trim();
      this.setClientCertPrivateKeyPassPhraseEncrypted(var1 == null ? null : this._encrypt("ClientCertPrivateKeyPassPhrase", var1));
   }

   public byte[] getClientCertPrivateKeyPassPhraseEncrypted() {
      return this._getHelper()._cloneArray(this._ClientCertPrivateKeyPassPhraseEncrypted);
   }

   public String getClientCertPrivateKeyPassPhraseEncryptedAsString() {
      byte[] var1 = this.getClientCertPrivateKeyPassPhraseEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isClientCertPrivateKeyPassPhraseEncryptedSet() {
      return this._isSet(40);
   }

   public void setClientCertPrivateKeyPassPhraseEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setClientCertPrivateKeyPassPhraseEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public String getOutboundPrivateKeyAlias() {
      if (!this._isSet(41)) {
         try {
            return this.isUseClientCertForOutbound() ? this.getClientCertAlias() : this.getServerPrivateKeyAlias();
         } catch (NullPointerException var2) {
         }
      }

      return this._OutboundPrivateKeyAlias;
   }

   public boolean isOutboundPrivateKeyAliasSet() {
      return this._isSet(41);
   }

   public void setOutboundPrivateKeyAlias(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._OutboundPrivateKeyAlias;
      this._OutboundPrivateKeyAlias = var1;
      this._postSet(41, var2, var1);
   }

   public String getOutboundPrivateKeyPassPhrase() {
      if (!this._isSet(42)) {
         try {
            return this.isUseClientCertForOutbound() ? this.getClientCertPrivateKeyPassPhrase() : this.getServerPrivateKeyPassPhrase();
         } catch (NullPointerException var2) {
         }
      }

      return this._OutboundPrivateKeyPassPhrase;
   }

   public boolean isOutboundPrivateKeyPassPhraseSet() {
      return this._isSet(42);
   }

   public void setOutboundPrivateKeyPassPhrase(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._OutboundPrivateKeyPassPhrase;
      this._OutboundPrivateKeyPassPhrase = var1;
      this._postSet(42, var2, var1);
   }

   public Object _getKey() {
      return this.getName();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      ServerLegalHelper.validateSSL(this);
   }

   public void setClientCertPrivateKeyPassPhraseEncrypted(byte[] var1) {
      byte[] var2 = this._ClientCertPrivateKeyPassPhraseEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: ClientCertPrivateKeyPassPhraseEncrypted of SSLMBean");
      } else {
         this._getHelper()._clearArray(this._ClientCertPrivateKeyPassPhraseEncrypted);
         this._ClientCertPrivateKeyPassPhraseEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(40, var2, var1);
      }
   }

   public void setServerPrivateKeyPassPhraseEncrypted(byte[] var1) {
      byte[] var2 = this._ServerPrivateKeyPassPhraseEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: ServerPrivateKeyPassPhraseEncrypted of SSLMBean");
      } else {
         this._getHelper()._clearArray(this._ServerPrivateKeyPassPhraseEncrypted);
         this._ServerPrivateKeyPassPhraseEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(29, var2, var1);
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
         if (var1 == 39) {
            this._markSet(40, false);
         }

         if (var1 == 28) {
            this._markSet(29, false);
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
         var1 = 10;
      }

      try {
         switch (var1) {
            case 10:
               this._CertAuthenticator = null;
               if (var2) {
                  break;
               }
            case 22:
               this._CertificateCacheSize = 3;
               if (var2) {
                  break;
               }
            case 9:
               this._Ciphersuites = new String[0];
               if (var2) {
                  break;
               }
            case 38:
               this._ClientCertAlias = null;
               if (var2) {
                  break;
               }
            case 39:
               this._ClientCertPrivateKeyPassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 40:
               this._ClientCertPrivateKeyPassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 16:
               this._ExportKeyLifespan = 500;
               if (var2) {
                  break;
               }
            case 11:
               this._HostnameVerifier = null;
               if (var2) {
                  break;
               }
            case 31:
               this._IdentityAndTrustLocations = "KeyStores";
               if (var2) {
                  break;
               }
            case 32:
               this._InboundCertificateValidation = "BuiltinSSLValidationOnly";
               if (var2) {
                  break;
               }
            case 19:
               this._ListenPort = 7002;
               if (var2) {
                  break;
               }
            case 24:
               this._LoginTimeoutMillis = 25000;
               if (var2) {
                  break;
               }
            case 2:
               this._customizer.setName((String)null);
               if (var2) {
                  break;
               }
            case 33:
               this._OutboundCertificateValidation = "BuiltinSSLValidationOnly";
               if (var2) {
                  break;
               }
            case 41:
               this._OutboundPrivateKeyAlias = null;
               if (var2) {
                  break;
               }
            case 42:
               this._OutboundPrivateKeyPassPhrase = null;
               if (var2) {
                  break;
               }
            case 14:
               this._PeerValidationEnforced = 0;
               if (var2) {
                  break;
               }
            case 21:
               this._ServerCertificateChainFileName = "server-certchain.pem";
               if (var2) {
                  break;
               }
            case 18:
               this._ServerCertificateFileName = "server-cert.der";
               if (var2) {
                  break;
               }
            case 25:
               this._ServerKeyFileName = "server-key.der";
               if (var2) {
                  break;
               }
            case 27:
               this._ServerPrivateKeyAlias = null;
               if (var2) {
                  break;
               }
            case 28:
               this._ServerPrivateKeyPassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 29:
               this._ServerPrivateKeyPassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 13:
               this._TrustedCAFileName = "trusted-ca.pem";
               if (var2) {
                  break;
               }
            case 34:
               this._AllowUnencryptedNullCipher = false;
               if (var2) {
                  break;
               }
            case 17:
               this._ClientCertificateEnforced = false;
               if (var2) {
                  break;
               }
            case 8:
               this._Enabled = false;
               if (var2) {
                  break;
               }
            case 23:
               this._HandlerEnabled = true;
               if (var2) {
                  break;
               }
            case 12:
               this._HostnameVerificationIgnored = false;
               if (var2) {
                  break;
               }
            case 36:
               this._JSSEEnabled = false;
               if (var2) {
                  break;
               }
            case 15:
               this._KeyEncrypted = false;
               if (var2) {
                  break;
               }
            case 20:
               this._ListenPortEnabled = false;
               if (var2) {
                  break;
               }
            case 30:
               this._SSLRejectionLoggingEnabled = true;
               if (var2) {
                  break;
               }
            case 26:
               this._TwoWaySSLEnabled = false;
               if (var2) {
                  break;
               }
            case 37:
               this._UseClientCertForOutbound = false;
               if (var2) {
                  break;
               }
            case 7:
               this._UseJava = true;
               if (var2) {
                  break;
               }
            case 35:
               this._UseServerCerts = false;
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
      return "SSL";
   }

   public void putValue(String var1, Object var2) {
      boolean var4;
      if (var1.equals("AllowUnencryptedNullCipher")) {
         var4 = this._AllowUnencryptedNullCipher;
         this._AllowUnencryptedNullCipher = (Boolean)var2;
         this._postSet(34, var4, this._AllowUnencryptedNullCipher);
      } else {
         String var5;
         if (var1.equals("CertAuthenticator")) {
            var5 = this._CertAuthenticator;
            this._CertAuthenticator = (String)var2;
            this._postSet(10, var5, this._CertAuthenticator);
         } else {
            int var7;
            if (var1.equals("CertificateCacheSize")) {
               var7 = this._CertificateCacheSize;
               this._CertificateCacheSize = (Integer)var2;
               this._postSet(22, var7, this._CertificateCacheSize);
            } else if (var1.equals("Ciphersuites")) {
               String[] var8 = this._Ciphersuites;
               this._Ciphersuites = (String[])((String[])var2);
               this._postSet(9, var8, this._Ciphersuites);
            } else if (var1.equals("ClientCertAlias")) {
               var5 = this._ClientCertAlias;
               this._ClientCertAlias = (String)var2;
               this._postSet(38, var5, this._ClientCertAlias);
            } else if (var1.equals("ClientCertPrivateKeyPassPhrase")) {
               var5 = this._ClientCertPrivateKeyPassPhrase;
               this._ClientCertPrivateKeyPassPhrase = (String)var2;
               this._postSet(39, var5, this._ClientCertPrivateKeyPassPhrase);
            } else {
               byte[] var6;
               if (var1.equals("ClientCertPrivateKeyPassPhraseEncrypted")) {
                  var6 = this._ClientCertPrivateKeyPassPhraseEncrypted;
                  this._ClientCertPrivateKeyPassPhraseEncrypted = (byte[])((byte[])var2);
                  this._postSet(40, var6, this._ClientCertPrivateKeyPassPhraseEncrypted);
               } else if (var1.equals("ClientCertificateEnforced")) {
                  var4 = this._ClientCertificateEnforced;
                  this._ClientCertificateEnforced = (Boolean)var2;
                  this._postSet(17, var4, this._ClientCertificateEnforced);
               } else if (var1.equals("Enabled")) {
                  var4 = this._Enabled;
                  this._Enabled = (Boolean)var2;
                  this._postSet(8, var4, this._Enabled);
               } else if (var1.equals("ExportKeyLifespan")) {
                  var7 = this._ExportKeyLifespan;
                  this._ExportKeyLifespan = (Integer)var2;
                  this._postSet(16, var7, this._ExportKeyLifespan);
               } else if (var1.equals("HandlerEnabled")) {
                  var4 = this._HandlerEnabled;
                  this._HandlerEnabled = (Boolean)var2;
                  this._postSet(23, var4, this._HandlerEnabled);
               } else if (var1.equals("HostnameVerificationIgnored")) {
                  var4 = this._HostnameVerificationIgnored;
                  this._HostnameVerificationIgnored = (Boolean)var2;
                  this._postSet(12, var4, this._HostnameVerificationIgnored);
               } else if (var1.equals("HostnameVerifier")) {
                  var5 = this._HostnameVerifier;
                  this._HostnameVerifier = (String)var2;
                  this._postSet(11, var5, this._HostnameVerifier);
               } else if (var1.equals("IdentityAndTrustLocations")) {
                  var5 = this._IdentityAndTrustLocations;
                  this._IdentityAndTrustLocations = (String)var2;
                  this._postSet(31, var5, this._IdentityAndTrustLocations);
               } else if (var1.equals("InboundCertificateValidation")) {
                  var5 = this._InboundCertificateValidation;
                  this._InboundCertificateValidation = (String)var2;
                  this._postSet(32, var5, this._InboundCertificateValidation);
               } else if (var1.equals("JSSEEnabled")) {
                  var4 = this._JSSEEnabled;
                  this._JSSEEnabled = (Boolean)var2;
                  this._postSet(36, var4, this._JSSEEnabled);
               } else if (var1.equals("KeyEncrypted")) {
                  var4 = this._KeyEncrypted;
                  this._KeyEncrypted = (Boolean)var2;
                  this._postSet(15, var4, this._KeyEncrypted);
               } else if (var1.equals("ListenPort")) {
                  var7 = this._ListenPort;
                  this._ListenPort = (Integer)var2;
                  this._postSet(19, var7, this._ListenPort);
               } else if (var1.equals("ListenPortEnabled")) {
                  var4 = this._ListenPortEnabled;
                  this._ListenPortEnabled = (Boolean)var2;
                  this._postSet(20, var4, this._ListenPortEnabled);
               } else if (var1.equals("LoginTimeoutMillis")) {
                  var7 = this._LoginTimeoutMillis;
                  this._LoginTimeoutMillis = (Integer)var2;
                  this._postSet(24, var7, this._LoginTimeoutMillis);
               } else if (var1.equals("Name")) {
                  var5 = this._Name;
                  this._Name = (String)var2;
                  this._postSet(2, var5, this._Name);
               } else if (var1.equals("OutboundCertificateValidation")) {
                  var5 = this._OutboundCertificateValidation;
                  this._OutboundCertificateValidation = (String)var2;
                  this._postSet(33, var5, this._OutboundCertificateValidation);
               } else if (var1.equals("OutboundPrivateKeyAlias")) {
                  var5 = this._OutboundPrivateKeyAlias;
                  this._OutboundPrivateKeyAlias = (String)var2;
                  this._postSet(41, var5, this._OutboundPrivateKeyAlias);
               } else if (var1.equals("OutboundPrivateKeyPassPhrase")) {
                  var5 = this._OutboundPrivateKeyPassPhrase;
                  this._OutboundPrivateKeyPassPhrase = (String)var2;
                  this._postSet(42, var5, this._OutboundPrivateKeyPassPhrase);
               } else if (var1.equals("PeerValidationEnforced")) {
                  var7 = this._PeerValidationEnforced;
                  this._PeerValidationEnforced = (Integer)var2;
                  this._postSet(14, var7, this._PeerValidationEnforced);
               } else if (var1.equals("SSLRejectionLoggingEnabled")) {
                  var4 = this._SSLRejectionLoggingEnabled;
                  this._SSLRejectionLoggingEnabled = (Boolean)var2;
                  this._postSet(30, var4, this._SSLRejectionLoggingEnabled);
               } else if (var1.equals("ServerCertificateChainFileName")) {
                  var5 = this._ServerCertificateChainFileName;
                  this._ServerCertificateChainFileName = (String)var2;
                  this._postSet(21, var5, this._ServerCertificateChainFileName);
               } else if (var1.equals("ServerCertificateFileName")) {
                  var5 = this._ServerCertificateFileName;
                  this._ServerCertificateFileName = (String)var2;
                  this._postSet(18, var5, this._ServerCertificateFileName);
               } else if (var1.equals("ServerKeyFileName")) {
                  var5 = this._ServerKeyFileName;
                  this._ServerKeyFileName = (String)var2;
                  this._postSet(25, var5, this._ServerKeyFileName);
               } else if (var1.equals("ServerPrivateKeyAlias")) {
                  var5 = this._ServerPrivateKeyAlias;
                  this._ServerPrivateKeyAlias = (String)var2;
                  this._postSet(27, var5, this._ServerPrivateKeyAlias);
               } else if (var1.equals("ServerPrivateKeyPassPhrase")) {
                  var5 = this._ServerPrivateKeyPassPhrase;
                  this._ServerPrivateKeyPassPhrase = (String)var2;
                  this._postSet(28, var5, this._ServerPrivateKeyPassPhrase);
               } else if (var1.equals("ServerPrivateKeyPassPhraseEncrypted")) {
                  var6 = this._ServerPrivateKeyPassPhraseEncrypted;
                  this._ServerPrivateKeyPassPhraseEncrypted = (byte[])((byte[])var2);
                  this._postSet(29, var6, this._ServerPrivateKeyPassPhraseEncrypted);
               } else if (var1.equals("TrustedCAFileName")) {
                  var5 = this._TrustedCAFileName;
                  this._TrustedCAFileName = (String)var2;
                  this._postSet(13, var5, this._TrustedCAFileName);
               } else if (var1.equals("TwoWaySSLEnabled")) {
                  var4 = this._TwoWaySSLEnabled;
                  this._TwoWaySSLEnabled = (Boolean)var2;
                  this._postSet(26, var4, this._TwoWaySSLEnabled);
               } else if (var1.equals("UseClientCertForOutbound")) {
                  var4 = this._UseClientCertForOutbound;
                  this._UseClientCertForOutbound = (Boolean)var2;
                  this._postSet(37, var4, this._UseClientCertForOutbound);
               } else if (var1.equals("UseJava")) {
                  var4 = this._UseJava;
                  this._UseJava = (Boolean)var2;
                  this._postSet(7, var4, this._UseJava);
               } else if (var1.equals("UseServerCerts")) {
                  var4 = this._UseServerCerts;
                  this._UseServerCerts = (Boolean)var2;
                  this._postSet(35, var4, this._UseServerCerts);
               } else if (var1.equals("customizer")) {
                  SSL var3 = this._customizer;
                  this._customizer = (SSL)var2;
               } else {
                  super.putValue(var1, var2);
               }
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AllowUnencryptedNullCipher")) {
         return new Boolean(this._AllowUnencryptedNullCipher);
      } else if (var1.equals("CertAuthenticator")) {
         return this._CertAuthenticator;
      } else if (var1.equals("CertificateCacheSize")) {
         return new Integer(this._CertificateCacheSize);
      } else if (var1.equals("Ciphersuites")) {
         return this._Ciphersuites;
      } else if (var1.equals("ClientCertAlias")) {
         return this._ClientCertAlias;
      } else if (var1.equals("ClientCertPrivateKeyPassPhrase")) {
         return this._ClientCertPrivateKeyPassPhrase;
      } else if (var1.equals("ClientCertPrivateKeyPassPhraseEncrypted")) {
         return this._ClientCertPrivateKeyPassPhraseEncrypted;
      } else if (var1.equals("ClientCertificateEnforced")) {
         return new Boolean(this._ClientCertificateEnforced);
      } else if (var1.equals("Enabled")) {
         return new Boolean(this._Enabled);
      } else if (var1.equals("ExportKeyLifespan")) {
         return new Integer(this._ExportKeyLifespan);
      } else if (var1.equals("HandlerEnabled")) {
         return new Boolean(this._HandlerEnabled);
      } else if (var1.equals("HostnameVerificationIgnored")) {
         return new Boolean(this._HostnameVerificationIgnored);
      } else if (var1.equals("HostnameVerifier")) {
         return this._HostnameVerifier;
      } else if (var1.equals("IdentityAndTrustLocations")) {
         return this._IdentityAndTrustLocations;
      } else if (var1.equals("InboundCertificateValidation")) {
         return this._InboundCertificateValidation;
      } else if (var1.equals("JSSEEnabled")) {
         return new Boolean(this._JSSEEnabled);
      } else if (var1.equals("KeyEncrypted")) {
         return new Boolean(this._KeyEncrypted);
      } else if (var1.equals("ListenPort")) {
         return new Integer(this._ListenPort);
      } else if (var1.equals("ListenPortEnabled")) {
         return new Boolean(this._ListenPortEnabled);
      } else if (var1.equals("LoginTimeoutMillis")) {
         return new Integer(this._LoginTimeoutMillis);
      } else if (var1.equals("Name")) {
         return this._Name;
      } else if (var1.equals("OutboundCertificateValidation")) {
         return this._OutboundCertificateValidation;
      } else if (var1.equals("OutboundPrivateKeyAlias")) {
         return this._OutboundPrivateKeyAlias;
      } else if (var1.equals("OutboundPrivateKeyPassPhrase")) {
         return this._OutboundPrivateKeyPassPhrase;
      } else if (var1.equals("PeerValidationEnforced")) {
         return new Integer(this._PeerValidationEnforced);
      } else if (var1.equals("SSLRejectionLoggingEnabled")) {
         return new Boolean(this._SSLRejectionLoggingEnabled);
      } else if (var1.equals("ServerCertificateChainFileName")) {
         return this._ServerCertificateChainFileName;
      } else if (var1.equals("ServerCertificateFileName")) {
         return this._ServerCertificateFileName;
      } else if (var1.equals("ServerKeyFileName")) {
         return this._ServerKeyFileName;
      } else if (var1.equals("ServerPrivateKeyAlias")) {
         return this._ServerPrivateKeyAlias;
      } else if (var1.equals("ServerPrivateKeyPassPhrase")) {
         return this._ServerPrivateKeyPassPhrase;
      } else if (var1.equals("ServerPrivateKeyPassPhraseEncrypted")) {
         return this._ServerPrivateKeyPassPhraseEncrypted;
      } else if (var1.equals("TrustedCAFileName")) {
         return this._TrustedCAFileName;
      } else if (var1.equals("TwoWaySSLEnabled")) {
         return new Boolean(this._TwoWaySSLEnabled);
      } else if (var1.equals("UseClientCertForOutbound")) {
         return new Boolean(this._UseClientCertForOutbound);
      } else if (var1.equals("UseJava")) {
         return new Boolean(this._UseJava);
      } else if (var1.equals("UseServerCerts")) {
         return new Boolean(this._UseServerCerts);
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
            case 9:
            case 10:
            case 14:
            case 21:
            case 23:
            case 25:
            case 33:
            case 36:
            case 37:
            case 38:
            case 39:
            case 41:
            case 42:
            case 43:
            case 44:
            default:
               break;
            case 7:
               if (var1.equals("enabled")) {
                  return 8;
               }
               break;
            case 8:
               if (var1.equals("use-java")) {
                  return 7;
               }
               break;
            case 11:
               if (var1.equals("ciphersuite")) {
                  return 9;
               }

               if (var1.equals("listen-port")) {
                  return 19;
               }
               break;
            case 12:
               if (var1.equals("jsse-enabled")) {
                  return 36;
               }
               break;
            case 13:
               if (var1.equals("key-encrypted")) {
                  return 15;
               }
               break;
            case 15:
               if (var1.equals("handler-enabled")) {
                  return 23;
               }
               break;
            case 16:
               if (var1.equals("use-server-certs")) {
                  return 35;
               }
               break;
            case 17:
               if (var1.equals("client-cert-alias")) {
                  return 38;
               }

               if (var1.equals("hostname-verifier")) {
                  return 11;
               }
               break;
            case 18:
               if (var1.equals("cert-authenticator")) {
                  return 10;
               }

               if (var1.equals("two-wayssl-enabled")) {
                  return 26;
               }
               break;
            case 19:
               if (var1.equals("export-key-lifespan")) {
                  return 16;
               }

               if (var1.equals("trustedca-file-name")) {
                  return 13;
               }

               if (var1.equals("listen-port-enabled")) {
                  return 20;
               }
               break;
            case 20:
               if (var1.equals("login-timeout-millis")) {
                  return 24;
               }

               if (var1.equals("server-key-file-name")) {
                  return 25;
               }
               break;
            case 22:
               if (var1.equals("certificate-cache-size")) {
                  return 22;
               }
               break;
            case 24:
               if (var1.equals("peer-validation-enforced")) {
                  return 14;
               }

               if (var1.equals("server-private-key-alias")) {
                  return 27;
               }
               break;
            case 26:
               if (var1.equals("outbound-private-key-alias")) {
                  return 41;
               }
               break;
            case 27:
               if (var1.equals("client-certificate-enforced")) {
                  return 17;
               }
               break;
            case 28:
               if (var1.equals("identity-and-trust-locations")) {
                  return 31;
               }

               if (var1.equals("server-certificate-file-name")) {
                  return 18;
               }

               if (var1.equals("use-client-cert-for-outbound")) {
                  return 37;
               }
               break;
            case 29:
               if (var1.equals("allow-unencrypted-null-cipher")) {
                  return 34;
               }

               if (var1.equals("hostname-verification-ignored")) {
                  return 12;
               }

               if (var1.equals("ssl-rejection-logging-enabled")) {
                  return 30;
               }
               break;
            case 30:
               if (var1.equals("inbound-certificate-validation")) {
                  return 32;
               }

               if (var1.equals("server-private-key-pass-phrase")) {
                  return 28;
               }
               break;
            case 31:
               if (var1.equals("outbound-certificate-validation")) {
                  return 33;
               }
               break;
            case 32:
               if (var1.equals("outbound-private-key-pass-phrase")) {
                  return 42;
               }
               break;
            case 34:
               if (var1.equals("server-certificate-chain-file-name")) {
                  return 21;
               }
               break;
            case 35:
               if (var1.equals("client-cert-private-key-pass-phrase")) {
                  return 39;
               }
               break;
            case 40:
               if (var1.equals("server-private-key-pass-phrase-encrypted")) {
                  return 29;
               }
               break;
            case 45:
               if (var1.equals("client-cert-private-key-pass-phrase-encrypted")) {
                  return 40;
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
               return "use-java";
            case 8:
               return "enabled";
            case 9:
               return "ciphersuite";
            case 10:
               return "cert-authenticator";
            case 11:
               return "hostname-verifier";
            case 12:
               return "hostname-verification-ignored";
            case 13:
               return "trustedca-file-name";
            case 14:
               return "peer-validation-enforced";
            case 15:
               return "key-encrypted";
            case 16:
               return "export-key-lifespan";
            case 17:
               return "client-certificate-enforced";
            case 18:
               return "server-certificate-file-name";
            case 19:
               return "listen-port";
            case 20:
               return "listen-port-enabled";
            case 21:
               return "server-certificate-chain-file-name";
            case 22:
               return "certificate-cache-size";
            case 23:
               return "handler-enabled";
            case 24:
               return "login-timeout-millis";
            case 25:
               return "server-key-file-name";
            case 26:
               return "two-wayssl-enabled";
            case 27:
               return "server-private-key-alias";
            case 28:
               return "server-private-key-pass-phrase";
            case 29:
               return "server-private-key-pass-phrase-encrypted";
            case 30:
               return "ssl-rejection-logging-enabled";
            case 31:
               return "identity-and-trust-locations";
            case 32:
               return "inbound-certificate-validation";
            case 33:
               return "outbound-certificate-validation";
            case 34:
               return "allow-unencrypted-null-cipher";
            case 35:
               return "use-server-certs";
            case 36:
               return "jsse-enabled";
            case 37:
               return "use-client-cert-for-outbound";
            case 38:
               return "client-cert-alias";
            case 39:
               return "client-cert-private-key-pass-phrase";
            case 40:
               return "client-cert-private-key-pass-phrase-encrypted";
            case 41:
               return "outbound-private-key-alias";
            case 42:
               return "outbound-private-key-pass-phrase";
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 9:
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
      private SSLMBeanImpl bean;

      protected Helper(SSLMBeanImpl var1) {
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
               return "UseJava";
            case 8:
               return "Enabled";
            case 9:
               return "Ciphersuites";
            case 10:
               return "CertAuthenticator";
            case 11:
               return "HostnameVerifier";
            case 12:
               return "HostnameVerificationIgnored";
            case 13:
               return "TrustedCAFileName";
            case 14:
               return "PeerValidationEnforced";
            case 15:
               return "KeyEncrypted";
            case 16:
               return "ExportKeyLifespan";
            case 17:
               return "ClientCertificateEnforced";
            case 18:
               return "ServerCertificateFileName";
            case 19:
               return "ListenPort";
            case 20:
               return "ListenPortEnabled";
            case 21:
               return "ServerCertificateChainFileName";
            case 22:
               return "CertificateCacheSize";
            case 23:
               return "HandlerEnabled";
            case 24:
               return "LoginTimeoutMillis";
            case 25:
               return "ServerKeyFileName";
            case 26:
               return "TwoWaySSLEnabled";
            case 27:
               return "ServerPrivateKeyAlias";
            case 28:
               return "ServerPrivateKeyPassPhrase";
            case 29:
               return "ServerPrivateKeyPassPhraseEncrypted";
            case 30:
               return "SSLRejectionLoggingEnabled";
            case 31:
               return "IdentityAndTrustLocations";
            case 32:
               return "InboundCertificateValidation";
            case 33:
               return "OutboundCertificateValidation";
            case 34:
               return "AllowUnencryptedNullCipher";
            case 35:
               return "UseServerCerts";
            case 36:
               return "JSSEEnabled";
            case 37:
               return "UseClientCertForOutbound";
            case 38:
               return "ClientCertAlias";
            case 39:
               return "ClientCertPrivateKeyPassPhrase";
            case 40:
               return "ClientCertPrivateKeyPassPhraseEncrypted";
            case 41:
               return "OutboundPrivateKeyAlias";
            case 42:
               return "OutboundPrivateKeyPassPhrase";
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("CertAuthenticator")) {
            return 10;
         } else if (var1.equals("CertificateCacheSize")) {
            return 22;
         } else if (var1.equals("Ciphersuites")) {
            return 9;
         } else if (var1.equals("ClientCertAlias")) {
            return 38;
         } else if (var1.equals("ClientCertPrivateKeyPassPhrase")) {
            return 39;
         } else if (var1.equals("ClientCertPrivateKeyPassPhraseEncrypted")) {
            return 40;
         } else if (var1.equals("ExportKeyLifespan")) {
            return 16;
         } else if (var1.equals("HostnameVerifier")) {
            return 11;
         } else if (var1.equals("IdentityAndTrustLocations")) {
            return 31;
         } else if (var1.equals("InboundCertificateValidation")) {
            return 32;
         } else if (var1.equals("ListenPort")) {
            return 19;
         } else if (var1.equals("LoginTimeoutMillis")) {
            return 24;
         } else if (var1.equals("Name")) {
            return 2;
         } else if (var1.equals("OutboundCertificateValidation")) {
            return 33;
         } else if (var1.equals("OutboundPrivateKeyAlias")) {
            return 41;
         } else if (var1.equals("OutboundPrivateKeyPassPhrase")) {
            return 42;
         } else if (var1.equals("PeerValidationEnforced")) {
            return 14;
         } else if (var1.equals("ServerCertificateChainFileName")) {
            return 21;
         } else if (var1.equals("ServerCertificateFileName")) {
            return 18;
         } else if (var1.equals("ServerKeyFileName")) {
            return 25;
         } else if (var1.equals("ServerPrivateKeyAlias")) {
            return 27;
         } else if (var1.equals("ServerPrivateKeyPassPhrase")) {
            return 28;
         } else if (var1.equals("ServerPrivateKeyPassPhraseEncrypted")) {
            return 29;
         } else if (var1.equals("TrustedCAFileName")) {
            return 13;
         } else if (var1.equals("AllowUnencryptedNullCipher")) {
            return 34;
         } else if (var1.equals("ClientCertificateEnforced")) {
            return 17;
         } else if (var1.equals("Enabled")) {
            return 8;
         } else if (var1.equals("HandlerEnabled")) {
            return 23;
         } else if (var1.equals("HostnameVerificationIgnored")) {
            return 12;
         } else if (var1.equals("JSSEEnabled")) {
            return 36;
         } else if (var1.equals("KeyEncrypted")) {
            return 15;
         } else if (var1.equals("ListenPortEnabled")) {
            return 20;
         } else if (var1.equals("SSLRejectionLoggingEnabled")) {
            return 30;
         } else if (var1.equals("TwoWaySSLEnabled")) {
            return 26;
         } else if (var1.equals("UseClientCertForOutbound")) {
            return 37;
         } else if (var1.equals("UseJava")) {
            return 7;
         } else {
            return var1.equals("UseServerCerts") ? 35 : super.getPropertyIndex(var1);
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
            if (this.bean.isCertAuthenticatorSet()) {
               var2.append("CertAuthenticator");
               var2.append(String.valueOf(this.bean.getCertAuthenticator()));
            }

            if (this.bean.isCertificateCacheSizeSet()) {
               var2.append("CertificateCacheSize");
               var2.append(String.valueOf(this.bean.getCertificateCacheSize()));
            }

            if (this.bean.isCiphersuitesSet()) {
               var2.append("Ciphersuites");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getCiphersuites())));
            }

            if (this.bean.isClientCertAliasSet()) {
               var2.append("ClientCertAlias");
               var2.append(String.valueOf(this.bean.getClientCertAlias()));
            }

            if (this.bean.isClientCertPrivateKeyPassPhraseSet()) {
               var2.append("ClientCertPrivateKeyPassPhrase");
               var2.append(String.valueOf(this.bean.getClientCertPrivateKeyPassPhrase()));
            }

            if (this.bean.isClientCertPrivateKeyPassPhraseEncryptedSet()) {
               var2.append("ClientCertPrivateKeyPassPhraseEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getClientCertPrivateKeyPassPhraseEncrypted())));
            }

            if (this.bean.isExportKeyLifespanSet()) {
               var2.append("ExportKeyLifespan");
               var2.append(String.valueOf(this.bean.getExportKeyLifespan()));
            }

            if (this.bean.isHostnameVerifierSet()) {
               var2.append("HostnameVerifier");
               var2.append(String.valueOf(this.bean.getHostnameVerifier()));
            }

            if (this.bean.isIdentityAndTrustLocationsSet()) {
               var2.append("IdentityAndTrustLocations");
               var2.append(String.valueOf(this.bean.getIdentityAndTrustLocations()));
            }

            if (this.bean.isInboundCertificateValidationSet()) {
               var2.append("InboundCertificateValidation");
               var2.append(String.valueOf(this.bean.getInboundCertificateValidation()));
            }

            if (this.bean.isListenPortSet()) {
               var2.append("ListenPort");
               var2.append(String.valueOf(this.bean.getListenPort()));
            }

            if (this.bean.isLoginTimeoutMillisSet()) {
               var2.append("LoginTimeoutMillis");
               var2.append(String.valueOf(this.bean.getLoginTimeoutMillis()));
            }

            if (this.bean.isNameSet()) {
               var2.append("Name");
               var2.append(String.valueOf(this.bean.getName()));
            }

            if (this.bean.isOutboundCertificateValidationSet()) {
               var2.append("OutboundCertificateValidation");
               var2.append(String.valueOf(this.bean.getOutboundCertificateValidation()));
            }

            if (this.bean.isOutboundPrivateKeyAliasSet()) {
               var2.append("OutboundPrivateKeyAlias");
               var2.append(String.valueOf(this.bean.getOutboundPrivateKeyAlias()));
            }

            if (this.bean.isOutboundPrivateKeyPassPhraseSet()) {
               var2.append("OutboundPrivateKeyPassPhrase");
               var2.append(String.valueOf(this.bean.getOutboundPrivateKeyPassPhrase()));
            }

            if (this.bean.isPeerValidationEnforcedSet()) {
               var2.append("PeerValidationEnforced");
               var2.append(String.valueOf(this.bean.getPeerValidationEnforced()));
            }

            if (this.bean.isServerCertificateChainFileNameSet()) {
               var2.append("ServerCertificateChainFileName");
               var2.append(String.valueOf(this.bean.getServerCertificateChainFileName()));
            }

            if (this.bean.isServerCertificateFileNameSet()) {
               var2.append("ServerCertificateFileName");
               var2.append(String.valueOf(this.bean.getServerCertificateFileName()));
            }

            if (this.bean.isServerKeyFileNameSet()) {
               var2.append("ServerKeyFileName");
               var2.append(String.valueOf(this.bean.getServerKeyFileName()));
            }

            if (this.bean.isServerPrivateKeyAliasSet()) {
               var2.append("ServerPrivateKeyAlias");
               var2.append(String.valueOf(this.bean.getServerPrivateKeyAlias()));
            }

            if (this.bean.isServerPrivateKeyPassPhraseSet()) {
               var2.append("ServerPrivateKeyPassPhrase");
               var2.append(String.valueOf(this.bean.getServerPrivateKeyPassPhrase()));
            }

            if (this.bean.isServerPrivateKeyPassPhraseEncryptedSet()) {
               var2.append("ServerPrivateKeyPassPhraseEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getServerPrivateKeyPassPhraseEncrypted())));
            }

            if (this.bean.isTrustedCAFileNameSet()) {
               var2.append("TrustedCAFileName");
               var2.append(String.valueOf(this.bean.getTrustedCAFileName()));
            }

            if (this.bean.isAllowUnencryptedNullCipherSet()) {
               var2.append("AllowUnencryptedNullCipher");
               var2.append(String.valueOf(this.bean.isAllowUnencryptedNullCipher()));
            }

            if (this.bean.isClientCertificateEnforcedSet()) {
               var2.append("ClientCertificateEnforced");
               var2.append(String.valueOf(this.bean.isClientCertificateEnforced()));
            }

            if (this.bean.isEnabledSet()) {
               var2.append("Enabled");
               var2.append(String.valueOf(this.bean.isEnabled()));
            }

            if (this.bean.isHandlerEnabledSet()) {
               var2.append("HandlerEnabled");
               var2.append(String.valueOf(this.bean.isHandlerEnabled()));
            }

            if (this.bean.isHostnameVerificationIgnoredSet()) {
               var2.append("HostnameVerificationIgnored");
               var2.append(String.valueOf(this.bean.isHostnameVerificationIgnored()));
            }

            if (this.bean.isJSSEEnabledSet()) {
               var2.append("JSSEEnabled");
               var2.append(String.valueOf(this.bean.isJSSEEnabled()));
            }

            if (this.bean.isKeyEncryptedSet()) {
               var2.append("KeyEncrypted");
               var2.append(String.valueOf(this.bean.isKeyEncrypted()));
            }

            if (this.bean.isListenPortEnabledSet()) {
               var2.append("ListenPortEnabled");
               var2.append(String.valueOf(this.bean.isListenPortEnabled()));
            }

            if (this.bean.isSSLRejectionLoggingEnabledSet()) {
               var2.append("SSLRejectionLoggingEnabled");
               var2.append(String.valueOf(this.bean.isSSLRejectionLoggingEnabled()));
            }

            if (this.bean.isTwoWaySSLEnabledSet()) {
               var2.append("TwoWaySSLEnabled");
               var2.append(String.valueOf(this.bean.isTwoWaySSLEnabled()));
            }

            if (this.bean.isUseClientCertForOutboundSet()) {
               var2.append("UseClientCertForOutbound");
               var2.append(String.valueOf(this.bean.isUseClientCertForOutbound()));
            }

            if (this.bean.isUseJavaSet()) {
               var2.append("UseJava");
               var2.append(String.valueOf(this.bean.isUseJava()));
            }

            if (this.bean.isUseServerCertsSet()) {
               var2.append("UseServerCerts");
               var2.append(String.valueOf(this.bean.isUseServerCerts()));
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
            SSLMBeanImpl var2 = (SSLMBeanImpl)var1;
            this.computeDiff("CertAuthenticator", this.bean.getCertAuthenticator(), var2.getCertAuthenticator(), false);
            this.computeDiff("CertificateCacheSize", this.bean.getCertificateCacheSize(), var2.getCertificateCacheSize(), false);
            this.computeDiff("Ciphersuites", this.bean.getCiphersuites(), var2.getCiphersuites(), true);
            this.computeDiff("ClientCertAlias", this.bean.getClientCertAlias(), var2.getClientCertAlias(), true);
            this.computeDiff("ClientCertPrivateKeyPassPhraseEncrypted", this.bean.getClientCertPrivateKeyPassPhraseEncrypted(), var2.getClientCertPrivateKeyPassPhraseEncrypted(), true);
            this.computeDiff("ExportKeyLifespan", this.bean.getExportKeyLifespan(), var2.getExportKeyLifespan(), true);
            this.computeDiff("HostnameVerifier", this.bean.getHostnameVerifier(), var2.getHostnameVerifier(), false);
            this.computeDiff("IdentityAndTrustLocations", this.bean.getIdentityAndTrustLocations(), var2.getIdentityAndTrustLocations(), true);
            this.computeDiff("InboundCertificateValidation", this.bean.getInboundCertificateValidation(), var2.getInboundCertificateValidation(), true);
            this.computeDiff("ListenPort", this.bean.getListenPort(), var2.getListenPort(), true);
            this.computeDiff("LoginTimeoutMillis", this.bean.getLoginTimeoutMillis(), var2.getLoginTimeoutMillis(), true);
            this.computeDiff("Name", this.bean.getName(), var2.getName(), false);
            this.computeDiff("OutboundCertificateValidation", this.bean.getOutboundCertificateValidation(), var2.getOutboundCertificateValidation(), true);
            this.computeDiff("OutboundPrivateKeyAlias", this.bean.getOutboundPrivateKeyAlias(), var2.getOutboundPrivateKeyAlias(), true);
            this.computeDiff("OutboundPrivateKeyPassPhrase", this.bean.getOutboundPrivateKeyPassPhrase(), var2.getOutboundPrivateKeyPassPhrase(), true);
            this.computeDiff("ServerCertificateChainFileName", this.bean.getServerCertificateChainFileName(), var2.getServerCertificateChainFileName(), true);
            this.computeDiff("ServerCertificateFileName", this.bean.getServerCertificateFileName(), var2.getServerCertificateFileName(), true);
            this.computeDiff("ServerKeyFileName", this.bean.getServerKeyFileName(), var2.getServerKeyFileName(), true);
            this.computeDiff("ServerPrivateKeyAlias", this.bean.getServerPrivateKeyAlias(), var2.getServerPrivateKeyAlias(), true);
            this.computeDiff("ServerPrivateKeyPassPhraseEncrypted", this.bean.getServerPrivateKeyPassPhraseEncrypted(), var2.getServerPrivateKeyPassPhraseEncrypted(), true);
            this.computeDiff("TrustedCAFileName", this.bean.getTrustedCAFileName(), var2.getTrustedCAFileName(), true);
            this.computeDiff("AllowUnencryptedNullCipher", this.bean.isAllowUnencryptedNullCipher(), var2.isAllowUnencryptedNullCipher(), false);
            this.computeDiff("ClientCertificateEnforced", this.bean.isClientCertificateEnforced(), var2.isClientCertificateEnforced(), true);
            this.computeDiff("Enabled", this.bean.isEnabled(), var2.isEnabled(), true);
            this.computeDiff("HandlerEnabled", this.bean.isHandlerEnabled(), var2.isHandlerEnabled(), false);
            this.computeDiff("HostnameVerificationIgnored", this.bean.isHostnameVerificationIgnored(), var2.isHostnameVerificationIgnored(), false);
            this.computeDiff("JSSEEnabled", this.bean.isJSSEEnabled(), var2.isJSSEEnabled(), false);
            this.computeDiff("KeyEncrypted", this.bean.isKeyEncrypted(), var2.isKeyEncrypted(), false);
            this.computeDiff("SSLRejectionLoggingEnabled", this.bean.isSSLRejectionLoggingEnabled(), var2.isSSLRejectionLoggingEnabled(), true);
            this.computeDiff("TwoWaySSLEnabled", this.bean.isTwoWaySSLEnabled(), var2.isTwoWaySSLEnabled(), true);
            this.computeDiff("UseClientCertForOutbound", this.bean.isUseClientCertForOutbound(), var2.isUseClientCertForOutbound(), true);
            this.computeDiff("UseJava", this.bean.isUseJava(), var2.isUseJava(), false);
            this.computeDiff("UseServerCerts", this.bean.isUseServerCerts(), var2.isUseServerCerts(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            SSLMBeanImpl var3 = (SSLMBeanImpl)var1.getSourceBean();
            SSLMBeanImpl var4 = (SSLMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("CertAuthenticator")) {
                  var3.setCertAuthenticator(var4.getCertAuthenticator());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("CertificateCacheSize")) {
                  var3.setCertificateCacheSize(var4.getCertificateCacheSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 22);
               } else if (var5.equals("Ciphersuites")) {
                  var3.setCiphersuites(var4.getCiphersuites());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("ClientCertAlias")) {
                  var3.setClientCertAlias(var4.getClientCertAlias());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 38);
               } else if (!var5.equals("ClientCertPrivateKeyPassPhrase")) {
                  if (var5.equals("ClientCertPrivateKeyPassPhraseEncrypted")) {
                     var3.setClientCertPrivateKeyPassPhraseEncrypted(var4.getClientCertPrivateKeyPassPhraseEncrypted());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 40);
                  } else if (var5.equals("ExportKeyLifespan")) {
                     var3.setExportKeyLifespan(var4.getExportKeyLifespan());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 16);
                  } else if (var5.equals("HostnameVerifier")) {
                     var3.setHostnameVerifier(var4.getHostnameVerifier());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 11);
                  } else if (var5.equals("IdentityAndTrustLocations")) {
                     var3.setIdentityAndTrustLocations(var4.getIdentityAndTrustLocations());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 31);
                  } else if (var5.equals("InboundCertificateValidation")) {
                     var3.setInboundCertificateValidation(var4.getInboundCertificateValidation());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 32);
                  } else if (var5.equals("ListenPort")) {
                     var3.setListenPort(var4.getListenPort());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 19);
                  } else if (var5.equals("LoginTimeoutMillis")) {
                     var3.setLoginTimeoutMillis(var4.getLoginTimeoutMillis());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 24);
                  } else if (var5.equals("Name")) {
                     var3.setName(var4.getName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 2);
                  } else if (var5.equals("OutboundCertificateValidation")) {
                     var3.setOutboundCertificateValidation(var4.getOutboundCertificateValidation());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 33);
                  } else if (var5.equals("OutboundPrivateKeyAlias")) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 41);
                  } else if (var5.equals("OutboundPrivateKeyPassPhrase")) {
                     var3._conditionalUnset(var2.isUnsetUpdate(), 42);
                  } else if (!var5.equals("PeerValidationEnforced")) {
                     if (var5.equals("ServerCertificateChainFileName")) {
                        var3.setServerCertificateChainFileName(var4.getServerCertificateChainFileName());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 21);
                     } else if (var5.equals("ServerCertificateFileName")) {
                        var3.setServerCertificateFileName(var4.getServerCertificateFileName());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 18);
                     } else if (var5.equals("ServerKeyFileName")) {
                        var3.setServerKeyFileName(var4.getServerKeyFileName());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 25);
                     } else if (var5.equals("ServerPrivateKeyAlias")) {
                        var3.setServerPrivateKeyAlias(var4.getServerPrivateKeyAlias());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 27);
                     } else if (!var5.equals("ServerPrivateKeyPassPhrase")) {
                        if (var5.equals("ServerPrivateKeyPassPhraseEncrypted")) {
                           var3.setServerPrivateKeyPassPhraseEncrypted(var4.getServerPrivateKeyPassPhraseEncrypted());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 29);
                        } else if (var5.equals("TrustedCAFileName")) {
                           var3.setTrustedCAFileName(var4.getTrustedCAFileName());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 13);
                        } else if (var5.equals("AllowUnencryptedNullCipher")) {
                           var3.setAllowUnencryptedNullCipher(var4.isAllowUnencryptedNullCipher());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 34);
                        } else if (var5.equals("ClientCertificateEnforced")) {
                           var3.setClientCertificateEnforced(var4.isClientCertificateEnforced());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 17);
                        } else if (var5.equals("Enabled")) {
                           var3.setEnabled(var4.isEnabled());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 8);
                        } else if (var5.equals("HandlerEnabled")) {
                           var3.setHandlerEnabled(var4.isHandlerEnabled());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 23);
                        } else if (var5.equals("HostnameVerificationIgnored")) {
                           var3.setHostnameVerificationIgnored(var4.isHostnameVerificationIgnored());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                        } else if (var5.equals("JSSEEnabled")) {
                           var3.setJSSEEnabled(var4.isJSSEEnabled());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 36);
                        } else if (var5.equals("KeyEncrypted")) {
                           var3.setKeyEncrypted(var4.isKeyEncrypted());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 15);
                        } else if (!var5.equals("ListenPortEnabled")) {
                           if (var5.equals("SSLRejectionLoggingEnabled")) {
                              var3.setSSLRejectionLoggingEnabled(var4.isSSLRejectionLoggingEnabled());
                              var3._conditionalUnset(var2.isUnsetUpdate(), 30);
                           } else if (var5.equals("TwoWaySSLEnabled")) {
                              var3.setTwoWaySSLEnabled(var4.isTwoWaySSLEnabled());
                              var3._conditionalUnset(var2.isUnsetUpdate(), 26);
                           } else if (var5.equals("UseClientCertForOutbound")) {
                              var3.setUseClientCertForOutbound(var4.isUseClientCertForOutbound());
                              var3._conditionalUnset(var2.isUnsetUpdate(), 37);
                           } else if (var5.equals("UseJava")) {
                              var3.setUseJava(var4.isUseJava());
                              var3._conditionalUnset(var2.isUnsetUpdate(), 7);
                           } else if (var5.equals("UseServerCerts")) {
                              var3.setUseServerCerts(var4.isUseServerCerts());
                              var3._conditionalUnset(var2.isUnsetUpdate(), 35);
                           } else {
                              super.applyPropertyUpdate(var1, var2);
                           }
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
            SSLMBeanImpl var5 = (SSLMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("CertAuthenticator")) && this.bean.isCertAuthenticatorSet()) {
               var5.setCertAuthenticator(this.bean.getCertAuthenticator());
            }

            if ((var3 == null || !var3.contains("CertificateCacheSize")) && this.bean.isCertificateCacheSizeSet()) {
               var5.setCertificateCacheSize(this.bean.getCertificateCacheSize());
            }

            if ((var3 == null || !var3.contains("Ciphersuites")) && this.bean.isCiphersuitesSet()) {
               String[] var4 = this.bean.getCiphersuites();
               var5.setCiphersuites(var4 == null ? null : (String[])((String[])((String[])((String[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("ClientCertAlias")) && this.bean.isClientCertAliasSet()) {
               var5.setClientCertAlias(this.bean.getClientCertAlias());
            }

            byte[] var8;
            if ((var3 == null || !var3.contains("ClientCertPrivateKeyPassPhraseEncrypted")) && this.bean.isClientCertPrivateKeyPassPhraseEncryptedSet()) {
               var8 = this.bean.getClientCertPrivateKeyPassPhraseEncrypted();
               var5.setClientCertPrivateKeyPassPhraseEncrypted(var8 == null ? null : (byte[])((byte[])((byte[])((byte[])var8)).clone()));
            }

            if ((var3 == null || !var3.contains("ExportKeyLifespan")) && this.bean.isExportKeyLifespanSet()) {
               var5.setExportKeyLifespan(this.bean.getExportKeyLifespan());
            }

            if ((var3 == null || !var3.contains("HostnameVerifier")) && this.bean.isHostnameVerifierSet()) {
               var5.setHostnameVerifier(this.bean.getHostnameVerifier());
            }

            if ((var3 == null || !var3.contains("IdentityAndTrustLocations")) && this.bean.isIdentityAndTrustLocationsSet()) {
               var5.setIdentityAndTrustLocations(this.bean.getIdentityAndTrustLocations());
            }

            if ((var3 == null || !var3.contains("InboundCertificateValidation")) && this.bean.isInboundCertificateValidationSet()) {
               var5.setInboundCertificateValidation(this.bean.getInboundCertificateValidation());
            }

            if ((var3 == null || !var3.contains("ListenPort")) && this.bean.isListenPortSet()) {
               var5.setListenPort(this.bean.getListenPort());
            }

            if ((var3 == null || !var3.contains("LoginTimeoutMillis")) && this.bean.isLoginTimeoutMillisSet()) {
               var5.setLoginTimeoutMillis(this.bean.getLoginTimeoutMillis());
            }

            if ((var3 == null || !var3.contains("Name")) && this.bean.isNameSet()) {
               var5.setName(this.bean.getName());
            }

            if ((var3 == null || !var3.contains("OutboundCertificateValidation")) && this.bean.isOutboundCertificateValidationSet()) {
               var5.setOutboundCertificateValidation(this.bean.getOutboundCertificateValidation());
            }

            if ((var3 == null || !var3.contains("OutboundPrivateKeyAlias")) && this.bean.isOutboundPrivateKeyAliasSet()) {
            }

            if ((var3 == null || !var3.contains("OutboundPrivateKeyPassPhrase")) && this.bean.isOutboundPrivateKeyPassPhraseSet()) {
            }

            if ((var3 == null || !var3.contains("ServerCertificateChainFileName")) && this.bean.isServerCertificateChainFileNameSet()) {
               var5.setServerCertificateChainFileName(this.bean.getServerCertificateChainFileName());
            }

            if ((var3 == null || !var3.contains("ServerCertificateFileName")) && this.bean.isServerCertificateFileNameSet()) {
               var5.setServerCertificateFileName(this.bean.getServerCertificateFileName());
            }

            if ((var3 == null || !var3.contains("ServerKeyFileName")) && this.bean.isServerKeyFileNameSet()) {
               var5.setServerKeyFileName(this.bean.getServerKeyFileName());
            }

            if ((var3 == null || !var3.contains("ServerPrivateKeyAlias")) && this.bean.isServerPrivateKeyAliasSet()) {
               var5.setServerPrivateKeyAlias(this.bean.getServerPrivateKeyAlias());
            }

            if ((var3 == null || !var3.contains("ServerPrivateKeyPassPhraseEncrypted")) && this.bean.isServerPrivateKeyPassPhraseEncryptedSet()) {
               var8 = this.bean.getServerPrivateKeyPassPhraseEncrypted();
               var5.setServerPrivateKeyPassPhraseEncrypted(var8 == null ? null : (byte[])((byte[])((byte[])((byte[])var8)).clone()));
            }

            if ((var3 == null || !var3.contains("TrustedCAFileName")) && this.bean.isTrustedCAFileNameSet()) {
               var5.setTrustedCAFileName(this.bean.getTrustedCAFileName());
            }

            if ((var3 == null || !var3.contains("AllowUnencryptedNullCipher")) && this.bean.isAllowUnencryptedNullCipherSet()) {
               var5.setAllowUnencryptedNullCipher(this.bean.isAllowUnencryptedNullCipher());
            }

            if ((var3 == null || !var3.contains("ClientCertificateEnforced")) && this.bean.isClientCertificateEnforcedSet()) {
               var5.setClientCertificateEnforced(this.bean.isClientCertificateEnforced());
            }

            if ((var3 == null || !var3.contains("Enabled")) && this.bean.isEnabledSet()) {
               var5.setEnabled(this.bean.isEnabled());
            }

            if ((var3 == null || !var3.contains("HandlerEnabled")) && this.bean.isHandlerEnabledSet()) {
               var5.setHandlerEnabled(this.bean.isHandlerEnabled());
            }

            if ((var3 == null || !var3.contains("HostnameVerificationIgnored")) && this.bean.isHostnameVerificationIgnoredSet()) {
               var5.setHostnameVerificationIgnored(this.bean.isHostnameVerificationIgnored());
            }

            if ((var3 == null || !var3.contains("JSSEEnabled")) && this.bean.isJSSEEnabledSet()) {
               var5.setJSSEEnabled(this.bean.isJSSEEnabled());
            }

            if ((var3 == null || !var3.contains("KeyEncrypted")) && this.bean.isKeyEncryptedSet()) {
               var5.setKeyEncrypted(this.bean.isKeyEncrypted());
            }

            if ((var3 == null || !var3.contains("SSLRejectionLoggingEnabled")) && this.bean.isSSLRejectionLoggingEnabledSet()) {
               var5.setSSLRejectionLoggingEnabled(this.bean.isSSLRejectionLoggingEnabled());
            }

            if ((var3 == null || !var3.contains("TwoWaySSLEnabled")) && this.bean.isTwoWaySSLEnabledSet()) {
               var5.setTwoWaySSLEnabled(this.bean.isTwoWaySSLEnabled());
            }

            if ((var3 == null || !var3.contains("UseClientCertForOutbound")) && this.bean.isUseClientCertForOutboundSet()) {
               var5.setUseClientCertForOutbound(this.bean.isUseClientCertForOutbound());
            }

            if ((var3 == null || !var3.contains("UseJava")) && this.bean.isUseJavaSet()) {
               var5.setUseJava(this.bean.isUseJava());
            }

            if ((var3 == null || !var3.contains("UseServerCerts")) && this.bean.isUseServerCertsSet()) {
               var5.setUseServerCerts(this.bean.isUseServerCerts());
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
