package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class RequireServerEntropy extends QNameAssertion {
   public static final String REQUIRE_SERVER_ENTROPY = "RequireServerEntropy";

   public QName getName() {
      return new QName(this.getNamespace(), "RequireServerEntropy", "sp");
   }
}
