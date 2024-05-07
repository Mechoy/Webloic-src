package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class Basic192Sha256 extends QNameAssertion {
   public static final String BASIC_192_SHA_256 = "Basic192Sha256";

   public QName getName() {
      return new QName(this.getNamespace(), "Basic192Sha256", "sp");
   }
}
