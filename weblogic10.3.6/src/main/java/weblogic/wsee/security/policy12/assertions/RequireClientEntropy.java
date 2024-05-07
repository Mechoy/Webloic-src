package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class RequireClientEntropy extends QNameAssertion {
   public static final String REQUIRE_CLIENT_ENTROPY = "RequireClientEntropy";

   public QName getName() {
      return new QName(this.getNamespace(), "RequireClientEntropy", "sp");
   }
}
