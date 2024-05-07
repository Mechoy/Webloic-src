package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class MustSupportServerChallenge extends QNameAssertion {
   public static final String MUST_SUPPORT_SERVER_CHALLENGE = "MustSupportServerChallenge";

   public QName getName() {
      return new QName(this.getNamespace(), "MustSupportServerChallenge", "sp");
   }
}
