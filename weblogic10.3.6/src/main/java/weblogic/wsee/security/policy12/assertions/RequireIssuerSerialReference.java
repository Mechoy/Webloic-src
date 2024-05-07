package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class RequireIssuerSerialReference extends QNameAssertion {
   public static final String REQUIRE_ISSUER_SERIAL_REFERNCE = "RequireIssuerSerialReference";

   public QName getName() {
      return new QName(this.getNamespace(), "RequireIssuerSerialReference", "sp");
   }
}
