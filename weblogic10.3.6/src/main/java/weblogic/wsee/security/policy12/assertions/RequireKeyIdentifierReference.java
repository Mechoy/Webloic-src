package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class RequireKeyIdentifierReference extends QNameAssertion {
   public static final String REQUIRE_KEY_IDENTIFIER_REFERENCE = "RequireKeyIdentifierReference";

   public QName getName() {
      return new QName(this.getNamespace(), "RequireKeyIdentifierReference", "sp");
   }
}
