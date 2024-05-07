package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class EncryptedSupportingTokens extends AbstractSupportingTokens {
   public static final String ENCRYPTED_SUPPORTING_TOKENS = "EncryptedSupportingTokens";

   public QName getName() {
      return new QName(this.getNamespace(), "EncryptedSupportingTokens", "sp");
   }
}
