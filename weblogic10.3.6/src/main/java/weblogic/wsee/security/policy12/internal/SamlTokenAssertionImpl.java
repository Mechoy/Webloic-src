package weblogic.wsee.security.policy12.internal;

import weblogic.wsee.security.policy12.assertions.SamlToken;
import weblogic.wsee.security.wssp.SamlTokenAssertion;

public class SamlTokenAssertionImpl extends TokenAssertionImpl implements SamlTokenAssertion {
   private boolean isKeyIdentifierReferenceRequired = false;
   private SamlTokenAssertion.TokenType tokenType;
   private SamlTokenAssertion.ConfirmationMethod confirmationMethod;

   SamlTokenAssertionImpl(SamlToken var1) {
      super(var1);
      this.tokenType = SamlTokenAssertion.TokenType.WSS_SAML_V11_TOKEN_10;
      this.confirmationMethod = SamlTokenAssertion.ConfirmationMethod.SENDER_VOUCHES;
      this.isKeyIdentifierReferenceRequired = var1.getRequireKeyIdentifierReference() != null;
      if (var1.getWssSamlV11Token11() != null) {
         this.tokenType = SamlTokenAssertion.TokenType.WSS_SAML_V11_TOKEN_11;
      } else if (var1.getWssSamlV20Token11() != null) {
         this.tokenType = SamlTokenAssertion.TokenType.WSS_SAML_V20_TOKEN_11;
      }

   }

   public boolean isKeyIdentifierReferenceRequired() {
      return this.isKeyIdentifierReferenceRequired;
   }

   public SamlTokenAssertion.TokenType getSamlTokenType() {
      return this.tokenType;
   }

   void setConfirmationMethodBearer() {
      this.confirmationMethod = SamlTokenAssertion.ConfirmationMethod.BEARER;
   }

   void setConfirmationMethodHolderOfKey() {
      this.confirmationMethod = SamlTokenAssertion.ConfirmationMethod.HOLDER_OF_KEY;
   }

   void setConfirmationMethodSenderVouches() {
      this.confirmationMethod = SamlTokenAssertion.ConfirmationMethod.SENDER_VOUCHES;
   }

   public void setSubjectConfirmationMethod(SamlTokenAssertion.ConfirmationMethod var1) {
      this.confirmationMethod = var1;
   }

   public SamlTokenAssertion.ConfirmationMethod getSubjectConfirmationMethod() {
      return this.confirmationMethod;
   }
}
