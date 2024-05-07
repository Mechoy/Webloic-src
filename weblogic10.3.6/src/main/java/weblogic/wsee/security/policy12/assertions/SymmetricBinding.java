package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class SymmetricBinding extends SecurityBinding {
   public static final String SYMMETRIC_BINDING = "SymmetricBinding";

   public QName getName() {
      return new QName(this.getNamespace(), "SymmetricBinding", "sp");
   }

   public ProtectionToken getProtectionToken() {
      return (ProtectionToken)this.getNestedAssertion(ProtectionToken.class);
   }

   public EncryptionToken getEncryptionToken() {
      return (EncryptionToken)this.getNestedAssertion(EncryptionToken.class);
   }

   public SignatureToken getSignatureToken() {
      return (SignatureToken)this.getNestedAssertion(SignatureToken.class);
   }
}
