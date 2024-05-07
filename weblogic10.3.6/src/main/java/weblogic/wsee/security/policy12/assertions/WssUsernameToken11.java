package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class WssUsernameToken11 extends QNameAssertion {
   public static final String WSS_USERNAME_TOKEN_11 = "WssUsernameToken11";

   public QName getName() {
      return new QName(this.getNamespace(), "WssUsernameToken11", "sp");
   }
}
