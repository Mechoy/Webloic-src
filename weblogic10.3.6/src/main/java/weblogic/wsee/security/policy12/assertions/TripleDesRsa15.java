package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class TripleDesRsa15 extends QNameAssertion {
   public static final String TRIPLE_DES_RSA_15 = "TripleDesRsa15";

   public QName getName() {
      return new QName(this.getNamespace(), "TripleDesRsa15", "sp");
   }
}
