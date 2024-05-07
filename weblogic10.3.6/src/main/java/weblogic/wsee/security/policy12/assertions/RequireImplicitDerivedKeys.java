package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class RequireImplicitDerivedKeys extends QNameAssertion {
   public static final String REQUIRE_IMPLICIT_DERIVED_KEYS = "RequireImplicitDerivedKeys";

   public QName getName() {
      return new QName(this.getNamespace(), "RequireImplicitDerivedKeys", "sp");
   }
}
