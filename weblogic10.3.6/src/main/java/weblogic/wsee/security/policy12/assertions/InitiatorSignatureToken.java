package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class InitiatorSignatureToken extends AsymmetricToken {
   public static final String INITIATOR_SIGNATURE_TOKEN = "InitiatorSignatureToken";

   public QName getName() {
      return new QName(this.getNamespace(), "InitiatorSignatureToken", "sp");
   }
}
