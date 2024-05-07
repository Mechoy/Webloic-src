package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class TripleDes extends QNameAssertion {
   public static final String TRIPLE_DES = "TripleDes";

   public QName getName() {
      return new QName(this.getNamespace(), "TripleDes", "sp");
   }
}
