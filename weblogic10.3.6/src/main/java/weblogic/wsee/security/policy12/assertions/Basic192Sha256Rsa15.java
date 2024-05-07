package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class Basic192Sha256Rsa15 extends QNameAssertion {
   public static final String BASIC_192_SHA_256_RSA_15 = "Basic192Sha256Rsa15";

   public QName getName() {
      return new QName(this.getNamespace(), "Basic192Sha256Rsa15", "sp");
   }
}
