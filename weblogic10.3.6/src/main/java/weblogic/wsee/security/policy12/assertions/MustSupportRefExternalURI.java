package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class MustSupportRefExternalURI extends QNameAssertion {
   public static final String MUST_SUPPORT_REF_EXTERNAL_URI = "MustSupportRefExternalURI";

   public QName getName() {
      return new QName(this.getNamespace(), "MustSupportRefExternalURI", "sp");
   }
}
