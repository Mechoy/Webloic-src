package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class MustSupportClientChallenge extends QNameAssertion {
   public static final String MUST_SUPPORT_CLIENT_CHALLENGE = "MustSupportClientChallenge";

   public QName getName() {
      return new QName(this.getNamespace(), "MustSupportClientChallenge", "sp");
   }
}
