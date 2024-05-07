package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class MustSupportRefEmbeddedToken extends QNameAssertion {
   public static final String MUST_SUPPORT_REF_EMBEDDED_TOKEN = "MustSupportRefEmbeddedToken";

   public QName getName() {
      return new QName(this.getNamespace(), "MustSupportRefEmbeddedToken", "sp");
   }
}
