package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class Basic256Sha256 extends QNameAssertion {
   public static final String BASIC_256_SHA_256 = "Basic256Sha256";

   public QName getName() {
      return new QName(this.getNamespace(), "Basic256Sha256", "sp");
   }
}
