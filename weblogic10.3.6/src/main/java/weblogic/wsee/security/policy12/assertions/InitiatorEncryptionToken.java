package weblogic.wsee.security.policy12.assertions;

import javax.xml.namespace.QName;

public class InitiatorEncryptionToken extends AsymmetricToken {
   public static final String INITIATOR_ENCRYPTION_TOKEN = "InitiatorEncryptionToken";

   public QName getName() {
      return new QName(this.getNamespace(), "InitiatorEncryptionToken", "sp");
   }
}
