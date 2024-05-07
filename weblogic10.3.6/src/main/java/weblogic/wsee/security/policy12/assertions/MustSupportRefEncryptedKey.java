package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class MustSupportRefEncryptedKey extends QNameAssertion {
   public static final String MUST_SUPPORT_ENCRYPTED_KEY_REF = "MustSupportRefEncryptedKey";

   public QName getName() {
      return new QName(this.getNamespace(), "MustSupportRefEncryptedKey", "sp");
   }
}
