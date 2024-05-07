package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class AsymmetricBinding extends SecurityBinding {
   public static final String ASYMMETRIC_BINDING = "AsymmetricBinding";

   public QName getName() {
      return new QName(this.getNamespace(), "AsymmetricBinding", "sp");
   }

   public InitiatorToken getInitiatorToken() {
      return (InitiatorToken)this.getNestedAssertion(InitiatorToken.class);
   }

   public InitiatorSignatureToken getInitiatorSignatureToken() {
      return (InitiatorSignatureToken)this.getNestedAssertion(InitiatorSignatureToken.class);
   }

   public InitiatorEncryptionToken getInitiatorEncryptionToken() {
      return (InitiatorEncryptionToken)this.getNestedAssertion(InitiatorEncryptionToken.class);
   }

   public RecipientToken getRecipientToken() {
      return (RecipientToken)this.getNestedAssertion(RecipientToken.class);
   }

   public RecipientSignatureToken getRecipientSignatureToken() {
      return (RecipientSignatureToken)this.getNestedAssertion(RecipientSignatureToken.class);
   }

   public RecipientEncryptionToken getRecipientEncryptionToken() {
      return (RecipientEncryptionToken)this.getNestedAssertion(RecipientEncryptionToken.class);
   }
}
