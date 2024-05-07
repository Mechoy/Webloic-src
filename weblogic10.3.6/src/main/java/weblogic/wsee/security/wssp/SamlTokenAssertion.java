package weblogic.wsee.security.wssp;

public interface SamlTokenAssertion extends TokenAssertion {
   boolean isKeyIdentifierReferenceRequired();

   TokenType getSamlTokenType();

   ConfirmationMethod getSubjectConfirmationMethod();

   public static enum ConfirmationMethod {
      BEARER,
      SENDER_VOUCHES,
      HOLDER_OF_KEY;
   }

   public static enum TokenType {
      WSS_SAML_V11_TOKEN_10,
      WSS_SAML_V11_TOKEN_11,
      WSS_SAML_V20_TOKEN_11;
   }
}
