package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class TripleDesSha256 extends QNameAssertion {
   public static final String TRIPLE_DES_SHA_256 = "TripleDesSha256";

   public QName getName() {
      return new QName(this.getNamespace(), "TripleDesSha256", "sp");
   }
}
