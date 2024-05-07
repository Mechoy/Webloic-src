package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class RequireEmbeddedTokenReference extends QNameAssertion {
   public static final String REQUIRE_EMBEDDED_TOKEN_REFERENCE = "RequireEmbeddedTokenReference";

   public QName getName() {
      return new QName(this.getNamespace(), "RequireEmbeddedTokenReference", "sp");
   }
}
