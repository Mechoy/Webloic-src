package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class MustSupportInteractiveChallenge extends QNameAssertion {
   public static final String MUST_SUPPORT_INTERACTIVE_CHALLENGE = "MustSupportInteractiveChallenge";

   public QName getName() {
      return new QName(this.getNamespace(), "MustSupportInteractiveChallenge", "sp13");
   }
}
