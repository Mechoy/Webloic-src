package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class BootstrapPolicy extends NestedSecurityPolicy12Assertion {
   public static final String BOOTSTRAP_POLICY = "BootstrapPolicy";

   public QName getName() {
      return new QName(this.getNamespace(), "BootstrapPolicy", "sp");
   }

   public TransportBinding getTransportBinding() {
      return (TransportBinding)this.getNestedAssertion(TransportBinding.class);
   }

   public SymmetricBinding getSymmetricBinding() {
      return (SymmetricBinding)this.getNestedAssertion(SymmetricBinding.class);
   }

   public AsymmetricBinding getAsymmetricBinding() {
      return (AsymmetricBinding)this.getNestedAssertion(AsymmetricBinding.class);
   }

   public SignedParts getSignedParts() {
      return (SignedParts)this.getNestedAssertion(SignedParts.class);
   }

   public EncryptedParts getEncryptedParts() {
      return (EncryptedParts)this.getNestedAssertion(EncryptedParts.class);
   }

   public SignedElements getSignedElements() {
      return (SignedElements)this.getNestedAssertion(SignedElements.class);
   }

   public EncryptedElements getEncryptedElements() {
      return (EncryptedElements)this.getNestedAssertion(EncryptedElements.class);
   }

   public ContentEncryptedElements getContentEncryptedElements() {
      return (ContentEncryptedElements)this.getNestedAssertion(ContentEncryptedElements.class);
   }
}
