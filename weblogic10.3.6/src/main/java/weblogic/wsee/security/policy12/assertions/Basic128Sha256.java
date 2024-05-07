package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class Basic128Sha256 extends QNameAssertion {
   public static final String BASIC_128_SHA_256 = "Basic128Sha256";

   public QName getName() {
      return new QName(this.getNamespace(), "Basic128Sha256", "sp");
   }
}
