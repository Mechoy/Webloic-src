package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class RequireExplicitDerivedKeys extends QNameAssertion {
   public static final String REQUIRE_EXPLICIT_DERIVED_KEYS = "RequireExplicitDerivedKeys";

   public QName getName() {
      return new QName(this.getNamespace(), "RequireExplicitDerivedKeys", "sp");
   }
}
