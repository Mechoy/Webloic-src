package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class MustSupportRefIssuerSerial extends QNameAssertion {
   public static final String MUST_SUPPORT_REF_ISSUER_SERIAL = "MustSupportRefIssuerSerial";

   public QName getName() {
      return new QName(this.getNamespace(), "MustSupportRefIssuerSerial", "sp");
   }
}
