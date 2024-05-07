package weblogic.wsee.security.wssp;

public interface Wss11Options extends Wss10Options {
   boolean isMustSupportThumbprintReference();

   boolean isMustSupportEncryptedKeyReference();

   boolean isSignatureConfirmationRequired();

   boolean isMustSupportThumbprintReferenceOptional();

   boolean isMustSupportEncryptedKeyReferenceOptional();

   boolean isSignatureConfirmationRequiredOptional();
}
