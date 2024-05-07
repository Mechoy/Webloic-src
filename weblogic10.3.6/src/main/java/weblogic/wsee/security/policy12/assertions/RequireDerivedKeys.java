package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class RequireDerivedKeys extends QNameAssertion {
   public static final String REQUIRE_DERIVED_KEYS = "RequireDerivedKeys";

   public QName getName() {
      return new QName(this.getNamespace(), "RequireDerivedKeys", "sp");
   }
}
