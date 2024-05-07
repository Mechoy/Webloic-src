package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class EncryptBeforeSigning extends QNameAssertion {
   public static final String ENCRYPT_BEFORE_SIGNING = "EncryptBeforeSigning";

   public QName getName() {
      return new QName(this.getNamespace(), "EncryptBeforeSigning", "sp");
   }
}
