package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class MustSupportIssuedTokens extends QNameAssertion {
   public static final String MUST_SUPPORT_ISSUED_TOKENS = "MustSupportIssuedTokens";

   public QName getName() {
      return new QName(this.getNamespace(), "MustSupportIssuedTokens", "sp");
   }
}
