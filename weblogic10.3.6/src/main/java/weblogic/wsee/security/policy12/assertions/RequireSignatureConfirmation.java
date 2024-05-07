package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class RequireSignatureConfirmation extends QNameAssertion {
   public static final String REQUIRE_SIGNATURE_CONFIRMATION = "RequireSignatureConfirmation";

   public QName getName() {
      return new QName(this.getNamespace(), "RequireSignatureConfirmation", "sp");
   }
}
