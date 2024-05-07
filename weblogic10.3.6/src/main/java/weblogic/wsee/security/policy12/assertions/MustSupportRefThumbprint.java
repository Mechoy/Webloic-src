package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class MustSupportRefThumbprint extends QNameAssertion {
   public static final String MUST_SUPPORT_THUMB_PRINT_REF = "MustSupportRefThumbprint";

   public QName getName() {
      return new QName(this.getNamespace(), "MustSupportRefThumbprint", "sp");
   }
}
