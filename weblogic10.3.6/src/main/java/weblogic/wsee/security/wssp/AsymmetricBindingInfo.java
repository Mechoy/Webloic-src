package weblogic.wsee.security.wssp;

public interface AsymmetricBindingInfo extends SecurityBindingPropertiesAssertion {
   InitiatorTokenAssertion getInitiatorTokenAssertion();

   RecipientTokenAssertion getRecipientTokenAssertion();

   InitiatorSignatureTokenAssertion getInitiatorSignatureTokenAssertion();

   InitiatorEncryptionTokenAssertion getInitiatorEncryptionTokenAssertion();

   RecipientSignatureTokenAssertion getRecipientSignatureTokenAssertion();

   RecipientEncryptionTokenAssertion getRecipientEncryptionTokenAssertion();
}
