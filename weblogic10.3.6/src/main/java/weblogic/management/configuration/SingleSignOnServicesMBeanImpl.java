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

public class SingleSignOnServicesMBeanImpl extends ConfigurationMBeanImpl implements SingleSignOnServicesMBean, Serializable {
   private int _ArtifactMaxCacheSize;
   private int _ArtifactTimeout;
   private int _AuthnRequestMaxCacheSize;
   private int _AuthnRequestTimeout;
   private String _BasicAuthPassword;
   private byte[] _BasicAuthPasswordEncrypted;
   private String _BasicAuthUsername;
   private String _ContactPersonCompany;
   private String _ContactPersonEmailAddress;
   private String _ContactPersonGivenName;
   private String _ContactPersonSurName;
   private String _ContactPersonTelephoneNumber;
   private String _ContactPersonType;
   private String _DefaultURL;
   private String _EntityID;
   private String _ErrorPath;
   private boolean _ForceAuthn;
   private boolean _IdentityProviderArtifactBindingEnabled;
   private boolean _IdentityProviderEnabled;
   private boolean _IdentityProviderPOSTBindingEnabled;
   private String _IdentityProviderPreferredBinding;
   private boolean _IdentityProviderRedirectBindingEnabled;
   private String _LoginReturnQueryParameter;
   private String _LoginURL;
   private String _OrganizationName;
   private String _OrganizationURL;
   private boolean _POSTOneUseCheckEnabled;
   private boolean _Passive;
   private String _PublishedSiteURL;
   private boolean _RecipientCheckEnabled;
   private boolean _ReplicatedCacheEnabled;
   private String _SSOSigningKeyAlias;
   private String _SSOSigningKeyPassPhrase;
   private byte[] _SSOSigningKeyPassPhraseEncrypted;
   private boolean _ServiceProviderArtifactBindingEnabled;
   private boolean _ServiceProviderEnabled;
   private boolean _ServiceProviderPOSTBindingEnabled;
   private String _ServiceProviderPreferredBinding;
   private boolean _SignAuthnRequests;
   private String _TransportLayerSecurityKeyAlias;
   private String _TransportLayerSecurityKeyPassPhrase;
   private byte[] _TransportLayerSecurityKeyPassPhraseEncrypted;
   private boolean _WantArtifactRequestsSigned;
   private boolean _WantAssertionsSigned;
   private boolean _WantAuthnRequestsSigned;
   private boolean _WantBasicAuthClientAuthentication;
   private boolean _WantTransportLayerSecurityClientAuthentication;
   private static SchemaHelper2 _schemaHelper;

   public SingleSignOnServicesMBeanImpl() {
      this._initializeProperty(-1);
   }

   public SingleSignOnServicesMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getContactPersonGivenName() {
      return this._ContactPersonGivenName;
   }

   public boolean isContactPersonGivenNameSet() {
      return this._isSet(7);
   }

   public void setContactPersonGivenName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ContactPersonGivenName;
      this._ContactPersonGivenName = var1;
      this._postSet(7, var2, var1);
   }

   public String getContactPersonSurName() {
      return this._ContactPersonSurName;
   }

   public boolean isContactPersonSurNameSet() {
      return this._isSet(8);
   }

   public void setContactPersonSurName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ContactPersonSurName;
      this._ContactPersonSurName = var1;
      this._postSet(8, var2, var1);
   }

   public String getContactPersonType() {
      return this._ContactPersonType;
   }

   public boolean isContactPersonTypeSet() {
      return this._isSet(9);
   }

   public void setContactPersonType(String var1) {
      var1 = var1 == null ? null : var1.trim();
      if (var1 != null && var1.trim().length() > 0) {
         weblogic.descriptor.beangen.LegalChecks.checkInEnum("ContactPersonType", var1, new String[]{"technical", "support", "administrative", "billing", "other"});
      }

      String var2 = this._ContactPersonType;
      this._ContactPersonType = var1;
      this._postSet(9, var2, var1);
   }

   public String getContactPersonCompany() {
      return this._ContactPersonCompany;
   }

   public boolean isContactPersonCompanySet() {
      return this._isSet(10);
   }

   public void setContactPersonCompany(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ContactPersonCompany;
      this._ContactPersonCompany = var1;
      this._postSet(10, var2, var1);
   }

   public String getContactPersonTelephoneNumber() {
      return this._ContactPersonTelephoneNumber;
   }

   public boolean isContactPersonTelephoneNumberSet() {
      return this._isSet(11);
   }

   public void setContactPersonTelephoneNumber(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ContactPersonTelephoneNumber;
      this._ContactPersonTelephoneNumber = var1;
      this._postSet(11, var2, var1);
   }

   public String getContactPersonEmailAddress() {
      return this._ContactPersonEmailAddress;
   }

   public boolean isContactPersonEmailAddressSet() {
      return this._isSet(12);
   }

   public void setContactPersonEmailAddress(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ContactPersonEmailAddress;
      this._ContactPersonEmailAddress = var1;
      this._postSet(12, var2, var1);
   }

   public String getOrganizationName() {
      return this._OrganizationName;
   }

   public boolean isOrganizationNameSet() {
      return this._isSet(13);
   }

   public void setOrganizationName(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._OrganizationName;
      this._OrganizationName = var1;
      this._postSet(13, var2, var1);
   }

   public String getOrganizationURL() {
      return this._OrganizationURL;
   }

   public boolean isOrganizationURLSet() {
      return this._isSet(14);
   }

   public void setOrganizationURL(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._OrganizationURL;
      this._OrganizationURL = var1;
      this._postSet(14, var2, var1);
   }

   public String getPublishedSiteURL() {
      return this._PublishedSiteURL;
   }

   public boolean isPublishedSiteURLSet() {
      return this._isSet(15);
   }

   public void setPublishedSiteURL(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._PublishedSiteURL;
      this._PublishedSiteURL = var1;
      this._postSet(15, var2, var1);
   }

   public String getEntityID() {
      return this._EntityID;
   }

   public boolean isEntityIDSet() {
      return this._isSet(16);
   }

   public void setEntityID(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._EntityID;
      this._EntityID = var1;
      this._postSet(16, var2, var1);
   }

   public String getErrorPath() {
      return this._ErrorPath;
   }

   public boolean isErrorPathSet() {
      return this._isSet(17);
   }

   public void setErrorPath(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ErrorPath;
      this._ErrorPath = var1;
      this._postSet(17, var2, var1);
   }

   public boolean isServiceProviderEnabled() {
      return this._ServiceProviderEnabled;
   }

   public boolean isServiceProviderEnabledSet() {
      return this._isSet(18);
   }

   public void setServiceProviderEnabled(boolean var1) {
      boolean var2 = this._ServiceProviderEnabled;
      this._ServiceProviderEnabled = var1;
      this._postSet(18, var2, var1);
   }

   public String getDefaultURL() {
      return this._DefaultURL;
   }

   public boolean isDefaultURLSet() {
      return this._isSet(19);
   }

   public void setDefaultURL(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._DefaultURL;
      this._DefaultURL = var1;
      this._postSet(19, var2, var1);
   }

   public boolean isServiceProviderArtifactBindingEnabled() {
      return this._ServiceProviderArtifactBindingEnabled;
   }

   public boolean isServiceProviderArtifactBindingEnabledSet() {
      return this._isSet(20);
   }

   public void setServiceProviderArtifactBindingEnabled(boolean var1) {
      boolean var2 = this._ServiceProviderArtifactBindingEnabled;
      this._ServiceProviderArtifactBindingEnabled = var1;
      this._postSet(20, var2, var1);
   }

   public boolean isServiceProviderPOSTBindingEnabled() {
      return this._ServiceProviderPOSTBindingEnabled;
   }

   public boolean isServiceProviderPOSTBindingEnabledSet() {
      return this._isSet(21);
   }

   public void setServiceProviderPOSTBindingEnabled(boolean var1) {
      boolean var2 = this._ServiceProviderPOSTBindingEnabled;
      this._ServiceProviderPOSTBindingEnabled = var1;
      this._postSet(21, var2, var1);
   }

   public String getServiceProviderPreferredBinding() {
      return this._ServiceProviderPreferredBinding;
   }

   public boolean isServiceProviderPreferredBindingSet() {
      return this._isSet(22);
   }

   public void setServiceProviderPreferredBinding(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"None", "HTTP/POST", "HTTP/Artifact"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("ServiceProviderPreferredBinding", var1, var2);
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("ServiceProviderPreferredBinding", var1);
      String var3 = this._ServiceProviderPreferredBinding;
      this._ServiceProviderPreferredBinding = var1;
      this._postSet(22, var3, var1);
   }

   public boolean isSignAuthnRequests() {
      return this._SignAuthnRequests;
   }

   public boolean isSignAuthnRequestsSet() {
      return this._isSet(23);
   }

   public void setSignAuthnRequests(boolean var1) {
      boolean var2 = this._SignAuthnRequests;
      this._SignAuthnRequests = var1;
      this._postSet(23, var2, var1);
   }

   public boolean isWantAssertionsSigned() {
      return this._WantAssertionsSigned;
   }

   public boolean isWantAssertionsSignedSet() {
      return this._isSet(24);
   }

   public void setWantAssertionsSigned(boolean var1) {
      boolean var2 = this._WantAssertionsSigned;
      this._WantAssertionsSigned = var1;
      this._postSet(24, var2, var1);
   }

   public String getSSOSigningKeyAlias() {
      return this._SSOSigningKeyAlias;
   }

   public boolean isSSOSigningKeyAliasSet() {
      return this._isSet(25);
   }

   public void setSSOSigningKeyAlias(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._SSOSigningKeyAlias;
      this._SSOSigningKeyAlias = var1;
      this._postSet(25, var2, var1);
   }

   public String getSSOSigningKeyPassPhrase() {
      byte[] var1 = this.getSSOSigningKeyPassPhraseEncrypted();
      return var1 == null ? null : this._decrypt("SSOSigningKeyPassPhrase", var1);
   }

   public boolean isSSOSigningKeyPassPhraseSet() {
      return this.isSSOSigningKeyPassPhraseEncryptedSet();
   }

   public void setSSOSigningKeyPassPhrase(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this.setSSOSigningKeyPassPhraseEncrypted(var1 == null ? null : this._encrypt("SSOSigningKeyPassPhrase", var1));
   }

   public byte[] getSSOSigningKeyPassPhraseEncrypted() {
      return this._getHelper()._cloneArray(this._SSOSigningKeyPassPhraseEncrypted);
   }

   public String getSSOSigningKeyPassPhraseEncryptedAsString() {
      byte[] var1 = this.getSSOSigningKeyPassPhraseEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isSSOSigningKeyPassPhraseEncryptedSet() {
      return this._isSet(27);
   }

   public void setSSOSigningKeyPassPhraseEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setSSOSigningKeyPassPhraseEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public boolean isForceAuthn() {
      return this._ForceAuthn;
   }

   public boolean isForceAuthnSet() {
      return this._isSet(28);
   }

   public void setForceAuthn(boolean var1) {
      boolean var2 = this._ForceAuthn;
      this._ForceAuthn = var1;
      this._postSet(28, var2, var1);
   }

   public boolean isPassive() {
      return this._Passive;
   }

   public boolean isPassiveSet() {
      return this._isSet(29);
   }

   public void setPassive(boolean var1) {
      boolean var2 = this._Passive;
      this._Passive = var1;
      this._postSet(29, var2, var1);
   }

   public boolean isIdentityProviderEnabled() {
      return this._IdentityProviderEnabled;
   }

   public boolean isIdentityProviderEnabledSet() {
      return this._isSet(30);
   }

   public void setIdentityProviderEnabled(boolean var1) {
      boolean var2 = this._IdentityProviderEnabled;
      this._IdentityProviderEnabled = var1;
      this._postSet(30, var2, var1);
   }

   public boolean isIdentityProviderArtifactBindingEnabled() {
      return this._IdentityProviderArtifactBindingEnabled;
   }

   public boolean isIdentityProviderArtifactBindingEnabledSet() {
      return this._isSet(31);
   }

   public void setIdentityProviderArtifactBindingEnabled(boolean var1) {
      boolean var2 = this._IdentityProviderArtifactBindingEnabled;
      this._IdentityProviderArtifactBindingEnabled = var1;
      this._postSet(31, var2, var1);
   }

   public boolean isIdentityProviderPOSTBindingEnabled() {
      return this._IdentityProviderPOSTBindingEnabled;
   }

   public boolean isIdentityProviderPOSTBindingEnabledSet() {
      return this._isSet(32);
   }

   public void setIdentityProviderPOSTBindingEnabled(boolean var1) {
      boolean var2 = this._IdentityProviderPOSTBindingEnabled;
      this._IdentityProviderPOSTBindingEnabled = var1;
      this._postSet(32, var2, var1);
   }

   public boolean isIdentityProviderRedirectBindingEnabled() {
      return this._IdentityProviderRedirectBindingEnabled;
   }

   public boolean isIdentityProviderRedirectBindingEnabledSet() {
      return this._isSet(33);
   }

   public void setIdentityProviderRedirectBindingEnabled(boolean var1) {
      boolean var2 = this._IdentityProviderRedirectBindingEnabled;
      this._IdentityProviderRedirectBindingEnabled = var1;
      this._postSet(33, var2, var1);
   }

   public String getIdentityProviderPreferredBinding() {
      return this._IdentityProviderPreferredBinding;
   }

   public boolean isIdentityProviderPreferredBindingSet() {
      return this._isSet(34);
   }

   public void setIdentityProviderPreferredBinding(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"None", "HTTP/POST", "HTTP/Artifact", "HTTP/Redirect"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("IdentityProviderPreferredBinding", var1, var2);
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("IdentityProviderPreferredBinding", var1);
      String var3 = this._IdentityProviderPreferredBinding;
      this._IdentityProviderPreferredBinding = var1;
      this._postSet(34, var3, var1);
   }

   public String getLoginURL() {
      return this._LoginURL;
   }

   public boolean isLoginURLSet() {
      return this._isSet(35);
   }

   public void setLoginURL(String var1) {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("LoginURL", var1);
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("LoginURL", var1);
      String var2 = this._LoginURL;
      this._LoginURL = var1;
      this._postSet(35, var2, var1);
   }

   public String getLoginReturnQueryParameter() {
      return this._LoginReturnQueryParameter;
   }

   public boolean isLoginReturnQueryParameterSet() {
      return this._isSet(36);
   }

   public void setLoginReturnQueryParameter(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._LoginReturnQueryParameter;
      this._LoginReturnQueryParameter = var1;
      this._postSet(36, var2, var1);
   }

   public boolean isWantAuthnRequestsSigned() {
      return this._WantAuthnRequestsSigned;
   }

   public boolean isWantAuthnRequestsSignedSet() {
      return this._isSet(37);
   }

   public void setWantAuthnRequestsSigned(boolean var1) {
      boolean var2 = this._WantAuthnRequestsSigned;
      this._WantAuthnRequestsSigned = var1;
      this._postSet(37, var2, var1);
   }

   public boolean isRecipientCheckEnabled() {
      return this._RecipientCheckEnabled;
   }

   public boolean isRecipientCheckEnabledSet() {
      return this._isSet(38);
   }

   public void setRecipientCheckEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._RecipientCheckEnabled;
      this._RecipientCheckEnabled = var1;
      this._postSet(38, var2, var1);
   }

   public boolean isPOSTOneUseCheckEnabled() {
      return this._POSTOneUseCheckEnabled;
   }

   public boolean isPOSTOneUseCheckEnabledSet() {
      return this._isSet(39);
   }

   public void setPOSTOneUseCheckEnabled(boolean var1) throws InvalidAttributeValueException {
      boolean var2 = this._POSTOneUseCheckEnabled;
      this._POSTOneUseCheckEnabled = var1;
      this._postSet(39, var2, var1);
   }

   public String getTransportLayerSecurityKeyAlias() {
      return this._TransportLayerSecurityKeyAlias;
   }

   public boolean isTransportLayerSecurityKeyAliasSet() {
      return this._isSet(40);
   }

   public void setTransportLayerSecurityKeyAlias(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._TransportLayerSecurityKeyAlias;
      this._TransportLayerSecurityKeyAlias = var1;
      this._postSet(40, var2, var1);
   }

   public String getTransportLayerSecurityKeyPassPhrase() {
      byte[] var1 = this.getTransportLayerSecurityKeyPassPhraseEncrypted();
      return var1 == null ? null : this._decrypt("TransportLayerSecurityKeyPassPhrase", var1);
   }

   public boolean isTransportLayerSecurityKeyPassPhraseSet() {
      return this.isTransportLayerSecurityKeyPassPhraseEncryptedSet();
   }

   public void setTransportLayerSecurityKeyPassPhrase(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this.setTransportLayerSecurityKeyPassPhraseEncrypted(var1 == null ? null : this._encrypt("TransportLayerSecurityKeyPassPhrase", var1));
   }

   public byte[] getTransportLayerSecurityKeyPassPhraseEncrypted() {
      return this._getHelper()._cloneArray(this._TransportLayerSecurityKeyPassPhraseEncrypted);
   }

   public String getTransportLayerSecurityKeyPassPhraseEncryptedAsString() {
      byte[] var1 = this.getTransportLayerSecurityKeyPassPhraseEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isTransportLayerSecurityKeyPassPhraseEncryptedSet() {
      return this._isSet(42);
   }

   public void setTransportLayerSecurityKeyPassPhraseEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setTransportLayerSecurityKeyPassPhraseEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public String getBasicAuthUsername() {
      return this._BasicAuthUsername;
   }

   public boolean isBasicAuthUsernameSet() {
      return this._isSet(43);
   }

   public void setBasicAuthUsername(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._BasicAuthUsername;
      this._BasicAuthUsername = var1;
      this._postSet(43, var2, var1);
   }

   public String getBasicAuthPassword() {
      byte[] var1 = this.getBasicAuthPasswordEncrypted();
      return var1 == null ? null : this._decrypt("BasicAuthPassword", var1);
   }

   public boolean isBasicAuthPasswordSet() {
      return this.isBasicAuthPasswordEncryptedSet();
   }

   public void setBasicAuthPassword(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      this.setBasicAuthPasswordEncrypted(var1 == null ? null : this._encrypt("BasicAuthPassword", var1));
   }

   public byte[] getBasicAuthPasswordEncrypted() {
      return this._getHelper()._cloneArray(this._BasicAuthPasswordEncrypted);
   }

   public String getBasicAuthPasswordEncryptedAsString() {
      byte[] var1 = this.getBasicAuthPasswordEncrypted();
      return var1 == null ? null : new String(var1);
   }

   public boolean isBasicAuthPasswordEncryptedSet() {
      return this._isSet(45);
   }

   public void setBasicAuthPasswordEncryptedAsString(String var1) {
      try {
         byte[] var2 = var1 == null ? null : var1.getBytes();
         this.setBasicAuthPasswordEncrypted(var2);
      } catch (Exception var3) {
         if (var3 instanceof RuntimeException) {
            throw (RuntimeException)var3;
         } else {
            throw new UndeclaredThrowableException(var3);
         }
      }
   }

   public boolean isWantArtifactRequestsSigned() {
      return this._WantArtifactRequestsSigned;
   }

   public boolean isWantArtifactRequestsSignedSet() {
      return this._isSet(46);
   }

   public void setWantArtifactRequestsSigned(boolean var1) {
      boolean var2 = this._WantArtifactRequestsSigned;
      this._WantArtifactRequestsSigned = var1;
      this._postSet(46, var2, var1);
   }

   public boolean isWantTransportLayerSecurityClientAuthentication() {
      return this._WantTransportLayerSecurityClientAuthentication;
   }

   public boolean isWantTransportLayerSecurityClientAuthenticationSet() {
      return this._isSet(47);
   }

   public void setWantTransportLayerSecurityClientAuthentication(boolean var1) {
      boolean var2 = this._WantTransportLayerSecurityClientAuthentication;
      this._WantTransportLayerSecurityClientAuthentication = var1;
      this._postSet(47, var2, var1);
   }

   public boolean isWantBasicAuthClientAuthentication() {
      return this._WantBasicAuthClientAuthentication;
   }

   public boolean isWantBasicAuthClientAuthenticationSet() {
      return this._isSet(48);
   }

   public void setWantBasicAuthClientAuthentication(boolean var1) {
      boolean var2 = this._WantBasicAuthClientAuthentication;
      this._WantBasicAuthClientAuthentication = var1;
      this._postSet(48, var2, var1);
   }

   public int getAuthnRequestMaxCacheSize() {
      return this._AuthnRequestMaxCacheSize;
   }

   public boolean isAuthnRequestMaxCacheSizeSet() {
      return this._isSet(49);
   }

   public void setAuthnRequestMaxCacheSize(int var1) {
      int var2 = this._AuthnRequestMaxCacheSize;
      this._AuthnRequestMaxCacheSize = var1;
      this._postSet(49, var2, var1);
   }

   public int getAuthnRequestTimeout() {
      return this._AuthnRequestTimeout;
   }

   public boolean isAuthnRequestTimeoutSet() {
      return this._isSet(50);
   }

   public void setAuthnRequestTimeout(int var1) {
      int var2 = this._AuthnRequestTimeout;
      this._AuthnRequestTimeout = var1;
      this._postSet(50, var2, var1);
   }

   public int getArtifactMaxCacheSize() {
      return this._ArtifactMaxCacheSize;
   }

   public boolean isArtifactMaxCacheSizeSet() {
      return this._isSet(51);
   }

   public void setArtifactMaxCacheSize(int var1) {
      int var2 = this._ArtifactMaxCacheSize;
      this._ArtifactMaxCacheSize = var1;
      this._postSet(51, var2, var1);
   }

   public int getArtifactTimeout() {
      return this._ArtifactTimeout;
   }

   public boolean isArtifactTimeoutSet() {
      return this._isSet(52);
   }

   public void setArtifactTimeout(int var1) {
      int var2 = this._ArtifactTimeout;
      this._ArtifactTimeout = var1;
      this._postSet(52, var2, var1);
   }

   public boolean isReplicatedCacheEnabled() {
      return this._ReplicatedCacheEnabled;
   }

   public boolean isReplicatedCacheEnabledSet() {
      return this._isSet(53);
   }

   public void setReplicatedCacheEnabled(boolean var1) {
      boolean var2 = this._ReplicatedCacheEnabled;
      this._ReplicatedCacheEnabled = var1;
      this._postSet(53, var2, var1);
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
      ServerLegalHelper.validateSingleSignOnServices(this);
   }

   public void setBasicAuthPasswordEncrypted(byte[] var1) {
      byte[] var2 = this._BasicAuthPasswordEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: BasicAuthPasswordEncrypted of SingleSignOnServicesMBean");
      } else {
         this._getHelper()._clearArray(this._BasicAuthPasswordEncrypted);
         this._BasicAuthPasswordEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(45, var2, var1);
      }
   }

   public void setSSOSigningKeyPassPhraseEncrypted(byte[] var1) {
      byte[] var2 = this._SSOSigningKeyPassPhraseEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: SSOSigningKeyPassPhraseEncrypted of SingleSignOnServicesMBean");
      } else {
         this._getHelper()._clearArray(this._SSOSigningKeyPassPhraseEncrypted);
         this._SSOSigningKeyPassPhraseEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(27, var2, var1);
      }
   }

   public void setTransportLayerSecurityKeyPassPhraseEncrypted(byte[] var1) {
      byte[] var2 = this._TransportLayerSecurityKeyPassPhraseEncrypted;
      if (this._isProductionModeEnabled() && var1 != null && !this._isEncrypted(var1)) {
         throw new IllegalArgumentException("In production mode, it's not allowed to set a clear text value to the property: TransportLayerSecurityKeyPassPhraseEncrypted of SingleSignOnServicesMBean");
      } else {
         this._getHelper()._clearArray(this._TransportLayerSecurityKeyPassPhraseEncrypted);
         this._TransportLayerSecurityKeyPassPhraseEncrypted = this._getHelper()._cloneArray(var1);
         this._postSet(42, var2, var1);
      }
   }

   protected void _unSet(int var1) {
      if (!this._initializeProperty(var1)) {
         super._unSet(var1);
      } else {
         this._markSet(var1, false);
         if (var1 == 44) {
            this._markSet(45, false);
         }

         if (var1 == 26) {
            this._markSet(27, false);
         }

         if (var1 == 41) {
            this._markSet(42, false);
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
         var1 = 51;
      }

      try {
         switch (var1) {
            case 51:
               this._ArtifactMaxCacheSize = 10000;
               if (var2) {
                  break;
               }
            case 52:
               this._ArtifactTimeout = 300;
               if (var2) {
                  break;
               }
            case 49:
               this._AuthnRequestMaxCacheSize = 10000;
               if (var2) {
                  break;
               }
            case 50:
               this._AuthnRequestTimeout = 300;
               if (var2) {
                  break;
               }
            case 44:
               this._BasicAuthPasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 45:
               this._BasicAuthPasswordEncrypted = null;
               if (var2) {
                  break;
               }
            case 43:
               this._BasicAuthUsername = null;
               if (var2) {
                  break;
               }
            case 10:
               this._ContactPersonCompany = null;
               if (var2) {
                  break;
               }
            case 12:
               this._ContactPersonEmailAddress = null;
               if (var2) {
                  break;
               }
            case 7:
               this._ContactPersonGivenName = null;
               if (var2) {
                  break;
               }
            case 8:
               this._ContactPersonSurName = null;
               if (var2) {
                  break;
               }
            case 11:
               this._ContactPersonTelephoneNumber = null;
               if (var2) {
                  break;
               }
            case 9:
               this._ContactPersonType = null;
               if (var2) {
                  break;
               }
            case 19:
               this._DefaultURL = null;
               if (var2) {
                  break;
               }
            case 16:
               this._EntityID = null;
               if (var2) {
                  break;
               }
            case 17:
               this._ErrorPath = null;
               if (var2) {
                  break;
               }
            case 34:
               this._IdentityProviderPreferredBinding = "None";
               if (var2) {
                  break;
               }
            case 36:
               this._LoginReturnQueryParameter = null;
               if (var2) {
                  break;
               }
            case 35:
               this._LoginURL = "/saml2/idp/login";
               if (var2) {
                  break;
               }
            case 13:
               this._OrganizationName = null;
               if (var2) {
                  break;
               }
            case 14:
               this._OrganizationURL = null;
               if (var2) {
                  break;
               }
            case 15:
               this._PublishedSiteURL = null;
               if (var2) {
                  break;
               }
            case 25:
               this._SSOSigningKeyAlias = null;
               if (var2) {
                  break;
               }
            case 26:
               this._SSOSigningKeyPassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 27:
               this._SSOSigningKeyPassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 22:
               this._ServiceProviderPreferredBinding = "None";
               if (var2) {
                  break;
               }
            case 40:
               this._TransportLayerSecurityKeyAlias = null;
               if (var2) {
                  break;
               }
            case 41:
               this._TransportLayerSecurityKeyPassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 42:
               this._TransportLayerSecurityKeyPassPhraseEncrypted = null;
               if (var2) {
                  break;
               }
            case 28:
               this._ForceAuthn = false;
               if (var2) {
                  break;
               }
            case 31:
               this._IdentityProviderArtifactBindingEnabled = true;
               if (var2) {
                  break;
               }
            case 30:
               this._IdentityProviderEnabled = false;
               if (var2) {
                  break;
               }
            case 32:
               this._IdentityProviderPOSTBindingEnabled = true;
               if (var2) {
                  break;
               }
            case 33:
               this._IdentityProviderRedirectBindingEnabled = true;
               if (var2) {
                  break;
               }
            case 39:
               this._POSTOneUseCheckEnabled = true;
               if (var2) {
                  break;
               }
            case 29:
               this._Passive = false;
               if (var2) {
                  break;
               }
            case 38:
               this._RecipientCheckEnabled = true;
               if (var2) {
                  break;
               }
            case 53:
               this._ReplicatedCacheEnabled = false;
               if (var2) {
                  break;
               }
            case 20:
               this._ServiceProviderArtifactBindingEnabled = true;
               if (var2) {
                  break;
               }
            case 18:
               this._ServiceProviderEnabled = false;
               if (var2) {
                  break;
               }
            case 21:
               this._ServiceProviderPOSTBindingEnabled = true;
               if (var2) {
                  break;
               }
            case 23:
               this._SignAuthnRequests = false;
               if (var2) {
                  break;
               }
            case 46:
               this._WantArtifactRequestsSigned = false;
               if (var2) {
                  break;
               }
            case 24:
               this._WantAssertionsSigned = false;
               if (var2) {
                  break;
               }
            case 37:
               this._WantAuthnRequestsSigned = false;
               if (var2) {
                  break;
               }
            case 48:
               this._WantBasicAuthClientAuthentication = false;
               if (var2) {
                  break;
               }
            case 47:
               this._WantTransportLayerSecurityClientAuthentication = false;
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
      return "SingleSignOnServices";
   }

   public void putValue(String var1, Object var2) {
      int var6;
      if (var1.equals("ArtifactMaxCacheSize")) {
         var6 = this._ArtifactMaxCacheSize;
         this._ArtifactMaxCacheSize = (Integer)var2;
         this._postSet(51, var6, this._ArtifactMaxCacheSize);
      } else if (var1.equals("ArtifactTimeout")) {
         var6 = this._ArtifactTimeout;
         this._ArtifactTimeout = (Integer)var2;
         this._postSet(52, var6, this._ArtifactTimeout);
      } else if (var1.equals("AuthnRequestMaxCacheSize")) {
         var6 = this._AuthnRequestMaxCacheSize;
         this._AuthnRequestMaxCacheSize = (Integer)var2;
         this._postSet(49, var6, this._AuthnRequestMaxCacheSize);
      } else if (var1.equals("AuthnRequestTimeout")) {
         var6 = this._AuthnRequestTimeout;
         this._AuthnRequestTimeout = (Integer)var2;
         this._postSet(50, var6, this._AuthnRequestTimeout);
      } else {
         String var5;
         if (var1.equals("BasicAuthPassword")) {
            var5 = this._BasicAuthPassword;
            this._BasicAuthPassword = (String)var2;
            this._postSet(44, var5, this._BasicAuthPassword);
         } else {
            byte[] var4;
            if (var1.equals("BasicAuthPasswordEncrypted")) {
               var4 = this._BasicAuthPasswordEncrypted;
               this._BasicAuthPasswordEncrypted = (byte[])((byte[])var2);
               this._postSet(45, var4, this._BasicAuthPasswordEncrypted);
            } else if (var1.equals("BasicAuthUsername")) {
               var5 = this._BasicAuthUsername;
               this._BasicAuthUsername = (String)var2;
               this._postSet(43, var5, this._BasicAuthUsername);
            } else if (var1.equals("ContactPersonCompany")) {
               var5 = this._ContactPersonCompany;
               this._ContactPersonCompany = (String)var2;
               this._postSet(10, var5, this._ContactPersonCompany);
            } else if (var1.equals("ContactPersonEmailAddress")) {
               var5 = this._ContactPersonEmailAddress;
               this._ContactPersonEmailAddress = (String)var2;
               this._postSet(12, var5, this._ContactPersonEmailAddress);
            } else if (var1.equals("ContactPersonGivenName")) {
               var5 = this._ContactPersonGivenName;
               this._ContactPersonGivenName = (String)var2;
               this._postSet(7, var5, this._ContactPersonGivenName);
            } else if (var1.equals("ContactPersonSurName")) {
               var5 = this._ContactPersonSurName;
               this._ContactPersonSurName = (String)var2;
               this._postSet(8, var5, this._ContactPersonSurName);
            } else if (var1.equals("ContactPersonTelephoneNumber")) {
               var5 = this._ContactPersonTelephoneNumber;
               this._ContactPersonTelephoneNumber = (String)var2;
               this._postSet(11, var5, this._ContactPersonTelephoneNumber);
            } else if (var1.equals("ContactPersonType")) {
               var5 = this._ContactPersonType;
               this._ContactPersonType = (String)var2;
               this._postSet(9, var5, this._ContactPersonType);
            } else if (var1.equals("DefaultURL")) {
               var5 = this._DefaultURL;
               this._DefaultURL = (String)var2;
               this._postSet(19, var5, this._DefaultURL);
            } else if (var1.equals("EntityID")) {
               var5 = this._EntityID;
               this._EntityID = (String)var2;
               this._postSet(16, var5, this._EntityID);
            } else if (var1.equals("ErrorPath")) {
               var5 = this._ErrorPath;
               this._ErrorPath = (String)var2;
               this._postSet(17, var5, this._ErrorPath);
            } else {
               boolean var3;
               if (var1.equals("ForceAuthn")) {
                  var3 = this._ForceAuthn;
                  this._ForceAuthn = (Boolean)var2;
                  this._postSet(28, var3, this._ForceAuthn);
               } else if (var1.equals("IdentityProviderArtifactBindingEnabled")) {
                  var3 = this._IdentityProviderArtifactBindingEnabled;
                  this._IdentityProviderArtifactBindingEnabled = (Boolean)var2;
                  this._postSet(31, var3, this._IdentityProviderArtifactBindingEnabled);
               } else if (var1.equals("IdentityProviderEnabled")) {
                  var3 = this._IdentityProviderEnabled;
                  this._IdentityProviderEnabled = (Boolean)var2;
                  this._postSet(30, var3, this._IdentityProviderEnabled);
               } else if (var1.equals("IdentityProviderPOSTBindingEnabled")) {
                  var3 = this._IdentityProviderPOSTBindingEnabled;
                  this._IdentityProviderPOSTBindingEnabled = (Boolean)var2;
                  this._postSet(32, var3, this._IdentityProviderPOSTBindingEnabled);
               } else if (var1.equals("IdentityProviderPreferredBinding")) {
                  var5 = this._IdentityProviderPreferredBinding;
                  this._IdentityProviderPreferredBinding = (String)var2;
                  this._postSet(34, var5, this._IdentityProviderPreferredBinding);
               } else if (var1.equals("IdentityProviderRedirectBindingEnabled")) {
                  var3 = this._IdentityProviderRedirectBindingEnabled;
                  this._IdentityProviderRedirectBindingEnabled = (Boolean)var2;
                  this._postSet(33, var3, this._IdentityProviderRedirectBindingEnabled);
               } else if (var1.equals("LoginReturnQueryParameter")) {
                  var5 = this._LoginReturnQueryParameter;
                  this._LoginReturnQueryParameter = (String)var2;
                  this._postSet(36, var5, this._LoginReturnQueryParameter);
               } else if (var1.equals("LoginURL")) {
                  var5 = this._LoginURL;
                  this._LoginURL = (String)var2;
                  this._postSet(35, var5, this._LoginURL);
               } else if (var1.equals("OrganizationName")) {
                  var5 = this._OrganizationName;
                  this._OrganizationName = (String)var2;
                  this._postSet(13, var5, this._OrganizationName);
               } else if (var1.equals("OrganizationURL")) {
                  var5 = this._OrganizationURL;
                  this._OrganizationURL = (String)var2;
                  this._postSet(14, var5, this._OrganizationURL);
               } else if (var1.equals("POSTOneUseCheckEnabled")) {
                  var3 = this._POSTOneUseCheckEnabled;
                  this._POSTOneUseCheckEnabled = (Boolean)var2;
                  this._postSet(39, var3, this._POSTOneUseCheckEnabled);
               } else if (var1.equals("Passive")) {
                  var3 = this._Passive;
                  this._Passive = (Boolean)var2;
                  this._postSet(29, var3, this._Passive);
               } else if (var1.equals("PublishedSiteURL")) {
                  var5 = this._PublishedSiteURL;
                  this._PublishedSiteURL = (String)var2;
                  this._postSet(15, var5, this._PublishedSiteURL);
               } else if (var1.equals("RecipientCheckEnabled")) {
                  var3 = this._RecipientCheckEnabled;
                  this._RecipientCheckEnabled = (Boolean)var2;
                  this._postSet(38, var3, this._RecipientCheckEnabled);
               } else if (var1.equals("ReplicatedCacheEnabled")) {
                  var3 = this._ReplicatedCacheEnabled;
                  this._ReplicatedCacheEnabled = (Boolean)var2;
                  this._postSet(53, var3, this._ReplicatedCacheEnabled);
               } else if (var1.equals("SSOSigningKeyAlias")) {
                  var5 = this._SSOSigningKeyAlias;
                  this._SSOSigningKeyAlias = (String)var2;
                  this._postSet(25, var5, this._SSOSigningKeyAlias);
               } else if (var1.equals("SSOSigningKeyPassPhrase")) {
                  var5 = this._SSOSigningKeyPassPhrase;
                  this._SSOSigningKeyPassPhrase = (String)var2;
                  this._postSet(26, var5, this._SSOSigningKeyPassPhrase);
               } else if (var1.equals("SSOSigningKeyPassPhraseEncrypted")) {
                  var4 = this._SSOSigningKeyPassPhraseEncrypted;
                  this._SSOSigningKeyPassPhraseEncrypted = (byte[])((byte[])var2);
                  this._postSet(27, var4, this._SSOSigningKeyPassPhraseEncrypted);
               } else if (var1.equals("ServiceProviderArtifactBindingEnabled")) {
                  var3 = this._ServiceProviderArtifactBindingEnabled;
                  this._ServiceProviderArtifactBindingEnabled = (Boolean)var2;
                  this._postSet(20, var3, this._ServiceProviderArtifactBindingEnabled);
               } else if (var1.equals("ServiceProviderEnabled")) {
                  var3 = this._ServiceProviderEnabled;
                  this._ServiceProviderEnabled = (Boolean)var2;
                  this._postSet(18, var3, this._ServiceProviderEnabled);
               } else if (var1.equals("ServiceProviderPOSTBindingEnabled")) {
                  var3 = this._ServiceProviderPOSTBindingEnabled;
                  this._ServiceProviderPOSTBindingEnabled = (Boolean)var2;
                  this._postSet(21, var3, this._ServiceProviderPOSTBindingEnabled);
               } else if (var1.equals("ServiceProviderPreferredBinding")) {
                  var5 = this._ServiceProviderPreferredBinding;
                  this._ServiceProviderPreferredBinding = (String)var2;
                  this._postSet(22, var5, this._ServiceProviderPreferredBinding);
               } else if (var1.equals("SignAuthnRequests")) {
                  var3 = this._SignAuthnRequests;
                  this._SignAuthnRequests = (Boolean)var2;
                  this._postSet(23, var3, this._SignAuthnRequests);
               } else if (var1.equals("TransportLayerSecurityKeyAlias")) {
                  var5 = this._TransportLayerSecurityKeyAlias;
                  this._TransportLayerSecurityKeyAlias = (String)var2;
                  this._postSet(40, var5, this._TransportLayerSecurityKeyAlias);
               } else if (var1.equals("TransportLayerSecurityKeyPassPhrase")) {
                  var5 = this._TransportLayerSecurityKeyPassPhrase;
                  this._TransportLayerSecurityKeyPassPhrase = (String)var2;
                  this._postSet(41, var5, this._TransportLayerSecurityKeyPassPhrase);
               } else if (var1.equals("TransportLayerSecurityKeyPassPhraseEncrypted")) {
                  var4 = this._TransportLayerSecurityKeyPassPhraseEncrypted;
                  this._TransportLayerSecurityKeyPassPhraseEncrypted = (byte[])((byte[])var2);
                  this._postSet(42, var4, this._TransportLayerSecurityKeyPassPhraseEncrypted);
               } else if (var1.equals("WantArtifactRequestsSigned")) {
                  var3 = this._WantArtifactRequestsSigned;
                  this._WantArtifactRequestsSigned = (Boolean)var2;
                  this._postSet(46, var3, this._WantArtifactRequestsSigned);
               } else if (var1.equals("WantAssertionsSigned")) {
                  var3 = this._WantAssertionsSigned;
                  this._WantAssertionsSigned = (Boolean)var2;
                  this._postSet(24, var3, this._WantAssertionsSigned);
               } else if (var1.equals("WantAuthnRequestsSigned")) {
                  var3 = this._WantAuthnRequestsSigned;
                  this._WantAuthnRequestsSigned = (Boolean)var2;
                  this._postSet(37, var3, this._WantAuthnRequestsSigned);
               } else if (var1.equals("WantBasicAuthClientAuthentication")) {
                  var3 = this._WantBasicAuthClientAuthentication;
                  this._WantBasicAuthClientAuthentication = (Boolean)var2;
                  this._postSet(48, var3, this._WantBasicAuthClientAuthentication);
               } else if (var1.equals("WantTransportLayerSecurityClientAuthentication")) {
                  var3 = this._WantTransportLayerSecurityClientAuthentication;
                  this._WantTransportLayerSecurityClientAuthentication = (Boolean)var2;
                  this._postSet(47, var3, this._WantTransportLayerSecurityClientAuthentication);
               } else {
                  super.putValue(var1, var2);
               }
            }
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("ArtifactMaxCacheSize")) {
         return new Integer(this._ArtifactMaxCacheSize);
      } else if (var1.equals("ArtifactTimeout")) {
         return new Integer(this._ArtifactTimeout);
      } else if (var1.equals("AuthnRequestMaxCacheSize")) {
         return new Integer(this._AuthnRequestMaxCacheSize);
      } else if (var1.equals("AuthnRequestTimeout")) {
         return new Integer(this._AuthnRequestTimeout);
      } else if (var1.equals("BasicAuthPassword")) {
         return this._BasicAuthPassword;
      } else if (var1.equals("BasicAuthPasswordEncrypted")) {
         return this._BasicAuthPasswordEncrypted;
      } else if (var1.equals("BasicAuthUsername")) {
         return this._BasicAuthUsername;
      } else if (var1.equals("ContactPersonCompany")) {
         return this._ContactPersonCompany;
      } else if (var1.equals("ContactPersonEmailAddress")) {
         return this._ContactPersonEmailAddress;
      } else if (var1.equals("ContactPersonGivenName")) {
         return this._ContactPersonGivenName;
      } else if (var1.equals("ContactPersonSurName")) {
         return this._ContactPersonSurName;
      } else if (var1.equals("ContactPersonTelephoneNumber")) {
         return this._ContactPersonTelephoneNumber;
      } else if (var1.equals("ContactPersonType")) {
         return this._ContactPersonType;
      } else if (var1.equals("DefaultURL")) {
         return this._DefaultURL;
      } else if (var1.equals("EntityID")) {
         return this._EntityID;
      } else if (var1.equals("ErrorPath")) {
         return this._ErrorPath;
      } else if (var1.equals("ForceAuthn")) {
         return new Boolean(this._ForceAuthn);
      } else if (var1.equals("IdentityProviderArtifactBindingEnabled")) {
         return new Boolean(this._IdentityProviderArtifactBindingEnabled);
      } else if (var1.equals("IdentityProviderEnabled")) {
         return new Boolean(this._IdentityProviderEnabled);
      } else if (var1.equals("IdentityProviderPOSTBindingEnabled")) {
         return new Boolean(this._IdentityProviderPOSTBindingEnabled);
      } else if (var1.equals("IdentityProviderPreferredBinding")) {
         return this._IdentityProviderPreferredBinding;
      } else if (var1.equals("IdentityProviderRedirectBindingEnabled")) {
         return new Boolean(this._IdentityProviderRedirectBindingEnabled);
      } else if (var1.equals("LoginReturnQueryParameter")) {
         return this._LoginReturnQueryParameter;
      } else if (var1.equals("LoginURL")) {
         return this._LoginURL;
      } else if (var1.equals("OrganizationName")) {
         return this._OrganizationName;
      } else if (var1.equals("OrganizationURL")) {
         return this._OrganizationURL;
      } else if (var1.equals("POSTOneUseCheckEnabled")) {
         return new Boolean(this._POSTOneUseCheckEnabled);
      } else if (var1.equals("Passive")) {
         return new Boolean(this._Passive);
      } else if (var1.equals("PublishedSiteURL")) {
         return this._PublishedSiteURL;
      } else if (var1.equals("RecipientCheckEnabled")) {
         return new Boolean(this._RecipientCheckEnabled);
      } else if (var1.equals("ReplicatedCacheEnabled")) {
         return new Boolean(this._ReplicatedCacheEnabled);
      } else if (var1.equals("SSOSigningKeyAlias")) {
         return this._SSOSigningKeyAlias;
      } else if (var1.equals("SSOSigningKeyPassPhrase")) {
         return this._SSOSigningKeyPassPhrase;
      } else if (var1.equals("SSOSigningKeyPassPhraseEncrypted")) {
         return this._SSOSigningKeyPassPhraseEncrypted;
      } else if (var1.equals("ServiceProviderArtifactBindingEnabled")) {
         return new Boolean(this._ServiceProviderArtifactBindingEnabled);
      } else if (var1.equals("ServiceProviderEnabled")) {
         return new Boolean(this._ServiceProviderEnabled);
      } else if (var1.equals("ServiceProviderPOSTBindingEnabled")) {
         return new Boolean(this._ServiceProviderPOSTBindingEnabled);
      } else if (var1.equals("ServiceProviderPreferredBinding")) {
         return this._ServiceProviderPreferredBinding;
      } else if (var1.equals("SignAuthnRequests")) {
         return new Boolean(this._SignAuthnRequests);
      } else if (var1.equals("TransportLayerSecurityKeyAlias")) {
         return this._TransportLayerSecurityKeyAlias;
      } else if (var1.equals("TransportLayerSecurityKeyPassPhrase")) {
         return this._TransportLayerSecurityKeyPassPhrase;
      } else if (var1.equals("TransportLayerSecurityKeyPassPhraseEncrypted")) {
         return this._TransportLayerSecurityKeyPassPhraseEncrypted;
      } else if (var1.equals("WantArtifactRequestsSigned")) {
         return new Boolean(this._WantArtifactRequestsSigned);
      } else if (var1.equals("WantAssertionsSigned")) {
         return new Boolean(this._WantAssertionsSigned);
      } else if (var1.equals("WantAuthnRequestsSigned")) {
         return new Boolean(this._WantAuthnRequestsSigned);
      } else if (var1.equals("WantBasicAuthClientAuthentication")) {
         return new Boolean(this._WantBasicAuthClientAuthentication);
      } else {
         return var1.equals("WantTransportLayerSecurityClientAuthentication") ? new Boolean(this._WantTransportLayerSecurityClientAuthentication) : super.getValue(var1);
      }
   }

   public static void validateGeneration() {
      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("IdentityProviderPreferredBinding", "None");
      } catch (IllegalArgumentException var4) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property IdentityProviderPreferredBinding in SingleSignOnServicesMBean" + var4.getMessage());
      }

      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonEmptyString("LoginURL", "/saml2/idp/login");
      } catch (IllegalArgumentException var3) {
         throw new DescriptorValidateException("The default value for the property  is zero-length. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-zero-length value on @default annotation. Refer annotation legalZeroLength on property LoginURL in SingleSignOnServicesMBean" + var3.getMessage());
      }

      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("LoginURL", "/saml2/idp/login");
      } catch (IllegalArgumentException var2) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property LoginURL in SingleSignOnServicesMBean" + var2.getMessage());
      }

      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("ServiceProviderPreferredBinding", "None");
      } catch (IllegalArgumentException var1) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property ServiceProviderPreferredBinding in SingleSignOnServicesMBean" + var1.getMessage());
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 7:
               if (var1.equals("passive")) {
                  return 29;
               }
               break;
            case 8:
               if (var1.equals("entityid")) {
                  return 16;
               }
               break;
            case 9:
               if (var1.equals("login-url")) {
                  return 35;
               }
               break;
            case 10:
               if (var1.equals("error-path")) {
                  return 17;
               }
               break;
            case 11:
               if (var1.equals("default-url")) {
                  return 19;
               }

               if (var1.equals("force-authn")) {
                  return 28;
               }
            case 12:
            case 13:
            case 14:
            case 15:
            case 20:
            case 30:
            case 32:
            case 33:
            case 38:
            case 39:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            default:
               break;
            case 16:
               if (var1.equals("artifact-timeout")) {
                  return 52;
               }

               if (var1.equals("organization-url")) {
                  return 14;
               }
               break;
            case 17:
               if (var1.equals("organization-name")) {
                  return 13;
               }
               break;
            case 18:
               if (var1.equals("published-site-url")) {
                  return 15;
               }
               break;
            case 19:
               if (var1.equals("basic-auth-password")) {
                  return 44;
               }

               if (var1.equals("basic-auth-username")) {
                  return 43;
               }

               if (var1.equals("contact-person-type")) {
                  return 9;
               }

               if (var1.equals("sign-authn-requests")) {
                  return 23;
               }
               break;
            case 21:
               if (var1.equals("authn-request-timeout")) {
                  return 50;
               }

               if (var1.equals("sso-signing-key-alias")) {
                  return 25;
               }
               break;
            case 22:
               if (var1.equals("contact-person-company")) {
                  return 10;
               }

               if (var1.equals("want-assertions-signed")) {
                  return 24;
               }
               break;
            case 23:
               if (var1.equals("artifact-max-cache-size")) {
                  return 51;
               }

               if (var1.equals("contact-person-sur-name")) {
                  return 8;
               }

               if (var1.equals("recipient-check-enabled")) {
                  return 38;
               }
               break;
            case 24:
               if (var1.equals("replicated-cache-enabled")) {
                  return 53;
               }

               if (var1.equals("service-provider-enabled")) {
                  return 18;
               }
               break;
            case 25:
               if (var1.equals("contact-person-given-name")) {
                  return 7;
               }

               if (var1.equals("identity-provider-enabled")) {
                  return 30;
               }
               break;
            case 26:
               if (var1.equals("post-one-use-check-enabled")) {
                  return 39;
               }

               if (var1.equals("want-authn-requests-signed")) {
                  return 37;
               }
               break;
            case 27:
               if (var1.equals("sso-signing-key-pass-phrase")) {
                  return 26;
               }
               break;
            case 28:
               if (var1.equals("authn-request-max-cache-size")) {
                  return 49;
               }

               if (var1.equals("contact-person-email-address")) {
                  return 12;
               }

               if (var1.equals("login-return-query-parameter")) {
                  return 36;
               }
               break;
            case 29:
               if (var1.equals("basic-auth-password-encrypted")) {
                  return 45;
               }

               if (var1.equals("want-artifact-requests-signed")) {
                  return 46;
               }
               break;
            case 31:
               if (var1.equals("contact-person-telephone-number")) {
                  return 11;
               }
               break;
            case 34:
               if (var1.equals("service-provider-preferred-binding")) {
                  return 22;
               }

               if (var1.equals("transport-layer-security-key-alias")) {
                  return 40;
               }
               break;
            case 35:
               if (var1.equals("identity-provider-preferred-binding")) {
                  return 34;
               }
               break;
            case 36:
               if (var1.equals("service-providerpost-binding-enabled")) {
                  return 21;
               }
               break;
            case 37:
               if (var1.equals("sso-signing-key-pass-phrase-encrypted")) {
                  return 27;
               }

               if (var1.equals("identity-providerpost-binding-enabled")) {
                  return 32;
               }

               if (var1.equals("want-basic-auth-client-authentication")) {
                  return 48;
               }
               break;
            case 40:
               if (var1.equals("transport-layer-security-key-pass-phrase")) {
                  return 41;
               }
               break;
            case 41:
               if (var1.equals("service-provider-artifact-binding-enabled")) {
                  return 20;
               }
               break;
            case 42:
               if (var1.equals("identity-provider-artifact-binding-enabled")) {
                  return 31;
               }

               if (var1.equals("identity-provider-redirect-binding-enabled")) {
                  return 33;
               }
               break;
            case 50:
               if (var1.equals("transport-layer-security-key-pass-phrase-encrypted")) {
                  return 42;
               }
               break;
            case 51:
               if (var1.equals("want-transport-layer-security-client-authentication")) {
                  return 47;
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
               return "contact-person-given-name";
            case 8:
               return "contact-person-sur-name";
            case 9:
               return "contact-person-type";
            case 10:
               return "contact-person-company";
            case 11:
               return "contact-person-telephone-number";
            case 12:
               return "contact-person-email-address";
            case 13:
               return "organization-name";
            case 14:
               return "organization-url";
            case 15:
               return "published-site-url";
            case 16:
               return "entityid";
            case 17:
               return "error-path";
            case 18:
               return "service-provider-enabled";
            case 19:
               return "default-url";
            case 20:
               return "service-provider-artifact-binding-enabled";
            case 21:
               return "service-providerpost-binding-enabled";
            case 22:
               return "service-provider-preferred-binding";
            case 23:
               return "sign-authn-requests";
            case 24:
               return "want-assertions-signed";
            case 25:
               return "sso-signing-key-alias";
            case 26:
               return "sso-signing-key-pass-phrase";
            case 27:
               return "sso-signing-key-pass-phrase-encrypted";
            case 28:
               return "force-authn";
            case 29:
               return "passive";
            case 30:
               return "identity-provider-enabled";
            case 31:
               return "identity-provider-artifact-binding-enabled";
            case 32:
               return "identity-providerpost-binding-enabled";
            case 33:
               return "identity-provider-redirect-binding-enabled";
            case 34:
               return "identity-provider-preferred-binding";
            case 35:
               return "login-url";
            case 36:
               return "login-return-query-parameter";
            case 37:
               return "want-authn-requests-signed";
            case 38:
               return "recipient-check-enabled";
            case 39:
               return "post-one-use-check-enabled";
            case 40:
               return "transport-layer-security-key-alias";
            case 41:
               return "transport-layer-security-key-pass-phrase";
            case 42:
               return "transport-layer-security-key-pass-phrase-encrypted";
            case 43:
               return "basic-auth-username";
            case 44:
               return "basic-auth-password";
            case 45:
               return "basic-auth-password-encrypted";
            case 46:
               return "want-artifact-requests-signed";
            case 47:
               return "want-transport-layer-security-client-authentication";
            case 48:
               return "want-basic-auth-client-authentication";
            case 49:
               return "authn-request-max-cache-size";
            case 50:
               return "authn-request-timeout";
            case 51:
               return "artifact-max-cache-size";
            case 52:
               return "artifact-timeout";
            case 53:
               return "replicated-cache-enabled";
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
      private SingleSignOnServicesMBeanImpl bean;

      protected Helper(SingleSignOnServicesMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "ContactPersonGivenName";
            case 8:
               return "ContactPersonSurName";
            case 9:
               return "ContactPersonType";
            case 10:
               return "ContactPersonCompany";
            case 11:
               return "ContactPersonTelephoneNumber";
            case 12:
               return "ContactPersonEmailAddress";
            case 13:
               return "OrganizationName";
            case 14:
               return "OrganizationURL";
            case 15:
               return "PublishedSiteURL";
            case 16:
               return "EntityID";
            case 17:
               return "ErrorPath";
            case 18:
               return "ServiceProviderEnabled";
            case 19:
               return "DefaultURL";
            case 20:
               return "ServiceProviderArtifactBindingEnabled";
            case 21:
               return "ServiceProviderPOSTBindingEnabled";
            case 22:
               return "ServiceProviderPreferredBinding";
            case 23:
               return "SignAuthnRequests";
            case 24:
               return "WantAssertionsSigned";
            case 25:
               return "SSOSigningKeyAlias";
            case 26:
               return "SSOSigningKeyPassPhrase";
            case 27:
               return "SSOSigningKeyPassPhraseEncrypted";
            case 28:
               return "ForceAuthn";
            case 29:
               return "Passive";
            case 30:
               return "IdentityProviderEnabled";
            case 31:
               return "IdentityProviderArtifactBindingEnabled";
            case 32:
               return "IdentityProviderPOSTBindingEnabled";
            case 33:
               return "IdentityProviderRedirectBindingEnabled";
            case 34:
               return "IdentityProviderPreferredBinding";
            case 35:
               return "LoginURL";
            case 36:
               return "LoginReturnQueryParameter";
            case 37:
               return "WantAuthnRequestsSigned";
            case 38:
               return "RecipientCheckEnabled";
            case 39:
               return "POSTOneUseCheckEnabled";
            case 40:
               return "TransportLayerSecurityKeyAlias";
            case 41:
               return "TransportLayerSecurityKeyPassPhrase";
            case 42:
               return "TransportLayerSecurityKeyPassPhraseEncrypted";
            case 43:
               return "BasicAuthUsername";
            case 44:
               return "BasicAuthPassword";
            case 45:
               return "BasicAuthPasswordEncrypted";
            case 46:
               return "WantArtifactRequestsSigned";
            case 47:
               return "WantTransportLayerSecurityClientAuthentication";
            case 48:
               return "WantBasicAuthClientAuthentication";
            case 49:
               return "AuthnRequestMaxCacheSize";
            case 50:
               return "AuthnRequestTimeout";
            case 51:
               return "ArtifactMaxCacheSize";
            case 52:
               return "ArtifactTimeout";
            case 53:
               return "ReplicatedCacheEnabled";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("ArtifactMaxCacheSize")) {
            return 51;
         } else if (var1.equals("ArtifactTimeout")) {
            return 52;
         } else if (var1.equals("AuthnRequestMaxCacheSize")) {
            return 49;
         } else if (var1.equals("AuthnRequestTimeout")) {
            return 50;
         } else if (var1.equals("BasicAuthPassword")) {
            return 44;
         } else if (var1.equals("BasicAuthPasswordEncrypted")) {
            return 45;
         } else if (var1.equals("BasicAuthUsername")) {
            return 43;
         } else if (var1.equals("ContactPersonCompany")) {
            return 10;
         } else if (var1.equals("ContactPersonEmailAddress")) {
            return 12;
         } else if (var1.equals("ContactPersonGivenName")) {
            return 7;
         } else if (var1.equals("ContactPersonSurName")) {
            return 8;
         } else if (var1.equals("ContactPersonTelephoneNumber")) {
            return 11;
         } else if (var1.equals("ContactPersonType")) {
            return 9;
         } else if (var1.equals("DefaultURL")) {
            return 19;
         } else if (var1.equals("EntityID")) {
            return 16;
         } else if (var1.equals("ErrorPath")) {
            return 17;
         } else if (var1.equals("IdentityProviderPreferredBinding")) {
            return 34;
         } else if (var1.equals("LoginReturnQueryParameter")) {
            return 36;
         } else if (var1.equals("LoginURL")) {
            return 35;
         } else if (var1.equals("OrganizationName")) {
            return 13;
         } else if (var1.equals("OrganizationURL")) {
            return 14;
         } else if (var1.equals("PublishedSiteURL")) {
            return 15;
         } else if (var1.equals("SSOSigningKeyAlias")) {
            return 25;
         } else if (var1.equals("SSOSigningKeyPassPhrase")) {
            return 26;
         } else if (var1.equals("SSOSigningKeyPassPhraseEncrypted")) {
            return 27;
         } else if (var1.equals("ServiceProviderPreferredBinding")) {
            return 22;
         } else if (var1.equals("TransportLayerSecurityKeyAlias")) {
            return 40;
         } else if (var1.equals("TransportLayerSecurityKeyPassPhrase")) {
            return 41;
         } else if (var1.equals("TransportLayerSecurityKeyPassPhraseEncrypted")) {
            return 42;
         } else if (var1.equals("ForceAuthn")) {
            return 28;
         } else if (var1.equals("IdentityProviderArtifactBindingEnabled")) {
            return 31;
         } else if (var1.equals("IdentityProviderEnabled")) {
            return 30;
         } else if (var1.equals("IdentityProviderPOSTBindingEnabled")) {
            return 32;
         } else if (var1.equals("IdentityProviderRedirectBindingEnabled")) {
            return 33;
         } else if (var1.equals("POSTOneUseCheckEnabled")) {
            return 39;
         } else if (var1.equals("Passive")) {
            return 29;
         } else if (var1.equals("RecipientCheckEnabled")) {
            return 38;
         } else if (var1.equals("ReplicatedCacheEnabled")) {
            return 53;
         } else if (var1.equals("ServiceProviderArtifactBindingEnabled")) {
            return 20;
         } else if (var1.equals("ServiceProviderEnabled")) {
            return 18;
         } else if (var1.equals("ServiceProviderPOSTBindingEnabled")) {
            return 21;
         } else if (var1.equals("SignAuthnRequests")) {
            return 23;
         } else if (var1.equals("WantArtifactRequestsSigned")) {
            return 46;
         } else if (var1.equals("WantAssertionsSigned")) {
            return 24;
         } else if (var1.equals("WantAuthnRequestsSigned")) {
            return 37;
         } else if (var1.equals("WantBasicAuthClientAuthentication")) {
            return 48;
         } else {
            return var1.equals("WantTransportLayerSecurityClientAuthentication") ? 47 : super.getPropertyIndex(var1);
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
            if (this.bean.isArtifactMaxCacheSizeSet()) {
               var2.append("ArtifactMaxCacheSize");
               var2.append(String.valueOf(this.bean.getArtifactMaxCacheSize()));
            }

            if (this.bean.isArtifactTimeoutSet()) {
               var2.append("ArtifactTimeout");
               var2.append(String.valueOf(this.bean.getArtifactTimeout()));
            }

            if (this.bean.isAuthnRequestMaxCacheSizeSet()) {
               var2.append("AuthnRequestMaxCacheSize");
               var2.append(String.valueOf(this.bean.getAuthnRequestMaxCacheSize()));
            }

            if (this.bean.isAuthnRequestTimeoutSet()) {
               var2.append("AuthnRequestTimeout");
               var2.append(String.valueOf(this.bean.getAuthnRequestTimeout()));
            }

            if (this.bean.isBasicAuthPasswordSet()) {
               var2.append("BasicAuthPassword");
               var2.append(String.valueOf(this.bean.getBasicAuthPassword()));
            }

            if (this.bean.isBasicAuthPasswordEncryptedSet()) {
               var2.append("BasicAuthPasswordEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getBasicAuthPasswordEncrypted())));
            }

            if (this.bean.isBasicAuthUsernameSet()) {
               var2.append("BasicAuthUsername");
               var2.append(String.valueOf(this.bean.getBasicAuthUsername()));
            }

            if (this.bean.isContactPersonCompanySet()) {
               var2.append("ContactPersonCompany");
               var2.append(String.valueOf(this.bean.getContactPersonCompany()));
            }

            if (this.bean.isContactPersonEmailAddressSet()) {
               var2.append("ContactPersonEmailAddress");
               var2.append(String.valueOf(this.bean.getContactPersonEmailAddress()));
            }

            if (this.bean.isContactPersonGivenNameSet()) {
               var2.append("ContactPersonGivenName");
               var2.append(String.valueOf(this.bean.getContactPersonGivenName()));
            }

            if (this.bean.isContactPersonSurNameSet()) {
               var2.append("ContactPersonSurName");
               var2.append(String.valueOf(this.bean.getContactPersonSurName()));
            }

            if (this.bean.isContactPersonTelephoneNumberSet()) {
               var2.append("ContactPersonTelephoneNumber");
               var2.append(String.valueOf(this.bean.getContactPersonTelephoneNumber()));
            }

            if (this.bean.isContactPersonTypeSet()) {
               var2.append("ContactPersonType");
               var2.append(String.valueOf(this.bean.getContactPersonType()));
            }

            if (this.bean.isDefaultURLSet()) {
               var2.append("DefaultURL");
               var2.append(String.valueOf(this.bean.getDefaultURL()));
            }

            if (this.bean.isEntityIDSet()) {
               var2.append("EntityID");
               var2.append(String.valueOf(this.bean.getEntityID()));
            }

            if (this.bean.isErrorPathSet()) {
               var2.append("ErrorPath");
               var2.append(String.valueOf(this.bean.getErrorPath()));
            }

            if (this.bean.isIdentityProviderPreferredBindingSet()) {
               var2.append("IdentityProviderPreferredBinding");
               var2.append(String.valueOf(this.bean.getIdentityProviderPreferredBinding()));
            }

            if (this.bean.isLoginReturnQueryParameterSet()) {
               var2.append("LoginReturnQueryParameter");
               var2.append(String.valueOf(this.bean.getLoginReturnQueryParameter()));
            }

            if (this.bean.isLoginURLSet()) {
               var2.append("LoginURL");
               var2.append(String.valueOf(this.bean.getLoginURL()));
            }

            if (this.bean.isOrganizationNameSet()) {
               var2.append("OrganizationName");
               var2.append(String.valueOf(this.bean.getOrganizationName()));
            }

            if (this.bean.isOrganizationURLSet()) {
               var2.append("OrganizationURL");
               var2.append(String.valueOf(this.bean.getOrganizationURL()));
            }

            if (this.bean.isPublishedSiteURLSet()) {
               var2.append("PublishedSiteURL");
               var2.append(String.valueOf(this.bean.getPublishedSiteURL()));
            }

            if (this.bean.isSSOSigningKeyAliasSet()) {
               var2.append("SSOSigningKeyAlias");
               var2.append(String.valueOf(this.bean.getSSOSigningKeyAlias()));
            }

            if (this.bean.isSSOSigningKeyPassPhraseSet()) {
               var2.append("SSOSigningKeyPassPhrase");
               var2.append(String.valueOf(this.bean.getSSOSigningKeyPassPhrase()));
            }

            if (this.bean.isSSOSigningKeyPassPhraseEncryptedSet()) {
               var2.append("SSOSigningKeyPassPhraseEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getSSOSigningKeyPassPhraseEncrypted())));
            }

            if (this.bean.isServiceProviderPreferredBindingSet()) {
               var2.append("ServiceProviderPreferredBinding");
               var2.append(String.valueOf(this.bean.getServiceProviderPreferredBinding()));
            }

            if (this.bean.isTransportLayerSecurityKeyAliasSet()) {
               var2.append("TransportLayerSecurityKeyAlias");
               var2.append(String.valueOf(this.bean.getTransportLayerSecurityKeyAlias()));
            }

            if (this.bean.isTransportLayerSecurityKeyPassPhraseSet()) {
               var2.append("TransportLayerSecurityKeyPassPhrase");
               var2.append(String.valueOf(this.bean.getTransportLayerSecurityKeyPassPhrase()));
            }

            if (this.bean.isTransportLayerSecurityKeyPassPhraseEncryptedSet()) {
               var2.append("TransportLayerSecurityKeyPassPhraseEncrypted");
               var2.append(Arrays.toString(ArrayUtils.copyAndSort(this.bean.getTransportLayerSecurityKeyPassPhraseEncrypted())));
            }

            if (this.bean.isForceAuthnSet()) {
               var2.append("ForceAuthn");
               var2.append(String.valueOf(this.bean.isForceAuthn()));
            }

            if (this.bean.isIdentityProviderArtifactBindingEnabledSet()) {
               var2.append("IdentityProviderArtifactBindingEnabled");
               var2.append(String.valueOf(this.bean.isIdentityProviderArtifactBindingEnabled()));
            }

            if (this.bean.isIdentityProviderEnabledSet()) {
               var2.append("IdentityProviderEnabled");
               var2.append(String.valueOf(this.bean.isIdentityProviderEnabled()));
            }

            if (this.bean.isIdentityProviderPOSTBindingEnabledSet()) {
               var2.append("IdentityProviderPOSTBindingEnabled");
               var2.append(String.valueOf(this.bean.isIdentityProviderPOSTBindingEnabled()));
            }

            if (this.bean.isIdentityProviderRedirectBindingEnabledSet()) {
               var2.append("IdentityProviderRedirectBindingEnabled");
               var2.append(String.valueOf(this.bean.isIdentityProviderRedirectBindingEnabled()));
            }

            if (this.bean.isPOSTOneUseCheckEnabledSet()) {
               var2.append("POSTOneUseCheckEnabled");
               var2.append(String.valueOf(this.bean.isPOSTOneUseCheckEnabled()));
            }

            if (this.bean.isPassiveSet()) {
               var2.append("Passive");
               var2.append(String.valueOf(this.bean.isPassive()));
            }

            if (this.bean.isRecipientCheckEnabledSet()) {
               var2.append("RecipientCheckEnabled");
               var2.append(String.valueOf(this.bean.isRecipientCheckEnabled()));
            }

            if (this.bean.isReplicatedCacheEnabledSet()) {
               var2.append("ReplicatedCacheEnabled");
               var2.append(String.valueOf(this.bean.isReplicatedCacheEnabled()));
            }

            if (this.bean.isServiceProviderArtifactBindingEnabledSet()) {
               var2.append("ServiceProviderArtifactBindingEnabled");
               var2.append(String.valueOf(this.bean.isServiceProviderArtifactBindingEnabled()));
            }

            if (this.bean.isServiceProviderEnabledSet()) {
               var2.append("ServiceProviderEnabled");
               var2.append(String.valueOf(this.bean.isServiceProviderEnabled()));
            }

            if (this.bean.isServiceProviderPOSTBindingEnabledSet()) {
               var2.append("ServiceProviderPOSTBindingEnabled");
               var2.append(String.valueOf(this.bean.isServiceProviderPOSTBindingEnabled()));
            }

            if (this.bean.isSignAuthnRequestsSet()) {
               var2.append("SignAuthnRequests");
               var2.append(String.valueOf(this.bean.isSignAuthnRequests()));
            }

            if (this.bean.isWantArtifactRequestsSignedSet()) {
               var2.append("WantArtifactRequestsSigned");
               var2.append(String.valueOf(this.bean.isWantArtifactRequestsSigned()));
            }

            if (this.bean.isWantAssertionsSignedSet()) {
               var2.append("WantAssertionsSigned");
               var2.append(String.valueOf(this.bean.isWantAssertionsSigned()));
            }

            if (this.bean.isWantAuthnRequestsSignedSet()) {
               var2.append("WantAuthnRequestsSigned");
               var2.append(String.valueOf(this.bean.isWantAuthnRequestsSigned()));
            }

            if (this.bean.isWantBasicAuthClientAuthenticationSet()) {
               var2.append("WantBasicAuthClientAuthentication");
               var2.append(String.valueOf(this.bean.isWantBasicAuthClientAuthentication()));
            }

            if (this.bean.isWantTransportLayerSecurityClientAuthenticationSet()) {
               var2.append("WantTransportLayerSecurityClientAuthentication");
               var2.append(String.valueOf(this.bean.isWantTransportLayerSecurityClientAuthentication()));
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
            SingleSignOnServicesMBeanImpl var2 = (SingleSignOnServicesMBeanImpl)var1;
            this.computeDiff("ArtifactMaxCacheSize", this.bean.getArtifactMaxCacheSize(), var2.getArtifactMaxCacheSize(), true);
            this.computeDiff("ArtifactTimeout", this.bean.getArtifactTimeout(), var2.getArtifactTimeout(), true);
            this.computeDiff("AuthnRequestMaxCacheSize", this.bean.getAuthnRequestMaxCacheSize(), var2.getAuthnRequestMaxCacheSize(), true);
            this.computeDiff("AuthnRequestTimeout", this.bean.getAuthnRequestTimeout(), var2.getAuthnRequestTimeout(), true);
            this.computeDiff("BasicAuthPasswordEncrypted", this.bean.getBasicAuthPasswordEncrypted(), var2.getBasicAuthPasswordEncrypted(), true);
            this.computeDiff("BasicAuthUsername", this.bean.getBasicAuthUsername(), var2.getBasicAuthUsername(), true);
            this.computeDiff("ContactPersonCompany", this.bean.getContactPersonCompany(), var2.getContactPersonCompany(), true);
            this.computeDiff("ContactPersonEmailAddress", this.bean.getContactPersonEmailAddress(), var2.getContactPersonEmailAddress(), true);
            this.computeDiff("ContactPersonGivenName", this.bean.getContactPersonGivenName(), var2.getContactPersonGivenName(), true);
            this.computeDiff("ContactPersonSurName", this.bean.getContactPersonSurName(), var2.getContactPersonSurName(), true);
            this.computeDiff("ContactPersonTelephoneNumber", this.bean.getContactPersonTelephoneNumber(), var2.getContactPersonTelephoneNumber(), true);
            this.computeDiff("ContactPersonType", this.bean.getContactPersonType(), var2.getContactPersonType(), true);
            this.computeDiff("DefaultURL", this.bean.getDefaultURL(), var2.getDefaultURL(), true);
            this.computeDiff("EntityID", this.bean.getEntityID(), var2.getEntityID(), true);
            this.computeDiff("ErrorPath", this.bean.getErrorPath(), var2.getErrorPath(), true);
            this.computeDiff("IdentityProviderPreferredBinding", this.bean.getIdentityProviderPreferredBinding(), var2.getIdentityProviderPreferredBinding(), true);
            this.computeDiff("LoginReturnQueryParameter", this.bean.getLoginReturnQueryParameter(), var2.getLoginReturnQueryParameter(), true);
            this.computeDiff("LoginURL", this.bean.getLoginURL(), var2.getLoginURL(), true);
            this.computeDiff("OrganizationName", this.bean.getOrganizationName(), var2.getOrganizationName(), true);
            this.computeDiff("OrganizationURL", this.bean.getOrganizationURL(), var2.getOrganizationURL(), true);
            this.computeDiff("PublishedSiteURL", this.bean.getPublishedSiteURL(), var2.getPublishedSiteURL(), true);
            this.computeDiff("SSOSigningKeyAlias", this.bean.getSSOSigningKeyAlias(), var2.getSSOSigningKeyAlias(), true);
            this.computeDiff("SSOSigningKeyPassPhraseEncrypted", this.bean.getSSOSigningKeyPassPhraseEncrypted(), var2.getSSOSigningKeyPassPhraseEncrypted(), true);
            this.computeDiff("ServiceProviderPreferredBinding", this.bean.getServiceProviderPreferredBinding(), var2.getServiceProviderPreferredBinding(), true);
            this.computeDiff("TransportLayerSecurityKeyAlias", this.bean.getTransportLayerSecurityKeyAlias(), var2.getTransportLayerSecurityKeyAlias(), true);
            this.computeDiff("TransportLayerSecurityKeyPassPhraseEncrypted", this.bean.getTransportLayerSecurityKeyPassPhraseEncrypted(), var2.getTransportLayerSecurityKeyPassPhraseEncrypted(), true);
            this.computeDiff("ForceAuthn", this.bean.isForceAuthn(), var2.isForceAuthn(), true);
            this.computeDiff("IdentityProviderArtifactBindingEnabled", this.bean.isIdentityProviderArtifactBindingEnabled(), var2.isIdentityProviderArtifactBindingEnabled(), true);
            this.computeDiff("IdentityProviderEnabled", this.bean.isIdentityProviderEnabled(), var2.isIdentityProviderEnabled(), true);
            this.computeDiff("IdentityProviderPOSTBindingEnabled", this.bean.isIdentityProviderPOSTBindingEnabled(), var2.isIdentityProviderPOSTBindingEnabled(), true);
            this.computeDiff("IdentityProviderRedirectBindingEnabled", this.bean.isIdentityProviderRedirectBindingEnabled(), var2.isIdentityProviderRedirectBindingEnabled(), true);
            this.computeDiff("POSTOneUseCheckEnabled", this.bean.isPOSTOneUseCheckEnabled(), var2.isPOSTOneUseCheckEnabled(), true);
            this.computeDiff("Passive", this.bean.isPassive(), var2.isPassive(), true);
            this.computeDiff("RecipientCheckEnabled", this.bean.isRecipientCheckEnabled(), var2.isRecipientCheckEnabled(), true);
            this.computeDiff("ReplicatedCacheEnabled", this.bean.isReplicatedCacheEnabled(), var2.isReplicatedCacheEnabled(), false);
            this.computeDiff("ServiceProviderArtifactBindingEnabled", this.bean.isServiceProviderArtifactBindingEnabled(), var2.isServiceProviderArtifactBindingEnabled(), true);
            this.computeDiff("ServiceProviderEnabled", this.bean.isServiceProviderEnabled(), var2.isServiceProviderEnabled(), true);
            this.computeDiff("ServiceProviderPOSTBindingEnabled", this.bean.isServiceProviderPOSTBindingEnabled(), var2.isServiceProviderPOSTBindingEnabled(), true);
            this.computeDiff("SignAuthnRequests", this.bean.isSignAuthnRequests(), var2.isSignAuthnRequests(), true);
            this.computeDiff("WantArtifactRequestsSigned", this.bean.isWantArtifactRequestsSigned(), var2.isWantArtifactRequestsSigned(), true);
            this.computeDiff("WantAssertionsSigned", this.bean.isWantAssertionsSigned(), var2.isWantAssertionsSigned(), true);
            this.computeDiff("WantAuthnRequestsSigned", this.bean.isWantAuthnRequestsSigned(), var2.isWantAuthnRequestsSigned(), true);
            this.computeDiff("WantBasicAuthClientAuthentication", this.bean.isWantBasicAuthClientAuthentication(), var2.isWantBasicAuthClientAuthentication(), true);
            this.computeDiff("WantTransportLayerSecurityClientAuthentication", this.bean.isWantTransportLayerSecurityClientAuthentication(), var2.isWantTransportLayerSecurityClientAuthentication(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            SingleSignOnServicesMBeanImpl var3 = (SingleSignOnServicesMBeanImpl)var1.getSourceBean();
            SingleSignOnServicesMBeanImpl var4 = (SingleSignOnServicesMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("ArtifactMaxCacheSize")) {
                  var3.setArtifactMaxCacheSize(var4.getArtifactMaxCacheSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 51);
               } else if (var5.equals("ArtifactTimeout")) {
                  var3.setArtifactTimeout(var4.getArtifactTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 52);
               } else if (var5.equals("AuthnRequestMaxCacheSize")) {
                  var3.setAuthnRequestMaxCacheSize(var4.getAuthnRequestMaxCacheSize());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 49);
               } else if (var5.equals("AuthnRequestTimeout")) {
                  var3.setAuthnRequestTimeout(var4.getAuthnRequestTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 50);
               } else if (!var5.equals("BasicAuthPassword")) {
                  if (var5.equals("BasicAuthPasswordEncrypted")) {
                     var3.setBasicAuthPasswordEncrypted(var4.getBasicAuthPasswordEncrypted());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 45);
                  } else if (var5.equals("BasicAuthUsername")) {
                     var3.setBasicAuthUsername(var4.getBasicAuthUsername());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 43);
                  } else if (var5.equals("ContactPersonCompany")) {
                     var3.setContactPersonCompany(var4.getContactPersonCompany());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 10);
                  } else if (var5.equals("ContactPersonEmailAddress")) {
                     var3.setContactPersonEmailAddress(var4.getContactPersonEmailAddress());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 12);
                  } else if (var5.equals("ContactPersonGivenName")) {
                     var3.setContactPersonGivenName(var4.getContactPersonGivenName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 7);
                  } else if (var5.equals("ContactPersonSurName")) {
                     var3.setContactPersonSurName(var4.getContactPersonSurName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 8);
                  } else if (var5.equals("ContactPersonTelephoneNumber")) {
                     var3.setContactPersonTelephoneNumber(var4.getContactPersonTelephoneNumber());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 11);
                  } else if (var5.equals("ContactPersonType")) {
                     var3.setContactPersonType(var4.getContactPersonType());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 9);
                  } else if (var5.equals("DefaultURL")) {
                     var3.setDefaultURL(var4.getDefaultURL());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 19);
                  } else if (var5.equals("EntityID")) {
                     var3.setEntityID(var4.getEntityID());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 16);
                  } else if (var5.equals("ErrorPath")) {
                     var3.setErrorPath(var4.getErrorPath());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 17);
                  } else if (var5.equals("IdentityProviderPreferredBinding")) {
                     var3.setIdentityProviderPreferredBinding(var4.getIdentityProviderPreferredBinding());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 34);
                  } else if (var5.equals("LoginReturnQueryParameter")) {
                     var3.setLoginReturnQueryParameter(var4.getLoginReturnQueryParameter());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 36);
                  } else if (var5.equals("LoginURL")) {
                     var3.setLoginURL(var4.getLoginURL());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 35);
                  } else if (var5.equals("OrganizationName")) {
                     var3.setOrganizationName(var4.getOrganizationName());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 13);
                  } else if (var5.equals("OrganizationURL")) {
                     var3.setOrganizationURL(var4.getOrganizationURL());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 14);
                  } else if (var5.equals("PublishedSiteURL")) {
                     var3.setPublishedSiteURL(var4.getPublishedSiteURL());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 15);
                  } else if (var5.equals("SSOSigningKeyAlias")) {
                     var3.setSSOSigningKeyAlias(var4.getSSOSigningKeyAlias());
                     var3._conditionalUnset(var2.isUnsetUpdate(), 25);
                  } else if (!var5.equals("SSOSigningKeyPassPhrase")) {
                     if (var5.equals("SSOSigningKeyPassPhraseEncrypted")) {
                        var3.setSSOSigningKeyPassPhraseEncrypted(var4.getSSOSigningKeyPassPhraseEncrypted());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 27);
                     } else if (var5.equals("ServiceProviderPreferredBinding")) {
                        var3.setServiceProviderPreferredBinding(var4.getServiceProviderPreferredBinding());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 22);
                     } else if (var5.equals("TransportLayerSecurityKeyAlias")) {
                        var3.setTransportLayerSecurityKeyAlias(var4.getTransportLayerSecurityKeyAlias());
                        var3._conditionalUnset(var2.isUnsetUpdate(), 40);
                     } else if (!var5.equals("TransportLayerSecurityKeyPassPhrase")) {
                        if (var5.equals("TransportLayerSecurityKeyPassPhraseEncrypted")) {
                           var3.setTransportLayerSecurityKeyPassPhraseEncrypted(var4.getTransportLayerSecurityKeyPassPhraseEncrypted());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 42);
                        } else if (var5.equals("ForceAuthn")) {
                           var3.setForceAuthn(var4.isForceAuthn());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 28);
                        } else if (var5.equals("IdentityProviderArtifactBindingEnabled")) {
                           var3.setIdentityProviderArtifactBindingEnabled(var4.isIdentityProviderArtifactBindingEnabled());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 31);
                        } else if (var5.equals("IdentityProviderEnabled")) {
                           var3.setIdentityProviderEnabled(var4.isIdentityProviderEnabled());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 30);
                        } else if (var5.equals("IdentityProviderPOSTBindingEnabled")) {
                           var3.setIdentityProviderPOSTBindingEnabled(var4.isIdentityProviderPOSTBindingEnabled());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 32);
                        } else if (var5.equals("IdentityProviderRedirectBindingEnabled")) {
                           var3.setIdentityProviderRedirectBindingEnabled(var4.isIdentityProviderRedirectBindingEnabled());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 33);
                        } else if (var5.equals("POSTOneUseCheckEnabled")) {
                           var3.setPOSTOneUseCheckEnabled(var4.isPOSTOneUseCheckEnabled());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 39);
                        } else if (var5.equals("Passive")) {
                           var3.setPassive(var4.isPassive());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 29);
                        } else if (var5.equals("RecipientCheckEnabled")) {
                           var3.setRecipientCheckEnabled(var4.isRecipientCheckEnabled());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 38);
                        } else if (var5.equals("ReplicatedCacheEnabled")) {
                           var3.setReplicatedCacheEnabled(var4.isReplicatedCacheEnabled());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 53);
                        } else if (var5.equals("ServiceProviderArtifactBindingEnabled")) {
                           var3.setServiceProviderArtifactBindingEnabled(var4.isServiceProviderArtifactBindingEnabled());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 20);
                        } else if (var5.equals("ServiceProviderEnabled")) {
                           var3.setServiceProviderEnabled(var4.isServiceProviderEnabled());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 18);
                        } else if (var5.equals("ServiceProviderPOSTBindingEnabled")) {
                           var3.setServiceProviderPOSTBindingEnabled(var4.isServiceProviderPOSTBindingEnabled());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 21);
                        } else if (var5.equals("SignAuthnRequests")) {
                           var3.setSignAuthnRequests(var4.isSignAuthnRequests());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 23);
                        } else if (var5.equals("WantArtifactRequestsSigned")) {
                           var3.setWantArtifactRequestsSigned(var4.isWantArtifactRequestsSigned());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 46);
                        } else if (var5.equals("WantAssertionsSigned")) {
                           var3.setWantAssertionsSigned(var4.isWantAssertionsSigned());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 24);
                        } else if (var5.equals("WantAuthnRequestsSigned")) {
                           var3.setWantAuthnRequestsSigned(var4.isWantAuthnRequestsSigned());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 37);
                        } else if (var5.equals("WantBasicAuthClientAuthentication")) {
                           var3.setWantBasicAuthClientAuthentication(var4.isWantBasicAuthClientAuthentication());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 48);
                        } else if (var5.equals("WantTransportLayerSecurityClientAuthentication")) {
                           var3.setWantTransportLayerSecurityClientAuthentication(var4.isWantTransportLayerSecurityClientAuthentication());
                           var3._conditionalUnset(var2.isUnsetUpdate(), 47);
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
            SingleSignOnServicesMBeanImpl var5 = (SingleSignOnServicesMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("ArtifactMaxCacheSize")) && this.bean.isArtifactMaxCacheSizeSet()) {
               var5.setArtifactMaxCacheSize(this.bean.getArtifactMaxCacheSize());
            }

            if ((var3 == null || !var3.contains("ArtifactTimeout")) && this.bean.isArtifactTimeoutSet()) {
               var5.setArtifactTimeout(this.bean.getArtifactTimeout());
            }

            if ((var3 == null || !var3.contains("AuthnRequestMaxCacheSize")) && this.bean.isAuthnRequestMaxCacheSizeSet()) {
               var5.setAuthnRequestMaxCacheSize(this.bean.getAuthnRequestMaxCacheSize());
            }

            if ((var3 == null || !var3.contains("AuthnRequestTimeout")) && this.bean.isAuthnRequestTimeoutSet()) {
               var5.setAuthnRequestTimeout(this.bean.getAuthnRequestTimeout());
            }

            byte[] var4;
            if ((var3 == null || !var3.contains("BasicAuthPasswordEncrypted")) && this.bean.isBasicAuthPasswordEncryptedSet()) {
               var4 = this.bean.getBasicAuthPasswordEncrypted();
               var5.setBasicAuthPasswordEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("BasicAuthUsername")) && this.bean.isBasicAuthUsernameSet()) {
               var5.setBasicAuthUsername(this.bean.getBasicAuthUsername());
            }

            if ((var3 == null || !var3.contains("ContactPersonCompany")) && this.bean.isContactPersonCompanySet()) {
               var5.setContactPersonCompany(this.bean.getContactPersonCompany());
            }

            if ((var3 == null || !var3.contains("ContactPersonEmailAddress")) && this.bean.isContactPersonEmailAddressSet()) {
               var5.setContactPersonEmailAddress(this.bean.getContactPersonEmailAddress());
            }

            if ((var3 == null || !var3.contains("ContactPersonGivenName")) && this.bean.isContactPersonGivenNameSet()) {
               var5.setContactPersonGivenName(this.bean.getContactPersonGivenName());
            }

            if ((var3 == null || !var3.contains("ContactPersonSurName")) && this.bean.isContactPersonSurNameSet()) {
               var5.setContactPersonSurName(this.bean.getContactPersonSurName());
            }

            if ((var3 == null || !var3.contains("ContactPersonTelephoneNumber")) && this.bean.isContactPersonTelephoneNumberSet()) {
               var5.setContactPersonTelephoneNumber(this.bean.getContactPersonTelephoneNumber());
            }

            if ((var3 == null || !var3.contains("ContactPersonType")) && this.bean.isContactPersonTypeSet()) {
               var5.setContactPersonType(this.bean.getContactPersonType());
            }

            if ((var3 == null || !var3.contains("DefaultURL")) && this.bean.isDefaultURLSet()) {
               var5.setDefaultURL(this.bean.getDefaultURL());
            }

            if ((var3 == null || !var3.contains("EntityID")) && this.bean.isEntityIDSet()) {
               var5.setEntityID(this.bean.getEntityID());
            }

            if ((var3 == null || !var3.contains("ErrorPath")) && this.bean.isErrorPathSet()) {
               var5.setErrorPath(this.bean.getErrorPath());
            }

            if ((var3 == null || !var3.contains("IdentityProviderPreferredBinding")) && this.bean.isIdentityProviderPreferredBindingSet()) {
               var5.setIdentityProviderPreferredBinding(this.bean.getIdentityProviderPreferredBinding());
            }

            if ((var3 == null || !var3.contains("LoginReturnQueryParameter")) && this.bean.isLoginReturnQueryParameterSet()) {
               var5.setLoginReturnQueryParameter(this.bean.getLoginReturnQueryParameter());
            }

            if ((var3 == null || !var3.contains("LoginURL")) && this.bean.isLoginURLSet()) {
               var5.setLoginURL(this.bean.getLoginURL());
            }

            if ((var3 == null || !var3.contains("OrganizationName")) && this.bean.isOrganizationNameSet()) {
               var5.setOrganizationName(this.bean.getOrganizationName());
            }

            if ((var3 == null || !var3.contains("OrganizationURL")) && this.bean.isOrganizationURLSet()) {
               var5.setOrganizationURL(this.bean.getOrganizationURL());
            }

            if ((var3 == null || !var3.contains("PublishedSiteURL")) && this.bean.isPublishedSiteURLSet()) {
               var5.setPublishedSiteURL(this.bean.getPublishedSiteURL());
            }

            if ((var3 == null || !var3.contains("SSOSigningKeyAlias")) && this.bean.isSSOSigningKeyAliasSet()) {
               var5.setSSOSigningKeyAlias(this.bean.getSSOSigningKeyAlias());
            }

            if ((var3 == null || !var3.contains("SSOSigningKeyPassPhraseEncrypted")) && this.bean.isSSOSigningKeyPassPhraseEncryptedSet()) {
               var4 = this.bean.getSSOSigningKeyPassPhraseEncrypted();
               var5.setSSOSigningKeyPassPhraseEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("ServiceProviderPreferredBinding")) && this.bean.isServiceProviderPreferredBindingSet()) {
               var5.setServiceProviderPreferredBinding(this.bean.getServiceProviderPreferredBinding());
            }

            if ((var3 == null || !var3.contains("TransportLayerSecurityKeyAlias")) && this.bean.isTransportLayerSecurityKeyAliasSet()) {
               var5.setTransportLayerSecurityKeyAlias(this.bean.getTransportLayerSecurityKeyAlias());
            }

            if ((var3 == null || !var3.contains("TransportLayerSecurityKeyPassPhraseEncrypted")) && this.bean.isTransportLayerSecurityKeyPassPhraseEncryptedSet()) {
               var4 = this.bean.getTransportLayerSecurityKeyPassPhraseEncrypted();
               var5.setTransportLayerSecurityKeyPassPhraseEncrypted(var4 == null ? null : (byte[])((byte[])((byte[])((byte[])var4)).clone()));
            }

            if ((var3 == null || !var3.contains("ForceAuthn")) && this.bean.isForceAuthnSet()) {
               var5.setForceAuthn(this.bean.isForceAuthn());
            }

            if ((var3 == null || !var3.contains("IdentityProviderArtifactBindingEnabled")) && this.bean.isIdentityProviderArtifactBindingEnabledSet()) {
               var5.setIdentityProviderArtifactBindingEnabled(this.bean.isIdentityProviderArtifactBindingEnabled());
            }

            if ((var3 == null || !var3.contains("IdentityProviderEnabled")) && this.bean.isIdentityProviderEnabledSet()) {
               var5.setIdentityProviderEnabled(this.bean.isIdentityProviderEnabled());
            }

            if ((var3 == null || !var3.contains("IdentityProviderPOSTBindingEnabled")) && this.bean.isIdentityProviderPOSTBindingEnabledSet()) {
               var5.setIdentityProviderPOSTBindingEnabled(this.bean.isIdentityProviderPOSTBindingEnabled());
            }

            if ((var3 == null || !var3.contains("IdentityProviderRedirectBindingEnabled")) && this.bean.isIdentityProviderRedirectBindingEnabledSet()) {
               var5.setIdentityProviderRedirectBindingEnabled(this.bean.isIdentityProviderRedirectBindingEnabled());
            }

            if ((var3 == null || !var3.contains("POSTOneUseCheckEnabled")) && this.bean.isPOSTOneUseCheckEnabledSet()) {
               var5.setPOSTOneUseCheckEnabled(this.bean.isPOSTOneUseCheckEnabled());
            }

            if ((var3 == null || !var3.contains("Passive")) && this.bean.isPassiveSet()) {
               var5.setPassive(this.bean.isPassive());
            }

            if ((var3 == null || !var3.contains("RecipientCheckEnabled")) && this.bean.isRecipientCheckEnabledSet()) {
               var5.setRecipientCheckEnabled(this.bean.isRecipientCheckEnabled());
            }

            if ((var3 == null || !var3.contains("ReplicatedCacheEnabled")) && this.bean.isReplicatedCacheEnabledSet()) {
               var5.setReplicatedCacheEnabled(this.bean.isReplicatedCacheEnabled());
            }

            if ((var3 == null || !var3.contains("ServiceProviderArtifactBindingEnabled")) && this.bean.isServiceProviderArtifactBindingEnabledSet()) {
               var5.setServiceProviderArtifactBindingEnabled(this.bean.isServiceProviderArtifactBindingEnabled());
            }

            if ((var3 == null || !var3.contains("ServiceProviderEnabled")) && this.bean.isServiceProviderEnabledSet()) {
               var5.setServiceProviderEnabled(this.bean.isServiceProviderEnabled());
            }

            if ((var3 == null || !var3.contains("ServiceProviderPOSTBindingEnabled")) && this.bean.isServiceProviderPOSTBindingEnabledSet()) {
               var5.setServiceProviderPOSTBindingEnabled(this.bean.isServiceProviderPOSTBindingEnabled());
            }

            if ((var3 == null || !var3.contains("SignAuthnRequests")) && this.bean.isSignAuthnRequestsSet()) {
               var5.setSignAuthnRequests(this.bean.isSignAuthnRequests());
            }

            if ((var3 == null || !var3.contains("WantArtifactRequestsSigned")) && this.bean.isWantArtifactRequestsSignedSet()) {
               var5.setWantArtifactRequestsSigned(this.bean.isWantArtifactRequestsSigned());
            }

            if ((var3 == null || !var3.contains("WantAssertionsSigned")) && this.bean.isWantAssertionsSignedSet()) {
               var5.setWantAssertionsSigned(this.bean.isWantAssertionsSigned());
            }

            if ((var3 == null || !var3.contains("WantAuthnRequestsSigned")) && this.bean.isWantAuthnRequestsSignedSet()) {
               var5.setWantAuthnRequestsSigned(this.bean.isWantAuthnRequestsSigned());
            }

            if ((var3 == null || !var3.contains("WantBasicAuthClientAuthentication")) && this.bean.isWantBasicAuthClientAuthenticationSet()) {
               var5.setWantBasicAuthClientAuthentication(this.bean.isWantBasicAuthClientAuthentication());
            }

            if ((var3 == null || !var3.contains("WantTransportLayerSecurityClientAuthentication")) && this.bean.isWantTransportLayerSecurityClientAuthenticationSet()) {
               var5.setWantTransportLayerSecurityClientAuthentication(this.bean.isWantTransportLayerSecurityClientAuthentication());
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
