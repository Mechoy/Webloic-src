package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class SignedEncryptedSupportingTokens extends AbstractSupportingTokens {
   public static final String SIGNED_ENCRYPTED_SUPPORTING_TOKENS = "SignedEncryptedSupportingTokens";

   public QName getName() {
      return new QName(this.getNamespace(), "SignedEncryptedSupportingTokens", "sp");
   }
}
