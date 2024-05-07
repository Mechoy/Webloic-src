package weblogic.wsee.security.wssp;

public interface SymmetricBindingInfo extends SecurityBindingPropertiesAssertion {
   SignatureTokenAssertion getSignatureTokenAssertion();

   EncryptionTokenAssertion getEncryptionTokenAssertion();

   ProtectionTokenAssertion getProtectionTokenAssertion();

   boolean isEncryptedKeyRequired();
}
