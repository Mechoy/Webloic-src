package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class TripleDesSha256Rsa15 extends QNameAssertion {
   public static final String TRIPLE_DES_SHA_256_RSA_15 = "TripleDesSha256Rsa15";

   public QName getName() {
      return new QName(this.getNamespace(), "TripleDesSha256Rsa15", "sp");
   }
}
