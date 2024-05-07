package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class RequireInternalReference extends QNameAssertion {
   private static final long serialVersionUID = -277479888753144701L;
   public static final String REQUIRE_INTERNAL_REFERENCE = "RequireInternalReference";

   public QName getName() {
      return new QName(this.getNamespace(), "RequireInternalReference", "sp");
   }
}
