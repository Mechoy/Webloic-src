package weblogic.wsee.security.wss.policy.wssp;

import weblogic.wsee.policy.util.PolicySelectionPreference;
import weblogic.wsee.security.wss.policy.GeneralPolicy;
import weblogic.wsee.security.wssp.HttpsTokenAssertion;
import weblogic.wsee.security.wssp.WsTrustOptions;
import weblogic.wsee.security.wssp.Wss10Options;
import weblogic.wsee.security.wssp.Wss11Options;
import weblogic.wsee.util.Verbose;

public class GeneralPolicyImpl implements GeneralPolicy {
   private static final boolean verbose = Verbose.isVerbose(GeneralPolicyImpl.class);
   private static final boolean debug = false;
   private String layout = null;
   private boolean isWss11 = false;
   private boolean isCompatMSFT = false;
   private Wss11Options wss11OptionsAssertion = null;
   private Wss10Options wss10OptionsAssertion = null;
   private WsTrustOptions trustOptions = null;
   private boolean encryptBeforeSigning = false;
   private String[] signatureValues;
   private boolean requireSignatureConfirmation = false;
   private int mustSupportedTypeCode = 0;
   private PolicySelectionPreference preference;
   private boolean optionalSignatureConfirmation = false;
   private HttpsTokenAssertion httpsAssertion = null;

   public int getMustSupportedTypeCode() {
      return this.mustSupportedTypeCode;
   }

   public String getLayout() {
      return null == this.layout ? "Lax" : this.layout;
   }

   public void setLayout(String var1) {
      this.layout = var1;
   }

   public void setLayoutToStrict() {
      this.layout = "Strict";
   }

   public void setLayoutToLaxTimestampFirst() {
      this.layout = "LaxTimestampFirst";
   }

   public void setLayoutToLaxTimestampLast() {
      this.layout = "LaxTimestampLast";
   }

   public void setLayoutToLax() {
      this.layout = "Lax";
   }

   public boolean isWss11() {
      return this.isWss11;
   }

   public void setWss11On() {
      this.isWss11 = true;
   }

   public void setWss11OptionsAssertion(Wss11Options var1) {
      this.wss11OptionsAssertion = var1;
      this.setMustSupportWS10Options(var1);
      if (var1.isMustSupportThumbprintReference()) {
         if (var1.isMustSupportThumbprintReferenceOptional() && !this.preference.isInteropFirst()) {
            if (verbose) {
               Verbose.say("Skip the MustSupportThumbprintReference assertion due to it is optional and interop is not a preference");
            }
         } else {
            this.setMustSupportRefThumbprint();
         }
      }

      if (var1.isSignatureConfirmationRequired()) {
         this.setRequireSignatureConfirmation(true);
         if (var1.isSignatureConfirmationRequiredOptional()) {
            this.setOptionalSignatureConfirmation(true);
         }
      } else {
         this.setRequireSignatureConfirmation(false);
      }

   }

   private void setMustSupportWS10Options(Wss10Options var1) {
      if (var1.isMustSupportEmbeddedTokenReference()) {
         this.setMustSupportRefEmbeddedToken();
      }

      if (var1.isMustSupportExternalUriReference()) {
         this.setMustSupportRefExternalURI();
      }

      if (var1.isMustSupportIssuerSerialReference()) {
         this.setMustSupportRefIssuerSerial();
      }

      if (var1.isMustSupportKeyIdentiferReference()) {
         this.setMustSupportRefKeyIdentifier();
      }

   }

   public void setWss10OptionsAssertion(Wss10Options var1) {
      this.wss10OptionsAssertion = var1;
      this.setMustSupportWS10Options(var1);
   }

   public boolean isRequireSignatureConfirmation() {
      return this.requireSignatureConfirmation;
   }

   public void setRequireSignatureConfirmation(boolean var1) {
      this.requireSignatureConfirmation = var1;
   }

   public String[] getSignatureValues() {
      if (null == this.signatureValues && this.isRequireSignatureConfirmation()) {
         this.signatureValues = new String[0];
      }

      return this.signatureValues;
   }

   public void setSignatureValues(String[] var1) {
      this.signatureValues = var1;
   }

   public void setMustSupportRefThumbprint() {
      this.mustSupportedTypeCode |= 1;
   }

   public void setMustSupportRefKeyIdentifier() {
      this.mustSupportedTypeCode |= 2;
   }

   public void setMustSupportRefIssuerSerial() {
      this.mustSupportedTypeCode |= 4;
   }

   public void setMustSupportRefExternalURI() {
      this.mustSupportedTypeCode |= 16;
   }

   public void setMustSupportRefEmbeddedToken() {
      this.mustSupportedTypeCode |= 8;
   }

   public boolean isEncryptBeforeSigning() {
      return this.encryptBeforeSigning;
   }

   public void setEncryptBeforeSigning(boolean var1) {
      this.encryptBeforeSigning = var1;
   }

   public WsTrustOptions getTrustOptions() {
      return this.trustOptions;
   }

   public void setTrustOptions(WsTrustOptions var1) {
      this.trustOptions = var1;
   }

   public boolean hasTrustOptions() {
      return this.trustOptions != null;
   }

   public boolean isWssc13() {
      return !this.hasTrustOptions() ? false : this.trustOptions.isWst13();
   }

   public boolean isCompatMSFT() {
      return this.isCompatMSFT;
   }

   public void setCompatMSFT(boolean var1) {
      this.isCompatMSFT = var1;
   }

   public PolicySelectionPreference getPreference() {
      return this.preference;
   }

   public void setPreference(PolicySelectionPreference var1) {
      this.preference = var1;
   }

   public boolean isOptionalSignatureConfirmation() {
      return this.optionalSignatureConfirmation;
   }

   public void setOptionalSignatureConfirmation(boolean var1) {
      this.optionalSignatureConfirmation = var1;
   }

   public void setHttpsAssertion(HttpsTokenAssertion var1) {
      this.httpsAssertion = var1;
   }

   public boolean isHTTPsRequired() {
      return this.httpsAssertion != null;
   }

   public boolean isClientCertificateRequired() {
      return !this.isHTTPsRequired() ? false : this.httpsAssertion.isClientCertificateRequired();
   }

   public boolean isHttpBasicAuthenticationRequired() {
      return !this.isHTTPsRequired() ? false : this.httpsAssertion.isHttpBasicAuthenticationRequired();
   }
}
