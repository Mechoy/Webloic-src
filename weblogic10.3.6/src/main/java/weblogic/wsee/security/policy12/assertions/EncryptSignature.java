package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class EncryptSignature extends QNameAssertion {
   public static final String ENCRYPT_SIGNATURE = "EncryptSignature";

   public QName getName() {
      return new QName(this.getNamespace(), "EncryptSignature", "sp");
   }
}
