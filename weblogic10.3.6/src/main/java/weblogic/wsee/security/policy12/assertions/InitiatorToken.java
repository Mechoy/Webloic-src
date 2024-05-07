package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class InitiatorToken extends AsymmetricToken {
   public static final String INITIATOR_TOKEN = "InitiatorToken";

   public QName getName() {
      return new QName(this.getNamespace(), "InitiatorToken", "sp");
   }
}
