package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class RequireThumbprintReference extends QNameAssertion {
   public static final String REQUIRE_THUMBPRINT_REFERENCE = "RequireThumbprintReference";

   public QName getName() {
      return new QName(this.getNamespace(), "RequireThumbprintReference", "sp");
   }
}
