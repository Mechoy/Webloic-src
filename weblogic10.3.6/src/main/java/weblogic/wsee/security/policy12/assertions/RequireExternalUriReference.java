package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class RequireExternalUriReference extends QNameAssertion {
   public static final String REQUIRE_EXTERNAL_URI_REFERENCE = "RequireExternalUriReference";

   public QName getName() {
      return new QName(this.getNamespace(), "RequireExternalUriReference", "sp");
   }
}
