package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class MustSupportRefKeyIdentifier extends QNameAssertion {
   public static final String MUST_SUPPORT_REF_KEY_IDENTIFIER = "MustSupportRefKeyIdentifier";

   public QName getName() {
      return new QName(this.getNamespace(), "MustSupportRefKeyIdentifier", "sp");
   }
}
