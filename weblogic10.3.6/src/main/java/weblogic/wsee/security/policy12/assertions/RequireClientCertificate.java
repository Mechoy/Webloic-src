package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class RequireClientCertificate extends QNameAssertion {
   public static final String REQUIRE_CLIENT_CERTIFICATE = "RequireClientCertificate";

   public QName getName() {
      return new QName(this.getNamespace(), "RequireClientCertificate", "sp");
   }
}
