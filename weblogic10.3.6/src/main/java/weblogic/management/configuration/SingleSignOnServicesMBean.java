package weblogic.management.configuration;

import com.bea.common.security.saml2.SingleSignOnServicesConfigSpi;
import javax.management.InvalidAttributeValueException;

public interface SingleSignOnServicesMBean extends ConfigurationMBean, SingleSignOnServicesConfigSpi {
   String getContactPersonGivenName();

   void setContactPersonGivenName(String var1);

   String getContactPersonSurName();

   void setContactPersonSurName(String var1);

   String getContactPersonType();

   void setContactPersonType(String var1);

   String getContactPersonCompany();

   void setContactPersonCompany(String var1);

   String getContactPersonTelephoneNumber();

   void setContactPersonTelephoneNumber(String var1);

   String getContactPersonEmailAddress();

   void setContactPersonEmailAddress(String var1);

   String getOrganizationName();

   void setOrganizationName(String var1);

   String getOrganizationURL();

   void setOrganizationURL(String var1);

   String getPublishedSiteURL();

   void setPublishedSiteURL(String var1);

   String getEntityID();

   void setEntityID(String var1);

   String getErrorPath();

   void setErrorPath(String var1);

   boolean isServiceProviderEnabled();

   void setServiceProviderEnabled(boolean var1);

   String getDefaultURL();

   void setDefaultURL(String var1);

   boolean isServiceProviderArtifactBindingEnabled();

   void setServiceProviderArtifactBindingEnabled(boolean var1);

   boolean isServiceProviderPOSTBindingEnabled();

   void setServiceProviderPOSTBindingEnabled(boolean var1);

   String getServiceProviderPreferredBinding();

   void setServiceProviderPreferredBinding(String var1);

   boolean isSignAuthnRequests();

   void setSignAuthnRequests(boolean var1);

   boolean isWantAssertionsSigned();

   void setWantAssertionsSigned(boolean var1);

   String getSSOSigningKeyAlias();

   void setSSOSigningKeyAlias(String var1);

   String getSSOSigningKeyPassPhrase();

   void setSSOSigningKeyPassPhrase(String var1) throws InvalidAttributeValueException;

   byte[] getSSOSigningKeyPassPhraseEncrypted();

   void setSSOSigningKeyPassPhraseEncrypted(byte[] var1) throws InvalidAttributeValueException;

   boolean isForceAuthn();

   void setForceAuthn(boolean var1);

   boolean isPassive();

   void setPassive(boolean var1);

   boolean isIdentityProviderEnabled();

   void setIdentityProviderEnabled(boolean var1);

   boolean isIdentityProviderArtifactBindingEnabled();

   void setIdentityProviderArtifactBindingEnabled(boolean var1);

   boolean isIdentityProviderPOSTBindingEnabled();

   void setIdentityProviderPOSTBindingEnabled(boolean var1);

   boolean isIdentityProviderRedirectBindingEnabled();

   void setIdentityProviderRedirectBindingEnabled(boolean var1);

   String getIdentityProviderPreferredBinding();

   void setIdentityProviderPreferredBinding(String var1);

   String getLoginURL();

   void setLoginURL(String var1);

   String getLoginReturnQueryParameter();

   void setLoginReturnQueryParameter(String var1);

   boolean isWantAuthnRequestsSigned();

   void setWantAuthnRequestsSigned(boolean var1);

   boolean isRecipientCheckEnabled();

   void setRecipientCheckEnabled(boolean var1) throws InvalidAttributeValueException;

   boolean isPOSTOneUseCheckEnabled();

   void setPOSTOneUseCheckEnabled(boolean var1) throws InvalidAttributeValueException;

   String getTransportLayerSecurityKeyAlias();

   void setTransportLayerSecurityKeyAlias(String var1);

   String getTransportLayerSecurityKeyPassPhrase();

   void setTransportLayerSecurityKeyPassPhrase(String var1) throws InvalidAttributeValueException;

   byte[] getTransportLayerSecurityKeyPassPhraseEncrypted();

   void setTransportLayerSecurityKeyPassPhraseEncrypted(byte[] var1) throws InvalidAttributeValueException;

   String getBasicAuthUsername();

   void setBasicAuthUsername(String var1);

   String getBasicAuthPassword();

   void setBasicAuthPassword(String var1) throws InvalidAttributeValueException;

   byte[] getBasicAuthPasswordEncrypted();

   void setBasicAuthPasswordEncrypted(byte[] var1) throws InvalidAttributeValueException;

   boolean isWantArtifactRequestsSigned();

   void setWantArtifactRequestsSigned(boolean var1);

   boolean isWantTransportLayerSecurityClientAuthentication();

   void setWantTransportLayerSecurityClientAuthentication(boolean var1);

   boolean isWantBasicAuthClientAuthentication();

   void setWantBasicAuthClientAuthentication(boolean var1);

   int getAuthnRequestMaxCacheSize();

   void setAuthnRequestMaxCacheSize(int var1);

   int getAuthnRequestTimeout();

   void setAuthnRequestTimeout(int var1);

   int getArtifactMaxCacheSize();

   void setArtifactMaxCacheSize(int var1);

   int getArtifactTimeout();

   void setArtifactTimeout(int var1);

   boolean isReplicatedCacheEnabled();

   void setReplicatedCacheEnabled(boolean var1);
}
