package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class WssUsernameToken10 extends QNameAssertion {
   public static final String WSS_USERNAME_TOKEN_10 = "WssUsernameToken10";

   public QName getName() {
      return new QName(this.getNamespace(), "WssUsernameToken10", "sp");
   }
}
